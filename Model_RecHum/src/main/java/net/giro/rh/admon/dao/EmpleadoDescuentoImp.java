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
import net.giro.rh.admon.beans.EmpleadoDescuento;

@Stateless
public class EmpleadoDescuentoImp extends DAOImpl<EmpleadoDescuento> implements EmpleadoDescuentoDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		EmpleadoDescuentoImp.orderBy = orderBy;
	}

	@Override
	public long save(EmpleadoDescuento entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoDescuento> saveOrUpdateList(List<EmpleadoDescuento> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoDescuento> findAll(long idEmpleado) throws Exception {
		String queryString = "select model from EmpleadoDescuento model where model.idEmpleado.id = :idEmpleado ";
		
		try {
			if (idEmpleado <= 0)
				idEmpleado = 0;
			queryString += "and model.estatus = 1 order by model.fecha " ;
			//----------------------------------------------------
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpleado", idEmpleado);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoDescuento> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from EmpleadoDescuento model where model.idEmpleado.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null)
				queryString = queryString + " and model."+ propertyName + " = :propertyValue";
			
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
	public List<EmpleadoDescuento> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from EmpleadoDescuento model where model.idEmpleado.idEmpresa = :idEmpresa ";
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
	public List<EmpleadoDescuento> findInProperty(String columnName, List<Object> values, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from EmpleadoDescuento model where model.idEmpleado.idEmpresa = :idEmpresa ";
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
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) {
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
	public List<EmpleadoDescuento> findByProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception{
		String queryString = "select model from EmpleadoDescuento model where model.idEmpleado.idEmpresa = :idEmpresa ";
		String where = "";

		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			for (Entry<String, String> e : params.entrySet()) {
				if (where.length() > 0)
					where += " and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "'";
				else
					where += " and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "'";
			}
			queryString += where;
			
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
	public List<EmpleadoDescuento> findLikeProperties(HashMap<String, String> params, long idEmpresa, int limite) throws Exception{
		String queryString = "select model from EmpleadoDescuento model where model.idEmpleado.idEmpresa = :idEmpresa ";
		String where = "";

		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			for (Entry<String, String> e : params.entrySet()) {
				if (where.length() > 0)
					where += " and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
				else
					where += " and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			queryString += where;
			
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
	public List<EmpleadoDescuento> comprobarDescuentosPorFecha(long idEmpleado, Date fecha, long idEmpresa) throws Exception {
		String queryString = "select model from EmpleadoDescuento model where model.idEmpleado.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (idEmpleado <= 0L || fecha == null)
				return new ArrayList<EmpleadoDescuento>();
			queryString += "and model.idEmpleado.id = :idEmpleado and DATE(model.fecha) = DATE(:fecha) and model.estatus = 1 ";
			if (orderBy != null && ! "".equals(orderBy))
				queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idEmpleado", idEmpleado);
			query.setParameter("fecha", formatter.format(fecha));
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
//	  2.1	| 25/05/2016 | Javier Tirado	| Creacion de EmpleadoDescuentoImp