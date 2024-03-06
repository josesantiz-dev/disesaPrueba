package net.giro.rh.admon.dao;

import java.util.List; 

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.EmpleadoBeneficiario; 

@Stateless
public class EmpleadoBeneficiarioImp extends DAOImpl<EmpleadoBeneficiario> implements EmpleadoBeneficiarioDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(EmpleadoBeneficiario entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoBeneficiario> saveOrUpdateList(List<EmpleadoBeneficiario> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmpleadoBeneficiario> findAll(long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoBeneficiario model where model.idEmpleado.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<EmpleadoBeneficiario> findByProperty(String propertyName, final Object value, long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoBeneficiario model where model.idEmpleado.idEmpresa = :idEmpresa and "
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public List<EmpleadoBeneficiario> findByIdEmpleado(long idEmpleado, long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoBeneficiario model where model.idEmpleado.idEmpresa = :idEmpresa and model.idEmpleado.id = " + idEmpleado;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoBeneficiario> findLikeClaveNombre(String value, long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoBeneficiario model where model.idEmpleado.idEmpresa = :idEmpresa " + 
			("".equals(value) || value == null ? "" : " and model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoBeneficiario> findByPropertyPojoCompleto(String propertyName, String tipo, long value, long idEmpresa) {
		return null;
	}
}
