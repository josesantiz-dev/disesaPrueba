package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.Checador;

@Remote
public interface ChecadorDAO extends DAO<Checador> {
	public void orderBy(String orderBy);
	
	public List<Checador> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<Checador> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<Checador> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<Checador> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<Checador> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta) throws Exception;
}
