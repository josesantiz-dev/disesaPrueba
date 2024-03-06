package net.izel.framework.util.imp;

import java.util.HashMap;

public class Errores 
{	
	public static final long ERROR_OBTENER_PARAMETROS_CONFIG = 1;
	public static final long ERROR_EXCEPTION = 2;
	public static final long ERROR_PARAMETROS_VACIOS = 3;
	public static final long ERROR_AL_OBTENER_DOCUMENTO_FTP = 4;
	public static final long ERROR_ID_JOB_INVALIDO = 5;
	public static final long ERROR_CAMPOS_REQUERIDOS = 6;
	
	
	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(ERROR_OBTENER_PARAMETROS_CONFIG, "ERROR AL OBTENER PARAMETROS DE CONFIGURACION");
		descError.put(ERROR_EXCEPTION, "ERROR CRITICO CONSULTE AL ADMINISTRADOR");
		descError.put(ERROR_PARAMETROS_VACIOS, "PARAMETROS PARA IMPRESION VACIOS");		
		descError.put(ERROR_AL_OBTENER_DOCUMENTO_FTP, "NO SE OBTUVO DOCUMENTO POR FTP");
		descError.put(ERROR_ID_JOB_INVALIDO, "NO SE PUDO OBTENER ID DE IMPRESION");
		descError.put(ERROR_CAMPOS_REQUERIDOS, "CAMPOS REQUERIDOS");
	}

}

/** !Errores.java */


