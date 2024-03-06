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

public class EstatusGral {

	public static final long ACTIVO = 100000000004L;
	public static final long INACTIVO = 100000000005L;

	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(ACTIVO,"ACTIVO");
		descError.put(ACTIVO,"INACTIVO");
	}
}

