package net.giro.adp.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.adp.beans.Obra;

@Stateless
public class ObraImp extends DAOImpl<Obra> implements ObraDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	private static Long estatusId;
	
	@Override
	public void orderBy(String orderBy) { 
		ObraImp.orderBy = orderBy; 
	}

	@Override
	public void estatus(Long estatus) {
		ObraImp.estatusId = estatus;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findAll() {
		String queryString = "select model from Obra model ";
		
		try {
			if (estatusId != null) {
				if (estatusId > 0L)
					queryString += "where model.estatus = abs(" + estatusId.toString() + ")";
				else
					queryString += "where model.estatus <> abs(" + estatusId.toString() + ")";
				estatusId = null;
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			queryString += " order by model.nombre";
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findByProperty(String propertyName, final Object value, int opcion) {
		String queryString = "select model from Obra model ";
		String whereString = "";
		
		try {
			if(value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || "idobra".equals(propertyName.toLowerCase())) {
					whereString = "where cast(model."+ propertyName + " as string) = :propertyValue";
				} else {
					whereString = "where lower(cast(model."+ propertyName + " as string)) = :propertyValue";
				}
			}
			
			if (opcion > 0)
				whereString += (whereString.trim().equals("") ? " where model.tipoObra = :tipoObra" : " and model.tipoObra = :tipoObra");
			else
				whereString += (whereString.trim().equals("") ? " where model.tipoObra <> 4" : " and model.tipoObra <> 4");
			
			if(estatusId != null && whereString.trim().length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			else if(estatusId != null && whereString.trim().length() == 0)
				whereString = "model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			queryString += whereString;
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			if(opcion > 0)
				query.setParameter("tipoObra", opcion);
			if (value != null)
				query.setParameter("propertyValue", value.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findLikeProperty(String propertyName, final String value, int opcion) {
		String queryString = "select model from Obra model ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if(value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || "idobra".equals(propertyName.toLowerCase())) {
					whereString = " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					whereString = " where lower(cast(model."+ propertyName + " as string)) LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if(opcion > 0)
				whereString += (whereString.trim().equals("") ? " where model.tipoObra = :tipoObra" : " and model.tipoObra = :tipoObra");
			else
				whereString += (whereString.trim().equals("") ? " where model.tipoObra <> 4" : " and model.tipoObra <> 4");
			
			if(estatusId != null && whereString.trim().length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			else if(estatusId != null && whereString.trim().length() == 0)
				whereString = " where model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			queryString += whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
						
			Query query = entityManager.createQuery(queryString);
			if(opcion > 0)
				query.setParameter("tipoObra", opcion);
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findLikeProperty(String propertyName, String value, boolean incluyeAdministrativas) throws Exception {

		String queryString = "select model from Obra model ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if(value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || "idobra".equals(propertyName.toLowerCase())) {
					whereString = " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					whereString = " where lower(cast(model."+ propertyName + " as string)) LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if(! incluyeAdministrativas)
				whereString += (whereString.trim().equals("") ? " where model.tipoObra <> 4" : " and model.tipoObra <> 4");
			
			if(estatusId != null && whereString.trim().length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			else if(estatusId != null && whereString.trim().length() == 0)
				whereString = " where model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			queryString += whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
						
			Query query = entityManager.createQuery(queryString);
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Obra> findByPropertyPojoCompleto(String propertyName, String tipo, Object value) {
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findObraPrincipalByProperty(String propertyName, Object value, int tipoObra, Long idObra) {
		String queryString = "select model from Obra model ";
		
		try {
			if(propertyName.toLowerCase().equals("idobra")) {
				queryString = queryString + " where (model.idObraPrincipal = null or model.idObraPrincipal <= 0) and model.id <> :idObraPrincipal and model.idObra = " + value + " and model.tipoObra = :tipoObra";
			} else {
				queryString = queryString + " where (model.idObraPrincipal = null or model.idObraPrincipal <= 0) and model.id <> :idObraPrincipal and model."+ propertyName + " = :propertyValue and model.tipoObra = :tipoObra";
			}
			
			if(tipoObra != 0)
				queryString += " and model.tipoObra = :tipoObra";
			else
				queryString += " and model.tipoObra <> 4";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObraPrincipal", idObra);
			if (value != null)
				query.setParameter("propertyValue", value);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findObraPrincipalLikeProperty(String propertyName, Object value, int tipoObra, Long idObra) {
		String queryString = "select model from Obra model where (model.idObraPrincipal = null or model.idObraPrincipal <= 0) and model.id <> :idObraPrincipal ";
		StringBuffer sb = null;
		
		try {
			if (idObra == null)
				idObra = 0L;
			
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || "idobra".equals(propertyName.toLowerCase())) {
					queryString += " and cast(model."+ propertyName + " as string) LIKE :" + propertyName;
				} else {
					queryString = queryString + " and lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if(tipoObra != 0)
				queryString += " and model.tipoObra = :tipoObra";
			else
				queryString += " and model.tipoObra <> 4";
			
			if(estatusId != null)
				queryString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObraPrincipal", idObra);
			if(tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	/** OPCIONES PARA KEY: [&, |]KEY[!, ?, >, <] 
	 * !, ?, >, <.         Representan los operadores SQL <>, IN, >, < respectivamente. Ejemplo: campo! --> campo <> valor
	 * &, |.               Representan AND y OR respectivamente. Ejemplo: |campo --> OR campo = valor
	 * ejemplo combinado : |campo! --> OR campo <> valor
	 * DEFAULT: []campo[] --> campo = valor
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findByProperties(HashMap<String, Object> params, int tipoObra) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Obra model ";
		String whereString = "";
		String operador = "";
		String union = "";
		String key = "";
		
		try {
			for(Entry<String, Object> e : params.entrySet()) {
				// Determina operador
				operador = determinaOperador(e.getKey(), false);
				// Determina union
				union = determinaUnion(e.getKey());
				// Determina key
				key = determinaKey(e.getKey());
					
				if (whereString.length() > 0)
					whereString += union;
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += " date(model." + key + ") " + operador + " date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " cast(model." + key + " as string) " + operador + " '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					whereString += " cast(model." + key + " as string) " + operador + " '" + e.getValue().toString() + "'";
			}
			
			if(estatusId != null && whereString.length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			else if(estatusId != null && whereString.length() == 0)
				whereString = "model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			
			if (tipoObra != 0)
				whereString += (whereString.trim().equals("") ? " model.tipoObra = :tipoObra" : " and model.tipoObra = :tipoObra");
			else
				whereString += (whereString.trim().equals("") ? " model.tipoObra <> 4" : " and model.tipoObra <> 4");
						
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.nombre";

			Query query = entityManager.createQuery(queryString);
			if(tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	/** OPCIONES PARA KEY: [&,|]KEY[!] 
	 * sufijos           : !, vacio. Representa NOT LIKE o LIKE respectivamente
	 * prefijos          : &, |. Representan AND y OR respectivamente. Ejemplo: |campo --> OR campo LIKE valor
	 * ejemplo combinado : |campo! --> OR campo NOT LIKE valor
	 * DEFAULT: []campo[] --> campo LIKE valor
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findLikeProperties(HashMap<String, String> params, int tipoObra) throws Exception {
		String queryString = "select model from Obra model ";
		String whereString = "";
		String operador = "";
		String union = "";
		String key = "";
		
		try {
			for(Entry<String, String> e : params.entrySet()){
				// Determina operador
				operador = determinaOperador(e.getKey(), true);
				// Determina union
				union = determinaUnion(e.getKey());
				// Determina key
				key = determinaKey(e.getKey());
				
				if (whereString.length() > 0)
					whereString += union;
				if (IsNumber(e.getValue()) && e.getValue().contains("-"))
					whereString += " cast(model." + key + " as string) " + operador + " '%" + e.getValue().replace("-", "") + "%'";
				else
					whereString += " cast(model." + key + " as string) " + operador + " '%" + e.getValue() + "%'";
			}
			
			if(estatusId != null && whereString.length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			else if(estatusId != null && whereString.length() == 0)
				whereString = "model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			
			if(tipoObra != 0)
				whereString += (whereString.trim().equals("") ? " model.tipoObra = :tipoObra" : " and model.tipoObra = :tipoObra");
			else
				whereString += (whereString.trim().equals("") ? " model.tipoObra <> 4" : " and model.tipoObra <> 4");
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.nombre";

			Query query = entityManager.createQuery(queryString);
			if(tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findByMultiProperties(HashMap<String, Object> params, String unionProps, int tipoObra, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Obra model ";
		String whereString = "";
		
		try {
			if (unionProps == null || "".equals(unionProps))
				unionProps = "and";
			
			for(Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " " + unionProps;
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
			
			if(estatusId != null && whereString.length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			else if(estatusId != null && whereString.length() == 0)
				whereString = "model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			
			if(tipoObra > 0)
				whereString += (whereString.trim().equals("") ? " model.tipoObra = :tipoObra" : " and model.tipoObra = :tipoObra");
						
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			if(tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findLikeMultiProperties(HashMap<String, String> params, String unionProps, int tipoObra, int limite) throws Exception {
		String queryString = "select model from Obra model ";
		String whereString = "";
		
		try {
			if (unionProps == null || "".equals(unionProps))
				unionProps = "and";
			
			for(Entry<String, String> e : params.entrySet()){
				if (whereString.length() > 0)
					whereString += " " + unionProps;
				if (IsNumber(e.getValue()) && e.getValue().contains("-"))
					whereString += " cast(model." + e.getKey() + " as string) not like '%" + e.getValue().replace("-", "") + "%'";
				else
					whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if(estatusId != null && whereString.length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			else if(estatusId != null && whereString.length() == 0)
				whereString = "model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			
			if(tipoObra > 0)
				whereString += (whereString.trim().equals("") ? " model.tipoObra = :tipoObra" : " and model.tipoObra = :tipoObra");
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			if(tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	private boolean IsNumber(String value) {
		try { Double.parseDouble(value); }
		catch (Exception e) { return false; }
		return true;
	}
	
	private String determinaOperador(String key, boolean toLike) {
		String resultado = "";
		
		if (key.contains("!")) {
			key = key.replace("!", "");
			resultado = (toLike) ? " not like " : " <> ";
		} else if (key.contains(">")) {
			key = key.replace(">", "");
			resultado = (toLike) ? " like " : " > ";
			resultado = " > ";
		} else if (key.contains("<")) {
			key = key.replace("<", "");
			resultado = (toLike) ? " like " : " < ";
		} else if (key.contains("?")) {
			key = key.replace("?", "");
			resultado = (toLike) ? " like " : " IN (_IN_)";
		} else {
			resultado = (toLike) ? " like " : " = ";
		}
		
		return resultado;
	}
	
	private String determinaUnion(String key) {
		String resultado = "";
		
		if (key.contains("&")) {
			key = key.replace("&", "");
			resultado = " AND";
		} else if (key.contains("|")) {
			key = key.replace("|", "");
			resultado = " OR";
		} else {
			resultado = " AND";
		}
		
		return resultado;
	}
	
	private String determinaKey(String key) {
		String caracteres = "&|?!<>";
		String resultado = "";
		String[] splitted = null;
		
		splitted = key.split("");
		for (String var : splitted) {
			if (caracteres.contains(var)) continue;
			resultado += var;
		}
		
		return resultado;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findInProperty(String propertyName, List<Object> values) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Obra model ";
		String whereString = "";
		String inFilter = "";
		Object objValue = null;
		
		try {
			if (values != null && ! values.isEmpty()) {
				objValue = values.get(0);
				
				// Agrupo los valores
				for (int index = 0; index < values.size(); index++) {
					if (! "".equals(inFilter)) 
						inFilter += ",";
					
					if (objValue.getClass() == java.math.BigDecimal.class) {
						if ("".equals(whereString))
							whereString = "WHERE model." + propertyName + " IN ";
						inFilter += values.get(index).toString();
					} else if (objValue.getClass() == java.lang.Long.class) {
						if ("".equals(whereString))
							whereString = "WHERE model." + propertyName + " IN ";
						inFilter += values.get(index).toString();
					} else if (objValue.getClass() == java.lang.Integer.class) {
						if ("".equals(whereString))
							whereString = "WHERE model." + propertyName + " IN ";
						inFilter += values.get(index).toString();
					} else if (objValue.getClass() == java.util.Date.class) {
						if ("".equals(whereString))
							whereString = "WHERE date(model." + propertyName + ") IN ";
						inFilter += " date('" + formateador.format((Date) values.get(index)) + "')";
					} else {
						if ("".equals(whereString))
							whereString = "WHERE lower(cast(model." + propertyName + " as string)) IN ";
						inFilter += "'" + values.get(index).toString() + "'";
					}
				}
				
				// Genero la clausula WHERE
				whereString = whereString + "(" + inFilter + ")";
			}
			
			if(estatusId != null && whereString.length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			else if(estatusId != null && whereString.length() == 0)
				whereString = "model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			
			if (! whereString.isEmpty())
				queryString += whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-17 | Javier Tirado 	| Añado los metodos orderBy, findByProperties y findLikeProperties. Normal y extendido
 * 1.2 | 2017-01-12 | Javier Tirado 	| Implemento los metodos findByMultiProperties y findLikeMultiProperties.
 */