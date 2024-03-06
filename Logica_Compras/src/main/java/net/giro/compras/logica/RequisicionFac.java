package net.giro.compras.logica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionExt;
import net.giro.compras.beans.SolicitudBodega;
import net.giro.compras.beans.SolicitudBodegaProducto;
import net.giro.compras.comun.EstatusCompras;
import net.giro.compras.dao.RequisicionDAO;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

@Stateless
public class RequisicionFac implements RequisicionRem {
	private static Logger log = Logger.getLogger(RequisicionFac.class);
	private InfoSesion infoSesion;
	private RequisicionDAO ifzRequisiciones;
	private CotizacionRem ifzCotizaciones;
	private TraspasoDetalleRem ifzTraspasosDetalles;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	
	public RequisicionFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzRequisiciones = (RequisicionDAO) ctx.lookup("ejb:/Model_Compras//RequisicionImp!net.giro.compras.dao.RequisicionDAO");
			this.ifzCotizaciones = (CotizacionRem) ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
			this.ifzTraspasosDetalles = (TraspasoDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("RequisicionFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.RequisicionFac", e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(Requisicion entity) throws Exception {
		try {
			return this.ifzRequisiciones.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.save(Requisicion)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(Requisicion entity) throws Exception {
		try {
			this.ifzRequisiciones.update(entity);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.update(Requisicion)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<Requisicion> saveOrUpdateList(List<Requisicion> entities) throws Exception {
		try {
			return this.ifzRequisiciones.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.saveOrUpdateList(List<Requisicion> entities)", e);
			throw e;
		}
	}

	@Override
	public Respuesta cancelar(long idRequisicion, long idUsuario) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<Cotizacion> cotizaciones = null;
		Requisicion requisicion = null;
		String cotizacion = "";
		
		try {
			this.ifzCotizaciones.setInfoSesion(this.infoSesion);
			cotizaciones = this.ifzCotizaciones.findByRequisicion(idRequisicion);
			if (cotizaciones != null && ! cotizaciones.isEmpty()) {
				cotizacion = "multiples Cotizaciones";
				if (cotizaciones.size() == 1)
					cotizacion = "la Cotizacion " + cotizaciones.get(0).getFolio();
				log.error("No se puede Cancelar la Requisicion porque esta total o parcialmente en " + cotizacion);
				respuesta.getBody().addValor("cotizaciones", cotizaciones);
				respuesta.getErrores().addCodigo("COMPRAS", 2L);
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se puede Cancelar la Requisicion porque esta total o parcialmente en " + cotizacion);
				return respuesta;
			}
			
			// Cancela la Cotizacion
			requisicion = this.findById(idRequisicion);
			requisicion.setEstatus(EstatusCompras.CANCELADA.ordinal());
			requisicion.setModificadoPor(getIdUsuario(idUsuario));
			requisicion.setFechaModificacion(Calendar.getInstance().getTime());
			this.update(requisicion);
			respuesta.getBody().addValor("requisicion", requisicion);
		} catch (Exception e) {
			log.error("error en Compras.RequisicionFac.cancelar(long idRequisicion, long idUsuario)", e);
			respuesta.getBody().addValor("exception", e);
			respuesta.getErrores().addCodigo("COMPRAS", 1L);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar cancelar la Requisicion indicada");
		}
		
		return respuesta;
	}

	@Override
	public void delete(long idRequisicion) throws Exception {
		try {
			this.ifzRequisiciones.delete(idRequisicion);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.delete(idRequisicion)", e);
			throw e;
		}
	}

	@Override
	public Requisicion findById(long idRequisicion) {
		try {
			return this.ifzRequisiciones.findById(idRequisicion);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findById(idRequisicion)", e);
			throw e;
		}
	}

	@Override
	public List<Requisicion> findAll(long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idPropietario, String orderBy, int limite) throws Exception {
		try {
			return this.ifzRequisiciones.findAll(idObra, tipo, incluyeEliminadas, incluyeSistema, getIdEmpresa(), idPropietario, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findAll(idObra, tipo, incluyeEliminadas, incluyeSistema, idPropietario, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Requisicion> findLike(String value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idPropietario, String orderBy, int limite) throws Exception {
		try {
			value = (value.trim().contains(" ") ? value.trim().replace(" ", "%") : value.trim());
			return this.ifzRequisiciones.findLike(value, idObra, tipo, incluyeEliminadas, incluyeSistema, getIdEmpresa(), idPropietario, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findLike(value, idObra, tipo, incluyeEliminadas, incluyeSistema, idPropietario, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Requisicion> findLikeProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idPropietario, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), idObra, tipo, incluyeEliminadas, incluyeSistema, idPropietario, orderBy, limite);
			if (value.getClass() == java.lang.String.class)
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			return this.ifzRequisiciones.findLikeProperty(propertyName, value, idObra, tipo, incluyeEliminadas, incluyeSistema, getIdEmpresa(), idPropietario, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findLikeProperty(propertyName, value, idObra, tipo, incluyeEliminadas, incluyeSistema, idPropietario, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Requisicion> findByProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idPropietario, String orderBy, int limite) throws Exception {
		try {
			return this.ifzRequisiciones.findByProperty(propertyName, value, idObra, tipo, incluyeEliminadas, incluyeSistema, getIdEmpresa(), idPropietario, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findByProperty(propertyName, value, idObra, tipo, incluyeEliminadas, incluyeSistema, idPropietario, orderBy, limite)", e);
			throw e;
		}
	}

	@Override
	public List<Requisicion> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, 0, false, false, 0L, "", limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		}
	}

	@Override
	public List<Requisicion> findByProperties(HashMap<String, Object> params, long idObra, int tipo, boolean incluyeEliminadas, boolean incluyeSistema, long idPropietario, String orderBy, int limite) throws Exception {
		try {
			return this.ifzRequisiciones.findByProperties(params, 0L, 0, false, false, getIdEmpresa(), 0L, orderBy, limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findByProperties(params, idObra, tipo, incluyeEliminadas, incluyeSistema, idPropietario, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Requisicion> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			return this.findByProperties(params, 0L, 0, false, false, 0L, "", limite);
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findByProperties(params, limite)", e);
			throw e;
		} 
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public List<SolicitudBodega> solicitudBodega(long idRequisicion) throws Exception {
		Requisicion requisicion = null;
		List<TraspasoDetalleExt> detalles = null;
		// --------------------------------------------------
		List<SolicitudBodega> solicitudes = null;
		SolicitudBodegaProducto solicitudProducto = null;
		
		try {
			requisicion = this.findById(idRequisicion);
			if (requisicion == null || requisicion.getId() == null || requisicion.getId() <= 0L)
				return solicitudes;
			
			solicitudes = recuperarSolicitudes(requisicion.getIdObra(), idRequisicion);
			if (solicitudes == null || solicitudes.isEmpty())
				return solicitudes;
			
			for (SolicitudBodega solicitud : solicitudes) {
				detalles = this.ifzTraspasosDetalles.findExtAll(solicitud.getIdSolicitud());
				if (detalles == null || detalles.isEmpty())
					continue;
				
				for (TraspasoDetalleExt detalle : detalles) {
					solicitudProducto = new SolicitudBodegaProducto();
					solicitudProducto.setIdAlmacen(solicitud.getIdAlmacen());
					solicitudProducto.setPojoProducto(detalle.getIdProducto());
					solicitudProducto.setIdProducto(detalle.getIdProducto().getId());
					solicitudProducto.setClave(detalle.getIdProducto().getClave());
					solicitudProducto.setDescripcion(detalle.getIdProducto().getDescripcion());
					if (detalle.getIdProducto().getFamilia() != null) {
						solicitudProducto.setIdFamilia(detalle.getIdProducto().getFamilia().getId());
						solicitudProducto.setFamilia(detalle.getIdProducto().getFamilia().getDescripcion());
					}
					if (detalle.getIdProducto().getUnidadMedida() != null) {
						solicitudProducto.setIdUnidadMedida(detalle.getIdProducto().getUnidadMedida().getId());
						solicitudProducto.setUnidadMedida(detalle.getIdProducto().getUnidadMedida().getDescripcion());
					}
					solicitudProducto.setDisponible(detalle.getCantidad());
					solicitudProducto.setRequeridos(detalle.getCantidad());
					solicitudProducto.setSolicitud(detalle.getCantidad());
					solicitud.getListProductos().add(solicitudProducto);
				}
			}
			
			// Ordenamos por productos de las solicitudes
			for (SolicitudBodega solicitud : solicitudes)
				solicitud.ordenarProductos();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar la(s) Solicitud(es) a Bodega de la Requisicion", e);
			throw e;
		}
		
		return solicitudes;
	}

	//------------------------------------------------------------------------------------------------------
	// EXTENDIDOS
	//------------------------------------------------------------------------------------------------------

	@Override
	public Long save(RequisicionExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.RequisicionExtToRequisicion(extendido));
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.save(extendido)", e);
			throw e;
		}
	}

	@Override
	public void update(RequisicionExt extendido) throws Exception {
		try {
			this.update(this.convertidor.RequisicionExtToRequisicion(extendido));
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.update(extendido)", e);
			throw e;
		}
	}

	@Override
	public RequisicionExt findExtById(Long idRequisicion) throws Exception {
		try {
			return this.convertidor.RequisicionToRequisicionExt(this.findById(idRequisicion));
		} catch (Exception e) {
			log.error("Error en Logica_Compras.RequisicionFac.findExtById(idRequisicion)", e);
			throw e;
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	// PRIVADOS
	//------------------------------------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	private List<SolicitudBodega> recuperarSolicitudes(Long idObra, Long idRequisicion) {
		List<Object> rows = null;
		Object[] item = null;
		String queryString = "";
		// --------------------------------------------
		List<SolicitudBodega> solicitudes = null;
		SolicitudBodega solicitud = null;
		
		try {
			if ((idObra == null || idObra <= 0L) || (idRequisicion == null || idRequisicion <= 0L))
				return null;
			queryString += "select a.id, a.id_almacen_origen, b.nombre, b.identificador, case a.estatus > 1 when true then a.estatus + a.completo else a.estatus end as estatus ";
			queryString += "from almacen_traspaso a inner join almacen b on b.id = a.id_almacen_origen ";
			queryString += "where a.tipo = 3 and a.estatus in (0,2) and a.id_obra = :idObra and a.solicitud_requisicion = :idRequisicion and a.solicitud_cotizacion = 0 order by a.id";
			queryString = queryString.replace(":idObra", idObra.toString());
			queryString = queryString.replace(":idRequisicion", idRequisicion.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty())
				return solicitudes;
			
			solicitudes = new ArrayList<SolicitudBodega>();
			for (Object row : rows) {
				item = (Object[]) row;
				solicitud = new SolicitudBodega();
				solicitud.setIdSolicitud(((BigDecimal) item[0]).longValue());
				solicitud.setIdAlmacen(((BigDecimal) item[1]).longValue());
				solicitud.setAlmacen(item[2].toString());
				solicitud.setIdentificador(item[3].toString());
				solicitud.setEstatus(((Short) item[4]).intValue());
				solicitudes.add(solicitud);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar la Bodega de la Obra " + idObra, e);
		}
		
		return solicitudes;
	}
	
	private Long getIdEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getId() : 1L);
	}

	private Long getCodigoEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
	
	private Long getIdUsuario(long defaultValue) {
		long resultado = 0;
		resultado = (this.infoSesion != null ? this.infoSesion.getAcceso().getUsuario().getId() : defaultValue);
		resultado = (resultado == 1 && defaultValue > 0 && resultado != defaultValue ? defaultValue : resultado);
		return resultado;
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-19 | Javier Tirado 	| Añado los metodos estatus, findByProperties y findLikeProperties. Normal y extendido
 */