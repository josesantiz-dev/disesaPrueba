package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAO;

import javax.ejb.Remote;

import net.giro.clientes.beans.Negocio;

@Remote
public interface NegocioDAO extends DAO<Negocio> {
	public long save(Negocio entity, long codigoEmpresa) throws Exception;
	
	public List<Negocio> saveOrUpdateList(List<Negocio> entities, long codigoEmpresa) throws Exception;

	public Negocio findByRfc(String rfc) throws Exception;
	
	public List<Negocio> findAll(boolean incluyeEliminados, boolean incluyeSistema, String orderBy);
	
	public List<Negocio> findByProperty(String propertyName, Object value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite);

	public List<Negocio> findLike(String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite);
	
	public List<Negocio> findLikeProperty(String propertyName, String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite);
}

/* ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   |       AUTOR      | DESCRIPCIÓN
 * ----------------------------------------------------------------------------------------------------------------
 *    1.2   | 2016-11-05 | Javier Tirado	| Añado metodos findAll y findAll(String)
 */