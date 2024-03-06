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
	
	public void delete(TraspasoDetalle entity) throws Exception;
	
	public TraspasoDetalle findById(Long id);

	public List<TraspasoDetalle> findAll();
	
	public List<TraspasoDetalle> findAllActivos();

	public List<TraspasoDetalle> findByProperty(String propertyName, Object value);
	
	public List<TraspasoDetalle> findDetallesByIdTraspaso(long idAlmacenTraspaso);
	
	public TraspasoDetalle convertir(TraspasoDetalleExt entity);
	
	public TraspasoDetalleExt convertir(TraspasoDetalle entity);

	// -------------------------------------------------------------------------------
	// EXTENDIDOS
	// -------------------------------------------------------------------------------
	
	public Long save(TraspasoDetalleExt entityExt) throws Exception;
	
	public void update(TraspasoDetalleExt entity) throws Exception;
	
	public void delete(TraspasoDetalleExt entityExt) throws Exception;
	
	public TraspasoDetalleExt findByIdExt(Long id);
	
	public List<TraspasoDetalleExt> findAllExt();
	
	public List<TraspasoDetalleExt> findExtByProperty(String propertyName, Object value);
	
	public List<TraspasoDetalleExt> findExtDetallesByIdTraspaso(long idAlmacenTraspaso);
}
