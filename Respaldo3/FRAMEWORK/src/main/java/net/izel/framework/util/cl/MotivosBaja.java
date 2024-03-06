
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

public class MotivosBaja {
	
	/*Motivos de baja de registros en el modulo CLIENTES*/
	
	/*BAJA POR SISTEMA*/
	public static final String BAJA_POR_SISTEMA = "BAJA_POR_SISTEMA";

	/*ESTATUS DE LOS REGISTROS DEL MODULO DE GARANTIA*/
	public static HashMap<String, String> descError = new HashMap<String, String>();
	
	static {
		descError.put(BAJA_POR_SISTEMA,"BAJA POR SISTEMA");
	}

}