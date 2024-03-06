package net.giro.adp.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraSatics;
import net.giro.adp.dao.ObraSaticsDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraSaticsFac implements ObraSaticsRem {
	private static Logger log = Logger.getLogger(ObraSaticsFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ObraSaticsDAO ifzObraSatics;
	private static String orderBy;
	
	public ObraSaticsFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzObraSatics = (ObraSaticsDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraSaticsImp!net.giro.adp.dao.ObraSaticsDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraSaticsFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void orderBy(String orderBy) {
		ObraSaticsFac.orderBy = orderBy;
	}

	@Override
	public Long save(ObraSatics entity) throws Exception {
		try {
			return this.ifzObraSatics.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.save(ObraSatics)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSatics> saveOrUpdateList(List<ObraSatics> entities) throws Exception {
		try {
			return this.ifzObraSatics.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.saveOrUpdateList(List<ObraSatics> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(ObraSatics entity) throws Exception {
		try {
			this.ifzObraSatics.setEmpresa(getIdEmpresa());
			this.ifzObraSatics.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.update(ObraSatics)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzObraSatics.setEmpresa(getIdEmpresa());
			this.ifzObraSatics.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public ObraSatics findById(Long id) {
		try {
			this.ifzObraSatics.setEmpresa(getIdEmpresa());
			return this.ifzObraSatics.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<ObraSatics> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraSatics.setEmpresa(getIdEmpresa());
			this.ifzObraSatics.orderBy(orderBy);
			return this.ifzObraSatics.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraSatics> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraSatics.setEmpresa(getIdEmpresa());
			this.ifzObraSatics.orderBy(orderBy);
			return this.ifzObraSatics.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraSatics> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzObraSatics.setEmpresa(getIdEmpresa());
			this.ifzObraSatics.orderBy(orderBy);
			return this.ifzObraSatics.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraSatics> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraSatics.setEmpresa(getIdEmpresa());
			this.ifzObraSatics.orderBy(orderBy);
			return this.ifzObraSatics.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraSatics> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraSatics.setEmpresa(getIdEmpresa());
			this.ifzObraSatics.orderBy(orderBy);
			return this.ifzObraSatics.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraSaticsFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
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
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|23/05/2016		|Javier Tirado	|Creando el facade ObraSaticsFac