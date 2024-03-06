package net.giro.cxc.dao;

import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;

import net.giro.cxc.beans.FacturaDetalle;

@Remote
public interface FacturaDetalleDAO extends DAO<FacturaDetalle>{
	
	//public long save(FacturaDetalle entity);
	
	public void delete(FacturaDetalle entity);
	
	public void update(FacturaDetalle entity);

	public FacturaDetalle findById(Long id);

	public List<FacturaDetalle> findByProperty(String propertyName, Object value);
	
	public List<FacturaDetalle> findAll();

	public List<FacturaDetalle> findByPropertyPojoCompleto(String propertyName, Object value);
	
}
