package net.giro.adp.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

	@Override
	public long save(Obra entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<Obra> saveOrUpdateList(List<Obra> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findAll(long idObraPrincipal, boolean incluyeCanceladas, int revisadas, int autorizadas, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (idEmpresa <= 0) 
				idEmpresa = 1;
			if (idObraPrincipal > 0)
				orderBy = "(case coalesce(nullif(model.idObraPrincipal,0),0) when 0 then concat(model.id, '-0') else concat(model.idObraPrincipal, '-', model.id) end), model.nombre";
			queryString += (! incluyeCanceladas ? "and model.estatus > 0 " : "");
			queryString += (revisadas > 0 ? "and model.revisado = :revisado " : "");
			queryString += (autorizadas > 0 ? "and model.autorizado = :autorizado " : "");
			queryString += (idObraPrincipal > 0 ? "and :idObraPrincipal in (model.id, model.idObraPrincipal) " : "");
			if (orderBy == null || "".equals(orderBy))
				orderBy = "(case coalesce(nullif(model.idObraPrincipal,0),0) when 0 then concat(model.id, '-0') else concat(model.idObraPrincipal, '-', model.id) end), model.nombre";
			queryString += "order by " + orderBy;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idObraPrincipal > 0)
				query.setParameter("idObraPrincipal", idObraPrincipal);
			if (revisadas > 0)
				query.setParameter("revisado", revisadas);
			if (autorizadas > 0)
				query.setParameter("autorizado", autorizadas);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findLike(String value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, int revisadas, int autorizadas, int jerarquia, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Obra model ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0) 
				idEmpresa = 1;
			if (tipo == 4)
				incluyeAdministrativas = true;
			if (idObraPrincipal > 0)
				jerarquia = 4;
			if (idObraPrincipal > 0)
				orderBy = "(case coalesce(nullif(model.idObraPrincipal,0),0) when 0 then concat(model.id, '-0') else concat(model.idObraPrincipal, '-', model.id) end), model.nombre";
			
			whereString += "where model.idEmpresa = :idEmpresa ";
			if (value != null && ! "".equals(value.trim())) {
				whereString += "and (cast(model.id as string) like :propertyValue "
						+ "or cast(model.idObraPrincipal as string) like :propertyValue "
						+ "or cast(model.idCliente as string) like :propertyValue "
						+ "or cast(model.idResponsable as string) like :propertyValue "
						+ "or lower(trim(model.nombreCliente)) like :propertyValue "
						+ "or lower(trim(model.nombre)) like :propertyValue "
						+ "or lower(trim(model.nombreSucursal)) like :propertyValue "
						+ "or lower(trim(model.nombreResponsable)) like :propertyValue) ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}
			
			whereString += (idSucursal > 0 ? "model.idSucursal = :sucursal " : "");
			whereString += (revisadas > 0 ? "and model.revisado = :revisado " : "");
			whereString += (autorizadas > 0 ? "and model.autorizado = :autorizado " : "");
			whereString += (tipo > 0 ? "and model.tipoObra = :tipo " : "");
			whereString += (! incluyeAdministrativas ? "and model.tipoObra <> 4 " : "");
			whereString += (! incluyeCanceladas ? "and model.estatus > 0 " : "");
			
			switch (jerarquia) {
				case 1: whereString += "and model.idObraPrincipal = 0 "; break; // Solo Obra principales
				case 2: whereString += "and model.idObraPrincipal > 0 "; break; // Solo SubObras
				case 3:                                                         // Obras principales con sus subobras
					whereString = whereString.replace("model.", "x.");
					whereString = "select case x.idObraPrincipal when 0 then x.id else x.idObraPrincipal end from Obra x " + whereString;
					whereString = "where model.id in (" + whereString + ") or model.idObraPrincipal in (" + whereString + ") ";
					orderBy = ""; break;
				case 4: whereString += "and :idObraPrincipal in (model.id, model.idObraPrincipal) "; break; // Obra principal con sus SubObras
				default:break;
			}
			
			queryString += whereString;
			if (orderBy == null || "".equals(orderBy))
				orderBy = "(case coalesce(nullif(model.idObraPrincipal,0),0) when 0 then concat(model.id, '-0') else concat(model.idObraPrincipal, '-', model.id) end), model.nombre";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && ! "".equals(value.trim())) 
				query.setParameter("propertyValue", sb.toString());
			if (jerarquia == 4)
				query.setParameter("idObraPrincipal", idObraPrincipal);
			if (idSucursal > 0)
				query.setParameter("sucursal", idSucursal);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (revisadas > 0)
				query.setParameter("revisado", revisadas);
			if (autorizadas > 0)
				query.setParameter("autorizado", autorizadas);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findLikeProperty(String propertyName, Object value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, int revisadas, int autorizadas, int jerarquia, long idEmpresa, String orderBy, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Obra model ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0) 
				idEmpresa = 1;
			if (tipo == 4)
				incluyeAdministrativas = true;
			if (idObraPrincipal > 0)
				jerarquia = 4;
			if (idObraPrincipal > 0)
				orderBy = "(case coalesce(nullif(model.idObraPrincipal,0),0) when 0 then concat(model.id, '-0') else concat(model.idObraPrincipal, '-', model.id) end), model.nombre";
			
			whereString += "where model.idEmpresa = :idEmpresa ";
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					whereString += "and date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else {
					whereString += "and lower(trim(cast(model." + propertyName + " as string))) like :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}
			
			whereString += (idSucursal > 0 ? "model.idSucursal = :sucursal " : "");
			whereString += (revisadas > 0 ? "and model.revisado = :revisado " : "");
			whereString += (autorizadas > 0 ? "and model.autorizado = :autorizado " : "");
			whereString += (tipo > 0 ? "and model.tipoObra = :tipo " : "");
			whereString += (! incluyeAdministrativas ? "and model.tipoObra <> 4 " : "");
			whereString += (! incluyeCanceladas ? "and model.estatus > 0 " : "");
			
			switch (jerarquia) {
				case 1: whereString += "and model.idObraPrincipal = 0 "; break; // Solo Obra principales
				case 2: whereString += "and model.idObraPrincipal > 0 "; break; // Solo SubObras
				case 3:                                                         // Obras principales con sus subobras
					whereString = whereString.replace("model.", "x.");
					whereString = "select case x.idObraPrincipal when 0 then x.id else x.idObraPrincipal end from Obra x " + whereString;
					whereString = "where model.id in (" + whereString + ") or model.idObraPrincipal in (" + whereString + ") ";
					orderBy = ""; break;
				case 4: whereString += "and :idObraPrincipal in (model.id, model.idObraPrincipal) "; break; // Obra principal con sus SubObras
				default:break;
			}
			
			queryString += whereString;
			if (orderBy == null || "".equals(orderBy))
				orderBy = "(case coalesce(nullif(model.idObraPrincipal,0),0) when 0 then concat(model.id, '-0') else concat(model.idObraPrincipal, '-', model.id) end), model.nombre";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null) 
				query.setParameter("propertyValue", sb.toString());
			if (jerarquia == 4)
				query.setParameter("idObraPrincipal", idObraPrincipal);
			if (idSucursal > 0)
				query.setParameter("sucursal", idSucursal);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (revisadas > 0)
				query.setParameter("revisado", revisadas);
			if (autorizadas > 0)
				query.setParameter("autorizado", autorizadas);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findByProperty(String propertyName, Object value, long idObraPrincipal, long idSucursal, int tipo, boolean incluyeAdministrativas, boolean incluyeCanceladas, int revisadas, int autorizadas, int jerarquia, long idEmpresa, String orderBy, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Obra model ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if (idEmpresa <= 0) 
				idEmpresa = 1;
			if (tipo == 4)
				incluyeAdministrativas = true;
			if (idObraPrincipal > 0)
				jerarquia = 4;
			if (idObraPrincipal > 0)
				orderBy = "(case coalesce(nullif(model.idObraPrincipal,0),0) when 0 then concat(model.id, '-0') else concat(model.idObraPrincipal, '-', model.id) end), model.nombre";
			
			whereString += "where model.idEmpresa = :idEmpresa ";
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					whereString += "and date(model." + propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else {
					whereString += "and lower(trim(cast(model." + propertyName + " as string))) = :propertyValue ";
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase().replace(" ", "%"));
		    		sb.append("%");
				}
			}
			
			whereString += (idSucursal > 0 ? "model.idSucursal = :sucursal " : "");
			whereString += (revisadas > 0 ? "and model.revisado = :revisado " : "");
			whereString += (autorizadas > 0 ? "and model.autorizado = :autorizado " : "");
			whereString += (tipo > 0 ? "and model.tipoObra = :tipo " : "");
			whereString += (! incluyeAdministrativas ? "and model.tipoObra <> 4 " : "");
			whereString += (! incluyeCanceladas ? "and model.estatus > 0 " : "");
			
			switch (jerarquia) {
				case 1: whereString += "and model.idObraPrincipal = 0 "; break; // Solo Obra principales
				case 2: whereString += "and model.idObraPrincipal > 0 "; break; // Solo SubObras
				case 3:                                                         // Obras principales con sus subobras
					whereString = whereString.replace("model.", "x.");
					whereString = "select case x.idObraPrincipal when 0 then x.id else x.idObraPrincipal end from Obra x " + whereString;
					whereString = "where model.id in (" + whereString + ") or model.idObraPrincipal in (" + whereString + ") ";
					orderBy = ""; break;
				case 4: whereString += "and :idObraPrincipal in (model.id, model.idObraPrincipal) "; break; // Obra principal con sus SubObras
				default:break;
			}
			
			queryString += whereString;
			if (orderBy == null || "".equals(orderBy))
				orderBy = "(case coalesce(nullif(model.idObraPrincipal,0),0) when 0 then concat(model.id, '-0') else concat(model.idObraPrincipal, '-', model.id) end), model.nombre";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null) 
				query.setParameter("propertyValue", sb.toString());
			if (jerarquia == 4)
				query.setParameter("idObraPrincipal", idObraPrincipal);
			if (idSucursal > 0)
				query.setParameter("sucursal", idSucursal);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (revisadas > 0)
				query.setParameter("revisado", revisadas);
			if (autorizadas > 0)
				query.setParameter("autorizado", autorizadas);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findInProperty(String propertyName, List<Object> values, long idEmpresa, String orderBy) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		String inFilter = "";
		String prefijo = "";
		String sufijo = "";
		Object objValue = null;
		
		try {
			if (idEmpresa <= 0) 
				idEmpresa = 1;
			if (values != null && ! values.isEmpty()) {
				objValue = values.get(0);
				if (objValue.getClass() == java.util.Date.class) {
					whereString = "and date(model." + propertyName + ") IN ";
					prefijo = "date('";
					sufijo = "')";
				} else if (objValue.getClass() == java.lang.String.class) {
					whereString = "and lower(cast(model." + propertyName + " as string)) IN ";
					prefijo = "'";
					sufijo = "'";
				} else {
					whereString = "and model." + propertyName + " IN ";
					prefijo = "";
					sufijo = "";
				}
				
				// Agrupo los valores
				for (Object value : values) {
					if (! "".equals(inFilter)) 
						inFilter += ",";
					if (objValue.getClass() == java.util.Date.class)
						inFilter += prefijo + formateador.format((Date) value) + sufijo;
					else
						inFilter += prefijo + value.toString() + sufijo;
				}
				
				// Genero la clausula WHERE
				whereString += "(" + inFilter + ") ";
			}
			
			if (! whereString.isEmpty())
				queryString += whereString;
			if (orderBy == null || "".equals(orderBy))
				orderBy = "(case coalesce(nullif(model.idObraPrincipal,0),0) when 0 then concat(model.id, '-0') else concat(model.idObraPrincipal, '-', model.id) end), model.nombre";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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