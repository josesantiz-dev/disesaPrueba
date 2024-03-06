package net.giro.contabilidad.logica;

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
	
	public PolizasInterfacesFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(environment);
			this.ifzPolizasInterfacess = (PolizasInterfacesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//PolizasInterfacesImp!net.giro.contabilidad.dao.PolizasInterfacesDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.PolizasInterfacesFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(PolizasInterfaces entity) throws Exception {
		try {
			return this.ifzPolizasInterfacess.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.save(PolizasInterfaces)", e);
			throw e;
		}
	}

	@Override
	public List<PolizasInterfaces> saveOrUpdateList(List<PolizasInterfaces> entities) throws Exception {
		try {
			return this.ifzPolizasInterfacess.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.saveOrUpdateList(List<PolizasInterfaces> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(PolizasInterfaces entity) throws Exception {
		try {
			this.ifzPolizasInterfacess.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.update(PolizasInterfaces)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idPolizasInterfaces) throws Exception {
		try {
			this.ifzPolizasInterfacess.delete(idPolizasInterfaces);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.delete(idPolizasInterfaces)", e);
			throw e;
		}
	}

	@Override
	public PolizasInterfaces findById(Long idPolizasInterfaces) {
		try {
			return this.ifzPolizasInterfacess.findById(idPolizasInterfaces);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findById(idPolizasInterfaces)", e);
			throw e;
		}
	}

	@Override
	public List<PolizasInterfaces> findAll(long idMensajeTransaccion, String orderBy) throws Exception {
		try {
			return this.ifzPolizasInterfacess.findAll(idMensajeTransaccion, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findAll(idMensajeTransaccion, orderBy)", e);
			throw e;
		} 
	}

	@Override
	public List<PolizasInterfaces> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzPolizasInterfacess.findByProperty(propertyName, value, orderBy, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<PolizasInterfaces> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzPolizasInterfacess.findLikeProperty(propertyName, value, orderBy, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	/*@Override
	public List<PolizasInterfaces> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findInProperty(columnName, values, getIdEmpresa(), limite);
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
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findByProperties(params, getIdEmpresa(), limite);
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
			this.ifzPolizasInterfacess.orderBy(orderBy);
			return this.ifzPolizasInterfacess.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.PolizasInterfacesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}*/

	// -------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// -------------------------------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L;
	}

	private Long getCodigoEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L;
	}
}