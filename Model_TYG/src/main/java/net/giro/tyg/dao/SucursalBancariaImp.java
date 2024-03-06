package net.giro.tyg.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.comun.ExcepConstraint;
import net.giro.tyg.admon.SucursalBancaria;

@Stateless
public class SucursalBancariaImp extends DAOImpl<SucursalBancaria> implements SucursalBancariaDAO {
	@PersistenceContext 
	private EntityManager entityManager;
	
	@Override
    public long save(SucursalBancaria entity, Long codigoEmpresa) throws ExcepConstraint {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
    public List<SucursalBancaria> saveOrUpdateList(List<SucursalBancaria> entities, Long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SucursalBancaria> findByProperty(String propertyName, Object value, Long idEmpresa, int limite) {
		String queryString = "select model from SucursalBancaria model where model." + propertyName + " = :propertyValue ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SucursalBancaria> findLikeProperty(String propertyName, Object value, Long idEmpresa, int limite) {
		String queryString = "select model from SucursalBancaria model where lower(cast(model." + propertyName + " as string)) like :propertyValue ";
		
		try {
			if (value == null)
				value = "";
			value = "%" + value + "%";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
