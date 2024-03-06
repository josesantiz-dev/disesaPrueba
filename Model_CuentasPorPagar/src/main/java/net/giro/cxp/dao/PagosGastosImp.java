package net.giro.cxp.dao;

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
import net.giro.cxp.beans.PagosGastos;

@Stateless
public class PagosGastosImp extends DAOImpl<PagosGastos> implements PagosGastosDAO {
	@PersistenceContext 
	private EntityManager entityManager;

	@Override
	public long save(PagosGastos entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<PagosGastos> saveOrUpdateList(List<PagosGastos> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findAll(String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idEmpresa, long idOwner) throws Exception {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		String estatusQuery = "";
		
		try {
			if (idOwner > 0L)
				queryString += "and :idOwner in (model.idBeneficiario, model.creadoPor) ";
			if ((estatus == null || "".equals(estatus.trim())) && ! incluyeCancelados) 
				estatusQuery = "and model.estatus not in ('X') ";
			else if (estatus != null && ! "".equals(estatus.trim())) 
				estatusQuery = "and model.estatus in ('" + estatus + "'" + (! incluyeCancelados ? ",'X'" : "") + ") ";
			
			if (tipo != null && ! "".equals(tipo.trim()))
				queryString += "and model.tipo = :tipo ";
			if (estatusQuery != null && ! "".equals(estatusQuery.trim()))
				queryString += estatusQuery;
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.fecha desc,model.id desc";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idOwner > 0L)
				query.setParameter("idOwner", idOwner);
			if (tipo != null && ! "".equals(tipo.trim()))
				query.setParameter("tipo", tipo);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLike(String value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idEmpresa, long idOwner, int limite) throws Exception {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		String estatusQuery = "";
		StringBuffer sb = null;
		
		try {
			if (idOwner > 0L)
				queryString += "and :idOwner in (model.idBeneficiario, model.creadoPor) ";
			if ((estatus == null || "".equals(estatus.trim())) && ! incluyeCancelados) 
				estatusQuery = "and model.estatus not in ('X') ";
			else if (estatus != null && ! "".equals(estatus.trim())) 
				estatusQuery = "and model.estatus in ('" + estatus + "'" + (! incluyeCancelados ? ",'X'" : "") + ") ";
			
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue ";
				queryString += "or cast(model.noCheque as string) like :propertyValue ";
				queryString += "or trim(lower(model.beneficiario)) like :propertyValue ";
				queryString += "or trim(lower(model.concepto)) like :propertyValue ";
				queryString += "or trim(lower(model.folioAutorizacion)) like :propertyValue ";
				queryString += "or trim(lower(model.concepto)) like :propertyValue ";
				queryString += "or trim(lower(model.numeroCuentaOrigen)) like :propertyValue) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}

			if (tipo != null && ! "".equals(tipo.trim()))
				queryString += "and model.tipo = :tipo ";
			if (estatusQuery != null && ! "".equals(estatusQuery.trim()))
				queryString += estatusQuery;
			if (orderBy == null || "".equals(orderBy.trim()))
				orderBy = "model.fecha desc,model.id desc";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idOwner > 0L)
				query.setParameter("idOwner", idOwner);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (tipo != null && ! "".equals(tipo.trim()))
				query.setParameter("tipo", tipo);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeProperty(String propertyName, Object value, String tipo, String estatus, boolean incluyeCancelados, String orderBy, long idEmpresa, long idOwner, int limite) throws Exception {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String estatusQuery = "";
		StringBuffer sb = null;
		
		try {
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc,model.id desc");
			if (idOwner > 0L)
				queryString += "and :idOwner in (model.idBeneficiario, model.creadoPor) ";
			if ((estatus == null || "".equals(estatus.trim())) && ! incluyeCancelados) 
				estatusQuery = "and model.estatus not in ('X') ";
			else if (estatus != null && ! "".equals(estatus.trim())) 
				estatusQuery = "and model.estatus in ('" + estatus + "'" + (! incluyeCancelados ? ",'X'" : "") + ") ";
			
			if (value != null && ! "".equals(value.toString())) {
				sb = new StringBuffer();
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model." + propertyName + ") = date(:propertyValue) ";
		    		sb.append(formateador.format((Date) value));
				} else if (value.getClass() != java.lang.String.class) {
					queryString += "and cast(model." + propertyName + " as string) like :propertyValue ";
		    		sb.append("%");
		    		sb.append(value.toString().trim());
		    		sb.append("%");
				} else {
					queryString += "and trim(lower(cast(model." + propertyName + " as string))) like :propertyValue ";
		    		sb.append("%");
		    		sb.append(value.toString().toLowerCase().trim());
		    		sb.append("%");
				}
			}

			if (tipo != null && ! "".equals(tipo.trim()))
				queryString += "and model.tipo = :tipo ";
			if (estatusQuery != null && ! "".equals(estatusQuery.trim()))
				queryString += estatusQuery;
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (tipo != null && ! "".equals(tipo.trim()))
				query.setParameter("tipo", tipo);
			if (idOwner > 0L)
				query.setParameter("idOwner", idOwner);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeProperty(String propertyName, Object value, String tipo, String estatus, String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		String tmp = "";
		
		try {
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.id");
			if (value != null && ! "".equals(value.toString())) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date(:propertyValue) ";
					tmp = formateador.format((Date) value);
				} else if (value.getClass() != java.lang.String.class) {
					queryString += "and cast(model." + propertyName + " as string) like :propertyValue ";
					tmp = "%" + value.toString().trim() + "%";
				} else {
					if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2))))
						queryString += "and cast(model." + propertyName + " as string) like :propertyValue ";
					else
						queryString += "and trim(lower(model." + propertyName + ")) like :propertyValue ";
					tmp = "%" + value.toString().toLowerCase().trim() + "%";
				}

				sb = new StringBuffer();
				sb.append(tmp);
			}

			if (tipo != null && ! "".equals(tipo.trim()))
				queryString += "and model.tipo = :tipo ";
			if (estatus != null && ! "".equals(estatus.trim()))
				queryString += "and model.estatus = :estatus ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			if (tipo != null && ! "".equals(tipo.trim()))
				query.setParameter("tipo", tipo);
			if (estatus != null && ! "".equals(estatus.trim()))
				query.setParameter("estatus", estatus);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findByProperty(String propertyName, Object value, String tipo, String estatus, String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.id");
			if (value != null)
				queryString += "and model."+ propertyName + " = :propertyValue ";
			if (tipo != null && ! "".equals(tipo.trim()))
				queryString += "and model.tipo = :tipo ";
			if (estatus != null && ! "".equals(estatus.trim()))
				queryString += "and model.estatus = :estatus ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (tipo != null && ! "".equals(tipo.trim()))
				query.setParameter("tipo", tipo);
			if (estatus != null && ! "".equals(estatus.trim()))
				query.setParameter("estatus", estatus);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	// --------------------------------------------------------------------------------------------------------------

	@Override
	public int findConsecutivoByBeneficiario(long idBeneficiario, String tipo, String estatus, long idEmpresa) {
		String queryString = "select (coalesce(max(model.consecutivo),0) + 1) from PagosGastos model where model.idEmpresa = :idEmpresa and model.idBeneficiario = :idBeneficiario and model.tipo = :tipo and model.estatus = :estatus"; // (COUNT(model.idBeneficiario) + 1)
		Integer consecutivo = 0;
		
		try {
			idEmpresa = (idEmpresa > 0L ? idEmpresa : 1L);
			idBeneficiario = (idBeneficiario > 0L ? idBeneficiario : 0L);
			tipo = (tipo != null && ! "".equals(tipo.trim()) ? tipo.trim() : "");
			estatus = (estatus != null && ! "".equals(estatus.trim()) ? estatus.trim() : "");
			if (idBeneficiario <= 0 || "".equals(tipo.trim()) || "".equals(estatus.trim()))
				return consecutivo;

			Query query = entityManager.createQuery(queryString, Integer.class);
			query.setParameter("idBeneficiario", idBeneficiario);
			query.setParameter("tipo", tipo);
			query.setParameter("estatus", estatus);
			query.setParameter("idEmpresa", idEmpresa);
			consecutivo = (Integer) query.getSingleResult();
			if (consecutivo == null)
				consecutivo = 1;
		} catch (Exception re) {
			throw re;
		} 
		
		return consecutivo;
	}
	
	// --------------------------------------------------------------------------------------------------------------
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findInProperty(String columnName, List<Object> values, String estatus, String orderBy, long idEmpresa, int limite) throws Exception {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		String inFilter = "";
    	
    	try {
    		if (values != null && ! values.isEmpty()) {
    			whereString = "and cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) 
    					inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
    			whereString = whereString + inFilter + ") ";
        	}
			
			if (estatus != null && whereString.length() > 0)
				whereString += " and estatus = " + estatus;
			else if (estatus != null && whereString.length() == 0)
				whereString += " and estatus = " + estatus;
			estatus = null;
			
			if (! whereString.isEmpty())
				queryString = queryString + whereString;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	query.setParameter("idEmpresa", idEmpresa);
        	if (values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findByProperties(HashMap<String, Object> params, String estatus, String orderBy, long idEmpresa, int limite) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			for(Entry<String, Object> e : params.entrySet()) {
				if (e.getValue().getClass() == java.util.Date.class) 
					queryString += " and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					queryString += " and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					queryString += " and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			if (estatus != null)
				queryString += " and estatus = " + estatus;
			estatus = null;
			
			if (orderBy != null && !"".equals(orderBy)) 
				queryString += " order by " + orderBy;

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
	public List<PagosGastos> findLikePersonasConGastos(String beneficiario, String tipoPer, String tipo, String estatus, long idEmpresa, int max, Date fecha, String sucursales) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			String filtro = getListOfGastosByBeneficiario(beneficiario, tipoPer, tipo, estatus, fecha, sucursales, idEmpresa);
			if ("".equals(filtro))
				filtro = "and model.id <> 0";
			else
				filtro = "and model.id in (" + filtro + ")";
			
			queryString += filtro + " order by model.id desc" ;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeGtosComprobar(String propertyName, Object value, String v_tipo, String v_estatus1, long idEmpresa, int max, String sucursales) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		String v_where="";
		
		try {
			if (value == null)
				value="";
			
			if ("".equals(propertyName))
				v_where="and lower(model.beneficiario) like '%"+ value.toString().toLowerCase() +"%' ";
			else if ("beneficiario".equals(propertyName))				
				v_where="and lower(model.beneficiario) like '%"+ value.toString().toLowerCase() +"%' ";
			else if ("concepto".equals(propertyName))				
				v_where="and lower(model.concepto) like '%"+ value.toString().toLowerCase() +"%' ";
			
			if ("".equals(value))
				queryString = queryString + " and model.tipo='"+ v_tipo +"' and (model.estatus='"+ v_estatus1 +"' or model.estatus='X') and model.idCuentaOrigen is not null"; // queryString=queryString +" where model.tipo='"+ v_tipo +"' and (model.estatus='"+ v_estatus1 +"' or model.estatus='X') and ctaOri.ctaCheques is not null";
			else
				queryString = queryString + v_where + " and model.tipo='"+ v_tipo +"' and (model.estatus='"+ v_estatus1 +"' or model.estatus='X')"; // and model.idCuentaOrigen is not null";
			queryString = queryString + " order by model.fecha, model.id desc" ; // queryString = queryString + 	" and sucCta.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") order by model.pagosGastosId desc" ;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findTransferencias(String propertyName, Object value, String v_tipo, long idEmpresa, int max, String sucursales) {
		String queryString = "select model from PagosGastos model ";
		String v_where="";
		
		if (value == null)
			value="";
		
		try {
			  queryString += "left join fetch model.idCuentaOrigen ctaOri "+
								   "left join fetch ctaOri.sucursal sucOri "+
								   "left join fetch model.cuentaOrigenTerceros ctaOri3 "+
								   "left join fetch model.cuentaDestino ctaDest "+
								   "left join fetch ctaDest.sucursal sucDest "+
								   "left join fetch model.cuentaDestinoTerceros ctaDest3 " +
								   " where model.idEmpresa = :idEmpresa and model.tipo='"+ v_tipo +"' and (model.estatus='G' or model.estatus='X') ";
								  
			if ("fecha".equals(propertyName) && !"".equals(value))				
				v_where="and ''''||to_char(model.fecha, 'yyyy-MM-dd')||''''  like '%"+ value.toString().toLowerCase() +"%' ";
			else if ("cuentaOrigen".equals(propertyName)&& !"".equals(value))				
			   v_where="and (lower(ctaOri.cinsnlargo) like '%"+ value.toString().toLowerCase() +"%' or "+	
					   "lower(ctaOri.cinsncorto) like '%"+value.toString().toLowerCase()+"%' or "+
					   "lower(ctaOri.ctaBancoId) like '%"+value.toString().toLowerCase()+"%'" +
					   " or (ctaOri.ctaCheques) like '%"+value.toString().toLowerCase() +"%')";
			else if ("cuentaOrigenTerceros".equals(propertyName)&& !"".equals(value))
				v_where="and (lower(ctaOri3.descripcion) like '%"+ value.toString().toLowerCase() +"%' or "+	
					   "lower(ctaOri3.valorId) like '%"+value.toString().toLowerCase()+"%' or "+
					   "lower(ctaOri3.atributo1) like '%"+value.toString().toLowerCase()+"%')";
			else if ("cuentaDestino".equals(propertyName)&& !"".equals(value))	
				v_where="and (lower(ctaDest.cinsnlargo) like '%"+ value.toString().toLowerCase() +"%' or "+	
					   "lower(ctaDest.cinsncorto) like '%"+value.toString().toLowerCase()+"%' or "+
					   "lower(ctaDest.ctaBancoId) like '%"+value.toString().toLowerCase()+"%')" +
					   " or (ctaDest.ctaCheques) like '%"+value.toString().toLowerCase() +"%')";
			else if ("cuentaDestinoTerceros".equals(propertyName)&& !"".equals(value))
				v_where="and (lower(ctaDest3.descripcion) like '%"+ value.toString().toLowerCase() +"%' or "+	
					   "lower(ctaDest3.valorId) like '%"+value.toString().toLowerCase()+"%' or "+
					   "lower(ctaDest3.atributo1) like '%"+value.toString().toLowerCase()+"%')";
			else if ("Referencia".equals(propertyName)&& !"".equals(value))
				v_where="and (lower(model.folioAutorizacion) like '%"+ value.toString().toLowerCase() +"%')";
			
			queryString=queryString + v_where + " and (sucOri.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") or sucDest.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ")) order by model.fecha desc" ;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeGtosPorComprobarPersona(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, long idEmpresa, int max) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			queryString += "and model.idBeneficiario = :propertyValue  and model.tipo='"+ tipo +"' and " +
								   (traer == null || "".equals(traer) ? "model.estatus='"+ estatus +"'" : "(model.estatus='C' or model.estatus='"+ estatus +"') ");
		
			if ("R".equals(tipo) && "A".equals(estatus)) {
				queryString = queryString + " and model.fecha <= :fecha";
			}
			queryString = queryString + " order by model.id desc ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if ("R".equals(tipo) && "A".equals(estatus))
				query.setParameter("fecha", fecha);
			query.setParameter("propertyValue", value);
			if (max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (Exception re) {			
			throw re;
		} 
	}

	@Override
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<PagosGastos> buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda, long idEmpresa) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try{
			queryString += "and model.tipo = 'M' and (model.estatus = 'G' or model.estatus = 'X') ";
			
			if (tipoBusqueda.equals("fecha"))
				queryString += " and ''''||to_char(model.fecha, 'yyyy-MM-dd')||'''' like '%" + valorBusqueda.toString().toLowerCase() +"%' ";
			else if (tipoBusqueda.equals("beneficiario")) 
				queryString += " and model.beneficiario like '%" + valorBusqueda.toString() + "%' ";
			else if (tipoBusqueda.equals("idCuentaOrigen")) 
				queryString += " and model.idCuentaOrigen = " + valorBusqueda.toString();

			queryString += " order by model.estatus ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	private String getListOfGastosByBeneficiario(String beneficiario, String tipoPer, String tipo, String estatus, Date fecha, String sucursales, long idEmpresa) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		String listaGastos = "";
		String listaBeneficiarios = "";
		
		try {
			if (beneficiario == null || "".equals(beneficiario))
				 beneficiario = "";
			else{
				beneficiario = " and lower(model.beneficiario) like  '%"+ beneficiario.toLowerCase() +"%' ";
			}
			
			if (tipoPer == null || "".equals(tipoPer))
				tipoPer = "";
			else
				tipoPer = " and model.tipoBeneficiario = '" + tipoPer + "' ";
			
			if (estatus == null)
				estatus = "";
			
			if ("pago".equals(estatus))
				estatus = " and (model.estatus = 'X' or model.estatus = 'C') "; 
			else
				estatus = " and model.estatus in (" + estatus + ")";
			
			queryString += "and " + beneficiario + tipoPer + " and model.tipo='"+ tipo + "' " + estatus;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			List<PagosGastos> listGastos = query.getResultList();
			
			if (! listGastos.isEmpty()) {
				for(PagosGastos var : listGastos) {
					if (! listaBeneficiarios.contains(var.getIdBeneficiario().toString())) {
						listaBeneficiarios += var.getIdBeneficiario().toString() + ",";
						
						if (! "".equals(listaGastos))
							listaGastos += ",";
						listaGastos += var.getId().toString();
					}
				}
			} else 
				listaGastos = "0";
		} catch (Exception re) {
			throw re;
		} 
		
		return listaGastos;
	}
}
