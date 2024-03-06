package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.Operaciones;

@Remote
public interface OperacionesDAO extends DAO<Operaciones> {
	public void orderBy(String orderBy);
	
	public List<Operaciones> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Operaciones> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Operaciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Operaciones> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Operaciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
