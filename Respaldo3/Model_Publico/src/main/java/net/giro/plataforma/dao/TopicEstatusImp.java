package net.giro.plataforma.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.beans.TopicEstatus;

@Stateless
public class TopicEstatusImp extends DAOImpl<TopicEstatus> implements TopicEstatusDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(TopicEstatus entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0 ? codigoEmpresa: 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<TopicEstatus> saveOrUpdateList(List<TopicEstatus> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0 ? codigoEmpresa: 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TopicEstatus> findAll(String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from TopicEstatus model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa: 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaCreado desc, model.id desc");
			queryString += "order by " + orderBy;
			
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
	public List<TopicEstatus> findLikeProperty(String propertyName, Object value, Date fecha, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from TopicEstatus model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa: 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaCreado desc, model.id desc");
			if (value != null) {
				if (value.getClass() != java.util.Date.class && ! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}

			if (fecha != null)
				queryString += "and date(model.fechaCreado) = date(:fecha) ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (fecha != null)
				query.setParameter("fecha", fecha);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TopicEstatus> findByProperty(String propertyName, Object value, Date fecha, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from TopicEstatus model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa: 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaCreado desc, model.id desc");
			if (fecha == null && value.getClass() == java.util.Date.class) {
				fecha = (Date) value;
				value = null;
			}
			
			if (value != null && value.getClass() != java.util.Date.class) 
				queryString += "and model." + propertyName + " = :propertyValue ";
			if (fecha != null)
				queryString += "and date(model.fechaCreado) = date(:fecha) ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (fecha != null)
				query.setParameter("fecha", fecha);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TopicEstatus> findByDates(Date fechaInicial, Date fechaFinal, long idEmpresa) throws Exception {
		String queryString = "select model from TopicEstatus model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa: 1L);
			if (fechaInicial != null)
				queryString += "and date(model.fechaCreado) >= date(:fechaInicial) ";
			if (fechaFinal != null)
				queryString += "and date(model.fechaCreado) <= date(:fechaFinal) ";
			queryString += "order by model.fechaCreado desc, model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (fechaInicial != null)
				query.setParameter("fechaInicial", fechaInicial);
			if (fechaFinal != null)
				query.setParameter("fechaFinal", fechaFinal);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<TopicEstatus> comprobarComando(String target, String referencia, String referenciaExtra, String atributos, Date fecha, long idEmpresa) throws Exception { 
		String queryString = "select model from TopicEstatus model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa: 1L);
			target = (target != null && ! "".equals(target.trim()) ? target.trim() : "");
			referencia = (referencia != null && ! "".equals(referencia.trim()) ? referencia.trim() : "");
			referenciaExtra = (referenciaExtra != null && ! "".equals(referenciaExtra.trim()) ? referenciaExtra.trim() : "");
			atributos = (atributos != null && ! "".equals(atributos.trim()) ? atributos.trim() : "");
			if (! "".equals(target.trim()))
				queryString += "and trim(model.target) = trim(:target) ";
			if (! "".equals(referencia.trim()))
				queryString += "and trim(model.referencia) = trim(:referencia) ";
			if (! "".equals(referenciaExtra.trim()))
				queryString += "and trim(model.referenciaExtra) = trim(:referenciaExtra) ";
			if (! "".equals(atributos.trim()))
				queryString += "and trim(model.atributos) = trim(:atributos) ";
			if (fecha != null)
				queryString += "and date(model.fechaCreado) = date(:fecha) ";
			queryString += "order by model.fechaCreado desc, model.id desc ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (! "".equals(target.trim()))
				query.setParameter("target", target);
			if (! "".equals(referencia.trim()))
				query.setParameter("referencia", referencia);
			if (! "".equals(referenciaExtra.trim()))
				query.setParameter("referenciaExtra", referenciaExtra);
			if (! "".equals(atributos.trim()))
				query.setParameter("atributos", atributos);
			if (fecha != null)
				query.setParameter("fecha", fecha);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
