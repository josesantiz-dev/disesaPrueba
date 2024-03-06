package net.giro.pruebas;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args) {
    	HashMap<String, String> mapReference = new HashMap<String, String>();
    	String ruta = "./cargas/";
    	LectorExcel lector = null;
    	byte[] data = null;
    	Path path = null;
    	
		try {
			HashMap<Long, String> test = new HashMap<Long, String>();
			test.put(10011088L, "SJC-340018 (C23DB96F-2AD0-4258-AEFF-F318F909FD0F)");
			String val = (new Gson()).toJson(test);
			System.out.println(val);
			
			System.out.println("RUTA DE CARGA: " + ruta);
			
	    	// Leemos archivo XLS
			mapReference.put("1","A");
			path = Paths.get(ruta + "test.xls");
			System.out.println("Leyendo --> test.xls");
			data = Files.readAllBytes(path);
			lector = new LectorExcel(mapReference);
			lector.leer(data);
	    	
	    	// Leemos archivo XLSX
			mapReference.put("1","E");
			path = Paths.get(ruta + "test.xlsx");
			System.out.println("Leyendo --> test.xlsx");
			data = Files.readAllBytes(path);
			lector = new LectorExcel(mapReference);
			lector.leer(data);
	    	
	    	/* ---------------------
	    	 * -- TIPOS DE CELDAS --
	    	 * ---------------------
	    	 * 0 CELL_TYPE_NUMERIC
	    	 * 1 CELL_TYPE_STRING
	    	 * 2 CELL_TYPE_FORMULA
	    	 * 3 CELL_TYPE_BLANK
	    	 * 4 CELL_TYPE_BOOLEAN
	    	 * 5 CELL_TYPE_ERROR
	    	 */
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
