package net.giro.cargas.documentos.plataforma;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;


import net.giro.cargas.documentos.plataforma.Utilerias;
import net.giro.plataforma.beans.ConValores;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
 


public abstract class Utilerias {
	private static Logger log = Logger.getLogger(Utilerias.class);
	
	public static String expRegSucursal = "(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)";
	public static String expRegIdDato = "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)";
	public static String expRegNumReal = "^\\d+.?\\d*$";
	public static String expRegNumRealObligatorio = "^\\d+.?\\d*$";
	public static String expRegNumEntero = "^\\d*$";
	public static String expRegNumEnteroObligatorio = "^\\d+$";
	
	public static String Encripta(String cadena) {
		String res = null;
		StringBuffer hexString = new StringBuffer();
		MessageDigest digest = null;
		try {
			digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(cadena.getBytes());
			byte[] hash = digest.digest();
			for (int i = 0; i < hash.length; i++) {
				hexString.append(Integer.toHexString(0xFF & hash[i]));
			}
			res = hexString.toString();
		} catch (Exception re) {
			re.printStackTrace();
		}
		return res;
	}

	public static String remplazaCadena(String fuente, String cad_a_quitar, String remplazar_con) {
		cad_a_quitar = daFormatToExpReg(cad_a_quitar);
		Pattern Patt = Pattern.compile(cad_a_quitar);
		Matcher matcher = Patt.matcher(fuente);
		String s = matcher.replaceAll(remplazar_con);
		return s;
	}
	
	public static String daFormatToExpReg(String s){
		StringBuffer sb = new StringBuffer();
		char []arry = new char[s.length()];
		arry = s.toCharArray();
		
		for(int i=0; i < arry.length; i++){
			if(arry[i]=='.' || arry[i]=='^' || arry[i]=='*' || arry[i]=='+' || arry[i]=='?' || arry[i]=='[' || arry[i]==']'){
				sb.append("\\");
				sb.append("\\");
			}
			sb.append(arry[i]);
		}
		return sb.toString();
	}
	
	public static String getPagina(String deUrl, String ext){
		String res = null;
		Pattern patron = Pattern.compile("/*[a-zA-Z1-9_]+\\.");
		Matcher m = patron.matcher(deUrl);
		if(m.find()){
			res = remplazaCadena(m.group(), "/", "") + ext;
		}

		return res!=null?res:"";
	}
	
	public static String getExtension(String str){
		String ext = "";
		try{
			Pattern patron = Pattern.compile("\\.[\\D]*");
			Matcher m = patron.matcher(str);
			if(m.find())
				ext = m.group();
		}catch(Exception ex){
			ocurrioError(ex.getMessage());
		}
		return ext;
	}
	/*****
	 * @return devuelve verdadero si se copio los datos de un archivo(ruta) a una ruta en especifico y falso en caso contrario
	 * **/
	public static boolean copiarArchivo(String origen, String destino){
		File fd, fileTMP = null;
		FileOutputStream fo = null;
		InputStream leeFile = null;
		byte[] datos = null;
		try{
			fileTMP = new File(origen);
			if(fileTMP.exists()){
				leeFile = new FileInputStream(fileTMP);
				datos = new byte[(int)fileTMP.length()];
				leeFile.read(datos);
				leeFile.close();
			}
			
			fd = new File(destino);
			if(fd.exists()){
				fd.delete();
			}
			fo = new FileOutputStream(fd);
			fo.write(datos);
			fo.flush();
			fo.close();
		}catch(Exception re){System.out.println(re.getMessage()); return false;}
		return true;
	}
	
	public static void ocurrioError(String str){
		try{
			str = String.valueOf(Calendar.DATE) + String.valueOf(Calendar.MONTH) + String.valueOf(Calendar.YEAR) + str + "\r\n"; 
			File f = new File("c:/errores.txt");
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(str.getBytes());
			fo.flush();
			fo.close();
		}catch(Exception re){re.printStackTrace();}
	}
	
	public static boolean esNumero(String num){
		if("".equals(num))
			return false;
	
		if(num.indexOf(".")>-1 && num.indexOf(".") != num.lastIndexOf(".") )
			return false;
		else
			num = num.replace(".", "");
		
		if(num.indexOf("-")>-1 && num.indexOf("-") != num.lastIndexOf("-") || num.indexOf("-") > 0)
			return false;
		else
			num = num.replace("-", "");
		
		for(int i=0;i < 10; i++){
			num = num.replace(String.valueOf(i), "");
		}
		return "".equals(num);
	}
	
	public static boolean esFecha(String str){
		Pattern patron = Pattern.compile("^[0-9]{2}[/-]{1}[0-9]{2}[/-]{1}[0-9]{4}");
		Matcher m = patron.matcher(str);
		return m.find();
	}
	
	public static String getNombreCompu(){
		String nombre="_error";
		try{
		      nombre=InetAddress.getLocalHost().getHostName();
		    }catch (Exception e){
		      System.out.println(e.getMessage());
		    }
		    return nombre;
	}
	
	public static String getIpCompu(){
		String ip="_error";
		try{
		      ip=InetAddress.getLocalHost().getHostAddress();
		    }catch (Exception e){
		      System.out.println(e.getMessage());
		    }
		    return ip;
	}
	
	public static double redondear(double nD, int nDec)
	{
	  return Math.round(nD*Math.pow(10,nDec))/Math.pow(10,nDec);
	}

	
	/**
	 * 
	 * @param parametros String con los parametros separados por el delimitador
	 * @param campoBuscar nombre parametro del que se espera obtener el valor ubicado en la String de parametros 
	 * @param delimit String que delimitara el termino del valor del parametro
	 * @return String vacia si no encontro el valor, de lo contrario el valor del parametro encontrado.
	 */
	public static String getValorParametro(String parametros, String campoBuscar, String delimit){
		String res = "";
		//checamos si el delimitador es un metacaracter de las exp. regulares y si lo es le asignamos \\ para que no truene la busqueda
		if("([{\\^-$|]})?*+.EQ ".indexOf(delimit) < 0)
			delimit = "\\" + delimit;
		Pattern p = Pattern.compile("[\\w\\s\\d]*" + campoBuscar + "={1}([\\w\\s\\d]*)" + delimit + "[\\w\\s\\d]*");
		Matcher m = p.matcher(parametros);
		
		if(m.find())
			res = m.group(1);
		
		return res;
	}
	
	/**
	 * 
	 * @param fecInicio primera fecha
	 * @param fecFinal segunda fecha
	 * @return numero de dias de diferencia entre las fechas
	 */
	public static int getDifDiasFechas(Date fecInicio, Date fecFinal){
		Long tmp = 0L;
		long val = 0;
		
		val = 1000 * 60 * 60 * 24;

		tmp = (fecFinal.getTime() / val) - (fecInicio.getTime() / val); 
		
		return tmp.intValue();
	}
	
	/**
	 * 
	 * @param datos flujo del archivo a obtener los renglones
	 * @return lista con los renglones del archivo
	 */
	public static List<String> getLineasFromFlujo(byte[] datos){
		List<String> res = new ArrayList<String>();
		String strLinea = "";
		for(int idxLectura = 0;idxLectura < datos.length; idxLectura++){
			if(datos[idxLectura] == 13){
				if(datos.length - 1 >= idxLectura && datos[idxLectura + 1] == 10)
					idxLectura++;
				res.add(strLinea);
				strLinea = "";
				continue;
			}else if(datos[idxLectura] == 10){
				res.add(strLinea);
				strLinea = "";
				continue;
			}else if(idxLectura >= datos.length - 1){
				res.add(strLinea);
				strLinea = "";
				continue;
			}
			strLinea += String.valueOf(datos[idxLectura] < 0  ? ' ' : (char)datos[idxLectura]);
		}		
		return res;
	}
	
	/**
	 * 
	 * @param datos texto a obtener los renglones
	 * @return lista con los renglones del texto
	 */
	public static List<String> getLineasFromTexto(String datos){
		List<String> res = new ArrayList<String>();
		String strLinea = "";
		for(int idxLectura = 0; idxLectura < datos.length(); idxLectura++){
			if(datos.charAt(idxLectura) == 13){
				if(datos.length() - 1 >= idxLectura && datos.charAt(idxLectura + 1) == 10)
					idxLectura++;
				res.add(strLinea);
				strLinea = "";
				continue;
			}else if(datos.charAt(idxLectura) == 10){
				res.add(strLinea);
				strLinea = "";
				continue;
			}else if(idxLectura >= datos.length() - 1){
				res.add(strLinea);
				strLinea = "";
				continue;
			}
			strLinea += String.valueOf(datos.charAt(idxLectura) < 0  ? ' ' : datos.charAt(idxLectura));
		}		
		return res;
	}
	/**
	 * 
	 * @param numNivel numero de nivel a sacar los subniveles
	 * @return cadena con los niveles concatenados por comas involucrados en numNivel
	 */
	public static String getNiveles(int numNivel){
		String tmp = "";
		String tmp2 = "";
		String binStr = Integer.toBinaryString(numNivel);
		
		for(int i = 0; i < binStr.length(); i++)
			tmp2 = binStr.substring(i, i+1) + tmp2;
		
		binStr = tmp2;
		
		for(int i = 0 ; i < binStr.length() ; i++){
			if("1".equals(binStr.substring(i, i+1)))
				tmp += i == 0 ? "1" : ("".equals(tmp) ? Double.valueOf(Math.pow(2,i)).intValue() :  "," + Double.valueOf(Math.pow(2,i)).intValue());  
		}
		
		return tmp;
	}
	/**
	 * 
	 * @param str cadena origen
	 * @return cadena sin espacios a los lados y con solo 1 espacio entre palabras
	 */
	public static String eliminaEspacios(String str){
		if(str==null) return null;
		
		Pattern p = Pattern.compile("^(\\s+)");
		Matcher m = p.matcher(str);
		String res = "";
		try {
			if(m.find())
				str = str.substring(m.group(1).length());
			p = Pattern.compile("(\\s+$)");
			m = p.matcher(str);
			if(m.find())
				str = str.substring(0, str.length() - m.group(1).length());
			
			for(String s:str.split(" ")){
				if("".equals(s))continue;
				res += "".equals(res) ? s.trim() : " " + s.trim();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			res = null;
		}
		return res;
	}
	
	public static String concatenaParametros(String[][] params){
		String res = null;
		try {
			
		} catch (Exception e) {
			log.error("Error en concatenaParametros", e);
		}
		return res;
	}
	
	/**
	 * 
	 * @param id valor que se busca
	 * @param lista universo en donde buscar el valor
	 * @return objeto conValor en caso de encontrarlo y null en caso contrario
	 */
	public static ConValores getValorById(long id, List<ConValores> lista){
		for(ConValores c : lista){
			if(c.getId() == id)
				return c;
		}
		return null;
	}
	
	
	public static String ejecutaProcesoBonita(String nombreProceso, Class<?> claseInvoca, HashMap<String, String> params) throws Exception{
		Properties prop = new Properties();
        InputStream in = claseInvoca.getResourceAsStream("/cde/plataforma/Entorno.properties");
    	prop.load(in);
    	params.put("proceso", nombreProceso);
    	return Utilerias.procesaRequest(prop.getProperty("bonitaServer"), Integer.valueOf(prop.getProperty("bonitaPort")), "/bos-web/procesa", params);
	}
	
	public static String procesaRequest(String server, int puerto, String ruta, HashMap<String, String> paramsBase) throws Exception{
		String url = "http://" + server + ":" + puerto + ruta;
		String resultado = null;
		   try {
		    HttpClient client = new HttpClient();
		    PostMethod method = new PostMethod( url );

			 // Configure the form parameters
		    for(Map.Entry<String, String> entry : paramsBase.entrySet())
		    	method.addParameter( entry.getKey(), entry.getValue() );
		    
			 // Execute the POST method
		    int statusCode = client.executeMethod( method );
		    if( statusCode != -1 ) {
		      resultado = method.getResponseBodyAsString();
		      method.releaseConnection();
		    }
		   }
		   catch( Exception e ) {
			   log.error("Error en metodo procesaRequest", e);
			   resultado = "error";
		   }
		   return resultado;
	}
	
	/**
	 * permite obtener el valor antiguo y el valor modificado de las propiedades modificadas en un Entity 
	 * @param viejoObj instancia inicial 'sin modificar'
	 * @param nuevoObj instancia modificada
	 * @return HashMap key: propiedad modificada, value: arreglo de strings que contiene el campo original y campo modificado
	 * @throws Exception
	 */
	public static HashMap<String, String[]> auditaEnties(Object viejoObj, Object nuevoObj) throws Exception{
		Object nuevoValor=null;
		Object viejoValor=null;
		Object nuevoValorJoin=null;
		Object viejoValorJoin=null;
		String[] valores = null;
		String nombrePropiedad = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		
		HashMap<String, String[]> resultado = new HashMap<String, String[]>();
		
		if(viejoObj.getClass() != nuevoObj.getClass())
			throw new Exception("No es posible auditar clases difentes!");
		
		
		for(Method m : nuevoObj.getClass().getMethods() ) {
			if(m.isAnnotationPresent(Id.class))
				continue;
			
			if(m.isAnnotationPresent(JoinColumn.class) || m.isAnnotationPresent(Column.class)){
				valores = new String[2] ;
				nuevoValor =  m.invoke(nuevoObj,new Object[]{});
				viejoValor =  m.invoke(viejoObj,new Object[]{});
				//obtengo el nombre de la propiedad, diferenciando entre un metodo que devuelve boolean 'is'
				nombrePropiedad = m.getName().substring(m.getName().indexOf("get") == 0 ? 3 : 2, m.getName().length());
			}
			
			if (m.isAnnotationPresent(Column.class)){
				if(nuevoValor.getClass() == Date.class){
					valores[0] = nuevoValor == null ? "" : sdf.format((Date)nuevoValor);
					valores[1] = viejoValor == null ? "" : sdf.format((Date)viejoValor);
				}else{
					valores[0] = nuevoValor == null ? "" : nuevoValor.toString();
					valores[1] = viejoValor == null ? "" : viejoValor.toString();
				}
				
				if(!nuevoValor.equals(viejoValor))
					resultado.put(nombrePropiedad, valores);
			}else if (m.isAnnotationPresent(JoinColumn.class)){
				nuevoValorJoin = null;
				viejoValorJoin = null;
				
				if(nuevoValor != null){
					try {
						for(Method m2 : nuevoValor.getClass().getMethods()){
							if(m2.isAnnotationPresent(Id.class)){
								nuevoValorJoin =  m2.invoke(nuevoValor,new Object[]{});
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						nuevoValorJoin = null;
					}
				}
				
				if(viejoValor != null){
					try {
						for(Method m2 : viejoValor.getClass().getMethods()){
							if(m2.isAnnotationPresent(Id.class)){
								viejoValorJoin =  m2.invoke(viejoValor,new Object[]{});
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						viejoValorJoin = null;
					}
				}
				
				if(nuevoValorJoin != null && viejoValorJoin != null && !nuevoValorJoin.toString().equals(viejoValorJoin.toString()   ) 
						|| ((nuevoValorJoin != null && viejoValorJoin == null) || (nuevoValorJoin == null && viejoValorJoin != null) ) ) {
					valores[0] = nuevoValorJoin == null ? "" : nuevoValorJoin.toString();
					valores[1] = viejoValorJoin == null ? "" : viejoValorJoin.toString();
					resultado.put(nombrePropiedad, valores);
				}
			}
		}
		
		return resultado;
	}
}