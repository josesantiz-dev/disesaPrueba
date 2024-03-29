package net.giro.contabilidad.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.AsignacionGrupos;

@Stateless
public class AsignacionGruposImp extends DAOImpl<AsignacionGrupos> implements AsignacionGruposDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		AsignacionGruposImp.orderBy = orderBy;
	}

	@Override
	public long save(AsignacionGrupos entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<AsignacionGrupos> saveOrUpdateList(List<AsignacionGrupos> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AsignacionGrupos> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from AsignacionGrupos model ";
		
		try {
			// JOINS
			queryString += "inner join fetch model.idTransaccion tr " 
					+ "inner join fetch model.idGrupoCredito gc " 
					+ "inner join fetch model.idGrupoDebito gd " 
					+ "inner join fetch model.idConcepto co ";
			queryString += "where model.idTransaccion.idEmpresa = :idEmpresa ";
			if (value != null) {
				if ("descripcion".equals(propertyName.toLowerCase())) {
					queryString += " and (lower(tr.descripcion) = :propertyValue";
					queryString += " 	or lower(gc.descripcion) = :propertyValue";
					queryString += " 	or lower(gd.descripcion) = :propertyValue";
					queryString += " 	or lower(co.descripcion) = :propertyValue)";
				} else {
					queryString += "and model."+ propertyName + " = :propertyValue";
				}
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
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AsignacionGrupos> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from AsignacionGrupos model ";
		StringBuffer sb = null;
		
		try {
			// JOINS
			queryString += "inner join fetch model.idTransaccion tr " 
					+ "inner join fetch model.idGrupoCredito gc " 
					+ "inner join fetch model.idGrupoDebito gd " 
					+ "inner join fetch model.idConcepto co ";
			queryString += "where model.idTransaccion.idEmpresa = :idEmpresa ";
			if (value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += "and (cast(model."+ propertyName + " as string) LIKE :propertyValue";
					queryString += " 	or cast(model.idTransaccion  as string) LIKE :propertyValue";
					queryString += " 	or cast(model.idGrupoCredito as string) LIKE :propertyValue";
					queryString += " 	or cast(model.idGrupoDebito  as string) LIKE :propertyValue";
					queryString += " 	or cast(model.idConcepto 	 as string) LIKE :propertyValue";
					queryString += " 	or cast(model.idFormaPago 	 as string) LIKE :propertyValue";
					queryString += " 	or cast(model.tipoPoliza 	 as string) LIKE :propertyValue)";
				} else {
					queryString += "and (lower(tr."+ propertyName + ") LIKE :propertyValue";
					queryString += " 	or lower(gc."+ propertyName + ") LIKE :propertyValue";
					queryString += " 	or lower(gd."+ propertyName + ") LIKE :propertyValue";
					queryString += " 	or lower(co."+ propertyName + ") LIKE :propertyValue";
					queryString += " 	or lower(cast(tr.codigo as string)) LIKE :propertyValue)";
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
	public List<AsignacionGrupos> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from AsignacionGrupos model ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
			// JOINS
			queryString += "inner join fetch model.idTransaccion tr " 
					+ "inner join fetch model.idGrupoCredito gc " 
					+ "inner join fetch model.idGrupoDebito gd " 
					+ "inner join fetch model.idConcepto co ";
			queryString += "where model.idTransaccion.idEmpresa = :idEmpresa ";
    		if(values != null && ! values.isEmpty()){
    			sqlWhere = " and cast(model." + columnName + " as string) IN (";
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
        		sqlWhere = sqlWhere + inFilter + ") ";
        	}
        	
        	queryString += sqlWhere;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) 
        			query.setParameter(columnName + i, values.get(i).toString());
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
	public List<AsignacionGrupos> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from AsignacionGrupos model ";
		
		try {
			// JOINS
			queryString += "inner join fetch model.idTransaccion tr " 
					+ "inner join fetch model.idGrupoCredito gc " 
					+ "inner join fetch model.idGrupoDebito gd " 
					+ "inner join fetch model.idConcepto co ";
			queryString += "where model.idTransaccion.idEmpresa = :idEmpresa ";
			for (Entry<String, String> e : params.entrySet())
				queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "' ";
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;

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
	public List<AsignacionGrupos> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from AsignacionGrupos model ";
		
		try {
			// JOINS
			queryString += "inner join fetch model.idTransaccion tr " 
					+ "inner join fetch model.idGrupoCredito gc " 
					+ "inner join fetch model.idGrupoDebito gd " 
					+ "inner join fetch model.idConcepto co ";
			queryString += "where model.idTransaccion.idEmpresa = :idEmpresa ";
			for (Entry<String, String> e : params.entrySet()) 
				queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "' ";
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
}