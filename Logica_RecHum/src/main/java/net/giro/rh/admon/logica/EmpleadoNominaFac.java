package net.giro.rh.admon.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import org.apache.commons.lang.StringUtils;
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

import net.giro.adp.beans.Obra;
import net.giro.adp.dao.ObraDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosRH;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.beans.EmpleadoNomina;
import net.giro.rh.admon.beans.EmpleadoNominaEstatus;
import net.giro.rh.admon.beans.EmpleadoNominaPreliminar;
import net.giro.rh.admon.beans.NominaQuincenalItem;
import net.giro.rh.admon.dao.EmpleadoNominaDAO;
import net.giro.rh.admon.dao.EmpleadoNominaEstatusDAO;
import net.giro.rh.admon.dao.EmpleadoNominaPreliminarDAO;

@Stateless
public class EmpleadoNominaFac implements EmpleadoNominaRem {
	private static Logger log = Logger.getLogger(EmpleadoNominaFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private EmpleadoNominaDAO ifzEmpleadoNomina;
	private EmpleadoNominaPreliminarDAO ifzEmpleadoNominaPreliminar;
	private EmpleadoNominaEstatusDAO ifzNominaEstatus;
	private ChecadorRem ifzListas;
	private EmpleadoRem ifzEmpleados;
	private ObraDAO ifzObras;
	private EmpleadoContratoRem ifzContratos;
	private EmpleadoFiniquitoRem ifzFiniquitos;
	private static String orderBy;
	private List<String> nominaLog;
	
	public EmpleadoNominaFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(environment);
			this.ifzEmpleadoNomina = (EmpleadoNominaDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoNominaImp!net.giro.rh.admon.dao.EmpleadoNominaDAO");
			this.ifzEmpleadoNominaPreliminar = (EmpleadoNominaPreliminarDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoNominaPreliminarImp!net.giro.rh.admon.dao.EmpleadoNominaPreliminarDAO");
			this.ifzNominaEstatus = (EmpleadoNominaEstatusDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoNominaEstatusImp!net.giro.rh.admon.dao.EmpleadoNominaEstatusDAO");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzContratos = (EmpleadoContratoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			this.ifzFiniquitos = (EmpleadoFiniquitoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFiniquitoFac!net.giro.rh.admon.logica.EmpleadoFiniquitoRem");
			this.ifzListas = (ChecadorRem) this.ctx.lookup("ejb:/Logica_RecHum//ChecadorFac!net.giro.rh.admon.logica.ChecadorRem");
			this.ifzObras = (ObraDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraImp!net.giro.adp.dao.ObraDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.EmpleadoNominaFac", e);
			ctx = null;
		}
	}
	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void orderBy(String orderBy) { EmpleadoNominaFac.orderBy = orderBy; }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(EmpleadoNomina entity) throws Exception {
		try {
			return this.ifzEmpleadoNomina.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.save(EmpleadoNomina)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<EmpleadoNomina> saveOrUpdateList(List<EmpleadoNomina> entities) throws Exception {
		try {
			return this.ifzEmpleadoNomina.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(EmpleadoNomina entity) throws Exception {
		try {
			this.ifzEmpleadoNomina.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.update(EmpleadoNomina)", e);
			throw e;
		}
	}

	@Override
	public void delete(long entityId) throws Exception {
		try {
			this.ifzEmpleadoNomina.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoNomina> deleteAll(List<EmpleadoNomina> entities) throws Exception {
		try {
			return this.ifzEmpleadoNomina.deleteAll(entities);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.deleteAll(entities)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoNomina findById(long entityId) {
		try {
			return this.ifzEmpleadoNomina.findById(entityId);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoNomina> findAll() throws Exception {
		try {
			this.ifzEmpleadoNomina.orderBy(orderBy);
			return this.ifzEmpleadoNomina.findAll();
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findAll()", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoNomina> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzEmpleadoNomina.orderBy(orderBy);
			return this.ifzEmpleadoNomina.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoNomina> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzEmpleadoNomina.orderBy(orderBy);
			return this.ifzEmpleadoNomina.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoNomina> findByDates(Date fechaDesde, Date fechaHasta) throws Exception {
		try {
			this.ifzEmpleadoNomina.orderBy(orderBy);
			return this.ifzEmpleadoNomina.findByDates(fechaDesde, fechaHasta, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findByDates(Date fechaDesde, Date fechaHasta)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta nominaSemanal(Date fechaDesde, Date fechaHasta, boolean recalcular, boolean preliminar) throws Exception {
		if (preliminar)
			return this.generarNominaSemanalPreliminar(fechaDesde, fechaHasta);
		return this.generarNominaSemanal(fechaDesde, fechaHasta, recalcular);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta nominaQuincenal(Date fechaDesde, Date fechaHasta, boolean recalcular, boolean preliminar) throws Exception {
		/*if (preliminar)
			return this.generarNominaQuincenalPreliminar(fechaDesde, fechaHasta);*/
		return this.generarNominaQuincenal(fechaDesde, fechaHasta, recalcular);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Respuesta importarNominaQuincenal(Date fechaDesde, Date fechaHasta, byte[] source, LinkedHashMap<String, String> layout, boolean retroactivo, boolean permitirEmpleadoSinSueldo) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<EmpleadoNomina> resultados = new ArrayList<EmpleadoNomina>();
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
		Method met = null;
		
		try {
			if (source == null) {
				log.error("### Error 204: No Content. Archivo sin contenido. Source null");
				respuesta.getErrores().setCodigoError(204L);
				respuesta.getErrores().setDescError("No se puede procesar el archivo. No tiene el formato correcto o no tiene contenido");
				return respuesta;
			}
			
			// Inicializaciones
			nominaLog("NOMINA15 - Inicializaciones ... ");
			valores = new LinkedHashMap<Integer, LinkedHashMap<String,Object>>();
			controlValue = layout.get("CONTROL");
			layout.remove("CONTROL");
			splitted = controlValue.split("\\|");
			colsRequeridas = Arrays.asList(splitted[0].split(","));
			colsTipoDatos = Arrays.asList(splitted[1].split(","));
			tolerancia = Integer.parseInt(splitted[2]);
			pojoBase = splitted[3];
			nominaLog("OK", true);

			// Instanciamos libro y recuperamos los datos
			nominaLog("NOMINA15 - Cargando libro ... ");
			file = new ByteArrayInputStream(source);
			workbook = WorkbookFactory.create(file);
			fEval = workbook.getCreationHelper().createFormulaEvaluator();
			sheetIndex = workbook.getNumberOfSheets() - 1;
			sheet = workbook.getSheetAt(sheetIndex);
			rowIterator = sheet.iterator();
			intento = 0;
			nominaLog("OK", true);
			
			nominaLog("NOMINA15 - Procesando hoja (#" + sheetIndex + ") - " + sheet.getSheetName() + " ... ");
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
			nominaLog("OK", true);
			
			// Reflection y comprobacion
			nominaLog("NOMINA15 - Reflection ... ");
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
			nominaLog("OK", true);
			
			// Validacion
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

					/*contrato = this.ifzContratos.findContrato(empleado.getId(), fechaDesde, fechaHasta); 
					if (retroactivo || finiquitoPermitido) {
						contratos = this.ifzContratos.findAll(empleado.getId(), "estatus,id desc", true, false);
						if (contratos != null && ! contratos.isEmpty())
							contrato = contratos.get(0);
					}*/
					
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
			nominaLog("NOMINA15 - Terminado!");
		} catch (Exception e) {
			log.error("### Error al leer el archivo EXCEL. Se proceso hasta la fila " + indexRow, e);
			respuesta.getErrores().setCodigoError(500L);
			respuesta.getErrores().setDescError("Error al procesar el Libro.\n" + e.getMessage());
			nominaLog("### Error al leer el archivo EXCEL. Se proceso hasta la fila " + indexRow);
		} finally {
			log.info(nominaLog());
		}

		return respuesta;
	}

	@Override
	public int comprobarCalculoNomina(long idNominaEstatus) throws Exception {
		EmpleadoNominaEstatus nominaEstatus = null;
		int estatusNomina = -1;
		
		try {
			nominaEstatus = this.ifzNominaEstatus.findById(idNominaEstatus);
			if (nominaEstatus == null || nominaEstatus.getId() == null || nominaEstatus.getId() <= 0L)
				return estatusNomina;
			estatusNomina = nominaEstatus.getEstatus();
		} catch (Exception e) {
			log.error("Ocurrio un problema al comprobar la peticion de Calculo de Nomina", e);
			estatusNomina = -1;
		} finally {
			if (nominaEstatus != null && nominaEstatus.getId() != null && nominaEstatus.getId() > 0L && estatusNomina == -1) {
				nominaEstatus.setEstatus(1);
				nominaEstatus.setMensaje("ERROR: No se pudo comprobar la peticion");
				nominaEstatus.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzNominaEstatus.update(nominaEstatus);
			}
		}
		
		return estatusNomina;
	}

	@Override
	public EmpleadoNominaEstatus findNominaEstatus(long idEstatus) {
		try {
			return this.ifzNominaEstatus.findById(idEstatus);
		} catch (Exception e) {
			log.error("Ocurrio un problema al comprobar la peticion de Calculo de Nomina", e);
			throw e;
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// PRELIMINAR
	// --------------------------------------------------------------------------------------------------------

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<EmpleadoNominaPreliminar> saveOrUpdateListPreliminar(List<EmpleadoNomina> entities) throws Exception {
		List<EmpleadoNominaPreliminar> preliminares = null; 
		EmpleadoNominaPreliminar preliminar = null;
		
		try {
			preliminares = new ArrayList<EmpleadoNominaPreliminar>();
			for (EmpleadoNomina entity : entities) {
				preliminar = new EmpleadoNominaPreliminar();
				preliminar.convetir(entity);
				preliminares.add(preliminar);
			}
			return this.ifzEmpleadoNominaPreliminar.saveOrUpdateList(preliminares, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.saveOrUpdateListPreliminar(entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<EmpleadoNominaPreliminar> deleteAllPreliminar(List<EmpleadoNominaPreliminar> entities) throws Exception {
		try {
			return this.ifzEmpleadoNominaPreliminar.deleteAll(entities);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.deleteAllPreliminar(entities)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoNominaPreliminar> findByDatesPreliminar(Date fechaDesde, Date fechaHasta) throws Exception {
		try {
			return this.ifzEmpleadoNominaPreliminar.findByDates(fechaDesde, fechaHasta, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findByDatesPreliminar(Date fechaDesde, Date fechaHasta)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------

	private void nominaLog(String mensaje) {
		nominaLog(mensaje, false);
	}

	private void nominaLog(String mensaje, boolean append) {
		if (this.nominaLog == null)
			this.nominaLog = new ArrayList<String>();
		
		if (! append) {
			this.nominaLog.add(mensaje);
			log.info(mensaje);
			return;
		}
		
		mensaje = this.nominaLog.get(this.nominaLog.size() - 1) + mensaje;
		this.nominaLog.set(this.nominaLog.size() - 1, mensaje);
	}
	
	private String nominaLog() {
		return StringUtils.join(this.nominaLog, "\n");
	}
	
	private Respuesta generarNominaSemanal(Date fechaDesde, Date fechaHasta, boolean recalcular) throws Exception {
		Respuesta res = new Respuesta();
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		List<EmpleadoNomina> regExistentes = new ArrayList<EmpleadoNomina>();
		// -----------------------------------------------------------------
		List<Checador> listas = null;
		long idNominaEstatus = 0L;
		
		try {
			if (recalcular) {
				this.ifzEmpleadoNomina.orderBy("idEmpleado, fecha");
				regExistentes = this.ifzEmpleadoNomina.findByDates(fechaDesde, fechaHasta, getIdEmpresa());
				if (regExistentes != null && ! regExistentes.isEmpty())
					regExistentes = this.ifzEmpleadoNomina.deleteAll(regExistentes);
			}
			
			// Listas de asistencias autorizadas
			formatter.applyPattern("dd-MM-yyyy");
			log.info("Recuperando listas (" + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta) + ") ...");
			this.ifzListas.setInfoSesion(this.infoSesion);
			listas = this.ifzListas.asistenciasNominas(fechaDesde, fechaHasta, 0L, null);
			if (listas == null || listas.isEmpty()) {
				res.getErrores().addCodigo("RECHUM", 5L);
				res.setBody(null); 
				return res;
			}
			
			idNominaEstatus = this.ifzNominaEstatus.save(fechaDesde, fechaHasta, false, this.infoSesion.getAcceso().getUsuario().getId(), getIdEmpresa(), getCodigoEmpresa());
			if (idNominaEstatus <= 0L) {
				res.getErrores().setCodigoError(1L);
				res.getErrores().setDescError("Ocurrio un problema al guardar la peticion del proceso de Nomina");
				return res;
			}
			
			res.getErrores().setCodigoError(0L);
			res.getErrores().setDescError("Nomina generada");
			res.getBody().addValor("idNominaEstatus", idNominaEstatus);
			formatter.applyPattern("dd-MM-yyyy");
			log.info("Nomina (" + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta) + ") registrada!");
			
			// Lanzamos la peticion
			sendGenerarNominaSemanal(idNominaEstatus, false);
			return res;
		} catch (Exception e) {
			formatter.applyPattern("dd-MM-yyyy");
			log.error("Ocurrio un problema al calcular la Nomina (" + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta) + ")", e);
			throw e;
		}
	}

	private Respuesta generarNominaSemanalPreliminar(Date fechaDesde, Date fechaHasta) throws Exception {
		Respuesta res = new Respuesta();
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		List<EmpleadoNominaPreliminar> regExistentes = null;
		// -----------------------------------------------------------------
		List<Checador> listas = null;
		long idNominaEstatus = 0L;
		
		try {
			regExistentes = this.ifzEmpleadoNominaPreliminar.findByDates(fechaDesde, fechaHasta, getIdEmpresa());
			if (regExistentes != null && ! regExistentes.isEmpty())
				this.ifzEmpleadoNominaPreliminar.deleteAll(regExistentes);
			
			// Listas de asistencias autorizadas
			formatter.applyPattern("dd-MM-yyyy");
			log.info("Recuperando listas (" + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta) + ") ...");
			this.ifzListas.setInfoSesion(this.infoSesion);
			listas = getListasAsistencias(fechaDesde, fechaHasta); 
			if (listas == null || listas.isEmpty()) {
				res.getErrores().addCodigo("RECHUM", 5L);
				res.setBody(null); 
				return res;
			}
			
			idNominaEstatus = this.ifzNominaEstatus.save(fechaDesde, fechaHasta, true, this.infoSesion.getAcceso().getUsuario().getId(), getIdEmpresa(), getCodigoEmpresa());
			if (idNominaEstatus <= 0L) {
				res.getErrores().setCodigoError(1L);
				res.getErrores().setDescError("Ocurrio un problema al guardar la peticion del proceso de Nomina preliminar");
				return res;
			}
			
			res.getErrores().setCodigoError(0L);
			res.getErrores().setDescError("Nomina preliminar generada");
			res.getBody().addValor("idNominaEstatus", idNominaEstatus);
			formatter.applyPattern("dd-MM-yyyy");
			log.info("Nomina preliminar (" + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta) + ") registrada!");
			
			// Lanzamos la peticion
			sendGenerarNominaSemanal(idNominaEstatus, true);
			return res;
		} catch (Exception e) {
			formatter.applyPattern("dd-MM-yyyy");
			log.error("Ocurrio un problema al calcular la Nomina preliminar (" + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta) + ")", e);
			throw e;
		}
	}

	private Respuesta generarNominaQuincenal(Date fechaDesde, Date fechaHasta, boolean recalcular) throws Exception {
		Respuesta res = new Respuesta();
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		List<EmpleadoNomina> regExistentes = new ArrayList<EmpleadoNomina>();
		// -----------------------------------------------------------------
		List<Checador> listas = null;
		long idNominaEstatus = 0L;
		
		try {
			if (recalcular) {
				this.ifzEmpleadoNomina.orderBy("idEmpleado, fecha");
				regExistentes = this.ifzEmpleadoNomina.findByDates(fechaDesde, fechaHasta, getIdEmpresa());
				if (regExistentes != null && ! regExistentes.isEmpty())
					regExistentes = this.ifzEmpleadoNomina.deleteAll(regExistentes);
			}
			
			// Listas de asistencias autorizadas
			formatter.applyPattern("dd-MM-yyyy");
			log.info("Recuperando listas (" + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta) + ") ...");
			this.ifzListas.setInfoSesion(this.infoSesion);
			listas = this.ifzListas.asistenciasNominas(fechaDesde, fechaHasta, 0L, null);
			if (listas == null || listas.isEmpty()) {
				res.getErrores().addCodigo("RECHUM", 5L);
				res.setBody(null); 
				return res;
			}
			
			idNominaEstatus = this.ifzNominaEstatus.save(fechaDesde, fechaHasta, false, this.infoSesion.getAcceso().getUsuario().getId(), getIdEmpresa(), getCodigoEmpresa());
			if (idNominaEstatus <= 0L) {
				res.getErrores().setCodigoError(1L);
				res.getErrores().setDescError("Ocurrio un problema al guardar la peticion del proceso de Nomina");
				return res;
			}
			
			res.getErrores().setCodigoError(0L);
			res.getErrores().setDescError("Nomina generada");
			res.getBody().addValor("idNominaEstatus", idNominaEstatus);
			formatter.applyPattern("dd-MM-yyyy");
			log.info("Nomina (" + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta) + ") registrada!");
			
			// Lanzamos la peticion
			sendGenerarNominaQuincenal(idNominaEstatus, false);
			return res;
		} catch (Exception e) {
			formatter.applyPattern("dd-MM-yyyy");
			log.error("Ocurrio un problema al calcular la Nomina (" + formatter.format(fechaDesde) + " | " + formatter.format(fechaHasta) + ")", e);
			throw e;
		}
	}
	
	private List<Checador> getListasAsistencias(Date fechaDesde, Date fechaHasta) {
		List<Checador> listas = null;
		List<Checador> aux = null;
		
		try {
			listas = this.ifzListas.findByDates(fechaDesde, fechaHasta, 0L, null);
			if (listas != null && ! listas.isEmpty()) {
				aux = new ArrayList<Checador>();
				aux.addAll(listas);
				listas.clear();
				for (Checador lista : aux) {
					if (lista.getEstatus() == 1)
						continue;
					listas.add(lista);
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar y filtrar las listas de asistencias", e);
			listas = null;
		}
		
		return listas;
	}
	
	private void sendGenerarNominaSemanal(Long idNominaEstatus, boolean nominaPreliminar) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idNominaEstatus == null)
				idNominaEstatus = 0L;
			target = idNominaEstatus.toString();
			referencia = (nominaPreliminar ? "1" : "0");
			msgTopic = new MensajeTopic(TopicEventosRH.NOMINA_SEMANAL, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al lanzar evento topic/RH:EM_NOMINA\n\n" + comando + "\n\n", e);
		}
	}

	private void sendGenerarNominaQuincenal(Long idNominaEstatus, boolean nominaPreliminar) {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idNominaEstatus == null)
				idNominaEstatus = 0L;
			target = idNominaEstatus.toString();
			referencia = (nominaPreliminar ? "1" : "0");
			msgTopic = new MensajeTopic(TopicEventosRH.NOMINA_QUINCENAL, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al lanzar evento topic/RH:EM_NOMINA15\n\n" + comando + "\n\n", e);
		}
	}

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
		/*String noValidos = "´'\"`^'\n";
		String[] splitted = null;
		
		try {
			splitted = value.split("");
			value = "";
			for (int i = 0; i < splitted.length; i++) {
				if (noValidos.contains(splitted[i]))
					continue;
				value += splitted[i];
			}
			value = value.trim();
			Double.parseDouble(value);
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar el valor String a Double", e);
			return false;
		}
		
		return true;*/
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
		Long resultado = 1L;
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaFac