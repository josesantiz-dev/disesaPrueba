package net.giro.plataforma.dao;

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
import net.giro.plataforma.beans.SubfamiliaImpuestos;

@Stateless
public class SubfamiliaImpuestosImp extends DAOImpl<SubfamiliaImpuestos> implements SubfamiliaImpuestosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private String queryString;
	private String whereString;
	private String orderBy;

	@Override
	@SuppressWarnings("unchecked")
	public List<SubfamiliaImpuestos> findByProperty(String propertyName, Object value, int limite) throws RuntimeException {
		try {
			this.queryString = "select model from SubfamiliaImpuestos model ";
			this.whereString = "";
			
			if (value != null) {
				this.whereString = " where model."+ propertyName + " = :propertyValue";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<SubfamiliaImpuestos> findByProperties(HashMap<String, Object> params, int limite) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			this.queryString = "select model from SubfamiliaImpuestos model ";
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
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SubfamiliaImpuestos> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		StringBuffer sb = null;
		
		try {
			this.queryString = "select model from SubfamiliaImpuestos model ";
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
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;			
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SubfamiliaImpuestos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			this.queryString = "select model from SubfamiliaImpuestos model ";
			this.whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (this.whereString.length() > 0)
					this.whereString += " and";
				this.whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<SubfamiliaImpuestos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		String inFilter = "";
		
		try {
			this.queryString = "select model from SubfamiliaImpuestos model ";
			this.whereString = "";
			
			if(values != null && ! values.isEmpty()){
				this.whereString = " WHERE cast(model." + columnName + " as string) IN (";
				
				for(int i = 0; i < values.size(); i++) {
					if (!"".equals(inFilter)) inFilter += ",";
					inFilter += ":" + columnName + i;
				}
				
				this.whereString = this.whereString + inFilter + ")";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;
			
			Query query = entityManager.createQuery(queryString);
			if(values != null && ! values.isEmpty()) {
				for(int i = 0; i < values.size(); i++) {
					query.setParameter(columnName + i, values.get(i).toString());
				}
			}
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
}
