package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraContratos;

@Remote
public interface ObraContratosDAO extends DAO<ObraContratos> {
	public void orderBy(String orderBy);
	
	public List<ObraContratos> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraContratos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ObraContratos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ObraContratos> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<ObraContratos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 15/06/2016 | Javier Tirado	| Creacion de ObraContratosDAO