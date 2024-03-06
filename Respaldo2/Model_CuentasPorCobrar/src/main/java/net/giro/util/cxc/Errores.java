package net.giro.util.cxc;

import java.util.HashMap;

public class Errores { 
	/*Transaccion Exitosa*/
	public static final long TRANSACCION_EXITOSA = 0;

	/*Error Inesperado*/
	public static final long ERROR_INESPERADO = 1;

	/*Verifique, debe seleccionar un criterio del listado de Tipo de busqueda*/
	public static final long CRITERIO_BUSQUEDA = 2;

	/*Capture campos requeridos*/
	public static final long CAMPOS_REQUERIDOS = 3;

	/*Solicitud Autorizada, Imposible Modificar*/
	public static final long SOLICITUD_AUTORIZADA_IMPOSIBLE_MODIFICAR = 4;

	/*Solicitud Rechazada, Imposible Modificar*/
	public static final long SOLICITUD_RECHAZDA_IMPOSIBLE_MODIFICAR = 5;

	/*Listado Vacio, Favor de Verificar */
	public static final long LISTADO_VACIO = 6;

	/*Verifique, cliente invalido para descontar facturas*/
	public static final long CLIENTE_INVALIDO_PARA_DESCONTAR_FACTURAS = 7;

	/*Verifique, el prospecto tiene un registro activo*/
	public static final long PROSPECTO_REGISTRO_ACTIVO = 8;

	/*Error al buscar prospecto
*/
	public static final long ERROR_BUSCAR_PROSPECTO = 9;

	/*Error al Guardar Prospecto*/
	public static final long ERROR_GUARDAR_PROSPECTO = 10;

	/*Error al guardar solicitud*/
	public static final long ERROR_GUARDAR_SOLICITUD = 11;

	/*Error al eliminar proveedor*/
	public static final long ERROR_ELIMINAR_PROVEEDOR = 12;

	/*Error al listar valores*/
	public static final long ERROR_LISTAR_VALORES = 13;

	/*No existe el grupo de valor*/
	public static final long NO_EXISTE_GRUPO_VALOR = 14;

	/*Error al listar sucursales*/
	public static final long ERROR_LISTAR_SUCURSALES = 15;

	/*Error al listar especialistas*/
	public static final long ERROR_LISTAR_ESPECIALISTAS = 16;

	/*Error al buscar Persona*/
	public static final long ERROR_BUSCAR_PERSONA = 17;

	/*Error al buscar Negocio*/
	public static final long ERROR_BUSCAR_NEGOCIO = 18;

	/*Error al Rechazar Solicitud*/
	public static final long ERROR_RECHAZAR_SOLICITUD = 19;

	/*Error al Buscar Solicitud*/
	public static final long ERROR_BUSCAR_SOLICITUD = 20;

	/*Error al Buscar Facturas*/
	public static final long ERROR_LISTAR_FACTURAS = 21;

	/*Error al Autorizar Solicitud*/
	public static final long ERROR_AUTORIZAR_SOLICITUD = 22;

	/*Error al listar Proveedores*/
	public static final long ERROR_LISTAR_PROVEEDORES = 23;

	/*Error al guardar Proveedor*/
	public static final long ERROR_GUARDAR_PROVEEDOR = 24;

	/*Error al Eliminar el Cliente*/
	public static final long ERROR_ELIMINAR_CLIENTE = 25;

	/*Error al Guardar el Cliente*/
	public static final long ERROR_GUARDAR_CLIENTE = 26;

	/*Error al Buscar Proveedor*/
	public static final long ERROR_BUSCAR_PROVEEDOR = 27;

	/*Error al buscar Cliente*/
	public static final long ERRORA_BUSCAR_CLIENTE = 28;

	/*Error al Guardar Documento*/
	public static final long ERROR_GUARDAR_DOCUMENTO = 29;

	/*Error al buscar Clientes*/
	public static final long ERROR_BUSCAR_CLIENTES = 30;

	/*Error al buscar Documento*/
	public static final long ERROR_BUSCAR_DOCUMENTO = 31;

	/*Error al Guardar Referencia*/
	public static final long ERROR_GUARDAR_REFERENCIA = 32;

	/*Error al Buscar Referencia*/
	public static final long ERROR_BUSCAR_REFERENCIA = 33;

	/*Error al Eliminar Referencia*/
	public static final long ERROR_ELIMINAR_REFERENCIA = 34;

	/*Error al Guardar Avales*/
	public static final long ERROR_GUARDAR_AVAL = 35;

	/*Error al Eliminar Aval*/
	public static final long ERROR_ELIMINAR_AVAL = 36;

	/*Error al Buscar Accionistas
*/
	public static final long ERROR_BUSCAR_ACCIONISTAS = 37;

	/*Error al Buscar Directivos*/
	public static final long ERROR_BUSCAR_DIRECTIVOS = 38;

	/*error al Listar Ventas
*/
	public static final long ERROR_LISTAR_VENTAS = 39;

	/*Error al Guardar Ventas
*/
	public static final long ERROR_GUARDAR_VENTAS = 40;

	/*Error al Buscar Venta
*/
	public static final long ERROR_BUSCAR_VENTA = 41;

	/*Error al Eliminar Venta*/
	public static final long ERROR_ELIMINAR_VENTA = 42;

	/*Error al Eliminar Pasivo*/
	public static final long ERROR_ELIMINAR_PASIVO = 43;

	/*error al Guardar Pasivo*/
	public static final long ERROR_GUARDAR_PASIVO = 44;

	/*Error al Buscar Pasivo*/
	public static final long ERROR_BUSCAR_PASIVO = 45;

	/*Error al Guardar Analisis*/
	public static final long ERROR_GUARDAR_ANALISIS = 46;

	/*Error al Eliminar Periodo*/
	public static final long ERROR_ELIMINAR_PERIODO = 47;

	/*Error al Listar Bancos*/
	public static final long ERROR_LISTAR_BANCOS = 48;

	/*Error al Guardar Periodo*/
	public static final long ERROR_GUARDAR_PERIODO = 49;

	/*Error al Listar Productos*/
	public static final long ERROR_LISTAR_PRODUCTOS = 50;

	/*Error al Buscar Producto*/
	public static final long ERROR_BUSCAR_PRODUCTO = 51;

	/*error al Dar de aja el Producto*/
	public static final long ERROR_BAJA_PRODUCTO = 52;

	/*Error al Guardar el Producto*/
	public static final long ERROR_GUARDAR_PRODUCTO = 53;

	/*Error al Listar los Requisitos del Producto*/
	public static final long ERROR_LISTAR_REQUISITOS_PRODUCTO = 54;

	/*Error al Buscar Requisito*/
	public static final long ERROR_BUSCAR_REQUISITO = 55;

	/*Error al Eliminar Requisito*/
	public static final long ERROR_ELIMINAR_REQUISITO = 56;

	/*Error al Guardar Requisito*/
	public static final long ERROR_GUARDAR_REQUISITO = 57;

	/*Error al Buscar Datos del Requisito
*/
	public static final long ERROR_DATOS_REQUISITO = 58;

	/*Error al Eliminar Retencion*/
	public static final long ERROR_ELIMINAR_RETENCION = 59;

	/*Error al Buscar Retencion*/
	public static final long ERROR_BUSCAR_RETENCION = 60;

	/*Error al Guardar Retencion*/
	public static final long ERROR_GUARDAR_RETENCION = 61;

	/*Error al Buscar Datos de la Retencion*/
	public static final long ERROR_DATOS_RETENCION = 62;

	/*Error al Buscar Datos del Producto*/
	public static final long ERROR_DATOS_PRODUCTO = 63;

	/*Error al Listar Retenciones del Producto*/
	public static final long ERROR_LISTAR_RETENCION_PRODUCTO = 64;

	/*error al Listar Documentos de Impresion del Producto*/
	public static final long ERROR_LISTAR_DOCUMENTO_IMPRESION_PRODUCTO = 65;

	/*Error al Buscar Documento de Impresion*/
	public static final long ERROR_BUSCAR_DOCUMENTO_IMPRESION = 66;

	/*Error al Eliminar Documento de Impresion*/
	public static final long ERROR_ELIMINAR_DOCUMENTO_IMPRESION = 67;

	/*Error al Guardar Documento de Impresion*/
	public static final long ERROR_GUARDAR_DOCUMENTO_IMPRESION = 68;

	/*Error al Listar Check List de Producto*/
	public static final long ERROR_LISTAR_CHECKLIST_PRODUCTO = 69;

	/*Error al Eliminar Documento del CheckList */
	public static final long ERROR_ELIMINAR_DOCUMENTO_CHECKLIST = 70;

	/*Error al Buscar Documento del CheckList*/
	public static final long ERROR_BUSCAR_DOCUMENTO_CHECKLIST = 71;

	/*Error al Guardar Documento del CheckList*/
	public static final long ERROR_GUARDAR_DOCUMENTO_CHECKLIST = 72;

	/*Error al Buscar los Datos del Documento de Impresion*/
	public static final long ERROR_BUSCAR_DATOS_DOCUMENTO_IMPRESION = 73;

	/*Error al Buscar los Datos del Documento del CheckList*/
	public static final long ERROR_BUSCAR_DATOS_DOCUMENTOS_CHECKLIST = 74;

	/*Error al Guardar Parametros del Producto*/
	public static final long ERROR_GUARDAR_PARAMETROS_PRODUCTO = 75;

	/*Error al Buscar Tabuladores*/
	public static final long ERROR_BUSCAR_TABULADORES = 76;

	/*Error al Eliminar Tabulador*/
	public static final long ERROR_ELIMINAR_TABULADOR = 77;

	/*Error al Listar Tabuladores del Producto*/
	public static final long ERROR_LISTAR_TABULADORES_PRODUCTO = 78;

	/*Error al Guardar Tabulador*/
	public static final long ERROR_GUARDAR_TABULADOR = 79;

	/*Error al Buscar Datos del Tabulador*/
	public static final long ERROR_BUSCAR_DATOS_TABULADOR = 80;

	/*Verifique, Antiguedad de Relacion Menor a la Permitida*/
	public static final long ANTIGUEDAD_RELACION_MENOR_PERMITIDA = 81;

	/*Error al listar tabulador por monto y plazo*/
	public static final long ERROR_LISTAR_TABULADOR_BY_MONTO_PRODUCTO = 82;

	/*Error al eliminar el tabulador por monto y plazo*/
	public static final long ERROR_ELIMINAR_TABULADOR_BY_MONTO_PLAZO = 83;

	/*Error al guardar el tabulador del Monto y Plazo*/
	public static final long ERROR_GUARDAR_TABULADOR_MONTO_PLAZO = 84;

	/*Error al listar Tasas*/
	public static final long ERROR_LISTAR_TASAS = 85;

	/*Error al buscar Tabulador Monto Plazo*/
	public static final long ERROR_BUSCAR_TABULADOR_BY_MONTO_PLAZO = 86;

	/*Error al eliminar la disposicion del tabulador*/
	public static final long ERROR_ELIMINAR_DISPOSICION_TABULADOR = 87;

	/*Error al listar disposiciones de tabulador*/
	public static final long ERROR_LISTAR_DISPOSICIONES_TABULADOR = 88;

	/*Error al guardar la disposicion del tabulador*/
	public static final long ERROR_GUARDAR_DISPOSICION_TABULADOR = 89;

	/*Error al eliminar el medio de pago de la disposicion*/
	public static final long ERROR_ELIMINAR_MEDIO_PAGO_DISPOSICION = 90;

	/*Error al buscar medios de pago*/
	public static final long ERROR_BUSCAR_MEDIOS_PAGO = 91;

	/*Error al guardar el medio de pago de la disposicion*/
	public static final long ERROR_GUARDAR_MEDIO_PAGO_DISPOSICION = 92;

	/*Error al listar Prospectos*/
	public static final long ERROR_LISTAR_PROSPECTOS = 93;

	/*Error al listar Monedas*/
	public static final long ERROR_LISTAR_MONEDAS = 94;

	/*Error al listar Solicitudes*/
	public static final long ERROR_LISTAR_SOLICITUDES = 95;

	/*Error al listar Negocios*/
	public static final long ERROR_LISTAR_NEGOCIOS = 96;

	/*Error al listar Clientes*/
	public static final long ERROR_LISTAR_CLIENTES = 97;

	/*Error al listar los tipos de documentos*/
	public static final long ERROR_LISTAR_TIPOS_DOCUMENTOS = 98;

	/*Error al eliminar un Documento*/
	public static final long ERROR_ELIMINAR_DOCUMENTO = 99;

	/*Error al listar los tipos de Referencias*/
	public static final long ERROR_LISTAR_TIPOS_REFERENCIAS = 100;

	/*Error al listar Avales*/
	public static final long ERROR_LISTAR_AVALES = 101;

	/*Error al listar los Tipos de Credito*/
	public static final long ERROR_LISTAR_TIPOS_CREDITO = 102;

	/*Error al listar Pasivos*/
	public static final long ERROR_LISTAR_PASIVOS = 103;

	/*Error al listar Saldos Iniciales*/
	public static final long ERROR_LISTAR_SALDOS_INICIALES = 104;

	/*Error al listar Depositos*/
	public static final long ERROR_LISTAR_DEPOSITOS = 105;

	/*Error al listar Retiros*/
	public static final long ERROR_LISTAR_RETIROS = 106;

	/*Error al listar Analisis*/
	public static final long ERROR_LISTAR_ANALISIS = 107;

	/*Error al listar los Motivos de Rechazo*/
	public static final long ERROR_LISTAR_MOTIVOS_RECHAZO = 108;

	/*Busqueda vacia*/
	public static final long BUSQUEDA_VACIA = 109;

	/*Negocio de Proveedor no definido*/
	public static final long ERROR_NEGOCIO_PROVEEDOR = 110;

	/*Verificar el campo Factor de Capacidad de Pago*/
	public static final long ERROR_FACTOR_CAPACIDAD_PAGO = 111;

	/*Error al evaluar la solicitud para modificacion*/
	public static final long ERROR_EVALUA_SOLICITUD_SOLICITUD_MODIFICACION = 112;

	/*Error al evaluar la capacidad de pago*/
	public static final long ERROR_EVALUA_FACTOR_CAPACIDAD_PAGO = 113;

	/*Error al buscar checklist de producto*/
	public static final long ERROR_BUSCAR_CHECKLIST_PRODUCTO = 114;

	/*Error al evaluar el criterio de busqueda de solicitudes*/
	public static final long ERROR_EVALUA_CRITERIO_BUSQUEDA = 115;

	/*Error al eliminar checklist de Producto*/
	public static final long ERROR_ELIMINAR_CHECKLIST_PRODUCTO = 116;

	/*Error al obtener parametros de Producto*/
	public static final long ERROR_OBTENER_PARAMETROS_PRODUCTO = 117;

	/*Error al listar Medios de Pago*/
	public static final long ERROR_LISTAR_MEDIOS_PAGO = 118;

	/*Error al obtener la linea de credito*/
	public static final long ERROR_OBTENER_LINEA_CREDITO = 119;

	/*Error al guarda la linea de credito*/
	public static final long ERROR_GUARDAR_LINEA_CREDITO = 120;

	/*Error al listar CheckLists de solicitud*/
	public static final long ERROR_LISTAR_CHECKLIST_SOLICITUD = 121;

	/*Error al guardar CheckList de Solicitud*/
	public static final long ERROR_GUARDAR_CHECKlIST_SOLICITUD = 122;

	/*Error al eliminar CheckList de solicitud*/
	public static final long ERROR_ELIMINAR_CHECKLIST_SOLICITUD = 123;

	/*Error al realizar la busqueda de contratos.*/
	public static final long ERROR_BUSCAR_CONTRATOS = 124;

	/*Error al autorizar el contrato*/
	public static final long ERROR_AUTORIZAR_CONTRATO = 125;

	/*Error al guardar el contrato*/
	public static final long ERROR_GUARDAR_CONTRATO = 126;

	/*Error al cancelar el contrato*/
	public static final long ERROR_CANCELAR_CONTRATO = 127;

	/*Error al buscar el contrato*/
	public static final long ERROR_BUSCAR_CONTRATO = 128;

	/*Error al eliminar parametros de producto*/
	public static final long ERROR_ELIMINAR_PARAMETROS_PRODUCTO = 129;

	/*Error al verificar si el medio de pago ya esta registrado*/
	public static final long ERROR_MEDIO_PAGO_EXISTENTE = 130;

	/*Verifique, el monto supera el disponible de la linea de credito. Monto Disponible: */
	public static final long MONTO_SUPERIOR_LINEA_CREDITO = 131;

	/*Error al obtener el monto para el contrato*/
	public static final long ERROR_OBTENER_MONTO_CONTRATO = 132;

	/*Error al listar monedas*/
	public static final long ERROR_LISTAR_MONEDA = 133;

	/*Error al asignar numero de control a contrato*/
	public static final long ERROR_ASIGNAR_NUMERO_CONTROL = 134;

	/*IMPOSIBLE CANCELAR, CONTRATO CON DOCUMENTOS DESCONTADOS*/
	public static final long CONTRATO_CON_DOCTOS_DESCONTADOS = 135;

	/*Error al listar los documentos de descuento del contrato*/
	public static final long ERROR_LISTAR_DOCUMENTOS_CONTRATO = 136;

	/*Error al cargar retenciones del contrato*/
	public static final long ERROR_CARGAR_RETENCIONES_CONTRATO = 137;

	/*Error al cargar disposiciones del contrato*/
	public static final long ERROR_CARGAR_DISPOSICIONES_CONTRATO = 138;

	/*Error al generar el descuento del documento*/
	public static final long ERROR_GENERAR_DESCUENTO_DOCUMENTO = 139;

	/*Error al generar pagare del documento*/
	public static final long ERROR_GENERAR_PAGARE_DOCUMENTO = 140;

	/*Imposible calcular monto del documento, verifique los tabuladores*/
	public static final long ERROR_MONTO_POR_TABULADOR = 141;

	/*Error al generar la disposicion del documento*/
	public static final long ERROR_GENERAR_DISPOSICION = 142;

	/*Error al actualizar la disposicion*/
	public static final long ERROR_ACTUALIZA_DISPOSICION = 143;

	/*Error al buscar disposicion*/
	public static final long ERROR_BUSCAR_DISPOSICION = 144;

	/*Error al listar cuentas bancarias*/
	public static final long ERROR_LISTAR_CUENTAS_BANCARIAS = 145;

	/*Error al buscar pagare*/
	public static final long ERROR_BUSCAR_PAGARE = 146;

	/*Error al actualizar el vencimiento del pagare*/
	public static final long ERROR_ACTUALIZA_VENCIMIENTO_PAGARE = 147;

	/*Error al eliminar documento de contrato*/
	public static final long ERROR_ELIMINAR_DOCUMENTO_CONTRATO = 148;

	/*Error al cargar los documentos de impresion*/
	public static final long ERROR_CARGAR_DOCUMENTOS_IMPRESION = 149;

	/*Error al generar documento de impresion de contrato*/
	public static final long ERROR_GENERA_DOCUMENTO_IMPRESION = 150;

	/*Error al generar retenciones de contrato*/
	public static final long ERROR_GENERA_RETENCION_CONTRATO = 151;

	/*Error al imprimir documento*/
	public static final long ERROR_IMPRIMIR_DOCUMENTO = 152;

	/*Verifique, monto superior al disponible de la linea de credito*/
	public static final long ERROR_LINEA_CREDITO_DISPONIBLE = 153;

	/*Imposible descontar, el credito tiene pagos vencidos*/
	public static final long ERROR_PAGARES_VENCIDOS = 153;

	/*Verifique, fecha de vencimiento del documento menor a 10 dias de la fecha actual.*/
	public static final long ERROR_FECHA_VENCIMIENTO_DOCUMENTO = 154;

	/*Error al evaluar formula*/
	public static final long ERROR_EVALUA_FORMULA = 155;

	/*Error al buscar el archivo relacionado con el documento.*/
	public static final long ERROR_BUSCAR_ARCHIVO = 156;

	/*Error al obtener el archivo del servidor.*/
	public static final long ERROR_OBTENER_ARCHIVO = 157;

	/*Error al listar el check list del contrato*/
	public static final long ERROR_LISTAR_CHECKLIST_CONTRATO = 158;

	/*Error al generar el check list de documentos del contrato*/
	public static final long ERROR_GENERAR_CHECKLIST_CONTRATO = 159;

	/*Error no es posible continuar con un documento ya descontado.*/
	public static final long ERROR_DOCUMENTO_DESCONTADO = 160;

	/*Verifique, el monto a pagar es superior al monto del pagare seleccionado*/
	public static final long MONTO_PAGO_SUPERIOR_PAGARE = 161;

	/*Error al guardar el pago*/
	public static final long ERROR_GUARDAR_PAGO = 162;

	/*Error al cargar pagares del contrato*/
	public static final long ERROR_CARGAR_PAGARES_CONTRATO = 163;

	/*No se permite guardar contratos con fechas a futuro.*/
	public static final long ERROR_FECHA_CONTRATO = 164;

	/*No se permite guardar contratos con estatus cancelado o autorizado.*/
	public static final long ERROR_GUARDAR_CONTRATO_ESTATUS = 165;

	/*No se permite editar contratos cancelados.*/
	public static final long ERROR_EDITAR_CONTRATO_CANCELADO = 166;

	/*Error el documento ya existe. Solo se permite un tipo de documento por personalidad.*/
	public static final long ERROR_CHECKLIST_PRODUCTO_EXISTENTE = 167;

	/*Error al generar la comision del documento*/
	public static final long ERROR_GENERAR_COMISION = 168;

	/*Error al cargar listado de estructuras de importacion de pagos*/
	public static final long ERROR_LISTAR_ESTRUSTURAS_IMPORTACION_PAGOS = 169;

	/*Verifique, la cuenta seleccionada ya tiene definida su estructura de importacion de pagos*/
	public static final long ERROR_EXISTE_ESTRUCTURA_CUENTA_BANCARIA = 170;

	/*Error al guardar estructura de importacion de pagos*/
	public static final long ERROR_GUARDAR_ESTRUCTURA_IMPORTACION = 171;

	/*Negocio de Cliente no definido.*/
	public static final long ERROR_NEGOCIO_CLIENTE = 172;

	/*Cliente de Documento no definido.*/
	public static final long ERROR_CLIENTE_DOCUMENTO = 173;

	/*Error al eliminar estructura de importacion de pagos*/
	public static final long ERROR_ELIMINAR_ESTRUCTURA_IMPOTACION_PAGOS = 174;

	/*Persona / Negocio de Referencia no definida.*/
	public static final long ERROR_PERSONA_NEGOCIO_REFERENCIA = 175;

	/*Error al eliminar pago*/
	public static final long ERROR_ELIMINAR_PAGO = 176;

	/*Error al generar el estado de cuenta*/
	public static final long ERROR_GENERAR_ESTADO_CUENTA = 177;

	/*Error el monto de aforo del documento supera la mitad del monto autorizado de la linea de credito*/
	public static final long ERROR_MONTO_AFORO_MAYOR_MITAD = 178;

	/*Error, no se permite monto aforado con porcentaje de aforo mayor a 90%*/
	public static final long ERROR_PORCENTAJE_AFORO_SUPERA_LIMITE = 179;

	/*Error al obtener el monto descontado.*/
	public static final long ERROR_OBTENER_MONTO_DESCONTADO = 180;

	/*Error al cargar pagos*/
	public static final long ERROR_CARGAR_PAGOS = 181;

	/*Verifique, el monto a pagar es superior al monto del pagare seleccionado*/
	public static final long ERROR_MONTO_PAGO_SUPERIOR = 182;

	/*Error al calcular intereses*/
	public static final long ERROR_CALCULO_INTERESES = 183;

	/*No se permite cancelar pagos ya cancelados.*/
	public static final long ERROR_PAGO_CANCELADO = 184;

	/*Error, el pagare no cuenta con fecha de vencimiento*/
	public static final long ERROR_FALTA_FECHA_VENCIMIENTO = 185;

	/*Error, no se encuentra linea de credito*/
	public static final long ERROR_LINEA_CREDITO_INEXISTENTE = 186;

	/*Error, el documento no se ha descontado*/
	public static final long ERROR_DOCUMENTO_NO_DESCONTADO = 187;

	/*Error, el cliente no es elegible para descontar*/
	public static final long ERROR_CLIENTE_NO_ELEGIBLE = 188;

	/*Verifique el porcentaje de credito y contado, el total del porcentaje no debe superar el 100%*/
	public static final long ERROR_PORCENTAJE_CREDITO_CONTADO = 189;

	/*Verifique porcentaje, no debe superar 100%*/
	public static final long ERROR_PORCENTAJE_SUPERA_CIEN = 190;

	/*Error al guardar el acta de comite*/
	public static final long ERROR_GUARDAR_ACTA_COMITE = 191;

	/*Error al guardar detalle del acta de comite*/
	public static final long ERROR_GUARDAR_ACTA_COMITE_DETALLE = 192;

	/*Error al guardar acta de comite y detalles*/
	public static final long ERROR_GUARDAR_ACTA_COMITE_Y_DETALLE = 193;

	/*Error al listar actas de comite*/
	public static final long ERROR_LISTAR_ACTA_COMITE = 194;

	/*Error al imprimir el acta de comite*/
	public static final long ERROR_IMPRIMIR_ACTA_COMITE = 195;

	/*Error al generar el reporte*/
	public static final long ERROR_GENERAR_REPORTE = 196;

	/*Error al generar el reporte personas elegibles*/
	public static final long ERROR_GENERAR_PERSONAS_ELEGIBLES = 197;

	/*Error al generar el acta del comite*/
	public static final long ERROR_GENERAR_ACTA_COMITE = 198;

	/*Error al generar autorizacion de terminos y condiciones*/
	public static final long ERROR_GENERAR_AUTORIZACION_TERMINOS_CONDICIONES = 199;

	/*Verifique, uno de los contratos ya se encuentra agregado a otra acta de comite*/
	public static final long ERROR_CONTRATO_ACTA_COMITE = 200;

	/*Error al imprimir pagare*/
	public static final long ERROR_IMPRIMIR_PAGARE = 201;

	/*Error al generar la relacion de documentos para descuento*/
	public static final long ERROR_GENERAR_RELACION_DOCUMENTOS_DESCUENTO = 202;

	/*Error al imprimir documento de contrato*/
	public static final long ERROR_IMPRIMIR_DOCUMENTO_CONTRATO = 203;
	
	//ERRORES AGREGADOS SIN USAR EL SOFTWARE DE LA 11.200
	
	/*Error al buscar la estructura de importacion de pagos de la cuenta*/
	public static final long ERROR_BUSCAR_ESTRUCTURA_IMPORTACION_PAGOS = 204;
	
	/*Error al importar pagos, verifique que la estructura y el archivo sean correctos*/
	public static final long ERROR_IMPORTAR_PAGOS = 205;
	
	//ERRORES AGREGADOS SIN USAR EL SOFTWARE DE LA 11.200
	
	/*Verifique, la fecha de expedicion del documento excede la antiguedad maxima permitida*/
	public static final long ERROR_ANTIGUEDAD_MAXIMA_EXCEDIDAD = 206;
	
	/*Verifique, el plazo para el pagare no puede ser negativo*/
	public static final long ERROR_PLAZO_PAGARE_NEGATIVO = 207;
	
	/*Indique la fecha de aforo*/
	public static final long ERROR_FECHA_AFORO = 208;
	
	/*Verifique, no cuentas con permisos para eliminar un documento descontado*/
	public static final long ERROR_PERFIL_DOCUMENTO_DESCONTADO = 209;

	/*Verifique, no se permite eliminar documentos que contiengan pagos*/
	public static final long ERROR_ELIMINAR_DOCUMENTO_PAGADO = 210;
	
	/*Error al guardar calculo de interes*/
	public static final long ERROR_GUARDAR_CALCULO_INTERES_CONTRATO = 211;
	
	/*Error al cargar calculo de interes*/
	public static final long ERROR_CARGAR_CALCULO_INTERES_CONTRATO = 212;
	
	/*Verifique, no se permite calcular interes con una fecha anterior a un pago*/
	public static final long ERROR_FECHA_CALCULO_INTERES_ANTERIOR_PAGO = 213;
	
	/*Error al buscar contrato por referencia*/
	public static final long ERROR_BUSCAR_CONTRATO_REFERENCIA = 214;
	
	/*Error al guardar y aplicar importacion*/
	public static final long ERROR_GUARDAR_APLICAR_IMPORTACION = 215;
	
	/*Verifique, el archivo a importar ya ha sido importado anteriormente*/
	public static final long ERROR_ARCHIVO_IMPORTADO = 216;
	
	/*Error al generar reporte importacion de pago*/
	public static final long ERROR_REPORTE_IMPORTACION_PAGO = 217;
	
	/*Error al imprimir solicitud*/
	public static final long ERROR_IMPRIMIR_SOLICITUD = 218;
	
	/*Error al calcular el monto de la linea de factoraje*/
	public static final long ERROR_CALCULAR_MONTO_LINEA_FACTORAJE = 219;
	
	/*Error al analizar XML*/
	public static final long ERROR_ANALIZAR_XML = 220;
	
	/*Error al guardar negocio*/
	public static final long ERROR_GUARDAR_NEGOCIO = 221;
	
	/*Error al guardar datos de cliente y negocio*/
	public static final long ERROR_GUARDAR_CLIENTE_NEGOCIO = 223;
	
	/*Verifique, el rfc del emisor de la factura no corresponde al cliente del contrato*/
	public static final long ERROR_RFC_EMISOR_INCORRECTO = 224;
	
	/*Error al analizar archivo, verifique que el archivo sea correcto*/
	public static final long ERROR_ANALIZAR_ARCHIVO = 225;
	
	/*Error en la fecha de vencimiento del documento, verifique fecha expedicion y plazo*/
	public static final long ERROR_FECHA_VENCIMIENTO_INVALIDA = 226;
	
	/*Error al obtener reporte*/
	public static final long ERROR_OBTENER_REPORTE = 227;
	
	/*Error al buscar giros*/
	public static final long ERROR_BUSCAR_GIROS = 228;
	
	/*Error al buscar oficiales*/
	public static final long ERROR_BUSCAR_OFICIALES = 229;
	
	/*Error al buscar Sectores*/
	public static final long ERROR_BUSCAR_SECTORES = 230;
	
	/*Error al buscar Sucurales*/
	public static final long ERROR_BUSCAR_SUCURSALES = 231;
	
	/*Verifique, falta seleccionar pagare*/
	public static final long ERROR_FALTA_PAGARE = 232;
	
	/*Error al obtener fecha de descuento*/
	public static final long ERROR_OBTENER_FECHA_DESCUENTO = 233;
	
	/*El numero de control ya existe*/
	public static final long ERROR_CONTRATO_GUARDAR_NUMERO_CONTROL_EXISTENTE = 234;
	
	/*Ya existe un documento con este FOLIO para este cliente*/
	public static final long ERROR_DOCUMENTO_EXISTENTE_CLIENTE = 235;
	
	/*El Documento ya ha sido agregado previamente*/
	public static final long ERROR_DOCUMENTO_PREVIO = 236;
	
	/*La fecha de aforo no debe ser menor a la fecha de expedicion del documento*/
	public static final long ERROR_FECHA_AFORO_INVALIDA = 237;
	
	/*El Documento ya ha sido agregado previamente*/
	public static final long ERROR_XML_PREVIO = 238;
	
	/*FORMATO NO VALIDO*/
	public static final long ERROR_FORMATO_NOVALIDO = 239;
	
	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(TRANSACCION_EXITOSA,"Transaccion Exitosa");
		descError.put(ERROR_INESPERADO,"Error Inesperado");
		descError.put(CRITERIO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de busqueda");
		descError.put(CAMPOS_REQUERIDOS,"Capture campos requeridos");
		descError.put(SOLICITUD_AUTORIZADA_IMPOSIBLE_MODIFICAR,"Solicitud Autorizada, Imposible Modificar");
		descError.put(SOLICITUD_RECHAZDA_IMPOSIBLE_MODIFICAR,"Solicitud Rechazada, Imposible Modificar");
		descError.put(LISTADO_VACIO,"Listado Vacio, Favor de Verificar");
		descError.put(CLIENTE_INVALIDO_PARA_DESCONTAR_FACTURAS,"Verifique, cliente invalido para descontar facturas");
		descError.put(PROSPECTO_REGISTRO_ACTIVO,"Verifique, el prospecto tiene un registro activo");
		descError.put(ERROR_BUSCAR_PROSPECTO,"Error al buscar prospecto");
		descError.put(ERROR_GUARDAR_PROSPECTO,"Error al Guardar Prospecto");
		descError.put(ERROR_GUARDAR_SOLICITUD,"Error al guardar solicitud");
		descError.put(ERROR_ELIMINAR_PROVEEDOR,"Error al eliminar proveedor");
		descError.put(ERROR_LISTAR_VALORES,"Error al listar valores");
		descError.put(NO_EXISTE_GRUPO_VALOR,"No existe el grupo de valor");
		descError.put(ERROR_LISTAR_SUCURSALES,"Error al listar sucursales");
		descError.put(ERROR_LISTAR_ESPECIALISTAS,"Error al listar especialistas");
		descError.put(ERROR_BUSCAR_PERSONA,"Error al buscar Persona");
		descError.put(ERROR_BUSCAR_NEGOCIO,"Error al buscar Negocio");
		descError.put(ERROR_RECHAZAR_SOLICITUD,"Error al Rechazar Solicitud");
		descError.put(ERROR_BUSCAR_SOLICITUD,"Error al Buscar Solicitud");
		descError.put(ERROR_LISTAR_FACTURAS,"Error al Buscar Facturas");
		descError.put(ERROR_AUTORIZAR_SOLICITUD,"Error al Autorizar Solicitud");
		descError.put(ERROR_LISTAR_PROVEEDORES,"Error al listar Proveedores");
		descError.put(ERROR_GUARDAR_PROVEEDOR,"Error al guardar Proveedor");
		descError.put(ERROR_ELIMINAR_CLIENTE,"Error al Eliminar el Cliente");
		descError.put(ERROR_GUARDAR_CLIENTE,"Error al Guardar el Cliente");
		descError.put(ERROR_BUSCAR_PROVEEDOR,"Error al Buscar Proveedor");
		descError.put(ERRORA_BUSCAR_CLIENTE,"Error al buscar Cliente");
		descError.put(ERROR_GUARDAR_DOCUMENTO,"Error al Guardar Documento");
		descError.put(ERROR_BUSCAR_CLIENTES,"Error al buscar Clientes");
		descError.put(ERROR_BUSCAR_DOCUMENTO,"Error al buscar Documento");
		descError.put(ERROR_GUARDAR_REFERENCIA,"Error al Guardar Referencia");
		descError.put(ERROR_BUSCAR_REFERENCIA,"Error al Buscar Referencia");
		descError.put(ERROR_ELIMINAR_REFERENCIA,"Error al Eliminar Referencia");
		descError.put(ERROR_GUARDAR_AVAL,"Error al Guardar Avales");
		descError.put(ERROR_ELIMINAR_AVAL,"Error al Eliminar Aval");
		descError.put(ERROR_BUSCAR_ACCIONISTAS,"Error al Buscar Accionistas");
		descError.put(ERROR_BUSCAR_DIRECTIVOS,"Error al Buscar Directivos");
		descError.put(ERROR_LISTAR_VENTAS,"error al Listar Ventas");
		descError.put(ERROR_GUARDAR_VENTAS,"Error al Guardar Ventas");
		descError.put(ERROR_BUSCAR_VENTA,"Error al Buscar Venta");
		descError.put(ERROR_ELIMINAR_VENTA,"Error al Eliminar Venta");
		descError.put(ERROR_ELIMINAR_PASIVO,"Error al Eliminar Pasivo");
		descError.put(ERROR_GUARDAR_PASIVO,"error al Guardar Pasivo");
		descError.put(ERROR_BUSCAR_PASIVO,"Error al Buscar Pasivo");
		descError.put(ERROR_GUARDAR_ANALISIS,"Error al Guardar Analisis");
		descError.put(ERROR_ELIMINAR_PERIODO,"Error al Eliminar Periodo");
		descError.put(ERROR_LISTAR_BANCOS,"Error al Listar Bancos");
		descError.put(ERROR_GUARDAR_PERIODO,"Error al Guardar Periodo");
		descError.put(ERROR_LISTAR_PRODUCTOS,"Error al Listar Productos");
		descError.put(ERROR_BUSCAR_PRODUCTO,"Error al Buscar Producto");
		descError.put(ERROR_BAJA_PRODUCTO,"error al Dar de aja el Producto");
		descError.put(ERROR_GUARDAR_PRODUCTO,"Error al Guardar el Producto");
		descError.put(ERROR_LISTAR_REQUISITOS_PRODUCTO,"Error al Listar los Requisitos del Producto");
		descError.put(ERROR_BUSCAR_REQUISITO,"Error al Buscar Requisito");
		descError.put(ERROR_ELIMINAR_REQUISITO,"Error al Eliminar Requisito");
		descError.put(ERROR_GUARDAR_REQUISITO,"Error al Guardar Requisito");
		descError.put(ERROR_DATOS_REQUISITO,"Error al Buscar Datos del Requisito");
		descError.put(ERROR_ELIMINAR_RETENCION,"Error al Eliminar Retencion");
		descError.put(ERROR_BUSCAR_RETENCION,"Error al Buscar Retencion");
		descError.put(ERROR_GUARDAR_RETENCION,"Error al Guardar Retencion");
		descError.put(ERROR_DATOS_RETENCION,"Error al Buscar Datos de la Retencion");
		descError.put(ERROR_DATOS_PRODUCTO,"Error al Buscar Datos del Producto");
		descError.put(ERROR_LISTAR_RETENCION_PRODUCTO,"Error al Listar Retenciones del Producto");
		descError.put(ERROR_LISTAR_DOCUMENTO_IMPRESION_PRODUCTO,"error al Listar Documentos de Impresion del Producto");
		descError.put(ERROR_BUSCAR_DOCUMENTO_IMPRESION,"Error al Buscar Documento de Impresion");
		descError.put(ERROR_ELIMINAR_DOCUMENTO_IMPRESION,"Error al Eliminar Documento de Impresion");
		descError.put(ERROR_GUARDAR_DOCUMENTO_IMPRESION,"Error al Guardar Documento de Impresion");
		descError.put(ERROR_LISTAR_CHECKLIST_PRODUCTO,"Error al Listar Check List de Producto");
		descError.put(ERROR_ELIMINAR_DOCUMENTO_CHECKLIST,"Error al Eliminar Documento del CheckList");
		descError.put(ERROR_BUSCAR_DOCUMENTO_CHECKLIST,"Error al Buscar Documento del CheckList");
		descError.put(ERROR_GUARDAR_DOCUMENTO_CHECKLIST,"Error al Guardar Documento del CheckList");
		descError.put(ERROR_BUSCAR_DATOS_DOCUMENTO_IMPRESION,"Error al Buscar los Datos del Documento de Impresion");
		descError.put(ERROR_BUSCAR_DATOS_DOCUMENTOS_CHECKLIST,"Error al Buscar los Datos del Documento del CheckList");
		descError.put(ERROR_GUARDAR_PARAMETROS_PRODUCTO,"Error al Guardar Parametros del Producto");
		descError.put(ERROR_BUSCAR_TABULADORES,"Error al Buscar Tabuladores");
		descError.put(ERROR_ELIMINAR_TABULADOR,"Error al Eliminar Tabulador");
		descError.put(ERROR_LISTAR_TABULADORES_PRODUCTO,"Error al Listar Tabuladores del Producto");
		descError.put(ERROR_GUARDAR_TABULADOR,"Error al Guardar Tabulador");
		descError.put(ERROR_BUSCAR_DATOS_TABULADOR,"Error al Buscar Datos del Tabulador");
		descError.put(ANTIGUEDAD_RELACION_MENOR_PERMITIDA,"Verifique, Antiguedad de Relacion Menor a la Permitida");
		descError.put(ERROR_LISTAR_TABULADOR_BY_MONTO_PRODUCTO,"Error al listar tabulador por monto y plazo");
		descError.put(ERROR_ELIMINAR_TABULADOR_BY_MONTO_PLAZO,"Error al eliminar el tabulador por monto y plazo");
		descError.put(ERROR_GUARDAR_TABULADOR_MONTO_PLAZO,"Error al guardar el tabulador del Monto y Plazo");
		descError.put(ERROR_LISTAR_TASAS,"Error al listar Tasas");
		descError.put(ERROR_BUSCAR_TABULADOR_BY_MONTO_PLAZO,"Error al buscar Tabulador Monto Plazo");
		descError.put(ERROR_ELIMINAR_DISPOSICION_TABULADOR,"Error al eliminar la disposicion del tabulador");
		descError.put(ERROR_LISTAR_DISPOSICIONES_TABULADOR,"Error al listar disposiciones de tabulador");
		descError.put(ERROR_GUARDAR_DISPOSICION_TABULADOR,"Error al guardar la disposicion del tabulador");
		descError.put(ERROR_ELIMINAR_MEDIO_PAGO_DISPOSICION,"Error al eliminar el medio de pago de la disposicion");
		descError.put(ERROR_BUSCAR_MEDIOS_PAGO,"Error al buscar medios de pago");
		descError.put(ERROR_GUARDAR_MEDIO_PAGO_DISPOSICION,"Error al guardar el medio de pago de la disposicion");
		descError.put(ERROR_LISTAR_PROSPECTOS,"Error al listar Prospectos");
		descError.put(ERROR_LISTAR_MONEDAS,"Error al listar Monedas");
		descError.put(ERROR_LISTAR_SOLICITUDES,"Error al listar Solicitudes");
		descError.put(ERROR_LISTAR_NEGOCIOS,"Error al listar Negocios");
		descError.put(ERROR_LISTAR_CLIENTES,"Error al listar Clientes");
		descError.put(ERROR_LISTAR_TIPOS_DOCUMENTOS,"Error al listar los tipos de documentos");
		descError.put(ERROR_ELIMINAR_DOCUMENTO,"Error al eliminar un Documento");
		descError.put(ERROR_LISTAR_TIPOS_REFERENCIAS,"Error al listar los tipos de Referencias");
		descError.put(ERROR_LISTAR_AVALES,"Error al listar Avales");
		descError.put(ERROR_LISTAR_TIPOS_CREDITO,"Error al listar los Tipos de Credito");
		descError.put(ERROR_LISTAR_PASIVOS,"Error al listar Pasivos");
		descError.put(ERROR_LISTAR_SALDOS_INICIALES,"Error al listar Saldos Iniciales");
		descError.put(ERROR_LISTAR_DEPOSITOS,"Error al listar Depositos");
		descError.put(ERROR_LISTAR_RETIROS,"Error al listar Retiros");
		descError.put(ERROR_LISTAR_ANALISIS,"Error al listar Analisis");
		descError.put(ERROR_LISTAR_MOTIVOS_RECHAZO,"Error al listar los Motivos de Rechazo");
		descError.put(BUSQUEDA_VACIA,"Busqueda vacia");
		descError.put(ERROR_NEGOCIO_PROVEEDOR,"Negocio de Proveedor no definido");
		descError.put(ERROR_FACTOR_CAPACIDAD_PAGO,"Verificar el campo Factor de Capacidad de Pago");
		descError.put(ERROR_EVALUA_SOLICITUD_SOLICITUD_MODIFICACION,"Error al evaluar la solicitud para modificacion");
		descError.put(ERROR_EVALUA_FACTOR_CAPACIDAD_PAGO,"Error al evaluar la capacidad de pago");
		descError.put(ERROR_BUSCAR_CHECKLIST_PRODUCTO,"Error al buscar checklist de producto");
		descError.put(ERROR_EVALUA_CRITERIO_BUSQUEDA,"Error al evaluar el criterio de busqueda de solicitudes");
		descError.put(ERROR_ELIMINAR_CHECKLIST_PRODUCTO,"Error al eliminar checklist de Producto");
		descError.put(ERROR_OBTENER_PARAMETROS_PRODUCTO,"Error al obtener parametros de Producto");
		descError.put(ERROR_LISTAR_MEDIOS_PAGO,"Error al listar Medios de Pago");
		descError.put(ERROR_OBTENER_LINEA_CREDITO,"Error al obtener la linea de credito");
		descError.put(ERROR_GUARDAR_LINEA_CREDITO,"Error al guarda la linea de credito");
		descError.put(ERROR_LISTAR_CHECKLIST_SOLICITUD,"Error al listar CheckLists de solicitud");
		descError.put(ERROR_GUARDAR_CHECKlIST_SOLICITUD,"Error al guardar CheckList de Solicitud");
		descError.put(ERROR_ELIMINAR_CHECKLIST_SOLICITUD,"Error al eliminar CheckList de solicitud");
		descError.put(ERROR_BUSCAR_CONTRATOS,"Error al realizar la busqueda de contratos.");
		descError.put(ERROR_AUTORIZAR_CONTRATO,"Error al autorizar el contrato");
		descError.put(ERROR_GUARDAR_CONTRATO,"Error al guardar el contrato");
		descError.put(ERROR_CANCELAR_CONTRATO,"Error al cancelar el contrato");
		descError.put(ERROR_BUSCAR_CONTRATO,"Error al buscar el contrato");
		descError.put(ERROR_ELIMINAR_PARAMETROS_PRODUCTO,"Error al eliminar parametros de producto");
		descError.put(ERROR_MEDIO_PAGO_EXISTENTE,"Error al verificar si el medio de pago ya esta registrado");
		descError.put(MONTO_SUPERIOR_LINEA_CREDITO,"Verifique, el monto supera el disponible de la linea de credito. Monto Disponible:");
		descError.put(ERROR_OBTENER_MONTO_CONTRATO,"Error al obtener el monto para el contrato");
		descError.put(ERROR_LISTAR_MONEDA,"Error al listar monedas");
		descError.put(ERROR_ASIGNAR_NUMERO_CONTROL,"Error al asignar numero de control a contrato");
		descError.put(CONTRATO_CON_DOCTOS_DESCONTADOS,"IMPOSIBLE CANCELAR, CONTRATO CON DOCUMENTOS DESCONTADOS");
		descError.put(ERROR_LISTAR_DOCUMENTOS_CONTRATO,"Error al listar los documentos de descuento del contrato");
		descError.put(ERROR_CARGAR_RETENCIONES_CONTRATO,"Error al cargar retenciones del contrato");
		descError.put(ERROR_CARGAR_DISPOSICIONES_CONTRATO,"Error al cargar disposiciones del contrato");
		descError.put(ERROR_GENERAR_DESCUENTO_DOCUMENTO,"Error al generar el descuento del documento");
		descError.put(ERROR_GENERAR_PAGARE_DOCUMENTO,"Error al generar pagare del documento");
		descError.put(ERROR_MONTO_POR_TABULADOR,"Imposible calcular monto del documento, verifique los tabuladores");
		descError.put(ERROR_GENERAR_DISPOSICION,"Error al generar la disposicion del documento");
		descError.put(ERROR_ACTUALIZA_DISPOSICION,"Error al actualizar la disposicion");
		descError.put(ERROR_BUSCAR_DISPOSICION,"Error al buscar disposicion");
		descError.put(ERROR_LISTAR_CUENTAS_BANCARIAS,"Error al listar cuentas bancarias");
		descError.put(ERROR_BUSCAR_PAGARE,"Error al buscar pagare");
		descError.put(ERROR_ACTUALIZA_VENCIMIENTO_PAGARE,"Error al actualizar el vencimiento del pagare");
		descError.put(ERROR_ELIMINAR_DOCUMENTO_CONTRATO,"Error al eliminar documento de contrato");
		descError.put(ERROR_CARGAR_DOCUMENTOS_IMPRESION,"Error al cargar los documentos de impresion");
		descError.put(ERROR_GENERA_DOCUMENTO_IMPRESION,"Error al generar documento de impresion de contrato");
		descError.put(ERROR_GENERA_RETENCION_CONTRATO,"Error al generar retenciones de contrato");
		descError.put(ERROR_IMPRIMIR_DOCUMENTO,"Error al imprimir documento");
		descError.put(ERROR_LINEA_CREDITO_DISPONIBLE,"Verifique, monto superior al disponible de la linea de credito");
		descError.put(ERROR_PAGARES_VENCIDOS,"Imposible descontar, el credito tiene pagos vencidos");
		descError.put(ERROR_FECHA_VENCIMIENTO_DOCUMENTO,"Verifique, fecha de vencimiento del documento menor a 10 dias de la fecha actual.");
		descError.put(ERROR_EVALUA_FORMULA,"Error al evaluar formula");
		descError.put(ERROR_BUSCAR_ARCHIVO,"Error al buscar el archivo relacionado con el documento.");
		descError.put(ERROR_OBTENER_ARCHIVO,"Error al obtener el archivo del servidor.");
		descError.put(ERROR_LISTAR_CHECKLIST_CONTRATO,"Error al listar el check list del contrato");
		descError.put(ERROR_GENERAR_CHECKLIST_CONTRATO,"Error al generar el check list de documentos del contrato");
		descError.put(ERROR_DOCUMENTO_DESCONTADO,"Error no es posible continuar con un documento ya descontado.");
		descError.put(MONTO_PAGO_SUPERIOR_PAGARE,"Verifique, el monto a pagar es superior al monto del pagare seleccionado");
		descError.put(ERROR_GUARDAR_PAGO,"Error al guardar el pago");
		descError.put(ERROR_CARGAR_PAGARES_CONTRATO,"Error al cargar pagares del contrato");
		descError.put(ERROR_FECHA_CONTRATO,"No se permite guardar contratos con fechas a futuro.");
		descError.put(ERROR_GUARDAR_CONTRATO_ESTATUS,"No se permite guardar contratos con estatus cancelado o autorizado.");
		descError.put(ERROR_EDITAR_CONTRATO_CANCELADO,"No se permite editar contratos cancelados.");
		descError.put(ERROR_CHECKLIST_PRODUCTO_EXISTENTE,"Error el documento ya existe. Solo se permite un tipo de documento por personalidad.");
		descError.put(ERROR_GENERAR_COMISION,"Error al generar la comision del documento");
		descError.put(ERROR_LISTAR_ESTRUSTURAS_IMPORTACION_PAGOS,"Error al cargar listado de estructuras de importacion de pagos");
		descError.put(ERROR_EXISTE_ESTRUCTURA_CUENTA_BANCARIA,"Verifique, la cuenta seleccionada ya tiene definida su estructura de importacion de pagos");
		descError.put(ERROR_GUARDAR_ESTRUCTURA_IMPORTACION,"Error al guardar estructura de importacion de pagos");
		descError.put(ERROR_NEGOCIO_CLIENTE,"Negocio de Cliente no definido.");
		descError.put(ERROR_CLIENTE_DOCUMENTO,"Cliente de Documento no definido.");
		descError.put(ERROR_ELIMINAR_ESTRUCTURA_IMPOTACION_PAGOS,"Error al eliminar estructura de importacion de pagos");
		descError.put(ERROR_PERSONA_NEGOCIO_REFERENCIA,"Persona / Negocio de Referencia no definida.");
		descError.put(ERROR_ELIMINAR_PAGO,"Error al eliminar pago");
		descError.put(ERROR_GENERAR_ESTADO_CUENTA,"Error al generar el estado de cuenta");
		descError.put(ERROR_MONTO_AFORO_MAYOR_MITAD,"Error el monto de aforo del documento supera la mitad del monto autorizado de la linea de credito");
		descError.put(ERROR_PORCENTAJE_AFORO_SUPERA_LIMITE,"Error, no se permite monto aforado con porcentaje de aforo mayor a 90%");
		descError.put(ERROR_OBTENER_MONTO_DESCONTADO,"Error al obtener el monto descontado.");
		descError.put(ERROR_CARGAR_PAGOS,"Error al cargar pagos");
		descError.put(ERROR_MONTO_PAGO_SUPERIOR,"Verifique, el monto a pagar es superior al monto del pagare seleccionado");
		descError.put(ERROR_CALCULO_INTERESES,"Error al calcular intereses");
		descError.put(ERROR_PAGO_CANCELADO,"No se permite cancelar pagos ya cancelados.");
		descError.put(ERROR_FALTA_FECHA_VENCIMIENTO,"Error, el pagare no cuenta con fecha de vencimiento");
		descError.put(ERROR_LINEA_CREDITO_INEXISTENTE,"Error, no se encuentra linea de credito");
		descError.put(ERROR_DOCUMENTO_NO_DESCONTADO,"Error, el documento no se ha descontado");
		descError.put(ERROR_CLIENTE_NO_ELEGIBLE,"Error, el cliente no es elegible para descontar");
		descError.put(ERROR_PORCENTAJE_CREDITO_CONTADO,"Verifique el porcentaje de credito y contado, el total del porcentaje no debe superar el 100%");
		descError.put(ERROR_PORCENTAJE_SUPERA_CIEN,"Verifique porcentaje, no debe superar 100%");
		descError.put(ERROR_GUARDAR_ACTA_COMITE,"Error al guardar el acta de comite");
		descError.put(ERROR_GUARDAR_ACTA_COMITE_DETALLE,"Error al guardar detalle del acta de comite");
		descError.put(ERROR_GUARDAR_ACTA_COMITE_Y_DETALLE,"Error al guardar acta de comite y detalles");
		descError.put(ERROR_LISTAR_ACTA_COMITE,"Error al listar actas de comite");
		descError.put(ERROR_IMPRIMIR_ACTA_COMITE,"Error al imprimir el acta de comite");
		descError.put(ERROR_GENERAR_REPORTE,"Error al generar el reporte");
		descError.put(ERROR_GENERAR_PERSONAS_ELEGIBLES,"Error al generar el reporte personas elegibles");
		descError.put(ERROR_GENERAR_ACTA_COMITE,"Error al generar el acta del comite");
		descError.put(ERROR_GENERAR_AUTORIZACION_TERMINOS_CONDICIONES,"Error al generar autorizacion de terminos y condiciones");
		descError.put(ERROR_CONTRATO_ACTA_COMITE,"Verifique, uno de los contratos ya se encuentra agregado a otra acta de comite");
		descError.put(ERROR_IMPRIMIR_PAGARE,"Error al imprimir pagare");
		descError.put(ERROR_GENERAR_RELACION_DOCUMENTOS_DESCUENTO,"Error al generar la relacion de documentos para descuento");
		descError.put(ERROR_IMPRIMIR_DOCUMENTO_CONTRATO,"Error al imprimir documento de contrato");
		descError.put(ERROR_BUSCAR_ESTRUCTURA_IMPORTACION_PAGOS, "Error al cargar la estructura de importacion de pagos de la cuenta");
		descError.put(ERROR_IMPORTAR_PAGOS, "Error al importar pagos, verifique que la estructura y el archivo sean correctos");
		descError.put(ERROR_ANTIGUEDAD_MAXIMA_EXCEDIDAD, "Verifique, la fecha de expedicion del documento excede la antiguedad maxima permitida");
		descError.put(ERROR_PLAZO_PAGARE_NEGATIVO, "Verifique, el plazo para el pagare no puede ser negativo");
		descError.put(ERROR_FECHA_AFORO, "Indique la fecha de aforo");
		descError.put(ERROR_PERFIL_DOCUMENTO_DESCONTADO, "Verifique, no cuentas con permisos para eliminar un documento descontado");
		descError.put(ERROR_ELIMINAR_DOCUMENTO_PAGADO, "Verifique, no se permite eliminar documentos que contiengan pagos");
		descError.put(ERROR_GUARDAR_CALCULO_INTERES_CONTRATO, "Error al guardar calculo de interes");
		descError.put(ERROR_CARGAR_CALCULO_INTERES_CONTRATO, "Error al cargar calculo de interes");
		descError.put(ERROR_FECHA_CALCULO_INTERES_ANTERIOR_PAGO, "Verifique, no se permite calcular interes con una fecha anterior a un pago");
		descError.put(ERROR_BUSCAR_CONTRATO_REFERENCIA, "Error al buscar contrato por referencia");
		descError.put(ERROR_GUARDAR_APLICAR_IMPORTACION, "Error al guardar y aplicar importacion");
		descError.put(ERROR_ARCHIVO_IMPORTADO, "Verifique, el archivo a importar ya ha sido importado anteriormente");
		descError.put(ERROR_REPORTE_IMPORTACION_PAGO, "Error al generar reporte importacion de pago");
		descError.put(ERROR_IMPRIMIR_SOLICITUD, "Error al imprimir solicitud");
		descError.put(ERROR_CALCULAR_MONTO_LINEA_FACTORAJE, "Error al calcular el monto de la linea de factoraje");
		descError.put(ERROR_ANALIZAR_XML, "Error al analizar XML");
		descError.put(ERROR_GUARDAR_NEGOCIO, "Error al guardar negocio");
		descError.put(ERROR_GUARDAR_CLIENTE_NEGOCIO, "Error al guardar datos de cliente y negocio");
		descError.put(ERROR_RFC_EMISOR_INCORRECTO, "Verifique, el rfc del emisor de la factura no corresponde al cliente del contrato");
		descError.put(ERROR_ANALIZAR_ARCHIVO, "Error al analizar archivo, verifique que el archivo sea correcto");
		descError.put(ERROR_FECHA_VENCIMIENTO_INVALIDA, "Error en la fecha de vencimiento del documento, verifique fecha expedicion y plazo");
		descError.put(ERROR_OBTENER_REPORTE, "Error al obtener reporte");
		descError.put(ERROR_BUSCAR_GIROS, "Error al buscar giros");
		descError.put(ERROR_BUSCAR_OFICIALES, "Error al buscar oficiales");
		descError.put(ERROR_BUSCAR_SECTORES, "Error al buscar Sectores");
		descError.put(ERROR_BUSCAR_SUCURSALES, "Error al buscar Sucursales");
		descError.put(ERROR_FALTA_PAGARE, "Verifique, falta seleccionar pagare");
		descError.put(ERROR_OBTENER_FECHA_DESCUENTO, "Error al obtener fecha de descuent");		
		descError.put(ERROR_CONTRATO_GUARDAR_NUMERO_CONTROL_EXISTENTE, "El numero de control ya existe");
		descError.put(ERROR_DOCUMENTO_EXISTENTE_CLIENTE, "El folio del documento ya existe para este cliente");
		descError.put(ERROR_DOCUMENTO_PREVIO, "El Documento ya ha sido agregado previamente");
		descError.put(ERROR_FECHA_AFORO_INVALIDA, "La fecha de aforo no debe ser menor a la fecha de expedicion del documento");
		descError.put(ERROR_XML_PREVIO, "El Documento ya ha sido agregado previamente");
		descError.put(ERROR_FORMATO_NOVALIDO, "El formato no es valido");
	}
}