package net.giro.rh.admon.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;

@Remote
public interface EmpleadoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(Empleado entity) throws Exception;
	
	public List<Empleado> saveOrUpdateList(List<Empleado> entities) throws Exception;

	public void update(Empleado entity) throws Exception;
	
	public void delete(Empleado entity) throws Exception;
	
	public Empleado findById(Long id);

	public List<Empleado> findAll();
	
	public List<Empleado> findAllActivos();
	
	public List<Empleado> findBy(Object value, String orderBy, int limite) throws Exception;
	
	public List<Empleado> findByProperty(String propertyName, Object value, int limite);
	
	public List<Empleado> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<Empleado> findLike(String value, String orderBy, int limite) throws Exception;
	
	public List<Empleado> findLikeProperty(String propertyName, Object value, int limite);

	public List<Empleado> findLikeProperties(HashMap<String, String> params) throws Exception;
	
	public List<Empleado> findLikeNombreEmpleado(String nombreEmpleado);
	
	public List<Empleado> findByPropertyPojoCompleto(String propertyName, String tipo, long value);

	public boolean findEmpleadoRepetido(long idEmpleado);

	public Respuesta procesarLayout(byte[] fileSrc, HashMap<String, String> layout, List<String> listRequeridos) throws Exception;

	public Empleado convertir(EmpleadoExt entity);

	public EmpleadoExt convertir(Empleado entity);

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------
	
	public Long save(EmpleadoExt entityExt) throws Exception;

	public void update(EmpleadoExt entity) throws Exception;

	public EmpleadoExt findByIdExt(Long id);

	public List<EmpleadoExt> findAllExt();

	public List<EmpleadoExt> findExtLikeNombreEmpleado(String nombreEmpleado);

	public List<EmpleadoExt> findExtLikeProperty(String propertyName, Object value, int limite);
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