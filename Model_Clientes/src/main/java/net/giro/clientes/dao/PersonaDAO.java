package net.giro.clientes.dao;

import java.util.HashMap;
import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.clientes.beans.Persona;

@Remote
public interface PersonaDAO extends DAO<Persona> {
	public long save(Persona entity, long codigoEmpresa) throws Exception;
	
	public List<Persona> saveOrUpdateList(List<Persona> entities, long codigoEmpresa) throws Exception;
	
	public Persona findByRfc(String rfc) throws Exception;

	public List<Persona> findAll(boolean incluyeEliminados, boolean incluyeSistema, String orderBy);
	
	public List<Persona> findByProperty(String propertyName, Object value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite);

	public List<Persona> findLike(String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite);
	
	public List<Persona> findLikeProperty(String propertyName, String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite);
	
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