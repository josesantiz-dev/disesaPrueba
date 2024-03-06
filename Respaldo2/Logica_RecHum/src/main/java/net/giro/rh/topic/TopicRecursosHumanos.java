package net.giro.rh.topic;

import java.util.Date;
import java.util.HashMap;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/RECHUM")}, 
	mappedName = "topic/RECHUM")
public class TopicRecursosHumanos implements MessageListener {
	private static Logger log = Logger.getLogger(TopicRecursosHumanos.class);
	//private InitialContext ctx;
	// ----------------------------------------------
	//private EmpleadoFac ifzEmpleados;
	//private ObraEmpleadoRem ifzOEmpleados;
	/*private OrdenCompraRem ifzOC;
	private OrdenCompraDetalleRem ifzOCDetalle;
	private CotizacionRem ifzCot;
	private CotizacionDetalleRem ifzCotDetalle;
	private RequisicionRem ifzReq;
	private RequisicionDetalleRem ifzReqDetalle;*/
	// ----------------------------------------------
	private String mensajeLog;
	
	
	public TopicRecursosHumanos() {
		try {
			//this.ctx = new InitialContext();
			/*this.ifzOC = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOCDetalle = (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzCot = (CotizacionRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzCotDetalle = (CotizacionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzReq = (RequisicionRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalle = (RequisicionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			*/
			//this.ifzEmpleados = new EmpleadoFac();
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar TopicCompras <-> topic/COMPRAS", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		Gson gson = new Gson();
		TextMessage mensaje = null;
		//Type tipo = null;
		// ------------------------------------------------------
		String evento = "";
		long target = 0L;
		//long referencia = 0L;
		
    	try {
			mensajeInfo("##############################    TOPIC RECURSOS HUMANOS    ##############################");
			mensajeInfo("****************************** " + new Date() + " ******************************");
	    	if (message instanceof TextMessage) {
				// Transformamos mensaje
				mensaje = (TextMessage) message;
				hm = gson.fromJson(mensaje.getText(), HashMap.class);
				
				// Recuperamos datos del mensaje
				evento = hm.get("evento").toString().trim().toUpperCase();
				target = Long.valueOf(hm.get("target").toString());

				// Lanzamos evento requerido
				switch (evento) {
					case "EMBA": // Baja de Empleado
						mensajeInfo(" -----> Evento EMBA: BackOffice Empleados");
						mensajeInfo(" -----> Baja de Empleado : " + target);
						if (target <= 0L) {
							mensajeInfo(" -----> Sin accion: Parametros insuficientes ");
							break;
						}
						
						// lanzamos back office correspondiente
						bajaEmpleado(target);
						break;
					default:
						mensajeInfo(" -----> Evento '" + evento + "' NO REGISTRADO :(");
						mensajeInfo(" -----> Sin accion: No se reconoce el evento ");
						break;
				}
			}
    	} catch (Exception e) {
			printLog("Ocurrio un problema al procesar el mensaje para TOPIC RECURSOS HUMANOS", e);
		} finally {
			printLog();
		}
	}

	// ----------------------------------------------------------------------------------------
	// EVENTOS
	// ----------------------------------------------------------------------------------------
	
	private void bajaEmpleado(long idEmpleado) {
		
	}

	// ----------------------------------------------------------------------------------------
	// METODOS PRIVADOS
	// ----------------------------------------------------------------------------------------

	private void mensajeInfo(String mensaje) {
		mensajeInfo(mensaje, false);
	}

	private void mensajeInfo(String mensaje, boolean appendMessage) {
		this.mensajeLog += (appendMessage ? "" : "\n") + mensaje;
	}
	
	private void printLog() {
		printLog("", null);
	}
	
	private void printLog(String error, Throwable throwable) {
		if (this.mensajeLog == null || "".equals(this.mensajeLog.trim()))
			return;

		if (error != null && ! "".equals(error.trim()))
			this.mensajeLog += "\n\n" + error;
		this.mensajeLog += "\n\n############################## TOPIC RECURSOS HUMANOS end at " + new Date();
		log.error(this.mensajeLog, throwable);
		this.mensajeLog = "";
	}
}
