package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
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
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private OperacionesIntegradasConceptosSqlDAO ifzOperInterConceptosSQL;
	private static String orderBy;
	
	public OperacionesIntegradasConceptosSqlFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try{
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzOperInterConceptosSQL = (OperacionesIntegradasConceptosSqlDAO) this.ctx.lookup("ejb:/Model_Contabilidad//OperacionesIntegradasConceptosSqlImp!net.giro.contabilidad.dao.OperacionesIntegradasConceptosSqlDAO");
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
	public Long save(OperacionesIntegradasConceptosSql entity) throws Exception {
		try {
			return this.ifzOperInterConceptosSQL.save(entity, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.save(TransaccionConceptosSql)", e);
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> saveOrUpdateList(List<OperacionesIntegradasConceptosSql> entities) throws Exception {
		try {
			return this.ifzOperInterConceptosSQL.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.save(TransaccionConceptosSql)", e);
			throw e;
		}
	}

	@Override
	public void update(OperacionesIntegradasConceptosSql entity) throws Exception {
		try {
			this.ifzOperInterConceptosSQL.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.update(TransaccionConceptosSql)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzOperInterConceptosSQL.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public OperacionesIntegradasConceptosSql findById(Long id) {
		try {
			return this.ifzOperInterConceptosSQL.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOperInterConceptosSQL.orderBy(orderBy);
			return this.ifzOperInterConceptosSQL.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzOperInterConceptosSQL.orderBy(orderBy);
			return this.ifzOperInterConceptosSQL.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<OperacionesIntegradasConceptosSql> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzOperInterConceptosSQL.orderBy(orderBy);
			return this.ifzOperInterConceptosSQL.findInProperty(columnName, values, limite);
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
			this.ifzOperInterConceptosSQL.orderBy(orderBy);
			return this.ifzOperInterConceptosSQL.findByProperties(params, limite);
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
			this.ifzOperInterConceptosSQL.orderBy(orderBy);
			return this.ifzOperInterConceptosSQL.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.OperacionesIntegradasConceptosSqlFac.findLikeProperties(params, limite)", e);
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
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de OperacionesIntegradasConceptosSqlFac