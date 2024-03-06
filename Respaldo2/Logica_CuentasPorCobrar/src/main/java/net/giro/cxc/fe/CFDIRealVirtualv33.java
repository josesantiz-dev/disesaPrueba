package net.giro.cxc.fe;

import org.apache.commons.ssl.Base64;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.tempuri.realvirtual.ws.v33.CancelTestResponse;
import org.tempuri.realvirtual.ws.v33.CancelTicketResponse;
import org.tempuri.realvirtual.ws.v33.GetTicketResponse;
import org.tempuri.realvirtual.ws.v33.SchemaCfdResponse;
import org.tempuri.realvirtual.ws.v33.StructCancel;
import org.tempuri.realvirtual.ws.v33.StructCfd;
import org.tempuri.realvirtual.ws.v33.TestCfd33Response;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.InputStreamReader;

public class CFDIRealVirtualv33 implements CFDI {
	private static Logger log = Logger.getLogger(CFDIRealVirtualv33.class.getName());
	private int httpCode;
	private String httpMensaje;
	private String xmlUsado;
	private boolean debugging;
	@SuppressWarnings("unused")
	private boolean testing;
	private String soapRequestVersion;
	private String stackTrace;
	private String stackTraceInterno;

	
	@Override
	public void debugging(boolean debugging) {
		this.debugging = debugging;
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
		StructCfd timbre = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		String soapxml = "";
		
		try {
			stackTrace();
			this.testing = false;
			this.xmlUsado = xml;
			stackTrace("Generando peticion ... ");
			body = "<GetTicket xmlns=\"http://tempuri.org/\">" 
					+ "<base64Cfd>" + Base64.encodeBase64String(xml.getBytes()) + "</base64Cfd>" 
				+ "</GetTicket>";
			stackTrace("OK", false);
			stackTrace("peticion : " + body);
			
			stackTrace("Enviando peticion ... ");
			respuesta = http(body, timbreUser, timbrePass);
			if (respuesta == null || "".equals(respuesta.trim())) {
				acuseRecepcion = new AcuseRecepcion();
				acuseRecepcion.setState(this.httpCode);
				acuseRecepcion.setDescripcion(this.httpMensaje);
				return acuseRecepcion;
			}
			stackTrace("OK", false);
			stackTrace("respuesta : " + respuesta);
			stackTrace(this.stackTraceInterno);

			stackTrace("Procesando respuesta ... ");
			soapxml = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { GetTicketResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ticket = (GetTicketResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapxml.getBytes()));
			timbre = ticket.getGetTicketResult();
			stackTrace("OK", false);

			stackTrace("Validando respuesta ... ");
			if (timbre.getState() == 0) {
				timbre.setCfdi(new String(Base64.decodeBase64(timbre.getCfdi())));
				timbre.setTimbre(new String(Base64.decodeBase64(timbre.getTimbre())));
				timbre.setDescripcion("");
				timbre.setState(0);
				stackTrace("OK", false);
			} else if (timbre.getState() == 500) {
				timbre.setTimbre(timbre.getDescripcion().substring(timbre.getDescripcion().indexOf("<?xml version=\"1.0\" encoding=\"utf-8\"?>")));
				timbre.setDescripcion(timbre.getDescripcion().substring(0, timbre.getDescripcion().indexOf("<?xml version=\"1.0\" encoding=\"utf-8\"?>")).trim());
				timbre.setCfdi(xml.replace("</cfdi:Comprobante>", "<cfdi:Complemento>" + timbre.getTimbre().replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "")) + "</cfdi:Complemento></cfdi:Comprobante>");
				timbre.setState(0);
				stackTrace("OK (timbre previo)", false);
			} else {
				acuseRecepcion = new AcuseRecepcion();
				acuseRecepcion.setState(timbre.getState());
				acuseRecepcion.setDescripcion(timbre.getDescripcion());
				stackTrace("ERROR", false);
				stackTrace(timbre.getState() + " - " + timbre.getDescripcion());
				return acuseRecepcion;
			}
			
			acuseRecepcion = generaAcuseRecepcion(timbre);
			/*
			// Leemos el TIMBRE para recuperar UUID, Fecha timbrado, Sello SAT y No Certificado SAT.
			stackTrace("Leyendo timbre ... ", false);
			builder = new SAXBuilder();
			document = (Document) builder.build(new StringReader(timbre.getCfdi()));
			rootNode = document.getRootElement();
			//document = (Document) builder.build(new StringReader(timbre.getCfdi()));
			//rootNode = (Element) (document.getRootElement().getChildren()).get(document.getRootElement().getChildren().size() - 1);
			//rootNode = (Element) rootNode.getChildren().get(0);
			stackTrace("OK");

			stackTrace("Generando Acuse ... ", false);
			acuseRecepcion.setState(timbre.getState());
			acuseRecepcion.setDescripcion(timbre.getDescripcion());
			acuseRecepcion.setXml(timbre.getCfdi()); 
			acuseRecepcion.setTimbre(timbre.getTimbre());
			acuseRecepcion.setUuid(rootNode.getAttributeValue("UUID"));
			acuseRecepcion.setFechaTimbrado(rootNode.getAttributeValue("FechaTimbrado"));
			acuseRecepcion.setNoCertificadoSat(rootNode.getAttributeValue("NoCertificadoSAT"));
			acuseRecepcion.setSelloSat(rootNode.getAttributeValue("SelloSAT"));
			stackTrace("OK");*/
		} catch (Exception ex) { 
			log.error("Ocurrio un problema al intentar Timbrar el CFDI", ex);
			acuseRecepcion.setDescripcion("Ocurrio un problema al intentar Timbrar el CFDI\n" + ex.toString());
			acuseRecepcion.setState(-2);
	    } finally {
	    	log.info(acuseRecepcion.toString());
	    	if (this.debugging)
	    		printStackTrace();
	    }
		
		return acuseRecepcion;
	}

	@Override
	public AcuseCancelacion cancelar(String xml, String timbreUser, String timbrePass) {
		AcuseCancelacion acuseCancelacion = null;
		CancelTicketResponse cancelTicket = null;
		StructCancel cancel = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		String soapxml = "";
		
		try {
			this.testing = false;
			this.xmlUsado = xml;
			acuseCancelacion = new AcuseCancelacion();
			if (this.debugging)
				log.info("Generando peticion ... ");
			body = "<CancelTicket xmlns=\"http://tempuri.org/\">" 
					+ "<base64Cfd>" + Base64.encodeBase64String(xml.getBytes()) + "</base64Cfd>" 
				+ "</CancelTicket>";
			
			if (this.debugging)
				log.info("Enviando peticion ... ");
			respuesta = http(body, timbreUser, timbrePass);
			if (respuesta == null || "".equals(respuesta.trim())) {
				acuseCancelacion.setState(this.httpCode);
				acuseCancelacion.setDescripcion(this.httpMensaje);
				return acuseCancelacion;
			}

			if (this.debugging)
				log.info("Procesando respuesta ... ");
			soapxml = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { CancelTicketResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			cancelTicket = (CancelTicketResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapxml.getBytes()));
			cancel = cancelTicket.getCancelTicketResult();

			if (this.debugging)
				log.info("Leemos acuse ... ");
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
			body = "<tem:SchemaCfd>" 
					+ "<tem:base64Cfd>" + Base64.encodeBase64String(xml.getBytes()) + "</tem:base64Cfd>" 
				+ "</tem:SchemaCfd>";
			
			if (this.debugging)
				log.info("Enviamos peticion ... ");
			respuesta = http(body, timbreUser, timbrePass);
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
		String soapxml = "";
		
		try {
			this.testing = true;
			this.xmlUsado = xml;
			timbreUser = "fgomez";
			timbrePass = "12121212";
			
			if (this.debugging)
				log.info("Generando peticion ... ");
			body = "<TestCfd33 xmlns=\"http://tempuri.org/\">" 
					+ "<base64Cfd>" + Base64.encodeBase64String(xml.getBytes()) + "</base64Cfd>" 
				+ "</TestCfd33>";

			if (this.debugging)
				log.info("Enviamos peticion ... ");
			respuesta = http(body, timbreUser, timbrePass);
			if (respuesta == null || "".equals(respuesta.trim())) {
				acuseRecepcion = new AcuseRecepcion();
				acuseRecepcion.setState(this.httpCode);
				acuseRecepcion.setDescripcion(this.httpMensaje);
				return acuseRecepcion;
			}

			if (this.debugging)
				log.info("Procesando respuesta ... ");
			soapxml = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { TestCfd33Response.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ticket = (TestCfd33Response) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapxml.getBytes()));
			timbre = ticket.getTestCfd33Result();
			
			if (timbre.getState() == 0) {
				timbre.setCfdi(new String(Base64.decodeBase64(timbre.getCfdi())));
				timbre.setTimbre(new String(Base64.decodeBase64(timbre.getTimbre())));
				timbre.setDescripcion("");
				timbre.setState(0);
			} else if (timbre.getState() == 500) {
				timbre.setTimbre(timbre.getDescripcion().substring(timbre.getDescripcion().indexOf("<?xml version=\"1.0\" encoding=\"utf-8\"?>")));
				timbre.setDescripcion(timbre.getDescripcion().substring(0, timbre.getDescripcion().indexOf("<?xml version=\"1.0\" encoding=\"utf-8\"?>")).trim());
				timbre.setCfdi(xml.replace("</cfdi:Comprobante>", "<cfdi:Complemento>" + timbre.getTimbre().replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "")) + "</cfdi:Complemento></cfdi:Comprobante>");
				timbre.setState(0);
			} else {
				acuseRecepcion = new AcuseRecepcion();
				acuseRecepcion.setState(timbre.getState());
				acuseRecepcion.setDescripcion(timbre.getDescripcion());
				return acuseRecepcion;
			}
			
			acuseRecepcion = generaAcuseRecepcion(timbre);
			/*
			// Leemos el TIMBRE para recuperar UUID, Fecha timbrado, Sello SAT y No Certificado SAT.
			builder = new SAXBuilder();
			document = (Document) builder.build(new StringReader(timbre.getTimbre()));
			rootNode = document.getRootElement();
			//document = (Document) builder.build(new StringReader(timbre.getCfdi()));
			//rootNode = (Element) (document.getRootElement().getChildren()).get(document.getRootElement().getChildren().size() - 1);
			//rootNode = (Element) rootNode.getChildren().get(0);

			acuseRecepcion.setState(timbre.getState());
			acuseRecepcion.setDescripcion(timbre.getDescripcion());
			acuseRecepcion.setXml(timbre.getCfdi()); 
			acuseRecepcion.setTimbre(timbre.getTimbre());
			acuseRecepcion.setUuid(rootNode.getAttributeValue("UUID"));
			acuseRecepcion.setFechaTimbrado(rootNode.getAttributeValue("FechaTimbrado"));
			acuseRecepcion.setNoCertificadoSat(rootNode.getAttributeValue("NoCertificadoSAT"));
			acuseRecepcion.setSelloSat(rootNode.getAttributeValue("SelloSAT"));*/
		} catch (Exception ex) { 
			log.error("Ocurrio un problema al intentar Timbrar el CFDI", ex);
			acuseRecepcion.setDescripcion("Ocurrio un problema al intentar Timbrar el CFDI\n" + ex.toString());
			acuseRecepcion.setState(-2);
	    } finally {
	    	log.info(acuseRecepcion.toString());
	    	if (this.debugging)
	    		printStackTrace();
	    }
		
		return acuseRecepcion;
	}

	@Override
	public AcuseCancelacion cancelarTest(String xml, String timbreUser, String timbrePass) {
		AcuseCancelacion acuseCancelacion = null;
		CancelTestResponse cancelTicket = null;
		StructCancel cancel = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		String soapxml = "";
		
		try {
			this.testing = true;
			this.xmlUsado = xml;
			timbreUser = "fgomez";
			timbrePass = "12121212";
			
			acuseCancelacion = new AcuseCancelacion();
			if (this.debugging)
				log.info("Generando peticion ... ");
			body = "<CancelTest xmlns=\"http://tempuri.org/\">" 
					+ "<CanB64>" + Base64.encodeBase64String(xml.getBytes()) + "</CanB64>" 
				+ "</CancelTest>";

			if (this.debugging)
				log.info("Enviando peticion ... ");
			respuesta = http(body, timbreUser, timbrePass);
			if (respuesta == null || "".equals(respuesta.trim())) {
				acuseCancelacion.setState(this.httpCode);
				acuseCancelacion.setDescripcion(this.httpMensaje);
				return acuseCancelacion;
			}

			if (this.debugging)
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
	
	private String http(String body, String timbreUser, String timbrePass) {
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
			stackTraceInterno("Generando peticion ... ");
			
			if (this.soapRequestVersion == null || "".equals(this.soapRequestVersion.trim()))
				this.soapRequestVersion = "1.1";
			
			// Genero peticion
			stackTraceInterno("Generando encabezado de peticion v" + this.soapRequestVersion + " ... ");
			if ("1.2".equals(this.soapRequestVersion)) {
				peticion = "<soap12:Envelope xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
						+ "<soap12:Header>"
							+ "<AuthSoapHd xmlns=\"http://tempuri.org/\">"
								+ "<strUserName>" + timbreUser + "</strUserName>"
								+ "<strPassword>" + timbrePass + "</strPassword>"
							+ "</AuthSoapHd>"
						+ "</soap12:Header>"
						+ "<soap12:Body>" + body + "</soap12:Body>"
						+ "</soap12:Envelope>";
			} else {
				peticion = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+ "<soap:Header>"
							+ "<AuthSoapHd xmlns=\"http://tempuri.org/\">"
								+ "<strUserName>" + timbreUser + "</strUserName>"
								+ "<strPassword>" + timbrePass + "</strPassword>"
							+ "</AuthSoapHd>"
						+ "</soap:Header>"
						+ "<soap:Body>" + body + "</soap:Body>"
					+ "</soap:Envelope>";
			}
			stackTraceInterno("OK", false);
			stackTraceInterno("Request :\n" + peticion);

			// Enviamos peticion (Request)
			this.httpMensaje = "Enviando peticion";
			stackTraceInterno("Enviando Request ... ");
			len = peticion.length();
			url = new URL("http://generacfdi.com.mx/rvltimbrado/service1.asmx");//("http://108.60.211.43/ws/service.asmx");
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
			log.error("Ocurrio un problema al intentar ejecutar la peticion: " + this.httpCode + " - " + this.httpMensaje, e);
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
	
	private AcuseRecepcion generaAcuseRecepcion(StructCfd timbre) {
		AcuseRecepcion acuseRecepcion = null;
		Element tfdTimbre = null;
		String timbreStr = "";
		
		try {
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
			}
		} catch (Exception ex) {
			log.error("Ocurrio un problema al intentar interpretar el Timbre del CFDI:\n" + timbreStr, ex);
			acuseRecepcion.setDescripcion("Ocurrio un problema al intentar interpretr el Timbre del CFDI (Exception)");
			acuseRecepcion.setState(-2);
		} 
		
		return acuseRecepcion;
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
		stackTraceInterno(message, true);
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
