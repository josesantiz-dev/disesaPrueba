package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAOImpl;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.clientes.beans.Domicilio;
import net.giro.clientes.beans.Negocio;

@Stateless
public class DomicilioImp extends DAOImpl<Domicilio> implements DomicilioDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Domicilio> findPrincipalByNegocio(Negocio pojoNegocio) throws Exception{
		String where = "";
		String queryString = "select Dom from Domicilio Dom";
		try {
			where = " where Dom.negocio.id = " + pojoNegocio.getId() + " and Dom.principal > 0";

			queryString += where;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
}
