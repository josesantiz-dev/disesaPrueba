package net.giro.inventarios.logica;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.dao.ProductoDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;

@Stateless
public class ProductoFac implements ProductoRem {
	private static Logger log = Logger.getLogger(ProductoFac.class);
	private InfoSesion infoSesion;
	private ProductoDAO ifzProductos;
	private ConValoresRem ifzConValores;
	private MonedaDAO ifzMonedas;
	private ConvertExt convertidor;
	// ---------------------------------------------------------------
	private ConGrupoValores unidadesMedida;
	private ConGrupoValores familias;
	private String modulo = "PRODUCTOS";
	// ---------------------------------------------------------------
	private LinkedHashMap<String, String> maestroLayoutReference;
	private boolean useMapReference;
	// Formato de celdas
	// ---------------------------------------------------------------
	private CellStyle sHeader = null;
	private CellStyle sNormalLeft = null;
	private CellStyle sNormalCenter = null;
	
	public ProductoFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    		ctx = new InitialContext(environment);
            this.ifzProductos = (ProductoDAO) ctx.lookup("ejb:/Model_Inventarios//ProductoImp!net.giro.inventarios.dao.ProductoDAO");
            this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
            this.ifzMonedas = (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar el EJB", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(Producto entity) throws Exception {
		try {
			if (entity == null)
				return 0L;
			entity.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaCreacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			return this.ifzProductos.save(entity, getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Producto> saveOrUpdateList(List<Producto> entities) throws Exception {
		List<Producto> stored = null;
		List<Producto> aux = null;
		double factor = 0.001;
		int lastIndex = 0;
		int limit = 1000;
		int laps = 0;
		int lap = 0;
		
		try {
			if (entities == null || entities.isEmpty())
				return null;
			for (Producto entity : entities) {
				if (entity.getId() == null || entity.getId() <= 0L) {
					entity.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
					entity.setFechaCreacion(Calendar.getInstance().getTime());
				}
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
				entity.setFechaModificacion(Calendar.getInstance().getTime());
			}
			
			if (entities.size() <= limit)
				return this.ifzProductos.saveOrUpdateList(entities, getCodigoEmpresa());
			
			stored = new ArrayList<Producto>();
			laps = (int) Math.ceil(entities.size() * factor);
			
			do {
				aux = new ArrayList<Producto>();
				lastIndex = (lap * limit) + limit;
				lastIndex = lastIndex > entities.size() ? entities.size() : lastIndex;
				for (int index = (lap * limit); index < lastIndex; index++) 
					aux.add(entities.get(index));
				stored.addAll(this.ifzProductos.saveOrUpdateList(aux, getCodigoEmpresa()));
				lap++;
			} while (lap < laps);
			
			return stored;
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(Producto entity) throws Exception {
		try {
			if (entity == null)
				return;
			if (entity.getId() == null || entity.getId() <= 0L) {
				this.save(entity);
				return;
			}
			
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzProductos.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Producto cancelar(long idProducto) throws Exception {
		Producto entity = null;
		
		try {
			entity = this.findById(idProducto);
			if (entity == null || entity.getId() == null || entity.getId() <= 0L)
				return null;
			entity.setEstatus(1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			if (entity.getId() != null && entity.getId() > 0L) 
				this.ifzProductos.update(entity);
			return entity;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(long idProducto) throws Exception {
		try {
			this.ifzProductos.delete(idProducto);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Producto findById(Long idProducto) {
		try {
			return this.ifzProductos.findById(idProducto);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Producto findByClave(String clave) {
		List<Producto> lista = null;
		
		try {
			lista = this.ifzProductos.findByClave(clave, true, false, "model.clave, model.id desc", getIdEmpresa()); // (clave, getIdEmpresa());
			if (lista != null && ! lista.isEmpty())
				return lista.get(0);
			return null;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Producto> findByClaveRango(String prefix, int inicioRango, int limiteRango) throws Exception {
		try {
			return this.ifzProductos.findByClaveRango(prefix, inicioRango, limiteRango, getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	public List<Producto> findAll() {
		try {
			return this.ifzProductos.findAll(false, false, "", getIdEmpresa());
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findAll(boolean incluyeCancelados, boolean incluyeOcultos, String orderBy) {
		try {
			return this.ifzProductos.findAll(incluyeCancelados, incluyeOcultos, orderBy, getIdEmpresa());
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findLike(String value, long idFamilia, int tipoMaestro, String orderBy, int limite) {
		try {
			return this.findLike(value, idFamilia, tipoMaestro, false, false, orderBy, limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findLike(String value, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, String orderBy, int limite) {
		try {
			while (value.trim().contains("  "))
				value = value.trim().replace("  ", " ");
			value = (value.trim().contains("+") ? value.trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.trim());
			value = (value.trim().contains("|") ? value.trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.trim());
			value = (value.trim().contains(" ") ? value.trim().replace(" ", "%") : value.trim());
			
			return this.ifzProductos.findLike(value, idFamilia, tipoMaestro, incluyeCancelados, incluyeOcultos, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperty(String propertyName, Object value, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, String orderBy, int limite) {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), idFamilia, tipoMaestro, incluyeCancelados, incluyeOcultos, orderBy, limite);
			
			if (value instanceof java.lang.String) {
				while (value.toString().trim().contains("  "))
					value = value.toString().trim().replace("  ", " ");
				value = (value.toString().trim().contains("+") ? value.toString().trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.toString().trim());
				value = (value.toString().trim().contains("|") ? value.toString().trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.toString().trim());
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			}
			
			return this.ifzProductos.findLikeProperty(propertyName, value, idFamilia, tipoMaestro, incluyeCancelados, incluyeOcultos, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperty(String propertyName, Object value, long idFamilia, int tipoMaestro, String orderBy, int limite) {
		try {
			return this.findLikeProperty(propertyName, value, idFamilia, tipoMaestro, false, false, orderBy, limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperty(String propertyName, Object value, int limite) {
		try {
			return this.findLikeProperty(propertyName, value, 0L, 0, false, false, "", limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperty(String propertyName, Object value, Long idFamilia, int limite) {
		try {
			return this.findLikeProperty(propertyName, value, idFamilia, 0, "", limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findByProperty(String propertyName, Object propertyValue, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, String orderBy, int limite) {
		try {
			return this.ifzProductos.findByProperty(propertyName, propertyValue, idFamilia, tipoMaestro, incluyeCancelados, incluyeOcultos, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Producto> findByProperty(String propertyName, Object propertyValue, long idFamilia, int tipoMaestro, String orderBy, int limite) {
		try {
			return this.findByProperty(propertyName, propertyValue, idFamilia, tipoMaestro, false, false, orderBy, limite);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, int limite) throws Exception {
		try {
			return this.ifzProductos.findLikeProperties(params, operador, getIdEmpresa(), limite);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, int limite, String orderBy) throws Exception {
		try {
			return this.ifzProductos.findLikeProperties(params, operador, orderBy, getIdEmpresa(), limite);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperties(HashMap<String, String> params, int tipo, int limite, String orderBy) throws Exception {
		try {
			return this.ifzProductos.findLikeProperties(params, tipo, orderBy, getIdEmpresa(), limite);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Producto> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			return this.findByProperties(params, "", limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Producto> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception {
		try {
			return this.ifzProductos.findByProperties(params, orderBy, getIdEmpresa(), limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Producto> findList(List<Long> idProductos) throws Exception {
		try {
			return this.ifzProductos.findList(idProductos);
		} catch (Exception re) {
			throw re;
		}
	}

	// --------------------------------------------------------------------------------------------

	@Override
	public boolean validarClaveProducto(Long idProducto, String clave) { 
		List<Producto> productos = null;
		
		idProducto = (idProducto != null && idProducto > 0L ? idProducto : 0L);
		clave = (clave != null && ! "".equals(clave.trim()) ? clave.trim() : "");
		if ("".equals(clave))
			return true;

		productos = this.ifzProductos.findByClave(clave, true, false, "model.clave, model.id desc", getIdEmpresa()); //(clave, getIdEmpresa());
		if (productos == null || productos.isEmpty())
			return false;
		
		if (idProducto > 0L) { 
			for (Producto var : productos) {
				if (! idProducto.equals(var.getId()))
					return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean validarClaveProducto(Producto pojoProducto) { 
		if (pojoProducto != null)
			return validarClaveProducto(pojoProducto.getId(), pojoProducto.getClave());
		return true;
	}

	@Override
	public boolean validarClaveProducto(ProductoExt pojoProducto) { 
		if (pojoProducto != null)
			return validarClaveProducto(pojoProducto.getId(), pojoProducto.getClave());
		return true;
	}

	@Override
	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension, ConGrupoValores unidadesMedida, ConGrupoValores familias, LinkedHashMap<String, String> maestroLayoutReference) throws Exception {
		this.maestroLayoutReference = maestroLayoutReference;
		return analizarArchivo(fileSrc, fileExtension, unidadesMedida, familias);
	}
	
	@Override
	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension, ConGrupoValores unidadesMedida, ConGrupoValores familias) throws Exception {
		Respuesta resp = new Respuesta();
		List<Producto> listItems = null;
		HashMap<Integer, String> mapNoProcesados = new HashMap<Integer, String>();
		
		try {
			log.info("---> Leyendo archivo ...");
			this.unidadesMedida = unidadesMedida;
			this.familias = familias;
			listItems = leerEXCEL(fileSrc, fileExtension, mapNoProcesados);
			if (listItems == null || listItems.isEmpty() || ! mapNoProcesados.isEmpty()) {
				resp.getErrores().addCodigo(modulo, 2L);
				if (! mapNoProcesados.isEmpty()) {
					log.error("---> Productos sin procesar " + mapNoProcesados.toString());
					resp.getErrores().setDescError("Error en datos de productos. Falta informacion.");
				} else {
					log.error("Error en formato del archivo EXCEL (*." + fileExtension + ")");
					resp.getErrores().setDescError("Error en formato.");
				}
			}

			log.info("     ... " + listItems.size() + " productos leidos");
			resp.getBody().addValor("productos", listItems);
			resp.getBody().addValor("sinprocesar", mapNoProcesados);
		} catch (Exception e) {
			log.error("Error en formato del archivo EXCEL: *." + fileExtension, e);
			resp.getErrores().addCodigo(modulo, 1L);
			resp.setBody(null);
		}
		
		return resp;
	}
	
	@Override
	public List<Producto> comprobarProductos(List<Producto> listItems) {
		List<Producto> listProductos = new ArrayList<Producto>();
		Producto pojoProducto = null;
		int itemIndex = -1;
		
		try {
			for (Producto var : listItems) {
				itemIndex++;
				if (var.getClave() == null) 
					continue;
				
				pojoProducto = comprobarProductoPorClave(var.getId(), var.getClave());
				if (pojoProducto == null) { // Guardamos nuevo --> VAR
					var.setId(0L);
					var.setCreadoPor(infoSesion.getAcceso().getId());
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(infoSesion.getAcceso().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
				} else { // Actualizamos producto --> pojoProducto;
					var.setId(pojoProducto.getId());
					var.setCreadoPor(pojoProducto.getCreadoPor());
					var.setFechaCreacion(pojoProducto.getFechaCreacion());
					var.setModificadoPor(infoSesion.getAcceso().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
				}
				
				// Si el producto es nuevo o si producto tiene algun cambio, lo añadimos a la lista
				if (comprobarModificado(var, pojoProducto))
					listProductos.add(var);
			}
			
			log.info("          -> productos comprobados     : " + listItems.size());
			log.info("          -> productos para guardar    : " + listProductos.size());
			log.info("          -> productos sin cambios     : " + (listItems.size() - listProductos.size()));
			log.info(" ");
		} catch (Exception e) {
			log.error("Error al Comprobar: Producto " + itemIndex, e);
			throw e;
		}
		
		return listProductos;
	}
	
	@Override
	public Respuesta actualizarProductos(byte[] fileSrc, String fileExtension, ConGrupoValores unidadesMedida, ConGrupoValores familias) throws Exception {
		Respuesta resp = new Respuesta();
		HashMap<Integer, String> mapNoProcesados = new HashMap<Integer, String>();
		boolean comprobandoProductos = false;
		int itemIndex = -1;
	
		try {
			if (fileExtension == null || "".equals(fileExtension))
				fileExtension = "xls";
			
			this.unidadesMedida = unidadesMedida;
			this.familias = familias;

			log.info("Leyendo archivo...");
			List<Producto> listItems = leerEXCEL(fileSrc, fileExtension, mapNoProcesados);
			if (listItems == null || listItems.isEmpty() || ! mapNoProcesados.isEmpty()) {
				resp.setBody(null);
				resp.getErrores().addCodigo(modulo, 2L);
				if (! mapNoProcesados.isEmpty()) {
					resp.getErrores().setDescError("Error en datos de productos. Falta informacion.");
					log.error("---> Productos sin procesar " + mapNoProcesados.toString());
				} else {
					resp.getErrores().setDescError("Error en formato.");
					log.error("Error en formato del archivo EXCEL (*." + fileExtension + ")");
				}
				return resp;
			}

			log.info("..." + listItems.size() + " archivos leidos");
			log.info("Comprobando productos...");
			Producto pojoProducto = null;
			for (Producto var : listItems) {
				comprobandoProductos = true;
				itemIndex++;
				if (String.valueOf(itemIndex).length() >= 4 && "000".equals(String.valueOf(itemIndex).substring(String.valueOf(itemIndex).length() - 3))) 
					log.info("... " + itemIndex + " productos comprobados");
				
				if (var.getClave() == null) 
					continue;
				
				pojoProducto = comprobarProductoPorClave(var.getId(), var.getClave());
				if (pojoProducto == null) { // Guardamos nuevo --> VAR
					var.setId(0L);
					var.setCreadoPor(infoSesion.getAcceso().getId());
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(infoSesion.getAcceso().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
				} else { // Actualizamos producto --> pojoProducto;
					var.setId(pojoProducto.getId());
					var.setCreadoPor(pojoProducto.getCreadoPor());
					var.setFechaCreacion(pojoProducto.getFechaCreacion());
					var.setModificadoPor(infoSesion.getAcceso().getId());
					var.setFechaModificacion(Calendar.getInstance().getTime());
				}
			}
			comprobandoProductos = false;
			log.info("... Todos los productos comprobados");
			
			// Generamos la respuesta
			resp.getBody().addValor("productos", listItems);
			resp.getBody().addValor("sinprocesar", mapNoProcesados);
		} catch (Exception e) {
			resp.setBody(null);
			resp.getErrores().addCodigo(modulo, 1L);
			if (comprobandoProductos)
				log.error("Error al comprobar los productos. Comprobando el producto " + itemIndex);
			log.error("Error en formato del archivo EXCEL (*." + fileExtension + "). ITEM: " + itemIndex, e);
			throw e;
		}
		
		return resp;
	}

	@Override
	public Respuesta exportarMaestro(List<Producto> listProductos, String nombreMaestro) throws Exception {
		Respuesta respuesta = new Respuesta();
		ByteArrayOutputStream memoryStream = null;
		HSSFWorkbook libro = null;
		HSSFSheet hoja = null;
	    HSSFRow row = null;
	    HSSFCell celda = null;
	    int rowIndex = 0;
	    int celIndex = 0;
		
		try {
			log.info("Comprobamos lista de productos para exportar");
			if (listProductos == null || listProductos.isEmpty()) {
				respuesta.setBody(null);
				respuesta.getErrores().addCodigo(modulo, 1L);
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("La busqueda no genero resultados");
				return respuesta;
			}

			log.info("Extendemos listado de productos");
			List<ProductoExt> listExtendidos = this.extenderLista(listProductos);
			if (listExtendidos == null || listExtendidos.isEmpty()) {
				log.info("Ocurrio un problema al extender la lista de productos para exportar");
				respuesta.setBody(null);
				respuesta.getErrores().addCodigo(modulo, 1L);
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("Ocurrio un problema al intentar extender la lista de productos");
				return respuesta;
			}
			
			if (nombreMaestro == null || "".equals(nombreMaestro))
				nombreMaestro = "MAESTRO";

            // Generamos libro, hoja y estilos de celdas
	        libro = new HSSFWorkbook();
	        hoja = libro.createSheet(nombreMaestro);
	        row = hoja.createRow(rowIndex);
            generaCellStyles(libro);
	        
	        // Creamos ENCABEZADOS
			log.info("Generamos encabezados");
	        celIndex = CellReference.convertColStringToIndex("A");
	        celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
			celda.setCellValue("ID");
			celda.setCellStyle(sHeader);
	
	        celIndex = CellReference.convertColStringToIndex("B");
	        celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
			celda.setCellValue("CLAVE");
			celda.setCellStyle(sHeader);
	
	        celIndex = CellReference.convertColStringToIndex("C");
	        celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
			celda.setCellValue("DESCRIPCION");
			celda.setCellStyle(sHeader);
	
	        celIndex = CellReference.convertColStringToIndex("D");
	        celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
			celda.setCellValue("UNIDAD MEDIDA");
			celda.setCellStyle(sHeader);
	
	        celIndex = CellReference.convertColStringToIndex("E");
	        celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
			celda.setCellValue("ESPECIALIDAD");
			celda.setCellStyle(sHeader);
	
	        celIndex = CellReference.convertColStringToIndex("F");
	        celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
			celda.setCellValue("FAMILIA");
			celda.setCellStyle(sHeader);
	
	        celIndex = CellReference.convertColStringToIndex("G");
	        celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
			celda.setCellValue("SUBFAMILIA");
			celda.setCellStyle(sHeader);
	
	        celIndex = CellReference.convertColStringToIndex("H");
	        celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
			celda.setCellValue("CLAVE SAT");
			celda.setCellStyle(sHeader);
	
	        celIndex = CellReference.convertColStringToIndex("I");
	        celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
			celda.setCellValue("MONEDA");
			celda.setCellStyle(sHeader);
			
			// 	PRODUCTOS
			log.info("Agregamos lsitado de productos");
			for (ProductoExt var : listExtendidos) {
				rowIndex += 1;
	            row = hoja.createRow(rowIndex);
				
	            celIndex = CellReference.convertColStringToIndex("A");
	            celda = row.createCell(celIndex, Cell.CELL_TYPE_NUMERIC);
	    		celda.setCellValue(var.getId());
	    		celda.setCellStyle(sNormalCenter);
	
	            celIndex = CellReference.convertColStringToIndex("B");
	            celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
	    		celda.setCellValue(var.getClave());
	    		celda.setCellStyle(sNormalCenter);
	
	            celIndex = CellReference.convertColStringToIndex("C");
	            celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
	    		celda.setCellValue(var.getDescripcion());
	    		celda.setCellStyle(sNormalLeft);
	
	            celIndex = CellReference.convertColStringToIndex("D");
	            celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
	    		celda.setCellValue(var.getDescUnidadMedida());
	    		celda.setCellStyle(sNormalLeft);
	
	            celIndex = CellReference.convertColStringToIndex("E");
	            celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
	    		celda.setCellValue(var.getDescEspecialidad());
	    		celda.setCellStyle(sNormalLeft);
	
	            celIndex = CellReference.convertColStringToIndex("F");
	            celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
	    		celda.setCellValue(var.getDescFamilia());
	    		celda.setCellStyle(sNormalLeft);
	
	            celIndex = CellReference.convertColStringToIndex("G");
	            celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
	    		celda.setCellValue(var.getDescSubfamilia());
	    		celda.setCellStyle(sNormalLeft);
	
	            celIndex = CellReference.convertColStringToIndex("H");
	            celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
	    		celda.setCellValue("");
	    		celda.setCellStyle(sNormalCenter);
	
	            celIndex = CellReference.convertColStringToIndex("I");
	            celda = row.createCell(celIndex, Cell.CELL_TYPE_STRING);
	    		celda.setCellValue(var.getDescMoneda());
	    		celda.setCellStyle(sNormalCenter);
			}
			
			memoryStream = new ByteArrayOutputStream();
			libro.write(memoryStream);
			memoryStream.close();
	        
	        // devolvemos resultado
			log.info("Devolvemos reporte");
	        respuesta.getBody().addValor("contenidoReporte", memoryStream.toByteArray());
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar exportar el Maestro", e);
			respuesta.getErrores().addCodigo(modulo, 1L);
			respuesta.setBody(null);
		}
		
	    return respuesta;
	}
	
	@Override
	public Respuesta exportarMaestro(HashMap<String, Object> params, String orderBy, String nombreMaestro) throws Exception {
		List<Producto> listProductos = null;
		Respuesta respuesta = new Respuesta();
		
		try {
			listProductos = this.findByProperties(params, orderBy, 0);
			respuesta = this.exportarMaestro(listProductos, nombreMaestro);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar exportar el Maestro", e);
			respuesta.getErrores().addCodigo(modulo, 1L);
			respuesta.setBody(null);
		}
		
        return respuesta;
	}

	// --------------------------------------------------------------------------------------------

	@Override
	public List<ConValores> recuperarTiposMaestro() throws Exception {
		try {
			return this.ifzConValores.findByGrupoNombre("SYS_CODE_NIVEL0");
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConValores> recuperarEspecialidades(long idMaestro) throws Exception {
		HashMap<String, Object> params = null;
		
		try {
			params = new HashMap<String, Object>();
			params.put("grupoValorId.nombre", "SYS_PRODUCTO_ESPECIALIDADES");
			params.put("atributo1", idMaestro);
			return this.ifzConValores.findByProperties(params, 0);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConValores> recuperarFamilias(long idEspecialidad) throws Exception {
		HashMap<String, Object> params = null;
		
		try {
			params = new HashMap<String, Object>();
			params.put("grupoValorId.nombre", "SYS_FAMILIA_PRODUCTO");
			params.put("atributo1", idEspecialidad);
			return this.ifzConValores.findByProperties(params, 0);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConValores> recuperarSubFamilias(long idFamilia) throws Exception {
		HashMap<String, Object> params = null;
		
		try {
			params = new HashMap<String, Object>();
			params.put("grupoValorId.nombre", "SYS_PRODUCTO_SUBFAMILIA");
			params.put("atributo1", idFamilia);
			return this.ifzConValores.findByProperties(params, 0);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ConValores> recuperarFamiliasByMaestro(long idMaestro) throws Exception {
		List<ConValores> especialidades = null;
		List<ConValores> familias = null;
		List<ConValores> aux = null;
		
		try {
			especialidades = this.recuperarEspecialidades(idMaestro);
			if (especialidades == null || especialidades.isEmpty())
				return null;
			
			for (ConValores especialidad : especialidades) {
				aux = this.recuperarFamilias(especialidad.getId());
				if (aux == null || aux.isEmpty())
					continue;
				if (familias == null)
					familias = new ArrayList<ConValores>();
				familias.addAll(aux);
			}

			Collections.sort(familias, new Comparator<ConValores>() {
		    	@Override
		        public int compare(ConValores o1, ConValores o2) {
		    		return o1.getDescripcion().compareTo(o2.getDescripcion());
		        }
			});
			
			return familias;
		} catch (Exception e) {
			throw e;
		}
	}
	
	// --------------------------------------------------------------------------------------------------
	// CONVERTIR
	// --------------------------------------------------------------------------------------------------

	@Override
	public ProductoExt convertir(Producto entity) {
		return this.convertidor.getExtendido(entity);
	}

	@Override
	public Producto convertir(ProductoExt entity) {
		return this.convertidor.getPojo(entity);
	}

	@Override
	public List<ProductoExt> extenderLista(List<Producto> entities) {
		List<ProductoExt> aux = new ArrayList<ProductoExt>();
		for (Producto entity : entities) 
			aux.add(this.convertidor.getExtendido(entity));
		return aux;
	}
	
	// --------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------

	@Override
	public Long save(ProductoExt entityExt) throws Exception {
		try {
			return this.save(this.convertidor.getPojo(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(ProductoExt entityExt) throws Exception {
		try {
			this.update(this.convertidor.getPojo(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public ProductoExt findExtById(Long idProducto) {
		try {
			return  this.convertidor.getExtendido(this.findById(idProducto));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public ProductoExt findExtByClave(String clave) {
		try {
			return this.convertidor.getExtendido(this.findByClave(clave));
		} catch (Exception re) {
			throw re;
		}
	}

	// --------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------

	private List<Producto> leerEXCEL(byte[] fileSrc, String fileExtension, HashMap<Integer, String> mapNoProcesados) throws Exception {
		HashMap<String, Long> mapUniMedida = new HashMap<String, Long>();
		HashMap<String, Long> mapFamilias = new HashMap<String, Long>();
		HashMap<String, Long> mapMonedas = new HashMap<String, Long>();
		List<Producto> listItems = new ArrayList<Producto>();
		List<ConValores> listaAuxiliar = null;
		Producto pojoProducto = null;
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		Row row = null;
		Cell celda = null;
		long idAux = 0L;
		String strAux = null;
		ConValores pojoAux = null;
		int indexRow = 0;
		int baseCols = 1;
		
		try {
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			
			// Recorremos todas las filas para mostrar el contenido de cada celda
			this.useMapReference = comprobarUsoMapReference(sheet.iterator(), baseCols);
			for (indexRow = 0; rowIterator.hasNext(); indexRow++) {
				row = rowIterator.next();
				pojoProducto = new Producto();
				pojoProducto.setTipo(1);
				
				// Clave
				celda = row.getCell(this.getColumnIndex("CODIGO", baseCols));
				if (celda == null || celda.getStringCellValue() == null || "".equals(celda.getStringCellValue().trim()) || "CLAVE".equals(celda.getStringCellValue().trim())) 
					continue;
				pojoProducto.setClave(celda.getStringCellValue());
				
				// Descripcion
				celda = row.getCell(this.getColumnIndex("DESCRIPCION", (baseCols + 1)));
				if (celda == null || celda.getStringCellValue() == null || "".equals(celda.getStringCellValue().trim())) {
					continue;
				}
				pojoProducto.setDescripcion(celda.getStringCellValue());
				
				// Unidad de medida
				celda = row.getCell(this.getColumnIndex("UM", (baseCols + 2)));
				if (celda == null || celda.getStringCellValue() == null || "".equals(celda.getStringCellValue().trim())) {
					mapNoProcesados.put(row.getRowNum() + 1, pojoProducto.getClave());
					continue;
				}
				if (mapUniMedida.containsKey(celda.getStringCellValue().trim())) {
					idAux = mapUniMedida.get(celda.getStringCellValue().trim());
				} else {
					idAux = 0L;
					listaAuxiliar = getListFromConValores(celda.getStringCellValue().trim(), this.unidadesMedida, 100);
					if (listaAuxiliar == null || listaAuxiliar.isEmpty()) {
						pojoAux = new ConValores();
						pojoAux.setGrupoValorId(this.unidadesMedida);
						pojoAux.setValor(celda.getStringCellValue().trim());
						pojoAux.setDescripcion(celda.getStringCellValue().trim());
						idAux = this.ifzConValores.save(pojoAux);
						pojoAux = null;
					} else {
						idAux = (listaAuxiliar).get(0).getId();
					}
					
					mapUniMedida.put(celda.getStringCellValue().trim(), idAux);
				}
				pojoProducto.setUnidadMedida(idAux);
				pojoProducto.setDescUnidadMedida(celda.getStringCellValue().trim());
				
				// Familia
				celda = row.getCell(this.getColumnIndex("FAMILIA", (baseCols + 3)));
				if (celda == null || celda.getStringCellValue() == null || "".equals(celda.getStringCellValue().trim())) {
					mapNoProcesados.put(row.getRowNum() + 1, pojoProducto.getClave());
					continue;
				}
				if (mapFamilias.containsKey(celda.getStringCellValue().trim())) {
					idAux = mapFamilias.get(celda.getStringCellValue().trim());
				} else {
					idAux = 0L;
					listaAuxiliar = getListFromConValores(celda.getStringCellValue().trim(), this.familias, 100);
					if (listaAuxiliar == null || listaAuxiliar.isEmpty()){
						pojoAux = new ConValores();
						pojoAux.setGrupoValorId(this.familias);
						pojoAux.setValor(celda.getStringCellValue().trim());
						pojoAux.setDescripcion(celda.getStringCellValue().trim());
						idAux = this.ifzConValores.save(pojoAux);
						pojoAux = null;
					} else {
						idAux = (listaAuxiliar).get(0).getId();
					}
					
					mapFamilias.put(celda.getStringCellValue().trim(), idAux);
				}
				pojoProducto.setFamilia(idAux);
				pojoProducto.setDescFamilia(celda.getStringCellValue().trim());
				
				// Moneda
				celda = row.getCell(this.getColumnIndex("MONEDA", (baseCols + 4)));
				if (celda == null || celda.getStringCellValue() == null || "".equals(celda.getStringCellValue().trim())) {
					idAux = 10000001;
					strAux = "PESOS";
				} else {
					idAux = 0L;
					strAux = celda.getStringCellValue().trim();
					if (mapMonedas.containsKey(strAux)) {
						idAux = mapMonedas.get(strAux);
					} else {
						List<Moneda> listMonedas = this.ifzMonedas.findLikeColumnName("sinonimos", strAux);
						if (listMonedas != null && ! listMonedas.isEmpty()) {
							idAux = listMonedas.get(0).getId();
							strAux = listMonedas.get(0).getNombre();
							listMonedas.clear();
						} else {
							idAux = 10000001;
							strAux = "PESOS";
						}
					}
				}
				mapMonedas.put(strAux.trim(), idAux);
				pojoProducto.setIdMoneda(idAux);
				pojoProducto.setDescMoneda(strAux);
				
				// Añadimos el producto al listado
				listItems.add(pojoProducto);
			}// fin for rows
			
			log.info("--- -- --> Familias encontradas :: " + mapFamilias.toString());
			log.info("--- -- --> Unidades encontradas :: " + mapUniMedida.toString());
			log.info("--- -- --> Monedas  encontradas :: " + mapMonedas.toString());
		} catch (Exception e) {
			log.error("------> Error al leear el archivo EXCEL (*." + fileExtension + ")", e);
			log.info("------> ROW: " + indexRow);
			throw e;
		}
		
		return listItems;
	}
	
	private Producto comprobarProductoPorClave(Long idProducto, String clave) {
		List<Producto> productos = null;
		
		try {
			idProducto = (idProducto != null && idProducto > 0L ? idProducto : 0L);
			clave = (clave != null && ! "".equals(clave.trim()) ? clave.trim() : "");
			if ("".equals(clave))
				return null;

			productos = this.ifzProductos.findByClave(clave, true, false, "model.clave, model.id desc", getIdEmpresa()); //(clave, getIdEmpresa());
			if (productos == null || productos.isEmpty())
				return null;
			if (productos.size() == 1)
				return productos.get(0);
			for (Producto var : productos) {
				if (idProducto.equals(var.getId()))
					return var;
			}
		} catch (Exception e) {
			log.error("ERROR al comprobar el producto con clave: " + clave, e);
			throw e;
		}
		
		return null;
	}
	
	private boolean comprobarModificado(Producto item1, Producto item2) {
		if (item2 == null)
			return true;
		
		if (! item1.getDescripcion().equals(item2.getDescripcion()))
			return true;
		
		if (item1.getUnidadMedida() != item2.getUnidadMedida())
			return true;
		
		if (item1.getFamilia() != item2.getFamilia())
			return true;
		
		return false;
	}
	
	private List<ConValores> getListFromConValores(String valor, ConGrupoValores grupo, int limite) throws Exception {
		try {
			return this.ifzConValores.findLikeValorAgenteEstatus(valor, grupo, null, limite);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private int getColumnIndex(String keyReference, int valorSugerido) {
		if (this.useMapReference && this.maestroLayoutReference != null && this.maestroLayoutReference.containsKey(keyReference))
			return CellReference.convertColStringToIndex(this.maestroLayoutReference.get(keyReference));
		return valorSugerido;
	}

	private boolean comprobarUsoMapReference(Iterator<Row> rowIterator, int baseCols) {
		Row row = null;
		Cell celda = null;
		int maxFallos = 5;
		int fallos = 0;
		
		if (this.maestroLayoutReference == null || this.maestroLayoutReference.isEmpty())
			return false;
		
		if (baseCols <= 0)
			return true;
		
		for (int indexRow = 0; rowIterator.hasNext(); indexRow++) {
			row = rowIterator.next();
			celda = row.getCell(CellReference.convertColStringToIndex("A"));
			if ((celda == null || celda.getStringCellValue() == null) && (indexRow <= maxFallos)) 
				fallos += 1;
			if (fallos == maxFallos)
				return false;
		}
		
		return true;
	}

	private void generaCellStyles(HSSFWorkbook wb) {
    	HSSFFont hfont = wb.createFont();

    	hfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    	
    	this.sHeader = wb.createCellStyle();
    	this.sHeader.setFont(hfont);
    	this.sHeader.setAlignment(CellStyle.ALIGN_CENTER);
    	this.sHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
    	this.sHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
    	this.sHeader.setWrapText(false);
    	
    	this.sNormalLeft = wb.createCellStyle();
    	this.sNormalLeft.setAlignment(CellStyle.ALIGN_LEFT);
    	this.sNormalLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sNormalLeft.setBorderTop(CellStyle.BORDER_THIN);
    	this.sNormalLeft.setBorderRight(CellStyle.BORDER_THIN);
    	this.sNormalLeft.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sNormalLeft.setBorderBottom(CellStyle.BORDER_THIN);
    	
    	this.sNormalCenter = wb.createCellStyle();
    	this.sNormalCenter.setAlignment(CellStyle.ALIGN_CENTER);
    	this.sNormalCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	this.sNormalCenter.setBorderTop(CellStyle.BORDER_THIN);
    	this.sNormalCenter.setBorderRight(CellStyle.BORDER_THIN);
    	this.sNormalCenter.setBorderLeft(CellStyle.BORDER_THIN);
    	this.sNormalCenter.setBorderBottom(CellStyle.BORDER_THIN);
    }

	private Long getIdEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getId() : 1L);
	}
	
	private long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 * 2.2 | 2017-06-19 | Javier Tirado 	| Implemento el metodo findLikeProperties
 *  2.2 | 2017-06-19 | Javier Tirado 	| Implemento el metodo exportarMaestro.
 *  2.1 | 2016-11-12 | Javier Tirado 	| Modifico el metodo leerEXCEL: Añado hashmap's para conservar unidades de medida y familias
 *  2.1 | 2017-03-31 | Javier Tirado 	| Implemento los metodos analizarArchivo y comprobarProductos: estos metodos deberian suplir al metodo actualizarProductos.
 *  2.1 | 2016-11-12 | Javier Tirado 	| Añado los metodos: 
 *  										1. findLikeProperty(String propertyName, Object value, int limite)
 *  										2. findLikeProperty(String propertyName, Object value, Long idFamilia, int limite)
 *  										3. convertir(Producto entity)
 *  										4. convertir(ProductoExt entity)
 *  										5. convertirLista(List<ProductoExt> entities)
 *  										6. convertirListaExtendida(List<Producto> entities) 
 *  										7. saveOrUpdateList(List<Producto> entities)
 *  										8. comprobarProductoPorClave
 */