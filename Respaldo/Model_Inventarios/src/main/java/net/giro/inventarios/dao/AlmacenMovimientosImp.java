package net.giro.inventarios.dao;
import net.giro.DAOImpl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.inventarios.beans.AlmacenMovimientos;

@Stateless
public class AlmacenMovimientosImp extends DAOImpl<AlmacenMovimientos> implements AlmacenMovimientosDAO{
	
	@PersistenceContext
	private EntityManager entityManager;

	public void delete(AlmacenMovimientos entity) {
		try {
			entity = entityManager.getReference(AlmacenMovimientos.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(AlmacenMovimientos entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public AlmacenMovimientos findById(Integer id) {
		try {
			AlmacenMovimientos instance = entityManager.find(AlmacenMovimientos.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from AlmacenMovimientos model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findByEspecificField(String propertyName, final Object value, int tipoMovimiento) {
		try {
			String queryString;
			queryString = "select am from AlmacenMovimientos am";
			
			if(propertyName.equals("nombreAlmacen")){
				queryString = "select am from AlmacenMovimientos am, Almacen a where am.idAlmacen = a.id and am.tipo = '"+ tipoMovimiento +"' and lower(a.nombre) like '%"+ value.toString().toLowerCase() +"%' ";
			}else{
				if(propertyName.equals("nombreObra")){
					queryString = "select am from AlmacenMovimientos am, Almacen a where am.idAlmacen = a.id and am.tipo = '"+ tipoMovimiento +"' and lower(am.nombreObra) like '%"+ value.toString().toLowerCase() +"%' ";
				}
			}
			
			System.out.println("findByEspecificField -> queryString: "+queryString);
			
			Query query = entityManager.createQuery(queryString);
			
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findAll() {
		try {
			final String queryString = "select model from AlmacenMovimientos model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findAllActivos() {
		try {
			final String queryString = "select model from AlmacenMovimientos model where model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

}
