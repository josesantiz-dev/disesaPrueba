package net.giro.rh.admon.dao;

import java.util.List; 

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.EmpleadoBeneficiario; 

@Stateless
public class EmpleadoBeneficiarioImp extends DAOImpl<EmpleadoBeneficiario> implements EmpleadoBeneficiarioDAO  {

	@PersistenceContext
	private EntityManager entityManager;

	public void delete(EmpleadoBeneficiario entity) {
		try {
			entity = entityManager.getReference(EmpleadoBeneficiario.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(EmpleadoBeneficiario entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EmpleadoBeneficiario findById(Integer id) {
		try {
			EmpleadoBeneficiario instance = entityManager.find(EmpleadoBeneficiario.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoBeneficiario> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from EmpleadoBeneficiario model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoBeneficiario> findAll() {
		try {
			final String queryString = "select model from EmpleadoBeneficiario model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoBeneficiario> findByIdEmpleado(long idEmpleado) {
		try {
			final String queryString = "select model from EmpleadoBeneficiario model where model.idEmpleado = " + idEmpleado;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoBeneficiario> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from EmpleadoBeneficiario model" + 
			("".equals(value) || value == null ? "" : " where model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoBeneficiario> findByPropertyPojoCompleto(String propertyName, String tipo, long value) {
		return null;
	}
}
