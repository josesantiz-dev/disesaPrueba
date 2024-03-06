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

	@Override
	public long save(EmpleadoTrabajoAnterior entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoTrabajoAnterior> saveOrUpdateList(List<EmpleadoTrabajoAnterior> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoTrabajoAnterior> findByProperty(String propertyName, final Object value, long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoTrabajoAnterior model where model.idEmpleado.idEmpresa = :idEmpresa and model."
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
	public List<EmpleadoTrabajoAnterior> findAll(long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoTrabajoAnterior model where model.idEmpleado.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoTrabajoAnterior> findLikeClaveNombre(String value, long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoTrabajoAnterior model where model.idEmpleado.idEmpresa = :idEmpresa " + 
			("".equals(value) || value == null ? "" : " and model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoTrabajoAnterior> findByPropertyPojoCompleto( String propertyName, String tipo, long value, long idEmpresa) {
		return null;
	}
}
