package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;
import net.giro.plataforma.InfoSesion;

@Remote
public interface OperacionesIntegradasConceptosSqlRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(OperacionesIntegradasConceptosSql entity) throws ExcepConstraint;
	//public Long save(OperacionesIntegradasConceptosSqlExt entityExt) throws ExcepConstraint;
	
	public void update(OperacionesIntegradasConceptosSql entity) throws ExcepConstraint;
	//public void update(OperacionesIntegradasConceptosSqlExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public OperacionesIntegradasConceptosSql findById(Long id);
	//public OperacionesIntegradasConceptosSqlExt findExtById(Long id) throws Exception;
	
	public List<OperacionesIntegradasConceptosSql> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<OperacionesIntegradasConceptosSqlExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<OperacionesIntegradasConceptosSqlExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesIntegradasConceptosSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<OperacionesIntegradasConceptosSqlExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<OperacionesIntegradasConceptosSqlExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<OperacionesIntegradasConceptosSql> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<OperacionesIntegradasConceptosSqlExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasConceptosSqlRem