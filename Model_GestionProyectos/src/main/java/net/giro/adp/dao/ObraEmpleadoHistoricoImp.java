package net.giro.adp.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.ObraEmpleado;
import net.giro.adp.beans.ObraEmpleadoHistorico;

@Stateless
public class ObraEmpleadoHistoricoImp extends DAOImpl<ObraEmpleadoHistorico> implements ObraEmpleadoHistoricoDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(ObraEmpleadoHistorico entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(long idObra, long idEmpleado, Date fechaAsignacion, long eliminadoPor, long codigoEmpresa) throws Exception {
		ObraEmpleadoHistorico entity = null;
		
		try {
			entity = new ObraEmpleadoHistorico();
			entity.setIdObra(idObra);
			entity.setIdEmpleado(idEmpleado);
			entity.setEliminadoPor(eliminadoPor);
			entity.setFechaAsignacion(fechaAsignacion);
			entity.setFechaTerminacion(Calendar.getInstance().getTime());
			return this.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(ObraEmpleado historico, long codigoEmpresa) throws Exception {
		ObraEmpleadoHistorico entity = null;
		
		try {
			entity = new ObraEmpleadoHistorico();
			entity.setIdObra(historico.getIdObra().getId());
			entity.setIdEmpleado(historico.getIdEmpleado());
			entity.setFechaAsignacion(historico.getFechaCreacion());
			entity.setFechaTerminacion(Calendar.getInstance().getTime());
			return this.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ObraEmpleadoHistorico> saveOrUpdateList(List<ObraEmpleadoHistorico> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraEmpleadoHistorico> findByObra(long idObra) {
		String queryString = "select model from ObraEmpleadoHistorico model where model.idObra = :idObra ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraEmpleadoHistorico> findByEmpleado(long idEmpleado) {
		String queryString = "select model from ObraEmpleadoHistorico model where model.idEmpleado = :idEmpleado ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpleado", idEmpleado);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
