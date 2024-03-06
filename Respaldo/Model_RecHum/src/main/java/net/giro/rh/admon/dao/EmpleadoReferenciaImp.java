package net.giro.rh.admon.dao;

import java.util.List; 

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.EmpleadoReferencia;

@Stateless
public class EmpleadoReferenciaImp extends DAOImpl<EmpleadoReferencia> implements EmpleadoReferenciaDAO  {
	
	@PersistenceContext
	private EntityManager entityManager;

	public void delete(EmpleadoReferencia entity) {
		try {
			entity = entityManager.getReference(EmpleadoReferencia.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(EmpleadoReferencia entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EmpleadoReferencia findById(Integer id) {
		try {
			EmpleadoReferencia instance = entityManager.find(EmpleadoReferencia.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoReferencia> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from EmpleadoReferencia model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoReferencia> findAll() {
		try {
			final String queryString = "select model from EmpleadoReferencia model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoReferencia> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from EmpleadoReferencia model" + 
			("".equals(value) || value == null ? "" : " where model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoReferencia> findByPropertyPojoCompleto( String propertyName, String tipo, long value) {
		return null;
	}
}
