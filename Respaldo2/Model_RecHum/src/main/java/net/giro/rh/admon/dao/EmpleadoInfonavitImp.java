package net.giro.rh.admon.dao;

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
import net.giro.rh.admon.beans.EmpleadoInfonavit;

@Stateless
public class EmpleadoInfonavitImp extends DAOImpl<EmpleadoInfonavit> implements EmpleadoInfonavitDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public void orderBy(String orderBy) {
		EmpleadoInfonavitImp.orderBy = orderBy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoInfonavit> findByProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from EmpleadoInfonavit model where model.idEmpleado.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) {
				queryString = queryString + " and model."+ propertyName + " = :propertyValue ";
			}
			
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	@SuppressWarnings("unchecked")
	public List<EmpleadoInfonavit> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from EmpleadoInfonavit model where model.idEmpleado.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)) && ! propertyName.contains("."))) {
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					queryString = queryString + " and lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && !"".equals(value.toString()))
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
	@SuppressWarnings("unchecked")
	public List<EmpleadoInfonavit> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		String queryString = "select model from EmpleadoInfonavit model where model.idEmpleado.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if(values != null && ! values.isEmpty()){
    			sqlWhere = " and cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = sqlWhere + inFilter + ")";
        	}
        	
        	queryString += sqlWhere;
			
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
    	} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoInfonavit> findByProperties(HashMap<String, Object> params, int limite) throws Exception{
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
			String queryString = "select model from EmpleadoInfonavit model where model.idEmpleado.idEmpresa = :idEmpresa ";
			String where = "";
			
			for(Entry<String, Object> e : params.entrySet()) {
				if (where.length() > 0)
					where += " and";
				
				if (e.getValue().getClass() == java.util.Date.class) 
					where += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					where += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					where += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
			
			if(! where.isEmpty())
				queryString = queryString + " and " + where;
			
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	@SuppressWarnings("unchecked")
	public List<EmpleadoInfonavit> findLikeProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			String queryString = "select model from EmpleadoInfonavit model where model.idEmpleado.idEmpresa = :idEmpresa ";
			String where = "";
			
			for(Entry<String, String> e : params.entrySet()) {
				if (where.length() > 0)
					where += " and";
				where += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if(! where.isEmpty())
				queryString = queryString + " and " + where;
			
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoInfonavitImp