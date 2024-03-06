package net.giro.cxc.realvirtual.beans;

import net.giro.cxc.realvirtual.ws.CancelTestResponse;
import net.giro.cxc.realvirtual.ws.CancelTicketResponse;
import net.giro.cxc.realvirtual.ws.EstatusCFDIResponse;
import net.giro.cxc.realvirtual.ws.GetTicketResponse;
import net.giro.cxc.realvirtual.ws.SchemaCfdResponse;
import net.giro.cxc.realvirtual.ws.StructCancel;
import net.giro.cxc.realvirtual.ws.StructCfd;
import net.giro.cxc.realvirtual.ws.TestCfd33Response;
import net.giro.cxc.realvirtual.ws.consultascfdireal.ConsultarCfdiUUIDResponse;
import net.giro.cxc.realvirtual.ws.consultascfdireal.StructCfdi;

import org.apache.commons.ssl.Base64;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.InputStreamReader;

public class CFDIRealVirtualv33 implements CFDI {
	private static Logger log = Logger.getLogger(CFDIRealVirtualv33.class);
	private int httpCode;
	private String httpMensaje;
	private String xmlUsado;
	private boolean debugging;
	@SuppressWarnings("unused")
	private boolean testing;
	private String soapRequestVersion;
	private String stackTrace;
	private String stackTraceInterno;
	private String wsTimbrado;
	private String wsTimbradoRequest;
	private String wsTimbradoRequest12;
	private String wsConsultas;
	private String wsConsultasRequest;
	private String wsConsultasRequest12;
	
	public CFDIRealVirtualv33() {
		// Timbrado, Cancelacion y Estatus 
		this.wsTimbrado = "http://generacfdi.com.mx/rvltimbrado/service1.asmx";
		this.wsTimbradoRequest = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Header><AuthSoapHd xmlns=\"http://tempuri.org/\"><strUserName>?timbreUser</strUserName><strPassword>?timbrePass</strPassword></AuthSoapHd></soap:Header><soap:Body>?body</soap:Body></soap:Envelope>";
		this.wsTimbradoRequest12 = "<soap12:Envelope xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soap12:Header><AuthSoapHd xmlns=\"http://tempuri.org/\"><strUserName>?timbreUser</strUserName><strPassword>?timbrePass</strPassword></AuthSoapHd></soap12:Header><soap12:Body>?body</soap12:Body></soap12:Envelope>";
		// Consulta y Recuperacion de CFDI 
		this.wsConsultas = "http://generacfdi.com.mx/ws_consultas_cfd_real/Service1.asmx";
		this.wsConsultasRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\"><soapenv:Header/><soapenv:Body>?body</soapenv:Body></soapenv:Envelope>";
		this.wsConsultasRequest12 = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tem=\"http://tempuri.org/\"><soap:Header/><soap:Body>?body</soap:Body></soap:Envelope>";
	}
	
	@Override
	public void debugging(boolean debugging) {
		this.debugging = debugging;
	}
	
	@Override
	public void testing(boolean testing) {
		this.testing = testing;
	}
	
	@Override
	public void soapRequestVersion(String soapRequestVersion) {
		this.soapRequestVersion = soapRequestVersion;
	}
	
	// ------------------------------------------------------------------------------------------
	// PRODUCCION
	// ------------------------------------------------------------------------------------------
	
	@Override
	public AcuseRecepcion timbrar(String xml, String timbreUser, String timbrePass) {
		GetTicketResponse ticket = null;
		AcuseRecepcion acuseRecepcion = null;
		StructCfd struct = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		boolean printStackTrace = false;
		// ------------------------------------
		String soapResponse = "";
		String xmlCfdi;
		String xmlTimbre;
		
		try {
			stackTrace();
			this.testing = false;
			this.xmlUsado = xml;
			stackTrace("Generando peticion ... ");
			body = "<GetTicket xmlns=\"http://tempuri.org/\"><base64Cfd>?base64Cfd</base64Cfd></GetTicket>";
			body = body.replace("?base64Cfd", Base64.encodeBase64String(xml.getBytes()));
			stackTrace("OK", false);
			stackTrace("peticion :\n" + body);
			
			stackTrace("Enviando peticion ... ");
			respuesta = http(body, timbreUser, timbrePass, this.wsTimbrado, null);
			if (respuesta == null || "".equals(respuesta.trim())) {
	    		printStackTrace();
				acuseRecepcion = new AcuseRecepcion();
				acuseRecepcion.setState(this.httpCode);
				acuseRecepcion.setDescripcion(this.httpMensaje);
	    		printStackTrace = true;
				return acuseRecepcion;
			}
			stackTrace("OK", false);
			stackTrace("respuesta :\n" + respuesta);
			stackTrace(this.stackTraceInterno);

			stackTrace("Procesando respuesta ... ");
			soapResponse = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { GetTicketResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ticket = (GetTicketResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapResponse.getBytes()));
			struct = ticket.getGetTicketResult();
			struct.validaMensaje();
			stackTrace("OK", false);

			stackTrace("Validando respuesta ... ");
			if (struct.getState() == 0) {
				xmlCfdi = new String(Base64.decodeBase64(struct.getCfdi()));
				xmlTimbre = new String(Base64.decodeBase64(struct.getTimbre()));
				
				struct.setCfdi(xmlCfdi);
				struct.setTimbre(xmlTimbre);
				struct.setDescripcion("");
				struct.setState(0);
				stackTrace("OK", false);
			} else if (struct.getState() == 307) {
				xmlCfdi = new String(Base64.decodeBase64(struct.getCfdi()));
				xmlTimbre = "";
				if (xmlCfdi.contains("><?xml")) {
					xmlCfdi = xmlCfdi.substring(xmlCfdi.indexOf("><?xml") + 1);
					xmlCfdi = xmlCfdi.replace("</cfdi:Complemento></cfdi:Comprobante></cfdi:Complemento></cfdi:Comprobante>", "</cfdi:Complemento></cfdi:Comprobante>");
				}
				
				struct.setCfdi(xmlCfdi);
				struct.setTimbre(xmlTimbre);
				struct.setDescripcion("");
				struct.setState(0);
				stackTrace("ERROR", false);
				stackTrace(struct.getState() + " - " + struct.getDescripcion() + "\nXML :\n" + this.xmlUsado);
			} else if (struct.getState() == 500) {
				struct.setTimbre(struct.getDescripcion().substring(struct.getDescripcion().indexOf("<?xml version=\"1.0\" encoding=\"utf-8\"?>")));
				struct.setDescripcion(struct.getDescripcion().substring(0, struct.getDescripcion().indexOf("<?xml version=\"1.0\" encoding=\"utf-8\"?>")).trim());
				struct.setCfdi(xml.replace("</cfdi:Comprobante>", "<cfdi:Complemento>" + struct.getTimbre().replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "")) + "</cfdi:Complemento></cfdi:Comprobante>");
				struct.setState(0);
				stackTrace("ERROR", false);
				stackTrace(struct.getState() + " - " + struct.getDescripcion() + "\nXML :\n" + this.xmlUsado);
			} else {
				acuseRecepcion = new AcuseRecepcion();
				acuseRecepcion.setState(struct.getState());
				acuseRecepcion.setDescripcion(struct.getDescripcion());
				stackTrace("ERROR", false);
				stackTrace(struct.getState() + " - " + struct.getDescripcion() + "\nXML :\n" + this.xmlUsado);
	    		printStackTrace = true;
				return acuseRecepcion;
			}

			// Leemos el TIMBRE para recuperar UUID, Fecha timbrado, Sello SAT y No Certificado SAT.
			acuseRecepcion = acuseRecepcion(struct);
		} catch (Exception ex) { 
			log.error("Ocurrio un problema al intentar Timbrar el CFDI", ex);
			acuseRecepcion.setDescripcion("Ocurrio un problema al intentar Timbrar el CFDI\n" + ex.toString());
			acuseRecepcion.setState(-2);
			printStackTrace = true;
	    } finally {
	    	if (this.debugging || printStackTrace)
		    	printStackTrace();
	    	log.info("\n\n### ACUSE ###\n" + acuseRecepcion.toString());
	    }
		
		return acuseRecepcion;
	}
	
	@Override
	public AcuseCancelacion cancelar(String xml, String timbreUser, String timbrePass) {
		AcuseCancelacion acuseCancelacion = null;
		CancelTicketResponse cancelTicket = null;
		StructCancel struct = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		String soapxml = "";
		
		try {
			this.testing = false;
			this.xmlUsado = xml;
			acuseCancelacion = new AcuseCancelacion();
			if (this.debugging) log.info("Generando peticion ... ");
			body = "<CancelTicket xmlns=\"http://tempuri.org/\"><base64Cfd>?base64Cfd</base64Cfd></CancelTicket>";
			body = body.replace("?base64Cfd", Base64.encodeBase64String(xml.getBytes()));
			
			if (this.debugging) log.info("Enviando peticion ... ");
			respuesta = http(body, timbreUser, timbrePass, this.wsTimbrado, null);
			if (respuesta == null || "".equals(respuesta.trim())) {
				acuseCancelacion.setState(this.httpCode);
				acuseCancelacion.setDescripcion(this.httpMensaje);
				return acuseCancelacion;
			}

			if (this.debugging) log.info("Procesando respuesta ... ");
			soapxml = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { CancelTicketResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			cancelTicket = (CancelTicketResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapxml.getBytes()));
			struct = cancelTicket.getCancelTicketResult();

			if (this.debugging) log.info("Leemos acuse ... ");
			if (struct.getState() != 0) {
				if (this.debugging) log.info("Estatus " + struct.getState() + " :( ... ");
				acuseCancelacion.setState(struct.getState());
				acuseCancelacion.setDescripcion(struct.getDescripcion());
				return acuseCancelacion;
			}

			if (this.debugging) log.info("Estatus 0 :) ... ");
			acuseCancelacion = acuseCancelacion(xml, struct);
			acuseCancelacion.setState(struct.getState());
			acuseCancelacion.setDescripcion(struct.getDescripcion());
		} catch (Exception ex) { 
			log.error("Ocurrio un problema al intentar Cancelar el CFDI indicado", ex);
			acuseCancelacion.setState(-2);
			acuseCancelacion.setDescripcion(ex.toString());
	    } finally {
	    	log.info("CANCELAR :: " + acuseCancelacion.toString());
	    }
		
		return acuseCancelacion;
	}
	
	@Override
	public AcuseEstatus estatus(String rfcEmisor, String rfcReceptor, String total, String uuid, String timbreUser, String timbrePass) {
		AcuseEstatus acuseEstatus = null;
		EstatusCFDIResponse estatusTicket = null;
		StructCancel struct = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		String soapxml = "";
		String expresionImpresa = "";
		
		try {
			this.testing = false;
			acuseEstatus = new AcuseEstatus();
			if (this.debugging) log.info("Generando peticion ... ");
			expresionImpresa = "?re=:rfcEmisor&rr=:rfcReceptor&tt=:total&id=:uuid";
			expresionImpresa = expresionImpresa.replace(":rfcEmisor", rfcEmisor).replace(":rfcReceptor", rfcReceptor).replace(":total", total).replace(":uuid", uuid);
			body = "<EstatusCFDI xmlns=\"http://tempuri.org/\"><RfcEmisor>?rfcEmisor</RfcEmisor><RfcReceptor>?rfcReceptor</RfcReceptor><total>?total</total><uuid>?uuid</uuid></EstatusCFDI>";
			body = body.replace("?rfcEmisor", rfcEmisor).replace("?rfcReceptor", rfcReceptor).replace("?total", total).replace("?uuid", uuid);
			
			if (this.debugging) log.info("Enviando peticion ... ");
			respuesta = http(body, timbreUser, timbrePass, this.wsTimbrado, null);
			if (respuesta == null || "".equals(respuesta.trim())) {
				acuseEstatus.setState(this.httpCode);
				acuseEstatus.setDescripcion(this.httpMensaje);
				return acuseEstatus;
			}

			if (this.debugging) log.info("Procesando respuesta ... ");
			soapxml = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { EstatusCFDIResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			estatusTicket = (EstatusCFDIResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapxml.getBytes()));
			struct = estatusTicket.getEstatusCFDIResult();

			if (this.debugging) log.info("Leemos acuse ... ");
			if (struct.getState() != 0) {
				if (this.debugging) log.info("Estatus " + struct.getState() + " :( ... ");
				acuseEstatus.setState(struct.getState());
				acuseEstatus.setDescripcion(struct.getDescripcion() + "\nexpresionImpresa: " + expresionImpresa);
				acuseEstatus.setExpresionImpresa(expresionImpresa);
				return acuseEstatus;
			}

			if (this.debugging) log.info("Estatus 0 :) ... ");
			acuseEstatus = acuseEstatus(struct, expresionImpresa);
			acuseEstatus.setState(struct.getState());
			acuseEstatus.setDescripcion(struct.getDescripcion());
		} catch (Exception ex) { 
			log.error("Ocurrio un problema al intentar Cancelar el CFDI indicado", ex);
			acuseEstatus.setState(-2);
			acuseEstatus.setDescripcion(ex.toString() + "\nexpresionImpresa: " + expresionImpresa);
			acuseEstatus.setExpresionImpresa(expresionImpresa);
	    } finally {
	    	log.info("ESTATUS :: " + acuseEstatus.toString());
	    }
		
		return acuseEstatus;
	}
	
	public AcuseCFDI acuseCfdi(String uuid, String fecha, String rfcEmisor, String timbreUser, String timbrePass) {
		ConsultarCfdiUUIDResponse ticket = null;
		AcuseCFDI acuse = null;
		StructCfdi struct = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		boolean printStackTrace = false;
		// ------------------------------------
		String soapResponse = "";
		
		try {
			stackTrace();
			this.testing = false;
			stackTrace("Generando peticion ... ");
			body = "<tem:Consultar_cfdi_UUID><tem:UUID>?uuid</tem:UUID><tem:fecha>?fecha</tem:fecha><tem:Usuario_admin>?timbreUser</tem:Usuario_admin><tem:Clave_admin>?timbrePass</tem:Clave_admin><tem:Rfc_Admin>?rfcEmisor</tem:Rfc_Admin></tem:Consultar_cfdi_UUID>";
			body = body.replace("?uuid", uuid).replace("?fecha", fecha).replace("?timbreUser", timbreUser).replace("?timbrePass", timbrePass).replace(":?rfcEmisor", rfcEmisor);
			stackTrace("OK", false);
			stackTrace("peticion :\n" + body);
			
			stackTrace("Enviando peticion ... ");
			respuesta = http(body, timbreUser, timbrePass, this.wsConsultas, rfcEmisor);
			if (respuesta == null || "".equals(respuesta.trim())) {
	    		printStackTrace();
				acuse = new AcuseCFDI();
				acuse.setState(this.httpCode);
				acuse.setDescripcion(this.httpMensaje);
	    		printStackTrace = true;
				return acuse;
			}
			stackTrace("OK", false);
			stackTrace("respuesta :\n" + respuesta);
			stackTrace(this.stackTraceInterno);

			stackTrace("Procesando respuesta ... ");
			jaxbContext = JAXBContext.newInstance(new Class[] { ConsultarCfdiUUIDResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			soapResponse = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			ticket = (ConsultarCfdiUUIDResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapResponse.getBytes()));
			struct = ticket.getConsultarCfdiUUIDResult();
			acuse = acuseCFDI(struct);
			stackTrace("OK", false);

			stackTrace("Validando respuesta ... ");
			if (acuse.getState() != 0) {
				stackTrace("ERROR", false);
				stackTrace(acuse.getState() + " - " + acuse.getDescripcion() + "\nUUID :\n" + uuid);
	    		printStackTrace = true;
				return acuse;
			}
		} catch (Exception ex) { 
			log.error("Ocurrio un problema al intentar recuperar el CFDI", ex);
			acuse.setDescripcion("Ocurrio un problema al intentar recuperar el CFDI\n" + ex.toString());
			acuse.setState(-2);
			printStackTrace = true;
	    } finally {
	    	if (this.debugging || printStackTrace)
		    	printStackTrace();
	    	log.info("\n\n### CFDI ###\n" + acuse.toString());
	    }
		
		return acuse;
	}
	
	@Override
	public AcuseSchema schema(String xml, String timbreUser, String timbrePass) {
		AcuseSchema acuseSchema = null;
		SchemaCfdResponse schemaCfd = null;
		StructCfd structCfd = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		String soapxml = "";
		
		try {
			this.testing = false;
			this.xmlUsado = xml;
			acuseSchema = new AcuseSchema();
			if (this.debugging)
				log.info("Generando peticion ... ");
			body = "<tem:SchemaCfd><tem:base64Cfd>?base64Cfd</tem:base64Cfd></tem:SchemaCfd>";
			body = body.replace("?base64Cfd", Base64.encodeBase64String(xml.getBytes()));
			
			if (this.debugging)
				log.info("Enviamos peticion ... ");
			respuesta = http(body, timbreUser, timbrePass, this.wsTimbrado, null);
			if (respuesta == null || "".equals(respuesta.trim())) {
				acuseSchema.setState(this.httpCode);
				acuseSchema.setDescripcion(this.httpMensaje);
				return acuseSchema;
			}

			if (this.debugging)
				log.info("Procesando respuesta ... ");
			soapxml = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { SchemaCfdResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			schemaCfd = (SchemaCfdResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapxml.getBytes()));
			if (schemaCfd.getSchemaCfdResult().getState() == 0) {
				acuseSchema.setState(schemaCfd.getSchemaCfdResult().getState());
				acuseSchema.setDescripcion(schemaCfd.getSchemaCfdResult().getDescripcion());
				acuseSchema.setCfdi(xml);
				acuseSchema.setRfcEmisor(schemaCfd.getSchemaCfdResult().getRfcEmisor());
				acuseSchema.setRfcReceptor(schemaCfd.getSchemaCfdResult().getRfcReceptor());
				acuseSchema.setVersion(schemaCfd.getSchemaCfdResult().getVersion());
				acuseSchema.setSerie(schemaCfd.getSchemaCfdResult().getSerie());
				acuseSchema.setFolio(schemaCfd.getSchemaCfdResult().getFolio());
				acuseSchema.setFechaExpedicion(schemaCfd.getSchemaCfdResult().getFechaExpedicion());
				acuseSchema.setMontoOperacion(schemaCfd.getSchemaCfdResult().getMontoOperacion());
				acuseSchema.setMontoImpuesto(schemaCfd.getSchemaCfdResult().getMontoImpuesto());
				acuseSchema.setTipoComprobante(schemaCfd.getSchemaCfdResult().getTipoComprobante());
				acuseSchema.setCadena(schemaCfd.getSchemaCfdResult().getCadena());
				acuseSchema.setFirma(schemaCfd.getSchemaCfdResult().getFirma());
				acuseSchema.setSerieCertificado(schemaCfd.getSchemaCfdResult().getSerieCertificado());
				acuseSchema.setTimbre(schemaCfd.getSchemaCfdResult().getTimbre());
			} else {
				structCfd = schemaCfd.getSchemaCfdResult();
				acuseSchema.setState(structCfd.getState());
				acuseSchema.setDescripcion(structCfd.getDescripcion());
			}
		} catch (Exception ex) { 
			log.error("Ocurrio un problema al intentar Comprobar el esquema del CFDI", ex);
			acuseSchema.setState(-2);
			acuseSchema.setDescripcion(ex.toString());
	    } finally {
	    	log.info(acuseSchema.toString());
	    }
		
		return acuseSchema;
	}
	
	// ------------------------------------------------------------------------------------------
	// TESTING 
	// ------------------------------------------------------------------------------------------
	
	@Override
	public AcuseRecepcion timbrarTest(String xml, String timbreUser, String timbrePass) {
		TestCfd33Response ticket = null;
		AcuseRecepcion acuseRecepcion = null;
		StructCfd timbre = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		// ------------------------------------
		String soapResponse = "";
		String xmlCfdi;
		String xmlTimbre;
		
		try {
			this.testing = true;
			this.xmlUsado = xml;
			timbreUser = "fgomez";
			timbrePass = "12121212";
			
			log.info("Generando peticion ... ");
			body = "<TestCfd33 xmlns=\"http://tempuri.org/\"><base64Cfd>" + Base64.encodeBase64String(xml.getBytes()) + "</base64Cfd></TestCfd33>";

			log.info("Enviamos peticion ... ");
			respuesta = http(body, timbreUser, timbrePass, this.wsTimbrado, null);
			if (respuesta == null || "".equals(respuesta.trim())) {
				acuseRecepcion = new AcuseRecepcion();
				acuseRecepcion.setState(this.httpCode);
				acuseRecepcion.setDescripcion(this.httpMensaje);
				return acuseRecepcion;
			}

			log.info("Procesando respuesta ... ");
			soapResponse = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { TestCfd33Response.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ticket = (TestCfd33Response) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapResponse.getBytes()));
			timbre = ticket.getTestCfd33Result();
			timbre.validaMensaje();
			
			if (timbre.getState() == 0) {
				xmlCfdi = new String(Base64.decodeBase64(timbre.getCfdi()));
				xmlTimbre = new String(Base64.decodeBase64(timbre.getTimbre()));
				
				timbre.setCfdi(xmlCfdi);
				timbre.setTimbre(xmlTimbre);
				timbre.setDescripcion("");
				timbre.setState(0);
				log.info("OK" + "\nXML :\n" + this.xmlUsado);
			} else if (timbre.getState() == 307) {
				xmlCfdi = new String(Base64.decodeBase64(timbre.getCfdi()));
				xmlTimbre = "";
				if (xmlCfdi.contains("><?xml")) {
					xmlCfdi = xmlCfdi.substring(xmlCfdi.indexOf("><?xml") + 1);
					xmlCfdi = xmlCfdi.replace("</cfdi:Complemento></cfdi:Comprobante></cfdi:Complemento></cfdi:Comprobante>", "</cfdi:Complemento></cfdi:Comprobante>");
				}
				
				timbre.setCfdi(xmlCfdi);
				timbre.setTimbre(xmlTimbre);
				timbre.setDescripcion("");
				timbre.setState(0);
				log.info("307 :: timbre previo" + "\nXML :\n" + this.xmlUsado);
			} else if (timbre.getState() == 500) {
				timbre.setTimbre(timbre.getDescripcion().substring(timbre.getDescripcion().indexOf("<?xml version=\"1.0\" encoding=\"utf-8\"?>")));
				timbre.setDescripcion(timbre.getDescripcion().substring(0, timbre.getDescripcion().indexOf("<?xml version=\"1.0\" encoding=\"utf-8\"?>")).trim());
				timbre.setCfdi(xml.replace("</cfdi:Comprobante>", "<cfdi:Complemento>" + timbre.getTimbre().replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "")) + "</cfdi:Complemento></cfdi:Comprobante>");
				timbre.setState(0);
				log.info("500 :: timbre previo" + "\nXML :\n" + this.xmlUsado);
			} else {
				log.info("\nXML :\n" + this.xmlUsado + "\n\n" + timbre.getState() + " - " + timbre.getDescripcion() + "\n");
				acuseRecepcion = new AcuseRecepcion();
				acuseRecepcion.setState(timbre.getState());
				acuseRecepcion.setDescripcion(timbre.getDescripcion());
				return acuseRecepcion;
			}

			// Leemos el TIMBRE para recuperar UUID, Fecha timbrado, Sello SAT y No Certificado SAT.
			acuseRecepcion = acuseRecepcion(timbre);
		} catch (Exception ex) { 
			log.error("Ocurrio un problema al intentar Timbrar el CFDI", ex);
			acuseRecepcion.setDescripcion("Ocurrio un problema al intentar Timbrar el CFDI\n" + ex.toString());
			acuseRecepcion.setState(-2);
	    } finally {
	    	log.info(acuseRecepcion.toString());
	    	printStackTrace();
	    }
		
		return acuseRecepcion;
	}
	
	@Override
	public AcuseCancelacion cancelarTest(String xml, String timbreUser, String timbrePass) {
		AcuseCancelacion acuseCancelacion = null;
		String soapxml = "";
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		CancelTestResponse cancelTicket = null;
		StructCancel cancel = null;
		String body = "";
		String respuesta = "";
		
		try {
			this.testing = true;
			this.xmlUsado = xml;
			timbreUser = "fgomez";
			timbrePass = "12121212";
			
			acuseCancelacion = new AcuseCancelacion();
			log.info("Generando peticion ... ");
			body = "<CancelTest xmlns=\"http://tempuri.org/\">" 
					+ "<CanB64>" + Base64.encodeBase64String(xml.getBytes()) + "</CanB64>" 
				+ "</CancelTest>";

			log.info("Enviando peticion ... ");
			respuesta = http(body, timbreUser, timbrePass, this.wsTimbrado, null);
			if (respuesta == null || "".equals(respuesta.trim())) {
				acuseCancelacion.setState(this.httpCode);
				acuseCancelacion.setDescripcion(this.httpMensaje);
				return acuseCancelacion;
			}

			log.info("Procesando respuesta ... ");
			soapxml = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { CancelTestResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			cancelTicket = (CancelTestResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapxml.getBytes()));
			cancel = cancelTicket.getCancelTestResult();
			
			if (cancel.getState() == 0) {
				acuseCancelacion.setState(cancel.getState());
				acuseCancelacion.setDescripcion(cancel.getDescripcion());
				acuseCancelacion.setAcuse(xml);
				acuseCancelacion.setRfcSolicitante(cancel.getRfcSolicitante());
				acuseCancelacion.setFecha(cancel.getFecha());
				acuseCancelacion.setUUIDs(cancel.getUUIDs());
				acuseCancelacion.setNoSerieFirmante(cancel.getNoSerieFirmante());
			} else {
				acuseCancelacion.setState(cancel.getState());
				acuseCancelacion.setDescripcion(cancel.getDescripcion());
				return acuseCancelacion;
			}
		} catch (Exception ex) { 
			log.error("Ocurrio un problema al intentar Cancelar el CFDI indicado", ex);
			acuseCancelacion.setState(-2);
			acuseCancelacion.setDescripcion(ex.toString());
	    } finally {
	    	log.info(acuseCancelacion.toString());
	    }
		
		return acuseCancelacion;
	}
	
	// ------------------------------------------------------------------------------------------
	// PRIVATE 
	// ------------------------------------------------------------------------------------------
	
	private String http(String body, String timbreUser, String timbrePass, String urlWebService, String rfcEmisor) {
		HttpURLConnection rc = null;
		URL url = null;
		OutputStreamWriter out = null;
		InputStreamReader read = null;
		StringBuilder sb = null;
		String peticion = "";
		String respuesta = "";
		int len = 0;
		int ch = 0;
		
		try {
			// Inicializamos
			this.httpCode = -1;
			this.httpMensaje = "Generando peticion";
			this.soapRequestVersion = (this.soapRequestVersion == null || "".equals(this.soapRequestVersion.trim()) ? "1.1" : this.soapRequestVersion);
			stackTraceInterno("Generando peticion ... ");
			if (urlWebService == null || "".equals(urlWebService.trim()))
				return respuesta;
			
			// Genero peticion
			stackTraceInterno("Generando encabezado de peticion v" + this.soapRequestVersion + " ... ");
			if (urlWebService.trim().equals(this.wsTimbrado)) {
				peticion = ("1.2".equals(this.soapRequestVersion) ? this.wsTimbradoRequest12 : this.wsTimbradoRequest);
				peticion = peticion.replace("?timbreUser", timbreUser).replace("?timbrePass", timbrePass).replace("?body", body);
			} else if (urlWebService.trim().equals(this.wsConsultas)) {
				peticion = ("1.2".equals(this.soapRequestVersion) ? this.wsConsultasRequest12 : this.wsConsultasRequest);
				peticion = peticion.replace("?timbreUser", timbreUser).replace("?timbrePass", timbrePass).replace("?body", body).replace("?rfcEmisor", rfcEmisor);
			}
			stackTraceInterno("OK", false);
			stackTraceInterno("Request :\n" + peticion);

			// Enviamos peticion (Request)
			this.httpMensaje = "Enviando peticion";
			stackTraceInterno("Enviando Request ... ");
			len = peticion.length();
			url = new URL(urlWebService);
			rc = (HttpURLConnection) url.openConnection();
			rc.setRequestMethod("POST");
			rc.setDoOutput(true);
			rc.setDoInput(true);
			rc.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			rc.setRequestProperty("Content-Length", Integer.toString(len));
			rc.connect();
			
			out = new OutputStreamWriter(rc.getOutputStream());
			out.write(peticion, 0, len);
			out.flush();
			stackTraceInterno("OK", false);
			
			// Leemos respuesta (Response)
			stackTraceInterno("Leyendo Response ... ");
			this.httpCode = rc.getResponseCode();
			this.httpMensaje = rc.getResponseMessage();
			read = new InputStreamReader(rc.getInputStream());
			sb = new StringBuilder();
			ch = read.read();
			while (ch != -1) {
				sb.append((char) ch);
				ch = read.read();
			}
			respuesta = sb.toString();
			stackTraceInterno("OK", false);
			stackTraceInterno("Response :\n" + respuesta);
			read.close();
			rc.disconnect();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar ejecutar la peticion: " + this.httpCode + " - " + this.httpMensaje + "\n\n" + this.stackTraceInterno, e);
			respuesta = "";
		} finally {
			log.info("HTTP Response : " + this.httpCode + " - " + this.httpMensaje);
			stackTraceInterno("\n");
			stackTraceInterno("WS Request  ... \n" + peticion);
			stackTraceInterno("WS Response ... \n" + respuesta);
			stackTraceInterno("XML/CFDI ... \n" + this.xmlUsado);
			stackTraceInterno("\n");
		}
		
		return respuesta;
	}
	
	private AcuseRecepcion acuseRecepcion(StructCfd timbre) {
		AcuseRecepcion acuseRecepcion = null;
		Element tfdTimbre = null;
		String timbreStr = "";
		
		try {
			log.info("Generando AcuseRecepcion ... ");
			acuseRecepcion = new AcuseRecepcion();
			acuseRecepcion.setState(timbre.getState());
			acuseRecepcion.setDescripcion(timbre.getDescripcion());
			acuseRecepcion.setXml(timbre.getCfdi()); 
			acuseRecepcion.setTimbre(timbre.getTimbre());
			
			timbreStr = timbre.getTimbre();
			tfdTimbre = generaElementFromString(timbreStr, "");
			if (tfdTimbre == null) {
				timbreStr = timbre.getCfdi();
				tfdTimbre = generaElementFromString(timbreStr, "TimbreFiscalDigital");
			}
			
			if (tfdTimbre != null) {
				acuseRecepcion.setUuid(tfdTimbre.getAttributeValue("UUID"));
				acuseRecepcion.setFechaTimbrado(tfdTimbre.getAttributeValue("FechaTimbrado"));
				acuseRecepcion.setNoCertificadoSat(tfdTimbre.getAttributeValue("NoCertificadoSAT"));
				acuseRecepcion.setSelloSat(tfdTimbre.getAttributeValue("SelloSAT"));
				acuseRecepcion.setVersionTimbre(tfdTimbre.getAttributeValue("Version"));
			}
			log.info("Generando AcuseRecepcion ... OK");
		} catch (Exception ex) {
			log.error("Ocurrio un problema al intentar interpretar el Timbre del CFDI:\n" + timbreStr, ex);
			acuseRecepcion.setDescripcion("Ocurrio un problema al intentar interpretr el Timbre del CFDI (Exception)");
			acuseRecepcion.setState(-2);
		} 
		
		return acuseRecepcion;
	}
	
	private AcuseCancelacion acuseCancelacion(String xml, StructCancel struct) {
		AcuseCancelacion acuseCancelacion = null;

		try {
			acuseCancelacion = new AcuseCancelacion();
			acuseCancelacion.setState(struct.getState());
			acuseCancelacion.setDescripcion(struct.getDescripcion());
			acuseCancelacion.setRfcSolicitante(struct.getRfcSolicitante());
			acuseCancelacion.setFecha(struct.getFecha());
			acuseCancelacion.setUUIDs(struct.getUUIDs());
			acuseCancelacion.setNoSerieFirmante(struct.getNoSerieFirmante());
			acuseCancelacion.setAcuse(struct.getAcuse());
			if (acuseCancelacion.getFecha() == null || "".equals(acuseCancelacion.getFecha().trim()))
				acuseCancelacion.setFecha((new SimpleDateFormat("dd/MM/yyyy HH:mm")).format(Calendar.getInstance().getTime()));
		} catch (Exception ex) {
			log.error("Ocurrio un problema al intentar interpretar el AcuseEstatus", ex);
			acuseCancelacion.setDescripcion("Ocurrio un problema al intentar interpretar el resultado (AcuseEstatus) del WS");
			acuseCancelacion.setState(-2);
		} 
		
		return acuseCancelacion;
	}
	
	private AcuseEstatus acuseEstatus(StructCancel struct, String expresionImpresa) {
		AcuseEstatus acuseEstatus = null;

		try {
			acuseEstatus = new AcuseEstatus();
			acuseEstatus.setState(struct.getState()); 
			acuseEstatus.setMensaje(struct.getMensaje());
			acuseEstatus.setFecha(struct.getFecha());
			acuseEstatus.setUUIDs(struct.getUUIDs());
			acuseEstatus.setRfcSolicitante(struct.getRfcSolicitante());
			acuseEstatus.setNoSerieFirmante(struct.getNoSerieFirmante());
			acuseEstatus.setAcuse(struct.getAcuse());
			acuseEstatus.setRutaDescargaAcuse(struct.getRutaDescargaAcuse());
			acuseEstatus.setEsCancelable(struct.getEsCancelable());
			acuseEstatus.setEstatusCancelacion(struct.getEstatusCancelacion());
			acuseEstatus.setEstatusCFDI(struct.getEstatusCFDI());
			acuseEstatus.setDescripcion(struct.getDescripcion());
			acuseEstatus.setExpresionImpresa(expresionImpresa);
		} catch (Exception ex) {
			log.error("Ocurrio un problema al intentar interpretar el AcuseEstatus", ex);
			acuseEstatus.setDescripcion("Ocurrio un problema al intentar interpretar el resultado (AcuseEstatus) del WS");
			acuseEstatus.setState(-2);
		} 
		
		return acuseEstatus;
	}

	private AcuseCFDI acuseCFDI(StructCfdi struct) {
		AcuseCFDI acuse = null;
		
		try {
			log.info("Generando AcuseCFDI ... ");
			acuse = new AcuseCFDI();
			if (struct.getError() != null && ! "".equals(struct.getError())) {
				acuse.setState(1);
				acuse.setDescripcion(struct.getError());
				acuse.setCFDI("");
			} else {
				acuse.setState(0);
				acuse.setDescripcion("");
				acuse.setCFDI(struct.getXml());
			}
			log.info("Generando AcuseCFDI ... OK");
		} catch (Exception ex) {
			log.error("Ocurrio un problema al intentar interpretar el CFDI", ex);
			acuse.setDescripcion("Ocurrio un problema al intentar interpretar el CFDI (Exception)");
			acuse.setState(2);
		} 
		
		return acuse;
	}
	
 	private Element generaElementFromString(String xml, String itemName) {
		SAXBuilder builder = null;
		Document document = null;
		Element rootNode = null;
		Element item = null;
		Element nodo = null;
		boolean encontrado = false;
		int size = 0;
		
		try {
			if (xml == null || "".equals(xml.trim()))
				return rootNode;
			builder = new SAXBuilder();
			document = builder.build(new StringReader(xml));
			rootNode = document.getRootElement();
			if (itemName != null && ! "".equals(itemName)) {
				size = rootNode.getChildren().size();
				for (int idx = 0; idx < size; idx ++) {
					item = (Element) rootNode.getChildren().get(idx);
					if (itemName.equals(item.getName())) {
						encontrado = true;
						rootNode = item;
						break;
					}
					
					for (Object obj : item.getChildren()) {
						nodo = (Element) obj;
						if (itemName.equals(nodo.getName())) {
							encontrado = true;
							rootNode = nodo;
							break;
						}
					}
					
					if (encontrado)
						break;
				}
			}
		} catch (JDOMException ex) {
			log.error("Ocurrio un problema al intentar interpretar el Timbre del CFDI (JDOMException) usando: \n" + xml, ex);
			rootNode = null;
		} catch (IOException ex) {
			log.error("Ocurrio un problema al intentar interpretar el Timbre del CFDI (IOException) usando: \n" + xml, ex);
			rootNode = null;
		} catch (Exception ex) {
			log.error("Ocurrio un problema al intentar interpretar el Timbre del CFDI (Exception) usando: \n" + xml, ex);
			rootNode = null;
		} 

		return rootNode;
	}
	
	private void stackTrace() {
		this.stackTrace = "";
		this.stackTraceInterno = "";
	}
	
	private void stackTrace(String message) {
		stackTrace(message, true);
	}
	
	private void stackTrace(String message, boolean newLine) {
		this.stackTrace += (newLine ? "\n" : "") + message;
	}
	
	private void stackTraceInterno(String message) {
		if (this.debugging)
			log.info(message);
	}
	
	private void stackTraceInterno(String message, boolean newLine) {
		this.stackTraceInterno += (newLine ? "\n" : "") + "    > " + message;
	}
	
	private void printStackTrace() {
		log.info(
				"\n-------------------------------------------------------------------------------------------" + 
				"\n STACKTRACE " + 
				"\n-------------------------------------------------------------------------------------------" + 
				this.stackTrace + 
				"\n-------------------------------------------------------------------------------------------"
		);
		stackTrace();
	}
}
