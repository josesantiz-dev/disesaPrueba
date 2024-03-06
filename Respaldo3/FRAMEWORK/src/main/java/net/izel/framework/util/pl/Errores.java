
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de errores en servicios
*
*/

package net.izel.framework.util.pl;

import java.util.HashMap;

public class Errores { 

	/*Seleccione cliente*/
	public static final long SELECCIONE_CLIENTE = 1;

	/*Seleccione solicitud*/
	public static final long SELECCIONE_SOLICITUD = 2;

	/*CAMPO %s REQUERIDO*/
	public static final long CAPTURE_CAMPOS_REQUERIDOS = 3;

	/*Capture informacion de Cuotas dobles*/
	public static final long CAPTURE_CUOTAS_DOBLES = 6;

	/*Capture renglones incompletos*/
	public static final long CAPTURE_RENGLONES_INCOMPLETOS = 7;

	/*o*/
	public static final long CAPTURE_ANIO = 8;

	/*Calcule monto*/
	public static final long CALCULE_MONTO = 10;

	/*Capture medio de pago*/
	public static final long CAPTURE_MEDIO_PAGO = 11;

	/*Prestamo validado*/
	public static final long PRESTAMO_VALIDADO = 12;

	/*Genere cronograma*/
	public static final long GENERE_CRONOGRAMA = 14;

	/*Verifique tipo y subtipo de garantia*/
	public static final long VERIFIQUE_TIPO_SUBTIPO_GTIA = 15;

	/*Porcentaje de cobertura de garantias incorrecto*/
	public static final long COBERTURA_GTIAS_INCORRECTO = 16;

	/*Tipo de cobertura en garantias incorrecto*/
	public static final long TIPO_COBERTURA_GTIAS_INCORRECTO = 17;

	/*Condicion de las garantias incorrecta*/
	public static final long CONDICION_GTIA_INCORRECTA = 18;

	/*Elimine garantias no se requieren*/
	public static final long ELIMINE_GARANTIAS = 19;

	/*Verifique depositos de garantia liquida */
	public static final long VERIFIQUE_DEPOSITOS_GTIA_LIQ = 20;

	/*Concepto de deposito anticipado no configurado para el producto*/
	public static final long CONCEPTO_DEP_ANT_NO_EN_PRODUCTO = 21;

	/*Genere o capture identificadores para cuenta*/
	public static final long GENERE_CAPTURE_IDENTIFICADOR_CTA = 22;

	/*Seleccione documento*/
	public static final long SELECCIONE_DOCUMENTO = 23;

	/*Fecha menor a la actual*/
	public static final long FECHA_MENOR_ACTUAL = 25;

	/*Programe desembolso(s)*/
	public static final long PROGRAME_DESEMBOLSO = 27;

	/*Capture importe*/
	public static final long CAPTURE_IMPORTE = 28;

	/*El importe excede del monto programado*/
	public static final long IMPORTE_EXCEDE_MTO_PROGRAMADO = 29;

	/*SELECCIONE SOLICITUD DE CAMBIO DE CONDICIONES*/
	public static final long SELECCIONE_SOLICITUD_CAMBIO_CONDICIONES = 30;

	/*No existe monto a pagar*/
	public static final long NO_EXISTE_MONTO = 31;

	/*Verifique monto a pagar*/
	public static final long VERIFIQUE_MTO_PAGAR = 32;

	/*Verifique columna total*/
	public static final long VERIFIQUE_COLUMNA_TOTAL = 33;

	/*Capture tipo de distribucion*/
	public static final long CAPTURE_TIPO_DISTRIBUCION = 34;

	/*Verifique porcentaje de garantias*/
	public static final long VERIFIQUE_PORCENTAJE_GARANTIAS = 35;

	/*CAMBIO DE CONDICIONES ACTIVAS*/
	public static final long CAMBIO_CONDICIONES_ACTIVAS = 36;

	/*Seleccione solicitud de reprogramacion*/
	public static final long SELECCIONE_SOLICITUD_REPROGRAMACION = 37;

	/*Reprogramacion activa*/
	public static final long REPROGRAMACION_ACTIVA = 38;

	/*No se ha realizado compromiso de pago*/
	public static final long NO_HA_REALIZADO_COMPROMISO_PAGO = 39;

	/*Seleccione informacion previa*/
	public static final long SELECCIONE_INFO_PREVIA = 40;

	/*Capture frecuencia pago y numero de cuotas*/
	public static final long CAPTURE_FRECUENCIA_Y_CUOTAS = 41;

	/*Capture monto solicitado y numero de desembolsos*/
	public static final long CAPTURE_MONTO_SOL_Y_NO_DESEMBOLSO = 42;

	/*Capture desembolsos*/
	public static final long CAPTURE_DESEMBOLSOS = 43;

	/*Verifique informacion de desembolsos*/
	public static final long VERIFIQUE_INF_DESEMBOLSOS = 44;

	/*Verifique fechas de desembolsos*/
	public static final long VERIFIQUE_FECHAS_DESEMBOLSO = 45;

	/*La suma total de desembolsos no coincide con el monto solicitado*/
	public static final long SUMA_TOTAL_DESEMBOLSO_DIFERENTE_MONTO_SOL = 46;

	/*Verifique fecha de desembolso menor a la actual*/
	public static final long FECHA_DESEMBOLSO_MENOR_ACUTUAL = 47;

	/*Excede el maximo de desembolsos del producto*/
	public static final long EXCEDE_MAX_DESEMBOLSO = 48;

	/*Dias de pago incorrectos respecto a la configuracion del producto*/
	public static final long DIAS_PAGO_INCORRECTOS = 49;

	/*El Monto a desembolsar debe ser menor al Monto solicitado*/
	public static final long MONTO_DESEMBOLSAR_MAYOR_SOLICITADO = 50;

	/*Verifique, fecha mayor al dia actual.*/
	public static final long FECHA_MAYOR = 51;

	/*Verifique dias de pago*/
	public static final long VERIFIQUE_DIAS_PAGO = 52;

	/*Modalidad no permite cuotas dobles*/
	public static final long MODALIDAD_NO_PERMITE_CUOTAS_DOBLES = 53;

	/*Verifique total no coincide con importe de operacion*/
	public static final long MONTO_OPERACION_DIFERENTE_SUMA_TOTAL = 54;

	/*El vencimiento y cuenta ya se registraron*/
	public static final long VENC_Y_CUENTA_YA_REGISTRARON = 55;

	/*Longitud Incorrecta*/
	public static final long _LONGITUD_INCORRECTA = 56;

	/*El identificador no cumple con el formato establecido*/
	public static final long IDENTIFICADOR_NO_CUMPLE_FORMATO_ESTABLECIDO = 57;

	/*Registro dado de baja*/
	public static final long REGISTRO_DADO_DE_BAJA = 58;

	/*Desea dar de baja el registro*/
	public static final long DAR_BAJA_REGISTRO = 59;

	/*La cuenta ya se registro*/
	public static final long CUENTA_YA_SE_REGISTRO = 60;

	/*El vencimiento ya se registro*/
	public static final long VENC_YA_SE_REGISTRO = 61;

	/*ERROR INESPERADO %s*/
	public static final long ERROR_INESPERADO = 101;

	/*Verifique, debe seleccionar un criterio del listado de Tipo de busqueda*/
	public static final long SIN_CRITERIO_BUSQUEDA = 102;

	/*Verifique, debe capturar un valor de busqueda de por lo menos 2 caracteres*/
	public static final long ALMENOS_DOS_CARACTERES = 103;

	/*Primero busque y seleccione una persona natural*/
	public static final long SOLO_NATURAL = 104;

	/*Verifique, primero seleccione un prestamo.*/
	public static final long SIN_PRESTAMO = 105;

	/*Verifique, el porcentaje debe estar en el rango de 1-100*/
	public static final long PORCENTAJE_ERROR = 106;

	/*Verifique la secuencia de las fechas capturadas estan invertidas*/
	public static final long FECHAS_INVERTIDAS = 107;

	/*Primero busque y seleccione una garantia*/
	public static final long SIN_GARANTIA = 108;

	/*Verifique, capture importes*/
	public static final long sin_importes = 109;

	/*DESEA APLICAR EL MEDIO DE PAGO? */
	public static final long APLICAR_MEDIO_PAGO = 110;

	/*VERIFIQUE MONTO*/
	public static final long VERIFIQUE_MTO = 111;

	/*MONTO INCORRECTO*/
	public static final long MTO_INCORRECTO = 112;

	/*ERROR AL CONSULTAR LOS DATOS DE PRESTAMO*/
	public static final long ERROR_CONSULTA_DATOS_PRESTAMO = 113;

	/*ERROR AL CONSULTAR MEDIOS DE PAGO DE DESEMBOLSOS PROGRAMADOS*/
	public static final long ERROR_CONSULTA_DESEMBOLSO_MEDIO_PAGO = 114;

	/*ERROR AL OBTENER LA SUMA DE IMPORTES*/
	public static final long ERR_OBTENER_SUMA_IMPORTES = 115;

	/*ERROR AL CONSULTAR DESEMBOLSOS PROGRAMADO*/
	public static final long ERR_CONSULTAR_DESEM_PROGRAMADOS = 116;

	/*ERROR AL BUSCAR LA CONFIGURACION DEL IDENTIFICADOR*/
	public static final long ERROR_BUSCAR_CONFIGURACION_IDENTIFICADOR_PRESTAMO = 117;

	/*ERROR AL CONSULTAR EL TIPO DE IDENTIFICADOR*/
	public static final long ERROR_CONSULTA_TIPO_IDENTIFICADOR = 118;

	/*ERROR AL VALIDAR PRESTAMO*/
	public static final long ERR_VALIDAR_PRESTAMO = 119;

	/*ERROR AL CREAR IDENTIFICADOR DE PRESTAMO*/
	public static final long ERROR_CREAR_IDENTIFICADOR_PRESTAMO = 120;

	/*ERROR EN CONSULTA COMPLETO PRESTAMO*/
	public static final long ERR_CONSULTA_COMPLETO_PRESTAMO = 121;

	/*ERROR AL CONSULTAR LAS CUENTAS BANCARIAS*/
	public static final long ERROR_LISTADO_CUENTAS_BANCARIAS = 122;

	/*ERROR EN IDENTIFICADOR POR VENCIMIENTO Y CUENTA BANCARIA*/
	public static final long ERROR_IDENTIFICADOR_VENCIMIENTO_CTA_BANCARIA = 123;

	/*ERROR EN CONSULTA GRUPO PLANTILLA DOCUMENTOS*/
	public static final long ERRWS_CONSULTA_GRUPO_PLANTILLA_DOCUMENTOS = 124;

	/*ERROR AL OBTENER EL LISTADO DE CRONOGRAMA CABECERA */
	public static final long ERROR_LISTADO_CRONOGRAMA_CABECERA_PRESTAMO_CUOTA = 125;

	/*ERROR AL CONSULTAR IDENTIFICADORES POR VENCIMIENTO Y CUENTA BANCARIA*/
	public static final long ERROR_CONSULTA_IDENT_VENC_CUENTA_BANCARIA = 126;

	/*ERROR AL CONSULTAR PARAMETROS DE PRODUCTO DE FRECUENCIA*/
	public static final long ERR_PARAMETROS_PROD_FREC = 127;

	/*CAPTURE TASAS*/
	public static final long CAPTURE_TASAS = 128;

	/*verfique, importe abonar excede el importe de la operacion*/
	public static final long IMPORTE_ABONAR_MAYOR = 129;

	/*verfique, importe cargo excede el importe de la operacion*/
	public static final long IMPORTE_CARGO_MAYOR = 130;

	/*verfique, Total abonar excede el importe de la operacion*/
	public static final long TOTAL_ABONAR_MAYOR = 131;

	/*verfique, Total cargo excede el importe de la operacion*/
	public static final long TOTAL_CARGO_MAYOR = 132;

	/*ERROR AL GENERAR IDENTIFICADOR POR VENCIMIENTO Y CUENTA BANCARIA*/
	public static final long ERROR_GENERAR_IDENTIFICADOR_VENC_CUENTA_BANCARIA = 133;

	/*ERROR EN CONSULTA CONCEPTOS PRESTAMO POR CATEGORIA*/
	public static final long ERR_CONSULTA_CONCEPTOS_PREST_X_CATEGORIA = 134;

	/*ERROR AL ACTIVAR REFINANCIACION*/
	public static final long ERR_ACTIVAR_REFINANCIACION = 135;

	/*ERROR AL DAR DE BAJA IDENTIFICADOR DE PRESTAMO*/
	public static final long ERROR_BAJA_IDENTIFICADOR_PRESTAMO = 136;

	/*LA BUSQUEDA NO REGRESO RESULTADOS*/
	public static final long BUSQUEDA_SIN_RESULTADOS = 137;

	/*ERROR AL MODIFICAR EL IDENTIFICADOR DE PRESTAMO*/
	public static final long ERROR_MODIFICAR_IDENTIFICADOR_PRESTAMO = 138;

	/*ERROR AL ACTIVAR REPROGRAMACION*/
	public static final long ERR_ACTIVAR_REPROGRAMACION = 139;

	/*ERROR EN LISTADO PRODUCTO*/
	public static final long ERR_LISTADO_PRODUCTO = 140;

	/*ERROR EN LISTADO FRECUENCIA*/
	public static final long ERR_LISTADO_FRECUENCIA = 141;

	/*ERROR EN IDENTIFICADOR POR VENCIMIENTO*/
	public static final long ERROR_IDENTIFICADOR_VENCIMIENTO = 142;

	/*ERROR AL GENERAR IDENTIFICADOR POR VENCIMIENTO*/
	public static final long ERROR_GENERAR_IDENTIFICADOR_VENCIMIENTO = 143;

	/*Verifique, el campo No. bienes  no es mayor a uno (1)*/
	public static final long NUM_BIENES_UNO = 144;

	/*ERROR AL BUSCAR IDENTIFICADOR DE PRESTAMO*/
	public static final long ERROR_BUSCAR_IDENTIFICADOR_PRESTAMO = 145;

	/*ERROR EN LISTADO SUCURSALES*/
	public static final long ERR_LISTADO_SUCURSALES = 146;

	/*Verifique, el valor de la garantia  es cero.*/
	public static final long GTIA_SIN_VALOR = 147;

	/*ERROR EN CONSULTA MOVIMIENTOS TRANSACCIONES*/
	public static final long ERR_CONSULTA_MOVTOS_TRANSACCIONES = 148;

	/*ERROR EN CONSULTA COMPLETA TITULARES*/
	public static final long ERR_CONSULTA_COMPLETA_TITULARES = 149;

	/*ERROR EN CONSULTA DESEMBOLSOS COMPLETA*/
	public static final long ERR_CONSULTA_DESEMBOLSOS_COMPLETA = 150;

	/*ERROR EN CONSULTA DE TRANSACCION*/
	public static final long ERR_CONSULTA_TRANSACCION = 151;

	/*ERROR EN CONSULTA VINCULADOS COMPLETO*/
	public static final long ERR_CONSULTA_VINCULADOS_COMPLETO = 152;

	/*ERROR EN CONSULTA VINCULADO PODER COMPLETO*/
	public static final long ERR_CONSULTA_VINCULADO_PODER_COMPLETO = 153;

	/*ERROR EN CONSULTA RESUMEN DEUDA*/
	public static final long ERR_CONSULTA_RESUMEN_DEUDA = 154;

	/*ERROR EN CONSULTA TASAS*/
	public static final long ERR_CONSULTA_TASAS = 155;

	/*ERROR EN CONSULTA CRONOGRAMA*/
	public static final long ERR_CONSULTA_CRONOGRAMA = 156;

	/*ERROR AL GENERAR IDENTIFICADOR POR CUENTA BANCARIA*/
	public static final long ERROR_GENERAR_IDENTIFICADOR_CUENTA_BANCARIA = 157;

	/*ERROR EN CONSULTA IDENTIFICADOR UNICO*/
	public static final long ERR_CONSULTA_IDE_UNICO = 158;

	/*ERROR EN CONSULTA IDENTIFICADOR CUENTA BANCARIA*/
	public static final long ERR_CONSULTA_IDE_CTA_BANCARIA = 159;

	/*ERROR EN CONSULTA IDENTIFICADOR VENCIMIENTO*/
	public static final long ERR_CONSULTA_IDE_VENCIMIENTO = 160;

	/*ERROR BUSCAR PRESTAMO*/
	public static final long ERR_BUSCAR_PRESTAMO = 161;

	/*ERROR AL GENERA AUTORIZACION*/
	public static final long ERR_GENERA_AUTORIZACION = 162;

	/*Primero busque y seleccione una dacion en pago*/
	public static final long SIN_DACION_EN_PAGO = 163;

	/*ERROR EN CONSULTA TRANSACCIONES MODULOS*/
	public static final long ERR_CONSULTA_TRAXS_MODULOS = 164;

	/*ERROR EN ANULAR TRANSACCIONES MODULOS*/
	public static final long ERR_ANULAR_TRAX_MOD = 165;

	/*NO_EXISTE_CATALOGO*/
	public static final long NO_EXISTE_CATALOGO = 166;

	/*CAPTURE SECCION PAGOS_CARGOS*/
	public static final long CAPTURE_SECCION_PAGOS_CARGOS = 167;

	/*TIPO DE NOTA NO ES DE ABONO*/
	public static final long TIPO_NOTA_NO_ABONO = 168;

	/*ERROR EN CONSULTA CUOTAS*/
	public static final long ERR_CONSULTA_CUOTAS = 169;

	/*ERROR EN AUTORIZACION CONDONACION COBRO*/
	public static final long ERR_AUT_CONDONACION_COBRO = 170;

	/*ERROR EN CONCEPTOS COMISIONES PRESTAMO*/
	public static final long ERR_CONCEPTOS_COMISIONES_PRESTAMO = 171;

	/*NO SE PERMITE MODIFICAR CONCEPTO*/
	public static final long NO_PERMITE_MODIFICAR_CONCEPTO = 172;

	/*ERROR EN ALTA DE PRESTAMO*/
	public static final long ERR_ALTA_PRESTAMO = 173;

	/*ERROR EN CONSULTA COMPLETA EXPEDIENTE*/
	public static final long ERR_CONSULTA_COMPLETA_EXPEDIENTE = 174;

	/*ERROR_ACTUALIZAR_SOLICITUD*/
	public static final long ERROR_ACTUALIZAR_SOLICITUD = 175;

	/*ERROR AL CONSULTAS INTEGRANTES DE SOLICITUD*/
	public static final long ERR_CONSULTA_INTEGRANTES_SOLICITUD = 176;

	/*LA SOLICITUD NO ES GRUPAL*/
	public static final long SOLICITUD_NO_GPAL = 178;

	/*NO EXISTE SOLICITUD*/
	public static final long NO_EX_SOLICITUD = 179;

	/*NO EXISTE GRUPO*/
	public static final long NO_EX_GPO = 180;

	/*ERROR EN CONSULTA DATOS GRUPO
*/
	public static final long ERR_CONSULTA_DATOS_GPO = 181;

	/*ERROR EN CONSULTA INTEGRANTES DE PRESTAMO*/
	public static final long ERR_CONSULTA_INTEGRANTES_PRESTAMO = 182;

	/*ERROR EN LISTADO INTEGRANTES DE PRESTAMO*/
	public static final long ERR_LISTADO_INTEGRANTES_PRESTAMO = 183;

	/*ERROR_OBTENER_EJB*/
	public static final long ERROR_OBTENER_EJB = 184;

	/*ERROR EN CONSULTA SOLICITUD GRUPAL*/
	public static final long ERR_CONSULTA_SOLICITUD_GPAL = 185;

	/*SELECCIONE EL TIPO GARANTIA*/
	public static final long SIN_TIPO_GARANTIA = 186;

	/*ERROR EN SIMULACION PREPAGO EXTRAORDINARIO*/
	public static final long ERR_SIMULACION_PREPAGO_EXTRA = 187;

	/*ERROR EN CONSULTA CRONOGRAMA REAL ESTADO CUOTA*/
	public static final long ERR_CONSULTA_CRONOGRAMA_REAL_EDO_CUOTA = 188;

	/*ERR_MOFICAR_PRESTAMO*/
	public static final long ERR_MOFICAR_PRESTAMO = 189;
	
	/*ERROR EN WS APLICA NOTA*/
	public static final long ERROR_EN_WS_APLICA_NOTA = 190;

	/*ERROR EN WS MODIFICA_DEUDA*/
	public static final long ERROR_EN_WS_MODIFICA_DEUDA = 191;

	/*EL PRODUCTO NO PERMITE DEPOSITOS ANTICIPADOS*/
	public static final long ERROR_DEP_ANTICIPADO_CAM_CON = 192;

	/*ERROR EN WS PRESTAMO CAMBIO CONDICION*/
	public static final long ERROR_WS_CAMBIO_CONDICION = 193;

	/*ERROR AL BUSCAR PRESTAMO SOLICITAR SUBSIDIO*/
	public static final long ERR_BUSCAR_PRESTAMO_SOL_SUBSIDIO = 194;

	/*ERROR AL PROCESAR LA SOLICITUD DE SUBSIDIO*/
	public static final long ERR_PROCESAR_SOLICITAR_SUBSIDIO = 195;

	/*ERROR AL CANCELAR SUBSIDIO CONAVI*/
	public static final long ERR_CANCELAR_SUBSIDIO = 196;

	/*EL PRESTAMO NO ESTA ACTIVO*/
	public static final long PRESTAMO_NO_ACTIVO = 197;

	/*PRESTAMO ACTIVADO*/
	public static final long PRESTAMO_ACTIVADO = 198;

	/*ERROR AL ACTIVAR PRESTAMO WS*/
	public static final long ERR_ACTIVAR_PRESTAMO_WS = 199;

	/*ERROR AL OBTENER EL LISTADO DE MEDIOS DE PAGO POR MODULO Y OPERACION*/
	public static final long ERROR_LISTADO_MEDIO_PAGO_CORE_OPERACION = 200;
	
	/*ERROR INESERADO AL PROCESAR EL MEDIO PAGO DEL DSEMBOLSO*/
	public static final long EI_PROCESAR_DESEMBOLSO_MEDIO_PAGO = 201;

	/*FALTA INFORMACION DE LA EMPRESA ASIGANDA AL SISTEMA CONTABLE*/
	public static final long FALTA_INF_EMPRESA_BACKOFFICE = 202;

	/*Regla %s*/
	public static final long REGLA_NEGOCIO = 203;
	
	/*EXISTEN MEDIOS DE PAGO PARA EL DESEMBOLSO PROGRAMADO*/
	public static final long EX_MEDIO_PAGO_DES_PROGRAMADO = 204;

	/*ERROR INESPERDO AL DAR DE BAJA DESEMBOLSO PROGRAMADO*/
	public static final long EI_BAJA_DESEMBOLSO_PROGRAMADO = 205;

	/*NO EXISTE CONFIGURACION DE CUENTA PARA DESEMBOLSOS*/
	public static final long NO_EX_CONF_CTA_DESEMBOLSO = 206;

	/*ERROR AL REALIZAR TRANSACCION BANCARIA*/
	public static final long ERROR_TRANSACCION_BANCARIA = 207;
	
	/*ERROR INESPERADO EN CONSULTA DATOS GARANTIA*/
	public static final long EI_CONSULTA_DATOS_GARANTIA = 208;

	/*ERROR INESPERADO EN CONSULTA DATOS GARANTIA*/
	public static final long EI_GUARDAR_DATOS_GARANTIA = 209;

	/*ERROR INESPERADO EN INSERTA CONCEPTO DATOS GARANTIA*/
	public static final long EI_INSERTA_CONCEPTO_DATOS_GARANTIA = 210;
	
	/*ERROR AL OBTENER CUOTA ESTIMADA*/
	public static final long ERROR_OBTENER_CUOTA_ESTIMADA = 211;

	/*ERROR AL OBTENER MONTO FINANCIADO*/
	public static final long ERROR_OBTENER_MONTO_FINANCIADO = 212;

	/*ERROR EN SIMULADOR PRESTAMO*/
	public static final long ERROR_WS_SIMULADOR_PRESTAMO = 213;
	
	/*ERROR EN EL WS CONSULTA COMPLETA IDENTIFICADORES*/
	public static final long ERR_CONSULTA_COMPLETA_IDENTIFICADORES = 215;

	/*DISTRIBUCION INCORRECTA NO DEBE COMBINAR CUOTAS ADICIONALES POR MONTO Y NUMERO DE CUOTAS*/
	public static final long DIST_INCORRECTA_CUOTAS_ADICIONALES = 216;
	
	/*CAPTURE MONTO APLICAR*/
	public static final long CAPTURE_MONTO_APLICAR = 217;

	/*ERROR AL APLICAR LIQUIDACION SALDO PENDIENTE MENOR QUE EL MONTO A PAGAR*/
	public static final long PL_LIQ_SALDO_CTA_MENOR_MONTO = 218;

	/*ERROR AL APLICAR PREPAGO EXTRAORDINARIO SALDO PENDIENTE MENOR QUE EL MONTO A PAGAR*/
	public static final long PL_PPEC_SALDO_CTA_MENOR_MONTO = 219;
	
	/*ERROR INESPERADO EN EL WS NEGOCIACION PRESTAMO*/
	public static final long EI_WS_NEGOCIACION_PRESTAMO  = 221;
	
	/*MONTO A PAGAR POR CONCEPTO MAYOR AL MONTO ADEDUADO*/
	public static final long NEG_PRE_PAGAR_MAY_DEUDA = 222;
	
	/*Verifique monto mayor a tabla de pagos*/
	public static final long ERR_VER_MONTO_MAYOR_TABLA_PAGOS = 223;
	
	/*LA CUENTA DE AHORRO CAPTURADA EN LA DESCRIPCION NO EXISTE*/
	public static final long CTA_AHORRO_NOEXISTE = 224;

	/*EL PROPIETARIO DE LA GARANTIA NO ES LA TITULAR DE LA CUENTA DE AHORRO*/
	public static final long PERSONA_PROPIETARIA_GTIA_NO_CTA_AHORRO = 225;

	/*ACTUALICE EL NUMERO DE SOLICITANTE DEL SISTEMA DE CARTERA EN LA PERSONA TITULAR DE LA GARANTIA*/
	public static final long ACTUALICE_NUM_SOL = 226;

	/*ERROR EN EL WEBSERVICE GARANTIAS REDISPOSICION*/
	public static final long EI_WS_GARANTIAS_REDISPOSICION = 227;
	
	/*ERROR EN IDENTIFICADOR POR CUENTA BANCARIA*/
	public static final long ERROR_IDENTIFICADOR_CUENTA_BANCARIA = 427;
	
	

	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		
		descError.put(SELECCIONE_CLIENTE,"Seleccione cliente");
		descError.put(SELECCIONE_SOLICITUD,"Seleccione solicitud");
		descError.put(CAPTURE_CAMPOS_REQUERIDOS,"CAMPO %s REQUERIDO");
		descError.put(CAPTURE_CUOTAS_DOBLES,"Capture informacion de Cuotas dobles");
		descError.put(CAPTURE_RENGLONES_INCOMPLETOS,"Capture renglones incompletos");
		descError.put(CAPTURE_ANIO,"Capture anio");
		descError.put(CALCULE_MONTO,"Calcule monto");
		descError.put(CAPTURE_MEDIO_PAGO,"Capture medio de pago");
		descError.put(PRESTAMO_VALIDADO,"Prestamo validado");
		descError.put(GENERE_CRONOGRAMA,"Genere cronograma");
		descError.put(VERIFIQUE_TIPO_SUBTIPO_GTIA,"Verifique tipo y subtipo de garantia");
		descError.put(COBERTURA_GTIAS_INCORRECTO,"Porcentaje de cobertura de garantias incorrecto");
		descError.put(TIPO_COBERTURA_GTIAS_INCORRECTO,"Tipo de cobertura en garantias incorrecto");
		descError.put(CONDICION_GTIA_INCORRECTA,"Condicion de las garantias incorrecta");
		descError.put(ELIMINE_GARANTIAS,"Elimine garantias no se requieren");
		descError.put(VERIFIQUE_DEPOSITOS_GTIA_LIQ,"Verifique depositos de garantia liquida");
		descError.put(CONCEPTO_DEP_ANT_NO_EN_PRODUCTO,"Concepto de deposito anticipado no configurado para el producto");
		descError.put(GENERE_CAPTURE_IDENTIFICADOR_CTA,"Genere o capture identificadores para cuenta");
		descError.put(SELECCIONE_DOCUMENTO,"Seleccione documento");
		descError.put(FECHA_MENOR_ACTUAL,"Fecha menor a la actual");
		descError.put(PROGRAME_DESEMBOLSO,"Programe desembolso(s)");
		descError.put(CAPTURE_IMPORTE,"Capture importe");
		descError.put(IMPORTE_EXCEDE_MTO_PROGRAMADO,"El importe excede del monto programado");
		descError.put(SELECCIONE_SOLICITUD_CAMBIO_CONDICIONES,"SELECCIONE SOLICITUD DE CAMBIO DE CONDICIONES");
		descError.put(NO_EXISTE_MONTO,"No existe monto a pagar");
		descError.put(VERIFIQUE_MTO_PAGAR,"Verifique monto a pagar");
		descError.put(VERIFIQUE_COLUMNA_TOTAL,"Verifique columna total");
		descError.put(CAPTURE_TIPO_DISTRIBUCION,"Capture tipo de distribucion");
		descError.put(VERIFIQUE_PORCENTAJE_GARANTIAS,"Verifique porcentaje de garantias");
		descError.put(CAMBIO_CONDICIONES_ACTIVAS,"CAMBIO DE CONDICIONES ACTIVAS");
		descError.put(SELECCIONE_SOLICITUD_REPROGRAMACION,"Seleccione solicitud de reprogramacion");
		descError.put(REPROGRAMACION_ACTIVA,"Reprogramacion activa");
		descError.put(NO_HA_REALIZADO_COMPROMISO_PAGO,"No se ha realizado compromiso de pago");
		descError.put(SELECCIONE_INFO_PREVIA,"Seleccione informacion previa");
		descError.put(CAPTURE_FRECUENCIA_Y_CUOTAS,"Capture frecuencia pago y numero de cuotas");
		descError.put(CAPTURE_MONTO_SOL_Y_NO_DESEMBOLSO,"Capture monto solicitado y numero de desembolsos");
		descError.put(CAPTURE_DESEMBOLSOS,"Capture desembolsos");
		descError.put(VERIFIQUE_INF_DESEMBOLSOS,"Verifique informacion de desembolsos");
		descError.put(VERIFIQUE_FECHAS_DESEMBOLSO,"Verifique fechas de desembolsos");
		descError.put(SUMA_TOTAL_DESEMBOLSO_DIFERENTE_MONTO_SOL,"La suma total de desembolsos no coincide con el monto solicitado");
		descError.put(FECHA_DESEMBOLSO_MENOR_ACUTUAL,"Verifique fecha de desembolso menor a la actual");
		descError.put(EXCEDE_MAX_DESEMBOLSO,"Excede el maximo de desembolsos del producto");
		descError.put(DIAS_PAGO_INCORRECTOS,"Dias de pago incorrectos respecto a la configuracion del producto");
		descError.put(MONTO_DESEMBOLSAR_MAYOR_SOLICITADO,"El Monto a desembolsar debe ser menor al Monto solicitado");
		descError.put(FECHA_MAYOR,"Verifique, fecha mayor al dia actual.");
		descError.put(VERIFIQUE_DIAS_PAGO,"Verifique dias de pago");
		descError.put(MODALIDAD_NO_PERMITE_CUOTAS_DOBLES,"Modalidad no permite cuotas dobles");
		descError.put(MONTO_OPERACION_DIFERENTE_SUMA_TOTAL,"Verifique total no coincide con importe de operacion");
		descError.put(VENC_Y_CUENTA_YA_REGISTRARON,"El vencimiento y cuenta ya se registraron");
		descError.put(_LONGITUD_INCORRECTA,"Longitud Incorrecta");
		descError.put(IDENTIFICADOR_NO_CUMPLE_FORMATO_ESTABLECIDO,"El identificador no cumple con el formato establecido");
		descError.put(REGISTRO_DADO_DE_BAJA,"Registro dado de baja");
		descError.put(DAR_BAJA_REGISTRO,"Desea dar de baja el registro");
		descError.put(CUENTA_YA_SE_REGISTRO,"La cuenta ya se registro");
		descError.put(VENC_YA_SE_REGISTRO,"El vencimiento ya se registro");
		descError.put(ERROR_INESPERADO,"ERROR INESPERADO %s");
		descError.put(SIN_CRITERIO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de busqueda");
		descError.put(ALMENOS_DOS_CARACTERES,"Verifique, debe capturar un valor de busqueda de por lo menos 2 caracteres");
		descError.put(SOLO_NATURAL,"Primero busque y seleccione una persona natural");
		descError.put(SIN_PRESTAMO,"Verifique, primero seleccione un prestamo.");
		descError.put(PORCENTAJE_ERROR,"Verifique, el porcentaje debe estar en el rango de 1-100");
		descError.put(FECHAS_INVERTIDAS,"Verifique la secuencia de las fechas capturadas estan invertidas");
		descError.put(SIN_GARANTIA,"Primero busque y seleccione una garantia");
		descError.put(sin_importes,"Verifique, capture importes");
		descError.put(APLICAR_MEDIO_PAGO,"DESEA APLICAR EL MEDIO DE PAGO?");
		descError.put(VERIFIQUE_MTO,"VERIFIQUE MONTO");
		descError.put(MTO_INCORRECTO,"MONTO INCORRECTO");
		descError.put(ERROR_CONSULTA_DATOS_PRESTAMO,"ERROR AL CONSULTAR LOS DATOS DE PRESTAMO");
		descError.put(ERROR_CONSULTA_DESEMBOLSO_MEDIO_PAGO,"ERROR AL CONSULTAR MEDIOS DE PAGO DE DESEMBOLSOS PROGRAMADOS");
		descError.put(ERR_OBTENER_SUMA_IMPORTES,"ERROR AL OBTENER LA SUMA DE IMPORTES");
		descError.put(ERR_CONSULTAR_DESEM_PROGRAMADOS,"ERROR AL CONSULTAR DESEMBOLSOS PROGRAMADO");
		descError.put(ERROR_BUSCAR_CONFIGURACION_IDENTIFICADOR_PRESTAMO,"ERROR AL BUSCAR LA CONFIGURACION DEL IDENTIFICADOR");
		descError.put(ERROR_CONSULTA_TIPO_IDENTIFICADOR,"ERROR AL CONSULTAR EL TIPO DE IDENTIFICADOR");
		descError.put(ERR_VALIDAR_PRESTAMO,"ERROR AL VALIDAR PRESTAMO");
		descError.put(ERROR_CREAR_IDENTIFICADOR_PRESTAMO,"ERROR AL CREAR IDENTIFICADOR DE PRESTAMO");
		descError.put(ERR_CONSULTA_COMPLETO_PRESTAMO,"ERROR EN CONSULTA COMPLETO PRESTAMO");
		descError.put(ERROR_LISTADO_CUENTAS_BANCARIAS,"ERROR AL CONSULTAR LAS CUENTAS BANCARIAS");
		descError.put(ERROR_IDENTIFICADOR_VENCIMIENTO_CTA_BANCARIA,"ERROR EN IDENTIFICADOR POR VENCIMIENTO Y CUENTA BANCARIA");
		descError.put(ERRWS_CONSULTA_GRUPO_PLANTILLA_DOCUMENTOS,"ERROR EN CONSULTA GRUPO PLANTILLA DOCUMENTOS");
		descError.put(ERROR_LISTADO_CRONOGRAMA_CABECERA_PRESTAMO_CUOTA,"ERROR AL OBTENER EL LISTADO DE CRONOGRAMA CABECERA");
		descError.put(ERROR_CONSULTA_IDENT_VENC_CUENTA_BANCARIA,"ERROR AL CONSULTAR IDENTIFICADORES POR VENCIMIENTO Y CUENTA BANCARIA");
		descError.put(ERR_PARAMETROS_PROD_FREC,"ERROR AL CONSULTAR PARAMETROS DE PRODUCTO DE FRECUENCIA");
		descError.put(CAPTURE_TASAS,"CAPTURE TASAS");
		descError.put(IMPORTE_ABONAR_MAYOR,"verfique, importe abonar excede el importe de la operacion");
		descError.put(IMPORTE_CARGO_MAYOR,"verfique, importe cargo excede el importe de la operacion");
		descError.put(TOTAL_ABONAR_MAYOR,"verfique, Total abonar excede el importe de la operacion");
		descError.put(TOTAL_CARGO_MAYOR,"verfique, Total cargo excede el importe de la operacion");
		descError.put(ERROR_GENERAR_IDENTIFICADOR_VENC_CUENTA_BANCARIA,"ERROR AL GENERAR IDENTIFICADOR POR VENCIMIENTO Y CUENTA BANCARIA");
		descError.put(ERR_CONSULTA_CONCEPTOS_PREST_X_CATEGORIA,"ERROR EN CONSULTA CONCEPTOS PRESTAMO POR CATEGORIA");
		descError.put(ERR_ACTIVAR_REFINANCIACION,"ERROR AL ACTIVAR REFINANCIACION");
		descError.put(ERROR_BAJA_IDENTIFICADOR_PRESTAMO,"ERROR AL DAR DE BAJA IDENTIFICADOR DE PRESTAMO");
		descError.put(BUSQUEDA_SIN_RESULTADOS,"LA BUSQUEDA NO REGRESO RESULTADOS");
		descError.put(ERROR_MODIFICAR_IDENTIFICADOR_PRESTAMO,"ERROR AL MODIFICAR EL IDENTIFICADOR DE PRESTAMO");
		descError.put(ERR_ACTIVAR_REPROGRAMACION,"ERROR AL ACTIVAR REPROGRAMACION");
		descError.put(ERR_LISTADO_PRODUCTO,"ERROR EN LISTADO PRODUCTO");
		descError.put(ERR_LISTADO_FRECUENCIA,"ERROR EN LISTADO FRECUENCIA");
		descError.put(ERROR_IDENTIFICADOR_VENCIMIENTO,"ERROR EN IDENTIFICADOR POR VENCIMIENTO");
		descError.put(ERROR_GENERAR_IDENTIFICADOR_VENCIMIENTO,"ERROR AL GENERAR IDENTIFICADOR POR VENCIMIENTO");
		descError.put(NUM_BIENES_UNO,"Verifique, el campo No. bienes  no es mayor a uno (1)");
		descError.put(ERROR_BUSCAR_IDENTIFICADOR_PRESTAMO,"ERROR AL BUSCAR IDENTIFICADOR DE PRESTAMO");
		descError.put(ERR_LISTADO_SUCURSALES,"ERROR EN LISTADO SUCURSALES");
		descError.put(GTIA_SIN_VALOR,"Verifique, el valor de la garantia  es cero.");
		descError.put(ERR_CONSULTA_MOVTOS_TRANSACCIONES,"ERROR EN CONSULTA MOVIMIENTOS TRANSACCIONES");
		descError.put(ERR_CONSULTA_COMPLETA_TITULARES,"ERROR EN CONSULTA COMPLETA TITULARES");
		descError.put(ERR_CONSULTA_DESEMBOLSOS_COMPLETA,"ERROR EN CONSULTA DESEMBOLSOS COMPLETA");
		descError.put(ERR_CONSULTA_TRANSACCION,"ERROR EN CONSULTA DE TRANSACCION");
		descError.put(ERR_CONSULTA_VINCULADOS_COMPLETO,"ERROR EN CONSULTA VINCULADOS COMPLETO");
		descError.put(ERR_CONSULTA_VINCULADO_PODER_COMPLETO,"ERROR EN CONSULTA VINCULADO PODER COMPLETO");
		descError.put(ERR_CONSULTA_RESUMEN_DEUDA,"ERROR EN CONSULTA RESUMEN DEUDA");
		descError.put(ERR_CONSULTA_TASAS,"ERROR EN CONSULTA TASAS");
		descError.put(ERR_CONSULTA_CRONOGRAMA,"ERROR EN CONSULTA CRONOGRAMA");
		descError.put(ERROR_GENERAR_IDENTIFICADOR_CUENTA_BANCARIA,"ERROR AL GENERAR IDENTIFICADOR POR CUENTA BANCARIA");
		descError.put(ERR_CONSULTA_IDE_UNICO,"ERROR EN CONSULTA IDENTIFICADOR UNICO");
		descError.put(ERR_CONSULTA_IDE_CTA_BANCARIA,"ERROR EN CONSULTA IDENTIFICADOR CUENTA BANCARIA");
		descError.put(ERR_CONSULTA_IDE_VENCIMIENTO,"ERROR EN CONSULTA IDENTIFICADOR VENCIMIENTO");
		descError.put(ERR_BUSCAR_PRESTAMO,"ERROR BUSCAR PRESTAMO");
		descError.put(ERR_GENERA_AUTORIZACION,"ERROR AL GENERA AUTORIZACION");
		descError.put(SIN_DACION_EN_PAGO,"Primero busque y seleccione una dacion en pago");
		descError.put(ERR_CONSULTA_TRAXS_MODULOS,"ERROR EN CONSULTA TRANSACCIONES MODULOS");
		descError.put(ERR_ANULAR_TRAX_MOD,"ERROR EN ANULAR TRANSACCIONES MODULOS");
		descError.put(NO_EXISTE_CATALOGO,"NO_EXISTE_CATALOGO");
		descError.put(CAPTURE_SECCION_PAGOS_CARGOS,"CAPTURE SECCION PAGOS_CARGOS");
		descError.put(TIPO_NOTA_NO_ABONO,"TIPO DE NOTA NO ES DE ABONO");
		descError.put(ERR_CONSULTA_CUOTAS,"ERROR EN CONSULTA CUOTAS");
		descError.put(ERR_AUT_CONDONACION_COBRO,"ERROR EN AUTORIZACION CONDONACION COBRO");
		descError.put(ERR_CONCEPTOS_COMISIONES_PRESTAMO,"ERROR EN CONCEPTOS COMISIONES PRESTAMO");
		descError.put(NO_PERMITE_MODIFICAR_CONCEPTO,"NO SE PERMITE MODIFICAR CONCEPTO");
		descError.put(ERR_ALTA_PRESTAMO,"ERROR EN ALTA DE PRESTAMO");
		descError.put(ERR_CONSULTA_COMPLETA_EXPEDIENTE,"ERROR EN CONSULTA COMPLETA EXPEDIENTE");
		descError.put(ERROR_ACTUALIZAR_SOLICITUD,"ERROR_ACTUALIZAR_SOLICITUD");
		descError.put(ERR_CONSULTA_INTEGRANTES_SOLICITUD,"ERROR AL CONSULTAS INTEGRANTES DE SOLICITUD");
		descError.put(SOLICITUD_NO_GPAL,"LA SOLICITUD NO ES GRUPAL");
		descError.put(NO_EX_SOLICITUD,"NO EXISTE SOLICITUD");
		descError.put(NO_EX_GPO,"NO EXISTE GRUPO");
		descError.put(ERR_CONSULTA_DATOS_GPO,"ERROR EN CONSULTA DATOS GRUPO");
		descError.put(ERR_CONSULTA_INTEGRANTES_PRESTAMO,"ERROR EN CONSULTA INTEGRANTES DE PRESTAMO");
		descError.put(ERR_LISTADO_INTEGRANTES_PRESTAMO,"ERROR EN LISTADO INTEGRANTES DE PRESTAMO");
		descError.put(ERROR_OBTENER_EJB,"ERROR_OBTENER_EJB");
		descError.put(ERR_CONSULTA_SOLICITUD_GPAL,"ERROR EN CONSULTA SOLICITUD GRUPAL");
		descError.put(SIN_TIPO_GARANTIA,"SELECCIONE EL TIPO GARANTIA");
		descError.put(ERR_SIMULACION_PREPAGO_EXTRA,"ERROR EN SIMULACION PREPAGO EXTRAORDINARIO");
		descError.put(ERR_CONSULTA_CRONOGRAMA_REAL_EDO_CUOTA,"ERROR EN CONSULTA CRONOGRAMA REAL ESTADO CUOTA");
		descError.put(ERR_MOFICAR_PRESTAMO,"ERR_MOFICAR_PRESTAMO");
		descError.put(ERROR_EN_WS_APLICA_NOTA,"ERROR EN WS APLICA NOTA");
		descError.put(ERROR_EN_WS_MODIFICA_DEUDA,"ERROR EN WS MODIFICA_DEUDA");
		descError.put(ERROR_DEP_ANTICIPADO_CAM_CON,"EL PRODUCTO NO PERMITE DEPOSITOS ANTICIPADOS");
		descError.put(ERROR_WS_CAMBIO_CONDICION,"ERROR EN WS PRESTAMO CAMBIO CONDICION");		
		descError.put(ERR_BUSCAR_PRESTAMO_SOL_SUBSIDIO,"ERROR AL BUSCAR PRESTAMO SOLICITAR SUBSIDIO");
		descError.put(ERR_PROCESAR_SOLICITAR_SUBSIDIO,"ERROR AL PROCESAR LA SOLICITUD DE SUBSIDIO");
		descError.put(ERR_CANCELAR_SUBSIDIO,"ERROR AL CANCELAR SUBSIDIO CONAVI");
		descError.put(PRESTAMO_NO_ACTIVO,"EL PRESTAMO NO ESTA ACTIVO");
		descError.put(PRESTAMO_ACTIVADO,"PRESTAMO ACTIVADO");
		descError.put(ERR_ACTIVAR_PRESTAMO_WS,"ERROR AL ACTIVAR PRESTAMO WS");
		descError.put(ERROR_LISTADO_MEDIO_PAGO_CORE_OPERACION,"ERROR AL OBTENER EL LISTADO DE MEDIOS DE PAGO POR MODULO Y OPERACION");
		descError.put(EI_PROCESAR_DESEMBOLSO_MEDIO_PAGO,"ERROR INESERADO AL PROCESAR EL MEDIO PAGO DEL DSEMBOLSO");
		descError.put(FALTA_INF_EMPRESA_BACKOFFICE,"FALTA INFORMACION DE LA EMPRESA ASIGANDA AL SISTEMA CONTABLE");
		descError.put(REGLA_NEGOCIO,"Regla %s");
		descError.put(EX_MEDIO_PAGO_DES_PROGRAMADO,"EXISTEN MEDIOS DE PAGO PARA EL DESEMBOLSO PROGRAMADO");
		descError.put(EI_BAJA_DESEMBOLSO_PROGRAMADO,"ERROR INESPERDO AL DAR DE BAJA DESEMBOLSO PROGRAMADO");
		descError.put(NO_EX_CONF_CTA_DESEMBOLSO,"NO EXISTE CONFIGURACION DE CUENTA PARA DESEMBOLSOS");
		descError.put(ERROR_TRANSACCION_BANCARIA,"ERROR AL REALIZAR TRANSACCION BANCARIA");
		descError.put(EI_CONSULTA_DATOS_GARANTIA,"ERROR INESPERADO EN CONSULTA DATOS GARANTIA");
		descError.put(EI_GUARDAR_DATOS_GARANTIA,"ERROR INESPERADO EN CONSULTA DATOS GARANTIA");
		descError.put(EI_INSERTA_CONCEPTO_DATOS_GARANTIA,"ERROR INESPERADO EN INSERTA CONCEPTO DATOS GARANTIA");
		descError.put(ERROR_OBTENER_CUOTA_ESTIMADA,"ERROR AL OBTENER CUOTA ESTIMADA");
		descError.put(ERROR_OBTENER_MONTO_FINANCIADO,"ERROR AL OBTENER MONTO FINANCIADO");
		descError.put(ERROR_WS_SIMULADOR_PRESTAMO,"ERROR EN SIMULADOR PRESTAMO");
		descError.put(ERR_CONSULTA_COMPLETA_IDENTIFICADORES,"ERROR EN EL WS CONSULTA COMPLETA IDENTIFICADORES");
		descError.put(DIST_INCORRECTA_CUOTAS_ADICIONALES,"DISTRIBUCION INCORRECTA NO DEBE COMBINAR CUOTAS ADICIONALES POR MONTO Y NUMERO DE CUOTAS");
		descError.put(CAPTURE_MONTO_APLICAR,"CAPTURE MONTO APLICAR");
		descError.put(PL_LIQ_SALDO_CTA_MENOR_MONTO,"ERROR AL APLICAR LIQUIDACION SALDO PENDIENTE MENOR QUE EL MONTO A PAGAR");
		descError.put(PL_PPEC_SALDO_CTA_MENOR_MONTO,"ERROR AL APLICAR PREPAGO EXTRAORDINARIO SALDO PENDIENTE MENOR QUE EL MONTO A PAGAR");
		descError.put(ERROR_IDENTIFICADOR_CUENTA_BANCARIA,"ERROR EN IDENTIFICADOR POR CUENTA BANCARIA");
		descError.put(NEG_PRE_PAGAR_MAY_DEUDA,"Monto a pagar por concepto mayor al monto adeudado");
		descError.put(ERR_VER_MONTO_MAYOR_TABLA_PAGOS,"Verifique monto mayor a tabla de pagos");
		descError.put(PERSONA_PROPIETARIA_GTIA_NO_CTA_AHORRO,"EL PROPIETARIO DE LA GARANTIA NO ES LA TITULAR DE LA CUENTA DE AHORRO");
		descError.put(ACTUALICE_NUM_SOL,"ACTUALICE EL NUMERO DE SOLICITANTE DEL SISTEMA DE CARTERA EN LA PERSONA TITULAR DE LA GARANTIA");
		descError.put(EI_WS_GARANTIAS_REDISPOSICION,"ERROR EN EL WEBSERVICE GARANTIAS REDISPOSICION");
		descError.put(EI_WS_NEGOCIACION_PRESTAMO,"Error inesperado en el ws Negociacion prestamo");
		descError.put(CTA_AHORRO_NOEXISTE,"LA CUENTA DE AHORRO CAPTURADA EN LA DESCRIPCION NO EXISTE");
	}

}

