package net.giro.plataforma.impresion;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.ejb.Stateless;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;
import net.giro.util.Errores;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

@Stateless
public class ReportesFac implements ReportesRem {
	private static Logger log = Logger.getLogger(ReportesFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private Session session;
	private Ftp ftp;
	private int tiempoEsperaDefault;
	protected Long ID;
	protected String salida;
	public static final long GENERADO = 1L;
	public static final long AUTORIZADO = 2L;
	public static final long CANCELADO = 3L;
	
	public ReportesFac() {
		try {
			this.ID = 0L;
			this.tiempoEsperaDefault = 20;
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear ReportesFac", e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte) {
		return this.ejecutaReporte(params, paramsReporte, null, this.tiempoEsperaDefault);
	}

	@Override
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte, int tiempoEspera) {
		return this.ejecutaReporte(params, paramsReporte, null, tiempoEspera);
	}

	@Override
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte, HashMap<String, String> paramsCorreo) {
		return this.ejecutaReporte(params, paramsReporte, paramsCorreo, this.tiempoEsperaDefault);
	}

	@Override
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte, HashMap<String, String> paramsCorreo, int tiempoEspera) {
		Respuesta respuesta = new Respuesta();
		HashMap<String, Object> map =  new HashMap<String, Object>();
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		HashMap<String, Object> programacion = new HashMap<String, Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Gson gson = new Gson();
		String socket_ip = params.get("socket_ip");
		String socket_puerto = params.get("socket_puerto");
		String idPrograma = params.get("idPrograma");
		String cadenaFechaFormateada = "";
		String resultado = "";
		String comando = "";
		String json = "";
		RespuestaCrearJob resp = null;
		Resultado res = null;
		byte[] contenidoReporte = null;
		byte[] b = new byte[64000];
		int intento = 0;
		
		try{
			log.info("Conectando socket");
			@SuppressWarnings("resource")
			Socket socket = new Socket(socket_ip, Integer.valueOf(socket_puerto));
			log.info("Socket conectado. Preparo parametros de reporte");
			
			map.put("tipo", "000100");
			map.put("idAplicacion", "1");
			map.put("idPrograma", idPrograma);
			map.put("usuario", params.get("usuario"));
			map.put("role", "PLATAFORMA");
			
			for (Entry<String, Object> e : paramsReporte.entrySet()) {
				Object o = e.getValue();
				
				if (o instanceof String)
					parametros.put(e.getKey(), (String) o);
				if (o instanceof Long)
					parametros.put(e.getKey(), String.valueOf((Long) o));
				if (o instanceof Date) {
					dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					cadenaFechaFormateada = dateFormat.format((Date) o);
					parametros.put(e.getKey(), cadenaFechaFormateada);
				}
			}
			
			map.put("programacion", programacion);
			map.put("parametros", parametros);
			
			if (paramsCorreo != null && ! paramsCorreo.isEmpty()) 
				map.put("correo", paramsCorreo);
			
			gson = new Gson();
			comando = gson.toJson(map);
			log.info("Genero comando --> " + comando);
			
			log.info("Envio peticion --> " + comando);
			socket.getOutputStream().write(comando.getBytes());
			socket.getInputStream().read(b);
			resultado = new String(b);
			resultado = resultado.trim();
			resp = gson.fromJson(resultado, RespuestaCrearJob.class);
			log.info("Recibo respuesta --> " + resultado);
			
			if (resp == null || resp.getID() == null || resp.getID() <= 0L) {
				log.error("501 Not Implemented. COMMAND " + comando);
				respuesta.getBody().addValor("proceso", this.ID);
				respuesta.getErrores().addCodigo("PUBLICO", Errores.ERROR_GENERAR_REPORTE);
				respuesta.getErrores().setCodigoError(Long.valueOf(resp.getError()));
				respuesta.getErrores().setDescError(resp.getMensaje());
				return respuesta;
			}
			
			log.info("Bloque de espera por procesamiento de reporte ... ");
			this.ID = resp.getID();
			while (intento < tiempoEspera) {
				log.info("---> Intento " + (intento + 1) + " de " + tiempoEspera);
				map =  new HashMap<String, Object>();
				map.put("tipo",  "020400");
				map.put("usuario","JPEREZ");
				map.put("role", "PLATAFORMA");
				map.put("ID", String.valueOf(this.ID));
				
				comando = gson.toJson(map);
				log.info("---> envio comando :: " + comando);
				socket.getOutputStream().write(comando.getBytes());

				b = new byte[64000]; //byte[4096];
				socket.getInputStream().read(b);
				json = new String(b);
				json = json.trim();
				
				log.info("---> recibo respuesta :: " + json);
				res = ResultadoConsulta(json);
				if (res.getCodigo() == 0L || res.getCodigo() == 2L) {
					res.setCodigo(0L);
					break;
				}

				Thread.sleep(1000);
				intento ++;
			}
			
			log.info("---> Comando   :: " + comando);
			log.info("---> Respuesta :: " + json);

			log.info("Espera terminada. Validamos");
			if (Long.valueOf(resp.getError()) > 0) {
				log.error("408 Request Timeout. ID " + this.ID);
				respuesta.getErrores().addCodigo("PUBLICO", Errores.ERROR_GENERAR_REPORTE);
				respuesta.getErrores().setCodigoError(408);
				respuesta.getErrores().setDescError("Tiempo de espera excedido");
				respuesta.getBody().addValor("proceso", this.ID);
				return respuesta;
			}

			log.info("Obteniendo reporte ID " + this.ID);
			contenidoReporte = obtenerReporte(params, this.ID);
			if (contenidoReporte == null)
				log.error("404 Not Found. ID " + this.ID);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("contenidoReporte", contenidoReporte);
			respuesta.getBody().addValor("formatoReporte", this.salida);
			respuesta.getBody().addValor("proceso", this.ID);
			log.info("200 OK. ID " + this.ID);
			
			this.ID = 0L;
			this.salida = "";
		} catch (Exception e) {
			log.error("500 Internal Server Error. COMMAND " + comando);
			log.error("Error en Logica_Publico.ReportesFac.ejecutaReporte", e);
			respuesta.getErrores().addCodigo("PUBLICO", Errores.ERROR_OBTENER_REPORTE);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// ENVIO DE CORREOS
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	@Override
	public Respuesta enviarCorreo(String receptores, String asunto, String mensaje, HashMap<String, Object> adjuntos) throws Exception {
		Respuesta respuesta = new Respuesta();
		HashMap<String, String> correo = null;
		
		try {
			receptores = (receptores != null && ! "".equals(receptores.trim())) ? receptores.trim() : "";
			asunto = (asunto != null && ! "".equals(asunto.trim())) ? asunto.trim() : "AIR - NOTIFICACION";
			mensaje = (mensaje != null && ! "".equals(mensaje.trim())) ? mensaje.trim() : "NOTIFICACION AIR VACIA";
			
			// Parametros para envio de correo
			correo = new HashMap<String, String>();
			correo.put("from", ""); 
			correo.put("fromPass", "");
			correo.put("to", receptores);
			correo.put("subject", asunto);
			correo.put("body", mensaje);
			return this.enviarCorreo(correo, adjuntos);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar enviar correo electronico a " + receptores, e);
			respuesta.getErrores().addCodigo("PUBLICO", 1L);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta enviarCorreo(HashMap<String, String> params, HashMap<String, Object> adjuntos) throws Exception {
		Respuesta respuesta = new Respuesta();
		Properties properties = new Properties();
		MimeMultipart multiParte = new MimeMultipart();
		MimeMessage message = null;
		Transport transport = null;
		BodyPart texto = null;
		BodyPart anexo = null;
		String anexoExtension = "";
		// -------------------------------------------
		String from = "";
		String fromPass = "";
		List<String> receptores = null;
		List<String> receptoresCC = null;
		List<String> receptoresCCO = null;
		// -------------------------------------------
		HashMap<String, String> correoProps = null;
		
		try {
			properties = new Properties();
			properties.load(this.getClass().getResourceAsStream("correo.properties"));
			correoProps = new HashMap<String, String>();
	        for (String key : properties.stringPropertyNames())
	        	correoProps.put(key.trim(), properties.getProperty(key));

			// Comprobamos emisor
	        if (! params.containsKey("to") || params.get("to") == null || "".equals(params.get("to").trim()))
	        	params.put("to", correoProps.get("from"));
	        if (! params.containsKey("from") || params.get("from") == null || "".equals(params.get("from").trim()))
	        	params.put("from", correoProps.get("from"));
	        if (! params.containsKey("fromPass") || params.get("fromPass") == null || "".equals(params.get("fromPass").trim())) {
	        	params.put("from", correoProps.get("from"));
	        	params.put("fromPass", correoProps.get("fromPass"));
	        } 
	       from = params.get("from");
	       fromPass = params.get("fromPass");
	       
			// Comprobamos receptores
			receptores = string2List(params.get("to"), "\\,");
			receptoresCC = string2List(params.get("cc"), "\\,");
			receptoresCCO = string2List(params.get("cco"), "\\,");
			
			// Asignamos properties
			properties = new Properties();
			properties.put("mail.smtp.mail.sender", from);
			properties.put("mail.smtp.user", from);
			properties.put("mail.smtp.host", correoProps.get("host")); 					//, "smtpout.secureserver.net");
			properties.put("mail.smtp.port", correoProps.get("port")); 					//, 3535);
			properties.put("mail.smtp.auth", correoProps.get("auth")); 					//, "true");
			properties.put("mail.smtp.starttls.enable", correoProps.get("starttls"));	//, "false");
			
			this.session = Session.getDefaultInstance(properties);
			texto = new MimeBodyPart();
			texto.setText(params.get("body"));
			multiParte.addBodyPart(texto);
			
			if (adjuntos != null && ! adjuntos.isEmpty()) {
				for (Entry<String, Object> adjunto : adjuntos.entrySet()) {
					if (adjunto.getValue() == null) 
						continue;
					
					// Obtengo la extension del archivo a adjuntar
					anexoExtension = adjunto.getKey().substring(adjunto.getKey().lastIndexOf('.') + 1);
					
					// Genero el adjunto
					anexo = new MimeBodyPart();
					anexo.setFileName(adjunto.getKey());
					anexo.setDataHandler(new DataHandler(new ByteArrayDataSource((byte[]) adjunto.getValue(), "application/" + anexoExtension)));
					
					// Añado al contenido del correo
					multiParte.addBodyPart(anexo);
				}
			}
			
			message = new MimeMessage(this.session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));
			for (String receptor : receptores)
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
			for (String copia : receptoresCC)
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(copia));
			for (String copiaOculta : receptoresCCO)
				message.addRecipient(Message.RecipientType.BCC, new InternetAddress(copiaOculta));
			message.setSubject(params.get("subject"));
			message.setContent(multiParte);
			
			transport = this.session.getTransport("smtp");
			transport.connect(from, fromPass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e){
			log.error("\n\n\nOcurrio un problema al intentar enviar correo electronico:\n" + params.toString() + "\n\n\n", e);
			respuesta.getErrores().addCodigo("PUBLICO", 1L);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}

	// -------------------------------------------------------------------------------------------------------------
	// PRIVADAS
	// -------------------------------------------------------------------------------------------------------------
	
	private List<String> string2List(String valor, String separador) {
		List<String> valores = new ArrayList<String>();
		String[] splitted = null;
		
		if (valor == null || "".equals(valor.trim()))
			return valores;
		
		splitted = valor.split(separador);
		for (String val : splitted) {
			if ("".equals(val.trim()))
				continue;
			valores.add(val);
		}
		
		return valores;
	}
	
	private Resultado ResultadoConsulta(String json) {
		Resultado resultado = new Resultado();
		Gson gson = new Gson();
		RespuestaProcesos respuesta = null;
		CProcesoConsulta job = new CProcesoConsulta();
		Long fase = -1L;
		
		try {
			if (json == null || "".equals(json.trim()))
				return resultado;

			json = json.trim();
			respuesta = gson.fromJson(json, RespuestaProcesos.class);
			for (Entry<String, HashMap<String, Object>> m : respuesta.getProceso().entrySet()) { 
				job.setPrograma((String)m.getValue().get("programa"));
				job.setStatus((String)m.getValue().get("status"));
				job.setPriority(Double.valueOf(m.getValue().get("priority").toString()).intValue());
				job.setPhase((String)m.getValue().get("phase"));
				job.setSalida((String)m.getValue().get("salida"));

				if (m.getValue().get("nextFireTime") != null)
					job.setNextFireTime(new Date(Double.valueOf(m.getValue().get("nextFireTime").toString()).longValue())); // gson intenta castear a double el tipo long :/
				
				if (m.getValue().get("endTime") != null && Double.valueOf(m.getValue().get("endTime").toString()) > 0)
					job.setEndTime(new Date(Double.valueOf(m.getValue().get("endTime").toString()).longValue()));
				
				job.setStartTime(new Date(Double.valueOf(m.getValue().get("startTime").toString()).longValue()));
				job.setId(Double.valueOf(m.getValue().get("id").toString()).longValue());

				if (m.getValue().get("prevFireTime") != null && Double.valueOf(m.getValue().get("prevFireTime").toString()) > 0)
					job.setPrevFireTime(new Date(Double.valueOf(m.getValue().get("prevFireTime").toString()).longValue()));
			}

			if ("Q".equals(job.getPhase()) || "R".equals(job.getPhase()))
				fase = 1L;
			
			if ("T".equals(job.getPhase())) {
				fase = 2L;
				if ("N".equals(job.getStatus()))
					fase = 0L;
				this.salida = job.getSalida();
			}

			resultado.setCodigo(fase);
			resultado.setError_descr(job.getPhaseCompleto());
			resultado.setID(this.ID);	
		} catch(Exception ex) {
			log.error(String.format("ReportesFac::ResultadoConsulta  Exception: %s", ex.toString()));
			resultado.setCodigo(-1L);
			resultado.setError_descr(ex.toString());
		}
		
		return resultado;
	}
	
	private byte[] obtenerReporte(HashMap<String, String> params, long jobId) throws Exception {
		try {
			this.ftp = new Ftp(params.get("ftp_user"), params.get("ftp_password"), params.get("ftp_host"), params.get("ftp_port"));
			this.ftp.setID(jobId);
			return this.ftp.getArchivo("SALIDA", jobId);
		} catch (Exception e) {
			log.error("Error en Logica_Publico.ReportesFac.obtenerReporte", e);
			throw e;
		}
	}
	
	@SuppressWarnings("unused")
	private String getExtensionFromFileName(String target) {
		String resultado = "";
		String[] splitted = null;
		
		if (target.contains(".")) {
			splitted = target.split("\\.");
			resultado = splitted[splitted.length - 1];
		}
		
		return resultado;
	}

	@SuppressWarnings("unused")
	private String getEmailUser(String target) {
		String resultado = "";
		String[] splitted = null;
		
		if (target.contains("@")) {
			splitted = target.split("\\@");
			resultado = splitted[0];
		}
		
		return resultado;
	}
}
