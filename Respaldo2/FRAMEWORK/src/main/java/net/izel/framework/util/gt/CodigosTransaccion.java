
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

public class CodigosTransaccion {
	
	/*CODIGOS DE TRANSACCIONES*/
	
	public static final Long REGISTRO_GARANTIA = 100000000001L;
	public static final Long ACTUALIZACION_GARANTIA = 100000000002L;
	public static final Long TASACION_GARANTIA = 100000000003L;
	public static final Long BAJA_TASACION_GARANTIA = 100000000004L;
	public static final Long ANULACION_GARANTIA = 100000000005L;
	public static final Long ACTIVACION_GARANTIA = 100000000006L;
	public static final Long COBERTURA_SOLICITUD_CREDITO = 100000000007L;
	public static final Long LIBERACION_COBERTURA_SOLICITUD_CREDITO = 100000000008L;
	public static final Long COBERTURA_CREDITO = 100000000009L;
	public static final Long LIBERACION_COBERTURA_CREDITO = 100000000010L;
	public static final Long BLOQUEO_TOTAL_GARANTIA = 100000000011L;
	public static final Long LIBERACION_BLOQUEO_TOTAL_GARANTIA = 100000000012L;
	public static final Long BLOQUEO_SALDO_GARANTIA = 100000000013L;
	public static final Long LIBERACION_BLOQUEO_SALDO_GARANTIA = 100000000014L;
	public static final Long ENTREGA_GARANTIA = 100000000015L;
	public static final Long VALORIZACION_GARANTIA = 100000000030L;
	public static final Long REGISTRO_DACION_PAGO = 100000000031L;
	public static final Long ACTUALIZAR_DACION_PAGO = 100000000032L;
	public static final Long TASACION_DACION_PAGO = 100000000033L;


	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		
		descError.put(REGISTRO_GARANTIA, "REGISTRO DE GARANTIA");
		descError.put(ACTUALIZACION_GARANTIA, "ACTUALIZACION DE GARANTIA");
		descError.put(TASACION_GARANTIA, "TASACION DE GARANTIA");
		descError.put(BAJA_TASACION_GARANTIA, "BAJA TASACION GARANTIA");
		descError.put(ANULACION_GARANTIA, "ANULACION DE GARANTIA");
		descError.put(ACTIVACION_GARANTIA, "ACTIVACION DE LA GARANTIA");
		descError.put(COBERTURA_SOLICITUD_CREDITO, "COBERTURA DE SOLICITUD DE CREDITO");
		descError.put(LIBERACION_COBERTURA_SOLICITUD_CREDITO, "LIBERACION DE COBERTURA DE SOLICITUD DE CREDITO");
		descError.put(COBERTURA_CREDITO, "COBERTURA DE CREDITO");
		descError.put(LIBERACION_COBERTURA_CREDITO, "LIBERACION COBERTURA DE CREDITO");
		descError.put(BLOQUEO_TOTAL_GARANTIA, "BLOQUEO TOTAL DE GARANTIA");
		descError.put(LIBERACION_BLOQUEO_TOTAL_GARANTIA, "LIBERACION DE BLOQUEO TOTAL DE GARANTIA");
		descError.put(BLOQUEO_SALDO_GARANTIA, "BLOQUEO DE SALDO DE GARANTIA");
		descError.put(LIBERACION_BLOQUEO_SALDO_GARANTIA, "LIBERACION DE BLOQUEO DE SALDO DE GARANTIA");
		descError.put(ENTREGA_GARANTIA, "ENTREGA DE GARANTIA");
		descError.put(VALORIZACION_GARANTIA, "VALORIZACION GARANTIA");
		descError.put(REGISTRO_DACION_PAGO, "REGISTRO DACION PAGO");
		descError.put(ACTUALIZAR_DACION_PAGO, "ACTUALIZAR DACION PAGO");
		descError.put(TASACION_DACION_PAGO, "TASACION DACION PAGO");
		

	}

}