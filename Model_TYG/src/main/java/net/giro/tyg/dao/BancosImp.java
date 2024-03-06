package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.tyg.admon.Banco;

@Stateless
public class BancosImp extends DAOImpl<Banco> implements BancosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Banco> findByProperty(String propertyName, Object value, Long idEmpresa, int limite) {
		try {
			final String queryString = "select model from Banco model where model." + propertyName + " = :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Banco> findLikeProperty(String propertyName, Object value, Long idEmpresa, int limite) {
		try {
			if (value == null)
				value = "";
			value = "%" + value + "%";
			final String queryString = "select model from Banco model where lower(cast(model." + propertyName + " as string)) like :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
