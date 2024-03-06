package net.giro.inventarios.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.inventarios.beans.TraspasoBodegaBodega;

@Stateless
public class TraspasoBodegaBodegaImp extends DAOImpl<TraspasoBodegaBodega> implements TraspasoBodegaBodegaDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(TraspasoBodegaBodega entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public long save(long idTraspaso, long idObraOrigen, long idObraDestino, long codigoEmpresa) throws Exception {
		try {
			return this.save(new TraspasoBodegaBodega(idTraspaso, idObraOrigen, idObraDestino), codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public TraspasoBodegaBodega findByTraspaso(long idTraspaso) {
		String queryString = "select model from TraspasoBodegaBodega model ";
		List<TraspasoBodegaBodega> traspasos = null;
		
		try {
			queryString += "where model.idTraspaso = :idTraspaso and model.estatus = 0 ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idTraspaso", idTraspaso);
			traspasos = query.getResultList();
			if (traspasos != null && ! traspasos.isEmpty())
				return traspasos.get(0);
			return null;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TraspasoBodegaBodega> findAll(long idTraspaso) {
		String queryString = "select model from TraspasoBodegaBodega model ";
		
		try {
			queryString += "where model.idTraspaso = :idTraspaso ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idTraspaso", idTraspaso);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TraspasoBodegaBodega> findAllObraOrigen(long idObra) {
		String queryString = "select model from TraspasoBodegaBodega model ";
		
		try {
			queryString += "where model.idObraOrigen = :idObra ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TraspasoBodegaBodega> findAllObraDestino(long idObra) {
		String queryString = "select model from TraspasoBodegaBodega model ";
		
		try {
			queryString += "where model.idObraDestino = :idObra ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
