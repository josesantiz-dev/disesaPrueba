package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface FacturaDetalleRem {
	public void showSystemOuts(boolean value);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(FacturaDetalle entity) throws Exception;
	
	public List<FacturaDetalle> saveOrUpdateList(List<FacturaDetalle> entities) throws Exception;

	public void update(FacturaDetalle entity) throws Exception;
	
	public void delete(Long idFacturaDetalle) throws Exception;

	public FacturaDetalle findById(Long idFacturaDetalle) throws Exception;

	public List<FacturaDetalle> findAll(Long idFactura) throws Exception;
	
	//public List<FacturaDetalle> findByProperty(String propertyName, Object value);
	
	//public List<FacturaDetalle> findByPropertyPojoCompleto(String propertyName, long value);

	// ------------------------------------------------------------------------------------------------------
	// CONVERTIDOR
	// ------------------------------------------------------------------------------------------------------

	public FacturaDetalleExt convertir(FacturaDetalle entity) throws Exception;
	
	public FacturaDetalle convertir(FacturaDetalleExt extendido) throws Exception;

	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------

	public Long save(FacturaDetalleExt extendido) throws Exception;

	public List<FacturaDetalleExt> saveOrUpdateListExt(List<FacturaDetalleExt> extendidos) throws Exception;

	public void update(FacturaDetalleExt extendido) throws Exception;

	public FacturaDetalleExt findByIdExt(Long idFacturaDetalle) throws Exception;

	public List<FacturaDetalleExt> findAllExt(Long idFactura) throws Exception;
}
