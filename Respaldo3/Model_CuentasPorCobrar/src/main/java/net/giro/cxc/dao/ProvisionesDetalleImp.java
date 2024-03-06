package net.giro.cxc.dao;

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
import net.giro.cxc.beans.ProvisionesDetalle;

@Stateless
public class ProvisionesDetalleImp extends DAOImpl<ProvisionesDetalle> implements ProvisionesDetalleDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ProvisionesDetalle entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ProvisionesDetalle> saveOrUpdateList(List<ProvisionesDetalle> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<ProvisionesDetalle> findAll(Long idProvisiones) throws Exception {
		return this.findByProperty("idProvision", idProvisiones, 0);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProvisionesDetalle> findByProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from ProvisionesDetalle model ";
		String whereString = "";
		
		try {
			if (value != null)
				whereString = " where model."+ propertyName + " = :propertyValue ";
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			queryString += " order by " + propertyName;
			
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
	public List<ProvisionesDetalle> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from ProvisionesDetalle model ";
		String whereString = "";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "id";
			
			for (Entry<String, Object> e : params.entrySet()) {
				if (! whereString.isEmpty())
					whereString += "and ";
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += "date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += "cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else if (e.getValue().getClass() == java.lang.Double.class) 
					whereString += "cast(model." + e.getKey() + " as string) = '" + ((Double) e.getValue()).toString() + "' ";
				else if (e.getValue().getClass() == java.lang.Integer.class) 
					whereString += "cast(model." + e.getKey() + " as string) = '" + ((Integer) e.getValue()).toString() + "' ";
				else
					whereString += "cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + "where " + whereString;
			queryString += "order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProvisionesDetalle> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		String queryString = "select model from ProvisionesDetalle model ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			value = validateString(value);
			if (value != null && ! "".equals(value.trim())) {
				whereString = "where lower(model."+ propertyName + ") LIKE :propertyValue ";
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2))))
					whereString = "where cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;			
			queryString += "order by " + propertyName;
			
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
	public List<ProvisionesDetalle> findLikeProperties( HashMap<String, String> params, String orderBy, int limite) throws Exception {
		String queryString = "select model from ProvisionesDetalle model ";
		String whereString = "";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "id";
			
			for (Entry<String, String> e : params.entrySet()) {
				if (! whereString.isEmpty())
					whereString += "and";
				whereString += "cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + "where " + whereString;
			queryString += "order by " + orderBy;

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
	public List<ProvisionesDetalle> findInProperty(String propertyName, List<Object> values, int limite) throws Exception {
		String queryString = "select model from ProvisionesDetalle model ";
		String whereString = "";
		String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()) {
    			whereString = "where cast(model." + propertyName + " as string) IN (";
    			
    			for (int i = 0; i < values.size(); i++) {
    				if (! "".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + propertyName + i;
    			}
    			
    			whereString += inFilter + ")";
        	}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			queryString += "order by " + propertyName;
        	
        	Query query = entityManager.createQuery(queryString);
        	if (values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++)
        			query.setParameter(propertyName + i, values.get(i).toString());
        	}
			if (limite > 0)
				query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}

	// ------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------------------
	
	private String validateString(String value) {
		if (value == null || "".equals(value.trim()))
			return "";
		
		value = value.trim().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");
		value = value.trim().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
		value = value.trim().replace("Ä", "A").replace("Ë", "E").replace("Ï", "I").replace("Ö", "O").replace("Ü", "U");
		value = value.trim().replace("ä", "a").replace("ë", "e").replace("ï", "i").replace("ö", "o").replace("ü", "u");
		
		return value;
	}
}

/*
 * HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN
 *  ----------------------------------------------------------------------------------------------------------------
 *    2.1	| 28/09/2017 | Javier Tirado	| Creacion de ProvisionesDetalleImp
 */