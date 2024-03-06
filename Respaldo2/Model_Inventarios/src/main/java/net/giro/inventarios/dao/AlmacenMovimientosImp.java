package net.giro.inventarios.dao;
import net.giro.DAOImpl;

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
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(AlmacenMovimientos entity) throws Exception {
		try {
			return super.save(entity, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public List<AlmacenMovimientos> saveOrUpdateList(List<AlmacenMovimientos> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities, getIdEmpresa());
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findAll() {
		try {
			final String queryString = "select model from AlmacenMovimientos model where model.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findAllActivos() {
		try {
			final String queryString = "select model from AlmacenMovimientos model where model.idEmpresa = :idEmpresa and model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<AlmacenMovimientos> findByProperty(String propertyName, final Object value) {
		try {
			return this.findByProperty(propertyName, value, 0);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findByProperty(String propertyName, Object value, int limite) {
		try {
			final String queryString = "select model from AlmacenMovimientos model, Almacen a where a.idEmpresa = :idEmpresa and model.idAlmacen = a.id and model." + propertyName + " = :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	public List<AlmacenMovimientos> findByProperty(String propertyName, Object propertyValue, int tipoMovimiento, String tipoEntrada, int limite) {
		String queryString = "select model from AlmacenMovimientos model, Almacen a where a.idEmpresa = :idEmpresa and model.idAlmacen = a.id ";
		
		try {
			queryString += "and model.tipo " + (tipoMovimiento == -1 ? ">" : "=") + " :tipoMovimiento ";
			if (propertyValue != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			
			if (tipoEntrada != null && ! "".equals(tipoEntrada.trim()))
				queryString += "and model.tipoEntrada = :tipoEntrada ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
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
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findLikeProperty(String propertyName, String propertyValue, int tipoMovimiento, String tipoEntrada, int limite) {
		String queryString = "select model from AlmacenMovimientos model, Almacen a where model.idEmpresa = :idEmpresa and model.idAlmacen = a.id ";
		
		try {
			if (propertyName == null || "".equals(propertyName.trim()))
				propertyName = "*";
			
			queryString += "and model.tipo " + (tipoMovimiento == -1 ? "<>" : "=") + " :tipoMovimiento ";
			if (propertyValue != null && ! "".equals(propertyValue.trim())) {
				if ("*".equals(propertyName.trim())) {
					queryString += "and (cast(model.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idAlmacen as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idOrdenCompra as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.entrega as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.recibe as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or lower(model.folioFactura) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.folioOrdenCompra) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(a.nombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(a.identificador) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.entregaNombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.recibeNombre) like '%" + propertyValue.toLowerCase().trim() + "%') ";
				} else if ("nombre".equals(propertyName.trim())) {
					queryString += "and (lower(a.nombre) like '%" + propertyValue.toLowerCase().trim() + "%' or lower(a.identificador) like '%" + propertyValue.toString().trim() + "%' ";
					queryString += "or lower(model.entregaNombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.recibeNombre) like '%" + propertyValue.toLowerCase().trim() + "%') ";
				} else if ("fecha".equals(propertyName.trim())) {
					queryString += "and cast(date(model.fecha) as string) like '%" + propertyValue.trim() + "%' ";
				} else if ("id".equals(propertyName.trim())) {
					queryString += "and (cast(model.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idAlmacen as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.entrega as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.recibe as string) like '%" + propertyValue.trim() + "%') ";
				} else {
					queryString += "and lower(cast(model." + propertyName.toString().trim() + " as string)) like '%" + propertyValue.toLowerCase().trim() + "%' ";
				}
			}
			
			if (tipoEntrada != null && ! "".equals(tipoEntrada.trim()))
				queryString += "and model.tipoEntrada = :tipoEntrada ";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("tipoMovimiento", tipoMovimiento);
			if (tipoEntrada != null && ! "".equals(tipoEntrada.trim()))
				query.setParameter("tipoEntrada", tipoEntrada);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findByEspecificField(String propertyName, final Object value, int tipoMovimiento) {
		String queryString = "select am from AlmacenMovimientos am where model.idEmpresa = :idEmpresa ";
		
		try {
			if (propertyName.equals("nombreAlmacen")) {
				queryString = "select am from AlmacenMovimientos am, Almacen a where model.idEmpresa = :idEmpresa and am.idAlmacen = a.id and am.tipo = '" + tipoMovimiento + "' and lower(a.nombre) like '%" + value.toString().toLowerCase() + "%' ";
			} else {
				if (propertyName.equals("nombreObra")) {
					queryString = "select am from AlmacenMovimientos am, Almacen a where model.idEmpresa = :idEmpresa and am.idAlmacen = a.id and am.tipo = '" + tipoMovimiento + "' and lower(am.nombreObra) like '%" + value.toString().toLowerCase() + "%' ";
				}
			}
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenMovimientos> findSalidaByTraspaso(long idTraspaso, int tipoTraspaso, int limite) {
		String queryString = "select am from AlmacenMovimientos mov, AlmacenTraspaso tr where model.idEmpresa = :idEmpresa and am.idAlmacen = a.id and mov.tipo = 1 and tr.tipo = :tipoTraspaso and tr.id = :idTraspaso ";
		
		try {
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", getIdEmpresa());
			query.setParameter("idTraspaso", idTraspaso);
			query.setParameter("tipoTraspaso", tipoTraspaso);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}
