package net.giro.adp.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraContratos;
import net.giro.adp.dao.ObraContratosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraContratosFac implements ObraContratosRem {
	private static Logger log = Logger.getLogger(ObraContratosFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ObraContratosDAO ifzObraContratos;
	private static String orderBy;
	
	public ObraContratosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzObraContratos = (ObraContratosDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraContratosImp!net.giro.adp.dao.ObraContratosDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraContratosFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void orderBy(String orderBy) { ObraContratosFac.orderBy = orderBy; }

	@Override
	public Long save(ObraContratos entity) throws Exception {
		try {
			return this.ifzObraContratos.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.save(ObraContratos)", e);
			throw e;
		}
	}

	@Override
	public List<ObraContratos> saveOrUpdateList(List<ObraContratos> entities) throws Exception {
		try {
			return this.ifzObraContratos.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.saveOrUpdateList(List<ObraSatics> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(ObraContratos entity) throws Exception {
		try {
			this.ifzObraContratos.setEmpresa(getIdEmpresa());
			this.ifzObraContratos.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.update(ObraContratos)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzObraContratos.setEmpresa(getIdEmpresa());
			this.ifzObraContratos.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public ObraContratos findById(Long id) {
		try {
			this.ifzObraContratos.setEmpresa(getIdEmpresa());
			return this.ifzObraContratos.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<ObraContratos> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraContratos.setEmpresa(getIdEmpresa());
			this.ifzObraContratos.orderBy(orderBy);
			return this.ifzObraContratos.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraContratos> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraContratos.setEmpresa(getIdEmpresa());
			this.ifzObraContratos.orderBy(orderBy);
			return this.ifzObraContratos.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraContratos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzObraContratos.setEmpresa(getIdEmpresa());
			this.ifzObraContratos.orderBy(orderBy);
			return this.ifzObraContratos.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraContratos> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzObraContratos.setEmpresa(getIdEmpresa());
			this.ifzObraContratos.orderBy(orderBy);
			return this.ifzObraContratos.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraContratos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraContratos.setEmpresa(getIdEmpresa());
			this.ifzObraContratos.orderBy(orderBy);
			return this.ifzObraContratos.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraContratosFac.findLikeProperties(params, limite)", e);
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
//	  2.2	| 15/06/2016 | Javier Tirado	| Creacion de ObraContratosFac