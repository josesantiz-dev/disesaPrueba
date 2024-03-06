package net.giro.util.tyg;

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

	/*Listado Vacio, Favor de Verificar*/
	public static final long LISTADO_VACIO = 4;

	/*Error al eliminar banco*/
	public static final long ERROR_ELIMINAR_BANCO = 5;

	/*Error al guardar banco*/
	public static final long ERROR_GUARDAR_BANCO = 6;

	/*Error al buscar banco*/
	public static final long ERROR_BUSCAR_BANCO = 7;

	/*Error al listar bancos*/
	public static final long ERROR_LISTAR_BANCO = 8;

	/*Error al listar empresas*/
	public static final long ERROR_LISTAR_EMPRESA = 9;

	/*Error al listar moneda*/
	public static final long ERROR_LISTAR_MONEDA = 10;

	/*Error al listar sucursales*/
	public static final long ERROR_LISTAR_SUCURSAL = 11;

	/*Error al eliminar cuenta de banco*/
	public static final long ERROR_ELIMINAR_CUENTA_BANCO = 12;

	/*Error al guardar cuenta de banco*/
	public static final long ERROR_GUARDAR_CUENTA_BANCO = 13;

	/*Error al buscar cuenta de banco*/
	public static final long ERROR_BUSCAR_CUENTA_BANCO = 14;

	/*Error al guardar valores de monedas*/
	public static final long ERROR_GUARDAR_MONEDAS_VALORES = 15;

	/*Error al buscar valores de monedas*/
	public static final long ERROR_BUSCAR_MONEDAS_VALORES = 16;

	/*Error al eliminar valores de monedas*/
	public static final long ERROR_ELIMINAR_MONEDAS_VALORES = 17;

	/*Error al listar valores de monedas*/
	public static final long ERROR_LISTAR_MONEDAS_VALORES = 18;

	/*Error al listar tasas*/
	public static final long ERROR_LISTAR_TASAS = 19;

	/*Error al guardar valores de tasas*/
	public static final long ERROR_GUARDAR_TASAS_VALORES = 20;

	/*Error al eliminar valores de tasas*/
	public static final long ERROR_ELIMINAR_TASAS_VALORES = 21;

	/*Error al buscar valores de tasas*/
	public static final long ERROR_BUSCAR_TASAS_VALORES = 22;

	/*Error al guardar formas de pago*/
	public static final long ERROR_GUARDAR_FORMAS_PAGO = 23;

	/*Error al buscar formas de pago*/
	public static final long ERROR_BUSCAR_FORMAS_PAGO = 24;

	/*Error al eliminar formas de pago*/
	public static final long ERROR_ELIMINAR_FORMAS_PAGO = 25;

	/*Error al buscar moneda*/
	public static final long ERROR_BUSCAR_MONEDA = 26;

	/*Error al guardar moneda*/
	public static final long ERROR_GUARDAR_MONEDA = 27;

	/*Error al listar estados*/
	public static final long ERROR_LISTAR_ESTADOS = 28;

	/*Error al buscar sucursales bancarias*/
	public static final long ERROR_BUSCAR_SUCURSALES_BANCARIAS = 29;

	/*Error al guardar sucursales bancarias*/
	public static final long ERROR_GUARDAR_SUCURSALES_BANCARIAS = 30;

	/*Error al eliminar sucursales bancarias*/
	public static final long ERROR_ELIMINAR_SUCURSALES_BANCARIAS = 31;

	/*Error al buscar tasas*/
	public static final long ERROR_BUSCAR_TASAS = 32;

	/*Error al guardar tasas*/
	public static final long ERROR_GUARDAR_TASAS = 33;

	/*Error al eliminar tasas*/
	public static final long ERROR_ELIMINAR_TASAS = 34;

	/*Error al buscar tipos de tasas*/
	public static final long ERROR_BUSCAR_TIPOS_TASAS = 35;

	/*Error al guardar tipos de tasas*/
	public static final long ERROR_GUARDAR_TIPOS_TASAS = 36;

	/*Error al eliminar tipos de tasas*/
	public static final long ERROR_ELIMINAR_TIPOS_TASAS = 37;

	public static HashMap<Long, String> descError = new HashMap<Long, String>();

	static {
		descError.put(TRANSACCION_EXITOSA,"Transaccion Exitosa");
		descError.put(ERROR_INESPERADO,"Error Inesperado");
		descError.put(CRITERIO_BUSQUEDA,"Verifique, debe seleccionar un criterio del listado de Tipo de busqueda");
		descError.put(CAMPOS_REQUERIDOS,"Capture campos requeridos");
		descError.put(LISTADO_VACIO,"Listado Vacio, Favor de Verificar");
		descError.put(ERROR_ELIMINAR_BANCO,"Error al eliminar banco");
		descError.put(ERROR_GUARDAR_BANCO,"Error al guardar banco");
		descError.put(ERROR_BUSCAR_BANCO,"Error al buscar banco");
		descError.put(ERROR_LISTAR_BANCO,"Error al listar bancos");
		descError.put(ERROR_LISTAR_EMPRESA,"Error al listar empresas");
		descError.put(ERROR_LISTAR_MONEDA,"Error al listar moneda");
		descError.put(ERROR_LISTAR_SUCURSAL,"Error al listar sucursales");
		descError.put(ERROR_ELIMINAR_CUENTA_BANCO,"Error al eliminar cuenta de banco");
		descError.put(ERROR_GUARDAR_CUENTA_BANCO,"Error al guardar cuenta de banco");
		descError.put(ERROR_BUSCAR_CUENTA_BANCO,"Error al buscar cuenta de banco");
		descError.put(ERROR_GUARDAR_MONEDAS_VALORES,"Error al guardar valores de monedas");
		descError.put(ERROR_BUSCAR_MONEDAS_VALORES,"Error al buscar valores de monedas");
		descError.put(ERROR_ELIMINAR_MONEDAS_VALORES,"Error al eliminar valores de monedas");
		descError.put(ERROR_LISTAR_MONEDAS_VALORES,"Error al listar valores de monedas");
		descError.put(ERROR_LISTAR_TASAS,"Error al listar tasas");
		descError.put(ERROR_GUARDAR_TASAS_VALORES,"Error al guardar valores de tasas");
		descError.put(ERROR_ELIMINAR_TASAS_VALORES,"Error al eliminar valores de tasas");
		descError.put(ERROR_BUSCAR_TASAS_VALORES,"Error al buscar valores de tasas");
		descError.put(ERROR_GUARDAR_FORMAS_PAGO,"Error al guardar formas de pago");
		descError.put(ERROR_BUSCAR_FORMAS_PAGO,"Error al buscar formas de pago");
		descError.put(ERROR_ELIMINAR_FORMAS_PAGO,"Error al eliminar formas de pago");
		descError.put(ERROR_BUSCAR_MONEDA,"Error al buscar moneda");
		descError.put(ERROR_GUARDAR_MONEDA,"Error al guardar moneda");
		descError.put(ERROR_LISTAR_ESTADOS,"Error al listar estados");
		descError.put(ERROR_BUSCAR_SUCURSALES_BANCARIAS,"Error al buscar sucursales bancarias");
		descError.put(ERROR_GUARDAR_SUCURSALES_BANCARIAS,"Error al guardar sucursales bancarias");
		descError.put(ERROR_ELIMINAR_SUCURSALES_BANCARIAS,"Error al eliminar sucursales bancarias");
		descError.put(ERROR_BUSCAR_TASAS,"Error al buscar tasas");
		descError.put(ERROR_GUARDAR_TASAS,"Error al guardar tasas");
		descError.put(ERROR_ELIMINAR_TASAS,"Error al eliminar tasas");
		descError.put(ERROR_BUSCAR_TIPOS_TASAS,"Error al buscar tipos de tasas");
		descError.put(ERROR_GUARDAR_TIPOS_TASAS,"Error al guardar tipos de tasas");
		descError.put(ERROR_ELIMINAR_TIPOS_TASAS,"Error al eliminar tipos de tasas");
	}
}