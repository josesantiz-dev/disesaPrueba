package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.compras.beans.Comparativa;
import net.giro.compras.comun.ExcepConstraint;

@Stateless
public class ComparativaImp extends DAOImpl<Comparativa> implements ComparativaDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void OrderBy(String orderBy) {
		ComparativaImp.orderBy = orderBy;
	}

	@Override
	public long save(Comparativa entity, Long idEmpresa) throws ExcepConstraint {
		try {
			return super.save(entity, idEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Comparativa> saveOrUpdateList(List<Comparativa> entities, Long idEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, idEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Comparativa> findByProperty(String propertyName, Object value, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Comparativa model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null) {
				queryString = queryString + "and model."+ propertyName + " = :propertyValue ";
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<Comparativa> findLikeProperty(String propertyName, Object value, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Comparativa model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null && !"".equals(value.toString())) {
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
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Comparativa> findInProperty(String columnName, List<Object> values, Long idEmpresa) throws Exception {
		String queryString = "select model from Comparativa model where model.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (idEmpresa == null || idEmpresa <= 0)
    			idEmpresa = 1L;
    		if (values != null && ! values.isEmpty()){
    			sqlWhere = "and cast(model." + columnName + " as string) IN (";
    			
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = sqlWhere + inFilter + ")";
        	}
        	
        	queryString += sqlWhere;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			orderBy = null;
		}
	}
}
