package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.inventarios.beans.MovimientosDetalle;

@Remote
public interface MovimientosDetalleDAO extends DAO<MovimientosDetalle> {
	public List<MovimientosDetalle> saveOrUpdateList(List<MovimientosDetalle> entities) throws Exception;
	
	public List<MovimientosDetalle> findAllActivos();
	
	public List<MovimientosDetalle> findDetallesById(long idAlmacenMovimiento);

	public List<MovimientosDetalle> findByProperty(String propertyName, Object value);
}
