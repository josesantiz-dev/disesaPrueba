package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoDescuento;
import net.giro.rh.admon.beans.EmpleadoDescuentoExt;

@Remote
public interface EmpleadoDescuentoRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);

	public Long save(EmpleadoDescuento entity) throws ExcepConstraint;
	public Long save(EmpleadoDescuentoExt entityExt) throws ExcepConstraint;
	
	public void update(EmpleadoDescuento entity) throws ExcepConstraint;
	public void update(EmpleadoDescuentoExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public EmpleadoDescuento findById(Long id);
	public EmpleadoDescuentoExt findExtById(Long id) throws Exception;
	
	public List<EmpleadoDescuento> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	public List<EmpleadoDescuentoExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoDescuento> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	public List<EmpleadoDescuentoExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<EmpleadoDescuento> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	public List<EmpleadoDescuentoExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<EmpleadoDescuento> findByProperties(HashMap<String, String> params, int limite) throws Exception;
	public List<EmpleadoDescuentoExt> findExtByProperties(HashMap<String, String> params, int limite) throws Exception;

	public List<EmpleadoDescuento> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	public List<EmpleadoDescuentoExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;

	public EmpleadoDescuento cancelar(EmpleadoDescuento entity) throws Exception;
	public EmpleadoDescuentoExt cancelar(EmpleadoDescuentoExt entityExt) throws Exception;
	
	public List<EmpleadoDescuento> comprobarDescuentoPorFechas(Long idEmpleado, Date fecha) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 25/05/2016 | Javier Tirado	| Creacion de EmpleadoDescuentoRem