package net.giro.rh.admon.dao; 

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.EmpleadoPariente;


@Stateless
public class EmpleadoParienteImp extends DAOImpl<EmpleadoPariente> implements EmpleadoParienteDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(EmpleadoPariente entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoPariente> saveOrUpdateList(List<EmpleadoPariente> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoPariente> findByProperty(String propertyName, final Object value, long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoPariente model where model.idEmpleado.idEmpresa = :idEmpresa and model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoPariente> findAll(long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoPariente model where model.idEmpleado.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoPariente> findLikeClaveNombre(String value, long idEmpresa) {
		try {
			final String queryString = "select model from Areas model where model.idEmpleado.idEmpresa = :idEmpresa " + 
			("".equals(value) || value == null ? "" : " where model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoPariente> findByPropertyPojoCompleto( String propertyName, String tipo, long value, long idEmpresa) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoPariente> findByIdEmpleadoParentesco(long idEmpleado, long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoPariente model where model.idEmpleado.idEmpresa = :idEmpresa and model.idEmpleado.id = "+idEmpleado;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
