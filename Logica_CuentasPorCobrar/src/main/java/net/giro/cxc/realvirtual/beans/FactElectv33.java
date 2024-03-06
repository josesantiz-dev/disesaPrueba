package net.giro.cxc.realvirtual.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.giro.cxc.FEv33.Comprobante;
import net.giro.cxc.FEv33.complementos.pagos10.Pagos;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.beans.FacturaDetalleImpuestoExt;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaPagosTimbre;
import net.giro.cxc.beans.FacturaTimbre;
import net.giro.cxc.beans.FacturasRelacionadas;
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaPagosTimbreRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.cxc.logica.FacturaTimbreRem;
import net.giro.ne.beans.EmpresaCertificado;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.logica.EmpresaCertificadoRem;
import net.giro.respuesta.Respuesta;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.ssl.Base64;
import org.apache.commons.ssl.PKCS8Key;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

@TransactionManagement(TransactionManagementType.BEAN)
public class FactElectv33 {
	private static Logger log = Logger.getLogger(FactElectv33.class);
	private HashMap<String, Object> params;
	private InfoSesion infoSesion;
	private InitialContext ctx;
	private DataSource datasource;
	private Connection conn;
	private FacturaRem ifzFactura;
	private FacturaTimbreRem ifzTimbres;
	private FacturaDetalleRem ifzDetalles;
	private FacturaPagosTimbreRem ifzPagosTimbres;
	private EmpresaCertificadoRem ifzEmpCertificado;
	private TimeZone timeZone;
	private Long errorCodigo;
	private String errorDescripcion;
	private boolean debugging;
	private boolean testing;
	private boolean noTimbrar;
	private String digestMethod;
	private String soapRequestVersion;
	private List<String> stackTrace;
	// ------------------------------------------------------------------
	private boolean timbrada;
	private boolean cancelada;
	private AcuseRecepcion acuseRecepcion;
	private AcuseCancelacion acuseCancelacion;
	private AcuseEstatus acuseEstatus;
	private AcuseCFDI acuseCFDI;
	
 	public FactElectv33(InfoSesion infoSesion) {
		try {
			this.ctx = new InitialContext();
			this.ifzFactura = (FacturaRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzDetalles = (FacturaDetalleRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzTimbres = (FacturaTimbreRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaTimbreFac!net.giro.cxc.logica.FacturaTimbreRem");
			this.ifzPagosTimbres = (FacturaPagosTimbreRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosTimbreFac!net.giro.cxc.logica.FacturaPagosTimbreRem");
			this.ifzEmpCertificado = (EmpresaCertificadoRem) this.ctx.lookup("ejb:/Logica_Publico//EmpresaCertificadoFac!net.giro.plataforma.logica.EmpresaCertificadoRem");
			this.testing = false;
			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
			setDigestMethodSHA1();
			setSoapRequestVersion11();
			this.params = new HashMap<String, Object>();
			this.stackTrace = new ArrayList<String>();
			this.timbrada = false;
			this.cancelada = false;
			this.errorCodigo = 0L;
			this.errorDescripcion = "";
			this.infoSesion = infoSesion;
		} catch (Exception e) {
			log.error("Ocurrio un problema al instancias FactElectv33", e);
		}
	}

 	public boolean timbrarFactura(FacturaExt pojoFactura, long usuarioId) throws Exception {
		List<FacturaDetalleExt> listDetalles = null;
		ResultSet rsCertificados = null;
		InputStream clavePrivada = null;
		String strQuery = "";
		String password = ""; 
		String fileName = "";
		String timbreUser = "";
		String timbrePass = "";
		Long idCertificado = 0L;
		// XML ------------------------------
		Element root = null;
		// ----------------------------------
		FacturaTimbre timbre = null;
		
		try {
			// Inicializaciones
			this.timbrada = false;
			this.conn = getConn();
			this.params = new HashMap<String, Object>();
			
			// Validaciones 100
			if (! validaciones(pojoFactura))
				return false;
			
			// Asignacion de Serie y Folio si corresponde 200
			if (! asignarSerieFolio(pojoFactura)) 
				return false;
			
			// Certificado
			strQuery += "SELECT id, id_empresa, no_certificado, certificado_data, certificado_data_pem, llave_data, llave_data_pem, llave_data_enc, palabra, usuario_timbre, clave_timbre, estatus ";
			strQuery += "FROM empresa_certificado WHERE id_empresa = :idEmpresa and coalesce(nullif(estatus, ''), 'A') = 'A' order by id desc";
			strQuery = strQuery.replace(":idEmpresa", pojoFactura.getIdEmpresa().getId().toString());
			rsCertificados = this.conn.createStatement().executeQuery(strQuery); 
			if (! rsCertificados.next()) {
				error(301L, "No se pudo recuperar el Certificado cargado a la Empresa");
				return false;
			}
			
			listDetalles = this.ifzDetalles.findAllExt(pojoFactura.getId());
			if (listDetalles == null || listDetalles.isEmpty()) {
				error(302L, "No se pudo recuperar los detalles de la Factura (Conceptos)");
				return false;
			}

			this.params.put("version", pojoFactura.getVersion());
			this.params.put("uso_cfdi", pojoFactura.getUsoCfdi());
			this.params.put("cfdi_relacionado", pojoFactura.getCfdiRelacionado());
			this.params.put("cfdi_relacionado_uuid", pojoFactura.getCfdiRelacionadoUuid());
			this.params.put("cfdi_tipo_relacion", pojoFactura.getCfdiTipoRelacion());
			this.params.put("nocertificado", rsCertificados.getString("no_certificado").trim());

			fileName = pojoFactura.getId() + "_" + pojoFactura.getFolioFactura();
			timbreUser = rsCertificados.getString("usuario_timbre"); 
			timbrePass = rsCertificados.getString("clave_timbre"); 
			clavePrivada = rsCertificados.getBinaryStream("llave_data");
			password = rsCertificados.getString("palabra"); 
			idCertificado = rsCertificados.getLong("id");
			if (idCertificado == null || idCertificado <= 0L)
				idCertificado = 0L;
			
			// Generamos XML
			root = generaXML(pojoFactura, listDetalles, rsCertificados.getString("no_certificado").trim(), new String(Base64.encodeBase64(rsCertificados.getBytes("certificado_data"))));
			if (root == null) {
				error(303L, "No se pudo general el XML (CFDI)");
				return false;
			}
			
			this.params.put("rfc_emisor", pojoFactura.getIdEmpresa().getRfc().trim());
			this.params.put("rfc_receptor", pojoFactura.getRfc().trim());
			
			// Generamos CFDI, timbramos y Guardamos resultado Timbre --- 400's
			this.timbrada = generarCFDI(root, clavePrivada, password, fileName, timbreUser, timbrePass);
			timbre = comprobamosTimbre(pojoFactura, pojoFactura.getSerie(), pojoFactura.getFolio(), pojoFactura.getIdEmpresa().getId(), usuarioId);
			timbre.setPruebas((this.testing ? 1 : 0));
			timbre.setRfcEmisor(pojoFactura.getIdEmpresa().getRfc().trim());
			timbre.setRfcReceptor(pojoFactura.getRfc().trim());
			this.ifzTimbres.setInfoSesion(this.infoSesion);
			timbre = this.ifzTimbres.saveOrUpdate(timbre);
			
			// Actualizamos la factura
			pojoFactura.setIdCertificado(idCertificado);
			pojoFactura.setIdTimbre(timbre);
			this.ifzFactura.setInfoSesion(this.infoSesion);
			this.ifzFactura.update(pojoFactura);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar el comprobante para la Factura", e);
			error(300L, "No se pudo generar el Comprobante para la Factura");
			return false;
		} 
		
		return this.timbrada;
	} 
 	public boolean timbrarFactura(FacturaExt pojoFactura, List<FacturasRelacionadas> listFacturasRelacionadas, long usuarioId) throws Exception {
		List<FacturaDetalleExt> listDetalles = null;
		ResultSet rsCertificados = null;
		InputStream clavePrivada = null;
		String strQuery = "";
		String password = ""; 
		String fileName = "";
		String timbreUser = "";
		String timbrePass = "";
		Long idCertificado = 0L;
		// XML ------------------------------
		Element root = null;
		// ----------------------------------
		FacturaTimbre timbre = null;
		
		try {
			// Inicializaciones
			this.timbrada = false;
			this.conn = getConn();
			this.params = new HashMap<String, Object>();
			
			// Validaciones 100
			if (! validaciones(pojoFactura))
				return false;
			
			// Asignacion de Serie y Folio si corresponde 200
			if (! asignarSerieFolio(pojoFactura)) 
				return false;
			
			// Certificado
			strQuery += "SELECT id, id_empresa, no_certificado, certificado_data, certificado_data_pem, llave_data, llave_data_pem, llave_data_enc, palabra, usuario_timbre, clave_timbre, estatus ";
			strQuery += "FROM empresa_certificado WHERE id_empresa = :idEmpresa and coalesce(nullif(estatus, ''), 'A') = 'A' order by id desc";
			strQuery = strQuery.replace(":idEmpresa", pojoFactura.getIdEmpresa().getId().toString());
			rsCertificados = this.conn.createStatement().executeQuery(strQuery); 
			if (! rsCertificados.next()) {
				error(301L, "No se pudo recuperar el Certificado cargado a la Empresa");
				return false;
			}
			
			listDetalles = this.ifzDetalles.findAllExt(pojoFactura.getId());
			if (listDetalles == null || listDetalles.isEmpty()) {
				error(302L, "No se pudo recuperar los detalles de la Factura (Conceptos)");
				return false;
			}

			this.params.put("version", pojoFactura.getVersion());
			this.params.put("uso_cfdi", pojoFactura.getUsoCfdi());
			this.params.put("cfdi_relacionado", pojoFactura.getCfdiRelacionado());
			this.params.put("cfdi_relacionado_uuid", pojoFactura.getCfdiRelacionadoUuid());
			this.params.put("cfdi_tipo_relacion", pojoFactura.getCfdiTipoRelacion());
			this.params.put("nocertificado", rsCertificados.getString("no_certificado").trim());

			fileName = pojoFactura.getId() + "_" + pojoFactura.getFolioFactura();
			timbreUser = rsCertificados.getString("usuario_timbre"); 
			timbrePass = rsCertificados.getString("clave_timbre"); 
			clavePrivada = rsCertificados.getBinaryStream("llave_data");
			password = rsCertificados.getString("palabra"); 
			idCertificado = rsCertificados.getLong("id");
			if (idCertificado == null || idCertificado <= 0L)
				idCertificado = 0L;
			
			// Generamos XML
			root = generaXML(pojoFactura, listFacturasRelacionadas, listDetalles, rsCertificados.getString("no_certificado").trim(), new String(Base64.encodeBase64(rsCertificados.getBytes("certificado_data"))));
			if (root == null) {
				error(303L, "No se pudo general el XML (CFDI)");
				return false;
			}
			
			this.params.put("rfc_emisor", pojoFactura.getIdEmpresa().getRfc().trim());
			this.params.put("rfc_receptor", pojoFactura.getRfc().trim());
			
			// Generamos CFDI, timbramos y Guardamos resultado Timbre --- 400's
			this.timbrada = generarCFDI(root, clavePrivada, password, fileName, timbreUser, timbrePass);
			timbre = comprobamosTimbre(pojoFactura, pojoFactura.getSerie(), pojoFactura.getFolio(), pojoFactura.getIdEmpresa().getId(), usuarioId);
			timbre.setPruebas((this.testing ? 1 : 0));
			timbre.setRfcEmisor(pojoFactura.getIdEmpresa().getRfc().trim());
			timbre.setRfcReceptor(pojoFactura.getRfc().trim());
			this.ifzTimbres.setInfoSesion(this.infoSesion);
			timbre = this.ifzTimbres.saveOrUpdate(timbre);
			
			// Actualizamos la factura
			pojoFactura.setIdCertificado(idCertificado);
			pojoFactura.setIdTimbre(timbre);
			this.ifzFactura.setInfoSesion(this.infoSesion);
			this.ifzFactura.update(pojoFactura);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar el comprobante para la Factura", e);
			error(300L, "No se pudo generar el Comprobante para la Factura");
			return false;
		} 
		
		return this.timbrada;
	} 
	  
	public Respuesta timbrarPago(Comprobante comprobante, double montoComprobante, String fileName, long idEmpresa, long idUsuario) {
		Respuesta respuesta = new Respuesta();
		Element root = null;
		// ---------------------------------------------------
		EmpresaCertificado empCertificado = null;
		InputStream clavePrivada = null;
		String password = ""; 
		String timbreUser = "";
		String timbrePass = "";
		// ------------------------------
		FacturaPagosTimbre timbre = null;
		long idTimbre = 0;
		
		try {
			error();
			this.conn = getConn();
			
			// Certificado
			this.ifzEmpCertificado.setInfoSesion(this.infoSesion);
			empCertificado = this.ifzEmpCertificado.findByEmpresa(idEmpresa);
			if (empCertificado == null || empCertificado.getId() == null || empCertificado.getId() <= 0L) {
				error(301L, "Ocurrio un problema al recuperar el Certificado de la Empresa");
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo recuperar el Certificado de la Empresa");
				return respuesta;
			}
			
			clavePrivada = new ByteArrayInputStream(empCertificado.getLlaveData());
			password = empCertificado.getPalabra();
			timbreUser = empCertificado.getUsuarioTimbre(); 
			timbrePass = empCertificado.getClaveTimbre();
			
			// Generamos XML
			root = generaXML(comprobante);
			if (root == null) {
				error(303L, "No se pudo general el XML (CFDI)");
				respuesta.getErrores().setCodigoError(this.errorCodigo);
				respuesta.getErrores().setDescError(this.errorDescripcion);
				return respuesta;
			}

			this.params.put("rfc_emisor", comprobante.getEmisor().getRfc());
			this.params.put("rfc_receptor", comprobante.getReceptor().getRfc());
			
			// Generamos CFDI 400
			if (! generarCFDI(root, clavePrivada, password, fileName, timbreUser, timbrePass)) {
				respuesta.getErrores().setCodigoError(this.errorCodigo);
				respuesta.getErrores().setDescError(this.errorDescripcion);
				return respuesta;
			}
			
			// Guardamos Timbre
			idTimbre = 0L;
			timbre = comprobamosTimbre(comprobante.getSerie(), comprobante.getFolio(), montoComprobante, idEmpresa, idUsuario);
			if (timbre != null) {
				timbre.setPruebas((this.testing ? 1 : 0));
				timbre.setRfcEmisor(comprobante.getEmisor().getRfc());
				timbre.setRfcReceptor(comprobante.getReceptor().getRfc());
				this.ifzPagosTimbres.setInfoSesion(this.infoSesion);
				timbre = this.ifzPagosTimbres.saveOrUpdate(timbre);
				idTimbre = timbre.getId();
			}

			respuesta.getBody().addValor("idTimbre", idTimbre);
			respuesta.getBody().addValor("pojoTimbre", timbre);
			respuesta.getBody().addValor("serie", comprobante.getSerie());
			respuesta.getBody().addValor("folio", comprobante.getFolio());
			respuesta.getBody().addValor("codigo", this.params.get("state").toString());
			respuesta.getBody().addValor("mensaje", this.params.get("descripcion").toString());
			respuesta.getBody().addValor("xmlPrevio", this.params.get("xml").toString().getBytes());
			respuesta.getBody().addValor("cadenaOriginal", this.params.get("cadena_original").toString().getBytes());
			respuesta.getBody().addValor("sello", this.params.get("sello").toString().getBytes());
			respuesta.getBody().addValor("timbrado", 0);
			if (this.params.get("uuid") != null && ! "".equals(this.params.get("uuid").toString().trim())) {
				respuesta.getBody().addValor("timbrado", 1);
				respuesta.getBody().addValor("uuid", this.params.get("uuid").toString());
				respuesta.getBody().addValor("fechaTimbrado", this.params.get("fecha_timbrado").toString());
				respuesta.getBody().addValor("xmlTimbrado", this.params.get("cfdi").toString().getBytes());
				respuesta.getBody().addValor("selloSat", this.params.get("sello_sat").toString().getBytes());
				respuesta.getBody().addValor("certificadoSat", this.params.get("no_certificado_sat").toString().getBytes());
				respuesta.getBody().addValor("versionTimbre", this.params.get("version_timbre").toString().getBytes());
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar el comprobante para la Factura", e);
			error(300L, "No se pudo generar el Comprobante para la Factura");
			respuesta.getErrores().setCodigoError(this.errorCodigo);
			respuesta.getErrores().setDescError(this.errorDescripcion);
		} 
		
		return respuesta;
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

	// ------------------------------------------------------------------------------------------------------------------------------------

	public Respuesta timbrar(Comprobante comprobante, long idEmpresa, long idUsuario) throws Exception {
		return null;
	}
	
	public Respuesta cancelar(Long idEmpresa, String rfcEmisor, List<String> folios, long idUsuario) {
		Respuesta respuesta = null;
		long errorLevel = 300;
		long errorCodes = 0;
		// ---------------------------------------------------
		EmpresaCertificado empCertificado = null;
		PrivateKey privateKey = null;
		InputStream clavePrivada = null;
		byte[] bCertificado = null;
		byte[] bLlave = null;
		String palabra = "";
		String timbreUser = "";
		String timbrePass = "";
		// ---------------------------------------------------
		SimpleDateFormat formatter = null;
		String fileName = "";
		Element root = null;
		PKCS8EncodedKeySpec keySpec = null;
		PKCS8Key key = null;
		
		try {
			this.cancelada = false;
			respuesta = new Respuesta();
			if (folios == null || folios.isEmpty()) {
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("No se indico ningun Folio Fiscal (UUID)");
				return respuesta;
			}
			
			// Inicializaciones
			this.conn = getConn();
			this.params = new HashMap<String, Object>();
			
			// Certificado
			errorCodes = 1;
			this.ifzEmpCertificado.setInfoSesion(this.infoSesion);
			empCertificado = this.ifzEmpCertificado.findByEmpresa(idEmpresa);
			if (empCertificado == null || empCertificado.getId() == null || empCertificado.getId() <= 0L) {
				log.error("Ocurrio un problema al generar el Comprobante.\nNo se pudo recuperar el certificado para Facturacion");
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo recuperar el Certificado de la Empresa");
				return respuesta;
			}
			
			timbreUser = empCertificado.getUsuarioTimbre(); 
			timbrePass = empCertificado.getClaveTimbre();
			bCertificado = empCertificado.getCertificadoData();
			clavePrivada = new ByteArrayInputStream(empCertificado.getLlaveData());
			palabra = empCertificado.getPalabra();
			errorCodes = 2;
			
			// Instanciamos llave (.key)
			key = new PKCS8Key(clavePrivada, palabra.toCharArray());
			bLlave = key.getDecryptedBytes();
			keySpec = new PKCS8EncodedKeySpec(bLlave);
			privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

			// Generamos CFDI
			errorCodes = 3;
			root = generaXMLCancelacion(rfcEmisor, folios, bCertificado, privateKey, palabra);
			if (root == null) {
				respuesta.getErrores().setCodigoError(3L);
				respuesta.getErrores().setDescError("No se pudo generar la solicitud de Cancelacion del CFDI");
				return respuesta;
			}
			
			// Generamos XML 400
			errorLevel = 400;
			formatter = new SimpleDateFormat("yyyyMMdd");
			fileName = rfcEmisor + "_" + formatter.format(Calendar.getInstance().getTime());
			errorCodes = generarCFDICancelacion(root, fileName, timbreUser, timbrePass);
			this.cancelada = (errorCodes == 0);
			
			if (errorCodes > 0) {
				respuesta.getErrores().setCodigoError(getErrorCodigo());
				respuesta.getErrores().setDescError(getErrorDescripcion());
				return respuesta;
			}
		} catch (Exception e) {
			this.cancelada = false;
			errorCodes = errorLevel + errorCodes;
			log.error("ERROR " + errorCodes + " - Ocurrio un problema con el proceso de Cancelacion de CFDI\n" + stackTrace(), e);
			error(errorCodes, "Ocurrio un problema con el proceso de Cancelacion de CFDI");
			respuesta.getErrores().setCodigoError(5L);
			respuesta.getErrores().setDescError("Ocurrio un problema con el proceso de Cancelacion de CFDI");
		} finally {
			respuesta.getBody().addValor("cancelada", (this.cancelada ? 1 : 0));
			respuesta.getBody().addValor("pruebas", validateKey("pruebas"));
			respuesta.getBody().addValor("state", validateKey("state"));
			respuesta.getBody().addValor("descripcion", validateKey("descripcion"));
			respuesta.getBody().addValor("no_serie_firmante", validateKey("no_serie_firmante"));
			respuesta.getBody().addValor("rfc_solicitante", validateKey("rfc_solicitante"));
			respuesta.getBody().addValor("fecha_cancelacion", validateKey("fecha_cancelacion"));
			respuesta.getBody().addValor("acuse_cancelacion", validateKey("acuse_cancelacion"));
			
			try { 
				if (this.conn != null) {
					errorCodes = 600;
					if (! this.conn.isClosed())
						this.conn.close(); 
				}
			} catch(Exception e) {
				log.error("ERROR " + errorCodes + " - CERRANDO LA CONECCION", e);
				error(errorCodes, "Ocurrio un problema al cerrar conexiones");
			}
		}
	
		return respuesta;
	}
	
	public Respuesta estatus(String rfcEmisor, String rfcReceptor, String total, String uuid, Long idEmpresa) {
		Respuesta respuesta = null;
		long errorLevel = 300;
		long errorCodes = 0;
		// ---------------------------------------------------
		EmpresaCertificado empCertificado = null;
		String timbreUser = "";
		String timbrePass = "";
		// ---------------------------------------------------
		CFDIRealVirtualv33 cfdi = null;

		try {
			respuesta = new Respuesta();
			if ("".equals(valueToString(rfcEmisor)) || "".equals(valueToString(rfcReceptor)) || "".equals(valueToString(total)) || "".equals(valueToString(uuid))) {
				log.error("Ocurrio un problema al generar el Comprobante.\nParametros de validacion de CFDI insuficientes");
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("Parametros de validacion de CFDI insuficientes");
				return respuesta;
			}
			
			// Inicializaciones
			this.ifzEmpCertificado.setInfoSesion(this.infoSesion);
			empCertificado = this.ifzEmpCertificado.findByEmpresa(idEmpresa);
			if (empCertificado == null || empCertificado.getId() == null || empCertificado.getId() <= 0L) {
				log.error("Ocurrio un problema al generar el Comprobante.\nNo se pudo recuperar el certificado para Facturacion");
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo recuperar el Certificado de la Empresa");
				return respuesta;
			}
			
			timbreUser = empCertificado.getUsuarioTimbre(); 
			timbrePass = empCertificado.getClaveTimbre();
			errorCodes = 2;
			
			cfdi = new CFDIRealVirtualv33();
			cfdi.soapRequestVersion(this.soapRequestVersion);
			cfdi.debugging(this.debugging);
			cfdi.testing(this.testing);
			this.acuseEstatus = cfdi.estatus(rfcEmisor, rfcReceptor, total, uuid, timbreUser, timbrePass);
			if (this.acuseEstatus == null)
				throw new Exception("Acuse de cancelacion nulo :(");
			
			this.cancelada = true;
			if (this.acuseEstatus.getState() != 0)  {
				this.cancelada = false;
				error((long) this.acuseEstatus.getState(), this.acuseEstatus.getDescripcion());
				respuesta.getErrores().setCodigoError(getErrorCodigo());
				respuesta.getErrores().setDescError(getErrorDescripcion());
				return respuesta;
			}

			this.cancelada = "cancelado".equals(this.acuseEstatus.getEstatusCFDI().trim().toLowerCase());
			respuesta.getErrores().setCodigoError(this.acuseEstatus.getState());
			respuesta.getErrores().setDescError(this.acuseEstatus.getMensaje());
		} catch (Exception e) {
			this.cancelada = false;
			errorCodes = errorLevel + errorCodes;
			log.error("ERROR " + errorCodes + " - Ocurrio un problema con el proceso de Consulta de Estatus de CFDI\n" + stackTrace(), e);
			error(errorCodes, "Ocurrio un problema con el proceso de Cancelacion de CFDI");
			respuesta.getErrores().setCodigoError(5L);
			respuesta.getErrores().setDescError("Ocurrio un problema con el proceso de Consulta de Estatus de CFDI");
		} finally {
			respuesta.getBody().addValor("cancelada", (this.cancelada ? 1 : 0));
			respuesta.getBody().addValor("pruebas", (this.testing ? 1 : 0));
			respuesta.getBody().addValor("state", getErrorCodigo());
			respuesta.getBody().addValor("descripcion", getErrorDescripcion());
			if (this.acuseEstatus != null && this.acuseEstatus.getState() == 0) {
				respuesta.getBody().addValor("descripcion", this.acuseEstatus.getEstatusCancelacion());
				respuesta.getBody().addValor("no_serie_firmante", this.acuseEstatus.getNoSerieFirmante());
				respuesta.getBody().addValor("rfc_solicitante", this.acuseEstatus.getRfcSolicitante());
				respuesta.getBody().addValor("fecha_cancelacion", this.acuseEstatus.getFecha());
				respuesta.getBody().addValor("acuse_cancelacion", this.acuseEstatus.getAcuse());
			}
			
			try { 
				if (this.conn != null) {
					errorCodes = 600;
					if (! this.conn.isClosed())
						this.conn.close(); 
				}
			} catch(Exception e) {
				log.error("ERROR " + errorCodes + " - CERRANDO LA CONECCION", e);
				error(errorCodes, "Ocurrio un problema al cerrar conexiones");
			}
		}
	
		return respuesta;
	}
	
	public Respuesta acuseCFDI(String uuid, String fecha, Long idEmpresa) {
		Respuesta respuesta = null;
		long errorLevel = 300;
		long errorCodes = 0;
		// ---------------------------------------------------
		EmpresaCertificado empCertificado = null;
		String rfcEmisor = "";
		String timbreUser = "";
		String timbrePass = "";
		// ---------------------------------------------------
		CFDIRealVirtualv33 cfdi = null;
		Comprobante comprobante = null;
		String xmlBase = "";

		try {
			respuesta = new Respuesta();
			if ("".equals(valueToString(uuid))) {
				log.error("Ocurrio un problema al generar el Comprobante.\nParametros de validacion de CFDI insuficientes");
				respuesta.getErrores().setCodigoError(1L);
				respuesta.getErrores().setDescError("Parametros de validacion de CFDI insuficientes");
				return respuesta;
			}
			
			// Inicializaciones
			this.ifzEmpCertificado.setInfoSesion(this.infoSesion);
			empCertificado = this.ifzEmpCertificado.findByEmpresa(idEmpresa);
			if (empCertificado == null || empCertificado.getId() == null || empCertificado.getId() <= 0L) {
				log.error("Ocurrio un problema al generar el Comprobante.\nNo se pudo recuperar el certificado para Facturacion");
				respuesta.getErrores().setCodigoError(2L);
				respuesta.getErrores().setDescError("No se pudo recuperar el Certificado de la Empresa");
				return respuesta;
			}
			
			rfcEmisor = empCertificado.getIdEmpresa().getRfc();
			timbreUser = empCertificado.getUsuarioTimbre(); 
			timbrePass = empCertificado.getClaveTimbre();
			errorCodes = 2;
			
			cfdi = new CFDIRealVirtualv33();
			cfdi.soapRequestVersion(this.soapRequestVersion);
			cfdi.debugging(this.debugging);
			cfdi.testing(this.testing);
			this.acuseCFDI = cfdi.acuseCfdi(uuid, fecha, rfcEmisor, timbreUser, timbrePass); 
			if (this.acuseCFDI == null)
				throw new Exception("Acuse de cancelacion nulo :(");
			
			respuesta.getErrores().setCodigoError(this.acuseCFDI.getState());
			respuesta.getErrores().setDescError(this.acuseCFDI.getDescripcion());
			if (this.acuseCFDI != null && this.acuseCFDI.getState() == 0) {
				xmlBase = extraerXMLBase(this.acuseCFDI.getCFDI());
				comprobante = generaComprobante(this.acuseCFDI.getCFDI());
				
				populateTimbre(this.acuseCFDI.getCFDI());
				respuesta.getBody().addValor("fecha_timbrado", this.params.get("fecha_timbrado").toString());
				respuesta.getBody().addValor("version_timbre", this.params.get("version_timbre").toString());
				respuesta.getBody().addValor("uuid", this.params.get("uuid").toString());
				respuesta.getBody().addValor("sello_sat", this.params.get("sello_sat").toString());
				respuesta.getBody().addValor("no_certificado_sat", this.params.get("no_certificado_sat").toString());
			}
		} catch (Exception e) {
			this.cancelada = false;
			errorCodes = errorLevel + errorCodes;
			log.error("ERROR " + errorCodes + " - Ocurrio un problema con el proceso de Consulta de CFDI\n" + stackTrace(), e);
			error(errorCodes, "Ocurrio un problema con el proceso de Cancelacion de CFDI");
			respuesta.getErrores().setCodigoError(5L);
			respuesta.getErrores().setDescError("Ocurrio un problema con el proceso de Consulta de CFDI");
		} finally {
			respuesta.getBody().addValor("pruebas", (this.testing ? 1 : 0));
			respuesta.getBody().addValor("state", getErrorCodigo());
			respuesta.getBody().addValor("descripcion", getErrorDescripcion());
			respuesta.getBody().addValor("xml", xmlBase);
			if (comprobante != null) {
				respuesta.getBody().addValor("comprobante", comprobante);
				respuesta.getBody().addValor("xmlTimbrado", this.acuseCFDI.getCFDI());
				respuesta.getBody().addValor("cfdi", this.acuseCFDI.getCFDI().getBytes());
				respuesta.getBody().addValor("timbre", this.acuseCFDI.getCFDI().getBytes());
			}
			
			try { 
				if (this.conn != null) {
					errorCodes = 600;
					if (! this.conn.isClosed())
						this.conn.close(); 
				}
			} catch(Exception e) {
				log.error("ERROR " + errorCodes + " - CERRANDO LA CONECCION", e);
				error(errorCodes, "Ocurrio un problema al cerrar conexiones");
			}
		}
	
		return respuesta;
	}
	
	public String formarXML(Comprobante comprobante) {
		Element root = null;
		Long idEmpresa = 0L;
		EmpresaCertificado empCertificado = null;
		InputStream clavePrivada = null;
		String password = "";
		String version = "";
		// -------------------------------------------
		Document document = null;
		GeneradorCFD cfd = null;
		XMLOutputter outputter = null;
		StringWriter sw = null;
		String xmlPrevio = "";
		String xml = "";
		
		try {
			root = this.generaXML(comprobante);
			if (root == null)
				return null;
			
			// escribir un archivo con la XML previo al sello
			outputter = new XMLOutputter();
			xmlPrevio = outputter.outputString(root);
			idEmpresa = this.infoSesion.getEmpresa().getId();
			
			this.ifzEmpCertificado.setInfoSesion(this.infoSesion);
			empCertificado = this.ifzEmpCertificado.findByEmpresa(idEmpresa);
			if (empCertificado == null || empCertificado.getId() == null || empCertificado.getId() <= 0L){
				log.error("Ocurrio un problema al generar el Comprobante.\nNo se pudo recuperar el certificado para Facturacion");
				return null;
			}
			
			clavePrivada = new ByteArrayInputStream(empCertificado.getLlaveData());
			password = empCertificado.getPalabra();
			version = "3.3";
			
			try {
				document = new Document(root);
				cfd = new GeneradorCFD(document, clavePrivada, password, version);
				document.getRootElement().setAttribute(new Attribute("Sello", new String(cfd.getSelloDigital())));
			} catch (Exception ex) {
				log.error("401 - No se pudo generar la Cadena Original y/o Sello Digital para Timbrar la Factura", ex);
				error(401L, "Ocurrio un problema generando Cadena Original y/o Sello Digital para timbrar la factura");
				return null;
			} 

			// escribir un archivo con el XML para timbrar
			sw = new StringWriter();
			outputter = new XMLOutputter();
			outputter.output(document, sw);
			sw.close();
			xml = sw.toString();
		} catch (Exception e) {
			log.error("400 - Ocurrio un problema al intentar generar el XML del CFDI indicado ", e);
			if (! "".equals(xmlPrevio.trim()))
				log.info("XML Previo: " + xmlPrevio);
			error(400L, "Ocurrio un problema al intentar generar el XML del CFDI indicado");
			return null;
		}
		
		return xml;
	}
	
	// ------------------------------------------------------------------------------------------------------------------------------------
	// METODOS PRIVADOS
	// ------------------------------------------------------------------------------------------------------------------------------------

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
		DecimalFormat formatterDecimal = null;
		List<FacturaDetalleImpuestoExt> listImpuestos = null;
		double totalImpuestosRetenidos = 0;
		double totalImpuestosTraslados = 0;
		double montoAux = 0;
		double monto = 0;
		// XML ---------------------------------------------------------------
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
		Element trasladoComprobante = null;
		Element retenciones = null;
		Element retencion = null;
		Element retencionComprobante = null;
		Element relacionados = null;
		Element relacionado = null;
		// mapa de impuestos -------------------------------------------------
		LinkedHashMap<String,Element> mapTraslados = null;
		LinkedHashMap<String,Element> mapRetenciones = null;
		
		try {
			formatterDecimal = new DecimalFormat("#######0.00");
			xmlns = Namespace.getNamespace("cfdi", "http://www.sat.gob.mx/cfd/3");
			xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			attSchema = new Attribute("schemaLocation", "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd", xsi);

			// COMPROBANTE
			// ----exit----------------------------------------------------------------------------------------------------
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
			if (! "MXN XXX".contains(pojoFactura.getAbreviaturaMoneda())) //if (pojoFactura.getTipoCambio() > 1)
				root.setAttribute(new Attribute("TipoCambio", (new DecimalFormat("#######0.##")).format(pojoFactura.getTipoCambio())));
			root.setAttribute(new Attribute("CondicionesDePago", pojoFactura.getCondicionesPago().trim()));
			
			// CFDI RELACIONADO
			// --------------------------------------------------------------------------------------------------------
			if (pojoFactura.getCfdiRelacionado() == 1) {
				relacionados = new Element("CfdiRelacionados", xmlns);
				relacionados.setAttribute(new Attribute("TipoRelacion", pojoFactura.getCfdiTipoRelacion()));
				relacionado = new Element("CfdiRelacionado", xmlns);	
				relacionado.setAttribute(new Attribute("UUID", pojoFactura.getCfdiRelacionadoUuid()));
				relacionados.addContent(relacionado);
				root.addContent(relacionados);
			}
			
			// EMISOR
			// --------------------------------------------------------------------------------------------------------
			emisor = new Element("Emisor", xmlns);
			emisor.setAttribute(new Attribute("Rfc", pojoFactura.getIdEmpresa().getRfc().trim()));
			emisor.setAttribute(new Attribute("Nombre", pojoFactura.getIdEmpresa().getEmpresa().trim()));
			emisor.setAttribute(new Attribute("RegimenFiscal", pojoFactura.getIdEmpresa().getCodigoRegimenFiscal()));
			root.addContent(emisor);

			// RECEPTOR
			// --------------------------------------------------------------------------------------------------------
			receptor = new Element("Receptor", xmlns);
			receptor.setAttribute(new Attribute("Rfc", pojoFactura.getRfc().trim()));
			receptor.setAttribute(new Attribute("Nombre", pojoFactura.getCliente().trim()));
			receptor.setAttribute(new Attribute("UsoCFDI", pojoFactura.getUsoCfdi())); 
			root.addContent(receptor);
			
			// CONCEPTOS
			// --------------------------------------------------------------------------------------------------------
			mapTraslados = new LinkedHashMap<String, Element>();
			mapRetenciones = new LinkedHashMap<String, Element>();
			conceptos = new Element("Conceptos",xmlns);
			for (FacturaDetalleExt detalle : listDetalles) {
				concepto = new Element("Concepto", xmlns);
				concepto.setAttributes(
					java.util.Arrays.asList(
						new Attribute("ClaveProdServ", detalle.getClaveSat().trim()),
						new Attribute("ClaveUnidad", detalle.getIdUnidadMedida().getValor().trim()),
						new Attribute("Cantidad", formatterDecimal.format(detalle.getCantidad())),
						new Attribute("NoIdentificacion", detalle.getIdConcepto().getId().toString()),
						new Attribute("Descripcion", detalle.getIdConcepto().getDescripcion().trim()),
						new Attribute("Unidad", detalle.getIdUnidadMedida().getDescripcion().trim()),
						new Attribute("ValorUnitario", formatterDecimal.format(detalle.getCosto())),
						new Attribute("Importe", formatterDecimal.format(detalle.getImporte()))
				));
				
				// Impuestos del Concepto
				listImpuestos = detalle.getListImpuestos(); 
				if (listImpuestos != null && ! listImpuestos.isEmpty()) {
					impuestos = new Element("Impuestos", xmlns);
					traslados = new Element("Traslados", xmlns);
					retenciones = new Element("Retenciones", xmlns);
					
					for (FacturaDetalleImpuestoExt imp : listImpuestos) {
						monto = Double.parseDouble(imp.getIdImpuesto().getAtributo1().trim());
						monto = monto / 100;
						
						if ("DE".equals(imp.getIdImpuesto().getTipoCuenta())) {
							traslado = new Element("Traslado", xmlns);
							traslado.setAttribute("Base", formatterDecimal.format(imp.getBase())); // traslado.setAttribute("Base", formatterDecimal.format(detalle.getImporte()));
							traslado.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
							traslado.setAttribute("TipoFactor", "Tasa");
							traslado.setAttribute("TasaOCuota", (new DecimalFormat("#######0.000000")).format(monto));
							traslado.setAttribute("Importe", formatterDecimal.format(imp.getImporte().doubleValue()));
							traslados.addContent(traslado);
							traslado = null;
							
							if (mapTraslados.containsKey(imp.getIdImpuesto().getAtributo4())) {
								trasladoComprobante = mapTraslados.get(imp.getIdImpuesto().getAtributo4());
								montoAux = Double.parseDouble(trasladoComprobante.getAttribute("Importe").getValue());
								montoAux += imp.getImporte().doubleValue();
								trasladoComprobante.setAttribute("Importe", formatterDecimal.format(montoAux));
								mapTraslados.put(imp.getIdImpuesto().getAtributo4(), trasladoComprobante);
								totalImpuestosTraslados += imp.getImporte().doubleValue();
							} else {
								trasladoComprobante = new Element("Traslado", xmlns);
								trasladoComprobante.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
								trasladoComprobante.setAttribute("TipoFactor", "Tasa");
								trasladoComprobante.setAttribute("TasaOCuota", (new DecimalFormat("#######0.000000")).format(monto));
								trasladoComprobante.setAttribute("Importe", formatterDecimal.format(imp.getImporte().doubleValue()));
								mapTraslados.put(imp.getIdImpuesto().getAtributo4(), trasladoComprobante);
								totalImpuestosTraslados += imp.getImporte().doubleValue();
							}
						} else {
							retencion = new Element("Retencion", xmlns);
							retencion.setAttribute("Base", formatterDecimal.format(imp.getBase())); // retencion.setAttribute("Base", formatterDecimal.format(detalle.getImporte()));
							retencion.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
							retencion.setAttribute("TipoFactor", "Tasa");
							retencion.setAttribute("TasaOCuota", (new DecimalFormat("#######0.000000")).format(monto));
							retencion.setAttribute("Importe", formatterDecimal.format(imp.getImporte().doubleValue()));
							retenciones.addContent(retencion);
							retencion = null;

							if (mapRetenciones.containsKey(imp.getIdImpuesto().getAtributo4())) {
								retencionComprobante = mapRetenciones.get(imp.getIdImpuesto().getAtributo4());
								montoAux = Double.parseDouble(retencionComprobante.getAttribute("Importe").getValue());
								montoAux += imp.getImporte().doubleValue();
								retencionComprobante.setAttribute("Importe", formatterDecimal.format(montoAux));
								mapRetenciones.put(imp.getIdImpuesto().getAtributo4(), retencionComprobante);
								totalImpuestosRetenidos += imp.getImporte().doubleValue();
							} else {
								retencionComprobante = new Element("Retencion", xmlns);
								retencionComprobante.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
								retencionComprobante.setAttribute("Importe", formatterDecimal.format(imp.getImporte().doubleValue()));
								mapRetenciones.put(imp.getIdImpuesto().getAtributo4(), retencionComprobante);
								totalImpuestosRetenidos += imp.getImporte().doubleValue();
							}
						}
					}

					if (traslados.getContentSize() > 0)
						impuestos.addContent(traslados);
					if (retenciones.getContentSize() > 0)
						impuestos.addContent(retenciones);
					if (impuestos.getContentSize() > 0)
						concepto.addContent(impuestos);
				}
				
				// Añado concepto
				conceptos.addContent(concepto);
			}
			
			// Añado conceptos
			root.addContent(conceptos);
			
			// IMPUESTOS
			// --------------------------------------------------------------------------------------------------------
			impuestos = new Element("Impuestos", xmlns);
			traslados = new Element("Traslados", xmlns);
			retenciones = new Element("Retenciones", xmlns);
			
			// Comprobamos traslados
			if (mapTraslados != null && ! mapTraslados.isEmpty()) { 
				for (Entry<String, Element> item : mapTraslados.entrySet())
					traslados.addContent(item.getValue());
			}
			
			// comprobamos retenciones
			if (mapRetenciones != null && ! mapRetenciones.isEmpty()) { 
				for (Entry<String, Element> item : mapRetenciones.entrySet())
					retenciones.addContent(item.getValue());
			}
			
			// Añado Retenciones, si corresponde
			if (retenciones.getContentSize() > 0 ) {
				impuestos.addContent(retenciones);
				impuestos.setAttribute("TotalImpuestosRetenidos", formatterDecimal.format(totalImpuestosRetenidos));
			}
			
			// Añado Traslados, si corresponde
			if (traslados.getContentSize() > 0 ) {
				impuestos.addContent(traslados);
				impuestos.setAttribute("TotalImpuestosTrasladados", formatterDecimal.format(totalImpuestosTraslados));
			}
			
			root.addContent(impuestos);
		} catch (Exception e) {
			log.error("303L - No se pudo general el XML (CFDI)", e);
			root = null;
		} 
		
		return root;
	}
	private Element generaXML(FacturaExt pojoFactura, List<FacturasRelacionadas> listFacturasRelacionadas, List<FacturaDetalleExt> listDetalles, String noCertificado, String certificadoData) {
		DecimalFormat formatterDecimal = null;
		List<FacturaDetalleImpuestoExt> listImpuestos = null;
		double totalImpuestosRetenidos = 0;
		double totalImpuestosTraslados = 0;
		double montoAux = 0;
		double monto = 0;
		// XML ---------------------------------------------------------------
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
		Element trasladoComprobante = null;
		Element retenciones = null;
		Element retencion = null;
		Element retencionComprobante = null;
		Element relacionados = null;
		Element relacionado = null;
		// mapa de impuestos -------------------------------------------------
		LinkedHashMap<String,Element> mapTraslados = null;
		LinkedHashMap<String,Element> mapRetenciones = null;
		
		try {
			formatterDecimal = new DecimalFormat("#######0.00");
			xmlns = Namespace.getNamespace("cfdi", "http://www.sat.gob.mx/cfd/3");
			xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			attSchema = new Attribute("schemaLocation", "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd", xsi);

			// COMPROBANTE
			// ----exit----------------------------------------------------------------------------------------------------
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
			if (! "MXN XXX".contains(pojoFactura.getAbreviaturaMoneda())) //if (pojoFactura.getTipoCambio() > 1)
				root.setAttribute(new Attribute("TipoCambio", (new DecimalFormat("#######0.##")).format(pojoFactura.getTipoCambio())));
			root.setAttribute(new Attribute("CondicionesDePago", pojoFactura.getCondicionesPago().trim()));
			
			// CFDI RELACIONADO
			// --------------------------------------------------------------------------------------------------------
			if (pojoFactura.getCfdiRelacionado() == 1) {
				relacionados = new Element("CfdiRelacionados", xmlns);
				relacionados.setAttribute(new Attribute("TipoRelacion", pojoFactura.getCfdiTipoRelacion()));
				for (FacturasRelacionadas var : listFacturasRelacionadas) {
					relacionado = new Element("CfdiRelacionado", xmlns);	
					relacionado.setAttribute(new Attribute("UUID", var.getCfdiRelacionadoUuid()));
					relacionados.addContent(relacionado);
				}
				root.addContent(relacionados);
			}
			
			// EMISOR
			// --------------------------------------------------------------------------------------------------------
			emisor = new Element("Emisor", xmlns);
			emisor.setAttribute(new Attribute("Rfc", pojoFactura.getIdEmpresa().getRfc().trim()));
			emisor.setAttribute(new Attribute("Nombre", pojoFactura.getIdEmpresa().getEmpresa().trim()));
			emisor.setAttribute(new Attribute("RegimenFiscal", pojoFactura.getIdEmpresa().getCodigoRegimenFiscal()));
			root.addContent(emisor);

			// RECEPTOR
			// --------------------------------------------------------------------------------------------------------
			receptor = new Element("Receptor", xmlns);
			receptor.setAttribute(new Attribute("Rfc", pojoFactura.getRfc().trim()));
			receptor.setAttribute(new Attribute("Nombre", pojoFactura.getCliente().trim()));
			receptor.setAttribute(new Attribute("UsoCFDI", pojoFactura.getUsoCfdi())); 
			root.addContent(receptor);
			
			// CONCEPTOS
			// --------------------------------------------------------------------------------------------------------
			mapTraslados = new LinkedHashMap<String, Element>();
			mapRetenciones = new LinkedHashMap<String, Element>();
			conceptos = new Element("Conceptos",xmlns);
			for (FacturaDetalleExt detalle : listDetalles) {
				concepto = new Element("Concepto", xmlns);
				concepto.setAttributes(
					java.util.Arrays.asList(
						new Attribute("ClaveProdServ", detalle.getClaveSat().trim()),
						new Attribute("ClaveUnidad", detalle.getIdUnidadMedida().getValor().trim()),
						new Attribute("Cantidad", formatterDecimal.format(detalle.getCantidad())),
						new Attribute("NoIdentificacion", detalle.getIdConcepto().getId().toString()),
						new Attribute("Descripcion", detalle.getIdConcepto().getDescripcion().trim()),
						new Attribute("Unidad", detalle.getIdUnidadMedida().getDescripcion().trim()),
						new Attribute("ValorUnitario", formatterDecimal.format(detalle.getCosto())),
						new Attribute("Importe", formatterDecimal.format(detalle.getImporte()))
				));
				
				// Impuestos del Concepto
				listImpuestos = detalle.getListImpuestos(); 
				if (listImpuestos != null && ! listImpuestos.isEmpty()) {
					impuestos = new Element("Impuestos", xmlns);
					traslados = new Element("Traslados", xmlns);
					retenciones = new Element("Retenciones", xmlns);
					
					for (FacturaDetalleImpuestoExt imp : listImpuestos) {
						monto = Double.parseDouble(imp.getIdImpuesto().getAtributo1().trim());
						monto = monto / 100;
						
						if ("DE".equals(imp.getIdImpuesto().getTipoCuenta())) {
							traslado = new Element("Traslado", xmlns);
							traslado.setAttribute("Base", formatterDecimal.format(imp.getBase())); // traslado.setAttribute("Base", formatterDecimal.format(detalle.getImporte()));
							traslado.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
							traslado.setAttribute("TipoFactor", "Tasa");
							traslado.setAttribute("TasaOCuota", (new DecimalFormat("#######0.000000")).format(monto));
							traslado.setAttribute("Importe", formatterDecimal.format(imp.getImporte().doubleValue()));
							traslados.addContent(traslado);
							traslado = null;
							
							if (mapTraslados.containsKey(imp.getIdImpuesto().getAtributo4())) {
								trasladoComprobante = mapTraslados.get(imp.getIdImpuesto().getAtributo4());
								montoAux = Double.parseDouble(trasladoComprobante.getAttribute("Importe").getValue());
								montoAux += imp.getImporte().doubleValue();
								trasladoComprobante.setAttribute("Importe", formatterDecimal.format(montoAux));
								mapTraslados.put(imp.getIdImpuesto().getAtributo4(), trasladoComprobante);
								totalImpuestosTraslados += imp.getImporte().doubleValue();
							} else {
								trasladoComprobante = new Element("Traslado", xmlns);
								trasladoComprobante.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
								trasladoComprobante.setAttribute("TipoFactor", "Tasa");
								trasladoComprobante.setAttribute("TasaOCuota", (new DecimalFormat("#######0.000000")).format(monto));
								trasladoComprobante.setAttribute("Importe", formatterDecimal.format(imp.getImporte().doubleValue()));
								mapTraslados.put(imp.getIdImpuesto().getAtributo4(), trasladoComprobante);
								totalImpuestosTraslados += imp.getImporte().doubleValue();
							}
						} else {
							retencion = new Element("Retencion", xmlns);
							retencion.setAttribute("Base", formatterDecimal.format(imp.getBase())); // retencion.setAttribute("Base", formatterDecimal.format(detalle.getImporte()));
							retencion.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
							retencion.setAttribute("TipoFactor", "Tasa");
							retencion.setAttribute("TasaOCuota", (new DecimalFormat("#######0.000000")).format(monto));
							retencion.setAttribute("Importe", formatterDecimal.format(imp.getImporte().doubleValue()));
							retenciones.addContent(retencion);
							retencion = null;

							if (mapRetenciones.containsKey(imp.getIdImpuesto().getAtributo4())) {
								retencionComprobante = mapRetenciones.get(imp.getIdImpuesto().getAtributo4());
								montoAux = Double.parseDouble(retencionComprobante.getAttribute("Importe").getValue());
								montoAux += imp.getImporte().doubleValue();
								retencionComprobante.setAttribute("Importe", formatterDecimal.format(montoAux));
								mapRetenciones.put(imp.getIdImpuesto().getAtributo4(), retencionComprobante);
								totalImpuestosRetenidos += imp.getImporte().doubleValue();
							} else {
								retencionComprobante = new Element("Retencion", xmlns);
								retencionComprobante.setAttribute("Impuesto", imp.getIdImpuesto().getAtributo4());
								retencionComprobante.setAttribute("Importe", formatterDecimal.format(imp.getImporte().doubleValue()));
								mapRetenciones.put(imp.getIdImpuesto().getAtributo4(), retencionComprobante);
								totalImpuestosRetenidos += imp.getImporte().doubleValue();
							}
						}
					}

					if (traslados.getContentSize() > 0)
						impuestos.addContent(traslados);
					if (retenciones.getContentSize() > 0)
						impuestos.addContent(retenciones);
					if (impuestos.getContentSize() > 0)
						concepto.addContent(impuestos);
				}
				
				// Añado concepto
				conceptos.addContent(concepto);
			}
			
			// Añado conceptos
			root.addContent(conceptos);
			
			// IMPUESTOS
			// --------------------------------------------------------------------------------------------------------
			impuestos = new Element("Impuestos", xmlns);
			traslados = new Element("Traslados", xmlns);
			retenciones = new Element("Retenciones", xmlns);
			
			// Comprobamos traslados
			if (mapTraslados != null && ! mapTraslados.isEmpty()) { 
				for (Entry<String, Element> item : mapTraslados.entrySet())
					traslados.addContent(item.getValue());
			}
			
			// comprobamos retenciones
			if (mapRetenciones != null && ! mapRetenciones.isEmpty()) { 
				for (Entry<String, Element> item : mapRetenciones.entrySet())
					retenciones.addContent(item.getValue());
			}
			
			// Añado Retenciones, si corresponde
			if (retenciones.getContentSize() > 0 ) {
				impuestos.addContent(retenciones);
				impuestos.setAttribute("TotalImpuestosRetenidos", formatterDecimal.format(totalImpuestosRetenidos));
			}
			
			// Añado Traslados, si corresponde
			if (traslados.getContentSize() > 0 ) {
				impuestos.addContent(traslados);
				impuestos.setAttribute("TotalImpuestosTrasladados", formatterDecimal.format(totalImpuestosTraslados));
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
	
	private Element generaXMLCancelacion(String rfcEmisor, List<String> uuids, byte[] certificado, PrivateKey privateKey, String password) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		org.jdom.Document jdom = null;
		Element root = null;
		Element folios = null;
		Element folio = null;
		Namespace xmlns = null;
		Namespace xsi = null;
		Namespace xsd = null;
		// -----------------------------
		byte[] pfx = null;
		XMLOutputter outputter = null;
		FileWriter fwco = null;
		String xml = "";
		String xmlSigned = "";
		// -----------------------------
		ByteArrayInputStream inputStream = null;
		org.w3c.dom.Document doc = null;
		
		try {
			// Namespaces
			mensaje("Namespaces ... ");
			xmlns = Namespace.getNamespace("http://cancelacfd.sat.gob.mx");
			xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			xsd = Namespace.getNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
			mensaje("OK", true);
			
			// COMPROBANTE Cancelacion
			mensaje("COMPROBANTE Cancelacion (Atributos) ... ");
			root = new Element("Cancelacion", xmlns);
			root.addNamespaceDeclaration(xsi);
			root.addNamespaceDeclaration(xsd);
			root.setAttribute(new Attribute("RfcEmisor", rfcEmisor));
			root.setAttribute(new Attribute("Fecha", (formatter.format(Calendar.getInstance(this.timeZone).getTime()).replace(' ','T'))));
			mensaje("OK", true);
			
			// Genero FOLIOS y los agrego al nodo principal
			mensaje("Generando FOLIOS ... ");
			folios = new Element("Folios");
			for (String uuid : uuids) {
				folio = new Element("UUID");
				folio.addContent(uuid);
				folios.addContent(folio);
			}
			root.addContent(folios);
			mensaje("OK", true);
			
			// escribir un archivo con la XML previo a la firma
			mensaje("Guardar XML previo ... ");
			outputter = new XMLOutputter();
			xml = outputter.outputString(root);
			if (xml.contains(" xmlns=\"\""))
				xml = xml.replace(" xmlns=\"\"", "");
			fwco = new FileWriter(new File("/tmp/cancelacion-prev.xml"));
			fwco.write(xml);
			fwco.close();
			mensaje("OK", true);

			mensaje("Generar PFX ... ");
			pfx = convertToPFX(certificado, privateKey, password);
			mensaje("OK", true);

			mensaje("Firmando XML ... ");
			xmlSigned = generaSignature(xml, pfx, password);
			if (xmlSigned == null || "".equals(xmlSigned.trim())) 
				return null;
			mensaje("OK", true);

			mensaje("Guardando XML firmado ... ");
			fwco = new FileWriter(new File("/tmp/cancelacion-signed.xml"));
			fwco.write(xmlSigned);
			fwco.close();
			mensaje("OK", true);

			mensaje("Generando CFDI (Objeto XML) ... ");
			inputStream = new ByteArrayInputStream(xmlSigned.getBytes());
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
			jdom = convertDOMtoJDOM(doc);
			root = jdom.getRootElement();
			mensaje("OK", true);
		} catch (Exception e) {
			log.error("303L - No se pudo general el XML para Cancelacion (CFDI)\n", e);
			error(303L, "No se pudo general el XML para Cancelacion (CFDI)");
			root = null;
		}
		
		return root;
	}
	
	private Comprobante generaComprobante(String xml) {
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		
		try {
			jaxbContext = JAXBContext.newInstance(new Class[] { Comprobante.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (Comprobante) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar el Comprobante desde el XML", e);
		}
		
		return null;
	}
	
	private void populateTimbre(String cfdi) {
		Element rootNode = null;
		SAXBuilder builder = null;
		Document document = null;
		Element item = null;
		Element nodo = null;
		Element tfd = null;
		//String tfdString = "";
		int size = 0;
		
		try {
			if (cfdi == null || "".equals(cfdi.trim()))
				return;
			
			//tfdString = cfdi.substring(cfdi.indexOf("<cfdi:Complemento>") + 18, cfdi.indexOf("</cfdi:Complemento>"));
			builder = new SAXBuilder();
			document = builder.build(new StringReader(cfdi));
			rootNode = document.getRootElement();
			size = rootNode.getChildren().size();
			for (int idx = 0; idx < size; idx++) {
				item = (Element) rootNode.getChildren().get(idx);
				if ("TimbreFiscalDigital".equals(item.getName())) {
					tfd = item;
					break;
				}
				
				for (Object obj : item.getChildren()) {
					nodo = (Element) obj;
					if ("TimbreFiscalDigital".equals(nodo.getName())) {
						tfd = nodo;
						break;
					}
				}
			}
			
			if (tfd != null) {
				this.params.put("timbre", cfdi);
				this.params.put("fecha_timbrado", tfd.getAttributeValue("FechaTimbrado").replace("T", " "));
				this.params.put("uuid", tfd.getAttributeValue("UUID"));
				this.params.put("no_certificado_sat", tfd.getAttributeValue("NoCertificadoSAT"));
				this.params.put("sello_sat", tfd.getAttributeValue("SelloSAT"));
				this.params.put("version_timbre", tfd.getAttributeValue("Version"));
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar distribuir los datos del Timbre del CFDI", e);
		}
	}
	
	private String extraerXMLBase(String xml) {
		try {
			return xml.substring(0, xml.indexOf("<cfdi:Complemento>")) + "</cfdi:Comprobante>";
		} catch (Exception e) {
			log.error("No se pudo extraer el XML base del CFDI", e);
		}
		
		return "";
	}
	
	private boolean generarCFDI(Element root, InputStream clavePrivada, String password, String fileName, String timbreUser, String timbrePass) {
		Document document = null;
		GeneradorCFD cfd = null;
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
			} finally {
				this.params.put("state", 0L);
				this.params.put("descripcion", "");
				this.params.put("cfdi", "");
				this.params.put("timbre", "");
				this.params.put("fecha_timbrado", null);
				this.params.put("uuid", "");
				this.params.put("no_certificado_sat", "");
				this.params.put("sello_sat", "");
				this.params.put("version_timbre", "");
			}

			// escribir un archivo con el XML para timbrar
			fwxml = new FileWriter(new File("/tmp/"+ fileName  + ".xml"));
			sw = new StringWriter();
			outputter = new XMLOutputter();
			outputter.output(document, fwxml);
			outputter.output(document, sw);
			sw.close();
			fwxml.close();
			xml = sw.toString();
			
			// escribir un archivo con la Cadena Original utilizada para generar el sello
			fwco = new FileWriter(new File("/tmp/"+ fileName  + ".txt"));
			fwco.write(cfd.getCadenaOriginal());
			fwco.close();

			if (this.noTimbrar) {
				this.params.put("state", 405L);
				this.params.put("descripcion", "ERROR INTERNO: 405 - Method Not Allowed");
				return true;
			}

			try {
				cfdi = new CFDIRealVirtualv33();
				cfdi.debugging(this.debugging);
				if (this.testing)
					this.acuseRecepcion = cfdi.timbrarTest(sw.toString(), timbreUser, timbrePass);
				else
					this.acuseRecepcion = cfdi.timbrar(sw.toString(), timbreUser, timbrePass);
				if (this.acuseRecepcion == null)
					throw new Exception("Acuse de cancelacion nulo :(");
			} catch (Exception ex) {
				log.error("ERROR INTERNO: 402 - No se pudo Timbrar la Factura", ex);
				error(402L, "No se pudo Timbrar la Factura");
				return false;
			}
			
			if (this.acuseRecepcion.getState() != 0) {
				error((long) this.acuseRecepcion.getState(), this.acuseRecepcion.getDescripcion());
				return false;
			}
			
			// escribir un archivo con XML timbrado
			fwco = new FileWriter(new File("/tmp/"+ fileName  + "-timbrado.xml"));
			fwco.write(this.acuseRecepcion.getXml());
			fwco.close();
		} catch (Exception e) {
			log.error("ERROR INTERNO: 400 - No se pudo generar el XML para Timbrar la Factura", e);
			error(400L, "Ocurrio un problema al generar el XML para timbrar la factura");
			return false;
		} finally {
			this.params.put("pruebas", (this.testing ? 1 : 0));
			if (cfd != null) {
				this.params.put("xml", xml);
				this.params.put("cadena_original", cfd.getCadenaOriginal());
				this.params.put("sello", cfd.getSelloDigital());
			}
			
			if (this.acuseRecepcion != null) {
				this.params.put("state", Long.valueOf(this.acuseRecepcion.getState()));
				this.params.put("descripcion", this.acuseRecepcion.getDescripcion());
				if (this.acuseRecepcion.getState() == 0) {
					this.params.put("cfdi", this.acuseRecepcion.getXml());
					this.params.put("timbre", this.acuseRecepcion.getTimbre());
					this.params.put("fecha_timbrado", this.acuseRecepcion.getFechaTimbrado().replace("T", " "));
					this.params.put("uuid", this.acuseRecepcion.getUuid());
					this.params.put("no_certificado_sat", this.acuseRecepcion.getNoCertificadoSat());
					this.params.put("sello_sat", this.acuseRecepcion.getSelloSat());
					this.params.put("version_timbre", this.acuseRecepcion.getVersionTimbre());
				}
			}
		}
		
		return true;
	}
	
	private long generarCFDICancelacion(Element root, String fileName, String timbreUser, String timbrePass) {
		CFDI cfdi = null;
		FileWriter fwxml = null;
		XMLOutputter outp = null;
		String acuseCancelacion = "";
		
		try {
			if (root == null) {
				log.error("ERROR INTERNO: 401 - No se genero el XML para Cancelacion");
				error(401L, "No se pudo generar el XML para Cancelacion. Objeto XML nulo");
				return 1;
			}

			mensaje("Generamos y guardamos XML (String) del CFDI para envio al servicio de Cancelacion ... ");
			outp = new XMLOutputter();
			acuseCancelacion = outp.outputString(root);
			if (acuseCancelacion.contains(" xmlns=\"\""))
				acuseCancelacion = acuseCancelacion.replace(" xmlns=\"\"", "");
			// escribir un archivo con la XML previo al sello
			fwxml = new FileWriter(new File("/tmp/" + fileName  + "_cancel.xml"));
			fwxml.write(acuseCancelacion);
			fwxml.close();
			mensaje("OK", true);

			try {
				mensaje("Instanciamos y lanzamos servicio de Cancelacion ... ");
				cfdi = new CFDIRealVirtualv33();
				cfdi.soapRequestVersion(this.soapRequestVersion);
				cfdi.debugging(this.debugging);
				if (this.testing)
					this.acuseCancelacion = cfdi.cancelarTest(acuseCancelacion, timbreUser, timbrePass);
				else
					this.acuseCancelacion = cfdi.cancelar(acuseCancelacion, timbreUser, timbrePass);
				if (this.acuseCancelacion == null)
					throw new Exception("Acuse de cancelacion nulo :(");
				mensaje("OK", true);
			} catch (Exception ex) {
				log.error("ERROR INTERNO: 402 - No se pudo Timbrar la Factura", ex);
				error(402L, "No se pudo Timbrar la Factura. " + ex.getMessage());
				return 2;
			}
			
			if (this.acuseCancelacion.getState() != 0) {
				error((long) this.acuseCancelacion.getState(), this.acuseCancelacion.getDescripcion());
				return (long) this.acuseCancelacion.getState();
			}
		} catch (Exception e) {
			log.error("ERROR INTERNO: 400 - No se pudo generar el XML de cancelacion de Factura", e);
			error(400L, "Ocurrio un problema al generar el XML de cancelacion de Factura. " + e.getMessage());
			return 4;
		} finally {
			this.params.put("pruebas", (this.testing ? 1 : 0));
			this.params.put("state", -1L);
			this.params.put("descripcion", "Acuse no recibido");
			this.params.put("no_serie_firmante", "");
			this.params.put("rfc_solicitante", "");
			if (this.acuseCancelacion != null) {
				this.params.put("state", Long.valueOf(this.acuseCancelacion.getState()));
				this.params.put("descripcion", this.acuseCancelacion.getDescripcion());
				if (this.acuseCancelacion.getState() == 0) {
					this.params.put("fecha_cancelacion", this.acuseCancelacion.getFecha().replace("T", " "));
					this.params.put("no_serie_firmante", this.acuseCancelacion.getNoSerieFirmante());
					this.params.put("rfc_solicitante", this.acuseCancelacion.getRfcSolicitante());
					this.params.put("acuse_cancelacion", this.acuseCancelacion.getAcuse());
				}
			}
		}
		
		return 0;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String generaSignature(String assertion, byte[] pfx, String password) {
		String signAssertion = "";
		byte[] binAssertion = null;
		// --------------------------------------------------------------------
		ByteArrayInputStream inputStream = null;
		String providerName = "";
		String alias = "";
        List x509 = null;
        List items = null;
		// --------------------------------------------------------------------
		javax.xml.parsers.DocumentBuilderFactory dbf = null;
		org.w3c.dom.Document doc = null;
		// --------------------------------------------------------------------
		String algorithmDigestMethod = "";
		String algorithmTransform = "";
		String canonicalizationMethod = "";
		String signatureMethod = "";
        javax.xml.crypto.dsig.spec.DigestMethodParameterSpec digestMethodParamSpec = null;
        javax.xml.crypto.dsig.spec.C14NMethodParameterSpec canonicalizationMethodParamSpec = null;
        javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec signatureMethodParam = null;
		javax.xml.crypto.dsig.spec.TransformParameterSpec transformSpec = null;
		// --------------------------------------------------------------------
		XMLSignatureFactory xmlSignatureFactory = null;
		javax.xml.crypto.dsig.DigestMethod digestMethod = null;
		javax.xml.crypto.dsig.Reference ref = null;
        javax.xml.crypto.dsig.SignedInfo signedInfo = null;
        java.security.KeyStore keyStore = null;
        java.security.PrivateKey privateKey = null;
        java.security.cert.X509Certificate cert = null;
        javax.xml.crypto.dsig.keyinfo.KeyInfoFactory keyInfoFactory = null;
        javax.xml.crypto.dsig.keyinfo.KeyInfo keyInfo = null;
        javax.xml.crypto.dsig.dom.DOMSignContext dsc = null;
        javax.xml.crypto.dsig.XMLSignature signature = null;
		// --------------------------------------------------------------------
        ByteArrayOutputStream outputStream = null;
        javax.xml.transform.TransformerFactory tf = null;
        javax.xml.transform.Transformer trans = null;
        javax.xml.transform.dom.DOMSource domsource = null;
        javax.xml.transform.stream.StreamResult streamResult = null;

        try {
            canonicalizationMethod = "http://www.w3.org/2001/10/xml-exc-c14n#";//"http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
        	algorithmDigestMethod = "http://www.w3.org/2000/09/xmldsig#sha1";
            signatureMethod = "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
            algorithmTransform = "http://www.w3.org/2000/09/xmldsig#enveloped-signature";
            
            binAssertion = assertion.getBytes();
            dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            inputStream = new ByteArrayInputStream(binAssertion);
            doc = dbf.newDocumentBuilder().parse(inputStream);
            
            providerName = System.getProperty("jsr105Provider", "org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI");
            xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM", (java.security.Provider) Class.forName(providerName).newInstance());
            digestMethod = xmlSignatureFactory.newDigestMethod(algorithmDigestMethod, digestMethodParamSpec);
            ref = xmlSignatureFactory.newReference(
            		"", 
            		digestMethod, 
            		java.util.Collections.singletonList(xmlSignatureFactory.newTransform(algorithmTransform, transformSpec)), 
            		null, 
            		null);
            
            signedInfo = xmlSignatureFactory.newSignedInfo(
            		xmlSignatureFactory.newCanonicalizationMethod(canonicalizationMethod, canonicalizationMethodParamSpec), 
            		xmlSignatureFactory.newSignatureMethod(signatureMethod, signatureMethodParam), 
            		java.util.Collections.singletonList(ref));
            
            keyStore = java.security.KeyStore.getInstance("pkcs12");
            keyStore.load(new ByteArrayInputStream(pfx), password.toCharArray());
            alias = (String) keyStore.aliases().nextElement();
            privateKey = (java.security.PrivateKey) keyStore.getKey(alias, password.toCharArray());
            cert = (java.security.cert.X509Certificate) keyStore.getCertificate(alias);
            
            x509 = new ArrayList();
            keyInfoFactory = xmlSignatureFactory.getKeyInfoFactory();
            x509.add(keyInfoFactory.newX509IssuerSerial(cert.getIssuerX500Principal().getName(), cert.getSerialNumber()));
            x509.add(cert);
            
            items = new ArrayList();
            items.add(keyInfoFactory.newX509Data(x509));
            
            keyInfo = keyInfoFactory.newKeyInfo(items);
            dsc = new javax.xml.crypto.dsig.dom.DOMSignContext(privateKey, doc.getDocumentElement());
            signature = xmlSignatureFactory.newXMLSignature(signedInfo, keyInfo);
            signature.sign(dsc);
            
            outputStream = new ByteArrayOutputStream();
            tf = javax.xml.transform.TransformerFactory.newInstance();
            trans = tf.newTransformer();
            domsource = new javax.xml.transform.dom.DOMSource(doc);
            streamResult = new javax.xml.transform.stream.StreamResult(outputStream);
            trans.transform(domsource, streamResult);
            signAssertion = new String(outputStream.toByteArray());
        } catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar el elemento Signature para el XML de cancelacion\n", e);
			mensaje("Signature para el XML de cancelacion", true);
			mensaje(e.getMessage());
			return "";
        }

        return signAssertion;
	}

	private FacturaTimbre comprobamosTimbre(FacturaExt factura, String serie, String folio, long idEmpresa, long idUsuario) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		FacturaTimbre timbre = null;
		
		try {
			timbre = factura.getIdTimbre();
			if (timbre == null || timbre.getId() == null || timbre.getId() <= 0L) {
				timbre = this.ifzTimbres.comprobarTimbre(serie, folio);
				if (timbre == null) {
					timbre = new FacturaTimbre();
					timbre.setId(0L);
					timbre.setSerie(serie);
					timbre.setFolio(folio);
					timbre.setIdEmpresa(idEmpresa);
					timbre.setCreadoPor(idUsuario);
					timbre.setFechaCreacion(Calendar.getInstance().getTime());
				}
			}

			timbre.setTimbrado(0);
			timbre.setTimbradoPor(idUsuario);
			timbre.setState((long) this.params.get("state"));
			timbre.setDescripcion(this.params.get("descripcion").toString());
			timbre.setVersion(this.params.get("version").toString());
			timbre.setUsoCfdi(this.params.get("uso_cfdi").toString());
			timbre.setCfdiRelacionado((int) this.params.get("cfdi_relacionado"));
			timbre.setCfdiRelacionadoUuid(this.params.get("cfdi_relacionado_uuid").toString());
			timbre.setCfdiTipoRelacion(this.params.get("cfdi_tipo_relacion").toString());
			timbre.setNoCertificado(this.params.get("nocertificado").toString());
			timbre.setCadenaOriginal(this.params.get("cadena_original").toString());
			timbre.setSello(this.params.get("sello").toString());
			timbre.setXml(this.params.get("xml").toString().getBytes());
			timbre.setRfcEmisor(this.params.get("rfc_emisor").toString());
			timbre.setRfcReceptor(this.params.get("rfc_receptor").toString());
			timbre.setFechaModificacion(Calendar.getInstance().getTime());
			timbre.setModificadoPor(idUsuario);
			
			if (this.params.get("uuid") != null && ! "".equals(this.params.get("uuid").toString().trim())) {
				timbre.setTimbrado(1);
				timbre.setFechaTimbrado(formatter.parse(this.params.get("fecha_timbrado").toString()));
				timbre.setVersionTimbre(this.params.get("version_timbre").toString());
				timbre.setUuid(this.params.get("uuid").toString());
				timbre.setSelloSat(this.params.get("sello_sat").toString());
				timbre.setCertificadoSat(this.params.get("no_certificado_sat").toString());
				timbre.setCfdi(this.params.get("cfdi").toString().getBytes());
				timbre.setTimbre(this.params.get("timbre").toString().getBytes());
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar el registro de timbre para la Factura", e);
		}
		
		return timbre;
	}
	
	private FacturaPagosTimbre comprobamosTimbre(String serie, String folio, double monto, long idEmpresa, long idUsuario) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		FacturaPagosTimbre timbre = null;
		
		try {
			// Comprobamos timbre
			timbre = this.ifzPagosTimbres.comprobarTimbre(serie, folio);
			if (timbre == null || timbre.getId() == null || timbre.getId() <= 0L) {
				timbre = new FacturaPagosTimbre();
				timbre.setId(0L);
				timbre.setSerie(serie);
				timbre.setFolio(folio);
				timbre.setIdEmpresa(idEmpresa);
				timbre.setCreadoPor(idUsuario);
				timbre.setFechaCreacion(Calendar.getInstance().getTime());
			}

			timbre.setTimbrado(0);
			timbre.setMonto(monto);
			timbre.setRfcEmisor(this.params.get("rfc_emisor").toString());
			timbre.setRfcReceptor(this.params.get("rfc_receptor").toString());
			timbre.setCadenaOriginal(this.params.get("cadena_original").toString());
			timbre.setSello(this.params.get("sello").toString());
			timbre.setXmlPrevio(this.params.get("xml").toString().getBytes());
			timbre.setCodigo((long) this.params.get("state"));
			timbre.setMensaje(this.params.get("descripcion").toString());
			timbre.setModificadoPor(idUsuario);
			timbre.setFechaModificacion(Calendar.getInstance().getTime());
			
			if (this.params.get("uuid") != null && ! "".equals(this.params.get("uuid").toString().trim())) {
				timbre.setTimbrado(1);
				timbre.setTimbradoPor(idUsuario);
				timbre.setUuid(this.params.get("uuid").toString());
				timbre.setFecha(formatter.parse(this.params.get("fecha_timbrado").toString()));
				timbre.setSelloSat(this.params.get("sello_sat").toString());
				timbre.setCertificadoSat(this.params.get("no_certificado_sat").toString());
				timbre.setXmlTimbrado(this.params.get("cfdi").toString().getBytes());
				timbre.setVersionTimbre(this.params.get("version_timbre").toString());
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar el registro de timbre para el Pago", e);
		}
		
		return timbre;
	}
	
	private void error() {
		this.errorCodigo = 0L;
		this.errorDescripcion = "";
	}
	
	private void error(Long codigo, String descripcion) {
		this.errorCodigo = codigo;
		this.errorDescripcion = descripcion;
		mensaje("ERROR " + codigo + " - " + descripcion, true);
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

	private void mensaje(String mensaje) {
		this.mensaje(mensaje, false);
	}
	
	private void mensaje(String mensaje, boolean append) {
		if (this.stackTrace == null)
			this.stackTrace = new ArrayList<String>();
		
		// Añadimos mensaje
		if (! append || (append && this.stackTrace.isEmpty())) {
			this.stackTrace.add(mensaje);
			return;
		}
		
		// Reemplazamos el ultimo mensaje
		mensaje = this.stackTrace.get(this.stackTrace.size() - 1) + mensaje;
		this.stackTrace.set((this.stackTrace.size() - 1), mensaje);
	}
	
	private String stackTrace() {
		return "\nSTACKTRACE\n" + StringUtils.join(this.stackTrace, "\n") + "\n";
	}

	private String validateKey(String key) {
		return (this.params.containsKey(key) ? valueToString(this.params.get(key)) : "");
	}
	
	private String valueToString(Object value) {
		return (value != null) ? value.toString().trim() : "";
	}
	
	// ------------------------------------------------------------------------------------------------------------------------------------
	// CONVERTIDORES
	// ------------------------------------------------------------------------------------------------------------------------------------
	
	private byte[] convertToPFX(byte[] certificate, PrivateKey privateKey, String newPassword) {
        java.security.cert.Certificate[] newCertificates = null;
        java.security.cert.Certificate x509Rsa = null;
        java.security.cert.CertificateFactory certificateFactory = null;
        java.security.cert.X509Certificate x509 = null;
        java.security.Signature signer = null;
        String displayName = "";
        String strSubject = "";
		// -----------------------------------------------
        boolean state = false;
        byte[] dataSigned = null;
        byte[] data = { 1, 2, 3 };
        byte[] pkcs12 = null;
		// -----------------------------------------------
        java.security.KeyStore store = null;
        ByteArrayOutputStream outStream = null;
		
        try {
            certificateFactory = java.security.cert.CertificateFactory.getInstance("X.509");
            x509 = (java.security.cert.X509Certificate) certificateFactory.generateCertificate(new java.io.ByteArrayInputStream(certificate));
            x509Rsa = certificateFactory.generateCertificate(new java.io.ByteArrayInputStream(certificate));

            strSubject = x509.getSubjectDN().getName();
            displayName = GetSubjectName("CN", strSubject);
            if (displayName.equalsIgnoreCase(""))
                displayName = GetSubjectName("OID.2.5.4.45", strSubject);
            if (displayName.equalsIgnoreCase(""))
                displayName = GetSubjectName("O", strSubject);
            if (displayName.equalsIgnoreCase("")) 
                displayName = GetSubjectName("OU", strSubject);
            newCertificates = new java.security.cert.Certificate[1];
            newCertificates[0] = x509Rsa;
        } catch (Exception ee) {
            log.error("ERROR 001 - Ocurrio un problema al generar PFX", ee);
            mensaje("ERROR 001 - Ocurrio un problema al generar PFX", true);
            mensaje(ee.getMessage());
        }

        if (newCertificates == null)
            return null;
        if (displayName.equalsIgnoreCase("")) 
            return null;

        try {
            signer = java.security.Signature.getInstance("SHA1withRSA");
            signer.initSign(privateKey);
            signer.update(data);
            dataSigned = signer.sign();
            signer.initVerify(x509Rsa);
            signer.update(data);
            state = signer.verify(dataSigned);
        } catch (Exception ee) {
            log.error("ERROR 002 - Ocurrio un problema al generar PFX", ee);
            mensaje("ERROR 002 - Ocurrio un problema al generar PFX", true);
            mensaje(ee.getMessage());
        }
        
        if (! state) 
            return null;
        
        try {
            store = java.security.KeyStore.getInstance("pkcs12");
            store.load(null, null);
            store.setKeyEntry(displayName, privateKey, newPassword.toCharArray(), newCertificates);

            outStream = new ByteArrayOutputStream();
            store.store(outStream, newPassword.toCharArray());
            pkcs12 = outStream.toByteArray();
        } catch (Exception ee) {
            log.error("ERROR 003 - Ocurrio un problema al generar PFX", ee);
            mensaje("ERROR 003 - Ocurrio un problema al generar PFX", true);
            mensaje(ee.getMessage());
        }

        return pkcs12;
	}
	
	private String GetSubjectName(String oid, String strSubject) {
		String systemName = "";
		String strValue = "";
		int pos = 0;
		int val = 0;
		
        pos = strSubject.indexOf(oid + "=", 0);
        if (pos < 0)
            return "";

        strValue = strSubject.substring(pos + oid.length() + 1);
        if (strValue.charAt(0) == '"') {
            pos = strValue.indexOf("\"", 1);
            if (pos >= 0)
                strValue = strValue.substring(1, pos);
        } else {
            pos = strValue.indexOf(",");
            if (pos >= 0) {
                strValue = strValue.substring(0, pos);
            }
        }

        systemName = System.getProperty("os.name").toUpperCase();
        if (systemName.charAt(0) == 'W') {
            for (int n = 0; n < strValue.length(); n++) {
                val = strValue.charAt(n);
                if (val == 209)
                    strValue = strValue.replace(strValue.charAt(n), '¥');
                if (val == 241)
                    strValue = strValue.replace(strValue.charAt(n), '¤');
            }
        } else {
            try {
                byte[] nx = strValue.getBytes("utf-8");
                strValue = new String(nx);
            } catch (Exception ee) {}
        }
        return strValue;
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

	// ------------------------------------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------------------------------------------------------

	public boolean getTimbrada() {
		return this.timbrada;
	}

	public boolean getCancelada() {
		return this.cancelada;
	}
	
	public AcuseRecepcion getAcuse() {
		return this.acuseRecepcion;
	}
	
	public AcuseCancelacion getAcuseCancelacion() {
		return this.acuseCancelacion;
	}
	
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
		if (this.errorCodigo == null)
			this.errorCodigo = 0L;
		return this.errorCodigo;
	}

	public void setErrorCodigo(Long errorCodigo) {
		if (errorCodigo == null)
			errorCodigo = 0L;
		this.errorCodigo = errorCodigo;
	}

	public String getErrorDescripcion() {
		if (this.errorDescripcion == null)
			this.errorDescripcion = "";
		return this.errorDescripcion;
	}

	public void setErrorDescripcion(String errorDescripcion) {
		if (errorDescripcion == null)
			errorDescripcion = "";
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

	public boolean getNoTimbrar() {
		return noTimbrar;
	}

	public void setNoTimbrar(boolean noTimbrar) {
		this.noTimbrar = noTimbrar;
	}
}
