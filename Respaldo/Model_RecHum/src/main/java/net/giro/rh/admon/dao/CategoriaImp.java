package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.Categoria;

@Stateless
public class CategoriaImp extends DAOImpl<Categoria> implements CategoriaDAO  {

	@PersistenceContext
	private EntityManager entityManager;

	public void delete(Categoria entity) {
		try {
			entity = entityManager.getReference(Categoria.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(Categoria entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Categoria findById(Integer id) {
		try {
			Categoria instance = entityManager.find(Categoria.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Categoria> findByProperty(String propertyName, final Object value) {
		try {
			/*
			final String queryString = "select model from Categoria model where model."
					+ propertyName + "= :propertyValue";
			*/
			final String queryString = "select model from Categoria model where lower(af) like '%"+ value.toString().toLowerCase() +"%'";
			Query query = entityManager.createQuery(queryString);
			//query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Categoria> findAll() {
		try {
			final String queryString = "select model from Categoria model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Categoria> findAllActivos() {
		try {
			final String queryString = "select model from Categoria model where model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Categoria> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from Categoria model" + 
			("".equals(value) || value == null ? "" : " where model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<Categoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value) {
		return null;
	}
	
}
