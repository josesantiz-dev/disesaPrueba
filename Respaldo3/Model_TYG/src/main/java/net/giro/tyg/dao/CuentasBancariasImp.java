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

	@Override
	public long save(CuentaBancaria entity, Long codigoEmpresa) throws ExcepConstraint {
		if (! puedeGuardar(entity.getId()))
			throw new ExcepConstraint("Ya existe una cuenta con el id solicitado", "ID");
		return super.save(entity, codigoEmpresa);
	}

	@Override
	public List<CuentaBancaria> saveOrUpdateList(List<CuentaBancaria> entities, Long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public void delete(CuentaBancaria entity) throws ExcepConstraint {
		try {
			super.delete(entity.getId());
		} catch (Exception re) {			
			throw re;
		}
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public CuentaBancaria findAllById(long id) {
		List <CuentaBancaria> listResult = new ArrayList<CuentaBancaria> ();
		try {
			final String queryString = "select model from CuentaBancaria model where model.id = :id";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("id", id);
			listResult = query.getResultList();
			if (listResult != null && listResult.size() >= 1)
				return listResult.get(0);
			return null;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findAll(long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from CuentaBancaria model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (orderBy != null && ! "".equals(orderBy.trim()))
				queryString += "order by " + orderBy;
			else
				queryString += "order by model.institucionBancaria.nombreCorto";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findAll(String sucursales) {
		
		try {
			final String queryString = "select model from CuentaBancaria model " +
			"where model.sucursalBancaria.id in (" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findTodas() {
		try {
			final String queryString = "select model from CuentaBancaria model ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findAllByProperty(String propertyName, String value, int maximo, String sucursales) {
		try {
			String queryString = "select model from CuentaBancaria model " + 
					"where lower(model."+ propertyName + ") like '%" + (value != null ?  value.toLowerCase() : "" ) + "%'" +
					" and model.sucursalBancaria.id in (" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ")" ;
			Query query = entityManager.createQuery(queryString);
			if (maximo > 0)
				query.setMaxResults(maximo);
			return query.getResultList();
		} catch (Exception re) {
		
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findLikeClaveNombreCuenta(String value, int max, String sucursales, Long empresaId) {
		String queryString = "select model from CuentaBancaria model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (lower(model.institucionBancaria.nombreLargo) like :propertyValue or lower(model.institucionBancaria.nombreCorto) like :propertyValue) ";
				value = "%" + value + "%";
			}
			
			if (sucursales != null && ! "".equals(sucursales.trim()))
				queryString += " and model.sucursalBancaria in (" + sucursales + ") ";
			queryString += " order by model.institucionBancaria.nombreCorto";
			
			/*queryString += "select model from CuentaBancaria model " +
				   "where ((lower(model.institucionBancaria.nombreLargo) like '%" + value.toLowerCase() + "%' or " +	
					   "lower(model.institucionBancaria.nombreCorto) like '%" + value.toLowerCase() + "%' or " +
					   "cast(model.id as string) like '%" + value.toLowerCase() + "%')) " +
					   (empresaId != null && empresaId > 0 ? " and model.idEmpresa = " + empresaId : "") +
					   (sucursales != null &&  !"".equals(sucursales) ? " and model.sucursalBancaria in (" + sucursales + ")" : "") +
				   " order by model.institucionBancaria.nombreCorto";*/
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", empresaId);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CuentaBancaria> findByProperty(String propertyName, final Object value, String sucursales) {
		try {
			final String queryString = "select model from CuentaBancaria model where model." + propertyName + "= :propertyValue ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean esBancoCierre(final Object value) {
		List<Boolean> res = null;
		
		try {
			final String queryString = "SELECT CASE WHEN coalesce(model.bancoCierre,0) > 0 THEN true ELSE false END " +
									   "FROM CuentaBancaria model " +
									   "WHERE model.bancoCierre.modelBancoId = :value AND model.bancoCierre.modelBancoId IN (SELECT model2.modelBancoId FROM CuentaBancaria model2 )";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("value", value);
			res = query.getResultList();
			return res.isEmpty() ? false : true;
		} catch (Exception re) {		
			throw re;
		}
	}

	private boolean puedeGuardar(Long id) {
		int num = 0;
		
		try {
			String queryString = "select count(model) from CuentaBancaria model where model.id =:id";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("id", (long) id);
			num = Integer.valueOf(query.getSingleResult().toString());
		} catch (Exception re) {
			num = 1;
		}
		
		return num == 0;
	}
}
