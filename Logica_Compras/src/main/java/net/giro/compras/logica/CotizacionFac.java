package net.giro.compras.logica;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import net.giro.clientes.beans.ContactoNegocioExt;
import net.giro.clientes.beans.ContactoPersonaExt;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.logica.ClientesRem;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.CotizacionExt;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.SolicitudBodega;
import net.giro.compras.beans.SolicitudBodegaProducto;
import net.giro.compras.comun.EstatusCompras;
import net.giro.compras.dao.CotizacionDAO;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosCompras;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Stateless
public class CotizacionFac implements CotizacionRem {
	private static Logger log = Logger.getLogger(CotizacionFac.class);
	private InfoSesion infoSesion;
	private CotizacionDAO ifzCotizaciones;
	private CotizacionDetalleRem ifzCotDetalles;
	private TraspasoDetalleRem ifzTraspasosDetalles;
	private OrdenCompraRem ifzOrdenes;
	private ClientesRem ifzClientes;
	private PersonaRem ifzPersonas;
	private NegociosRem ifzNegocios;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	
	public CotizacionFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzCotizaciones = (CotizacionDAO) ctx.lookup("ejb:/Model_Compras//CotizacionImp!net.giro.compras.dao.CotizacionDAO");
			this.ifzCotDetalles = (CotizacionDetalleRem) ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzOrdenes = (OrdenCompraRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzClientes = (ClientesRem) ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			this.ifzPersonas = (PersonaRem) ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzNegocios = (NegociosRem) ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
			this.ifzTraspasosDetalles = (TraspasoDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			
			this.convertidor = new ConvertExt();
			this.convertidor.setFrom("CotizacionFac");
			this.convertidor.setMostrarSystemOut(false);
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Compras.CotizacionFac", e);
		}
	}
	
	@Override
	public void showSystemOuts(boolean value) {
		this.convertidor.setMostrarSystemOut(value);
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Long save(Cotizacion entity) throws Exception {
		try {
			if (entity.getRfcProveedor() == null)
				entity.setRfcProveedor("");
			if (entity.getFolio() == null || "".equals(entity.getFolio().trim())) 
				entity = this.asignarFolio(entity);
			return this.ifzCotizaciones.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.save(Cotizacion)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public List<Cotizacion> saveOrUpdateList(List<Cotizacion> entities) throws Exception {
		try {
			return this.ifzCotizaciones.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.saveOrUpdateList(List<Cotizacion> entities)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void update(Cotizacion entity) throws Exception {
		try {
			if (entity.getRfcProveedor() == null)
				entity.setRfcProveedor("");
			if (entity.getFolio() == null || "".equals(entity.getFolio().trim())) 
				entity = this.asignarFolio(entity);
			this.ifzCotizaciones.update(entity);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.update(Cotizacion)", e);
			throw e;
		}
	}

	@Override
	public Respuesta cancelar(long idCotizacion, long idUsuario) throws Exception {
		Respuesta respuesta = null;
		List<OrdenCompra> listOrdenes = null;
		Cotizacion pojoCotizacion = null;
		String ordenCompra = "";
		
		try {
			respuesta = new Respuesta();
			this.ifzOrdenes.setInfoSesion(this.infoSesion);
			listOrdenes = this.ifzOrdenes.findByCotizacion(idCotizacion, false, 0);
			if (listOrdenes != null && ! listOrdenes.isEmpty()) {
				ordenCompra = "multiples Ordenes de Compra";
				if (listOrdenes.size() == 1)
					ordenCompra = "la Orden de Compra " + listOrdenes.get(0).getFolio();
				log.error("No se puede Cancelar la Cotizacion porque esta total o parcialmente en " + ordenCompra);
				respuesta.getBody().addValor("ordenes", listOrdenes);
				respuesta.getErrores().addCodigo("COMPRAS", 2L);
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se puede Cancelar la Cotizacion porque esta total o parcialmente en " + ordenCompra);
				return respuesta;
			}
			
			// Cancela la Cotizacion
			pojoCotizacion = this.findById(idCotizacion);
			pojoCotizacion.setEstatus(EstatusCompras.CANCELADA.ordinal());
			pojoCotizacion.setModificadoPor(getIdUsuario(idUsuario));
			pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
			this.update(pojoCotizacion);
			
			// DESHABILITADO: ya no hay conteo de cotizados. :: Recupero los productos de la Orden de Compra para el BackOffice
			/*this.ifzCotDetalles.setInfoSesion(this.infoSesion);
			listDetalles = this.ifzCotDetalles.findAll(idCotizacion);
			
			// Envio mensaje a COMPRAS:Estatus Requisicion, si corresponde
			if (pojoCotizacion.getIdRequisicion() != null && pojoCotizacion.getIdRequisicion() > 0L && (listDetalles != null && ! listDetalles.isEmpty()))
				boRequisicion(pojoCotizacion.getIdRequisicion(), idCotizacion, listDetalles); */
			
			respuesta.getBody().addValor("cotizacion", pojoCotizacion);
		} catch (Exception e) {
			log.error("error en Compras.CotizacionFac.cancelar(long idCotizacion, long idUsuario)", e);
			respuesta.getBody().addValor("exception", e);
			respuesta.getErrores().addCodigo("COMPRAS", 1L);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al intentar cancelar la Cotizacion indicada");
		}
		
		return respuesta;
	}

	@Override
	public void delete(Long idCotizacion) throws Exception {
		try {
			this.ifzCotizaciones.delete(idCotizacion);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.delete(idCotizacion)", e);
			throw e;
		}
	}

	@Override
	public Cotizacion findById(Long idCotizacion) {
		try {
			return this.ifzCotizaciones.findById(idCotizacion);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findById(idCotizacion)", e);
			throw e;
		}
	}

	@Override
	public List<Cotizacion> findAll(long idObra) throws Exception {
		try {
			return this.findAll(idObra, -1, "");
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findAll(idObra)", e);
			throw e;
		} 
	}

	@Override
	public List<Cotizacion> findAll(long idObra, int estatus, String orderBy) throws Exception {
		try {
			return this.ifzCotizaciones.findAll(idObra, estatus, orderBy);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findAll(idObra, estatus, orderBy)", e);
			throw e;
		} 
	}
	
	@Override
	public List<Cotizacion> findByRequisicion(long idRequisicion) throws Exception {
		try {
			return this.findByProperty("idRequisicion", idRequisicion, 0L, 0, false, false, false, "", 0);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Cotizacion> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findByProperty(propertyName, value, 0L, 0, true, false, false, "", limite);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Cotizacion> findByProperty(String propertyName, final Object value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, String orderBy, int limite) throws Exception {
		try {
			return this.ifzCotizaciones.findByProperty(propertyName, value, idObra, tipo, incluyeSuministradas, incluyeCanceladas, incluyeSistema, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findByProperty(propertyName, value, idObra, tipo, incluyeSuministradas, incluyeCanceladas, incluyeSistema, orderBy, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Cotizacion> findLike(String value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, String orderBy, int limite) throws Exception {
		try {
			value = (value.trim().contains(" ") ? value.trim().replace(" ", "%") : value.trim());
			return this.ifzCotizaciones.findLike(value, idObra, tipo, incluyeSuministradas, incluyeCanceladas, incluyeSistema, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLike(value, idObra, tipo, incluyeEliminadas, incluyeSistema, orderBy, idEmpresa, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Cotizacion> findLike(String value, long idObra, int estatus, int limite) throws Exception {
		try {
			return this.findLike(value, idObra, 0, (estatus == 2), (estatus == 1), false, "", limite);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLike(value, idObra, estatus, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Cotizacion> findLikeProperty(String propertyName, Object value, long idObra, int tipo, boolean incluyeSuministradas, boolean incluyeCanceladas, boolean incluyeSistema, String orderBy, int limite) throws Exception {
		try {
			if (propertyName == null || "".equals(propertyName.trim()) || propertyName.trim().contains("*"))
				return this.findLike(value.toString(), idObra, tipo, incluyeSuministradas, incluyeCanceladas, incluyeSistema, orderBy, limite);
			if (value.getClass() == java.lang.String.class)
				value = (value.toString().trim().contains(" ") ? value.toString().trim().replace(" ", "%") : value.toString().trim());
			return this.ifzCotizaciones.findLikeProperty(propertyName, value, idObra, tipo, incluyeSuministradas, incluyeCanceladas, incluyeSistema, getIdEmpresa(), orderBy, limite);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLikeProperty(propertyName, value, idObra, tipo, incluyeEliminadas, incluyeSistema, orderBy, idEmpresa, limite)", e);
			throw e;
		} 
	}

	@Override
	public List<Cotizacion> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			return this.findLikeProperty(propertyName, value, 0L, 0, true, false, false, "", limite);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findLikeProperty(propertyName, value, 0L, 0, false, false, orderBy, getIdEmpresa(), limite)", e);
			throw e;
		} 
	}

	@Override
	public Cotizacion asignarFolio(Cotizacion cotizacion) throws Exception {
		SimpleDateFormat formateador = null;
		String idPro = "";
		String annio = "";
		String folio = "";
		int consecutivo = 0;
		
		try {
			if (cotizacion.getIdProveedor() == null && cotizacion.getIdProveedor() <= 0L)
				return cotizacion;
			formateador = new SimpleDateFormat("yy");
			cotizacion.setFolio(folio);
			cotizacion.setConsecutivoProveedor(consecutivo);
			consecutivo = this.findConsecutivoByProveedor(cotizacion.getIdProveedor());
			// -------------------------------------------------------------------------------
			idPro = String.valueOf(cotizacion.getIdProveedor());
			annio = formateador.format(Calendar.getInstance().getTime());
			idPro = idPro.substring(idPro.length() - 4);
			folio = idPro + "-" + annio + "-" + ((consecutivo < 10) ? "0" : "") + consecutivo;
			// -------------------------------------------------------------------------------
			cotizacion.setFolio(folio);
			cotizacion.setConsecutivoProveedor(consecutivo);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar el Folio para la Cotizacion", e);
			throw e;
		}
		
		return cotizacion;
	}

	@Override
	public int findConsecutivoByProveedor(long idProveedor) throws Exception {
		try {
			return this.ifzCotizaciones.findConsecutivoByProveedor(idProveedor, getIdEmpresa());
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findConsecutivoByProveedor(idProveedor)", e);
			throw e;
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// ACTUALIZACION DE PRECIOS A ORDENES DE COMPRA
	// ------------------------------------------------------------------------------------------------------

	@Override
	public boolean backOfficeCotizacionPreciosOrdenCompra(Long idCotizacion) {
		List<CotizacionDetalle> cotDetalles = null;
		HashMap<Long, Double> productos = null;
		
		try { 
			if (this.infoSesion == null ) {
				log.error("Ocurrio un problema al validar la Sesion de Usuario");
				return false;
			}
			
			if (idCotizacion == null || idCotizacion <= 0L) {
				log.error("No se especificaron parametros suficientes para lanzar el BackOffice CotizacionPreciosOrdenCompra");
				return false;
			}
			
			this.ifzCotDetalles.setInfoSesion(this.infoSesion);
			cotDetalles = this.ifzCotDetalles.findAll(idCotizacion);
			if (cotDetalles == null || cotDetalles.isEmpty()) {
				log.error("Ocurrio un problema al intentar recuperar el detalle de la Orden de Compra indicada");
				return false;
			}
			
			productos = new HashMap<Long, Double>();
			for (CotizacionDetalle item : cotDetalles) 
				productos.put(item.getIdProducto(), item.getPrecioUnitario());
			boCotizacionPreciosOrdenCompra(idCotizacion, productos);
		} catch (Exception e) {
			log.error("Error en OrdenCompraFac.mensajeBOCotizacion(Long idOrdenCompra, Long idCotizacion)", e);
			return false;
		}
		
		return true;
	}

	// ------------------------------------------------------------------------------------------------------
	// SOLICITUD A BODEGA
	// ------------------------------------------------------------------------------------------------------

	@Override
	@SuppressWarnings("unchecked")
	public boolean comprobarSolicitudBodega(Long idObra) throws Exception {
		List<Object> rows = null;
		String queryString = "";

		try {
			if ((idObra == null || idObra <= 0L))
				return false;
			queryString = "select count(id) as sbo from almacen_traspaso where tipo = 3 and solicitud_cotizacion in (select id from cotizacion where id_obra = :idObra and id_requisicion <= 0) ";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows == null || rows.isEmpty())
				return false;
			return (((BigInteger) rows.get(0)).intValue() > 0);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.comprobarSolicitudBodega(idObra)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public List<SolicitudBodega> solicitudBodega(long idCotizacion) throws Exception {
		Cotizacion cotizacion = null;
		// traspasos
		List<TraspasoDetalleExt> detalles = null;
		String campo = "";
		Long idOrigen = 0L;
		// solicitudes
		List<SolicitudBodega> solicitudes = null;
		SolicitudBodegaProducto solicitudProducto = null;
		
		try {
			cotizacion = this.findById(idCotizacion);
			if (cotizacion == null || cotizacion.getId() == null || cotizacion.getId() <= 0L)
				return solicitudes;
			
			campo = "solicitud_cotizacion";
			idOrigen = cotizacion.getId();
			if (cotizacion.getIdRequisicion() != null && cotizacion.getIdRequisicion() > 0L) {
				campo = "solicitud_requisicion";
				idOrigen = cotizacion.getIdRequisicion();
			}
			
			solicitudes = recuperarSolicitudes(cotizacion.getIdObra(), campo, idOrigen);
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
			log.error("Ocurrio un problema al intentar recuperar la(s) Solicitud(es) a Bodega de la Cotizacion", e);
			throw e;
		}
		
		return solicitudes;
	}
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public Respuesta solicitudBodega(Long idObra, Long idCotizacion, Long idRequisicion, List<SolicitudBodega> listSolicitudes) {
		Respuesta respuesta = null;
		LinkedHashMap<Long, HashMap<Long, Double>> items = null;
		HashMap<Long, Double> productos = null;
		boolean hasError = false;
		
		try {
			respuesta = new Respuesta();
			if (this.infoSesion == null) {
				log.error("Ocurrio un problema al validar la Sesion de Usuario");
				respuesta.getBody().addValor("solicitudes", 0);
				respuesta.getErrores().addCodigo("COMPRAS", 1L);
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No se pudo validar la Sesion de Usuario");
				return respuesta;
			}
			
			idObra = (idObra != null && idObra > 0L ? idObra : 0L);
			idCotizacion = (idCotizacion != null && idCotizacion > 0L ? idCotizacion : 0L);
			idRequisicion = (idRequisicion != null && idRequisicion > 0L ? idRequisicion : 0L);
			if (listSolicitudes == null || listSolicitudes.isEmpty()) {
				log.info("SOLICITUD BODEGA :: No se indicaron Solicitudes para registrar");
				listSolicitudes = new ArrayList<SolicitudBodega>();
				respuesta.getBody().addValor("solicitudes", 0);
				respuesta.getErrores().addCodigo("COMPRAS", 1L);
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se indicaron Solicitudes para registrar");
				hasError = true;
			}
			
			// Generamos los items para cada solicitud
			items = new LinkedHashMap<Long, HashMap<Long, Double>>();
			for (SolicitudBodega solicitud : listSolicitudes) {
				if (solicitud.getListProductos().size() <= 0)
					continue;
				productos = new HashMap<Long, Double>();
				for (SolicitudBodegaProducto producto : solicitud.getListProductos())
					productos.put(producto.getIdProducto(), producto.getSolicitud());
				items.put(solicitud.getIdAlmacen(), productos);
			}

			if (! hasError && (items == null || items.isEmpty())) {
				log.info("SOLICITUD BODEGA :: No se indicaron materiales para la Solicitud");
				respuesta.getBody().addValor("solicitudes", 0);
				respuesta.getErrores().addCodigo("COMPRAS", 1L);
				respuesta.getErrores().setCodigoError(3L);
				respuesta.getErrores().setDescError("No se indicaron materiales para la Solicitud");
				hasError = true;
			}
			
			// lanzamos evento
			log.info("COMPRAS - SOLICITUD BODEGA :: Evento lanzado");
			boSolicitudBodega(idObra, idCotizacion, idRequisicion, items);
			if (! hasError) {
				respuesta.getErrores().setCodigoError(0L);
				respuesta.getErrores().setDescError("");
				respuesta.getBody().addValor("solicitudes", items.size());
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS para SBO", e);
			respuesta.getBody().addValor("solicitudes", 0);
			respuesta.getErrores().addCodigo("COMPRAS", 1L);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al generar la Solicitud a Bodega.\n" + listSolicitudes.size() + " Solicitud(es) pendientes");
		}
		
		return respuesta;
	}

	// ------------------------------------------------------------------------------------------------------
	// BACKOFFICE
	// ------------------------------------------------------------------------------------------------------

	private void boCotizacionPreciosOrdenCompra(Long idCotizacion, HashMap<Long, Double> productos) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		Type tipo = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			gson = new Gson();
			tipo = new TypeToken<HashMap<Long, Double>>() {}.getType();
			target = idCotizacion.toString();
			atributos = gson.toJson(productos, tipo);
			
			msgTopic = new MensajeTopic(TopicEventosCompras.COTIZACION_PRECIOS, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS:BO_CO$\n\n" + comando + "\n\n", e);
		}
	}

	private void boSolicitudBodega(Long idObra, Long idCotizacion, Long idRequisicion, LinkedHashMap<Long, HashMap<Long, Double>> productos) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		Type tipo = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String referenciaExtra = "0";
		String atributos = "";
		String comando = "";
		
		try {
			gson = new Gson();
			tipo = new TypeToken<LinkedHashMap<Long, HashMap<Long, Double>>>() {}.getType();
			target = idObra.toString();
			referencia = idCotizacion.toString();
			referenciaExtra = idRequisicion.toString();
			atributos = gson.toJson(productos, tipo);
			
			msgTopic = new MensajeTopic(TopicEventosCompras.COMPRAS_SBO, target, referencia, atributos, this.infoSesion);
			msgTopic.setReferenciaExtra(referenciaExtra);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:" + TopicEventosCompras.COMPRAS_SBO.toString() + "\n\n" + comando + "\n\n", e);
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// CONVERTIDOR
	// ------------------------------------------------------------------------------------------------------

	@Override
	public Cotizacion convertir(CotizacionExt target) throws Exception {
		try {
			return this.convertidor.CotizacionExtToCotizacion(target);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.convertir(CotizacionExt target)", e);
			throw e;
		}
	}

	@Override
	public CotizacionExt convertir(Cotizacion target) throws Exception {
		try {
			return this.convertidor.CotizacionToCotizacionExt(target);
		} catch (Exception e) {
			log.error("Error en CotizacionFac.convertir(Cotizacion target)", e);
			throw e;
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// EXTENDIDOS 
	// ------------------------------------------------------------------------------------------------------

	@Override
	public Long save(CotizacionExt extendido) throws Exception {
		try {
			if (extendido.getRfcProveedor() == null)
				extendido.setRfcProveedor(extendido.getIdProveedor().getRfc());
			return this.save(this.convertidor.CotizacionExtToCotizacion(extendido));
		} catch (Exception e) {
			log.error("Error en CotizacionFac.save(extendido)", e);
			throw e;
		}
	}

	@Override
	public void update(CotizacionExt extendido) throws Exception {
		try {
			if (extendido.getRfcProveedor() == null)
				extendido.setRfcProveedor(extendido.getIdProveedor().getRfc());
			this.update(this.convertidor.CotizacionExtToCotizacion(extendido));
		} catch (Exception e) {
			log.error("Error en CotizacionFac.update(extendido)", e);
			throw e;
		}
	}

	@Override
	public CotizacionExt findExtById(Long idCotizacion) throws Exception {
		try {
			return this.convertidor.CotizacionToCotizacionExt(this.findById(idCotizacion));
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findExtById(idCotizacion)", e);
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public PersonaExt findContactoByProveedor(PersonaExt idProveedor, String tipoPersona) throws Exception {
		List<ContactoPersonaExt> listPersonasContactos = null;
		List<ContactoNegocioExt> listNegociosContactos = null;
		Respuesta respuesta = new Respuesta();
		PersonaExt pojoContacto = null;
		
		try {
			if (tipoPersona == null || "".equals(tipoPersona.trim()))
				return pojoContacto;
			
			if ("P".equals(tipoPersona)) {
				respuesta = this.ifzClientes.buscarPersonasContacto(idProveedor);
				if (respuesta != null) {
					if (respuesta.getErrores().getCodigoError() > 0) 
						return pojoContacto;
					
					listPersonasContactos = (List<ContactoPersonaExt>) respuesta.getBody().getValor("personasContacto");
					if (listPersonasContactos == null || listPersonasContactos.isEmpty()) 
						return pojoContacto;
					
					for (ContactoPersonaExt var : listPersonasContactos) {
						if (var.getEstatusId().longValue() == 1) {
							pojoContacto = var.getIdPersonaContacto();
							break;
						}
					}
				}
			} else if ("N".equals(tipoPersona)) {
				respuesta = this.ifzClientes.buscarNegociosContacto(idProveedor);
				if (respuesta != null) {
					if(respuesta.getErrores().getCodigoError() > 0) 
						return pojoContacto;
					
					listNegociosContactos = (List<ContactoNegocioExt>) respuesta.getBody().getValor("negociosContacto");
					if (listNegociosContactos == null || listNegociosContactos.isEmpty()) 
						return pojoContacto;
					
					for (ContactoNegocioExt var : listNegociosContactos) {
						if (var.getEstatusId().longValue() == 1) {
							pojoContacto = var.getIdPersonaContacto();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findContactoByProveedor(PersonaExt, tipoPersona)", e);
			throw e;
		}
		
		return pojoContacto;
	}

	@Override
	public List<PersonaExt> findPersonaLikeProperty(String propertyName, Object value, String tipoPersona, int limite) throws Exception {
		List<PersonaExt> listaExt = new ArrayList<PersonaExt>();
		
		try {
			if ("P".equals(tipoPersona)) {
				this.ifzPersonas.setInfoSesion(this.infoSesion);
				List<Persona> lista = this.ifzPersonas.findLikeColumnName(propertyName, value.toString());
				for (Persona var : lista) {
					listaExt.add(this.convertidor.PersonaToPersonaExt(var));
				}
			} else {
				this.ifzNegocios.setInfoSesion(this.infoSesion);
				List<Negocio> lista = this.ifzNegocios.findLikeProperty(propertyName, value.toString(), 0);
				for (Negocio var : lista) {
					listaExt.add(this.convertidor.NegocioToPersonaExt(var));
				}
			}
		} catch (Exception e) {
			log.error("Error en CotizacionFac.findPersonaLikeProperty(propertyName, value, tipoPersona, limite)", e);
			throw e;
		}
		
		return listaExt;
	}

	// ------------------------------------------------------------------------------------------------------
	// PRIVADOS
	// ------------------------------------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	private List<SolicitudBodega> recuperarSolicitudes(Long idObra, String campo, Long idOrigen) {
		List<Object> rows = null;
		Object[] item = null;
		String queryString = "";
		// --------------------------------------------
		List<SolicitudBodega> solicitudes = null;
		SolicitudBodega solicitud = null;
		
		try {
			if ((idObra == null || idObra <= 0L) || (idOrigen == null || idOrigen <= 0L))
				return null;
			if (campo == null || "".equals(campo.trim()))
				campo = ":idOrigen in (a.solicitud_cotizacion, a.solicitud_requisicion)";
			else
				campo = "a." + campo + " = :idOrigen";
			queryString += "select a.id, a.id_almacen_origen, b.nombre, b.identificador, case a.estatus > 1 when true then a.estatus + a.completo else a.estatus end as estatus ";
			queryString += "from almacen_traspaso a inner join almacen b on b.id = a.id_almacen_origen ";
			queryString += "where a.tipo = 3 and a.estatus in (0,2) and a.id_obra = :idObra and :campo order by a.id";
			queryString = queryString.replace(":campo", campo);
			queryString = queryString.replace(":idObra", idObra.toString());
			queryString = queryString.replace(":idOrigen", idOrigen.toString());
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
		
		resultado = ((this.infoSesion != null) ? this.infoSesion.getAcceso().getUsuario().getId() : 0L);
		if (resultado == 0)
			resultado = defaultValue;
		else if (resultado == 1 && defaultValue > 0 && resultado != defaultValue)
			resultado = defaultValue;
		
		return resultado;
	}
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 1.2 | 2016-11-18 | Javier Tirado 	| Añado los metodos orderBy, findByProperties, findLikeProperties,findByPropertyWithObra, findLikePropertyWithObra y findInPropertyWithObra. Normal y extendido
 */