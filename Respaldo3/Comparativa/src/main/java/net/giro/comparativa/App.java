package net.giro.comparativa;

import java.util.HashMap;

public class App {
    public static void main(String[] args) throws Exception {
		HashMap<String, String> params = null;
		String[] splitted = null;
		Comparativa comp = null;
		
    	try {
	    	// Decimal a Binario
    		String val = Integer.toString(3, 2);// Integer.toBinaryString(maxLongitud);
    		val = String.format("%03d", Integer.valueOf(val));
    		splitted = val.split("(?!^)");
    		System.out.println("0 : " + splitted[0]);
    		System.out.println("1 : " + splitted[1]);
    		System.out.println("2 : " + splitted[2]);
    		System.out.println("decimal : " + Integer.parseInt(val, 2));
    		splitted = null;
	    	
    		int size = 1001;
    		int limit = 1000;
    		double factor = 0.001;
    		int laps = 0;
    		int lap = 0;
    		int itemInicial = 0;
    		int itemFinal = 0;
    		
			laps = (int) Math.ceil(size * factor);

			do {
				itemInicial = 0;
				itemFinal = (lap * limit) + limit;
				itemFinal = itemFinal > size ? size : itemFinal;
				for (int index = (lap * limit); index < itemFinal; index++) 
					itemInicial = itemInicial <= 0 ? index : itemInicial;
				lap++;
		    	System.out.println("Lap : " + lap + " - ItemInicial : " + itemInicial);
			} while (lap < laps);
    		
    		TipoDeComprobante enumVal = null;
    		enumVal = TipoDeComprobante.fromString("i");
	    	System.out.println("TipoDeComprobante : " + enumVal.value());
    		enumVal = TipoDeComprobante.fromString("ingreso");
	    	System.out.println("TipoDeComprobante : " + enumVal.value());
    		enumVal = TipoDeComprobante.fromString("I");
	    	System.out.println("TipoDeComprobante : " + enumVal.value());
    		enumVal = TipoDeComprobante.fromString("INGRESO");
	    	System.out.println("TipoDeComprobante : " + enumVal.value());
    		
    		
    		// Generamos los parametros de acuerdo a los argumentos
    		params = new HashMap<String, String>();
    		for (String p : args) {
    			splitted = p.split("=");
    			params.put(splitted[0], splitted[1]);
    		}

    		// PARAMETROS de ejemplo 
	    	System.out.println("Inicializando parametros ... ");
			params.put("pgDriver","org.postgresql.Driver");
			params.put("pgUrl","jdbc:postgresql://localhost:5432/disesa");
			//params.put("pgUrl","jdbc:postgresql://localhost:5555/disesa");
			params.put("pgUsuario","apps");
			params.put("pgPassword","apps");
			params.put("ruta_img","/home/javaz/developer/sources/air/reportes/imagenes/");
			params.put("idComparativa","10001417");
			params.put("idObra","10000739");
			params.put("idFamilia","0");
			params.put("idRequisicion","0");
			params.put("ruta_salida","/home/javaz/developer/sources/air/outputs/");
			params.put("salida","/home/javaz/developer/sources/air/outputs/Comparativa_" + params.get("idComparativa") + "_O-" + params.get("idObra"));

	    	// creamos nuestra instancia y le pasamos los parametros generados si corresponde
    		if (params != null && ! params.isEmpty()) {
    	    	System.out.println("Ejecutando reporte ... ");
    	    	comp = new Comparativa(params);
    	    	comp.generarReporte();
    	    	System.out.println("Reporte terminado");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw e;
    	}
    }
}
