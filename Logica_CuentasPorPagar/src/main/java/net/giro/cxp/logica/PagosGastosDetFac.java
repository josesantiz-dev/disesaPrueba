package net.giro.cxp.logica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import mx.gob.sat.cfdi.v40.Comprobante;
import mx.gob.sat.cfdi.v40.Comprobante.Conceptos.Concepto;
import mx.gob.sat.cfdi.v40.Comprobante.Conceptos.Concepto.Impuestos.Retenciones.Retencion;
import mx.gob.sat.cfdi.v40.Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado;
import net.giro.cargas.documentos.beans.ComprobacionFactura;

import net.giro.cargas.documentos.logica.ComprobacionFacturaRem;
import net.giro.cargas.documentos.respuesta.Errores;
import net.giro.cxp.beans.FacturaConcepto;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.TiposGastoCXP;
import net.giro.cxp.dao.PagosGastosDetDAO;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.respuesta.Respuesta;

@Stateless
public class PagosGastosDetFac implements PagosGastosDetRem {
	private static Logger log = Logger.getLogger(PagosGastosDetFac.class);
	private InfoSesion infoSesion;
	private PagosGastosDetDAO ifzPagosGastosDet;
	private PagosGastosDetImpuestosRem ifzImptos;
	private PagosGastosRfcPermitidosRem ifzRfcPermitidos;
	private ComprobacionFacturaRem ifzFacturas;
	private FtpRem ifzFtp;
	private NQueryRem ifzQuery;
	private ConvertExt convertidor;
	// property constants
	public static final String PROVEEDOR_ID = "proveedorId";
	public static final String REFERENCIA = "referencia";
	public static final String SUBTOTAL = "subtotal";
	public static final String IVA = "iva";
	public static final String CREADO_POR = "creadoPor";
	public static final String MODIFICADO_POR = "modificadoPor";
	public static final String OBSERVACIONES = "observaciones";
	public static final String CONCEPTO_ID = "conceptoId";

	public PagosGastosDetFac() {
		Hashtable<String, Object> environtment = null;
		InitialContext ctx = null; 
		
		try {
			environtment = new Hashtable<String, Object>();
            environtment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            ctx = new InitialContext(environtment);
    		this.ifzPagosGastosDet = (PagosGastosDetDAO) ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosDetImp!net.giro.cxp.dao.PagosGastosDetDAO");
    		this.ifzImptos = (PagosGastosDetImpuestosRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
    		this.ifzRfcPermitidos = (PagosGastosRfcPermitidosRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosRfcPermitidosFac!net.giro.cxp.logica.PagosGastosRfcPermitidosRem");
    		this.ifzFacturas = (ComprobacionFacturaRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//ComprobacionFacturaFac!net.giro.cargas.documentos.logica.ComprobacionFacturaRem");
    		this.ifzFtp = (FtpRem) ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
    		this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
    		this.convertidor = new ConvertExt();
    	} catch (Exception e) {
    		log.error("Ocurrio un problema al inicializar el EJB", e);
		}
	}

	@Override
	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Long save(PagosGastosDet entity) throws Exception {
		try {
			return this.ifzPagosGastosDet.save(entity, getCodigoEmpresa());
		} catch (Exception re) {
			control("Logica_CuentasPorPagar.save(entity)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<PagosGastosDet> saveOrUpdateList(List<PagosGastosDet> entities) throws Exception {
		try {
			return this.ifzPagosGastosDet.saveOrUpdateList(entities, getCodigoEmpresa());
		} catch (Exception re) {
			control("Logica_CuentasPorPagar.saveOrUpdateList(entities)", re);
			throw re;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(PagosGastosDet entity) throws Exception {
		try {
			this.ifzPagosGastosDet.update(entity);
		} catch (Exception re) {
			control("Logica_CuentasPorPagar.update(entity)", re);
			throw re;
		}
	}

	@Override
	public void delete(long idPagosGastosDet) throws Exception {
		try {
			this.ifzPagosGastosDet.delete(idPagosGastosDet);
		} catch (Exception re) {
			control("Logica_CuentasPorPagar.delete(idPagosGastosDet)", re);
			throw re;
		}
	}

	@Override
	public PagosGastosDet findById(Long idPagosGastosDet) {
		try {
			return this.ifzPagosGastosDet.findById(idPagosGastosDet);
		} catch (Exception re) {
			control("Logica_CuentasPorPagar.findById(idPagosGastosDet)", re);
			throw re;
		}
	}
	
	public PagosGastosDet findByUniqueValue(String uniqueValue) throws Exception {
		List<PagosGastosDet> detalles = null;
		
		try {
			detalles = this.ifzPagosGastosDet.findByProperty("uniqueValue", uniqueValue, null);
			if (detalles != null && ! detalles.isEmpty())
				return detalles.get(0);
			return null;
		} catch (Exception re) {
			control("Logica_CuentasPorPagar.findByUniqueValue(uniqueValue)", re);
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findAll(long idPagosGastos) {
		try {
			return this.ifzPagosGastosDet.findAll(idPagosGastos);
		} catch (Exception re) {
			control("Logica_CuentasPorPagar.findAll(idPagosGastos)", re);
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findByFactura(long idFactura) throws Exception {
		try {
			return this.ifzPagosGastosDet.findByFactura(idFactura, false);
		} catch (Exception re) {
			control("Logica_CuentasPorPagar.findByFactura(idFactura, incluyeCanceladas)", re);
			throw re;
		}
	}

	@Override
	public List<PagosGastosDet> findByFactura(long idFactura, boolean incluyeCanceladas) throws Exception {
		try {
			return this.ifzPagosGastosDet.findByFactura(idFactura, incluyeCanceladas);
		} catch (Exception re) {
			control("Logica_CuentasPorPagar.findByFactura(idFactura, incluyeCanceladas)", re);
			throw re;
		}
	}

	@Override
	public Respuesta cargaFacturaXML(byte[] fileSrc, TiposGastoCXP tipoGasto) throws Exception {
		return this.cargaFacturaXML(fileSrc, tipoGasto, null);
	}
	
	@Override
	public Respuesta cargaFacturaXML(byte[] fileSrc, TiposGastoCXP tipoGasto, Long idReferencia) throws Exception {
		Respuesta respuesta = new Respuesta();
		List<FacturaConcepto> conceptos = null;
		FacturaConcepto concepto = null;
		Comprobante cfdi = null;
		double totalImpuesto = 0;
		Long idComprobante = 0L;

		try {
			if (idReferencia == null || idReferencia <= 0)
				idReferencia = 0L;

			respuesta = analizaFactura(fileSrc, null);
			if (respuesta.getErrores().getCodigoError() == 6L) {
				log.warn("La Factura ya existe :: " + tipoGasto);
				if (tipoGasto.equals(TiposGastoCXP.CajaChica))
					return comprobacionesCajaChica(respuesta, idReferencia);
				else if (tipoGasto.equals(TiposGastoCXP.Provision))
					return comprobacionesProvisiones(respuesta, idReferencia);
				else if (tipoGasto.equals(TiposGastoCXP.RegistroGasto))
					return comprobacionesRegistroGasto(respuesta, idReferencia, null);
			}
			
			if (respuesta.getBody().getValor("conceptos") == null && respuesta.getBody().getValor("pojoComprobante") != null) {
				cfdi = (Comprobante) respuesta.getBody().getValor("pojoComprobante");
				if (cfdi.getConceptos().getConcepto().size() > 1) {
					for (Concepto item : cfdi.getConceptos().getConcepto()) {
						concepto = new FacturaConcepto();
						concepto.setIdConcepto(0L);
						concepto.setDescConcepto("");
						concepto.setDescripcion(item.getDescripcion());
						concepto.setImporte(item.getImporte().doubleValue());
						concepto.setObservaciones("");
						
						if (item.getImpuestos() != null) {
							// Impuestos Trasladados
							if (item.getImpuestos().getTraslados() != null && ! item.getImpuestos().getTraslados().getTraslado().isEmpty()) {
								totalImpuesto = 0;
								for (Traslado imp : item.getImpuestos().getTraslados().getTraslado()) {
									concepto.getTraslados().put(imp.getImpuesto(), imp.getTasaOCuota().doubleValue());
									totalImpuesto += imp.getImporte().doubleValue();
								}
								concepto.setImpuesto(totalImpuesto);
							}

							// Impuestos Retenidos
							if (item.getImpuestos().getRetenciones() != null && ! item.getImpuestos().getRetenciones().getRetencion().isEmpty()) {
								totalImpuesto = 0;
								for (Retencion imp : item.getImpuestos().getRetenciones().getRetencion()) {
									concepto.getRetenciones().put(imp.getImpuesto(), imp.getTasaOCuota().doubleValue());
									totalImpuesto += imp.getImporte().doubleValue();
								}
								concepto.setRetencion(totalImpuesto);
							}
						}
						
						conceptos = (conceptos != null ? conceptos : new ArrayList<FacturaConcepto>());
						conceptos.add(concepto);
					}
	
					// Añadimos los conceptos
					respuesta.getBody().addValor("conceptos", conceptos);
				}
			}
			
			idComprobante = validate2Long(respuesta.getBody().getValor("idComprobante"));
			if (idComprobante != null && idComprobante > 0L)
				respuesta.getBody().addValor("valores", this.facturaValores(idComprobante));
		} catch (Exception e) {
			control("Ocurrio un problema en Logica_CuentasPorPagar.PagosGastosDetFac.cargaFacturaXML(xml, tipoGasto) al procesar la Factura (XML)\n Exception:\n", e);
			respuesta.getErrores().addCodigo("CXP", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al procesar la Factura (XML)");
		} 
		
		return respuesta;
	}

	@Override
	public Respuesta procesarCFDI(byte[] fileSrc, TiposGastoCXP tipoGasto, Long idReferencia, String fileName) throws Exception {
		return this.procesarCFDI(fileSrc, tipoGasto, idReferencia, fileName, null);
	}

	@Override
	public Respuesta procesarCFDI(byte[] fileSrc, TiposGastoCXP tipoGasto, Long idReferencia, String fileName, String rfcReferencia) throws Exception {
		Respuesta respuesta = null;
		Long idComprobante = 0L;
		String rfcEmisor = "";

		try {
			respuesta = new Respuesta();
			if (this.infoSesion == null) {
				log.info("Ocurrio un problema al validar la sesion de Usuario. Sesion nula");
				respuesta.getErrores().addCodigo("CXP", Errores.ERROR_INESPERADO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_INESPERADO);
				respuesta.getErrores().setDescError("No se puede comprobar la sesion de Usuario");
				return respuesta;
			}
			
			if (fileSrc == null || fileSrc.length <= 0) {
				log.info("No indico el XML");
				respuesta.getErrores().addCodigo("CXP", Errores.ERROR_INESPERADO);
				respuesta.getErrores().setCodigoError(Errores.ERROR_INESPERADO);
				respuesta.getErrores().setDescError("No indico el XML");
				return respuesta;
			}
			
			idReferencia = (idReferencia != null && idReferencia > 0L ? idReferencia : 0L);
			fileName = (fileName != null && ! "".equals(fileName.trim()) ? fileName.trim() : "");
			rfcReferencia = (rfcReferencia != null && ! "".equals(rfcReferencia.trim()) ? rfcReferencia.trim() : "");
			respuesta = analizaFactura(fileSrc, fileName);
			if (respuesta.getErrores().getCodigoError() == 6L) {
				log.warn("La Factura ya existe :: " + tipoGasto);
				if (tipoGasto.equals(TiposGastoCXP.CajaChica))
					return comprobacionesCajaChica(respuesta, idReferencia);
				else if (tipoGasto.equals(TiposGastoCXP.Provision))
					return comprobacionesProvisiones(respuesta, idReferencia);
				else if (tipoGasto.equals(TiposGastoCXP.RegistroGasto))
					return comprobacionesRegistroGasto(respuesta, idReferencia, rfcReferencia);
			}
			
			idComprobante = validate2Long(respuesta.getBody().getValor("idComprobante"));
			if (idComprobante != null && idComprobante > 0L)
				respuesta.getBody().addValor("valores", this.facturaValores(idComprobante));

			if (rfcReferencia != null && ! "".equals(rfcReferencia.trim())) {
				rfcEmisor = validate2String(respuesta.getBody().getValor("comprobanteEmisor"));
				if (! rfcReferencia.trim().equals(rfcEmisor.trim())) {
					log.info("ERROR_XML_RFC_EMISOR - RFC DOCUMENTO " + rfcEmisor + " vs " + rfcReferencia + " :: Emisor distincto ");
					respuesta.getErrores().addCodigo("CXP", Errores.ERROR_XML_RFC_EMISOR);
					respuesta.getErrores().setCodigoError(Errores.ERROR_XML_RFC_EMISOR);
					respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_XML_RFC_EMISOR));
					respuesta.getBody().addValor("idComprobante", idComprobante); 
					return respuesta;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema en Logica_CuentasPorPagar.PagosGastosDetFac.cargaFacturaXML(xml, tipoGasto) al procesar la Factura (XML)\n Exception:\n", e);
			respuesta.getErrores().addCodigo("CXP", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(1L);
			respuesta.getErrores().setDescError("Ocurrio un problema al procesar la Factura (XML)");
		} 
		
		return respuesta;
	}
	
	@Override
	public void eliminarFactura(long idFactura) throws Exception {
		try {
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			this.ifzFacturas.deleteXML(idFactura);
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.eliminarFactura.eliminarFactura(idFactura)", e);
		}
	}

	@Override
	public HashMap<String, Object> facturaValores(long idFactura) throws Exception {
		HashMap<String, Object> valores = new HashMap<String, Object>();
		ComprobacionFactura factura = null;
		
		try {
			this.ifzImptos.setInfoSesion(this.infoSesion);
			factura = this.ifzFacturas.findById(idFactura);
			if (factura != null && factura.getId() > 0L) {
				valores.put("id", idFactura);
				valores.put("uuid", factura.getFacturaFolioFiscal());
				valores.put("factura", factura.getFacturaSerie() + "-" + factura.getFacturaFolio());
				valores.put("serie", factura.getFacturaSerie());
				valores.put("folio", factura.getFacturaFolio());
				valores.put("tipo", factura.getFacturaTipo());
				valores.put("formaPago", factura.getFacturaFormaPago());
				valores.put("metodoPago", factura.getFacturaMetodo());
				valores.put("rfc", factura.getRfcEmisor());
				valores.put("emisor", factura.getFacturaEmisor());
				valores.put("moneda", factura.getFacturaMoneda());
				valores.put("tipoCambio", factura.getFacturaTipoCambio());
				valores.put("descuento", factura.getDescuentoPesos());
				valores.put("subtotal", factura.getSubtotalPesos());
				valores.put("impuestos", factura.getImpuestosPesos());
				valores.put("retenciones", factura.getRetencionesPesos());
				valores.put("total", factura.getTotalPesos());
			}
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.PagosGastosDetFac.getFacturaProperty(idFactura)", e);
		}
		
		return valores;
	}
	
	@Override
	public String getFacturaProperty(long idFactura, String property) {
		String resultado = "";
		
		try {
			this.ifzImptos.setInfoSesion(this.infoSesion);
			resultado = this.ifzFacturas.getProperty(idFactura, property);
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.PagosGastosDetFac.getFacturaProperty(idFactura)", e);
			resultado = "";
		}
		
		return resultado;
	}
	
	@Override
	public void setFacturaProperty(long idFactura, String property, String value) throws Exception {
		try {
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			this.ifzFacturas.setProperty(idFactura, property, value);
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.PagosGastosDetFac.setFacturaProperty. Exception: ", e);
		}
	}

	@Override
	public double getFacturaTipoCambio(long idFactura) throws Exception {
		try {
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			return this.ifzFacturas.getFacturaTipoCambio(idFactura);
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.getFacturaTipoCambio(idFactura)", e);
			throw e;
		}
	}

	@Override
	public double getFacturaDescuento(long idFactura) throws Exception {
		try {
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			return this.ifzFacturas.getFacturaDescuento(idFactura);
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.getFacturaDescuento(idFactura)", e);
			throw e;
		}
	}

	@Override
	public double getFacturaSubtotal(long idFactura) throws Exception {
		try {
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			return this.ifzFacturas.getFacturaSubtotal(idFactura);
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.getFacturaSubtotal(idFactura)", e);
			throw e;
		}
	}

	@Override
	public double getFacturaTotal(long idFactura) throws Exception {
		try {
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			return this.ifzFacturas.getFacturaTotal(idFactura);
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.getFacturaTotal(idFactura)", e);
			throw e;
		}
	}

	// --------------------------------------------------------------------------------------
	
	@Override
	public PagosGastosDetExt convertir(PagosGastosDet entity) throws Exception {
		return this.convertidor.PagosGastosDetToPagosGastosDetExt(entity);
	}
	
	@Override
	public PagosGastosDet convertir(PagosGastosDetExt extendido) throws Exception {	
		return this.convertidor.PagosGastosDetExtToPagosGastosDet(extendido);
	}

	// --------------------------------------------------------------------------------------
	// EXTENDIDO 
	// --------------------------------------------------------------------------------------

	@Override
	public Long save(PagosGastosDetExt extendido) throws Exception {
		try {
			return this.save(this.convertidor.PagosGastosDetExtToPagosGastosDet(extendido));
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDetExt> saveOrUpdateListExt(List<PagosGastosDetExt> extendidos) throws Exception {
		List<PagosGastosDet> entities = new ArrayList<PagosGastosDet>();
		
		try {
			for (PagosGastosDetExt item : extendidos)
				entities.add(this.convertir(item));
			entities = this.saveOrUpdateList(entities);
			extendidos.clear();
			for (PagosGastosDet item : entities)
				extendidos.add(this.convertir(item));
		} catch (Exception re ) {
			throw re;
		}
		
		return extendidos;
	}

	@Override
	public void update(PagosGastosDetExt extendido) throws Exception {
		try {
			this.update(this.convertidor.PagosGastosDetExtToPagosGastosDet(extendido));
		} catch (Exception re) {
			throw re;
		}
	}

	@Override
	public List<PagosGastosDetExt> findExtAll(long idPagosGastos) throws Exception {
		List<PagosGastosDetExt> extendidos = new ArrayList<PagosGastosDetExt>();
		List<PagosGastosDet> entities = null;
		
		try {
			entities = this.findAll(idPagosGastos);
			if (entities == null || entities.isEmpty()) 
				return extendidos;
			
			for (PagosGastosDet entity : entities)
				extendidos.add(this.convertidor.PagosGastosDetToPagosGastosDetExt(entity));
			return extendidos;
		} catch (Exception re) {
			throw re;
		}
	}

	// --------------------------------------------------------------------------------------
	// PRIVADOS 
	// --------------------------------------------------------------------------------------

	private Respuesta analizaFactura(byte[] fileSrc, String fileName) throws Exception {
		Respuesta respuesta = null;
		Comprobante pojoComprobante = null;
		boolean validarEstatusCFDI = false;
		String stepTrace = "";
		long errorCode = 0L;
		// ------------------------------------------------
		String valor = "";
		Long idFactura = 0L;
		// ------------------------------------------------
		List<FacturaConcepto> conceptos = null;
		
		try {
			respuesta = new Respuesta();
			validarEstatusCFDI = comprobarValidacionEstatusCFDI("CFDI_VALIDACION_ESTATUS");
			stepTrace += "|setting-infoSesion";
			stepTrace += "|send-analizarXML";

			// Recuperamos Comprobante
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			log.info("Antes de analizar factura");
			respuesta = this.ifzFacturas.analizarXML(fileSrc, validarEstatusCFDI);
			log.info("Despues de analizar factura");
			if (respuesta.getBody().getValor("pojoComprobante") != null) {
				log.info("Pojo comprobante no es null");
				pojoComprobante = (Comprobante) respuesta.getBody().getValor("pojoComprobante");

				log.info("Inicia recuperacion conceptos");
				conceptos = recuperarConceptos(pojoComprobante.getConceptos().getConcepto());
				log.info("Termino recuperacion de conceptos");
				respuesta.getBody().addValor("conceptos", conceptos); 
			}else{
				log.info("Pojo comprobante es null");
			}
			
			// Validamos resultado de carga
			errorCode = respuesta.getErrores().getCodigoError(); 
			stepTrace += "|trace-analizarXML{" + respuesta.getBody().getValor("stepTrace").toString() + "}";
			if (errorCode != 0L) {
				log.info("Encontro un error con el codigo :: " + errorCode);
				control("Error en Logica_CuentasPorPagar.PagosGastosDetFac.analizaFactura)\n\nTRACE\n" + stepTrace + "\n\n ERROR: " + respuesta.getErrores().getDescError());
				respuesta.getBody().addValor("stepTrace", stepTrace);
				return respuesta;
			}
			
			stepTrace += "|setting-respuesta";
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
			// Recuperamo ID generado de la carga
			valor = respuesta.getBody().getValor("idComprobante").toString(); 
			if (valor != null && ! "".equals(valor.trim()))
				idFactura = Long.parseLong(valor.trim());

			log.info("Asigna valores a respuesta");
			respuesta.getBody().addValor("comprobanteEmisor", pojoComprobante.getEmisor().getRfc());
			respuesta.getBody().addValor("comprobanteReceptor", pojoComprobante.getReceptor().getRfc());
			respuesta.getBody().addValor("comprobanteTipo", pojoComprobante.getTipoDeComprobante().value());
			respuesta.getBody().addValor("comprobanteMetodo", pojoComprobante.getMetodoPago());
			respuesta.getBody().addValor("comprobanteFormaPago", pojoComprobante.getFormaPago());
			respuesta.getBody().addValor("comprobanteMoneda", pojoComprobante.getMoneda());
			respuesta.getBody().addValor("comprobanteTipoCambio", pojoComprobante.getTipoCambio());
			respuesta.getBody().addValor("comprobanteDescuento", pojoComprobante.getDescuento()); 
			respuesta.getBody().addValor("comprobanteSubtotal", pojoComprobante.getSubTotal());
			log.info("Finaliza la asignacion de valores a respuesta");

			stepTrace += "|passing-pojoAcuse";
			respuesta.getBody().addValor("stepTrace", stepTrace);
			log.info("Proceso completo.\n\nTRACE\n" + stepTrace + "\n\n");
			
			if (fileName != null && ! "".equals(fileName.trim())) {
				fileName = fileName.replace("[ID]", idFactura.toString());
				log.info("Subimos XML ... ");
				if (! this.ifzFtp.put(fileSrc, fileName)) {
					control("Ocurrio un problema al intentar guardar la factura en el servidor");
					stepTrace += "|Ocurrio un problema al intentar guardar la factura en el servidor";
					respuesta.getBody().addValor("stepTrace", stepTrace);
					log.info("FTP - Carga FAIL");
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar interpretar analizaFactura\nTRACE:" + stepTrace + "\nException:\n", e);
			respuesta.getErrores().addCodigo("CXP", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setDescError("Ocurrio un problema al Analizar el XML");
			respuesta.getBody().addValor("stepTrace", stepTrace);
		} 
		
		return respuesta;
	}

	/**
	 * Validacion para Cajas Chicas. Errores 100
	 * @param respuesta
	 * @param idProvisionReferencia
	 * @return
	 * @throws Exception
	 */
	private Respuesta comprobacionesCajaChica(Respuesta respuesta, long idCajaChicaReferencia) throws Exception {
		List<PagosGastosDet> listDetalles = null;
		ComprobacionFactura pojoFactura = null;
		long idFactura = 0L;
		String facturaTipo = "";
		String facturaMetodo = "";
		String facturaEmisor = "";
		String stepTrace = "";
		// ---------------------------------------
		double facturado = 0;
		//double pagado = 0;
		double saldo = 0;
		String comprobados = "";

		try {
			stepTrace = (String) respuesta.getBody().getValor("stepTrace") + "|[comprobacionesCajaChica]";
			// Recupero el ID de la carga previa (XML)
			idFactura = (long) respuesta.getBody().getValor("idComprobante");
			facturaTipo = validaTipoComprobante((String) respuesta.getBody().getValor("comprobanteTipo"));
			facturaMetodo = validaMetodoPago((String) respuesta.getBody().getValor("comprobanteMetodo"));
			facturaEmisor = (String) respuesta.getBody().getValor("comprobanteEmisor");
			stepTrace += "|id-xml-previo-" + idFactura;
			
			if (idFactura > 0L)
				respuesta.getBody().addValor("valores", this.facturaValores(idFactura));
			
			// Solo facturas de ingresos son permitidas
			if (! "I".equals(facturaTipo)) {
				respuesta.getErrores().setCodigoError(101L);
				respuesta.getErrores().setDescError("Tipo de Comprobante no valido.\nSolo Facturas de Ingresos(I) son permitidas");
				return respuesta;
			}

			// Solo facturas pago en una sola exhibicion son permitidas this.ifzRfcPermitidos.findAll(rfc);
			if (! "PUE".equals(facturaMetodo)) {
				if (! this.ifzRfcPermitidos.comprobarPermiso("C", facturaEmisor, facturaTipo, facturaMetodo)) {
					respuesta.getErrores().setCodigoError(102L);
					respuesta.getErrores().setDescError("Metodo de Pago del Comprobante no valido.\nSolo Facturas de Contado (PUE-Pago en una Sola Exhibicion) son permitidas");
					return respuesta;
				}
			}
			
			this.ifzFacturas.setInfoSesion(this.infoSesion);
			pojoFactura = this.ifzFacturas.findById(idFactura);
			facturado = Double.valueOf(pojoFactura.getTotalPesos());
			saldo = facturado;
			
			// Recuperamos las comprobaciones que tienen asignado el mismo XML
			stepTrace += "|verificar-comprobaciones";
			listDetalles = this.findByFactura(idFactura);
			if (listDetalles != null && ! listDetalles.isEmpty()) {
				respuesta.getBody().addValor("conceptos", null); 
				// Recuperamos comprobaciones previas
				stepTrace += "|comprobaciones-encontradas-x" + listDetalles.size();
				//pagado = 0;
				for (PagosGastosDet item : listDetalles) {
					if (item.getIdPagosGastos().getId().longValue() == idCajaChicaReferencia)
						continue;
					comprobados = agregaExpresion(comprobados, item, facturaTipo);
					
					// Si ya esta en Caja Chica, descartamos la carga
					if ("C".equals(item.getIdPagosGastos().getTipo())) {
						respuesta.getErrores().setCodigoError(103L);
						respuesta.getErrores().setDescError("La Factura ya ha sido Comprobada.");
						return respuesta;
					}
					
					// Si es Reg. Egreso, el metodo de pago debe ser PPD (Pago en parcialidades o diferido)
					if ("P".contains(item.getIdPagosGastos().getTipo()) && ! "PPD".equals(facturaMetodo)) {
						respuesta.getErrores().setCodigoError(104L);
						respuesta.getErrores().setDescError("La Factura ya ha sido Registrada como Egreso.");
						return respuesta;
					}
				}
			}

			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.PagosGastosDetFac.analizaFactura()\n\nTRACE\n" + stepTrace + "\n\n Exception:\n", e);
			respuesta.getErrores().addCodigo("CXP", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(100L);
			respuesta.getErrores().setDescError("Ocurrio un problema al procesar el Comprobante (XML)");
		} finally {
			respuesta.getBody().addValor("stepTrace", stepTrace);
			respuesta.getBody().addValor("comprobados", comprobados); 
			
			if (pojoFactura != null) {
				respuesta.getBody().addValor("pojoComprobacionFactura", pojoFactura);
				respuesta.getBody().addValor("idComprobante", pojoFactura.getId()); 
				respuesta.getBody().addValor("comprobanteFactura", pojoFactura.getFactura()); 
				respuesta.getBody().addValor("comprobanteSerie", pojoFactura.getFacturaSerie()); 
				respuesta.getBody().addValor("comprobanteFolio", pojoFactura.getFacturaFolio()); 
				respuesta.getBody().addValor("comprobanteRfc", pojoFactura.getRfcEmisor()); 
				respuesta.getBody().addValor("comprobanteRazonSocial", pojoFactura.getFacturaEmisor()); 
				respuesta.getBody().addValor("comprobanteRazonSocialRfc", pojoFactura.getRazonSocialORfc()); 
				respuesta.getBody().addValor("comprobanteTotal", facturado); 
				respuesta.getBody().addValor("comprobanteSaldo", saldo);
			}
		}
		
		return respuesta;
	}
	
	/**
	 * Validacion para Provisiones. Errores 200
	 * @param respuesta
	 * @param idProvisionReferencia
	 * @return
	 * @throws Exception
	 */
	private Respuesta comprobacionesProvisiones(Respuesta respuesta, long idProvisionReferencia) throws Exception {
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		List<PagosGastosDet> listDetalles = null;
		ComprobacionFactura pojoFactura = null;
		long idFactura = 0L;
		Date fechaFactura = null;
		String facturaTipo = "";
		String stepTrace = "";
		// ---------------------------------------
		double facturado = 0;
		double saldo = 0;
		double pagadoMes = 0;
		//double pagadoPosterior = 0;
		int mesFacturado = 0;
		int mesPagado = 0;
		String comprobados = "";

		try {
			stepTrace = (String) respuesta.getBody().getValor("stepTrace") + "|[comprobacionesProvisiones]";
			// Recupero el ID de la carga previa (XML)
			idFactura = (long) respuesta.getBody().getValor("idComprobante");
			facturaTipo = validaTipoComprobante((String) respuesta.getBody().getValor("comprobanteTipo"));
			fechaFactura = dtFormat.parse(respuesta.getBody().getValor("comprobanteFecha").toString());
			stepTrace += "|id-xml-previo-" + idFactura;
			
			if (idFactura > 0L)
				respuesta.getBody().addValor("valores", this.facturaValores(idFactura));
			
			// Validamos el Tipo de Comprobante
			if (! "I,E,N".contains(facturaTipo)) {
				respuesta.getErrores().setCodigoError(201L);
				respuesta.getErrores().setDescError("El Tipo de Comprobante no es valido para Provisionar.");
				return respuesta;
			}
			
			pojoFactura = this.ifzFacturas.findById(idFactura);
			facturado = Double.valueOf(pojoFactura.getTotalPesos());
			saldo = facturado;
			cal.setTime(fechaFactura);
			mesFacturado = cal.get(Calendar.MONTH);
			
			// Recuperamos las comprobaciones que tienen asignado el mismo XML
			stepTrace += "|verificar-comprobaciones";
			listDetalles = this.findByFactura(idFactura);
			if (listDetalles != null && ! listDetalles.isEmpty()) {
				respuesta.getBody().addValor("conceptos", null); 
				pagadoMes = 0;
				//pagadoPosterior = 0;
				stepTrace += "|comprobaciones-encontradas-x" + listDetalles.size();
				stepTrace += "|calculando saldo";
				for (PagosGastosDet item : listDetalles) {
					// Si es un egreso, descartamos la carga
					if ("E".equals(facturaTipo)) {
						comprobados = agregaExpresion(comprobados, item, facturaTipo);
						return respuesta;
					}
					
					if (item.getIdPagosGastos().getId().longValue() == idProvisionReferencia)
						continue;
					comprobados = agregaExpresion(comprobados, item, facturaTipo);
					
					// Si ya ha sido provisionada, descartamos la carga
					if ("F".equals(item.getIdPagosGastos().getTipo())) {
						respuesta.getErrores().setCodigoError(203L);
						respuesta.getErrores().setDescError("El Comprobante ya ha sido Provisionado.");
						return respuesta;
					}
					
					// Si ya tiene pago, comprobamos el saldo segun fecha de registro
					if ("P".equals(item.getIdPagosGastos().getTipo())) {
						cal.setTime(item.getFechaCreacion()); 
						mesPagado = cal.get(Calendar.MONTH);
						if (mesPagado == mesFacturado)
							pagadoMes += item.getTotal();
						/*else if (mesPagado > mesFacturado)
							pagadoPosterior += item.getTotal();*/
					}
				}
				
				if (pagadoMes > 0) {
					saldo = saldo - pagadoMes;
					if (saldo <= 0) {
						log.info("ERROR_XML_SALDADO - ID DOCUMENTO " + idFactura + " :: Factura saldada ");
						respuesta.getErrores().addCodigo("CXP", Errores.ERROR_XML_SALDADO);
						respuesta.getErrores().setCodigoError(Errores.ERROR_XML_SALDADO);
						respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_XML_SALDADO));
						respuesta.getBody().addValor("idComprobante", idFactura); 
						return respuesta;
					}
				}
				
				/*if (saldo > 0 && pagadoMes <= 0 && pagadoPosterior > 0) {
					saldo = saldo - pagadoPosterior;
					if (saldo <= 0) {
						log.info("ERROR_XML_SALDADO - ID DOCUMENTO " + idFactura + " :: Factura saldada ");
						respuesta.getErrores().addCodigo("CXP", Errores.ERROR_XML_SALDADO);
						respuesta.getErrores().setCodigoError(Errores.ERROR_XML_SALDADO);
						respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_XML_SALDADO));
						respuesta.getBody().addValor("idComprobante", idFactura); 
						return respuesta;
					}
				}*/
			}
			
			// Comprobamos si debemos añadir los conceptos
			/*if (respuesta.getBody().getValor("pojoComprobante") != null) {
				cfdi = (Comprobante) respuesta.getBody().getValor("pojoComprobante");
				conceptos = recuperarConceptos(cfdi.getConceptos());
				respuesta.getBody().addValor("conceptos", conceptos); */
				/*if (cfdi.getConceptos().size() > 1) {
					for (Concepto item : cfdi.getConceptos()) {
						concepto = new FacturaConcepto();
						concepto.setIdConcepto(0L);
						concepto.setDescConcepto("");
						concepto.setDescripcion(item.getDescripcion());
						concepto.setImporte(item.getImporte().doubleValue());
						concepto.setObservaciones("");
						
						if (item.getImpuestos() != null) {
							// Impuestos Trasladados
							if (item.getImpuestos().getTraslados() != null && ! item.getImpuestos().getTraslados().isEmpty()) {
								totalImpuesto = 0;
								for (Traslado imp : item.getImpuestos().getTraslados()) {
									concepto.getTraslados().put(imp.getImpuesto(), 0D);
									if (! "exento".equals(imp.getTipofactor().trim().toLowerCase())) {
										concepto.getTraslados().put(imp.getImpuesto(), Double.parseDouble(imp.getTasaOCuota()));
										totalImpuesto += Double.parseDouble(imp.getImporte());
									}
								}
								concepto.setImpuesto(totalImpuesto);
							}
							
							// Impuestos Retenidos
							if (item.getImpuestos().getRetenciones() != null && ! item.getImpuestos().getRetenciones().isEmpty()) {
								totalImpuesto = 0;
								for (Retencion imp : item.getImpuestos().getRetenciones()) {
									concepto.getRetenciones().put(imp.getImpuesto(), Double.parseDouble(imp.getTasaOCuota()));
									totalImpuesto += Double.parseDouble(imp.getImporte());
								}
								concepto.setRetencion(totalImpuesto);
							}
						}
						
						if (conceptos == null)
							conceptos = new ArrayList<FacturaConcepto>();
						conceptos.add(concepto);
					}
				}*/
			//}
			
			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.PagosGastosDetFac.analizaFactura()\n\nTRACE\n" + stepTrace + "\n\n Exception:\n", e);
			respuesta.getErrores().addCodigo("CXP", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(200L);
			respuesta.getErrores().setDescError("Ocurrio un problema al procesar el Comprobante (XML)");
		} finally {
			respuesta.getBody().addValor("stepTrace", stepTrace);
			respuesta.getBody().addValor("comprobados", comprobados); 
			
			if (pojoFactura != null) {
				respuesta.getBody().addValor("pojoComprobacionFactura", pojoFactura);
				respuesta.getBody().addValor("idComprobante", pojoFactura.getId()); 
				respuesta.getBody().addValor("comprobanteFactura", pojoFactura.getFactura()); 
				respuesta.getBody().addValor("comprobanteSerie", pojoFactura.getFacturaSerie()); 
				respuesta.getBody().addValor("comprobanteFolio", pojoFactura.getFacturaFolio()); 
				respuesta.getBody().addValor("comprobanteRfc", pojoFactura.getRfcEmisor()); 
				respuesta.getBody().addValor("comprobanteRazonSocial", pojoFactura.getFacturaEmisor()); 
				respuesta.getBody().addValor("comprobanteRazonSocialRfc", pojoFactura.getRazonSocialORfc()); 
				respuesta.getBody().addValor("comprobanteTotal", facturado); 
				respuesta.getBody().addValor("comprobanteSaldo", saldo);
			}
		}
		
		return respuesta;
	}

	/**
	 * Validacion para Registro Egresos. Errores 300
	 * @param respuesta
	 * @param idProvisionReferencia
	 * @return
	 * @throws Exception
	 */
	private Respuesta comprobacionesRegistroGasto(Respuesta respuesta, long idRegistroGastoReferencia, String rfcEmisorPermitido) throws Exception {
		String stepTrace = "";
		List<PagosGastosDet> listDetalles = null;
		ComprobacionFactura pojoFactura = null;
		long idFactura = 0L;
		double facturado = 0;
		double pagado = 0;
		double saldo = 0;
		String facturaTipo = "";
		String rfcEmisor = "";
		String comprobados = "";

		try {
			stepTrace = (String) respuesta.getBody().getValor("stepTrace") + "|[comprobacionesRegistroGasto]";
			// Recupero el ID de la carga previa (XML)
			idFactura = validate2Long(respuesta.getBody().getValor("idComprobante"));
			facturaTipo = validaTipoComprobante(validate2String(respuesta.getBody().getValor("comprobanteTipo")));
			rfcEmisor = validate2String(respuesta.getBody().getValor("comprobanteEmisor"));
			stepTrace += "|id-xml-previo-" + idFactura;
			
			if (rfcEmisorPermitido != null && ! "".equals(rfcEmisorPermitido.trim()) && ! rfcEmisorPermitido.trim().equals(rfcEmisor.trim())) {
				log.info("ERROR_XML_RFC_EMISOR - RFC DOCUMENTO " + rfcEmisor + " vs " + rfcEmisorPermitido + " :: Emisor distincto ");
				respuesta.getErrores().addCodigo("CXP", Errores.ERROR_XML_RFC_EMISOR);
				respuesta.getErrores().setCodigoError(Errores.ERROR_XML_RFC_EMISOR);
				respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_XML_RFC_EMISOR));
				respuesta.getBody().addValor("idComprobante", idFactura); 
				return respuesta;
			}
			
			if (idFactura > 0L)
				respuesta.getBody().addValor("valores", this.facturaValores(idFactura));

			this.ifzFacturas.setInfoSesion(this.infoSesion);
			pojoFactura = this.ifzFacturas.findById(idFactura);
			facturado = Double.valueOf(pojoFactura.getTotalPesos());
			saldo = facturado;
			
			// Recuperamos las comprobaciones que tienen asignado el mismo XML
			stepTrace += "|verificar-comprobaciones";
			listDetalles = this.findByFactura(idFactura);
			if (listDetalles != null && ! listDetalles.isEmpty()) {
				respuesta.getBody().addValor("conceptos", null); 
				// Calcular saldo
				pagado = 0D;
				stepTrace += "|comprobaciones-encontradas-x" + listDetalles.size();
				stepTrace += "|calculando saldo";
				for (PagosGastosDet item : listDetalles) {
					// Comprobamos: Solo los ingresos se pueden cargar multiples veces
					if ("E".equals(facturaTipo)) {
						comprobados = agregaExpresion(comprobados, item, facturaTipo);
						return respuesta;
					}
					
					if (item.getIdPagosGastos().getId().longValue() == idRegistroGastoReferencia)
						continue;
					comprobados = agregaExpresion(comprobados, item, facturaTipo);

					if ("P".equals(item.getIdPagosGastos().getTipo()))
						pagado += item.getTotal(); 
				}
				
				if (pagado > 0) {
					saldo = saldo - pagado;
					if (saldo <= 0) {
						log.info("ERROR_XML_SALDADO - ID DOCUMENTO " + idFactura + " :: Factura saldada ");
						respuesta.getErrores().addCodigo("CXP", Errores.ERROR_XML_SALDADO);
						respuesta.getErrores().setCodigoError(Errores.ERROR_XML_SALDADO);
						respuesta.getErrores().setDescError(Errores.descError.get(Errores.ERROR_XML_SALDADO));
						respuesta.getBody().addValor("idComprobante", idFactura); 
						return respuesta;
					}
				}
			}

			respuesta.getErrores().setCodigoError(0L);
			respuesta.getErrores().setDescError("");
		} catch (Exception e) {
			control("Error en Logica_CuentasPorPagar.PagosGastosDetFac.analizaFactura()\n\nTRACE\n" + stepTrace + "\n\n Exception:\n", e);
			respuesta.getErrores().addCodigo("CXP", Errores.ERROR_ANALIZAR_XML);
			respuesta.getErrores().setCodigoError(300L);
			respuesta.getErrores().setDescError("Ocurrio un problema al procesar el Comprobante (XML)");
		} finally {
			respuesta.getBody().addValor("stepTrace", stepTrace);
			respuesta.getBody().addValor("comprobados", comprobados); 
			
			if (pojoFactura != null) {
				respuesta.getBody().addValor("pojoComprobacionFactura", pojoFactura);
				respuesta.getBody().addValor("idComprobante", pojoFactura.getId()); 
				respuesta.getBody().addValor("comprobanteFactura", pojoFactura.getFactura()); 
				respuesta.getBody().addValor("comprobanteSerie", pojoFactura.getFacturaSerie()); 
				respuesta.getBody().addValor("comprobanteFolio", pojoFactura.getFacturaFolio()); 
				respuesta.getBody().addValor("comprobanteFolioFactura", pojoFactura.getFacturaFolio()); 
				respuesta.getBody().addValor("comprobanteRfc", pojoFactura.getRfcEmisor()); 
				respuesta.getBody().addValor("comprobanteRazonSocial", pojoFactura.getFacturaEmisor()); 
				respuesta.getBody().addValor("comprobanteRazonSocialRfc", pojoFactura.getRazonSocialORfc());
				respuesta.getBody().addValor("comprobanteTipoPersona", validaTipoPersona(pojoFactura.getRfcEmisor()));
				respuesta.getBody().addValor("comprobanteTotal", facturado); 
				respuesta.getBody().addValor("comprobanteSaldo", saldo);
			}
		}
		
		return respuesta;
	}

	@SuppressWarnings("unchecked")
	private boolean comprobarValidacionEstatusCFDI(String perfilName) {
		List<String> nativeResult = null;
		String queryString = "";
		String resultado = "";
		
		try {
			queryString += "select b.ai from d7729f32ba7 a inner join b761110ccfe b on b.af = a.aa where a.af = ':perfilName' ";
			queryString = queryString.replace(":perfilName", perfilName);
			nativeResult = this.ifzQuery.findNativeQuery(queryString);
			if (nativeResult != null && ! nativeResult.isEmpty())
				resultado = nativeResult.get(0);
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Perfil indicado: " + perfilName, e);
		}
		
		return ("S".equals(resultado));
	}
	
	private List<FacturaConcepto> recuperarConceptos(List<Concepto> cfdiConceptos) {
		List<FacturaConcepto> conceptos = null;
		FacturaConcepto concepto = null;
		double totalImpuesto = 0;

		if (cfdiConceptos == null || cfdiConceptos.isEmpty())
			return null;

		for (Concepto item : cfdiConceptos) {
			concepto = new FacturaConcepto();
			concepto.setIdConcepto(0L);
			concepto.setDescConcepto(item.getDescripcion());
			concepto.setDescripcion(item.getDescripcion());
			concepto.setImporte(item.getImporte().doubleValue());
			concepto.setObservaciones("");
			
			if (item.getImpuestos() != null) {
				// Impuestos Trasladados
				if (item.getImpuestos().getTraslados() != null && ! item.getImpuestos().getTraslados().getTraslado().isEmpty()) {
					totalImpuesto = 0;
					for (Traslado imp : item.getImpuestos().getTraslados().getTraslado()) {
						concepto.getTraslados().put(imp.getImpuesto(), 0D);
						if (! "exento".equals(imp.getTipoFactor().value().trim().toLowerCase())) {
							concepto.getTraslados().put(imp.getImpuesto(), imp.getTasaOCuota().doubleValue());
							totalImpuesto += imp.getImporte().doubleValue();
						}
					}
					concepto.setImpuesto(totalImpuesto);
				}

				// Impuestos Retenidos
				if (item.getImpuestos().getRetenciones() != null && ! item.getImpuestos().getRetenciones().getRetencion().isEmpty()) {
					totalImpuesto = 0;
					for (Retencion imp : item.getImpuestos().getRetenciones().getRetencion()) {
						concepto.getRetenciones().put(imp.getImpuesto(), imp.getTasaOCuota().doubleValue());
						totalImpuesto += imp.getImporte().doubleValue();
					}
					concepto.setRetencion(totalImpuesto);
				}
			}

			// Añado el concepto
			conceptos = (conceptos != null ? conceptos : new ArrayList<FacturaConcepto>());
			conceptos.add(concepto);
		}
		
		return conceptos;
	}
	
	private String agregaExpresion(String expresion, PagosGastosDet item, String tipoComprobante) {
		double total = 0;
		
		if (expresion.contains(item.getIdPagosGastos().getId().toString()))
			return expresion;
		
		total = item.getTotal();
		if (total < 0)
			total = total * -1;
		if (! "".equals(expresion))
			expresion += ",";
		expresion += item.getIdPagosGastos().getTipo() + "|" + item.getIdPagosGastos().getId() + "|" + total;
		if (tipoComprobante != null && ! "".equals(tipoComprobante.trim()))
			expresion += "|" + tipoComprobante;
		
		return expresion;
	}
	
	private String validaTipoComprobante(String tipoComprobante) {
		if (tipoComprobante == null || "".equals(tipoComprobante.trim()))
			return "";
		tipoComprobante = tipoComprobante.trim().toUpperCase();
		if ("INGRESO".equals(tipoComprobante))
			tipoComprobante = "I";
		if ("EGRESO".equals(tipoComprobante))
			tipoComprobante = "E";
		if ("TRASLADO".equals(tipoComprobante))
			tipoComprobante = "T";
		if ("NÓMINA".equals(tipoComprobante))
			tipoComprobante = "N";
		if ("NOMINA".equals(tipoComprobante))
			tipoComprobante = "N";
		if ("PAGO".equals(tipoComprobante))
			tipoComprobante = "P";
		if (tipoComprobante.length() > 1)
			tipoComprobante = tipoComprobante.substring(1, 2);
		return tipoComprobante;
	}
	
	private String validaMetodoPago(String metodoPago) {
		if (metodoPago == null || "".equals(metodoPago.trim()))
			return "";
		metodoPago = metodoPago.trim().toUpperCase();
		if ("PAGO EN UNA SOLA EXHIBICION".equals(metodoPago.trim().toUpperCase()))
			metodoPago = "PUE";
		if ("PAGO EN UNA SOLA EXHIBICIÓN".equals(metodoPago.trim().toUpperCase()))
			metodoPago = "PUE";
		if ("PAGO EN PARCIALIDADES O DIFERIDO".equals(metodoPago.trim().toUpperCase()))
			metodoPago = "PPD";
		return metodoPago;
	}
	
	@SuppressWarnings("unused")
	private String validaFormaPago(String formaPago) {
		if (formaPago == null || "".equals(formaPago.trim()))
			return "";
		formaPago = formaPago.trim().toUpperCase();
		if ("EFECTIVO".equals(formaPago))
			formaPago = "01";
		if ("CHEQUE NOMINATIVO".equals(formaPago))
			formaPago = "02";
		if ("TRANSFERENCIA ELECTRÓNICA DE FONDOS".equals(formaPago))
			formaPago = "03";
		if ("TRANSFERENCIA ELECTRONICA DE FONDOS".equals(formaPago))
			formaPago = "03";
		if ("POR DEFINIR".equals(formaPago))
			formaPago = "99";
		return formaPago;
	}
	
	private String validaTipoPersona(String rfc) {
		if (rfc == null || "".equals(rfc.trim()))
			return "P";
		if ("XAXX010101000".equals(rfc.trim()) || "XEXX010101000".equals(rfc.trim()))
			return "N";
		return (rfc.length() == 12 ? "N" : (rfc.length() == 13 ? "P" : "X"));
	}
	
	private String validate2String(Object target) {
		if (target != null && ! "".equals(target.toString().trim()))
			return target.toString().trim();
		return "";
	}
	
	private long validate2Long(Object target) {
		String value = validate2String(target);
		if ("".equals(value))
			value = "0";
		return Long.valueOf(value);
	}

	private Long getCodigoEmpresa() {
		return ((this.infoSesion != null) ? this.infoSesion.getEmpresa().getCodigo() : 1L);
	}
	
	private void control(String mensaje) {
		control(mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		log.error(this.infoSesion.getAcceso().getUsuario().getUsuario() + " - " + mensaje, throwable);
	}
}
