package net.giro.plataforma.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.beans.AirReinicios;

@Stateless
public class AirReiniciosImp extends DAOImpl<AirReinicios> implements AirReiniciosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save( AirReinicios entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AirReinicios> saveOrUpdateList(List< AirReinicios> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AirReinicios> findAll(String orderBy) throws Exception {
		String queryString = "select model from AirReinicios model ";
		
		try {
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaProgramada desc");
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AirReinicios> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		String queryString = "select model from AirReinicios model where model.idDiaFestivo.idEmpresa = :idEmpresa and model.estatus = 0 ";
		SimpleDateFormat formateador = null;
		StringBuffer sb = null;
		
		try {
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaProgramada desc");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					formateador = new SimpleDateFormat("MM/dd/yyyy");
					queryString += "and date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
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
	public List< AirReinicios> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		String queryString = "select model from AirReinicios model ";
		
		try {
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaProgramada desc");
			if (value != null)
				queryString += "where model." + propertyName + " = :propertyValue ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
