package net.giro.plataforma.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.plataforma.beans.InventariosExcluidos;

@Stateless
public class InventariosExcluidosImp extends DAOImpl<InventariosExcluidos> implements InventariosExcluidosDAO {
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
	public long save(InventariosExcluidos entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	public List<InventariosExcluidos> saveOrUpdateList(List<InventariosExcluidos> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<InventariosExcluidos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from InventariosExcluidos model where model.idEmpresa = :idEmpresa ";
		
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
	public List<InventariosExcluidos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from InventariosExcluidos model where model.idEmpresa = :idEmpresa ";
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
