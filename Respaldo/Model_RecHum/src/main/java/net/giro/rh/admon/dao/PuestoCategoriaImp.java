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
public class PuestoCategoriaImp extends DAOImpl<PuestoCategoria> implements PuestoCategoriaDAO  {

	@PersistenceContext
	private EntityManager entityManager;

	public void delete(PuestoCategoria entity) {
		try {
			entity = entityManager.getReference(PuestoCategoria.class, entity.getId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void update(PuestoCategoria entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public PuestoCategoria findById(Integer id) {
		try {
			PuestoCategoria instance = entityManager.find(PuestoCategoria.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByProperty(String propertyName, final Object value) {
		try {
			final String queryString = "select model from PuestoCategoria model where model."
					+ propertyName + " = " + value.toString();
			Query query = entityManager.createQuery(queryString);
			//query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByPuestoCategoria(int idPuesto, int idCategoria) {
		try {
			final String queryString = "select model from PuestoCategoria model where model.idPuesto = " + idPuesto +" and model.idCategoria = "+idCategoria + " and model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			//query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByIdPuesto(int idPuesto) {
		try {
			final String queryString = "select model from PuestoCategoria model where model.idPuesto = " + idPuesto ;
			Query query = entityManager.createQuery(queryString);
			//query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findAll() {
		try {
			final String queryString = "select model from PuestoCategoria model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findLikeClaveNombre(String value) {
		try {
			final String queryString = "select model from PuestoCategoria model" + 
			("".equals(value) || value == null ? "" : " where model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public List<PuestoCategoria> findByPropertyPojoCompleto( String propertyName, String tipo, long value) {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByProperties(HashMap<String, Object> params) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy");
		String queryString = "";
		String whereString = "";
		
		try {
			queryString = "select model from PuestoCategoria model ";
			for(Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += " and";
				
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += " date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "')";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += " cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "'";
				else
					whereString += " cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "'";
			}
			
			if (! whereString.isEmpty())
				queryString = queryString + " where " + whereString;

			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}


//HISTORIAL DE MODIFICACIONES 
//-------------------------------------------------------------------------------------------------------------------
//VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
//-------------------------------------------------------------------------------------------------------------------
//  1.0   | 2016-09-20 | Javier Tirado      | Se agrego filtro por estatus = 0 en el metodo findByPuestoCategoria