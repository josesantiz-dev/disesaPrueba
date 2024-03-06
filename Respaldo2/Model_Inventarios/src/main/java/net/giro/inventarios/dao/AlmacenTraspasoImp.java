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
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(AlmacenTraspaso entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<AlmacenTraspaso> saveOrUpdateList(List<AlmacenTraspaso> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findAll() {
		try {
			final String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa order by id desc";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findAllActivos() {
		try {
			final String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and model.estatus = 0 order by id desc";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and model." + propertyName + "= :propertyValue order by id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findLikeProperty(String propertyName, String value) {
		String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa ";
		 
		try {
			if (value != null && ! "".equals(value.trim())) {
				value = "%" + value.trim().replace(" ", "%") + "%";
				queryString += "and lower(cast(model." + propertyName + " as string)) like :propertyValue ";
			}
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			if (value != null && ! "".equals(value.trim()))
				query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacen(String nombreAlmacen) {
		String queryString = "select model from AlmacenTraspaso model, Almacen a, Almacen b where model.idEmpresa = :idEmpresa and model.idAlmacenOrigen = a.id and model.idAlmacenDestino = b.id ";
		 
		try {
			if (nombreAlmacen != null && ! "".equals(nombreAlmacen.trim())) {
				nombreAlmacen = "%" + nombreAlmacen.trim().replace(" ", "%") + "%";
				queryString += "and ((lower(a.nombre) like :propertyValue) or (lower(b.nombre) like :propertyValue)) ";
			}
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			if (nombreAlmacen != null && ! "".equals(nombreAlmacen.trim()))
				query.setParameter("propertyValue", nombreAlmacen);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacenOrigen(String nombreAlmacen){
		try {
			final String queryString = "select at from AlmacenTraspaso at, Almacen a where at.idEmpresa = :idEmpresa and at.idAlmacenOrigen = a.id and lower(a.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%' order by id desc";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacenDestino(String nombreAlmacen){
		try {
			final String queryString = "select at from AlmacenTraspaso at, Almacen a where at.idEmpresa = :idEmpresa and at.idAlmacenDestino = a.id and lower(a.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%' order by id desc";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen) {
		String queryString = "";
		
		try {
			switch (tipoAlmacen) {
				case 0:	//Todos
					queryString = "select at from AlmacenTraspaso at where at.idEmpresa = :idEmpresa and at.completo = 0 order by id desc";
					break;
				case 1:	//Destino
					queryString = "select at from AlmacenTraspaso at, Almacen a where at.idEmpresa = :idEmpresa and at.idAlmacenDestino = a.id and lower(a.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%' and at.completo = 0 order by id desc";
					break;
				case 2:	//Origen
					queryString = "select at from AlmacenTraspaso at, Almacen a where at.idEmpresa = :idEmpresa and at.idAlmacenOrigen = a.id and lower(a.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%'  and at.completo = 0 order by id desc";
					break;
			}
			
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findLikeWithDestino(String value, long idAlmacenDestino) {
		String queryString = "select model from AlmacenTraspaso model, Almacen a where model.idEmpresa = :idEmpresa and model.completo = 0 AND model.idAlmacenOrigen = a.id AND model.idAlmacenDestino = :almacenDestino ";
		
		try {
			if (value != null && ! "".equals(value.trim()))
				queryString += "and lower(a.nombre) like '%" + value.toLowerCase() + "%' ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("almacenDestino", idAlmacenDestino);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findIncompletosLikeProperty(String propertyName, String propertyValue, String orderBy, int limite) {
		String queryString = "select model from AlmacenTraspaso model, Almacen a, Almacen b where model.idEmpresa = :idEmpresa and model.idAlmacenOrigen = a.id and model.idAlmacenDestino = b.id and model.completo = 0 ";
		
		try {
			if (propertyName == null || "".equals(propertyName.trim()))
				propertyName = "*";
			
			if (propertyValue != null) {
				if ("*".equals(propertyName.trim())) {
					queryString += "and (cast(model.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(a.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(b.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idOrdenCompra as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.entrega as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.recibe as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or lower(a.nombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(a.identificador) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(b.nombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(b.identificador) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.entregaNombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.recibeNombre) like '%" + propertyValue.toLowerCase().trim() + "%') ";
				} else if ("nombre".equals(propertyName.trim())) {
					queryString += "and (lower(a.nombre) like '%" + propertyValue.toLowerCase().trim() + "%' or lower(a.identificador) like '%" + propertyValue.toString().trim() + "%' ";
					queryString += "or lower(b.nombre) like '%" + propertyValue.toLowerCase().trim() + "%' or lower(b.identificador) like '%" + propertyValue.toString().trim() + "%' ";
					queryString += "or lower(model.entregaNombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.recibeNombre) like '%" + propertyValue.toLowerCase().trim() + "%') ";
				} else if ("fecha".equals(propertyName.trim())) {
					queryString += "and cast(date(model.fecha) as string) like '%" + propertyValue.trim() + "%' ";
				} else if ("id".equals(propertyName.trim())) {
					queryString += "and (cast(model.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idAlmacenOrigen as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idAlmacenDestino as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.entrega as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.recibe as string) like '%" + propertyValue.trim() + "%') ";
				} else {
					queryString += "and lower(cast(model." + propertyName.toString().trim() + " as string)) like '%" + propertyValue.toLowerCase().trim() + "%' ";
				}
			}
			
			if (orderBy != null && ! "".equals(orderBy.trim()))
				queryString += "order by " + orderBy;
			else
				queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
