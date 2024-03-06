
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

public class TiposConceptosOperacion {
	
	/*TIPOS DE CONCEPTOS DE OPERACIONES*/
	
	/*CONCEPTOS DE OPERACION DE TRANSACCIONES*/
	public static final Long TRANSACCIONES = 1000000000606L;
	
	/*CONCEPTOS DE OPERACION COBERTURA_OPERACION*/
	public static final Long COBERTURA_OPERACION = 1000000000609L;
	
	/*CONCEPTOS DE OPERACION VALORIZACION Y SALDOS*/
	public static final Long VALORIZACIONES_SALDOS = 1000000000608L;
	
	/*CONCEPTOS DE OPERACION TASASIONES*/
	public static final Long TASASIONES = 1000000000607L;
	
	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	static {
		descError.put(TRANSACCIONES,"CONCEPTOS DE OPERACION DE TRANSACCIONES");
		descError.put(COBERTURA_OPERACION,"CONCEPTOS DE OPERACION COBERTURA OPERACION");
		descError.put(VALORIZACIONES_SALDOS,"CONCEPTOS DE OPERACION VALORIZACION Y SALDOS");
		descError.put(TASASIONES,"CONCEPTOS DE OPERACION TASASIONES");
	}
	
}