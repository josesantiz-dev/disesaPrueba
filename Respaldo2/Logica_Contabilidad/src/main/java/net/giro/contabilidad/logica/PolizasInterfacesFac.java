package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.PolizasInterfaces;
import net.giro.contabilidad.dao.PolizasInterfacesDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class PolizasInterfacesFac implements PolizasInterfacesRem {
	private static Logger log = Logger.getLogger(PolizasInterfacesFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private PolizasInterfacesDAO ifzPolizasInterfacess;
	private static String orderBy;
	
	public PolizasInterfacesFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			
			this.ifzPolizasInterfacess = (PolizasInterfacesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//PolizasInterfacesImp!net.giro.contabilidad.dao.PolizasInterfacesDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.PolizasInterfacesFac", e);
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

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void orderBy(String orderBy) {
		PolizasInterfacesFac.orderBy = orderBy;
	}

	@Override
	public Long save(PolizasInterfaces entity) throws Exception {
		try {
			return this.ifzPolizasInterfacess.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.save(PolizasInterfaces)", e);
			throw e;
		}
	}

	@Override
	public List<PolizasInterfaces> saveOrUpdateList(List<PolizasInterfaces> entities) throws Exception {
		try {
			return this.ifzPolizasInterfacess.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.saveOrUpdateList(List<PolizasInterfaces> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(PolizasInterfaces entity) throws Exception {
		try {
			this.ifzPolizasInterfacess.setEmpresa(getIdEmpresa());
			this.ifzPolizasInterfacess.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.update(PolizasInterfaces)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzPolizasInterfacess.setEmpresa(getIdEmpresa());
			this.ifzPolizasInterfacess.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public PolizasInterfaces findById(Long id) {
		try {
			this.ifzPolizasInterfacess.setEmpresa(getIdEmpresa());
			return this.ifzPolizasInterfacess.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<PolizasInterfaces> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzPolizasInterfacess.setEmpresa(getIdEmpresa());
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<PolizasInterfaces> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzPolizasInterfacess.setEmpresa(getIdEmpresa());
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<PolizasInterfaces> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzPolizasInterfacess.setEmpresa(getIdEmpresa());
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<PolizasInterfaces> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzPolizasInterfacess.setEmpresa(getIdEmpresa());
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<PolizasInterfaces> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzPolizasInterfacess.setEmpresa(getIdEmpresa());
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
}