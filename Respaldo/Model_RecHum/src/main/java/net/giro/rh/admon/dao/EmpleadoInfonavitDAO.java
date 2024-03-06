package net.giro.rh.admon.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.EmpleadoInfonavit;

@Remote
public interface EmpleadoInfonavitDAO extends DAO<EmpleadoInfonavit> {
	public void orderBy(String orderBy);
	
	public List<EmpleadoInfonavit> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoInfonavit> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<EmpleadoInfonavit> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<EmpleadoInfonavit> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<EmpleadoInfonavit> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoInfonavitDAO