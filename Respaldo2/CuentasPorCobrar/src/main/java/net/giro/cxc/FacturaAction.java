package net.giro.cxc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;
import java.util.TimeZone;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.logica.NegociosRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.cxc.beans.ConceptoFacturacion;
import net.giro.cxc.beans.ConceptoFacturacionImpuestosExt;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalle;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.beans.FacturaDetalleImpuestoExt;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.fe.FactElect;
import net.giro.cxc.fe.FactElectv33;
import net.giro.cxc.logica.ConceptoFacturacionImpuestosRem;
import net.giro.cxc.logica.ConceptoFacturacionRem;
import net.giro.cxc.logica.FacturaDetalleImpuestoRem;
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaPagosRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.Utilerias;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.logica.EmpresasRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.dao.MonedasValoresDAO;
import net.giro.tyg.logica.BancosRem;
import net.giro.tyg.logica.CuentasBancariasRem;
import net.giro.tyg.logica.FormasPagosRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="facturaAction")
public class FacturaAction implements Serializable, Runnable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FacturaAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx; 
	// Interfaces 
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private FacturaRem ifzFactura;
	private FacturaDetalleRem ifzFacturaDetalle;
	private FacturaDetalleImpuestoRem ifzFacDetImpuestos;
	private ReportesRem ifzReportes;
	private FacturaPagosRem ifzPagos;
	private EmpresasRem ifzEmpresa;
	private PersonaRem ifzPersonas;
	private NegociosRem ifzNegocios;
	private CuentasBancariasRem ifzCtas;
	private BancosRem ifzBancos;
	// POJO's
	//private Factura pojoFacturaBorrar;
	//private boolean cancelacionConvertida;
	private FacturaExt pojoFactura;
	private FacturaDetalleExt pojoDetalle; 
	private FacturaDetalleImpuestoExt pojoDetalleImpuesto;
	private long idFacturaBorrar;
	// Listas
	private List<Factura> listFacturas;
	private List<FacturaDetalleExt> listDetalles;
	private List<FacturaDetalleExt> listDetallesEliminados;
	private List<FacturaDetalleImpuestoExt> listDetalleImpuestos;
	private List<FacturaDetalleImpuestoExt> listDetalleImpuestosEliminados;
	private HashMap<Long, List<FacturaDetalleImpuestoExt>> listMapDetalleImpuestos;
	// Elementos auxiliares
	private List<SelectItem> listConceptoFacturacionItems;	
	private SelectItem conceptoSeleccionado;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;
	private List<SelectItem> listMeses;
	private String campoBusqueda;
	private String valorBusqueda;
	private Date busquedaFecha;
	private int busquedaMes;
	//private int tipoBusqueda;
	private int numPagina;
	// Busqueda proyectos/Obras
	private ObraRem ifzObra;
	private List<Obra> listaObras;
	private Obra pojoObra;
	private List<SelectItem> opcionesBusquedaObras;
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int tipoBusquedaProyecto;
	private int paginacionObras;
	// Busqueda Sucursales
	private SucursalesRem ifzSucursales;
	private List<Sucursal> listBusquedaSucursal;
	private List<SelectItem> listBusquedaSucursalItems;	
	private List<SelectItem> tipoBusquedaSucursal;	
	private String valorBusquedaSucursal;
	private String campoBusquedaSucursal;
	private int paginaSucursales;
	private long sucursalId;
	// Busqueda conceptos de facturacion
	private ConceptoFacturacionRem ifzConceptosFacturacion;
	private ConceptoFacturacionImpuestosRem ifzConFacImpuestos;
	private List<ConceptoFacturacion> listConceptoFacturacion;
	private ConceptoFacturacion pojoConcepto;
	private List<SelectItem> tiposBusquedaConceptos;	
	private String valBusquedaConceptos;
	private String campoBusquedaConceptos;
	private int numPaginaConceptos;
	// Metodos de pago
	private ConGrupoValores pojoGpoMetodosPago; 
	private List<ConValores> listMetodosPago;
	private List<SelectItem> listMetodosPagoItems;
	private long idMetodoPago;
	// Formas de pago
	private FormasPagosRem ifzFormasPago;
	private List<FormasPagos> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	// Variables de control
	private boolean procesando;
	private boolean procesoIncompleto;
	private int tipoMensaje;
	private String mensaje;
    private long usuarioId;
    private long empresaId;
    private String usuario;
    private String usuarioEmail;
	private int numPaginaDetalles;
	private int numPaginaImpuestos;
	private int indexDetalleFactura;
	private boolean facturaTimbrada;
	private boolean puedeTimbrar;
	private boolean perfilEgresos;
	private boolean buscarAdministrativo;
	private long idMonedaSugerida;
	private long idFormaPagoSugerido;
	private long idMetodoPagoSugerido;
	private boolean refacturar;
	// Datos de la factura
	private double subtotal;
	private double impuestos;
	private double retenciones;
	private double total;
	private double costo;
	private long conceptoAnterior;
	private int MAX_LENGTH_FOLIO_FACTURA;
	private String facturaSerie;
	private String facturaFolio;
	private Date fechaEmision;
	private TimeZone timeZone;
	private String mensajeCancelacion;
	// Email
	private String email;
	private String emailAsunto;
	private String emailCuerpo;
	// Provisiones
	private boolean perfilProvisiones;
	private boolean permiteProvisionar;
	private double montoFacturado;
	private double montoPagado;
	private double montoProvision;
	// Moneda
	private MonedaDAO ifzMonedas;
	private MonedasValoresDAO ifzMonValores;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private Moneda monedaBase;
	// TIMBRE
	private ConGrupoValores grupoCFDIUsos;
	private ConGrupoValores grupoCFDITipoRelacion;
	private ConGrupoValores grupoCFDITipoComprobante;
	private List<ConValores> listCFDIUso;
	private List<SelectItem> listCFDIUsoItems;
	private List<ConValores> listCFDITipoRelacion;
	private List<SelectItem> listCFDITipoRelacionItems;
	private List<ConValores> listCFDITipoComprobante;
	private List<SelectItem> listCFDITipoComprobanteItems;
	private List<SelectItem> listCFDIVersionItems;
	private String cfdiVersion;
	private String cfdiVersionSugerida;
	private long idUsoCfdi;
	private long idUsoCfdiSugerido;
	private long idTipoComprobante;
	private long idTipoComprobanteSugerido;
	private boolean cfdiRelacionado;
	private long idTipoRelacion;
	private String cfdiRelacionadoUuid;
	// Busqueda facturas
	private List<Factura> listFacturasCfdi;
	private Factura pojoFacturaCfdiSeleccionada;
	private List<SelectItem> tipoBusquedaFacturasCfdi;	
	private String valorBusquedaFacturasCfdi;
	private String campoBusquedaFacturasCfdi;
	private int numPaginaFacturasCfdi;
	// Pagos
	private List<CuentaBancaria> listCuentas;
	private List<SelectItem> listCuentasItems;
	private List<Banco> listBancos;
	private List<SelectItem> listBancosItems;
	private List<FacturaPagos> listFacturaPagos;
	private FacturaPagos pojoFacPago;
	private long idPago;
	private int paginacionPagos;
	// DEBUG
	private boolean isDebug;
	private Map<String,String> paramsRequest;


	public FacturaAction() throws NamingException,Exception {
		Map<String,String> params = null;
		ValueExpression valExp = null;
		FacesContext fc = null;
		Application app = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			this.usuarioEmail = this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo();
			this.empresaId = 1; // this.loginManager.getUsuarioResponsabilidad().getUsuario().getIdEmpresa();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			// Obtenemmos los parametros de la URL si corresponde
			params = fc.getExternalContext().getRequestParameterMap();
		    if (this.paramsRequest == null)
		    	this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : params.entrySet())
		    	this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
		    // Comprobamos si requerimos levantar la variable DEBUG
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
	
			this.ctx = new InitialContext();
			this.ifzFactura 			 = (FacturaRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacturaDetalle 		 = (FacturaDetalleRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzFacDetImpuestos 	 = (FacturaDetalleImpuestoRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleImpuestoFac!net.giro.cxc.logica.FacturaDetalleImpuestoRem");
			this.ifzConceptosFacturacion = (ConceptoFacturacionRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//ConceptoFacturacionFac!net.giro.cxc.logica.ConceptoFacturacionRem");
			this.ifzConFacImpuestos 	 = (ConceptoFacturacionImpuestosRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//ConceptoFacturacionImpuestosFac!net.giro.cxc.logica.ConceptoFacturacionImpuestosRem");
			this.ifzPagos				 = (FacturaPagosRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosFac!net.giro.cxc.logica.FacturaPagosRem");
			this.ifzObra 				 = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzGpoVal 				 = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores 			 = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzSucursales			 = (SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzReportes 			 = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzMonedas 			 = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzMonValores 			 = (MonedasValoresDAO) this.ctx.lookup("ejb:/Model_TYG//MonedasValoresImp!net.giro.tyg.dao.MonedasValoresDAO");
			this.ifzFormasPago 			 = (FormasPagosRem) this.ctx.lookup("ejb:/Logica_TYG//FormasPagosFac!net.giro.tyg.logica.FormasPagosRem");
			this.ifzEmpresa 			 = (EmpresasRem) this.ctx.lookup("ejb:/Logica_Publico//EmpresasFac!net.giro.plataforma.logica.EmpresasRem");
			this.ifzPersonas 			 = (PersonaRem) this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzNegocios 			 = (NegociosRem) this.ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
			this.ifzCtas 				 = (CuentasBancariasRem) this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
			this.ifzBancos				 = (BancosRem) this.ctx.lookup("ejb:/Logica_TYG//BancosFac!net.giro.tyg.logica.BancosRem");
			
			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFacturaDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFacDetImpuestos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConceptosFacturacion.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConFacImpuestos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObra.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSucursales.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFormasPago.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpresa.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzBancos.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.ifzFactura.showSystemOuts(false);
			this.ifzFacturaDetalle.showSystemOuts(false);
			this.ifzConFacImpuestos.showSystemOuts(false);
	
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);

			valPerfil = this.loginManager.getAutentificacion().getPerfil("FACTURAS_PROVISION");
			this.perfilProvisiones = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			Moneda valMoneda = this.ifzMonedas.findByAbreviacion(valPerfil);
			if (valMoneda != null) {
				this.monedaBase = valMoneda;
				this.idMonedaSugerida = valMoneda.getId();
			}
			
			this.pojoFactura = new FacturaExt();
			this.pojoDetalle = new FacturaDetalleExt();
			this.listFacturas = new ArrayList<Factura>();
			this.listDetalles = new ArrayList<FacturaDetalleExt>();
			this.listaObras = new ArrayList<Obra>();
		    this.listCuentasItems = new ArrayList<SelectItem>();
		    this.listBancosItems = new ArrayList<SelectItem>();

			// Busqueda pricipal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("folioFactura","Folio Factura"));
			this.tiposBusqueda.add(new SelectItem("nombreObra","Obra"));
			this.tiposBusqueda.add(new SelectItem("nombreCliente","Cliente"));
			this.tiposBusqueda.add(new SelectItem("fechaEmision","Fecha"));
			this.tiposBusqueda.add(new SelectItem("mes","Mes"));
			this.tiposBusqueda.add(new SelectItem("id","ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			//this.tipoBusqueda = 0;
			this.numPagina = 1;
			this.numPaginaDetalles = 1;
			this.numPaginaImpuestos = 1;
			
			this.listMeses = new ArrayList<SelectItem>();
			this.listMeses.add(new SelectItem( 1, "Enero"));
			this.listMeses.add(new SelectItem( 2, "Febrero"));
			this.listMeses.add(new SelectItem( 3, "Marzo"));
			this.listMeses.add(new SelectItem( 4, "Abril"));
			this.listMeses.add(new SelectItem( 5, "Mayo"));
			this.listMeses.add(new SelectItem( 6, "Junio"));
			this.listMeses.add(new SelectItem( 7, "Julio"));
			this.listMeses.add(new SelectItem( 8, "Agosto"));
			this.listMeses.add(new SelectItem( 9, "Septiembre"));
			this.listMeses.add(new SelectItem(10, "Octubre"));
			this.listMeses.add(new SelectItem(11, "Noviembre"));
			this.listMeses.add(new SelectItem(12, "Diciembre"));
			
			// Busqueda proyectos/Obras
			this.campoBusquedaObras = "folioFactura";
			this.valorBusquedaObras = "";
			this.tipoBusquedaProyecto = 0;
			
			// Busqueda conceptos de facturacion
			this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
			this.tiposBusquedaConceptos.add(new SelectItem("descripcion","Concepto"));
			this.tiposBusquedaConceptos.add(new SelectItem("cuentaContable","Cuenta Contable"));
			this.tiposBusquedaConceptos.add(new SelectItem("id","Clave"));
			this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getDescription();
			this.valBusquedaConceptos = "";
			this.numPaginaConceptos = 1;

			// Busqueda facturas
			this.tipoBusquedaFacturasCfdi = new ArrayList<SelectItem>();
			this.tipoBusquedaFacturasCfdi.add(new SelectItem("folioFactura", "Folio"));
			this.tipoBusquedaFacturasCfdi.add(new SelectItem("cliente", "Cliente"));
			this.tipoBusquedaFacturasCfdi.add(new SelectItem("id", "ID"));
			this.valorBusquedaFacturasCfdi = this.tipoBusquedaFacturasCfdi.get(0).getValue().toString();
			this.campoBusquedaFacturasCfdi = "";
			this.numPaginaFacturasCfdi = 1;
			
			this.listCFDIVersionItems = new ArrayList<SelectItem>();
			this.listCFDIVersionItems.add(new SelectItem("3.3", "CFDI 3.3"));
			//this.listCFDIVersionItems.add(new SelectItem("3.2", "CFDI 3.2"));
			
			// Inicializamos listas
			if (this.listConceptoFacturacion == null)
				this.listConceptoFacturacion = new ArrayList<ConceptoFacturacion>();
			this.listConceptoFacturacion.clear();
			
			if (this.listConceptoFacturacionItems == null)
				this.listConceptoFacturacionItems = new ArrayList<SelectItem>();
			this.listConceptoFacturacionItems.clear();
			
			//this.concepto = 0L;
			this.conceptoAnterior = 0L;
			this.subtotal = 0;
			this.impuestos = 0;
			this.total = 0;
			
			this.numPaginaDetalles = 1;
			this.paginacionObras = 1;
			this.numPaginaImpuestos = 1;
			
			this.indexDetalleFactura = -1;
			
			// Busqueda SUCURSALES
			this.tipoBusquedaSucursal = new ArrayList<SelectItem>();
			this.tipoBusquedaSucursal.add(new SelectItem("sucursal", "Nombre"));
			this.tipoBusquedaSucursal.add(new SelectItem("id", "Clave"));
			this.valorBusquedaSucursal = this.tipoBusquedaSucursal.get(0).getValue().toString();
			this.campoBusquedaSucursal = "";
			this.paginaSucursales = 1;
			
			// Busqueda obras
			this.opcionesBusquedaObras = new ArrayList<SelectItem>();
			this.opcionesBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.opcionesBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.opcionesBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.opcionesBusquedaObras.add(new SelectItem("id", "Clave"));
			this.campoBusquedaObras = this.opcionesBusquedaObras.get(0).getDescription();
			this.valorBusquedaObras = "";
			this.tipoBusquedaProyecto = 0;
			this.paginacionObras = 1;
			
			// COMPROBAMOS LA MAXIMA LONGITUD DE FOLIO PARA LA FACTURACION
			// ---------------------------------------------------------------------------------------------------------
			this.MAX_LENGTH_FOLIO_FACTURA = 6;
			if (this.entornoProperties.containsKey("MAX_LENGTH_FOLIO_FACTURA") && this.entornoProperties.getString("MAX_LENGTH_FOLIO_FACTURA") != null && ! "".equals(entornoProperties.getString("MAX_LENGTH_FOLIO_FACTURA")))
				this.MAX_LENGTH_FOLIO_FACTURA = valueLengthFolioFactura(this.entornoProperties.getString("MAX_LENGTH_FOLIO_FACTURA"), this.MAX_LENGTH_FOLIO_FACTURA);
			
			// CARGAMOS LOS METODOS DE PAGO PARA FACTURAS
			// ---------------------------------------------------------------------------------------------------------
			this.pojoGpoMetodosPago = this.ifzGpoVal.findByName("SYS_METODO_PAGO");
			if (this.pojoGpoMetodosPago == null || this.pojoGpoMetodosPago.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_METODO_PAGO en con_grupo_valores");
			cargarMetodosPagos();

			// CARGAMOS LOS USOS PARA CFDI
			// ---------------------------------------------------------------------------------------------------------
			this.grupoCFDIUsos = this.ifzGpoVal.findByName("SYS_CFDI_USO");
			if (this.grupoCFDIUsos == null || this.grupoCFDIUsos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_CFDI_USO en con_grupo_valores");
			cargarCFDIUsos();

			// CARGAMOS LOS TIPOS DE RELACION PARA CFDI
			// ---------------------------------------------------------------------------------------------------------
			this.grupoCFDITipoRelacion = this.ifzGpoVal.findByName("SYS_CFDI_TIPO_RELACION");
			if (this.grupoCFDITipoRelacion == null || this.grupoCFDITipoRelacion.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_CFDI_TIPO_RELACION en con_grupo_valores");
			cargarCFDITiposRelacion();

			// CARGAMOS LOS TIPOS DE COMPROBANTE PARA CFDI
			// ---------------------------------------------------------------------------------------------------------
			this.grupoCFDITipoComprobante = this.ifzGpoVal.findByName("SYS_CFDI_TIPOS_COMPROBANTE");
			if (this.grupoCFDITipoComprobante == null || this.grupoCFDITipoComprobante.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_CFDI_TIPOS_COMPROBANTE en con_grupo_valores");
			cargarCFDITiposComprobante();
			
			// Cargo Formas de Pago
			cargarFormasPagos();

			// Cargo monedas
			cargarMonedas();
			
			// Cargo sucursal
			cargarSucursales();
			
			// Cuentas Bancarias
			this.listCuentasItems.clear();
			this.listCuentas = this.ifzCtas.findLikeColumnName("numeroDeCuenta", "");
			for (CuentaBancaria var : listCuentas) {
				this.listCuentasItems.add(new SelectItem(var.getId(),  var.getNumeroDeCuenta() + " - " + var.getInstitucionBancaria().getNombreCorto()));
			}
			
			// Bancos
			this.listBancosItems.clear();
			this.listBancos = this.ifzBancos.findLikeColumnName("nombreCorto", "");
			for (Banco var : listBancos) {
				this.listBancosItems.add(new SelectItem(var.getId(), var.getId() + " - " + var.getNombreCorto()));
			}
			
			control();
			this.cfdiVersionSugerida = "3.3";
			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
			this.paginacionPagos = 1;
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction", e);
			this.ctx = null;
		}
	}
   	
	
	public void buscar() {
		String campo = "";
		
		try {
			control();
			if ("".equals(this.campoBusqueda) || "".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			
			this.numPagina = 1;
			if (this.listFacturas != null)
				this.listFacturas.clear();

			if ("fechaEmision".equals(this.campoBusqueda)) {
				this.listFacturas = this.ifzFactura.findByProperty(this.campoBusqueda, this.busquedaFecha);
			} else {
				campo = this.campoBusqueda;
				if ("mes".equals(this.campoBusqueda)) {
					campo = "cast(date(fechaEmision) as string)";
					this.valorBusqueda = (new SimpleDateFormat("YYYY")).format(Calendar.getInstance().getTime()) + "-" + (this.busquedaMes < 10 ? "0" : "") + this.busquedaMes;
				}

				this.ifzFactura.orderBy("estatus DESC, fechaEmision DESC, id DESC");
				this.listFacturas = this.ifzFactura.findLikeProperty(campo, this.valorBusqueda, 0, 1000);
			}
			
			//this.ifzFactura.orderBy("estatus DESC, fechaCreacion DESC, id DESC");
			//this.listFacturas = this.ifzFactura.findLikeProperty(this.campoBusqueda, this.valorBusqueda, this.tipoBusqueda, 1000);
			if (this.listFacturas == null || this.listFacturas.isEmpty()) {
				log.info("2 - Busqueda sin resultados con el filtro " + this.campoBusqueda + " = " + this.valorBusqueda);
				control(2);
			}
		} catch(Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.buscar", e);
			control(true);
		}
	}

	public void nuevo() {
		try {
			control();
			this.pojoFactura = new FacturaExt();
			
			// Aplico sugeridos
			this.pojoFactura.setTipo("X");
			this.pojoFactura.setIdMoneda(this.idMonedaSugerida);
			this.pojoFactura.setIdFormaPago(this.idFormaPagoSugerido);
			this.idMetodoPago = this.idMetodoPagoSugerido;
			
			if (this.listDetalles == null)
				this.listDetalles = new ArrayList<FacturaDetalleExt>();
			this.listDetalles.clear();
			this.pojoDetalle = new FacturaDetalleExt();
			
			if (this.listDetallesEliminados == null)
				this.listDetallesEliminados = new ArrayList<FacturaDetalleExt>();
			this.listDetallesEliminados.clear();
			
			if (this.listDetalleImpuestos == null) 
				this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalleImpuestos.clear();
			
			if (this.listDetalleImpuestosEliminados == null) 
				this.listDetalleImpuestosEliminados = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalleImpuestosEliminados.clear();
			
			if (this.listMapDetalleImpuestos == null)
				this.listMapDetalleImpuestos = new HashMap<Long, List<FacturaDetalleImpuestoExt>>();
			this.listMapDetalleImpuestos.clear();
			
			this.sucursalId = 0L;
			this.conceptoAnterior = 0L;
			this.subtotal = 0;
			this.impuestos = 0;
			this.retenciones = 0;
			this.total = 0;
			this.puedeTimbrar = false;
			this.facturaTimbrada = false;
			//this.cancelacionConvertida = false;
			this.fechaEmision = Calendar.getInstance(this.timeZone).getTime();
			this.refacturar = false;
		} catch (Exception e) {
			log.error("Error en el metodo nuevo.", e);
			control(true);
		}
	}

	public void editar() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String fechaEmisionFactura = "";
		
		try {
			control();
			log.info("Proceso de edicion de Factura ... ");
			this.sucursalId = 0L;
			this.pojoObra = new Obra();
			
			// Inicializamos listas y variables de la factura
			if (this.listDetalles == null) 
				this.listDetalles = new ArrayList<FacturaDetalleExt>();
			this.listDetalles.clear();
			
			if (this.listDetallesEliminados == null) 
				this.listDetallesEliminados = new ArrayList<FacturaDetalleExt>();
			this.listDetallesEliminados.clear();
			
			if (this.listDetalleImpuestos == null) 
				this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalleImpuestos.clear();
			
			if (this.listDetalleImpuestosEliminados == null) 
				this.listDetalleImpuestosEliminados = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalleImpuestosEliminados.clear();
			
			if (this.listMapDetalleImpuestos == null)
				this.listMapDetalleImpuestos = new HashMap<Long, List<FacturaDetalleImpuestoExt>>();
			this.listMapDetalleImpuestos.clear();

			nuevoConceptoFacturacion();
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				log.info("ERROR INTERNO - No se pudo recuperar la Factura seleccionada"); 
				control("No se pudo recuperar la Factura seleccionada");
				return;
			}

			// Sucursal 
			if (this.isDebug)
				log.info("Recuperando sucursal ... ");
			if (this.pojoFactura.getIdSucursal() != null && this.pojoFactura.getIdSucursal().getId() > 0L)
				this.sucursalId = this.pojoFactura.getIdSucursal().getId();
			
			// Metodo de Pago
			if (this.isDebug)
				log.info("Recuperando metodo de pago ... ");
			if (this.pojoFactura.getIdMetodoPago() != null && this.pojoFactura.getIdMetodoPago().getId() > 0L)
				this.idMetodoPago = this.pojoFactura.getIdMetodoPago().getId();
			
			// Fecha Emision
			if (this.isDebug)
				log.info("Compruebo fecha Emision ... ");
			if ("00:00:00".equals(formatter.format(this.pojoFactura.getFechaEmision()))) {
				formatter.applyPattern("MM-dd-yyyy");
				fechaEmisionFactura = formatter.format(this.pojoFactura.getFechaEmision());
				formatter.applyPattern("HH:mm:ss");
				fechaEmisionFactura = fechaEmisionFactura + " " + formatter.format(Calendar.getInstance(this.timeZone).getTime());
				formatter.applyPattern("MM-dd-yyyy HH:mm:ss");
				this.pojoFactura.setFechaEmision(formatter.parse(fechaEmisionFactura));
				if (this.isDebug)
					log.info("Asigno fecha emision: " + fechaEmisionFactura);
			}
			this.fechaEmision = this.pojoFactura.getFechaEmision();
			
			// Detalles de factura (Conceptos)
			if (this.isDebug)
				log.info("Recuperando detalles (Conceptos) de factura ... ");
			this.listDetalles = ifzFacturaDetalle.findByPropertyPojoCompletoExt("idFactura", pojoFactura.getId().longValue());
			if (this.listDetalles == null || this.listDetalles.isEmpty()) {
				log.info("ERROR INTERNO - No se pudo recuperar los detalles (Conceptos) de la factura indicada"); 
				control("No se pudo recuperar los detalles (Conceptos) de la factura indicada");
				return;
			}
			if (this.isDebug)
				log.info(this.listDetalles.size() + " conceptos en factura. Comprobando conceptos ... ");
			
			// Desglozamos los impuestos para cada concepto para generar listas de control (MAPA)
			if (this.isDebug)
				log.info("Desglozando impuestos ... ");
			this.pojoDetalle = new FacturaDetalleExt();
			for (FacturaDetalleExt var : this.listDetalles) {
				this.pojoDetalle = var;
				if (var.getIdConcepto() == null) 
					continue;
				desglozarImpuestos();
			}
			if (this.isDebug)
				log.info("impuestos desglozado ... ");
			
			this.puedeTimbrar = true;
			this.facturaTimbrada = false;
			if (this.pojoFactura.getUuid() != null && ! "".equals(this.pojoFactura.getUuid())) {
				this.facturaTimbrada = true;
				this.puedeTimbrar = false;
			}
			
			totalizar();
			log.info("Proceso terminando");
		} catch (Exception e) {
			log.error("Error en editar()", e);
			control(true);
		}
	}
	
	public void guardar() {
		FacturaDetalle pojoDetalleAux = null;
		
		try {
			control();
			if (this.procesando)
				return;
			this.procesando = true;
			
			log.info("Validando Factura ... ");
			if (! validarFactura())
				return;

			log.info("Guardando Factura ... ");
			this.pojoFactura.setFolioFactura(this.pojoFactura.getSerie() + "-" + this.pojoFactura.getFolio());
			this.pojoFactura.setFechaVencimiento(Calendar.getInstance(this.timeZone).getTime());
			this.pojoFactura.setSubtotal(new BigDecimal(this.subtotal));
			this.pojoFactura.setImpuestos(this.impuestos);
			this.pojoFactura.setRetenciones(this.retenciones);
			this.pojoFactura.setTotal(new BigDecimal(this.total));
			this.pojoFactura.setSaldo(new BigDecimal(this.total));
			
			if (this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				this.pojoFactura.setEstatus(1);
				this.pojoFactura.setCreadoPor(this.usuarioId);
				this.pojoFactura.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
				this.pojoFactura.setModificadoPor(this.usuarioId);
				this.pojoFactura.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				
				// Guardamos la factura y la añadimos al listado
				this.pojoFactura.setId(this.ifzFactura.save(this.pojoFactura));
				this.listFacturas.add(0, this.ifzFactura.convertir(this.pojoFactura));
			} else {
				this.pojoFactura.setModificadoPor(this.usuarioId);
				this.pojoFactura.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				
				// Actualizamos la factura en la BD
				this.ifzFactura.update(this.pojoFactura);
				
				// Actualizamos la factura en la lista
				int index = -1;
				for(Factura var : this.listFacturas) {
					if(var.getId() == this.pojoFactura.getId()) {
						index = this.listFacturas.indexOf(var);
						break;
					}
				}
				
				if (index >= 0) 
					this.listFacturas.set(index, this.ifzFactura.findById(this.pojoFactura.getId()));
				log.info("Factura actualizada");
			}
			
			// Guardamos los detalles de la factura
			log.info("Guardando detalles (" + this.listDetalles.size() + ") ... ");
			for (FacturaDetalleExt var : this.listDetalles) {
				log.info("Guardando concepto " + var.getIdConcepto().getDescripcion() + " ... ");
				var.setIdFactura(this.pojoFactura);
				
				if (var.getId() == null || var.getId() <= 0L) {
					// Asignamos la factura a la que corresponde y su auditoria
					var.setIdFactura(this.pojoFactura);
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
					
					// Guadamos en la BD y actualizamos el ID que le corresponde
					var.setId(this.ifzFacturaDetalle.save(var));
					log.info("Concepto guardado, comprobando impuestos ... ");
				} else {
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
					
					// Actualizamos el detalle en la BD
					this.ifzFacturaDetalle.update(var);
					log.info("Concepto actualizado, comprobando impuestos ... ");
				}
				
				if (this.listMapDetalleImpuestos.containsKey(var.getIdConcepto().getId())) {
					if (this.listDetalleImpuestos == null)
						this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
					this.listDetalleImpuestos.clear();
					
					this.listDetalleImpuestos.addAll(this.listMapDetalleImpuestos.get(var.getIdConcepto().getId()));
					if (this.listDetalleImpuestos != null && ! this.listDetalleImpuestos.isEmpty()) {
						pojoDetalleAux = this.ifzFacturaDetalle.convertir(var);
						
						// Guardamos los impuestos
						log.info("Guardando Impuestos de Concepto ... ");
						for (FacturaDetalleImpuestoExt imp : this.listDetalleImpuestos) {
							imp.setIdFacturaDetalle(pojoDetalleAux);
							imp.setModificadoPor(this.usuarioId);
							imp.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
							
							if (imp.getId() == null || imp.getId() <= 0L) {
								imp.setCreadoPor(this.usuarioId);
								imp.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
								imp.setId(this.ifzFacDetImpuestos.save(imp));
								continue;
							} 
							
							this.ifzFacDetImpuestos.update(imp);
						}
						
						log.info("Impuestos de Concepto guardados.");
					}
				}
			}
			
			// Comprobamos y eliminamos los detalles previamente borrados de la factura
			if (this.listDetallesEliminados != null && !this.listDetallesEliminados.isEmpty()) {
				log.info("Eliminando detalles previamente borrados ... ");
				for (FacturaDetalleExt var: this.listDetallesEliminados) {
					if (var.getId() == null || var.getId() <= 0L)
						continue;
					
					// Borramos de la BD el detalle de factura.
					this.ifzFacturaDetalle.delete(var);
				}
				
				// Limpiamos el listado de detalles eliminados
				this.listDetallesEliminados.clear();
				log.info("Detalles eliminados.");
			}
			
			// Comprobamos y eliminamos los impuestos previamente borrados de los conceptos
			if (this.listDetalleImpuestosEliminados != null && ! this.listDetalleImpuestosEliminados.isEmpty()) {
				log.info("Eliminando impuestos de detalles previamente borrados ... ");
				for (FacturaDetalleImpuestoExt var : this.listDetalleImpuestosEliminados) {
					if (var.getId() == null || var.getId() <= 0L)
						continue;
					
					// Borramos de la BD el detalle de factura.
					this.ifzFacDetImpuestos.delete(var.getId());
				}

				// Limpiamos el listado de impuestos eliminados
				this.listDetalleImpuestosEliminados.clear();
				log.info("Impuestos eliminados.");
			}
			
			this.puedeTimbrar = true;
			this.facturaTimbrada = false;
			if (this.pojoFactura.getUuid() != null && ! "".equals(this.pojoFactura.getUuid())) {
				this.facturaTimbrada = true;
				this.puedeTimbrar = false;
			}

			log.info("Factura guardada: " + this.pojoFactura.getId());
			control();
		} catch(Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.guardar", e);
			control(true);
			return;
		} finally {
			this.refacturar = false;
			this.procesando = false;
		}
	}
	
	public void cancelar() {
		Respuesta respuesta = null;
		FactElectv33 timbre33 = null;
		
		try {
			control();
			if (this.procesando)
				return;
			this.procesando = true;
			if (this.idFacturaBorrar > 0L) {
			//if (this.pojoFacturaBorrar != null && this.pojoFacturaBorrar.getId() > 0L) {
				if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L || this.idFacturaBorrar != this.pojoFactura.getId().longValue())
					this.pojoFactura = this.ifzFactura.findExtById(this.idFacturaBorrar);
				log.info("Proceso de Cancelacion de Factura. " + this.pojoFactura.getId() + " : " + this.pojoFactura.getFolioFactura() + " ... ");
				//log.info("Proceso de Cancelacion de Factura. " + this.pojoFacturaBorrar.getId() + " : " + this.pojoFactura.getFolioFactura() + " ... ");
				
				// Extendemos la factura
				if (this.isDebug)
					log.info("Extendiendo factura ... ");
				/*if (! this.cancelacionConvertida)
					this.pojoFactura = this.ifzFactura.convertir(this.pojoFacturaBorrar);*/
				if (this.isDebug)
					log.info("Factura extendida ... ");
				
				// Cancelamos timbre si corresponde
				if (this.pojoFactura.getUuid() != null && ! "".equals(this.pojoFactura.getUuid().trim())) {
					if (validaRequest("CFDI_TEST", null)) {
						if ("3.3".equals(this.pojoFactura.getVersion())) {
							log.info("Cancelando Timbre : " + this.pojoFactura.getUuid() + " ... ");
							timbre33 = new FactElectv33();
							timbre33.setDebugging(this.isDebug);
							timbre33.setTesting(true);//(validaRequest("CFDI_TEST", "1"));
							timbre33.setDigestMethodSHA1();
							timbre33.setSoapRequestVersion11();
							
							if (! timbre33.cancelar(this.pojoFactura, this.usuarioId)) {
								log.error("Ocurrio un problema al intentar Cancelar el CFDI ... " + timbre33.getErrorCodigo() + " - " + timbre33.getErrorDescripcion());
								if (timbre33.getError().contains("CadenaOriginal:"))
									control("No se pudo Cancelar la Factura.\n" + timbre33.getError().substring(0, timbre33.getError().indexOf("CadenaOriginal:")).trim());
								else
									control("No se pudo Cancelar la Factura.\n" + timbre33.getError());
								return;
							}
						} else if ("3.2".equals(this.pojoFactura.getVersion())) {
							// NOT IMPLEMENTED: do nothing
							log.error("NOT IMPLEMENTED YET !!!");
						}
					}
				}
				
				// Actualizamos la factura
				log.info("Cancelando Factura " + this.pojoFactura.getFolioFactura() + " ... ");
				if (this.pojoFactura.getObservaciones() != null && ! "".equals(this.pojoFactura.getObservaciones().trim()))
					this.pojoFactura.setObservaciones(this.pojoFactura.getObservaciones() + " | CANCELACION: " + this.mensajeCancelacion);
				else
					this.pojoFactura.setObservaciones("CANCELACION: " + this.mensajeCancelacion);
				respuesta = this.ifzFactura.cancelarFactura(this.pojoFactura, this.usuarioId);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					log.error("Ocurrio un problema al intentar Cancelar la factura " + this.pojoFactura.getId());
					control("Ocurrio un problema al intentar Cancelar la Factura seleccionada.");
					return;
				}

				// Actualizamos en la lista
				if (this.isDebug)
					log.info("Factura cancelada. Actualizando listado de facturas ... ");
				for (Factura var : this.listFacturas) {
					if (var.getId() == this.pojoFactura.getId()) {
						var.setEstatus(this.pojoFactura.getEstatus());
						break;
					}
				}
				
				//this.pojoFacturaBorrar = null;
				log.info("Proceso cancelacion terminado");
				
				// Refacturamos si corresponde
				if (this.refacturar) {
					log.info("14 - Factura Cancelada");
					refacturar();
				} else {
					this.pojoFactura = new FacturaExt();
					this.pojoFactura.setIdMoneda(this.idMonedaSugerida);
					this.pojoFactura.setIdFormaPago(this.idFormaPagoSugerido);
					log.info("14 - Factura Cancelada");
					control(14);
				}
			}
			
			this.mensajeCancelacion = "";
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.eliminar.", e);
			control(true);
		} finally {
			this.procesando = false;
		}
	}
	
	public void refacturar() {
		try {
			control();
			log.info("Refacturando ... ");
			// Simulo edicion para recuperar conceptos e impuestos
			editar();
			
			// Reinicio valores de factura
			this.puedeTimbrar = false;
			this.facturaTimbrada = false;
			this.idMetodoPago = this.idMetodoPagoSugerido;
			this.fechaEmision = Calendar.getInstance(this.timeZone).getTime();
			
			this.pojoFactura.setId(0L);
			this.pojoFactura.setUuid("");
			this.pojoFactura.setSerie("");
			this.pojoFactura.setFolio("");
			this.pojoFactura.setFolioFactura("");
			evaluaFacturaFolio();
			
			// Desligamos conceptos de la factura para que se agreguen como nuevos
			for (FacturaDetalleExt var : this.listDetalles)
				var.setIdFactura(null);
			log.info("Proceso refacturacion terminado");
		} catch (Exception e) {
			log.error("Error en refacturar()", e);
			control(true);
		}
	}
	
	public void noCancelar() {
		try {
			// Borro lo respaldo (Factura)
			this.refacturar = false;
			this.idFacturaBorrar = 0L;
			//nuevo();
		} catch (Exception e) {
			log.error("Error en noCancelar()", e);
			control(true);
		}
	}
	
	public void noRefacturar() {
		try {
			// Borro lo respaldo (Factura)
			this.refacturar = false;
			this.idFacturaBorrar = 0L;
			//nuevo();
		} catch (Exception e) {
			log.error("Error en noRefacturar()", e);
			control(true);
		}
	}
	
	public void imprimir() {
		HashMap<String, Object> paramsReporte = null;
		HashMap<String, String> params = null;
		Respuesta respuesta = null;
		String nombreDocumento = "";
		
		try {
			control();
			log.info("Imprimiendo reporte ... ");
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				log.info("Reporte abortado: El POJO Factura es nulo");
				control("No se pudo recuperar la Factura para su impresion");
				return;
			}
			
			// Parametros para la ejecucion del reporte
			if (! "".equals(this.pojoFactura.getFolioFactura()))
				nombreDocumento = "F-" + this.pojoFactura.getFolioFactura() + ".pdf";
			else
				nombreDocumento = "F-" + this.pojoFactura.getId() + ".pdf";
			
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  "168");
			params.put("nombreDocumento", nombreDocumento);
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.usuario);

			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idFactura", this.pojoFactura.getId());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte, null);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.info("ERROR INTERNO: No se pudo procesar el reporte de Factura.\n" + respuesta.getErrores().getDescError());
				control("Ocurrio un problema y No se pudo procesar el reporte de Factura.\nContacte a su Administrador");
				return;
			}

			this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
			this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte")); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento); 
			log.info("Reporte lanzado");
    	} catch (Exception e) {
    		log.error("Error en CuentasPorCobrar.FacturaAction.enviarFactura", e);
    		control(true);
    	}
	}
	
	public void recuperarXML() {
		String nombreDocumento = "";
		byte[] contenidoDocumento = null;
		
		try {
			control();
			log.info("Descargando XML ... ");
			if (this.pojoFactura == null || this.pojoFactura.getUuid() == null || "".equals(this.pojoFactura.getUuid().trim())) {
				log.info("Descarga abortada");
				return;
			}

			nombreDocumento = "F-" + this.pojoFactura.getFolioFactura() + ".xml";
			contenidoDocumento = this.pojoFactura.getCfdi();

			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", "xml");
			log.info("Descarga lanzada");
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el XML de la factura", e);
			control(true);
		}
	}
	
	public void totalizar() {
		this.subtotal = 0;
		this.impuestos = 0;
		this.retenciones = 0;
		this.total = 0;
		
		if (this.isDebug)
			log.info("Totalizando ... ");

		if (this.listDetalles == null)
			this.listDetalles = new ArrayList<FacturaDetalleExt>();
		if (this.listDetalles.isEmpty()) {
			log.info("Factura sin conceptos");
			return;
		}
		
		for(FacturaDetalleExt var : this.listDetalles) {
			this.subtotal 	 += var.getImporte().doubleValue();
			this.impuestos   += var.getImpuestos().doubleValue();
			this.retenciones += var.getRetenciones().doubleValue();
		}
		
		this.total = this.subtotal + (this.impuestos - this.retenciones);
		this.total = Utilerias.redondear(this.total, 2);
		if (this.isDebug)
			log.info("Totalizado terminado");
	}
	
	public void evaluaCancelar() {
		try {
			control();
			log.info("Preparando para Cancelar ... ");
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				log.info("ERROR INTERNO - No se pudo recuperar la Factura seleccionada"); 
				control("No se pudo recuperar la Factura seleccionada");
				return;
			}

			this.idFacturaBorrar = this.pojoFactura.getId();
			/*if (this.isDebug)
				log.info("Convirtiendo extendido a Factura para Cancelar ... ");
			this.pojoFacturaBorrar = this.ifzFactura.convertir(this.pojoFactura);
			this.cancelacionConvertida = true;*/
			log.info("Proceso terminado!");
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.evaluaCancelar", e);
			control(true);
		}
	}

	public void validarCondicionesPago() {
		if (this.pojoFactura == null) {
			this.setTipoFactura(false);
			return;
		}
		
		if ("X".equals(this.pojoFactura.getTipo())) {
			this.pojoFactura.setCondicionesPago("Contado");
			
			for (FormasPagos var : this.listFormasPago) {
				if ("01".equals(var.getClaveSat())) {
					this.pojoFactura.setIdFormaPago(var.getId());
					break;
				}
			}
			
			for (ConValores mp : this.listMetodosPago) {
				if ("PUE".equals(mp.getValor())) {
					this.idMetodoPago = mp.getId();
					break;
				}
			}
		} else {
			for (FormasPagos var : this.listFormasPago) {
				if ("99".equals(var.getClaveSat())) {
					this.pojoFactura.setIdFormaPago(var.getId());
					break;
				}
			}
			
			for (ConValores mp : this.listMetodosPago) {
				if ("PPD".equals(mp.getValor())) {
					this.idMetodoPago = mp.getId();
					break;
				}
			}
		}
	}
	
	public void cargarCFDIUsos() {
		try {
    		control();
    		if (this.isDebug)
				log.info("Recuperando TIPOS DE USO PARA CFDI ... ");
			if (this.listCFDIUso == null)
				this.listCFDIUso = new ArrayList<ConValores>();
			this.listCFDIUso.clear();

			if (this.listCFDIUsoItems == null)
				this.listCFDIUsoItems = new ArrayList<SelectItem>();
			this.listCFDIUsoItems.clear();
			
			this.listCFDIUso = this.ifzConValores.findAll(this.grupoCFDIUsos, "valor", 0);
			if (this.listCFDIUso == null || this.listCFDIUso.isEmpty()) {
	    		log.info("Sin TIPOS DE USO PARA CFDI");
				return;
			}
			
			for (ConValores var : this.listCFDIUso) {
				if ("P01".equals(var.getValor()))
					this.idUsoCfdiSugerido = var.getId();
				this.listCFDIUsoItems.add(new SelectItem(var.getId(), var.getValor() + " - " + var.getDescripcion()));
			}
			if (this.isDebug)
				log.info(this.listCFDIUso.size() + " TIPOS DE USO PARA CFDI recuperandos");
		} catch (Exception e) {
    		log.error("Error en CuentasPorCobrar.FacturaAction.cargarCFDIUsos", e);
    		control(true);
		}
	}

	public void cargarCFDITiposRelacion() {
		try {
    		control();
    		if (this.isDebug)
				log.info("Recuperando TIPOS DE RELACION PARA CFDI ... ");
			if (this.listCFDITipoRelacion == null)
				this.listCFDITipoRelacion = new ArrayList<ConValores>();
			this.listCFDITipoRelacion.clear();

			if (this.listCFDITipoRelacionItems == null)
				this.listCFDITipoRelacionItems = new ArrayList<SelectItem>();
			this.listCFDITipoRelacionItems.clear();
			
			this.listCFDITipoRelacion = this.ifzConValores.findAll(this.grupoCFDITipoRelacion, "valor", 0);
			if (this.listCFDITipoRelacion == null || this.listCFDITipoRelacion.isEmpty()) {
	    		log.info("Sin TIPOS DE RELACION PARA CFDI");
				return;
			}
			
			for (ConValores var : this.listCFDITipoRelacion)
				this.listCFDITipoRelacionItems.add(new SelectItem(var.getId(), var.getValor() + " - " + var.getDescripcion()));
			if (this.isDebug)
				log.info(this.listCFDITipoRelacion.size() + " TIPOS DE RELACION PARA CFDI recuperandos");
		} catch (Exception e) {
    		log.error("Error en CuentasPorCobrar.FacturaAction.cargarCFDITiposRelacion", e);
    		control(true);
		}
	}

	public void cargarCFDITiposComprobante() {
		try {
    		control();
    		if (this.isDebug)
				log.info("Recuperando TIPOS DE COMPROBANTE PARA CFDI ... ");
			if (this.listCFDITipoComprobante == null)
				this.listCFDITipoComprobante = new ArrayList<ConValores>();
			this.listCFDITipoComprobante.clear();

			if (this.listCFDITipoComprobanteItems == null)
				this.listCFDITipoComprobanteItems = new ArrayList<SelectItem>();
			this.listCFDITipoComprobanteItems.clear();
			
			this.listCFDITipoComprobante = this.ifzConValores.findAll(this.grupoCFDITipoComprobante, "valor", 0);
			if (this.listCFDITipoComprobante == null || this.listCFDITipoComprobante.isEmpty()) {
	    		log.info("Sin TIPOS DE COMPROBANTE PARA CFDI");
				return;
			}
			
			for (ConValores var : this.listCFDITipoComprobante) {
				if ("I".equals(var.getValor()))
					this.idTipoComprobanteSugerido = var.getId();
				this.listCFDITipoComprobanteItems.add(new SelectItem(var.getId(), var.getValor() + " - " + var.getDescripcion()));
			}
			if (this.isDebug)
				log.info(this.listCFDITipoComprobante.size() + " TIPOS DE COMPROBANTE PARA CFDI recuperandos");
		} catch (Exception e) {
    		log.error("Error en CuentasPorCobrar.FacturaAction.cargarCFDITiposComprobante", e);
    		control(true);
		}
	}
	
	public void cargarFormasPagos() {
		try {
    		control();
			if (this.listFormasPagoItems == null)
				this.listFormasPagoItems = new ArrayList<SelectItem>();
			this.listFormasPagoItems.clear();
			
			this.listFormasPago = this.ifzFormasPago.findAll("claveSat");
			if (!this.listFormasPago.isEmpty()) {
				for (FormasPagos var : this.listFormasPago) {
					if ("99".equals(var.getClaveSat()))
						this.idFormaPagoSugerido = var.getId();
					this.listFormasPagoItems.add(new SelectItem(var.getId(), var.getClaveSat() + " - " + var.getFormaPago()));
				}
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.cargarFormasPagos", e);
    		control(true);
		}
	}

	public void cargarMetodosPagos() {
		try {
    		control();
    		if (this.isDebug)
				log.info("Recuperando METODOS DE PAGO ... ");
			if (this.listMetodosPago == null)
				this.listMetodosPago = new ArrayList<ConValores>();
			this.listMetodosPago.clear();

			if (this.listMetodosPagoItems == null)
				this.listMetodosPagoItems = new ArrayList<SelectItem>();
			this.listMetodosPagoItems.clear();
			
			this.listMetodosPago = this.ifzConValores.findAll(this.pojoGpoMetodosPago, "valor", 0);
			if (this.listMetodosPago == null || this.listMetodosPago.isEmpty()) {
	    		log.info("Sin METODOS DE PAGO");
				return;
			}
			
			for (ConValores var : this.listMetodosPago) {
				if ("PPD".equals(var.getValor()))
					this.idMetodoPagoSugerido = var.getId();
				this.listMetodosPagoItems.add(new SelectItem(var.getId(), var.getValor() + " - " + var.getDescripcion()));
			}
			if (this.isDebug)
				log.info(this.listMetodosPago.size() + " METODOS DE PAGO recuperandos");
		} catch (Exception e) {
    		log.error("Error en CuentasPorCobrar.FacturaAction.cargarMetodosPagos", e);
    		control(true);
		}
	}

	public void cargarSucursales() throws Exception {
		try {
			control();
			if (this.isDebug)
				log.info("Cargando sucursales");
			if (this.listBusquedaSucursal == null)
				this.listBusquedaSucursal = new ArrayList<Sucursal>();
			this.listBusquedaSucursal.clear();
			
			if (this.listBusquedaSucursalItems == null)
				this.listBusquedaSucursalItems = new ArrayList<SelectItem>();
			this.listBusquedaSucursalItems.clear();

			this.listBusquedaSucursal = this.ifzSucursales.buscarSucursales("sucursal", "");
			if (this.listBusquedaSucursal == null || this.listBusquedaSucursal.isEmpty()) {
				log.info("Ninguna sucursal encontrada");
				return;
			}

			for (Sucursal var : this.listBusquedaSucursal) {
				if ("loreto".equals(var.getSucursal().trim().toLowerCase()))
					continue;
				this.listBusquedaSucursalItems.add(new SelectItem(var.getId(), var.getSucursal()));
			}
			if (this.isDebug)
				log.info(this.listBusquedaSucursal.size() + " sucursales encontradas");
		} catch (Exception e) {
    		log.error("Error en cargarSucursales", e);
			control(true);
		}
	}

	private void cargarMonedas() {
		try {
			if (this.listMonedasItems == null)
				this.listMonedasItems = new ArrayList<SelectItem>();
			this.listMonedasItems.clear();
			
			this.listMonedas = this.ifzMonedas.findAll();
			if (this.listMonedas != null && ! this.listMonedas.isEmpty()) {
				for (Moneda mon : this.listMonedas)
					this.listMonedasItems.add(new SelectItem(mon.getId(), mon.getNombre() + " (" + mon.getAbreviacion() + ")"));
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.cargarMonedas", e);
		}
	}

	private String formatoFolio(String folio) {
		int iteracion = 0;
		
		if (folio.length() < this.MAX_LENGTH_FOLIO_FACTURA) {
			iteracion = (this.MAX_LENGTH_FOLIO_FACTURA - folio.length());
			for(int x = 0; x < iteracion; x++) {
				folio = "0" + folio;
			}
		}
		
		return folio;
	}
	
	private int valueLengthFolioFactura(String value, int defaultValue) {
		try {
			return Integer.parseInt(value.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	private boolean validarFactura() {
		SimpleDateFormat formatter = null;
		Moneda monedaFactura = null;
		MonedasValores valTipoCambio = null;
		Respuesta respuesta = null;
		Sucursal pojoSucursal = null;
		String dateStr = "";
		long folio = 0;
		
		log.info("Validando Detalles (Conceptos) ... ");
		if (this.listDetalles.isEmpty()) {
			log.info("ERROR 3: Factura sin detalles");
			control(3);
			return false;
		}

		log.info("Validando Metodo de Pago ... ");
		if (this.idMetodoPago <= 0L) {
			log.info("ERROR INTERNO: No se asigno ninguno Metodo de Pago a la Factura");
			control("Debe indicar una Metodo de Pago");
			return false;
		}

		log.info("Validando Moneda ... ");
		if (this.pojoFactura.getIdObra() == null || this.pojoFactura.getIdObra().getId() == null || this.pojoFactura.getIdObra().getId() <= 0L) {
			log.info("ERROR INTERNO: No se asigno ninguno Obra a la Factura");
			control("Debe indicar una Obra");
			return false;
		}

		log.info("Validando Sucursal ... ");
		if (this.sucursalId <= 0L) {
			log.info("ERROR INTERNO: No se asigno ninguno Sucursal a la Factura");
			control("Debe indicar una Sucursal");
			return false;
		} else {
			log.info("Obteniendo Sucursal seleccionada: " + this.sucursalId);
			for (Sucursal var : this.listBusquedaSucursal) {
				if (this.sucursalId != var.getId())
					continue;
				pojoSucursal = var;
				break;
			}
			
			if (pojoSucursal == null) {
				log.info("ERROR INTERNO: No se asigno ninguno Sucursal a la Factura");
				control("Debe indicar una Sucursal");
				return false;
			}
		}

		log.info("Comprobando MetodoDePago ... ");
		if (this.pojoFactura.getIdMetodoPago() == null || this.pojoFactura.getIdMetodoPago().getId() <= 0L || this.idMetodoPago != this.pojoFactura.getIdMetodoPago().getId()) {
			log.info("Asignando MetodoDePago: " + this.monedaBase.getNombre());
			for (ConValores var : this.listMetodosPago) {
				if (this.idMetodoPago == var.getId()) {
					this.pojoFactura.setIdMetodoPago(var);
					break;
				}
			}
		}

		log.info("Comprobando Moneda ... ");
		if ((this.pojoFactura.getIdMoneda() == null || this.pojoFactura.getIdMoneda() <= 0L) && this.monedaBase != null) {
			log.info("Asignando Moneda: " + this.monedaBase.getNombre());
			this.pojoFactura.setIdMoneda(this.monedaBase.getId());
			this.pojoFactura.setDescripcionMoneda(this.monedaBase.getNombre());
			this.pojoFactura.setAbreviaturaMoneda(this.monedaBase.getAbreviacion());
		} else {
			for (Moneda var : this.listMonedas) {
				if (this.pojoFactura.getIdMoneda().longValue() == var.getId()) {
					monedaFactura = var;
					this.pojoFactura.setIdMoneda(var.getId());
					this.pojoFactura.setDescripcionMoneda(var.getNombre());
					this.pojoFactura.setAbreviaturaMoneda(var.getAbreviacion());
					break;
				}
			}
		}

		log.info("Comprobando Tipo de Cambio ... ");
		if (this.pojoFactura.getIdMoneda().longValue() != this.monedaBase.getId()) {
			try {
				valTipoCambio = this.ifzMonValores.findActual(this.monedaBase, monedaFactura);
				this.pojoFactura.setTipoCambio(valTipoCambio.getValor().doubleValue());
			} catch (Exception e) {
				log.info("ERROR INTERNO: Ocurrio un problema al intentar recuperar el Tipo de Cambio para la moneda seleccionada", e);
				control("Ocurrio un problema al intentar asignar el Tipo de Cambio para la moneda seleccionada");
				return false;
			}
		}

		log.info("Comprobando Cliente ... ");
		if (! validaDatosCliente()) {
			log.info("Asignando Cliente: " + this.pojoFactura.getIdObra().getIdCliente().getId() + " - " + this.pojoObra.getNombreCliente());
			asignarDatosCliente();
		}

		log.info("Comprobando Sucursal ... ");
		if (this.pojoFactura.getIdSucursal() == null || this.pojoFactura.getIdSucursal().getId() <= 0L) {
			log.info("Asignando Sucursal: " + pojoSucursal.getId() + " - " + pojoSucursal.getSucursal());
			this.pojoFactura.setIdSucursal(pojoSucursal);
			this.pojoFactura.setNombreSucursal(pojoSucursal.getSucursal());
		}
		
		log.info("Comprobando Serie ... ");
		if (this.pojoFactura.getSerie() == null || "".equals(this.pojoFactura.getSerie())) {
			if (pojoSucursal.getSerie() == null || "".equals(pojoSucursal.getSerie().trim())) {
				log.info("ERROR 9: Sucursal sin serie y/o folio asignadas para facturacion");
				control(9);
				return false;
			} 
			
			// Asigno Serie tomada desde Sucursal
			this.pojoFactura.setSerie(pojoSucursal.getSerie());
		}

		log.info("Comprobando Folio ... ");
		if (this.pojoFactura.getFolio() == null || "".equals(this.pojoFactura.getFolio()) || "0".equals(this.pojoFactura.getFolio())) {
			// Asignamos folio obtenida de la sucursal
			try {
				log.info("Obteniendo Serie y Folio desde Sucursal ... " + pojoSucursal.getId() + " - " + pojoSucursal.getSucursal());
				respuesta = this.ifzSucursales.folioFacturacion(pojoSucursal);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					if (respuesta.getErrores().getCodigoError() == -1) {
						log.info("ERROR 8: Sucursal no existe");
						control(8);
						return false;
					}
					
					if (respuesta.getErrores().getCodigoError() == -2) {
						log.info("ERROR 9: La Sucursal no tiene asignada Serie para facturacion");
						control(9);
						return false;
					}
				}
				
				folio = (Long) respuesta.getBody().getValor("folioFacturacion");
				log.info("Asignando Serie y Folio: " + this.pojoFactura.getSerie() + "-" + folio);
				this.pojoFactura.setFolio(formatoFolio(String.valueOf(folio)));
			} catch (Exception e) {
				log.info("ERROR INTERNO: Ocurrio un problema al intentar recuperar el Folio para la sucursal seleccionada", e);
				control("Ocurrio un problema al intentar asignar un folio de la Sucursal(Serie) seleccionada");
				return false;
			}
		} else {
			// Comprobamos Folio
			try {
				List<Factura> lista = this.ifzFactura.comprobarFolioFacturacion(this.pojoFactura.getSerie(), this.pojoFactura.getFolio());//.findByProperty("folio", this.pojoFactura.getFolio());
				if (lista != null && ! lista.isEmpty()) {
					if (this.pojoFactura.getId() == null || this.pojoFactura.getId().longValue() <= 0L) {
						log.info("ERROR INTERNO: El Folio indicado ya esta asignado a otra Factura");
						control("El Folio indicado ya esta asignado a otra Factura");
						return false;
					}
					
					for (Factura var : lista) {
						if (var.getEstatus() != 1)
							continue;
						if (this.pojoFactura.getId().longValue() == var.getId().longValue())
							continue;
						if (var.getSerie() != null && ! this.pojoFactura.getSerie().equals(var.getSerie()))
							continue;
						log.info("ERROR INTERNO: El Folio indicado ya esta asignado a otra Factura");
						control("El Folio indicado ya esta asignado a otra Factura");
						return false;
					}
				}
			} catch (Exception e) {
				log.warn("No se pudo conprobar el Folio de la Factura", e);
			}
		}
		
		if (this.pojoFactura.getTipo() == null || "".equals(this.pojoFactura.getTipo())) {
			this.pojoFactura.setTipo("X");
			this.pojoFactura.setCondicionesPago("Contado");
		}
		
		// Asigno condiciones de pago si corresponde
		if ("C".equals(this.pojoFactura.getTipo()) && (this.pojoFactura.getCondicionesPago() == null || "".equals(this.pojoFactura.getCondicionesPago())))
			this.pojoFactura.setCondicionesPago("0");
		
		// Asigno empresa si corresponde
		if (this.pojoFactura.getIdEmpresa() == null && this.empresaId > 0L)
			this.pojoFactura.setIdEmpresa(this.ifzEmpresa.findById(this.empresaId));
		
		try {
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			dateStr = formatter.format(this.fechaEmision);
			formatter.applyPattern("HH:mm:ss");
			dateStr = dateStr + " " + formatter.format(Calendar.getInstance(this.timeZone).getTime());
			formatter.applyPattern("dd/MM/yyyy HH:mm:ss");
			this.fechaEmision = formatter.parse(dateStr);
			this.pojoFactura.setFechaEmision(this.fechaEmision);
			log.info("Fecha de emision asignada: " + dateStr);
		} catch (Exception e) {
			log.warn("No se pudo añadir la hora a la fecha de emision: " + dateStr, e);
		}
		
		if (! this.isDebug || (this.isDebug && ! this.paramsRequest.containsKey("NO_FOLIO"))) {
			if ("".equals(this.pojoFactura.getSerie()) && "".equals(this.pojoFactura.getFolio())) {
				log.info("ERROR 12: Factura sin Serie y/o Folio asignados");
				control(12);
				return false;
			}
		} 
		
		return true;
	}
	
	private boolean validaDatosCliente() {
		boolean dataClientValid = true;

		log.info("Validando datos de Cliente ... ");
		if (this.pojoFactura.getIdCliente() == null || this.pojoFactura.getIdCliente() <= 0L) {
			log.info("Validando datos de Cliente ... INVALIDO: Cliente no asignado");
			dataClientValid = false;
		} else if (this.pojoFactura.getRfc() == null || "".equals(this.pojoFactura.getRfc())) {
			log.info("Validando datos de Cliente ... INVALIDO: RFC Cliente vacio");
			dataClientValid = false;
		} else if (! this.pojoFactura.getRfc().equals(this.pojoFactura.getIdObra().getIdCliente().getRfc())) {
			log.info("Validando datos de Cliente ... INVALIDO: RFC Cliente distinto");
			dataClientValid = false;
		} else if (this.pojoFactura.getCliente() == null || "".equals(this.pojoFactura.getCliente())) {
			log.info("Validando datos de Cliente ... INVALIDO: Nombre Cliente vacio");
			dataClientValid = false;
		} else if (! this.pojoFactura.getCliente().equals(this.pojoFactura.getIdObra().getIdCliente().getNombre())) {
			log.info("Validando datos de Cliente ... INVALIDO: Nombre Cliente distinto");
			dataClientValid = false;
		} else if (this.pojoFactura.getDomicilio() == null) {
			log.info("Validando datos de Cliente ... INVALIDO: Nombre Cliente distinto");
			dataClientValid = false;
		} else if ("".equals(this.pojoFactura.getDomicilio().trim())) {
			log.info("Validando datos de Cliente ... INVALIDO: Nombre Cliente distinto");
			dataClientValid = false;
		}

		log.info("Validando datos de Cliente ... CORRECTO! ");
		return dataClientValid;
	}
	
	private void asignarDatosCliente() {
		Respuesta respDomicilio = null;
		
		if (this.pojoFactura == null)
			return;
		
		try {
			control();
			log.info("Asignando datos de Cliente");
			this.pojoFactura.setIdCliente(this.pojoFactura.getIdObra().getIdCliente().getId());
			this.pojoFactura.setCliente(this.pojoFactura.getIdObra().getIdCliente().getNombre());
			this.pojoFactura.setRfc(this.pojoFactura.getIdObra().getIdCliente().getRfc());
			this.pojoFactura.setTipoCliente(this.pojoFactura.getIdObra().getTipoCliente());
			this.pojoFactura.setIdBeneficiario(this.pojoFactura.getIdObra().getIdCliente().getId());
			this.pojoFactura.setBeneficiario(this.pojoFactura.getIdObra().getIdCliente().getNombre());
			this.pojoFactura.setTipoBeneficiario(this.pojoFactura.getIdObra().getTipoCliente());
			if (this.pojoFactura.getTipoBeneficiario() == null || "".equals(this.pojoFactura.getTipoBeneficiario())) {
				if (this.pojoFactura.getIdObra().getIdCliente().getTipoPersona() == 1L)
					this.pojoFactura.setTipoBeneficiario("P");
				else
					this.pojoFactura.setTipoBeneficiario("N");
				this.pojoFactura.setTipoCliente(this.pojoFactura.getTipoBeneficiario());
			}
			
			log.info("Asignando domicilio de Cliente");
			respDomicilio = new Respuesta();
			if ("P".equals(this.pojoFactura.getTipoBeneficiario()))
				respDomicilio = this.ifzPersonas.buscarDomicilio(this.pojoFactura.getIdCliente());
			else
				respDomicilio = this.ifzNegocios.buscarDomicilio(this.pojoFactura.getIdCliente());
			if (respDomicilio.getErrores().getCodigoError() != 0) {
				log.info(respDomicilio.getErrores().getCodigoError() + " - " + respDomicilio.getErrores().getDescError());
				return;
			}

			this.pojoFactura.setDomicilio((String) respDomicilio.getBody().getValor("domicilio"));
			this.pojoFactura.setNoExterno((String) respDomicilio.getBody().getValor("numero_exterior"));
			this.pojoFactura.setNoInterno((String) respDomicilio.getBody().getValor("numero_interior"));
			this.pojoFactura.setColonia((String) respDomicilio.getBody().getValor("colonia"));
			this.pojoFactura.setCp(Integer.parseInt((String) respDomicilio.getBody().getValor("codigo_postal")));
			this.pojoFactura.setCiudad((String) respDomicilio.getBody().getValor("ciudad"));
			this.pojoFactura.setMunicipio((String) respDomicilio.getBody().getValor("municipio"));
			this.pojoFactura.setEstado((String) respDomicilio.getBody().getValor("estado"));
			this.pojoFactura.setPais((String) respDomicilio.getBody().getValor("pais"));
		} catch (Exception e) {
    		log.error("Error en asignarDatosCliente", e);
			control(true);
		}
	}
	
	private boolean validaRequest(String param, String refValue) {
		if (! this.isDebug)
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		if (refValue == null || "".equals(refValue.trim()))
			return true;
		
		return (refValue.trim().equals(this.paramsRequest.get(param).trim()));
	}
	
	public void autoAsignaFormaPago() {
		long fPago = 0L;
		String formaPago = "";
		String formaPagoPrev = "";
		
		if (this.idMetodoPago <= 0L)
			return;
		
		for (ConValores mp : this.listMetodosPago) {
			if (this.idMetodoPago == mp.getId()) {
				formaPago = "01";
				if ("PPD".equals(mp.getValor()))
					formaPago = "99";
			}
		}
		
		for (FormasPagos var : this.listFormasPago) {
			if (formaPago.equals(var.getClaveSat()))
				fPago = var.getId();
			if (this.pojoFactura.getIdFormaPago().longValue() == var.getId().longValue())
				formaPagoPrev = var.getClaveSat();
		}
		
		if (! formaPagoPrev.equals(formaPago))
			this.pojoFactura.setIdFormaPago(fPago);
	}
	
	public void autoAsignaMetodoPago() {
		String metodoPago = "";
		
		if (this.pojoFactura == null || this.pojoFactura.getIdFormaPago() == null || this.pojoFactura.getIdFormaPago() <= 0L)
			return;
		
		for (FormasPagos var : this.listFormasPago) {
			if (this.pojoFactura.getIdFormaPago() == var.getId().longValue()) {
				metodoPago = "PUE";
				if ("99".equals(var.getClaveSat()))
					metodoPago = "PPD";
				break;
			}
		}
		
		for (ConValores mp : this.listMetodosPago) {
			if (metodoPago.equals(mp.getValor())) {
				this.idMetodoPago = mp.getId();
				break;
			}
		}
	}
	
	private void control() {
		control(false, 0, "");
	}
	
	private void control(boolean value) {
		this.procesoIncompleto = value;
		
		if (! value) {
			control(false, 0, "");
			return;
		} 

		control(true, 1, "ERROR");
	}
		
	private void control(int tipoMensaje) {
		if (tipoMensaje == 0) {
			control(false);
			return;
		}
		
		control(true, tipoMensaje, "ERROR");
	}
	
	private void control(String mensaje) {
		if (mensaje == null || "".equals(mensaje.trim())) 
			control();
		else
			control(true, -1, mensaje);
	}
	
	private void control(boolean procesoIncompleto, int tipoMensaje, String mensaje) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "ERROR";
		
		this.procesoIncompleto = procesoIncompleto;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = ((mensaje.contains("\n")) ? mensaje.trim().replace("\n", "<br/>") : mensaje.trim());
	}

	// ---------------------------------------
	// DETALLES DE FACTURA
	// ---------------------------------------

	public void nuevoConceptoFacturacion() {
		try {
			control();
			if (this.listDetalleImpuestos == null)
				this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalleImpuestos.clear();

			this.conceptoAnterior = 0L;
			this.pojoDetalle = new FacturaDetalleExt();
			this.pojoDetalle.setCantidad(new BigDecimal(1));
			this.pojoDetalle.setCosto(BigDecimal.ZERO);
		} catch (Exception e) {
			log.error("Error en nuevoConceptoFacturacion()", e);
			control(true);
		}
	}
	
	public void editarFacturaDetalleConcepto(){
		if (this.pojoDetalle.getIdConcepto() != null) {
			this.conceptoAnterior = 0L;
			this.indexDetalleFactura = -1;
			this.indexDetalleFactura = this.listDetalles.indexOf(this.pojoDetalle);
			desglozarImpuestos();
		}
	}
	
	public void guardarConcepto() {
		try {
			control();
			if (this.pojoDetalle.getCantidad().doubleValue() <= 0) {
				log.info("ERROR 10: La cantidad debe ser mayor a cero");
				control(10);
				return;
			}
			
			if (this.pojoDetalle.getCosto().doubleValue() <= 0) {
				log.info("ERROR 11: El costo debe ser mayor a cero");
				control(11);
				return;
			}
			
			if (this.pojoDetalle != null && this.pojoDetalle.getId() == null) {
				if (this.indexDetalleFactura == -1)
					this.listDetalles.add(this.pojoDetalle);
				else
					this.listDetalles.set(this.indexDetalleFactura, this.pojoDetalle);
			} else {
				for (FacturaDetalleExt var : this.listDetalles) {
					if (var.getId() == this.pojoDetalle.getId()) {
						var = this.pojoDetalle;
						break;
					}
				}
			}
			
			totalizar();
			
			this.indexDetalleFactura = -1;
			this.pojoDetalle = null;
			nuevoConceptoFacturacion();
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorCobrar.FacturaAction.guardarConcepto", e);
		}
	}
	
	public void eliminarFacturaDetalle(){
		try {
			control();
			
			if(this.listDetallesEliminados == null)
				this.listDetallesEliminados = new ArrayList<FacturaDetalleExt>();
			
			this.listDetallesEliminados.add(this.pojoDetalle);
			this.listDetalles.remove(this.pojoDetalle);

			if (this.listMapDetalleImpuestos.containsKey(this.pojoDetalle.getIdConcepto().getId())) {
				if (this.listDetalleImpuestosEliminados == null) 
					this.listDetalleImpuestosEliminados = new ArrayList<FacturaDetalleImpuestoExt>();
				this.listDetalleImpuestosEliminados.clear();
				this.listDetalleImpuestosEliminados.addAll(this.listMapDetalleImpuestos.get(this.pojoDetalle.getIdConcepto().getId()));
				this.listMapDetalleImpuestos.remove(this.pojoDetalle.getIdConcepto().getId());
			}
		
			totalizar();
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorCobrar.FacturaAction.eliminarFacturaDetalle", e);
		}
	}

	public void eliminarImpuesto() {
		if (this.pojoDetalleImpuesto != null) {
			log.info("Eliminando Impuesto ... ");
			this.listDetalleImpuestos.remove(this.pojoDetalleImpuesto);
			this.listDetalleImpuestosEliminados.add(this.pojoDetalleImpuesto);
			log.info("Impuesto eliminado. Desglosando impuestos ... ");
			
			desglozarImpuestos();
			log.info("Impuestos desglosandod");
		}
	}
	
	public void desglozarImpuestos() {
		List<ConceptoFacturacionImpuestosExt> listImpuestos = null;
		List<FacturaDetalleImpuestoExt> listAux = null;
		FacturaDetalleImpuestoExt pojoImpuesto = null;
		double total = 0D;
		Double impuesto = 0D;
		Double totalImpuestos = 0D;
		Double totalRetenciones = 0D;
		Double porImpuesto = 0D;
		Double porRetension = 0D;
		
		try {
			control();
			if (this.isDebug)
				log.info("Preparando desgloce de impuestos ... ");
			if (this.pojoDetalle.getIdConcepto().getId() == null || this.pojoDetalle.getIdConcepto().getId() <= 0L) {
				log.info("No selecciono ningun concepto");
				control("No selecciono ningun concepto");
				return;
			}
			
			if (this.conceptoAnterior == this.pojoDetalle.getIdConcepto().getId().longValue() && ! this.listDetalleImpuestos.isEmpty()) {
				log.info("Impuestos desglosados previamente");
				return;
			}
			
			if (this.listDetalleImpuestos == null)
				this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalleImpuestos.clear();
			
			// Recupero impuestos del concepto, si corresponde
			if (this.isDebug)
				log.info("Recuperando impuestos ... ");
			if (this.pojoDetalle.getId() != null && this.pojoDetalle.getId() > 0L) 
				this.listDetalleImpuestos = this.ifzFacDetImpuestos.findExtByFacturaDetalle(this.pojoDetalle);
			if (this.listDetalleImpuestos != null && ! this.listDetalleImpuestos.isEmpty()) {
				for (FacturaDetalleImpuestoExt var : this.listDetalleImpuestos) {
					impuesto = var.getMonto().doubleValue();
					if (impuesto <= 0) {
						impuesto = ((this.pojoDetalle.getImporte().doubleValue() * Double.valueOf(var.getIdImpuesto().getAtributo1())) / 100);
						var.setMonto(new BigDecimal(impuesto));
					}
					
					if ("DE".equals(var.getIdImpuesto().getTipoCuenta())) {
						porImpuesto = Double.valueOf(var.getIdImpuesto().getAtributo1());
						totalImpuestos += impuesto;
					} else {
						porRetension = Double.valueOf(var.getIdImpuesto().getAtributo1());
						totalRetenciones += impuesto;
					}
				}
			} else {
				// Recuperamos Impuesto de concepto
				listImpuestos = this.ifzConFacImpuestos.findByPropertyExt("idConceptoFacturacion", this.pojoDetalle.getIdConcepto().getId());
				if (listImpuestos == null || listImpuestos.isEmpty()) {
					if (this.listMapDetalleImpuestos.containsKey(this.pojoDetalle.getIdConcepto().getId())) {
						if (this.listDetalleImpuestosEliminados == null) 
							this.listDetalleImpuestosEliminados = new ArrayList<FacturaDetalleImpuestoExt>();
						this.listDetalleImpuestosEliminados.addAll(this.listMapDetalleImpuestos.get(this.pojoDetalle.getIdConcepto().getId()));
						this.listMapDetalleImpuestos.remove(this.pojoDetalle.getIdConcepto().getId());
					}
					
					if (this.isDebug)
						log.info("El concepto no tiene asignado ningun impuesto");
					control("El concepto no tiene asignado ningun impuesto");
					return;
				}

				// Realizando desgloce
				if (this.isDebug)
					log.info("Desglozando impuestos ... ");
				pojoImpuesto = null;
				this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
				for (ConceptoFacturacionImpuestosExt var : listImpuestos) {
					if (var.getIdImpuesto() != null && var.getIdImpuesto().getAtributo1() != null && ! "".equals(var.getIdImpuesto().getAtributo1())) {					
						impuesto = ((this.pojoDetalle.getImporte().doubleValue() * Double.valueOf(var.getIdImpuesto().getAtributo1())) / 100);
						var.setMonto(new BigDecimal(impuesto));
						if ("DE".equals(var.getIdImpuesto().getTipoCuenta())) {
							porImpuesto = Double.valueOf(var.getIdImpuesto().getAtributo1());
							totalImpuestos += impuesto;
						} else {
							porRetension = Double.valueOf(var.getIdImpuesto().getAtributo1());
							totalRetenciones += impuesto;
						}
						
						pojoImpuesto = new FacturaDetalleImpuestoExt();
						pojoImpuesto.setIdFacturaDetalle(this.ifzFacturaDetalle.convertir(this.pojoDetalle));
						pojoImpuesto.setIdImpuesto(var.getIdImpuesto());
						pojoImpuesto.setMonto(new BigDecimal(impuesto));
						pojoImpuesto.setCreadoPor(this.usuarioId);
						pojoImpuesto.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
						pojoImpuesto.setModificadoPor(this.usuarioId);
						pojoImpuesto.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
						this.listDetalleImpuestos.add(pojoImpuesto);
					}
				}
			}

			listAux = new ArrayList<FacturaDetalleImpuestoExt>();
			listAux.addAll(this.listDetalleImpuestos);
			this.listMapDetalleImpuestos.put(this.pojoDetalle.getIdConcepto().getId(), listAux);

			total = totalImpuestos - totalRetenciones;
			total = this.pojoDetalle.getImporte().doubleValue() + total;
			if (total < 0)
				total = 0;
			
			this.pojoDetalle.setPorcentajeIva(porImpuesto);
			this.pojoDetalle.setPorcentajeRetencion(porRetension);
			this.pojoDetalle.setImpuestos(new BigDecimal(totalImpuestos));
			this.pojoDetalle.setRetenciones(new BigDecimal(totalRetenciones));
			this.pojoDetalle.setTotal(new BigDecimal(total));
			if (this.isDebug)
				log.info("Desgloce de impuestos terminado");
		} catch (Exception e) {
			log.error("\nError en CuentasPorCobrar.FacturaAction.desglozarImpuestos(). No se pudo desglozar impuestos\n", e);
			control("No se pudo desglozar impuestos");
		}
	}

	// ---------------------------------------
	// BUSQUEDA CONCEPTOS DE FACTURACION
	// ---------------------------------------	
	
	public void busquedaConceptos() {
		control();
		if (this.listConceptoFacturacion == null)
			this.listConceptoFacturacion = new ArrayList<ConceptoFacturacion>();
		this.listConceptoFacturacion.clear();
		this.pojoConcepto = null;//new ConceptoFacturacion();
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
		this.valBusquedaConceptos = "";
		this.numPaginaConceptos = 1;
	}
	
	public void buscarConceptos() {
		try {
			control();
			if ("".equals(this.campoBusquedaConceptos))
				this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
			
			this.listConceptoFacturacion = this.ifzConceptosFacturacion.findLikeProperty(this.campoBusquedaConceptos, this.valBusquedaConceptos, 0);
			if (this.listConceptoFacturacion.isEmpty()) {
				log.info("No se encontro ningun concepto de facturacion con el filtro --> " + this.campoBusquedaConceptos + ":" + this.valBusquedaConceptos);
				control(2);
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.buscarConceptos", e);
			control(true);
		}
	}
	
	public void seleccionarConcepto() {
		ConValores pojoUnidadMedida = null;
		
		try {
			control();
			if (this.pojoConcepto == null || this.pojoConcepto.getId() == null || this.pojoConcepto.getId() <= 0L) {
				log.info("No selecciono ningun Concepto. POJO nulo");
				control("Debe seleccionar un Concepto");
				return;
			}
			
			// Recuperamos el pojo de Unidad de Medida
			log.info("Obteniendo Unidad Medida ... ");
			pojoUnidadMedida = this.ifzConValores.findById(this.pojoConcepto.getIdUnidadMedida());
			
			// Añadimos el concepto y desglozamos sus impuestos, si corresponde
			this.pojoDetalle.setIdConcepto(this.pojoConcepto);
			if (pojoUnidadMedida != null) {
				this.pojoDetalle.setIdUnidadMedida(pojoUnidadMedida);
				log.info("Unidad Medida asignada");
			}
			
			if (this.listDetalleImpuestos == null)
				this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalleImpuestos.clear();
			
			// Desglozamos impuestos del Concepto si corresponde
			if (this.pojoDetalle.getCosto().doubleValue() > 0)
				desglozarImpuestos();
			
			busquedaConceptos();
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorCobrar.FacturaAction.seleccionarConcepto", e);
		}
	}

	// ---------------------------------------
	// OBRAS/PROYECTOS
	// ---------------------------------------
	
	public void nuevaBusquedaObras() {
		control();
		this.campoBusquedaObras = this.opcionesBusquedaObras.get(0).getDescription();
		this.valorBusquedaObras = "";
		this.tipoBusquedaProyecto = 0;
		this.paginacionObras = 1;
		
		this.pojoObra = new Obra();
		if (this.listaObras != null && !this.listaObras.isEmpty())
			this.listaObras.clear();
	}

	public void buscarObras(){
		try {
			control();
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.opcionesBusquedaObras.get(0).getValue().toString();
			
			this.tipoBusquedaProyecto = 0;
			if (this.buscarAdministrativo)
				this.tipoBusquedaProyecto = 4;

			this.ifzObra.estatus(this.ifzObra.findEstatusCanceladoObras());
			this.listaObras = this.ifzObra.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.tipoBusquedaProyecto);
			if(listaObras.isEmpty()) {
				log.info("ERROR 2: Busqueda sin resultados. " + this.campoBusquedaObras + ": " + this.valorBusquedaObras);
				control(2);
			}
		} catch(Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.buscarProyectos", e);
			control(true);
		}
	}
	
	public void seleccionarObra() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				log.info("No se pudo recuperar la Obra seleccionada. POJO nulo");
				control("No se pudo recuperar la Obra seleccionada");
				return;
			}

			log.info("Asignando Obra: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
			this.pojoFactura.setIdObra(this.ifzObra.convertir(this.pojoObra));
			if (this.pojoObra.getIdMoneda() != null && this.pojoObra.getIdMoneda() > 0L)
				this.pojoFactura.setIdMoneda(this.pojoObra.getIdMoneda());
			else
				this.pojoFactura.setIdMoneda(this.idMonedaSugerida);

			log.info("Asignando Cliente: " + this.pojoFactura.getIdObra().getIdCliente().getId() + " - " + this.pojoObra.getNombreCliente());
			asignarDatosCliente();
			
			nuevaBusquedaObras();
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorCobrar.FacturaAction.seleccionarProyecto", e);
		} 
	}

	// ---------------------------------------
	// TIMBRAR
	// ---------------------------------------
	
	public void validarTimbrar() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		MonedasValores valTipoCambio = null;
		String fechaEmisionFactura = "";
		boolean guardar = false;
		
		try {
			control();
			log.info("Validando Factura ... ");
			if (this.pojoFactura != null) {
				if (this.pojoFactura.getSerie() == null || "".equals(this.pojoFactura.getSerie())) {
					log.info("ERROR INTERNO - Factura sin Serie");
					control("La factura indicada no tiene Serie asignada");
					return;
				}
				
				if (this.pojoFactura.getFolio() == null || "".equals(this.pojoFactura.getFolio())) {
					log.info("ERROR INTERNO - Factura sin Folio");
					control("La factura indicada no tiene Folio asignado");
					return;
				}
				
				this.cfdiRelacionado = false;
				this.cfdiRelacionadoUuid = "";
				this.idTipoComprobante = this.idTipoComprobanteSugerido;
				this.idTipoRelacion = 0;
				this.idUsoCfdi = this.idUsoCfdiSugerido;
				this.cfdiVersion = this.cfdiVersionSugerida;

				// Fecha Emision
				log.info("Compruebo fecha Emision ... ");
				if ("00:00:00".equals(formatter.format(this.pojoFactura.getFechaEmision()))) {
					formatter.applyPattern("MM-dd-yyyy");
					fechaEmisionFactura = formatter.format(this.pojoFactura.getFechaEmision());
					formatter.applyPattern("HH:mm:ss");
					fechaEmisionFactura = fechaEmisionFactura + " " + formatter.format(Calendar.getInstance(this.timeZone).getTime());
					formatter.applyPattern("MM-dd-yyyy HH:mm:ss");
					this.pojoFactura.setFechaEmision(formatter.parse(fechaEmisionFactura));
					log.info("Asigno fecha emision: " + fechaEmisionFactura + ". Pendiente actualizar ... ");
					guardar = true;
				}

				// Moneda
				log.info("Compruebo Moneda ... ");
				if (this.pojoFactura.getIdMoneda() == null || this.pojoFactura.getIdMoneda() <= 0L) {
					if (this.monedaBase != null) {
						control("Ocurrio un problema al intentar asignar la Moneda");
						return;
					}
					
					this.pojoFactura.setIdMoneda(this.monedaBase.getId());
					this.pojoFactura.setDescripcionMoneda(this.monedaBase.getNombre());
					this.pojoFactura.setAbreviaturaMoneda(this.monedaBase.getAbreviacion());
					this.pojoFactura.setTipoCambio(1.0);
					log.info("Asigno datos de Moneda. Pendiente actualizar ... ");
					guardar = true;
				} else {
					for (Moneda var : this.listMonedas) {
						if (this.pojoFactura.getIdMoneda().longValue() == var.getId()) {
							this.pojoFactura.setDescripcionMoneda(var.getNombre());
							this.pojoFactura.setAbreviaturaMoneda(var.getAbreviacion());
							this.pojoFactura.setTipoCambio(1.0);
							log.info("Asigno datos de Moneda. Pendiente actualizar ... ");
							guardar = true;
							break;
						}
					}
				}

				// Tipo Cambio
				log.info("Comprobando Tipo de Cambio ... ");
				if (this.pojoFactura.getIdMoneda().longValue() != this.monedaBase.getId()) {
					try {
						for (Moneda var : this.listMonedas) {
							if (this.pojoFactura.getIdMoneda().longValue() == var.getId()) {
								valTipoCambio = this.ifzMonValores.findActual(this.monedaBase, var);
								if (valTipoCambio == null || valTipoCambio.getId() == null || valTipoCambio.getId() <= 0L) {
									control("Ocurrio un problema al intentar Recuperar el Tipo de Cambio");
									return;
								}
								
								// Asigno tipo de cambio
								this.pojoFactura.setTipoCambio(valTipoCambio.getValor().doubleValue());
								log.info("Asigno Tipo de Cambio: " + valTipoCambio.getValor().doubleValue() + ". Pendiente actualizar ... ");
								guardar = true;
								break;
							}
						}
					} catch (Exception e) {
						log.error("ERROR INTERNO: Ocurrio un problema al intentar recuperar el Tipo de Cambio para la moneda de la Factura", e);
					}
				}

				// Cliente
				log.info("Compruebo datos de Cliente ... ");
				if (! validaDatosCliente()) {
					asignarDatosCliente();
					log.info("Asigno datos de Cliente. Pendiente actualizar ... ");
					guardar = true;
				}
				
				// Actualizo los datos de la Factura si corresponde
				if (guardar) {
					log.info("Actualizando factura ... ");
					this.ifzFactura.update(this.pojoFactura);
					log.info("Factura actualizada!");
				}
			}
			
			log.info("Validacion terminada!");
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.validarTimbre.", e);
			control(true);
		}
	}
	
	public void timbrar() {
		String resTimbre = "";
		
		try {
			control();
			if(this.procesando)
				return;
			this.procesando = true;
			log.info("Timbrando factura ... ");
			if (this.pojoFactura == null) {
				log.info("Timbrado abortado");
				return;
			}
			
			// Version XML
			this.pojoFactura.setVersion(this.cfdiVersion);
			
			// Tipo de Comprobante
			this.pojoFactura.setTipoComprobante("");
			for (ConValores var : this.listCFDITipoComprobante) {
				if (this.idTipoComprobante == var.getId()) {
					this.pojoFactura.setTipoComprobante(var.getValor());
					break;
				}
			}
			
			// Uso de CFDI
			this.pojoFactura.setUsoCfdi("");
			for (ConValores var : this.listCFDIUso) {
				if (this.idUsoCfdi == var.getId()) {
					this.pojoFactura.setUsoCfdi(var.getValor());
					break;
				}
			}
			
			// CFDI Relacionado
			this.pojoFactura.setCfdiRelacionado(0);
			this.pojoFactura.setCfdiRelacionadoUuid("");
			this.pojoFactura.setCfdiTipoRelacion("");
			if (this.cfdiRelacionado) {
				this.pojoFactura.setCfdiRelacionado(1);
				this.pojoFactura.setCfdiRelacionadoUuid(this.cfdiRelacionadoUuid);
				for (ConValores var : this.listCFDITipoRelacion) {
					if (this.idTipoRelacion == var.getId()) {
						this.pojoFactura.setCfdiTipoRelacion(var.getValor());
						break;
					}
				}
			}
			
			// Actualizamos factura
			this.ifzFactura.update(this.pojoFactura);
			
			if ("3.3".equals(this.cfdiVersion)) {
				FactElectv33 timbre = new FactElectv33();
				timbre.setDebugging(this.isDebug);
				timbre.setTesting(validaRequest("CFDI_TEST", "1"));
				if (! timbre.timbrar(this.pojoFactura, this.usuarioId)) {
					log.error("Ocurrio un problema al intentar Timbrar el CFDI ... " + timbre.getErrorCodigo() + " :: " + timbre.getErrorDescripcion());
					if (timbre.getError().contains("CadenaOriginal:"))
						control("No se pudo timbrar la Factura.\n" + timbre.getError().substring(0, timbre.getError().indexOf("CadenaOriginal:")).trim());
					else
						control("No se pudo timbrar la Factura.\n" + timbre.getError());
					return;
				}
			} else {
				FactElect timbre = new FactElect();
				resTimbre = timbre.foliar(this.pojoFactura.getId(), this.usuarioId);
				actualizaFactura(timbre.getData(), timbre.facturaActualizada());
				
				if (! "".equals(resTimbre.trim())) {
					log.info("ERROR 5: Error al timbrar: " + resTimbre.trim());
					control(5);
					this.mensaje = resTimbre.trim();
					return;
				} 
			}
			
			log.info("Factura timbrada!");
			this.facturaTimbrada = true;
			this.puedeTimbrar = false;
			
			// Actualizamos la factura en la lista
			int index = -1;
			for(Factura var : this.listFacturas) {
				if(var.getId() == this.pojoFactura.getId()) {
					index = this.listFacturas.indexOf(var);
					break;
				}
			}
			
			// Actualizamos factura
			this.ifzFactura.update(this.pojoFactura);
			
			if (index >= 0)
				this.listFacturas.set(index, this.ifzFactura.findById(this.pojoFactura.getId()));
			log.info("Factura actualizada");
		} catch(Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.timbrar.", e);
			control(true);
		} finally {
			this.cfdiVersion = "";
			this.cfdiRelacionadoUuid = "";
			this.idUsoCfdi = 0;
			this.idTipoRelacion = 0;
			this.idTipoComprobante = 0;
			this.cfdiRelacionado = false;
			this.procesando = false;
		}
	}
	
	private void actualizaFactura(HashMap<String, Object> data, boolean facturaActualizada) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			if (data.containsKey("no_externo") && data.get("no_externo") != null && ! "SN".equals(data.get("no_externo").toString().trim()) && ! "".equals(data.get("no_externo").toString().trim()))
				this.pojoFactura.setNoExterno(data.get("no_externo").toString());
			if (data.containsKey("no_interno") && data.get("no_interno") != null && ! "SN".equals(data.get("no_interno").toString().trim()) && ! "".equals(data.get("no_interno").toString().trim()))
				this.pojoFactura.setNoInterno(data.get("no_interno").toString());
			if (data.containsKey("cadena_original") && data.get("cadena_original") != null)
				this.pojoFactura.setCadenaOriginal(data.get("cadena_original").toString().getBytes());
			if (data.containsKey("cfdi") && data.get("cfdi") != null)
				this.pojoFactura.setCfdi(data.get("cfdi").toString().getBytes());
			if (data.containsKey("ciudad") && data.get("ciudad") != null)
				this.pojoFactura.setCiudad(data.get("ciudad").toString());
			if (data.containsKey("cliente") && data.get("cliente") != null)
				this.pojoFactura.setCliente(data.get("cliente").toString());
			if (data.containsKey("colonia") && data.get("colonia") != null)
				this.pojoFactura.setColonia(data.get("colonia").toString());
			if (data.containsKey("cp") && data.get("cp") != null)
				this.pojoFactura.setCp(Integer.valueOf(data.get("cp").toString()));
			if (data.containsKey("domicilio") && data.get("domicilio") != null)
				this.pojoFactura.setDomicilio(data.get("domicilio").toString());
			if (data.containsKey("estado") && data.get("estado") != null)
				this.pojoFactura.setEstado(data.get("estado").toString());
			if (data.containsKey("folio") && data.get("folio") != null)
				this.pojoFactura.setFolio(data.get("folio").toString());
			if (data.containsKey("id_folio") && data.get("id_folio") != null)
				this.pojoFactura.setIdFolio(Long.valueOf(data.get("id_folio").toString()));
			if (data.containsKey("timbre") && data.get("timbre") != null)
				this.pojoFactura.setTimbre(data.get("timbre").toString().getBytes());
			if (data.containsKey("tipo_comprobante") && data.get("tipo_comprobante") != null)
				this.pojoFactura.setTipoComprobante(data.get("tipo_comprobante").toString());
			if (data.containsKey("pais") && data.get("pais") != null)
				this.pojoFactura.setPais(data.get("pais").toString());
			if (data.containsKey("sello") && data.get("sello") != null)
				this.pojoFactura.setSello(data.get("sello").toString().getBytes());
			if (data.containsKey("serie") && data.get("serie") != null)
				this.pojoFactura.setSerie(data.get("serie").toString());
			if (data.containsKey("state") && data.get("state") != null)
				this.pojoFactura.setState(Double.valueOf(data.get("state").toString()));
			if (data.containsKey("descripcion") && data.get("descripcion") != null)
				this.pojoFactura.setDescripcion(data.get("descripcion").toString()); // .setState(Double.valueOf(data.get("state").toString()));
			if (data.containsKey("xml") && data.get("xml") != null)
				this.pojoFactura.setXml(data.get("xml").toString().getBytes());
			if (data.containsKey("nocertificado") && data.get("nocertificado") != null)
				this.pojoFactura.setNocertificado(data.get("nocertificado").toString());
			if (data.containsKey("uuid") && data.get("uuid") != null)
				this.pojoFactura.setUuid(data.get("uuid").toString());
			if (data.containsKey("fecha_timbrado") && data.get("fecha_timbrado") != null)
				this.pojoFactura.setFechaTimbrado(formatter.parse(data.get("fecha_timbrado").toString()));
			if (data.containsKey("no_certificado_sat") && data.get("no_certificado_sat") != null)
				this.pojoFactura.setNoCertificadoSat(data.get("no_certificado_sat").toString());
			if (data.containsKey("sello_sat") && data.get("sello_sat") != null)
				this.pojoFactura.setSelloSat(data.get("sello_sat").toString());
			
			if (! facturaActualizada)
				this.ifzFactura.update(pojoFactura);
			
			this.puedeTimbrar = true;
			this.facturaTimbrada = false;
			if (this.pojoFactura.getUuid() != null && ! "".equals(this.pojoFactura.getUuid())) {
				this.facturaTimbrada = true;
				this.puedeTimbrar = false;
			}
		} catch(Exception e) {
			this.procesoIncompleto = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en actualizaFactura.", e);
		}
	}

	// ---------------------------------------
	// ENVIO DE FACTURA
	// ---------------------------------------
	
	public void nuevoEnvio() {
		this.email = "";
		this.emailAsunto = "Envio de Factura";
		if (this.pojoFactura != null && this.pojoFactura.getId() > 0L) {
			// Asunto, sugerido
			this.emailAsunto += " " + this.pojoFactura.getFolioFactura();
			// Email cliente, sugerido
			if (this.pojoFactura.getIdObra().getIdCliente().getCorreo() != null && ! "".equals(this.pojoFactura.getIdObra().getIdCliente().getCorreo().trim()))
				this.email = this.pojoFactura.getIdObra().getIdCliente().getCorreo();
		}
		this.emailCuerpo = "Anexo factura en formato PDF y XML";
	}
	
	public void enviarFactura() {
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte = null;
		HashMap<String, Object> adjuntos = null;
		HashMap<String, String> params = null;
		HashMap<String, String> correo = null;
		byte[] contenidoFacturaPDF = null;
		byte[] contenidoFacturaXML = null;
		String nombreFacturaPDF = "";
		String nombreFacturaXML = "";
		
		try {
			control();
			if (this.pojoFactura != null) {
				if ("".equals(this.email)) {
					log.info("ERROR 13: NO especifico correo para el envio de la factura");
					control(13);
					return;
				}
				
				// Parametros para la ejecucion del reporte
				log.info("Enviar Factura (PDF y XML) por correo a " + this.email + " ... ");
				if (! "".equals(this.pojoFactura.getFolioFactura())) {
					nombreFacturaPDF = "F_" + this.pojoFactura.getId() + "_" + this.pojoFactura.getFolioFactura() + "_" + this.pojoFactura.getUuid() + ".pdf";
					nombreFacturaXML = "F_" + this.pojoFactura.getId() + "_" + this.pojoFactura.getFolioFactura() + "_" + this.pojoFactura.getUuid() + ".xml";
				} else {
					nombreFacturaPDF = "F_" + this.pojoFactura.getId() + ".pdf";
					nombreFacturaXML = "F_" + this.pojoFactura.getId() + ".xml";
				}

				// Validamos mensaje
				if ("".equals(this.emailCuerpo))
					this.emailCuerpo = "Anexo factura en formato PDF.";

				if (this.emailCuerpo.contains("[Mensaje]"))
					this.emailCuerpo = this.emailCuerpo.replace("[Mensaje]", "");

				if (this.emailCuerpo.contains("\n"))
					this.emailCuerpo = this.emailCuerpo.replace("\n", "<br/>");
				
				this.emailCuerpo = "<html><body>" + this.emailCuerpo + "</body></html>";

				// Parametros para la ejecucion del reporte
				log.info("Generando parametros para la ejecucion de Reporte ... ");
				params = new HashMap<String, String>();
				params.put("idPrograma", 	  "168");
				params.put("nombreDocumento", nombreFacturaPDF);
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);
				
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idFactura", this.pojoFactura.getId());

				log.info("Ejecutando reporte ... ");
				respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte, null);
				if(respuesta.getErrores().getCodigoError() != 0L) {
					log.error("ERROR INTERNO: No se pudo procesar el reporte de Factura.\n" + respuesta.getErrores().getDescError());
					control("Ocurrio un problema y No se pudo procesar el reporte de Factura.\nContacte a su Administrador");
					return;
				}
				
				log.info("Recuperando contenido de reporte (Factura PDF y XML) ... ");
				contenidoFacturaPDF = (byte[]) respuesta.getBody().getValor("contenidoReporte");
				contenidoFacturaXML = this.pojoFactura.getCfdi();
				
				// Parametros para envio de correo
				log.info("Asignando parametros de envio de correo ... ");
				correo = new HashMap<>();
				correo.put("from", this.usuarioEmail); 
				correo.put("fromPass", "disesa12");
				correo.put("to", this.email);
				correo.put("subject", this.emailAsunto);
				correo.put("body", this.emailCuerpo);
				
				// Adjuntos
				log.info("Asignando adjuntos ... ");
				adjuntos = new HashMap<String, Object>();
				adjuntos.put(nombreFacturaPDF, contenidoFacturaPDF);
				adjuntos.put(nombreFacturaXML, contenidoFacturaXML);
				log.info("Adjuntos\n" + adjuntos.toString());

				log.info("Enviado correo ... ");
				respuesta = this.ifzReportes.enviarCorreo(correo, adjuntos);
				if(respuesta.getErrores().getCodigoError() != 0L) {
					log.error("Error Interno - No se pudo enviar el reporte por correo electronico :: " + respuesta.getErrores().getDescError());
		    		control(true, -1, "Error Interno - No se pudo enviar el reporte por correo electronico");
					return;
				}

				log.info("Factura Enviada a " + this.email + " desde " + this.usuarioEmail + " ... Proceso terminado!");
				control(7);
			}
    	} catch (Exception e) {
    		log.error("Error en CuentasPorCobrar.FacturaAction.enviarFactura", e);
    		control(true);
    	}
	}

	// ---------------------------------------
	// PROVISIONES
	// ---------------------------------------
	
	public void evaluaProvision() {
		//List<FacturaPagos> listPagos = null;
		BigDecimal liquidado = BigDecimal.ZERO;
		boolean guardar = false;
		
		try {
			control();
			this.montoPagado = 0;
			this.montoProvision = 0;
			this.permiteProvisionar = false;

			// Cliente
			log.info("Compruebo datos de Cliente ... ");
			if (! validaDatosCliente()) {
				asignarDatosCliente();
				log.info("Asigno datos de Cliente. Pendiente actualizar ... ");
				guardar = true;
			}
			
			// Cargamos los abonos
			liquidado = this.ifzPagos.findLiquidado(this.pojoFactura.getId());
			liquidado = liquidado.setScale(2);
			this.montoPagado = liquidado.doubleValue();
			/*if (listPagos != null && ! listPagos.isEmpty()) {
				for (FacturaPagos var : listPagos) 
					this.montoPagado += var.getMonto().doubleValue();
			}*/
			
			this.montoFacturado = (new BigDecimal((new DecimalFormat("0.0000")).format(this.pojoFactura.getTotal()))).doubleValue();
			this.montoPagado 	= (new BigDecimal((new DecimalFormat("0.0000")).format(this.montoPagado))).doubleValue();
			this.montoProvision = this.montoFacturado - this.montoPagado;
			log.info("Sugerencia de monto de Provision: " + this.montoProvision);
			
			if (this.montoProvision <= 0) {
				control("No se puede provisionar.\nFactura Liquidada");
				return;
			}

			if (this.pojoFactura.getProvisionada() == 1)
				this.permiteProvisionar = true;
			
			
			// Actualizo los datos de la Factura si corresponde
			if (guardar) {
				log.info("Actualizando factura ... ");
				this.ifzFactura.update(this.pojoFactura);
				log.info("Factura actualizada!");
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.evaluaProvision", e);
			control(true);
		}
	}
	
	public void provisionar() {
		boolean guardar = false;
		
		try {
			control();
			if (this.montoProvision <= 0) {
				log.info("Intento provisionar una factura sin monto de provision. Factura " + this.pojoFactura.getId() + " - " + this.pojoFactura.getFolioFactura());
				control("Debe indicar un monto para la Provision");
				return;
			}

			// Comprobamos la moneda
			log.info("Compruebo Moneda ... ");
			if (this.pojoFactura.getIdMoneda() == null && this.monedaBase != null) {
				this.pojoFactura.setIdMoneda(this.monedaBase.getId());
				this.pojoFactura.setDescripcionMoneda(this.monedaBase.getNombre());
				this.pojoFactura.setAbreviaturaMoneda(this.monedaBase.getAbreviacion());
				log.info("Asigno datos de Moneda. Pendiente actualizar ... ");
				guardar = true;
			}
			
			// Comprobamos los datos de cliente
			log.info("Compruebo datos de Cliente ... ");
			if (! validaDatosCliente()) {
				asignarDatosCliente();
				log.info("Asigno datos de Cliente. Pendiente actualizar ... ");
				guardar = true;
			}
			
			// Actualizamos los datos de la factura si corresponde
			if (guardar) {
				log.info("Actualizando Factura ... ");
				this.ifzFactura.update(this.pojoFactura);
				log.info("Factura actualizada.");
				
				// Lanzamos la transaccion en un hilo secundario
				new Thread(this).start();
			} else {
				log.info("Provisionando Factura ... ");
				Respuesta respuesta = this.ifzFactura.provisionar(this.pojoFactura, this.montoProvision, this.usuarioId);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					log.error("\nOcurrio un problema al intentar provisionar la Factura\n", (Throwable) respuesta.getBody().getValor("exception"));
					control(respuesta.getErrores().getDescError());
					return;
				}
				log.info("Factura Provisionada");
			}
			
			this.montoProvision = 0;
			this.montoFacturado = 0;
			this.montoPagado = 0;
		} catch (Exception e) {
			log.error("ERROR INTERNO: No se pudo enviar la Factura a Provision", e);
			control("Ocurrio un problema al intentar enviar la Factura a Provision");
		}
	}

	@Override
	public void run() {
		Respuesta respuesta = null;

		try {
			control();
			Thread.sleep(1000);
			log.info("Provisionando Factura ... ");
			respuesta = this.ifzFactura.provisionar(this.pojoFactura, this.montoProvision, this.usuarioId);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("\nOcurrio un problema al intentar provisionar la Factura\n", (Throwable) respuesta.getBody().getValor("exception"));
				control(respuesta.getErrores().getDescError());
				return;
			}

			log.info("Factura Provisionada");
		} catch (Exception e) {
			log.error("ERROR INTERNO: No se pudo enviar la Factura a Provision", e);
			control("Ocurrio un problema al intentar enviar la Factura a Provision");
		}
	}

	// --------------------------------------------------------------------------------
	// BUSQUEDA FACTURAS
	// --------------------------------------------------------------------------------
	
	public void nuevaBusquedaFacturas() {
		this.campoBusquedaFacturasCfdi = this.tipoBusquedaFacturasCfdi.get(0).getValue().toString();
		this.valorBusquedaFacturasCfdi = "";
		this.numPaginaFacturasCfdi = 1;

		this.pojoFacturaCfdiSeleccionada = null;
		if (this.listFacturasCfdi == null)
			this.listFacturasCfdi = new ArrayList<Factura>();
		this.listFacturasCfdi.clear();
	}
	
	public void buscarFacturas() {
		try {
			control();
			if (this.campoBusquedaFacturasCfdi == null || "".equals(this.campoBusquedaFacturasCfdi))
				this.campoBusquedaFacturasCfdi = this.tipoBusquedaFacturasCfdi.get(0).getValue().toString();
			
			this.listFacturasCfdi = this.ifzFactura.findTimbradas("id desc", 0);
			if (this.listFacturasCfdi == null || this.listFacturasCfdi.isEmpty()) {
				log.info("Error 2 - Busqueda de Facturas sin resultado");
				control(2);
			}
		} catch(Exception e) {
			log.error("ERROR INTERNO: Ocurrio un problema al intentar realizar la busqueda de Facturas", e);
			control("Ocurrio un problema al intentar realizar la busqueda de Facturas");
		} finally {
			log.info(this.listFacturasCfdi.size() + " Facturas encontradas");
		}
	}
	
	public void seleccionarFactura() {
		try {
			control();
			log.info("Seleccionando factura ... ");
			if (this.pojoFacturaCfdiSeleccionada == null) {
				log.info("Ocurrio un problema al intentar recuperar la Factura seleccionada");
				control("Ocurrio un problema al intentar recuperar la Factura seleccionada");
				return;
			}
			
			this.cfdiRelacionadoUuid = this.pojoFacturaCfdiSeleccionada.getUuid();
			nuevaBusquedaFacturas();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar obtener datos de la factura seleccionada", e);
			control("Ocurrio un problema al intentar obtener datos de la factura seleccionada");
		} 
	}

	// --------------------------------------------------------------------------------
	// FOLIO
	// --------------------------------------------------------------------------------

	public void evaluaFacturaFolio() {
		try {
			control();
			this.facturaSerie = this.pojoFactura.getSerie();
			this.facturaFolio = this.pojoFactura.getFolio();
			
			if (this.sucursalId > 0L) {
				for (Sucursal var : this.listBusquedaSucursal) {
					if (this.sucursalId == var.getId() && (var.getSerie() != null && ! "".equals(var.getSerie().trim()))) {
						this.facturaSerie = var.getSerie();
						if (! this.pojoFactura.getSerie().equals(this.facturaSerie))
							this.facturaFolio = "";
						break;
					}
				}
			} 
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.evaluaFacturaFolio(). No se pudo cargar los valores para Serie y Folio de la factura");
			control("No se pudo cargar la Serie y Folio de la Factura indicada.");
		}
	}

	public void guardarFacturaFolio() {
		try {
			control();
			if (this.facturaSerie == null)
				this.facturaSerie = "";
			if (this.facturaFolio == null || "".equals(this.facturaFolio.trim()))
				this.facturaFolio = "0";
			
			this.pojoFactura.setSerie(this.facturaSerie);
			this.pojoFactura.setFolio(this.facturaFolio);
			
			if (! "".equals(this.facturaSerie.trim()) && ! "0".equals(this.facturaFolio.trim())) {
				this.pojoFactura.setFolioFactura(this.facturaSerie + '-' + this.facturaFolio);
				this.pojoFactura.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				this.pojoFactura.setModificadoPor(this.usuarioId);
				log.info("Serie y folio modificados correctamente: " + this.pojoFactura.getFolioFactura());
			} else {
				log.info("Serie y folio modificados correctamente: " + this.pojoFactura.getFolioFactura());
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.guardarFacturaFolio(). No se pudo guardar la Serie y Folio ingresados.", e);
			control("No se pudo guardar la Serie y Folio ingresados");
		}
	}

	// --------------------------------------------------------------------------------
	// PAGOS
	// --------------------------------------------------------------------------------
	
	public void obtenerPagos() {
		try {
			control();
			this.paginacionPagos = 1;
			this.listFacturaPagos = this.ifzPagos.findByFactura(this.pojoFactura.getId());
			if (this.listFacturaPagos != null && ! this.listFacturaPagos.isEmpty()) {
				Collections.sort(this.listFacturaPagos, new Comparator<FacturaPagos>() {
					@Override
					public int compare(FacturaPagos o1, FacturaPagos o2) {
						return o2.getFecha().compareTo(o1.getFecha());
					}
				});
			}
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar consultar los Pagos de la Factura");
		}
	}

	public void obtenerPagoPDF() {
		
	}
	
	public void obtenerPagoXML() {
		String nombreDocumento = "";
		byte[] contenidoDocumento = null;
		FacturaPagos fPago = null;
		
		try {
			control();
			log.info("Descargando XML ... ");
			if (this.idPago <= 0L) {
				log.info("Descarga abortada");
				return;
			}
			
			fPago = this.ifzPagos.findById(idPago);
			if (fPago == null || fPago.getId() == null || fPago.getId() <= 0L) {
				control("Ocurrio un problema al recuperar el Pago indicado");
				return;
			}

			nombreDocumento = "P-" + fPago.getSerie() + "-" + fPago.getFolio() + ".xml";
			contenidoDocumento = fPago.getXmlTimbrado();

			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", "xml");
			log.info("Descarga lanzada");
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el XML de la factura", e);
			control(true);
		}
	}
	
	public void guardarPago() {
		
	}
	
	// --------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------

	public Factura getPojoFactura() {
		try {
			if (this.pojoFactura != null)
				return this.ifzFactura.convertir(this.pojoFactura);
		} catch (Exception e) {
			log.error("No puedo convertir el pojo extendido a Factura", e);
		}
		
		return new Factura();
	}
	
	public void setPojoFactura(Factura pojoFactura) {
		try {
			if (pojoFactura != null) {
				if (this.isDebug)
					log.info("Extendiendo pojo Factura");
				// Lo vuelvo a consultar para recuperar sus datos actualizados, y despues lo extiendo
				pojoFactura = this.ifzFactura.findById(pojoFactura.getId());
				setFactura(this.ifzFactura.convertir(pojoFactura));
			}
		} catch (Exception e) {
			log.error("No puedo convertir el pojo Factura a extendido", e);
		}
	}

	public FacturaExt getFactura(){
		return this.pojoFactura;
	}
	
	public void setFactura(FacturaExt factura) {
		this.pojoFactura = factura;
	}

	public String getUuid() {
		if (this.facturaTimbrada && this.pojoFactura.getUuid() != null && ! "".equals(this.pojoFactura.getUuid().trim()))
			return this.pojoFactura.getUuid();
		return "Sin timbrar";
	}
	
	public void setUuid(String value) {}
	
	public boolean getTipoFactura() {
		if (this.pojoFactura != null && this.pojoFactura.getTipo() != null && ! "".equals(this.pojoFactura.getTipo()))
			return "C".equals(this.pojoFactura.getTipo());
		return false;
	}
	
	public void setTipoFactura(boolean tipoFactura) {
		if (this.pojoFactura != null)
			this.pojoFactura.setTipo(tipoFactura ? "C" : "X");
	}
	
	public String getCondicionesPago() {
		if (this.pojoFactura != null)
			return this.pojoFactura.getCondicionesPago();
		return "";
	}
	
	public void setCondicionesPago(String condicionesPago) {
		if (this.pojoFactura != null)
			this.pojoFactura.setCondicionesPago(condicionesPago);
	}
	
	public String getTituloFactura() {
		if (this.pojoFactura != null && this.pojoFactura.getId() != null && this.pojoFactura.getId() > 0L)
			return "Factura ID " + this.pojoFactura.getId();
		return "Nueva Factura";
	}
	
	public void setTituloFactura(String value) {}

	public String getTituloFacturaPagos() {
		if (this.pojoFactura != null && this.pojoFactura.getId() != null && this.pojoFactura.getId() > 0L)
			return "Factura " + this.pojoFactura.getFolioFactura() + " (" + this.pojoFactura.getId() + ")";
		return "Nueva Factura";
	}
	
	public void setTituloFacturaPagos(String value) {}

	public String getNombreObra() {
		if (this.pojoFactura != null && this.pojoFactura.getIdObra() != null && this.pojoFactura.getIdObra().getNombre() != null)
			return this.pojoFactura.getIdObra().getId() + " - " + this.pojoFactura.getIdObra().getNombre();
		return "";
	}
	
	public void setNombreObra(String value) {}
	
	public String getNombreCliente() {
		if (this.pojoFactura != null && this.pojoFactura.getIdObra() != null && this.pojoFactura.getIdObra().getNombre() != null)
			return this.pojoFactura.getIdObra().getIdCliente().getId() + " - " 
				+ this.pojoFactura.getIdObra().getClienteNombre() + " (" 
				+ this.pojoFactura.getIdObra().getIdCliente().getRfc() + ")";
		return "";
	}
	
	public void setNombreCliente(String value) {}
	
	public String getDireccionCliente() {
		if (this.pojoFactura != null && this.pojoFactura.getIdObra() != null && this.pojoFactura.getIdObra().getNombre() != null)
			return this.pojoFactura.getIdObra().getIdCliente().getDomicilio();
		return "";
	}
	
	public void setDireccionCliente(String value) {}

	public String getConceptoDescripcion() {
		if (this.pojoDetalle != null && this.pojoDetalle.getIdConcepto() != null && this.pojoDetalle.getIdConcepto().getId() != null && this.pojoDetalle.getIdConcepto().getId() >= 0L)
			return this.pojoDetalle.getIdConcepto().getId() + " - " + this.pojoDetalle.getIdConcepto().getDescripcion();
		return "";
	}
	
	public void setConceptoDescripcion(String value) { }

	public Date getFechaEmision() {
		return this.fechaEmision;
	}
	
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public FacturaDetalleExt getFacturaDetalle() {
		return pojoDetalle;
	}
	
	public void setFacturaDetalle(FacturaDetalleExt facturaDetalle) {
		this.pojoDetalle = facturaDetalle;
	}
	
	public Obra getPojoObra() {
		return this.pojoObra;
	}
		
	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public double getTotal() {
		return this.total;
	}
		
	public void setTotal(double total) {
		this.total = total;
	}
	
	public int getTipoMensaje() {
		return tipoMensaje;
	}
	
	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
	
	public List<Obra> getListaObras() {
		return listaObras;
	}
	
	public void setListaObras(List<Obra> listaObras) {
		this.listaObras = listaObras;
	}
	
	public List<FacturaDetalleExt> getListFacturaDetalles() {
		return listDetalles;
	}
		
	public void setListFacturaDetalles(List<FacturaDetalleExt> listFacturaDetalles) {
		this.listDetalles = listFacturaDetalles;
	}
	
	public double getSubtotal() {
		return this.subtotal;
	}
		
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
		
	public double getImpuestos() {
		return this.impuestos;
	}
		
	public void setImpuestos(double impuestos) {
		this.impuestos = impuestos;
	}
		
	public String getResOperacion() {
		return mensaje;
	}
	
	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}
	
	public String getCampoBusqueda() {
		return campoBusqueda;
	}
		
	public void setCampoBusqueda(String campoBusqueda){
		this.campoBusqueda = campoBusqueda;
	}
		
	public String getCampoBusquedaProyecto() {
		return campoBusquedaObras;
	}
		
	public void setCampoBusquedaProyecto(String campoBusquedaProyecto) {
		this.campoBusquedaObras = campoBusquedaProyecto;
	}
		
	public String getValorBusqueda() {
		return valorBusqueda;
	}
	
	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}
	
	public String getValorBusquedaProyecto() {
		return valorBusquedaObras;
	}
		
	public void setValorBusquedaProyecto(String valorBusquedaProyecto) {
		this.valorBusquedaObras = valorBusquedaProyecto;
	}
		
	/*public int getTipoBusqueda() {
		return tipoBusqueda;
	}
	
	public void setTipoBusqueda(int tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}*/
		
	public int getTipoBusquedaProyecto() {
		return tipoBusquedaProyecto;
	}
	
	public void setTipoBusquedaProyecto(int tipoBusquedaProyecto) {
		this.tipoBusquedaProyecto = tipoBusquedaProyecto;
	}
	
	public List<Factura> getListFacturaGrid() {
		return listFacturas;
	}
	
	public void setListFacturaGrid(List<Factura> listFacturaGrid) {
		this.listFacturas = listFacturaGrid;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}
	
	public double getCosto() {
		return costo;
	}
	
	public void setCosto(double costo) {
		this.costo = costo;
	}
	
	public SelectItem getConceptoSeleccionado() {
		return conceptoSeleccionado;
	}
	
	public void setConceptoSeleccionado(SelectItem conceptoSeleccionado) {
		this.conceptoSeleccionado = conceptoSeleccionado;
	}
	
	public int getNumPaginaDetalles() {
		return numPaginaDetalles;
	}
	
	public void setNumPaginaDetalles(int numPaginaDetalles) {
		this.numPaginaDetalles = numPaginaDetalles;
	}
		
	public boolean isBand() {
		return procesoIncompleto;
	}
	
	public void setBand(boolean band) {
		this.procesoIncompleto = band;
	}
	
	public List<SelectItem> getListConceptoFacturacionItems() {
		return listConceptoFacturacionItems;
	}
	
	public void setListConceptoFacturacionItems(List<SelectItem> listConceptoFacturacionItems) {
		this.listConceptoFacturacionItems = listConceptoFacturacionItems;
	}
	
	public int getNumPaginaImpuestos() {
		return numPaginaImpuestos;
	}
	
	public void setNumPaginaImpuestos(int numPaginaImpuestos) {
		this.numPaginaImpuestos = numPaginaImpuestos;
	}
	
	public List<FacturaDetalleImpuestoExt> getListConceptoFacImpuestos() {
		return listDetalleImpuestos;
	}
	
	public void setListConceptoFacImpuestos(List<FacturaDetalleImpuestoExt> listConceptoFacImpuestos) {
		this.listDetalleImpuestos = listConceptoFacImpuestos;
	}
	
	public List<FacturaDetalleImpuestoExt> getListConceptoFacImpuestosEliminados() {
		return listDetalleImpuestosEliminados;
	}
	
	public void setListConceptoFacImpuestosEliminados(List<FacturaDetalleImpuestoExt> listConceptoFacImpuestosEliminados) {
		this.listDetalleImpuestosEliminados = listConceptoFacImpuestosEliminados;
	}
	
	public int getNumPaginaProyectos() {
		return paginacionObras;
	}
	
	public void setNumPaginaProyectos(int numPaginaProyectos) {
		this.paginacionObras = numPaginaProyectos;
	}
	
	public FacturaDetalleImpuestoExt getPojoConceptoImpuesto() {
		return pojoDetalleImpuesto;
	}
	
	public void setPojoConceptoImpuesto(FacturaDetalleImpuestoExt pojoConceptoImpuesto) {
		this.pojoDetalleImpuesto = pojoConceptoImpuesto;
	}
	
	public double getRetenciones() {
		return retenciones;
	}
	
	public void setRetenciones(double retenciones) {
		this.retenciones = retenciones;
	}

	public List<Sucursal> getListBusquedaSucursal() {
		return listBusquedaSucursal;
	}

	public void setListBusquedaSucursal(List<Sucursal> listBusquedaSucursal) {
		this.listBusquedaSucursal = listBusquedaSucursal;
	}

	public List<SelectItem> getTipoBusquedaSucursal() {
		return tipoBusquedaSucursal;
	}

	public void setTipoBusquedaSucursal(List<SelectItem> tipoBusquedaSucursal) {
		this.tipoBusquedaSucursal = tipoBusquedaSucursal;
	}

	public String getValorBusquedaSucursal() {
		return valorBusquedaSucursal;
	}

	public void setValorBusquedaSucursal(String valorBusquedaSucursal) {
		this.valorBusquedaSucursal = valorBusquedaSucursal;
	}

	public String getCampoBusquedaSucursal() {
		return campoBusquedaSucursal;
	}

	public void setCampoBusquedaSucursal(String campoBusquedaSucursal) {
		this.campoBusquedaSucursal = campoBusquedaSucursal;
	}
	
	public int getPaginaSucursales() {
		return paginaSucursales;
	}
	
	public void setPaginaSucursales(int paginaSucursales) {
		this.paginaSucursales = paginaSucursales;
	}

	public List<SelectItem> getListBusquedaSucursalItems() {
		return listBusquedaSucursalItems;
	}

	public void setListBusquedaSucursalItems(List<SelectItem> listBusquedaSucursalItems) {
		this.listBusquedaSucursalItems = listBusquedaSucursalItems;
	}

	public long getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(long sucursalId) {
		this.sucursalId = sucursalId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailAsunto() {
		return emailAsunto;
	}

	public void setEmailAsunto(String emailAsunto) {
		this.emailAsunto = emailAsunto;
	}

	public String getEmailCuerpo() {
		return emailCuerpo;
	}

	public void setEmailCuerpo(String emailCuerpo) {
		this.emailCuerpo = emailCuerpo;
	}
	
	public boolean isPuedeTimbrar() {
		return puedeTimbrar;
	}

	public void setPuedeTimbrar(boolean puedeTimbrar) {
		this.puedeTimbrar = puedeTimbrar;
	}
	
	public boolean isFacturaTimbrada() {
		return facturaTimbrada;
	}

	public void setFacturaTimbrada(boolean facturaTimbrada) {
		this.facturaTimbrada = facturaTimbrada;
	}
	
	public List<SelectItem> getListMetodosPagoItems() {
		return listMetodosPagoItems;
	}

	public void setListMetodosPagoItems(List<SelectItem> listMetodosPagoItems) {
		this.listMetodosPagoItems = listMetodosPagoItems;
	}

	public List<SelectItem> getListFormasPagoItems() {
		return listFormasPagoItems;
	}

	public void setListFormasPagoItems(List<SelectItem> listFormasPagoItems) {
		this.listFormasPagoItems = listFormasPagoItems;
	}

	public List<SelectItem> getOpcionesBusquedaProyecto() {
		return opcionesBusquedaObras;
	}

	public void setOpcionesBusquedaProyecto(List<SelectItem> opcionesBusquedaProyecto) {
		this.opcionesBusquedaObras = opcionesBusquedaProyecto;
	}
	
	public List<ConceptoFacturacion> getListConceptoFacturacion() {
		return listConceptoFacturacion;
	}

	public void setListConceptoFacturacion(List<ConceptoFacturacion> listConceptoFacturacion) {
		this.listConceptoFacturacion = listConceptoFacturacion;
	}

	public ConceptoFacturacion getPojoConcepto() {
		return pojoConcepto;
	}

	public void setPojoConcepto(ConceptoFacturacion pojoConcepto) {
		this.pojoConcepto = pojoConcepto;
	}
	
	public List<SelectItem> getTiposBusquedaConceptos() {
		return tiposBusquedaConceptos;
	}

	public void setTiposBusquedaConceptos(List<SelectItem> tiposBusquedaConceptos) {
		this.tiposBusquedaConceptos = tiposBusquedaConceptos;
	}

	public String getValBusquedaConceptos() {
		return valBusquedaConceptos;
	}

	public void setValBusquedaConceptos(String valBusquedaConceptos) {
		this.valBusquedaConceptos = valBusquedaConceptos;
	}

	public String getCampoBusquedaConceptos() {
		return campoBusquedaConceptos;
	}

	public void setCampoBusquedaConceptos(String campoBusquedaConceptos) {
		this.campoBusquedaConceptos = campoBusquedaConceptos;
	}

	public int getNumPaginaConceptos() {
		return numPaginaConceptos;
	}

	public void setNumPaginaConceptos(int numPaginaConceptos) {
		this.numPaginaConceptos = numPaginaConceptos;
	}

	public boolean getEsAdministrativo() {
		return perfilEgresos;
	}
	
	public void setEsAdministrativo(boolean esAdministrativo) {
		this.perfilEgresos = esAdministrativo;
	}
	
	public boolean isBuscarAdministrativo() {
		return buscarAdministrativo;
	}
	
	public void setBuscarAdministrativo(boolean buscarAdministrativo) {
		this.buscarAdministrativo = buscarAdministrativo;
	}

	public double getMontoFacturado() {
		return montoFacturado;
	}

	public void setMontoFacturado(double montoFacturado) {
		this.montoFacturado = montoFacturado;
	}

	public double getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(double montoPagado) {
		this.montoPagado = montoPagado;
	}

	public double getMontoProvision() {
		return montoProvision;
	}

	public void setMontoProvision(double montoProvision) {
		this.montoProvision = montoProvision;
	}
	
	public boolean getUserAdmin() {
		return ("ADMINISTRADOR".equals(this.usuario) ? true : false);
	}
	
	public void setUserAdmin(boolean value) {}

	public String getFacturaSerie() {
		return facturaSerie;
	}

	public void setFacturaSerie(String facturaSerie) {
		this.facturaSerie = facturaSerie;
	}

	public String getFacturaFolio() {
		return facturaFolio;
	}

	public void setFacturaFolio(String facturaFolio) {
		this.facturaFolio = facturaFolio;
	}
	
	public boolean getDebugging() {
		return this.isDebug;
	}
	
	public void setDebugging(boolean value) {
		this.isDebug = value;
	}

	public boolean isPermiteProvisionar() {
		return permiteProvisionar;
	}

	public void setPermiteProvisionar(boolean permiteProvisionar) {
		this.permiteProvisionar = permiteProvisionar;
	}

	/*public Factura getPojoFacturaBorrar() {
		return pojoFacturaBorrar;
	}

	public void setPojoFacturaBorrar(Factura pojoFacturaBorrar) {
		this.pojoFacturaBorrar = pojoFacturaBorrar;
	}*/

	public long getIdFacturaBorrar() {
		return idFacturaBorrar;
	}

	public void setIdFacturaBorrar(long idFacturaBorrar) {
		this.idFacturaBorrar = idFacturaBorrar;
	}
	
	public List<SelectItem> getListMonedasItems() {
		return listMonedasItems;
	}

	public void setListMonedasItems(List<SelectItem> listMonedasItems) {
		this.listMonedasItems = listMonedasItems;
	}

	public long getIdMetodoPago() {
		return idMetodoPago;
	}

	public void setIdMetodoPago(long idMetodoPago) {
		this.idMetodoPago = idMetodoPago;
	}
	
	public List<SelectItem> getListCFDIUsoItems() {
		return listCFDIUsoItems;
	}
	
	public void setListCFDIUsoItems(List<SelectItem> listCFDIUsoItems) {
		this.listCFDIUsoItems = listCFDIUsoItems;
	}
	
	public List<SelectItem> getListCFDITipoRelacionItems() {
		return listCFDITipoRelacionItems;
	}
	
	public void setListCFDITipoRelacionItems(List<SelectItem> listCFDITipoRelacionItems) {
		this.listCFDITipoRelacionItems = listCFDITipoRelacionItems;
	}
	
	public List<SelectItem> getListCFDITipoComprobanteItems() {
		return listCFDITipoComprobanteItems;
	}
	
	public void setListCFDITipoComprobanteItems(List<SelectItem> listCFDITipoComprobanteItems) {
		this.listCFDITipoComprobanteItems = listCFDITipoComprobanteItems;
	}
	
	public List<SelectItem> getListCFDIVersionItems() {
		return listCFDIVersionItems;
	}
	
	public void setListCFDIVersionItems(List<SelectItem> listCFDIVersionItems) {
		this.listCFDIVersionItems = listCFDIVersionItems;
	}
	
	public String getCfdiVersion() {
		return cfdiVersion;
	}
	
	public void setCfdiVersion(String cfdiVersion) {
		this.cfdiVersion = cfdiVersion;
	}
	
	public long getIdUsoCfdi() {
		return idUsoCfdi;
	}
	
	public void setIdUsoCfdi(long idUsoCfdi) {
		this.idUsoCfdi = idUsoCfdi;
	}
	
	public long getIdTipoComprobante() {
		return idTipoComprobante;
	}
	
	public void setIdTipoComprobante(long idTipoComprobante) {
		this.idTipoComprobante = idTipoComprobante;
	}

	public boolean getCfdiRelacionado() {
		return cfdiRelacionado;
	}

	public void setCfdiRelacionado(boolean cfdiRelacionado) {
		this.cfdiRelacionado = cfdiRelacionado;
	}

	public long getIdTipoRelacion() {
		return idTipoRelacion;
	}

	public void setIdTipoRelacion(long idTipoRelacion) {
		this.idTipoRelacion = idTipoRelacion;
	}

	public String getCfdiRelacionadoUuid() {
		return cfdiRelacionadoUuid;
	}

	public void setCfdiRelacionadoUuid(String cfdiRelacionadoUuid) {
		this.cfdiRelacionadoUuid = cfdiRelacionadoUuid;
	}
	
	public List<Factura> getListFacturasCfdi() {
		return listFacturasCfdi;
	}
	
	public void setListFacturasCfdi(List<Factura> listFacturasCfdi) {
		this.listFacturasCfdi = listFacturasCfdi;
	}
	
	public Factura getPojoFacturaCfdiSeleccionada() {
		return pojoFacturaCfdiSeleccionada;
	}
	
	public void setPojoFacturaCfdiSeleccionada(Factura pojoFacturaCfdiSeleccionada) {
		this.pojoFacturaCfdiSeleccionada = pojoFacturaCfdiSeleccionada;
	}
	
	public List<SelectItem> getTipoBusquedaFacturasCfdi() {
		return tipoBusquedaFacturasCfdi;
	}
	
	public void setTipoBusquedaFacturasCfdi(
			List<SelectItem> tipoBusquedaFacturasCfdi) {
		this.tipoBusquedaFacturasCfdi = tipoBusquedaFacturasCfdi;
	}
	
	public String getValorBusquedaFacturasCfdi() {
		return valorBusquedaFacturasCfdi;
	}
	
	public void setValorBusquedaFacturasCfdi(String valorBusquedaFacturasCfdi) {
		this.valorBusquedaFacturasCfdi = valorBusquedaFacturasCfdi;
	}
	
	public String getCampoBusquedaFacturasCfdi() {
		return campoBusquedaFacturasCfdi;
	}
	
	public void setCampoBusquedaFacturasCfdi(String campoBusquedaFacturasCfdi) {
		this.campoBusquedaFacturasCfdi = campoBusquedaFacturasCfdi;
	}
	
	public int getNumPaginaFacturasCfdi() {
		return numPaginaFacturasCfdi;
	}
	
	public void setNumPaginaFacturasCfdi(int numPaginaFacturasCfdi) {
		this.numPaginaFacturasCfdi = numPaginaFacturasCfdi;
	}

	public String getUsuarioEmail() {
		return usuarioEmail;
	}

	public void setUsuarioEmail(String usuarioEmail) {
		this.usuarioEmail = usuarioEmail;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public boolean isPerfilProvisiones() {
		return perfilProvisiones;
	}

	public void setPerfilProvisiones(boolean perfilProvisiones) {
		this.perfilProvisiones = perfilProvisiones;
	}

	public boolean isRefacturar() {
		return refacturar;
	}

	public void setRefacturar(boolean refacturar) {
		this.refacturar = refacturar;
	}

	public String getMensajeCancelacion() {
		return mensajeCancelacion;
	}

	public void setMensajeCancelacion(String mensajeCancelacion) {
		this.mensajeCancelacion = mensajeCancelacion;
	}

	public List<FacturaPagos> getListFacturaPagos() {
		return listFacturaPagos;
	}

	public void setListFacturaPagos(List<FacturaPagos> listFacturaPagos) {
		this.listFacturaPagos = listFacturaPagos;
	}

	public int getPaginacionPagos() {
		return paginacionPagos;
	}

	public void setPaginacionPagos(int paginacionPagos) {
		this.paginacionPagos = paginacionPagos;
	}

	public long getIdPago() {
		return idPago;
	}

	public void setIdPago(long idPago) {
		this.idPago = idPago;
	}

	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	public List<SelectItem> getListMeses() {
		return listMeses;
	}

	public void setListMeses(List<SelectItem> listMeses) {
		this.listMeses = listMeses;
	}

	public Date getBusquedaFecha() {
		return busquedaFecha;
	}

	public void setBusquedaFecha(Date busquedaFecha) {
		this.busquedaFecha = busquedaFecha;
	}

	public int getBusquedaMes() {
		return busquedaMes;
	}

	public void setBusquedaMes(int busquedaMes) {
		this.busquedaMes = busquedaMes;
	}

	public FacturaPagos getPojoFacPago() {
		if (this.pojoFacPago == null)
			this.pojoFacPago = new FacturaPagos();
		return pojoFacPago;
	}

	public void setPojoFacPago(FacturaPagos pojoFacPago) {
		this.pojoFacPago = pojoFacPago;
	}
	
	public long getFormaPago() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdFormaPago() != null) 
			return this.pojoFacPago.getIdFormaPago();
		return 0L;
	}
	
	public void setFormaPago(long idFormaPago) {
		if (idFormaPago <= 0)
			return;
		for (FormasPagos var : this.listFormasPago) {
			if (var.getId() == idFormaPago) {
				this.pojoFacPago.setIdFormaPago(var.getId());
				this.pojoFacPago.setFormaPago(var.getFormaPago());
				break;
			}
		}
	}
	
	public long getBancoOrigen() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdBancoOrigen() != null) 
			return this.pojoFacPago.getIdBancoOrigen();
		return 0L;
	}
	
	public void setBancoOrigen(long bancoOrigen) {
		if (bancoOrigen <= 0)
			return;
		for (Banco var : this.listBancos) {
			if (var.getId() == bancoOrigen) {
				this.pojoFacPago.setIdBancoOrigen(var.getId());
				break;
			}
		}
	}
	
	public long getCuentaBancaria() {
		if (this.pojoFacPago != null && this.pojoFacPago.getIdCuentaDeposito() != null) 
			return this.pojoFacPago.getIdCuentaDeposito();
		return 0L;
	}
		
	public void setCuentaBancaria(long idCuentaBancaria) {
		if (idCuentaBancaria <= 0)
			return;
		for (CuentaBancaria var : this.listCuentas) {
			if (var.getId() == idCuentaBancaria) {
				this.pojoFacPago.setIdCuentaDeposito(var.getId());
				this.pojoFacPago.setCuentaBanco(var.getInstitucionBancaria().getNombreCorto());
				this.pojoFacPago.setCuentaNumero(var.getNumeroDeCuenta());
				break;
			}
		}
	}
	
	public List<SelectItem> getListBancosItems() {
		return listBancosItems;
	}
	
	public void setListBancosItems(List<SelectItem> listBancosItems) {
		this.listBancosItems = listBancosItems;
	}
	
	public List<SelectItem> getListCuentasItems() {
		return listCuentasItems;
	}
	
	public void setListCuentasItems(List<SelectItem> listCuentasItems) {
		this.listCuentasItems = listCuentasItems;
	}
}

// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA    |        AUTOR       | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//    1.0  | 2016-10-18  | Javier Tirado      | Agregamos el listado de metodos de pago 
//    2.2  | 2017-05-15  | Javier Tirado      | Agilizamos la busqueda de obras (cambio de extendido a normal)
