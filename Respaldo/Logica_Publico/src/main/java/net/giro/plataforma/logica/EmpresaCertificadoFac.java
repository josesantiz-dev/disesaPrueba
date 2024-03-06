package net.giro.plataforma.logica;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.commons.ssl.Base64;
//import org.apache.commons.ssl.PKCS8Key;
import org.apache.log4j.Logger;

import net.giro.comun.ExcepConstraint;
import net.giro.ne.beans.EmpresaCertificado;
import net.giro.ne.dao.EmpresaCertificadoDAO;
import net.giro.plataforma.InfoSesion;
import net.giro.publico.respuesta.Respuesta;

@Stateless
public class EmpresaCertificadoFac implements EmpresaCertificadoRem {
	private static Logger log = Logger.getLogger(EmpresaCertificadoFac.class);
	@SuppressWarnings("unused")
	private InfoSesion infoSesion;
	InitialContext ctx;

	private EmpresaCertificadoDAO ifzEmpresaCertificado;
	//private ConvertExt convertidor;
	private static String orderBy;
	//private Long estatus;
	
	public EmpresaCertificadoFac() {
		try{
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			this.ctx = new InitialContext(p);
			this.ifzEmpresaCertificado = (EmpresaCertificadoDAO) this.ctx.lookup("ejb:/Model_Publico//EmpresaCertificadoImp!net.giro.ne.dao.EmpresaCertificadoDAO");
			
			/*this.convertidor = new ConvertExt();
			this.convertidor.setFrom("EmpresaCertificadoFac");
			this.convertidor.setMostrarSystemOut(false);*/
		} catch(Exception e) {
			log.error("Error en el método contexto, no se pudo crear Logica_Publico.EmpresaCertificadoFac", e);
			ctx = null;
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) { this.infoSesion = infoSesion; }

	@Override
	public void showSystemOuts(boolean value) { /*this.convertidor.setMostrarSystemOut(value);*/ }

	@Override
	public void orderBy(String orderBy) { EmpresaCertificadoFac.orderBy = orderBy; }
	
	/*@Override
	public void estatus(Long estatus) { this.estatus = estatus; }*/

	@Override
	public Long save(EmpresaCertificado entity) throws ExcepConstraint {
		try {
			return this.ifzEmpresaCertificado.save(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.save(EmpresaCertificado)", e);
			throw e;
		}
	}

	/*@Override
	public Long save(EmpresaCertificadoExt entityExt) throws ExcepConstraint {
		try {
			return this.save(this.convertidor.EmpresaCertificadoExtToEmpresaCertificado(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.save(EmpresaCertificadoExt)", e);
			throw e;
		}
	}*/

	@Override
	public void update(EmpresaCertificado entity) throws ExcepConstraint {
		try {
			this.ifzEmpresaCertificado.update(entity);
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.update(EmpresaCertificado)", e);
			throw e;
		}
	}

	/*@Override
	public void update(EmpresaCertificadoExt entityExt) throws ExcepConstraint {
		try {
			this.update(this.convertidor.EmpresaCertificadoExtToEmpresaCertificado(entityExt));
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.update(EmpresaCertificadoExt)", e);
			throw e;
		}
	}*/

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

	/*@Override
	public EmpresaCertificadoExt findExtById(Long id) throws Exception {
		try {
			return this.convertidor.EmpresaCertificadoToEmpresaCertificadoExt(this.findById(id));
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findExtById(id)", e);
			throw e;
		}
	}*/

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

	/*@Override
	public List<EmpresaCertificadoExt> findExtAll() throws Exception {
		List<EmpresaCertificadoExt> listaExt = new ArrayList<EmpresaCertificadoExt>();
		
		try {
			List<EmpresaCertificado> lista = this.findAll();
			if (lista != null) {
				for(EmpresaCertificado var : lista) {
					listaExt.add(this.convertidor.EmpresaCertificadoToEmpresaCertificadoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findExtAll()", e);
			throw e;
		}
		
		return listaExt;
	}*/

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

	/*@Override
	public List<EmpresaCertificadoExt> findExtByProperty(String propertyName, Object value, int limite) throws Exception {
		List<EmpresaCertificadoExt> listaExt = new ArrayList<EmpresaCertificadoExt>();
		
		try {
			List<EmpresaCertificado> lista = this.findByProperty(propertyName, value, limite);
			if (lista != null) {
				for(EmpresaCertificado var : lista) {
					listaExt.add(this.convertidor.EmpresaCertificadoToEmpresaCertificadoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findExtByProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

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

	/*@Override
	public List<EmpresaCertificadoExt> findExtLikeProperty(String propertyName, Object value, int limite) throws Exception {
		List<EmpresaCertificadoExt> listaExt = new ArrayList<EmpresaCertificadoExt>();
		
		try {
			List<EmpresaCertificado> lista = this.findLikeProperty(propertyName, value, limite);
			if (lista != null) {
				for(EmpresaCertificado var : lista) {
					listaExt.add(this.convertidor.EmpresaCertificadoToEmpresaCertificadoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findExtLikeProperty(propertyName, value, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

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

	/*@Override
	public List<EmpresaCertificadoExt> findExtInProperty(String columnName, List<Object> values, int limite) throws Exception {
		List<EmpresaCertificadoExt> listaExt = new ArrayList<EmpresaCertificadoExt>();
		
		try {
			List<EmpresaCertificado> lista = this.findInProperty(columnName, values, limite);
			if (lista != null) {
				for(EmpresaCertificado var : lista) {
					listaExt.add(this.convertidor.EmpresaCertificadoToEmpresaCertificadoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findExtInProperty(columnName, values, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

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

	/*@Override
	public List<EmpresaCertificadoExt> findExtByProperties(HashMap<String, Object> params, int limite) throws Exception {
		List<EmpresaCertificadoExt> listaExt = new ArrayList<EmpresaCertificadoExt>();
		
		try {
			List<EmpresaCertificado> lista = this.findByProperties(params, limite);
			if (lista != null) {
				for(EmpresaCertificado var : lista) {
					listaExt.add(this.convertidor.EmpresaCertificadoToEmpresaCertificadoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findExtByProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

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

	
	/*@Override
	public List<EmpresaCertificadoExt> findExtLikeProperties(HashMap<String, String> params, int limite) throws Exception {
		List<EmpresaCertificadoExt> listaExt = new ArrayList<EmpresaCertificadoExt>();
		
		try {
			List<EmpresaCertificado> lista = this.findLikeProperties(params, limite);
			if (lista != null) {
				for(EmpresaCertificado var : lista) {
					listaExt.add(this.convertidor.EmpresaCertificadoToEmpresaCertificadoExt(var));
				}
			}
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.findExtLikeProperties(params, limite)", e);
			throw e;
		}
		
		return listaExt;
	}*/

	/*@Override
	public EmpresaCertificado cancelar(EmpresaCertificado entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.update(entity);
			
			return entity;
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.cancelar(entity)", e);
			throw e;
		}
	}*/

	/*@Override
	public void cancelar(EmpresaCertificadoExt entityExt) throws ExcepConstraint {
		try {
			entityExt.setEstatus(1);
			entityExt.setModificadoPor((int) this.infoSesion.getResponsabilidad().getUsuario().getId());
			entityExt.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.update(entityExt);
			
			return entityExt;
		} catch (Exception e) {
			log.error("error en Logica_Publico.EmpresaCertificadoFac.cancelar(entityExt)", e);
			throw e;
		}
	}*/
	
	@Override
	public Respuesta cargaCertificado(String certificadoId, String palabra, String certificado64, String llave64, String usuarioTimbre, String claveTimbre) throws Exception {
		Respuesta respuesta = new Respuesta();
		String cmd = null;
		Runtime runtime = null;
		Process exec = null;
		
		try {
			String sCertificado = "/tmp/" + certificadoId + ".cer";
			String sLlave = "/tmp/" + certificadoId + ".key";
			String sCertificadoPEM = "/tmp/" + certificadoId + ".cer.pem";
			String sLLavePEM = "/tmp/" + certificadoId + ".key.pem";
			String sLLaveENC = "/tmp/" + certificadoId + ".key.enc";

			byte[] bytesCertificado = Base64.decodeBase64(certificado64);
			FileOutputStream fo = null;
			fo = new FileOutputStream(new File(sCertificado));
			fo.write(bytesCertificado);
			fo.close();

			byte[] bytesLlave = Base64.decodeBase64(llave64);
			fo = new FileOutputStream(new File(sLlave));
			fo.write(bytesLlave);
			fo.close();

			//new PKCS8Key(bytesLlave, palabra.toCharArray());
			X509Certificate c = X509Certificate.getInstance(bytesCertificado);

			runtime = Runtime.getRuntime();
			cmd = "openssl pkcs8 -inform DER -in " + sLlave + " -passin pass:" + palabra + " -out "
			+ sLLavePEM;
			log.info(cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();

			cmd = "openssl x509 -inform DER -outform PEM -in " + sCertificado + " -pubkey -out " + sCertificadoPEM;
			log.info(cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();
			cmd = "openssl rsa -in " + sLLavePEM + " -des3 -out " + sLLaveENC
			+ " -passout pass:" + claveTimbre;
			log.info(cmd);
			exec = runtime.exec(cmd);
			exec.waitFor();

			byte[] bytesCertificadoPEM = (new String(Files.readAllBytes(Paths.get(sCertificadoPEM)))).getBytes();
			byte[] bytesLlavePEM = (new String(Files.readAllBytes(Paths.get(sLLavePEM)))).getBytes();
			byte[] bytesLlaveENC = (new String(Files.readAllBytes(Paths.get(sLLaveENC)))).getBytes();

			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("noCertificado", hex2decimal(c.getSerialNumber().toString(16)));
			respuesta.getBody().addValor("certificadoData", bytesCertificado);
			respuesta.getBody().addValor("certificadoDataPem", bytesCertificadoPEM);
			respuesta.getBody().addValor("llaveData", bytesLlave);
			respuesta.getBody().addValor("llaveDataPem", bytesLlavePEM);
			respuesta.getBody().addValor("llaveDataEnc", bytesLlaveENC);
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
		/*} catch (GeneralSecurityException e) {
			respuesta.getErrores().addCodigo("PUBLICO", -4);
			respuesta.getErrores().setCodigoError(-4);
			respuesta.getErrores().setDescError("GeneralSecurityException");
			log.error(e);*/
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
	
	public String hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
	    s = s.toUpperCase();
	    int val = 0;
	    String res = "";
	    
	    for (int i = 0; i < s.length(); ) {
	    	val = 0;
			char c = s.charAt(i++);
			int d = digits.indexOf(c);
			val = 16 * d;
			c = s.charAt(i++);
			d = digits.indexOf(c);
			val += d;
			res = res + String.valueOf((char)val);
	    }
	    return res;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN	|    FECHA 	 | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 01/07/2016 | Javier Tirado	| Creacion de EmpresaCertificadoFac