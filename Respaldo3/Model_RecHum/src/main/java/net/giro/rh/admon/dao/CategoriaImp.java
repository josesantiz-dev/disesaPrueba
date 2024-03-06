package net.giro.rh.admon.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.Categoria;

@Stateless
public class CategoriaImp extends DAOImpl<Categoria> implements CategoriaDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Categoria entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Categoria> saveOrUpdateList(List<Categoria> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Categoria> findAll(long idEmpresa) {
		try {
			final String queryString = "select model from Categoria model where model.idEmpresa = :idEmpresa";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Categoria> findAllActivos(long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from Categoria model where model.idEmpresa = :idEmpresa and model.estatus = 0 ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Categoria> findByProperty(String propertyName, final Object value, long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from Categoria model where model.idEmpresa = :idEmpresa and model." + propertyName + " = :propertyValue ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Categoria> findLikeClaveNombre(String value, long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from Categoria model where model.idEmpresa = :idEmpresa " + 
			(value == null || "".equals(value) ? "" : "and model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Categoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value, long idEmpresa) {
		return null;
	}
}
