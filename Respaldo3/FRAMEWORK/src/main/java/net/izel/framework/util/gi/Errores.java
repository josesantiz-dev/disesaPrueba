package net.izel.framework.util.gi;

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
	

	public static final long  ERROR_CREAR_EMISION_GIRO= 13;
	public static final long  ERROR_EMISION_GIRO_CALLS= 14;
	
	public static final long  ERROR_PAGO_GIRO_CALLS= 15;
	public static final long  ERROR_NO_EXISTE_EMISION_GIRO= 16;
	public static final long  ERROR_GIRO_YA_FUE_PAGADO= 17;
	public static final long  ERROR_ACTUALIZAR_PAGO_GIRO= 18;
	
	public static final long  ERROR_GIRO_NO_EXISTE= 19;
	public static final long  ERROR_OBTENER_GIRO= 20;
	public static final long  ERROR_ANULAR_GIRO= 21;
	public static final long  ERROR_ANULA_GIRO_CALLS= 22;
	
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
		
		descError.put(ERROR_CREAR_EMISION_GIRO,"Error al efectuar la emision de giro");
		descError.put(ERROR_EMISION_GIRO_CALLS,"Error al llamar a la funcion EMISION GIRO");
		
		descError.put(ERROR_PAGO_GIRO_CALLS,"Error al llamar a la funcion PAGO GIRO");
		descError.put(ERROR_NO_EXISTE_EMISION_GIRO,"No existe giro");
		descError.put(ERROR_GIRO_YA_FUE_PAGADO,"El giro ya fue pagado");
		descError.put(ERROR_ACTUALIZAR_PAGO_GIRO,"Error la efectuar pago de giro");
		
		descError.put(ERROR_GIRO_NO_EXISTE,"Error la giro no existe");
	    descError.put(ERROR_OBTENER_GIRO,"Error al obtener el giro");
		descError.put(ERROR_ANULAR_GIRO,"Error al anular el giro");
		descError.put(ERROR_ANULA_GIRO_CALLS,"Error al llamar a la funcion anulaGiro");

		
		
	}
}