package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAOImpl;
import net.giro.tyg.admon.FormasPagos;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class FormasPagosImp extends DAOImpl<FormasPagos> implements FormasPagosDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<FormasPagos> findAll(String orderBy) throws Exception {
		String queryString = "select model from FormasPagos model ";
		
		try {
			queryString += "where model.estatus = 0";
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by id";
			
        	Query query = entityManager.createQuery(queryString);
        	return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FormasPagos> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		String queryString = "select model from FormasPagos model ";
		
		try {
			queryString += "where model.estatus = 0";
			if (value != null)
				queryString += " and model."+ propertyName + " = :propertyValue";
	
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
			
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
	public List<FormasPagos> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception {
		String queryString = "select model from FormasPagos model ";
    	//String sqlWhere = "";
    	StringBuffer sb = null;
		
		try {
			queryString += "where model.estatus = 0";
			if (value != null && ! "".equals(value.trim())) {
        		if ("id".equals(propertyName) || "ahorro".equals(propertyName))
        			queryString += " and cast(model." + propertyName + " as string) LIKE :" + propertyName;
            	else
            		queryString += " and lower(model." + propertyName + ") LIKE :" + propertyName;

        		sb = new StringBuffer();
        		sb.append("%");
        		sb.append(value.trim().toLowerCase());
        		sb.append("%");
        	}
        	
        	//queryString = queryString + sqlWhere;
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
			
        	Query query = entityManager.createQuery(queryString);
        	if (value != null && ! "".equals(value.trim()))
            	query.setParameter(propertyName, sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
        	return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
