package net.giro.compras.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.compras.beans.Cotizacion;

@Stateless
public class CotizacionImp extends DAOImpl<Cotizacion> implements CotizacionDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Cotizacion entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Cotizacion> saveOrUpdateList(List<Cotizacion> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findAll(long idObra, int estatus, String orderBy) throws Exception {
		String queryString = "select model from Cotizacion model where model.idObra = :idObra ";
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.sistema when 0 then 1 when 1 then 2 else 3 end, model.fecha desc, model.id desc, model.folio desc");
			if (idObra <= 0L)
				return null;
			if (estatus >= 0)
				queryString += "and model.estatus = :estatus ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			if (estatus >= 0)
				query.setParameter("estatus", estatus);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findLike(String value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.sistema when 0 then 1 when 1 then 2 else 3 end, model.fecha desc, model.id desc, model.folio desc");
			
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue "
						+ "or cast(model.idObra as string) like :propertyValue "
						+ "or cast(model.idRequisicion as string) like :propertyValue "
						+ "or cast(model.idProveedor as string) like :propertyValue "
						+ "or cast(model.idComprador as string) like :propertyValue "
						+ "or lower(trim(model.folio)) like :propertyValue "
						+ "or lower(trim(model.nombreObra)) like :propertyValue "
						+ "or lower(trim(model.nombreProveedor)) like :propertyValue "
						+ "or lower(trim(model.rfcProveedor)) like :propertyValue "
						+ "or lower(trim(model.nombreComprador)) like :propertyValue) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}

			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.estatus in (0" + ((incluyeSuministradas) ? ",2" : "") + ((incluyeCanceladas) ? ",1" : "") + ") ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findLikeProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.sistema when 0 then 1 when 1 then 2 else 3 end, model.fecha desc, model.id desc, model.folio desc");
			
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
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "and model.estatus in (0" + ((incluyeSuministradas) ? ",2" : "") + ((incluyeCanceladas) ? ",1" : "") + ") ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cotizacion> findByProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Cotizacion model where model.idEmpresa = :idEmpresa ";
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.sistema when 0 then 1 when 1 then 2 else 3 end, model.fecha desc, model.id desc, model.folio desc");
			
			if (value != null)
				queryString += "and model."+ propertyName + " = :propertyValue ";
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			queryString += "and model.estatus in (0" + ((incluyeSuministradas) ? ",2" : "") + ((incluyeCanceladas) ? ",1" : "") + ") ";
			queryString += "and model.sistema in (0" + (incluyeSistema ? ",1" : "") + ") ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor, long idEmpresa) throws Exception {
		String queryString = "select (COUNT(model.idProveedor) + 1) from Cotizacion model where model.idEmpresa = :idEmpresa and model.idProveedor = :idProveedor";
		Long consecutivo;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			if (idProveedor <= 0L) 
				return 0;
			
			Query query = entityManager.createQuery(queryString, Long.class);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idProveedor", idProveedor);
			consecutivo = (Long) query.getSingleResult();
			if (consecutivo == null)
				consecutivo = 0L;
			return consecutivo.intValue();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Cotizacion> busquedaDinamica(String value, long idEmpresa) throws Exception {
		return this.findLike(value, 0L, 0, true, false, true, idEmpresa, "", 0);
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. 
 */