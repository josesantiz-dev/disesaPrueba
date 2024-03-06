package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.cxc.beans.FacturaPagos;

@Stateless
public class FacturaPagosImp extends DAOImpl<FacturaPagos> implements FacturaPagosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findByProperty(String columnName, Object value, int limite) {
		String queryString = "select model from FacturaPagos model INNER JOIN FETCH model.idFactura fac ";
    	
    	try {
    		if ("id".equals(columnName))
    			columnName = "model.id";
    		
    		if (value != null && ! "".equals(value))
    			queryString += "WHERE " + columnName + " = :propertyValue ";
    		queryString += "ORDER BY " + columnName;
        	
        	Query query = entityManager.createQuery(queryString);
        	if (value != null && ! "".equals(value))
            	query.setParameter("propertyValue", value);
        	if (limite > 0)
        		query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findLikeProperty(String columnName, String value, int limite) {
		String queryString = "SELECT model FROM FacturaPagos model INNER JOIN FETCH model.idFactura fac ";
    	StringBuffer sb = null;
    	
    	try {
    		if ("id".equals(columnName))
    			columnName = "model.id";
    		
    		if (value != null && ! "".equals(value.trim())) {
        		if (columnName.startsWith("id") && ! columnName.contains(".")) {
        			queryString += "where lower(cast(" + columnName + " as string)) LIKE :propertyValue ";
            	} else {
            		queryString += "WHERE lower(" + columnName + ") LIKE :propertyValue ";
            	}

        		sb = new StringBuffer();
        		sb.append("%");
        		sb.append(value.toLowerCase());
        		sb.append("%");
        	}
    		
    		queryString += "ORDER BY " + columnName;
        	
        	Query query = entityManager.createQuery(queryString);
        	if (value != null && ! "".equals(value.trim()))
            	query.setParameter("propertyValue", sb.toString());
        	if (limite > 0)
        		query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findInProperty(String columnName, List<Long> values, int limite) {
		String queryString = "SELECT model FROM FacturaPagos model INNER JOIN FETCH model.idFactura fac ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = "WHERE cast(model." + columnName + " as string) IN (";
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
        		sqlWhere = sqlWhere + inFilter + ") ";
        	}
    		
    		queryString += "ORDER BY " + columnName;
        	
        	Query query = entityManager.createQuery(queryString);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) 
        			query.setParameter(columnName + i, values.get(i).toString());
        	}
        	if (limite > 0)
        		query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}
}
