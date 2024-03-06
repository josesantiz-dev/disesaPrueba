package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.TransaccionConceptos;

@Remote
public interface TransaccionConceptosDAO extends DAO<TransaccionConceptos> {
	public void orderBy(String orderBy);

	public void setEmpresa(Long idEmpresa);
	
	public long save(TransaccionConceptos entity, Long idEmpresa) throws Exception;
	
	public List<TransaccionConceptos> saveOrUpdateList(List<TransaccionConceptos> entities, Long idEmpresa) throws Exception;

	public List<TransaccionConceptos> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<TransaccionConceptos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<TransaccionConceptos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<TransaccionConceptos> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<TransaccionConceptos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/06/2016 | Javier Tirado	| Creacion de TransaccionConceptosDAO