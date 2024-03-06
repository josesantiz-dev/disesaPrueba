package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
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
	
	public Respuesta cancelar(long idContrato) throws Exception;
	
	public String validarCancelacion(long idContrato) throws Exception;

	public void delete(long idContrato) throws Exception;

	public void delete(EmpleadoContrato entity) throws Exception;

	public EmpleadoContrato findById(long idContrato) throws Exception;

	public List<EmpleadoContrato> findAll(long idEmpleado) throws Exception;

	public List<EmpleadoContrato> findAll(long idEmpleado, String orderBy, boolean incluyeContratosCancelados, boolean incluyeEmpleadosSistema) throws Exception;
	
	public List<EmpleadoContrato> findByProperty(String propertyName, Object value) throws Exception;
	
	public List<EmpleadoContrato> findByProperty(String propertyName, Object value, boolean incluyeContratosCancelados, boolean incluyeEmpleadosSistema) throws Exception;
	
	public List<EmpleadoContrato> contratoValido(long idEmpleado) throws Exception;
	
	public void cancelarContratosPrevios(long idEmpleado, long idContratoActual, long idUsuario) throws Exception;
	
	/**
	 * Obtiene el Contrato vigente del Empleado especificado
	 * @param idEmpleado
	 * @return
	 * @throws Exception
	 */
	public EmpleadoContrato findContrato(long idEmpleado) throws Exception;

	/**
	 * Obtiene el Contrato vigente en las fecha indicado. Si no encuentra, obtiene el ultimo Contrato cancelado o vigente registrado
	 * @param idEmpleado
	 * @param fechaDesde Fecha inicial de validez del Contrato
	 * @param fechaHasta Fecha limite de validez del Contrato
	 * @return
	 * @throws Exception
	 */
	public EmpleadoContrato findContrato(long idEmpleado, Date fechaDesde, Date fechaHasta) throws Exception;
	
	public EmpleadoContrato convertir(EmpleadoContratoExt entityExt) throws Exception;
	
	public EmpleadoContratoExt convertir(EmpleadoContrato entity) throws Exception;

	// ---------------------------------------------------------------------------------------
	// EXTENDIDOS
	// ---------------------------------------------------------------------------------------

	public Long save(EmpleadoContratoExt entityExt) throws Exception;
	
	public void update(EmpleadoContratoExt entity) throws Exception;
	
	public void cancelar(EmpleadoContratoExt entityExt, long idUsuario) throws Exception;
	
	public void delete(EmpleadoContratoExt entityExt) throws Exception;
	
	public EmpleadoContratoExt findByIdExt(long id) throws Exception;
}
