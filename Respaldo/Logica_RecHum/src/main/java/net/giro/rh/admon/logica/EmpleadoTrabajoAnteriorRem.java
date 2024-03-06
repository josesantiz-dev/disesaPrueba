package net.giro.rh.admon.logica;

import java.util.List; 

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoTrabajoAnterior;

@Remote
public interface EmpleadoTrabajoAnteriorRem {
	
	public Long save(EmpleadoTrabajoAnterior entity) throws ExcepConstraint;
	//public Long save(EmpleadoTrabajoAnteriorExt entityExt) throws ExcepConstraint;
	
	public void delete(EmpleadoTrabajoAnterior entity) throws ExcepConstraint;
	//public void delete(EmpleadoTrabajoAnteriorExt entityExt) throws ExcepConstraint;

	public EmpleadoTrabajoAnterior update(EmpleadoTrabajoAnterior entity) throws ExcepConstraint;
	//public EmpleadoTrabajoAnterior update(EmpleadoTrabajoAnteriorExt entity) throws ExcepConstraint;

	public EmpleadoTrabajoAnterior findById(Long id);

	public List<EmpleadoTrabajoAnterior> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoTrabajoAnterior> findAll();
	
	public List<EmpleadoTrabajoAnterior> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	//public List<EmpleadoTrabajoAnteriorExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
}
