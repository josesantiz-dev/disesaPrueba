package net.giro.cxc.dao;

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

import net.giro.cxc.beans.Factura;

@Stateless
public class FacturaImp extends DAOImpl<Factura> implements FacturaDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(Factura entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Factura> saveOrUpdateList(List<Factura> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findAll(String orderBy, long idEmpresa) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaEmision desc, model.id desc");
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
	public List<Factura> findLike(String value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		List<String> valores = null;
    	StringBuffer sb = null;
		
		try {
    		idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
    		orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaEmision desc, model.id desc");
			value = validateString(value);
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (lower(trim(cast(model.id as string))) like :value ";
				queryString += "or lower(trim(cast(model.idObra as string))) like :value ";
				queryString += "or lower(trim(model.folioFactura)) like :value ";
				queryString += "or lower(trim(model.nombreObra)) like :value ";
				queryString += "or lower(trim(model.nombreSucursal)) like :value ";
				queryString += "or lower(trim(model.cliente)) like :value ";
				queryString += "or lower(trim(model.rfc)) like :value) ";

				valores = recuperaValores(value.toLowerCase().trim(), "\\+");
				if (valores == null || valores.isEmpty()) {
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.trim().toLowerCase());
		    		sb.append("%");
				}
        	}

			queryString += "and model.estatus in (1" + (incluyeCanceladas ? ",0,2,3) " : ") ");
			queryString += "and model.tipoObra " + (tipoObra > 0 ? "=" : "<>") + " :tipoObra ";
			if (idCliente > 0L)
				queryString += "and model.idCliente = :idCliente ";
			if (tipoComprobante != null && ! "".equals(tipoComprobante.trim()))
				queryString += "and model.tipoComprobante = :tipoComprobante ";
			if (timbradas)
				queryString += "and model.timbrado = 1 and trim(coalesce(uuid, '')) <> '' ";
			if (valores != null && ! valores.isEmpty()) 
				queryString = multiplicaConsulta(queryString, valores);
			queryString += " order by " + orderBy;
			
        	Query query = entityManager.createQuery(queryString);  
        	query.setParameter("idEmpresa", idEmpresa);
        	query.setParameter("tipoObra", tipoObra);
			if (idCliente > 0L)
				query.setParameter("idCliente", idCliente);
			if (tipoComprobante != null && ! "".equals(tipoComprobante.trim()))
				query.setParameter("tipoComprobante", tipoComprobante);
			if (sb != null && ! "".equals(sb.toString().trim()))
            	query.setParameter("value", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
        	return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findLikeProperty(String propertyName, Object value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		List<String> valores = null;
    	StringBuffer sb = null;
		
		try {
    		idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
    		orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaEmision desc, model.id desc");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(" + propertyName + ") = date('" + formatter.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString())) {
					value = validateString(value.toString());
					queryString += "and lower(trim(cast(" + propertyName + " as string))) like :value ";

					valores = recuperaValores(value.toString().toLowerCase().trim(), "\\+");
					if (valores == null || valores.isEmpty()) {
						sb = new StringBuffer();
			    		sb.append("%");
			    		sb.append(value.toString().trim().toLowerCase());
			    		sb.append("%");
					}
				} 
        	}

			queryString += "and model.estatus in (1" + (incluyeCanceladas ? ",0,2,3) " : ") ");
			queryString += "and model.tipoObra " + (tipoObra > 0 ? "=" : "<>") + " :tipoObra ";
			if (idCliente > 0L)
				queryString += "and model.idCliente = :idCliente ";
			if (tipoComprobante != null && ! "".equals(tipoComprobante.trim()))
				queryString += "and model.tipoComprobante = :tipoComprobante ";
			if (timbradas)
				queryString += "and model.timbrado = 1 and trim(coalesce(uuid, '')) <> '' ";
			if (valores != null && ! valores.isEmpty()) 
				queryString = multiplicaConsulta(queryString, valores);
			queryString += " order by " + orderBy;
			
        	Query query = entityManager.createQuery(queryString);   
        	query.setParameter("idEmpresa", idEmpresa);
        	query.setParameter("tipoObra", tipoObra);
			if (idCliente > 0L)
				query.setParameter("idCliente", idCliente);
			if (tipoComprobante != null && ! "".equals(tipoComprobante.trim()))
				query.setParameter("tipoComprobante", tipoComprobante);
			if (sb != null && ! "".equals(sb.toString().trim()))
            	query.setParameter("value", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
        	return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findByProperty(String propertyName, Object value, long idCliente, String tipoComprobante, int tipoObra, boolean timbradas, boolean incluyeCanceladas, String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaEmision desc, model.id desc");
			if (value != null) {
				if (java.util.Date.class == value.getClass()) {
					queryString += " and date(" + propertyName + ") = date('" + formatter.format((Date) value) + "') ";
					value = null;
				} else {
					queryString += " and " + propertyName + " = :propertyValue ";
				}
			}

			queryString += "and model.estatus in (1" + (incluyeCanceladas ? ",0,2,3) " : ") ");
			queryString += "and model.tipoObra " + (tipoObra > 0 ? "=" : "<>") + " :tipoObra ";
			if (idCliente > 0L)
				queryString += "and model.idCliente = :idCliente ";
			if (tipoComprobante != null && ! "".equals(tipoComprobante.trim()))
				queryString += "and model.tipoComprobante = :tipoComprobante ";
			if (timbradas)
				queryString += "and model.timbrado = 1 and trim(coalesce(uuid, '')) <> '' ";
			queryString += " order by " + orderBy;
			
        	Query query = entityManager.createQuery(queryString);   
        	query.setParameter("idEmpresa", idEmpresa);
        	query.setParameter("tipoObra", tipoObra);
			if (idCliente > 0L)
				query.setParameter("idCliente", idCliente);
			if (tipoComprobante != null && ! "".equals(tipoComprobante.trim()))
				query.setParameter("tipoComprobante", tipoComprobante);
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
	public List<Factura> findLikeProperties(HashMap<String, Object> params, long idEmpresa) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try { 
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			for (Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " or ";
				
				if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += "cast(model." + e.getKey() + " as string) LIKE '%" + ((BigDecimal) e.getValue()).toString() + "%'";
				else
					whereString += "cast(model." + e.getKey() + " as string) LIKE '%" + e.getValue().toString() + "%'";
			}

			if (! whereString.isEmpty())
				queryString += "and (" + whereString + ") ";
			queryString += "order by model.fechaEmision desc, model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> comprobarFolioFacturacion(String serie, String folio, String orderBy, long idEmpresa) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa and trim(model.serie) = :serie and trim(model.folio) like :folio ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaEmision desc, model.id desc");
			serie = validateString(serie);
			folio = validateString(folio);
			if ("".equals(serie.trim()) || "".equals(folio.trim()))
				return null;
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("serie", serie);
			query.setParameter("folio", folio);
			return query.getResultList();
		} catch (Exception re) {		
			throw re;
		} 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findLikePropertySinProvision(String propertyName, Object value, int tipoObra, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa and model.provisionada in (0,2) and model.saldo > 0 ";
    	String sqlWhere = "";
    	StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			sqlWhere = "and tipoObra <> :tipoObra ";
			if (tipoObra > 0)
				sqlWhere = "and tipoObra = :tipoObra ";
			
			if (value != null && ! "".equals(value.toString())){
        		if (propertyName.contains("id") || propertyName.toLowerCase().equals("idobra")) {
            		sqlWhere += "and lower(cast(" + propertyName + " as string)) LIKE :propertyValue ";
            	} else {
            		sqlWhere += "and lower(" + propertyName + ") LIKE :propertyValue ";
            	}

        		sb = new StringBuffer();
        		sb.append("%");
        		sb.append(value.toString().toLowerCase());
        		sb.append("%");
        	}
        	
        	queryString = queryString + sqlWhere;
			queryString += "order by model.fechaEmision desc, model.id desc";
			
        	Query query = entityManager.createQuery(queryString);   
        	query.setParameter("idEmpresa", idEmpresa);     	
        	query.setParameter("tipoObra", tipoObra);			
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
	public List<Factura> findProvisionadas(String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa and model.provisionada > 0 and estatus = 1 ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaEmision desc, model.id desc");
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
	public List<Factura> provisionMensual(Date fechaDesde, Date fechaHasta, String orderBy,long idEmpresa, int limite) throws Exception {
		String queryString = "select model from Factura model where model.fechaEmision between :fechaDesde and :fechaHasta and model.idEmpresa = :idEmpresa and model.provisionada > 0 and estatus = 1 ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaEmision desc, model.id desc");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("fechaDesde", fechaDesde);
			query.setParameter("fechaHasta", fechaHasta);
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
	public List<Factura> paraProvisionar(Date fechaDesde, Date fechaHasta, String orderBy, long idEmpresa) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaEmision desc, model.id desc");
			queryString += "and date(model.fechaEmision) between date(:fechaDesde) and date(:fechaHasta) ";
			queryString += "and model.saldo > 0 ";
			queryString += "and model.provisionada = 0 ";
			queryString += "and model.estatus = 1 ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("fechaDesde", fechaDesde);
			query.setParameter("fechaHasta", fechaHasta);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Factura> findList(List<Long> idFacturas, String orderBy, long idEmpresa) throws Exception {
		String queryString = "select model from Factura model where model.idEmpresa = :idEmpresa and model.id in (:lista) ";
		String lista = "";
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fechaEmision desc, model.id desc");
			idFacturas = (idFacturas != null && ! idFacturas.isEmpty() ? idFacturas : new ArrayList<Long>());
			lista = (! idFacturas.isEmpty() ?  StringUtils.join(idFacturas, ",") : "0");
			queryString = queryString.replace(":lista", lista);
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	// ------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------------------

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
			queryModificada += (! "".equals(queryModificada.trim()) ? " or " : "") +  "model.id in (" + queryOriginal.trim().replace("select model from", "select model.id from").replace(":value", "'%" + valor + "%'") + ")";
		return "select model from Factura model where (" + queryModificada.trim() + ") ";
	}
	
	private String validateString(String value) {
		if (value == null || "".equals(value.trim()))
			return "";
		
		value = value.trim().replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U");
		value = value.trim().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u");
		value = value.trim().replace("Ä", "A").replace("Ë", "E").replace("Ï", "I").replace("Ö", "O").replace("Ü", "U");
		value = value.trim().replace("ä", "a").replace("ë", "e").replace("ï", "i").replace("ö", "o").replace("ü", "u");
		
		return value;
	}
}
