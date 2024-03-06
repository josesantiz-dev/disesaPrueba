package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.OperacionesIntegradasSql;

@Stateless
public class OperacionesIntegradasSqlImp extends DAOImpl<OperacionesIntegradasSql> implements OperacionesIntegradasSqlDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		OperacionesIntegradasSqlImp.orderBy = orderBy;
	}

	@Override
	public long save(OperacionesIntegradasSql entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradasSql> saveOrUpdateList(List<OperacionesIntegradasSql> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradasSql> findAll(long idOperacionIntegrada, String orderBy) throws Exception {
		String queryString = "select model from OperacionesIntegradasSql model where model.idOperacionIntegrada.id = :idOperacionIntegrada ";
		
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
	public List<OperacionesIntegradasSql> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasSql model where model.idOperacionIntegrada.idTransaccion.idEmpresa = :idEmpresa and model.idOperacionIntegrada.estatus = 0";
		
		try {
			if (value != null) 
				queryString = queryString + " and model."+ propertyName + " = :propertyValue ";
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
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
	public List<OperacionesIntegradasSql> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasSql model where model.idOperacionIntegrada.idTransaccion.idEmpresa = :idEmpresa and model.idOperacionIntegrada.estatus = 0";
		StringBuffer sb = null;
		
		try {
			if (value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2))))
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				else
					queryString += " and lower(model."+ propertyName + ") LIKE :propertyValue ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<OperacionesIntegradasSql> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasSql model where model.idOperacionIntegrada.idTransaccion.idEmpresa = :idEmpresa and model.idOperacionIntegrada.estatus = 0";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()){
    			sqlWhere = " and cast(model." + columnName + " as string) IN (";
    			for (int i = 0; i < values.size(); i++) {
    				if (! "".equals(inFilter)) 
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
	public List<OperacionesIntegradasSql> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasSql model where model.idOperacionIntegrada.idTransaccion.idEmpresa = :idEmpresa and model.idOperacionIntegrada.estatus = 0";
		
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
	public List<OperacionesIntegradasSql> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasSql model where model.idOperacionIntegrada.idTransaccion.idEmpresa = :idEmpresa and model.idOperacionIntegrada.estatus = 0";
		
		try {
			for (Entry<String, String> e : params.entrySet())
				queryString += " and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
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
//	  2.1	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasSqlImp