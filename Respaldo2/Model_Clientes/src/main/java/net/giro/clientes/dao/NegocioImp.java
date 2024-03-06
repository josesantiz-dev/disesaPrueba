package net.giro.clientes.dao;

import java.util.List;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.clientes.beans.Negocio;

@Stateless
public class NegocioImp extends DAOImpl<Negocio> implements NegocioDAO  {
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
	public long save(Negocio entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Negocio> saveOrUpdateList(List<Negocio> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findAll() {
		String queryString = "select model from Negocio model where model.idEmpresa = :idEmpresa ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findAll(String nombre) throws Exception{
		boolean isNumber = false;
		String queryString = "select model from Negocio model ";
		String where = "";
		
		try {
			if (!"".equals(nombre.trim())) {
				try { Integer.parseInt(nombre.trim()); isNumber= true; } 
				catch (Exception ex) { isNumber = false; }
				
				if (isNumber) {
					where = " where model.idEmpresa = :idEmpresa and  cast(model.id as varchar) like '%" + nombre.trim() + "%'";
				} else {
					where = " where model.idEmpresa = :idEmpresa and  lower(model.nombre) like '%" + nombre.trim().toLowerCase() + "%'";
				}
			}

			queryString += where;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findLikeId(String id) throws Exception{
		try {
			String queryString = "select Negs from Negocio Negs ";
			String where = " where model.idEmpresa = :idEmpresa and cast(Negs.id as varchar) like '%" + id + "%'";

			queryString += where;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findLikeColumnName(String propertyName, String value, int max) {
		String queryString = "select model from Negocio model where model.idEmpresa = :idEmpresa ";
		String orderBy = "";
		
		try {
			if (propertyName == null || "".equals(propertyName))
				propertyName = "nombre";
			
			orderBy = propertyName;
			if(value == null) {
				orderBy = "id desc";
				value = "";
			}
			
			queryString += "where lower(cast(model."+ propertyName + " as string)) like '%" + value.toLowerCase() + "%' ";
			queryString += "order by model." + orderBy;
							 			   
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if(max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re ) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findByProperty(String propertyName, Object value, int limite) {
		String queryString = "select model from Negocio model where model.idEmpresa = :idEmpresa ";
		String orderBy = "";
		
		try {
			if (propertyName == null || "".equals(propertyName))
				propertyName = "nombre";
			
			orderBy = "order by model.id desc";
			if (value != null) {
				queryString += "where model." + propertyName + " = :propertyValue ";
				orderBy = "order by model." + propertyName;
			}
			queryString += orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Negocio> findLikeProperty(String propertyName, String value, int limite) {
		String queryString = "select model from Negocio model ";
		//String queryString = "select model from Negocio model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		String orderBy = "";
		StringBuffer sb = null;
		
		try {
			if(value != null && ! "".equals(value.trim())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					whereString = "where cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					whereString = "where lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
	    		orderBy = propertyName;
			} else {
				orderBy = "id desc";
				value = "";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			queryString += "order by model." + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			//query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && !"".equals(value))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-05 | Javier Tirado		| Añado metodos findAll y findAll(String)
 * 1.2 | 2016-11-09 | Javier Tirado 	| Añado los metodos findByProperty y findLikeProperty
 */