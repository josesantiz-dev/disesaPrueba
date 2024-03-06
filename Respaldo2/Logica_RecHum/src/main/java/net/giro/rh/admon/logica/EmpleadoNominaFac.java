package net.giro.rh.admon.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoDescuento;
import net.giro.rh.admon.beans.EmpleadoNomina;
import net.giro.rh.admon.beans.NominaQuincenalItem;
import net.giro.rh.admon.dao.EmpleadoNominaDAO;
import net.giro.util.DateIterator;

@Stateless
public class EmpleadoNominaFac implements EmpleadoNominaRem {
	private static Logger log = Logger.getLogger(EmpleadoNominaFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private EmpleadoNominaDAO ifzEmpleadoNomina;
	private EmpleadoRem ifzEmpleados;
	private static String orderBy;
	
	
	public EmpleadoNominaFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzEmpleadoNomina 	= (EmpleadoNominaDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoNominaImp!net.giro.rh.admon.dao.EmpleadoNominaDAO");
			this.ifzEmpleados 		= (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
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
	public Long save(EmpleadoNomina entity) throws Exception {
		try {
			return this.ifzEmpleadoNomina.save(entity, null);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.save(EmpleadoNomina)", e);
			throw e;
		}
	}

	@Override
	public void update(EmpleadoNomina entity) throws Exception {
		try {
			this.ifzEmpleadoNomina.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.update(EmpleadoNomina)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzEmpleadoNomina.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoNomina findById(Long id) {
		try {
			return this.ifzEmpleadoNomina.findById(id);
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
			return this.ifzEmpleadoNomina.findByProperty(propertyName, value, limite);
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
			return this.ifzEmpleadoNomina.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoNomina> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzEmpleadoNomina.orderBy(orderBy);
			return this.ifzEmpleadoNomina.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoNomina> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzEmpleadoNomina.orderBy(orderBy);
			return this.ifzEmpleadoNomina.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoNomina> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzEmpleadoNomina.orderBy(orderBy);
			return this.ifzEmpleadoNomina.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	public Respuesta generarNomina(Date fechaDesde, Date fechaHasta, boolean recalcular) throws Exception {
		Respuesta res = new Respuesta();
		List<EmpleadoNomina> regExistentes = new ArrayList<EmpleadoNomina>();;
		List<EmpleadoNomina> nomina = new ArrayList<EmpleadoNomina>();
		List<EmpleadoContrato> listaContratos = new ArrayList<EmpleadoContrato>();
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		// Interfaces			
		ChecadorDetalleFac asistencias = new ChecadorDetalleFac();
		EmpleadoFac empleados = new EmpleadoFac();
		EmpleadoContratoFac contratos = new EmpleadoContratoFac();
		EmpleadoDescuentoFac retenciones = new EmpleadoDescuentoFac();
		EmpleadoNomina item;
		EmpleadoNomina itemAux;
		EmpleadoContrato contrato;
		Empleado empleado;
		// Variables de calculo
		double sueldoNormal = 0;
		double sueldoExtra = 0;
		double montoRetenciones = 0;
		double montoNeto = 0;
		int horasExtrasAutorizadas = 0;
		
		try {
			if (recalcular) {
				this.ifzEmpleadoNomina.orderBy("idEmpleado, fecha");
				regExistentes = this.ifzEmpleadoNomina.findByDates(fechaDesde, fechaHasta);
			}
			
			asistencias.orderBy("idEmpleado, fecha");
			List<ChecadorDetalle> listaAsistencia = asistencias.findByDates(fechaDesde, fechaHasta); 
			if (listaAsistencia == null || listaAsistencia.isEmpty()) {
				res.getErrores().addCodigo("RECHUM", 5L);
				res.setBody(null); 
				return res;
			}
			
			boolean existente = false;
			for(ChecadorDetalle var : listaAsistencia) {
				// Comprobamos el registro existente
				itemAux = null;
				existente = false;
				formatter.applyPattern("MM-dd-yyyy");
				for (EmpleadoNomina en : regExistentes) {
					if (var.getIdEmpleado().equals(en.getIdEmpleado().getId()) && formatter.format(var.getFecha()).equals(formatter.format(en.getFecha()))) {
						itemAux = en;
						existente = true;
						break;
					} 
				}
				
				// Recuperamos el empleado si corresponde
				if (! existente)
					empleado = empleados.findById(var.getIdEmpleado());
				else
					empleado = itemAux.getIdEmpleado();
				
				// Comprobamos si tiene contrato vigente
				listaContratos = contratos.contratoValido(var.getIdEmpleado());
				if (listaContratos == null || listaContratos.isEmpty()) continue;
				contrato = listaContratos.get(0);
				
				// Calculamos sueldo normal y extra
				formatter.applyPattern("HH");
				horasExtrasAutorizadas = 0;
				if (var.getHorasExtraAutorizadas() != null && var.getUsuarioAutoriza() > 0)
					horasExtrasAutorizadas = Integer.parseInt(formatter.format(var.getHorasExtraAutorizadas()));
				sueldoNormal = contrato.getSueldoHora().multiply(new BigDecimal(var.getHorasTrabajadas())).doubleValue();
				sueldoExtra = contrato.getSueldoHoraExtra().multiply(new BigDecimal(horasExtrasAutorizadas)).doubleValue();
				
				// comprobamos retenciones
				montoRetenciones = 0;
				List<EmpleadoDescuento> listaDescuentos = retenciones.comprobarDescuentoPorFechas(var.getIdEmpleado(), var.getFecha());
				if (listaDescuentos != null && ! listaDescuentos.isEmpty()) {
					for (EmpleadoDescuento des : listaDescuentos) {
						montoRetenciones += des.getMonto().doubleValue();
					}
				}
				
				// Calculamos el sueldo neto
				montoNeto = (sueldoNormal + sueldoExtra) - montoRetenciones;
				
				if (! existente) {
					item = new EmpleadoNomina();
					item.setIdEmpleado(empleado);
					item.setFecha(var.getFecha());
					item.setMontoNormal(new BigDecimal(sueldoNormal));
					item.setMontoExtra(new BigDecimal(sueldoExtra));
					item.setMontoRetenciones(new BigDecimal(montoRetenciones));
					item.setMontoNeto(new BigDecimal(montoNeto));
					
					item.setCreadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
					item.setFechaCreacion(Calendar.getInstance().getTime());
					item.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
					item.setFechaModificacion(Calendar.getInstance().getTime());
					
					 // Guardamos, asignamos ID y añadimos a la lista resultante
					item.setId(this.save(item));
					nomina.add(item);
				} else {
					itemAux.setMontoNormal(new BigDecimal(sueldoNormal));
					itemAux.setMontoExtra(new BigDecimal(sueldoExtra));
					itemAux.setMontoRetenciones(new BigDecimal(montoRetenciones));
					itemAux.setMontoNeto(new BigDecimal(montoNeto));
					
					itemAux.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
					itemAux.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Actualizamos y añadimos a la lista resultante
					this.update(itemAux);
					nomina.add(itemAux);
				}
			}
			
			res.getErrores().setCodigoError(0L);
			res.getBody().addValor("nomina", nomina);
			
			return res;
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.generarNomina(Date fechaDesde, Date fechaHasta, boolean recalcular)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoNomina> findByDates(Date fechaDesde, Date fechaHasta) throws Exception {
		try {
			this.ifzEmpleadoNomina.orderBy(orderBy);
			return this.ifzEmpleadoNomina.findByDates(fechaDesde, fechaHasta);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.findByDates(Date fechaDesde, Date fechaHasta)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public Respuesta importarNominaQuincenal(String filename, byte[] source, LinkedHashMap<String, String> layout) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<EmpleadoNomina> resultado = new ArrayList<EmpleadoNomina>();
		List<NominaQuincenalItem> noProcesados = new ArrayList<NominaQuincenalItem>();
		List<NominaQuincenalItem> empAdvertencia = new ArrayList<NominaQuincenalItem>();
		List<Empleado> listEmp = null;
		NominaQuincenalItem pojoItem = null;
		EmpleadoNomina pojoNomina = null;
		Empleado pojoEmpleado = null;
		Iterator<Row> rowIterator = null;
		Workbook workbook = null;
		InputStream file = null;
		Sheet sheet = null;
		Row row = null;
		Cell celda = null;
		Date fechaInicio = null;
		Date fechaLimite = null;
		DateIterator iterator = null;
		SimpleDateFormat formatter = null;
		String[] splitted = null;
		double montoDiario = 0;
		int indexRow = 0;
		
		try {
			if (source == null) {
				log.error("### Error 204: No Content. Archivo sin contenido. Source null");
				respuesta.getErrores().setCodigoError(204L);
				respuesta.getErrores().setDescError("No se puede procesar el archivo. No tiene contenido");
				return respuesta;
			}
			
			file = new ByteArrayInputStream(source);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();
			
			for (indexRow = 0; rowIterator.hasNext(); indexRow++) {
				row = rowIterator.next();
				
				// Comprobamos la columna de control para el LAYOUT
				if (layout.containsKey("CONTROL")) {
					if (! comprobarColumnaControl(row, layout.get("CONTROL"))) {
						if (indexRow <= 3 && fechaLimite == null) {
							// Valido si puedo sacar el rango de fecha
							if (layout.containsKey("RANGO")) {
								formatter = new SimpleDateFormat("dd/MMM/yyyy");
								splitted = layout.get("RANGO").split("\\|");
								splitted = splitted[0].split(",");

								celda = row.getCell(CellReference.convertColStringToIndex(splitted[0]));
								if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(celda)) {
									fechaInicio = celda.getDateCellValue(); 
								} else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
									fechaInicio = formatter.parse(celda.getStringCellValue());
								}

								celda = row.getCell(CellReference.convertColStringToIndex(splitted[1]));
								if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(celda)) {
									fechaLimite = celda.getDateCellValue(); 
								} else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
									fechaLimite = formatter.parse(celda.getStringCellValue());
								}
							} else {
								/*celda = row.getCell(CellReference.convertColStringToIndex("A"));
								if (celda != null && Cell.CELL_TYPE_STRING == celda.getCellType() && celda.getStringCellValue().toUpperCase().contains("NOMINA")) {
									fechaInicio = Calendar.getInstance().getTime();
									fechaLimite = Calendar.getInstance().getTime();
								}*/
							}
						}
						
						continue;
					}
				}

				pojoItem = new NominaQuincenalItem();
				for (Entry<String, String> item : layout.entrySet()) {
					celda = row.getCell(CellReference.convertColStringToIndex(item.getKey()));
					if (celda == null)
						continue;
					
					try {
						if ("A".equals(item.getKey())) {
							pojoItem.setSucursal(celda.getStringCellValue());
							continue;
						} else if ("B".equals(item.getKey())) {
							pojoItem.setIdObra((long) celda.getNumericCellValue());
							continue;
						} else if ("C".equals(item.getKey())) {
							pojoItem.setNss(celda.getStringCellValue());
							continue;
						} else if ("D".equals(item.getKey())) {
							pojoItem.setEmpleado(celda.getStringCellValue());
							continue;
						} else if ("E".equals(item.getKey())) {
							pojoItem.setSueldoQuincenal(celda.getNumericCellValue());
							continue;
						} else if ("F".equals(item.getKey())) {
							pojoItem.setPagoDeposito(celda.getNumericCellValue());
							continue;
						} else if ("G".equals(item.getKey())) {
							pojoItem.setCuentaDeposito(celda.getStringCellValue());
							continue;
						} else if ("H".equals(item.getKey())) {
							pojoItem.setObservaciones(celda.getStringCellValue());
							continue;
						}
					} catch (Exception ex) {
						log.error("### Error al leer el archivo EXCEL (" + filename + "). Se proceso hasta la fila " + indexRow, ex);
						respuesta.getErrores().setCodigoError(500L);
						respuesta.getErrores().setDescError("La celda " + item.getKey() + (indexRow + 1)  + " no tiene el formato correcto.");
						return respuesta;
					}
				}
				
				// Comprobamos si tenemos todos los datos para el item o filas
				if (! pojoItem.completo())  {
					pojoItem.setMensaje("Informacion incompleta");
					noProcesados.add(pojoItem);
					continue;
				}
				
				// Comprobamos si podemos recuperar empleado con su NSS
				this.ifzEmpleados.setInfoSesion(this.infoSesion);
				listEmp = this.ifzEmpleados.findByProperty("numeroSeguridadSocial", pojoItem.getNss(), 0);
				if (listEmp == null || listEmp.isEmpty()) {
					pojoItem.setMensaje("No se encontro Empleado con este Numero de Seguro Social (NSS)");
					noProcesados.add(pojoItem);
					continue;
				} else if (listEmp.size() > 1) {
					pojoItem.setMensaje("Multiples empleados con este Numero de Seguro Social (NSS)");
					noProcesados.add(pojoItem);
					continue;
				}
				
				// Recupero el empleado
				pojoEmpleado = listEmp.get(0);
				if (pojoEmpleado == null) {
					pojoItem.setMensaje("Empleado no encontrado");
					noProcesados.add(pojoItem);
					continue;
				}
				
				// Compara nombre del empleado en archivo y en base de datos
				if (! pojoItem.getEmpleado().equals(pojoEmpleado.getNombre())) {
					pojoItem.setMensaje("Nombre de empleado no coincide con el registrado en el sistema");
					empAdvertencia.add(pojoItem);
				}
				
				montoDiario = pojoItem.getSueldoQuincenal() / 15;
				iterator = new DateIterator(fechaInicio, fechaLimite);
				while (iterator.hasNext()) {
					iterator.next();
					
					pojoNomina = new EmpleadoNomina();
					pojoNomina.setIdEmpleado(pojoEmpleado);
					pojoNomina.setFecha(iterator.getCurrent());
					pojoNomina.setMontoNormal(new BigDecimal(montoDiario));
					pojoNomina.setMontoExtra(BigDecimal.ZERO);
					pojoNomina.setMontoRetenciones(BigDecimal.ZERO);
					pojoNomina.setMontoNeto(new BigDecimal(montoDiario));
					pojoNomina.setCreadoPor(infoSesion.getAcceso().getUsuario().getId());
					pojoNomina.setFechaCreacion(Calendar.getInstance().getTime());
					pojoNomina.setModificadoPor(infoSesion.getAcceso().getUsuario().getId());
					pojoNomina.setFechaModificacion(Calendar.getInstance().getTime());
					
					resultado.add(pojoNomina);
				}
			} // fin for rows
			

			respuesta.getBody().addValor("nominaQuincenal", resultado);
			respuesta.getBody().addValor("sinProcesar", noProcesados);
			respuesta.getBody().addValor("advertencias", empAdvertencia);
		} catch (Exception e) {
			log.error("### Error al leer el archivo EXCEL (" + filename + "). Se proceso hasta la fila " + indexRow, e);
			respuesta.getErrores().setCodigoError(500L);
			respuesta.getErrores().setDescError("No se pudo procesar el archivo.\n" + e.getMessage());
		}

		return respuesta;
	}
	
	private boolean comprobarColumnaControl(Row row, String control) {
		String[] splitted = null;
		Cell celda = null;
		
		splitted = control.split("\\|");
		celda = row.getCell(CellReference.convertColStringToIndex(splitted[0]));
		if (celda == null) {
			return false;
		}
		
		if ("java.lang.String".equals(splitted[1])) {
			if (Cell.CELL_TYPE_STRING != celda.getCellType())
				return false;
		} else if ("java.lang.Long".equals(splitted[1]))  {
			if (Cell.CELL_TYPE_NUMERIC != celda.getCellType())
				return false;
		} else if ("java.lang.Double".equals(splitted[1]))  {
			if (Cell.CELL_TYPE_NUMERIC != celda.getCellType())
				return false;
		} else if ("java.lang.Integer".equals(splitted[1]))  {
			if (Cell.CELL_TYPE_NUMERIC != celda.getCellType())
				return false;
		} else if ("java.math.BigDecimal".equals(splitted[1]))  {
			if (Cell.CELL_TYPE_NUMERIC != celda.getCellType())
				return false;
		}
		return true;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaFac