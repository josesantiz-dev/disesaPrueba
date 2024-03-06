package net.giro.rh.admon.dao;

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
import net.giro.rh.admon.beans.Checador;

@Stateless
public class ChecadorImp extends DAOImpl<Checador> implements ChecadorDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Checador entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Checador> saveOrUpdateList(List<Checador> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Checador> findAll(long idObra, String orderBy) throws Exception {
		String queryString = "select model from Checador model where model.idObra = :idObra ";
		
		try {
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Checador> findLike(String value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		List<String> valores = null;
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			if (value != null && ! "".equals(value.toString().trim())) {
				queryString += "and (cast(model.id as string) LIKE :propertyValue " 
						+ "or cast(model.idObra as string) LIKE :propertyValue " 
						+ "or lower(trim(model.nombreObra)) LIKE :propertyValue) ";

				valores = recuperaValores(value.toString().toLowerCase().trim(), "\\+");
				if (valores == null || valores.isEmpty()) {
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.trim().toLowerCase());
		    		sb.append("%");
				}
			} 

			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "model.fecha desc, model.id desc";
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (sb != null && ! "".equals(sb.toString().trim()))
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
	public List<Checador> findLikeProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = null;
		StringBuffer sb = null;
		
		try {
			formateador = new SimpleDateFormat("MM/dd/yyyy");
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			if (value != null && ! "".equals(value.toString().trim())) {
				if (java.util.Date.class == value.getClass()) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else {
					queryString += "and lower(cast(model." + propertyName + " as string)) LIKE :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().toLowerCase());
		    		sb.append("%");
				}
			}
			
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString().trim()))
				query.setParameter("propertyValue", sb.toString().trim().toLowerCase());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Checador> findByProperty(String propertyName, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			if (value != null)
				queryString += "and model."+ propertyName + " = :propertyValue ";
			queryString += "order by " + orderBy;
			
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
	public List<Checador> findByDate(Date fecha, String obra, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = null;
		
		try {
			if (fecha == null)
				return new ArrayList<Checador>();
			formatter = new SimpleDateFormat("MM/dd/yyyy");
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? "CASE model.estatus WHEN 0 THEN 0 WHEN 1 THEN 3 WHEN 2 THEN 1 ELSE 4 END, " + orderBy.trim() : "CASE model.estatus WHEN 0 THEN 0 WHEN 1 THEN 3 WHEN 2 THEN 1 ELSE 4 END, model.fecha desc, model.nombreObra");
			
			if (obra != null && "".equals(obra.trim()))
				queryString += "and (cast(model.idObra as string) like '%" + obra.trim().toLowerCase() + "%' or lower(model.nombreObra) like '%" + obra.trim().toLowerCase() + "%') ";
			queryString += "and DATE(model.fecha) = DATE('" + formatter.format(fecha) + "') ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = null;
		
		try {
			if (fechaDesde == null || fechaHasta == null)
				return new ArrayList<Checador>();
			if (fechaDesde.after(fechaHasta))
				return new ArrayList<Checador>();
			
			formatter = new SimpleDateFormat("MM/dd/yyyy");
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			
			queryString += "and DATE(model.fecha) BETWEEN DATE('" + formatter.format(fechaDesde) + "') AND DATE('" + formatter.format(fechaHasta) + "')";
			queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Checador> findByDates(Date fechaDesde, Date fechaHasta, long idObra, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (fechaDesde == null || fechaHasta == null)
				return new ArrayList<Checador>();
			if (fechaDesde.after(fechaHasta))
				return new ArrayList<Checador>();

			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			idObra = (idObra > 0L ? idObra : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			queryString += "and DATE(model.fecha) BETWEEN DATE('" + formatter.format(fechaDesde) + "') AND DATE('" + formatter.format(fechaHasta) + "') ";
			queryString += "order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Checador> asistenciasNominas(Date fechaDesde, Date fechaHasta, long idObra, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from Checador model where model.idEmpresa = :idEmpresa and model.estatus = 2 ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (fechaDesde == null || fechaHasta == null)
				return new ArrayList<Checador>();
			if (fechaDesde.after(fechaHasta))
				return new ArrayList<Checador>();

			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			idObra = (idObra > 0L ? idObra : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			queryString += "and DATE(model.fecha) BETWEEN DATE('" + formatter.format(fechaDesde) + "') AND DATE('" + formatter.format(fechaHasta) + "') ";
			queryString += "order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Checador> findList(List<Long> listValues, String orderBy) throws Exception {
		String queryString = "select model from Checador model where model.id in (:lista) ";
		String lista = "";
		
		try {
			listValues = (listValues != null && ! listValues.isEmpty() ? listValues : new ArrayList<Long>());
			lista = (! listValues.isEmpty() ?  StringUtils.join(listValues, ",") : "0");
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			queryString = queryString.replace(":lista", lista);
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// PRIVADOS 
	// ------------------------------------------------------------------------------------------------------------------------------------
	
	private List<String> recuperaValores(String valor, String separador) {
		List<String> valores = null;
		String[] splitted = null;
		
		if ((valor == null || "".equals(valor.trim())) || (separador == null || "".equals(separador.trim())) || (! valor.trim().contains(separador.trim().replace("\\", ""))))
			return null;
		
		splitted = valor.split(separador);
		valores = new ArrayList<String>();
		for (int i = 0; i < splitted.length; i++)
			valores.add(splitted[i].trim());
		return valores;
	}
	
	private String multiplicaConsulta(String queryOriginal, List<String> valores) {
		String queryModificada = "";
		
		if (valores == null || valores.isEmpty() || valores.size() == 1)
			return queryOriginal;
		
		for (String valor : valores)
			queryModificada += (! "".equals(queryModificada.trim()) ? " or " : "") +  "model.id in (" + queryOriginal.trim().replace("select model from", "select model.id from").replace(":propertyValue", "'%" + valor + "%'") + ")";
		return "select model from Checador model where (" + queryModificada.trim() + ") ";
	}
}
