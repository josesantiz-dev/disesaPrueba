package net.giro.compras.topic;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import net.giro.adp.beans.Insumos;
import net.giro.adp.beans.InsumosDetalles;
import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.logica.InsumosDetallesRem;
import net.giro.adp.logica.InsumosRem;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.adp.logica.ObraRem;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.TipoMovimientoInventario;
import net.giro.inventarios.beans.TipoMovimientoReferencia;
import net.giro.inventarios.beans.TipoTraspaso;
import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/INVENTARIOS")}, 
	mappedName = "topic/INVENTARIOS")
public class TopicInventarios implements MessageListener {
	private static Logger log = Logger.getLogger(TopicInventarios.class);
	private InitialContext ctx;
	// ----------------------------------------------
	private AlmacenMovimientosRem ifzMovimientos;
	private MovimientosDetalleRem ifzMovimientosItems;
	private AlmacenTraspasoRem ifzTraspaso;
	private TraspasoDetalleRem ifzTraspasoDetalle;
	private AlmacenProductoRem ifzAlmacenProducto;
	private InsumosRem ifzInsumos;
	private InsumosDetallesRem ifzInsumosDetalles;
	private ObraRem ifzObras;
	private ObraAlmacenesRem ifzObraAlmacen;
	private AlmacenRem ifzAlmacenes;
	private OrdenCompraRem ifzCompra;
	private OrdenCompraDetalleRem ifzCompraDetalle;
	private RequisicionRem ifzReq;
	private RequisicionDetalleRem ifzReqDetalle;
	// ----------------------------------------------
	private String mensajeLog;
	
	
	public TopicInventarios() throws NamingException,Exception { 
		this.ctx = new InitialContext();
		this.ifzCompra = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
		this.ifzCompraDetalle = (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
		this.ifzMovimientos = (AlmacenMovimientosRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
		this.ifzMovimientosItems = (MovimientosDetalleRem) this.ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");
		this.ifzAlmacenProducto = (AlmacenProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
		this.ifzInsumos = (InsumosRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
		this.ifzInsumosDetalles = (InsumosDetallesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosDetallesFac!net.giro.adp.logica.InsumosDetallesRem");
		this.ifzObraAlmacen = (ObraAlmacenesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
		this.ifzAlmacenes = (AlmacenRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
		this.ifzTraspaso = (AlmacenTraspasoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
		this.ifzTraspasoDetalle = (TraspasoDetalleRem) this.ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
		this.ifzReq = (RequisicionRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
		this.ifzReqDetalle = (RequisicionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
		this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
		
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		HashMap<String,Object> hm = new HashMap<String,Object>();	
		Gson gson = new Gson();
		TextMessage mensaje = null;
		Type tipo = null;
		// ------------------------------------------------------
		String evento = "";
		List<MovimientosDetalle> listaEntrada = null;
		List<RequisicionDetalle> reqDetalles = null;
		long idTarget = 0; // ID Target: Orden de Compra, Traspaso, etc.
		long idReferencia = 0; // ID Movimiento
		
    	try {
	    	if (message instanceof TextMessage) {
				mensajeInfo("****************************** TOPIC INVENTARIOS --- INICIO : " + new Date());
				// Transformamos mensaje
				mensaje = (TextMessage) message;
				hm = gson.fromJson(mensaje.getText(), HashMap.class);
				
				// Recuperamos datos del mensaje
				evento = hm.get("evento").toString().trim().toUpperCase();
				idTarget = Long.valueOf(hm.get("target").toString());
				
				// Lanzamos evento requerido
				switch (evento) {
					case "BO-COMPRAS":
						mensajeInfo("****************************** BackOrder COMPRAS   ******************* ");
						mensajeInfo("*********** Afectacion Existencia en Inventarios   *********** ");
						tipo = new TypeToken<List<MovimientosDetalle>>() {}.getType();
						listaEntrada = new Gson().fromJson(hm.get("atributos").toString().trim(), tipo);
						if (listaEntrada == null)
							listaEntrada = new ArrayList<MovimientosDetalle>();
						
						if (hm.containsKey("referencia"))
							idReferencia = Long.valueOf(hm.get("referencia").toString());
						
						mensajeInfo("****************************** Orden Compra : " + idTarget);
						mensajeInfo("****************************** Movimiento   : " + idReferencia);
						mensajeInfo("****************************** Productos    : " + listaEntrada.size());
						if (idTarget <= 0L || idReferencia <= 0L || listaEntrada == null || listaEntrada.isEmpty()) {
							mensajeInfo("****************************** ............. Parametros insuficientes ");
							break;
						}
						
						backOrderCompras(idTarget, idReferencia, listaEntrada);
						break;
					case "BO-INVENTARIO":
						mensajeInfo("******************* BackOffice INVENTARIOS   ******************* ");
						mensajeInfo("*********** Afectacion Existencia en Inventarios   *********** ");
						tipo = new TypeToken<List<MovimientosDetalle>>() {}.getType();
						listaEntrada = new Gson().fromJson(hm.get("atributos").toString().trim(), tipo);
						if (listaEntrada == null)
							listaEntrada = new ArrayList<MovimientosDetalle>();

						mensajeInfo("****************************** Movimiento  : " + idTarget);
						mensajeInfo("****************************** Productos   : " + listaEntrada.size());
						if (idTarget <= 0L || listaEntrada == null || listaEntrada.isEmpty()) {
							mensajeInfo("****************************** ............. Parametros insuficientes ");
							break;
						}
						
						backOfficeInventario(idTarget, listaEntrada);
						break;
						
					case "BO-SBO":
						mensajeInfo("******************* BackOffice Solicitud a Bodega por Obra   ******************* ");
						mensajeInfo("*********** Generar Traspaso por Solicitud a Bodega   *********** ");
						mensajeInfo("****************************** Obra        : " + idTarget);
						if (idTarget <= 0L) {
							mensajeInfo("****************************** ............. Parametros insuficientes ");
							break;
						}
						
						solicitudBodega(idTarget);
						break;
						
					case "BO-SBR":
						mensajeInfo("******************* BackOffice Solicitud a Bodega por Requisicion   ******************* ");
						mensajeInfo("*********** Generar Traspaso por Solicitud a Bodega   *********** ");
						tipo = new TypeToken<List<RequisicionDetalle>>() {}.getType();
						reqDetalles = new Gson().fromJson(hm.get("atributos").toString().trim(), tipo);
						
						if (reqDetalles == null)
							reqDetalles = new ArrayList<RequisicionDetalle>();

						mensajeInfo("****************************** Requisicion : " + idTarget);
						mensajeInfo("****************************** Productos   : " + reqDetalles.size());
						if (idReferencia <= 0L || reqDetalles == null || reqDetalles.isEmpty()) {
							mensajeInfo("****************************** ............. Parametros insuficientes ");
							break;
						}
						
						solicitudBodega(idTarget, reqDetalles);
						break;
						
					default:
						mensajeInfo("****************************** Evento '" + evento + "' no identificado XD");
						break;
				}
			}

			printLog("****************************** TOPIC INVENTARIOS --- FIN : " + new Date());
    	} catch (Exception e) {
			printLog("Ocurrio un problema al procesar el mensaje para BACKORDER COMPRAS", e);
		}
    }

	// ----------------------------------------------------------------------------------------
	// EVENTOS
	// ----------------------------------------------------------------------------------------
	
	private void backOrderCompras(long idOrdenCompra, long idMovimiento, List<MovimientosDetalle> listaEntrada) {
		List<Insumos> listAux = null;
		List<InsumosDetalles> listInsumos = null;
		List<OrdenCompraDetalle> listaDetalles = null;
		AlmacenMovimientos movimiento = null;
		OrdenCompra orden = null;
		double actual = 0;
		double cantidad = 0;
		boolean completa = true;
		
		try {
			// Recupero Orden de Compras
			mensajeInfo("****************************** BACKORDER COMPRAS. Recuperando Orden de Compra ... ");
			orden = this.ifzCompra.findById(idOrdenCompra);
			if (orden == null)
				throw new Exception("No se pudo recuperar la Orden de Compra recibida");
			
			// Recupero los detalles de la Orden de Compra
			mensajeInfo("****************************** BACKORDER COMPRAS. Recuperando detalles de Orden de Compra ... ");
			listaDetalles = this.ifzCompraDetalle.findByProperty("idOrdenCompra", idOrdenCompra, 0);
			if (listaDetalles == null || listaDetalles.isEmpty())
				throw new Exception("No se pudo recuperar los detalles de la Orden de Compra recibida");
			
			// Recupero Movimiento
			movimiento = this.ifzMovimientos.findById(idMovimiento);
			if (movimiento == null)
				throw new Exception("No se pudo recuperar el Movimiento recibido");
			
			mensajeInfo("****************************** BACKORDER COMPRAS. Procesando " + listaDetalles.size()+ " productos ... ");
			for (OrdenCompraDetalle var : listaDetalles) {
				actual = getCantidad(var.getIdProducto(), listaEntrada);
				mensajeInfo("Procesando Producto " + var.getIdProducto()+ ". Recibido: " + var.getCantidadRecibida()+ ", Actual: " + actual);
				actual += var.getCantidadRecibida();
				var.setCantidadRecibida(actual);
			}
			
			// Guardamos los cambios
			listaDetalles = this.ifzCompraDetalle.saveOrUpdateList(listaDetalles);
			
			// Una vez se actualizó, verificar si la compra ya esta completa: Consultar nuevamente los detalles
			mensajeInfo("****************************** BACKORDER COMPRAS. Comprobando Orden de Compra completa ... ");
			listaDetalles = this.ifzCompraDetalle.findByProperty("idOrdenCompra", idOrdenCompra, 0);
			for (OrdenCompraDetalle var : listaDetalles) {
				if (var.getCantidad() != var.getCantidadRecibida()) {
					completa = false;	//si por lo menos un solo detalle que sea diferente la cantidad a la recibida, aun no esta completa
					break;
				}
			}
			
			// Actualizamos el estatus completo de la Orden de Compra si corresponde
			if (completa) {
				mensajeInfo("****************************** BACKORDER COMPRAS. Actualizando estatus Orden de Compra ... ");
				// Corrijo estatus de orden de compra y estatus de entrada en la ORDEN DE COMPRA
				orden.setEstatus(2);
				orden.setCompleta(1);
				orden.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Actualizo ORDEN DE COMPRA
				this.ifzCompra.update(orden);
				
				notificaCompradorOrdenCompra(orden, listaDetalles);
			}
			
			// Añadimos los productos al Almacen/Bodega
			aumentaInventario(movimiento, listaEntrada);
			
			// Actualizo la Explosion de Insumos
			mensajeInfo("****************************** BACKORDER COMPRAS. Actualizando Explosion de Insumos ... ");
			listAux = this.ifzInsumos.findByProperty("idObra", orden.getIdObra(), 0);
			if (listAux != null && ! listAux.isEmpty()) {
				listInsumos = this.ifzInsumosDetalles.findByProperty("idInsumo", listAux.get(0).getId(), 0);
				if (listInsumos != null && ! listInsumos.isEmpty()) {
					for (MovimientosDetalle item : listaEntrada) {
						cantidad = 0;
						for (InsumosDetalles ins : listInsumos) {
							if (ins.getIdProducto().longValue() != item.getIdProducto())
								continue;
							
							// Actualizo cantidad surtida 
							cantidad = item.getCantidad();
							if (ins.getCantidadSurtida() > 0)
								cantidad += ins.getCantidadSurtida();
							ins.setCantidadSurtida(cantidad);
							this.ifzInsumosDetalles.update(ins);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			printLog("Ocurrio un problema al actualizar el BACKORDER COMPRAS", e);
		}
	}
	
	private void backOfficeInventario(long idMovimiento, List<MovimientosDetalle> listDetalles) {
		AlmacenMovimientos movimiento = null;

		try {
			// Recupero Movimiento
			movimiento = this.ifzMovimientos.findById(idMovimiento);
			if (movimiento == null)
				throw new Exception("Movimiento no encontrado :( ");
			
			if (movimiento.getTipo() == TipoMovimientoInventario.ENTRADA.ordinal())
				aumentaInventario(movimiento, listDetalles);
			else if (movimiento.getTipo() == TipoMovimientoInventario.SALIDA.ordinal())
				descuentaInventario(movimiento, listDetalles);
			else
				throw new Exception("Tipo de Movimiento desconocido :| ");
		} catch (Exception e) {
			printLog("Ocurrio un problema al procesar el BackOffice para INVENTARIO. Movimiento " + idMovimiento, e);
		}
	}
	
	private void solicitudBodega(long idObra) {
		List<InsumosDetalles> listInsumoDetalles = null;
		List<Insumos> listInsumos = null;
		Insumos pojoInsumo = null;
		Obra pojoObra = null;
		double cantidad = 0;
		// ----------------------------------
		AlmacenProducto pInventario = null;
		Almacen almacen = null;
		Almacen bodega = null;
		long idAlmacen = 0L;
		Long idRecibe = 0L;
		// ---------------------------------
		List<TraspasoDetalle> listDetalles = null;
		AlmacenTraspaso pojoTraspaso = null;
		TraspasoDetalle det = null;
		double cantTraspaso = 0;
		// ---------------------------------
		List<MovimientosDetalle> listSalidaDetalle = null;
		MovimientosDetalle salidaDetalle = null;
		AlmacenMovimientos pojoSalida = null;
		
		try {
			if (idObra <= 0L) {
				mensajeInfo("Sin Obra para recuperar Explosion de Insumos.");
				return;
			}
			
			// Recuperamos las explosiones de insumos cargadas a la Obra y filtramos la activa
			listInsumos = this.ifzInsumos.findByProperty("idObra", idObra, 0);
			for (Insumos var : listInsumos) {
				if (var.getEstatus() == 1)
					continue;
				
				listInsumoDetalles = this.ifzInsumosDetalles.findByProperty("idInsumo", var.getId(), 0);
				if (listInsumoDetalles == null || listInsumoDetalles.isEmpty()) {
					log.warn("La Explosion de Insumos " + var.getId() + " no tiene Productos cargados. Continuo verificando ... ");
					continue;
				}

				pojoInsumo = var;
				break;
			}
			
			// Comprobamos que tenemos una Explosion de Insumos
			if (pojoInsumo == null || pojoInsumo.getId() == null || pojoInsumo.getId() <= 0L || listInsumoDetalles == null || listInsumoDetalles.isEmpty()) {
				mensajeInfo("No encontre ninguna Explosion de Insumos activa o No tiene Productos cargados");
				return;
			}
			
			idAlmacen = recuperarIdAlmacen(idObra);
			almacen = this.ifzAlmacenes.findById(recuperarIdAlmacen(idObra));
			bodega = this.ifzAlmacenes.findById(recuperarIdBodega(idObra));
			
			if (almacen == null || almacen.getId() == null || almacen.getId() <= 0L) {
				log.warn("La Obra no tiene asignado ninguna Almacen General como Principal");
				return;
			}
			
			if (bodega == null || bodega.getId() == null || bodega.getId() <= 0L) {
				log.warn("La Obra no tiene asignada ninguna Bodega");
				return;
			}
			
			if (almacen.getIdEncargado() == null || almacen.getIdEncargado() <= 0L)
				mensajeInfo("El Almacen " + almacen.getNombre() + " (" + almacen.getId() + ") no tiene asignado el Encargado");

			idRecibe = bodega.getIdEncargado();
			if (idRecibe == null || idRecibe <= 0L) {
				mensajeInfo("La Bodega " + bodega.getNombre() + " (" + bodega.getId() + ") no tiene asignado el Encargado");
				pojoObra = this.ifzObras.findById(idObra);
				idRecibe = pojoObra.getIdResponsable();
				if (idRecibe == null || idRecibe <= 0L) {
					log.warn("La Obra no tiene asignado un Responsable y la Bodega no tiene Encargado, es necesario alguno para recibir el Traspaso que se generara por Solicitud a Bodega");
					return;
				}
			}
			
			// Comprobamos existencias y generamos detalle de traspaso si corresponde
			for (InsumosDetalles var : listInsumoDetalles) {
				pInventario = this.ifzAlmacenProducto.findAlmacenProducto(idAlmacen, var.getIdProducto());
				if (pInventario == null || pInventario.getId() == null || pInventario.getId() <= 0L || pInventario.getExistencia() <= 0)
					continue;
				
				if (listDetalles == null)
					listDetalles = new ArrayList<TraspasoDetalle>();
				
				det = new TraspasoDetalle();
				det.setIdProducto(var.getIdProducto());
				det.setCantidad(cantTraspaso);
				det.setCantidadRecibida(0);
				det.setEstatus(1);
				det.setCreadoPor(1);
				det.setFechaCreacion(Calendar.getInstance().getTime());
				det.setModificadoPor(1);
				det.setFechaModificacion(Calendar.getInstance().getTime());
				
				listDetalles.add(det);
			}
			
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.warn("Sin existencias para Traspaso");
				return;
			}
			
			// Genero Traspaso
			pojoTraspaso = new AlmacenTraspaso();
			pojoTraspaso.setIdAlmacenOrigen(almacen.getId());
			pojoTraspaso.setEntrega(almacen.getIdEncargado());
			pojoTraspaso.setIdAlmacenDestino(bodega.getId());
			pojoTraspaso.setRecibe(idRecibe);
			pojoTraspaso.setTipo(TipoTraspaso.SolicitudABodega.ordinal()); 
			pojoTraspaso.setEstatus(0);
			pojoTraspaso.setSistema(1);
			pojoTraspaso.setCreadoPor(1);
			pojoTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
			pojoTraspaso.setModificadoPor(1);
			pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
			
			// Guardo Traspaso y detalles
			pojoTraspaso.setId(this.ifzTraspaso.save(pojoTraspaso));
			for (TraspasoDetalle item : listDetalles) {
				item.setIdAlmacenTraspaso(pojoTraspaso.getId());
				det.setFechaCreacion(Calendar.getInstance().getTime());
				det.setFechaModificacion(Calendar.getInstance().getTime());

				det.setId(this.ifzTraspasoDetalle.save(det));
			}
			
			// Genera Movimiento SALIDA
			pojoSalida = new AlmacenMovimientos();
			pojoSalida.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
			pojoSalida.setTipoEntrada(TipoMovimientoReferencia.TRASPASO.toString());
			pojoSalida.setIdOrdenCompra(0L);
			pojoSalida.setIdTraspaso(pojoTraspaso.getId());
			pojoSalida.setIdAlmacen(pojoTraspaso.getIdAlmacenOrigen());
			pojoSalida.setFecha(pojoTraspaso.getFecha());
			pojoSalida.setEntrega(pojoTraspaso.getEntrega());
			pojoSalida.setRecibe(pojoTraspaso.getRecibe());
			pojoSalida.setCreadoPor(pojoTraspaso.getCreadoPor());
			pojoSalida.setFechaCreacion(pojoTraspaso.getFechaCreacion());
			pojoSalida.setModificadoPor(pojoTraspaso.getModificadoPor());
			pojoSalida.setFechaModificacion(pojoTraspaso.getFechaModificacion());
			pojoSalida.setSistema(1);
			
			// Guardo salida (movimiento)
			mensajeInfo("Guardando movimiento de Salida para el Traspaso ...");
			pojoSalida.setId(this.ifzMovimientos.save(pojoSalida));
			
			// Genero detalles de salida
			mensajeInfo("Movimiento de Salida guardado. Guardando detalle de movimiento ...");
			for (TraspasoDetalle item : listDetalles) {
				salidaDetalle = new MovimientosDetalle();
				salidaDetalle.setIdAlmacenMovimiento(pojoSalida.getId());
				salidaDetalle.setIdProducto(item.getIdProducto());
				salidaDetalle.setCantidad(item.getCantidad());
				salidaDetalle.setCantidadSolicitada(item.getCantidad());
				salidaDetalle.setEstatus(0);
				salidaDetalle.setCreadoPor(item.getCreadoPor());
				salidaDetalle.setFechaCreacion(item.getFechaCreacion());
				salidaDetalle.setModificadoPor(item.getModificadoPor());
				salidaDetalle.setFechaModificacion(item.getFechaModificacion());
				
				salidaDetalle.setId(this.ifzMovimientosItems.save(salidaDetalle)); 
				
				if (listSalidaDetalle == null)
					listSalidaDetalle = new ArrayList<MovimientosDetalle>();
				listSalidaDetalle.add(salidaDetalle);
			}
			
			// Actualizo INVENTARIO
			descuentaInventario(pojoSalida, listSalidaDetalle);
			
			// Notificamos al Encargado de Almacen del Traspaso (Salida de Material)
			notificaEncargadoAlmacen(pojoTraspaso.getId());

			// Notificamos al Encargado de Bodega del Traspaso para su recepcion cuando corresponda
			notificaEncargadoBodega(pojoTraspaso.getId());
			
			// Actualizo EXPLOSION DE INSUMOS
			for (MovimientosDetalle item : listSalidaDetalle) {
				cantidad = 0;
				for (InsumosDetalles ins : listInsumoDetalles) {
					if (ins.getIdProducto().longValue() != item.getIdProducto())
						continue;
					
					// Actualizo cantidad surtida 
					cantidad = item.getCantidad();
					if (ins.getCantidadSurtida() > 0)
						cantidad += ins.getCantidadSurtida();
					ins.setCantidadSurtida(cantidad);
					this.ifzInsumosDetalles.update(ins);
					break;
				}
			}
		} catch (Exception e) {
			printLog("Ocurrio un problema al generar la Solicitud a Bodega de la Explosion de Insumos", e);
		}
	}

	private void solicitudBodega(long idRequisicion, List<RequisicionDetalle> listReqDetalles) {
		Requisicion pojoRequisicion = null;
		Obra pojoObra = null;
		// ----------------------------------
		AlmacenProducto pInventario = null;
		Almacen almacen = null;
		Almacen bodega = null;
		long idAlmacen = 0L;
		Long idRecibe = 0L;
		// ---------------------------------
		List<TraspasoDetalle> listDetalles = null;
		AlmacenTraspaso pojoTraspaso = null;
		TraspasoDetalle det = null;
		double cantTraspaso = 0;
		// ---------------------------------
		List<MovimientosDetalle> listSalidaDetalle = null;
		MovimientosDetalle salidaDetalle = null;
		AlmacenMovimientos pojoSalida = null;
		
		try {
			// Validamos REQUISICION y DETALLES
			if (idRequisicion <= 0L) {
				log.warn("ID Requisicion no valido.");
				return;
			}
			
			pojoRequisicion = this.ifzReq.findById(idRequisicion);
			if (pojoRequisicion == null || pojoRequisicion.getId() == null || pojoRequisicion.getId() <= 0L) {
				log.warn("No pude recuperar la Requisicion: " + idRequisicion);
				return;
			}
			
			if (listReqDetalles == null || listReqDetalles.isEmpty()) {
				listReqDetalles = this.ifzReqDetalle.findByProperty("idRequisicion", idRequisicion, 0);
				if (listReqDetalles == null || listReqDetalles.isEmpty()) {
					log.warn("La Requisicion " + idRequisicion + " no tiene Productos cargados.");
					return;
				}
			}
			
			// Recuperamos datos para TRASPASO (Almacen, Bodega, Entrega, Recibe).
			idAlmacen = recuperarIdAlmacen(pojoRequisicion.getIdObra());
			almacen = this.ifzAlmacenes.findById(recuperarIdAlmacen(pojoRequisicion.getIdObra()));
			bodega = this.ifzAlmacenes.findById(recuperarIdBodega(pojoRequisicion.getIdObra()));
			
			if (almacen == null || almacen.getId() == null || almacen.getId() <= 0L) {
				log.warn("La Obra no tiene asignado ninguna Almacen General como Principal");
				return;
			}
			
			if (bodega == null || bodega.getId() == null || bodega.getId() <= 0L) {
				log.warn("La Obra no tiene asignada ninguna Bodega");
				return;
			}
			
			if (almacen.getIdEncargado() == null || almacen.getIdEncargado() <= 0L)
				mensajeInfo("El Almacen " + almacen.getNombre() + " (" + almacen.getId() + ") no tiene asignado el Encargado");

			idRecibe = bodega.getIdEncargado();
			if (idRecibe == null || idRecibe <= 0L) {
				mensajeInfo("La Bodega " + bodega.getNombre() + " (" + bodega.getId() + ") no tiene asignado el Encargado");
				pojoObra = this.ifzObras.findById(pojoRequisicion.getIdObra());
				idRecibe = pojoObra.getIdResponsable();
				if (idRecibe == null || idRecibe <= 0L) {
					log.warn("La Obra no tiene asignado un Responsable y la Bodega no tiene Encargado, es necesario alguno para recibir el Traspaso que se generara por Solicitud a Bodega");
					return;
				}
			}
			
			// Comprobamos existencias y generamos detalle de traspaso si corresponde
			for (RequisicionDetalle var : listReqDetalles) {
				pInventario = this.ifzAlmacenProducto.findAlmacenProducto(idAlmacen, var.getIdProducto());
				if (pInventario == null || pInventario.getId() == null || pInventario.getId() <= 0L || pInventario.getExistencia() <= 0)
					continue;
				
				if (listDetalles == null)
					listDetalles = new ArrayList<TraspasoDetalle>();
				
				det = new TraspasoDetalle();
				det.setIdProducto(var.getIdProducto());
				det.setCantidad(cantTraspaso);
				det.setCantidadRecibida(0);
				det.setEstatus(1);
				det.setCreadoPor(1);
				det.setFechaCreacion(Calendar.getInstance().getTime());
				det.setModificadoPor(1);
				det.setFechaModificacion(Calendar.getInstance().getTime());
				
				listDetalles.add(det);
			}
			
			if (listDetalles == null || listDetalles.isEmpty()) {
				log.warn("Sin existencias para Traspaso");
				return;
			}
			
			// Genero Traspaso
			pojoTraspaso = new AlmacenTraspaso();
			pojoTraspaso.setIdAlmacenOrigen(almacen.getId());
			pojoTraspaso.setEntrega(almacen.getIdEncargado());
			pojoTraspaso.setIdAlmacenDestino(bodega.getId());
			pojoTraspaso.setRecibe(idRecibe);
			pojoTraspaso.setTipo(TipoTraspaso.SolicitudABodega.ordinal()); 
			pojoTraspaso.setEstatus(0);
			pojoTraspaso.setSistema(1);
			pojoTraspaso.setCreadoPor(1);
			pojoTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
			pojoTraspaso.setModificadoPor(1);
			pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
			
			// Guardo Traspaso y detalles
			pojoTraspaso.setId(this.ifzTraspaso.save(pojoTraspaso));
			for (TraspasoDetalle item : listDetalles) {
				item.setIdAlmacenTraspaso(pojoTraspaso.getId());
				det.setFechaCreacion(Calendar.getInstance().getTime());
				det.setFechaModificacion(Calendar.getInstance().getTime());

				det.setId(this.ifzTraspasoDetalle.save(det));
			}
			
			// Genera Movimiento SALIDA
			pojoSalida = new AlmacenMovimientos();
			pojoSalida.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
			pojoSalida.setTipoEntrada(TipoMovimientoReferencia.TRASPASO.toString());
			pojoSalida.setIdOrdenCompra(0L);
			pojoSalida.setIdTraspaso(pojoTraspaso.getId());
			pojoSalida.setIdAlmacen(pojoTraspaso.getIdAlmacenOrigen());
			pojoSalida.setFecha(pojoTraspaso.getFecha());
			pojoSalida.setEntrega(pojoTraspaso.getEntrega());
			pojoSalida.setRecibe(pojoTraspaso.getRecibe());
			pojoSalida.setCreadoPor(pojoTraspaso.getCreadoPor());
			pojoSalida.setFechaCreacion(pojoTraspaso.getFechaCreacion());
			pojoSalida.setModificadoPor(pojoTraspaso.getModificadoPor());
			pojoSalida.setFechaModificacion(pojoTraspaso.getFechaModificacion());
			pojoSalida.setSistema(1);
			
			// Guardo salida (movimiento)
			mensajeInfo("Guardando movimiento de Salida para el Traspaso ...");
			pojoSalida.setId(this.ifzMovimientos.save(pojoSalida));
			
			// Genero detalles de salida
			mensajeInfo("Movimiento de Salida guardado. Guardando detalle de movimiento ...");
			for (TraspasoDetalle item : listDetalles) {
				salidaDetalle = new MovimientosDetalle();
				salidaDetalle.setIdAlmacenMovimiento(pojoSalida.getId());
				salidaDetalle.setIdProducto(item.getIdProducto());
				salidaDetalle.setCantidad(item.getCantidad());
				salidaDetalle.setCantidadSolicitada(item.getCantidad());
				salidaDetalle.setEstatus(0);
				salidaDetalle.setCreadoPor(item.getCreadoPor());
				salidaDetalle.setFechaCreacion(item.getFechaCreacion());
				salidaDetalle.setModificadoPor(item.getModificadoPor());
				salidaDetalle.setFechaModificacion(item.getFechaModificacion());
				
				salidaDetalle.setId(this.ifzMovimientosItems.save(salidaDetalle));
				
				if (listSalidaDetalle == null)
					listSalidaDetalle = new ArrayList<MovimientosDetalle>();
				listSalidaDetalle.add(salidaDetalle);
			}
			
			// Actualizo INVENTARIO
			descuentaInventario(pojoSalida, listSalidaDetalle);
			
			// Notificamos al Encargado de Almacen del Traspaso (Salida de Material)
			notificaEncargadoAlmacen(pojoTraspaso.getId());

			// Notificamos al Encargado de Bodega del Traspaso para su recepcion cuando corresponda
			notificaEncargadoBodega(pojoTraspaso.getId());
		} catch (Exception e) {
			printLog("Ocurrio un problema al generar la Solicitud a Bodega de la Explosion de Insumos", e);
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// METODOS PRIVADOS
	// ----------------------------------------------------------------------------------------

	private void aumentaInventario(AlmacenMovimientos movimiento, List<MovimientosDetalle> listaEntrada) {
		List<AlmacenProducto> listItems = null;
		AlmacenProducto ap = null;
		long idAlmacenBodega = 0L;
		
		try {
			// Recuperamos el almacen afectado
			idAlmacenBodega = movimiento.getIdAlmacen();
			
			// Añadimos el producto al Almacen indicado
			mensajeInfo("****************************** BACKORDER COMPRAS. Agregando a Inventarios ... ");
			for (MovimientosDetalle item : listaEntrada) {
				// Compruebo producto en almacen
				ap = this.ifzAlmacenProducto.findAlmacenProducto(idAlmacenBodega, item.getIdProducto());
				if (ap == null) {
					// Producto nuevo en Almacen
					ap = new AlmacenProducto();
					ap.setIdAlmacen(movimiento.getIdAlmacen());
					ap.setIdProducto(item.getIdProducto());
					ap.setExistencia(item.getCantidad());
					ap.setEstatus(0);
					ap.setCreadoPor(movimiento.getCreadoPor());
					ap.setFechaCreacion(Calendar.getInstance().getTime());
					ap.setModificadoPor(movimiento.getCreadoPor());
					ap.setFechaModificacion(Calendar.getInstance().getTime());
				} else {
					// Actualizo existencia
					ap.setExistencia(ap.getExistencia() + item.getCantidad());
					ap.setModificadoPor(movimiento.getCreadoPor());
					ap.setFechaModificacion(Calendar.getInstance().getTime());
				}
				
				if (listItems == null)
					listItems = new ArrayList<AlmacenProducto>();
				listItems.add(ap);
			}
			
			// Guardo los cambios
			mensajeInfo("****************************** Actualizando inventario ... ");
			if (listItems != null && ! listItems.isEmpty())
				this.ifzAlmacenProducto.saveOrUpdateList(listItems);
			mensajeInfo("****************************** Actualizando inventario ... OK");
		} catch (Exception e) {
			printLog("Ocurrio un problema al aumentar el INVENTARIO del Almacen/Bodega " + idAlmacenBodega, e);
		}
	}
	
	private void descuentaInventario(AlmacenMovimientos movimiento, List<MovimientosDetalle> listaEntrada) {
		List<AlmacenProducto> listItems = null;
		AlmacenProducto ap = null;
		long idAlmacenBodega = 0L;
		
		try {
			// Recuperamos el almacen afectado
			idAlmacenBodega = movimiento.getIdAlmacen();
			
			// Descontamos el producto al Almacen indicado
			mensajeInfo("****************************** Comprobando detalles ... ");
			for (MovimientosDetalle item : listaEntrada) {
				// Compruebo producto en almacen
				ap = this.ifzAlmacenProducto.findAlmacenProducto(idAlmacenBodega, item.getIdProducto());
				if (ap != null) {
					// Disminuyo existencia
					ap.setExistencia(ap.getExistencia() - item.getCantidad());
					ap.setModificadoPor(movimiento.getCreadoPor());
					ap.setFechaModificacion(Calendar.getInstance().getTime());
					
					if (listItems == null)
						listItems = new ArrayList<AlmacenProducto>();
					listItems.add(ap);
				}
			}
			mensajeInfo("****************************** Comprobando detalles ... OK");
			
			// Guardo los cambios
			mensajeInfo("****************************** Actualizando inventario ... ");
			if (listItems != null && ! listItems.isEmpty())
				this.ifzAlmacenProducto.saveOrUpdateList(listItems);
			mensajeInfo("****************************** Actualizando inventario ... OK");
		} catch (Exception e) {
			printLog("Ocurrio un problema al descontar el INVENTARIO del Almacen/Bodega " + idAlmacenBodega, e);
		}
	}

	private double getCantidad(Long idProducto, List<MovimientosDetalle> listaEntrada) {
		for (MovimientosDetalle var : listaEntrada) {
			if (idProducto.equals(var.getIdProducto())) {
				mensajeInfo("Producto Encontrado: "+idProducto+", cant: "+var.getCantidad());
				return var.getCantidad();
			}
		}
		
		return 0;
	}

	private long recuperarIdAlmacen(long idObra) {
		ObraAlmacenes almacen = null;

		try {
			almacen = this.ifzObraAlmacen.findAlmacenPrincipal(idObra, 0);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar recuperar el Almacen Principal de la Obra " + idObra, e);
		}
		
		return almacen.getIdAlmacen();
	}
	
	private long recuperarIdBodega(long idObra) {
		ObraAlmacenes almacen = null;

		try {
			almacen = this.ifzObraAlmacen.findBodega(idObra);
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar recuperar la Bodega de la Obra " + idObra, e);
		}
		
		return almacen.getIdAlmacen();
	}
	
	private void notificaEncargadoAlmacen(long idTraspaso) {
		// do nothing
	}
	
	private void notificaEncargadoBodega(long idTraspaso) {
		// do nothing
	}
	
	private void notificaCompradorOrdenCompra(OrdenCompra pojoOrdenCompra, List<OrdenCompraDetalle> detalles) {
		// do nothing
	}
	
	private void mensajeInfo(String mensaje) {
		this.mensajeLog += "\n" + mensaje;
	}
	
	private void printLog(String mensaje) {
		log.info("topic/INVENTARIOS Log\n\n" + this.mensajeLog + "\n\n" + mensaje);
	}

	private void printLog(String error, Throwable throwable) {
		log.error("topic/INVENTARIOS Log\n\n" + this.mensajeLog + "\n\nOcurrio un problema al intentar enviar mensaje al topic/INVENTARIOS", throwable);
	}
}
