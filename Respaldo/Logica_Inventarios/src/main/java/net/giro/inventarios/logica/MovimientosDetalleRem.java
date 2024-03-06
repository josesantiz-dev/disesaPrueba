package net.giro.inventarios.logica;
import java.util.List;
import javax.ejb.Remote;
import net.giro.comun.ExcepConstraint;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.MovimientosDetalleExt;

@Remote
public interface MovimientosDetalleRem {

	public Long save(MovimientosDetalle entity) throws ExcepConstraint;
	public Long save(MovimientosDetalleExt entityExt) throws ExcepConstraint;
	
	public void delete(MovimientosDetalle entity) throws ExcepConstraint;
	public void delete(MovimientosDetalleExt entityExt) throws ExcepConstraint;

	public MovimientosDetalle update(MovimientosDetalle entity) throws ExcepConstraint;
	public MovimientosDetalle update(MovimientosDetalleExt entity) throws ExcepConstraint;

	public MovimientosDetalle findById(Long id);
	public MovimientosDetalleExt findByIdExt(Long id);

	public List<MovimientosDetalle> findByProperty(String propertyName, Object value);
	public List<MovimientosDetalleExt> findExtByProperty(String propertyName, Object value);

	public List<MovimientosDetalle> findAll();
	public List<MovimientosDetalleExt> findAllExt();
	
	public List<MovimientosDetalle> findAllActivos();

	public List<MovimientosDetalle> findDetallesById(long idAlmacenMovimiento);
	
	public List<MovimientosDetalleExt> findDetallesExtById(long idAlmacenMovimiento);
	
	public List<MovimientosDetalleExt> findDetallesExtByIdOrdenCompra(long idOrdenCompra);
	
}
