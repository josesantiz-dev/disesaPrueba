package net.giro.inventarios.logica;
import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface MovimientosDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(MovimientosDetalle entity) throws Exception;
	
	public List<MovimientosDetalle> saveOrUpdateList(List<MovimientosDetalle> entities) throws Exception;

	public void update(MovimientosDetalle entity) throws Exception;
	
	public void delete(MovimientosDetalle entity) throws Exception;

	public MovimientosDetalle findById(Long id);

	public List<MovimientosDetalle> findAll();
	
	public List<MovimientosDetalle> findAllActivos();

	public List<MovimientosDetalle> findDetallesById(long idAlmacenMovimiento);

	public List<MovimientosDetalle> findByProperty(String propertyName, Object value);
	
	public List<MovimientosDetalleExt> findDetallesExtByIdOrdenCompra(long idOrdenCompra);
	
	public MovimientosDetalle convertir(MovimientosDetalleExt target);
	
	public MovimientosDetalleExt convertir(MovimientosDetalle target);
	
	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------

	public Long save(MovimientosDetalleExt entityExt) throws Exception;
	
	public void update(MovimientosDetalleExt entity) throws Exception;
	
	public List<MovimientosDetalleExt> saveOrUpdateListExt(List<MovimientosDetalleExt> entities) throws Exception;
	
	public void delete(MovimientosDetalleExt entityExt) throws Exception;
	
	public MovimientosDetalleExt findByIdExt(Long id);
	
	public List<MovimientosDetalleExt> findAllExt();
	
	public List<MovimientosDetalleExt> findExtAllActivos();
	
	public List<MovimientosDetalleExt> findExtByProperty(String propertyName, Object value);
	
	public List<MovimientosDetalleExt> findDetallesExtById(long idAlmacenMovimiento);
}
