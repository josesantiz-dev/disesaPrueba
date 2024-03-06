/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de errores en servicios
*
*/

package net.izel.framework.util.cat;

import java.util.HashMap;
public class Errores { 

	/*TRANSACCION EXITOSA*/
	public static final long OK = 0;

	/*USUARIO SIN EMPRESA ASIGNADA*/
	public static final long USUARIO_SIN_EMPRESA_ASIGNADA = 1;

	/*NO EXISTEN MODALIDADES DE CUOTAS DEFINIDAS*/
	public static final long NO_EX_MODALIDAD_CUOTAS = 2;

	/*ERROR INESPERADO %s*/
	public static final long ERROR_INESPERADO = 3;

	/*CAMPOS REQUERIDOS*/
	public static final long CAMPOS_REQUERIDOS = 4;

	/*NO EXISTEN DOCUMENTOS*/
	public static final long NO_EXISTEN_DOCUMENTOS = 5;

	/*ERROR EN CONSULTA GRUPO PLANTILLA DOCUMENTOS*/
	public static final long ERR_CONSULTA_GRUPO_PLANTILLA_DOCUMENTOS = 6;

	/*SELECCIONE LA MONEDA*/
	public static final long SIN_MONEDA = 7;

	/*NO EXISTE TIPO DE CAMBIO*/
	public static final long SIN_TIPO_CAMBIO = 8;

	/*SELECCIONE CATEGORIA*/
	public static final long SELECCIONE_CATEGORIA = 9;

	/*ERROR EN VALIDA EXISTE CATEGORIA*/
	public static final long ERR_VALIDA_EX_CATEGORIA = 10;

	/*NO EXISTE CATEGORIA*/
	public static final long NO_EX_CATEGORIA = 11;

	/*SELECCIONE SUBCATEGORIA*/
	public static final long SELECCIONE_SUBCATEGORIA = 12;

	/*NO EXISTE SUBCATEGORIA*/
	public static final long NO_EX_SUBCATEGORIA = 13;

	/*ERROR EN VALIDA EXISTE SUBCATEGORIA*/
	public static final long ERR_VALIDA_EX_SUBCATEGORIA = 14;

	/*LA MONEDA SOLICITADA NO SE ENCUENTRA ACTIVA O DADA DE ALTA*/
	public static final long ERROR_MONEDA_NO_EXISTE = 15;

	/*CLAVE DE OPERACION VACIA*/
	public static final long CLAVE_OPERACION_VACIA = 16;

	/*CLAVE OPERACION NO EXISTE*/
	public static final long CLAVE_OPERACION_NO_EX = 17;

	/*ERROR EN VALIDAR CLAVE OPERACION*/
	public static final long ERR_VALIDA_CLAVE_OPE = 18;

	/*IMPOSIBLE CONECTAR CON EL SERVIDOR WS*/
	public static final long ERROR_CONECTAR = 19;

	/*OCURRIO UN PROBLEMA AL CONSUMIR EL WS*/
	public static final long ERROR_CONSUMIR = 20;

	/*SE PRODUJO UN ERROR AL REALIZAR LA CONSULTA*/
	public static final long ERROR_CONSULTA = 21;

	/*SE PRODUJO UN ERROR AL OBTENER EJB*/
	public static final long ERROR_OBTENER_EJB = 22;

	/*NO EXISTE CATALOGO*/
	public static final long NO_EXISTE_CATALOGO = 100;

	/*EL CATALOGO YA EXISTE*/
	public static final long EL_CATALOGO_YA_EXISTE = 101;

	/*SELECCIONE CATALOGO*/
	public static final long SELECCIONE_CATALOGO = 102;

	/*MAYOR A CERO*/
	public static final long MAYOR_A_CERO = 103;

	/*FUERA DE RANGO*/
	public static final long FUERA_DE_RANGO = 104;

	/*EL ATRIBUTO QUE DESEA BORRAR CONTIENE VALORES QUE IMPIDEN SU ELIMINACION*/
	public static final long VALOR_DEPENDIENTE_ATRIBUTO = 105;

	/*OCCURIO UN PROBLEMA AL INTENTAR GUARDAR EL CATALOGO*/
	public static final long ERROR_CREAR_CATALOGO = 106;

	/*OCCURIO UN PROBLEMA AL INTENTAR ACTUALIZAR EL CATALOGO*/
	public static final long ERROR_ACTUALIZAR_CATALOGO = 107;

	/*OCCURIO UN PROBLEMA AL INTENTAR ELIMINAR EL CATALOGO*/
	public static final long ERROR_ELIMINAR_CATALOGO = 108;

	/*OCCURIO UN PROBLEMA AL INTENTAR GUARDAR EL ATRIBUTO DEL CATALOGO*/
	public static final long ERROR_CREAR_ATRIBUTO_CATALOGO = 109;

	/*OCCURIO UN PROBLEMA AL INTENTAR ACTUALIZAR EL ATRIBUTO DEL CATALOGO*/
	public static final long ERROR_ACTUALIZAR_ATRIBUTO_CATALOGO = 110;

	/*OCCURIO UN PROBLEMA AL INTENTAR ELIMINAR EL ATRIBUTO DEL CATALOGO*/
	public static final long ERROR_ELIMINAR_ATRIBUTO_CATALOGO = 111;

	/*ES NECESARIO SELECCIONAR UN GRUPO*/
	public static final long GRUPO_NECESARIO = 112;

	/*NOMBRE DE LA COLUMNA REQUERIDO*/
	public static final long NOMBRE_COLUMNA_REQUERIDO = 113;

	/*NOMBRE DE LA TABLA REQUERIDO*/
	public static final long NOMBRE_TABLA_REQUERIDO = 114;

	/*DEBE EXISTIR SOLO UN CAMPO DE BUSQUEDA*/
	public static final long CAMPO_BUSQUEDA_UNICO = 115;

	/*FALTAN CAMPOS DE AUDITORIA EN XML*/
	public static final long CAMPO_AUDIT_XML = 116;

	/*ERROR AL CONSULTAR LISTADO DE SUCURSALES*/
	public static final long ERR_LISTADO_SUCURSALES = 117;

	/*NO EXISTE MEDIO DE PAGO*/
	public static final long NO_EX_MEDIO_PAGO = 118;

	/*ERROR EN VALIDA EXISTE MEDIO PAGO*/
	public static final long ERR_VALIDA_EX_MEDIO_PAGO_ID = 119;

	/*CLAVE CONCEPTO TRANSACCION VACIA*/
	public static final long CVE_CONCEPTO_TRAX_VACIA = 120;

	/*NO EXISTE CLAVE CONCEPTO TRANSACCION*/
	public static final long NO_EX_CVE_CONCEPTO_TRAX = 121;

	/*ERROR EN VALIDA CLAVE CONCEPTO TRANSACCION*/
	public static final long ERR_VALIDA_CVE_CONCEPTO_TRAX = 122;

	/*CLAVE MEDIO DE PAGO VACIA*/
	public static final long CVE_MEDIO_PAGO_VACIA = 123;

	/*ERROR EN VALIDA CLAVE MEDIO PAGO*/
	public static final long ERR_VALIDA_CVE_MEDIO_PAGO = 124;

	/*ERROR_CONECTAR_WS*/
	public static final long ERROR_CONECTAR_WS = 125;

	/*ERROR_CONSUMIR_WS*/
	public static final long ERROR_CONSUMIR_WS = 126;

	/*ERROR_PROCESAR_XML*/
	public static final long ERROR_PROCESAR_XML = 127;

	/*SELECCIONE ESTADO*/
	public static final long SELECCIONE_EDO = 128;

	/*SELECCIONE MUNICIPIO*/
	public static final long SELECCIONE_MPIO = 129;

	/*NO EXISTEN REGISTROS CON LAS CONDICIONES DE BUSQUEDA*/
	public static final long NO_EX_REGISTROS_BUSQUEDA = 130;

	/*ERROR EN CONSULTA ENTIDADES FEDERATIVAS*/
	public static final long ERR_CONSULTA_ENTIDADES_FEDERATIVAS = 131;

	/*CLAVE CATALOGO VACIA*/
	public static final long CVE_CATALOGO_VACIA = 132;

	/*NO EXISTE CATALOGO*/
	public static final long NO_EX_CATALOGO = 133;

	/*ERROR AL VALIDAR CLAVE VALOR*/
	public static final long ERR_VALIDAR_CVE_VALOR = 134;

	/*ERROR EN CONSULTA MUNICIPIOS*/
	public static final long ERR_CONSULTA_MUNICIPIOS = 135;

	/*ERROR EN CONSULTA LOCALIDADES*/
	public static final long ERR_CONSULTA_LOCALIDADES = 136;

	/*ERROR EN CONSULTA COLONIA*/
	public static final long ERR_CONSULTA_COLONIA = 137;

	/*SELECCIONE COLONIA*/
	public static final long SELECCIONE_COLONIA = 138;

	/*INFORMACION INCOMPLETA DE COLONIA*/
	public static final long INF_INCOMPLETA_COLONIA = 139;

	/*ERROR OBTENER CADENA COLONIA*/
	public static final long ERR_OBTENER_CADENA_COLONIA = 140;

	/*Falta capturar catalogo*/
	public static final long ATRIBUTO_VACIO = 141;

	/*VERIFIQUE INICIALES DE RFC*/
	public static final long VERIFIQUE_RFC1 = 142;

	/*ERROR AL VALIDAR LISTADO SUBTIPO GARANTIA*/
	public static final long ERROR_VALIDAR_LISTADO_SUBTIPO_GARANTIA = 143;

	/*ERROR AL VALIDAR LISTADO VALORES POR GRUPO ATRIBUTO*/
	public static final long ERROR_VALIDAR_LISTADO_VALORES_POR_GRUPO_ATRIBUTO = 144;

	/*ERROR AL OBTENER LISTADO VALORES POR GRUPO ATRIBUTO*/
	public static final long ERROR_OBTENER_LISTADO_VALORES_POR_GRUPO_ATRIBUTO = 145;

	/*VERIFIQUE FECHA DE RFC*/
	public static final long VERIFIQUE_RFC2 = 146;

	/*ERROR FALTA ATRIBUTO VACIO*/
	public static final long ERROR_ATRIBUTO_VACIO = 147;

	/*ERROR AL VALIDAR RFC*/
	public static final long ERR_VALIDAR_RFC = 148;

	/*VERIFIQUE FORMATO DE RFC*/
	public static final long VERIFIQUE_RFC = 149;

	/*FALTA INFORMACION PARA SECTORIZACION*/
	public static final long FALTA_INF_SECTORIZACION = 150;

	/*LA COLONIA NO PERTENECE AL OFICIAL*/
	public static final long COLONIA_NO_PERTENECE_OFICIAL = 151;

	/*ERROR EN VALIDA COLONIA PROMOTOR*/
	public static final long ERR_VALIDA_COLONIA_PROMOTOR = 152;

	/*ERROR AL CONSULTAR LISTADO DE CANALES*/
	public static final long ERR_LISTADO_CANAL = 154;

	/*SELECCIONE PAIS DE NACIMIENTO*/
	public static final long SELECCIONE_PAIS_NACIMIENTO = 155;

	/*ERROR AL BUSCAR NACIONALIDAD*/
	public static final long ERROR_BUSCAR_NACIONALIDAD = 156;

	/*ERROR AL OBTENER LA CLAVE*/
	public static final long ERROR_OBTENER_CLAVE = 157;

	/*ERROR AL CONSULTA VALOR ID*/
	public static final long ERR_VALIDAR_CONSULTA_VALOR_ID = 158;

	/*VERIFIQUE FECHAS*/
	public static final long VERIFIQUE_FECHAS = 159;

	/*ELIMINE CAMPO VECES*/
	public static final long ELIMINE_CAMPO_VECES = 160;

	/*ELIMINE CAMPOS FECHAS*/
	public static final long ELIMINE_CAMPOS_FECHAS = 161;

	/*ERROR EN CONSULTA EXCEPCIONES*/
	public static final long ERR_CONSULTA_EXCE = 162;

	/*ERROR EN OBTENER RELACIONES EXCEPCIONES*/
	public static final long ERR_OBTENER_RELACIONES_EXCE = 163;

	/*SELECCIONE EXCEPCION*/
	public static final long SELECCIONE_EXCEPCION = 164;

	/*NO EXISTE EXCEPCION*/
	public static final long NO_EX_EXCEPCION = 165;

	/*ERROR EN CONSULTAR EXCEPCION POR ID*/
	public static final long ERR_CONSULTAR_EXCE_XID = 166;

	/*ERROR EN AUTORIZAR EXCEPCION*/
	public static final long ERR_AUTORIZAR_EXCE = 167;

	/*FECHA INICIAL MENOR A LA ACTUAL*/
	public static final long FECHA_INI_MENOR_ACTUAL = 168;

	/*VERIFIQUE TIPO DE OMISION*/
	public static final long VERIFIQUE_OMISION = 169;

	/*SELECCIONE PARAMETRO*/
	public static final long SELECCIONE_PARAMETRO = 170;

	/*SELECCIONE SERVICIO*/
	public static final long SELECCIONE_SERVICIO = 171;

	/*NO EXISTE PARAMETRO*/
	public static final long NO_EX_PARAMETRO = 172;

	/*ERROR EN CONSULTA PARAMETRO POR NOMBRE*/
	public static final long ERR_CONSULTA_PARAMETRO_X_NOMBRE = 173;

	/*ERROR EN VALIDAR AUTORIZAR EXCEPCION*/
	public static final long ERR_VALIDAR_AUT_EXCEP = 174;

	/*EXCEPCION YA AUTORIZADA*/
	public static final long EXCEP_YA_AUTORIZADA = 175;

	/*NO SE PERMITE AUTORIZAR LA EXCEPCION GENERADA POR ERROR*/
	public static final long EXCEP_ERROR = 176;

	/*ERROR AL CONSULTAR LISTADO DE SERVICIOS*/
	public static final long ERROR_LISTADO_SERVICIOS_ = 177;

	/*SELECCIONE UN SERVICIO*/
	public static final long SELECCIONE_UN_SERVICIO = 178;

	/*ERROR AL CONSULTAR LISTADO DE REGLAS SERVICIO*/
	public static final long ERROR_LISTADO_REGLAS_SERVICIO = 179;

	/*ERRO AL CONSULTAR LISTADO PARAMETROS*/
	public static final long ERROR_LISTADO_PARAMETROS = 180;

	/*ERROR AL AUTORIZAR REGLA SERVICIO*/
	public static final long ERROR_AUTORIZAR_REGLA_SERVICIO = 181;

	/*LA REGLA NO EXISTE*/
	public static final long REGLA_NO_EXISTE = 182;

	/*ERRO EN CONSULTA REGLA*/
	public static final long ERROR_CONSULTA_REGLA = 183;

	/*ERROR AL VALIDAR AUTORIZAR REGLA SERVICIO*/
	public static final long ERROR_VALIDAR_AUTORIZAR_REGLA_SERVICIO_ = 184;

	/*ERROR EN CONSULTA PARAMETRO POR ID*/
	public static final long ERROR_CONSULTA_PARAMETRO_X_ID = 185;

	/*SELECCIONE UNA REGLA*/
	public static final long SELECCIONE_REGLA = 186;
	
	/*OCURRIO UN ERROR AL CONSULTAR EL LISTADO DE TRANSACCIONES POR CLASE Y TIPO*/
	public static final long ERR_LISTA_CONCEPTO_TRANSACCION_CLASE_TIPO = 187;

	/*EL CONCEPTO DE TRANSACCION NO EXISTE EN EL CATALOGO*/
	public static final long ERR_CONCEPTO_TRX_NO_EX_CAT = 188;

	/*SELECCIONE CONCEPTO DE TRANSACCION*/
	public static final long SELCCIONE_CONCEPTO_TRX = 189;

	/*ERROR EN CONSULTA CONCEPTO TRANSACCION*/
	public static final long ERR_CONSULTA_CONCEPTO_TRX = 190;
	
	/*ERROR LA SUCURSAL YA EXISTE*/
	public static final long SUCURSAL_YA_EXISTE = 191;

	/*EL CONCEPTO DE SUCURSAL NO EXISTE EN EL CATALOGO*/
	public static final long ERR_CONCEPTO_SUCURSAL_NO_EX = 192;

	/*Verifique, debe seleccionar un criterio del listado de Tipo de Busqueda*/
	public static final long ERROR_TIPO_BUSQUEDA = 193;

	/*LA SUCURSAL YA ESTA DADA DE BAJA*/
	public static final long SUCURSAL_BAJA = 193;

	/*ERROR AL VALIDAR LA BAJA DE LA SUCURSAL*/
	public static final long ERROR_VALIDAR_BAJA_SUC = 194;

	/*ERROR AL VALIDAR LOS DATOS PARA DAR DE BAJA*/
	public static final long ERROR_VALIDAR_BAJA = 195;

	/*ERROR AL DAR DE BAJA LA SUCURSAL*/
	public static final long ERROR_BAJA_SUC = 196;

	/*ERROR EN VALIDAR CONSULTA SUCURSAL*/
	public static final long ERROR_VALIDA_CONSULTA_SUC = 197;

	/*ERROR AL VALIDAR CONCEPTO TRANSACCION ID, ESTA VACIO*/
	public static final long ERR_VAL_CONCEPTO_TRX_ID_VACIO = 198;

	/*ERROR AL VALIDAR CONCEPTO TRANSACCION ID, REGISTRO DE EMPRESA DIFERENTE*/
	public static final long ERR_VAL__CONCEPTO_TRX_ID_EMPRESA_DIF = 199;

	/*ERROR AL VALIDAR CONCEPTO TRANSACCION ID*/
	public static final long ERR_VAL_CONCEPTO_TRX = 200;
	
	/*ERROR AL VALIDAR LISTADO VALORES POR ATRIBUTO, CAMPOS REQUERIDOS*/
	public static final long ERR_VLVXGA_CAMPOS_REQUERIDOS = 201;

	/*ERROR AL OBTENER LISTADO DE PROGRAMAS DE APV*/
	public static final long ERR_LISTADO_PROGRAMAS_APV = 202;

	/*ERROR EN LISTADO PROGRAMAS DE APV VACIO*/
	public static final long ERR_LISTADO_PROGRAMAS_APV_VACIO = 203;

	/*NO EXISTEN PROGRAMAS ASIGNADOS AL APV*/
	public static final long NO_EXISTEN_PROGRAMAS_DE_APV = 204;

	/*ERROR EN SVC AL OBTENER LISTADO DE PROGRAMAS DE APV*/
	public static final long ERR_SVC_LISTADO_PROGRAMAS_APV = 205;

	/*ERROR EN WS CONSULTA CATALOGO*/
	public static final long ERR_WS_CONSULTA_VALOR_ID = 206;

	/*ERROR EN AL VALIDAR CONSULTA PLANTILLA DOCUMENTO POR GRUPO, CLAVE GRUPO VACIA*/
	public static final long ERR_VALIDAR_CONS_PLANTILLA_DOCTO_GPO_CLAVE_GPO_VACIA = 207;

	/*ERROR EN AL VALIDAR CONSULTA PLANTILLA DOCUMENTO POR GRUPO, MODULO VACIO*/
	public static final long ERR_VALIDAR_CONS_PLANTILLA_DOCTO_GPO_MODULO_VACIO = 208;

	/*ERROR AL VALIDAR CONSULTA PLANTILLA DOCUMENTO POR GRUPO*/
	public static final long ERR_VALIDAR_CONSULTA_PLANTILLA_DOCUMENTO_POR_GRUPO = 209;

	/*ERROR EN CONSULTA PLANTILLA DOCUMENTO POR GRUPO*/
	public static final long ERR_CONSULTA_PLANTILLA_DOCTO_GRUPO = 210;

	/*ERROR EN CONSULTA TIPO USO CANAL ATENCION ID VACIO*/
	public static final long ERR_CONSULTA_TIPO_USO_CANAL_ATENCION_ID_VACIO = 211;

	/*ERROR EN CONSULTA TIPO USO CANAL ATENCION NO EXISTE ID*/
	public static final long ERR_CONSULTA_TIPO_USO_CANAL_ATENCION_NO_EX = 212;

	/*ERROR INESPERADO EN CONSULTA TIPO USO CANAL ATENCION */
	public static final long EI_CONSULTA_TIPO_USO_CANAL_ATENCION_ = 213;
	
	/*GUARDE INFORMACION DE SUCURSAL*/
	public static final long GUARDE_INFORMACION_SUCURSAL = 214;

	/*LONGITUD DEL VALOR A BUSCAR CORTA*/
	public static final long LONGITUD_VALOR_CORTA = 215;

	/*ERROR AL CONSULTAR EMPRESA*/
	public static final long ERROR_CONSULTAR_EMPRESA = 216;

	/*ERROR AL CONSULTA EMPRESA ID*/
	public static final long ERR_CONSULTA_EMPRESA_ID = 217;

	/*NO EXISTE EMPRESA*/
	public static final long NO_EX_EMPRESA = 218;

	/*IDENTIFICADOR DE LA EMPRESA REQUERIDO*/
	public static final long EMPRESA_ID_REQUERIDO = 219;

	/*ERROR AL VALIDAR CLAVE DE CONCEPTO SUCURSAL CLAVE VACIA*/
	public static final long ERR_VAL_CVE_CONCEPTO_SUCURSAL_VACIA = 220;

	/*ERROR INESPERADO AL VALIDAR CLAVE DE CONCEPTO SUCURSAL*/
	public static final long EI_VAL_CVE_CONCEPTO_SUCURSAL = 221;

	/*ERROR INESPERADO AL CONSULTAR CONCEPTO DE DATOS SUCURSAL*/
	public static final long EI_CONSULTAR_CONCEPTO_DATOS_SUCURSAL = 222;

	/*ERROR INESPERADO AL DAR DE BAJA CONCEPTO DE DATOS SUCURSAL POR ID*/
	public static final long EI_BAJA_CONCEPTO_DATOS_SUCURSAL_ID = 223;

	/*ERROR INESPERADO AL VALIDAR BAJA CONCEPTO DATOS SUCURSAL CLAVE*/
	public static final long EI_VALIDAR_BAJA_CONCEPTO_DATOS_SUCURSAL_CLAVE = 224;

	/*ERROR AL CONSULTAR TODAS LAS DE SUCURSALES*/
	public static final long ERR_CONSULTAR_TODAS_SUCURSALES = 225;

	/*ERROR INESPERADO AL BUSCAR SUCURSAL*/
	public static final long EI_BUSCAR_SUCURSAL = 226;

	/*NO EXISTE SUCURSAL*/
	public static final long NO_EXISTE_SUCURSAL = 227;

	/*ERROR INESPERADO AL MODIFICAR SUCURSAL*/
	public static final long EI_MODIFICAR_SUCURSAL = 228;

	/*ERROR INESPERADO AL VALIDAR CONCEPTO SUCURSAL*/
	public static final long EI_VALIDAR_CONCEPTO_SUCURSAL = 229;

	/*ERROR INESPERADO AL MODIFICAR CONCEPTO SUCURSAL*/
	public static final long EI_MODIFICAR_CONCEPTO_SUCURSAL = 230;

	/*ERROR INESPERADO AL VALIDAR DATOS SUCURSAL*/
	public static final long EI_VALIDAR_DATOS_SUCURSAL = 231;

	/*ERROR EN WS CONSULTA CATALOGO POR CLAVE*/
	public static final long ERR_WS_CONSULTA_VALOR_CLAVE = 232;

	/*ERROR AL CREAR NUEVA SUCURSAL*/
	public static final long ERR_CREAR_SUCURSAL = 233;

	/*ERROR AL BUSCAR PERSONA NATURAL REPRESENTANTE*/
	public static final long ERR_BUSCAR_PERSONA = 234;

	/*LA SUCURSAL YA ESTA DADA DE BAJA*/
	public static final long ERR_SUCURSAL_ES_BAJA = 235;
	
	/*SUCURSAL NO ESPECIFICADA*/
	public static final long FALTA_SUCURSAL_ID = 236;
	
	/*ERROR AL BUSCAR PUNTO DE ATENCION*/
	public static final long ERR_BUSCAR_PUNTO_ATENCION = 237;

	/*NO EXISTE PUNTO DE ATENCION*/
	public static final long ERR_NO_EXISTE_PTO_ATENCION = 238;

	/*ERROR AL CONSULTAR CANAL DE ATENCION*/
	public static final long ERR_CONSULTA_CANAL_ATENCION = 239;

	/*ERROR EN CONSULTA DATOS EMPRESA COMPLETO EMPRESA VACIA*/
	public static final long ERR_CONSULTA_DATOS_EMPRESA_COMPLETO_ID_VACIA = 240;

	/*ERROR INESPERADO EN CONSULTA DATOS EMPRESA COMPLETO*/
	public static final long EI_CONSULTA_DATOS_EMPRESA_COMPLETO = 241;

	/*ERROR AL CONSULTAR POR VALOR POR ID*/
	public static final long ERR_CONSULTA_POR_VALOR_POR_ID = 242;
	
	/*FALTA INFORMACION DE LA EMPRESA ASIGANDA AL SISTEMA CONTABLE*/
	public static final long FALTA_INF_EMPRESA_BACKOFFICE = 243;

	/*ERROR AL OBTENER LISTADO INSTITUCIONES BANCARIAS*/
	public static final long ERROR_LISTADO_INSTITUCIONES_BANCARIAS = 244;

	/*ERROR AL CONSULTAR SUCURSALES ASIGNADAS A USUARIO*/
	public static final long ERR_CONSULTA_SUCURSAL_USUARIO = 245;
	
	/*ERROR AL ENVIAR UNA PLANTILLA  POR CORREO*/
	public static final long ERR_ENVIAR_PLANTILLA_CORREO = 246;
	
	/*ERROR AL OBTENER RESPUESTA IMPORTACION MANUAL*/
	public static final long ERR_OBTENER_IMPORTACION_MANUAL = 247;
	
	/*ERROR INESPERADO WS CONSULTA CONCEPTO TRANSACCION CLAVE*/
	public static final long ERR_WS_CONSULTA_CONCEPTO_TRANSACCION_CLAVE = 248;

	/*ERROR INESPERADO WS CONSULTA OPERACION CLAVE*/
	public static final long ERR_WS_CONSULTA_OPERACION_CLAVE = 249;
	
	/*ERROR INESPERADO AL BUSCAR MEDIO DE PAGO POR CLAVE*/
	public static final long EI_BUSCAR_MEDIO_PAGOXCLAVE  = 250;
	
	/*ERROR AL BUSCAR SUCURSAL POR ID DEL SISTEMA DE CARTERA ID VACIA*/
	public static final long BUS_SUC_SIST_ANTERIOR_ID_VACIO  = 251;
	
	/*ERROR AL BUSCAR SUCURSAL POR ID DEL SISTEMA DE CARTERA EMPRESA VACIA*/
	public static final long BUS_SUC_SIST_ANTERIOR_EMPRESAID_VACIO  = 252;
	
	/*ERROR INESPERADO AL BUSCAR SUCURSAL POR SISTEMA ANTERIOR ID*/
	public static final long EI_BUS_SUC_SIST_ANTERIOR  = 253;
	
	/*ERROR AL BUSCAR CANAL POR REFERENCIAID VACIO*/
	public static final long BUS_PUCANAL_REF_CVTA_REFERENCIAID_VACIO  = 254;
	
	/*ERROR AL BUSCAR CANAL POR REFERENCIA EMPRESAID VACIO*/
	public static final long BUS_PUCANAL_REF_CVTA_EMPRESAID_VACIO  = 255;
	
	/*ERROR AL BUSCAR CANAL POR REFERENCIA CANAL VENTA VACIO*/
	public static final long BUS_PUCANAL_REF_CVTA_CANALVTAID_VACIO  = 256;
	
	/*ERROR INESPERADO AL BUSCAR CANAL POR REFERENCIA Y CANAL DE VENTA*/
	public static final long EI_BUS_PUCANAL_REF_CVTA   = 257;
	
	/*IDENTIFICADOR DE USUARIO REQUERIDO PARA BUSCAR USUARIO POR ID*/
	public static final long BUPI_USUARIO_ID_REQUERIDO = 258;

	/*IDENTIFICADOR DE LA EMPRESA REQUERIDO PARA BUSCAR USUARIO POR ID*/
	public static final long BUPI_EMPRESA_ID_REQUERIDO = 259;

	/*ERROR INESPERADO AL BUSCAR USUARIO POR ID*/
	public static final long EI_AL_BUSCAR_USUARIO_POR_ID = 260;

	/*EL USUARIO NO PERTENECE A LA EMPRESA AL BUSCARLO POR ID*/
	public static final long BUPI_USUARIO_NO_PERTENECE_EMPRESA = 261;

	/*EL USUARIO NO EXISE AL BUSCAR POR ID*/
	public static final long BUPI_USUARIO_NO_EXISTE = 262;
	
	/*ACTUALICE CONCEPTO NUMERO SOLICITANTE DE LA PERSONA*/
	public static final long ACTUALICE_NUM_SOL = 263;

	/*PERSONA SIN CUENTAS QUE CUMPLAN CON LA VIGENCIA*/
	public static final long PERSONA_SIN_CUENTAS = 264;
	
	/*ERROR INESPERADO CONSULTA CUENTAS PERSONA*/
	public static final long EI_CONSULTA_CUENTAS_PERSONA = 265;
	
	
	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(EI_BUS_PUCANAL_REF_CVTA,"ERROR INESPERADO AL BUSCAR CANAL POR REFERENCIA Y CANAL DE VENTA");
		
		descError.put(OK,"TRANSACCION EXITOSA");
		descError.put(USUARIO_SIN_EMPRESA_ASIGNADA,"USUARIO SIN EMPRESA ASIGNADA");
		descError.put(NO_EX_MODALIDAD_CUOTAS,"NO EXISTEN MODALIDADES DE CUOTAS DEFINIDAS");
		descError.put(ERROR_INESPERADO,"ERROR INESPERADO %s");
		descError.put(CAMPOS_REQUERIDOS,"CAMPOS %s REQUERIDOS");
		descError.put(NO_EXISTEN_DOCUMENTOS,"NO EXISTEN DOCUMENTOS");
		descError.put(ERR_CONSULTA_GRUPO_PLANTILLA_DOCUMENTOS,"ERROR EN CONSULTA GRUPO PLANTILLA DOCUMENTOS");
		descError.put(SIN_MONEDA,"SELECCIONE LA MONEDA");
		descError.put(SIN_TIPO_CAMBIO,"NO EXISTE TIPO DE CAMBIO");
		descError.put(SELECCIONE_CATEGORIA,"SELECCIONE CATEGORIA");
		descError.put(ERR_VALIDA_EX_CATEGORIA,"ERROR EN VALIDA EXISTE CATEGORIA");
		descError.put(NO_EX_CATEGORIA,"NO EXISTE CATEGORIA");
		descError.put(SELECCIONE_SUBCATEGORIA,"SELECCIONE SUBCATEGORIA");
		descError.put(NO_EX_SUBCATEGORIA,"NO EXISTE SUBCATEGORIA");
		descError.put(ERR_VALIDA_EX_SUBCATEGORIA,"ERROR EN VALIDA EXISTE SUBCATEGORIA");
		descError.put(ERROR_MONEDA_NO_EXISTE,"LA MONEDA SOLICITADA NO SE ENCUENTRA ACTIVA O DADA DE ALTA");
		descError.put(CLAVE_OPERACION_VACIA,"CLAVE DE OPERACION VACIA");
		descError.put(CLAVE_OPERACION_NO_EX,"CLAVE OPERACION NO EXISTE");
		descError.put(ERR_VALIDA_CLAVE_OPE,"ERROR EN VALIDAR CLAVE OPERACION");
		descError.put(ERROR_CONECTAR,"IMPOSIBLE CONECTAR CON EL SERVIDOR WS");
		descError.put(ERROR_CONSUMIR,"OCURRIO UN PROBLEMA AL CONSUMIR EL WS");
		descError.put(ERROR_CONSULTA,"SE PRODUJO UN ERROR AL REALIZAR LA CONSULTA");
		descError.put(ERROR_OBTENER_EJB,"SE PRODUJO UN ERROR AL OBTENER EJB");
		descError.put(NO_EXISTE_CATALOGO,"NO EXISTE CATALOGO");
		descError.put(EL_CATALOGO_YA_EXISTE,"EL CATALOGO YA EXISTE");
		descError.put(SELECCIONE_CATALOGO,"SELECCIONE CATALOGO");
		descError.put(MAYOR_A_CERO,"MAYOR A CERO");
		descError.put(FUERA_DE_RANGO,"FUERA DE RANGO");
		descError.put(VALOR_DEPENDIENTE_ATRIBUTO,"EL ATRIBUTO QUE DESEA BORRAR CONTIENE VALORES QUE IMPIDEN SU ELIMINACION");
		descError.put(ERROR_CREAR_CATALOGO,"OCCURIO UN PROBLEMA AL INTENTAR GUARDAR EL CATALOGO");
		descError.put(ERROR_ACTUALIZAR_CATALOGO,"OCCURIO UN PROBLEMA AL INTENTAR ACTUALIZAR EL CATALOGO");
		descError.put(ERROR_ELIMINAR_CATALOGO,"OCCURIO UN PROBLEMA AL INTENTAR ELIMINAR EL CATALOGO");
		descError.put(ERROR_CREAR_ATRIBUTO_CATALOGO,"OCCURIO UN PROBLEMA AL INTENTAR GUARDAR EL ATRIBUTO DEL CATALOGO");
		descError.put(ERROR_ACTUALIZAR_ATRIBUTO_CATALOGO,"OCCURIO UN PROBLEMA AL INTENTAR ACTUALIZAR EL ATRIBUTO DEL CATALOGO");
		descError.put(ERROR_ELIMINAR_ATRIBUTO_CATALOGO,"OCCURIO UN PROBLEMA AL INTENTAR ELIMINAR EL ATRIBUTO DEL CATALOGO");
		descError.put(GRUPO_NECESARIO,"ES NECESARIO SELECCIONAR UN GRUPO");
		descError.put(NOMBRE_COLUMNA_REQUERIDO,"NOMBRE DE LA COLUMNA REQUERIDO");
		descError.put(NOMBRE_TABLA_REQUERIDO,"NOMBRE DE LA TABLA REQUERIDO");
		descError.put(CAMPO_BUSQUEDA_UNICO,"DEBE EXISTIR SOLO UN CAMPO DE BUSQUEDA");
		descError.put(CAMPO_AUDIT_XML,"FALTAN CAMPOS DE AUDITORIA EN XML");
		descError.put(ERR_LISTADO_SUCURSALES,"ERROR AL CONSULTAR LISTADO DE SUCURSALES");
		descError.put(NO_EX_MEDIO_PAGO,"NO EXISTE MEDIO DE PAGO");
		descError.put(ERR_VALIDA_EX_MEDIO_PAGO_ID,"ERROR EN VALIDA EXISTE MEDIO PAGO");
		descError.put(CVE_CONCEPTO_TRAX_VACIA,"CLAVE CONCEPTO TRANSACCION VACIA");
		descError.put(NO_EX_CVE_CONCEPTO_TRAX,"NO EXISTE CLAVE CONCEPTO TRANSACCION");
		descError.put(ERR_VALIDA_CVE_CONCEPTO_TRAX,"ERROR EN VALIDA CLAVE CONCEPTO TRANSACCION");
		descError.put(CVE_MEDIO_PAGO_VACIA,"CLAVE MEDIO DE PAGO VACIA");
		descError.put(ERR_VALIDA_CVE_MEDIO_PAGO,"ERROR EN VALIDA CLAVE MEDIO PAGO");
		descError.put(ERROR_CONECTAR_WS,"ERROR_CONECTAR_WS");
		descError.put(ERROR_CONSUMIR_WS,"ERROR_CONSUMIR_WS");
		descError.put(ERROR_PROCESAR_XML,"ERROR_PROCESAR_XML");
		descError.put(SELECCIONE_EDO,"SELECCIONE ESTADO");
		descError.put(SELECCIONE_MPIO,"SELECCIONE MUNICIPIO");
		descError.put(NO_EX_REGISTROS_BUSQUEDA,"NO EXISTEN REGISTROS CON LAS CONDICIONES DE BUSQUEDA");
		descError.put(ERR_CONSULTA_ENTIDADES_FEDERATIVAS,"ERROR EN CONSULTA ENTIDADES FEDERATIVAS");
		descError.put(CVE_CATALOGO_VACIA,"CLAVE CATALOGO VACIA");
		descError.put(NO_EX_CATALOGO,"NO EXISTE CATALOGO");
		descError.put(ERR_VALIDAR_CVE_VALOR,"ERROR AL VALIDAR CLAVE VALOR");
		descError.put(ERR_CONSULTA_MUNICIPIOS,"ERROR EN CONSULTA MUNICIPIOS");
		descError.put(ERR_CONSULTA_LOCALIDADES,"ERROR EN CONSULTA LOCALIDADES");
		descError.put(ERR_CONSULTA_COLONIA,"ERROR EN CONSULTA COLONIA");
		descError.put(SELECCIONE_COLONIA,"SELECCIONE COLONIA");
		descError.put(INF_INCOMPLETA_COLONIA,"INFORMACION INCOMPLETA DE COLONIA");
		descError.put(ERR_OBTENER_CADENA_COLONIA,"ERROR OBTENER CADENA COLONIA");
		descError.put(ATRIBUTO_VACIO,"Falta capturar catalogo");
		descError.put(VERIFIQUE_RFC1,"VERIFIQUE INICIALES DE RFC");
		descError.put(ERROR_VALIDAR_LISTADO_SUBTIPO_GARANTIA,"ERROR AL VALIDAR LISTADO SUBTIPO GARANTIA");
		descError.put(ERROR_VALIDAR_LISTADO_VALORES_POR_GRUPO_ATRIBUTO,"ERROR AL VALIDAR LISTADO VALORES POR GRUPO ATRIBUTO");
		descError.put(ERROR_OBTENER_LISTADO_VALORES_POR_GRUPO_ATRIBUTO,"ERROR AL OBTENER LISTADO VALORES POR GRUPO ATRIBUTO");
		descError.put(VERIFIQUE_RFC2,"VERIFIQUE FECHA DE RFC");
		descError.put(ERROR_ATRIBUTO_VACIO,"ERROR FALTA ATRIBUTO VACIO");
		descError.put(ERR_VALIDAR_RFC,"ERROR AL VALIDAR RFC");
		descError.put(VERIFIQUE_RFC,"VERIFIQUE FORMATO DE RFC");
		descError.put(FALTA_INF_SECTORIZACION,"FALTA INFORMACION PARA SECTORIZACION");
		descError.put(COLONIA_NO_PERTENECE_OFICIAL,"LA COLONIA NO PERTENECE AL OFICIAL");
		descError.put(ERR_VALIDA_COLONIA_PROMOTOR,"ERROR EN VALIDA COLONIA PROMOTOR");
		descError.put(ERR_LISTADO_CANAL,"ERROR AL CONSULTAR LISTADO DE CANALES");
		descError.put(SELECCIONE_PAIS_NACIMIENTO,"SELECCIONE PAIS DE NACIMIENTO");
		descError.put(ERROR_BUSCAR_NACIONALIDAD,"ERROR AL BUSCAR NACIONALIDAD");
		descError.put(ERROR_OBTENER_CLAVE,"ERROR AL OBTENER LA CLAVE");
		descError.put(ERR_VALIDAR_CONSULTA_VALOR_ID,"ERROR AL CONSULTA VALOR ID");
		descError.put(VERIFIQUE_FECHAS,"VERIFIQUE FECHAS");
		descError.put(ELIMINE_CAMPO_VECES,"ELIMINE CAMPO VECES");
		descError.put(ELIMINE_CAMPOS_FECHAS,"ELIMINE CAMPOS FECHAS");
		descError.put(ERR_CONSULTA_EXCE,"ERROR EN CONSULTA EXCEPCIONES");
		descError.put(ERR_OBTENER_RELACIONES_EXCE,"ERROR EN OBTENER RELACIONES EXCEPCIONES");
		descError.put(SELECCIONE_EXCEPCION,"SELECCIONE EXCEPCION");
		descError.put(NO_EX_EXCEPCION,"NO EXISTE EXCEPCION");
		descError.put(ERR_CONSULTAR_EXCE_XID,"ERROR EN CONSULTAR EXCEPCION POR ID");
		descError.put(ERR_AUTORIZAR_EXCE,"ERROR EN AUTORIZAR EXCEPCION");
		descError.put(FECHA_INI_MENOR_ACTUAL,"FECHA INICIAL MENOR A LA ACTUAL");
		descError.put(VERIFIQUE_OMISION,"VERIFIQUE TIPO DE OMISION");
		descError.put(SELECCIONE_PARAMETRO,"SELECCIONE PARAMETRO");
		descError.put(SELECCIONE_SERVICIO,"SELECCIONE SERVICIO");
		descError.put(NO_EX_PARAMETRO,"NO EXISTE PARAMETRO");
		descError.put(ERR_CONSULTA_PARAMETRO_X_NOMBRE,"ERROR EN CONSULTA PARAMETRO POR NOMBRE");
		descError.put(ERR_VALIDAR_AUT_EXCEP,"ERROR EN VALIDAR AUTORIZAR EXCEPCION");
		descError.put(EXCEP_YA_AUTORIZADA,"EXCEPCION YA AUTORIZADA");
		descError.put(EXCEP_ERROR,"NO SE PERMITE AUTORIZAR LA EXCEPCION GENERADA POR ERROR");
		descError.put(ERROR_LISTADO_SERVICIOS_,"ERROR AL CONSULTAR LISTADO DE SERVICIOS");
		descError.put(SELECCIONE_UN_SERVICIO,"SELECCIONE UN SERVICIO");
		descError.put(ERROR_LISTADO_REGLAS_SERVICIO,"ERROR AL CONSULTAR LISTADO DE REGLAS SERVICIO");
		descError.put(ERROR_LISTADO_PARAMETROS,"ERRO AL CONSULTAR LISTADO PARAMETROS");
		descError.put(ERROR_AUTORIZAR_REGLA_SERVICIO,"ERROR AL AUTORIZAR REGLA SERVICIO");
		descError.put(REGLA_NO_EXISTE,"LA REGLA NO EXISTE");
		descError.put(ERROR_CONSULTA_REGLA,"ERRO EN CONSULTA REGLA");
		descError.put(ERROR_VALIDAR_AUTORIZAR_REGLA_SERVICIO_,"ERROR AL VALIDAR AUTORIZAR REGLA SERVICIO");
		descError.put(ERROR_CONSULTA_PARAMETRO_X_ID,"ERROR EN CONSULTA PARAMETRO POR ID");
		descError.put(SELECCIONE_REGLA,"SELECCIONE UNA REGLA");
		descError.put(ERR_LISTA_CONCEPTO_TRANSACCION_CLASE_TIPO,"OCURRIO UN ERROR AL CONSULTAR EL LISTADO DE TRANSACCIONES POR CLASE Y TIPO");
		descError.put(ERR_CONCEPTO_TRX_NO_EX_CAT,"EL CONCEPTO DE TRANSACCION NO EXISTE EN EL CATALOGO");
		descError.put(SELCCIONE_CONCEPTO_TRX,"SELECCIONE CONCEPTO DE TRANSACCION");
		descError.put(ERR_CONSULTA_CONCEPTO_TRX,"ERROR EN CONSULTA CONCEPTO TRANSACCION");
		descError.put(SUCURSAL_YA_EXISTE,"ERROR LA SUCURSAL YA EXISTE");
		descError.put(ERR_CONCEPTO_SUCURSAL_NO_EX,"EL CONCEPTO DE SUCURSAL NO EXISTE EN EL CATALOGO");
		descError.put(ERROR_TIPO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de Busqueda");
		descError.put(SUCURSAL_BAJA,"LA SUCURSAL YA ESTA DADA DE BAJA");
		descError.put(ERROR_VALIDAR_BAJA_SUC,"ERROR AL VALIDAR LA BAJA DE LA SUCURSAL");
		descError.put(ERROR_VALIDAR_BAJA,"ERROR AL VALIDAR LOS DATOS PARA DAR DE BAJA");
		descError.put(ERROR_BAJA_SUC,"ERROR AL DAR DE BAJA LA SUCURSAL");
		descError.put(ERROR_VALIDA_CONSULTA_SUC,"ERROR EN VALIDAR CONSULTA SUCURSAL");
		descError.put(ERR_VAL_CONCEPTO_TRX_ID_VACIO,"ERROR AL VALIDAR CONCEPTO TRANSACCION ID, ESTA VACIO");
		descError.put(ERR_VAL__CONCEPTO_TRX_ID_EMPRESA_DIF,"ERROR AL VALIDAR CONCEPTO TRANSACCION ID, REGISTRO DE EMPRESA DIFERENTE");
		descError.put(ERR_VAL_CONCEPTO_TRX,"ERROR AL VALIDAR CONCEPTO TRANSACCION ID");
		descError.put(ERR_VLVXGA_CAMPOS_REQUERIDOS,"ERROR AL VALIDAR LISTADO VALORES POR ATRIBUTO, CAMPOS REQUERIDOS");
		descError.put(ERR_LISTADO_PROGRAMAS_APV,"ERROR AL OBTENER LISTADO DE PROGRAMAS DE APV");
		descError.put(ERR_LISTADO_PROGRAMAS_APV_VACIO,"ERROR EN LISTADO PROGRAMAS DE APV VACIO");
		descError.put(NO_EXISTEN_PROGRAMAS_DE_APV,"NO EXISTEN PROGRAMAS ASIGNADOS AL APV");
		descError.put(ERR_SVC_LISTADO_PROGRAMAS_APV,"ERROR EN SVC AL OBTENER LISTADO DE PROGRAMAS DE APV");
		descError.put(ERR_WS_CONSULTA_VALOR_ID,"ERROR EN WS CONSULTA CATALOGO");
		descError.put(ERR_VALIDAR_CONS_PLANTILLA_DOCTO_GPO_CLAVE_GPO_VACIA,"ERROR EN AL VALIDAR CONSULTA PLANTILLA DOCUMENTO POR GRUPO, CLAVE GRUPO VACIA");
		descError.put(ERR_VALIDAR_CONS_PLANTILLA_DOCTO_GPO_MODULO_VACIO,"ERROR EN AL VALIDAR CONSULTA PLANTILLA DOCUMENTO POR GRUPO, MODULO VACIO");
		descError.put(ERR_VALIDAR_CONSULTA_PLANTILLA_DOCUMENTO_POR_GRUPO,"ERROR AL VALIDAR CONSULTA PLANTILLA DOCUMENTO POR GRUPO");
		descError.put(ERR_CONSULTA_PLANTILLA_DOCTO_GRUPO,"ERROR EN CONSULTA PLANTILLA DOCUMENTO POR GRUPO");
		descError.put(ERR_CONSULTA_TIPO_USO_CANAL_ATENCION_ID_VACIO,"ERROR EN CONSULTA TIPO USO CANAL ATENCION ID VACIO");
		descError.put(ERR_CONSULTA_TIPO_USO_CANAL_ATENCION_NO_EX,"ERROR EN CONSULTA TIPO USO CANAL ATENCION NO EXISTE ID");
		descError.put(EI_CONSULTA_TIPO_USO_CANAL_ATENCION_,"ERROR INESPERADO EN CONSULTA TIPO USO CANAL ATENCION");
		descError.put(GUARDE_INFORMACION_SUCURSAL,"GUARDE INFORMACION DE SUCURSAL");
		descError.put(LONGITUD_VALOR_CORTA,"LONGITUD DEL VALOR A BUSCAR CORTA");
		descError.put(ERROR_CONSULTAR_EMPRESA,"ERROR AL CONSULTAR EMPRESA");
		descError.put(ERR_CONSULTA_EMPRESA_ID,"ERROR AL CONSULTA EMPRESA ID");
		descError.put(NO_EX_EMPRESA,"NO EXISTE EMPRESA");
		descError.put(EMPRESA_ID_REQUERIDO,"IDENTIFICADOR DE LA EMPRESA REQUERIDO");
		descError.put(ERR_VAL_CVE_CONCEPTO_SUCURSAL_VACIA,"ERROR AL VALIDAR CLAVE DE CONCEPTO SUCURSAL CLAVE VACIA");
		descError.put(EI_VAL_CVE_CONCEPTO_SUCURSAL,"ERROR INESPERADO AL VALIDAR CLAVE DE CONCEPTO SUCURSAL");
		descError.put(EI_CONSULTAR_CONCEPTO_DATOS_SUCURSAL,"ERROR INESPERADO AL CONSULTAR CONCEPTO DE DATOS SUCURSAL");
		descError.put(EI_BAJA_CONCEPTO_DATOS_SUCURSAL_ID,"ERROR INESPERADO AL DAR DE BAJA CONCEPTO DE DATOS SUCURSAL POR ID");
		descError.put(EI_VALIDAR_BAJA_CONCEPTO_DATOS_SUCURSAL_CLAVE,"ERROR INESPERADO AL VALIDAR BAJA CONCEPTO DATOS SUCURSAL CLAVE");
		descError.put(ERR_CONSULTAR_TODAS_SUCURSALES,"ERROR AL CONSULTAR TODAS LAS DE SUCURSALES");
		descError.put(EI_BUSCAR_SUCURSAL,"ERROR INESPERADO AL BUSCAR SUCURSAL");
		descError.put(NO_EXISTE_SUCURSAL,"NO EXISTE SUCURSAL");
		descError.put(EI_MODIFICAR_SUCURSAL,"ERROR INESPERADO AL MODIFICAR SUCURSAL");
		descError.put(EI_VALIDAR_CONCEPTO_SUCURSAL,"ERROR INESPERADO AL VALIDAR CONCEPTO SUCURSAL");
		descError.put(EI_MODIFICAR_CONCEPTO_SUCURSAL,"ERROR INESPERADO AL MODIFICAR CONCEPTO SUCURSAL");
		descError.put(EI_VALIDAR_DATOS_SUCURSAL,"ERROR INESPERADO AL VALIDAR DATOS SUCURSAL");
		descError.put(ERR_WS_CONSULTA_VALOR_CLAVE,"ERROR EN WS CONSULTA CATALOGO POR CLAVE");
		descError.put(ERR_CREAR_SUCURSAL,"ERROR AL CREAR NUEVA SUCURSAL");
		descError.put(ERR_BUSCAR_PERSONA,"ERROR AL BUSCAR PERSONA NATURAL REPRESENTANTE");
		descError.put(ERR_SUCURSAL_ES_BAJA,"LA SUCURSAL YA ESTA DADA DE BAJA");
		descError.put(FALTA_SUCURSAL_ID,"SUCURSAL NO ESPECIFICADA");
		descError.put(ERR_BUSCAR_PUNTO_ATENCION,"ERROR AL BUSCAR PUNTO DE ATENCION");
		descError.put(ERR_NO_EXISTE_PTO_ATENCION,"NO EXISTE PUNTO DE ATENCION");
		descError.put(ERR_CONSULTA_CANAL_ATENCION,"ERROR AL CONSULTAR CANAL DE ATENCION");
		descError.put(ERR_CONSULTA_DATOS_EMPRESA_COMPLETO_ID_VACIA,"ERROR EN CONSULTA DATOS EMPRESA COMPLETO EMPRESA VACIA");
		descError.put(EI_CONSULTA_DATOS_EMPRESA_COMPLETO,"ERROR INESPERADO EN CONSULTA DATOS EMPRESA COMPLETO");
		descError.put(ERR_CONSULTA_POR_VALOR_POR_ID,"ERROR AL CONSULTAR POR VALOR POR ID");
		descError.put(FALTA_INF_EMPRESA_BACKOFFICE,"FALTA INFORMACION DE LA EMPRESA ASIGANDA AL SISTEMA CONTABLE");
		descError.put(ERROR_LISTADO_INSTITUCIONES_BANCARIAS,"ERROR AL OBTENER LISTADO INSTITUCIONES BANCARIAS");
		descError.put(ERR_CONSULTA_SUCURSAL_USUARIO,"ERROR AL CONSULTAR SUCURSALES ASIGNADAS A USUARIO");
		descError.put(ERR_ENVIAR_PLANTILLA_CORREO,"ERROR AL ENVIAR UNA PLANTILLA  POR CORREO");
		descError.put(ERR_OBTENER_IMPORTACION_MANUAL, "ERROR AL OBTENER RESPUESTA DE IMPORTACION MANUAL");
		descError.put(ERR_WS_CONSULTA_CONCEPTO_TRANSACCION_CLAVE,"ERROR INESPERADO WS CONSULTA CONCEPTO TRANSACCION CLAVE");
		descError.put(ERR_WS_CONSULTA_OPERACION_CLAVE,"ERROR INESPERADO WS CONSULTA OPERACION CLAVE");
		descError.put(EI_BUSCAR_MEDIO_PAGOXCLAVE,"ERROR INESPERADO AL BUSCAR MEDIO DE PAGO POR CLAVE");
		descError.put(BUS_SUC_SIST_ANTERIOR_ID_VACIO,"ERROR AL BUSCAR SUCURSAL POR ID DEL SISTEMA DE CARTERA ID VACIA");
		descError.put(BUS_SUC_SIST_ANTERIOR_EMPRESAID_VACIO,"ERROR AL BUSCAR SUCURSAL POR ID DEL SISTEMA DE CARTERA EMPRESA VACIA");
		descError.put(EI_BUS_SUC_SIST_ANTERIOR,"ERROR INESPERADO AL BUSCAR SUCURSAL POR SISTEMA ANTERIOR ID");
		descError.put(BUS_PUCANAL_REF_CVTA_REFERENCIAID_VACIO,"ERROR AL BUSCAR CANAL POR REFERENCIAID VACIO");
		descError.put(BUS_PUCANAL_REF_CVTA_EMPRESAID_VACIO,"ERROR AL BUSCAR CANAL POR REFERENCIA EMPRESAID VACIO");
		descError.put(BUS_PUCANAL_REF_CVTA_CANALVTAID_VACIO,"ERROR AL BUSCAR CANAL POR REFERENCIA CANAL VENTA VACIO");
		descError.put(BUPI_USUARIO_ID_REQUERIDO,"IDENTIFICADOR DE USUARIO REQUERIDO PARA BUSCAR USUARIO POR ID");
		descError.put(BUPI_EMPRESA_ID_REQUERIDO,"IDENTIFICADOR DE LA EMPRESA REQUERIDO PARA BUSCAR USUARIO POR ID");
		descError.put(EI_AL_BUSCAR_USUARIO_POR_ID,"ERROR INESPERADO AL BUSCAR USUARIO POR ID");
		descError.put(BUPI_USUARIO_NO_PERTENECE_EMPRESA,"EL USUARIO NO PERTENECE A LA EMPRESA AL BUSCARLO POR ID");
		descError.put(BUPI_USUARIO_NO_EXISTE,"EL USUARIO NO EXISE AL BUSCAR POR ID");
		descError.put(ACTUALICE_NUM_SOL,"ACTUALICE CONCEPTO NUMERO SOLICITANTE DE LA PERSONA");
		descError.put(PERSONA_SIN_CUENTAS,"PERSONA SIN CUENTAS QUE CUMPLAN CON LA VIGENCIA");
		descError.put(EI_CONSULTA_CUENTAS_PERSONA,"ERROR INESPERADO CONSULTA CUENTAS PERSONA");
	}

}
