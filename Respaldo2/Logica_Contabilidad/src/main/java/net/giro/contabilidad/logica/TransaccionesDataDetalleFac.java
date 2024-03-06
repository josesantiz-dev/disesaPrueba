package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.TransaccionesDataDetalle;
import net.giro.contabilidad.dao.TransaccionesDataDetalleDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class TransaccionesDataDetalleFac implements TransaccionesDataDetalleRem {
	private static Logger log = Logger.getLogger(TransaccionesDataDetalleFac.class);
	private InitialContext ctx;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private TransaccionesDataDetalleDAO ifzTransaccionesDataDetalles;
	private static String orderBy;
	
	public TransaccionesDataDetalleFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzTransaccionesDataDetalles = (TransaccionesDataDetalleDAO) this.ctx.lookup("ejb:/Model_Contabilidad//TransaccionesDataDetalleImp!net.giro.contabilidad.dao.TransaccionesDataDetalleDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.TransaccionesDataDetalleFac", e);
			ctx = null;
		}
	}

	@Override
	public void orderBy(String orderBy) {
		TransaccionesDataDetalleFac.orderBy = orderBy;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(TransaccionesDataDetalle entity) throws Exception {
		try {
			return this.ifzTransaccionesDataDetalles.save(entity, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.save(TransaccionesDataDetalle)", e);
			throw e;
		}
	}

	@Override
	public List<TransaccionesDataDetalle> saveOrUpdateList(List<TransaccionesDataDetalle> entities) throws Exception {
		try {
			return this.ifzTransaccionesDataDetalles.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.saveOrUpdateList(List<TransaccionesDataDetalle> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(TransaccionesDataDetalle entity) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.update(TransaccionesDataDetalle)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public TransaccionesDataDetalle findById(Long id) {
		try {
			return this.ifzTransaccionesDataDetalles.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<TransaccionesDataDetalle> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.orderBy(orderBy);
			return this.ifzTransaccionesDataDetalles.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionesDataDetalle> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.orderBy(orderBy);
			return this.ifzTransaccionesDataDetalles.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionesDataDetalle> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.orderBy(orderBy);
			return this.ifzTransaccionesDataDetalles.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionesDataDetalle> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.orderBy(orderBy);
			return this.ifzTransaccionesDataDetalles.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionesDataDetalle> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTransaccionesDataDetalles.orderBy(orderBy);
			return this.ifzTransaccionesDataDetalles.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionesDataDetalleFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
}