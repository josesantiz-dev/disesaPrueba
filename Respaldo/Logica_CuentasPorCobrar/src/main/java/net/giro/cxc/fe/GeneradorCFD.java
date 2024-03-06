package net.giro.cxc.fe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException; 
import java.security.PrivateKey; 
import java.security.Signature;

import org.apache.commons.ssl.Base64;
import org.apache.commons.ssl.PKCS8Key;
import org.jdom.Document;
import org.jdom.output.Format; 
import org.jdom.output.XMLOutputter; 
import org.jdom.output.Format.TextMode; 

/** 
* Clase que genera el comprobante fiscal digital asi como el sello digital 
* @author Xgress 
* 
*/ 
public class GeneradorCFD { 
	/** 
	* Constructor que inicializa el objeto con un Comprobante, genera el 
	* selloDigital y se lo agrega al objeto Comprobante 
	* 
	* @param comprobante 
	* Objeto que contiene todos los datos de un Comprobante Fiscal 
	* Digital 
	* @param archivoClavePrivada 
	* InputStream que contiene el archivo .key de la llave privada 
	* @param password 
	* String con el password de la llave privada 
	* @throws GeneralSecurityException 
	* Excepcion arrogada cuando el password no es el correcto 
	*/ 
	//private String sello; 
	private String XML = null;
	private String cadenaOriginal = null;
	private String selloDigital = null;
	private Document document = null;
	
	
	public GeneradorCFD(Document document, InputStream archivoClavePrivada, String password) throws GeneralSecurityException { 
		this.document = document; 
		setSelloDigital(archivoClavePrivada, password); 
	} 
	
	
	public Document getDocument() {
		return document;
	}
	
	public void setDocument(Document document) {
		this.document = document;
	}
	 
	/** 
	* Metodo que genera el sello digital a partir de la cadena original y la 
	* clave privada siguiendo los siguientes pasos: 1)Se le aplica la funcion 
	* hash de digestion MD5 a la cadena original y se aplica el algoritmo de 
	* encripcion RSA con la clave privada. 2) Se codifica el resultado a Base64 
	* teniendo como resultado una cadena imprimible. 
	* 
	* @param archivoClavePrivada 
	* InputStream que contiene la clave privada(archivo .key) 
	* @param password 
	* String con el password de la clave privada 
	* @return String con el sello digital 
	* @throws GeneralSecurityException 
	* Excepcion arrogada cuando el password no es el correcto 
	*/ 
	public String getSelloDigital() {
		return selloDigital; 
	}
	
	public void setSelloDigital(InputStream archivoClavePrivada, String password) throws GeneralSecurityException { 
		try {
			// Generamos CADENA ORIGINAL
			String cadenaOriginal = getCadenaOriginal(); 
			System.out.println(cadenaOriginal);

			// Generamos SELLO DIGITAL
			byte[] clavePrivada = getBytes(archivoClavePrivada); 
			PKCS8Key pkcs8 = new PKCS8Key(clavePrivada, password.toCharArray()); 
			PrivateKey pk = pkcs8.getPrivateKey(); 
			Signature firma = Signature.getInstance("SHA1withRSA"); 
			
			firma.initSign(pk);
			firma.update(cadenaOriginal.getBytes("UTF-8"));
			
			this.selloDigital = new String(Base64.encodeBase64(firma.sign())); 
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace(); 
		}
	} 

	public String getCadenaOriginal() {
		if (cadenaOriginal == null) {
			try {
				String xsltFileName = "  ";
				InputStream is = GeneradorCFD.class.getResourceAsStream("cadenaoriginal_3_2.xslt"); //"C:\\tmp\\cadenaoriginal_3_2.xslt";

				if (xsltFileName == null || "".equals(xsltFileName))
					xsltFileName = "/tmp/cadenaoriginal_3_2.xslt";
				else
					xsltFileName = getStringFromInputStream(is);
				
				StringWriter out = new StringWriter();
				Util.ProcesarXSLT(getXML(), xsltFileName, out);
				out.close();
				
				this.cadenaOriginal = out.toString();
			} catch(Exception e) {
				e.printStackTrace();
			}  
		}

		return cadenaOriginal;
	}
	
	/** 
	* Genera el xml extensible del comprobante fiscal digital 
	* @return String con el xml 
	 * @throws Exception 
	*/ 
	public String getCFD() throws Exception { 
		Format format = Format.getPrettyFormat(); 
		format.setEncoding("UTF-8"); 
		format.setTextMode(TextMode.NORMALIZE); 
		XMLOutputter xmlOutputer = new XMLOutputter(format); 
		String res = xmlOutputer.outputString(getDocument()); 

		try { 
			res = new String(res.getBytes("UTF-8")); 
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace(); 
		} 

		return res; 
	} 

	public String getXML() {
		// escribir el xml
		if (XML == null) {
			try { 
				XMLOutputter outputter = new XMLOutputter();
				StringWriter writer = new StringWriter();
				outputter.output(document, writer);
				writer.close();
				XML = writer.toString();
			} catch(Exception e) { }
		}

		return XML;
	}
	
	private static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}
	
	/** 
	* Metodo que convierte un input stream con la llave privada a un array de bytes 
	* @param is InputSteam con la clave privada 
	* @return Arreglo de bytes con la clave privada 
	*/ 
	private byte[] getBytes(InputStream is) { 
		int totalBytes = 714; 
		byte[] buffer = null; 

		try { 
			//totalBytes = is.r.available();
			buffer = new byte[totalBytes]; 
			is.read(buffer, 0, totalBytes); 
			is.close(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		return buffer; 
	} 
}