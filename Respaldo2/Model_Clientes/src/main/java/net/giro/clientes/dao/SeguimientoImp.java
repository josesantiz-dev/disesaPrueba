package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.clientes.beans.Seguimiento;
import net.giro.comun.ExcepConstraint;

@Stateless
public class SeguimientoImp extends DAOImpl<Seguimiento> implements SeguimientoDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Seguimiento> findLikePropiedadYEstatus(String propiedad, String valor, Long valor2) throws ExcepConstraint{
		String where = "";
		String and = " and ";
		String queryString = "select Segs from Seguimiento Segs " +
								"left join fetch Segs.prospectoId pros " + 
								"left join fetch pros.persona per ";
		try {
			
			if(valor == null || "".equals(valor)){
				where = "";
				  and = " where ";
			} else {
				if("especialista".equals(propiedad)){
				   where = "where";
				   and = "";
			   } else {
				   where = "where lower(per." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
				   }
			}
			
			if(valor2 == null || "".equals(valor2)) {
				where = "";
				and = "";
			}else
				and += " Segs.estatusId = " + valor2;

			queryString += where + and;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
