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

	public long save(EmpleadoDescuento entity, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoDescuento> saveOrUpdateList(List<EmpleadoDescuento> entities, long codigoEmpresa) throws Exception;

	public List<EmpleadoDescuento> findAll(long idEmpleado) throws Exception;

	public List<EmpleadoDescuento> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<EmpleadoDescuento> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<EmpleadoDescuento> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception;

	public List<EmpleadoDescuento> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;

	public List<EmpleadoDescuento> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception;
	
	public List<EmpleadoDescuento> comprobarDescuentosPorFecha(long idEmpleado, Date fecha, long idEmpresa) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 25/05/2016 | Javier Tirado	| Creacion de EmpleadoDescuentoDAO