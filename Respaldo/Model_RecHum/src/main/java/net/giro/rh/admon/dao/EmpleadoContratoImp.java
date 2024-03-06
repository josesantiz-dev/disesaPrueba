package net.giro.rh.admon.dao;


import net.giro.DAOImpl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.EmpleadoContrato;

@Stateless
public class EmpleadoContratoImp extends DAOImpl<EmpleadoContrato> implements EmpleadoContratoDAO {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void delete(EmpleadoContrato entity) {
		try {
			entity = entityManager.getReference(EmpleadoContrato.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(EmpleadoContrato entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EmpleadoContrato findById(Integer id) {
		try {
			EmpleadoContrato instance = entityManager.find(EmpleadoContrato.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoContrato> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from EmpleadoContrato model " 
					+ "where lower('"+ propertyName +"') like '%"+ value.toString().toLowerCase() +"%'";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoContrato> findAll() {
		try {
			final String queryString = "select model from EmpleadoContrato model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoContrato> findAllByIdEmpleado(long idEmpleado) {
		try {
			final String queryString = "select model from EmpleadoContrato model where model.idEmpleado = " + idEmpleado;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoContrato> contratoValido(long idEmpleado) {
		try {
			final String queryString = "select model from EmpleadoContrato model "
					+ "where estatus = 0 AND idEmpleado = "+ idEmpleado + " AND DATE(CURRENT_DATE) BETWEEN DATE(fechaInicio) AND DATE(fechaFin) " 
					+ "ORDER BY fechaInicio DESC";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
