package net.giro.publico.util;

import java.util.HashMap;

public class Errores { 
	/*Transaccion exitosa*/
	public static final long TRANSACCION_EXITOSA = 0;

	/*Error inesperado*/
	public static final long ERROR_INESPERADO = 1;

	/*Verifique, debe seleccionar un criterio del listado de Tipo de busqueda*/
	public static final long CRITERIO_BUSQUEDA = 2;

	/*Capture campos requeridos*/
	public static final long CAMPOS_REQUERIDOS = 3;

	/*Listado Vacio, Favor de Verificar*/
	public static final long LISTADO_VACIO = 4;

	/*Error al buscar funciones*/
	public static final long ERROR_BUSCAR_FUNCION = 5;

	/*Error al guardar funcion*/
	public static final long ERROR_GUARDAR_FUNCION = 6;

	/*Error al eliminar funcion*/
	public static final long ERROR_ELIMINAR_FUNCION = 7;

	/*Error al listar aplicaciones*/
	public static final long ERROR_LISTAR_APLICACIONES = 8;

	/*Error al buscar grupo de valores*/
	public static final long ERROR_BUSCAR_GRUPO_VALORES = 9;

	/*Error al guardar grupo de valores*/
	public static final long ERROR_GUARDAR_GRUPO_VALORES = 10;

	/*Error al eliminar grupo de valores*/
	public static final long ERROR_ELIMINAR_GRUPO_VALORES = 11;

	/*Error al buscar menus*/
	public static final long ERROR_BUSCAR_MENU = 12;

	/*Error al guardar menu*/
	public static final long ERROR_GUARDAR_MENU = 13;

	/*Error al eliminar menu*/
	public static final long ERROR_ELIMINAR_MENU = 14;

	/*Error al buscar aplicacion*/
	public static final long ERROR_BUSCAR_APLICACION = 15;

	/*Error al guardar funcion en menu*/
	public static final long ERROR_GUARDAR_MENU_FUNCION = 16;

	/*Error al eliminar menu funcion*/
	public static final long ERROR_ELIMINAR_MENU_FUNCION = 17;

	/*Error al buscar funcion de menu*/
	public static final long ERROR_BUSCAR_MENU_FUNCION = 18;

	/*Error al buscar perfil*/
	public static final long ERROR_BUSCAR_PERFIL = 19;

	/*Error al guardar perfil*/
	public static final long ERROR_GUARDAR_PERFIL = 20;

	/*Error al eliminar perfil*/
	public static final long ERROR_ELIMINAR_PERFIL = 21;

	/*Error al listar menus*/
	public static final long ERROR_LISTAR_MENUS = 22;

	/*Error al buscar resposabilidades*/
	public static final long ERROR_BUSCAR_RESPONSABILIDAD = 23;

	/*Error al guardar responsabilidad*/
	public static final long ERROR_GUARDAR_RESPONSABILIDAD = 24;

	/*Error al eliminar responsabilidad*/
	public static final long ERROR_ELIMINAR_RESPONSABILIDAD = 25;

	/*Verifique que el rango de fechas sea correcto.*/
	public static final long RANGO_FECHAS_INVALIDO = 26;

	/*Error al buscar usuario*/
	public static final long ERROR_BUSCAR_USUARIO = 27;

	/*Error al guardar usuario*/
	public static final long ERROR_GUARDAR_USUARIO = 28;

	/*Error al eliminar usuario*/
	public static final long ERROR_ELIMINAR_USUARIO = 29;

	/*Error al agregar responsabilidad*/
	public static final long ERROR_AGREGAR_RESPONSABILIDAD = 30;

	/*Error al listar responsabilidades*/
	public static final long ERROR_LISTAR_RESPONSABILIDADES = 31;

	/*Error al quitar responsabilidad*/
	public static final long ERROR_QUITAR_RESPONSABILIDAD = 32;

	/*Error al listar valores*/
	public static final long ERROR_LISTAR_VALORES = 33;

	/*Error al listar usuarios*/
	public static final long ERROR_LISTAR_USUARIOS = 34;

	/*Error al listar responsabilidades*/
	public static final long ERROR_LISTAR_RESPONSABILIDAD = 35;

	/*Error al listar empresas*/
	public static final long ERROR_LISTAR_EMPRESA = 35;

	/*Error al listar sucursal*/
	public static final long ERROR_LISTAR_SUCURSAL = 36;

	/*Error al buscar valores de perfiles*/
	public static final long ERROR_BUSCAR_PERFILES_VALORES = 37;

	/*Error al guardar valores de perfiles*/
	public static final long ERROR_GUARDAR_PERFILES_VALORES = 38;

	/*Error al eliminar valores de perfiles*/
	public static final long ERROR_ELIMINAR_PERFILES_VALORES = 39;

	/*Error al buscar valor*/
	public static final long ERROR_BUSCAR_VALOR = 40;

	/*Error al guardar valor*/
	public static final long ERROR_GUARDAR_VALOR = 41;

	/*Error al eliminar valor*/
	public static final long ERROR_ELIMINAR_VALOR = 42;
	
	/*Error al cargar oficiales*/
	public static final long ERROR_CARGAR_OFICIALES = 43;
	
	/*Error al guardar oficial*/
	public static final long ERROR_GUARDAR_OFICIAL = 44;
	
	/*Error al guardar localidad*/
	public static final long ERROR_GUARDAR_LOCALIDAD = 45;
	
	/*Error al buscar localidades*/
	public static final long ERROR_BUSCAR_LOCALIDADES = 46;
	
	/*Error al buscar municipios*/
	public static final long ERROR_BUSCAR_MUNICIPIOS = 47;
	
	/*Error al cargar estados*/
	public static final long ERROR_CARGAR_ESTADOS = 48;
	
	/*Error al guardar municipios*/
	public static final long ERROR_GUARDAR_MUNICIPIO = 49;
	
	/*Verifique, falta seleccionar municipio*/
	public static final long ERROR_FALTA_MUNICIPIO = 50;
	
	/*Verifique, falta seleccionar estado*/
	public static final long ERROR_FALTA_ESTADO = 51;
	
	/*Error al guardar empresas*/
	public static final long ERROR_GUARDAR_EMPRESA = 52;
	
	/*Error al buscar empresas*/
	public static final long ERROR_BUSCAR_EMPRESAS = 53;
	
	/*Error al eliminar empresas*/
	public static final long ERROR_ELIMINAR_EMPRESAS = 54;
	
	/*Error al cargar catalogo de tipos de domicilio*/
	public static final long ERROR_CARGAR_CATALOGO_TIPOS_DOMICILIO = 55;
	
	/*Error al cargar monedas*/
	public static final long ERROR_CARGAR_MONEDAS = 56;
	
	/*Error al buscar colonias*/
	public static final long ERROR_BUSCAR_COLONIAS = 57;
	
	/*Error al guardar sucursal*/
	public static final long ERROR_GUARDAR_SUCURSAL = 58;
	
	/*Verifique, falta seleccionar colonia*/
	public static final long ERROR_FALTA_COLONIA = 59;
	
	/*Error al guardar colonia*/
	public static final long ERROR_GUARDAR_COLONIA = 60;
	
	/*Verifique, falta seleccionar localidad*/
	public static final long ERROR_FALTA_LOCALIDAD = 61;
	
	/*Error al intentar recuperar el folio de facturacion*/
	public static final long ERROR_FOLIO_FACTURACION = 62;
	
	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(TRANSACCION_EXITOSA,"Transaccion exitosa");
		descError.put(ERROR_INESPERADO,"Error inesperado");
		descError.put(CRITERIO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de busqueda");
		descError.put(CAMPOS_REQUERIDOS,"Capture campos requeridos");
		descError.put(LISTADO_VACIO,"Listado Vacio, Favor de Verificar");
		descError.put(ERROR_BUSCAR_FUNCION,"Error al buscar funciones");
		descError.put(ERROR_GUARDAR_FUNCION,"Error al guardar funcion");
		descError.put(ERROR_ELIMINAR_FUNCION,"Error al eliminar funcion");
		descError.put(ERROR_LISTAR_APLICACIONES,"Error al listar aplicaciones");
		descError.put(ERROR_BUSCAR_GRUPO_VALORES,"Error al buscar grupo de valores");
		descError.put(ERROR_GUARDAR_GRUPO_VALORES,"Error al guardar grupo de valores");
		descError.put(ERROR_ELIMINAR_GRUPO_VALORES,"Error al eliminar grupo de valores");
		descError.put(ERROR_BUSCAR_MENU,"Error al buscar menus");
		descError.put(ERROR_GUARDAR_MENU,"Error al guardar menu");
		descError.put(ERROR_ELIMINAR_MENU,"Error al eliminar menu");
		descError.put(ERROR_BUSCAR_APLICACION,"Error al buscar aplicacion");
		descError.put(ERROR_GUARDAR_MENU_FUNCION,"Error al guardar funcion en menu");
		descError.put(ERROR_ELIMINAR_MENU_FUNCION,"Error al eliminar menu funcion");
		descError.put(ERROR_BUSCAR_MENU_FUNCION,"Error al buscar funcion de menu");
		descError.put(ERROR_BUSCAR_PERFIL,"Error al buscar perfil");
		descError.put(ERROR_GUARDAR_PERFIL,"Error al guardar perfil");
		descError.put(ERROR_ELIMINAR_PERFIL,"Error al eliminar perfil");
		descError.put(ERROR_LISTAR_MENUS,"Error al listar menus");
		descError.put(ERROR_BUSCAR_RESPONSABILIDAD,"Error al buscar resposabilidades");
		descError.put(ERROR_GUARDAR_RESPONSABILIDAD,"Error al guardar responsabilidad");
		descError.put(ERROR_ELIMINAR_RESPONSABILIDAD,"Error al eliminar responsabilidad");
		descError.put(RANGO_FECHAS_INVALIDO,"Verifique que el rango de fechas sea correcto.");
		descError.put(ERROR_BUSCAR_USUARIO,"Error al buscar usuario");
		descError.put(ERROR_GUARDAR_USUARIO,"Error al guardar usuario");
		descError.put(ERROR_ELIMINAR_USUARIO,"Error al eliminar usuario");
		descError.put(ERROR_AGREGAR_RESPONSABILIDAD,"Error al agregar responsabilidad");
		descError.put(ERROR_LISTAR_RESPONSABILIDADES,"Error al listar responsabilidades");
		descError.put(ERROR_QUITAR_RESPONSABILIDAD,"Error al quitar responsabilidad");
		descError.put(ERROR_LISTAR_VALORES,"Error al listar valores");
		descError.put(ERROR_LISTAR_USUARIOS,"Error al listar usuarios");
		descError.put(ERROR_LISTAR_RESPONSABILIDAD,"Error al listar responsabilidades");
		descError.put(ERROR_LISTAR_EMPRESA,"Error al listar empresas");
		descError.put(ERROR_LISTAR_SUCURSAL,"Error al listar sucursal");
		descError.put(ERROR_BUSCAR_PERFILES_VALORES,"Error al buscar valores de perfiles");
		descError.put(ERROR_GUARDAR_PERFILES_VALORES,"Error al guardar valores de perfiles");
		descError.put(ERROR_ELIMINAR_PERFILES_VALORES,"Error al eliminar valores de perfiles");
		descError.put(ERROR_BUSCAR_VALOR,"Error al buscar valor");
		descError.put(ERROR_GUARDAR_VALOR,"Error al guardar valor");
		descError.put(ERROR_ELIMINAR_VALOR,"Error al eliminar valor");
		descError.put(ERROR_CARGAR_OFICIALES, "Error al cargar oficiales");
		descError.put(ERROR_GUARDAR_OFICIAL, "Error al guardar oficial");
		descError.put(ERROR_GUARDAR_LOCALIDAD, "Error al guardar localidad");
		descError.put(ERROR_BUSCAR_LOCALIDADES, "Error al buscar localidades");
		descError.put(ERROR_BUSCAR_MUNICIPIOS, "Error al buscar municipios");
		descError.put(ERROR_CARGAR_ESTADOS, "Error al cargar estados");
		descError.put(ERROR_GUARDAR_MUNICIPIO, "Error al guardar municipios");
		descError.put(ERROR_FALTA_MUNICIPIO, "Verifique, falta seleccionar municipio");
		descError.put(ERROR_FALTA_ESTADO, "Verifique, falta seleccionar estado");
		descError.put(ERROR_CARGAR_CATALOGO_TIPOS_DOMICILIO, "Error al cargar catalogo de tipos de domicilio");
		descError.put(ERROR_CARGAR_MONEDAS, "Error al cargar monedas");
		descError.put(ERROR_BUSCAR_COLONIAS, "Error al buscar colonias");
		descError.put(ERROR_GUARDAR_SUCURSAL, "Error al guardar sucursal");
		descError.put(ERROR_FALTA_COLONIA, "Verifique, falta seleccionar colonia");
		descError.put(ERROR_GUARDAR_COLONIA, "Error al guardar colonia");
		descError.put(ERROR_FALTA_LOCALIDAD, "Verifique, falta seleccionar localidad");
		descError.put(ERROR_FOLIO_FACTURACION, "Error al intentar recuperar el folio de facturacion");
	}	
}

// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA   |        AUTOR     | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//   1.0   | 2016-10-19 | Javier Tirado    | Añado el valor ERROR_FOLIO_FACTURACION