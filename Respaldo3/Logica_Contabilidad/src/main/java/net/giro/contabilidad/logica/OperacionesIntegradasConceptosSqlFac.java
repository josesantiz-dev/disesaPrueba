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

import net.giro.contabilidad.beans.OperacionesIntegradasConceptosSql;
import net.giro.contabilidad.dao.OperacionesIntegradasConceptosSqlDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class OperacionesIntegradasConceptosSqlFac implements OperacionesIntegradasConceptosSqlRem {
	private static Logger log = Logger.getLogger(OperacionesIntegradasConceptosSqlFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private OperacionesIntegradasConceptosSqlDAO ifzIntegradaConceptos;
	private static String orderBy;
	
	public OperacionesIntegradasConceptosSqlFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzIntegradaConceptos = (OperacionesIntegradasConceptosSqlDAO) this.ctx.lookup("ejb:/Model_Contabilidad//OperacionesIntegradasConceptosSqlImp!net.giro.contabilidad.dao.OperacionesIntegradasConceptosSqlDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void orderBy(String orderBy) { OperacionesIntegradasConceptosSqlFac.orderBy = orderBy; }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(OperacionesIntegradasConceptosSql entity) throws Exception {
		try {
			return this.ifzIntegradaConceptos.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.save(TransaccionConceptosSql)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<OperacionesIntegradasConceptosSql> saveOrUpdateList(List<OperacionesIntegradasConceptosSql> entities) throws Exception {
		try {
			return this.ifzIntegradaConceptos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.save(TransaccionConceptosSql)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(OperacionesIntegradasConceptosSql entity) throws Exception {
		try {
			this.ifzIntegradaConceptos.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.update(TransaccionConceptosSql)", e);
			throw e;
		}
	}

	@Override
	public void delete(long idOperacionIntegradaConceptosSql) throws Exception {
		try {
			this.ifzIntegradaConceptos.delete(idOperacionIntegradaConceptosSql);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.delete(idOperacionIntegradaConceptosSql)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasConceptosSql findById(long idOperacionIntegradaConceptosSql) {
		try {
			return this.ifzIntegradaConceptos.findById(idOperacionIntegradaConceptosSql);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findById(idOperacionIntegradaConceptosSql)", e);
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findAll(long idOperacionIntegrada) throws Exception {
		try {
			return this.findAll(idOperacionIntegrada, "");
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findAll(idOperacionIntegrada)", e);
			throw e;
		} 
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findAll(long idOperacionIntegrada, String orderBy) throws Exception {
		try {
			return this.ifzIntegradaConceptos.findAll(idOperacionIntegrada, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findByOperacionIntegrada(idOperacionIntegrada, orderBy)", e);
			throw e;
		} 
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findByOperacionIntegrada(long idOperacionIntegrada) throws Exception {
		try {
			return this.findByProperty("idOperacionIntegrada.id", idOperacionIntegrada, 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findByOperacionIntegrada(idOperacionIntegrada)", e);
			throw e;
		} 
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzIntegradaConceptos.orderBy(orderBy);
			return this.ifzIntegradaConceptos.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzIntegradaConceptos.orderBy(orderBy);
			return this.ifzIntegradaConceptos.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzIntegradaConceptos.orderBy(orderBy);
			return this.ifzIntegradaConceptos.findInProperty(columnName, values, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzIntegradaConceptos.orderBy(orderBy);
			return this.ifzIntegradaConceptos.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzIntegradaConceptos.orderBy(orderBy);
			return this.ifzIntegradaConceptos.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findLikeProperties(params, limite)", e);
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

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasConceptosSqlFac