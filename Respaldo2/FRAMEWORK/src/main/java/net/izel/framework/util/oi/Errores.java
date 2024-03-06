package net.izel.framework.util.oi;

import java.util.HashMap;

public class Errores { 

	/*TRANSACCION_EXITOSA*/
	public static final long TRANSACCION_EXITOSA = 0;
	
	/*ERROR INESPERADO*/
	public static final long ERROR_INESPERADO = 1;
	public static final long ERROR_CONECTAR_WS = 2;
	public static final long ERROR_CONSUMIR_WS = 3;
	public static final long CAMPOS_REQUERIDOS = 4;
	public static final long ERROR_CONSULTA = 5;
	public static final long ERROR_OBTENER_EJB = 6;
	public static final long NO_EXISTE_CATALOGO = 7;
	public static final long EL_CATALOGO_YA_EXISTE = 8;
	public static final long SELECCIONE_CATALOGO = 9;	
	public static final long SELECCIONE_CATEGORIA = 10;
	public static final long CARGUE_CATALOGOS=11;
	/*CAPTURE_CAMPOS_REQUERIDOS*/
	public static final long CAPTURE_CAMPOS_REQUERIDOS = 12;
	

	public static final long  ERROR_COMPRA_MONEDA_CALLS= 13;
	public static final long  ERROR_VENTA_MONEDA_CALLS= 14;
	public static final long  ERROR_CREAR_CALLS= 15;
	public static final long  ERROR_CREAR_TRANSACCION= 16;
	public static final long  ERROR_BUSCAR_TRANSACCION= 17;
	
	/*No se pudo convertir datos entre arreglo y lista*/
	public static final long ERROR_GENERAR_ARRAY_LIST = 10005;

	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	static {

		descError.put(TRANSACCION_EXITOSA,"Transacción Exitosa");
		descError.put(ERROR_INESPERADO,"Error inesperado");
		descError.put(ERROR_CONECTAR_WS,"Error al conectar al WS" );
		descError.put(ERROR_CONSUMIR_WS,"Error al consumir el WS" );
		descError.put(CAMPOS_REQUERIDOS,"Campos requeridos");
		descError.put(ERROR_CONSULTA ,"Error en la consulta");
		descError.put(ERROR_OBTENER_EJB,"Error al obtener el EJB" );
		descError.put(NO_EXISTE_CATALOGO,"No existe catalogo" );
		descError.put(EL_CATALOGO_YA_EXISTE,"El catalogo ya existe");
		descError.put(SELECCIONE_CATALOGO,"Seleccione catalogo");
		descError.put(SELECCIONE_CATEGORIA,"Seleccione Categoria");
		descError.put(CARGUE_CATALOGOS,"Cargue catalogo");
		descError.put(CAPTURE_CAMPOS_REQUERIDOS,"Capture campos requeridos" );
		descError.put(ERROR_GENERAR_ARRAY_LIST,"No se pudo convertir datos entre arreglo y lista");
		
		descError.put(ERROR_COMPRA_MONEDA_CALLS,"Error al llamar a la funcion Compra moneda");
		descError.put(ERROR_VENTA_MONEDA_CALLS,"Error al llamar a la funcion Venta moneda");
		descError.put(ERROR_CREAR_CALLS,"Error al llamar a la funcion Crear");
		descError.put(ERROR_CREAR_TRANSACCION,"Error al crear la transaccion");
		descError.put(ERROR_BUSCAR_TRANSACCION, "No se encontro transaccion core para anulacion");
		
	}
}