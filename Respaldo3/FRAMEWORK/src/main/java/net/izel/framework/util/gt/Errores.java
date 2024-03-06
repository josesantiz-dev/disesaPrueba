
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de errores en servicios
*
*/

package net.izel.framework.util.gt;

import java.util.HashMap;

public class Errores {

	/*TRANSACCION_EXITOSA*/
	public static final long TRANSACCION_EXITOSA = 0;

	/*CAMPO %s REQUERIDO*/
	public static final long CAPTURE_CAMPOS_REQUERIDOS = 1;

	/*PRIMERO BUSQUE Y SELECCIONE UNA GARANTIA*/
	public static final long PRIMERO_BUSQUE_Y_SELECCIONE_UNA_GARANTIA = 3;

	/*Registro ya ha sido dado de baja*/
	public static final long REGISTRO_INACTIVO = 4;

	/*REGISTRO_DAR_BAJA_ES_UNICO_ACTIVO*/
	public static final long REGISTRO_DAR_BAJA_ES_UNICO_ACTIVO = 6;

	/*VERIFIQUE, EL DETALLE DE LOS BIENES NO COINCIDE CON LA CANTIDAD INDICADA EN EL CAMPO NO. BIENES*/
	public static final long VERIFIQUE_DETALLE_LOS_BIENES_NO_COINCIDE_CON_NO_BIENES = 8;

	/*VERIFIQUE,  LA SECUENCIA DE LAS FECHAS CAPTURADAS ESTAN INVERTIDAS*/
	public static final long VERIFIQUE_SECUENCIA_FECHAS_CAPTURADAS_INVERTIDAS = 11;

	/*CAPTURE_AL_MENOS_UNA_PROPIETARIO*/
	public static final long CAPTURE_AL_MENOS_UNA_PROPIETARIO = 13;

	/*CAPTURE_DOMICILIO_GARANTIA*/
	public static final long CAPTURE_DOMICILIO_GARANTIA = 15;

	/*NO ES POSIBLE ANULAR LA GARANTIA, ESTA YA HA SIDO ACTIVADA.*/
	public static final long NO_ES_POSIBLE_ANULAR_GARANTIA_ESTA_ACTIVADA = 16;

	/*ERROR INESPERADO %s*/
	public static final long ERROR_INESPERADO = 17;

	/*VERIFIQUE_MINIMO_VALOR_BUSQUEDA*/
	public static final long VERIFIQUE_MINIMO_VALOR_BUSQUEDA = 18;

	/*LA ENTIDAD A QUIEN SE ESTA REGISTRANDO EL BIEN YA HA SIDO ANULADA*/
	public static final long ENTIDAD_A_REGISTRAR_BIEN_ANULADA = 26;

	/*LA ENTIDAD A QUIEN SE ESTA REGISTRANDO EL PROPIETARIO YA HA SIDO ANULADA*/
	public static final long ENTIDAD_A_REGISTRAR_PROPIETARIO_ANULADA = 27;

	/*La garantia a quien se esta registrando la cobertura ya ha sido anulada*/
	public static final long GTIA_ANULADA = 30;

	/*Cobertura ya ha sido liberada*/
	public static final long COBERTURA_LIBERADA = 31;

	/*LIBERE_TODAS_LAS_COBERTURAS_OPERACIONES_QUE_TIENE_GARANTIA*/
	public static final long LIBERE_TODAS_LAS_COBERTURAS_OPERACIONES_QUE_TIENE_GARANTIA = 32;

	/*LIBERE_BLOQUEO_TOTAL_GARANTIA*/
	public static final long LIBERE_BLOQUEO_TOTAL_GARANTIA = 33;

	/*LIBERE_BLOQUEO_SOBRE_SALDO_GARANTIA*/
	public static final long LIBERE_BLOQUEO_SOBRE_SALDO_GARANTIA = 34;

	/*GARANTIA CON BLOQUEO TOTAL , NO ES POSIBLE REGISTRAR UNA COBERTURA*/
	public static final long GARANTIA_CON_BLOQUEO_TOTAL_NO_ES_POSIBLE_REGISTRAR_UNA_COBERTURA = 35;

	/*ERROR_VALIDAR_BUSQUEDA_GARANTIA*/
	public static final long ERROR_VALIDAR_BUSQUEDA_GARANTIA = 36;

	/*ERROR_TIPO_GARANTIA_NO_EXISTE*/
	public static final long ERROR_TIPO_GARANTIA_NO_EXISTE = 37;

	/*BUSQUEDA_GARANTIA_SIN_RESULTADOS*/
	public static final long BUSQUEDA_GARANTIA_SIN_RESULTADOS = 38;

	/*ERROR_CREAR_GARANTIA*/
	public static final long ERROR_CREAR_GARANTIA = 39;

	/*BUSQUEDA_GARANTIA_FISICA_SIN_RESULTADOS*/
	public static final long BUSQUEDA_GARANTIA_FISICA_SIN_RESULTADOS = 40;

	/*ERROR_BUSCAR_DATOS_GARANTIA*/
	public static final long ERROR_BUSCAR_DATOS_GARANTIA = 41;

	/*ERROR_REVISA_ESTATUS_ENTIDAD*/
	public static final long ERROR_REVISA_ESTATUS_ENTIDAD = 42;

	/*ENTIDAD_ASIGNADA_NO_EXISTE*/
	public static final long ENTIDAD_ASIGNADA_NO_EXISTE = 43;

	/*ERROR_ACTUALIZAR_GARANTIA_FISICA*/
	public static final long ERROR_ACTUALIZAR_GARANTIA_FISICA = 44;

	/*FALLO_VALIDAR_DATOS_ENTIDAD_TIPOENTIDAD*/
	public static final long FALLO_VALIDAR_DATOS_ENTIDAD_TIPOENTIDAD = 45;

	/*ERROR_MODIFICAR_GARANTIA*/
	public static final long ERROR_MODIFICAR_GARANTIA = 46;

	/*NO SE PUEDE MODIFICAR EL REGISTRO POR QUE SU ESTADO ES ANULADO*/
	public static final long ERROR_MODIFICAR_ESTA_ANULADO = 47;

	/*ERROR_VALIDAR_DATOS_BIEN*/
	public static final long ERROR_VALIDAR_DATOS_BIEN = 48;

	/*ERROR_CREAR_DETALLE_BIEN*/
	public static final long ERROR_CREAR_DETALLE_BIEN = 49;

	/*La garantia ya ha sido anulada.*/
	public static final long GARANTIA_ANULADA = 50;

	/*GARANTIA_SIN_BLOQUEO_TOTAL*/
	public static final long GARANTIA_SIN_BLOQUEO_TOTAL = 51;

	/*ERROR_ACTUALIZAR_DETALLE_BIEN*/
	public static final long ERROR_ACTUALIZAR_DETALLE_BIEN = 52;

	/*ERROR_VALIDAR_BUSQUEDA_BIENES*/
	public static final long ERROR_VALIDAR_BUSQUEDA_BIENES = 53;

	/*ERROR_BUSCAR_DATOS_BIEN*/
	public static final long ERROR_BUSCAR_DATOS_BIEN = 54;

	/*ERROR_MODIFICAR_BIEN*/
	public static final long ERROR_MODIFICAR_BIEN = 55;

	/*BUSQUEDA_BIENES_SIN_RESULTADOS*/
	public static final long BUSQUEDA_BIENES_SIN_RESULTADOS = 56;

	/*ENTIDAD_ASIGNADA_ANULADA*/
	public static final long ENTIDAD_ASIGNADA_ANULADA = 57;

	/*ERROR_CONTADOR_BIENES_ACTIVOS*/
	public static final long ERROR_CONTADOR_BIENES_ACTIVOS = 58;

	/*UNICO_REG_ACTIVO_INBORABLE*/
	public static final long UNICO_REG_ACTIVO_INBORABLE = 59;

	/*ERROR_DAR_BAJA_BIEN*/
	public static final long ERROR_DAR_BAJA_BIEN = 60;

	/*Verifique, el saldo a bloquear es menor o igual a cero.*/
	public static final long SALDO_BLOQUEO_MENOR = 61;

	/*Verifique, el saldo SIN BLOQUEO es menor al saldo que intenta bloquear.*/
	public static final long SALDO_SIN_BLOQUEO_MENOR = 62;

	/*ERROR_VALIDAR_DATOS_BAJA*/
	public static final long ERROR_VALIDAR_DATOS_BAJA = 63;

	/*ERROR_ELIMINAR_BIEN*/
	public static final long ERROR_ELIMINAR_BIEN = 64;

	/*ERROR_VALIDAR_DATOS_DOMICILIOS*/
	public static final long ERROR_VALIDAR_DATOS_DOMICILIOS = 65;

	/*ERROR_CREAR_DOMICILIO*/
	public static final long ERROR_CREAR_DOMICILIO = 66;

	/*ERROR_VALIDAR_DATOS_PROPIETARIO*/
	public static final long ERROR_VALIDAR_DATOS_PROPIETARIO = 67;

	/*ERROR_CREAR_PROPIETARIO*/
	public static final long ERROR_CREAR_PROPIETARIO = 68;

	/*ERROR_BUSCAR_DATOS_PROPIETARIO*/
	public static final long ERROR_BUSCAR_DATOS_PROPIETARIO = 69;

	/*BUSQUEDA_PROPIETARIO_SIN_RESULTADOS*/
	public static final long BUSQUEDA_PROPIETARIO_SIN_RESULTADOS = 70;

	/*ERROR_VALIDAR_PROPIETARIOS_VINCULADOS*/
	public static final long ERROR_VALIDAR_PROPIETARIOS_VINCULADOS = 71;

	/*ERROR_CONTADOR_PROPIETARIOS_ACTIVOS*/
	public static final long ERROR_CONTADOR_PROPIETARIOS_ACTIVOS = 72;

	/*ERROR_ACTUALIZAR_PROPIETARIO*/
	public static final long ERROR_ACTUALIZAR_PROPIETARIO = 73;

	/*Verifique, la garantia no tiene tasaciÃ³n activa*/
	public static final long GARANTIA_SIN_TASACION = 74;

	/*Garantia con bloqueo total no es posible realizar operaciones sobre esta garantia.*/
	public static final long GARANTIA_BLOQUEO_TOTAL = 75;

	/*ERROR_MODIFICAR_PROPIETARIO*/
	public static final long ERROR_MODIFICAR_PROPIETARIO = 76;

	/*ERROR_DAR_BAJA_PROPIETARIO*/
	public static final long ERROR_DAR_BAJA_PROPIETARIO = 77;

	/*No es posible tasar la garantia, esta ya ha sido activada*/
	public static final long GARANTIA_ACTIVADA_NO_TASAR = 78;

	/*ERROR_VALIDAR_DATOS_TASACION*/
	public static final long ERROR_VALIDAR_DATOS_TASACION = 79;

	/*ENTIDAD_A_REGISTRAR_TASACION_ANULADA*/
	public static final long ENTIDAD_A_REGISTRAR_TASACION_ANULADA = 80;

	/*ERROR_CREAR_TASACION*/
	public static final long ERROR_CREAR_TASACION = 81;

	/*ERROR_CREAR_VALORIZACION*/
	public static final long ERROR_CREAR_VALORIZACION = 82;

	/*ERROR_VALIDAR_LISTADO*/
	public static final long ERROR_VALIDAR_LISTADO = 83;

	/*ERROR_BUSCAR_DATOS_TASACION*/
	public static final long ERROR_BUSCAR_DATOS_TASACION = 84;

	/*ERROR_VALIDAR_DATOS_POLIZA_SEGURO*/
	public static final long ERROR_VALIDAR_DATOS_POLIZA_SEGURO = 85;

	/*ENTIDAD_POLIZA_SEGURO_NO_EXISTE*/
	public static final long ENTIDAD_POLIZA_SEGURO_NO_EXISTE = 86;

	/*ERROR_CREAR_POLIZA_SEGURO*/
	public static final long ERROR_CREAR_POLIZA_SEGURO = 87;

	/*ERROR_DAR_BAJA_POLIZA_SEGURO*/
	public static final long ERROR_DAR_BAJA_POLIZA_SEGURO = 88;

	/*ERROR_CONTADOR_POLIZA_SEGURO_ACTIVOS*/
	public static final long ERROR_CONTADOR_POLIZA_SEGURO_ACTIVOS = 89;

	/*ENTIDAD_A_REGISTRAR_POLIZA_SEGURO_ANULADA*/
	public static final long ENTIDAD_A_REGISTRAR_POLIZA_SEGURO_ANULADA = 90;

	/*ERROR_VALIDAR_POLIZAS_SEGURO_VINCULADOS*/
	public static final long ERROR_VALIDAR_POLIZAS_SEGURO_VINCULADOS = 91;

	/*ERROR_ACTUALIZAR_POLIZA_SEGURO*/
	public static final long ERROR_ACTUALIZAR_POLIZA_SEGURO = 92;

	/*ERROR_BUSCAR_DATOS_POLIZA_SEGURO*/
	public static final long ERROR_BUSCAR_DATOS_POLIZA_SEGURO = 93;

	/*ERROR_ACTUALIZAR_TASACION*/
	public static final long ERROR_ACTUALIZAR_TASACION = 94;

	/*ERROR_ACTUALIZAR_VALORIZACIONES_SALDOS*/
	public static final long ERROR_ACTUALIZAR_VALORIZACIONES_SALDOS = 95;

	/*Verifique, el saldo a liberar es menor o igual a cero*/
	public static final long SALDO_LIBERAR_MENOR_CERO = 96;

	/*Verifique, el saldo a liberar es mayor al Total del saldo bloqueado.â€�*/
	public static final long SALDO_LIBERAR_MAYOR = 97;

	/*BUSQUEDA_POLIZA_SEGURO_SIN_RESULTADOS*/
	public static final long BUSQUEDA_POLIZA_SEGURO_SIN_RESULTADOS = 98;

	/*BUSQUEDA_TASACIONES_SIN_RESULTADOS*/
	public static final long BUSQUEDA_TASACIONES_SIN_RESULTADOS = 99;

	/*ERROR_MODIFICAR_TASACION*/
	public static final long ERROR_MODIFICAR_TASACION = 100;

	/*ERROR_MODIFICAR_POLIZA_SEGURO*/
	public static final long ERROR_MODIFICAR_POLIZA_SEGURO = 101;

	/*ERROR_ELIMINAR_POLIZA_SEGURO*/
	public static final long ERROR_ELIMINAR_POLIZA_SEGURO = 102;

	/*ERROR_VALIDAR_ACTIVACION_GARANTIA*/
	public static final long ERROR_VALIDAR_ACTIVACION_GARANTIA = 103;

	/*ERROR_ACTIVAR_GARANTIA*/
	public static final long ERROR_ACTIVAR_GARANTIA = 104;

	/*Falta capturar los subtipos de garantia*/
	public static final long SUBTIPOS_VACIO = 105;

	/*BUSQUEDA_ESTADOS_GARANTIA_SIN_RESULTADOS*/
	public static final long BUSQUEDA_ESTADOS_GARANTIA_SIN_RESULTADOS = 106;

	/*ERROR_CREAR_GARANTIA_FISICA*/
	public static final long ERROR_CREAR_GARANTIA_FISICA = 107;

	/*ERROR_VALIDAR_DATOS_COBERTURA_OPERACION*/
	public static final long ERROR_VALIDAR_DATOS_COBERTURA_OPERACION = 108;

	/*ENTIDAD_COBERTURA_OPERACION_NO_EXISTE*/
	public static final long ENTIDAD_COBERTURA_OPERACION_NO_EXISTE = 109;

	/*ERROR_BUSCAR_DATOS_COBERTURA_OPERACION*/
	public static final long ERROR_BUSCAR_DATOS_COBERTURA_OPERACION = 110;

	/*ERROR_ACTUALIZAR_COBERTURA_OPERACION*/
	public static final long ERROR_ACTUALIZAR_COBERTURA_OPERACION = 111;

	/*ERROR_VALIDAR_DATOS_LIBERAR_COBERTURA_OPERACION*/
	public static final long ERROR_VALIDAR_DATOS_LIBERAR_COBERTURA_OPERACION = 112;

	/*ERROR_LIBERAR_COBERTRA_OPERACION*/
	public static final long ERROR_LIBERAR_COBERTRA_OPERACION = 113;

	/*ERROR_CREAR_COBERTURA_OPERACION*/
	public static final long ERROR_CREAR_COBERTURA_OPERACION = 114;

	/*ENTIDAD_A_REGISTRAR_COBERTURA_OPERACION_ANULADA*/
	public static final long ENTIDAD_A_REGISTRAR_COBERTURA_OPERACION_ANULADA = 115;

	/*ERROR_MODIFICAR_COBERTURA_OPERACION*/
	public static final long ERROR_MODIFICAR_COBERTURA_OPERACION = 116;

	/*BUSQUEDA_COBERTURA_OPERACION_SIN_RESULTADOS*/
	public static final long BUSQUEDA_COBERTURA_OPERACION_SIN_RESULTADOS = 117;

	/*ERROR_VALIDAR_GARANTIA*/
	public static final long ERROR_VALIDAR_GARANTIA = 118;

	/*ERROR_GUARDAR_DETALLE_TRANSACCION*/
	public static final long ERROR_GUARDAR_DETALLE_TRANSACCION = 119;

	/*ERROR_GUARDAR_TRANSACCION*/
	public static final long ERROR_GUARDAR_TRANSACCION = 120;

	/*ERROR_VALIDAR_ENTREGA_GARANTIA*/
	public static final long ERROR_VALIDAR_ENTREGA_GARANTIA = 121;

	/*ERROR_ENTREGAR_GARANTIA*/
	public static final long ERROR_ENTREGAR_GARANTIA = 122;

	/*ERROR_VALIDAR_ANULAR_GARANTIA*/
	public static final long ERROR_VALIDAR_ANULAR_GARANTIA = 123;

	/*ERROR_ANULAR_GARANTIA*/
	public static final long ERROR_ANULAR_GARANTIA = 124;

	/*ERROR_VALIDAR_GARANTIA_FISICA*/
	public static final long ERROR_VALIDAR_GARANTIA_FISICA = 125;

	/*ERROR_VALIDAR_GARANTIA_PERSONAL*/
	public static final long ERROR_VALIDAR_GARANTIA_PERSONAL = 126;

	/*ERROR_VALIDAR_GARANTIA_TITULO_VALOR*/
	public static final long ERROR_VALIDAR_GARANTIA_TITULO_VALOR = 127;

	/*ERROR_CREAR_GARANTIA_PERSONAL*/
	public static final long ERROR_CREAR_GARANTIA_PERSONAL = 128;

	/*ERROR_CREAR_GARANTIA_TITULO_VALOR*/
	public static final long ERROR_CREAR_GARANTIA_TITULO_VALOR = 129;

	/*ERROR_ACTUALIZAR_GARANTIA_TITULO_VALOR*/
	public static final long ERROR_ACTUALIZAR_GARANTIA_TITULO_VALOR = 130;

	/*ERROR_BUSCAR_DATOS_GARANTIA_PERSONAL*/
	public static final long ERROR_BUSCAR_DATOS_GARANTIA_PERSONAL = 131;

	/*ERROR_BUSCAR_DATOS_GARANTIA_TITULO_VALOR*/
	public static final long ERROR_BUSCAR_DATOS_GARANTIA_TITULO_VALOR = 132;

	/*BUSQUEDA_GARANTIA_TITULO_VALOR_SIN_RESULTADOS*/
	public static final long BUSQUEDA_GARANTIA_TITULO_VALOR_SIN_RESULTADOS = 133;

	/*BUSQUEDA_GARANTIA_PERSONAL_SIN_RESULTADOS*/
	public static final long BUSQUEDA_GARANTIA_PERSONAL_SIN_RESULTADOS = 134;

	/*ERROR_VALIDAR_BLOQUEO_SALDO_GARANTIA*/
	public static final long ERROR_VALIDAR_BLOQUEO_SALDO_GARANTIA = 135;

	/*ERROR_BLOQUEO_SALDO_GARANTIA*/
	public static final long ERROR_BLOQUEO_SALDO_GARANTIA = 136;

	/*ERROR_MODIFICAR_GARANTIA_PERSONAL*/
	public static final long ERROR_MODIFICAR_GARANTIA_PERSONAL = 137;

	/*ERROR_MODIFICAR_GARANTIA_TITULO_VALOR*/
	public static final long ERROR_MODIFICAR_GARANTIA_TITULO_VALOR = 138;

	/*ERROR_VALIDAR_LIBERAR_BLOQUEO_SALDO_GARANTIA*/
	public static final long ERROR_VALIDAR_LIBERAR_BLOQUEO_SALDO_GARANTIA = 139;

	/*ERROR_LIBERAR_BLOQUEO_SALDO_GARANTIA*/
	public static final long ERROR_LIBERAR_BLOQUEO_SALDO_GARANTIA = 140;

	/*ERROR_ACTUALIZAR_SALDOS*/
	public static final long ERROR_ACTUALIZAR_SALDOS = 141;

	/*ERROR_BUSCAR_LISTA_TASACION*/
	public static final long ERROR_BUSCAR_LISTA_TASACION = 142;

	/*Verifique, el campo fecha mayor al dia actual.*/
	public static final long FECHA_MAYOR = 143;

	/*ERROR_BUSCAR_DATOS_VALORIZACIONES_Y_SALDOS*/
	public static final long ERROR_BUSCAR_DATOS_VALORIZACIONES_Y_SALDOS = 144;

	/*LA ENTIDAD A QUIEN SE ESTA REGISTRANDO EL DEPOSITO YA HA SIDO ANULADA
*/
	public static final long DEPOSITO_GARANTIA_ANULADA = 145;

	/*ERROR_MODIFICAR_GARANTIA_FISICA*/
	public static final long ERROR_MODIFICAR_GARANTIA_FISICA = 146;

	/*ERROR_VALIDAR_GARANTIA_LIQUIDA*/
	public static final long ERROR_VALIDAR_GARANTIA_LIQUIDA = 147;

	/*ERROR_CREAR_GARANTIA_LIQUIDA*/
	public static final long ERROR_CREAR_GARANTIA_LIQUIDA = 148;

	/*ERROR_ACTUALIZAR_GARANTIA_LIQUIDA*/
	public static final long ERROR_ACTUALIZAR_GARANTIA_LIQUIDA = 149;

	/*ERROR_VALIDAR_BUSQUEDA_GARANTIA_LIQUIDA*/
	public static final long ERROR_VALIDAR_BUSQUEDA_GARANTIA_LIQUIDA = 150;

	/*ERROR_BUSCAR_DATOS_GARANTIA_LIQUIDA*/
	public static final long ERROR_BUSCAR_DATOS_GARANTIA_LIQUIDA = 151;

	/*FALLO_VALIDAR_DATOS_DESCRIPCION_ENTIDAD*/
	public static final long FALLO_VALIDAR_DATOS_DESCRIPCION_ENTIDAD = 152;

	/*BUSQUEDA_GARANTIA_DOMICILIOS_SIN_RESULTADO*/
	public static final long BUSQUEDA_GARANTIA_DOMICILIOS_SIN_RESULTADO = 153;

	/*BUSQUEDA_GARANTIA_BIENES_SIN_RESULTADOS*/
	public static final long BUSQUEDA_GARANTIA_BIENES_SIN_RESULTADOS = 154;

	/*ERROR_OBTENER_DESCRIPCION_ENTIDAD*/
	public static final long ERROR_OBTENER_DESCRIPCION_ENTIDAD = 155;

	/*ERROR_CONTADOR_TASACIONES_ACTIVAS*/
	public static final long ERROR_CONTADOR_TASACIONES_ACTIVAS = 156;

	/*ERROR_DAR_BAJA_TASACION*/
	public static final long ERROR_DAR_BAJA_TASACION = 157;

	/*ERROR_BLOQUEO_TOTAL_GARANTIA*/
	public static final long ERROR_BLOQUEO_TOTAL_GARANTIA = 158;

	/*ERROR_VALIDAR_LIBERAR_BLOQUEO_TOTAL_GARANTIA*/
	public static final long ERROR_VALIDAR_LIBERAR_BLOQUEO_TOTAL_GARANTIA = 159;

	/*ERROR_LIBERAR_BLOQUEO_TOTAl_GARANTIA*/
	public static final long ERROR_LIBERAR_BLOQUEO_TOTAl_GARANTIA = 160;

	/*BUSQUEDA_VALORIZACIONES_SALDOS_SIN_RESULTADOS*/
	public static final long BUSQUEDA_VALORIZACIONES_SALDOS_SIN_RESULTADOS = 161;

	/*ERROR_VALIDAR_IMPORTE_CONCEPTO_VALORIZACIONES*/
	public static final long ERROR_VALIDAR_IMPORTE_CONCEPTO_VALORIZACIONES = 162;

	/*ERROR_BUSCAR_IMPORTE_CONCEPTO*/
	public static final long ERROR_BUSCAR_IMPORTE_CONCEPTO = 163;

	/*BUSQUEDA_IMPORTES_CONCEPTOS*/
	public static final long BUSQUEDA_IMPORTES_CONCEPTOS = 164;

	/*Saldo disponible de la garantia insuficiente para coberturar la operacion.*/
	public static final long SALDO_INSUFICIENTE = 165;

	/*Falta capturar los conceptos de operacion*/
	public static final long SIN_CONCEPTOS = 166;

	/*PRIMERO BUSQUE Y SELECCIONE LA OPERACION A COBERTURAR*/
	public static final long PRIMERO_BUSQUE_Y_SELECCIONE_OPERACION = 167;

	/*ERROR_BUSCAR_DATOS_CONCEPTOS_OPERACION*/
	public static final long ERROR_BUSCAR_DATOS_CONCEPTOS_OPERACION = 168;

	/*Seleccionar una garantia activada.*/
	public static final long GARANTIA_SIN_ACTIVAR = 169;

	/*Verifique, el valor de la garantia  es cero.*/
	public static final long GTIA_SIN_VALOR = 170;

	/*NO es posible hacer la activacion, favor de revisar  los datos generales de la garantia y corroborar los campos requeridos.*/
	public static final long GTIA_INCOMPLETA = 171;

	/*La garantia ya esta activada.*/
	public static final long GTIA_ACTIVADA = 172;

	/*No es posible realizar modificaciones,esta garantia ya esta coberturando operaciones.*/
	public static final long GTIA_COBERTURANDO = 173;

	/*ERROR_BUSCAR_DATOS_NOTARIOS*/
	public static final long ERROR_BUSCAR_DATOS_NOTARIOS = 174;

	/*ERROR_BUSCAR_DATOS_PERITOS*/
	public static final long ERROR_BUSCAR_DATOS_PERITOS = 176;

	/*La garantia tiene estatus distinto de APERTURADA no es posible modificarla.*/
	public static final long GTIA_NO_APERTURADA = 177;

	/*ERROR_BUSCAR_DATOS_DEOPSITOS_LIQUIDA*/
	public static final long ERROR_BUSCAR_DATOS_DEOPSITOS_LIQUIDA = 178;

	/*La entidad a quien se esta registrando el deposito ya ha sido anulada*/
	public static final long GTIA_LIQ_ANULADA_NO_DEPOSITO = 179;

	/*La garantia no puede ser modificada tiene estatus diferente de Aperturada y/o Valorizada.*/
	public static final long GTIA_ESTATUS_MAYOR_VALORIZADA = 180;

	/*ERROR_VALIDAR_DATOS_DEPOSITO_LIQUIDA*/
	public static final long ERROR_VALIDAR_DATOS_DEPOSITO_LIQUIDA = 181;

	/*Primero busque y seleccione una dacion en pago.*/
	public static final long SIN_DACION_PAGO = 182;

	/*Dacion en pago anulada*/
	public static final long DACION_PAGO_ANULADA = 183;

	/*La dacion en pago tiene estatus distinto de APERTURADA no es posible modificarla.*/
	public static final long DACION_ESTATUS_MAYOR_VALORIZADA = 184;

	/*ERROR_CREAR_DACION_PAGO*/
	public static final long ERROR_CREAR_DACION_PAGO = 185;

	/*ERROR_VALIDAR_DACION_PAGO*/
	public static final long ERROR_VALIDAR_DACION_PAGO = 186;

	/*ERROR_BUSCAR_DATOS_DACION_PAGO*/
	public static final long ERROR_BUSCAR_DATOS_DACION_PAGO = 187;

	/*ERROR_VALIDAR_BUSQUEDA_DACION_PAGO*/
	public static final long ERROR_VALIDAR_BUSQUEDA_DACION_PAGO = 188;

	/*ERROR_CREAR_DOMICILIO_DACION_PAGO*/
	public static final long ERROR_CREAR_DOMICILIO_DACION_PAGO = 189;

	/*El porcentaje de la participacion esta fuera del rango*/
	public static final long PORCENTAJE_PARTICIPACION_FUERA_RANGO = 190;

	/*Verifique la participacion en conjunto de los propietarios excede el 100%.*/
	public static final long PARTICIPACION_PROPIETARIOS_EXCEDE = 191;

	/*ERROR EN LA BUSQUEDA ASEGURADORAS*/
	public static final long ERROR_BUSCAR_DATOS_ASEGURADORAS = 192;

	/*Verifique, el listado de conceptos del valor de la cobertura esta vacio.*/
	public static final long LISTADO_CONCEPTOS_COBERTURA_VACIO = 193;

	/*ERROR EN LISTADO PROPIETARIOS VINCULACIONES*/
	public static final long ERR_LIST_PROPIETARIOS_VINCULACIONES = 194;

	/*SELECCIONE EL TIPO DE GARANTIA*/
	public static final long SIN_TIPO_GARANTIA = 195;

	/*ERROR_AVAL_DUPLICADO*/
	public static final long ERROR_AVAL_DUPLICADO = 196;

	/*ERROR_PRESTAMO_TIENE_COBERTURA_MISMA_GARANTIA*/
	public static final long ERROR_PRESTAMO_TIENE_COBERTURA_MISMA_GARANTIA = 197;

	/*La garantia ya esta coberturando otra operacion y no esta marcada como Garantia Sabana*/
	public static final long ERROR_PRESTAMO_TIENE_COBERTURA_OTRA_OPRACION_NO_SABANA = 198;
	
	/*USUARIO SIN EMPRESA ASIGNADA*/
	public static final long USUARIO_SIN_EMPRESA_ASIG = 199;

	/*ERROR INESPERADO ENCONSULTA DATOS GARANTIA*/
	public static final long EI_CONSULTA_DATOS_GARANTIA = 200;

	/*ERROR INESPERADO AL GUARDAR DATOS GARANTIA*/
	public static final long EI_GUARDAR_DATOS_GARANTIA = 201;

	/*ERROR INESPERADO EN INSERTA CONCEPTOS DATOS GARANTIA*/
	public static final long EI_INSERTA_CONCEPTOS_DATOS_GARANTIA = 202;

	/*ESPECIFIQUE TIPO DE GARANTIA PARA INSERTAR CONCEPTOS EN DATOS SOLICITUD*/
	public static final long ICDG_TIPO_GTIA_VACIO = 203;

	/*ESPECIFIQUE SUB TIPO DE GARANTIA PARA INSERTAR CONCEPTOS EN DATOS SOLICITUD*/
	public static final long ICDG_SUB_TIPO_GTIA_VACIO = 204;

	/*ERROR INESPERADO EN ACTUALIZA DATOS GARANTIA*/
	public static final long EI_ACTUALIZA_DATOS_GARANTIA = 205;

	/*ESPECIFIQUE TIPO DE GARANTIA PARA ACTUALIZAR CONCEPTOS EN DATOS SOLICITUD*/
	public static final long ACDG_TIPO_GTIA_VACIO = 206;

	/*ESPECIFIQUE SUB TIPO DE GARANTIA PARA ACTUALIZAR CONCEPTOS EN DATOS SOLICITUD*/
	public static final long ACDG_SUB_TIPO_GTIA_VACIO = 207;

	/*LA GARANTIA TIENE COBERTURAS VIGENTES*/
	public static final long GTIA_COB_VIG = 208;
	
	/*ERROR_OBTENER_EJB*/
	public static final long ERROR_OBTENER_EJB = 226;
	
	/*ERROR EN EL WEBSERVICE GARANTIAS REDISPOSICION*/
	public static final long EI_WS_GARANTIAS_REDISPOSICION = 227;
	
	

	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		
		descError.put(TRANSACCION_EXITOSA,"TRANSACCION_EXITOSA");
		descError.put(CAPTURE_CAMPOS_REQUERIDOS,"CAMPO %s REQUERIDO");
		descError.put(PRIMERO_BUSQUE_Y_SELECCIONE_UNA_GARANTIA,"PRIMERO BUSQUE Y SELECCIONE UNA GARANTIA");
		descError.put(REGISTRO_INACTIVO,"Registro ya ha sido dado de baja");
		descError.put(REGISTRO_DAR_BAJA_ES_UNICO_ACTIVO,"REGISTRO_DAR_BAJA_ES_UNICO_ACTIVO");
		descError.put(VERIFIQUE_DETALLE_LOS_BIENES_NO_COINCIDE_CON_NO_BIENES,"VERIFIQUE, EL DETALLE DE LOS BIENES NO COINCIDE CON LA CANTIDAD INDICADA EN EL CAMPO NO. BIENES");
		descError.put(VERIFIQUE_SECUENCIA_FECHAS_CAPTURADAS_INVERTIDAS,"VERIFIQUE,  LA SECUENCIA DE LAS FECHAS CAPTURADAS ESTAN INVERTIDAS");
		descError.put(CAPTURE_AL_MENOS_UNA_PROPIETARIO,"CAPTURE_AL_MENOS_UNA_PROPIETARIO");
		descError.put(CAPTURE_DOMICILIO_GARANTIA,"CAPTURE_DOMICILIO_GARANTIA");
		descError.put(NO_ES_POSIBLE_ANULAR_GARANTIA_ESTA_ACTIVADA,"NO ES POSIBLE ANULAR LA GARANTIA, ESTA YA HA SIDO ACTIVADA.");
		descError.put(ERROR_INESPERADO,"ERROR INESPERADO %s");
		descError.put(VERIFIQUE_MINIMO_VALOR_BUSQUEDA,"VERIFIQUE_MINIMO_VALOR_BUSQUEDA");
		descError.put(ENTIDAD_A_REGISTRAR_BIEN_ANULADA,"LA ENTIDAD A QUIEN SE ESTA REGISTRANDO EL BIEN YA HA SIDO ANULADA");
		descError.put(ENTIDAD_A_REGISTRAR_PROPIETARIO_ANULADA,"LA ENTIDAD A QUIEN SE ESTA REGISTRANDO EL PROPIETARIO YA HA SIDO ANULADA");
		descError.put(GTIA_ANULADA,"La garantia a quien se esta registrando la cobertura ya ha sido anulada");
		descError.put(COBERTURA_LIBERADA,"Cobertura ya ha sido liberada");
		descError.put(LIBERE_TODAS_LAS_COBERTURAS_OPERACIONES_QUE_TIENE_GARANTIA,"LIBERE_TODAS_LAS_COBERTURAS_OPERACIONES_QUE_TIENE_GARANTIA");
		descError.put(LIBERE_BLOQUEO_TOTAL_GARANTIA,"LIBERE_BLOQUEO_TOTAL_GARANTIA");
		descError.put(LIBERE_BLOQUEO_SOBRE_SALDO_GARANTIA,"LIBERE_BLOQUEO_SOBRE_SALDO_GARANTIA");
		descError.put(GARANTIA_CON_BLOQUEO_TOTAL_NO_ES_POSIBLE_REGISTRAR_UNA_COBERTURA,"GARANTIA CON BLOQUEO TOTAL , NO ES POSIBLE REGISTRAR UNA COBERTURA");
		descError.put(ERROR_VALIDAR_BUSQUEDA_GARANTIA,"ERROR_VALIDAR_BUSQUEDA_GARANTIA");
		descError.put(ERROR_TIPO_GARANTIA_NO_EXISTE,"ERROR_TIPO_GARANTIA_NO_EXISTE");
		descError.put(BUSQUEDA_GARANTIA_SIN_RESULTADOS,"BUSQUEDA_GARANTIA_SIN_RESULTADOS");
		descError.put(ERROR_CREAR_GARANTIA,"ERROR_CREAR_GARANTIA");
		descError.put(BUSQUEDA_GARANTIA_FISICA_SIN_RESULTADOS,"BUSQUEDA_GARANTIA_FISICA_SIN_RESULTADOS");
		descError.put(ERROR_BUSCAR_DATOS_GARANTIA,"ERROR_BUSCAR_DATOS_GARANTIA");
		descError.put(ERROR_REVISA_ESTATUS_ENTIDAD,"ERROR_REVISA_ESTATUS_ENTIDAD");
		descError.put(ENTIDAD_ASIGNADA_NO_EXISTE,"ENTIDAD_ASIGNADA_NO_EXISTE");
		descError.put(ERROR_ACTUALIZAR_GARANTIA_FISICA,"ERROR_ACTUALIZAR_GARANTIA_FISICA");
		descError.put(FALLO_VALIDAR_DATOS_ENTIDAD_TIPOENTIDAD,"FALLO_VALIDAR_DATOS_ENTIDAD_TIPOENTIDAD");
		descError.put(ERROR_MODIFICAR_GARANTIA,"ERROR_MODIFICAR_GARANTIA");
		descError.put(ERROR_MODIFICAR_ESTA_ANULADO,"NO SE PUEDE MODIFICAR EL REGISTRO POR QUE SU ESTADO ES ANULADO");
		descError.put(ERROR_VALIDAR_DATOS_BIEN,"ERROR_VALIDAR_DATOS_BIEN");
		descError.put(ERROR_CREAR_DETALLE_BIEN,"ERROR_CREAR_DETALLE_BIEN");
		descError.put(GARANTIA_ANULADA,"La garantia ya ha sido anulada.");
		descError.put(GARANTIA_SIN_BLOQUEO_TOTAL,"GARANTIA_SIN_BLOQUEO_TOTAL");
		descError.put(ERROR_ACTUALIZAR_DETALLE_BIEN,"ERROR_ACTUALIZAR_DETALLE_BIEN");
		descError.put(ERROR_VALIDAR_BUSQUEDA_BIENES,"ERROR_VALIDAR_BUSQUEDA_BIENES");
		descError.put(ERROR_BUSCAR_DATOS_BIEN,"ERROR_BUSCAR_DATOS_BIEN");
		descError.put(ERROR_MODIFICAR_BIEN,"ERROR_MODIFICAR_BIEN");
		descError.put(BUSQUEDA_BIENES_SIN_RESULTADOS,"BUSQUEDA_BIENES_SIN_RESULTADOS");
		descError.put(ENTIDAD_ASIGNADA_ANULADA,"ENTIDAD_ASIGNADA_ANULADA");
		descError.put(ERROR_CONTADOR_BIENES_ACTIVOS,"ERROR_CONTADOR_BIENES_ACTIVOS");
		descError.put(UNICO_REG_ACTIVO_INBORABLE,"UNICO_REG_ACTIVO_INBORABLE");
		descError.put(ERROR_DAR_BAJA_BIEN,"ERROR_DAR_BAJA_BIEN");
		descError.put(SALDO_BLOQUEO_MENOR,"Verifique, el saldo a bloquear es menor o igual a cero.");
		descError.put(SALDO_SIN_BLOQUEO_MENOR,"Verifique, el saldo SIN BLOQUEO es menor al saldo que intenta bloquear.");
		descError.put(ERROR_VALIDAR_DATOS_BAJA,"ERROR_VALIDAR_DATOS_BAJA");
		descError.put(ERROR_ELIMINAR_BIEN,"ERROR_ELIMINAR_BIEN");
		descError.put(ERROR_VALIDAR_DATOS_DOMICILIOS,"ERROR_VALIDAR_DATOS_DOMICILIOS");
		descError.put(ERROR_CREAR_DOMICILIO,"ERROR_CREAR_DOMICILIO");
		descError.put(ERROR_VALIDAR_DATOS_PROPIETARIO,"ERROR_VALIDAR_DATOS_PROPIETARIO");
		descError.put(ERROR_CREAR_PROPIETARIO,"ERROR_CREAR_PROPIETARIO");
		descError.put(ERROR_BUSCAR_DATOS_PROPIETARIO,"ERROR_BUSCAR_DATOS_PROPIETARIO");
		descError.put(BUSQUEDA_PROPIETARIO_SIN_RESULTADOS,"BUSQUEDA_PROPIETARIO_SIN_RESULTADOS");
		descError.put(ERROR_VALIDAR_PROPIETARIOS_VINCULADOS,"ERROR_VALIDAR_PROPIETARIOS_VINCULADOS");
		descError.put(ERROR_CONTADOR_PROPIETARIOS_ACTIVOS,"ERROR_CONTADOR_PROPIETARIOS_ACTIVOS");
		descError.put(ERROR_ACTUALIZAR_PROPIETARIO,"ERROR_ACTUALIZAR_PROPIETARIO");
		descError.put(GARANTIA_SIN_TASACION,"Verifique, la garantia no tiene tasaciÃ³n activa");
		descError.put(GARANTIA_BLOQUEO_TOTAL,"Garantia con bloqueo total no es posible realizar operaciones sobre esta garantia.");
		descError.put(ERROR_MODIFICAR_PROPIETARIO,"ERROR_MODIFICAR_PROPIETARIO");
		descError.put(ERROR_DAR_BAJA_PROPIETARIO,"ERROR_DAR_BAJA_PROPIETARIO");
		descError.put(GARANTIA_ACTIVADA_NO_TASAR,"No es posible tasar la garantia, esta ya ha sido activada");
		descError.put(ERROR_VALIDAR_DATOS_TASACION,"ERROR_VALIDAR_DATOS_TASACION");
		descError.put(ENTIDAD_A_REGISTRAR_TASACION_ANULADA,"ENTIDAD_A_REGISTRAR_TASACION_ANULADA");
		descError.put(ERROR_CREAR_TASACION,"ERROR_CREAR_TASACION");
		descError.put(ERROR_CREAR_VALORIZACION,"ERROR_CREAR_VALORIZACION");
		descError.put(ERROR_VALIDAR_LISTADO,"ERROR_VALIDAR_LISTADO");
		descError.put(ERROR_BUSCAR_DATOS_TASACION,"ERROR_BUSCAR_DATOS_TASACION");
		descError.put(ERROR_VALIDAR_DATOS_POLIZA_SEGURO,"ERROR_VALIDAR_DATOS_POLIZA_SEGURO");
		descError.put(ENTIDAD_POLIZA_SEGURO_NO_EXISTE,"ENTIDAD_POLIZA_SEGURO_NO_EXISTE");
		descError.put(ERROR_CREAR_POLIZA_SEGURO,"ERROR_CREAR_POLIZA_SEGURO");
		descError.put(ERROR_DAR_BAJA_POLIZA_SEGURO,"ERROR_DAR_BAJA_POLIZA_SEGURO");
		descError.put(ERROR_CONTADOR_POLIZA_SEGURO_ACTIVOS,"ERROR_CONTADOR_POLIZA_SEGURO_ACTIVOS");
		descError.put(ENTIDAD_A_REGISTRAR_POLIZA_SEGURO_ANULADA,"ENTIDAD_A_REGISTRAR_POLIZA_SEGURO_ANULADA");
		descError.put(ERROR_VALIDAR_POLIZAS_SEGURO_VINCULADOS,"ERROR_VALIDAR_POLIZAS_SEGURO_VINCULADOS");
		descError.put(ERROR_ACTUALIZAR_POLIZA_SEGURO,"ERROR_ACTUALIZAR_POLIZA_SEGURO");
		descError.put(ERROR_BUSCAR_DATOS_POLIZA_SEGURO,"ERROR_BUSCAR_DATOS_POLIZA_SEGURO");
		descError.put(ERROR_ACTUALIZAR_TASACION,"ERROR_ACTUALIZAR_TASACION");
		descError.put(ERROR_ACTUALIZAR_VALORIZACIONES_SALDOS,"ERROR_ACTUALIZAR_VALORIZACIONES_SALDOS");
		descError.put(SALDO_LIBERAR_MENOR_CERO,"Verifique, el saldo a liberar es menor o igual a cero");
		descError.put(SALDO_LIBERAR_MAYOR,"Verifique, el saldo a liberar es mayor al Total del saldo bloqueado.â€�");
		descError.put(BUSQUEDA_POLIZA_SEGURO_SIN_RESULTADOS,"BUSQUEDA_POLIZA_SEGURO_SIN_RESULTADOS");
		descError.put(BUSQUEDA_TASACIONES_SIN_RESULTADOS,"BUSQUEDA_TASACIONES_SIN_RESULTADOS");
		descError.put(ERROR_MODIFICAR_TASACION,"ERROR_MODIFICAR_TASACION");
		descError.put(ERROR_MODIFICAR_POLIZA_SEGURO,"ERROR_MODIFICAR_POLIZA_SEGURO");
		descError.put(ERROR_ELIMINAR_POLIZA_SEGURO,"ERROR_ELIMINAR_POLIZA_SEGURO");
		descError.put(ERROR_VALIDAR_ACTIVACION_GARANTIA,"ERROR_VALIDAR_ACTIVACION_GARANTIA");
		descError.put(ERROR_ACTIVAR_GARANTIA,"ERROR_ACTIVAR_GARANTIA");
		descError.put(SUBTIPOS_VACIO,"Falta capturar los subtipos de garantia");
		descError.put(BUSQUEDA_ESTADOS_GARANTIA_SIN_RESULTADOS,"BUSQUEDA_ESTADOS_GARANTIA_SIN_RESULTADOS");
		descError.put(ERROR_CREAR_GARANTIA_FISICA,"ERROR_CREAR_GARANTIA_FISICA");
		descError.put(ERROR_VALIDAR_DATOS_COBERTURA_OPERACION,"ERROR_VALIDAR_DATOS_COBERTURA_OPERACION");
		descError.put(ENTIDAD_COBERTURA_OPERACION_NO_EXISTE,"ENTIDAD_COBERTURA_OPERACION_NO_EXISTE");
		descError.put(ERROR_BUSCAR_DATOS_COBERTURA_OPERACION,"ERROR_BUSCAR_DATOS_COBERTURA_OPERACION");
		descError.put(ERROR_ACTUALIZAR_COBERTURA_OPERACION,"ERROR_ACTUALIZAR_COBERTURA_OPERACION");
		descError.put(ERROR_VALIDAR_DATOS_LIBERAR_COBERTURA_OPERACION,"ERROR_VALIDAR_DATOS_LIBERAR_COBERTURA_OPERACION");
		descError.put(ERROR_LIBERAR_COBERTRA_OPERACION,"ERROR_LIBERAR_COBERTRA_OPERACION");
		descError.put(ERROR_CREAR_COBERTURA_OPERACION,"ERROR_CREAR_COBERTURA_OPERACION");
		descError.put(ENTIDAD_A_REGISTRAR_COBERTURA_OPERACION_ANULADA,"ENTIDAD_A_REGISTRAR_COBERTURA_OPERACION_ANULADA");
		descError.put(ERROR_MODIFICAR_COBERTURA_OPERACION,"ERROR_MODIFICAR_COBERTURA_OPERACION");
		descError.put(BUSQUEDA_COBERTURA_OPERACION_SIN_RESULTADOS,"BUSQUEDA_COBERTURA_OPERACION_SIN_RESULTADOS");
		descError.put(ERROR_VALIDAR_GARANTIA,"ERROR_VALIDAR_GARANTIA");
		descError.put(ERROR_GUARDAR_DETALLE_TRANSACCION,"ERROR_GUARDAR_DETALLE_TRANSACCION");
		descError.put(ERROR_GUARDAR_TRANSACCION,"ERROR_GUARDAR_TRANSACCION");
		descError.put(ERROR_VALIDAR_ENTREGA_GARANTIA,"ERROR_VALIDAR_ENTREGA_GARANTIA");
		descError.put(ERROR_ENTREGAR_GARANTIA,"ERROR_ENTREGAR_GARANTIA");
		descError.put(ERROR_VALIDAR_ANULAR_GARANTIA,"ERROR_VALIDAR_ANULAR_GARANTIA");
		descError.put(ERROR_ANULAR_GARANTIA,"ERROR_ANULAR_GARANTIA");
		descError.put(ERROR_VALIDAR_GARANTIA_FISICA,"ERROR_VALIDAR_GARANTIA_FISICA");
		descError.put(ERROR_VALIDAR_GARANTIA_PERSONAL,"ERROR_VALIDAR_GARANTIA_PERSONAL");
		descError.put(ERROR_VALIDAR_GARANTIA_TITULO_VALOR,"ERROR_VALIDAR_GARANTIA_TITULO_VALOR");
		descError.put(ERROR_CREAR_GARANTIA_PERSONAL,"ERROR_CREAR_GARANTIA_PERSONAL");
		descError.put(ERROR_CREAR_GARANTIA_TITULO_VALOR,"ERROR_CREAR_GARANTIA_TITULO_VALOR");
		descError.put(ERROR_ACTUALIZAR_GARANTIA_TITULO_VALOR,"ERROR_ACTUALIZAR_GARANTIA_TITULO_VALOR");
		descError.put(ERROR_BUSCAR_DATOS_GARANTIA_PERSONAL,"ERROR_BUSCAR_DATOS_GARANTIA_PERSONAL");
		descError.put(ERROR_BUSCAR_DATOS_GARANTIA_TITULO_VALOR,"ERROR_BUSCAR_DATOS_GARANTIA_TITULO_VALOR");
		descError.put(BUSQUEDA_GARANTIA_TITULO_VALOR_SIN_RESULTADOS,"BUSQUEDA_GARANTIA_TITULO_VALOR_SIN_RESULTADOS");
		descError.put(BUSQUEDA_GARANTIA_PERSONAL_SIN_RESULTADOS,"BUSQUEDA_GARANTIA_PERSONAL_SIN_RESULTADOS");
		descError.put(ERROR_VALIDAR_BLOQUEO_SALDO_GARANTIA,"ERROR_VALIDAR_BLOQUEO_SALDO_GARANTIA");
		descError.put(ERROR_BLOQUEO_SALDO_GARANTIA,"ERROR_BLOQUEO_SALDO_GARANTIA");
		descError.put(ERROR_MODIFICAR_GARANTIA_PERSONAL,"ERROR_MODIFICAR_GARANTIA_PERSONAL");
		descError.put(ERROR_MODIFICAR_GARANTIA_TITULO_VALOR,"ERROR_MODIFICAR_GARANTIA_TITULO_VALOR");
		descError.put(ERROR_VALIDAR_LIBERAR_BLOQUEO_SALDO_GARANTIA,"ERROR_VALIDAR_LIBERAR_BLOQUEO_SALDO_GARANTIA");
		descError.put(ERROR_LIBERAR_BLOQUEO_SALDO_GARANTIA,"ERROR_LIBERAR_BLOQUEO_SALDO_GARANTIA");
		descError.put(ERROR_ACTUALIZAR_SALDOS,"ERROR_ACTUALIZAR_SALDOS");
		descError.put(ERROR_BUSCAR_LISTA_TASACION,"ERROR_BUSCAR_LISTA_TASACION");
		descError.put(FECHA_MAYOR,"Verifique, el campo fecha mayor al dia actual.");
		descError.put(ERROR_BUSCAR_DATOS_VALORIZACIONES_Y_SALDOS,"ERROR_BUSCAR_DATOS_VALORIZACIONES_Y_SALDOS");
		descError.put(DEPOSITO_GARANTIA_ANULADA,"LA ENTIDAD A QUIEN SE ESTA REGISTRANDO EL DEPOSITO YA HA SIDO ANULADA");
		descError.put(ERROR_MODIFICAR_GARANTIA_FISICA,"ERROR_MODIFICAR_GARANTIA_FISICA");
		descError.put(ERROR_VALIDAR_GARANTIA_LIQUIDA,"ERROR_VALIDAR_GARANTIA_LIQUIDA");
		descError.put(ERROR_CREAR_GARANTIA_LIQUIDA,"ERROR_CREAR_GARANTIA_LIQUIDA");
		descError.put(ERROR_ACTUALIZAR_GARANTIA_LIQUIDA,"ERROR_ACTUALIZAR_GARANTIA_LIQUIDA");
		descError.put(ERROR_VALIDAR_BUSQUEDA_GARANTIA_LIQUIDA,"ERROR_VALIDAR_BUSQUEDA_GARANTIA_LIQUIDA");
		descError.put(ERROR_BUSCAR_DATOS_GARANTIA_LIQUIDA,"ERROR_BUSCAR_DATOS_GARANTIA_LIQUIDA");
		descError.put(FALLO_VALIDAR_DATOS_DESCRIPCION_ENTIDAD,"FALLO_VALIDAR_DATOS_DESCRIPCION_ENTIDAD");
		descError.put(BUSQUEDA_GARANTIA_DOMICILIOS_SIN_RESULTADO,"BUSQUEDA_GARANTIA_DOMICILIOS_SIN_RESULTADO");
		descError.put(BUSQUEDA_GARANTIA_BIENES_SIN_RESULTADOS,"BUSQUEDA_GARANTIA_BIENES_SIN_RESULTADOS");
		descError.put(ERROR_OBTENER_DESCRIPCION_ENTIDAD,"ERROR_OBTENER_DESCRIPCION_ENTIDAD");
		descError.put(ERROR_CONTADOR_TASACIONES_ACTIVAS,"ERROR_CONTADOR_TASACIONES_ACTIVAS");
		descError.put(ERROR_DAR_BAJA_TASACION,"ERROR_DAR_BAJA_TASACION");
		descError.put(ERROR_BLOQUEO_TOTAL_GARANTIA,"ERROR_BLOQUEO_TOTAL_GARANTIA");
		descError.put(ERROR_VALIDAR_LIBERAR_BLOQUEO_TOTAL_GARANTIA,"ERROR_VALIDAR_LIBERAR_BLOQUEO_TOTAL_GARANTIA");
		descError.put(ERROR_LIBERAR_BLOQUEO_TOTAl_GARANTIA,"ERROR_LIBERAR_BLOQUEO_TOTAl_GARANTIA");
		descError.put(BUSQUEDA_VALORIZACIONES_SALDOS_SIN_RESULTADOS,"BUSQUEDA_VALORIZACIONES_SALDOS_SIN_RESULTADOS");
		descError.put(ERROR_VALIDAR_IMPORTE_CONCEPTO_VALORIZACIONES,"ERROR_VALIDAR_IMPORTE_CONCEPTO_VALORIZACIONES");
		descError.put(ERROR_BUSCAR_IMPORTE_CONCEPTO,"ERROR_BUSCAR_IMPORTE_CONCEPTO");
		descError.put(BUSQUEDA_IMPORTES_CONCEPTOS,"BUSQUEDA_IMPORTES_CONCEPTOS");
		descError.put(SALDO_INSUFICIENTE,"Saldo disponible de la garantia insuficiente para coberturar la operacion.");
		descError.put(SIN_CONCEPTOS,"Falta capturar los conceptos de operacion");
		descError.put(PRIMERO_BUSQUE_Y_SELECCIONE_OPERACION,"PRIMERO BUSQUE Y SELECCIONE LA OPERACION A COBERTURAR");
		descError.put(ERROR_BUSCAR_DATOS_CONCEPTOS_OPERACION,"ERROR_BUSCAR_DATOS_CONCEPTOS_OPERACION");
		descError.put(GARANTIA_SIN_ACTIVAR,"Seleccionar una garantia activada.");
		descError.put(GTIA_SIN_VALOR,"Verifique, el valor de la garantia  es cero.");
		descError.put(GTIA_INCOMPLETA,"NO es posible hacer la activacion, favor de revisar  los datos generales de la garantia y corroborar los campos requeridos.");
		descError.put(GTIA_ACTIVADA,"La garantia ya esta activada.");
		descError.put(GTIA_COBERTURANDO,"No es posible realizar modificaciones,esta garantia ya esta coberturando operaciones.");
		descError.put(ERROR_BUSCAR_DATOS_NOTARIOS,"ERROR_BUSCAR_DATOS_NOTARIOS");
		descError.put(ERROR_BUSCAR_DATOS_PERITOS,"ERROR_BUSCAR_DATOS_PERITOS");
		descError.put(GTIA_NO_APERTURADA,"La garantia tiene estatus distinto de APERTURADA no es posible modificarla.");
		descError.put(ERROR_BUSCAR_DATOS_DEOPSITOS_LIQUIDA,"ERROR_BUSCAR_DATOS_DEOPSITOS_LIQUIDA");
		descError.put(GTIA_LIQ_ANULADA_NO_DEPOSITO,"La entidad a quien se esta registrando el deposito ya ha sido anulada");
		descError.put(GTIA_ESTATUS_MAYOR_VALORIZADA,"La garantia no puede ser modificada tiene estatus diferente de Aperturada y/o Valorizada.");
		descError.put(ERROR_VALIDAR_DATOS_DEPOSITO_LIQUIDA,"ERROR_VALIDAR_DATOS_DEPOSITO_LIQUIDA");
		descError.put(SIN_DACION_PAGO,"Primero busque y seleccione una dacion en pago.");
		descError.put(DACION_PAGO_ANULADA,"Dacion en pago anulada");
		descError.put(DACION_ESTATUS_MAYOR_VALORIZADA,"La dacion en pago tiene estatus distinto de APERTURADA no es posible modificarla.");
		descError.put(ERROR_CREAR_DACION_PAGO,"ERROR_CREAR_DACION_PAGO");
		descError.put(ERROR_VALIDAR_DACION_PAGO,"ERROR_VALIDAR_DACION_PAGO");
		descError.put(ERROR_BUSCAR_DATOS_DACION_PAGO,"ERROR_BUSCAR_DATOS_DACION_PAGO");
		descError.put(ERROR_VALIDAR_BUSQUEDA_DACION_PAGO,"ERROR_VALIDAR_BUSQUEDA_DACION_PAGO");
		descError.put(ERROR_CREAR_DOMICILIO_DACION_PAGO,"ERROR_CREAR_DOMICILIO_DACION_PAGO");
		descError.put(PORCENTAJE_PARTICIPACION_FUERA_RANGO,"El porcentaje de la participacion esta fuera del rango");
		descError.put(PARTICIPACION_PROPIETARIOS_EXCEDE,"Verifique la participacion en conjunto de los propietarios excede el 100%.");
		descError.put(ERROR_BUSCAR_DATOS_ASEGURADORAS,"ERROR EN LA BUSQUEDA ASEGURADORAS");
		descError.put(LISTADO_CONCEPTOS_COBERTURA_VACIO,"Verifique, el listado de conceptos del valor de la cobertura esta vacio.");
		descError.put(ERR_LIST_PROPIETARIOS_VINCULACIONES,"ERROR EN LISTADO PROPIETARIOS VINCULACIONES");
		descError.put(SIN_TIPO_GARANTIA,"SELECCIONE EL TIPO DE GARANTIA");
		descError.put(ERROR_AVAL_DUPLICADO,"ERROR_AVAL_DUPLICADO");
		descError.put(ERROR_PRESTAMO_TIENE_COBERTURA_MISMA_GARANTIA,"ERROR_PRESTAMO_TIENE_COBERTURA_MISMA_GARANTIA");
		descError.put(ERROR_PRESTAMO_TIENE_COBERTURA_OTRA_OPRACION_NO_SABANA,"La garantia ya esta coberturando otra operacion y no esta marcada como Garantia Sabana");
		descError.put(USUARIO_SIN_EMPRESA_ASIG,"USUARIO SIN EMPRESA ASIGNADA");
		descError.put(EI_CONSULTA_DATOS_GARANTIA,"ERROR INESPERADO ENCONSULTA DATOS GARANTIA");
		descError.put(EI_GUARDAR_DATOS_GARANTIA,"ERROR INESPERADO AL GUARDAR DATOS GARANTIA");
		descError.put(EI_INSERTA_CONCEPTOS_DATOS_GARANTIA,"ERROR INESPERADO EN INSERTA CONCEPTOS DATOS GARANTIA");
		descError.put(ICDG_TIPO_GTIA_VACIO,"ESPECIFIQUE TIPO DE GARANTIA PARA INSERTAR CONCEPTOS EN DATOS SOLICITUD");
		descError.put(ICDG_SUB_TIPO_GTIA_VACIO,"ESPECIFIQUE SUB TIPO DE GARANTIA PARA INSERTAR CONCEPTOS EN DATOS SOLICITUD");
		descError.put(EI_ACTUALIZA_DATOS_GARANTIA,"ERROR INESPERADO EN ACTUALIZA DATOS GARANTIA");
		descError.put(ACDG_TIPO_GTIA_VACIO,"ESPECIFIQUE TIPO DE GARANTIA PARA ACTUALIZAR CONCEPTOS EN DATOS SOLICITUD");
		descError.put(ACDG_SUB_TIPO_GTIA_VACIO,"ESPECIFIQUE SUB TIPO DE GARANTIA PARA ACTUALIZAR CONCEPTOS EN DATOS SOLICITUD");
		descError.put(ERROR_OBTENER_EJB,"ERROR_OBTENER_EJB");
		descError.put(EI_WS_GARANTIAS_REDISPOSICION,"ERROR EN EL WEBSERVICE GARANTIAS REDISPOSICION");
		descError.put(GTIA_COB_VIG,"LA GARANTIA TIENE COBERTURAS VIGENTES");
		
	}

}

