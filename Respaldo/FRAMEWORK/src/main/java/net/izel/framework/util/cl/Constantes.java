
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de estatus en servicio Clientes
*
*/

package net.izel.framework.util.cl;

import java.util.HashMap;

public class Constantes { 
	/*ESTATUS DE LOS REGISTROS DEL MODULO DE CLIENTES*/
	/*Activo - Alta*/
	public static final long OMITIR_HOMONIIA = 1L;
	/*Inactivo - Baja*/
	public static final long NO_OMITIR_HOMONIMIA = 0L;
	/*ESTATUS DE LOS REGISTROS DEL MODULO DE CLIENTES*/
	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	//* TIPO CONTRIBUYENTE
	public static final long PERSONA_NATURAL_CON_NEGOCIO = 1000000000563L;
	public static final long PERSONA_NATURAL_SIN_NEGOCIO = 1000000000564L;
	public static final long NINGUNA = 1000000000801L;
	
	static {
		descError.put(OMITIR_HOMONIIA,"OMITIR_HOMONIIA");
		descError.put(NO_OMITIR_HOMONIMIA,"NO_OMITIR_HOMONIMIA");
		descError.put(PERSONA_NATURAL_CON_NEGOCIO, "PERSONA NATURAL CON NEGOCIO");
		descError.put(PERSONA_NATURAL_SIN_NEGOCIO, "PERSONA SIN NEGOCIO");
		descError.put(NINGUNA, "NINGUNA");
	}

}