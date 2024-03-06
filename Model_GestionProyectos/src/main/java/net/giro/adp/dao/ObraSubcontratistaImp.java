package net.giro.adp.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.adp.beans.ObraSubcontratista;

@Stateless
public class ObraSubcontratistaImp extends DAOImpl<ObraSubcontratista> implements ObraSubcontratistaDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ObraSubcontratista entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<ObraSubcontratista> saveOrUpdateList(List<ObraSubcontratista> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraSubcontratista> findAll(long idObra, String orderBy) throws Exception {
		String queryString = "select model from ObraSubcontratista model where model.idObra.id = :idObra ";
		
		try {
			idObra = (idObra > 0 ? idObra : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folioFactura desc, model.idFactura desc");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ObraSubcontratista> findLikeProperty(String propertyName, Object value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraSubcontratista model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = null;
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0 ? idObra : 0L);
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folioFactura desc, model.idFactura desc");
			limite = (value == null && idObra <= 0L && limite <= 0 ? 1000 : limite);
			
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					formateador = new SimpleDateFormat("MM/dd/yyyy");
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}
			
			if (idObra > 0L)
				queryString += "and model.idObra.id = :idObra ";
			queryString += "order by " + orderBy;
			
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
	public List<ObraSubcontratista> findByProperty(String propertyName, Object value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ObraSubcontratista model where model.idEmpresa = :idEmpresa ";
		
		try {
			idObra = (idObra > 0 ? idObra : 0L);
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idObra.id, model.fecha desc, model.folioFactura desc, model.idFactura desc");
			limite = (value == null && idObra <= 0L && limite <= 0 ? 1000 : limite);
			
			if (value != null) 
				queryString += "and model." + propertyName + " = :propertyValue ";
			
			if (idObra > 0L)
				queryString += "and model.idObra.id = :idObra ";
			queryString += "order by " + orderBy;
			
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
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-02-19 | Javier Tirado 	| Creacion de EJB
 */