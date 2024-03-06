package net.giro.contabilidad.logica;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.ImpuestoEquivalencia;
import net.giro.contabilidad.dao.ImpuestoEquivalenciaDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class ImpuestoEquivalenciaFac implements ImpuestoEquivalenciaRem {
	private static Logger log = Logger.getLogger(ImpuestoEquivalenciaFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private ImpuestoEquivalenciaDAO ifzImpuestoEquivalencias;
	
	public ImpuestoEquivalenciaFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzImpuestoEquivalencias = (ImpuestoEquivalenciaDAO) this.ctx.lookup("ejb:/Model_Contabilidad//ImpuestoEquivalenciaImp!net.giro.contabilidad.dao.ImpuestoEquivalenciaDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.ImpuestoEquivalenciaFac", e);
			ctx = null;
		}
	}

	
	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(ImpuestoEquivalencia entity) throws Exception {
		try {
			return this.ifzImpuestoEquivalencias.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ImpuestoEquivalenciaFac.save(ImpuestoEquivalencia)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestoEquivalencia> saveOrUpdateList(List<ImpuestoEquivalencia> entities) throws Exception {
		try {
			return this.ifzImpuestoEquivalencias.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ImpuestoEquivalenciaFac.saveOrUpdateList(List<ImpuestoEquivalencia> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(ImpuestoEquivalencia entity) throws Exception {
		try {
			this.ifzImpuestoEquivalencias.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ImpuestoEquivalenciaFac.update(ImpuestoEquivalencia)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long idImpuestoEquivalencia) throws Exception {
		try {
			this.ifzImpuestoEquivalencias.delete(idImpuestoEquivalencia);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ImpuestoEquivalenciaFac.delete(idImpuestoEquivalencia)", e);
			throw e;
		}
	}
	
	@Override
	public ImpuestoEquivalencia findById(Long idImpuestoEquivalencia) {
		try {
			return this.ifzImpuestoEquivalencias.findById(idImpuestoEquivalencia);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ImpuestoEquivalenciaFac.findById(idImpuestoEquivalencia)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestoEquivalencia> findAll(long codigoTransaccion, String orderBy) throws Exception {
		try {
			return this.ifzImpuestoEquivalencias.findAll(codigoTransaccion, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ImpuestoEquivalenciaFac.findAll(codigoTransaccion, orderBy)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestoEquivalencia> findByProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzImpuestoEquivalencias.findByProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ImpuestoEquivalenciaFac.findByProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestoEquivalencia> findLikeProperty(String propertyName, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzImpuestoEquivalencias.findLikeProperty(propertyName, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ImpuestoEquivalenciaFac.findLikeProperty(propertyName, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<ImpuestoEquivalencia> findInProperty(String propertyName, List<Object> values, String orderBy, int limite) throws Exception {
		try {
			return this.ifzImpuestoEquivalencias.findInProperty(propertyName, values, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.ImpuestoEquivalenciaFac.findInProperty(propertyName, values, orderBy, limite)", e);
			throw e;
		} 
	}

	// ------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------
	
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
//	  2.2	| DD/MM/YYYY | Javier Tirado	| Creacion de ImpuestoEquivalenciaFac