package net.giro.cxc.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.FacturaDetalle;

@Stateless
public class FacturaDetalleImp extends DAOImpl<FacturaDetalle> implements FacturaDetalleDAO{
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(FacturaDetalle entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<FacturaDetalle> saveOrUpdateList(List<FacturaDetalle> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaDetalle> findAll(long idFactura) {
		try {
			final String queryString = "select model from FacturaDetalle model where model.idFactura = :idFactura";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idFactura", idFactura);
			return query.getResultList();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaDetalle> findByProperty(String propertyName, Object value, long idEmpresa) {
		try {
			final String queryString = "select model from FacturaDetalle model where model." + propertyName + " = :propertyValue ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaDetalle> findLikeProperty(String propertyName, String value, long idEmpresa) {
		String queryString = "select model from FacturaDetalle model ";
		
		try {
			value = validateString(value);
			if (value != null && ! "".equals(value.trim()))
				queryString += " where model." + propertyName + " = '%" + value.trim() + "%' ";
			Query query = entityManager.createQuery(queryString);
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
