package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;

@Remote
public interface OperacionesIntegradasConceptosSqlDAO extends DAO<OperacionesIntegradasConceptosSql> {
	public void orderBy(String orderBy);
	
	public List<OperacionesIntegradasConceptosSql> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesIntegradasConceptosSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasConceptosSqlDAO