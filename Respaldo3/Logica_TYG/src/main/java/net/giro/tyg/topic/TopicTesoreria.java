package net.giro.tyg.topic;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import mx.org.banxico.dgie.ws.DgieWSPortProxy;
import mx.org.banxico.sie.api.DataSerie;
import mx.org.banxico.sie.api.Response;
import mx.org.banxico.sie.api.ResponseStatus;
import mx.org.banxico.sie.api.Serie;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.logica.TopicEstatusRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TiposTopicEstatus;
import net.giro.plataforma.topics.TopicEventosTYG;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.beans.DomParser;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.dao.MonedasValoresDAO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@MessageDriven(
	activationConfig = { 
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"), 
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/TYG")}, 
	mappedName = "topic/TYG")
public class TopicTesoreria implements MessageListener {
	private static Logger log = Logger.getLogger(TopicTesoreria.class);
	private InfoSesion infoSesion;
	private long usuarioId;
	private long empresaId;
	private long aplicacionId;
	// -------------------------------------------------------------------------------------------------------------------
	private TopicEstatusRem ifzTopicEstatus;
	private long idTopicEstatus;
	private List<String> mensajeLogs;
	// -------------------------------------------------------------------------------------------------------------------
	private MonedaDAO ifzMonedas;
	private MonedasValoresDAO ifzMonValores;
	
	public TopicTesoreria() {
		InitialContext ctx = null;
		
    	try {
			ctx = new InitialContext();
			this.ifzTopicEstatus = (TopicEstatusRem) ctx.lookup("ejb:/Logica_Publico//TopicEstatusFac!net.giro.plataforma.logica.TopicEstatusRem");
			this.ifzMonedas = (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzMonValores = (MonedasValoresDAO) ctx.lookup("ejb:/Model_TYG//MonedasValoresImp!net.giro.tyg.dao.MonedasValoresDAO");
        } catch (Exception e) {
        	log.error("Error al crear TopicCuentasPorCobrar", e);
        }
	}
	
	@Override
	public void onMessage(Message message) {
		MensajeTopic mensajeTopic = null;
		TextMessage mensaje = null;
		TopicEventosTYG evento = null;
		String eventoParam = "";
		// ------------------------------------------------------
		Type tipo = null;
		HashMap<Long, Double> atributos = null;
		HashMap<String, String> parametros = null;
		Gson gson = null;
		
    	try {
    		mensajeLog("****************************** TOPIC TYG --- INICIO : " + new Date() + "\n");
	    	if (message instanceof TextMessage) {
				// Transformamos mensaje
	    		gson = new Gson();
				mensaje = (TextMessage) message;
				mensajeTopic = gson.fromJson(mensaje.getText(), MensajeTopic.class);
				this.infoSesion = mensajeTopic.getInfoSesion();
				setInfoSesion(mensajeTopic.getUsuario(), mensajeTopic.getEmpresa(), mensajeTopic.getAplicacion(), null);

				// Recuperamos valores 
				eventoParam = mensajeTopic.getEvento();
				this.idTopicEstatus = (mensajeTopic.getIdTopicEstatus() != null && mensajeTopic.getIdTopicEstatus() > 0L) ? mensajeTopic.getIdTopicEstatus() : 0L;
				if (this.idTopicEstatus <= 0L)
					topicRegistar(eventoParam, mensaje.getText().trim());
				mensajeLog(" --- ID " + this.idTopicEstatus, true);
				Thread.sleep(1000);
				
				// Lanzamos evento requerido
				evento = TopicEventosTYG.fromString(eventoParam);
				switch (evento) {
					case SALDO_CUENTAS:
						tipo = new TypeToken<HashMap<Long, Double>>() {}.getType();
						atributos = new Gson().fromJson(mensajeTopic.getAtributos().trim(), tipo);
						bo_saldoCuentas(valueToLong(mensajeTopic.getTarget()), valueToLong(mensajeTopic.getReferencia()), atributos);
						break;
					
					case TIPO_CAMBIO: 
						tipo = new TypeToken<HashMap<String, String>>() {}.getType();
						parametros = new Gson().fromJson(mensajeTopic.getAtributos().trim(), tipo);
						bo_tipoCambioAPI(valueToString(mensajeTopic.getTarget()), valueToString(mensajeTopic.getReferencia()), parametros);
						break;
					
					default:
						mensajeLog("****************************** Evento '" + eventoParam + "' no identificado XD");
						break;
				}
			}

			printLog();
    	} catch (Exception e) {
        	printLog("Ocurrio un problema al recibir Evento " + this.getClass().getSimpleName() + "\n" + eventoParam, e);
		} 
	}
	
	// -------------------------------------------------------------------------------------------------------------------
	// EVENTOS 
	// -------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Metodo para actualizar los saldos de las Cuentas Bancarias
	 * @param target
	 * @param referencia
	 * @param atributos
	 * @throws Exception
	 */
	private void bo_saldoCuentas(Long target, Long referencia, HashMap<Long, Double> atributos) throws Exception {
		boolean mailEnviar = false;
		String mailAsunto = "";
		String mailException = "";
		
		try {
			if (atributos == null)
				atributos = new HashMap<Long, Double>();
			mensajeLog(">>>>>>>>>>> BackOffice Saldo de Cuentas *******************");
			mensajeLog("*********** target     : " + target);
			mensajeLog("*********** referencia : " + referencia);
			mensajeLog("*********** atributos  : " + atributos.size());
			
			if (target <= 0L || referencia <= 0L || atributos.isEmpty()) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}

			mailAsunto = "AIR - Saldo Cuentas CXP";
			topicSinAccion("Metodo no Implementado");
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el BackOffice Saldo de Cuentas", e);
			mailException = "EXCEPION: " + e.getMessage();
			mailEnviar = true;
			throw e;
		} finally {
			if (mailEnviar) 
				this.ifzTopicEstatus.enviarCorreo("ftirado.disesa@gmail.com", mailAsunto, mailException + "\n" + StringUtils.join(this.mensajeLogs, "\n"), null);
		}
	}
	
	/**
	 * Metodo para recuperar el tipo de cambio desde BANXICO
	 * @param moneda Abreviacion moneda extranjera (Ej. USD)
	 * @param monedaBase Abreviacion moneda destino (Ej. MXN)
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private void bo_tipoCambio(String moneda, String monedaBase) throws Exception {
		HashMap<String, Double> monedas = new HashMap<String, Double>();
		MonedasValores resultado = null;
		Moneda mDestino = new Moneda();
		Moneda mBase = new Moneda();
		Double valor = 1.0;
		String xmlBanxico = "";
		DgieWSPortProxy proxy = null;
		DomParser dpe = null;
		
		try {
			moneda = (moneda != null ? moneda : "");
			monedaBase = (monedaBase != null ? monedaBase : "");
			mensajeLog(">>>>>>>>>>> BackOffice Tipo de Cambio *******************");
			mensajeLog("*********** Moneda      : " + moneda);
			mensajeLog("*********** Moneda Base : " + monedaBase);
			if ("".equals(moneda.trim()) || "".equals(monedaBase.trim())) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			mensajeLog("Comprobando Tipo de Cambio ... ");
			mBase = this.ifzMonedas.findByAbreviacion(monedaBase);
			mDestino = this.ifzMonedas.findByAbreviacion(moneda);
			resultado = this.ifzMonValores.findActual(mBase, mDestino);
			if (resultado != null) {
				mensajeLog("OK", true);
				topicSinAccion("Tipo de Cambio al dia: " + resultado.getValor());
				return;
			}
			
			// Obtenemos el tipo de cambio: BANXICO
			log.info("     ###### --- --- Consultando Tipo de Cambio en BANXICO");
			try {
				mensajeLog("Inicializando WS de BANXICO ... ");
				proxy = new DgieWSPortProxy();
				dpe = new DomParser();
				mensajeLog("OK", true);
			} catch (Exception e) {
				log.error("Error al inicializar WS de banxico :(", e);
				throw e;
			}
			
			try {
				mensajeLog("Consultando Tipo de Cambio en BANXICO ... ");
				xmlBanxico = proxy.tiposDeCambioBanxico();
				dpe.runExample(xmlBanxico);
				monedas = dpe.getMonedas();
				valor = monedas.get("PesoxDoll");
				mensajeLog("OK", true);
				mensajeLog("Tipo de Cambio obtenido: " + valor);
			} catch (Exception e) {
				log.error("Error al consultar el Tipo de Cambio :(", e);
				throw e;
			} 
			
			// Actualizamos el tipo de cambio
			mensajeLog("Guardando Tipo de Cambio ... ");
			resultado = new MonedasValores();
			resultado.setMonedaBase(mBase);
			resultado.setMonedaDestino(mDestino);
			resultado.setFechaDesde(Calendar.getInstance().getTime());
			resultado.setFechaHasta(Calendar.getInstance().getTime());
			resultado.setValor(new BigDecimal(valor));
			resultado.setCreadoPor(1L);
			resultado.setFechaCreacion(Calendar.getInstance().getTime());
			resultado.setModificadoPor(1L);
			resultado.setFechaModificacion(Calendar.getInstance().getTime());
			resultado.setId(this.ifzMonValores.save(resultado, null));
			mensajeLog("OK", true);
		} catch (Exception e) {
			mensajeLog("ERROR", true);
			log.error("Ocurrio un problema al procesar el BackOffice Tipo de Cambio", e);
			throw e;
		}
	}

	/**
	 * Metodo para recuperar el tipo de cambio desde BANXICO
	 * @param moneda Abreviacion moneda extranjera (Ej. USD)
	 * @param monedaBase Abreviacion moneda destino (Ej. MXN)
	 * @throws Exception
	 */
	private void bo_tipoCambioAPI(String moneda, String monedaBase, HashMap<String, String> params) throws Exception {
		Response response = null;
		Date fecha = null;
		Double valor = 1.0;
		// -------------------------------
		MonedasValores resultado = null;
		Moneda mDestino = new Moneda();
		Moneda mBase = new Moneda();
		// -------------------------------
		String token = ""; // Token BANXICO API
		String serie = ""; // Serie para Tipo de Cambio
		String datos = "";
		// -------------------------------------------------------------------------------------------------------------------
		boolean mailEnviar = false;
		String mailException = "";

		try {
			moneda = (moneda != null ? moneda : "");
			monedaBase = (monedaBase != null ? monedaBase : "");
			mensajeLog(">>>>>>>>>>> BackOffice Tipo de Cambio *******************");
			mensajeLog("*********** Moneda      : " + moneda);
			mensajeLog("*********** Moneda Base : " + monedaBase);
			if ("".equals(moneda.trim()) || "".equals(monedaBase.trim())) {
				topicSinAccion("Parametros insuficientes ");
				return;
			}
			
			// Parametros para el servicio y monedas
			mensajeLog("Inicializando parametros del servicio ... ");
			params = (params == null || params.isEmpty()) ? tipoCambioScheduled() : params;
			token = (params.containsKey("token") ? params.get("token") : "");
			serie = (params.containsKey("serie") ? params.get("serie") : "");
			datos = (params.containsKey("datos") ? params.get("datos") : "");
			mBase = this.ifzMonedas.findByAbreviacion(monedaBase);
			mDestino = this.ifzMonedas.findByAbreviacion(moneda);
			mensajeLog("OK", true);

			// Comprobamos tipo de cambio si corresponde
			if ("oportuno".equals(datos)) {
				mensajeLog("Comprobando Tipo de Cambio ... ");
				resultado = this.ifzMonValores.findActual(mBase, mDestino);
				if (resultado != null) {
					mensajeLog("OK", true);
					topicSinAccion("Tipo de Cambio al dia: " + resultado.getValor());
					return;
				}
			}

			// Validamos token
			mensajeLog("Validando Token ... ");
			if (! validarToken(token)) {
				mensajeLog("ERROR", true);
				mensajeLog("TOKEN No valido: " + token);
				mailEnviar = true;
				return;
			}
			
			// Consumimos servicio
			mensajeLog("Recuperando Tipo de Cambio ... ");
			response = readSeries(token, serie, datos);
			if (response == null || response.getBmx() == null || response.getBmx().getSeries() == null || response.getBmx().getSeries().isEmpty()) 
				throw new Exception("Sin resultados para los parametros indicados: " + params.toString());
			
			// Recuperando Tipo(s) de Cambio(s) recuperamos
			for (Serie bmxSerie : response.getBmx().getSeries()) {
				if (bmxSerie.getDatos() == null || bmxSerie.getDatos().isEmpty()) {
					mensajeLog("ERROR", true);
					mensajeLog("La Serie no devolvio resultados: " + bmxSerie);
					return;
				}
				
				for (DataSerie data : bmxSerie.getDatos()) {
					if (data.getDato().equals("N/E")) 
						continue;
					
					mensajeLog("Recuperamos datos de Serie ... ");
					fecha = (new SimpleDateFormat("dd/MM/yyyy")).parse(data.getFecha());
					if ("oportuno".equals(datos))
						fecha = Calendar.getInstance().getTime();
					valor = Double.parseDouble(data.getDato());
					mensajeLog("OK", true);
					
					// Actualizamos el tipo de cambio
					mensajeLog("Guardando Tipo de Cambio : " + data.getFecha() + " - " + data.getDato() + " ... ");
					resultado = new MonedasValores();
					resultado.setMonedaBase(mBase);
					resultado.setMonedaDestino(mDestino);
					resultado.setFechaDesde(fecha);
					resultado.setFechaHasta(fecha);
					resultado.setValor(new BigDecimal(valor));
					resultado.setCreadoPor(1L);
					resultado.setFechaCreacion(Calendar.getInstance().getTime());
					resultado.setModificadoPor(1L);
					resultado.setFechaModificacion(Calendar.getInstance().getTime());
					resultado.setId(this.ifzMonValores.save(resultado, null));
					mensajeLog("OK", true);
				}
			}

			mailEnviar = false;
		} catch (Exception e) {
			mensajeLog("ERROR", true);
			log.error("Ocurrio un problema al procesar el BackOffice Tipo de Cambio", e);
			mailException = e.getMessage();
			mailEnviar = true;
			throw e;
		} finally {
			if (mailEnviar) 
				this.ifzTopicEstatus.enviarCorreo("ftirado.disesa@gmail.com", "AIR - Tipo de Cambio", "EXCEPION:\n" + mailException + "\n" + StringUtils.join(this.mensajeLogs, "\n"), null);
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------------
	// METODOS PRIVADOS 
	// -------------------------------------------------------------------------------------------------------------------

	/*private void autoMessage(TopicEventosTYG evento, Object target, Object referencia, Object atributos) {
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

	private boolean validarToken(String token) {
		Gson gson = null;
		String urlServicio = "";
		String respuesta = "";
		ResponseStatus status = null;
		boolean statusVal = false;
		
		try {
			//  Generamos llamada al API y recuperamos respuesta
			urlServicio = "https://www.banxico.org.mx/SieAPIRest/service/sec/token/:token/status";
			urlServicio = urlServicio.replace(":token", token);
			respuesta = banxicoService(urlServicio, token);
			
			// Mapeamos la respuesta
			gson = new Gson();
			status = gson.fromJson(respuesta, ResponseStatus.class);
			statusVal = status.getStatus().getActivoHistorico() && status.getStatus().getActivoOportuno();
		} catch (Exception e) {
			log.error("Ocurrio un problema al consumir el servicio BANXICO. Validar Token: " + token + "\nRequest: " + urlServicio + "\n", e);
			return false;
		} 
		
		return statusVal;
	}
	
	private Response readSeries(String token, String serie, String datos) throws Exception {
		//SimpleDateFormat formatter = null;
		String urlServicio = "";
		// --------------------------------
		Response response = null;
		String respuesta = "";
		Gson gson = null;
		
		try {
			urlServicio = "https://www.banxico.org.mx/SieAPIRest/service/v1/series/:serie/datos/:datos";
			urlServicio = urlServicio.replace(":serie", serie).replace(":datos", datos);
			//formatter = new SimpleDateFormat("yyyy-MM-dd");
			//urlServicio = "https://www.banxico.org.mx/SieAPIRest/service/v1/series/:serie/datos/:fechaDesde/:fechaHasta";
			//urlServicio = urlServicio.replace(":serie", serie).replace(":fechaDesde", formatter.format(fechaDesde)).replace(":fechaHasta", formatter.format(fechaHasta));
			respuesta = banxicoService(urlServicio, token);
			
			// Mapeamos la respuesta
			gson = new Gson();
			response = gson.fromJson(respuesta, Response.class);
		} catch (Exception e) {
			log.error("Ocurrio un problema al consumir el servicio BANXICO. Consulta por Serie: " + serie + "\nRequest: " + urlServicio + "\n", e);
			throw e;
		} 
		
		return response;
	}
	
	private String banxicoService(String urlAndParams, String token) throws Exception {
		String defaultProtocols = "";
		HttpURLConnection conn = null;
		URL url = null;
		// -----------------------------------------------------------------
		InputStreamReader read = null;
		StringBuilder sb = null;
		int ch = 0;
		String respuesta = "";

		try {
			defaultProtocols = (System.getProperty("https.protocols") != null ? System.getProperty("https.protocols") : "");
			System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
			
			//La URL a consultar con los parametros de idSerie y fechas 
			url = new URL(urlAndParams);
			conn = (HttpURLConnection) url.openConnection();
			//Se realiza una petición GET
			conn.setRequestMethod("GET");
			//Se solicita que la respuesta esté en formato JSON
			conn.setRequestProperty("Content-Type", "application/json");
			//Se envía el header Bmx-Token con el token de consulta :: Modificar por el token de consulta propio
			if (token != null && ! "".equals(token.trim()))
				conn.setRequestProperty("Bmx-Token", token);
			
			//En caso de ser exitosa la petición se devuelve un estatus HTTP 200
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) 
				throw new RuntimeException("HTTP Error Code : " + conn.getResponseCode() + " - " + conn.getResponseMessage());

			//Se utiliza Jackson para mapear el JSON a objetos Java
			//response = (new ObjectMapper()).readValue(conn.getInputStream(), Response.class);
			
			read = new InputStreamReader(conn.getInputStream());
			sb = new StringBuilder();
			ch = read.read();
			while (ch != -1) {
				sb.append((char) ch);
				ch = read.read();
			}
			respuesta = sb.toString();
			read.close();
		} catch (Exception e) {
			log.error("Ocurrio un problema al consumir el servicio BANXICO:\n" + urlAndParams + "\nToken :" + token + "\n", e);
			throw e;
		} finally {
			System.setProperty("https.protocols", defaultProtocols);
			conn.disconnect();
		}
		
		return respuesta;
	}
	
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

	private HashMap<String, String> tipoCambioScheduled() throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		Properties banxico = null;
		
		try {
			banxico = new Properties();
			banxico.load(this.getClass().getResourceAsStream("/net/giro/tyg/banxico.properties"));
			if (banxico.containsKey("banxico.token"))
				params.put("token", banxico.getProperty("banxico.token"));
			if (banxico.containsKey("banxico.serie"))
				params.put("serie", banxico.getProperty("banxico.serie"));
			if (banxico.containsKey("banxico.datos"))
				params.put("datos", banxico.getProperty("banxico.datos"));
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar cargar el archivo 'banxico.properties'.", e);
		}

		return params;
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
		printLog("##############################    TOPIC CUENTAS POR COBRAR    ##############################");
	}

	private void printLog(String mensaje) {
		try {
			mensajeLog(mensaje);
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.info("TOPIC/TYG Log\n\n" + mensaje);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Terminado, "OK\n" + mensaje);
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/TYG. TopicEstatus: " + this.idTopicEstatus, e);
		}
	}
	
	private void printLog(String mensaje, Throwable throwable) {
		try {
			if (mensaje != null && ! "".equals(mensaje.trim()))
				mensajeLog(mensaje);
			mensajeLog("############################## TOPIC TYG --- FIN : " + new Date());
			mensaje = StringUtils.join(this.mensajeLogs, "\n");
			log.error("TOPIC/TYG Log\n\n" + mensaje + "\nEXCEPTION\n", throwable);
			this.ifzTopicEstatus.ejecucion(this.idTopicEstatus, TiposTopicEstatus.Error, mensaje + "\nEXCEPTION\n" + throwable.getMessage());
			this.idTopicEstatus = 0L;
			this.mensajeLogs.clear();
		} catch (Exception e) {
			log.error("NO SE PUDO ACTUALIZAR EL ESTATUS DEL TOPIC/TYG. TopicEstatus: " + this.idTopicEstatus, e);
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
