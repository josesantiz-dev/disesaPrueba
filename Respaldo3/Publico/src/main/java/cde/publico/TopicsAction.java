package cde.publico;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.TopicEstatus;
import net.giro.plataforma.logica.TopicEstatusRem;
import net.giro.plataforma.topics.MensajeTopic;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class TopicsAction implements Serializable {
	private static Logger log = Logger.getLogger(TopicsAction.class);
	private static final long serialVersionUID = 1L;
	private LoginManager loginManager;
	private TopicEstatusRem ifzTopics;
	private List<TopicEstatus> listEventos;
	private TopicEstatus evento;
	// Busqueda Principal
	private List<SelectItem> tiposBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private boolean activarFecha;
	private Date fechaBusqueda;
   	private int numPagina;
	// control
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;

	public TopicsAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			
			ctx = new InitialContext();
			this.ifzTopics = (TopicEstatusRem) ctx.lookup("ejb:/Logica_Publico//TopicEstatusFac!net.giro.plataforma.logica.TopicEstatusRem");
			
			// Busqueda principal
			// ----------------------------------------------------------------------
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("nombre", "Topic"));
			this.tiposBusqueda.add(new SelectItem("evento", "Evento"));
			this.tiposBusqueda.add(new SelectItem("target", "Target"));
			this.tiposBusqueda.add(new SelectItem("referencia", "Referencia"));
			this.tiposBusqueda.add(new SelectItem("fechaCreado", "Fecha"));
			this.tiposBusqueda.add(new SelectItem("comando:target", "Target (comando)"));
			this.tiposBusqueda.add(new SelectItem("comando:referencia", "Referencia (comando)"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.fechaBusqueda = Calendar.getInstance().getTime();
			this.numPagina = 1;
	   		
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;

			this.listEventos = new ArrayList<TopicEstatus>();
			this.evento = new TopicEstatus();
		} catch (Exception e) {
			log.error("Error en Publico.TopicsAction", e);
		}
	}
	
	public void buscar() {
		try {
			control();
			this.campoBusqueda = (this.campoBusqueda != null && ! "".equals(this.campoBusqueda.trim()) ? this.campoBusqueda.trim() : this.tiposBusqueda.get(0).getValue().toString());
			this.valorBusqueda = (this.valorBusqueda != null && ! "".equals(this.valorBusqueda.trim()) ? this.valorBusqueda.trim() : "%");
   			if (! this.activarFecha)
   				this.fechaBusqueda = null;
   			if (this.fechaBusqueda == null && this.activarFecha)
   				this.fechaBusqueda = Calendar.getInstance().getTime();
   			
   			if ("%".equals(this.valorBusqueda.trim()) && this.fechaBusqueda == null) {
   				this.fechaBusqueda = Calendar.getInstance().getTime();
   				this.valorBusqueda = "%";
   			}

			this.numPagina = 1;
			this.ifzTopics.setInfoSesion(this.loginManager.getInfoSesion());
			this.listEventos = this.ifzTopics.findLikeProperty(this.campoBusqueda, this.valorBusqueda.trim().replace(" ", "%"), this.fechaBusqueda, "", 0);
			if (this.listEventos == null || this.listEventos.isEmpty())
				control(2, "La búsqueda no regresó resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Eventos", e);
		}
	}
	
	public void ver() {
		try {
			control();
			if (this.evento == null || this.evento.getId() == null || this.evento.getId() <= 0L || this.evento.getComando() == null || "".equals(this.evento.getComando())) {
				control(-1, "Ocurrio un problema al recuperar el evento seleccionado");
				return;
			}
			
			//this.evento = this.ifzTopics.populateCommand(this.evento);
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Evento", e);
		}
	}
	
	public void guardar() {
		MensajeTopic topic = null;
		Gson gson = null;
		
		try {
			control();
			if (! validaciones())
				return;

			gson = new Gson();
			topic = gson.fromJson(this.evento.getComando(), MensajeTopic.class);
			if (topic.getIdTopicEstatus() == null || topic.getIdTopicEstatus() <= 0L)
				topic.setIdTopicEstatus(this.evento.getId());
			if (topic.getInfoSesion() == null)
				topic.setInfoSesion(this.loginManager.getInfoSesion());
			topic.enviar();
			
			this.evento.setComando(topic.getCommand());
			this.evento.setManual(1);
			this.evento.setFechaManual(Calendar.getInstance().getTime());
			this.evento = this.ifzTopics.saveOrUpdate(this.evento);
			if (this.listEventos == null || this.listEventos.isEmpty())
				return;
			
			for (TopicEstatus e : this.listEventos) {
				if (this.evento.getId().longValue() == e.getId().longValue()) {
					e = this.evento;
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al guardar el Evento", e);
		}
	}

	public void lanzar() {
		MensajeTopic topic = null;
		Gson gson = null;
		
		try {
			control();
			if (this.evento == null) {
				control(-1, "Ocurrio un problema al recuperar el Evento seleccionado");
				return;
			}
			
			gson = new Gson();
			topic = gson.fromJson(this.evento.getComando(), MensajeTopic.class);
			if (topic.getIdTopicEstatus() == null || topic.getIdTopicEstatus() <= 0L)
				topic.setIdTopicEstatus(this.evento.getId());
			if (topic.getInfoSesion() == null)
				topic.setInfoSesion(this.loginManager.getInfoSesion());
			topic.enviar();
		} catch (Exception e) {
			control("Ocurrio un problema al lanzar el Evento", e);
		} 
	}

	public void cancelar() {
		try {
			control();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cancelar el Evento", e);
		}
	}

	public void inflate() {
		Date fechaInicial = null;
		Date fechaFinal = null;
		
		try {
			control();
			fechaInicial = (new SimpleDateFormat("dd/MM/yyyy")).parse("01/01/2020");
			fechaFinal = Calendar.getInstance().getTime();
			this.ifzTopics.inflateTopicEstatus(fechaInicial, fechaFinal);
		} catch (Exception e) {
			control("Ocurrio un problema al Inflar los TopicEstatus", e);
		}
	}
	
	private boolean validaciones() {
		if (this.evento == null) {
			control(-1, "Ocurrio un problema al recuperar el Evento seleccionado");
			return false;
		}
		
		return true;
	}

	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		String message = "";
		
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al intentar realizar la operacion indicada";
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		
		message = this.getClass().getCanonicalName() + " :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + "\nMensaje :: " + this.tipoMensaje + " - " + mensaje;
		message = "\n" + message + "\n";
		if (! operacionCancelada) {
			log.info(message, throwable); 
			return;
		}
		
		log.error(message, throwable); 
	}

	// -------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------
	
	public String getIdEvento() {
		if (this.evento != null && this.evento.getId() != null && this.evento.getId() > 0L)
			return this.evento.getId().toString();
		return "";
	}
	
	public void setIdEvento(String value) {}
	
	public List<TopicEstatus> getListEventos() {
		return listEventos;
	}

	public void setListEventos(List<TopicEstatus> listEventos) {
		this.listEventos = listEventos;
	}

	public TopicEstatus getEvento() {
		return evento;
	}

	public void setEvento(TopicEstatus evento) {
		this.evento = evento;
	}
	
	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public boolean isOperacionCancelada() {
		return operacionCancelada;
	}

	public void setOperacionCancelada(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}
	
	public boolean getActivarFecha() {
		return activarFecha;
	}
	
	public void setActivarFecha(boolean activarFecha) {
		this.activarFecha = activarFecha;
	}
}
