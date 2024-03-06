package net.giro.contabilidad.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.ImpuestoEquivalencia;

@Stateless
public class ImpuestoEquivalenciaImp extends DAOImpl<ImpuestoEquivalencia> implements ImpuestoEquivalenciaDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ImpuestoEquivalencia entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ImpuestoEquivalencia> saveOrUpdateList(List<ImpuestoEquivalencia> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ImpuestoEquivalencia> findAll(long codigoTransaccion, String orderBy) throws Exception {
		String queryString = "select model from ImpuestoEquivalencia model where model.transaccion = :codigoTransaccion ";
		
		try {
			if (orderBy == null || "".equals(orderBy))
				orderBy = "id";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("codigoTransaccion", codigoTransaccion);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ImpuestoEquivalencia> findByProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ImpuestoEquivalencia model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) 
				queryString = queryString + " and model."+ propertyName + " = :propertyValue ";
			if (orderBy == null || "".equals(orderBy))
				orderBy = "id";
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
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ImpuestoEquivalencia> findLikeProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ImpuestoEquivalencia model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) 
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				else
					queryString += " and lower(model."+ propertyName + ") LIKE :propertyValue";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}

			if (orderBy == null || "".equals(orderBy))
				orderBy = "id";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ImpuestoEquivalencia> findInProperty(String propertyName, List<Object> values, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ImpuestoEquivalencia model where model.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = " and cast(model." + propertyName + " as string) IN (";
    			for (int i = 0; i < values.size(); i++) {
    				if (! "".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + propertyName + i;
    			}
        		sqlWhere = sqlWhere + inFilter + ") ";
        	}
        	
        	queryString += sqlWhere;
			if (orderBy == null || "".equals(orderBy))
				orderBy = "id";
			queryString += "order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) 
        			query.setParameter(propertyName + i, values.get(i).toString());
        	}
			if (limite > 0)
				query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2019-05-17 | Javier Tirado 	| Creacion de ImpuestoEquivalenciaImp
 */
