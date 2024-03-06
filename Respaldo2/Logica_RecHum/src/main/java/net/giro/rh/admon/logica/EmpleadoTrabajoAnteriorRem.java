package net.giro.rh.admon.logica;

import java.util.List; 

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoTrabajoAnterior;
import net.giro.rh.admon.beans.EmpleadoTrabajoAnteriorExt;

@Remote
public interface EmpleadoTrabajoAnteriorRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(EmpleadoTrabajoAnterior entity) throws Exception;

	public void update(EmpleadoTrabajoAnterior entity) throws Exception;
	
	public void delete(EmpleadoTrabajoAnterior entity) throws Exception;

	public EmpleadoTrabajoAnterior findById(Long id);
	
	public List<EmpleadoTrabajoAnterior> findAll();

	public List<EmpleadoTrabajoAnterior> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoTrabajoAnterior> findByPropertyPojoCompleto(String propertyName, String tipo, long value);

	// -----------------------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// -----------------------------------------------------------------------------------------------------------------------

	public Long save(EmpleadoTrabajoAnteriorExt entityExt) throws Exception;
	
	public void update(EmpleadoTrabajoAnteriorExt entityExt) throws Exception;
	
	public void delete(EmpleadoTrabajoAnteriorExt entityExt) throws Exception;
	
	public List<EmpleadoTrabajoAnteriorExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);
}
