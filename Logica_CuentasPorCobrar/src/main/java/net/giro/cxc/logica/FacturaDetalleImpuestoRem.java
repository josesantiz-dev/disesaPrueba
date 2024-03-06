package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.FacturaDetalleImpuesto;
import net.giro.cxc.beans.FacturaDetalleImpuestoExt;
import net.giro.plataforma.InfoSesion;

@Remote
public interface FacturaDetalleImpuestoRem {
	public void showSystemOuts(boolean value);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(FacturaDetalleImpuesto entity) throws Exception;
	
	public List<FacturaDetalleImpuesto> saveOrUpdateList(List<FacturaDetalleImpuesto> entities) throws Exception;

	public void update(FacturaDetalleImpuesto entity) throws Exception;
	
	public void delete(Long idFacturaDetalleImpuesto) throws Exception;

	public FacturaDetalleImpuesto findById(Long idFacturaDetalleImpuesto);
	
	public List<FacturaDetalleImpuesto> findAll(Long idFacturaDetalle, String orderBy) throws Exception;

	public List<FacturaDetalleImpuesto> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	public List<FacturaDetalleImpuesto> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception;

	// ------------------------------------------------------------------------------------------------------
	// CONVERTIDOR
	// ------------------------------------------------------------------------------------------------------

	public FacturaDetalleImpuesto convertir(FacturaDetalleImpuestoExt extendido);
	
	public FacturaDetalleImpuestoExt convertir(FacturaDetalleImpuesto target) throws Exception;
	
	// ----------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------------------------------------------------
	
	public Long save(FacturaDetalleImpuestoExt extendido) throws Exception;

	public List<FacturaDetalleImpuestoExt> saveOrUpdateListExt(List<FacturaDetalleImpuestoExt> extendidos) throws Exception;

	public void update(FacturaDetalleImpuestoExt extendido) throws Exception;
	
	public FacturaDetalleImpuestoExt findExtById(Long idFacturaDetalleImpuesto) throws Exception;
	
	public List<FacturaDetalleImpuestoExt> findAllExt(Long idFacturaDetalleImpuesto, String orderBy) throws Exception;
}
