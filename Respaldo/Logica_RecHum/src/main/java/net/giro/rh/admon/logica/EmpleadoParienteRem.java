package net.giro.rh.admon.logica;
import java.util.List; 

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoPariente;
import net.giro.rh.admon.beans.EmpleadoParienteExt;

@Remote
public interface EmpleadoParienteRem {
	
	public Long save(EmpleadoPariente entity) throws ExcepConstraint;
	public Long save(EmpleadoParienteExt entityExt) throws ExcepConstraint;
	
	public void delete(EmpleadoPariente entity) throws ExcepConstraint;
	public void delete(EmpleadoParienteExt entityExt) throws ExcepConstraint;

	public EmpleadoPariente update(EmpleadoPariente entity) throws ExcepConstraint;
	public EmpleadoPariente update(EmpleadoParienteExt entity) throws ExcepConstraint;

	public EmpleadoPariente findById(Long id);

	public List<EmpleadoPariente> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoPariente> findAll();
	

	public List<EmpleadoParienteExt> findByIdEmpleadoParentesco(long idEmpleado);
	
	
	
	public List<EmpleadoPariente> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	//public List<EmpleadoParienteExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
	
}
