package net.giro.inventarios.dao;

import net.giro.DAOImpl;
import net.giro.inventarios.beans.MovimientosDetalle;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MovimientosDetalleImp extends DAOImpl<MovimientosDetalle> implements MovimientosDetalleDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(MovimientosDetalle entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = codigoEmpresa > 0L ? codigoEmpresa : 1L;
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<MovimientosDetalle> saveOrUpdateList(List<MovimientosDetalle> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = codigoEmpresa > 0L ? codigoEmpresa : 1L;
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MovimientosDetalle> findAll(long idAlmacenMovimiento){
		String queryString = "select model from MovimientosDetalle model where model.idAlmacenMovimiento = :idAlmacenMovimiento ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idAlmacenMovimiento", idAlmacenMovimiento);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MovimientosDetalle> findLikeProperty(String propertyName, Object value, long idAlmacenMovimiento) {
		String queryString = "select model from MovimientosDetalle model where model.idAlmacenMovimiento = :idAlmacenMovimiento ";
		
		try {
			if (value != null)
				queryString = "and trim(cast(model." + propertyName + " as string)) like '%" + value.toString().trim() +"%' ";
			queryString = "order by model.id ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idAlmacenMovimiento", idAlmacenMovimiento);
			if (value != null)
				query.setParameter("propertyValue", value.toString().trim());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MovimientosDetalle> findByProperty(String propertyName, Object value, long idAlmacenMovimiento) {
		String queryString = "select model from MovimientosDetalle model where model.idAlmacenMovimiento = :idAlmacenMovimiento ";
		
		try {
			if (value != null)
				queryString = "and model." + propertyName + " = :propertyValue ";
			queryString = "order by model.id ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idAlmacenMovimiento", idAlmacenMovimiento);
			if (value != null)
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
