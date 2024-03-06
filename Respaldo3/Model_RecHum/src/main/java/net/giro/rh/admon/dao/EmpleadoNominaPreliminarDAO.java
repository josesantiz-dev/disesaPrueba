package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.EmpleadoNominaPreliminar;

@Remote
public interface EmpleadoNominaPreliminarDAO extends DAO<EmpleadoNominaPreliminar> {
	public long save(EmpleadoNominaPreliminar entity, long codigoEmpresa) throws Exception;

	public List<EmpleadoNominaPreliminar> saveOrUpdateList(List<EmpleadoNominaPreliminar> entities, long codigoEmpresa) throws Exception;

	public List<EmpleadoNominaPreliminar> deleteAll(List<EmpleadoNominaPreliminar> entities) throws Exception;
	
	public List<EmpleadoNominaPreliminar> findByDates(Date fechaDesde, Date fechaHasta, long idEmpresa) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaPreliminarDAO