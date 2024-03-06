package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.Llaves;
import net.giro.contabilidad.dao.LlavesDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class LlavesFac implements LlavesRem {
	private static Logger log = Logger.getLogger(LlavesFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private LlavesDAO ifzLlaves;
	private static String orderBy;
	
	public LlavesFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzLlaves = (LlavesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//LlavesImp!net.giro.contabilidad.dao.LlavesDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.LlavesFac", e);
			ctx = null;
		}
	}

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	@Override
	public void orderBy(String orderBy) {
		LlavesFac.orderBy = orderBy;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}


	@Override
	public Long save(Llaves entity) throws Exception {
		try {
			return this.ifzLlaves.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.save(Llaves)", e);
			throw e;
		}
	}
	
	@Override
	public List<Llaves> saveOrUpdateList(List<Llaves> entities) throws Exception {
		try {
			return this.ifzLlaves.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.save(Llaves)", e);
			throw e;
		}
	}
	
	@Override
	public void update(Llaves entity) throws Exception {
		try {
			this.ifzLlaves.setEmpresa(getIdEmpresa());
			this.ifzLlaves.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.update(Llaves)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzLlaves.setEmpresa(getIdEmpresa());
			this.ifzLlaves.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public Llaves findById(Long id) {
		try {
			this.ifzLlaves.setEmpresa(getIdEmpresa());
			return this.ifzLlaves.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<Llaves> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzLlaves.setEmpresa(getIdEmpresa());
			this.ifzLlaves.orderBy(orderBy);
			return this.ifzLlaves.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Llaves> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzLlaves.setEmpresa(getIdEmpresa());
			this.ifzLlaves.orderBy(orderBy);
			return this.ifzLlaves.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Llaves> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzLlaves.setEmpresa(getIdEmpresa());
			this.ifzLlaves.orderBy(orderBy);
			return this.ifzLlaves.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Llaves> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzLlaves.setEmpresa(getIdEmpresa());
			this.ifzLlaves.orderBy(orderBy);
			return this.ifzLlaves.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Llaves> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzLlaves.setEmpresa(getIdEmpresa());
			this.ifzLlaves.orderBy(orderBy);
			return this.ifzLlaves.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public boolean comprobarPosicion(Long idLlave, int posicion) throws Exception {
		try {
			this.ifzLlaves.setEmpresa(getIdEmpresa());
			return this.ifzLlaves.comprobarPosicion(idLlave, posicion);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.LlavesFac.comprobarPosicion(idLlave, posicion)", e);
			throw e;
		}
	}
}