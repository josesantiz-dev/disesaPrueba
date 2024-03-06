package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.TraspasoDetalle;

@Remote
public interface TraspasoDetalleDAO extends DAO<TraspasoDetalle> {
	public long save(TraspasoDetalle entity, long codigoEmpresa) throws Exception;
	
	public List<TraspasoDetalle> saveOrUpdateList(List<TraspasoDetalle> entities, long codigoEmpresa) throws Exception;
	
	public List<TraspasoDetalle> findAll(long idAlmacenTraspaso);

	public List<TraspasoDetalle> findByProperty(String propertyName, Object value, long idTraspaso);

	public List<TraspasoDetalle> findLikeProperty(String propertyName, Object value, long idTraspaso);
}
