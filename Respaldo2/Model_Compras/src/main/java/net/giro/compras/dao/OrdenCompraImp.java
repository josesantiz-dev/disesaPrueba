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
import net.giro.compras.beans.OrdenCompra;
import net.giro.comun.ExcepConstraint;

@Stateless
public class OrdenCompraImp extends DAOImpl<OrdenCompra> implements OrdenCompraDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	private static Long estatus;

	@Override
	public void OrderBy(String orderBy) {
		OrdenCompraImp.orderBy = orderBy;
	}

	@Override
	public void estatus(Long estatus) {
		OrdenCompraImp.estatus = estatus;
	}

	@Override
	public long save(OrdenCompra entity, Long idEmpresa) throws ExcepConstraint {
		try {
			return super.save(entity, idEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<OrdenCompra> saveOrUpdateList(List<OrdenCompra> entities, Long idEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, idEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public OrdenCompra findByCodigo(String codigo, Long idEmpresa) {
		String queryString = "select model from OrdenCompra model where model.idEmpresa = :idEmpresa and model.folio = :codigo";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (codigo != null && "".equals(codigo)) 
				codigo = "CODIGO";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("codigo", codigo);
			return (OrdenCompra) query.getSingleResult();
		} catch (RuntimeException re) {
			throw re;
		} 
	}
	
	@Override
	public List<OrdenCompra> findByProperty(String propertyName, Object value, int limite, Long idEmpresa) throws Exception {
		return this.findByProperty(propertyName, value, 0L, limite, idEmpresa);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findByProperty(String propertyName, Object value, long idObra, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from OrdenCompra model where sistema = 0 and model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			if (value != null)
				queryString += "and model."+ propertyName + " = :propertyValue ";
			
			if (estatus != null)
				queryString += "and estatus = " + estatus + " ";
			estatus = null;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.fecha desc, model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}

	@Override
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, int limite, Long idEmpresa) throws Exception {
		return this.findLikeProperty(propertyName, value, 0L, limite, idEmpresa);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findLikeProperty(String propertyName, Object value, long idObra, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from OrdenCompra model where sistema = 0 and model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					queryString += "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (estatus != null)
				queryString += "and estatus = " + estatus + " ";
			estatus = null;
		
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += "order by CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.fecha desc, model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findNoCompletas(String propertyName, Object value, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from OrdenCompra model where sistema = 0 and model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					queryString += "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (estatus != null)
				queryString += "and estatus = " + estatus + " ";
			estatus = null;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.fecha desc, model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findInProperty(String columnName, List<Object> values, Long idEmpresa) throws Exception {
		String queryString = "select model from OrdenCompra model where sistema = 0 and model.idEmpresa = :idEmpresa ";
    	String inFilter = "";
    	
    	try {
    		if (idEmpresa == null || idEmpresa <= 0)
    			idEmpresa = 1L;
    		if (values != null && ! values.isEmpty()){
    			queryString += "and cast(model." + columnName + " as string) IN (";
    			
    			for (int i = 0; i < values.size(); i++) {
    				if (! "".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
    			queryString += inFilter + ")";
        	}
			
			if (estatus != null)
				queryString += "and estatus = " + estatus + " ";
			estatus = null;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += "order by CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.fecha desc, model.id desc";
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++)
        			query.setParameter(columnName + i, values.get(i).toString());
        	}
        	
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			orderBy = null;
			estatus = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findByProperties(HashMap<String, Object> params, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from OrdenCompra model where sistema = 0 and model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			for (Entry<String, Object> e : params.entrySet()) {
				if (e.getValue().getClass() == java.util.Date.class) 
					queryString += "and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					queryString += "and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			if (estatus != null)
				queryString += "and estatus = " + estatus + " ";
			estatus = null;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.fecha desc, model.id desc";

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findLikeProperties(HashMap<String, String> params, int limite, Long idEmpresa) throws Exception{
		String queryString = "select model from OrdenCompra model where sistema = 0 and model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			for (Entry<String, String> e : params.entrySet())
				queryString += "and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			
			if (estatus != null)
				queryString += "and estatus = " + estatus + " ";
			estatus = null;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.fecha desc, model.id desc";

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor, Long idEmpresa) throws Exception {
		String queryString = "select (COUNT(model.idProveedor) + 1) from OrdenCompra model where model.idEmpresa = :idEmpresa and model.idProveedor = :propertyValue ";
		Long consecutivo;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
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
	public List<OrdenCompra> findBy(Object value, long idObra, boolean incluyeSistema, int limite, Long idEmpresa) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompra> findLike(String value, long idObra, boolean incluyeSistema, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from OrdenCompra model where sistema = 0 and model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (incluyeSistema)
				queryString = "select model from OrdenCompra model where sistema in (0,1) ";
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue "
						+ "or cast(model.idCotizacion as string) like :propertyValue "
						+ "or cast(model.idObra as string) like :propertyValue "
						+ "or cast(model.idProveedor as string) like :propertyValue "
						+ "or cast(model.idSolicita as string) like :propertyValue "
						+ "or cast(model.idContacto as string) like :propertyValue "
						+ "or cast(model.idMoneda as string) like :propertyValue "
						+ "or cast(model.idUsuarioAutorizo as string) like :propertyValue "
						+ "or lower(trim(model.folio)) like lower(:propertyValue) "
						+ "or lower(trim(model.lugarEntrega)) like lower(:propertyValue) "
						+ "or lower(trim(model.nombreObra)) like lower(:propertyValue) "
						+ "or lower(trim(model.nombreProveedor)) like lower(:propertyValue) "
						+ "or lower(trim(model.nombreSolicita)) like lower(:propertyValue) "
						+ "or lower(trim(model.nombreContacto)) like lower(:propertyValue) "
						+ "or lower(trim(model.tipoPersonaProveedor)) like lower(:propertyValue) "
						+ "or lower(trim(model.referencia)) like lower(:propertyValue)) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}
			
			if (estatus != null)
				queryString += "and estatus = " + estatus + " ";
			estatus = null;
		
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.fecha desc, model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
			estatus = null;
		}
	}
}
