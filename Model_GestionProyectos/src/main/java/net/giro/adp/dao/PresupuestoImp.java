package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.Presupuesto;

@Stateless
public class PresupuestoImp  extends DAOImpl<Presupuesto> implements PresupuestoDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(Presupuesto entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Presupuesto> saveOrUpdateList(List<Presupuesto> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public Presupuesto findActual(long idObra, long idEmpresa) {
		List<Presupuesto> resultados = null;
		Presupuesto resultado = null;
		
		try {
			resultados = this.findAll(idObra, idEmpresa, 0);
			if (resultados != null && ! resultados.isEmpty())
				resultado = resultados.get(0);
			return resultado;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Presupuesto> findAll(long idObra, long idEmpresa, int limite) {
		String queryString = "select model from Presupuesto model where model.idObra.idEmpresa = :idEmpresa and model.idObra.id = :idObra ";
		
		try {
			if (idObra <= 0)
				idObra = 0;
			if (idEmpresa <= 0)
				idEmpresa = 1;
			queryString += " order by id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
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
	public List<Presupuesto> findByProperty(String propertyName, Object value, long idEmpresa, int limite) {
		String queryString = "select model from Presupuesto model where model.idObra.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null && ! "".equals(value.toString())) 
				queryString += "and model."+ propertyName + " = :propertyValue ";
			queryString += " order by id asc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
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
	public List<Presupuesto> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) {
		String queryString = "select model from Presupuesto model where model.idObra.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName))
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyName ";
				else
					queryString += "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
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
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------'
//VERSIÓN	| FECHA 		| AUTOR 		| DESCRIPCIÓN '
//----------------------------------------------------------------------------------------------------------------'
//2.2		|05/05/2016		|Daniel Azamar	|Creando la clase PresupuestoImp