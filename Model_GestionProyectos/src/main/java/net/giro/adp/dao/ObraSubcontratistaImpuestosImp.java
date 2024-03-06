package net.giro.adp.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.ObraSubcontratistaImpuestos;

@Stateless
public class ObraSubcontratistaImpuestosImp extends DAOImpl<ObraSubcontratistaImpuestos> implements ObraSubcontratistaImpuestosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(ObraSubcontratistaImpuestos entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<ObraSubcontratistaImpuestos> saveOrUpdateList(List<ObraSubcontratistaImpuestos> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ObraSubcontratistaImpuestos> findAll(long idObraSubcontratista, String orderBy) throws Exception {
		String queryString = "select model from ObraSubcontratistaImpuestos model where model.idObraSubcontratista.id = :idObraSubcontratista ";
		
		try {
			idObraSubcontratista = (idObraSubcontratista > 0 ? idObraSubcontratista : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObraSubcontratista.id desc, model.id");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObraSubcontratista", idObraSubcontratista);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ObraSubcontratistaImpuestos> findLikeProperty(String propertyName, Object value, long idObraSubcontratista, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraSubcontratistaImpuestos model ";
		SimpleDateFormat formateador = null;
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			idObraSubcontratista = (idObraSubcontratista > 0 ? idObraSubcontratista : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObraSubcontratista.id desc, model.id");
			limite = (value == null && idObraSubcontratista <= 0L && limite <= 0 ? 1000 : limite);
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

			if (idObraSubcontratista > 0L)
				whereString += ("".equals(whereString.trim()) ? "where" : "and") + " model.idObraSubcontratista.id = :idObraSubcontratista ";
			queryString += whereString + "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idObraSubcontratista > 0L)
				query.setParameter("idObraSubcontratista", idObraSubcontratista);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ObraSubcontratistaImpuestos> findByProperty(String propertyName, Object value, long idObraSubcontratista, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraSubcontratistaImpuestos model ";
		String whereString = "";
		
		try {
			idObraSubcontratista = (idObraSubcontratista > 0 ? idObraSubcontratista : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObraSubcontratista.id desc, model.id");
			limite = (value == null && idObraSubcontratista <= 0L && limite <= 0 ? 1000 : limite);
			if (value != null) 
				whereString += ("".equals(whereString.trim()) ? "where" : "and") + " model." + propertyName + " = :propertyValue ";
			if (idObraSubcontratista > 0L)
				whereString += ("".equals(whereString.trim()) ? "where" : "and") + " model.idObraSubcontratista.id = :idObraSubcontratista ";
			queryString += whereString + "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (idObraSubcontratista > 0L)
				query.setParameter("idObraSubcontratista", idObraSubcontratista);
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
 *  1.2 | 2020-02-19 | Javier Tirado 	| Creacion de EJB
 */