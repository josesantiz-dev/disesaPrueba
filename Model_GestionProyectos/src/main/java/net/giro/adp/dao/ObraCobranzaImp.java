package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.ObraCobranza;

@Stateless
public class ObraCobranzaImp extends DAOImpl<ObraCobranza> implements ObraCobranzaDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(ObraCobranza entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<ObraCobranza> saveOrUpdateList(List<ObraCobranza> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranza> findAll(long idObra, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraCobranza model where model.idObra.id = :idObra ";
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folio desc, model.idFactura desc");
			queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranza> findLikeProperty(String propertyName, Object value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraCobranza model where model.idObra.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folio desc, model.idFactura desc");
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) 
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				else 
					queryString = queryString + " and lower(model."+ propertyName + ") LIKE :propertyValue";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			if (idObra > 0L) 
				queryString = queryString + "and model.idObra = :idObra ";
			queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranza> findByProperty(String propertyName, Object value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraCobranza model where model.idObra.idEmpresa = :idEmpresa ";
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folio desc, model.idFactura desc");
			if (value != null) 
				queryString = queryString + " and model."+ propertyName + " = :propertyValue";
			if (idObra > 0L) 
				queryString = queryString + "and model.idObra = :idObra ";
			queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranza> findBySpecific(long idObra, long idFactura, long idConcepto, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraCobranza model where model.idObra.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			idObra = (idObra > 0L ? idObra : 0L);
			idFactura = (idFactura > 0L ? idFactura : 0L);
			idConcepto = (idConcepto > 0L ? idConcepto : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folio desc, model.idFactura desc");
			limite = (limite == 0 && idObra <= 0L && idFactura <= 0L && idConcepto <= 0L ? 1000 : limite);
			if (idObra > 0L) 
				queryString = queryString + "and model.idObra = :idObra ";
			if (idFactura > 0L) 
				queryString = queryString + "and model.idFactura = :idFactura ";
			if (idConcepto > 0L) 
				queryString = queryString + "and model.idConcepto = :idConcepto ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObra > 0L)
				query.setParameter("idObra", idObra);
			if (idFactura > 0L)
				query.setParameter("idFactura", idFactura);
			if (idConcepto > 0L)
				query.setParameter("idConcepto", idConcepto);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	/*@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranza> findByProperties(HashMap<String, String> params, long idEmpresa, String orderBy, int limite) throws Exception{
		String queryString = "select model from ObraCobranza model where model.idObra.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folio desc, model.idFactura desc");
			for (Entry<String, String> e : params.entrySet()) 
				queryString += " and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "'";
			queryString += " order by " + orderBy;

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
	public List<ObraCobranza> findList(List<Long> lista, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from ObraCobranza model where model.idObra.idEmpresa = :idEmpresa and model.id in (:lista) ";
		String listValues = "";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folio desc, model.idFactura desc");
			lista = (lista != null && ! lista.isEmpty() ? lista : new ArrayList<Long>());
			listValues = (! lista.isEmpty() ?  StringUtils.join(lista, ",") : "0");
			queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("lista", listValues);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranza> findInProperty(String propertyName, List<Object> values, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraCobranza model where model.idObra.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folio desc, model.idFactura desc");
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = " and cast(model." + propertyName + " as string) IN (";
    			for (int i = 0; i < values.size(); i++) 
    				inFilter += (!"".equals(inFilter) ? "," : "") + ":" + propertyName + i;
        		sqlWhere = sqlWhere + inFilter + ")";
        	}
        	
        	queryString += sqlWhere;
			queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) 
        			query.setParameter(propertyName + i, values.get(i).toString());
        	}
			if (limite > 0)
				query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranza> findLikeProperties(HashMap<String, String> params, long idEmpresa, String orderBy, int limite) throws Exception{
		String queryString = "select model from ObraCobranza model where model.idObra.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folio desc, model.idFactura desc");
			for (Entry<String, String> e : params.entrySet()) 
				queryString += " and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "'";
			queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}*/
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 24/05/2016 | Javier Tirado	| Creacion de ObraCobranzaImp