package net.giro.cxc.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import net.giro.DAOImpl;
import net.giro.cxc.beans.FacturaPagos;

@Stateless
public class FacturaPagosImp extends DAOImpl<FacturaPagos> implements FacturaPagosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(FacturaPagos entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<FacturaPagos> saveOrUpdateList(List<FacturaPagos> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findAll(long idFactura, boolean incluyeCanceladas, boolean soloTimbrado, String orderBy) throws Exception {
		String queryString = "select model from FacturaPagos model where model.idFactura.id = :idFactura ";
    	
    	try {
    		idFactura = (idFactura > 0L ? idFactura : 0L);
    		orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "date(model.fecha)");
    		if (soloTimbrado)
        		queryString += "and model.timbrado = 1 ";
    		queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1,2,3" : "") + ") ";
    		queryString += "order by " + orderBy;
    		
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idFactura", idFactura);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findLike(String value, boolean incluyeCanceladas, boolean soloTimbrado, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from FacturaPagos model where model.idFactura.idEmpresa = :idEmpresa ";
    	StringBuffer sb = null;
    	
    	try {
    		idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
    		orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.id desc, coalesce(nullif((model.serie || model.folio), '')) desc");
			value = validateString(value);
    		if (value != null && ! "".equals(value.trim())) {
        		sb = new StringBuffer(); 
        		sb.append("%"); 
        		sb.append(value.toString().trim().toLowerCase()); 
        		sb.append("%"); 

				queryString += "and (lower(trim(model.idFactura.folioFactura)) like :value ";
				queryString += "or lower(trim(model.idFactura.serie)) like :value ";
				queryString += "or lower(trim(model.idFactura.folio)) like :value ";
				queryString += "or lower(trim(model.beneficiario)) like :value ";
				queryString += "or lower(trim(model.formaPago)) like :value ";
				queryString += "or lower(trim(model.referenciaFormaPago)) like :value ";
				queryString += "or lower(trim(model.cuentaBanco)) like :value ";
				queryString += "or lower(trim(model.cuentaNumero)) like :value ";
				queryString += "or lower(trim(model.observaciones)) like :value ";
				queryString += "or lower(trim(model.serie)) like :value ";
				queryString += "or lower(trim(model.folio)) like :value ";
				queryString += "or lower(trim(model.uuid)) like :value ";
				queryString += "or lower(trim(model.agrupador)) like :value ";
				queryString += "or cast(model.idFactura.id as string) like :value ";
				queryString += "or cast(model.id as string) like :value) ";
        	}

    		if (soloTimbrado)
        		queryString += "and model.timbrado = 1 ";
    		queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1,2,3" : "") + ") ";
    		queryString += "order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (value != null && ! "".equals(value.toString()))
            	query.setParameter("value", sb.toString());
        	if (limite > 0)
        		query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findLikeProperty(String propertyName, Object value, boolean incluyeCanceladas, boolean soloTimbrado, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from FacturaPagos model where model.idFactura.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
    	String propertyValue = "";
    	StringBuffer sb = null;
    	
    	try {
    		idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
    		orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			propertyName = (propertyName.contains("model.") ? propertyName.replace("model.", "") : propertyName);
    		if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(" + propertyName + " as string))) like :propertyValue ";
					propertyValue = validateString(value.toString());
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(propertyValue.trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}

    		if (soloTimbrado)
        		queryString += "and model.timbrado = 1 ";
    		queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1,2,3" : "") + ") ";
    		queryString += "order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (value != null && ! "".equals(value.toString().trim()))
            	query.setParameter("propertyValue", sb.toString());
        	if (limite > 0)
        		query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, boolean soloTimbrado, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from FacturaPagos model where model.idFactura.idEmpresa = :idEmpresa ";
    	
    	try {
    		idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
    		orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
        	if (value != null && ! "".equals(value.toString().trim()))
        		queryString += "and model." + propertyName + " = :propertyValue ";
    		if (soloTimbrado)
        		queryString += "and model.timbrado = 1 ";
    		queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1,2,3" : "") + ") ";
    		queryString += "order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (value != null && ! "".equals(value))
            	query.setParameter("propertyValue", value);
        	if (limite > 0)
        		query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findInProperty(String propertyName, List<Long> values, boolean incluyeCanceladas, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from FacturaPagos model inner join fetch model.idFactura fac where fac.idEmpresa = :idEmpresa ";
		String lista = "";
    	
    	try {
    		idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
    		orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			values = (values != null && ! values.isEmpty() ? values : new ArrayList<Long>());
			lista = (! values.isEmpty() ?  StringUtils.join(values, ",") : "0");
			if (values != null && ! values.isEmpty()) {
    			queryString += "and cast(model." + propertyName + " as string) in (:lista) ";
    			queryString = queryString.replace(":lista", lista);
			}
			
    		queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1,2,3" : "") + ") ";
    		queryString += "order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (limite > 0)
        		query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaPagos> findByTimbre(long idTimbre, boolean incluyeCanceladas, String orderBy) throws Exception {
		String queryString = "select model from FacturaPagos model where model.idTimbre = :idTimbre ";
    	
    	try {
    		idTimbre = (idTimbre > 0L ? idTimbre : 0L);
    		orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
    		queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1,2,3" : "") + ") ";
    		queryString += "order by " + orderBy;
    		
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idTimbre", idTimbre);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}

	// ------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------------------
	
	private String validateString(String value) {
		if (value == null || "".equals(value.trim()))
			return "";
		
		value = value.trim().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");
		value = value.trim().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
		value = value.trim().replace("Ä", "A").replace("Ë", "E").replace("Ï", "I").replace("Ö", "O").replace("Ü", "U");
		value = value.trim().replace("ä", "a").replace("ë", "e").replace("ï", "i").replace("ö", "o").replace("ü", "u");
		
		return value;
	}
}
