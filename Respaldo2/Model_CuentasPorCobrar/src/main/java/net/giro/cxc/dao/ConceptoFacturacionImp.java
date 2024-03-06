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
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(ConceptoFacturacion entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<ConceptoFacturacion> saveOrUpdateList(List<ConceptoFacturacion> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacion> findAll() {
		try {
			final String queryString = "select model from ConceptoFacturacion model where model.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
			queryString = "select model from ConceptoFacturacion model where model.idEmpresa = :idEmpresa ";
			if (value != null) {
				if ("id".equals(propertyName)) {
					queryString += "and model.id = :" + propertyName + " order by model.id desc";
				} else if ("claveSat".equals(propertyName)) {
					queryString += "and model.claveSat = :" + propertyName + " order by model.claveSat";
				} else {
					queryString += "and lower(model." + propertyName + ") LIKE :" + propertyName + " order by model." + propertyName;
				}
			}

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
			queryString = "select model from ConceptoFacturacion model where model.idEmpresa = :idEmpresa ";
			if (value != null && ! "".equals(value)) {
				if ("id".equals(propertyName)) {
					queryString += "and cast(model.id as string) LIKE :" + propertyName + " order by model.id desc";
				} else if ("claveSat".equals(propertyName)) {
					queryString += "and cast(model.claveSat as string) LIKE :" + propertyName + " order by model.claveSat";
				} else {
					queryString += "and lower(model." + propertyName + ") LIKE :" + propertyName + " order by model." + propertyName;
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
