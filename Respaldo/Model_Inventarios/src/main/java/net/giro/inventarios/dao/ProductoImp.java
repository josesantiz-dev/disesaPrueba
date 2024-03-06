package net.giro.inventarios.dao;
import net.giro.DAOImpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.inventarios.beans.Producto;

@Stateless
public class ProductoImp extends DAOImpl<Producto> implements ProductoDAO {	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void delete(Producto entity) {
		try {
			entity = entityManager.getReference(Producto.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public void update(Producto entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public Producto findById(Long id) {
		try {
			Producto instance = entityManager.find(Producto.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from Producto model where model."
					+ propertyName + "= :propertyValue and model.estatus = 0 and model.oculto = 0 order by model." + propertyName;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByNombre(String descripcion) {
		try {
			final String queryString = "select model from Producto model where lower(model.descripcion) like '%" + descripcion.toLowerCase() + "%' and model.estatus = 0 and model.oculto = 0 order by model.descripcion ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findAll() {
		try {
			final String queryString = "select model from Producto model where estatus = 0 and model.oculto = 0 order by model.clave ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findAllActivos() {
		try {
			final String queryString = "select model from Producto model where model.estatus = 0 and model.oculto = 0 order by model.clave ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findAllInactivos() {
		try {
			final String queryString = "select model from Producto model where model.estatus = 1 and model.oculto = 0 order by model.clave ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findAllOcultos() {
		try {
			final String queryString = "select model from Producto model where model.oculto = 1 order by model.clave ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByClave(String clave) {
		try {
			final String queryString = "select model from Producto model where lower(model.clave) = '" + clave.toLowerCase() + "' order by model.clave";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperty(String propertyName, String propertyValue, int limite) {
		return this.findLikeProperty(propertyName, propertyValue, null, 0, limite);
		/*String queryString = "select model from Producto model ";
		StringBuffer sb = null;
		
		try {
			if (propertyValue != null && ! "".equals(propertyValue)) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2))))
					queryString += " where cast(model."+ propertyName + " as string) LIKE :propertyValue and model.estatus = 0 and model.oculto = 0";
				else
					queryString = queryString + " where lower(model."+ propertyName + ") LIKE :propertyValue and model.estatus = 0 and model.oculto = 0";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(propertyValue.toLowerCase());
	    		sb.append("%");
			}
			
			queryString += " order by model." + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			if (propertyValue != null && !"".equals(propertyValue))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}*/
	}
	
	@Override
	public List<Producto> findLikeProperty(String propertyName, String propertyValue, Long idFamilia, int limite) {
		return this.findLikeProperty(propertyName, propertyValue, idFamilia, 0, limite);
		/*String queryString = "select model from Producto model ";
		StringBuffer sb = null;
		
		try {
			if (propertyValue != null && ! "".equals(propertyValue)) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += "where cast(model."+ propertyName + " as string) LIKE :propertyValue and model.estatus = 0 and model.oculto = 0 ";
				} else {
					queryString = queryString + "where lower(model."+ propertyName + ") LIKE :propertyValue and model.estatus = 0 and model.oculto = 0 ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(propertyValue.toLowerCase());
	    		sb.append("%");
				
				if (idFamilia != null && idFamilia > 0L)
					queryString += " and model.familia = " + idFamilia;
			} else {
				queryString += "where model.estatus = 0 and model.oculto = 0 ";
				if (idFamilia != null && idFamilia > 0L)
					queryString += "and model.familia = " + idFamilia;
			}
			
			queryString += " order by model." + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			if (propertyValue != null && !"".equals(propertyValue))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}*/
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findLikeProperty(String propertyName, String propertyValue, Long idFamilia, int tipo, int limite) {
		String queryString = "select model from Producto model ";
		StringBuffer sb = null;
		
		try {
			if (propertyValue != null && ! "".equals(propertyValue)) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += "where cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					queryString = queryString + "where lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(propertyValue.toLowerCase());
	    		sb.append("%");
	    		
	    		if (tipo > 0 && (idFamilia != null && idFamilia > 0L))
	    			queryString += "and model.tipo = " + tipo + " and model.familia = " + idFamilia + " ";
	    		else if (tipo > 0 && (idFamilia == null || idFamilia <= 0L))
	    			queryString += "and model.tipo = " + tipo + " ";
	    		else if (tipo <= 0 && (idFamilia != null && idFamilia > 0L))
	    			queryString += "and model.familia = " + idFamilia + " ";
				queryString += "and model.estatus = 0 and model.oculto = 0 ";
			} else {
				queryString += "where model.estatus = 0 and model.oculto = 0 ";
	    		if (tipo > 0 && (idFamilia != null && idFamilia > 0L))
	    			queryString += "and model.tipo = " + tipo + " and model.familia = " + idFamilia + " ";
	    		else if (tipo > 0 && (idFamilia == null || idFamilia <= 0L))
	    			queryString += "and model.tipo = " + tipo + " ";
	    		else if (tipo <= 0 && (idFamilia != null && idFamilia > 0L))
	    			queryString += "and model.familia = " + idFamilia + " ";
			}
			
			queryString += "order by model." + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			if (propertyValue != null && !"".equals(propertyValue))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<Producto> saveOrUpdateList(List<Producto> entities) throws Exception {
		return super.saveOrUpdateList(entities);
	}
	
	@Override
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, int limite) throws Exception {
		try {
			return this.findLikeProperties(params, operador, limite, null);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, int limite, String orderBy) throws Exception {
		String queryString = "select model from Producto model ";
		String operadorAlt = "";
		String whereString = "";
		String key = "";
		
		try {
			if (operador == null || "".equals(operador))
				operador = "AND";
			if (orderBy == null || "".equals(orderBy))
				orderBy = "model.clave";
			
			operadorAlt = operador;
			for(Entry<String, String> e : params.entrySet()) {
				key = e.getKey();
				operador = operadorAlt;
				
				if (key.contains("-")) {
					key = key.substring(0, key.indexOf("-"));
					operador = "OR";
				}
				
				if (whereString.length() > 0)
					whereString += " " + operador;
				whereString += " lower(cast(model." + key + " as string)) like '%" + e.getValue().trim().toLowerCase() + "%'";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findLikeProperties(HashMap<String, String> params, int tipo, int limite, String orderBy) throws Exception {
		String queryString = "select model from Producto model ";
		String operadorAlt = "";
		String operador = "";
		String whereString = "";
		String key = "";
		
		try {
			operador = "AND";
			operadorAlt = operador;
			
			if (orderBy == null || "".equals(orderBy))
				orderBy = "model.clave";
			
			for(Entry<String, String> e : params.entrySet()) {
				key = e.getKey();
				operador = operadorAlt;
				
				if (key.contains("-")) {
					key = key.substring(0, key.indexOf("-"));
					operador = "OR";
				}
				
				if (whereString.length() > 0)
					whereString += " " + operador;
				whereString += " lower(cast(model." + key + " as string)) like '%" + e.getValue().trim().toLowerCase() + "%'";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + " where (" + whereString.trim() + ((tipo > 0) ? ") AND model.tipo = " + tipo : ")");
			queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<Producto> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			return this.findByProperties(params, limite, null);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByProperties(HashMap<String, Object> params, int limite, String orderBy) throws Exception {
		LinkedHashMap<String, String> parametros = new LinkedHashMap<String, String>();
		HashMap<String, String> operadores = new HashMap<String, String>();
		HashMap<String, String> uniones = new HashMap<String, String>();
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Producto model ";
		String[] splitted = null;
		String whereString = "";
		String operador = "=";
		String union = "";
		String key = "";
		
		try {
			if (orderBy == null || "".equals(orderBy))
				orderBy = "model.clave";

			operadores.put("=", "=");
			operadores.put("!", "<>");
			operadores.put("%", "like");

			uniones.put("&", "and");
			uniones.put("|", "or");

			for (Entry<String, Object> item : params.entrySet()) {
				key = item.getKey();
				operador = key.substring(0, 1);
				union = key.substring(0, key.length() - 1);
				
				if (operadores.containsKey(operador))
					operador = operadores.get(operador);
				else
					operador = "=";
				
				if (uniones.containsKey(union))
					union = uniones.get(union);
				else
					union = "and";
				
				splitted = key.split("");
				key = "";
				for (String var : splitted) {
					if (operadores.containsKey(var)) continue;
					if (uniones.containsKey(var)) continue;
					key += var.trim();
				}
				
				parametros.put(item.getKey(), ((! parametros.isEmpty()) ? union : "") + " " + key + " " + operador + " OBJECT");
			}
			
			for(Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " and";
				
				if (e.getValue().getClass() == java.util.Date.class) {
					whereString += union + " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				} else if (e.getValue().getClass() == java.math.BigDecimal.class) {
					whereString += " lower(cast(model." + e.getKey() + " as string)) = '" + ((BigDecimal) e.getValue()).toString().toLowerCase() + "' ";
				} else { 
					whereString += " lower(cast(model." + e.getKey() + " as string)) = '" + e.getValue().toString().toLowerCase() + "' ";
				}
			}
						
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByClaveRango(String prefix, int inicioRango, int limiteRango) throws Exception {
		String queryString = "select model from Producto model ";
		
		try {
			if (prefix == null || "".equals(prefix.trim())) 
				return null;
			
			prefix = prefix.trim().toLowerCase();
			if (inicioRango <= 0 && limiteRango <= 0) {
				queryString += "where lower(model.clave) LIKE '" + prefix + "%' ";
				queryString += "order by cast(substring(model.clave, " + (prefix.length() + 1) + ") as int)";
			} else {
				queryString += "where lower(model.clave) LIKE '" + prefix + "%' ";
				queryString += "AND cast(substring(model.clave, " + (prefix.length() + 1) + ") as int) >= " + inicioRango + " ";
				if (limiteRango > 0)
					queryString += "AND cast(substring(model.clave, " + (prefix.length() + 1) + ") as int) <= " + limiteRango + " ";
				queryString += "order by cast(substring(model.clave, " + (prefix.length() + 1) + ") as int)";
			}
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
}
/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-06-19 | Javier Tirado 	| Implemento el metodo findLikeProperties
 *  2.1 | 2016-11-12 | Javier Tirado 	| Implemento el metodo saveOrUpdateList(List<Producto> entities)
 */