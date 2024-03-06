package net.giro.cxc.fe;

import org.tempuri.realvirtual.ws.v32.CancelTicketResponse;
import org.tempuri.realvirtual.ws.v32.GetTicketResponse;
import org.tempuri.realvirtual.ws.v32.SchemaCfdResponse;
import org.tempuri.realvirtual.ws.v32.StructCancel;
import org.tempuri.realvirtual.ws.v32.StructCfd;
import org.apache.commons.ssl.Base64;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.InputStreamReader;

public class CFDIRealVirtual implements CFDI {
	private static Logger log = Logger.getLogger(CFDIRealVirtual.class.getName());
	private String httpMensaje;
	private int httpCode;
	private boolean debugging;
	private boolean testing;
	@SuppressWarnings("unused")
	private String verSoapRequest;
	

	@Override
	public void debugging(boolean debugging) {
		this.debugging = debugging;
	}

	@Override
	public void soapRequestVersion(String versionSoapRequest) {
		this.verSoapRequest = versionSoapRequest;
	}
	
	@Override
	public AcuseRecepcion timbrar(String xml, String usuarioTimbre, String claveTimbre) {
		GetTicketResponse ticket = null;
		AcuseRecepcion acuseRecepcion = null;
		StructCfd timbre = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		SAXBuilder builder = null;
		Document document = null;
		Element rootNode = null;
		byte[] decodedBytes = null;
		String body = "";
		String respuesta = "";
		String soapxml = "";
		
		try {
			if (this.debugging)
				log.info("Generando peticion ... ");
			acuseRecepcion = new AcuseRecepcion();
			body = "<tem:TestCfd33>" 
				+ "    <tem:base64Cfd>" + Base64.encodeBase64String(xml.getBytes()) + "</tem:base64Cfd>" 
				+ "</tem:TestCfd33>";
			respuesta = http(body, usuarioTimbre, claveTimbre);
			soapxml = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { GetTicketResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			ticket = (GetTicketResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapxml.getBytes()));
			if (ticket.getGetTicketResult().getState() == 0) {
				acuseRecepcion.setState(ticket.getGetTicketResult().getState());
				acuseRecepcion.setDescripcion(ticket.getGetTicketResult().getDescripcion());
				
				decodedBytes = Base64.decodeBase64(ticket.getGetTicketResult().getCfdi());
				ticket.getGetTicketResult().setCfdi(new String(decodedBytes));
				
				acuseRecepcion.setState(0);
				acuseRecepcion.setXml(new String(decodedBytes)); 
				
				decodedBytes = Base64.decodeBase64(ticket.getGetTicketResult().getTimbre());
				acuseRecepcion.setTimbre(new String(decodedBytes));
				
				// Leemos el TIMBRE para recuperar UUID, Fecha timbrado, Sello SAT y No Certificado SAT.
				builder = new SAXBuilder();
				document = (Document) builder.build(new StringReader(acuseRecepcion.getXml()));
				rootNode = (Element) (document.getRootElement().getChildren()).get(4);
				rootNode = (Element) rootNode.getChildren().get(0);
				
				acuseRecepcion.setFechaTimbrado(rootNode.getAttributeValue("FechaTimbrado"));
				acuseRecepcion.setNoCertificadoSat(rootNode.getAttributeValue("noCertificadoSAT"));
				acuseRecepcion.setSelloSat(rootNode.getAttributeValue("selloSAT"));
				acuseRecepcion.setUuid(rootNode.getAttributeValue("UUID"));
			} else {
				timbre = ticket.getGetTicketResult();
				acuseRecepcion.setState(timbre.getState());
				acuseRecepcion.setDescripcion(timbre.getDescripcion());
			}
		} catch (Exception ex) { 
			log.error("Ocurrio un problema al intentar Timbrar el CFDI", ex);
			acuseRecepcion.setState(-2);
			acuseRecepcion.setDescripcion(ex.toString());
	    } finally {
	    	log.info(acuseRecepcion.toString());
	    }
		
		return acuseRecepcion;
	}

	@Override
	public AcuseRecepcion timbrarTest(String xml, String usuarioTimbre, String claveTimbre) {
		return null;
	}

	@Override
	public AcuseCancelacion cancelar(String xml, String usuarioTimbre, String claveTimbre) {
		AcuseCancelacion acuseCancelacion = null;
		CancelTicketResponse cancelTicket = null;
		StructCancel cancel = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		String soapxml = "";
		
		try {
			acuseCancelacion = new AcuseCancelacion();
			body = "<tem:CancelTicket>" 
				+ "    <tem:base64Cfd>" + Base64.encodeBase64String(xml.getBytes()) + "</tem:base64Cfd>" 
				+ "</tem:CancelTicket>";
			respuesta = http(body, usuarioTimbre, claveTimbre);
			soapxml = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { CancelTicketResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			cancelTicket = (CancelTicketResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapxml.getBytes()));
			if (cancelTicket.getCancelTicketResult().getState() == 0) {
				acuseCancelacion.setState(cancelTicket.getCancelTicketResult().getState());
				acuseCancelacion.setDescripcion(cancelTicket.getCancelTicketResult().getDescripcion());
				acuseCancelacion.setAcuse(xml);
				acuseCancelacion.setRfcSolicitante(cancelTicket.getCancelTicketResult().getRfcSolicitante());
				acuseCancelacion.setFecha(cancelTicket.getCancelTicketResult().getFecha());
				acuseCancelacion.setUUIDs(cancelTicket.getCancelTicketResult().getUUIDs());
				acuseCancelacion.setNoSerieFirmante(cancelTicket.getCancelTicketResult().getNoSerieFirmante());
			} else {
				cancel = cancelTicket.getCancelTicketResult();
				acuseCancelacion.setState(cancel.getState());
				acuseCancelacion.setDescripcion(cancel.getDescripcion());
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
	public AcuseCancelacion cancelarTest(String xml, String usuarioTimbre, String claveTimbre) {
		return null;
	}

	@Override
	public AcuseSchema schema(String xml, String usuarioTimbre, String claveTimbre) {
		AcuseSchema acuseSchema = null;
		SchemaCfdResponse schemaCfd = null;
		StructCfd structCfd = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		String body = "";
		String respuesta = "";
		String soapxml = "";
		
		try {
			acuseSchema = new AcuseSchema();
			body = "<tem:SchemaCfd>" 
				+ "    <tem:base64Cfd>" + Base64.encodeBase64String(xml.getBytes()) + "</tem:base64Cfd>" 
				+ "</tem:SchemaCfd>";
			respuesta = http(body, usuarioTimbre, claveTimbre);
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
	
	private String http(String body, String usuarioTimbre, String claveTimbre) throws Exception {
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
			log.info("Generamos peticion ... ");
			peticion = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" 
					+ "    <soapenv:Header>" 
					+ "        <tem:AuthSoapHd>" 
					+ "            <tem:strUserName>" + usuarioTimbre + "</tem:strUserName>" 
					+ "            <tem:strPassword>" + claveTimbre   + "</tem:strPassword>" 
					+ "        </tem:AuthSoapHd>" 
					+ "    </soapenv:Header>" 
					+ "    <soapenv:Body>" 
					+ "        " + body 
					+ "    </soapenv:Body>"
					+ "</soapenv:Envelope>";
			//reqStr = consulta;
			len = peticion.length();
			log.info("Peticion:\n" + peticion);
			
			// Enviamos peticion (Request)
			log.info("Enviamos peticion ... ");
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
			
			// Leemos respuesta (Response)
			log.info("Leemos respuesta ... ");
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
			log.info("Respuesta:\n" + respuesta);
			read.close();
			rc.disconnect();
			
			return respuesta;
		} catch (Exception e) {
			log.info("Ocurrio un problema al intentar ejecutar la peticion", e);
			throw e;
		} finally {
			log.info("HTTP Response : " + this.httpCode + " - " + this.httpMensaje);
			if (this.testing) {
				log.info(" Peticion WS ... \n" + peticion);
				log.info("Respuesta WS ... \n" + respuesta);
			}
		}
	}
}
