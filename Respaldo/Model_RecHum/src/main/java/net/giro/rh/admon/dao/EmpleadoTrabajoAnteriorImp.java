package net.giro.rh.admon.dao; 

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.EmpleadoTrabajoAnterior;

@Stateless
public class EmpleadoTrabajoAnteriorImp extends DAOImpl<EmpleadoTrabajoAnterior> implements EmpleadoTrabajoAnteriorDAO  {

	@PersistenceContext
	private EntityManager entityManager;

	public void delete(EmpleadoTrabajoAnterior entity) {
		try {
			entity = entityManager.getReference(EmpleadoTrabajoAnterior.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(EmpleadoTrabajoAnterior entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EmpleadoTrabajoAnterior findById(Integer id) {
		try {
			EmpleadoTrabajoAnterior instance = entityManager.find(EmpleadoTrabajoAnterior.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoTrabajoAnterior> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from EmpleadoTrabajoAnterior model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoTrabajoAnterior> findAll() {
		try {
			final String queryString = "select model from EmpleadoTrabajoAnterior model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoTrabajoAnterior> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from EmpleadoTrabajoAnterior model" + 
			("".equals(value) || value == null ? "" : " where model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoTrabajoAnterior> findByPropertyPojoCompleto( String propertyName, String tipo, long value) {
		return null;
	}
	
}
