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

import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.dao.ProductoDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;

@Stateless
public class ProductoFac implements ProductoRem {
	private static Logger log = Logger.getLogger(ProductoFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private ConGrupoValores unidadesMedida;
	private ConGrupoValores familias;
	private ProductoDAO ifzProductos;
	private ConValoresDAO ifzConValores;
	private MonedaDAO ifzMonedas;
	private ConvertExt convertidor;
	private LinkedHashMap<String, String> maestroLayoutReference;
	private boolean useMapReference;
	private String modulo = "PRODUCTOS";
	// Formato de celdas
	private CellStyle sHeader = null;
	private CellStyle sNormalLeft = null;
	private CellStyle sNormalCenter = null;
	
	
	public ProductoFac() {
		try {
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            
            this.ifzProductos = (ProductoDAO) this.ctx.lookup("ejb:/Model_Inventarios//ProductoImp!net.giro.inventarios.dao.ProductoDAO");
            this.ifzConValores= (ConValoresDAO) this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
            this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
            this.convertidor = new ConvertExt(); 
		} catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear ProductoFac", e);
			this.ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(Producto entity) throws ExcepConstraint {
		try {
			return this.ifzProductos.save(entity, getIdEmpresa());
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Producto> saveOrUpdateList(List<Producto> entities) throws Exception {
		try {
			return this.ifzProductos.saveOrUpdateList(entities);
		} catch (Exception re) {	
			throw re;
		}
	}
	
	@Override
	public void update(Producto entity) throws ExcepConstraint {
		try {
			this.ifzProductos.update(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(Producto entity) throws ExcepConstraint {
		try {
			this.ifzProductos.delete(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public Producto findById(Long id) {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findById(id);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Producto> findByProperty(String propertyName, Object value) {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findByProperty(propertyName, value);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public Producto findByClave(String clave) {
		List<Producto> lista = null;
		
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			lista = this.ifzProductos.findByProperty("clave", clave);
			if (lista != null && ! lista.isEmpty()) {
				Collections.sort(lista, new Comparator<Producto>() {
					@Override
					public int compare(Producto o1, Producto o2) {
						return o2.getId().compareTo(o1.getId());
					}
				});
			}
			
			return lista.get(0);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Producto> findByNombre(String descripcion) {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findByNombre(descripcion);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Producto> findAll() {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findAll();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findAllActivos() {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findAllActivos();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findBy(Object value, long idFamilia, int tipoMaestro, int limite) {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findBy(value, idFamilia, tipoMaestro, limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findLike(String value, long idFamilia, int tipoMaestro, int limite) {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findLike(value, idFamilia, tipoMaestro, limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperty(String propertyName, Object value, int limite) {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findLikeProperty(propertyName, value.toString(), limite);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Producto> findLikeProperty(String propertyName, Object value, Long idFamilia, int limite) {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findLikeProperty(propertyName, value.toString(), idFamilia, limite);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Producto> findLikeProperty(String propertyName, Object value, Long idFamilia, int tipo, int limite) {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findLikeProperty(propertyName, value.toString(), idFamilia, tipo, limite);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, int limite) throws Exception {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findLikeProperties(params, operador, limite);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, int limite, String orderBy) throws Exception {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findLikeProperties(params, operador, limite, orderBy);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperties(HashMap<String, String> params, int tipo, int limite, String orderBy) throws Exception {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findLikeProperties(params, tipo, limite, orderBy);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<Producto> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findByProperties(params, limite);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Producto> findByProperties(HashMap<String, Object> params, int limite, String orderBy) throws Exception {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findByProperties(params, limite, orderBy);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean validarClaveProducto(Long idProducto, String clave) { 
		List<Producto> productos = null;
		
		if (clave == null || "".equals(clave))
			return true;

		this.ifzProductos.setEmpresa(getIdEmpresa());
		productos = this.ifzProductos.findByClave(clave);
		if (productos == null || productos.isEmpty())
			return false;
		
		if (idProducto != null && idProducto > 0L) { 
			for (Producto var : productos) {
				if (! idProducto.equals(var.getId()))
					return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean validarClaveProducto(Producto pojoProducto) { 
		List<Producto> productos = null;
		
		if (pojoProducto == null)
			return true;

		this.ifzProductos.setEmpresa(getIdEmpresa());
		productos = this.ifzProductos.findByClave(pojoProducto.getClave());
		if (productos == null || productos.isEmpty())
			return false;
		
		if (pojoProducto.getId() != null && pojoProducto.getId() > 0L) { 
			for (Producto var : productos) {
				if (! pojoProducto.getId().equals(var.getId()))
					return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean validarClaveProducto(ProductoExt pojoProducto) { 
		return this.validarClaveProducto(this.convertir(pojoProducto));
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
			
			// Guardamos la lista
			//log.info("Guardando/Actualizando productos... (" + listItems.size() + ")");
			//this.ifzProductos.saveOrUpdateList(listItems);
			
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
	public List<Producto> findByClaveRango(String prefix, int inicioRango, int limiteRango) throws Exception {
		try {
			this.ifzProductos.setEmpresa(getIdEmpresa());
			return this.ifzProductos.findByClaveRango(prefix, inicioRango, limiteRango);
		} catch (Exception re) {	
			throw re;
		}
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
			List<ProductoExt> listExtendidos = this.convertirListaExtendida(listProductos);
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
	        //respuesta.getBody().addValor("contenidoReporte", libro.getBytes());
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
			listProductos = this.findByProperties(params, 0, orderBy);
			respuesta = this.exportarMaestro(listProductos, nombreMaestro);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar exportar el Maestro", e);
			respuesta.getErrores().addCodigo(modulo, 1L);
			respuesta.setBody(null);
		}
		
        return respuesta;
	}

	@Override
	public ProductoExt convertir(Producto entity) {
		return this.convertidor.ProductoToProductoExt(entity);
	}

	@Override
	public Producto convertir(ProductoExt entity) {
		return this.convertidor.ProductoExtToProducto(entity);
	}

	@Override
	public List<Producto> convertirLista(List<ProductoExt> entities) {
		List<Producto> aux = new ArrayList<Producto>();
		for (ProductoExt entity : entities) {
			aux.add(this.convertidor.ProductoExtToProducto(entity));
		}
		return aux;
	}

	@Override
	public List<ProductoExt> convertirListaExtendida(List<Producto> entities) {
		List<ProductoExt> aux = new ArrayList<ProductoExt>();
		for (Producto entity : entities) {
			aux.add(this.convertidor.ProductoToProductoExt(entity));
		}
		return aux;
	}
	
	// --------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------

	@Override
	public Long save(ProductoExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.ProductoExtToProducto(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void update(ProductoExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.ProductoExtToProducto(entityExt));
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public void delete(ProductoExt entity) throws ExcepConstraint {
		try {
			this.ifzProductos.delete(entity);
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public ProductoExt findExtById(Long id) {
		try {
			Producto producto = this.ifzProductos.findById(id);
			return  this.convertidor.ProductoToProductoExt( producto ) ;
		} catch (Exception re) {	
			throw re;
		}
	}

	@Override
	public List<ProductoExt> findExtByProperty(String propertyName, Object value) {
		try {
			List<Producto> lista = this.ifzProductos.findByProperty(propertyName, value);
			List<ProductoExt> listaExt = new ArrayList<>();
			for(Producto p:lista){
				listaExt.add( this.convertidor.ProductoToProductoExt(p) );
			}
			return listaExt;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public ProductoExt findExtByClave(String clave) {
		try {
			Producto p = this.findByClave(clave);
			if(p == null)
				return null;
			return this.convertidor.ProductoToProductoExt(p);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ProductoExt> findExtByNombre(String descripcion) {
		try {
			List<Producto> lista = this.ifzProductos.findByNombre(descripcion);
			List<ProductoExt> listaExt = new ArrayList<>();
			
			for(Producto p: lista)
				listaExt.add( this.convertidor.ProductoToProductoExt(p) );
			
			return listaExt;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ProductoExt> findExtAll() {
		try {
			List<Producto> lista = this.ifzProductos.findAll();
			List<ProductoExt> listaExt = new ArrayList<>();
			for(Producto p: lista){
				listaExt.add( this.convertidor.ProductoToProductoExt(p) );
			}
			return listaExt;
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<ProductoExt> findExtBy(Object value, long idFamilia, int tipoMaestro, int limite) {
		List<ProductoExt> extendidos = new ArrayList<ProductoExt>();
		List<Producto> lista = null;
		
		try {
			lista = this.findBy(value, idFamilia, tipoMaestro, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (Producto var : lista)
					extendidos.add(this.convertidor.ProductoToProductoExt(var));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public List<ProductoExt> findExtLike(String value, long idFamilia, int tipoMaestro, int limite) {
		List<ProductoExt> extendidos = new ArrayList<ProductoExt>();
		List<Producto> lista = null;
		
		try {
			lista = this.findLike(value, idFamilia, tipoMaestro, limite);
			if (lista != null && ! lista.isEmpty()) {
				for (Producto var : lista)
					extendidos.add(this.convertidor.ProductoToProductoExt(var));
			}
		} catch (Exception re) {		
			throw re;
		}
		
		return extendidos;
	}
	
	@Override
	public List<ProductoExt> findExtLikeProperty(String propertyName, Object value, int limite) {
		try {
			List<Producto> lista = this.ifzProductos.findLikeProperty(propertyName, value.toString(), limite);
			List<ProductoExt> listaExt = new ArrayList<>();
			for(Producto p : lista){
				listaExt.add(this.convertidor.ProductoToProductoExt(p));
			}
			
			return listaExt;
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<ProductoExt> findExtLikeProperty(String propertyName, Object value, Long idFamilia, int limite) {
		try {
			List<Producto> lista = this.ifzProductos.findLikeProperty(propertyName, value.toString(), idFamilia, limite);
			List<ProductoExt> listaExt = new ArrayList<>();
			
			for(Producto p : lista){
				listaExt.add(this.convertidor.ProductoToProductoExt(p));
			}
			
			return listaExt;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ProductoExt> findExtByClaveRango(String prefix, int inicioRango, int limiteRango) throws Exception {
		List<ProductoExt> listaExt = new ArrayList<ProductoExt>();
		
		try {
			List<Producto> lista = this.findByClaveRango(prefix, inicioRango, limiteRango);
			if (lista != null && ! lista.isEmpty()) {
				for(Producto p : lista)
					listaExt.add(this.convertidor.ProductoToProductoExt(p));
			}
			return listaExt;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ProductoExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<ProductoExt> listaExt = new ArrayList<ProductoExt>();
		
		try {
			List<Producto> lista = this.findByProperties(params, limite);
			for(Producto p : lista)
				listaExt.add(this.convertidor.ProductoToProductoExt(p));
			return listaExt;
		} catch (Exception e) {
			throw e;
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
		Producto pojoProducto;
		InputStream file;
		Workbook workbook;
		Sheet sheet;
		Iterator<Row> rowIterator;
		Row row;
		Cell celda;
		long idAux;
		String strAux;
		ConValores pojoAux;
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
					listaAuxiliar = getListFromConValores(celda.getStringCellValue().trim(), this.unidadesMedida, 80);
					if (listaAuxiliar == null || listaAuxiliar.isEmpty()) {
						pojoAux = new ConValores();
						pojoAux.setGrupoValorId(this.unidadesMedida);
						pojoAux.setValor(celda.getStringCellValue().trim());
						pojoAux.setDescripcion(celda.getStringCellValue().trim());
						idAux = this.ifzConValores.save(pojoAux, null);
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
					listaAuxiliar = getListFromConValores(celda.getStringCellValue().trim(), this.familias, 80);
					if (listaAuxiliar == null || listaAuxiliar.isEmpty()){
						pojoAux = new ConValores();
						pojoAux.setGrupoValorId(this.familias);
						pojoAux.setValor(celda.getStringCellValue().trim());
						pojoAux.setDescripcion(celda.getStringCellValue().trim());
						idAux = this.ifzConValores.save(pojoAux, null);
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
		try {
			if (idProducto == null) idProducto = 0L;
			if (clave == null || "".equals(clave)) return null;
			
			List<Producto> productos = this.ifzProductos.findByClave(clave);
			if(productos != null && ! productos.isEmpty()) {
				if (productos.size() == 1)
					return productos.get(0);
				
				for (Producto var : productos) {
					if (idProducto.equals(var.getId()))
						return var;
				}
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
	
	private List<ConValores> getListFromConValores(String valor, ConGrupoValores grupo, int limite) {
		return this.ifzConValores.findLikeClaveValorGrupo(valor, grupo, limite);
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
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
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