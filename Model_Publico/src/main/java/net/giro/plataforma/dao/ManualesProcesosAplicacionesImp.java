package net.giro.plataforma.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.beans.ManualesProcesosAplicaciones;

public class ManualesProcesosAplicacionesImp extends DAOImpl<ManualesProcesosAplicaciones> implements ManualesProcesosAplicacionesDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ManualesProcesosAplicaciones entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ManualesProcesosAplicaciones> saveOrUpdateList(List<ManualesProcesosAplicaciones> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ManualesProcesosAplicaciones> findByManualProceso(long idManualProceso, boolean incluyeCancelados, String orderBy) throws Exception {
		String queryString = "select model from ManualesProcesosAplicaciones model where model.idManualProceso = :idManualProceso ";
		
		try {
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1" : "") + ") ";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.idAplicacion.aplicacion";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idManualProceso", idManualProceso);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ManualesProcesosAplicaciones> findByAplicacion(long idAplicacion, boolean incluyeCancelados, String orderBy) throws Exception {
		String queryString = "select model from ManualesProcesosAplicaciones model where model.idAplicacion = :idAplicacion ";
		
		try {
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1" : "") + ") ";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.idManualesProcesos.formato";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idAplicacion", idAplicacion);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
