
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de Tipos de entidades en servicio Clientes
*
*/

package net.izel.framework.util.cl;

import java.util.HashMap;

public class TiposEntidades { 

	/*Persona Natural, Persona Juridica, Grupo Solidario*/
	public static final long TIPO_ENTIDAD_NATURAL_JURIDICA_GPOSOLIDARIO = 1;

	/*Persona Negocio*/
	public static final long TIPO_ENTIDAD_NEGOCIO = 2;

	/*Beneficiarios, Familiares, Contactos, Apoderados, Funcionarios, Integrantes grupo Solidario, Otras Vinculaciones*/
	public static final long TIPO_ENTIDAD_VINCULACIONES = 3;

	/*Persona Domicilio*/
	public static final long TIPO_ENTIDAD_DOMICILIO = 4;

	/*Persona Telefono*/
	public static final long TIPO_ENTIDAD_TELEFONO = 5;

	/*Persona Correo Electronico*/
	public static final long TIPO_ENTIDAD_CORREO_ELECTRONICO = 6;

	/*Persona Registro-permiso, Identificaciones */
	public static final long TIPO_ENTIDAD_PERMISO_IDENTIFICACION = 7;

	/*Persona Poder */
	public static final long TIPO_ENTIDAD_PODER = 8;

	public static HashMap<Long, String> descTipoEntidad = new HashMap<Long, String>();

	static {
		descTipoEntidad.put(TIPO_ENTIDAD_NATURAL_JURIDICA_GPOSOLIDARIO,"TIPO DE ENTIDAD: Natural, Juridica, Grupo Solidario");
		descTipoEntidad.put(TIPO_ENTIDAD_NEGOCIO,"TIPO DE ENTIDAD: Negocio");
		descTipoEntidad.put(TIPO_ENTIDAD_VINCULACIONES,"TIPO DE ENTIDAD: Beneficiarios, Familiares, Contactos, Apoderados, Funcionarios, Integrantes grupo Solidario, Otras Vinculaciones");
		descTipoEntidad.put(TIPO_ENTIDAD_DOMICILIO,"TIPO DE ENTIDAD: Domicilios");
		descTipoEntidad.put(TIPO_ENTIDAD_TELEFONO,"TIPO DE ENTIDAD: Telefono");
		descTipoEntidad.put(TIPO_ENTIDAD_CORREO_ELECTRONICO,"TIPO DE ENTIDAD: Correo Electronico");
		descTipoEntidad.put(TIPO_ENTIDAD_PERMISO_IDENTIFICACION,"TIPO DE ENTIDAD:  Registro-Permiso, Identificaciones");
		descTipoEntidad.put(TIPO_ENTIDAD_PODER,"TIPO DE ENTIDAD: Poder");
	}

}