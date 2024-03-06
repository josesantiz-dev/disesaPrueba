package net.giro.rh.admon.logica;

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
import net.giro.rh.admon.beans.EmpleadoDescuento;
import net.giro.rh.admon.beans.EmpleadoDescuentoExt;
import net.giro.rh.admon.dao.EmpleadoDescuentoDAO;

@Stateless
public class EmpleadoDescuentoFac implements EmpleadoDescuentoRem {
	private static Logger log = Logger.getLogger(EmpleadoDescuentoFac.class);
	private InfoSesion infoSesion;
	InitialContext ctx;

	private EmpleadoDescuentoDAO ifzEmpRetenciones;
	private ConvertExt convertidor;
	private static String orderBy;
	
	public EmpleadoDescuentoFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzEmpRetenciones = (EmpleadoDescuentoDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoDescuentoImp!net.giro.rh.admon.dao.EmpleadoDescuentoDAO");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("EmpleadoDescuentoFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.EmpleadoDescuentoFac", e);
			ctx = null;
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
	public void orderBy(String orderBy) {
		EmpleadoDescuentoFac.orderBy = orderBy;
	}

	@Override
	public Long save(EmpleadoDescuento entity) throws ExcepConstraint {
		try {
			return this.ifzEmpRetenciones.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.save(EmpleadoDescuento)", e);
			throw e;
		}
	}

	@Override
	public Long save(EmpleadoDescuentoExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.EmpleadoDescuentoExtToEmpleadoDescuento(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.save(EmpleadoDescuentoExt)", e);
			throw e;
		}
	}

	@Override
	public void update(EmpleadoDescuento entity) throws ExcepConstraint {
		try {
			this.ifzEmpRetenciones.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.update(EmpleadoDescuento)", e);
			throw e;
		}
	}

	@Override
	public void update(EmpleadoDescuentoExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.EmpleadoDescuentoExtToEmpleadoDescuento(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.update(EmpleadoDescuentoExt)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzEmpRetenciones.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoDescuento findById(Long id) {
		try {
			return this.ifzEmpRetenciones.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoDescuentoExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.EmpleadoDescuentoToEmpleadoDescuentoExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findExtById(id)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoDescuento> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzEmpRetenciones.orderBy(orderBy);
			return this.ifzEmpRetenciones.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoDescuentoExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<EmpleadoDescuentoExt> listaExt = new ArrayList<EmpleadoDescuentoExt>();
		
		try {
			List<EmpleadoDescuento> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(EmpleadoDescuento var : lista) {
					listaExt.add(this.convertidor.EmpleadoDescuentoToEmpleadoDescuentoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<EmpleadoDescuento> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzEmpRetenciones.orderBy(orderBy);
			return this.ifzEmpRetenciones.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoDescuentoExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<EmpleadoDescuentoExt> listaExt = new ArrayList<EmpleadoDescuentoExt>();
		
		try {
			List<EmpleadoDescuento> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(EmpleadoDescuento var : lista) {
					listaExt.add(this.convertidor.EmpleadoDescuentoToEmpleadoDescuentoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<EmpleadoDescuento> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzEmpRetenciones.orderBy(orderBy);
			return this.ifzEmpRetenciones.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoDescuentoExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<EmpleadoDescuentoExt> listaExt = new ArrayList<EmpleadoDescuentoExt>();
		
		try {
			List<EmpleadoDescuento> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(EmpleadoDescuento var : lista) {
					listaExt.add(this.convertidor.EmpleadoDescuentoToEmpleadoDescuentoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<EmpleadoDescuento> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzEmpRetenciones.orderBy(orderBy);
			return this.ifzEmpRetenciones.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoDescuentoExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception {
		List<EmpleadoDescuentoExt> listaExt = new ArrayList<EmpleadoDescuentoExt>();
		
		try {
			List<EmpleadoDescuento> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(EmpleadoDescuento var : lista) {
					listaExt.add(this.convertidor.EmpleadoDescuentoToEmpleadoDescuentoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public List<EmpleadoDescuento> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzEmpRetenciones.orderBy(orderBy);
			return this.ifzEmpRetenciones.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpleadoDescuentoExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<EmpleadoDescuentoExt> listaExt = new ArrayList<EmpleadoDescuentoExt>();
		
		try {
			List<EmpleadoDescuento> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(EmpleadoDescuento var : lista) {
					listaExt.add(this.convertidor.EmpleadoDescuentoToEmpleadoDescuentoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	@Override
	public EmpleadoDescuento cancelar(EmpleadoDescuento entity) throws Exception {
		try {
			entity.setEstatus(0);
			entity.setModificadoPor((int) infoSesion.getResponsabilidad().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.update(entity);
			
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.cancelar(entity, grupoEstatus)", e);
			throw e;
		}
	}
	
	@Override
	public EmpleadoDescuentoExt cancelar(EmpleadoDescuentoExt entityExt) throws Exception {
		try {
			return this.convertidor.EmpleadoDescuentoToEmpleadoDescuentoExt(this.cancelar(this.convertidor.EmpleadoDescuentoExtToEmpleadoDescuento(entityExt)));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.cancelar(entityExt, grupoEstatus)", e);
			throw e;
		}
	}

	
	@Override
	public List<EmpleadoDescuento> comprobarDescuentoPorFechas(Long idEmpleado, Date fecha) throws Exception {
		try {
			return this.ifzEmpRetenciones.comprobarDescuentoPorFechas(idEmpleado, fecha);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoDescuentoFac.cancelar(entityExt, grupoEstatus)", e);
			throw e;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 25/05/2016 | Javier Tirado	| Creacion de EmpleadoDescuentoFac