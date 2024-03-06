package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.Area;

@Stateless
public class AreaImp extends DAOImpl<Area> implements AreaDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Area entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Area> saveOrUpdateList(List<Area> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Area> findAll(long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			final String queryString = "select model from Area model where model.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Area> findAllActivos(long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			final String queryString = "select model from Area model where model.idEmpresa = :idEmpresa and model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Area> findByProperty(String propertyName, final Object value, long idEmpresa) {
		String queryString = "select model from Area model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			queryString += "order by descripcion, id desc";
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
	public List<Area> findLikeProperty(String propertyName, final Object value, long idEmpresa) {
		String queryString = "select model from Area model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null)
				queryString += "and model." + propertyName + " like '%" + value.toString().trim().toLowerCase() + "%' ";
			queryString += "order by descripcion, id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Area> findLikeClaveNombre(String value, long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			final String queryString = "select model from Area model where model.idEmpresa = :idEmpresa " + 
			("".equals(value) || value == null ? "" : "and model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Area> findByPropertyPojoCompleto(String propertyName, String tipo, Object value, long idEmpresa) {
		return null;
	}
}
