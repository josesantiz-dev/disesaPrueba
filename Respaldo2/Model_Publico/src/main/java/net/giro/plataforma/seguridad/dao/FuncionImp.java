package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.beans.Funcion;

@Stateless
public class FuncionImp extends DAOImpl<Funcion> implements FuncionDAO  {
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Funcion> findLikeNombreAplicacion(String nombFuncion,Aplicacion app, int max) throws Exception {
		try {
			String queryString = "select model from Funcion model " +
				" where lower(model.nombre) like '%" + (nombFuncion != null ? nombFuncion.toLowerCase() : "") + "%' " + (app != null ? " and model.aplicacion = :aplic order by model.aplicacion, model.nombre" : " and model.aplicacion is null order by model.nombre");
			
			Query query = entityManager.createQuery(queryString);
			
			if(app != null)
				query.setParameter("aplic", app);
			
			if(max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
