package net.giro.cxp.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.cxp.beans.GastosImpuesto;
import net.giro.cxp.beans.GastosImpuestoExt;

@Remote
public interface GastosImpuestoRem {
	public Long save(GastosImpuesto entity) throws ExcepConstraint;
	
	public Long save(GastosImpuestoExt entityExt) throws ExcepConstraint;
	
	public void delete(GastosImpuesto entity) throws ExcepConstraint;
	
	public void delete(GastosImpuestoExt entityExt) throws ExcepConstraint;

	public GastosImpuesto update(GastosImpuesto entity) throws ExcepConstraint;
	
	public GastosImpuesto update(GastosImpuestoExt entity) throws ExcepConstraint;

	public GastosImpuesto findById(Long id);

	public List<GastosImpuesto> findByProperty(String propertyName, Object value);
	
	public List<GastosImpuesto> findAll();
	
	public List<GastosImpuesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	public List<GastosImpuestoExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
}
