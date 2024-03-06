package net.giro.util;

import java.util.HashMap;

public class Errores { 
	/*Transaccion exitosa*/
	public static final long TRANSACCION_EXITOSA = 0;

	/*Error inesperado*/
	public static final long ERROR_INESPERADO = 1;

	/*Verifique, debe seleccionar un criterio del listado de Tipo de busqueda*/
	public static final long CRITERIO_BUSQUEDA = 2;

	/*Capture campos requeridos*/
	public static final long CAMPOS_REQUERIDOS = 3;

	/*Listado vacio, favor de verificar*/
	public static final long LISTADO_VACIO = 4;
 
	public static final long ERROR_GENERAR_REPORTE  = 5;
	public static final long ERROR_OBTENER_REPORTE  = 6;
	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(TRANSACCION_EXITOSA,"Transaccion exitosa");
		descError.put(ERROR_INESPERADO,"Error inesperado");
		descError.put(CRITERIO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de busqueda");
		descError.put(CAMPOS_REQUERIDOS,"Capture campos requeridos");
		descError.put(LISTADO_VACIO,"Listado vacio, favor de verificar");
		descError.put(ERROR_GENERAR_REPORTE,"Error al generar el reporte");
		descError.put(ERROR_OBTENER_REPORTE,"Error al obtener el reporte");
	}
}