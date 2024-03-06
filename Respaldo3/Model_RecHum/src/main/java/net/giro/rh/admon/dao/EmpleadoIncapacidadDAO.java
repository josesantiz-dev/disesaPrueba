package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.EmpleadoIncapacidad;

@Remote
public interface EmpleadoIncapacidadDAO extends DAO<EmpleadoIncapacidad> {
	public long save(EmpleadoIncapacidad entity, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoIncapacidad> saveOrUpdateList(List<EmpleadoIncapacidad> entities, long codigoEmpresa) throws Exception;

	public List<EmpleadoIncapacidad> findAll(long idEmpleado) throws Exception;
	
	public List<EmpleadoIncapacidad> findAllByDate(Date fecha, long idEmpresa) throws Exception;
	
	public List<EmpleadoIncapacidad> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<EmpleadoIncapacidad> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<EmpleadoIncapacidad> comprobarPorFecha(long idEmpleado, Date fecha, long idEmpresa) throws Exception;
	
	public List<EmpleadoIncapacidad> comprobarPorFecha(long idEmpleado, Date fechaDesde, Date fechaHasta, long idEmpresa) throws Exception;
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 *	  2.1	| 23/11/2018 | Javier Tirado	| Creacion de EmpleadoIncapacidadDAO
*/