package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.EmpleadoNomina;

@Remote
public interface EmpleadoNominaDAO extends DAO<EmpleadoNomina> {
	public void orderBy(String orderBy);

	public long save(EmpleadoNomina entity, long codigoEmpresa) throws Exception;

	public List<EmpleadoNomina> saveOrUpdateList(List<EmpleadoNomina> entities, long codigoEmpresa) throws Exception;

	public List<EmpleadoNomina> deleteAll(List<EmpleadoNomina> entities) throws Exception;
	
	public List<EmpleadoNomina> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<EmpleadoNomina> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<EmpleadoNomina> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<EmpleadoNomina> findByProperties(HashMap<String, Object> params, long idEmpresa, int limite) throws Exception;

	public List<EmpleadoNomina> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
	
	public List<EmpleadoNomina> findByDates(Date fechaDesde, Date fechaHasta, long idEmpresa) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaDAO