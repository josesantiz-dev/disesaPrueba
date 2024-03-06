package net.giro.cxc.dao;

import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;

import net.giro.cxc.beans.Impuesto;

@Remote
public interface ImpuestoDAO extends DAO<Impuesto>{
	
	public long save(Impuesto entity);
	
	public void delete(Impuesto entity);
	
	public void update(Impuesto entity);

	public Impuesto findById(Long id);

	public List<Impuesto> findByProperty(String propertyName, Object value);
	
	public List<Impuesto> findAll();
	
	public List<Impuesto> findByPropertyPojoCompleto(String propertyName, String tipo, Object value);
}
