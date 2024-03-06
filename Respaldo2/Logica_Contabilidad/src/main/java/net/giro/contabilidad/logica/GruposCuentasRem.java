package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.contabilidad.beans.GruposCuentas;
import net.giro.plataforma.InfoSesion;

@Remote
public interface GruposCuentasRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(GruposCuentas entity) throws Exception;

	public List<GruposCuentas> saveOrUpdateList(List<GruposCuentas> entities) throws Exception;

	public void update(GruposCuentas entity) throws Exception;
	
	public void delete(Long entity) throws Exception;

	public GruposCuentas findById(Long id);
	
	public List<GruposCuentas> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<GruposCuentas> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<GruposCuentas> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<GruposCuentas> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<GruposCuentas> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}