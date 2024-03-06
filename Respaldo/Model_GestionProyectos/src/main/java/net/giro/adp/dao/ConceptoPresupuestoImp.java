package net.giro.adp.dao;


import net.giro.DAOImpl;

import net.giro.adp.beans.ConceptoPresupuesto;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ConceptoPresupuestoImp extends DAOImpl<ConceptoPresupuesto> implements ConceptoPresupuestoDAO {
	
	@PersistenceContext
	private EntityManager entityManager; 

	public void delete(ConceptoPresupuesto entity) {
		try {
			entity = entityManager.getReference(ConceptoPresupuesto.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(ConceptoPresupuesto entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ConceptoPresupuesto findById(Integer id) {
		try {
			ConceptoPresupuesto instance = entityManager.find(ConceptoPresupuesto.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoPresupuesto> findByProperty(String propertyName, Object value, int max) {
		String queryString = "select model from ConceptoPresupuesto model ";
		
		try {
			if (value != null && !"".equals(value.toString())) {
				queryString = queryString + " where model."+ propertyName + " = :propertyValue";
			}
			
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

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoPresupuesto> findLikeProperty(String propertyName, Object value, int max) {
		String queryString = "select model from ConceptoPresupuesto model ";
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
	
	@SuppressWarnings("unchecked")
	public List<ConceptoPresupuesto> findAllActivos() {
		try {
			final String queryString = "select model from ConceptoPresupuesto model order by model.orden asc";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
