package net.giro.cargas.documentos.respuesta;

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
	
	/*Error al analizar XML*/
	public static final long ERROR_ANALIZAR_XML = 5;
	
	/*El Documento ya ha sido agregado previamente*/
	public static final long ERROR_XML_PREVIO = 6;


	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(TRANSACCION_EXITOSA,"Transaccion exitosa");
		descError.put(ERROR_INESPERADO,"Error inesperado");
		descError.put(CRITERIO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de busqueda");
		descError.put(CAMPOS_REQUERIDOS,"Capture campos requeridos");
		descError.put(LISTADO_VACIO,"Listado vacio, favor de verificar");
		descError.put(ERROR_ANALIZAR_XML, "Error al validar XML");
		descError.put(ERROR_XML_PREVIO, "El Documento ya ha sido agregado previamente");
	}
}
