package net.giro.rh.admon.logica;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.rh.admon.beans.EmpleadoInfonavit;

@Remote
public interface EmpleadoInfonavitRem {
	public void orderBy(String orderBy);

	public void setInfoSesion(InfoSesion infoSesion);
	
	public Long save(EmpleadoInfonavit entity) throws Exception;
	
	public List<EmpleadoInfonavit> saveOrUpdateList(List<EmpleadoInfonavit> entities) throws Exception;
	
	public void update(EmpleadoInfonavit entity) throws Exception;

	public void cancelar(EmpleadoInfonavit entity) throws Exception;

	public void delete(long idEmpleadoInfonavit) throws Exception;

	public EmpleadoInfonavit findById(long idEmpleadoInfonavit);

	public List<EmpleadoInfonavit> findAll(long idEmpleado) throws Exception;

	public List<EmpleadoInfonavit> findByProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoInfonavit> findLikeProperty(String propertyName, final Object value, int limite) throws Exception;

	public List<EmpleadoInfonavit> comprobarPorFecha(long idEmpleado, Date fecha) throws Exception;

	public List<EmpleadoInfonavit> comprobarPorFecha(long idEmpleado, Date fechaDesde, Date fechaHasta) throws Exception;

	public boolean comprobarRegistro(EmpleadoInfonavit entity) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoInfonavitRem