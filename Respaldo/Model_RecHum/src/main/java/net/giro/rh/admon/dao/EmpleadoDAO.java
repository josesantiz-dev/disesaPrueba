package net.giro.rh.admon.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.rh.admon.beans.Empleado;

@Remote
public interface EmpleadoDAO extends DAO<Empleado> {

	public void delete(Empleado entity);

	public void update(Empleado entity);

	public Empleado findById(Integer id);

	public List<Empleado> findByProperty(String propertyName, Object value);

	public List<Empleado> findAll();
	public List<Empleado> findAllActivos();

	public List<Empleado> findByEmpleado(String nombreEmpleado);
	
	public List<Empleado> findLikeClaveNombre(String value);

	public List<Empleado> findByPropertyPojoCompleto(String propertyName, String tipo, long value);
	
	public List<Empleado> findEmpleadoRepetido(long idEmpleado);

	public List<Empleado> findLikeProperty(String propertyName, Object value, int limite);

	public List<Empleado> findByProperties(HashMap<String, Object> params) throws Exception;

	public List<Empleado> findLikeProperties(HashMap<String, String> params) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado metodos findByProperties y findLikeProperties
 */