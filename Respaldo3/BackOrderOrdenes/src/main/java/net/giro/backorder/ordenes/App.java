package net.giro.backorder.ordenes;

import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;

public class App {
    public static void main(String[] args) throws Exception {
		HashMap<String, String> params = null;
		String[] splitted = null;
		
    	try {
    		BasicConfigurator.configure();
    		params = new HashMap<String, String>();
    		// Generamos los parametros de acuerdo a los argumentos
    		for (String p : args) {
    			splitted = p.split("=");
    			params.put(splitted[0], splitted[1]);
    		}

    		// PARAMETROS de ejemplo 
    		if (params.isEmpty()) {
    			params.put("pgDriver","org.postgresql.Driver");
    			params.put("pgUrl","jdbc:postgresql://localhost:5432/disesa");
    			params.put("pgUsuario","apps");
    			params.put("pgPassword","apps");
    			params.put("ruta_salida","/home/javaz/developer/sources/air/outputs/");
    			params.put("ruta_img","/home/javaz/developer/sources/air/reportes/imagenes/");
    			params.put("idOrdenCompra", "10008367");//,"10002853"); //,"10000090");
    			params.put("salida","/home/javaz/developer/sources/air/outputs/backorder_OC-" + params.get("idOrdenCompra"));
    		}
    		
    		if (params != null && ! params.isEmpty()) {
    	    	// creamos nuestra instancia y le pasamos los parametros generados 
    	    	BackOrderOrdenes comp = new BackOrderOrdenes(params);
    	    	
    	    	// Generamos reporte de comparativa
    	    	comp.generarReporte();
    		}
    	} catch (Exception e) {
    		throw e;
    	}
    }
}
