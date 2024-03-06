package net.giro.rh.admon.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.ChecadorDetalleExt;
import net.giro.rh.admon.beans.ChecadorSemanalItem;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.dao.ChecadorDetalleDAO;

@Stateless
public class ChecadorDetalleFac implements ChecadorDetalleRem {
	private static Logger log = Logger.getLogger(ChecadorDetalleFac.class);
	private InfoSesion infoSesion;
	private ChecadorDetalleDAO ifzChecadorDetalles;
	private EmpleadoRem ifzEmpleados;
	private EmpleadoContratoRem ifzContratos;
	private ConvertExt convertidor;
	private List<ChecadorDetalle> detallesLog;
	private TimeZone timeZone;
	
	public ChecadorDetalleFac() {
		Hashtable<String, Object> environtment = null;
		InitialContext ctx = null;
		
		try {
			environtment = new Hashtable<String, Object>();
			environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environtment);
			this.ifzChecadorDetalles = (ChecadorDetalleDAO) ctx.lookup("ejb:/Model_RecHum//ChecadorDetalleImp!net.giro.rh.admon.dao.ChecadorDetalleDAO");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzContratos = (EmpleadoContratoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ChecadorDetalleFac");
			this.convertidor.setMostrarSystemOut(false);
			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.ChecadorDetalleFac", e);
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(ChecadorDetalle entity) throws Exception {
		try {
			return this.ifzChecadorDetalles.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.save(ChecadorDetalle)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ChecadorDetalle> saveOrUpdateList(List<ChecadorDetalle> entities) throws Exception {
		try {
			return this.ifzChecadorDetalles.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.saveOrUpdateList(List<ChecadorDetalle> entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(ChecadorDetalle entity) throws Exception {
		try {
			this.ifzChecadorDetalles.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.update(ChecadorDetalle)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void delete(Long idChecadorDetalle) throws Exception {
		try {
			this.ifzChecadorDetalles.delete(idChecadorDetalle);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public void delete(List<ChecadorDetalle> entities) throws Exception {
		if (entities == null || entities.isEmpty())
			return;
		for (ChecadorDetalle entity : entities)
			this.delete(entity.getId());
	}

	@Override
	public ChecadorDetalle findById(Long idChecadorDetalle) {
		try {
			return this.ifzChecadorDetalles.findById(idChecadorDetalle);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findById(idChecadorDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<ChecadorDetalle> findAll(long idChecador, String orderBy) throws Exception {
		try {
			return this.ifzChecadorDetalles.findAll(idChecador, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.asistenciaByChecador(idChecador)", e);
			throw e;
		}
	}

	@Override
	public List<ChecadorDetalle> findLike(String value, long idObra, String orderBy, int limite) throws Exception {
		try {
			while (value.trim().contains("  "))
				value = value.trim().replace("  ", " ");
			value = (value.trim().contains("+") ? value.trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.trim());
			value = (value.trim().contains("|") ? value.trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.trim());
			value = (value.trim().contains(" ") ? value.trim().replace(" ", "%") : value.trim());
			
			return this.ifzChecadorDetalles.findLike(value, idObra, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findLike(value, idObra, orderBy, limite)", e);
			throw e;
		} 
	}
	
	@Override
	public List<ChecadorDetalle> findLikeProperty(String propertyName, Object value, long idObra, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), idObra, orderBy, limite);
			
			if (value instanceof java.lang.String) {
				while (value.toString().trim().contains("  "))
					value = value.toString().trim().replace("  ", " ");
				value = (value.toString().trim().contains("+") ? value.toString().trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.toString().trim());
				value = (value.toString().trim().contains("|") ? value.toString().trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.toString().trim());
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			}
			
			return this.ifzChecadorDetalles.findLikeProperty(propertyName, value, idObra, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findLikeProperty(propertyName, value, idObra, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ChecadorDetalle> findByProperty(String propertyName, Object value, long idObra, String orderBy, int limite) throws Exception {
		try {
			return this.ifzChecadorDetalles.findByProperty(propertyName, value, idObra, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findByProperty(propertyName, value, idObra, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ChecadorDetalle> findByDates(Date fechaDesde, Date fechaHasta, String orderBy) throws Exception {
		try {
			return this.ifzChecadorDetalles.findByDates(fechaDesde, fechaHasta, getIdEmpresa(), orderBy);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findByDates(fechaDesde, fechaHasta)", e);
			throw e;
		}
	}

	@Override
	public List<ChecadorDetalle> findAsistenciasPosteriorFecha(long idEmpleado, Date fecha, String orderBy) throws Exception {
		try {
			return this.ifzChecadorDetalles.findAsistenciasPosteriorFecha(idEmpleado, fecha, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findAsistenciasPosteriorFecha(idEmpleado, fecha)", e);
			throw e;
		}
	}
	
	/*@Override
	public List<ChecadorDetalle> findByObraEmpleado(long idObra, long idEmpleado, Date fechaBase, String orderBy) throws Exception {
		try {
			return this.ifzChecadorDetalles.findByObraEmpleado(idObra, idEmpleado, fechaBase, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findByObraEmpleado(idObra, idEmpleado, fechaBase)", e);
			throw e;
		}
	}

	@Override
	public List<ChecadorDetalle> findInProperty(String columnName, List<Object> values, String orderBy, int limite) throws Exception {
		try {
			return this.ifzChecadorDetalles.findInProperty(columnName, values, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ChecadorDetalle> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			return this.ifzChecadorDetalles.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findByProperties(params, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<ChecadorDetalle> findLikeProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			return this.ifzChecadorDetalles.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findLikeProperties(params, limite)", e);
			throw e;
		} 
	}*/

	@Override
	public Respuesta analizaDetalles(String fileName, String fileExtension, byte[] fileSrc) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<ChecadorDetalle> detalles = null;
		
		try {
			detalles = procesarArchivo(fileName, fileSrc);
			if (detalles == null || detalles.isEmpty()) {
				log.error("Listado de detalles vacio: " + fileName);
				respuesta.getErrores().addCodigo("CHECADOR_DETALLE", 2L);
				respuesta.setBody(null);
				return respuesta;
			}

			respuesta.getBody().addValor("detalles", detalles);
			respuesta.getBody().addValor("detallesLog", this.detallesLog);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("CHECADOR_DETALLE", 1L);
			respuesta.setBody(null);
			log.error("Error al leer el archivo: " + fileName, e);
			throw e;
		}
		
		return respuesta;
	}
	
	@Override
	public boolean existeAsistencia(Long idChecador, Long idEmpleado, Date fecha) throws Exception {
		ChecadorDetalle pojo = null;
		
		try {
			pojo = this.existeAsistenciaPojo(idChecador, idEmpleado, fecha);
			if (pojo == null)
				return false;
			return true;
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.existeAsistencia(idEmpleado, fecha)", e);
			throw e;
		}
	}

	@Override
	public Long existeAsistenciaID(Long idChecador, Long idEmpleado, Date fecha) throws Exception {
		ChecadorDetalle pojo = null;
		
		try {
			pojo = this.existeAsistenciaPojo(idChecador, idEmpleado, fecha);
			if (pojo == null)
				return null;
			return pojo.getId();
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.existeAsistenciaID(idEmpleado, fecha)", e);
			throw e;
		}
	}

	@Override
	public ChecadorDetalle existeAsistenciaPojo(Long idChecador, Long idEmpleado, Date fecha) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<ChecadorDetalle> lista = null;
		DateFormat df = null;
		String fechaFormateada = "";
		
		try {
			df = new SimpleDateFormat("yyyy-MM-dd");
			fechaFormateada = df.format(fecha);

			params = new HashMap<String, Object>();
			params.put("idChecador", idChecador);
			params.put("idEmpleado", idEmpleado);
			params.put("fecha", fechaFormateada);

			lista = this.ifzChecadorDetalles.findByProperties(params, getIdEmpresa(), 120);			
			if (lista == null || lista.isEmpty())
				return null;
			return lista.get(0);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.existeAsistenciaPojo(idEmpleado, fecha)", e);
			throw e;
		}
	}

	@Override
	public Respuesta importarAsistencia(String fileName, byte[] fileSrc, HashMap<String, String> mapLayout) throws Exception {
		Respuesta respuesta = null;
		List<ChecadorDetalle> asistencias = null;
		Empleado empleado = null;
		EmpleadoContrato contrato = null;
		int horasContrato = 0;
		String motivo = "";
		Date fecha = null;
		// Layout ----------------------------------------------
		List<ChecadorSemanalItem> itemsInvalidos = null;
		List<ChecadorSemanalItem> items = null;
		ChecadorSemanalItem item = null;
		HashMap<String, String> columnas = null;
		List<String> columnasRequeridas = null;
		String auxiliar = "";
		int initRow = 0;
		int endRow = 0;
		// Apache POI ------------------------------------------
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		Row row = null;
		Cell celda = null;
		FormulaEvaluator formulaEvaluator = null;
		CellValue cellValue = null;
		
		try {
			respuesta = new Respuesta();
			if (mapLayout == null || mapLayout.isEmpty()) {
				motivo = "No se indico Layout";
				throw new Exception(motivo);
			}
			
			// preparamos layout
			log.info("Procesando Layout");
			auxiliar = (mapLayout != null && mapLayout.containsKey("RENGLON.INICIAL") ? mapLayout.get("RENGLON.INICIAL") : "");
			auxiliar = (auxiliar != null && ! "".equals(auxiliar.trim()) && NumberUtils.isNumber(auxiliar) ? auxiliar.trim() : "1");
			initRow = Integer.parseInt(auxiliar);
			
			auxiliar = (mapLayout != null && mapLayout.containsKey("RENGLON.FINAL") ? mapLayout.get("RENGLON.FINAL") : "");
			auxiliar = (auxiliar != null && ! "".equals(auxiliar.trim()) && NumberUtils.isNumber(auxiliar) ? auxiliar.trim() : "0");
			endRow = Integer.parseInt(auxiliar);
			
			auxiliar = (mapLayout != null && mapLayout.containsKey("COLUMNAS.REQUERIDAS") ? mapLayout.get("COLUMNAS.REQUERIDAS") : "");
			auxiliar = (auxiliar != null && ! "".equals(auxiliar.trim()) ? auxiliar.trim() : "");
			columnasRequeridas = stringToList(auxiliar.trim(), ",");
			
			columnas = new HashMap<String, String>();
			for (Entry<String, String> token : mapLayout.entrySet()) {
				if (token.getKey().toLowerCase().startsWith("columna."))
					columnas.put(token.getKey().toLowerCase().replace("columna.", "").toUpperCase(), token.getValue());
			}
			
			if (columnas == null || columnas.isEmpty()) {
				motivo = "No se pudo interpretar el Layout";
				throw new Exception(motivo);
			}
			
			// Instanciamos libro excel
			log.info("Instanciando libro");
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
			
			log.info("Procesando libro");
			for (int indexRow = 0; rowIterator.hasNext(); indexRow++) {
				row = rowIterator.next();
				if ((indexRow + 1) < initRow)
					continue;
				if (endRow > 0 && (indexRow + 1) > endRow)
					break;
				
				item = new ChecadorSemanalItem();
				item.setRow((indexRow + 1));
				item.setValido(true);
				for (Entry<String, String> columna : columnas.entrySet()) {
					celda = row.getCell(CellReference.convertColStringToIndex(columna.getKey()));
					if ((celda == null || celda.getCellType() == Cell.CELL_TYPE_BLANK) && validarRequerido(columnasRequeridas, columna.getKey())) {
						item.setValido(false);
						item.setObservaciones("Valor requerido en columna " + columna.getKey() + ". Celda no asignada o vacia");
						continue;
					}
					
					if (celda == null)
						continue;
					
					switch (celda.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC:
							if (isNullOrEmpty(celda.getNumericCellValue()) && validarRequerido(columnasRequeridas, columna.getKey())) {
								item.setValido(false);
								item.setObservaciones("Valor requerido en columna " + columna.getKey() + ". Tipo " + (HSSFDateUtil.isCellDateFormatted(celda) ? "Fecha" : "Numerico"));
								continue;
							}
							
							item = inflateValue(item, (HSSFDateUtil.isCellDateFormatted(celda) ? celda.getDateCellValue() : celda.getNumericCellValue()), columna.getValue()); 
							break;

						case Cell.CELL_TYPE_STRING: 
							if (isNullOrEmpty(celda.getStringCellValue()) && validarRequerido(columnasRequeridas, columna.getKey())) {
								item.setValido(false);
								item.setObservaciones("Valor requerido en columna " + columna.getKey() + ". Tipo Alfanumerico");
								continue;
							}
							
							item = inflateValue(item, celda.getStringCellValue().trim(), columna.getValue());
							break;
							
						case Cell.CELL_TYPE_FORMULA: 
							cellValue = formulaEvaluator.evaluate(celda);
							if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
								if (isNullOrEmpty(cellValue.getStringValue()) && validarRequerido(columnasRequeridas, columna.getKey())) {
									item.setValido(false);
									item.setObservaciones("Valor requerido en columna " + columna.getKey() + ". Tipo Alfanumerico");
									continue;
								}
								
								item = inflateValue(item, cellValue.getStringValue().trim(), columna.getValue());
							} else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
								if (isNullOrEmpty(cellValue.getNumberValue()) && validarRequerido(columnasRequeridas, columna.getKey())) {
									item.setValido(false);
									item.setObservaciones("Valor requerido en columna " + columna.getKey() + ". Tipo " + (HSSFDateUtil.isCellDateFormatted(celda) ? "Fecha" : "Numerico"));
									continue;
								}
								
								item = inflateValue(item, (HSSFDateUtil.isCellDateFormatted(celda) ? celda.getDateCellValue() : cellValue.getNumberValue()), columna.getValue()); 
							} else {
								log.warn("501 Not Implemented");
							}
							break;
						
						case Cell.CELL_TYPE_BOOLEAN: 
							log.warn("501 Not Implemented");
							break;
						
						case Cell.CELL_TYPE_ERROR:  
							log.warn("501 Not Implemented");
							break;
						
						default: 
							log.warn("404 Not Found");
							break;
					}
				}
				
				items = (items != null ? items : new ArrayList<ChecadorSemanalItem>());
				itemsInvalidos = (itemsInvalidos != null ? itemsInvalidos : new ArrayList<ChecadorSemanalItem>());
				if (item.isValido())
					items.add(item);
				else
					itemsInvalidos.add(item);
				if (fecha == null)
					fecha = item.getFecha();
			}

			// Comprobacion de empleados 
			log.info("Comprobando empleados");
			asistencias = new ArrayList<ChecadorDetalle>();
			for (ChecadorSemanalItem var : items) {
				empleado = comprobarEmpleado(var.getIdAIR(), var.getEmpleado(), var.getNss());
				if (empleado == null || empleado.getId() == null || empleado.getId() <= 0L) {
					var.setValido(false);
					var.setObservaciones("No se pudo encontrar el Empleado");
					itemsInvalidos.add(var);
					continue;
				}


				// Recupero las jornada de trabajo segun contrato en horas por dia
				contrato = validamosContrato(empleado.getId());
				horasContrato = horasContrato(contrato, 8);
				if (contrato != null) 
					var.asignarDatos(contrato);
				else
					var.asignarEmpleado(empleado.getId(), empleado.getNombre());
				var.validaTiempoAsistido(horasContrato, this.timeZone);
				var.setValido(true);
				
				// Generamos asistencia
				asistencias.add(var.getAsistencia());
			}

			// Terminando
			respuesta.getErrores().setCodigoError(0);
			respuesta.getBody().addValor("fecha", fecha);
			respuesta.getBody().addValor("detalles", asistencias);
			respuesta.getBody().addValor("invalidos", itemsInvalidos);
		} catch (Exception e) {
			motivo = (motivo != null && ! "".equals(motivo.trim()) ? "\n" + motivo.trim() : "\nNo se pudo procesar");
			log.error("Ocurrio un problema al intentar importar la lista de Asistencia: " + fileName, e);
			respuesta.getErrores().setCodigoError(1);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar importar la lista de Asistencia" + motivo);
		}
		
		return respuesta;
	}
	
	// --------------------------------------------------------------------------------------------------------------
	// CONVERSORES
	// --------------------------------------------------------------------------------------------------------------

	@Override
	public ChecadorDetalle convertir(ChecadorDetalleExt extendido) throws Exception {
		return this.convertidor.ChecadorDetalleExtToChecadorDetalle(extendido);
	}
	
	@Override
	public ChecadorDetalleExt convertir(ChecadorDetalle entity) throws Exception {
		return this.convertidor.ChecadorDetalleToChecadorDetalleExt(entity);
	}

	// --------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------------------------

	@Override
	public Long save(ChecadorDetalleExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.ChecadorDetalleExtToChecadorDetalle(extendido));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.save(ChecadorDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public List<ChecadorDetalleExt> saveOrUpdateListExt(List<ChecadorDetalleExt> extendidos) throws Exception {
		List<ChecadorDetalle> entities = null;
		
		try {
			entities = new ArrayList<ChecadorDetalle>();
			for (ChecadorDetalleExt item : extendidos)
				entities.add(this.convertidor.ChecadorDetalleExtToChecadorDetalle(item));
			entities = this.saveOrUpdateList(entities);
			extendidos.clear();
			for (ChecadorDetalle item : entities)
				extendidos.add(this.convertidor.ChecadorDetalleToChecadorDetalleExt(item));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.saveOrUpdateListExt(extendidos)", e);
			throw e;
		}
		
		return extendidos;
	}

	@Override
	public void update(ChecadorDetalleExt extendido) throws Exception {
		try {
			this.update(this.convertidor.ChecadorDetalleExtToChecadorDetalle(extendido));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.update(extendido)", e);
			throw e;
		}
	}

	@Override
	public ChecadorDetalleExt findExtById(Long idChecadorDetalle) throws Exception {
		try {
			return this.convertidor.ChecadorDetalleToChecadorDetalleExt(this.findById(idChecadorDetalle));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findExtById(idChecadorDetalle)", e);
			throw e;
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------
	
	private List<ChecadorDetalle> procesarArchivo(String fileName, byte[] fileSrc) throws Exception {
		boolean toLog = false;
		// ----------------------------
		InputStream file = null;
		Workbook workbook = null;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		Iterator<Cell> cellIterator = null;
		Row row = null;
		Cell celda = null;
		// -----------------------------
		List<ChecadorDetalle> detalles = null;
		ChecadorDetalle item = null;
		SimpleDateFormat formatter = null;
		GregorianCalendar t1 = null;
		GregorianCalendar t2 = null;
		long diff = 0;
		
		try {
			this.detallesLog = new ArrayList<ChecadorDetalle>();
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			
			// Recorremos todas las filas para mostrar el contenido de cada celda
			for (int indexRows = 0; rowIterator.hasNext(); indexRows++) {
				row = rowIterator.next();
				cellIterator = row.cellIterator();
				
				if (indexRows == 0) 
					continue;
				
				item = new ChecadorDetalle();
				for (int indexCell = 0; cellIterator.hasNext(); indexCell++) {
					celda = cellIterator.next();
					
					switch (indexCell) {
					case 0: // ID Empleado
						if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							item.setIdEmpleado((long) celda.getNumericCellValue());
						} else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
							item.setIdEmpleado(Long.valueOf(celda.getStringCellValue()));
						}		
					break;
					
					case 1: // Nombre Empleado 
						item.setNombreEmpleado(celda.getStringCellValue()); 
					break;
					
					case 2: // Fecha
						if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(celda)) {
							item.setFecha(celda.getDateCellValue()); 
						} else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
							formatter = new SimpleDateFormat("dd/MM/yyyy");
							item.setFecha(formatter.parse(celda.getStringCellValue()));
						}
					break;
					
					case 3: // Hora Entrada
						if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(celda)) {
							item.setHoraEntrada(celda.getDateCellValue()); 
						} else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
							formatter = new SimpleDateFormat("HH:mm");
							item.setHoraEntrada(formatter.parse(celda.getStringCellValue()));
						}
					break;
					
					case 4: // Hora Salida
						if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(celda)) {
							item.setHoraSalida(celda.getDateCellValue()); 
						} else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
							formatter = new SimpleDateFormat("HH:mm");
							item.setHoraSalida(formatter.parse(celda.getStringCellValue()));
						}
					break;
					
					case 5: // Hora Entrada Marcada
						if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(celda)) {
							item.setHoraEntradaMarcada(celda.getDateCellValue()); 
						} else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
							formatter = new SimpleDateFormat("HH:mm");
							item.setHoraEntradaMarcada(formatter.parse(celda.getStringCellValue()));
						} else if (celda.getCellType() == Cell.CELL_TYPE_BLANK) {
							item.setHoraEntradaMarcada(null);
							toLog = true; // item.setHoraEntradaMarcada(item.getHoraEntrada());
						}
					break;
					
					case 6: // Hora Salida Marcada
						if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(celda)) {
							item.setHoraSalidaMarcada(celda.getDateCellValue()); 
						} else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
							formatter = new SimpleDateFormat("HH:mm");
							item.setHoraSalidaMarcada(formatter.parse(celda.getStringCellValue()));
						} else if (celda.getCellType() == Cell.CELL_TYPE_BLANK) {
							item.setHoraSalidaMarcada(null);
							toLog = true; // item.setHoraSalidaMarcada(item.getHoraSalida());
						}
					break;
					
					case 7: // Tiempo Asistido
						if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(celda)) {
							item.setTiempoAsistido(celda.getDateCellValue()); 
						} else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
							formatter = new SimpleDateFormat("HH:mm");
							item.setTiempoAsistido(formatter.parse(celda.getStringCellValue()));
						}
					break;
					}
				}
				
				if (item != null) {
					if (toLog) {
						this.detallesLog.add(item);
						toLog = false;
					} else {
						t1 = new GregorianCalendar();
						t1.setTime(item.getHoraSalidaMarcada());				
						t2 = new GregorianCalendar();
						t2.setTime(item.getHoraEntradaMarcada());				
						diff = t1.getTimeInMillis() - t2.getTimeInMillis();
						item.setHorasTrabajadas((int) (diff / 3600000));
						
						if (detalles == null)
							detalles = new ArrayList<ChecadorDetalle>();
						detalles.add(item);
					}
				}
			}
			
			return detalles;
		} catch (Exception e) {
			log.error("Error en formato del archivo: " + fileName, e);
			throw e;
		}
	}

	private List<String> stringToList(String values, String separador) {
		List<String> lista = null;
		String str[] = null;

		if (values == null || "".equals(values.trim()) || ! values.contains(separador))
			return null;
		
		lista = new ArrayList<String>();
		if (! values.contains(separador)) {
			lista.add(values);
		} else {
			str = values.split(",");
			lista = Arrays.asList(str);
		}
		
		return lista;
	}
	
	private boolean validarRequerido(List<String> requeridos, String target) {
		if (requeridos != null && ! requeridos.isEmpty())
			return requeridos.contains(target);
		return false;
	}
	
	private boolean isNullOrEmpty(Object value) {
		return "".equals((value != null && ! "".equals(value.toString().trim()) ? value.toString().trim() : ""));
	}
	
	private ChecadorSemanalItem inflateValue(ChecadorSemanalItem item, Object value, String reflectionMap) {
		String className = "";
		boolean forzarConversion;
		Class<?> clase = null;
		String methodName = "";
		Class<?> metodoType = null;
		Method metodo = null;
		
		try {
			if (item == null || value == null || ! reflectionMap.contains(":"))
				return item;

			forzarConversion = reflectionMap.contains("*");
			methodName = reflectionMap.substring(0, reflectionMap.indexOf(":"));
			className = reflectionMap.substring(reflectionMap.indexOf(":") + 1).replace("*", "");
			metodoType = Class.forName(className);
			if (value.getClass() == java.lang.Double.class) {
				if (metodoType == java.lang.Long.class)
					value = ((Double) value).longValue();
				else if (metodoType == java.math.BigDecimal.class)
					value = new BigDecimal((Double) value);
				else if (forzarConversion && metodoType == java.lang.String.class)
					value = (new BigDecimal((Double) value)).toString();
			} 
			
			if (metodoType != value.getClass()) {
				item.setObservaciones("Tipos incompatibles");
				item.setValido(false);
				return item;
			}
			
			clase = item.getClass();
			metodo = clase.getMethod(methodName, metodoType);
			metodo.invoke(item, value);
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el valor de la celda con reflection : (ChecadorSemanalItem) " + reflectionMap, e);
			item.setObservaciones("No se pudo procesar el registro");
			item.setValido(false);
		}
		
		return item;
	}
	
	private Empleado comprobarEmpleado(Long idEmpleado, String nombreEmpleado, String nssEmpleado) {
		List<Empleado> empleados = null;
		Empleado empleado = null;
		
		try {
			idEmpleado = (idEmpleado != null ? idEmpleado : 0L);
			nssEmpleado = (nssEmpleado != null ? nssEmpleado.trim() : "");
			nombreEmpleado = (nombreEmpleado != null ? nombreEmpleado.trim() : "");
			
			this.ifzEmpleados.setInfoSesion(this.infoSesion);
			if (idEmpleado > 0L)
				empleado = this.ifzEmpleados.findById(idEmpleado);
			if ((empleado == null || empleado.getId() == null || empleado.getId() <= 0L) && ! "".equals(nssEmpleado.trim()))
				empleado = this.ifzEmpleados.findByNss(nssEmpleado);
			if (empleado == null || empleado.getId() == null || empleado.getId() <= 0L) {
				empleados = this.ifzEmpleados.findLikeProperty("nombre", nombreEmpleado, 0);
				if (empleados != null && ! empleados.isEmpty())
					empleado = empleados.get(0);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar comprobar el Empleado: " + idEmpleado + " - " + nombreEmpleado + " - " + nssEmpleado);
		}
		
		return empleado;
	}

	private EmpleadoContrato validamosContrato(long idEmpleado) {
		try {
			this.ifzContratos.setInfoSesion(this.infoSesion);
			return this.ifzContratos.findContrato(idEmpleado);
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar el Contrato del empleado indicado: " + idEmpleado, e);
		}
		
		return null;
	}
	
	public int horasContrato(EmpleadoContrato pojoContrato, int defaultValue) {
		GregorianCalendar timeEntrada = null;
		GregorianCalendar timeSalida = null;
		long horas = 0;
		long minutos = 0;
		
		try {
			horas = defaultValue;
			// Recuperando contrato...
			if (pojoContrato != null) {
				timeEntrada = new GregorianCalendar(this.timeZone);
				timeEntrada.setTime(pojoContrato.getHoraEntrada());
				timeSalida = new GregorianCalendar(this.timeZone);
				timeSalida.setTime(pojoContrato.getHoraSalida());
				
				minutos = timeSalida.getTimeInMillis() - timeEntrada.getTimeInMillis();
				minutos = minutos / (1000 * 60);
				horas = 0;
				while (minutos >= 60) {
					horas += 1;
					minutos = minutos - 60;
					if (minutos < 0)
						minutos = 0;
				}
				
				if (pojoContrato.getHoraEntradaComplemento() != null && pojoContrato.getHoraSalidaComplemento() != null) {
					timeEntrada.setTime(pojoContrato.getHoraEntradaComplemento());
					timeSalida.setTime(pojoContrato.getHoraSalidaComplemento());
					
					minutos = timeSalida.getTimeInMillis() - timeEntrada.getTimeInMillis();
					minutos = minutos / (1000 * 60);
					
					while (minutos >= 60) {
						horas += 1;
						minutos = minutos - 60;
						if (minutos < 0)
							minutos = 0;
					}
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar el Contrato del empleado indicado. Horas default: " + defaultValue, e);
			horas = defaultValue;
		}

		return (new Long(horas)).intValue();
	}
	
	private Long getIdEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getId() : 1L);
	}

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
