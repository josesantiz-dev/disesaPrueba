package net.giro.inventarios.dao;

import net.giro.DAOImpl;

import net.giro.inventarios.beans.Almacen;

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

@Stateless
public class AlmacenImp extends DAOImpl<Almacen> implements AlmacenDAO {
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
	public long save(Almacen entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Almacen> saveOrUpdateList(List<Almacen> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findAll() {
		final String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findAllActivos() {
		final String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa and model.estatus = 0";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findByProperty(String propertyName, final Object value) {
		String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa and model.estatus = 0 ";
		
		try {
			if (propertyName != null && ! "".equals(propertyName.trim()) && value != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			queryString += "order by model." + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (propertyName != null && ! "".equals(propertyName.trim()) && value != null)
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findLikeProperty(String propertyName, Object value) throws Exception {
		String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa and model.estatus = 0 ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					whereString = "and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					whereString = "and lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && ! "".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findByProperties(HashMap<String, Object> params) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			for(Entry<String, Object> e : params.entrySet()) {
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += "and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += "and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					whereString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			queryString += whereString;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findLikeProperties(HashMap<String, String> params) throws Exception {
		String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			for (Entry<String, String> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += "or ";
				whereString += "cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			}
			
			if (! whereString.isEmpty())
				queryString += "and (" + whereString + ") ";

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> comprobarPrincipal(Long idSucursal, Long idAlmacen) throws Exception {
		String queryString = "SELECT model FROM Almacen model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (idSucursal == null || idSucursal <= 0L) 
				return null;
			whereString = "and model.idSucursal = " + idSucursal + " AND model.tipo = 1 ";			
			if (idAlmacen != null && idAlmacen > 0L)
				whereString += "and model.id <> " + idAlmacen;
			queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> comprobarNombre(String nombre, Long idAlmacen) throws Exception {
		String queryString = "SELECT model FROM Almacen model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (nombre == null || "".equals(nombre)) 
				return null;
			whereString = "and model.nombre = '" + nombre + "' ";			
			if (idAlmacen != null && idAlmacen > 0L)
				whereString += "and model.id <> " + idAlmacen;
			queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
