package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraAvance;

@Remote
public interface ObraAvanceDAO extends DAO<ObraAvance> {
	public void orderBy(String orderBy);

	public void setEmpresa(Long idEmpresa);
	
	public List<ObraAvance> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraAvance> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ObraAvance> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ObraAvance> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<ObraAvance> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 14/06/2016 | Javier Tirado	| Creacion de ObraAvanceDAO