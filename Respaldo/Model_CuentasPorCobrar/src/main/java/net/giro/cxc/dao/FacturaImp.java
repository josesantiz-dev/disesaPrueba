package net.giro.cxc.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.Factura;

@Stateless
public class FacturaImp extends DAOImpl<Factura> implements FacturaDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		FacturaImp.orderBy = orderBy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findAll() {
		String queryString = "select model from Factura model";
		
		try {
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {		
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findByProperty(String propertyName, final Object value) {
		try {
			return this.findByProperty(propertyName, value, 0, 0);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findByProperty(String propertyName, Object value, int tipoObra) {
		try {
			return this.findByProperty(propertyName, value, tipoObra, 0);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findByProperty(String propertyName, Object value, int tipoObra, int max) {
		String queryString = "select model from Factura model ";
		
		try {
			if (tipoObra <= 0)
				queryString += "where model.tipoObra <> " + tipoObra;
			else
				queryString += "where model.tipoObra = " + tipoObra;
			
			if (value != null)
				queryString += " and model."+ propertyName + "= :propertyValue";
	
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findByPropertyPojoCompleto(String propertyName, Object value, int tipo) {
		return this.findByProperty(propertyName, value, tipo);
	}

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value) {
		try {
			return this.findLikeProperty(propertyName, value, 0, 0);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value, int max) {
		try {
			return this.findLikeProperty(propertyName, value, 0, max);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findLikeProperty(String propertyName, Object value, int tipoObra, int max) {
		String queryString = "select model from Factura model ";
    	String sqlWhere = "";
    	StringBuffer sb = null;
		
		try {
			sqlWhere = "where model.tipoObra <> :tipoObra ";
			if (tipoObra > 0)
				sqlWhere = "where model.tipoObra = :tipoObra ";
			
			if (value != null && ! "".equals(value.toString())){
        		if (propertyName.contains("id") || propertyName.toLowerCase().equals("idobra")) {
            		sqlWhere += " and lower(cast(model." + propertyName + " as string)) LIKE :" + propertyName;
            	} else {
            		sqlWhere += " and lower(model." + propertyName + ") LIKE :" + propertyName;
            	}

        		sb = new StringBuffer();
        		sb.append("%");
        		sb.append(value.toString().toLowerCase());
        		sb.append("%");
        	}
        	
        	queryString = queryString + sqlWhere;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
        	Query query = entityManager.createQuery(queryString);        	
        	query.setParameter("tipoObra", tipoObra);			
        	if (value != null && ! "".equals(value.toString()))
            	query.setParameter(propertyName, sb.toString());
			if (max > 0)
				query.setMaxResults(max);
        	return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findLikeProperties(HashMap<String, Object> params) throws Exception {
		String queryString = "select model from Factura model ";
		String whereString = "";
		
		try { 
			for(Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " and";
				
				if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " cast(model." + e.getKey() + " as string) LIKE '%" + ((BigDecimal) e.getValue()).toString() + "%'";
				else
					whereString += " cast(model." + e.getKey() + " as string) LIKE '%" + e.getValue().toString() + "%'";
			}

			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> saveOrUpdateList(List<Factura> entities) throws Exception {
		return super.saveOrUpdateList(entities);
	}
}
