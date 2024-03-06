package net.izel.framework.util.sti;

import java.util.HashMap;

public class Constante 
{

		public static final int PENDIENTE_AUTORIZAR_XPERMISO = 88;
		public static final int PENDIENTE_AUTORIZAR_XHORARIO = 89;
	
	// property constants
		public static final Long ALTA_ID = 100000000004L;
		
		public static final Long BAJA_ID = 100000000005L;
		
		public static HashMap<Long, String> descEstatus = new HashMap<Long, String>();
		
		static {
			descEstatus.put(ALTA_ID,"A");
			descEstatus.put(BAJA_ID,"B");
		}

}

/** !Constante */



