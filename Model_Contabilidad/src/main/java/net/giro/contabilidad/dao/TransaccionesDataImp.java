package net.giro.contabilidad.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.TransaccionesData;

@Stateless
public class TransaccionesDataImp extends DAOImpl<TransaccionesData> implements TransaccionesDataDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		TransaccionesDataImp.orderBy = orderBy;
	}

	@Override
	public long save(TransaccionesData entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<TransaccionesData> saveOrUpdateList( List<TransaccionesData> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionesData> findAll(long idMensajeTransaccion, String orderBy) throws Exception {
		String queryString = "select model from TransaccionesData model where model.idMensajeTransaccion = :idMensajeTransaccion ";
		
		try {
			if (orderBy != null && ! "".equals(orderBy))
				queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idMensajeTransaccion", idMensajeTransaccion);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionesData> findByProperty(String propertyName, Object value, long idEmpesa, int limite) throws Exception {
		String queryString = "select model from TransaccionesData model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) 
				queryString += "and model."+ propertyName + " = :propertyValue ";
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpesa);
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
	public List<TransaccionesData> findLikeProperty(String propertyName, Object value, long idEmpesa, int limite) throws Exception {
		String queryString = "select model from TransaccionesData model where model.idEmpresa = :idEmpresa ";
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
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpesa);
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
	public List<TransaccionesData> findPrevio(Long codigoTransaccion, Long poliza, Long lote) throws Exception {
		String queryString = "select model from TransaccionesData model where model.codigoTransaccion = :codigoTransaccion and model.poliza = :poliza and model.lote = :lote and model.contabilizado = 0 ";
		
		try {
			if (codigoTransaccion == null || codigoTransaccion <= 0L)
				codigoTransaccion = 0L;
			if (poliza == null || poliza <= 0L)
				poliza = 0L;
			if (lote == null || lote <= 0L)
				lote = 0L;
			if (orderBy != null && ! "".equals(orderBy))
				queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("codigoTransaccion", codigoTransaccion);
			query.setParameter("poliza", poliza);
			query.setParameter("lote", lote);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionesData> findInProperty(String propertyName, List<Object> values, long idEmpesa, int limite) throws Exception {
		String queryString = "select model from TransaccionesData model where model.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = "and cast(model." + propertyName + " as string) IN (";
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + propertyName + i;
    			}
        		sqlWhere = sqlWhere + inFilter + ") ";
        	}
        	queryString += sqlWhere;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpesa);
        	if(values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(propertyName + i, values.get(i).toString());
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

	/*@Override
	@SuppressWarnings("unchecked")
	public List<TransaccionesData> findByProperties(HashMap<String, String> params, long idEmpesa, int limite) throws Exception {
		String queryString = "select model from TransaccionesData model where model.idEmpresa = :idEmpresa ";
		
		try { 
			for (Entry<String, String> e : params.entrySet()) 
				queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "' ";
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpesa);
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
	public List<TransaccionesData> findLikeProperties(HashMap<String, String> params, long idEmpesa, int limite) throws Exception {
		String queryString = "select model from TransaccionesData model where model.idEmpresa = :idEmpresa ";
		
		try {
			for (Entry<String, String> e : params.entrySet())
				queryString += "and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpesa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}*/
}