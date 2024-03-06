package net.giro.plataforma.impresion;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
	private String modulo;
	public static HashMap<Long, String> descEstatus = new HashMap<Long, String>();
	
	public static final long GENERADO = 1L;
	public static final long AUTORIZADO = 2L;
	public static final long CANCELADO = 3L;
	protected Long ID;
	protected String salida;
	private int intentos;
	
	
	public ReportesFac(){
		try {
			this.ID = 0L;
			this.intentos = 20;
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear ReportesFac", e);
		}
	}
	

	@Override
	public void intentosReporte(int value) {
		this.intentos = value;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte) {
		return this.ejecutaReporte(params, paramsReporte, null);
	}
	
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte,  HashMap<String, String> paramsCorreo) {
		Respuesta respuesta = new Respuesta();
		HashMap<String, Object> map =  new HashMap<String, Object>();
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		HashMap<String, Object> programacion = new HashMap<String, Object>();
		
		String socket_ip = params.get("socket_ip");
		String socket_puerto = params.get("socket_puerto");
		String idPrograma = params.get("idPrograma");
		String resultado;
		RespuestaCrearJob resp;
		
		byte[] b = new byte[64000];
		int cont = 0;
		
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
			
			for(Entry<String, Object> e : paramsReporte.entrySet()){
				Object o = e.getValue();
				
				if(o instanceof String)
					parametros.put(e.getKey(), (String) o);
				
				if(o instanceof Long)
					parametros.put(e.getKey(), String.valueOf((Long)o));
				
				if(o instanceof Date){
					SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					String cadenaFechaFormateada = dateFormat.format((Date) o);
					parametros.put(e.getKey(), cadenaFechaFormateada);
				}
			}
			
			map.put("programacion", programacion);
			map.put( "parametros", parametros);
			
			if (paramsCorreo != null && ! paramsCorreo.isEmpty()) 
				map.put("correo", paramsCorreo);
			
			Gson gson = new Gson();
			String comando = gson.toJson(map);
			log.info("Genero comando --> " + comando);
			
			log.info("Envio peticion --> " + comando);
			socket.getOutputStream().write(comando.getBytes());
			socket.getInputStream().read(b);
			resultado = new String(b);
			resultado = resultado.trim();
			resp = gson.fromJson(resultado, RespuestaCrearJob.class);
			log.info("Recibo respuesta --> " + resultado);
			
			if (resp == null || resp.getID() == null || resp.getID() <= 0L) {
				respuesta.setBody(null);
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GENERAR_REPORTE);
				respuesta.getErrores().setCodigoError(Long.valueOf(resp.getError()));
				respuesta.getErrores().setDescError(resp.getMensaje());
				return respuesta;
			}
			
			log.info("Bloque de espera por procesamiento de reporte");
			this.ID = resp.getID();
			while (cont < this.intentos) {
				log.info("---> En espera ... intento " + (cont + 1) + " de " + this.intentos);
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
				String json = new String(b);
				json = json.trim();
				log.info("---> recibo respuesta :: " + json);
				Resultado res = ResultadoConsulta(json);
				
				if (res.getCodigo() == 0L || res.getCodigo() == 2L) {
					res.setCodigo(0L);
					break;
				}

				Thread.sleep(1000);
				cont ++;
			}

			log.info("Espera terminada. Validamos");
			if(Long.valueOf(resp.getError()) > 0) {
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_GENERAR_REPORTE);
				respuesta.setBody(null);
				return respuesta;
			}

			log.info("Obteniendo reporte ID " + this.ID);
			byte[] contenidoReporte = obtenerReporte(params, this.ID);
			if (contenidoReporte == null)
				log.error("Error Interno - No se pudo obtener el reporte ID " + this.ID);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("contenidoReporte", contenidoReporte);
			respuesta.getBody().addValor("formatoReporte", this.salida);
			
			this.ID = 0L;
			this.salida = "";
		} catch (Exception e) {
			log.error("Error en Logica_Publico.ReportesFac.ejecutaReporte", e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_OBTENER_REPORTE);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}

	@Override
	public HashMap<Long, String> getDescEstatus() {
		return descEstatus;
	}

	@Override
	public Respuesta enviarCorreo(HashMap<String, String> params, HashMap<String, Object> adjuntos) throws Exception {
		Respuesta respuesta = new Respuesta();
		Properties properties = new Properties();
		MimeMessage message = null;
		Transport transport = null;
		MimeMultipart multiParte = new MimeMultipart();
		BodyPart texto = null;
		BodyPart anexo = null;
		String[] receptores = null;
		String anexoExtension = "";
		
		try {
			// Comprobamos receptores
			if (params.get("to").contains(","))
				receptores = params.get("to").split("\\,");
			
			// Asignamos properties
			properties.put("mail.smtp.mail.sender", params.get("from"));
			properties.put("mail.smtp.host", "smtpout.secureserver.net");
			properties.put("mail.smtp.port", 3535);
			properties.put("mail.smtp.user", params.get("from"));
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "false");
			
			session = Session.getDefaultInstance(properties);
			
			texto = new MimeBodyPart();
			texto.setText(params.get("body"));
			multiParte.addBodyPart(texto);
			
			if (adjuntos != null && ! adjuntos.isEmpty()) {
				anexo = new MimeBodyPart();
				for (Entry<String,Object> var : adjuntos.entrySet()) {
					if (var.getValue() == null) continue;
					anexoExtension = var.getKey().substring(var.getKey().lastIndexOf('.') + 1);
					anexo.setFileName(var.getKey());
					anexo.setDataHandler(
						new DataHandler(
							new ByteArrayDataSource(
								(byte[]) var.getValue(), 
								"application/" + anexoExtension
							)
						)
					);
					
					multiParte.addBodyPart(anexo);
				}
			}
			
			message = new MimeMessage(session);
			message.setFrom(new InternetAddress(params.get("from")));
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress(params.get("from")));
			if (receptores != null && receptores.length > 0) {
				for (int i = 0; i < receptores.length; i++)
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(receptores[i]));
			} else
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(params.get("to")));
			message.setSubject(params.get("subject"));
			message.setContent(multiParte);
			
			transport = session.getTransport("smtp");
			transport.connect(params.get("from"), params.get("fromPass"));
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			respuesta.getErrores().setCodigoError(0L);
		} catch (Exception e){
			log.error("Error en Logica_Publico.ReportesFac.ejecutaReporte", e);
			respuesta.getErrores().addCodigo(modulo, 1L);
			respuesta.setBody(null);
		}
		
		return respuesta;
	}

	@Override
	public Respuesta enviarCorreo(String emisor, String emisorClave, String asunto, String mensaje, String receptores, HashMap<String, Object> adjuntos) throws Exception {
		Respuesta respuesta = new Respuesta();
		
		try {
			// Parametros para envio de correo
			HashMap<String, String> correo = new HashMap<>();
			correo.put("from", emisor); 
			correo.put("fromPass", emisorClave);
			correo.put("to", receptores);
			correo.put("subject", asunto);
			correo.put("body", mensaje);
			
			return this.enviarCorreo(correo, adjuntos);
		} catch (Exception e){
			respuesta.getErrores().addCodigo(modulo, 1L);
			respuesta.setBody(null);
			log.error("Error en Logica_Publico.ReportesFac.enviarCorreo", e);
		}
		
		return respuesta;
	}
	
	private Resultado ResultadoConsulta(String json) {
		Resultado resultado;
		Gson gson;
		RespuestaProcesos  respuesta;
		CProcesoConsulta job;
		Long fase;

		fase 		= -1L;
		gson 		= new Gson();
		resultado	= new Resultado();
		job 		= new CProcesoConsulta();
		json		= json.trim();
		
		try {
			if (json != null && !"".equals(json)) {
				respuesta = gson.fromJson(json, RespuestaProcesos.class);

				for (Entry<String, HashMap<String, Object>> m : respuesta.getProceso().entrySet()) { 
					job.setPrograma((String)m.getValue().get("programa"));
					job.setStatus((String)m.getValue().get("status"));
					job.setPriority(Double.valueOf(m.getValue().get("priority").toString()).intValue());
					job.setPhase((String)m.getValue().get("phase"));
					job.setSalida((String)m.getValue().get("salida"));

					if (m.getValue().get("nextFireTime") != null) {
						job.setNextFireTime( new Date(Double.valueOf(m.getValue().get("nextFireTime").toString()).longValue())); // gson intenta castear a double el tipo long :/
					}
					
					if (m.getValue().get("endTime") != null && Double.valueOf(m.getValue().get("endTime").toString()) > 0) {
						job.setEndTime( new Date(Double.valueOf(m.getValue().get("endTime").toString()).longValue()));
					}
					
					job.setStartTime(new Date(Double.valueOf(m.getValue().get("startTime").toString()).longValue()));
					job.setId(Double.valueOf(m.getValue().get("id").toString()).longValue());

					if (m.getValue().get("prevFireTime") != null && Double.valueOf(m.getValue().get("prevFireTime").toString()) > 0) {
						job.setPrevFireTime( new Date(Double.valueOf(m.getValue().get("prevFireTime").toString()).longValue()));
					}
				}

				if ("Q".equals(job.getPhase())  || "R".equals(job.getPhase()) ) {
					fase = 1L;
				}
				
				if ("T".equals(job.getPhase()) ) {
					fase = 2L;
					if ("N".equals(job.getStatus()))
						fase = 0L;
					this.salida = job.getSalida();
				}

				resultado.setCodigo(fase);
				resultado.setError_descr(job.getPhaseCompleto());
				resultado.setID(this.ID);			
			}
		} catch(Exception ex) {
			resultado.setCodigo(-1L);
			resultado.setError_descr(ex.toString());
			log.error(String.format("ReportesFac::ResultadoConsulta  Exception: %s", ex.toString() ) );
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
