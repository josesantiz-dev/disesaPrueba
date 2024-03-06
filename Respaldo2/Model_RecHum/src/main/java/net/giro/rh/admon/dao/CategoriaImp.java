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
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Categoria entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Categoria> saveOrUpdateList(List<Categoria> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Categoria> findByProperty(String propertyName, final Object value) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from Categoria model where model.idEmpresa = :idEmpresa lower(af) like '%"+ value.toString().toLowerCase() +"%'";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Categoria> findAll() {
		try {
			final String queryString = "select model from Categoria model where model.idEmpresa = :idEmpresa";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Categoria> findAllActivos() {
		try {
			final String queryString = "select model from Categoria model where model.idEmpresa = :idEmpresa and model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Categoria> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from Categoria model where model.idEmpresa = :idEmpresa " + 
			(value == null || "".equals(value) ? "" : "and model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Categoria> findByPropertyPojoCompleto(String propertyName, String tipo, long value) {
		return null;
	}
}
