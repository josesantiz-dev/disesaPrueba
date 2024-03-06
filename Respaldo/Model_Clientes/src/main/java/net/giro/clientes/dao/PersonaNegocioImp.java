package net.giro.clientes.dao;

import java.util.ArrayList;
import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.clientes.beans.PersonaNegocio;
import net.giro.comun.ExcepConstraint;

@Stateless
public class PersonaNegocioImp extends DAOImpl<PersonaNegocio> implements PersonaNegocioDAO  {
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<PersonaNegocio> findLikePropiedad(String propiedad, String valor, Long personaId) throws ExcepConstraint{
		String where = "";
		String and = "";
		List<PersonaNegocio> list = new ArrayList<PersonaNegocio>();
		try {
			
			if(valor == null || "".equals(valor)){
				where = "";
				  and = " where ";
			} else if("id".equals(propiedad)){
				   where = "where cast(neg.id as string) like '%" +valor+ "%'";
				     and = " and ";
			   } else if("nombre".equals(propiedad)){
					where = "where lower(neg." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
					  and = " and ";
			     } else if("rfc".equals(propiedad)){
			    	 where = "where lower(neg." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
			    	   and = " and ";
			     	}else
			     		return list;
			
			if(personaId == null || personaId == 0)
				and = "";
			else
				and = and + " per.id = " + personaId ;

			final String queryString = "select perNeg from PersonaNegocio perNeg " +
										"left join fetch perNeg.persona per " +
										"left join fetch perNeg.negocio neg " +
										"left join fetch perNeg.apoderadoId apoderado " +
										
										where + and;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonaNegocio> findLikeNegocioPropiedad(String propiedad, String valor, Long negocioId) throws ExcepConstraint{
		String where = "";
		String and = "";
		List<PersonaNegocio> list = new ArrayList<PersonaNegocio>();
		try {
			
			if(valor == null || "".equals(valor)){
				where = "";
				  and = " where ";
			} else if("id".equals(propiedad)){
				   where = "where cast(per.id as string) like '%" +valor+ "%'";
				     and = " and ";
			   } else if("nombre".equals(propiedad)){
					where = "where lower(per." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
					  and = " and ";
			     } else if("rfc".equals(propiedad)){
			    	 where = "where lower(per." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
			    	   and = " and ";
			     	}else
			     		return list;
			
			if(negocioId == null || negocioId == 0)
				and = "";
			else
				and = and + " neg.id = " + negocioId ;

			final String queryString = "select perNeg from PersonaNegocio perNeg " +
										"left join fetch perNeg.persona per " +
										"left join fetch perNeg.negocio neg " +
										"left join fetch perNeg.apoderadoId apoderado " +
										
										where + and;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonaNegocio> findAccionistasLikeNegocio(Long negocioId) throws ExcepConstraint{
		String where = "";
		try {
			
			if(negocioId == null || "".equals(negocioId)){
				where = "";
			}else {
				where = "where lower(neg.id like '%" + negocioId + "%' and ((perNeg.participacion.signum()) == 1)";
			}
			
			final String queryString = "select perNeg from PersonaNegocio perNeg " +
										"left join fetch perNeg.persona per " +
										"left join fetch perNeg.negocio neg " +
										"left join fetch perNeg.apoderadoId apoderado " +
										where;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonaNegocio> findDirectivosLikeNegocio(Long negocioId) throws ExcepConstraint{
		String where = "";
		try {
			
			if(negocioId == null || "".equals(negocioId)){
				where = "";
			}else {
				where = "where lower(neg.id like '%" + negocioId + "%' and perNeg.participacion == 0";
			}
			
			final String queryString = "select perNeg from PersonaNegocio perNeg " +
										"left join fetch perNeg.persona per " +
										"left join fetch perNeg.negocio neg " +
										"left join fetch perNeg.apoderadoId apoderado " +
										where;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
