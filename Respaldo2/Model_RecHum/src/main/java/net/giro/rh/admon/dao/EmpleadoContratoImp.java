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
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoContrato> findAllByIdEmpleado(long idEmpleado) {
		try {
			final String queryString = "select model from EmpleadoContrato model where model.idEmpleado.idEmpresa = :idEmpresa and model.idEmpleado.id = " + idEmpleado;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoContrato> findByProperty(String propertyName, final Object value) {
		String queryString = "select model from EmpleadoContrato model where model.idEmpleado.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) 
				queryString += "and model." + propertyName + " = :propertyValue ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoContrato> contratoValido(long idEmpleado) {
		try {
			final String queryString = "select model from EmpleadoContrato model where model.idEmpleado.idEmpresa = :idEmpresa "
					+ " and estatus = 0 AND model.idEmpleado.id = "+ idEmpleado + " AND DATE(CURRENT_DATE) BETWEEN DATE(fechaInicio) AND DATE(fechaFin) " 
					+ "ORDER BY fechaInicio DESC";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
