package net.giro.rh.admon.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.rh.admon.beans.EmpleadoInfonavit;

@Remote
public interface EmpleadoInfonavitDAO extends DAO<EmpleadoInfonavit> {
	public void orderBy(String orderBy);

	public long save(EmpleadoInfonavit entity, long codigoEmpresa) throws Exception;
	
	public List<EmpleadoInfonavit> saveOrUpdateList(List<EmpleadoInfonavit> entities, long codigoEmpresa) throws Exception;

	public List<EmpleadoInfonavit> findAll(long idEmpleado) throws Exception;
	
	public List<EmpleadoInfonavit> findByProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;

	public List<EmpleadoInfonavit> findLikeProperty(String propertyName, final Object value, long idEmpresa, int limite) throws Exception;
	
	public List<EmpleadoInfonavit> comprobarPorFecha(long idEmpleado, Date fecha, long idEmpresa) throws Exception;
	
	public List<EmpleadoInfonavit> comprobarPorFecha(long idEmpleado, Date fechaDesde, Date fechaHasta, long idEmpresa) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoInfonavitDAO