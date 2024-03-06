package net.giro.inventarios.dao;
import java.util.List;
import javax.ejb.Remote;
import net.giro.DAO;
import net.giro.inventarios.beans.MovimientosDetalle;

@Remote
public interface MovimientosDetalleDAO extends DAO<MovimientosDetalle>{

	public void delete(MovimientosDetalle entity);

	public void update(MovimientosDetalle entity);

	public MovimientosDetalle findById(Integer id);

	public List<MovimientosDetalle> findByProperty(String propertyName, Object value);

	public List<MovimientosDetalle> findAll();
	
	public List<MovimientosDetalle> findAllActivos();
	
	public List<MovimientosDetalle> findDetallesById(long idAlmacenMovimiento);
	
}
