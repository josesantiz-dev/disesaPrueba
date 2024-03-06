package net.giro.compras.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.OrdenCompraDetalle;

@Remote
public interface OrdenCompraDetalleDAO extends DAO<OrdenCompraDetalle> {
	public void OrderBy(String orderBy);
	
	public List<OrdenCompraDetalle> findByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<OrdenCompraDetalle> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<OrdenCompraDetalle> findInProperty(String columnName, List<Object> values) throws Exception;

	public List<OrdenCompraDetalle> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<OrdenCompraDetalle> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
}
