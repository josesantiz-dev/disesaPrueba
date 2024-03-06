package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.Insumos;

@Stateless
public class InsumosImp extends DAOImpl<Insumos> implements InsumosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(Insumos entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Insumos> saveOrUpdateList(List<Insumos> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Insumos findActual(long idObra) throws Exception {
		String queryString = "select model from Insumos model where model.idObra = :idObra and model.estatus in (0,2) order by model.id desc ";
		List<Insumos> resultados = null;
		Insumos resultado = null;
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			resultados = query.getResultList();
			if (resultados != null && ! resultados.isEmpty())
				resultado = resultados.get(0);
			return resultado;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findAll(long idObra, boolean incluyeCanceladas) throws Exception {
		String queryString = "select model from Insumos model where model.idObra = :idObra ";
		
		try {
			queryString += "and model.estatus in (0 " + (incluyeCanceladas ? ",1" : "") + ",2) order by id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Insumos> findAllActivos(long idObra) throws Exception {
		String queryString = "select model from Insumos model where model.idObra = :idObra and model.estatus = 0 order by id desc";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findLike(String value, boolean incluyeCanceladas, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Insumos model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :value ";
				queryString += "or cast(model.idObra as string) like :value ";
				queryString += "or cast(model.idPresupuesto as string) like :value ";
				queryString += "or lower(trim(model.nombreObra)) like :value ";
				queryString += "or lower(trim(model.observaciones)) like :value) ";
				
				value = value.trim().replace(" ", "%");
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}

			queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1" : "") + ",2) order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("value", sb.toString());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findLikeProperty(String propertyName, Object value, boolean incluyeCanceladas, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Insumos model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null && ! "".equals(value.toString())) {
				queryString += " and lower(cast(model." + propertyName + " as string)) like :propertyValue ";

				value = value.toString().trim().replace(" ", "%");
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}

			queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1" : "") + ",2) order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Insumos> findByProperty(String propertyName, Object value, boolean incluyeCanceladas, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Insumos model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null) 
				queryString += "and model."+ propertyName + " = :propertyValue ";
			queryString += "and model.estatus in (0" + (incluyeCanceladas ? ",1" : "") + ",2) order by model.id desc";
			
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
}
