package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.cxc.beans.FacturaPagos;

@Stateless
public class FacturaPagosImp extends DAOImpl<FacturaPagos> implements FacturaPagosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(FacturaPagos entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<FacturaPagos> saveOrUpdateList(List<FacturaPagos> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findByProperty(String columnName, Object value, int limite) {
		String queryString = "select model from FacturaPagos model inner join fetch model.idFactura fac ";
    	
    	try {
    		if ("id".equals(columnName))
    			columnName = "model.id";
    		
    		queryString += "where model.idEmpresa = :idEmpresa and model.estatus = 0 ";
    		if (value != null && ! "".equals(value))
    			queryString += "and " + columnName + " = :propertyValue ";
    		queryString += "order by " + columnName;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
        	if (value != null && ! "".equals(value))
            	query.setParameter("propertyValue", value);
        	if (limite > 0)
        		query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findLikeProperty(String columnName, String value, int limite) {
		String queryString = "select model from FacturaPagos model inner join fetch model.idFactura fac ";
    	StringBuffer sb = null;
    	
    	try {
    		if ("id".equals(columnName))
    			columnName = "model.id";

    		queryString += "where model.idEmpresa = :idEmpresa and model.estatus = 0 ";
    		if (value != null && ! "".equals(value.trim())) {
        		if (columnName.startsWith("id") && ! columnName.contains(".")) {
        			queryString += "and lower(cast(" + columnName + " as string)) LIKE :propertyValue ";
            	} else {
            		queryString += "and lower(" + columnName + ") LIKE :propertyValue ";
            	}

        		sb = new StringBuffer();
        		sb.append("%");
        		sb.append(value.trim().toLowerCase());
        		sb.append("%");
        	}
    		
    		queryString += "order by " + columnName;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
        	if (value != null && ! "".equals(value.trim()))
            	query.setParameter("propertyValue", sb.toString());
        	if (limite > 0)
        		query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findInProperty(String columnName, List<Long> values, int limite) {
		String queryString = "select model from FacturaPagos model inner join fetch model.idFactura fac ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()) {
    			for (int i = 0; i < values.size(); i++) {
    				if (! "".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
        		sqlWhere = "and cast(model." + columnName + " as string) IN (" + inFilter + ") ";
        	}
    		
    		sqlWhere = "where model.idEmpresa = :idEmpresa and model.estatus = 0 " + sqlWhere;
    		queryString += "order by " + columnName;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) 
        			query.setParameter(columnName + i, values.get(i).toString());
        	}
        	if (limite > 0)
        		query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findByFactura(Long idFactura) {
		String queryString = "select model from FacturaPagos model ";
    	
    	try {
    		queryString += "where model.idEmpresa = :idEmpresa and model.estatus = 0 ";
    		queryString += "and model.idFactura = " + ((idFactura == null) ? 0L : idFactura) + " ";
    		queryString += "order by date(model.fecha)";
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}
}
