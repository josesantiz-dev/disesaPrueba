package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.PolizasInterfaces;

@Remote
public interface PolizasInterfacesDAO extends DAO<PolizasInterfaces> {
	public void orderBy(String orderBy);
	
	public List<PolizasInterfaces> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<PolizasInterfaces> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PolizasInterfaces> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<PolizasInterfaces> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<PolizasInterfaces> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
