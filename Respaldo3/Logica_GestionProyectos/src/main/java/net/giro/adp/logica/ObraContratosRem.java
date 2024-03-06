package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraContratos;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraContratosRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void orderBy(String orderBy);

	public Long save(ObraContratos entity) throws Exception;

	public List<ObraContratos> saveOrUpdateList(List<ObraContratos> entities) throws Exception;

	public void update(ObraContratos entity) throws Exception;
	
	public void delete(Long entityId) throws Exception;

	public ObraContratos findById(Long id);
	
	public List<ObraContratos> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraContratos> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<ObraContratos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraContratos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<ObraContratos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 15/06/2016 | Javier Tirado	| Creacion de ObraContratosRem