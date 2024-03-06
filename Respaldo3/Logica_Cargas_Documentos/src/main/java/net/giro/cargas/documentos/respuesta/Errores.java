package net.giro.cargas.documentos.respuesta;

import java.util.HashMap;

public class Errores { 
	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	/** Transaccion exitosa **/
	public static final long TRANSACCION_EXITOSA = 0;
	/** Error inesperado **/
	public static final long ERROR_INESPERADO = 1;
	/** Verifique, debe seleccionar un criterio del listado de Tipo de busqueda **/
	public static final long CRITERIO_BUSQUEDA = 2;
	/** Capture campos requeridos **/
	public static final long CAMPOS_REQUERIDOS = 3;
	/** Listado vacio, favor de verificar **/
	public static final long LISTADO_VACIO = 4;
	/** Error al analizar XML **/
	public static final long ERROR_ANALIZAR_XML = 5;
	/** El Documento ya ha sido agregado previamente **/
	public static final long ERROR_XML_PREVIO = 6;
	/** El Receptor del Documento no es valido **/
	public static final long ERROR_EMPRESA_INVALIDA = 7;
	/** El Receptor del Documento no es valido **/
	public static final long ERROR_XML_SALDADO = 8;
	/** El CFDI esta cancelado **/
	public static final long ERROR_CFDI_CANCELADO = 9;
	/** El RFC del Emisor del Documento es distincto al RFC de referencia **/
	public static final long ERROR_XML_RFC_EMISOR = 10;
	/** El RFC del Emisor del Documento es distincto al RFC de referencia **/
	public static final long ERROR_TIPOCOMPROBANTE_INVALIDO = 11;

	static {
		descError.put(TRANSACCION_EXITOSA,"Transaccion exitosa");
		descError.put(ERROR_INESPERADO,"Error inesperado");
		descError.put(CRITERIO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de busqueda");
		descError.put(CAMPOS_REQUERIDOS,"Capture campos requeridos");
		descError.put(LISTADO_VACIO,"Listado vacio, favor de verificar");
		descError.put(ERROR_ANALIZAR_XML, "Error al validar XML");
		descError.put(ERROR_XML_PREVIO, "El Documento ya ha sido agregado previamente");
		descError.put(ERROR_EMPRESA_INVALIDA, "El Receptor del Documento no es valido");
		descError.put(ERROR_XML_SALDADO, "El Documento ya ha sido saldado");
		descError.put(ERROR_CFDI_CANCELADO, "El CFDI esta cancelado");
		descError.put(ERROR_XML_RFC_EMISOR, "El RFC del Emisor del Documento es distincto al RFC de referencia");
		descError.put(ERROR_TIPOCOMPROBANTE_INVALIDO, "El Tipo de Comprobante del Documento no es valido");
	}
}
