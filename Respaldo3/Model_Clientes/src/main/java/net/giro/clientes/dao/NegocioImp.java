package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.clientes.beans.Negocio;

@Stateless
public class NegocioImp extends DAOImpl<Negocio> implements NegocioDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Negocio entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Negocio> saveOrUpdateList(List<Negocio> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Negocio findByRfc(String rfc) throws Exception {
		String queryString = "select model from Negocio model where model.rfc = :rfc order by model.id desc";
		List<Negocio> negocios = null;
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("rfc", rfc);
			negocios = (List<Negocio>) query.getResultList();
			if (negocios != null && ! negocios.isEmpty())
				return negocios.get(0);
			return null;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findAll(boolean incluyeEliminados, boolean incluyeSistema, String orderBy) {
		String queryString = "select model from Negocio model where model.estatus in (:estatus) and model.sistema in (:sistema) ";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.nombre";
			queryString = queryString.replace(":estatus", (incluyeEliminados ? "1,2" : "1"));
			queryString = queryString.replace(":sistema", (incluyeSistema ? "0,1" : "0"));
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findByProperty(String propertyName, Object value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite) {
		String queryString = "select model from Negocio model where model.estatus in (:estatus) and model.sistema in (:sistema) ";
		
		try {
			if (propertyName == null || "".equals(propertyName))
				propertyName = "nombre";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.nombre";
			if (value != null) 
				queryString += "and model." + propertyName + " = :propertyValue ";
			queryString = queryString.replace(":estatus", (incluyeEliminados ? "1,2" : "1"));
			queryString = queryString.replace(":sistema", (incluyeSistema ? "0,1" : "0"));
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findLike(String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite) {
		String queryString = "select model from Negocio model where model.estatus in (:estatus) and model.sistema in (:sistema) ";
		StringBuffer sb = null;
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.nombre";
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue ";
				queryString += "or lower(trim(model.nombre)) like :propertyValue ";
				queryString += "or lower(trim(model.rfc)) like :propertyValue ";
				queryString += "or lower(trim(model.correoContacto)) like :propertyValue) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
	    		sb.append("%");
			}

			queryString = queryString.replace(":estatus", (incluyeEliminados ? "1,2" : "1"));
			queryString = queryString.replace(":sistema", (incluyeSistema ? "0,1" : "0"));
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findLikeProperty(String propertyName, String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite) {
		String queryString = "select model from Negocio model where model.estatus in (:estatus) and model.sistema in (:sistema) ";
		StringBuffer sb = null;
		
		try {
			if (propertyName == null || "".equals(propertyName))
				propertyName = "nombre";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.nombre";
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
	    		sb.append("%");
			}

			queryString = queryString.replace(":estatus", (incluyeEliminados ? "1,2" : "1"));
			queryString = queryString.replace(":sistema", (incluyeSistema ? "0,1" : "0"));
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-05 | Javier Tirado		| Añado metodos findAll y findAll(String)
 * 1.2 | 2016-11-09 | Javier Tirado 	| Añado los metodos findByProperty y findLikeProperty
 */
