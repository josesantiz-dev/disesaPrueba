package net.giro.cxc.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.cxc.beans.FacturasAplicarPagos;

@Stateless
public class FacturasAplicarPagosImp extends DAOImpl<FacturasAplicarPagos> implements FacturasAplicarPagosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(FacturasAplicarPagos entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<FacturasAplicarPagos> saveOrUpdateList(List<FacturasAplicarPagos> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturasAplicarPagos> pagosAplicables(long idFactura) throws Exception {
		String queryString = "select model from FacturasAplicarPagos model where model.idFactura = :idFactura order by model.idFactura";
    	
    	try {
    		if (idFactura <= 0L)
    			return null;
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idFactura", idFactura);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}

	@Override
	public FacturasAplicarPagos comprobarPagoAplicable(long idFacturaOriginal, long idFactura) throws Exception {
		String queryString = "select model from FacturasAplicarPagos model where model.idFacturaOriginal = :idFacturaOriginal and model.idFactura = :idFactura order by model.idFactura";
    	
    	try {
    		if (idFacturaOriginal <= 0L || idFactura <= 0L)
    			return null;
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idFacturaOriginal", idFacturaOriginal);
			query.setParameter("idFactura", idFactura);
			return (FacturasAplicarPagos) query.getSingleResult();
    	} catch (Exception e) {
    		throw e;
    	}
	}
}
