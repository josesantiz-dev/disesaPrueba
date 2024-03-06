package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.TransaccionConceptos;

@Stateless
public class TransaccionConceptosImp extends DAOImpl<TransaccionConceptos> implements TransaccionConceptosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private String orderBy;

	@Override
	public void orderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionConceptos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from TransaccionConceptos model ";
		
		try {
			if (value != null) {
				queryString = queryString + " where model."+ propertyName + " = :propertyValue";
			}
			
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			
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
	public List<TransaccionConceptos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from TransaccionConceptos model ";
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
			
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionConceptos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		String queryString = "select model from TransaccionConceptos model ";
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
			
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
        	
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
    	}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionConceptos> findByProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			String queryString = "select model from TransaccionConceptos model";
			String where = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if(where.length() > 0)
					where += " and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "'";
				else
					where += " where cast(model." + e.getKey() + " as string) = '" + e.getValue() + "'";
			}
			
			queryString += where;
			
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionConceptos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			String queryString = "select model from TransaccionConceptos model";
			String where = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if(where.length() > 0)
					where += " and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
				else
					where += " where cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			queryString += where;
			
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/06/2016 | Javier Tirado	| Creacion de TransaccionConceptosImp