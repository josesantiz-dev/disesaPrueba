package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.TransaccionConceptos;

@Remote
public interface TransaccionConceptosDAO extends DAO<TransaccionConceptos> {
	public void orderBy(String orderBy);

	public long save(TransaccionConceptos entity, long codigoEmpresa) throws Exception;
	
	public List<TransaccionConceptos> saveOrUpdateList(List<TransaccionConceptos> entities, long codigoEmpresa) throws Exception;
	
	public List<TransaccionConceptos> findAll(long idTransaccion) throws Exception;

	public List<TransaccionConceptos> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<TransaccionConceptos> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<TransaccionConceptos> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<TransaccionConceptos> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<TransaccionConceptos> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/06/2016 | Javier Tirado	| Creacion de TransaccionConceptosDAO