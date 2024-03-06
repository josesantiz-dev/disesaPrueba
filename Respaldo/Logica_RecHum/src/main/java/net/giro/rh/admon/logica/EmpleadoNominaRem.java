package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.publico.respuesta.Respuesta;
import net.giro.rh.admon.beans.EmpleadoNomina;

@Remote
public interface EmpleadoNominaRem {
	public void setInfoSesion(InfoSesion infoSesion);
	public void showSystemOuts(boolean value);
	public void orderBy(String orderBy);
	//public void estatus(Long estatus);

	public Long save(EmpleadoNomina entity) throws ExcepConstraint;
	//public Long save(EmpleadoNominaExt entityExt) throws ExcepConstraint;
	
	public void update(EmpleadoNomina entity) throws ExcepConstraint;
	//public void update(EmpleadoNominaExt entityExt) throws ExcepConstraint;
	
	public void delete(Long entityId) throws ExcepConstraint;

	public EmpleadoNomina findById(Long id);
	//public EmpleadoNominaExt findExtById(Long id) throws Exception;

	public List<EmpleadoNomina> findAll() throws Exception;
	//public List<EmpleadoNominaExt> findExtAll() throws Exception;
	
	public List<EmpleadoNomina> findByProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<EmpleadoNominaExt> findExtByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoNomina> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	//public List<EmpleadoNominaExt> findExtLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<EmpleadoNomina> findInProperty(String columnName, List<Object> values, int limite) throws Exception;
	//public List<EmpleadoNominaExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<EmpleadoNomina> findByProperties(HashMap<String, Object> params, int limite) throws Exception;
	//public List<EmpleadoNominaExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<EmpleadoNomina> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	//public List<EmpleadoNominaExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception;

	//public EmpleadoNomina cancelar(EmpleadoNomina entity) throws Exception;
	//public EmpleadoNomina cancelar(EmpleadoNominaExt entityExt) throws Exception;
	
	public Respuesta generarNomina(Date fechaDesde, Date fechaHasta, boolean recalcular) throws Exception;
	
	public List<EmpleadoNomina> findByDates(Date fechaDesde, Date fechaHasta) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaRem