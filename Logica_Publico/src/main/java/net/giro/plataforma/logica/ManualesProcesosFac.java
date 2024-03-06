package net.giro.plataforma.logica;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.beans.ManualesProcesos;
import net.giro.plataforma.beans.ManualesProcesosAplicaciones;
import net.giro.plataforma.dao.ManualesProcesosAplicacionesDAO;
import net.giro.plataforma.dao.ManualesProcesosDAO;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.plataforma.seguridad.dao.AplicacionDAO;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

@Stateless
public class ManualesProcesosFac implements ManualesProcesosRem {
	private static Logger log = Logger.getLogger(ManualesProcesosFac.class);
	private InitialContext ctx;
	private InfoSesion infoSesion;
	private ManualesProcesosDAO ifzManuales;
	private ManualesProcesosAplicacionesDAO ifzManualesApps;
	private AplicacionDAO ifzApps;
	private FtpRem ifzFtp;

	public ManualesProcesosFac() {
		Hashtable<String, Object> environment = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			this.ctx = new InitialContext(environment);
			this.ifzManuales = (ManualesProcesosDAO) this.ctx.lookup("ejb:/Model_Publico//ManualesProcesosImp!net.giro.plataforma.dao.ManualesProcesosDAO");
			this.ifzManualesApps = (ManualesProcesosAplicacionesDAO) this.ctx.lookup("ejb:/Model_Publico//ManualesProcesosAplicacionesImp!net.giro.plataforma.dao.ManualesProcesosAplicacionesDAO");
			this.ifzApps = (AplicacionDAO) this.ctx.lookup("ejb:/Model_Publico//AplicacionImp!net.giro.plataforma.seguridad.dao.AplicacionDAO");
			this.ifzFtp = (FtpRem) this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Publico.ManualesProcesosFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion; 
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public long save(ManualesProcesos entity) throws Exception {
		try {
			return this.ifzManuales.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.save(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public ManualesProcesos saveOrUpdate(ManualesProcesos entity) {
		boolean guardarArchivo = false;
		Long idManualProceso = 0L;
		String fileName = "";
		
		try {
			if (entity == null)
				return entity;
			
			if (entity.getId() != null && entity.getId() > 0L) {
				entity.setFechaModificacion(Calendar.getInstance().getTime());
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
				this.update(entity);
				return entity;
			}

			entity.setFechaCreacion(Calendar.getInstance().getTime());
			entity.setCreadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			guardarArchivo = (entity.getFileSrc() != null ? true : false);
			idManualProceso = this.ifzManuales.save(entity, getCodigoEmpresa());
			fileName = entity.getStorageFileName().replace(":ID", idManualProceso.toString());
			entity.setId(idManualProceso);
			entity.setStorageFileName(fileName);
			this.update(entity);
			
			// Generamos nombre de archivo si corresponde
			if (guardarArchivo && ! "".equals(fileName)) {
				if (! this.ifzFtp.put(entity.getFileSrc(), fileName))
					log.error("Ocurrio un problema al intentar guardar la archivo en el servidor");
			}
			
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.save(entity)", e);
			return null;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ManualesProcesos> saveOrUpdateList(List<ManualesProcesos> entities) throws Exception {
		try {
			return this.ifzManuales.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.TopicEstatusFac.saveOrUpdateList(entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(ManualesProcesos entity) throws Exception {
		try {
			this.ifzManuales.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.update(entity)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta cancelar(ManualesProcesos entity) {
		Respuesta respuesta = new Respuesta();
		
		try {
			entity.setEstatus(1);
			entity.setModificadoPor(1L);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			if (this.infoSesion != null)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			this.update(entity);
			respuesta.getBody().addValor("manualProceso", entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.cancelar(entity)", e);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar eliminar el Manual/Proceso indicado");
		}
		
		return respuesta;
	}

	@Override
	public void delete(ManualesProcesos entity) throws Exception {
		try {
			if (entity.getStorageFileName() != null && ! "".equals(entity.getStorageFileName()))
				this.ifzFtp.delArchivo(entity.getStorageFileName());
			this.ifzManuales.delete(entity.getId());
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.cancelar(entity)", e);
			throw e;
		}
	}
	
	@Override
	public ManualesProcesos findById(long idManualProceso) throws Exception {
		try {
			return this.ifzManuales.findById(idManualProceso);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.findById(idManualProceso)", e);
			throw e;
		}
	}
	
	@Override
	public List<ManualesProcesos> findAll(String orderBy, int limite) throws Exception {
		try {
			return this.ifzManuales.findAll(orderBy, getIdEmpresa(), limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.findAll(orderBy, limite)", e);
			throw e;
		}
	}
	
	@Override
	public List<ManualesProcesos> findLike(String value, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		try {
			return this.ifzManuales.findLike(value, incluyeCancelados, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.findLike(value, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}
	}
	
	@Override
	public List<ManualesProcesos> findLikeProperty(String propertyName, Object value, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		try {
			return this.ifzManuales.findLikeProperty(propertyName, value, incluyeCancelados, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.findLikeProperty(propertyName, value, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}
	}
	
	@Override
	public List<ManualesProcesos> findByProperty(String propertyName, Object value, boolean incluyeCancelados, String orderBy, int limite) throws Exception {
		try {
			return this.ifzManuales.findByProperty(propertyName, value, incluyeCancelados, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.findByProperty(propertyName, value, incluyeCancelados, orderBy, limite)", e);
			throw e;
		}
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Respuesta save(ManualesProcesos entity, List<ManualesProcesosAplicaciones> aplicaciones) {
		Respuesta respuesta = new Respuesta();
		
		try {
			entity = this.saveOrUpdate(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.save(entity, aplicaciones)", e);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar guardar el Manual/Proceso");
			return respuesta;
		}

		try {
			for (ManualesProcesosAplicaciones app : aplicaciones)
				app.setIdManualesProcesos(entity);
			aplicaciones = this.ifzManualesApps.saveOrUpdateList(aplicaciones, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.save(entity, aplicaciones)", e);
			respuesta.getErrores().setCodigoError(2L);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar guardar las Aplicaciones del Manual/Proceso especificado");
			return respuesta;
		}

		respuesta.getBody().addValor("manualProceso", entity);
		respuesta.getBody().addValor("aplicaciones", aplicaciones);
		
		return respuesta;
	}
	
	@Override
	public List<ManualesProcesosAplicaciones> findByManualProceso(long idManualProceso, boolean incluyeCancelados, String orderBy) throws Exception {
		try {
			return this.ifzManualesApps.findByManualProceso(idManualProceso, incluyeCancelados, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.findByManualProceso(idManualProceso, incluyeCancelados, orderBy)", e);
			throw e;
		}
	}
	
	@Override
	public List<ManualesProcesosAplicaciones> findByAplicacion(long idAplicacion, boolean incluyeCancelados, String orderBy) throws Exception {
		try {
			return this.ifzManualesApps.findByAplicacion(idAplicacion, incluyeCancelados, orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.findByAplicacion(idAplicacion, incluyeCancelados, orderBy)", e);
			throw e;
		}
	}
	
	@Override
	public void deleteAplicaciones(List<ManualesProcesosAplicaciones> aplicaciones) throws Exception {
		try {
			if (aplicaciones == null || aplicaciones.isEmpty())
				return;
			for (ManualesProcesosAplicaciones item : aplicaciones) {
				if (item.getId() != null && item.getId() > 0L)
					this.ifzManualesApps.delete(item.getId());
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.deleteAplicaciones(aplicaciones)", e);
			throw e;
		}
	}
	
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public List<Aplicacion> findAllAplicaciones(String orderBy) throws Exception {
		try {
			return this.ifzApps.findAll(orderBy);
		} catch (Exception e) {
			log.error("error en Logica_Publico.ManualesProcesosFac.findAllAplicaciones(orderBy)", e);
			throw e;
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------
	// PRIVADOS 
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	private Long getIdEmpresa() {
		Long idEmpresa = 1L;
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getId();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}

	private Long getCodigoEmpresa() {
		Long idEmpresa = 1L;
		if (this.infoSesion != null) {
			idEmpresa = this.infoSesion.getEmpresa().getCodigo();
			idEmpresa = (idEmpresa != null && idEmpresa > 0L ? idEmpresa : 1L);
		}
		
		return idEmpresa;
	}
}
