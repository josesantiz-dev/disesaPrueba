
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

public class ConceptosOperacion {
	
	/*CONCEPTOS DE OPERACION DE TRANSACCIONES*/

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
	public static final Long VALORIZACION_GARANTIA = 100000000029L;
	public static final Long REGISTRO_DACION_PAGO = 100000000030L;
	public static final Long ACTUALIZAR_DACION_PAGO = 100000000031L;
	public static final Long TASACION_DACION_PAGO = 100000000032L;
	
	/*CONCEPTOS DE OPERACION COBERTURA*/

	public static final Long MONTO_COBERTURA_OPERACION = 100000000016L;
	public static final Long MONTO_COBERTURA_GARANTIA = 100000000017L;
	public static final Long TIPO_CAMBIO = 100000000018L;

	/*CONCEPTOS DE OPERACION TASASIONES*/
	
	public static final Long VALOR_CONSTITUCION_GARANTIA = 100000000019L;
	public static final Long VALOR_COMERCIAL_GARANTIA = 100000000020L;
	public static final Long VALOR_REALIZACION_GARANTIA = 100000000021L;
	public static final Long VALOR_MERCADO = 100000000022L;
	
	/*CONCEPTOS DE OPERACION VALORIZACION Y SALDOS*/
	
	public static final Long MONTO_TOTAL_REAL_UTILIZADO = 100000000023L;
	public static final Long MONTO_TOTAL_COMPROMETIDO_NO_DESEMBOLSADAS = 100000000024L;
	public static final Long MONTO_TOTAL_BLOQUEADO = 100000000025L;
	public static final Long SALDO_REAL = 100000000026L;
	public static final Long SALDO_DISPONIBLE = 100000000027L;
	public static final Long MONTO_NO_DISPONIBLE = 100000000028L;
	
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
		descError.put(VALORIZACION_GARANTIA, "VALORIZACION DE GARANTIA");
		descError.put(REGISTRO_DACION_PAGO, "REGISTRO DE DACION EN PAGO");
		
		descError.put(MONTO_COBERTURA_OPERACION, "MONTO_COBERTURA_OPERACION");
		descError.put(MONTO_COBERTURA_GARANTIA, "MONTO_COBERTURA_GARANTIA");
		descError.put(TIPO_CAMBIO, "TIPO_CAMBIO");
		
		descError.put(VALOR_CONSTITUCION_GARANTIA, "VALOR CONSTITUCION GARANTIA");
		descError.put(VALOR_COMERCIAL_GARANTIA, "VALOR COMERCIAL GARANTIA");
		descError.put(VALOR_REALIZACION_GARANTIA, "VALOR REALIZACION GARANTIA");
		descError.put(VALOR_MERCADO, "VALOR MERCADO");
		
	}

}