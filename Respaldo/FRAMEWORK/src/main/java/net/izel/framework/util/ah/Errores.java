package net.izel.framework.util.ah;

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
	
	public static final long  ERROR_VALIDAR_NUEVO_PRODUCTO=13;
	public static final long  ERROR_YA_PRODUCTO_EXITE=14;
	/*ERROR_CREAR_PRODUCTO*/
	public static final long ERROR_CREAR_PRODUCTO = 15;
	
	/*ERROR_VALIDAR_BUSQUEDA_PRODUCTO*/
	public static final long ERROR_VALIDAR_BUSQUEDA_PRODUCTO = 16;
	
	public static final long NO_EXISTE_PRODUCTO = 17;

	/*BUSQUEDA_PRODUCTO_SIN_RESULTADOS*/
	public static final long BUSQUEDA_PRODUCTO_SIN_RESULTADOS = 18;
	public static final long ERROR_BUSCAR_PRODUCTO=19;
	
	public static final long OBTENER_PRODUCTO_SIN_RESULTADOS=20;
	public static final long ERROR_OBTENER_PRODUCTO=21;

	public static final long ERROR_BUSCAR_DATOS_PRODUCTO = 22;

	public static final long SELECCIONE_PRODUCTO = 23;
	
	public static final long ERROR_MODIFICAR_PRODUCTO = 24;

	public static final long ERROR_MODIFICAR_ESTA_INACTIVO = 25;
	
	public static final long ERROR_OBTENER_LISTA_CONDICIONES=26;

	public static final long ERROR_CODIGO_EMPRESA=27;
	
	public static final long NO_EXISTE_CONDICION = 28;	
	
	public static final long ERROR_REQUIRE_CONDICION= 29;
	
	public static final long ERROR_ASIGNAR_CONDICION= 30;	
	
	public static final long ERROR_QUITAR_CONDICION= 31;
	
	public static final long ERROR_VALIDAR_GUARDAR_DEPOSITO_APERTURA= 32;
	public static final long ERROR_GUARDAR_DEPOSITO_APERTURA=33;
	public static final long ERROR_CREAR_DEPOSITO_APERTURA=34;
	public static final long ERROR_ACTUALIZAR_DEPOSITO_APERTURA=35;
	
	public static final long ERROR_DEBE_ASIGNAR_LA_CONDICION=36;
	
	public static final long ERROR_OBTENER_DEPOSITO_APERTURA=37;
	
	public static final long ERROR_MONTO_MINIMO_DEBE_SER_MAYOR_IGUAl_0 = 38;


	public static final long ERROR_OBTENER_MANEJA_PLAZOS=39;
	public static final long ERROR_PLAZO_DIAS_DESDE_DEBE_SER_MAYOR_IGUAl_0=40;
	public static final long ERROR_PLAZO_DIAS_HASTA_DEBE_SER_MAYOR_IGUAl_0=41;	
	public static final long ERROR_PLAZO_DIAS_HASTA_NO_DEBE_SER_MENOR_A_PLAZO_DIAS_DESDE=42;
	public static final long ERROR_VALIDAR_GUARDAR_MANEJA_PLAZOS=43;
	public static final long ERROR_ACTUALIZAR_MANEJA_PLAZOS=44;
	public static final long ERROR_CREAR_MANEJA_PLAZOS=45;
	
	
	public static final long ERROR_OBTENER_AHORRO_PROGRAMADO=46;
	public static final long ERROR_PORCENTAJE_BONI_MENOR_IGUAL_A_CERO=47;
	public static final long ERROR_PORCENTAJE_BONI_TIENE_QUE_ESTAR_VACIO=48;
	public static final long ERROR_VALIDAR_GUARDAR_AHORRO_PROGRAMADO=49;
	public static final long ERROR_ACTUALIZAR_AHORRO_PROGRAMADO=50;
	public static final long ERROR_CREAR_AHORRO_PROGRAMADO=51;

	
	public static final long ERROR_VALIDAR_NUEVO_MONTO_DEPOSITO=53;
	public static final long ERROR_MONTO_MINIMO_DEBE_SER_MAYOR_A_0=52;
	public static final long ERROR_CREAR_MONTO_DEPOSITO=54;
	
	public static final long ERROR_VALIDAR_MODIFICAR_MONTO_DEPOSITO=55;
	public static final long ERROR_MODIFICAR_MONTO_DEPOSITO=56;
	public static final long ERROR_MONTO_DEPOSITO_NO_EXISTE=57;
	public static final long ERROR_CONDICION_ASIGNADA_NO_EXISTE=58;
	
	public static final long ERROR_VALIDAR_BAJA_MONTO_DEPOSITO=59;
	public static final long ERROR_BAJA_MONTO_DEPOSITO=60;
	
	

	public static final long ERROR_SALDO_PROMEDIO_MINIMO_DEBE_SER_MAYOR_A_0=61;
	public static final long ERROR_CREAR_SALDO_PROMEDIO=62;
	public static final long ERROR_VALIDAR_NUEVO_SALDO_PROMEDIO=63;
	
	
	public static final long ERROR_VALIDAR_MODIFICAR_SALDO_PROMEDIO=64;
	public static final long ERROR_SALDO_PROMEDIO_NO_EXISTE=65;
	public static final long ERROR_MODIFICAR_SALDO_PROMEDIO=66;
	
	
	public static final long ERROR_VALIDAR_BAJA_SALDO_PROMEDIO=67;
	public static final long ERROR_BAJA_SALDO_PROMEDIO=68;
	
	
	public static final long ERROR_OBTENER_MONTOS_DEPOSITOS=69;
	public static final long ERROR_OBTENER_MONTO_DEPOSITO=70;
	
	
	public static final long ERROR_OBTENER_SALDOS_PROMEDIOS=71;
	public static final long ERROR_OBTENER_SALDO_PROMEDIO=72;
	
	
	public static final long ERROR_VALIDAR_GUARDAR_CERTIFICADO_DEPOSITO=73;
	public static final long ERROR_CREAR_CERTIFICADO_DEPOSITO=74;
	public static final long ERROR_ACTUALIZAR_CERTIFICADO_DEPOSITO=75;
	
	public static final long ERROR_OBTENER_LISTA_COMISIONES=76;
	
	public static final long NO_EXISTE_COMISION=77;
	public static final long ERROR_APLICAR_COMISION=78;
	public static final long ERROR_QUITAR_COMISION=79;
	
	
	public static final long ERROR_DEBE_APLICAR_LA_COMISION=80;
	public static final long ERROR_VALIDAR_GUARDAR_COMISION_GENERALES=81;
	
	public static final long ERROR_COMISION_APLICADA_NO_EXISTE=82;
	public static final long ERROR_OBTENER_IMPORTES_COMISION=83;
	
	
	public static final long ERROR_IMPORTE_COMISION_NO_EXISTE=84;
	public static final long ERROR_OBTENER_IMPORTE_COMISION=85;
	
	
	public static final long ERROR_SALDO_PROM_DESDE_NO_PUEDE_SER_NEG=86;
	public static final long ERROR_SALDO_PROM_HASTA_NO_PUEDE_SER_NEG=87;
	public static final long ERROR_SALDO_PROM_HASTA_NO_PUEDE_SER_MENOR_A_SALDO_PROM_DESDE=88;
	public static final long ERROR_NUM_OCURREN_DESDE_NO_PUEDE_SER_NEG=89;
	public static final long ERROR_NUM_OCURREN_HASTA_NO_PUEDE_SER_NEG=90;
	public static final long ERROR_NUM_OCURREN_HASTA_NO_PUEDE_SER_MENOR_A_NUM_OCURREM_DESDE=91;
	public static final long ERROR_IMPORTE_PORCENTAJE_NO_PUEDE_SER_NEG=92;
	
	public static final long ERROR_VALIDAR_NUEVO_IMPORTE_COMISION=93;
	public static final long ERROR_CREAR_IMPORTE_COMISION=94;
	public static final long ERROR_ITEM_NO_ENCONTRADO=95;
	
	
	public static final long ERROR_IMPORT_MIN_APLI_DEBE_SER_MAY_0=96;
	public static final long ERROR_IMPORT_MAX_APLI_DEBE_SER_MAY_0=97;
	public static final long ERROR_IMPORT_MAX_APLI_DEBE_SER_MAY_A_IMPORT_MIN=98;
	
	
	public static final long ERROR_VALIDAR_MODIFICAR_IMPORTE_COMISION=99;
	public static final long ERROR_MODIFICAR_IMPORTE_COMISION=100;
	
	
	public static final long ERROR_VALIDAR_BAJA_IMPORTE_COMISION=101;
	public static final long ERROR_BAJA_IMPORTE_COMISION=102;
	
	
	public static final long ERROR_OBTENER_TIPOS_INTERESES=103;
	
	public static final long ERROR_TIPO_INTERES_NO_EXISTE=104;
	public static final long ERROR_OBTENER_TIPO_INTERES=105;
	
	public static final long ERROR_VALIDAR_NUEVO_TIPO_INTERES=106;
	public static final long ERROR_CREAR_TIPO_INTERES=107;
	
	
	public static final long ERROR_VALIDAR_MODIFICAR_TIPO_INTERES=108;
	public static final long ERROR_MODIFICAR_TIPO_INTERES=109;
	
	
	public static final long ERROR_VALIDAR_BAJA_TIPO_INTERES=110;
	public static final long ERROR_BAJA_TIPO_INTERES=111;
	
	
	public static final long ERROR_OBTENER_TARIFAS_INTERES=112;
	
	
	public static final long ERROR_TARIFA_INTERES_NO_EXISTE=113;
	public static final long ERROR_OBTENER_TARIFA_INTERES=114;
	
	
	public static final long ERROR_MONTO_MINIMO_NO_PUEDE_SER_NEG=115;
	public static final long ERROR_MONTO_MAXIMO_NO_PUEDE_SER_NEG=116;
	public static final long ERROR_MONTO_MAXIMO_NO_PUEDE_SER_MENOR_A_MONTO_MINIMO=117;
	public static final long ERROR_PLAZO_MINIMO_NO_PUEDE_SER_NEG=118;
	public static final long ERROR_PLAZO_MAXIMO_NO_PUEDE_SER_NEG=119;
	public static final long ERROR_PLAZO_MAXIMO_NO_PUEDE_SER_MENOR_A_PLAZO_MINIMO=120;
	
	
	public static final long ERROR_VALIDAR_NUEVA_TARIFA_INTERES=121;
	public static final long ERROR_CREAR_TARIFA_INTERES=122;
	
	
	public static final long ERROR_VALIDAR_MODIFICAR_TARIFA_INTERES=123;
	public static final long ERROR_MODIFICAR_TARIFA_INTERES=124;
	
	public static final long ERROR_OBTENER_DIAS_INAMOVILIDAD=125;
	
	public static final long ERROR_DIAS_INAMOVILIDAD_DEBE_SER_MAYOR_IGUAl_0=126;
	public static final long ERROR_VALIDAR_GUARDAR_DIAS_INAMOBILIDAD=126;
	public static final long ERROR_ACTUALIZAR_DIAS_INAMOVILIDAD=127;
	public static final long ERROR_CREAR_DIAS_INAMOVILIDAD=128;
	
	
	
	public static final long ERROR_OBTENER_RENOVACION_PLAZO=129;
	
	
	public static final long INGRESE_NUEVO_PLAZO=130;
	public static final long ERROR_NUEVO_PLAZO_MENOR_A_0=131;
	public static final long INGRESE_DIAS_ESPERA=132;
	public static final long ERROR_DIAS_ESPERA_MENOR_A_0=133;
	public static final long ERROR_SELECCIONE_TASA_INTERES_DIAS_ESPERA=134;
	public static final long ERROR_SELECCIONE_GANA_INT_VENCI=135;
	
	
	public static final long ERROR_VALIDAR_GUARDAR_RENOVACION_PLAZO=136;
	public static final long ERROR_ACTUALIZAR_RENOVACION_PLAZO=137;
	public static final long ERROR_CREAR_RENOVACION_PLAZO=138;
	
	
	public static final long ERROR_DIAS_NO_PENA_MENOR_IGUAL_A_CERO=139;
	
	public static final long ERROR_OBTENER_CANCELACION_PLAZO=140;
	
	
	public static final long ERROR_VALIDAR_GUARDAR_CANELACION_PLAZO=141;
	public static final long ERROR_ACTUALIZAR_CANCELACION_PLAZO=142;
	public static final long ERROR_CREAR_CANCELACION_PLAZO=143;
	
	
	public static final long ERROR_DIAS_SIN_MOV_DEBE_SER_MAYOR_IGUAl_0=144;
	public static final long ERROR_SALDO_DEBE_SER_MAYOR_IGUAl_0=145;
	
	
	public static final long ERROR_VALIDAR_GUARDAR_PASA_INACTIVA=146;
	public static final long ERROR_ACTUALIZAR_PASA_INACTIVA=147;
	public static final long ERROR_CREAR_PASA_INACTIVA=148;
	
	public static final long ERROR_PRODUCTO_NO_EXISTE=149;
	public static final long ERROR_VALIDAR_ACTIVAR_PRODUCTO=150;
	public static final long ERROR_ACTIVAR_PRODUCTO=151;
	public static final long ERROR_VALIDAR_BAJA_PRODUCTO=152;
	public static final long ERROR_BAJA_PRODUCTO=153;
	
	
	public static final long ERROR_GUARDAR_AHORRO_PROGRAMADO=154;
	
	public static final long ERROR_GUARDAR_CANCELACION_PLAZO=155;
	
	public static final long ERROR_GUARDAR_CERTIFICADO_DEPOSITO=156;
	
	public static final long ERROR_GUARDAR_COMISION_GENERALES=157;
	
	public static final long ERROR_ACTUALIZAR_COMISION_GENERALES=158;
	public static final long ERROR_CREAR_COMISION_GENERALES=159;
	
	public static final long ERROR_GUARDAR_DIAS_INAMOVILIDAD=160;
	
	public static final long ERROR_GUARDAR_PASA_INACTIVA=161;
	
	public static final long ERROR_GUARDAR_RENOVACION_PLAZO=162;
	
	public static final long ERROR_NUEVO_IMPORTE_COMISION=163;
	
	public static final long ERROR_NUEVO_MONTO_DEPOSITO=164;
	
	public static final long ERROR_NUEVO_SALDO_PROMEDIO=165;

	
	public static final long ERROR_NUEVA_TARIFA_INTERES=166;
	public static final long ERROR_NUEVO_TIPO_INTERES=167;
	public static final long ERROR_OBTENER_COMISION_GENERALES=169;
	
	
	public static final long ERROR_OBTENER_DIAS_INAMOBILIDAD=170;
	
	public static final long ERROR_OBTENER_PASA_INACTIVA=171;
	
	public static final long ERROR_OBTENER_CERTIFICADO_DEPOSITO=172;
	
	public static final long ERROR_BAJA_MONTOS_DEPOSITOS=173;
	public static final long ERROR_BAJA_SALDOS_PROMEDIOS=174;
	public static final long ERROR_BAJA_IMPORTES_COMISION=175;
	public static final long ERROR_BAJA_TIPOS_INTERESES=176;
	public static final long ERROR_BAJA_TARIFAS_INTERESES=177;
	
	
	public static final long ERROR_BAJA_TARIFAS_INTERES=178;
	public static final long ERROR_BAJA_TIPOS_INTERES=179;
	
	public static final long FECHA_MENOR_ACTUAL=180;
 
	
	public static final long ERROR_VALIDAR_BAJA_TARIFA_INTERES=181;
	public static final long ERROR_BAJA_TARIFA_INTERES=182;

	
	public static final long  ERROR_ACTUALIZAR_ESTADO_CONDICION=183;
	public static final long  ERROR_ACTUALIZAR_ESTADO_COMISION=184;
	public static final long  ERROR_VERIF_SI_HAY_TIPOS_INTERES=185;
	public static final long  ERROR_OBTENER_MATRIZ_CONDICION_ID=186;
	
	
	public static final long  ERROR_VALIDAR_ASIGNAR_CONDICION=187;
	
	public static final long  ERROR_OBTENER_CODIGOS_PRINCIPAL=188;
	public static final long  ERROR_OBTENER_CODIGOS_DEPENDIENTES=189;
	
	public static final long  ERROR_DEPENDENCIA_CONDICION=190;
	
	public static final long  ERROR_OBTENER_CODIGOS_EXCLUYENTE=191;
	public static final long  ERROR_EXCLUYENTE_CONDICION=192;
	
	public static final long  ERROR_GUARDAR_MANEJA_PLAZOS=193;
	
	public static final long  ERROR_CONDICIONES_SIN_REGISTRAR=194;
	
	/*ERROR_PROCESAR_XML*/
	public static final long ERROR_PROCESAR_XML = 510;
	
	
	/******** cuentas ***********/

	/*Error al generar cuenta*/
	public static final long ERROR_GENERA_CUENTA_GEN = 9937;

	/*Error al generar el apertura al cuenta*/
	public static final long ERROR_APERTURACUENTA_GEN = 9938;

	/*Error al crear el cuenta en la base de datos*/
	public static final long ERROR_CREACUENTA_GEN = 9939;

	/*Error al generar en memoria maestro de cuenta*/
	public static final long ERROR_GENARRAY_MAESTROCNTA_GEN = 9941;

	/*Error al generar datos en memoria para maestro de cuentas*/
	public static final long ERROR_GENARRAY_DATOSCNTA_GEN = 9942;

	/*Error al generar resumen de cuenta en memoria para cuenta*/
	public static final long ERROR_GENARRAY_RESUMENCNTA_GEN = 9943;

	/*Error al generar tasas en memoria para cuenta*/
	public static final long ERROR_GENARRAY_TASASCNTA_GEN = 9944;

	/*Error al generar procesos ejecutados en memoria para cuenta*/
	public static final long ERROR_GENARRAY_PROCEJECNTA_GEN = 9947;

	/*Error al obtener rango de tarifa*/
	public static final long ERROR_OBTENER_RANGETARIFA_GEN = 9948;

	/*Error al obtener tarifa*/
	public static final long ERROR_OBTENER_TARIFA_GEN = 9949;

	/*Error al generar la secuencia del maestro de cuentas*/
	public static final long ERROR_MAESTROCNTA_SECUEN = 9950;

	/*Error al generar la secuencia de datos del cuenta*/
	public static final long ERROR_DATOSCNTA_SECUEN = 9951;

	/*Error al generar la secuencia de resumen de cuenta del cuenta*/
	public static final long ERROR_RESUMENCNTA_SECUEN = 9952;

	/*Error al generar la secuencia de tasas del cuentas*/
	public static final long ERROR_TASASCNTA_SECUEN = 9953;

	/*Error al generar la secuencia de procesos ejecutados del cuenta*/
	public static final long ERROR_PROCEJECNTA_SECUEN = 9956;

	/*Error de llamada en funcion APERTURA CUENTA*/
	public static final long ERROR_APERTURACUENTA_CALLS = 9959;

	/*Error de llamada en funcion GENERA CUENTA*/
	public static final long ERROR_GENERACUENTA_CALLS = 9960;

	/*Error de llamada en funcion CREA CUENTA*/
	public static final long ERROR_CREACUENTA_CALLS = 9961;

	/*Error de llamada en funcion OBTENER TASAS DE INTERES*/
	public static final long ERROR_OBTIENETASAINTERESF_CALLS = 9963;

	/*Error de llamada en funcion OBTENER TARIFAS*/
	public static final long ERROR_OBTIENETASASALL_CALLS = 9964;

	/*Error de llamada en funcion OBTENER TASA INTERES*/
	public static final long ERROR_OBTIENETASAINTERES_CALLS = 9966;

	/*Error de llamada en funcion OBTENER TASA AGREGADA*/
	public static final long ERROR_OBTIENETASAAGREGADA_CALLS = 9967;

	/*Se encontro mas de un concepto INTERES CUOTA*/
	public static final long ERROR_OBTENER_TASAINT_MANY = 9975;

	/*No se encontro el concepto tarifa correspondiente*/
	public static final long ERROR_CONCEPTO_TARIFA = 9976;

	/*Hay mas de un rango de tarifa definida para el concepto*/
	public static final long ERROR_OBTENER_RANGETARIFA_MANY = 9977;

	/*No hay rango de tarifa definida para el concepto*/
	public static final long ERROR_OBTENER_RANGETARIFA_NULL = 9978;

	/*Error de llamada en funcion GENERAR CUENTA, fecha primer vencimiento calculada esta vacia*/
	public static final long ERROR_GENERACUENTA_PRIMERVCTO_VACIO = 9979;

	/*Error de llamada en funcion GENERAR CUENTA, fecha vencimiento del cuenta calculada esta vacia*/
	public static final long ERROR_GENERACUENTA_VCTOCUENTA_VACIO = 9980;

	/*Error de llamada en funcion GENERAR CUENTA, factor de amortizacion del cuenta calculado esta vacio*/
	public static final long ERROR_GENERACUENTA_FACTAMORTCUENTA_VACIO = 9981;

	/*Error de llamada en funcion GENERAR CUENTA, actualizando monto financiado*/
	public static final long ERROR_GENERACUENTA_ACT_MTOFIN = 9982;

	/*Error de llamada en funcion CREA CUENTA, no pudo crear el cuenta en la base de datos*/
	public static final long ERROR_CREACUENTA_MAESTRO = 9985;

	/*Error de llamada en funcion CREA CUENTA, no pudo crear los datos del cuenta en la base de datos*/
	public static final long ERROR_CREACUENTA_DATOS = 9986;

	/*Error de llamada en funcion CREA CUENTA, no pudo crear las tasas del cuenta en la base de datos*/
	public static final long ERROR_CREACUENTA_TASAS = 9987;

	/*Error de llamada en funcion CREA CUENTA, no pudo crear el registro del proceso ejecutado del cuenta en la base de datos*/
	public static final long ERROR_CREACUENTA_PROCES = 9992;

	/*Error de llamada en funcion OBTENER TASA INTERES, no pudo convertir tasa tarifario a tasa aplicacion*/
	public static final long ERROR_OBTENER_TASAINT_CONVERT = 9993;

	/*Error de llamada en funcion OBTENER TASA INTERES, no pudo calcular tasa periodo*/
	public static final long ERROR_OBTENER_TASAINT_PERIOD = 9994;

	/*Error de llamada en funcion OBTENER TASA INTERES, no pudo calcular tasa diaria*/
	public static final long ERROR_OBTENER_TASAINT_DIARIA = 9995;

	/*Error de llamada en funcion OBTENER TASA AGREGADA, no pudo convertir tasa tarifario a tasa aplicacion*/
	public static final long ERROR_OBTENER_TASAADD_CONVERT = 9996;

	/*Error de llamada en funcion OBTENER TASA AGREGADA, no pudo calcular tasa periodo*/
	public static final long ERROR_OBTENER_TASAADD_PERIOD = 9997;

	/*Error de llamada en funcion OBTENER TASA AGREGADA, no pudo calcular tasa diaria*/
	public static final long ERROR_OBTENER_TASAADD_DIARIA = 9998;

	/*Error de llamada en funcion OBTENER TASA AGREGADA, no pudo calcular tasa agregada*/
	public static final long ERROR_OBTENER_TASAADD_CALCUL = 9999;

	/*No existe concepto SALDO CAPITAL*/
	public static final long ERROR_CONCEPTO_SALDO_CAPITAL = 10000;

	/*No existe concepto TOTAL CUOTA*/
	public static final long ERROR_CONCEPTO_TOTAL_CUOTA = 10001;

	/*No se encontro el producto*/
	public static final long ERROR_OBTENER_DATOS_PRODUCTO = 10003;

	/*No se encontro la frecuencia de pago para las cuotas*/
	public static final long ERROR_OBTENER_DATOS_FRECUENCIA = 10004;

	/*No se pudo convertir datos entre arreglo y lista*/
	public static final long ERROR_GENERAR_ARRAY_LIST = 10005;

	/*Error al generar la secuencia de cabcera de transaccion del cuenta*/
	public static final long ERROR_TRANSACCAB_SECUEN = 10006;

	/*Error al generar la secuencia de detalle de transaccion del cuenta*/
	public static final long ERROR_TRANSACDET_SECUEN = 10007;

	/*Error al generar la secuencia de detalle cuotas de transaccion del cuenta*/
	public static final long ERROR_TRANSACCUO_SECUEN = 10008;

	/*Error al generar transaccion cabecera en memoria para cuenta*/
	public static final long ERROR_GENARRAY_TRXCAB_GEN = 10009;

	/*Error al generar transaccion detalle en memoria para cuenta*/
	public static final long ERROR_GENARRAY_TRXDET_GEN = 10010;

	/*Error al generar transaccion detalle cuotas en memoria para cuenta*/
	public static final long ERROR_GENARRAY_TRXCUO_GEN = 10011;

	/*Error al generar transaccion*/
	public static final long ERROR_CREA_TRX_GEN = 10012;

	/*Error de llamada en funcion CREA TRANSACCION*/
	public static final long ERROR_CREA_TRX_CALL = 10013;

	/*Error de llamada en funcion CREA TRANSACCION, no pudo crear cabecera de transaccion del cuenta en la base de datos*/
	public static final long ERROR_CREA_TRX_CAB = 10014;

	/*Error de llamada en funcion CREA TRANSACCION, no pudo crear detalle de transaccion del cuenta en la base de datos*/
	public static final long ERROR_CREA_TRX_DET = 10015;

	/*Error de llamada en funcion CREA TRANSACCION, no pudo crear detalle cuotas de transaccion del cuenta en la base de datos*/
	public static final long ERROR_CREA_TRX_CUO = 10016;

	/*No existe valor para TIPO ESTATUS ALTA*/
	public static final long ERROR_VALOR_TIPO_ESTATUS_ALTA = 10017;

	/*No existe valor para CONDICION cuenta NUEVO*/
	public static final long ERROR_VALOR_CONDICION_CUENTA_NUEVO = 10019;

	/*No existe valor para ESTADO EN PROCESO D*/
	public static final long ERROR_VALOR_ESTADO_PROCESO_D = 10020;

	/*No existe codigo en tabla estado cuenta*/
	public static final long ERROR_CATA_ESTADO_CUENTA = 10022;

	/*No existe codigo en tabla estado contable*/
	public static final long ERROR_CATA_ESTADO_CONTABLE = 10023;

	/*No existe valor para ESTADO TRANSACCION ACTIVA*/
	public static final long ERROR_VALOR_ESTADO_TRANSACCION_ACTIVA = 10024;

	/*Fecha valor no puede ser vacia*/
	public static final long ERROR_VALIDAR_FECHAVALOR1 = 10025;

	/*Fecha valor no puede ser mayor a la fecha actual*/
	public static final long ERROR_VALIDAR_FECHAVALOR2 = 10026;

	/*Producto no esta activo*/
	public static final long ERROR_VALIDAR_PRODUCTOACTIVO = 10027;

	/*Frecuencia de pago no puede ser vacia*/
	public static final long ERROR_VALIDAR_FRECUENCIA1 = 10028;

	/*Frecuencia de pago no permitida para el producto*/
	public static final long ERROR_VALIDAR_FRECUENCIA2 = 10029;

	/*Monto a financiar tiene que ser mayor a cero*/
	public static final long ERROR_VALIDAR_MONTOFIN1 = 10030;

	/*Monto a financiar no permitido para el producto*/
	public static final long ERROR_VALIDAR_MONTOFIN2 = 10031;

	/*Plazo tiene que ser mayor a cero*/
	public static final long ERROR_VALIDAR_PLAZO1 = 10032;

	/*No se ha ingresado monto solicitado*/
	public static final long ERROR_VALIDAR_VALORES1 = 10033;

	/*Monto ingresado tiene que ser mayor a cero*/
	public static final long ERROR_VALIDAR_VALORES2 = 10034;

	/*Fecha de desembolso no puede ser vacia*/
	public static final long ERROR_VALIDAR_FECDESEMBOLSO1 = 10035;

	/*Fecha desembolso no puede ser mayor a la fecha actual*/
	public static final long ERROR_VALIDAR_FECDESEMBOLSO2 = 10036;

	/*Producto no permite meses dobles*/
	public static final long ERROR_VALIDAR_MESESDOBLE1 = 10037;

	/*Modalidad de cuota no ha sido definida*/
	public static final long ERROR_VALIDAR_MODCUOTA1 = 10040;

	/*Producto no permite periodo de gracia*/
	public static final long ERROR_VALIDAR_PERGRACIA1 = 10045;

	/*El periodo de gracia excede a lo permitido por el producto*/
	public static final long ERROR_VALIDAR_PERGRACIA2 = 10046;

	/*No se ha definido al titular del cuenta*/
	public static final long ERROR_VALIDAR_TITULAR1 = 10047;

	/*No se ha definido el nombre del titular del cuenta*/
	public static final long ERROR_VALIDAR_TITULAR2 = 10048;

	/*No se ha definido moneda*/
	public static final long ERROR_VALIDAR_MONEDA1 = 10049;

	/*Moneda no permitida para el producto*/
	public static final long ERROR_VALIDAR_MONEDA2 = 10050;

	/*Numero de solicitud no pueder ser vacia*/
	public static final long ERROR_VALIDAR_NUMSOL = 10051;

	/*Error de llamada en funcion GENERAR CUENTA, plazo fuera del rango permitido para el producto*/
	public static final long ERROR_GENERACUENTA_PLAZODIAS = 10052;

	/*No existe valor para ESTADO EN PROCESO P*/
	public static final long ERROR_VALOR_ESTADO_PROCESO_P = 10053;

	/*Error al OBTENER TIPO FRECUENCIA*/
	public static final long ERROR_OBTENER_TIPO_FRECUENCIA = 10055;

	/*No existe concepto DESCRIPCION FRECUENCIA PAGO*/
	public static final long ERROR_CONCEPTO_DESC_FRECUENCIA_PAGO = 10056;

	/*No existe concepto DIAS FRECUENCIA PAGO*/
	public static final long ERROR_CONCEPTO_DIAS_FRECUENCIA_PAGO = 10057;

	/*No existe concepto FRECUENCIA PAGO*/
	public static final long ERROR_CONCEPTO_FRECUENCIA_PAGO = 10058;

	/*No existe concepto NUMERO DESEMBOLSOS*/
	public static final long ERROR_CONCEPTO_NUM_DESEMBOLSOS = 10059;

	/*El documento expediente ya existe*/
	public static final long DOCUMENTO_YA_EXISTE = 10062;

	/*Seleccione un documento expediente*/
	public static final long SELECCIONE_DOCUMENTO = 10063;

	/*El documento expediente no existe*/
	public static final long DOCUMENTO_NO_EXISTE = 10064;

	/*El documento esta dado de baja*/
	public static final long DOCUMENTO_DADO_BAJA = 10065;
	
	public static final long  ERROR_PERSONACNTA_SECUEN = 10066;	
	
	public static final long  ERROR_DATPERSONACNTA_SECUEN = 10067;
	
	public static final long  ERROR_GENARRAY_PERSONACNTA_GEN = 10068;
	
	public static final long  ERROR_PARTICIPANTESCUENTA_GEN = 10069;
	public static final long  ERROR_ALTACUENTA_GEN = 10070;
	public static final long  ERROR_PARTICIPANTESCNTA_CALLS = 10071;
	public static final long  ERROR_ALTACNTA_CALLS = 10072;
	public static final long  ERROR_VALIDAR_CNTRL_PROCESO = 10073;
	public static final long  ERROR_OBTNER_PRE_REQUISITO = 10074;
	public static final long  ERROR_PRE_REQUISITO_NO_ENCONTRADO = 10075;
	public static final long  ERROR_OBTNER_CNTRL_PROCESO = 10076;
	public static final long  ERROR_PRE_REQUISITO_NO_EJECUTADO = 10077;
	//falta
	public static final long  ERROR_OBTENER_DATOS_APERTURACNTA = 10078;
	public static final long  ERROR_OBTENER_DATOS_PARTICIPANTECNTA = 10079;
	public static final long  ERROR_OBTENER_DATOS_ALTACNTA = 10080;
	
	public static final long  ERROR_CNTRLPROCESOSCNTA_SECUEN = 10081;
	public static final long  ERROR_GENARRAY_CNTRLPROCESOSCNTA_GEN = 10082;
	public static final long  ERROR_CREACNTRLPROCESOS_CALLS = 10083;
	public static final long  ERROR_CREACNTRLPROCESOS_DATOS = 10084;
	public static final long  ERROR_CREACNTRLPROCESOS_GEN = 10085;
	
	/*Error de llamada en funcion actualizar titular cuenta, no pudo actualizar el titular la cuenta en la base de datos*/
	public static final long ERROR_ALTUALIZAR_TITULAR_CNTA = 10086;
	/*Error de llamada en funcion actualizar estado cuenta, no pudo actualizar el estado de la cuenta en la base de datos*/
	public static final long ERROR_ALTUALIZAR_ESTADO_CNTA = 10087;
	
	public static final long ERROR_CAMPOS_REQUERIDOS = 10088;
	public static final long ERROR_FILTRAR_PRODUCTO = 10089; //falta
	public static final long ERROR_LISTAR_CUENTAS = 10090; //falta
	public static final long ERROR_LISTAR_PROCESOS = 10091; //falta
	public static final long ERROR_LISTA_CUENTA_SIN_RESULTADO = 10092; //falta
	
 
	public static final long ERROR_MONTO_APERTURA = 10093; //falta
	public static final long ERROR_FECHA_APERTURA = 10094; //falta
	public static final long ERROR_NUMERO_FIRMAS = 10095; //falta
	public static final long ERROR_NUM_FIRMAS_OBL = 10096; //falta
	
 
	public static final long ERROR_NUM_CNTA_REF_DEPOSITO = 10097; //falta
	public static final long ERROR_IND_RENO_AUT = 10098; //falta
	public static final long ERROR_TIPO_CERTIFICADO = 10099; //falta
	public static final long ERROR_NUM_CERTIFICADO = 10100; //falta
	public static final long ERROR_DIAS_RET_POST_APER = 10101; //falta
	public static final long ERROR_FECHA_ULT_MOV_CNTA = 10102; //falta
	public static final long ERROR_FECHA_ULT_CALC_CNTA = 10103; //falta
	public static final long ERROR_FECHA_ULT_PAGO_INT_CNTA = 10104; //falta
	public static final long ERROR_FECHA_ULT_CAPIT_CNTAS = 10105; //falta
	public static final long ERROR_FECHA_CANCELACION_CNTA = 10106; //falta
	public static final long ERROR_FECHA_ULT_CAMB_TASA_CNTA = 10107; //falta
	public static final long ERROR_IND_MEN_EDAD = 10108; //falta
	public static final long ERROR_FECHA_ULT_RENOVACION = 10109; //falta
	public static final long ERROR_NUM_RENOVACION = 10110; //falta
	public static final long ERROR_MOTIVO_ANULACION = 10111; //falta
	public static final long ERROR_MOTIVO_CANCELACION = 10112; //falta
	public static final long ERROR_MOTIVO_ACTIVACION = 10113; //falta
	public static final long ERROR_MOTIVO_INACTIVACION = 10114; //falta
	public static final long ERROR_NUM_DIAS_PASA_INACT = 10115; //falta
	public static final long ERROR_FECHA_VIG_PLAZO_FIJO = 10116; //falta
	
	

	public static final long ERROR_TIPO_CONTROL_AHOPROG = 10117; //falta
	public static final long ERROR_MONTO_CONTROL_AHOPROG = 10118; //falta
	public static final long ERROR_DISPONIBILIDAD = 10119; //falta
	public static final long ERROR_DESTINO_INTERES = 10120; //falta
	
	
	//---
	public static final long ERROR_MONEDA = 10121; //falta
	public static final long ERROR_BASE_CALCULO = 10122; //falta
	public static final long ERROR_TASA_TARIFARIO = 10123; //falta
	public static final long ERROR_TASA_APLICACION = 10124; //falta
	public static final long ERROR_CONCEPTO_TASA = 10125; //falta
	public static final long ERROR_TIPO_TASA = 10126; //falta
	public static final long ERROR_TIPO_DISP_INTERES = 10127; //falta
	public static final long ERROR_FREC_CALC_INTERES = 10128; //falta
	public static final long ERROR_DIAS_FREC_CALC_INTERES = 10129; //falta
	public static final long ERROR_FREC_PAGO_INTERES =10130; //falta
	public static final long ERROR_DIAS_FREC_PAGO_INTERES = 10131; //falta
	public static final long ERROR_FREC_CAPITALIZACION = 10132; //falta
	public static final long ERROR_DIAS_FREC_CAPITALIZACION = 10133; //falta
	public static final long ERROR_IND_GANA_INT = 10134; //falta

	public static final long ERROR_IND_DEPOSITO_APERURA = 10135; //falta
	public static final long ERROR_IND_MANEJA_PLAZOS = 10136; //falta
	public static final long ERROR_IND_PLAZO_FIJO = 10137; //falta
	public static final long ERROR_IND_MANEJA_SALDO_INTANG = 10138; //falta
	public static final long ERROR_IND_MANEJA_SOBRE_GIRO = 10139; //falta
	public static final long ERROR_IND_MANEJA_AHO_PROG = 10140; //falta
	public static final long ERROR_IND_CUENTA_HABERES = 10141; //falta
	
	public static final long ERROR_HIDE_EMITE_CERT_DEPOS = 10142; //falta
	public static final long ERROR_IND_EMITE_CHEQUERA = 10143; //falta
	public static final long ERROR_IND_EMITE_ORDEN_PAGO = 10144; //falta
	public static final long ERROR_IND_COBRA_COMI = 10145; //falta
	public static final long ERROR_HIDE_DIAS_INAMOV_APER = 10146; //falta
	public static final long ERROR_HIDE_RENOVACION_PLAZO = 10147; //falta
	public static final long ERROR_IND_PERMITE_DEPOS = 10148; //falta
	public static final long ERROR_IND_RECIBE_TRANSF = 10149; //falta
	public static final long ERROR_IND_PERMITE_RETIROS_DISP = 10150; //falta
	public static final long ERROR_IND_PERMITE_TRANSF_DISP = 10151; //falta
	public static final long ERROR_IND_PERMITE_DOMICI = 10152; //falta
	public static final long ERROR_IND_PASA_INACTIVA = 10153; //falta
	
	public static final long ERROR_OBTENER_CONFIG_APERTURA = 10154; //falta
	
	public static final long ERROR_ELIMINARCNTA_PARTICIPANTE = 10155; //falta
	
	public static final long ERROR_OBTENER_TIPO_CONTROL_AHOPROG = 10156; //falta
	public static final long ERROR_OBTENER_MONTO_CONTROL_AHOPROG = 10157; //falta
	public static final long ERROR_OBTENER_NUM_DIAS_PLAZO_FIJO = 10158; //falta
	public static final long ERROR_OBTENER_MONTO_APERTURA = 10159; //falta
	public static final long ERROR_OBTENER_DISPONIBILIDAD = 10160; //falta
	public static final long ERROR_OBTENER_DESTINO_INTERES = 10161; //falta
	
	
	public static final long ERROR_OBTENER_DATOSCONFIGAPERTURA = 10162; //falta
	public static final long ERROR_CODIGO_CUENTA = 10163; //falta
	
	public static final long ERROR_LISTAR_PARTICIPANTES = 10164; //falta
	
	public static final long ERROR_DATOS_CNTA = 10165; //falta
	public static final long ERROR_RESUMEN_CNTA = 10166; //falta

	
	public static final long ERROR_OBTENER_PORCENTAJE_DISPONIBILIDAD = 10167; //falta
	public static final long ERROR_PORCENTAJE_DISPONIBILIDAD_NO_EXISTE = 10168; //falta
	
	public static final long ERROR_OBTENER_DATO_CATALOGO = 10169; //falta
	
	
	public static final long ERROR_MONTO_CAPITAL = 10170; //falta
	public static final long ERROR_MONTO_CONTABLE = 10171; //falta
	public static final long ERROR_CAPITAL_DISPONIBLE = 10172; //falta
	public static final long ERROR_INTERES_DISPONIBLE = 10173; //falta
	public static final long ERROR_TOTAL_DISPONIBLE = 10174; //falta
	public static final long ERROR_CAPITAL_INTANGIBLE = 10175; //falta
	public static final long ERROR_INTERES_INTANGIBLE = 10176; //falta
	public static final long ERROR_TOTAL_INTANGIBLE = 10177; //falta
	public static final long ERROR_CAPITAL_BLQ_CTA = 10178; //falta
	public static final long ERROR_INTERES_BLQ_CTA = 10179; //falta
	public static final long ERROR_TOTAL_BLQ_CTA = 10180; //falta
	public static final long ERROR_INT_ULT_CALCULO_NO_BLQ = 10181; //falta
	public static final long ERROR_INT_ULT_CALCULO_BLQS = 10182; //falta
	public static final long ERROR_INT_ULT_CALCULO_TOTAL = 10183; //falta
	public static final long ERROR_INT_CALC_NUEVO_PERI = 10184; //falta
	public static final long ERROR_INT_CALC_PERI = 10185; //falta
	public static final long ERROR_INT_TOTAL_PAGADO = 10186; //falta
	public static final long ERROR_INT_TOTAL_CAPITALIZADO = 10187; //falta
	public static final long ERROR_TOTAL_DEPOSITADO = 10188; //falta
	public static final long ERROR_TOTAL_RETIRADO = 10189; //falta
	public static final long ERROR_INT_TOTAL_RETIRADO = 10190; //falta
	public static final long ERROR_CAP_TOTAL_RETIRADO = 10191; //falta
	
	
	public static final long ERROR_ACTUA_DATOS_CNTA = 10192; //falta
	public static final long ERROR_ACTUA_RESUMEN_CNTA = 10193; //falta
	
	public static final long ERROR_CREA_DEPOSITO_CALL = 10194; //falta
	
	
	public static final long ERROR_INVKDEPOSITO_CALLS = 10195; //falta
	
	public static final long ERROR_VALOR_OPERACION_DEPOSITO = 10196; //falta
	public static final long ERROR_VALOR_CONCEPTO_TRANSACCION_DEPOSITO = 10197; //falta
	
	
	
	public static final long ERROR_TOTAL_CAPITAL = 10198; //falta
	public static final long ERROR_TOTAL_INTERES = 10199; //falta
	public static final long ERROR_TOTAL_SALDO = 10200; //falta
	public static final long ERROR_MONTO_ULTIMA_TRX = 10201; //falta
	public static final long ERROR_ULT_REMUNE = 10202; //falta
	public static final long ERROR_ULTIMO_FACTOR_DISPONIBLE = 10203; //falta
	public static final long ERROR_MONTO_ULTIMA_TRX_DISPO = 10204; //falta
	public static final long ERROR_MONTO_ULTIMA_TRX_INTANG = 10205; //falta
	public static final long ERROR_MONTO_TRX = 10206; //falta
	public static final long ERROR_FACTOR_DISPONIBILIDAD = 10207; //falta
	
	public static final long ERROR_DEPOSITO_CALLS= 10208; //falta
	
	public static final long ERROR_PRODUCTO= 10209; //falta
	
	public static final long ERROR_CALCTRANS= 10210; //falta
	public static final long ERROR_CONCEPXTRANS= 10211; //falta
	
	
	public static final long ERROR_CREA_CABE_DETAL_TRX= 10212; //falta
	public static final long ERROR_CREA_CABE_DETAL_TRX_CALL= 10213; //falta
	
	public static final long ERROR_OBTENER_TARIFAS= 10214; //falta
	public static final long ERROR_OBTENER_TARIFAS_GEN= 10215; //falta
	
	public static final long ERROR_OBTENER_TASASALL_GEN= 10216; //falta
	public static final long ERROR_OBTENER_TASAAGREGADA_GEN= 10217; //falta
	
	
	
	public static final long ERROR_MOVDEPOSITO_CALL= 10218; //falta
	public static final long ERROR_MOVDEPOSITO_GEN= 10219; //falta
	public static final long ERROR_DEPOSITO_GEN= 10220; //falta
	public static final long ERROR_MOVRETIRO_GEN= 10221; //falta
	public static final long ERROR_RETIRO_GEN= 10222; //falta
	public static final long ERROR_MOVRETIRO_CALL= 10223; //falta
	
	public static final long ERROR_MOVCUENTA_CALL= 10224; //falta
	public static final long ERROR_MOVCUENTA_GEN= 10225; //falta
	
	public static final long ERROR_VALOR_OPERACION_RETIRO= 10226; //falta
	public static final long ERROR_VALOR_CONCEPTO_TRANSACCION_RETIRO= 10227; //falta
	
	public static final long ERROR_OBTENER_CONCEP_OPERACION= 10228; //falta
	
	public static final long ERROR_TRANSF_PROPIAS_GEN= 10229; //falta
	public static final long ERROR_TRANSF_PROPIAS_CALL= 10230; //falta
	
	public static final long ERROR_DEPOSITO_CALL= 10231; //falta
	public static final long ERROR_RETIRO_CALL= 10232; //falta
	public static final long ERROR_NOTA_CARGO_GEN= 10233; //falta
	public static final long ERROR_NOTA_CARGO_CALL= 10234; //falta
	public static final long ERROR_NOTA_ABONO_CALL= 10235; //falta
	public static final long ERROR_NOTA_ABONO_GEN= 10236; //falta
	
	public static final long ERROR_OBTENER_DATOS_RETIRO_CALL= 10237; //falta
	
	public static final long ERROR_TRANSF_TERCEROS_GEN= 10238; //falta
	public static final long ERROR_TRANSF_TERCEROS_CALL= 10239; //falta
	
	public static final long ERROR_VALOR_OPERACION_NTCARGO= 10240; //falta
	public static final long ERROR_VALOR_OPERACION_NTABONO= 10241; //falta
	
	
	public static final long ERROR_VALOR_OPERACION_TRANSF_PROPIAS= 10242; //falta
	public static final long ERROR_VALOR_OPERACION_TRANSF_TERCEROS= 10243; //falta
	
	
	public static final long ERROR_OBTENER_CONCEP_OPERACION_XGRPOCLAVE= 10244; //falta
	
	public static final long ERROR_EMPRESA_REQUERIDO= 10245; //falta
	
	public static final long ERROR_MONTO_REQUERIDO= 10246;
	
	public static final long ERROR_CONSULTA_SALDO= 10247;
	
	public static final long ERROR_CONSULTA_SALDO_CON= 10248;
	
	public static final long ERROR_PRODUCTO_SIN_CONDICIONES= 10249;
	
	public static final long ERROR_TARIFA_INACTIVA= 10250;
	
	public static final long ERROR_YA_EXISTE_CONCEPTO_TASA= 10251;
	
	public static final long ERROR_IMPORTEID_NULL= 10252;
	
	public static final long ERROR_MONTO_DEPOSITO_VACIO= 10253;
	public static final long ERROR_SALDO_PROMEDIO_VACIO= 10254;
	public static final long ERROR_AHORRO_PROGRAMADO_NO_EXISTE= 10255;
	
	public static final long ERROR_ACTUALIZAR_INDSALDO_AHORRO_PROG= 10256;
	public static final long ERROR_ACTUALIZAR_INDMONTO_AHORRO_PROG= 10257;
	
	public static final long VALOR_NEGATIVO= 10258;
	public static final long DIA_PAGO_VACIO= 10259;
	public static final long DIA_PAGO_DETERNIMADO= 10260;
	public static final long DIA_INCORRECTO= 10261;
	
	public static final long ERROR_CREAR_TIPO_INTERES_DIA_PAGO=10262;
	public static final long ERROR_ELIMINAR_TIPO_INTERES_DIA_PAGO=10263;
	
	
	public static final long ERROR_RANG_MONTO_TARIF_INT=10264;
	public static final long ERROR_CORTE_INT=10265;
	public static final long ERROR_DIA_CORTE_INT=10266;
	
	public static final long ERROR_OBTNER_OPERACION_APERTURACNTA=10267;
	public static final long ERROR_OBTENER_CONCEPTRANSAC_APERTURACNTA=10268;
	public static final long ERROR_OBTENER_ESTADOTRANSAC_ACTIVO=10269;
	
	public static final long ERROR_PRODUCTO_DESTINO_NULL=10270;
	public static final long ERROR_NROCUENTAEXISTENTE_NULL=10271;
	
	public static final long ERROR_NRO_CNTA_EXIST=10272;
	
	public static final long ERROR_CALCULAR_INTERES=10273;
	
	public static final long SALDOCALCULOINTERES_NULL=10274;
	
	public static final long ERROR_SALDO_CALCULO_INTERES=10275;
	
	public static final long ERROR_ACTUAL_CALCULO_INTERES=10276;
	
	public static final long ERROR_CALCULO_INTERES_CALLS=10277;
	public static final long ERROR_ACTUALIZA_CALCULO_INTERES_CALLS=10278;
	
	public static final long ERROR_FECHA_VENC_PLAZO_FIJO=10279;
	public static final long ERROR_LIQUIDACION_INTERES_CALLS=10280;
	
	public static final long DECIMALFACTOR_NULL=10281;
	public static final long DECIMALINTERES_NULL=10282;
	public static final long DECIMALLIQUIDACION_NULL=10283;
	
	public static final long ERROR_CALCCAPITALIZACION_INTERES_CALLS=10284;
	public static final long ERROR_CALCPAGO_INTERES_CALLS=10285;
	
	public static final long ERROR_CONCEP_OPERACION_XGRPOYTIPOCLAVE=10286;
	
	public static final long ERROR_VALOR_OPERACION_PAGO_INTERES=10287;
	public static final long ERROR_MOVPAGOINT_CALL=10288;
	public static final long ERROR_PAGO_INT_GEN=10289;
	
	public static final long ERROR_OBTENER_DATOS_BATCH_CNTA=10290;
	
	public static final long ERROR_PROCCNTA_PRINCIPAL_GEN=10291;
	
	
	public static final long ERROR_OBT_DECIMAL_FACTOR=10292;
	public static final long ERROR_OBT_DECIMAL_INTERES=10293;
	public static final long ERROR_OBT_DECIMAL_LIQUIDACION=10294;
	

	public static final long ERROR_OBT_SALDO_PROMEDIO=10295;
	public static final long ERROR_TASAS_CNTA=10296;
	
	public static final long ERROR_PROCCNTA_ACTBDGEN_GEN=10297;
	
	
	public static final long ERROR_PAGO_INTERES_MENOR_A_MES=10298;
	public static final long ERROR_FRECCAP_DIARIA_O_FRECPAG=10299;
	public static final long ERROR_FRECCAL_DIARIA_O_FRECCAP=10300;
	public static final long ERROR_FRECCAL_DIARIA_O_FRECPAG=10301;
	public static final long ERROR_PAGO_INTERES_MENOR_A_SEMANAL=10302;
	public static final long DIA1_DIA2_REQUERIDOS=10303;
	public static final long DIA1_DIA2_NOMAYOR_30=10304;
	public static final long DIAS_CORTE_PAGO_DIFE_FRECPAG=10305;
	public static final long ERROR_FRECCAP_MAYOR_A_FRECPAG=10306;
	public static final long ERROR_FRECCAL_MAYOR_A_FRECCAP=10307;
	public static final long ERROR_FRECCAL_MAYOR_A_FRECPAG=10308;
	
	public static final long DISPOSICION_INTERES_REQUERIDO=10309;
	
	public static final long FRECCAL_REQUERIDO=10310;
	public static final long FRECCAP_REQUERIDO=10311;
	public static final long FRECPAG_REQUERIDO=10312;
	
	public static final long ERROR_NUM_DIAS_PLAZO_FIJO = 10313; //falta
	public static final long DIA1_REQUERIDOS= 10314;
	
	public static final long ERROR_SALDOS_MENSUAL_SECUEN= 10315;
	public static final long ERROR_GENARRAY_SALDOS_MENSUAL_GEN= 10316;
	public static final long ERROR_SALDOS_DIARIOS_SECUEN= 10317;
	public static final long ERROR_GENARRAY_SALDOS_DIARIOS_GEN= 10318;
	
	public static final long ERROR_ACTUA_SALDO_DIARIO= 10319;
	public static final long ERROR_CREAR_SALDO_DIARIO= 10320;
	public static final long ERROR_OBTIENE_SALDOSXDIA_CALLS= 10321;
	public static final long ERROR_GENARRAY_SALDOS_MENSUAL_CALLS= 10322;
	public static final long ERROR_CREAR_SALDOSMENSUAL= 10323;
	public static final long ERROR_OBTENER_SALDOS_DIARIOS= 10324;
	public static final long ERROR_OBTENER_MAXSALDOSMESUALESID= 10325;
	public static final long ERROR_OBTENER_SALDO_MESUAL= 10326;

	public static final long ERROR_VALOR_CONCEPTO_TRANSACCION_PAGINTERES= 10327;
	public static final long ERROR_INVKPAGOINTERES_CALLS= 10328;
	public static final long ERROR_OBTIENE_DATOS_DEPOSITO_CALLS= 10329;
	public static final long ERROR_OBTIENE_DATOS_PAGOINT_CALLS= 10330;
	
	
	public static final long ERROR_TARIFASINTERES_NULL= 10331;
	
	
	public static final long ERROR_GENERA_SALDOSMENSUALES= 10332;
	public static final long ERROR_GENERA_SALDOSMENS_CALLS= 10333;
	
	public static final long ERROR_TRXPAGO_INTERES_CALLS= 10334;
	
	
	
	public static final long ERROR_OBTENER_OPERACION= 10335;
	public static final long ERROR_OBTENER_EXPRESIONCALC= 10336;
	public static final long ERROR_EXPRESIONCALC_NOEXISTE= 10337;
	public static final long ERROR_ALORDENAR_EXPRESIONCALC= 10338;
	public static final long ERROR_INVKOBTIENEEXPRESIONCALC_CALLS= 10339;
	
	
	public static final long ERROR_OBTIENE_EXPRESIONCALC_CALCINT_CALLS= 10340;
	
	public static final long ERROR_PROCESEXPRESION= 10341;
	
	
	public static final long ERROR_PROCESA_EXPRESIONCALC_CALLS= 10342;
	public static final long ERROR_OBTIENE_EXPRESIONCALC_CAPIINT_CALLS= 10343;
	public static final long ERROR_OBTIENE_EXPRESIONCALC_PAGOINT_CALLS= 10344;
	
	public static final long ERROR_IND_FREC_CAPITALIZACION= 10345;
	
	
	public static final long ERROR_MOVCAPITALIZACION_GEN= 10346;
	public static final long ERROR_OBTIENE_DATOS_CAPIINT_CALLS= 10347;
	public static final long ERROR_MOVPAGOINTERES_GEN= 10348;
	
	public static final long ERROR_VALOR_OPERACION_CAPITALIZACION_INTERES= 10349;
	public static final long ERROR_MOVCAPIINT_CALL= 10350;
	public static final long ERROR_CAPI_INT_GEN= 10351;
	
	public static final long ERROR_CAPITALIZACION_CALL= 10352;
	
	
	
	public static final long ERROR_OBTENER_DATOS_CAPIINTERES_CALLS= 10353;
	public static final long ERROR_OBTENER_DATOS_PAGOINTERES_CALLS= 10354;
	public static final long ERROR_VALOR_OPERACION_PASAINACTCNTA= 10355;
	public static final long ERROR_VALOR_CONCEPTO_TRANSACCION_PASAINACTCNTA= 10356;
	public static final long ERROR_OBTIENE_DATOS_PASAINACTCNTA_CALLS= 10357;
	public static final long ERROR_VALOR_CONCEPTO_TRANSACCION_CAPIINTERES= 10358;
	
	
	public static final long ERROR_TRXPASAINACTIVA_CALLS= 10359;
	public static final long ERROR_ACTUAL_INDESTADOCUENTA_GEN= 10360;
	public static final long ERROR_ACTUALIZAR_INDESTADO_CNTA= 10361;
	
	
	public static final long ERROR_MOVPASAINACT_CALL= 10362;
	public static final long ERROR_PASAINAC_GEN= 10363;
	public static final long ERROR_MOVGENERICO_CALL= 10364;
	public static final long ERROR_MOVPASAINACTIVA_GEN= 10365;
	
	public static final long ERROR_PASAINACTIVA_GEN= 10366;
	
	
	public static final long ERROR_TIPOINTERES_CANCELACION= 10367;
	
	
	
	
	public static final long ERROR_OBTENER_DATOS_CNTAPLAZO_GEN= 10368;
	public static final long ERROR_CREAR_CRONOPLAZOFIJO_GEN= 10369;
	public static final long ERROR_OBTIENE_DATOS_CNTAPLAZO_CALLS= 10370;
	public static final long ERROR_CREA_CRONOPLAZOFIJO_CALLS= 10371;
	public static final long ERROR_CREAR_CRONOPLAZO_GEN= 10372;
	public static final long ERROR_OBTENER_CRONOPAGOS= 10373;
	public static final long ERROR_OBTENER_ULT_CRONOPAGO_GEN= 10374;
	public static final long ERROR_ACTUAL_CRONOPLAZOFIJO_GEN= 10375;
	public static final long ERROR_ELIMINA_CRONOGRAMAPLAZO= 10376;
	public static final long ERROR_OBTIENE_ULTIMO_CRONOPAGO_CALLS= 10377;
	public static final long ERROR_OBTENER_IMPORTE_ACTUAL= 10378;
	public static final long ERROR_ACTUAL_CRONOPLAZOFIJO_CALLS= 10379;
	public static final long ERROR_ACTUAL_CRONOPLAZO_GEN= 10380;
	public static final long ERROR_ACTUAL_CRONOPLAZO_CALLS= 10381;
	public static final long ERROR_CREA_CRONOPLAZO_CALLS= 10382;
	
	
	
	public static final long RANGO_TARIFA_NO_EXISTE= 10383;
	
	
	public static final long ERROR_CRONOPAGOCNTA_SECUEN= 10384;
	public static final long ERROR_GENARRAY_CRONOCNTA_GEN= 10385;
	public static final long ERROR_GENARRAY_CRONOCNTA_CALLS= 10386;
	
	
	public static final long ERROR_CREAR_CRONOPLAZO= 10387;
	
	public static final long ERROR_LIQUIDACION_INTERES_GEN= 10388;
	
	public static final long ERROR_CALCINTERES_GEN= 10389;
	public static final long ERROR_CALCCAPITALIZACION_GEN= 10390;
	public static final long ERROR_CALCPAGOINTERES_GEN= 10391;
	
	
	public static final long ERROR_OBTENER_FRECENCIA= 10392;
	public static final long ERROR_FRECUENCIA_NOEXISTE= 10393;
	
	public static final long  ERROR_CORTE_PRODUCTO_NORMAL= 10394;

	
	public static final long  ERROR_REGISTRO_CRONOPAGO= 10395;
	
	
	public static final long  ERROR_OBTENER_CODIGO_OPERACION= 10396;
	public static final long  ERROR_OBTENER_CODIGO_CONCEP_TRANSAC= 10397;
	
	
	public static final long  ERROR_LIQUIDA_INTERES_CALLS= 10398;
	
	public static final long  ERROR_CNTA_ACTBDGEN_GEN= 10399;
	public static final long  ERROR_CNTA_ACTBDGEN_CALLS= 10400;
	
	
	public static final long  ERROR_OBTENER_CONCEPTOS_OPERACION_GEN= 10401;
	

	public static final long  ERROR_NROCUENTA_DESTINO_NULL= 10402;
	public static final long  ERROR_ESTADO_CUENTA= 10402;
	
	
	public static final long  ERROR_TRXTRNS_PROPIAS_CALLS= 10403;
	
	public static final long  ERROR_OBTENER_DATOS_TRANSF_CALL= 10404;
	public static final long  ERROR_OBTENER_DATOS_NOTAS_CALL= 10405;
	public static final long  ERROR_OBTENER_DATOS_TRANSF_GEN= 10406;
	public static final long  ERROR_OBTENER_DATOS_NOTAS_GEN= 10407;
	public static final long  ERROR_OBTIENE_DATOS_RETIRO_CALLS= 10408;
	
	
	public static final long  ERROR_VALOR_OPERACION_APERTURA_CNTA= 10409;
	public static final long  ERROR_VALOR_OPERACION_CALCULO_INTERES= 10410;
	
	public static final long  ERROR_OBTENER_DATOS_DEPOSITO_CALL= 10411;
	
	
	public static final long  ERROR_TRXNOTACARGO_CALLS= 10412;
	
	public static final long  ERROR_CREA_CRONO_CANCEL_PLAZOFIJO_CALLS= 10413;
	public static final long  ERROR_CREAR_CANCEL_CRONOPLAZO= 10414;
	
	
	
	public static final long  ERROR_OBTENER_DATOS_CANCELCNTA= 10415;
	public static final long  ERROR_OBTENER_DATOS_CANCELCNTA_GEN= 10416;
	public static final long  ERROR_OBTENER_DATOS_CANCELCNTA_CALLS= 10417;

	
	
	public static final long  ERROR_CONFIG_CANCELPLZ_NOEXISTE= 10418;
	
	
	public static final long  ERROR_VALOR_OPERACION_RENOVAR_CNTA= 10419;
	
	
	
	public static final long  ERROR_ACTUALIZA_CRONOPLAZOFIJO_CALLS= 10420;
	public static final long  ERROR_ACTUALIZA_CRONOPLAZO_GEN= 10421;

	
	
	public static final long  ERROR_FECHA_ULT_PRO_CIERRE= 10422;
	public static final long  ERROR_FECHA_ULT_VAL_CIERRE= 10423;
	public static final long  ERROR_FECHA_ULT_COR_CIERRE= 10424;
	
	
	public static final long  ERROR_CREA_SALDIA_CALL= 10425;
	public static final long  ERROR_ACT_SALDIA_CALL= 10426;
	
	
	public static final long  ERROR_CREA_SALDIA_GEN= 10427;
	public static final long  ERROR_ACT_SALDIA_GEN= 10428;
	public static final long  ERROR_SALDO_DIARIO_GEN= 10429;
	public static final long  ERROR_SALDO_DIARIO_CALL= 10430;
	
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
		descError.put(ERROR_VALIDAR_NUEVO_PRODUCTO,"Error al validar el nuevo producto");
		descError.put(ERROR_YA_PRODUCTO_EXITE,"Error el producto ya existe");
		descError.put(ERROR_CREAR_PRODUCTO,"Error al crear el producto" );
		descError.put(ERROR_VALIDAR_BUSQUEDA_PRODUCTO,"Error al validar la busqueda del producto" );
		descError.put(NO_EXISTE_PRODUCTO ,"No existe producto");
		descError.put(BUSQUEDA_PRODUCTO_SIN_RESULTADOS,"Busqueda de producto sin resultado" );
		descError.put(ERROR_BUSCAR_PRODUCTO,"Error al buscar el producto");
		descError.put(SELECCIONE_PRODUCTO,"Seleccione producto");
		descError.put(OBTENER_PRODUCTO_SIN_RESULTADOS,"Obtener producto sin resultado");
		descError.put(ERROR_OBTENER_PRODUCTO,"Error al obtener el producto");
		descError.put(ERROR_BUSCAR_DATOS_PRODUCTO,"Error al buscar datos del producto" );
		descError.put(ERROR_MODIFICAR_PRODUCTO,"Error modificar producto" );
		descError.put(ERROR_MODIFICAR_ESTA_INACTIVO,"Error modificar el producto esta inactivo" );
		
		descError.put(ERROR_OBTENER_LISTA_CONDICIONES,"Error obtener lista de condiciones" );
		descError.put(ERROR_CODIGO_EMPRESA,"Error codigo de empresa" );
		descError.put(NO_EXISTE_CONDICION,"No existe condicion");
		descError.put(ERROR_MONTO_MINIMO_DEBE_SER_MAYOR_IGUAl_0,"Monto minimo debe ser mayor o igual a cero");
		
		descError.put(ERROR_VALIDAR_GUARDAR_DEPOSITO_APERTURA,"Error validar datos a guardar para el Deposito Apertura");
		descError.put(ERROR_GUARDAR_DEPOSITO_APERTURA,"Error guardar Depisoto Apertura");
		descError.put(ERROR_CREAR_DEPOSITO_APERTURA,"Error al crear el Deposito Apertura");
		descError.put(ERROR_ACTUALIZAR_DEPOSITO_APERTURA,"Error al actualizar los datos para Deposito Apertura");
		descError.put(ERROR_DEBE_ASIGNAR_LA_CONDICION,"Condicion no esta asignada");
		descError.put(ERROR_OBTENER_DEPOSITO_APERTURA,"Error al obtener los datos para Deposito de Apertura");
		
		descError.put(ERROR_OBTENER_MANEJA_PLAZOS,"Error al obtener Maneja Plazos");
		descError.put(ERROR_PLAZO_DIAS_DESDE_DEBE_SER_MAYOR_IGUAl_0,"Dias Desde debe ser mayor o igual a cero");
		descError.put(ERROR_PLAZO_DIAS_HASTA_DEBE_SER_MAYOR_IGUAl_0,"Dias Hasta debe ser mayor o igual a cero");		
		descError.put(ERROR_PLAZO_DIAS_HASTA_NO_DEBE_SER_MENOR_A_PLAZO_DIAS_DESDE,"El plazo en días hasta no debe ser menor que el plazo en días desde.");
		descError.put(ERROR_VALIDAR_GUARDAR_MANEJA_PLAZOS,"Error al validar datos para Maneja Plazos");
		descError.put(ERROR_ACTUALIZAR_MANEJA_PLAZOS,"Error al actualizar los datos para Maneja Plazos");
		descError.put(ERROR_CREAR_MANEJA_PLAZOS,"Error al crear Maneja Plazos");
		
		descError.put(ERROR_OBTENER_AHORRO_PROGRAMADO,"Error al obtener datos para Ahorro Programado");
		descError.put(ERROR_PORCENTAJE_BONI_MENOR_IGUAL_A_CERO,"Porcentaje de bonificacion menor igual a cero");
		descError.put(ERROR_PORCENTAJE_BONI_TIENE_QUE_ESTAR_VACIO,"Porcentaje bonificacion tiene que estar vacio");
		descError.put(ERROR_VALIDAR_GUARDAR_AHORRO_PROGRAMADO,"Error al validar datos para Ahorro Programado");
		descError.put(ERROR_ACTUALIZAR_AHORRO_PROGRAMADO,"Error al actualizar datos para Ahorrro Programado");
		descError.put(ERROR_CREAR_AHORRO_PROGRAMADO,"Error al crear Ahorro Programado");
		descError.put(ERROR_GUARDAR_AHORRO_PROGRAMADO,"Error al guardar datos para Ahorro Programado");
		
		descError.put(ERROR_MONTO_MINIMO_DEBE_SER_MAYOR_A_0,"Monto mínimo debe ser mayor a cero");
		descError.put(ERROR_VALIDAR_NUEVO_MONTO_DEPOSITO,"Error validar datos para el nuevo Monto Deposito");
		descError.put(ERROR_CREAR_MONTO_DEPOSITO,"Error al Crear Monto Deposito");
		descError.put(ERROR_NUEVO_MONTO_DEPOSITO,"Error en Nuevo Monto Deposito");
		
		descError.put(ERROR_MODIFICAR_MONTO_DEPOSITO,"Error modificar Monto Depisito");
		descError.put(ERROR_VALIDAR_MODIFICAR_MONTO_DEPOSITO,"Error validar datos para Modificar el Monto Deposito");
		descError.put(ERROR_MONTO_DEPOSITO_NO_EXISTE,"Monto Deposito no existe");
		descError.put(ERROR_CONDICION_ASIGNADA_NO_EXISTE,"Condicion Asignada no existe");
		
		descError.put(ERROR_VALIDAR_BAJA_MONTO_DEPOSITO,"Error validar datos de baja para Monto Deposito");
		descError.put(ERROR_BAJA_MONTO_DEPOSITO,"Error baja Monto Deposito");
		
		descError.put(ERROR_SALDO_PROMEDIO_MINIMO_DEBE_SER_MAYOR_A_0,"Saldo promedio mínimo debe ser mayor a cero");
		descError.put(ERROR_CREAR_SALDO_PROMEDIO,"Error Crear Saldo Promedio");
		descError.put(ERROR_VALIDAR_NUEVO_SALDO_PROMEDIO,"Error al validar datos para Nuevo Saldo Promedio");
		descError.put(ERROR_NUEVO_SALDO_PROMEDIO,"Error Nuevo Saldo Promedio");
		
		descError.put(ERROR_VALIDAR_MODIFICAR_SALDO_PROMEDIO,"Error al validar datos a modificar para Saldo Promedio");
		descError.put(ERROR_SALDO_PROMEDIO_NO_EXISTE,"Error Saldo Promedio no existe");
		descError.put(ERROR_MODIFICAR_SALDO_PROMEDIO,"Error Modificar Saldo Promedio");
		
		descError.put(ERROR_VALIDAR_BAJA_SALDO_PROMEDIO,"Error validar datos baja para Saldo Promedio");
		descError.put(ERROR_BAJA_SALDO_PROMEDIO,"Error Baja Saldo Promedio");
		
		descError.put(ERROR_OBTENER_MONTOS_DEPOSITOS,"Error Obtener Montos Depositos");
		descError.put(ERROR_OBTENER_MONTO_DEPOSITO,"Error Obtener Monto Deposito");
		
		descError.put(ERROR_OBTENER_SALDOS_PROMEDIOS,"Error Obtener Saldos Promedios");
		descError.put(ERROR_OBTENER_SALDO_PROMEDIO,"Error Obtener Saldo Promedio");	
		
		descError.put(ERROR_VALIDAR_GUARDAR_CERTIFICADO_DEPOSITO,"Error validar datos para Certidicado Deposito");	
		descError.put(ERROR_CREAR_CERTIFICADO_DEPOSITO,"Error Crear Certificado Deposito");	
		descError.put(ERROR_ACTUALIZAR_CERTIFICADO_DEPOSITO,"Error al Actualizar datos para Certificado Deposito");
		descError.put(ERROR_GUARDAR_CERTIFICADO_DEPOSITO,"Error al Guardar datos para Cetificado Deposito");
		
		descError.put(ERROR_OBTENER_LISTA_COMISIONES,"Error al Obtener la lista de Comisiones");
		
		descError.put(NO_EXISTE_COMISION,"Comision no existe");;
		descError.put(ERROR_APLICAR_COMISION,"Error Aplicar Comision");
		descError.put(ERROR_QUITAR_COMISION,"Error al Quitar Comision");
		
		descError.put(ERROR_DEBE_APLICAR_LA_COMISION,"Debe aplicar Comision");
		descError.put(ERROR_VALIDAR_GUARDAR_COMISION_GENERALES,"Error al Guardar Comision Generales");
		
		descError.put(ERROR_ACTUALIZAR_COMISION_GENERALES,"Error al Actualizar datos para Comision Generales");
		descError.put(ERROR_CREAR_COMISION_GENERALES,"Error al Crear Comision Generales");
		descError.put(ERROR_GUARDAR_COMISION_GENERALES,"Error al Guardar datos para Comision Generales");
		
		descError.put(ERROR_COMISION_APLICADA_NO_EXISTE,"Comision aplicada no existe");
		descError.put(ERROR_OBTENER_IMPORTES_COMISION,"Error Obtener Importes Comision");
		
		descError.put(ERROR_IMPORTE_COMISION_NO_EXISTE,"Error Obtener Importe Comision");
		descError.put(ERROR_OBTENER_IMPORTE_COMISION,"Error Obtener Importe Comision");
		
		descError.put(ERROR_SALDO_PROM_DESDE_NO_PUEDE_SER_NEG,"Saldo promedio desde no puede ser negativo");
		descError.put(ERROR_SALDO_PROM_HASTA_NO_PUEDE_SER_NEG,"Saldo promedio hasta no puede ser negativo");
		descError.put(ERROR_SALDO_PROM_HASTA_NO_PUEDE_SER_MENOR_A_SALDO_PROM_DESDE,"Saldo promedio hasta no puede ser menor que el saldo promedio desde");
		descError.put(ERROR_NUM_OCURREN_DESDE_NO_PUEDE_SER_NEG,"No. Ocurrencia desde no puede ser negativo");
		descError.put(ERROR_NUM_OCURREN_HASTA_NO_PUEDE_SER_NEG,"No. ocurrencia hasta no puede ser negativo");
		descError.put(ERROR_NUM_OCURREN_HASTA_NO_PUEDE_SER_MENOR_A_NUM_OCURREM_DESDE,"No. Ocurrencia hasta no puede ser menor que No. Ocurrencia desde");
		descError.put(ERROR_IMPORTE_PORCENTAJE_NO_PUEDE_SER_NEG,"Importe o porcentaje no puede ser negativo");
		
		descError.put(ERROR_VALIDAR_NUEVO_IMPORTE_COMISION,"Error validar Nuevo Importe Comision");
		descError.put(ERROR_CREAR_IMPORTE_COMISION,"Error Crear Importe Comision");
		descError.put(ERROR_NUEVO_IMPORTE_COMISION,"Error Nuevo Importe Comision");
		
		descError.put(ERROR_ITEM_NO_ENCONTRADO,"Item no encontrado");
		
		descError.put(ERROR_IMPORT_MIN_APLI_DEBE_SER_MAY_0,"Importe mínimo a aplicar tiene que ser mayor a cero");
		descError.put(ERROR_IMPORT_MAX_APLI_DEBE_SER_MAY_0,"Importe máximo a aplicar tiene que ser mayor a cero");
		descError.put(ERROR_IMPORT_MAX_APLI_DEBE_SER_MAY_A_IMPORT_MIN,"Importe máximo a aplicar tiene que ser mayor o igual a importe mínimo a aplicar");
		
		descError.put(ERROR_VALIDAR_MODIFICAR_IMPORTE_COMISION,"Error validar modificar Importe Comision");
		descError.put(ERROR_MODIFICAR_IMPORTE_COMISION,"Error Modificar Importe Comision");

		descError.put(ERROR_VALIDAR_BAJA_IMPORTE_COMISION,"Error validar baja Importe Comision");
		descError.put(ERROR_BAJA_IMPORTE_COMISION,"Error Baja Importe Comision");
		
		descError.put(ERROR_OBTENER_TIPOS_INTERESES,"Error Obtener Tipos Intereses");
		
		descError.put(ERROR_TIPO_INTERES_NO_EXISTE,"Tipo Interes no existe");
		descError.put(ERROR_OBTENER_TIPO_INTERES,"Error Obtener Tipo Interes");
		
		descError.put(ERROR_VALIDAR_NUEVO_TIPO_INTERES,"Error validar Nuevo Tipo Interes");
		descError.put(ERROR_CREAR_TIPO_INTERES,"Error Crear Tipo Interes");
		
		descError.put(ERROR_VALIDAR_MODIFICAR_TIPO_INTERES,"Error validar modificar Tipo Interes");
		descError.put(ERROR_MODIFICAR_TIPO_INTERES,"Error Modificar Tipo Interes");
		
		descError.put(ERROR_VALIDAR_BAJA_TIPO_INTERES,"Error validar baja Tipo Interes");
		descError.put(ERROR_BAJA_TIPO_INTERES,"Error Baja Tipo Interes");
		
		descError.put(ERROR_OBTENER_TARIFAS_INTERES,"Error Obtener Tarifas Interes");
		
		descError.put(ERROR_TARIFA_INTERES_NO_EXISTE,"Tarifa Interes no existe");
		descError.put(ERROR_OBTENER_TARIFA_INTERES,"Error Obtener Tarifa Interes");
		
		descError.put(ERROR_MONTO_MINIMO_NO_PUEDE_SER_NEG,"Monto mínimo no puede ser negativo");
		descError.put(ERROR_MONTO_MAXIMO_NO_PUEDE_SER_NEG,"Monto máximo no puede ser negativo");
		descError.put(ERROR_MONTO_MAXIMO_NO_PUEDE_SER_MENOR_A_MONTO_MINIMO,"Monto máximo no puede ser menor que Monto mínimo");
		descError.put(ERROR_PLAZO_MINIMO_NO_PUEDE_SER_NEG,"Plazo mínimo no puede ser negativo");
		descError.put(ERROR_PLAZO_MAXIMO_NO_PUEDE_SER_NEG,"Plazo máximo no puede ser negativo");
		descError.put(ERROR_PLAZO_MAXIMO_NO_PUEDE_SER_MENOR_A_PLAZO_MINIMO,"Plazo máximo no puede ser menor que Plazo mínimo");
		
		descError.put(ERROR_VALIDAR_NUEVA_TARIFA_INTERES,"Error validar Nueva Tarifa Interes");
		descError.put(ERROR_CREAR_TARIFA_INTERES,"Error Crear Tarifa Interes");
		descError.put(ERROR_NUEVA_TARIFA_INTERES,"Error Nueva Tarifa Interes");
		
		descError.put(ERROR_VALIDAR_MODIFICAR_TARIFA_INTERES,"Error validar modificar Tarifa Interes");
		descError.put(ERROR_MODIFICAR_TARIFA_INTERES,"Error Modificar Tarifa Interes");
		
		descError.put(ERROR_OBTENER_DIAS_INAMOVILIDAD,"Error Obtener Dias Inamovilidad");
		
		descError.put(ERROR_DIAS_INAMOVILIDAD_DEBE_SER_MAYOR_IGUAl_0,"Días inamovilidad deber ser mayor o igual a cero");
		descError.put(ERROR_VALIDAR_GUARDAR_DIAS_INAMOBILIDAD,"Error validar Guardar Dias Inamovilidad");
		descError.put(ERROR_ACTUALIZAR_DIAS_INAMOVILIDAD,"Error Actualizar datos para Dias Inamovilidad");
		descError.put(ERROR_CREAR_DIAS_INAMOVILIDAD,"Error Crear Dias Inamovilidad");
		descError.put(ERROR_GUARDAR_DIAS_INAMOVILIDAD,"Error Guardar Dias Inamovilidad");
		
		descError.put(ERROR_OBTENER_RENOVACION_PLAZO,"Error Obtener Renovación Plazo");
		
		descError.put(INGRESE_NUEVO_PLAZO,"Ingrese nuevo plazo");
		descError.put(ERROR_NUEVO_PLAZO_MENOR_A_0,"Nuevo plazo menor a cero");
		descError.put(INGRESE_DIAS_ESPERA,"Ingrese Dias espera");
		descError.put(ERROR_DIAS_ESPERA_MENOR_A_0,"Dias espera menor a cero");
		descError.put(ERROR_SELECCIONE_TASA_INTERES_DIAS_ESPERA,"Seleccione Item en  Tasa Interes dias espera");
		descError.put(ERROR_SELECCIONE_GANA_INT_VENCI,"Seleccione Item en  Gana interes despúes vencimiento");
		
		descError.put(ERROR_VALIDAR_GUARDAR_RENOVACION_PLAZO,"Error validar Guardar Renovación Plazo");
		descError.put(ERROR_ACTUALIZAR_RENOVACION_PLAZO,"Error Actualizar Renovación Plazo");
		descError.put(ERROR_CREAR_RENOVACION_PLAZO,"Error Crear Renovación Plazo");
		descError.put(ERROR_GUARDAR_RENOVACION_PLAZO,"Error Guardar Renovación Plazo");
		
		descError.put(ERROR_DIAS_NO_PENA_MENOR_IGUAL_A_CERO,"Días a partir de los cuales no penaliza debe ser mayor a cero");
		
		descError.put(ERROR_OBTENER_CANCELACION_PLAZO,"Error Obtener Cancelación Plazo");
		
		descError.put(ERROR_VALIDAR_GUARDAR_CANELACION_PLAZO,"Error validar Guardar Cancelación Plazo");
		descError.put(ERROR_ACTUALIZAR_CANCELACION_PLAZO,"Error Actualizar Cancelación Plazo");
		descError.put(ERROR_CREAR_CANCELACION_PLAZO,"Error Crear Cancelación Plazo");
		descError.put(ERROR_GUARDAR_CANCELACION_PLAZO,"Error Guardar Cancelación Plazo");
		
		descError.put(ERROR_DIAS_SIN_MOV_DEBE_SER_MAYOR_IGUAl_0,"Días sin movimiento debe ser mayor a cero.");
		descError.put(ERROR_SALDO_DEBE_SER_MAYOR_IGUAl_0,"Saldo debe ser mayor o igual a cero.");
		
		descError.put(ERROR_VALIDAR_GUARDAR_PASA_INACTIVA,"Error valida guarda Pasa a Inactiva");
		descError.put(ERROR_ACTUALIZAR_PASA_INACTIVA,"Error Actualizar datos para Pasa a Inactiva");
		descError.put(ERROR_CREAR_PASA_INACTIVA,"Error Crear Pasa a Inactiva");
		descError.put(ERROR_GUARDAR_PASA_INACTIVA,"Error Guardar Pasa a Inactiva");
		
		descError.put(ERROR_PRODUCTO_NO_EXISTE,"Producto no existe");
		descError.put(ERROR_VALIDAR_ACTIVAR_PRODUCTO,"Error validar Activar Producto");
		descError.put(ERROR_ACTIVAR_PRODUCTO,"Error Activar Producto");
		descError.put(ERROR_VALIDAR_BAJA_PRODUCTO,"Error validar Baja Prodcuto");
		descError.put(ERROR_BAJA_PRODUCTO,"Error Baja Producto");
		
		
		descError.put(ERROR_NUEVA_TARIFA_INTERES,"Error Nueva Tarifa Interes");;
		descError.put(ERROR_NUEVO_TIPO_INTERES,"Error Nuevo Tipo Interes");
		descError.put(ERROR_OBTENER_COMISION_GENERALES,"Error Obtener Comision Generales");
		
		descError.put(ERROR_OBTENER_DIAS_INAMOBILIDAD,"Error Obtener Dias Inamovilidad");
		
		descError.put(ERROR_OBTENER_PASA_INACTIVA,"Error Obtener Pasa a Inactiva");
		
		descError.put(ERROR_OBTENER_CERTIFICADO_DEPOSITO,"Error Obtener Certificado Deposito");
		
		
		descError.put(ERROR_BAJA_MONTOS_DEPOSITOS,"Error baja Montos Depositos");
		descError.put(ERROR_BAJA_SALDOS_PROMEDIOS,"Error baja Saldos Promedios");
		descError.put(ERROR_BAJA_IMPORTES_COMISION,"Error baja Importes Comisión");
		descError.put(ERROR_BAJA_TIPOS_INTERESES,"Error Baja Tipos Intereses");
		descError.put(ERROR_BAJA_TARIFAS_INTERESES,"Error baja Tarifas Interes");
		
		descError.put(ERROR_BAJA_TARIFAS_INTERES,"Error baja Tarifas Interes");
		descError.put(ERROR_BAJA_TIPOS_INTERES,"Error baja tipos Interes");
		
		descError.put(FECHA_MENOR_ACTUAL,"Fecha es menor a la actual");
		
		descError.put(ERROR_VALIDAR_BAJA_TARIFA_INTERES,"Error validar baja Tarifa Interes");
		descError.put(ERROR_BAJA_TARIFA_INTERES,"Error baja Tarifa Interes");
		
		descError.put(ERROR_ACTUALIZAR_ESTADO_CONDICION,"Error Actualizar Estado Condición");
		descError.put(ERROR_VERIF_SI_HAY_TIPOS_INTERES,"Error al verificar si hay tipos de interes");
		descError.put(ERROR_ACTUALIZAR_ESTADO_COMISION,"Error Actualizar Estado Comisión");
		descError.put(ERROR_OBTENER_MATRIZ_CONDICION_ID,"Error Obtener MatrizCondicionId");
		
		
		descError.put(ERROR_VALIDAR_ASIGNAR_CONDICION,"Error validar Asignar Condición");
		
		descError.put(ERROR_OBTENER_CODIGOS_PRINCIPAL,"Error Obtener codigos principal");
		descError.put(ERROR_OBTENER_CODIGOS_DEPENDIENTES,"Error Obtener codigigos dependientes");
		descError.put(ERROR_DEPENDENCIA_CONDICION,"Error dependencia condición");
		
		descError.put(ERROR_OBTENER_CODIGOS_EXCLUYENTE,"Error Obtener codigos excluyente");
		descError.put(ERROR_EXCLUYENTE_CONDICION,"Error Excluyente condición");
		
		descError.put(ERROR_GUARDAR_MANEJA_PLAZOS,"Error Guardar Maneja Plazos");
		
		descError.put(ERROR_CONDICIONES_SIN_REGISTRAR,"No se pudo activar el producto debido que hay condiciones que no han completado su registro satisfactoriamente");

		descError.put(ERROR_PROCESAR_XML,"ERROR_PROCESAR_XML");
		
		
		descError.put(ERROR_GENERAR_ARRAY_LIST,"No se pudo convertir datos entre arreglo y lista");
		
		descError.put(ERROR_GENERA_CUENTA_GEN,"Error al generar cuenta");
		descError.put(ERROR_APERTURACUENTA_GEN,"Error al generar el alta al cuenta");
		descError.put(ERROR_CREACUENTA_GEN,"rror al crear el cuenta en la base de datos");
		descError.put(ERROR_GENARRAY_MAESTROCNTA_GEN,"Error al generar en memoria maestro de cuentas");
		descError.put(ERROR_GENARRAY_DATOSCNTA_GEN,"Error al generar datos en memoria para maestro de cuentas");
		descError.put(ERROR_GENARRAY_RESUMENCNTA_GEN,"Error al generar resumen de cuenta en memoria para cuenta");
		descError.put(ERROR_GENARRAY_TASASCNTA_GEN,"Error al generar tasas en memoria para cuenta");
		descError.put(ERROR_GENARRAY_PROCEJECNTA_GEN,"Error al generar procesos ejecutados en memoria para cuenta");
		descError.put(ERROR_OBTENER_RANGETARIFA_GEN,"Error al obtener rango de tarifa");
		descError.put(ERROR_OBTENER_TARIFA_GEN,"Error al obtener tarifa");
		descError.put(ERROR_MAESTROCNTA_SECUEN,"Error al generar la secuencia del maestro de cuentas");
		descError.put(ERROR_DATOSCNTA_SECUEN,"Error al generar la secuencia de datos del cuenta");
		descError.put(ERROR_RESUMENCNTA_SECUEN,"Error al generar la secuencia de resumen de cuenta de la cuenta");
		descError.put(ERROR_TASASCNTA_SECUEN,"Error al generar la secuencia de tasas del cuentas");
		descError.put(ERROR_PROCEJECNTA_SECUEN,"Error al generar la secuencia de procesos ejecutados del cuenta");
		descError.put(ERROR_APERTURACUENTA_CALLS,"Error de llamada en funcion APERTURA CUENTA");
		descError.put(ERROR_GENERACUENTA_CALLS,"Error de llamada en funcion GENERA cuenta");
		descError.put(ERROR_CREACUENTA_CALLS,"Error de llamada en funcion CREA cuenta");
		descError.put(ERROR_OBTIENETASAINTERESF_CALLS,"Error de llamada en funcion OBTENER TASAS DE INTERES");
		descError.put(ERROR_OBTIENETASASALL_CALLS,"Error de llamada en funcion OBTENER TARIFAS");
		descError.put(ERROR_OBTIENETASAINTERES_CALLS,"Error de llamada en funcion OBTENER TASA INTERES");
		descError.put(ERROR_OBTIENETASAAGREGADA_CALLS,"Error de llamada en funcion OBTENER TASA AGREGADA");
		descError.put(ERROR_OBTENER_TASAINT_MANY,"Se encontro mas de un concepto INTERES CUOTA");
		descError.put(ERROR_CONCEPTO_TARIFA,"No se encontro el concepto tarifa correspondiente");
		descError.put(ERROR_OBTENER_RANGETARIFA_MANY,"Hay mas de un rango de tarifa definida para el concepto");
		descError.put(ERROR_OBTENER_RANGETARIFA_NULL,"No hay rango de tarifa definida para el concepto");
		descError.put(ERROR_GENERACUENTA_PRIMERVCTO_VACIO,"Error de llamada en funcion GENERAR CUENTA, fecha primer vencimiento calculada esta vacia");
		descError.put(ERROR_GENERACUENTA_VCTOCUENTA_VACIO,"Error de llamada en funcion GENERAR CUENTA, fecha vencimiento del cuenta calculada esta vacia");
		descError.put(ERROR_GENERACUENTA_FACTAMORTCUENTA_VACIO,"Error de llamada en funcion GENERAR CUENTA, factor de amortizacion del cuenta calculado esta vacio");
		descError.put(ERROR_GENERACUENTA_ACT_MTOFIN,"Error de llamada en funcion GENERAR CUENTA, actualizando monto financiado");
		descError.put(ERROR_CREACUENTA_MAESTRO,"Error de llamada en funcion CREA CUENTA, no pudo crear el cuenta en la base de datos");
		descError.put(ERROR_CREACUENTA_DATOS,"Error de llamada en funcion CREA CUENTA, no pudo crear los datos del cuenta en la base de datos");
		descError.put(ERROR_CREACUENTA_TASAS,"Error de llamada en funcion CREA CUENTA, no pudo crear las tasas del cuenta en la base de datos");
		descError.put(ERROR_CREACUENTA_PROCES,"Error de llamada en funcion CREA CUENTA, no pudo crear el registro del proceso ejecutado del cuenta en la base de datos");
		descError.put(ERROR_OBTENER_TASAINT_CONVERT,"Error de llamada en funcion OBTENER TASA INTERES, no pudo convertir tasa tarifario a tasa aplicacion");
		descError.put(ERROR_OBTENER_TASAINT_PERIOD,"Error de llamada en funcion OBTENER TASA INTERES, no pudo calcular tasa periodo");
		descError.put(ERROR_OBTENER_TASAINT_DIARIA,"Error de llamada en funcion OBTENER TASA INTERES, no pudo calcular tasa diaria");
		descError.put(ERROR_OBTENER_TASAADD_CONVERT,"Error de llamada en funcion OBTENER TASA AGREGADA, no pudo convertir tasa tarifario a tasa aplicacion");
		descError.put(ERROR_OBTENER_TASAADD_PERIOD,"Error de llamada en funcion OBTENER TASA AGREGADA, no pudo calcular tasa periodo");
		descError.put(ERROR_OBTENER_TASAADD_DIARIA,"Error de llamada en funcion OBTENER TASA AGREGADA, no pudo calcular tasa diaria");
		descError.put(ERROR_OBTENER_TASAADD_CALCUL,"Error de llamada en funcion OBTENER TASA AGREGADA, no pudo calcular tasa agregada");
		descError.put(ERROR_CONCEPTO_SALDO_CAPITAL,"No existe concepto SALDO CAPITAL");
		descError.put(ERROR_CONCEPTO_TOTAL_CUOTA,"No existe concepto TOTAL CUOTA");
		descError.put(ERROR_OBTENER_DATOS_PRODUCTO,"No se encontro el producto");
		descError.put(ERROR_OBTENER_DATOS_FRECUENCIA,"No se encontro la frecuencia de pago para las cuotas");
		descError.put(ERROR_GENERAR_ARRAY_LIST,"No se pudo convertir datos entre arreglo y lista");
		descError.put(ERROR_TRANSACCAB_SECUEN,"Error al generar la secuencia de cabcera de transaccion del cuenta");
		descError.put(ERROR_TRANSACDET_SECUEN,"Error al generar la secuencia de detalle de transaccion del cuenta");
		descError.put(ERROR_TRANSACCUO_SECUEN,"Error al generar la secuencia de detalle cuotas de transaccion del cuenta");
		descError.put(ERROR_GENARRAY_TRXCAB_GEN,"Error al generar transaccion cabecera en memoria para cuenta");
		descError.put(ERROR_GENARRAY_TRXDET_GEN,"Error al generar transaccion detalle en memoria para cuenta");
		descError.put(ERROR_GENARRAY_TRXCUO_GEN,"Error al generar transaccion detalle cuotas en memoria para cuenta");
		descError.put(ERROR_CREA_TRX_GEN,"Error al generar transaccion");
		descError.put(ERROR_CREA_TRX_CALL,"Error de llamada en funcion CREA TRANSACCION");
		descError.put(ERROR_CREA_TRX_CAB,"Error de llamada en funcion CREA TRANSACCION, no pudo crear cabecera de transaccion del cuenta en la base de datos");
		descError.put(ERROR_CREA_TRX_DET,"Error de llamada en funcion CREA TRANSACCION, no pudo crear detalle de transaccion del cuenta en la base de datos");
		descError.put(ERROR_CREA_TRX_CUO,"Error de llamada en funcion CREA TRANSACCION, no pudo crear detalle cuotas de transaccion del cuenta en la base de datos");
		descError.put(ERROR_VALOR_TIPO_ESTATUS_ALTA,"No existe valor para TIPO ESTATUS ALTA");
		descError.put(ERROR_VALOR_CONDICION_CUENTA_NUEVO,"No existe valor para CONDICION cuenta NUEVO");
		descError.put(ERROR_VALOR_ESTADO_PROCESO_D,"No existe valor para ESTADO EN PROCESO D");
		descError.put(ERROR_CATA_ESTADO_CUENTA,"No existe codigo en tabla estado cuenta");
		descError.put(ERROR_CATA_ESTADO_CONTABLE,"No existe codigo en tabla estado contable");
		descError.put(ERROR_VALOR_ESTADO_TRANSACCION_ACTIVA,"No existe valor para ESTADO TRANSACCION ACTIVA");
		descError.put(ERROR_VALIDAR_FECHAVALOR1,"Fecha valor no puede ser vacia");
		descError.put(ERROR_VALIDAR_FECHAVALOR2,"Fecha valor no puede ser mayor a la fecha actual");
		descError.put(ERROR_VALIDAR_PRODUCTOACTIVO,"Producto no esta activo");
		descError.put(ERROR_VALIDAR_FRECUENCIA1,"Frecuencia de pago no puede ser vacia");
		descError.put(ERROR_VALIDAR_FRECUENCIA2,"Frecuencia de pago no permitida para el producto");
		descError.put(ERROR_VALIDAR_MONTOFIN1,"Monto a financiar tiene que ser mayor a cero");
		descError.put(ERROR_VALIDAR_MONTOFIN2,"Monto a financiar no permitido para el producto");
		descError.put(ERROR_VALIDAR_PLAZO1,"Plazo tiene que ser mayor a cero");
		descError.put(ERROR_VALIDAR_VALORES1,"No se ha ingresado monto solicitado");
		descError.put(ERROR_VALIDAR_VALORES2,"Monto ingresado tiene que ser mayor a cero");
		descError.put(ERROR_VALIDAR_FECDESEMBOLSO1,"Fecha de desembolso no puede ser vacia");
		descError.put(ERROR_VALIDAR_FECDESEMBOLSO2,"Fecha desembolso no puede ser mayor a la fecha actual");
		descError.put(ERROR_VALIDAR_MESESDOBLE1,"Producto no permite meses dobles");
		descError.put(ERROR_VALIDAR_MODCUOTA1,"Modalidad de cuota no ha sido definida");
		descError.put(ERROR_VALIDAR_PERGRACIA1,"Producto no permite periodo de gracia");
		descError.put(ERROR_VALIDAR_PERGRACIA2,"El periodo de gracia excede a lo permitido por el producto");
		descError.put(ERROR_VALIDAR_TITULAR1,"No se ha definido al titular del cuenta");
		descError.put(ERROR_VALIDAR_TITULAR2,"No se ha definido el nombre del titular del cuenta");
		descError.put(ERROR_VALIDAR_MONEDA1,"No se ha definido moneda");
		descError.put(ERROR_VALIDAR_MONEDA2,"Moneda no permitida para el producto");
		descError.put(ERROR_VALIDAR_NUMSOL,"Numero de solicitud no pueder ser vacia");
		descError.put(ERROR_GENERACUENTA_PLAZODIAS,"Error de llamada en funcion GENERAR CUENTA, plazo fuera del rango permitido para el producto");
		descError.put(ERROR_VALOR_ESTADO_PROCESO_P,"No existe valor para ESTADO EN PROCESO P");
		descError.put(ERROR_OBTENER_TIPO_FRECUENCIA,"Error al OBTENER TIPO FRECUENCIA");
		descError.put(ERROR_CONCEPTO_DESC_FRECUENCIA_PAGO,"No existe concepto DESCRIPCION FRECUENCIA PAGO");
		descError.put(ERROR_CONCEPTO_DIAS_FRECUENCIA_PAGO,"No existe concepto DIAS FRECUENCIA PAGO");
		descError.put(ERROR_CONCEPTO_FRECUENCIA_PAGO,"No existe concepto FRECUENCIA PAGO");
		descError.put(ERROR_CONCEPTO_NUM_DESEMBOLSOS,"No existe concepto NUMERO DESEMBOLSOS");
		descError.put(DOCUMENTO_YA_EXISTE,"El documento expediente ya existe");
		descError.put(SELECCIONE_DOCUMENTO,"Seleccione un documento expediente");
		descError.put(DOCUMENTO_NO_EXISTE,"El documento expediente no existe");
		descError.put(DOCUMENTO_DADO_BAJA,"El documento esta dado de baja");
		
		descError.put(ERROR_PERSONACNTA_SECUEN,"Error al generar la secuencia de persona cuenta");
		descError.put(ERROR_DATPERSONACNTA_SECUEN,"Error al generar la secuencia de datos persona cuenta");
		descError.put(ERROR_GENARRAY_PERSONACNTA_GEN,"Error al generar la persona en memoria para la cuenta");
		
		
		descError.put(ERROR_PARTICIPANTESCUENTA_GEN,"Error al generar particpantes para la cuenta.");
		descError.put(ERROR_ALTACUENTA_GEN,"Error al dar de alta a la cuenta.");
		descError.put(ERROR_PARTICIPANTESCNTA_CALLS,"Error en la llamada Participantes Cuenta.");
		descError.put(ERROR_ALTACNTA_CALLS,"Error en la llamada Alta Cuenta.");
		descError.put(ERROR_VALIDAR_CNTRL_PROCESO,"Error el validar control proceso.");
		descError.put(ERROR_OBTNER_PRE_REQUISITO,"Error al obtener el pre-requisito.");
		descError.put(ERROR_PRE_REQUISITO_NO_ENCONTRADO,"Error pre-requisito no encontrado.");
		descError.put(ERROR_OBTNER_CNTRL_PROCESO,"Error en control del proceso.");
		descError.put(ERROR_PRE_REQUISITO_NO_EJECUTADO,"Error se requiere ejecutar el pre-requisito primero.");
		

		descError.put(ERROR_OBTENER_DATOS_APERTURACNTA,"Error al obtener datos para apertura cuenta.");
		descError.put(ERROR_OBTENER_DATOS_PARTICIPANTECNTA,"Error al obtener datos para participantes cuenta .");
		descError.put(ERROR_OBTENER_DATOS_ALTACNTA,"Error al obtener datos para alta cuenta.");
		
		descError.put(ERROR_CNTRLPROCESOSCNTA_SECUEN,"Error al generar la secuencia del control de procesos para la cuenta.");
		descError.put(ERROR_GENARRAY_CNTRLPROCESOSCNTA_GEN,"Error al generar el array para control de procesos para la cuenta.");
		descError.put(ERROR_CREACNTRLPROCESOS_CALLS,"Error al llamar crear control procesos cuenta.");
		descError.put(ERROR_CREACNTRLPROCESOS_DATOS,"Error al obtener datos para el control procesos para la cuenta.");
		descError.put(ERROR_CREACNTRLPROCESOS_GEN,"Error al crear el control procesos para la cuenta.");
		
	
		descError.put(ERROR_ALTUALIZAR_TITULAR_CNTA,"Error de llamada en funcion actualizar titular cuenta, no pudo actualizar el titular la cuenta en la base de datos.");
		descError.put(ERROR_ALTUALIZAR_ESTADO_CNTA,"Error de llamada en funcion actualizar estado cuenta, no pudo actualizar el estado de la cuenta en la base de datos.");
		
		descError.put(ERROR_CAMPOS_REQUERIDOS,"Error se requieren más datos para procesar.");
		

		descError.put(ERROR_FILTRAR_PRODUCTO,"Error no se pudo filtar los producto.");
		descError.put(ERROR_LISTAR_CUENTAS,"Error al listar las cuentas.");
		descError.put(ERROR_LISTAR_PROCESOS,"Error al listar los procesos.");
		descError.put(ERROR_LISTA_CUENTA_SIN_RESULTADO,"No se encontraron cuentas.");
		
	 
		descError.put(ERROR_MONTO_APERTURA,"Error el en concepto de monto de apertura.");
		descError.put(ERROR_FECHA_APERTURA,"Error en el concepto fecha de apertura.");
		descError.put(ERROR_NUMERO_FIRMAS,"Error en el numero de firmar.");
		descError.put(ERROR_NUM_FIRMAS_OBL,"Error en el numero de firmas obligatorias.");
		
	 
		descError.put(ERROR_NUM_CNTA_REF_DEPOSITO,"Error en el numero referencia de la cuenta depsito.");
		descError.put(ERROR_IND_RENO_AUT,"Error en Indica Renovacion Automatica.");
		descError.put(ERROR_TIPO_CERTIFICADO,"Error en el tipo de certificado.");
		descError.put(ERROR_NUM_CERTIFICADO,"Error en el numero de certificado.");
		descError.put(ERROR_DIAS_RET_POST_APER,"Error en los dias de retiro post apertura.");
		descError.put(ERROR_FECHA_ULT_MOV_CNTA,"Error en la fecha del ultimo movimiento de la cuenta.");
		descError.put(ERROR_FECHA_ULT_CALC_CNTA,"Error en la fecha del ultimo calcuclo de la cuenta.");
		descError.put(ERROR_FECHA_ULT_PAGO_INT_CNTA,"Error en la fecha ultimo pago interes de la cuenta.");
		descError.put(ERROR_FECHA_ULT_CAPIT_CNTAS,"Error en la fecha ultimo capitalización cuenta.");
		descError.put(ERROR_FECHA_CANCELACION_CNTA,"Error fecha cancelacion cuenta.");
		descError.put(ERROR_FECHA_ULT_CAMB_TASA_CNTA,"Error fecha ultima cambio tasa cuenta.");
		descError.put(ERROR_IND_MEN_EDAD,"Error indica menor de edad.");
		descError.put(ERROR_FECHA_ULT_RENOVACION,"Error fecha ultima renovacion.");
		descError.put(ERROR_NUM_RENOVACION,"Error numero de renovacion.");
		descError.put(ERROR_MOTIVO_ANULACION,"Error motivo anulacion.");
		descError.put(ERROR_MOTIVO_CANCELACION,"Error motivo de cancelacion.");
		descError.put(ERROR_MOTIVO_ACTIVACION,"Error motivo activacion.");
		descError.put(ERROR_MOTIVO_INACTIVACION,"Error motivo inactivacion.");
		descError.put(ERROR_NUM_DIAS_PASA_INACT,"Error numero dias pasa a inactiva.");
		descError.put(ERROR_FECHA_VIG_PLAZO_FIJO,"Error fecha vigencia de plazo fijo.");
		
		descError.put(ERROR_NUM_DIAS_PLAZO_FIJO,"Error dias plazo.");
		descError.put(ERROR_TIPO_CONTROL_AHOPROG,"Error tipo de control.");
		descError.put(ERROR_MONTO_CONTROL_AHOPROG,"Error monto control.");
		descError.put(ERROR_DISPONIBILIDAD,"Error disponibilidad.");
		descError.put(ERROR_DESTINO_INTERES,"Error destino interes.");
		
		descError.put(ERROR_MONEDA,"Error al obtener la Moneda.");
		descError.put(ERROR_BASE_CALCULO,"Error al obterner  Base de calculo.");
		descError.put(ERROR_TASA_TARIFARIO,"Error al obtener Tasa tarifario.");
		descError.put(ERROR_TASA_APLICACION,"Error al obtener la Tasa aplicacion.");
		descError.put(ERROR_CONCEPTO_TASA,"Error al obtener Concepto tasa.");
		descError.put(ERROR_TIPO_TASA,"Error al obtener el Tipo de tasa.");
		descError.put(ERROR_TIPO_DISP_INTERES,"Error al obtener el Tipo de disposicion de interes.");
		descError.put(ERROR_FREC_CALC_INTERES,"Error al obtener la frecuencia del calculo de interes.");
		descError.put(ERROR_DIAS_FREC_CALC_INTERES,"Error al obtener los Dias frecuencia de calculo de interes.");
		descError.put(ERROR_FREC_PAGO_INTERES,"Error al obtener la Frecuencia de pago de interes.");
		descError.put(ERROR_DIAS_FREC_PAGO_INTERES,"Error al obtener los Dias de la frecuencia del pago de interes.");
		descError.put(ERROR_FREC_CAPITALIZACION,"Error al obtener la Freciencia de capitalizacion.");
		descError.put(ERROR_DIAS_FREC_CAPITALIZACION,"Error al obtener los dias de la Frecuencia de capitalizacion.");
		descError.put(ERROR_IND_GANA_INT,"Error al obtener si Gana interes.");

		descError.put(ERROR_IND_DEPOSITO_APERURA,"Error al obtener el Indicador  de Deposito minimo de apertura.");
		descError.put(ERROR_IND_MANEJA_PLAZOS,"Error al obtener el indicador de Maneja Plazos.");
		descError.put(ERROR_IND_PLAZO_FIJO,"Error al obtener el indicador de Plazo Fijo.");
		descError.put(ERROR_IND_MANEJA_SALDO_INTANG,"Error al obtener el indicador de Maneja saldo Intangible.");
		descError.put(ERROR_IND_MANEJA_SOBRE_GIRO,"Error al obtener el indicador de Maneja Sobre Giro.");
		descError.put(ERROR_IND_MANEJA_AHO_PROG,"Error al obtener el indicador de Maneja Ahorro Programado.");
		descError.put(ERROR_IND_CUENTA_HABERES,"Error al obtener el indicador de Cuenta Haberes.");
		
		descError.put(ERROR_HIDE_EMITE_CERT_DEPOS,"Error al obtener Emite Certidicador Deposito.");
		descError.put(ERROR_IND_EMITE_CHEQUERA,"Error al obtener el identificador de Emite Chequera.");
		descError.put(ERROR_IND_EMITE_ORDEN_PAGO,"Error al obtener el indicador de Emite Orden de Pago.");
		descError.put(ERROR_IND_COBRA_COMI,"Error al obtener el indicador de Cobra Comision.");
		descError.put(ERROR_HIDE_DIAS_INAMOV_APER,"Error al obtener Dias Inamovilidad a partir de la Apertura.");
		descError.put(ERROR_HIDE_RENOVACION_PLAZO,"Error al obtener Renovacion Plazo.");
		descError.put(ERROR_IND_PERMITE_DEPOS,"Error al obtener el indicador Permite Deposito.");
		descError.put(ERROR_IND_RECIBE_TRANSF,"Error al obtener el indicador Recibe Tranferencias.");
		descError.put(ERROR_IND_PERMITE_RETIROS_DISP,"Error al obtener el indicador Retiros sobre el Disponibles.");
		descError.put(ERROR_IND_PERMITE_TRANSF_DISP,"Eror al obtener el indicador Permite Tranferencias.");
		descError.put(ERROR_IND_PERMITE_DOMICI,"Error al obtener el indicador Permite Domiciliaciones.");
		descError.put(ERROR_IND_PASA_INACTIVA,"Error al obtener el indicador Pasa a Inactiva.");
		
		descError.put(ERROR_OBTENER_CONFIG_APERTURA,"Error al obtener los indicadores de configuracion para la cuenta.");
		
		descError.put(ERROR_ELIMINARCNTA_PARTICIPANTE,"Error eliminar los participantes de la cuenta.");
		
		
		descError.put(ERROR_OBTENER_TIPO_CONTROL_AHOPROG,"Error al obtener el Tipo de control de la cuenta.");
		descError.put(ERROR_OBTENER_MONTO_CONTROL_AHOPROG,"Error al obtener el monto de control de la cuenta.");
		descError.put(ERROR_OBTENER_NUM_DIAS_PLAZO_FIJO,"Error al obtener el Numero de Dias del Plazos.");
		descError.put(ERROR_OBTENER_MONTO_APERTURA,"Error al obtener el Monto de la cuenta.");
		descError.put(ERROR_OBTENER_DISPONIBILIDAD,"Error al obtener la Disponibilidad de la cuenta.");
		descError.put(ERROR_OBTENER_DESTINO_INTERES,"Error al obtener el Destino Interes de la cuenta.");
		
		
		
		
		descError.put(ERROR_OBTENER_DATOSCONFIGAPERTURA,"Error el obtener los Datos de Configuracion de la Apertura.");
		descError.put(ERROR_CODIGO_CUENTA,"Error el codigo de la cuenta es incoreccto.");
		
		descError.put(ERROR_LISTAR_PARTICIPANTES,"Error al listar los Participantes.");
		
		
		descError.put(ERROR_DATOS_CNTA,"Error al obtener DATOS CUENTA.");
		descError.put(ERROR_RESUMEN_CNTA,"Error al obtener RESUMEN CUENTA.");
		
		descError.put(ERROR_OBTENER_PORCENTAJE_DISPONIBILIDAD,"Error al obtener los datos de Porcentaje de Disponibilidad.");
		descError.put(ERROR_PORCENTAJE_DISPONIBILIDAD_NO_EXISTE,"Error Porcentaje de Disponibilidad no existe.");
		descError.put(ERROR_OBTENER_DATO_CATALOGO,"Error al obtener dato del Catalogo.");
		
		
		
		descError.put(ERROR_MONTO_CAPITAL,"Error con el Monto Capital.");
		descError.put(ERROR_MONTO_CONTABLE,"Error con el Monto Contable.");
		descError.put(ERROR_CAPITAL_DISPONIBLE,"Error con el Capital Disponible.");
		descError.put(ERROR_INTERES_DISPONIBLE,"Error con el Interes Disponible.");
		descError.put(ERROR_TOTAL_DISPONIBLE,"Error con el Total Disponible.");
		descError.put(ERROR_CAPITAL_INTANGIBLE,"Error con el Capital Intangible.");
		descError.put(ERROR_INTERES_INTANGIBLE,"Error con el Interes Intangible.");
		descError.put(ERROR_TOTAL_INTANGIBLE,"Error con el Total Intangible.");
		descError.put(ERROR_CAPITAL_BLQ_CTA,"Error con el Capital Bloqueo Cuenta.");
		descError.put(ERROR_INTERES_BLQ_CTA,"Error con el Interes Bloqueo Cuenta.");
		descError.put(ERROR_TOTAL_BLQ_CTA,"Error con el Total Bloqueo Cuenta.");
		descError.put(ERROR_INT_ULT_CALCULO_NO_BLQ,"Error con el Interes Ultimo Calculo no Bloqueado.");
		descError.put(ERROR_INT_ULT_CALCULO_BLQS,"Error con el Interes Ultimo Calculo Bloqueado.");
		descError.put(ERROR_INT_ULT_CALCULO_TOTAL,"Error con el Interes Ultimo Calculo Total.");
		descError.put(ERROR_INT_CALC_NUEVO_PERI,"Error con el Interes Calculo Nuevo Periodo.");
		descError.put(ERROR_INT_CALC_PERI,"Error con el Interes Calculo Periodo.");
		descError.put(ERROR_INT_TOTAL_PAGADO,"Error con el Interes Total Pagado.");
		descError.put(ERROR_INT_TOTAL_CAPITALIZADO,"Error con el Interes Total Capitalizado.");
		descError.put(ERROR_TOTAL_DEPOSITADO,"Error con el Total Depositado.");
		descError.put(ERROR_TOTAL_RETIRADO,"Error con el Total Retirado.");
		descError.put(ERROR_INT_TOTAL_RETIRADO,"Error con el Interes Total Retirado.");
		descError.put(ERROR_CAP_TOTAL_RETIRADO,"Error con el Interes Total Retirado.");
		
		
		descError.put(ERROR_ACTUA_DATOS_CNTA,"Error al actualizar Datos de la Cuenta.");
		descError.put(ERROR_ACTUA_RESUMEN_CNTA,"Error al actualizar Resumen Cuenta.");
		descError.put(ERROR_CREA_DEPOSITO_CALL,"Error al crear el Deposito.");
		
		descError.put(ERROR_INVKDEPOSITO_CALLS,"Error de llamada en funcion INVKDEPOSITO");
 
		descError.put(ERROR_VALOR_OPERACION_DEPOSITO,"No existe valor DEPOSITO para OPERACION");
		descError.put(ERROR_VALOR_CONCEPTO_TRANSACCION_DEPOSITO,"No existe valor DEPOSITO para CONCEPTO TRANSACCION");
		
		
		
		descError.put(ERROR_TOTAL_CAPITAL,"Error en Total Capital");
		descError.put(ERROR_TOTAL_INTERES,"Error en Total Interes");
		descError.put(ERROR_TOTAL_SALDO,"Error Total Saldo");
		descError.put(ERROR_MONTO_ULTIMA_TRX,"Error Monto Ultima Transaccion");
		descError.put(ERROR_ULT_REMUNE,"Error Ulima Remuneracion");
		descError.put(ERROR_ULTIMO_FACTOR_DISPONIBLE,"Error Ultimo Factor Disponible");
		descError.put(ERROR_MONTO_ULTIMA_TRX_DISPO,"Error Monto Ultima Transaccion Disponible");
		descError.put(ERROR_MONTO_ULTIMA_TRX_INTANG,"Error Monto Ultima Transaccion Intangible");
		descError.put(ERROR_MONTO_TRX,"Error Monto Transaccion");
		descError.put(ERROR_FACTOR_DISPONIBILIDAD,"Error Factor Disponible");
		
		descError.put(ERROR_DEPOSITO_CALLS,"Error de llamada en funcion Deposito");
		
		descError.put(ERROR_PRODUCTO,"No se ha definido el producto");
		
		descError.put(ERROR_CALCTRANS,"Error, no se encontraron datos en Calculo Transaccion");
		descError.put(ERROR_CONCEPXTRANS,"Error, no se encontraron datos en Conceptos por Transaccion");
		
		descError.put(ERROR_CREA_CABE_DETAL_TRX,"Error al crear cabecera y detalle para la transaccion ");
		descError.put(ERROR_CREA_CABE_DETAL_TRX_CALL,"Error al llamar la funcion GENERA CABECERA Y DETALLE TRANSACCION");
		
		descError.put(ERROR_OBTENER_TARIFAS,"Error obtener Tarifas");
		descError.put(ERROR_OBTENER_TARIFAS_GEN,"Error al generar el tarifas para el producto de ahorro");
		
		descError.put(ERROR_OBTENER_TASASALL_GEN,"Error en la funcion obtiene TASASALL");
		descError.put(ERROR_OBTENER_TASAAGREGADA_GEN,"Error en la funcion obtiene TASA AGREGADA");
		
		descError.put(ERROR_MOVDEPOSITO_CALL,"Error al llamar a la funcion MOVDEPOSITO");
		descError.put(ERROR_MOVDEPOSITO_GEN,"Error en la funcion MOVDEPOSITO");
		descError.put(ERROR_DEPOSITO_GEN,"Error en la funcion DEPOSITO");
		descError.put(ERROR_MOVRETIRO_GEN,"Error en la funcion MOVRETIRO ");
		descError.put(ERROR_RETIRO_GEN,"Error en la funcion RETIRO");
		descError.put(ERROR_MOVRETIRO_CALL,"Error al llamar a la funcion MOVRETIRO");
		
		descError.put(ERROR_MOVCUENTA_CALL,"Error al llamar a la funcion MOVCUENTA");
		descError.put(ERROR_MOVCUENTA_GEN,"Error en la funcion MOVCUENTA");
		
		descError.put(ERROR_VALOR_OPERACION_RETIRO,"Error no se encontro RETIRO en el catalogo OPERACION");
		descError.put(ERROR_VALOR_CONCEPTO_TRANSACCION_RETIRO,"Error no se encontro RETIRO en el catalogo CONCEPTO TRANSACCION ");
		
		descError.put(ERROR_OBTENER_CONCEP_OPERACION,"Error en la funcion obtiene CONCEPTO OPERACION");
		
		descError.put(ERROR_TRANSF_PROPIAS_GEN,"Error en la funcion TRANSFERENCIA PROPIAS");
		descError.put(ERROR_TRANSF_PROPIAS_CALL,"Error al llamar la funcion TRANSFERENCIA PROPIAS");
		descError.put(ERROR_DEPOSITO_CALL,"Error al llamar la funcion DEPOSITO");
		descError.put(ERROR_RETIRO_CALL,"Error al llamar la funcion RETIRO");
		descError.put(ERROR_NOTA_CARGO_GEN,"Error en la funcion NOTA CARGO");
		descError.put(ERROR_NOTA_CARGO_CALL,"Error al llamar la funcion NOTA CARGO");
		descError.put(ERROR_NOTA_ABONO_CALL,"Error al llamar la funcion NOTA ABONO");
		descError.put(ERROR_NOTA_ABONO_GEN,"Error en la funcion NOTA ABONO");
		
		descError.put(ERROR_OBTENER_DATOS_RETIRO_CALL,"Error al obtener datos para el RETIRO");
		
		
		descError.put(ERROR_TRANSF_TERCEROS_GEN,"Error en la funcion TRANSFERENCIA TERCERTOS");
		descError.put(ERROR_TRANSF_TERCEROS_CALL,"Error al llamar la funcion TRANSFERENCIA TERCEROS");
		
		descError.put(ERROR_VALOR_OPERACION_NTCARGO,"Error no se encontro el valor NOTA DE CARGO en el catalogo OPERACION");
		descError.put(ERROR_VALOR_OPERACION_NTABONO,"Error no se encontro el valor NOTA DE ABONO en el catalogo OPERACION");
		
		descError.put(ERROR_VALOR_OPERACION_TRANSF_PROPIAS,"Error no se encontro el valor TRANSFERENCIA PROPIAS en el catalogo OPERACION");
		descError.put(ERROR_VALOR_OPERACION_TRANSF_TERCEROS,"Error no se encontro el valor TRANSFERENCIA TERCEROS en el catalogo OPERACION");
		descError.put(ERROR_OBTENER_CONCEP_OPERACION_XGRPOCLAVE,"Error al obtener Conceptos de Operacion por grupo concepto clave");
		descError.put(ERROR_EMPRESA_REQUERIDO,"Codigo de empresa requerido");
		descError.put(ERROR_MONTO_REQUERIDO,"Monto es requerido");
		descError.put(ERROR_CONSULTA_SALDO,"Error al consultar saldo");
		descError.put(ERROR_CONSULTA_SALDO_CON,"Error al consultar saldo contable");
		
		descError.put(ERROR_PRODUCTO_SIN_CONDICIONES,"Para dar de alta a este producto tienes que haber configurado alguna condicion");
		descError.put(ERROR_TARIFA_INACTIVA,"Error Tarfica de interes inactiva");
		descError.put(ERROR_YA_EXISTE_CONCEPTO_TASA,"Ya existe concepto de tasa");
		descError.put(ERROR_IMPORTEID_NULL,"Importe comision Id es  null o cero");
		
		descError.put(ERROR_MONTO_DEPOSITO_VACIO,"Aun no ha registrado Montos");
		descError.put(ERROR_SALDO_PROMEDIO_VACIO,"Aun no ha registrado Saldos");
		descError.put(ERROR_AHORRO_PROGRAMADO_NO_EXISTE,"No se encontro el Ahorro Programado");
		descError.put(ERROR_ACTUALIZAR_INDSALDO_AHORRO_PROG,"Error al actualizar Indicador por Saldo en ahorro programado");
		descError.put(ERROR_ACTUALIZAR_INDMONTO_AHORRO_PROG,"Error al actualizar Indicador por monto en ahorro programado");
		
		descError.put(VALOR_NEGATIVO,"Campo negativo");
		descError.put(DIA_PAGO_VACIO,"Tiene que seleccionar dia(s) de pago");
		descError.put(DIA_PAGO_DETERNIMADO,"Seleccione el dia determinada para el pago antes/despues del vencimiento");
		descError.put(DIA_INCORRECTO,"Dia no puede ser menor a 1 o mayor a 31");
		descError.put(ERROR_CREAR_TIPO_INTERES_DIA_PAGO,"Error al guardar el Dia de pago para el Tipo de interes");
		descError.put(ERROR_ELIMINAR_TIPO_INTERES_DIA_PAGO,"Error al eliminar  el Dia de pago para el Tipo de interes");
		
		
		descError.put(ERROR_RANG_MONTO_TARIF_INT,"Error al obtener el Rango monto tarifario");
		descError.put(ERROR_CORTE_INT,"Error al obtener el tipo de Corte para el tipo de interes");
		descError.put(ERROR_DIA_CORTE_INT,"Error al obtener el dia del corte del tipo de interes");
		
		descError.put(ERROR_OBTNER_OPERACION_APERTURACNTA,"Error al obtener la operacion: APERTURA CUENTA");
		descError.put(ERROR_OBTENER_CONCEPTRANSAC_APERTURACNTA,"Error al obtener el concepto transaccion: APERTURA CUENTA");
		descError.put(ERROR_OBTENER_ESTADOTRANSAC_ACTIVO,"Error al obtener el estado del transaccion: ACTIVO");
		
		descError.put(ERROR_PRODUCTO_DESTINO_NULL,"No indico cuenta existente para deposito ni producto para crear cuenta de deposito");
		descError.put(ERROR_NROCUENTAEXISTENTE_NULL,"Nro de cuenta existente no debe ser NULL");
		
		descError.put(ERROR_NRO_CNTA_EXIST,"Error al obtener el Numero de cuenta existente");
		descError.put(ERROR_CALCULAR_INTERES,"Error al calcular el interes");
		
		descError.put(SALDOCALCULOINTERES_NULL,"Campo requerido Saldo calculo interes");
		
		descError.put(ERROR_SALDO_CALCULO_INTERES,"Error obtener Saldo calculo interes");
		
		descError.put(ERROR_ACTUAL_CALCULO_INTERES,"Error al acutalizar el calculo de interes");
		
		descError.put(ERROR_CALCULO_INTERES_CALLS,"Error al llamar a la funcion el calculo de interes");
		descError.put(ERROR_ACTUALIZA_CALCULO_INTERES_CALLS,"Error al llamar a la funcion acutalizar el calculo de interes");
		descError.put(ERROR_FECHA_VENC_PLAZO_FIJO,"Error al llamar a la funcion el calculo de interes");
		descError.put(ERROR_LIQUIDACION_INTERES_CALLS,"Error al llamar a la funcion Liquidacion interes");
		
		descError.put(DECIMALFACTOR_NULL,"Numero decimal para Factor de interes requerido");
		descError.put(DECIMALINTERES_NULL,"Numero decimal para el Interes requerido");
		descError.put(DECIMALLIQUIDACION_NULL,"Numero decimal para la Lquidacion del interes requerido");
		
		descError.put(ERROR_CALCCAPITALIZACION_INTERES_CALLS,"Error al llamar a la funcion Calculo Capitalizacion Interes");
		descError.put(ERROR_CALCPAGO_INTERES_CALLS,"Error al llamar a la funcion Calculo Pago Interes");
		descError.put(ERROR_CONCEP_OPERACION_XGRPOYTIPOCLAVE,"Error al obtener los conceptos de operacion por grupo y tipo de concepto");
		
		descError.put(ERROR_VALOR_OPERACION_PAGO_INTERES,"Error al obtener el valor de la operacion Pago Interes");
		descError.put(ERROR_MOVPAGOINT_CALL,"Error al llamar al metodo movPagoInt");
		descError.put(ERROR_PAGO_INT_GEN,"Error al llamar al metodo PagoInt General");
		
		descError.put(ERROR_OBTENER_DATOS_BATCH_CNTA,"Error obtener los datos de la cuenta en el proceso batch");
		
		descError.put(ERROR_PROCCNTA_PRINCIPAL_GEN,"Fallo en el procesamiento principal de la cuenta");
		
		descError.put(ERROR_OBT_DECIMAL_FACTOR,"Error obtener el numero decimal para el factor de interes");
		descError.put(ERROR_OBT_DECIMAL_INTERES,"Error obtener el numero decimal para el calculo de interes");
		descError.put(ERROR_OBT_DECIMAL_LIQUIDACION,"Error obtener el numero decimal para la liquidacion de interes");
		
		descError.put(ERROR_OBT_SALDO_PROMEDIO,"Error al obtener el Saldo promedio");
		descError.put(ERROR_TASAS_CNTA,"Error al obtener el Tasas de la cuenta");
		
		descError.put(ERROR_PROCCNTA_ACTBDGEN_GEN,"Fallo al grabar datos generales de la cuenta en la BD");
		

		descError.put(ERROR_PAGO_INTERES_MENOR_A_MES,"Frecunecia de pago de Interes no puede ser menor a UN MES");
		descError.put(ERROR_FRECCAP_DIARIA_O_FRECPAG,"Frecuencia de Capitalizacion tiene que ser DIARIA o igual a la Frecuencia Pago de Interes");
		descError.put(ERROR_FRECCAL_DIARIA_O_FRECCAP,"Frecuencia de Calculo de Interes tiene que ser diaria o igual a la Frecuencia de Capitalizacion");
		descError.put(ERROR_FRECCAL_DIARIA_O_FRECPAG,"Frecunecia de Calculo de Interes tiene que ser DIARIA o igual a la Frecuencia Pago de Interes");
		descError.put(ERROR_PAGO_INTERES_MENOR_A_SEMANAL,"Frecunecia Pago de Interes no puede ser menor a SEMANAL");
		descError.put(DIA1_DIA2_REQUERIDOS,"Los dias 1 y 2 del corte para el Pago de Interes son requeridos");
		descError.put(DIA1_DIA2_NOMAYOR_30,"Los dias 1 y 2 del corte no tienen que ser mayor a 30");
		descError.put(DIAS_CORTE_PAGO_DIFE_FRECPAG,"Los dias del corte de pago no conciden con la Frecuencia de Pago");
		descError.put(ERROR_FRECCAP_MAYOR_A_FRECPAG,"Frecuencia de Capitalizacion no puede ser mayor a la  Frecunecia de Pago");
		descError.put(ERROR_FRECCAL_MAYOR_A_FRECCAP,"Frecuencia de Calculo de Interes no puede ser mayor a la Frecunecia de Capitalizacion");
		descError.put(ERROR_FRECCAL_MAYOR_A_FRECPAG,"Frecuencia de Calculo de Interes no puede ser mayor a la Frecunecia de Pago");
		
		descError.put(DISPOSICION_INTERES_REQUERIDO,"Disposicion de Interes campo requerido");
		
		descError.put(FRECCAL_REQUERIDO,"Frecuencia Calculo Interes campo requerido");
		descError.put(FRECCAP_REQUERIDO,"Frecuencia Capitalizacion Interes campo requerido");
		descError.put(FRECPAG_REQUERIDO,"Frecuencia Pago Interes campo requerido");
		descError.put(DIA1_REQUERIDOS,"Dia 1 de pagos es requerido");
		
		descError.put(ERROR_SALDOS_MENSUAL_SECUEN,"Error al generar la secuencia para el Saldo Mensual");
		descError.put(ERROR_GENARRAY_SALDOS_MENSUAL_GEN,"Error al llamar a la funcion generaArraySaldoMensual");
		descError.put(ERROR_SALDOS_DIARIOS_SECUEN,"Error al generar la secuencia para el Saldo diario");
		descError.put(ERROR_GENARRAY_SALDOS_DIARIOS_GEN,"Error al llamar a la funcion generaArraySaldoDiario");
		
		
		descError.put(ERROR_ACTUA_SALDO_DIARIO,"Error al actualizar el saldo diario");
		descError.put(ERROR_CREAR_SALDO_DIARIO,"Error al crear el saldo diario");
		
		descError.put(ERROR_OBTIENE_SALDOSXDIA_CALLS,"Error al llamar a la funcion obtieneSaldosPorDia");
		descError.put(ERROR_GENARRAY_SALDOS_MENSUAL_CALLS,"Error al llamar a la funcion generaArraySaldosMensual");
		descError.put(ERROR_CREAR_SALDOSMENSUAL,"Error al crear el saldo mensual");
		
		descError.put(ERROR_OBTENER_SALDOS_DIARIOS,"Error al obtener el saldo diario");
		descError.put(ERROR_OBTENER_MAXSALDOSMESUALESID,"Error al obtener el Id del maximo saldo mensual");
		descError.put(ERROR_OBTENER_SALDO_MESUAL,"Error al obtener el saldo mensual");
		
		descError.put(ERROR_VALOR_CONCEPTO_TRANSACCION_PAGINTERES,"Error no se encontro el concepto transaccion para el Pago de interes");
		descError.put(ERROR_INVKPAGOINTERES_CALLS,"Error al llamar a la funcion INVKPAGOINTERES");
		descError.put(ERROR_OBTIENE_DATOS_DEPOSITO_CALLS,"Error al llamar a la funcion obtieneDatosDeposito");
		descError.put(ERROR_OBTIENE_DATOS_PAGOINT_CALLS,"Error al llamar a la funcion obtieneDatosPagaInteres");
		descError.put(ERROR_TARIFASINTERES_NULL,"Error no se encontraron tarifas");
		
		
		descError.put(ERROR_GENERA_SALDOSMENSUALES,"Error al generar el Saldo mensual");
		descError.put(ERROR_GENERA_SALDOSMENS_CALLS,"Error al llamar a la funcion genera Saldo Mensual");
		
		descError.put(ERROR_TRXPAGO_INTERES_CALLS,"Error en la Transaccion Pago Interes CALLS");
		
		descError.put(ERROR_OBTENER_OPERACION,"Error al obtener el codigo de Operacion");
		descError.put(ERROR_OBTENER_EXPRESIONCALC,"Error al obtener la lista de expresiones de Calculo");
		descError.put(ERROR_EXPRESIONCALC_NOEXISTE,"No existe encontraron expresiones de Calculo");
		descError.put(ERROR_ALORDENAR_EXPRESIONCALC,"Error al ordenar las expresiones de Calculo");
		descError.put(ERROR_INVKOBTIENEEXPRESIONCALC_CALLS,"Error al llamar a la funcion obtieneExpresionCalculo CALLS");
		
		descError.put(ERROR_OBTIENE_EXPRESIONCALC_CALCINT_CALLS,"Error al obtener las expresiones de calculo para el Calculo de Interes");
		
		
		descError.put(ERROR_PROCESEXPRESION,"Error al procesar expresionesde calculo");
		
		descError.put(ERROR_PROCESA_EXPRESIONCALC_CALLS,"Error al procesar expresionesde calculo");
		descError.put(ERROR_OBTIENE_EXPRESIONCALC_CAPIINT_CALLS,"Error al procesar expresionesde calculo");
		descError.put(ERROR_OBTIENE_EXPRESIONCALC_PAGOINT_CALLS,"Error al obtener");
		
		descError.put(ERROR_IND_FREC_CAPITALIZACION,"Error obtener el indicador de capitalizacion");
		
		descError.put(ERROR_MOVCAPITALIZACION_GEN,"Error en la funcion MOVCAPITALIZACIONINTERES");
		descError.put(ERROR_OBTIENE_DATOS_CAPIINT_CALLS,"Error al llamar a la funcion obtieneDatosCapitalizacionInteres");
		descError.put(ERROR_MOVPAGOINTERES_GEN,"Error en la funcion MOVPAGOINTERES");
		
		descError.put(ERROR_VALOR_OPERACION_CAPITALIZACION_INTERES,"Error obtener el codigo de operacion para Capitalizacion Interes");
		descError.put(ERROR_MOVCAPIINT_CALL,"Error al llamar a la funcion MOVCAPITALIZACIONINTERES");
		descError.put(ERROR_CAPI_INT_GEN,"Error en la funcion CAPITALIZACIONINTERES");
		
		descError.put(ERROR_CAPITALIZACION_CALL,"Error en la funcion CAPITALIZACION INTERES");
		
		
		
		descError.put(ERROR_OBTENER_DATOS_CAPIINTERES_CALLS,"Error en la funcion obtieneDatosCapitalizacionInteres");
		descError.put(ERROR_OBTENER_DATOS_PAGOINTERES_CALLS,"Error en la funcion obtieneDatosPagoInteres");
		descError.put(ERROR_VALOR_OPERACION_PASAINACTCNTA,"Error al obtener la operacion para Pasa a Inactiva Cuenta");
		descError.put(ERROR_VALOR_CONCEPTO_TRANSACCION_PASAINACTCNTA,"Error al obtener el concepto transaccion para Pasa Inactiva Cuenta");
		descError.put(ERROR_OBTIENE_DATOS_PASAINACTCNTA_CALLS,"Error en la funcion obtieneDatosPasaInactiva");
		
		descError.put(ERROR_VALOR_CONCEPTO_TRANSACCION_CAPIINTERES,"Error al obtener el concepto transaccion para la Capitalizacion Interes");
	
		descError.put(ERROR_TRXPASAINACTIVA_CALLS,"Error en la transaccion Pasa Inactiva");
		descError.put(ERROR_ACTUAL_INDESTADOCUENTA_GEN,"Error en la funcion actualizaIndEstadoCuenta");
		descError.put(ERROR_ACTUALIZAR_INDESTADO_CNTA,"Error al actualizar el  indicar de estado de la cuenta");
		
		
		descError.put(ERROR_MOVPASAINACT_CALL,"Error al llamar a la funcion MOVPASAINACTIVA");
		descError.put(ERROR_PASAINAC_GEN,"Error en la transaccion PASA INACTIVA");
		descError.put(ERROR_MOVGENERICO_CALL,"Error al llamar a la funcion MOVGENERICO");
		descError.put(ERROR_MOVPASAINACTIVA_GEN,"Error en la funcion MOVPASAINACTIVA");
		descError.put(ERROR_PASAINACTIVA_GEN,"Error en la funcion Pasa Inactiva");
		
		descError.put(ERROR_TIPOINTERES_CANCELACION,"Seleccione el tipo de interes para la cancelacion");
		
		
		descError.put(ERROR_OBTENER_DATOS_CNTAPLAZO_GEN,"Error en la funcion obtieneDatosCuentaPLazo");
		descError.put(ERROR_CREAR_CRONOPLAZOFIJO_GEN,"Error en la funcion creaCronogramaPlazoFijo");
		descError.put(ERROR_OBTIENE_DATOS_CNTAPLAZO_CALLS,"Error al llamar a la funcion obtieneDatosCuentaPLazo");
		descError.put(ERROR_CREA_CRONOPLAZOFIJO_CALLS,"Error al llamar a la funcion creaCronogramaPlazoFijo");
		descError.put(ERROR_CREAR_CRONOPLAZO_GEN,"Error en la funcion creaCronograma");
		descError.put(ERROR_OBTENER_CRONOPAGOS,"Error al obtener los Cronogramas de Pagos para la cuenta");
		descError.put(ERROR_OBTENER_ULT_CRONOPAGO_GEN,"Error en la funcion obtieneUltimoPago");
		descError.put(ERROR_ACTUAL_CRONOPLAZOFIJO_GEN,"Error en la funcion actualizaCronogramaPlazoFijo");
		descError.put(ERROR_ELIMINA_CRONOGRAMAPLAZO,"Error al eliminar el Cronograma de pago de la cuenta");
		descError.put(ERROR_OBTIENE_ULTIMO_CRONOPAGO_CALLS,"Error al llamar a la en la funcion obtieneUltimoPago");
		descError.put(ERROR_OBTENER_IMPORTE_ACTUAL,"Error al obtener el improte actual de la cuenta");
		descError.put(ERROR_ACTUAL_CRONOPLAZOFIJO_CALLS,"Error al llamar en la funcion actualizaCronogramaPlazoFijo");
		descError.put(ERROR_ACTUAL_CRONOPLAZO_GEN,"Error en la funcion actualizaCronogramaPlazoFijo");
		descError.put(ERROR_ACTUAL_CRONOPLAZO_CALLS,"Error al llamar a la funcion actualizaCronograma");
		descError.put(ERROR_CREA_CRONOPLAZO_CALLS,"Error al llamar a la funcion creaCromograma");
		
		descError.put(RANGO_TARIFA_NO_EXISTE,"No existe el rango manto para el numero de Dia(s)");
		
		descError.put(ERROR_CRONOPAGOCNTA_SECUEN,"Error al generar la secuencia para el Cronograma de Pago");
		descError.put(ERROR_GENARRAY_CRONOCNTA_GEN,"Error al llamar a la funcion generaArrayCronogramaPago");
		descError.put(ERROR_GENARRAY_CRONOCNTA_CALLS,"Error en la funcion generaArrayCronogramaPago");
		
		descError.put(ERROR_CREAR_CRONOPLAZO,"Error al guardar el Cronograma de Pago a la DB");
		
		descError.put(ERROR_LIQUIDACION_INTERES_GEN,"Error en la funcion LiquidacionInteres");
		
		
		descError.put(ERROR_CALCINTERES_GEN,"Error en la funcion Calculo Interes");
		descError.put(ERROR_CALCCAPITALIZACION_GEN,"Error en la funcion Calculo Capitalizacion Interes");
		descError.put(ERROR_CALCPAGOINTERES_GEN,"Error en la funcion Calculo Pago Interes");
		
		descError.put(ERROR_OBTENER_FRECENCIA,"Error al obtener el Tipo de Frecuencia");
		descError.put(ERROR_FRECUENCIA_NOEXISTE,"Tipo de Frecuencia no existe");
		
		descError.put(ERROR_CORTE_PRODUCTO_NORMAL,"El corte de pago No puede ser Segun Fecha Apertura");
		
		descError.put(ERROR_REGISTRO_CRONOPAGO,"Error al obtener el registro del cronograma de pago");
		
		descError.put(ERROR_OBTENER_CODIGO_OPERACION,"Error al obtener el codigo de Operacion");
		descError.put(ERROR_OBTENER_CODIGO_CONCEP_TRANSAC,"Error al obtener el codigo de Concepto Transaccion");
		
		descError.put(ERROR_LIQUIDA_INTERES_CALLS,"Error en la llamada del metodo Liquidacion Interes");
		
		descError.put(ERROR_CNTA_ACTBDGEN_GEN,"Error en la funcion para actualizar los datos/resumen de la cuenta");
		descError.put(ERROR_CNTA_ACTBDGEN_CALLS,"Error al llamar ala funcion que actualiza los datos/resumen de la cuenta");
		
		descError.put(ERROR_OBTENER_CONCEPTOS_OPERACION_GEN,"Error obtener Conceptos de Operacion");

		descError.put(ERROR_NROCUENTA_DESTINO_NULL,"Error el Numero de cuenta destino para el interes es nulo");

		descError.put(ERROR_ESTADO_CUENTA,"Error en el ESTADO CUENTA.");
		
		descError.put(ERROR_TRXTRNS_PROPIAS_CALLS,"Error en la Transaccion Transferencia propia CALLS.");
		
		
		descError.put(ERROR_OBTENER_DATOS_TRANSF_CALL,"Error al llamar a la obtieneDatosTransf");
		descError.put(ERROR_OBTENER_DATOS_NOTAS_CALL,"Error al llamar a la funcion obtieneDatosNotas.");
		descError.put(ERROR_OBTENER_DATOS_TRANSF_GEN,"Error en la funcion obtieneDatosTransf");
		descError.put(ERROR_OBTENER_DATOS_NOTAS_GEN,"Error en la funcion obtieneDatosNotas.");
		descError.put(ERROR_OBTIENE_DATOS_RETIRO_CALLS,"Error al llamar a la funcion obtieneDAtosRetiro.");
		
		
		descError.put(ERROR_VALOR_OPERACION_APERTURA_CNTA,"Error al obtener la codigo de operacion para  la Apertura de cuenta.");
		descError.put(ERROR_VALOR_OPERACION_CALCULO_INTERES,"Error al obtener el codigo de operacion para el Calculo de interes");
		
		descError.put(ERROR_OBTENER_DATOS_DEPOSITO_CALL,"Error al llamar a la funcion obtieneDatosDeposito.");
		
		
		descError.put(ERROR_TRXNOTACARGO_CALLS,"Error en la Transaccion Nota de Cargo CALLS.");

		
		descError.put(ERROR_CREA_CRONO_CANCEL_PLAZOFIJO_CALLS,"Error al llamar a la funcion crea cronograma cancelacion plazo fijo.");
		descError.put(ERROR_CREAR_CANCEL_CRONOPLAZO,"Error al guarda el cronograma de cancelacion del plazo fijo en la BD");
		
		
		
		descError.put(ERROR_OBTENER_DATOS_CANCELCNTA,"Error obtener datos para la cancelacion de la cuenta");
		descError.put(ERROR_OBTENER_DATOS_CANCELCNTA_GEN,"Error en la funciones obtener datos cuenta");
		descError.put(ERROR_OBTENER_DATOS_CANCELCNTA_CALLS,"Error al llamar a la funcione obtener datos cuenta CALLS.");
		
		
		descError.put(ERROR_CONFIG_CANCELPLZ_NOEXISTE,"Error no se encontro la configuracion para la Cancelacion del Plazo Fijo");
		
		
		
		descError.put(ERROR_VALOR_OPERACION_RENOVAR_CNTA,"Error al obtener la codigo de operacion para Renovacion de cuenta.");
		
		
		
		
		descError.put(ERROR_ACTUALIZA_CRONOPLAZOFIJO_CALLS,"Error al llamar a la funcion actualiza cronograma plazo fijo");
		descError.put(ERROR_ACTUALIZA_CRONOPLAZO_GEN,"Error en la funcion actualiza cronograma plazo fijo");
		
		
		
		descError.put(ERROR_FECHA_ULT_PRO_CIERRE,"Error en Fecha ultimo proceso de cierre.");
		descError.put(ERROR_FECHA_ULT_VAL_CIERRE,"Error en Fecha ultimo valor cierre.");
		descError.put(ERROR_FECHA_ULT_COR_CIERRE,"Error en Fecha ultimo corte cierre.");
		
		
		
		descError.put(ERROR_CREA_SALDIA_CALL,"Error llamar la funcion Crea Saldo Diario.");
		descError.put(ERROR_ACT_SALDIA_CALL,"Error llamar la funcion Actualiza Saldo Diario.");
		
		descError.put(ERROR_CREA_SALDIA_GEN,"Error en la funcion Crea Saldo Diario.");
		descError.put(ERROR_ACT_SALDIA_GEN,"Error en la funcion Actualiza Saldo Diario.");
		descError.put(ERROR_SALDO_DIARIO_GEN,"Error en la funcion saldoDiario.");
		descError.put(ERROR_SALDO_DIARIO_CALL,"Error al llamar a la funcion saldoDiario.");
		
		
	}
}