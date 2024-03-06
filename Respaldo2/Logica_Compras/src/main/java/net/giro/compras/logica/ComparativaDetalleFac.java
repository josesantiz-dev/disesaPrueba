package net.giro.compras.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.compras.beans.ComparativaDetalle;
import net.giro.compras.dao.ComparativaDetalleDAO;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ComparativaDetalleFac implements ComparativaDetalleRem {
	private static Logger log = Logger.getLogger(ComparativaDetalleFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ComparativaDetalleDAO ifzComparativaDetalles;
	private static String orderBy;
	
	
	public ComparativaDetalleFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);
            this.ifzComparativaDetalles = (ComparativaDetalleDAO) this.ctx.lookup("ejb:/Model_Compras//ComparativaDetalleImp!net.giro.compras.dao.ComparativaDetalleDAO");
        } catch (Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.ComparativaDetalleFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void OrderBy(String orderBy) {
		ComparativaDetalleFac.orderBy = orderBy;
	}

	@Override
	public Long save(ComparativaDetalle entity) throws ExcepConstraint {
		try {
			return this.ifzComparativaDetalles.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.save(ComparativaDetalle)", e);
			throw e;
		}
	}

	@Override
	public void update(ComparativaDetalle entity) throws ExcepConstraint {
		try {
			this.ifzComparativaDetalles.update(entity);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.ComparativaDetalleFac.update(ComparativaDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<ComparativaDetalle> saveOrUpdateList(List<ComparativaDetalle> entities) throws Exception {
		try {
			return this.ifzComparativaDetalles.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.ComparativaDetalleFac.saveOrUpdateList(List<ComparativaDetalle> entities)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long id) throws ExcepConstraint {
		try {
			this.ifzComparativaDetalles.delete(id);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.delete(id)", e);
			throw e;
		}
	}

	@Override
	public ComparativaDetalle findById(Long id) {
		try {
			return this.ifzComparativaDetalles.findById(id);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<ComparativaDetalle> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzComparativaDetalles.OrderBy(orderBy);
			return this.ifzComparativaDetalles.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ComparativaDetalle> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzComparativaDetalles.OrderBy(orderBy);
			return this.ifzComparativaDetalles.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<ComparativaDetalle> findInProperty(String columnName, List<Object> values) throws Exception {
		try {
			this.ifzComparativaDetalles.OrderBy(orderBy);
			return this.ifzComparativaDetalles.findInProperty(columnName, values);
		} catch (Exception e) {
			log.error("Error en ComparativaDetalleFac.findInProperty(columnName, values)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	private Long getIdEmpresa() {
		Long idEmpresa = 1L;
		
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getId();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
}
