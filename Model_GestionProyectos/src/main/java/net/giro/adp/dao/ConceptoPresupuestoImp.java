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
	
	@Override
	public long save(ConceptoPresupuesto entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<ConceptoPresupuesto> saveOrUpdateList(List<ConceptoPresupuesto> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ConceptoPresupuesto> findAllActivos() {
		String queryString = "select model from ConceptoPresupuesto model order by model.orden asc";
		
		try {
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoPresupuesto> findByProperty(String propertyName, final Object value, int limite) {
		String queryString = "select model from ConceptoPresupuesto model ";
		
		try {
			if (value != null && ! "".equals(value.toString()))
				queryString = queryString + "where model."+ propertyName + " = :propertyValue";
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.toString()))
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
	public List<ConceptoPresupuesto> findLikeProperty(String propertyName, final String value, int limite) {
		String queryString = "select model from ConceptoPresupuesto model ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.trim())) {
				if ("id".equals(propertyName))
					queryString += "where cast(model."+ propertyName + " as string) LIKE :propertyName";
				else
					queryString += "where lower(model."+ propertyName + ") LIKE :propertyValue";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
