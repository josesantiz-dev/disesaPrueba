package net.giro.compras.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.compras.beans.Cotizacion;

@Stateless
public class CotizacionImp extends DAOImpl<Cotizacion> implements CotizacionDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	private static Integer estatusId;

	@Override
	public void OrderBy(String orderBy) {
		CotizacionImp.orderBy = orderBy;
	}
	
	@Override
	public void estatus(Integer estatus) {
		CotizacionImp.estatusId = estatus;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findByProperty(String propertyName, Object value, int max) throws Exception {
		String queryString = "select model from Cotizacion model ";
		String where = "";
		
		try {
			if (value != null)
				where = " where model."+ propertyName + " = :propertyValue";
			
			if (estatusId != null) {
				if ("".equals(where))
					where = " where model.estatus = :estatus";
				else
					where += " and model.estatus = :estatus";
			}
			
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		String queryString = "select model from Cotizacion model ";
		String where = "";
		StringBuffer sb = null;
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					where = " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					where = " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (estatusId != null) {
				if ("".equals(where))
					where = " where model.estatus = :estatus";
				else
					where += " and model.estatus = :estatus";
			}
			
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findInProperty(String columnName, List<Object> values) throws Exception {
		String queryString = "select model from Cotizacion model ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if(values != null && ! values.isEmpty()){
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = " WHERE cast(model." + columnName + " as string) IN (" + inFilter + ")";
        	}
			
			if (estatusId != null) {
				if ("".equals(sqlWhere))
					sqlWhere = " where model.estatus = :estatus";
				else
					sqlWhere += " and model.estatus = :estatus";
			}
        	
        	queryString += sqlWhere;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	if(values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor) throws Exception {
		Long consecutivo;
		
		try {
			if (idProveedor <= 0L) 
				return 0;
			
			String queryString = "select (COUNT(movCta.idProveedor) + 1) from Cotizacion movCta " + 
				"where movCta.idProveedor = :propertyValue";
			
			Query query = entityManager.createQuery(queryString, Long.class);
			query.setParameter("propertyValue", idProveedor);
			
			consecutivo = (Long) query.getSingleResult();
			
			if (consecutivo == null)
				consecutivo = 0L;
			
			return consecutivo.intValue();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findByProperties(HashMap<String, Object> params) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Cotizacion model ";
		String whereString = "";
		
		try {
			for(Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " and";
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
			
			if(estatusId != null && whereString.length() > 0)
				whereString += " and estatus = " + estatusId;
			else if(estatusId != null && whereString.length() == 0)
				whereString = " estatus = " + estatusId;
			estatusId = null;
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			orderBy = null;

			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findLikeProperties(HashMap<String, String> params) throws Exception {
		String queryString = "select model from Cotizacion model ";
		String whereString = "";
		
		try {
			for (Entry<String, String> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " and";
				whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if(estatusId != null && whereString.length() > 0)
				whereString += " and estatus = " + estatusId;
			else if(estatusId != null && whereString.length() == 0)
				whereString = "  estatus = " + estatusId;
			estatusId = null;
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			orderBy = null;

			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findByPropertyWithObra(String propertyName, Object value, long idObra, int max) throws Exception {
		String queryString = "select model from Cotizacion model ";
		String where = "";
		
		try {
			if (value != null)
				where = " where model."+ propertyName + " = :propertyValue";
			
			if (estatusId != null)
				where += ("".equals(where.trim()) ? " where model.estatus = :estatus" : " and model.estatus = :estatus");
			if (idObra > 0L)
				where += ("".equals(where.trim()) ? " where model.idObra = :idObra" : " and model.idObra = :idObra"); 
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
			if (idObra > 0L) 
				query.setParameter("idObra", idObra);
			if (max > 0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findLikePropertyWithObra(String propertyName, Object value, long idObra, int max) throws Exception {
		String queryString = "select model from Cotizacion model ";
		String where = "";
		StringBuffer sb = null;
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					where = " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					where = " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (estatusId != null)
				where += ("".equals(where.trim()) ? " where model.estatus = :estatus" : " and model.estatus = :estatus");
			if (idObra > 0L)
				where += ("".equals(where.trim()) ? " where model.idObra = :idObra" : " and model.idObra = :idObra"); 
			
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
			if (idObra > 0L) 
				query.setParameter("idObra", idObra);
			if (max > 0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findInPropertyWithObra(String columnName, List<Object> values, long idObra) throws Exception {
		String queryString = "select model from Cotizacion model ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if(values != null && ! values.isEmpty()){
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = " WHERE cast(model." + columnName + " as string) IN (" + inFilter + ")";
        	}
			
			if (estatusId != null)
				sqlWhere += ("".equals(sqlWhere.trim()) ? " where model.estatus = :estatus" : " and model.estatus = :estatus");
			if (idObra > 0L)
				sqlWhere += ("".equals(sqlWhere.trim()) ? " where model.idObra = :idObra" : " and model.idObra = :idObra"); 
        	queryString += sqlWhere;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	if(values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
			if (idObra > 0L) 
				query.setParameter("idObra", idObra);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			orderBy = null;
			estatusId = null;
		}
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. 
 */