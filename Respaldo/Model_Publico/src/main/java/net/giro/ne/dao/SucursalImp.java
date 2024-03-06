package net.giro.ne.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Sucursal;

@Stateless
public class SucursalImp extends DAOImpl<Sucursal> implements SucursalDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<Sucursal> findLikePropiedad(String propiedad, String valor) throws ExcepConstraint{
		String where = "";
		String queryString = "select Suc from Sucursal Suc ";
		try {
			
			if(valor == null || "".equals(valor)){
				where = "";
			} else {
				if("id".equals(propiedad)){
				   where = "where cast(Suc.id as string) like '%" +valor+ "%'";
			   } else {
				   where = "where lower(Suc." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
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
