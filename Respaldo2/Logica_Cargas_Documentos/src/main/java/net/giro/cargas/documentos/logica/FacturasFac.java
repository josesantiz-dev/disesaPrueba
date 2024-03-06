package net.giro.cargas.documentos.logica;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.giro.cargas.documentos.beans.ComprobacionFactura;
import net.giro.cargas.documentos.beans.Comprobante;
import net.giro.cargas.documentos.dao.ComprobacionFacturaDAO;
import net.giro.cargas.documentos.respuesta.Errores;
import net.giro.comun.ExcepConstraint;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.seguridad.beans.Acceso;
import net.giro.respuesta.Respuesta;

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
	private Consulta consulta;
	private ConsultaCFDIService consultaService;
	private IConsultaCFDIService iConsultaService;
	private ComprobacionFacturaDAO ifzComprobacionFactura;
	
	public FacturasFac() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		
		try {
			this.infoSesion = new InfoSesion();
			p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

			this.ctx = new InitialContext(p);
			this.ifzComprobacionFactura = (ComprobacionFacturaDAO) this.ctx.lookup("ejb:/Model_Cargas_Documentos//ComprobacionFacturaImp!net.giro.cargas.documentos.dao.ComprobacionFacturaDAO");
			
			this.modulo = "CARGAS_DOCUMENTOS";
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear FacturasFac", e);
			ctx = null;
		}
	}

	@Override
	public InfoSesion getInfoSesion() {
		return infoSesion;
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public void setInfoSesion(Date ultimaModificacion, Long idAcceso) {
		Acceso pojoAcceso = new Acceso();
		this.infoSesion = new InfoSesion();
		this.infoSesion.setUltimaModificacion(ultimaModificacion);
		pojoAcceso.setId(idAcceso);
		this.infoSesion.setAcceso(pojoAcceso);
	}
	
	@Override
	public Long save(ComprobacionFactura entity) throws Exception {
		try {
			return this.ifzComprobacionFactura.save(entity, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.FacturasFac.save(ComprobacionFactura)", e);
			throw e;
		}
	}

	@Override
	public List<ComprobacionFactura> saveOrUpdateList(List<ComprobacionFactura> entities) throws Exception {
		try {
			return this.ifzComprobacionFactura.saveOrUpdateList(entities, getIdEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Publico.FacturasFac.saveOrUpdateList(List<ComprobacionFactura> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(ComprobacionFactura entity) throws Exception {
		try {
			this.ifzComprobacionFactura.update(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void cancelar(long idComprobanteFactura) throws Exception {
		try {
			this.cancelar(this.ifzComprobacionFactura.findById(idComprobanteFactura));
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void cancelar(ComprobacionFactura entity) throws Exception {
		try {
			entity.setEstatus(1);
			entity.setFechaModificacion(Calendar.getInstance().getTime());
			if (this.infoSesion != null && this.infoSesion.getAcceso() != null && this.infoSesion.getAcceso().getUsuario() != null && this.infoSesion.getAcceso().getUsuario().getId() > 0L)
				entity.setModificadoPor(this.infoSesion.getAcceso().getUsuario().getId());
			else
				entity.setModificadoPor(entity.getModificadoPor() * -1);
			this.ifzComprobacionFactura.update(entity);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Respuesta analizarXML(byte[] archivoSrc) {
		Respuesta respuesta = new Respuesta();
		List<ComprobacionFactura> listFacs = null;
		Serializer serializer = new Persister();
		ComprobacionFactura pojoComprobacionFactura = null;
		Comprobante pojoComprobante = null;
		Acuse pojoAcuse = null;
		byte[] xmlb = null;
		String expresionImpresa = "";
		String xml = "";
		String stepTrace = "";
		
		try {
			stepTrace += "|leyendo archivo";
			xml = readFile(archivoSrc);
			stepTrace += "|minificando";
			xmlb = aMinusculas(xml);
			stepTrace += "|serializando";
			pojoComprobante = serializer.read(Comprobante.class, new ByteArrayInputStream(xmlb), false);

			stepTrace += "|expresion-impresa";
			expresionImpresa = "?re=" + pojoComprobante.getEmisor().getRfc();
			expresionImpresa += "&rr=" + pojoComprobante.getReceptor().getRfc();
			expresionImpresa += "&tt=" + pojoComprobante.getTotal();
			expresionImpresa += "&id=" + pojoComprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			
			// Comprobamos que el documento especificado no se halla subido previamente
			stepTrace += "|comprobando-previo";
			listFacs = this.ifzComprobacionFactura.findByColumnName("expresionImpresa", expresionImpresa);
			if (listFacs != null && ! listFacs.isEmpty()) {
				for (ComprobacionFactura aux : listFacs) {
					if (aux.getEstatus() == 0 && ! "cancelado".equals(aux.getEstado().toLowerCase())) {
						stepTrace += "|xml-previo-" + aux.getId();
						log.info("ERROR_XML_PREVIO - ID DOCUMENTO " + aux.getId());
						respuesta.getErrores().addCodigo(modulo, Errores.ERROR_XML_PREVIO);
						respuesta.getErrores().setCodigoError(Errores.ERROR_XML_PREVIO);
						respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_XML_PREVIO));
						respuesta.getBody().addValor("idComprobante", aux.getId()); 
						respuesta.getBody().addValor("stepTrace", stepTrace);
						return respuesta;
					}
				}
			}
			
			// Recupero el Acuse
			stepTrace += "|recuperando-acuse";
			pojoAcuse = consultaCFDI(expresionImpresa);
			
			// Genero Comprobacion de Factura
			stepTrace += "|genero-ComprobacionFactura";
			pojoComprobacionFactura = new ComprobacionFactura();
			if (pojoAcuse != null) {
				stepTrace += "|acuse-recuperado";
				pojoComprobacionFactura.setEstado(pojoAcuse.getEstado().getValue());
				pojoComprobacionFactura.setCodigoEstatus(pojoAcuse.getCodigoEstatus().getValue());
			} else {
				stepTrace += "|acuse-nulo";
				pojoComprobacionFactura.setEstado("Error al obtener estatus");
				pojoComprobacionFactura.setCodigoEstatus("Error al consultar comprobante");
			}
			pojoComprobacionFactura.setFechaComprobacion(Calendar.getInstance().getTime());
			pojoComprobacionFactura.setExpresionImpresa(expresionImpresa);
			pojoComprobacionFactura.setFacturaFolioFiscal(pojoComprobante.getComplemento().getTimbreFiscalDigital().getUUID());
			if (pojoComprobante.getSerie() != null)
				pojoComprobacionFactura.setFacturaSerie(pojoComprobante.getSerie());
			if (pojoComprobante.getFolio() != null)
				pojoComprobacionFactura.setFacturaFolio(pojoComprobante.getFolio());
			if (pojoComprobante.getEmisor().getNombre() != null)
				pojoComprobacionFactura.setFacturaRazonSocial(pojoComprobante.getEmisor().getNombre());
			pojoComprobacionFactura.setCreadoPor(1L);
			pojoComprobacionFactura.setFechaCreacion(Calendar.getInstance().getTime());
			pojoComprobacionFactura.setModificadoPor(1L);
			pojoComprobacionFactura.setFechaModificacion(Calendar.getInstance().getTime());
			if (this.infoSesion != null) {
				pojoComprobacionFactura.setCreadoPor(this.infoSesion.getAcceso().getId());
				pojoComprobacionFactura.setModificadoPor(this.infoSesion.getAcceso().getId());
			} 
			
			// Guardamos
			stepTrace += "|guardamos-ComprobacionFactura";
			pojoComprobacionFactura.setId(this.ifzComprobacionFactura.save(pojoComprobacionFactura));
			stepTrace += "-OK-" + pojoComprobacionFactura.getId();

			stepTrace += "|generando-respuesta";
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getBody().addValor("pojoComprobante", pojoComprobante);
			respuesta.getBody().addValor("pojoComprobacionFactura", pojoComprobacionFactura);
			respuesta.getBody().addValor("idComprobante", pojoComprobacionFactura.getId()); 
			respuesta.getBody().addValor("comprobanteFactura", pojoComprobacionFactura.getFactura()); 
			respuesta.getBody().addValor("comprobanteSerie", pojoComprobacionFactura.getFacturaSerie()); 
			respuesta.getBody().addValor("comprobanteFolio", pojoComprobacionFactura.getFacturaFolio()); 
			respuesta.getBody().addValor("comprobanteRfc", pojoComprobacionFactura.getRfcEmisor()); 
			respuesta.getBody().addValor("comprobanteRazonSocial", pojoComprobacionFactura.getFacturaRazonSocial()); 
			respuesta.getBody().addValor("comprobanteRazonSocialRfc", pojoComprobacionFactura.getRazonSocialORfc()); 
			respuesta.getBody().addValor("comprobanteTotal", pojoComprobacionFactura.getTotal()); 
			respuesta.getBody().addValor("stepTrace", stepTrace);
			stepTrace += "|OK-añado-acuse";
			respuesta.getBody().addValor("pojoAcuse", pojoAcuse);
			respuesta.getBody().addValor("stepTrace", stepTrace);
		} catch (Exception e) {
			log.error("Error en FacturasFac.analizarXML. STEPS -> " + stepTrace, e);
			respuesta.getErrores().addCodigo(modulo, Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_ANALIZAR_XML));
			respuesta.getBody().addValor("stepTrace", stepTrace);
		}
		
		return respuesta;
	}

	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void deleteXML(long idComprobane) {
		try {
			this.ifzComprobacionFactura.delete(idComprobane);
		} catch (Exception e) {
			log.error("Error en FacturasFac.deleteXML", e);
		}
	}

	@Override
	public String getProperty(long idFactura, String property) {
		ComprobacionFactura pojo = null;
		String resultado = "";

		pojo = this.ifzComprobacionFactura.findById(idFactura);
		if (pojo != null) {
			if ("uuid".equals(property))
				resultado = pojo.getFacturaFolioFiscal();
			if ("nombre".equals(property))
				resultado = pojo.getRazonSocialORfc();
			if ("factura".equals(property))
				resultado = pojo.getFactura();
			if ("serie".equals(property))
				resultado = pojo.getFacturaSerie();
			if ("folio".equals(property))
				resultado = pojo.getFacturaFolio();
			if ("rfcEmisor".equals(property))
				resultado = pojo.getRfcEmisor();
			if ("rfcReceptor".equals(property))
				resultado = pojo.getRfcReceptor();
			if ("total".equals(property))
				resultado = pojo.getTotal();
		}
		
		return resultado;
	}

	@Override
	public void setProperty(long idFactura, String property, String value) throws Exception {
		ComprobacionFactura pojo = this.ifzComprobacionFactura.findById(idFactura);
		if (pojo != null) {
			if ("nombre".equals(property))
				pojo.setFacturaRazonSocial(value);
			if ("serie".equals(property))
				pojo.setFacturaSerie(value);
			if ("folio".equals(property))
				pojo.setFacturaFolio(value);
			
			try {
				this.ifzComprobacionFactura.update(pojo);
			} catch (ExcepConstraint e) {
				log.error("Error en FacturasFac.setProperty", e);
				throw e;
			}
		}
	}

	private Long getIdEmpresa() {
		Long resultado = 1L;
		
		if (this.infoSesion != null) {
			resultado = this.infoSesion.getEmpresa().getId();
			resultado = (resultado != null && resultado > 0L ? resultado : 1L);
		}
		
		return resultado;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Acuse consultaCFDI(String expresionImpresa){
		Acuse acuse = null;
		String stepTrace = "";
		
		try {
			if (this.consulta == null || this.consultaService == null || this.iConsultaService == null) {
				stepTrace += "|genero-Consulta";
				this.consulta = new Consulta();
				stepTrace += "|genero-ConsultaCFDIService";
				this.consultaService = new ConsultaCFDIService();
				stepTrace += "|obtengo-getBasicHttpBindingIConsultaCFDIService";
				this.iConsultaService = this.consultaService.getBasicHttpBindingIConsultaCFDIService();
			}

			stepTrace += "|iConsultaService.consulta(expresionImpresa)";
			acuse = this.iConsultaService.consulta(expresionImpresa);
			return acuse;
		} catch (Exception e) {
			log.error("Error en FacturasFac.consultaCFDI: Error al intentar recuperar el ACUSE desde el servicio.\n\nSTEPS\n" + stepTrace + "\n\nException:\n", e);
		}

		try {
			stepTrace = "|genero-Acuse";
			acuse = new Acuse();
			stepTrace += "|setCodigoEstatus(Error al consultar comprobante)";
			acuse.setCodigoEstatus(new JAXBElement(new QName("http://schemas.datacontract.org/2004/07/Sat.Cfdi.Negocio.ConsultaCfdi.Servicio", "CodigoEstatus"), String.class, "Error al consultar comprobante"));
			stepTrace += "|setCodigoEstatus(Error al obtener estatus)";
			acuse.setCodigoEstatus(new JAXBElement(new QName("http://schemas.datacontract.org/2004/07/Sat.Cfdi.Negocio.ConsultaCfdi.Servicio", "Estado"), String.class, "Error al obtener estatus"));
			return acuse;
		} catch (Exception e) {
			log.error("Error en FacturasFac.consultaCFDI al generar acuse, devolvere acuse nulo.\n\nSTEPS\n" + stepTrace + "\n\nException:\n", e);
		}
		
		return null;
	}
	
	private String readFile(byte[] archivoSrc) throws Exception {
		try {
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
			if (fileString.charAt(0) != '<')
				fileString = fileString.substring(1, fileString.length() - 1);
			
			return fileString;
		} catch (Exception e){
			log.error("Error en FacturasFac.readFile", e);
			throw e;
		}
	}
	
	private byte[] aMinusculas(String xml) throws IOException, URISyntaxException, TransformerException {
		ByteArrayOutputStream bf = null;
		TransformerFactory factory = null;
		Transformer transformer = null;
		Source xslt = null;
		Source text = null;
		
		factory = TransformerFactory.newInstance();
		bf = new ByteArrayOutputStream();
		text = new StreamSource(new ByteArrayInputStream(xml.getBytes()));
		xslt = new StreamSource(this.getClass().getResourceAsStream("/net/giro/cargas/documentos/beans/min1.xslt"));
		transformer = factory.newTransformer(xslt);
		transformer.transform(text, new StreamResult(bf));
		
		return bf.toByteArray();
	}
}
