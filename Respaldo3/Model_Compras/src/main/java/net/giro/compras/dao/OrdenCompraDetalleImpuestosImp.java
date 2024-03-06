package net.giro.compras.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.compras.beans.OrdenCompraDetalleImpuestos;
import net.giro.compras.comun.ExcepConstraint;

@Stateless
public class OrdenCompraDetalleImpuestosImp extends DAOImpl<OrdenCompraDetalleImpuestos> implements OrdenCompraDetalleImpuestosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(OrdenCompraDetalleImpuestos entity, Long idEmpresa) throws ExcepConstraint {
		try {
			return super.save(entity, idEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<OrdenCompraDetalleImpuestos> saveOrUpdateList(List<OrdenCompraDetalleImpuestos> entities, Long idEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, idEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OrdenCompraDetalleImpuestos> findByProperty(String propertyName, Object value, int limite) throws RuntimeException {
		String queryString = "select model from OrdenCompraDetalleImpuestos model ";
		String whereString = "";
		
		try {
			if (value != null)
				whereString = "where model."+ propertyName + " = :propertyValue ";
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			queryString += "order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
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
	public List<OrdenCompraDetalleImpuestos> findLikeProperty(String propertyName, String value, int limite) throws Exception {
		String queryString = "select model from OrdenCompraDetalleImpuestos model ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.trim())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					whereString = "where cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					whereString = "where lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
				sb.append("%");
				sb.append(value.trim().toLowerCase());
				sb.append("%");
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			queryString += "order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
