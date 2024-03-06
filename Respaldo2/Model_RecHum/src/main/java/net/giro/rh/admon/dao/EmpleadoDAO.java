package net.giro.rh.admon.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.Empleado;

@Remote
public interface EmpleadoDAO extends DAO<Empleado> {
	public long save(Empleado entity, Long idEmpresa) throws Exception;
	
	public List<Empleado> saveOrUpdateList(List<Empleado> entities, Long idEmpresa) throws Exception;
	
	public List<Empleado> findAll(Long idEmpresa);
	
	public List<Empleado> findAllActivos(Long idEmpresa);
	
	public List<Empleado> findBy(Object value, String orderBy, int limite, Long idEmpresa) throws Exception;

	public List<Empleado> findByProperty(String propertyName, Object value, Long idEmpresa);

	public List<Empleado> findByProperties(HashMap<String, Object> params, Long idEmpresa) throws Exception;
	
	public List<Empleado> findLike(String value, String orderBy, int limite, Long idEmpresa) throws Exception;

	public List<Empleado> findLikeProperty(String propertyName, Object value, int limite, Long idEmpresa);

	public List<Empleado> findLikeProperties(HashMap<String, String> params, Long idEmpresa) throws Exception;

	public List<Empleado> findByEmpleado(String nombreEmpleado, Long idEmpresa);
	
	public List<Empleado> findLikeClaveNombre(String value, Long idEmpresa);

	public List<Empleado> findByPropertyPojoCompleto(String propertyName, String tipo, long value, Long idEmpresa);
	
	public List<Empleado> findEmpleadoRepetido(long idEmpleado, Long idEmpresa);
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado metodos findByProperties y findLikeProperties
 */