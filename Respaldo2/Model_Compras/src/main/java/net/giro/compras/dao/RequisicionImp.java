package net.giro.compras.dao;

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
import net.giro.compras.beans.Requisicion;
import net.giro.comun.ExcepConstraint;

@Stateless
public class RequisicionImp extends DAOImpl<Requisicion> implements RequisicionDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	private static Long estatus; 

	@Override
	public void OrderBy(String orderBy) {
		RequisicionImp.orderBy = orderBy;
	}
	
	@Override
	public void estatus(Long estatus) {
		RequisicionImp.estatus = estatus;
	}

	@Override
	public long save(Requisicion entity, Long idEmpresa) throws ExcepConstraint {
		try {
			return super.save(entity, idEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Requisicion> saveOrUpdateList(List<Requisicion> entities, Long idEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, idEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findByProperty(String propertyName, Object value, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Requisicion model where model.sistema = 0 and model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null)
				queryString += "and model."+ propertyName + " = :propertyValue ";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += " order by model.fecha desc, model.id DESC";
			
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
	public List<Requisicion> findLikeProperty(String propertyName, Object value, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Requisicion model where sistema = 0 and model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || "idpresupuesto".equals(propertyName.toLowerCase())) {
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					queryString = queryString + "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by model.fecha desc, model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
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
	public List<Requisicion> findLikeProperty(String propertyName, String value, int tipo, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Requisicion model where sistema = 0 and model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null && ! "".equals(value)) {
				if ("id".equals(propertyName) || "idpresupuesto".equals(propertyName.toLowerCase())) {
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					queryString += "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toLowerCase());
	    		sb.append("%");
			}
			
			queryString += (tipo > 0 ? "and model.tipo = " + tipo : "");
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy + " ";
			else
				queryString += " order by model.fecha desc, model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
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
	public List<Requisicion> findInProperty(String columnName, List<Object> values, Long idEmpresa) throws Exception {
		String queryString = "select model from Requisicion model where sistema = 0 and model.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = "and cast(model." + columnName + " as string) IN (";
    			
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = sqlWhere + inFilter + ") ";
        	}
        	
        	queryString += sqlWhere;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by model.fecha desc, model.id DESC";
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) 
        			query.setParameter(columnName + i, values.get(i).toString());
        	}
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findByProperties(HashMap<String, Object> params, int limite, Long idEmpresa) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Requisicion model where sistema = 0 and model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			for (Entry<String, Object> e : params.entrySet()) {
				if (e.getValue().getClass() == java.util.Date.class) 
					queryString += "and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					queryString += "and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			if (estatus != null)
				queryString += "and estatus = " + estatus + " ";
			estatus = null;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by model.fecha desc, model.id DESC";

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findLikeProperties(HashMap<String, String> params, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Requisicion model where sistema = 0 and model.idEmpresa = :idEmpresa ";

		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			for (Entry<String, String> e : params.entrySet())
				queryString += "and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			
			if (estatus != null)
				queryString += "and estatus = " + estatus + " ";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			orderBy = null;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}	
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-19 | Javier Tirado 	| Añado los metodos estatus, findByProperties y findLikeProperties. 
 */