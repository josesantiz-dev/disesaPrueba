package net.giro.rh.admon.logica;

import java.util.List; 

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoPariente;
import net.giro.rh.admon.beans.EmpleadoParienteExt;

@Remote
public interface EmpleadoParienteRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(EmpleadoPariente entity) throws Exception;
	
	public void update(EmpleadoPariente entity) throws Exception;
	
	public void delete(EmpleadoPariente entity) throws Exception;

	public EmpleadoPariente findById(Long id);
	
	public List<EmpleadoPariente> findAll();

	public List<EmpleadoPariente> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoPariente> findByPropertyPojoCompleto(String propertyName, String tipo, long value);

	//----------------------------------------------------------------------------------
	// EXTENDIDOS
	//----------------------------------------------------------------------------------

	public Long save(EmpleadoParienteExt entityExt) throws Exception;
	
	public void update(EmpleadoParienteExt entity) throws Exception;
	
	public void delete(EmpleadoParienteExt entityExt) throws Exception;
	
	public List<EmpleadoParienteExt> findByIdEmpleadoParentesco(long idEmpleado);
	
	public List<EmpleadoParienteExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
}
