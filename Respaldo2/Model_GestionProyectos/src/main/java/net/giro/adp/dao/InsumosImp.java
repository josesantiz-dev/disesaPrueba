package net.giro.adp.dao;

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
import net.giro.adp.beans.Insumos;

@Stateless
public class InsumosImp extends DAOImpl<Insumos> implements InsumosDAO {
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
	public long save(Insumos entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Insumos> saveOrUpdateList(List<Insumos> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findByProperty(String propertyName, Object value, int max) {
		String queryString = "select model from Insumos model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) {
				queryString += "and model."+ propertyName + " = :propertyValue";
			}
			
			queryString += " order by id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
		
	@SuppressWarnings("unchecked")
	public List<Insumos> findByActivos(int max) {
		String queryString = "select model from Insumos model where model.idEmpresa = :idEmpresa and model.estatus = 0  order by id desc";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findLikeProperty(String propertyName, Object value, int max) {
		String queryString = "select model from Insumos model, Obra o where model.idEmpresa = :idEmpresa and model.idObra = o.id ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || "idobra idpresupuesto".contains(propertyName.toLowerCase()))
					queryString += " and cast(" + (propertyName.contains("model.") ? propertyName : ("model." + propertyName)) + " as string) LIKE :propertyValue";
				else
					queryString = queryString + " and lower(" + propertyName + ") LIKE :propertyValue";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			queryString += " order by model.estatus, model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> comprobarInsumos(Long idObra) {
		String queryString = "select model from Insumos model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idObra != null) {
				queryString += "and model.idObra = :propertyValue and model.estatus = 0";
			}
			
			queryString += " order by id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (idObra != null)
				query.setParameter("propertyValue", idObra);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findByProperties(HashMap<String, Object> params) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Entity model where model.idEmpresa = :idEmpresa ";
		
		try {
			for(Entry<String, Object> e : params.entrySet()) {
				if (e.getValue().getClass() == java.util.Date.class) 
					queryString += "and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					queryString += "and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findLikeProperties(HashMap<String, String> params) throws Exception {
		String queryString = "";
		String whereString = "";
		
		try {
			queryString = "select model from Insumos model where model.idEmpresa = :idEmpresa ";
			whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (whereString.length() > 0)
					whereString += "or ";
				whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			}
			
			if (! whereString.isEmpty())
				queryString += " and (" + whereString + ") ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findInProperty(String columnName, List<Object> values) throws Exception {
		String queryString = "";
		String whereString = "";
		String inFilter = "";
    	
    	try {
    		queryString = "select model from Insumos model where model.idEmpresa = :idEmpresa ";
			whereString = "";
    		
    		if(values != null && ! values.isEmpty()){
    			whereString = " and cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
    			whereString = whereString + inFilter + ")";
        	}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
        	if(values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}
}
