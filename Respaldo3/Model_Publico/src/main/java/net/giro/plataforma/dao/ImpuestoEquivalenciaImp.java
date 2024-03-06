package net.giro.plataforma.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.beans.ImpuestoEquivalencia;

@Stateless
public class ImpuestoEquivalenciaImp extends DAOImpl<ImpuestoEquivalencia> implements ImpuestoEquivalenciaDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(ImpuestoEquivalencia entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<ImpuestoEquivalencia> saveOrUpdateList(List<ImpuestoEquivalencia> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ImpuestoEquivalencia> findByTransaccion(Long codigoTransaccion) throws Exception {
		String queryString = "select model from ImpuestoEquivalencia model where model.idEmpresa = :idEmpresa and model.codigoTransaccion = :codigo ";
		
		try {
			if (codigoTransaccion == null)
				codigoTransaccion = 0L;
			queryString += "order by model.idImpuestoOrigen, model.idImpuestoDestino";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (codigoTransaccion != null && codigoTransaccion > 0L)
				query.setParameter("codigo", codigoTransaccion);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ImpuestoEquivalencia> findByProperty(String propertyName, Object value, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from ImpuestoEquivalencia model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else {
					queryString += "and lower(model." + propertyName + ") = :propertyValue ";
				}
			}

			queryString += "order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	public List<ImpuestoEquivalencia> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from ImpuestoEquivalencia model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else {
					queryString += "and cast(model." + propertyName + " as string) like :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			queryString += "order by " + propertyName;
						
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
}
