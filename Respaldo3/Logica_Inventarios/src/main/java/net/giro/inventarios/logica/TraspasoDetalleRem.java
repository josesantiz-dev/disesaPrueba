package net.giro.inventarios.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface TraspasoDetalleRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(TraspasoDetalle entity) throws Exception;

	public List<TraspasoDetalle> saveOrUpdateList(List<TraspasoDetalle> entities) throws Exception;

	public void update(TraspasoDetalle entity) throws Exception;

	public void delete(long idTraspasoDetalle) throws Exception;

	public void deleteAll(List<TraspasoDetalle> entities) throws Exception;
	
	public TraspasoDetalle findById(long idTraspasoDetalle);

	public List<TraspasoDetalle> findAll(long idAlmacenTraspaso);
	
	public List<TraspasoDetalle> findByProperty(String propertyName, Object value, long idAlmacenTraspaso);

	public List<TraspasoDetalle> findLikeProperty(String propertyName, Object value, long idAlmacenTraspaso);
	
	public TraspasoDetalle convertir(TraspasoDetalleExt entity);
	
	public TraspasoDetalleExt convertir(TraspasoDetalle entity);
	
	public List<TraspasoDetalle> convertirLista(List<TraspasoDetalleExt> extendidos);
	
	public List<TraspasoDetalleExt> extenderLista(List<TraspasoDetalle> entities);

	// -------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------
	
	public Long save(TraspasoDetalleExt extendido) throws Exception;

	public List<TraspasoDetalleExt> saveOrUpdateListExt(List<TraspasoDetalleExt> extendidos) throws Exception;
	
	public void update(TraspasoDetalleExt extendido) throws Exception;
	
	public void deleteAllExt(List<TraspasoDetalleExt> extendidos) throws Exception;
	
	public TraspasoDetalleExt findByIdExt(long idTraspasoDetalle);
	
	public List<TraspasoDetalleExt> findExtAll(long idAlmacenTraspaso);
	
	public List<TraspasoDetalleExt> findExtByProperty(String propertyName, Object value, long idAlmacenTraspaso);
	
	public List<TraspasoDetalleExt> findExtLikeProperty(String propertyName, Object value, long idAlmacenTraspaso);
}
