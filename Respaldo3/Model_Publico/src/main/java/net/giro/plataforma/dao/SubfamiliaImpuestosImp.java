package net.giro.plataforma.dao;

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
import net.giro.plataforma.beans.SubfamiliaImpuestos;

@Stateless
public class SubfamiliaImpuestosImp extends DAOImpl<SubfamiliaImpuestos> implements SubfamiliaImpuestosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private String orderBy;
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(SubfamiliaImpuestos entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<SubfamiliaImpuestos> saveOrUpdateList(List<SubfamiliaImpuestos> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SubfamiliaImpuestos> findByProperty(String propertyName, Object value, int limite) throws RuntimeException {
		String queryString = "select model from SubfamiliaImpuestos model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (value != null) {
				whereString = "and model."+ propertyName + " = :propertyValue ";
			}
			
			queryString += whereString;
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;
			
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
	public List<SubfamiliaImpuestos> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from SubfamiliaImpuestos model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			for (Entry<String, Object> e : params.entrySet()) {
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += "and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += "and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					whereString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			queryString += whereString;
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;

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
	public List<SubfamiliaImpuestos> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		String queryString = "select model from SubfamiliaImpuestos model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if (value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					whereString = "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					whereString = "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
				sb.append("%");
				sb.append(value.toString().toLowerCase());
				sb.append("%");
			}
			
			queryString += whereString;			
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && !"".equals(value.toString()))
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
	public List<SubfamiliaImpuestos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		String queryString = "select model from SubfamiliaImpuestos model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			for (Entry<String, String> e : params.entrySet()){
				if (whereString.length() > 0)
					whereString += "or ";
				whereString += "cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			}
			
			if (! whereString.isEmpty())
				queryString += "and (" + whereString + ") ";
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;

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
	public List<SubfamiliaImpuestos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		String queryString = "select model from SubfamiliaImpuestos model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		String inFilter = "";
		
		try {
			if (values != null && ! values.isEmpty()){
				whereString = "and cast(model." + columnName + " as string) IN (";
				
				for (int i = 0; i < values.size(); i++) {
					if (!"".equals(inFilter)) 
						inFilter += ",";
					inFilter += ":" + columnName + i;
				}
				
				whereString += inFilter + ") ";
			}
			
			queryString += whereString;
			if (this.orderBy != null && !"".equals(this.orderBy))
				queryString += " order by " + this.orderBy;
			this.orderBy = null;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (values != null && ! values.isEmpty()) {
				for(int i = 0; i < values.size(); i++) {
					query.setParameter(columnName + i, values.get(i).toString());
				}
			}
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
}
