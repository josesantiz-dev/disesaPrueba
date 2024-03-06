package net.giro.contabilidad.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.contabilidad.beans.MensajeTransaccion;

@Stateless
public class MensajeTransaccionImp extends DAOImpl<MensajeTransaccion> implements MensajeTransaccionDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(MensajeTransaccion entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<MensajeTransaccion> saveOrUpdateList(List<MensajeTransaccion> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MensajeTransaccion> findByProperty(String propertyName, final Object value, boolean incluyeContabilizados, boolean incluyeCancelados, long idEmpresa, String orderBy, int limite) {
		String queryString = "select model from MensajeTransaccion model where model.idEmpresa = :idEmpresa and model.estatus in (:estatus) ";
		String strEstatus = "0,1";
		
		try {
			if (value != null) 
				queryString = " and model."+ propertyName + " = :propertyValue";
			if (incluyeContabilizados)
				strEstatus += ",2";
			if (incluyeCancelados)
				strEstatus += ",-1";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
			queryString = queryString.replace(":estatus", strEstatus);
			
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
	public List<MensajeTransaccion> findLikeProperty(String propertyName, Object value, boolean incluyeContabilizados, boolean incluyeCancelados, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from MensajeTransaccion model where model.idEmpresa = :idEmpresa and model.estatus in (:estatus) ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String strEstatus = "0,1";
		StringBuffer sb = null;
		
		try {
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}
			
			if (incluyeContabilizados)
				strEstatus += ",2";
			if (incluyeCancelados)
				strEstatus += ",-1";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
			queryString = queryString.replace(":estatus", strEstatus);
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString().trim()))
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
	public List<MensajeTransaccion> findByProperties(HashMap<String, Object> params, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from MensajeTransaccion model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			for (Entry<String, Object> e : params.entrySet()) {
				if (e.getValue().getClass() == java.util.Date.class) 
					queryString += "and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					queryString += "and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<MensajeTransaccion> findLikeProperties(HashMap<String, String> params, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from MensajeTransaccion model where model.idEmpresa = :idEmpresa ";
		
		try {
			for (Entry<String, String> e : params.entrySet())
				queryString += "and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<MensajeTransaccion> findInProperty(String propertyName, List<Object> values, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from MensajeTransaccion model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()) {
    			for (int i = 0; i < values.size(); i++) {
    				if (! "".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + propertyName + i;
    			}

    			whereString += "and cast(model." + propertyName + " as string) IN (" + inFilter + ") ";
        	}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
        	
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
    	} finally {
			orderBy = null;
		}
	}

	@Override
	public MensajeTransaccion comprobarMensajeTransaccion(Long idTransaccion, Long idOperacion, long idEmpresa) throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<MensajeTransaccion> lista = null;
		MensajeTransaccion resultado = null;
		
		try {
			if (idTransaccion == null || idTransaccion <= 0L)
				return null;
			if (idOperacion == null || idOperacion <= 0L)
				return null;
			params.put("idTransaccion", idTransaccion);
			params.put("idOperacion", idOperacion);
			lista = this.findByProperties(params, idEmpresa, "model.id desc", 0);
			if (lista != null && ! lista.isEmpty()) {
				for (MensajeTransaccion var : lista) {
					if (var.getEstatus() == 0 || var.getEstatus() == 1) {
						resultado = var;
						break;
					}
				}
			}
			
			return resultado;
		} catch (Exception re) {
			throw re;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.1	| 14/07/2016 | Javier Tirado	| Creacion de MensajeTransaccionImp