package net.izel.framework.util.bp;

import java.util.HashMap;

public abstract class Constante {
	
	// ESTATUS DEL REGISTRO
	public static final Long ALTA_ID = 1L;	
	public static final Long BAJA_ID = 2L;		
	public static final Long EMPRESA_ID = 1L;	
	
	public static HashMap<Long, String> descEstatus = new HashMap<Long, String>();
	
	static {
		
		descEstatus.put(ALTA_ID, "A");
		descEstatus.put(BAJA_ID, "B");
		
		
	}
}
