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

import net.giro.inventarios.beans.AlmacenTraspaso;

@Stateless
public class AlmacenTraspasoImp extends DAOImpl<AlmacenTraspaso> implements AlmacenTraspasoDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(AlmacenTraspaso entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<AlmacenTraspaso> saveOrUpdateList(List<AlmacenTraspaso> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	/*@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findAll(long idEmpresa) {
		try {
			final String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and model.sistema in (0,1) order by model.fecha desc, model.id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findAllActivos(long idEmpresa) {
		try {
			final String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and model.estatus = 0 and model.sistema in (0,1) order by model.fecha desc, model.id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}*/

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findLike(String value, long idAlmacenOrigen, long idAlmacenDestino, int tipo, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, long idEmpresa, int limite) {
		String queryString = "select a.id from AlmacenTraspaso a, TraspasoDetalle b, Producto c where a.idEmpresa = :idEmpresa and b.idAlmacenTraspaso = a.id and c.id = b.idProducto ";
		List<String> valores = null;
		StringBuffer sb = null;
		 
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacenOrigen  = (idAlmacenOrigen  > 0 ? idAlmacenOrigen  : 0L);
			idAlmacenDestino = (idAlmacenDestino > 0 ? idAlmacenDestino : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(a.id as string) like :propertyValue ";
				queryString += "or trim(lower(a.entregaNombre)) like :propertyValue ";
				queryString += "or trim(lower(a.recibeNombre)) like :propertyValue ";
				queryString += "or trim(lower(a.nombreObra)) like :propertyValue ";
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

			if (idAlmacenOrigen > 0 && idAlmacenDestino > 0 && idAlmacenOrigen == idAlmacenDestino)
				queryString += "and :idAlmacenOrigen in (a.idAlmacenOrigen.id, a.idAlmacenDestino.id) and :idAlmacenDestino <> cast(0 as long) ";
			else if (idAlmacenOrigen > 0 && idAlmacenDestino > 0 && idAlmacenOrigen != idAlmacenDestino)
				queryString += "and (a.idAlmacenOrigen.id = :idAlmacenOrigen or a.idAlmacenDestino.id = :idAlmacenDestino) ";
			else if (idAlmacenOrigen > 0)
				queryString += "and a.idAlmacenOrigen.id = :idAlmacenOrigen ";
			else if (idAlmacenDestino > 0)
				queryString += "and a.idAlmacenDestino.id = :idAlmacenDestino ";
			if (tipo > 0)
				queryString += "and a.tipo = :tipo ";
			queryString += "and a.completo in (0" + ((incluyeCompleto) ? ",1" : "") + ") ";
			queryString += "and a.sistema in (0" + ((incluyeSistema) ? ",1" : "") + ") ";

			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "model.fecha desc, model.id desc ";
			} else 
				queryString = "select model from AlmacenTraspaso model where model.id in (:subqueries) ".replace(":subqueries", queryString);
			queryString += "order by " + orderBy.trim();
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idAlmacenOrigen > 0)
				query.setParameter("idAlmacenOrigen", idAlmacenOrigen);
			if (idAlmacenDestino > 0)
				query.setParameter("idAlmacenDestino", idAlmacenDestino);
			if (sb != null && ! "".equals(sb.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (tipo > 0)
				query.setParameter("tipo", tipo);
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
	public List<AlmacenTraspaso> findLikeProperty(String propertyName, Object value, long idAlmacenOrigen, long idAlmacenDestino, int tipo, boolean incluyeCompleto, boolean incluyeSistema, String orderBy, long idEmpresa, int limite) {
		String queryString = "select a.id from AlmacenTraspaso a, TraspasoDetalle b, Producto c where a.idEmpresa = :idEmpresa and b.idAlmacenTraspaso = a.id and c.id = b.idProducto ";
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		List<String> valores = null;
		StringBuffer sb = null;
		 
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			idAlmacenOrigen  = (idAlmacenOrigen  > 0 ? idAlmacenOrigen  : 0L);
			idAlmacenDestino = (idAlmacenDestino > 0 ? idAlmacenDestino : 0L);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "model.fecha desc, model.id desc");
			
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
					queryString += "and date(model.fecha) = date('" + formateador.format((Date) value) + "') ";
					value = "";
				} else if (value.getClass() == java.lang.String.class) {
					queryString += "and lower(trim(" + propertyName + ")) like :propertyValue ";
				} else {
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
			
			if (idAlmacenOrigen > 0 && idAlmacenDestino > 0 && idAlmacenOrigen == idAlmacenDestino)
				queryString += "and :idAlmacenOrigen in (model.idAlmacenOrigen.id, model.idAlmacenDestino.id) and :idAlmacenDestino <> cast(0 as long) ";
			else if (idAlmacenOrigen > 0 && idAlmacenDestino > 0 && idAlmacenOrigen != idAlmacenDestino)
				queryString += "and model.idAlmacenOrigen.id = :idAlmacenOrigen and model.idAlmacenDestino.id = :idAlmacenDestino ";
			else if (idAlmacenOrigen > 0)
				queryString += "and model.idAlmacenOrigen.id = :idAlmacenOrigen ";
			else if (idAlmacenDestino > 0)
				queryString += "and model.idAlmacenDestino.id = :idAlmacenDestino ";
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			queryString += "and model.completo in (0" + ((incluyeCompleto) ? ",1" : "") + ") ";
			queryString += "and model.sistema in (0" + ((incluyeSistema) ? ",1" : "") + ") ";

			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "model.fecha desc, model.id desc ";
			} else 
				queryString = "select model from AlmacenTraspaso model where model.id in (:subqueries) ".replace(":subqueries", queryString);
			queryString += "order by " + orderBy.trim();
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (idAlmacenOrigen > 0)
				query.setParameter("idAlmacenOrigen", idAlmacenOrigen);
			if (idAlmacenDestino > 0)
				query.setParameter("idAlmacenDestino", idAlmacenDestino);
			if (sb != null && ! "".equals(sb.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (tipo > 0)
				query.setParameter("tipo", tipo);
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
	public List<AlmacenTraspaso> findByProperty(String propertyName, Object value, int tipo, boolean incluyeCompleto, boolean incluyeSistema, long idEmpresa, int limite) {
		String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (value != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			queryString += "and model.completo in (0" + ((incluyeCompleto) ? ",1" : "") + ") ";
			queryString += "and model.sistema in (0" + ((incluyeSistema) ? ",1" : "") + ") ";
			queryString += "order by model.fecha desc, model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	/*@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacen(String nombreAlmacen, long idEmpresa) {
		String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa ";
		 
		try {
			if (nombreAlmacen != null && ! "".equals(nombreAlmacen.trim())) {
				nombreAlmacen = "%" + nombreAlmacen.trim().replace(" ", "%") + "%";
				queryString += "and ((lower(model.idAlmacenOrigen.nombre) like :propertyValue) or (lower(model.idAlmacenDestino.nombre) like :propertyValue)) ";
			}
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (nombreAlmacen != null && ! "".equals(nombreAlmacen.trim()))
				query.setParameter("propertyValue", nombreAlmacen);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacenOrigen(String nombreAlmacen, long idEmpresa){
		try {
			final String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and lower(model.idAlmacenOrigen..nombre) like '%"+ nombreAlmacen.toLowerCase() +"%' order by id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacenDestino(String nombreAlmacen, long idEmpresa){
		try {
			final String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and lower(model.idAlmacenDestino.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%' order by id desc";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findByAlmacenOrdenCompleta(int tipoAlmacen, String nombreAlmacen, long idEmpresa) {
		String queryString = "";
		
		try {
			switch (tipoAlmacen) {
				case 0:	//Todos
					queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and at.completo = 0 order by id desc";
					break;
				case 1:	//Destino
					queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and lower(model.idAlmacenDestino.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%' and model.completo = 0 order by id desc";
					break;
				case 2:	//Origen
					queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and lower(model.idAlmacenOrigen.nombre) like '%"+ nombreAlmacen.toLowerCase() +"%'  and model.completo = 0 order by id desc";
					break;
			}
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findLikeWithDestino(String value, long idAlmacenDestino, long idEmpresa) {
		String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and model.idAlmacenDestino.id = :almacenDestino ";
		
		try {
			if (value != null && ! "".equals(value.trim()))
				queryString += "and lower(model.idAlmacenDestino.nombre) like '%" + value.toLowerCase() + "%' ";
			queryString += "order by model.id desc";
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("almacenDestino", idAlmacenDestino);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AlmacenTraspaso> findIncompletosLikeProperty(String propertyName, String propertyValue, String orderBy, long idEmpresa, int limite) {
		String queryString = "select model from AlmacenTraspaso model where model.idEmpresa = :idEmpresa and model.completo = 0 ";
		
		try {
			if (propertyName == null || "".equals(propertyName.trim()))
				propertyName = "*";
			
			if (propertyValue != null) {
				if ("*".equals(propertyName.trim())) {
					queryString += "and (cast(model.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idAlmacenOrigen.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idAlmacenDestino.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idOrdenCompra as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.entrega as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.recibe as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or lower(model.idAlmacenOrigen.nombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.idAlmacenOrigen.identificador) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.idAlmacenDestino.nombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.idAlmacenDestino.identificador) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.entregaNombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.recibeNombre) like '%" + propertyValue.toLowerCase().trim() + "%') ";
				} else if ("nombre".equals(propertyName.trim())) {
					queryString += "and (lower(model.idAlmacenOrigen.nombre) like '%" + propertyValue.toLowerCase().trim() + "%' or lower(model.idAlmacenOrigen.identificador) like '%" + propertyValue.toString().trim() + "%' ";
					queryString += "or lower(model.idAlmacenDestino.nombre) like '%" + propertyValue.toLowerCase().trim() + "%' or lower(model.idAlmacenDestino.identificador) like '%" + propertyValue.toString().trim() + "%' ";
					queryString += "or lower(model.entregaNombre) like '%" + propertyValue.toLowerCase().trim() + "%' ";
					queryString += "or lower(model.recibeNombre) like '%" + propertyValue.toLowerCase().trim() + "%') ";
				} else if ("fecha".equals(propertyName.trim())) {
					queryString += "and cast(date(model.fecha) as string) like '%" + propertyValue.trim() + "%' ";
				} else if ("id".equals(propertyName.trim())) {
					queryString += "and (cast(model.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idAlmacenOrigen.id as string) like '%" + propertyValue.trim() + "%' ";
					queryString += "or cast(model.idAlmacenDestino.id as string) like '%" + propertyValue.trim() + "%' ";
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
			query.setParameter("idEmpresa", idEmpresa);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}*/

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
		return "select model from AlmacenTraspaso model where (" + queryModificada + ")";
	}
}
