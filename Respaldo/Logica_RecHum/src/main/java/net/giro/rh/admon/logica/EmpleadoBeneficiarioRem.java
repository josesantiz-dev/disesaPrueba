package net.giro.rh.admon.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoBeneficiario;
import net.giro.rh.admon.beans.EmpleadoBeneficiarioExt;

@Remote
public interface EmpleadoBeneficiarioRem {

	public Long save(EmpleadoBeneficiario entity) throws ExcepConstraint;
	public Long save(EmpleadoBeneficiarioExt entityExt) throws ExcepConstraint;
	
	public void delete(EmpleadoBeneficiario entity) throws ExcepConstraint;
	public void delete(EmpleadoBeneficiarioExt entityExt) throws ExcepConstraint;

	public EmpleadoBeneficiario update(EmpleadoBeneficiario entity) throws ExcepConstraint;
	public EmpleadoBeneficiario update(EmpleadoBeneficiarioExt entity) throws ExcepConstraint;

	public EmpleadoBeneficiario findById(Long id);

	public List<EmpleadoBeneficiario> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoBeneficiario> findAll();
	public List<EmpleadoBeneficiarioExt> findByIdEmpleado(long idEmpleado);
	
	public List<EmpleadoBeneficiario> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	//public List<EmpleadoBeneficiarioExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
	
}
