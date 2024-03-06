package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;

@Stateless
public class OperacionesIntegradasTransaccionesImp extends DAOImpl<OperacionesIntegradasTransacciones> implements OperacionesIntegradasTransaccionesDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private Long idEmpresa;
	private static String orderBy;

	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public void orderBy(String orderBy) {
		OperacionesIntegradasTransaccionesImp.orderBy = orderBy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradasTransacciones> findByProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasTransacciones model where model.idEmpresa = :idEmpresa and estatus = 0 ";
		
		try {
			if (value != null) {
				queryString = queryString + "and model."+ propertyName + " = :propertyValue";
			}
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradasTransacciones> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasTransacciones model where model.idEmpresa = :idEmpresa and estatus = 0 ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				queryString += "and lower(cast(model."+ propertyName + " as string)) LIKE :propertyValue ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradasTransacciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasTransacciones model where model.idEmpresa = :idEmpresa and estatus = 0 ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()){
    			sqlWhere = "and cast(model." + columnName + " as string) IN (";
    			
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = sqlWhere + inFilter + ")";
        	}
        	
        	queryString += sqlWhere;
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", getIdEmpresa());
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
	public List<OperacionesIntegradasTransacciones> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasTransacciones model where model.idEmpresa = :idEmpresa and estatus = 0 ";
		String where = "";
		
		try {
			for (Entry<String, String> e : params.entrySet()) 
				where += "and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "' ";
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradasTransacciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasTransacciones model where model.idEmpresa = :idEmpresa and estatus = 0 ";
		String where = "";
		
		try {
			for (Entry<String, String> e : params.entrySet())
				where += "and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
}