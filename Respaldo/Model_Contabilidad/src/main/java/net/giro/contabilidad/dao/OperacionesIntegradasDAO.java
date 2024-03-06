package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.OperacionesIntegradas;

@Remote
public interface OperacionesIntegradasDAO extends DAO<OperacionesIntegradas> {
	public void orderBy(String orderBy);
	
	public List<OperacionesIntegradas> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<OperacionesIntegradas> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<OperacionesIntegradas> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<OperacionesIntegradas> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<OperacionesIntegradas> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;

}
