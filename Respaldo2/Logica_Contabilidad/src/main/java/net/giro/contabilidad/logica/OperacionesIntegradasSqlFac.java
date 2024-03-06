package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
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
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private OperacionesIntegradasSqlDAO ifzOperacionesIntegradasSqls;
	private static String orderBy;
	
	public OperacionesIntegradasSqlFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzOperacionesIntegradasSqls = (OperacionesIntegradasSqlDAO) this.ctx.lookup("ejb:/Model_Contabilidad//OperacionesIntegradasSqlImp!net.giro.contabilidad.dao.OperacionesIntegradasSqlDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.OperacionesIntegradasSqlFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { /*this.convertidor.setMostrarSystemOut(value);*/ }

	@Override
	public void orderBy(String orderBy) { OperacionesIntegradasSqlFac.orderBy = orderBy; }

	@Override
	public Long save(OperacionesIntegradasSql entity) throws Exception {
		try {
			return this.ifzOperacionesIntegradasSqls.save(entity, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.save(OperacionesIntegradasSql)", e);
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradasSql> saveOrUpdateList(List<OperacionesIntegradasSql> entities) throws Exception {
		try {
			return this.ifzOperacionesIntegradasSqls.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.saveOrUpdateList(List<OperacionesIntegradasSql> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(OperacionesIntegradasSql entity) throws Exception {
		try {
			this.ifzOperacionesIntegradasSqls.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.update(OperacionesIntegradasSql)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzOperacionesIntegradasSqls.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasSql findById(Long id) {
		try {
			return this.ifzOperacionesIntegradasSqls.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradasSql> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOperacionesIntegradasSqls.orderBy(orderBy);
			return this.ifzOperacionesIntegradasSqls.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasSql> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOperacionesIntegradasSqls.orderBy(orderBy);
			return this.ifzOperacionesIntegradasSqls.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzOperacionesIntegradasSqls.orderBy(orderBy);
			return this.ifzOperacionesIntegradasSqls.findInProperty(columnName, values, limite);
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
			this.ifzOperacionesIntegradasSqls.orderBy(orderBy);
			return this.ifzOperacionesIntegradasSqls.findByProperties(params, limite);
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
			this.ifzOperacionesIntegradasSqls.orderBy(orderBy);
			return this.ifzOperacionesIntegradasSqls.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasSqlFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasSqlFac