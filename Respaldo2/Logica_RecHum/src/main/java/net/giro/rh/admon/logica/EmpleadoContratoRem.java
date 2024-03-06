package net.giro.rh.admon.logica;

import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.beans.EmpleadoContratoExt;

@Remote
public interface EmpleadoContratoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(EmpleadoContrato entity) throws Exception;
	
	public List<EmpleadoContrato> saveOrUpdateList(List<EmpleadoContrato> entities) throws Exception;

	public void update(EmpleadoContrato entity) throws Exception;
	
	public void cancelar(long idContrato, long idUsuario) throws Exception;
	
	public void cancelar(EmpleadoContrato entity, long idUsuario) throws Exception;
	
	public void delete(EmpleadoContrato entity) throws Exception;

	public EmpleadoContrato findById(Long id);

	public List<EmpleadoContrato> findAll();
	
	public List<EmpleadoContrato> findAllByIdEmpleado(long idEmpleado);

	public List<EmpleadoContrato> findByProperty(String propertyName, Object value);
	
	public List<EmpleadoContrato> contratoValido(long idEmpleado) throws Exception;
	
	public void cancelarContratosPrevios(long idEmpleado, long idContratoActual, long idUsuario) throws Exception;
	
	public EmpleadoContrato findContratoByEmpleado(long idEmpleado) throws Exception;
	
	public EmpleadoContrato convertir(EmpleadoContratoExt entityExt) throws Exception;
	
	public EmpleadoContratoExt convertir(EmpleadoContrato entity) throws Exception;

	// ---------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------------

	public Long save(EmpleadoContratoExt entityExt) throws Exception;
	
	public void update(EmpleadoContratoExt entity) throws Exception;
	
	public void cancelar(EmpleadoContratoExt entityExt, long idUsuario) throws Exception;
	
	public void delete(EmpleadoContratoExt entityExt) throws Exception;
	
	public EmpleadoContratoExt findByIdExt(Long id);
}
