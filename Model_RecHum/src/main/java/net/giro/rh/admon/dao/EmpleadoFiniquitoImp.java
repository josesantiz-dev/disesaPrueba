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

	@Override
	public long save(EmpleadoFiniquito entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoFiniquito> saveOrUpdateList(List<EmpleadoFiniquito> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public EmpleadoFiniquito findByIdEmpleado(long idEmpleado, long idContrato, boolean incluyeAprobados, boolean incluyeCancelado) {
		String queryString = "select model from EmpleadoFiniquito model where model.idEmpleado.id = :idEmpleado ";
		List<EmpleadoFiniquito> finiquitos = null;
		
		try {
			idEmpleado = (idEmpleado > 0L ? idEmpleado : 0L);
			idContrato = (idContrato > 0L ? idContrato : 0L);
			if (idContrato > 0L)
				queryString += "and model.idContrato = :idContrato ";
			queryString += "and model.estatus in (0" + (incluyeAprobados ? ",2" : "") + (incluyeCancelado ? ",1" : "") + ") ";
			queryString += "order by case model.estatus when 0 then 1 when 2 then 2 when 1 then 3 else 4 end, model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpleado", idEmpleado);
			if (idContrato > 0L)
				query.setParameter("idContrato", idContrato);
			finiquitos = query.getResultList();
			if (finiquitos != null && ! finiquitos.isEmpty())
				return finiquitos.get(0);
			return null;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoFiniquito> findAll(long idEmpresa) {
		try {
			final String queryString = "select model from EmpleadoFiniquito model where model.idEmpleado.idEmpresa = :idEmpresa order by model.id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoFiniquito> findFiniquitosByEmpleado(long idEmpleado, boolean incluyeAprobados, boolean incluyeCancelado) {
		String queryString = "select model from EmpleadoFiniquito model where model.idEmpleado.id = :idEmpleado ";
		
		try {
			queryString += "and model.estatus in (0" + (incluyeAprobados ? ",2" : "") + (incluyeCancelado ? ",1" : "") + ") ";
			queryString += "order by model.id desc ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpleado", idEmpleado);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoFiniquito> findByEmpleado(String nombreEmpleado, long idEmpresa) {
		String queryString = "select model from EmpleadoFiniquito model where model.idEmpleado.idEmpresa = :idEmpresa ";
		
		try {
			queryString += "and lower(model.idEmpleado.nombre) like '%" + nombreEmpleado.toLowerCase() + "%' ";
			queryString += "order by model.id desc ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoFiniquito> findByProperty(String propertyName, final Object value, long idEmpresa) {
		String queryString = "select model from EmpleadoFiniquito model where model.idEmpleado.idEmpresa = :idEmpresa ";
		
		try {
			queryString += "and model." + propertyName + "= :propertyValue";
			queryString += "order by model.id desc ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
