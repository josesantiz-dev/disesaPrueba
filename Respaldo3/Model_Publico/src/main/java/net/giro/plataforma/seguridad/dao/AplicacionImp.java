package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.plataforma.seguridad.beans.Aplicacion;

@Stateless
public class AplicacionImp extends DAOImpl<Aplicacion> implements AplicacionDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<Aplicacion> findAll(String orderBy) throws Exception {
		String queryString = "select model from Aplicacion model ";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.aplicacion";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
