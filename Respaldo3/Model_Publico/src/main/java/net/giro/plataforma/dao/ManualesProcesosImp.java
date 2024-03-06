package net.giro.plataforma.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.beans.ManualesProcesos;

@Stateless
public class ManualesProcesosImp extends DAOImpl<ManualesProcesos> implements ManualesProcesosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ManualesProcesos entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ManualesProcesos> saveOrUpdateList(List<ManualesProcesos> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ManualesProcesos> findAll(String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ManualesProcesos model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "order by model.formato ";
			queryString += orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ManualesProcesos> findLike(String value, boolean incluyeCancelados, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ManualesProcesos model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (lower(model.formato) like :propertyValue ";
				queryString += "or lower(model.nombreArchivo) like :propertyValue ";
				queryString += "or lower(model.descripcion) like :propertyValue) ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase().replace(" ", "%"));
	    		sb.append("%");
			}
			
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1" : "") + ") ";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.formato";
			queryString += "order by " + orderBy;
			
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
	public List<ManualesProcesos> findLikeProperty(String propertyName, Object value, boolean incluyeCancelados, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ManualesProcesos model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				if (value instanceof Date) {
					queryString += "and date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else {
					queryString += "and cast(model."+ propertyName + " as string) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}
			
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1" : "") + ") ";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.formato";
			queryString += "order by " + orderBy;
			
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
	public List<ManualesProcesos> findByProperty(String propertyName, Object value, boolean incluyeCancelados, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ManualesProcesos model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				if (value instanceof Date) {
					queryString += "and date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else {
					queryString += "and model."+ propertyName + " = :propertyValue ";
				}
			}
			
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1" : "") + ") ";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.formato";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
