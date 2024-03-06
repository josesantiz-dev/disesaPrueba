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
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Producto entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Producto> saveOrUpdateList(List<Producto> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
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
	public List<Producto> findAll() {
		try {
			final String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and estatus = 0 and model.oculto = 0 order by model.clave ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findAllActivos() {
		try {
			final String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and model.estatus = 0 and model.oculto = 0 order by model.clave ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findAllInactivos() {
		try {
			final String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and model.estatus = 1 and model.oculto = 0 order by model.clave ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findAllOcultos() {
		try {
			final String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and model.oculto = 1 order by model.clave ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findBy(Object value, long idFamilia, int tipoMaestro, int limite) {
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and model.estatus = 0 and model.oculto = 0 ";
		
		try {
			if (tipoMaestro <= 0)
				tipoMaestro = 1;
			
			if (value != null) {
				queryString += "and (model.id = :propertyValue " 
						+ "or model.clave = :propertyValue " 
						+ "or model.descripcion = :propertyValue " 
						+ "or model.familia = :propertyValue " 
						+ "or model.descFamilia = :propertyValue " 
						+ "or model.unidadMedida = :propertyValue " 
						+ "or model.descUnidadMedida = :propertyValue " 
						+ "or model.claveSat = :propertyValue) ";
			} 
			
			if (idFamilia > 0L)
				queryString += "and model.familia = :idFamilia ";
			queryString += "and model.tipo = :tipoMaestro ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value);
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and model."
					+ propertyName + "= :propertyValue and model.estatus = 0 and model.oculto = 0 order by model." + propertyName;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
			final String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and lower(model.descripcion) like '%" + descripcion.toLowerCase() + "%' and model.estatus = 0 and model.oculto = 0 order by model.descripcion ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByClave(String clave) {
		try {
			final String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and lower(model.clave) = '" + clave.toLowerCase() + "' order by model.clave";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findLike(String value, long idFamilia, int tipoMaestro, int limite) {
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and model.estatus = 0 and model.oculto = 0 ";
		StringBuffer sb = null;
		
		try {
			if (tipoMaestro <= 0)
				tipoMaestro = 1;
			
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) LIKE :propertyValue " 
						+ "or lower(trim(model.clave)) LIKE :propertyValue " 
						+ "or lower(trim(model.descripcion)) LIKE :propertyValue " 
						+ "or lower(trim(model.descFamilia)) LIKE :propertyValue " 
						+ "or lower(trim(model.descUnidadMedida)) LIKE :propertyValue " 
						+ "or lower(trim(model.claveSat)) LIKE :propertyValue) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toLowerCase());
	    		sb.append("%");
			} 
			
			if (idFamilia > 0L)
				queryString += "and model.familia = :idFamilia ";
			queryString += "and model.tipo = :tipoMaestro ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Producto> findLikeProperty(String propertyName, String propertyValue, int limite) {
		return this.findLikeProperty(propertyName, propertyValue, null, 0, limite);
	}
	
	@Override
	public List<Producto> findLikeProperty(String propertyName, String propertyValue, Long idFamilia, int limite) {
		return this.findLikeProperty(propertyName, propertyValue, idFamilia, 0, limite);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findLikeProperty(String propertyName, String propertyValue, Long idFamilia, int tipo, int limite) {
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (propertyValue != null && ! "".equals(propertyValue)) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					queryString = queryString + "and lower(model."+ propertyName + ") LIKE :propertyValue ";
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
			query.setParameter("idEmpresa", getIdEmpresa());
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
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa ";
		String operadorAlt = "";
		String whereString = "";
		String key = "";
		
		try {
			if (operador == null || "".equals(operador))
				operador = "and";
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

				whereString += operador + " lower(cast(model." + key + " as string)) like '%" + e.getValue().trim().toLowerCase() + "%' ";
			}
			
			if (! whereString.isEmpty())
				queryString += whereString;
			queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa ";
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
				whereString += " lower(cast(model." + key + " as string)) like '%" + e.getValue().trim().toLowerCase() + "%' ";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + "and (" + whereString.trim() + ((tipo > 0) ? ") AND model.tipo = " + tipo : ")");
			queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa ";
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
			
			for (Entry<String, Object> e : params.entrySet()) {
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
				queryString = queryString + "and " + whereString;
			queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and model.oculto = 0 ";
		
		try {
			if (prefix == null || "".equals(prefix.trim())) 
				return null;
			
			prefix = prefix.trim().toLowerCase();
			queryString += "and lower(model.clave) LIKE '" + prefix + "%' ";
			queryString += "and cast(substring(model.clave, " + (prefix.length() + 1) + ") as int) >= " + inicioRango + " ";
			if (limiteRango > 0)
				queryString += "and cast(substring(model.clave, " + (prefix.length() + 1) + ") as int) <= " + limiteRango + " ";
			queryString += "order by cast(substring(model.clave, " + (prefix.length() + 1) + ") as int)";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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