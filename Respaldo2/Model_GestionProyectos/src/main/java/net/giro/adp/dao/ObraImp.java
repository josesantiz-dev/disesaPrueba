package net.giro.adp.dao;

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

import net.giro.adp.beans.Obra;

@Stateless
public class ObraImp extends DAOImpl<Obra> implements ObraDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private static String orderBy;
	private static Long estatusId;
	
	@Override
	public void orderBy(String orderBy) { 
		ObraImp.orderBy = orderBy; 
	}

	@Override
	public void estatus(Long estatus) {
		ObraImp.estatusId = estatus;
	}

	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(Obra entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<Obra> saveOrUpdateList(List<Obra> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findAll() {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (estatusId != null) {
				if (estatusId > 0L)
					queryString += "and model.estatus = abs(" + estatusId.toString() + ")";
				else
					queryString += "and model.estatus <> abs(" + estatusId.toString() + ")";
				estatusId = null;
			}
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			queryString += " order by model.nombre";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findBy(Object value, boolean incluyeAdministrativas, String orderBy, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					whereString += "and date(model.fechaIngreso) = date('" + formateador.format((Date) value) + "') ";
				} else if (value.getClass() == java.lang.Long.class) {
					whereString += " cast(model.id as string) = '" + value.toString() + "' ";
				} else if (value.getClass() == java.math.BigDecimal.class) {
					whereString += "and cast(model.montoContratado as string) = '" + value.toString() + "' ";
				} else {
					whereString += "and lower(model.nombre) = lower('" + value.toString() + "') ";
				}
			}
			
			if (! incluyeAdministrativas)
				whereString += " and model.tipoObra <> 4 ";
			queryString += whereString;
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findByProperty(String propertyName, final Object value, int opcion) {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if(value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || "idobra".equals(propertyName.toLowerCase())) {
					whereString = "and cast(model."+ propertyName + " as string) = :propertyValue";
				} else {
					whereString = "and lower(cast(model."+ propertyName + " as string)) = :propertyValue";
				}
			}
			
			if (opcion > 0)
				whereString += " and model.tipoObra = :tipoObra ";
			else
				whereString += " and model.tipoObra <> 4 ";
			
			if(estatusId != null && whereString.trim().length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			else if(estatusId != null && whereString.trim().length() == 0)
				whereString = "model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			queryString += whereString;
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if(opcion > 0)
				query.setParameter("tipoObra", opcion);
			if (value != null)
				query.setParameter("propertyValue", value.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findLike(String value, boolean incluyeAdministrativas, String orderBy, int limite) throws Exception {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa and estatus > 0 ";
		StringBuffer sb = null;
		
		try {
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue "
						+ "or cast(model.idObraPrincipal as string) like :propertyValue "
						+ "or cast(model.idCliente as string) like :propertyValue "
						+ "or cast(model.idSucursal as string) like :propertyValue "
						+ "or cast(model.idResponsable as string) like :propertyValue "
						+ "or cast(model.idMoneda as string) like :propertyValue "
						+ "or lower(trim(model.nombreCliente)) like :propertyValue "
						+ "or lower(trim(model.tipoCliente)) like :propertyValue "
						+ "or lower(trim(model.nombre)) like :propertyValue "
						+ "or lower(trim(model.domicilio)) like :propertyValue "
						+ "or lower(trim(model.nombreContrato)) like :propertyValue "
						+ "or lower(trim(model.objetoContrato)) like :propertyValue "
						+ "or lower(trim(model.observaciones)) like :propertyValue "
						+ "or lower(trim(model.nombreResponsable)) like :propertyValue "
						+ "or lower(trim(model.nombreObraPrincipal)) like :propertyValue "
						+ "or lower(trim(model.descripcionMoneda)) like :propertyValue) ";
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.trim().toLowerCase());
	    		sb.append("%");
			}
			
			if (! incluyeAdministrativas)
				queryString += "and model.tipoObra <> 4 ";
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by nombre";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	public List<Obra> findLikeProperty(String propertyName, final String value, int opcion) {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if(value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || "idobra".equals(propertyName.toLowerCase())) {
					whereString = " and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					whereString = " and lower(cast(model."+ propertyName + " as string)) LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if(opcion > 0)
				whereString += " and model.tipoObra = :tipoObra ";
			else
				whereString += " and model.tipoObra <> 4 ";
			
			if(estatusId != null && whereString.trim().length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			else if(estatusId != null && whereString.trim().length() == 0)
				whereString = "  and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			queryString += whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
						
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (opcion > 0)
				query.setParameter("tipoObra", opcion);
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findLikeProperty(String propertyName, String value, boolean incluyeAdministrativas) throws Exception {

		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		StringBuffer sb = null;
		
		try {
			if(value != null && ! "".equals(value.toString())) {
				if ("id".equals(propertyName) || "idobra".equals(propertyName.toLowerCase())) {
					whereString = " and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
				} else {
					whereString = " and lower(cast(model."+ propertyName + " as string)) LIKE :propertyValue ";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if(! incluyeAdministrativas)
				whereString += " and model.tipoObra <> 4 ";
			
			if(estatusId != null && whereString.trim().length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ") ";
			else if(estatusId != null && whereString.trim().length() == 0)
				whereString = " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ") ";
			queryString += whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by " + propertyName;
						
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	@Override
	public List<Obra> findByPropertyPojoCompleto(String propertyName, String tipo, Object value) {
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findObraPrincipalByProperty(String propertyName, Object value, int tipoObra, Long idObra) {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		
		try {
			if(propertyName.toLowerCase().equals("idobra")) {
				queryString = queryString + " and (model.idObraPrincipal = null or model.idObraPrincipal <= 0) and model.id <> :idObraPrincipal and model.id = " + value + " and model.tipoObra = :tipoObra";
			} else {
				queryString = queryString + " and (model.idObraPrincipal = null or model.idObraPrincipal <= 0) and model.id <> :idObraPrincipal and model."+ propertyName + " = :propertyValue and model.tipoObra = :tipoObra";
			}
			
			if(tipoObra != 0)
				queryString += " and model.tipoObra = :tipoObra ";
			else
				queryString += " and model.tipoObra <> 4 ";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("idObraPrincipal", idObra);
			if (value != null)
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findObraPrincipalLikeProperty(String propertyName, Object value, int tipoObra, Long idObra) {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa and (model.idObraPrincipal = null or model.idObraPrincipal <= 0) and model.id <> :idObraPrincipal ";
		StringBuffer sb = null;
		
		try {
			if (idObra == null)
				idObra = 0L;
			
			if(value != null && !"".equals(value.toString())) {
				if ("id".equals(propertyName) || "idobra".equals(propertyName.toLowerCase())) {
					queryString += " and cast(model."+ propertyName + " as string) LIKE :propertyValue";
				} else {
					queryString += " and lower(model."+ propertyName + ") LIKE :propertyValue";
				}
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toString().toLowerCase());
	    		sb.append("%");
			}
			
			if(tipoObra != 0)
				queryString += " and model.tipoObra = :tipoObra ";
			else
				queryString += " and model.tipoObra <> 4 ";
			
			if(estatusId != null)
				queryString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("idObraPrincipal", idObra);
			if(tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			if ((value != null && !"".equals(value.toString())) && ! "idobra".equals(propertyName.toLowerCase()))
				query.setParameter("propertyValue", sb.toString());
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}

	/** OPCIONES PARA KEY: [&, |]KEY[!, ?, >, <] 
	 * !, ?, >, <.         Representan los operadores SQL <>, IN, >, < respectivamente. Ejemplo: campo! --> campo <> valor
	 * &, |.               Representan AND y OR respectivamente. Ejemplo: |campo --> OR campo = valor
	 * ejemplo combinado : |campo! --> OR campo <> valor
	 * DEFAULT: []campo[] --> campo = valor
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findByProperties(HashMap<String, Object> params, int tipoObra) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		String operador = "";
		String union = "";
		String key = "";
		
		try {
			for(Entry<String, Object> e : params.entrySet()) {
				// Determina operador
				operador = determinaOperador(e.getKey(), false);
				// Determina union
				union = determinaUnion(e.getKey());
				// Determina key
				key = determinaKey(e.getKey());
					
				if (whereString.length() > 0)
					whereString += union;
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += "and date(model." + key + ") " + operador + " date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += "and cast(model." + key + " as string) " + operador + " '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					whereString += "and cast(model." + key + " as string) " + operador + " '" + e.getValue().toString() + "'";
			}
			
			if(estatusId != null && whereString.length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ") ";
			else if(estatusId != null && whereString.length() == 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ") ";
			
			if (tipoObra != 0)
				whereString += " and model.tipoObra = :tipoObra ";
			else
				whereString += " and model.tipoObra <> 4 ";
						
			if (! whereString.isEmpty())
				queryString += whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.nombre";

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if(tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	/** OPCIONES PARA KEY: [&,|]KEY[!] 
	 * sufijos           : !, vacio. Representa NOT LIKE o LIKE respectivamente
	 * prefijos          : &, |. Representan AND y OR respectivamente. Ejemplo: |campo --> OR campo LIKE valor
	 * ejemplo combinado : |campo! --> OR campo NOT LIKE valor
	 * DEFAULT: []campo[] --> campo LIKE valor
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findLikeProperties(HashMap<String, String> params, int tipoObra) throws Exception {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		String operador = "";
		String union = "";
		String key = "";
		
		try {
			for (Entry<String, String> e : params.entrySet()) {
				// Determina operador
				operador = determinaOperador(e.getKey(), true);
				// Determina union
				union = determinaUnion(e.getKey());
				// Determina key
				key = determinaKey(e.getKey());
				
				if (whereString.length() > 0)
					whereString += union;
				if (IsNumber(e.getValue()) && e.getValue().contains("-"))
					whereString += "and cast(model." + key + " as string) " + operador + " '%" + e.getValue().replace("-", "") + "%' ";
				else
					whereString += "and cast(model." + key + " as string) " + operador + " '%" + e.getValue() + "%' ";
			}
			
			if(estatusId != null && whereString.length() > 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ") ";
			else if(estatusId != null && whereString.length() == 0)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ") ";
			
			if(tipoObra != 0)
				whereString += " and model.tipoObra = :tipoObra ";
			else
				whereString += " and model.tipoObra <> 4 ";
			
			if (! whereString.isEmpty())
				queryString += whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			else
				queryString += " order by model.nombre";

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if(tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findByMultiProperties(HashMap<String, Object> params, String unionProps, int tipoObra, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (unionProps == null || "".equals(unionProps))
				unionProps = "and";
			
			for(Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " " + unionProps;
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += " and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					whereString += " and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			if(estatusId != null)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ") ";
			
			if(tipoObra > 0)
				whereString += " and model.tipoObra = :tipoObra ";
			else
				whereString += " and model.tipoObra <> 4 ";
						
			if (! whereString.isEmpty())
				queryString += whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if(tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findLikeMultiProperties(HashMap<String, String> params, String unionProps, int tipoObra, int limite) throws Exception {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (unionProps == null || "".equals(unionProps))
				unionProps = "and";
			
			for(Entry<String, String> e : params.entrySet()){
				if (whereString.length() > 0)
					whereString += " " + unionProps;
				if (IsNumber(e.getValue()) && e.getValue().contains("-"))
					whereString += "and cast(model." + e.getKey() + " as string) not like '%" + e.getValue().replace("-", "") + "%'";
				else
					whereString += "and cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if(estatusId != null)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			
			if(tipoObra > 0)
				whereString += " and model.tipoObra = :tipoObra ";
			else
				whereString += " and model.tipoObra <> 4 ";
			
			if (! whereString.isEmpty())
				queryString += whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if(tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		} finally {
			estatusId = null;
			orderBy = null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findInProperty(String propertyName, List<Object> values) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		String inFilter = "";
		Object objValue = null;
		
		try {
			if (values != null && ! values.isEmpty()) {
				objValue = values.get(0);
				
				// Agrupo los valores
				for (int index = 0; index < values.size(); index++) {
					if (! "".equals(inFilter)) 
						inFilter += ",";
					
					if (objValue.getClass() == java.math.BigDecimal.class) {
						if ("".equals(whereString))
							whereString = "and model." + propertyName + " IN ";
						inFilter += values.get(index).toString();
					} else if (objValue.getClass() == java.lang.Long.class) {
						if ("".equals(whereString))
							whereString = "and model." + propertyName + " IN ";
						inFilter += values.get(index).toString();
					} else if (objValue.getClass() == java.lang.Integer.class) {
						if ("".equals(whereString))
							whereString = "and model." + propertyName + " IN ";
						inFilter += values.get(index).toString();
					} else if (objValue.getClass() == java.util.Date.class) {
						if ("".equals(whereString))
							whereString = "and date(model." + propertyName + ") IN ";
						inFilter += " date('" + formateador.format((Date) values.get(index)) + "')";
					} else {
						if ("".equals(whereString))
							whereString = "and lower(cast(model." + propertyName + " as string)) IN ";
						inFilter += "'" + values.get(index).toString() + "'";
					}
				}
				
				// Genero la clausula WHERE
				whereString = whereString + "(" + inFilter + ") ";
			}
			
			if(estatusId != null)
				whereString += " and model.estatus" + ((estatusId > 0L) ? " = " : " <> ") + "abs(" + estatusId.toString() + ")";
			
			if (! whereString.isEmpty())
				queryString += whereString;
			
			if (orderBy != null && !"".equals(orderBy))
				queryString += " order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Obra> findPrincipalBy(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "SELECT model FROM Obra model " 
				+ "where model.idEmpresa = :idEmpresa and model.estatus > 0 and [TIPO] and ((model.idObraPrincipal = 0[WHERE]) OR (model.idObraPrincipal IN (" 
				+ "SELECT model.id FROM Obra model WHERE model.estatus > 0 and [TIPO] and model.idObraPrincipal = 0[WHERE]))) " 
			+ "ORDER BY CASE COALESCE(NULLIF(model.idObraPrincipal,0),0) WHEN 0 THEN concat(model.id, '-0') ELSE concat(model.idObraPrincipal, '-', model.id) END";
		
		try {
			if (value != null) {
				if (propertyName.contains("model."))
					propertyName = propertyName.replace("model.", "");
				
				if (value.getClass() == java.util.Date.class) {
					queryString = queryString.replace("[WHERE]", " date(model."+ propertyName + ") = date('" + formateador.format((Date) value) + "') ");
					value = null;
				} else {
					queryString = queryString.replace("[WHERE]", " and model."+ propertyName + " = :propertyValue ");
				}
			}
			
			if (tipoObra > 0)
				queryString = queryString.replace("[TIPO]","model.tipoObra = :tipoObra "); 
			else
				queryString = queryString.replace("[TIPO]","model.tipoObra <> 4 "); 
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += ", " + orderBy;;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
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
	public List<Obra> findPrincipalLike(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception {
		StringBuffer sb = null;
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "SELECT model FROM Obra model " 
				+ "where model.idEmpresa = :idEmpresa and model.estatus > 0 and [TIPO] and ((model.idObraPrincipal = 0[WHERE]) OR (model.idObraPrincipal IN (" 
					+ "SELECT model.id FROM Obra model WHERE model.estatus > 0 and [TIPO] and model.idObraPrincipal = 0[WHERE]))) " 
				+ "ORDER BY CASE COALESCE(NULLIF(model.idObraPrincipal,0),0) WHEN 0 THEN concat(model.id, '-0') ELSE concat(model.idObraPrincipal, '-', model.id) END";
		
		try {
			if (value != null) {
				if (propertyName.contains("model."))
					propertyName = propertyName.replace("model.", "");

				if (value.getClass() == java.util.Date.class) {
					queryString = queryString.replace("[WHERE]", " date(model."+ propertyName + ") = date('" + formateador.format((Date) value) + "') ");
					value = null;
				} else if (value.getClass() == java.lang.String.class && ! "".equals(value.toString().trim())) {
					if ("id".equals(propertyName) || propertyName.startsWith("id"))
						queryString = queryString.replace("[WHERE]", " and cast(model." + propertyName + " as string) LIKE :propertyValue ");
					else
						queryString = queryString.replace("[WHERE]", " and lower(cast(model."+ propertyName + " as string)) LIKE :propertyValue ");
					
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase());
		    		sb.append("%");
				} else {
					value = null;
				}
			} else {
				queryString = queryString.replace("[WHERE]", "");
			}
			
			if (tipoObra > 0)
				queryString = queryString.replace("[TIPO]","model.tipoObra = :tipoObra "); 
			else
				queryString = queryString.replace("[TIPO]","model.tipoObra <> 4 "); 
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += ", " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			if (value != null)
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
	public List<Obra> findSubObraBy(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa and COALESCE(model.idObraPrincipal,0) <> 0 and model.estatus > 0 ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model."+ propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else {
					queryString += "and model."+ propertyName + " = :propertyValue ";
				}
			}
			
			if (tipoObra > 0)
				queryString += "and model.tipoObra = :tipoObra ";
			else
				queryString += "and model.tipoObra <> 4 ";
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
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
	public List<Obra> findSubObraLike(String propertyName, Object value, int tipoObra, String orderBy, int limite) throws Exception {
		String queryString = "select model from Obra model where model.idEmpresa = :idEmpresa and COALESCE(model.idObraPrincipal,0) <> 0 and model.estatus > 0 ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = null;
		
		try {
			if (value != null) {
				if (propertyName.contains("model."))
					propertyName = propertyName.replace("model.", "");
				
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model."+ propertyName + ") = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (value.getClass() == java.lang.String.class && ! "".equals(value.toString().trim())) {
					if ("id".equals(propertyName) || propertyName.startsWith("id"))
						queryString += "and cast(model."+ propertyName + " as string) LIKE :propertyValue ";
					else
						queryString += "and lower(cast(model."+ propertyName + " as string)) LIKE :propertyValue ";
					
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase());
		    		sb.append("%");
				} else {
					value = null;
				}
			}
			
			if (tipoObra > 0)
				queryString += "and model.tipoObra = :tipoObra ";
			else
				queryString += "and model.tipoObra <> 4 ";
			
			if (orderBy != null && ! "".equals(orderBy))
				queryString += "order by " + orderBy;
			else
				queryString += "order by " + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (tipoObra > 0)
				query.setParameter("tipoObra", tipoObra);
			if (value != null)
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		} 
	}

	private boolean IsNumber(String value) {
		try { Double.parseDouble(value); }
		catch (Exception e) { return false; }
		return true;
	}
	
	private String determinaOperador(String key, boolean toLike) {
		String resultado = "";
		
		if (key.contains("!")) {
			key = key.replace("!", "");
			resultado = (toLike) ? " not like " : " <> ";
		} else if (key.contains(">")) {
			key = key.replace(">", "");
			resultado = (toLike) ? " like " : " > ";
			resultado = " > ";
		} else if (key.contains("<")) {
			key = key.replace("<", "");
			resultado = (toLike) ? " like " : " < ";
		} else if (key.contains("?")) {
			key = key.replace("?", "");
			resultado = (toLike) ? " like " : " IN (_IN_)";
		} else {
			resultado = (toLike) ? " like " : " = ";
		}
		
		return resultado;
	}
	
	private String determinaUnion(String key) {
		String resultado = "";
		
		if (key.contains("&")) {
			key = key.replace("&", "");
			resultado = " AND";
		} else if (key.contains("|")) {
			key = key.replace("|", "");
			resultado = " OR";
		} else {
			resultado = " AND";
		}
		
		return resultado;
	}
	
	private String determinaKey(String key) {
		String caracteres = "&|?!<>";
		String resultado = "";
		String[] splitted = null;
		
		splitted = key.split("");
		for (String var : splitted) {
			if (caracteres.contains(var)) continue;
			resultado += var;
		}
		
		return resultado;
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