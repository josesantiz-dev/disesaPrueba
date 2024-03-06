package net.giro.cargas.documentos.logica;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;

import net.giro.cargas.documentos.beans.ComprobacionFactura;
import net.giro.cargas.documentos.dao.ComprobacionFacturaDAO;
import net.giro.cargas.documentos.respuesta.Errores;
import net.giro.plataforma.InfoSesion;
import net.giro.respuesta.Respuesta;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import mx.gob.sat.cfdi.consulta.Acuse;
import mx.gob.sat.cfdi.consulta.tempuri.Consulta;
import mx.gob.sat.cfdi.consulta.tempuri.ConsultaCFDIService;
import mx.gob.sat.cfdi.consulta.tempuri.ConsultaResponse;
import mx.gob.sat.cfdi.consulta.tempuri.IConsultaCFDIService;
import mx.gob.sat.cfdi.v33.Comprobante;
import mx.gob.sat.cfdi.v33.complementos.TimbreFiscalDigital11.TimbreFiscalDigital;
import mx.gob.sat.cfdi.v33.complementos.nomina12.Nomina;
import mx.gob.sat.cfdi.v33.complementos.pagos10.Pagos.Pago;

@Stateless
public class ComprobacionFacturaFac implements ComprobacionFacturaRem {
	private static Logger log = Logger.getLogger(ComprobacionFacturaFac.class);
	private InfoSesion infoSesion;
	private ComprobacionFacturaDAO ifzBase;
	// ----------------------------------------------------
	private Consulta consulta;
	private ConsultaCFDIService consultaService;
	private IConsultaCFDIService iConsultaService;
	
	public ComprobacionFacturaFac() {
		Hashtable<String, Object> environment = null;
		InitialContext ctx = null;
		
		try {
			environment = new Hashtable<String, Object>();
			environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			ctx = new InitialContext(environment);
			this.ifzBase = (ComprobacionFacturaDAO) ctx.lookup("ejb:/Model_Cargas_Documentos//ComprobacionFacturaImp!net.giro.cargas.documentos.dao.ComprobacionFacturaDAO");
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar " + this.getClass().getCanonicalName(), e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(ComprobacionFactura entity) throws Exception {
		try {
			return this.ifzBase.save(entity, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Cargas_Documentos.FacturasFac.save(ComprobacionFactura)", e);
			throw e;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<ComprobacionFactura> saveOrUpdateList(List<ComprobacionFactura> entities) throws Exception {
		try {
			return this.ifzBase.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception e) {
			log.error("error en Logica_Cargas_Documentos.FacturasFac.saveOrUpdateList(List<ComprobacionFactura> entities)", e);
			throw e;
		}
	}

	@Override
	public void update(ComprobacionFactura entity) throws Exception {
		try {
			this.ifzBase.update(entity);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void cancelar(long idComprobanteFactura) throws Exception {
		try {
			this.cancelar(this.ifzBase.findById(idComprobanteFactura));
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
			this.ifzBase.update(entity);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public ComprobacionFactura findById(long entityId) throws Exception {
		try {
			return this.ifzBase.findById(entityId);
		} catch (Exception e) {
			log.error("error en Logica_Cargas_Documentos.FacturasFac.findById(long entityId)", e);
			throw e;
		}
	}

	@Override
	public List<ComprobacionFactura> findByDates(Date fechaDesde, Date fechaHasta) throws Exception {
		return this.findByDates(fechaDesde, fechaHasta, "model.facturaFecha");
	}

	@Override
	public List<ComprobacionFactura> findByDates(Date fechaDesde, Date fechaHasta, String orderBy) throws Exception {
		try {
			return this.ifzBase.findByDates(fechaDesde, fechaHasta, orderBy, getIdEmpresa());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ComprobacionFactura> comprobar(String expresionImpresa, String orderBy) throws Exception {
		try {
			return this.ifzBase.comprobar(expresionImpresa, orderBy, getIdEmpresa());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void comprobarEstatusSAT(long idComprobanteFactura) throws Exception {
		try {
			this.comprobarEstatusSAT(this.findById(idComprobanteFactura));
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void comprobarEstatusSAT(ComprobacionFactura entity) throws Exception {
		Acuse acuse = null;
		
		try {
			if (entity == null || entity.getExpresionImpresa() == null || "".equals(entity.getExpresionImpresa().trim()))
				return;

			acuse = this.consultaCFDI(entity.getExpresionImpresa().trim(), true);
			if (acuse != null && ! entity.getEstado().equals(acuse.getEstado().getValue())) {
				entity.setEstado(acuse.getEstado().getValue());
				entity.setCodigoEstatus(acuse.getCodigoEstatus().getValue());
				this.update(entity);
			} 
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Respuesta comprobarEstatusSAT(String expresionImpresa) throws Exception {
		Respuesta respuesta = new Respuesta();
		Acuse acuse = null;
		
		try {
			if (expresionImpresa == null || "".equals(expresionImpresa.trim()))
				expresionImpresa = "";
			acuse = this.consultaCFDI(expresionImpresa, true);
			if (acuse == null) 
				return respuesta;
			respuesta = new Respuesta();
			respuesta.getBody().addValor("codigo", acuse.getCodigoEstatus().getValue());
			respuesta.getBody().addValor("descripcion", acuse.getEstado().getValue());
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar comprobar el estatus de la Factura indicada: " + expresionImpresa, e);
		}
		
		return respuesta;
	}
	
	@Override
	public Respuesta analizarXML(byte[] archivoSrc, boolean validarEstatus) throws Exception {
		SimpleDateFormat dtFormat = null;
		Respuesta respuesta = null;
		List<ComprobacionFactura> listFacs = null;
		ComprobacionFactura pojoComprobacionFactura = null;
		Comprobante cfdi = null;
		TimbreFiscalDigital tfd = null;
		String expresionImpresa = "";
		Acuse pojoAcuse = null;
		// --------------------------------------
		/*Pattern pattern = null;
		Matcher matcher = null;
		Serializer serializer = null;
		byte[] xmlb = null;
		String xml = "";*/
		String folioFactura = "";
		long idComprobacion = 0;
		double descuentoFactura = 0;
		double subtotalFactura = 0;
		double impuestosFactura = 0;
		double retencionesFactura = 0;
		double totalFactura = 0;
		// --------------------------------------
		double tipoCambio = 0;
		double descuento = 0;
		double subtotal = 0;
		double impuestos = 0;
		double retenciones = 0;
		double total = 0;
		// --------------------------------------
		List<String> steps = null;
		
		try {
			// Inicializando
			respuesta = new Respuesta();
			dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			steps = new ArrayList<String>();
			if (this.infoSesion == null) {
				log.info("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_INESPERADO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_INESPERADO);
				respuesta.getErrores().setDescError("No se puede comprobar la sesion de Usuario");
				return respuesta;
			}
			
			// Serializando
			steps.add("serializando ... ");
			cfdi = toComprobante(archivoSrc);
			if (cfdi == null) {
				log.info("Ocurrio un problema al interpretar el XML ingresado");
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_INESPERADO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_INESPERADO);
				respuesta.getErrores().setDescError("Ocurrio un problema al interpretar el XML ingresado");
				return respuesta;
			}
			
			// Recuperando Complementos: Timbre
			steps.add("Recuperando Complmentos ... ");
			tfd = getNodoTimbreFiscalDigital(cfdi.getComplemento());
			if (tfd == null) {
				log.info("Ocurrio un problema al recuperar el Timbre del XML ingresado");
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_INESPERADO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_INESPERADO);
				respuesta.getErrores().setDescError("Ocurrio un problema al recuperar el Timbre del XML ingresado");
				return respuesta;
			}
			
			// Comprobacion Tipo de Comprobante
			steps.add("Comprobacion Tipo de Comprobante ... ");
			cfdi.setSerie(setDefaultValue(cfdi.getSerie(), ""));
			cfdi.setFolio(setDefaultValue(cfdi.getFolio(), tfd.getUUID().substring(tfd.getUUID().length() - 8)));
			if (! "I E".contains(cfdi.getTipoDeComprobante().value())) {
				steps.add("TipoDeComprobante-invalido-" + cfdi.getReceptor().getRfc());
				log.info(Errores.ERROR_TIPOCOMPROBANTE_INVALIDO + " - " + Errores.descError.get(Errores.ERROR_TIPOCOMPROBANTE_INVALIDO) + ": " + cfdi.getTipoDeComprobante().value());
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_TIPOCOMPROBANTE_INVALIDO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_TIPOCOMPROBANTE_INVALIDO);
				respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_TIPOCOMPROBANTE_INVALIDO));
				return respuesta;
			}

			// Comprobamos que el XML va dirijido a la empresa actualmente cargada
			steps.add("comprobando-empresa");
			if (! comprobarEmpresa(cfdi.getReceptor().getRfc(), cfdi.getEmisor().getRfc())) {
				steps.add("empresa-invalida-" + cfdi.getReceptor().getRfc());
				log.info(Errores.ERROR_EMPRESA_INVALIDA + " - " + Errores.descError.get(Errores.ERROR_EMPRESA_INVALIDA) + ": " + cfdi.getReceptor().getRfc());
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setCodigoError(Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_EMPRESA_INVALIDA));
				return respuesta;
			}
			
			// Compruebo el estatus del CFDI en el SAT
			steps.add("expresion-impresa");
			expresionImpresa = "?re=" + cfdi.getEmisor().getRfc() + "&rr=" + cfdi.getReceptor().getRfc() + "&tt=" + cfdi.getTotal() + "&id=" + tfd.getUUID();
			
			/*// Cargamos y validamos el xml
			steps.add("leyendo archivo");
			xml = readFile(archivoSrc);
			// Validacion de caracteres al XML
			steps.add("Validacion de caracteres al XML");
			if (! xml.startsWith("<?xml") && ! xml.startsWith("<cfdi:Comprobante")) {
				if (xml.contains("<?xml"))
					xml = xml.substring(xml.indexOf("<?xml")); // xml = StringEscapeUtils.unescapeHtml(xml);
				else if (xml.contains("<cfdi:Comprobante"))
					xml = xml.substring(xml.indexOf("<cfdi:Comprobante"));
			}
			if (xml.contains("C"))
				xml = xml.replaceAll("C", "Ñ");
			pattern = Pattern.compile("[\\000]*");
			matcher = pattern.matcher(xml);
			if (matcher.find())
				xml = matcher.replaceAll("");
			steps.add("minificando");
			xmlb = aMinusculas(xml);
			// Serializando
			steps.add("serializando");
			serializer = new Persister();
			cfdi = serializer.read(Comprobante.class, new ByteArrayInputStream(xmlb), false);
			if (cfdi.getSerie() == null || "".equals(cfdi.getSerie().trim()))
				cfdi.setSerie("F");
			if (cfdi.getFolio() == null || "".equals(cfdi.getFolio().trim()))
				cfdi.setFolio(cfdi.getComplemento().getTimbreFiscalDigital().getUUID().substring(cfdi.getComplemento().getTimbreFiscalDigital().getUUID().length() - 8));

			// Comprobamos que el XML va dirijido a la empresa actualmente cargada
			steps.add("comprobando-empresa");
			if (! comprobarEmpresa(cfdi.getReceptor().getRfc(), cfdi.getEmisor().getRfc())) {
				steps.add("empresa-invalida-" + cfdi.getReceptor().getRfc());
				log.info(Errores.ERROR_EMPRESA_INVALIDA + " - " + Errores.descError.get(Errores.ERROR_EMPRESA_INVALIDA) + ": " + cfdi.getReceptor().getRfc());
				respuesta.getErrores().addCodigo(modulo, Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setCodigoError(Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_EMPRESA_INVALIDA));
				return respuesta;
			}
			
			// Compruebo el estatus del CFDI en el SAT
			steps.add("expresion-impresa");
			expresionImpresa = "?re=" + cfdi.getEmisor().getRfc() + "&rr=" + cfdi.getReceptor().getRfc() + "&tt=" + cfdi.getTotal() + "&id=" + cfdi.getComplemento().getTimbreFiscalDigital().getUUID();
			*///refSello = "&fe=" + cfdi.getSello().substring(cfdi.getSello().length() - 8);
			steps.add("recuperando-acuse");
			pojoAcuse = consultaCFDI(expresionImpresa, validarEstatus);
			if (pojoAcuse != null) {
				if ("cancelado".equals(pojoAcuse.getEstado().getValue().toLowerCase())) {
					steps.add("cfdi-cancelado-" + tfd.getUUID());
					log.info(Errores.ERROR_CFDI_CANCELADO + " - " + Errores.descError.get(Errores.ERROR_CFDI_CANCELADO) + ": " + tfd.getUUID());
					respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_CFDI_CANCELADO);
					respuesta.getErrores().setCodigoError(Errores.ERROR_CFDI_CANCELADO);
					respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_CFDI_CANCELADO));
					return respuesta;
				}
			}
			
			// Comprobamos que el documento especificado no se halla subido previamente
			idComprobacion = 0;
			steps.add("comprobando-previo");
			listFacs = this.comprobar(expresionImpresa, "id desc"); 
			if (listFacs != null && ! listFacs.isEmpty()) {
				listFacs = actualizarDatosFacturasPrevias(cfdi, listFacs);
				pojoComprobacionFactura = listFacs.get(0);
				idComprobacion = pojoComprobacionFactura.getId();
				tipoCambio = pojoComprobacionFactura.getFacturaTipoCambio();
				descuento = pojoComprobacionFactura.getDescuentoPesos();
				subtotal = pojoComprobacionFactura.getSubtotalPesos();
				impuestos = pojoComprobacionFactura.getImpuestosPesos();
				retenciones = pojoComprobacionFactura.getRetencionesPesos();
				total = pojoComprobacionFactura.getTotalPesos();
				steps.add("xml-previo-" + idComprobacion);
				log.info(Errores.ERROR_XML_PREVIO + " - " + Errores.descError.get(Errores.ERROR_XML_PREVIO) + ": " + idComprobacion);
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_XML_PREVIO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_XML_PREVIO);
				respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_XML_PREVIO));
				return respuesta;
			}

			if (cfdi.getDescuento() != null && cfdi.getDescuento().doubleValue() > 0)
				descuentoFactura = cfdi.getDescuento().doubleValue();
			if (cfdi.getImpuestos() != null && cfdi.getImpuestos().getTotalImpuestosTrasladados() != null && cfdi.getImpuestos().getTotalImpuestosTrasladados().doubleValue() > 0)
				impuestosFactura = cfdi.getImpuestos().getTotalImpuestosTrasladados().doubleValue();
			if (cfdi.getImpuestos() != null && cfdi.getImpuestos().getTotalImpuestosRetenidos() != null && cfdi.getImpuestos().getTotalImpuestosRetenidos().doubleValue() > 0)
				retencionesFactura = cfdi.getImpuestos().getTotalImpuestosRetenidos().doubleValue();
			subtotalFactura = cfdi.getSubTotal().doubleValue();
			totalFactura = cfdi.getTotal().doubleValue();
			tipoCambio = ((cfdi.getTipoCambio() != null && cfdi.getTipoCambio().doubleValue() > 0) ? cfdi.getTipoCambio().doubleValue() : 1);
			
			// Aplicamos conversion si corresponde
			descuento = descuentoFactura * tipoCambio;
			subtotal = subtotalFactura * tipoCambio;
			impuestos = impuestosFactura * tipoCambio;
			retenciones = retencionesFactura * tipoCambio;
			total = totalFactura * tipoCambio;
			
			// Genero Comprobacion de Factura
			steps.add("genero-ComprobacionFactura");
			pojoComprobacionFactura = new ComprobacionFactura();
			pojoComprobacionFactura.setId(idComprobacion);
			pojoComprobacionFactura.setExpresionImpresa(expresionImpresa);
			pojoComprobacionFactura.setFacturaSerie("");
			if (cfdi.getSerie() != null)
				pojoComprobacionFactura.setFacturaSerie(cfdi.getSerie());
			pojoComprobacionFactura.setFacturaFolio("");
			if (cfdi.getFolio() != null)
				pojoComprobacionFactura.setFacturaFolio(cfdi.getFolio());
			pojoComprobacionFactura.setFacturaEmisor("");
			if (cfdi.getEmisor().getNombre() != null)
				pojoComprobacionFactura.setFacturaEmisor(cfdi.getEmisor().getNombre());
			pojoComprobacionFactura.setFacturaTipo(cfdi.getTipoDeComprobante().value());
			pojoComprobacionFactura.setFacturaMetodo(cfdi.getMetodoPago().value());
			pojoComprobacionFactura.setFacturaFormaPago(cfdi.getFormaPago());
			pojoComprobacionFactura.setFacturaFecha(cfdi.getFecha());
			pojoComprobacionFactura.setFacturaFolioFiscal(tfd.getUUID());
			pojoComprobacionFactura.setFacturaDescuento(descuentoFactura);
			pojoComprobacionFactura.setFacturaSubtotal(subtotalFactura);
			pojoComprobacionFactura.setFacturaTotal(totalFactura);
			pojoComprobacionFactura.setFacturaMoneda(cfdi.getMoneda().value());
			pojoComprobacionFactura.setFacturaTipoCambio(tipoCambio);
			pojoComprobacionFactura.setDescuentoPesos(descuento);
			pojoComprobacionFactura.setSubtotalPesos(subtotal);
			pojoComprobacionFactura.setImpuestosPesos(impuestos);
			pojoComprobacionFactura.setRetencionesPesos(retenciones);
			pojoComprobacionFactura.setTotalPesos(total);
			if (pojoAcuse != null) { 
				steps.add("acuse-recuperado");
				pojoComprobacionFactura.setEstado(pojoAcuse.getEstado().getValue());
				pojoComprobacionFactura.setCodigoEstatus(pojoAcuse.getCodigoEstatus().getValue());
			} else { 
				steps.add("acuse-nulo");
				pojoComprobacionFactura.setEstado("Error al obtener estatus");
				pojoComprobacionFactura.setCodigoEstatus("Error al consultar comprobante");
			} 
			pojoComprobacionFactura.setIdEmpresa(getIdEmpresa());
			pojoComprobacionFactura.setCreadoPor(this.infoSesion.getAcceso().getId());
			pojoComprobacionFactura.setFechaCreacion(Calendar.getInstance().getTime());
			pojoComprobacionFactura.setModificadoPor(this.infoSesion.getAcceso().getId());
			pojoComprobacionFactura.setFechaModificacion(Calendar.getInstance().getTime());
			
			// Guardamos
			steps.add("guardamos-ComprobacionFactura");
			pojoComprobacionFactura.setId(this.ifzBase.save(pojoComprobacionFactura, getCodigoEmpresa()));
			idComprobacion = pojoComprobacionFactura.getId();
			steps.add("CoprobacionFactura-" + pojoComprobacionFactura.getId());
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
			steps.add("terminado");
		} catch (TransformerException e) {
			log.error("Ocurrio un problema al Transformar el XML ", e);
			respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(3L);
			respuesta.getErrores().setDescError("El XML indicado contiene caracteres no validos.");
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el CFDI indicado ::\n" + StringUtils.join(steps, "\n"), e);
			respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_ANALIZAR_XML));
		} finally {
			respuesta.getBody().addValor("stepTrace", StringUtils.join(steps, "|"));
			respuesta.getBody().addValor("idComprobante", idComprobacion); 
			
			if (pojoAcuse != null)
				respuesta.getBody().addValor("pojoAcuse", pojoAcuse);
			
			if (cfdi != null) {
				folioFactura = validateString(cfdi.getSerie()) + (! "".equals(validateString(cfdi.getSerie())) ? "-" : "") + validateString(cfdi.getFolio());
				respuesta.getBody().addValor("pojoComprobante", cfdi);
				respuesta.getBody().addValor("comprobanteFecha", dtFormat.format(cfdi.getFecha()));
				respuesta.getBody().addValor("comprobanteSerie", validateString(cfdi.getSerie())); 
				respuesta.getBody().addValor("comprobanteFolio", validateString(cfdi.getFolio())); 
				respuesta.getBody().addValor("comprobanteFolioFactura", folioFactura); 
				respuesta.getBody().addValor("comprobanteTipo", cfdi.getTipoDeComprobante().value());
				respuesta.getBody().addValor("comprobanteMetodo", cfdi.getMetodoPago().value());
				respuesta.getBody().addValor("comprobanteFormaPago", validateString(cfdi.getFormaPago()));
				respuesta.getBody().addValor("comprobanteMoneda", cfdi.getMoneda().value());
				respuesta.getBody().addValor("comprobanteTipoCambio", tipoCambio);
				respuesta.getBody().addValor("comprobanteDescuento", descuento); 
				respuesta.getBody().addValor("comprobanteSubtotal", subtotal); 
				respuesta.getBody().addValor("comprobanteEmisor", cfdi.getEmisor().getRfc());
				respuesta.getBody().addValor("comprobanteReceptor", cfdi.getReceptor().getRfc());
				respuesta.getBody().addValor("comprobanteUuid", tfd.getUUID());
				respuesta.getBody().addValor("comprobanteTipoPersona", validaTipoPersona(cfdi.getEmisor().getRfc()));
				respuesta.getBody().addValor("comprobantePersonalidad", validaPersonalidad(cfdi.getEmisor().getRfc()));
			}
			
			if (pojoComprobacionFactura != null) {
				folioFactura = validateString(pojoComprobacionFactura.getFacturaSerie()) + (! "".equals(validateString(pojoComprobacionFactura.getFacturaSerie())) ? "-" : "") + validateString(pojoComprobacionFactura.getFacturaFolio());
				respuesta.getBody().addValor("pojoComprobacionFactura", pojoComprobacionFactura);
				respuesta.getBody().addValor("comprobanteSerie", pojoComprobacionFactura.getFacturaSerie()); 
				respuesta.getBody().addValor("comprobanteFolio", pojoComprobacionFactura.getFacturaFolio()); 
				respuesta.getBody().addValor("comprobanteFolioFactura", folioFactura); 
				respuesta.getBody().addValor("comprobanteRfc", pojoComprobacionFactura.getRfcEmisor()); 
				respuesta.getBody().addValor("comprobanteRazonSocial", pojoComprobacionFactura.getFacturaEmisor()); 
				respuesta.getBody().addValor("comprobanteRazonSocialRfc", pojoComprobacionFactura.getRazonSocialORfc()); 
				respuesta.getBody().addValor("comprobanteDescuento", pojoComprobacionFactura.getDescuentoPesos()); 
				respuesta.getBody().addValor("comprobanteSubtotal", pojoComprobacionFactura.getSubtotalPesos()); 
				respuesta.getBody().addValor("comprobanteTotal", pojoComprobacionFactura.getTotalPesos()); 
				respuesta.getBody().addValor("comprobanteSaldo", pojoComprobacionFactura.getTotalPesos());
				respuesta.getBody().addValor("comprobanteMoneda", pojoComprobacionFactura.getFacturaMoneda());
				respuesta.getBody().addValor("comprobanteTipoCambio", pojoComprobacionFactura.getFacturaTipoCambio());
				respuesta.getBody().addValor("comprobanteUuid", pojoComprobacionFactura.getFacturaFolioFiscal());
				respuesta.getBody().addValor("comprobanteTipoPersona", validaTipoPersona(pojoComprobacionFactura.getRfcEmisor()));
				respuesta.getBody().addValor("comprobantePersonalidad", validaPersonalidad(cfdi.getEmisor().getRfc()));
			}
		}
		
		return respuesta;
	}

	@Override
	public Respuesta importarCFDI(Comprobante cfdi) throws Exception {
		Respuesta respuesta = null;
		SimpleDateFormat dtFormat = null;
		TimbreFiscalDigital tfd = null;
		Acuse pojoAcuse = null;
		String expresionImpresa = "";
		// -----------------------------------------------------------------------------
		List<ComprobacionFactura> comprobacionesPrevias = null;
		ComprobacionFactura comprobacion = null;
		String folioFactura = "";
		long idComprobacion = 0;
		double tipoCambio = 0;
		// -----------------------------------------------------------------------------
		double descuento = 0;
		double subtotal = 0;
		double impuestos = 0;
		double retenciones = 0;
		double total = 0;
		
		try {
			respuesta = new Respuesta();
			dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (cfdi == null) {
				log.info("Ocurrio un problema al interpretar el XML ingresado");
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_INESPERADO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_INESPERADO);
				respuesta.getErrores().setDescError("Ocurrio un problema al interpretar el XML ingresado");
				return respuesta;
			}
			
			// Recuperando Complementos: Timbre
			tfd = getNodoTimbreFiscalDigital(cfdi.getComplemento());
			if (tfd == null) {
				log.info("Ocurrio un problema al recuperar el Timbre del XML ingresado");
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_INESPERADO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_INESPERADO);
				respuesta.getErrores().setDescError("Ocurrio un problema al recuperar el Timbre del XML ingresado");
				return respuesta;
			}
			
			// Comprobacion Tipo de Comprobante
			cfdi.setSerie(setDefaultValue(cfdi.getSerie(), ""));
			cfdi.setFolio(setDefaultValue(cfdi.getFolio(), tfd.getUUID().substring(tfd.getUUID().length() - 8)));
			// Compruebo el estatus del CFDI en el SAT
			expresionImpresa = "?re=" + cfdi.getEmisor().getRfc() + "&rr=" + cfdi.getReceptor().getRfc() + "&tt=" + cfdi.getTotal() + "&id=" + tfd.getUUID();
			if (! "I E".contains(cfdi.getTipoDeComprobante().value())) {
				log.info(Errores.ERROR_TIPOCOMPROBANTE_INVALIDO + " - " + Errores.descError.get(Errores.ERROR_TIPOCOMPROBANTE_INVALIDO) + ": " + cfdi.getTipoDeComprobante().value());
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_TIPOCOMPROBANTE_INVALIDO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_TIPOCOMPROBANTE_INVALIDO);
				respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_TIPOCOMPROBANTE_INVALIDO));
				return respuesta;
			}

			// Comprobamos que el XML va dirijido a la empresa actualmente cargada
			if (! comprobarEmpresa(cfdi.getReceptor().getRfc(), cfdi.getEmisor().getRfc())) {
				log.info(Errores.ERROR_EMPRESA_INVALIDA + " - " + Errores.descError.get(Errores.ERROR_EMPRESA_INVALIDA) + ": " + cfdi.getReceptor().getRfc());
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setCodigoError(Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_EMPRESA_INVALIDA));
				return respuesta;
			}

			pojoAcuse = consultaCFDI(expresionImpresa, false);
			if (pojoAcuse != null) {
				if ("cancelado".equals(pojoAcuse.getEstado().getValue().toLowerCase())) {
					log.info(Errores.ERROR_CFDI_CANCELADO + " - " + Errores.descError.get(Errores.ERROR_CFDI_CANCELADO) + ": " + tfd.getUUID());
					respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_CFDI_CANCELADO);
					respuesta.getErrores().setCodigoError(Errores.ERROR_CFDI_CANCELADO);
					respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_CFDI_CANCELADO));
					return respuesta;
				}
			}
			
			// Comprobamos que el documento especificado no se halla subido previamente
			idComprobacion = 0;
			comprobacionesPrevias = this.comprobar(expresionImpresa, "id desc"); 
			if (comprobacionesPrevias != null && ! comprobacionesPrevias.isEmpty()) {
				comprobacionesPrevias = actualizarDatosFacturasPrevias(cfdi, comprobacionesPrevias);
				comprobacion = comprobacionesPrevias.get(0); 
				idComprobacion = comprobacion.getId();
				tipoCambio = comprobacion.getFacturaTipoCambio();
				descuento = comprobacion.getDescuentoPesos();
				subtotal = comprobacion.getSubtotalPesos();
				impuestos = comprobacion.getImpuestosPesos();
				retenciones = comprobacion.getRetencionesPesos();
				total = comprobacion.getTotalPesos();
				log.info(Errores.ERROR_XML_PREVIO + " - " + Errores.descError.get(Errores.ERROR_XML_PREVIO) + ": " + idComprobacion);
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_XML_PREVIO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_XML_PREVIO);
				respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_XML_PREVIO));
				return respuesta;
			}
			
			comprobacion = getComprobacionFactura(cfdi, tfd.getUUID(), expresionImpresa);
			if (comprobacion == null) {
				log.info(Errores.ERROR_EMPRESA_INVALIDA + " - Ocurrio un problema al generar la Comprobacion del CFDI");
				respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setCodigoError(Errores.ERROR_EMPRESA_INVALIDA);
				respuesta.getErrores().setDescError("Ocurrio un problema al generar la Comprobacion del CFDI");
				return respuesta;
			}
			
			comprobacion.setEstado("Error al obtener estatus");
			comprobacion.setCodigoEstatus("Error al consultar comprobante");
			if (pojoAcuse != null) { 
				comprobacion.setEstado(pojoAcuse.getEstado().getValue());
				comprobacion.setCodigoEstatus(pojoAcuse.getCodigoEstatus().getValue());
			}
			
			// Guardamos
			comprobacion.setId(this.ifzBase.save(comprobacion, getCodigoEmpresa()));
			idComprobacion = comprobacion.getId();
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
		} catch (Exception e) {
			log.error("Ocurrio un problema al procesar el CFDI indicado", e);
			respuesta.getErrores().addCodigo("CARGAS_DOCUMENTOS", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_ANALIZAR_XML));

		} finally {
			respuesta.getBody().addValor("idComprobante", idComprobacion); 
			respuesta.getBody().addValor("pojoComprobante", cfdi);
			respuesta.getBody().addValor("expresionImpresa", expresionImpresa);
			respuesta.getBody().addValor("pojoComprobacionFactura", comprobacion);
			respuesta.getBody().addValor("pojoAcuse", pojoAcuse);
			
			if (cfdi != null) {
				folioFactura = validateString(cfdi.getSerie()) + (! "".equals(validateString(cfdi.getSerie())) ? "-" : "") + validateString(cfdi.getFolio());
				respuesta.getBody().addValor("comprobanteFecha", dtFormat.format(cfdi.getFecha()));
				respuesta.getBody().addValor("comprobanteSerie", validateString(cfdi.getSerie())); 
				respuesta.getBody().addValor("comprobanteFolio", validateString(cfdi.getFolio())); 
				respuesta.getBody().addValor("comprobanteFolioFactura", folioFactura); 
				respuesta.getBody().addValor("comprobanteTipo", cfdi.getTipoDeComprobante().value());
				respuesta.getBody().addValor("comprobanteMetodo", cfdi.getMetodoPago().value());
				respuesta.getBody().addValor("comprobanteFormaPago", validateString(cfdi.getFormaPago()));
				respuesta.getBody().addValor("comprobanteMoneda", cfdi.getMoneda().value());
				respuesta.getBody().addValor("comprobanteTipoCambio", tipoCambio);
				respuesta.getBody().addValor("comprobanteDescuento", descuento); 
				respuesta.getBody().addValor("comprobanteSubtotal", subtotal); 
				respuesta.getBody().addValor("comprobanteImpuestos", impuestos); 
				respuesta.getBody().addValor("comprobanteRetenciones", retenciones); 
				respuesta.getBody().addValor("comprobanteTotal", total); 
				respuesta.getBody().addValor("comprobanteEmisor", cfdi.getEmisor().getRfc());
				respuesta.getBody().addValor("comprobanteReceptor", cfdi.getReceptor().getRfc());
				respuesta.getBody().addValor("comprobanteUuid", tfd.getUUID());
				respuesta.getBody().addValor("comprobanteTipoPersona", validaTipoPersona(cfdi.getEmisor().getRfc()));
				respuesta.getBody().addValor("comprobantePersonalidad", validaPersonalidad(cfdi.getEmisor().getRfc()));
			}
			
			if (comprobacion != null) {
				folioFactura = validateString(comprobacion.getFacturaSerie()) + (! "".equals(validateString(comprobacion.getFacturaSerie())) ? "-" : "") + validateString(comprobacion.getFacturaFolio());
				respuesta.getBody().addValor("comprobanteSerie", comprobacion.getFacturaSerie()); 
				respuesta.getBody().addValor("comprobanteFolio", comprobacion.getFacturaFolio()); 
				respuesta.getBody().addValor("comprobanteFolioFactura", folioFactura); 
				respuesta.getBody().addValor("comprobanteRfc", comprobacion.getRfcEmisor()); 
				respuesta.getBody().addValor("comprobanteRazonSocial", comprobacion.getFacturaEmisor()); 
				respuesta.getBody().addValor("comprobanteRazonSocialRfc", comprobacion.getRazonSocialORfc()); 
				respuesta.getBody().addValor("comprobanteDescuento", comprobacion.getDescuentoPesos()); 
				respuesta.getBody().addValor("comprobanteSubtotal", comprobacion.getSubtotalPesos()); 
				respuesta.getBody().addValor("comprobanteImpuestos", comprobacion.getImpuestosPesos()); 
				respuesta.getBody().addValor("comprobanteRetenciones", comprobacion.getRetencionesPesos()); 
				respuesta.getBody().addValor("comprobanteTotal", comprobacion.getTotalPesos()); 
				respuesta.getBody().addValor("comprobanteSaldo", comprobacion.getTotalPesos());
				respuesta.getBody().addValor("comprobanteMoneda", comprobacion.getFacturaMoneda());
				respuesta.getBody().addValor("comprobanteTipoCambio", comprobacion.getFacturaTipoCambio());
				respuesta.getBody().addValor("comprobanteUuid", comprobacion.getFacturaFolioFiscal());
				respuesta.getBody().addValor("comprobanteTipoPersona", validaTipoPersona(comprobacion.getRfcEmisor()));
				respuesta.getBody().addValor("comprobantePersonalidad", validaPersonalidad(cfdi.getEmisor().getRfc()));
			}
		}
		
		return respuesta;
	}
	
	@Override
	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	public void deleteXML(long idComprobane) {
		try {
			this.ifzBase.delete(idComprobane);
		} catch (Exception e) {
			log.error("Error en FacturasFac.deleteXML", e);
		}
	}

	@Override
	public String getProperty(long idFactura, String property) {
		ComprobacionFactura pojo = null;
		String resultado = "";

		pojo = this.ifzBase.findById(idFactura);
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
			if ("moneda".equals(property))
				resultado = pojo.getFacturaMoneda();
			if ("tipo".equals(property))
				resultado = pojo.getFacturaTipo();
			if ("expresionImpresa".equals(property))
				resultado = pojo.getExpresionImpresa();
		}
		
		return resultado;
	}

	@Override
	public void setProperty(long idFactura, String property, String value) throws Exception {
		ComprobacionFactura pojo = null;
			
		try {
			// Recupero y valido Factura
			pojo = this.ifzBase.findById(idFactura);
			if (pojo == null || pojo.getId() <= 0L)
				return;

			// Asignamos propiedad
			if ("nombre".equals(property))
				pojo.setFacturaEmisor(value);
			if ("serie".equals(property))
				pojo.setFacturaSerie(value);
			if ("folio".equals(property))
				pojo.setFacturaFolio(value);
			
			// Guardamos Factura
			this.ifzBase.update(pojo);
		} catch (Exception e) {
			log.error("Error en FacturasFac.setProperty", e);
			throw e;
		}
	}

	@Override
	public double getFacturaTipoCambio(long idFactura) throws Exception {
		ComprobacionFactura pojo = null;
		
		try {
			// Recupero y valido Factura
			pojo = this.ifzBase.findById(idFactura);
			if (pojo == null || pojo.getId() <= 0L)
				return 0;

			return pojo.getFacturaTipoCambio();
		} catch (Exception e) {
			log.error("Error en FacturasFac.getFacturaTipoCambio(idFactura)", e);
			throw e;
		}
	}

	@Override
	public double getFacturaDescuento(long idFactura) throws Exception {
		ComprobacionFactura pojo = null;
		
		try {
			// Recupero y valido Factura
			pojo = this.ifzBase.findById(idFactura);
			if (pojo == null || pojo.getId() <= 0L)
				return 0;
			return pojo.getFacturaDescuento();
		} catch (Exception e) {
			log.error("Error en FacturasFac.getFacturaDescuento(idFactura)", e);
			throw e;
		}
	}

	@Override
	public double getFacturaSubtotal(long idFactura) throws Exception {
		ComprobacionFactura pojo = null;
		
		try {
			// Recupero y valido Factura
			pojo = this.ifzBase.findById(idFactura);
			if (pojo == null || pojo.getId() <= 0L)
				return 0;
			return pojo.getFacturaSubtotal();
		} catch (Exception e) {
			log.error("Error en FacturasFac.getFacturaSubtotal(idFactura)", e);
			throw e;
		}
	}

	@Override
	public double getFacturaTotal(long idFactura) throws Exception {
		ComprobacionFactura pojo = null;
		
		try {
			// Recupero y valido Factura
			pojo = this.ifzBase.findById(idFactura);
			if (pojo == null || pojo.getId() <= 0L)
				return 0;
			return pojo.getTotalPesos();
		} catch (Exception e) {
			log.error("Error en FacturasFac.getFacturaTotal(idFactura)", e);
			throw e;
		}
	}

	// --------------------------------------------------------------------------------------------------
	// PRIVADOS
	// --------------------------------------------------------------------------------------------------
	
	private Long getIdEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getId() : 1L);
	}
	
	private Long getCodigoEmpresa() {
		return (this.infoSesion != null ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}

	private Comprobante toComprobante(byte[] fileSource) {
		JAXBContext context = null;
		Unmarshaller unmarshaller = null;
		// ---------------------------------------------
		byte[] xmlSource = null;
		//String xml = "";
		
		try {
			if (fileSource == null) 
				return null;
			xmlSource = fileSource;
			//xml = readFile(fileSource);
			//xmlSource = aMinusculas(xml);
			context = JAXBContext.newInstance(new Class[] {Comprobante.class, TimbreFiscalDigital.class, Nomina.class, Pago.class});
			unmarshaller = context.createUnmarshaller();
			return (Comprobante) unmarshaller.unmarshal(new ByteArrayInputStream(xmlSource));
		} catch (Exception e) {
			log.error("Ocurrio un problema al deserializar el XML a clases", e);
		}
		
		return null;
	}

	private TimbreFiscalDigital getNodoTimbreFiscalDigital(List<Comprobante.Complemento> complementos) {
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

	private ComprobacionFactura getComprobacionFactura(Comprobante cfdi, String uuid, String expresionImpresa) {
		ComprobacionFactura comprobacion = null;
		long idComprobacion = 0L;
		double descuentoFactura = 0;
		double subtotalFactura = 0;
		double impuestosFactura = 0;
		double retencionesFactura = 0;
		double totalFactura = 0;
		double tipoCambio = 0;
		double descuento = 0;
		double subtotal = 0;
		double impuestos = 0;
		double retenciones = 0;
		double total = 0;
		
		try {
			if (cfdi == null)
				return null;
			if (cfdi.getDescuento() != null && ! "".equals(cfdi.getDescuento()))
				descuentoFactura = cfdi.getDescuento().doubleValue();
			if (cfdi.getImpuestos() != null && cfdi.getImpuestos().getTotalImpuestosTrasladados() != null && ! "".equals(cfdi.getImpuestos().getTotalImpuestosTrasladados()))
				impuestosFactura = cfdi.getImpuestos().getTotalImpuestosTrasladados().doubleValue();
			if (cfdi.getImpuestos() != null && cfdi.getImpuestos().getTotalImpuestosRetenidos() != null && ! "".equals(cfdi.getImpuestos().getTotalImpuestosRetenidos()))
				retencionesFactura = cfdi.getImpuestos().getTotalImpuestosRetenidos().doubleValue();
			subtotalFactura = cfdi.getSubTotal().doubleValue();
			totalFactura = cfdi.getTotal().doubleValue();
			tipoCambio = ((cfdi.getTipoCambio() != null && cfdi.getTipoCambio().doubleValue() > 0) ? cfdi.getTipoCambio().doubleValue() : 1);
			
			// Aplicamos conversion si corresponde
			descuento = descuentoFactura * tipoCambio;
			subtotal = subtotalFactura * tipoCambio;
			impuestos = impuestosFactura * tipoCambio;
			retenciones = retencionesFactura * tipoCambio;
			total = totalFactura * tipoCambio;
			
			comprobacion = new ComprobacionFactura();
			comprobacion.setId(idComprobacion);
			comprobacion.setExpresionImpresa(expresionImpresa);
			comprobacion.setFacturaSerie(cfdi.getSerie());
			comprobacion.setFacturaFolio(cfdi.getFolio());
			comprobacion.setFacturaEmisor(cfdi.getEmisor().getNombre());
			comprobacion.setFacturaTipo(cfdi.getTipoDeComprobante().value());
			comprobacion.setFacturaMetodo(cfdi.getMetodoPago().value());
			comprobacion.setFacturaFormaPago(cfdi.getFormaPago());
			comprobacion.setFacturaFecha(cfdi.getFecha());
			comprobacion.setFacturaFolioFiscal(uuid);
			comprobacion.setFacturaDescuento(descuentoFactura);
			comprobacion.setFacturaSubtotal(subtotalFactura);
			comprobacion.setFacturaTotal(totalFactura);
			comprobacion.setFacturaMoneda(cfdi.getMoneda().value());
			comprobacion.setFacturaTipoCambio(tipoCambio);
			comprobacion.setDescuentoPesos(descuento);
			comprobacion.setSubtotalPesos(subtotal);
			comprobacion.setImpuestosPesos(impuestos);
			comprobacion.setRetencionesPesos(retenciones);
			comprobacion.setTotalPesos(total);
			comprobacion.setIdEmpresa(getIdEmpresa());
			comprobacion.setCreadoPor(this.infoSesion.getAcceso().getId());
			comprobacion.setFechaCreacion(Calendar.getInstance().getTime());
			comprobacion.setModificadoPor(this.infoSesion.getAcceso().getId());
			comprobacion.setFechaModificacion(Calendar.getInstance().getTime());
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar generar la Comprobacion de CFDI", e);
		}
		
		return comprobacion;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Acuse consultaCFDI(String expresionImpresa, boolean validarEstatus) {
		String stepTrace = "";
		Acuse acuse = null;
		
		if (! validarEstatus) {
			try {
				stepTrace = "|genero-Acuse|Validacion desactivada";
				acuse = new Acuse();
				stepTrace += "|setCodigoEstatus(Error al consultar comprobante)";
				acuse.setCodigoEstatus(new JAXBElement(new QName("http://schemas.datacontract.org/2004/07/Sat.Cfdi.Negocio.ConsultaCfdi.Servicio", "CodigoEstatus"), String.class, "Validacion de Estatus desactivada"));
				stepTrace += "|setCodigoEstatus(Error al obtener estatus)";
				acuse.setEstado(new JAXBElement(new QName("http://schemas.datacontract.org/2004/07/Sat.Cfdi.Negocio.ConsultaCfdi.Servicio", "Estado"), String.class, "No se consulto el estatus del CFDI (ConsultaCFDI): Validacion desactivada"));
				return acuse;
			} catch (Exception e) {
				log.error("Ocurrio un problema al generar acuse: 'Error de consulta CFDI (Validacion desactivada)'. Devolvere acuse nulo.\nSTEPS:\n" + stepTrace + "\n\n", e);
				return null;
			}
		}
		
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
			stepTrace += "|directRequestSoap";
			acuse = directRequestSoap(expresionImpresa);
			if (acuse != null)
				return acuse;
			stepTrace += "\n" + expresionImpresa;
			log.error("Ocurrio un problema al intentar consumir el servicio de consultas de estatus de CFDI (SAT).\nSTEPS:\n" + stepTrace + "\n\n", e);
		}
		
		try {
			stepTrace = "|genero-Acuse";
			acuse = new Acuse();
			stepTrace += "|setCodigoEstatus(Error al consultar comprobante)";
			acuse.setCodigoEstatus(new JAXBElement(new QName("http://schemas.datacontract.org/2004/07/Sat.Cfdi.Negocio.ConsultaCfdi.Servicio", "CodigoEstatus"), String.class, "Error al consultar comprobante"));
			stepTrace += "|setCodigoEstatus(Error al obtener estatus)";
			acuse.setEstado(new JAXBElement(new QName("http://schemas.datacontract.org/2004/07/Sat.Cfdi.Negocio.ConsultaCfdi.Servicio", "Estado"), String.class, "No se pudo consultar de estatus del CFDI (ConsultaCFDI)"));
			return acuse;
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar acuse: 'Error de consulta CFDI'. Devolvere acuse nulo.\nSTEPS:\n" + stepTrace + "\n\n", e);
		}
		
		return null;
	}
	
	private Acuse directRequestSoap(String expresionImpresa) {
		HttpURLConnection rc = null;
		URL url = null;
		OutputStreamWriter out = null;
		InputStreamReader read = null;
		StringBuilder sb = null;
		String peticion = "";
		String respuesta = "";
		String soapResponse = "";
		int len = 0;
		int ch = 0;
		// ----------------------------------------------------------------------
		int httpCode = 0;
		String httpMensaje = "";
		// ----------------------------------------------------------------------
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller = null;
		ConsultaResponse response = null;
		Acuse acuse = null;
		
		try {
			// Inicializamos
			peticion = "<![CDATA[" + expresionImpresa + "]]>";
			peticion = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\"><soapenv:Header/><soapenv:Body><tem:Consulta><tem:expresionImpresa>#EXPRESION</tem:expresionImpresa></tem:Consulta></soapenv:Body></soapenv:Envelope>";
			peticion = peticion.replace("#EXPRESION", "<![CDATA[" + expresionImpresa + "]]>");
			len = peticion.length();
			url = new URL("https://consultaqr.facturaelectronica.sat.gob.mx/ConsultaCFDIService.svc?wsdl");
			rc = (HttpURLConnection) url.openConnection();
			rc.setRequestMethod("POST");
			rc.setDoOutput(true);
			rc.setDoInput(true);
			rc.setRequestProperty("Content-Type", "text/xml; charset=utf-8; action=\"http://tempuri.org/IConsultaCFDIService/Consulta\""); //"text/xml; charset=utf-8");
			rc.setRequestProperty("Content-Length", Integer.toString(len));
			rc.connect();
			
			out = new OutputStreamWriter(rc.getOutputStream());
			out.write(peticion, 0, len);
			out.flush();
			httpCode = rc.getResponseCode();
			httpMensaje = rc.getResponseMessage();
			if (httpCode == 200)
				read = new InputStreamReader(rc.getInputStream());
			else
				read = new InputStreamReader(rc.getErrorStream());
			sb = new StringBuilder();
			ch = read.read();
			while (ch != -1) {
				sb.append((char) ch);
				ch = read.read();
			}
			respuesta = sb.toString();
			read.close();
			rc.disconnect();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar ejecutar la peticion (directRequestSoap): " + httpCode + " - " + httpMensaje + "\nPeticion: " + expresionImpresa + "\n\n", e);
			return null;
		} 
		
		try {
			soapResponse = respuesta.substring(respuesta.indexOf("<s:Body>") + 8, respuesta.indexOf("</s:Body>"));
			jaxbContext = JAXBContext.newInstance(new Class[] { ConsultaResponse.class });
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			response = (ConsultaResponse) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(soapResponse.getBytes()));
			acuse = response.getConsultaResult().getValue();
			return acuse;
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar procesar la respuesta del servicio de consultas de estatus de CFDI (SAT :: DirectRequest)\nPeticion: " + expresionImpresa + "\nRecibido:\n" + respuesta + "\n\n", e);
		} 
		
		return null;
	}
	
	/*private String readFile(byte[] archivoSrc) throws Exception {
		return readFile(archivoSrc, "UTF-8");
	}
	
	private String readFile(byte[] archivoSrc, String charsetName) throws Exception {
		InputStream stream = null;
		BufferedReader br = null;
		StringBuilder sb = null;
		String line = "";
		String fileString = "";
		
		try {
			if (charsetName == null || "".equals(charsetName.trim()))
				charsetName = "UTF-8";
			charsetName = charsetName.toUpperCase();
			sb = new StringBuilder();
			stream = new ByteArrayInputStream(archivoSrc);
			br = new BufferedReader(new InputStreamReader(stream, charsetName));
			line = br.readLine();

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
	
	private byte[] aMinusculas(String xml) throws IOException, URISyntaxException, TransformerException {
		ByteArrayOutputStream bf = null;
		TransformerFactory factory = null;
		Transformer transformer = null;
		Source xslt = null;
		Source text = null;
		
		try {
			factory = TransformerFactory.newInstance();
			bf = new ByteArrayOutputStream();
			text = new StreamSource(new ByteArrayInputStream(xml.getBytes()));
			xslt = new StreamSource(this.getClass().getResourceAsStream("/net/giro/cargas/documentos/beans/min1.xslt"));
			transformer = factory.newTransformer(xslt);
			transformer.transform(text, new StreamResult(bf));
			return bf.toByteArray();
		} catch (Exception e) {
			log.error("Ocurrio un problema al minificar el XML", e);
		}
		
		return xml.getBytes();
	}*/
	
	private boolean comprobarEmpresa(String rfcReceptor, String rfcEmisor) {
		rfcReceptor = (rfcReceptor != null && ! "".equals(rfcReceptor.trim()) ? rfcReceptor.trim() : "");
		rfcEmisor = (rfcEmisor != null && ! "".equals(rfcEmisor.trim()) ? rfcEmisor.trim() : "");
		if ("".equals(rfcReceptor.trim()) || "".equals(rfcEmisor.trim()))
			return false;
		if (this.infoSesion == null)
			return true;
		return (this.infoSesion.getEmpresa().getRfc().equals(rfcReceptor.trim()) || this.infoSesion.getEmpresa().getRfc().equals(rfcEmisor.trim()));
	}
	
	private List<ComprobacionFactura> actualizarDatosFacturasPrevias(Comprobante cfdi, List<ComprobacionFactura> facturas) {
		try {
			if (cfdi == null)
				return facturas;
			
			for (ComprobacionFactura factura : facturas) {
				factura.setFacturaTipoCambio(1);
				factura.setDescuentoPesos(0);
				factura.setFacturaImpuestos(0);
				factura.setFacturaRetenciones(0);
				
				if ((factura.getFacturaSerie() == null || "".equals(factura.getFacturaSerie().trim())) && ! "".equals(validateString(cfdi.getSerie())))
					factura.setFacturaSerie(validateString(cfdi.getSerie()));
				if ((factura.getFacturaFolio() == null || "".equals(factura.getFacturaFolio().trim())) && ! "".equals(validateString(cfdi.getFolio())))
					factura.setFacturaFolio(validateString(cfdi.getFolio()));
				if ((factura.getFacturaTipo() == null || "".equals(factura.getFacturaTipo().trim())) && ! "".equals(validateString(cfdi.getTipoDeComprobante().value())))
					factura.setFacturaTipo(validateString(cfdi.getTipoDeComprobante().value()));
				if ((factura.getFacturaMetodo() == null || "".equals(factura.getFacturaMetodo().trim())) && ! "".equals(validateString(cfdi.getMetodoPago().value())))
					factura.setFacturaMetodo(validateString(cfdi.getMetodoPago().value()));
				if ((factura.getFacturaFormaPago() == null || "".equals(factura.getFacturaFormaPago().trim())) && ! "".equals(validateString(cfdi.getFormaPago())))
					factura.setFacturaFormaPago(validateString(cfdi.getFormaPago()));
				if ((factura.getFacturaMoneda() == null || "".equals(factura.getFacturaMoneda().trim())) && ! "".equals(validateString(cfdi.getMoneda().value())))
					factura.setFacturaMoneda(validateString(cfdi.getMoneda().value()));
				if (cfdi.getTipoCambio() != null && ! "".equals(cfdi.getTipoCambio()) && cfdi.getTipoCambio().doubleValue() > 0)
					factura.setFacturaTipoCambio(cfdi.getTipoCambio().doubleValue());
				if (cfdi.getDescuento() != null && ! "".equals(cfdi.getDescuento()))
					factura.setDescuentoPesos(cfdi.getDescuento().doubleValue());
				
				if (cfdi.getImpuestos() != null) {
					if (cfdi.getImpuestos().getTotalImpuestosTrasladados() != null && ! "".equals(cfdi.getImpuestos().getTotalImpuestosTrasladados()))
						factura.setFacturaImpuestos(cfdi.getImpuestos().getTotalImpuestosTrasladados().doubleValue());
					if (cfdi.getImpuestos().getTotalImpuestosRetenidos() != null && ! "".equals(cfdi.getImpuestos().getTotalImpuestosRetenidos()))
						factura.setFacturaRetenciones(cfdi.getImpuestos().getTotalImpuestosRetenidos().doubleValue());
				}

				factura.setFacturaSubtotal(cfdi.getSubTotal().doubleValue());
				factura.setFacturaTotal(cfdi.getTotal().doubleValue());
				factura.conversionPesos();
			}
			
			if (facturas != null && ! facturas.isEmpty())
				facturas = this.saveOrUpdateList(facturas);
		} catch (Exception e) {
			log.error("Ocurrio un problema al actualizar los datos del CFDI en las Facturas cargadas", e);
		}
		
		return facturas;
	}

	private String validaTipoPersona(String rfc) {
		rfc = (rfc != null && ! "".equals(rfc.trim()) ? rfc.trim() : "");
		if ("".equals(rfc.trim()))
			return "P";
		if ("XAXX010101000".equals(rfc.trim()) || "XEXX010101000".equals(rfc.trim()))
			return "N";
		return (rfc.length() == 12 ? "N" : (rfc.length() == 13 ? "P" : "X"));
	}

	private String validaPersonalidad(String rfc) {
		rfc = (rfc != null && ! "".equals(rfc.trim()) ? rfc.trim() : "");
		if ("".equals(rfc.trim()) || "XAXX010101000".equals(rfc.trim()) || "XEXX010101000".equals(rfc.trim()))
			return "M";
		return (rfc.length() == 12 ? "M" : (rfc.length() == 13 ? "F" : "X"));
	}
	
	private String validateString(String value) {
		return (value != null && ! "".equals(value.trim()) ? value.trim() : "");
	}

	private String setDefaultValue(String value, String defaultValue) {
		return (value != null && ! "".equals(value.trim()) ? value.trim() : validateString(defaultValue));
	}
}

/* ----------------------------------------------------------------------------------------------------------------
 *  HISTORIAL DE MODIFICACIONES
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA    | 		AUTOR 		 | DESCRIPCION
 * ----------------------------------------------------------------------------------------------------------------
 *  1.2 | ????-??-??  | ??????????? 		 | Creacion de EJB
 */
