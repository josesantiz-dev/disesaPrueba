package net.giro.adp.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.adp.beans.ObraAvance;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Remote
public interface ObraAvanceRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(ObraAvance entity) throws ExcepConstraint;
	//public Long save(ObraAvanceExt entityExt) throws ExcepConstraint;
	
	public void update(ObraAvance entity) throws ExcepConstraint;
	//public void update(ObraAvanceExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public ObraAvance findById(Long id);
	//public ObraAvanceExt findExtById(Long id) throws Exception;
	
	public List<ObraAvance> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<ObraAvanceExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ObraAvance> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<ObraAvanceExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ObraAvance> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<ObraAvanceExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ObraAvance> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	//public List<ObraAvanceExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<ObraAvance> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<ObraAvanceExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;

	//public ObraAvance cancelar(ObraAvance entity) throws Exception;
	//public ObraAvance cancelar(ObraAvanceExt entityExt) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 14/06/2016 | Javier Tirado	| Creacion de ObraAvanceRem