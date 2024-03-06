package net.giro.inventarios.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.inventarios.beans.AuditoriaInventarios;

@Stateless
public class AuditoriaInventariosImp extends DAOImpl<AuditoriaInventarios> implements AuditoriaInventariosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(AuditoriaInventarios entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AuditoriaInventarios> saveOrUpdateList(List<AuditoriaInventarios> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AuditoriaInventarios> findAll(long idProducto, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from AuditoriaInventarios model where model.idEmpresa = :idEmpresa and model.idProducto = :idProducto ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null ? orderBy : "model.id desc");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idProducto", idProducto);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AuditoriaInventarios> findByProperty(String propertyName, Object propertyValue, long idUsuario, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from AuditoriaInventarios model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			if (propertyValue != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			if (idUsuario > 0L)
				queryString += "and model.creadoPor = :idUsuario ";
			orderBy = (orderBy != null ? orderBy : "model.id desc");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idUsuario > 0L)
				query.setParameter("idUsuario", idUsuario);
			if (propertyValue != null)
				query.setParameter("propertyValue", propertyValue);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AuditoriaInventarios> findLikeProperty(String propertyName, Object propertyValue, long idUsuario, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from AuditoriaInventarios model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			if (propertyValue != null) {
				if (propertyValue.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) propertyValue) + "') ";
					propertyValue = null;
				} else if (! "".equals(propertyValue.toString().trim())) {
					queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(propertyValue.toString().trim().toLowerCase());
		    		sb.append("%");
				}
			}
			
			if (idUsuario > 0L)
				queryString += "and model.creadoPor = :idUsuario ";
			orderBy = (orderBy != null ? orderBy : "model.id desc");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idUsuario > 0L)
				query.setParameter("idUsuario", idUsuario);
			if (propertyValue != null && ! "".equals(propertyValue.toString().trim()))
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
	public List<AuditoriaInventarios> findByDates(Date fechaDesde, Date fechaHasta, long idProducto, long idUsuario, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from AuditoriaInventarios model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			queryString += "and date(model.fecha) between date(:fechaDesde) and date(:fechaHasta) ";
			if (idProducto > 0L)
				queryString += "and model.idProducto = :idProducto ";
			if (idUsuario > 0L)
				queryString += "and model.creadoPor = :idUsuario ";
			orderBy = (orderBy != null ? orderBy : "model.id desc");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("fechaDesde", fechaDesde);
			query.setParameter("fechaHasta", fechaHasta);
			if (idProducto > 0L)
				query.setParameter("idProducto", idProducto);
			if (idUsuario > 0L)
				query.setParameter("idUsuario", idUsuario);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2019-11-29 | Javier Tirado 	| Creacion de EJB
 */
