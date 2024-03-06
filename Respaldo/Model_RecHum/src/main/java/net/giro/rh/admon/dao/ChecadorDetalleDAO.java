package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.ChecadorDetalle;

@Remote
public interface ChecadorDetalleDAO extends DAO<ChecadorDetalle> {
	public void orderBy(String orderBy);
	
	public List<ChecadorDetalle> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<ChecadorDetalle> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<ChecadorDetalle> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<ChecadorDetalle> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<ChecadorDetalle> findLikeProperties(HashMap<String, Object> params, int limite) throws Exception;
	
	public List<ChecadorDetalle> findByDates(Date fechaDesde, Date fechaHasta) throws Exception;
}
