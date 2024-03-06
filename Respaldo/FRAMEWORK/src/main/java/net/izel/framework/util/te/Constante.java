package net.izel.framework.util.te;

import java.util.HashMap;

public abstract class Constante {
	
	// property constants
	public static final Long S_OK = 0L;
	
	public static final Long ALTA_ID = 100000000004L;
	public static final Long BAJA_ID = 100000000005L;
	public static final Long OPE_INGRE_ID = 1000000000035L;
	public static final Long OPE_EGRE_ID = 1000000000036L;
	
	// CONCEPTOS TRANSACCION -publico--
	public static final Long PREPAGO_ADELANTO_CUOTA = 100000000042L;
	public static final Long PREPAGO_EXTRAORDINARIO = 100000000041L;
	public static final Long CANCELACION_ANTICIPADA = 100000000043L;
	
	// CONCEPTOS OPERACION
	public static final Long PAGO_PRESTAMO = 1000000000005L;
	
	// TIPO TRANSACCION
	public static final Long ENTREGA_DINERO = 1000000004175L;
	
	
	public static HashMap<Long, String> descEstatus = new HashMap<Long, String>();
	
	static {
		descEstatus.put(ALTA_ID,"A");
		descEstatus.put(BAJA_ID,"B");
		descEstatus.put(PREPAGO_ADELANTO_CUOTA, "PREPAGO ADELANTO CUOTA");
		descEstatus.put(PREPAGO_EXTRAORDINARIO, "PREPAGO EXTRAORDINARIO");
		descEstatus.put(CANCELACION_ANTICIPADA, "CANCELACION ANTICIPADA");
		descEstatus.put(ENTREGA_DINERO, "ENTREGA_DINERO");
	}
}
