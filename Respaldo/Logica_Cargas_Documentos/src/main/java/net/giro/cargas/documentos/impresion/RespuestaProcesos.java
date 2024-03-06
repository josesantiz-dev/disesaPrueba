package net.giro.cargas.documentos.impresion;

import java.util.HashMap;

public class RespuestaProcesos {
	private HashMap<String, HashMap<String, Object>> proceso;
	private String error;
	private String tipo;
	private String sesion;
	private Long resultado;
	private String msgHost;
	private String mensaje;
	private String respuesta;
	
	
	public RespuestaProcesos(){
		
	}

	public HashMap<String, HashMap<String, Object>> getProceso() {
		return proceso;
	}

	public void setProceso(HashMap<String, HashMap<String, Object>> proceso) {
		this.proceso = proceso;
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

	public Long getResultado() {
		return resultado;
	}

	public void setResultado(Long resultado) {
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
