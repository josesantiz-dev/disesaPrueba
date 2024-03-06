package net.giro.rh.admon.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.rh.admon.beans.EmpleadoFiniquito;

@Stateless
public class EmpleadoFiniquitoImp extends DAOImpl<EmpleadoFiniquito> implements EmpleadoFiniquitoDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoFiniquito> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from EmpleadoFiniquito model where model.idEmpresa = :idEmpresa and model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoFiniquito> findAll() {
		try {
			final String queryString = "select model from EmpleadoFiniquito model where model.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado) {
		try {
			final String queryString = "select model from EmpleadoFiniquito model, Empleado e where model.idEmpresa = :idEmpresa and model.idEmpleado = e.id and lower(e.nombre) like '%"+ nombreEmpleado.toLowerCase() +"%' ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
