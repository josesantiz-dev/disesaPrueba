
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

public class EstatusGarantia {
	
	/*ESTATUS DE LAS GARANTIAS*/
	
	
	/*GARANTIA APERTURADA*/
	public static final Long ESTATUS_VALORIZADA = 100000000002L;

	/*GARANTIA ACTIVADA*/
	public static final Long ESTATUS_ACTIVADA = 100000000003L;

	/*GARANTIA BLOQUEADA*/
	public static final Long ESTATUS_BLOQUEADA = 100000000004L;
	
	/*GARANTIA ENTREGADA*/
	public static final Long ESTATUS_ENTREGADA = 100000000005L;

	/*GARANTIA ANULADA*/
	public static final Long ESTATUS_ANULADA = 100000000006L;
	
	/*DACION EN PAGO*/
	public static final Long ESTATUS_DACION_EN_PAGO = 100000000007L;
	
	/*ADJUDICADA*/
	public static final Long ESTATUS_ADJUDICADA = 100000000008L;
	
	/*PRE VENTA*/
	public static final Long ESTATUS_PRE_VENTA = 100000000009L;
	
	/*DACION EN PAGO*/
	public static final Long ESTATUS_VENDIDA = 100000000010L;
	
	/*GARANTIA APERTURADA*/
	public static final Long ESTATUS_APERTURADA = 100000000022L;

	/*ESTATUS DE LAS GARANTIAS*/
	public static HashMap<Long, String> descError = new HashMap<Long, String>();
	
	static {
		descError.put(ESTATUS_VALORIZADA,"VALORIZADA");
		descError.put(ESTATUS_ACTIVADA,"ACTIVADA");
		descError.put(ESTATUS_BLOQUEADA,"BLOQUEADA");
		descError.put(ESTATUS_ENTREGADA,"ENTREGADA");
		descError.put(ESTATUS_ANULADA,"ANULADA");
		descError.put(ESTATUS_DACION_EN_PAGO,"DACION EN PAGO");
		descError.put(ESTATUS_ADJUDICADA,"ADJUDICADA");
		descError.put(ESTATUS_PRE_VENTA,"PRE-VENTA");
		descError.put(ESTATUS_VENDIDA,"VENDIDA");
		descError.put(ESTATUS_APERTURADA,"APERTURADA");
	}

}