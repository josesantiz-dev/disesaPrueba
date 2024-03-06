package net.giro.inventarios.dao;
import net.giro.DAOImpl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.inventarios.beans.AlmacenProducto;

@Stateless
public class AlmacenProductoImp extends DAOImpl<AlmacenProducto> implements AlmacenProductoDAO{
	
	@PersistenceContext
	private EntityManager entityManager;

	public void delete(AlmacenProducto entity) {
		try {
			entity = entityManager.getReference(AlmacenProducto.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(AlmacenProducto entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public AlmacenProducto findById(Integer id) {
		try {
			AlmacenProducto instance = entityManager.find(AlmacenProducto.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from AlmacenProducto model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findCantidadEnAlmacen(long idAlmacen, long idProducto) {
		try {
			final String queryString = "select model from AlmacenProducto model where model.idProducto = " + idProducto + " and model.idAlmacen = "+idAlmacen;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAlmacenProducto(long idAlmacen, long idProducto) {
		try {
			final String queryString = "select model from AlmacenProducto model where model.idProducto = " + idProducto + " and model.idAlmacen = "+idAlmacen;
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAll() {
		try {
			final String queryString = "select model from AlmacenProducto model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findAllActivos() {
		try {
			final String queryString = "select model from AlmacenProducto model where model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public AlmacenProducto findProductoExistente(String campoBusqueda, long idAlmacen, String valor){
		AlmacenProducto ap = null;
		
		String queryString = "select ap from Producto p, AlmacenProducto ap where ap.idProducto = p.id and p.estatus = 0 and ap.idAlmacen = "+ idAlmacen +" and p.existencia > 0 and lower(p."+campoBusqueda+") = '"+ valor.toLowerCase() + "'";
		
		//System.out.println("QS: "+ queryString);
		
		try {
			Query query = entityManager.createQuery(queryString);
			List<AlmacenProducto> listaProductos = query.getResultList();
			if(listaProductos.isEmpty()) return null;
			
			ap = listaProductos.get(0);
			
		} catch (RuntimeException re) {
			throw re;
		}
		
		return ap;
	}
	
	@SuppressWarnings("unchecked")
	public List<AlmacenProducto> findExistentes(String campoBusqueda, long idAlmacen, String valor){
		
		String queryString ="";
		
		// Select p from producto p, almacen_producto ap where p.id = ap.id_producto
		
		if(campoBusqueda.equals("")){
			queryString = "select ap from AlmacenProducto ap where ap.estatus = 0 and ap.existencia > 0 and ap.idAlmacen = "+idAlmacen;
		}else{
			queryString = "select ap from Producto p, AlmacenProducto ap where ap.idProducto = p.id and p.estatus = 0 and ap.idAlmacen = "+ idAlmacen +" and p.existencia > 0 and lower(p."+campoBusqueda+") like '%"+ valor.toLowerCase() +"%'";
			//System.out.println("Query: "+queryString);
		}
		
		try {
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

}
