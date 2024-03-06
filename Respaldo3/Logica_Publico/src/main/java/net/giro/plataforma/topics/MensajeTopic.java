package net.giro.plataforma.topics;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.TopicEstatusRem;

public class MensajeTopic implements Serializable {
	private static Logger log = Logger.getLogger(MensajeTopic.class);
	private static final long serialVersionUID = 1L;
	// -------------------------------------------------------------------------------------------------------------
	private transient String command;
	private transient List<String> mensajeLogs;
	private transient TopicEstatusRem ifzTopicEstatus;
	private transient ReportesRem ifzReportes;
	private transient String className;
	private transient boolean eventStoraged;
	// -------------------------------------------------------------------------------------------------------------
	private Long idTopicEstatusOwner;
	private Long idTopicEstatus;
	private String topicName;
	private String objeto;
	private String evento;
	// -------------------------------------------------------------------------------------------------------------
	private String target;
	private String referencia;
	private String referenciaExtra;
	private String atributos;
	private String fecha;
	private String monto;
	// -------------------------------------------------------------------------------------------------------------
	private InfoSesion infoSesion;
	private String idAcceso;
	private String idUsuarioResponsabilidad;
	private String usuario;
	private String aplicacion;
	private String empresa;
	private String emailSender;
	private String emailSenderClave;
	
	// -------------------------------------------------------------------------------------------------------------
	// CONSTRUCTORES
	// -------------------------------------------------------------------------------------------------------------
	
	public MensajeTopic() {
		InitialContext ctx = null;
		
		try {
			this.eventStoraged = false;
			this.idTopicEstatus = 0L;
			this.className = "";
			this.topicName = "";
			this.objeto = "";
			this.evento = "";
			//-------------------------------
			this.target = "";
			this.referencia = "";
			this.referenciaExtra = "";
			this.atributos = "";
			this.fecha = "";
			this.monto = "";
			//-------------------------------
			this.idAcceso = "";
			this.idUsuarioResponsabilidad = "";
			this.usuario = "";
			this.aplicacion = "";
			this.empresa = "";
			this.emailSender = "";
			this.emailSenderClave = "";
			//-------------------------------
			this.command = "";
			this.infoSesion = null;
			this.mensajeLogs = new ArrayList<String>();
			
			ctx = new InitialContext();
			this.ifzTopicEstatus = (TopicEstatusRem) ctx.lookup("ejb:/Logica_Publico//TopicEstatusFac!net.giro.plataforma.logica.TopicEstatusRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
			this.ifzTopicEstatus = null;
		}
	}

	public MensajeTopic(TopicEventosCARGAS evento, String target, String referencia, String atributos, InfoSesion infoSesion) {
		this();
		this.className = "TopicCargasDocumentos";
		this.objeto = "CARGAS";
		this.topicName = "topic/CARGAS";
		this.evento = evento.toString();
		this.target = target;
		this.referencia = referencia;
		this.atributos = atributos;
		this.infoSesion = infoSesion;
		if (infoSesion != null) {
			setIdAcceso(infoSesion.getAcceso().getId());
			setIdUsuarioResponsabilidad(infoSesion.getAcceso().getUsuario().getId());
			setUsuario(infoSesion.getAcceso().getUsuario().getId());
			setAplicacion(infoSesion.getAcceso().getAplicacion().getId());
			setEmpresa(infoSesion.getEmpresa().getId());
		}
	}

	public MensajeTopic(TopicEventosCompras evento, String target, String referencia, String atributos, InfoSesion infoSesion) {
		this();
		this.className = "TopicCompras";
		this.objeto = "COMPRAS";
		this.topicName = "topic/COMPRAS";
		this.evento = evento.toString();
		this.target = target;
		this.referencia = referencia;
		this.atributos = atributos;
		this.infoSesion = infoSesion;
		if (infoSesion != null) {
			setIdAcceso(infoSesion.getAcceso().getId());
			setIdUsuarioResponsabilidad(infoSesion.getAcceso().getUsuario().getId());
			setUsuario(infoSesion.getAcceso().getUsuario().getId());
			setAplicacion(infoSesion.getAcceso().getAplicacion().getId());
			setEmpresa(infoSesion.getEmpresa().getId());
		}
	}

	public MensajeTopic(TopicEventosCXC evento, String target, String referencia, String atributos, InfoSesion infoSesion) {
		this();
		this.className = "TopicCuentasPorCobrar";
		this.objeto = "CXC";
		this.topicName = "topic/CXC";
		this.evento = evento.toString();
		this.target = target;
		this.referencia = referencia;
		this.atributos = atributos;
		this.infoSesion = infoSesion;
		if (infoSesion != null) {
			setIdAcceso(infoSesion.getAcceso().getId());
			setIdUsuarioResponsabilidad(infoSesion.getAcceso().getUsuario().getId());
			setUsuario(infoSesion.getAcceso().getUsuario().getId());
			setAplicacion(infoSesion.getAcceso().getAplicacion().getId());
			setEmpresa(infoSesion.getEmpresa().getId());
		}
	}

	public MensajeTopic(TopicEventosCXP evento, String target, String referencia, String atributos, InfoSesion infoSesion) {
		this();
		this.className = "TopicCuentasPorPagar";
		this.objeto = "CXP";
		this.topicName = "topic/CXP";
		this.evento = evento.toString();
		this.target = target;
		this.referencia = referencia;
		this.atributos = atributos;
		this.infoSesion = infoSesion;
		if (infoSesion != null) {
			setIdAcceso(infoSesion.getAcceso().getId());
			setIdUsuarioResponsabilidad(infoSesion.getAcceso().getUsuario().getId());
			setUsuario(infoSesion.getAcceso().getUsuario().getId());
			setAplicacion(infoSesion.getAcceso().getAplicacion().getId());
			setEmpresa(infoSesion.getEmpresa().getId());
		}
	}

	public MensajeTopic(TopicEventosGP evento, String target, String referencia, String atributos, InfoSesion infoSesion) {
		this();
		this.className = "TopicGestionProyectos";
		this.objeto = "GP";
		this.topicName = "topic/GP";
		this.evento = evento.toString();
		this.target = target;
		this.referencia = referencia;
		this.atributos = atributos;
		this.infoSesion = infoSesion;
		if (infoSesion != null) {
			setIdAcceso(infoSesion.getAcceso().getId());
			setIdUsuarioResponsabilidad(infoSesion.getAcceso().getUsuario().getId());
			setUsuario(infoSesion.getAcceso().getUsuario().getId());
			setAplicacion(infoSesion.getAcceso().getAplicacion().getId());
			setEmpresa(infoSesion.getEmpresa().getId());
		}
	}

	public MensajeTopic(TopicEventosInventarios evento, String target, String referencia, String atributos, InfoSesion infoSesion) {
		this();
		this.className = "TopicInventarios";
		this.objeto = "INVENTARIOS";
		this.topicName = "topic/INVENTARIOS";
		this.evento = evento.toString();
		this.target = target;
		this.referencia = referencia;
		this.atributos = atributos;
		this.infoSesion = infoSesion;
		if (infoSesion != null) {
			setIdAcceso(infoSesion.getAcceso().getId());
			setIdUsuarioResponsabilidad(infoSesion.getAcceso().getUsuario().getId());
			setUsuario(infoSesion.getAcceso().getUsuario().getId());
			setAplicacion(infoSesion.getAcceso().getAplicacion().getId());
			setEmpresa(infoSesion.getEmpresa().getId());
		}
	}

	public MensajeTopic(TopicEventosRH evento, String target, String referencia, String atributos, InfoSesion infoSesion) {
		this();
		this.className = "TopicRecursosHumanos";
		this.objeto = "RH";
		this.topicName = "topic/RH";
		this.evento = evento.toString();
		this.target = target;
		this.referencia = referencia;
		this.atributos = atributos;
		this.infoSesion = infoSesion;
		if (infoSesion != null) {
			setIdAcceso(infoSesion.getAcceso().getId());
			setIdUsuarioResponsabilidad(infoSesion.getAcceso().getUsuario().getId());
			setUsuario(infoSesion.getAcceso().getUsuario().getId());
			setAplicacion(infoSesion.getAcceso().getAplicacion().getId());
			setEmpresa(infoSesion.getEmpresa().getId());
		}
	}

	public MensajeTopic(TopicEventosTYG evento, String target, String referencia, String atributos, InfoSesion infoSesion) {
		this();
		this.className = "TopicTesoreria";
		this.objeto = "TYG";
		this.topicName = "topic/TYG";
		this.evento = evento.toString();
		this.target = target;
		this.referencia = referencia;
		this.atributos = atributos;
		this.infoSesion = infoSesion;
		if (infoSesion != null) {
			setIdAcceso(infoSesion.getAcceso().getId());
			setIdUsuarioResponsabilidad(infoSesion.getAcceso().getUsuario().getId());
			setUsuario(infoSesion.getAcceso().getUsuario().getId());
			setAplicacion(infoSesion.getAcceso().getAplicacion().getId());
			setEmpresa(infoSesion.getEmpresa().getId());
		}
	}

	// -------------------------------------------------------------------------------------------------------------
	// METODOS
	// -------------------------------------------------------------------------------------------------------------

	public void enviar() throws Exception {
		QueueConnectionFactory tcf = null;
		MessageProducer producer = null;
		InitialContext iniCtx = null;
		Connection conn = null;
		Session session = null;
		TextMessage tm = null;
		Topic topic = null;
		Object tmp = null;

		// ---------------------------------------------------------------------------------------
		// Inicializamos
		// ---------------------------------------------------------------------------------------
		
		try {
			// Generamos comando 
			this.idTopicEstatus = this.idTopicEstatus != null && this.idTopicEstatus > 0L ? this.idTopicEstatus : 0L;
			this.topicName = this.topicName.trim() != null && ! "".equals(this.topicName.trim()) ? this.topicName.trim() : "";
			this.command = this.command.trim() != null && ! "".equals(this.command.trim()) ? this.command.trim() : "";
			if ("".equals(this.topicName.trim()))
				throw new Exception("TOPIC NOT ESPECIFIED");
			generaComando();
			if ("".equals(this.command.trim())) 
				throw new Exception("COMMAND NOT FOUND");
			info("MensajeTopic: Evento [" + this.topicName + "] " + this.evento + " generado!");
		} catch (Exception e) {
			error("MensajeTopic: Ocurrio un problema al preparar/generar el mensaje al topic requerido ... [" + this.topicName + "] " + this.evento + " ...\n" + this.command, e);
			print();
			throw e;
		}

		// ---------------------------------------------------------------------------------------
		// Guardamos Evento
		// ---------------------------------------------------------------------------------------
		
		try {
			// Guardamos/Actualizamos Evento
			storageEvent();
			// Regeneramos Comando, para incluir el idTopicEstatus
			generaComando(); 
		} catch (Exception e) {
			error("MensajeTopic: Ocurrio un problema al registrar el mensaje indicado ... [" + this.topicName + "] " + this.evento + " ...\n" + this.command, e);
		}

		// ---------------------------------------------------------------------------------------
		// Enviamos Evento
		// ---------------------------------------------------------------------------------------
		
		try {
			// Preparamos conector para el TOPIC
			info("MensajeTopic: Preparamos conector ... ");
			iniCtx = new InitialContext();
			tmp = iniCtx.lookup("ConnectionFactory");
			tcf = (QueueConnectionFactory) tmp;
			conn = tcf.createQueueConnection();

			// Instanciamos y conectamos con TOPIC
			info("MensajeTopic: Instanciamos y conectamos ... ");
			topic = (Topic) iniCtx.lookup(this.topicName);
			session = conn.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			conn.start();
			
			// Creamos Producer y enviamos mensaje
			info("MensajeTopic: Creamos Producer y enviamos mensaje ... ");
			producer = session.createProducer(topic);
			tm = session.createTextMessage(this.command);
			producer.send(tm);
			info("MensajeTopic: Evento [" + this.topicName + "] " + this.evento + " lanzado!");
		} catch (Exception e) {
			error("MensajeTopic: Ocurrio un problema al enviar el mensaje al topic requerido ... [" + this.topicName + "] " + this.evento + " ...\n" + this.command, e);
			throw e;
		} finally {
			// Cerramos conexiones
			info("MensajeTopic: Cerramos conexiones ... ");
			conn.stop();
			session.close();
			conn.close();
			print();
		}
	}

	public void storageEvent() {
		int intentos = 10;
		int intento = 0;

		do {
			this.eventStoraged = false;
			try {
				intento++;
				if (intento > intentos) {
					info("MensajeTopic: No se pudo registrar el evento - Intentos excedidos!");
					return;
				}
				
				if (this.ifzTopicEstatus == null) {
					info("MensajeTopic: No se pudo registrar el evento - Instancia nula. ");
					return;
				}

				if (intento > 1)
					log.info("Intento " + intento + "/" + intentos + "...");
				info("MensajeTopic: Evento [" + this.topicName + "] " + this.evento + " registrando ... ");
				this.ifzTopicEstatus.setInfoSesion(this.infoSesion);
				this.idTopicEstatus = this.ifzTopicEstatus.save(this.className, this.evento, this.command, this.idTopicEstatus);
				this.eventStoraged = (this.idTopicEstatus > 0L ? true : false);
			} catch (Exception e) {
				error("MensajeTopic: Ocurrio un problema al intentar registrar el evento requerido ... [" + this.topicName + "] " + this.evento, e);
				this.eventStoraged = false;
			} 
		} while (! this.eventStoraged);
		
		if (this.eventStoraged)
			info("MensajeTopic: Evento [" + this.topicName + "] " + this.evento + " registrado: " + this.idTopicEstatus);
	}
	
	private void generaComando() {
		owner();
		this.eventStoraged = false;
		this.command = (new Gson()).toJson(this, this.getClass());
		this.command = (this.command != null && ! "".equals(this.command.trim()) ? this.command.trim() : "");
	}
	
	private void owner() {
		if (this.infoSesion != null) {
			this.usuario = String.valueOf(this.infoSesion.getAcceso().getUsuario().getId());
			this.aplicacion = String.valueOf(this.infoSesion.getAcceso().getAplicacion().getId());
			this.empresa = this.infoSesion.getEmpresa().getId().toString();
			this.emailSender = this.infoSesion.getAcceso().getUsuario().getCorreo();
			this.emailSenderClave = this.infoSesion.getAcceso().getUsuario().getCorreoClave();
		}
	}
	
	private void info(String message) {
		this.mensajeLogs = (this.mensajeLogs != null ? this.mensajeLogs : new ArrayList<String>());
		this.mensajeLogs.add(message);
	}
	
	private void error(String message, Throwable throwable) {
		info(message);
		message = StringUtils.join(this.mensajeLogs, "\n");
		this.mensajeLogs.clear();
		log.error(message, throwable);
		enviarCorreo(message, throwable);
	}
	
	private void print() {
		this.mensajeLogs = (this.mensajeLogs != null ? this.mensajeLogs : new ArrayList<String>());
		log.info("\n#######################################################\n" + StringUtils.join(this.mensajeLogs, "\n") + "\n#######################################################");
		this.mensajeLogs.clear();
	}

	private void enviarCorreo(String mensaje, Throwable throwable) {
		String body = "";
		
		try {
			body += "\nNotificacion Evento " + this.className + "." + this.evento;
			body += "\n\tTarget: " + this.target;
			body += "\n\tReferencia: " + this.referencia;
			body += "\n\tReferenciaExtra: " + this.referenciaExtra;
			body += "\n\tAtributos: " + this.atributos;
			body += "\n\nComando:\n" + this.command;
			body += "\n\nLog:\n" + mensaje;
			if (throwable != null) 
				body += "\n\nException:\n" + throwable.getMessage();
			this.ifzReportes.enviarCorreo("soporteit@grupodisesa.com.mx", "AIR - Notificacion: Evento " + this.evento, body, null);
		} catch (Exception e) {
			log.error("Ocurrio un problema al notificar el error del evento " + this.evento + " en " + this.getClass().getCanonicalName(), e);
		}
	}
	
	// -------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------------

	public Long getIdTopicEstatusOwner() {
		return idTopicEstatusOwner;
	}

	public void setIdTopicEstatusOwner(Long value) {
		value = (value != null && value > 0L) ? value : 0L;
		this.idTopicEstatusOwner = value;
	}

	public Long getIdTopicEstatus() {
		return idTopicEstatus;
	}

	public void setIdTopicEstatus(Long value) {
		value = (value != null && value > 0L) ? value : 0L;
		this.idTopicEstatus = value;
	}

	/**
	 * read-only
	 * @return
	 */
	public String getTopicName() {
		return topicName;
	}

	/**
	 * read-only
	 * @return
	 */
	public void setTopicName(String topicName) {}

	public String getObjeto() {
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getReferenciaExtra() {
		return referenciaExtra;
	}

	public void setReferenciaExtra(String referenciaExtra) {
		this.referenciaExtra = referenciaExtra;
	}

	public String getAtributos() {
		return atributos;
	}

	public void setAtributos(String atributos) {
		this.atributos = atributos;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(fecha);
	}

	public void setFecha(Date fecha, String formato) {
		if (formato == null || "".equals(formato.trim()))
			formato = "dd-MM-yyyy HH:mm:ss";
		this.fecha = (new SimpleDateFormat(formato)).format(fecha);
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto.toString();
	}

	/**
	 * transient read-only
	 * @return
	 */
	public String getCommand() {
		if (this.command == null || "".equals(this.command.trim()))
			generaComando();
		return this.command;
	}

	/**
	 * transient read-only
	 * @return
	 */
	public void setCommand(String command) {}
	
	public String getIdAcceso() {
		return idAcceso;
	}

	public void setIdAcceso(String idAcceso) {
		this.idAcceso = idAcceso;
	}
	
	public void setIdAcceso(Long value) {
		if (value == null)
			value = 0L;
		this.idAcceso = value.toString();
	}

	public String getIdUsuarioResponsabilidad() {
		return idUsuarioResponsabilidad;
	}

	public void setIdUsuarioResponsabilidad(String idUsuarioResponsabilidad) {
		this.idUsuarioResponsabilidad = idUsuarioResponsabilidad;
	}
	
	public void setIdUsuarioResponsabilidad(Long value) {
		if (value == null)
			value = 0L;
		this.idUsuarioResponsabilidad = value.toString();
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String value) {
		if (value == null || "".equals(value.trim()))
			value = "1";
		this.usuario = value;
	}

	public void setUsuario(Long value) {
		if (value == null || value <= 0L)
			value = 1L;
		this.usuario = value.toString();
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String value) {
		if (value == null)
			value = "0";
		this.aplicacion = value;
	}

	public void setAplicacion(Long value) {
		if (value == null)
			value = 0L;
		this.aplicacion = value.toString();
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String value) {
		if (value == null || "".equals(value.trim()))
			value = "1";
		this.empresa = value;
	}

	public void setEmpresa(Long value) {
		if (value == null || value <= 0L)
			value = 1L;
		this.empresa = value.toString();
	}

	public String getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(String value) {
		this.emailSender = value;
	}

	public String getEmailSenderClave() {
		return emailSenderClave;
	}

	public void setEmailSenderClave(String value) {
		this.emailSenderClave = value;
	}

	public InfoSesion getInfoSesion() {
		return infoSesion;
	}

	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
		if (infoSesion != null) {
			setIdAcceso(infoSesion.getAcceso().getId());
			setIdUsuarioResponsabilidad(infoSesion.getAcceso().getUsuario().getId());
			setUsuario(infoSesion.getAcceso().getUsuario().getId());
			setAplicacion(infoSesion.getAcceso().getAplicacion().getId());
			setEmpresa(infoSesion.getEmpresa().getId());
		}
	}
	
	public void setInfoSesion(long idUsuario, long idEmpresa, long idAplicacion) {
		setUsuario(idUsuario);
		setEmpresa(idEmpresa);
		setAplicacion(idAplicacion);
	}
}
