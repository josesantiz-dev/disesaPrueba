package net.giro.contabilidad.logica;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.Conceptos;
import net.giro.contabilidad.dao.ConceptosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ConceptosFac implements ConceptosRem {
	private static Logger log = Logger.getLogger(ConceptosFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ConceptosDAO ifzConceptoss;
	private static String orderBy;
	
	public ConceptosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzConceptoss = (ConceptosDAO) this.ctx.lookup("ejb:/Model_Contabilidad//ConceptosImp!net.giro.contabilidad.dao.ConceptosDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.ConceptosFac", e);
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
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void orderBy(String orderBy) {
		ConceptosFac.orderBy = orderBy;
	}

	@Override
	public Long save(Conceptos entity) throws Exception {
		try {
			return this.ifzConceptoss.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.save(Conceptos)", e);
			throw e;
		}
	}

	@Override
	public List<Conceptos> saveOrUpdateList(List<Conceptos> entities) throws Exception {
		try {
			return this.ifzConceptoss.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.saveOrUpdateList(List<Conceptos> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(Conceptos entity) throws Exception {
		try {
			this.ifzConceptoss.setEmpresa(getIdEmpresa());
			this.ifzConceptoss.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.update(Conceptos)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzConceptoss.setEmpresa(getIdEmpresa());
			this.ifzConceptoss.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.delete(Long)", e);
			throw e;
		}
	}
	
	@Override
	public Conceptos findById(Long id) {
		try {
			this.ifzConceptoss.setEmpresa(getIdEmpresa());
			return this.ifzConceptoss.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<Conceptos> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzConceptoss.setEmpresa(getIdEmpresa());
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Conceptos> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzConceptoss.setEmpresa(getIdEmpresa());
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Conceptos> findLikeProperty(String propertyName, Object value, Integer limite) throws Exception {
		try {
			this.ifzConceptoss.setEmpresa(getIdEmpresa());
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Conceptos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzConceptoss.setEmpresa(getIdEmpresa());
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Conceptos> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzConceptoss.setEmpresa(getIdEmpresa());
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Conceptos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzConceptoss.setEmpresa(getIdEmpresa());
			this.ifzConceptoss.orderBy(orderBy);
			return this.ifzConceptoss.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public Conceptos cancelar(Conceptos entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor(this.infoSesion.getResponsabilidad().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());

			this.ifzConceptoss.setEmpresa(getIdEmpresa());
			this.ifzConceptoss.update(entity);
			
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.cancelar(entity)", e);
			throw e;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| DD/MM/YYYY | Javier Tirado	| Creacion de ConceptosFac