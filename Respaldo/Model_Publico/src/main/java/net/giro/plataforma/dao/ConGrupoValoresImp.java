package net.giro.plataforma.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.giro.plataforma.beans.ConGrupoValores;

import javax.persistence.Query;


@Stateless
public class ConGrupoValoresImp extends DAOImpl<ConGrupoValores> implements ConGrupoValoresDAO  {
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<ConGrupoValores> findLikeClaveNombre(Object value, int max) {
		String queryString = "";
		String filtro = "";
		
		try {
			queryString = "SELECT model FROM ConGrupoValores model {WHERE} ORDER BY model.descripcion, model.nombre";
			if (value != null && ! "".equals(value))
				filtro = "WHERE cast(model.id, string) LIKE '%" + value.toString().toLowerCase() + "%' OR lower(model.nombre) LIKE '%" + value.toString().toLowerCase() + "%'";
			queryString = queryString.replace("{WHERE}", filtro);
			
			Query query = entityManager.createQuery(queryString);
			if(max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
