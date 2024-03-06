package net.giro.adp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.ObraAlmacenes;

@Stateless
public class ObraAlmacenesImp extends DAOImpl<ObraAlmacenes> implements ObraAlmacenesDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ObraAlmacenes entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ObraAlmacenes> saveOrUpdateList(List<ObraAlmacenes> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraAlmacenes> findAll(long idObra, String orderBy) throws Exception {
		String queryString = "select model from ObraAlmacenes model where model.idObra.id = :idObra ";
		
		try {
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.almacenPrincipal desc, model.tipo, model.idObra.nombre, model.nombreAlmacen");
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
	public List<ObraAlmacenes> findLike(String value, long idAlmacen, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraAlmacenes model where model.idObra.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.almacenPrincipal desc, model.tipo, model.idObra.nombre, model.nombreAlmacen");
			idAlmacen = (idAlmacen > 0L ? idAlmacen : 0L);
			if (idAlmacen > 0)
				queryString += "and model.idAlmacen = :idAlmacen ";
				
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue "
						+ "or cast(model.idObra.id as string) like :propertyValue "
						+ "or lower(trim(model.idObra.nombre)) like lower(:propertyValue) "
						+ "or lower(trim(model.nombreObra)) like lower(:propertyValue) "
						+ "or lower(trim(model.nombreAlmacen)) like lower(:propertyValue)) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idAlmacen > 0)
				query.setParameter("idAlmacen", idAlmacen);
			if (value != null && !"".equals(value.trim()))
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
	public List<ObraAlmacenes> findLikeProperty(String propertyName, Object value, long idAlmacen, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraAlmacenes model where model.idObra.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.almacenPrincipal desc, model.tipo, model.idObra.nombre, model.nombreAlmacen");
			idAlmacen = (idAlmacen > 0L ? idAlmacen : 0L);
			if (idAlmacen > 0)
				queryString += "and model.idAlmacen = :idAlmacen ";
				
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) 
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				else 
					queryString += "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idAlmacen > 0)
				query.setParameter("idAlmacen", idAlmacen);
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
	public List<ObraAlmacenes> findByProperty(String propertyName, Object value, long idAlmacen, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraAlmacenes model where model.idObra.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.almacenPrincipal desc, model.tipo, model.idObra.nombre, model.nombreAlmacen");
			idAlmacen = (idAlmacen > 0L ? idAlmacen : 0L);
			if (idAlmacen > 0)
				queryString += "and model.idAlmacen = :idAlmacen ";
			if (value != null) 
				queryString += "and model."+ propertyName + " = :propertyValue ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idAlmacen > 0)
				query.setParameter("idAlmacen", idAlmacen);
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
	public List<ObraAlmacenes> findByProperties(HashMap<String, String> params, long idEmpresa, String orderBy, int limite) throws Exception{
		String queryString = "select model from ObraAlmacenes model where model.idObra.idEmpresa = :idEmpresa ";

		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.almacenPrincipal desc, model.tipo, model.idObra.nombre, model.nombreAlmacen");
			for (Entry<String, String> e : params.entrySet()) 
				queryString += " and lower(trim(cast(model." + e.getKey() + " as string))) = lower(trim('" + e.getValue() + "')) ";
			queryString += "order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	/*@Override
	@SuppressWarnings("unchecked")
	public List<ObraAlmacenes> findInProperty(String columnName, List<Object> values, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraAlmacenes model where model.idObra.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.almacenPrincipal desc, model.tipo, model.idObra.nombre, model.nombreAlmacen");
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = "and cast(model." + columnName + " as string) IN (";
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
        		sqlWhere = sqlWhere + inFilter + ") ";
        	}
        	
        	queryString += sqlWhere;
			queryString += "order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) 
        			query.setParameter(columnName + i, values.get(i).toString());
        	}
			if (limite > 0)
				query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraAlmacenes> findLikeProperties(HashMap<String, String> params, long idEmpresa, String orderBy, int limite) throws Exception{
		String queryString = "select model from ObraAlmacenes model where model.idObra.idEmpresa = :idEmpresa ";

		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.almacenPrincipal desc, model.tipo, model.idObra.nombre, model.nombreAlmacen");
			for (Entry<String, String> e : params.entrySet()) 
				queryString += " and lower(trim(cast(model." + e.getKey() + " as string))) like lower(trim('%" + e.getValue() + "%')) ";
			queryString += "order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}*/

	@Override
	@SuppressWarnings("unchecked")
	public ObraAlmacenes findAlmacenPrincipal(long idObra, long idSucursal, long idEmpresa) {
		String queryString = "select model from ObraAlmacenes model where model.idObra.id = :idObra and model.idObra.idEmpresa = :idEmpresa and model.tipo = case model.idObra.tipoObra when 4 then 3 else 1 end and model.almacenPrincipal = 1 ";
		List<ObraAlmacenes> result = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			if (idSucursal > 0L)
				queryString += "and model.idObra.idSucursal = :idSucursal ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idObra", idObra);
			if (idSucursal > 0L)
				query.setParameter("idSucursal", idSucursal);
			result = query.getResultList();
			return (! result.isEmpty()) ? result.get(0) : null;
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public ObraAlmacenes findBodega(long idObra, long idEmpresa) {
		String queryString = "select model from ObraAlmacenes model where model.idObra.id = :idObra and model.idObra.idEmpresa = :idEmpresa and model.almacenPrincipal = 0 and model.tipo = 2 ";//case model.idObra.tipoObra when 4 then 4 else 2 end ";
		List<ObraAlmacenes> result = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idObra", idObra);
			result = query.getResultList();
			return (! result.isEmpty()) ? result.get(0) : null;
		} catch (Exception re) {
			throw re;
		} 
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|19/05/2016		|Javier Tirado	|Creando la clase ObraAlmacenesImp