package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.OperacionesIntegradasTransacciones;
import net.giro.contabilidad.beans.Transacciones;
import net.giro.contabilidad.dao.OperacionesIntegradasTransaccionesDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class OperacionesIntegradasTransaccionesFac implements OperacionesIntegradasTransaccionesRem {
	private static Logger log = Logger.getLogger(OperacionesIntegradasTransaccionesFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private OperacionesIntegradasTransaccionesDAO ifzIntegradas;
	private static String orderBy;
	
	public OperacionesIntegradasTransaccionesFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzIntegradas = (OperacionesIntegradasTransaccionesDAO) this.ctx.lookup("ejb:/Model_Contabilidad//OperacionesIntegradasTransaccionesImp!net.giro.contabilidad.dao.OperacionesIntegradasTransaccionesDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.OperacionesIntegradasTransaccionesFac", e);
			ctx = null;
		}
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
		OperacionesIntegradasTransaccionesFac.orderBy = orderBy;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(OperacionesIntegradasTransacciones entity) throws Exception {
		try {
			return this.ifzIntegradas.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.save(OperacionesIntegradasTransacciones)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<OperacionesIntegradasTransacciones> saveOrUpdateList(List<OperacionesIntegradasTransacciones> entities) throws Exception {
		try {
			return this.ifzIntegradas.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.saveOrUpdateList(List<OperacionesIntegradasTransacciones> entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(OperacionesIntegradasTransacciones entity) throws Exception {
		try {
			this.ifzIntegradas.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.update(OperacionesIntegradasTransacciones)", e);
			throw e;
		}
	}

	@Override
	public void delete(long idOperacionIntegrada) throws Exception {
		try {
			this.ifzIntegradas.delete(idOperacionIntegrada);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.delete(idOperacionIntegrada)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasTransacciones findById(long idOperacionIntegrada) {
		try {
			return this.ifzIntegradas.findById(idOperacionIntegrada);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findById(idOperacionIntegrada)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasTransacciones findByTransaccion(Transacciones transaccion) throws Exception {
		try {
			return this.ifzIntegradas.findByTransaccion(transaccion);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findByIdTransaccion(idTransaccion)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasTransacciones findByCodigoTransaccion(long codigoTransaccion) throws Exception {
		try {
			return this.ifzIntegradas.findByCodigoTransaccion(codigoTransaccion, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findByTransaccion(codigoTransaccion)", e);
			throw e;
		}
	}
	
	@Override
	public List<OperacionesIntegradasTransacciones> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzIntegradas.orderBy(orderBy);
			return this.ifzIntegradas.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasTransacciones> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzIntegradas.orderBy(orderBy);
			return this.ifzIntegradas.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasTransacciones> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzIntegradas.orderBy(orderBy);
			return this.ifzIntegradas.findInProperty(columnName, values, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasTransacciones> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzIntegradas.orderBy(orderBy);
			return this.ifzIntegradas.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasTransacciones> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzIntegradas.orderBy(orderBy);
			return this.ifzIntegradas.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasTransaccionesFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------------
	// PRIVADAS
	// -----------------------------------------------------------------------------------------------------------------------

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
}