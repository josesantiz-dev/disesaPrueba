package net.giro.inventarios.dao;

import net.giro.DAOImpl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.inventarios.beans.AlmacenProducto;

@Stateless
public class AlmacenProductoImp extends DAOImpl<AlmacenProducto> implements AlmacenProductoDAO {
	@PersistenceContext
	private EntityManager entityManager;
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(AlmacenProducto entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<AlmacenProducto> saveOrUpdateList(List<AlmacenProducto> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findBy(long idAlmacen, Object value, long idFamilia, int tipoMaestro, int limite) {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.existencia > 0 and p.estatus = 0 and p.oculto = 0 ";
		
		try {
			if (tipoMaestro <= 0)
				tipoMaestro = 1;
			
			if (value != null) {
				queryString += "and (p.id = :propertyValue " 
						+ "or p.clave = :propertyValue " 
						+ "or p.descripcion = :propertyValue " 
						+ "or p.familia = :propertyValue " 
						+ "or p.descFamilia = :propertyValue " 
						+ "or p.unidadMedida = :propertyValue " 
						+ "or p.descUnidadMedida = :propertyValue " 
						+ "or p.claveSat = :propertyValue) ";
			} 
			
			if (idAlmacen > 0L)
				queryString += "and model.idAlmacen = :idAlmacen ";
			if (idFamilia > 0L)
				queryString += "and p.familia = :idFamilia ";
			queryString += "and p.tipo = :tipoMaestro ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value);
			if (idAlmacen > 0L)
				query.setParameter("idAlmacen", idAlmacen);
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findByProperty(String propertyName, final Object value) {
		String queryString = "select model from AlmacenProducto model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findLike(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite) {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.existencia > 0 and p.estatus = 0 and p.oculto = 0 ";
		StringBuffer sb = null;
		
		try {
			if (tipoMaestro <= 0)
				tipoMaestro = 1;
			
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(p.id as string) LIKE :propertyValue " 
						+ "or lower(trim(p.clave)) LIKE :propertyValue " 
						+ "or lower(trim(p.descripcion)) LIKE :propertyValue " 
						+ "or lower(trim(p.descFamilia)) LIKE :propertyValue " 
						+ "or lower(trim(p.descUnidadMedida)) LIKE :propertyValue " 
						+ "or lower(trim(p.claveSat)) LIKE :propertyValue) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toLowerCase());
	    		sb.append("%");
			} 
			
			if (idAlmacen > 0L)
				queryString += "and model.idAlmacen = :idAlmacen ";
			if (idFamilia > 0L)
				queryString += "and p.familia = :idFamilia ";
			queryString += "and p.tipo = :tipoMaestro ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idAlmacen > 0L)
				query.setParameter("idAlmacen", idAlmacen);
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findLikeProperty(String propertyName, String value) {
		String queryString = "select model from AlmacenProducto model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and lower(cast(model." + propertyName + " as string)) like :propertyValue ";
				value = "%" + value.replace(" ", "%") + "%";
			}
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (value != null)
				query.setParameter("propertyValue", value.trim().toLowerCase());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findLikeProperty(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite) {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.existencia > 0 and p.estatus = 0 and p.oculto = 0 ";
		StringBuffer sb = null;
		
		try {
			if (tipoMaestro <= 0)
				tipoMaestro = 1;
			
			if (propertyValue != null && ! "".equals(propertyValue.trim())) {
				queryString += "and cast(p." + propertyName + " as string) LIKE :propertyValue ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(propertyValue.toLowerCase());
	    		sb.append("%");
			} 
			
			if (idAlmacen > 0L)
				queryString += "and model.idAlmacen = :idAlmacen ";
			if (idFamilia > 0L)
				queryString += "and p.familia = :idFamilia ";
			queryString += "and p.tipo = :tipoMaestro ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			if (propertyValue != null && ! "".equals(propertyValue.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idAlmacen > 0L)
				query.setParameter("idAlmacen", idAlmacen);
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findCantidadEnAlmacen(long idAlmacen, long idProducto) {
		try {
			final String queryString = "select model from AlmacenProducto model where model.idEmpresa = :idEmpresa and model.idProducto = " + idProducto + " and model.idAlmacen = "+idAlmacen;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAlmacenProducto(long idAlmacen, long idProducto) {
		try {
			final String queryString = "select model from AlmacenProducto model where model.idEmpresa = :idEmpresa and model.idProducto = " + idProducto + " and model.idAlmacen = "+idAlmacen;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAll() {
		try {
			final String queryString = "select model from AlmacenProducto model where model.idEmpresa = :idEmpresa order by model.id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAllActivos() {
		try {
			final String queryString = "select model from AlmacenProducto model where model.idEmpresa = :idEmpresa and model.estatus = 0 order by model.id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public AlmacenProducto findProductoExistente(String campoBusqueda, long idAlmacen, String valor) {
		String queryString = "select ap from AlmacenProducto ap, Producto p where model.idEmpresa = :idEmpresa and ap.idProducto = p.id and p.estatus = 0 and ap.idAlmacen = "+ idAlmacen +" and p.existencia > 0 and lower(p."+campoBusqueda+") = '"+ valor.toLowerCase() + "'";
		List<AlmacenProducto> listaProductos = null;
		AlmacenProducto ap = null;
		
		try {
			queryString += " order by ap.id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			listaProductos = query.getResultList();
			if (listaProductos.isEmpty()) 
				return null;
			ap = listaProductos.get(0);
		} catch (Exception re) {
			throw re;
		}
		
		return ap;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findExistentes(String campoBusqueda, long idAlmacen, String valor) {
		String queryString = "select ap from AlmacenProducto ap, Producto p where model.idEmpresa = :idEmpresa and ap.existencia > 0 and ap.estatus = 0 and ap.idProducto = p.id and ap.idAlmacen = " + idAlmacen;
		//String queryString = "select ap from AlmacenProducto ap where ap.estatus = 0 and ap.existencia > 0 and ap.idAlmacen = " + idAlmacen;
		
		try {
			if (campoBusqueda != null && ! "".equals(campoBusqueda.trim()))
				queryString += " and lower(p." + campoBusqueda + ") like '%" + valor.toLowerCase() + "%' and p.estatus = 0";
				//queryString = "select ap from Producto p, AlmacenProducto ap where ap.existencia > 0 and ap.estatus = 0 and ap.idProducto = p.id and ap.idAlmacen = " + idAlmacen + " and lower(p." + campoBusqueda + ") like '%" + valor.toLowerCase() + "%' and p.estatus = 0 ";
			queryString += " order by ap.id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findExistentes(long idAlmacen, String value, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia) {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.estatus = 0 and p.estatus = 0 and p.oculto = 0 ";
		StringBuffer sb = null;
		
		try {
			if (idAlmacen <= 0)
				return null;
			
			if (tipoMaestro <= 0)
				tipoMaestro = 1;
			
			queryString += "and model.idAlmacen = :idAlmacen ";
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(p.id as string) LIKE :propertyValue " 
						+ "or lower(trim(p.clave)) LIKE :propertyValue " 
						+ "or lower(trim(p.descripcion)) LIKE :propertyValue " 
						+ "or lower(trim(p.descFamilia)) LIKE :propertyValue " 
						+ "or lower(trim(p.descUnidadMedida)) LIKE :propertyValue " 
						+ "or lower(trim(p.claveSat)) LIKE :propertyValue) ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(value.toLowerCase());
	    		sb.append("%");
			} 
			
			if (idFamilia > 0L)
				queryString += "and p.familia = :idFamilia ";
			queryString += "and p.tipo = :tipoMaestro ";
			if (! excluyeExistencia)
				queryString += "and model.existencia > 0 ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("idAlmacen", idAlmacen);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findExistentes(long idAlmacen, String propertyName, String propertyValue, long idFamilia, int tipoMaestro, int limite, boolean excluyeExistencia) {
		String queryString = "select model from AlmacenProducto model, Producto p where model.idEmpresa = :idEmpresa and model.idProducto = p.id and model.estatus = 0 and p.estatus = 0 and p.oculto = 0 ";
		StringBuffer sb = null;
		
		try {
			if (idAlmacen <= 0)
				return null;
			
			if (tipoMaestro <= 0)
				tipoMaestro = 1;
			
			if (propertyName == null || "".equals(propertyName.trim()) || "*".equals(propertyName.trim()))
				return this.findExistentes(idAlmacen, propertyValue, idFamilia, tipoMaestro, limite, excluyeExistencia);

			queryString += "and model.idAlmacen = :idAlmacen ";
			if (propertyValue != null && ! "".equals(propertyValue.trim())) {
				queryString += "and cast(p." + propertyName + " as string) LIKE :propertyValue ";
				
				sb = new StringBuffer();
	    		sb.append("%");
	    		sb.append(propertyValue.trim().toLowerCase());
	    		sb.append("%");
			} 
			
			if (idFamilia > 0L)
				queryString += "and p.familia = :idFamilia ";
			queryString += "and p.tipo = :tipoMaestro ";
			if (! excluyeExistencia)
				queryString += "and model.existencia > 0 ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("idAlmacen", idAlmacen);
			if (propertyValue != null && ! "".equals(propertyValue.trim()))
				query.setParameter("propertyValue", sb.toString());
			if (idFamilia > 0L)
				query.setParameter("idFamilia", idFamilia);
			query.setParameter("tipoMaestro", tipoMaestro);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
