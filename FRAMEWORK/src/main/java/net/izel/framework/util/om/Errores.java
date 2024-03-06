package net.izel.framework.util.om;

import java.util.HashMap;

public class Errores { 

	/*Datos de usuario invalidos*/
	public static final long DATOS_USUARIO_INVALIDOS = 1;

	/*Capture campos requeridos*/
	public static final long CAMPOS_REQUERIDOS = 2;

	/*El dispositivo no existe*/
	public static final long NO_EXISTE_DISPOSITIVO = 3;

	/*Ya se encuentra logueado*/
	public static final long SESION_ACTIVA = 4;

	/*Favor de iniciar sesión*/
	public static final long SESION_INACTIVA = 5;

	/*Error al buscar el contacto*/
	public static final long ERROR_BUSCAR_CONTACTO = 6;

	/*Error al buscar el prospecto*/
	public static final long ERROR_BUSCAR_PROSPECTO = 7;

	/*Error al buscar el contrato*/
	public static final long ERROR_BUSCAR_CONTRATO = 8;

	/*Error al buscar la supervisión*/
	public static final long ERROR_BUSCAR_SUPERVISION = 9;

	/*Error al buscar la recuperación*/
	public static final long ERROR_BUSCAR_RECUPERACION = 10;

	/*Error de conexión a Internet*/
	public static final long ERROR_CONEXION_INTERNET = 11;

	/*Error al modificar el contacto*/
	public static final long ERROR_MODIFICAR_CONTACTO = 12;

	/*Error al agregar el contacto*/
	public static final long ERROR_AGREGAR_CONTACTO = 13;

	/*Error al iniciar sesión*/
	public static final long ERROR_INICIAR_SESION = 14;

	/*Error al rechazar contacto*/
	public static final long ERROR_RECHAZO_CONTACTO = 15;

	/*Error al obtener la agenda*/
	public static final long ERROR_OBTENER_AGENDA = 16;

	/*Error al autorizar contacto*/
	public static final long ERROR_AUTORIZAR_CONTACTO = 17;

	/*Error al obtener los estados de nacimiento*/
	public static final long ERROR_OBTENER_ESTADOS = 18;

	/*Error al obtener colonias*/
	public static final long ERROR_OBTENER_COLONIAS = 19;

	/*Error al obtener actividades*/
	public static final long ERROR_OBTENER_ACTIVIDADES = 20;

	/*Error al obtener ocupaciones*/
	public static final long ERROR_OBTENER_OCUPACIONES = 22;

	/*Error al obtener giros*/
	public static final long ERROR_OBTENER_GIROS = 23;

	/*Error al obtener asesores*/
	public static final long ERROR_OBTENER_ASESORES = 24;

	/*Error al obtener sucursales*/
	public static final long ERROR_OBTENER_SUCURSALES = 25;

	/*Error obtener modos contacto siguiente*/
	public static final long ERROR_OBTENER_MODOS_CONTACTO = 26;

	/*Los campos están incorrectos*/
	public static final long ERROR_CAMPOS_INCORRECTOS = 27;

	/*Error al obtener catálogo de categorías.*/
	public static final long ERROR_OBTENER_CATEGORIAS_CREDITO = 28;

	/*Error al obtener subcategorías de crédito.*/
	public static final long ERROR_OBTENER_SUBCATEGORIAS_CREDITO = 29;

	/*Error al obtener los productos de crédito*/
	public static final long ERROR_OBTENER_PRODUCTOS_CREDITO = 30;

	/*Error al obtener las frecuencias de pago*/
	public static final long ERROR_OBTENER_FRECUENCIAS_PAGO = 31;

	/*Error al realizar operación. Consulte a su supervisor.*/
	public static final long ERR_REALIZAR_OPERACION = 32;

	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(DATOS_USUARIO_INVALIDOS,"Datos de usuario invalidos");
		descError.put(CAMPOS_REQUERIDOS,"Capture campos requeridos");
		descError.put(NO_EXISTE_DISPOSITIVO,"El dispositivo no existe");
		descError.put(SESION_ACTIVA,"Ya se encuentra logueado");
		descError.put(SESION_INACTIVA,"Favor de iniciar sesión");
		descError.put(ERROR_BUSCAR_CONTACTO,"Error al buscar el contacto");
		descError.put(ERROR_BUSCAR_PROSPECTO,"Error al buscar el prospecto");
		descError.put(ERROR_BUSCAR_CONTRATO,"Error al buscar el contrato");
		descError.put(ERROR_BUSCAR_SUPERVISION,"Error al buscar la supervisión");
		descError.put(ERROR_BUSCAR_RECUPERACION,"Error al buscar la recuperación");
		descError.put(ERROR_CONEXION_INTERNET,"Error de conexión a Internet");
		descError.put(ERROR_MODIFICAR_CONTACTO,"Error al modificar el contacto");
		descError.put(ERROR_AGREGAR_CONTACTO,"Error al agregar el contacto");
		descError.put(ERROR_INICIAR_SESION,"Error al iniciar sesión");
		descError.put(ERROR_RECHAZO_CONTACTO,"Error al rechazar contacto");
		descError.put(ERROR_OBTENER_AGENDA,"Error al obtener la agenda");
		descError.put(ERROR_AUTORIZAR_CONTACTO,"Error al autorizar contacto");
		descError.put(ERROR_OBTENER_ESTADOS,"Error al obtener los estados de nacimiento");
		descError.put(ERROR_OBTENER_COLONIAS,"Error al obtener colonias");
		descError.put(ERROR_OBTENER_ACTIVIDADES,"Error al obtener actividades");
		descError.put(ERROR_OBTENER_OCUPACIONES,"Error al obtener ocupaciones");
		descError.put(ERROR_OBTENER_GIROS,"Error al obtener giros");
		descError.put(ERROR_OBTENER_ASESORES,"Error al obtener asesores");
		descError.put(ERROR_OBTENER_SUCURSALES,"Error al obtener sucursales");
		descError.put(ERROR_OBTENER_MODOS_CONTACTO,"Error obtener modos contacto siguiente");
		descError.put(ERROR_CAMPOS_INCORRECTOS,"Los campos están incorrectos");
		descError.put(ERROR_OBTENER_CATEGORIAS_CREDITO,"Error al obtener catálogo de categorías.");
		descError.put(ERROR_OBTENER_SUBCATEGORIAS_CREDITO,"Error al obtener subcategorías de crédito.");
		descError.put(ERROR_OBTENER_PRODUCTOS_CREDITO,"Error al obtener los productos de crédito");
		descError.put(ERROR_OBTENER_FRECUENCIAS_PAGO,"Error al obtener las frecuencias de pago");
		descError.put(ERR_REALIZAR_OPERACION,"Error al realizar operación. Consulte a su supervisor.");
	}

}