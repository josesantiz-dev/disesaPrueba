package net.giro.rh.admon.logica;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.publico.respuesta.Respuesta;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoDescuento;
import net.giro.rh.admon.beans.EmpleadoNomina;
import net.giro.rh.admon.dao.EmpleadoNominaDAO;

@Stateless
public class EmpleadoNominaFac implements EmpleadoNominaRem {
	private static Logger log = Logger.getLogger(EmpleadoNominaFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private EmpleadoNominaDAO ifzEmpleadoNomina;
	private static String orderBy;
	
	
	public EmpleadoNominaFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzEmpleadoNomina = (EmpleadoNominaDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoNominaImp!net.giro.rh.admon.dao.EmpleadoNominaDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("EmpleadoNominaFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.EmpleadoNominaFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { }

	@Override
	public void orderBy(String orderBy) { EmpleadoNominaFac.orderBy = orderBy; }

	@Override
	public Long save(EmpleadoNomina entity) throws ExcepConstraint {
		try {
			return this.ifzEmpleadoNomina.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.save(EmpleadoNomina)", e);
			throw e;
		}
	}

	@Override
	public void update(EmpleadoNomina entity) throws ExcepConstraint {
		try {
			this.ifzEmpleadoNomina.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoNominaFac.update(EmpleadoNomina)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
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
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaFac