package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.beans.FacturaExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface FacturaDetalleRem {
	public void showSystemOuts(boolean value);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(FacturaDetalle entity) throws Exception;
	
	public List<FacturaDetalle> saveOrUpdateList(List<FacturaDetalle> entities) throws Exception;

	public void update(FacturaDetalle entity) throws Exception;
	
	public void delete(FacturaDetalle entity) throws Exception;

	public FacturaDetalle findById(Long id);

	public List<FacturaDetalle> findByProperty(String propertyName, Object value);
	
	public List<FacturaDetalle> findAll();
	
	public List<FacturaDetalle> findByPropertyPojoCompleto(String propertyName,  long value);
	
	public List<FacturaDetalle> findByFactura(Long idFactura) throws Exception;

	public List<FacturaDetalle> findByFactura(Factura idFactura) throws Exception;

	public List<FacturaDetalle> findByFactura(FacturaExt idFactura) throws Exception;

	public FacturaDetalleExt convertir(FacturaDetalle entity) throws Exception;
	
	public FacturaDetalle convertir(FacturaDetalleExt entity) throws Exception;

	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ------------------------------------------------------------------------------------------------------

	public Long save(FacturaDetalleExt entityExt) throws Exception;

	public void update(FacturaDetalleExt entity) throws Exception;

	public void delete(FacturaDetalleExt entityExt) throws Exception;

	public List<FacturaDetalleExt> findByPropertyPojoCompletoExt(String propertyName, long value) throws Exception;

	public List<FacturaDetalleExt> findExtByFactura(Long idFactura) throws Exception;

	public List<FacturaDetalleExt> findExtByFactura(Factura idFactura) throws Exception;

	public List<FacturaDetalleExt> findExtByFactura(FacturaExt idFactura) throws Exception;
}
