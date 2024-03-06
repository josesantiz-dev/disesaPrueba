package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.TransaccionConceptos;
import net.giro.plataforma.InfoSesion;

@Remote
public interface TransaccionConceptosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(TransaccionConceptos entity) throws ExcepConstraint;
	//public Long save(TransaccionConceptosExt entityExt) throws ExcepConstraint;
	
	public void update(TransaccionConceptos entity) throws ExcepConstraint;
	//public void update(TransaccionConceptosExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public TransaccionConceptos findById(Long id);
	//public TransaccionConceptosExt findExtById(Long id) throws Exception;
	
	public List<TransaccionConceptos> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<TransaccionConceptosExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<TransaccionConceptos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<TransaccionConceptosExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<TransaccionConceptos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<TransaccionConceptosExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<TransaccionConceptos> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<TransaccionConceptosExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<TransaccionConceptos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<TransaccionConceptosExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de TransaccionConceptosRem