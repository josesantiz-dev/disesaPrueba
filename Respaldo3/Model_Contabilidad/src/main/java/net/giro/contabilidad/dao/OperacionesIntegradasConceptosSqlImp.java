package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;

@Stateless
public class OperacionesIntegradasConceptosSqlImp extends DAOImpl<OperacionesIntegradasConceptosSql> implements OperacionesIntegradasConceptosSqlDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		OperacionesIntegradasConceptosSqlImp.orderBy = orderBy;
	}

	@Override
	public long save(OperacionesIntegradasConceptosSql entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> saveOrUpdateList(List<OperacionesIntegradasConceptosSql> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradasConceptosSql> findAll(long idOperacionIntegrada, String orderBy) throws Exception {
		String queryString = "select model from OperacionesIntegradasConceptosSql model where model.idOperacionIntegrada.id = :idOperacionIntegrada ";
		
		try {
			if (orderBy == null || "".equals(orderBy))
				orderBy = "model.id";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idOperacionIntegrada", idOperacionIntegrada);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradasConceptosSql> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasConceptosSql model where model.idOperacionIntegrada.idTransaccion.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) 
				queryString += "and model."+ propertyName + " = :propertyValue ";
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<OperacionesIntegradasConceptosSql> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasConceptosSql model where model.idOperacionIntegrada.idTransaccion.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) 
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				else 
					queryString += " and lower(model."+ propertyName + ") LIKE :propertyValue";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<OperacionesIntegradasConceptosSql> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasConceptosSql model where model.idOperacionIntegrada.idTransaccion.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = " and cast(model." + columnName + " as string) IN (";
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter))
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = sqlWhere + inFilter + ") ";
        	}
        	
        	queryString += sqlWhere;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) 
        			query.setParameter(columnName + i, values.get(i).toString());
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
	public List<OperacionesIntegradasConceptosSql> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasConceptosSql model where model.idOperacionIntegrada.idTransaccion.idEmpresa = :idEmpresa ";
		
		try {
			for (Entry<String, String> e : params.entrySet()) 
				queryString += " and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "' ";
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<OperacionesIntegradasConceptosSql> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasConceptosSql model where model.idOperacionIntegrada.idTransaccion.idEmpresa = :idEmpresa ";
		
		try {
			for (Entry<String, String> e : params.entrySet())
				queryString += " and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "' ";
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasConceptosSqlImp