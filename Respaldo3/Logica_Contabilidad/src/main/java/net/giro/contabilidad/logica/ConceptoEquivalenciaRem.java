package net.giro.contabilidad.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.ConceptoEquivalencia;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ConceptoEquivalenciaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(ConceptoEquivalencia entity) throws Exception;

	public List<ConceptoEquivalencia> saveOrUpdateList(List<ConceptoEquivalencia> entities) throws Exception;

	public void update(ConceptoEquivalencia entity) throws Exception;
	
	public void delete(Long idConceptoEquivalencia) throws Exception;

	public ConceptoEquivalencia findById(Long idConceptoEquivalencia) throws Exception;

	public List<ConceptoEquivalencia> findAll(long codigoTransaccion, String orderBy) throws Exception;

	public List<ConceptoEquivalencia> findByProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception;

	public List<ConceptoEquivalencia> findLikeProperty(String propertyName, final Object value, String orderBy, int limite) throws Exception;
	
	public List<ConceptoEquivalencia> findInProperty(String columnName, List<Object> values, String orderBy, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 30/05/2016 | Javier Tirado	| Creacion de ConceptoEquivalenciaRem