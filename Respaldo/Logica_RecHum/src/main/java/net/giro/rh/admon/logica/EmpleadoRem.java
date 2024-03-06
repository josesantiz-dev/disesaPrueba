package net.giro.rh.admon.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;

@Remote
public interface EmpleadoRem {
	
	public Long save(Empleado entity) throws ExcepConstraint;
	public Long save(EmpleadoExt entityExt) throws ExcepConstraint;
	
	public void delete(Empleado entity) throws ExcepConstraint;
	//public void delete(EmpleadoExt entityExt) throws ExcepConstraint;

	public Empleado update(Empleado entity) throws ExcepConstraint;
	public Empleado update(EmpleadoExt entity) throws ExcepConstraint;

	public Empleado findById(Long id);
	public EmpleadoExt findByIdExt(Long id);

	public List<Empleado> findByProperty(String propertyName, Object value);

	public List<Empleado> findAll();
	public List<Empleado> findAllActivos();
	public List<EmpleadoExt> findAllExt();

	public List<EmpleadoExt> findByEmpleado(String nombreEmpleado);
	public List<Empleado> findByNombreEmpleado(String nombreEmpleado);
	
	public List<Empleado> findByPropertyPojoCompleto(String propertyName, String tipo, long value);

	public boolean findEmpleadoRepetido(long idEmpleado);
	//public List<EmpleadoExt> findByPropertyPojoCompletoExt(String propertyName, String tipo, long value);

	public List<Empleado> findLikeProperty(String propertyName, Object value, int limite);
	public List<EmpleadoExt> findExtLikeProperty(String propertyName, Object value, int limite);

	public Empleado convertir(EmpleadoExt entity);
	public EmpleadoExt convertir(Empleado entity);
	
	public Respuesta procesarLayout(byte[] fileSrc, HashMap<String, String> layout, List<String> listRequeridos) throws Exception;
	
	public List<Empleado> findByProperties(HashMap<String, Object> params) throws Exception;

	public List<Empleado> findLikeProperties(HashMap<String, String> params) throws Exception;
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2016-11-09 | Javier Tirado 	| Agrego disponibilidad del convertidor
 *  2.2 | 2017-05-16 | Javier Tirado 	| Añado procesamiento de layout de Empleados (carga por lotes)
 *  2.2 | 2017-05-23 | Javier Tirado 	| Añado los metodos findByProperties y findLikeProperties
 */