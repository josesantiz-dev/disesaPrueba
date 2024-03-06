package net.giro.clientes.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import net.giro.comun.ExcepConstraint;

@Stateless
public class PersonaImp extends DAOImpl<Persona> implements PersonaDAO  {
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
	public long save(Persona entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Persona> saveOrUpdateList(List<Persona> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<Persona> findLikePersonaPropiedad(String propiedad, String valor, Long valor2) throws ExcepConstraint{
		String where = "";
		String and = "";
		List<Persona> list = new ArrayList<Persona>();
		
		try {
			if(valor != null && ! "".equals(valor)) {
				if("id".equals(propiedad)){
					where = "and cast(per.id as string) like '%" +valor+ "%' ";
					and = " and ";
				} else if("nombre".equals(propiedad)){
					where = "and lower(per." + propiedad + ") like '%" + valor.toLowerCase() + "%' ";
					and = " and ";
				} else if("rfc".equals(propiedad)){
					where = "and lower(per." + propiedad + ") like '%" + valor.toLowerCase() + "%' ";
					and = " and ";
				} else
					return list;
			} 

			if(valor2 == null || "".equals(valor2))
				and = "";
			else
				and = and + " per.tipoPersona = " + valor2;

			final String queryString = "select per from Persona per where model.idEmpresa = :idEmpresa " + where + and;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Persona> findLikeId(String id) throws Exception{
		try {
			String queryString = "select per from Persona per ";
			String where = " where model.idEmpresa = :idEmpresa and cast(per.id as string) like '%" + id + "%'";

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
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Long> getIdListLikeNombre(String nombre) throws Exception {
		try{
			String queryString = "select per from Persona per ";
			String where = "where model.idEmpresa = :idEmpresa and lower(trim(trim(trim(per.primerNombre || ' ' || per.segundoNombre) || ' ' || per.primerApellido) || ' ' || per.segundoApellido)) " +
							"like lower('%' || :nombre || '%')";
			
			queryString += where;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
			String queryString = "select model from Persona model where model.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
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
			   "where model.idEmpresa = :idEmpresa and (lower(coalesce(pers.nombre,'') ||' '||coalesce(pers.apellidoPaterno,'')||' '||coalesce(pers.apellidoMaterno,'')) like  '%"+ value.toString().toLowerCase() +"%' or "+
		   		   "lower(pers.rfc) like  '%"+ value.toString().toLowerCase() +"%')";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if(max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re ) {
		
			throw re;
		}
	} 
	
	@SuppressWarnings("unchecked")
	public List<Persona> findLikeClaveNombre(String propertyName, Object value, long valGpo, String tipoPersona, int max, Boolean valido) {
		try {
			if(value == null)
				value="";
			
			String v_where="";
			String queryString = "select pers from Persona pers where model.idEmpresa = :idEmpresa ";
									   
		   if(! "nombre".equals(propertyName))
			   v_where= "and lower(pers."+ propertyName + ") like '%"+ value.toString().toLowerCase() +"%' ";
		   else
			   v_where= "and (lower(coalesce(pers.nombre,'') ||' '||coalesce(pers.primerApellido,'')||' '||coalesce(pers.segundoApellido,'')) like  '%"+ value.toString().toLowerCase() +"%' or "+
			   			"lower(pers.rfc) like  '%"+ value.toString().toLowerCase() +"%') ";
			
		   queryString = queryString + v_where;
					 			   
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if(max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re ) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Persona> findLikePersonas(Object value, int max) {
		try {
			String queryString = "select pers from Persona pers " +
				"where model.idEmpresa = :idEmpresa and lower(coalesce(pers.nombre,'') ||' '||coalesce(pers.primerApellido,'')||' '||coalesce(pers.segundoApellido,'')) like '%"+ value.toString().toLowerCase() + "%'";

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if(max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re ) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Persona> findByProperty(String propertyName, Object value, int limite) {
		String queryString = "select model from Persona model where model.idEmpresa = :idEmpresa ";
		String orderBy = "";
		
		try {
			if (propertyName == null || "".equals(propertyName))
				propertyName = "nombre";
			
			orderBy = "order by model.id desc";
			if (value != null) {
				queryString += "and model." + propertyName + " = :propertyValue ";
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
	public List<Persona> findLikeProperty(String propertyName, String value, int limite) {
		StringBuffer sb = null;
		String queryString = "select model from Persona model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		String orderBy = "";
		
		try {
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					whereString = "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					whereString = "and lower(model."+ propertyName + ") LIKE :propertyValue ";
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
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && !"".equals(value.toString()))
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
	public List<Persona> findByProperties(HashMap<String, Object> params) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "";
		
		try {
			queryString = "select model from Persona model where model.idEmpresa = :idEmpresa and ";
			for(Entry<String, Object> e : params.entrySet()) {
				if (e.getValue().getClass() == java.util.Date.class) 
					queryString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					queryString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					queryString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
 * 1.2 | 2016-11-09 | Javier Tirado 	| Añado los metodos findByProperty y findLikeProperty
 * 2.2 | 2017-05-18 | Javier Tirado 	| Implemento el metodo findByProperties
 */