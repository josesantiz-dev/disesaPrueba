package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.TransaccionesData;

@Remote
public interface TransaccionesDataDAO extends DAO<TransaccionesData> {
	public void orderBy(String orderBy);
	
	public List<TransaccionesData> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<TransaccionesData> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<TransaccionesData> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<TransaccionesData> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<TransaccionesData> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
