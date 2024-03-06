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

	//public void estatus(Long estatus);
	
	public List<EmpleadoNomina> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoNomina> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<EmpleadoNomina> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<EmpleadoNomina> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<EmpleadoNomina> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<EmpleadoNomina> findByDates(Date fechaDesde, Date fechaHasta) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaDAO