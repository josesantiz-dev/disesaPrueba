
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

public class TipoGarantia {
	
	/*TIPOS DE GARANTIAS*/
	/*FISICA*/
	public static final Long FISICA = 1000000000593L;
	
	/*TITULO_VALOR*/
	public static final Long TITULO_VALOR = 1000000000594L;
	
	/*PERSONAL*/
	public static final Long PERSONAL = 1000000000595L;
	
	/*LIQUIDA*/
	public static final Long LIQUIDA = 1000000000596L;

	/*ESTATUS DE LOS REGISTROS DEL MODULO DE GARANTIA*/
	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	static {
		descError.put(FISICA,"Garantia tipo Fisica");
		descError.put(TITULO_VALOR,"Garantia tipo Titulo o Valor");
		descError.put(PERSONAL,"Garantia tipo Personal");
		descError.put(LIQUIDA,"Garantia tipo Liquida");
	}

}