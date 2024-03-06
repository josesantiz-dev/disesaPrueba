package net.giro.rh.admon.dao;

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

import org.apache.commons.lang.StringUtils;

import net.giro.rh.admon.beans.Empleado;

@Stateless
public class EmpleadoImp extends DAOImpl<Empleado> implements EmpleadoDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Empleado entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Empleado> saveOrUpdateList(List<Empleado> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findAll(boolean incluyeBajas, boolean soloSistema, long idEmpresa, String orderBy) {
		String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			queryString += "and model.sistema in (" + (soloSistema ? "1" : "0") + ") ";
			queryString += "and model.estatus in (0" + (incluyeBajas ? ",1,2" : "") + ") ";
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.nombre";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findAll(List<Long> empleados, boolean incluyeBajas, boolean soloSistema, String orderBy) {
		String queryString = "select model from Empleado model where model.id in (:lista) ";
		String lista = "";
		
		try {
			empleados = (empleados != null && ! empleados.isEmpty() ? empleados : new ArrayList<Long>());
			lista = (! empleados.isEmpty() ?  StringUtils.join(empleados, ",") : "0");
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombre, model.id desc");
			
			queryString = queryString.replace(":lista", lista);
			queryString += "and model.sistema in (" + (soloSistema ? "1" : "0") + ") ";
			queryString += "and model.estatus in (0" + (incluyeBajas ? ",1,2" : "") + ") ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findLike(String value, boolean incluyeBajas, boolean soloSistema, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa "; 
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.primerApellido, model.segundoApellido, model.primerNombre, model.segundoNombre");
			value = (value != null && ! "".equals(value.trim()) ? value.trim() : "");
			value = value.replace(" ", "%");
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue ";
				queryString += "or cast(model.idPersona as string) like :propertyValue ";
				queryString += "or lower(trim(model.clave)) like :propertyValue ";
				queryString += "or lower(trim(model.nombre)) like :propertyValue ";
				queryString += "or lower(trim(model.primerNombre)) like :propertyValue ";
				queryString += "or lower(trim(model.segundoNombre)) like :propertyValue ";
				queryString += "or lower(trim(model.nombresPropios)) like :propertyValue ";
				queryString += "or lower(trim(model.primerApellido)) like :propertyValue ";
				queryString += "or lower(trim(model.segundoApellido)) like :propertyValue ";
				queryString += "or lower(trim(model.areaDescripcion)) like :propertyValue ";
				queryString += "or lower(trim(model.puestoDescripcion)) like :propertyValue ";
				queryString += "or lower(trim(model.categoriaDescripcion)) like :propertyValue ";
				queryString += "or lower(trim(model.nombreSucursal)) like :propertyValue ";
				queryString += "or lower(trim(model.numeroSeguridadSocial)) like :propertyValue ";
				queryString += "or lower(trim(model.email)) like :propertyValue) ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}

			queryString += "and model.estatus in (0" + (incluyeBajas ? ",1,2" : "") + ") ";
			queryString += "and model.sistema in (" + (soloSistema ? "1" : "0") + ") ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
	public List<Empleado> findLikeProperty(String propertyName, Object value, boolean incluyeBajas, boolean soloSistema, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.primerApellido, model.segundoApellido, model.primerNombre, model.segundoNombre");
			value = (value != null ? value : "");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fechaIngreso) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString())) {
					queryString += "and lower(trim(cast(model."+ propertyName + " as string))) like :propertyValue ";
					if (value.toString().contains(" "))
						value = value.toString().replace(" ", "%");
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().toLowerCase());
		    		sb.append("%");
				}
			}

			queryString += "and model.estatus in (0" + (incluyeBajas ? ",1,2" : "") + ") ";
			queryString += "and model.sistema in (" + (soloSistema ? "1" : "0") + ") ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
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
	public List<Empleado> findByProperty(String propertyName, Object value, boolean incluyeBajas, boolean soloSistema, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.primerApellido, model.segundoApellido, model.primerNombre, model.segundoNombre");
			value = (value != null ? value : "");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString())) {
					queryString += "and model." + propertyName + " = :propertyValue ";
				}
			}

			queryString += "and model.estatus in (0" + (incluyeBajas ? ",1,2" : "") + ") ";
			queryString += "and model.sistema in (" + (soloSistema ? "1" : "0") + ") ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.toString()))
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
	public List<Empleado> findEmpleadoRepetido(long idPersona, long idEmpresa) {
		try {
			idPersona = (idPersona > 0L ? idPersona : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			final String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa and model.sistema = 0 and model.idPersona = "+ idPersona + " order by model.id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findEmpleadosAlmacenes(List<Long> puestosPermitidos, long idEmpresa) {
		String queryString = "select model from Empleado model, PuestoCategoria x where model.idEmpresa = :idEmpresa and model.sistema = 0 and model.estatus = 0 and model.idPuestoCategoria = x.id and x.idPuesto.id in (:lista) ";
		String lista = "";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			puestosPermitidos = (puestosPermitidos != null && ! puestosPermitidos.isEmpty() ? puestosPermitidos : new ArrayList<Long>());
			lista = (! puestosPermitidos.isEmpty() ?  StringUtils.join(puestosPermitidos, ",") : "0");
			
			queryString = queryString.replace(":lista", lista);
			queryString += "order by model.nombre ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findByProperties(HashMap<String, Object> params, long idEmpresa) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "";
		String whereString = "";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa and model.sistema = 0 and model.estatus = 0 ";
			if (! params.containsKey("estatus"))
				queryString += "and estatus = 0 ";
			
			for (Entry<String, Object> e : params.entrySet()) {
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += "and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += "and lower(cast(model." + e.getKey() + " as string)) = lower('" + ((BigDecimal) e.getValue()).toString() + "') ";
				else
					whereString += "and lower(cast(model." + e.getKey() + " as string)) = lower('" + e.getValue().toString() + "') ";
			}
			
			queryString += ((! params.containsKey("estatus")) ? "estatus = 0 and " : "") + whereString;
			queryString += " order by model.primerApellido, model.segundoApellido, model.primerNombre, model.segundoNombre";

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public String generaClaveEmpleado(String categoria) {
		String queryString = "";
		List<Empleado> empleadoList = null;
		// --------------------------------
		String clave = "";
		String tmpClave = "";
		int clavetmp = 0;
		int cerosFaltantes = 0;
				
		try {
			if (categoria.equals("OPERACIONES")) {
				queryString = "select model from Empleado model where model.clave like 'DA-%' order by model.fechaCreacion desc limit 1";
				tmpClave = "DA-";
			} else {
				queryString = "select model from Empleado model where model.clave like 'DO-%' order by model.fechaCreacion desc limit 1";
				tmpClave = "DO-";
			}
			
			Query query = entityManager.createQuery(queryString);
			empleadoList = query.getResultList();
			if (empleadoList != null && ! empleadoList.isEmpty()) {
				clave = empleadoList.get(0).getClave();
				clave = clave.substring(3);
				clavetmp = Integer.parseInt(clave);
			}
			clavetmp += 1;
			clave = String.valueOf(clavetmp);
			cerosFaltantes = (4-clave.length());
			for (int x = 0; x < cerosFaltantes; x++)
				clave = "0" + clave ;
			clave = tmpClave + clave;
		} catch (Exception re) {
			throw re;
		}
		
		return clave;
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Implemento los metodos findByProperties y findLikeProperties
 */