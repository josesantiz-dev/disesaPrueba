package net.giro.clientes.dao;

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

import net.giro.DAOImpl;
import net.giro.clientes.beans.PersonaNegocioVista;

@Stateless
public class PersonaNegocioVistaImp extends DAOImpl<PersonaNegocioVista> implements PersonaNegocioVistaDAO {
	@PersistenceContext
	private EntityManager PersonaNegocioVistaManager;

	@Override
	public PersonaNegocioVista findById(Long id, String tipo) {
		String queryString = "select model from PersonaNegocioVista model where model.id = :id and tipo = :tipo ";
		
		try {
			if (id == null || id <= 0L || tipo == null || "".equals(tipo.trim()))
				return null;
			
			Query query = this.PersonaNegocioVistaManager.createQuery(queryString);
			query.setParameter("id", id);
			query.setParameter("tipo", tipo);
			return (PersonaNegocioVista) query.getSingleResult();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PersonaNegocioVista> busquedaDinamica(String value) throws Exception {
		String queryString = "select model from PersonaNegocioVista model ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.trim())) {
				queryString += "where (lower(trim(model.nombre)) like :propertyValue "
						+ "or lower(trim(model.rfc)) like :propertyValue "
						+ "or lower(trim(model.telefono)) like :propertyValue "
						+ "or lower(trim(model.direccion)) like :propertyValue "
						+ "or lower(trim(model.domicilio)) like :propertyValue "
						+ "or lower(trim(model.noExterno)) like :propertyValue "
						+ "or lower(trim(model.noInterno)) like :propertyValue "
						+ "or lower(trim(model.colonia)) like :propertyValue "
						+ "or lower(trim(model.localidad)) like :propertyValue "
						+ "or lower(trim(model.municipio)) like :propertyValue "
						+ "or lower(trim(model.estado)) like :propertyValue "
						+ "or lower(trim(model.pais)) like :propertyValue "
						+ "or lower(trim(model.cp)) like :propertyValue "
						+ "or cast(model.id as string) like :propertyValue) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
		
			queryString += "order by model.nombre, model.id desc";
			
			Query query = this.PersonaNegocioVistaManager.createQuery(queryString);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PersonaNegocioVista> findLike(String value, String tipo) throws Exception {
		String queryString = "select model from PersonaNegocioVista model ";
		StringBuffer sb = null;
		
		try {
			if (tipo == null || "".equals(tipo.trim()))
				queryString += "where coalesce(trim(model.tipo),'') <> '' ";
			else
				queryString += "where coalesce(trim(model.tipo),'') = '" + tipo + "' ";
				
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (lower(trim(model.nombre)) like :propertyValue "
						+ "or lower(trim(model.primerNombre)) like :propertyValue "
						+ "or lower(trim(model.segundoNombre)) like :propertyValue "
						+ "or lower(trim(model.primerApellido)) like :propertyValue "
						+ "or lower(trim(model.segundoApellido)) like :propertyValue "
						+ "or lower(trim(model.rfc)) like :propertyValue "
						+ "or lower(trim(model.telefono)) like :propertyValue "
						+ "or lower(trim(model.direccion)) like :propertyValue "
						+ "or lower(trim(model.domicilio)) like :propertyValue "
						+ "or lower(trim(model.noExterno)) like :propertyValue "
						+ "or lower(trim(model.noInterno)) like :propertyValue "
						+ "or lower(trim(model.colonia)) like :propertyValue "
						+ "or lower(trim(model.localidad)) like :propertyValue "
						+ "or lower(trim(model.municipio)) like :propertyValue "
						+ "or lower(trim(model.estado)) like :propertyValue "
						+ "or lower(trim(model.pais)) like :propertyValue "
						+ "or lower(trim(model.cp)) like :propertyValue "
						+ "or cast(model.id as string) like :propertyValue) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
		
			queryString += " order by model.nombre, model.id desc";
			
			Query query = this.PersonaNegocioVistaManager.createQuery(queryString);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PersonaNegocioVista> findAll(Long estatus, int limite) throws Exception {
		String queryString = "select model from PersonaNegocioVista model ";
		
		try {
			if (estatus != null)
				queryString += "where model.estatus = " + estatus;
			
			Query query = this.PersonaNegocioVistaManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PersonaNegocioVista> findByProperty(String propertyName, Object value, Long estatus, int limite) throws RuntimeException {
		String queryString = "select model from PersonaNegocioVista model ";
		String whereString = "";
		
		try {
			if (value != null)
				whereString += " where model."+ propertyName + " = :propertyValue";
			
			if (estatus != null && whereString.length() > 0)
				whereString += " and estatus = " + estatus;
			else if (estatus != null && whereString.length() == 0)
				whereString = " where estatus = " + estatus;
			estatus = null;
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			queryString += " order by model." + propertyName;
			
			Query query = this.PersonaNegocioVistaManager.createQuery(queryString);
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
	public List<PersonaNegocioVista> findByProperties(HashMap<String, Object> params, Long estatus, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from PersonaNegocioVista model ";
		String whereString = "";
		String orderBy = "";
		
		try {
			for (Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0) {
					whereString += " and";
					orderBy += ",";
				}
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
				orderBy += "model." + e.getKey();
			}
			
			if (estatus != null && whereString.length() > 0)
				whereString += " and estatus = " + estatus;
			else if (estatus != null && whereString.length() == 0)
				whereString = " estatus = " + estatus;
			estatus = null;
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			if (! "".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = this.PersonaNegocioVistaManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PersonaNegocioVista> findLikeProperty(String propertyName, Object value, Long estatus, int limite) throws Exception {
		String queryString = "select model from PersonaNegocioVista model ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if (value != null && !"".equals(value.toString())) {
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
			
			if (estatus != null && whereString.length() > 0)
				whereString += " and estatus = " + estatus;
			else if (estatus != null && whereString.length() == 0)
				whereString = " where estatus = " + estatus;
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;			
			queryString += " order by model." + propertyName;
			
			Query query = this.PersonaNegocioVistaManager.createQuery(queryString);
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
	public List<PersonaNegocioVista> findLikeProperties(HashMap<String, String> params, Long estatus, int limite) throws Exception {
		String queryString = "select model from PersonaNegocioVista model ";
		String whereString = "";
		String orderBy = "";
		
		try {
			for (Entry<String, String> e : params.entrySet()) {
				if (whereString.length() > 0) {
					whereString += " and";
					orderBy += ",";
				}
				
				whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
				orderBy += "model." + e.getKey();
			}
			
			if (estatus != null && whereString.length() > 0)
				whereString += " and model.estatus = " + estatus;
			else if (estatus != null && whereString.length() == 0)
				whereString = " model.estatus = " + estatus;
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;
			
			if (! "".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = this.PersonaNegocioVistaManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PersonaNegocioVista> findInProperty(String columnName, List<Object> values, Long estatus, int limite) throws Exception {
		String queryString = "select model from PersonaNegocioVista model ";
		String whereString = "";
    	String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()){
    			whereString = " WHERE cast(model." + columnName + " as string) IN (";
    			
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
    			whereString = whereString + inFilter + ")";
        	}
			
			if (estatus != null && whereString.length() > 0)
				whereString += " and estatus = " + estatus;
			else if (estatus != null && whereString.length() == 0)
				whereString = " where estatus = " + estatus;
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			queryString += " order by model." + columnName;
        	
        	Query query = this.PersonaNegocioVistaManager.createQuery(queryString);
        	if (values != null && ! values.isEmpty()) {
        		for (int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
        	
			if (limite > 0)
				query.setMaxResults(limite);
        	
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 24/04/2017 | Javier Tirado	| Creacion de PersonaNegocioVistaImp