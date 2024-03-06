package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.adp.beans.ObraContratos;

@Remote
public interface ObraContratosDAO extends DAO<ObraContratos> {
	public void orderBy(String orderBy);

	public long save(ObraContratos entity, long codigoEmpresa) throws Exception;
	
	public List<ObraContratos> saveOrUpdateList(List<ObraContratos> entities, long codigoEmpresa) throws Exception;
	
	public List<ObraContratos> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<ObraContratos> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<ObraContratos> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<ObraContratos> findByProperties(HashMap<String, Object> params, long idEmpresa, int limite) throws Exception;

	public List<ObraContratos> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 15/06/2016 | Javier Tirado	| Creacion de ObraContratosDAO