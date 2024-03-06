package net.giro.cargas.documentos.logica;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import net.giro.cargas.documentos.beans.ComprobacionFactura;
import net.giro.cargas.documentos.beans.Comprobante;
import net.giro.cargas.documentos.dao.ComprobacionFacturaDAO;
import net.giro.cargas.documentos.plataforma.InfoSesion;
import net.giro.cargas.documentos.respuesta.Errores;
import net.giro.cargas.documentos.respuesta.Respuesta;
import net.giro.plataforma.seguridad.beans.Acceso;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import sat.gob.mx.consulta.cfdi.Acuse;
import sat.gob.mx.consulta.cfdi.tempuri.Consulta;
import sat.gob.mx.consulta.cfdi.tempuri.ConsultaCFDIService;
import sat.gob.mx.consulta.cfdi.tempuri.IConsultaCFDIService;

@Stateless
public class FacturasFac implements FacturasRem {
	private static Logger log = Logger.getLogger(FacturasFac.class);
	private InitialContext ctx = null;
	private InfoSesion infoSesion;
	@Resource
	private SessionContext sctx;

	private String modulo;
	
	Consulta consulta;
	ConsultaCFDIService consultaService;
	IConsultaCFDIService iConsultaService;
	
	ComprobacionFacturaDAO ifzComprobacionFactura;

	public FacturasFac() {
		try {
			Hashtable<String, Object> p = new Hashtable<String, Object>();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(p);

			infoSesion = new InfoSesion();
			
			ifzComprobacionFactura = (ComprobacionFacturaDAO)ctx.lookup("ejb:/Model_Cargas_Documentos//ComprobacionFacturaImp!net.giro.cargas.documentos.dao.ComprobacionFacturaDAO");

			modulo = "CARGAS_DOCUMENTOS";
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear FacturasFac", e);
			ctx = null;
		}
	}
	
	@Override
	public Respuesta analizarXML(byte[] archivoSrc) {
		Respuesta respuesta = new Respuesta();
		
		try {
			Serializer serializer = new Persister();
			
			String xml = readFile(archivoSrc);
			Comprobante pojoComprobante = serializer.read(Comprobante.class, xml);
			
			String expresionImpresa = "?re=" + pojoComprobante.getEmisor().getRfc();
			expresionImpresa += "&rr=" + pojoComprobante.getReceptor().getRfc();
			expresionImpresa += "&tt=" + pojoComprobante.getTotal();
			expresionImpresa += "&id=" + pojoComprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			
			// Comprobamos que el documento especificado no se halla subido previamente
			List<ComprobacionFactura> listFacs = new ArrayList<ComprobacionFactura>();
			listFacs = ifzComprobacionFactura.findByColumnName("expresionImpresa", expresionImpresa);
			if (listFacs.size() > 0) {
				for(ComprobacionFactura aux : listFacs) {
					if (!"cancelado".equals(aux.getEstado().toLowerCase())) {
						respuesta.getErrores().addCodigo(modulo, Errores.ERROR_XML_PREVIO);
						respuesta.getBody().addValor("idComprobante", aux.getId()); // respuesta.setBody(null);
						return respuesta;
					}
				}
			}
			
			Acuse pojoAcuse = consultaCFDI(expresionImpresa);			
			ComprobacionFactura pojoComprobacionFactura = new ComprobacionFactura();
			
			pojoComprobacionFactura.setExpresionImpresa(expresionImpresa);
			pojoComprobacionFactura.setEstado(pojoAcuse.getEstado().getValue());
			pojoComprobacionFactura.setCodigoEstatus(pojoAcuse.getCodigoEstatus().getValue());
			pojoComprobacionFactura.setFechaComprobacion(Calendar.getInstance().getTime());
			pojoComprobacionFactura.setCreadoPor(infoSesion.getAcceso().getId());
			pojoComprobacionFactura.setFechaCreacion(Calendar.getInstance().getTime());
			pojoComprobacionFactura.setModificadoPor(infoSesion.getAcceso().getId());
			pojoComprobacionFactura.setFechaModificacion(Calendar.getInstance().getTime());
			
			// Guardamos
			long idComprobante = ifzComprobacionFactura.save(pojoComprobacionFactura);
			pojoComprobacionFactura.setId(idComprobante);
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("idComprobante", idComprobante); 
			respuesta.getBody().addValor("pojoComprobante", pojoComprobante);
			respuesta.getBody().addValor("pojoAcuse", pojoAcuse);
		} catch (Exception e) {
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ANALIZAR_XML);
			respuesta.setBody(null);
			log.error("Error en FacturasFac.analizarXML", e);
		}
		
		return respuesta;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Acuse consultaCFDI(String expresionImpresa){
		try{
			
			if(consulta == null || consultaService == null || iConsultaService == null){
				consulta = new Consulta();
				consultaService = new ConsultaCFDIService();
				iConsultaService = consultaService.getBasicHttpBindingIConsultaCFDIService();
			}
			
			Acuse acuse = iConsultaService.consulta(expresionImpresa);
			
			return acuse;
		} catch (Exception e){
			Acuse acuse = new Acuse();
			acuse.setCodigoEstatus(new JAXBElement(new QName("http://schemas.datacontract.org/2004/07/Sat.Cfdi.Negocio.ConsultaCfdi.Servicio", "CodigoEstatus"), String.class, "Error al consultar comprobante"));
			acuse.setCodigoEstatus(new JAXBElement(new QName("http://schemas.datacontract.org/2004/07/Sat.Cfdi.Negocio.ConsultaCfdi.Servicio", "Estado"), String.class, "Error al obtener estatus"));
			
			log.error("Error en FacturasFac.consultaCFDI", e);
			
			return acuse;
		}
	}
	
	private String readFile(byte[] archivoSrc) throws Exception{
		try{
			InputStream stream = new ByteArrayInputStream(archivoSrc);
			BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			
			StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        String fileString = sb.toString();
	        
	        if(fileString.charAt(0) != '<')
				fileString = fileString.substring(1, fileString.length() - 1);
	        
	        return fileString;
		} catch (Exception e){
			log.error("Error en FacturasFac.readFile", e);
			throw e;
		}
		
	}

	public InfoSesion getInfoSesion() {
		return infoSesion;
	}

	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}
	
	public void setInfoSesion(Date ultimaModificacion, Long idAcceso) {
		this.infoSesion = new InfoSesion();
		this.infoSesion.setUltimaModificacion(ultimaModificacion);
		
		Acceso pojoAcceso = new Acceso();
		pojoAcceso.setId(idAcceso);
		
		this.infoSesion.setAcceso(pojoAcceso);
	}

}
