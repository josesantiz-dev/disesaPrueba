package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoIncapacidad;

@Remote
public interface EmpleadoIncapacidadRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(EmpleadoIncapacidad entity) throws Exception;
	
	public List<EmpleadoIncapacidad> saveOrUpdateList(List<EmpleadoIncapacidad> entities) throws Exception;
	
	public void update(EmpleadoIncapacidad entity) throws Exception;

	public void cancelar(EmpleadoIncapacidad entity) throws Exception;

	public void delete(long idEmpleadoIncapacidad) throws Exception;

	public EmpleadoIncapacidad findById(long idEmpleadoIncapacidad);

	public List<EmpleadoIncapacidad> findAll(long idEmpleado) throws Exception;

	public List<EmpleadoIncapacidad> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoIncapacidad> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoIncapacidad> comprobarPorFecha(long idEmpleado, Date fecha) throws Exception;

	public List<EmpleadoIncapacidad> comprobarPorFecha(long idEmpleado, Date fechaDesde, Date fechaHasta) throws Exception;

	public boolean comprobarRegistro(EmpleadoIncapacidad entity) throws Exception;
	
	public boolean verificarIncapacidad(long idEmpleado, Date fecha) throws Exception;
	
	public List<EmpleadoIncapacidad> comprobarParaCancelar(Date fecha) throws Exception; 
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *   VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  2.2	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoIncapacidadRem
*/