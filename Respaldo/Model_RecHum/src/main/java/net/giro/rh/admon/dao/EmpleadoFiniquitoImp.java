package net.giro.rh.admon.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.rh.admon.beans.EmpleadoFiniquito;

@Stateless
public class EmpleadoFiniquitoImp extends DAOImpl<EmpleadoFiniquito> implements EmpleadoFiniquitoDAO{
	
	@PersistenceContext
	private EntityManager entityManager;

	public void delete(EmpleadoFiniquito entity) {
		try {
			entity = entityManager.getReference(EmpleadoFiniquito.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void update(EmpleadoFiniquito entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EmpleadoFiniquito findById(Integer id) {
		try {
			EmpleadoFiniquito instance = entityManager.find(EmpleadoFiniquito.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoFiniquito> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from EmpleadoFiniquito model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoFiniquito> findAll() {
		try {
			final String queryString = "select model from EmpleadoFiniquito model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado) {
		try {
			final String queryString = "select ef from EmpleadoFiniquito ef, Empleado e where ef.idEmpleado = e.id and lower(e.nombre) like '%"+ nombreEmpleado.toLowerCase() +"%' ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
}
