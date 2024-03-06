package net.giro.adp.dao;


import net.giro.DAOImpl;

import net.giro.adp.beans.ConceptoPresupuesto;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ConceptoPresupuestoImp extends DAOImpl<ConceptoPresupuesto> implements ConceptoPresupuestoDAO {
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
	public long save(ConceptoPresupuesto entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<ConceptoPresupuesto> saveOrUpdateList(List<ConceptoPresupuesto> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ConceptoPresupuesto> findAllActivos() {
		String queryString = "select model from ConceptoPresupuesto model where model.idEmpresa = :idEmpresa order by model.orden asc";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoPresupuesto> findByProperty(String propertyName, final Object value, int limite) {
		String queryString = "select model from ConceptoPresupuesto model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null && ! "".equals(value.toString()))
				queryString = queryString + "and model."+ propertyName + " = :propertyValue";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && ! "".equals(value.toString()))
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
	public List<ConceptoPresupuesto> findLikeProperty(String propertyName, final String value, int limite) {
		String queryString = "select model from ConceptoPresupuesto model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.trim())) {
				if ("id".equals(propertyName))
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyName";
				else
					queryString += "and lower(model."+ propertyName + ") LIKE :propertyValue";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
