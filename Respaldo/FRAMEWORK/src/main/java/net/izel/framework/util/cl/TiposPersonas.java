
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de errores en servicios
*
*/

package net.izel.framework.util.cl;

import java.util.HashMap;

public class TiposPersonas { 

	/*Persona Natural*/
	public static final long TIPO_NATURAL = 1;

	/*Persona JuridicA*/
	public static final long TIPO_JURIDICA = 2;

	/*Grupo Solidario*/
	public static final long TIPO_GRUPO_SOLIDARIO = 3;

	/*Persona Negocio*/
	public static final long TIPO_NEGOCIO = 4;

	public static HashMap<Long, String> descTipoPersona = new HashMap<Long, String>();

	static {
		descTipoPersona.put(TIPO_NATURAL,"PERSONA NATURAL");
		descTipoPersona.put(TIPO_JURIDICA,"PERSONA JURIDICA");
		descTipoPersona.put(TIPO_GRUPO_SOLIDARIO,"GRUPO SOLIDARIO");
		descTipoPersona.put(TIPO_NEGOCIO,"NEGOCIO");
	}

}