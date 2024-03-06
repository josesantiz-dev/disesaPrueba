package net.giro.ne.dao;

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

/*import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jpa.HibernateEntityManager;*/

import net.giro.DAOImpl;
import net.giro.ne.beans.Empresa;

@Stateless
public class EmpresaImp extends DAOImpl<Empresa> implements EmpresaDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private String queryString;
	private String whereString;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		EmpresaImp.orderBy = orderBy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empresa> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.queryString = "select model from Empresa model ";
			this.whereString = "";
			
			if (value != null) {
				this.whereString = " where model."+ propertyName + " = :propertyValue";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
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
	@SuppressWarnings("unchecked")
	public List<Empresa> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		StringBuffer sb = null;
		
		try {
			this.queryString = "select model from Empresa model ";
			this.whereString = "";
			
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					this.whereString = " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					this.whereString = " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Empresa> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
    	String inFilter = "";
    	
    	try {
    		this.queryString = "select model from Empresa model ";
			this.whereString = "";
    		
    		if(values != null && ! values.isEmpty()){
    			this.whereString = " WHERE cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
    			this.whereString = this.whereString + inFilter + ")";
        	}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	if(values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
			if (limite > 0)
				query.setMaxResults(limite);
        	
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Empresa> findByProperties(HashMap<String, Object> params, int limite) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			this.queryString = "select model from Empresa model ";
			this.whereString = "";
			
			for(Entry<String, Object> e : params.entrySet()) {
				if (this.whereString.length() > 0)
					this.whereString += " and";
				
				if (e.getValue().getClass() == java.util.Date.class) 
					this.whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					this.whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					this.whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empresa> findLikeProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			this.queryString = "select model from Empresa model ";
			this.whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (this.whereString.length() > 0)
					this.whereString += " and";
				this.whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
	
	/*
	@Override
	public Empresa findByNombre(String nombre) throws Exception{
		HibernateEntityManager hem = em.unwrap(HibernateEntityManager.class);
		
		Criteria crit = hem.getSession().createCriteria(persistenceClass, "emp")
				.add(Restrictions.eq("emp.descripcion", nombre))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		@SuppressWarnings("unchecked")
		List<Empresa> lista = crit.list();
		
		if(!lista.isEmpty() && lista.size() > 0)
			throw new Exception("Empresa duplicada");
		
		return lista.isEmpty() ? null : lista.get(0);
	}
	
	@Override
	public Empresa monedaBase(Long empresaId){
		HibernateEntityManager hem = em.unwrap(HibernateEntityManager.class);
		
		Criteria crit = hem.getSession().createCriteria(persistenceClass, "emp")
				.add(Restrictions.eq("emp.id", empresaId))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		@SuppressWarnings("unchecked")
		List<Empresa> lista = crit.list();
		
		return lista.isEmpty() ? null : lista.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empresa> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint{
		String where = "";
		String queryString = "select Emp from Empresa Emp ";
		try {
			
			if(valor == null || "".equals(valor)){
				where = "";
			} else {
				if("id".equals(propiedad)){
				   where = "where cast(Emp.id as string) like '%" +valor+ "%'";
			   } else {
				   where = "where lower(Emp." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
			   }
			}
			
			queryString += where;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}*/
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 01/07/2016 | Javier Tirado	| Modificacion de EmpresaImp