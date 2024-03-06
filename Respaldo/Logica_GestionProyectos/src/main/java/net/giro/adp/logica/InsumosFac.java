package net.giro.adp.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import net.giro.adp.beans.InsumoRow;
import net.giro.adp.beans.Insumos;
import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.adp.beans.InsumosExt;
import net.giro.adp.beans.Obra;
import net.giro.adp.dao.InsumosDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.TipoInsumo;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.respuesta.Respuesta;

@Stateless
public class InsumosFac implements InsumosRem {
	private static Logger log = Logger.getLogger(InsumosFac.class);
	
	private InitialContext ctx;	
	private InfoSesion infoSesion;
	private ConvertExt convertidor;
	private String modulo = "INSUMOS";
	private InsumosDAO ifzInsumos;
	private ProductoRem ifzProductos;
	private ObraRem ifzObras;
	private ConGrupoValoresRem ifzGrupos;
	private ConValoresRem ifzConValores;
	private ConGrupoValores grupoUnidades;
	private InsumosDetallesExt detalleInsumo;
	private List<InsumosDetallesExt> listMateriales;
	private List<InsumosDetallesExt> listManoDeObra;
	private List<InsumosDetallesExt> listHerramientas;
	private List<InsumosDetallesExt> listOtros;
	private List<InsumoRow> listInsumos;
	private List<InsumoRow>	listNoEncontrados;
	private HashMap<String, String> layoutReference; 
	private boolean iniciaCategoria = false; 
	
	
	public InsumosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);

            this.ifzInsumos   	= (InsumosDAO) 			this.ctx.lookup("ejb:/Model_GestionProyectos//InsumosImp!net.giro.adp.dao.InsumosDAO");
            this.ifzProductos 	= (ProductoRem) 		this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
            this.ifzObras 		= (ObraRem) 			this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
            this.ifzGrupos 		= (ConGrupoValoresRem) 	this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
            this.ifzConValores	= (ConValoresRem) 		this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
            
           this.convertidor = new ConvertExt();
           this.convertidor.setFrom("InsumosFac");
           this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear InsumosFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void setInfoSecion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	@Override
	public Long save(Insumos entity) throws ExcepConstraint {
		try {
			return this.ifzInsumos.save(entity);
		} catch (Exception e) {
			log.error("Error en InsumosFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	public Long save(InsumosExt entityExt) throws ExcepConstraint {
		try {
			Insumos entity = this.convertidor.InsumosExtToInsumos(entityExt);
			return this.save(entity);
		} catch (Exception e) {
			log.error("Error en InsumosFac.save(entityExt)", e);
			throw e;
		}
	}

	@Override
	public void update(Insumos entity) throws ExcepConstraint {
		try {
			this.ifzInsumos.update(entity);
		} catch (Exception e) {
			log.error("Error en InsumosFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	public void update(InsumosExt entityExt) throws ExcepConstraint {
		try {
			Insumos entity = this.convertidor.InsumosExtToInsumos(entityExt);
			this.update(entity);
		} catch (Exception e) {
			log.error("Error en InsumosFac.update(entityExt)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws ExcepConstraint {
		try {
			this.ifzInsumos.delete(entity);
		} catch (Exception e) {
			log.error("Error en InsumosFac.delete(entity)", e);
			throw e;
		}
	}

	@Override
	public List<Insumos> findAll() {
		try {
			return this.ifzInsumos.findAll();
		} catch (Exception e) {
			log.error("Error en InsumosFac.findAll()", e);
			throw e;
		}
	}

	@Override
	public Insumos findById(Long id) {
		try {
			return this.ifzInsumos.findById(id);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public InsumosExt findByIdExt(Long id) throws Exception {
		try {
			return this.convertidor.InsumosToInsumosExt(this.findById(id));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByIdExt(id)", e);
			throw e;
		}
	}

	@Override
	public List<Insumos> findByProperty(String propertyName, Object value, int max) {
		try {
			return this.ifzInsumos.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByProperty(propertyName, value, max)", e);
			throw e;
		}
	}

	@Override
	public List<InsumosExt> findByPropertyExt(String propertyName, Object value, int max) throws Exception {
		List<InsumosExt> listaExt = new ArrayList<InsumosExt>();
		
		try {
			List<Insumos> lista = this.findByProperty(propertyName, value, max);
			for(Insumos var : lista)
				listaExt.add(this.convertidor.InsumosToInsumosExt(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByPropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<Insumos> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			if ("nombreObra".equals(propertyName)) {
				List<Obra> listTemp = this.ifzObras.findLikeProperty("nombre", value.toString());
				if (listTemp == null || listTemp.isEmpty()) 
					return new ArrayList<Insumos>();
				
				List<Object> values = new ArrayList<Object>();
				for (Obra var : listTemp)
					values.add(var.getId());
				
				return this.ifzInsumos.findInProperty("idObra", values);
			} else {
				return this.ifzInsumos.findLikeProperty(propertyName, value, max);
			}
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		}
	}

	@Override
	public List<InsumosExt> findLikePropertyExt(String propertyName, Object value, int max) throws Exception {
		List<InsumosExt> listaExt = new ArrayList<InsumosExt>();
		
		try {
			List<Insumos> lista = this.findLikeProperty(propertyName, value, max);
			for(Insumos var : lista)
				listaExt.add(this.convertidor.InsumosToInsumosExt(var));
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikePropertyExt(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public Respuesta analizarExcel(byte[] fileSrc, String fileExtension, HashMap<String, String> insumosCellReference) throws Exception {
		Respuesta resp = new Respuesta();
		double montoTotal = 0;
		
		try {
			if (fileExtension == null || "".equals(fileExtension))
				fileExtension = "xls";
			
			// leemos el archivo anexo
			log.info("Leyendo archivo");
			resp = leerEXCEL(fileSrc, insumosCellReference);
			if (resp.getErrores().getCodigoError() != 0L)
				return resp;
			log.info(this.listInsumos.size() + " registros leidos");

			// Comprobamos y generamos las listas correpondientes de insumos
			log.info("Comprobamos registros");
			resp = this.comprobarInsumos();
			if (resp.getErrores().getCodigoError() != 0L)
				return resp;
			
			// Generamos el pojo de INSUMO con los totales de los detalles y total general.
			log.info("Generando insumo");
			InsumosExt pojoInsumo = new InsumosExt();
			pojoInsumo.setCreadoPor(this.infoSesion.getAcceso().getId());
			pojoInsumo.setFechaCreacion(Calendar.getInstance().getTime());
			pojoInsumo.setModificadoPor(this.infoSesion.getAcceso().getId());
			pojoInsumo.setFechaModificacion(Calendar.getInstance().getTime());
			
			montoTotal = 0;
			for(InsumosDetallesExt var : this.listMateriales)
				montoTotal += var.getMonto().doubleValue();
			pojoInsumo.setMontoMateriales(new BigDecimal(montoTotal));
			
			montoTotal = 0;
			for(InsumosDetallesExt var : this.listManoDeObra)
				montoTotal += var.getMonto().doubleValue();
			pojoInsumo.setMontoManoDeObra(new BigDecimal(montoTotal));
			
			montoTotal = 0;
			for(InsumosDetallesExt var : this.listHerramientas)
				montoTotal += var.getMonto().doubleValue();
			pojoInsumo.setMontoHerramientas(new BigDecimal(montoTotal));
			
			montoTotal = 0;
			for(InsumosDetallesExt var : this.listOtros)
				montoTotal += var.getMonto().doubleValue();
			pojoInsumo.setMontoOtros(new BigDecimal(montoTotal));
			
			pojoInsumo.setTotal(pojoInsumo.getTotalCalculado());

			// Generamos la respuesta
			resp.getBody().addValor("pojoInsumo", pojoInsumo);
			resp.getBody().addValor("materiales", this.listMateriales);
			resp.getBody().addValor("manoDeObra", this.listManoDeObra);
			resp.getBody().addValor("herramientas", this.listHerramientas);
			resp.getBody().addValor("otros", this.listOtros);
			resp.getBody().addValor("productos", this.listNoEncontrados);
			log.info("Insumo generado");
			log.info("---> Registros (" + this.listInsumos.size() + ")");
			log.info("---> Materiales (" + this.listMateriales.size() + ")");
			log.info("---> Mano de Obra (" + this.listManoDeObra.size() + ")");
			log.info("---> Herramientas (" + this.listHerramientas.size() + ")");
			log.info("---> Otros (" + this.listOtros.size() + ")");
			log.info("---> Sin procesar (" + this.listNoEncontrados.size() + ")");
		} catch (Exception e) {
			resp.setBody(null);
			resp.getErrores().addCodigo(modulo, 1L);
			resp.getErrores().setDescError("Error al analizar el archivo de Explosion de Insumos");
			log.error("Error al leer el archivo XLS", e);
		}
		
		return resp;
	}

	@Override
	public boolean comprobarInsumo(Long idObra) {
		try {
			return !(this.ifzInsumos.comprobarInsumos(idObra)).isEmpty();
		} catch (Exception e) {
			log.error("Error en InsumosFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		}
	}

	@Override
	public List<Insumos> findByActivos(int max){
		try {
			return this.ifzInsumos.findByActivos(max);
		} catch (Exception e) {
			log.error("Error en InsumosFac.findByActivos(propertyName, value, max)", e);
			throw e;
		}
	}

	@Override
	public List<InsumosExt> findExtByActivos(int max){
		List<InsumosExt> listaExt = new ArrayList<>();
		try {
			List<Insumos> lista = this.ifzInsumos.findByActivos(max);
			for(Insumos var:lista){
				listaExt.add( this.convertidor.InsumosToInsumosExt(var) );
			}
		} catch (Exception e) {
			log.error("Error en InsumosFac.findExtByActivos(propertyName, value, max)", e);
			try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return listaExt;
	}
	
	@Override
	public Insumos convertir(InsumosExt target) throws Exception {
		return this.convertidor.InsumosExtToInsumos(target);
	}

	@Override
	public InsumosExt convertir(Insumos target) throws Exception {
		return this.convertidor.InsumosToInsumosExt(target);
	}
	
	// ----------------------------------------------------------------------------------------------------
	// PRIVADAS
	// ----------------------------------------------------------------------------------------------------
	
	private Respuesta leerEXCEL(byte[] fileSrc, HashMap<String, String> layoutReference) throws Exception {
		Respuesta respuesta = new Respuesta();
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		Row row = null;
		Cell celda = null;
		InsumoRow itemInsumo = null;
		int indexRow = 0;	
		TipoInsumo tipoInsumo = TipoInsumo.Ninguno;
		int tipoNivel = 0;
		
		try {
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			
			if (this.listInsumos == null)
				this.listInsumos = new ArrayList<InsumoRow>();
			this.listInsumos.clear();
			this.layoutReference = layoutReference;
			this.iniciaCategoria = true;
			
			for (indexRow = 0; rowIterator.hasNext(); indexRow++) {
				itemInsumo = new InsumoRow();
				row = rowIterator.next();
				
				if (! validaDatos(row)) {
					tipoInsumo = determinaTipoInsumo(row, tipoInsumo, tipoNivel);
					continue;
				}

				// TIPO DE INSUMO
				itemInsumo.setTipo(tipoInsumo.ordinal());

				// CODIGO o SECCION
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CODIGO")));
				itemInsumo.setClave(celda.getStringCellValue().trim().toUpperCase());
				
				// DESCRIPCION
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("DESCRIPCION")));
				itemInsumo.setDescripcion(celda.getStringCellValue().trim().toUpperCase());
				
				// UNIDAD DE MEDIDA
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("UM")));
				itemInsumo.setUnidad(celda.getStringCellValue().trim().toUpperCase());

				// FAMILIA
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("FAMILIA")));
				if (celda != null && ! "".equals(celda.getStringCellValue().trim()))
					itemInsumo.setFamilia(celda.getStringCellValue().trim().toUpperCase());
				
				// CANTIDAD
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CANTIDAD")));
				itemInsumo.setCantidad(celda.getNumericCellValue());
				
				// PRECIO UNITARIO
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("PU")));
				itemInsumo.setCostoUnitario(celda.getNumericCellValue());
				
				// IMPORTE
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("IMPORTE")));
				itemInsumo.setMonto(celda.getNumericCellValue());
				
				// PORCENTAJE
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("PORCENTAJE")));
				if (celda != null && celda.getCellType() == Cell.CELL_TYPE_NUMERIC)
					itemInsumo.setPorcentaje(celda.getNumericCellValue());
				
				// MONEDA
				celda = row.getCell(this.getColumnIndex(this.layoutReference.get("MONEDA")));
				if (celda != null && ! "".equals(celda.getStringCellValue().trim()))
					itemInsumo.setMoneda(celda.getStringCellValue().trim().toUpperCase());
				
				// Añadimos el item a la lista
				this.listInsumos.add(itemInsumo);
			}
		} catch (Exception e) {
			respuesta.setBody(null);
			respuesta.getErrores().addCodigo(modulo, 1L);
			if (e.getMessage().contains("{NO_UNIDAD}"))
				respuesta.getErrores().setDescError("Error en formato de Explosion de Insumos.\n\nFalta informacion para algunos productos:\n" + e.getMessage().replace("{NO_UNIDAD}", ""));
			else
				respuesta.getErrores().setDescError("Error en formato de Explosion de Insumos");
			log.error("Error al leer el formato de Explosion de Insumos.  Comprobo hasta el renglon " + (indexRow + 1), e);
		}
		
		return respuesta;
	}
	
	private Respuesta comprobarInsumos() {
		Respuesta resp = new Respuesta();
		InsumoRow current = null;
		this.listMateriales = new ArrayList<InsumosDetallesExt>();
		this.listManoDeObra = new ArrayList<InsumosDetallesExt>();
		this.listHerramientas = new ArrayList<InsumosDetallesExt>();
		this.listOtros = new ArrayList<InsumosDetallesExt>();
		this.listNoEncontrados = new ArrayList<InsumoRow>();
		
		try {
			for (InsumoRow var : this.listInsumos) {
				current = var;
				this.detalleInsumo = getInsumoDetalleFromInsumoRow(var);
				if (this.detalleInsumo == null) {
					this.listNoEncontrados.add(var);

					// Generamos nuestro detalle: solo para referencia
					this.detalleInsumo = new InsumosDetallesExt();
					this.detalleInsumo.setNombreProducto(var.getClave() + " - " + var.getDescripcion());
					this.detalleInsumo.setCantidad(var.getCantidad());
					this.detalleInsumo.setPrecioUnitario(var.getCostoUnitario());
					this.detalleInsumo.setMonto(new BigDecimal(var.getMonto()));
					this.detalleInsumo.setPorcentaje(var.getPorcentaje());
					this.detalleInsumo.setTipo(var.getTipo());
				} 
				
				if(var.getTipo() == TipoInsumo.Material.ordinal())
					this.listMateriales.add(this.detalleInsumo);
				else if(var.getTipo() == TipoInsumo.ManoDeObra.ordinal())
					this.listManoDeObra.add(this.detalleInsumo);
				else if(var.getTipo() == TipoInsumo.Herramienta.ordinal())
					this.listHerramientas.add(this.detalleInsumo);
				else
					this.listOtros.add(this.detalleInsumo);
			}
		} catch (Exception e) {
			log.error("Error comprobando la Explosion de Insumos en Logica_GestionProyectos.InsumosFac.comprobarInsumos con: " + current.toString(), e);
			resp.setBody(null);
			resp.getErrores().addCodigo(modulo, 1L);
			resp.getErrores().setDescError("Error comprobando la Explosion de Insumos");
		}
		
		return resp;
	}
	
	private InsumosDetallesExt getInsumoDetalleFromInsumoRow(InsumoRow value) {
		InsumosDetallesExt result = new InsumosDetallesExt();
		List<ConValores> listUnidades = null;
		Producto pojoProducto = null;

		pojoProducto = this.ifzProductos.findByClave(value.getClave());
		if (pojoProducto == null)
			return null; // if (! prodValido.getUnidadMedida().getDescripcion().equals(value.getUnidad()))
		
		if (pojoProducto.getUnidadMedida() <= 0) {
			try {
				if (this.grupoUnidades == null) 
					this.grupoUnidades = this.ifzGrupos.findByName("SYS_UNIDAD_MEDIDA");
				
				if (this.grupoUnidades != null) {
					// Localizamos la UNIDAD DE MEDIDA
					listUnidades = this.ifzConValores.findByProperty("valor", value.getUnidad(), this.grupoUnidades, 0);
					if (listUnidades != null && ! listUnidades.isEmpty()) {
						// Actualizamos UNIDAD DE MEDIDA en el producto
						pojoProducto.setUnidadMedida(listUnidades.get(0).getId());
						pojoProducto.setDescUnidadMedida(listUnidades.get(0).getDescripcion());
						this.ifzProductos.update(pojoProducto);
					}
				}
			} catch (Exception e) {
				log.error("ERROR INTERNO - No pude actualizar la Unidad de Medida", e);
			}
		}
		
		result.setIdProducto(this.ifzProductos.convertir(pojoProducto));
		result.setNombreProducto(value.getDescripcion());
		result.setCantidad(value.getCantidad());
		result.setPrecioUnitario(value.getCostoUnitario());
		result.setMonto(new BigDecimal(value.getMonto()));
		result.setPorcentaje(value.getPorcentaje());
		result.setTipo(value.getTipo());
		
		result.setCreadoPor(this.infoSesion.getAcceso().getId());
		result.setFechaCreacion(Calendar.getInstance().getTime());
		result.setModificadoPor(this.infoSesion.getAcceso().getId());
		result.setFechaModificacion(Calendar.getInstance().getTime());
		
		return result;
	}	

	private int getColumnIndex(String cellHeader) {
		return CellReference.convertColStringToIndex(cellHeader);
	}
	
	private boolean validaDatos(Row row) throws Exception {
		HashMap<String, String> excluyentes = new HashMap<String, String>();
		FormulaEvaluator eval = null;
		Cell celda = null;
		CellValue cellValue = null;
		String cadena = "";
		
		excluyentes.put("MATERIALES", "TOTAL_DE_MATERIALES");
		excluyentes.put("MANO_DE_OBRA", "TOTAL_DE_MANO_DE_OBRA");
		excluyentes.put("HERRAMIENTA", "HERRAMIENTA");
		excluyentes.put("EQUIPO", "TOTAL_EQUIPO");
		excluyentes.put("AUXILIARES", "TOTAL_AUXILIARES");
		eval = row.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
				
		// Validacion de codigo
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CODIGO")));
		if (celda == null) 
			return false;
		cadena = celda.getStringCellValue().trim().toUpperCase();
		if ("".equals(cadena.trim()))
			return false; 
		if (excluyentes.containsKey(cadena.trim().replace(" ", "_").toUpperCase()))
			return false; 
		
		// Validacion de descripcion
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("DESCRIPCION")));
		if (celda == null) 
			return false;
		cadena = celda.getStringCellValue().trim();
		if ("".equals(cadena.trim()))
			return false;
		
		// Validacion de unidad de medida
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("UM")));
		if (celda == null) {
			celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CODIGO")));
			cadena = celda.getStringCellValue().trim();
			throw new Exception("{NO_UNIDAD}El producto " + cadena + " no tiene asignada UNIDAD DE MEDIDA");
		}
		cadena = celda.getStringCellValue().trim();
		if ("".equals(cadena.trim())) {
			celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CODIGO")));
			cadena = celda.getStringCellValue().trim();
			throw new Exception("{NO_UNIDAD}El producto " + cadena + " no tiene asignada UNIDAD DE MEDIDA");
		}
		
		/*
		 * NO SE VALIDAN LOS DEMAS CAMPOS PARA QUE LO DE POR BUENO Y ROMPA EL FLUJO CUANDO
		 * INTENTE RECUPERAR UN DATO Y ESTE INCORRECTO: FORMATO, VACIO, ETC. 
		*/
		
		// CANTIDAD
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CANTIDAD")));
		if (celda == null) 
			return false;
		else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = eval.evaluate(celda);
			if (cellValue.getCellType() != Cell.CELL_TYPE_NUMERIC)
				return false;
		} else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_STRING) {
			cadena = celda.getStringCellValue().trim();
			cadena = cadena.replace("'", "").replace(",","").replace("$","").replace(" ","").trim();
			if (! NumberUtils.isNumber(cadena))
				return false;
			celda.setCellValue(Double.parseDouble(cadena));
		} else if (celda != null && celda.getCellType() != Cell.CELL_TYPE_NUMERIC)
			return false;

		// PRECIO UNITARIO
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("PU")));
		if (celda == null) 
			return false;
		else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = eval.evaluate(celda);
			if (cellValue.getCellType() != Cell.CELL_TYPE_NUMERIC)
				return false;
		} else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_STRING) {
			cadena = celda.getStringCellValue().trim();
			cadena = cadena.replace("'", "").replace(",","").replace("$","").replace(" ","").trim();
			if (! NumberUtils.isNumber(cadena))
				return false;
			celda.setCellValue(Double.parseDouble(cadena));
		} else if (celda != null && celda.getCellType() != Cell.CELL_TYPE_NUMERIC)
			return false;
		
		// IMPORTE
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("IMPORTE")));
		if (celda == null) 
			return false;
		else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = eval.evaluate(celda);
			if (cellValue.getCellType() != Cell.CELL_TYPE_NUMERIC)
				return false;
		} else if (celda != null && celda.getCellType() == Cell.CELL_TYPE_STRING) {
			cadena = celda.getStringCellValue().trim();
			cadena = cadena.replace("'", "").replace(",","").replace("$","").replace(" ","").trim();
			if (! NumberUtils.isNumber(cadena))
				return false;
			celda.setCellValue(Double.parseDouble(cadena));
		} else if (celda != null && celda.getCellType() != Cell.CELL_TYPE_NUMERIC)
			return false;
		
		return true;
	}
	
	private TipoInsumo determinaTipoInsumo(Row row, TipoInsumo tipoActual, int tipoNivel) {
		HashMap<String, TipoInsumo> map = new HashMap<String, TipoInsumo>();
		Cell celda = null;
		String cadena = "";
		
		map.put("MATERIALES", TipoInsumo.Material);
		map.put("TOTAL_DE_MATERIALES", TipoInsumo.Material);
		map.put("MANO_DE_OBRA", TipoInsumo.ManoDeObra);
		map.put("TOTAL_DE_MANO_DE_OBRA", TipoInsumo.ManoDeObra);
		map.put("HERRAMIENTA", TipoInsumo.Herramienta);
		map.put("TOTAL_HERRAMIENTA", TipoInsumo.Herramienta);
		/*map.put("AUXILIARES", TipoInsumo.Otros);
		map.put("TOTAL_AUXILIARES", TipoInsumo.Otros);
		map.put("EQUIPO", TipoInsumo.Otros);
		map.put("TOTAL_EQUIPO", TipoInsumo.Otros);*/
		
		celda = row.getCell(this.getColumnIndex(this.layoutReference.get("CODIGO")));
		if (celda == null) {
			if (this.iniciaCategoria && tipoActual == TipoInsumo.Ninguno) {
				this.iniciaCategoria = false;
				tipoActual = TipoInsumo.Material;
			}
			return tipoActual;
		}
		
		cadena = celda.getStringCellValue().trim();
		if ("".equals(cadena.trim()))
			return tipoActual;
		
		if (this.iniciaCategoria && tipoActual == TipoInsumo.Ninguno) {
			this.iniciaCategoria = false;
			tipoActual = TipoInsumo.Material;
			return tipoActual;
		}
		
		cadena = cadena.trim().toUpperCase().replace(" ", "_");
		if (map.containsKey(cadena))
			tipoActual = map.get(cadena); 
		else
			tipoActual = TipoInsumo.Otros;
		
		if (cadena.contains("TOTAL_"))
			this.iniciaCategoria = true;
		
		/*switch (cadena) {
		case "MATERIALES": 
		case "TOTAL_DE_MATERIALES": 
			tipoActual = TipoInsumo.Material; 
			this.iniciaCategoria = true; 
			break;
		case "MANO_DE_OBRA": 
		case "TOTAL_DE_MANO_DE_OBRA": 
			tipoActual = TipoInsumo.ManoDeObra; 
			this.iniciaCategoria = true; 
			break;
		case "HERRAMIENTA": 
		case "TOTAL_DE_HERRAMIENTA": 
			tipoActual = TipoInsumo.Herramienta; 
			this.iniciaCategoria = true; 
			break;
		default: 
			if (! this.iniciaCategoria)
				return tipoActual; 
			else 
				tipoActual = TipoInsumo.Otros;
			break;
		}*/
		
		return tipoActual;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSION |    FECHA   | 			 AUTOR 			 | DESCRIPCI\D3N 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  2.1	| 03/04/2017 | Javier Tirado 			 | Modifico el metodo leerExcel. Lee el archivo en base a un layout.
 *    2.1	| 03/04/2017 | Javier Tirado 			 | Añado los metodos convertir. Convierte el pojo de insumos a extendido y viceversa
 */