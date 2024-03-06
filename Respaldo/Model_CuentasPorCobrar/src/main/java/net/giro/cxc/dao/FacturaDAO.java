package net.giro.cxc.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.Factura;

@Remote
public interface FacturaDAO extends DAO<Factura>{
	public void orderBy(String orderBy);
	
	public List<Factura> findAll();

	public List<Factura> findByProperty(String propertyName, Object value);
	
	public List<Factura> findByProperty(String propertyName, Object value, int tipoObra);

	public List<Factura> findByProperty(String propertyName, Object value, int tipoObra, int max);
	
	public List<Factura> findLikeProperty(String propertyName, Object value);

	public List<Factura> findLikeProperty(String propertyName, Object value, int max);

	public List<Factura> findLikeProperty(String propertyName, Object value, int tipoObra, int max);
	
	public List<Factura> findByPropertyPojoCompleto(String propertyName, Object value, int tipo);
	
	public List<Factura> findLikeProperties(HashMap<String, Object> params) throws Exception;

	public List<Factura> saveOrUpdateList(List<Factura> entities) throws Exception;
}
