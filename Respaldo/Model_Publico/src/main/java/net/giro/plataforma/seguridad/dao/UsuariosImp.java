package net.giro.plataforma.seguridad.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.seguridad.beans.Usuario;

@Stateless
public class UsuariosImp extends DAOImpl<Usuario> implements UsuariosDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Usuario> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint{
		String where = "";
		String queryString = "select Usu from Usuario Usu ";
		try {
			
			if(valor == null || "".equals(valor)){
				where = "";
			} else {
				if("id".equals(propiedad)){
				   where = "where cast(Usu.id as string) like '%" +valor+ "%'";
			   } else {
				   where = "where lower(Usu." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
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
