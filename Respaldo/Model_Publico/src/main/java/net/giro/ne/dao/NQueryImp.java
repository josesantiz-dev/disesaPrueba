package net.giro.ne.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.ne.beans.NQuery;


@Stateless
public class NQueryImp extends DAOImpl<NQuery> implements NQueryDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public NQuery findByProperty( String sql ) {
		try {
			Query query = entityManager.createNativeQuery(sql,"SRMNQuery");
			return (NQuery) query.getResultList().get(0);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List findNativeQuery(String SrtQuery){
		try {
			Query query = entityManager.createNativeQuery(SrtQuery);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List findJPAQuery(String SrtQuery, HashMap<String, Object> parametros){
		Iterator iterator = null;
		String param = null;
		Object val	= null;
		try {
			Query query = entityManager.createQuery(SrtQuery);
			
			if(parametros != null){
				iterator = parametros.keySet().iterator();
	
				while(iterator.hasNext()){
					param = iterator.next().toString();
					val = parametros.get(param);
					query.setParameter(param, val);
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	} 
	
	@Override
	public void ejecutaAccion(String strSentencia){
		try {
			Query query = entityManager.createNativeQuery(strSentencia);
			query.executeUpdate();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
