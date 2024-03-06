package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.AsignacionGrupos;
import net.giro.contabilidad.beans.AsignacionGruposExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface AsignacionGruposRem {
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(AsignacionGrupos entity) throws Exception;
	
	public Long save(AsignacionGruposExt entityExt) throws Exception;

	public List<AsignacionGrupos> saveOrUpdateList(List<AsignacionGrupos> entities) throws Exception;

	public void update(AsignacionGrupos entity) throws Exception;
	
	public void update(AsignacionGruposExt entityExt) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public AsignacionGrupos findById(Long id);
	
	public AsignacionGruposExt findExtById(Long id) throws Exception;
	
	public List<AsignacionGrupos> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<AsignacionGruposExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<AsignacionGrupos> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<AsignacionGruposExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<AsignacionGrupos> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	
	public List<AsignacionGruposExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<AsignacionGrupos> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<AsignacionGruposExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<AsignacionGrupos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<AsignacionGruposExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public AsignacionGruposExt convertir(AsignacionGrupos entity) throws Exception;
	
	public AsignacionGrupos convertir(AsignacionGruposExt entityExt) throws Exception;
}