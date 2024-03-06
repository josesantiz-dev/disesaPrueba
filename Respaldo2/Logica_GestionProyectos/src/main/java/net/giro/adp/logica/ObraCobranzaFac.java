package net.giro.adp.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraCobranza;
import net.giro.adp.dao.ObraCobranzaDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ObraCobranzaFac implements ObraCobranzaRem {
	private static Logger log = Logger.getLogger(ObraCobranzaFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ObraCobranzaDAO ifzObraCobranzas;
	private static String orderBy;
	
	public ObraCobranzaFac() {
		Hashtable<String, Object> p = null;
		
		try{
			p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzObraCobranzas = (ObraCobranzaDAO) this.ctx.lookup("ejb:/Model_GestionProyectos//ObraCobranzaImp!net.giro.adp.dao.ObraCobranzaDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_GestionProyectos.ObraCobranzaFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void orderBy(String orderBy) {
		ObraCobranzaFac.orderBy = orderBy;
	}

	@Override
	public Long save(ObraCobranza entity) throws Exception {
		try {
			return this.ifzObraCobranzas.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.save(ObraCobranza)", e);
			throw e;
		}
	}

	@Override
	public List<ObraCobranza> saveOrUpdateList(List<ObraCobranza> entities) throws Exception {
		try {
			return this.ifzObraCobranzas.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.saveOrUpdateList(List<ObraSatics> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(ObraCobranza entity) throws Exception {
		try {
			this.ifzObraCobranzas.setEmpresa(getIdEmpresa());
			this.ifzObraCobranzas.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.update(ObraCobranza)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzObraCobranzas.setEmpresa(getIdEmpresa());
			this.ifzObraCobranzas.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public ObraCobranza findById(Long id) {
		try {
			this.ifzObraCobranzas.setEmpresa(getIdEmpresa());
			return this.ifzObraCobranzas.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<ObraCobranza> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraCobranzas.setEmpresa(getIdEmpresa());
			this.ifzObraCobranzas.orderBy(orderBy);
			return this.ifzObraCobranzas.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraCobranza> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzObraCobranzas.setEmpresa(getIdEmpresa());
			this.ifzObraCobranzas.orderBy(orderBy);
			return this.ifzObraCobranzas.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraCobranza> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzObraCobranzas.setEmpresa(getIdEmpresa());
			this.ifzObraCobranzas.orderBy(orderBy);
			return this.ifzObraCobranzas.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraCobranza> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraCobranzas.setEmpresa(getIdEmpresa());
			this.ifzObraCobranzas.orderBy(orderBy);
			return this.ifzObraCobranzas.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraCobranza> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzObraCobranzas.setEmpresa(getIdEmpresa());
			this.ifzObraCobranzas.orderBy(orderBy);
			return this.ifzObraCobranzas.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null; 
		}
	}

	@Override
	public List<ObraCobranza> findCobranza(List<Object> facturasActuales, long idObra, int limite) throws Exception {
		return null;
	}

	@Override
	public ObraCobranza comprobarConcepto(Long idObra, Long idFactura, Long idConcepto) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		List<ObraCobranza> lista = null;
		
		try {
			params.put("idObra.id", idObra.toString());
			params.put("idFactura", idFactura.toString());
			params.put("idConcepto", idConcepto.toString());
			
			lista = this.findByProperties(params, 0);
			if (lista == null || lista.isEmpty())
				return null;
			
			return lista.get(0);
		} catch (Exception e) {
			log.error("error en Logica_GestionProyectos.ObraCobranzaFac.comprobarConcepto(idObra, idFactura, idConcepto)", e);
			throw e;
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
//	  2.2	| 24/05/2016 | Javier Tirado	| Creacion de ObraCobranzaFac