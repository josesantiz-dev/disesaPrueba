package net.giro.rh.admon.dao;

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

import net.giro.rh.admon.beans.Empleado;

@Stateless
public class EmpleadoImp extends DAOImpl<Empleado> implements EmpleadoDAO  {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Empleado entity, Long idEmpresa) throws Exception {
		try {
			return super.save(entity, idEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Empleado> saveOrUpdateList(List<Empleado> entities, Long idEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, idEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findAll(Long idEmpresa) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa order by model.nombre ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findAllActivos(Long idEmpresa) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa and model.estatus = 0 order by model.nombre ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findBy(Object value, String orderBy, int limite, Long idEmpresa) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Empleado model where estatus = 0 and model.idEmpresa = :idEmpresa "; 
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "primerApellido, segundoApellido, primerNombre, segundoNombre";
			
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and (date(model.fechaIngreso) = date('" + formateador.format((Date) value) + "') ";
					queryString += "or date(model.fechaAltaSeguroSocial) = date('" + formateador.format((Date) value) + "') ";
					queryString += "or date(model.fechaCreacion) = date('" + formateador.format((Date) value) + "') ";
					queryString += "or date(model.fechaModificacion) = date('" + formateador.format((Date) value) + "')) ";
				} else if (value.getClass() == java.lang.Long.class) {
					queryString += "and (cast(model.id as string) = '" + value.toString() + "' ";
					queryString += "or cast(model.idPersona as string) = '" + value.toString() + "' ";
					queryString += "or lower(model.clave) = lower('" + value.toString() + "') ";
					queryString += "or cast(model.homonimo as string) = '" + value.toString() + "' ";
					queryString += "or cast(model.idEmpresa as string) = '" + value.toString() + "' ";
					queryString += "or cast(model.idArea as string) = '" + value.toString() + "' ";
					queryString += "or cast(model.idPuestoCategoria as string) = '" + value.toString() + "' ";
					queryString += "or cast(model.idSucursal as string) = '" + value.toString() + "' ";
					queryString += "or cast(model.externo as string) = '" + value.toString() + "' ";
					queryString += "or cast(model.altaSeguroSocial as string) = '" + value.toString() + "') ";
				} else {
					queryString += "and (cast(model.id as string) = '" + value.toString() + "' ";
					queryString += "or cast(model.idPersona as string) = '" + value.toString() + "' ";
					queryString += "or lower(model.clave) = lower('" + value.toString() + "') ";
					queryString += "or lower(model.nombre) = lower('" + value.toString() + "') ";
					queryString += "or lower(model.primerNombre) = lower('" + value.toString() + "') ";
					queryString += "or lower(model.segundoNombre) = lower('" + value.toString() + "') ";
					queryString += "or lower(model.nombresPropios) = lower('" + value.toString() + "') ";
					queryString += "or lower(model.primerApellido) = lower('" + value.toString() + "') ";
					queryString += "or lower(model.segundoApellido) = lower('" + value.toString() + "') ";
					queryString += "or cast(model.homonimo as string) = '" + value.toString() + "' ";
					queryString += "or cast(model.idEmpresa as string) = '" + value.toString() + "' ";
					queryString += "or cast(model.idArea as string) = '" + value.toString() + "' ";
					queryString += "or lower(model.areaDescripcion) = lower('" + value.toString() + "') ";
					queryString += "or cast(model.idPuestoCategoria as string) = '" + value.toString() + "' ";
					queryString += "or lower(model.puestoDescripcion) = lower('" + value.toString() + "') ";
					queryString += "or lower(model.categoriaDescripcion) = lower('" + value.toString() + "') ";
					queryString += "or cast(model.idSucursal as string) = '" + value.toString() + "' ";
					queryString += "or lower(model.nombreSucursal) = lower('" + value.toString() + "') ";
					queryString += "or lower(model.numeroSeguridadSocial) = lower('" + value.toString() + "') ";
					queryString += "or lower(model.nombreCasoEmergencia) = lower('" + value.toString() + "') ";
					queryString += "or lower(model.email) = lower('" + value.toString() + "') ";
					queryString += "or cast(model.altaSeguroSocial as string) = '" + value.toString() + "') ";
				}
			}
			
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findByProperty(String propertyName, final Object value, Long idEmpresa) {
		String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			queryString += "and estatus = 0 and model." + propertyName + "= :propertyValue order by model.primerApellido, model.segundoApellido, model.primerNombre, model.segundoNombre";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findByProperties(HashMap<String, Object> params, Long idEmpresa) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "";
		String whereString = "";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa ";
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
	public List<Empleado> findLike(String value, String orderBy, int limite, Long idEmpresa) throws Exception {
		String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa and estatus = 0 "; 
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "coalesce(trim(model.primerApellido),''), coalesce(trim(model.segundoApellido),''), coalesce(trim(model.primerNombre),''), coalesce(trim(model.segundoNombre),'')";
			
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like '%" + value.trim() + "%' ";
				queryString += "or cast(model.idPersona as string) like '%" + value.trim() + "%' ";
				queryString += "or lower(model.clave) like lower('%" + value.trim() + "%') ";
				queryString += "or lower(model.nombre) like lower('%" + value.trim() + "%') ";
				queryString += "or lower(model.primerNombre) like lower('%" + value.trim() + "%') ";
				queryString += "or lower(model.segundoNombre) like lower('%" + value.trim() + "%') ";
				queryString += "or lower(model.nombresPropios) like lower('%" + value.trim() + "%') ";
				queryString += "or lower(model.primerApellido) like lower('%" + value.trim() + "%') ";
				queryString += "or lower(model.segundoApellido) like lower('%" + value.trim() + "%') ";
				queryString += "or cast(model.homonimo as string) like '%" + value.trim() + "%' ";
				queryString += "or cast(model.idEmpresa as string) like '%" + value.trim() + "%' ";
				queryString += "or cast(model.idArea as string) like '%" + value.trim() + "%' ";
				queryString += "or lower(model.areaDescripcion) like lower('%" + value.trim() + "%') ";
				queryString += "or cast(model.idPuestoCategoria as string) like '%" + value.trim() + "%' ";
				queryString += "or lower(model.puestoDescripcion) like lower('%" + value.trim() + "%') ";
				queryString += "or lower(model.categoriaDescripcion) like lower('%" + value.trim() + "%') ";
				queryString += "or cast(model.idSucursal as string) like '%" + value.trim() + "%' ";
				queryString += "or lower(model.nombreSucursal) like lower('%" + value.trim() + "%') ";
				queryString += "or lower(model.numeroSeguridadSocial) like lower('%" + value.trim() + "%') ";
				queryString += "or lower(model.nombreCasoEmergencia) like lower('%" + value.trim() + "%') ";
				queryString += "or lower(model.email) like lower('%" + value.trim() + "%') ";
				queryString += "or cast(model.altaSeguroSocial as string) like '%" + value.trim() + "%') ";
			}
			
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findLikeProperty(String propertyName, Object value, int limite, Long idEmpresa) {
		String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa and estatus = 0 ";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			if (value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					queryString = queryString + "and lower(model."+ propertyName + ") LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			queryString += "order by model.primerApellido, model.segundoApellido, model.primerNombre, model.segundoNombre";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findLikeProperties(HashMap<String, String> params, Long idEmpresa) throws Exception{
		String queryString = "";
		String whereString = "";
		
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa ";
			for(Entry<String, String> e : params.entrySet()){
				if (whereString.length() > 0)
					whereString += "or ";
				whereString += "cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + "and " + ((! params.containsKey("estatus")) ? "estatus = 0 and (" : "(") + whereString + ") ";
			queryString += "order by model.primerApellido, model.segundoApellido, model.primerNombre, model.segundoNombre";

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	//It's there to suppress the warning generated by (E) elementData[lastRet = i], 
	//which for the compiler is type unsafe. The compiler can not garauntee that the casting will succeed at runtime.
	//But since the the person who wrote the code knew that it was always going to be safe, decided to use @SuppressWarnings("unchecked")
	// to suppress the warning at compilation.
	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findByEmpleado(String nombreEmpleado, Long idEmpresa) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa and estatus = 0 and lower(model.nombre) like '%"+ nombreEmpleado +"%' order by model.primerApellido, model.segundoApellido, model.primerNombre, model.segundoNombre ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findLikeClaveNombre(String value, Long idEmpresa) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from Empleado model where model.idEmpresa = :idEmpresa " 
					+ (value == null || "".equals(value) ? "" : "and model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'")
					+ "order by model.primerApellido, model.segundoApellido, model.primerNombre, model.segundoNombre";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Empleado> findByPropertyPojoCompleto(String propertyName, String tipo, long value, Long idEmpresa) {
		return null;
	}
	
	//Para validar empleado repetido
	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> findEmpleadoRepetido(long idEmpleado, Long idEmpresa) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select empleado from Empleado empleado where model.idEmpresa = :idEmpresa and empleado.idPersona = "+ idEmpleado + " order by id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-05-23 | Javier Tirado 	| Implemento los metodos findByProperties y findLikeProperties
 */