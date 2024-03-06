package net.giro.rh.admon.logica;

import java.util.List; 

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.rh.admon.beans.EmpleadoReferencia;

@Remote
public interface EmpleadoReferenciaRem {
	
	public Long save(EmpleadoReferencia entity) throws ExcepConstraint;
	//public Long save(EmpleadoReferenciaExt entityExt) throws ExcepConstraint;
	
	public void delete(EmpleadoReferencia entity) throws ExcepConstraint;
	//public void delete(EmpleadoReferenciaExt entityExt) throws ExcepConstraint;

	public EmpleadoReferencia update(EmpleadoReferencia entity) throws ExcepConstraint;
	//public EmpleadoReferencia update(EmpleadoReferenciaExt entity) throws ExcepConstraint;

	public EmpleadoReferencia findById(Long id);

	public List<EmpleadoReferencia> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoReferencia> findAll();
	
	public List<EmpleadoReferencia> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	//public List<EmpleadoReferenciaExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
}
