package net.giro.rh.admon.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoFiniquito;
import net.giro.rh.admon.beans.EmpleadoFiniquitoExt;

@Remote
public interface EmpleadoFiniquitoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(EmpleadoFiniquito entity) throws Exception; 

	public void update(EmpleadoFiniquito entity) throws Exception;

	public void delete(EmpleadoFiniquito entity) throws Exception;

	public EmpleadoFiniquito findById(Long id);

	public List<EmpleadoFiniquito> findAll();

	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado);

	public List<EmpleadoFiniquito> findByProperty(String propertyName, Object value);

	// ---------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------------

	public Long save(EmpleadoFiniquitoExt entity) throws Exception; 
	
	public void update(EmpleadoFiniquitoExt entity) throws Exception;
	
	public void delete(EmpleadoFiniquitoExt entity) throws Exception;
	
	public List<EmpleadoFiniquitoExt> findAllExt();
	
	public List<EmpleadoFiniquitoExt> findByEmpleadoExt(String nombreEmpleado);
}
