package net.giro.cargas.documentos.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.giro.DAOImplFact;
import net.giro.cargas.documentos.beans.ComprobacionPago;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ComprobacionPagosImp extends DAOImplFact<ComprobacionPago> implements ComprobacionPagosDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(ComprobacionPago entity, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa <= 0L ? 1L : codigoEmpresa);
			if (entity.getId() == null || entity.getId() <= 0L)
				return super.save(entity, codigoEmpresa);
			super.update(entity);
			return entity.getId();
		} catch (Exception re) {
			throw re;
		}
	}
	
	@Override
	public List<ComprobacionPago> saveOrUpdateList(List<ComprobacionPago> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa <= 0L ? 1L : codigoEmpresa);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ComprobacionPago> comprobar(String expresionImpresa, boolean incluyeCancelados, String orderBy, long idEmpresa) throws Exception {
		String queryString = "select model from ComprobacionPago model where model.idEmpresa = :idEmpresa and trim(model.expresionImpresa) = trim(:expresionImpresa) ";
		
		try {
			idEmpresa = (idEmpresa <= 0L ? 1L : idEmpresa);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy : "model.id desc");
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1) " : ") ");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("expresionImpresa", expresionImpresa);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception e) { 
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ComprobacionPago> findByDates(Date fechaDesde, Date fechaHasta, boolean incluyeCancelados, String orderBy, long idEmpresa) throws Exception {
		String queryString = "select model from ComprobacionPago model where model.idEmpresa = :idEmpresa ";
		
		try {
			fechaDesde = (fechaDesde != null ? fechaDesde : Calendar.getInstance().getTime());
			fechaHasta = (fechaHasta != null ? fechaHasta : Calendar.getInstance().getTime());
			idEmpresa = (idEmpresa <= 0L ? 1L : idEmpresa);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy : "model.id desc");

			if (fechaDesde != null && fechaHasta != null)
				queryString += "and ((date(model.fecha) between date(:fechaDesde) and date(:fechaHasta)) or (date(model.pagoFecha) between date(:fechaDesde) and date(:fechaHasta)))";
			else if (fechaDesde != null && fechaHasta == null)
				queryString += "and date(:fechaDesde) between date(model.fecha) and date(model.pagoFecha)";
			else if (fechaDesde == null && fechaHasta != null)
				queryString += "and date(:fechaHasta) between date(model.fecha) and date(model.pagoFecha)";
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1) " : ") ");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			if (fechaDesde != null)
				query.setParameter("fechaDesde", fechaDesde);
			if (fechaHasta != null)
				query.setParameter("fechaHasta", fechaHasta);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception e) { 
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ComprobacionPago> findInProperty(String propertyName, List<Object> values, boolean incluyeCancelados, String orderBy, long idEmpresa) throws Exception {
		String queryString = "select model from ComprobacionPago model where model.idEmpresa = :idEmpresa ";
		String valores = "";
		
		try {
			idEmpresa = (idEmpresa <= 0L ? 1L : idEmpresa);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy : "model.id desc");
			if (values == null || values.isEmpty())
				return null;
			
			for (Object value : values) {
				if ("".equals(valores.trim()))
					valores += ",";
				valores += (value instanceof String ? "'" : "") + value.toString() + (value instanceof String ? "'" : "");
			}

			queryString += "and model.propertyName in (:valores) ";
			queryString = queryString.replace("propertyName", propertyName);
			queryString = queryString.replace(":valores", valores);
			queryString += "and model.estatus in (0" + (incluyeCancelados ? ",1) " : ") ");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception e) { 
			throw e;
		}
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA    | 		AUTOR 		 | DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-03-04  | Javier Tirado 		 | Creacion de EJB
 */