package net.giro.plataforma.impresion;

import java.util.HashMap;

import javax.ejb.Remote;

import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Remote
public interface ReportesRem {
	public void setInfoSesion(InfoSesion infoSesion);
	
	/**
	 * Metodo para lanzar ejecucion de reporte
	 * @param params Parametros de ejecucion
	 * @param paramsReporte Parametros de reporte
	 * @return
	 */
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte);
	
	/**
	 * Metodo para lanzar ejecucion de reporte
	 * @param params Parametros de ejecucion
	 * @param paramsReporte Parametros de reporte
	 * @param tiempoEspera Tiempo de espera de terminacion en segundos
	 * @return
	 */
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte, int tiempoEspera);
	
	/**
	 * Metodo para lanzar ejecucion de reporte
	 * @param params Parametros de ejecucion
	 * @param paramsReporte Parametros de reporte
	 * @param paramsCorreo Parametros para el envio por correo post-ejecucion
	 * @return
	 */
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte, HashMap<String, String> paramsCorreo);
	
	/**
	 * Metodo para lanzar ejecucion de reporte
	 * @param params Parametros de ejecucion
	 * @param paramsReporte Parametros de reporte
	 * @param paramsCorreo Parametros para el envio por correo post-ejecucion
	 * @param tiempoEspera Tiempo de espera de terminacion en segundos
	 * @return
	 */
	public Respuesta ejecutaReporte(HashMap<String, String> params, HashMap<String, Object> paramsReporte, HashMap<String, String> paramsCorreo, int tiempoEspera);
	
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
	public Respuesta enviarCorreo(String receptores, String asunto, String mensaje, HashMap<String, Object> adjuntos) throws Exception;

	/**
	 * Envio de correo
	 * @param params Debe contener los valores para [from, fromPass, to, subject, body]
	 * @param adjuntos contiene los adjuntos para añadir al correo. [filename.extension, contenido adjunto]
	 * @return
	 * @throws Exception
	 */
	public Respuesta enviarCorreo(HashMap<String, String> params, HashMap<String, Object> adjuntos) throws Exception;
}

/*
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 * VER | 	FECHA 	| 		AUTOR 		| DESCRIPCIÓN
 * ---------------------------------------------------------------------------------------------------------------- 
 * 2.2 | 2016-11-21 | Javier Tirado 	| Añado metodo enviarCorreo
 */