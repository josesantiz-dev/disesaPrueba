package net.giro.clientes.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.clientes.beans.Persona;
import net.giro.comun.ExcepConstraint;

@Remote
public interface PersonaDAO extends DAO<Persona> {
	public List<Persona> findLikeId(String id) throws Exception;
	
	public List<Persona> findLikePersonaPropiedad(String propiedad, String valor, Long valor2) throws ExcepConstraint;
	
	public HashMap<String, Long> getIdListLikeNombre(String nombre) throws Exception;
	
	public List<Persona> findAll();
	
	public List<Persona> findLikePersonas(Object value, int max);
	
	public List<Persona> findLikeClaveNombre(String propertyName, Object value, long valGpo, String tipoPersona, int max, Boolean valido);
	
	public List<Persona> findLikeProveedor(Object value, long valGpo, String tipoPersona, Long tipoGastoAsociado, int max);
	
	//public List<Persona> findLikePersonasConGastos(Object value, long valGpo, String tipoPersona, String tipo, String estatus, int max, Date fecha, String sucursales);
	
	public List<Persona> findByProperty(String propertyName, Object value, int limite);
	
	public List<Persona> findLikeProperty(String propertyName, String value, int limite);
	
	public List<Persona> findByProperties(HashMap<String, Object> params) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.2 | 2017-05-18 | Javier Tirado 	| Añado el metodo findByProperties
 */