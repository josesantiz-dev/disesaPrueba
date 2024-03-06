package net.giro.plataforma.topics;

import java.io.Serializable;

public class MensajeTopic implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String objeto;
	private String evento;
	private String target;
	private String referencia;
	private String atributos;
	private String emailSender;
	private String emailSenderClave;
	
	
	public MensajeTopic(TopicComprasEventos evento, String target, String referencia, String atributos) {
		this.objeto = "COMPRAS";
		this.evento = evento.toString();
		this.target = target;
		this.referencia = referencia;
		this.atributos = atributos;
		this.emailSender = "";
		this.emailSenderClave = "";
	}

	public MensajeTopic(TopicInventariosEventos evento, String target, String referencia, String atributos) {
		this.objeto = "INVENTARIOS";
		this.evento = evento.toString();
		this.target = target;
		this.referencia = referencia;
		this.atributos = atributos;
		this.emailSender = "";
		this.emailSenderClave = "";
	}
	

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

	public String getAtributos() {
		return atributos;
	}

	public void setAtributos(String atributos) {
		this.atributos = atributos;
	}

	public String getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(String emailSender) {
		this.emailSender = emailSender;
	}

	public String getEmailSenderClave() {
		return emailSenderClave;
	}

	public void setEmailSenderClave(String emailSenderClave) {
		this.emailSenderClave = emailSenderClave;
	}
}
