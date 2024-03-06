package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.OperacionesIntegradasSql;

@Remote
public interface OperacionesIntegradasSqlDAO extends DAO<OperacionesIntegradasSql> {
	public void orderBy(String orderBy);

	public long save(OperacionesIntegradasSql entity, long codigoEmpresa) throws Exception;
	
	public List<OperacionesIntegradasSql> saveOrUpdateList(List<OperacionesIntegradasSql> entities, long codigoEmpresa) throws Exception;

	public List<OperacionesIntegradasSql> findAll(long idOperacionIntegrada, String orderBy) throws Exception;

	public List<OperacionesIntegradasSql> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<OperacionesIntegradasSql> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasSqlDAO