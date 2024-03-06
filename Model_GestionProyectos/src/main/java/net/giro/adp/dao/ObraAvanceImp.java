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
import net.giro.adp.beans.ObraAvance;

@Stateless
public class ObraAvanceImp extends DAOImpl<ObraAvance> implements ObraAvanceDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	
	@Override
	public void orderBy(String orderBy) {
		ObraAvanceImp.orderBy = orderBy;
	}

	@Override
	public long save(ObraAvance entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ObraAvance> saveOrUpdateList(List<ObraAvance> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraAvance> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ObraAvance model where model.idObra.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null) {
				queryString = queryString + " and model."+ propertyName + " = :propertyValue";
			}
			
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
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
	public List<ObraAvance> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ObraAvance model where model.idObra.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					queryString = queryString + " and lower(model."+ propertyName + ") LIKE :propertyValue";
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
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
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
	public List<ObraAvance> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ObraAvance model where model.idObra.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
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
	public List<ObraAvance> findByProperties(HashMap<String, Object> params, long idEmpresa, int limite) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from ObraAvance model where model.idObra.idEmpresa = :idEmpresa ";
		String where = "";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
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
	public List<ObraAvance> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception{
		String queryString = "select model from ObraAvance model where model.idObra.idEmpresa = :idEmpresa ";
		String where = "";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			for(Entry<String, String> e : params.entrySet()){
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
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 14/06/2016 | Javier Tirado	| Creacion de ObraAvanceImp