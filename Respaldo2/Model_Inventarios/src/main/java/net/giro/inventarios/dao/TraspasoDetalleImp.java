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
	@SuppressWarnings("unchecked")
	public List<TraspasoDetalle> findByProperty(String propertyName, final Object value) {
		String queryString = "select model from TraspasoDetalle model ";
		
		try {
			if (propertyName == "id")
				queryString = "where id = " + value;
			else
				queryString = "where lower( model." + propertyName + " ) like '%" + value.toString().toLowerCase() +"%'";
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TraspasoDetalle> findAll() {
		try {
			final String queryString = "select model from TraspasoDetalle model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TraspasoDetalle> findAllActivos() {
		try {
			final String queryString = "select model from TraspasoDetalle model where model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TraspasoDetalle> findDetallesById(long idAlmacenTraspaso) {
		try {
			final String queryString = "select model from TraspasoDetalle model where model.idAlmacenTraspaso = " + idAlmacenTraspaso;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
