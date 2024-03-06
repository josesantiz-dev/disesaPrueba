package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.ComparativaDetalle;

@Remote
public interface ComparativaDetalleDAO extends DAO<ComparativaDetalle> {
	public void OrderBy(String orderBy);
	
	public List<ComparativaDetalle> findByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<ComparativaDetalle> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<ComparativaDetalle> findInProperty(String columnName, List<Object> values) throws Exception;
}
