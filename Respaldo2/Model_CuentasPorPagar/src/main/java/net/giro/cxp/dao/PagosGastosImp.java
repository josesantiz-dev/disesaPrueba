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
	//private String queryString;
	//private String whereString;
	private static String orderBy;
	private static Long estatus;
	private Long idEmpresa;

	@Override
	public void orderBy(String orderBy) {
		PagosGastosImp.orderBy = orderBy;
	}

	@Override
	public void estatus(Long estatus) {
		PagosGastosImp.estatus = estatus;
	}
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(PagosGastos entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<PagosGastos> saveOrUpdateList(List<PagosGastos> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public PagosGastos findAllById(Long id) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa and model.id=:id order by model.id desc";
		PagosGastos model = null;
			
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("id", id);
			model = (PagosGastos) query.getSingleResult(); 
			return model;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findByProperty(String propertyName, Object value, int max) throws Exception {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null) {
				queryString += "and model."+ propertyName + " = :propertyValue ";
			}
			
			if (estatus != null)
				queryString += " and estatus = " + estatus;
			estatus = null;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		StringBuffer sb = null;
		
		try {
			if (value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || (propertyName.startsWith("id") && Character.isUpperCase(propertyName.charAt(2)))) {
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					queryString += " and lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if (estatus != null)
				queryString += " and estatus = " + estatus;
			estatus = null;
					
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
        	query.setParameter("idEmpresa", getIdEmpresa());
        	if (values != null && ! values.isEmpty()) {
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
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			for(Entry<String, Object> e : params.entrySet()) {
				if (e.getValue().getClass() == java.util.Date.class) 
					queryString += "and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					queryString += "and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			if (estatus != null)
				queryString += " and estatus = " + estatus;
			estatus = null;
			
			if (orderBy != null && !"".equals(orderBy)) 
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			for(Entry<String, String> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " or ";
				whereString += "cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%' ";
			}
			
			if (! whereString.isEmpty())
				queryString += " and (" + whereString + ") ";
			
			if (estatus != null && whereString.length() > 0)
				whereString += "and estatus = " + estatus;
			else if (estatus != null && whereString.length() == 0)
				whereString += "and estatus = " + estatus;
			estatus = null;
			if (orderBy != null && !"".equals(orderBy)) {
				queryString += " order by " + orderBy;
				orderBy = "";
			}

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	public List<PagosGastos> findLikeBenefTipoPersona(String beneficiario, String tipoPer, String tipo, String estatus, int max, String sucursales) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (beneficiario == null || "".equals(beneficiario))
				beneficiario = "";
			else
				beneficiario = "lower(model.beneficiario) like  '%"+ beneficiario.toLowerCase() +"%' and ";
			
			if (tipoPer == null || "".equals(tipoPer))
				tipoPer = "";
			else
				tipoPer = " model.tipoBeneficiario = '" + tipoPer + "' and ";
			
			if (estatus == null)
				estatus = "";
			
			if ("pago".equals(estatus))
				estatus = " and (model.estatus = 'X' or model.estatus = 'C') "; 
			else
				estatus = " and model.estatus = '" + estatus + "'";
			
			queryString += beneficiario + tipoPer + " model.tipo='"+ tipo + "' " + estatus +" order by model.id desc" ;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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

	@SuppressWarnings("unchecked")
	private String getListOfGastosByBeneficiario(String beneficiario, String tipoPer, String tipo, String estatus, Date fecha, String sucursales) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		String listaGastos = "";
		String listaBeneficiarios = "";
		
		try {
			if (beneficiario == null || "".equals(beneficiario))
				 beneficiario = "";
			else{
				beneficiario = "lower(model.beneficiario) like  '%"+ beneficiario.toLowerCase() +"%' and ";
			}
			
			if (tipoPer == null || "".equals(tipoPer))
				tipoPer = "";
			else
				tipoPer = " model.tipoBeneficiario = '" + tipoPer + "' and ";
			
			if (estatus == null)
				estatus = "";
			
			if ("pago".equals(estatus))
				estatus = " and (model.estatus = 'X' or model.estatus = 'C') "; 
			else
				estatus = " and model.estatus in (" + estatus + ")";
			
			queryString += "and " + beneficiario + tipoPer + " model.tipo='"+ tipo + "' " + estatus;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikePersonasConGastos(String beneficiario, String tipoPer, String tipo, String estatus, int max, Date fecha, String sucursales) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			String filtro = getListOfGastosByBeneficiario(beneficiario, tipoPer, tipo, estatus, fecha, sucursales);
			if ("".equals(filtro))
				filtro = "and model.id <> 0";
			else
				filtro = "and model.id in (" + filtro + ")";
			
			queryString += filtro + " order by model.id desc" ;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	public List<PagosGastos> findLikeGtosComprobar(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
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
			query.setParameter("idEmpresa", getIdEmpresa());
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
	public List<PagosGastos> findLikeMovCtas(String propertyName,Object value,String v_tipo,String v_estatus1,int max,String sucursales) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		String v_where="";
		
		try {
			if (value == null)
				value = "";
			
			if ("fecha".equals(propertyName)) {
				v_where="and ''''||to_char(model.fecha, 'yyyy-MM-dd')||''''  like '%"+ value.toString().toLowerCase() +"%' ";		
			} else if ("idCuentaOrigen".equals(propertyName)) {
				v_where="and model.idCuentaOrigen in (" + value.toString().toLowerCase() + ") ";
			} else if ("idTiposMovimiento".equals(propertyName)) {
				v_where="and model.idTiposMovimiento in (" + value.toString().toLowerCase() + ") ";
			}
				
			if ("".equals(value)) {
				queryString = queryString + " and model.tipo='"+ v_tipo +"' and (model.estatus='"+ v_estatus1 +"' or model.estatus='X') and model.idCuentaOrigen is not null"; // ctaOri.ctaCheques is not null";
			} else {
				queryString = queryString + v_where + " and model.tipo='"+ v_tipo +"' and (model.estatus='"+ v_estatus1 +"' or model.estatus='X') and model.idCuentaOrigen is not null"; // ctaOri.ctaCheques is not null";
			}
				
			queryString = queryString + " order by model.id desc" ;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (max>0)
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
	public List<PagosGastos> findTransferencias(String propertyName, Object value, String v_tipo, int max, String sucursales) {
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
			query.setParameter("idEmpresa", getIdEmpresa());
			if (max>0)
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
	public List<PagosGastos> findLikeCajaChica(String propertyName, Object value, String v_tipo, String v_estatus1, int max, String sucursales) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa and model.tipo='"+ v_tipo +"' and (model.estatus='"+ v_estatus1 +"' or model.estatus='X') ";
		String v_where="";
		
		if (value == null)
			value="";
		
		try {
			if (value != null && ! "".equals(value.toString())) {
				if ("fecha".equals(propertyName))				
					v_where=" and ''''||to_char(model.fecha, 'yyyy-MM-dd')||''''  like '%"+ value.toString().toLowerCase() +"%' ";
				else if ("beneficiario".equals(propertyName))				
				   v_where="and (lower(model.beneficiario) like '%"+ value.toString().toLowerCase() +"%') ";
				else if ("cuentaOrigen".equals(propertyName))
				   v_where=" and (lower(cast(model.idCuentaOrigen, string)) like '%"+ value.toString().toLowerCase() +"%') ";
				else
					v_where="and (lower(cast(model." + propertyName + " as string)) like '%"+ value.toString().toLowerCase() +"%') ";
			}
			
			queryString = queryString + v_where; 
			queryString = queryString + " order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (max>0)
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
	public List<PagosGastos> findProvisiones(String propertyName, Object value, String estatus, String estatus2, int max, String sucursales) {
		String queryString = "select model from PagosGastos model ";
		String v_where="";
		
		if (value == null)
			value="";
		
		try {
			queryString += "left join fetch model.sucursal suc "+
								   " where model.idEmpresa = :idEmpresa and model.tipo='A' and (model.estatus='"+ estatus +"' or model.estatus='X' "+ (estatus2 != null ? (" or model.estatus='"+ estatus2)+"'":"")+") " +
								   	"and (suc.id in(" + (sucursales != null && !"".equals(sucursales) ? sucursales :"-1") + ") ) " ;
								  
			if ("fecha".equals(propertyName) && !"".equals(value))				
				v_where="and ''''||to_char(model.fecha, 'yyyy-MM-dd')||''''  like '%"+ value.toString().toLowerCase() +"%' ";
			if ("sucursal".equals(propertyName) && !"".equals(value))	
				v_where=" and (lower(suc.nombre) like '%"+ value.toString().toLowerCase() +"%')";
			if (estatus2 == null)
				v_where = v_where + " and model.cuentaOrigen is not null";
			else
				v_where = v_where + " and model.cuentaOrigen is null";
			queryString = queryString + v_where + " order by model.pagosGastosId desc ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (max>0)
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
	public List<PagosGastos> findTransPorDia(Date fecha) throws Exception{
		String queryString = "select model from PagosGastos model ";
		
		try{
			Date fecTmp =  (Date)fecha.clone();
			Calendar fec1 = Calendar.getInstance();
			Calendar fec2 = Calendar.getInstance();
			fec1.setTime(fecTmp);
			fec1.add(Calendar.DAY_OF_MONTH, -1);
			fec2.setTime(fecTmp);
			fec2.add(Calendar.DAY_OF_MONTH, 1);
			
			queryString += "left join fetch model.cuentaOrigen ctaOri "+
								   "left join fetch ctaOri.sucursal sucOri "+
								   "left join fetch model.cuentaOrigenTerceros ctaOri3 "+
								   "left join fetch model.cuentaDestino ctaDest "+
								   "left join fetch ctaDest.sucursal sucDest "+
								   "left join fetch model.cuentaDestinoTerceros ctaDest3 " +
								"where model.idEmpresa = :idEmpresa and model.tipo ='T' and model.estatus ='G' and coalesce(model.nota,'')<>'S' and model.fechaCreacion>:fec1 and model.fechaCreacion<:fec2 "+
								" order by model.fecha, model.cuentaOrigen, model.cuentaDestino";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findLikeGtosPorComprobarPersona(Long value, Long suc, String tipo, String estatus, String tipoPersona, Date fecha, String traer, int max) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			queryString += "and model.idBeneficiario = :propertyValue  and model.tipo='"+ tipo +"' and " +
								   (traer == null || "".equals(traer) ? "model.estatus='"+ estatus +"'" : "(model.estatus='C' or model.estatus='"+ estatus +"') ");
		
			if ("R".equals(tipo) && "A".equals(estatus)) {
				queryString = queryString + " and model.fecha <= :fecha";
			}
			queryString = queryString + " order by model.id desc ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if ("R".equals(tipo) && "A".equals(estatus))
				query.setParameter("fecha", fecha);
			query.setParameter("propertyValue", value);
			if (max>0)
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
	public List<PagosGastos> findPersonaEnUso(long persona) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		List<PagosGastos> res = null;
		
		try {
			queryString += "and model.idBeneficiario = :persona ";
			Query query = entityManager.createQuery(queryString);	
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("persona", persona);
			res = query.getResultList(); 
		} catch (RuntimeException re) {
			throw re;
		}
		
		return res;
	}

	@Override
	public boolean existeTransferencia(long ctaOrigen, String folioAutorizacion, Date fecha) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			queryString += "and model.cuentaOrigen = :ctaOrigen and model.folioAutorizacion = :folioAuth and model.fecha = :fecha";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("ctaOrigen", ctaOrigen);
			query.setParameter("fecha", fecha);
			query.setParameter("folioAuth", folioAutorizacion);
			return !query.getResultList().isEmpty();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> findCtasBancoById(String value, String v_tipo, String v_estatus1, int max) {
		String queryString = "select model from PagosGastos model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value == null)
				value="";
			
			queryString += "and idCuentaOrigen in (" + value + ")";
			queryString = queryString + " and model.tipo='"+ v_tipo +"' and (model.estatus='"+ v_estatus1 +"' or model.estatus='X')";
			queryString = queryString + " order by model.id desc" ; 
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (max>0)
				query.setMaxResults(max);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PagosGastos> buscarMovimientosCuentas(String tipoBusqueda, Object valorBusqueda) {
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
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public int findConsecutivoByBeneficiario(long beneficiario, String tipo, String estatus) {
		String queryString = "select (COUNT(model.idBeneficiario) + 1) from PagosGastos model where model.idEmpresa = :idEmpresa ";
		Long consecutivo;
		
		try {
			if (tipo == null) 
				tipo = "";
			
			if (estatus == null) 
				estatus = "";
			
			queryString += "and model.idBeneficiario = :propertyValue and model.tipo = '" + tipo + "' and model.estatus = '" + estatus + "'";
			
			Query query = entityManager.createQuery(queryString, Long.class);
			query.setParameter("idEmpresa", getIdEmpresa());
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
}
