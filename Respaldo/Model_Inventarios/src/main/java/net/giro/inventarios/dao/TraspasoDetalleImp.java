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

	public void delete(TraspasoDetalle entity) {
		try {
			entity = entityManager.getReference(TraspasoDetalle.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(TraspasoDetalle entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public TraspasoDetalle findById(Integer id) {
		try {
			TraspasoDetalle instance = entityManager.find(TraspasoDetalle.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<TraspasoDetalle> findByProperty(String propertyName, final Object value) {
		try {
			String queryString = "select model from TraspasoDetalle model";
			
			if(propertyName=="id"){
				queryString = "select model from TraspasoDetalle model where id = "+ value ;
				
			}else{
				queryString = "select model from TraspasoDetalle model where lower( model." + propertyName + " ) like '%"+ value.toString().toLowerCase() +"%'";
				System.out.println("queryString: "+queryString);
			}
			
			
			Query query = entityManager.createQuery(queryString);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

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

	@SuppressWarnings("unchecked")
	public List<TraspasoDetalle> findDetallesById(long idAlmacenTraspaso) {
		try {
			final String queryString = "select model from TraspasoDetalle model where model.idAlmacenTraspaso = "+idAlmacenTraspaso;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
