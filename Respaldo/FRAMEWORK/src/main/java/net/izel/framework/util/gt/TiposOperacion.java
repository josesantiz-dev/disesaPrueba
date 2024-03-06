
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de estatus en servicio Clientes
*
*/

package net.izel.framework.util.gt;

import java.util.HashMap;

public class TiposOperacion {
	
	/*TIPOS DE OPERACIONES*/
	
	/*TIPO DE OPERACION DESEMBOLSADA*/
	public static final Long DESEMBOLSADA = 1000000000613L;
	
	/*TIPO DE OPERACION SIN DESEMBOLSAR*/
	public static final Long SIN_DESEMBOLSAR = 1000000000614L;
	
	
	/*ESTATUS DE LOS REGISTROS DEL MODULO DE GARANTIA*/
	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	static {
		descError.put(DESEMBOLSADA,"TIPO DE OPERACION DESEMBOLSADA");
		descError.put(SIN_DESEMBOLSAR,"TIPO DE OPERACION SIN DESEMBOLSAR");
	}

}