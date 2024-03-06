package net.giro.clientes.dao;

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
import net.giro.clientes.beans.PersonaNegocioVista;

@Stateless
public class PersonaNegocioVistaImp extends DAOImpl<PersonaNegocioVista> implements PersonaNegocioVistaDAO {
	@PersistenceContext
	private EntityManager PersonaNegocioVistaManager;
	private String queryString;
	private String whereString;

	@Override
	@SuppressWarnings("unchecked")
	public List<PersonaNegocioVista> findAll(Long estatus, int limite) throws Exception {
		try {
			this.queryString = "select model from PersonaNegocioVista model ";
			this.whereString = "";
			
			if (estatus != null) {
				this.whereString = " where model.estatus = "+ estatus;
			}
			
			Query query = PersonaNegocioVistaManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PersonaNegocioVista> findByProperty(String propertyName, Object value, Long estatus, int limite) throws RuntimeException {
		try {
			this.queryString = "select model from PersonaNegocioVista model ";
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
			queryString += " order by model." + propertyName;
			
			Query query = PersonaNegocioVistaManager.createQuery(queryString);
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
	public List<PersonaNegocioVista> findByProperties(HashMap<String, Object> params, Long estatus, int limite) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String orderBy = "";
		
		try {
			this.queryString = "select model from PersonaNegocioVista model ";
			this.whereString = "";
			
			for(Entry<String, Object> e : params.entrySet()) {
				if (this.whereString.length() > 0) {
					this.whereString += " and";
					orderBy += ",";
				}
				
				if (e.getValue().getClass() == java.util.Date.class) 
					this.whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					this.whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					this.whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
				orderBy += "model." + e.getKey();
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			if (! "".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = PersonaNegocioVistaManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PersonaNegocioVista> findLikeProperty(String propertyName, Object value, Long estatus, int limite) throws Exception {
		StringBuffer sb = null;
		
		try {
			this.queryString = "select model from PersonaNegocioVista model ";
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
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;			
			queryString += " order by model." + propertyName;
			
			Query query = PersonaNegocioVistaManager.createQuery(queryString);
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
	public List<PersonaNegocioVista> findLikeProperties(HashMap<String, String> params, Long estatus, int limite) throws Exception{
		String orderBy = "";
		
		try {
			this.queryString = "select model from PersonaNegocioVista model ";
			this.whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (this.whereString.length() > 0) {
					this.whereString += " and";
					orderBy += ",";
				}
				
				this.whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
				orderBy += "model." + e.getKey();
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and model.estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " model.estatus = " + estatus;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			
			if (! "".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = PersonaNegocioVistaManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			limite = 0;

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PersonaNegocioVista> findInProperty(String columnName, List<Object> values, Long estatus, int limite) throws Exception {
    	String inFilter = "";
    	
    	try {
    		this.queryString = "select model from PersonaNegocioVista model ";
			this.whereString = "";
    		
    		if(values != null && ! values.isEmpty()){
    			this.whereString = " WHERE cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
    			this.whereString = this.whereString + inFilter + ")";
        	}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " where estatus = " + estatus;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			queryString += " order by model." + columnName;
        	
        	Query query = PersonaNegocioVistaManager.createQuery(queryString);
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

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 24/04/2017 | Javier Tirado	| Creacion de PersonaNegocioVistaImp