package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.OperacionesIntegradas;

@Stateless
public class OperacionesIntegradasImp extends DAOImpl<OperacionesIntegradas> implements OperacionesIntegradasDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		OperacionesIntegradasImp.orderBy = orderBy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradas> findByProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradas model ";
		
		try {
			if (value != null) {
				queryString = queryString + " where model."+ propertyName + " = :propertyValue";
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradas> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradas model ";
		StringBuffer sb = null;
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					queryString = queryString + " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradas> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradas model ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if(values != null && ! values.isEmpty()){
    			sqlWhere = " WHERE cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = sqlWhere + inFilter + ")";
        	}
        	
        	queryString += sqlWhere;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	if(values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
			if (limite > 0)
				query.setMaxResults(limite);
        	
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradas> findByProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			String queryString = "select model from OperacionesIntegradas model";
			String where = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if(where.length() > 0)
					where += " and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "'";
				else
					where += " where cast(model." + e.getKey() + " as string) = '" + e.getValue() + "'";
			}
			
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradas> findLikeProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			String queryString = "select model from OperacionesIntegradas model";
			String where = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if(where.length() > 0)
					where += " and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
				else
					where += " where cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
}