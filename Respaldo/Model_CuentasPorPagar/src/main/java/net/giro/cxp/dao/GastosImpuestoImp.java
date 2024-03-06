package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxp.beans.GastosImpuesto;

@Stateless
public class GastosImpuestoImp extends DAOImpl<GastosImpuesto> implements GastosImpuestoDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	/*public long save(GastosImpuesto entity) {
		try {
			entityManager.persist(entity);
			return entity.getId();
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void update(GastosImpuesto entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(GastosImpuesto entity) {
		try {
			entity = entityManager.getReference(GastosImpuesto.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}*/

	public GastosImpuesto findById(Long id) {
		try {
			GastosImpuesto instance = entityManager.find(GastosImpuesto.class,id);
			return instance;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<GastosImpuesto> findByProperty(String propertyName,	final Object value) {
		try {
			final String queryString = "select model from GastosImpuesto model where model."+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<GastosImpuesto> findAll() {
		try {
			final String queryString = "select model from GastosImpuesto model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<GastosImpuesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value){
		String where="";
		try{
			String queryString = "select gtosImpto from GastosImpuesto gtosImpto "+
								 //"inner join fetch gtosImpto.impuestoId impto "+
								 "where gtosImpto."	+ propertyName + " = :propertyValue ";
			/*if("AC".equals(tipo))
				where = "and impto.tipoCuenta= 'AC' ";
			else if(tipo != null && !"".equals(tipo))
				where = " and (impto.tipoCuenta != 'AC' or impto.tipoCuenta is null)";*/
			
			queryString = queryString + where;
					
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();		
		}catch(RuntimeException re){
			throw re;
		}
	}
}
