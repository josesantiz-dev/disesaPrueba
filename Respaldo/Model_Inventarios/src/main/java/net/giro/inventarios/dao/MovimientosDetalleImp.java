package net.giro.inventarios.dao;

import net.giro.DAOImpl;

import net.giro.inventarios.beans.MovimientosDetalle;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MovimientosDetalleImp  extends DAOImpl<MovimientosDetalle> implements MovimientosDetalleDAO{
	
	@PersistenceContext
	private EntityManager entityManager;

	public void delete(MovimientosDetalle entity) {
		try {
			entity = entityManager.getReference(MovimientosDetalle.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(MovimientosDetalle entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public MovimientosDetalle findById(Integer id) {
		try {
			MovimientosDetalle instance = entityManager.find(MovimientosDetalle.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<MovimientosDetalle> findByProperty(String propertyName, final Object value) {
		try {
			
			String queryString = "select model from MovimientosDetalle model";
			
			if(propertyName=="id"){
				queryString = "select model from MovimientosDetalle model where id = "+ value ;
				
			}else{
				queryString = "select model from MovimientosDetalle model where lower( model." + propertyName + " ) like '%"+ value.toString().toLowerCase() +"%'";
			}
			
			Query query = entityManager.createQuery(queryString);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<MovimientosDetalle> findAll() {
		try {
			final String queryString = "select model from MovimientosDetalle model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MovimientosDetalle> findAllActivos() {
		try {
			final String queryString = "select model from MovimientosDetalle model where model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MovimientosDetalle> findDetallesById(long idAlmacenMovimiento){
		try {
			final String queryString = "select model from MovimientosDetalle model where model.idAlmacenMovimiento = "+idAlmacenMovimiento;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

}
