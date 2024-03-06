package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.PolizasDetalles;
import net.giro.contabilidad.dao.PolizasDetallesDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class PolizasDetallesFac implements PolizasDetallesRem {
	private static Logger log = Logger.getLogger(PolizasDetallesFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private PolizasDetallesDAO ifzPolizasDetalless;
	private static String orderBy;
	
	public PolizasDetallesFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzPolizasDetalless = (PolizasDetallesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//PolizasDetallesImp!net.giro.contabilidad.dao.PolizasDetallesDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.PolizasDetallesFac", e);
			ctx = null;
		}
	}


	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	private Long getCodigoEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getCodigo();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		//this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void orderBy(String orderBy) {
		PolizasDetallesFac.orderBy = orderBy;
	}

	@Override
	public Long save(PolizasDetalles entity) throws Exception {
		try {
			return this.ifzPolizasDetalless.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.save(PolizasDetalles)", e);
			throw e;
		}
	}

	@Override
	public List<PolizasDetalles> saveOrUpdateList(List<PolizasDetalles> entities) throws Exception {
		try {
			return this.ifzPolizasDetalless.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.saveOrUpdateList(List<PolizasDetalles> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(PolizasDetalles entity) throws Exception {
		try {
			this.ifzPolizasDetalless.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.update(PolizasDetalles)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzPolizasDetalless.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public PolizasDetalles findById(Long id) {
		try {
			return this.ifzPolizasDetalless.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<PolizasDetalles> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzPolizasDetalless.orderBy(orderBy);
			return this.ifzPolizasDetalless.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<PolizasDetalles> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzPolizasDetalless.orderBy(orderBy);
			return this.ifzPolizasDetalless.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<PolizasDetalles> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzPolizasDetalless.orderBy(orderBy);
			return this.ifzPolizasDetalless.findInProperty(columnName, values, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<PolizasDetalles> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzPolizasDetalless.orderBy(orderBy);
			return this.ifzPolizasDetalless.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<PolizasDetalles> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzPolizasDetalless.orderBy(orderBy);
			return this.ifzPolizasDetalless.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasDetallesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
}