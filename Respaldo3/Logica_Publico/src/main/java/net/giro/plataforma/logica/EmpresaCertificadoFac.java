package net.giro.plataforma.logica;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.commons.ssl.Base64;
import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.EmpresaCertificado;
import net.giro.ne.dao.EmpresaCertificadoDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

@Stateless
public class EmpresaCertificadoFac implements EmpresaCertificadoRem {
	private static Logger log = Logger.getLogger(EmpresaCertificadoFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private EmpresaCertificadoDAO ifzEmpresaCertificado;
	private static String orderBy;
	
	public EmpresaCertificadoFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzEmpresaCertificado = (EmpresaCertificadoDAO) this.ctx.lookup("ejb:/Model_Publico//EmpresaCertificadoImp!net.giro.ne.dao.EmpresaCertificadoDAO");
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Publico.EmpresaCertificadoFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void orderBy(String orderBy) { EmpresaCertificadoFac.orderBy = orderBy; }

	@Override
	public Long save(EmpresaCertificado entity) throws ExcepConstraint {
		try {
			return this.ifzEmpresaCertificado.save(entity, null);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.save(EmpresaCertificado)", e);
			throw e;
		}
	}

	@Override
	public void update(EmpresaCertificado entity) throws ExcepConstraint {
		try {
			this.ifzEmpresaCertificado.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.update(EmpresaCertificado)", e);
			throw e;
		}
	}

	@Override
	public void delete(Long entityId) throws ExcepConstraint {
		try {
			this.ifzEmpresaCertificado.delete(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.delete(Long)", e);
			throw e;
		}
	}

	@Override
	public EmpresaCertificado findById(Long id) {
		try {
			return this.ifzEmpresaCertificado.findById(id);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findById(id)", e);
			throw e;
		}
	}

	@Override
	public EmpresaCertificado findByEmpresa(Long idEmpresa) throws Exception {

		try {
			return this.ifzEmpresaCertificado.findByEmpresa(idEmpresa);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findByEmpresa(Long idEmpresa)", e);
			throw e;
		}
	}

	@Override
	public EmpresaCertificado findByEmpresa(Empresa idEmpresa) throws Exception {
		try {
			return this.findByEmpresa(idEmpresa.getId());
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findByEmpresa(Empresa idEmpresa)", e);
			throw e;
		}
	}

	@Override
	public List<EmpresaCertificado> findAll() throws Exception {
		try {
			this.ifzEmpresaCertificado.orderBy(orderBy);
			return this.ifzEmpresaCertificado.findAll();
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findAll()", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpresaCertificado> findByProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzEmpresaCertificado.orderBy(orderBy);
			return this.ifzEmpresaCertificado.findByProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findByProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpresaCertificado> findLikeProperty(String propertyName, Object value, int limite) throws Exception {
		try {
			this.ifzEmpresaCertificado.orderBy(orderBy);
			return this.ifzEmpresaCertificado.findLikeProperty(propertyName, value, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findLikeProperty(propertyName, value, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpresaCertificado> findInProperty(String columnName, List<Object> values, int limite) throws Exception {
		try {
			this.ifzEmpresaCertificado.orderBy(orderBy);
			return this.ifzEmpresaCertificado.findInProperty(columnName, values, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findInProperty(columnName, values, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpresaCertificado> findByProperties(HashMap<String, Object> params, int limite) throws Exception {
		try {
			this.ifzEmpresaCertificado.orderBy(orderBy);
			return this.ifzEmpresaCertificado.findByProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findByProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public List<EmpresaCertificado> findLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		try {
			this.ifzEmpresaCertificado.orderBy(orderBy);
			return this.ifzEmpresaCertificado.findLikeProperties(params, limite);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findLikeProperties(params, limite)", e);
			throw e;
		} finally {
			orderBy = null;
		}
	}

	@Override
	public Respuesta guardarCertificado(EmpresaCertificado entity, String certificado64, String llave64) throws Exception {
		Respuesta respuesta = new Respuesta();
		byte[] bCertificado = null;
		byte[] bCertificadoPEM = null;
		byte[] bLlave = null;
		byte[] bLlavePEM = null;
		byte[] bLlaveENC = null;
		FileOutputStream fo = null;
		X509Certificate c = null;
		Runtime runtime = null;
		Process exec = null;
		String cmd = null;
		String certificadoId = "";
		String palabra = "";
		String noCertificado = "";
		String sCertificado = "";
		String sCertificadoPEM = "";
		String sLlave = "";
		String sLLavePEM = "";
		String sLLaveENC = "";
		
		try {
			palabra = entity.getPalabra();
			certificadoId = entity.getCertificado().substring(0, entity.getCertificado().lastIndexOf("."));
			sCertificado = "/tmp/" + certificadoId + ".cer";
			sLlave = "/tmp/" + certificadoId + ".key";
			sCertificadoPEM = "/tmp/" + certificadoId + ".cer.pem";
			sLLavePEM = "/tmp/" + certificadoId + ".key.pem";
			sLLaveENC = "/tmp/" + certificadoId + ".key.enc";
			certificadoId = entity.getCertificado();

			bCertificado = Base64.decodeBase64(certificado64);
			fo = new FileOutputStream(new File(sCertificado));
			fo.write(bCertificado);
			fo.close();

			bLlave = Base64.decodeBase64(llave64);
			fo = new FileOutputStream(new File(sLlave));
			fo.write(bLlave);
			fo.close();
			
			// Obtenemos el numero de certificado
			c = X509Certificate.getInstance(bCertificado);
			noCertificado = hex2decimal(c.getSerialNumber().toString(16));
			log.info("Obtengo en numero de certificado: " + noCertificado);
			
			// Generamos archivos PEM
			log.info("Generando archivos PEM ... ");
			runtime = Runtime.getRuntime();
			cmd = "openssl x509 -inform DER -outform PEM -in " + sCertificado + " -pubkey -out " + sCertificadoPEM;
			log.info(" ---> Generando Certificado ... \n" + cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();
			cmd = "openssl pkcs8 -inform DER -in " + sLlave + " -passin pass:" + palabra + " -out " + sLLavePEM;
			log.info(" ---> Generando Llave ... \n" + cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();
			cmd = "openssl rsa -in " + sLLavePEM + " -des3 -out " + sLLaveENC + " -passout pass:" + palabra;
			log.info(" ---> Generando Llave Encriptada ... \n" + cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();

			// Leyendo archivos
			log.info("Leyendo archivos PEM generados ... ");
			bCertificadoPEM = Files.readAllBytes(Paths.get(sCertificadoPEM));
			bLlavePEM = Files.readAllBytes(Paths.get(sLLavePEM));
			bLlaveENC = Files.readAllBytes(Paths.get(sLLaveENC));

			entity.setNoCertificado(noCertificado);
			entity.setCertificadoData(bCertificado);
			entity.setCertificadoDataPem(bCertificadoPEM);
			entity.setLlaveData(bLlave);
			entity.setLlaveDataPem(bLlavePEM);
			entity.setLlaveDataEnc(bLlaveENC);
			
			if (entity.getId() != null && entity.getId() > 0L)
				this.update(entity);
			else
				entity.setId(this.save(entity));

			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("EmpresaCertificado", entity);
		} catch (Exception e) {
			log.error(e);
			respuesta.getErrores().addCodigo("PUBLICO", -1);
			respuesta.getErrores().setCodigoError(-1);
			respuesta.getErrores().setDescError("Exception");
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta cargaCertificado(String certificadoId, String palabra, String certificado64, String llave64, String usuarioTimbre, String claveTimbre) throws Exception {
		Respuesta respuesta = new Respuesta();
		byte[] bCertificado = null;
		byte[] bCertificadoPEM = null;
		byte[] bLlave = null;
		byte[] bLlavePEM = null;
		byte[] bLlaveENC = null;
		FileOutputStream fo = null;
		X509Certificate c = null;
		Runtime runtime = null;
		Process exec = null;
		String cmd = null;
		String noCertificado = "";
		String sCertificado = "";
		String sCertificadoPEM = "";
		String sLlave = "";
		String sLLavePEM = "";
		String sLLaveENC = "";
		String extension = "";
		
		try {
			extension = certificadoId.substring(certificadoId.lastIndexOf("."));
			certificadoId = certificadoId.substring(0, certificadoId.lastIndexOf("."));
			
			sCertificado = "/tmp/" + certificadoId + ".cer";
			sLlave = "/tmp/" + certificadoId + ".key";
			sCertificadoPEM = "/tmp/" + certificadoId + ".cer.pem";
			sLLavePEM = "/tmp/" + certificadoId + ".key.pem";
			sLLaveENC = "/tmp/" + certificadoId + ".key.enc";
			certificadoId += extension;

			bCertificado = Base64.decodeBase64(certificado64);
			fo = new FileOutputStream(new File(sCertificado));
			fo.write(bCertificado);
			fo.close();

			bLlave = Base64.decodeBase64(llave64);
			fo = new FileOutputStream(new File(sLlave));
			fo.write(bLlave);
			fo.close();

			// ???
			//new PKCS8Key(bLlave, palabra.toCharArray());
			
			// Obtenemos el numero de certificado
			c = X509Certificate.getInstance(bCertificado);
			noCertificado = hex2decimal(c.getSerialNumber().toString(16));
			log.info("Obtengo en numero de certificado: " + noCertificado);
			
			// Generamos archivos PEM
			log.info("Generando archivos PEM ... ");
			runtime = Runtime.getRuntime();
			cmd = "openssl x509 -inform DER -outform PEM -in " + sCertificado + " -pubkey -out " + sCertificadoPEM;
			log.info(" ---> Generando Certificado ... \n" + cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();
			cmd = "openssl pkcs8 -inform DER -in " + sLlave + " -passin pass:" + palabra + " -out " + sLLavePEM;
			log.info(" ---> Generando Llave ... \n" + cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();
			cmd = "openssl rsa -in " + sLLavePEM + " -des3 -out " + sLLaveENC + " -passout pass:" + palabra;
			log.info(" ---> Generando Llave Encriptada ... \n" + cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();

			// Leyendo archivos
			log.info("Leyendo archivos PEM generados ... ");
			bCertificadoPEM = Files.readAllBytes(Paths.get(sCertificadoPEM));
			bLlavePEM = Files.readAllBytes(Paths.get(sLLavePEM));
			bLlaveENC = Files.readAllBytes(Paths.get(sLLaveENC));

			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("noCertificado", noCertificado);
			respuesta.getBody().addValor("certificadoData", bCertificado);
			respuesta.getBody().addValor("certificadoDataPem", bCertificadoPEM);
			respuesta.getBody().addValor("llaveData", bLlave);
			respuesta.getBody().addValor("llaveDataPem", bLlavePEM);
			respuesta.getBody().addValor("llaveDataEnc", bLlaveENC);
		} catch (FileNotFoundException e) {
			respuesta.getErrores().addCodigo("PUBLICO", -2);
			respuesta.getErrores().setCodigoError(-2);
			respuesta.getErrores().setDescError("FileNotFoundException");
			respuesta.setBody(null);
			log.error(e);
		} catch (IOException e) {
			respuesta.getErrores().addCodigo("PUBLICO", -3);
			respuesta.getErrores().setCodigoError(-3);
			respuesta.getErrores().setDescError("IOException");
			log.error(e);
		} catch (CertificateException e) {
			respuesta.getErrores().addCodigo("PUBLICO", -5);
			respuesta.getErrores().setCodigoError(-5);
			respuesta.getErrores().setDescError("CertificateException");
			log.error(e);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo("PUBLICO", -1);
			log.error(e);
		}
		
		return respuesta;
	}
	
	public String hex2decimal(String source) {
		String digits = "0123456789ABCDEF";
	    String resultado = "";
	    char caracter = Character.MIN_VALUE;
	    int digito = 0;
	    int valor = 0;

	    source = source.toUpperCase();
	    for (int index = 0; index < source.length(); ) {
	    	valor = 0;
			caracter = source.charAt(index++);
			digito = digits.indexOf(caracter);
			valor = 16 * digito;
			caracter = source.charAt(index++);
			digito = digits.indexOf(caracter);
			valor += digito;
			resultado = resultado + String.valueOf((char) valor);
	    }
	    
	    return resultado;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/07/2016 | Javier Tirado	| Creacion de EmpresaCertificadoFac