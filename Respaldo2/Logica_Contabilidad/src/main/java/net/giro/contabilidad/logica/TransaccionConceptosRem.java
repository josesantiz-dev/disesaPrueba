package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.TransaccionConceptos;
import net.giro.plataforma.InfoSesion;

@Remote
public interface TransaccionConceptosRem {
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(TransaccionConceptos entity) throws Exception;

	public List<TransaccionConceptos> saveOrUpdateList(List<TransaccionConceptos> entities) throws Exception;

	public void update(TransaccionConceptos entity) throws Exception;
	
	public void delete(Long entityId) throws Exception;

	public TransaccionConceptos findById(Long id);
	
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
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de TransaccionConceptosRem