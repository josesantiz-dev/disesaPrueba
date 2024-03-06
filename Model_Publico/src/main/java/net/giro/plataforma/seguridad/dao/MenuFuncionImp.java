package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.plataforma.seguridad.beans.MenuFuncion;

@Stateless
public class MenuFuncionImp extends DAOImpl<MenuFuncion> implements MenuFuncionDAO  {
	@PersistenceContext
	private EntityManager entityManager;
	private String orderBy;

	@Override
	public void orderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MenuFuncion> findByMenuId(long id) throws Exception{
		String where = "";
		String queryString = " select mf from MenuFuncion mf fetch all properties "; 
		try {
			if(id > 0){
				where = " where mf.menu.id = " + id;
				queryString += where;
			}
			
			if (this.orderBy != null && !"".equals(this.orderBy)) {
				queryString += " order by " + this.orderBy;
				this.orderBy = "";
			} else {
				queryString += " order by mf.promptId asc ";
			}
			
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
			query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MenuFuncion> findByProperty(String propertyName, Object value, int limite) throws Exception {
		String queryString = "select mf from MenuFuncion mf fetch all properties ";
		
		try {
			if (value != null) {
				queryString = queryString + " where mf."+ propertyName + " = :propertyValue";
			}
			
			if (this.orderBy != null && !"".equals(this.orderBy)) {
				queryString += " order by " + this.orderBy;
				this.orderBy = "";
			}
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
