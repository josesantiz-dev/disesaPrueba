package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.PresupuestoDetalle;

@Stateless
public class PresupuestoDetalleImp extends DAOImpl<PresupuestoDetalle> implements PresupuestoDetalleDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(PresupuestoDetalle entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PresupuestoDetalle> saveOrUpdateList(List<PresupuestoDetalle> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PresupuestoDetalle> findAll(long idPresupuesto, String orderBy) {
		String queryString = "select model from PresupuestoDetalle model ";
		
		try {
			queryString += "where model.idPresupuesto = :propertyValue ";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.id";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", idPresupuesto);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PresupuestoDetalle> findByProperty(String propertyName, Object value, String orderBy, int limite) {
		String queryString = "select model from PresupuestoDetalle model ";
		
		try {
			if (value != null && !"".equals(value.toString())) 
				queryString += "where model."+ propertyName + " = :propertyValue ";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.id";
			queryString += "order by " + orderBy;
			
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
	public List<PresupuestoDetalle> findLikeProperty(String propertyName, Object value, String orderBy, int limite) {
		String queryString = "select model from PresupuestoDetalle model ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName)) 
					queryString += "where cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				else
					queryString += "where lower(model."+ propertyName + ") LIKE :propertyValue ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.id";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase PresupuestoImp