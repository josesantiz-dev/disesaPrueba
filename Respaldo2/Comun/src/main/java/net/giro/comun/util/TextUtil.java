package net.giro.comun.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
	
	/**
	 * Metodo que devuelve un booleano true si es caso es blanco o nulo y false 
	 * en caso contrario 
	 * @param cadena: cadena que se va evaluar
	 * @return boolean [true|false]
	 */
	public static boolean esBlankStringCadena(String cadena){
		boolean b = true;
		if (cadena != null && cadena.trim().length()>0){
			return false;
		}
		return b;
	}
	
	/**
	 * Metodo que te devuelve un arreglo con datos de una trama contenido 
	 * tiene un separador
	 * @param cadena: parametro de tipo cadena ejemplo ["234;444;567;24"]
	 * @param separador: parametro cadena [";"]
	 * @return retorna un arreglo s[0]="234" s[1]="444" s[2]="567" s[3]="24" 
	 * o en su defecto null;
	 */
	public static String[] desencadenar(String cadena,String separador){
		if (cadena == null || "".equals(cadena))
			return null;
		
		StringTokenizer st = new StringTokenizer(cadena,separador);
		String[] s = new String[st.countTokens()];
		int i=0;
		while (st.hasMoreTokens()) {
			s[i] = st.nextToken();
			i++;
		}
		
		return s;
	}
	
	/**
	 * Metodo que te devuelve una Lista de strings con datos de una trama contenido 
	 * tiene un separador
	 * @param cadena: parametro de tipo cadena ejemplo ["234;444;567;24"]
	 * @param separador: parametro cadena [";"]
	 * @return retorna un List<String> 
	 * o en su defecto null;
	 */
	public static List<String> desencadenarList(String cadena,String separador){
		if (cadena == null || "".equals(cadena))
			return null;
		
		StringTokenizer st = new StringTokenizer(cadena,separador);
		List<String> s = new ArrayList<String>();
		
		while (st.hasMoreTokens()) {
			s.add(st.nextToken());
		}
		
		return s;
	}
	
	//Recibe un String y si es null devuelve ""
	public static String obtieneCadena(String cadena){
	    return cadena == null?"":cadena;
	}
	
	//Prepara una cadena para que sea aceptable al grid
	public static String preparaCadena(String cadena){
	    if(cadena == null)
	        return cadena;

        //Transforma el Enter: (char)10 en espacio vacio
	    cadena = cadena.replace((char)10,' ');
	    cadena = cadena.replace((char)13,' ');
        
        //Reemplaza las " y , con espacios vacios
	    cadena = cadena.replace(',',' ');
	    cadena = cadena.replace('"',' ');
	    
	    return cadena;
	}
	
	//Devuelve el substring de una cadena
	public static String obtieneSubStr(String cadena, int inicio, int fin){
	    String subStr = "";
	    
	    if(cadena == null || "".equals(cadena))
	        return subStr;
	    
	    if(inicio > cadena.length() || fin > cadena.length() || inicio > fin)
	        return subStr;
	    
	    subStr = cadena.substring(inicio,fin);
	    
	    return subStr;
	}
	
	public static String llenaCeros(String strCad, int intNumero) {
			String strAcumulaCadena = "";
			for (int i=0; i<intNumero; i++){
				strAcumulaCadena = strAcumulaCadena + strCad;
			}
			
	return strAcumulaCadena;
	}
	
	public static int contarNumerosEnCadena(String cadena){
		int cntNrosEnCadena=0;
		String caracter = "";
		for (int i=0;i<cadena.length();i++){
			caracter=cadena.substring(i,i+1);
			if (caracter.equals("0") || caracter.equals("1") || caracter.equals("2") || caracter.equals("3") || caracter.equals("4") || caracter.equals("5") || caracter.equals("6") || caracter.equals("7") || caracter.equals("8") || caracter.equals("9")){
				cntNrosEnCadena++;
			}
		}
		return cntNrosEnCadena;
	}
	
	public static int contarRangoCaracteresEnCadena(String cadena, String rangoCaracteres){
		int cntRanCarEnCadena=0;
		String caracter = "";
		int encontrado = 0;
		for (int i=0;i<cadena.length();i++){
			caracter=cadena.substring(i,i+1);
			System.out.println("caracter"+caracter);
			encontrado=rangoCaracteres.indexOf(caracter,0);
			System.out.println("encontrado"+encontrado);
			if (encontrado!=-1) cntRanCarEnCadena++;
		}
		return cntRanCarEnCadena;
	}
	
	public static void main(String args[]){
		System.out.println("contarRangoCaracteresEnCadena>>"+contarRangoCaracteresEnCadena("ABCDEFGHIJALBMNOP","ABC"));
		System.out.println("contarNumerosEnCadena>>"+contarNumerosEnCadena("A23FSDJ78F4354D8D"));
	}
	
	 //metodo para validar correo electronio
    public static boolean esEmail(String correo) {

    	Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            System.out.println("[" + mat.group() + "]");
            return true;
        }else{
            return false;
        }  
            
    }
    
	public static boolean isTelefonoValido(String numeroTelefono) 
	{
		boolean hr;
	
		Pattern pattern = Pattern.compile("^\\d+$");         
        hr= pattern.matcher(numeroTelefono).matches(); 
		return hr;		
	}
	
	public static String format(String cad, Object ... args) { 
		String formateada = cad;
		try { 
			formateada = String.format(cad, args) ; 
		} catch(Exception e ) { 
			
		} 
		return formateada; 
	}
	
	public static String StackTrace(Exception e) { 
		StackTraceElement[] stackTrace = e.getStackTrace(); 
		StringBuilder stackTraceBuilder = new StringBuilder(); 
		for( int idx=0;stackTrace.length!=idx; idx++ ) { 
			stackTraceBuilder.append( stackTrace[idx].toString() ); 
			stackTraceBuilder.append("\n"); 
		} 
		return stackTraceBuilder.toString(); 
	}
	
	
	public static String StackTrace(Throwable e) { 
		StackTraceElement[] stackTrace = e.getStackTrace(); 
		StringBuilder stackTraceBuilder = new StringBuilder(); 
		for( int idx=0;stackTrace.length!=idx; idx++ ) { 
			stackTraceBuilder.append( stackTrace[idx].toString() ); 
			stackTraceBuilder.append("\n"); 
		} 
		return stackTraceBuilder.toString(); 
	}
}

