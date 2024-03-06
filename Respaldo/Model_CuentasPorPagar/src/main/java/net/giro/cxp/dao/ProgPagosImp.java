package net.giro.cxp.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.cxp.beans.ProgPagos;

@Stateless
public class ProgPagosImp extends DAOImpl<ProgPagos> implements ProgPagosDAO  {
	@PersistenceContext
	private EntityManager entityManager;
	
	public ProgPagos findByIdPojoCompleto(long id) {
		try {
			String queryString = "select model from ProgPagos model " +
			 "inner join fetch model.agente suc " +
			 "where lower(model.id) = '"+ id +"' and model.estatus <> 'T' ";
			 
			Query query = entityManager.createQuery(queryString);
			
			return (ProgPagos) query.getSingleResult();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ProgPagos> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from ProgPagos model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ProgPagos> findByProgramaciones(long value, int max) {
		try {
			String queryString = " select model from ProgPagos model " +
								 " where model.agente = :propertyValue and model.estatus <> 'T' and model.total > 0 " +
								 " order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if(max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProgPagos> findByRevisiones(Date fech1, Date fech2, int max) {
		try {
			String queryString = "select model from ProgPagos model " +
								 "inner join fetch model.agente suc " +
								 "where ((model.del between :fech1 and :fech2) or (model.al between :fech1 and :fech2) or (model.del <= :fech1 and model.al >= :fech2)) and "+
								 "model.estatus='P' and model.estatus <> 'T' and model.total > 0 " +
								 "order by model.progPagoId desc";
			
			Query query = entityManager.createQuery(queryString);	
			query.setParameter("fech1", fech1);
			query.setParameter("fech2", fech2);
			if(max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProgPagos> findAll() {
		try {
			final String queryString = "select model from ProgPagos model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			
			throw re;
		}
	}
}
