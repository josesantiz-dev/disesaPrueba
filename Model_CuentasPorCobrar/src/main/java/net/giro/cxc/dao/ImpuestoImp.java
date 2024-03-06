package net.giro.cxc.dao;
import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.Impuesto;

@Stateless
public class ImpuestoImp extends DAOImpl<Impuesto> implements ImpuestoDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Impuesto entity) throws Exception {
		try {
			return super.save(entity, 1L);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Impuesto> saveOrUpdateList(List<Impuesto> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, 1L);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Impuesto> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from Impuesto model where model."+ propertyName + " = :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Impuesto> findLikeProperty(String propertyName, String value) {
		String queryString = "select model from Impuesto model ";
		
		try {
			value = validateString(value);
			if (value != null && ! "".equals(value.trim()))
				queryString += "where lower(cast(model."+ propertyName + " as string)) like '%" + value.trim() + "%' ";
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", value);
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
