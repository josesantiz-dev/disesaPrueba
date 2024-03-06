package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.inventarios.beans.SolicitudBodegaEstatus;

@Stateless
public class SolicitudBodegaEstatusImp extends DAOImpl<SolicitudBodegaEstatus> implements SolicitudBodegaEstatusDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(SolicitudBodegaEstatus entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<SolicitudBodegaEstatus> findAll(long idSolicitud, String orderBy) throws Exception {
		String queryString = "select model from SolicitudBodegaEstatus model where model.idSolicitud = :idSolicitud ";

		try {
			if (orderBy == null || "".equals(orderBy))
				orderBy = "model.id desc";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idSolicitud", idSolicitud);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SolicitudBodegaEstatus> findAllByMovimiento(long idSolicitud, long idMovimiento) throws Exception {
		String queryString = "select model from SolicitudBodegaEstatus model where model.idSolicitud = :idSolicitud and model.idMovimiento = :idMovimiento ";

		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idSolicitud", idSolicitud);
			query.setParameter("idMovimiento", idMovimiento);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SolicitudBodegaEstatus> findAllByTraspaso(long idSolicitud, long idTraspaso) throws Exception {
		String queryString = "select model from SolicitudBodegaEstatus model where model.idSolicitud = :idSolicitud and model.idTraspaso = :idTraspaso ";

		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idSolicitud", idSolicitud);
			query.setParameter("idTraspaso", idTraspaso);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  3.1 | 2019-07-16 | Javier Tirado 	| Creacion de EJB
 */
