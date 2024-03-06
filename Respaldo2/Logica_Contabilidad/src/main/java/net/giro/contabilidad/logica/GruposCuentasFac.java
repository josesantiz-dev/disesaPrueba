package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.GruposCuentas;
import net.giro.contabilidad.dao.GruposCuentasDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class GruposCuentasFac implements GruposCuentasRem {
	private static Logger log = Logger.getLogger(GruposCuentasFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private GruposCuentasDAO ifzGruposCuentass;
	private static String orderBy;
	
	
	public GruposCuentasFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzGruposCuentass = (GruposCuentasDAO) this.ctx.lookup("ejb:/Model_Contabilidad//GruposCuentasImp!net.giro.contabilidad.dao.GruposCuentasDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.GruposCuentasFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void orderBy(String orderBy) {
		GruposCuentasFac.orderBy = orderBy;
	}

	@Override
	public Long save(GruposCuentas entity) throws Exception {
		try {
			return this.ifzGruposCuentass.save(entity, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.save(GruposCuentas)", e);
			throw e;
		}
	}

	@Override
	public List<GruposCuentas> saveOrUpdateList(List<GruposCuentas> entities) throws Exception {
		try {
			return this.ifzGruposCuentass.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.saveOrUpdateList(List<GruposCuentas> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(GruposCuentas entity) throws Exception {
		try {
			this.ifzGruposCuentass.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.update(GruposCuentas)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzGruposCuentass.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public GruposCuentas findById(Long id) {
		try {
			return this.ifzGruposCuentass.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<GruposCuentas> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzGruposCuentass.orderBy(orderBy);			
			return this.ifzGruposCuentass.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<GruposCuentas> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzGruposCuentass.orderBy(orderBy);
			return this.ifzGruposCuentass.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<GruposCuentas> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzGruposCuentass.orderBy(orderBy);
			return this.ifzGruposCuentass.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<GruposCuentas> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzGruposCuentass.orderBy(orderBy);
			return this.ifzGruposCuentass.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<GruposCuentas> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzGruposCuentass.orderBy(orderBy);
			return this.ifzGruposCuentass.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
}