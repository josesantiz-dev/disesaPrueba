package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.inventarios.beans.TraspasoDetalle;

@Stateless
public class TraspasoDetalleImp extends DAOImpl<TraspasoDetalle> implements TraspasoDetalleDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(TraspasoDetalle entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<TraspasoDetalle> saveOrUpdateList(List<TraspasoDetalle> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TraspasoDetalle> findAll(long idAlmacenTraspaso) {
		String queryString = "select model from TraspasoDetalle model ";
		
		try {
			queryString += "where model.idAlmacenTraspaso = :idAlmacenTraspaso ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idAlmacenTraspaso", idAlmacenTraspaso);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TraspasoDetalle> findByProperty(String propertyName, Object value, long idAlmacenTraspaso) {
		String queryString = "select model from TraspasoDetalle model where model.idAlmacenTraspaso = :idAlmacenTraspaso ";
		
		try {
			if (value != null)
				queryString = "and model." + propertyName + " = :propertyValue ";
			queryString = "order by model.id ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idAlmacenTraspaso", idAlmacenTraspaso);
			if (value != null)
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TraspasoDetalle> findLikeProperty(String propertyName, Object value, long idAlmacenTraspaso) {
		String queryString = "select model from TraspasoDetalle model where model.idAlmacenTraspaso = :idAlmacenTraspaso ";
		
		try {
			if (value != null)
				queryString = "and cast(model." + propertyName + " as string) like '%" + value.toString().trim() +"%' ";
			queryString = "order by model.id ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idAlmacenTraspaso", idAlmacenTraspaso);
			if (value != null)
				query.setParameter("propertyValue", value.toString().trim());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
