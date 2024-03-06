package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.GruposCuentas;

@Remote
public interface GruposCuentasDAO extends DAO<GruposCuentas> {
	public void orderBy(String orderBy);
	
	public List<GruposCuentas> findByProperty(String propertyName, Object value, int limite) throws Exception;

	public List<GruposCuentas> findLikeProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<GruposCuentas> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<GruposCuentas> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<GruposCuentas> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
