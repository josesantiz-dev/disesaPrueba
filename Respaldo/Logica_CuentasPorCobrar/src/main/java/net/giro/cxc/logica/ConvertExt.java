package net.giro.cxc.logica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraExt;
import net.giro.adp.logica.ObraRem;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.dao.OrdenCompraDAO;
import net.giro.cxc.beans.ConceptoFacturacion;
import net.giro.cxc.beans.ConceptoFacturacionImpuestos;
import net.giro.cxc.beans.ConceptoFacturacionImpuestosExt;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.cxc.beans.Provisiones;
import net.giro.cxc.beans.ProvisionesDetalle;
import net.giro.cxc.beans.ProvisionesDetalleExt;
import net.giro.cxc.dao.ConceptoFacturacionDAO;
import net.giro.cxc.dao.ProvisionesDAO;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.tyg.admon.CatBancos;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.CatBancosDAO;
import net.giro.tyg.dao.CtasBancariasDAO;
import net.giro.tyg.dao.FormasPagosDAO;
import net.giro.tyg.dao.MonedaDAO;

public class ConvertExt implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ConvertExt.class);
	
	private InitialContext ctx = null;
	private FacturaRem ifzFacturas;
	private FormasPagosDAO ifzFormasPagos;
	private ObraRem ifzObras;
	private ConValoresDAO ifzConValores;
	private CtasBancariasDAO ifzCtasBancos;
	private CatBancosDAO ifzBancos;
	private ConceptoFacturacionDAO ifzConFac;
	private SucursalesRem ifzSucursales;
	private ProvisionesDAO ifzProvisiones;
	private OrdenCompraDAO ifzOrdenes;
	private MonedaDAO ifzMonedas;
	
	private String from;
	private boolean mostrarSystemOut;
	
	
	public ConvertExt() {
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            
    		this.ctx = new InitialContext(p);

    		this.ifzFacturas 	 = (FacturaRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
    		this.ifzObras		 = (ObraRem)				this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
    		this.ifzSucursales	 = (SucursalesRem)			this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
    		this.ifzOrdenes		 = (OrdenCompraDAO) 		this.ctx.lookup("ejb:/Model_Compras//OrdenCompraImp!net.giro.compras.dao.OrdenCompraDAO");
    		this.ifzConFac 		 = (ConceptoFacturacionDAO) this.ctx.lookup("ejb:/Model_CuentasPorCobrar//ConceptoFacturacionImp!net.giro.cxc.dao.ConceptoFacturacionDAO");
    		this.ifzProvisiones  = (ProvisionesDAO)			this.ctx.lookup("ejb:/Model_CuentasPorCobrar//ProvisionesImp!net.giro.cxc.dao.ProvisionesDAO");
    		this.ifzConValores 	 = (ConValoresDAO) 			this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
    		this.ifzFormasPagos  = (FormasPagosDAO) 		this.ctx.lookup("ejb:/Model_TYG//FormasPagosImp!net.giro.tyg.dao.FormasPagosDAO");
    		this.ifzCtasBancos 	 = (CtasBancariasDAO) 		this.ctx.lookup("ejb:/Model_TYG//CtasBancariasImp!net.giro.tyg.dao.CtasBancariasDAO");
    		this.ifzBancos 		 = (CatBancosDAO) 			this.ctx.lookup("ejb:/Model_TYG//CatBancosImp!net.giro.tyg.dao.CatBancosDAO");
    		this.ifzMonedas 	 = (MonedaDAO) 				this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
    		
    		this.setFrom("Default");
    		this.setMostrarSystemOut(false);
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear ConvertExt", e);
			this.ctx = null;
		}
	}
	
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public boolean getMostrarSystemOut() {
		return mostrarSystemOut;
	}

	public void setMostrarSystemOut(boolean mostrarSystemOut) {
		this.mostrarSystemOut = mostrarSystemOut;
	}
	
	public Factura FacturaExtToFactura(FacturaExt pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: FacturaExt To Factura");
		Factura pojoResult = new Factura();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setFolioFactura(pojoTarget.getFolioFactura());
			pojoResult.setFechaEmision(pojoTarget.getFechaEmision());
			pojoResult.setFechaVencimiento(pojoTarget.getFechaVencimiento());
			pojoResult.setSubtotal(pojoTarget.getSubtotal());
			pojoResult.setImpuestos(pojoTarget.getImpuestos());
			pojoResult.setRetenciones(pojoTarget.getRetenciones());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setNombreSucursal(pojoTarget.getNombreSucursal());
			pojoResult.setIdCliente(pojoTarget.getIdCliente());
			pojoResult.setCliente(pojoTarget.getCliente());
			pojoResult.setTipoCliente(pojoTarget.getTipoCliente());
			pojoResult.setCliente(pojoTarget.getCliente());
			pojoResult.setDomicilio(pojoTarget.getDomicilio());
			pojoResult.setCiudad(pojoTarget.getCiudad());
			pojoResult.setRfc(pojoTarget.getRfc());
			pojoResult.setRetensionIva(pojoTarget.getRetensionIva());
			pojoResult.setRetensionIsr(pojoTarget.getRetensionIsr());
			pojoResult.setTotal(new BigDecimal(pojoTarget.getTotal()));
			pojoResult.setSaldo(pojoTarget.getSaldo());
			pojoResult.setFechaCancelacion(pojoTarget.getFechaCancelacion());
			pojoResult.setCanceladoPor(pojoTarget.getCanceladoPor());
			pojoResult.setIdFormaPago(pojoTarget.getIdFormaPago());
			pojoResult.setSerie(pojoTarget.getSerie());
			pojoResult.setTipoComprobante(pojoTarget.getTipoComprobante());
			pojoResult.setMunicipio(pojoTarget.getMunicipio());
			pojoResult.setEstado(pojoTarget.getEstado());
			pojoResult.setPais(pojoTarget.getPais());
			pojoResult.setNocertificado(pojoTarget.getNocertificado());
			pojoResult.setFolio(pojoTarget.getFolio());
			pojoResult.setIdFolio(pojoTarget.getIdFolio());
			pojoResult.setIdCertificado(pojoTarget.getIdCertificado());
			pojoResult.setColonia(pojoTarget.getColonia());
			pojoResult.setCp(pojoTarget.getCp());
			pojoResult.setNoExterno(pojoTarget.getNoExterno());
			pojoResult.setNoInterno(pojoTarget.getNoInterno());
			pojoResult.setXml(pojoTarget.getXml());
			pojoResult.setCadenaOriginal(pojoTarget.getCadenaOriginal());
			pojoResult.setSello(pojoTarget.getSello());
			pojoResult.setTasaIva(pojoTarget.getTasaIva());
			pojoResult.setDescuento(pojoTarget.getDescuento());
			pojoResult.setMotivoDescuento(pojoTarget.getMotivoDescuento());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setIdMetodoPago(pojoTarget.getIdMetodoPago());
			pojoResult.setCuenta(pojoTarget.getCuenta());
			pojoResult.setCfdi(pojoTarget.getCfdi());
			pojoResult.setTimbre(pojoTarget.getTimbre());
			pojoResult.setState(pojoTarget.getState());
			pojoResult.setDescripcion(pojoTarget.getDescripcion());
			pojoResult.setEstadoCancelacion(pojoTarget.getEstadoCancelacion());
			pojoResult.setMensajeCancelacion(pojoTarget.getMensajeCancelacion());
			pojoResult.setAcuseCancelacion(pojoTarget.getAcuseCancelacion());
			pojoResult.setUuid(pojoTarget.getUuid());
			pojoResult.setFechaTimbrado(pojoTarget.getFechaTimbrado());
			pojoResult.setSelloSat(pojoTarget.getSelloSat());
			pojoResult.setNoCertificadoSat(pojoTarget.getNoCertificadoSat());
			pojoResult.setIdEmpresa(1L);
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setDescripcionMoneda(pojoTarget.getDescMoneda());
			pojoResult.setProvisionada(pojoTarget.getProvisionada());
			pojoResult.setProvisionadaPor(pojoTarget.getProvisionadaPor());
			pojoResult.setFechaProvisionada(pojoTarget.getFechaProvisionada());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0) {
				pojoResult.setIdObra(pojoTarget.getIdObra().getId());
				pojoResult.setNombreObra(pojoTarget.getIdObra().getNombre());
				pojoResult.setTipoObra(pojoTarget.getIdObra().getTipoObra());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Sucursal");
			if (pojoTarget.getIdSucursal() != null && pojoTarget.getIdSucursal().getId() > 0) {
				pojoResult.setIdSucursal(pojoTarget.getIdSucursal().getId());
				pojoResult.setNombreSucursal(pojoTarget.getIdSucursal().getSucursal());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Sucursal terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Moneda");
			if (pojoTarget.getIdMoneda() != null && pojoTarget.getIdMoneda().getId() > 0) {
				pojoResult.setIdMoneda(pojoTarget.getIdMoneda().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Moneda terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Empresa");
			if (pojoTarget.getIdEmpresa() != null && pojoTarget.getIdEmpresa().getId() > 0) {
				pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Empresa terminado");
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public FacturaExt FacturaToFacturaExt(Factura pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: Factura To FacturaExt");
		FacturaExt pojoResult = new FacturaExt();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");

			pojoResult.setId(pojoTarget.getId());
			pojoResult.setFolioFactura(pojoTarget.getFolioFactura());
			pojoResult.setFechaEmision(pojoTarget.getFechaEmision());
			pojoResult.setFechaVencimiento(pojoTarget.getFechaVencimiento());
			pojoResult.setSubtotal(pojoTarget.getSubtotal());
			pojoResult.setImpuestos(pojoTarget.getImpuestos());
			pojoResult.setRetenciones(pojoTarget.getRetenciones());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setNombreSucursal(pojoTarget.getNombreSucursal());
			pojoResult.setIdCliente(pojoTarget.getIdCliente());
			pojoResult.setCliente(pojoTarget.getCliente());
			pojoResult.setTipoCliente(pojoTarget.getTipoCliente());
			pojoResult.setDomicilio(pojoTarget.getDomicilio());
			pojoResult.setCiudad(pojoTarget.getCiudad());
			pojoResult.setRfc(pojoTarget.getRfc());
			pojoResult.setRetensionIva(pojoTarget.getRetensionIva());
			pojoResult.setRetensionIsr(pojoTarget.getRetensionIsr());
			pojoResult.setSaldo(pojoTarget.getSaldo());
			pojoResult.setFechaCancelacion(pojoTarget.getFechaCancelacion());
			pojoResult.setCanceladoPor(pojoTarget.getCanceladoPor());
			pojoResult.setIdFormaPago(pojoTarget.getIdFormaPago());
			pojoResult.setSerie(pojoTarget.getSerie());
			pojoResult.setTipoComprobante(pojoTarget.getTipoComprobante());
			pojoResult.setMunicipio(pojoTarget.getMunicipio());
			pojoResult.setEstado(pojoTarget.getEstado());
			pojoResult.setPais(pojoTarget.getPais());
			pojoResult.setNocertificado(pojoTarget.getNocertificado());
			pojoResult.setFolio(pojoTarget.getFolio());
			pojoResult.setIdFolio(pojoTarget.getIdFolio());
			pojoResult.setIdCertificado(pojoTarget.getIdCertificado());
			pojoResult.setColonia(pojoTarget.getColonia());
			pojoResult.setCp(pojoTarget.getCp());
			pojoResult.setNoExterno(pojoTarget.getNoExterno());
			pojoResult.setNoInterno(pojoTarget.getNoInterno());
			pojoResult.setXml(pojoTarget.getXml());
			pojoResult.setCadenaOriginal(pojoTarget.getCadenaOriginal());
			pojoResult.setSello(pojoTarget.getSello());
			pojoResult.setTasaIva(pojoTarget.getTasaIva());
			pojoResult.setDescuento(pojoTarget.getDescuento());
			pojoResult.setMotivoDescuento(pojoTarget.getMotivoDescuento());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setIdMetodoPago(pojoTarget.getIdMetodoPago());
			pojoResult.setCuenta(pojoTarget.getCuenta());
			pojoResult.setCfdi(pojoTarget.getCfdi());
			pojoResult.setTimbre(pojoTarget.getTimbre());
			pojoResult.setState(pojoTarget.getState());
			pojoResult.setDescripcion(pojoTarget.getDescripcion());
			pojoResult.setEstadoCancelacion(pojoTarget.getEstadoCancelacion());
			pojoResult.setMensajeCancelacion(pojoTarget.getMensajeCancelacion());
			pojoResult.setAcuseCancelacion(pojoTarget.getAcuseCancelacion());
			pojoResult.setUuid(pojoTarget.getUuid());
			pojoResult.setFechaTimbrado(pojoTarget.getFechaTimbrado());
			pojoResult.setSelloSat(pojoTarget.getSelloSat());
			pojoResult.setNoCertificadoSat(pojoTarget.getNoCertificadoSat());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setDescMoneda(pojoTarget.getDescripcionMoneda());
			pojoResult.setProvisionada(pojoTarget.getProvisionada());
			pojoResult.setProvisionadaPor(pojoTarget.getProvisionadaPor());
			pojoResult.setFechaProvisionada(pojoTarget.getFechaProvisionada());
			pojoResult.setAbreviaturaMoneda("");
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra");
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra() > 0) {
				ObraExt pojoObra = this.ifzObras.findByIdExt(pojoTarget.getIdObra());
				
				if (pojoObra != null) {
					pojoResult.setIdObra(pojoObra);
					// Comprobamos si devemos actualizar el nombre de la obra
					if (!pojoObra.getNombre().equals(pojoTarget.getNombreObra()))
						pojoResult.setActualizarObra(true);
				} else {
					pojoObra = new ObraExt();
					pojoObra.setId(pojoTarget.getIdObra());
					pojoObra.setNombre(pojoTarget.getNombreObra());
					
					pojoResult.setIdObra(pojoObra);
					pojoResult.setActualizarObra(true);
				}
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Obra terminado");
			} 
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Sucursal");
			if (pojoTarget.getIdSucursal() != null && pojoTarget.getIdSucursal() > 0) {
				Sucursal pojoAux = this.ifzSucursales.findById(pojoTarget.getIdSucursal());
				if (pojoAux == null) pojoAux = new Sucursal();
				pojoResult.setIdSucursal(pojoAux);
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Sucursal terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Moneda");
			if (pojoTarget.getIdMoneda() != null && pojoTarget.getIdMoneda() > 0) {
				Moneda pojoAux = this.ifzMonedas.findById(pojoTarget.getIdMoneda());
				if (pojoAux == null) pojoAux = new Moneda();
				pojoResult.setIdMoneda(pojoAux);
				pojoResult.setAbreviaturaMoneda(pojoAux.getAbreviacion());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Moneda terminado");
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Empresa");
			if (pojoTarget.getIdEmpresa() != null && pojoTarget.getIdEmpresa() > 0) {
				//pojoResult.setIdEmpresa(pojoTarget.getIdEmpresa());
				if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Empresa terminado");
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public FacturaDetalle FacturaDetalleExtToFacturaDetalle(FacturaDetalleExt pojoTarget){
		FacturaDetalle pojoResult = new FacturaDetalle();
		
		try {
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setClaveSat(pojoTarget.getClaveSat());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCosto(pojoTarget.getCosto());
			pojoResult.setTotal(pojoTarget.getTotal());
			pojoResult.setPorcentajeIva(pojoTarget.getPorcentajeIva());
			pojoResult.setPorcentajeRetencion(pojoTarget.getPorcentajeRetencion());
			pojoResult.setImpuestos(pojoTarget.getImpuestos());
			pojoResult.setRetenciones(pojoTarget.getRetenciones());
			pojoResult.setDescripcionConcepto(pojoTarget.getDescripcionConcepto());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			
			if (pojoTarget.getIdFactura() != null && pojoTarget.getIdFactura().getId() != null && pojoTarget.getIdFactura().getId() > 0L)
				pojoResult.setIdFactura(pojoTarget.getIdFactura().getId());
			
			if (pojoTarget.getIdConcepto() != null && pojoTarget.getIdConcepto().getId() != null && pojoTarget.getIdConcepto().getId() > 0L)
				pojoResult.setIdConcepto(pojoTarget.getIdConcepto().getId());
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public FacturaDetalleExt FacturaDetalleToFacturaDetalleExt(FacturaDetalle pojoTarget) throws Exception{
		FacturaDetalleExt pojoResult = new FacturaDetalleExt();
		
		try {
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setClaveSat(pojoTarget.getClaveSat());
			pojoResult.setCantidad(pojoTarget.getCantidad());
			pojoResult.setCosto(pojoTarget.getCosto());
			pojoResult.setPorcentajeIva(pojoTarget.getPorcentajeIva());
			pojoResult.setPorcentajeRetencion(pojoTarget.getPorcentajeRetencion());
			pojoResult.setImpuestos(pojoTarget.getImpuestos());
			pojoResult.setRetenciones(pojoTarget.getRetenciones());
			pojoResult.setDescripcionConcepto(pojoTarget.getDescripcionConcepto());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			
			if (pojoTarget.getIdFactura() != null && pojoTarget.getIdFactura() > 0L) {
				Factura pojoAux = this.ifzFacturas.findById(pojoTarget.getIdFactura());
				
				if (pojoAux == null)
					pojoAux = new Factura();
				
				pojoResult.setIdFactura(this.FacturaToFacturaExt(pojoAux));
			}
			
			if (pojoTarget.getIdConcepto() != null && pojoTarget.getIdConcepto() > 0L) {
				ConceptoFacturacion pojoAux = this.ifzConFac.findById(pojoTarget.getIdConcepto());
				
				if (pojoAux == null)
					pojoAux = new ConceptoFacturacion();

				pojoResult.setIdConcepto(pojoAux);
				pojoResult.setClaveSat(pojoAux.getClaveSat());
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public FacturaPagos FacturaPagosExtToFacturaPagos(FacturaPagosExt pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: FacturaPagosExt To FacturaPagos");
		FacturaPagos pojoResult = new FacturaPagos();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setReferenciaFormaPago(pojoTarget.getReferenciaFormaPago());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdBeneficiario(pojoTarget.getIdBeneficiario());
			pojoResult.setBeneficiario(pojoTarget.getBeneficiario());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Factura");
			if (pojoTarget.getIdFactura() != null && pojoTarget.getIdFactura().getId() != null && pojoTarget.getIdFactura().getId() > 0) {
				pojoResult.setIdFactura(this.FacturaExtToFactura(pojoTarget.getIdFactura()));
				/*Factura pojoFactura = this.FacturaExtToFactura(pojoTarget.getIdFactura());
				pojoResult.setIdFactura(pojoFactura.getId());*/
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando FormaPago");
			if (pojoTarget.getIdFormaPago() != null && pojoTarget.getIdFormaPago().getId() > 0) {
				pojoResult.setIdFormaPago(pojoTarget.getIdFormaPago().getId());
			}

			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando BancoDeposito");
			if (pojoTarget.getIdCuentaDeposito() != null && pojoTarget.getIdCuentaDeposito().getId() > 0) {
				pojoResult.setIdCuentaDeposito(pojoTarget.getIdCuentaDeposito().getId());
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando BancoOrigen");
			if (pojoTarget.getIdBancoOrigen() != null && pojoTarget.getIdBancoOrigen().getId() > 0) {
				pojoResult.setIdBancoOrigen(pojoTarget.getIdBancoOrigen().getId());
			}
		} catch (Exception ex) {
			log.error("ConvertExt :: ERROR al convertir FacturaPagosExt To FacturaPagos :: " + ex.getMessage());
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: FacturaPagosExt To FacturaPagos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public FacturaPagosExt FacturaPagosToFacturaPagosExt(FacturaPagos pojoTarget) throws Exception {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: FacturaPagos To FacturaPagosExt");
		FacturaPagosExt pojoResult = new FacturaPagosExt();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setReferenciaFormaPago(pojoTarget.getReferenciaFormaPago());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setIdBeneficiario(pojoTarget.getIdBeneficiario());
			pojoResult.setBeneficiario(pojoTarget.getBeneficiario());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Factura");
			if (pojoTarget.getIdFactura() != null && pojoTarget.getIdFactura().getId() != null && pojoTarget.getIdFactura().getId() > 0) {
				pojoResult.setIdFactura(this.FacturaToFacturaExt(pojoTarget.getIdFactura()));
				/*Factura pojoFactura = this.ifzFacturas.findById(pojoTarget.getIdFactura());
				
				if (pojoFactura == null)
					pojoFactura = new Factura();
				
				pojoResult.setIdFactura(this.FacturaToFacturaExt(pojoFactura));*/
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando FormaPago");
			if (pojoTarget.getIdFormaPago() != null && pojoTarget.getIdFormaPago() > 0) {
				FormasPagos pojoFormaPago = this.ifzFormasPagos.findById(pojoTarget.getIdFormaPago());
				pojoResult.setIdFormaPago(pojoFormaPago);
			}

			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando BancoDeposito");
			if (pojoTarget.getIdCuentaDeposito() != null && pojoTarget.getIdCuentaDeposito() > 0) {
				CtasBanco pojoCuentaBancaria = this.ifzCtasBancos.findById(pojoTarget.getIdCuentaDeposito());
				pojoResult.setIdCuentaDeposito(pojoCuentaBancaria);
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando BancoOrigen");
			if (pojoTarget.getIdBancoOrigen() != null && pojoTarget.getIdBancoOrigen() > 0) {
				CatBancos pojoInstitucionBancaria = this.ifzBancos.findById(pojoTarget.getIdBancoOrigen());
				pojoResult.setIdBancoOrigen(pojoInstitucionBancaria);
			}
		} catch (Exception ex) {
			log.error("ConvertExt :: ERROR al convertir FacturaPagos To FacturaPagosExt :: " + ex.getMessage());
			throw ex;
		}
		
		return pojoResult;
	}
	
	public ConceptoFacturacionImpuestos ConceptoFacturacionImpuestosExtToConceptoFacturacionImpuestos(ConceptoFacturacionImpuestosExt pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: ConceptoFacturacionExt To ConceptoFacturacion");
		ConceptoFacturacionImpuestos pojoResult = new ConceptoFacturacionImpuestos();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando ConceptoFacturacion");
			if (pojoTarget.getIdConceptoFacturacion() != null && pojoTarget.getIdConceptoFacturacion().getId() != null && pojoTarget.getIdConceptoFacturacion().getId() > 0L) {
				pojoResult.setIdConceptoFacturacion(pojoTarget.getIdConceptoFacturacion().getId());
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Impuesto (ConValores)");
			if (pojoTarget.getIdImpuesto() != null && pojoTarget.getIdImpuesto().getId() > 0L) {
				pojoResult.setIdImpuesto(pojoTarget.getIdImpuesto().getId());
			}
		} catch (Exception ex) {
			log.error("ConvertExt :: ERROR al convertir ConceptoFacturacionExt To ConceptoFacturacion :: " + ex.getMessage());
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: ConceptoFacturacionExt To ConceptoFacturacion :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ConceptoFacturacionImpuestosExt ConceptoFacturacionImpuestosToConceptoFacturacionImpuestosExt(ConceptoFacturacionImpuestos pojoTarget) {
		if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: ConceptoFacturacionImpuestos To ConceptoFacturacionImpuestosExt");
		ConceptoFacturacionImpuestosExt pojoResult = new ConceptoFacturacionImpuestosExt();
		
		try {
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando ConceptoFacturacion");
			if (pojoTarget.getIdConceptoFacturacion() != null && pojoTarget.getIdConceptoFacturacion() != null && pojoTarget.getIdConceptoFacturacion() > 0L) {
				ConceptoFacturacion pojoAux = this.ifzConFac.findById(pojoTarget.getIdConceptoFacturacion());
				pojoResult.setIdConceptoFacturacion(pojoAux);
			}
			
			if (this.mostrarSystemOut) log.info("---- -- ----> Comprobando Impuesto (ConValores)");
			if (pojoTarget.getIdImpuesto() != null && pojoTarget.getIdImpuesto() != null && pojoTarget.getIdImpuesto() > 0L) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getIdImpuesto());
				pojoResult.setIdImpuesto(pojoAux);
			}
		} catch (Exception ex) {
			log.error("ConvertExt :: ERROR al convertir ConceptoFacturacionImpuestos To ConceptoFacturacionImpuestosExt :: " + ex.getMessage());
			throw ex;
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: ConceptoFacturacionImpuestos To ConceptoFacturacionImpuestosExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ProvisionesDetalle ProvisionesDetalleExtToProvisionesDetalle(ProvisionesDetalleExt pojoTarget) {
		ProvisionesDetalle pojoResult = new ProvisionesDetalle();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: ProvisionesDetalleExt To ProvisionesDetalle");
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdTipoGasto(pojoTarget.getIdTipoGasto());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setMontoRevisado(pojoTarget.getMontoRevisado());
			pojoResult.setMontoOriginal(pojoTarget.getMontoOriginal());
			pojoResult.setIdBeneficiario(pojoTarget.getIdBeneficiario());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setIdProvision(0L);
			pojoResult.setIdFactura(0L);
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando Provision");
			if (pojoTarget.getIdProvision() != null && pojoTarget.getIdProvision().getId() != null && pojoTarget.getIdProvision().getId() > 0) {
				pojoResult.setIdProvision(pojoTarget.getIdProvision().getId());
				if (this.mostrarSystemOut) log.info("---- -- ----> Provision Obra terminado");
			}
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando Factura");
			if (pojoTarget.getIdFactura() != null && pojoTarget.getIdFactura().getId() > 0L) {
				pojoResult.setIdFactura(pojoTarget.getIdFactura().getId());
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando Factura terminado");
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
	
	public ProvisionesDetalleExt ProvisionesDetalleToProvisionesDetalleExt(ProvisionesDetalle pojoTarget) throws Exception {
		ProvisionesDetalleExt pojoResult = new ProvisionesDetalleExt();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: ProvisionesDetalleExt To ProvisionesDetalle");
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdTipoGasto(pojoTarget.getIdTipoGasto());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setMontoRevisado(pojoTarget.getMontoRevisado());
			pojoResult.setMontoOriginal(pojoTarget.getMontoOriginal());
			pojoResult.setIdBeneficiario(pojoTarget.getIdBeneficiario());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setIdProvision(null);
			pojoResult.setIdFactura(null);
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando de tipos nativos terminado");
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando Provision");
			if (pojoTarget.getIdProvision() != null && pojoTarget.getIdProvision() > 0) {
				Provisiones pojoProvision = this.ifzProvisiones.findById(pojoTarget.getIdProvision());
				if (pojoProvision == null)
					pojoProvision = new Provisiones();
				pojoResult.setIdProvision(pojoProvision);
				if (this.mostrarSystemOut) log.info("---- -- ----> Provision Obra terminado");
			}
			
			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando Factura");
			if (pojoTarget.getIdFactura() != null && pojoTarget.getIdFactura() > 0) {
				Factura pojoAux = this.ifzFacturas.findById(pojoTarget.getIdFactura());
				if (pojoAux == null)
					pojoAux = new Factura();
				FacturaExt pojoFactura = this.ifzFacturas.convertir(pojoAux);
				pojoResult.setIdFactura(pojoFactura);
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando Factura terminado");
			}

			if (this.mostrarSystemOut) 
				log.info("---- -- ----> Comprobando Orden de Compra");
			if (pojoTarget.getIdOrdenCompra() != null && pojoTarget.getIdOrdenCompra() > 0L) {
				OrdenCompra pojoOrden = this.ifzOrdenes.findById(pojoTarget.getIdOrdenCompra());
				if (pojoOrden == null)
					pojoOrden = new OrdenCompra();
				pojoResult.setFolioOrdenCompra(pojoOrden.getFolio());
				if (this.mostrarSystemOut) 
					log.info("---- -- ----> Comprobando Orden de Compra terminado");
			}
		} catch (Exception e) {
			throw e;
		}
		
		return pojoResult;
	}
}
