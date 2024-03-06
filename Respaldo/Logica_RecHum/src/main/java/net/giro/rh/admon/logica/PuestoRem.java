package net.giro.rh.admon.logica;

import java.util.List; 

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.Puesto;

@Remote
public interface PuestoRem {


	public Long save(Puesto entity) throws ExcepConstraint;
	//public Long save(PuestoExt entityExt) throws ExcepConstraint;
	
	public void delete(Puesto entity) throws ExcepConstraint;
	//public void delete(PuestoExt entityExt) throws ExcepConstraint;

	public Puesto update(Puesto entity) throws ExcepConstraint;
	//public Puesto update(PuestoExt entity) throws ExcepConstraint;

	public Puesto findById(Long id);

	public List<Puesto> findByProperty(String propertyName, Object value);
	
	public List<Puesto> findAll();
	public List<Puesto> findAllActivos();
	
	public List<Puesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	//public List<PuestoExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
	
}
