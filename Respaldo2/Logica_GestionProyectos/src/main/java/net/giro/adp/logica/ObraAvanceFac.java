package net.giro.adp.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraAvance;
import net.giro.adp.dao.ObraAvanceDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraAvanceFac implements ObraAvanceRem {
	private static Logger log = Logger.getLogger(ObraAvanceFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ObraAvanceDAO ifzObraAvance;
	private static String orderBy;
	
	public ObraAvanceFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzObraAvance = (ObraAvanceDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraAvanceImp!net.giro.adp.dao.ObraAvanceDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraAvanceFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void orderBy(String orderBy) { ObraAvanceFac.orderBy = orderBy; }

	@Override
	public Long save(ObraAvance entity) throws Exception {
		try {
			return this.ifzObraAvance.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.save(ObraAvance)", e);
			throw e;
		}
	}

	@Override
	public List<ObraAvance> saveOrUpdateList(List<ObraAvance> entities) throws Exception {
		try {
			return this.ifzObraAvance.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.saveOrUpdateList(List<ObraSatics> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(ObraAvance entity) throws Exception {
		try {
			this.ifzObraAvance.setEmpresa(getIdEmpresa());
			this.ifzObraAvance.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.update(ObraAvance)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzObraAvance.setEmpresa(getIdEmpresa());
			this.ifzObraAvance.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public ObraAvance findById(Long id) {
		try {
			this.ifzObraAvance.setEmpresa(getIdEmpresa());
			return this.ifzObraAvance.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<ObraAvance> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraAvance.setEmpresa(getIdEmpresa());
			this.ifzObraAvance.orderBy(orderBy);
			return this.ifzObraAvance.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraAvance> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraAvance.setEmpresa(getIdEmpresa());
			this.ifzObraAvance.orderBy(orderBy);
			return this.ifzObraAvance.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraAvance> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzObraAvance.setEmpresa(getIdEmpresa());
			this.ifzObraAvance.orderBy(orderBy);
			return this.ifzObraAvance.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraAvance> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzObraAvance.setEmpresa(getIdEmpresa());
			this.ifzObraAvance.orderBy(orderBy);
			return this.ifzObraAvance.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraAvance> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraAvance.setEmpresa(getIdEmpresa());
			this.ifzObraAvance.orderBy(orderBy);
			return this.ifzObraAvance.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraAvanceFac.findLikeProperties(params, limite)", e);
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
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 14/06/2016 | Javier Tirado	| Creacion de ObraAvanceFac