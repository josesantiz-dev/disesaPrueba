package net.giro.plataforma.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.beans.ConValoresBlacklist;

@Stateless
public class ConValoresBlacklistImp extends DAOImpl<ConValoresBlacklist> implements ConValoresBlacklistDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(ConValoresBlacklist entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public ConValoresBlacklist save(ConValores conValores, long creadoPor, long idEmpresa, long codigoEmpresa) throws Exception {
		ConValoresBlacklist entity = null;
		
		try {
			entity = new ConValoresBlacklist();
			entity.setIdConValores(conValores);
			entity.setIdGrupo(conValores.getGrupoValorId());
			entity.setIdEmpresa(idEmpresa);
			entity.setCreadoPor(creadoPor);
			entity.setFechaCreacion(Calendar.getInstance().getTime());
			entity.setId(this.save(entity, codigoEmpresa));
			return entity;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ConValoresBlacklist> saveOrUpdateList(List<ConValoresBlacklist> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ConValoresBlacklist> findAll(long idGrupo, String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ConValoresBlacklist model where model.idGrupo.id = :idGrupo ";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.fechaCreacion desc";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idGrupo", idGrupo);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ConValoresBlacklist> findByLikeProperty(String propertyName, Object propertyValue, long idGrupo, String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ConValoresBlacklist model where model.idGrupo.id = :idGrupo ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			
			if (propertyValue != null) {
				if (propertyValue.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) propertyValue) + "') ";
					propertyValue = null;
				} else if (! "".equals(propertyValue.toString().trim())) {
					queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(propertyValue.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}
			
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.fechaCreacion desc";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idGrupo", idGrupo);
			if (propertyValue != null && ! "".equals(propertyValue.toString()))
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
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ConValoresBlacklist> findByByProperty(String propertyName, Object propertyValue, long idGrupo, String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ConValoresBlacklist model where model.idGrupo.id = :idGrupo ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			
			if (propertyValue != null) {
				if (propertyValue.getClass() == java.util.Date.class)
					queryString += "and date(model." + propertyName + ") = date(:propertyValue) ";
				else 
					queryString += "and model." + propertyName + " = :propertyValue ";
			}

			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.fechaCreacion desc";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idGrupo", idGrupo);
			if (propertyValue != null)
				query.setParameter("propertyValue", propertyValue);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.1 | 2019-08-14 | Javier Tirado 	| Creacion de EJB.
 */
