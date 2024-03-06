package net.giro.cxp.topic;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
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

import net.giro.clientes.beans.Negocio;
import net.giro.clientes.logica.NegociosRem;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.logica.PagosGastosDetRem;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.logica.TopicEstatusRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TiposTopicEstatus;
import net.giro.plataforma.topics.TopicEventosCXP;
import net.giro.respuesta.Respuesta;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/CXP")}, 
	mappedName = "topic/CXP")
public class TopicCuentasPorPagar implements MessageListener {
	private static Logger log = Logger.getLogger(TopicCuentasPorPagar.class);
	private InfoSesion infoSesion;
	private long usuarioId;
	private long empresaId;
	private long aplicacionId;
	// ----------------------------------------------
	private TopicEstatusRem ifzTopicEstatus;
	private long idTopicEstatus;
	private List<String> mensajeLogs;
	// ----------------------------------------------
	private PagosGastosRem ifzPagosGastos; 
	private PagosGastosDetRem ifzPagosGastosDet;
	private NegociosRem ifzNegocios;
	
	public TopicCuentasPorPagar() {
		InitialContext ctx = null;
		
    	try {
			ctx = new InitialContext();
			this.ifzTopicEstatus = (TopicEstatusRem) ctx.lookup("ejb:/Logica_Publico//TopicEstatusFac!net.giro.plataforma.logica.TopicEstatusRem");
	   		this.ifzPagosGastos = (PagosGastosRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
	   		this.ifzPagosGastosDet = (PagosGastosDetRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
	   		this.ifzNegocios = (NegociosRem) ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
        } catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
        }
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		HashMap<String, Object> hm = new HashMap<String, Object>();
		MensajeTopic mensajeTopic = null;
		TextMessage mensaje = null;
		TopicEventosCXP evento;
		Gson gson = null;
		Type tipo = null;
		// ------------------------------------------------------
		String eventoParam = "";
		
    	try {
	    	if (message instanceof TextMessage) {
				mensajeLog("############################## TOPIC CXP --- INICIO : " + new Date());
	    		gson = new Gson();
				// Transformamos mensaje
				mensaje = (TextMessage) message;
				mensajeTopic = gson.fromJson(mensaje.getText(), MensajeTopic.class);
				hm = gson.fromJson(mensaje.getText(), HashMap.class);
				
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
				evento = TopicEventosCXP.fromString(eventoParam);
				switch (evento) {
					case SALDO:
						tipo = new TypeToken<HashMap<Long, Double>>() {}.getType();
						bo_saldo(valueToLong(hm.get("target")), valueToLong(hm.get("referencia")), (HashMap<Long, Double>) (new Gson()).fromJson(hm.get("atributos").toString().trim(), tipo));
						break;
					
					case PROVISION: 
						tipo = new TypeToken<HashMap<Long, Double>>() {}.getType();
						bo_provision(valueToLong(hm.get("target")), valueToLong(hm.get("referencia")), (HashMap<Long, Double>) (new Gson()).fromJson(hm.get("atributos").toString().trim(), tipo));
						break;
					
					case CANCELACION: 
						tipo = new TypeToken<HashMap<Long, Double>>() {}.getType();
						bo_cancelacion(valueToLong(hm.get("target")), valueToLong(hm.get("referencia")), (HashMap<Long, Double>) (new Gson()).fromJson(hm.get("atributos").toString().trim(), tipo));
						break;
						
					case CFDI_ESTATUS: 
						tipo = new TypeToken<HashMap<Long, String>>() {}.getType();
						bo_estatusCFDI((HashMap<Long, String>) (new Gson()).fromJson(hm.get("atributos").toString().trim(), tipo));
						break;
						
					case TRANSACCION:
						bo_transaccion(valueToLong(hm.get("target")));
						break;
						
					case PROVEEDOR:
						tipo = new TypeToken<HashMap<String, String>>() {}.getType();
						bo_proveedor(valueToString(hm.get("target")), (HashMap<String, String>) (new Gson()).fromJson(hm.get("atributos").toString().trim(), tipo));
						break;
					
					default:
						mensajeLog("****************************** Evento '" + eventoParam + "' no identificado XD");
						break;
				}
			}
	    	
			printLog();
    	} catch (Exception e) {
			printLog("Ocurrio un problema al procesar el mensaje para Topic CXP", e);
		}
	}

	// ----------------------------------------------------------------------------------------
	// EVENTOS
	// ----------------------------------------------------------------------------------------

	private void bo_saldo(long idPagosGastos, long idReferencia, HashMap<Long, Double> items) throws Exception { }

	private void bo_provision(long idPagosGastos, long idReferencia, HashMap<Long, Double> items) throws Exception { }

	private void bo_cancelacion(long idPagosGastos, long idReferencia, HashMap<Long, Double> items) throws Exception { }

	private void bo_estatusCFDI(HashMap<Long, String> listCFDI) throws Exception {
		List<PagosGastosDet> comprobaciones = null;
		// ------------------------------------------------------------------------------
		HashMap<Long, String> cajasChicas = null;
		HashMap<Long, String> regEgresos = null;
		HashMap<Long, String> provisiones = null;
		HashMap<Long, String> desconocidos = null;
		SimpleDateFormat formatter = null;
		String formatCC = "Caja Chica #:numero (:id) del :fecha, :beneficiario";
		String formatRE = "Reg. Egreso :id del :fecha, :proveedor (:rfc)";
		String formatPF = "Provision :id del :fecha, :proveedor (:rfc)";
		String formatXX = "Desconocido :id del :fecha";
		String descripcion = "";
		// ------------------------------------------------------------------------------
		String body = "";
		
		try {
			listCFDI = (listCFDI != null ? listCFDI : new HashMap<Long, String>());
			mensajeLog(" -----> Evento CFDI_ESTATUS: Actualizacion de Estatus de CFDI activos");
			mensajeLog(" -----> CFDIs     : " + listCFDI.size());
			if (listCFDI.isEmpty()) {
				topicSinAccion("Sin accion por parametros insuficientes ");
				return;
			}
			
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			for (Entry<Long,String> cfdi : listCFDI.entrySet()) {
				comprobaciones = this.ifzPagosGastosDet.findByFactura(cfdi.getKey());
				if (comprobaciones == null || comprobaciones.isEmpty()) {
					mensajeLog("CFDI (XML) " + cfdi.getKey() + " sin comprobaciones");
					continue;
				}

				cajasChicas = new HashMap<Long, String>();
				regEgresos = new HashMap<Long, String>();
				provisiones = new HashMap<Long, String>();
				desconocidos = new HashMap<Long, String>();
				for (PagosGastosDet comprobacion : comprobaciones) {
					comprobacion.setEstatusCfdi(2);
					switch (comprobacion.getIdPagosGastos().getTipo()) {
						case "C" : 
							descripcion = formatCC.replace(":numero", String.valueOf(comprobacion.getIdPagosGastos().getConsecutivo())).replace(":id", comprobacion.getIdPagosGastos().getId().toString()).replace(":fecha", formatter.format(comprobacion.getIdPagosGastos().getFecha())).replace(":beneficiario", comprobacion.getIdPagosGastos().getBeneficiario().toUpperCase());
							cajasChicas.put(comprobacion.getIdPagosGastos().getId(), descripcion);
							break;
							
						case "P" : 
							descripcion = formatRE.replace(":id", String.valueOf(comprobacion.getIdPagosGastos().getId().toString())).replace(":fecha", formatter.format(comprobacion.getIdPagosGastos().getFecha())).replace(":proveedor", comprobacion.getNombreProveedor()).replace(":rfc", (comprobacion.getRfcProveedor() != null && ! "".equals(comprobacion.getRfcProveedor().trim()) ? comprobacion.getRfcProveedor() : "N/A"));
							regEgresos.put(comprobacion.getIdPagosGastos().getId(), descripcion);
							break;
							
						case "F" : 
							descripcion = formatPF.replace(":id", String.valueOf(comprobacion.getIdPagosGastos().getId().toString())).replace(":fecha", formatter.format(comprobacion.getIdPagosGastos().getFecha())).replace(":proveedor", comprobacion.getNombreProveedor()).replace(":rfc", (comprobacion.getRfcProveedor() != null && ! "".equals(comprobacion.getRfcProveedor().trim()) ? comprobacion.getRfcProveedor() : "N/A"));
							provisiones.put(comprobacion.getIdPagosGastos().getId(), descripcion);
							break;
							
						default : 
							descripcion = formatXX.replace(":id", String.valueOf(comprobacion.getIdPagosGastos().getId().toString())).replace(":fecha", formatter.format(comprobacion.getIdPagosGastos().getFecha()));
							desconocidos.put(comprobacion.getIdPagosGastos().getId(), descripcion);
							break;
					}
				}
				
				this.ifzPagosGastosDet.setInfoSesion(this.infoSesion);
				comprobaciones = this.ifzPagosGastosDet.saveOrUpdateList(comprobaciones);
				body += "\n\nFactura " + cfdi.getValue() + ":";
				for (Entry<Long, String> cajaChica : cajasChicas.entrySet())
					body += "\n    " + cajaChica.getValue();
				for (Entry<Long, String> regEgreso : regEgresos.entrySet())
					body += "\n    " + regEgreso.getValue();
				for (Entry<Long, String> provision : provisiones.entrySet())
					body += "\n    " + provision.getValue();
				for (Entry<Long, String> desconocido : desconocidos.entrySet())
					body += "\n    " + desconocido.getValue();
			}
			
			// Construimos BODY
			body = "Cuentas por Pagar.\n\nLas siguientes Facturas cambiaron su estatus a CANCELADO:" + body + "\n\n\nReferencia " + this.idTopicEstatus;
			
			// Enviamos Notificacion
			this.ifzTopicEstatus.enviarCorreo("kmartinez@grupodisesa.com.mx, auxcontable@grupodisesa.com.mx", "", "ftirado.disesa@gmail.com", "AIR - Estatus CFDI en CXP", body, null);
		} catch (Exception e) { 
			printLog("Ocurrio un problema al intentar actualizar el Estatus del CFDI indicado", e);
			throw e;
		}
	}
	
	private void bo_transaccion(long idPagosGastos) throws Exception {
		Respuesta respuesta = null;
		PagosGastos entity = null;

		try {
			idPagosGastos = (idPagosGastos > 0L ? idPagosGastos : 0L);
			mensajeLog(">>>>>>>>>>> BackOffice CXP ");
			mensajeLog("*********** TRANSACCION PagosGastos ");
			mensajeLog("*********** idPagosGastos : " + idPagosGastos);
			if (idPagosGastos <= 0L) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}
			
			// Recupero Movimiento
			this.ifzPagosGastos.setInfoSesion(this.infoSesion);
			entity = this.ifzPagosGastos.findById(idPagosGastos);
			if (entity == null || entity.getId() == null || entity.getId() <= 0L)
				throw new Exception("PagosGastos no encontrado :( ");
			
			if (! "F".equals(entity.getTipo())) {
				// Validaciones Caja Chica, Registro Egresos, Movimientos Cuentas, Gastos a Comprobar
				if (entity.getOperacion() == null || "".equals(entity.getOperacion().trim())) {
					topicSinAccion("Caja Chica incompleta para Contabilizador: Falta Operacion");
					return;
				}
				
				if (entity.getIdCuentaOrigen() == null || entity.getIdCuentaOrigen() <= 0L) {
					topicSinAccion("Caja Chica incompleta para Contabilizador: Falta Cuenta Origen");
					return;
				}
			} else if ("F".equals(entity.getTipo())) {
				// Validaciones Provisiones
			} else  {
				topicSinAccion("No se pudo determinar el TIPO del PagosGastos indicado: " + idPagosGastos);
				return;
			}
			
			this.ifzPagosGastos.setInfoSesion(this.infoSesion);
			respuesta = this.ifzPagosGastos.enviarTransaccion(entity);
			if (respuesta.getErrores().getCodigoError() != 0L) 
				topicSinAccion(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
		} catch (Exception e) {
			printLog("Ocurrio un problema al procesar el BackOffice para TRANSACCION del PagosGastos indicado. PagosGastos " + idPagosGastos, e);
			throw e;
		}
	}
	
	private void bo_proveedor(String uniqueValue, HashMap<String, String> params) throws Exception {
		PagosGastosDet comprobacion = null;
		// ---------------------------------
		Respuesta respuesta = null;
		Negocio negocio = null;
		// ---------------------------------
		String nombre = "";
		String rfc = "";
		String tipoPersona = "";
		String tipoPersonalidad = "";
		// ---------------------------------
		String asunto = "";
		String body = "";
		
		try {
			params = (params != null && ! params.isEmpty() ? params : new HashMap<String, String>());
			mensajeLog(">>>>>>>>>>> BackOffice CXP ");
			mensajeLog("*********** Nuevo PROVEEDOR (Negocio) ");
			mensajeLog("*********** uniqueValue   : " + uniqueValue);
			mensajeLog("*********** params        : " + params.toString());
			if (params.isEmpty()) {
				topicSinAccion("Sin accion por Parametros insuficientes ");
				return;
			}

			asunto = "AIR - Nuevo PROVEEDOR (Negocio) en CXP";
			body += "Cuentas por Pagar.\n\nNuevo registro para Proveedor (negocio) desde CXP:\n\n    Comprobacion : " + uniqueValue + "\n\n    Parametros   : " + params.toString();
			body += "\n\n\nReferencia " + this.idTopicEstatus;
			this.ifzTopicEstatus.enviarCorreo("ftirado.disesa@gmail.com", asunto, body, null);
			
			uniqueValue = (uniqueValue != null ? uniqueValue.trim() : "");
			nombre = params.get("nombre");
			rfc = params.get("rfc");
			tipoPersona = params.get("tipoPersona");
			tipoPersonalidad = ("N".equals(tipoPersona)) ? "M" : "F";
			
			negocio = new Negocio();
			negocio.setId(0L);
			negocio.setRfc(rfc);
			negocio.setNombre(nombre);
			negocio.setTipoPersonalidad(tipoPersonalidad);
			negocio.setCreadoPor(1L);
			negocio.setFechaCreacion(Calendar.getInstance().getTime());
			negocio.setModificadoPor(1L);
			negocio.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.ifzNegocios.setInfoSesion(this.infoSesion);
			respuesta = this.ifzNegocios.salvarNegocio(negocio);
			if (respuesta.getErrores().getCodigoError() != 0L && respuesta.getErrores().getCodigoError() != 115L) {
				mensajeLog("Ocurrio un problema al guardar el Negocio indicado: " + rfc);
				return;
			}
			
			negocio = (Negocio) respuesta.getBody().getValor("pojoNegocio");
			if (negocio == null) {
				mensajeLog("Ocurrio un problema al recuperar el Negocio indicado: " + rfc);
				return;
			}
			
			if ("".equals(uniqueValue)) {
				topicSinAccion("Sin Referencia de Comprobacion para actualizar: uniqueValue");
				return;
			}

			this.ifzPagosGastosDet.setInfoSesion(this.infoSesion);
			comprobacion = this.ifzPagosGastosDet.findByUniqueValue(uniqueValue);
			if (comprobacion == null) {
				topicSinAccion("No se encontro ninguna Comprobacion con la referencia (uniqueValue) indicada: " + uniqueValue);
				return;
			}
			
			comprobacion.setIdProveedor(negocio.getId());
			comprobacion.setTipoPersonaProveedor(tipoPersonalidad);
			comprobacion.setNombreProveedor(negocio.getNombre());
			comprobacion.setRfcProveedor(negocio.getRfc());

			this.ifzPagosGastosDet.setInfoSesion(this.infoSesion);
			this.ifzPagosGastosDet.update(comprobacion);
		} catch (Exception e) {
			printLog("Ocurrio un problema al Guardar/Actualizar el Proveedor indicado: " + rfc, e);
			throw e;
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// METODOS PRIVADOS
	// ----------------------------------------------------------------------------------------

	/*private void autoMessage(TopicEventosCXP evento, Object target, Object referencia, Object atributos) {
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
	}*/

	private long valueToLong(Object value) {
		return ((! "".equals(valueToString(value))) ? Long.valueOf(valueToString(value)) : 0L);
	}
	
	private String valueToString(Object value) {
		return (value != null ? value.toString().trim() : "");
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
		printLog("############################## TOPIC CXP --- FIN : " + new Date());
	}

	private void printLog(String mensaje) {
		try {
			mensajeLog(mensaje);
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.info("TOPIC/INVENTARIOS Log\n\n" + mensaje);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Terminado, "OK\n" + mensaje);
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/CXP. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}
	
	private void printLog(String mensaje, Throwable throwable) {
		try {
			if (mensaje != null && ! "".equals(mensaje.trim()))
				mensajeLog(mensaje);
			mensajeLog("############################## TOPIC CXP --- FIN : " + new Date());
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.error("TOPIC/INVENTARIOS Log\n\n" + mensaje + "\nEXCEPTION\n", throwable);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Error, mensaje + "\nEXCEPTION\n" + throwable.getMessage());
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/CXP. TopicEstatus: " + this.idTopicEstatus, e);
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
