package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.EmpleadoDescuento;

@Remote
public interface EmpleadoDescuentoDAO extends DAO<EmpleadoDescuento> {
	public void orderBy(String orderBy);
	
	public List<EmpleadoDescuento> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoDescuento> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<EmpleadoDescuento> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<EmpleadoDescuento> findByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<EmpleadoDescuento> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public List<EmpleadoDescuento> comprobarDescuentoPorFechas(Long idEmpleado, Date fecha) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 25/05/2016 | Javier Tirado	| Creacion de EmpleadoDescuentoDAO