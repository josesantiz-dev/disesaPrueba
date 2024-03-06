package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.seguridad.beans.Responsabilidad;

@Stateless
public class ResponsabilidadImp extends DAOImpl<Responsabilidad> implements ResponsabilidadDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Responsabilidad> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint{
		String where = "";
		String queryString = "select Res from Responsabilidad Res ";
		try {
			
			if(valor == null || "".equals(valor)){
				where = "";
			} else {
				if("id".equals(propiedad)){
				   where = "where cast(Res.id as string) like '%" +valor+ "%'";
			   } else {
				   where = "where lower(Res." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
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
	}
}
