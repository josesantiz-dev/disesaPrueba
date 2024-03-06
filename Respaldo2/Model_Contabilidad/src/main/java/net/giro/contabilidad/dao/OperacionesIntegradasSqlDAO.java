package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.OperacionesIntegradasSql;

@Remote
public interface OperacionesIntegradasSqlDAO extends DAO<OperacionesIntegradasSql> {
	public void orderBy(String orderBy);

	public void setEmpresa(Long idEmpresa);
	
	public long save(OperacionesIntegradasSql entity, Long idEmpresa) throws Exception;
	
	public List<OperacionesIntegradasSql> saveOrUpdateList(List<OperacionesIntegradasSql> entities, Long idEmpresa) throws Exception;

	public List<OperacionesIntegradasSql> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesIntegradasSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasSqlDAO