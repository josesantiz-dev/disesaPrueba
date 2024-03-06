package net.giro.contabilidad.logica;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.Grupos;
import net.giro.contabilidad.beans.GruposCuentas;
import net.giro.contabilidad.dao.GruposCuentasDAO;
import net.giro.plataforma.InfoSesion;

@Stateless
public class GruposCuentasFac implements GruposCuentasRem {
	private static Logger log = Logger.getLogger(GruposCuentasFac.class);
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private GruposCuentasDAO ifzGruposCuentas;
	private static String orderBy;
	
	public GruposCuentasFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(p);
			this.ifzGruposCuentas = (GruposCuentasDAO) this.ctx.lookup("ejb:/Model_Contabilidad//GruposCuentasImp!net.giro.contabilidad.dao.GruposCuentasDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Contabilidad.GruposCuentasFac", e);
			ctx = null;
		}
	}


	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void orderBy(String orderBy) {
		GruposCuentasFac.orderBy = orderBy;
	}

	@Override
	public Long save(GruposCuentas entity) throws Exception {
		try {
			return this.ifzGruposCuentas.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.save(GruposCuentas)", e);
			throw e;
		}
	}

	@Override
	public List<GruposCuentas> saveOrUpdateList(List<GruposCuentas> entities) throws Exception {
		try {
			return this.ifzGruposCuentas.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.saveOrUpdateList(List<GruposCuentas> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(GruposCuentas entity) throws Exception {
		try {
			this.ifzGruposCuentas.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.update(GruposCuentas)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entity) throws Exception {
		try {
			this.ifzGruposCuentas.delete(entity);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public GruposCuentas findById(Long id) {
		try {
			return this.ifzGruposCuentas.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public List<GruposCuentas> findAll(long idGrupo) throws Exception {
		try {
			return this.findAll(idGrupo, "");
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findAll(idGrupo, orderBy)", e);
			throw e;
		} 
	}

	@Override
	public List<GruposCuentas> findAll(long idGrupo, String orderBy) throws Exception {
		try {
			return this.ifzGruposCuentas.findAll(idGrupo, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findAll(idGrupo, orderBy)", e);
			throw e;
		} 
	}

	@Override
	public List<GruposCuentas> findAll(Grupos idGrupo, String orderBy) throws Exception {
		try {
			if (idGrupo == null)
				return null;
			return this.findAll(idGrupo.getId(), orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findAll(idGrupo, orderBy)", e);
			throw e;
		} 
	}

	@Override
	public List<GruposCuentas> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzGruposCuentas.orderBy(orderBy);			
			return this.ifzGruposCuentas.findByProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<GruposCuentas> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzGruposCuentas.orderBy(orderBy);
			return this.ifzGruposCuentas.findLikeProperty(propertyName, value, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<GruposCuentas> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzGruposCuentas.orderBy(orderBy);
			return this.ifzGruposCuentas.findInProperty(columnName, values, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<GruposCuentas> findByProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzGruposCuentas.orderBy(orderBy);
			return this.ifzGruposCuentas.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<GruposCuentas> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzGruposCuentas.orderBy(orderBy);
			return this.ifzGruposCuentas.findByProperties(params, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Contabilidad.GruposCuentasFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// -------------------------------------------------------------------------------------------------------------------

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