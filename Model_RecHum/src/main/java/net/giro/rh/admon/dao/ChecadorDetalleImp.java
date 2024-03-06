package net.giro.rh.admon.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.DAOImpl;
import net.giro.rh.admon.beans.ChecadorDetalle;

@Stateless
public class ChecadorDetalleImp extends DAOImpl<ChecadorDetalle> implements ChecadorDetalleDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(ChecadorDetalle entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<ChecadorDetalle> saveOrUpdateList(List<ChecadorDetalle> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ChecadorDetalle> findAll(long idChecador, String orderBy) throws Exception {
		String queryString = "select model from ChecadorDetalle model where model.idChecador.id = :idChecador ";
		
		try {
			idChecador = (idChecador > 0L ? idChecador : 0L);
			if (idChecador <= 0)
				return new ArrayList<ChecadorDetalle>();
			
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombreEmpleado");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idChecador", idChecador);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ChecadorDetalle> findLike(String value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ChecadorDetalle model where model.idChecador.idEmpresa = :idEmpresa ";
		List<String> valores = null;
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.idChecador.fecha desc, model.idChecador.idObra, model.nombreEmpleado");
			if (value != null && ! "".equals(value.toString().trim())) {
				queryString += "and (cast(model.id as string) LIKE :propertyValue "  
						+ "or cast(model.idEmpleado as string) LIKE :propertyValue " 
						+ "or lower(trim(model.nombreEmpleado)) LIKE :propertyValue "
						+ "or cast(model.idChecador.id as string) LIKE :propertyValue " 
						+ "or cast(model.idChecador.idObra as string) LIKE :propertyValue " 
						+ "or lower(trim(model.idChecador.nombreObra)) LIKE :propertyValue) ";

				valores = recuperaValores(value.toString().toLowerCase().trim(), "\\+");
				if (valores == null || valores.isEmpty()) {
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.trim().toLowerCase());
		    		sb.append("%");
				}
			} 
			
			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "model.fecha desc, model.id desc";
			}
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
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
	public List<ChecadorDetalle> findLikeProperty(String propertyName, Object value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ChecadorDetalle model where model.idChecador.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombreEmpleado");
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					queryString = queryString + " and lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
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
	public List<ChecadorDetalle> findByProperty(String propertyName, Object value, long idObra, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ChecadorDetalle model where model.idChecador.idEmpresa = :idEmpresa ";
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombreEmpleado");
			if (value != null)
				queryString = queryString + " and model."+ propertyName + " = :propertyValue";
			queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	/*@Override
	@SuppressWarnings("unchecked")
	public List<ChecadorDetalle> findInProperty(String columnName, List<Object> values, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from ChecadorDetalle model where model.idChecador.idEmpresa = :idEmpresa ";
    	String sqlWhere = "";
    	String inFilter = "";
    	
    	try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombreEmpleado");
    		if (values != null && ! values.isEmpty()) {
    			sqlWhere = " and cast(model." + columnName + " as string) IN (";
    			
    			for (int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
        		sqlWhere = sqlWhere + inFilter + ")";
        	}
        	
        	queryString += sqlWhere;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
			if (limite > 0)
				query.setMaxResults(limite);
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} 
	}*/

	@Override
	@SuppressWarnings("unchecked")
	public List<ChecadorDetalle> findByProperties(HashMap<String, Object> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ChecadorDetalle model where model.idChecador.idEmpresa = :idEmpresa ";
		String where = "";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			for (Entry<String, Object> e : params.entrySet()) 
				where += "and cast(model." + e.getKey() + " as string) = '" + e.getValue() + "' ";
			queryString += where + "order by model.nombreEmpleado" ;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	/*@Override
	@SuppressWarnings("unchecked")
	public List<ChecadorDetalle> findLikeProperties(HashMap<String, Object> params, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from ChecadorDetalle model where model.idChecador.idEmpresa = :idEmpresa ";
		String where = "";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			for (Entry<String, Object> e : params.entrySet())
				where += "and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			queryString += where + "order by model.nombreEmpleado" ;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}*/

	@Override
	@SuppressWarnings("unchecked")
	public List<ChecadorDetalle> findByDates(Date fechaDesde, Date fechaHasta, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from ChecadorDetalle model where model.idChecador.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (fechaDesde == null || fechaHasta == null)
				return new ArrayList<ChecadorDetalle>();
			
			if (fechaDesde.after(fechaHasta))
				return new ArrayList<ChecadorDetalle>();

			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombreEmpleado");
			queryString = queryString + " and idChecador.estatus = 0 AND DATE(model.fecha) BETWEEN DATE('" + formatter.format(fechaDesde) + "') AND DATE('" + formatter.format(fechaHasta) + "')";
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			orderBy = null;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	/*@Override
	@SuppressWarnings("unchecked")
	public List<ChecadorDetalle> findByObraEmpleado(long idObra, long idEmpleado, Date fechaBase, String orderBy) throws Exception {
		String queryString = "select model from ChecadorDetalle model where model.idChecador.idObra = :idObra and date(model.idChecador.fecha) > date(:fechaBase) and model.idEmpleado = :idEmpleado order by model.idChecador.fecha ";
		SimpleDateFormat formatter = null;
		
		try {
			if (fechaBase == null)
				return new ArrayList<ChecadorDetalle>();

			formatter = new SimpleDateFormat("MM/dd/yyyy");
			idObra = (idObra > 0L ? idObra : 0L);
			idEmpleado = (idEmpleado > 0L ? idEmpleado : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombreEmpleado");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idObra", idObra);
			query.setParameter("idEmpleado", idEmpleado);
			query.setParameter("fechaBase", formatter.format(fechaBase));
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}*/

	@Override
	@SuppressWarnings("unchecked")
	public List<ChecadorDetalle> findAsistenciasPosteriorFecha(long idEmpleado, Date fecha, String orderBy) throws Exception {
		String queryString = "select model from ChecadorDetalle model where model.idEmpleado = :idEmpleado and date(model.fecha) > date(:fecha) ";
		
		try {
			idEmpleado = (idEmpleado > 0L ? idEmpleado : 0L);
			if (idEmpleado <= 0L || fecha == null)
				return new ArrayList<ChecadorDetalle>();

			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.nombreEmpleado");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpleado", idEmpleado);
			query.setParameter("fecha", fecha);
			return query.getResultList();
		} catch (Exception re) {
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
		return "select model from ChecadorDetalle model where (" + queryModificada.trim() + ") ";
	}
}
