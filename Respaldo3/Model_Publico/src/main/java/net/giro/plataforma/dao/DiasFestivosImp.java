package net.giro.plataforma.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.beans.DiasFestivos;

@Stateless
public class DiasFestivosImp extends DAOImpl<DiasFestivos> implements DiasFestivosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(DiasFestivos entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<DiasFestivos> saveOrUpdateList(List<DiasFestivos> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DiasFestivos> findAll(long idEmpresa, int limite) throws Exception {
		String queryString = "select model from DiasFestivos model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			queryString += "order by model.mes, model.dia ";
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
	public List<DiasFestivos> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from DiasFestivos model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			if (value != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			queryString += "order by model.mes, model.dia ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
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
	public List<DiasFestivos> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from DiasFestivos model ";
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += "where cast(model."+ propertyName + " as string) like :propertyValue ";
				} else {
					queryString += "where lower(model."+ propertyName + ") like :propertyValue ";
				}
				
				sb = new StringBuffer();
				sb.append("%");
				sb.append(value.toString().toLowerCase());
				sb.append("%");
			}
			queryString += "order by model.mes, model.dia ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DiasFestivos> comprobarDiaFestivo(int mes, int dia, long idEmpresa) throws Exception {
		String queryString = "select model from DiasFestivos model where model.idEmpresa = :idEmpresa and model.mes = :mes and :dia in (model.dia, model.feriado) order by model.mes, model.dia ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("mes", mes);
			query.setParameter("dia", dia);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
