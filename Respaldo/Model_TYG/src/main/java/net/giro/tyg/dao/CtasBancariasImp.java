package net.giro.tyg.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.CtasBanco;


@Stateless
public class CtasBancariasImp extends DAOImpl<CtasBanco> implements CtasBancariasDAO{
	@PersistenceContext private EntityManager entityManager;
    
	public long save(CtasBanco entity) throws ExcepConstraint, RuntimeException {
		if(! puedeGuardar((short) entity.getId()))
			throw new ExcepConstraint("Ya existe una cuenta con el id solicitado", "ctaBancoId");
		//entityManager.persist(entity);
		//super.save(entity);
		return super.save(entity);
	}
	
	private boolean puedeGuardar(Short id) {
		int num = 0;
		
		try {
			String queryString = "select count(model) from CtasBanco model where model.id =:id";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("id", (long) id);
			num = Integer.valueOf(query.getSingleResult().toString());
		} catch (Exception re) {
			num = 1;
		}
		
		return num == 0;
	}
	
	/*public void update(CtasBanco entity) {
		try {
			entityManager.merge(entity);			
		} catch (RuntimeException re) {			
			throw re;
		}
	}*/
	
	public void delete(CtasBanco entity) {
		try {
			entity = entityManager.getReference(CtasBanco.class, entity.getId());
			entityManager.remove(entity);			
		} catch (RuntimeException re) {			
			throw re;
		}
	}
	
	/*public CtasBanco findById(Short id) {
		
		try {
			CtasBanco instance = entityManager.find(CtasBanco.class, id);
			return instance;
		} catch (RuntimeException re) {
			
			throw re;
		}
	}*/ 

	@SuppressWarnings({ "unchecked" })
	public CtasBanco findAllById(long id) {
		List <CtasBanco> listResult = new ArrayList<CtasBanco> ();
		try {
			final String queryString = "select model from CtasBanco model left join fetch model.empresa " +
					"inner join fetch model.catBancoId " +
					"inner join fetch model.sucursal " +
					"left join fetch model.moneda " +
					"left join fetch model.bancoCierre " +
					"left join fetch model.sucursalDefault where model.ctaBancoId = :id";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("id", id);
			listResult = query.getResultList();
			if (listResult != null && listResult.size() >= 1)
				return listResult.get(0);
			return null;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CtasBanco> findAllByProperty(String propertyName, String value, int maximo, String sucursales) {
		
		try {
			String queryString = "select model from CtasBanco model " + 
					"inner join fetch model.empresa emp " +
					"inner join fetch model.catBancoId " +
					"inner join fetch model.moneda " +
					"left join fetch model.bancoCierre " +
					"left join fetch model.sucursalDefault "+
					"left join fetch model.sucursal suc " +
					"where lower(model."+ propertyName + ") like '%"+ (value != null ?  value.toLowerCase() : "" )+"%'" +
					" and suc.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ")" ;
			Query query = entityManager.createQuery(queryString);
			if (maximo > 0)
				query.setMaxResults(maximo);
			return query.getResultList();
		} catch (RuntimeException re) {
		
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CtasBanco> findLikeClaveNombreCuenta(String value, int max, String sucursales, Integer empresaId){
		try{
			String queryString = "select ctas from CtasBanco ctas " +	
					   "inner join fetch ctas.institucionBancaria ban " +
					   //"inner join fetch ctas.sucursalBancaria suc " +
				   "where ((lower(ban.nombreLargo) like '%" + value.toLowerCase() + "%' or " +	
					   "lower(ban.nombreCorto) like '%" + value.toLowerCase() + "%' or " +
					   "cast(ctas.id as string) like '%" + value.toLowerCase() + "%')) " +
					   (empresaId != null && empresaId > 0 ? " and ctas.empresa = " + empresaId : "") +
					   (sucursales != null &&  !"".equals(sucursales) ? " and ctas.sucursalBancaria in(" + sucursales + ")" : "") +
				   " order by ban.nombreCorto";
			/*String queryString = "select ctas from CtasBanco ctas "+	
					   "inner join fetch ctas.catBancoId "+
					   "inner join fetch ctas.empresa emp "+
					   "left join fetch ctas.sucursalDefault "+
					   "inner join fetch ctas.sucursal suc " +
					   "left join fetch ctas.moneda "+
					   "left join fetch ctas.bancoCierre " +
				   "where ((lower(ctas.cinsnlargo) like '%"+ value.toLowerCase() +"%' or "+	
					   "lower(ctas.cinsncorto) like '%"+value.toLowerCase()+"%' or "+
					   "lower(ctas.ctaBancoId) like '%"+value.toLowerCase()+"%')) and "+
					   "ctas.ctaCheques is not null " + 
					   (empresaId != null ? " and emp.clave = " + empresaId : "") +
					 //  " and suc.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") " +
					   " order by ctas.cinsncorto";*/
			
			Query query = entityManager.createQuery(queryString);
			
			if(max>0)
				query.setMaxResults(max);
							   
			//return query.getResultList();
			
			List<CtasBanco> result = query.getResultList();
			/*if (result.isEmpty()) {
				try {
					throw new Exception("LISTA VACIA :: " + queryString);
				} catch (Exception e) {
					// TO DO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
			return result;
		}catch(RuntimeException re){
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CtasBanco> findByProperty(String propertyName, final Object value, String sucursales) {
		
		try {
			final String queryString = "select model from CtasBanco model where model." + propertyName + "= :propertyValue ";
					//"inner join fetch model.empresa emp "+
					//"inner join fetch model.sucursal suc " +
					//" and suc.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
		
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean esBancoCierre(final Object value) {
		List<Boolean> res = null;
		try {
			final String queryString = "SELECT CASE WHEN coalesce(cta.bancoCierre,0) > 0 THEN true ELSE false END " +
									   "FROM CtasBanco cta " +
									   "WHERE cta.bancoCierre.ctaBancoId = :value AND cta.bancoCierre.ctaBancoId IN (SELECT cta2.ctaBancoId FROM CtasBanco cta2 )";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("value", value);
			res = query.getResultList();
			return res.isEmpty() ? false : true;
		} catch (RuntimeException re) {		
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CtasBanco> findAll(String sucursales) {
		
		try {
			final String queryString = "select ctas from CtasBanco ctas " +
			//"inner join fetch ctas.catBancoId "+
		    //"inner join fetch ctas.empresa emp "+
		    //"left join fetch ctas.sucursalDefault "+
		    //"inner join fetch ctas.sucursal suc " +
		    //"left join fetch ctas.moneda "+
		    //"left join fetch ctas.bancoCierre " +
			//"where suc.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") ";
			"where ctas.empresa in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CtasBanco> findTodas() {
		
		try {
			final String queryString = "select ctas from CtasBanco ctas " +
			"inner join fetch ctas.catBancoId "+
		    "inner join fetch ctas.empresa emp "+
		    "left join fetch ctas.sucursalDefault "+
		    "inner join fetch ctas.sucursal suc " +
		    "left join fetch ctas.moneda "+
		    "left join fetch ctas.bancoCierre ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			
			throw re;
		}
	}
}
