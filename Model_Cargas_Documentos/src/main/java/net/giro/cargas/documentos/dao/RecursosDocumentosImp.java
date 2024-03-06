package net.giro.cargas.documentos.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.giro.DAOImplFact;
import net.giro.cargas.documentos.beans.RecursosDocumentos;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

@Stateless
public class RecursosDocumentosImp extends DAOImplFact<RecursosDocumentos> implements RecursosDocumentosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(RecursosDocumentos entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<RecursosDocumentos> saveOrUpdateList(List<RecursosDocumentos> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RecursosDocumentos> findLike(String value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from RecursosDocumentos model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombre");
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue "
						+ "or lower(trim(model.nombre)) like lower(:propertyValue) "
						+ "or lower(trim(model.descripcion)) like lower(:propertyValue)) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.trim()))
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
	public List<RecursosDocumentos> findLikeProperty(String property, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from RecursosDocumentos model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombre");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model." + property + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(model." + property + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString().trim()))
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
	public List<RecursosDocumentos> findByProperty(String property, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from RecursosDocumentos model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombre");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model." + property + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and model." + property + " = :propertyValue ";
				}
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString().trim()))
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
	public List<RecursosDocumentos> findAllById(List<Long> listIds, int tipo, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from RecursosDocumentos model where model.idEmpresa = :idEmpresa and model.tipo = :tipo and model.id in (:lista) ";
		String lista = "";
		
		try {
			if (listIds == null || listIds.isEmpty()) 
				return null;
			
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			lista = StringUtils.join(listIds, ",");
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombre");
			queryString = queryString.replace(":lista", lista);
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("tipo", tipo);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
}
