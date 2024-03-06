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
	
	@SuppressWarnings("unchecked")
	public List<Persona> findLikePersonaPropiedad(String propiedad, String valor, Long valor2) throws ExcepConstraint{
		String where = "";
		String and = "";
		List<Persona> list = new ArrayList<Persona>();
		try {
			
			if(valor == null || "".equals(valor)){
				where = "";
				  and = " where ";
			} else if("id".equals(propiedad)){
				   where = "where cast(per.id as string) like '%" +valor+ "%'";
				     and = " and ";
			   } else if("nombre".equals(propiedad)){
					where = "where lower(per." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
					  and = " and ";
			     } else if("rfc".equals(propiedad)){
			    	 where = "where lower(per." + propiedad + ") like '%" + valor.toLowerCase() + "%'";
			    	   and = " and ";
			     	}else
			     		return list;
			
			if(valor2 == null || "".equals(valor2))
				and = "";
			else
				and = and + " per.tipoPersona = " + valor2;

			final String queryString = "select per from Persona per " +
										where + and;
			Query query = entityManager.createQuery(queryString);
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
			String queryString = "select model from Persona model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	/*@SuppressWarnings("unchecked")
	public List<Persona> findLikePersonasConGastos(Object value, long valGpo, String tipoPersona, String tipo, String estatus, int max, Date fecha, String sucursales) {
		String where = "";
		
		try {
			String queryString ="";
			
			if("".equals(value)){//por si piden busqueda en vacio
				queryString = "select pers  " +
						   "from Persona pers " +
						   "inner join fetch pers.relacionId con "+	
						   "left join fetch pers.sucursalId suc "+
						   "left join fetch pers.catBancoId bco " +
						   "left join fetch pers.monedaId mon " +
						   "where con.grupoValorId=:valGpo " +	
						   " and lower(con.descripcion) like '%"+ tipoPersona.toLowerCase() +"%' and "+
						   "pers in (select movCta.noBeneficiario from MovimientosCuentas movCta " +
						   "inner join movCta.cuentaOrigen ctaOri "+
						   "inner join ctaOri.sucursal sucCta "+
						   "where movCta.tipo='" +tipo+ "' and movCta.estatus in ("+ estatus +") and movCta.monto > 0  ";
			} else {
				queryString = "select pers  " +
				   "from Persona pers " +
				   "inner join fetch pers.relacionId con "+
				   "left join fetch pers.sucursalId suc "+
				   "left join fetch pers.catBancoId bco " +
				   "left join fetch pers.monedaId mon " +
				   "where (lower(coalesce(pers.nombre,'') ||' '||coalesce(pers.apellidoPaterno,'')||' '||coalesce(pers.apellidoMaterno,'')) like '%"+ value.toString().toLowerCase() + "%'  or " +
				   "lower(pers.rfc) like '%"+ value.toString().toLowerCase() + "%') and "+
				   "con.grupoValorId=:valGpo " +	
				   " and lower(con.descripcion) like '%"+ tipoPersona.toLowerCase() +"%' and "+
				   "pers in (select movCta.noBeneficiario from MovimientosCuentas movCta " +
				   "inner join movCta.cuentaOrigen ctaOri "+
				   "inner join ctaOri.sucursal sucCta "+
				   "where movCta.tipo='" +tipo+ "' and movCta.estatus in ("+ estatus +") and movCta.monto > 0  ";
			}	
		
			if ("R".equals(tipo) && "'A'".equals(estatus))
				where = where + " and movCta.fecha <=:fecha)";
			else
				where = where + ")";
			
			queryString = queryString + where;
			
			Query query = entityManager.createQuery(queryString);
			if ("R".equals(tipo) && "'A'".equals(estatus))
				query.setParameter("fecha", fecha);
			query.setParameter("valGpo", valGpo);
			
			if(max>0)
				query.setMaxResults(max);
				
			return query.getResultList();
		} catch (RuntimeException re ) {
			throw re;
		}
	}*/
	
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
			
			/*String queryString = "select pers from Persona pers " +
				   "inner join fetch pers.relacionId con "+
				   "left join fetch pers.nacionalidadId nac "+
				   "left join fetch pers.coloniaId col "+
				   "left join fetch col.localidadId loc "+
				   "left join fetch pers.sucursalId suc "+
				   "left join fetch loc.catmpioId mcpio "+
				   "left join fetch mcpio.estadoId edo "+	
				   "left join fetch pers.catBancoId bco " +
				   "left join fetch pers.monedaId mon " +
			   "where (lower(coalesce(pers.nombre,'') ||' '||coalesce(pers.apellidoPaterno,'')||' '||coalesce(pers.apellidoMaterno,'')) like  '%"+ value.toString().toLowerCase() +"%' or "+
		   		   "lower(pers.rfc) like  '%"+ value.toString().toLowerCase() +"%') and "+
		   		   "con.grupoValorId=:valGpo " +	
		   		   " and lower(con.descripcion) like '%"+ tipoPersona.toLowerCase() +"%' and "+
		   		   "pers in (select persGtos.personaId from TygpersonasGastos persGtos where persGtos.gastoId="+tipoGastoAsociado+")";*/
			
			Query query = entityManager.createQuery(queryString);
			//query.setParameter("valGpo", valGpo);
			
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
			String queryString = "select pers from Persona pers ";
			/*String queryString = "select pers  " +
					   "from Persona pers " +
					   "inner join fetch pers.relacionId con "+
					   "left join fetch pers.nacionalidadId nac "+
					   "left join fetch pers.coloniaId col "+
					   "left join fetch col.localidadId loc "+
					   "left join fetch pers.sucursalId suc "+
					   "left join fetch loc.catmpioId mcpio "+
					   "left join fetch pers.catBancoId bco " +
					   "left join fetch pers.monedaId mon " +
					   "left join fetch mcpio.estadoId edo ";*/
									   
		   if(! "nombre".equals(propertyName))
			   v_where= "where lower(pers."+ propertyName + ") like '%"+ value.toString().toLowerCase() +"%' ";
		   else
			   v_where= "where (lower(coalesce(pers.nombre,'') ||' '||coalesce(pers.primerApellido,'')||' '||coalesce(pers.segundoApellido,'')) like  '%"+ value.toString().toLowerCase() +"%' or "+
			   			"lower(pers.rfc) like  '%"+ value.toString().toLowerCase() +"%') ";
			
			/*if("".equals(value)){
				queryString = queryString + "where con.grupoValorId=:propertyValue and lower(con.descripcion) like '%"+ tipoPersona.toLowerCase() +"%' ";
			} else {
				  //queryString = queryString + v_where +" and con.grupoValorId="+valGpo.getGrupoValorId() +" and lower(con.descripcion) like '%"+ tipoPersona.toLowerCase() +"%' ";	
				queryString = queryString + v_where +" and con.grupoValorId=:propertyValue and lower(con.descripcion) like '%"+ tipoPersona.toLowerCase() +"%' ";
			}*/
		   
		   queryString = queryString + v_where;
			
			/*if(valido){
				queryString = queryString + " and pers.validado = true";
			}*/
						 			   
			Query query = entityManager.createQuery(queryString);
			//query.setParameter("propertyValue", valGpo);	
			
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
				"where lower(coalesce(pers.nombre,'') ||' '||coalesce(pers.primerApellido,'')||' '||coalesce(pers.segundoApellido,'')) like '%"+ value.toString().toLowerCase() + "%'";

			Query query = entityManager.createQuery(queryString);
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
		} catch (RuntimeException re) {
			throw re;
		}/*String queryString = "select model from Persona model ";
		String whereString = "";
		
		try {
			if (value != null) {
				whereString = " where model."+ propertyName + " = :propertyValue";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}*/
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
		} catch (RuntimeException re) {
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