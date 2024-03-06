package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.EmpleadoNomina;
import net.giro.rh.admon.beans.EmpleadoNominaEstatus;
import net.giro.rh.admon.beans.EmpleadoNominaPreliminar;

@Remote
public interface EmpleadoNominaRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);

	public Long save(EmpleadoNomina entity) throws Exception;

	public List<EmpleadoNomina> saveOrUpdateList(List<EmpleadoNomina> entities) throws Exception;

	public void update(EmpleadoNomina entity) throws Exception;
	
	public void delete(long entityId) throws Exception;

	public List<EmpleadoNomina> deleteAll(List<EmpleadoNomina> entities) throws Exception;

	public EmpleadoNomina findById(long entityId);

	public List<EmpleadoNomina> findAll() throws Exception;
	
	public List<EmpleadoNomina> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoNomina> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoNomina> findByDates(Date fechaDesde, Date fechaHasta) throws Exception;

	public Respuesta nominaSemanal(Date fechaDesde, Date fechaHasta, boolean recalcular, boolean preliminar) throws Exception;

	public Respuesta nominaQuincenal(Date fechaDesde, Date fechaHasta, boolean recalcular, boolean preliminar) throws Exception;
	
	/**
	 * Metodo para importar Nomina Quincenal
	 * @param fechaDesde Fecha inicial del periodo quincenal
	 * @param fechaHasta Fecha final del periodo quincenal
	 * @param source Hoja de calculo digitalizada con la informacion de Nomina Quincenal
	 * @param layout Mapa de celdas (formato) para el reconocimiento de informacion 
	 * @param retroactivo
	 * @param permitirEmpleadoSinSueldo
	 * @return
	 * @throws Exception
	 */
	public Respuesta importarNominaQuincenal(Date fechaDesde, Date fechaHasta, byte[] source, LinkedHashMap<String, String> layout, boolean retroactivo, boolean permitirEmpleadoSinSueldo) throws Exception;
	
	public int comprobarCalculoNomina(long idNominaEstatus) throws Exception;

	public EmpleadoNominaEstatus findNominaEstatus(long idEstatus);

	// --------------------------------------------------------------------------------------------------------
	// PRELIMINAR
	// --------------------------------------------------------------------------------------------------------

	public List<EmpleadoNominaPreliminar> saveOrUpdateListPreliminar(List<EmpleadoNomina> entities) throws Exception;
	
	public List<EmpleadoNominaPreliminar> deleteAllPreliminar(List<EmpleadoNominaPreliminar> entities) throws Exception;

	public List<EmpleadoNominaPreliminar> findByDatesPreliminar(Date fechaDesde, Date fechaHasta) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaRem