package net.izel.framework.util.te;

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
	
	public static final long ERROR_VALIDAR_GUARDAR_CARTA_INSTRUCTIVA= 13; 
	public static final long ERROR_CREAR_CARTA_INSTRUCTIVA= 14; 
	
	public static final long ERROR_CODIGO_CARTA_INSTRUCTIVA= 15; 
	public static final long ERROR_OBTENER_CARTA_INSTRUCTIVA_COMPLETO= 16; 
	public static final long ERROR_ACTUALIZAR_ESTADO_CARTA_INSTRUCTIVA= 17; 
	public static final long ERROR_CREAR_TRANSACCION_DIARIA= 18; 
	public static final long ERROR_BUSCAR_SALDO_EFECTIVO_X_CNTRLACCPUNTOATENCION= 19; 
	public static final long ERROR_ACTUALIZAR_MONTOPE_SALFINAL_SALDOS_EFECTIVO_DIARIO= 20; 
	public static final long ERROR_CREAR_SALDO_EFECTIVO_DIARIO= 21; 
	public static final long ERROR_DIA_INICIADO= 22;  
	public static final long ERROR_NO_SE_INICIO_DIA= 23;  
	public static final long ERROR_BUSCAR_CNTRLACC_X_IDYFECHA= 24;  
	public static final long ERROR_ACTUALIZAR_ESTADO_CNTRLACCPUNTOATENCION= 25;  
	public static final long ERROR_CREAR_CNTRLACCPUNTOATENCION= 26;  
	public static final long ERROR_VALIDAR_INICIO_DIA_CALLS= 27;  
	public static final long ERROR_OBTENER_CNTRLACCTIPOATENCION= 28;  
	public static final long  ERROR_FIND_BY_IPMACMONITOR= 29; 
	
	public static final long  ERROR_VALIDAR_ACCESSO_CALLS= 30; 
	
	public static final long  ERROR_PUNTO_ATENCION_INCORRECTO= 31; 
	public static final long  ERROR_PUNTO_ATENCION_INACTIVO= 32; 
	public static final long  ERROR_USUARIO_NO_EXISTE= 33; 
	public static final long  ERROR_USUARIO_INACTIVO= 34; 
	
	public static final long  ERROR_NO_EXISTE_CARTA_INSTRUCTIVA= 35;
	
	public static final long  ERROR_VALIDAR_SOLICITUD= 36;
	public static final long  ERROR_EXISTEN_MAS_BOVEDAS= 37;
	public static final long  ERROR_CREAR_REMESA_DINERO_SOLI= 38;
	public static final long  ERROR_OBTENER_REMESA_DINERO_PAD= 39;
	public static final long  ERROR_OBTENER_REMESA_DINERO_PAO= 40;
	public static final long  ERROR_CREAR_REMESA_DINERO_PRO= 41;
	public static final long  ERROR_ACTUALIZAR_ESTADO_REMESA_DINERO= 42;
	public static final long  ERROR_NO_EXISTE_REMESA_DINERO__SOLICITUD= 43;
	public static final long  ERROR_EXISTE_REMESA_DINERO__SOLICITUD= 44;
	
	public static final long  ERROR_BUSCAR_OPERACION_X_CLAVE= 45;
	public static final long  ERROR_BUSCAR_CONCEPTO_TRANSACCION_X_CLAVE= 46;
	
	public static final long  ERROR_SALDO_EFECTIVO_NO_EXISTE= 47;
	public static final long  ERROR_SALDO_EFECTIVO_INSUFICIENTE= 48;
	
	public static final long  ERROR_LISTA_REMESAS_DINERO_PAO= 49;
	public static final long  ERROR_BUSCAR_OPERACION_X_GRUPO_CLAVE= 50;
	public static final long  ERROR_BUSCAR_OPERACION_X_GRUPO_CLAVE_CALLS= 51;
	public static final long  ERROR_ACCESO_USUARIO_PUNTOATENCION= 52;
	public static final long  ERROR_ACCESO_USUARIO_PUNTO_INACTIVO= 53;
	public static final long  ERROR_FIND_BY_USUARIO_PUNTOATENCION_CALLS= 54;
	public static final long  ERROR_FIND_BY_USUARIOID_CALLS= 55;
	public static final long  ERROR_DATOS_SESSION_CALLS= 56;
	
	public static final long  ERROR_FIND_BY_ID_COMPLETO_OPERACION_CALLS= 57;
	public static final long  ERROR_CREAR_CARTA_CALLS= 58;
	public static final long  ERROR_OBTENER_CARTA_CALLS= 59;
	public static final long  ERROR_ACTUALIZAR_ESTADO_CARTA_INSTRUCTIVA_CALLS= 60;
	public static final long  ERROR_APLICAR_CARTA_CALLS= 61;
	public static final long  ERROR_OBTIENE_OPERACION_CARTA_CALLS= 62;
	public static final long  ERROR_OBTIENE_OPERACION_CONSULTA_CALLS= 63;
	public static final long  ERROR_INICIO_DIA_CALLS= 64;
	public static final long  ERROR_CIERRE_DIA_CALLS= 65;
	public static final long  ERROR_LISTAR_MONEDAS_CALLS= 66;
	public static final long  ERROR_CREAR_SALDO_MONEDAS_CALLS= 67;
	public static final long  ERROR_VALIDA_SALDO_CALLS= 68;
	public static final long  ERROR_OBTIENE_SOLICITUD_REMESAS_CALLS= 69;
	public static final long  ERROR_TRANSACCION_TELLER_CALLS= 70;
	public static final long  ERROR_CREA_SOLICITUD_CALLS= 71;
	public static final long  ERROR_CNTRLACCESO_POR_PUNTOATENCIONID= 72;
	public static final long  ERROR_OBTIENE_PUNTOATENCION_DATOS_OPERACION_CALLS= 73;
	public static final long  NO_EXISTE_CNTRLACCESO_POR_PUNTOATENCIONID= 74;
	
	public static final long  NO_EXISTE_AGENCIA_USUARIO_PUNTOATENCION= 75;
	public static final long  NO_EXISTE_USUARIOS= 76;
	
	public static final long  ERROR_DIARIO_ELECTRONICO_CALLS= 77;
	public static final long  ERROR_RESUMEN_DIARIO_ELECTRONICO_CALLS= 78;
	public static final long  ERROR_LISTAR_RESUMEN_DIARIO_ELEC_CALLS= 79;
	public static final long  ERROR_LISTAR_DIARIO_ELECTRONICO_CALLS= 80;
	public static final long  ERROR_FINDBYUSUARIOID_CALLS= 81;
	public static final long  ERROR_LISTAR_CNTRLACC_BY_USUARIO_CALLS= 82;
	public static final long  ERROR_OBTENER_PRESTAMO_ID= 83;
	public static final long  ERROR_NO_EXISTE_BOVEDA = 84;
	public static final long  ERROR_VALIDAR_MONEDA = 85;
	public static final long  ERROR_VALIDAR_CANAL_VENTA = 86;
	public static final long  ERROR_CREAR_DINERO_FALSO_CAB=87;
	public static final long  ERROR_CREAR_DINERO_FALSO_DET=88;
	public static final long  ERROR_CREA_DINERO_FALSO=89;
	public static final long  ERROR_OBTENER_GIRO=90;
	public static final long  ERROR_ACTUALIZAR_PAGO_GIRO= 91;
	public static final long  ERROR_CONSULTAR_MEDIO_PAGO= 92;
	
	public static final long  ERROR_TRANSACCION_DIARIA_NO_EXISTE= 93;
	public static final long  ERROR_OBTENER_TRANSACCION_DIARIA= 94;
	public static final long  ERROR_ACTUALIZAR_ESTADO_TRANSACCION_DIARIA= 95;
	public static final long  ERROR_OBTENER_CATALOGO_MEDIO_PAGO= 96;
	public static final long  ERROR_ANULAR_TRANSACCION_CALLS= 97;
	public static final long  ERROR_NO_EXISTE_REMESA_DINERO= 98;
	public static final long  ERROR_ANULAR_REMESA_CALLS= 99;
	public static final long  ERROR_OBTENER_REMESA_POR_TRANSACCION= 100;
	public static final long  ERROR_ACTUALIZAR_ESTADO_REMESA= 101;
	public static final long  ERROR_NO_EXISTE_DINERO_FALSO= 102;
	public static final long  ERROR_OBTENER_DINERO_FALSO_POR_TRANSACCION= 103;
	public static final long  ERROR_ACTUALIZAR_ESTADO_DINERO_FALSO= 104;
	public static final long  ERROR_ANULAR_DINERO_FALSO_CALLS= 105;
	public static final long  ERROR_TRANSACCIONID= 106;
	public static final long  ERROR_OBTENER_CARTA_INSTRUCTIVA_X_TRANSACCIONID= 107;
	public static final long  ERROR_ANULAR_CARTA_CALLS= 108;
	public static final long  ERROR_CARTA_INSTRUCTIVA_X_TRANSACCIONID_NO_EXISTE= 109;
	

	public static final long  ERROR_OBTENER_DINERO_FALSO_CAB= 110;
	public static final long  ERROR_ACTUAL_TRANSACCIONID_DINERO_FALSO_CAB= 111;
	public static final long  ERROR_ACTUAL_TRANSACCIONID_DINERO_FALSO_CALLS= 112;
	public static final long  ERROR_OBTENER_REMESA_DINERO= 113;
	public static final long  ERROR_ACTUAL_TRANSACCIONID_REMESA_DINERO= 114;
	public static final long  ERROR_ACTUAL_TRANSACCIONID_REMESA_DINERO_CALLS= 115;
	public static final long  ERROR_ACTUAL_TRANSACCIONID_CARTA_INST= 116;
	public static final long  ERROR_ACTUAL_TRANSACCIONID_CARTA_INST_CALLS= 117;
	
	public static final long  ERROR_OPERACIO_MONTO_TRX_CALLS= 118;
	
	
	public static final long  ACCESO_DENEGADO_PUNTO_OPERACION= 119;
	public static final long  ACCESO_DENEGADO_USUARIO_OPERACION= 120;
	public static final long  ACCESO_DENEGADO_USUARIO_AUTORIZAR= 121;
	public static final long  INGRESE_USUARIO_CLAVE= 122;
	public static final long  MONTO_NO_PERMITIDO_USUARIO_AUTORIZAR= 123;
	public static final long  ACCESO_DENEGADO_USUARIO_MONTO= 124;
	public static final long  ERROR_OBTENER_OPERACION_AUTORIZAR= 125;
	public static final long  ERROR_VALIDAR_ACCESSO_OPERACION_CALLS= 126;
	public static final long  ERROR_TRANSACCION_MEDIO_PAGO= 127;
	
	/*ESPECIFIQUE EMPRESA PARA CANCELAR CARTA INSTRUCTIVA*/
	public static final long ACI_ESPECIFIQUE_EMPRESA = 128;
	/*ERROR INESPERADO AL CANCELAR CARTA INSTRUCTIVA*/
	public static final long EI_CANCELA_CARTA_INSTRUCTIVA = 129;
	/*LA CAJA TIENE SALDO DE EFECTIVO %s*/
	public static final long ERR_SALDO_CIERRE = 130;
	/*ERROR AL DAR DE BAJA PUNTO ATENCION*/
	public static final long ER_BAJA_PTO_ATENCION = 131;
	/*ERROR EL PUNTO DE ATENCION NO ENCONTRADO*/
	public static final long ERR_PTO_ATENCION_NO_EXISTE = 132;
	/*EL PUNTO DE ATENCION YA EXISTE*/
	public static final long ERR_PTO_ATENCION_EXISTE = 133;
	/*NO SE ENCONTRARON PUNTOS DE ATENCION PARA EL USUARIO*/
	public static final long ERR_PUNTO_ATE_POR_USUARIO = 134;	
	/*PUNTO DE ATENCION INVALIDO PARA DOTACION A BOVEDA */
	public static final long ERR_PUNTO_ATE_DOT_INVALIDO = 135;
	/*ERROR AL ACTUALIZAR CONCILIACION TRANSACCION DEPOSITO*/
	public static final long ERR_ACTUALIZAR_CONCILIACION = 136;
	/*NO SE ENCONTRO REFERENCIA DEL DEPOSITO*/
	public static final long ERR_BUSCAR_TRANSACCION_DEP_XREF = 137;
	/*LA TRANSACCION YA FUE CONCILIADA*/
	public static final long ERR_TRANSACCION_YA_CONC = 138;
	/*PUNTO DE ATENCION CON CIERRE PENDIENTE*/	
	public static final long ERR_CIERRE_PENDIENTE = 139;
	/*TRANSACCION DE DEPOSITO YA CANCELADA*/
	public static final long ERR_TRANSACCION_DEP_CANC = 140;

	
	public static final long  ERROR_OBTENER_CARTA_INSTRUCTIVA= 900;
	public static final long  ERROR_VALIDAR_ACCESSO= 901;
	public static final long  ERROR_VALIDAR_INICIO_DIA = 902;
	
	public static final long  ERROR_CREAR_DENOMINACIONES = 903;
	public static final long  ERROR_CREAR_DENOMINACIONES_CALLS = 904;
	
	public static final long  ERROR_NO_EXISTEN_TIPOS_DOCUMENTOS_BANCARIOS = 905;
	public static final long  ERROR_TIPOS_DOCUMENTOS_BANCARIOS = 906;
	public static final long  ERROR_DOCUMENTOS_BANCARIOS = 907;
	public static final long  ERROR_DOCUMENTOS_BANCARIOS_RECIBIDOS_CALLS = 908;
	
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

		descError.put(ERROR_VALIDAR_GUARDAR_CARTA_INSTRUCTIVA,"Error al guardar los datos CARTA INSTRUCTIVA");
		descError.put(ERROR_CREAR_CARTA_INSTRUCTIVA,"Error al validar los datos CARTA INSTRUCTIVA");
		
		descError.put(ERROR_CODIGO_CARTA_INSTRUCTIVA,"El codigo de Carta Instructiva es incorrecto" );
		descError.put(ERROR_OBTENER_CARTA_INSTRUCTIVA_COMPLETO,"Error al obtener la Carata Instructiva" );
		
		descError.put(ERROR_ACTUALIZAR_ESTADO_CARTA_INSTRUCTIVA,"Error al actualizar el estado de la Carata Instructiva" );
		
		descError.put(ERROR_CREAR_TRANSACCION_DIARIA,"Error al crear la Transaccion diaria" );
		descError.put(ERROR_BUSCAR_SALDO_EFECTIVO_X_CNTRLACCPUNTOATENCION,"Error al buscar el Saldo Efectivo por controlAccesoPuntoAntencion" );
		descError.put(ERROR_ACTUALIZAR_MONTOPE_SALFINAL_SALDOS_EFECTIVO_DIARIO,"Error Actualizar Monto Operacion y Saldo Final en TrxSaldoEfectivoDiario" );
	
		descError.put(ERROR_CREAR_SALDO_EFECTIVO_DIARIO,"Error al crear el saldo efectivo diario" );
		descError.put(ERROR_DIA_INICIADO,"Error Punto de atencion esta abierto" );
		
		descError.put(ERROR_NO_SE_INICIO_DIA,"No ha realizado inicio de dia" );
		descError.put(ERROR_BUSCAR_CNTRLACC_X_IDYFECHA,"Erro al buscar el Control de Acceso Punto Atencion por Id y Fecha");
		descError.put(ERROR_ACTUALIZAR_ESTADO_CNTRLACCPUNTOATENCION,"Error al actualizar el estado de Control Acceso Punto Antecion" );
		descError.put(ERROR_CREAR_CNTRLACCPUNTOATENCION,"Error al crear Control de Acceso Punto Atencion" );
		descError.put(ERROR_VALIDAR_INICIO_DIA_CALLS,"Error al llamar a la funcion validar Inicio de Dia" );
		descError.put(ERROR_OBTENER_CNTRLACCTIPOATENCION,"Error no se puedo obtener el pojo de CONTROL ACCESO TIPO ATENCION" );
		descError.put(ERROR_FIND_BY_IPMACMONITOR,"Error al obtener el Punto de Atencion por Ip, Mac y Nombre Terminal" );
		
		descError.put(ERROR_VALIDAR_ACCESSO_CALLS,"Error al llamar al metodo VALIDAR ACESSO");
		descError.put(ERROR_PUNTO_ATENCION_INCORRECTO,"Error punto de atencion incorrecto");
		
		descError.put(ERROR_PUNTO_ATENCION_INACTIVO,"Error Punto de atencion no dado de Baja");
		descError.put(ERROR_USUARIO_NO_EXISTE,"Error usuario no existe"); 
		descError.put(ERROR_USUARIO_INACTIVO,"Error usuario dado de baja"); 
		
		descError.put(ERROR_NO_EXISTE_CARTA_INSTRUCTIVA,"No se encontro la Carta Instructiva"); 
		
		descError.put(ERROR_VALIDAR_SOLICITUD,"Error  al validar la Solicitud de Remesa de dinero"); 
		descError.put(ERROR_EXISTEN_MAS_BOVEDAS,"Error hay mas de una boveda" ); 
		descError.put(ERROR_CREAR_REMESA_DINERO_SOLI,"Error al crear la Remesa solicitada"); 
		descError.put(ERROR_OBTENER_REMESA_DINERO_PAD,"Error al obtener la Remesa de dinero por punto de atencion destino"); 
		descError.put(ERROR_OBTENER_REMESA_DINERO_PAO,"Error al obtener la Remesa de dinero por punto de atencion origen"); 
		descError.put(ERROR_CREAR_REMESA_DINERO_PRO,"Error al crear la Remesa Procesada"); 
		descError.put(ERROR_ACTUALIZAR_ESTADO_REMESA_DINERO,"Error al Actulizar la remesa solicitada"); 
		descError.put(ERROR_NO_EXISTE_REMESA_DINERO__SOLICITUD,"Error la Remesa de dinero solicitada no Existe");
		descError.put(ERROR_EXISTE_REMESA_DINERO__SOLICITUD,"Error ya existe una solicitud de dinero pendiente de aceptar"); 
		descError.put(ERROR_BUSCAR_OPERACION_X_CLAVE,"Error al buscar por clave en Operacion para la caja");
		descError.put(ERROR_BUSCAR_CONCEPTO_TRANSACCION_X_CLAVE,"Error al buscar por clave en Concepto Transaccion de caja"); 
		
		descError.put(ERROR_SALDO_EFECTIVO_NO_EXISTE,"Error no existe saldo efectivo"); 
		descError.put(ERROR_SALDO_EFECTIVO_INSUFICIENTE,"Error Saldo final insufuciente");
		
		
		descError.put(ERROR_LISTA_REMESAS_DINERO_PAO,"Error al listar las solicitudes de remesa");
		descError.put(ERROR_BUSCAR_OPERACION_X_GRUPO_CLAVE,"Error al listar Operaciones por grpOperacionClave");
		descError.put(ERROR_BUSCAR_OPERACION_X_GRUPO_CLAVE_CALLS,"Error llamar a la funcion obtieneOperacionesPorGrupoClave");
		descError.put(ERROR_ACCESO_USUARIO_PUNTOATENCION,"Error el usuario no esta asignado al punto de atencion");
		
		descError.put(ERROR_ACCESO_USUARIO_PUNTO_INACTIVO,"Error el acceso esta dado de Baja");
		descError.put(ERROR_FIND_BY_USUARIO_PUNTOATENCION_CALLS,"Error al llamar al metodo FindByUsuarioIdYPuntoAtencionId");
		descError.put(ERROR_FIND_BY_USUARIOID_CALLS,"Error llamar a la funcion findById para el UsuariosDePuntoAtencion");
		descError.put(ERROR_DATOS_SESSION_CALLS,"Error llamar a la funcion datosSession");
		
		descError.put(ERROR_FIND_BY_ID_COMPLETO_OPERACION_CALLS,"Error llamar a la funcion findByIdCompleto en OperacionDAO");
		descError.put(ERROR_CREAR_CARTA_CALLS,"Error llamar a la funcion crear en CartaInstructiva");
		
		descError.put(ERROR_OBTENER_CARTA_CALLS,"Error llamar a la funcion obtener  en CartaInstructiva");
		descError.put(ERROR_ACTUALIZAR_ESTADO_CARTA_INSTRUCTIVA_CALLS,"Error llamar a la funcion  actualizarEstado en CartaInstructiva");
		descError.put(ERROR_APLICAR_CARTA_CALLS,"Error llamar a la funcion aplicar  en CartaInstructiva");
		descError.put(ERROR_OBTIENE_OPERACION_CARTA_CALLS,"Error llamar a la funcion obtieneOperacion  en CartaInstructiva");
		descError.put(ERROR_OBTIENE_OPERACION_CONSULTA_CALLS,"Error llamar a la funcion obtieneOperacion  en Consultas de caja");
		descError.put(ERROR_INICIO_DIA_CALLS,"Error llamar a la funcion inicioDia  en ControlAccesoPuntoAtencion");
		descError.put(ERROR_CIERRE_DIA_CALLS,"Error llamar a la funcion cierreDia  en ControlAccesoPuntoAtencion");
		descError.put(ERROR_LISTAR_MONEDAS_CALLS,"Error llamar a la funcion listaMonedasXEmpresa  en MonedaDAO");
		descError.put(ERROR_CREAR_SALDO_MONEDAS_CALLS,"Error llamar a la funcion creaSaldosMonedas  en ControlAccesoPuntoAtencion");
		descError.put(ERROR_VALIDA_SALDO_CALLS,"Error llamar a la funcion validaSaldoDia  en SaldosDiarios");
		descError.put(ERROR_OBTIENE_SOLICITUD_REMESAS_CALLS,"Error llamar a la funcion obtenerSolicitudRemesas  en RemesaDinero");
		descError.put(ERROR_TRANSACCION_TELLER_CALLS,"Error llamar a la funcion transaccion de caja  en Transaccion");
		descError.put(ERROR_CREA_SOLICITUD_CALLS,"Error llamar a la funcion crearSolicitud  en RemesaDinero");
		descError.put(ERROR_CNTRLACCESO_POR_PUNTOATENCIONID,"No se encontro el control  de acceso para este punto de atencion");
		descError.put(ERROR_OBTIENE_PUNTOATENCION_DATOS_OPERACION_CALLS,"Error llamar a la funcion obtienePuntoAtencionYDatosOperacion  en Consultas caja");
		descError.put(NO_EXISTE_CNTRLACCESO_POR_PUNTOATENCIONID,"No existe el control de acceso para este punto de atencion");
		
		descError.put(NO_EXISTE_AGENCIA_USUARIO_PUNTOATENCION,"No existe agencia para este punto de atencion");
		descError.put(NO_EXISTE_USUARIOS, "No existe el usuario");
		
		descError.put(ERROR_DIARIO_ELECTRONICO_CALLS,"Error llamar a la funcion diarioElectronico  en TrxTransaccionesDiariaDAO");
		descError.put(ERROR_RESUMEN_DIARIO_ELECTRONICO_CALLS,"Error llamar a la funcion resumenDiarioElectronico  en TrxTransaccionesDiariaDAO");
		descError.put(ERROR_LISTAR_RESUMEN_DIARIO_ELEC_CALLS,"Error llamar a la funcion listaDiarioElectronico  en Consultas caja");
		descError.put(ERROR_LISTAR_DIARIO_ELECTRONICO_CALLS,"Error llamar a la funcion listaResumenDiarioElectronico  en Consultas caja");
		descError.put(ERROR_FINDBYUSUARIOID_CALLS, "Error al llamar a la funcion findByUsuarioId en TrxControlAccesoPuntoAtencionImp");
		descError.put(ERROR_LISTAR_CNTRLACC_BY_USUARIO_CALLS, "Error al llamar a la funcion listCntrlAccByUsuario en Consultas caja");
		descError.put(ERROR_OBTENER_PRESTAMO_ID, "Error al buscar cuenta prestamo");
		descError.put(ERROR_NO_EXISTE_BOVEDA, "Error no se encontro el punto de Boveda");
		descError.put(ERROR_VALIDAR_MONEDA,"No se ha definido moneda");
		descError.put(ERROR_VALIDAR_CANAL_VENTA, "Error no se encontro canal venta");
		
		descError.put(ERROR_CREAR_DINERO_FALSO_CAB," Error no se pudo guardar el registro de Dinero Falso Cabecera");
		descError.put(ERROR_CREAR_DINERO_FALSO_DET,"Error no se pudo guardar el registro de Dinero Falso Detalle");
		descError.put(ERROR_CREA_DINERO_FALSO,"Error en la funcion Crear en la clase DineroFalso");
		descError.put(ERROR_OBTENER_GIRO, "No se encontro folio de giro");
		
		descError.put(ERROR_CREAR_DENOMINACIONES,"Error al crear la Denominacion de Dinero");
		descError.put(ERROR_CREAR_DENOMINACIONES_CALLS,"Error al llamar la funcion crearDenominaciones");
		
		descError.put(ERROR_NO_EXISTEN_TIPOS_DOCUMENTOS_BANCARIOS,"No se encontraron Documentos Bancarios Recibidos");
		descError.put(ERROR_TIPOS_DOCUMENTOS_BANCARIOS,"Error al buscar Tipos Documentos Bancarios");
		descError.put(ERROR_DOCUMENTOS_BANCARIOS,"Error al buscar Documentos Bancarios");
		descError.put(ERROR_DOCUMENTOS_BANCARIOS_RECIBIDOS_CALLS,"Error al llamar a la funcion documentosBancariosRecibidos");
		descError.put(ERROR_ACTUALIZAR_PAGO_GIRO,"Error la efectuar pago de giro");
		descError.put(ERROR_CONSULTAR_MEDIO_PAGO,"Error al buscar medios pago caja");
		
		descError.put(ERROR_TRANSACCION_DIARIA_NO_EXISTE,"Transaccion diaria no existe");
		descError.put(ERROR_OBTENER_TRANSACCION_DIARIA,"Error al obtener la Transaccion Diaria");
		descError.put(ERROR_ACTUALIZAR_ESTADO_TRANSACCION_DIARIA,"Error al actualizar la Transaccion Diaria");
		descError.put(ERROR_OBTENER_CATALOGO_MEDIO_PAGO,"Error al obtener el catalogo de medio de pago");
		descError.put(ERROR_ANULAR_TRANSACCION_CALLS,"Error al llamar a la funcion Anular Transaccion");

		descError.put(ERROR_NO_EXISTE_REMESA_DINERO,"Remesa de dinero  no existe");
		descError.put(ERROR_ANULAR_REMESA_CALLS,"Error al llamar a la funcion anularRemesa");
		descError.put(ERROR_OBTENER_REMESA_POR_TRANSACCION,"Error al obtener la remesa por transaccion");
		descError.put(ERROR_ACTUALIZAR_ESTADO_REMESA,"Error al actualizar es estado de la remesa");
		descError.put(ERROR_NO_EXISTE_DINERO_FALSO,"Registro Dineroi Falso no existe");
		descError.put(ERROR_OBTENER_DINERO_FALSO_POR_TRANSACCION,"Error al obtener dinero falso");
		descError.put(ERROR_ACTUALIZAR_ESTADO_DINERO_FALSO,"Error al actualizar dinero falso");
		descError.put(ERROR_ANULAR_DINERO_FALSO_CALLS,"Error al llamar a la funcion anularDineroFalso");
		
		descError.put(ERROR_TRANSACCIONID," transaccionId es incorrecto");
		descError.put(ERROR_OBTENER_CARTA_INSTRUCTIVA_X_TRANSACCIONID,"Error al obtener carta instructiva por transaccionId");
		descError.put(ERROR_ANULAR_CARTA_CALLS,"Error a llamar a la funcion anularCarta");
		descError.put(ERROR_CARTA_INSTRUCTIVA_X_TRANSACCIONID_NO_EXISTE,"Error Carta instructiva no existe");
 
		descError.put(ERROR_OPERACIO_MONTO_TRX_CALLS,"Error a llamar a la funcion  obtineOperacionMontoTrx");
		
				
		descError.put(ACCESO_DENEGADO_PUNTO_OPERACION,"El punto de atencion no tiene acceso a esta operacion");
		descError.put(ACCESO_DENEGADO_USUARIO_OPERACION,"El usuario no tiene acceso a esta operacion");
		descError.put(ACCESO_DENEGADO_USUARIO_AUTORIZAR,"El usuario no puede autorizar la operacion, ACCESO DENEGADO!!");
		descError.put(INGRESE_USUARIO_CLAVE,"Ingrese clave y usuario");
		descError.put(MONTO_NO_PERMITIDO_USUARIO_AUTORIZAR,"El usuario no puede autorizar el monto de la operacion");
		descError.put(ACCESO_DENEGADO_USUARIO_MONTO,"El usuario puede autorizar monto");
		descError.put(ERROR_OBTENER_OPERACION_AUTORIZAR,"Error al consultar  operacion autorizar");
		descError.put(ERROR_VALIDAR_ACCESSO_OPERACION_CALLS,"Error a llamar a la funcion validaAccesoOperacion");
		descError.put(ERROR_TRANSACCION_MEDIO_PAGO,"Error al consultar medios de pago por transaccion");		
		descError.put(ACI_ESPECIFIQUE_EMPRESA,"ESPECIFIQUE EMPRESA PARA CANCELAR CARTA INSTRUCTIVA");
		descError.put(EI_CANCELA_CARTA_INSTRUCTIVA,"ERROR INESPERADO AL CANCELAR CARTA INSTRUCTIVA");			
		descError.put(ERROR_GENERAR_ARRAY_LIST,"No se pudo convertir datos entre arreglo y lista");
		descError.put(ERR_SALDO_CIERRE,"LA CAJA TIENE SALDO DE EFECTIVO");		
		descError.put(ER_BAJA_PTO_ATENCION,"ERROR AL DAR DE BAJA PUNTO ATENCION");
		descError.put(ERR_PTO_ATENCION_NO_EXISTE,"ERROR EL PUNTO DE ATENCION NO ENCONTRADO");
		descError.put(ERR_PTO_ATENCION_EXISTE,"EL PUNTO DE ATENCION YA EXISTE");
		descError.put(ERR_PUNTO_ATE_POR_USUARIO,"NO SE ENCONTRARON PUNTOS DE ATENCION PARA EL USUARIO");
		descError.put(ERR_PUNTO_ATE_DOT_INVALIDO,"PUNTO DE ATENCION INVALIDO PARA DOTACION A BOVEDA");
		descError.put(ERR_ACTUALIZAR_CONCILIACION,"ERROR AL ACTUALIZAR CONCILIACION TRANSACCION DEPOSITO");
		descError.put(ERR_BUSCAR_TRANSACCION_DEP_XREF,"NO SE ENCONTRO REFERENCIA DEL DEPOSITO");
		descError.put(ERR_TRANSACCION_YA_CONC,"LA TRANSACCION YA FUE CONCILIADA");		
		descError.put(ERR_CIERRE_PENDIENTE,"PUNTO DE ATENCION CON CIERRE PENDIENTE");
		descError.put(ERR_TRANSACCION_DEP_CANC,"TRANSACCION DE DEPOSITO YA CANCELADA");		
		
		
		descError.put(ERROR_OBTENER_CARTA_INSTRUCTIVA,"");
		descError.put(ERROR_VALIDAR_ACCESSO,"");
		descError.put(ERROR_VALIDAR_INICIO_DIA,"");
		
	}
}