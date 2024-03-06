package net.izel.framework.util.so;

import java.util.HashMap;


/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Programador: 		Ivan Castro Maldonado
* Fecha Creacion: 	11/Abr/2013
* 
* Clase de definicion de constantes de errores en servicios
*
*/

public class Errores { 


	/*CAMPO %s REQUERIDO*/
	public static final long CAPTURE_CAMPOS_REQUERIDOS = 1;

	/*Usuario sin empresa asignada*/
	public static final long USUARIO_SIN_EMPRESA = 2;

	/*No existen solicitudes pendientes*/
	public static final long NO_EXISTEN_SOLICITUDES_PENDIENTES = 3;

	/*CLAVE DE ESTADO VACIO*/
	public static final long CLAVE_ESTADO_VACIO = 4;

	/*CLAVE DE ESTADO NO EXISTE*/
	public static final long CLAVE_ESTADO_NO_EXISTE = 5;

	/*Seleccione cliente*/
	public static final long SELECCIONE_CLIENTE = 6;

	/*NO EXISTE SOLICITUD*/
	public static final long NO_EXISTE_SOLICITUD = 7;

	/*Seleccione solicitud*/
	public static final long SELECCIONE_SOLICITUD = 8;

	/*CLAVE DE CATALOGO NO EXISTE*/
	public static final long CLAVE_CATALOGO_NO_EX = 9;

	/*CLAVE DE CATALOGO VACIA*/
	public static final long CLAVE_CATALOGO_VACIA = 10;

	/*ERROR AL CONSULTAR SOLICITUDES*/
	public static final long ERR_CONSULTAR_SOLICITUDES = 11;

	/*NO EXISTEN TITULARES*/
	public static final long NO_EX_TITULARES = 12;

	/*ERROR AL CONSULTAR TITULARES*/
	public static final long ERR_CONSULTAR_TITULARES = 13;

	/*MONTO DE TITULARES NO ESPECIFICADO*/
	public static final long MTO_TITULARES_NO_ESPECIFICADO = 14;

	/*ERROR AL CONSULTAR MONTO DE TITULARES*/
	public static final long ERR_SUMAMONTO_TITULARES = 15;

	/*El prospecto ya esta registrado o genero duplicidad. Desea continuar?*/
	public static final long PROSPECTO_DUPLICADO = 16;

	/*Verifique el campo Fecha nacimiento selecciono una fecha mayor al dia actual*/
	public static final long fecha_nacimiento_invalida = 17;

	/*SELECCIONE PRESTAMO*/
	public static final long SELECCIONE_PRESTAMO = 18;

	/*ERROR EN ACTUALIZA SOLICITUD ACREDITADA*/
	public static final long ERR_ACT_SOLICITUD_ACREDITADA = 19;

	/*ERROR EN VALIDA ESTADO SOLICITUD CLAVE*/
	public static final long ERR_VALIDA_EDO_SOLICITUD_CVE = 20;

	/*Monto solicitado fuera de rango permitido del producto.*/
	public static final long MONTO_FUERA_RANGO = 21;

	/*NO EXISTEN INTEGRANTES DE GRUPO*/
	public static final long NO_EX_GRUPO_INTEGRANTES = 22;

	/*ERROR AL CONSULTAR INTEGRANTES DE GRUPO*/
	public static final long ERR_CONSULTA_GRUPO_INTEGRANTES = 23;

	/*Verifique, fecha desde mayor al dia actual*/
	public static final long FECHA_DESDE_MAYOR = 24;

	/*Verifique, debe seleccionar un criterio del listado de Tipo de busqueda*/
	public static final long SIN_CRITERIO_BUSQUEDA = 25;

	/*ERROR AL OBTENER SUMA MONTO INTEGRANTES*/
	public static final long ERR_SUMA_MTO_INTEGRANTES = 26;

	/*Capture al menos 2 caracteres para realizar la busqueda*/
	public static final long ALMENOS_DOS_CARACTERES = 27;

	/*Verifique el tipo y subtipo de (las) garantia(s) coberturando la solicitud*/
	public static final long TIPO_SUBTIPO_GTIA = 28;

	/*Porcentaje total de cobertura de garantias incorrecto*/
	public static final long PORCENTAJE_COBERTURA_INCORRECTO = 29;

	/*El producto requiere garantia sabana*/
	public static final long SIN_GTIA_SABANA = 30;

	/*El producto requiere garantia especifica*/
	public static final long SIN_GTIA_ESPECIFICA = 31;

	/*Verifique porcentaje de garantias en la solicitud respecto a definidos en el producto*/
	public static final long ERRONEA_PROPORCION_GARANTIA_SOLICITUD = 32;

	/*El producto no requiere cobertura de garantias*/
	public static final long ERROR_CON_GTIAS = 33;

	/*Seleccione prospecto*/
	public static final long SIN_PROSPECTO = 34;

	/*ERROR EN NUEVO DOMICILIO PROSPECTO*/
	public static final long ERR_NUEVO_DOMICILIO_PROSPECTO = 35;

	/*seleccione el tipo de consulta de historial crediticio*/
	public static final long SIN_TIPO_CONSULTA = 36;

	/*CAPTURE INFORMACION ZONIFICACION*/
	public static final long CAP_INF_ZONIFICACION = 37;

	/*CAPTURE INFORMACION RESIDENCIA*/
	public static final long CAP_INF_RESIDENCIA = 38;

	/*ERROR EN VALIDAR DOMICILIO PROSPECTO*/
	public static final long ERR_VALIDAR_DOMICILIO_PROSPECTO = 39;

	/* CAPTURE INFORMACION TIPO ZONIFICACION*/
	public static final long CAP_INF_TIPO_ZONIFICACION = 40;

	/*la consulta de historial crediticio no esta vigente*/
	public static final long CONSULTA_CREDITICIA_INACTIVA = 41;

	/*El prospecto ya ha sido rechazado.*/
	public static final long PROSPECTO_RECHAZADO = 42;

	/*El prospecto ya ha sido autorizado*/
	public static final long prospecto_autorizado = 43;

	/*Seleccione la entidad a la que revisara el estatus*/
	public static final long SIN_ENTIDAD = 44;

	/*Selecciones el tipo entidad a reviar su estatus*/
	public static final long SIN_TIPO_ENTIDAD = 45;

	/*Seleccione producto*/
	public static final long SIN_PRODUCTO = 46;

	/*VERIFIQUE FORMATO DE RFC*/
	public static final long VERIFIQUE_RFC = 47;

	/*SELECCIONE PERSONA*/
	public static final long SIN_PERSONA = 48;

	/*NO EXISTE PROSPECTO*/
	public static final long NO_EX_PROSPECTO = 49;

	/*ERROR EN ACTUALIZA DOMICILIO PROSPECTO*/
	public static final long ERR_ACTUALIZA_DOMICILIO_PROSPECTO = 50;

	/*El producto de credito ha sido dado de baja*/
	public static final long PRODUCTO_INACTIVO = 51;

	/*ERROR EN NUEVO TELEFONO PROSPECTO*/
	public static final long ERR_NUEVO_TEL_PROSPECTO = 52;

	/*ERROR EN VALIDAR TELEFONO PROSPECTO*/
	public static final long ERR_VALIDAR_TELEFONO_PROSPECTO = 53;

	/*ERROR EN ACTUALIZA TELEFONO PROSPECTO*/
	public static final long ERR_ACTUALIZA_TELEFONO_PROSPECTO = 54;

	/*VERIFIQUE GENERO*/
	public static final long VERIFIQUE_GENERO = 55;

	/*ERROR VALIDAR PROSPECTO*/
	public static final long ERR_VALIDAR_PROSPECTO = 56;

	/*ERROR EN VALIDAR HOMONIMIA DUPLICIDAD*/
	public static final long ERR_VALIDAR_HOMONIMIA_DUPLICIDAD = 58;

	/*ERROR EN NUEVO PROSPECTO*/
	public static final long ERR_NUEVO_PROSPECTO = 59;

	/*El prospecto no tiene consultas de historial crediticio registradas.*/
	public static final long SIN_CONSULTA_CREDITICIA = 60;

	/*No hay documentos asociados*/
	public static final long SIN_DOCUMENTOS = 61;

	/*Verifique, fecha menor al dia actual.*/
	public static final long FECHA_MENOR = 62;

	/*ERROR VALIDAR IDENTIFICACION PROSPECTO*/
	public static final long ERR_VALIDAR_IDE_PROSPECTO = 63;

	/*ERROR EN NUEVO PROSPECTO IDENTIFICACION*/
	public static final long ERR_NVO_PROSPECTO_IDE = 64;

	/*ERROR EN REGISTRAR PROSPECTO NATURAL COMPLETO*/
	public static final long ERR_REG_PROSPECTO_NATURAL_COMPLETO = 65;

	/*ERROR EN ACTUALIZA INDENTIFICACION PROSPECTO*/
	public static final long ERR_ACTUALIZA_IDE_PROSPECTO = 66;

	/*ERROR AL CONCATENAR DOMICILIO*/
	public static final long ERR_CONCATENAR_DOMICILIO = 67;

	/*DOMICILIO VACIO, VERIFIQUE CATALOGOS*/
	public static final long DOMICILIO_VACIO = 68;

	/*ERROR EN CONCATENAR TELEFONO*/
	public static final long ERR_CONCATENAR_TELEFONO = 69;

	/*ASESOR INACTIVO*/
	public static final long ASESOR_INACTIVO = 70;

	/*Seleccione negocio*/
	public static final long SIN_NEGOCIO = 71;

	/*Verifique, negocio dado de baja*/
	public static final long NEGOCIO_INACTIVO = 72;

	/*Verifique, solicitud acreditada*/
	public static final long SOLICITUD_ACREDITADA = 73;

	/*Verifique,  la fecha desde menor a la actual.*/
	public static final long FECHA_DESDE_MENOR = 74;

	/*ULTIMA CONSULTA SIN CALIFICACION*/
	public static final long CONSULTA_SIN_CALIFICACION = 75;

	/*Seleccione garantia*/
	public static final long SIN_GARANTIA = 76;

	/*Verifique fecha desde y fecha hasta estan invertidas.*/
	public static final long FECHAS_INVERTIDAS = 77;

	/*Verifique, el listado de conceptos del valor de la cobertura esta vacio.*/
	public static final long LISTADO_CONCEPTOS_COBERTURA_VACIO = 78;

	/*CALIFACION NO FAVORABLE*/
	public static final long CALIFACION_NO_FAVORABLE = 79;

	/*ERROR EN VALIDA HISTORIAL CREDITICIO*/
	public static final long ERR_VALIDA_HIST_CREDITICIO = 80;

	/*Seleccione moneda de la solicitud.*/
	public static final long SIN_MONEDA_SOLICITUD = 81;

	/*NO EXISTE DOMICILIO*/
	public static final long NO_EX_DOMICILIO = 82;

	/*ERROR EN CONSULTA DOMICILIO PROSPECTO*/
	public static final long ERR_CONSULTA_DOMICILIO_PROSPECTO = 83;

	/*FALTA INFORMACION PARA SECTORIZACION*/
	public static final long FALTA_INF_SECTORIZACION = 84;

	/*Cobertutura ya ha sido dado liberada*/
	public static final long COBERTURA_SOLICITUD_INACTIVA = 85;

	/*ERROR EN VALIDAR AUTORIZAR PROSPECTO*/
	public static final long ERR_VALIDAR_AUT_PROSPECTO = 86;

	/*ERROR AL AUTORIZAR PROSPECTO*/
	public static final long ERR_AUTORIZAR_PROSPECTO = 87;

	/*Verifique, error actualizando la cobertura en garantias.*/
	public static final long ERR_COBERTURA_GARANTIAS = 88;

	/*NO EXISTE TELEFONO*/
	public static final long NO_EX_TELEFONO = 89;

	/*ERROR CONSULTA TELEFONO PROSPECTO*/
	public static final long ERR_CONSULTA_TEL_PROSPECTO = 90;

	/*ERROR EN CONSULTA INFORMACION PROSPECTO*/
	public static final long ERR_CONSULTA_INF_PROSPECTO = 91;

	/*primero busque y seleccione una cobertura de solicitud*/
	public static final long SIN_COBERTURA_SOLICITUD = 92;

	/*Cobertura ya ha sido liberado*/
	public static final long COBERTURA_LIBERADA = 93;

	/*NO EXISTE PROSPECTO NATURAL*/
	public static final long NO_EX_PROSPECTO_NAT = 94;

	/*ERROR EN CONSULTA PROSPECTO NATURAL*/
	public static final long ERR_CONSULTA_PROSPECTO_NAT = 95;

	/*NO EXISTE IDENTIFICACION*/
	public static final long NO_EX_IDENTIFICACION = 96;

	/*ERROR CONSULTA IDENTIFICACION PROSPECTO*/
	public static final long ERR_CONSULTA_IDEN_PROSPECTO = 97;

	/*Seleccione concepto operacion*/
	public static final long SIN_CONCEPTO_OPERACION = 98;

	/*ERROR_ACTUALIZAR_COBERTURA_OPERACION*/
	public static final long ERROR_ACTUALIZAR_COBERTURA_OPERACION = 99;

	/*Verifique falta capturar el comentario.*/
	public static final long CAPTURE_COMENTARIO = 100;

	/*ERROR INESPERADO %s*/
	public static final long ERROR_INESPERADO = 101;

	/*NO SE GENERO LA PERSONA EN EL CORE DE CLIENTES*/
	public static final long ERR_GENERAR_PERSONA = 102;

	/*Verifique, no existe el concepto de operacion.*/
	public static final long INEXISTENTE_CONCEPTO = 103;

	/*ERROR EN ACTUALIZA PERSONA EN PROSPECTO*/
	public static final long ERR_ACTUALIZA_PERSONA_PROSPECTO = 104;

	/*ERROR AL ACTUALIZAR CONSULTA*/
	public static final long ERR_ACTUALIZAR_CONSULTA = 105;

	/*ERROR VALIDAR RECHAZAR PROSPECTO*/
	public static final long ERR_VALIDAR_RECHAZAR_PROSPECTO = 106;

	/*ERROR EN RECHAZAR PROSPECTO*/
	public static final long ERR_RECHAZAR_PROSPECTO = 107;

	/*DIAS DE PAGO INCORRECTOS*/
	public static final long DIAS_PAGO_MAL = 108;

	/*VERIFIQUE FECHA, MAYOR AL DIA ACTUAL*/
	public static final long FECHA_MAYOR = 109;

	/*ERROR EN DATOS PROSPECTO NATURAL CONSULTA SIC*/
	public static final long ERR_DATOS_PROSPECTO_NATURAL_CONSULTA_SIC = 110;

	/*ERROR EN LISTADO FRECUENCIA*/
	public static final long ERR_LISTADO_FRECUENCIA = 111;

	/*ERROR AL CONSULTAR PARAMETROS DE PRODUCTO DE FRECUENCIA*/
	public static final long ERR_PARAMETROS_PROD_FREC = 112;

	/*ERROR AL OBTENER DATOS PERSONA NATURAL CONSULTA SIC*/
	public static final long ERROR_DATOS_PERSONA_NATURAL_CONSULTA_SIC = 114;

	/*ERROR EN NUEVO MAESTRO SOLICITUD*/
	public static final long ERR_NUEVO_MAESTRO_SOLICITUD = 115;

	/*NOMBRE TITULAR NO ESPECIFICADO*/
	public static final long FALTA_NOMBRE_TITULAR = 116;

	/*DATOS DEL PRODUCTO NO ESPECIFICADOS*/
	public static final long FALTA_DATOS_PRODUCTO = 117;

	/*FALTAN DATOS COMPORTAMIENTO TITULAR*/
	public static final long FALTAN_DATOS_COMPORTAMIENTO_TITULAR = 118;

	/*ESTADO SOLICITUD NO ESPECIFICADO*/
	public static final long FALTA_EDO_SOLICITUD_ID = 119;

	/*ESQUEMA DE OPERACION NO ESPECIFICADO*/
	public static final long FALTA_ESQUEMA_OPERACION_ID = 120;

	/*SUCURSAL NO ESPECIFICADA*/
	public static final long FALTA_SUCURSAL_ID = 121;

	/*TIPO SOLICITUD NO ESPECIFICADO*/
	public static final long FALTA_TIPO_SOLICITUD_ID = 122;

	/*INDICADORES DE PRESTAMOS NO ESPECIFICADOS*/
	public static final long IND_PRESTAMOS_NO_ESPECIFICADOS = 123;

	/*CAMPOS PARA TRANSACCION NO ESPECIFICADOS*/
	public static final long FALTAN_CAMPOS_TRX = 124;

	/*ERROR EN VALIDA CAMPOS TRANSACCION*/
	public static final long ERR_VALIDA_CAMPOS_TRAX = 125;

	/*PLAZO FUERA DE RANGO PERMITIDO DEL PRODUCTO*/
	public static final long PLAZO_FUERA_RANGO = 126;

	/*ERROR EN VALIDA CLAVE CONCEPTO OPERACION*/
	public static final long ERR_VALIDA_CVE_CONCEPTO_OP = 127;

	/*ERROR_BUSCAR_DATOS_COBERTURA_OPERACION*/
	public static final long ERROR_BUSCAR_DATOS_COBERTURA_OPERACION = 128;

	/*PERSONA SIN IDENTIFICACION PRINCIPAL*/
	public static final long FALTA_IDE_PRINCIPAL = 129;

	/*ERROR EN VALIDAR ALTA SOLICITUD NIVEL 1*/
	public static final long ERR_VALIDAR_ALTA_SOL_N1 = 130;

	/*ERROR_VALIDAR_DATOS_LIBERAR_COBERTURA_OPERACION*/
	public static final long ERROR_VALIDAR_DATOS_LIBERAR_COBERTURA_OPERACION = 131;

	/*ERROR_LIBERAR_COBERTRA_OPERACION*/
	public static final long ERROR_LIBERAR_COBERTRA_OPERACION = 132;

	/*PRODUCTO NO PERMITE DIAS DE PAGO*/
	public static final long PRODUCTO_SIN_DIAS_PAGO = 133;

	/*ERROR EN VALIDAR PARAMETROS PRODUCTO*/
	public static final long ERR_VALIDAR_PARAM_PRODUCTO = 134;

	/*ERROR_MODIFICAR_COBERTURA_OPERACION*/
	public static final long ERROR_MODIFICAR_COBERTURA_OPERACION = 135;

	/*ERROR AL AUTORIZAR REGISTRO PROSPECTO*/
	public static final long ERROR_AUTORIZAR_REGISTRO_PROSPECTO = 136;

	/*ERROR EN ALTA SOLICITUD NIVEL 1*/
	public static final long ERR_ALTA_SOL_N1 = 137;

	/*ERROR_OBTENER_DATOS_MAESTROPERSONA*/
	public static final long ERROR_OBTENER_DATOS_MAESTROPERSONA = 138;

	/*LA ENTIDAD ASIGNADA NO EXISTE*/
	public static final long ENTIDAD_ASIGNADA_NO_EXISTE = 139;

	/*CAPTURE DESTINO*/
	public static final long CAPTURE_DESTINO = 140;

	/*ERROR EN NUEVO DESTINO SOLICITUD*/
	public static final long ERR_NVO__DESTINO_SOL = 141;

	/*IMPORTE INCORRECTO*/
	public static final long IMPORTE_INCORRECTO = 142;

	/*SUMA IMPORTES MAYOR A MONTO SOLICITADO*/
	public static final long SUM_IMPORTES_MAYOR_MTO_SOL = 143;

	/*ERROR VALIDAR DESTINO SOLICITUD*/
	public static final long ERR_VALIDAR_DESTINO_SOL = 144;

	/*ERROR EN CONSULTA DATOS SOLICITUD*/
	public static final long ERR_CONSULTA_DATOS_SOLICITUD = 145;

	/*ERROR AL AUTORIZAR SOLICITUD NIVEL 1*/
	public static final long ERR_AUT_SOL_N1 = 146;

	/*LA SOLICITUD YA HA SIDO RECHAZADA*/
	public static final long SOLICITUD_RECHAZADA = 147;

	/*LA SOLICITUD YA HA SIDO AUTORIZADA*/
	public static final long SOLICITUD_AUTORIZADA = 148;

	/*ERROR EN VALIDAR AUTORIZAR SOLICITUD NIVEL1*/
	public static final long ERR_VALIDAR_AUT_SOL_N1 = 149;

	/*ERROR_OBTENER_EJB*/
	public static final long ERROR_OBTENER_EJB = 150;

	/*ERROR_ALTA_DATOS_SOLICITUD*/
	public static final long ERROR_ALTA_DATOS_SOLICITUD = 151;

	/*ERROR AL OBTENER LISTADO GARANTIAID*/
	public static final long ERR_OBTENER_LIST_GTIAID = 152;

	/*ERROR_ALTA_DESTINO_SOLICITUD*/
	public static final long ERROR_ALTA_DESTINO_SOLICITUD = 153;

	/*ERROR EN NUEVO SOLICITUD VINCULADO*/
	public static final long ERR_NVO_SOL_VINCULADO = 154;

	/*PORCENTAJE MENOR A CERO*/
	public static final long PORC_MENOR_CERO = 155;

	/*ERROR VALIDAR SOLICITUD VINCULADO*/
	public static final long ERR_VALIDAR_SOL_VINCULADO = 156;

	/*ERRO AL DAR DE ALTA UN TITULAR SOLICITUD*/
	public static final long ERROR_NUEVO_TITULAR_SOLICITUD = 157;

	/*EL TITULAR YA EXISTE*/
	public static final long TITULAR_YA_EXISTE = 158;

	/*ERROR EN ACTUALIZA DESTINO PPAL SOLICITUD*/
	public static final long ERR_ACTUALIZA_DESTINO_SOLICITUD = 159;

	/*ERROR EN VALIDAR RECHAZAR SOLICITUD*/
	public static final long ERR_VALIDAR_RECHAZAR_SOLICITUD = 160;

	/*ERROR EN NUEVO SOLICITUD MEDIO PAGO*/
	public static final long ERR_NVO_SOL_MEDIO_PAGO = 161;

	/*ERROR EN NUEVO HISTORICO PROMOTOR*/
	public static final long ERR_NVO_HISTORICO_PROMOTOR = 162;

	/*SELECCIONE SOLICITUD O PROSPECTO*/
	public static final long SELEC_SOLI_O_PROSPECTO = 163;

	/*ERROR EN BAJA HISTORICO PROMOTOR*/
	public static final long ERR_BAJA_HISTORICO_PROMOTOR = 164;

	/*ERROR EN BAJA DATOS_SOLICITUD*/
	public static final long ERR_BAJA_DATOS_SOLI = 165;

	/*ERROR EN BAJA DESTINO POR SOLICITUD*/
	public static final long ERR_BAJA_DESTINO_SOLI = 166;

	/*ERROR EN BAJA EXPEDIENTE POR SOLICITUD*/
	public static final long ERR_BAJA_EXPEDIENTE_SOLI = 167;

	/*ERROR EN BAJA MEDIO PAGO POR SOLICITUD*/
	public static final long ERR_BAJA_MEDIO_PAGO_SOLI = 168;

	/*ERROR EN BAJA NEGOCIO POR SOLICITUD*/
	public static final long ERR_BAJA_NEGOCIO_SOLI = 169;

	/*ERROR EN BAJA TITULAR POR SOLICITUD*/
	public static final long ERR_BAJA_TITULAR_SOLI = 170;

	/*ERROR EN BAJA PODER POR VINCULADO*/
	public static final long ERR_BAJA_PODER_VINCULADO = 171;

	/*ERROR EN CONSULTA DESTINO SOLICITUD*/
	public static final long ERR_CONSULTA_DESTINO_SOLICITUD = 172;

	/*ERROR EN CONSULTA DESTINO PRINCIPAL*/
	public static final long ERR_CONSULTA_DESTINO_PRINCIPAL = 173;

	/*SOLICITUD SIN DESTINO PRINCIPAL*/
	public static final long SIN_DESTINO_PRINCIPAL = 174;

	/*LISTADO SOLICITUD GARANTIAS VACIO*/
	public static final long LISTADO_SOLICITUD_GARANTIAS_VACIO = 175;

	/*ERROR AL CREAR COBERTURA SOLICITUD*/
	public static final long ERROR_CREAR_COBERTURA_SOLICITUD = 176;

	/*ERROR_CONSULTA_CIRCULO_CREDITO*/
	public static final long ERROR_CONSULTA_CIRCULO_CREDITO = 177;

	/*ERROR AL CONSULTAR BURO DE CREDITO*/
	public static final long ERROR_CONSULTA_BURO_CREDITO = 178;

	/*LISTADO SOLICITUD NEGOCIOS VACIO*/
	public static final long LISTADO_SOLICITUD_NEGOCIOS_VACIO = 179;

	/*ERROR BUSCAR DATOS SOLICITUD NEGOCIO*/
	public static final long ERROR_BUSCAR_DATOS_SOLICITUD_NEGOCIO = 180;

	/*VERIFICAR, IDENTIFICACION DUPLICADA. */
	public static final long IDENTIFICACION_DUPLICADA = 181;

	/*SIN RESULTADOS*/
	public static final long SIN_RESULTADOS = 182;

	/*verifique, debe capturar un valor de busqueda de por lo menos 2 caracteres*/
	public static final long CAPTURE_ALMENOS_2 = 184;

	/*BUSQUEDA DE NEGOCIO SIN RESULTADOS*/
	public static final long BUSQUEDA_NEGOCIO_SIN_RESULTADOS = 185;

	/*BUSQUEDA DE BITACORA SIC SIN RESULTADOS*/
	public static final long BUSQUEDA_BITACORA_SIN_RESULTADOS = 186;

	/*BURO RETORNO ERROR AL REALIZAR LA CONSULTA, CONSULTE CON EL ADMINISTRADOR*/
	public static final long ERROR_BURO_RETORNA_ERROR_CONSULTA = 187;

	/*ERROR DE TIPO DE IDENTIDAD*/
	public static final long ERROR_BURO_TIPO_IDENTIDAD = 188;

	/*ERROR AL CONSULTAR CUENTAS EN LA CONSULTA ACTUAL DE BURO*/
	public static final long ERROR_CONSULTA_CUENTA_BURO = 189;

	/*ERROR AL CONSULTAR EL TIPO DE CALIFICACION DE HISTORIAL CREDITICIO*/
	public static final long ERROR_TIPO_HISTORIAL_CREDITICIO = 190;

	/*NO SE ENCONTRO TIPO DE CALIFICACION*/
	public static final long ERROR_CONSULTA_TIPO_HISTORIAL_CREDITICIO = 191;

	/*LA PERSONA YA HA SIDO RECHAZADA*/
	public static final long PERSONA_RECHAZADA = 192;

	/*ERROR AL VALIDAR RECHAZAR PERSONA*/
	public static final long ERROR_VALIDAR_RECHAZAR_PERSONA = 193;

	/*ERROR AL RECHAZAR PERSONA*/
	public static final long ERROR_RECHAZAR_PERSONA = 194;

	/*ERROR AL BUSCAR DATOS SOLICITUD CENTROS TRABAJO*/
	public static final long ERROR_BUSCAR_DATOS_SOLICITUD_CENTROS_TRABAJO = 195;

	/*ERROR AL OBTENER DATOS NEGOCIO*/
	public static final long ERROR_OBTENER_DATOS_NEGOCIO = 196;

	/*ERROR AL BUSCAR CENTRO TRABAJO*/
	public static final long ERROR_BUSCAR_CENTRO_TRABAJO = 198;

	/*ERR_VALIDAR_ALTA_SOL*/
	public static final long ERR_VALIDAR_ALTA_SOL = 199;

	/*ERR_VALIDAR_AUT_SOL*/
	public static final long ERR_VALIDAR_AUT_SOL = 200;

	/*ERR_AUT_SOL*/
	public static final long ERR_AUT_SOL = 201;

	/*Regla %s*/
	public static final long REGLA_NEGOCIO = 202;
	
	/* ERROR_REGISTRO_DUPLICADO */
	public static final long ERROR_REGISTRO_DUPLICADO = 203;
	
	/*CONFIGURE DESTINOS EN EL PRODUCTO*/
	public static final long ERR_CONF_DESTINOS_PRODUCTO = 204;

	/*ERROR EN LISTADO DESTINOS PRODUCTO*/
	public static final long ERR_LIST_DESTINOS_PRODUCTO = 205;

	/*EL DOCUMENTO ES OBLIGATORIO PARA LA INTEGRACION DEL EXPEDIENTE*/
	public static final long ERROR_DOCUMENTO_OBLIGATORIO = 206;
	
	/*LA PERSONA ES TITULAR DE LA SOLICITUD, SELECCIONE OTRA*/
	public static final long ERROR_PERSONA_TITULAR_SOLICITUD = 207;
	
	/*PRESTAMO SELECCIONADO NO ESTA LIQUIDADO, SELECCIONE OTRO*/
	public static final long SELECCIONE_PRESTAMO_LIQ = 208;
	
	/*ERROR AL ACTUALIZAR CONCEPTOS SIC EN DATOS PERSONA FALTA EL ID DE PERSONA*/
	public static final long ERR_ACT_CONCEPTOS_SIC_DATOS_PERSONA_FALTA_PERSONA_ID = 209;
	
	/*ERROR AL CONSULTAR INFORMACION DE BURO - FALTA ID BITACORA SIC*/
	public static final long ERR_CONSULTAR_BITACORA_SIC_FALTA_ID = 210;
	
	/*ERROR AL CONSULTAR INFORMACION DE BURO*/
	public static final long ERR_CONSULTA_BITACORA_SIC = 211;
	
	/*ERROR EN EN EL METODO ACTUALIZA SOLICITUD BITACORA SIC*/
	public static final long ERR_ACT_SOLICITUD_BITACORASIC = 212;
		
	/*ERROR REVISAR ESTADO SOLICITUD*/
	public static final long ERR_REVISAR_EDO_SOLICITUD = 213;
		
	/*ERROR EN VALIDA ESTADO SOLICITUD CLAVE VACIA*/
	public static final long ERR_VALIDA_EDO_SOL_CVE_VACIA = 214;

	/*ERROR EN VALIDA ESTADO SOLICITUD CLAVE DE ESTADO NO EXISTE*/
	public static final long ERR_VALIDA_EDO_SOL_CVE_NO_EX = 215;

	/*ERROR EN VALIDA CLAVE DE ESTADO CLAVE DE GRUPO DE ESTADOS VACIA*/
	public static final long ERR_VALIDA_CVE_ESTADO_CVE_GPO_EDOS_VACIA = 216;

	/*ERROR EN VALIDA CLAVE DE ESTADO CLAVE DE GRUPO DE ESTADOS NO EXISTE*/
	public static final long ERR_VALIDA_CVE_ESTADO_CVE_GPO_EDOS_NO_EX = 217;

	/*ERROR EN VALIDA CLAVE GRUPO DE ESTADOS*/
	public static final long ERR_VALIDA_CVE_GPO_EDO = 218;

	/*ERROR AL CALCULAR CAPACIDAD DE PAGO ACTUALICE PARAMETROS DEL PRODUCTO*/
	public static final long ERR_CAL_CAP_PAGO_ACT_PARAMETROS_PRODUCTO = 219;

	/*ERROR AL OBTENER CUOTA ESTIMADA*/
	public static final long ERROR_OBTENER_CUOTA_ESTIMADA = 220;

	/*ERROR AL OBTENER MONTO FINANCIADO*/
	public static final long ERROR_OBTENER_MONTO_FINANCIADO = 221;
	
	/*ERROR INESPERADO AL GUARDAR EXPEDIENTE SOLICITUD*/
	public static final long ERR_INESPERADO_GUARDAR_EXP_SOLICITUD = 222;
	
	/*ERROR AL CAPTURAR DETALLE DOCUMENTO OBLIGATORIO*/
	public static final long CAP_DETALLE_DOCTO_OBLIGATORIO = 223;
	
	/*DIGITALIZA IMAGEN PARA DOCUMENTO OBLIGATORIO*/
	public static final long DIG_IMAGEN_DOCTO_OBLIGATORIO = 224;
	
	/*ERROR INESOPERADO AL VALIDAR GUARDAR EXPEDIENTE SOLICITUD*/
	public static final long EI_VAL_GUARDAR_EXP_SOLICITUD = 225;
	
	/*CONFIGURE FRECUENCIA DE PAGO EN LOS CENTROS DE TRABAJO*/
	public static final long CONF_FREC_PAGO_CENTROS_TRABAJO_PERSONA = 226;
	
	/*BUSQUEDA DE PERSONAS SIN RESULTADOS*/
	public static final long BUSQUEDA_PERSONAS_SIN_RESULTADOS = 227;

	/*BUSQUEDA DEL CONVENIO MEDICO SIN RESULTADOS*/
	public static final long BUSQUEDA_CONVENIO_MEDICO_SIN_RESULTADOS = 228;

	/*LISTA DE COACREDITADOS NO PERMITE MAS DE 1 COACREDITADOS*/
	public static final long SOLO_UN_COACREDITADO = 229;

	/*ERROR INESPERADO AL VALIDAR CANAL DE VENTA DEL PROSPECTO*/
	public static final long EI_VALIDAR_CANAL_VENTA_PROSPECTO = 230;

	/*ACTUALICE INFORMACION DEL CLIENTE*/
	public static final long ACTUALICE_INFORMACION_CLIENTE = 231;

	/*ACTUALICE INFORMACION DEL NEGOCIO EN EL MODULO DE CLIENTES*/
	public static final long ACTUALICE_INFORMACION_NEGOCIO = 232;

	/*CAPTURE FECHA*/
	public static final long CAPTURE_FECHA = 233;

	/*FALTA INFORMACION PARA GENERAR CONVENIO*/
	public static final long FALTA_INFO_GEN_CONVENIO = 234;
	
	/*SIN DUEÑO*/
	public static final long SIN_DUENO = 235;

	/*SIN FECHA DE INGRESO*/
	public static final long SIN_FECHA_INGRESO = 236;

	/*SIN TIPO DE CONTRATO*/
	public static final long SIN_TIPO_CONTRATO = 237;

	/*SIN TIPO DE INGRESO*/
	public static final long SIN_TIPO_INGRESO = 238;

	/*SIN FRECUENCIA DE PAGO*/
	public static final long SIN_FRECUENCIA_PAGO = 239;

	/*SIN MONEDA*/
	public static final long SIN_MONEDA = 240;

	/*SIN INGRESO*/
	public static final long SIN_INGRESO = 241;

	/*SIN CARGO LABORAL*/
	public static final long SIN_CARGO_LABORAL = 242;

	/*SIN CENTRO DE TRABAJO*/
	public static final long SIN_CENTRO_TRABAJO = 243;

	/*CAPTURE AL TITULAR COMO BENEFICIARIO*/
	public static final long CAPTURE_TITULAR_COMO_BENEFICIARIO = 244;

	/*CAPTURE EL INGRESO MENSUAL*/
	public static final long ERR_ING_MENSUAL = 245;

	/*CAPTURE EL CODIGO DE VINCULACION CON EL TITUALR*/
	public static final long ERR_COD_VINC = 246;

	/*ERROR EN EL METODO ALTA LISTADO PROVEEDORES DE RECURSOS SOLICITUD*/
	public static final long ERR_ALTA_LIST_PROV_REC = 247;

	/*CAPTURE MONTO MAXIMO COMO ANTICIPO*/
	public static final long ERR_MTO_MAX_ANT = 248;

	/*CAPTURE MONTO MAXIMO A PAGAR EN UN MES*/
	public static final long ERR_MTO_MAX_PAGAR = 249;

	/*CAPTURE A UN PROVEEDOR DE RECURSOS*/
	public static final long CAPTURE_PROVEEDOR_RECURSOS = 250;

	/*LA LISTA DE BENEFICIARIOS DEBE INCLUIR POR LO MENOS AL TITULAR*/
	public static final long ERR_LIST_BENEF = 251;

	/*ERROR EN EL METODO VALIDA LISTADO BENEFICIARIOS SOLICITUD*/
	public static final long ERR_VAL_ALTA_LIST_BENEF = 252;

	/*ERROR EN  ALTA LISTADO BENEFICIARIOS SOLICITUD*/
	public static final long ERROR_ALTA_LISTADO_BENEFICIARIOS_SOLICITUD = 253;

	/*ERROR EN EL METODO VALIDAR ALTA LISTADO PROVEEDORES DE RECURSOS*/
	public static final long ERR_VAL_ALTA_LIST_PROV_REC = 254;

	/*ERROR EN LA BUSQUEDA DE BITACORA SIC*/
	public static final long ERR_BUSQUEDA_BITACORASIC = 255;

	/*ERROR AL CONSULTAR EL METODO DE BITACORA SIC*/
	public static final long ERR_METODO_CONSULTA_BITACORASIC = 256;

	/*ERROR BITACORASICID NO EXISTE*/
	public static final long ERR_BITACORASICID_NO_EXISTE = 257;
	
	/*ACTUALICE CONDICION DE LA PROPIEDAD EN EL MODULO DE CLIENTES*/
	public static final long ACT_CONDICION_PROPIEDAD_DE_CLIENTES = 258;

	/*ERROR INESPERADO EN ALTA NEGOCIO SOLICITUD*/
	public static final long EI_ALTA_NEGOCIO_SOLICITUD = 259;

	/*FALTA PROPIETARIO DEL NEGOCIO*/
	public static final long FALTA_PROPIETARIO_NEGOCIO = 260;
	
	/*ERROR EN REVISAR CALIFICACION*/
	public static final long ERR_REVISAR_CALIFICACION = 261;

	/*ERROR INESPERADO EN REVISA CALIFICACION*/
	public static final long EI_REVISA_CALIFICACION = 262;
	
	/*ESPECIFIQUE CONSULTA PARA REVISAR CALIFICACION*/
	public static final long ERR_ESPECIFIQUE_CONSULTA_REVISA_CALIFICACION = 263;

	/*VERIFIQUE DIAS DE PAGO EL DIA UNO DEBE SER MENOR AL DIA DOS*/
	public static final long DIA_PAGO1_MAYOR_2 = 264;

	/*SELECCIONE UN HOMONIMO*/
	public static final long SELECCIONE_HOMONIMO = 265;

	/*SELECCIONE PERSONA TITULAR DE LA CUENTA A DOMICILIAR*/
	public static final long DOM_PERSONA_TITULAR_CUENTA_VACIO = 266;

	/*CAPTURE LA INFORMACION PARA DOMICILIAR EL PAGO*/
	public static final long DOM_FALTA_INFO = 267;

	/*VERIFIQUE LONGITUD DE CLABE INTERBANCARIA*/
	public static final long DOM_LONGITUD_CLABE = 268;

	/*VERIFIQUE LONGITUD DE LA CUENTA*/
	public static final long DOM_LONGITUD_CUENTA = 269;

	/*VERIFIQUE LONGITUD DEL NUMERO DE TARJETA*/
	public static final long DOM_LONGITUD_NUM_TARJETA = 270;

	/*ERROR EN LISTADO PERSONAS HOMONIMO*/
	public static final long ERR_LIST_PERSONAS_HOMONIMO_ = 271;
	
	/*FOLIO CONSULTA VACIO PARA ACTUALIZAR COMPROBANTE DE PAGO*/
	public static final long FOLIO_CONSULTA_VACIO_COMPROBANTE = 272;

	/*ERROR INESPERADO AL GUARDAR COMPROBANTE DE PAGO*/
	public static final long EI_GUARDAR_BITACORA_SIC_DETALLE = 273;

	/*FOLIO CONSULTA PARA CONSULTAR DATOS*/
	public static final long FOLIO_CONSULTA_VACIO_DATOS = 274;

	/*NO EXISTE REGISTRO DEL FOLIO CONSULTA*/
	public static final long NO_EX_FOLIO_CONSULTA = 275;

	/*ERROR INESPERADO EN CONSULTA SIC POR FOLIO*/
	public static final long EI_CONSULTA_BITACORA_SIC_FOLIO = 276;

	/*ERROR AL ACTUALIZAR ESTADO DE CONSULTA CLAVE VACIA*/
	public static final long CVE_EDO_BITACORA_VACIO = 277;

	/*ERROR INESPERADO AL ACTUALIZAR ESTADO CONSULTA*/
	public static final long EI_ACTUALIZA_ESTADO_CONSULTA = 278;

	/*CONSULTA NO AUTORIZADA*/
	public static final long CONSULTA_NO_AUTORIZADA = 279;
	
	/*ERROR INESPERADO AL OBTENER LISTADO DE PROSPECTOS*/
	public static final long EI_PROSPECTO_BUSCAR = 280;

	/*ERROR INESPERADO EN EL SERVICIO PARA LISTADO DE PROSPECTOS*/
	public static final long EI_SVC_PROSPECTO_BUSCAR = 281;

	/*ESPECIFIQUE TIPO DE PERSONA*/
	public static final long TIPO_PERSONA_VACIO = 282;
	
	/*ERROR AL REGISTAR PROSPECTO JURIDICO TIPO DE PERSONA NO VALIDO*/
	public static final long NPJ_TIPO_PERSONA_NO_JURIDICA = 283;
	
	/*ERROR AL REGISTAR PROSPECTO JURIDICO IDENTIFICACION VACIA*/
	public static final long NPJ_IDENTIFICACION_VACIA = 284;

	/*ERROR AL REGISTAR PROSPECTO JURIDICO TIPO IDENTIFICACION VACIA*/
	public static final long NPJ_TIPO_IDENTIFICACION_VACIA = 285;

	/*ERROR EN NUEVO PROSPECTO JURIDICO*/
	public static final long ERR_NUEVO_PROSPECTO_JURIDICO = 286;

	/*ERROR INESPERADO AL OBTENER LISTA PROSPECTO BUSCADOS*/
	public static final long EI_LISTA_PROSPECTO_BUSCAR = 287;

	/*ESPECIFIQUE LA CLAVE DE ESTADO*/
	public static final long LPB_CLAVE_CVE_ESTADO_PROSPECTO_VACIO = 288;
	
	/*REALICE CONSULTAS FALTANTES*/
	public static final long CONSULTAS_FALTANTES = 289;

	/*FALTA RESULTADO DE BURO INTERNO*/
	public static final long FALTA_RESULTADO_BI = 290;

	/*ACTUALICE LA PERSONA YA QUE NO COINCIDE CON LA SELECCIONADA EN BURO INTERNO*/
	public static final long ACT_PERSONAID_COACREDITADA = 291;

	/*ACTUALICE REPRESENTANTE YA QUE NO COINCIDE CON LA SELECCIONADA EN BURO INTERNO*/
	public static final long ACT_PERSONAID_REPRESENTANTE = 292;

	/*EL CLIENTE CUENTA CON CREDITOS DE ESTE PRODUCTE INICIE REDISPOSICION DE PRESTAMO*/
	public static final long CLIENTE_REDISPOSICION = 293;

	/*REGISTRE PERSONA JURIDICA EN MODULO DE CLIENTES*/
	public static final long REGI_PJ_MODULO_CLIENTES = 294;

	/*EL PRESTAMO SELECCIONADO NO ESTA ACTIVO, SELECCIONE OTRO*/
	public static final long SELECCIONE_PRESTAMO_ACT = 295;
	
	/*ERROR INESPERADO EN REDISPOSICION AUTOMATICA*/
	public static final long EI_REDISPOSICION_AUTOMATICA = 296;

	/*ERROR INESPERADO EN REDISPOSICION ANTICIPADA*/
	public static final long EI_REDISPOSICION_ANTICIPADA = 297;
	
	/*EL PRESTAMO ANTERIOR NO TIENE SOLICITUD PARA OBTENER INFORMACION*/
	public static final long FALTA_SOL_ANTERIOR = 298;
	
	/*ESPECIFIQUE SOLICITUD PARA OBTENER EL LISTADO DE SUS NEGOCIOS*/
	public static final long LSN_SOLICITUD_ID_VACIO = 299;
	
	/*ERROR INESPERADO EN LISTA SOLICITUD NEGOCIO*/
	public static final long EI_LISTA_SOLICITUD_NEGOCIO = 300;

	/*CAPTURE MODO DE CONTACTO SIGUIENTE*/
	public static final long CAPTURE_MODO_CONTACTO_SIGUIENTE = 301;

	/*ESPECIFIQUE PROCESO INICIADO PARA ACTUALIZARLO AL PROSPECTO*/
	public static final long APPI_ESPECIFIQUE_PROCESO_INICIADO = 302;

	/*ERROR INESPERADO AL ACTUALIZAR PROCESO INICIADO AL PROSPECTO*/
	public static final long EI_ACTUALIZA_PROSPECTO_PROCESO_INICIADO = 303;

	/*PROSPECTO CON PROCESO ACTIVO*/
	public static final long PROSPECTO_ACTIVO = 304;
	
	/*FOLIO CONSULTA YA EXISTE*/
	public static final long EXISTE_FOLIO_CONSULTA = 305;
	
	/*ERROR AL VALIDAR ALTA BITACORA SIC*/
	public static final long EI_VALIDA_ALTA_BITACORA_SIC = 306;
	
	/*PRESTAMO CON PROCESO DE REDISPOSICION ACTIVA*/
	public static final long PRESTAMO_REDISPOSICION_ACTIVA  = 307;
	
	/*ERROR AL GUARDAR DATOS PERSONA BITACORA SIC*/
	public static final long _EI_GUARDAR_PERSONA_BITACORA_SIC = 308;

	/*ERROR AL GUARDAR CALIFICACION BITACORA SIC*/
	public static final long EI_GUARDAR_CALIFICACION_BITACORA_SIC = 309;
	
	/*CAPTURE SCORING*/
	public static final long CAPTURE_SCORING = 310;

	/*ERROR DE CONFIGURACION EN LOS MOTIVOS DE RECHAZO NO EXISTE RECHAZADO POR SCORING*/
	public static final long ERROR_CONF_RECHAZO_SCORING = 311;

	/*VERIFIQUE PORCENTAJE DE BENEFICIARIOS*/
	public static final long ERROR_PORC_BENEFICIARIOS = 312;

	/*DOMICILIO VACIO, VERIFIQUE INFORMACION EN EL MODULO DE CLIENTES*/
	public static final long DOM_VACIO_MOD_CLIENTES = 313;

	/*EMPRESA REQUERIDA PARA DAR DE ALTA BENEFICIARIOS DEL SEGURO*/
	public static final long ABS_EMPRESA_REQUERIDA = 314;

	/*LA LISTA DE BENEFICIARIOS DEL SEGURO ESTA VACIA*/
	public static final long ABS_LISTA_BENEFICIARIOS_VACIA = 315;

	/*ERROR INESPERADO EN ALTA BENEFICIARIOS SEGURO*/
	public static final long EI_ALTA_BENEFICIARIOS_SEGURO = 316;

	/*CAMPO %s REQUERIDOS PARA ALTA DE BENEFICIARIOS SEGURO*/
	public static final long ABS_CAMPO_REQUERIDO = 317;

	/*ERROR INESPERADO EN EL METODO VALIDA BENEFICIARIOS SEGURO*/
	public static final long EI_VAL_ALTA_BENEF_SEGURO = 318;
	
	/*EL TITULAR DE LA SOLICITUD NO PUEDE SER BENEFICIARIO DEL SEGURO*/
	public static final long ABS_TITULAR_BENEFICIARIO = 319;

	/*CAMPOS %REQUERIDOS PARA CONSULTAR BENEFICIARIOS*/
	public static final long CBS_CAMPO_REQUERIDOS = 320;

	/*ERROR INESPERADO EN CONSULTA BENEFICIARIOS SEGURO*/
	public static final long EI_CONSULTA_BENEFICIARIOS_SEGURO = 321;
	
	/*EDAD DEL BENEFICIARIO SEGURO INCORRECTA*/
	public static final long EDAD_BENEFICIARIO_SEGURO_INCORRECTA = 322;
	
	/*ERROR AL OBTENER CONCEPTO TARIFA ID*/
	public static final long ERR_OBT_CONCP_TARF_ID = 323;

	/*ERROR AL OBTENER DESCRIPCION CONCEPTO TARIFA*/
	public static final long ERR_OBT_DESC_CONCP_TAR = 324;

	/*ERROR AL OBTENER TASA EFECTIVA ANUAL*/
	public static final long ERR_OBT_TAS_EFCT_ANUAL = 325;

	/*ERROR AL OBTENER TASA REAL*/
	public static final long ERR_OBT_TASA_REAL = 326;

	/*ERROR AL ACTUALIZAR ESTADO SOLICITUD*/
	public static final long ERR_ACT_ESTADO_SOLICITUD = 327;
	
	/*ERROR EN LA CONFIGURACION DEL PRODUCTO MONTO MINIMO VACIO O CERO*/
	public static final long ERR_CONF_PROD_MTO_MINIMO = 328;

	/*ERROR EN LA CONFIGURACION DEL PRODUCTO MONTO MAXIMO VACIO O CERO*/
	public static final long ERR_CONF_PROD_MTO_MAXIMO = 329;

	/*ERROR EN LA CONFIGURACION DEL PRODUCTO TIPO VACIO*/
	public static final long ERR_CONF_PROD_CVE_TIPO_PRODUCTO = 330;

	/*ERROR EN LA CONFIGURACION DEL PRODUCTO SUB TIPO VACIO*/
	public static final long ERR_CONF_PROD_CVE_SUBTIPO_PRODUCTO = 331;

	/*FORMA DE COMPROBACION DE INGRESOS INCORRECTA SELECCIONE ACTIVIDAD PRODUCTIVA*/
	public static final long ERR_COMPROBACION_INGRESOS = 332;
	
	/*TIPO PROPIEDAD DEL DOMICILIO DE LA VIVIENDA NO PERMITIDO*/
	public static final long ERR_TIPO_PROPIEDAD = 333;
	
	public static final long ERROR_CIRCULO_TIPO_IDENTIDAD = 334;
	
	public static final long ERROR_CIRCULO_RETORNA_ERROR_CONSULTA = 335;
	
	/*ERROR DEL SISTEMA DE CARTERA*/
	public static final long ERROR_SISTACC=336;
	
	/*ERROR INESPERADO EN REDISPOSICION NORMAL*/
	public static final long EI_REDISPOSICION_NORMAL=337;
	
	/*ERROR EL SALDO DEL CREDITO NO SE PUEDE REDISPONER CON EL PRODUCTO SELECCIONADO*/
	public static final long ERROR_MTO_REDISPONER_NEGATIVO = 338;
	
	/*ERROR INESPERADO EN VALIDA GARANTIAS_REDISPOSICION*/
	public static final long EI_VALIDA_GARANTIAS_REDISPOSICION = 339;
	
	/*ERROR INESPERADO EN REDISPOSICION MEJORA TU CASA*/
	public static final long EI_MTC = 340;

	/*ERROR INESPERADO EN LISTA SOLICITUD CENTROS DE TRABAJO*/
	public static final long EI_LISTA_SOLICITUD_CENTROS_TRABAJO = 341;

	/*ERROR INESPERADO AL DAR DE ALTA SOLICITUD DE REPROGRAMACION*/
	public static final long EI_ALTA_SOL_REPROGRAMACION = 342;

	/*REALICE SIMULACION*/
	public static final long REALICE_SIMULACION = 343;

	/*ERROR INESPERADO EN VALIDAR SOLICITUD DE REPROGRAMACION*/
	public static final long EIWS_VALIDAR_SOL_REPROGRAMACION = 344;

	/*ERROR EN EL WS VALIDAR SOLICITUD REALICE SIMULACION*/
	public static final long EWS_VSREP_REALICE_SIMULACION = 345;

	/*ERROR EN EL WS VALIDAR SOLICITUD SELECCIONE SOLO UNA OPCION A CAPTURAR*/
	public static final long EWS_VSREP__SELECCIONE_OPT_CAPTURAR = 346;

	/*ERROR INESPERADO WS AL AUTORIZAR SOLICITUD DE REPROGRAMACION*/
	public static final long EIWS_AUTORIZAR_SOL_REPROGRAMACION = 347;
	
	
	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {

		
		descError.put(CAPTURE_CAMPOS_REQUERIDOS,"CAMPO %s REQUERIDO");
		descError.put(USUARIO_SIN_EMPRESA,"Usuario sin empresa asignada");
		descError.put(NO_EXISTEN_SOLICITUDES_PENDIENTES,"No existen solicitudes pendientes");
		descError.put(CLAVE_ESTADO_VACIO,"CLAVE DE ESTADO VACIO");
		descError.put(CLAVE_ESTADO_NO_EXISTE,"CLAVE DE ESTADO NO EXISTE");
		descError.put(SELECCIONE_CLIENTE,"Seleccione cliente");
		descError.put(NO_EXISTE_SOLICITUD,"NO EXISTE SOLICITUD");
		descError.put(SELECCIONE_SOLICITUD,"Seleccione solicitud");
		descError.put(CLAVE_CATALOGO_NO_EX,"CLAVE DE CATALOGO NO EXISTE");
		descError.put(CLAVE_CATALOGO_VACIA,"CLAVE DE CATALOGO VACIA");
		descError.put(ERR_CONSULTAR_SOLICITUDES,"ERROR AL CONSULTAR SOLICITUDES");
		descError.put(NO_EX_TITULARES,"NO EXISTEN TITULARES");
		descError.put(ERR_CONSULTAR_TITULARES,"ERROR AL CONSULTAR TITULARES");
		descError.put(MTO_TITULARES_NO_ESPECIFICADO,"MONTO DE TITULARES NO ESPECIFICADO");
		descError.put(ERR_SUMAMONTO_TITULARES,"ERROR AL CONSULTAR MONTO DE TITULARES");
		descError.put(PROSPECTO_DUPLICADO,"El prospecto ya esta registrado o genero duplicidad. Desea continuar?");
		descError.put(fecha_nacimiento_invalida,"Verifique el campo Fecha nacimiento selecciono una fecha mayor al dia actual");
		descError.put(SELECCIONE_PRESTAMO,"SELECCIONE PRESTAMO");
		descError.put(ERR_ACT_SOLICITUD_ACREDITADA,"ERROR EN ACTUALIZA SOLICITUD ACREDITADA");
		descError.put(ERR_VALIDA_EDO_SOLICITUD_CVE,"ERROR EN VALIDA ESTADO SOLICITUD CLAVE");
		descError.put(MONTO_FUERA_RANGO,"Monto solicitado fuera de rango permitido del producto.");
		descError.put(NO_EX_GRUPO_INTEGRANTES,"NO EXISTEN INTEGRANTES DE GRUPO");
		descError.put(ERR_CONSULTA_GRUPO_INTEGRANTES,"ERROR AL CONSULTAR INTEGRANTES DE GRUPO");
		descError.put(FECHA_DESDE_MAYOR,"Verifique, fecha desde mayor al dia actual");
		descError.put(SIN_CRITERIO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de busqueda");
		descError.put(ERR_SUMA_MTO_INTEGRANTES,"ERROR AL OBTENER SUMA MONTO INTEGRANTES");
		descError.put(ALMENOS_DOS_CARACTERES,"Capture al menos 2 caracteres para realizar la busqueda");
		descError.put(TIPO_SUBTIPO_GTIA,"Verifique el tipo y subtipo de (las) garantia(s) coberturando la solicitud");
		descError.put(PORCENTAJE_COBERTURA_INCORRECTO,"Porcentaje total de cobertura de garantias incorrecto");
		descError.put(SIN_GTIA_SABANA,"El producto requiere garantia sabana");
		descError.put(SIN_GTIA_ESPECIFICA,"El producto requiere garantia especifica");
		descError.put(ERRONEA_PROPORCION_GARANTIA_SOLICITUD,"Verifique porcentaje de garantias en la solicitud respecto a definidos en el producto");
		descError.put(ERROR_CON_GTIAS,"El producto no requiere cobertura de garantias");
		descError.put(SIN_PROSPECTO,"Seleccione prospecto");
		descError.put(ERR_NUEVO_DOMICILIO_PROSPECTO,"ERROR EN NUEVO DOMICILIO PROSPECTO");
		descError.put(SIN_TIPO_CONSULTA,"seleccione el tipo de consulta de historial crediticio");
		descError.put(CAP_INF_ZONIFICACION,"CAPTURE INFORMACION ZONIFICACION");
		descError.put(CAP_INF_RESIDENCIA,"CAPTURE INFORMACION RESIDENCIA");
		descError.put(ERR_VALIDAR_DOMICILIO_PROSPECTO,"ERROR EN VALIDAR DOMICILIO PROSPECTO");
		descError.put(CAP_INF_TIPO_ZONIFICACION," CAPTURE INFORMACION TIPO ZONIFICACION");
		descError.put(CONSULTA_CREDITICIA_INACTIVA,"la consulta de historial crediticio no esta vigente");
		descError.put(PROSPECTO_RECHAZADO,"El prospecto ya ha sido rechazado.");
		descError.put(prospecto_autorizado,"El prospecto ya ha sido autorizado");
		descError.put(SIN_ENTIDAD,"Seleccione la entidad a la que revisara el estatus");
		descError.put(SIN_TIPO_ENTIDAD,"Selecciones el tipo entidad a reviar su estatus");
		descError.put(SIN_PRODUCTO,"Seleccione producto");
		descError.put(VERIFIQUE_RFC,"VERIFIQUE FORMATO DE RFC");
		descError.put(SIN_PERSONA,"SELECCIONE PERSONA");
		descError.put(NO_EX_PROSPECTO,"NO EXISTE PROSPECTO");
		descError.put(ERR_ACTUALIZA_DOMICILIO_PROSPECTO,"ERROR EN ACTUALIZA DOMICILIO PROSPECTO");
		descError.put(PRODUCTO_INACTIVO,"El producto de credito ha sido dado de baja");
		descError.put(ERR_NUEVO_TEL_PROSPECTO,"ERROR EN NUEVO TELEFONO PROSPECTO");
		descError.put(ERR_VALIDAR_TELEFONO_PROSPECTO,"ERROR EN VALIDAR TELEFONO PROSPECTO");
		descError.put(ERR_ACTUALIZA_TELEFONO_PROSPECTO,"ERROR EN ACTUALIZA TELEFONO PROSPECTO");
		descError.put(VERIFIQUE_GENERO,"VERIFIQUE GENERO");
		descError.put(ERR_VALIDAR_PROSPECTO,"ERROR VALIDAR PROSPECTO");
		descError.put(ERR_VALIDAR_HOMONIMIA_DUPLICIDAD,"ERROR EN VALIDAR HOMONIMIA DUPLICIDAD");
		descError.put(ERR_NUEVO_PROSPECTO,"ERROR EN NUEVO PROSPECTO");
		descError.put(SIN_CONSULTA_CREDITICIA,"El prospecto no tiene consultas de historial crediticio registradas.");
		descError.put(SIN_DOCUMENTOS,"No hay documentos asociados");
		descError.put(FECHA_MENOR,"Verifique, fecha menor al dia actual.");
		descError.put(ERR_VALIDAR_IDE_PROSPECTO,"ERROR VALIDAR IDENTIFICACION PROSPECTO");
		descError.put(ERR_NVO_PROSPECTO_IDE,"ERROR EN NUEVO PROSPECTO IDENTIFICACION");
		descError.put(ERR_REG_PROSPECTO_NATURAL_COMPLETO,"ERROR EN REGISTRAR PROSPECTO NATURAL COMPLETO");
		descError.put(ERR_ACTUALIZA_IDE_PROSPECTO,"ERROR EN ACTUALIZA INDENTIFICACION PROSPECTO");
		descError.put(ERR_CONCATENAR_DOMICILIO,"ERROR AL CONCATENAR DOMICILIO");
		descError.put(DOMICILIO_VACIO,"DOMICILIO VACIO, VERIFIQUE CATALOGOS");
		descError.put(ERR_CONCATENAR_TELEFONO,"ERROR EN CONCATENAR TELEFONO");
		descError.put(ASESOR_INACTIVO,"ASESOR INACTIVO");
		descError.put(SIN_NEGOCIO,"Seleccione negocio");
		descError.put(NEGOCIO_INACTIVO,"Verifique, negocio dado de baja");
		descError.put(SOLICITUD_ACREDITADA,"Verifique, solicitud acreditada");
		descError.put(FECHA_DESDE_MENOR,"Verifique,  la fecha desde menor a la actual.");
		descError.put(CONSULTA_SIN_CALIFICACION,"ULTIMA CONSULTA SIN CALIFICACION");
		descError.put(SIN_GARANTIA,"Seleccione garantia");
		descError.put(FECHAS_INVERTIDAS,"Verifique fecha desde y fecha hasta estan invertidas.");
		descError.put(LISTADO_CONCEPTOS_COBERTURA_VACIO,"Verifique, el listado de conceptos del valor de la cobertura esta vacio.");
		descError.put(CALIFACION_NO_FAVORABLE,"CALIFACION NO FAVORABLE");
		descError.put(ERR_VALIDA_HIST_CREDITICIO,"ERROR EN VALIDA HISTORIAL CREDITICIO");
		descError.put(SIN_MONEDA_SOLICITUD,"Seleccione moneda de la solicitud.");
		descError.put(NO_EX_DOMICILIO,"NO EXISTE DOMICILIO");
		descError.put(ERR_CONSULTA_DOMICILIO_PROSPECTO,"ERROR EN CONSULTA DOMICILIO PROSPECTO");
		descError.put(FALTA_INF_SECTORIZACION,"FALTA INFORMACION PARA SECTORIZACION");
		descError.put(COBERTURA_SOLICITUD_INACTIVA,"Cobertutura ya ha sido dado liberada");
		descError.put(ERR_VALIDAR_AUT_PROSPECTO,"ERROR EN VALIDAR AUTORIZAR PROSPECTO");
		descError.put(ERR_AUTORIZAR_PROSPECTO,"ERROR AL AUTORIZAR PROSPECTO");
		descError.put(ERR_COBERTURA_GARANTIAS,"Verifique, error actualizando la cobertura en garantias.");
		descError.put(NO_EX_TELEFONO,"NO EXISTE TELEFONO");
		descError.put(ERR_CONSULTA_TEL_PROSPECTO,"ERROR CONSULTA TELEFONO PROSPECTO");
		descError.put(ERR_CONSULTA_INF_PROSPECTO,"ERROR EN CONSULTA INFORMACION PROSPECTO");
		descError.put(SIN_COBERTURA_SOLICITUD,"primero busque y seleccione una cobertura de solicitud");
		descError.put(COBERTURA_LIBERADA,"Cobertura ya ha sido liberado");
		descError.put(NO_EX_PROSPECTO_NAT,"NO EXISTE PROSPECTO NATURAL");
		descError.put(ERR_CONSULTA_PROSPECTO_NAT,"ERROR EN CONSULTA PROSPECTO NATURAL");
		descError.put(NO_EX_IDENTIFICACION,"NO EXISTE IDENTIFICACION");
		descError.put(ERR_CONSULTA_IDEN_PROSPECTO,"ERROR CONSULTA IDENTIFICACION PROSPECTO");
		descError.put(SIN_CONCEPTO_OPERACION,"Seleccione concepto operacion");
		descError.put(ERROR_ACTUALIZAR_COBERTURA_OPERACION,"ERROR_ACTUALIZAR_COBERTURA_OPERACION");
		descError.put(CAPTURE_COMENTARIO,"Verifique falta capturar el comentario.");
		descError.put(ERROR_INESPERADO,"ERROR INESPERADO %s");
		descError.put(ERR_GENERAR_PERSONA,"NO SE GENERO LA PERSONA EN EL CORE DE CLIENTES");
		descError.put(INEXISTENTE_CONCEPTO,"Verifique, no existe el concepto de operacion.");
		descError.put(ERR_ACTUALIZA_PERSONA_PROSPECTO,"ERROR EN ACTUALIZA PERSONA EN PROSPECTO");
		descError.put(ERR_ACTUALIZAR_CONSULTA,"ERROR AL ACTUALIZAR CONSULTA");
		descError.put(ERR_VALIDAR_RECHAZAR_PROSPECTO,"ERROR VALIDAR RECHAZAR PROSPECTO");
		descError.put(ERR_RECHAZAR_PROSPECTO,"ERROR EN RECHAZAR PROSPECTO");
		descError.put(DIAS_PAGO_MAL,"DIAS DE PAGO INCORRECTOS");
		descError.put(FECHA_MAYOR,"VERIFIQUE FECHA, MAYOR AL DIA ACTUAL");
		descError.put(ERR_DATOS_PROSPECTO_NATURAL_CONSULTA_SIC,"ERROR EN DATOS PROSPECTO NATURAL CONSULTA SIC");
		descError.put(ERR_LISTADO_FRECUENCIA,"ERROR EN LISTADO FRECUENCIA");
		descError.put(ERR_PARAMETROS_PROD_FREC,"ERROR AL CONSULTAR PARAMETROS DE PRODUCTO DE FRECUENCIA");
		descError.put(ERROR_DATOS_PERSONA_NATURAL_CONSULTA_SIC,"ERROR AL OBTENER DATOS PERSONA NATURAL CONSULTA SIC");
		descError.put(ERR_NUEVO_MAESTRO_SOLICITUD,"ERROR EN NUEVO MAESTRO SOLICITUD");
		descError.put(FALTA_NOMBRE_TITULAR,"NOMBRE TITULAR NO ESPECIFICADO");
		descError.put(FALTA_DATOS_PRODUCTO,"DATOS DEL PRODUCTO NO ESPECIFICADOS");
		descError.put(FALTAN_DATOS_COMPORTAMIENTO_TITULAR,"FALTAN DATOS COMPORTAMIENTO TITULAR");
		descError.put(FALTA_EDO_SOLICITUD_ID,"ESTADO SOLICITUD NO ESPECIFICADO");
		descError.put(FALTA_ESQUEMA_OPERACION_ID,"ESQUEMA DE OPERACION NO ESPECIFICADO");
		descError.put(FALTA_SUCURSAL_ID,"SUCURSAL NO ESPECIFICADA");
		descError.put(FALTA_TIPO_SOLICITUD_ID,"TIPO SOLICITUD NO ESPECIFICADO");
		descError.put(IND_PRESTAMOS_NO_ESPECIFICADOS,"INDICADORES DE PRESTAMOS NO ESPECIFICADOS");
		descError.put(FALTAN_CAMPOS_TRX,"CAMPOS PARA TRANSACCION NO ESPECIFICADOS");
		descError.put(ERR_VALIDA_CAMPOS_TRAX,"ERROR EN VALIDA CAMPOS TRANSACCION");
		descError.put(PLAZO_FUERA_RANGO,"PLAZO FUERA DE RANGO PERMITIDO DEL PRODUCTO");
		descError.put(ERR_VALIDA_CVE_CONCEPTO_OP,"ERROR EN VALIDA CLAVE CONCEPTO OPERACION");
		descError.put(ERROR_BUSCAR_DATOS_COBERTURA_OPERACION,"ERROR_BUSCAR_DATOS_COBERTURA_OPERACION");
		descError.put(FALTA_IDE_PRINCIPAL,"PERSONA SIN IDENTIFICACION PRINCIPAL");
		descError.put(ERR_VALIDAR_ALTA_SOL_N1,"ERROR EN VALIDAR ALTA SOLICITUD NIVEL 1");
		descError.put(ERROR_VALIDAR_DATOS_LIBERAR_COBERTURA_OPERACION,"ERROR_VALIDAR_DATOS_LIBERAR_COBERTURA_OPERACION");
		descError.put(ERROR_LIBERAR_COBERTRA_OPERACION,"ERROR_LIBERAR_COBERTRA_OPERACION");
		descError.put(PRODUCTO_SIN_DIAS_PAGO,"PRODUCTO NO PERMITE DIAS DE PAGO");
		descError.put(ERR_VALIDAR_PARAM_PRODUCTO,"ERROR EN VALIDAR PARAMETROS PRODUCTO");
		descError.put(ERROR_MODIFICAR_COBERTURA_OPERACION,"ERROR_MODIFICAR_COBERTURA_OPERACION");
		descError.put(ERROR_AUTORIZAR_REGISTRO_PROSPECTO,"ERROR AL AUTORIZAR REGISTRO PROSPECTO");
		descError.put(ERR_ALTA_SOL_N1,"ERROR EN ALTA SOLICITUD NIVEL 1");
		descError.put(ERROR_OBTENER_DATOS_MAESTROPERSONA,"ERROR_OBTENER_DATOS_MAESTROPERSONA");
		descError.put(ENTIDAD_ASIGNADA_NO_EXISTE,"LA ENTIDAD ASIGNADA NO EXISTE");
		descError.put(CAPTURE_DESTINO,"CAPTURE DESTINO");
		descError.put(ERR_NVO__DESTINO_SOL,"ERROR EN NUEVO DESTINO SOLICITUD");
		descError.put(IMPORTE_INCORRECTO,"IMPORTE INCORRECTO");
		descError.put(SUM_IMPORTES_MAYOR_MTO_SOL,"SUMA IMPORTES MAYOR A MONTO SOLICITADO");
		descError.put(ERR_VALIDAR_DESTINO_SOL,"ERROR VALIDAR DESTINO SOLICITUD");
		descError.put(ERR_CONSULTA_DATOS_SOLICITUD,"ERROR EN CONSULTA DATOS SOLICITUD");
		descError.put(ERR_AUT_SOL_N1,"ERROR AL AUTORIZAR SOLICITUD NIVEL 1");
		descError.put(SOLICITUD_RECHAZADA,"LA SOLICITUD YA HA SIDO RECHAZADA");
		descError.put(SOLICITUD_AUTORIZADA,"LA SOLICITUD YA HA SIDO AUTORIZADA");
		descError.put(ERR_VALIDAR_AUT_SOL_N1,"ERROR EN VALIDAR AUTORIZAR SOLICITUD NIVEL1");
		descError.put(ERROR_OBTENER_EJB,"ERROR_OBTENER_EJB");
		descError.put(ERROR_ALTA_DATOS_SOLICITUD,"ERROR_ALTA_DATOS_SOLICITUD");
		descError.put(ERR_OBTENER_LIST_GTIAID,"ERROR AL OBTENER LISTADO GARANTIAID");
		descError.put(ERROR_ALTA_DESTINO_SOLICITUD,"ERROR_ALTA_DESTINO_SOLICITUD");
		descError.put(ERR_NVO_SOL_VINCULADO,"ERROR EN NUEVO SOLICITUD VINCULADO");
		descError.put(PORC_MENOR_CERO,"PORCENTAJE MENOR A CERO");
		descError.put(ERR_VALIDAR_SOL_VINCULADO,"ERROR VALIDAR SOLICITUD VINCULADO");
		descError.put(ERROR_NUEVO_TITULAR_SOLICITUD,"ERRO AL DAR DE ALTA UN TITULAR SOLICITUD");
		descError.put(TITULAR_YA_EXISTE,"EL TITULAR YA EXISTE");
		descError.put(ERR_ACTUALIZA_DESTINO_SOLICITUD,"ERROR EN ACTUALIZA DESTINO PPAL SOLICITUD");
		descError.put(ERR_VALIDAR_RECHAZAR_SOLICITUD,"ERROR EN VALIDAR RECHAZAR SOLICITUD");
		descError.put(ERR_NVO_SOL_MEDIO_PAGO,"ERROR EN NUEVO SOLICITUD MEDIO PAGO");
		descError.put(ERR_NVO_HISTORICO_PROMOTOR,"ERROR EN NUEVO HISTORICO PROMOTOR");
		descError.put(SELEC_SOLI_O_PROSPECTO,"SELECCIONE SOLICITUD O PROSPECTO");
		descError.put(ERR_BAJA_HISTORICO_PROMOTOR,"ERROR EN BAJA HISTORICO PROMOTOR");
		descError.put(ERR_BAJA_DATOS_SOLI,"ERROR EN BAJA DATOS_SOLICITUD");
		descError.put(ERR_BAJA_DESTINO_SOLI,"ERROR EN BAJA DESTINO POR SOLICITUD");
		descError.put(ERR_BAJA_EXPEDIENTE_SOLI,"ERROR EN BAJA EXPEDIENTE POR SOLICITUD");
		descError.put(ERR_BAJA_MEDIO_PAGO_SOLI,"ERROR EN BAJA MEDIO PAGO POR SOLICITUD");
		descError.put(ERR_BAJA_NEGOCIO_SOLI,"ERROR EN BAJA NEGOCIO POR SOLICITUD");
		descError.put(ERR_BAJA_TITULAR_SOLI,"ERROR EN BAJA TITULAR POR SOLICITUD");
		descError.put(ERR_BAJA_PODER_VINCULADO,"ERROR EN BAJA PODER POR VINCULADO");
		descError.put(ERR_CONSULTA_DESTINO_SOLICITUD,"ERROR EN CONSULTA DESTINO SOLICITUD");
		descError.put(ERR_CONSULTA_DESTINO_PRINCIPAL,"ERROR EN CONSULTA DESTINO PRINCIPAL");
		descError.put(SIN_DESTINO_PRINCIPAL,"SOLICITUD SIN DESTINO PRINCIPAL");
		descError.put(LISTADO_SOLICITUD_GARANTIAS_VACIO,"LISTADO SOLICITUD GARANTIAS VACIO");
		descError.put(ERROR_CREAR_COBERTURA_SOLICITUD,"ERROR AL CREAR COBERTURA SOLICITUD");
		descError.put(ERROR_CONSULTA_CIRCULO_CREDITO,"ERROR_CONSULTA_CIRCULO_CREDITO");
		descError.put(ERROR_CONSULTA_BURO_CREDITO,"ERROR AL CONSULTAR BURO DE CREDITO");
		descError.put(LISTADO_SOLICITUD_NEGOCIOS_VACIO,"LISTADO SOLICITUD NEGOCIOS VACIO");
		descError.put(ERROR_BUSCAR_DATOS_SOLICITUD_NEGOCIO,"ERROR BUSCAR DATOS SOLICITUD NEGOCIO");
		descError.put(IDENTIFICACION_DUPLICADA,"VERIFICAR, IDENTIFICACION DUPLICADA.");
		descError.put(SIN_RESULTADOS,"SIN RESULTADOS");
		descError.put(CAPTURE_ALMENOS_2,"verifique, debe capturar un valor de busqueda de por lo menos 2 caracteres");
		descError.put(BUSQUEDA_NEGOCIO_SIN_RESULTADOS,"BUSQUEDA DE NEGOCIO SIN RESULTADOS");
		descError.put(BUSQUEDA_BITACORA_SIN_RESULTADOS,"BUSQUEDA DE BITACORA SIC SIN RESULTADOS");
		descError.put(ERROR_BURO_RETORNA_ERROR_CONSULTA,"BURO RETORNO ERROR AL REALIZAR LA CONSULTA, CONSULTE CON EL ADMINISTRADOR");
		descError.put(ERROR_BURO_TIPO_IDENTIDAD,"ERROR DE TIPO DE IDENTIDAD");
		descError.put(ERROR_CONSULTA_CUENTA_BURO,"ERROR AL CONSULTAR CUENTAS EN LA CONSULTA ACTUAL DE BURO");
		descError.put(ERROR_TIPO_HISTORIAL_CREDITICIO,"ERROR AL CONSULTAR EL TIPO DE CALIFICACION DE HISTORIAL CREDITICIO");
		descError.put(ERROR_CONSULTA_TIPO_HISTORIAL_CREDITICIO,"NO SE ENCONTRO TIPO DE CALIFICACION");
		descError.put(PERSONA_RECHAZADA,"LA PERSONA YA HA SIDO RECHAZADA");
		descError.put(ERROR_VALIDAR_RECHAZAR_PERSONA,"ERROR AL VALIDAR RECHAZAR PERSONA");
		descError.put(ERROR_RECHAZAR_PERSONA,"ERROR AL RECHAZAR PERSONA");
		descError.put(ERROR_BUSCAR_DATOS_SOLICITUD_CENTROS_TRABAJO,"ERROR AL BUSCAR DATOS SOLICITUD CENTROS TRABAJO");
		descError.put(ERROR_OBTENER_DATOS_NEGOCIO,"ERROR AL OBTENER DATOS NEGOCIO");
		descError.put(ERROR_BUSCAR_CENTRO_TRABAJO,"ERROR AL BUSCAR CENTRO TRABAJO");
		descError.put(ERR_VALIDAR_ALTA_SOL,"ERR_VALIDAR_ALTA_SOL");
		descError.put(ERR_VALIDAR_AUT_SOL,"ERR_VALIDAR_AUT_SOL");
		descError.put(ERR_AUT_SOL,"ERR_AUT_SOL");
		descError.put(REGLA_NEGOCIO,"Regla %s");
		descError.put(ERROR_REGISTRO_DUPLICADO, "REGISTRO DUPLICADO");
		descError.put(ERR_CONF_DESTINOS_PRODUCTO,"CONFIGURE DESTINOS EN EL PRODUCTO");
		descError.put(ERR_LIST_DESTINOS_PRODUCTO,"ERROR EN LISTADO DESTINOS PRODUCTO");
		descError.put(ERROR_DOCUMENTO_OBLIGATORIO,"EL DOCUMENTO ES OBLIGATORIO PARA LA INTEGRACION DEL EXPEDIENTE");
		descError.put(ERROR_PERSONA_TITULAR_SOLICITUD,"LA PERSONA ES TITULAR DE LA SOLICITUD, SELECCIONE OTRA");
		descError.put(SELECCIONE_PRESTAMO_LIQ,"EL PRESTAMO SELECCIONADO NO ESTA LIQUIDADO, SELECCIONE OTRO");
		descError.put(ERR_ACT_CONCEPTOS_SIC_DATOS_PERSONA_FALTA_PERSONA_ID,"ERROR AL ACTUALIZAR CONCEPTOS SIC EN DATOS PERSONA FALTA EL ID DE PERSONA");
		descError.put(ERR_CONSULTAR_BITACORA_SIC_FALTA_ID,"ERROR AL CONSULTAR INFORMACION DE BURO - FALTA ID BITACORA SIC");
		descError.put(ERR_CONSULTA_BITACORA_SIC,"ERROR AL CONSULTAR INFORMACION DE BURO");
		descError.put(ERR_ACT_SOLICITUD_BITACORASIC,"ERROR AL ACTUALIZAR SOLICITUD EN CONSULTA BURO");
		descError.put(ERR_REVISAR_EDO_SOLICITUD,"ERROR REVISAR ESTADO SOLICITUD");
		descError.put(ERR_VALIDA_EDO_SOL_CVE_VACIA,"ERROR EN VALIDA ESTADO SOLICITUD CLAVE VACIA");
		descError.put(ERR_VALIDA_EDO_SOL_CVE_NO_EX,"ERROR EN VALIDA ESTADO SOLICITUD CLAVE DE ESTADO NO EXISTE");
		descError.put(ERR_VALIDA_CVE_ESTADO_CVE_GPO_EDOS_VACIA,"ERROR EN VALIDA CLAVE DE ESTADO CLAVE DE GRUPO DE ESTADOS VACIA");
		descError.put(ERR_VALIDA_CVE_ESTADO_CVE_GPO_EDOS_NO_EX,"ERROR EN VALIDA CLAVE DE ESTADO CLAVE DE GRUPO DE ESTADOS NO EXISTE");
		descError.put(ERR_VALIDA_CVE_GPO_EDO,"ERROR EN VALIDA CLAVE GRUPO DE ESTADOS");
		descError.put(ERR_CAL_CAP_PAGO_ACT_PARAMETROS_PRODUCTO,"ERROR AL CALCULAR CAPACIDAD DE PAGO ACTUALICE PARAMETROS DEL PRODUCTO");
		descError.put(ERROR_OBTENER_CUOTA_ESTIMADA,"ERROR AL OBTENER CUOTA ESTIMADA");
		descError.put(ERROR_OBTENER_MONTO_FINANCIADO,"ERROR AL OBTENER MONTO FINANCIADO");
		descError.put(ERR_INESPERADO_GUARDAR_EXP_SOLICITUD,"ERROR INESPERADO AL GUARDAR EXPEDIENTE SOLICITUD");
		descError.put(CAP_DETALLE_DOCTO_OBLIGATORIO,"CAPTURE DETALLE PARA DOCUMENTO OBLIGATORIO");
		descError.put(DIG_IMAGEN_DOCTO_OBLIGATORIO,"DIGITALICE IMAGEN PARA DOCUMENTO OBLIGATORIO");
		descError.put(EI_VAL_GUARDAR_EXP_SOLICITUD,"ERROR INESPERADO AL VALIDAR EXPEDIENTE SOLICITUD");
		descError.put(CONF_FREC_PAGO_CENTROS_TRABAJO_PERSONA,"CONFIGURA FRECUENCIA DE PAGO EN CENTROS DE TRABAJO DE LA PERSONA");
		descError.put(BUSQUEDA_PERSONAS_SIN_RESULTADOS,"BUSQUEDA DE PERSONAS SIN RESULTADOS");
		descError.put(BUSQUEDA_CONVENIO_MEDICO_SIN_RESULTADOS,"BUSQUEDA DEL CONVENIO MEDICO SIN RESULTADOS");
		descError.put(SOLO_UN_COACREDITADO,"LISTA DE COACREDITADOS NO PERMITE MAS DE 1 COACREDITADOS");
		descError.put(EI_VALIDAR_CANAL_VENTA_PROSPECTO,"ERROR INESPERADO AL VALIDAR CANAL DE VENTA DEL PROSPECTO");
		descError.put(ACTUALICE_INFORMACION_CLIENTE,"ACTUALICE INFORMACION DEL CLIENTE");
		descError.put(ACTUALICE_INFORMACION_NEGOCIO,"ACTUALICE INFORMACION DEL NEGOCIO EN EL MODULO DE CLIENTES");
		descError.put(CAPTURE_FECHA,"CAPTURE FECHA");
		descError.put(FALTA_INFO_GEN_CONVENIO,"FALTA INFORMACION PARA GENERAR CONVENIO");
		descError.put(SIN_DUENO,"SIN DUEÑO");
		descError.put(SIN_FECHA_INGRESO,"SIN FECHA DE INGRESO");
		descError.put(SIN_TIPO_CONTRATO,"SIN TIPO DE CONTRATO");
		descError.put(SIN_TIPO_INGRESO,"SIN TIPO DE INGRESO");
		descError.put(SIN_FRECUENCIA_PAGO,"SIN FRECUENCIA DE PAGO");
		descError.put(SIN_MONEDA,"SIN MONEDA");
		descError.put(SIN_INGRESO,"SIN INGRESO");
		descError.put(SIN_CARGO_LABORAL,"SIN CARGO LABORAL");
		descError.put(SIN_CENTRO_TRABAJO,"SIN CENTRO DE TRABAJO");
		descError.put(CAPTURE_TITULAR_COMO_BENEFICIARIO,"CAPTURE AL TITULAR COMO BENEFICIARIO");
		descError.put(ERR_ING_MENSUAL,"CAPTURE EL INGRESO MENSUAL");
		descError.put(ERR_COD_VINC,"CAPTURE EL CODIGO DE VINCULACION CON EL TITUALR");
		descError.put(ERR_ALTA_LIST_PROV_REC,"ERROR EN EL METODO ALTA LISTADO PROVEEDORES DE RECURSOS SOLICITUD");
		descError.put(ERR_MTO_MAX_ANT,"CAPTURE MONTO MAXIMO COMO ANTICIPO");
		descError.put(ERR_MTO_MAX_PAGAR,"CAPTURE MONTO MAXIMO A PAGAR EN UN MES");
		descError.put(CAPTURE_PROVEEDOR_RECURSOS,"CAPTURE A UN PROVEEDOR DE RECURSOS");
		descError.put(ERR_LIST_BENEF,"LA LISTA DE BENEFICIARIOS DEBE INCLUIR POR LO MENOS AL TITULAR");
		descError.put(ERR_VAL_ALTA_LIST_BENEF,"ERROR EN EL METODO VALIDA LISTADO BENEFICIARIOS SOLICITUD");
		descError.put(ERROR_ALTA_LISTADO_BENEFICIARIOS_SOLICITUD,"ERROR EN  ALTA LISTADO BENEFICIARIOS SOLICITUD");
		descError.put(ERR_VAL_ALTA_LIST_PROV_REC,"ERROR EN EL METODO VALIDAR ALTA LISTADO PROVEEDORES DE RECURSOS");
		descError.put(ERR_BUSQUEDA_BITACORASIC,"ERROR EN LA BUSQUEDA DE BITACORA SIC");
		descError.put(ERR_METODO_CONSULTA_BITACORASIC,"ERROR AL CONSULTAR EL METODO DE BITACORA SIC");
		descError.put(ERR_BITACORASICID_NO_EXISTE,"ERROR BITACORASICID NO EXISTE");
		descError.put(ACT_CONDICION_PROPIEDAD_DE_CLIENTES,"ACTUALICE CONDICION DE LA PROPIEDAD EN EL MODULO DE CLIENTES");
		descError.put(EI_ALTA_NEGOCIO_SOLICITUD,"ERROR INESPERADO EN ALTA NEGOCIO SOLICITUD");
		descError.put(FALTA_PROPIETARIO_NEGOCIO,"FALTA PROPIETARIO DEL NEGOCIO");
		descError.put(ERR_REVISAR_CALIFICACION,"ERROR EN REVISAR CALIFICACION");
		descError.put(EI_REVISA_CALIFICACION,"ERROR INESPERADO EN REVISA CALIFICACION");
		descError.put(ERR_ESPECIFIQUE_CONSULTA_REVISA_CALIFICACION,"ESPECIFIQUE CONSULTA PARA REVISAR CALIFICACION");
		descError.put(DIA_PAGO1_MAYOR_2,"VERIFIQUE DIAS DE PAGO EL DIA UNO DEBE SER MENOR AL DIA DOS");
		descError.put(SELECCIONE_HOMONIMO,"SELECCIONE UN HOMONIMO");
		descError.put(DOM_PERSONA_TITULAR_CUENTA_VACIO,"SELECCIONE PERSONA TITULAR DE LA CUENTA A DOMICILIAR");
		descError.put(DOM_FALTA_INFO,"CAPTURE LA INFORMACION PARA DOMICILIAR EL PAGO");
		descError.put(DOM_LONGITUD_CLABE,"VERIFIQUE LONGITUD DE CLABE INTERBANCARIA");
		descError.put(DOM_LONGITUD_CUENTA,"VERIFIQUE LONGITUD DE LA CUENTA");
		descError.put(DOM_LONGITUD_NUM_TARJETA,"VERIFIQUE LONGITUD DEL NUMERO DE TARJETA");
		descError.put(ERR_LIST_PERSONAS_HOMONIMO_,"ERROR EN LISTADO PERSONAS HOMONIMO");
		descError.put(FOLIO_CONSULTA_VACIO_COMPROBANTE,"FOLIO CONSULTA VACIO PARA ACTUALIZAR COMPROBANTE DE PAGO");
		descError.put(EI_GUARDAR_BITACORA_SIC_DETALLE,"ERROR INESPERADO AL GUARDAR COMPROBANTE DE PAGO");
		descError.put(FOLIO_CONSULTA_VACIO_DATOS,"FOLIO CONSULTA PARA CONSULTAR DATOS");
		descError.put(NO_EX_FOLIO_CONSULTA,"NO EXISTE REGISTRO DEL FOLIO CONSULTA");
		descError.put(EI_CONSULTA_BITACORA_SIC_FOLIO,"ERROR INESPERADO EN CONSULTA SIC POR FOLIO");
		descError.put(CVE_EDO_BITACORA_VACIO,"ERROR AL ACTUALIZAR ESTADO DE CONSULTA CLAVE VACIA");
		descError.put(EI_ACTUALIZA_ESTADO_CONSULTA,"ERROR INESPERADO AL ACTUALIZAR ESTADO CONSULTA");
		descError.put(CONSULTA_NO_AUTORIZADA,"CONSULTA NO AUTORIZADA");
		descError.put(EI_PROSPECTO_BUSCAR,"ERROR INESPERADO AL OBTENER LISTADO DE PROSPECTOS");
		descError.put(EI_SVC_PROSPECTO_BUSCAR,"ERROR INESPERADO EN EL SERVICIO PARA LISTADO DE PROSPECTOS");
		descError.put(TIPO_PERSONA_VACIO,"ESPECIFIQUE TIPO DE PERSONA");
		descError.put(NPJ_TIPO_PERSONA_NO_JURIDICA,"ERROR AL REGISTAR PROSPECTO JURIDICO TIPO DE PERSONA NO VALIDO");
		descError.put(NPJ_IDENTIFICACION_VACIA,"ERROR AL REGISTAR PROSPECTO JURIDICO IDENTIFICACION VACIA");
		descError.put(NPJ_TIPO_IDENTIFICACION_VACIA,"ERROR AL REGISTAR PROSPECTO JURIDICO TIPO IDENTIFICACION VACIA");
		descError.put(ERR_NUEVO_PROSPECTO_JURIDICO,"ERROR EN NUEVO PROSPECTO JURIDICO");
		descError.put(EI_LISTA_PROSPECTO_BUSCAR,"ERROR INESPERADO AL OBTENER LISTA PROSPECTO BUSCADOS");
		descError.put(LPB_CLAVE_CVE_ESTADO_PROSPECTO_VACIO,"ESPECIFIQUE LA CLAVE DE ESTADO");
		descError.put(CONSULTAS_FALTANTES,"REALICE CONSULTAS FALTANTES");
		descError.put(FALTA_RESULTADO_BI,"FALTA RESULTADO DE BURO INTERNO");
		descError.put(ACT_PERSONAID_COACREDITADA,"ACTUALICE LA PERSONA YA QUE NO COINCIDE CON LA SELECCIONADA EN BURO INTERNO");
		descError.put(ACT_PERSONAID_REPRESENTANTE,"ACTUALICE REPRESENTANTE YA QUE NO COINCIDE CON LA SELECCIONADA EN BURO INTERNO");
		descError.put(CLIENTE_REDISPOSICION,"EL CLIENTE CUENTA CON CREDITOS DE ESTE PRODUCTE INICIE REDISPOSICION DE PRESTAMO");
		descError.put(REGI_PJ_MODULO_CLIENTES,"REGISTRE PERSONA JURIDICA EN MODULO DE CLIENTES");
		descError.put(SELECCIONE_PRESTAMO_ACT,"EL PRESTAMO SELECCIONADO NO ESTA ACTIVO, SELECCIONE OTRO");
		descError.put(EI_REDISPOSICION_AUTOMATICA,"ERROR INESPERADO EN REDISPOSICION AUTOMATICA");
		descError.put(EI_REDISPOSICION_ANTICIPADA,"ERROR INESPERADO EN REDISPOSICION ANTICIPADA");
		descError.put(FALTA_SOL_ANTERIOR,"EL PRESTAMO ANTERIOR NO TIENE SOLICITUD PARA OBTENER INFORMACION");
		descError.put(LSN_SOLICITUD_ID_VACIO,"ESPECIFIQUE SOLICITUD PARA OBTENER EL LISTADO DE SUS NEGOCIOS");
		descError.put(EI_LISTA_SOLICITUD_NEGOCIO,"ERROR INESPRADO EN LISTA SOLICITUD NEGOCIO");
		descError.put(FALTA_SOL_ANTERIOR,"EL PRESTAMO ANTERIOR NO TIENE SOLICITUD PARA OBTENER INFORMACION");
		descError.put(CAPTURE_MODO_CONTACTO_SIGUIENTE,"CAPTURE MODO DE CONTACTO SIGUIENTE");
		descError.put(APPI_ESPECIFIQUE_PROCESO_INICIADO,"ESPECIFIQUE PROCESO INICIADO PARA ACTUALIZARLO AL PROSPECTO");
		descError.put(EI_ACTUALIZA_PROSPECTO_PROCESO_INICIADO,"ERROR INESPERADO AL ACTUALIZAR PROCESO INICIADO AL PROSPECTO");
		descError.put(PROSPECTO_ACTIVO,"PROSPECTO CON PROCESO ACTIVO");		
		descError.put(EXISTE_FOLIO_CONSULTA,"FOLIO CONSULTA YA EXISTE");
		descError.put(EI_VALIDA_ALTA_BITACORA_SIC,"ERROR AL VALIDAR ALTA BITACORA SIC");
		descError.put(PRESTAMO_REDISPOSICION_ACTIVA,"PRESTAMO CON PROCESO DE REDISPOSICION ACTIVA");
		descError.put(_EI_GUARDAR_PERSONA_BITACORA_SIC,"ERROR AL GUARDAR DATOS PERSONA BITACORA SIC");
		descError.put(EI_GUARDAR_CALIFICACION_BITACORA_SIC,"ERROR AL GUARDAR CALIFICACION BITACORA SIC");
		descError.put(CAPTURE_SCORING,"CAPTURE SCORING");
		descError.put(ERROR_CONF_RECHAZO_SCORING,"ERROR DE CONFIGURACION EN LOS MOTIVOS DE RECHAZO NO EXISTE RECHAZADO POR SCORING");
		descError.put(ERROR_PORC_BENEFICIARIOS,"VERIFIQUE PORCENTAJE DE BENEFICIARIOS");
		descError.put(DOM_VACIO_MOD_CLIENTES,"DOMICILIO VACIO, VERIFIQUE INFORMACION EN EL MODULO DE CLIENTES");
		descError.put(ABS_EMPRESA_REQUERIDA,"EMPRESA REQUERIDA PARA DAR DE ALTA BENEFICIARIOS DEL SEGURO");
		descError.put(ABS_LISTA_BENEFICIARIOS_VACIA,"LA LISTA DE BENEFICIARIOS DEL SEGURO ESTA VACIA");
		descError.put(EI_ALTA_BENEFICIARIOS_SEGURO,"ERROR INESPERADO EN ALTA BENEFICIARIOS SEGURO");
		descError.put(ABS_CAMPO_REQUERIDO,"CAMPO %s REQUERIDOS PARA ALTA DE BENEFICIARIOS SEGURO");
		descError.put(EI_VAL_ALTA_BENEF_SEGURO,"ERROR INESPERADO EN EL METODO VALIDA BENEFICIARIOS SEGURO");
		descError.put(ABS_TITULAR_BENEFICIARIO,"EL TITULAR DE LA SOLICITUD NO PUEDE SER BENEFICIARIO DEL SEGURO");
		descError.put(CBS_CAMPO_REQUERIDOS,"CAMPOS %REQUERIDOS PARA CONSULTAR BENEFICIARIOS");
		descError.put(EI_CONSULTA_BENEFICIARIOS_SEGURO,"ERROR INESPERADO EN CONSULTA BENEFICIARIOS SEGURO");
		descError.put(EDAD_BENEFICIARIO_SEGURO_INCORRECTA,"EDAD DEL BENEFICIARIO SEGURO INCORRECTA");
		descError.put(ERR_OBT_CONCP_TARF_ID,"ERROR AL OBTENER CONCEPTO TARIFA ID");
		descError.put(ERR_OBT_DESC_CONCP_TAR,"ERROR AL OBTENER DESCRIPCION CONCEPTO TARIFA");
		descError.put(ERR_OBT_TAS_EFCT_ANUAL,"ERROR AL OBTENER TASA EFECTIVA ANUAL");
		descError.put(ERR_OBT_TASA_REAL,"ERROR AL OBTENER TASA REAL");
		descError.put(ERR_ACT_ESTADO_SOLICITUD,"ERROR AL ACTUALIZAR ESTADO SOLICITUD");
		descError.put(ERR_CONF_PROD_MTO_MINIMO,"ERROR EN LA CONFIGURACION DEL PRODUCTO MONTO MINIMO VACIO O CERO");
		descError.put(ERR_CONF_PROD_CVE_TIPO_PRODUCTO,"ERROR EN LA CONFIGURACION DEL PRODUCTO TIPO VACIO");
		descError.put(ERR_CONF_PROD_CVE_SUBTIPO_PRODUCTO,"ERROR EN LA CONFIGURACION DEL PRODUCTO SUB TIPO VACIO");
		descError.put(ERR_COMPROBACION_INGRESOS,"FORMA DE COMPROBACION DE INGRESOS INCORRECTA SELECCIONE ACTIVIDAD PRODUCTIVA");
		descError.put(ERR_TIPO_PROPIEDAD,"TIPO PROPIEDAD DEL DOMICILIO DE LA VIVIENDA NO PERMITIDO");
		descError.put(ERROR_CIRCULO_TIPO_IDENTIDAD,"ERROR DE TIPO DE IDENTIDAD");
		descError.put(ERROR_CIRCULO_RETORNA_ERROR_CONSULTA,"CIRCULO RETORNO ERROR AL REALIZAR LA CONSULTA, CONSULTE CON EL ADMINISTRADOR");
		descError.put(ERROR_SISTACC,"ERROR DEL SISTEMA DE CARTERA %s");
		descError.put(EI_REDISPOSICION_NORMAL,"ERROR INESPERADO EN REDISPOSICION NORMAL");
		descError.put(ERROR_MTO_REDISPONER_NEGATIVO,"ERROR EL SALDO DEL CREDITO NO SE PUEDE REDISPONER CON EL PRODUCTO SELECCIONADO");
		descError.put(EI_VALIDA_GARANTIAS_REDISPOSICION,"ERROR INESPERADO EN VALIDA GARANTIAS_REDISPOSICION");
		descError.put(EI_MTC,"ERROR INESPERADO EN REDISPOSICION MEJORA TU CASA");
		descError.put(EI_LISTA_SOLICITUD_CENTROS_TRABAJO,"ERROR INESPERADO EN LISTA SOLICITUD CENTROS DE TRABAJO");
		descError.put(EI_ALTA_SOL_REPROGRAMACION,"ERROR INESPERADO AL DAR DE ALTA SOLICITUD DE REPROGRAMACION");
		descError.put(REALICE_SIMULACION,"REALICE SIMULACION");
		descError.put(EIWS_VALIDAR_SOL_REPROGRAMACION,"ERROR INESPERADO EN VALIDAR SOLICITUD DE REPROGRAMACION");
		descError.put(EWS_VSREP_REALICE_SIMULACION,"ERROR EN EL WS VALIDAR SOLICITUD REALICE SIMULACION");
		descError.put(EWS_VSREP__SELECCIONE_OPT_CAPTURAR,"ERROR EN EL WS VALIDAR SOLICITUD SELECCIONE SOLO UNA OPCION A CAPTURAR");
		descError.put(EIWS_AUTORIZAR_SOL_REPROGRAMACION,"ERROR INESPERADO WS AL AUTORIZAR SOLICITUD DE REPROGRAMACION");
	}
}


