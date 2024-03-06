package net.giro.util.cxp;

import java.util.HashMap;

public class Errores { 
	/*Transaccion Exitosa*/ 
	public static final long TRANSACCION_EXITOSA = 0;

	/*Error Inesperado*/
	public static final long ERROR_INESPERADO = 1;

	/*Verifique, debe seleccionar un criterio del listado de Tipo de busqueda*/
	public static final long CRITERIO_BUSQUEDA = 2;

	/*Capture campos requeridos*/
	public static final long CAMPOS_REQUERIDOS = 3;

	/*Error al buscar cuentas bancarias*/
	public static final long ERROR_BUSCAR_CUENTAS_BANCO = 4;
	
	/*Error al buscar personas*/
	public static final long ERROR_BUSCAR_PERSONAS = 5;
	
	/*Error al buscar movimientos de cuentas*/
	public static final long ERROR_BUSCAR_MOVIMIENTOS_CUENTAS = 6;
	
	/*Error al guardar movimientos de cuentas*/
	public static final long ERROR_GUARDAR_MOVIMIENTOS_CUENTAS = 7;
	
	/*Error al generar cheque*/
	public static final long ERROR_GENERAR_CHEQUE = 8;
	
	/*Error al buscar sucursales*/
	public static final long ERROR_BUSCAR_SUCURSALES = 9;
	
	/*Debe agregar por lo menos un comprobante*/
	public static final long ERROR_FALTAN_FACTURAS = 10;
	
	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(TRANSACCION_EXITOSA,"Transaccion Exitosa");
		descError.put(ERROR_INESPERADO,"Error Inesperado");
		descError.put(CRITERIO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de busqueda");
		descError.put(CAMPOS_REQUERIDOS,"Capture campos requeridos");
		descError.put(ERROR_BUSCAR_CUENTAS_BANCO, "Error al buscar cuentas bancarias");
		descError.put(ERROR_BUSCAR_PERSONAS, "Error al buscar personas");
		descError.put(ERROR_BUSCAR_MOVIMIENTOS_CUENTAS, "Error al buscar movimientos de cuentas");
		descError.put(ERROR_GUARDAR_MOVIMIENTOS_CUENTAS, "Error al guardar movimientos de cuentas");
		descError.put(ERROR_GENERAR_CHEQUE, "Error al generar cheque");
		descError.put(ERROR_BUSCAR_SUCURSALES, "Error al buscar sucursales");
		descError.put(ERROR_FALTAN_FACTURAS, "Debe agregar por lo menos un comprobante");
	}
}