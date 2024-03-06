package net.giro.cxp.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class PagosGastosImp extends DAOImpl<PagosGastos> implements PagosGastosDAO  {
	@PersistenceContext 
	private EntityManager entityManager;
	private String queryString;
	private String whereString;
	private static String orderBy;
	private static Long estatus;

	@Override
	public void orderBy(String orderBy) {
		PagosGastosImp.orderBy = orderBy;
	}

	@Override
	public void estatus(Long estatus) {
		PagosGastosImp.estatus = estatus;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.queryString = "select model from PagosGastos model ";
			this.whereString = "";
			
			if (value != null) {
				this.whereString = " where model."+ propertyName + " = :propertyValue";
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " where estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null)
				query.setParameter("propertyValue", value);
			if (max > 0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		StringBuffer sb = null;
		
		try {
			this.queryString = "select model from PagosGastos model ";
			this.whereString = "";
			
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					this.whereString = " where cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					this.whereString = " where lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " where estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && !"".equals(value.toString()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
    	String inFilter = "";
    	
    	try {
    		this.queryString = "select model from PagosGastos model ";
			this.whereString = "";
    		
    		if(values != null && ! values.isEmpty()){
    			this.whereString = " WHERE cast(model." + columnName + " as string) IN (";
    			
    			for(int i = 0; i < values.size(); i++) {
    				if (!"".equals(inFilter)) inFilter += ",";
        			inFilter += ":" + columnName + i;
    			}
    			
    			this.whereString = this.whereString + inFilter + ")";
        	}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " where estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
        	
        	Query query = entityManager.createQuery(queryString);
        	if(values != null && ! values.isEmpty()) {
        		for(int i = 0; i < values.size(); i++) {
        			query.setParameter(columnName + i, values.get(i).toString());
    			}
        	}
        	
        	return query.getResultList();
    	} catch (Exception e) {
    		throw e;
    	} finally {
			estatus = null;
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findByProperties(HashMap<String, Object> params, int limite) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			this.queryString = "select model from PagosGastos model ";
			this.whereString = "";
			
			for(Entry<String, Object> e : params.entrySet()) {
				if (this.whereString.length() > 0)
					this.whereString += " and";
				
				if (e.getValue().getClass() == java.util.Date.class) 
					this.whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					this.whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					this.whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = " estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
			}

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			this.queryString = "select model from PagosGastos model ";
			this.whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (this.whereString.length() > 0)
					this.whereString += " and";
				this.whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if(estatus != null && this.whereString.length() > 0)
				this.whereString += " and estatus = " + estatus;
			else if(estatus != null && this.whereString.length() == 0)
				this.whereString = "  estatus = " + estatus;
			estatus = null;
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
				orderBy = "";
			}

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);

			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}
	
	public PagosGastos findAllById(Long id){
		PagosGastos movCta = null;
			
		try {
			String queryString = "select movCta from PagosGastos movCta "+
				"where movCta.id=:id order by movCta.id desc";
			  
			Query query = entityManager.createQuery(queryString);
			query.setParameter("id", id);
			movCta = (PagosGastos)query.getSingleResult(); 
			return movCta;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeBenefTipoPersona(String beneficiario, String tipoPer, String tipo, String estatus, int max, String sucursales) {
		try {
			if(beneficiario == null || "".equals(beneficiario))
				beneficiario = "";
			else
				beneficiario = "lower(movCta.beneficiario) like  '%"+ beneficiario.toLowerCase() +"%' and ";
			
			if(tipoPer == null || "".equals(tipoPer))
				tipoPer = "";
			else
				tipoPer = " movCta.tipoBeneficiario = '" + tipoPer + "' and ";
			
			if(estatus == null)
				estatus = "";
			
			if("pago".equals(estatus))
				estatus = " and (movCta.estatus = 'X' or movCta.estatus = 'C') "; 
			else
				estatus = " and movCta.estatus = '" + estatus + "'";
			
			String queryString = "select movCta from PagosGastos movCta " +
			    "where " + beneficiario + tipoPer + " movCta.tipo='"+ tipo + "' " + estatus +" " +
			    "order by movCta.id desc" ;

			Query query = entityManager.createQuery(queryString);
			
			if(max > 0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private String getListOfGastosByBeneficiario(String beneficiario, String tipoPer, String tipo, String estatus, Date fecha, String sucursales) {
		String listaGastos = "";
		String listaBeneficiarios = "";
		
		try {
			if(beneficiario == null || "".equals(beneficiario))
				 beneficiario = "";
			else{
				beneficiario = "lower(movCta.beneficiario) like  '%"+ beneficiario.toLowerCase() +"%' and ";
			}
			
			if(tipoPer == null || "".equals(tipoPer))
				tipoPer = "";
			else
				tipoPer = " movCta.tipoBeneficiario = '" + tipoPer + "' and ";
			
			if(estatus == null)
				estatus = "";
			
			if("pago".equals(estatus))
				estatus = " and (movCta.estatus = 'X' or movCta.estatus = 'C') "; 
			else
				estatus = " and movCta.estatus in (" + estatus + ")";
			
			 String queryString = "select movCta from PagosGastos movCta " +
			    "where " + beneficiario + tipoPer + " movCta.tipo='"+ tipo + "' " + estatus;
			
			Query query = entityManager.createQuery(queryString);
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
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
		
		return listaGastos;
	}
	
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikePersonasConGastos(String beneficiario, String tipoPer, String tipo, String estatus, int max, Date fecha, String sucursales) {
		try {
			/*if(beneficiario == null || "".equals(beneficiario))
				 beneficiario = "";
			else{
				beneficiario = "lower(movCta.beneficiario) like  '%"+ beneficiario.toLowerCase() +"%' and ";
			}
			
			if(tipoPer == null || "".equals(tipoPer))
				tipoPer = "";
			else
				tipoPer = " movCta.tipoBeneficiario = '" + tipoPer + "' and ";
			
			if(estatus == null)
				estatus = "";
			
			if("pago".equals(estatus))
				estatus = " and (movCta.estatus = 'X' or movCta.estatus = 'C') "; 
			else
				estatus = " and movCta.estatus in (" + estatus + ")";
			
			 String queryString = "select movCta from PagosGastos movCta " +
			    "where " + beneficiario + tipoPer + " movCta.tipo='"+ tipo + "' " + estatus + " " +
			    "order by movCta.id desc" ;*/
			String filtro = getListOfGastosByBeneficiario(beneficiario, tipoPer, tipo, estatus, fecha, sucursales);
			if ("".equals(filtro))
				filtro = "where movCta.id <> 0";
			else
				filtro = "where movCta.id in (" + filtro + ")";
			
			String queryString = "select movCta from PagosGastos movCta " + filtro + " order by movCta.id desc" ;
			Query query = entityManager.createQuery(queryString);
			if(max > 0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeGtosComprobar(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
		String v_where="";
		
		try {
			String queryString = "select movCta from PagosGastos movCta ";
			
			if(value == null)
				value="";
			
			if("".equals(propertyName))
				v_where="where lower(movCta.beneficiario) like '%"+ value.toString().toLowerCase() +"%' ";
			else if("beneficiario".equals(propertyName))				
				v_where="where lower(movCta.beneficiario) like '%"+ value.toString().toLowerCase() +"%' ";
			else if("concepto".equals(propertyName))				
				v_where="where lower(movCta.concepto) like '%"+ value.toString().toLowerCase() +"%' ";
			
			/*else if("cuentaOrigen".equals(propertyName))
				v_where="where lower(movCta.idCuentaOrigen) like '%"+ value.toString().toLowerCase() +"%' ";*/
						   
			if("".equals(value))
				queryString = queryString + " where movCta.tipo='"+ v_tipo +"' and (movCta.estatus='"+ v_estatus1 +"' or movCta.estatus='X') and movCta.idCuentaOrigen is not null"; // queryString=queryString +" where movCta.tipo='"+ v_tipo +"' and (movCta.estatus='"+ v_estatus1 +"' or movCta.estatus='X') and ctaOri.ctaCheques is not null";
			else
				queryString = queryString + v_where + " and movCta.tipo='"+ v_tipo +"' and (movCta.estatus='"+ v_estatus1 +"' or movCta.estatus='X')"; // and movCta.idCuentaOrigen is not null";
			
			queryString = queryString + " order by movCta.fecha, movCta.id desc" ; // queryString = queryString + 	" and sucCta.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") order by movCta.pagosGastosId desc" ;
			
			Query query = entityManager.createQuery(queryString);
			
			if(max > 0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeMovCtas(String propertyName,Object value,String v_tipo,String v_estatus1,int max,String sucursales) {
		String v_where="";
		
		try {
			if(value == null)
				value="";
			
			String queryString = "select movCta from PagosGastos movCta ";
								   //"inner join fetch movCta.cuentaOrigen ctaOri "+
								   //"inner join fetch ctaOri.sucursal sucCta " +
								   //"inner join fetch ctaOri.catBancoId catBanc "+
								   //"inner  join fetch movCta.tiposMovtoId tipoMov "+
								   //"left join fetch movCta.noBeneficiario ben "+
								   //"left join fetch ben.relacionId rel "+
								   //"left join fetch movCta.sucursal suc "+
								   //"left  join fetch movCta.cuentaDestino ctaDest "+
								   //"left  join fetch movCta.cuentaDestinoTerceros ctaDest3 ";
								
			if("fecha".equals(propertyName)) {
				v_where="where ''''||to_char(movCta.fecha, 'yyyy-MM-dd')||''''  like '%"+ value.toString().toLowerCase() +"%' ";		
			} else if("idCuentaOrigen".equals(propertyName)) {
				v_where="where movCta.idCuentaOrigen in (" + value.toString().toLowerCase() + ") ";
				   /*v_where="where (lower(ctaOri.cinsnlargo) like '%"+ value.toString().toLowerCase() +"%' or "+	
						   "lower(ctaOri.cinsncorto) like '%"+value.toString().toLowerCase()+"%' or "+
						   "lower(ctaOri.ctaBancoId) like '%"+value.toString().toLowerCase()+"%' or "+
						   "lower(ctaOri.ctaCheques) like '%"+value.toString().toLowerCase() + "%') ";*/
						   
			
			/*else if("tiposMovtoId".equals(propertyName))
			       v_where="where (lower(tipoMov.valorId) like '%"+ value.toString().toLowerCase() +"%' or "+	
						   "lower(tipoMov.descripcion) like '%"+value.toString().toLowerCase()+"%') ";*/
			} else if("idTiposMovimiento".equals(propertyName)) {
				v_where="where movCta.idTiposMovimiento in (" + value.toString().toLowerCase() + ") ";
			}
				
			if("".equals(value)){
				queryString = queryString + " where movCta.tipo='"+ v_tipo +"' and (movCta.estatus='"+ v_estatus1 +"' or movCta.estatus='X') and movCta.idCuentaOrigen is not null"; // ctaOri.ctaCheques is not null";
			}
			else{
				queryString = queryString + v_where + " and movCta.tipo='"+ v_tipo +"' and (movCta.estatus='"+ v_estatus1 +"' or movCta.estatus='X') and movCta.idCuentaOrigen is not null"; // ctaOri.ctaCheques is not null";
			}
				
			queryString = queryString + " order by movCta.id desc" ;
			//queryString = queryString + " and sucCta.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") order by movCta.id desc" ;	 
			
			Query query = entityManager.createQuery(queryString);
			
			if(max>0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PagosGastos> findTransferencias(String propertyName, Object value, String v_tipo, int max, String sucursales) {
		String v_where="";
		
		if(value == null)
			value="";
		
		try {
			  String queryString = "select movCta " +
								   "from PagosGastos movCta "+
								   "left join fetch movCta.idCuentaOrigen ctaOri "+
								   "left join fetch ctaOri.sucursal sucOri "+
								   "left join fetch movCta.cuentaOrigenTerceros ctaOri3 "+
								   "left join fetch movCta.cuentaDestino ctaDest "+
								   "left join fetch ctaDest.sucursal sucDest "+
								   "left join fetch movCta.cuentaDestinoTerceros ctaDest3 " +
								   " where movCta.tipo='"+ v_tipo +"' and (movCta.estatus='G' or movCta.estatus='X') ";
								  
			if("fecha".equals(propertyName) && !"".equals(value))				
				v_where="and ''''||to_char(movCta.fecha, 'yyyy-MM-dd')||''''  like '%"+ value.toString().toLowerCase() +"%' ";
			else if("cuentaOrigen".equals(propertyName)&& !"".equals(value))				
			   v_where="and (lower(ctaOri.cinsnlargo) like '%"+ value.toString().toLowerCase() +"%' or "+	
					   "lower(ctaOri.cinsncorto) like '%"+value.toString().toLowerCase()+"%' or "+
					   "lower(ctaOri.ctaBancoId) like '%"+value.toString().toLowerCase()+"%'" +
					   " or (ctaOri.ctaCheques) like '%"+value.toString().toLowerCase() +"%')";
			else if("cuentaOrigenTerceros".equals(propertyName)&& !"".equals(value))
				v_where="and (lower(ctaOri3.descripcion) like '%"+ value.toString().toLowerCase() +"%' or "+	
					   "lower(ctaOri3.valorId) like '%"+value.toString().toLowerCase()+"%' or "+
					   "lower(ctaOri3.atributo1) like '%"+value.toString().toLowerCase()+"%')";
			else if("cuentaDestino".equals(propertyName)&& !"".equals(value))	
				v_where="and (lower(ctaDest.cinsnlargo) like '%"+ value.toString().toLowerCase() +"%' or "+	
					   "lower(ctaDest.cinsncorto) like '%"+value.toString().toLowerCase()+"%' or "+
					   "lower(ctaDest.ctaBancoId) like '%"+value.toString().toLowerCase()+"%')" +
					   " or (ctaDest.ctaCheques) like '%"+value.toString().toLowerCase() +"%')";
			else if("cuentaDestinoTerceros".equals(propertyName)&& !"".equals(value))
				v_where="and (lower(ctaDest3.descripcion) like '%"+ value.toString().toLowerCase() +"%' or "+	
					   "lower(ctaDest3.valorId) like '%"+value.toString().toLowerCase()+"%' or "+
					   "lower(ctaDest3.atributo1) like '%"+value.toString().toLowerCase()+"%')";
			else if("Referencia".equals(propertyName)&& !"".equals(value))
				v_where="and (lower(movCta.folioAutorizacion) like '%"+ value.toString().toLowerCase() +"%')";
			
			queryString=queryString + v_where + " and (sucOri.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") or sucDest.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ")) order by movCta.fecha desc" ;
			
			Query query = entityManager.createQuery(queryString);
			
			if(max>0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeCajaChica(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
		
		String v_where="";
		
		if(value == null)
			value="";
		
		try {
			String queryString = "select movCta from PagosGastos movCta where movCta.tipo='"+ v_tipo +"' and (movCta.estatus='"+ v_estatus1 +"' or movCta.estatus='X') ";
				/*"select movCta from PagosGastos movCta "+
			   "left join fetch movCta.idCuentaOrigen ctaOri "+
			   "left join fetch ctaOri.idSucursal sucCta " +
			   "left join fetch ctaOri.catBancoId catBanc "+
			   "left  join fetch movCta.tiposMovtoId tipoMov "+
			   "inner join fetch movCta.idBeneficiario ben "+
			   "inner join fetch ben.relacionId rel "+
			   "left join fetch movCta.sucursal suc "+
			   "left  join fetch movCta.idCuentaDestino ctaDest "+
			   "left  join fetch movCta.cuentaDestinoTerceros ctaDest3 " +
			   "where movCta.tipo='"+ v_tipo +"' and (movCta.estatus='"+ v_estatus1 +"' or movCta.estatus='X') ";*/
			if (value != null && ! "".equals(value.toString())) {
				if("fecha".equals(propertyName))				
					v_where=" and ''''||to_char(movCta.fecha, 'yyyy-MM-dd')||''''  like '%"+ value.toString().toLowerCase() +"%' ";
				else if("beneficiario".equals(propertyName))				
				   v_where="and (lower(movCta.beneficiario) like '%"+ value.toString().toLowerCase() +"%') ";/* +
			   		   " or lower(ben.apellidoPaterno)like '%"+ value.toString().toLowerCase() +"%' " +
			   		   " or lower(ben.apellidoMaterno)like '%"+ value.toString().toLowerCase() +"%') ";*/
				else if("cuentaOrigen".equals(propertyName))
				   v_where=" and (lower(cast(movCta.idCuentaOrigen, string)) like '%"+ value.toString().toLowerCase() +"%') ";	
				   /*v_where="and (lower(cast(movCta.idCuentaOrigen, string)) like '%"+ value.toString().toLowerCase() +"%' or "+	
				   "lower(ctaOri.cinsncorto) like '%"+value.toString().toLowerCase()+"%' or "+
				   "lower(ctaOri.ctaBancoId) like '%"+value.toString().toLowerCase()+"%' or "+
				   "lower(ctaOri.ctaCheques) like '%"+value.toString().toLowerCase() + "%') ";*/
				else
					v_where="and (lower(cast(movCta." + propertyName + " as string)) like '%"+ value.toString().toLowerCase() +"%') ";
			}
			
			queryString = queryString + v_where; // + " and ((sucCta.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") and ctaOri.ctaCheques is not null)  or suc.id in (" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ")) order by movCta.pagosGastosId desc";
			queryString = queryString + " order by movCta.id desc";
			
			Query query = entityManager.createQuery(queryString);
			
			if(max>0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PagosGastos> findProvisiones(String propertyName, Object value, String estatus, String estatus2, int max, String sucursales) {
		String v_where="";
		
		if(value == null)
			value="";
		
		try {
			
			  String queryString = "select movCta " +
								   "from PagosGastos movCta "+
								   "left join fetch movCta.sucursal suc "+
								   " where movCta.tipo='A' and (movCta.estatus='"+ estatus +"' or movCta.estatus='X' "+ (estatus2 != null ? (" or movCta.estatus='"+ estatus2)+"'":"")+") " +
								   	"and (suc.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") ) " ;
								  
			if("fecha".equals(propertyName) && !"".equals(value))				
				v_where="and ''''||to_char(movCta.fecha, 'yyyy-MM-dd')||''''  like '%"+ value.toString().toLowerCase() +"%' ";
			if("sucursal".equals(propertyName) && !"".equals(value))	
				v_where=" and (lower(suc.nombre) like '%"+ value.toString().toLowerCase() +"%')";
			if(estatus2 == null)
				v_where = v_where + " and movCta.cuentaOrigen is not null";
			else
				v_where = v_where + " and movCta.cuentaOrigen is null";
			
			queryString = queryString + v_where + " order by movCta.pagosGastosId desc ";
			
			Query query = entityManager.createQuery(queryString);
			
			if(max>0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PagosGastos> findTransPorDia(Date fecha) throws Exception{
		try{
			Date fecTmp =  (Date)fecha.clone();
			Calendar fec1 = Calendar.getInstance();
			Calendar fec2 = Calendar.getInstance();
			fec1.setTime(fecTmp);
			fec1.add(Calendar.DAY_OF_MONTH, -1);
			fec2.setTime(fecTmp);
			fec2.add(Calendar.DAY_OF_MONTH, 1);
			
			String queryString ="select movCta from PagosGastos movCta " +
								  "left join fetch movCta.cuentaOrigen ctaOri "+
								   "left join fetch ctaOri.sucursal sucOri "+
								   "left join fetch movCta.cuentaOrigenTerceros ctaOri3 "+
								   "left join fetch movCta.cuentaDestino ctaDest "+
								   "left join fetch ctaDest.sucursal sucDest "+
								   "left join fetch movCta.cuentaDestinoTerceros ctaDest3 " +
								"where movCta.tipo ='T' and movCta.estatus ='G' and coalesce(movCta.nota,'')<>'S' and movCta.fechaCreacion>:fec1 and movCta.fechaCreacion<:fec2 "+
								" order by movCta.fecha, movCta.cuentaOrigen, movCta.cuentaDestino";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("fec1", fec1.getTime());
			query.setParameter("fec2", fec2.getTime());
			return query.getResultList();
		} catch(Exception e) {
			throw e;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeGtosPorComprobarPersona(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max) {
		try {
			String queryString = "select movCta from PagosGastos movCta "+
								   "where movCta.idBeneficiario = :propertyValue "+
								   //" and lower(movCta.tipoBeneficiario) like '%"+ tipoPersona.toLowerCase() +"%' "+
								   " and movCta.tipo='"+ tipo +"' and " +
								   (traer == null || "".equals(traer) ? "movCta.estatus='"+ estatus +"'" : "(movCta.estatus='C' or movCta.estatus='"+ estatus +"') ");
		
			if("R".equals(tipo) && "A".equals(estatus)){
				queryString = queryString + " and movCta.fecha <= :fecha";
			}
			queryString = queryString + " order by movCta.id desc ";
			Query query = entityManager.createQuery(queryString);
			
			if("R".equals(tipo) && "A".equals(estatus))
				query.setParameter("fecha", fecha);
			query.setParameter("propertyValue", value);
			if(max>0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {			
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findPersonaEnUso(long persona) {
		List<PagosGastos> res = null;
		try {
			final String queryString = "select model from PagosGastos model where model.idBeneficiario = :persona ";
			Query query = entityManager.createQuery(queryString);	
			query.setParameter("persona", persona);
			res = query.getResultList(); 
		} catch (RuntimeException re) {
			throw re;
		}
		
		return res;
	}
	
	public boolean existeTransferencia(long ctaOrigen, String folioAutorizacion, Date fecha){
		try {
			final String queryString = "select movCta from PagosGastos movCta " + 
				"where movCta.cuentaOrigen = :ctaOrigen and movCta.folioAutorizacion = :folioAuth and movCta.fecha = :fecha";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("ctaOrigen", ctaOrigen);
			query.setParameter("fecha", fecha);
			query.setParameter("folioAuth", folioAutorizacion);
			return !query.getResultList().isEmpty();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findCtasBancoById(String value, String v_tipo, String v_estatus1, int max) {
		
		try {
			if(value == null)
				value="";
			
			String queryString = "select movCta from PagosGastos movCta where idCuentaOrigen in (" + value + ")";
			queryString = queryString + " and movCta.tipo='"+ v_tipo +"' and (movCta.estatus='"+ v_estatus1 +"' or movCta.estatus='X')";
			queryString = queryString + " order by movCta.id desc" ; 
			
			Query query = entityManager.createQuery(queryString);
			
			if(max>0)
				query.setMaxResults(max);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PagosGastos> buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda) {
		try{
			String queryString = "select mov from PagosGastos mov " +
								"where mov.tipo = 'M' and (mov.estatus = 'G' or mov.estatus = 'X') ";
			
			if(tipoBusqueda.equals("fecha"))
				queryString += " and ''''||to_char(mov.fecha, 'yyyy-MM-dd')||'''' like '%" + valorBusqueda.toString().toLowerCase() +"%' ";
			else if (tipoBusqueda.equals("beneficiario")) 
				queryString += " and mov.beneficiario like '%" + valorBusqueda.toString() + "%' ";
			else if(tipoBusqueda.equals("idCuentaOrigen")) 
				queryString += " and mov.idCuentaOrigen = " + valorBusqueda.toString();

			queryString += " order by mov.estatus ";
			
			Query query = entityManager.createQuery(queryString);
			
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public int findConsecutivoByBeneficiario(long beneficiario, String tipo, String estatus) {
		Long consecutivo;
		
		try {
			if (tipo == null) 
				tipo = "";
			
			if (estatus == null) 
				estatus = "";
			
			final String queryString = "select (COUNT(movCta.idBeneficiario) + 1) from PagosGastos movCta " + 
				"where movCta.idBeneficiario = :propertyValue and movCta.tipo = '" + tipo + "' and movCta.estatus = '" + estatus + "'";
			
			Query query = entityManager.createQuery(queryString, Long.class);
			query.setParameter("propertyValue", beneficiario);
			
			consecutivo = (Long) query.getSingleResult();
			
			if (consecutivo == null)
				consecutivo = 0L;
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatus = null;
			orderBy = null;
		}
		
		return consecutivo.intValue();
	}
	
	/*public PagosGastos actualizar(PagosGastos entity, String estatus,Date fech_modificacion,Short usuario, Long empresa) throws Exception {
		try {
			if(ctx == null)
				ctx = new InitialContext();
			utx.begin();
			
			if(ifzMessage == null){
				lookup = ctx.lookup("sendMessage/local");
				ifzMessage = (sendMessageFacadeLocal)PortableRemoteObject.narrow(lookup,sendMessageFacadeLocal.class);
			}
			
			entity.setModificadoPor(usuario);
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus(estatus);
			
			//entityManager.merge(entity);
			utx.commit();
			
			// envio de mensaje para la contabilidad
			utx.begin();
			ifzMessage.enviar(entity, empresa != null ? empresa : findEmpresa(entity).getClave());
			utx.commit();
			
			return entity;
		} catch (RuntimeException re) {
			log.error("error metodo actualizar", re);
			try {
				utx.rollback();
			} catch (Exception e) {
				log.error("error en rollback de transaccion en metodo actualizar", e);
			}
			throw re;	
		}
	}

	/*public Respuesta salvar(PagosGastos entity, boolean band, Long empresa) throws Exception{
		Respuesta reg = new Respuesta();

		try{
			reg = salvaPojo(entity, band);
			if((reg.getResultado() == 0 && !"C".equals(entity.getTipo()) && !"P".equals(entity.getTipo())) || (reg.getResultado() == 0 && "C".equals(entity.getTipo()) && "C".equals(entity.getOperacion())))
				reg = mensajeSaldoCtas((PagosGastos)reg.getObjeto(), empresa);
		}catch(RuntimeException re){
			log.error("error en metodo salvar", re);
			throw re;
		}

		return reg;
	}
	
	public Respuesta salvaPojo(PagosGastos entity, boolean band) throws Exception{
		Respuesta reg = new Respuesta();
		List<Object> resultadoSQL = new ArrayList<Object>();
		String control="";

		try {
			if(ctx == null)
				ctx = new InitialContext();
			utx.begin();
			
			if("C".equals(entity.getOperacion()) || "2".equals(entity.getOperacion()) ){//si la operacion del gasto es con cheque se inserta aqui mismo el cheque
				if(ifzEditorSQL == null){
					 lookup =  ctx.lookup("NQueryFacade/local");
					 ifzEditorSQL = (NQueryFacadeLocal) PortableRemoteObject.narrow(lookup, NQueryFacadeLocal.class);
				}
				//validando folio cheque				
				//parametros enviados:  select cancela_cheque(id cta bancaria, folio de cheque, true SI cancela folio, Id usuario)
				resultadoSQL =  ifzEditorSQL.findNativeQuery("select cancela_cheques("+entity.getCuentaOrigen().getCtaBancoId()+","+entity.getNoCheque() +","+ band +","+  entity.getCreadoPor() +")");
				
				if(! resultadoSQL.isEmpty()){
					for(Object var: resultadoSQL){
						if(! "BIEN".equals(var)){
							reg.setResultado(-1);
							reg.setRespuesta(var.toString());
							utx.commit();
							return reg;
						}
					}
				}
				
				Cheques pojoCheque = new Cheques();
				
				if(ifzCheques == null){
					lookup = ctx.lookup("ChequesFacade/local");
					ifzCheques = (ChequesFacadeLocal) PortableRemoteObject.narrow(lookup, ChequesFacadeLocal.class);
				}
				
				//insertando el cheque 
				 pojoCheque.setBancoId(entity.getCuentaOrigen());
				 control = String.format( "%06d", entity.getNoCheque())+ "-" + entity.getCuentaOrigen().getSucursal().getNombre();
				 pojoCheque.setControl(control.length() > 17 ? control.substring(0,17):control);
				 pojoCheque.setMinistracion(0);
				 pojoCheque.setEstatus("E");
				 pojoCheque.setTipo("T");
				 pojoCheque.setFecha(entity.getFecha());
				 pojoCheque.setImporte(Float.valueOf(String.valueOf( entity.getMonto())));
				 pojoCheque.setFolio(String.valueOf( entity.getNoCheque()));
				 pojoCheque.setCreadoPor(Short.valueOf(String.valueOf( entity.getCreadoPor())));
				 pojoCheque.setFechaCreacion(Calendar.getInstance().getTime());
				 pojoCheque.setModificadoPor(Short.valueOf(String.valueOf( entity.getModificadoPor())));
				 pojoCheque.setFechaModificacion(Calendar.getInstance().getTime());
				 ifzCheques.save(pojoCheque);
			}
						
			//entityManager.persist(entity);
			reg.setObjeto(entity);
			reg.setResultado(0);
			reg.setReferencia(String.valueOf(entity.getPagosGastosId()));
			
			utx.commit();
		} catch (RuntimeException re) {
			log.error("error en metodo salvaPojo", re);
			try {
				utx.rollback();
			} catch (Exception e) {
				log.error("error en rollback de transaccion en metodo salvaPojo", e);
			}
			throw re;
		}
			
		return reg;
	}
	
	public Respuesta cancelacion(PagosGastos entity, Date fech_modificacion,Short usuario, Long empresa) throws Exception{
		try{
			Respuesta reg = new Respuesta();
			reg = cancelacionPojo(entity, fech_modificacion, usuario);
			if(reg.getResultado() == 0)
				reg = mensajeSaldoCtas((PagosGastos)reg.getObjeto(), empresa);
			return reg;
		}catch(RuntimeException re){
			log.error("error en metodo cancelacion", re);
			throw re;
		}
	}
	
	public Respuesta cancelacionPojo(PagosGastos entity, Date fech_modificacion,Short usuario) throws Exception{
		List<Object> resultadoSQL = new ArrayList<Object>();
		try {
			if(ctx == null)
				ctx = new InitialContext();
			utx.begin();
			
			if(ifzMessage == null){
				lookup = ctx.lookup("sendMessage/local");
				ifzMessage = (sendMessageFacadeLocal)PortableRemoteObject.narrow(lookup,sendMessageFacadeLocal.class);
			}
			
			if (ifzBancosOperaciones == null){
				this.lookup = this.ctx.lookup("CtasBancoOperaciones/local");		
				this.ifzBancosOperaciones = (CtasBancoOperacionesLocal)PortableRemoteObject.narrow(this.lookup,CtasBancoOperacionesLocal.class);
			}
			
			Respuesta reg = new Respuesta();
			
			entity.setModificadoPor(usuario);
			entity.setFechaModificacion(fech_modificacion);
			entity.setEstatus("X");
			
			if("C".equals(entity.getOperacion()) || "2".equals(entity.getOperacion())){//si la operacion del gasto es con cheque se CANCELA
				if(ifzEditorSQL == null){
					 lookup =  ctx.lookup("NQueryFacade/local");
					 ifzEditorSQL = (NQueryFacadeLocal) PortableRemoteObject.narrow(lookup, NQueryFacadeLocal.class);
				}
														
				//revisando el estatus del cheque...solo los E se pueden cancelar
				resultadoSQL =  ifzEditorSQL.findNativeQuery("SELECT estatus from cheques where folio="+entity.getNoCheque()+" and banco_id="+entity.getCuentaOrigen().getCtaBancoId()+" and tipo='T' ");
				
				if(! resultadoSQL.isEmpty()){
					if("E".equals(resultadoSQL.get(0).toString())){
						Cheques pojoCheque = new Cheques();;
						
						if(ifzCheques == null){
							lookup = ctx.lookup("ChequesFacade/local");
							ifzCheques = (ChequesFacadeLocal) PortableRemoteObject.narrow(lookup, ChequesFacadeLocal.class);
						}
						
						pojoCheque =ifzCheques.findChequeCompleto(String.valueOf(entity.getNoCheque()),entity.getCuentaOrigen().getCtaBancoId(),"T","E");
						
						pojoCheque.setEstatus("C");
						pojoCheque.setModificadoPor(usuario);
						pojoCheque.setFechaModificacion(fech_modificacion);
						
						pojoCheque = ifzCheques.update(pojoCheque);
						
					}
					else{
						if("P".equals(resultadoSQL.get(0).toString())){
							reg.setRespuesta("No es posible cancelar este gasto porque el cheque ha sido cobrado!");
							reg.setResultado(-1);
							utx.commit();
							return reg;
						}
						else{
							if("C".equals(resultadoSQL.get(0).toString())){
								reg.setRespuesta("Error: El estatus del cheque del gasto esta CANCELADO y el gasto se encuentra VIGENTE.");
								reg.setResultado(-1);
								utx.commit();
								return reg;
							}
						}
							
					}
						
				}					
			}
				
			//entityManager.merge(entity);			
			reg.setObjeto(entity);
			reg.setResultado(0);
			utx.commit();			
			return reg;
			
		} catch (RuntimeException re) {	
			log.error("error en metodo cancelacion", re);
			try {
				utx.rollback();
			} catch (Exception e) {
				log.error("error en rollback de transaccion en metodo cancelacion", e);
			}
			throw re;		
		}
	}
	
	public Respuesta mensajeSaldoCtas(PagosGastos entity, Long empId) throws Exception{
		Respuesta reg = new Respuesta();

		try {
			if(ctx == null)
				ctx = new InitialContext();
			utx.begin();
			if(ifzMessage == null){
				lookup = ctx.lookup("sendMessage/local");
				ifzMessage = (sendMessageFacadeLocal)PortableRemoteObject.narrow(lookup,sendMessageFacadeLocal.class);
			}
			
			if (ifzBancosOperaciones == null){
				this.lookup = this.ctx.lookup("CtasBancoOperaciones/local");		
				this.ifzBancosOperaciones = (CtasBancoOperacionesLocal)PortableRemoteObject.narrow(this.lookup,CtasBancoOperacionesLocal.class);
			}
			//envio mensaje para la contabilidad
			if(!"T".equals(entity.getTipo()))
				ifzMessage.enviar(entity, empId != null ? empId : entity.getCuentaOrigen().getEmpresa().getClave());
			
			reg.setObjeto(entity);
			reg.setResultado(0);
			reg.setReferencia(String.valueOf(entity.getPagosGastosId()));
			utx.commit();
			return reg;
		} catch (RuntimeException re){
			log.error("error en metodo mensajeSaldoCtas", re);
			try {
				utx.rollback();
			} catch (Exception e) {
				log.error("error en rollback de transaccion en metodo mensajeSaldoCtas", e);
			}
			throw re;
		}
	}

	public boolean findPersonaEnUso(Persona persona) {
		List<PagosGastos> res = null;
		try {
			final String queryString = "select model from pagos_gastos model " +
				" inner join model.noBeneficiario movto " +
				"where movto = :persona";
			Query query = entityManager.createQuery(queryString);	
			query.setParameter("persona", persona);
			res = query.getResultList(); 
		} catch (RuntimeException re) {
			throw re;
		}
		return res == null || res.isEmpty();
	}

	public boolean existeTransferencia(CtasBanco ctaOrigen, String folioAutorizacion, Date fecha){
		try {
			final String queryString = "select movCta from pagos_gastos movCta " + 
				"where movCta.cuentaOrigen = :ctaOrigen and movCta.folioAutorizacion = :folioAuth and movCta.fecha = :fecha";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("ctaOrigen", ctaOrigen);
			query.setParameter("fecha", fecha);
			query.setParameter("folioAuth", folioAutorizacion);
			return !query.getResultList().isEmpty();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	*/
	/*public CatEmpresas findEmpresa(PagosGastos movCta){
		List<CatEmpresas> listEmp = null;
		try {
			final String queryString = "select emp " +
				"from pagos_gastos movCta inner join movCta.cuentaOrigen.empresa emp where movCta = :cta";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("cta", movCta);
			listEmp = query.getResultList();
			return listEmp != null ? listEmp.get(0) : null;
		} catch (RuntimeException re) {
			throw re;
		}
	}*/
}
