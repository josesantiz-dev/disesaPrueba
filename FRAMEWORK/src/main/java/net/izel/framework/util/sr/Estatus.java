
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de estatus en servicio Clientes
*
*/

package net.izel.framework.util.sr;

import java.util.HashMap;

public class Estatus { 
	public static final long ESTATUS_ACTIVO = 100000000004L;
	public static final long ESTATUS_INACTIVO = 100000000005L;

	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	static {
		descError.put(ESTATUS_ACTIVO,"ACTIVO");
		descError.put(ESTATUS_INACTIVO,"INACTIVO");
	}

}