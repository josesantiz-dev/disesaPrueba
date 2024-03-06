package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.cxp.beans.GastosImpuesto;

@Remote
public interface GastosImpuestoDAO extends DAO<GastosImpuesto> {
	/*public long save(GastosImpuesto entity);
	
	public void update(GastosImpuesto entity);
	
	public void delete(GastosImpuesto entity);*/

	public GastosImpuesto findById(Long id);

	public List<GastosImpuesto> findByProperty(String propertyName, Object value);
	
	public List<GastosImpuesto> findAll();
	
	public List<GastosImpuesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
}
