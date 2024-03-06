package net.giro.inventarios.dao;
import net.giro.DAOImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.inventarios.beans.AlmacenMovimientos;

@Stateless
public class AlmacenMovimientosImp extends DAOImpl<AlmacenMovimientos> implements AlmacenMovimientosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(AlmacenMovimientos entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<AlmacenMovimientos> saveOrUpdateList(List<AlmacenMovimientos> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/*@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findAll(int tipoMovimiento, boolean incluyeCancelados, long idEmpresa, String orderBy) {
		String queryString = "select model from AlmacenMovimientos model where model.idEmpresa = :idEmpresa ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			tipoMovimiento = (tipoMovimiento >= 0 ? tipoMovimiento : -1);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			if (tipoMovimiento >= 0)
				queryString += "and model.tipo = :tipoMovimiento ";
			queryString += "and model.estatus in (0" + ((incluyeCancelados) ? ",1" : "") + ") ";
			queryString += "order by " + orderBy.trim();
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (tipoMovimiento >= 0)
				query.setParameter("tipoMovimiento", tipoMovimiento);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}*/

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findLike(String value, long idAlmacen, int tipoMovimiento, String tipoEntrada, boolean incluyeCancelados, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select a.id from AlmacenMovimientos a, MovimientosDetalle b, Producto c where a.idEmpresa = :idEmpresa and b.idAlmacenMovimiento = a.id and c.id = b.idProducto ";
		List<String> valores = null;
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacen = (idAlmacen > 0 ? idAlmacen : 0L);
			tipoMovimiento = (tipoMovimiento >= 0 ? tipoMovimiento : -1);
			tipoEntrada = (tipoEntrada != null && ! "".equals(tipoEntrada.trim()) ? tipoEntrada.trim() : "");
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(a.id as string) like :propertyValue ";
				queryString += "or trim(lower(a.folioFactura)) like :propertyValue ";
				queryString += "or trim(lower(a.recibeNombre)) like :propertyValue ";
				queryString += "or trim(lower(a.entregaNombre)) like :propertyValue ";
				queryString += "or trim(lower(a.nombreObra)) like :propertyValue ";
				queryString += "or trim(lower(a.folioOrdenCompra)) like :propertyValue ";
				queryString += "or trim(lower(c.clave)) like :propertyValue ";
				queryString += "or trim(lower(c.descripcion)) like :propertyValue) ";
				
				valores = recuperaValores(value.toString().toLowerCase().trim(), "\\+");
				if (valores == null || valores.isEmpty()) {
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.trim().toLowerCase());
		    		sb.append("%");
				}
			}

			if (idAlmacen > 0L)
				queryString += "and a.idAlmacen.id = :idAlmacen ";
			if (tipoMovimiento >= 0)
				queryString += "and a.tipo = :tipoMovimiento ";
			if (! "".equals(tipoEntrada.trim()))
				queryString += "and a.tipoEntrada = :tipoEntrada ";
			queryString += "and a.estatus in (0" + ((incluyeCancelados) ? ",1" : "") + ") ";
			queryString += "and a.sistema in (0" + ((incluyeSistema) ? ",1" : "") + ") ";

			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "model.fecha desc, model.id desc ";
			} else 
				queryString = "select model from AlmacenMovimientos model where model.id in (:subqueries) ".replace(":subqueries", queryString);
			queryString += "order by " + orderBy.trim();
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idAlmacen > 0L)
				query.setParameter("idAlmacen", idAlmacen);
			if (tipoMovimiento >= 0)
				query.setParameter("tipoMovimiento", tipoMovimiento);
			if (! "".equals(tipoEntrada.trim()))
				query.setParameter("tipoEntrada", tipoEntrada);
			if (sb != null && ! "".equals(sb.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			System.out.println(queryString);
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findLikeProperty(String propertyName, Object propertyValue, long idAlmacen, int tipoMovimiento, String tipoEntrada, boolean incluyeCancelados, boolean incluyeSistema, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select a.id from AlmacenMovimientos a, MovimientosDetalle b, Producto c where a.idEmpresa = :idEmpresa and b.idAlmacenMovimiento = a.id and c.id = b.idProducto ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		List<String> valores = null;
		StringBuffer sb = null;
		 
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacen = (idAlmacen > 0 ? idAlmacen : 0L);
			tipoMovimiento = (tipoMovimiento >= 0 ? tipoMovimiento : -1);
			tipoEntrada = (tipoEntrada != null && ! "".equals(tipoEntrada.trim()) ? tipoEntrada.trim() : "");
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			
			if (propertyValue != null) {
				if (propertyValue.getClass() == java.util.Date.class) {
					queryString += "and date(a.fecha) = date('" + formateador.format((Date) propertyValue) + "') ";
					propertyValue = "";
				} else if (propertyValue.getClass() == java.lang.String.class) {
					queryString += "and lower(trim(" + propertyName + ")) like :propertyValue ";
				} else {
					queryString += "and lower(trim(cast(" + propertyName + " as string))) like :propertyValue ";
				}

				valores = recuperaValores(propertyValue.toString().toLowerCase().trim(), "\\+");
				if (valores == null || valores.isEmpty()) {
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(propertyValue.toString().trim().toLowerCase());
		    		sb.append("%");
				} 
			}
			
			if (idAlmacen > 0L)
				queryString += "and a.idAlmacen.id = :idAlmacen ";
			if (tipoMovimiento >= 0)
				queryString += "and a.tipo = :tipoMovimiento ";
			if (! "".equals(tipoEntrada.trim()))
				queryString += "and a.tipoEntrada = :tipoEntrada ";
			queryString += "and a.estatus in (0" + ((incluyeCancelados) ? ",1" : "") + ") ";
			queryString += "and a.sistema in (0" + ((incluyeSistema) ? ",1" : "") + ") ";

			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "model.fecha desc, model.id desc ";
			} else 
				queryString = "select model from AlmacenMovimientos model where model.id in (:subqueries) ".replace(":subqueries", queryString);
			queryString += "order by " + orderBy.trim();
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idAlmacen > 0L)
				query.setParameter("idAlmacen", idAlmacen);
			if (tipoMovimiento >= 0)
				query.setParameter("tipoMovimiento", tipoMovimiento);
			if (! "".equals(tipoEntrada.trim()))
				query.setParameter("tipoEntrada", tipoEntrada);
			if (sb != null && ! "".equals(sb.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			System.out.println(queryString);
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findByProperty(String propertyName, Object propertyValue, int tipoMovimiento, String tipoEntrada, long idEmpresa, int limite) {
		String queryString = "select model from AlmacenMovimientos model where model.idEmpresa = :idEmpresa ";
		
		try {
			queryString += "and model.tipo " + (tipoMovimiento == -1 ? ">" : "=") + " :tipoMovimiento ";
			if (propertyValue != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			
			if (tipoEntrada != null && ! "".equals(tipoEntrada.trim()))
				queryString += "and model.tipoEntrada = :tipoEntrada ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("tipoMovimiento", tipoMovimiento);
			if (propertyValue != null)
				query.setParameter("propertyValue", propertyValue);
			if (tipoEntrada != null && ! "".equals(tipoEntrada.trim()))
				query.setParameter("tipoEntrada", tipoEntrada);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	/*@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findByEspecificField(String propertyName, final Object value, int tipoMovimiento, long idEmpresa) {
		String queryString = "select model from AlmacenMovimientos model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (propertyName.equals("nombreAlmacen")) {
				queryString += "and model.tipo = '" + tipoMovimiento + "' and lower(model.idAlmacen.nombre) like '%" + value.toString().toLowerCase() + "%' ";
			} else {
				if (propertyName.equals("nombreObra")) {
					queryString += " and model.tipo = '" + tipoMovimiento + "' and lower(model.nombreObra) like '%" + value.toString().toLowerCase() + "%' ";
				}
			}
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}*/

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findSalidaByTraspaso(long idTraspaso, int tipoTraspaso, long idEmpresa, int limite) {
		String queryString = "select model from AlmacenMovimientos model, AlmacenTraspaso tr where model.idEmpresa = :idEmpresa and model.tipo = 1 and model.idTraspaso = tr.id and tr.tipo = :tipoTraspaso and tr.id = :idTraspaso ";
		
		try {
			queryString += "order by model.id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idTraspaso", idTraspaso);
			query.setParameter("tipoTraspaso", tipoTraspaso);
			if (limite > 0)
				query.setMaxResults(limite);
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
			queryModificada += (! "".equals(queryModificada.trim()) ? "or " : "") +  "model.id in (:query)".replace(":query", queryOriginal.trim().replace(":propertyValue", "'%" + valor + "%'"));
		return "select model from AlmacenMovimientos model where (" + queryModificada + ")";
	}
}
