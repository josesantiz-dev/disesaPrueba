package net.giro.plataforma.ubicacion.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.ubicacion.beans.Municipio;

@Stateless
public class MunicipioImp extends DAOImpl<Municipio> implements MunicipioDAO  {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Municipio> findByPropertyLikeValor(String nombrepojo,final Object pojo,String propiedad,final Object valorpropiedad) throws ExcepConstraint {
		try {
			String quer = "";
			
			if(valorpropiedad == null)
				quer = "";
			else
				quer = " and lower(model."+ propiedad + ")" + "like '%"+valorpropiedad.toString().toLowerCase()+"%' ";
			
			final String queryString = "select model from Municipio model " +
					"inner join fetch model.estado " +
					"where model." + nombrepojo + "= :propertyValue " + quer + "order by model.nombre asc";	
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", pojo);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
