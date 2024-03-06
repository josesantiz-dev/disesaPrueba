package net.giro.rh.admon.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoInfonavit;

@Remote
public interface EmpleadoInfonavitRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(EmpleadoInfonavit entity) throws Exception;
	
	public List<EmpleadoInfonavit> saveOrUpdateList(List<EmpleadoInfonavit> entities) throws Exception;
	
	public void update(EmpleadoInfonavit entity) throws Exception;
	
	public void delete(Long entityId) throws Exception;

	public EmpleadoInfonavit findById(Long id);
	
	public List<EmpleadoInfonavit> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoInfonavit> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<EmpleadoInfonavit> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoInfonavit> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<EmpleadoInfonavit> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public boolean comprobarRegistro(EmpleadoInfonavit entity) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoInfonavitRem