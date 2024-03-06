package net.giro.sesion;

import java.io.Serializable;

import net.giro.sesion.Opciones;

public class Mensaje implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Opciones opciones;
	private String error;
	private String tipo;
	private String sesion;
	private String resultado;
	private String msgHost;
	private String mensaje;
	private String respuesta;
	
	
	public Mensaje() {
		this.error = "";
		this.tipo = "";
		this.sesion = "";
		this.resultado = "";
		this.msgHost = "";
		this.mensaje = "";
		this.respuesta = "";
		this.opciones = new Opciones();
	}
	

	public Opciones getOpciones() {
		return opciones;
	}

	public void setOpciones(Opciones opciones) {
		this.opciones = opciones;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getSesion() {
		return sesion;
	}

	public void setSesion(String sesion) {
		this.sesion = sesion;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getMsgHost() {
		return msgHost;
	}

	public void setMsgHost(String msgHost) {
		this.msgHost = msgHost;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
}
