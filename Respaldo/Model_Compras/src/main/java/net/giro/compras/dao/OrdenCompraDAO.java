package net.giro.compras.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.OrdenCompra;

@Remote
public interface OrdenCompraDAO extends DAO<OrdenCompra> {
	public void OrderBy(String orderBy);
	
	public void estatus(Long estatus);
	
	public List<OrdenCompra> findByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<OrdenCompra> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<OrdenCompra> findInProperty(String columnName, List<Object> values) throws Exception;

	public List<OrdenCompra> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<OrdenCompra> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<OrdenCompra> findNoCompletas(String propertyName, Object value, int max) throws Exception;
	
	public int findConsecutivoByProveedor(long idProveedor) throws Exception;
}
