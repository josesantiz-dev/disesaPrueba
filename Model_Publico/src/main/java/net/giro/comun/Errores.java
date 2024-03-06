package net.giro.comun;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Errores { 
	public static final Properties properties = new Properties();
	
	public static String get(String llave, Object ... args) { 
		String formateada = get(llave);
		try { 
			formateada = String.format(formateada, args) ; 
		} catch(Exception e ) { 
			e.printStackTrace();
		} 
		return formateada; 
	}
	
	public static String get(String llave) { 
		String valor = llave;
		try { 
			if (properties == null) 
				return llave;
			valor = properties.getProperty(llave);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return valor;
	}
	
	static {
	    final ClassLoader cl = Thread.currentThread().getContextClassLoader();
	    final InputStream inputStream = cl.getResourceAsStream("messages.properties");
	    
	    if (inputStream != null) {
	    	try {
	    		properties.load(inputStream);
	      	} catch (IOException e) {
				e.printStackTrace();
			} finally {
		    	try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }  
	    }
	}
	
	
}