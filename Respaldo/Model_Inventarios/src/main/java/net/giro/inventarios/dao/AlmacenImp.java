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
public class AlmacenImp extends DAOImpl<Almacen> implements AlmacenDAO{
	@PersistenceContext
	private EntityManager entityManager;

	public void delete(Almacen entity) {
		try {
			entity = entityManager.getReference(Almacen.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(Almacen entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public Almacen findById(Integer id) {
		try {
			Almacen instance = entityManager.find(Almacen.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Almacen> findByProperty(String propertyName, final Object value) {
		try {
			System.out.println("Model.findByProperty --> propertyName: "+propertyName+", value: "+value);
			String queryString = "select model from Almacen model";
			
			if(propertyName=="id"){
				queryString = "select model from Almacen model where id = "+ value ;
				
			}else{
				queryString = "select model from Almacen model where lower( model." + propertyName + " ) like '%"+ value.toString().toLowerCase() +"%'";
				System.out.println("queryString: "+queryString);
			}
			
			
			Query query = entityManager.createQuery(queryString);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Almacen> findAll() {
		try {
			final String queryString = "select model from Almacen model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Almacen> findAllActivos() {
		try {
			final String queryString = "select model from Almacen model where model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findByProperties(HashMap<String, Object> params) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			String queryString = "select model from Almacen model ";
			String whereString = "";
			
			for(Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " and";
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;

			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findLikeProperty(String propertyName, Object value) throws Exception {
		StringBuffer sb = null;
		
		try {
			String queryString = "select model from Almacen model ";
			String whereString = "";
			
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					whereString = " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					whereString = " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findLikeProperties(HashMap<String, String> params) throws Exception{
		try {
			String queryString = "select model from Almacen model ";
			String whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (whereString.length() > 0)
					whereString += " and";
				whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;

			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> comprobarPrincipal(Long idSucursal, Long idAlmacen) throws Exception {
		try {
			String queryString = "SELECT model FROM Almacen model ";
			String whereString = "";
			
			if(idSucursal == null || idSucursal <= 0L) 
				return null;
				
			whereString = "WHERE model.idSucursal = " + idSucursal + " AND model.tipo = 1";			
			if (idAlmacen != null && idAlmacen > 0L)
				whereString += " AND model.id <> " + idAlmacen;
			queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> comprobarNombre(String nombre, Long idAlmacen) throws Exception {
		try {
			String queryString = "SELECT model FROM Almacen model ";
			String whereString = "";
			
			if(nombre == null || "".equals(nombre)) 
				return null;
				
			whereString = "WHERE model.nombre = '" + nombre + "'";			
			if (idAlmacen != null && idAlmacen > 0L)
				whereString += " AND model.id <> " + idAlmacen;
			queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
