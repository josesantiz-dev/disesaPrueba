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
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { /*this.convertidor.setMostrarSystemOut(value);*/ }

	@Override
	public void orderBy(String orderBy) { TransaccionConceptosFac.orderBy = orderBy; }

	@Override
	public Long save(TransaccionConceptos entity) throws Exception {
		try {
			return this.ifzTransaccionConceptoss.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.save(TransaccionConceptos)", e);
			throw e;
		}
	}

	@Override
	public List<TransaccionConceptos> saveOrUpdateList(List<TransaccionConceptos> entities) throws Exception {
		try {
			return this.ifzTransaccionConceptoss.saveOrUpdateList(entities, getCodigoEmpresa());
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
	public void delete(long idTransaccionConceptos) throws Exception {
		try {
			this.ifzTransaccionConceptoss.delete(idTransaccionConceptos);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.delete(idTransaccionConceptos)", e);
			throw e;
		}
	}

	@Override
	public TransaccionConceptos findById(long idTransaccionConceptos) {
		try {
			return this.ifzTransaccionConceptoss.findById(idTransaccionConceptos);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findById(idTransaccionConceptos)", e);
			throw e;
		}
	}

	@Override
	public List<TransaccionConceptos> findByTransaccion(long idTransaccion) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.findByProperty("idTransaccion.id", idTransaccion, 0);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionConceptos> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.ifzTransaccionConceptoss.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionConceptos> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);
			return this.ifzTransaccionConceptoss.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.TransaccionConceptosFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<TransaccionConceptos> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzTransaccionConceptoss.orderBy(orderBy);			
			return this.ifzTransaccionConceptoss.findInProperty(columnName, values, getIdEmpresa(), limite);
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
			return this.ifzTransaccionConceptoss.findByProperties(params, getIdEmpresa(), limite);
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
			return this.ifzTransaccionConceptoss.findByProperties(params, getIdEmpresa(), limite);
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