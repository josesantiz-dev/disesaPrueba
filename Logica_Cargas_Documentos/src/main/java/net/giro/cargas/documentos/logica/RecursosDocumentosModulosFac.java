package net.giro.cargas.documentos.logica;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.cargas.documentos.beans.RecursosDocumentosModulos;
import net.giro.cargas.documentos.dao.RecursosDocumentosModulosDAO;
import net.giro.plataforma.InfoSesion;

import org.apache.log4j.Logger;

@Stateless
public class RecursosDocumentosModulosFac implements RecursosDocumentosModulosRem {
	private static Logger log = Logger.getLogger(RecursosDocumentosModulosFac.class);
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
	private RecursosDocumentosModulosDAO ifzRecursosDocumentosModulos;
	
	public RecursosDocumentosModulosFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(environment);
			this.ifzRecursosDocumentosModulos = (RecursosDocumentosModulosDAO) this.ctx.lookup("ejb:/Model_Cargas_Documentos//RecursosDocumentosModulosImp!net.giro.cargas.documentos.dao.RecursosDocumentosModulosDAO");
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear " + this.getClass().getCanonicalName(), e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Long save(RecursosDocumentosModulos entity) throws Exception {
		try {
			entity.setFechaCreacion(Calendar.getInstance().getTime());
			entity.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			return this.ifzRecursosDocumentosModulos.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".save(RecursosDocumentosModulos)", e);
			throw e;
		}
	}

	public RecursosDocumentosModulos saveOrUpdate(RecursosDocumentosModulos entity) throws Exception {
		try {
			if (entity == null)
				return null;
			
			if (entity.getId() == null || entity.getId() <= 0L)
				entity.setId(this.save(entity));
			else
				entity = this.update(entity);
			return entity;
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".save(RecursosDocumentosModulos)", e);
			throw e;
		}
	}
	
	@Override
	public List<RecursosDocumentosModulos> saveOrUpdateList(List<RecursosDocumentosModulos> entities) throws Exception {
		try {
			return this.ifzRecursosDocumentosModulos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".saveOrUpdateList(List<RecursosDocumentosModulos>)", e);
			throw e;
		}
	}

	@Override
	public RecursosDocumentosModulos update(RecursosDocumentosModulos entity) throws Exception {
		try {
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.ifzRecursosDocumentosModulos.update(entity);
			return entity;
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".update(RecursosDocumentosModulos)", e);
			throw e;
		}
	}

	@Override
	public void delete(long idRecursoDocumento) throws Exception {
		try {
			this.ifzRecursosDocumentosModulos.delete(idRecursoDocumento);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".delete(idRecursoDocumento)", e);
			throw e;
		}
	}

	@Override
	public RecursosDocumentosModulos findById(long idRecursoDocumentoModulo) throws Exception {
		try {
			return this.ifzRecursosDocumentosModulos.findById(idRecursoDocumentoModulo);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".findById(idRecursoDocumentoModulo)", e);
			throw e;
		}
	}

	@Override
	public List<RecursosDocumentosModulos> findAll(long idRecursoDocumento) throws Exception {
		try {
			return this.ifzRecursosDocumentosModulos.findAll(idRecursoDocumento);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".findAll(idRecursoDocumento)", e);
			throw e;
		}
	}

	@Override
	public List<RecursosDocumentosModulos> findAllByModulo(long idModulo) throws Exception {
		try {
			if (idModulo <= 0L)
				return null;
			return this.ifzRecursosDocumentosModulos.findByProperty("idModulo", idModulo, getIdEmpresa(), "", 0);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".findAllByModulo(idModulo)", e);
			throw e;
		}
	}

	@Override
	public List<RecursosDocumentosModulos> findLikeProperty(String property, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzRecursosDocumentosModulos.findLikeProperty(property, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".findLikeProperty(property, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<RecursosDocumentosModulos> findByProperty(String property, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzRecursosDocumentosModulos.findByProperty(property, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".findByProperty(property, value, orderBy, limite)", e);
			throw e;
		}
	}

	// --------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L;
	}
	
	private Long getCodigoEmpresa() {
		return (this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L;
	}
}
