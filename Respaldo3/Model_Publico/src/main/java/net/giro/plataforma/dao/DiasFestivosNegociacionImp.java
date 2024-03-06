package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.beans.DiasFestivosNegociacion;

@Stateless
public class DiasFestivosNegociacionImp extends DAOImpl<DiasFestivosNegociacion> implements DiasFestivosNegociacionDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(DiasFestivosNegociacion entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<DiasFestivosNegociacion> saveOrUpdateList(List<DiasFestivosNegociacion> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DiasFestivosNegociacion> findAll(long idObra, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from DiasFestivosNegociacion model where model.idDiaFestivo.idEmpresa = :idEmpresa and model.estatus = 0 ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			queryString += "order by id desc ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DiasFestivosNegociacion> findByProperty(String propertyName, Object value, long idObra, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from DiasFestivosNegociacion model where model.idDiaFestivo.idEmpresa = :idEmpresa and model.estatus = 0 ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			if (value != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			queryString += "order by id desc ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DiasFestivosNegociacion> findLikeProperty(String propertyName, Object value, long idObra, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from DiasFestivosNegociacion model where model.idDiaFestivo.idEmpresa = :idEmpresa and model.estatus = 0 ";
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += "and cast(model."+ propertyName + " as string) like :propertyValue ";
				} else {
					queryString += "and lower(model."+ propertyName + ") like :propertyValue ";
				}
				
				sb = new StringBuffer();
				sb.append("%");
				sb.append(value.toString().toLowerCase());
				sb.append("%");
			}
			
			if (idObra > 0L)
				queryString += "and model.idObra = :idObra ";
			queryString += "order by id desc ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DiasFestivosNegociacion> comprobarNegociacion(long idDiaFestivo, long idObra, long idEmpresa) throws Exception {
		String queryString = "select model from DiasFestivosNegociacion model where model.idDiaFestivo.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			queryString += "and model.idDiaFestivo.id = :idDiaFestivo ";
			queryString += "and model.idObra = :idObra ";
			queryString += "order by model.id desc ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idDiaFestivo", idDiaFestivo);
			query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
