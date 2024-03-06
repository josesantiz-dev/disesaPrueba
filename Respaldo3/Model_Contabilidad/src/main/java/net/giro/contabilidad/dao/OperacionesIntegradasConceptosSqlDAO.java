package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;

@Remote
public interface OperacionesIntegradasConceptosSqlDAO extends DAO<OperacionesIntegradasConceptosSql> {
	public void orderBy(String orderBy);

	public long save(OperacionesIntegradasConceptosSql entity, long codigoEmpresa) throws Exception;
	
	public List<OperacionesIntegradasConceptosSql> saveOrUpdateList(List<OperacionesIntegradasConceptosSql> entities, long codigoEmpresa) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findAll(long idOperacionIntegrada, String orderBy) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<OperacionesIntegradasConceptosSql> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasConceptosSqlDAO