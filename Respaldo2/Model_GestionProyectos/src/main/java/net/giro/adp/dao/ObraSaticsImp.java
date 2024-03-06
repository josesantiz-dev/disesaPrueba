package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.ObraSatics;

@Stateless
public class ObraSaticsImp extends DAOImpl<ObraSatics> implements ObraSaticsDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	private Long idEmpresa;

	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public void orderBy(String orderBy) {
		ObraSaticsImp.orderBy = orderBy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraSatics> findByProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from ObraSatics model where model.idObra.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) {
				queryString = queryString + " and model."+ propertyName + " = :propertyValue";
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	public List<ObraSatics> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from ObraSatics model where model.idObra.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					queryString = queryString + " and lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	public List<ObraSatics> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		String queryString = "select model from ObraSatics model where model.idObra.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if(values != null && ! values.isEmpty()){
    			sqlWhere = " and cast(model." + columnName + " as string) IN (";
    			
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
	public List<ObraSatics> findByProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			String queryString = "select model from ObraSatics model where model.idObra.idEmpresa = :idEmpresa ";
			String where = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if(where.length() > 0)
					where += " and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "'";
				else
					where += " and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "'";
			}
			
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	public List<ObraSatics> findLikeProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			String queryString = "select model from ObraSatics model where model.idObra.idEmpresa = :idEmpresa ";
			String where = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if(where.length() > 0)
					where += " and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
				else
					where += " and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|23/05/2016		|Javier Tirado	|Creando la implementacion ObraSaticsImp