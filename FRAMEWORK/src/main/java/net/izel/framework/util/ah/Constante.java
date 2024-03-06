package net.izel.framework.util.ah;

import java.util.HashMap;

public abstract class Constante {
	
	// property constants
	public static final Long ALTA_ID = 100000000004L;
	
	public static final Long BAJA_ID = 100000000005L;
	
	public static HashMap<Long, String> descEstatus = new HashMap<Long, String>();
	
	static {
		descEstatus.put(ALTA_ID,"A");
		descEstatus.put(BAJA_ID,"B");
	}
}
