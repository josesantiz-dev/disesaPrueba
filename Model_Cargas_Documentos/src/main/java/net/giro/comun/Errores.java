package net.giro.comun;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class Errores { 
	public static final Properties properties = new Properties();
	/*Transaccion exitosa*/
	public static final long TRANSACCION_EXITOSA = 0;

	/*Error inesperado*/
	public static final long ERROR_INESPERADO = 1;

	/*Verifique, debe seleccionar un criterio del listado de Tipo de busqueda*/
	public static final long CRITERIO_BUSQUEDA = 2;

	/*Capture campos requeridos*/
	public static final long CAMPOS_REQUERIDOS = 3;

	/*Listado vacio, favor de verificar*/
	public static final long LISTADO_VACIO = 4;

	/*Verifique, el prospecto tiene un registro activo*/
	public static final long ERROR_PROSPECTO_INVALIDO = 5;

	/*Error al guardar prospecto*/
	public static final long ERROR_GUARDAR_PROSPECTO = 6;

	/*Error al buscar prospecto*/
	public static final long ERROR_BUSCAR_PROSPECTO = 7;

	/*Error al insertar seguimiento*/
	public static final long ERROR_INSERTAR_SEGUIMIENTO = 8;

	/*Error al buscar seguimiento*/
	public static final long ERROR_BUSCAR_SEGUIMIENTO = 9;

	/*Error al autorizar seguimiento*/
	public static final long ERROR_AUTORIZAR_SEGUIMIENTO = 10;

	/*Error al rechazar seguimiento*/
	public static final long ERROR_RECHAZAR_SEGUIMIENTO = 11;

	/*Error al buscar solicitud*/
	public static final long ERROR_BUSCAR_SOLICITUD = 12;

	/*Error al guardar solicitud*/
	public static final long ERROR_GUARDAR_SOLICITUD = 13;

	/*Error al eliminar proveedor*/
	public static final long ERROR_ELIMINAR_PROVEEDOR = 14;

	/*Error al listar valores*/
	public static final long ERROR_LISTAR_VALORES = 15;

	/*No existe el grupo de valor*/
	public static final long ERROR_GRUPO_VALOR_INEXISTENTE = 16;

	/*Error al listar sucursales*/
	public static final long ERROR_LISTAR_SUCURSALES = 17;

	/*Error al listar especialistas*/
	public static final long ERROR_LISTAR_ESPECIALISTAS = 18;

	/*Error al listar monedas*/
	public static final long ERROR_LISTAR_MONEDAS = 19;

	/*Error al buscar persona*/
	public static final long ERROR_BUSCAR_PERSONA = 20;

	/*Error al buscar negocio*/
	public static final long ERROR_BUSCAR_NEGOCIO = 21;

	/*Error al rechazar solicitud*/
	public static final long ERROR_RECHAZAR_SOLICITUD = 22;

	/*Error al listar facturas*/
	public static final long ERROR_LISTAR_FACTURAS = 23;

	/*Error al autorizar solicitud*/
	public static final long ERROR_AUTORIZAR_SOLICITUD = 24;

	/*Error al listar proveedores*/
	public static final long ERROR_LISTAR_PROVEEDORES = 25;

	/*Error al guardar proveedor*/
	public static final long ERROR_GUARDAR_PROVEEDOR = 26;

	/*Error al buscar proveedor*/
	public static final long ERROR_BUSCAR_PROVEEDOR = 27;

	/*Error al listar clientes*/
	public static final long ERROR_LISTAR_CLIENTES = 28;

	/*Error al eliminar cliente*/
	public static final long ERROR_ELIMINAR_CLIENTE = 29;

	/*Error al guardar cliente*/
	public static final long ERROR_GUARDAR_CLIENTE = 30;

	/*Error al buscar cliente*/
	public static final long ERROR_BUSCAR_CLIENTE = 31;

	/*Error al eliminar documento*/
	public static final long ERROR_ELIMINAR_DOCUMENTO = 32;

	/*Error al guardar documento*/
	public static final long ERROR_GUARDAR_DOCUMENTO = 33;

	/*Error al buscar documento*/
	public static final long ERROR_BUSCAR_DOCUMENTO = 34;

	/*Error al eliminar referencia*/
	public static final long ERROR_ELIMINAR_REFERENCIA = 35;

	/*Error al guardar referencia*/
	public static final long ERROR_GUARDAR_REFERENCIA = 36;

	/*Error al buscar referencia*/
	public static final long ERROR_BUSCAR_REFERENCIA = 37;

	/*Error al guardar avales*/
	public static final long ERROR_GUARDAR_AVALES = 38;

	/*Error al eliminar aval*/
	public static final long ERROR_ELIMINAR_AVAL = 39;

	/*Error al buscar accionistas*/
	public static final long ERROR_BUSCAR_ACCIONISTAS = 40;

	/*Error al buscar directivos*/
	public static final long ERROR_BUSCAR_DIRECTIVOS = 41;

	/*Error al listar ventas*/
	public static final long ERROR_LISTAR_VENTAS = 42;

	/*Error al eliminar venta*/
	public static final long ERROR_ELIMINAR_VENTA = 43;

	/*Error al guardar venta*/
	public static final long ERROR_GUARDAR_VENTA = 44;

	/*Error al buscar venta*/
	public static final long ERROR_BUSCAR_VENTA = 45;

	/*Error al eliminar pasivo*/
	public static final long ERROR_ELIMINAR_PASIVO = 46;

	/*Error al guardar pasivo*/
	public static final long ERROR_GUARDAR_PASIVO = 47;

	/*Error al buscar pasivo*/
	public static final long ERROR_BUSCAR_PASIVO = 48;

	/*Error al guardar analisis*/
	public static final long ERROR_GUARDAR_ANALISIS = 49;

	/*Error al eliminar periodo*/
	public static final long ERROR_ELIMINAR_PERIODO = 50;

	/*Error al listar bancos*/
	public static final long ERROR_LISTAR_BANCOS = 51;

	/*Error al guardar periodo*/
	public static final long ERROR_GUARDAR_PERIODO = 52;

	/*Error al listar los estatus de seguimiento*/
	public static final long ERROR_LISTAR_ESTATUS_SEGUIMIENTO = 53;

	/*Error al expirar seguimiento*/
	public static final long ERROR_EXPIRAR_SEGUIMIENTO = 54;

	/*Error al obtener perfil*/
	public static final long ERROR_OBTENER_PERFIL = 55;

	/*Verifique, usuario sin perfil requerido*/
	public static final long ERROR_FALTA_PERMISO = 56;

	/*Error al obtener reporte historico de seguimiento*/
	public static final long ERROR_OBTENER_HISTORICO = 57;

	/*Error al establecer domicilio principal.*/
	public static final long ERROR_ESTABLECER_DOMICILIO_PRINCIPAL = 58;

	/*Error al guardar domicilio*/
	public static final long ERROR_GUARDAR_DOMICILIO = 59;

	/*Error el domicilio no esta activo, activelo para continuar*/
	public static final long ERROR_DOMICILIO_INACTIVO = 60;

	/*Error al habilitar/deshabilitar domicilio.*/
	public static final long ERROR_CAMBIAR_ESTATUS_DOMICILIO = 61;

	/*Error al deshabilitar domicilio, seleccione otro domicilio como principal antes de continuar.*/
	public static final long ERROR_DESHABILITAR_DOMICILIO_PRINCIPAL = 62;

	/*Error al eliminar negocio.*/
	public static final long ERROR_ELIMINAR_NEGOCIO = 63;

	/*Error, no se permite agregar/editar datos para negocios inactivos.*/
	public static final long ERROR_NEGOCIO_INACTIVO = 64;

	/*Error al guardar negocio.*/
	public static final long ERROR_GUARDAR_NEGOCIO = 65;

	/*Error al guardar persona de negocio*/
	public static final long ERROR_GUARDAR_PERSONA_NEGOCIO = 66;

	/*Error al eliminar persona de negocio*/
	public static final long ERROR_ELIMINAR_PERSONA_NEGOCIO = 67;

	/*Error al guardar directorio*/
	public static final long ERROR_GUARDAR_DIRECTORIO = 68;

	/*Error al establecer el telefono principal para el negocio.*/
	public static final long ERROR_ESTABLECER_TELEFONO_PRINCIPAL = 69;

	/*Error al cargar tipos de canales.*/
	public static final long ERROR_CARGAR_TIPOS_CANALES = 70;

	/*Error al guardar canal de negocio.*/
	public static final long ERROR_GUARDAR_CANAL_NEGOCIO = 71;

	/*Error al eliminar canal de negocio*/
	public static final long ERROR_ELIMINAR_CANAL_NEGOCIO = 72;

	/*Error al buscar canales de negocio*/
	public static final long ERROR_BUSCAR_CANALES_NEGOCIO = 73;

	/*Error al cargar los estados de operacion de negocio*/
	public static final long ERROR_CARGAR_ESTADOS_NEGOCIO = 74;

	/*Error al guardar estado de operacion de negocio*/
	public static final long ERROR_GUARDAR_ESTADOS_NEGOCIO = 75;

	/*Error al eliminar estado de operacion de negocio*/
	public static final long ERROR_ELIMINAR_ESTADOS_NEGOCIO = 76;

	/*Error al cargar estados*/
	public static final long ERROR_CARGAR_ESTADOS = 77;

	/*Verifica el rfc.*/
	public static final long ERROR_RFC = 78;

	/*Error al cargar contactos de negocio*/
	public static final long ERROR_CARGAR_CONTACTOS_NEGOCIO = 79;

	/*Error al guardar contacto de negocio*/
	public static final long ERROR_GUARDAR_CONTACTOS_NEGOCIO = 80;

	/*Error al eliminar contacto de negocio*/
	public static final long ERROR_ELIMINAR_CONTACTO_NEGOCIO = 81;

	/*El conyuge debe ser distinto a la persona.*/
	public static final long ERROR_CONYUGE_IGUAL_PERSONA = 82;

	/*Error al registrar la persona.*/
	public static final long ERROR_GUARDAR_PERSONA = 83;

	/*Debe definir una colonia.*/
	public static final long ERROR_FALTA_DEFINIR_COLONIA = 84;

	/*Error al habilitar/deshabilitar contacto de negocio*/
	public static final long ERROR_HABILITAR_CONTACTO_NEGOCIO = 85;

	/*No se permite editar contactos inactivos*/
	public static final long ERROR_CONTACTO_INACTIVO = 86;

	/*Error al buscar contactos de negocio*/
	public static final long ERROR_BUSCAR_CONTACTOS_NEGOCIO = 87;

	/*Error al listar directorios telefonicos de persona.*/
	public static final long ERROR_LISTAR_DIRECTORIOS_PERSONA = 88;

	/*Error al listar Negocios de persona.*/
	public static final long ERROR_LISTAR_NEGOCIO_PERSONA = 89;

	/*Error al eliminar negocio de persona.*/
	public static final long ERROR_ELIMINAR_NEGOCIO_PERSONA = 90;

	/*Error al eliminar la identificacion de la persona.*/
	public static final long ERROR_ELIMINAR_IDENTIFICACION = 91;

	/*La Participacion del representante debe ser mayo a 0.*/
	public static final long ERROR_PARTICIPACION_REPRESENTANTE_MAYOR_A_CERO = 92;

	/*Error al eliminar nombre alterno.*/
	public static final long ERROR_ELIMINAR_NOMBRE_ALTERNO = 93;

	/*Error al listar los contactos de la persona.*/
	public static final long ERROR_LISTAR_CONTACTOS_PERSONA = 94;

	/*Error al guardar contacto de persona.*/
	public static final long ERROR_GUARDAR_CONTACTO_PERSONA = 95;

	/*Error al eliminar el contacto de la persona.*/
	public static final long ERROR_ELIMINAR_CONTACTO_PERSONA = 96;

	/*Error al cargar las estructuras de importacion de pagos*/
	public static final long ERROR_CARGAR_ESTRUCTURA_IMPORTACION = 97;

	/*Error al guardar estructura de importacion de pagos*/
	public static final long ERROR_GUARDAR_ESTRUCTURA_IMPORTACION = 98;

	/*Error al eliminar estructura de importacion de pago*/
	public static final long ERROR_ELIMINAR_ESTRUCTURA_IMPORTACION_PAGO = 99;

	/*Error al cargar las cuentas bancarias*/
	public static final long ERROR_LISTAR_CUENTAS_BANCARIAS = 100;

	/*El separador debe ser un solo caracter. Verifique la longitud del separador.*/
	public static final long ERROR_LONGITUD_SEPARADOR = 101;

	/*Verifique, la cuenta seleccionada ya tiene definida su estructura de importacion de pagos*/
	public static final long ERROR_ESTRUCTURA_DEFINIDA = 102;

	/*Verifique la participacion, el total de participaciones no puede superar el 100%*/
	public static final long ERROR_PARTICIPACION_SUPERA_CIEN = 103;
	
	//ERRORES AGREGADOS SIN USAR EL SOFTWARE DE LA 11.200
	
	/*Verifique, si se indico posicion de fecha se requiere formato tambien*/
	public static final long ERROR_FORMATO_FECHA_REQUERIDO = 104;
	
	/*Verifique, si se indico posicion de identificador se requiere el valor de identificador*/
	public static final long ERROR_VALOR_IDENTIFICADOR_REQUERIDO = 105;
	
	/*Verifique, si no existe separador y se indico posicion de fecha se requiere longitud tambien*/
	public static final long ERROR_LONGITUD_FECHA_REQUERIDO = 106;
	
	/*Verifique, si no existe separador y se indico posicion de identificador se requiere longitud tambien*/
	public static final long ERROR_LONGITUD_SEPARADOR_REQUERIDO = 107;
	
	/*Verifique, si no existe separador y se indico posicion de referencia se requiere longitud tambien*/
	public static final long ERROR_LONGITUD_REFERENCIA_REQUERIDO = 108;
	
	/*Verifique, si no existe separador y se indico posicion de monto se requiere longitud tambien*/
	public static final long ERROR_LONGITUD_MONTO_REQUERIDO = 109;
	
	/*Verifique, se requiere personalidad si la persona firma documento*/
	public static final long ERROR_PERSONALIDAD_REQUERIDA = 110;
	
	/*Error al buscar giros*/
	public static final long ERROR_BUSCAR_GIROS = 111;
	
	/*Error al buscar giro*/
	public static final long ERROR_BUSCAR_GIRO = 112;
	
	/*Error al guardar identificacion*/
	public static final long ERROR_GUARDAR_IDENTIFICACION = 113;
	
	/*Verifique es necesario seleccionar un negocio*/
	public static final long ERROR_FALTA_NEGOCIO = 114;
	
	/*Verifique es necesario seleccionar un negocio*/
	public static final long ERROR_RFC_EXISTE = 115;
	
	/*Verifique es necesario seleccionar un negocio*/
	public static final long ERROR_EMAIL_INVALIDO = 116;

	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	public static String get(String llave, Object ... args) { 
		String formateada = get(llave);
		try { 
			formateada = String.format(formateada, args) ; 
		} catch(Exception e ) { 
			
		} 
		return formateada; 
	}
	
	public static String get(String llave) { 
		if (properties == null) 
			return llave;
		String valor = llave;
		try { 
			valor = properties.getProperty(llave);
		} catch(Exception e) {
			
		}
		return valor;
	}
	
	static {
	    final ClassLoader cl = Thread.currentThread().getContextClassLoader();
	    final InputStream inputStream = cl.getResourceAsStream("messages.properties");
	    
	    if (inputStream != null) {
	    	try {
	    		properties.load(inputStream);
	      	} catch (IOException e) {
				e.printStackTrace();
			} finally {
		    	try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }  
	    }
	    
		descError.put(TRANSACCION_EXITOSA,"Transaccion exitosa");
		descError.put(ERROR_INESPERADO,"Error inesperado");
		descError.put(CRITERIO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de busqueda");
		descError.put(CAMPOS_REQUERIDOS,"Capture campos requeridos");
		descError.put(LISTADO_VACIO,"Listado vacio, favor de verificar");
		descError.put(ERROR_PROSPECTO_INVALIDO,"Verifique, el prospecto tiene un registro activo");
		descError.put(ERROR_GUARDAR_PROSPECTO,"Error al guardar prospecto");
		descError.put(ERROR_BUSCAR_PROSPECTO,"Error al buscar prospecto");
		descError.put(ERROR_INSERTAR_SEGUIMIENTO,"Error al insertar seguimiento");
		descError.put(ERROR_BUSCAR_SEGUIMIENTO,"Error al buscar seguimiento");
		descError.put(ERROR_AUTORIZAR_SEGUIMIENTO,"Error al autorizar seguimiento");
		descError.put(ERROR_RECHAZAR_SEGUIMIENTO,"Error al rechazar seguimiento");
		descError.put(ERROR_BUSCAR_SOLICITUD,"Error al buscar solicitud");
		descError.put(ERROR_GUARDAR_SOLICITUD,"Error al guardar solicitud");
		descError.put(ERROR_ELIMINAR_PROVEEDOR,"Error al eliminar proveedor");
		descError.put(ERROR_LISTAR_VALORES,"Error al listar valores");
		descError.put(ERROR_GRUPO_VALOR_INEXISTENTE,"No existe el grupo de valor");
		descError.put(ERROR_LISTAR_SUCURSALES,"Error al listar sucursales");
		descError.put(ERROR_LISTAR_ESPECIALISTAS,"Error al listar especialistas");
		descError.put(ERROR_LISTAR_MONEDAS,"Error al listar monedas");
		descError.put(ERROR_BUSCAR_PERSONA,"Error al buscar persona");
		descError.put(ERROR_BUSCAR_NEGOCIO,"Error al buscar negocio");
		descError.put(ERROR_RECHAZAR_SOLICITUD,"Error al rechazar solicitud");
		descError.put(ERROR_LISTAR_FACTURAS,"Error al listar facturas");
		descError.put(ERROR_AUTORIZAR_SOLICITUD,"Error al autorizar solicitud");
		descError.put(ERROR_LISTAR_PROVEEDORES,"Error al listar proveedores");
		descError.put(ERROR_GUARDAR_PROVEEDOR,"Error al guardar proveedor");
		descError.put(ERROR_BUSCAR_PROVEEDOR,"Error al buscar proveedor");
		descError.put(ERROR_LISTAR_CLIENTES,"Error al listar clientes");
		descError.put(ERROR_ELIMINAR_CLIENTE,"Error al eliminar cliente");
		descError.put(ERROR_GUARDAR_CLIENTE,"Error al guardar cliente");
		descError.put(ERROR_BUSCAR_CLIENTE,"Error al buscar cliente");
		descError.put(ERROR_ELIMINAR_DOCUMENTO,"Error al eliminar documento");
		descError.put(ERROR_GUARDAR_DOCUMENTO,"Error al guardar documento");
		descError.put(ERROR_BUSCAR_DOCUMENTO,"Error al buscar documento");
		descError.put(ERROR_ELIMINAR_REFERENCIA,"Error al eliminar referencia");
		descError.put(ERROR_GUARDAR_REFERENCIA,"Error al guardar referencia");
		descError.put(ERROR_BUSCAR_REFERENCIA,"Error al buscar referencia");
		descError.put(ERROR_GUARDAR_AVALES,"Error al guardar avales");
		descError.put(ERROR_ELIMINAR_AVAL,"Error al eliminar aval");
		descError.put(ERROR_BUSCAR_ACCIONISTAS,"Error al buscar accionistas");
		descError.put(ERROR_BUSCAR_DIRECTIVOS,"Error al buscar directivos");
		descError.put(ERROR_LISTAR_VENTAS,"Error al listar ventas");
		descError.put(ERROR_ELIMINAR_VENTA,"Error al eliminar venta");
		descError.put(ERROR_GUARDAR_VENTA,"Error al guardar venta");
		descError.put(ERROR_BUSCAR_VENTA,"Error al buscar venta");
		descError.put(ERROR_ELIMINAR_PASIVO,"Error al eliminar pasivo");
		descError.put(ERROR_GUARDAR_PASIVO,"Error al guardar pasivo");
		descError.put(ERROR_BUSCAR_PASIVO,"Error al buscar pasivo");
		descError.put(ERROR_GUARDAR_ANALISIS,"Error al guardar analisis");
		descError.put(ERROR_ELIMINAR_PERIODO,"Error al eliminar periodo");
		descError.put(ERROR_LISTAR_BANCOS,"Error al listar bancos");
		descError.put(ERROR_GUARDAR_PERIODO,"Error al guardar periodo");
		descError.put(ERROR_LISTAR_ESTATUS_SEGUIMIENTO,"Error al listar los estatus de seguimiento");
		descError.put(ERROR_EXPIRAR_SEGUIMIENTO,"Error al expirar seguimiento");
		descError.put(ERROR_OBTENER_PERFIL,"Error al obtener perfil");
		descError.put(ERROR_FALTA_PERMISO,"Verifique, usuario sin perfil requerido");
		descError.put(ERROR_OBTENER_HISTORICO,"Error al obtener reporte historico de seguimiento");
		descError.put(ERROR_ESTABLECER_DOMICILIO_PRINCIPAL,"Error al establecer domicilio principal.");
		descError.put(ERROR_GUARDAR_DOMICILIO,"Error al guardar domicilio");
		descError.put(ERROR_DOMICILIO_INACTIVO,"Error el domicilio no esta activo, activelo para continuar");
		descError.put(ERROR_CAMBIAR_ESTATUS_DOMICILIO,"Error al habilitar/deshabilitar domicilio.");
		descError.put(ERROR_DESHABILITAR_DOMICILIO_PRINCIPAL,"Error al deshabilitar domicilio, seleccione otro domicilio como principal antes de continuar.");
		descError.put(ERROR_ELIMINAR_NEGOCIO,"Error al eliminar negocio.");
		descError.put(ERROR_NEGOCIO_INACTIVO,"Error, no se permite agregar/editar datos para negocios inactivos.");
		descError.put(ERROR_GUARDAR_NEGOCIO,"Error al guardar negocio.");
		descError.put(ERROR_GUARDAR_PERSONA_NEGOCIO,"Error al guardar persona de negocio");
		descError.put(ERROR_ELIMINAR_PERSONA_NEGOCIO,"Error al eliminar persona de negocio");
		descError.put(ERROR_GUARDAR_DIRECTORIO,"Error al guardar directorio");
		descError.put(ERROR_ESTABLECER_TELEFONO_PRINCIPAL,"Error al establecer el telefono principal para el negocio.");
		descError.put(ERROR_CARGAR_TIPOS_CANALES,"Error al cargar tipos de canales.");
		descError.put(ERROR_GUARDAR_CANAL_NEGOCIO,"Error al guardar canal de negocio.");
		descError.put(ERROR_ELIMINAR_CANAL_NEGOCIO,"Error al eliminar canal de negocio");
		descError.put(ERROR_BUSCAR_CANALES_NEGOCIO,"Error al buscar canales de negocio");
		descError.put(ERROR_CARGAR_ESTADOS_NEGOCIO,"Error al cargar los estados de operacion de negocio");
		descError.put(ERROR_GUARDAR_ESTADOS_NEGOCIO,"Error al guardar estado de operacion de negocio");
		descError.put(ERROR_ELIMINAR_ESTADOS_NEGOCIO,"Error al eliminar estado de operacion de negocio");
		descError.put(ERROR_CARGAR_ESTADOS,"Error al cargar estados");
		descError.put(ERROR_RFC,"Verifica el rfc.");
		descError.put(ERROR_CARGAR_CONTACTOS_NEGOCIO,"Error al cargar contactos de negocio");
		descError.put(ERROR_GUARDAR_CONTACTOS_NEGOCIO,"Error al guardar contacto de negocio");
		descError.put(ERROR_ELIMINAR_CONTACTO_NEGOCIO,"Error al eliminar contacto de negocio");
		descError.put(ERROR_CONYUGE_IGUAL_PERSONA,"El conyuge debe ser distinto a la persona.");
		descError.put(ERROR_GUARDAR_PERSONA,"Error al registrar la persona.");
		descError.put(ERROR_FALTA_DEFINIR_COLONIA,"Debe definir una colonia.");
		descError.put(ERROR_HABILITAR_CONTACTO_NEGOCIO,"Error al habilitar/deshabilitar contacto de negocio");
		descError.put(ERROR_CONTACTO_INACTIVO,"No se permite editar contactos inactivos");
		descError.put(ERROR_BUSCAR_CONTACTOS_NEGOCIO,"Error al buscar contactos de negocio");
		descError.put(ERROR_LISTAR_DIRECTORIOS_PERSONA,"Error al listar directorios telefonicos de persona.");
		descError.put(ERROR_LISTAR_NEGOCIO_PERSONA,"Error al listar Negocios de persona.");
		descError.put(ERROR_ELIMINAR_NEGOCIO_PERSONA,"Error al eliminar negocio de persona.");
		descError.put(ERROR_ELIMINAR_IDENTIFICACION,"Error al eliminar la identificacion de la persona.");
		descError.put(ERROR_PARTICIPACION_REPRESENTANTE_MAYOR_A_CERO,"La Participacion del representante debe ser mayo a 0.");
		descError.put(ERROR_ELIMINAR_NOMBRE_ALTERNO,"Error al eliminar nombre alterno.");
		descError.put(ERROR_LISTAR_CONTACTOS_PERSONA,"Error al listar los contactos de la persona.");
		descError.put(ERROR_GUARDAR_CONTACTO_PERSONA,"Error al guardar contacto de persona.");
		descError.put(ERROR_ELIMINAR_CONTACTO_PERSONA,"Error al eliminar el contacto de la persona.");
		descError.put(ERROR_CARGAR_ESTRUCTURA_IMPORTACION,"Error al cargar las estructuras de importacion de pagos");
		descError.put(ERROR_GUARDAR_ESTRUCTURA_IMPORTACION,"Error al guardar estructura de importacion de pagos");
		descError.put(ERROR_ELIMINAR_ESTRUCTURA_IMPORTACION_PAGO,"Error al eliminar estructura de importacion de pago");
		descError.put(ERROR_LISTAR_CUENTAS_BANCARIAS,"Error al cargar las cuentas bancarias");
		descError.put(ERROR_LONGITUD_SEPARADOR,"El separador debe ser un solo caracter. Verifique la longitud del separador.");
		descError.put(ERROR_ESTRUCTURA_DEFINIDA,"Verifique, la cuenta seleccionada ya tiene definida su estructura de importacion de pagos");
		descError.put(ERROR_PARTICIPACION_SUPERA_CIEN,"Verifique la participacion, el total de participaciones no puede superar el 100%");
		descError.put(ERROR_FORMATO_FECHA_REQUERIDO, "Verifique, si se indico posicion de fecha se requiere formato tambien");
		descError.put(ERROR_VALOR_IDENTIFICADOR_REQUERIDO, "Verifique, si se indico posicion de identificador se requiere el valor de identificador");
		descError.put(ERROR_LONGITUD_FECHA_REQUERIDO, "Verifique, si no existe separador y se indico posicion de fecha se requiere longitud tambien");
		descError.put(ERROR_LONGITUD_SEPARADOR_REQUERIDO, "Verifique, si no existe separador y se indico posicion de identificador se requiere longitud tambien");
		descError.put(ERROR_LONGITUD_REFERENCIA_REQUERIDO, "Verifique, si no existe separador y se indico posicion de referencia se requiere longitud tambien");
		descError.put(ERROR_LONGITUD_MONTO_REQUERIDO, "Verifique, si no existe separador y se indico posicion de monto se requiere longitud tambien");
		descError.put(ERROR_PERSONALIDAD_REQUERIDA, "Verifique, se requiere personalidad si la persona firma documento");
		descError.put(ERROR_BUSCAR_GIROS, "Error al buscar giros");
		descError.put(ERROR_BUSCAR_GIRO, "Error al buscar giro");
		descError.put(ERROR_GUARDAR_IDENTIFICACION, "Error al guardar identificacion");
		descError.put(ERROR_FALTA_NEGOCIO, "Verifique es necesario seleccionar un negocio");
		descError.put(ERROR_RFC_EXISTE, "Verifique, el RFC ya existe");
		descError.put(ERROR_EMAIL_INVALIDO, "Verifique, el Email no es valido");
	}
	
	
}