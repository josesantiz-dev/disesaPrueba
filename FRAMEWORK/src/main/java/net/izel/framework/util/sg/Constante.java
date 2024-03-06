package net.izel.framework.util.sg;

import java.util.HashMap;

public abstract class Constante {
	
	// property constants
	public static final Long ALTA_ID = 100000000004L;
	
	public static final Long BAJA_ID = 100000000005L;
	
	//SEGUROS
	public static final Long COSTO_SEGURO_OBLIGARIO = 1000000000005L;
	
	//id de reporte
	public static final Long SEG_VID = 195L;
	public static final Long SEG_CAN = 196L;
	public static final Long SEG_INF = 194L;
	public static final Long ASI_MED = 203L;
	public static final Long AP = 198L;
	public static final Long ASI_FUN = 204L;
	
	public static HashMap<Long, String> descEstatus = new HashMap<Long, String>();
	
	static {
		descEstatus.put(ALTA_ID,"A");
		descEstatus.put(BAJA_ID,"B");
		descEstatus.put(COSTO_SEGURO_OBLIGARIO,"Constante seguro obligatorio");
	}
}
