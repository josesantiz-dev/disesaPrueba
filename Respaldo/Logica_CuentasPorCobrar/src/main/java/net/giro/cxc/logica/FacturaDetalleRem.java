package net.giro.cxc.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.beans.FacturaDetalleExt;

@Remote
public interface FacturaDetalleRem {
	public void showSystemOuts(boolean value);
		
	public Long save(FacturaDetalle entity) throws ExcepConstraint;
	public Long save(FacturaDetalleExt entityExt) throws ExcepConstraint;
	
	public void delete(FacturaDetalle entity) throws ExcepConstraint;
	public void delete(FacturaDetalleExt entityExt) throws ExcepConstraint;

	public FacturaDetalle update(FacturaDetalle entity) throws ExcepConstraint;
	public FacturaDetalle update(FacturaDetalleExt entity) throws ExcepConstraint;

	public FacturaDetalle findById(Long id);

	public List<FacturaDetalle> findByProperty(String propertyName, Object value);
	
	public List<FacturaDetalle> findAll();
	
	public List<FacturaDetalle> findByPropertyPojoCompleto(String propertyName,  long value);
	
	public List<FacturaDetalleExt> findByPropertyPojoCompletoExt(String propertyName, long value) throws Exception;
	
}
