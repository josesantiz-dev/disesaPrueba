package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.ObraEmpleado;

@Stateless
public class ObraEmpleadoImp extends DAOImpl<ObraEmpleado> implements ObraEmpleadoDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ObraEmpleado entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ObraEmpleado> saveOrUpdateList(List<ObraEmpleado> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraEmpleado> findAll(long idObra, String orderBy) {
		String queryString = "select model from ObraEmpleado model where model.idObra.id = :idObra ";
		
		try {
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.nombre, model.nombreEmpleado");
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraEmpleado> findLikeProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) {
		String queryString = "select model from ObraEmpleado model where model.idObra.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.nombre, model.nombreEmpleado");
			if (value != null && ! "".equals(value.toString().trim())) {
				if (propertyName.toLowerCase().contains("id")) 
					queryString += " where lower(cast(model."+ propertyName + " as string)) LIKE :propertyValue ";
				else
					queryString += " where lower(model."+ propertyName + ") LIKE :propertyValue ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().trim().toLowerCase());
	    		sb.append("%");
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
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
	public List<ObraEmpleado> findByProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) {
		String queryString = "select model from ObraEmpleado model where model.idObra.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.nombre, model.nombreEmpleado");
			if (value != null && ! "".equals(value.toString().trim())) 
				queryString = queryString + " and model."+ propertyName + " = :propertyValue ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString().trim()))
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
