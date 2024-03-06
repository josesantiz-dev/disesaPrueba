package net.giro.cxc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.cxc.beans.FacturaTimbre;

@Stateless
public class FacturaTimbreImp extends DAOImpl<FacturaTimbre> implements FacturaTimbreDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(FacturaTimbre entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public long saveOrUpdate(FacturaTimbre entity, long codigoEmpresa) throws Exception {
		long idTimbre = 0L;
		
		try {
			if (entity.getId() == null || entity.getId() <= 0L)
				idTimbre = this.save(entity, codigoEmpresa);
			else
				super.update(entity);
			return ((entity.getId() != null && entity.getId() > 0L) ? entity.getId() : idTimbre);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<FacturaTimbre> saveOrUpdateList(List<FacturaTimbre> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaTimbre> findAll(long idEmpresa) throws Exception {
		String queryString = "select model from FacturaTimbre model where model.idEmpresa = :idEmpresa ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<FacturaTimbre> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from FacturaTimbre model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (value != null) {
				if (java.util.Date.class == value.getClass()) {
					queryString += " and date(model."+ propertyName + ") = date(:propertyValue) ";
					value = formatter.format((Date) value);
				} else {
					queryString += " and model."+ propertyName + " = :propertyValue ";
				}
			}
	
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
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
	public List<FacturaTimbre> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
    	StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					value = validateString(value.toString());
					queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}

        	Query query = entityManager.createQuery(queryString);   
        	query.setParameter("idEmpresa", idEmpresa);
        	if (value != null && ! "".equals(value.toString()))
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
	public List<FacturaTimbre> comprobarTimbre(String serie, String folio, long idEmpresa) throws Exception {
		String queryString = "select model from FacturaTimbre model where model.idEmpresa = :idEmpresa and model.serie = :serie and model.folio = :folio order by model.id desc ";
		
		try {
			serie = validateString(serie);
			folio = validateString(folio);
			if (serie == null || "".equals(serie.trim()) || folio == null || "".equals(folio.trim()))
				return null;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("serie", serie);
			query.setParameter("folio", folio);
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
