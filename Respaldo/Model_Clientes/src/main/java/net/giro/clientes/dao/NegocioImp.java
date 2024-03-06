package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.clientes.beans.Negocio;

@Stateless
public class NegocioImp extends DAOImpl<Negocio> implements NegocioDAO  {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findAll() {
		String queryString = "select model from Negocio model ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findAll(String nombre) throws Exception{
		boolean isNumber = false;
		String queryString = "select model from Negocio model ";
		String where = "";
		
		try {
			if (!"".equals(nombre.trim())) {
				try { Integer.parseInt(nombre.trim()); isNumber= true; } 
				catch (Exception ex) { isNumber = false; }
				
				if (isNumber) {
					where = " where cast(model.id as varchar) like '%" + nombre.trim() + "%'";
				} else {
					where = " where lower(model.nombre) like '%" + nombre.trim().toLowerCase() + "%'";
				}
			}

			queryString += where;
			
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findLikeId(String id) throws Exception{
		try {
			String queryString = "select Negs from Negocio Negs ";
			String where = " where cast(Negs.id as varchar) like '%" + id + "%'";

			queryString += where;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findLikeColumnName(String propertyName, String value, int max) {
		String queryString = "select model from Negocio model ";
		String orderBy = "";
		
		try {
			if (propertyName == null || "".equals(propertyName))
				propertyName = "nombre";
			
			orderBy = propertyName;
			if(value == null) {
				orderBy = "id desc";
				value = "";
			}
			
			queryString += "where lower(cast(model."+ propertyName + " as string)) like '%" + value.toLowerCase() + "%' ";
			queryString += "order by model." + orderBy;
							 			   
			Query query = entityManager.createQuery(queryString);
			if(max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re ) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findByProperty(String propertyName, Object value, int limite) {
		String queryString = "select model from Negocio model ";
		String orderBy = "";
		
		try {
			if (propertyName == null || "".equals(propertyName))
				propertyName = "nombre";
			
			orderBy = "order by model.id desc";
			if (value != null) {
				queryString += "where model." + propertyName + " = :propertyValue ";
				orderBy = "order by model." + propertyName;
			}
			queryString += orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findLikeProperty(String propertyName, String value, int limite) {
		StringBuffer sb = null;
		String queryString = "select model from Negocio model ";
		String whereString = "";
		String orderBy = "";
		
		try {
			if(value != null && ! "".equals(value.trim())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					whereString = "where cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					whereString = "where lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
	    		orderBy = propertyName;
			} else {
				orderBy = "id desc";
				value = "";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			queryString += "order by model." + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
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