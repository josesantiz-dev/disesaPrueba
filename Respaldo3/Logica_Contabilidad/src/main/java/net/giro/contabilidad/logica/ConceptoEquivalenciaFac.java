package net.giro.contabilidad.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.ConceptoEquivalencia;
import net.giro.contabilidad.dao.ConceptoEquivalenciaDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ConceptoEquivalenciaFac implements ConceptoEquivalenciaRem {
	private static Logger log = Logger.getLogger(ConceptoEquivalenciaFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ConceptoEquivalenciaDAO ifzConceptoEquivalencias;
	
	public ConceptoEquivalenciaFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzConceptoEquivalencias = (ConceptoEquivalenciaDAO) this.ctx.lookup("ejb:/Model_Contabilidad//ConceptoEquivalenciaImp!net.giro.contabilidad.dao.ConceptoEquivalenciaDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.ConceptoEquivalenciaFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(ConceptoEquivalencia entity) throws Exception {
		try {
			return this.ifzConceptoEquivalencias.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptoEquivalenciaFac.save(ConceptoEquivalencia)", e);
			throw e;
		}
	}

	@Override
	public List<ConceptoEquivalencia> saveOrUpdateList(List<ConceptoEquivalencia> entities) throws Exception {
		try {
			return this.ifzConceptoEquivalencias.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptoEquivalenciaFac.saveOrUpdateList(List<ConceptoEquivalencia> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(ConceptoEquivalencia entity) throws Exception {
		try {
			this.ifzConceptoEquivalencias.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptoEquivalenciaFac.update(ConceptoEquivalencia)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idImpuestoEquivalencia) throws Exception {
		try {
			this.ifzConceptoEquivalencias.delete(idImpuestoEquivalencia);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptoEquivalenciaFac.delete(idImpuestoEquivalencia)", e);
			throw e;
		}
	}
	
	@Override
	public ConceptoEquivalencia findById(Long idConceptoEquivalencia) {
		try {
			return this.ifzConceptoEquivalencias.findById(idConceptoEquivalencia);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptoEquivalenciaFac.findById(idConceptoEquivalencia)", e);
			throw e;
		}
	}

	@Override
	public List<ConceptoEquivalencia> findAll(long codigoTransaccion, String orderBy) throws Exception {
		try {
			return this.ifzConceptoEquivalencias.findAll(codigoTransaccion, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptoEquivalenciaFac.findAll(codigoTransaccion, orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<ConceptoEquivalencia> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzConceptoEquivalencias.findByProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptoEquivalenciaFac.findByProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<ConceptoEquivalencia> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzConceptoEquivalencias.findLikeProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptoEquivalenciaFac.findLikeProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<ConceptoEquivalencia> findInProperty(String propertyName, List<Object> values, String orderBy, int limite) throws Exception {
		try {
			return this.ifzConceptoEquivalencias.findInProperty(propertyName, values, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ConceptoEquivalenciaFac.findInProperty(propertyName, values, orderBy, limite)", e);
			throw e;
		} 
	}

	// ------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------
	
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
//	  2.2	| DD/MM/YYYY | Javier Tirado	| Creacion de ConceptoEquivalenciaFac