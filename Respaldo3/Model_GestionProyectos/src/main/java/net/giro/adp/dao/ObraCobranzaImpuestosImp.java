package net.giro.adp.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.ObraCobranzaImpuestos;

@Stateless
public class ObraCobranzaImpuestosImp extends DAOImpl<ObraCobranzaImpuestos> implements ObraCobranzaImpuestosDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ObraCobranzaImpuestos entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ObraCobranzaImpuestos> saveOrUpdateList(List<ObraCobranzaImpuestos> entities, long codigoEmpresa)
			throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranzaImpuestos> findAll(long idObraCobranza, String orderBy) throws Exception {
		String queryString = "select model from ObraCobranzaImpuestos model where model.idObraCobranza.id = :idObraCobranza ";
		
		try {
			idObraCobranza = (idObraCobranza > 0 ? idObraCobranza : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObraCobranza.id desc, model.id");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObraCobranza", idObraCobranza);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	/*@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranzaImpuestos> findLikeProperty(String propertyName, Object value, long idObraCobranza, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraCobranzaImpuestos model ";
		SimpleDateFormat formateador = null;
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			idObraCobranza = (idObraCobranza > 0 ? idObraCobranza : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObraCobranza.id desc, model.id");
			limite = (value == null && idObraCobranza <= 0L && limite <= 0 ? 1000 : limite);
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					formateador = new SimpleDateFormat("MM/dd/yyyy");
					whereString += ("".equals(whereString.trim()) ? "where" : "and") + " date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					whereString += ("".equals(whereString.trim()) ? "where" : "and") + " lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}

			if (idObraCobranza > 0L)
				whereString += ("".equals(whereString.trim()) ? "where" : "and") + " model.idObraCobranza.id = :idObraCobranza ";
			queryString += whereString + "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idObraCobranza > 0L)
				query.setParameter("idObraCobranza", idObraCobranza);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraCobranzaImpuestos> findByProperty(String propertyName, Object value, long idObraCobranza, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraCobranzaImpuestos model ";
		String whereString = "";
		
		try {
			idObraCobranza = (idObraCobranza > 0 ? idObraCobranza : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObraCobranza.id desc, model.id");
			limite = (value == null && idObraCobranza <= 0L && limite <= 0 ? 1000 : limite);
			if (value != null) 
				whereString += ("".equals(whereString.trim()) ? "where" : "and") + " model." + propertyName + " = :propertyValue ";
			if (idObraCobranza > 0L)
				whereString += ("".equals(whereString.trim()) ? "where" : "and") + " model.idObraCobranza.id = :idObraCobranza ";
			queryString += whereString + "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (idObraCobranza > 0L)
				query.setParameter("idObraCobranza", idObraCobranza);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}*/
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-02-19 | Javier Tirado 	| Creacion de EJB
 */
