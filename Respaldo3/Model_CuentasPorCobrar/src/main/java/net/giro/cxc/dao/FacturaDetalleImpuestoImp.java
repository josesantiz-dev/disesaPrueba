package net.giro.cxc.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.FacturaDetalleImpuesto;

@Stateless
public class FacturaDetalleImpuestoImp extends DAOImpl<FacturaDetalleImpuesto> implements FacturaDetalleImpuestoDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(FacturaDetalleImpuesto entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<FacturaDetalleImpuesto> saveOrUpdateList(List<FacturaDetalleImpuesto> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaDetalleImpuesto> findAll(long idFacturaDetalle, String orderBy) throws Exception {
		String queryString = "select model from FacturaDetalleImpuesto model where model.idFacturaDetalle = :idFacturaDetalle ";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.id";
			queryString += "order by " + orderBy;
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idFacturaDetalle", idFacturaDetalle);
        	return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaDetalleImpuesto> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		String queryString = "select model from FacturaDetalleImpuesto model ";
		
		try {
			if (value != null)
				queryString += "where model." + propertyName + " = :propertyValue ";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = propertyName;
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
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
	public List<FacturaDetalleImpuesto> findLikeProperty(String propertyName, String value, String orderBy, int limite) throws Exception {
		String queryString = "select model from FacturaDetalleImpuesto model ";
    	String sqlWhere = "";
    	StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.trim())) {
        		if (propertyName.startsWith("id") || "monto".equals(propertyName))
            		sqlWhere += "where cast(model." + propertyName + " as string) LIKE :propertyValue ";
            	else
            		sqlWhere += "where lower(model." + propertyName + ") LIKE :propertyValue ";
    			value = validateString(value);
        		sb = new StringBuffer();
        		sb.append("%");
        		sb.append(value.trim().toLowerCase());
        		sb.append("%");
        	}
        	
        	queryString = queryString + sqlWhere;
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = propertyName;
			queryString += "order by " + orderBy;
			
        	Query query = entityManager.createQuery(queryString);
        	if (value != null && ! "".equals(value.trim()))
            	query.setParameter(propertyName, sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
        	return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------------------
	
	private String validateString(String value) {
		if (value == null || "".equals(value.trim()))
			return "";
		
		value = value.trim().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");
		value = value.trim().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
		value = value.trim().replace("Ä", "A").replace("Ë", "E").replace("Ï", "I").replace("Ö", "O").replace("Ü", "U");
		value = value.trim().replace("ä", "a").replace("ë", "e").replace("ï", "i").replace("ö", "o").replace("ü", "u");
		
		return value;
	}
}
