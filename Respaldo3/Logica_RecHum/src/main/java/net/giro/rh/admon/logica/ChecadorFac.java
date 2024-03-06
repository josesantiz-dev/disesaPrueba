package net.giro.rh.admon.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.NominaQuincenalItem;
import net.giro.rh.admon.dao.ChecadorDAO;

@Stateless
public class ChecadorFac implements ChecadorRem {
	private static Logger log = Logger.getLogger(ChecadorFac.class);
	private InfoSesion infoSesion;
	private ChecadorDAO ifzChecador;
	private ChecadorDetalleRem ifzAsistencias;
	
	public ChecadorFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzChecador = (ChecadorDAO) ctx.lookup("ejb:/Model_RecHum//ChecadorImp!net.giro.rh.admon.dao.ChecadorDAO");
			this.ifzAsistencias = (ChecadorDetalleRem) ctx.lookup("ejb:/Logica_RecHum//ChecadorDetalleFac!net.giro.rh.admon.logica.ChecadorDetalleRem");
        } catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.ChecadorFac", e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(Checador Checador) throws Exception {
		try {
			return this.ifzChecador.save(Checador, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.save(Checador)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Checador> saveOrUpdateList(List<Checador> entities) throws Exception {
		try {
			return this.ifzChecador.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.save(Checador)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(Checador Checador) throws Exception {
		try {
			this.ifzChecador.update(Checador);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.update(Checador)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idChecador) throws Exception {
		try {
			this.ifzChecador.delete(idChecador);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.delete(idChecador)", e);
			throw e;
		}
	}

	@Override
	public Checador findById(Long idChecador) {
		try {
			return this.ifzChecador.findById(idChecador);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findById(idChecador)", e);
			throw e;
		}
	}

	@Override
	public List<Checador> findAll(long idObra, String orderBy) throws Exception {
		try {
			return this.ifzChecador.findAll(idObra, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findAll(idObra, orderBy)", e);
			throw e;
		} 
	}

	@Override
	public List<Checador> findLike(String value, String orderBy, int limite) throws Exception {
		try {
			while (value.trim().contains("  "))
				value = value.trim().replace("  ", " ");
			value = (value.trim().contains("+") ? value.trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.trim());
			value = (value.trim().contains("|") ? value.trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.trim());
			value = (value.trim().contains(" ") ? value.trim().replace(" ", "%") : value.trim());
			
			return this.ifzChecador.findLike(value, getIdEmpresa(), orderBy, limite);
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	public List<Checador> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), orderBy, limite);
			
			if (value instanceof java.lang.String) {
				while (value.toString().trim().contains("  "))
					value = value.toString().trim().replace("  ", " ");
				value = (value.toString().trim().contains("+") ? value.toString().trim().replace(" + ", "+").replace("+ ", "+").replace(" +", "+") : value.toString().trim());
				value = (value.toString().trim().contains("|") ? value.toString().trim().replace(" | ", "|").replace("| ", "|").replace(" |", "|") : value.toString().trim());
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			}
			
			return this.ifzChecador.findLikeProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Checador> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzChecador.findByProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Checador> findByDate(Date fecha, String obra, String orderBy) throws Exception {
		try {
			return this.ifzChecador.findByDate(fecha, obra, getIdEmpresa(), orderBy);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findByDate(fecha, obra, orderBy)", e);
			throw e;
		}
	}
	
	@Override
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta, String orderBy) throws Exception {
		try {
			return this.ifzChecador.findByDates(fechaDesde, fechaHasta, getIdEmpresa(), orderBy);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findByDates(fechaDesde, fechaHasta, orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta, long idObra, String orderBy) throws Exception {
		try {
			return this.ifzChecador.findByDates(fechaDesde, fechaHasta, idObra, getIdEmpresa(), orderBy);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findByDates(fechaDesde, fechaHasta, idObra, orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<Checador> asistenciasNominas(Date fechaDesde, Date fechaHasta, long idObra, String orderBy) throws Exception {
		try {
			return this.ifzChecador.asistenciasNominas(fechaDesde, fechaHasta, idObra, getIdEmpresa(), orderBy);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.asistenciasNominas(fechaDesde, fechaHasta, idObra, orderBy)", e);
			throw e;
		}
	}

	@Override
	public Respuesta analizarArchivo(byte[] fileSrc, String fileExtension) throws Exception {
		return null;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Respuesta importarChecador(byte[] source, LinkedHashMap<String, String> layout) throws Exception {
		Respuesta respuesta = null;
		// ---------------------------------------------------------------------------
		String controlValue = "";
		List<String> colsRequeridas = null;
		List<String> colsTipoDatos = null;
		LinkedHashMap<Integer, LinkedHashMap<String, Object>> valores = null;
		LinkedHashMap<String, Object> rowValues = null;
		List<NominaQuincenalItem> cargados = null;
		// ---------------------------------------------------------------------------
		String[] splitted = null;
		// ---------------------------------------------------------------------------
		InputStream file = null;
		Workbook workbook = null;
		FormulaEvaluator fEval = null;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		Row row = null;
		Cell celda = null;
		CellValue val = null;
		int sheetIndex = 0;
		int indexRow = 0;
		int intento = 0;
		int tolerancia = 0;
		// ---------------------------------------------------------------------------
		DataFormatter defaultFormat = new DataFormatter();
		String valString = "";
		// ---------------------------------------------------------------------------
		String pojoBase = "";
		Object objectInstance = null;
		Class cls = null;
		Method met = null;
		/*List<EmpleadoNomina> resultados = new ArrayList<EmpleadoNomina>();
		EmpleadoNomina resultado = null;
		// ---------------------------------------------------------------------------
		List<NominaQuincenalItem> cargados = new ArrayList<NominaQuincenalItem>();
		List<NominaQuincenalItem> noProcesados = new ArrayList<NominaQuincenalItem>();
		// ---------------------------------------------------------------------------
		Obra obra = null;
		Empleado empleado = null;
		EmpleadoContrato contrato = null;
		EmpleadoFiniquito finiquito = null;
		boolean finiquitoPermitido = false;
		// ---------------------------------------------------------------------------
		int sheetIndex = 0;
		int indexRow = 0;
		// ---------------------------------------------------------------------------
		InputStream file = null;
		Workbook workbook = null;
		FormulaEvaluator fEval = null;
		Sheet sheet = null;
		Iterator<Row> rowIterator = null;
		Row row = null;
		Cell celda = null;
		CellValue val = null;
		DataFormatter defaultFormat = new DataFormatter();
		String valString = "";
		// ---------------------------------------------------------------------------
		String[] splitted = null;
		int tolerancia = 0;
		int intento = 0;
		// ---------------------------------------------------------------------------
		String controlValue = "";
		List<String> colsRequeridas = null;
		List<String> colsTipoDatos = null;
		LinkedHashMap<Integer, LinkedHashMap<String, Object>> valores = null;
		LinkedHashMap<String, Object> rowValues = null;
		// ---------------------------------------------------------------------------
		String pojoBase = "";
		Object objectInstance = null;
		Class cls = null;
		Method met = null;*/
		
		try {
			respuesta = new Respuesta();
			if (source == null) {
				log.error("### Error 204: No Content. Archivo sin contenido. Source null");
				respuesta.getErrores().setCodigoError(204L);
				respuesta.getErrores().setDescError("No se puede procesar el archivo. No tiene el formato correcto o no tiene contenido");
				return respuesta;
			}
			
			// Inicializaciones
			valores = new LinkedHashMap<Integer, LinkedHashMap<String, Object>>();
			
			controlValue = layout.containsKey("CONTROL") ? layout.get("CONTROL") : "";
			if (controlValue == null || "".equals(controlValue.trim())) {
				log.error("### Error 404: No Content. El layout no tiene una opcion de control");
				respuesta.getErrores().setCodigoError(204L);
				respuesta.getErrores().setDescError("No se puede procesar el archivo. El layout no tiene una opcion de control");
				return respuesta;
			}
			
			layout.remove("CONTROL");
			splitted = controlValue.split("\\|");
			pojoBase = splitted[0];
			colsRequeridas = Arrays.asList(splitted[1].split(","));

			// Instanciamos libro y recuperamos los datos
			file = new ByteArrayInputStream(source);
			workbook = WorkbookFactory.create(file);
			fEval = workbook.getCreationHelper().createFormulaEvaluator();
			sheetIndex = workbook.getNumberOfSheets() - 1;
			sheet = workbook.getSheetAt(sheetIndex);
			rowIterator = sheet.iterator();
			intento = 0;
			
			// Procesando hoja 
			for (indexRow = 0; rowIterator.hasNext(); indexRow++) {
				row = rowIterator.next();
				intento += 1;
				if (! validarColumnasRequeridas(row, colsRequeridas, colsTipoDatos)) {
					if (intento > tolerancia)
						break;
					continue;
				}

				intento = 0;
				rowValues = new LinkedHashMap<String, Object>();
				for (Entry<String, String> mapColumn : layout.entrySet()) {
					rowValues.put(mapColumn.getKey(), "");
					celda = row.getCell(CellReference.convertColStringToIndex(mapColumn.getKey()));
					if (isCellEmpty(celda))
						continue;
					
					valString = defaultFormat.formatCellValue(celda, fEval);
					splitted = mapColumn.getValue().split("\\|");
					if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC && ! HSSFDateUtil.isCellDateFormatted(celda)) 
						rowValues.put(mapColumn.getKey(), aplicarTipo(valString, splitted[0]));
					else if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(celda))
						rowValues.put(mapColumn.getKey(), aplicarTipo(valString, splitted[0]));
					else if (celda.getCellType() == Cell.CELL_TYPE_STRING) 
						rowValues.put(mapColumn.getKey(), aplicarTipo(valString, splitted[0]));
					else if (celda.getCellType() == Cell.CELL_TYPE_FORMULA) {
						val = fEval.evaluate(celda);
						if (val.getCellType() == Cell.CELL_TYPE_NUMERIC)
							rowValues.put(mapColumn.getKey(), aplicarTipo(valString, splitted[0]));
						else if (celda.getCellType() == Cell.CELL_TYPE_STRING) 
							rowValues.put(mapColumn.getKey(), aplicarTipo(valString, splitted[0]));
					}
				}
				
				valores.put(indexRow + 1, rowValues);
			}
			
			// Reflection y comprobacion
			cargados = new ArrayList<NominaQuincenalItem>();
			for (Entry<Integer, LinkedHashMap<String, Object>> item : valores.entrySet()) {
				rowValues = item.getValue();
				objectInstance = (NominaQuincenalItem) Class.forName(pojoBase).newInstance();
				cls = objectInstance.getClass();
				for (Entry<String, String> valor : layout.entrySet()) {
					splitted = valor.getValue().split("\\|");
					met = cls.getMethod(splitted[1], Class.forName(splitted[0].trim()));
					if (rowValues.get(valor.getKey()) == null || "".equals(rowValues.get(valor.getKey()).toString()))
						continue;
					met.invoke(objectInstance, rowValues.get(valor.getKey()));
				}

				met = cls.getMethod("setRowIndex", Integer.class);
				met.invoke(objectInstance, item.getKey());
				if (cls != null)
					cargados.add((NominaQuincenalItem) objectInstance);
			}
			
			/*// Validacion
			nominaLog("NOMINA15 - Validaciones de Items ... ");
			for (NominaQuincenalItem item : cargados) {
				indexRow = item.getRowIndex();
				nominaLog("         - #" + item.getRowIndex() + " Empleado " + item.getNss() + " - ");
				if (! item.completo()) {
					if (! retroactivo || (permitirEmpleadoSinSueldo && ! item.sinPago())) {
						item.setMensaje("Informacion incompleta");
						noProcesados.add(item);
						nominaLog("INCOMPLETO", true);
						continue;
					}
				}

				nominaLog("Validando Obra: ", true);
				obra = this.ifzObras.findById(item.getIdObra());
				if (obra == null || obra.getId() == null || obra.getId() <= 0L) {
					item.setMensaje("No se encontro la Obra asignada al empleado");
					noProcesados.add(item);
					nominaLog("ERROR", true);
					continue;
				}

				nominaLog("OK, Validando NSS: ", true);
				empleado = this.ifzEmpleados.findByNss(item.getNss());
				if (empleado == null || empleado.getId() == null || empleado.getId() <= 0L) {
					item.setMensaje("No se encontro ningun empleado con el Numero de Seguridad Social (NSS) indicado");
					noProcesados.add(item);
					nominaLog("ERROR", true);
					continue;
				}
				
				nominaLog("OK, Validando Finiquito: ", true);
				finiquitoPermitido = false;
				if (! retroactivo && empleado.getEstatus() != 0) {
					if (empleado.getEstatus() == 1) {
						finiquitoPermitido = false;
						item.setMensaje("Dado de Baja el " + (new SimpleDateFormat("dd-MMM-yyyy")).format(empleado.getFechaModificacion()).toUpperCase());
					} else if (empleado.getEstatus() == 2) {
						finiquitoPermitido = false;
						item.setMensaje("Dado de Baja el " + (new SimpleDateFormat("dd-MMM-yyyy")).format(empleado.getFechaModificacion()).toUpperCase() + " por Finiquito");
						
						finiquito = this.ifzFiniquitos.findByIdEmpleado(empleado.getId(), true, false);
						if (finiquito != null && finiquito.getId() != null && finiquito.getId() > 0L) {
							finiquitoPermitido = true;
							// Validamos si el finiquito esta aprobado que la fecha de baja debe ser igual o posterior al inicio del periodo quincenal :: RMESTA - 2019-11-28
							if (finiquito.getAprobacion() == 1) {
								finiquitoPermitido = false;
								item.setMensaje("Dado de Baja el " + (new SimpleDateFormat("dd-MMM-yyyy")).format(finiquito.getFechaSolicitudBaja()).toUpperCase() + " por Finiquito");
								if (finiquito.getFechaSolicitudBaja().compareTo(fechaDesde) > 0) {
									item.setMensaje("Dado de Baja el " + (new SimpleDateFormat("dd-MMM-yyyy")).format(finiquito.getFechaSolicitudBaja()).toUpperCase() + " por Finiquito. Permitido, baja posterior al periodo quincenal");
									finiquitoPermitido = true;
								}
							}
						} 
					} else if (empleado.getEstatus() == 3) {
						finiquitoPermitido = false;
						item.setMensaje("Empleado con incapacidad");
					}
					
					if (! finiquitoPermitido) {
						noProcesados.add(item);
						nominaLog("ERROR", true);
						continue;
					}
				}
				
				nominaLog("OK, Validando Contrato: ", true);
				contrato = this.ifzContratos.findContrato(empleado.getId());
				if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L) {
					if (retroactivo || finiquitoPermitido)
						contrato = this.ifzContratos.findContrato(empleado.getId(), fechaDesde, fechaHasta); 

					/ *contrato = this.ifzContratos.findContrato(empleado.getId(), fechaDesde, fechaHasta); 
					if (retroactivo || finiquitoPermitido) {
						contratos = this.ifzContratos.findAll(empleado.getId(), "estatus,id desc", true, false);
						if (contratos != null && ! contratos.isEmpty())
							contrato = contratos.get(0);
					}* /
					
					if (contrato == null || contrato.getId() == null || contrato.getId() <= 0L) {
						item.setMensaje("El empleado no tiene asignado un Contrato valido");
						noProcesados.add(item);
						nominaLog("ERROR", true);
						continue;
					}
				}

				nominaLog("OK, Completando Item: ", true);
				item.setIdSucursal(obra.getIdSucursal());
				item.setSucursal(obra.getNombreSucursal());
				item.setIdEmpleado(empleado.getId());
				item.setIdContrato(contrato.getId());
				item.setIdPeriodicidad(contrato.getPeriodicidadPago());

				nominaLog("OK, Generando Nomina: ", true);
				resultado = new EmpleadoNomina();
				resultado.setTipo(1);
				resultado.setFecha(fechaHasta);
				resultado.setIdEmpleado(empleado);
				resultado.setIdContrato(item.getIdContrato());
				resultado.setIdPeriodicidad(item.getIdPeriodicidad());
				resultado.setIdObra(item.getIdObra());
				resultado.setHorasTrabajadas(96);
				resultado.setHorasExtras(0);
				resultado.setPagoNormal(new BigDecimal(item.getSueldoQuincenal()));
				resultado.setPagoExtra(new BigDecimal(item.getPagoExtra()));
				resultado.setDescuento(new BigDecimal(item.getDescuentos()));
				resultado.setPagoNeto(new BigDecimal(item.getSueldoNeto()));
				resultado.setIdEmpresa(getIdEmpresa());
				resultado.setObservaciones(item.getObservaciones());
				resultado.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
				resultado.setFechaCreacion(Calendar.getInstance().getTime());
				resultado.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
				resultado.setFechaModificacion(Calendar.getInstance().getTime());
				resultados.add(resultado);
				nominaLog("OK", true);
			}
			nominaLog("NOMINA15 - Validaciones ... OK");

			respuesta.getBody().addValor("nominaQuincenal", resultados);
			respuesta.getBody().addValor("sinProcesar", noProcesados);
			nominaLog("NOMINA15 - Terminado!");*/
		} catch (Exception e) {
			log.error("### Error al leer el archivo EXCEL. Se proceso hasta la fila " + indexRow, e);
			respuesta.getErrores().setCodigoError(500L);
			respuesta.getErrores().setDescError("Error al procesar el Libro.\n" + e.getMessage());
		} 

		return respuesta;
	}

	@Override
	public List<Checador> findAsistencias(String propertyName, Object value, Long idObra, String orderBy, int limite) throws Exception {
		List<ChecadorDetalle> asistencias = null;
		List<Long> lista = null;
		
		try {
			if (value == null || "".equals(value.toString().trim()))
				return this.findLikeProperty(propertyName, value, orderBy, limite);
			
			this.ifzAsistencias.setInfoSesion(this.infoSesion);
			asistencias = this.ifzAsistencias.findLikeProperty(propertyName, value, idObra, orderBy, limite);
			if (asistencias == null || asistencias.isEmpty())
				return null;
			
			lista = new ArrayList<Long>();
			for (ChecadorDetalle asistencia : asistencias)
				lista.add(asistencia.getIdChecador().getId());
			return this.ifzChecador.findList(lista,  orderBy);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorFac.findAsistencias(propertyName, value, idObra, orderBy, limite)", e);
			throw e;
		} 
	}

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------

	private boolean validarColumnasRequeridas(Row row, List<String> columnasRequeridas, List<String> tiposDatosColumnas) {
		Class<?> claseColumna = null;
		Cell cell = null;
		String tipo = "";
		int index = 0;
		
		try {
			for (String col : columnasRequeridas) {
				index = columnasRequeridas.indexOf(col);
				cell = row.getCell(CellReference.convertColStringToIndex(col));
				if (isCellEmpty(cell))
					return false;
				
				tipo = tiposDatosColumnas.get(index);
				claseColumna = Class.forName(tipo.trim());
				if (claseColumna == java.lang.String.class && cell.getCellType() != Cell.CELL_TYPE_STRING)
					return false;
				else if (claseColumna == java.lang.Double.class && cell.getCellType() != Cell.CELL_TYPE_NUMERIC)
					return (cell.getCellType() == Cell.CELL_TYPE_STRING) ? validateToNumber(cell.getStringCellValue()) : false;
				else if (claseColumna == java.lang.Long.class && cell.getCellType() != Cell.CELL_TYPE_NUMERIC)
					return (cell.getCellType() == Cell.CELL_TYPE_STRING) ? validateToNumber(cell.getStringCellValue()) : false;
				else if (claseColumna == java.lang.Integer.class && cell.getCellType() != Cell.CELL_TYPE_NUMERIC)
					return (cell.getCellType() == Cell.CELL_TYPE_STRING) ? validateToNumber(cell.getStringCellValue()) : false;
				else if (claseColumna == java.math.BigDecimal.class && cell.getCellType() != Cell.CELL_TYPE_NUMERIC)
					return (cell.getCellType() == Cell.CELL_TYPE_STRING) ? validateToNumber(cell.getStringCellValue()) : false;
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar los tipos de datos de las columnas requeridas", e);
		}
		
		return true;
	}

	private boolean validateToNumber(String value) {
		String noValidos = "´'\"`^'\n";
		String[] splitted = null;

		splitted = value.split("");
		value = "";
		for (int i = 0; i < splitted.length; i++) {
			if (noValidos.contains(splitted[i]))
				continue;
			value += splitted[i];
		}
		value = value.trim();
	
		return value.matches("-?\\d+(\\.\\d+)?");
	}

	private boolean isCellEmpty(Cell cell) {
		if (cell == null)
			return true;
		if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
			return true;
		if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().trim().isEmpty())
			return true;
		return false;
	}

	private Object aplicarTipo(String value, String tipo) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Class<?> claseColumna = null;
		String noValidos = "´'\"`^'\n";
		String[] splitted = null;
		
		try {
			claseColumna = Class.forName(tipo.trim());
			splitted = value.split("");
			value = "";
			for (int i = 0; i < splitted.length; i++) {
				if (noValidos.contains(splitted[i]))
					continue;
				value += splitted[i];
			}
			value = value.trim();
			if (claseColumna == java.lang.Long.class)
				return Long.valueOf(getNumeric(value, false));
			if (claseColumna == java.lang.Double.class)
				return Double.valueOf(getNumeric(value, true));
			if (claseColumna == java.util.Date.class)
				return formatter.parse(value.replace("/", "-"));
		} catch (Exception e) {
			log.error("Ocurrio un problema al aplicar el tipo de dato requerido. " + value + "::" + tipo, e);
		}

		return value;
	}

	private String getNumeric(String value, boolean incluyeDecimales) {
		DecimalFormat df = new DecimalFormat("#");
		String permitidos = "1234567890";
		String[] splitted = null;
		
		if (incluyeDecimales) {
			df.applyPattern("#.00");
			permitidos += ".";
		}
		
		if (value == null || "".equals(value.trim()))
			return "";
		
		splitted = value.trim().split("");
		value = "";
		for (int i = 0; i < splitted.length; i++) {
			if ("".equals(splitted[i])) continue;
			if (permitidos.contains(splitted[i]))
				value += splitted[i];
		}
		
		if ("".equals(value.trim())) return "";
		value = df.format(new BigDecimal(value));
		
		return value;
	}
	
	private Long getIdEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getId() : 1L);
	}

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
}
