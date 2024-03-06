package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.clientes.beans.Negocio;

@Remote
public interface NegocioDAO extends DAO<Negocio> {
	public List<Negocio> findLikeId(String id) throws Exception;
	
	public List<Negocio> findLikeColumnName(String propertyName, String value, int max);
	
	public List<Negocio> findAll();
	
	public List<Negocio> findAll(String nombre) throws Exception;
	
	public List<Negocio> findByProperty(String propertyName, Object value, int limite);
	
	public List<Negocio> findLikeProperty(String propertyName, String value, int limite);
}

/* ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   |       AUTOR      | DESCRIPCIÓN
 * ----------------------------------------------------------------------------------------------------------------
 *    1.2   | 2016-11-05 | Javier Tirado	| Añado metodos findAll y findAll(String)
 */