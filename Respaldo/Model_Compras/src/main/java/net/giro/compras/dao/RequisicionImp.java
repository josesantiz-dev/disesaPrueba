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
import net.giro.compras.beans.Requisicion;

@Stateless
public class RequisicionImp extends DAOImpl<Requisicion> implements RequisicionDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	private static Long estatus; 

	@Override
	public void OrderBy(String orderBy) {
		RequisicionImp.orderBy = orderBy;
	}
	
	@Override
	public void estatus(Long estatus) {
		RequisicionImp.estatus = estatus;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findByProperty(String propertyName, Object value, int max) throws Exception {
		String queryString = "select model from Requisicion model ";
		
		try {
			if (value != null) {
				queryString = queryString + " where model."+ propertyName + " = :propertyValue";
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		String queryString = "select model from Requisicion model ";
		StringBuffer sb = null;
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || "idpresupuesto".equals(propertyName.toLowerCase())) {
					queryString += " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					queryString = queryString + " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findLikeProperty(String propertyName, String value, int tipo, int limite) throws Exception {
		String queryString = "select model from Requisicion model ";
		String where = "";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value)) {
				if ("id".equals(propertyName) || "idpresupuesto".equals(propertyName.toLowerCase())) {
					where = " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					where = " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toLowerCase());
	    		sb.append("%");
			}
			
			if (! "".equals(where))
				queryString += where + (tipo > 0 ? " AND model.tipo = " + tipo : "");
			else
				queryString += (tipo > 0 ? " where model.tipo = " + tipo : "");
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findInProperty(String columnName, List<Object> values) throws Exception {
		String queryString = "select model from Requisicion model ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = " WHERE cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = sqlWhere + inFilter + ")";
        	}
        	
        	queryString += sqlWhere;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.id DESC";
        	
        	Query query = entityManager.createQuery(queryString);
        	if(values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
        	
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Requisicion model ";
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
			
			if (estatus != null && whereString.length() > 0)
				whereString += " and estatus = " + estatus;
			else if(estatus != null && whereString.length() == 0)
				whereString = " estatus = " + estatus;
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.id DESC";

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		String queryString = "select model from Requisicion model ";
		String whereString = "";

		try {
			for(Entry<String, String> e : params.entrySet()){
				if (whereString.length() > 0)
					whereString += " and";
				whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if(estatus != null && whereString.length() > 0)
				whereString += " and estatus = " + estatus;
			else if(estatus != null && whereString.length() == 0)
				whereString = "  estatus = " + estatus;
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}	
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-19 | Javier Tirado 	| Añado los metodos estatus, findByProperties y findLikeProperties. 
 */