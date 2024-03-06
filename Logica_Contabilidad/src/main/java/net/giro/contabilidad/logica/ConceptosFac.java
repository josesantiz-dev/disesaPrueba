package net.giro.contabilidad.logica;

import java.util.Calendar;
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
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private ConceptosDAO ifzConceptos;
	
	public ConceptosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzConceptos = (ConceptosDAO) this.ctx.lookup("ejb:/Model_Contabilidad//ConceptosImp!net.giro.contabilidad.dao.ConceptosDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.ConceptosFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(Conceptos entity) throws Exception {
		try {
			return this.ifzConceptos.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.save(Conceptos)", e);
			throw e;
		}
	}

	@Override
	public List<Conceptos> saveOrUpdateList(List<Conceptos> entities) throws Exception {
		try {
			return this.ifzConceptos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.saveOrUpdateList(List<Conceptos> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(Conceptos entity) throws Exception {
		try {
			this.ifzConceptos.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.update(Conceptos)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idConcepto) throws Exception {
		try {
			this.ifzConceptos.delete(idConcepto);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public Conceptos cancelar(Conceptos entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor(this.infoSesion.getResponsabilidad().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzConceptos.update(entity);
			
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.cancelar(entity)", e);
			throw e;
		}
	}

	@Override
	public Conceptos findById(Long idConcepto) {
		try {
			return this.ifzConceptos.findById(idConcepto);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findById(idConcepto)", e);
			throw e;
		}
	}

	@Override
	public List<Conceptos> findAll() throws Exception {
		try {
			return this.findAll("");
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findAll()", e);
			throw e;
		} 
	}

	@Override
	public List<Conceptos> findAll(String orderBy) throws Exception {
		try {
			return this.ifzConceptos.findAll(getIdEmpresa(), orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findAll(orderBy)", e);
			throw e;
		} 
	}

	@Override
	public List<Conceptos> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzConceptos.findByProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findByProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Conceptos> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			if (value != null && value.getClass() == java.lang.String.class)
				value = ((value.toString().trim().contains(" ")) ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			return this.ifzConceptos.findLikeProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findLikeProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Conceptos> findLikeProperty(String propertyName, Object value, Integer limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, "", limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	// --------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| DD/MM/YYYY | Javier Tirado	| Creacion de ConceptosFac