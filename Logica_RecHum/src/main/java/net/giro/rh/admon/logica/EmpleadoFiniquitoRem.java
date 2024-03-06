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

	public List<EmpleadoFiniquito> saveOrUpdateList(List<EmpleadoFiniquito> entities) throws Exception;

	public void update(EmpleadoFiniquito entity) throws Exception;

	public void cancelar(EmpleadoFiniquito entity) throws Exception;

	public void delete(EmpleadoFiniquito entity) throws Exception;
	
	public EmpleadoFiniquito finiquitar(EmpleadoFiniquito entity) throws Exception;

	public EmpleadoFiniquito findById(Long id);

	public EmpleadoFiniquito findByIdEmpleado(long idEmpleado);

	public EmpleadoFiniquito findByIdEmpleado(long idEmpleado, long idContrato);

	public EmpleadoFiniquito findByIdEmpleado(long idEmpleado, boolean incluyeAprobados, boolean incluyeCancelado);

	public EmpleadoFiniquito findByIdEmpleado(long idEmpleado, long idContrato, boolean incluyeAprobados, boolean incluyeCancelado);

	public List<EmpleadoFiniquito> findAll();

	public List<EmpleadoFiniquito> findFiniquitosByEmpleado(long idEmpleado);

	public List<EmpleadoFiniquito> findFiniquitosByEmpleado(long idEmpleado, boolean incluyeAprobados, boolean incluyeCancelado);

	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado);

	public List<EmpleadoFiniquito> findByProperty(String propertyName, Object value);
	
	public boolean comprobarFiniquitos(long idEmpleado);

	// ---------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// ---------------------------------------------------------------------------------------

	public Long save(EmpleadoFiniquitoExt entity) throws Exception; 
	
	public void update(EmpleadoFiniquitoExt entity) throws Exception;
	
	public void delete(EmpleadoFiniquitoExt entity) throws Exception;
	
	public EmpleadoFiniquitoExt finiquitarExt(EmpleadoFiniquitoExt entity) throws Exception;

	public EmpleadoFiniquitoExt findByIdExt(Long id);

	public EmpleadoFiniquitoExt findByIdEmpleadoExt(long idEmpleado);
	
	public List<EmpleadoFiniquitoExt> findAllExt();

	public List<EmpleadoFiniquitoExt> findFiniquitosByEmpleadoExt(long idEmpleado);
	
	public List<EmpleadoFiniquitoExt> findByEmpleadoExt(String nombreEmpleado);
}
