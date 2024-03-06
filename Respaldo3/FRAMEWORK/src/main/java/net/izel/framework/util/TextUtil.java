package net.izel.framework.util;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.izel.framework.tool.ConstantesGral;
import net.izel.framework.util.cat.Errores;

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
			//System.out.println("entro al llenaCeros");
			for (int i=0; i<intNumero; i++){
				//System.out.println("entro al strAcumulaCadena " + strAcumulaCadena + " i:" + i);
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
        pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        mat = pat.matcher(correo);
        return mat.matches();
        /*
        if (mat.find()) {
            System.out.println("[" + mat.group() + "]");
            return true;
        }else{
            return false;
        }  */
            
    }
    
    /**
	 * Metodo que permite validar el RFC de una persona/prospecto
	 * @param cadena: parametro de tipo cadena ejemplo ["234;444;567;24"]
	 * @param separador: parametro cadena [";"]
	 * @param rfc1 : 1ra letra Ape Pat mas 1ra vocal interna Ape Pat, inicial Ape Materno (X si no tiene), inicial primer nombre ["XXXX"]
	 * @param rfc2 : año nacimiento, mes nacimiento, el dia nacimiento ["999999"]
	 * @param tipoPersona : PERSONA NATURAL, PERSONA JURIDICA [1-2]
	 * @param apellidoPaterno : Apellido paterno
	 * @param apellidoMaterno : Apellido materno
	 * @param primerNombre : Primer Nombre
	 * @param segundoNombre : Segundo Nombre
	 * @param rfc3 : Homoclave, designada por SAT
	 * @return retorna un long [codigo de error] 
	 */
	public static long validarRfc(String rfc1, String rfc2, String tipoPersona,
								  String apellidoPaterno, String apellidoMaterno, String primerNombre,
								  String segundoNombre, String rfc3){
		long codigoRespuesta = 0;
		int aRFC   = 0;
		int mesRFC = 0;
		int diaRFC = 0;
		GregorianCalendar calendar = new GregorianCalendar();
		
		if(rfc1 == null || rfc1.trim().equals("")){
			codigoRespuesta = Errores.VERIFIQUE_RFC; //VERIFIQUE_RFC - VERIFIQUE FORMATO DE RFC
			return codigoRespuesta;
		}

		if(tipoPersona.equals("1")){ //PERSONA NATURAL
			if(rfc1.length() != 4){
				codigoRespuesta = Errores.VERIFIQUE_RFC;//VERIFIQUE_RFC1 - VERIFIQUE INICIALES DE RFC;
				return codigoRespuesta;
			}
		}
		
		if(tipoPersona.equals("2")){ //PERSONA JURIDICA
			if(rfc1.length() != 3){
				codigoRespuesta = Errores.VERIFIQUE_RFC1;//VERIFIQUE_RFC1 - VERIFIQUE INICIALES DE RFC;
				return codigoRespuesta;
			}
		}
		
		if(rfc2 == null || rfc2.trim().equals("")){
			codigoRespuesta = Errores.VERIFIQUE_RFC; //VERIFIQUE_RFC - VERIFIQUE FORMATO DE RFC
			return codigoRespuesta;
		}

		if(rfc3 != null && !rfc3.trim().equals("")){
			if(rfc3.length() < 3){
				codigoRespuesta = Errores.VERIFIQUE_RFC; //VERIFIQUE_RFC - VERIFIQUE FORMATO DE RFC
				return codigoRespuesta;
			}
		}

		aRFC   = Integer.valueOf(rfc2.substring(0, 2));
		mesRFC = Integer.valueOf(rfc2.substring(2, 4));
		diaRFC = Integer.valueOf(rfc2.substring(4, 6));

		if(mesRFC > 12){
			codigoRespuesta = Errores.VERIFIQUE_RFC2; //VERIFIQUE_RFC2 - VERIFIQUE FECHA DE RFC
			return codigoRespuesta;
		}


		if(calendar.isLeapYear(aRFC) && diaRFC > 29 && mesRFC == 2){
			codigoRespuesta = Errores.VERIFIQUE_RFC2; //VERIFIQUE_RFC2 - VERIFIQUE FECHA DE RFC
			return codigoRespuesta;
		}


		if (!calendar.isLeapYear(aRFC) && diaRFC > 28  && mesRFC == 2){
			codigoRespuesta = Errores.VERIFIQUE_RFC2; //VERIFIQUE_RFC2 - VERIFIQUE FECHA DE RFC
			return codigoRespuesta;
		}

		if((mesRFC == 4 || mesRFC == 6 || mesRFC == 9 || mesRFC == 11) && (diaRFC > 30)){
			codigoRespuesta = Errores.VERIFIQUE_RFC2; //VERIFIQUE_RFC2 - VERIFIQUE FECHA DE RFC
			return codigoRespuesta;
		}

		return codigoRespuesta;
	}
	
	public static boolean isTelefonoValido(String numeroTelefono) 
	{
		boolean hr;
	
		Pattern pattern = Pattern.compile("^\\d+$");         
        hr= pattern.matcher(numeroTelefono).matches(); 
		return hr;		
	}
	
	
	public static boolean isString( String validaCadena, Long tipoCadena ){
		boolean valido = true;
		Pattern pattern = null;

		if( validaCadena == null){
			valido = false; 
			return valido;
		}
		
		if( "".equals(validaCadena) ){
			valido = false;
			return valido;
		}
		
		if( tipoCadena == ConstantesGral.ALFANUMERICO ){
			pattern = Pattern.compile("\\d+|\\D+");         
			valido = pattern.matcher(validaCadena).matches(); 
		}
		
		if( tipoCadena == ConstantesGral.NUMERICO ){
			pattern = Pattern.compile("^\\d+$");         
			valido = pattern.matcher(validaCadena).matches(); 
		}
		
		return valido;
	}
	
	public static String leftPad(String val, int max, String caracter){
        String valor= val.toString().trim();
        int x=val.length();        
        for (x=valor.length(); x<max; x++){
            valor= caracter+valor;
        }
        if(x>max)
            valor = valor.toString().substring(0, max);        
        return valor;
    } 
	
	public static String leftPadr(String val, int max, String caracter){
        String valor= val.toString().trim();
        int x=val.length();        
        for (x=valor.length(); x<max; x++){
            valor= caracter+valor;
        }
        if(x>max)
            valor = valor.toString().substring(0, max);        
        return valor;
    }
	
	public static String BuscarValorJSON(String json, String llave )
	{
		String cad;
		String straux;
		int pos;

		try {
			straux= "";
			if ((json== null) || "".equals(json)) {
				return "";
			}

			pos= json.indexOf(String.format("\"%s\"", llave));

			if (pos >0 && pos<= json.length()) {
				pos = pos + String.format("\"%s\"", llave).length();
				cad= json.substring(pos);
				if (cad!= null && cad.length()> 0) {
					for (int i= 1; i < cad.length(); i++) {
						if ((",".equals(cad.substring(i, i+1))) || ("]".equals(cad.substring(i, i+1)))) {
							break;							
						}
						straux= straux + cad.substring(i, i+1);
					}
				}		
				straux = straux.replace(":", "").replace("]", "").replace("[", "").replace("\"", "");
				straux = straux.trim();
			}
		}
		catch (Exception ex) {
			straux= "";
		}
		return straux;
	}

	public static HashMap<String, String> ValoresJson(String sjson)  
	{
		HashMap<String, String> valores = new HashMap<String, String>();
		String tokens[];
		
		sjson = sjson.replace("{", "").replace("}", "");
		String bb[] = sjson.split(",");
		for (String p : bb) {
			p= p.replace("\"", "");
			tokens= p.split(":");
			valores.put(tokens[0].trim(), tokens[1].trim());					
		}
		return valores;
	}
	
	
	
}

