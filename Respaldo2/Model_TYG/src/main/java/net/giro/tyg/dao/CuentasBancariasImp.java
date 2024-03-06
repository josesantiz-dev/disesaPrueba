package net.giro.tyg.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.CuentaBancaria;

@Stateless
public class CuentasBancariasImp extends DAOImpl<CuentaBancaria> implements CuentasBancariasDAO {
	@PersistenceContext 
	private EntityManager entityManager;
	private Long idEmpresa;
	
	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(CuentaBancaria entity) throws Exception {
		if (! puedeGuardar(entity.getId()))
			throw new ExcepConstraint("Ya existe una cuenta con el id solicitado", "ctaBancoId");
		return super.save(entity, getIdEmpresa());
	}

	@Override
	public List<CuentaBancaria> saveOrUpdateList(List<CuentaBancaria> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void delete(CuentaBancaria entity) throws ExcepConstraint {
		try {
			super.delete(entity.getId());
		} catch (RuntimeException re) {			
			throw re;
		}
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public CuentaBancaria findAllById(long id) {
		List <CuentaBancaria> listResult = new ArrayList<CuentaBancaria> ();
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

	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findAll(String sucursales) {
		
		try {
			final String queryString = "select ctas from CtasBanco ctas " +
			"where ctas.empresa in (" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findTodas() {
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
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findAllByProperty(String propertyName, String value, int maximo, String sucursales) {
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

	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findLikeClaveNombreCuenta(String value, int max, String sucursales, Long empresaId){
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
			
			Query query = entityManager.createQuery(queryString);
			
			if(max>0)
				query.setMaxResults(max);
			return query.getResultList();
		}catch(RuntimeException re){
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findByProperty(String propertyName, final Object value, String sucursales) {
		try {
			final String queryString = "select model from CtasBanco model where model." + propertyName + "= :propertyValue ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
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

	private boolean puedeGuardar(Long id) {
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

	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}
}
