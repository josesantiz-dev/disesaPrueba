package net.giro.cxc.dao;

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
import net.giro.cxc.beans.Provisiones;

@Stateless
public class ProvisionesImp extends DAOImpl<Provisiones> implements ProvisionesDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Provisiones entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Provisiones> saveOrUpdateList(List<Provisiones> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<Provisiones> findAll() {
		String queryString = "select model from Provisiones model ";
		
		try {
			queryString += "where model.idEmpresa = :idEmpresa and estatus = 0 order by id desc ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {		
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Provisiones> findAllInactives() {
		String queryString = "select model from Provisiones model ";
		
		try {
			queryString += "where model.idEmpresa = :idEmpresa and estatus != 0 order by estatus desc, id ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {		
			throw re;
		} 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Provisiones> findByProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from Provisiones model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (value != null)
				whereString = "and model."+ propertyName + " = :propertyValue and model.estatus = 0 ";
			
			if (! whereString.isEmpty())
				queryString += whereString;
			else
				queryString += "and model.estatus = 0 ";
			queryString += "order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Provisiones> findByProperties(HashMap<String, Object> params, String orderBy, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Provisiones model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "id";
			
			for (Entry<String, Object> e : params.entrySet()) {
				if ("estatus".equals(e.getKey()) || "model.estatus".equals(e.getKey()))
					continue;
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += " and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else if (e.getValue().getClass() == java.lang.Double.class) 
					whereString += " and cast(model." + e.getKey() + " as string) = '" + ((Double) e.getValue()).toString() + "' ";
				else if (e.getValue().getClass() == java.lang.Integer.class) 
					whereString += " and cast(model." + e.getKey() + " as string) = '" + ((Integer) e.getValue()).toString() + "' ";
				else
					whereString += " and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			if (! whereString.isEmpty()) {
				queryString += "and " + whereString + "";
				if (params.containsKey("estatus") || params.containsKey("model.estatus"))
					queryString += "and model.estatus = " + params.get("estatus").toString() + " ";
				else
					queryString += "and model.estatus = 0 ";
			} else {
				if (params.containsKey("estatus") || params.containsKey("model.estatus"))
					queryString += "and model.estatus = " + params.get("estatus").toString() + " ";
				else
					queryString += "and model.estatus = 0 ";
			}
			queryString += "orderby " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Provisiones> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		String queryString = "select model from Provisiones model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if (value == null)
				value = "";
			
			if (! "".equals(value.trim())) {
				whereString = " and lower(model."+ propertyName + ") LIKE :propertyValue and model.estatus = 0 ";
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2))))
					whereString = " and cast(model."+ propertyName + " as string) LIKE :propertyValue and model.estatus = 0 ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}
			
			if (! whereString.isEmpty())
				queryString += whereString;
			else
				queryString += "and model.estatus = 0 ";
			queryString += "order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Provisiones> findLikeProperties(HashMap<String, String> params, String orderBy, int limite) throws Exception {
		String queryString = "select model from Provisiones model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "id";
			
			for (Entry<String, String> e : params.entrySet()) {
				if ("estatus".equals(e.getKey()) || "model.estatus".equals(e.getKey()))
					continue;
				if (! whereString.isEmpty())
					whereString += " or ";
				whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			}
			
			if (! whereString.isEmpty()) {
				queryString += " and (" + whereString + ") ";
				if (params.containsKey("estatus") || params.containsKey("model.estatus"))
					queryString += "and model.estatus = " + params.get("estatus") + " ";
				else
					queryString += "and model.estatus = 0 ";
			} else {
				if (params.containsKey("estatus") || params.containsKey("model.estatus"))
					queryString += "where model.estatus = " + params.get("estatus") + " ";
				else
					queryString += "where model.estatus = 0 ";
			}
			queryString += "orderby " + orderBy;
			
			if (! whereString.isEmpty())
				queryString = queryString + "where " + whereString;
			queryString = queryString + "orderby " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public Integer findLastGrupo() throws Exception {
		String queryString = "select coalesce(max(model.grupo), 0) from Provisiones model where model.idEmpresa = :idEmpresa ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return (Integer) query.getSingleResult();
		} catch (RuntimeException re) {
			throw re;
		} 
	}

	@Override
	public Integer findNextGrupo() throws Exception {
		Integer grupo = 0;
		
		try {
			grupo = this.findLastGrupo();
			return (grupo == 0) ? 1 : (grupo + 1);
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
