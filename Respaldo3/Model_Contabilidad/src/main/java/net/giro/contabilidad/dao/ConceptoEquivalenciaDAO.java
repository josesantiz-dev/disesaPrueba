package net.giro.contabilidad.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.ConceptoEquivalencia;

@Remote
public interface ConceptoEquivalenciaDAO extends DAO<ConceptoEquivalencia> {
	public long save(ConceptoEquivalencia entity, long codigoEmpresa) throws Exception;
	
	public List<ConceptoEquivalencia> saveOrUpdateList(List<ConceptoEquivalencia> entities, long codigoEmpresa) throws Exception;

	public List<ConceptoEquivalencia> findAll(long codigoTransaccion, String orderBy) throws Exception;

	public List<ConceptoEquivalencia> findByProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<ConceptoEquivalencia> findLikeProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<ConceptoEquivalencia> findInProperty(String propertyName, List<Object> values, long idEmpresa, String orderBy, int limite) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2019-05-17 | Javier Tirado 	| Creacion de ConceptoEquivalenciaDAO
 */
