package net.giro.rh.admon.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoInfonavit;

@Remote
public interface EmpleadoInfonavitRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(EmpleadoInfonavit entity) throws ExcepConstraint;
	//public Long save(EmpleadoInfonavitExt entityExt) throws ExcepConstraint;
	
	public void update(EmpleadoInfonavit entity) throws ExcepConstraint;
	//public void update(EmpleadoInfonavitExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public EmpleadoInfonavit findById(Long id);
	//public EmpleadoInfonavitExt findExtById(Long id) throws Exception;
	
	public List<EmpleadoInfonavit> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<EmpleadoInfonavitExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoInfonavit> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<EmpleadoInfonavitExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<EmpleadoInfonavit> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<EmpleadoInfonavitExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<EmpleadoInfonavit> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	//public List<EmpleadoInfonavitExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<EmpleadoInfonavit> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<EmpleadoInfonavitExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;

	//public EmpleadoInfonavit cancelar(EmpleadoInfonavit entity) throws Exception;
	//public EmpleadoInfonavit cancelar(EmpleadoInfonavitExt entityExt) throws Exception;
	
	public boolean comprobarRegistro(EmpleadoInfonavit entity) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoInfonavitRem