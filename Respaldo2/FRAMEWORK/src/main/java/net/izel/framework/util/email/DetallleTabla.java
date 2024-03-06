package net.izel.framework.util.email;

import java.util.HashMap;

public class DetallleTabla 
{
	public static final long DETALLE_SIMULACION_AHORRO = 1;
	public static final long DETALLE_SIMULACION_CREDITO7= 2;
	public static final long DETALLE_SIMULACION_CREDITO8 = 3;
	
	public static HashMap<Long, String> template = new HashMap<Long, String>();
	static {
		template.put(DETALLE_SIMULACION_AHORRO,"<tr class=\"[ETIQ0]\"><td>[ETIQ1]</td><td>[ETIQ2]</td><td>[ETIQ3]</td><td>[ETIQ4]</td><td class=\"remarc\">[ETIQ5]</td></tr>");
		template.put(DETALLE_SIMULACION_CREDITO7,"<tr><td style=\"text-align:center;\">[ETIQ0]</td><td>[ETIQ1]</td><td style=\"text-align:center;\">[ETIQ2]</td><td style=\"text-align:right;\">[ETIQ3]</td><td style=\"text-align:right;\">[ETIQ4]</td><td style=\"text-align:right;\">[ETIQ5]</td><td style=\"text-align:right;\">[ETIQ6]</td></tr>");
		template.put(DETALLE_SIMULACION_CREDITO8,"<tr><td style=\"text-align:center;\">[ETIQ0]</td><td>[ETIQ1]</td><td style=\"text-align:center;\">[ETIQ2]</td><td style=\"text-align:right;\">[ETIQ3]</td><td style=\"text-align:right;\">[ETIQ4]</td><td style=\"text-align:right;\">[ETIQ5]</td><td style=\"text-align:right;\">[ETIQ6]</td><td style=\"text-align:right;\">[ETIQ7]</td></tr>");
	}
}

/** !DetallleTabla.java */




