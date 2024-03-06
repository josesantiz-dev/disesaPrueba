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
	public long save(TransaccionConceptos entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<TransaccionConceptos> saveOrUpdateList(List<TransaccionConceptos> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionConceptos> findAll(long idTransaccion) throws Exception {
		String queryString = "select model from TransaccionConceptos model where model.idTransaccion.id = :idTransaccion ";
		
		try {
			if (this.orderBy != null && ! "".equals(this.orderBy))
				queryString += "order by " + this.orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idTransaccion", idTransaccion);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionConceptos> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from TransaccionConceptos model where model.idTransaccion.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) 
				queryString += "and model."+ propertyName + " = :propertyValue ";
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += "order by " + this.orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionConceptos> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from TransaccionConceptos model where model.idTransaccion.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) 
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				else 
					queryString += "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionConceptos> findInProperty(String propertyName, List<Object> values, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from TransaccionConceptos model where model.idTransaccion.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = " and cast(model." + propertyName + " as string) IN (";
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + propertyName + i;
    			}
        		sqlWhere = sqlWhere + inFilter + ") ";
        	}
        	
        	queryString += sqlWhere;
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) 
        			query.setParameter(propertyName + i, values.get(i).toString());
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
	public List<TransaccionConceptos> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from TransaccionConceptos model where model.idTransaccion.idEmpresa = :idEmpresa ";
		
		try {
			for (Entry<String, String> e : params.entrySet())
				queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "' ";
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionConceptos> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from TransaccionConceptos model where model.idTransaccion.idEmpresa = :idEmpresa ";
		
		try {
			for (Entry<String, String> e : params.entrySet())
				queryString += "and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += "order by " + this.orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/06/2016 | Javier Tirado	| Creacion de TransaccionConceptosImp