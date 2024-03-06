package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.TransaccionConceptos;
import net.giro.contabilidad.dao.TransaccionConceptosDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class TransaccionConceptosFac implements TransaccionConceptosRem {
	private static Logger log = Logger.getLogger(TransaccionConceptosFac.class);
	private InitialContext ctx;
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private TransaccionConceptosDAO ifzTransaccionConceptoss;
	private static String orderBy;
	
	
	public TransaccionConceptosFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzTransaccionConceptoss = (TransaccionConceptosDAO) this.ctx.lookup("ejb:/Model_Contabilidad//TransaccionConceptosImp!net.giro.contabilidad.dao.TransaccionConceptosDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.TransaccionConceptosFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { /*this.convertidor.setMostrarSystemOut(value);*/ }

	@Override
	public void orderBy(String orderBy) { TransaccionConceptosFac.orderBy = orderBy; }

	@Override
	public Long save(TransaccionConceptos entity) throws Exception {
		try {
			return this.ifzTransaccionConceptoss.save(entity, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.save(TransaccionConceptos)", e);
			throw e;
		}
	}

	@Override
	public List<TransaccionConceptos> saveOrUpdateList(List<TransaccionConceptos> entities) throws Exception {
		try {
			return this.ifzTransaccionConceptoss.saveOrUpdateList(entities, null);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.saveOrUpdateList(List<TransaccionConceptos> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(TransaccionConceptos entity) throws Exception {
		try {
			this.ifzTransaccionConceptoss.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.update(TransaccionConceptos)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws Exception {
		try {
			this.ifzTransaccionConceptoss.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public TransaccionConceptos findById(Long id) {
		try {
			return this.ifzTransaccionConceptoss.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<TransaccionConceptos> findByProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.ifzTransaccionConceptoss.findByProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findByProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionConceptos> findLikeProperty(String propertyName, Object value, int max) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.ifzTransaccionConceptoss.findLikeProperty(propertyName, value, max);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findLikeProperty(propertyName, value, max)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionConceptos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);			
			return this.ifzTransaccionConceptoss.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionConceptos> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.ifzTransaccionConceptoss.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}
	
	@Override
	public List<TransaccionConceptos> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.ifzTransaccionConceptoss.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findLikeProperties(params, limite)", e);
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
//	  2.2	| 01/06/2016 | Javier Tirado	| Creacion de TransaccionConceptosFac