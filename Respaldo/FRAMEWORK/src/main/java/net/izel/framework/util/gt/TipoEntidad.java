
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de estatus en servicio Clientes
*
*/

package net.izel.framework.util.gt;

import java.util.HashMap;

public class TipoEntidad {
	
	/*TIPO ENTIDADES DE GARANTIAS*/
	
	/* 1
	   garantía fisica,
       garantía titulo valor,
       garantía personal
       garantia liquida
    */
	public static final Long ENTIDAD_FISICA_TITULO_PERSONAL_LIQUIDA = 1L;
	
	/* 2 domicilios*/
	public static final Long ENTIDAD_DOMICILIO = 2L;
	
	/* 3 bienes*/
	public static final Long ENTIDAD_BIENES = 3L;

	/* 4 propietarios*/
	public static final Long ENTIDAD_PROPIETARIOS = 4L;
	
	/* 5 tasaciones*/
	public static final Long ENTIDAD_TASACIONES = 5L;
	
	/* 6 poliza de seguros*/
	public static final Long ENTIDAD_POLIZA_SEGUROS = 6L;
	
	/* 7 cobertura operacion*/
	public static final Long ENTIDAD_COBERTURA_OPERACION = 7L;
	
	/* 8 cobertura operacion*/
	public static final Long ENTIDAD_DACION_PAGO = 8L;

	/* 9 tasaciones*/
	public static final Long ENTIDAD_DACION_PAGO_TASACIONES = 9L;
	
	/* 10 tasaciones*/
	public static final Long ENTIDAD_DACION_PAGO_PROPIETARIO = 10L;
	
	/* 11 Dacion pago bienes*/
	public static final Long ENTIDAD_DACION_PAGO_BIENES = 11L;
	
	/*ESTATUS DE LOS REGISTROS DEL MODULO DE GARANTIA*/
	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	static {
		descError.put(ENTIDAD_FISICA_TITULO_PERSONAL_LIQUIDA,"GARANTIA, FISICA, TITULO VALOR, PERSONAL y LIQUIDA");
		descError.put(ENTIDAD_DOMICILIO,"DOMICILIO");
		descError.put(ENTIDAD_BIENES,"BIENES");
		descError.put(ENTIDAD_PROPIETARIOS,"PROPIETARIOS");
		descError.put(ENTIDAD_TASACIONES,"TASACIONES");
		descError.put(ENTIDAD_POLIZA_SEGUROS,"POLIZA DE SEGUROS");
		descError.put(ENTIDAD_COBERTURA_OPERACION,"COBERTURA OPERACION");
		descError.put(ENTIDAD_DACION_PAGO,"DACION EN PAGO");
		descError.put(ENTIDAD_DACION_PAGO_TASACIONES,"DACION PAGO TASACION");
		descError.put(ENTIDAD_DACION_PAGO_PROPIETARIO,"DACION PAGO PROPIETARIO");
	}
	
}