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
	private InitialContext ctx;
	private EmpleadoInfonavitDAO ifzEmpInfonavit;
	private static String orderBy;
	
	public EmpleadoInfonavitFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzEmpInfonavit = (EmpleadoInfonavitDAO) this.ctx.lookup("ejb:/Model_RecHum//EmpleadoInfonavitImp!net.giro.rh.admon.dao.EmpleadoInfonavitDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_RecHum.EmpleadoInfonavitFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void orderBy(String orderBy) { EmpleadoInfonavitFac.orderBy = orderBy; }

	@Override
	public Long save(EmpleadoInfonavit entity) throws ExcepConstraint {
		try {
			return this.ifzEmpInfonavit.save(entity, null);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.save(EmpleadoInfonavit)", e);
			throw e;
		}
	}

	@Override
	public List<EmpleadoInfonavit> saveOrUpdateList(List<EmpleadoInfonavit> entities) throws Exception {
		try {
			return this.ifzEmpInfonavit.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.save(EmpleadoInfonavit)", e);
			throw e;
		}
	}

	@Override
	public void update(EmpleadoInfonavit entity) throws ExcepConstraint {
		try {
			this.ifzEmpInfonavit.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_RecHum.EmpleadoInfonavitFac.update(EmpleadoInfonavit)", e);
			throw e;
		}
	}

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