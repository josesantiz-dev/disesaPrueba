package net.giro.rh.admon.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.beans.EmpleadoFiniquitoExt;

@Remote
public interface EmpleadoFiniquitoRem {

	public Long save(EmpleadoFiniquito entity) throws ExcepConstraint; 
	public Long save(EmpleadoFiniquitoExt entity) throws ExcepConstraint; 

	public void delete(EmpleadoFiniquito entity) throws ExcepConstraint;
	public void delete(EmpleadoFiniquitoExt entity) throws ExcepConstraint;

	public EmpleadoFiniquito update(EmpleadoFiniquito entity) throws ExcepConstraint;
	public EmpleadoFiniquito update(EmpleadoFiniquitoExt entity) throws ExcepConstraint;

	public EmpleadoFiniquito findById(Long id);

	public List<EmpleadoFiniquito> findByProperty(String propertyName, Object value);

	public List<EmpleadoFiniquito> findAll();
	public List<EmpleadoFiniquitoExt> findAllExt();

	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado);
	public List<EmpleadoFiniquitoExt> findByEmpleadoExt(String nombreEmpleado);
}
