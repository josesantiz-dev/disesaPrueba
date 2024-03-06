package net.giro.rh.admon.dao;

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
import net.giro.rh.admon.beans.Checador;

@Stateless
public class ChecadorImp extends DAOImpl<Checador> implements ChecadorDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		ChecadorImp.orderBy = orderBy;
	}

	@Override
	public long save(Checador entity, Long idEmpresa) throws Exception {
		try {
			return super.save(entity, idEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Checador> saveOrUpdateList(List<Checador> entities, Long idEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, idEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Checador> findByProperty(String propertyName, Object value, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null)
				queryString += "and model."+ propertyName + " = :propertyValue ";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			
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
	public List<Checador> findLikeProperty(String propertyName, String value, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null && ! "".equals(value.toString().trim())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					queryString += "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString().trim()))
				query.setParameter("propertyValue", sb.toString().trim().toLowerCase());
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
	public List<Checador> findInProperty(String columnName, List<Object> values, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (idEmpresa == null || idEmpresa <= 0)
    			idEmpresa = 1L;
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = "and lower(cast(model." + columnName + " as string)) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (! "".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = sqlWhere + inFilter + ") ";
            	queryString += sqlWhere;
        	}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++)
        			query.setParameter(columnName + i, values.get(i).toString().toLowerCase());
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
	public List<Checador> findByProperties(HashMap<String, String> params, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		String where = "";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (params != null && ! params.isEmpty()) {
				for (Entry<String, String> e : params.entrySet())
					where += "where lower(cast(model." + e.getKey() + " as string)) = '" + e.getValue().toLowerCase() + "' ";
				queryString += where;
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

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
	public List<Checador> findLikeProperties(HashMap<String, String> params, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		String where = "";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (params != null && ! params.isEmpty()) {
				for (Entry<String, String> e : params.entrySet()) {
					if (where.length() > 0)
						where += "or ";
					where += "lower(cast(model." + e.getKey() + " as string)) like '%" + e.getValue().toLowerCase() + "%' ";
				}
				
				queryString += "(" + where + ") ";
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

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
	public List<Checador> findByDate(Date fecha, String obra, Long idEmpresa) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (fecha == null)
				return new ArrayList<Checador>();
				
			queryString += "and DATE(model.fecha) = DATE('" + formatter.format(fecha) + "') ";
			if (obra != null && "".equals(obra.trim()))
				queryString += "and (cast(model.idObra as string) like '%" + obra.trim().toLowerCase() + "%' or lower(model.nombreObra) like '%" + obra.trim().toLowerCase() + "%') ";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by CASE model.estatus WHEN 0 THEN 0 WHEN 1 THEN 3 WHEN 2 THEN 1 ELSE 4 END, " + orderBy;
			else
				queryString += " order by CASE model.estatus WHEN 0 THEN 0 WHEN 1 THEN 3 WHEN 2 THEN 1 ELSE 4 END, model.fecha desc, model.nombreObra";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta, Long idEmpresa) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (fechaDesde == null || fechaHasta == null)
				return new ArrayList<Checador>();
			
			if (fechaDesde.after(fechaHasta))
				return new ArrayList<Checador>();
				
			queryString = queryString + "and DATE(model.fecha) BETWEEN DATE('" + formatter.format(fechaDesde) + "') AND DATE('" + formatter.format(fechaHasta) + "')";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
}
