package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.FacturaDetalleImpuesto;

@Remote
public interface FacturaDetalleImpuestoDAO extends DAO<FacturaDetalleImpuesto> {
	public long save(FacturaDetalleImpuesto entity, long codigoEmpresa) throws Exception;
	
	public List<FacturaDetalleImpuesto> saveOrUpdateList(List<FacturaDetalleImpuesto> entities, long codigoEmpresa) throws Exception;
	
	public List<FacturaDetalleImpuesto> findAll(long idFacturaDetalle, String orderBy) throws Exception;

	public List<FacturaDetalleImpuesto> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	public List<FacturaDetalleImpuesto> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception;
}
