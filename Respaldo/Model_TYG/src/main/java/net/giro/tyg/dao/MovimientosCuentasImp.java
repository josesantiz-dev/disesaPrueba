package net.giro.tyg.dao;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.giro.DAOImpl;
import net.giro.tyg.admon.MovimientosCuentas;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class MovimientosCuentasImp extends DAOImpl<MovimientosCuentas> 
      implements MovimientosCuentasDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<MovimientosCuentas> query(String queryString, Map<String, Object> parameters)  {
		try {
			Query query = entityManager.createQuery(queryString);
			for(Entry<String, Object> e : parameters.entrySet()){
				query.setParameter(e.getKey(), e.getValue());
			}
			query.setMaxResults(200);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
}
