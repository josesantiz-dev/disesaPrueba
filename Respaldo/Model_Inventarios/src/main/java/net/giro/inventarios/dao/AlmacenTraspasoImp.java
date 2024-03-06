package net.giro.inventarios.dao;
import net.giro.DAOImpl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.inventarios.beans.AlmacenTraspaso;

@Stateless
public class AlmacenTraspasoImp extends DAOImpl<AlmacenTraspaso> implements AlmacenTraspasoDAO {
	
	@PersistenceContext
	private EntityManager entityManager;

	public void delete(AlmacenTraspaso entity) {
		try {
			entity = entityManager.getReference(AlmacenTraspaso.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(AlmacenTraspaso entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public AlmacenTraspaso findById(Integer id) {
		try {
			AlmacenTraspaso instance = entityManager.find(AlmacenTraspaso.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from AlmacenTraspaso model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacenOrigen(String nombreAlmacen){
		try {
			final String queryString = "select at from AlmacenTraspaso at, Almacen a where at.idAlmacenOrigen = a.id and lower(a.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%'";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacenDestino(String nombreAlmacen){
		try {
			final String queryString = "select at from AlmacenTraspaso at, Almacen a where at.idAlmacenDestino = a.id and lower(a.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%'";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen){
		try {
			
			//and completo = 0
			
			String queryString = "";
			
			switch (tipoAlmacen){
			
				case 0:	//Todos
					queryString = "select at from AlmacenTraspaso at where at.completo = 0";
					break;
				
				case 1:	//Destino
					queryString = "select at from AlmacenTraspaso at, Almacen a where at.idAlmacenDestino = a.id and lower(a.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%' and at.completo = 0 ";
					break;
					
				case 2:	//Origen
					queryString = "select at from AlmacenTraspaso at, Almacen a where at.idAlmacenOrigen = a.id and lower(a.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%'  and at.completo = 0";
					break;
				
			}
			
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findAll() {
		try {
			final String queryString = "select model from AlmacenTraspaso model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findAllActivos() {
		try {
			final String queryString = "select model from AlmacenTraspaso model where model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
