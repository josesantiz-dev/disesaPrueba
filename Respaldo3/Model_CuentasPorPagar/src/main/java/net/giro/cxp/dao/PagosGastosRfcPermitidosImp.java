package net.giro.cxp.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.cxp.beans.PagosGastosRfcPermitidos;

@Stateless
public class PagosGastosRfcPermitidosImp extends DAOImpl<PagosGastosRfcPermitidos> implements PagosGastosRfcPermitidosDAO {
	@PersistenceContext 
	private EntityManager entityManager;

	@Override 
	public long save(PagosGastosRfcPermitidos entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosRfcPermitidos> saveOrUpdateList( List<PagosGastosRfcPermitidos> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastosRfcPermitidos> findAll(String rfc, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from PagosGastosRfcPermitidos model where model.estatus = 0 and model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0L)
				idEmpresa = 1;
			if (rfc != null && ! "".equals(rfc.trim())) {
				queryString += "and lower(trim(model.rfc)) like :rfc ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(rfc.trim().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy == null || "".equals(orderBy.trim())) 
				orderBy = "model.rfc, model.tipoPagosGastos ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (rfc != null && ! "".equals(rfc.trim()))
				query.setParameter("rfc", sb.toString());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastosRfcPermitidos> findByProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from PagosGastosRfcPermitidos model where model.estatus = 0 and model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0L)
				idEmpresa = 1;
			if (value != null) 
				queryString += "and model." + propertyName + " = :propertyValue ";
			if (orderBy == null || "".equals(orderBy.trim())) 
				orderBy = "model.rfc, model.tipoPagosGastos ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastosRfcPermitidos> findLikeProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from PagosGastosRfcPermitidos model where model.estatus = 0 and model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0L)
				idEmpresa = 1;
			if (value != null && ! "".equals(value.toString())) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(" + propertyName + ") = date(:propertyValue) ";
					value = formateador.format((Date) value);
				} else {
					queryString += "and cast(model." + propertyName + " as string) like :propertyValue ";
		    		value = value.toString().trim();
				} 
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().trim().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy == null || "".equals(orderBy.trim())) 
				orderBy = "model.rfc, model.tipoPagosGastos ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
}
