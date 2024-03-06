package net.giro.rh.admon.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

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

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.ChecadorDetalleExt;
import net.giro.rh.admon.dao.ChecadorDetalleDAO;

@Stateless
public class ChecadorDetalleFac implements ChecadorDetalleRem {
	private static Logger log = Logger.getLogger(ChecadorDetalleFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private ChecadorDetalleDAO ifzChecadorDetalles;
	private ConvertExt convertidor;
	private static String orderBy;
	List<ChecadorDetalle> detallesLog;
	
	public ChecadorDetalleFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
            
			this.ifzChecadorDetalles = (ChecadorDetalleDAO) this.ctx.lookup("ejb:/Model_RecHum//ChecadorDetalleImp!net.giro.rh.admon.dao.ChecadorDetalleDAO");
            
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("ChecadorDetalleFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.ChecadorDetalleFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSecion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void orderBy(String orderBy) {
		ChecadorDetalleFac.orderBy = orderBy;
	}

	
	@Override
	public Long save(ChecadorDetalle ChecadorDetalle) throws ExcepConstraint {
		try {
			return this.ifzChecadorDetalles.save(ChecadorDetalle);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.save(ChecadorDetalle)", e);
			throw e;
		}
	}

	@Override
	public Long save(ChecadorDetalleExt ChecadorDetalleExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.ChecadorDetalleExtToChecadorDetalle(ChecadorDetalleExt));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.save(ChecadorDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public void update(ChecadorDetalle ChecadorDetalle) throws ExcepConstraint {
		try {
			this.ifzChecadorDetalles.update(ChecadorDetalle);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.update(ChecadorDetalle)", e);
			throw e;
		}
	}

	@Override
	public void update(ChecadorDetalleExt ChecadorDetalleExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.ChecadorDetalleExtToChecadorDetalle(ChecadorDetalleExt));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.update(ChecadorDetalleExt)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long ChecadorDetalle) throws ExcepConstraint {
		try {
			this.ifzChecadorDetalles.delete(ChecadorDetalle);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.delete(Long)", e);
			throw e;
		}
	}

	
	@Override
	public ChecadorDetalle findById(Long id) {
		try {
			return this.ifzChecadorDetalles.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public ChecadorDetalleExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.ChecadorDetalleToChecadorDetalleExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<ChecadorDetalle> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzChecadorDetalles.orderBy(orderBy);
			return this.ifzChecadorDetalles.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ChecadorDetalleExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<ChecadorDetalleExt> listaExt = new ArrayList<ChecadorDetalleExt>();
		
		try {
			List<ChecadorDetalle> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(ChecadorDetalle var : lista) {
					listaExt.add(this.convertidor.ChecadorDetalleToChecadorDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ChecadorDetalle> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzChecadorDetalles.orderBy(orderBy);
			return this.ifzChecadorDetalles.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ChecadorDetalleExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<ChecadorDetalleExt> listaExt = new ArrayList<ChecadorDetalleExt>();
		
		try {
			List<ChecadorDetalle> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(ChecadorDetalle var : lista) {
					listaExt.add(this.convertidor.ChecadorDetalleToChecadorDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ChecadorDetalle> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzChecadorDetalles.orderBy(orderBy);
			return this.ifzChecadorDetalles.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ChecadorDetalleExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<ChecadorDetalleExt> listaExt = new ArrayList<ChecadorDetalleExt>();
		
		try {
			List<ChecadorDetalle> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(ChecadorDetalle var : lista) {
					listaExt.add(this.convertidor.ChecadorDetalleToChecadorDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ChecadorDetalle> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzChecadorDetalles.orderBy(orderBy);
			return this.ifzChecadorDetalles.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ChecadorDetalleExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<ChecadorDetalleExt> listaExt = new ArrayList<ChecadorDetalleExt>();
		
		try {
			List<ChecadorDetalle> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(ChecadorDetalle var : lista) {
					listaExt.add(this.convertidor.ChecadorDetalleToChecadorDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<ChecadorDetalle> findLikeProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzChecadorDetalles.orderBy(orderBy);
			return this.ifzChecadorDetalles.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ChecadorDetalleExt> findExtLikeProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<ChecadorDetalleExt> listaExt = new ArrayList<ChecadorDetalleExt>();
		
		try {
			List<ChecadorDetalle> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(ChecadorDetalle var : lista) {
					listaExt.add(this.convertidor.ChecadorDetalleToChecadorDetalleExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public Respuesta analizaDetalles(String fileName, String fileExtension, byte[] fileSrc) throws Exception {
		Respuesta respuesta = new Respuesta();
		
		try {
			List<ChecadorDetalle> detalles = procesarArchivo(fileName, fileSrc);
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
	
	private List<ChecadorDetalle> procesarArchivo(String fileName, byte[] fileSrc) throws Exception {
		InputStream file;
		Workbook workbook;
		Sheet sheet;
		Iterator<Row> rowIterator;
		Iterator<Cell> cellIterator;
		Row row;
		Cell celda;
		
		try {
			boolean toLog = false;
			this.detallesLog = new ArrayList<ChecadorDetalle>();
			List<ChecadorDetalle> detalles = new ArrayList<ChecadorDetalle>();
			ChecadorDetalle item;
			SimpleDateFormat formatter;
			
			file = new ByteArrayInputStream(fileSrc);
			workbook = WorkbookFactory.create(file);
			sheet = workbook.getSheetAt(0);
			rowIterator = sheet.iterator();

			// Recorremos todas las filas para mostrar el contenido de cada celda
			for (int indexRows = 0; rowIterator.hasNext(); indexRows++) {
				row = rowIterator.next();
				cellIterator = row.cellIterator();
				
				if (indexRows == 0) continue;
				
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
						GregorianCalendar t1 = new GregorianCalendar();
						t1.setTime(item.getHoraSalidaMarcada());				
						GregorianCalendar t2 = new GregorianCalendar();
						t2.setTime(item.getHoraEntradaMarcada());				
						long diff = t1.getTimeInMillis() - t2.getTimeInMillis();
						item.setHorasTrabajadas((int) (diff / 3600000));
						
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
	
	@Override
	public boolean existeAsistencia(Long idChecador, Long idEmpleado, Date fecha) throws Exception {
		try {
			ChecadorDetalle pojo = this.existeAsistenciaPojo(idChecador, idEmpleado, fecha);
			
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
		try {
			ChecadorDetalle pojo = this.existeAsistenciaPojo(idChecador, idEmpleado, fecha);
			
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
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String fechaFormateada = df.format(fecha);
			
			params.put("idChecador", idChecador);
			params.put("idEmpleado", idEmpleado);
			params.put("fecha", fechaFormateada);
			
			List<ChecadorDetalle> lista = this.ifzChecadorDetalles.findByProperties(params, 120);
			
			if (lista == null || lista.isEmpty())
				return null;
			
			return lista.get(0);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.existeAsistenciaPojo(idEmpleado, fecha)", e);
			throw e;
		}
	}
	
	@Override
	public List<ChecadorDetalle> findByDates(Date fechaDesde, Date fechaHasta) throws Exception {
		try {
			return this.ifzChecadorDetalles.findByDates(fechaDesde, fechaHasta);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.ChecadorDetalleFac.findByDates(fechaDesde, fechaHasta)", e);
			throw e;
		}
	}
	
	@Override
	public ChecadorDetalle convertChecadorDetalleExtToChecadorDetalle(ChecadorDetalleExt entity) throws Exception {
		return this.convertChecadorDetalleExtToChecadorDetalle(entity);
	}
	
	@Override
	public ChecadorDetalleExt convertChecadorDetalleToChecadorDetalleExt(ChecadorDetalle entity) throws Exception {
		return this.convertChecadorDetalleToChecadorDetalleExt(entity);
	}
}
