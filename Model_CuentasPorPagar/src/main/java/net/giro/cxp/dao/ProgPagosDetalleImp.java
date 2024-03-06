package net.giro.cxp.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.cxp.beans.ProgPagosDetalle;

@Stateless
public class ProgPagosDetalleImp extends DAOImpl<ProgPagosDetalle> implements ProgPagosDetalleDAO  {
	@PersistenceContext
	private EntityManager entityManager;
	
	/*public long save(ProgPagosDetalle entity) {		
		try {
			entityManager.persist(entity);
			return entity.getId();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void delete(ProgPagosDetalle entity) {
		try {
			entity = entityManager.getReference(ProgPagosDetalle.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void update(ProgPagosDetalle entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}*/

	public ProgPagosDetalle findById(Long id) {
		try {
			ProgPagosDetalle instance = entityManager.find(ProgPagosDetalle.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProgPagosDetalle> findByProperty(String propertyName,final Object value) {
		try {
			final String queryString = "select model from ProgPagosDetalle model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ProgPagosDetalle> findByPropertyPojoCompleto(String propertyName,final Object value) {
		try {
			final String queryString = "select model " +
									   "from ProgPagosDetalle model " +
									   "inner join fetch model.tiposGastos gto "+
									   "inner join fetch model.progPagos pag "+
									   "where model."+ propertyName + "= :propertyValue";
			
			Query query = entityManager.createQuery(queryString);
			
			query.setParameter("propertyValue", value);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProgPagosDetalle> findByPropertyPojoCompletoMontoNoCero(String propertyName,final Object value) {
		try {
			final String queryString = "select model " +
									   "from ProgPagosDetalle model " +
									   "where model.monto > 0 and model."+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);			
			query.setParameter("propertyValue", value);			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProgPagosDetalle> findByAgenteEstatusMontoConcepto(String agenteValue, String estatusValue, Long conceptoValue) {
		List<ProgPagosDetalle> res = null;
		
		try {
			final String queryString = "select pag.estatus, model " +
									   "from ProgPagosDetalle model " +
									   "inner join fetch model.progPagos pag "+
									   "where pag.agente.id in(" + (agenteValue != null && !"".equals(agenteValue) ? agenteValue :"-1") + ") and coalesce(model.estatus,'G') = :estatusValue " +
									   "and model.tiposGastos.valorId = :conceptoValue "; //and pag.del<= :fec and pag.al>= :fec ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("estatusValue", estatusValue);
			query.setParameter("conceptoValue", conceptoValue);
			res = query.getResultList();
			return res.isEmpty()?null:res;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProgPagosDetalle> findAll() {
		try {
			final String queryString = "select model from ProgPagosDetalle model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProgPagosDetalle> findByMontoConceptoEstatus(Double montoValue, Long conceptoValue, String estatusValue) {
		List<ProgPagosDetalle> res = null;
		
		try {
			final String queryString = "select model from ProgPagosDetalle model " +
					   "where model.montoRev = :montoValue and coalesce(model.estatus,'G') = :estatusValue and model.tiposGastos = :conceptoValue ";
			/*final String queryString = "select model " +
					   "from ProgPagosDetalle model " +
					   "where model.montoRev = :montoValue and coalesce(model.estatus,'G') = :estatusValue " +
					   "and model.tiposGastos.valorId = :conceptoValue ";*/			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("montoValue", new BigDecimal(montoValue).setScale(2,BigDecimal.ROUND_HALF_UP));
			query.setParameter("conceptoValue", conceptoValue);
			query.setParameter("estatusValue", estatusValue);			
			res = query.getResultList();
			
			return res.isEmpty() ? null : res;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
