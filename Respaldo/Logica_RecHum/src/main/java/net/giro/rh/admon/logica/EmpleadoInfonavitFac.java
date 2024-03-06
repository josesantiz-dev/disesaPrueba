package net.giro.rh.admon.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoInfonavit;
import net.giro.rh.admon.dao.EmpleadoInfonavitDAO;

@Stateless
public class EmpleadoInfonavitFac implements EmpleadoInfonavitRem {
	private static Logger log = Logger.getLogger(EmpleadoInfonavitFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private EmpleadoInfonavitDAO ifzEmpInfonavit;
	//private ConvertExt convertidor;
	private static String orderBy;
	
	public EmpleadoInfonavitFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzEmpInfonavit = (EmpleadoInfonavitDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoInfonavitImp!net.giro.rh.admon.dao.EmpleadoInfonavitDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("EmpleadoInfonavitFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.EmpleadoInfonavitFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { /*this.convertidor.setMostrarSystemOut(value);*/ }

	@Override
	public void orderBy(String orderBy) { EmpleadoInfonavitFac.orderBy = orderBy; }

	@Override
	public Long save(EmpleadoInfonavit entity) throws ExcepConstraint {
		try {
			return this.ifzEmpInfonavit.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.save(EmpleadoInfonavit)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(EmpleadoInfonavitExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.EmpleadoInfonavitExtToEmpleadoInfonavit(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.save(EmpleadoInfonavitExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(EmpleadoInfonavit entity) throws ExcepConstraint {
		try {
			this.ifzEmpInfonavit.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.update(EmpleadoInfonavit)", e);
			throw e;
		}
	}

	/*@Override
	public void update(EmpleadoInfonavitExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.EmpleadoInfonavitExtToEmpleadoInfonavit(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.update(EmpleadoInfonavitExt)", e);
			throw e;
		}
	}*/

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzEmpInfonavit.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public EmpleadoInfonavit findById(Long id) {
		try {
			return this.ifzEmpInfonavit.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findById(id)", e);
			throw e;
		}
	}

	/*@Override
	public EmpleadoInfonavitExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.EmpleadoInfonavitToEmpleadoInfonavitExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findExtById(id)", e);
			throw e;
		}
	}*/

	@Override
	public List<EmpleadoInfonavit> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			return this.ifzEmpInfonavit.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<EmpleadoInfonavitExt> findExtByProperty(String propertyName, Object value, int max) throws Exception {
		List<EmpleadoInfonavitExt> listaExt = new ArrayList<EmpleadoInfonavitExt>();
		
		try {
			List<EmpleadoInfonavit> lista = this.findByProperty(propertyName, value, max);
			if (lista != null) {
				for(EmpleadoInfonavit var : lista) {
					listaExt.add(this.convertidor.EmpleadoInfonavitToEmpleadoInfonavitExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findExtByProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<EmpleadoInfonavit> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			return this.ifzEmpInfonavit.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<EmpleadoInfonavitExt> findExtLikeProperty(String propertyName, Object value, int max) throws Exception {
		List<EmpleadoInfonavitExt> listaExt = new ArrayList<EmpleadoInfonavitExt>();
		
		try {
			List<EmpleadoInfonavit> lista = this.findLikeProperty(propertyName, value, max);
			if (lista != null) {
				for(EmpleadoInfonavit var : lista) {
					listaExt.add(this.convertidor.EmpleadoInfonavitToEmpleadoInfonavitExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findExtLikeProperty(propertyName, value, max)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<EmpleadoInfonavit> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			return this.ifzEmpInfonavit.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<EmpleadoInfonavitExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<EmpleadoInfonavitExt> listaExt = new ArrayList<EmpleadoInfonavitExt>();
		
		try {
			List<EmpleadoInfonavit> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(EmpleadoInfonavit var : lista) {
					listaExt.add(this.convertidor.EmpleadoInfonavitToEmpleadoInfonavitExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<EmpleadoInfonavit> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			return this.ifzEmpInfonavit.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<EmpleadoInfonavitExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<EmpleadoInfonavitExt> listaExt = new ArrayList<EmpleadoInfonavitExt>();
		
		try {
			List<EmpleadoInfonavit> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(EmpleadoInfonavit var : lista) {
					listaExt.add(this.convertidor.EmpleadoInfonavitToEmpleadoInfonavitExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	@Override
	public List<EmpleadoInfonavit> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzEmpInfonavit.orderBy(orderBy);
			return this.ifzEmpInfonavit.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	public List<EmpleadoInfonavitExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<EmpleadoInfonavitExt> listaExt = new ArrayList<EmpleadoInfonavitExt>();
		
		try {
			List<EmpleadoInfonavit> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(EmpleadoInfonavit var : lista) {
					listaExt.add(this.convertidor.EmpleadoInfonavitToEmpleadoInfonavitExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	/*@Override
	public EmpleadoInfonavit cancelar(EmpleadoInfonavit entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.ifzEmpInfonavit.update(entity);
			
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.cancelar(entity)", e);
			throw e;
		}
	}*/

	/*@Override
	public void cancelar(EmpleadoInfonavitExt entityExt) throws ExcepConstraint {
		try {
			entityExt.setEstatus(1);
			entityExt.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
			entityExt.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.update(entityExt);
			
			return entityExt;
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.cancelar(entityExt)", e);
			throw e;
		}
	}*/

	@Override
	public boolean comprobarRegistro(EmpleadoInfonavit entity) throws Exception {
		try {
			// Generamos parametros
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("idEmpleado.id", entity.getIdEmpleado().getId());
			params.put("fechaDesde", entity.getFechaDesde());
			params.put("fechaHasta", entity.getFechaHasta());
			
			this.ifzEmpInfonavit.orderBy(orderBy);
			List<EmpleadoInfonavit> lista = this.findByProperties(params, 120);
			
			return ! (lista == null || lista.isEmpty());
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.comprobarRegistro(idEmpleado, bimestre, annio)", e);
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
//	  2.2	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoInfonavitFac