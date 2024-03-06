package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.EmpleadoNominaEstatus;

@Remote
public interface EmpleadoNominaEstatusDAO extends DAO<EmpleadoNominaEstatus> {
	public long save(EmpleadoNominaEstatus entity, long codigoEmpresa) throws Exception;

	public long save(Date fechaDesde, Date fechaHasta, boolean nominaPreliminar, long idUsuario, long idEmpresa, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoNominaEstatus> saveOrUpdateList(List<EmpleadoNominaEstatus> entities, long codigoEmpresa) throws Exception;

	public List<EmpleadoNominaEstatus> deleteAll(List<EmpleadoNominaEstatus> entities) throws Exception;
	
	public List<EmpleadoNominaEstatus> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<EmpleadoNominaEstatus> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<EmpleadoNominaEstatus> comprobarPeticion(Date fechaDesde, Date fechaHasta, boolean nominaPreliminar, long idEmpresa) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaEstatusDAO