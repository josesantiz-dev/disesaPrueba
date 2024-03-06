
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

public class EstatusCoberturaOperacion {
	
	/*ESTATUS DE COBERTURAS DE OPERACIONES*/
	
	/*VIGENTE*/
	public static final Long COBERTURA_VIGENTE = 1000000000626L;
	
	/*NO_VIGENTE*/
	public static final Long COBERTURA_NO_VIGENTE = 1000000000627L;
	
	/*ANULADA*/
	public static final Long COBERTURA_ANULADA = 1000000000628L;
	
	/*ESTATUS DE LOS REGISTROS DEL MODULO DE GARANTIA*/
	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	static {
		descError.put(COBERTURA_VIGENTE,"VIGENTE");
		descError.put(COBERTURA_NO_VIGENTE,"NO_VIGENTE");
		descError.put(COBERTURA_ANULADA,"ANULADA");
	}

}