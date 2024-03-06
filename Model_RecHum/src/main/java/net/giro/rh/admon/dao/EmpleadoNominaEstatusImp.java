package net.giro.rh.admon.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.rh.admon.beans.EmpleadoNominaEstatus;

@Stateless
public class EmpleadoNominaEstatusImp extends DAOImpl<EmpleadoNominaEstatus> implements EmpleadoNominaEstatusDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(EmpleadoNominaEstatus entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(Date fechaDesde, Date fechaHasta, boolean nominaPreliminar, long idUsuario, long idEmpresa, long codigoEmpresa) throws Exception {
		List<EmpleadoNominaEstatus> peticiones = null;
		EmpleadoNominaEstatus entity = null;
		
		try {
			// Comprobamos alguna peticion previa inconclusa ... 
			peticiones = this.comprobarPeticion(fechaDesde, fechaHasta, nominaPreliminar, idEmpresa);
			if (peticiones != null && ! peticiones.isEmpty()) {
				entity = peticiones.get(0);
				entity.setEstatus(0);
				entity.setMensaje("Procesando ...");
				entity.setModificadoPor(idUsuario);
				entity.setFechaModificacion(Calendar.getInstance().getTime());
				this.update(entity);
				return entity.getId();
			}

			// Generamos nueva peticion
			entity = new EmpleadoNominaEstatus();
			entity.setId(0L);
			entity.setFechaDesde(fechaDesde);
			entity.setFechaHasta(fechaHasta);
			entity.setPreliminar((nominaPreliminar ? 1 : 0));
			entity.setIdEmpresa(idEmpresa);
			entity.setEstatus(0);
			entity.setMensaje("Procesando ...");
			entity.setCreadoPor(idUsuario);
			entity.setFechaCreacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(idUsuario);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			return this.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<EmpleadoNominaEstatus> saveOrUpdateList(List<EmpleadoNominaEstatus> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<EmpleadoNominaEstatus> deleteAll(List<EmpleadoNominaEstatus> entities) throws Exception {
		List<EmpleadoNominaEstatus> deleted = null;
		
		try {
			deleted = super.deleteAll(entities);
			if (deleted != null && ! deleted.isEmpty()) {
				for (EmpleadoNominaEstatus delete : deleted)
					entities.remove(delete);
			}
			return entities;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoNominaEstatus> findByProperty(String propertyName, Object value, long idEmpresa, int max) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "";
		String whereString = "";
		boolean hasValue = false;
		
		try {
			queryString = "select model from EmpleadoNominaEstatus model where model.idEmpresa = :idEmpresa ";
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					whereString = " and date(model."+ propertyName + ") = date('" + formateador.format((Date) value) + "')";
					hasValue = true;
				} else {
					whereString = " and model."+ propertyName + " = :propertyValue";
				}
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! hasValue)
				query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoNominaEstatus> findLikeProperty(String propertyName, Object value, long idEmpresa, int max) throws Exception {
		StringBuffer sb = null;
		String queryString = "";
		String whereString = "";
		
		try {
			queryString = "select model from EmpleadoNominaEstatus model where model.idEmpresa = :idEmpresa ";
			if (value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					whereString = " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					whereString = " and lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<EmpleadoNominaEstatus> comprobarPeticion(Date fechaDesde, Date fechaHasta, boolean nominaPreliminar, long idEmpresa) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from EmpleadoNominaEstatus model where model.idEmpresa = :idEmpresa ";
		
		try {
			queryString += "and date(model.fechaDesde) = date(:fechaDesde) ";
			queryString += "and date(model.fechaHasta) = date(:fechaHasta) ";
			queryString += "and model.preliminar = :preliminar ";
			queryString += "and model.estatus <> 2 ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("fechaDesde", formateador.format(fechaDesde));
			query.setParameter("fechaHasta", formateador.format(fechaHasta));
			query.setParameter("preliminar", (nominaPreliminar ? 1 : 0));
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
//	  2.1	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaEstatusImp