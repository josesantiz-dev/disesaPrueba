package net.izel.framework.util;

public class NumericUtil
{

	public static final String fmtSalidaMoneda = "###,###,##0.00";
	public static final String fmtSalidaNumero = "###,###";
	
	
	//metodo para validar si una cadena recibida es numerica
	public static boolean isNumero(String cadena){
		int countPunctuation = 0;
		if(cadena == null || cadena.isEmpty())
			return false;
		
		int i = 0;
		if(cadena.charAt(0) == '-'){
			if(cadena.length() > 1){
				i++;
			}else{
				return false;
			}
		}
		
		for(; i< cadena.length(); i++){
			if(!Character.isDigit(cadena.charAt(i))){
				if(Character.isDefined('.')){
					countPunctuation ++;
				}else{
					return false;
				}
			}
		}
		if(countPunctuation>1){
			return false;
		}
		
		return true;
	}
	
	public static boolean isDecimalNumber(String cadena){
	    try {
	    	Float.parseFloat(cadena);
	    	return true;
    	} catch (NumberFormatException nfe){
    		return false;
    	}
    }
	
	
	public static String formateaNumero(String cadena){
	    try {
	    	return String.format(fmtSalidaMoneda, cadena);
    	} catch (NumberFormatException nfe){
    		return "";
    	}
    }
}

