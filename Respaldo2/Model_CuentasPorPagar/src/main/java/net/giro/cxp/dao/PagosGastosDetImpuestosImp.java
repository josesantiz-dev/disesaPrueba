package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetImpuestos;

@Stateless
public class PagosGastosDetImpuestosImp extends DAOImpl<PagosGastosDetImpuestos> implements PagosGastosDetImpuestosDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	public PagosGastosDetImpuestos findById(Long id) {
		try {
			PagosGastosDetImpuestos instance = entityManager.find(PagosGastosDetImpuestos.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PagosGastosDetImpuestos> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from PagosGastosDetImpuestos model where model." + propertyName + "= :propertyValue";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
		
	@SuppressWarnings("unchecked")
	public List<PagosGastosDetImpuestos> findAll() {		
		try {
			final String queryString = "select model from PagosGastosDetImpuestos model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
		
	@SuppressWarnings("unchecked")
	public List<PagosGastosDetImpuestos> findLikePojoCompleto(Object value, int max) {
		String queryString = "";
		PagosGastosDet aux = null;
		
		try {
			if (value == null)
				return null;
			
			queryString = "select model from PagosGastosDetImpuestos model where model.idPagosGastosDet = :propertyValue";
			aux = (PagosGastosDet) value;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", aux.getId());
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PagosGastosDetImpuestos> findImptos2DetGtos (Object value,int max){
		try {
			String queryString = "select model from PagosGastosDetImpuestos imp " +
								   "inner join fetch model.idPagosGastosDet fact " +
								   "inner join fetch fact.pagosGastosId gasto " +								   								   
								   "inner join fetch model.impuestoId tipoImp "+							   			  
								   "where fact =:propertyValue " +
								   "order by tipoImp.valorId";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
