package net.giro.plataforma.seguridad.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.plataforma.seguridad.beans.Perfil;

@Stateless
public class PerfilImp extends DAOImpl<Perfil> implements PerfilDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<Perfil> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Perfil model ";
		StringBuffer sb = null;
		
		try {
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "where date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else {
					queryString += "where lower(cast(model." + propertyName + " as string)) like :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			queryString += "order by " + propertyName;
						
			Query query = entityManager.createQuery(queryString);
			if (value != null)
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
	public List<Perfil> findByProperty(String propertyName, Object value, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Perfil model ";
		
		try {
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "where date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else {
					queryString += "where lower(model." + propertyName + ") = :propertyValue ";
				}
			}

			queryString += "order by " + propertyName;
			
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
}
