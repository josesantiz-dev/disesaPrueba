package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.OperacionesIntegradasSql;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OperacionesIntegradasSqlRem {
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(OperacionesIntegradasSql entity) throws Exception;

	public List<OperacionesIntegradasSql> saveOrUpdateList(List<OperacionesIntegradasSql> entities) throws Exception;

	public void update(OperacionesIntegradasSql entity) throws Exception;
	
	public void delete(Long entityId) throws Exception;

	public OperacionesIntegradasSql findById(Long id);
	
	public List<OperacionesIntegradasSql> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesIntegradasSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasSqlRem