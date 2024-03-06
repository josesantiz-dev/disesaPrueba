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
import net.giro.contabilidad.beans.Transacciones;

@Stateless
public class OperacionesIntegradasTransaccionesImp extends DAOImpl<OperacionesIntegradasTransacciones> implements OperacionesIntegradasTransaccionesDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		OperacionesIntegradasTransaccionesImp.orderBy = orderBy;
	}

	@Override
	public long save(OperacionesIntegradasTransacciones entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradasTransacciones> saveOrUpdateList(List<OperacionesIntegradasTransacciones> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasTransacciones findByTransaccion(Transacciones transaccion) throws Exception {
		List<OperacionesIntegradasTransacciones> resultado = null;
		
		try {
			resultado = this.findByProperty("model.idTransaccion", transaccion, transaccion.getIdEmpresa(), 0);
			if (resultado == null || resultado.isEmpty())
				return null;
			return resultado.get(0);
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	public OperacionesIntegradasTransacciones findByCodigoTransaccion(long codigoTransaccion, long idEmpresa) throws Exception {
		List<OperacionesIntegradasTransacciones> resultado = null;
		
		try {
			resultado = this.findByProperty("idTransaccion.codigo", codigoTransaccion, idEmpresa, 0);
			if (resultado == null || resultado.isEmpty())
				return null;
			return resultado.get(0);
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OperacionesIntegradasTransacciones> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasTransacciones model where model.idTransaccion.idEmpresa = :idEmpresa and estatus = 0 ";
		
		try {
			if (value != null)
				queryString += "and model."+ propertyName + " = :propertyValue ";
			if (orderBy != null && ! "".equals(orderBy))
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
	public List<OperacionesIntegradasTransacciones> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasTransacciones model where model.idTransaccion.idEmpresa = :idEmpresa and estatus = 0 ";
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
	public List<OperacionesIntegradasTransacciones> findInProperty(String propertyName, List<Object> values, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasTransacciones model where model.idTransaccion.idEmpresa = :idEmpresa and estatus = 0 ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()){
    			sqlWhere = "and cast(model." + propertyName + " as string) IN (";
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + propertyName + i;
    			}
    			
        		sqlWhere = sqlWhere + inFilter + ") ";
        	}
        	queryString += sqlWhere;
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) 
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
	public List<OperacionesIntegradasTransacciones> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasTransacciones model where model.idTransaccion.idEmpresa = :idEmpresa and estatus = 0 ";
		String where = "";
		
		try {
			for (Entry<String, String> e : params.entrySet()) 
				where += "and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "' ";
			queryString += where;
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
	public List<OperacionesIntegradasTransacciones> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from OperacionesIntegradasTransacciones model where model.idTransaccion.idEmpresa = :idEmpresa and estatus = 0 ";
		
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
}