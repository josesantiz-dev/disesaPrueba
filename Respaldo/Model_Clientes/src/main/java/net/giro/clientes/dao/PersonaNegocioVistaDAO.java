package net.giro.clientes.dao;

import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import net.giro.DAO;
import net.giro.clientes.beans.PersonaNegocioVista;

@Remote
public interface PersonaNegocioVistaDAO extends DAO<PersonaNegocioVista> {
	public List<PersonaNegocioVista> findAll(Long estatus, int limite) throws Exception;
	
	public List<PersonaNegocioVista> findByProperty(String propertyName, final Object value, Long estatus, int limite) throws RuntimeException;

	public List<PersonaNegocioVista> findByProperties(HashMap<String, Object> params, Long estatus, int limite) throws Exception;

	public List<PersonaNegocioVista> findLikeProperty(String propertyName, final Object value, Long estatus, int limite) throws Exception;

	public List<PersonaNegocioVista> findLikeProperties(HashMap<String, String> params, Long estatus, int limite) throws Exception;
	
	public List<PersonaNegocioVista> findInProperty(String columnName, List<Object> values, Long estatus, int limite) throws Exception;
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 24/04/2017 | Javier Tirado	| Creacion de PersonaNegocioVistaDAO