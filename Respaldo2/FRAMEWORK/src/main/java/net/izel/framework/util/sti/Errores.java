package net.izel.framework.util.sti;

import java.util.HashMap;

public class Errores 
{
	/*TRANSACCION_EXITOSA*/
	public static final long TRANSACCION_EXITOSA = 0;
	
	
	/*USUARIO SIN EMPRESA ASIGNADA*/
	public static final long USUARIO_SIN_EMPRESA_ASIGNADA = 1;

	/*FECHA INVALIDA, FECHA INICIAL MAYOR A LA FECHA FINAL*/
	public static final long ERR_FECHA_INVALIDA = 3;

	/*CAMPOS %s REQUERIDOS*/
	public static final long CAMPOS_REQUERIDOS = 4;

	/*ERROR AL OBTENER EL ID DEL MODULO EN LA CUENTA CLABE*/
	public static final long ERR_VALIDAR_MODULO_EN_CLABE = 5;

	/*ERROR AL VALIDAR LA CLABE DE LA CUENTA*/
	public static final long ERR_VALIDAR_CLABE = 6;

	/*ERROR CUENTA CLABE INVALIDA*/
	public static final long ERR_CLABE_INVALIDA = 7;

	/*ERROR AL VALIDAR DIGITO VERIFICADOR DE LA CUENTA CLABE*/
	public static final long ERR_DIGITO_VERIFICADOR = 8;

	/*ERROR AL OBTENER PERMISO DE PRODUCTO */
	public static final long ERR_PRODUCTO_INVALIDO = 9;

	/*ERROR HORA INICIAL MAYOR A LA HORA FINAL*/
	public static final long ERR_HORA_INVALIDA = 10;

	/*ERROR AL CONSULTAR HORARIO DE SUCURSAL*/
	public static final long ERR_CONSULTA_HORARIOS_SUCURSAL = 11;

	/*ERROR AL VALIDAR CAMPOS REQUERIDOS*/
	public static final long ERR_VALIDAR_CAMPOS_REQURIDOS = 12;

	/*ERROR AL CREAR HORARIO DE ENVIO SPEI PARA SUCURSAL*/
	public static final long ERR_NUEVO_HORARIO_SUCURSAL = 13;

	/*YA EXISTE UNA SUCURSAL */
	public static final long ERR_SUCURSALYA_EXISTE = 14;

	/*ERROR EN ALTA DE HORARIO DE SUCURSAL*/
	public static final long ERR_ALTA_HORARIO_SUCURSAL = 15;

	/*ERROR AL MODIFICAR HORARIO DE SUCURSAL*/
	public static final long ERR_MODIFICAR_HORARIO_SUCURSAL = 16;

	/*NO EXISTE HORARIO SUCURSAL*/
	public static final long ERR_HORARIO_SUCURSAL_NO_EXISTE = 17;

	/*HORARIO SUCURSAL YA ESTA DADO DE BAJA*/
	public static final long ERR_HORARIO_SUCURSAl_BAJA = 18;

	/*HORARIO DE SUCURSAL YA ESTA DADO DE BAJA*/
	public static final long ERR_BAJA_HORARIO_SUCURSAL = 19;

	/*ERROR AL CONSULTAR USUARIOS AUTORIZADOS*/
	public static final long ERR_CONSULTAR_USUARIOS_AUTO = 20;

	/*EL USUARIO AUTORIZADO EXISTE PARA ESTA EMPRESA*/
	public static final long ERR_EXISTE_USUARIO_AUTORIZADO = 21;

	/*ERROR AL VALIDAR AUTORIZACION PARA USUARIO*/
	public static final long ERR_VALIDAR_USUARIO_AUTORIZADO = 22;

	/*ERROR AL CREAR AUTORIZACION  DE USUARIO*/
	public static final long ERR_CREAR_USUARIO_AUTORIZADO = 23;

	/*ERROR AL MODIFICAR AUTORIZACION DE USUARIO*/
	public static final long ERR_MODIFICAR_USUARIO_AUTORIZADO = 24;

	/*EL USUARIO YA ESTA DADO DE BAJA*/
	public static final long ERR_USUARIO_BAJA = 25;

	/*ERROR AL DAR DE BAJA USUARIO AUTORIZADO*/
	public static final long ERR_BAJA_USUARIO_AUTO = 26;

	/*EL USUARIO NO EXISTE*/
	public static final long ERR_NO_EXISTE_USUARIO_AUTO = 27;

	/*ERROR AL GENERAR AUTORIZACION SPEI*/
	public static final long ERR_AUTORIZAR_SPEI = 28;

	/*ERROR SPEI DE SALIDA NO EXISTE*/
	public static final long ERR_SPEI_OUTGOING_NO_EXISTE = 29;

	/*EL SPEI YA ESTA DADO DE BAJA*/
	public static final long ERR_SPEI_BAJA = 30;

	/*ERROR EL USUARIO O EL MODULO NO EXISTE*/
	public static final long ERR_USUARIO_MODULO_NO_EXISTE = 31;

	/*EL MONTO A ENVIAR ES INVALIDO PARA ESTE USUARIO*/
	public static final long ERR_MONTO_INVALIDO = 32;

	/*FECHA DE AUTORIZACION FUERA DE RANGO*/
	public static final long ERR_FECHA_FUERA_RANGO = 33;

	/*ERROR AL PROCESAR LA AUTORIZACION PARA EL USUARIO */
	public static final long ERR_AUTORIZACION_USUARIO = 34;

	/*ERROR AL CONSULTA INSTITUCION BANCARIA*/
	public static final long ERR_CONSULTAR_INSTITUCION = 35;

	/*LA INSTITUCION BANCARIA YA EXISTE*/
	public static final long ERR_INSTITUCION_BANCARIA = 35;

	/*ERROR AL CREAR INSTITUCION BANCARIA*/
	public static final long ERR_NUEVA_INSTITUCION = 36;

	/*ERROR AL VALIDAR LA INSITUCION BANCARIA*/
	public static final long ERR_VALIDAR_INSTITUCION = 37;

	/*LA INSTITUCION BANCARIA NO EXISTE*/
	public static final long ERR_INSTITUCION_NO_EXISTE = 38;

	/*LA INSTITUCION YA ESTA DADA DE BAJA*/
	public static final long ERR_INSTITUCION_BAJA = 39;

	/*ERROR AL DAR DE BAJA LA INSTITUCION BANCARIA*/
	public static final long ERR_BAJA_INSTITUCION = 40;

	/*ERROR AL MODIFICAR INSTITUCION BANCARIA*/
	public static final long ERR_MODIFICAR_INSTITUCION = 41;

	/*INSTITUCION NO EXISTEN EN CATALOGO DE BANXICO*/
	public static final long ERR_CATALOGO_INST_BANXICO = 42;

	/*NO SE ENCONTRO LA CONFIGURACION DE CUENTA ORDENANTE*/
	public static final long ERR_CONFIURACION_CUENTA_ORD = 43;

	/*ERROR EL GENERAR  LA TRANSACCION DE SALIDA SPEI*/
	public static final long ERR_TRANSACCION_SPEI = 44;

	/*SPEI ENVIADO EN ESPERA DE AUTORIZACION*/
	public static final long MSG_SPEI_ENVIADO = 45;

	/*SPEI PROGRAMADO EN ESPERA DE AUTORIZACION*/
	public static final long MSG_SPEI_PROGRAMADO = 46;

	/*SPEI PROGRAMADO POR ENVIO FUERA DE HORARIO*/
	public static final long MSG_SPEI_PROGRAMADO_X_HORARIO = 47;

	/*ERROR AL OBTENER EL CONTEXTO DE EJB*/
	public static final long ERR_OBTENER_CONTEXTO = 48;

	/*ERROR AL DAR DE ALTA CUENTA CLABE*/
	public static final long ERR_ALTA_CUENTA_CLABE = 49;
	
	/*LA CLAVE DE IDENTIFICACION DE LA CUENTA NO EXISTE*/
	public static final long ERR_CLAVE_APLICACION_NO_EXISTE = 50;
	
	/*ERROR DE APLICACION AL BUSCAR CLABE*/
	public static final long ERR_BUSCAR_CLABE = 51;

	/*LA CUENTA CLABE NO EXISTE*/
	public static final long ERR_NO_EXISTE_CUENTA = 52;
	
	/*ERROR CRITICO AL LEVANTAR PROCESO DE APLICACION DE PAGOS*/
	public static final long ERR_APLICACION = 53;

	/*ERROR CRITICO AL ACTUALIZAR CR OUTGOING */
	public static final long ERR_ACTUALIZAR_CR_OUTGOING = 54;

	/*NO SE ENCONTRO REGISTRO DE TRANSACCION OUTGOING  PROCESADA*/
	public static final long ERR_BUSCAR_OUTGOING = 55;

	/*ERROR CRITICO AL BUSCAR TRANSACCION OUTGOING  PROCESADA*/
	public static final long ERR_CRITICO_BUSCAR_OUTGOING = 56;
	
	/*ERROR CRITICO AL INSERTAR TRANSACCION EN CR INCOMING*/
	public static final long ERR_CRITICO_INSERTAR_CR_INCOMING = 57;
	
	/*NO SE ENCONTRO TRANSACCION RECEPCION PAGOS*/
	public static final long ERR_BUSCAR_RECEPCION_PAGO = 58;

	/*ERROR CRITICO AL BUSCAR TRANSACCIONES DE RECEPCION PAGOS*/
	public static final long ERR_CRITICO_RECEPCION_PAGOS = 59;

	/*LA CUENTA CLABE  YA EXISTE*/
	public static final long ERR_CLABE_YA_EXISTE = 60;
	
	/*CUENTA CLABE DEL ORDENANTE INVALIDA*/
	public static final long ERR_CLABE_ORDENATE = 61;

	/*CUENTA CLABE DEL BENEFICIARIO INVALIDA*/
	public static final long ERR_CLABE_BENEFICIARIO = 62;

	/*ERROR DE APLICACION AL VALIDAR DATOS DE ENVIO %s */
	public static final long ERR_VALIDACION_CRITICO = 63;
	
	/*LA CUENTA CLABE DEL ORDENANTE NO EXISTE*/
	public static final long ERR_CLABE_ORD_NO_EXISTE = 64;
	
	/*NO SE ENCONTRO PERMISO DE USUARIO PARA EL TIPO CUENTA Y EMPRESA*/
	public static final long ERR_CONFIG_USUARIO = 65;
	
	/*EL MONTO INFERIOR ES MAYOR AL MONTO SUPERIOR*/
	public static final long ERR_COMPARA_MONTOS = 66;
	
	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(TRANSACCION_EXITOSA,"TRANSACCION EXITOSA");
		descError.put(USUARIO_SIN_EMPRESA_ASIGNADA,"USUARIO SIN EMPRESA ASIGNADA");
		descError.put(ERR_FECHA_INVALIDA,"FECHA INVALIDA, FECHA INICIAL MAYOR A LA FECHA FINAL");
		descError.put(CAMPOS_REQUERIDOS,"CAMPOS %s REQUERIDOS");
		descError.put(ERR_VALIDAR_MODULO_EN_CLABE,"ERROR AL OBTENER EL ID DEL MODULO EN LA CUENTA CLABE");
		descError.put(ERR_VALIDAR_CLABE,"ERROR AL VALIDAR LA CLABE DE LA CUENTA");
		descError.put(ERR_CLABE_INVALIDA,"ERROR CUENTA CLABE INVALIDA");
		descError.put(ERR_DIGITO_VERIFICADOR,"ERROR AL VALIDAR DIGITO VERIFICADOR DE LA CUENTA CLABE");
		descError.put(ERR_PRODUCTO_INVALIDO,"ERROR AL OBTENER PERMISO DE PRODUCTO");
		descError.put(ERR_HORA_INVALIDA,"ERROR HORA INICIAL MAYOR A LA HORA FINAL");
		descError.put(ERR_CONSULTA_HORARIOS_SUCURSAL,"ERROR AL CONSULTAR HORARIO DE SUCURSAL");
		descError.put(ERR_VALIDAR_CAMPOS_REQURIDOS,"ERROR AL VALIDAR CAMPOS REQUERIDOS");
		descError.put(ERR_NUEVO_HORARIO_SUCURSAL,"ERROR AL CREAR HORARIO DE ENVIO SPEI PARA SUCURSAL");
		descError.put(ERR_SUCURSALYA_EXISTE,"YA EXISTE UNA SUCURSAL");
		descError.put(ERR_ALTA_HORARIO_SUCURSAL,"ERROR EN ALTA DE HORARIO DE SUCURSAL");
		descError.put(ERR_MODIFICAR_HORARIO_SUCURSAL,"ERROR AL MODIFICAR HORARIO DE SUCURSAL");
		descError.put(ERR_HORARIO_SUCURSAL_NO_EXISTE,"NO EXISTE HORARIO SUCURSAL");
		descError.put(ERR_HORARIO_SUCURSAl_BAJA,"HORARIO SUCURSAL YA ESTA DADO DE BAJA");
		descError.put(ERR_BAJA_HORARIO_SUCURSAL,"HORARIO DE SUCURSAL YA ESTA DADO DE BAJA");
		descError.put(ERR_CONSULTAR_USUARIOS_AUTO,"ERROR AL CONSULTAR USUARIOS AUTORIZADOS");
		descError.put(ERR_EXISTE_USUARIO_AUTORIZADO,"EL USUARIO AUTORIZADO EXISTE PARA ESTA EMPRESA");
		descError.put(ERR_VALIDAR_USUARIO_AUTORIZADO,"ERROR AL VALIDAR AUTORIZACION PARA USUARIO");
		descError.put(ERR_CREAR_USUARIO_AUTORIZADO,"ERROR AL CREAR AUTORIZACION  DE USUARIO");
		descError.put(ERR_MODIFICAR_USUARIO_AUTORIZADO,"ERROR AL MODIFICAR AUTORIZACION DE USUARIO");
		descError.put(ERR_USUARIO_BAJA,"EL USUARIO YA ESTA DADO DE BAJA");
		descError.put(ERR_BAJA_USUARIO_AUTO,"ERROR AL DAR DE BAJA USUARIO AUTORIZADO");
		descError.put(ERR_NO_EXISTE_USUARIO_AUTO,"EL USUARIO NO EXISTE");
		descError.put(ERR_AUTORIZAR_SPEI,"ERROR AL GENERAR AUTORIZACION SPEI");
		descError.put(ERR_SPEI_OUTGOING_NO_EXISTE,"ERROR SPEI DE SALIDA NO EXISTE");
		descError.put(ERR_SPEI_BAJA,"EL SPEI YA ESTA DADO DE BAJA");
		descError.put(ERR_USUARIO_MODULO_NO_EXISTE,"ERROR EL USUARIO O EL MODULO NO EXISTE");
		descError.put(ERR_MONTO_INVALIDO,"EL MONTO A ENVIAR ES INVALIDO PARA ESTE USUARIO");
		descError.put(ERR_FECHA_FUERA_RANGO,"FECHA DE AUTORIZACION FUERA DE RANGO");
		descError.put(ERR_AUTORIZACION_USUARIO,"ERROR AL PROCESAR LA AUTORIZACION PARA EL USUARIO");
		descError.put(ERR_CONSULTAR_INSTITUCION,"ERROR AL CONSULTA INSTITUCION BANCARIA");
		descError.put(ERR_INSTITUCION_BANCARIA,"LA INSTITUCION BANCARIA YA EXISTE");
		descError.put(ERR_NUEVA_INSTITUCION,"ERROR AL CREAR INSTITUCION BANCARIA");
		descError.put(ERR_VALIDAR_INSTITUCION,"ERROR AL VALIDAR LA INSITUCION BANCARIA");
		descError.put(ERR_INSTITUCION_NO_EXISTE,"LA INSTITUCION BANCARIA NO EXISTE");
		descError.put(ERR_INSTITUCION_BAJA,"LA INSTITUCION YA ESTA DADA DE BAJA");
		descError.put(ERR_BAJA_INSTITUCION,"ERROR AL DAR DE BAJA LA INSTITUCION BANCARIA");
		descError.put(ERR_MODIFICAR_INSTITUCION,"ERROR AL MODIFICAR INSTITUCION BANCARIA");
		descError.put(ERR_CATALOGO_INST_BANXICO,"INSTITUCION NO EXISTEN EN CATALOGO DE BANXICO");
		descError.put(ERR_CONFIURACION_CUENTA_ORD,"NO SE ENCONTRO LA CONFIGURACION DE CUENTA ORDENANTE");
		descError.put(ERR_TRANSACCION_SPEI,"ERROR EL GENERAR  LA TRANSACCION DE SALIDA SPEI");
		descError.put(MSG_SPEI_ENVIADO,"SPEI ENVIADO EN ESPERA DE AUTORIZACION");
		descError.put(MSG_SPEI_PROGRAMADO,"SPEI PROGRAMADO EN ESPERA DE AUTORIZACION");
		descError.put(MSG_SPEI_PROGRAMADO_X_HORARIO,"SPEI PROGRAMADO POR ENVIO FUERA DE HORARIO");
		descError.put(ERR_OBTENER_CONTEXTO,"ERROR AL OBTENER EL CONTEXTO DE EJB");
		descError.put(ERR_ALTA_CUENTA_CLABE,"ERROR AL DAR DE ALTA CUENTA CLABE");
		descError.put(ERR_CLAVE_APLICACION_NO_EXISTE,"LA CLAVE DE IDENTIFICACION DE LA CUENTA NO EXISTE");
		descError.put(ERR_BUSCAR_CLABE,"ERROR DE APLICACION AL BUSCAR CLABE");
		descError.put(ERR_NO_EXISTE_CUENTA,"LA CUENTA CLABE NO EXISTE");
		descError.put(ERR_APLICACION,"ERROR CRITICO AL LEVANTAR PROCESO DE APLICACION DE PAGOS");
		descError.put(ERR_ACTUALIZAR_CR_OUTGOING,"ERROR CRITICO AL ACTUALIZAR CR OUTGOING");
		descError.put(ERR_BUSCAR_OUTGOING,"NO SE ENCONTRO REGISTRO DE TRANSACCION OUTGOING  PROCESADA");
		descError.put(ERR_CRITICO_BUSCAR_OUTGOING,"ERROR CRITICO AL BUSCAR TRANSACCION OUTGOING  PROCESADA");
		descError.put(ERR_CRITICO_INSERTAR_CR_INCOMING,"ERROR CRITICO AL INSERTAR TRANSACCION EN CR INCOMING");
		descError.put(ERR_BUSCAR_RECEPCION_PAGO,"NO SE ENCONTRO TRANSACCION RECEPCION PAGOS");
		descError.put(ERR_CRITICO_RECEPCION_PAGOS,"ERROR CRITICO AL BUSCAR TRANSACCIONES DE RECEPCION PAGOS");
		descError.put(ERR_CLABE_YA_EXISTE,"LA CUENTA CLABE  YA EXISTE");
		descError.put(ERR_CLABE_ORDENATE,"CUENTA CLABE DEL ORDENANTE INVALIDA");
		descError.put(ERR_CLABE_BENEFICIARIO,"CUENTA CLABE DEL BENEFICIARIO INVALIDA");
		descError.put(ERR_VALIDACION_CRITICO,"ERROR DE APLICACION AL VALIDAR DATOS DE ENVIO %s");
		descError.put(ERR_CLABE_ORD_NO_EXISTE,"LA CUENTA CLABE DEL ORDENANTE NO EXISTE");
		descError.put(ERR_CONFIG_USUARIO,"NO SE ENCONTRO PERMISO DE USUARIO PARA EL TIPO CUENTA Y EMPRESA");
		descError.put(ERR_COMPARA_MONTOS,"EL MONTO INFERIOR ES MAYOR AL MONTO SUPERIOR");
	}
	
}
/**  !Errores.java */





