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

import net.giro.contabilidad.beans.OperacionesIntegradasSql;
import net.giro.contabilidad.dao.OperacionesIntegradasSqlDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class OperacionesIntegradasSqlFac implements OperacionesIntegradasSqlRem {
	private static Logger log = Logger.getLogger(OperacionesIntegradasSqlFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private OperacionesIntegradasSqlDAO ifzIntegradasSQL;
	private static String orderBy;
	
	public OperacionesIntegradasSqlFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzIntegradasSQL = (OperacionesIntegradasSqlDAO) this.ctx.lookup("ejb:/Model_Contabilidad//OperacionesIntegradasSqlImp!net.giro.contabilidad.dao.OperacionesIntegradasSqlDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.OperacionesIntegradasSqlFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void orderBy(String orderBy) { OperacionesIntegradasSqlFac.orderBy = orderBy; }

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(OperacionesIntegradasSql entity) throws Exception {
		try {
			return this.ifzIntegradasSQL.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.save(OperacionesIntegradasSql)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<OperacionesIntegradasSql> saveOrUpdateList(List<OperacionesIntegradasSql> entities) throws Exception {
		try {
			return this.ifzIntegradasSQL.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.saveOrUpdateList(List<OperacionesIntegradasSql> entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(OperacionesIntegradasSql entity) throws Exception {
		try {
			this.ifzIntegradasSQL.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.update(OperacionesIntegradasSql)", e);
			throw e;
		}
	}

	@Override
	public void delete(long idOperacionIntegradaSql) throws Exception {
		try {
			this.ifzIntegradasSQL.delete(idOperacionIntegradaSql);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.delete(idOperacionIntegradaSql)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasSql findById(long idOperacionIntegradaSql) {
		try {
			return this.ifzIntegradasSQL.findById(idOperacionIntegradaSql);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findById(idOperacionIntegradaSql)", e);
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradasSql> findAll(long idOperacionIntegrada) throws Exception {
		try {
			return this.findAll(idOperacionIntegrada, "");
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findAll(idOperacionIntegrada)", e);
			throw e;
		} 
	}

	@Override
	public List<OperacionesIntegradasSql> findAll(long idOperacionIntegrada, String orderBy) throws Exception {
		try {
			return this.ifzIntegradasSQL.findAll(idOperacionIntegrada, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findAll(idOperacionIntegrada, orderBy)", e);
			throw e;
		} 
	}

	@Override
	public List<OperacionesIntegradasSql> findByOperacionIntegrada(long idOperacionIntegrada) throws Exception {
		try {
			return this.findByProperty("idOperacionIntegrada.id", idOperacionIntegrada, 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findByOperacionIntegrada(idOperacionIntegrada)", e);
			throw e;
		} 
	}

	@Override
	public List<OperacionesIntegradasSql> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzIntegradasSQL.orderBy(orderBy);
			return this.ifzIntegradasSQL.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasSql> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzIntegradasSQL.orderBy(orderBy);
			return this.ifzIntegradasSQL.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzIntegradasSQL.orderBy(orderBy);
			return this.ifzIntegradasSQL.findInProperty(columnName, values, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasSql> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzIntegradasSQL.orderBy(orderBy);
			return this.ifzIntegradasSQL.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasSql> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzIntegradasSQL.orderBy(orderBy);
			return this.ifzIntegradasSQL.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findLikeProperties(params, limite)", e);
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
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasSqlFac