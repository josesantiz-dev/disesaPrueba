package net.giro.cargas.documentos.logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.cargas.documentos.beans.RecursosDocumentos;
import net.giro.cargas.documentos.beans.RecursosDocumentosModulos;
import net.giro.cargas.documentos.dao.RecursosDocumentosDAO;
import net.giro.cargas.documentos.util.TipoRecursosDocumentos;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

@Stateless
public class RecursosDocumentosFac implements RecursosDocumentosRem {
	private static Logger log = Logger.getLogger(RecursosDocumentosFac.class);
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
	private RecursosDocumentosDAO ifzRecursos;
	private RecursosDocumentosModulosRem ifzRecursosModulos;
	private FtpRem ifzFtp;
	
	public RecursosDocumentosFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(environment);
			this.ifzRecursos = (RecursosDocumentosDAO) this.ctx.lookup("ejb:/Model_Cargas_Documentos//RecursosDocumentosImp!net.giro.cargas.documentos.dao.RecursosDocumentosDAO");
			this.ifzRecursosModulos = (RecursosDocumentosModulosRem) this.ctx.lookup("ejb:/Logica_Cargas_Documentos//RecursosDocumentosModulosFac!net.giro.cargas.documentos.logica.RecursosDocumentosModulosRem");
    		this.ifzFtp = (FtpRem) this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
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
	public Long save(RecursosDocumentos entity) throws Exception {
		try {
			entity.setFechaCreacion(Calendar.getInstance().getTime());
			entity.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			return this.ifzRecursos.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".save(RecursosDocumentos)", e);
			throw e;
		}
	}

	@Override
	public List<RecursosDocumentos> saveOrUpdateList(List<RecursosDocumentos> entities) throws Exception {
		try {
			return this.ifzRecursos.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".saveOrUpdateList(List<RecursosDocumentos>)", e);
			throw e;
		}
	}

	@Override
	public RecursosDocumentos update(RecursosDocumentos entity) throws Exception {
		try {
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.ifzRecursos.update(entity);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".update(RecursosDocumentos)", e);
			throw e;
		}
		
		return entity;
	}

	@Override
	public void delete(long idRecursoDocumento) throws Exception {
		try {
			this.ifzRecursos.delete(idRecursoDocumento);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".delete(idRecursoDocumento)", e);
			throw e;
		}
	}

	@Override
	public RecursosDocumentos findById(long idRecursoDocumento) throws Exception {
		try {
			return this.ifzRecursos.findById(idRecursoDocumento);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".save(RecursosDocumentos)", e);
			throw e;
		}
	}

	@Override
	public List<RecursosDocumentos> findLike(String value, String orderBy, int limite) throws Exception {
		try {
			value = value.replace(" ", "%");
			return this.ifzRecursos.findLike(value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".findLike(value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<RecursosDocumentos> findLikeProperty(String property, Object value, String orderBy, int limite) throws Exception {
		try {
			if (property == null || "".equals(property.trim()) || "*".equals(property.trim()))
				return this.findLike(value.toString(), orderBy, limite);
			if (value.getClass() == java.lang.String.class)
				value = value.toString().replace(" ", "%");
			return this.ifzRecursos.findLikeProperty(property, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".findLikeProperty(property, value, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<RecursosDocumentos> findByProperty(String property, Object value, String orderBy, int limite) throws Exception {
		try {
			return this.ifzRecursos.findByProperty(property, value, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".findByProperty(property, value, orderBy, limite)", e);
			throw e;
		}
	}

	public List<RecursosDocumentos> findByTipoAplicacion(TipoRecursosDocumentos tipo, long idModulo) throws Exception {
		List<RecursosDocumentosModulos> detalles = null;
		List<Long> lista = null;
		int tipoRecurso = -1;
		
		try {
			detalles = this.ifzRecursosModulos.findByProperty("idModulo", idModulo, "", 0);
			if (detalles == null || detalles.isEmpty())
				return null;
			
			lista = new ArrayList<Long>();
			for (RecursosDocumentosModulos detalle : detalles)
				lista.add(detalle.getIdRecursoDocumento());
			tipoRecurso = (tipo != null) ? tipo.ordinal() : 0;
			return this.ifzRecursos.findAllById(lista, tipoRecurso, getIdEmpresa(), "");
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".findByTipoAplicacion(tipo, idModulo)", e);
			throw e;
		}
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// RecursosDocumentosModulos
	// -----------------------------------------------------------------------------------------------------------------------------

	@Override
	public List<RecursosDocumentosModulos> findAllModulos(long idRecursoDocumento) throws Exception {
		try {
			this.ifzRecursosModulos.setInfoSesion(this.infoSesion);
			return this.ifzRecursosModulos.findAll(idRecursoDocumento);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".findAllModulos(idRecursoDocumento)", e);
			throw e;
		}
	}

	@Override
	public RecursosDocumentosModulos saveOrUpdateModulo(long idRecursoDocumento, RecursosDocumentosModulos recursoDocumentoModulo) throws Exception {
		try {
			if (idRecursoDocumento <= 0L)
				return null;
			if (recursoDocumentoModulo == null)
				return null;
			
			recursoDocumentoModulo.setIdRecursoDocumento(idRecursoDocumento);
			recursoDocumentoModulo.setFechaModificacion(Calendar.getInstance().getTime());
			recursoDocumentoModulo.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			if (recursoDocumentoModulo.getId() == null || recursoDocumentoModulo.getId() <= 0L) {
				recursoDocumentoModulo.setFechaCreacion(Calendar.getInstance().getTime());
				recursoDocumentoModulo.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			}

			this.ifzRecursosModulos.setInfoSesion(this.infoSesion);
			return this.ifzRecursosModulos.saveOrUpdate(recursoDocumentoModulo);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".saveModulos(idRecursoDocumento, listRecursosDocumentosModulos)", e);
			throw e;
		}
	}
	
	@Override
	public List<RecursosDocumentosModulos> saveModulos(long idRecursoDocumento, List<RecursosDocumentosModulos> recursosDocumentosModulos) throws Exception {
		try {
			if (idRecursoDocumento <= 0L)
				return null;
			if (recursosDocumentosModulos == null || recursosDocumentosModulos.isEmpty())
				return null;
			
			for (RecursosDocumentosModulos item : recursosDocumentosModulos) {
				item.setIdRecursoDocumento(idRecursoDocumento);
				item.setFechaModificacion(Calendar.getInstance().getTime());
				item.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
				if (item.getId() == null || item.getId() <= 0L) {
					item.setFechaCreacion(Calendar.getInstance().getTime());
					item.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
				}
			}

			this.ifzRecursosModulos.setInfoSesion(this.infoSesion);
			return this.ifzRecursosModulos.saveOrUpdateList(recursosDocumentosModulos);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".saveModulos(idRecursoDocumento, listRecursosDocumentosModulos)", e);
			throw e;
		}
	}

	@Override
	public void deleteAllModulos(List<RecursosDocumentosModulos> recursosDocumentosModulos) throws Exception {
		try {
			if (recursosDocumentosModulos == null || recursosDocumentosModulos.isEmpty())
				return;

			this.ifzRecursosModulos.setInfoSesion(this.infoSesion);
			for (RecursosDocumentosModulos item : recursosDocumentosModulos) 
				this.ifzRecursosModulos.delete(item.getId());
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".deleteAllModulos(recursosDocumentosModulos)", e);
			throw e;
		}
	}

	@Override
	public void deleteModulo(long idRecursoDocumentoModulo) throws Exception {
		try {
			this.ifzRecursosModulos.setInfoSesion(this.infoSesion);
			this.ifzRecursosModulos.delete(idRecursoDocumentoModulo);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".deleteModulo(idRecursoDocumentoModulo)", e);
			throw e;
		}
	}
	
	// -----------------------------------------------------------------------------------------------------------------------------
	// Importar/Exportar Recurso
	// -----------------------------------------------------------------------------------------------------------------------------
	
	@Override
	public Respuesta importar(RecursosDocumentos entity, byte[] fileSource, String path) throws Exception {
		Respuesta respuesta = null;
		String extension = "";
		String filename = "";
		String prefix = "";
		
		try {
			if (entity == null)
				return null;
			
			respuesta = new Respuesta();
			if (entity.getId() == null || entity.getId() <= 0L)
				entity.setId(this.save(entity));
			
			prefix = TipoRecursosDocumentos.fromOrdinal(entity.getTipo()).toString();
			filename = prefix + "-" + entity.getId().toString();
			extension = "." + entity.getExtension();
			if (path == null || "".equals(path.trim()))
				path = "/var/cargas/";
			
			if (! this.ifzFtp.put(fileSource, path + filename + extension)) {
				log.error("Ocurrio un problema al intentar guardar el Documento en el servidor.\nFTP - Carga FAIL");
				respuesta.getErrores().setDescError("Ocurrio un problema al intentar guardar el Documento en el servidor");
				respuesta.getErrores().setCodigoError(2L);
				return respuesta;
			}
			
			entity.setFilename(filename);
			entity = this.update(entity);
			
			respuesta.getBody().addValor("entity", entity);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".importar(RecursosDocumentos, fileSource, path)", e);
			respuesta.getBody().addValor("exception", e);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar guardar el Documento en el servidor");
			respuesta.getErrores().setCodigoError(1L);
		}
		
		return respuesta;
	}

	@Override
	public byte[] exportar(long idRecursoDocumento, String path) throws Exception {
		RecursosDocumentos recurso = null;
		
		try {
			recurso = this.findById(idRecursoDocumento);
			if (recurso == null)
				return null;
			return this.exportar(recurso, path);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".exportar(idRecursoDocumento, path)", e);
			throw e;
		}
	}

	@Override
	public byte[] exportar(RecursosDocumentos recurso, String path) throws Exception {
		String prefix = "";
		String filename = "";
		String extension = "";
		
		try {
			if (recurso == null || recurso.getId() == null || recurso.getId() <= 0L)
				return null;
			
			prefix = TipoRecursosDocumentos.fromOrdinal(recurso.getTipo()).toString();
			filename = (recurso.getFilename() == null || "".equals(recurso.getFilename().trim()) ? prefix + "-" + recurso.getId().toString() : recurso.getFilename());
			extension = "." + recurso.getExtension();
			if (path == null || "".equals(path.trim()))
				path = "/var/cargas/";
			
			return this.ifzFtp.get(path + filename + extension);
		} catch (Exception e) {
			log.error("Error en " + this.getClass().getCanonicalName() + ".exportar(RecursoDocumento, path)", e);
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
