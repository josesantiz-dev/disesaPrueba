package net.giro.comparativa;

import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;


public class App {
    public static void main(String[] args) throws Exception {
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		String[] splitted = null;
    		
    		BasicConfigurator.configure();
    		
    		// Generamos los parametros de acuerdo a los argumentos
    		for (String p : args) {
    			splitted = p.split("=");
    			params.put(splitted[0], splitted[1]);
    		}

    		// PARAMETROS de ejemplo 
			params.put("pgUsuario","apps");
			params.put("pgPassword","apps");
			//params.put("pgUrl","jdbc:postgresql://localhost:5432/disesa");
			params.put("pgUrl","jdbc:postgresql://localhost:5555/disesa");
			params.put("pgDriver","org.postgresql.Driver");
			params.put("ruta_img","/home/javaz/Developer/sources/reportes/imagenes/");
			params.put("idComparativa","10000179");
			//params.put("idObra","10000090");
			params.put("idObra","10000075");
			params.put("idFamilia","0");
			params.put("idRequisicion","0");
			params.put("ruta_salida","/home/javaz/Developer/sources/ERP/reportes/outputs/");
			params.put("salida","/home/javaz/Developer/sources/ERP/reportes/outputs/Comparativa-" + params.get("idObra"));
			
    		if (params != null && ! params.isEmpty()) {
    	    	// creamos nuestra instancia y le pasamos los parametros generados 
    	    	Comparativa comp = new Comparativa(params);
    	    	
    	    	// Generamos reporte de comparativa
    	    	comp.generarReporte();
    		}
    	} catch (Exception e) {
    		throw e;
    	}
    }
}
