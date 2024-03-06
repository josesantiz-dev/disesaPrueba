package net.giro.cargas.documentos.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.giro.DAOImplFact;
import net.giro.cargas.documentos.beans.RecursosDocumentosModulos;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RecursosDocumentosModulosImp extends DAOImplFact<RecursosDocumentosModulos> implements RecursosDocumentosModulosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(RecursosDocumentosModulos entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa <= 0L ? 1L : codigoEmpresa);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<RecursosDocumentosModulos> saveOrUpdateList(List<RecursosDocumentosModulos> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa <= 0L ? 1L : codigoEmpresa);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RecursosDocumentosModulos> findAll(long idRecursoDocumento) throws Exception {
		String queryString = "select model from RecursosDocumentosModulos model where model.idRecursoDocumento = :idRecursoDocumento order by model.idRecursoDocumento, model.moduloNombre";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idRecursoDocumento", idRecursoDocumento);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RecursosDocumentosModulos> findLikeProperty(String property, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from RecursosDocumentosModulos model ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa <= 0L ? 1L : idEmpresa);
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "where date(model." + property + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "where lower(trim(cast(model." + property + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}

			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.nombre";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.toString().trim()))
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
	public List<RecursosDocumentosModulos> findByProperty(String property, Object value, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from RecursosDocumentosModulos model ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			idEmpresa = (idEmpresa <= 0L ? 1L : idEmpresa);
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "where date(model." + property + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "where model." + property + " = :propertyValue ";
				}
			}
			
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.moduloNombre";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.toString().trim()))
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
}
