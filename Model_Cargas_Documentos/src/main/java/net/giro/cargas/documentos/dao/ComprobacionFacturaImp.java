package net.giro.cargas.documentos.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.giro.DAOImplFact;
import net.giro.cargas.documentos.beans.ComprobacionFactura;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ComprobacionFacturaImp extends DAOImplFact<ComprobacionFactura> implements ComprobacionFacturaDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public long save(ComprobacionFactura entity, long codigoEmpresa) throws Exception {
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
	public List<ComprobacionFactura> saveOrUpdateList(List<ComprobacionFactura> entities, long codigoEmpresa) throws Exception {
		try {
			codigoEmpresa = (codigoEmpresa <= 0L ? 1L : codigoEmpresa);
			return super.saveOrUpdateList(entities, codigoEmpresa);
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ComprobacionFactura> comprobar(String expresionImpresa, String orderBy, long idEmpresa) throws Exception {
		String queryString = "select model from ComprobacionFactura model where trim(model.expresionImpresa) = trim(:expresionImpresa) and model.idEmpresa = :idEmpresa and model.estatus = 0 ";
		
		try {
			idEmpresa = (idEmpresa <= 0L ? 1L : idEmpresa);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy : "model.id desc");
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
	public List<ComprobacionFactura> findByDates(Date fechaDesde, Date fechaHasta, String orderBy, long idEmpresa) throws Exception {
		String queryString = "select model from ComprobacionFactura model where date(model.facturaFecha) between date(:fechaDesde) and date(:fechaHasta) and model.idEmpresa = :idEmpresa and model.estatus = 0 ";
		
		try {
			fechaDesde = (fechaDesde != null ? fechaDesde : Calendar.getInstance().getTime());
			fechaHasta = (fechaHasta != null ? fechaHasta : Calendar.getInstance().getTime());
			idEmpresa = (idEmpresa <= 0L ? 1L : idEmpresa);
			orderBy = (orderBy != null && ! "".equals(orderBy.trim()) ? orderBy : "model.id desc");
			queryString += "order by " + orderBy;
			
			Query query = entityManager.createQuery(queryString);
			query.setParameter("fechaDesde", fechaDesde);
			query.setParameter("fechaHasta", fechaHasta);
			query.setParameter("idEmpresa", idEmpresa);
			return query.getResultList();
		} catch (Exception e) { 
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ComprobacionFactura> findInProperty(String propertyName, List<Object> values, long idEmpresa) throws Exception {
		String queryString = "select model from ComprobacionFactura model where model.idEmpresa = :idEmpresa and model.estatus = 0 ";
		String valores = "";
		
		try {
			if (values == null || values.isEmpty())
				return null;
			
			for (Object value : values) {
				if ("".equals(valores.trim()))
					valores += ",";
				valores += (value instanceof String ? "'" : "") + value.toString() + (value instanceof String ? "'" : "");
			}

			idEmpresa = (idEmpresa <= 0L ? 1L : idEmpresa);
			queryString += "and model.propertyName in (valores) ";
			queryString = queryString.replace("propertyName", propertyName);
			queryString = queryString.replace("valores", valores);
			
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
 *  VER |    FECHA   | 		AUTOR 		| DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | 2020-03-04 | Javier Tirado 	| Creacion de EJB
 */