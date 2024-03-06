package net.izel.framework.util.cl;

import java.util.HashMap;

public class TiposBusquedaMR
{
	/*nombre*/
	public static final long TIPO_NOMBRE = 1;

	/*regla*/
	public static final long TIPO_REGLA = 2;

	/*servicio*/
	public static final long TIPO_SERVICIO = 3;


	public static HashMap<Long, String> descTipoBusquedaMR= new HashMap<Long, String>();

	static {
		descTipoBusquedaMR.put(TIPO_NOMBRE,"NOMBRE");
		descTipoBusquedaMR.put(TIPO_REGLA,"REGLA");
		descTipoBusquedaMR.put(TIPO_SERVICIO,"SERVICIO");
	}

}
