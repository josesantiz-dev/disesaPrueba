package net.giro.tyg.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.ValoresTasas;

@Stateless
public class ValoresTasasImp  extends DAOImpl<ValoresTasas> implements ValoresTasasDAO{
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<ValoresTasas> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint{
		String where = "";
		String queryString = "select VT from ValoresTasas VT ";
		try {
			
			if(valor == null || "".equals(valor)){
				where = "";
			} else {
				if("anio".equals(propiedad)){
				   where = "where cast(VT.anio as string) like '%" + valor.toLowerCase() + "%'";
			   } else if("id".equals(propiedad)){
				   where = "where cast(VT.id as string) like '%" +valor+ "%'";
			   } else if("tasas".equals(propiedad)){
				   where = "where lower(VT.tasas.descripcion) like '%" +valor+ "%'";
			   } else {
				   where = "where lower(VT." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
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
