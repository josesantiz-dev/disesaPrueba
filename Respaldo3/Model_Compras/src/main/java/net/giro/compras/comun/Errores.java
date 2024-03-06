package net.giro.compras.comun;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Errores { 
	public static  final Properties properties = new Properties();
	
	public static String get(String llave, Object ... args) { 
		String formateada = get(llave);
		try { 
			formateada = String.format(formateada, args) ; 
		} catch(Exception e ) { } 
		return formateada; 
	}
	
	public static String get(String llave) { 
		if (properties == null) 
			return llave;
		String valor = llave;
		try { 
			valor = properties.getProperty(llave);
		} catch(Exception e) { }
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