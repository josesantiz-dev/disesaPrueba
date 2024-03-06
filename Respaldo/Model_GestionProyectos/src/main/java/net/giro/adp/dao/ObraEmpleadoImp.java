package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.ObraEmpleado;

@Stateless
public class ObraEmpleadoImp extends DAOImpl<ObraEmpleado> implements ObraEmpleadoDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraEmpleado> findByProperty(String propertyName, Object value, int max) {
		String queryString = "select model from ObraEmpleado model ";
		
		try {
			if(value != null && !"".equals(value.toString())) {
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
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, int max) {
		String queryString = "select model from ObraEmpleado model ";
		StringBuffer sb = null;
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if (propertyName.toLowerCase().contains("id")) {
					queryString += " where lower(cast(model."+ propertyName + " as string)) LIKE :propertyValue";
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
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
