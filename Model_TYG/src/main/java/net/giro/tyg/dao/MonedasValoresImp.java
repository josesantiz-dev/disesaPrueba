package net.giro.tyg.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.giro.DAOImpl;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class MonedasValoresImp extends DAOImpl<MonedasValores> implements MonedasValoresDAO  {
	@PersistenceContext
	private EntityManager entityManager;
	private String queryString;
	private String whereString;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MonedasValores> findByBaseDestino(Long monedaBase, Long monedaDestino){
		String queryString = "select model from MonedasValores model ";
		String where = "";
		String and = " and ";
		
		try {
			if(monedaBase != null && monedaBase > 0)
				where = "where model.monedaBase.id = " + monedaBase.toString() + " ";
			else{
				where = "";
				and = "where ";
			}
			if(monedaDestino != null && monedaDestino > 0)
				and += " model.monedaDestino.id = " + monedaDestino.toString() + " ";
			else
				and = "";
			queryString += where + and;
			Query query = entityManager.createQuery(queryString);
			if("".equals(where))
				query.setMaxResults(500);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MonedasValores> findMonedaAFecha(long monedaId, Date fechaMovimiento) {
		try {
			final String queryString = "select model from MonedasValores model where model.monedaId = :moneda and :fecha between desde and hasta";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("moneda", monedaId);
			query.setParameter("fecha", fechaMovimiento);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean rangoFechasValido(long moneda, Date inicio, Date fin, Long idMonedaValor){
		List<Double> res = null;
		
		try{
			String queryString = "select model.valor from MonedasValores model where (:inicio between model.desde and model.hasta" +
					" or :fin between model.desde and model.hasta " +
					" or :inicio <= model.desde and :fin >= model.hasta) " +
					" and model.monedaId= :moneda and model.monedaValorId <> :monedaValorId";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("inicio",inicio);
			query.setParameter("fin",fin);
			query.setParameter("moneda",moneda);
			query.setParameter("monedaValorId", idMonedaValor);
			res= query.getResultList();
		} catch(RuntimeException re) {
			throw re;
		}
		return res == null || res.isEmpty();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MonedasValores> findByProperty(String propertyName, Object value, int limite) throws RuntimeException {
		try {
			this.queryString = "select model from MonedasValores model ";
			
			this.whereString = "";
			if (value != null)
				this.whereString = " where model."+ propertyName + " = :propertyValue";
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			
			Query query = entityManager.createQuery(queryString);
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
	public List<MonedasValores> findByProperties(HashMap<String, Object> params, int limite) throws Exception{
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			this.queryString = "select model from MonedasValores model ";
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
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MonedasValores> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		StringBuffer sb = null;
		
		try {
			this.queryString = "select model from MonedasValores model ";
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
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;	
			
			Query query = entityManager.createQuery(queryString);
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
	public List<MonedasValores> findLikeProperties(HashMap<String, String> params, int limite) throws Exception{
		try {
			this.queryString = "select model from MonedasValores model ";
			this.whereString = "";
			
			for(Entry<String, String> e : params.entrySet()){
				if (this.whereString.length() > 0)
					this.whereString += " and";
				this.whereString += " cast(model." + e.getKey() + " as string) like '%" + e.getValue() + "%'";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + " where " + this.whereString;

			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MonedasValores> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		String inFilter = "";
		
		try {
			this.queryString = "select model from MonedasValores model ";
			this.whereString = "";
			
			if(values != null && ! values.isEmpty()){
				this.whereString = " WHERE cast(model." + columnName + " as string) IN (";
				
				for(int i = 0; i < values.size(); i++) {
					if (!"".equals(inFilter)) inFilter += ",";
					inFilter += ":" + columnName + i;
				}
				
				this.whereString = this.whereString + inFilter + ")";
			}
			
			if (! this.whereString.isEmpty())
				this.queryString = this.queryString + this.whereString;
			
			Query query = entityManager.createQuery(queryString);
			if(values != null && ! values.isEmpty()) {
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
	}

	@Override
	public MonedasValores findActual(Moneda monedaBase, Moneda monedaDestino) throws Exception {
		try {
			List<MonedasValores> valores = this.findByFecha(monedaBase, monedaDestino, Calendar.getInstance().getTime(), 0);
			if (valores != null && ! valores.isEmpty())
				return valores.get(0);
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MonedasValores> findByFecha(Moneda monedaBase, Moneda monedaDestino, Date fecha, int limite) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			this.queryString = "select model from MonedasValores model ";
			this.whereString = "where ";
			
			if (monedaBase == null || monedaDestino == null)
				return null;
			if (fecha == null)
				fecha = Calendar.getInstance().getTime();

			this.whereString += "model.monedaBase = :monedaBase and model.monedaDestino = :monedaDestino and ";
			this.whereString += "date(model.fechaDesde) >= date(:fecha) and date(model.fechaHasta) <= date(:fecha) ";
			this.queryString += this.whereString;
			this.queryString += "order by model.fechaHasta desc, model.fechaDesde ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("monedaBase", monedaBase);
			query.setParameter("monedaDestino", monedaDestino);
			query.setParameter("fecha", formatter.format(fecha));
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<MonedasValores> findByFechas(Moneda monedaBase, Moneda monedaDestino, Date fechaDesde, Date fechaHasta, int limite) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			this.queryString = "select model from MonedasValores model ";
			this.whereString = "where ";
			
			if (monedaBase == null || monedaDestino == null)
				return null;
			if (fechaDesde == null)
				fechaDesde = Calendar.getInstance().getTime();
			if (fechaHasta == null)
				fechaHasta = Calendar.getInstance().getTime();

			this.whereString += "model.monedaBase = :monedaBase and model.monedaDestino = :monedaDestino and ";
			this.whereString += "date(model.fechaDesde) >= date(:fechaDesde) and date(model.fechaHasta) <= date(:fechaHasta) ";
			this.queryString += this.whereString;
			this.queryString += "order by model.fechaHasta desc, model.model.fechaDesde ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("monedaBase", monedaBase);
			query.setParameter("monedaDestino", monedaDestino);
			query.setParameter("fechaDesde", formatter.format(fechaDesde));
			query.setParameter("fechaHasta", formatter.format(fechaHasta));
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  2.2 | 2017-07-11 | Javier Tirado 	| Implemento nuevos metodos de busqueda
 */