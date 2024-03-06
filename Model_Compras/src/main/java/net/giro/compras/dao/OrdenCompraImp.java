package net.giro.compras.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.compras.beans.OrdenCompra;

@Stateless
public class OrdenCompraImp extends DAOImpl<OrdenCompra> implements OrdenCompraDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(OrdenCompra entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<OrdenCompra> saveOrUpdateList(List<OrdenCompra> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findAll(long idObra, boolean incluyeSistema, boolean incluyeCanceladas, String orderBy) throws Exception {
		String queryString = "select model from OrdenCompra model where model.idObra = :idObra ";
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.sistema, extract(year from model.fecha) desc, extract(month from model.fecha) desc, model.id desc, model.folio desc");
			if (idObra <= 0L)
				return null;
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1" : "") + ",2) ";
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
	public List<OrdenCompra> findLike(String value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from OrdenCompra model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.sistema, extract(year from model.fecha) desc, extract(month from model.fecha) desc, model.id desc, model.folio desc");
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue "
						+ "or cast(model.idCotizacion.id as string) like :propertyValue "
						+ "or cast(model.idCotizacion.idRequisicion as string) like :propertyValue "
						+ "or cast(model.idObra as string) like :propertyValue "
						+ "or cast(model.idProveedor as string) like :propertyValue "
						+ "or cast(model.idSolicita as string) like :propertyValue "
						+ "or lower(trim(model.folio)) like lower(:propertyValue) "
						+ "or lower(trim(model.idCotizacion.folio)) like :propertyValue "
						+ "or lower(trim(model.lugarEntrega)) like lower(:propertyValue) "
						+ "or lower(trim(model.nombreObra)) like lower(:propertyValue) "
						+ "or lower(trim(model.nombreProveedor)) like lower(:propertyValue) "
						+ "or lower(trim(model.nombreSolicita)) like lower(:propertyValue) "
						+ "or lower(trim(model.referencia)) like lower(:propertyValue)) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}

			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			if (tipoMaestro > 0)
				queryString += "and model.tipo = :tipoMaestro ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.autorizado in (" + (autorizadas ? "1" : "0,1") + ") ";
			queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1" : "") + ",2) ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (tipoMaestro > 0)
				query.setParameter("tipoMaestro", tipoMaestro);
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
	public List<OrdenCompra> findLikeProperty(String property, Object value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from OrdenCompra model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.sistema, extract(year from model.fecha) desc, extract(month from model.fecha) desc, model.id desc, model.folio desc");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(model." + property + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}
			
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			if (tipoMaestro > 0)
				queryString += "and model.tipo = :tipoMaestro ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.autorizado in (" + (autorizadas ? "1" : "0,1") + ") ";
			queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1" : "") + ",2) ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (tipoMaestro > 0)
				query.setParameter("tipoMaestro", tipoMaestro);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findByProperty(String property, Object value, long idObra, int tipoMaestro, boolean autorizadas, boolean incluyeSistema, boolean incluyeCanceladas, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from OrdenCompra model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.sistema, extract(year from model.fecha) desc, extract(month from model.fecha) desc, model.id desc, model.folio desc");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and model."+ property + " = :propertyValue ";
				}
			}
			
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			if (tipoMaestro > 0)
				queryString += "and model.tipo = :tipoMaestro ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.autorizado in (" + (autorizadas ? "1" : "0,1") + ") ";
			queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1" : "") + ",2) ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString().trim()))
				query.setParameter("propertyValue", value);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (tipoMaestro > 0)
				query.setParameter("tipoMaestro", tipoMaestro);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findByObra(long idObra, int estatus, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from OrdenCompra model where model.idEmpresa = :idEmpresa ";
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.sistema, extract(year from model.fecha) desc, extract(month from model.fecha) desc, model.id desc, model.folio desc");
			
			queryString += "and model.idObra = :idObra ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			if (estatus >= 0)
				queryString += "and model.estatus = :estatus ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idObra", idObra);
			if (estatus > 0)
				query.setParameter("estatus", estatus);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor, long idEmpresa) throws Exception {
		String queryString = "select (COUNT(model.idProveedor) + 1) from OrdenCompra model where model.idEmpresa = :idEmpresa and model.idProveedor = :propertyValue ";
		Long consecutivo;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			if (idProveedor <= 0L) 
				return 0;
			Query query = entityManager.createQuery(queryString, Long.class);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", idProveedor);
			consecutivo = (Long) query.getSingleResult();
			if (consecutivo == null)
				consecutivo = 0L;
			return consecutivo.intValue();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findNoCompletas(String property, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from OrdenCompra model where (model.estatus = 0 or model.estatus = 2 and model.completa = 0) and model.sistema = 0 and model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.sistema, extract(year from model.fecha) desc, extract(month from model.fecha) desc, model.id desc, model.folio desc");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
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
	public List<OrdenCompra> findNoAutorizadas(String property, Object value, long idObra, boolean incluyeCanceladas, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from OrdenCompra model where model.autorizado = 0 and model.sistema = 0 and model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.sistema, extract(year from model.fecha) desc, extract(month from model.fecha) desc, model.id desc, model.folio desc");
			if (idObra > 0)
				queryString += "and model.idObra = :idObra ";
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(model." + property + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}
			
			queryString += "and estatus in (0" + (incluyeCanceladas ? ",1" : "") + ",2) ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0)
				query.setParameter("idObra", idObra);
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
}
