package net.giro.rh.admon.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoContratoExt;

@Remote
public interface EmpleadoContratoRem {
	
	public Long save(EmpleadoContrato entity) throws ExcepConstraint;
	public Long save(EmpleadoContratoExt entityExt) throws ExcepConstraint;
	
	public void delete(EmpleadoContrato entity) throws ExcepConstraint;
	public void delete(EmpleadoContratoExt entityExt) throws ExcepConstraint;

	public EmpleadoContrato update(EmpleadoContrato entity) throws ExcepConstraint;
	public EmpleadoContrato update(EmpleadoContratoExt entity) throws ExcepConstraint;

	public EmpleadoContrato findById(Long id);
	public EmpleadoContratoExt findByIdExt(Long id);

	public List<EmpleadoContrato> findByProperty(String propertyName, Object value);

	public List<EmpleadoContrato> findAll();
	
	public List<EmpleadoContrato> findAllByIdEmpleado(long idEmpleado);
	
	public List<EmpleadoContrato> contratoValido(long idEmpleado) throws Exception;
}
