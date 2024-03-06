package net.giro.rh.admon.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.giro.DAOImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.giro.rh.admon.beans.PuestoCategoria;

@Stateless
public class PuestoCategoriaImp extends DAOImpl<PuestoCategoria> implements PuestoCategoriaDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public long save(PuestoCategoria entity, long codigoEmpresa) throws Exception {
		try {
			return super.save(entity , codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PuestoCategoria> saveOrUpdateList(List<PuestoCategoria> entities, long codigoEmpresa) throws Exception {
		try {
			return super.saveOrUpdateList(entities , codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByProperty(String propertyName, final Object value, long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from PuestoCategoria model where model.idPuesto.idEmpresa = :idEmpresa and model." + propertyName + " = " + value.toString();
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByPuestoCategoria(long idPuesto, long idCategoria, long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from PuestoCategoria model where model.idPuesto.idEmpresa = :idEmpresa and model.idPuesto.id = " + idPuesto +" and model.idCategoria.id = "+idCategoria + " and model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByIdPuesto(long idPuesto, long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from PuestoCategoria model where model.idPuesto.idEmpresa = :idEmpresa and model.idPuesto.id = " + idPuesto ;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findAll(long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from PuestoCategoria model where model.idPuesto.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findLikeClaveNombre(String value, long idEmpresa) {
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from PuestoCategoria model where model.idPuesto.idEmpresa = :idEmpresa " + 
			(value == null || "".equals(value) ? "" : "and model.idCategoria.descripcion like '%" + value + "%' or lower(model.idPuesto.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PuestoCategoria> findByPropertyPojoCompleto( String propertyName, String tipo, long value, long idEmpresa) {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByProperties(HashMap<String, Object> params, long idEmpresa) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "select model from PuestoCategoria model where model.idPuesto.idEmpresa = :idEmpresa ";
		//String whereString = "";
		
		try {
			if (idEmpresa <= 0)
				idEmpresa = 1L;
			/*if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			queryString = "select model from PuestoCategoria model where model.idPuesto.idEmpresa = :idEmpresa ";*/
			for (Entry<String, Object> e : params.entrySet()) {
				/*if (whereString.length() > 0)
					whereString += "and ";*/
				if (e.getValue().getClass() == java.util.Date.class) 
					queryString += "and date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					queryString += "and cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					queryString += "and cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			/*if (! whereString.isEmpty())
				queryString += whereString;*/

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}
}


//HISTORIAL DE MODIFICACIONES 
//-------------------------------------------------------------------------------------------------------------------
//VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
//-------------------------------------------------------------------------------------------------------------------
//  1.0   | 2016-09-20 | Javier Tirado      | Se agrego filtro por estatus = 0 en el metodo findByPuestoCategoria