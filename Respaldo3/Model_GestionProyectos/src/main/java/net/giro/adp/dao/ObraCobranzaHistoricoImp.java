package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.ObraCobranzaHistorico;

@Stateless
public class ObraCobranzaHistoricoImp extends DAOImpl<ObraCobranzaHistorico> implements ObraCobranzaHistoricoDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	
	@Override
	public void orderBy(String orderBy) {
		ObraCobranzaHistoricoImp.orderBy = orderBy;
	}
	
	@Override
	public long save(ObraCobranzaHistorico entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<ObraCobranzaHistorico> saveOrUpdateList(List<ObraCobranzaHistorico> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranzaHistorico> findByProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ObraCobranzaHistorico model where model.idObra.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null) {
				queryString = queryString + " and model."+ propertyName + " = :propertyValue";
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
			
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
	public List<ObraCobranzaHistorico> findLikeProperty(String propertyName, Object value, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ObraCobranzaHistorico model where model.idObra.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1;
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					queryString = queryString + " and lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
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
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 24/05/2016 | Javier Tirado	| Creacion de ObraCobranzaHistoricoImp