package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.Llaves;

@Stateless
public class LlavesImp extends DAOImpl<Llaves> implements LlavesDAO {
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
		LlavesImp.orderBy = orderBy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Llaves> findByProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from Llaves model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) {
				queryString = queryString + " and model."+ propertyName + " = :propertyValue";
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
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
	public List<Llaves> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from Llaves model where model.idEmpresa = :idEmpresa ";
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
			if (value != null && !"".equals(value.toString()))
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
	public List<Llaves> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		String queryString = "select model from Llaves model where model.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()){
    			sqlWhere = " and cast(model." + columnName + " as string) IN (";
    			
    			for (int i = 0; i < values.size(); i++) {
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
	public List<Llaves> findByProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			String queryString = "select model from Llaves model where model.idEmpresa = :idEmpresa ";
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
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Llaves> findLikeProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			String queryString = "select model from Llaves model where model.idEmpresa = :idEmpresa ";
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
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public boolean comprobarPosicion(Long idLlave, int posicion) throws Exception {
		String queryString = "select model from Llaves model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idLlave == null || idLlave <= 0L)
				idLlave = 0L;
			queryString = queryString + " and model.id <> :idLlave and model.posicion = :posicion";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("idLlave", idLlave);
			query.setParameter("posicion", posicion);
			return (query.getResultList().size() == 0);
		} catch (Exception re) {
			throw re;
		}
	}
}