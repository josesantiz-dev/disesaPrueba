package net.giro.respuesta;

import java.io.Serializable;

import net.giro.respuesta.RespuestaBody;
import net.giro.respuesta.RespuestaError;

public class Respuesta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private RespuestaBody body;
	private RespuestaError errores;
	private Integer resultado=null;
	private String referencia="";
	private Object objeto = null;
	private String respuesta="";
	
	public Respuesta(){
		body = new RespuestaBody();
		errores = new RespuestaError();
	}
	
	public RespuestaBody getBody() {
		return body;
	}
	
	public void setBody(RespuestaBody body) {
		this.body = body;
	}
	
	public RespuestaError getErrores() {
		return errores;
	}
	
	public void setErrores(RespuestaError errores) {
		this.errores = errores;
	}
	
	public Integer getResultado() {
		return resultado;
	}

	public void setResultado(Integer resultado) {
		this.resultado = resultado;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Object getObjeto() {
		return objeto;
	}

	public void setObjeto(Object objeto) {
		this.objeto = objeto;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
}
