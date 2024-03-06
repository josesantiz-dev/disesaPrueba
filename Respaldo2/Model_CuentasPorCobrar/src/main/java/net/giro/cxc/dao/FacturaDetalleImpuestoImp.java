package net.giro.cxc.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.FacturaDetalleImpuesto;

@Stateless
public class FacturaDetalleImpuestoImp extends DAOImpl<FacturaDetalleImpuesto> implements FacturaDetalleImpuestoDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaDetalleImpuesto> findAll(String orderBy) throws Exception {
		String queryString = "select model from FacturaDetalleImpuesto model ";
		
		try {
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
	public List<FacturaDetalleImpuesto> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		String queryString = "select model from FacturaDetalleImpuesto model ";
		
		try {
			if (value != null)
				queryString += "WHERE model."+ propertyName + " = :propertyValue";
	
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
	public List<FacturaDetalleImpuesto> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception {
		String queryString = "select model from FacturaDetalleImpuesto model ";
    	String sqlWhere = "";
    	StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.trim())) {
        		if (propertyName.startsWith("id") || "monto".equals(propertyName))
            		sqlWhere += "where cast(model." + propertyName + " as string) LIKE :" + propertyName;
            	else
            		sqlWhere += "where lower(model." + propertyName + ") LIKE :" + propertyName;

        		sb = new StringBuffer();
        		sb.append("%");
        		sb.append(value.trim().toLowerCase());
        		sb.append("%");
        	}
        	
        	queryString = queryString + sqlWhere;
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
