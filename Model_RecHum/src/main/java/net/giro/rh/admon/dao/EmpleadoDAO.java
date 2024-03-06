package net.giro.rh.admon.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.Empleado;

@Remote
public interface EmpleadoDAO extends DAO<Empleado> {
	public long save(Empleado entity, long codigoEmpresa) throws Exception;
	
	public List<Empleado> saveOrUpdateList(List<Empleado> entities, long codigoEmpresa) throws Exception;
	
	public List<Empleado> findAll(boolean incluyeBajas, boolean soloSistema, long idEmpresa, String orderBy);
	
	public List<Empleado> findAll(List<Long> empleados, boolean incluyeBajas, boolean soloSistema, String orderBy);
	
	public List<Empleado> findLike(String value, boolean incluyeBajas, boolean soloSistema, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Empleado> findLikeProperty(String propertyName, Object value, boolean incluyeBajas, boolean soloSistema, long idEmpresa, String orderBy, int limite) throws Exception;

	public List<Empleado> findByProperty(String propertyName, Object value, boolean incluyeBajas, boolean soloSistema, long idEmpresa, String orderBy, int limite) throws Exception;
	
	public List<Empleado> findEmpleadoRepetido(long idPersona, long idEmpresa);
	
	public List<Empleado> findEmpleadosAlmacenes(List<Long> puestosPermitidos, long idEmpresa);

	public List<Empleado> findByProperties(HashMap<String, Object> params, long idEmpresa) throws Exception;
	
	public String generaClaveEmpleado(String categoria);
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado metodos findByProperties y findLikeProperties
 */