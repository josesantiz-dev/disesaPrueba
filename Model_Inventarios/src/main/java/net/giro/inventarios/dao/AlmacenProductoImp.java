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

import org.apache.commons.lang.StringUtils;

import net.giro.inventarios.beans.AlmacenProducto;

@Stateless
public class AlmacenProductoImp extends DAOImpl<AlmacenProducto> implements AlmacenProductoDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(AlmacenProducto entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenProducto> saveOrUpdateList(List<AlmacenProducto> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa > 0L ? codigoEmpresa : 1L);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAll(long idAlmacen, boolean excluyeSinExistencia, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.estatus = 0 and p.estatus = 0 and p.oculto = 0 ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacen = (idAlmacen > 0 ? idAlmacen : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "p.clave, model.id desc ");
			if (! excluyeSinExistencia)
				queryString += "and model.existencia > 0 ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idAlmacen", idAlmacen);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, long idEmpresa, String orderBy, int limite) {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.estatus = 0 and p.estatus = 0 and p.oculto = 0 ";
		List<String> valores = null;
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacen = (idAlmacen > 0 ? idAlmacen : 0L);
			tipoMaestro = (tipoMaestro > 0 ? tipoMaestro : 1);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "p.clave, model.id desc ");
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(p.id as string) like :propertyValue " 
						+ "or lower(trim(p.clave)) like :propertyValue " 
						+ "or lower(trim(p.descripcion)) like :propertyValue " 
						+ "or lower(trim(p.descFamilia)) like :propertyValue " 
						+ "or lower(trim(p.descUnidadMedida)) like :propertyValue " 
						+ "or lower(trim(p.claveSat)) like :propertyValue) ";

				valores = recuperaValores(value.toString().toLowerCase().trim(), "\\+");
				if (valores == null || valores.isEmpty()) {
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.trim().toLowerCase());
		    		sb.append("%");
				}
			} 
			
			if (idAlmacen > 0L)
				queryString += "and model.idAlmacen.id = :idAlmacen ";
			if (idFamilia > 0L)
				queryString += "and p.familia = :idFamilia ";
			queryString += "and p.tipo = :tipoMaestro ";
			if (! excluyeSinExistencia)
				queryString += "and model.existencia > 0 ";
			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "p.clave, model.id desc ";
			}
			queryString += "order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (sb != null && ! "".equals(sb.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idAlmacen > 0L)
				query.setParameter("idAlmacen", idAlmacen);
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findLikeProperty(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, long idEmpresa, String orderBy, int limite) {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.estatus = 0 and p.estatus = 0 and p.oculto = 0 ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		List<String> valores = null;
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacen = (idAlmacen > 0 ? idAlmacen : 0L);
			tipoMaestro = (tipoMaestro > 0 ? tipoMaestro : 1);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "p.clave, model.id desc ");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = "";
				} else if (value.getClass() == java.lang.String.class) {
					queryString += "and lower(trim(" + propertyName + ")) like :propertyValue ";
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and lower(trim(cast(" + propertyName + " as string))) like :propertyValue ";
				}
				
				valores = recuperaValores(value.toString().toLowerCase().trim(), "\\+");
				if (valores == null || valores.isEmpty()) {
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.toString().trim().toLowerCase());
		    		sb.append("%");
				} 
			}
			
			if (idAlmacen > 0L)
				queryString += "and model.idAlmacen.id = :idAlmacen ";
			if (idFamilia > 0L)
				queryString += "and p.familia = :idFamilia ";
			queryString += "and p.tipo = :tipoMaestro ";
			if (! excluyeSinExistencia)
				queryString += "and model.existencia > 0 ";
			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "p.clave, model.id desc ";
			}
			queryString += "order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (sb != null && ! "".equals(sb.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idAlmacen > 0L)
				query.setParameter("idAlmacen", idAlmacen);
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findByProperty(long idAlmacen, String propertyName, Object value, long idFamilia, int tipoMaestro, boolean excluyeSinExistencia, long idEmpresa, String orderBy, int limite) {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.estatus = 0 and p.estatus = 0 and p.oculto = 0 ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacen = (idAlmacen > 0 ? idAlmacen : 0L);
			tipoMaestro = (tipoMaestro > 0 ? tipoMaestro : 1);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "p.clave, model.id desc ");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = null;
				} else if (! "".equals(value.toString().trim())) {
					queryString += "and "+ propertyName + " = :propertyValue ";
				}
			}
			
			if (idAlmacen > 0L)
				queryString += "and model.idAlmacen.id = :idAlmacen ";
			if (idFamilia > 0L)
				queryString += "and p.familia = :idFamilia ";
			queryString += "and p.tipo = :tipoMaestro ";
			if (! excluyeSinExistencia)
				queryString += "and model.existencia > 0 ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (value != null && ! "".equals(value.toString().trim()))
				query.setParameter("propertyValue", value);
			if (idAlmacen > 0L)
				query.setParameter("idAlmacen", idAlmacen);
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> encontrarExistencia(long idAlmacen, List<Long> listProductos, boolean excluyeSinExistencia, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.estatus = 0 and p.estatus = 0 and p.oculto = 0 and model.idAlmacen.id = :idAlmacen and model.idProducto in (:lista) ";
		String lista = "";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacen = (idAlmacen > 0 ? idAlmacen : 0L);
			listProductos = (listProductos != null && ! listProductos.isEmpty() ? listProductos : new ArrayList<Long>());
			lista = (! listProductos.isEmpty() ?  StringUtils.join(listProductos, ",") : "0");
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "p.clave, model.id desc ");
			
			if (idAlmacen <= 0L)
				return null;
			if (listProductos == null || listProductos.isEmpty())
				return null;
			
			queryString = queryString.replace(":lista", lista);
			if (! excluyeSinExistencia)
				queryString += "and model.existencia > 0 ";
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idAlmacen", idAlmacen);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAlmacenProductos(long idAlmacen, long idProducto, long idEmpresa) throws Exception {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.estatus = 0 and p.estatus = 0 and p.oculto = 0 ";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacen = (idAlmacen > 0 ? idAlmacen : 0L);
			if (idAlmacen <= 0L)
				return null;
			if (idProducto <= 0L)
				return null;
			queryString += "and model.idAlmacen.id = :idAlmacen and model.idProducto = :idProducto ";
			queryString += "order by model.fechaCreacion desc ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idAlmacen", idAlmacen);
			query.setParameter("idProducto", idProducto);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public AlmacenProducto findAlmacenProducto(long idAlmacen, long idProducto, long idEmpresa) throws Exception {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.estatus = 0 and p.estatus = 0 and p.oculto = 0 ";
		List<AlmacenProducto> resultados = null;
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacen = (idAlmacen > 0 ? idAlmacen : 0L);
			if (idAlmacen <= 0L)
				return null;
			if (idProducto <= 0L)
				return null;
			queryString += "and model.idAlmacen.id = :idAlmacen and model.idProducto = :idProducto ";
			queryString += "order by model.fechaCreacion desc ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("idAlmacen", idAlmacen);
			query.setParameter("idProducto", idProducto);
			resultados = query.getResultList();
			if (resultados != null && ! resultados.isEmpty())
				return resultados.get(0);
			return null;
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public AlmacenProducto findProductoExistente(long idAlmacen, String propertyName, Object value, long idEmpresa) {
		List<AlmacenProducto> productos = null;
		AlmacenProducto ap = null;
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacen = (idAlmacen > 0 ? idAlmacen : 0L);
			productos = this.findByProperty(idAlmacen, propertyName, value, 0L, 0, true, idEmpresa, "", 0);
			if (productos == null || productos.isEmpty()) 
				return null;
			ap = productos.get(0);
		} catch (Exception re) {
			throw re;
		}
		
		return ap;
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
			queryModificada += (! "".equals(queryModificada.trim()) ? "or " : "") +  "model.id in (" + queryOriginal.trim().replace("select model from", "select model.id from").replace(":propertyValue", "'%" + valor + "%'") + ")";
		return "select model from AlmacenProducto model, Producto p where model.idProducto = p.id and (" + queryModificada + ")";
	}
}
