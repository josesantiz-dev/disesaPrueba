
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de estatus en servicio Clientes
*
*/

package net.izel.framework.util.cat;

import java.util.HashMap;

public class Estatus { 
	/*ESTATUS DE LOS REGISTROS DEL MODULO PUBLICO*/
	/*Activo - Alta*/
	public static final long ESTATUS_ALTA = 100000000004L;
	/*Inactivo - Baja*/
	public static final long ESTATUS_BAJA = 100000000005L;
	/*ESTATUS DE LOS REGISTROS DEL MODULO DE CLIENTES*/
	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	static {
		descError.put(ESTATUS_ALTA,"ALTA");
		descError.put(ESTATUS_BAJA,"BAJA");
	}

}