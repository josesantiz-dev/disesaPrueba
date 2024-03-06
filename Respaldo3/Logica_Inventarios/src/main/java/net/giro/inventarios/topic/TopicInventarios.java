package net.giro.inventarios.topic;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.comun.TipoMovimientoInventario;
import net.giro.inventarios.comun.TipoMovimientoReferencia;
import net.giro.inventarios.comun.TipoMovimientoReferenciaExtendida;
import net.giro.inventarios.comun.TipoTraspaso;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.logica.TopicEstatusRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TiposTopicEstatus;
import net.giro.plataforma.topics.TopicEventosCompras;
import net.giro.plataforma.topics.TopicEventosGP;
import net.giro.plataforma.topics.TopicEventosInventarios;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/INVENTARIOS")}, 
	mappedName = "topic/INVENTARIOS")
public class TopicInventarios implements MessageListener {
	private static Logger log = Logger.getLogger(TopicInventarios.class);
	private InfoSesion infoSesion;
	private long usuarioId;
	private long empresaId;
	private long aplicacionId;
	// -----------------------------------------------------------------------------------------------
	private List<String> mensajeLogs;
	private TopicEstatusRem ifzTopicEstatus;
	private long idTopicEstatus;
	// -----------------------------------------------------------------------------------------------
	private NQueryRem ifzQuery;
	private ProductoRem ifzProductos;
	private AlmacenMovimientosRem ifzMovimientos;
	private MovimientosDetalleRem ifzMovimientosItems;
	private AlmacenTraspasoRem ifzTraspaso;
	private TraspasoDetalleRem ifzTraspasoItems;
	private AlmacenProductoRem ifzAlmacenProducto;
	private AlmacenRem ifzAlmacenes;
	
	public TopicInventarios() throws NamingException,Exception { 
		InitialContext ctx = null;
		
		try {
			ctx = new InitialContext();
			this.ifzTopicEstatus = (TopicEstatusRem) ctx.lookup("ejb:/Logica_Publico//TopicEstatusFac!net.giro.plataforma.logica.TopicEstatusRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
			this.ifzMovimientos = (AlmacenMovimientosRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzMovimientosItems = (MovimientosDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");
			this.ifzAlmacenProducto = (AlmacenProductoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			this.ifzProductos = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzTraspaso = (AlmacenTraspasoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
			this.ifzTraspasoItems = (TraspasoDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			this.ifzAlmacenes = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
		}
	}
	
	@Override
	public void onMessage(Message message) {
		Gson gson = null;
		TextMessage mensaje = null;
		MensajeTopic mensajeTopic = null;
		TopicEventosInventarios evento = null;
		String eventoParam = "";
		
    	try {
	    	if (message instanceof TextMessage) {
				mensajeLog("############################## TOPIC INVENTARIOS --- INICIO : " + new Date());
				// Transformamos mensaje
				mensaje = (TextMessage) message;
				gson = new Gson();
				mensajeTopic = gson.fromJson(mensaje.getText(), MensajeTopic.class);
				
				this.infoSesion = mensajeTopic.getInfoSesion();
				setInfoSesion(mensajeTopic.getUsuario(), mensajeTopic.getEmpresa(), mensajeTopic.getAplicacion(), null);
				
				// Recuperamos datos del mensaje
				eventoParam = mensajeTopic.getEvento();
				this.idTopicEstatus = (mensajeTopic.getIdTopicEstatus() != null && mensajeTopic.getIdTopicEstatus() > 0L) ? mensajeTopic.getIdTopicEstatus() : 0L;
				if (this.idTopicEstatus <= 0L)
					topicRegistar(eventoParam, mensaje.getText().trim());
				mensajeLog(" --- ID " + this.idTopicEstatus, true);
				Thread.sleep(1000);
				
				// Lanzamos evento requerido
				evento = TopicEventosInventarios.fromString(eventoParam);
				switch (evento) {
					case MOVIMIENTO_OLD:
					case MOVIMIENTO:
						bo_movimiento(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getReferencia()));
						break;
						
					case MOVIMIENTO_CANCEL:
						bo_movimiento_cancelacion(valueToLong(mensajeTopic.getTarget()));
						break;

					case TRASPASOS:
						bo_traspasos(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()));
						break;

					case TRASPASOS_CANCEL:
						bo_traspasos_cancelacion(valueToLong(mensajeTopic.getTarget()));
						break;

					case POST_ENTRADA_OLD:
					case POST_ENTRADA:
						bo_postEntrada(valueToLong(mensajeTopic.getTarget()));
						break;

					case POST_TRASPASO_OLD:
					case POST_TRASPASO:
						bo_postTraspaso(valueToLong(mensajeTopic.getTarget()));
						break;

					case SOLICITUD_BODEGA_OLD:
					case SOLICITUD_BODEGA:
						bo_solicitudBodega(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), valueToLong(mensajeTopic.getUsuario()), valueToString(mensajeTopic.getAtributos()));//, almacenProductos);
						break;

					case SOLICITUD_BODEGA_REQUISICION_OLD:
					case SOLICITUD_BODEGA_REQUISICION:
						bo_solicitudBodegaRequisicion(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), valueToLong(mensajeTopic.getReferenciaExtra()), valueToLong(mensajeTopic.getUsuario()), valueToString(mensajeTopic.getAtributos()));//, almacenProductos);
						break;
						
					case SOLICITUD_BODEGA_DESCARGA:
						bo_solicitudBodegaDescarga(valueToLong(mensajeTopic.getTarget()));
						break;
						
					case SOLICITUD_BODEGA_CANTIDAD:
						bo_solicitudAlmacenProducto(valueToLong(mensajeTopic.getTarget()));
						break;
						
					case SOLICITUD_BODEGA_CANCELACION:
						bo_solicitudBodegaCancelacion(valueToLong(mensajeTopic.getTarget()));
						break;
						
					case SOLICITUD_BODEGA_QUITAR:
						bo_solicitudBodegaQuitarProducto(valueToLong(mensajeTopic.getTarget()), valueToBoolean(mensajeTopic.getReferencia()), valueToString(mensajeTopic.getAtributos()));//, atributos);
						break;

					case PRODUCTO_PRECIOS:
						bo_productoPrecios(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getReferencia()), valueToLong(mensajeTopic.getReferenciaExtra()), valueToString(mensajeTopic.getAtributos()));
						break;
						
					case ALMACEN_QUITAR_ENCARGADO:
						bo_almacenQuitarEncargado(valueToLong(mensajeTopic.getTarget()));
						break;
						
					case ASIGNACION_ALMACENES:
						bo_recuperarAlmacenes(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), valueToInteger(mensajeTopic.getReferenciaExtra()));
						break;
						
					default:
						mensajeLog("****************************** Evento '" + eventoParam + "' no identificado :|");
						break;
				}
			}
	    	
			printLog();
    	} catch (Exception e) {
			printLog("Ocurrio un problema al procesar el mensaje para Topic INVENTARIOS", e);
		}
    }

	// -----------------------------------------------------------------------------------------------
	// EVENTOS 
	// -----------------------------------------------------------------------------------------------
	
	/**
	 * BO-INVENTARIO: BackOffice Inventarios. Efectua los cambios de Entradas o Salidas de productos en Almacen segun movimiento.
	 * @param idMovimiento
	 * @param listDetalles
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bo_movimiento(Long idMovimiento, String referencia) throws Exception { 
		AlmacenMovimientos movimiento = null;
		List<MovimientosDetalle> items = null;

		try {
			idMovimiento = (idMovimiento != null && idMovimiento > 0L ? idMovimiento : 0L);
			referencia = (referencia != null && ! "".equals(referencia.trim()) ? referencia.trim() : "");
			mensajeLog(">>>>>>>>>>> BackOffice INVENTARIOS ");
			mensajeLog("*********** Afectacion Existencia en Inventarios ");
			mensajeLog("*********** Movimiento : " + idMovimiento);
			mensajeLog("*********** Referencia : " + referencia);
			
			if (idMovimiento <= 0L) {
				mensajeLog("Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recupero Movimiento
			movimiento = this.ifzMovimientos.findById(idMovimiento);
			if (movimiento == null || movimiento.getId() == null || movimiento.getId() <= 0L)
				throw new Exception("Movimiento no encontrado :( ");

			items = this.ifzMovimientosItems.findAll(idMovimiento);
			items = (items != null ? items : new ArrayList<MovimientosDetalle>());
			if (items.isEmpty())
				throw new Exception("Movimiento sin productos :( ");
			
			if (movimiento.getTipo() == TipoMovimientoInventario.ENTRADA.ordinal()) 
				aumentaInventario(movimiento, items);
			else if (movimiento.getTipo() == TipoMovimientoInventario.SALIDA.ordinal()) 
				descuentaInventario(movimiento, items);
			else 
				throw new Exception("Tipo de Movimiento desconocido :| ");
		} catch (Exception e) {
			printLog("Ocurrio un problema al procesar el BackOffice para INVENTARIO. Movimiento " + idMovimiento, e);
			throw e;
		}
	}

	private void bo_movimiento_cancelacion(Long idMovimiento) {
		
	}
	
	/**
	 * BO-TRASPASO: BackOffice Traspasos
	 * @param idTraspaso id Traspaso
	 * @param idMovimiento id Movimiento que afecto el traspaso
	 * @param detalles Listado de Productos
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bo_traspasos(Long idTraspaso, Long idMovimiento) throws Exception { 
		List<TraspasoDetalle> lista = null;
		List<MovimientosDetalle> detalles = null;
		AlmacenTraspaso traspaso = null;
		AlmacenMovimientos movimiento = null;
		double cantidad = 0;
		HashMap<Long, Double> productos = null;
		
		try {
			idMovimiento = (idMovimiento != null ? idMovimiento : 0L);
			idTraspaso = (idTraspaso != null ? idTraspaso : 0L);
			mensajeLog(">>>>>>>>>>> BackOrder TRASPASOS ");
			mensajeLog("*********** Afectacion Existencia en Inventarios ");
			mensajeLog("*********** Traspaso   : " + idTraspaso);
			mensajeLog("*********** Movimiento : " + idMovimiento);
			if (idTraspaso <= 0L || idMovimiento <= 0L) { 
				mensajeLog("Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Validaciones Movimiento
			movimiento = this.ifzMovimientos.findById(idMovimiento);
			if (movimiento == null || movimiento.getId() == null || movimiento.getId() <= 0L)
				throw new Exception("No se puedo recuperar el Movimiento " + idMovimiento);
			
			detalles = this.ifzMovimientosItems.findAll(idMovimiento);
			detalles = (detalles != null ? detalles : new ArrayList<MovimientosDetalle>());
			if (detalles == null || detalles.isEmpty())
				throw new Exception("El Movimiento " + idMovimiento + " no tiene productos asignados");

			// Validaciones Traspaso
			traspaso = this.ifzTraspaso.findById(idTraspaso);
			if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
				throw new Exception("No se puedo recuperar el Traspaso " + idTraspaso);
			
			lista = this.ifzTraspasoItems.findAll(idTraspaso);
			if (lista == null || lista.isEmpty())
				throw new Exception("El Traspaso " + idTraspaso + " no tiene productos asignados");
			
			productos = new HashMap<Long, Double>();
			for (MovimientosDetalle detalle : detalles)
				productos.put(detalle.getIdProducto(), detalle.getCantidad());

			try {
				// Calculamos lo recibido
				for (TraspasoDetalle var : lista) {
					if (! productos.containsKey(var.getIdProducto()))
						continue;
					cantidad = productos.get(var.getIdProducto());
					if (cantidad <= 0) 
						continue;
					var.setCantidadRecibida(var.getCantidadRecibida() + cantidad);
					if (var.getCantidad() == var.getCantidadRecibida())
						var.setEstatus(2); // estatus ?  1 transito : 2 completado
				}

				// Guardamos el detalle del traspaso
				lista = this.ifzTraspasoItems.saveOrUpdateList(lista);
				traspaso.setRecibido(1);
				traspaso.setRecibidoPor(movimiento.getCreadoPor());
				traspaso.setFechaRecibido(Calendar.getInstance().getTime());
				this.ifzTraspaso.update(traspaso);
				// Actualizamos el estatus de la solicitudes a bodegas involucradas con el traspaso si corresponde
				actualizaEstatusSolicitudBodega(traspaso);
			} catch (Exception e) {
				log.error("Ocurrio un problema al intentar actualizar lo recibido del Traspaso", e);
				throw e;
			}
			
			try {
				// Actualizamos estatus del traspaso
				for (TraspasoDetalle var : lista) {
					if (var.getCantidad() != var.getCantidadRecibida())
						return; // con alguno que este en false, ya no se actualiza el estatus del traspaso
				}
				
				log.info("Actualizando estatus traspaso ... ");
				traspaso.setCompleto(1);
				traspaso.setModificadoPor(movimiento.getCreadoPor());
				traspaso.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzTraspaso.update(traspaso);
			} catch (Exception e) {
				log.error("Ocurrio un problema al intentar actualizar el estatus del Traspaso", e);
			}
		} catch (Exception e) {
			printLog("Ocurrio un problema al actualizar el BACKORDER TRASPASOS", e);
			throw e;
		}
	}
	
	private void bo_traspasos_cancelacion(Long idTraspaso) throws Exception {
		AlmacenTraspaso traspaso = null;
		List<TraspasoDetalle> detalles = null;
		// ---------------------------------------------------------------------
		HashMap<Long, Double> productos = null;
		List<AlmacenProducto> existencias = null;
		List<Long> tmpProductos = null;
		int factor = 0;
		
		try {
			idTraspaso = (idTraspaso != null && idTraspaso > 0L ? idTraspaso : 0L);
			mensajeLog(">>>>>>>>>>> CANCELACION de Traspaso");
			mensajeLog("*********** Traspaso   : " + idTraspaso);
			if (idTraspaso <= 0L ) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			// Recuperamos DATOS
			traspaso = this.ifzTraspaso.findById(idTraspaso);
			if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
				throw new Exception ("Ocurrio un problema al recuperar el Traspaso indicado: " + idTraspaso);
			
			detalles = this.ifzTraspasoItems.findAll(idTraspaso);
			if (detalles == null || detalles.isEmpty())
				throw new Exception ("Ocurrio un problema al recuperar los productos del Traspaso indicado: " + idTraspaso);

			mensajeLog("*********** Estatus    : " + traspaso.getEstatus());
			factor = (traspaso.getEstatus() == 0) ? 1 : -1;
			
			tmpProductos = new ArrayList<Long>();
			productos = new HashMap<Long, Double>();
			for (TraspasoDetalle solicitado : detalles) {
				productos.put(solicitado.getIdProducto(), solicitado.getCantidad() * factor);
				tmpProductos.add(solicitado.getIdProducto());
			}
			
			// Recuperamos de Almacen los productos involucrados en la SBO
			this.ifzAlmacenProducto.setInfoSesion(this.infoSesion);
			existencias = this.ifzAlmacenProducto.encontrarExistencia(traspaso.getIdAlmacenOrigen().getId(), tmpProductos, true, "");
			if (existencias == null || existencias.isEmpty()) {
				topicSinAccion("Ningun producto en el Almacen del Traspaso indicado: " + idTraspaso);
				return;
			}

			// Devolvemos cantidades a Almacen
			for (AlmacenProducto existencia : existencias) 
				existencia.addExistencia(Math.abs(productos.get(existencia.getIdProducto())));
			
			// Guardamos cambios
			this.ifzAlmacenProducto.setInfoSesion(this.infoSesion);
			this.ifzAlmacenProducto.saveOrUpdateList(existencias);
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el BackOffice Solicitud Almacen Producto: Actualizador de cantidad en solicitud para Inventario", e);
			throw e;
		}
	}
	
	/**
	 * BO-POST-ENTRADA: BackOffice POST-ENTRADA: Registra los movimientos necesarios para soportar el Movimiento de Entrada indicado.
	 * @param idMovimiento
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bo_postEntrada(Long idMovimiento) throws Exception {
		AlmacenMovimientos movimiento = null;
		List<MovimientosDetalle> detalles = null;
		// -------------------------------------------------------
		HashMap<Long, Double> productos = null;

		try {
			idMovimiento = (idMovimiento != null && idMovimiento > 0L ? idMovimiento : 0L);
			mensajeLog(">>>>>>>>>>> BackOffice POST-ENTRADA ");
			mensajeLog("*********** Generar movimientos necesarios para el soporte de la Entrada indicada");
			mensajeLog("*********** Movimiento : " + idMovimiento);
			if (idMovimiento <= 0L) {
				mensajeLog("Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recupero Movimiento y materiales/productos
			movimiento = this.ifzMovimientos.findById(idMovimiento);
			if (movimiento == null || movimiento.getId() == null || movimiento.getId() <= 0L)
				throw new Exception("Movimiento no encontrado :( ");
			if (movimiento.getTipo() != TipoMovimientoInventario.ENTRADA.ordinal()) 
				throw new Exception("El Movimiento indicado no es una Entrada :| ");
			detalles = this.ifzMovimientosItems.findAll(idMovimiento);
			if (detalles == null || detalles.isEmpty())
				throw new Exception("El Movimiento indicado no tiene Material/Productos :| ");
			
			productos = new HashMap<Long, Double>();
			for (MovimientosDetalle detalle : detalles)
				productos.put(detalle.getIdProducto(), detalle.getCantidad());

			if ("OC".equals(movimiento.getTipoEntrada())) {
				generaRegistrosEntrada(movimiento, detalles);
				if ("BO".equals(movimiento.getIdAlmacen().getTipoAlmacen())) 
					generaRegistrosOC(movimiento, productos);
				
			} else if ("TR".equals(movimiento.getTipoEntrada())) {
				autoMessage(TopicEventosInventarios.MOVIMIENTO, idMovimiento, movimiento.getIdTraspaso(), ""); // , detalles); // bo_movimiento(idMovimiento, String.valueOf(movimiento.getIdTraspaso()), detalles);
				autoMessage(TopicEventosInventarios.TRASPASOS, movimiento.getIdTraspaso(), idMovimiento, ""); // , detalles); // bo_traspasos(movimiento.getIdTraspaso(), idMovimiento, detalles);
				generaRegistrosTR(movimiento, productos);
				
			} else if ("DE".equals(movimiento.getTipoEntrada())) {
				autoMessage(TopicEventosInventarios.MOVIMIENTO, idMovimiento, movimiento.getIdTraspaso(), ""); // , detalles); // bo_movimiento(idMovimiento, String.valueOf(movimiento.getIdTraspaso()), detalles);
				autoMessage(TopicEventosInventarios.TRASPASOS, movimiento.getIdTraspaso(), idMovimiento, ""); // , detalles); // bo_traspasos(movimiento.getIdTraspaso(), idMovimiento, detalles);
				generaRegistrosDE(movimiento, productos);
				
			} else if ("SO".equals(movimiento.getTipoEntrada())) {
				autoMessage(TopicEventosInventarios.MOVIMIENTO, idMovimiento, movimiento.getIdObra(), ""); //, detalles); // bo_movimiento(idMovimiento, String.valueOf(movimiento.getIdObra()), detalles);
			}
		} catch (Exception e) {
			printLog("Ocurrio un problema al procesar el BackOffice POST-ENTRADA. Movimiento " + idMovimiento, e);
			throw e;
		}
	}
	
	/**
	 * BO-POST-TRASPASO: BackOffice POST-TRASPASO: Registra la SALIDA necesaria para soportar el TRASPASO indicado.
	 * @param idTraspaso
	 * @throws Exception
	 */
	private void bo_postTraspaso(Long idTraspaso) throws Exception {
		AlmacenTraspaso traspaso = null;
		List<TraspasoDetalle> detalles = null;
		// -----------------------------------------
		AlmacenMovimientos movimiento = null;
		List<MovimientosDetalle> productos = null;
		MovimientosDetalle item = null; 
		
		try {
			idTraspaso = (idTraspaso != null && idTraspaso > 0L ? idTraspaso : 0L);
			mensajeLog(">>>>>>>>>>> BackOffice POST-TRASPASO ");
			mensajeLog("*********** Registrar la SALIDA necesaria para soportar el TRASPASO indicado");
			mensajeLog("*********** Traspaso   : " + idTraspaso);
			if (idTraspaso <= 0L) {
				mensajeLog("Sin accion por Parametros insuficientes ");
				return;
			}
			
			traspaso = this.ifzTraspaso.findById(idTraspaso);
			if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
				throw new Exception("Traspaso no encontrado :( ");
			
			detalles = this.ifzTraspasoItems.findAll(idTraspaso);
			if (detalles == null || detalles.isEmpty())
				throw new Exception("Traspaso sin productos :( ");
			
			movimiento = comprobarMovimientoSalida(idTraspaso);
			if (movimiento == null || movimiento.getId() == null || movimiento.getId() <= 0L) {
				// Genero Movimiento (Entrada o Salida) y detalles
				mensajeLog("Generando Movimiento ... ");
				movimiento = new AlmacenMovimientos();
				movimiento.setSistema(1);
				movimiento.setFecha(traspaso.getFecha());
				movimiento.setIdAlmacen(traspaso.getIdAlmacenOrigen());
				movimiento.setIdOrdenCompra(0L);
				movimiento.setIdTraspaso(traspaso.getId());
				movimiento.setEntrega(traspaso.getEntrega());
				movimiento.setRecibe(traspaso.getRecibe());
				movimiento.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
				if (traspaso.getTipo() == 1)
					movimiento.setTipoEntrada(TipoMovimientoReferencia.TRASPASO.toString());
				else if (traspaso.getTipo() == 2)
					movimiento.setTipoEntrada(TipoMovimientoReferencia.DEVOLUCION.toString());
				movimiento.setCreadoPor(traspaso.getCreadoPor());
				movimiento.setFechaCreacion(traspaso.getFechaCreacion());
				movimiento.setModificadoPor(traspaso.getModificadoPor());
				movimiento.setFechaModificacion(traspaso.getFechaModificacion());
				mensajeLog("OK", true);
	
				mensajeLog("Generando detalles de Movimiento ... ");
				productos = new ArrayList<MovimientosDetalle>();
				for (TraspasoDetalle detalle : detalles) {
					item = new MovimientosDetalle();
					item.setIdProducto(detalle.getIdProducto());
					item.setCantidad(detalle.getCantidad());
					item.setCantidadSolicitada(detalle.getCantidad());
					item.setEstatus(0);
					item.setCreadoPor(detalle.getCreadoPor());
					item.setFechaCreacion(detalle.getFechaCreacion());
					item.setModificadoPor(detalle.getModificadoPor());
					item.setFechaModificacion(detalle.getFechaModificacion());
					productos.add(item);
				}
				mensajeLog("OK", true);
	
				// Guardo entrada (movimiento) y detalles
				log.info("Guardando Movimiento y detalles ... ");
				movimiento.setId(this.ifzMovimientos.save(movimiento));
				for (MovimientosDetalle producto : productos)
					producto.setIdAlmacenMovimiento(movimiento.getId());
				productos = this.ifzMovimientosItems.saveOrUpdateList(productos);
				mensajeLog("OK", true);
			} 
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar procesar el evento del Traspaso: " + idTraspaso + " [" + TopicEventosInventarios.POST_TRASPASO.toString() + "]", e);
			throw e;
		} 

		try {
			mensajeLog("Ejecutando Movimiento (backoffice descuento) ... ");
			this.ifzMovimientos.setInfoSesion(this.infoSesion);
			if (! this.ifzMovimientos.backOfficeInventario(movimiento.getId(), traspaso.getId().toString()))
				throw new Exception("No se pudo realizar la descarga de material. Fallo proceso BackOffice :( ");
			mensajeLog("OK", true);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar procesar el moviemiento de descuento para el evento del Traspaso: " + idTraspaso + " [" + TopicEventosInventarios.POST_TRASPASO.toString() + "]", e);
		}
	}
	
	/**
	 * BO-SBO: BackOffice Solicitud a Bodega por Explosion de Insumos
	 * @param idObra
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bo_solicitudBodega(Long idObra, Long idCotizacion, Long creadoPor, String atributos) throws Exception {
		LinkedHashMap<Long, HashMap<Long, Double>> attValues = null;
		Gson gson = null;
		Type tipo = null;
		// ---------------------------------------------------------------------
		String nombreObra = "";
		Long idResponsable = 0L;
		String nombreResponsable = "";
		// -----------------------------------------------
		Almacen almacen = null;
		Almacen bodega = null;
		Long idRecibe = 0L;
		String nombreRecibe = "";
		// -----------------------------------------------
		AlmacenTraspaso pojoTraspaso = null;
		List<TraspasoDetalle> listDetalles = null;
		TraspasoDetalle detalle = null;
		// -----------------------------------------------
		HashMap<Long, Double> mapaProductos = null;
		HashMap<Long, Double> insumosSuministro = null;
		double cantidad = 0;
		// -----------------------------------------------
		Long idSolicitud = 0L;
		
		try {
			gson = new Gson();
			tipo = new TypeToken<LinkedHashMap<Long, HashMap<Long, Double>>>() {}.getType();
			idObra = idObra != null ? idObra : 0L;
			idCotizacion = idCotizacion != null ? idCotizacion : 0L;
			creadoPor = creadoPor != null ? creadoPor : 0L;
			atributos = atributos != null && ! "".equals(atributos.trim()) ? atributos.trim() : "";
			attValues = gson.fromJson(atributos, tipo);
			attValues = (attValues != null ? attValues : new LinkedHashMap<Long, HashMap<Long, Double>>());
			mensajeLog(">>>>>>>>>>> Solicitud a Bodega (Explosion de Insumos)");
			mensajeLog("*********** Obra        : " + idObra);
			mensajeLog("*********** Cotizacion  : " + idCotizacion);
			mensajeLog("*********** Usuario     : " + creadoPor);
			mensajeLog("*********** Solicitudes : " + attValues.size());
			
			if (idObra <= 0L || creadoPor <= 0L || attValues.isEmpty()) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			// Verificacion de Obra
			// ------------------------------------------------------------------------------------------------------------------------
			nombreObra = obtenerObra(idObra);
			idResponsable = obtenerIdResponsable(idObra);
			nombreResponsable = obtenerResponsable(idObra);

			insumosSuministro = new HashMap<Long, Double>();
			for (Entry<Long, HashMap<Long, Double>> itemAlmacen : attValues.entrySet()) {
				mensajeLog("Solicitud para " + itemAlmacen.getKey());
				mapaProductos = itemAlmacen.getValue();
				
				// Verificacion de Almacen y Bodega para Traspaso
				// ------------------------------------------------------------------------------------------------------------------------
				almacen = this.ifzAlmacenes.findById(itemAlmacen.getKey()); 
				if (almacen == null || almacen.getId() == null || almacen.getId() <= 0L)
					throw new Exception("La Obra no tiene asignado ningun Almacen General como Principal");
				
				bodega = bodegaByObra(idObra);
				if (bodega == null || bodega.getId() == null || bodega.getId() <= 0L) 
					throw new Exception("La Obra no tiene asignada ninguna Bodega");
				
				idRecibe = bodega.getIdEncargado();
				if (idRecibe == null || idRecibe <= 0L) {
					// Alertamos sbre el encargado de Bodega si corresponde
					mensajeLog("La Bodega " + bodega.getNombre() + " (" + bodega.getId() + ") no tiene asignado el Encargado");
					idRecibe = idResponsable;
					nombreRecibe = nombreResponsable;
					if (idRecibe == null || idRecibe <= 0L) 
						throw new Exception("La Obra no tiene asignado un Responsable y la Bodega no tiene Encargado, es necesario alguno para recibir el Traspaso que se generara por Solicitud a Bodega");
				}
				
				// Generamos detalle de traspaso 
				// ------------------------------------------------------------------------------------------------------------------------
				listDetalles = new ArrayList<TraspasoDetalle>();
				for (Entry<Long, Double> itemProducto : mapaProductos.entrySet()) {
					detalle = new TraspasoDetalle();
					detalle.setIdProducto(itemProducto.getKey());
					detalle.setCantidad(itemProducto.getValue());
					detalle.setCantidadRecibida(0);
					detalle.setEstatus(1);
					detalle.setCreadoPor(1);
					detalle.setFechaCreacion(Calendar.getInstance().getTime());
					detalle.setModificadoPor(1);
					detalle.setFechaModificacion(Calendar.getInstance().getTime());
					// Añadimos a lista
					listDetalles.add(detalle);

					cantidad = itemProducto.getValue();
					if (insumosSuministro.containsKey(itemProducto.getKey()))
						cantidad += insumosSuministro.get(itemProducto.getKey());
					insumosSuministro.put(itemProducto.getKey(), cantidad);
				}
				
				if (listDetalles == null || listDetalles.isEmpty())
					throw new Exception("No se realizo el Traspaso. No hay existencia de los Productos en el Almacen " + almacen.getNombre() + " (" + almacen.getId() + ") de la Obra " + nombreObra + " (" + idObra + ")");
				
				// Genero Traspaso (Solicitud a Bodega) [3]
				// ------------------------------------------------------------------------------------------------------------------------
				pojoTraspaso = new AlmacenTraspaso();
				pojoTraspaso.setId(0L);
				pojoTraspaso.setTipo(TipoTraspaso.SOLICITUD_BODEGA.ordinal()); 
				pojoTraspaso.setFecha(Calendar.getInstance().getTime());
				pojoTraspaso.setIdAlmacenOrigen(almacen);
				pojoTraspaso.setEntrega(almacen.getIdEncargado());
				pojoTraspaso.setEntregaNombre(almacen.getNombreEncargado());
				pojoTraspaso.setIdObra(idObra);
				pojoTraspaso.setNombreObra(nombreObra);
				pojoTraspaso.setIdAlmacenDestino(bodega);
				pojoTraspaso.setRecibe(idRecibe);
				pojoTraspaso.setRecibeNombre(nombreRecibe);
				pojoTraspaso.setSolicitudCotizacion(idCotizacion);
				pojoTraspaso.setSolicitudRequisicion(0L);
				pojoTraspaso.setIdEmpresa(this.empresaId);
				pojoTraspaso.setEstatus(0);
				pojoTraspaso.setSistema(0);
				pojoTraspaso.setCreadoPor(creadoPor);
				pojoTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
				pojoTraspaso.setModificadoPor(creadoPor);
				pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardo Traspaso y detalles
				// ------------------------------------------------------------------------------------------------------------------------
				pojoTraspaso.setId(this.ifzTraspaso.save(pojoTraspaso));
				for (TraspasoDetalle item : listDetalles) {
					item.setIdAlmacenTraspaso(pojoTraspaso.getId());
					item.setId(0L);
				}
				
				// Guardo los detalles del traspaso
				listDetalles = this.ifzTraspasoItems.saveOrUpdateList(listDetalles);
				idSolicitud = pojoTraspaso.getId();
				
				// Descarga de cantidades en Inventario por Solicitud a Bodega
				autoMessage(TopicEventosInventarios.SOLICITUD_BODEGA_DESCARGA, idSolicitud, null, null); 
				
				// Notificamos al Encargado de Almacen del Traspaso (Salida de Material)
				notificaEncargadoAlmacen(pojoTraspaso.getId());
	
				// Notificamos al Encargado de Bodega del Traspaso para su recepcion cuando corresponda
				notificaEncargadoBodega(pojoTraspaso.getId());
				
				// Actualizo la cantidad en solicitud a los productos en Almacen
				autoMessage(TopicEventosInventarios.SOLICITUD_BODEGA_CANTIDAD, idSolicitud, null, null); 
				mensajeLog("Solicitud para " + itemAlmacen.getKey() + " FIN");
			}

			// Lanzamos evento de suministro para Cotizacion o Explosion de Insumos segun se requiera
			if (idCotizacion > 0L)
				eventoSuministroCotizacion(idCotizacion, insumosSuministro);
			else
				eventoSuministroExplosionInsumos(idObra, insumosSuministro);
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar la Solicitud a Bodega de la Explosion de Insumos en la Obra " + idObra, e);
			throw e;
		}
	}

	/**
	 * BO-SBR: BackOffice Solicitud a Bodega por Requisicion
	 * @param idRequisicion
	 * @param almacenProductos
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bo_solicitudBodegaRequisicion(Long idObra, Long idRequisicion, Long idCotizacion, Long creadoPor, String atributos) throws Exception {
		LinkedHashMap<Long, HashMap<Long, Double>> almacenProductos = null;
		Gson gson = null;
		Type tipo = null;
		// ---------------------------------------------------------------------
		String nombreObra = "";
		Long idResponsable = 0L;
		String nombreResponsable = "";
		// -----------------------------------------------
		Almacen almacen = null;
		Almacen bodega = null;
		Long idRecibe = 0L;
		String nombreRecibe = "";
		// -----------------------------------------------
		AlmacenTraspaso pojoTraspaso = null;
		List<TraspasoDetalle> listDetalles = null;
		TraspasoDetalle detalle = null;
		// -----------------------------------------------
		HashMap<Long,Double> mapaProductos = null;
		HashMap<Long,Double> insumosSuministro = null;
		double cantidad = 0;
		// -----------------------------------------------
		Long idSolicitud = 0L;
		
		try {
			gson = new Gson();
			tipo = new TypeToken<LinkedHashMap<Long, HashMap<Long, Double>>>() {}.getType();
			idObra = idObra != null ? idObra : 0L;
			idCotizacion = idCotizacion != null ? idCotizacion : 0L;
			idRequisicion = idRequisicion != null ? idRequisicion : 0L;
			creadoPor = creadoPor != null ? creadoPor : 0L;
			atributos = atributos != null && ! "".equals(atributos.trim()) ? atributos.trim() : "";
			almacenProductos = gson.fromJson(atributos, tipo);
			almacenProductos = (almacenProductos != null ? almacenProductos : new LinkedHashMap<Long, HashMap<Long, Double>>());
			mensajeLog(">>>>>>>>>>> Solicitud a Bodega (Requisicion)");
			mensajeLog("*********** Obra        : " + idObra);
			mensajeLog("*********** Requisicion : " + idRequisicion);
			mensajeLog("*********** Cotizacion  : " + idCotizacion);
			mensajeLog("*********** Usuario     : " + creadoPor);
			mensajeLog("*********** Productos   : " + almacenProductos.size());

			if (idObra <= 0L || idRequisicion <= 0L || almacenProductos.isEmpty()) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			// Verificacion de Obra
			// ------------------------------------------------------------------------------------------------------------------------
			nombreObra = obtenerObra(idObra);
			idResponsable = obtenerIdResponsable(idObra);
			nombreResponsable = obtenerResponsable(idObra);

			insumosSuministro = new HashMap<Long, Double>();
			for (Entry<Long, HashMap<Long, Double>> itemAlmacen : almacenProductos.entrySet()) {
				mapaProductos = itemAlmacen.getValue();
				
				// Verificacion de Almacen y Bodega para Traspaso
				// ------------------------------------------------------------------------------------------------------------------------
				almacen = this.ifzAlmacenes.findById(itemAlmacen.getKey()); //recuperarAlmacenByObra(idObra, pojoObra.getIdSucursal());
				if (almacen == null || almacen.getId() == null || almacen.getId() <= 0L)
					throw new Exception("La Obra no tiene asignado ninguna Almacen General como Principal");
				
				bodega = bodegaByObra(idObra);
				if (bodega == null || bodega.getId() == null || bodega.getId() <= 0L) 
					throw new Exception("La Obra no tiene asignada ninguna Bodega");
				idRecibe = bodega.getIdEncargado();
				
				// Alertamos sbre el encargado de Bodega si corresponde
				if (idRecibe == null || idRecibe <= 0L) {
					mensajeLog("La Bodega " + bodega.getNombre() + " (" + bodega.getId() + ") no tiene asignado el Encargado");
					idRecibe = idResponsable;
					nombreRecibe = nombreResponsable;
					if (idRecibe == null || idRecibe <= 0L) 
						throw new Exception("La Obra no tiene asignado un Responsable y la Bodega no tiene Encargado, es necesario alguno para recibir el Traspaso que se generara por Solicitud a Bodega");
				}
				
				// Generamos detalle de traspaso 
				// ------------------------------------------------------------------------------------------------------------------------
				listDetalles = new ArrayList<TraspasoDetalle>();
				for (Entry<Long, Double> itemProducto : mapaProductos.entrySet()) {
					detalle = new TraspasoDetalle();
					detalle.setId(0L);
					detalle.setIdProducto(itemProducto.getKey());
					detalle.setCantidad(itemProducto.getValue());
					detalle.setCantidadRecibida(0);
					detalle.setEstatus(1);
					detalle.setCreadoPor(creadoPor);
					detalle.setFechaCreacion(Calendar.getInstance().getTime());
					detalle.setModificadoPor(creadoPor);
					detalle.setFechaModificacion(Calendar.getInstance().getTime());
					// Añado a listado
					listDetalles.add(detalle);

					cantidad = itemProducto.getValue();
					if (insumosSuministro.containsKey(itemProducto.getKey()))
						cantidad += insumosSuministro.get(itemProducto.getKey());
					insumosSuministro.put(itemProducto.getKey(), cantidad);
				}
				
				// Genero Traspaso (Solicitud a Bodega) [3]
				// -------------------------------------------------------------------------------------
				mensajeLog("Generando Traspaso ...");
				pojoTraspaso = new AlmacenTraspaso();
				pojoTraspaso.setId(0L);
				pojoTraspaso.setTipo(TipoTraspaso.SOLICITUD_BODEGA.ordinal()); 
				pojoTraspaso.setFecha(Calendar.getInstance().getTime());
				pojoTraspaso.setIdAlmacenOrigen(almacen);
				pojoTraspaso.setEntrega(almacen.getIdEncargado());
				pojoTraspaso.setEntregaNombre(almacen.getNombreEncargado());
				pojoTraspaso.setIdObra(idObra);
				pojoTraspaso.setNombreObra(nombreObra);
				pojoTraspaso.setIdAlmacenDestino(bodega);
				pojoTraspaso.setRecibe(idRecibe);
				pojoTraspaso.setRecibeNombre(nombreRecibe);
				pojoTraspaso.setSolicitudCotizacion(0L);
				pojoTraspaso.setSolicitudRequisicion(idRequisicion);
				pojoTraspaso.setIdEmpresa(this.empresaId);
				pojoTraspaso.setEstatus(0);
				pojoTraspaso.setSistema(0);
				pojoTraspaso.setCreadoPor(creadoPor);
				pojoTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
				pojoTraspaso.setModificadoPor(creadoPor);
				pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardo el Traspaso 
				mensajeLog("Guardando Traspaso y detalles ...");
				pojoTraspaso.setId(this.ifzTraspaso.save(pojoTraspaso));
				for (TraspasoDetalle item : listDetalles) {
					item.setIdAlmacenTraspaso(pojoTraspaso.getId());
					item.setId(0L);
				}
				
				// Guardo los detalles del traspaso
				listDetalles = this.ifzTraspasoItems.saveOrUpdateList(listDetalles);
				idSolicitud = pojoTraspaso.getId();
				
				// Descarga de cantidades en Inventario por Solicitud a Bodega
				autoMessage(TopicEventosInventarios.SOLICITUD_BODEGA_DESCARGA, idSolicitud, null, null); // bo_solicitudBodegaDescarga(idSolicitud);

				// Notificamos al Comprador sobre la Solicitud de Bodega (Salida de Material)
				notificaComprador(creadoPor, pojoTraspaso, listDetalles);
	
				// Notificamos al Encargado de Almacen sobre la Solicitud de Bodega (Salida de Material)
				notificaEncargadoAlmacen(pojoTraspaso.getId());
				
				// Actualizo la cantidad en solicitud a los productos en Almacen
				autoMessage(TopicEventosInventarios.SOLICITUD_BODEGA_CANTIDAD, idSolicitud, null, null); // bo_solicitudAlmacenProducto(idSolicitud);
			}
			
			// Lanzamos evento de suministro para Cotizacion o Requisicion segun se requiera
			if (idCotizacion > 0L)
				eventoSuministroCotizacion(idCotizacion, insumosSuministro);
			else
				eventoSuministroRequisicion(idRequisicion, insumosSuministro);
		} catch (Exception e) {
			printLog("Ocurrio un problema al generar la Solicitud a Bodega de la Explosion de Insumos", e);
			throw e;
		}
	}

	/**
	 * BO-SBO: BackOffice Descarga de cantidades en Inventario por Solicitud a Bodega
	 * @param idSolicitud
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bo_solicitudBodegaDescarga(Long idSolicitud) throws Exception {
		AlmacenTraspaso pojoTraspaso = null;
		List<TraspasoDetalle> listDetalles = null;
		// ------------------------------------------------
		List<AlmacenMovimientos> salidas = null;
		AlmacenMovimientos pojoSalida = null;
		List<MovimientosDetalle> listSalidaDetalle = null;
		MovimientosDetalle salidaDetalle = null;
		
		try {
			idSolicitud = (idSolicitud != null ? idSolicitud : 0L);
			pojoTraspaso = this.ifzTraspaso.findById(idSolicitud);
			if (pojoTraspaso == null || pojoTraspaso.getId() == null || pojoTraspaso.getId() <= 0L)
				throw new Exception("No se pudo encontrar la Solicitud a Bodega Indicada: " + idSolicitud);
			listDetalles = this.ifzTraspasoItems.findAll(idSolicitud);
			if (listDetalles == null)
				listDetalles = new ArrayList<TraspasoDetalle>();
			
			mensajeLog(">>>>>>>>>>> Solicitud a Bodega (Descarga)");
			mensajeLog("*********** Solicitud : " + idSolicitud);
			mensajeLog("*********** Productos : " + listDetalles.size());
			if (idSolicitud <= 0L || listDetalles.isEmpty()) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			salidas = this.ifzMovimientos.findSalidaByTraspaso(idSolicitud, TipoTraspaso.SOLICITUD_BODEGA.ordinal(), 0);
			if (salidas != null && ! salidas.isEmpty()) {
				// -------------------------------------------------------------------------------------
				// Recuperamos Movimiento SALIDA de la Solicitud a Bodega
				// -------------------------------------------------------------------------------------
				mensajeLog("Recuperamos Movimiento SALIDA de la Solicitud a Bodega ... ");
				pojoSalida = salidas.get(0);
				listSalidaDetalle = this.ifzMovimientosItems.findAll(pojoSalida.getId());
			} else {
				// -------------------------------------------------------------------------------------
				// Genera Movimiento SALIDA de la Solicitud a Bodega
				// -------------------------------------------------------------------------------------
				mensajeLog("Generando movimiento de Salida para la Solicitud a Bodega ... ");
				pojoSalida = new AlmacenMovimientos();
				pojoSalida.setId(0L);
				pojoSalida.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
				pojoSalida.setTipoEntrada(TipoMovimientoReferencia.TRASPASO.toString());
				pojoSalida.setIdOrdenCompra(0L);
				pojoSalida.setIdTraspaso(pojoTraspaso.getId());
				pojoSalida.setIdAlmacen(pojoTraspaso.getIdAlmacenOrigen());
				pojoSalida.setFecha(pojoTraspaso.getFecha());
				pojoSalida.setEntrega(pojoTraspaso.getEntrega());
				pojoSalida.setEntregaNombre(pojoTraspaso.getEntregaNombre());
				pojoSalida.setRecibe(pojoTraspaso.getRecibe());
				pojoSalida.setRecibeNombre(pojoTraspaso.getRecibeNombre());
				pojoSalida.setIdEmpresa(pojoTraspaso.getIdEmpresa());
				pojoSalida.setCreadoPor(pojoTraspaso.getCreadoPor());
				pojoSalida.setFechaCreacion(pojoTraspaso.getFechaCreacion());
				pojoSalida.setModificadoPor(pojoTraspaso.getModificadoPor());
				pojoSalida.setFechaModificacion(pojoTraspaso.getFechaModificacion());
				pojoSalida.setSistema(1);
				
				// Genero detalles de salida
				listSalidaDetalle = new ArrayList<MovimientosDetalle>();
				for (TraspasoDetalle item : listDetalles) {
					salidaDetalle = new MovimientosDetalle();
					salidaDetalle.setIdAlmacenMovimiento(0L);
					salidaDetalle.setIdProducto(item.getIdProducto());
					salidaDetalle.setCantidad(item.getCantidad());
					salidaDetalle.setCantidadSolicitada(item.getCantidad());
					salidaDetalle.setEstatus(0);
					salidaDetalle.setCreadoPor(item.getCreadoPor());
					salidaDetalle.setFechaCreacion(item.getFechaCreacion());
					salidaDetalle.setModificadoPor(item.getModificadoPor());
					salidaDetalle.setFechaModificacion(item.getFechaModificacion());
					// Añadimos a la lista
					listSalidaDetalle.add(salidaDetalle);
				}
	
				// Guardo salida (movimiento)
				mensajeLog("Guardando movimiento de Salida del Traspaso ...");
				pojoSalida.setId(this.ifzMovimientos.save(pojoSalida));
				for (MovimientosDetalle item : listSalidaDetalle)
					item.setIdAlmacenMovimiento(pojoSalida.getId());
				listSalidaDetalle = this.ifzMovimientosItems.saveOrUpdateList(listSalidaDetalle);
			}

			// Actualizo estatus
			//this.ifzSolicitudEstatus.saveMovimiento(idSolicitud, pojoSalida.getId());
			
			// Actualizo INVENTARIO
			descuentaInventario(pojoSalida, listSalidaDetalle);
		} catch (Exception e) {
			printLog("Ocurrio un problema al realizar la descarga de Material/Productos de la Solicitud a Bodega", e);
			throw e;
		}
	}
	
	/**
	 * BO-SBO-CANT: BackOffice Solicitud a Bodega. Actualiza la cantidad (aumenta o disminuye) en solicitud del producto en Almacen
	 * @param idSolicitud
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bo_solicitudAlmacenProducto(Long idSolicitud) throws Exception {
		AlmacenTraspaso solicitud = null;
		List<TraspasoDetalle> productos = null;
		int factor = 0;
		// ---------------------------------------------------------------------
		List<AlmacenProducto> existencias = null;
		List<Long> tmpProductos = null;
		HashMap<Long, Integer> indexes = null;
		double cantidad = 0;
		
		try {
			idSolicitud = (idSolicitud != null ? idSolicitud : 0L);
			productos = new ArrayList<TraspasoDetalle>();
			mensajeLog(">>>>>>>>>>> Solicitud a Bodega (Actualizacion de cantidad en Inventario)");
			mensajeLog("*********** Solicitud : " + idSolicitud);
			if (idSolicitud <= 0L ) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			// Recuperamos DATOS
			solicitud = this.ifzTraspaso.findById(idSolicitud);
			if (solicitud == null || solicitud.getId() == null || solicitud.getId() <= 0L)
				throw new Exception ("Ocurrio un problema al recuperar la Solicitud a Bodega: " + idSolicitud);
			
			productos = this.ifzTraspasoItems.findAll(idSolicitud);
			if (productos == null || productos.isEmpty())
				throw new Exception ("Ocurrio un problema al recuperar los productos de la Solicitud a Bodega: " + idSolicitud);
			
			tmpProductos = new ArrayList<Long>();
			for (TraspasoDetalle producto : productos) 
				tmpProductos.add(producto.getIdProducto());
			
			existencias = this.ifzAlmacenProducto.encontrarExistencia(solicitud.getIdAlmacenOrigen().getId(), tmpProductos, true, "");
			if (existencias == null || existencias.isEmpty()) {
				topicSinAccion("Ningun producto en al Almacen de la Solicitud a Bodega: " + idSolicitud);
				return;
			}
			
			// Generamos indice de existencias
			indexes = new HashMap<Long, Integer>();
			for (AlmacenProducto existencia : existencias)
				indexes.put(existencia.getIdProducto(), existencias.indexOf(existencia));
			
			// Validamos si debemos aumentar o disminuir la cantidad (indicado en solicitud) del producto en el Almacen
			mensajeLog("*********** Estatus   : " + solicitud.getEstatus());
			factor = (solicitud.getEstatus() == 0) ? 1 : -1;
			
			// Actualizo cantidad del producto en el Almacen
			for (TraspasoDetalle producto : productos) {
				if (! indexes.containsKey(producto.getIdProducto()))
					continue;
				cantidad = existencias.get(indexes.get(producto.getIdProducto())).getSolicitud();
				cantidad += (producto.getCantidad() * factor); 
				if (cantidad <= 0)
					cantidad = 0;
				existencias.get(indexes.get(producto.getIdProducto())).setSolicitud(cantidad);
			}
			
			// Guardamos cambios
			this.ifzAlmacenProducto.saveOrUpdateList(existencias);
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el BackOffice Solicitud Almacen Producto: Actualizador de cantidad en solicitud para Inventario", e);
			throw e;
		}
	}
	
	/**
	 * BO-SBO-CANCEL: BackOffice Solicitud a Bodega. Cancelacion de Solicitud a Bodega
	 * @param idSolicitud
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bo_solicitudBodegaCancelacion(Long idSolicitud) throws Exception {
		AlmacenTraspaso solicitud = null;
		List<TraspasoDetalle> solicitados = null;
		// ---------------------------------------------------------------------
		HashMap<Long, Double> productos = null;
		List<AlmacenProducto> existencias = null;
		List<Long> tmpProductos = null;
		int factor = 0;
		// ---------------------------------------------------------------------
		long idCotizacion = 0L;
		long idRequisicion = 0L;
		
		try {
			idSolicitud = (idSolicitud != null ? idSolicitud : 0L);
			mensajeLog(">>>>>>>>>>> Solicitud a Bodega (Actualizacion de cantidad en Inventario)");
			mensajeLog("*********** Solicitud : " + idSolicitud);
			if (idSolicitud <= 0L ) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			// Recuperamos DATOS
			solicitud = this.ifzTraspaso.findById(idSolicitud);
			if (solicitud == null || solicitud.getId() == null || solicitud.getId() <= 0L)
				throw new Exception ("Ocurrio un problema al recuperar la Solicitud a Bodega: " + idSolicitud);
			
			solicitados = this.ifzTraspasoItems.findAll(idSolicitud);
			if (solicitados == null || solicitados.isEmpty())
				throw new Exception ("Ocurrio un problema al recuperar los productos de la Solicitud a Bodega: " + idSolicitud);
			
			mensajeLog("*********** Estatus   : " + solicitud.getEstatus());
			factor = (solicitud.getEstatus() == 0) ? 1 : -1;
			idCotizacion = solicitud.getSolicitudCotizacion();
			idRequisicion = solicitud.getSolicitudRequisicion();
			
			tmpProductos = new ArrayList<Long>();
			productos = new HashMap<Long, Double>();
			for (TraspasoDetalle solicitado : solicitados) {
				productos.put(solicitado.getIdProducto(), solicitado.getCantidad() * factor);
				tmpProductos.add(solicitado.getIdProducto());
			}
			
			// Recuperamos de Almacen los productos involucrados en la SBO
			existencias = this.ifzAlmacenProducto.encontrarExistencia(solicitud.getIdAlmacenOrigen().getId(), tmpProductos, true, "");
			if (existencias == null || existencias.isEmpty()) {
				topicSinAccion("Ningun producto en al Almacen de la Solicitud a Bodega: " + idSolicitud);
				return;
			}

			// Devolvemos cantidades a Almacen
			for (AlmacenProducto existencia : existencias) {
				existencia.addExistencia(Math.abs(productos.get(existencia.getIdProducto())));
				existencia.addSolicitud(productos.get(existencia.getIdProducto()));
			}
			
			// Guardamos cambios
			this.ifzAlmacenProducto.saveOrUpdateList(existencias);
			
			// Actualizamos Suministros
			if (idCotizacion > 0L) 
				eventoCantidadCotizacion(idCotizacion, productos);
			else if (idRequisicion > 0L) 
				eventoSuministroRequisicion(idRequisicion, productos);
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el BackOffice Solicitud Almacen Producto: Actualizador de cantidad en solicitud para Inventario", e);
			throw e;
		}
	}
	
	private void bo_solicitudBodegaQuitarProducto(Long idSolicitud, boolean afectarInventario, String atributos) throws Exception {
		HashMap<Long, Double> attValues = null;
		Gson gson = null;
		Type tipo = null;
		// ---------------------------------------------------------------------
		AlmacenTraspaso solicitud = null;
		List<TraspasoDetalle> solicitados = null;
		List<TraspasoDetalle> borrables = null;
		// ---------------------------------------------------------------------
		List<AlmacenProducto> existencias = null;
		List<Long> tmpProductos = null;
		// ---------------------------------------------------------------------
		long idCotizacion = 0L;
		long idRequisicion = 0L;
		
		try {
			gson = new Gson();
			tipo = new TypeToken<HashMap<Long, Double>>() {}.getType();
			idSolicitud = (idSolicitud != null ? idSolicitud : 0L);
			atributos = atributos != null && ! "".equals(atributos.trim()) ? atributos.trim() : "";
			attValues = gson.fromJson(atributos, tipo);
			attValues = (attValues != null ? attValues : new HashMap<Long, Double>());
			mensajeLog(">>>>>>>>>>> Solicitud a Bodega (Quitar Producto de Solicitud a Bodega)");
			mensajeLog("*********** Solicitud : " + idSolicitud);
			mensajeLog("*********** Productos : " + attValues.size());
			mensajeLog("*********** Afectar   : " + afectarInventario);
			if (idSolicitud <= 0L || attValues.size() <= 0) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			// Recuperamos DATOS
			solicitud = this.ifzTraspaso.findById(idSolicitud);
			if (solicitud == null || solicitud.getId() == null || solicitud.getId() <= 0L)
				throw new Exception ("Ocurrio un problema al recuperar la Solicitud a Bodega: " + idSolicitud);
			
			solicitados = this.ifzTraspasoItems.findAll(idSolicitud);
			if (solicitados == null || solicitados.isEmpty())
				throw new Exception ("Ocurrio un problema al recuperar los productos de la Solicitud a Bodega: " + idSolicitud);
			
			mensajeLog("*********** Estatus   : " + solicitud.getEstatus());
			idCotizacion = solicitud.getSolicitudCotizacion();
			idRequisicion = solicitud.getSolicitudRequisicion();
			
			// Quitamos el producto de la Solicitud si corresponde
			borrables = new ArrayList<TraspasoDetalle>();
			for (TraspasoDetalle item : solicitados) {
				if (attValues.containsKey(item.getIdProducto())) 
					borrables.add(item);
			}
			
			if (borrables != null && ! borrables.isEmpty())
				this.ifzTraspasoItems.deleteAll(borrables);
			
			// Validacion de cantidad
			tmpProductos = new ArrayList<Long>();
			for (Entry<Long, Double> item : attValues.entrySet()) {
				item.setValue(((item.getValue() > 0) ? (item.getValue() * -1) : item.getValue()));
				tmpProductos.add(item.getKey());
			}
			
			// Replicamos afectacion de suministro en retrospectiva 
			
			if (afectarInventario) {
				// Recuperamos de Almacen los productos involucrados en la SBO
				existencias = this.ifzAlmacenProducto.encontrarExistencia(solicitud.getIdAlmacenOrigen().getId(), tmpProductos, true, "");
				if (existencias == null || existencias.isEmpty()) {
					topicSinAccion("Ningun producto en el Almacen de la Solicitud a Bodega: " + idSolicitud);
					return;
				}
	
				// Devolvemos cantidades a Almacen
				for (AlmacenProducto existencia : existencias) {
					existencia.addExistencia(Math.abs(attValues.get(existencia.getIdProducto())));
					existencia.addSolicitud(attValues.get(existencia.getIdProducto()));
				}
				
				// Guardamos cambios
				this.ifzAlmacenProducto.saveOrUpdateList(existencias);
			}
			
			// Actualizamos Suministros
			if (idCotizacion > 0L) 
				eventoCantidadCotizacion(idCotizacion, attValues);
			else if (idRequisicion > 0L) 
				eventoSuministroRequisicion(idRequisicion, attValues);
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el BackOffice Solicitud Almacen Producto: Actualizador de cantidad en solicitud para Inventario", e);
			throw e;
		}
	}
	
	/**
	 * BackOffice Productos: Actualizacion de precios en productos
	 * @param items Mapa de idProducto-precio
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bo_productoPrecios(Long idOrdenCompra, String ordenCompra, Long idMovimiento, String atributos) throws Exception { 
		HashMap<Long, List<Double>> precios = null;
		HashMap<Long, Double> items = null;
		Gson gson = null;
		Type tipo = null;
		// -----------------------------------------------
		List<Producto> productos = null;
		Producto producto = null;
		double precio = 0;
		
		try {
			gson = new Gson();
			idOrdenCompra = (idOrdenCompra != null ? idOrdenCompra : 0L);
			ordenCompra = (ordenCompra != null ? ordenCompra : "");
			idMovimiento = (idMovimiento != null ? idMovimiento : 0L);
			if (idMovimiento > 0L) {
				tipo = new TypeToken<HashMap<Long, List<Double>>>() {}.getType();
				precios = gson.fromJson(atributos, tipo);
				precios = (precios != null ? precios : new HashMap<Long, List<Double>>());
				actualizaPrecios(idOrdenCompra, ordenCompra, idMovimiento, precios);
				return;
			} 

			tipo = new TypeToken<HashMap<Long, Double>>() {}.getType();
			items = gson.fromJson(atributos, tipo);
			items = (items != null ? items : new HashMap<Long, Double>());
		} catch (Exception e) {
			log.error("Ocurrio un problema al interpretar los parametros para el Actualizador de Precios (BackOffice de Productos)", e);
			throw e;
		}
		
		try {
			mensajeLog(">>>>>>>>>>> BackOffice Productos: Actualizador de Precios *******************");
			mensajeLog("*********** idOrdenCompra : " + idOrdenCompra);
			mensajeLog("*********** OrdenCompra   : " + ordenCompra);
			mensajeLog("*********** Productos     : " + items.size());
			if (idOrdenCompra <= 0L || items.isEmpty()) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}

			productos = new ArrayList<Producto>();
			for (Entry<Long, Double> item : items.entrySet()) {
				producto = this.ifzProductos.findById(item.getKey());
				if (producto == null || producto.getId() == null || producto.getId() <= 0L) 
					continue;
				if (producto.getPrecioCompra() > item.getValue().doubleValue())
					continue;
				
				precio = item.getValue().doubleValue();
				producto.setIdOrdenCompra(idOrdenCompra);
				producto.setOrdenCompra(ordenCompra);
				producto.setPrecioCompra(precio);
				producto.setPrecioUnitario((precio * 1.1)); // 10%
				producto.setPrecioVenta((precio * 1.7)); // 70%
				productos.add(producto);
			}
			
			if (productos == null || productos.isEmpty()) {
				topicSinAccion("Sin accion. No se actualizo ningun producto ");
				return;
			}
			
			// Actualimos precios de productos
			this.ifzProductos.setInfoSesion(this.infoSesion);
			this.ifzProductos.saveOrUpdateList(productos);
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el BackOffice Productos: Actualizador de Precios", e);
			throw e;
		}
	}

	/**
	 * BO-DEA: BackOffice para quitar un empleado de la asignacion de Almacenes a las Obras
	 * @param idEmpleado
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void bo_almacenQuitarEncargado(Long idEmpleado) throws Exception {
		List<Almacen> listAlmacenes = null;

		try {
			idEmpleado = (idEmpleado != null ? idEmpleado : 0L);
			mensajeLog(">>>>>>>>>>> BackOffice Desasignar Encargado en Almacenes ******************* ");
			mensajeLog("*********** Desasignar Encargado en Almacenes por Baja de Empleado *********** ");
			mensajeLog("*********** Empleado : " + idEmpleado);
			
			if (idEmpleado <= 0L) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			// Recupero Movimiento
			listAlmacenes = this.ifzAlmacenes.findByProperty("idEncargado", idEmpleado);
			if (listAlmacenes != null && ! listAlmacenes.isEmpty()) {
				for (Almacen item : listAlmacenes) {
					item.setIdEncargado(0L);
					item.setNombreEncargado("");
				}
				this.ifzAlmacenes.saveOrUpdateList(listAlmacenes);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el BackOffice de INVENTARIO: Desasignar Encargado (" + idEmpleado + ") de Almacenes/Bodegas", e);
			throw e;
		}
	}

	/**
	 * Recuperar Almacenes para asignacion en Obra
	 * @param idObra
	 * @param idSucursal
	 * @param tipoObra
	 * @throws Exception
	 */
	private void bo_recuperarAlmacenes(Long idObra, Long idSucursal, Integer tipoObra) throws Exception {
		List<Almacen> listAlmacenes = null;
		HashMap<Long, String> almacenes = null;
		Long idAlmacenPrincipal = 0L;
		int tipoAlmacenes = 0;
		// --------------------------------------------------------
		Type tipo = null;
		MensajeTopic msgTopic = null;
		String target = "";
		String referencia = "";
		String atributos = "";

		try {
			idObra = (idObra != null ? idObra : 0L);
			idSucursal = (idSucursal != null ? idSucursal : 0L);
			tipoObra = (tipoObra != null ? tipoObra : 0);
			mensajeLog(">>>>>>>>>>> BackOffice INVENTARIOS ******************* ");
			mensajeLog("*********** Recuperar Almacenes para asignacion en Obra *********** ");
			mensajeLog("*********** Obra     : " + idObra);
			mensajeLog("*********** Sucursal : " + idSucursal);
			mensajeLog("*********** tipoObra : " + tipoObra);
			if (idObra <= 0L || idSucursal <= 0L) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			// Recupero Movimiento
			tipoAlmacenes = (tipoObra == 4 ? 3 : 1);
			listAlmacenes = this.ifzAlmacenes.findAll(tipoAlmacenes);
			if (listAlmacenes == null || listAlmacenes.isEmpty()) {
				topicSinAccion("No se encontro ningun Almacen activo. Tipo: " + tipoAlmacenes);
				return;
			}
			
			almacenes = new HashMap<Long, String>();
			for (Almacen almacen : listAlmacenes) {
				if (idAlmacenPrincipal.longValue() <= 0L && idSucursal.longValue() == almacen.getIdSucursal())
					idAlmacenPrincipal = almacen.getId();
				almacenes.put(almacen.getId(), almacen.getNombre());
			}

			tipo = new TypeToken<HashMap<Long, String>>() {}.getType();
			atributos = (new Gson()).toJson(almacenes, tipo);
			idAlmacenPrincipal = (idAlmacenPrincipal != null ? idAlmacenPrincipal : 0L);
			
			target = idObra.toString();
			referencia = idAlmacenPrincipal.toString();
			atributos = (atributos != null ? atributos.trim() : "");
			msgTopic = new MensajeTopic(TopicEventosGP.OBRA_ASIGNAR_ALMACENES, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el BackOffice de INVENTARIO: Recuperar Almacenes para asignacion en Obra: " + idObra, e);
			throw e;
		}
	}

	// -----------------------------------------------------------------------------------------------
	// METODOS PRIVADOS 
	// -----------------------------------------------------------------------------------------------

	private void autoMessage(TopicEventosInventarios evento, Object target, Object referencia, Object atributos) {
		MensajeTopic msgTopic = null;
		
		try {
			if (evento == null)
				return;
			target = (target != null ? target : "");
			referencia = (referencia != null ? referencia : "");
			atributos = (atributos != null ? atributos : "");
			msgTopic = new MensajeTopic(evento, target.toString(), referencia.toString(), atributos.toString(), this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) { 
			log.error("Ocurrio un problema al autoenviar mensaje JMS\n" + this.getClass().getCanonicalName() + ".autoMessage(evento, target, referencia, atributos)", e);
		} 
	}

	/**
	 * Aumenta el Inventario de los productos indicados
	 * @param movimiento
	 * @param detalles Listado de productos
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private boolean aumentaInventario(AlmacenMovimientos movimiento, List<MovimientosDetalle> detalles) {
		HashMap<Long, Double> productos = null;
		List<AlmacenProducto> existencias = null;
		AlmacenProducto existencia = null;
		long idAlmacenBodega = 0L;
		
		try {
			// Recuperamos el almacen afectado
			idAlmacenBodega = movimiento.getIdAlmacen().getId();
			if (movimiento.getTipo() == TipoMovimientoInventario.ENTRADA.ordinal() && "OC".equals(movimiento.getTipoEntrada()))
				productos = new HashMap<Long, Double>();
			
			// Añadimos el producto al Almacen indicado
			mensajeLog(">>>>>>>>>>> Cargando a Almacen/Bodega " + movimiento.getIdAlmacen().getIdentificador() + " ... ");
			existencias = new ArrayList<AlmacenProducto>();
			for (MovimientosDetalle detalle : detalles) {
				if (productos != null)
					productos.put(detalle.getIdProducto(), detalle.getCantidad());
				
				// Compruebo producto en almacen
				existencia = this.ifzAlmacenProducto.findAlmacenProducto(idAlmacenBodega, detalle.getIdProducto());
				if (existencia == null || existencia.getId() == null || existencia.getId() <= 0L) { // Producto nuevo en Almacen
					existencia = new AlmacenProducto();
					existencia.setIdAlmacen(movimiento.getIdAlmacen());
					existencia.setIdProducto(detalle.getIdProducto());
					existencia.setExistencia(0);
					existencia.setEstatus(0);
					existencia.setIdEmpresa(movimiento.getIdEmpresa());
					existencia.setCreadoPor(movimiento.getCreadoPor());
					existencia.setFechaCreacion(Calendar.getInstance().getTime());
				} 
				
				// Actualizo existencia
				existencia.setExistencia(existencia.getExistencia() + detalle.getCantidad());
				existencia.setModificadoPor(movimiento.getCreadoPor());
				existencia.setFechaModificacion(Calendar.getInstance().getTime());
				existencias.add(existencia);
				
				// Indico que fue procesado
				detalle.setEjecutado(1);
			}
			mensajeLog("****************************** Cargando a Almacen/Bodega ... OK");
			
			// Guardo los cambios
			mensajeLog("****************************** Guardando cambios ... ");
			if (existencias != null && ! existencias.isEmpty()) {
				this.ifzAlmacenProducto.setInfoSesion(this.infoSesion);
				this.ifzAlmacenProducto.saveOrUpdateList(existencias);
			}
			mensajeLog("****************************** Guardando cambios ... OK");

			if (productos != null && ! productos.isEmpty()) {
				mensajeLog("****************************** Guardando existencia en catalogo de Productos ... ");
				actualizaExistenciaCatalogoProducto(productos);
				mensajeLog("****************************** Guardando existencia en catalogo de Productos ... OK");
			}
			
			// Indico que se ejecuto la descarga de productos/material
			this.ifzMovimientosItems.setInfoSesion(this.infoSesion);
			this.ifzMovimientosItems.saveOrUpdateList(detalles);
		} catch (Exception e) {
			printLog("Ocurrio un problema al aumentar el INVENTARIO del Almacen/Bodega " + idAlmacenBodega, e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Disminuye el Inventario de los productos indicados
	 * @param movimiento 
	 * @param detalles Listados de productos
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private boolean descuentaInventario(AlmacenMovimientos movimiento, List<MovimientosDetalle> detalles) {
		HashMap<Long, Double> productos = null;
		List<AlmacenProducto> existencias = null;
		AlmacenProducto existencia = null;
		long idAlmacenBodega = 0L;
		
		try {
			// Recuperamos el almacen afectado
			idAlmacenBodega = movimiento.getIdAlmacen().getId();
			if (movimiento.getTipo() == TipoMovimientoInventario.SALIDA.ordinal() && "SO".equals(movimiento.getTipoEntrada()))
				productos = new HashMap<Long, Double>();
			
			// Descontamos el producto al Almacen indicado
			mensajeLog(">>>>>>>>>>> Descargando de Almacen/Bodega " + movimiento.getIdAlmacen().getIdentificador() + " ... ");
			existencias = new ArrayList<AlmacenProducto>();
			for (MovimientosDetalle detalle : detalles) {
				if (productos != null)
					productos.put(detalle.getIdProducto(), (detalle.getCantidad() < 0 ? detalle.getCantidad() : (detalle.getCantidad() * -1)));
				
				// Compruebo producto en almacen
				existencia = this.ifzAlmacenProducto.findAlmacenProducto(idAlmacenBodega, detalle.getIdProducto());
				if (existencia == null || existencia.getId() == null || existencia.getId() <= 0L) { 
					// Producto nuevo
					existencia = new AlmacenProducto();
					existencia.setIdAlmacen(movimiento.getIdAlmacen());
					existencia.setIdProducto(detalle.getIdProducto());
					existencia.setExistencia(detalle.getCantidad()); // le asigno la misma cantidad para que quede en cero
					existencia.setIdEmpresa(movimiento.getIdEmpresa());
					existencia.setEstatus(0);
					existencia.setCreadoPor(movimiento.getCreadoPor());
					existencia.setFechaCreacion(Calendar.getInstance().getTime());
				} 
				
				existencia.setExistencia(existencia.getExistencia() - detalle.getCantidad());
				existencia.setModificadoPor(movimiento.getCreadoPor());
				existencia.setFechaModificacion(Calendar.getInstance().getTime());
				existencias.add(existencia);
				
				// Indico que fue procesado
				detalle.setEjecutado(1);
			}
			mensajeLog("****************************** Descargando de Almacen/Bodega ... OK");
			
			// Guardo los cambios
			mensajeLog("****************************** Guardando cambios ... ");
			if (existencias != null && ! existencias.isEmpty()) {
				this.ifzAlmacenProducto.setInfoSesion(this.infoSesion);
				this.ifzAlmacenProducto.saveOrUpdateList(existencias);
			}
			mensajeLog("****************************** Guardando cambios ... OK");

			if (productos != null) {
				mensajeLog("****************************** Guardando existencia en catalogo de Productos ... ");
				actualizaExistenciaCatalogoProducto(productos);
				mensajeLog("****************************** Guardando existencia en catalogo de Productos ... OK");
			}
			
			// Indico que se ejecuto la descarga de productos/material
			this.ifzMovimientosItems.setInfoSesion(this.infoSesion);
			this.ifzMovimientosItems.saveOrUpdateList(detalles);
		} catch (Exception e) {
			printLog("Ocurrio un problema al descargar el INVENTARIO del Almacen/Bodega " + idAlmacenBodega, e);
			return false;
		}
		
		return true;
	}

	private void actualizaPrecios(Long idOrdenCompra, String ordenCompra, Long idMovimiento, HashMap<Long, List<Double>> precios) throws Exception {
		AlmacenMovimientos movimiento = null;
		List<MovimientosDetalle> detalles = null;
		List<Double> valores = null;
		// -----------------------------------------------
		List<AlmacenProducto> existencias = null;
		List<Long> listProductos = null;
		// -----------------------------------------------
		List<Producto> productos = null;
		Producto producto = null;
		double precioPesos = 0;
		double precio = 0;
		double tipoCambio = 0;
		long idMoneda = 0L;
		// -----------------------------------------------
		double precioGuardado = 0;
		double precioComparable = 0;

		try {
			mensajeLog(">>>>>>>>>>> BackOffice Productos: Actualizador de Precios *******************");
			mensajeLog("*********** idOrdenCompra : " + idOrdenCompra);
			mensajeLog("*********** OrdenCompra   : " + ordenCompra);
			mensajeLog("*********** Movimiento    : " + idMovimiento);
			mensajeLog("*********** Productos     : " + precios.size());
			if (precios.isEmpty()) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}

			listProductos = new ArrayList<Long>();
			for (Entry<Long, List<Double>> item : precios.entrySet()) {
				listProductos.add(item.getKey());
				producto = this.ifzProductos.findById(item.getKey());
				if (producto == null || producto.getId() == null || producto.getId() <= 0L) 
					continue;
				valores = item.getValue();
				if (valores == null || valores.isEmpty())
					continue;
				
				precio = valores.get(0); // precio unitario
				idMoneda = valores.get(1).longValue(); // moneda
				tipoCambio = valores.get(2); // tipo cambio
				precioPesos = precio;
				
				// Comparamos monedas para comparacion de precios
				if (this.infoSesion.getEmpresa().getMonedaId() == producto.getIdMoneda() && producto.getIdMoneda() != idMoneda) {
					precioPesos = precio * tipoCambio;
					precioComparable = precioPesos;
					precioGuardado = producto.getPrecioCompra();
				} else if (this.infoSesion.getEmpresa().getMonedaId() != producto.getIdMoneda() && producto.getIdMoneda() == idMoneda) {
					precioPesos = precio * tipoCambio;
					precioComparable = precio;
					precioGuardado = producto.getPrecioCompra();
				} else {
					precioComparable = precio;
					precioGuardado = producto.getPrecioCompra();
				}

				// Actualizamos precio si corresponde
				if (precioGuardado > precioComparable)
					continue;

				producto.setIdOrdenCompra(idOrdenCompra);
				producto.setOrdenCompra(ordenCompra);
				producto.setPrecioCompra(precio);
				producto.setPrecioCompraPesos(precioPesos);
				producto.setPrecioUnitario((precio * 1.1)); // 10%
				producto.setPrecioVenta((precio * 1.7)); // 70%
				producto.setTipoCambio(tipoCambio);
				
				productos = (productos != null ? productos : new ArrayList<Producto>());
				productos.add(producto);
			}
			
			if (productos == null || productos.isEmpty()) {
				topicSinAccion("Sin accion. No se actualizo ningun producto ");
				return;
			}
			
			// Guardamos precios de productos
			this.ifzProductos.setInfoSesion(this.infoSesion);
			this.ifzProductos.saveOrUpdateList(productos);
			
			// --------------------------------------------------------------------------------------------------------------
			 // Actualimos precios en MOVIMIENTO
			// --------------------------------------------------------------------------------------------------------------
			
			// Obtenemos productos(detalles) del movimiento de inventario
			detalles = this.ifzMovimientosItems.findAll(idMovimiento);
			if (detalles == null || detalles.isEmpty())
				return;
			for (MovimientosDetalle detalle : detalles) {
				if (precios.containsKey(detalle.getIdProducto())) {
					precio = precios.get(detalle.getIdProducto()).get(0); // precio unitario
					idMoneda = precios.get(detalle.getIdProducto()).get(1).longValue(); // moneda
					tipoCambio = precios.get(detalle.getIdProducto()).get(2); // tipo cambio
					
					precio = precio * (this.infoSesion.getEmpresa().getMonedaId() != idMoneda ? tipoCambio : 1); // conversion
					detalle.setPrecioUnitario(precio);
				}
			}
			
			// Guardamos precios de movimiento
			this.ifzMovimientosItems.setInfoSesion(this.infoSesion);
			this.ifzMovimientosItems.saveOrUpdateList(detalles);
			
			// --------------------------------------------------------------------------------------------------------------
			 // Actualimos precios en Almacen (Inventario)
			// --------------------------------------------------------------------------------------------------------------
			
			movimiento = this.ifzMovimientos.findById(idMovimiento);
			if (movimiento != null && movimiento.getId() != null && movimiento.getId() > 0L) {
				existencias = this.ifzAlmacenProducto.encontrarExistencia(movimiento.getIdAlmacen().getId(), listProductos);
				if (existencias != null && ! existencias.isEmpty()) {
					for (AlmacenProducto existencia : existencias) {
						if (precios.containsKey(existencia.getIdProducto())) {
							precio = precios.get(existencia.getIdProducto()).get(0); // precio unitario
							idMoneda = precios.get(existencia.getIdProducto()).get(1).longValue(); // moneda
							tipoCambio = precios.get(existencia.getIdProducto()).get(2); // tipo cambio
							
							precio = precio * (this.infoSesion.getEmpresa().getMonedaId() != idMoneda ? tipoCambio : 1); // conversion
							existencia.setPrecioUnitario(precio);
						}
					}
					
					// Guardamos precios de movimiento
					this.ifzAlmacenProducto.setInfoSesion(this.infoSesion);
					this.ifzAlmacenProducto.saveOrUpdateList(existencias);
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el BackOffice Productos: Actualizador de Precios", e);
			throw e;
		}
	}
	
	/**
	 * Actualiza la existencia del producto en el catalogo de Productos
	 * @param existencias
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void actualizaExistenciaCatalogoProducto(HashMap<Long, Double> existencias) {
		List<Producto> productos = null;
		List<Long> idProductos = null;
		double existencia = 0;
		
		try {
			if (existencias == null || existencias.isEmpty())
				return;
			
			idProductos = new ArrayList<Long>();
            for (Long key : existencias.keySet())
            	idProductos.add(key);
			productos = this.ifzProductos.findList(idProductos);
			if (productos == null || productos.isEmpty())
				return;
			
			for (Producto producto : productos) {
				if (! existencias.containsKey(producto.getId()))
					continue;
				existencia = producto.getExistencia();
				existencia += existencias.get(producto.getId());
				if (existencia <= 0)
					existencia = 0;
				producto.setExistencia(existencia);
			}
			
			this.ifzProductos.saveOrUpdateList(productos);
		} catch (Exception e) {
			log.error("Ocurrio un problema al actualizar la existencia en el catalogo de productos", e);
		}
	}
	
	/**
	 * Actualiza el estatus de las Solicitudes a Bodega involucradas con el Traspaso indicado
	 * @param traspaso
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void actualizaEstatusSolicitudBodega(AlmacenTraspaso traspaso) {
		List<AlmacenTraspaso> solicitudes = null;
		long idTraspaso = 0L;
		
		try {
			if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
				return;
			if (traspaso.getRecibido() != 1)
				return;
			
			idTraspaso = traspaso.getId();
			solicitudes = this.ifzTraspaso.findByProperty("idTraspaso.id", idTraspaso);
			if (solicitudes == null || solicitudes.isEmpty())
				return;
			
			log.info("Actualizando estatus de Solicitudes a Bodega ... ");
			for (AlmacenTraspaso solicitud : solicitudes) 
				solicitud.setCompleto(1);
			this.ifzTraspaso.saveOrUpdateList(solicitudes);
			log.info("Actualizando estatus de Solicitudes a Bodega ... OK");
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar atualizar los estatus de las Solicitudes a Bodega del Traspaso " + idTraspaso, e);
		}
	}

	/**
	 * Afectacion en inventario (aumento de existencia) por la entrada de una orden de compra y
	 * lanzamiento de evento de BACKORDER Orden de Compra
	 * @param movimiento
	 * @param items
	 */
	private void generaRegistrosEntrada(AlmacenMovimientos movimiento, List<MovimientosDetalle> items) throws Exception {
		Gson gson = new Gson();
		MensajeTopic msgTopic = null;
		String comando = "";
		HashMap<Long, Double> atributos = null;
		Long idOrdenCompra = 0L;
		
		try {
			movimiento = (movimiento != null ? movimiento : new AlmacenMovimientos());
			idOrdenCompra = (movimiento.getIdOrdenCompra() > 0L ? movimiento.getIdOrdenCompra() : 0L);
			items = (items != null ? items : new ArrayList<MovimientosDetalle>());
			mensajeLog(">>>>>>>>>>> Afectacion Existencia en Inventarios (BackOrder COMPRAS) *********** ");
			mensajeLog("*********** Movimiento   : " + movimiento.getId());
			mensajeLog("*********** Orden Compra : " + idOrdenCompra);
			mensajeLog("*********** Productos    : " + items.size());
			if (movimiento.getId() <= 0L || items.isEmpty()) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			// Añadimos los productos al Almacen/Bodega
			mensajeLog(" ---> Procesando ... ");
			aumentaInventario(movimiento, items);

			// Genero listado de suministro
			mensajeLog(" ---> Generando listado de suministro ... ");
			atributos = new HashMap<Long, Double>();
			for (MovimientosDetalle item : items) 
				atributos.put(item.getIdProducto(), item.getCantidad());
			
			// Disparo BackOrder Orden de Compra
			mensajeLog(" ---> Disparo BackOrder Orden de Compra ... ");
			msgTopic = new MensajeTopic(TopicEventosCompras.ORDEN_SUMINISTRO, idOrdenCompra.toString(), movimiento.getId().toString(), gson.toJson(atributos), this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			printLog("Ocurrio un problema al actualizar el BACKORDER COMPRAS\nEvento\n" + comando, e);
			throw e;
		}
	}
	
	/**
	 * Generamos los registros necesarios para soportar la Entrada indicada (Entradas a Almacen, Traspasos Almacen-Almacen, Traspaso Almacen-Bodega, etc)
	 * @param movimiento
	 * @param productos
	 */
	private void generaRegistrosOC(AlmacenMovimientos movimiento, HashMap<Long, Double> productos) {
		Almacen almacen = null;
		Almacen bodega = null;
		// ---------------------------------
		AlmacenMovimientos entrada = null;
		AlmacenMovimientos salida = null;
		AlmacenTraspaso traspaso = null;
		
		try {
			almacen = almacenBaseByObra(movimiento.getIdObra());
			bodega  = movimiento.getIdAlmacen();
			
			// Generamos la Entrada al Almacen principal asignado a la Bodega del movimiento original (SIN AFECTAR INVENTARIOS)
			entrada = generaEntrada(movimiento, productos, almacen);
			if (entrada == null || entrada.getId() == null || entrada.getId() <= 0L)
				throw new Exception("No se pudo crear la Entrada a Almacen con la Orden de Compra indicada");
			
			// Generamos el Traspaso del Almacen principal hacia la Bodega del movimiento original (SIN AFECTAR INVENTARIOS)
			traspaso = generaTraspaso(entrada, productos, TipoTraspaso.TRASPASO, almacen, bodega);
			if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
				throw new Exception("No se pudo crear el Traspaso de Almacen a Bodega");
			
			// Generamos el movimiento de salida por traspaso correspondiente al paso anterior (SIN AFECTAR INVENTARIOS)
			salida = generaSalida(traspaso, productos);
			if (salida == null || salida.getId() == null || salida.getId() <= 0L)
				throw new Exception("No se pudo crear la Salida por Traspaso");
			
			// Generamos la entrada por traspaso en la Bodega del movimiento original (SIN AFECTAR INVENTARIOS)
			entrada = generaEntrada(traspaso, productos);
			if (entrada == null || entrada.getId() == null || entrada.getId() <= 0L)
				throw new Exception("No se pudo crear la Entrada por Traspaso a la Bodega indicada");
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar los movimiento internos para el soporte de Entrada por Orden de Compra de Material a BOdega", e);
		}
	}

	private void generaRegistrosTR(AlmacenMovimientos movimiento, HashMap<Long, Double> productos) {
		AlmacenTraspaso traspasoOriginal = null;
		// ---------------------------------
		Almacen almacenBase = null;
		Almacen almacenAuxiliar = null;
		// ---------------------------------
		AlmacenMovimientos entrada = null;
		AlmacenMovimientos salida = null;
		AlmacenTraspaso traspaso = null;
		
		try {
			traspasoOriginal = this.ifzTraspaso.findById(movimiento.getIdTraspaso());
			if (traspasoOriginal == null || traspasoOriginal.getId() == null || traspasoOriginal.getId() <= 0L)
				throw new Exception("No se pudo recuperar el Traspaso del Movimiento indicado");
			
			// Traspaso de Almacen a Almacen ... nada que hacer :)
			if ("AL".equals(traspasoOriginal.getIdAlmacenOrigen().getTipoAlmacen()) && "AL".equals(traspasoOriginal.getIdAlmacenDestino().getTipoAlmacen()))
				return;

			// Traspaso de Almacen a Bodega | Traspaso entre Almacenes + Traspaso a Bodega destino 
			if ("AL".equals(traspasoOriginal.getIdAlmacenOrigen().getTipoAlmacen()) && "BO".equals(traspasoOriginal.getIdAlmacenDestino().getTipoAlmacen())) {
				// Si la Bodega destino tiene asignado el mismo Almacen de origen como principal... nada que hacer :)
				almacenBase = almacenBaseByObra(movimiento.getIdObra());
				if (traspasoOriginal.getIdAlmacenOrigen().getId().longValue() == almacenBase.getId().longValue())
					return;
				
				// Generamos Traspaso de Almacen origen del Traspaso hacia el Almacen principal asignado a la Bodega. (SIN AFECTAR INVENTARIOS)
				// ----------------------------------------------------------------------------------------------------------------------------
				traspaso = generaTraspaso(movimiento, productos, TipoTraspaso.TRASPASO, traspasoOriginal.getIdAlmacenOrigen(), almacenBase);
				if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
					throw new Exception("No se pudo crear el Traspaso de Almacen origen a Almacen principal asignado a la Bodega destino");
				
				// Generamos el movimiento de salida por traspaso correspondiente al paso anterior (SIN AFECTAR INVENTARIOS)
				salida = generaSalida(traspaso, productos);
				if (salida == null || salida.getId() == null || salida.getId() <= 0L)
					throw new Exception("No se pudo crear la Salida por Traspaso (Almacen origen a Almacen principal asignado a la Bodega destino)");
				
				// Generamos la entrada por traspaso en el Almacen principal asignado a la Bodega (SIN AFECTAR INVENTARIOS)
				// ----------------------------------------------------------------------------------------------------------------------------
				entrada = generaEntrada(traspaso, productos);
				if (entrada == null || entrada.getId() == null || entrada.getId() <= 0L)
					throw new Exception("No se pudo crear la Entrada por Traspaso al Almacen principal asignado a la Bodega Destino");
				
				// Generamos Traspaso del Almacen principal asignado a la Bodega hacia la Bodega destino original.
				// ----------------------------------------------------------------------------------------------------------------------------
				traspaso = generaTraspaso(movimiento, productos, TipoTraspaso.TRASPASO, almacenBase, traspasoOriginal.getIdAlmacenDestino());
				if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
					throw new Exception("No se pudo crear el Traspaso de Almacen a Bodega destino original");
				
				// Generamos el movimiento de salida por traspaso correspondiente al paso anterior (SIN AFECTAR INVENTARIOS)
				salida = generaSalida(traspaso, productos);
				if (salida == null || salida.getId() == null || salida.getId() <= 0L)
					throw new Exception("No se pudo crear la Salida por Traspaso");
			}
			
			// Traspaso de Bodega a Bodega | Devolucion a Almacen + [Traspaso entre Almacenes] + Traspaso a Bodega destino
			if ("BO".equals(traspasoOriginal.getIdAlmacenOrigen().getTipoAlmacen()) && "BO".equals(traspasoOriginal.getIdAlmacenDestino().getTipoAlmacen())) {
				almacenBase = almacenBaseByObra(movimiento.getIdObra());
				almacenAuxiliar = almacenAuxiliarByObra(movimiento.getIdObra());
				
				if (traspasoOriginal.getIdAlmacenOrigen().getIdSucursal() == traspasoOriginal.getIdAlmacenDestino().getIdSucursal()) {
					// Generamos Devolucion de Bodega origen al Almacen principal asignado a la Bodega. (SIN AFECTAR INVENTARIOS)
					// ----------------------------------------------------------------------------------------------------------------------------
					traspaso = generaTraspaso(movimiento, productos, TipoTraspaso.DEVOLUCION, traspasoOriginal.getIdAlmacenOrigen(), almacenBase);
					if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
						throw new Exception("No se pudo crear la Devolucion de la Bodega origen al Almacen principal asignado a la Bodega origen");
					
					// Generamos el movimiento de salida por traspaso correspondiente al paso anterior (SIN AFECTAR INVENTARIOS)
					salida = generaSalida(traspaso, productos);
					if (salida == null || salida.getId() == null || salida.getId() <= 0L)
						throw new Exception("No se pudo crear la Salida por Devolucion (Bodega origen al Almacen principal asignado)");
	
					// Generamos la entrada por Devolucion en el Almacen principal asignado a la Bodega origen (SIN AFECTAR INVENTARIOS)
					// ----------------------------------------------------------------------------------------------------------------------------
					entrada = generaEntrada(traspaso, productos);
					if (entrada == null || entrada.getId() == null || entrada.getId() <= 0L)
						throw new Exception("No se pudo crear la Entrada por Devolucion al Almacen principal asignado a la Bodega origen");
					
					// Generamos Traspaso del Almacen principal asignado a la Bodega hacia el Almacen destino original.
					// ----------------------------------------------------------------------------------------------------------------------------
					traspaso = generaTraspaso(movimiento, productos, TipoTraspaso.TRASPASO, almacenBase, traspasoOriginal.getIdAlmacenDestino());
					if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
						throw new Exception("No se pudo crear el Traspaso de Almacen a Bodega destino original");
					
					// Generamos el movimiento de salida por traspaso correspondiente al paso anterior (SIN AFECTAR INVENTARIOS)
					salida = generaSalida(traspaso, productos);
					if (salida == null || salida.getId() == null || salida.getId() <= 0L)
						throw new Exception("No se pudo crear la Salida por Traspaso");
					return;
				}
				
				// Generamos Devolucion de Bodega origen a Almacen. (SIN AFECTAR INVENTARIOS)
				// ----------------------------------------------------------------------------------------------------------------------------
				traspaso = generaTraspaso(movimiento, productos, TipoTraspaso.DEVOLUCION, traspasoOriginal.getIdAlmacenOrigen(), almacenAuxiliar);
				if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
					throw new Exception("No se pudo crear la Devolucion de la Bodega origen a su Almacen principal");

				// Generamos el movimiento de salida por traspaso correspondiente al paso anterior (SIN AFECTAR INVENTARIOS)
				salida = generaSalida(traspaso, productos);
				if (salida == null || salida.getId() == null || salida.getId() <= 0L)
					throw new Exception("No se pudo crear la Salida por Devolucion (Bodega origen)");

				// Generamos la entrada por Devolucion al Almacen principal asignado a la Bodega origen (SIN AFECTAR INVENTARIOS)
				entrada = generaEntrada(traspaso, productos);
				if (entrada == null || entrada.getId() == null || entrada.getId() <= 0L)
					throw new Exception("No se pudo crear la Entrada por Devolucion al Almacen principal (Bodega origen)");

				// Generamos Traspaso de Almacen origen a Almacen destino. (SIN AFECTAR INVENTARIOS)
				// ----------------------------------------------------------------------------------------------------------------------------
				traspaso = generaTraspaso(movimiento, productos, TipoTraspaso.TRASPASO, almacenAuxiliar, almacenBase);
				if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
					throw new Exception("No se pudo crear el Traspaso entre Almacenes");

				// Generamos el movimiento de salida por traspaso correspondiente al paso anterior (SIN AFECTAR INVENTARIOS)
				salida = generaSalida(traspaso, productos);
				if (salida == null || salida.getId() == null || salida.getId() <= 0L)
					throw new Exception("No se pudo crear la Salida por Traspaso (Almacenes)");

				// Generamos la entrada por Traspaso en el Almacen principal asignado a la Bodega destino (SIN AFECTAR INVENTARIOS)
				entrada = generaEntrada(traspaso, productos);
				if (entrada == null || entrada.getId() == null || entrada.getId() <= 0L)
					throw new Exception("No se pudo crear la Entrada por Traspaso (Almacenes)");

				// Generamos Traspaso de Almacen a Bodega destino. (SIN AFECTAR INVENTARIOS)
				// ----------------------------------------------------------------------------------------------------------------------------
				traspaso = generaTraspaso(movimiento, productos, TipoTraspaso.TRASPASO, almacenBase, traspasoOriginal.getIdAlmacenDestino());
				if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
					throw new Exception("No se pudo crear el Traspaso del Almacen principal a la Bodega destino");

				// Generamos el movimiento de salida por traspaso correspondiente al paso anterior (SIN AFECTAR INVENTARIOS)
				salida = generaSalida(traspaso, productos);
				if (salida == null || salida.getId() == null || salida.getId() <= 0L)
					throw new Exception("No se pudo crear la Salida por Traspaso (Bodega destino)");
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar los movimiento internos para el soporte de Entrada por Traspaso de Material a Almacen/Bodega", e);
		}
	}

	private void generaRegistrosDE(AlmacenMovimientos movimiento, HashMap<Long, Double> productos) {
		AlmacenTraspaso traspasoOriginal = null;
		// -------------------------------------
		Almacen almacenBase = null;
		AlmacenMovimientos entrada = null;
		AlmacenMovimientos salida = null;
		AlmacenTraspaso traspaso = null;
		
		try {
			traspasoOriginal = this.ifzTraspaso.findById(movimiento.getIdTraspaso());
			if (traspasoOriginal == null || traspasoOriginal.getId() == null || traspasoOriginal.getId() <= 0L)
				throw new Exception("No se pudo recuperar el Traspaso del Movimiento indicado");
			
			// Si la Bodega destino tiene asignado el mismo Almacen de origen como principal... nada que hacer :)
			almacenBase = almacenBaseByObra(movimiento.getIdObra());
			if (traspasoOriginal.getIdAlmacenDestino().getId().longValue() == almacenBase.getId().longValue())
				return;
			
			// Generamos Devolucion de Bodega origen al Almacen principal asignado a la Bodega. (SIN AFECTAR INVENTARIOS)
			// ----------------------------------------------------------------------------------------------------------------------------
			traspaso = generaTraspaso(movimiento, productos, TipoTraspaso.DEVOLUCION, traspasoOriginal.getIdAlmacenOrigen(), almacenBase);
			if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
				throw new Exception("No se pudo crear la Devolucion de la Bodega origen al Almacen principal asignado a la Bodega origen");
			
			// Generamos el movimiento de salida por traspaso correspondiente al paso anterior (SIN AFECTAR INVENTARIOS)
			salida = generaSalida(traspaso, productos);
			if (salida == null || salida.getId() == null || salida.getId() <= 0L)
				throw new Exception("No se pudo crear la Salida por Devolucion (Bodega origen al Almacen principal asignado)");

			// Generamos la entrada por Devolucion en el Almacen principal asignado a la Bodega origen (SIN AFECTAR INVENTARIOS)
			// ----------------------------------------------------------------------------------------------------------------------------
			entrada = generaEntrada(traspaso, productos);
			if (entrada == null || entrada.getId() == null || entrada.getId() <= 0L)
				throw new Exception("No se pudo crear la Entrada por Devolucion al Almacen principal asignado a la Bodega origen");
			
			// Generamos Traspaso del Almacen principal asignado a la Bodega hacia el Almacen destino original.
			// ----------------------------------------------------------------------------------------------------------------------------
			traspaso = generaTraspaso(movimiento, productos, TipoTraspaso.TRASPASO, almacenBase, traspasoOriginal.getIdAlmacenDestino());
			if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L)
				throw new Exception("No se pudo crear el Traspaso de Almacen a Bodega destino original");
			
			// Generamos el movimiento de salida por traspaso correspondiente al paso anterior (SIN AFECTAR INVENTARIOS)
			salida = generaSalida(traspaso, productos);
			if (salida == null || salida.getId() == null || salida.getId() <= 0L)
				throw new Exception("No se pudo crear la Salida por Traspaso");
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar los movimiento internos para el soporte de Entrada por Devolucion de Material a Almacen", e);
		}
	}
	
	private AlmacenMovimientos generaEntrada(AlmacenMovimientos movimiento, HashMap<Long, Double> productos, Almacen almacenBodega) {
		AlmacenMovimientos entrada = null;
		List<MovimientosDetalle> detalles = null;
		MovimientosDetalle detalle = null;
		long idOwner = 0L;
		
		try {
			idOwner = movimiento.getOwner();
			if (idOwner <= 0L)
				idOwner = movimiento.getId();
			
			// Genero movimiento de ENTRADA
			entrada = new AlmacenMovimientos();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(entrada, movimiento);
			entrada.setId(null);
			entrada.setTipo(TipoMovimientoInventario.ENTRADA.ordinal());
			entrada.setTipoEntrada(TipoMovimientoReferencia.ORDEN_COMPRA.toString());
			entrada.setIdAlmacen(almacenBodega);
			entrada.setIdEmpresa(movimiento.getIdEmpresa());
			entrada.setOwner(idOwner);
			entrada.setSistema(1);
			entrada.setEstatus(0);
			entrada.setFechaCreacion(Calendar.getInstance().getTime());
			entrada.setFechaModificacion(Calendar.getInstance().getTime());
			
			// Genero detalles de salida
			log.info("Movimiento de Salida guardado. Guardando detalle de movimiento ...");
			detalles = new ArrayList<MovimientosDetalle>();
			for (Entry<Long, Double> producto : productos.entrySet()) {
				detalle = new MovimientosDetalle();
				detalle.setIdProducto(producto.getKey());
				detalle.setCantidad(producto.getValue());
				detalle.setCantidadSolicitada(producto.getValue());
				detalle.setEstatus(0);
				detalle.setCreadoPor(movimiento.getCreadoPor());
				detalle.setFechaCreacion(Calendar.getInstance().getTime());
				detalle.setModificadoPor(movimiento.getModificadoPor());
				detalle.setFechaModificacion(Calendar.getInstance().getTime());
				detalles.add(detalle);
			}

			// Guardo entrada (movimiento) y detalles
			log.info("Guardando Movimiento y detalles ...");
			this.ifzMovimientos.setInfoSesion(this.infoSesion);
			entrada.setId(this.ifzMovimientos.save(entrada));
			for (MovimientosDetalle item : detalles)
				item.setIdAlmacenMovimiento(entrada.getId());
			this.ifzMovimientosItems.setInfoSesion(this.infoSesion);
			this.ifzMovimientosItems.saveOrUpdateList(detalles);
			log.info("Detalles de Movimiento guardados. Proceso finalizado.");
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar el movimiento de ENTRADA", e);
			entrada = null;
		}
		
		return entrada;
	}

	private AlmacenMovimientos generaEntrada(AlmacenTraspaso traspaso, HashMap<Long, Double> productos) {
		AlmacenMovimientos entrada = null;
		List<MovimientosDetalle> detalles = null;
		MovimientosDetalle detalle = null;
		long idOwner = 0L;
		
		try {
			idOwner = traspaso.getOwner();
			if (idOwner <= 0L)
				idOwner = traspaso.getId();
			
			// Genero movimiento de ENTRADA
			entrada = new AlmacenMovimientos();
			entrada.setFecha(traspaso.getFecha());
			entrada.setTipo(0);
			entrada.setTipoEntrada(TipoMovimientoReferencia.TRASPASO.toString());
			entrada.setIdAlmacen(traspaso.getIdAlmacenDestino());
			entrada.setRecibe(traspaso.getRecibe());
			entrada.setRecibeNombre(traspaso.getRecibeNombre());
			entrada.setEntrega(traspaso.getEntrega());
			entrada.setEntregaNombre(traspaso.getEntregaNombre());
			entrada.setIdObra(traspaso.getIdObra());
			entrada.setNombreObra(traspaso.getNombreObra());
			entrada.setIdTraspaso(traspaso.getId());
			entrada.setIdEmpresa(traspaso.getIdEmpresa());
			entrada.setOwner(idOwner);
			entrada.setSistema(1);
			entrada.setEstatus(0);
			
			// Genero detalles de salida
			log.info("Movimiento de Salida guardado. Guardando detalle de movimiento ...");
			detalles = new ArrayList<MovimientosDetalle>();
			for (Entry<Long, Double> producto : productos.entrySet()) {
				detalle = new MovimientosDetalle();
				detalle.setIdProducto(producto.getKey());
				detalle.setCantidad(producto.getValue());
				detalle.setCantidadSolicitada(producto.getValue());
				detalle.setEstatus(0);
				detalle.setCreadoPor(entrada.getCreadoPor());
				detalle.setFechaCreacion(Calendar.getInstance().getTime());
				detalle.setModificadoPor(entrada.getModificadoPor());
				detalle.setFechaModificacion(Calendar.getInstance().getTime());
				detalles.add(detalle);
			}

			// Guardo entrada (movimiento) y detalles
			log.info("Guardando Movimiento y detalles ...");
			this.ifzMovimientos.setInfoSesion(this.infoSesion);
			entrada.setId(this.ifzMovimientos.save(entrada));
			for (MovimientosDetalle item : detalles)
				item.setIdAlmacenMovimiento(entrada.getId());
			this.ifzMovimientosItems.setInfoSesion(this.infoSesion);
			detalles = this.ifzMovimientosItems.saveOrUpdateList(detalles);
			log.info("Detalles de Movimiento guardados. Proceso finalizado.");
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar el movimiento de ENTRADA", e);
			entrada = null;
		}
		
		return entrada;
	}

	private AlmacenMovimientos generaSalida(AlmacenTraspaso traspaso, HashMap<Long, Double> productos) {
		AlmacenMovimientos salida = null;
		List<MovimientosDetalle> detalles = null;
		MovimientosDetalle detalle = null;
		long idOwner = 0L;
		
		try {
			idOwner = traspaso.getOwner();
			if (idOwner <= 0L)
				idOwner = traspaso.getId();
			
			// Genero movimiento de SALIDA
			salida = new AlmacenMovimientos();
			salida.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
			salida.setTipoEntrada(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString());
			salida.setIdOrdenCompra(0L);
			salida.setIdTraspaso(traspaso.getId());
			salida.setIdAlmacen(traspaso.getIdAlmacenOrigen());
			salida.setFecha(traspaso.getFecha());
			salida.setEntrega(traspaso.getEntrega());
			salida.setEntregaNombre(traspaso.getEntregaNombre());
			salida.setRecibe(traspaso.getRecibe());
			salida.setRecibeNombre(traspaso.getRecibeNombre());
			salida.setIdEmpresa(traspaso.getIdEmpresa());
			salida.setOwner(idOwner);
			salida.setSistema(1);
			salida.setEstatus(0);
			salida.setCreadoPor(traspaso.getCreadoPor());
			salida.setFechaCreacion(traspaso.getFechaCreacion());
			salida.setModificadoPor(traspaso.getModificadoPor());
			salida.setFechaModificacion(traspaso.getFechaModificacion());
			
			// Genero detalles de salida
			log.info("Movimiento de Salida guardado. Guardando detalle de movimiento ...");
			detalles = new ArrayList<MovimientosDetalle>();
			for (Entry<Long, Double> producto : productos.entrySet()) {
				detalle = new MovimientosDetalle();
				detalle.setIdProducto(producto.getKey());
				detalle.setCantidad(producto.getValue());
				detalle.setCantidadSolicitada(producto.getValue());
				detalle.setEstatus(0);
				detalle.setCreadoPor(salida.getCreadoPor());
				detalle.setFechaCreacion(Calendar.getInstance().getTime());
				detalle.setModificadoPor(salida.getModificadoPor());
				detalle.setFechaModificacion(Calendar.getInstance().getTime());
				detalles.add(detalle);
			}

			// Guardo entrada (movimiento) y detalles
			log.info("Guardando Movimiento y detalles ...");
			this.ifzMovimientos.setInfoSesion(this.infoSesion);
			salida.setId(this.ifzMovimientos.save(salida));
			for (MovimientosDetalle item : detalles)
				item.setIdAlmacenMovimiento(salida.getId());
			this.ifzMovimientosItems.setInfoSesion(this.infoSesion);
			this.ifzMovimientosItems.saveOrUpdateList(detalles);
			log.info("Detalles de Movimiento guardados. Proceso finalizado.");
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar la SALIDA por TRASPASO", e);
			salida = null;
		}
		
		return salida;
	}
	
	private AlmacenTraspaso generaTraspaso(AlmacenMovimientos movimiento, HashMap<Long, Double> productos, TipoTraspaso tipoTraspaso, Almacen almacen, Almacen bodega) {
		AlmacenTraspaso traspaso = null;
		List<TraspasoDetalle> detalles = null;
		TraspasoDetalle detalle = null;
		long idOwner = 0L;
		
		try {
			idOwner = movimiento.getOwner();
			if (idOwner <= 0L)
				idOwner = movimiento.getId();
			
			// Genera Traspaso
			traspaso = new AlmacenTraspaso();
			traspaso.setTipo(tipoTraspaso.ordinal());
			traspaso.setFecha(movimiento.getFecha());
			traspaso.setIdAlmacenOrigen(almacen);
			traspaso.setEntrega(almacen.getIdEncargado());
			traspaso.setEntregaNombre(almacen.getNombreEncargado());
			traspaso.setIdObra(movimiento.getIdObra());
			traspaso.setNombreObra(movimiento.getNombreObra());
			traspaso.setIdAlmacenDestino(bodega);
			traspaso.setRecibe(bodega.getIdEncargado());
			traspaso.setRecibeNombre(bodega.getNombreEncargado());
			traspaso.setIdEmpresa(movimiento.getIdEmpresa());
			traspaso.setOwner(idOwner);
			traspaso.setCompleto(1);
			traspaso.setSistema(1);
			traspaso.setEstatus(0);
			traspaso.setCreadoPor(movimiento.getCreadoPor());
			traspaso.setFechaCreacion(Calendar.getInstance().getTime());
			traspaso.setModificadoPor(movimiento.getModificadoPor());
			traspaso.setFechaModificacion(Calendar.getInstance().getTime());
			traspaso.setRecibido(1);
			traspaso.setRecibidoPor(movimiento.getCreadoPor());
			traspaso.setFechaRecibido(Calendar.getInstance().getTime());
			
			// Genero detalles de salida
			log.info("Movimiento de Salida guardado. Guardando detalle de movimiento ...");
			detalles = new ArrayList<TraspasoDetalle>();
			for (Entry<Long, Double> producto : productos.entrySet()) {
				detalle = new TraspasoDetalle();
				detalle.setIdProducto(producto.getKey());
				detalle.setCantidad(producto.getValue());
				detalle.setCantidadRecibida(producto.getValue());
				detalle.setEstatus(2); // Completa
				detalle.setCreadoPor(traspaso.getCreadoPor());
				detalle.setFechaCreacion(Calendar.getInstance().getTime());
				detalle.setModificadoPor(traspaso.getModificadoPor());
				detalle.setFechaModificacion(Calendar.getInstance().getTime());
				detalles.add(detalle);
			}

			// Guardo entrada (movimiento) y detalles
			log.info("Guardando Traspaso y detalles ...");
			this.ifzMovimientos.setInfoSesion(this.infoSesion);
			traspaso.setId(this.ifzTraspaso.save(traspaso));
			for (TraspasoDetalle item : detalles)
				item.setIdAlmacenTraspaso(traspaso.getId());
			this.ifzMovimientosItems.setInfoSesion(this.infoSesion);
			this.ifzTraspasoItems.saveOrUpdateList(detalles);
			log.info("Detalles de Traspaso guardados. Proceso finalizado.");
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar el TRASPASO", e);
			traspaso = null;
		}
		
		return traspaso;
	}

	@SuppressWarnings("unchecked")
	private AlmacenMovimientos comprobarMovimientoSalida(Long idTraspaso) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long resultado = 0;
		
		try {
			if (idTraspaso == null || idTraspaso <= 0L)
				return null;
			queryString = "select id from almacen_movimientos where tipo = 1 and id_traspaso = :idTraspaso order by id desc ";
			queryString = queryString.replace(":idTraspaso", idTraspaso.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = ((BigDecimal) rows.get(0)).longValue();
			return this.ifzMovimientos.findById(resultado);
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Movimiento de SALIDA para la Solicitud: " + idTraspaso, e);
		}

		return null;
	}

	private String valueToString(Object value) {
		String resultado = "";
		
		if (value != null) {
			resultado = value.toString();
			resultado = ("".equals(resultado.trim()) ? "" : resultado.trim());
		}
		
		return resultado;
	}
	
	private long valueToLong(Object value) {
		String strValue = valueToString(value);
		strValue = (! "".equals(strValue.trim()) ? strValue.trim() : "0");
		return Long.valueOf(strValue);
	}

	private boolean valueToBoolean(Object value) {
		return (valueToLong(value) > 0L);
	}

	private int valueToInteger(Object value) {
		String strValue = valueToString(value);
		strValue = (! "".equals(strValue.trim()) ? strValue.trim() : "0");
		return Integer.valueOf(strValue);
	}

	private void notificaComprador(long idUsuario, AlmacenTraspaso pojoTraspaso, List<TraspasoDetalle> detalles) {
		// do nothing
	}
	
	private void notificaEncargadoAlmacen(long idTraspaso) {
		// do nothing
	}
	
	private void notificaEncargadoBodega(long idTraspaso) {
		// do nothing
	}
	
	private void eventoSuministroExplosionInsumos(Long idObra, HashMap<Long, Double> items) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idObra == null)
				idObra = 0L;
			if (items == null || items.isEmpty())
				return;
			
			gson = new Gson();
			target = idObra.toString();
			atributos = gson.toJson(items);
			
			msgTopic = new MensajeTopic(TopicEventosGP.INSUMOS_SUMINISTRO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-RQ\n\n" + comando + "\n\n", e);
		}
	}

	private void eventoSuministroCotizacion(Long idCotizacion, HashMap<Long, Double> items) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idCotizacion == null)
				idCotizacion = 0L;
			if (items == null || items.isEmpty())
				return;
			
			gson = new Gson();
			target = idCotizacion.toString();
			atributos = gson.toJson(items);
			
			msgTopic = new MensajeTopic(TopicEventosCompras.COTIZACION_SUMINISTRO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-RQ\n\n" + comando + "\n\n", e);
		}
	}
	
	private void eventoSuministroRequisicion(Long idRequisicion, HashMap<Long, Double> items) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idRequisicion == null)
				idRequisicion = 0L;
			if (items == null || items.isEmpty())
				return;
			
			gson = new Gson();
			target = idRequisicion.toString();
			atributos = gson.toJson(items);
			
			msgTopic = new MensajeTopic(TopicEventosCompras.REQUISICION_SUMINISTRO, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-RQ\n\n" + comando + "\n\n", e);
		}
	}

	private void eventoCantidadCotizacion(Long idCotizacion, HashMap<Long, Double> productos) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (idCotizacion == null)
				idCotizacion = 0L;
			if (productos == null || productos.isEmpty())
				return;
			
			gson = new Gson();
			target = idCotizacion.toString();
			atributos = gson.toJson(productos);
			
			msgTopic = new MensajeTopic(TopicEventosCompras.COTIZACION_CANTIDAD, target, referencia, atributos, this.infoSesion);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:" + TopicEventosCompras.COTIZACION_CANTIDAD.toString() + "\n\n" + comando + "\n\n", e);
		}
	}

	@SuppressWarnings("unchecked")
	private String obtenerObra(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		String resultado = "";
		
		try {
			if (idObra == null || idObra <= 0L)
				return "";
			queryString += "select nombre from obra where id_obra = :idObra ";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = rows.get(0).toString();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el nombre de la Obra: " + idObra, e);
			resultado = "";
		}
		
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	private long obtenerIdResponsable(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long resultado = 0;
		
		try {
			if (idObra == null || idObra <= 0L)
				return 0L;
			queryString += "select id_responsable from obra where id_obra = :idObra ";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = ((BigDecimal) rows.get(0)).longValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el idResponsable de la Obra: " + idObra, e);
			resultado = 0;
		}
		
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	private String obtenerResponsable(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		String resultado = "";
		
		try {
			if (idObra == null || idObra <= 0L)
				return "";
			queryString += "select nombre_responsable from obra where id_obra = :idObra ";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = rows.get(0).toString();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el nombre del idResponsable de la Obra: " + idObra, e);
			resultado = "";
		}
		
		return resultado;
	}

	@SuppressWarnings("unchecked")
	private Almacen almacenBaseByObra(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long idResultado = 0;
		Almacen resultado = null;
		
		try {
			if (idObra == null || idObra <= 0L)
				return null;
			queryString += "select model.id_almacen from obra_almacenes model inner join obra idObra on model.id_obra = idObra.id_obra ";
			queryString += "where model.id_obra = :idObra and model.tipo = case idObra.tipo when 4 then 3 else 1 end and model.almacen_principal = 1";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				idResultado = ((BigDecimal) rows.get(0)).longValue();
			if (idResultado > 0L)
				resultado = this.ifzAlmacenes.findById(idResultado);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar recuperar la Bodega de la Obra " + idObra, e);
		}
		
		return resultado;
	}

	@SuppressWarnings("unchecked")
	private Almacen almacenAuxiliarByObra(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long idResultado = 0;
		Almacen resultado = null;
		
		try {
			if (idObra == null || idObra <= 0L)
				return null;
			queryString += "select model.id_almacen from obra_almacenes model inner join obra idObra on model.id_obra = idObra.id_obra ";
			queryString += "where model.id_obra = :idObra and model.tipo = case idObra.tipo when 4 then 3 else 1 end and model.almacen_principal = 0";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				idResultado = ((BigDecimal) rows.get(0)).longValue();
			if (idResultado > 0L)
				resultado = this.ifzAlmacenes.findById(idResultado);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar recuperar la Bodega de la Obra " + idObra, e);
		}
		
		return resultado;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private Almacen almacenByBodega(Long idBodega) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long idResultado = 0;
		Almacen resultado = null;
		
		try {
			if (idBodega == null || idBodega <= 0L)
				return null;
			queryString += "select max(a.id) as id from obra_almacenes a inner join ";
			queryString += "    (select a.id_obra, b.id_empresa from obra_almacenes a inner join almacen b on b.id = a.id_almacen ";
			queryString += "    where a.id_almacen = :idBodega) x on x.id_obra = a.id_obra ";
			queryString += "where a.tipo = 1 and a.almacen_principal = 1 ";
			queryString += "group by id_almacen, tipo, almacen_principal ";
			queryString = queryString.replace(":idBodega", idBodega.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				idResultado = ((BigDecimal) rows.get(0)).longValue();
			if (idResultado > 0L)
				resultado = this.ifzAlmacenes.findById(idResultado);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el Almacen de la Bodega " + idBodega, e);
		}
		
		return resultado;
	}

	@SuppressWarnings("unchecked")
	private Almacen bodegaByObra(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long idResultado = 0;
		Almacen resultado = null;
		
		try {
			if (idObra == null || idObra <= 0L)
				return null;
			queryString += "select model.id_almacen from obra_almacenes model inner join obra idObra on model.id_obra = idObra.id_obra ";
			queryString += "where model.id_obra = :idObra and model.tipo = case idObra.tipo when 4 then 4 else 2 end and model.almacen_principal = 0";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				idResultado = ((BigDecimal) rows.get(0)).longValue();
			if (idResultado > 0L)
				resultado = this.ifzAlmacenes.findById(idResultado);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar recuperar la Bodega de la Obra " + idObra, e);
		}
		
		return resultado;
	}

	private void setInfoSesion(Object usuario, Object empresa, Object aplicacion, Object infoSesion) {
		Gson gson = null;
		
		if (usuario != null && usuario instanceof String && ! "".equals(usuario.toString().trim()))
			this.usuarioId = Long.parseLong(usuario.toString().trim());
		if (empresa != null && empresa instanceof String && ! "".equals(empresa.toString().trim()))
			this.empresaId = Long.parseLong(empresa.toString().trim());
		if (aplicacion != null && aplicacion instanceof String && ! "".equals(aplicacion.toString().trim()))
			this.aplicacionId = Long.parseLong(aplicacion.toString().trim());

		gson = new Gson();
		if (infoSesion != null && infoSesion instanceof String && ! "".equals(infoSesion.toString().trim()))
			this.infoSesion = gson.fromJson(infoSesion.toString().trim(), InfoSesion.class);
		
		// Asignamos INFOSESION si corresponde
		if (this.infoSesion == null)
			return;

		if (this.empresaId != this.infoSesion.getEmpresa().getId())
			this.empresaId =  this.infoSesion.getEmpresa().getId();

		if (this.usuarioId != this.infoSesion.getAcceso().getUsuario().getId())
			this.usuarioId =  this.infoSesion.getAcceso().getUsuario().getId();

		if (this.aplicacionId != this.infoSesion.getAcceso().getAplicacion().getId())
			this.aplicacionId =  this.infoSesion.getAcceso().getAplicacion().getId();
		
		this.ifzMovimientos.setInfoSesion(this.infoSesion);
		this.ifzMovimientosItems.setInfoSesion(this.infoSesion);
		this.ifzTraspaso.setInfoSesion(this.infoSesion);
		this.ifzTraspasoItems.setInfoSesion(this.infoSesion);
		this.ifzAlmacenProducto.setInfoSesion(this.infoSesion);
	}
	
	private void mensajeLog(String mensaje) {
		mensajeLog(mensaje, false);
	}

	private void mensajeLog(String mensaje, boolean appendMessage) {
		if (this.mensajeLogs == null)
			this.mensajeLogs = new ArrayList<String>();
		
		// Actualizo mensaje, si corresponde
		if (appendMessage) {
			mensaje = (this.mensajeLogs.get(this.mensajeLogs.size() - 1) + mensaje);
			this.mensajeLogs.set((this.mensajeLogs.size() - 1), mensaje);
			return;
		}

		// Añado mensaje
		this.mensajeLogs.add(mensaje);
	}
	
	private void printLog() {
		printLog("############################## TOPIC INVENTARIOS --- FIN : " + new Date());
	}

	private void printLog(String mensaje) {
		try {
			mensajeLog(mensaje);
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.info("TOPIC/INVENTARIOS Log\n\n" + mensaje);
			if (this.idTopicEstatus > 0L)
				this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Terminado, "OK\n" + mensaje);
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/INVENTARIOS. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}
	
	private void printLog(String mensaje, Throwable throwable) {
		try {
			if (mensaje != null && ! "".equals(mensaje.trim()))
				mensajeLog(mensaje);
			mensajeLog("############################## TOPIC INVENTARIOS --- FIN : " + new Date());
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.error("TOPIC/INVENTARIOS Log\n\n" + mensaje + "\nEXCEPTION\n", throwable);
			if (this.idTopicEstatus > 0L)
				this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Error, mensaje + "\nEXCEPTION\n" + throwable.getMessage());
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/INVENTARIOS. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}
	
	private void topicRegistar(String evento, String mensaje) throws Exception {
		this.idTopicEstatus = this.ifzTopicEstatus.save(this.getClass().getSimpleName(), evento, mensaje, this.idTopicEstatus);
	}
	
	private void topicSinAccion(String mensaje) throws Exception {
		if (this.idTopicEstatus > 0L)
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.SinAccion, mensaje);
		this.idTopicEstatus = 0L;
		mensajeLog(mensaje);
	}
}
