package net.giro.sesion;

import java.io.InputStream;
import net.giro.sesion.CSoctetStream;
import org.apache.log4j.Logger;

public class CSoctetStream {
	private static Logger log = Logger.getLogger(CSoctetStream.class);
	
	public static String read(InputStream inputS) {
		StringBuffer s = new StringBuffer();
		String respuesta = null;
		
		try {
			for (;;) {
				if (inputS.available() > 20) {
					byte[] buffer = new byte[inputS.available()];
					
					@SuppressWarnings("unused")
					int read = inputS.read(buffer);
					String sx = new String(buffer);
					s.append(sx);
					if (s.indexOf("\"respuesta\":") != -1) {
						break;
					}
					buffer = null;
				}
			}
			
			respuesta = s.toString();
			respuesta = respuesta.trim();
		} catch (Exception e) {
			respuesta = null;
			log.error("Error al leer el stream del socket", e);
		}
		return respuesta;
	}
}