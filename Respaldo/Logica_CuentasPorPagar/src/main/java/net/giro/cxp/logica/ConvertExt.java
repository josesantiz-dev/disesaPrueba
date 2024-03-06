package net.giro.cxp.logica;


import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.dao.ObraDAO;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.dao.NegocioDAO;
import net.giro.clientes.dao.PersonaDAO;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.cxp.beans.CatBancosExt;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.Factura;
import net.giro.cxp.beans.FacturaExt;
import net.giro.cxp.beans.GastosExt;
import net.giro.cxp.beans.GastosImpuesto;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.beans.MonedaTYGExt;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestos;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.cxp.beans.ProgPagos;
import net.giro.cxp.beans.ProgPagosDetalle;
import net.giro.cxp.beans.ProgPagosDetalleExt;
import net.giro.cxp.beans.ProgPagosExt;
import net.giro.cxp.beans.SucursalBancariaExt;
import net.giro.tyg.admon.MovimientosCuentas;
import net.giro.cxp.beans.MovimientosCuentasExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.SucursalExt;
import net.giro.cxp.dao.PagosGastosDAO;
import net.giro.cxp.dao.PagosGastosDetDAO;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.ne.beans.Empresa;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.dao.EmpresaDAO;
import net.giro.ne.dao.SucursalDAO;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.dao.ConValoresDAO;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.tyg.admon.CatBancos;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.SucursalBancaria;
import net.giro.tyg.dao.CatBancosDAO;
import net.giro.tyg.dao.CtasBancariasDAO;
import net.giro.tyg.dao.SucursalBancariaDAO;

public class ConvertExt {
	private static Logger log = Logger.getLogger(ConvertExt.class);
	
	private InitialContext ctx = null;
	private PagosGastosDAO ifzPagosGastos;
	private PagosGastosDetDAO ifzPagosGastosDet;
	//private ProgPagosDAO ifzProgPagos;
	private ProductoRem ifzProductos;
	private SucursalBancariaDAO ifzSucursalesBancarias;
	private CtasBancariasDAO ifzCtasBancos;
	private ConValoresDAO ifzConValores;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	private CatBancosDAO ifzBancos;
	private SucursalDAO ifzSucursales;
	private PersonaDAO ifzPersonas;
	private NegocioDAO ifzNegocios;
	private EmpresaDAO ifzEmpresas;
	private ObraDAO ifzObras;
	private OrdenCompraRem ifzOC;
	private String from;
	private boolean mostrarSystemOut;
	
	
	public ConvertExt() {
		try {
            this.ctx = new InitialContext();
    		this.ifzPersonas 			= (PersonaDAO) 			this.ctx.lookup("ejb:/Model_Clientes//PersonaImp!net.giro.clientes.dao.PersonaDAO");
    		this.ifzNegocios 			= (NegocioDAO) 			this.ctx.lookup("ejb:/Model_Clientes//NegocioImp!net.giro.clientes.dao.NegocioDAO");
    		this.ifzPagosGastos 		= (PagosGastosDAO) 		this.ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosImp!net.giro.cxp.dao.PagosGastosDAO");
    		this.ifzPagosGastosDet 		= (PagosGastosDetDAO) 	this.ctx.lookup("ejb:/Model_CuentasPorPagar//PagosGastosDetImp!net.giro.cxp.dao.PagosGastosDetDAO");
    		this.ifzObras 				= (ObraDAO) 			this.ctx.lookup("ejb:/Model_GestionProyectos//ObraImp!net.giro.adp.dao.ObraDAO");
    		this.ifzColonia 			= (ColoniaDAO) 			this.ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");
    		this.ifzConValores 			= (ConValoresDAO) 		this.ctx.lookup("ejb:/Model_Publico//ConValoresImp!net.giro.plataforma.dao.ConValoresDAO");
    		this.ifzLocalidad 			= (LocalidadDAO) 		this.ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
    		this.ifzEmpresas 			= (EmpresaDAO) 			this.ctx.lookup("ejb:/Model_Publico//EmpresaImp!net.giro.ne.dao.EmpresaDAO");
    		this.ifzSucursales 			= (SucursalDAO) 		this.ctx.lookup("ejb:/Model_Publico//SucursalImp!net.giro.ne.dao.SucursalDAO");
    		this.ifzBancos 				= (CatBancosDAO) 		this.ctx.lookup("ejb:/Model_TYG//CatBancosImp!net.giro.tyg.dao.CatBancosDAO");
    		this.ifzCtasBancos 			= (CtasBancariasDAO) 	this.ctx.lookup("ejb:/Model_TYG//CtasBancariasImp!net.giro.tyg.dao.CtasBancariasDAO");
    		this.ifzSucursalesBancarias = (SucursalBancariaDAO) this.ctx.lookup("ejb:/Model_TYG//SucursalBancariaImp!net.giro.tyg.dao.SucursalBancariaDAO");
    		this.ifzOC					= (OrdenCompraRem)		this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
    		this.ifzProductos 			= (ProductoRem) 		this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			
    		this.from = "Default";
    		this.mostrarSystemOut = false;
		} catch (Exception e) {
			log.error("Error en el metodo contexto, no se pudo crear Logica_CuentasPorPagar.ConvertExt", e);
			ctx = null;
		}
	}
	
	
	public boolean getMostrarSystemOut() {
		return this.mostrarSystemOut;
	}
	
	public void setMostrarSystemOut(boolean value) {
		this.mostrarSystemOut = value;
	}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	public GastosExt GastosExtToGastos(ConValores pojoTarget) throws Exception {
		GastosExt pojoResult = new GastosExt();
		
		try {
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setDescripcion(pojoTarget.getDescripcion());
			pojoResult.setIdGrupo(pojoTarget.getGrupoValorId());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			
			if (pojoTarget.getValor() != null && ! "".equals(pojoTarget.getValor().trim())) {
				Producto pojoProducto = this.ifzProductos.findById(Long.valueOf(pojoTarget.getValor().trim()));
				if (pojoProducto != null) {
					pojoResult.setIdProducto(pojoProducto.getId());
					pojoResult.setClaveProducto(pojoProducto.getClave());
				}
			}
			
			if (pojoTarget.getAtributo1() != null && ! "".equals(pojoTarget.getAtributo1().trim())) {
				ConValores pojoTipoEgreso = this.ifzConValores.findById(Long.valueOf(pojoTarget.getAtributo1().trim()));
				if (pojoTipoEgreso != null)
					pojoResult.setIdTipoEgreso(pojoTipoEgreso);
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.GastosExtToGastos", e);
		}
		
		return pojoResult;
	}

	public ConValores GastosToGastosExt(GastosExt pojoTarget) throws Exception {
		ConValores pojoResult = new ConValores();
		
		try {
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setDescripcion(pojoTarget.getDescripcion());
			pojoResult.setGrupoValorId(pojoTarget.getIdGrupo());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setValor("");
			pojoResult.setAtributo1("");
			
			if (pojoTarget.getIdProducto() > 0L)
				pojoResult.setValor(String.valueOf(pojoTarget.getIdProducto()));
			
			if (pojoTarget.getIdTipoEgreso() != null && pojoTarget.getIdTipoEgreso().getId() > 0L)
				pojoResult.setAtributo1(String.valueOf(pojoTarget.getIdTipoEgreso().getId()));
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.GastosToGastosExt", e);
		}
		
		return pojoResult;
	}
	
	public CatBancosExt CatBancosToCatBancosExt(CatBancos pojoTarget) throws Exception{
		CatBancosExt pojoResult = new CatBancosExt();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: CatBancosToCatBancosExt");
			pojoResult.setId(pojoTarget.getId());
			
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			
			pojoResult.setNombreCorto(pojoTarget.getNombreCorto());
			pojoResult.setNombreLargo(pojoTarget.getNombreLargo());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.CatBancosToCatBancosExt", e);
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: CatBancos To CatBancosExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public CtasBancoExt CtasBancoToCtasBancoExt(CtasBanco pojoTarget) throws Exception{
		CtasBancoExt pojoResult = new CtasBancoExt();
		
		try{
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: CtasBancoToCtasBancoExt");
			pojoResult.setId(pojoTarget.getId());			
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());			
			pojoResult.setNumeroDeCuenta(pojoTarget.getNumeroDeCuenta());
			pojoResult.setClave(pojoTarget.getClave());
			pojoResult.setSaldo(pojoTarget.getSaldo());
			pojoResult.setSaldoMinimo(pojoTarget.getSaldoMinimo());
			pojoResult.setValidaReferencia(pojoTarget.getValidaReferencia() > 0 ? true : false);
			pojoResult.setValidaSaldo(pojoTarget.getValidaSaldo() > 0 ? true : false);
			
			if(pojoTarget.getSucursalBancaria() != null)
				pojoResult.setSucursalBancaria(SucursalBancariaToSucursalBancaraExt(ifzSucursalesBancarias.findById(pojoTarget.getSucursalBancaria().getId())));
			
			if(pojoTarget.getEmpresa() != null)
				pojoResult.setEmpresa(ifzEmpresas.findById(pojoTarget.getId()));
			//pojoCtasBancoExt.setEmpresa(pojoCtasBanco.getEmpresa());
			
			if(pojoTarget.getInstitucionBancaria() != null)
				pojoResult.setInstitucionBancaria(CatBancosToCatBancosExt(pojoTarget.getInstitucionBancaria()));
			if(pojoTarget.getSucursalBancaria() != null)
				pojoResult.setSucursalBancaria(SucursalBancariaToSucursalBancaraExt(pojoTarget.getSucursalBancaria()));
			if(pojoTarget.getMoneda() != null)
				pojoResult.setMoneda(MonedaToMonedaTYGExt(pojoTarget.getMoneda()));
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.CtasBancoToCtasBancoExt", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: CtasBanco To CtasBancoExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public FacturaExt FacturaToFacturaExt(Factura pojoTarget) throws Exception{
		FacturaExt pojoResult = new FacturaExt();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: FacturaToFacturaExt");
			pojoResult.setId(pojoTarget.getId());

			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			
			if (pojoTarget.getIdMovimientosCuentas() != null) {
				// TO DO: REvisar este codigo era el anterior de JOSE
				// pojoFacturaExt.setIdMovimientosCuentas(MovimientosCuentasToMovimientosCuentasExt(pojoFactura.getIdMovimientosCuentas()));
				/*MovimientosCuentasExt movimientosCuentasExt = new MovimientosCuentasExt(); 
				movimientosCuentasExt.setId(pojoFactura.getIdMovimientosCuentas());
				pojoFacturaExt.setIdMovimientosCuentas(movimientosCuentasExt);*/
				
				PagosGastos movimientosCuentasExt = new PagosGastos(); 
				movimientosCuentasExt.setId(pojoTarget.getIdMovimientosCuentas());
				pojoResult.setIdMovimientosCuentas(movimientosCuentasExt);
			}
			
			if (pojoTarget.getIdProveedor() != null && pojoTarget.getIdProveedor() > 0L) {
				Persona pojoPersona = ifzPersonas.findById(pojoTarget.getIdProveedor());
				pojoResult.setIdProveedor(PersonaToPersonaExt(pojoPersona));
			}
			
			pojoResult.setReferencia(pojoTarget.getReferencia());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setSubtotal(pojoTarget.getSubtotal());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			
			if (pojoTarget.getIdConcepto() != null && pojoTarget.getIdConcepto() > 0L) {
				ConValores pojoValor = ifzConValores.findById(pojoTarget.getIdConcepto());
				pojoResult.setIdConcepto(pojoValor);
			}
			
			pojoResult.setTotalImpuestos(pojoTarget.getTotalImpuestos());
			pojoResult.setTotalRetenciones(pojoTarget.getTotalRetenciones());
			pojoResult.setEstatus(pojoTarget.getEstatus());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.FacturaToFacturaExt");
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: Factura To FacturaExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public MonedaTYGExt MonedaToMonedaTYGExt(Moneda pojoTarget) throws Exception{
		MonedaTYGExt pojoResult = new MonedaTYGExt();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: MonedaToMonedaTYGExt");
			pojoResult.setId(pojoTarget.getId());
			
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			
			pojoResult.setAbreviacion(pojoTarget.getAbreviacion());
			pojoResult.setNombre(pojoTarget.getNombre());
			pojoResult.setIdShf(pojoTarget.getIdShf());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.MonedaToMonedaTYGExt", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: Moneda To MonedaTYGExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public MovimientosCuentasExt MovimientosCuentasToMovimientosCuentasExt(MovimientosCuentas pojoTarget){
		MovimientosCuentasExt pojoResult = new MovimientosCuentasExt();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: MovimientosCuentasToMovimientosCuentasExt");
			pojoResult.setId(pojoTarget.getId());
			
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			
			if (pojoTarget.getIdCuentaOrigen() != null && pojoTarget.getIdCuentaOrigen() > 0L) {
				CtasBanco pojoCuentaBanco = ifzCtasBancos.findById(pojoTarget.getIdCuentaOrigen());
				pojoResult.setIdCuentaOrigen(CtasBancoToCtasBancoExt(pojoCuentaBanco));
			}
			
			if (pojoTarget.getIdCuentaOrigenTerceros() != null && pojoTarget.getIdCuentaOrigenTerceros() > 0L) {
				Persona pojoPersona = ifzPersonas.findById(pojoTarget.getIdCuentaOrigenTerceros());
				pojoResult.setIdCuentaOrigenTerceros(PersonaToPersonaExt(pojoPersona));
			}
				
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setNoCheque(pojoTarget.getNoCheque());
			pojoResult.setMonto(pojoTarget.getMonto());
			
			if (pojoTarget.getIdSucursal() != null && pojoTarget.getIdSucursal() > 0L) {
				Sucursal pojoSucursal = ifzSucursales.findById(pojoTarget.getIdSucursal());
				pojoResult.setIdSucursal(SucursalToSucursalExt(pojoSucursal));
			}
			
			if (pojoTarget.getIdBeneficiario() != null && pojoTarget.getIdBeneficiario() > 0L) {
				Persona pojoPersona = ifzPersonas.findById(pojoTarget.getIdBeneficiario());
				pojoResult.setIdBeneficiario(PersonaToPersonaExt(pojoPersona));
			}
			
			pojoResult.setOperacion(pojoTarget.getOperacion());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			
			if (pojoTarget.getIdCuentaDestino() != null && pojoTarget.getIdCuentaDestino() > 0L) {
				CtasBanco pojoCuentaBanco = ifzCtasBancos.findById(pojoTarget.getIdCuentaDestino());
				pojoResult.setIdCuentaDestino(CtasBancoToCtasBancoExt(pojoCuentaBanco));
			}
			
			if (pojoTarget.getIdCuentaDestinoTerceros() != null && pojoTarget.getIdCuentaDestinoTerceros() > 0L) {
				Persona pojoPersona = ifzPersonas.findById(pojoTarget.getIdCuentaDestinoTerceros());
				pojoResult.setIdCuentaDestinoTerceros(PersonaToPersonaExt(pojoPersona));
			}
			
			pojoResult.setIdPagosGastosRef(pojoTarget.getIdPagosGastosRef());
			
			if (pojoTarget.getIdTiposMovimiento() != null && pojoTarget.getIdTiposMovimiento() > 0L) {
				ConValores pojoValor = ifzConValores.findById(pojoTarget.getIdTiposMovimiento());
				pojoResult.setIdTiposMovimiento(pojoValor);
			}
			
			pojoResult.setIdPagoMultiple(pojoTarget.getIdPagoMultiple());
			pojoResult.setNota(pojoTarget.getNota());
			pojoResult.setConcepto(pojoTarget.getConcepto());
			pojoResult.setFolioAutorizacion(pojoTarget.getFolioAutorizacion());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setSegmento(pojoTarget.getSegmento());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.MovimientosCuentasToMovimientosCuentasExt", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: MovimientosCuentas To MovimientosCuentasExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public MovimientosCuentas MovimientosCuentasExtToMovimientosCuentas(MovimientosCuentasExt pojoTarget){
		MovimientosCuentas pojoResult = new MovimientosCuentas();
		
		try{
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: MovimientosCuentasExtToMovimientosCuentas");
			pojoResult.setId(pojoTarget.getId());
			
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			
			if (pojoTarget.getIdCuentaOrigen() != null && pojoTarget.getIdCuentaOrigen().getId() > 0L) 
				pojoResult.setIdCuentaOrigen(pojoTarget.getIdCuentaOrigen().getId());
			
			if (pojoTarget.getIdCuentaOrigenTerceros() != null && pojoTarget.getIdCuentaOrigenTerceros().getId() > 0L) 
				pojoResult.setIdCuentaOrigenTerceros(pojoTarget.getIdCuentaOrigenTerceros().getId());
				
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setNoCheque(pojoTarget.getNoCheque());
			pojoResult.setMonto(pojoTarget.getMonto());
			
			if (pojoTarget.getIdSucursal() != null && pojoTarget.getIdSucursal().getId() > 0L)
				pojoResult.setIdSucursal(pojoTarget.getIdSucursal().getId());
			
			if (pojoTarget.getIdBeneficiario() != null && pojoTarget.getIdBeneficiario().getId() > 0L)
				pojoResult.setIdBeneficiario(pojoTarget.getIdBeneficiario().getId());
			
			pojoResult.setOperacion(pojoTarget.getOperacion());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			
			if (pojoTarget.getIdCuentaDestino() != null && pojoTarget.getIdCuentaDestino().getId() > 0L)
				pojoResult.setIdCuentaDestino(pojoTarget.getIdCuentaDestino().getId());
			
			if (pojoTarget.getIdCuentaDestinoTerceros() != null && pojoTarget.getIdCuentaDestinoTerceros().getId() > 0L)
				pojoResult.setIdCuentaDestinoTerceros(pojoTarget.getIdCuentaDestinoTerceros().getId());
			
			pojoResult.setIdPagosGastosRef(pojoTarget.getIdPagosGastosRef());
			
			if (pojoTarget.getIdTiposMovimiento() != null && pojoTarget.getIdTiposMovimiento().getId() > 0L)
				pojoResult.setIdTiposMovimiento(pojoTarget.getIdTiposMovimiento().getId());
			
			pojoResult.setIdPagoMultiple(pojoTarget.getIdPagoMultiple());
			pojoResult.setNota(pojoTarget.getNota());
			pojoResult.setConcepto(pojoTarget.getConcepto());
			pojoResult.setFolioAutorizacion(pojoTarget.getFolioAutorizacion());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setSegmento(pojoTarget.getSegmento());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.MovimientosCuentasExtToMovimientosCuentas", e);
		}
		
		log.info("ConvertExt :: MovimientosCuentasExt To MovimientosCuentas :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public PagosGastos PagosGastosExtToPagosGastos(PagosGastosExt pojoTarget){
		PagosGastos pojoResult = new PagosGastos();

		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: PagosGastosExtToPagosGastos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setNoCheque(pojoTarget.getNoCheque());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setOperacion(pojoTarget.getOperacion());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			pojoResult.setIdPagosGastosRef(pojoTarget.getIdPagosGastosRef());
			pojoResult.setIdPagoMultiple(pojoTarget.getIdPagoMultiple());
			pojoResult.setNota(pojoTarget.getNota());
			pojoResult.setConcepto(pojoTarget.getConcepto());
			pojoResult.setFolioAutorizacion(pojoTarget.getFolioAutorizacion());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setSegmento(pojoTarget.getSegmento());
			pojoResult.setArea(pojoTarget.getArea());
			pojoResult.setConsecutivo(pojoTarget.getConsecutivo());
			pojoResult.setMontoLimite(pojoTarget.getMontoLimite());
			pojoResult.setVoboPor(pojoTarget.getVoboPor());
			pojoResult.setFechaVobo(pojoTarget.getFechaVobo());
			pojoResult.setAutorizadoPor(pojoTarget.getAutorizadoPor());
			pojoResult.setFechaAutorizado(pojoTarget.getFechaAutorizado());
			pojoResult.setNumeroCuentaOrigen(pojoTarget.getNumeroCuentaOrigen());
			pojoResult.setIdObra(0L);
			pojoResult.setIdOrdenCompra(0L);
			
			if (pojoTarget.getIdCuentaOrigen() != null && pojoTarget.getIdCuentaOrigen().getId() > 0L)
				pojoResult.setIdCuentaOrigen(pojoTarget.getIdCuentaOrigen().getId());
			
			if (pojoTarget.getIdCuentaOrigenTerceros() != null && pojoTarget.getIdCuentaOrigenTerceros().getId() > 0L)
				pojoResult.setIdCuentaOrigenTerceros(pojoTarget.getIdCuentaOrigenTerceros().getId());

			if (pojoTarget.getIdSucursal() != null && pojoTarget.getIdSucursal().getId() > 0L)
				pojoResult.setIdSucursal(pojoTarget.getIdSucursal().getId());
			
			if (pojoTarget.getIdBeneficiario() != null && pojoTarget.getIdBeneficiario().getId() > 0L) {
				pojoResult.setIdBeneficiario(pojoTarget.getIdBeneficiario().getId());
				pojoResult.setBeneficiario(pojoTarget.getIdBeneficiario().getNombre());
			}

			if (pojoTarget.getIdCuentaDestino() != null && pojoTarget.getIdCuentaDestino().getId() > 0L)
				pojoResult.setIdCuentaDestino(pojoTarget.getIdCuentaDestino().getId());
			
			if (pojoTarget.getIdCuentaDestinoTerceros() != null && pojoTarget.getIdCuentaDestinoTerceros().getId() > 0L)
				pojoResult.setIdCuentaDestinoTerceros(pojoTarget.getIdCuentaDestinoTerceros().getId());

			if (pojoTarget.getIdTiposMovimiento() != null && pojoTarget.getIdTiposMovimiento().getId() > 0L)
				pojoResult.setIdTiposMovimiento(pojoTarget.getIdTiposMovimiento().getId());

			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0)
				pojoResult.setIdObra(pojoTarget.getIdObra().getId() );
			
			if (pojoTarget.getIdOrdenCompra() > 0L) 
				pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.PagosGastosExtToPagosGastos", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: PagosGastosExt To PagosGastos :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public PagosGastosExt PagosGastosToPagosGastosExt(PagosGastos pojoTarget){
		PagosGastosExt pojoResult = new PagosGastosExt();

		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: PagosGastosToPagosGastosExt");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setNoCheque(pojoTarget.getNoCheque());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setOperacion(pojoTarget.getOperacion());
			pojoResult.setBeneficiario(pojoResult.getBeneficiario());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			pojoResult.setIdPagoMultiple(pojoTarget.getIdPagoMultiple());
			pojoResult.setNota(pojoTarget.getNota());
			pojoResult.setConcepto(pojoTarget.getConcepto());
			pojoResult.setFolioAutorizacion(pojoTarget.getFolioAutorizacion());
			pojoResult.setTipoCambio(pojoTarget.getTipoCambio());
			pojoResult.setSegmento(pojoTarget.getSegmento());
			pojoResult.setIdPagosGastosRef(pojoTarget.getIdPagosGastosRef());
			pojoResult.setArea(pojoTarget.getArea());
			pojoResult.setConsecutivo(pojoTarget.getConsecutivo());
			pojoResult.setMontoLimite(pojoTarget.getMontoLimite());
			pojoResult.setVoboPor(pojoTarget.getVoboPor());
			pojoResult.setFechaVobo(pojoTarget.getFechaVobo());
			pojoResult.setAutorizadoPor(pojoTarget.getAutorizadoPor());
			pojoResult.setFechaAutorizado(pojoTarget.getFechaAutorizado());
			pojoResult.setNumeroCuentaOrigen(pojoTarget.getNumeroCuentaOrigen());
			
			if (pojoTarget.getIdCuentaOrigen() != null && pojoTarget.getIdCuentaOrigen() > 0L){
				CtasBanco pojoAux = ifzCtasBancos.findById(pojoTarget.getIdCuentaOrigen());
				if (pojoAux == null) 
					pojoAux = new CtasBanco();
				pojoResult.setIdCuentaOrigen(CtasBancoToCtasBancoExt(pojoAux));
			}
			
			if (pojoTarget.getIdCuentaOrigenTerceros() != null && pojoTarget.getIdCuentaOrigenTerceros() > 0L) {
				Persona pojoAux = ifzPersonas.findById(pojoTarget.getIdCuentaOrigenTerceros());
				if (pojoAux == null) 
					pojoAux = new Persona();
				pojoResult.setIdCuentaOrigenTerceros(PersonaToPersonaExt(pojoAux));
			}
			
			if (pojoTarget.getIdSucursal() != null && pojoTarget.getIdSucursal() > 0L){
				Sucursal pojoAux = ifzSucursales.findById(pojoTarget.getIdSucursal());
				if (pojoAux == null) 
					pojoAux = new Sucursal();
				pojoResult.setIdSucursal(SucursalToSucursalExt(pojoAux));
			}
			
			if (pojoTarget.getIdBeneficiario() != null && pojoTarget.getIdBeneficiario() > 0L) {
				Persona pojoPersona = new Persona();
				
				if ("P".equals(pojoResult.getTipoBeneficiario())) {
					pojoPersona = ifzPersonas.findById(pojoTarget.getIdBeneficiario());
					if (pojoPersona == null)
						pojoPersona = new Persona();
					pojoPersona.setTipoPersona(1L);
				} else {
					Negocio pojoNegocio = ifzNegocios.findById(pojoTarget.getIdBeneficiario());
					if (pojoNegocio == null)
						pojoNegocio = new Negocio();
					pojoPersona.setId(pojoNegocio.getId());
					pojoPersona.setNombre(pojoNegocio.getNombre());
					pojoPersona.setRfc(pojoNegocio.getRfc());
					pojoPersona.setBanco(pojoNegocio.getBanco());
					pojoPersona.setClabeInterbancaria(pojoNegocio.getClabeInterbancaria());
					pojoPersona.setTipoPersona(0L);
				}

				pojoResult.setBeneficiario(pojoPersona.getNombre());
				pojoResult.setIdBeneficiario(PersonaToPersonaExt(pojoPersona));
			}

			if (pojoTarget.getIdCuentaDestino() != null && pojoTarget.getIdCuentaDestino() > 0L) {
				CtasBanco pojoAux = ifzCtasBancos.findById(pojoTarget.getIdCuentaDestino());
				if (pojoAux == null) 
					pojoAux = new CtasBanco();
				pojoResult.setIdCuentaDestino(CtasBancoToCtasBancoExt(pojoAux));
			}
			
			if (pojoTarget.getIdCuentaDestinoTerceros() != null && pojoTarget.getIdCuentaDestinoTerceros() > 0L) {
				Persona pojoAux = ifzPersonas.findById(pojoTarget.getIdCuentaDestinoTerceros());
				if (pojoAux == null) 
					pojoAux = new Persona();
				pojoResult.setIdCuentaDestinoTerceros(PersonaToPersonaExt(pojoAux));
			}
			
			if (pojoTarget.getIdTiposMovimiento() != null && pojoTarget.getIdTiposMovimiento() > 0L) {
				ConValores pojoAux = ifzConValores.findById(pojoTarget.getIdTiposMovimiento());
				if (pojoAux == null) 
					pojoAux = new ConValores();
				pojoResult.setIdTiposMovimiento(pojoAux);
			}
			
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra() > 0L) {
				Obra pojoAux = ifzObras.findById(pojoTarget.getIdObra());
				if (pojoAux == null) 
					pojoAux = new Obra();
				pojoResult.setIdObra(pojoAux);
			}
			
			if (pojoTarget.getIdOrdenCompra() > 0L) 
				pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.PagosGastosToPagosGastosExt", e);
			pojoResult = this.PagosGastosToPagosGastosExt(pojoTarget);
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: PagosGastos To PagosGastosExt TERMINADO :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		return pojoResult;
	}
	
	public PagosGastosDet PagosGastosDetExtToPagosGastosDet(PagosGastosDetExt pojoTarget) throws Exception {
		PagosGastosDet pojoResult = new PagosGastosDet();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: PagosGastosDetExtToPagosGastosDet");
			if(pojoTarget.getId() != null)
				pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdConcepto(pojoTarget.getIdConcepto().getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setIdProveedor(pojoTarget.getIdProveedor().getId());
			pojoResult.setReferencia(pojoTarget.getReferencia());
			pojoResult.setSubtotal(pojoTarget.getSubtotal());
			pojoResult.setTotalImpuestos(pojoTarget.getTotalImpuestos());
			pojoResult.setTotalRetenciones(pojoTarget.getTotalRetenciones());
			pojoResult.setFacturado(pojoTarget.getFacturado());
			pojoResult.setIdXml(pojoTarget.getIdXml());
			pojoResult.setEsCredito(pojoTarget.getEsCredito() ? 1 : 0);
			pojoResult.setIdObra(0L);
			pojoResult.setIdOrdenCompra(0L);
			
			if (pojoTarget.getIdPagosGastos() != null) {
				PagosGastos pojoAux = this.PagosGastosExtToPagosGastos(pojoTarget.getIdPagosGastos());
				pojoResult.setIdPagosGastos(pojoAux);
			}
			
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra().getId() != null && pojoTarget.getIdObra().getId() > 0L)
				pojoResult.setIdObra(pojoTarget.getIdObra().getId());
			
			if (pojoTarget.getIdOrdenCompra() != null && pojoTarget.getIdOrdenCompra().getId() != null && pojoTarget.getIdOrdenCompra().getId() > 0L)
				pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra().getId());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.PagosGastosDetExtToPagosGastosDet", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: PagosGastosDetExt To PagosGastosDet :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public PagosGastosDetExt PagosGastosDetToPagosGastosDetExt(PagosGastosDet pojoTarget) throws Exception {
		PagosGastosDetExt pojoResult = new PagosGastosDetExt();
		
		try {
			if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: PagosGastosDetToPagosGastosDetExt");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setReferencia(pojoTarget.getReferencia());
			pojoResult.setSubtotal(pojoTarget.getSubtotal());
			pojoResult.setTotalImpuestos(pojoTarget.getTotalImpuestos());
			pojoResult.setTotalRetenciones(pojoTarget.getTotalRetenciones());
			pojoResult.setFacturado(pojoTarget.getFacturado());
			pojoResult.setIdXml(pojoTarget.getIdXml());
			pojoResult.setEsCredito(pojoTarget.getEsCredito() == 1);
			
			if (pojoTarget.getIdPagosGastos() != null) {
				PagosGastosExt pojoAux = this.PagosGastosToPagosGastosExt(pojoTarget.getIdPagosGastos());
				pojoResult.setIdPagosGastos(pojoAux);
			}
			
			if (pojoTarget.getIdConcepto() != 0L) {
				ConValores pojoConcepto = this.ifzConValores.findById(pojoTarget.getIdConcepto());
				pojoResult.setIdConcepto(pojoConcepto);
			}
			
			if (pojoTarget.getIdProveedor() != 0L) {
				Persona pojoPersona = this.ifzPersonas.findById(pojoTarget.getIdProveedor());
				pojoResult.setIdProveedor(this.PersonaToPersonaExt(pojoPersona));
			}
			
			if (pojoTarget.getIdObra() != null && pojoTarget.getIdObra() > 0L) {
				Obra pojoObra = this.ifzObras.findById(pojoTarget.getIdObra());
				if (pojoObra == null)
					pojoObra = new Obra();
				pojoResult.setIdObra(pojoObra);
			}
			
			if (pojoTarget.getIdOrdenCompra() != null && pojoTarget.getIdOrdenCompra() > 0L) {
				OrdenCompra pojoOrdenCompra = this.ifzOC.findById(pojoTarget.getIdOrdenCompra());
				if (pojoOrdenCompra == null)
					pojoOrdenCompra = new OrdenCompra();
				pojoResult.setIdOrdenCompra(pojoOrdenCompra);
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.PagosGastosDetToPagosGastosDetExt", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: PagosGastosDet To PagosGastosDetExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public PagosGastosDetImpuestos PagosGastosDetImpuestosExtToPagosGastosDetImpuestos(PagosGastosDetImpuestosExt pojoTarget) throws Exception {
		PagosGastosDetImpuestos pojoResult = new PagosGastosDetImpuestos();
		
		try {
			if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: PagosGastosDetImpuestosExtToPagosGastosDetImpuestos");
			if (pojoTarget.getId() == null )
				pojoTarget.setId(0L);
			
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setValor(pojoTarget.getValor());
			
			if (pojoTarget.getIdPagosGastosDet().getId() != null)
				pojoResult.setIdImpuesto(pojoTarget.getIdImpuesto().getId());
			else
				pojoResult.setIdImpuesto(0L);
			
			if (pojoTarget.getIdPagosGastosDet().getId() != null){
				PagosGastosDet pojoAux = this.PagosGastosDetExtToPagosGastosDet(pojoTarget.getIdPagosGastosDet());
				pojoResult.setIdPagosGastosDet(pojoAux.getId());
				//pojoResult.setPagosGastosDetId(pojoAux);
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.PagosGastosDetImpuestosExtToPagosGastosDetImpuestos", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: PagosGastosDetImpuestosExt To PagosGastosDetImpuestos :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public PagosGastosDetImpuestosExt PagosGastosDetImpuestosToPagosGastosDetImpuestosExt(PagosGastosDetImpuestos pojoTarget) throws Exception {
		PagosGastosDetImpuestosExt pojoResult = new PagosGastosDetImpuestosExt();
		
		try {
			if (this.mostrarSystemOut) log.info("ConvertExt :: From " + this.from + " :: PagosGastosDetImpuestosToPagosGastosDetImpuestosExt");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setValor(pojoTarget.getValor());
			
			if (pojoTarget.getIdImpuesto() != 0L) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getIdImpuesto());
				pojoResult.setIdImpuesto(pojoAux);
			}
			
			if (pojoTarget.getIdPagosGastosDet() != null) {
				PagosGastosDet pojoAux0 = this.ifzPagosGastosDet.findById(pojoTarget.getIdPagosGastosDet());
				PagosGastosDetExt pojoAux = this.PagosGastosDetToPagosGastosDetExt(pojoAux0);
				//PagosGastosDetExt pojoAux = this.PagosGastosDetToPagosGastosDetExt(pojoTarget.getPagosGastosDetId());
				pojoResult.setIdPagosGastosDet(pojoAux);
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.PagosGastosDetImpuestosToPagosGastosDetImpuestosExt", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: PagosGastosDetImpuestos To PagosGastosDetImpuestosExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}

	public GastosImpuesto GastosImpuestoExtToGastosImpuesto(GastosImpuestoExt pojoTarget) {
		GastosImpuesto pojoResult = new GastosImpuesto();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: GastosImpuestoExtToGastosImpuesto");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setGastoId(pojoTarget.getGastoId());
			pojoResult.setImpuestoId(pojoTarget.getImpuestoId().getId());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.GastosImpuestoExtToGastosImpuesto", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: GastosImpuestoExt To GastosImpuesto :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public GastosImpuestoExt GastosImpuestoToGastosImpuestoExt(GastosImpuesto pojoTarget) {
		GastosImpuestoExt pojoResult = new GastosImpuestoExt();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: GastosImpuesto To GastosImpuestoExt");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setGastoId(pojoTarget.getGastoId());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			if (pojoTarget.getImpuestoId() != 0L) {
				ConValores pojo = ifzConValores.findById(pojoTarget.getImpuestoId());
				pojoResult.setImpuestoId(pojo);
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.GastosImpuestoToGastosImpuestoExt", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: GastosImpuesto To GastosImpuestoExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ProgPagos ProgPagosExtToProgPagos(ProgPagosExt pojoTarget) throws Exception {
		ProgPagos pojoResult = new ProgPagos();

		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setAl(pojoTarget.getAl());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setDel(pojoTarget.getDel());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setFechaRevision(pojoTarget.getFechaRevision());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setMontoAutorizado(pojoTarget.getMontoAutorizado());
			pojoResult.setMontoRevisado(pojoTarget.getMontoRevisado());
			pojoResult.setNumLote(pojoTarget.getNumLote());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setRevisadoPor(pojoTarget.getRevisadoPor());
			pojoResult.setTotal(pojoTarget.getTotal());
			pojoResult.setTransferenciaId(pojoTarget.getTransferenciaId());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante de tipos nativos terminado");
			
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante AGENTE");
			if (pojoTarget.getAgente() != null && pojoTarget.getAgente().getId() > 0L)
				pojoResult.setAgente(pojoTarget.getAgente().getId());

			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante EMPRESA");
			if (pojoTarget.getEmpresaId() != null && pojoTarget.getEmpresaId().getId() != null && pojoTarget.getEmpresaId().getId() > 0L)
				pojoResult.setEmpresaId(pojoTarget.getEmpresaId().getId());

			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante BENEFICIARIO");
			if (pojoTarget.getBeneficiarioId() != null && pojoTarget.getBeneficiarioId().getId() > 0L)
				pojoResult.setBeneficiarioId(pojoTarget.getBeneficiarioId().getId());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.ProgPagosExtToProgPagos", e);
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ProgPagosExt ProgPagosToProgPagosExt(ProgPagos pojoTarget) throws Exception {
		ProgPagosExt pojoResult = new ProgPagosExt();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagos To ProgPagosExt");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setAl(pojoTarget.getAl());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setDel(pojoTarget.getDel());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setFechaRevision(pojoTarget.getFechaRevision());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setMontoAutorizado(pojoTarget.getMontoAutorizado());
			pojoResult.setMontoRevisado(pojoTarget.getMontoRevisado());
			pojoResult.setNumLote(pojoTarget.getNumLote());
			pojoResult.setObservaciones(pojoTarget.getObservaciones());
			pojoResult.setRevisadoPor(pojoTarget.getRevisadoPor());
			pojoResult.setTotal(pojoTarget.getTotal());
			pojoResult.setTransferenciaId(pojoTarget.getTransferenciaId());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			
			if (pojoTarget.getAgente() != null && pojoTarget.getAgente() > 0L) {
				Sucursal pojoSucursal = ifzSucursales.findById(pojoTarget.getAgente());
				pojoResult.setAgente(this.SucursalToSucursalExt(pojoSucursal));
			}
			
			if (pojoTarget.getEmpresaId() != null && pojoTarget.getEmpresaId() > 0L) {
				Empresa pojoEmpresa = ifzEmpresas.findById(pojoTarget.getEmpresaId());
				pojoResult.setEmpresaId(pojoEmpresa);
			}
			
			if (pojoTarget.getBeneficiarioId() != null && pojoTarget.getBeneficiarioId() > 0L) {
				Persona pojoPersona = this.ifzPersonas.findById(pojoTarget.getBeneficiarioId());
				pojoResult.setBeneficiarioId(this.PersonaToPersonaExt(pojoPersona));
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.ProgPagosToProgPagosExt", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: ProgPagos To ProgPagosExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ProgPagosDetalle ProgPagosDetalleExtToProgPagosDetalle(ProgPagosDetalleExt pojoTarget) throws Exception {
		ProgPagosDetalle pojoResult = new ProgPagosDetalle();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosDetalleExt To ProgPagosDetalle :: Comprobante de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setControl(pojoTarget.getControl());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setMontoOriginal(pojoTarget.getMontoOriginal());
			pojoResult.setMontoRev(pojoTarget.getMontoRev());
			pojoResult.setRestar(pojoTarget.getRestar());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			pojoResult.setIdOrdenCompra(0L);
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante de tipos nativos terminado");
			
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante TIPOSGASTOS");
			if (pojoTarget.getTiposGastos() != null && pojoTarget.getTiposGastos().getId() > 0L) 
				pojoResult.setTiposGastos(pojoTarget.getTiposGastos().getId());
			
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante PROGPAGOS");
			if (pojoTarget.getProgPagos() != null && pojoTarget.getProgPagos().getId() != null && pojoTarget.getProgPagos().getId() > 0L)
				pojoResult.setProgPagos(this.ProgPagosExtToProgPagos(pojoTarget.getProgPagos()));
			
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante PAGOSGASTOS");
			if(pojoTarget.getPagosGastosId() != null && pojoTarget.getPagosGastosId().getId() != null && pojoTarget.getPagosGastosId().getId() > 0L)
				pojoResult.setPagosGastosId(pojoTarget.getPagosGastosId().getId());
			
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante BENEFICIARIO");
			if (pojoTarget.getIdBeneficiario() != null && pojoTarget.getIdBeneficiario().getId() > 0L) 
				pojoResult.setIdBeneficiario(pojoTarget.getIdBeneficiario().getId());
			
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante ORDEN COMPRA");
			if (pojoTarget.getIdOrdenCompra() != null && pojoTarget.getIdOrdenCompra() > 0L) 
				pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.ProgPagosDetalleExtToProgPagosDetalle", e);
		}
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public ProgPagosDetalleExt ProgPagosDetalleToProgPagosDetalleExt(ProgPagosDetalle pojoTarget) throws Exception {
		ProgPagosDetalleExt pojoResult = new ProgPagosDetalleExt();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosDetalle To ProgPagosDetalleExt :: Comprobante de tipos nativos");
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setControl(pojoTarget.getControl());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setMonto(pojoTarget.getMonto());
			pojoResult.setMontoOriginal(pojoTarget.getMontoOriginal());
			pojoResult.setMontoRev(pojoTarget.getMontoRev());
			pojoResult.setRestar(pojoTarget.getRestar());
			pojoResult.setTipoBeneficiario(pojoTarget.getTipoBeneficiario());
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagos To ProgPagosExt :: Comprobante de tipos nativos terminado");

			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagos To ProgPagosExt :: Comprobante TIPOSGASTOS");
			if (pojoTarget.getTiposGastos() != null && pojoTarget.getTiposGastos() != 0L) {
				ConValores pojoAux = this.ifzConValores.findById(pojoTarget.getTiposGastos());
				pojoResult.setTiposGastos(pojoAux);
			}

			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagos To ProgPagosExt :: Comprobante PROGPAGOS");
			if (pojoTarget.getProgPagos() != null) {
				pojoResult.setProgPagos(ProgPagosToProgPagosExt(pojoTarget.getProgPagos()));
				//ProgPagos pojoAux = this.ifzProgPagos.findById(pojoTarget.getProgPagos());
				//pojoResult.setProgPagos(ProgPagosToProgPagosExt(pojoAux));
			}
			
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagos To ProgPagosExt :: Comprobante PAGOSGASTOS");
			if (pojoTarget.getPagosGastosId() != null && pojoTarget.getPagosGastosId() > 0L) {
				PagosGastos pojoAux = this.ifzPagosGastos.findById(pojoTarget.getPagosGastosId());
				pojoResult.setPagosGastosId(this.PagosGastosToPagosGastosExt(pojoAux));
			}
			
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagosExt To ProgPagos :: Comprobante BENEFICIARIO");
			if (pojoTarget.getIdBeneficiario() != null && pojoTarget.getIdBeneficiario() > 0L) {
				Persona pojoPersona = new Persona();
				
				if (pojoTarget.getTipoBeneficiario() == null || "P".equals(pojoTarget.getTipoBeneficiario())) {
					pojoPersona = this.ifzPersonas.findById(pojoTarget.getIdBeneficiario());
					pojoPersona.setTipoPersona(1L);
				} else {
					Negocio pojoNegocio = this.ifzNegocios.findById(pojoTarget.getIdBeneficiario());
					
					pojoPersona.setId(pojoNegocio.getId());
					pojoPersona.setNombre(pojoNegocio.getNombre());
					pojoPersona.setRfc(pojoNegocio.getRfc());
					pojoPersona.setBanco(pojoNegocio.getBanco());
					pojoPersona.setClabeInterbancaria(pojoNegocio.getClabeInterbancaria());
					pojoPersona.setTipoPersona(0L);
				}
				
				pojoResult.setIdBeneficiario(this.PersonaToPersonaExt(pojoPersona));
			}
			
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: ProgPagos To ProgPagosExt :: Comprobante ORDEN COMPRA");
			if (pojoTarget.getIdOrdenCompra() != null && pojoTarget.getIdOrdenCompra() > 0L) {
				pojoResult.setIdOrdenCompra(pojoTarget.getIdOrdenCompra());
				/*OrdenCompra pojoAux = this.ifzOC.findById(pojoTarget.getIdOrdenCompra());
				if (pojoAux == null) pojoAux = new OrdenCompra();
				pojoResult.setIdOrdenCompra(pojoAux);*/
			}
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.ProgPagosDetalleToProgPagosDetalleExt", e);
		}
		
		if (this.mostrarSystemOut) log.info("ConvertExt :: ProgPagos To ProgPagosExt :: Conversion terminada :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public PersonaExt PersonaToPersonaExt(Persona pojoTarget) throws Exception {
		PersonaExt pojoResult =  new PersonaExt();
		
		if (this.mostrarSystemOut) 
			log.info("ConvertExt :: From " + this.from + " :: PersonaToPersonaExt");

		try {
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			
			if (pojoTarget.getHomonimo() > 0)
				pojoResult.setHomonimo(true);
			else
				pojoResult.setHomonimo(false);
			
			pojoResult.setNombre(pojoTarget.getNombre());
			pojoResult.setPrimerNombre(pojoTarget.getPrimerNombre());
			pojoResult.setSegundoNombre(pojoTarget.getSegundoNombre());
			pojoResult.setNombresPropios(pojoTarget.getNombresPropios());
			pojoResult.setPrimerApellido(pojoTarget.getPrimerApellido());
			pojoResult.setSegundoApellido(pojoTarget.getSegundoApellido());
			pojoResult.setRfc(pojoTarget.getRfc());
			pojoResult.setSexo(pojoTarget.getSexo());
			pojoResult.setFechaNacimiento(pojoTarget.getFechaNacimiento());
			
			if (pojoTarget.getEstadoCivil() != null) 
				pojoResult.setEstadoCivil(ifzConValores.findById(pojoTarget.getEstadoCivil()));
			
			if (pojoTarget.getLocalidad() != null) 
				pojoResult.setLocalidad(ifzLocalidad.findById(pojoTarget.getLocalidad()));
			
			if (pojoTarget.getNacionalidad() != null)
				pojoResult.setNacionalidad(ifzConValores.findById(pojoTarget.getNacionalidad()));
			
			if (pojoTarget.getConyuge() != null)
				pojoResult.setConyuge(PersonaToPersonaExt(pojoTarget.getConyuge()));
			
			pojoResult.setNumeroHijos(pojoTarget.getNumeroHijos());
			pojoResult.setDomicilio(pojoTarget.getDomicilio());
			pojoResult.setTelefono(pojoTarget.getTelefono());
			pojoResult.setCorreo(pojoTarget.getCorreo());
			
			if (pojoTarget.getFinado() > 0) {
				pojoResult.setFinado(true);
			} else {
				pojoResult.setFinado(false);
			}
			
			if (pojoTarget.getTipoSangre() != null)
				pojoResult.setTipoSangre(ifzConValores.findById(pojoTarget.getTipoSangre()));
			
			if (pojoTarget.getColonia() != null)
				pojoResult.setColonia(ifzColonia.findById(pojoTarget.getColonia()));
			
			pojoResult.setTipoPersona(pojoTarget.getTipoPersona());
			
			String res = pojoTarget.getPrimerNombre() != null && pojoTarget.getPrimerNombre().length() > 0? pojoTarget.getPrimerNombre() : "";
			res += pojoTarget.getSegundoNombre()	!= null && pojoTarget.getSegundoNombre().length() > 0 ? " " + pojoTarget.getSegundoNombre() : "";
			res += pojoTarget.getNombresPropios()	!= null && pojoTarget.getNombresPropios().length() > 0 ? " " + pojoTarget.getNombresPropios() : "";
			res += pojoTarget.getPrimerApellido()	!= null && pojoTarget.getPrimerApellido().length() > 0 ? " " + pojoTarget.getPrimerApellido() : "";
			res += pojoTarget.getSegundoApellido()	!= null && pojoTarget.getSegundoApellido().length() > 0 ? " " + pojoTarget.getSegundoApellido() : "";
			
			pojoResult.setNombreCompleto(res);
			
			pojoResult.setNumeroCuenta(pojoTarget.getNumeroCuenta());
			pojoResult.setClabeInterbancaria(pojoTarget.getClabeInterbancaria());
			
			if (pojoTarget.getBanco() != null && pojoTarget.getBanco() > 0)
				pojoResult.setBanco(CatBancosToCatBancosExt(ifzBancos.findById(pojoTarget.getBanco())));
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.PersonaToPersonaExt", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: Persona To PersonaExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public SucursalExt SucursalToSucursalExt(Sucursal pojoTarget) throws Exception{
		SucursalExt pojoResult = new SucursalExt();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: SucursalToSucursalExt");
			pojoResult.setId(pojoTarget.getId());
			
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			
			pojoResult.setSucursal(pojoTarget.getSucursal());
			pojoResult.setColonia(pojoTarget.getColonia());
			
			pojoResult.setDomicilio(pojoTarget.getDomicilio());
			
			pojoResult.setDomicilio1(pojoTarget.getDomicilio1());
			pojoResult.setDomicilio2(pojoTarget.getDomicilio2());
			pojoResult.setDomicilio3(pojoTarget.getDomicilio3());
			pojoResult.setDomicilio4(pojoTarget.getDomicilio4());
			pojoResult.setDomicilio5(pojoTarget.getDomicilio5());
			
			pojoResult.setDescDomicilio1(pojoTarget.getDescDomicilio1());
			pojoResult.setDescDomicilio2(pojoTarget.getDescDomicilio2());
			pojoResult.setDescDomicilio3(pojoTarget.getDescDomicilio3());
			pojoResult.setDescDomicilio4(pojoTarget.getDescDomicilio4());
			pojoResult.setDescDomicilio5(pojoTarget.getDescDomicilio5());
			
			pojoResult.setEmpresa(pojoTarget.getEmpresa());
			pojoResult.setRegion(pojoTarget.getRegion());
			pojoResult.setCalendarioDiaInhabil(pojoTarget.getCalendarioDiaInhabil());
			
			pojoResult.setSegmentoContable(pojoTarget.getSegmentoContable());
			pojoResult.setSegmentoNegocio(pojoTarget.getSegmentoNegocio());
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.SucursalToSucursalExt", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: Sucursal To SucursalExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
	
	public SucursalBancariaExt SucursalBancariaToSucursalBancaraExt(SucursalBancaria pojoTarget) throws Exception {
		SucursalBancariaExt pojoResult = new SucursalBancariaExt();
		
		try {
			if (this.mostrarSystemOut) 
				log.info("ConvertExt :: From " + this.from + " :: SucursalBancariaToSucursalBancaraExt");
			pojoResult.setId(pojoTarget.getId());
			
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
			
			pojoResult.setDomicilio(pojoTarget.getDomicilio());
			pojoResult.setNombre(pojoTarget.getNombre());
			pojoResult.setSucursalBancariaId(pojoTarget.getSucursalBancariaId());
			pojoResult.setEstado(pojoTarget.getEstado());
			
			pojoResult.setCatBancoId(CatBancosToCatBancosExt(pojoTarget.getCatBancoId()));
		} catch (Exception e) {
			log.error("Error en Logica_CuentasPorPagar.ConvertExt.SucursalBancariaToSucursalBancaraExt", e);
		}
		
		if (this.mostrarSystemOut)
			log.info("ConvertExt :: SucursalBancaria To SucursalBancaraExt :: " + pojoTarget.getId() + "|" + pojoResult.getId());
		
		return pojoResult;
	}
}
