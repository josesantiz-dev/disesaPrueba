package net.giro.contabilidad.dao;

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
import net.giro.contabilidad.beans.MensajeTransaccion;

@Stateless
public class MensajeTransaccionImp extends DAOImpl<MensajeTransaccion> implements MensajeTransaccionDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private String queryString;
	private String whereString;
	private static String orderBy;
	@SuppressWarnings("unused")
	private static Integer limite;

	@Override
	public void limite(Integer limite) { 
		MensajeTransaccionImp.limite = limite; 
	}

	@Override
	public void orderBy(String orderBy) { 
		MensajeTransaccionImp.orderBy = orderBy; 
	}

	/*@Override
	public void estatus(Long estatus) { this.estatus = estatus; }*/

	@Override
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value) throws RuntimeException {
		return this.findByProperty(propertyName, value, 0);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value, int limite) throws RuntimeException {
		try {
			this.queryString = "select model from MensajeTransaccion model ";
			this.whereString = "";
			
			if (value != null) {
				this.whereString = " where model."+ propertyName + " = :propertyValue";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				this.queryString += " order by " + orderBy;
			else
				this.queryString += " order by " + propertyName;
			
			Query query = entityManager.createQuery(this.queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params) throws Exception {
		return this.findByProperties(params, 0);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			this.queryString = "select model from MensajeTransaccion model ";
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
			
			if (orderBy != null && !"".equals(orderBy))
				this.queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(this.queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<MensajeTransaccion> findLikeProperty(String propertyName, String value) throws Exception {
		return this.findLikeProperty(propertyName, value, 0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MensajeTransaccion> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		StringBuffer sb = null;
		
		try {
			this.queryString = "select model from MensajeTransaccion model ";
			this.whereString = "";
			
			if(value != null && ! "".equals(value.trim())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					this.whereString = " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					this.whereString = " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				this.queryString += " order by " + orderBy;
			else
				this.queryString += " order by " + propertyName;
			
			Query query = entityManager.createQuery(this.queryString);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params) throws Exception {
		return this.findLikeProperties(params, 0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.queryString = "select model from MensajeTransaccion model ";
			this.whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (this.whereString.length() > 0)
					this.whereString += " and";
				this.whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				this.queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(this.queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values) throws Exception {
    	return this.findInProperty(columnName, values, 0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MensajeTransaccion> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		String inFilter = "";
    	
    	try {
    		this.queryString = "select model from MensajeTransaccion model ";
			this.whereString = "";
    		
    		if (values != null && ! values.isEmpty()) {
    			for(int i = 0; i < values.size(); i++) {
    				if (! "".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}

    			this.whereString += " WHERE cast(model." + columnName + " as string) IN (" + inFilter + ")";
        	}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				this.queryString += " order by " + orderBy;
			else
				this.queryString += " order by " + columnName;
        	
        	Query query = entityManager.createQuery(this.queryString);
        	if (values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
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

	@Override
	public MensajeTransaccion comprobarMensajeTransaccion(Long idTransaccion, Long idOperacion) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<MensajeTransaccion> lista = null;
		MensajeTransaccion resultado = null;
		
		try {
			if (idTransaccion == null || idTransaccion <= 0L)
				return null;
			
			if (idOperacion == null || idOperacion <= 0L)
				return null;
			
			params.put("idTransaccion", idTransaccion);
			params.put("idOperacion", idOperacion);
			
			lista = this.findByProperties(params);
			if (lista != null && ! lista.isEmpty()) {
				for (MensajeTransaccion var : lista) {
					if (var.getEstatus() == 0 || var.getEstatus() == 1) {
						resultado = var;
						break;
					}
				}
			}
			
			return resultado;
		} catch (Exception re) {
			throw re;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 14/07/2016 | Javier Tirado	| Creacion de MensajeTransaccionImp