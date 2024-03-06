package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.Puesto;

@Stateless
public class PuestoImp extends DAOImpl<Puesto> implements PuestoDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Puesto entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Puesto> saveOrUpdateList(List<Puesto> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Puesto> findAll(boolean incluyeEliminados, long idEmpresa) throws Exception {
		String queryString = "select model from Puesto model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			queryString += "and model.estatus in (0" + (incluyeEliminados ? ",1" : "") + ") ";
			queryString += "order by model.descripcion ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Puesto> findByProperty(String propertyName, final Object value, boolean incluyeEliminados, long idEmpresa) throws Exception {
		String queryString = "select model from Puesto model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null) 
				queryString += "and model." + propertyName + " = :propertyValue ";
			queryString += "and model.estatus in (0" + (incluyeEliminados ? ",1" : "") + ") ";
			queryString += "order by model.descripcion ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null) 
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Puesto> findLikeProperty(String propertyName, String value, boolean incluyeEliminados, long idEmpresa) throws Exception {
		String queryString = "select model from Puesto model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null && ! "".equals(value.trim())) {
				value = "%" + value.trim().toLowerCase().replace(" ",  "%") + "%";
				queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
			}
			
			queryString += "and model.estatus in (0" + (incluyeEliminados ? ",1" : "") + ") ";
			queryString += "order by model.descripcion ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.trim())) 
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
