package net.giro.clientes.logica;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.clientes.beans.PersonaNegocioVista;
import net.giro.plataforma.InfoSesion;

@Remote
public interface PersonaNegocioVistaRem {
	public void setInfoSesion(InfoSesion infoSesion);

	public PersonaNegocioVista findById(Long id);
	
	public PersonaNegocioVista findById(Long id, String tipo);

	public List<PersonaNegocioVista> findAll(Long estatus, int limite) throws Exception;
	
	public List<PersonaNegocioVista> busquedaDinamica(String nombre) throws Exception;
	
	/**
	 * 
	 * @param nombre
	 * @param tipoPersona Tipo de Persona: N - Negocio, P - Persona
	 * @return
	 * @throws Exception
	 */
	public List<PersonaNegocioVista> findLike(String nombre, String tipoPersona) throws Exception;
	
	public List<PersonaNegocioVista> findByProperty(String propertyName, final Object value, Long estatus, int limite) throws Exception;

	public List<PersonaNegocioVista> findByProperties(HashMap<String, Object> params, Long estatus, int limite) throws Exception;

	public List<PersonaNegocioVista> findLikeProperty(String propertyName, final Object value, Long estatus, int limite) throws Exception;

	public List<PersonaNegocioVista> findLikeProperties(HashMap<String, String> params, Long estatus, int limite) throws Exception;
	
	public List<PersonaNegocioVista> findInProperty(String columnName, List<Object> values, Long estatus, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 24/04/2017 | Javier Tirado	| Creacion de PersonaNegocioVistaRem