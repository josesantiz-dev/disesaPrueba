package net.giro.rh.admon.dao; 

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

//import net.giro.rh.admon.beans.EmpleadoPariente;
import net.giro.rh.admon.beans.EmpleadoPariente;


@Stateless
public class EmpleadoParienteImp extends DAOImpl<EmpleadoPariente> implements EmpleadoParienteDAO  {

	@PersistenceContext
	private EntityManager entityManager;

	public void delete(EmpleadoPariente entity) {
		try {
			entity = entityManager.getReference(EmpleadoPariente.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(EmpleadoPariente entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EmpleadoPariente findById(Integer id) {
		try {
			EmpleadoPariente instance = entityManager.find(EmpleadoPariente.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoPariente> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from EmpleadoPariente model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoPariente> findAll() {
		try {
			final String queryString = "select model from EmpleadoPariente model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoPariente> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from Areas model" + 
			("".equals(value) || value == null ? "" : " where model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoPariente> findByPropertyPojoCompleto( String propertyName, String tipo, long value) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoPariente> findByIdEmpleadoParentesco(long idEmpleado) {
		try {
			final String queryString = "select model from EmpleadoPariente model where model.idEmpleado = "+idEmpleado;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
