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
import net.giro.compras.beans.OrdenCompra;

@Stateless
public class OrdenCompraImp extends DAOImpl<OrdenCompra> implements OrdenCompraDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private String queryString;
	private String whereString;
	private static String orderBy;
	private static Long estatus;

	@Override
	public void OrderBy(String orderBy) {
		OrdenCompraImp.orderBy = orderBy;
	}

	@Override
	public void estatus(Long estatus) {
		OrdenCompraImp.estatus = estatus;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.queryString = "select model from OrdenCompra model ";
			this.whereString = "";
			
			if (value != null) {
				this.whereString = " where model."+ propertyName + " = :propertyValue";
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " where estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
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
			estatus = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		StringBuffer sb = null;
		
		try {
			this.queryString = "select model from OrdenCompra model ";
			this.whereString = "";
			
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					this.whereString = " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					this.whereString = " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " where estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
		
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findNoCompletas(String propertyName, Object value, int max) throws Exception {
		StringBuffer sb = null;
		
		try {
			this.queryString = "select model from OrdenCompra model where model.completa = 0";
			this.whereString = "";
			
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					this.whereString = " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					this.whereString = " and lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " where estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;	
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
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
	public List<OrdenCompra> findInProperty(String columnName, List<Object> values) throws Exception {
    	String inFilter = "";
    	
    	try {
    		this.queryString = "select model from OrdenCompra model ";
			this.whereString = "";
    		
    		if(values != null && ! values.isEmpty()){
    			this.whereString = " WHERE cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
    			this.whereString = this.whereString + inFilter + ")";
        	}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " where estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model." + columnName;
        	
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
			estatus = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findByProperties(HashMap<String, Object> params, int limite) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			this.queryString = "select model from OrdenCompra model ";
			this.whereString = "";
			
			for(Entry<String, Object> e : params.entrySet()) {
				if (this.whereString.length() > 0)
					this.whereString += " and";
				
				if (e.getValue().getClass() == java.util.Date.class) 
					this.whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					this.whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					this.whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			
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

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findLikeProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			this.queryString = "select model from OrdenCompra model ";
			this.whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (this.whereString.length() > 0)
					this.whereString += " and";
				this.whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = "  estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by id desc";

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
	public int findConsecutivoByProveedor(long idProveedor) throws Exception {
		Long consecutivo;
		
		try {
			this.queryString = "select (COUNT(movCta.idProveedor) + 1) from OrdenCompra movCta " + 
				"where movCta.idProveedor = :propertyValue";
			
			if (idProveedor <= 0L) 
				return 0;
			
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
}
