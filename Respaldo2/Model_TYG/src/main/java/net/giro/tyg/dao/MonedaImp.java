package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.tyg.admon.Moneda;

@Stateless
public class MonedaImp extends DAOImpl<Moneda> implements MonedaDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<Moneda> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from Moneda model where model.estatus = 0 and model."
					+ propertyName + "= :propertyValue order by model." + propertyName;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Moneda> findAll() {
		try {
			final String queryString = "select model from Moneda model where model.estatus = 0 order by id";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Moneda> findByPropertyLike(String propertyName, final String value) {
		try {
			String queryString = "select model from Moneda model where model.estatus = 0 and lower(model."
					+ propertyName + ") like '%"+ (value != null ?  value.toLowerCase() : "" )+"%' order by model.nombre";
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public Moneda findByAbreviacion(String valor) {
		try {
			final String queryString = "select model from Moneda model where model.estatus = 0 and model.abreviacion='"+ valor + "' order by model.abreviacion";			
			Query query = entityManager.createQuery(queryString);
			return (Moneda) query.getSingleResult(); //.getResultList().isEmpty();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
