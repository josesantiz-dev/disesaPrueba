package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.AsignacionGrupos;
import net.giro.contabilidad.beans.AsignacionGruposExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface AsignacionGruposRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public void showSystemOuts(boolean value);
	
	public void orderBy(String orderBy);

	public Long save(AsignacionGrupos entity) throws ExcepConstraint;
	
	public Long save(AsignacionGruposExt entityExt) throws ExcepConstraint;
	
	public void update(AsignacionGrupos entity) throws ExcepConstraint;
	
	public void update(AsignacionGruposExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

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