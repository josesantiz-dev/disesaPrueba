package net.giro.rh.admon.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.rh.admon.beans.EmpleadoInfonavit;

@Stateless
public class EmpleadoInfonavitImp extends DAOImpl<EmpleadoInfonavit> implements EmpleadoInfonavitDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;

	@Override
	public void orderBy(String orderBy) {
		EmpleadoInfonavitImp.orderBy = orderBy;
	}

	@Override
	public long save(EmpleadoInfonavit entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoInfonavit> saveOrUpdateList(List<EmpleadoInfonavit> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoInfonavit> findAll(long idEmpleado) throws Exception {
		String queryString = "select model from EmpleadoInfonavit model where model.idEmpleado.id = :idEmpleado ";
		
		try {
			if (idEmpleado <= 0L) 
				idEmpleado = 0L;
			queryString += "and model.estatus = 0 order by model.id ";
			// ------------------------------------------------------
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpleado", idEmpleado);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoInfonavit> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from EmpleadoInfonavit model where model.idEmpleado.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) 
				queryString += " and model."+ propertyName + " = :propertyValue ";
			if (orderBy != null && !"".equals(orderBy)) 
				queryString += " order by " + orderBy;
			// ------------------------------------------------------
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoInfonavit> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from EmpleadoInfonavit model where model.idEmpleado.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)) && ! propertyName.contains("."))) 
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				else 
					queryString = queryString + " and lower(model."+ propertyName + ") LIKE :propertyValue ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && !"".equals(orderBy)) 
				queryString += " order by " + orderBy;
			// ------------------------------------------------------
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} finally {
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoInfonavit> comprobarPorFecha(long idEmpleado, Date fecha, long idEmpresa) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from EmpleadoInfonavit model where model.idEmpleado.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (idEmpleado <= 0 || fecha == null)
				return new ArrayList<EmpleadoInfonavit>();
			queryString += "and model.idEmpleado.id = :idEmpleado ";
			queryString += "and date(:fecha) between date(model.fechaDesde) and date(model.fechaHasta) ";
			// ------------------------------------------------------
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idEmpleado", idEmpleado);
			query.setParameter("fecha", formateador.format(fecha));
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoInfonavit> comprobarPorFecha(long idEmpleado, Date fechaDesde, Date fechaHasta, long idEmpresa) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from EmpleadoInfonavit model where model.idEmpleado.idEmpresa = :idEmpresa ";
		
		try {
			queryString += "and model.idEmpleado.id = :idEmpleado ";
			queryString += "and date(:fechaDesde) = date(model.fechaDesde) ";
			queryString += "and date(:fechaHasta) = date(model.fechaHasta) ";
			// ------------------------------------------------------
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idEmpleado", idEmpleado);
			query.setParameter("fechaDesde", formateador.format(fechaDesde));
			query.setParameter("fechaHasta", formateador.format(fechaHasta));
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 07/06/2016 | Javier Tirado	| Creacion de EmpleadoInfonavitImp