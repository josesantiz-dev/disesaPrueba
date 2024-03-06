
/*
*
* Copyright (c) 2012 OPCIONES EMPRESARIALES DEL NORESTE S.A. DE C.V. S.F.P / CONDESE S.C.
* Avenida Delfines s/n Entre Marlin y Tiburon, La Paz, BCS Mexico CP 23090.
*
* Clase de definicion de constantes de errores en servicios
*
*/

package net.izel.framework.util.cl;

import java.util.HashMap;

public class TiposVinculaciones { 
	/*
	TIPO_VINCULACION	1000000000010	RELACION LABORAL CON LA INSTITUCION
	TIPO_VINCULACION	1000000000011	FAMILIAR
	TIPO_VINCULACION	1000000000012	BENEFICIARIO
	TIPO_VINCULACION	1000000000013	APODERADO
	TIPO_VINCULACION	1000000000014	FUNCIONARIO
	TIPO_VINCULACION	1000000000015 	GRUPO ECONOMICO
	TIPO_VINCULACION	1000000000016	OTROS VINCULOS
	TIPO_VINCULACION	1000000000017	CONTACTO
	TIPO_VINCULACION	1000000000018	INTEGRANTE GPO. SOLIDARIO
	TIPO_VINCULACION	1000000000583	GARANTE
	TIPO_VINCULACION	1000000000582	AVAL
*/
	
	/*1.beneficiarios*/
	public static final long TIPO_BENEFICIARIO = 1000000000012L;

	/*2.familiares*/
	public static final long TIPO_FAMILIARES = 1000000000011L;

	/*3.contactos*/
	public static final long TIPO_CONTACTO = 1000000000017L;

	/*4.apoderados*/
	public static final long TIPO_APODERADO = 1000000000013L;
	
	/*5.otras vinculaciones*/
	public static final long TIPO_OTRAS_VINCULACIONES = 1000000000016L;
	
	/*6.funcionarios*/
	public static final long TIPO_FUNCIONARIO = 1000000000014L;
	
	/*7.Integrantes grupo solidario*/
	public static final long TIPO_INTEGRANTES = 1000000000018L;

	/*8.Grupo economico*/
	public static final long TIPO_GRUPO_ECONOMICO = 1000000000015L;
	
	/*9.Relacion laboral con la institucion*/
	public static final long TIPO_RELACION_LABORAL = 1000000000010L;
	
	/*10.Garante*/
	public static final long TIPO_GARANTE = 1000000000583L;
	
	/*11.Aval*/
	public static final long TIPO_AVAL = 1000000000582L;
	
	public static HashMap<Long, String> descTipoVinculacion = new HashMap<Long, String>();

	static {
		descTipoVinculacion.put(TIPO_BENEFICIARIO,"Beneficiario");
		descTipoVinculacion.put(TIPO_FAMILIARES,"Familiar");
		descTipoVinculacion.put(TIPO_CONTACTO,"Contacto");
		descTipoVinculacion.put(TIPO_APODERADO,"Apoderado");
		descTipoVinculacion.put(TIPO_OTRAS_VINCULACIONES,"Otra vinculacion");
		descTipoVinculacion.put(TIPO_FUNCIONARIO,"Fucionario");
		descTipoVinculacion.put(TIPO_INTEGRANTES,"Integrante Grupo Solidario");
		descTipoVinculacion.put(TIPO_RELACION_LABORAL,"Relacion laboral con la institucion");
		descTipoVinculacion.put(TIPO_GRUPO_ECONOMICO,"Grupo economico");
		descTipoVinculacion.put(TIPO_GARANTE,"Garante");
		descTipoVinculacion.put(TIPO_AVAL,"Aval");
		
	}

}