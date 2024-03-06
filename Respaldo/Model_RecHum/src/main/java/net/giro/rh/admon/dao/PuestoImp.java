package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.Puesto;

@Stateless
public class PuestoImp extends DAOImpl<Puesto> implements PuestoDAO  {

	@PersistenceContext
	private EntityManager entityManager;

	public void delete(Puesto entity) {
		try {
			entity = entityManager.getReference(Puesto.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(Puesto entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Puesto findById(Integer id) {
		try {
			Puesto instance = entityManager.find(Puesto.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Puesto> findByProperty(String propertyName, final Object value) {
		try {
			/*
			final String queryString = "select model from Puesto model where model."
					+ propertyName + "= :propertyValue";*/
			final String queryString = "select model from Puesto model where lower(af) like '%"+ value.toString().toLowerCase() +"%'";
			Query query = entityManager.createQuery(queryString);
			//query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Puesto> findAll() {
		try {
			final String queryString = "select model from Puesto model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Puesto> findAllActivos() {
		try {
			final String queryString = "select model from Puesto model where model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Puesto> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from Puesto model" + 
			("".equals(value) || value == null ? "" : " where model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<Puesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value) {
		return null;
	}
}
