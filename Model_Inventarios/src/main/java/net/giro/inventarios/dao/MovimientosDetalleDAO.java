package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.MovimientosDetalle;

@Remote
public interface MovimientosDetalleDAO extends DAO<MovimientosDetalle> {
	public long save(MovimientosDetalle entity, long codigoEmpresa) throws Exception;
	
	public List<MovimientosDetalle> saveOrUpdateList(List<MovimientosDetalle> entities, long codigoEmpresa) throws Exception;
	
	public List<MovimientosDetalle> findAll(long idAlmacenMovimiento);

	public List<MovimientosDetalle> findLikeProperty(String propertyName, Object value, long idAlmacenMovimiento);

	public List<MovimientosDetalle> findByProperty(String propertyName, Object value, long idAlmacenMovimiento);
}
