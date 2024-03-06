package net.giro.cxp.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import net.giro.cxp.beans.GastosImpuesto;

@Stateless
public class GastosImpuestoImp extends DAOImpl<GastosImpuesto> implements GastosImpuestoDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@SuppressWarnings("unchecked")
	public List<GastosImpuesto> findAll() {
		try {
			final String queryString = "select model from GastosImpuesto model ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {		
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GastosImpuesto> findAll(long idGasto, String orderBy) {
		String queryString = "select model from GastosImpuesto model where model.gastoId = :idGasto ";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.id";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idGasto", idGasto);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GastosImpuesto> findAllByImpuesto(long idImpuesto, String orderBy) {
		String queryString = "select model from GastosImpuesto model where model.impuestoId = :idImpuesto ";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.id";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idImpuesto", idImpuesto);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<GastosImpuesto> findAllById(List<Long> idGastosImpuestos) {
		String queryString = "select gtosImpto from GastosImpuesto gtosImpto where gtosImpto.id in (:lista) ";
		String lista = "";
		
		try {
			if (idGastosImpuestos == null || idGastosImpuestos.isEmpty())
				return null;
			lista = StringUtils.join(idGastosImpuestos, ",");
			queryString = queryString.replace(":lista", lista);
					
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();		
		} catch (Exception re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<GastosImpuesto> findByProperty(String propertyName,	final Object value) {
		try {
			final String queryString = "select model from GastosImpuesto model where model."+ propertyName + "= :propertyValue ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<GastosImpuesto> findByPropertyPojoCompleto(String propertyName, String tipo, long value) {
		String queryString = "select gtosImpto from GastosImpuesto gtosImpto where gtosImpto."	+ propertyName + " = :propertyValue ";
		String where = "";
		
		try {
			queryString = queryString + where;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();		
		} catch (Exception re) {
			throw re;
		}
	}
}
