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

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.logica.ProductoRem;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/COMPRAS")}, 
	mappedName = "topic/COMPRAS")
public class TopicCompras implements MessageListener {
	private static Logger log = Logger.getLogger(TopicCompras.class);
	private InitialContext ctx;
	// ----------------------------------------------
	private OrdenCompraRem ifzOC;
	private OrdenCompraDetalleRem ifzOCDetalle;
	private CotizacionRem ifzCot;
	private CotizacionDetalleRem ifzCotDetalle;
	private RequisicionRem ifzReq;
	private RequisicionDetalleRem ifzReqDetalle;
	private ProductoRem ifzProductos;
	// ----------------------------------------------
	private String mensajeLog;
	
	
	public TopicCompras() {
		try {
			this.ctx = new InitialContext();
			this.ifzOC = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOCDetalle = (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzCot = (CotizacionRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzCotDetalle = (CotizacionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzReq = (RequisicionRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalle = (RequisicionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzProductos = (ProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
		} catch (Exception e) {
			printLog("Ocurrio un problema al instanciar TopicCompras <-> topic/COMPRAS", e);
		}
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		Gson gson = new Gson();
		TextMessage mensaje = null;
		Type tipo = null;
		// ------------------------------------------------------
		String evento = "";
		List<CotizacionDetalle> cotDetalles = null;
		List<OrdenCompraDetalle> ocDetalles = null;
		long idTarget = 0L;
		long idReferencia = 0L;
		
    	try {
			mensajeInfo("****************************** TOPIC COMPRAS --- INICIO : " + new Date() + "\n");
	    	if (message instanceof TextMessage) {
				// Transformamos mensaje
				mensaje = (TextMessage) message;
				hm = gson.fromJson(mensaje.getText(), HashMap.class);
				
				// Recuperamos datos del mensaje
				evento = hm.get("evento").toString().trim().toUpperCase();
				idTarget = Long.valueOf(hm.get("target").toString());

				// Lanzamos evento requerido
				switch (evento) {
					case "BO-RQ": 
						mensajeInfo("******************* BackOffice Requisicion  ******************* ");
						mensajeInfo("*********** Actualizacion de estatus a Requisicion  *********** ");
						tipo = new TypeToken<List<CotizacionDetalle>>() {}.getType();
						if (hm.containsKey("atributos") && ! "".equals(hm.get("atributos").toString().trim()))
							cotDetalles = new Gson().fromJson(hm.get("atributos").toString().trim(), tipo);
						
						if (cotDetalles == null)
							cotDetalles = new ArrayList<CotizacionDetalle>();
						
						if (hm.containsKey("referencia"))
							idReferencia = Long.valueOf(hm.get("referencia").toString());
						
						mensajeInfo("*** Requisicion : " + idTarget);
						mensajeInfo("*** Cotizacion  : " + idReferencia);
						mensajeInfo("*** Productos   : " + cotDetalles.size());
						if (idTarget <= 0L || cotDetalles.isEmpty()) {
							mensajeInfo("****************************** ...... Parametros insuficientes ");
							break;
						}
						
						// lanzamos back office para requisicion
						backOfficeRequisicion(idTarget, idReferencia, cotDetalles);
						break;
					case "BO-CO": 
						mensajeInfo("******************* BackOffice Cotizacion   ******************* ");
						mensajeInfo("*********** Actualizacion de estatus a Cotizacion   *********** ");
						tipo = new TypeToken<List<OrdenCompraDetalle>>() {}.getType();
						if (hm.containsKey("atributos") && ! "".equals(hm.get("atributos").toString().trim()))
							ocDetalles = new Gson().fromJson(hm.get("atributos").toString().trim(), tipo);
						
						if (ocDetalles == null)
							ocDetalles = new ArrayList<OrdenCompraDetalle>();
						
						if (hm.containsKey("referencia"))
							idReferencia = Long.valueOf(hm.get("referencia").toString());
						
						mensajeInfo("*** Cotizacion  : " + idTarget);
						mensajeInfo("*** OrdenCompra : " + idReferencia);
						mensajeInfo("*** Productos   : " + ocDetalles.size());
						if (idTarget <= 0L || ocDetalles.isEmpty()) {
							mensajeInfo("****************************** ...... Parametros insuficientes ");
							break;
						}
						
						// lanzamos back office para cotizacion
						backOfficeCotizacion(idTarget, idReferencia, ocDetalles);
						break;
					case "BO-CO$": 
						mensajeInfo("******************* BackOffice Cotizacion   ******************* ");
						mensajeInfo("*********** Actualizacion de precios a Orden Compra *********** ");
						tipo = new TypeToken<List<CotizacionDetalle>>() {}.getType();
						if (hm.containsKey("atributos") && ! "".equals(hm.get("atributos").toString().trim()))
							cotDetalles = new Gson().fromJson(hm.get("atributos").toString().trim(), tipo);
						
						if (cotDetalles == null)
							cotDetalles = new ArrayList<CotizacionDetalle>();

						mensajeInfo("*** Cotizacion  : " + idTarget);
						mensajeInfo("*** Productos   : " + cotDetalles.size());
						if (idTarget <= 0L || cotDetalles.isEmpty()) {
							mensajeInfo("****************************** ...... Parametros insuficientes ");
							break;
						}
						
						// lanzamos back office para precios en orden de compras
						backOfficeCotizacionPreciosOrdenCompra(idTarget, cotDetalles);
						break;
					case "BO-OC": 
						mensajeInfo("******************* BackOffice Orden Compra ******************* ");
						mensajeInfo("*********** Actualizacion de estatus a Orden Compra *********** ");
						tipo = new TypeToken<List<OrdenCompraDetalle>>() {}.getType();
						if (hm.containsKey("atributos") && ! "".equals(hm.get("atributos").toString().trim()))
							ocDetalles = new Gson().fromJson(hm.get("atributos").toString().trim(), tipo);
						
						if (ocDetalles == null)
							ocDetalles = new ArrayList<OrdenCompraDetalle>();

						mensajeInfo("*** OrdenCompra : " + idTarget);
						mensajeInfo("*** Productos   : " + ocDetalles.size());
						if (idTarget <= 0L || ocDetalles.isEmpty()) {
							mensajeInfo("****************************** ...... Parametros insuficientes ");
							break;
						}
						
						// lanzamos back office para actulizar precios en productos segun orden de compra
						backOfficeOrdenCompraPreciosProductos(idTarget, ocDetalles);
						break;
					default:
						mensajeInfo("****************************** Evento '" + evento + "' no identificado XD");
						break;
				}
			}

			printLog("****************************** TOPIC COMPRAS --- FIN : " + new Date());
    	} catch (Exception e) {
			printLog("Ocurrio un problema al intentar procesar evento en TOPIC COMPRAS", e);
		}
	}

	// ----------------------------------------------------------------------------------------
	// EVENTOS
	// ----------------------------------------------------------------------------------------
	
	private void backOfficeRequisicion(long idRequisicion, long idCotizacion, List<CotizacionDetalle> listDetalles) {
		List<RequisicionDetalle> reqDetalles = null;
		Requisicion pojoRequisicion = null;
		String folioCotizacion = "";
		int estatusRequisicion = 2; // En Orden de Compra
		
		try {
			mensajeInfo("Comprobando Detalles ... ");
			if (listDetalles == null || listDetalles.isEmpty()) {
				mensajeInfo("No se especifico el listado de referencia.");
				return;
			}
			mensajeInfoAppend("OK");
			
			mensajeInfo("Comprobando Requisicion y detalles ... ");
			folioCotizacion = recuperarFolioCotizacion(idCotizacion);
			reqDetalles = this.ifzReqDetalle.findByProperty("idRequisicion", idRequisicion, 0);
			if (idRequisicion <= 0L || reqDetalles == null || reqDetalles.isEmpty()) {
				mensajeInfo("No se pudo recupear la Requisicion y/o detalles.");
				return;
			}
			mensajeInfoAppend("OK");

			mensajeInfo("Actualizando detalles ... ");
			for (CotizacionDetalle item : listDetalles) {
				for (RequisicionDetalle det : reqDetalles) {
					if (item.getIdProducto().longValue() != det.getIdProducto().longValue())
						continue;
					
					det.setIdCotizacion(0L);
					det.setCotizacionFolio("");
					if (idCotizacion > 0) {
						det.setIdCotizacion(idCotizacion);
						det.setCotizacionFolio(folioCotizacion);
					}
				}
			}
			
			// Guardamos los cambios
			reqDetalles = this.ifzReqDetalle.saveOrUpdateList(reqDetalles);
			mensajeInfoAppend("OK");
			
			// Actualizo el estatus de la Requisicion: Si algun producto no esta en Cotizacion, la Requisicion esta disponible
			mensajeInfo("Comprobando estatus Requisicion ... ");
			pojoRequisicion = this.ifzReq.findById(idRequisicion);
			reqDetalles = this.ifzReqDetalle.findByProperty("idRequisicion", idRequisicion, 0);
			for (RequisicionDetalle det : reqDetalles) {
				if (det.getIdCotizacion() == 0) {
					estatusRequisicion = 0;
					break;
				}
			}
			mensajeInfoAppend("OK");

			mensajeInfo("Actualizando estatus(" + estatusRequisicion + ") Requisicion " + pojoRequisicion.getId() + " ... ");
			pojoRequisicion.setEstatus(estatusRequisicion);
			this.ifzReq.update(pojoRequisicion);
			mensajeInfoAppend("OK");
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar procesar la Requisicion: " + idRequisicion, e);
		}
	}
	
	private void backOfficeCotizacion(long idCotizacion, long idOrdenCompra, List<OrdenCompraDetalle> listDetalles) {
		List<CotizacionDetalle> cotDetalles = null;
		Cotizacion pojoCotizacion = null;
		int estatusCotizacion = 2; // En Orden de Compra
		
		try {
			mensajeInfo("STACKTRACE Evento BO-CO\n-----------------------");
			mensajeInfo("Comprobando Detalles ... ");
			if (listDetalles == null || listDetalles.isEmpty()) {
				mensajeInfo("No se especifico el listado de referencia.");
				return;
			}
			mensajeInfoAppend("OK");
			
			mensajeInfo("Comprobando Cotizacion y detalles ... ");
			cotDetalles = this.ifzCotDetalle.findByProperty("idCotizacion", idCotizacion, 0);
			if (idCotizacion <= 0L || cotDetalles == null || cotDetalles.isEmpty()) {
				mensajeInfo("No se pudo recupear la Cotizacion y/o detalles.");
				return;
			}
			mensajeInfoAppend("OK");
			
			// Actualizo estatus del detalle de la cotizacion: Si especifico la Orden de Compra, los detalles estan en Orden de Compra
			mensajeInfo("Actualizando detalles ... ");
			for (OrdenCompraDetalle item : listDetalles) {
				for (CotizacionDetalle det : cotDetalles) {
					if (item.getIdProducto().longValue() != det.getIdProducto().longValue())
						continue;
					
					det.setEstatus(0);
					if (idOrdenCompra > 0)
						det.setEstatus(1);
				}
			}

			// Guardamos los cambios
			cotDetalles = this.ifzCotDetalle.saveOrUpdateList(cotDetalles);
			mensajeInfoAppend("OK");
			
			// Actualizo el estatus de la Cotizacion: Si algun producto no esta en Orden de Compra, la Cotizacion esta disponible
			mensajeInfo("Comprobante estatus Cotizacion ... ");
			pojoCotizacion = this.ifzCot.findById(idCotizacion);
			cotDetalles = this.ifzCotDetalle.findByProperty("idCotizacion", idCotizacion, 0);
			for (CotizacionDetalle det : cotDetalles) {
				if (det.getEstatus() == 0) {
					estatusCotizacion = 0;
					break;
				}
			}
			mensajeInfoAppend("OK");

			mensajeInfo("Actualizando estatus(" + estatusCotizacion + ") Cotizacion " + idCotizacion + " ... ");
			pojoCotizacion.setEstatus(estatusCotizacion);
			this.ifzCot.update(pojoCotizacion);
			mensajeInfoAppend("OK");
		} catch (Exception e) {
			printLog("Ocurrio un problema al intentar procesar la Cotizacion: " + idCotizacion, e);
		}
	}
	
	private void backOfficeCotizacionPreciosOrdenCompra(long idCotizacion, List<CotizacionDetalle> listDetalles) {
		List<OrdenCompraDetalle> ocDetalles = null;
		List<OrdenCompra> listOC = null;

		try {
			mensajeInfo("Comprobando Detalles ... ");
			if (idCotizacion <= 0L || listDetalles == null || listDetalles.isEmpty()) {
				mensajeInfo("No se especifico el listado de referencia.");
				return;
			}
			mensajeInfoAppend("OK");

			mensajeInfo("Comprobando Ordenes de Compra ... ");
			listOC = this.ifzOC.findByProperty("idCotizacion", idCotizacion, 0);
			if (listOC == null || listOC.isEmpty()) {
				mensajeInfo("Sin Ordenes de Compra para actualizar.");
				return;
			}
			mensajeInfoAppend("OK");

			for (OrdenCompra oc : listOC) {
				mensajeInfo("Comprobando detalles Ordenes de Compra " + oc.getFolio() + " ... ");
				ocDetalles = this.ifzOCDetalle.findByProperty("idOrdenCompra", oc.getId(), 0);
				if (ocDetalles == null || ocDetalles.isEmpty()) 
					continue;
				mensajeInfoAppend("OK");

				mensajeInfo("Actualizando precios en Orden de Compra " + oc.getFolio() + " ... ");
				for (CotizacionDetalle var : listDetalles) {
					for (OrdenCompraDetalle det : ocDetalles) {
						if (! var.getIdProducto().equals(det.getIdProducto())) 
							continue;
						
						det.setPrecioUnitario(var.getPrecioUnitario());
						det.setModificadoPor(var.getModificadoPor());
						det.setFechaModificacion(Calendar.getInstance().getTime());
					}
				}
				
				// Guardo los cambios
				ocDetalles = this.ifzOCDetalle.saveOrUpdateList(ocDetalles);
				mensajeInfoAppend("OK");
			}
		} catch (Exception e) {
    		log.error("No se pudo actualizar los precios en ninguna Orden de Compra", e);
		}
	}
	
	private void backOfficeOrdenCompraPreciosProductos(Long idOrdenCompra, List<OrdenCompraDetalle> listDetalles) {
		Producto prodStored = null;
		
		try {
			mensajeInfo("Actualizando Precios de Orden de Compra en catalogo de Productos ... ");
			for (OrdenCompraDetalle item : listDetalles) {
				prodStored = this.ifzProductos.findById(item.getIdProducto());
				if (prodStored != null && prodStored.getId() != null && prodStored.getId() > 0L && prodStored.getPrecioUnitario() < item.getPrecioUnitario()) {
					prodStored.setPrecioCompra(item.getPrecioUnitario());
					prodStored.setPrecioVenta(item.getPrecioUnitario() * 1.7); // 70%
					prodStored.setPrecioUnitario(item.getPrecioUnitario() * 1.1); // 10%
					prodStored.setFechaModificacion(Calendar.getInstance().getTime());
					prodStored.setModificadoPor(item.getCreadoPor());
					
					this.ifzProductos.update(prodStored);
				}
			}
			mensajeInfoAppend("OK");
		} catch (Exception e) {
    		log.error("No se pudo actualizar los precios a los Productos", e);
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// METODOS PRIVADOS
	// ----------------------------------------------------------------------------------------

	private String recuperarFolioCotizacion(long idCotizacion) {
		Cotizacion pojoCotizacion = null;
		String resultado = "";
		
		try {
			pojoCotizacion = this.ifzCot.findById(idCotizacion);
			if (pojoCotizacion != null && pojoCotizacion.getFolio() != null && ! "".equals(pojoCotizacion.getFolio().trim()))
				resultado = pojoCotizacion.getFolio().trim();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el Folio de la Cotizacion " + idCotizacion, e);
			resultado = "";
		}
		
		return resultado;
	}
	
	private void mensajeInfo(String mensaje) {
		this.mensajeLog += "\n" + mensaje;
	}

	private void mensajeInfoAppend(String mensaje) {
		this.mensajeLog += mensaje;
	}
	
	private void printLog(String mensaje) {
		log.info("topic/COMPRAS Log\n\n" + this.mensajeLog + "\n\n" + mensaje);
	}
	
	private void printLog(String error, Throwable throwable) {
		log.error("topic/COMPRAS Log\n\n" + this.mensajeLog + "\n\nOcurrio un problema al intentar enviar mensaje al topic/COMPRAS", throwable);
	}
}
