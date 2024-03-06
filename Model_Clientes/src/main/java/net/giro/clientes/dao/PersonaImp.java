package net.giro.clientes.dao;

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

import net.giro.clientes.beans.Persona;

@Stateless
public class PersonaImp extends DAOImpl<Persona> implements PersonaDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Persona entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Persona> saveOrUpdateList(List<Persona> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Persona findByRfc(String rfc) throws Exception {
		String queryString = "select model from Persona model where model.rfc = :rfc order by model.id desc";
		List<Persona> personas = null;
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("rfc", rfc);
			personas = (List<Persona>) query.getResultList();
			if (personas != null && ! personas.isEmpty())
				return personas.get(0);
			return null;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Persona> findAll(boolean incluyeEliminados, boolean incluyeSistema, String orderBy) {
		String queryString = "select model from Persona model where model.estatus in (:estatus) and model.sistema in (:sistema) ";
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.nombre";
			queryString = queryString.replace(":estatus", (incluyeEliminados ? "0,1" : "0"));
			queryString = queryString.replace(":sistema", (incluyeSistema ? "0,1" : "0"));
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Persona> findByProperty(String propertyName, Object value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite) {
		String queryString = "select model from Persona model where model.estatus in (:estatus) and model.sistema in (:sistema) ";
		
		try {
			if (propertyName == null || "".equals(propertyName))
				propertyName = "nombre";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.nombre";
			if (value != null) 
				queryString += "and model." + propertyName + " = :propertyValue ";
			queryString = queryString.replace(":estatus", (incluyeEliminados ? "0,1" : "0"));
			queryString = queryString.replace(":sistema", (incluyeSistema ? "0,1" : "0"));
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
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
	public List<Persona> findLike(String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite) {
		String queryString = "select model from Persona model where model.estatus in (:estatus) and model.sistema in (:sistema) ";
		StringBuffer sb = null;
		
		try {
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.nombre";
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue ";
				queryString += "or lower(trim(model.nombre)) like :propertyValue ";
				queryString += "or lower(trim(model.rfc)) like :propertyValue ";
				queryString += "or lower(trim(model.correo)) like :propertyValue) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
	    		sb.append("%");
			}
			
			queryString = queryString.replace(":estatus", (incluyeEliminados ? "0,1" : "0"));
			queryString = queryString.replace(":sistema", (incluyeSistema ? "0,1" : "0"));
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.trim()))
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
	public List<Persona> findLikeProperty(String propertyName, String value, boolean incluyeEliminados, boolean incluyeSistema, String orderBy, int limite) {
		String queryString = "select model from Persona model where model.estatus in (:estatus) and model.sistema in (:sistema) ";
		StringBuffer sb = null;
		
		try {
			if (propertyName == null || "".equals(propertyName))
				propertyName = "nombre";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.nombre";
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
	    		sb.append("%");
			}
			
			queryString = queryString.replace(":estatus", (incluyeEliminados ? "0,1" : "0"));
			queryString = queryString.replace(":sistema", (incluyeSistema ? "0,1" : "0"));
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.trim()))
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
	public List<Persona> findByProperties(HashMap<String, Object> params) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "";
		String whereString = "";
		
		try {
			queryString = "select model from Persona model ";
			for (Entry<String, Object> e : params.entrySet()) {
				if (! "".equals(whereString))
					whereString += " and ";
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
			
			if (! "".equals(whereString))
				queryString += " where " + whereString;
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	/*private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Persona entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Persona> saveOrUpdateList(List<Persona> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<Persona> findLikePersonaPropiedad(String propiedad, String valor, Long valor2) throws Exception {
		//String and = "";
		//List<Persona> list = new ArrayList<Persona>();
		String queryString = "select model from Persona model ";
		String where = "";
		
		try {
			if (valor == null)
				valor = "";
			//if (valor != null && ! "".equals(valor)) {
				if ("id".equals(propiedad)) 
					where += "where cast(model.id as string) like '%" + valor + "%' ";
				else if ("nombre".equals(propiedad)) 
					where += "where lower(model." + propiedad + ") like '%" + valor.toLowerCase() + "%' ";
				else if ("rfc".equals(propiedad)) 
					where += "where lower(model." + propiedad + ") like '%" + valor.toLowerCase() + "%' ";
				else
					return new ArrayList<Persona>();
			//} 

			if (valor2 != null && valor2 > 0L)
				where += " and model.tipoPersona = " + valor2;

			queryString += where;
			Query query = entityManager.createQuery(queryString);
			if ("".equals(valor))
				query.setMaxResults(1000);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Persona> findLikeId(String id) throws Exception{
		try {
			String queryString = "select per from Persona per ";
			String where = " where cast(per.id as string) like '%" + id + "%'";

			queryString += where;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Long> getIdListLikeNombre(String nombre) throws Exception {
		try{
			String queryString = "select per from Persona per ";
			String where = "where lower(trim(trim(trim(per.primerNombre || ' ' || per.segundoNombre) || ' ' || per.primerApellido) || ' ' || per.segundoApellido)) " +
							"like lower('%' || :nombre || '%')";
			
			queryString += where;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("nombre", nombre);
			
			List<Persona> listPersonas = query.getResultList();
			
			int id = 1;
			HashMap<String, Long> resultado = new HashMap<String, Long>();
			for(Persona pojoPersona : listPersonas){
				resultado.put("valor" + id, pojoPersona.getId());
				id++;
			}
			
			return resultado;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Persona> findAll() {
		try {
			String queryString = "select model from Persona model ";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Persona> findLikeProveedor(Object value, long valGpo, String tipoPersona, Long tipoGastoAsociado, int max) {
		try {
			if(value == null)
				value="";
			
			String queryString = "select pers from Persona pers " +
				   "left join fetch pers.nacionalidad nac "+
				   "left join fetch pers.colonia col "+
				   "left join fetch col.localidad loc "+	
				   "left join fetch pers.banco bco " +
			   "where (lower(coalesce(pers.nombre,'') ||' '||coalesce(pers.apellidoPaterno,'')||' '||coalesce(pers.apellidoMaterno,'')) like  '%"+ value.toString().toLowerCase() +"%' or "+
		   		   "lower(pers.rfc) like  '%"+ value.toString().toLowerCase() +"%')";
			
			Query query = entityManager.createQuery(queryString);
			if(max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception re ) {
		
			throw re;
		}
	} 
	
	@SuppressWarnings("unchecked")
	public List<Persona> findLikeClaveNombre(String propertyName, Object value, long valGpo, String tipoPersona, int max, Boolean valido) {
		try {
			if(value == null)
				value="";
			
			String v_where="";
			String queryString = "select pers from Persona pers ";
									   
		   if(! "nombre".equals(propertyName))
			   v_where= "where lower(pers."+ propertyName + ") like '%"+ value.toString().toLowerCase() +"%' ";
		   else
			   v_where= "where (lower(coalesce(pers.nombre,'') ||' '||coalesce(pers.primerApellido,'')||' '||coalesce(pers.segundoApellido,'')) like  '%"+ value.toString().toLowerCase() +"%' or "+
			   			"lower(pers.rfc) like  '%"+ value.toString().toLowerCase() +"%') ";
			
		   queryString = queryString + v_where;
					 			   
			Query query = entityManager.createQuery(queryString);
			if(max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception re ) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Persona> findLikePersonas(Object value, int max) {
		try {
			String queryString = "select pers from Persona pers " +
				"where lower(coalesce(pers.nombre,'') ||' '||coalesce(pers.primerApellido,'')||' '||coalesce(pers.segundoApellido,'')) like '%"+ value.toString().toLowerCase() + "%'";

			Query query = entityManager.createQuery(queryString);
			if(max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception re ) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Persona> findByProperty(String propertyName, Object value, int limite) {
		String queryString = "select model from Persona model ";
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
	public List<Persona> findLikeProperty(String propertyName, String value, int limite) {
		StringBuffer sb = null;
		String queryString = "select model from Persona model ";
		String whereString = "";
		String orderBy = "";
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					whereString = "where cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					whereString = "where lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
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
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}*/
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-09 | Javier Tirado 	| Añado los metodos findByProperty y findLikeProperty
 * 2.2 | 2017-05-18 | Javier Tirado 	| Implemento el metodo findByProperties
 */