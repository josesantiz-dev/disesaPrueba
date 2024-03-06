package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.contabilidad.beans.GruposCuentas;
import net.giro.plataforma.InfoSesion;

@Remote
public interface GruposCuentasRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(GruposCuentas entity) throws ExcepConstraint;
	//public Long save(GruposCuentasExt entityExt) throws ExcepConstraint;
	
	public void update(GruposCuentas entity) throws ExcepConstraint;
	//public void update(GruposCuentasExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entity) throws ExcepConstraint;

	public GruposCuentas findById(Long id);
	//public GruposCuentasExt findExtById(Long id) throws Exception;
	
	public List<GruposCuentas> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<GruposCuentasExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<GruposCuentas> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<GruposCuentasExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<GruposCuentas> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<GruposCuentasExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<GruposCuentas> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<GruposCuentasExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<GruposCuentas> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<GruposCuentasExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}