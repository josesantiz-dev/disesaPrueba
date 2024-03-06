package net.giro.rh.admon.dao; 

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.EmpleadoTrabajoAnterior;

@Stateless
public class EmpleadoTrabajoAnteriorImp extends DAOImpl<EmpleadoTrabajoAnterior> implements EmpleadoTrabajoAnteriorDAO {
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
	public List<EmpleadoTrabajoAnterior> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from EmpleadoTrabajoAnterior model where model.idEmpleado.idEmpresa = :idEmpresa and model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoTrabajoAnterior> findAll() {
		try {
			final String queryString = "select model from EmpleadoTrabajoAnterior model where model.idEmpleado.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoTrabajoAnterior> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from EmpleadoTrabajoAnterior model where model.idEmpleado.idEmpresa = :idEmpresa " + 
			("".equals(value) || value == null ? "" : " and model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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