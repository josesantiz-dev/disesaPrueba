package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraAvance;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraAvanceRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void orderBy(String orderBy);

	public Long save(ObraAvance entity) throws Exception;

	public List<ObraAvance> saveOrUpdateList(List<ObraAvance> entities) throws Exception;

	public void update(ObraAvance entity) throws Exception;
	
	public void delete(Long entityId) throws Exception;

	public ObraAvance findById(Long id);
	
	public List<ObraAvance> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraAvance> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<ObraAvance> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraAvance> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<ObraAvance> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 14/06/2016 | Javier Tirado	| Creacion de ObraAvanceRem