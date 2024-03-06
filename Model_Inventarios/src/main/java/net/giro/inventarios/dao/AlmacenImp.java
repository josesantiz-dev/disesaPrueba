package net.giro.inventarios.dao;

import net.giro.DAOImpl;
import net.giro.inventarios.beans.Almacen;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AlmacenImp extends DAOImpl<Almacen> implements AlmacenDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(Almacen entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<Almacen> saveOrUpdateList(List<Almacen> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findAll(int tipo, boolean incluyeEliminados, long idEmpresa) throws Exception {
		String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa and model.estatus in (0) ";
		
		try {
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			if (incluyeEliminados)
				queryString = queryString.replace("(0)", "(0,1)");
			queryString += "order by case model.tipo when 1 then 1 when 3 then 2 when 2 then 3 when 4 then 4 else 5 end, model.nombre";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findLike(String value, int tipo, boolean incluyeEliminados, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa ";
		List<String> valores = null;
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			tipo = (tipo > 0 ? tipo : 0);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.tipo when 1 then 1 when 3 then 2 when 2 then 3 when 4 then 4 else 5 end, model.nombre ");
			if (value != null && ! "".equals(value.trim())) {
				queryString += "and (cast(model.id as string) like :propertyValue ";
				queryString += "or lower(trim(model.nombre)) like :propertyValue ";
				queryString += "or lower(trim(model.identificador)) like :propertyValue ";
				queryString += "or lower(trim(model.nombreEncargado)) like :propertyValue) ";

				valores = recuperaValores(value.toString().toLowerCase().trim(), "\\+");
				if (valores == null || valores.isEmpty()) {
					sb = new StringBuffer();
		    		sb.append("%");
		    		sb.append(value.trim().toLowerCase());
		    		sb.append("%");
				}
			} 
			

			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			queryString += "and model.estatus in (0" + (incluyeEliminados ? ",1" : "") + ") ";
			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "case model.tipo when 1 then 1 when 3 then 2 when 2 then 3 when 4 then 4 else 5 end, model.nombre ";
			}
			queryString += "order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (sb != null && ! "".equals(sb.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findLikeProperty(String propertyName, Object value, int tipo, boolean incluyeEliminados, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa ";
		List<String> valores = null;
		StringBuffer sb = null;
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			tipo = (tipo > 0 ? tipo : 0);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.tipo when 1 then 1 when 3 then 2 when 2 then 3 when 4 then 4 else 5 end, model.nombre ");
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "p.clave, model.id desc ");
			if (value != null) {
				if (value.getClass() == java.util.Date.class) {
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
			
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			queryString += "and model.estatus in (0" + (incluyeEliminados ? ",1" : "") + ") ";
			if (valores != null && ! valores.isEmpty()) {
				queryString = multiplicaConsulta(queryString, valores);
				orderBy = "case model.tipo when 1 then 1 when 3 then 2 when 2 then 3 when 4 then 4 else 5 end, model.nombre ";
			}
			queryString += "order by " + orderBy;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (sb != null && ! "".equals(sb.toString().trim()))
				query.setParameter("propertyValue", sb.toString());
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (limite > 0)
				query.setMaxResults(limite);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findByProperty(String propertyName, Object value, int tipo, boolean incluyeEliminados, long idEmpresa, String orderBy, int limite) throws Exception {
		String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa ";
		
		try {
			if (propertyName != null && ! "".equals(propertyName.trim()) && value != null)
				queryString += "and model." + propertyName + " = :propertyValue ";
			if (tipo > 0)
				queryString += "and model.tipo = :tipo ";
			queryString += "and model.estatus in (0" + (incluyeEliminados ? ",1" : "") + ") ";
			queryString += "order by case model.tipo when 1 then 1 when 3 then 2 when 2 then 3 when 4 then 4 else 5 end, model." + propertyName;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			if (tipo > 0)
				query.setParameter("tipo", tipo);
			if (propertyName != null && ! "".equals(propertyName.trim()) && value != null)
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
	public List<Almacen> comprobarPrincipal(Long idSucursal, Long idAlmacen, long idEmpresa) throws Exception {
		String queryString = "SELECT model FROM Almacen model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (idSucursal == null || idSucursal <= 0L) 
				return null;
			whereString = "and model.idSucursal = " + idSucursal + " AND model.tipo = 1 ";			
			if (idAlmacen != null && idAlmacen > 0L)
				whereString += "and model.id <> " + idAlmacen;
			queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> comprobarNombre(String nombre, Long idAlmacen, long idEmpresa) throws Exception {
		String queryString = "SELECT model FROM Almacen model where model.idEmpresa = :idEmpresa ";
		String whereString = "";
		
		try {
			if (nombre == null || "".equals(nombre)) 
				return null;
			whereString = "and model.nombre = '" + nombre + "' ";			
			if (idAlmacen != null && idAlmacen > 0L)
				whereString += "and model.id <> " + idAlmacen;
			queryString = queryString + whereString;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Almacen> findByTipo(List<Integer> tipos, boolean incluyeEliminados, long idEmpresa, String orderBy) throws Exception {
		String queryString = "select model from Almacen model where model.idEmpresa = :idEmpresa and model.estatus in (:estatus) ";
		String tipo = "";
		
		try {
			idEmpresa = (idEmpresa > 0 ? idEmpresa : 1L);
			tipos = (tipos != null ? tipos : new ArrayList<Integer>());
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy.trim() : "case model.tipo when 1 then 1 when 3 then 2 when 2 then 3 when 4 then 4 else 5 end, model.nombre ");
			if (! tipos.isEmpty()) {
				for (Integer item : tipos)
					tipo += (! "".equals(tipo) ? "," : "") + item.toString();
				queryString += "and model.tipo in (" + tipo + ") ";
			}
			queryString = incluyeEliminados ? queryString.replace(":estatus", "0,1") : queryString.replace(":estatus", "0");
			queryString += "order by case model.tipo when 1 then 1 when 3 then 2 when 2 then 3 when 4 then 4 else 5 end, model.nombre";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
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
			queryModificada += (! "".equals(queryModificada.trim()) ? "or " : "") +  "model.id in (" + queryOriginal.trim().replace("select model from", "select model.id from").replace(":propertyValue", "'%" + valor + "%'") + ")";
		return "select model from Almacen model where (" + queryModificada + ")";
	}
}
