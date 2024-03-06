package net.giro.publico.respuesta;

import java.io.Serializable;

import net.giro.publico.respuesta.RespuestaBody;
import net.giro.publico.respuesta.RespuestaError;

public class Respuesta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private RespuestaBody body;
	private RespuestaError errores;
	
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
		//this.errores.setCodigoError(1);
	}
}
