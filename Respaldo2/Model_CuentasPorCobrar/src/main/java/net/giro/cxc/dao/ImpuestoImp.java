package net.giro.cxc.dao;
import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.cxc.beans.Impuesto;

@Stateless
public class ImpuestoImp extends DAOImpl<Impuesto> implements ImpuestoDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	public long save(Impuesto entity) {
		try {
			entityManager.persist(entity);
			return entity.getId();
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void delete(Impuesto entity) {
		try {
			entity = entityManager.getReference(Impuesto.class, entity.getId());
			entityManager.remove(entity);
			
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public void update(Impuesto entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	public Impuesto findById(Long id) {
		try {
			Impuesto instance = entityManager.find(Impuesto.class,id);
			return instance;
		} catch (RuntimeException re) {	
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Impuesto> findByProperty(String propertyName,	final Object value) {
		try {
			final String queryString = "select model from Impuesto model where model."+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Impuesto> findAll() {
		try {
			final String queryString = "select model from Impuesto model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	@Override
	public List<Impuesto> findByPropertyPojoCompleto(String propertyName, String tipo, Object value) {
		return null;
	}

//	@SuppressWarnings("unchecked")
//	public List<Impuesto> findByPropertyPojoCompleto(String propertyName, String tipo, Object value){
//		String where="";
//		try{
//			String queryString = "select gtosImpto "+
//								 "from GastosImpuesto gtosImpto "+
//								 "inner join fetch gtosImpto.impuestoId impto "+
//								 "where gtosImpto."	+ propertyName + "= :propertyValue ";
//			if("AC".equals(tipo))
//				where= "and impto.tipoCuenta= 'AC' ";
//			else if(tipo != null && !"".equals(tipo))
//				where= "and (impto.tipoCuenta != 'AC' or impto.tipoCuenta is null)";
//			
//			queryString = queryString + where;
//					
//			Query query = entityManager.createQuery(queryString);
//			query.setParameter("propertyValue", value);
//			return query.getResultList();		
//		}catch(RuntimeException re){
//			throw re;
//		}
//	}
}
