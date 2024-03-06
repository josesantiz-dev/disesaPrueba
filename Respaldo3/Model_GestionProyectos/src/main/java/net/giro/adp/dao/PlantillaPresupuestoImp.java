package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.PlantillaPresupuesto;

@Stateless
public class PlantillaPresupuestoImp extends DAOImpl<PlantillaPresupuesto> implements PlantillaPresupuestoDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<PlantillaPresupuesto> findByProperty(String propertyName, Object value, int max) {
		String queryString = "select model from PlantillaPresupuesto model ";
		
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
	public List<PlantillaPresupuesto> findLikeProperty(String propertyName, Object value, int max) {
		String queryString = "select model from PlantillaPresupuesto model ";
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
