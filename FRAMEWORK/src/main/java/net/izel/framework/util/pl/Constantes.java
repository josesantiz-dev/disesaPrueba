
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes
*
*/

package net.izel.framework.util.pl;

import java.util.HashMap;

public class Constantes { 
	
	/*IDENTIFICADOR DE LOS MODULOS*/
	/*CLIENTES*/
	public static final long MODULO_CLIENTES = 1L;
	
	/*CREDITO*/
	public static final long MODULO_CREDITO = 2L;
	
	/*AHORRO*/
	public static final long MODULO_AHORRO = 3L;
	
	/*GARANTIA*/
	public static final long MODULO_GARANTIAS = 4L;
	
	/*ADMINISTRACION*/
	public static final long MODULO_ADMINISTRACION = 5L;
	
	/*PLATAFORMA*/
	public static final long MODULO_PLATAFORMA = 6L;
	
	/*PUBLICO*/
	public static final long PUBLICO = 7L;
	
	/*SUBTIPO GATANTIA CUENTA AHORRO*/
	public static final long SUBTIPO_GARANTIA_CUENTA_AHORRO = 1000000001391L;
	
	/*SISTEMA AHORRO IZEL*/
	public static final Boolean SISTEMA_AHORRO_IZEL = false;
	
	/*REGISTROS DE LOS MODULOS*/
	public static HashMap<Long, String> descModulo = new HashMap<Long, String>();
	
	static {
		descModulo.put(MODULO_CLIENTES,"MODULO DE CLIENTES");
		descModulo.put(MODULO_CREDITO,"MODULO DE CREDITO");
		descModulo.put(MODULO_GARANTIAS,"MODULO DE GARANTIAS");
		descModulo.put(MODULO_AHORRO,"MODULO DE AHORRO");
		descModulo.put(MODULO_ADMINISTRACION,"MODULO DE ADMINISTRACION");
		descModulo.put(MODULO_PLATAFORMA,"MODULO DE PLATAFORMA");
		descModulo.put(PUBLICO,"MODULO PUBLICO");
		descModulo.put(SUBTIPO_GARANTIA_CUENTA_AHORRO,"SUBTIPO GARANTIA CUENTA AHORRO");
	}

	/*MEDIOS DE PAGO*/
	public static final long EFECTIVO = 1000000000001L;
	
	public static final long CHEQUES = 1000000000002L;
	
	public static final long TRANSFERENCIA = 1000000000003L;
	
	public static final long ABONO = 1000000000005L;
	
	public static final long CARGO = 1000000000004L;
	
	public static final long INTERNO = 1000000000006L;
	
	public static final long CUENTA_CONTABLE = 1000000000007L;
	
	/*REGISTROS DE LOS MEDIOS DE PAGO*/
	public static HashMap<Long, String> descMediosPago = new HashMap<Long, String>();
	
	static {
		descMediosPago.put(EFECTIVO,"EFECTIVO");
		descMediosPago.put(CHEQUES,"CHEQUES");
		descMediosPago.put(TRANSFERENCIA,"TRANSFERENCIA");
		descMediosPago.put(ABONO,"ABONO");
		descMediosPago.put(CARGO,"CARGO");
		descMediosPago.put(INTERNO, "INTERNO");
	}
}