package net.giro.cxc.fe;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.naming.InitialContext;
import javax.security.cert.X509Certificate;
import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.beans.FacturaDetalleImpuestoExt;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.fe.v33.Comprobante;
import net.giro.cxc.fe.v33.complemento.pagos.Pagos;
import net.giro.cxc.logica.FacturaDetalleImpuestoRem;
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.respuesta.Respuesta;

import org.apache.commons.ssl.Base64;
import org.apache.commons.ssl.PKCS8Key;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.DOMBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Node;

public class FactElectv33 {
	private static Logger log = Logger.getLogger(FactElectv33.class.getName());
	private HashMap<String, Object> params = new HashMap<String, Object>();
	private InitialContext ctx = null;
	private DataSource datasource = null;
	private Connection conn;
	private FacturaRem ifzFactura;
	private FacturaDetalleRem ifzDetalles;
	private FacturaDetalleImpuestoRem ifzImpuestos;
	private TimeZone timeZone;
	private Long errorCodigo;
	private String errorDescripcion;
	private boolean debugging;
	private boolean testing;
	private String digestMethod;
	private String soapRequestVersion;

	
	public FactElectv33() {
		try {
			this.ctx = new InitialContext();
			this.ifzFactura = (FacturaRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzDetalles = (FacturaDetalleRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzImpuestos = (FacturaDetalleImpuestoRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleImpuestoFac!net.giro.cxc.logica.FacturaDetalleImpuestoRem");
			this.testing = false;
			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
			setDigestMethodSHA1();
			setSoapRequestVersion11();
		} catch (Exception e) {
			log.error("Ocurrio un problema al instancias FactElect", e);
		}
	}


	public boolean timbrar(long idFactura, long usuarioId) throws Exception {
		try {
			return timbrar(this.ifzFactura.findById(idFactura), usuarioId);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar Timbrar la Factura", e);
			throw e;
		}
	}

	public boolean timbrar(Factura pojoFactura, long usuarioId) throws Exception {
		try {
			return timbrar(this.ifzFactura.convertir(pojoFactura), usuarioId);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar Timbrar la Factura", e);
			throw e;
		}
	}

	public boolean timbrar(FacturaExt pojoFactura, long usuarioId) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<FacturaDetalleExt> listDetalles = null;
		ResultSet rsCertificados = null;
		InputStream clavePrivada = null;
		String strQuery = "";
		String password = ""; 
		String fileName = "";
		String timbreUser = "";
		String timbrePass = "";
		// XML
		Element root = null;
		
		try {
			// Inicializaciones
			this.conn = getConn();
			if (this.params == null)
				this.params = new HashMap<String, Object>();
			this.params.clear();
			
			// Validaciones 100
			if (! validaciones(pojoFactura))
				return false;
			
			// Asignacion de Serie y Folio si corresponde 200
			if (! asignarSerieFolio(pojoFactura)) 
				return false;
			
			// Certificado
			strQuery = "SELECT no_certificado, certificado_data, certificado_data_pem"
						+ ", llave_data, llave_data_pem, llave_data_enc"
						+ ", palabra, usuario_timbre, clave_timbre "
					+ "FROM empresa_certificado "
					+ "WHERE (estatus IS NULL OR estatus='A') AND id_empresa = " + pojoFactura.getIdEmpresa().getId();
			rsCertificados = this.conn.createStatement().executeQuery(strQuery); 
			if (! rsCertificados.next()) {
				error(301L, "No se pudo recuperar el Certificado cargado a la Empresa");
				return false;
			}
			
			listDetalles = this.ifzDetalles.findExtByFactura(pojoFactura);
			if (listDetalles == null || listDetalles.isEmpty()) {
				error(302L, "No se pudo recuperar los detalles de la Factura (Conceptos)");
				return false;
			}

			this.params.put("nocertificado", rsCertificados.getString("no_certificado"));

			fileName = pojoFactura.getId() + "_" + pojoFactura.getFolioFactura();
			timbreUser = rsCertificados.getString("usuario_timbre"); 
			timbrePass = rsCertificados.getString("clave_timbre"); 
			clavePrivada = rsCertificados.getBinaryStream("llave_data");
			password = rsCertificados.getString("palabra"); 
			
			// Generamos XML
			root = generaXML(pojoFactura, listDetalles, rsCertificados.getString("no_certificado").trim(), new String(Base64.encodeBase64(rsCertificados.getBytes("certificado_data"))));
			if (root == null) {
				error(303L, "No se pudo general el XML (CFDI)");
				return false;
			}
			
			// Generamos CFDI 400
			if (! generarCFDI(root, clavePrivada, password, fileName, timbreUser, timbrePass))
				return false;
			
			pojoFactura.setXml(this.params.get("xml").toString().getBytes());
			pojoFactura.setCadenaOriginal(this.params.get("cadena_original").toString().getBytes());
			pojoFactura.setSello(this.params.get("sello").toString().getBytes());
			pojoFactura.setState(Double.parseDouble(this.params.get("state").toString()));
			pojoFactura.setDescripcion(this.params.get("descripcion").toString());
			pojoFactura.setCfdi(this.params.get("cfdi").toString().getBytes());
			pojoFactura.setTimbre(this.params.get("timbre").toString().getBytes());
			pojoFactura.setFechaTimbrado(formateador.parse(this.params.get("fecha_timbrado").toString()));
			pojoFactura.setUuid(this.params.get("uuid").toString());
			pojoFactura.setSelloSat(this.params.get("sello_sat").toString());
			pojoFactura.setNoCertificadoSat(this.params.get("no_certificado_sat").toString());
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar el comprobante para la Factura", e);
			error(300L, "No se pudo generar el Comprobante para la Factura");
			return false;
		} finally {
			actualizamosFactura(pojoFactura.getId(), usuarioId);
		}
		
		return true;
	}

	public Respuesta timbrar(Comprobante comprobante, String fileName, long idEmpresa) {
		Respuesta respuesta = new Respuesta();
		Element root = null;
		// ------------------
		ResultSet rsCertificados = null;
		// ------------------
		InputStream clavePrivada = null;
		String strQuery = "";
		String password = ""; 
		String timbreUser = "";
		String timbrePass = "";
		
		try {
			error();
			this.conn = getConn();
			
			// Certificado
			strQuery = "SELECT llave_data, palabra, usuario_timbre, clave_timbre FROM empresa_certificado "
					+ "WHERE (estatus IS NULL OR estatus='A') AND id_empresa = " + idEmpresa;
			rsCertificados = this.conn.createStatement().executeQuery(strQuery); 
			if (! rsCertificados.next()) {
				error(301L, "No se pudo recuperar el Certificado cargado a la Empresa");
				respuesta.getErrores().setCodigoError(this.errorCodigo);
				respuesta.getErrores().setDescError(this.errorDescripcion);
				return respuesta;
			}
			
			//fileName = "P-" + comprobante.getSerie() + comprobante.getFolio() + "_" + idFactura;
			clavePrivada = rsCertificados.getBinaryStream("llave_data");
			password = rsCertificados.getString("palabra"); 
			timbreUser = rsCertificados.getString("usuario_timbre"); 
			timbrePass = rsCertificados.getString("clave_timbre"); 
			
			// Generamos XML
			root = generaXML(comprobante);
			if (root == null) {
				error(303L, "No se pudo general el XML (CFDI)");
				respuesta.getErrores().setCodigoError(this.errorCodigo);
				respuesta.getErrores().setDescError(this.errorDescripcion);
				return respuesta;
			}
			
			// Generamos CFDI 400
			if (! generarCFDI(root, clavePrivada, password, fileName, timbreUser, timbrePass)) {
				respuesta.getErrores().setCodigoError(this.errorCodigo);
				respuesta.getErrores().setDescError(this.errorDescripcion);
				return respuesta;
			}
			
			respuesta.getBody().addValor("timbrado", 1);
			respuesta.getBody().addValor("fechaTimbrado", this.params.get("fecha_timbrado").toString());
			respuesta.getBody().addValor("codigo", this.params.get("state").toString());
			respuesta.getBody().addValor("mensaje", this.params.get("descripcion").toString());
			respuesta.getBody().addValor("serie", comprobante.getSerie());
			respuesta.getBody().addValor("folio", comprobante.getFolio());
			respuesta.getBody().addValor("uuid", this.params.get("uuid").toString());
			respuesta.getBody().addValor("xmlPrevio", this.params.get("xml").toString().getBytes());
			respuesta.getBody().addValor("xmlTimbrado", this.params.get("cfdi").toString().getBytes());
			respuesta.getBody().addValor("cadenaOriginal", this.params.get("cadena_original").toString().getBytes());
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar el comprobante para la Factura", e);
			error(300L, "No se pudo generar el Comprobante para la Factura");
			respuesta.getErrores().setCodigoError(this.errorCodigo);
			respuesta.getErrores().setDescError(this.errorDescripcion);
		} 
		
		return respuesta;
	}
	
	public boolean cancelar(long idFactura, long usuarioId) throws Exception {
		try {
			return cancelar(this.ifzFactura.findById(idFactura), usuarioId);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar Timbrar la Factura", e);
			throw e;
		}
	}

	public boolean cancelar(Factura pojoFactura, long usuarioId) throws Exception {
		try {
			return cancelar(this.ifzFactura.convertir(pojoFactura), usuarioId);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar Timbrar la Factura", e);
			throw e;
		}
	}

	public boolean cancelar(FacturaExt pojoFactura, long usuarioId) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ResultSet rsCertificados = null;
		Element root = null;
		X509Certificate c = null;
		PKCS8EncodedKeySpec keySpec = null;
		PKCS8Key key = null;
		PrivateKey pk = null;
		InputStream clavePrivada = null;
		byte[] bCertificado = null;
		byte[] bLlave = null;
		String palabra = "";
		String certificado = "";
		String strQuery = "";
		String timbreUser = "";
		String timbrePass = "";
		String fileName = "";

		try {
			// Inicializaciones
			this.conn = getConn();
			if (this.params == null)
				this.params = new HashMap<String, Object>();
			this.params.clear();
			
			// Certificado
			strQuery = "SELECT no_certificado, certificado_data, certificado_data_pem"
						+ ", llave_data, llave_data_pem, llave_data_enc"
						+ ", palabra, usuario_timbre, clave_timbre "
					+ "FROM empresa_certificado "
					+ "WHERE (estatus IS NULL OR estatus='A') AND id_empresa = " + pojoFactura.getIdEmpresa().getId();
			rsCertificados = this.conn.createStatement().executeQuery(strQuery); 
			if (! rsCertificados.next()) {
				error(301L, "No se pudo recuperar el Certificado cargado a la Empresa");
				return false;
			}

			fileName = pojoFactura.getId() + "_" + pojoFactura.getFolioFactura();
			timbreUser = rsCertificados.getString("usuario_timbre"); 
			timbrePass = rsCertificados.getString("clave_timbre"); 
			bCertificado = rsCertificados.getBytes("certificado_data");
			clavePrivada = rsCertificados.getBinaryStream("llave_data");
			palabra = rsCertificados.getString("palabra");
			
			// Instanciamos certificado (.cer)
			c = X509Certificate.getInstance(bCertificado);
			certificado = new String(Base64.encodeBase64(bCertificado));
			
			// Instanciamos llave (.key)
			key = new PKCS8Key(clavePrivada, palabra.toCharArray());
			bLlave = key.getDecryptedBytes();
			keySpec = new PKCS8EncodedKeySpec(bLlave);
			pk = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

			// Generamos CFDI
			root = generaXMLCancelacion(pojoFactura.getIdEmpresa().getRfc(), Arrays.asList(pojoFactura.getUuid()), c, certificado, pk);
			if (root == null) {
				error(303L, "No se pudo generar la solicitud de Cancelacion de Timbre");
				return false;
			}
			
			// Generamos XML 400
			if (! generarCFDICancelacion(root, fileName, timbreUser, timbrePass))
				return false;

			pojoFactura.setCanceladoPor(usuarioId);
			pojoFactura.setAcuseCancelacion(this.params.get("acuse").toString().getBytes());
			pojoFactura.setEstadoCancelacion(Double.parseDouble(this.params.get("state").toString()));
			pojoFactura.setMensajeCancelacion(this.params.get("descripcion").toString());
			pojoFactura.setFechaCancelacion(formatter.parse(this.params.get("fecha_cancelacion").toString()));
		} catch (Exception e) {
			log.error("ERROR INTERNO: 400 - No se pudo generar el XML para Timbrar la Factura", e);
			error(400L, "Ocurrio un problema al generar el XML para timbrar la factura");
			return false;
		} 
	
		try { 
			this.conn.close(); 
		} catch(Exception e) {
			log.error("ERROR CERRANDO LA CONECCION", e);
		}

		return  true;
	}
	
	public boolean cancelar(long idEmpresa, String rfcEmisor, List<FacturaExt> listFacturas, long usuarioId) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		List<String> listUuids = new ArrayList<String>();
		ResultSet rsCertificados = null;
		String strQuery = "";
		String timbreUser = "";
		String timbrePass = "";
		String fileName = "";
		Element root = null;
		X509Certificate c = null;
		PKCS8EncodedKeySpec keySpec = null;
		PKCS8Key key = null;
		PrivateKey pk = null;
		InputStream clavePrivada = null;
		byte[] bCertificado = null;
		byte[] bLlave = null;
		String certificado = "";
		String palabra = "";

		try {
			if (listFacturas == null || listFacturas.isEmpty()) {
				error(300L, "No se indico ninguna Factura (CFDI)");
				return false;
			}
			
			// Inicializaciones
			this.conn = getConn();
			if (this.params == null)
				this.params = new HashMap<String, Object>();
			this.params.clear();
			
			// Certificado
			strQuery = "SELECT no_certificado, certificado_data, certificado_data_pem"
						+ ", llave_data, llave_data_pem, llave_data_enc"
						+ ", palabra, usuario_timbre, clave_timbre "
					+ "FROM empresa_certificado "
					+ "WHERE (estatus IS NULL OR estatus='A') AND id_empresa = " + idEmpresa;
			rsCertificados = this.conn.createStatement().executeQuery(strQuery); 
			if (! rsCertificados.next()) {
				error(301L, "No se pudo recuperar el Certificado cargado a la Empresa");
				return false;
			}

			timbreUser = rsCertificados.getString("usuario_timbre"); 
			timbrePass = rsCertificados.getString("clave_timbre"); 
			bCertificado = rsCertificados.getBytes("certificado_data");
			clavePrivada = rsCertificados.getBinaryStream("llave_data");
			palabra = rsCertificados.getString("palabra");
			
			// Instanciamos certificado (.cer)
			c = X509Certificate.getInstance(bCertificado);
			certificado = new String(Base64.encodeBase64(bCertificado));
			
			// Instanciamos llave (.key)
			key = new PKCS8Key(clavePrivada, palabra.toCharArray());
			bLlave = key.getDecryptedBytes();
			keySpec = new PKCS8EncodedKeySpec(bLlave);
			pk = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

			for (FacturaExt pojoFactura : listFacturas)
				listUuids.add(pojoFactura.getUuid());

			// Generamos CFDI
			root = generaXMLCancelacion(rfcEmisor, listUuids, c, certificado, pk);
			if (root == null) {
				error(303L, "No se pudo general el XML (CFDI)");
				return false;
			}
			
			// Generamos XML 400
			fileName = root.getAttributeValue("Fecha") + "_x" + listFacturas.size();
			if (! generarCFDICancelacion(root, fileName, timbreUser, timbrePass))
				return false;

			// Actualizo facturas
			for (FacturaExt pojoFactura : listFacturas) {
				pojoFactura.setCanceladoPor(usuarioId);
				pojoFactura.setAcuseCancelacion(this.params.get("acuse").toString().getBytes());
				pojoFactura.setEstadoCancelacion(Double.parseDouble(this.params.get("state").toString()));
				pojoFactura.setMensajeCancelacion(this.params.get("descripcion").toString());
				pojoFactura.setFechaCancelacion(formatter.parse(this.params.get("fecha_cancelacion").toString()));
				
				// Actualizo factura
				this.ifzFactura.update(pojoFactura);
			}
		} catch (Exception e) {
			log.error("ERROR INTERNO: 400 - No se pudo generar el XML para Timbrar la Factura", e);
			error(400L, "Ocurrio un problema al generar el XML para timbrar la factura");
			return false;
		} 
	
		try { 
			this.conn.close(); 
		} catch(Exception e) {
			log.error("ERROR CERRANDO LA CONECCION", e);
		}

		return  true;
	}
	
	public boolean resumenFacturacion(long procesoId, int empresaId) {
		/*ResultSet rs = null;
		String sql = null;
		String res = "";
		String cuerpo = null;
		Correo correo = null;
		String toCorreo;
		//String toCCCorreo;
		String soporteCorreo = "ameza@condese.com";
		
		try {
			conn = getConn();
			sql ="select  " +
				" case when estatus = 'G' then 'Generada' " +
				"   when estatus='C' then 'Cancelada'" +
				"   when estatus='X' then 'Timbrado Cancelada'" +
				"   when estatus='F' then 'Foliada'" +
				"   when estatus='T' then 'Timbrada'" +
				" else estatus end " +
				"   ,count(*)  " +
				" from fe_facturas " +
				" where id_proceso = " + procesoId +
				" group by estatus";
			rs = this.conn.createStatement().executeQuery(sql);
			if (! rs.next()) {
				res = "No se encontraron datos para el proceso " + procesoId;
				cuerpo ="<html><body>" + res + "</body></html>";
			} else {
				try {
					cuerpo ="<html><body><table><tr><td colspan=\"2\">Resultado del proceso</td></tr>";
					while (rs.next()) {
						cuerpo += "<tr><td>" + rs.getString(1)  + "</td><td>"
								+ rs.getString(2) + "</td></tr>";
					}
					cuerpo += "</table></body></html>";
					res += "\nDatos generados para correo";
				} catch(Exception e) {
					res = "Error al generar correo \n" + e.toString();
					cuerpo ="<html><body>" + res + "</body></html>";
					Util.log(e);
				}
			}
		} catch(Exception e) {
			res += "\nError en proceso de generacion de  correo \n" + e.toString();
			cuerpo = "<html><body>" + res + "</body></html>";
			Util.log(e);
		}

		try {
			correo = new Correo();
			ctx = new InitialContext();
			correo.conexion(ctx);
			rs = this.conn.createStatement().executeQuery(
					"select correo  " +
					" from cat_empresas " +
					" where empresa_id = " + empresaId );
			if ( rs.next() ) {
				res += "\nNo se encontraron las cuentas de correo para la empresa " + empresaId;
				return res;
			}
			toCorreo = rs.getString(1) != null ? rs.getString(1) : soporteCorreo;
			correo.msgsend(toCorreo, "Resumen de Facturacion, Proceso:" + procesoId, cuerpo);
			res += "\nCorreo enviado a " + toCorreo;
		} catch(Exception e ) {
			res += "\nError al crear conexion de correo \n" + e.toString();
			Util.log(e);
		}

		try {
			this.conn.close();
			conn = null;
			correo.desconexion();
		} catch(Exception e) { }

		return res;*/
		return true;
	}

	// -----------------------------
	// METODOS PRIVADOS
	// -----------------------------

	private boolean validaciones(FacturaExt pojoFactura) {
		log.info("Validando para timbrar ... ");
		if (pojoFactura == null || pojoFactura.getId() == null || pojoFactura.getId() <= 0L) {
			error(101L, "No existe la factura especificada");
			return false;
		}
		
		if (pojoFactura.getIdEmpresa() == null || pojoFactura.getIdEmpresa().getId() == null || pojoFactura.getIdEmpresa().getId() <= 0L) {
			error(102L, "La factura indicada no tiene asignada una Empresa");
			return false;
		}
		
		if (pojoFactura.getIdSucursal() == null || pojoFactura.getIdSucursal().getId() <= 0L) {
			error(103L, "La factura indicada no tiene asignada una Sucursal");
			return false;
		}

		if (pojoFactura.getTipoCambio() <= 0)
			pojoFactura.setTipoCambio(1.0);
		
		if (pojoFactura.getTipo() == null || "".equals(pojoFactura.getTipo()))
			pojoFactura.setTipo("X");
		
		if (pojoFactura.getCondicionesPago() == null || "".equals(pojoFactura.getCondicionesPago()))
			pojoFactura.setCondicionesPago("Contado");
		
		log.info("Validacion terminada");
		return true;
	}
	
	private boolean asignarSerieFolio(FacturaExt pojoFactura) {
		boolean actualizarSerieFolio = false;
		ResultSet rsFolios = null;
		ResultSet rs = null;
		Statement stmt = null;
		String strQuery ="";
		String secuencia = "";
		String serie = "";
		String folio = "";
		Integer ifolio = 0;
		
		try {
			log.info("Comprobando Serie y Folio ... ");
			stmt = this.conn.createStatement();
			
			// Comprobamos Serie
			serie = pojoFactura.getSerie();
			if (serie == null || "".equals(serie.trim())) {
				actualizarSerieFolio = true;
				strQuery = "SELECT COALESCE(serie, '') AS serie FROM a535303dbc WHERE aa = " + pojoFactura.getIdSucursal().getId() + ";";
				rsFolios = stmt.executeQuery(strQuery);
				if (! rsFolios.next()) {
					error(201L, "No fue posible asignar Serie y Folio a la factura. La sucursal no tiene asignado Serie para Facturacion.");
					return false;
				}
				
				serie = rsFolios.getString("serie");
				if (serie == null || "".equals(serie.trim())) {
					error(201L, "No fue posible asignar Serie y Folio a la factura. La sucursal no tiene asignado Serie para Facturacion.");
					return false;
				}
			}
			
			// Comprobamos folio
			folio = pojoFactura.getFolio();
			if (folio == null || "".equals(folio.trim()) || "0".equals(folio.trim())) {
				actualizarSerieFolio = true;
				
				// Comprobamos secuencia para Folio de facturacion
				try {
					secuencia = "seq_sucursal_folio_facturacion_" + pojoFactura.getIdSucursal().getId() + "_serie_" + serie.trim().toLowerCase();
					strQuery = "select 1 from pg_class where relkind = 'S' and relname = '" + secuencia + "'";
					rs  = stmt.executeQuery(strQuery);
					if (! rs.next()) {
						strQuery = "CREATE SEQUENCE " +  secuencia  + " INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;";
						stmt.execute(strQuery);
					}
				} catch (Exception e) {
					error(202L, "No fue posible comprobar el Foliaje de la Sucursal asignada a la Factura.");
					return false;
				}
				
				// Asignamos Folio
				try {
					strQuery = "SELECT nextval('" + secuencia + " ') AS folio ";
					rs  = stmt.executeQuery(strQuery);
					if (! rs.next())
						throw new Exception("CONTROL");
	
					ifolio = rs.getInt("folio");
					if (ifolio == null || ifolio <= 0)
						throw new Exception("CONTROL");
					folio = String.valueOf(ifolio);
				} catch (Exception e) {
					error(203L, "No fue posible generar Folio a la Factura.");
					if ("CONTROL".equals(e.getMessage()))
						throw e;
					return false;
				}
				
				// Actualizamos el folio generado a la sucursal
				try {
					strQuery = "UPDATE a535303dbc SET folio = " + ifolio + " WHERE aa = " + pojoFactura.getIdSucursal().getId() + ";";
					stmt.execute(strQuery);
				} catch (Exception e) {
					error(203L, "No fue posible generar Folio a la Factura.");
					if ("CONTROL".equals(e.getMessage()))
						throw e;
					return false;
				}
			}
			
			if (actualizarSerieFolio) {
				// Asigno Serie y Folio a la Factura
				pojoFactura.setSerie(serie);
				pojoFactura.setFolio(folio);
				pojoFactura.setFolioFactura(serie + "-" + folio);
				
				this.params.put("serie", serie);
				this.params.put("folio", folio);
				this.params.put("id_folio", pojoFactura.getIdSucursal().getId());
			}
			log.info("Comprobacion terminada");
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar foliar la Factura.", e);
			error(200L, "Ocurrio un problema al intentar foliar la Factura.");
			return false;
		}
		
		return true;
	}
	
	private Element generaXML(FacturaExt pojoFactura, List<FacturaDetalleExt> listDetalles, String noCertificado, String certificadoData) {
		DecimalFormat formatterDecimal = new DecimalFormat("#######0.00");
		List<FacturaDetalleImpuestoExt> listImpuestos = null;
		double montoRetencion = 0;
		double montoTraslado = 0;
		double monto = 0;
		// XML
		Namespace xmlns = null;
		Namespace xsi = null;
		Attribute attSchema = null;
		Element root = null;
		Element emisor = null;
		Element receptor = null;
		Element conceptos = null;
		Element concepto = null;
		Element impuestos = null;
		Element traslados = null;
		Element traslado = null;
		Element trasladoMayor = null;
		Element retenciones = null;
		Element retencion = null;
		Element retencionMayor = null;
		Element relacionados = null;
		Element relacionado = null;
		boolean tieneImpuestos = false;
		
		try {
			xmlns = Namespace.getNamespace("cfdi", "http://www.sat.gob.mx/cfd/3");
			xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			attSchema = new Attribute("schemaLocation", "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd", xsi);

			// COMPROBANTE
			root = new Element("Comprobante", xmlns);
			root.setAttribute(attSchema);
			root.setAttribute(new Attribute("Version", pojoFactura.getVersion()));
			root.setAttribute(new Attribute("Serie", pojoFactura.getSerie().trim()));
			root.setAttribute(new Attribute("Folio", pojoFactura.getFolio().trim()));
			root.setAttribute(new Attribute("Fecha",(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pojoFactura.getFechaEmision()).replace(' ','T')))); 
			root.setAttribute(new Attribute("MetodoPago", pojoFactura.getIdMetodoPago().getValor().trim()));
			root.setAttribute(new Attribute("FormaPago", claveFormaPago(pojoFactura.getIdFormaPago())));
			root.setAttribute(new Attribute("NoCertificado", noCertificado));
			root.setAttribute(new Attribute("Certificado", certificadoData));
			if (pojoFactura.getDescuento().doubleValue() > 0) {
				root.setAttribute(new Attribute("Descuento", formatterDecimal.format(pojoFactura.getDescuento().doubleValue())));
				if (pojoFactura.getMotivoDescuento() != null && ! "".equals(pojoFactura.getMotivoDescuento()))
					root.setAttribute(new Attribute("motivoDescuento", pojoFactura.getMotivoDescuento()));
			}
			root.setAttribute(new Attribute("SubTotal", formatterDecimal.format(pojoFactura.getSubtotal().doubleValue())));
			root.setAttribute(new Attribute("Total", formatterDecimal.format(pojoFactura.getTotal())));
			root.setAttribute(new Attribute("TipoDeComprobante", pojoFactura.getTipoComprobante())); 
			root.setAttribute(new Attribute("Moneda", pojoFactura.getAbreviaturaMoneda()));
			root.setAttribute(new Attribute("LugarExpedicion", pojoFactura.getIdSucursal().getColonia().getCp()));
			if (pojoFactura.getTipoCambio() > 1)
				root.setAttribute(new Attribute("TipoCambio", (new DecimalFormat("#######0.##")).format(pojoFactura.getTipoCambio())));
			root.setAttribute(new Attribute("CondicionesDePago", pojoFactura.getCondicionesPago().trim()));
			
			// CFDI RELACIONADO
			if (pojoFactura.getCfdiRelacionado() == 1) {
				relacionados = new Element("CfdiRelacionados", xmlns);
				relacionados.setAttribute(new Attribute("TipoRelacion", pojoFactura.getCfdiTipoRelacion()));
				relacionado = new Element("CfdiRelacionado", xmlns);	
				relacionado.setAttribute(new Attribute("UUID", pojoFactura.getCfdiRelacionadoUuid()));
				relacionados.addContent(relacionado);
				root.addContent(relacionados);
			}
			
			// EMISOR
			emisor = new Element("Emisor", xmlns);
			emisor.setAttribute(new Attribute("Rfc", pojoFactura.getIdEmpresa().getRfc().trim()));
			emisor.setAttribute(new Attribute("Nombre", pojoFactura.getIdEmpresa().getEmpresa().trim()));
			emisor.setAttribute(new Attribute("RegimenFiscal", pojoFactura.getIdEmpresa().getCodigoRegimenFiscal()));
			root.addContent(emisor);

			// RECEPTOS
			receptor = new Element("Receptor", xmlns);
			receptor.setAttribute(new Attribute("Rfc", pojoFactura.getRfc().trim()));
			receptor.setAttribute(new Attribute("Nombre", pojoFactura.getCliente().trim()));
			receptor.setAttribute(new Attribute("UsoCFDI", pojoFactura.getUsoCfdi())); 
			root.addContent(receptor);
			
			// CONCEPTOS
			conceptos = new Element("Conceptos",xmlns);
			for (FacturaDetalleExt var : listDetalles) {
				concepto = new Element("Concepto", xmlns);
				concepto.setAttributes(
					java.util.Arrays.asList(
						new Attribute("ClaveProdServ", var.getClaveSat().trim()),
						new Attribute("ClaveUnidad", var.getIdUnidadMedida().getValor().trim()),
						new Attribute("Cantidad", formatterDecimal.format(var.getCantidad())),
						new Attribute("NoIdentificacion", var.getIdConcepto().getId().toString()),
						new Attribute("Descripcion", var.getIdConcepto().getDescripcion().trim()),
						new Attribute("Unidad", var.getIdUnidadMedida().getDescripcion().trim()),
						new Attribute("ValorUnitario", formatterDecimal.format(var.getCosto())),
						new Attribute("Importe", formatterDecimal.format(var.getImporte()))
				));
				
				// Impuestos del Concepto
				listImpuestos = this.ifzImpuestos.findExtByFacturaDetalle(var);
				if (listImpuestos != null && ! listImpuestos.isEmpty()) {
					impuestos = new Element("Impuestos", xmlns);
					traslados = new Element("Traslados", xmlns);
					retenciones = new Element("Retenciones", xmlns);
					
					for (FacturaDetalleImpuestoExt imp : listImpuestos) {
						monto = Double.parseDouble(imp.getIdImpuesto().getAtributo1().trim());
						monto = monto / 100;
						
						if ("DE".equals(imp.getIdImpuesto().getTipoCuenta())) {
							traslado = new Element("Traslado", xmlns);
							traslado.setAttribute("Base", formatterDecimal.format(var.getImporte()));
							traslado.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
							traslado.setAttribute("TipoFactor", "Tasa");
							traslado.setAttribute("TasaOCuota", (new DecimalFormat("#######0.000000")).format(monto));
							traslado.setAttribute("Importe", formatterDecimal.format(imp.getMonto().doubleValue()));
							traslados.addContent(traslado);
							
							if (montoTraslado == 0 || imp.getMonto().doubleValue() > montoTraslado) {
								tieneImpuestos = true;
								trasladoMayor = new Element("Traslado", xmlns);
								trasladoMayor.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
								trasladoMayor.setAttribute("TipoFactor", "Tasa");
								trasladoMayor.setAttribute("TasaOCuota", (new DecimalFormat("#######0.000000")).format(monto));
								trasladoMayor.setAttribute("Importe", formatterDecimal.format(imp.getMonto().doubleValue()));
								montoTraslado = imp.getMonto().doubleValue();
							}
						} else {
							retencion = new Element("Retencion", xmlns);
							retencion.setAttribute("Base", formatterDecimal.format(var.getImporte()));
							retencion.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
							retencion.setAttribute("TipoFactor", "Tasa");
							retencion.setAttribute("TasaOCuota", (new DecimalFormat("#######0.000000")).format(monto));
							retencion.setAttribute("Importe", formatterDecimal.format(imp.getMonto().doubleValue()));
							retenciones.addContent(retencion);

							if (montoTraslado == 0 || imp.getMonto().doubleValue() > montoRetencion) {
								tieneImpuestos = true;
								retencionMayor = new Element("Traslado", xmlns);
								retencionMayor.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
								retencionMayor.setAttribute("TipoFactor", "Tasa");
								retencionMayor.setAttribute("TasaOCuota", (new DecimalFormat("#######0.000000")).format(monto));
								retencionMayor.setAttribute("Importe", formatterDecimal.format(imp.getMonto().doubleValue()));
								montoRetencion = imp.getMonto().doubleValue();
							}
						}
					}

					if (trasladoMayor != null)
						impuestos.addContent(traslados);
					if (retencionMayor != null)
						impuestos.addContent(retenciones);
					if (tieneImpuestos)
						concepto.addContent(impuestos);
				}
				
				// Añado concepto
				conceptos.addContent(concepto);
			}
			
			// Añado conceptos
			root.addContent(conceptos);
			
			// IMPUESTOS
			impuestos = new Element("Impuestos", xmlns);
			traslados = new Element("Traslados", xmlns);
			retenciones = new Element("Retenciones", xmlns);
			
			if (trasladoMayor != null) {
				impuestos.setAttribute("TotalImpuestosTrasladados", formatterDecimal.format(pojoFactura.getImpuestos()));
				traslados.addContent(trasladoMayor);
				impuestos.addContent(traslados);
			}
			
			if (retencionMayor != null) {
				impuestos.setAttribute("TotalImpuestosRetenidos", formatterDecimal.format(pojoFactura.getRetenciones()));
				retenciones.addContent(retencionMayor);
				impuestos.addContent(retenciones);
			}
			
			root.addContent(impuestos);
		} catch (Exception e) {
			log.error("303L - No se pudo general el XML (CFDI)", e);
			root = null;
		}
		
		return root;
	}

	private Element generaXML(Comprobante comprobante) {
		DocumentBuilderFactory dbf = null;
		DocumentBuilder db = null;
		org.w3c.dom.Document doc = null;
		org.jdom.Document jdom = null;
		Element root = null;
		// -------------------------------
		JAXBContext contextObj = null;
		Marshaller marshallerObj = null;
		
		try {
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			
			db = dbf.newDocumentBuilder();
			doc = db.newDocument();
			
			contextObj = JAXBContext.newInstance(Comprobante.class, Pagos.class);
			marshallerObj = contextObj.createMarshaller();
			marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshallerObj.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshallerObj.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.sat.gob.mx/cfd/3  http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd");
			marshallerObj.marshal(comprobante, doc);
			
			jdom = convertDOMtoJDOM(doc);
			root = jdom.detachRootElement();
		} catch (Exception e) {
			log.error("303L - No se pudo general el XML (CFDI)", e);
			root = null;
		}
		
		return root;
	}
	
	private Element generaXMLCancelacion(String rfcEmisor, List<String> uuids, X509Certificate c, String certificateData, PrivateKey pk) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		org.w3c.dom.Document dom = null;
		org.jdom.Document jdom = null;
		Element root = null;
		Element folios = null;
		Element folio = null;
		Namespace xmlns = null;
		Namespace xsi = null;
		Namespace xsd = null;
		
		try {
			// Namespaces
			xmlns = Namespace.getNamespace("http://www.sat.gob.mx/cfd/3");
			xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			xsd = Namespace.getNamespace("xsd", "http://www.w3.org/2001/XMLSchema");

			// COMPROBANTE Cancelacion
			root = new Element("Cancelacion", xmlns);
			root.addNamespaceDeclaration(xsi);
			root.addNamespaceDeclaration(xsd);
			root.setAttribute(new Attribute("RfcEmisor", rfcEmisor));
			root.setAttribute(new Attribute("Fecha", (formatter.format(Calendar.getInstance(this.timeZone).getTime()).replace(' ','T'))));
			
			// Genero FOLIOS
			folios = new Element("Folios", Namespace.NO_NAMESPACE);
			for (String uuid : uuids) {
				folio = new Element("UUID");
				folio.addContent(uuid);
				folios.addContent(folio);
			}
			
			// Añado los folios al nodo principal
			root.addContent(folios);
			
			// Genero Signature
			jdom = new Document(root);
			dom = convertJDOMToDOM(jdom);
			if (! generaSignature(dom, pk, c, certificateData)) {
				log.info("304L - No se pudo general el XML para Cancelacion (CFDI) [Signature]");
				return null;
			}
			
			jdom = convertDOMtoJDOM(dom);
			root = jdom.getRootElement();
		} catch (Exception e) {
			log.error("303L - No se pudo general el XML para Cancelacion (CFDI)", e);
			root = null;
		}
		
		return root;
	}
	
	private boolean generarCFDI(Element root, InputStream clavePrivada, String password, String fileName, String timbreUser, String timbrePass) {
		Document document = null;
		GeneradorCFD cfd = null;
		AcuseRecepcion acuse = null;
		CFDI cfdi = null;
		XMLOutputter outputter = null;
		FileWriter fwxml = null;
		FileWriter fwco = null;
		StringWriter sw = null;
		String xml = "";
		
		try {
			// escribir un archivo con la XML previo al sello
			outputter = new XMLOutputter();
			xml = outputter.outputString(root);
			fwco = new FileWriter(new File("/tmp/" + fileName  + "-prev.xml"));
			fwco.write(xml);
			fwco.close();
			
			try {
				document = new Document(root);
				cfd = new GeneradorCFD(document, clavePrivada, password, "3.3");
				document.getRootElement().setAttribute(new Attribute("Sello", new String(cfd.getSelloDigital())));
			} catch (Exception ex) {
				log.error("401 - No se pudo generar la Cadena Original y/o Sello Digital para Timbrar la Factura", ex);
				error(401L, "Ocurrio un problema generando Cadena Original y/o Sello Digital para timbrar la factura");
				return false;
			}

			// escribir un archivo con el XML para timbrar
			fwxml = new FileWriter(new File("/tmp/"+ fileName  + ".xml"));
			sw = new StringWriter();
			outputter = new XMLOutputter();
			outputter.output(document, fwxml);
			outputter.output(document, sw);
			sw.close();
			fwxml.close();
			
			// escribir un archivo con la Cadena Original utilizada para generar el sello
			fwco = new FileWriter(new File("/tmp/"+ fileName  + ".txt"));
			fwco.write(cfd.getCadenaOriginal());
			fwco.close();

			try {
				cfdi = new CFDIRealVirtualv33();
				cfdi.debugging(this.debugging);
				if (this.testing)
					acuse = cfdi.timbrarTest(sw.toString(), timbreUser, timbrePass);
				else
					acuse = cfdi.timbrar(sw.toString(), timbreUser, timbrePass);
			} catch (Exception ex) {
				log.error("ERROR INTERNO: 402 - No se pudo Timbrar la Factura", ex);
				error(402L, "No se pudo Timbrar la Factura");
				return false;
			}
			
			if (acuse.getState() != 0) {
				error((long) acuse.getState(), acuse.getDescripcion());
				return false;
			}
			
			// escribir un archivo con XML timbrado
			fwco = new FileWriter(new File("/tmp/"+ fileName  + "-timbrado.xml"));
			fwco.write(acuse.getXml());
			fwco.close();
		} catch (Exception e) {
			log.error("ERROR INTERNO: 400 - No se pudo generar el XML para Timbrar la Factura", e);
			error(400L, "Ocurrio un problema al generar el XML para timbrar la factura");
			return false;
		} finally {
			if (cfd != null) {
				this.params.put("xml", cfd.getXML().getBytes());
				this.params.put("cadena_original", cfd.getCadenaOriginal());
				this.params.put("sello", cfd.getSelloDigital());
			}
			
			if (acuse != null) {
				this.params.put("state", Long.valueOf(acuse.getState()));
				this.params.put("descripcion", acuse.getDescripcion());
				if (acuse.getState() == 0) {
					this.params.put("cfdi", acuse.getXml());
					this.params.put("timbre", acuse.getTimbre());
					this.params.put("fecha_timbrado", acuse.getFechaTimbrado().replace("T", " "));
					this.params.put("uuid", acuse.getUuid());
					this.params.put("no_certificado_sat", acuse.getNoCertificadoSat());
					this.params.put("sello_sat", acuse.getSelloSat());
				}
			}
		}
		
		return true;
	}
	
	private boolean generarCFDICancelacion(Element root, String fileName, String timbreUser, String timbrePass) {
		AcuseCancelacion acuse = null;
		CFDI cfdi = null;
		XMLOutputter outp = null;
		FileWriter fwxml = null;
		String acuseCancelacion = "";
		
		try {
			if (root == null) {
				log.error("ERROR INTERNO: 401 - No se genero el XML para Cancelacion");
				error(401L, "No se pudo generar el XML para Cancelacion");
				return false;
			}
			
			outp = new XMLOutputter();
			acuseCancelacion = outp.outputString(root);

			if (acuseCancelacion.contains(" xmlns=\"\""))
				acuseCancelacion = acuseCancelacion.replace(" xmlns=\"\"", "");

			// escribir un archivo con la XML previo al sello
			fwxml = new FileWriter(new File("/tmp/" + fileName  + "_cancel.xml"));
			fwxml.write(acuseCancelacion);
			fwxml.close();

			try {
				cfdi = new CFDIRealVirtualv33();
				cfdi.soapRequestVersion(this.soapRequestVersion);
				cfdi.debugging(this.debugging);
				if (this.testing)
					acuse = cfdi.cancelarTest(acuseCancelacion, timbreUser, timbrePass);
				else
					acuse = cfdi.cancelar(acuseCancelacion, timbreUser, timbrePass);
			} catch (Exception ex) {
				log.error("ERROR INTERNO: 402 - No se pudo Timbrar la Factura", ex);
				error(402L, "No se pudo Timbrar la Factura");
				return false;
			}
			
			if (acuse.getState() != 0) {
				error((long) acuse.getState(), acuse.getDescripcion());
				return false;
			}
		} catch (Exception e) {
			log.error("ERROR INTERNO: 400 - No se pudo generar el XML para Timbrar la Factura", e);
			error(400L, "Ocurrio un problema al generar el XML para timbrar la factura");
			return false;
		} finally {
			if (acuse != null) {
				this.params.put("state", Long.valueOf(acuse.getState()));
				this.params.put("descripcion", acuse.getDescripcion());
				
				if (acuse.getState() == 0) {
					this.params.put("rfcSolicitante", acuse.getRfcSolicitante());
					this.params.put("uuid", acuse.getUUIDs());
					this.params.put("fecha", acuse.getFecha().replace("T", " "));
					this.params.put("noSerieFirmante", acuse.getNoSerieFirmante());
					this.params.put("acuse_cancelacion", acuse.getAcuse());
				}
			}
		}
		
		return true;
	}
	
	private boolean actualizamosFactura(long idFactura, long idUsuario) throws Exception {
		SimpleDateFormat formateador = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String strQuery = "";
		
		try {
			log.info("Actualizando factura ... ");
			for (Entry<String, Object> item : this.params.entrySet()) {
				if (item == null || item.getValue() == null || item.getValue().getClass() == null) 
					continue;
				
				if (! "".equals(strQuery)) 
					strQuery += ",";
				if (java.util.Date.class == item.getValue().getClass()) 
					strQuery += item.getKey() + " = date('" + formateador.format((Date) item.getValue()) + "')";
				else if (java.lang.Integer.class == item.getValue().getClass()) 
					strQuery += item.getKey() + " = " + item.getValue().toString();
				else if (java.lang.Long.class == item.getValue().getClass()) 
					strQuery += item.getKey() + " = " + item.getValue().toString();
				else if (java.lang.Double.class == item.getValue().getClass()) 
					strQuery += item.getKey() + " = " + item.getValue().toString();
				else if (java.math.BigDecimal.class == item.getValue().getClass()) 
					strQuery += item.getKey() + " = " + ((BigDecimal) item.getValue()).toString();
				else
					strQuery += item.getKey() + " = '" + item.getValue().toString() + "'";
			}
			
			if ("".equals(strQuery))
				return false;
			
			strQuery = "UPDATE factura SET " + strQuery + ", fecha_modificacion = NOW(), modificado_por = " + idUsuario + " WHERE id = " + idFactura;
			this.conn.createStatement().execute(strQuery);
			log.info("Factura actualizada");
		} catch (Exception e) {
			log.info("-------------------------------------------------------------------------------------------------------------------------------");
			log.error("Error al actualizar datos en factura. Logica_CuentasPorCobrar.FactElectv33.actualizamosFactura(long idFactura, long idUsuario)", e);
			log.info("-------------------------------------------------------------------------------------------------------------------------------");
			return false;
		}
		
		return true;
	}

	private boolean generaSignature(org.w3c.dom.Document originalXmlDocument, PrivateKey pk, X509Certificate cert, String certificateData) {
		XMLSignatureFactory fac = null;
		DOMSignContext dsc = null;
		Reference ref = null;
		SignedInfo si = null;
		KeyInfoFactory kif = null;
		XMLSignature signature = null;
		KeyInfo ki = null;
		X509Data xd = null;
		X509IssuerSerial issuer = null;
		List<Object> x509Content = null;
		Node root = null;
		
		try {
			// Create factory XML
	        fac = XMLSignatureFactory.getInstance("DOM");
	        
	        // Create a Reference to the enveloped document
	        ref = fac.newReference("", 
	        		fac.newDigestMethod(this.digestMethod, null), 
	        		Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), 
	        		null, 
	        		null
	        );
	
	        // Create the SignedInfo.
	        si = fac.newSignedInfo(
	        		fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null), 
	        		fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), 
	        		Collections.singletonList(ref)
	        ); 
	        
	        // Create the KeyInfo containing the X509Data.
	        kif = fac.getKeyInfoFactory();
	        
	        // Create IssuerSerial
	        issuer = kif.newX509IssuerSerial(cert.getIssuerDN().getName(), cert.getSerialNumber());

	        // Create KeyInfo
			x509Content = new ArrayList<Object>();
			x509Content.add(issuer);
			x509Content.add(convertJavaxX509CertificateToJavaX509Certificate(cert));
			xd = kif.newX509Data(x509Content);
			ki = kif.newKeyInfo(Collections.singletonList(xd));

			// get root element of XML and create the context to sign
	        root = originalXmlDocument.getFirstChild();
			dsc = new DOMSignContext(pk, root); 
			
			// Create the XMLSignature, but don't sign it yet.
	        signature = fac.newXMLSignature(si, ki);
	        
	        // Marshal, generate, and sign the enveloped signature.
	        signature.sign(dsc);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar el elemento Signature para el XML de cancelacion", e);
			return false;
		}
        
        return true;
    }
	
	private void error() {
		this.errorCodigo = 0L;
		this.errorDescripcion = "";
	}
	
	private void error(Long codigo, String descripcion) {
		this.errorCodigo = codigo;
		this.errorDescripcion = descripcion;
	}

	private String claveFormaPago(long idFormaPago) {
		ResultSet rs = null;
		String strQuery = "";
		
		try {
			strQuery = "SELECT forma_pago_id, forma_pago, clave_sat FROM formas_pagos WHERE forma_pago_id = " + idFormaPago;
			rs = this.conn.createStatement().executeQuery(strQuery); 
			if (rs.next()) {
				strQuery = rs.getString("clave_sat");
				if (strQuery != null && ! "".equals(strQuery.trim()))
					return strQuery;
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar obtener la clave de Forma de Pago: " + strQuery, e);
		}
		
		return "99";
	}
	
	private Connection getConn() {
		try {
			if (this.conn == null) {
				this.ctx = new InitialContext();
				this.datasource = ((DataSource)this.ctx.lookup("java:/giroDS"));
				this.conn = this.datasource.getConnection();
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar la coneccion", e);
		}

		return this.conn;
	}

	// -----------------------------
	// CONVERTIDORES
	// -----------------------------

	private java.security.cert.X509Certificate convertJavaxX509CertificateToJavaX509Certificate(javax.security.cert.X509Certificate certInput) {
		java.security.cert.X509Certificate certOutput = null;
		java.security.cert.CertificateFactory cf = null;
		ByteArrayInputStream bis = null;
		byte[] encoded = null;
		
	    try {
	        encoded = certInput.getEncoded();
	        bis = new ByteArrayInputStream(encoded);
	        cf = java.security.cert.CertificateFactory.getInstance("X.509");
	        certOutput = (java.security.cert.X509Certificate) cf.generateCertificate(bis);
	    } catch (Exception e) {
			log.error("Ocurrio un problema al convertir javax.security.cert.X509Certificate a java.security.cert.X509Certificate", e);
			certOutput = null;
	    }
	    
	    return certOutput;
	}
	
	@SuppressWarnings("unused")
	private javax.security.cert.X509Certificate convertJavaxX509CertificateToJavaX509Certificate(java.security.cert.X509Certificate certInput) {
		javax.security.cert.X509Certificate certOutput = null;
		byte[] encoded = null;
		
		try {
			encoded = certInput.getEncoded();
			certOutput = javax.security.cert.X509Certificate.getInstance(encoded);
	    } catch (Exception e) {
			log.error("Ocurrio un problema al convertir java.security.cert.X509Certificate a javax.security.cert.X509Certificate", e);
			certOutput = null;
	    }
		
	    return certOutput;
	}
	
	private org.jdom.Document convertDOMtoJDOM(org.w3c.dom.Document docInput) {
		org.jdom.Document docOutput = null;
		DOMBuilder builder = null;
		
		try {
			 builder = new DOMBuilder();
			 docOutput = builder.build(docInput);
		} catch (Exception e) {
			log.error("Ocurrio un problema al convertir org.w3c.dom.Document a org.jdom.Document", e);
			docOutput = null;
		}

		return docOutput;
	}

	private org.w3c.dom.Document convertJDOMToDOM(org.jdom.Document docInput) {
		org.w3c.dom.Document docOutput = null;
		DOMOutputter outputter = null;
		
		try {
			outputter = new DOMOutputter();
			docOutput = outputter.output(docInput);
		} catch (Exception e) {
			log.error("Ocurrio un problema al convertir org.jdom.Document a org.w3c.dom.Document", e);
			docOutput = null;
		}

		return docOutput;
	}
	
	@SuppressWarnings("unused")
	private String hex2decimal(String source) {
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
	
	// -----------------------------
	// PROPIEDADES
	// -----------------------------
	
	public String getError() {
		if (this.errorDescripcion != null) {
			if (this.errorCodigo != null && this.errorCodigo > 0L && ! this.errorDescripcion.contains(String.valueOf(this.errorCodigo)))
				return this.errorCodigo + " - " + this.errorDescripcion;
			return this.errorDescripcion;
		}
		
		return "";
	}
	
	public HashMap<String, Object> getData() {
		return this.params;
	}

	public Long getErrorCodigo() {
		return errorCodigo;
	}

	public void setErrorCodigo(Long errorCodigo) {
		this.errorCodigo = errorCodigo;
	}

	public String getErrorDescripcion() {
		return errorDescripcion;
	}

	public void setErrorDescripcion(String errorDescripcion) {
		this.errorDescripcion = errorDescripcion;
	}

	public boolean isTesting() {
		return testing;
	}
	
	public void setTesting(boolean testing) {
		this.testing = testing;
	}

	public boolean isDebugging() {
		return debugging;
	}

	public void setDebugging(boolean debugging) {
		this.debugging = debugging;
	}
	
	public String getDigestMethod() {
		return this.digestMethod;
	}
	
	public void setDigestMethodSHA1() {
		this.digestMethod = DigestMethod.SHA1;
	}
	
	public void setDigestMethodSHA256() {
		this.digestMethod = DigestMethod.SHA256;
	}
	
	public void setDigestMethodSHA512() {
		this.digestMethod = DigestMethod.SHA512;
	}
	
	public String getSoapRequestVersion() {
		return this.soapRequestVersion;
	}
	
	public void setSoapRequestVersion11() {
		this.soapRequestVersion = "1.1";
	}
	
	public void setSoapRequestVersion12() {
		this.soapRequestVersion = "1.2";
	}
}
