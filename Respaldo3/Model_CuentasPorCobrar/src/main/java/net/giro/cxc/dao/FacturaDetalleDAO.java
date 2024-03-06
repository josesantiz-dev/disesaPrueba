package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.cxc.beans.FacturaDetalle;

@Remote
public interface FacturaDetalleDAO extends DAO<FacturaDetalle>{
	public long save(FacturaDetalle entity, long codigoEmpresa) throws Exception;
	
	public List<FacturaDetalle> saveOrUpdateList(List<FacturaDetalle> entities, long codigoEmpresa) throws Exception;
	
	public List<FacturaDetalle> findAll(long idFactura);

	public List<FacturaDetalle> findByProperty(String propertyName, Object value, long idEmpresa);

	public List<FacturaDetalle> findLikeProperty(String propertyName, String value, long idEmpresa);
}
