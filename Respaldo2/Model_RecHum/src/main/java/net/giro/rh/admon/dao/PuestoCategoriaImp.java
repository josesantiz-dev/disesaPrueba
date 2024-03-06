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
	private Long idEmpresa;
	
	private Long getIdEmpresa() {
		return (this.idEmpresa == null || this.idEmpresa <= 0L ? 1L : this.idEmpresa);
	}

	@Override
	public void setEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Override
	public long save(PuestoCategoria entity) throws Exception {
		try {
			return super.save(entity , getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PuestoCategoria> saveOrUpdateList(List<PuestoCategoria> entities) throws Exception {
		try {
			return super.saveOrUpdateList(entities , getIdEmpresa());
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByProperty(String propertyName, final Object value) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from PuestoCategoria model where model.idEmpresa = :idEmpresa and model."
					+ propertyName + " = " + value.toString();
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa" , getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByPuestoCategoria(long idPuesto, long idCategoria) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from PuestoCategoria model where model.idEmpresa = :idEmpresa and model.idPuesto = " + idPuesto +" and model.idCategoria = "+idCategoria + " and model.estatus = 0";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa" , getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findByIdPuesto(long idPuesto) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from PuestoCategoria model where model.idEmpresa = :idEmpresa and model.idPuesto = " + idPuesto ;
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa" , getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findAll(Long idEmpresa) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from PuestoCategoria model where model.idEmpresa = :idEmpresa ";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa" , getIdEmpresa());
			return query.getResultList();
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PuestoCategoria> findLikeClaveNombre(String value) {
		try {
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			final String queryString = "select model from PuestoCategoria model where model.idEmpresa = :idEmpresa " + 
			(value == null || "".equals(value) ? "" : "and model.catAreasId like '%" + value + "%' or lower(model.descripcion) like '%" + value.toLowerCase() + "%'");
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa" , getIdEmpresa());
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (Exception re) {
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
			if (idEmpresa == null || idEmpresa <= 0)
				idEmpresa = 1L;
			queryString = "select model from PuestoCategoria model where model.idEmpresa = :idEmpresa ";
			for (Entry<String, Object> e : params.entrySet()) {
				if (whereString.length() > 0)
					whereString += "and ";
				if (e.getValue().getClass() == java.util.Date.class) 
					whereString += "date(model." + e.getKey() + ") = date('" + formateador.format((Date) e.getValue()) + "') ";
				else if (e.getValue().getClass() == java.math.BigDecimal.class) 
					whereString += "cast(model." + e.getKey() + " as string) = '" + ((BigDecimal) e.getValue()).toString() + "' ";
				else
					whereString += "cast(model." + e.getKey() + " as string) = '" + e.getValue().toString() + "' ";
			}
			
			if (! whereString.isEmpty())
				queryString += whereString;

			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa" , getIdEmpresa());
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