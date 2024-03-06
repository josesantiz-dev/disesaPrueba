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

	public void delete(long idMovimientosDetalle) throws Exception;

	public void delete(MovimientosDetalle entity) throws Exception;

	public MovimientosDetalle findById(Long idMovimientosDetalle) throws Exception;

	public List<MovimientosDetalle> findAll(long idAlmacenMovimiento) throws Exception;

	public List<MovimientosDetalle> findLikeProperty(String propertyName, Object value, long idAlmacenMovimiento) throws Exception;

	public List<MovimientosDetalle> findByProperty(String propertyName, Object value, long idAlmacenMovimiento) throws Exception;

	// --------------------------------------------------------------------------------------------
	// CONVERTIDOR 
	// --------------------------------------------------------------------------------------------
	
	public MovimientosDetalle convertir(MovimientosDetalleExt target) throws Exception;
	
	public MovimientosDetalleExt convertir(MovimientosDetalle target) throws Exception;
	
	public List<MovimientosDetalle> convertirLista(List<MovimientosDetalleExt> extendidos) throws Exception;
	
	public List<MovimientosDetalleExt> extenderLista(List<MovimientosDetalle> entities) throws Exception;
	
	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// --------------------------------------------------------------------------------------------
	
	public List<MovimientosDetalleExt> saveOrUpdateListExt(List<MovimientosDetalleExt> entities) throws Exception;

	public List<MovimientosDetalleExt> findAllExt(long idAlmacenMovimiento) throws Exception;
}
