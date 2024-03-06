package net.giro.plataforma.impresion;

import java.util.HashMap;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface ReportesRem {
	public void intentosReporte(int value);
	
	public void setInfoSesion(InfoSesion infoSesion);
	
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte);
	
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte,  HashMap<String, String> paramsCorreo);

	public HashMap<Long, String> getDescEstatus();
	
	/**
	 * Envio de correo
	 * @param params Debe contener los valores para [from, fromPass, to, subject, body]
	 * @param adjuntos contiene los adjuntos para añadir al correo. [filename.extension, contenido adjunto]
	 * @return
	 * @throws Exception
	 */
	public Respuesta enviarCorreo(HashMap<String, String> params, HashMap<String, Object> adjuntos) throws Exception;
	
	/**
	 * Envio de correo
	 * @param emisor Correo de quien emite el correo
	 * @param emisorClave Contraseña de quien emite el correo (Para acceso a SMTP)
	 * @param asunto Asunto del correo
	 * @param mensaje Cuerpo del mensaje
	 * @param receptores Correos de los receptores del e-mail separados por coma
	 * @param adjuntos HashMap<String, Object> contiene los adjuntos que deben agregar al correo
	 * @return
	 * @throws Exception
	 */
	public Respuesta enviarCorreo(String emisor, String emisorClave, String asunto, String mensaje, String receptores, HashMap<String, Object> adjuntos) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.2 | 2016-11-21 | Javier Tirado 	| Añado metodo enviarCorreo
 */