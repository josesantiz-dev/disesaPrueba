package net.giro.rh.admon.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.rh.admon.beans.EmpleadoNomina;

@Stateless
public class EmpleadoNominaImp extends DAOImpl<EmpleadoNomina> implements EmpleadoNominaDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private String queryString;
	private String whereString;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		EmpleadoNominaImp.orderBy = orderBy;
	}

	@Override
	public long save(EmpleadoNomina entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoNomina> saveOrUpdateList(List<EmpleadoNomina> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoNomina> deleteAll(List<EmpleadoNomina> entities) throws Exception {
		List<EmpleadoNomina> deleted = null;
		
		try {
			deleted = super.deleteAll(entities);
			if (deleted != null && ! deleted.isEmpty()) {
				for (EmpleadoNomina delete : deleted)
					entities.remove(delete);
			}
			return entities;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoNomina> findByProperty(String propertyName, Object value, long idEmpresa, int max) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		boolean hasValue = false;
		
		try {
			this.queryString = "select model from EmpleadoNomina model where model.idEmpresa = :idEmpresa ";
			this.whereString = "";
			
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					this.whereString = " and date(model."+ propertyName + ") = date('" + formateador.format((Date) value) + "')";
					hasValue = true;
				} else {
					this.whereString = " and model."+ propertyName + " = :propertyValue";
				}
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! hasValue)
				query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoNomina> findLikeProperty(String propertyName, Object value, long idEmpresa, int max) throws Exception {
		StringBuffer sb = null;
		
		try {
			this.queryString = "select model from EmpleadoNomina model where model.idEmpresa = :idEmpresa ";
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
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoNomina> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception {
    	String inFilter = "";
    	
    	try {
    		this.queryString = "select model from EmpleadoNomina model where model.idEmpresa = :idEmpresa ";
			this.whereString = "";
    		
    		if(values != null && ! values.isEmpty()){
    			this.whereString = " and cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
    			this.whereString = this.whereString + inFilter + ")";
        	}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<EmpleadoNomina> findByProperties(HashMap<String, Object> params, long idEmpresa, int limite) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			this.queryString = "select model from EmpleadoNomina model where model.idEmpresa = :idEmpresa ";
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
				this.queryString = this.queryString + " and " + this.whereString;
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<EmpleadoNomina> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception{
		try {
			this.queryString = "select model from EmpleadoNomina model where model.idEmpresa = :idEmpresa ";
			this.whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (this.whereString.length() > 0)
					this.whereString += " or";
				this.whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " and (" + this.whereString + ") ";
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<EmpleadoNomina> findByDates(Date fechaDesde, Date fechaHasta, long idEmpresa) throws Exception {
		String queryString = "select model from EmpleadoNomina model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (fechaDesde == null || fechaHasta == null)
				return new ArrayList<EmpleadoNomina>();
			if (fechaDesde.after(fechaHasta))
				return new ArrayList<EmpleadoNomina>();
			
			queryString += "and DATE(model.fecha) between DATE(:fechaDesde) and DATE(:fechaHasta)";
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("fechaDesde", formatter.format(fechaDesde));
			query.setParameter("fechaHasta", formatter.format(fechaHasta));
			return query.getResultList();
		} catch (Exception re) {
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
//	  2.1	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaImp