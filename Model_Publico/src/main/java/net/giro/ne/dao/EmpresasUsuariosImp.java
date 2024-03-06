package net.giro.ne.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.ne.beans.EmpresasUsuarios;

@Stateless
public class EmpresasUsuariosImp extends DAOImpl<EmpresasUsuarios> implements EmpresasUsuariosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private String orderBy;
	
	@Override
	public void orderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EmpresasUsuarios> findByProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from EmpresasUsuarios model ";

		try {
			if (value != null)
				queryString += "where model."+ propertyName + " = :propertyValue ";
			if (this.orderBy != null && !"".equals(this.orderBy.trim()))
				queryString += "order by " + this.orderBy;
			
			Query query = entityManager.createQuery(queryString);
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
	public List<EmpresasUsuarios> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select model from EmpresasUsuarios model ";
		StringBuffer sb = null;

		try {
			if (value != null && !"".equals(value.toString())) {
				queryString += "where lower(cast(model."+ propertyName + " as string)) LIKE :propertyValue";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().trim().toLowerCase());
	    		sb.append("%");
			}
	
			if (this.orderBy != null && !"".equals(this.orderBy.trim()))
				queryString += "order by " + this.orderBy.trim();
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}
}
