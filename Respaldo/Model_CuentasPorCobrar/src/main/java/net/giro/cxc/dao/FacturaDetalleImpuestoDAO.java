package net.giro.cxc.dao;

import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;

import net.giro.cxc.beans.FacturaDetalleImpuesto;

@Remote
public interface FacturaDetalleImpuestoDAO extends DAO<FacturaDetalleImpuesto>{
	
	public long save(FacturaDetalleImpuesto entity);
	
	public void delete(FacturaDetalleImpuesto entity);
	
	public void update(FacturaDetalleImpuesto entity);

	public FacturaDetalleImpuesto findById(Long id);

	public List<FacturaDetalleImpuesto> findByProperty(String propertyName, Object value);
	
	public List<FacturaDetalleImpuesto> findAll();
	
	public List<FacturaDetalleImpuesto> findByPropertyPojoCompleto(String propertyName, String tipo, Object value);
	
}
