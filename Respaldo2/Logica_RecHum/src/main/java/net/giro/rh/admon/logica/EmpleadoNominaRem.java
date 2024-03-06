package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.EmpleadoNomina;

@Remote
public interface EmpleadoNominaRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(EmpleadoNomina entity) throws Exception;
	
	public void update(EmpleadoNomina entity) throws Exception;
	
	public void delete(Long entityId) throws Exception;

	public EmpleadoNomina findById(Long id);

	public List<EmpleadoNomina> findAll() throws Exception;
	
	public List<EmpleadoNomina> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoNomina> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;
	
	public List<EmpleadoNomina> findInProperty(String columnName, List<Object> values, int limite) throws Exception;

	public List<EmpleadoNomina> findByProperties(HashMap<String, Object> params, int limite) throws Exception;

	public List<EmpleadoNomina> findLikeProperties(HashMap<String, String> params, int limite) throws Exception;
	
	public Respuesta generarNomina(Date fechaDesde, Date fechaHasta, boolean recalcular) throws Exception;
	
	public List<EmpleadoNomina> findByDates(Date fechaDesde, Date fechaHasta) throws Exception;
	
	/**
	 * Importa el archivo especificado a nominas de empleados (Quincenal)
	 * @param filename Nombre del archivo a importar
	 * @param source contenido en bytes del archivo a importar
	 * @param layout formato al cual debe apegarse el archivo a importar
	 * @return nominaQuincenal, sinProcesar, advertencias
	 * @throws Exception
	 */
	public Respuesta importarNominaQuincenal(String filename, byte[] source, LinkedHashMap<String, String> layout) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaRem