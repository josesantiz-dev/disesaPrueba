package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.InsumosDetalles;

@Stateless
public class InsumosDetallesImp extends DAOImpl<InsumosDetalles> implements InsumosDetallesDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<InsumosDetalles> findByProperty(String propertyName, Object value, int max) {
		String queryString = "select model from InsumosDetalles model ";
		
		try {
			if (value != null) {
				queryString += " where model."+ propertyName + " = :propertyValue";
			}
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
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
	public List<InsumosDetalles> findLikeProperty(String propertyName, Object value, int max) {
		String queryString = "select model from InsumosDetalles model ";
		StringBuffer sb = null;
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || "idinsumo".equals(propertyName.toLowerCase()) || "idproducto".equals(propertyName.toLowerCase())) {
					queryString += " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					queryString += " where lower(model."+ propertyName + ") LIKE :propertyValue";
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
