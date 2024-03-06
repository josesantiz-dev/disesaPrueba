package net.giro.compras.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.comun.ExcepConstraint;

@Stateless
public class RequisicionImp extends DAOImpl<Requisicion> implements RequisicionDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Requisicion entity, long codigoEmpresa) throws ExcepConstraint {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Requisicion> saveOrUpdateList(List<Requisicion> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findAll(long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idEmpresa, long idPropietario, String orderBy, int limite) throws Exception {
		String queryString = "select model from Requisicion model where model.idEmpresa = :idEmpresa ";
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.sistema when 0 then 1 when 1 then 2 else 3 end, model.fecha desc, model.id desc");
			
			if (idObra > 0)
				queryString += "and model.idObra = :idObra ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.estatus >= 0 ";
			if (! incluyeEliminadas)
				queryString += "and model.estatus <> 1 ";
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			if (idPropietario > 0)
				queryString += "and model.creadoPor = :idPropietario ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0)
				query.setParameter("idObra", idObra);
			if (idPropietario > 0)
				query.setParameter("idPropietario", idPropietario);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findLike(String value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idEmpresa, long idPropietario, String orderBy, int limite) throws Exception {
		String queryString = "select model from Requisicion model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.sistema when 0 then 1 when 1 then 2 else 3 end, model.fecha desc, model.id desc");
			
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue "
						+ "or cast(model.idObra as string) like :propertyValue "
						+ "or cast(model.idSolicita as string) like :propertyValue "
						+ "or lower(trim(model.nombreObra)) like lower(:propertyValue) "
						+ "or lower(trim(model.nombreSolicita)) like lower(:propertyValue)) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}

			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.estatus >= 0 ";
			if (! incluyeEliminadas)
				queryString += "and model.estatus <> 1 ";
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			if (idPropietario > 0)
				queryString += "and model.creadoPor = :idPropietario ";
			queryString += "order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (idPropietario > 0)
				query.setParameter("idPropietario", idPropietario);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findLikeProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idEmpresa, long idPropietario, String orderBy, int limite) throws Exception {
		String queryString = "select model from Requisicion model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.sistema when 0 then 1 when 1 then 2 else 3 end, model.fecha desc, model.id desc");
			
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}

			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.estatus >= 0 ";
			if (! incluyeEliminadas)
				queryString += "and model.estatus <> 1 ";
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			if (idPropietario > 0)
				queryString += "and model.creadoPor = :idPropietario ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (value != null && ! "".equals(value.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (idPropietario > 0)
				query.setParameter("idPropietario", idPropietario);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findByProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idEmpresa, long idPropietario, String orderBy, int limite) throws Exception {
		String queryString = "select model from Requisicion model where model.idEmpresa = :idEmpresa ";
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.sistema when 0 then 1 when 1 then 2 else 3 end, model.fecha desc, model.id desc");
			
			if (value != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.estatus >= 0 ";
			if (! incluyeEliminadas)
				queryString += "and model.estatus <> 1 ";
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			if (idPropietario > 0)
				queryString += "and model.creadoPor = :idPropietario ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (idPropietario > 0)
				query.setParameter("idPropietario", idPropietario);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Requisicion> findByProperties(HashMap<String, Object> params, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idEmpresa, long idPropietario, String orderBy, int limite) throws Exception {
		String queryString = "select model from Requisicion model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.sistema when 0 then 1 when 1 then 2 else 3 end, model.fecha desc, model.id desc");
			
			if (params != null && ! params.isEmpty()) {
				for (Entry<String, Object> e : params.entrySet()) {
					switch (e.getValue().getClass().getName()) {
						case "java.lang.Integer": 
							queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
							break;
						case "java.lang.Long": 
							queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
							break;
						case "java.lang.Double": 
							queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
							break;
						case "java.math.BigDecimal": 
							queryString += "and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
							break;
						case "java.util.Date": 
							queryString += "and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
							break;
						default:
							queryString += "and lower(trim(model." + e.getKey() + ")) = '" + e.getValue().toString() + "' ";
							break;
					}
				}
			}

			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.estatus >= 0 ";
			if (! incluyeEliminadas)
				queryString += "and model.estatus <> 1 ";
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			if (idPropietario > 0)
				queryString += "and model.creadoPor = :idPropietario ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (idPropietario > 0)
				query.setParameter("idPropietario", idPropietario);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-19 | Javier Tirado 	| Añado los metodos estatus, findByProperties y findLikeProperties. 
 */