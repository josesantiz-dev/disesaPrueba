package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.Presupuesto;

@Stateless
public class PresupuestoImp  extends DAOImpl<Presupuesto> implements PresupuestoDAO {
	@PersistenceContext
	private EntityManager entityManager;

	public void delete(Presupuesto entity) {
		try {
			entity = entityManager.getReference(Presupuesto.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(Presupuesto entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Presupuesto findById(Integer id) {
		try {
			Presupuesto instance = entityManager.find(Presupuesto.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Presupuesto> findByProperty(String propertyName, Object value, int max) {
		
		System.out.println("Entrando a model de Presupuesto Obra");
		
		String queryString = "select model from Presupuesto model ";
		
		try {
			if (value != null && !"".equals(value.toString())) {
				queryString = queryString + " where model."+ propertyName + " = :propertyValue";
			}
			
			queryString += " order by id asc";
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Presupuesto> findLikeProperty(String propertyName, Object value, int max) {
		String queryString = "select model from Presupuesto model ";
		StringBuffer sb = null;
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName)) {
					queryString += " where cast(model."+ propertyName + " as string) LIKE :propertyName";
				} else {
					queryString = queryString + " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase PresupuestoImp