package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.compras.beans.RequisicionDetalleHistorico;

@Stateless
public class RequisicionDetalleHistoricoImp extends DAOImpl<RequisicionDetalleHistorico> implements RequisicionDetalleHistoricoDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void save(RequisicionDetalleHistorico entity) throws Exception {
		try {
			if (entity == null || entity.getId() == null || entity.getId() <= 0L)
				return;
			entityManager.persist(entity);
			entityManager.flush();
			entityManager.clear();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void saveList(List<RequisicionDetalleHistorico> entities) throws Exception {
		try {
			if (entities == null || entities.isEmpty())
				return;
			for (RequisicionDetalleHistorico entity : entities) {
				entityManager.persist(entity);
				entityManager.flush();
				entityManager.clear();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RequisicionDetalleHistorico> findAll(long idRequisicion) throws Exception {
		String queryString = "select model from RequisicionDetalleHistorico model where model.idRequisicion = :idRequisicion order by id desc; ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idRequisicion", idRequisicion);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
}
