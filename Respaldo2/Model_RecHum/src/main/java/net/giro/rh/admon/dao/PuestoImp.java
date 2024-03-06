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
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Puesto entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Puesto> saveOrUpdateList(List<Puesto> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Puesto> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from Puesto model where model.idEmpresa = :idEmpresa and lower(af) like '%"+ value.toString().toLowerCase() +"%' ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Puesto> findAll() {
		try {
			final String queryString = "select model from Puesto model where model.idEmpresa = :idEmpresa";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Puesto> findAllActivos() {
		try {
			final String queryString = "select model from Puesto model where model.estatus = 0 and model.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Puesto> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from Puesto model" + 
			("".equals(value) || value == null ? "" : " where model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%' and model.idEmpresa = :idEmpresa ");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Puesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value) {
		return null;
	}
}
