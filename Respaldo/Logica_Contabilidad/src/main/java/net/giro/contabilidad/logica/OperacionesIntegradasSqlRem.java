package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.OperacionesIntegradasSql;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OperacionesIntegradasSqlRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(OperacionesIntegradasSql entity) throws ExcepConstraint;
	//public Long save(OperacionesIntegradasSqlExt entityExt) throws ExcepConstraint;
	
	public void update(OperacionesIntegradasSql entity) throws ExcepConstraint;
	//public void update(OperacionesIntegradasSqlExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public OperacionesIntegradasSql findById(Long id);
	//public OperacionesIntegradasSqlExt findExtById(Long id) throws Exception;
	
	public List<OperacionesIntegradasSql> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<OperacionesIntegradasSqlExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<OperacionesIntegradasSqlExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesIntegradasSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<OperacionesIntegradasSqlExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<OperacionesIntegradasSqlExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<OperacionesIntegradasSql> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<OperacionesIntegradasSqlExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasSqlRem