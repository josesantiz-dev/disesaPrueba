package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.Conceptos;

@Remote
public interface ConceptosDAO extends DAO<Conceptos> {
	public void orderBy(String orderBy);
	
	public List<Conceptos> findByProperty(String propertyName, Object value, int limite) throws Exception;

	public List<Conceptos> findLikeProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<Conceptos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Conceptos> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Conceptos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 30/05/2016 | Javier Tirado	| Creacion de ConceptosDAO