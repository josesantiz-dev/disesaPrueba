package net.giro.compras.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.compras.dao.OrdenCompraDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicComprasEventos;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.logica.MonedaRem;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.gson.Gson;

@Stateless
public class OrdenCompraFac implements OrdenCompraRem {
	private static Logger log = Logger.getLogger(OrdenCompraFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private OrdenCompraDAO ifzOrdenCompras;
	private OrdenCompraDetalleRem ifzOrdenDetalles;
	private CotizacionDetalleRem ifzCotDetalles;
	private AlmacenMovimientosRem ifzMovimientos;
	private MonedaRem ifzMonedas;
	private ConvertExt convertidor;
	private static String orderBy;
	private static Long estatus;
	
	
	public OrdenCompraFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
            
			this.ifzOrdenCompras = (OrdenCompraDAO) this.ctx.lookup("ejb:/Model_Compras//OrdenCompraImp!net.giro.compras.dao.OrdenCompraDAO");
			this.ifzOrdenDetalles = (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzCotDetalles = (CotizacionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzMovimientos = (AlmacenMovimientosRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzMonedas = (MonedaRem) this.ctx.lookup("ejb:/Logica_TYG//MonedaFac!net.giro.tyg.logica.MonedaRem");
            
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("OrdenCompraFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.OrdenCompraFac", e);
			ctx = null;
		}
	}
	
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { this.convertidor.setMostrarSystemOut(value); }

	@Override
	public void OrderBy(String orderBy) { OrdenCompraFac.orderBy = orderBy; }
	
	@Override
	public void estatus(Long estatus) { OrdenCompraFac.estatus = estatus; }

	@Override
	public Long save(OrdenCompra entity) throws ExcepConstraint {
		try {
			return this.ifzOrdenCompras.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.save(OrdenCompra)", e);
			throw e;
		}
	}

	@Override
	public Respuesta save(OrdenCompra entity, List<OrdenCompraDetalle> details) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(OrdenCompra entity) throws ExcepConstraint {
		try {
			this.ifzOrdenCompras.update(entity);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.update(OrdenCompra)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompra> saveOrUpdateList(List<OrdenCompra> entities) throws Exception {
		try {
			return this.ifzOrdenCompras.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.saveOrUpdateList(List<OrdenCompra> entities)", e);
			throw e;
		}
	}

	@Override
	public Respuesta cancelar(long idOrdenCompra, long idUsuario) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<OrdenCompraDetalle> listDetalles = null;
		List<AlmacenMovimientos> listMovs = null;
		OrdenCompra pojoOrden = null;
		
		try {
			this.ifzMovimientos.setInfoSesion(this.infoSesion);
			listMovs = this.ifzMovimientos.findByProperty("idOrdenCompra", idOrdenCompra);
			if (listMovs != null && ! listMovs.isEmpty()) {
				log.error("No se puede Cancelar la Orden de Compra porque ha sido suministrada total o parcialmente");
				respuesta.getBody().addValor("movimientos", listMovs);
				respuesta.getErrores().addCodigo("COMPRAS", 2L);
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se puede Cancelar la Orden de Compra porque ha sido suministrada total o parcialmente");
				return respuesta;
			}
			
			// Recupero los productos de la Orden de Compra para el BackOffice
			this.ifzOrdenDetalles.setInfoSesion(this.infoSesion);
			listDetalles = this.ifzOrdenDetalles.findAll(idOrdenCompra);
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.error("Ocurrio un problema al intentar recuperar el detalle de la Orden de Compra indicada");
				respuesta.getErrores().addCodigo("COMPRAS", 3L);
				respuesta.getErrores().setCodigoError(3L);
				respuesta.getErrores().setDescError("Ocurrio un problema al intentar recuperar los detalles de la Orden de Compra indicada");
				return respuesta;
			}
			
			// Cancela la Orden de Compra
			pojoOrden = this.ifzOrdenCompras.findById(idOrdenCompra);
			pojoOrden.setEstatus(1);
			pojoOrden.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			pojoOrden.setFechaModificacion(Calendar.getInstance().getTime());
			this.update(pojoOrden);
			
			// Envio mensaje a COMPRAS:Back Office Cotizacion
			boCotizacion(pojoOrden.getIdCotizacion(), 0L, listDetalles);
			
			respuesta.getBody().addValor("ordenCompra", pojoOrden);
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.cancelar(idOrdenCompra, idUsuario)", e);
			respuesta.getBody().addValor("exception", e);
			respuesta.getErrores().addCodigo("COMPRAS", 2L);
			respuesta.getErrores().setCodigoError(2L);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar Cancelar la Orden de Compra indicada");
		}
		
		return respuesta;
	}

	@Override
	public Respuesta cancelar(long idOrdenCompra, long idUsuario, boolean forzarCancelacion) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<OrdenCompraDetalle> listDetalles = null;
		List<AlmacenMovimientos> listMovs = null;
		OrdenCompra pojoOrden = null;
		
		try {
			if (! forzarCancelacion)
				return this.cancelar(idOrdenCompra, idUsuario);

			this.ifzMovimientos.setInfoSesion(this.infoSesion);
			listMovs = this.ifzMovimientos.findByProperty("idOrdenCompra", idOrdenCompra);
			if (listMovs != null && ! listMovs.isEmpty()) {
				for (AlmacenMovimientos mov : listMovs) {
					mov.setEstatus(1);
					mov.setModificadoPor(idUsuario);
					mov.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzMovimientos.update(mov);
				}
			}
			
			// Recupero los productos de la Orden de Compra para el BackOffice
			this.ifzOrdenDetalles.setInfoSesion(this.infoSesion);
			listDetalles = this.ifzOrdenDetalles.findAll(idOrdenCompra);
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.error("Ocurrio un problema al intentar recuperar el detalle de la Orden de Compra indicada");
				respuesta.getErrores().addCodigo("COMPRAS", 3L);
				respuesta.getErrores().setCodigoError(3L);
				respuesta.getErrores().setDescError("Ocurrio un problema al intentar recuperar los detalles de la Orden de Compra indicada");
				return respuesta;
			}
			
			// Cancela la Orden de Compra
			pojoOrden = this.ifzOrdenCompras.findById(idOrdenCompra);
			pojoOrden.setEstatus(1);
			pojoOrden.setModificadoPor(idUsuario);
			pojoOrden.setFechaModificacion(Calendar.getInstance().getTime());
			this.update(pojoOrden);
			
			// Envio mensaje a COMPRAS:Back Office Cotizacion
			boCotizacion(pojoOrden.getIdCotizacion(), 0L, listDetalles);
			
			respuesta.getBody().addValor("ordenCompra", pojoOrden);
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.cancelar(idOrdenCompra, idUsuario)", e);
			respuesta.getBody().addValor("exception", e);
			respuesta.getErrores().addCodigo("COMPRAS", 2L);
			respuesta.getErrores().setCodigoError(2L);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar Cancelar la Orden de Compra indicada");
		}
		
		return respuesta;
	}

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzOrdenCompras.delete(entity);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompra findById(Long id) {
		try {
			return this.ifzOrdenCompras.findById(id);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompra findByCodigo(String codigo) {
		try {
			return this.ifzOrdenCompras.findByCodigo(codigo, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findByCodigo(codigo)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompra> findByProperty(String propertyName, Object value, int limite) throws Exception {
		return this.findByProperty(propertyName, value, 0L, limite);
	}

	@Override
	public List<OrdenCompra> findByProperty(String propertyName, Object value, long idObra, int limite) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findByProperty(propertyName, value, idObra, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findByProperty(propertyName, value, idObra, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompra> findLike(String value) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findLike(value, 0L, true, 0, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findLike(String value)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		return this.findLikeProperty(propertyName, value, 0L, limite);
	}

	@Override
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, long idObra, int limite) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findLikeProperty(propertyName, value, idObra, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findLikeProperty(propertyName, value, idObra, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompra> findNoCompletas(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findNoCompletas(propertyName, value, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findNoCompletas(propertyName, value, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompra> findInProperty(String columnName, List<Object> values) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findInProperty(columnName, values, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findInProperty(columnName, values)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompra> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findByProperties(params, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public List<OrdenCompra> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			return this.ifzOrdenCompras.findLikeProperties(params, limite, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor) throws Exception {
		try {
			return this.ifzOrdenCompras.findConsecutivoByProveedor(idProveedor, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findConsecutivoByProveedor(idProveedor)", e);
			throw e;
		}
	}

	@Override
	public Respuesta procesarArchivo(byte[] fileSrc, HashMap<String, String> cellReference) throws Exception {
		HashMap<String, Respuesta> listRespuestas = null;
		Respuesta res = null;
		// ----------------------------------------------------------
		Respuesta resItem = null;
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		FormulaEvaluator eval;
		// ----------------------------------------------------------
		String valProds = null;
		String[] sections = null;
		String[] labels = null;
		String[] cols = null;
		int initProductos = -1;
		
		try {
			// Leemos archivo
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			eval = workbook.getCreationHelper().createFormulaEvaluator();
			sheet = workbook.getSheetAt(0);
			
			if (cellReference.containsKey("PRODUCTOS")) {
				valProds = cellReference.get("PRODUCTOS");
				sections = valProds.split("\\|");
				
				initProductos = Integer.parseInt(sections[0]);
				initProductos = initProductos - 1; // Correcion de inicio de productos por indice base 0
				labels = sections[1].split("\\,");
				cols = sections[2].split("\\,");
			}
			
			for (int indexSheet = 0; indexSheet < workbook.getNumberOfSheets(); indexSheet++) {
				sheet = workbook.getSheetAt(indexSheet);
				resItem = procesaHoja(sheet, eval, cellReference, valProds, sections, labels, cols, initProductos);
				resItem.getBody().addValor("hoja", sheet.getSheetName());
				
				if (res == null)
					res = resItem;
				
				if (listRespuestas == null)
					listRespuestas = new HashMap<String, Respuesta>();
				listRespuestas.put(sheet.getSheetName(), resItem);
			}
			
			// Añadimos el lote de resultados de todas las hojas procesadas (Incluida la inicial)
			res.getBody().addValor("respuestas", listRespuestas);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.procesarArchivo", e);
			res.setBody(null);
			res.getErrores().addCodigo("COMPRAS", 1L);
			res.getErrores().setDescError("Estructura o datos no son correctos");
		} 
		
		return res;
	}

	@Override
	public Respuesta procesarLibro(byte[] fileSrc, HashMap<String, String> cellReference) throws Exception {
		HashMap<String, Respuesta> listRespuestas = null;
		Respuesta res = null;
		// ----------------------------------------------------------
		Respuesta resItem = null;
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		FormulaEvaluator eval;
		// ----------------------------------------------------------
		List<String> colsRequeridos = null; 
		List<String> colsProductos = null;
		HashMap<String, String> colFacturas = null;
		// ----------------------------------------------------------
		/*String valProds = null;
		String[] sections = null;*/
		String[] labels = null;
		String[] cols = null;
		String celdaInicial = "";
		//int initProductos = -1;
		
		try {
			// Leemos archivo
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			eval = workbook.getCreationHelper().createFormulaEvaluator();
			sheet = workbook.getSheetAt(0);
			
			/*if (cellReference.containsKey("PRODUCTOS")) {
				valProds = cellReference.get("PRODUCTOS");
				sections = valProds.split("\\|");
				
				initProductos = Integer.parseInt(sections[0]);
				initProductos = initProductos - 1; // Correcion de inicio de productos por indice base 0
				labels = sections[1].split("\\,");
				cols = sections[2].split("\\,");
			}*/
			
			colsRequeridos 	= getColumnasRequeridos(sheet, cellReference);
			celdaInicial 	= getColumnasProductos(sheet, cellReference, colsProductos, labels, cols);
			colFacturas 	= getColumnasFacturas(sheet, cellReference);
			
			for (int indexSheet = 0; indexSheet < workbook.getNumberOfSheets(); indexSheet++) {
				sheet = workbook.getSheetAt(indexSheet);
				resItem = procesarHoja(sheet, eval, colsRequeridos, colFacturas, labels, cols, celdaInicial);
				resItem.getBody().addValor("hoja", sheet.getSheetName());

				if (res == null)
					res = resItem;
				
				if (listRespuestas == null)
					listRespuestas = new HashMap<String, Respuesta>();
				listRespuestas.put(sheet.getSheetName(), resItem);
			}
			
			// Añadimos el lote de resultados de todas las hojas procesadas (Incluida la inicial)
			res.getBody().addValor("respuestas", listRespuestas);
		} catch (Exception e) {
			res.setBody(null);
			res.getErrores().addCodigo("COMPRAS", 1L);
			res.getErrores().setDescError("Estructura o datos no son correctos");
			log.error("Error en Logica_Compras.OrdenCompraFac.procesarArchivo", e);
		} 
		
		return res;
	}

	@Override
	public List<Moneda> findMonedas() throws Exception {
		try {
			return this.ifzMonedas.findAll();
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findMonedas(columnName, values)", e);
			throw e;
		}
	}

	@Override
	public boolean backOfficeOrdenCompra(Long idOrdenCompra) {
		List<OrdenCompraDetalle> listDetalles = null;
		
		try {
			this.ifzOrdenDetalles.setInfoSesion(this.infoSesion);
			listDetalles = this.ifzOrdenDetalles.findAll(idOrdenCompra);
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.error("Ocurrio un problema al intentar recuperar el detalle de la Orden de Compra indicada");
				return false;
			}
			
			boOrdenCompra(idOrdenCompra, listDetalles);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.mensajeBOCotizacion(Long idOrdenCompra, Long idCotizacion)", e);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean backOfficeCotizacion(Long idOrdenCompra) {
		List<OrdenCompraDetalle> listDetalles = null;
		OrdenCompra pojoOrdenCompra = null;
		Long idCotizacion = 0L;
		
		try {
			this.ifzOrdenDetalles.setInfoSesion(this.infoSesion);
			pojoOrdenCompra = this.ifzOrdenCompras.findById(idOrdenCompra);
			idCotizacion = pojoOrdenCompra.getIdCotizacion();
			listDetalles = this.ifzOrdenDetalles.findAll(idOrdenCompra);
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.error("Ocurrio un problema al intentar recuperar el detalle de la Orden de Compra indicada");
				return false;
			}
			
			boCotizacion(idCotizacion, idOrdenCompra, listDetalles);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.mensajeBOCotizacion(Long idOrdenCompra, Long idCotizacion)", e);
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean backOfficeRequisicion(Long idOrdenCompra) {
		List<CotizacionDetalle> cotDetalles = null;
		OrdenCompra pojoOrdenCompra = null;
		Long idCotizacion = 0L;
		Long idRequisicion = 0L;
		
		try {
			this.ifzCotDetalles.setInfoSesion(this.infoSesion);
			pojoOrdenCompra = this.ifzOrdenCompras.findById(idOrdenCompra);
			idCotizacion = pojoOrdenCompra.getIdCotizacion();
			cotDetalles = this.ifzCotDetalles.findAll(idCotizacion);
			
			if (cotDetalles == null || cotDetalles.isEmpty()) {
				log.error("Ocurrio un problema al intentar recuperar el detalle de la Orden de Compra indicada");
				return false;
			}
			
			boRequisicion(idRequisicion, idCotizacion, cotDetalles);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.mensajeBOCotizacion(Long idOrdenCompra, Long idCotizacion)", e);
			return false;
		}
		
		return true;
	}

	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------

	@Override
	public Long save(OrdenCompraExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.OrdenCompraExtToOrdenCompra(entityExt));
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.save(OrdenCompraExt)", e);
			throw e;
		}
	}

	@Override
	public Respuesta save(OrdenCompraExt entity, List<OrdenCompraDetalleExt> details) throws Exception {
		return null;
	}

	@Override
	public void update(OrdenCompraExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.OrdenCompraExtToOrdenCompra(entityExt));
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.update(OrdenCompraExt)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraExt findExtById(Long id) throws Exception {
		try {
			OrdenCompra entity = this.findById(id);
			return this.convertidor.OrdenCompraToOrdenCompraExt(entity);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompraExt findExtByCodigo(String codigo) throws Exception {
		try {
			return this.convertidor.OrdenCompraToOrdenCompraExt(this.findByCodigo(codigo));
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findExtByCodigo(codigo)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompraExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		
		try {
			List<OrdenCompra> lista = this.findByProperty(propertyName, value, limite);
			if (lista != null) {
				for (OrdenCompra var : lista) {
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompraExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		
		try {
			List<OrdenCompra> lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null) {
				for (OrdenCompra var : lista) {
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}
	
	@Override
	public List<OrdenCompraExt> findNoCompletasExt(String propertyName, Object value, int limite) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		List<OrdenCompra> lista = null;
		
		try {
			this.ifzOrdenCompras.estatus(estatus);
			this.ifzOrdenCompras.OrderBy(orderBy);
			lista = this.ifzOrdenCompras.findNoCompletas(propertyName, value, limite, getIdEmpresa());
			if (lista != null && ! lista.isEmpty()) {
				for (OrdenCompra var : lista)
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
			}
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.findNoCompletasExt(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompraExt> findExtInProperty(String columnName, List<Object> values) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		
		try {
			List<OrdenCompra> lista = this.findInProperty(columnName, values);
			if (lista != null) {
				for (OrdenCompra var : lista) {
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenCompraFac.findExtInProperty(columnName, values)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompraExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		
		try {
			List<OrdenCompra> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for (OrdenCompra var : lista) {
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<OrdenCompraExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<OrdenCompraExt> listaExt = new ArrayList<OrdenCompraExt>();
		
		try {
			List<OrdenCompra> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for (OrdenCompra var : lista) {
					listaExt.add(this.convertidor.OrdenCompraToOrdenCompraExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	// ------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------
	
	private Respuesta procesarHoja(Sheet sheet, FormulaEvaluator eval, List<String> colsRequeridos2, HashMap<String, String> colFacturas, String[] labels, String[] celdasRequeridas, String celdaInicial) {
		Respuesta res = new Respuesta();
		HashMap<String, Object> encabezados = new HashMap<String, Object>();
		HashMap<String, String> productos = new HashMap<String, String>(); 
		HashMap<String, String> producto = new HashMap<String, String>(); 
		HashMap<String, HashMap<String, String>> sin_procesar = null; 
		HashMap<String, Double> facturas = null;
		// ----------------------------------------------------------
		Gson gson = new Gson(); 
		String json = "";
		// ----------------------------------------------------------
		Iterator<Row> rowIterator = null;
		Row row = null;
		Cell celda = null;
		CellValue cellValue = null;
		// ----------------------------------------------------------
		HashMap<String,String> colsRequeridos = null;
		HashMap<String,String> colsProductos = null;
		String valKeyMap = "";
		// ----------------------------------------------------------
		String codigo  = "";
		String refCell = "";
		int rowIndex = 0;
		int celIndex = 0;
		double cantidad = 0;
		double pu = 0;
		boolean terminaCarga = false;
		// ----------------------------------------------------------
		int iniProductos = 0;
		int finProductos = 0;
		//String valorFin = "";
		boolean endByBlank = false;
		
		try {
			rowIndex = -1;
			rowIterator = sheet.iterator();

			// Recuperamos valores para ENCABEZADOS
			colsRequeridos = new HashMap<String,String>();
			for (Entry<String,String> item : colsRequeridos.entrySet()) {
				row = sheet.getRow(getRowIndexFromCellReference(item.getKey()));
				celda = row.getCell(getCellIndexFromCellReference(item.getKey()));
				if (celda == null)
					continue;

				// Validamos celda
				if (Cell.CELL_TYPE_BLANK == celda.getCellType()) 
					continue;
				if (Cell.CELL_TYPE_STRING == celda.getCellType() && "".equals(celda.getStringCellValue())) 
					continue;
				
				switch (celda.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						encabezados.put(item.getValue(), celda.getNumericCellValue());
						break;
						
					case Cell.CELL_TYPE_FORMULA:
						cellValue = eval.evaluate(celda);
						encabezados.put(item.getValue(), ((cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) ? cellValue.getNumberValue() : cellValue.getStringValue().trim()));
						break;
						
					case Cell.CELL_TYPE_BOOLEAN:
						encabezados.put(item.getValue(), celda.getBooleanCellValue());
						break;
						
					default: break;
				}
			}
			
			// iteramos las filas
			rowIndex = -1;
			valKeyMap = "";
			rowIterator = sheet.iterator();
			iniProductos = getRowIndexFromCellReference(celdaInicial);
			for (rowIndex = 0; rowIterator.hasNext();) {
				row = rowIterator.next();
				rowIndex = row.getRowNum();
				
				if (terminaCarga)
					break; 
				
				if (! endByBlank && finProductos > 0 && rowIndex >= finProductos)
					break;
				
				if (rowIndex < iniProductos)
					continue;

				// Recuperamos valores para PRODUCTO
				colsProductos = new HashMap<String,String>();
				for (Entry<String,String> item : colsProductos.entrySet()) {
					celIndex = CellReference.convertColStringToIndex(item.getKey());
					celda = row.getCell(celIndex);
					if (celda == null)
						continue;
					
					if (endByBlank && celda.getCellType() == Cell.CELL_TYPE_BLANK) {
						terminaCarga = true;
						break;
					}
					
					valKeyMap = getCellReference(celda.getColumnIndex(), row.getRowNum());
					if (! item.getKey().equals(item.getValue()))
						valKeyMap = item.getValue();
					
					if (! item.getKey().equals(item.getValue()))
					switch (celda.getCellType()) {
						case Cell.CELL_TYPE_STRING: 
							producto.put(valKeyMap, celda.getStringCellValue().trim()); 
							break;
						case Cell.CELL_TYPE_NUMERIC:
							producto.put(valKeyMap, celda.getStringCellValue().trim()); 
							break;
						default:
							break;
					}
				}
				
				if (terminaCarga)
					continue;
				
				// Recuperamos valores para lista de FACTURAS del PRODUCTO, si corresponde
				if (colFacturas != null && ! colFacturas.isEmpty()) {
					for (Entry<String, String> item : colFacturas.entrySet()) {
						celIndex = CellReference.convertColStringToIndex(item.getKey());
						celda = row.getCell(celIndex);
						if (celda == null) 
							continue;
						
						if (celda.getCellType() != Cell.CELL_TYPE_NUMERIC)
							continue;

						if (facturas == null)
							facturas = new HashMap<String, Double>();
						facturas.put(item.getValue(), celda.getNumericCellValue());
					}
				}
				
				// Si los HashMap tienen el mismo tamaño
				if (producto.size() == colsProductos.size()) {
					codigo = producto.get("CODIGO");
					cantidad = (new BigDecimal(producto.get("CANTIDAD"))).doubleValue();
					pu = (new BigDecimal(producto.get("PRECIO"))).doubleValue();
					productos.put(codigo, cantidad + "|" + pu);
					
					// Añado las facturas si corresponde
					if (facturas != null && ! facturas.isEmpty()) {
						json = gson.toJson(facturas); 
						productos.put(codigo, cantidad + "|" + pu + "|" + json);
					}
				} else if (producto.size() != colsProductos.size() && producto.size() > 0) {
					// el producto no se puede procesar
					if (sin_procesar == null)
						sin_procesar = new HashMap<String, HashMap<String,String>>();
					sin_procesar.put("C" + (row.getRowNum() + 1), producto); // "C" + row.getRowNum() + 1 :: Correccion indice base 0
				} else {
					// renglon vacio, terminamos carga
					terminaCarga = true;
				}

				producto = null;
				facturas = null;
				
				/*
				// PRODUCTOS
				codigo = "";
				cantidad = 0;
				pu = 0;

				// Iteracion de columnas de productos
				producto = new HashMap<String, String>();
				for (int i = 0; i < celdasRequeridas.length; i++) {
					celIndex = getCellIndex(sheet, celdasRequeridas[i]);
					celda = row.getCell(celIndex);
					
					if (celda == null) 
						continue;
					
					switch (celda.getCellType()) {
						case Cell.CELL_TYPE_STRING: 
							producto.put(labels[i], celda.getStringCellValue().trim()); 
							break;
						case Cell.CELL_TYPE_NUMERIC:
							producto.put(labels[i], getStringValue(celda.getNumericCellValue()));
							break;
						default:
							break;
					}
				}
				
				// Iteramos la fila de facturas y recuperamos todas las registradas
				if (colFacturas != null && ! colFacturas.isEmpty()) {
					for (Entry<String, String> item : colFacturas.entrySet()) {
						celIndex = CellReference.convertColStringToIndex(item.getKey());
						celda = row.getCell(celIndex);
						
						if (celda == null) 
							continue;
						
						if (celda.getCellType() != Cell.CELL_TYPE_NUMERIC)
							continue;

						if (facturas == null)
							facturas = new HashMap<String, Double>();
						facturas.put(item.getValue(), celda.getNumericCellValue());
					}
				}
				
				// Si el HashMap tiene la misma longitud que las celdas requeridas
				if (producto.size() == celdasRequeridas.length) {
					codigo = producto.get("CODIGO");
					cantidad = (new BigDecimal(producto.get("CANTIDAD"))).doubleValue();
					pu = (new BigDecimal(producto.get("PRECIO"))).doubleValue();

					productos.put(codigo, cantidad + "|" + pu);
					if (facturas != null && ! facturas.isEmpty()) {
						json = gson.toJson(facturas); 
						productos.put(codigo, cantidad + "|" + pu + "|facturas|" + json);
					}
				} else if (producto.size() != celdasRequeridas.length && producto.size() > 0) {
					// el producto no se puede procesar
					if (sin_procesar == null)
						sin_procesar = new HashMap<String, HashMap<String,String>>();
					sin_procesar.put("C" + (row.getRowNum() + 1), producto); // "C" + row.getRowNum() + 1 :: Correccion indice base 0
				} else {
					// renglon vacio, terminamos carga
					terminaCarga = true;
				}

				producto = null;
				facturas = null;*/
			}
	
			res.getBody().addValor("encabezados", encabezados);
			res.getBody().addValor("productos", productos);
			res.getBody().addValor("sin_procesar", sin_procesar);
		} catch (Exception e) {
			refCell = getCellReference(celIndex, rowIndex);
			if (refCell != null && ! "".equals(refCell))
				refCell = ". Celda procesada " + refCell;
			log.error("Error en Logica_Compras.OrdenCompraFac.procesaHoja(). Hoja " + sheet.getSheetName() + refCell, e);
			res.setBody(null);
			res.getErrores().addCodigo("COMPRAS", 1L);
			res.getErrores().setDescError(sheet.getSheetName() + ": Estructura o datos no son correctos" + refCell);
		} 
		
		return res;
	}

	private Respuesta procesaHoja(Sheet sheet, FormulaEvaluator eval, HashMap<String, String> cellReference, String valProds, String[] sections, String[] labels, String[] celdasRequeridas, int initProductos) {
		Respuesta res = new Respuesta();
		HashMap<String, Object> encabezados = new HashMap<String, Object>();
		HashMap<String, String> productos = new HashMap<String, String>(); 
		HashMap<String, String> producto = new HashMap<String, String>(); 
		HashMap<String, HashMap<String, String>> sin_procesar = null; 
		HashMap<String, String> colFacturas = null;
		HashMap<String, Double> facturas = null;
		// ----------------------------------------------------------
		Gson gson = new Gson(); 
		String json = "";
		// ----------------------------------------------------------
		Iterator<Row> rowIterator = null;
		Iterator<Cell> cellIterator = null;
		Row row = null;
		Cell celda = null;
		CellValue cellValue = null;
		// ----------------------------------------------------------
		String codigo  = "";
		String refCell = "";
		int rowIndex = 0;
		int celIndex = 0;
		double cantidad = 0;
		double pu = 0;
		boolean terminaCarga = false;
		
		try {
			colFacturas = getColumnasFacturas(sheet, cellReference);
			
			// iteramos las filas
			rowIndex = -1;
			rowIterator = sheet.iterator();
			for (rowIndex = 0; rowIterator.hasNext();) {
				row = rowIterator.next();
				cellIterator = row.cellIterator();
				rowIndex = row.getRowNum();
				
				if (terminaCarga)
					break;
				
				// ENCABEZADO
				if (rowIndex < initProductos) { 
					// iteramos las columnas
					for (celIndex = 0; cellIterator.hasNext();) {
						celda = cellIterator.next();
						celIndex = celda.getColumnIndex();
	
						// Validamos celda
						if (Cell.CELL_TYPE_BLANK == celda.getCellType()) 
							continue;
						if (Cell.CELL_TYPE_STRING == celda.getCellType() && "".equals(celda.getStringCellValue())) 
							continue;
						
						// Validacion para encabezados
						refCell = getCellReference(celIndex, rowIndex);
						if (! cellReference.containsKey(refCell)) 
							continue;
						
						switch (celda.getCellType()) {
							case Cell.CELL_TYPE_NUMERIC:
								encabezados.put(cellReference.get(refCell), celda.getNumericCellValue());
								break;
								
							case Cell.CELL_TYPE_FORMULA:
								cellValue = eval.evaluate(celda);
								encabezados.put(cellReference.get(refCell), ((cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) ? cellValue.getNumberValue() : cellValue.getStringValue().trim()));
								break;
								
							case Cell.CELL_TYPE_BOOLEAN:
								encabezados.put(cellReference.get(refCell), celda.getBooleanCellValue());
								break;
								
							default:
								encabezados.put(cellReference.get(refCell), celda.getStringCellValue().trim());
								break;
						}
					}
				} 
	
				// PRODUCTOS
				if (rowIndex >= initProductos) {  
					codigo = "";
					cantidad = 0;
					pu = 0;
	
					// Iteracion de columnas de productos
					producto = new HashMap<String, String>();
					for (int i = 0; i < celdasRequeridas.length; i++) {
						celIndex = getCellIndex(sheet, celdasRequeridas[i]);
						celda = row.getCell(celIndex);
						
						if (celda == null) 
							continue;
						
						switch (celda.getCellType()) {
							case Cell.CELL_TYPE_STRING: 
								producto.put(labels[i], celda.getStringCellValue().trim()); 
								break;
							case Cell.CELL_TYPE_NUMERIC:
								producto.put(labels[i], getStringValue(celda.getNumericCellValue()));
								break;
							default:
								break;
						}
					}
					
					// Iteramos la fila de facturas y recuperamos todas las registradas
					if (colFacturas != null && ! colFacturas.isEmpty()) {
						for (Entry<String, String> item : colFacturas.entrySet()) {
							celIndex = CellReference.convertColStringToIndex(item.getKey());
							celda = row.getCell(celIndex);
							
							if (celda == null) 
								continue;
							
							if (celda.getCellType() != Cell.CELL_TYPE_NUMERIC)
								continue;

							if (facturas == null)
								facturas = new HashMap<String, Double>();
							facturas.put(item.getValue(), celda.getNumericCellValue());
						}
					}
					
					// Si el HashMap tiene la misma longitud que las celdas requeridas
					if (producto.size() == celdasRequeridas.length) {
						codigo = producto.get("CODIGO");
						cantidad = (new BigDecimal(producto.get("CANTIDAD"))).doubleValue();
						pu = (new BigDecimal(producto.get("PRECIO"))).doubleValue();
						productos.put(codigo, cantidad + "|" + pu);
						
						// Añado facturas si corresponde
						if (facturas != null && ! facturas.isEmpty()) {
							json = gson.toJson(facturas); 
							productos.put(codigo, cantidad + "|" + pu + "|" + json);
						}
					} else if (producto.size() != celdasRequeridas.length && producto.size() > 0) {
						// el producto no se puede procesar
						if (sin_procesar == null)
							sin_procesar = new HashMap<String, HashMap<String,String>>();
						sin_procesar.put("C" + (row.getRowNum() + 1), producto); // "C" + row.getRowNum() + 1 :: Correccion indice base 0
					} else {
						// renglon vacio, terminamos carga
						terminaCarga = true;
					}
	
					producto = null;
					facturas = null;
				} 
			}
	
			res.getBody().addValor("encabezados", encabezados);
			res.getBody().addValor("productos", productos);
			res.getBody().addValor("sin_procesar", sin_procesar);
		} catch (Exception e) {
			refCell = getCellReference(celIndex, rowIndex);
			if (refCell != null && ! "".equals(refCell))
				refCell = ". Celda procesada " + refCell;
			log.error("Error en Logica_Compras.OrdenCompraFac.procesaHoja(). Hoja " + sheet.getSheetName() + refCell, e);
			res.setBody(null);
			res.getErrores().addCodigo("COMPRAS", 1L);
			res.getErrores().setDescError(sheet.getSheetName() + ": Estructura o datos no son correctos" + refCell);
		} 
		
		return res;
	}
	
	private List<String> getColumnasRequeridos(Sheet sheet, HashMap<String, String> cellReference) {
		List<String> resultado = new ArrayList<String>();
		Pattern pattern = Pattern.compile("(^[a-zA-Z]{1,2})([1-9]*)$");
		Matcher matcher = null;
		
		for (Entry<String, String> item : cellReference.entrySet()) {
			matcher = pattern.matcher(item.getKey());
			if (matcher.find())
				resultado.add(matcher.group(1));
		}
		
		return resultado;
	}
	
	private String getColumnasProductos(Sheet sheet, HashMap<String, String> cellReference, List<String> resultado, String[] labels, String[] cols) {
		String celdaInicial = ""; //List<String> resultado = new ArrayList<String>();
		int initProductos = -1; //List<String> resultado = new ArrayList<String>();
		String[] splitted = null;
		String[] rango = null;
		Pattern pattern = Pattern.compile("(^[a-zA-Z]{1,2})([1-9]*)$");
		Matcher matcher = null;
		String valor = "";
		/*List<String> supplierCells = Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
		int iniCol = -1;
		int finCol = -1;*/
		
		if (! cellReference.containsKey("PRODUCTOS"))
			return "";
		/*
		 * Ejemplos para valor:
		 * 14|C:I|C,G,H|CODIGO,CANTIDAD,PRECIO			Iniciando en C14 hasta la celda sin valor (BLANK), tomo los valores de C14, G14 y H14, y aplico las etiquetas CODIGO, CANTIDAD y PRECIO
		 * 14|C:I|C,G,H									Iniciando en C14 hasta la celda sin valor (BLANK), tomo los valores de C14, G14 y H14
		 * 14|C:I|*										Iniciando en C14 hasta la celda sin valor (BLANK), tomo los valores de las columnas C, D, E, F, G, H y I				
		 * 14|C:I,$TOTAL|C,G,H|CODIGO,CANTIDAD,PRECIO	Iniciando en C14 hasta la celda con valor 'TOTAL', tomo los valores de C14, G14 y H14, y aplico las etiquetas CODIGO, CANTIDAD y PRECIO
		 * 14|C:I,16|C,G,H|CODIGO,CANTIDAD,PRECIO		Iniciando en C14 hasta la fila 16, tomo los valores de C14, G14 y H14, y aplico las etiquetas CODIGO, CANTIDAD y PRECIO
		 * 14|C:I,16|*									Iniciando en C14 hasta la fila 16, tomo los valores de las columnas C, D, E, F, G, H y I
		 */
		valor = cellReference.get("PRODUCTOS");
		splitted = valor.split("\\|");
		
		initProductos = Integer.parseInt(splitted[0]);
		initProductos = initProductos - 1; // Correcion de inicio de productos por indice base 0
		
		if (splitted.length >= 2 && ! "*".equals(splitted[3])) {
			// columnas especificas
			valor = splitted[2];
			splitted = valor.split("\\,");
			for (int i = 0; i < splitted.length; i++) {
				if ("".equals(celdaInicial))
					celdaInicial = splitted[i];
				resultado.add(splitted[i]);
			}
			
			return getCellReference(CellReference.convertColStringToIndex(celdaInicial), initProductos);
		}

		// rango de columnas
		valor = splitted[0];
		splitted = valor.split("\\,");
		for (int i = 0; i < splitted.length; i++) {
			rango = splitted[i].split("\\:");
			valor = rango[0];
			matcher = pattern.matcher(valor);
			if (matcher.find())
				resultado.add(matcher.group(1));
			if (rango.length > 1) {
				if (! rango[1].contains("$")) {
					valor = rango[1];
					matcher = pattern.matcher(valor);
					if (! matcher.find())
						continue;
					valor = matcher.group(1);
				}
			}
		}
		
		return getCellReference(CellReference.convertColStringToIndex(celdaInicial), initProductos);
	}
	
	private HashMap<String, String> getColumnasFacturas(Sheet sheet, HashMap<String, String> cellReference) {
		HashMap<String, String> mapFacturas = null;
		Row row = null;
		Iterator<Cell> cellIterator = null;
		Cell celda = null;
		// --------------------------------------------
		String[] splitted = null;
		String val = "";
		int initRow = 0;
		int celIndex = 0;
		int colInit = 0;
		int colEnd = 0;
		String initCol = "";
		String endCol = "";
		boolean endByValue = false;
		
		if (cellReference.containsKey("FACTURAS")) {
			val = cellReference.get("FACTURAS");
			splitted = val.split("\\|");
			
			initRow = Integer.parseInt(splitted[0]);
			initRow = initRow - 1; // Correcion de inicio de productos por indice base 0
			
			val = splitted[2];
			initCol = val;
			if (val.contains(":")) {
				splitted = val.split("\\:");
				initCol = splitted[0];
				endCol = splitted[1];
				if (endCol.contains("$")) {
					endCol = endCol.replace("$", "");
					endByValue = true;
				}
			}
			
			colInit = CellReference.convertColStringToIndex(initCol);
			row = sheet.getRow(initRow);
			cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				celda = cellIterator.next();
				celIndex = celda.getColumnIndex();
				//log.info("FACTURAS: Checking cell " + getCellReference(celIndex, initRow));
				
				if (celda == null || celda.getCellType() == Cell.CELL_TYPE_BLANK)
					continue;

				if (celda.getCellType() != Cell.CELL_TYPE_STRING)
					continue;

				if (celda.getStringCellValue() == null || "".equals(celda.getStringCellValue()))
					continue;
				
				if (celIndex < colInit)
					continue;
				
				if (endByValue && endCol.equals(celda.getStringCellValue()))
					break;
				
				if (mapFacturas == null)
					mapFacturas = new HashMap<String, String>();
				mapFacturas.put(CellReference.convertNumToColString(celIndex), celda.getStringCellValue());

				if (! endByValue && celIndex >= colEnd)
					break;
			}
		}
		
		return mapFacturas;
	}
	
	private String getCellReference(int col, int row) {
		return (new CellReference(row, col)).formatAsString().replace("$", "");
	}
	
	private String getStringValue(double value) {
		NumberFormat nf = DecimalFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(value).replace(",", "");
	}
	
	private int getRowIndexFromCellReference(String cellReference) {
		Pattern pattern = Pattern.compile("(^[a-zA-Z]{1,2})([1-9]*)$");
		Matcher matcher = null;
		int index = -1;
		
		matcher = pattern.matcher(cellReference);
		if (matcher.find())
			index = ((matcher.group(2) != null && ! "".equals(matcher.group(2).trim())) ? Integer.parseInt(matcher.group(2)) : index);
		return index;
	}
	
	private int getCellIndexFromCellReference(String cellReference) {
		Pattern pattern = Pattern.compile("(^[a-zA-Z]{1,2})([1-9]*)$");
		Matcher matcher = null;
		int index = -1;
		
		matcher = pattern.matcher(cellReference);
		if (matcher.find())
			index = ((matcher.group(1) != null && ! "".equals(matcher.group(1).trim())) ? CellReference.convertColStringToIndex(matcher.group(1)) : index);
		return index;
	}
	
	private int getCellIndex(Sheet sheet, String target) {
		String[] splitted = null;
		int celIndex = 0;
		
		if (! target.contains(":"))
			return CellReference.convertColStringToIndex(target);
		
		// Si contiene ':' significa que el valor puede estar en una o varias columnas ocultas, 
		// tomamos la primera visible o la ultima de la lista si no hay ninguna visible
		splitted = target.split("\\:");
		for (int x = 0; x < splitted.length; x++) {
			celIndex = CellReference.convertColStringToIndex(splitted[x]);
			if (! sheet.isColumnHidden(celIndex)) 
				break;
		}
		
		return celIndex;
	}
	
	private void boOrdenCompra(Long idOrdenCompra, List<OrdenCompraDetalle> listDetalles) {
		MensajeTopic msgCompras = null;
		Gson gson = new Gson();
		String comando = "";
		QueueConnectionFactory qcf = null;
		Connection conn = null;
		Session session = null;
		Topic topicCompras = null;
		TextMessage tm = null;
		MessageProducer sendtopic = null;
		Object tmp = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.warn("No se especificaron productos para el BackOffice Cotizacion");
				return;
			}
			
			if (idOrdenCompra == null)
				idOrdenCompra = 0L;
			
			gson = new Gson();
			target = idOrdenCompra.toString();
			atributos = gson.toJson(listDetalles);
			msgCompras = new MensajeTopic(TopicComprasEventos.BO_OC, target, referencia, atributos);
			comando = gson.toJson(msgCompras);
			
			tmp = this.ctx.lookup("ConnectionFactory");
			qcf = (QueueConnectionFactory) tmp;
			conn = qcf.createQueueConnection();
	
			// Instanciamos conectamos con TOPIC
			topicCompras = (Topic) this.ctx.lookup("topic/COMPRAS");
			session = conn.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			conn.start();
			
			// Creamos Producer y enviamos mensaje
			sendtopic = session.createProducer(topicCompras);
			tm = session.createTextMessage(comando);
			sendtopic.send(tm);
			
			// Cerramos conexiones
			conn.stop();
			session.close();
			conn.close();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-CO", e);
		}
	}
	
	private void boCotizacion(Long idCotizacion, Long idOrdenCompra, List<OrdenCompraDetalle> listDetalles) {
		MensajeTopic msgCompras = null;
		Gson gson = new Gson();
		String comando = "";
		QueueConnectionFactory qcf = null;
		Connection conn = null;
		Session session = null;
		Topic topicCompras = null;
		TextMessage tm = null;
		MessageProducer sendtopic = null;
		Object tmp = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.warn("No se especificaron productos para el BackOffice Cotizacion");
				return;
			}
			
			if (idOrdenCompra == null)
				idOrdenCompra = 0L;
			
			gson = new Gson();
			target = idCotizacion.toString();
			referencia = idOrdenCompra.toString();
			atributos = gson.toJson(listDetalles);
			msgCompras = new MensajeTopic(TopicComprasEventos.BO_CO, target, referencia, atributos);
			comando = gson.toJson(msgCompras);
			
			tmp = this.ctx.lookup("ConnectionFactory");
			qcf = (QueueConnectionFactory) tmp;
			conn = qcf.createQueueConnection();
	
			// Instanciamos conectamos con TOPIC
			topicCompras = (Topic) this.ctx.lookup("topic/COMPRAS");
			session = conn.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			conn.start();
			
			// Creamos Producer y enviamos mensaje
			sendtopic = session.createProducer(topicCompras);
			tm = session.createTextMessage(comando);
			sendtopic.send(tm);
			
			// Cerramos conexiones
			conn.stop();
			session.close();
			conn.close();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-CO", e);
		}
	}
	
	private void boRequisicion(Long idRequisicion, Long idCotizacion, List<CotizacionDetalle> listDetalles) {
		MensajeTopic msgCompras = null;
		Gson gson = new Gson();
		String comando = "";
		QueueConnectionFactory qcf = null;
		Connection conn = null;
		Session session = null;
		Topic topicCompras = null;
		TextMessage tm = null;
		MessageProducer sendtopic = null;
		Object tmp = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.warn("No se especificaron productos para el BackOffice Requisicion");
				return;
			}
			
			if (idCotizacion == null)
				idCotizacion = 0L;
			
			gson = new Gson();
			target = idRequisicion.toString();
			referencia = idCotizacion.toString();
			atributos = gson.toJson(listDetalles);
			msgCompras = new MensajeTopic(TopicComprasEventos.BO_RQ, target, referencia, atributos);
			comando = gson.toJson(msgCompras);
			
			tmp = this.ctx.lookup("ConnectionFactory");
			qcf = (QueueConnectionFactory) tmp;
			conn = qcf.createQueueConnection();
	
			// Instanciamos conectamos con TOPIC
			topicCompras = (Topic) this.ctx.lookup("topic/COMPRAS");
			session = conn.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			conn.start();
			
			// Creamos Producer y enviamos mensaje
			sendtopic = session.createProducer(topicCompras);
			tm = session.createTextMessage(comando);
			sendtopic.send(tm);
			
			// Cerramos conexiones
			conn.stop();
			session.close();
			conn.close();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-RQ", e);
		}
	}

	private Long getIdEmpresa() {
		Long idEmpresa = 1L;
		
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getId();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  2.2	| 29/04/2017 | Javier Tirado 			 | Corrigo el metodo procesarArchivo. Mayor precision con la referencia de celdas
 */