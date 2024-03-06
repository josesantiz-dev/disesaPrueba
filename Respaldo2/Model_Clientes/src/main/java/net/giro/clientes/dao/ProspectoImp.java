package net.giro.clientes.dao;

import java.util.ArrayList;
import java.util.List;

import net.giro.DAOImpl;
import net.giro.clientes.beans.Prospecto;
import net.giro.comun.ExcepConstraint;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class ProspectoImp extends DAOImpl<Prospecto> implements ProspectoDAO  {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Prospecto> findLikePersonaPropiedad(String propiedad, String valor, Long valor2) throws ExcepConstraint{
		String where = "";
		String and = "";
		List<Prospecto> list = new ArrayList<Prospecto>();
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
			
			if(valor2 == null || "".equals(valor2))
				and = "";
			else
				and = and + " Pros.estatus = " + valor2;

			final String queryString = "select Pros from Prospecto Pros " +
										"left join fetch Pros.persona per " +
										"left join fetch Pros.negocio neg " +
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
	public List<Prospecto> findLikeNegocioPropiedad(String propiedad, String valor, Long valor2) throws ExcepConstraint{
		String where = "";
		String and = "";
		List<Prospecto> list = new ArrayList<Prospecto>();
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
			
			if(valor2 == null || "".equals(valor2))
				and = "";
			else
				and = and + " Pros.estatus = " + valor2;

			final String queryString = "select Pros from Prospecto Pros " +
										"left join fetch Pros.persona per " +
										"left join fetch Pros.negocio neg " +
										where + and;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
