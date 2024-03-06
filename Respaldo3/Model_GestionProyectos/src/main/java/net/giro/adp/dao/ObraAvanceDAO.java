package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraAvance;

@Remote
public interface ObraAvanceDAO extends DAO<ObraAvance> {
	public void orderBy(String orderBy);

	public long save(ObraAvance entity, long codigoEmpresa) throws Exception;
	
	public List<ObraAvance> saveOrUpdateList(List<ObraAvance> entities, long codigoEmpresa) throws Exception;
	
	public List<ObraAvance> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<ObraAvance> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<ObraAvance> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<ObraAvance> findByProperties(HashMap<String, Object> params, long idEmpresa, int limite) throws Exception;

	public List<ObraAvance> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 14/06/2016 | Javier Tirado	| Creacion de ObraAvanceDAO