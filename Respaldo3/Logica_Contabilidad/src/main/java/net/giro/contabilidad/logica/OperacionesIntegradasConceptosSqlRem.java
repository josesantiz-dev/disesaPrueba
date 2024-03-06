package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OperacionesIntegradasConceptosSqlRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(OperacionesIntegradasConceptosSql entity) throws Exception;

	public List<OperacionesIntegradasConceptosSql> saveOrUpdateList(List<OperacionesIntegradasConceptosSql> entities) throws Exception;

	public void update(OperacionesIntegradasConceptosSql entity) throws Exception;
	
	public void delete(long idOperacionIntegradaConceptosSql) throws Exception;

	public OperacionesIntegradasConceptosSql findById(long idOperacionIntegradaConceptosSql);

	public List<OperacionesIntegradasConceptosSql> findAll(long idOperacionIntegrada) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findAll(long idOperacionIntegrada, String orderBy) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findByOperacionIntegrada(long idOperacionIntegrada) throws Exception;

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
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasConceptosSqlRem