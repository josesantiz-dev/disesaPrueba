package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraCobranza;

@Remote
public interface ObraCobranzaDAO extends DAO<ObraCobranza> {
	public void orderBy(String orderBy);

	public void setEmpresa(Long idEmpresa);
	
	public List<ObraCobranza> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraCobranza> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ObraCobranza> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ObraCobranza> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<ObraCobranza> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 24/05/2016 | Javier Tirado	| Creacion de ObraCobranzaDAO