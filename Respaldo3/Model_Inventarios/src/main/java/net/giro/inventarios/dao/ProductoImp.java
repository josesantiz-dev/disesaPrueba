package net.giro.inventarios.dao;

import net.giro.DAOImpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import net.giro.inventarios.beans.Producto;

@Stateless
public class ProductoImp extends DAOImpl<Producto> implements ProductoDAO {	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(Producto entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Producto> saveOrUpdateList(List<Producto> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findAll(boolean incluyeCancelados, boolean incluyeOcultos, String orderBy, long idEmpresa) {
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy : "model.clave");
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1" : "") + ") ";
			queryString += "and model.oculto in (0" + (incluyeOcultos ? ",1" : "") + ") ";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findLike(String value, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, long idEmpresa, String orderBy, int limite) {
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and model.estatus = 0 and model.oculto = 0 ";
		List<String> valores = null;
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy : "model.clave");
			tipoMaestro = (tipoMaestro > 0 ? tipoMaestro : 1);
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) LIKE :propertyValue " 
						+ "or lower(trim(model.clave)) LIKE :propertyValue " 
						+ "or lower(trim(model.descripcion)) LIKE :propertyValue " 
						+ "or lower(trim(model.descFamilia)) LIKE :propertyValue " 
						+ "or lower(trim(model.descUnidadMedida)) LIKE :propertyValue " 
						+ "or lower(trim(model.claveSat)) LIKE :propertyValue) ";

				valores = recuperaValores(value.toString().toLowerCase().trim(), "\\+");
				if (valores == null || valores.isEmpty()) {
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.trim().toLowerCase());
		    		sb.append("%");
				}
			} 

			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1" : "") + ") ";
			queryString += "and model.oculto in (0" + (incluyeOcultos ? ",1" : "") + ") ";
			if (idFamilia > 0L)
				queryString += "and model.familia = :idFamilia ";
			queryString += "and model.tipo = :tipoMaestro ";
			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "model.clave";
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (sb != null && ! "".equals(sb.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findLikeProperty(String propertyName,Object propertyValue, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, long idEmpresa, String orderBy, int limite) {
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and model.estatus = 0 and model.oculto = 0 ";
		List<String> valores = null;
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy : "model.clave");
			if (propertyValue != null) {
				if (propertyValue.getClass() == java.util.Date.class) {
					propertyValue = "";
				} else if (propertyValue.getClass() == java.lang.String.class) {
					queryString += "and lower(trim(" + propertyName + ")) like :propertyValue ";
				} else if (! "".equals(propertyValue.toString().trim())) {
					queryString += "and lower(trim(cast(" + propertyName + " as string))) like :propertyValue ";
				}
				
				valores = recuperaValores(propertyValue.toString().toLowerCase().trim(), "\\+");
				if (valores == null || valores.isEmpty()) {
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(propertyValue.toString().trim().toLowerCase());
		    		sb.append("%");
				} 
			}

			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1" : "") + ") ";
			queryString += "and model.oculto in (0" + (incluyeOcultos ? ",1" : "") + ") ";
    		if (tipoMaestro > 0 && idFamilia > 0L)
    			queryString += "and model.tipo = " + tipoMaestro + " and model.familia = " + idFamilia + " ";
    		else if (tipoMaestro > 0 && idFamilia <= 0L)
    			queryString += "and model.tipo = " + tipoMaestro + " ";
    		else if (tipoMaestro <= 0 && idFamilia > 0L)
    			queryString += "and model.familia = " + idFamilia + " ";
			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "model.clave";
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (sb != null && ! "".equals(sb.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByProperty(String propertyName, Object propertyValue, long idFamilia, int tipoMaestro, boolean incluyeCancelados, boolean incluyeOcultos, long idEmpresa, String orderBy, int limite) {
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy : "model.clave");
			if (propertyValue != null && ! "".equals(propertyValue)) 
				queryString += "and model."+ propertyName + " = :propertyValue ";
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1" : "") + ") ";
			queryString += "and model.oculto in (0" + (incluyeOcultos ? ",1" : "") + ") ";
    		if (tipoMaestro > 0 && idFamilia > 0L)
    			queryString += "and model.tipo = " + tipoMaestro + " and model.familia = " + idFamilia + " ";
    		else if (tipoMaestro > 0 && idFamilia <= 0L)
    			queryString += "and model.tipo = " + tipoMaestro + " ";
    		else if (tipoMaestro <= 0 && idFamilia > 0L)
    			queryString += "and model.familia = " + idFamilia + " ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (propertyValue != null && ! "".equals(propertyValue))
				query.setParameter("propertyValue", propertyValue);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByClave(String clave, boolean incluyeCancelados, boolean incluyeOcultos, String orderBy, long idEmpresa) {
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and lower(model.clave) = :clave ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy : "model.clave, model.id desc");
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1" : "") + ") ";
			queryString += "and model.oculto in (0" + (incluyeOcultos ? ",1" : "") + ") ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("clave", clave.toLowerCase());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByClaveRango(String prefix, int inicioRango, int limiteRango, long idEmpresa) throws Exception {
		String queryString = "select model from Producto model where model.idEmpresa = :idEmpresa and model.oculto = 0 ";
		
		try {
			if (prefix == null || "".equals(prefix.trim())) 
				return null;
			prefix = prefix.trim().toLowerCase();
			queryString += "and lower(model.clave) like '" + prefix + "%' ";
			if (limiteRango > 0)
				queryString += "and fn_util_producto_clave2number(model.clave, :prefix) between " + inicioRango + " and " + limiteRango;
			else
				queryString += "and fn_util_producto_clave2number(model.clave, :prefix) >= " + inicioRango;
			queryString += " order by fn_util_producto_clave2number(model.clave, :prefix)";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("prefix", prefix);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findList(List<Long> idProductos) throws Exception {
		String queryString = "select model from Producto model where model.id in (:lista) ";
		String lista = "";
		
		try {
			idProductos = (idProductos != null && ! idProductos.isEmpty() ? idProductos : new ArrayList<Long>());
			lista = (! idProductos.isEmpty() ?  StringUtils.join(idProductos, ",") : "0");
			queryString = queryString.replace(":lista", lista);
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, long idEmpresa, int limite) throws Exception {
		try {
			return this.findLikeProperties(params, operador, "", idEmpresa, limite);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findLikeProperties(HashMap<String, String> params, String operador, String orderBy, long idEmpresa, int limite) throws Exception {
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
			for (Entry<String, String> e : params.entrySet()) {
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
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findLikeProperties(HashMap<String, String> params, int tipo, String orderBy, long idEmpresa, int limite) throws Exception {
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
			
			for (Entry<String, String> e : params.entrySet()) {
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
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Producto> findByProperties(HashMap<String, Object> params, long idEmpresa, int limite) throws Exception {
		try {
			return this.findByProperties(params, null, idEmpresa, limite);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Producto> findByProperties(HashMap<String, Object> params, String orderBy, long idEmpresa, int limite) throws Exception {
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
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} 
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// PRIVADOS 
	// ------------------------------------------------------------------------------------------------------------------------------------
	
	private List<String> recuperaValores(String valor, String separador) {
		List<String> valores = null;
		String[] splitted = null;
		
		if ((valor == null || "".equals(valor.trim())) || (separador == null || "".equals(separador.trim())) || (! valor.trim().contains(separador.trim().replace("\\", ""))))
			return null;
		
		splitted = valor.split(separador);
		valores = new ArrayList<String>();
		for (int i = 0; i < splitted.length; i++)
			valores.add(splitted[i].trim());
		return valores;
	}
	
	private String multiplicaConsulta(String queryOriginal, List<String> valores) {
		String queryModificada = "";
		
		if (valores == null || valores.isEmpty() || valores.size() == 1)
			return queryOriginal;
		
		for (String valor : valores)
			queryModificada += (! "".equals(queryModificada.trim()) ? " or " : "") +  "model.id in (" + queryOriginal.trim().replace("select model from", "select model.id from").replace(":propertyValue", "'%" + valor + "%'") + ")";
		return "select model from Producto model where (" + queryModificada.trim() + ") ";
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