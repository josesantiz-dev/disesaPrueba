package net.giro.cxc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.ConceptoFacturacion;

@Stateless
public class ConceptoFacturacionImp extends DAOImpl<ConceptoFacturacion> implements ConceptoFacturacionDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ConceptoFacturacion entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<ConceptoFacturacion> saveOrUpdateList(List<ConceptoFacturacion> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacion> findAll(boolean incluyeCanceladas, String orderBy, long idEmpresa) {
		String queryString = "select model from ConceptoFacturacion model where model.idEmpresa = :idEmpresa ";
		
		try {
			queryString += " and model.estatus in (0" + (incluyeCanceladas ? ",1) " : ") ");
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.id desc ";
			queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacion> findLike(String value, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ConceptoFacturacion model where model.idEmpresa = :idEmpresa ";
    	StringBuffer sb = null;
		
		try {
			value = validateString(value);
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (lower(trim(cast(model.id as string))) like :value ";
				queryString += "or lower(trim(model.descripcion)) like :value ";
				queryString += "or lower(trim(model.claveSat)) like :value) ";
				
        		sb = new StringBuffer();
        		sb.append("%");
        		sb.append(value.trim().toLowerCase());
        		sb.append("%");
        	}

			queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1) " : ") ");
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.id desc ";
			queryString += "order by " + orderBy;
			
        	Query query = entityManager.createQuery(queryString);  
        	query.setParameter("idEmpresa", idEmpresa);
        	if (value != null && ! "".equals(value.trim()))
            	query.setParameter("value", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
        	return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacion> findLikeProperty(String propertyName, Object value, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite) {
		String queryString = "select model from ConceptoFacturacion model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
    	String propertyValue = "";
    	StringBuffer sb = null;
    	
		try {
			propertyName = (propertyName.contains("model.") ? propertyName.replace("model.", "") : propertyName);
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					propertyValue = validateString(value.toString());
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(propertyValue.trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}

			queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1) " : ") ");
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = propertyName;
			queryString += "order by model." + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (value != null && !"".equals(value.toString()))
            	query.setParameter("propertyValue", sb.toString());
        	if (limite > 0)
        		query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacion> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite) {
		String queryString = "select model from ConceptoFacturacion model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null)
				queryString += "and model."+ propertyName + " = :propertyValue ";
			queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1) " : ") ");
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.id desc ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (value != null && ! "".equals(value.toString()))
    			query.setParameter("propertyValue", value);
        	if (limite > 0)
        		query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------------------
	
	private String validateString(String value) {
		if (value == null || "".equals(value.trim()))
			return "";
		
		value = value.trim().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");
		value = value.trim().replace("Ä", "A").replace("Ë", "E").replace("Ï", "I").replace("Ö", "O").replace("Ü", "U");
		value = value.trim().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
		value = value.trim().replace("ä", "a").replace("ë", "e").replace("ï", "i").replace("ö", "o").replace("ü", "u");
		
		return value;
	}
}
