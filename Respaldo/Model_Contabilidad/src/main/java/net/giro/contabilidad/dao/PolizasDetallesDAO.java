package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.contabilidad.beans.PolizasDetalles;

@Remote
public interface PolizasDetallesDAO extends DAO<PolizasDetalles> {
	public void orderBy(String orderBy);
	
	public List<PolizasDetalles> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<PolizasDetalles> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<PolizasDetalles> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<PolizasDetalles> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<PolizasDetalles> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
