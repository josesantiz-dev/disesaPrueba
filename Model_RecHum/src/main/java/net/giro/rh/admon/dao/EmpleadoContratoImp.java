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

	@Override
	public long save(EmpleadoContrato entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoContrato> saveOrUpdateList(List<EmpleadoContrato> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoContrato> findAll(long idEmpleado, String orderBy, boolean incluyeContratosCancelados, boolean incluyeEmpleadosSistema, long idEmpresa) throws Exception {
		String queryString = "select model from EmpleadoContrato model where model.idEmpleado.idEmpresa = :idEmpresa ";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.id desc, model.estatus, model.determinado";
			if (idEmpleado > 0L)
				queryString += "and model.idEmpleado.id = :idEmpleado ";
			else
				queryString += "and model.idEmpleado.sistema in (0" + (incluyeEmpleadosSistema ? ",1) " : ") "); 
			queryString += "and model.estatus in (0" + (incluyeContratosCancelados ? ",1,2" : "") + ") "; 
			queryString += "order by " + orderBy; 
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idEmpleado > 0L)
				query.setParameter("idEmpleado", idEmpleado);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoContrato> findByProperty(String propertyName, final Object value, boolean incluyeContratosCancelados, boolean incluyeEmpleadosSistema, long idEmpresa) throws Exception {
		String queryString = "select model from EmpleadoContrato model where model.idEmpleado.idEmpresa = :idEmpresa and model.estatus = 0 ";
		
		try {
			if (value != null) 
				queryString += "and model." + propertyName + " = :propertyValue ";
			queryString += "and model.idEmpleado.sistema in (0" + (incluyeEmpleadosSistema ? ",1) " : ") "); 
			queryString += "and model.estatus in (0" + (incluyeContratosCancelados ? ",1,2" : "") + ") "; 
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoContrato> contratoValido(long idEmpleado, long idEmpresa) throws Exception {
		try {
			final String queryString = "select model from EmpleadoContrato model where model.idEmpleado.idEmpresa = :idEmpresa "
					+ " and estatus = 0 AND model.idEmpleado.id = "+ idEmpleado + " AND DATE(CURRENT_DATE) BETWEEN DATE(fechaInicio) AND DATE(fechaFin) " 
					+ "ORDER BY fechaInicio DESC";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
