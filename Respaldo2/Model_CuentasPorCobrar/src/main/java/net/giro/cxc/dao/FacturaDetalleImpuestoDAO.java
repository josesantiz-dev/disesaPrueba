package net.giro.cxc.dao;

import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;

import net.giro.cxc.beans.FacturaDetalleImpuesto;

@Remote
public interface FacturaDetalleImpuestoDAO extends DAO<FacturaDetalleImpuesto> {
	public List<FacturaDetalleImpuesto> findAll(String orderBy) throws Exception;

	public List<FacturaDetalleImpuesto> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	public List<FacturaDetalleImpuesto> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception;
}
