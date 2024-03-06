package net.giro.compras.topic;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.comun.EstatusCompras;
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
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
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/COMPRAS")}, 
	mappedName = "topic/COMPRAS")
public class TopicCompras implements MessageListener {
	private static Logger log = Logger.getLogger(TopicCompras.class);
	private InfoSesion infoSesion;
	private long usuarioId;
	private long empresaId;
	private long aplicacionId;
	// -----------------------------------------------------------------------------------------------
	private TopicEstatusRem ifzTopicEstatus;
	private long idTopicEstatus;
	private List<String> mensajeLogs;
	// -----------------------------------------------------------------------------------------------
	private OrdenCompraRem ifzOC;
	private OrdenCompraDetalleRem ifzOCDetalle;
	private CotizacionRem ifzCot;
	private CotizacionDetalleRem ifzCotDetalle;
	private RequisicionRem ifzReq;
	private RequisicionDetalleRem ifzReqDetalle;
	
	public TopicCompras() {
		InitialContext ctx = null;
		
		try {
			ctx = new InitialContext();
			this.ifzTopicEstatus = (TopicEstatusRem) ctx.lookup("ejb:/Logica_Publico//TopicEstatusFac!net.giro.plataforma.logica.TopicEstatusRem");
			this.ifzOC = (OrdenCompraRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOCDetalle = (OrdenCompraDetalleRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzCot = (CotizacionRem) ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzCotDetalle = (CotizacionDetalleRem) ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzReq = (RequisicionRem) ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalle = (RequisicionDetalleRem) ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
		} catch (Exception e) {
			printLog("Ocurrio un problema al instanciar TopicCompras <-> topic/COMPRAS", e);
		}
	}

	@Override
	public void onMessage(Message message) {
		Gson gson = null;
		TextMessage mensaje = null;
		MensajeTopic mensajeTopic = null;
		TopicEventosCompras evento = null;
		String eventoParam = "";
		
    	try {
	    	if (message instanceof TextMessage) {
				mensajeLog("############################## TOPIC COMPRAS --- INICIO : " + new Date());
				// Transformamos mensaje
				gson = new Gson();
				mensaje = (TextMessage) message;
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
				evento = TopicEventosCompras.fromString(eventoParam);
				switch (evento) {
					case REQUISICION_ESTATUS: 
						bo_requisicionEstatus(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), valueToString(mensajeTopic.getAtributos()));
						break;
						
					case REQUISICION_CANTIDAD:
						bo_requisicionCantidad(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getAtributos()));
						break;

					case REQUISICION_SUMINISTRO: 
						bo_requisicionSuministro(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getAtributos()));
						break;
						
					case COTIZACION_ESTATUS: 
						bo_cotizacionEstatus(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), valueToString(mensajeTopic.getAtributos()));
						break;

					case COTIZACION_CANTIDAD:
						bo_cotizacionCantidad(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getAtributos()));
						break;

					case COTIZACION_SUMINISTRO: 
						bo_cotizacionSuministro(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getAtributos()));
						break;
						
					case COTIZACION_PRECIOS: 
						bo_cotizacionPrecios(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getAtributos()));
						break;
						
					case ORDEN_ESTATUS: 
						bo_ordenEstatus(valueToLong(mensajeTopic.getTarget()), valueToString(mensajeTopic.getAtributos()));
						break;
						
					case ORDEN_SUMINISTRO: 
						bo_ordenSuministro(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), valueToString(mensajeTopic.getAtributos()));
						break;
						
					case COMPRAS_SBO:
						bo_solicitudBodega(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), valueToLong(mensajeTopic.getReferenciaExtra()), valueToString(mensajeTopic.getAtributos()));
						break;
						
					default:
						mensajeLog("****************************** Evento '" + eventoParam + "' no identificado XD");
						break;
				}
			}
	    	
			printLog();
    	} catch (Exception e) {
    		printLog("Ocurrio un problema al intentar procesar evento en TOPIC COMPRAS", e);
		} 
	}

	// -----------------------------------------------------------------------------------------------
	// EVENTOS 
	// -----------------------------------------------------------------------------------------------
	
	/**
	 * BackOffice Requisicion: Actualizacion de estatus en Requisicion
	 * @param idRequisicion Requisicion se le actualizara el estatus
	 * @param idOrdenCompra Orden de Compra que provoco el cambio de estatus
	 * @param productos Lista de productos que cambio
	 * @throws Exception
	 */
	private void bo_requisicionEstatus(long idRequisicion, long idOrdenCompra, String atributos) throws Exception {
		HashMap<String, Double> productos = null;
		Gson gson = null;
		Type tipo = null;
		// --------------------------------------------------------------------
		List<RequisicionDetalle> detalles = null;
		Requisicion requisicion = null;
		int cerrada = 0;
		EstatusCompras estatus = null; 
		// -------------------------------------------
		OrdenCompra orden = null;
		int factor = 1;
		
		try {
			gson = new Gson();
			idRequisicion = (idRequisicion > 0L ? idRequisicion : 0L);
			idOrdenCompra = (idOrdenCompra > 0L ? idOrdenCompra : 0L);
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			productos = gson.fromJson(atributos.trim(), tipo);
			productos = (productos != null ? productos : new HashMap<String, Double>());
			mensajeLog("***************************************************************************************************************");
			mensajeLog("*********** BackOffice Requisicion ****************************************************************************");
			mensajeLog("*********** Actualizacion de estatus a Requisicion ************************************************************");
			mensajeLog("*** Requisicion : " + idRequisicion);
			mensajeLog("*** OrdenCompra : " + idOrdenCompra);
			mensajeLog("*** Productos   : " + productos.size());
			if (idRequisicion <= 0L || idOrdenCompra <= 0L || productos.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}

			mensajeLog("Comprobando Requisicion ... ");
			requisicion = this.ifzReq.findById(idRequisicion);
			if (requisicion == null || requisicion.getId() == null || requisicion.getId() <= 0L) 
				throw new Exception("No se pudo recuperar la Requisicion.");
			estatus = EstatusCompras.fromOrdinal(requisicion.getEstatus());
			mensajeLog("OK", true);

			mensajeLog("Comprobando detalles ... ");
			detalles = this.ifzReqDetalle.findAll(idRequisicion);
			if (idRequisicion <= 0L || detalles == null || detalles.isEmpty())
				throw new Exception("No se pudo recuperar los detalles de la Requisicion.");
			mensajeLog("OK", true);

			mensajeLog("Comprobando Orden de Compra ... ");
			orden = this.ifzOC.findById(idOrdenCompra);
			if (orden == null || orden.getId() == null || orden.getId() <= 0L)
				throw new Exception("No se pudo recuperar la Orden Compra.");
			factor = (orden.getEstatus() == 1 ? -1 : 1);
			mensajeLog("OK", true);

			mensajeLog("Actualizando detalles ... ");
			for (RequisicionDetalle det : detalles) {
				if (productos.containsKey(det.getIdProducto().toString())) 
					det.addOrdenCompra((productos.get(det.getIdProducto().toString()) * factor));
			}
			mensajeLog("OK", true);
			
			// Guardamos los cambios
			mensajeLog("Guardamos detalles ... ");
			detalles = this.ifzReqDetalle.saveOrUpdateList(detalles);
			mensajeLog("OK", true);
			
			// Actualizo el estatus de la Requisicion: Si algun producto no esta totalmente en ordenes de compra, la Requisicion esta disponible
			mensajeLog("Calculando estatus Requisicion ... ");
			cerrada = 1;
			for (RequisicionDetalle detalle : detalles) {
				if (detalle.getPendiente() > 0) {
					cerrada = 0;
					break;
				}
			}
			mensajeLog("OK", true);
			
			mensajeLog("Actualizando estatus(" + estatus.toString() + ":" + cerrada + ") Requisicion " + requisicion.getId() + " ... ");
			requisicion.setCerrada(cerrada);
			requisicion.setEstatus(estatus.ordinal());
			this.ifzReq.update(requisicion);
			mensajeLog("OK", true);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar procesar la Requisicion: " + idRequisicion, e);
			throw e;
		}
	}
	
	/**
	 * Backoffice Requisicion: Aumento de cantidad de productos de la Requisicion
	 * @param idRequisicion
	 * @param productos
	 * @throws Exception
	 */
	private void bo_requisicionCantidad(long idRequisicion, String atributos) throws Exception {
		HashMap<String, Double> productos = null;
		Gson gson = null;
		Type tipo = null;
		// --------------------------------------------------------------------
		List<RequisicionDetalle> detalles = null;
		Requisicion requisicion = null;
		int cerrada = 0;
		EstatusCompras estatus = null; 
		
		try {
			gson = new Gson();
			idRequisicion = (idRequisicion > 0L ? idRequisicion : 0L);
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			productos = gson.fromJson(atributos.trim(), tipo);
			productos = (productos != null ? productos : new HashMap<String, Double>());
			mensajeLog("***************************************************************************************************************");
			mensajeLog("*********** BackOffice Requisicion ****************************************************************************");
			mensajeLog("*********** Actualizacion de estatus a Requisicion ************************************************************");
			mensajeLog("*** Requisicion : " + idRequisicion);
			mensajeLog("*** Productos   : " + productos.size());
			if (idRequisicion <= 0L || productos.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}

			mensajeLog("Comprobando Requisicion ... ");
			requisicion = this.ifzReq.findById(idRequisicion);
			if (requisicion == null || requisicion.getId() == null || requisicion.getId() <= 0L) 
				throw new Exception("No se pudo recuperar la Requisicion.");
			estatus = EstatusCompras.fromOrdinal(requisicion.getEstatus());
			mensajeLog("OK", true);

			mensajeLog("Comprobando detalles ... ");
			detalles = this.ifzReqDetalle.findAll(idRequisicion);
			if (idRequisicion <= 0L || detalles == null || detalles.isEmpty())
				throw new Exception("No se pudo recuperar los detalles de la Requisicion.");
			mensajeLog("OK", true);

			mensajeLog("Actualizando detalles ... ");
			for (RequisicionDetalle detalle : detalles) {
				if (productos.containsKey(detalle.getIdProducto().toString())) 
					detalle.addCantidad(Math.abs(productos.get(detalle.getIdProducto().toString())));
				if (detalle.getCantidadOrdenCompra() > 0) 
					detalle.setEstatus(1);
			}
			
			// Guardamos los cambios
			mensajeLog("Guardamos detalles ... ");
			detalles = this.ifzReqDetalle.saveOrUpdateList(detalles);
			mensajeLog("OK", true);
			
			// Actualizo el estatus de la Requisicion: Si algun producto no esta totalmente en ordenes de compra, la Requisicion esta disponible
			mensajeLog("Calculando estatus Requisicion ... ");
			cerrada = 1;
			for (RequisicionDetalle detalle : detalles) {
				if (detalle.getPendiente() > 0) {
					cerrada = 0;
					break;
				}
			}
			mensajeLog("OK", true);
			
			mensajeLog("Actualizando estatus(" + estatus.toString() + ":" + cerrada + ") Requisicion " + requisicion.getId() + " ... ");
			requisicion.setCerrada(cerrada);
			requisicion.setEstatus(estatus.ordinal());
			this.ifzReq.update(requisicion);
			mensajeLog("OK", true);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar procesar la Requisicion: " + idRequisicion, e);
			throw e;
		}
	}
	
	/**
	 * BackOrder Compras: Actualizacion de suministro en Requisicion
	 * @param idRequisicion Requisicion que se le actualizaran los suministros
	 * @param productos Listado de productos suministrados
	 */
	private void bo_requisicionSuministro(long idRequisicion, String atributos) throws Exception {
		HashMap<String, Double> suministros = null;
		Gson gson = null;
		Type tipo = null;
		// --------------------------------------------------------------------
		List<RequisicionDetalle> detalles = null;
		Requisicion requisicion = null;
		EstatusCompras estatus = null; 
		boolean suministrada = false;
		
		try {
			gson = new Gson();
			idRequisicion = (idRequisicion > 0L ? idRequisicion : 0L);
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			suministros = gson.fromJson(atributos.trim(), tipo);
			suministros = (suministros != null ? suministros : new HashMap<String, Double>());
			mensajeLog("***************************************************************************************************************");
			mensajeLog("*********** BackOffice Requisicion ****************************************************************************");
			mensajeLog("*********** Actualizacion de Suministro a Requisicion *********************************************************");
			mensajeLog("*** Requisicion : " + idRequisicion);
			mensajeLog("*** Productos   : " + suministros.size());
			if (idRequisicion <= 0L || suministros.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}

			// Comprobamos Requisicion y detalles
			mensajeLog("Comprobando Requisicion ... ");
			requisicion = this.ifzReq.findById(idRequisicion);
			if (requisicion == null || requisicion.getId() == null || requisicion.getId() <= 0L) 
				throw new Exception("No se pudo recuperar la Requisicion indicada: " + idRequisicion);
			estatus = EstatusCompras.fromOrdinal(requisicion.getEstatus());
			mensajeLog("OK", true);

			mensajeLog("Comprobando detalles ... ");
			detalles = this.ifzReqDetalle.findAll(idRequisicion);
			if (detalles == null || detalles.isEmpty()) 
				throw new Exception("No se pudo recuperar los productos de la Requisicion indicada: " + idRequisicion);
			mensajeLog("OK", true);
			
			// Actualizamos suministro
			mensajeLog("Actualizando Suministro ... ");
			for (RequisicionDetalle detalle : detalles) {
				if (suministros.containsKey(detalle.getIdProducto().toString()))
					detalle.addSuministrado(suministros.get(detalle.getIdProducto().toString()));
			}
			mensajeLog("OK", true);

			mensajeLog("Guardando cambios ... ");
			detalles = this.ifzReqDetalle.saveOrUpdateList(detalles);
			mensajeLog("OK", true);

			// Actualizo el estatus de la Requisicion: Si algun producto no esta totalmente en ordenes de compra, la Requisicion esta disponible
			mensajeLog("Calculando estatus Requisicion ... ");
			suministrada = true;
			for (RequisicionDetalle detalle : detalles) {
				if (detalle.getSuministroPendiente() > 0) {
					suministrada = false;
					break;
				}
			}
			estatus = (estatus == EstatusCompras.ACTIVA && suministrada) ? EstatusCompras.SUMINISTRADA : EstatusCompras.ACTIVA; 
			mensajeLog("OK", true);

			mensajeLog("Actualizando estatus(" + estatus.toString() + ") Requisicion " + idRequisicion + " ... ");
			requisicion.setEstatus(estatus.ordinal());
			this.ifzReq.update(requisicion);
			mensajeLog("OK", true);
		} catch (Exception e) {
			printLog("Ocurrio un problema al actualizar el suministro en la Requisicion " + idRequisicion, e);
			throw e;
		}
	}

	/**
	 * Backoffice Cotizacion: Actualizacion de estatus en Cotizacion
	 * @param idCotizacion Cotizacion que se actualizara el estatus
	 * @param idOrdenCompra Orden de Compra que provoco el cambio de estatus
	 * @param productos Lista de productos que cambio
	 * @throws Exception
	 */
	private void bo_cotizacionEstatus(long idCotizacion, long idOrdenCompra, String atributos) throws Exception {
		HashMap<String, Double> productos = null;
		Gson gson = null;
		Type tipo = null;
		// --------------------------------------------------------------------
		List<CotizacionDetalle> detalles = null;
		Cotizacion cotizacion = null;
		int cerrada = 0;
		EstatusCompras estatus = null; 
		// -------------------------------------------
		OrdenCompra orden = null;
		int factor = 1;
		
		try {
			gson = new Gson();
			idCotizacion = (idCotizacion > 0L ? idCotizacion : 0L);
			idOrdenCompra = (idOrdenCompra > 0L ? idOrdenCompra : 0L);
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			productos = gson.fromJson(atributos.trim(), tipo);
			productos = (productos != null ? productos : new HashMap<String, Double>());
			mensajeLog("***************************************************************************************************************");
			mensajeLog("*********** BackOffice Cotizacion *****************************************************************************");
			mensajeLog("*********** Actualizacion de estatus a Cotizacion *************************************************************");
			mensajeLog("*** Cotizacion  : " + idCotizacion);
			mensajeLog("*** OrdenCompra : " + idOrdenCompra);
			mensajeLog("*** Productos   : " + productos.size());
			if (idCotizacion <= 0L || idOrdenCompra <= 0L || productos.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}

			mensajeLog("Comprobando Cotizacion ... ");
			cotizacion = this.ifzCot.findById(idCotizacion);
			if (cotizacion == null || cotizacion.getId() == null || cotizacion.getId() <= 0L) 
				throw new Exception("No se pudo recuperar la Cotizacion.");
			estatus = EstatusCompras.fromOrdinal(cotizacion.getEstatus());
			mensajeLog("OK", true);
			
			mensajeLog("Comprobando detalles de Cotizacion ... ");
			detalles = this.ifzCotDetalle.findAll(idCotizacion);
			if (idCotizacion <= 0L || detalles == null || detalles.isEmpty()) 
				throw new Exception("No se pudo recuperar los detalles de la Cotizacion.");
			mensajeLog("OK", true);

			mensajeLog("Comprobando Orden de Compra ... ");
			orden = this.ifzOC.findById(idOrdenCompra);
			if (orden == null || orden.getId() == null || orden.getId() <= 0L)
				throw new Exception("No se pudo recuperar la Orden Compra.");
			factor = (orden.getEstatus() == 1 ? -1 : 1);
			mensajeLog("OK", true);

			// Ciclo para el calculo de lo que esta en Orden de Compra y estatus de detalle en cotizacion
			mensajeLog("Actualizando detalles de Cotizacion ... ");
			for (CotizacionDetalle det : detalles) {
				if (productos.containsKey(det.getIdProducto().toString())) 
					det.addOrdenCompra((productos.get(det.getIdProducto().toString()) * factor));
			}
			mensajeLog("OK", true);

			// Guardamos los cambios
			mensajeLog("Guardamos detalles ... ");
			detalles = this.ifzCotDetalle.saveOrUpdateList(detalles);
			mensajeLog("OK", true);
			
			// Calculamos estatus de la Cotizacion: Si algun producto no esta en Orden de Compra, la Cotizacion esta disponible
			mensajeLog("Calculando estatus Cotizacion ... ");
			cerrada = 1;
			for (CotizacionDetalle detalle : detalles) {
				if (detalle.getSuministroPendiente() > 0) {
				//if (detalle.getPendiente() > 0) {
					cerrada = 0;
					break;
				}
			}
			mensajeLog("OK", true);
			
			mensajeLog("Actualizando estatus(" + estatus.toString() + ":" + cerrada + ") Cotizacion " + idCotizacion + " ... ");
			cotizacion.setCerrada(cerrada);
			cotizacion.setEstatus(estatus.ordinal());
			this.ifzCot.update(cotizacion);
			mensajeLog("OK", true);
			
			if (cotizacion.getIdRequisicion() != null && cotizacion.getIdRequisicion() > 0L && factor >= 0) 
				autoMessage(TopicEventosCompras.REQUISICION_ESTATUS, cotizacion.getIdRequisicion(), idOrdenCompra, "", productos);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar procesar la Cotizacion: " + idCotizacion, e);
			throw e;
		}
	}

	/**
	 * Backoffice Cotizacion: Aumento de cantidad de productos de la Cotizacion
	 * @param idCotizacion
	 * @param productos
	 * @throws Exception
	 */
	private void bo_cotizacionCantidad(long idCotizacion, String atributos) throws Exception {
		HashMap<String, Double> productos = null;
		Gson gson = null;
		Type tipo = null;
		// --------------------------------------------------------------------
		List<CotizacionDetalle> detalles = null;
		List<CotizacionDetalle> nuevos = null;
		CotizacionDetalle detalle = null;
		Cotizacion cotizacion = null;
		List<Long> productosActualizados;
		double cantidad = 0;
		// -------------------------------------------
		int cerrada = 0;
		EstatusCompras estatus = null; 
		
		try {
			gson = new Gson();
			idCotizacion = (idCotizacion > 0L ? idCotizacion : 0L);
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			productos = gson.fromJson(atributos.trim(), tipo);
			productos = (productos != null ? productos : new HashMap<String, Double>());
			mensajeLog("***************************************************************************************************************");
			mensajeLog("*********** BackOffice Cotizacion *****************************************************************************");
			mensajeLog("*********** Actualizacion de estatus a Cotizacion *************************************************************");
			mensajeLog("*** Cotizacion  : " + idCotizacion);
			mensajeLog("*** Productos   : " + productos.size());
			if (idCotizacion <= 0L || productos.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}

			mensajeLog("Comprobando Cotizacion ... ");
			cotizacion = this.ifzCot.findById(idCotizacion);
			if (cotizacion == null || cotizacion.getId() == null || cotizacion.getId() <= 0L) 
				throw new Exception("No se pudo recuperar la Cotizacion.");
			estatus = EstatusCompras.fromOrdinal(cotizacion.getEstatus());
			mensajeLog("OK", true);
			
			mensajeLog("Comprobando detalles de Cotizacion ... ");
			detalles = this.ifzCotDetalle.findAll(idCotizacion);
			if (idCotizacion <= 0L || detalles == null || detalles.isEmpty()) 
				throw new Exception("No se pudo recuperar los detalles de la Cotizacion.");
			mensajeLog("OK", true);

			// Ciclo para el calculo de lo que esta en Orden de Compra y estatus de detalle en cotizacion
			mensajeLog("Actualizando detalles de Cotizacion ... ");
			productosActualizados = new ArrayList<Long>();
			for (CotizacionDetalle det : detalles) {
				if (productos.containsKey(det.getIdProducto().toString())) 
					det.addCantidad(Math.abs(productos.get(det.getIdProducto().toString())));
				if (det.getCantidadOrdenCompra() > 0) 
					det.setEstatus(1);
			}

			nuevos = new ArrayList<CotizacionDetalle>();
			for (Entry<String, Double> producto : productos.entrySet()) {
				if (productosActualizados.contains(producto.getKey())) 
					continue;
				cantidad = Math.abs(producto.getValue());
				detalle = new CotizacionDetalle();
				detalle.setIdCotizacion(idCotizacion);
				detalle.setIdProducto(Long.parseLong(producto.getKey()));
				detalle.setCantidad(cantidad);
				detalle.setPrecioUnitario(0);
				detalle.setImporte(0);
				detalle.setMargen(0);
				detalle.setCantidadInicial(cantidad);
				detalle.setCantidadOrdenCompra(0);
				detalle.setCantidadSuministrada(0);
				detalle.setEstatus(0); // 0: ACTIVA, 1: EN OC
				detalle.setCreadoPor(cotizacion.getCreadoPor());
				detalle.setFechaCreacion(Calendar.getInstance().getTime());
				detalle.setModificadoPor(cotizacion.getModificadoPor());
				detalle.setFechaModificacion(Calendar.getInstance().getTime());
				nuevos.add(detalle);
			}

			// Guardamos los cambios
			mensajeLog("Guardamos detalles ... ");
			if (nuevos != null && ! nuevos.isEmpty())
				detalles.addAll(nuevos);
			detalles = this.ifzCotDetalle.saveOrUpdateList(detalles);
			mensajeLog("OK", true);
			
			// Calculamos estatus de la Cotizacion: Si algun producto no esta en Orden de Compra, la Cotizacion esta disponible
			mensajeLog("Calculando estatus Cotizacion ... ");
			cerrada = 1;
			for (CotizacionDetalle det : detalles) {
				if (det.getSuministroPendiente() > 0) {
				//if (det.getPendiente() > 0) {
					cerrada = 0;
					break;
				}
			}
			mensajeLog("OK", true);
			
			mensajeLog("Actualizando estatus(" + estatus.toString() + ":" + cerrada + ") Cotizacion " + idCotizacion + " ... ");
			cotizacion.setCerrada(cerrada);
			cotizacion.setEstatus(estatus.ordinal());
			this.ifzCot.update(cotizacion);
			mensajeLog("OK", true);

			/* Previamente se actualizaba a cuenta de cotizados
			if (cotizacion.getIdRequisicion() != null && cotizacion.getIdRequisicion() > 0L)
				bo_requisicionCantidad(cotizacion.getIdRequisicion(), productos);
			bo_cotizacionSuministro(idCotizacion, productos);*/
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar procesar la Cotizacion: " + idCotizacion, e);
			throw e;
		}
	}
	
	/**
	 * BackOrder Compras: Actualizacion de suministro en Cotizacion
	 * @param idCotizacion Cotizacion que se le actualizaran los suministros
	 * @param productos Listado de productos suministrados
	 */
	private void bo_cotizacionSuministro(long idCotizacion, String atributos) throws Exception {
		HashMap<String, Double> suministros = null;
		List<CotizacionDetalle> detalles = null;
		Cotizacion cotizacion = null;
		EstatusCompras estatus = null; 
		boolean suministrada = false;
		// --------------------------------------------------------------------
		MensajeTopic msgTopic = null;
		Gson gson = null;
		Type tipo = null;
		String target = "0";
		String referencia = "0";
		String comando = "";
		
		try {
			gson = new Gson();
			idCotizacion = (idCotizacion > 0L ? idCotizacion : 0L);
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			suministros = gson.fromJson(atributos.trim(), tipo);
			suministros = (suministros != null ? suministros : new HashMap<String, Double>());
			mensajeLog("***************************************************************************************************************");
			mensajeLog("*********** BackOffice Cotizacion *****************************************************************************");
			mensajeLog("*********** Actualizacion de Suministro a Cotizacion *************************************************************");
			mensajeLog("*** Cotizacion  : " + idCotizacion);
			mensajeLog("*** Productos   : " + suministros.size());
			if (idCotizacion <= 0L || suministros.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}

			mensajeLog("Comprobando Cotizacion ... ");
			cotizacion = this.ifzCot.findById(idCotizacion);
			if (cotizacion == null || cotizacion.getId() == null || cotizacion.getId() <= 0L) 
				throw new Exception("No se pudo recuperar la Cotizacion indicada: " + idCotizacion);
			estatus = EstatusCompras.fromOrdinal(cotizacion.getEstatus());
			mensajeLog("OK", true);
			
			detalles = this.ifzCotDetalle.findAll(idCotizacion);
			if (detalles == null || detalles.isEmpty()) 
				throw new Exception("No se pudo recuperar los producto de la Cotizacion indicada: " + idCotizacion);
			mensajeLog("OK", true);

			// Actualizamos suministro
			mensajeLog("Actualizando Suministro ... ");
			for (CotizacionDetalle detalle : detalles) {
				if (suministros.containsKey(detalle.getIdProducto().toString()))
					detalle.addSuministrado(suministros.get(detalle.getIdProducto().toString()));
			}
			mensajeLog("OK", true);

			mensajeLog("Guardando cambios ... ");
			detalles = this.ifzCotDetalle.saveOrUpdateList(detalles);
			mensajeLog("OK", true);

			// Actualizo el estatus de la Cotizacion: Si algun producto no esta en Cotizacion, la Requisicion esta disponible
			mensajeLog("Calculando estatus Cotizacion ... ");
			suministrada = true;
			for (CotizacionDetalle detalle : detalles) {
				if (detalle.getSuministroPendiente() > 0) {
					suministrada = false;
					break;
				}
			}
			estatus = (estatus == EstatusCompras.ACTIVA && suministrada) ? EstatusCompras.SUMINISTRADA : EstatusCompras.ACTIVA; 
			mensajeLog("OK", true);

			mensajeLog("Actualizando estatus(" + estatus.toString() + ") Cotizacion " + idCotizacion + " ... ");
			cotizacion.setEstatus(estatus.ordinal());
			this.ifzCot.update(cotizacion);
			mensajeLog("OK", true);

			// Replico suministro a Requisicion o Explosion de Insumos segun sea el caso
			if (cotizacion.getIdRequisicion() != null && cotizacion.getIdRequisicion() > 0L) {
				autoMessage(TopicEventosCompras.REQUISICION_SUMINISTRO, cotizacion.getIdRequisicion(), "", "", suministros);
				return;
			}
			/*else
				bo_explosionInsumosSuministro(cotizacion.getIdObra(), suministros);*/
		} catch (Exception e) {
			printLog("Ocurrio un problema al actualizar el suministro de Cotizacion", e);
			throw e;
		}

		try {
			// Envio de mensaje a GP
			gson = new Gson();
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			target = cotizacion.getIdObra().toString();
			atributos = gson.toJson(suministros, tipo);
			
			msgTopic = new MensajeTopic(TopicEventosGP.INSUMOS_SUMINISTRO, target, referencia, atributos, this.infoSesion);
			msgTopic.setIdTopicEstatusOwner(this.idTopicEstatus);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-RQ\n\n" + comando + "\n\n", e);
		}
	}

	/**
	 * BackOffice Orden de Compra: Actualizacion de precios en Orden de Compra desde Cotizacion
	 * @param idCotizacion
	 * @param productos Lista de Productos [idProducto : precioUnitario]
	 * @throws Exception
	 */
	private void bo_cotizacionPrecios(long idCotizacion, String atributos) throws Exception {
		HashMap<String, Double> productos = null;
		Gson gson = null;
		Type tipo = null;
		// --------------------------------------------------------------------
		List<OrdenCompraDetalle> detalles = null;
		List<OrdenCompra> ordenes = null;

		try {
			gson = new Gson();
			idCotizacion = (idCotizacion > 0L ? idCotizacion : 0L);
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			productos = gson.fromJson(atributos.trim(), tipo);
			productos = (productos != null ? productos : new HashMap<String, Double>());
			mensajeLog("***************************************************************************************************************");
			mensajeLog("*********** BackOffice Cotizacion *****************************************************************************");
			mensajeLog("*********** Actualizacion de precios a Orden Compra ***********************************************************");
			mensajeLog("*** Cotizacion : " + idCotizacion);
			mensajeLog("*** Productos  : " + productos.size());
			if (idCotizacion <= 0L || productos.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			mensajeLog("Comprobando Ordenes de Compra ... ");
			ordenes = this.ifzOC.findByCotizacion(idCotizacion, false, 0); // .findByProperty("idCotizacion.id", idCotizacion, 0);
			if (ordenes == null || ordenes.isEmpty()) {
				topicSinAccion("Sin accion. Sin Ordenes de Compra para actualizar");
				return;
			}
			mensajeLog("OK", true);

			for (OrdenCompra orden : ordenes) {
				mensajeLog("Comprobando detalles Ordenes de Compra " + orden.getFolio() + " ... ");
				detalles = this.ifzOCDetalle.findAll(orden.getId());
				if (detalles == null || detalles.isEmpty()) 
					continue;
				mensajeLog("OK", true);

				mensajeLog("Actualizando precios en Orden de Compra " + orden.getFolio() + " ... ");
				for (OrdenCompraDetalle detalle : detalles) {
					if (productos.containsKey(detalle.getIdProducto().toString()))
						detalle.setPrecioUnitario(productos.get(detalle.getIdProducto().toString()));
				}
				mensajeLog("OK", true);
				
				// Guardo los cambios
				mensajeLog("Guardando precios en Orden de Compra " + orden.getFolio() + " ... ");
				this.ifzOCDetalle.saveOrUpdateList(detalles);
				mensajeLog("OK", true);
			}
		} catch (Exception e) {
			printLog("No se pudo actualizar los precios en ninguna Orden de Compra", e);
    		throw e;
		}
	}

	/**
	 * Backoffice Orden de Compra: Actualizacion de cantidad en Cotizacion y Requisicion de los productos en Orden de Compra
	 * @param idOrdenCompra
	 * @param productos
	 * @throws Exception
	 */
	private void bo_ordenEstatus(long idOrdenCompra, String atributos) throws Exception {
		HashMap<String, Double> productos = null;
		Gson gson = null;
		Type tipo = null;
		// --------------------------------------------------------------------
		OrdenCompra orden = null;
		Cotizacion cotizacion = null;
		
		try {
			gson = new Gson();
			idOrdenCompra = (idOrdenCompra > 0L ? idOrdenCompra : 0L);
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			productos = gson.fromJson(atributos.trim(), tipo);
			productos = (productos != null ? productos : new HashMap<String, Double>());
			mensajeLog("***************************************************************************************************************");
			mensajeLog("*********** BackOffice Orden de Compra ************************************************************************");
			mensajeLog("*********** Actualizacion de cantidad en Cotizacion/Requisicion de los productos en Orden de Compra ***********");
			mensajeLog("*** OrdenCompra : " + idOrdenCompra);
			mensajeLog("*** Productos   : " + productos.size());
			if (idOrdenCompra <= 0L || productos.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}

			// Comprobamos Orden de Compra
			mensajeLog("Comprobando Orden Compra ... ");
			orden = this.ifzOC.findById(idOrdenCompra);
			if (orden == null || orden.getId() == null || orden.getId() <= 0L) 
				throw new Exception("No se pudo recuperar la Orden de Compra.");
			mensajeLog("OK", true);

			// Comprobamos Cotizacion
			mensajeLog("Comprobando Cotizacion ... ");
			cotizacion = orden.getIdCotizacion();
			if (cotizacion == null || cotizacion.getId() == null || cotizacion.getId() <= 0L) 
				throw new Exception("No se pudo recuperar la Cotizacion.");
			mensajeLog("OK", true);

			// Actualizacion de estatus de cotizacion
			autoMessage(TopicEventosCompras.COTIZACION_ESTATUS, cotizacion.getId(), idOrdenCompra, "", productos);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar procesar la Orden de Compra: " + idOrdenCompra, e);
			throw e;
		}
	}
	
	/**
	 * BackOrder Compras: Actualizacion de Suministro en Orden de Compra
	 * @param idOrdenCompra Orden de Compra ID
	 * @param suministros Listado de Productos (ID) con su cantidad suministrada
	 */
	private void bo_ordenSuministro(Long idOrdenCompra, Long idMovimiento, String atributos) throws Exception {
		HashMap<String, Double> suministros = null;
		Gson gson = null;
		Type tipo = null;
		// --------------------------------------------------------------------
		//HashMap<Long, HashMap<String, Object>> productosPrecios = null;
		//HashMap<String, Object> productoPrecio = null;
		// --------------------------------------------------------------------
		HashMap<String, List<Double>> precios = null;
		List<Double> precio = null;
		//double precioCompra = 0;
		double idMoneda = 0;
		double tipoCambio = 0;
		// --------------------------------------------------------------------
		Calendar fechaLimite = null;
		List<OrdenCompraDetalle> productos = null;
		OrdenCompra orden = null;
		EstatusCompras estatus = null; 
		boolean completa = false;
		
		try {
			gson = new Gson();
			idOrdenCompra = (idOrdenCompra > 0L ? idOrdenCompra : 0L);
			idMovimiento = (idMovimiento != null ? idMovimiento : 0L);
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			suministros = gson.fromJson(atributos.trim(), tipo);
			suministros = (suministros != null ? suministros : new HashMap<String, Double>());
			mensajeLog("***************************************************************************************************************");
			mensajeLog("*********** BackOffice Suministro *****************************************************************************");
			mensajeLog("*********** Actualizacion de Suministro a Orden de Compra *****************************************************");
			mensajeLog("*** OrdenCompra : " + idOrdenCompra);
			mensajeLog("*** Productos   : " + suministros.size());
			if (idOrdenCompra <= 0L || suministros.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recupero Orden de Compras
			mensajeLog(" ---> Recuperando Orden de Compra ... ");
			this.ifzOC.setInfoSesion(this.infoSesion);
			orden = this.ifzOC.findById(idOrdenCompra);
			if (orden == null)
				throw new Exception("No se pudo recuperar la Orden de Compra recibida");
			idMoneda = orden.getIdMoneda();
			tipoCambio = (orden.getTipoCambio().doubleValue() > 0 ? orden.getTipoCambio().doubleValue() : 1);
			tipoCambio = (tipoCambio <= 1 ? this.ifzOC.tipoCambio(this.infoSesion.getEmpresa().getMonedaId(), 10000002L, orden.getFecha()) : tipoCambio);
			estatus = EstatusCompras.fromOrdinal(orden.getEstatus());
			mensajeLog("OK", true);
			
			// Recupero los detalles de la Orden de Compra
			mensajeLog(" ---> Recuperando productos de Orden de Compra ... ");
			productos = this.ifzOCDetalle.findAll(idOrdenCompra);
			if (productos == null || productos.isEmpty())
				throw new Exception("No se pudo recuperar los detalles de la Orden de Compra recibida");
			mensajeLog("OK", true);
			
			/*mensajeLog(" ---> Procesando " + productos.size() + " productos ... ");
			productosPrecios = new HashMap<Long, HashMap<String, Object>>();
			for (OrdenCompraDetalle producto : productos) {
				if (suministros.containsKey(producto.getIdProducto().toString())) {
					// Suministro
					producto.addSuministrada(suministros.get(producto.getIdProducto().toString()));
					// Producto precio
					precioCompra = producto.getPrecioUnitario();
					productoPrecio = new HashMap<String, Object>(); 
					productoPrecio.put("tipoCambio", tipoCambio);
					productoPrecio.put("precioCompra", precioCompra);
					productoPrecio.put("precioCompraPesos", precioCompra * tipoCambio);
					productosPrecios.put(producto.getIdProducto(), productoPrecio);
				}
			}
			mensajeLog("OK", true);*/
			
			mensajeLog(" ---> Procesando " + productos.size() + " productos ... ");
			precios = new HashMap<String, List<Double>>();
			for (OrdenCompraDetalle producto : productos) {
				if (suministros.containsKey(producto.getIdProducto().toString())) {
					// Suministro
					producto.addSuministrada(suministros.get(producto.getIdProducto().toString()));
					// Precio
					precio = new ArrayList<Double>(); //precio = producto.getPrecioUnitario() * tipoCambio;
					precio.add(producto.getPrecioUnitario());
					precio.add(idMoneda);
					precio.add(tipoCambio);
					precios.put(producto.getIdProducto().toString(), precio);
				}
			}
			mensajeLog("OK", true);
			
			// Guardamos lo suministrado
			mensajeLog(" ---> Guardando suministro ... ");
			productos = this.ifzOCDetalle.saveOrUpdateList(productos);
			mensajeLog("OK", true);

			// -----------------------------------------------------------------------------------
			// Calculo integral de estatus para la Orden de Compra
			// -----------------------------------------------------------------------------------

			// Actualizo el estatus de la Orden de Compra: Si algun producto no esta en Cotizacion, la Requisicion esta disponible
			mensajeLog("Actualizando estatus Orden de Compra " + idOrdenCompra + " ... ");
			completa = true;
			for (OrdenCompraDetalle detalle : productos) {
				if (detalle.getPendiente() > 0) {
					completa = false;
					break;
				}
			}
			
			estatus = (estatus == EstatusCompras.ACTIVA && completa) ? EstatusCompras.SUMINISTRADA : EstatusCompras.ACTIVA; 
			orden.setCompleta((completa ? 1 : 0));
			orden.setEstatus(estatus.ordinal());
			this.ifzOC.update(orden);
			mensajeLog("OK --> " + estatus.toString(), true);

			// -----------------------------------------------------------------------------------
			// Notificaciones
			// -----------------------------------------------------------------------------------

			// Validamos si lanzamos la actualizacion de precios al catalogo de productos
			fechaLimite = Calendar.getInstance();
			fechaLimite.setTime(Calendar.getInstance().getTime());
			fechaLimite.add(Calendar.MONTH, -3);
			if (fechaLimite.getTime().compareTo(orden.getFecha()) >= 0) 
				bo_productosActualizacionPrecios(orden.getId(), orden.getFolio(), idMovimiento, precios);
			
			// Notificamos al comprador sobre el suministro registrado
			mensajeLog(" ---> Notificando a comprador ... ");
			// TO DO: notificaCompradorOrdenCompra(orden, listaDetalles);
			mensajeLog("OK", true);
			
			// Replico suministro en Cotizacion
			autoMessage(TopicEventosCompras.COTIZACION_SUMINISTRO, orden.getIdCotizacion().getId(), "", "", suministros);
		} catch (Exception e) {
			printLog("Ocurrio un problema al actualizar el BACKORDER COMPRAS", e);
			throw e;
		}
	}

	/**
	 * BackOffie Compras: Determina el tipo de Solicitud a Bodega que debe generarse
	 * @param idObra
	 * @param idCotizacion
	 * @param idRequisicion
	 * @param atributos Listado de Productos (ID) con su cantidad suministrada
	 * @throws Exception
	 */
	private void bo_solicitudBodega(Long idObra, Long idCotizacion, Long idRequisicion, String atributos) throws Exception {
		TopicEventosInventarios evento = null;
		MensajeTopic msgTopic = null;
		String target = "0";
		String referencia = "0";
		String referenciaExtra = "0";
		String comando = "";
		
		try {
			// Procesamiento de mensaje de compras
			mensajeLog("***************************************************************************************************************");
			mensajeLog("*********** Compras: Solicitud a Bodega ***********************************************************************");
			mensajeLog("*********** Determina  y lanza proceso de Solicitud a Bodega **************************************************");
			mensajeLog("*** Obra        : " + idObra);
			mensajeLog("*** Cotizacion  : " + idCotizacion);
			mensajeLog("*** Requisicion : " + idRequisicion);
			if (idObra <= 0L || (idCotizacion <= 0L && idRequisicion <= 0L)) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			evento = TopicEventosInventarios.SOLICITUD_BODEGA;
			target = idObra.toString();
			referencia = idCotizacion.toString();
			if (idRequisicion != null && idRequisicion > 0L) {
				evento = TopicEventosInventarios.SOLICITUD_BODEGA_REQUISICION;
				referencia = idRequisicion.toString();
				referenciaExtra = idCotizacion.toString();
			} 
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el evento topic/COMPRAS:" + TopicEventosCompras.COMPRAS_SBO.toString() + " y enviar mensaje a topic/INVENTARIO:" + evento.toString() + "\n\n", e);
			throw e;
		}

		try {
			// Envio de mensaje a INVENTARIOS
			msgTopic = new MensajeTopic(evento, target, referencia, atributos, this.infoSesion);
			msgTopic.setIdTopicEstatusOwner(this.idTopicEstatus);
			msgTopic.setReferenciaExtra(referenciaExtra);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIO:" + evento.toString() + "\n\n" + comando + "\n\n", e);
		}
	}
	
	// -----------------------------------------------------------------------------------------------
	// METODOS PRIVADOS
	// -----------------------------------------------------------------------------------------------

	private void autoMessage(TopicEventosCompras evento, Object target, Object referencia, Object referenciaExtra, Object atributos) {
		MensajeTopic msgTopic = null;
		
		try {
			if (evento == null)
				return;
			target = (target != null ? target : "");
			referencia = (referencia != null ? referencia : "");
			atributos = (atributos != null ? atributos : "");
			msgTopic = new MensajeTopic(evento, target.toString(), referencia.toString(), atributos.toString(), this.infoSesion);
			msgTopic.setReferenciaExtra(referenciaExtra.toString());
			msgTopic.setIdTopicEstatusOwner(this.idTopicEstatus);
			msgTopic.enviar();
		} catch (Exception e) { 
			log.error("Ocurrio un problema al autoenviar mensaje JMS\n" + this.getClass().getCanonicalName() + ".autoMessage(evento, target, referencia, atributos)", e);
		} 
	}

	/**
	 * Lanza evento de actualizacion de precios en el catalogo de Productos
	 * @param idOrdenCompra
	 * @param folioOrdenCompra
	 * @param precios
	 */
	private void bo_productosActualizacionPrecios(Long idOrdenCompra, String folioOrdenCompra, Long idMovimiento, HashMap<String, List<Double>> precios) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		Type tipo = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			idOrdenCompra = (idOrdenCompra > 0L ? idOrdenCompra : 0L);
			folioOrdenCompra = (folioOrdenCompra != null && ! "".equals(folioOrdenCompra.trim()) ? folioOrdenCompra.trim() : "");
			idMovimiento = (idMovimiento != null ? idMovimiento : 0L);
			if ((idOrdenCompra == null || idOrdenCompra <= 0L) || (precios == null || precios.isEmpty()))
				return;
			
			gson = new Gson();
			tipo = new TypeToken<HashMap<String, List<Double>>>() {}.getType();
			target = idOrdenCompra.toString();
			referencia = folioOrdenCompra;
			atributos = gson.toJson(precios, tipo);
			
			msgTopic = new MensajeTopic(TopicEventosInventarios.PRODUCTO_PRECIOS, target, referencia, atributos, this.infoSesion);
			msgTopic.setIdTopicEstatusOwner(this.idTopicEstatus);
			msgTopic.setReferenciaExtra(idMovimiento.toString());
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-RQ\n\n" + comando + "\n\n", e);
		}
	}
	
	/*private void bo_productosActualizacionPrecios(Long idOrdenCompra, String folioOrdenCompra, Long idMovimiento, HashMap<Long, HashMap<String, Object>> precios) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		Type tipo = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			idOrdenCompra = (idOrdenCompra > 0L ? idOrdenCompra : 0L);
			folioOrdenCompra = (folioOrdenCompra != null && ! "".equals(folioOrdenCompra.trim()) ? folioOrdenCompra.trim() : "");
			idMovimiento = (idMovimiento != null ? idMovimiento : 0L);
			if ((idOrdenCompra == null || idOrdenCompra <= 0L) || (precios == null || precios.isEmpty()))
				return;
			
			gson = new Gson();
			tipo = new TypeToken<HashMap<Long, HashMap<String, Object>>>() {}.getType();
			target = idOrdenCompra.toString();
			referencia = folioOrdenCompra;
			atributos = gson.toJson(precios, tipo);
			
			msgTopic = new MensajeTopic(TopicEventosInventarios.PRODUCTO_PRECIOS, target, referencia, atributos, this.infoSesion);
			msgTopic.setReferenciaExtra(idMovimiento.toString());
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-RQ\n\n" + comando + "\n\n", e);
		}
	}*/
	
 	/*private void bo_explosionInsumosSuministro(Long idObra, HashMap<String, Double> items) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		Type tipo = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			idObra = (idObra > 0L ? idObra : 0L);
			if (items == null || items.isEmpty())
				return;
			
			gson = new Gson();
			tipo = new TypeToken<HashMap<String, Double>>() {}.getType();
			target = idObra.toString();
			atributos = gson.toJson(items, tipo);
			
			msgTopic = new MensajeTopic(TopicEventosGP.INSUMOS_SUMINISTRO, target, referencia, atributos, this.infoSesion);
			msgTopic.setIdTopicEstatusOwner(this.idTopicEstatus);
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/COMPRAS:BO-RQ\n\n" + comando + "\n\n", e);
		}
	}*/

	private String valueToString(Object value) {
		return (value != null ? value.toString().trim() : "");
	}

	private long valueToLong(Object value) {
		return ((! "".equals(valueToString(value))) ? Long.valueOf(valueToString(value)) : 0L);
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
		printLog("##############################    TOPIC COMPRAS    ##############################");
	}

	private void printLog(String mensaje) {
		try {
			mensajeLog(mensaje);
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.info("TOPIC/COMPRAS Log\n\n" + mensaje);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Terminado, "OK\n" + mensaje);
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/COMPRAS. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}
	
	private void printLog(String mensaje, Throwable throwable) {
		try {
			if (mensaje != null && ! "".equals(mensaje.trim()))
				mensajeLog(mensaje);
			mensajeLog("############################## TOPIC COMPRAS --- FIN : " + new Date());
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.error("TOPIC/COMPRAS Log\n\n" + mensaje + "\nEXCEPTION\n", throwable);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Error, mensaje + "\nEXCEPTION\n" + throwable.getMessage());
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/COMPRAS. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}
	
	private void topicRegistar(String evento, String mensaje) throws Exception {
		this.idTopicEstatus = this.ifzTopicEstatus.save(this.getClass().getSimpleName(), evento, mensaje, this.idTopicEstatus);
	}
	
	private void topicSinAccion(String mensaje) throws Exception {
		this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.SinAccion, mensaje);
		this.idTopicEstatus = 0L;
		mensajeLog(mensaje);
	}
}
