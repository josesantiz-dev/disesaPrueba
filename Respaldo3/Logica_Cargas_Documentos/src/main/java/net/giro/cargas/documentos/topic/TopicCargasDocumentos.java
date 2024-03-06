package net.giro.cargas.documentos.topic;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
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

import net.giro.cargas.documentos.beans.ComprobacionFactura;
import net.giro.cargas.documentos.logica.ComprobacionFacturaRem;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.logica.TopicEstatusRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TiposTopicEstatus;
import net.giro.plataforma.topics.TopicEventosCARGAS;
import net.giro.plataforma.topics.TopicEventosCXP;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import mx.gob.sat.cfdi.consulta.Acuse;
import mx.gob.sat.cfdi.consulta.tempuri.ConsultaCFDIService;
import mx.gob.sat.cfdi.consulta.tempuri.IConsultaCFDIService;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/CARGAS")}, 
	mappedName = "topic/CARGAS")
public class TopicCargasDocumentos implements MessageListener {
	private static Logger log = Logger.getLogger(TopicCargasDocumentos.class);
	private InfoSesion infoSesion;
	private long usuarioId;
	private long empresaId;
	private long aplicacionId;
	// -----------------------------------------------------------------------
	private TopicEstatusRem ifzTopicEstatus;
	private long idTopicEstatus;
	private List<String> mensajeLogs; 
	// -----------------------------------------------------------------------
	private ComprobacionFacturaRem ifzCFDI;
    private NQueryRem ifzQuery;
	
	public TopicCargasDocumentos() {
		InitialContext ctx = null;
		
    	try {
			ctx = new InitialContext();
			this.ifzTopicEstatus = (TopicEstatusRem) ctx.lookup("ejb:/Logica_Publico//TopicEstatusFac!net.giro.plataforma.logica.TopicEstatusRem");
			this.ifzCFDI = (ComprobacionFacturaRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//ComprobacionFacturaFac!net.giro.cargas.documentos.logica.ComprobacionFacturaRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
        } catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
        }
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
		Gson gson = null;
		Type tipo = null;
		TextMessage mensaje = null;
		MensajeTopic mensajeTopic = null;
		TopicEventosCARGAS evento;
    	String jsonString = "";
		String eventoParam = "";
		
    	try {
	    	if (message instanceof TextMessage) {
				mensajeLog("############################## TOPIC CARGAS DOCUMENTOS --- INICIO : " + new Date());
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
				evento = TopicEventosCARGAS.fromString(eventoParam);
				switch (evento) {
					case ACTUALIZAR_DATOS:
						tipo = new TypeToken<HashMap<Long, Double>>() {}.getType();
						bo_actualizarDatos(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), (HashMap<Long, Double>) gson.fromJson(mensajeTopic.getAtributos(), tipo));
						break;
						
					case CFDI_ESTATUS:
						bo_comprobarEstatus(valueToDate(mensajeTopic.getTarget()), valueToDate(mensajeTopic.getReferencia()));
						break;
						
					default:
						mensajeLog("****************************** Evento '" + eventoParam + "' no identificado XD");
						break;
				}
			}
	    	
			printLog();
    	} catch (Exception e) {
        	printLog("Ocurrio un problema al recibir Evento " + this.getClass().getSimpleName() + "\n" + jsonString, e);
		} 
	}

	// ----------------------------------------------------------------------------------------
	// EVENTOS
	// ----------------------------------------------------------------------------------------

	private void bo_actualizarDatos(long idPagosGastos, long idReferencia, HashMap<Long, Double> items) throws Exception {
		try {
			if (items == null)
				items = new HashMap<Long, Double>();
			mensajeLog(" -----> Evento CFDI_ESTATUS: BackOffice CXC - Actualiza el SALDO en facturas");
			if (idPagosGastos <= 0L || idReferencia <= 0L || items.isEmpty()) {
				topicSinAccion("Sin accion por parametros insuficientes ");
				return;
			}
			
		} catch (Exception e) { 
			printLog("Ocurrio un problema al intentar consultar el Estatus del CFDI indicado: ", e);
			throw e;
		}
	}

	private void bo_comprobarEstatus(Date fechaDesde, Date fechaHasta) throws Exception {
		SimpleDateFormat formatter = null;
		List<ComprobacionFactura> listCFDI = null;
		HashMap<Long, String> listXML = null;
		List<Long> sincronizacion = null;
		// -----------------------------------------------
		ConsultaCFDIService consultaService = null;
		IConsultaCFDIService iConsultaService = null;
		Acuse acuse = null;
		
		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			fechaDesde = (fechaDesde == null ? asignarFechaDesde() : fechaDesde);
			fechaHasta = (fechaHasta == null ? asignarFechaHasta() : fechaHasta);
			listCFDI = this.ifzCFDI.findByDates(fechaDesde, fechaHasta);
			listCFDI = (listCFDI != null ? listCFDI : new ArrayList<ComprobacionFactura>());
			mensajeLog(" -----> Evento CFDI_ESTATUS: Validacion de Estatus de CFDI activos");
			mensajeLog(" -----> Periodo   : " + formatter.format(fechaDesde) + " / " + formatter.format(fechaHasta));
			mensajeLog(" -----> CFDIs     : " + listCFDI.size());
			if (listCFDI.isEmpty()) {
				topicSinAccion("Sin accion por parametros insuficientes ");
				return;
			}
			
			mensajeLog(" -----> Comprobando permiso para Validar ... ");
			if (! getPerfilValue("CFDI_VALIDACION_ESTATUS")) {
				mensajeLog("NO PERMITIDO", true);
				topicSinAccion("Validacion de Estatus desactivada");
				return;
			}
			mensajeLog("PERMITIDO", true);
			
			mensajeLog(" -----> Inicializando Servicio ConsultaQR (SAT) ... ");
			listXML = new HashMap<Long, String>();
			sincronizacion = new ArrayList<Long>();
			consultaService = new ConsultaCFDIService();
			iConsultaService = consultaService.getBasicHttpBindingIConsultaCFDIService();
			mensajeLog("OK", true);
			for (ComprobacionFactura cfdi : listCFDI) {
				mensajeLog(" -----> Comprobando " + cfdi.getExpresionImpresa() + " ... ");
				acuse = iConsultaService.consulta(cfdi.getExpresionImpresa());
				if (acuse == null) {
					mensajeLog("SIN COMPROBAR", true);
					continue;
				}
				
				mensajeLog("OK", true);
				sincronizacion.add(cfdi.getId());
				if (! cfdi.getEstado().equals(acuse.getEstado().getValue())) {
					cfdi.setEstado(acuse.getEstado().getValue());
					cfdi.setCodigoEstatus(acuse.getCodigoEstatus().getValue());
					if ("cancelado".equals(cfdi.getEstado().toLowerCase())) {
						listXML.put(cfdi.getId(), cfdi.getFacturaSerie() + "-" + cfdi.getFacturaFolio() + " (" + cfdi.getFacturaFolioFiscal() + ")");
						sincronizacion.remove(cfdi.getId());
					}
				} 
			}
			
			mensajeLog(" -----> Actualizando listado de CFDI comprobados ... ");
			this.ifzCFDI.setInfoSesion(this.infoSesion);
			this.ifzCFDI.saveOrUpdateList(listCFDI);
			mensajeLog("OK", true);
			
			mensajeLog(" -----> Sincronizando Comprobaciones ... ");
			syncComprobacionCFDI(sincronizacion);
			mensajeLog("OK", true);
			
			if (! listXML.isEmpty()) 
				actualizarComprobaciones(listXML);
		} catch (Exception e) { 
			printLog("Ocurrio un problema al intentar consultar el Estatus del CFDI indicado: ", e);
			throw e;
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// METODOS PRIVADOS
	// ----------------------------------------------------------------------------------------

	/*private void autoMessage(TopicEventosCARGAS evento, Object target, Object referencia, Object atributos) {
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

	private void actualizarComprobaciones(HashMap<Long, String> lista) {
		MensajeTopic msgTopic = null;
		String target = "";
		String referencia = "";
		String atributos = "";
		Gson gson = null;
		
		try {
			gson = new Gson();
			lista = (lista != null ? lista : new HashMap<Long, String>());
			atributos = gson.toJson(lista);
			
			msgTopic = new MensajeTopic(TopicEventosCXP.CFDI_ESTATUS, target, referencia, atributos, this.infoSesion);
			if (this.infoSesion == null)
				msgTopic.setInfoSesion(this.usuarioId, this.empresaId, this.aplicacionId);
			msgTopic.enviar();
		} catch (Exception e) { 
			log.error("Ocurrio un problema al autoenviar mensaje JMS\n" + this.getClass().getCanonicalName() + ".actualizarComprobaciones(List<Long> lista)", e);
		} 
	}

	private void syncComprobacionCFDI(List<Long> lista) {
		String queryString = "";
		String valor = "";
		
		try {
			if (lista == null || lista.isEmpty())
				return;
			valor = StringUtils.join(lista, ",");
			queryString = "update pagos_gastos_det a set xml_estatus = case x.ai when 'Vigente' then 1 when 'Cancelado' then 2 else 0 end from c66ff17ccc x where x.aa = a.id_xml and x.aa in (:lista);";
			queryString = queryString.replace(":lista", StringUtils.join(lista, ","));
			this.ifzQuery.ejecutaAccion(queryString);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar sincronizar las Comprobaciones con los CFDI verificados: " + valor, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean getPerfilValue(String perfilName) {
		List<String> nativeResult = null;
		String queryString = "";
		String valor = "";
		
		try {
			queryString += "select b.ai from d7729f32ba7 a inner join b761110ccfe b on b.af = a.aa where a.af = ':perfilName' ";
			queryString = queryString.replace(":perfilName", perfilName);
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty())
				valor = nativeResult.get(0);
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Perfil indicado: " + perfilName, e);
		}
		
		return valor.equals("S");
	}
	
	private long valueToLong(Object value) {
		return ((! "".equals(valueToString(value))) ? Long.valueOf(valueToString(value)) : 0L);
	}
	
	private String valueToString(Object value) {
		return (value != null ? value.toString().trim() : "");
	}

	private Date valueToDate(Object value) {
		return ((! "".equals(valueToString(value))) ? (Date) value : null);
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

	private Date asignarFechaDesde() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(Calendar.getInstance().getTime());
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	private Date asignarFechaHasta() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(Calendar.getInstance().getTime());
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	private void mensajeLog(String mensaje) {
		mensajeLog(mensaje, false);
	}
	
	private void mensajeLog(String mensaje, boolean appendMessage) {
		if (this.mensajeLogs == null)
			this.mensajeLogs = new ArrayList<String>();
		
		// Actualizo mensaje, si corresponde
		if (! appendMessage) {
			this.mensajeLogs.add(mensaje);
			return;
		}

		mensaje = (this.mensajeLogs.get(this.mensajeLogs.size() - 1) + mensaje);
		this.mensajeLogs.set((this.mensajeLogs.size() - 1), mensaje);
	}
	
	private void printLog() {
		printLog("##############################    TOPIC CARGAS DOCUMENTOS    ##############################");
	}

	private void printLog(String mensaje) {
		try {
			mensajeLog(mensaje);
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.info("TOPIC/CARGAS Log\n\n" + mensaje);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Terminado, "OK\n" + mensaje);
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/CARGAS. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}

	private void printLog(String mensaje, Throwable throwable) {
		try {
			if (mensaje != null && ! "".equals(mensaje.trim()))
				mensajeLog(mensaje);
			mensajeLog("############################## TOPIC CARGAS DOCUMENTOS --- FIN : " + new Date());
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.error("TOPIC/CARGAS Log\n\n" + mensaje + "\nEXCEPTION\n", throwable);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Error, mensaje + "\nEXCEPTION\n" + throwable.getMessage());
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/CARGAS. TopicEstatus: " + this.idTopicEstatus, e);
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
