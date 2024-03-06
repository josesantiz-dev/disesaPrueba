package net.giro.adp.dao;

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
import net.giro.adp.beans.Insumos;

@Stateless
public class InsumosImp extends DAOImpl<Insumos> implements InsumosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findByProperty(String propertyName, Object value, int max) {
		String queryString = "select model from Insumos model ";
		
		try {
			if (value != null) {
				queryString = queryString + " where model."+ propertyName + " = :propertyValue";
			}
			
			queryString += " order by id desc";
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
		
	@SuppressWarnings("unchecked")
	public List<Insumos> findByActivos(int max) {
		String queryString = "select model from Insumos model where model.estatus = 0  order by id desc";
		
		try {
			
			Query query = entityManager.createQuery(queryString);
			
			if (max > 0)
				query.setMaxResults(max);
			
			return query.getResultList();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findLikeProperty(String propertyName, Object value, int max) {
		String queryString = "select model from Insumos model ";
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
			
			queryString += " order by estatus, id desc";
			
			Query query = entityManager.createQuery(queryString);
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> comprobarInsumos(Long idObra) {
		String queryString = "select model from Insumos model ";
		
		try {
			if (idObra != null) {
				queryString = queryString + " where model.idObra = :propertyValue and model.estatus = 0";
			}
			
			queryString += " order by id desc";
			
			Query query = entityManager.createQuery(queryString);
			if (idObra != null)
				query.setParameter("propertyValue", idObra);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findByProperties(HashMap<String, Object> params) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "";
		String whereString = "";
		
		try {
			queryString = "select model from Entity model ";
			whereString = "";
			
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
			
			/*if(this.estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + this.estatus;
			else if(this.estatus != null && this.whereString.length() == 0)
				this.whereString = " estatus = " + this.estatus;
			this.estatus = null;*/
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			/*if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;*/

			Query query = entityManager.createQuery(queryString);
			/*if (this.limite > 0)
				query.setMaxResults(this.limite);
			this.limite = 0;*/

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findLikeProperties(HashMap<String, String> params) throws Exception {
		String queryString = "";
		String whereString = "";
		
		try {
			queryString = "select model from Insumos model ";
			whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (whereString.length() > 0)
					whereString += " and";
				whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			/*if(this.estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + this.estatus;
			else if(this.estatus != null && this.whereString.length() == 0)
				this.whereString = "  estatus = " + this.estatus;
			this.estatus = null;*/
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			/*if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;*/

			Query query = entityManager.createQuery(queryString);
			/*if (this.limite > 0)
				query.setMaxResults(this.limite);
			this.limite = 0;*/

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findInProperty(String columnName, List<Object> values) throws Exception {
		String queryString = "";
		String whereString = "";
		String inFilter = "";
    	
    	try {
    		queryString = "select model from Insumos model ";
			whereString = "";
    		
    		if(values != null && ! values.isEmpty()){
    			whereString = " WHERE cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
    			whereString = whereString + inFilter + ")";
        	}
			
			/*if(this.estatus != null && whereString.length() > 0)
				whereString += " and estatus = " + this.estatus;
			else if(this.estatus != null && whereString.length() == 0)
				whereString = " where estatus = " + this.estatus;
			this.estatus = null;*/
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			/*if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;*/

        	Query query = entityManager.createQuery(queryString);
        	if(values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
			/*if (this.limite > 0)
				query.setMaxResults(this.limite);
			this.limite = 0;*/
        	
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}
}
