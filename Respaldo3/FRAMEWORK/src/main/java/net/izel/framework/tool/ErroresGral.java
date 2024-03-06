/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de errores en servicios
*
*/

package net.izel.framework.tool;

import java.util.HashMap;

public class ErroresGral {

	/*ERROR INESPERADO*/
	public static final long ERROR_INESPERADO = 1;
	public static final long ERROR_CONECTAR_WS = 2;
	public static final long ERROR_CONSUMIR_WS = 3;
	public static final long CAMPOS_REQUERIDOS = 4;
	public static final long ERROR_CONSULTA = 5;
	public static final long ERROR_OBTENER_EJB = 6;
	public static final long ERROR_VALOR_DEPENDIENTE = 7;
	
	public static final long  USUARIO_INVALIDO = 50;
	public static final long  USUARIO_BLOQUEADO = 51;
	public static final long  USUARIO_EXPIRADO = 52;
	public static final long  USUARIO_INACTIVO = 53;
	

	/*SELECCIONE PERIODO*/

	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(ERROR_INESPERADO,"ERROR INESPERADO");
		descError.put(ERROR_CONECTAR_WS,"IMPOSIBLE CONECTAR CON EL SERVIDOR WS");
		descError.put(ERROR_CONSUMIR_WS,"OCURRIO UN PROBLEMA AL CONSUMIR EL WS");
		descError.put(CAMPOS_REQUERIDOS,"CAMPOS REQUERIDOS");
		descError.put(ERROR_CONSULTA,"SE PRODUJO UN PROBLEMA AL REALIZAR LA CONSULTA");
		descError.put(ERROR_OBTENER_EJB,"SE PRODUJO UN PROBLEMA AL CONECTAR CON JBoss");
		descError.put(USUARIO_INVALIDO,"Usuario y/o contrasena invalida");
		descError.put(USUARIO_INVALIDO,"Usuario y/o contrasena invalida");
		descError.put(USUARIO_BLOQUEADO,"Usuario Bloqueado");
		descError.put(USUARIO_EXPIRADO,"El usuario ha expirado, el tiempo de actividad ha terminado");
		descError.put(USUARIO_INACTIVO,"El usuario ha estado inactivo por el periodo maximo, se ha bloqueado por seguridad");
		descError.put(ERROR_VALOR_DEPENDIENTE,"No es posible eliminar el registro, existen dependencias al mismo");
	}
}

