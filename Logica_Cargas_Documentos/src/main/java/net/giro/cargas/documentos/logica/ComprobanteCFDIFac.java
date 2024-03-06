package net.giro.cargas.documentos.logica;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import mx.gob.sat.cfdi.v33.Comprobante;
import mx.gob.sat.cfdi.v33.complementos.TimbreFiscalDigital11.TimbreFiscalDigital;
import mx.gob.sat.cfdi.v33.complementos.nomina12.Nomina;
import mx.gob.sat.cfdi.v33.complementos.pagos10.Pagos.Pago;
import net.giro.cargas.documentos.beans.ComprobacionFactura;
import net.giro.cargas.documentos.beans.ComprobacionNomina;
import net.giro.cargas.documentos.beans.ComprobacionPago;
import net.giro.cargas.documentos.respuesta.Errores;
import net.giro.cargas.documentos.util.TipoDeComprobante;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.respuesta.Respuesta;

@Stateless
public class ComprobanteCFDIFac implements ComprobanteCFDIRem {
	private static Logger log = Logger.getLogger(ComprobanteCFDIFac.class);
	private InfoSesion infoSesion;
	// --------------------------------------------------------------------
	private ComprobacionFacturaRem ifzFacturas;
	private ComprobacionNominaRem ifzNominas;
	private ComprobacionPagosRem ifzPagos;
	private FtpRem ifzFtp;
	
	public ComprobanteCFDIFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzFacturas = (ComprobacionFacturaRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//ComprobacionFacturaFac!net.giro.cargas.documentos.logica.ComprobacionFacturaRem");
			this.ifzNominas = (ComprobacionNominaRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//ComprobacionNominaFac!net.giro.cargas.documentos.logica.ComprobacionNominaRem");
			this.ifzPagos = (ComprobacionPagosRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//ComprobacionPagosFac!net.giro.cargas.documentos.logica.ComprobacionPagosRem");
    		this.ifzFtp = (FtpRem) ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar " + this.getClass().getCanonicalName(), e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	public Respuesta importarXML(byte[] archivoSrc, String prefijo) throws Exception {
		Respuesta respuesta = null;
		// CFDI v33
		Comprobante cfdi = null;
		TimbreFiscalDigital timbre = null;
		// --------------------------------------------------------------------------------------------------------------
		DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document doc = null;
        Element element = null;
		String xml = "";
		String version = "";
		String tipoComprobante = "";
		// --------------------------------------------------------------------------------------------------------------
		Long idComprobante = 0L;
		String expresionImpresa = "";
		String cfdiFilename = "";
		boolean storage = false;
		HashMap<String, Object> valores = null;
		String valoresJson = null;
		
		try {
			// Validando sesion
			respuesta = new Respuesta();
			if (this.infoSesion == null) {
				log.info("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_INESPERADO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_INESPERADO);
				respuesta.getErrores().setDescError("No se puede comprobar la sesion de Usuario");
				return respuesta;
			}

			// Inicializando XML
			prefijo = (prefijo != null && ! "".equals(prefijo.trim()) ? (prefijo + (! prefijo.endsWith("-") ? "-" : "")) : "");
			xml = readFile(archivoSrc);
			// Generando Documento
			factory = DocumentBuilderFactory.newInstance();  
            builder = factory.newDocumentBuilder();  
            doc = builder.parse(new InputSource(new StringReader(xml))); 
            element = doc.getDocumentElement();
            // Recuperamos atributos
            version = (element.hasAttribute("version") ? element.getAttribute("version") : "3.3");
            tipoComprobante = (element.hasAttribute("tipoDeComprobante") ? element.getAttribute("tipoDeComprobante") : tipoComprobante);
            tipoComprobante = ("".equals(tipoComprobante.trim()) && element.hasAttribute("TipoDeComprobante") ? element.getAttribute("TipoDeComprobante") : tipoComprobante);
            tipoComprobante = TipoDeComprobante.fromString(tipoComprobante).value();

        	// tratamos 3.2 si corresponde
            if ("3.2".equals(version)) 
            	return comprobante32(archivoSrc, prefijo);
            
			// Serializando: Generamos comprobante 3.3 y recuperamos timbre
			cfdi = generaComprobante(archivoSrc);
			timbre = getTimbreFiscalDigital(cfdi.getComplemento());
			// Generando Comprobacion
			cfdi.setSerie(setDefaultValue(cfdi.getSerie(), ""));
			cfdi.setFolio(setDefaultValue(cfdi.getFolio(), timbre.getUUID().substring(timbre.getUUID().length() - 8)));

			// Comprobamos que el XML va dirijido a la empresa actualmente cargada
			if (! comprobarEmpresa(cfdi.getReceptor().getRfc(), cfdi.getEmisor().getRfc())) {
				log.info(Errores.ERROR_EMPRESA_INVALIDA + " - " + Errores.descError.get(Errores.ERROR_EMPRESA_INVALIDA) + ": " + cfdi.getReceptor().getRfc());
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setCodigoError(Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_EMPRESA_INVALIDA));
				return respuesta;
			}
			
            if ("IE".contains(tipoComprobante)) {
    			this.ifzFacturas.setInfoSesion(this.infoSesion);
    			respuesta = this.ifzFacturas.importarCFDI(cfdi); 
            } else if ("P".equals(tipoComprobante)) {
    			this.ifzPagos.setInfoSesion(this.infoSesion);
    			respuesta = this.ifzPagos.importarCFDI(cfdi); 
            } else if ("N".equals(tipoComprobante)) {
    			this.ifzNominas.setInfoSesion(this.infoSesion);
    			respuesta = this.ifzNominas.importarCFDI(cfdi); 
            } else {
				log.info("Ocurrio un problema al interpretar el Tipo de Comprobante del CFDI indicado");
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_INESPERADO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_INESPERADO);
				respuesta.getErrores().setDescError("No se pudo interpretar el Tipo de Comprobante del CFDI indicado");
				return respuesta;
            }
            
            // Guardamos en servidor
            idComprobante = (Long) respuesta.getBody().getValor("idComprobante"); 
            expresionImpresa = respuesta.getBody().getValor("expresionImpresa").toString(); 
            cfdiFilename = prefijo + tipoComprobante + "-" + idComprobante + ".xml";
            storage = storage(archivoSrc, cfdiFilename);
            valores = mapearComprobante(cfdi, expresionImpresa, idComprobante);
            valoresJson = mapaJson(valores);
            
			respuesta.getBody().addValor("storage", storage);
			respuesta.getBody().addValor("storageName", cfdiFilename);
			respuesta.getBody().addValor("cfdi_map", valores);
			respuesta.getBody().addValor("cfdi_json", valoresJson);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
		} catch (Exception e) {
			log.error("Ocurrio un problema al importar el CFDI indicado", e);
			respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_ANALIZAR_XML));
		} 
		
		return respuesta;
	}

	@Override
	public Respuesta exportarXML(long idComprobacion, TipoDeComprobante tipoDeComprobante, String prefijo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean storage(byte[] archivoSrc, String filename) throws Exception {
		try {
			filename = (filename != null && ! "".equals(filename.trim()) ? filename.trim() : "");
			if ("".equals(filename.trim()))
				return false;
			
            // Guardamos en servidor
            return this.ifzFtp.put(archivoSrc, filename);
		} catch (Exception e) {
			log.error("Ocurrio un problema al importar el CFDI indicado ", e);
			return false;
		} 
	}

	// -----------------------------------------------------------------------------------------------
	// PRIVADOS
	// -----------------------------------------------------------------------------------------------

	@SuppressWarnings("unused")
	private ComprobacionFactura ingresoEgresoById(Long idComprobacionFactura) throws Exception {
		return null;
	}

	@SuppressWarnings("unused")
	private ComprobacionPago pagoById(Long idComprobacionPago) throws Exception {
		return null;
	}

	@SuppressWarnings("unused")
	private ComprobacionNomina nominaById(Long idComprobacionNomina) throws Exception {
		return null;
	}

	private BigDecimal validateBigDecimal(String value) {
		return (value != null && ! "".equals(value.trim()) ? new BigDecimal(value) : BigDecimal.ZERO);
	}
	
	private String validateString(String value) {
		return (value != null && ! "".equals(value.trim()) ? value.trim() : "");
	}
	
	private String setDefaultValue(String value, String defaultValue) {
		return (value != null && ! "".equals(value.trim()) ? value.trim() : validateString(defaultValue));
	}

	@SuppressWarnings("rawtypes")
	private Comprobante generaComprobante(byte[] fileSource) {
		Comprobante cfdi = null;
		JAXBContext context = null;
		Unmarshaller unmarshaller = null;
		Class[] classestoBeBound = null;
		
		try {
			if (fileSource == null) 
				return null;
			// Clases: CFDI + complementos
			classestoBeBound = new Class[] {Comprobante.class, TimbreFiscalDigital.class, Nomina.class, Pago.class};
			context = JAXBContext.newInstance(classestoBeBound);
			unmarshaller = context.createUnmarshaller();
			cfdi = (Comprobante) unmarshaller.unmarshal(new ByteArrayInputStream(fileSource));
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar interpretar el CFDI. No se puede generar el Objeto Comprobante", e);
			return null;
		}
		
		return cfdi;
	}
	
	private TimbreFiscalDigital getTimbreFiscalDigital(List<Comprobante.Complemento> complementos) {
		try {
			if (complementos == null || complementos.isEmpty()) 
				return null;
			for (Comprobante.Complemento item : complementos) {
				if (item.getAny() == null || item.getAny().isEmpty())
					continue;
				for (Object value : item.getAny()) {
					if (value instanceof TimbreFiscalDigital)
						return (TimbreFiscalDigital) value;
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el nodo TimbreFiscalDigital de los Complementos", e);
		}
		
		return null;
	}

	private Respuesta comprobante32(byte[] fileSource, String prefijo) {
		Respuesta respuesta = null;
		// CFDI 
		mx.gob.sat.cfdi.v32.Comprobante cfdi = null;
		mx.gob.sat.cfdi.v32.complementos.TimbreFiscalDigital10.TimbreFiscalDigital timbre = null;
		// --------------------------------------------------------------------------------------------------------------
		Long idComprobante = 0L;
		String cfdiFilename = "";
		String expresionImpresa = "";
		boolean storage = false;
		HashMap<String, Object> valores = null;
		String valoresJson = null;

		
		try {
			respuesta = new Respuesta();
			// Serializando: Generamos comprobante 3.3 y recuperamos timbre
			cfdi = generaComprobante32(fileSource);
			timbre = getTimbreFiscalDigital10(cfdi.getComplemento());
			// Generando Comprobacion
			cfdi.setSerie(setDefaultValue(cfdi.getSerie(), ""));
			cfdi.setFolio(setDefaultValue(cfdi.getFolio(), timbre.getUUID().substring(timbre.getUUID().length() - 8)));
	
			// Comprobamos que el XML va dirijido a la empresa actualmente cargada
			if (! comprobarEmpresa(cfdi.getReceptor().getRfc(), cfdi.getEmisor().getRfc())) {
				log.info(Errores.ERROR_EMPRESA_INVALIDA + " - " + Errores.descError.get(Errores.ERROR_EMPRESA_INVALIDA) + ": " + cfdi.getReceptor().getRfc());
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setCodigoError(Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_EMPRESA_INVALIDA));
				return respuesta;
			}

			this.ifzFacturas.setInfoSesion(this.infoSesion);
			respuesta = this.ifzFacturas.analizarXML(fileSource, false); 
			
            // Guardamos en servidor
            idComprobante = (Long) respuesta.getBody().getValor("idComprobante"); 
            expresionImpresa = respuesta.getBody().getValor("expresionImpresa").toString(); 
            cfdiFilename = prefijo + idComprobante + ".xml";
            storage = storage(fileSource, cfdiFilename);
            valores = mapearComprobante(cfdi, expresionImpresa, idComprobante);
            valoresJson = mapaJson(valores);
            
			respuesta.getBody().addValor("storage", storage);
			respuesta.getBody().addValor("storageName", cfdiFilename);
			respuesta.getBody().addValor("cfdi_map", valores);
			respuesta.getBody().addValor("cfdi_json", valoresJson);
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
		} catch (Exception e) {
			log.error("Ocurrio un problema al importar el CFDI indicado", e);
			respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_ANALIZAR_XML));
		} 
		
		return respuesta;
	}

	@SuppressWarnings("rawtypes")
	private mx.gob.sat.cfdi.v32.Comprobante generaComprobante32(byte[] fileSource) {
		mx.gob.sat.cfdi.v32.Comprobante cfdi = null;
		JAXBContext context = null;
		Unmarshaller unmarshaller = null;
		Class[] classestoBeBound = null;
		
		try {
			if (fileSource == null) 
				return null;
			// Clases: CFDI + complementos
			classestoBeBound = new Class[] {mx.gob.sat.cfdi.v32.Comprobante.class, mx.gob.sat.cfdi.v32.complementos.TimbreFiscalDigital10.TimbreFiscalDigital.class};
			context = JAXBContext.newInstance(classestoBeBound);
			unmarshaller = context.createUnmarshaller();
			cfdi = (mx.gob.sat.cfdi.v32.Comprobante) unmarshaller.unmarshal(new ByteArrayInputStream(fileSource));
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar interpretar el CFDI v32. No se puede generar el Objeto Comprobante", e);
			return null;
		}
		
		return cfdi;
	}

	private mx.gob.sat.cfdi.v32.complementos.TimbreFiscalDigital10.TimbreFiscalDigital getTimbreFiscalDigital10(mx.gob.sat.cfdi.v32.Comprobante.Complemento complemento) {
		try {
			if (complemento == null) 
				return null;
			for (Object value : complemento.getAny()) {
				if (value instanceof mx.gob.sat.cfdi.v32.complementos.TimbreFiscalDigital10.TimbreFiscalDigital)
					return (mx.gob.sat.cfdi.v32.complementos.TimbreFiscalDigital10.TimbreFiscalDigital) value;
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el nodo TimbreFiscalDigital (CFDI v32) de los Complementos", e);
		}
		
		return null;
	}

	private HashMap<String, Object> mapearComprobante(Comprobante cfdi, String expresionImpresa, Long idComprobante) {
		HashMap<String, Object> valores = null;
		List<HashMap<String, Object>> conceptos = null;
		HashMap<String, Object> concepto = null;
		// ---------------------------------------------------------------------------------------
		TimbreFiscalDigital tfd = null;
		String factura = "";
		String serie = "";
		String folio = "";
		String concepto1 = "";
		BigDecimal tipoCambio = BigDecimal.ZERO;
		BigDecimal descuento = BigDecimal.ZERO;
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal impuestos = BigDecimal.ZERO;
		BigDecimal retenciones = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalPesos = BigDecimal.ZERO;
		BigDecimal conceptoImpuestos = BigDecimal.ZERO;
		BigDecimal conceptoRetenciones = BigDecimal.ZERO;
		
		try {
			if (cfdi == null)
				return null;

			serie = validateString(cfdi.getSerie());
			folio = validateString(cfdi.getFolio());
			factura = serie + (! "".equals(serie) ? "-" : "") + folio;
			tfd = getTimbreFiscalDigital(cfdi.getComplemento());
			tipoCambio = (cfdi.getTipoCambio() != null && cfdi.getTipoCambio().doubleValue() > 0 ? cfdi.getTipoCambio() : BigDecimal.ZERO);
			descuento = (cfdi.getDescuento() != null && cfdi.getDescuento().doubleValue() > 0 ? cfdi.getDescuento() : BigDecimal.ZERO);
			subtotal = (cfdi.getSubTotal() != null && cfdi.getSubTotal().doubleValue() > 0 ? cfdi.getSubTotal() : BigDecimal.ZERO);
			impuestos = (cfdi.getImpuestos() != null && cfdi.getImpuestos().getTotalImpuestosTrasladados() != null && cfdi.getImpuestos().getTotalImpuestosTrasladados() .doubleValue() > 0 ? cfdi.getImpuestos().getTotalImpuestosTrasladados()  : BigDecimal.ZERO);
			retenciones = (cfdi.getImpuestos() != null && cfdi.getImpuestos().getTotalImpuestosRetenidos() != null && cfdi.getImpuestos().getTotalImpuestosRetenidos().doubleValue() > 0 ? cfdi.getImpuestos().getTotalImpuestosRetenidos() : BigDecimal.ZERO);
			total = (cfdi.getTotal() != null && cfdi.getTotal().doubleValue() > 0 ? cfdi.getTotal() : BigDecimal.ZERO);
			
			tipoCambio  = (tipoCambio.doubleValue() > 0 ? tipoCambio : new BigDecimal(1));
			totalPesos = (this.infoSesion.getEmpresa().getMoneda().equals(cfdi.getMoneda().value()) ? total : total.multiply(tipoCambio));
			
			for (Comprobante.Conceptos.Concepto item : cfdi.getConceptos().getConcepto()) {
				// Primer concepto
				concepto1 = (! "".equals(concepto1.trim()) ? concepto1.trim() : item.getDescripcion());
				
				// Obtenemos importes de impuestos
				if (item.getImpuestos() != null) {
					if (item.getImpuestos().getTraslados() != null && item.getImpuestos().getTraslados().getTraslado() != null && ! item.getImpuestos().getTraslados().getTraslado().isEmpty()) {
						for (Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado var : item.getImpuestos().getTraslados().getTraslado())
							conceptoImpuestos = conceptoImpuestos.add(var.getImporte());
					}
					
					if (item.getImpuestos().getRetenciones() != null && item.getImpuestos().getRetenciones().getRetencion() != null && ! item.getImpuestos().getRetenciones().getRetencion().isEmpty()) {
						for (Comprobante.Conceptos.Concepto.Impuestos.Retenciones.Retencion var : item.getImpuestos().getRetenciones().getRetencion())
							conceptoRetenciones = conceptoRetenciones.add(var.getImporte());
					}
				}
				
				// Creamos el concepto
				concepto = new HashMap<String, Object>();
				concepto.put("noIdentificacion", item.getNoIdentificacion());
				concepto.put("cantidad", item.getCantidad());
				concepto.put("descripcion", item.getDescripcion());
				concepto.put("precioUnitario", item.getValorUnitario());
				concepto.put("descuento", item.getDescuento());
				concepto.put("importe", item.getImporte());
				concepto.put("impuestos", conceptoImpuestos);
				concepto.put("retenciones", conceptoRetenciones);
				concepto.put("unidad", item.getUnidad());
				concepto.put("claveProdServ", item.getClaveProdServ());
				concepto.put("claveUnidad", item.getClaveUnidad());
				// Añadimos el concepto
				conceptos = (conceptos != null ? conceptos : new ArrayList<HashMap<String, Object>>());
				conceptos.add(concepto);
			}

			valores = new HashMap<String, Object>();
			valores.put("idComprobante", idComprobante);
			valores.put("expresionImpresa", expresionImpresa);
			valores.put("serie", serie); 
			valores.put("folio", folio); 
			valores.put("factura", factura); 
			valores.put("uuid", tfd.getUUID());
			valores.put("version", cfdi.getVersion());
			valores.put("fecha", cfdi.getFecha());
			valores.put("tipoDeComprobante", cfdi.getTipoDeComprobante().value());
			valores.put("formaDePago", validateString(cfdi.getFormaPago()));
			valores.put("metodoDePago", cfdi.getMetodoPago().value());
			valores.put("emisor", cfdi.getEmisor().getNombre());
			valores.put("emisorRFC", cfdi.getEmisor().getRfc());
			valores.put("emisorRegimenFiscal", cfdi.getEmisor().getRegimenFiscal());
			valores.put("receptor", cfdi.getReceptor().getNombre());
			valores.put("receptorRFC", cfdi.getReceptor().getRfc());
			valores.put("receptorUsoCFDI", cfdi.getReceptor().getUsoCFDI().value());
			valores.put("moneda", cfdi.getMoneda().value());
			valores.put("tipoCambio", tipoCambio);
			valores.put("descuento", descuento); 
			valores.put("subtotal", subtotal); 
			valores.put("impuestos", impuestos); 
			valores.put("retenciones", retenciones); 
			valores.put("total", total); 
			valores.put("totalPesos", totalPesos); 
			valores.put("concepto1", concepto1); 
			valores.put("conceptos", conceptos); 
		} catch (Exception e) {
			log.error("Ocurrio un problema al mapear el CFDI", e);
		}
		
		return valores;
	}

	private HashMap<String, Object> mapearComprobante(mx.gob.sat.cfdi.v32.Comprobante cfdi, String expresionImpresa, Long idComprobante) {
		HashMap<String, Object> valores = null;
		List<HashMap<String, Object>> conceptos = null;
		HashMap<String, Object> concepto = null;
		// ---------------------------------------------------------------------------------------
		mx.gob.sat.cfdi.v32.complementos.TimbreFiscalDigital10.TimbreFiscalDigital tfd = null;
		String factura = "";
		String serie = "";
		String folio = "";
		String concepto1 = "";
		BigDecimal tipoCambio = BigDecimal.ZERO;
		BigDecimal descuento = BigDecimal.ZERO;
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal impuestos = BigDecimal.ZERO;
		BigDecimal retenciones = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalPesos = BigDecimal.ZERO;
		
		try {
			if (cfdi == null)
				return null;

			serie = validateString(cfdi.getSerie());
			folio = validateString(cfdi.getFolio());
			factura = serie + (! "".equals(serie) ? "-" : "") + folio;
			tfd = getTimbreFiscalDigital10(cfdi.getComplemento());
			tipoCambio = validateBigDecimal(cfdi.getTipoCambio());
			descuento = (cfdi.getDescuento() != null && cfdi.getDescuento().doubleValue() > 0 ? cfdi.getDescuento() : BigDecimal.ZERO);
			subtotal = (cfdi.getSubTotal() != null && cfdi.getSubTotal().doubleValue() > 0 ? cfdi.getSubTotal() : BigDecimal.ZERO);
			impuestos = (cfdi.getImpuestos() != null && cfdi.getImpuestos().getTotalImpuestosTrasladados() != null && cfdi.getImpuestos().getTotalImpuestosTrasladados() .doubleValue() > 0 ? cfdi.getImpuestos().getTotalImpuestosTrasladados()  : BigDecimal.ZERO);
			retenciones = (cfdi.getImpuestos() != null && cfdi.getImpuestos().getTotalImpuestosRetenidos() != null && cfdi.getImpuestos().getTotalImpuestosRetenidos().doubleValue() > 0 ? cfdi.getImpuestos().getTotalImpuestosRetenidos() : BigDecimal.ZERO);
			total = (cfdi.getTotal() != null && cfdi.getTotal().doubleValue() > 0 ? cfdi.getTotal() : BigDecimal.ZERO);
			
			tipoCambio  = (tipoCambio.doubleValue() > 0 ? tipoCambio : new BigDecimal(1));
			totalPesos = (this.infoSesion.getEmpresa().getMoneda().equals(cfdi.getMoneda()) ? total : total.multiply(tipoCambio));

			for (mx.gob.sat.cfdi.v32.Comprobante.Conceptos.Concepto item : cfdi.getConceptos().getConcepto()) {
				// Primer concepto
				concepto1 = (! "".equals(concepto1.trim()) ? concepto1.trim() : item.getDescripcion());
				
				// Creamos el concepto
				concepto = new HashMap<String, Object>();
				concepto.put("noIdentificacion", item.getNoIdentificacion());
				concepto.put("cantidad", item.getCantidad());
				concepto.put("descripcion", item.getDescripcion());
				concepto.put("precioUnitario", item.getValorUnitario());
				concepto.put("importe", item.getImporte());
				concepto.put("descuento", BigDecimal.ZERO);
				concepto.put("impuestos", BigDecimal.ZERO);
				concepto.put("retenciones", BigDecimal.ZERO);
				concepto.put("unidad", item.getUnidad());
				concepto.put("claveProdServ", "");
				concepto.put("claveUnidad", "");
				// Añadimos el concepto
				conceptos = (conceptos != null ? conceptos : new ArrayList<HashMap<String, Object>>());
				conceptos.add(concepto);
			}

			valores = new HashMap<String, Object>();
			valores.put("idComprobante", idComprobante);
			valores.put("expresionImpresa", expresionImpresa);
			valores.put("serie", serie); 
			valores.put("folio", folio); 
			valores.put("factura", factura); 
			valores.put("uuid", tfd.getUUID());
			valores.put("version", cfdi.getVersion());
			valores.put("fecha", cfdi.getFecha());
			valores.put("tipoDeComprobante", cfdi.getTipoDeComprobante());
			valores.put("formaDePago", validateString(cfdi.getFormaDePago()));
			valores.put("metodoDePago", cfdi.getMetodoDePago());
			valores.put("emisor", cfdi.getEmisor().getRfc());
			valores.put("emisorRFC", cfdi.getEmisor().getNombre());
			valores.put("emisorRegimenFiscal", cfdi.getEmisor().getRegimenFiscal());
			valores.put("receptor", cfdi.getReceptor().getNombre());
			valores.put("receptorRFC", cfdi.getReceptor().getRfc());
			valores.put("receptorUsoCFDI", "");
			valores.put("moneda", cfdi.getMoneda());
			valores.put("tipoCambio", tipoCambio);
			valores.put("descuento", descuento); 
			valores.put("subtotal", subtotal); 
			valores.put("impuestos", impuestos); 
			valores.put("retenciones", retenciones); 
			valores.put("total", total); 
			valores.put("totalPesos", totalPesos); 
			valores.put("concepto1", concepto1); 
			valores.put("conceptos", conceptos); 
		} catch (Exception e) {
			log.error("Ocurrio un problema al mapear el CFDI v3.2", e);
		}
		
		return valores;
	}
	
	private String mapaJson(HashMap<String, Object> mapa) {
		String valores = "";
		Gson gson = null;
		Type tipo = null;
		
		try {
			if (mapa == null || mapa.isEmpty())
				return "";
			gson = new Gson();
			tipo = new TypeToken<HashMap<String, Object>>() {}.getType();
			valores = gson.toJson(mapa, tipo);
		} catch (Exception e) {
			log.error("Ocurrio un problema al convertir el mapa en json", e);
			valores = "";
		}
		
		return valores;
	}
	
	private boolean comprobarEmpresa(String rfcReceptor, String rfcEmisor) {
		rfcReceptor = (rfcReceptor != null ? rfcReceptor.trim() : "");
		rfcEmisor = (rfcEmisor != null ? rfcEmisor.trim() : "");
		if ("".equals(rfcReceptor.trim()) || "".equals(rfcEmisor.trim()))
			return false;
		if (this.infoSesion == null)
			return true;
		return (this.infoSesion.getEmpresa().getRfc().equals(rfcReceptor.trim()) || this.infoSesion.getEmpresa().getRfc().equals(rfcEmisor.trim()));
	}

	private String readFile(byte[] archivoSrc) throws Exception {
		return readFile(archivoSrc, "UTF-8");
	}
	
	private String readFile(byte[] archivoSrc, String charsetName) throws Exception {
		InputStream stream = null;
		BufferedReader br = null;
		StringBuilder sb = null;
		String line = "";
		String fileString = "";
		
		try {
			charsetName = (charsetName != null && ! "".equals(charsetName.trim()) ? charsetName.trim().toUpperCase() : "UTF-8");
			stream = new ByteArrayInputStream(archivoSrc);
			br = new BufferedReader(new InputStreamReader(stream, charsetName));
			line = br.readLine();

			sb = new StringBuilder();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			
			fileString = sb.toString();
			if (fileString.charAt(0) != '<')
				fileString = fileString.substring(1, fileString.length() - 1);
			
			return fileString;
		} catch (Exception e){
			log.error("Error en FacturasFac.readFile", e);
			throw e;
		}
	}
}
