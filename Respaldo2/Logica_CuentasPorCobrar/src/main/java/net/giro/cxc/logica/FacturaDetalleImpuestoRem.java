package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.beans.FacturaDetalleExt;
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
	
	public void delete(Long entityId) throws Exception;
	
	public void delete(FacturaDetalleImpuesto entity) throws Exception;

	public FacturaDetalleImpuesto findById(Long id);
	
	public List<FacturaDetalleImpuesto> findAll();
	
	public List<FacturaDetalleImpuesto> findAll(String orderBy) throws Exception;

	public List<FacturaDetalleImpuesto> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	public List<FacturaDetalleImpuesto> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception;

	public List<FacturaDetalleImpuesto> findByFacturaDetalle(Long idFacturaDetalle) throws Exception;

	public List<FacturaDetalleImpuesto> findByFacturaDetalle(FacturaDetalle idFacturaDetalle) throws Exception;

	public List<FacturaDetalleImpuesto> findByFacturaDetalle(FacturaDetalleExt idFacturaDetalle) throws Exception;

	public FacturaDetalleImpuesto convertir(FacturaDetalleImpuestoExt target);
	
	public FacturaDetalleImpuestoExt convertir(FacturaDetalleImpuesto target);
	
	// ----------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ----------------------------------------------------------------------------------------------------------
	
	public Long save(FacturaDetalleImpuestoExt entity) throws Exception;

	public void update(FacturaDetalleImpuestoExt entity) throws Exception;
	
	public void delete(FacturaDetalleImpuestoExt entity) throws Exception;

	public FacturaDetalleImpuestoExt findExtById(Long id) throws Exception;
	
	public List<FacturaDetalleImpuestoExt> findExtAll() throws Exception;
	
	public List<FacturaDetalleImpuestoExt> findExtAll(String orderBy) throws Exception;

	public List<FacturaDetalleImpuestoExt> findExtByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception;
	
	public List<FacturaDetalleImpuestoExt> findExtLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception;

	public List<FacturaDetalleImpuestoExt> findExtByFacturaDetalle(Long idFacturaDetalle) throws Exception;

	public List<FacturaDetalleImpuestoExt> findExtByFacturaDetalle(FacturaDetalle idFacturaDetalle) throws Exception;

	public List<FacturaDetalleImpuestoExt> findExtByFacturaDetalle(FacturaDetalleExt idFacturaDetalle) throws Exception;
}
