package net.giro.cxc.fe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException; 
import java.security.KeyFactory;
import java.security.PrivateKey; 
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.ssl.Base64;
import org.apache.commons.ssl.PKCS8Key;
import org.apache.log4j.Logger;
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
	private static Logger log = Logger.getLogger(GeneradorCFD.class.getName());
	//private String sello; 
	private String version = "";
	private Document document = null;
	private String cadenaOriginal = null;
	private String selloDigital = null;
	private String XML = null;
	private String cfd = null;
	
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
	public GeneradorCFD(Document document, InputStream archivoClavePrivada, String password, String version) throws GeneralSecurityException {
		this.version = version;
		this.document = document;

		generaSelloDigital(archivoClavePrivada, password); 
	} 
	
	
	public Document getDocument() {
		return this.document;
	}
	
	public void setDocument(Document document) {
		this.document = document;
	}
	
	public String getSelloDigital() {
		return this.selloDigital; 
	}
	
	public String getCadenaOriginal() {
		return this.cadenaOriginal;
	}
	
	public String getXML() {
		if (this.XML == null || "".equals(this.XML))
			generaXML();
		return this.XML;
	}
	
	public String getCFD() {
		if (this.cfd == null || "".equals(this.cfd))
			generaCFD();
		return this.cfd;
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
	private void generaSelloDigital(InputStream archivoClavePrivada, String password) throws GeneralSecurityException { 
		PKCS8EncodedKeySpec keySpec = null;
		PKCS8Key pkcs8 = null;
		PrivateKey pk = null;
		Signature firma = null;
		byte[] clavePrivada = null;
		
		try {
			// Generamos CADENA ORIGINAL
			generaCadenaOriginal(); 

			// Generamos SELLO DIGITAL
			log.info("Generando Sello Digital ... " + (("3.3".equals(this.version)) ? "SHA256withRSA" : "SHA1withRSA"));
			pkcs8 = new PKCS8Key(archivoClavePrivada, password.toCharArray());
			clavePrivada = pkcs8.getDecryptedBytes();
			keySpec = new PKCS8EncodedKeySpec(clavePrivada);
			pk = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
			
			/*clavePrivada = getBytes(archivoClavePrivada); 
			pkcs8 = new PKCS8Key(clavePrivada, password.toCharArray()); 
			pk = pkcs8.getPrivateKey();*/
			
			if ("3.3".equals(this.version))
				firma = Signature.getInstance("SHA256withRSA"); 
			else
				firma = Signature.getInstance("SHA1withRSA"); 
			firma.initSign(pk);
			firma.update(this.cadenaOriginal.getBytes());
			
			this.selloDigital = new String(Base64.encodeBase64(firma.sign())); 
			log.info("Sello Digital generado:\n" + this.selloDigital);
		} catch (Exception e) { 
			log.error("Ocurrio un problema al generar el Sello Digital del CFDI", e);
		}
	} 

	/**
	 * Metodo que genera la Cadena Original
	 */
	private void generaCadenaOriginal() {
		InputStream is = null;
		StringWriter out = null;
		String xsltFileName = "";

		try {
			if (this.cadenaOriginal == null || "".equals(this.cadenaOriginal.trim())) {
				log.info("Cargando esquema Cadena Original para v" + this.version + " ... ");
				if ("3.3".equals(this.version)) 
					is = GeneradorCFD.class.getResourceAsStream("cadenaoriginal_3_3.xslt"); 
				else
					is = GeneradorCFD.class.getResourceAsStream("cadenaoriginal_3_2.xslt"); 
				xsltFileName = getStringFromInputStream(is);
				
				log.info("Procesando esquema v" + this.version + " ... ");
				out = new StringWriter();
				Util.ProcesarXSLT(getXML(), xsltFileName, out);
				out.close();
				
				this.cadenaOriginal = out.toString();
				log.info("Cadena Original:\n" + this.cadenaOriginal);
			}
		} catch(Exception e) {
			log.error("Ocurrio un problema al intentar generar la Cadena Original del CFDI v" + this.version, e);
		}  
	}
	
	/** 
	* Genera el xml extensible del comprobante fiscal digital 
	* @return String con el xml 
	 * @throws Exception 
	*/ 
	private void generaCFD() { 
		XMLOutputter xmlOutputer = null; 
		Format format = null; 
		String resultado = ""; 

		try { 
			format = Format.getPrettyFormat(); 
			format.setEncoding("UTF-8"); 
			format.setTextMode(TextMode.NORMALIZE); 
			xmlOutputer = new XMLOutputter(format); 
			resultado = xmlOutputer.outputString(getDocument()); 
			resultado = new String(resultado.getBytes("UTF-8")); 
			this.cfd = resultado;
		} catch (UnsupportedEncodingException e) { 
			log.error("Ocurrio un problema al intentar generar el CFD", e); 
		} 
	} 

	private void generaXML() {
		XMLOutputter outputter = null;
		StringWriter writer = null;
		
		try { 
			if (this.XML == null) {
				outputter = new XMLOutputter();
				writer = new StringWriter();
				outputter.output(this.document, writer);
				writer.close();
				this.XML = writer.toString();
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar convertir el Document al XML", e);
		}
	}
	
	/**
	 * Conviente un InputStream a String
	 * @param is InputStream
	 * @return String
	 */
	private String getStringFromInputStream(InputStream is) {
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
}