package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.Transacciones;

@Stateless
public class TransaccionesImp extends DAOImpl<Transacciones> implements TransaccionesDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	
	@Override
	public void orderBy(String orderBy) {
		TransaccionesImp.orderBy = orderBy;
	}
	
	@Override
	public long save(Transacciones entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<Transacciones> saveOrUpdateList(List<Transacciones> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Transacciones findByCodigo(long codigoTransaccion, long idEmpresa) throws Exception {
		String queryString = "select model from Transacciones model where model.idEmpresa = :idEmpresa and model.codigo = :codigoTransaccion ";
		
		try {
			if (codigoTransaccion <= 0L)
				codigoTransaccion = 0L;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("codigoTransaccion", codigoTransaccion);
			return (Transacciones) query.getSingleResult();
		} catch (Exception re) {
			throw re;
		} 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Transacciones> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Transacciones model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) 
				queryString += "and model."+ propertyName + " = :propertyValue ";
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
	public List<Transacciones> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Transacciones model inner join fetch model.idOperacion op where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || "codigo".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) 
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				else 
					queryString += "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				
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
	public List<Transacciones> findInProperty(String propertyName, List<Object> values, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Transacciones model where model.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if(values != null && ! values.isEmpty()) {
    			sqlWhere = "and cast(model." + propertyName + " as string) IN (";
    			for (int i = 0; i < values.size(); i++) {
    				if (! "".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + propertyName + i;
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
        			query.setParameter(propertyName + i, values.get(i).toString());
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
	public List<Transacciones> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Transacciones model where model.idEmpresa = :idEmpresa ";
		
		try {
			for (Entry<String, String> e : params.entrySet()) 
				queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "' ";
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
	public List<Transacciones> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Transacciones model where model.idEmpresa = :idEmpresa ";
		
		try {
			for (Entry<String, String> e : params.entrySet())
				queryString += "and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
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
	public boolean comprobarCodigo(long idTransaccion, long codigo, long idEmpresa) throws Exception {
		String queryString = "select model from Transacciones model where model.idEmpresa = :idEmpresa and model.id <> :idTransaccion and model.codigo = :codigo ";
		
		try {
			if (idTransaccion <= 0L)
				idTransaccion = 0L;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idTransaccion", idTransaccion);
			query.setParameter("codigo", codigo);
			return (query.getResultList().size() == 0);
		} catch (Exception re) {
			throw re;
		}
	}
}
