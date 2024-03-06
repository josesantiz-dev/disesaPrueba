package net.giro.cxc.fe;

import org.tempuri.realvirtual.ws.GetTicketResponse;
import org.tempuri.realvirtual.ws.StructCfd;
import org.apache.commons.ssl.Base64;
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
	@Override
	public AcuseRecepcion timbrar(Integer facturaId, String xml, String certificadoPEM, String llavePEM, String llaveENC, String usuarioTimbre, String claveTimbre) {
		GetTicketResponse ticket = null;
		StructCfd timbre = null;
		AcuseRecepcion acuseRecepcion = new AcuseRecepcion();
		
		try {
			String body = " <tem:GetTicket> <tem:base64Cfd>" + Base64.encodeBase64String(xml.getBytes()) + "</tem:base64Cfd> </tem:GetTicket>";
			String respuesta = http(body, usuarioTimbre, claveTimbre);
			String soapxml = respuesta.substring(respuesta.indexOf("<soap:Body>") + 11, respuesta.indexOf("</soap:Body>"));
			JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { GetTicketResponse.class });
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			ticket = (GetTicketResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapxml.getBytes()));
			if (ticket.getGetTicketResult().getState() == 0) {
				acuseRecepcion.setState(ticket.getGetTicketResult().getState());
				acuseRecepcion.setDescripcion(ticket.getGetTicketResult().getDescripcion());
				
				byte[] decodedBytes = Base64.decodeBase64(ticket.getGetTicketResult().getCfdi());
				ticket.getGetTicketResult().setCfdi(new String(decodedBytes));
				
				acuseRecepcion.setState(0);
				acuseRecepcion.setXml(new String(decodedBytes)); 
				
				decodedBytes = Base64.decodeBase64(ticket.getGetTicketResult().getTimbre());
				acuseRecepcion.setTimbre(new String(decodedBytes));
				
				// Leemos el TIMBRE para recuperar UUID, Fecha timbrado, Sello SAT y No Certificado SAT.
				SAXBuilder builder = new SAXBuilder();
				Document document = (Document) builder.build(new StringReader(acuseRecepcion.getXml()));
				Element rootNode = (Element) (document.getRootElement().getChildren()).get(4);
				
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
			acuseRecepcion.setState(-2);
			acuseRecepcion.setDescripcion(ex.toString());
	    }
		
		return acuseRecepcion;
	}
	
	public AcuseCancelacion cancelar(Integer facturaId, String xml, byte[] certificadoPEM, String llavePEM, byte[] llaveENC, String usuarioTimbre, String claveTimbre) {
		return null;
	}
	
	private String http(String body, String usuarioTimbre, String claveTimbre)  throws Exception {
		String consulta = " <soapenv:Envelope " +
				  "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
				  "xmlns:tem=\"http://tempuri.org/\"> " +
				  " <soapenv:Header> " +
				  " <tem:AuthSoapHd> " +
				  " <tem:strUserName>" + usuarioTimbre + "</tem:strUserName> " +
				  " <tem:strPassword>" + claveTimbre + "</tem:strPassword>  " +
				  "</tem:AuthSoapHd> " +
				  "</soapenv:Header> "  +
				  "<soapenv:Body>" + 
				  body + 
				  "    </soapenv:Body>" + 
				  " </soapenv:Envelope>";
		URL url = new URL("http://108.60.211.43/ws/service.asmx");
		HttpURLConnection rc = (HttpURLConnection)url.openConnection();

		rc.setRequestMethod("POST");
		rc.setDoOutput(true);
		rc.setDoInput(true);
		rc.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		String reqStr = consulta;
		int len = reqStr.length();
		rc.setRequestProperty("Content-Length", Integer.toString(len));
		rc.connect();
		OutputStreamWriter out = new OutputStreamWriter(rc.getOutputStream());
		out.write(reqStr, 0, len);
		out.flush();
		
		System.out.println("Request sent, reading response ");
		InputStreamReader read = new InputStreamReader(rc.getInputStream());
		StringBuilder sb = new StringBuilder();
		int ch = read.read();
		while (ch != -1) {
			sb.append((char)ch);
			ch = read.read();
		}
		
		String respuesta = sb.toString();
		read.close();
		rc.disconnect();
		
		return respuesta;
	}
}
