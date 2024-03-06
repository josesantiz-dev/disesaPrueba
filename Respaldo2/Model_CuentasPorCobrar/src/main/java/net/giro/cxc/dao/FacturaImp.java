package net.giro.cxc.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.Factura;

@Stateless
public class FacturaImp extends DAOImpl<Factura> implements FacturaDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	private Long idEmpresa;

	@Override
	public void orderBy(String orderBy) {
		FacturaImp.orderBy = orderBy;
	}
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Factura entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Factura> saveOrUpdateList(List<Factura> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findAll() {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {		
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findByProperty(String propertyName, final Object value) {
		try {
			return this.findByProperty(propertyName, value, 0, 0);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findByProperty(String propertyName, Object value, int tipoObra) {
		try {
			return this.findByProperty(propertyName, value, tipoObra, 0);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findByProperty(String propertyName, Object value, int tipoObra, int max) {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (tipoObra <= 0)
				queryString += "and model.tipoObra <> " + tipoObra;
			else
				queryString += "and model.tipoObra = " + tipoObra;
			
			if (value != null)
				queryString += " and model."+ propertyName + "= :propertyValue";
	
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findByPropertyPojoCompleto(String propertyName, Object value, int tipo) {
		return this.findByProperty(propertyName, value, tipo);
	}

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value) {
		try {
			return this.findLikeProperty(propertyName, value, 0, 0);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<Factura> findLikeProperty(String propertyName, Object value, int max) {
		try {
			return this.findLikeProperty(propertyName, value, 0, max);
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findLikeProperty(String propertyName, Object value, int tipoObra, int max) {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	StringBuffer sb = null;
		
		try {
			sqlWhere = "and tipoObra <> :tipoObra ";
			if (tipoObra > 0)
				sqlWhere = "and tipoObra = :tipoObra ";
			
			if (value != null && ! "".equals(value.toString())){
        		if (propertyName.contains("id") || propertyName.toLowerCase().equals("idobra")) {
            		sqlWhere += "and lower(cast(" + propertyName + " as string)) LIKE :propertyValue ";
            	} else {
            		sqlWhere += "and lower(" + propertyName + ") LIKE :propertyValue ";
            	}

        		sb = new StringBuffer();
        		sb.append("%");
        		sb.append(value.toString().toLowerCase());
        		sb.append("%");
        	}
        	
        	queryString = queryString + sqlWhere;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
        	Query query = entityManager.createQuery(queryString);   
        	query.setParameter("idEmpresa", getIdEmpresa());     	
        	query.setParameter("tipoObra", tipoObra);			
        	if (value != null && ! "".equals(value.toString()))
            	query.setParameter("propertyValue", sb.toString());
			if (max > 0)
				query.setMaxResults(max);
        	return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findLikeProperties(HashMap<String, Object> params) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try { 
			for(Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " or ";
				
				if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " cast(model." + e.getKey() + " as string) LIKE '%" + ((BigDecimal) e.getValue()).toString() + "%'";
				else
					whereString += " cast(model." + e.getKey() + " as string) LIKE '%" + e.getValue().toString() + "%'";
			}

			if (! whereString.isEmpty())
				queryString = queryString + " and (" + whereString + ") ";
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findTimbradas(String orderBy, int limite) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		
		try {
			queryString += "and estatus = 1 and trim(coalesce(uuid, '')) <> '' ";
			if (orderBy != null && !"".equals(orderBy))
				queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {		
			throw re;
		} finally {
			orderBy = null;
		}
	}
}
