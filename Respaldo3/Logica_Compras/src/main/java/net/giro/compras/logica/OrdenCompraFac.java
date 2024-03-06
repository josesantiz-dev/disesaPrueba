package net.giro.compras.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.compras.comun.EstatusCompras;
import net.giro.compras.dao.OrdenCompraDAO;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosCompras;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.logica.MonedaRem;
import net.giro.tyg.logica.MonedasValoresRem;

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
import com.google.gson.reflect.TypeToken;

@Stateless
public class OrdenCompraFac implements OrdenCompraRem {
	private static Logger log = Logger.getLogger(OrdenCompraFac.class);
	private InfoSesion infoSesion;
	private OrdenCompraDAO ifzOrdenCompras;
	private OrdenCompraDetalleRem ifzOrdenDetalles;
	private AlmacenMovimientosRem ifzMovimientos;
	private MonedaRem ifzMonedas;
	private MonedasValoresRem ifzMonedasValores;
	private ConvertExt convertidor;
	
	public OrdenCompraFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzOrdenCompras = (OrdenCompraDAO) ctx.lookup("ejb:/Model_Compras//OrdenCompraImp!net.giro.compras.dao.OrdenCompraDAO");
			this.ifzOrdenDetalles = (OrdenCompraDetalleRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzMovimientos = (AlmacenMovimientosRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzMonedas = (MonedaRem) ctx.lookup("ejb:/Logica_TYG//MonedaFac!net.giro.tyg.logica.MonedaRem");
			this.ifzMonedasValores = (MonedasValoresRem) ctx.lookup("ejb:/Logica_TYG//MonedasValoresFac!net.giro.tyg.logica.MonedasValoresRem");
            
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("OrdenCompraFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.OrdenCompraFac", e);
		}
	}
	
	@Override
	public void showSystemOuts(boolean value) { 
		this.convertidor.setMostrarSystemOut(value); 
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { 
		this.infoSesion = infoSesion; 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(OrdenCompra entity) throws Exception {
		try {
			if (entity.getFolio() == null || "".equals(entity.getFolio().trim())) 
				entity = this.asignarFolio(entity);
			return this.ifzOrdenCompras.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.save(OrdenCompra)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(OrdenCompra entity) throws Exception {
		try {
			if (entity.getFolio() == null || "".equals(entity.getFolio().trim())) 
				entity = this.asignarFolio(entity);
			this.ifzOrdenCompras.update(entity);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.update(OrdenCompra)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<OrdenCompra> saveOrUpdateList(List<OrdenCompra> entities) throws Exception {
		try {
			return this.ifzOrdenCompras.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.saveOrUpdateList(List<OrdenCompra> entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta guardar(OrdenCompra entity) {
		Respuesta respuesta = null;
		
		try {
			respuesta = new Respuesta();
			// Asignamos Folio si corresponde
			if (entity.getFolio() == null || "".equals(entity.getFolio().trim())) 
				entity = this.asignarFolio(entity);
			
			// Guardamos Orden de Compra
			if (entity.getId() == null || entity.getId() <= 0L)
				entity.setId(this.save(entity));
			else
				this.update(entity);

			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
			respuesta.getBody().addValor("id", entity.getId());
			respuesta.getBody().addValor("folio", entity.getFolio());
			respuesta.getBody().addValor("entity", entity);
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar la Orden de Compra", e);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al guardar la Orden de Compra");
		}
		
		return respuesta;
	}

	@Override
	public Respuesta cancelar(long idOrdenCompra, long idUsuario) throws Exception {
		try {
			return this.cancelar(idOrdenCompra, idUsuario, false);
		} catch (Exception e) {
			log.error("error en Compras.OrdenCompraFac.cancelar(idOrdenCompra, idUsuario)", e);
			throw e;
		}
	}

	@Override
	public Respuesta cancelar(long idOrdenCompra, long idUsuario, boolean forzarCancelacion) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<OrdenCompraDetalle> listDetalles = null;
		List<AlmacenMovimientos> listMovs = null;
		OrdenCompra pojoOrden = null;
		HashMap<Long, Double> productos = null;
		
		try {
			this.ifzMovimientos.setInfoSesion(this.infoSesion);
			listMovs = this.ifzMovimientos.findByProperty("idOrdenCompra", idOrdenCompra);
			if (listMovs != null && ! listMovs.isEmpty()) {
				if (! forzarCancelacion) {
					log.error("No se puede Cancelar la Orden de Compra porque ha sido suministrada total o parcialmente");
					respuesta.getBody().addValor("movimientos", listMovs);
					respuesta.getErrores().addCodigo("COMPRAS", 2L);
					respuesta.getErrores().setCodigoError(2L);
					respuesta.getErrores().setDescError("No se puede Cancelar la Orden de Compra porque ha sido suministrada total o parcialmente");
					return respuesta;
				}
				
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
			pojoOrden.setEstatus(EstatusCompras.CANCELADA.ordinal());
			pojoOrden.setModificadoPor(idUsuario);
			pojoOrden.setFechaModificacion(Calendar.getInstance().getTime());
			this.update(pojoOrden);

			productos = new HashMap<Long, Double>();
			for (OrdenCompraDetalle item : listDetalles) 
				productos.put(item.getIdProducto(), item.getCantidad());
			
			// Envio mensaje a COMPRAS:Back Office Orden de Compra
			boOrdenCompra(idOrdenCompra, productos);
			
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
	public Respuesta autorizar(long idOrdenCompra, boolean forzarAutorizacion, boolean incluyeExtendido) {
		Respuesta respuesta = null;
		OrdenCompra pojoOrden = null;
		OrdenCompraExt extendido = null;
		
		try {
			respuesta = new Respuesta();
			idOrdenCompra = (idOrdenCompra > 0L ? idOrdenCompra : 0L);
			pojoOrden = this.ifzOrdenCompras.findById(idOrdenCompra);
			if (pojoOrden == null || pojoOrden.getId() == null || pojoOrden.getId() <= 0L) {
				log.error("Orden de Compra no encontrada");
				respuesta.getBody().addValor("idOrdenCompra", idOrdenCompra);
				respuesta.getErrores().addCodigo("COMPRAS", 2L);
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("Orden de Compra no encontrada");
				return respuesta;
			}
			
			if (pojoOrden.getAutorizado() == 1) {
				if (! forzarAutorizacion) {
					log.error("La Orden de Compra ya ha sido autorizada previamente");
					respuesta.getBody().addValor("idOrdenCompra", idOrdenCompra);
					respuesta.getBody().addValor("ordenCompra", pojoOrden);
					respuesta.getErrores().addCodigo("COMPRAS", 2L);
					respuesta.getErrores().setCodigoError(3L);
					respuesta.getErrores().setDescError("La Orden de Compra ya ha sido autorizada previamente");
					return respuesta;
				}
			}

			pojoOrden.setAutorizado(1);
			pojoOrden.setFechaAutorizacion(Calendar.getInstance().getTime());
			pojoOrden.setIdUsuarioAutorizo(this.infoSesion.getAcceso().getUsuario().getId());
			this.update(pojoOrden);
			
			if (incluyeExtendido) 
				extendido = this.findExtById(idOrdenCompra);

			respuesta.getBody().addValor("idOrdenCompra", idOrdenCompra);
			respuesta.getBody().addValor("ordenCompra", pojoOrden);
			respuesta.getBody().addValor("extendido", extendido);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("Orden de Compra autorizada");
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar Autorizar la Orden de Compra", e);
			respuesta.getBody().addValor("idOrdenCompra", idOrdenCompra);
			respuesta.getBody().addValor("exception", e);
			respuesta.getErrores().addCodigo("COMPRAS", 2L);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al Autorizar la Orden de Compra indicada");
		}
		
		return respuesta;
	}

	@Override
	public void delete(long idOrdenCompra) throws Exception {
		try {
			this.ifzOrdenCompras.delete(idOrdenCompra);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompra findById(long idOrdenCompra) {
		try {
			return this.ifzOrdenCompras.findById(idOrdenCompra);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findById(idOrdenCompra)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompra findByCodigo(String codigo) throws Exception {
		List<OrdenCompra> ordenes = null;
		try {
			ordenes = this.findByProperty("folio", codigo, 0);
			if (ordenes == null || ordenes.isEmpty())
				return null;
			return ordenes.get(0);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findByCodigo(codigo)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompra> findAll(long idObra) throws Exception {
		try {
			return this.findAll(idObra, false, false, "");
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findAll(idObra)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findAll(long idObra, boolean incluyeSistema, boolean incluyeCanceladas, String orderBy) throws Exception {
		try {
			return this.ifzOrdenCompras.findAll(idObra, incluyeSistema, incluyeCanceladas, orderBy);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findAll(idObra, estatus, orderBy)", e);
			throw e;
		} 
	}
	
	@Override
	public List<OrdenCompra> findLike(String value) throws Exception {
		try {
			return this.findLike(value, 0L, 0, false, false, false, "", 0);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findLike(String value)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findLike(String value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			tipoMaestro = (idObra <= 0L ? tipoMaestro : 0);
			value = (value.trim().contains(" ") ? value.trim().replace(" ", "%") : value.trim());
			return this.ifzOrdenCompras.findLike(value, idObra, tipoMaestro, autorizadas, incluyeSistema, incluyeCanceladas, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findLike(value, idObra, tipoMaestro, autorizadas, incluyeSistema, incluyeCanceladas, estatus, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, 0, false, false, false, "", limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findLikeProperty(propertyName, value, idObra, incluyeCanceladas, idEmpresa, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, long idObra, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, idObra, 0, false, false, false, "", limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findLikeProperty(propertyName, value, idObra, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, long idObra, boolean incluyeCanceladas, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, idObra, 0, false, false, incluyeCanceladas, "", limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findLikeProperty(propertyName, value, idObra, incluyeCanceladas, idEmpresa, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			tipoMaestro = (idObra <= 0L ? tipoMaestro : 0);
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), idObra, tipoMaestro, autorizadas, incluyeSistema, incluyeCanceladas, orderBy, limite);
			if (value.getClass() == java.lang.String.class)
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			return this.ifzOrdenCompras.findLikeProperty(propertyName, value, idObra, tipoMaestro, autorizadas, incluyeSistema, incluyeCanceladas, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findLikeProperty(propertyName, value, idObra, tipoMaestro, autorizadas, incluyeSistema, incluyeCanceladas, estatus, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompra> findByProperty(String propertyName, Object value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			tipoMaestro = (idObra <= 0L ? tipoMaestro : 0);
			return this.ifzOrdenCompras.findByProperty(propertyName, value, idObra, tipoMaestro, autorizadas, incluyeSistema, incluyeCanceladas, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findByProperty(propertyName, value, idObra, tipoMaestro, autorizadas, incluyeSistema, incluyeCanceladas, estatus, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, 1, false, false, false, "", limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findByProperty(propertyName, value, idObra, incluyeCanceladas, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findByProperty(String propertyName, Object value, long idObra, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, idObra, 1, false, false, false, "", limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findByProperty(propertyName, value, idObra, incluyeCanceladas, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findByProperty(String propertyName, Object value, long idObra, boolean incluyeCanceladas, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, idObra, 1, false, false, incluyeCanceladas, "", limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findByProperty(propertyName, value, idObra, incluyeCanceladas, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findByObra(long idObra, int estatus, boolean incluyeSistema, String orderBy, int limite) throws Exception {
		try {
			return this.ifzOrdenCompras.findByObra(idObra, estatus, incluyeSistema, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findByObra(idObra, estatus, incluyeSistema, idEmpresa, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<OrdenCompra> findByCotizacion(long idCotizacion, boolean incluyeCanceladas, int limite) throws Exception {
		try {
			return this.findByProperty("idCotizacion.id", idCotizacion, 0L, 0, false, false, incluyeCanceladas, "", limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findByCotizacion(idCotizacion, incluyeCanceladas, limite)", e);
			throw e;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public List<OrdenCompra> findNoCompletas(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.ifzOrdenCompras.findNoCompletas(propertyName, value, getIdEmpresa(), "", limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findNoCompletas(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<OrdenCompra> findNoAutorizadas(String property, Object value, long idObra, boolean incluyeCanceladas, String orderBy, int limite) throws Exception {
		try {
			return this.ifzOrdenCompras.findNoAutorizadas(property, value, idObra, incluyeCanceladas, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findNoCompletas(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor) throws Exception {
		try {
			return this.ifzOrdenCompras.findConsecutivoByProveedor(idProveedor, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findConsecutivoByProveedor(idProveedor)", e);
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
			this.ifzMonedas.setInfoSesion(this.infoSesion);
			return this.ifzMonedas.findAll();
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findMonedas()", e);
			throw e;
		}
	}

	@Override
	public double tipoCambio(long idMonedaOrigen, long idMonedaDestino, Date fecha) throws Exception {
		List<MonedasValores> valores = null;
		
		try {
			this.ifzMonedasValores.setInfoSesion(this.infoSesion);
			valores = this.ifzMonedasValores.findByFecha(idMonedaOrigen, idMonedaDestino, fecha, 0);
			if (valores != null && ! valores.isEmpty())
				return valores.get(0).getValor().doubleValue();
			return 1;
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.tipoCambio(idMonedaOrigen, idMonedaDestino, fecha)", e);
			throw e;
		}
	}
	
	@Override
	public List<OrdenCompra> busquedaDinamica(String value) throws Exception {
		try {
			return this.findLike(value, 0L, 1, false, false, false, "", 0);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.busquedaDinamica(value)", e);
			throw e;
		}
	}

	@Override
	public OrdenCompra asignarFolio(OrdenCompra entity) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("yy");
		int consecutivo = 0;
		String folio = "";
		//-------------------------------------------------------
		String idPro = "";
		String annio = "";
		String numero = "";
		
		try {
			if (entity.getIdProveedor() == null && entity.getIdProveedor() <= 0L)
				return entity;

			annio = formateador.format(Calendar.getInstance().getTime());
			idPro = String.valueOf(entity.getIdProveedor()).substring(1);
			idPro = String.valueOf(Long.parseLong(idPro)); 
			if (idPro.length() < 4) {
				idPro = String.valueOf(entity.getIdProveedor()).substring(1);
				idPro = idPro.substring(idPro.length() - 4);
			}

			consecutivo = this.findConsecutivoByProveedor(entity.getIdProveedor());
			numero = ((consecutivo < 10) ? "0" : "") + consecutivo;
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar el Folio para la Orden de Compra", e);
		} finally {
			folio = idPro + "-" + annio + "-" + numero;
			entity.setFolio(folio);
			entity.setConsecutivoProveedor(consecutivo);
		}
		
		return entity;
	}

	@Override
	public boolean backOfficeOrdenCompra(long idOrdenCompra) {
		List<OrdenCompraDetalle> listDetalles = null;
		HashMap<Long, Double> productos = null;
		
		try {
			if (this.infoSesion == null ) {
				log.error("Ocurrio un problema al validar la Sesion de Usuario");
				return false;
			}
			
			if (idOrdenCompra <= 0L) {
				log.error("No se especificaron Orden de Compra para el BackOffice Cotizacion");
				return false;
			}
			
			this.ifzOrdenDetalles.setInfoSesion(this.infoSesion);
			listDetalles = this.ifzOrdenDetalles.findAll(idOrdenCompra);
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.error("Ocurrio un problema al intentar recuperar el detalle de la Orden de Compra indicada");
				return false;
			}
			
			productos = new HashMap<Long, Double>();
			for (OrdenCompraDetalle item : listDetalles) 
				productos.put(item.getIdProducto(), item.getCantidad());
			boOrdenCompra(idOrdenCompra, productos);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.mensajeBOCotizacion(Long idOrdenCompra, Long idCotizacion)", e);
			return false;
		}
		
		return true;
	}

	// ------------------------------------------------------------------------------------------------------
	// BACKOFFICE
	// ------------------------------------------------------------------------------------------------------

	private void boOrdenCompra(Long idOrdenCompra, HashMap<Long, Double> productos) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		Type tipo = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			gson = new Gson();
			tipo = new TypeToken<HashMap<Long, Double>>() {}.getType();
			target = idOrdenCompra.toString();
			atributos = gson.toJson(productos, tipo);
			
			msgTopic = new MensajeTopic(TopicEventosCompras.ORDEN_ESTATUS, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-OC\n\n" + comando + "\n\n", e);
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// CONVERTIDOR
	// ------------------------------------------------------------------------------------------------------

	@Override
	public OrdenCompra convertir(OrdenCompraExt extendido) throws Exception {
		return this.convertidor.OrdenCompraExtToOrdenCompra(extendido);
	}

	@Override
	public OrdenCompraExt convertir(OrdenCompra entity) throws Exception {
		return this.convertidor.OrdenCompraToOrdenCompraExt(entity);
	}
	
	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------

	@Override
	public Long save(OrdenCompraExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.OrdenCompraExtToOrdenCompra(extendido));
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.save(extendido)", e);
			throw e;
		}
	}

	@Override
	public void update(OrdenCompraExt extendido) throws Exception {
		try {
			this.update(this.convertidor.OrdenCompraExtToOrdenCompra(extendido));
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.update(extendido)", e);
			throw e;
		}
	}

	@Override
	public Respuesta guardar(OrdenCompraExt extendido) {
		Respuesta respuesta = null;
		OrdenCompra entity = null;
		
		try {
			entity = this.convertidor.OrdenCompraExtToOrdenCompra(extendido);
			respuesta = this.guardar(entity);
			
			// Recuperamos extendido
			entity = (OrdenCompra) respuesta.getBody().getValor("entity");
			extendido = this.findExtById(entity.getId());
			respuesta.getBody().addValor("extendido", extendido);
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar la Orden de Compra", e);
			respuesta.getErrores().setCodigoError(2L);
			respuesta.getErrores().setDescError("Ocurrio un problema al guardar la Orden de Compra");
		}
		
		return respuesta;
	}

	@Override
	public OrdenCompraExt findExtById(long idOrdenCompra) throws Exception {
		try {
			return this.convertidor.OrdenCompraToOrdenCompraExt(this.findById(idOrdenCompra));
		} catch (Exception e) {
			log.error("Error en Logica_Compras.OrdenCompraFac.findExtById(idOrdenCompra)", e);
			throw e;
		}
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
	
	private Long getIdEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L);
	}

	private Long getCodigoEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  2.2	| 29/04/2017 | Javier Tirado 			 | Corrigo el metodo procesarArchivo. Mayor precision con la referencia de celdas
 */