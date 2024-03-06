package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.compras.beans.Comparativa;

@Remote
public interface ComparativaDAO extends DAO<Comparativa> {
	public void OrderBy(String orderBy);
	
	public List<Comparativa> findByProperty(String propertyName, final Object value, int max) throws Exception;

	public List<Comparativa> findLikeProperty(String propertyName, final Object value, int max) throws Exception;
	
	public List<Comparativa> findInProperty(String columnName, List<Object> values) throws Exception;
}
