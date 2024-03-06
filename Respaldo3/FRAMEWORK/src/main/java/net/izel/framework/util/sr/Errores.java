
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de errores en servicios
*
*/

package net.izel.framework.util.sr;

import java.util.HashMap;

public class Errores { 

	public static final long ERROR_INESPERADO = 1L;
	public static final long ERROR_NO_EXISTE_SCORING = 2L;
	public static final long ERROR_NO_EXISTE_LISTADO_SCORING = 3L;
	public static final long ERROR_EMPRESA_NO_ASIGNADA = 4L;
	public static final long ERROR_ALTA_USUARIO = 5L;
	public static final long ERROR_MODIFICA_USUARIO = 6L;
	
	public static final long ERROR_NO_EXISTE_USUARIO = 7L;
	public static final long ERROR_NO_EXISTE_PASSWORD = 8L;
	public static final long ERROR_PASSWORD_NO_COINCIDE = 9L;
	public static final long ERROR_INESPERADO_VERIFICA_USUARIO = 10L;
	public static final long ERROR_PASSWORD_USUARIO_YA_ESTA_REGISTRADO = 11L;
	public static final long CAMBIO_DE_PASSWORD = 12L;
	public static final long ERROR_ALTA_BITACORA = 13L;
	public static final long ERROR_PASSWORD_BLOQUEADO = 14L;
	public static final long ERROR_CORREO_ENVIADO_POR_BLOQUEO = 15L;
	public static final long ERROR_NUMERO_INTENTOS_EXCEDIDOS = 16L;
	public static final long PROCESO_TERMINADO = 17L;
	
	
	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(ERROR_INESPERADO,"ERROR NO IDENTIFICADO");
		descError.put(ERROR_NO_EXISTE_SCORING,"EL SCORING BUSCADO NO EXISTE EN EL SISTEMA");
		descError.put(ERROR_NO_EXISTE_LISTADO_SCORING,"EL LISTADO DE SCORING'S DEL SISTEMA NO EXISTE");
		descError.put(ERROR_ALTA_USUARIO,"No es posible registrar el usuario, intentelo nuevamente");
		descError.put(ERROR_MODIFICA_USUARIO,"No es posible realizar un cambio a su cuenta de usaurio, intentelo nuevamente");
		
		descError.put(ERROR_NO_EXISTE_USUARIO,"El usaurio que intenta ingresar, no se encuentra registrado");
		descError.put(ERROR_NO_EXISTE_PASSWORD,"El usuario aun no ha ingresado una contraseña");
		descError.put(ERROR_PASSWORD_NO_COINCIDE,"La contraseña entrada no coincide con la del usuario, intentelo nuevamente");
		descError.put(ERROR_INESPERADO_VERIFICA_USUARIO,"Error al verificar el usuario que intenta ingresar, intentelo nuevamente");
		descError.put(ERROR_PASSWORD_USUARIO_YA_ESTA_REGISTRADO,"La contraseña que intenta registrar ya a sido utilizada anteriormente, intente con una nueva contraseña");
		descError.put(CAMBIO_DE_PASSWORD,"La contraseña actual a expirado, favor de realizar el cambio de contraseña");
		
		descError.put(ERROR_ALTA_BITACORA,"La bitacora de acceso no se grabo correctamente");
		
		descError.put(ERROR_PASSWORD_BLOQUEADO,"La cuenta registrada ha sido bloquead por exceso en el número de intentos");
		descError.put(ERROR_CORREO_ENVIADO_POR_BLOQUEO,"Un correo se ha enviado a su cuenta de correo/usuario con instrucciones para desbloqueo de su cuenta");
		descError.put(ERROR_NUMERO_INTENTOS_EXCEDIDOS, "El número de intentos por ingresar ha excedito el límite establecido, su cuenta se ha bloqueado");
		descError.put(PROCESO_TERMINADO, "El registro de tu solicitud ya se concluyo, un asesor se pondra en contacto contigo");
	}
}

