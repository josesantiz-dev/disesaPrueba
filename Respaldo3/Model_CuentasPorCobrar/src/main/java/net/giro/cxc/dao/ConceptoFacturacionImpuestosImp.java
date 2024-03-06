package net.giro.cxc.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.ConceptoFacturacionImpuestos;

@Stateless
public class ConceptoFacturacionImpuestosImp  extends DAOImpl<ConceptoFacturacionImpuestos> implements ConceptoFacturacionImpuestosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ConceptoFacturacionImpuestos entity) throws Exception {
		try {
			return super.save(entity, 1L);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ConceptoFacturacionImpuestos> saveOrUpdateList(List<ConceptoFacturacionImpuestos> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, 1L);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacionImpuestos> findAll(long idConceptoFacturacion) {
		try {
			final String queryString = "select model from ConceptoFacturacionImpuestos model where model.idConceptoFacturacion = :idConceptoFacturacion ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idConceptoFacturacion", idConceptoFacturacion);
			return query.getResultList();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacionImpuestos> findByProperty(String propertyName, Object value, int limite) {
		String queryString = "select model from ConceptoFacturacionImpuestos model ";
		
		try {
			if (value != null)
				queryString += " where model." + propertyName + " = :propertyValue ";
			queryString += "order by model.id desc";

			Query query = entityManager.createQuery(queryString);
        	if (value != null && ! "".equals(value.toString()))
    			query.setParameter("propertyValue", value);
        	if (limite > 0)
        		query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConceptoFacturacionImpuestos> findLikeProperty(String propertyName, String value, int limite) {
		String queryString = "select model from ConceptoFacturacionImpuestos model ";
		
		try {
			value = validateString(value);
			if (value != null && ! "".equals(value.trim()))
				queryString += " where lower(cast(model." + propertyName + " as string)) like '%" + value + "%' ";
			queryString += "order by model.id desc";

			Query query = entityManager.createQuery(queryString);
        	if (value != null && ! "".equals(value.trim()))
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
		value = value.trim().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
		value = value.trim().replace("Ä", "A").replace("Ë", "E").replace("Ï", "I").replace("Ö", "O").replace("Ü", "U");
		value = value.trim().replace("ä", "a").replace("ë", "e").replace("ï", "i").replace("ö", "o").replace("ü", "u");
		
		return value;
	}
}
