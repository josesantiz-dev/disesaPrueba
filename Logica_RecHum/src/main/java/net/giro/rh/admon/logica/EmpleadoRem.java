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
	
	public Empleado findById(long idEmpleado);

	public Empleado findByNss(String nssEmpleado) throws Exception;

	public List<Empleado> findAll(boolean incluyeBajas, boolean soloSistema, String orderBy);

	public List<Empleado> findAll(List<Long> listaEmpleados) throws Exception;

	public List<Empleado> findAll(List<Long> listaEmpleados, boolean incluyeBajas, boolean soloSistema, String orderBy) throws Exception;

	public List<Empleado> findAll();

	public List<Empleado> findAllActivos();
	
	public List<Empleado> findAllPuesto(Long idPuesto) throws Exception;

	public List<Empleado> findAllPuesto(List<Long> idPuestos) throws Exception;
	
	public List<Empleado> findAllByNss(String nssEmpleado) throws Exception;

	public List<Empleado> findByProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<Empleado> findByProperty(String propertyName, Object value, boolean incluyeBajas, boolean soloSistema, String orderBy, int limite) throws Exception;
	
	public List<Empleado> findByProperties(HashMap<String, Object> params) throws Exception;
	
	public List<Empleado> findLike(String value, String orderBy, int estatus, int limite) throws Exception;
	
	public List<Empleado> findLike(String value, boolean incluyeBajas, boolean soloSistema, String orderBy, int limite) throws Exception;
	
	public List<Empleado> findLikeProperty(String propertyName, Object value, boolean incluyeBajas, boolean soloSistema, String orderBy, int limite) throws Exception;

	public List<Empleado> findLikeProperty(String propertyName, Object value, int limite) throws Exception;

	public List<Empleado> findLikeSemanales(String value, String orderBy) throws Exception;
	
	public List<Empleado> findLikeQuincenales(String value, String orderBy) throws Exception;
	
	public boolean comprobarSemanal(long idEmpleado) throws Exception;

	public boolean comprobarQuincenal(long idEmpleado) throws Exception;

	public boolean findEmpleadoRepetido(long idPersona);

	public boolean validarEmpleado(long idPersona);
	
	public Empleado comprobarBajaPrevia(long idPersona);
	
	public List<Empleado> findEmpleadosAlmacenes(List<Long> puestosPermitidos);

	public Respuesta procesarLayout(byte[] fileSrc, HashMap<String, String> layout, List<String> listRequeridos) throws Exception;

	public Empleado convertir(EmpleadoExt entity);

	public EmpleadoExt convertir(Empleado entity);

	// --------------------------------------------------------------------------------------------
	// EXTENDIDOS
	// --------------------------------------------------------------------------------------------
	
	public String generaClaveEmpleado(String categoria) throws Exception;
	
	public Long save(EmpleadoExt extendido) throws Exception;

	public void update(EmpleadoExt entity) throws Exception;

	public EmpleadoExt findByIdExt(long idEmpleado);
	
	public EmpleadoExt findByNssExt(String nssEmpleado) throws Exception;

	public List<EmpleadoExt> findAllExt();

	public List<EmpleadoExt> findAllPuestoExt(long idPuesto) throws Exception;

	public List<EmpleadoExt> findAllPuestoExt(List<Long> idPuestos) throws Exception;
	
	public List<EmpleadoExt> findExtLikeNombreEmpleado(String nombreEmpleado) throws Exception;

	public List<EmpleadoExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception;
	
	public List<EmpleadoExt> findExtEmpleadosAlmacenes(List<Long> puestosPermitidos);
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