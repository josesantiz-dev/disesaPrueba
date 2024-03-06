package net.giro.cxc.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.ConceptoFacturacion;

@Stateless
public class ConceptoFacturacionImp extends DAOImpl<ConceptoFacturacion> implements ConceptoFacturacionDAO{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacion> findAll() {
		try {
			final String queryString = "select model from ConceptoFacturacion model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacion> findByProperty(String propertyName, final Object value, int limite) {
		String queryString = "";
		
		try {
			queryString = "select model from ConceptoFacturacion model ";
			if (value != null) {
				if ("id".equals(propertyName)) {
					queryString += "where model.id = :" + propertyName + " order by model.id desc";
				} else if ("claveSat".equals(propertyName)) {
					queryString += "where model.claveSat = :" + propertyName + " order by model.claveSat";
				} else {
					queryString += "where lower(model." + propertyName + ") LIKE :" + propertyName + " order by model." + propertyName;
				}
			}

			Query query = entityManager.createQuery(queryString);
        	if (value != null && !"".equals(value.toString()))
    			query.setParameter("propertyValue", value);
        	if (limite > 0)
        		query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacion> findLikeProperty(String propertyName, final String value, int limite) {
		String queryString = "";
    	StringBuffer sb = null;
    	
		try {
			queryString = "select model from ConceptoFacturacion model ";
			if (value != null && ! "".equals(value)) {
				if ("id".equals(propertyName)) {
					queryString += "where cast(model.id as string) LIKE :" + propertyName + " order by model.id desc";
				} else if ("claveSat".equals(propertyName)) {
					queryString += "where cast(model.claveSat as string) LIKE :" + propertyName + " order by model.claveSat";
				} else {
					queryString += "where lower(model." + propertyName + ") LIKE :" + propertyName + " order by model." + propertyName;
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			Query query = entityManager.createQuery(queryString);
        	if (value != null && !"".equals(value.toString()))
            	query.setParameter(propertyName, sb.toString());
        	if (limite > 0)
        		query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacion> findByPropertyPojoCompleto(String propertyName, String tipo, Object value) {
		return null;
	}
}
