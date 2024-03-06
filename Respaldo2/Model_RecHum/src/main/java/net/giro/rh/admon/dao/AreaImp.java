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
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Area entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Area> saveOrUpdateList(List<Area> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Area> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from Area model where model.idEmpresa = :idEmpresa and lower(af) like '%"+ value.toString().toLowerCase() +"%'";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Area> findAll() {
		try {
			final String queryString = "select model from Area model where model.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Area> findAllActivos() {
		try {
			final String queryString = "select model from Area model where model.idEmpresa = :idEmpresa and model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Area> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from Area model where model.idEmpresa = :idEmpresa " + 
			("".equals(value) || value == null ? "" : "and model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Area> findByPropertyPojoCompleto(String propertyName, String tipo, Object value) {
		return null;
	}
}
