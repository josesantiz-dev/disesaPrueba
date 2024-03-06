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
import net.giro.compras.beans.Cotizacion;
import net.giro.comun.ExcepConstraint;

@Stateless
public class CotizacionImp extends DAOImpl<Cotizacion> implements CotizacionDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	private static Integer estatusId;

	@Override
	public void OrderBy(String orderBy) {
		CotizacionImp.orderBy = orderBy;
	}
	
	@Override
	public void estatus(Integer estatus) {
		CotizacionImp.estatusId = estatus;
	}

	@Override
	public long save(Cotizacion entity, Long idEmpresa) throws ExcepConstraint {
		try {
			return super.save(entity, idEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Cotizacion> saveOrUpdateList(List<Cotizacion> entities, Long idEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, idEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findByProperty(String propertyName, Object value, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null)
				queryString += "and model."+ propertyName + " = :propertyValue";
			
			if (estatusId != null)
				queryString += "and model.estatus = :estatus";
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.fecha desc, model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findLikeProperty(String propertyName, Object value, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null && ! "".equals(value.toString())) {
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
			
			if (estatusId != null)
				queryString += " and model.estatus = :estatus";
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.fecha desc, model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findInProperty(String columnName, List<Object> values, Long idEmpresa) throws Exception {
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (idEmpresa == null || idEmpresa <= 0)
    			idEmpresa = 1L;
    		if (values != null && ! values.isEmpty()) {
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = "and cast(model." + columnName + " as string) in (" + inFilter + ") ";
        	}
			
			if (! sqlWhere.isEmpty())
				queryString += sqlWhere;
			
			if (estatusId != null)
				queryString += "and model.estatus = :estatus ";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.fecha desc, model.id DESC";
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findByProperties(HashMap<String, Object> params, Long idEmpresa) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			for (Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += "and ";
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += "date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += "cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					whereString += "cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + "and " + whereString;
			
			if (estatusId != null)
				queryString += "and estatus = " + estatusId + " ";
			estatusId = null;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			orderBy = null;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findLikeProperties(HashMap<String, String> params, Long idEmpresa) throws Exception {
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			for (Entry<String, String> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += "or ";
				whereString += "cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + "and (" + whereString + ") ";
			
			if (estatusId != null)
				queryString += "and model.estatus = " + estatusId + " ";
			estatusId = null;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			orderBy = null;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findByPropertyWithObra(String propertyName, Object value, long idObra, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
		String where = "";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null)
				where = "and model."+ propertyName + " = :propertyValue ";
			
			if (estatusId != null)
				where += "and model.estatus = :estatus ";
			if (idObra > 0L)
				where += "and model.idObra = :idObra "; 
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.fecha desc, model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
			if (idObra > 0L) 
				query.setParameter("idObra", idObra);
			if (limite > 0)
				query.setMaxResults(limite);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findLikePropertyWithObra(String propertyName, Object value, long idObra, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
		String where = "";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					where = "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					where = "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (estatusId != null)
				where += "and model.estatus = :estatus ";
			
			if (idObra > 0L)
				where += "and model.idObra = :idObra "; 
			queryString += where;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.fecha desc, model.id DESC";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
			if (idObra > 0L) 
				query.setParameter("idObra", idObra);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findInPropertyWithObra(String columnName, List<Object> values, long idObra, Long idEmpresa) throws Exception {
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (idEmpresa == null || idEmpresa <= 0)
    			idEmpresa = 1L;
    		if (values != null && ! values.isEmpty()){
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = " and cast(model." + columnName + " as string) IN (" + inFilter + ")";
        	}
			
			if (estatusId != null)
				sqlWhere += " and model.estatus = :estatus ";
			
			if (idObra > 0L)
				sqlWhere += " and model.idObra = :idObra "; 
        	queryString += sqlWhere;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.fecha desc, model.id DESC";
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
			if (estatusId != null) 
				query.setParameter("estatus", estatusId);
			if (idObra > 0L) 
				query.setParameter("idObra", idObra);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			orderBy = null;
			estatusId = null;
		}
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor, Long idEmpresa) throws Exception {
		String queryString = "select (COUNT(movCta.idProveedor) + 1) from Cotizacion movCta where movCta.idEmpresa = :idEmpresa and movCta.idProveedor = :propertyValue";
		Long consecutivo;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (idProveedor <= 0L) 
				return 0;
			
			Query query = entityManager.createQuery(queryString, Long.class);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", idProveedor);
			consecutivo = (Long) query.getSingleResult();
			if (consecutivo == null)
				consecutivo = 0L;
			return consecutivo.intValue();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. 
 */