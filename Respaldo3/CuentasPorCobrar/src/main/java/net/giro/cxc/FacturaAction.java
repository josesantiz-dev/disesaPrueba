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
import java.util.LinkedHashMap;
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
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraExt;
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
import net.giro.cxc.beans.FacturaPagosExt;
import net.giro.cxc.beans.FacturaTimbre;
import net.giro.cxc.beans.FacturasRelacionadas;
import net.giro.cxc.beans.FacturasRelacionadasExt;
import net.giro.cxc.logica.ConceptoFacturacionImpuestosRem;
import net.giro.cxc.logica.ConceptoFacturacionRem;
import net.giro.cxc.logica.FacturaDetalleImpuestoRem;
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaPagosRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.cxc.logica.FacturasRelacionadasRem;
import net.giro.cxc.util.FACTURA_ESTATUS;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.Utilerias;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Banco;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.logica.BancosRem;
import net.giro.tyg.logica.CuentasBancariasRem;
import net.giro.tyg.logica.FormasPagosRem;
import net.giro.tyg.logica.MonedasValoresRem;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="facturaAction")
public class FacturaAction implements Serializable, Runnable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FacturaAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	// Interfaces 
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private FacturaRem ifzFactura;
	private FacturasRelacionadasRem ifzFacturasRelacionadas;
	private FacturaDetalleRem ifzFacturaDetalle;
	private FacturaDetalleImpuestoRem ifzFacDetImpuestos;
	private ReportesRem ifzReportes;
	private FacturaPagosRem ifzPagos;
	private PersonaRem ifzPersonas;
	private NegociosRem ifzNegocios;
	private CuentasBancariasRem ifzCtas;
	private BancosRem ifzBancos;
	// POJO's
	private long idFactura;
	private FacturaExt pojoFactura;
	private FacturaDetalleExt pojoDetalle; 
	private FacturaDetalleImpuestoExt pojoDetalleImpuesto;
	private ObraExt pojoObraOriginal;
	// Listas
	private List<Factura> listFacturas;
	private List<FacturaDetalleExt> listDetalles;
	private List<FacturaDetalleExt> listDetallesEliminados;
	private List<FacturaDetalleImpuestoExt> listDetalleImpuestos;
	private List<FacturaDetalleImpuestoExt> listDetalleImpuestosEliminados;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;
	private List<SelectItem> listMeses;
	private String campoBusqueda;
	private String valorBusqueda;
	private Date busquedaFecha;
	private int busquedaMes;
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
	private long sucursalId;
	// Busqueda conceptos de facturacion
	private ConceptoFacturacionRem ifzConceptosFacturacion;
	private ConceptoFacturacionImpuestosRem ifzConFacImpuestos;
	private List<ConceptoFacturacion> listConceptoFacturacion;
	private ConceptoFacturacion pojoConcepto;
	private List<SelectItem> tiposBusquedaConceptos;	
	private String valorBusquedaConceptos;
	private String campoBusquedaConceptos;
	private int numPaginaConceptos;
	private boolean conceptosDuplidados;
	// Metodos de pago
	private ConGrupoValores pojoGpoMetodosPago; 
	private List<ConValores> listMetodosPago;
	private List<SelectItem> listMetodosPagoItems;
	private long idMetodoPago;
	// Formas de pago
	private FormasPagosRem ifzFormasPago;
	private List<FormasPagos> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
    private String usuarioEmail;
    private String usuarioEmailClave;
    private String emailBase;
    private String emailBaseClave;
	private int numPaginaDetalles;
	private int numPaginaImpuestos;
	private int indexDetalleFactura;
	private boolean perfilEgresos;
	private boolean buscarAdministrativo;
	private long idMonedaSugerida;
	private long idFormaPagoSugerido;
	private long idMetodoPagoSugerido;
    private double porcentajeIva;
    // Refacturacion
	private boolean refacturar;
	private boolean aplicarPagos;
	private long idFacturaPrevia;
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
	private int pagosRegistrados;// 0-Sin pagos
	// Email
	private String email;
	private String emailAsunto;
	private String emailCuerpo;
	private boolean anexarPDF;
	private boolean anexarXML;
	// Provisiones
	private boolean perfilProvisiones;
	private boolean permiteProvisionar;
	private double montoFacturado;
	private double montoPagado;
	private double montoProvision;
	// Moneda
	private MonedaDAO ifzMonedas;
	private MonedasValoresRem ifzMonValores;
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
	private String tipoComprobante;
	private String tipoComprobanteSugerido;
	private boolean cfdiRelacionado;
	private long idTipoRelacion;
	private String cfdiRelacionadoUuid;
	// Busqueda CFDI
	private List<Factura> listBusquedaCfdi;
	private Factura pojoCfdiSeleccionado;
	private List<SelectItem> tipoBusquedaCfdi;	
	private String valorBusquedaCfdi;
	private String campoBusquedaCfdi;
	private int numPaginaCfdi;
	// Busqueda Factura
	private FacturasRelacionadasExt pojoFacturasRelacionadasExt;
	private List<FacturasRelacionadasExt> listFacturasRelacionadasExt;
	private List<FacturasRelacionadas> listFacturasRelacionadas;
	private FacturasRelacionadas pojoFacturasRelacionadas;
	private List<Factura> listBusquedaFacturas;
	private Factura pojoFacturaSeleccionada;
	private List<SelectItem> tipoBusquedaFacturas; 
	private String valorBusquedaFacturas;
	private String campoBusquedaFacturas;
	private int numPaginaFacturas;
	// Pagos
	private List<CuentaBancaria> listCuentas;
	private List<SelectItem> listCuentasItems;
	private List<Banco> listBancos;
	private List<SelectItem> listBancosItems;
	private List<FacturaPagosExt> listFacturaPagos;
	private FacturaPagos pojoFacPago;
	private long idPago;
	private int paginacionPagos;
	// Notas Credito
	private List<Factura> listNotasCredito;
	private int paginacionNotasCredito;
	private long idNotaCredito;
	// PERFILES
	private boolean perfilPermitirAsignarFolio;
	private boolean ordenCronologico;
	// Auditoria
	private List<SelectItem> listAuditoriaItems;
	// Variables de control
	private boolean procesando;
	private boolean procesoIncompleto;
	private int tipoMensaje;
	private String mensaje;
	private boolean facturaTimbrada;
	private boolean editable;
	// DEBUG
	private boolean isDebug;
	private Map<String,String> paramsRequest;

	public FacturaAction() {
		InitialContext ctx = null;
		Moneda valMoneda = null;
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
			this.usuarioEmail = this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo();
			this.usuarioEmailClave = this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreoClave();
			if (this.usuarioEmailClave == null || "".equals(this.usuarioEmailClave.trim()))
				this.usuarioEmailClave = "Opdisesa_01";
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			// Obtenemmos los parametros de la URL si corresponde
			this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
		    	this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
	
			ctx = new InitialContext();
			this.ifzFactura = (FacturaRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacturaDetalle = (FacturaDetalleRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzFacturasRelacionadas = (FacturasRelacionadasRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturasRelacionadasFac!net.giro.cxc.logica.FacturasRelacionadasRem");
			this.ifzFacDetImpuestos = (FacturaDetalleImpuestoRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleImpuestoFac!net.giro.cxc.logica.FacturaDetalleImpuestoRem");
			this.ifzConceptosFacturacion = (ConceptoFacturacionRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//ConceptoFacturacionFac!net.giro.cxc.logica.ConceptoFacturacionRem");
			this.ifzConFacImpuestos = (ConceptoFacturacionImpuestosRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//ConceptoFacturacionImpuestosFac!net.giro.cxc.logica.ConceptoFacturacionImpuestosRem");
			this.ifzPagos = (FacturaPagosRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosFac!net.giro.cxc.logica.FacturaPagosRem");
			this.ifzObra = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzGpoVal = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzSucursales = (SucursalesRem) ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzMonedas = (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzMonValores = (MonedasValoresRem) ctx.lookup("ejb:/Logica_TYG//MonedasValoresFac!net.giro.tyg.logica.MonedasValoresRem");
			this.ifzFormasPago = (FormasPagosRem) ctx.lookup("ejb:/Logica_TYG//FormasPagosFac!net.giro.tyg.logica.FormasPagosRem");
			this.ifzPersonas = (PersonaRem) ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzNegocios = (NegociosRem) ctx.lookup("ejb:/Logica_Clientes//NegociosFac!net.giro.clientes.logica.NegociosRem");
			this.ifzCtas = (CuentasBancariasRem) ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
			this.ifzBancos = (BancosRem) ctx.lookup("ejb:/Logica_TYG//BancosFac!net.giro.tyg.logica.BancosRem");
			
			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFacturasRelacionadas.setInfoSesion(this.loginManager.getInfoSesion());
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
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzNegocios.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzBancos.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.ifzFactura.showSystemOuts(false);
			this.ifzFacturaDetalle.showSystemOuts(false);
			this.ifzConFacImpuestos.showSystemOuts(false);
			this.ifzFacturasRelacionadas.showSystemOuts(false);

			// Porcentaje IVA
			this.porcentajeIva = 1;
			valPerfil = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			if (valPerfil != null && ! "".equals(valPerfil))
				this.porcentajeIva = Double.valueOf(valPerfil);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			this.perfilEgresos = ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario())? true : this.perfilEgresos);

			valPerfil = this.loginManager.getAutentificacion().getPerfil("FACTURAS_PROVISION");
			this.perfilProvisiones = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			this.perfilProvisiones = ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario())? true : this.perfilProvisiones);

			valPerfil = this.loginManager.getAutentificacion().getPerfil("FACTURAS_FOLIO_MANUAL");
			this.perfilPermitirAsignarFolio = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			this.perfilPermitirAsignarFolio = ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario())? true : this.perfilPermitirAsignarFolio);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			valMoneda = this.ifzMonedas.findByAbreviacion(valPerfil);
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
			this.conceptoAnterior = 0L;
			this.subtotal = 0;
			this.impuestos = 0;
			this.total = 0;
			this.numPaginaDetalles = 1;
			this.numPaginaImpuestos = 1;
			this.indexDetalleFactura = -1;

			// Busqueda pricipal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("folioFactura","Folio Factura"));
			this.tiposBusqueda.add(new SelectItem("nombreObra","Obra"));
			this.tiposBusqueda.add(new SelectItem("cliente","Cliente"));
			this.tiposBusqueda.add(new SelectItem("fechaEmision","Fecha"));
			this.tiposBusqueda.add(new SelectItem("mes","Mes"));
			this.tiposBusqueda.add(new SelectItem("id","ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
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
			
			// Busqueda conceptos de facturacion
			this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
			this.tiposBusquedaConceptos.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaConceptos.add(new SelectItem("descripcion","Concepto"));
			this.tiposBusquedaConceptos.add(new SelectItem("id","ID"));
			busquedaConceptos();

			// Busqueda CFDI
			this.tipoBusquedaCfdi = new ArrayList<SelectItem>();
			this.tipoBusquedaCfdi.add(new SelectItem("*", "Coincidencia"));
			this.tipoBusquedaCfdi.add(new SelectItem("folioFactura", "Folio"));
			this.tipoBusquedaCfdi.add(new SelectItem("nombreObra", "Obra"));
			this.tipoBusquedaCfdi.add(new SelectItem("cliente", "Cliente"));
			this.tipoBusquedaCfdi.add(new SelectItem("id", "ID"));
			nuevaBusquedaCFDI();

			// Busqueda Facturas
			this.tipoBusquedaFacturas = new ArrayList<SelectItem>();
			this.tipoBusquedaFacturas.add(new SelectItem("*", "Coincidencia"));
			this.tipoBusquedaFacturas.add(new SelectItem("folioFactura", "Folio"));
			this.tipoBusquedaFacturas.add(new SelectItem("nombreObra", "Obra"));
			this.tipoBusquedaFacturas.add(new SelectItem("cliente", "Cliente"));
			this.tipoBusquedaFacturas.add(new SelectItem("id", "ID"));
			nuevaBusquedaFacturas();
			
			this.listCFDIVersionItems = new ArrayList<SelectItem>();
			this.listCFDIVersionItems.add(new SelectItem("3.3", "CFDI 3.3"));
			
			// Busqueda obras
			this.opcionesBusquedaObras = new ArrayList<SelectItem>();
			this.opcionesBusquedaObras.add(new SelectItem("*", "Coincidencia"));
			this.opcionesBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.opcionesBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.opcionesBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.opcionesBusquedaObras.add(new SelectItem("id", "Clave"));
			nuevaBusquedaObras();
			
			// OBTENEMOS EMAIL BASE
			// ---------------------------------------------------------------------------------------------------------
			this.emailBase = "";
			if (this.entornoProperties.containsKey("emailBase"))
				this.emailBase = this.entornoProperties.getString("emailBase");
			this.emailBaseClave = "";
			if (this.entornoProperties.containsKey("emailBaseClave"))
				this.emailBaseClave = this.entornoProperties.getString("emailBaseClave");
			
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
			for (CuentaBancaria var : this.listCuentas) 
				this.listCuentasItems.add(new SelectItem(var.getId(), var.getNumeroDeCuenta() + " - " + var.getInstitucionBancaria().getNombreCorto()));
			
			// Bancos
			this.listBancosItems.clear();
			this.listBancos = this.ifzBancos.findLikeColumnName("nombreCorto", "");
			for (Banco var : this.listBancos) 
				this.listBancosItems.add(new SelectItem(var.getId(), var.getId() + " - " + var.getNombreCorto()));
			
			this.cfdiVersionSugerida = "3.3";
			this.tipoComprobanteSugerido = "I";
			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
			this.paginacionPagos = 1;
			this.ordenCronologico = true;
			
			comprobarEmail();
			control();
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction", e);
		}
	}
   	
	public void buscar() {
		String campo = "";
		String orderBy = "";
		
		try {
			control();
			orderBy = "date(fechaEmision) DESC, folioFactura desc, id DESC";
			if (this.campoBusqueda == null || "".equals(this.campoBusqueda.trim()))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			if (this.isDebug && ! this.ordenCronologico)
				orderBy = "estatus DESC, fechaEmision DESC, id DESC";
			
			campo = this.campoBusqueda;
			if ("mes".equals(this.campoBusqueda)) {
				this.valorBusqueda = (new SimpleDateFormat("YYYY")).format(Calendar.getInstance().getTime()) + "-" + (this.busquedaMes < 10 ? "0" : "") + this.busquedaMes;
				campo = "date(fechaEmision)";
			}

			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			this.listFacturas = this.ifzFactura.findLikeProperty(campo, ("fechaEmision".equals(campo) ? this.busquedaFecha : this.valorBusqueda), 0L, "", 0, false, this.ordenCronologico, orderBy, 0);
			if (this.listFacturas == null || this.listFacturas.isEmpty())
				control(2, "Busqueda sin resultados", campo, ("fechaEmision".equals(campo) ? ((new SimpleDateFormat("dd-MM-yyyy")).format(this.busquedaFecha)) : this.valorBusqueda));
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar las Facturas", e);
		} finally {
			this.numPagina = 1;
			this.listFacturas = (this.listFacturas != null ? this.listFacturas : new ArrayList<Factura>());
			controlLog(this.listFacturas.size() + " Facturas encontradas");
		}
	}

	public void nuevo() {
		try {
			control();
			this.fechaEmision = Calendar.getInstance(this.timeZone).getTime();
			this.tipoComprobante = this.tipoComprobanteSugerido;
			this.idMetodoPago = this.idMetodoPagoSugerido;
			this.mensajeCancelacion = "";
			this.sucursalId = 0L;
			this.conceptoAnterior = 0L;
			this.subtotal = 0;
			this.impuestos = 0;
			this.retenciones = 0;
			this.total = 0;
			this.refacturar = false;
			
			this.idFactura = 0L;
			this.pojoFactura = new FacturaExt();
			this.pojoFactura.setTipo("X");
			this.pojoFactura.setFechaEmision(this.fechaEmision);
			this.pojoFactura.setTipoComprobante(this.tipoComprobante);
			this.pojoFactura.setIdMoneda(this.idMonedaSugerida);
			this.pojoFactura.setIdFormaPago(this.idFormaPagoSugerido);
			this.pojoFactura.setTasaIva(this.porcentajeIva);
			this.pojoFactura.setTipoCambio(1.0);
			
			this.pojoObraOriginal = new ObraExt();
			this.listDetallesEliminados = new ArrayList<FacturaDetalleExt>();
			this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalleImpuestosEliminados = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalles = new ArrayList<FacturaDetalleExt>();
			this.pojoDetalle = new FacturaDetalleExt();

			this.pojoFacturasRelacionadas = new FacturasRelacionadas();
			this.pojoFacturasRelacionadasExt = new FacturasRelacionadasExt();
			this.listFacturasRelacionadasExt = new ArrayList<FacturasRelacionadasExt>(); 
		} catch (Exception e) {
			control("Ocurrio un problema al instanciar una nueva Factura.", e);
		} finally {
			this.numPagina = 1;
			this.numPaginaDetalles = 1;
			this.numPaginaImpuestos = 1;
			
			// Evaluar edicion
			if (this.isDebug) controlLog("Validando timbre ... ");
			this.facturaTimbrada = false;
			if (this.isDebug) controlLog("Validando ESTATUS ... ");
			this.editable = true;
		}
	}

	public void editar() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String fechaEmisionFactura = "";
		
		try {
			control();
			controlLog("Proceso de edicion de Factura ... ");
			this.pojoFactura = this.ifzFactura.findExtById(this.idFactura);
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				control(-1, "No se pudo recuperar la Factura seleccionada");
				return;
			}
			
			// Inicializamos listas y variables de la factura
			this.sucursalId = 0L;
			this.listDetalles = new ArrayList<FacturaDetalleExt>();
			this.listDetallesEliminados = new ArrayList<FacturaDetalleExt>();
			this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalleImpuestosEliminados = new ArrayList<FacturaDetalleImpuestoExt>();
			this.pojoObraOriginal = new ObraExt();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.pojoObraOriginal, this.pojoFactura.getIdObra());
			nuevoConcepto();
			
			// Fecha Emision
			if (this.isDebug) controlLog("Compruebo fecha Emision ... ");
			if ("00:00:00".equals(formatter.format(this.pojoFactura.getFechaEmision()))) {
				formatter.applyPattern("MM-dd-yyyy");
				fechaEmisionFactura = formatter.format(this.pojoFactura.getFechaEmision());
				formatter.applyPattern("HH:mm:ss");
				fechaEmisionFactura = fechaEmisionFactura + " " + formatter.format(Calendar.getInstance(this.timeZone).getTime());
				formatter.applyPattern("MM-dd-yyyy HH:mm:ss");
				this.pojoFactura.setFechaEmision(formatter.parse(fechaEmisionFactura));
				if (this.isDebug) controlLog("Asigno fecha emision: " + fechaEmisionFactura);
			}
			this.fechaEmision = this.pojoFactura.getFechaEmision();
			
			// TipoDeComprobante
			if (this.isDebug) controlLog("Recuperando Tipo de Comprobante ... ");
			this.tipoComprobante = this.tipoComprobanteSugerido;
			if (this.pojoFactura.getTipoComprobante() != null && ! "".equals(this.pojoFactura.getTipoComprobante().trim())) 
				this.tipoComprobante = this.pojoFactura.getTipoComprobante().trim();

			// Sucursal 
			if (this.isDebug) controlLog("Recuperando sucursal ... ");
			if (this.pojoFactura.getIdSucursal() != null && this.pojoFactura.getIdSucursal().getId() > 0L)
				this.sucursalId = this.pojoFactura.getIdSucursal().getId();
			
			// Metodo de Pago
			if (this.isDebug) controlLog("Recuperando metodo de pago ... ");
			if (this.pojoFactura.getIdMetodoPago() != null && this.pojoFactura.getIdMetodoPago().getId() > 0L)
				this.idMetodoPago = this.pojoFactura.getIdMetodoPago().getId();
			
			// Detalles de factura (Conceptos)
			if (this.isDebug) controlLog("Recuperando detalles (Conceptos) de factura ... ");
			this.listDetalles = this.ifzFacturaDetalle.findAllExt(this.pojoFactura.getId());
			if (this.listDetalles == null || this.listDetalles.isEmpty()) {
				control(-1, "Ocurrio un problema al recuperar la Factura.\nLa Factura no tiene Conceptos");
				return;
			}

			// Cargamos los Pagos
			this.paginacionPagos = 1;
			this.listFacturaPagos = this.ifzPagos.findExtByFactura(this.pojoFactura.getId());
			if (this.listFacturaPagos != null && ! this.listFacturaPagos.isEmpty()) {
				Collections.sort(this.listFacturaPagos, new Comparator<FacturaPagosExt>() {
					@Override
					public int compare(FacturaPagosExt o1, FacturaPagosExt o2) {
						return o2.getFecha().compareTo(o1.getFecha());
					}
				});
			}
			
			// Cargamos las Notas de Credito
			this.paginacionNotasCredito = 1;
			this.listNotasCredito = this.ifzFactura.findNotasCreditoByFactura(this.pojoFactura.getId());
			if (this.listNotasCredito != null && ! this.listNotasCredito.isEmpty()) {
				Collections.sort(this.listNotasCredito, new Comparator<Factura>() {
					@Override
					public int compare(Factura o1, Factura o2) {
						return o2.getFechaEmision().compareTo(o1.getFechaEmision());
					}
				});
			}
			
			// Cargamos facturas relacionadas
			this.listFacturasRelacionadasExt = new ArrayList<FacturasRelacionadasExt>();
			this.listFacturasRelacionadas = ifzFacturasRelacionadas.findFacturasById(this.pojoFactura.getId());
			for (FacturasRelacionadas var : listFacturasRelacionadas) {
				this.pojoFacturasRelacionadasExt = new FacturasRelacionadasExt();
				this.pojoFacturaSeleccionada = ifzFactura.findById(var.getIdFacturaRelacionada());
				if(pojoFacturaSeleccionada != null){
					this.pojoFacturasRelacionadasExt.setIdFacturaRelacionada(this.pojoFacturaSeleccionada);
					this.pojoFacturasRelacionadasExt.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					this.pojoFacturasRelacionadasExt.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
					this.listFacturasRelacionadasExt.add(this.pojoFacturasRelacionadasExt);
				}
			}
			
			totalizar();
			controlLog("Factura lista para editar");
		} catch (Exception e) {
			control("Ocurrio un problema al procesar la factura seleccionada", e);
		} finally {
			this.numPagina = 1;
			this.numPaginaDetalles = 1;
			this.numPaginaImpuestos = 1;
			evaluamosEditable();
		}
	}
	
	public void guardar() {
		FacturaDetalle pojoDetalleAux = null;
		BigDecimal saldo = BigDecimal.ZERO;
		boolean esNueva = false;
		
		try {
			control();
			if (this.procesando)
				return;
			this.procesando = true;
			controlLog("Validando Factura ... ");
			if (! validarFactura())
				return;

			controlLog("Guardando Factura ... ");
			this.pojoFactura.setFolioFactura(this.pojoFactura.getSerie() + "-" + this.pojoFactura.getFolio());
			this.pojoFactura.setFechaVencimiento(Calendar.getInstance(this.timeZone).getTime());
			this.pojoFactura.setSubtotal(new BigDecimal(this.subtotal));
			this.pojoFactura.setImpuestos(this.impuestos);
			this.pojoFactura.setRetenciones(this.retenciones);
			this.pojoFactura.setTotal(new BigDecimal(this.total));

			controlLog("Guardando Factura ... ");
			if (this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				esNueva = true;
				saldo = new BigDecimal(this.total);
				if ("E".equals(this.pojoFactura.getTipoComprobante()))
					saldo = BigDecimal.ZERO;

				this.pojoFactura.setSaldo(saldo);
				this.pojoFactura.setEstatus(FACTURA_ESTATUS.Activa);
				this.pojoFactura.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoFactura.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
				this.pojoFactura.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoFactura.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				
				// Guardamos la factura y la añadimos al listado
				this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoFactura.setId(this.ifzFactura.save(this.pojoFactura));
				this.listFacturas.add(0, this.ifzFactura.convertir(this.pojoFactura));
				
			} else {
				saldo = BigDecimal.ZERO;
				if ("I".equals(this.pojoFactura.getTipoComprobante())) {
					this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
					saldo = this.ifzFactura.recalculaSaldo(this.pojoFactura.getId(), this.pojoFactura.getTotal());
				}
				this.pojoFactura.setSaldo(saldo);
				this.pojoFactura.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoFactura.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				
				// Actualizamos la factura en la BD y en la lista
				this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzFactura.update(this.pojoFactura);
				for (Factura var : this.listFacturas) {
					if (this.pojoFactura.getId().longValue() == var.getId().longValue()) {
						this.listFacturas.set(this.listFacturas.indexOf(var), this.ifzFactura.findById(this.pojoFactura.getId()));
						controlLog("Factura actualizada");
						break;
					}
				}
			}			
			this.idFactura = this.pojoFactura.getId();
			controlLog("Factura guardada: " + this.idFactura);
			

			controlLog("Guardando Facturas Relacionadas: " + this.idFactura);
			this.ifzFacturasRelacionadas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzFacturasRelacionadas.deleteFacturasRelacionadas(this.idFactura);
			for (FacturasRelacionadasExt var : listFacturasRelacionadasExt) {
				this.pojoFacturasRelacionadas = new FacturasRelacionadas();
				this.pojoFacturasRelacionadas.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoFacturasRelacionadas.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
				this.pojoFacturasRelacionadas.setIdFactura(this.idFactura);
				this.pojoFacturasRelacionadas.setIdFacturaRelacionada(var.getIdFacturaRelacionada().getId());
				this.pojoFacturasRelacionadas.setCfdiRelacionadoUuid(var.getIdFacturaRelacionada().getUuid());
				var.setId(this.ifzFacturasRelacionadas.save(this.pojoFacturasRelacionadas));
			}
			
			// Enlazando detalles a la factura
			controlLog("Guardando detalles (" + this.listDetalles.size() + ") de Factura ... ");
			for (FacturaDetalleExt detalle : this.listDetalles) {
				controlLog("Asignando concepto " + detalle.getIdConcepto().getDescripcion() + " ... ");
				detalle.setIdFactura(this.pojoFactura);
				if (detalle.getId() == null || detalle.getId() <= 0L) {
					detalle.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					detalle.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
				}
				detalle.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				detalle.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
			}
			
			// Guardando los detalles de la factura
			this.ifzFacturaDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.listDetalles = this.ifzFacturaDetalle.saveOrUpdateListExt(this.listDetalles);
			controlLog("Detalles guardados.");
			
			// Enlazando impuestos a los detalles de la factura
			controlLog("Guardando Impuestos ... ");
			for (FacturaDetalleExt var : this.listDetalles) {
				this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
				this.listDetalleImpuestos = var.getListImpuestos();
				if (this.listDetalleImpuestos == null || this.listDetalleImpuestos.isEmpty()) 
					continue;
				
				pojoDetalleAux = this.ifzFacturaDetalle.convertir(var);
				for (FacturaDetalleImpuestoExt imp : this.listDetalleImpuestos) {
					imp.setIdFacturaDetalle(pojoDetalleAux);
					if (imp.getId() == null || imp.getId() <= 0L) {
						imp.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						imp.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
					}
					imp.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					imp.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				}
				
				// Guardando los impuestos del detalle de la factura
				this.ifzFacDetImpuestos.setInfoSesion(this.loginManager.getInfoSesion());
				this.listDetalleImpuestos = this.ifzFacDetImpuestos.saveOrUpdateListExt(this.listDetalleImpuestos);
				var.setListImpuestos(this.listDetalleImpuestos);
			}
			controlLog("Impuestos guardados.");
			
			// Comprobamos y eliminamos los detalles previamente borrados de la factura
			if (this.listDetallesEliminados != null && ! this.listDetallesEliminados.isEmpty()) {
				controlLog("Eliminando detalles previamente borrados ... ");
				for (FacturaDetalleExt var: this.listDetallesEliminados) {
					if (var.getId() == null || var.getId() <= 0L)
						continue;
					this.ifzFacturaDetalle.delete(var.getId());
				}
				
				// Limpiamos el listado de detalles eliminados
				this.listDetallesEliminados.clear();
				controlLog("Detalles eliminados.");
			}
			
			// Comprobamos y eliminamos los impuestos previamente borrados de los conceptos
			if (this.listDetalleImpuestosEliminados != null && ! this.listDetalleImpuestosEliminados.isEmpty()) {
				controlLog("Eliminando impuestos de detalles previamente borrados ... ");
				for (FacturaDetalleImpuestoExt var : this.listDetalleImpuestosEliminados) {
					if (var.getId() == null || var.getId() <= 0L)
						continue;
					this.ifzFacDetImpuestos.delete(var.getId());
				}
				
				// Limpiamos el listado de impuestos eliminados
				this.listDetalleImpuestosEliminados.clear();
				controlLog("Impuestos eliminados.");
			}
			
			// Comprobamos si debemos aplicar los Pagos de la Factura anterior en caso de Refacturacion
			if (this.aplicarPagos) {
				this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzPagos.guardarPagoAplicable(this.idFacturaPrevia, this.idFactura);
				this.idFacturaPrevia = 0L;
				this.aplicarPagos = false;
			}
			
			if (! esNueva) {
				this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzFactura.cobranzaUbicacionPrevia(this.pojoFactura.getId());
			}
			controlLog("Factura guardada: " + this.pojoFactura.getId());
			control();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Factura", e);
		} finally {
			this.refacturar = false;
			this.procesando = false;
			evaluamosEditable();
		}
	}
	
	public void cancelar() {
		Respuesta respuesta = null;
		Factura factura = null;
		// ----------------------------
		boolean testing = false;

		if (this.procesando)
			return;
		
		try {
			control();
			this.procesando = true;
			testing = validaRequest("CFDI_TEST");
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				control(-1, "Debe indicar la Factura para Cancelar");
				return;
			}
			
			testing = ((this.pojoFactura.getIdTimbre() != null && this.pojoFactura.getIdTimbre().validarTimbrada()) ? (this.pojoFactura.getIdTimbre().getPruebas() == 1) : testing);
			controlLog("Cancelando Factura " + this.pojoFactura.getFolioFactura() + " (" + this.pojoFactura.getId() + ") ..." + (this.isDebug ? " DEBUG" : "") + (testing ? " TESTING" : ""));
			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzFactura.cancelarFactura(this.pojoFactura, this.mensajeCancelacion, this.isDebug, testing);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO: " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al intentar cancelar la Factura.\n" + respuesta.getErrores().getDescError());
				if (! testing)
					return;
			}
			
			// Actualizamos en la lista
			if (this.isDebug) controlLog("Factura cancelada. Actualizando listado de facturas ... ");
			factura = (Factura) respuesta.getBody().getValor("factura");
			this.pojoFactura = (FacturaExt) respuesta.getBody().getValor("extendida"); 
			for (Factura var : this.listFacturas) { 
				if (var.getId().longValue() == factura.getId().longValue()) {
					this.listFacturas.set(this.listFacturas.indexOf(var), factura);
					break;
				}
			}
			
			controlLog("Proceso cancelacion terminado");
			controlLog("14 - Factura Cancelada: " + factura.getFolioFactura());
			// Refacturamos si corresponde
			if (this.refacturar) {
				refacturar();
				return;
			}
			
			// Reiniciamos valores
			nuevo();
			control(false, 14, "Factura Cancelada", null, null, null);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Cancelar la Factura", e);
		} finally {
			this.procesando = false;
			evaluamosEditable();
		}
	}
	
	public void refacturar() {
		List<FacturaDetalleExt> conceptos = null;
		
		try {
			control();
			// Simulo edicion para recuperar conceptos e impuestos
			controlLog("Refacturando ... ");
			this.idFactura = this.pojoFactura.getId();
			if (this.aplicarPagos)
				this.idFacturaPrevia = this.pojoFactura.getId();
			editar();
			
			// Reinicio valores de factura
			controlLog("Reinicio valores de factura previa ... ");
			this.editable = true;
			//this.puedeTimbrar = false;
			this.facturaTimbrada = false;
			this.idMetodoPago = this.idMetodoPagoSugerido;
			this.fechaEmision = Calendar.getInstance(this.timeZone).getTime();
			this.sucursalId = this.pojoFactura.getIdSucursal().getId();
			
			this.pojoFactura.setId(null);
			this.pojoFactura.setFechaEmision(this.fechaEmision);
			this.pojoFactura.setUuid("");
			this.pojoFactura.setSerie("");
			this.pojoFactura.setFolio("");
			this.pojoFactura.setFolioFactura("");
			this.pojoFactura.setTimbrado(0);
			this.pojoFactura.setTimbradoPor(0L);
			this.pojoFactura.setFechaTimbrado(null);
			this.pojoFactura.setIdTimbre(null);
			this.pojoFactura.setNoCertificadoSat("");
			this.pojoFactura.setSelloSat("");
			this.pojoFactura.setUsoCfdi("");
			this.pojoFactura.setFechaTimbrado(null);
			this.pojoFactura.setCfdi(null);
			this.pojoFactura.setTimbre(null);
			this.pojoFactura.setXml(null);
			this.pojoFactura.setSello(null);
			this.pojoFactura.setCadenaOriginal(null);
			this.pojoFactura.setObservaciones("");
			this.pojoFactura.setCanceladoPor(0);
			this.pojoFactura.setFechaCancelacion(null);
			this.pojoFactura.setMensajeCancelacion("");
			this.pojoFactura.setEstatus(FACTURA_ESTATUS.Activa);
			evaluaFacturaFolio();
			
			// Desligamos conceptos de la factura para que se agreguen como nuevos
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			conceptos = new ArrayList<FacturaDetalleExt>();
			conceptos.addAll(this.listDetalles);
			this.listDetalles.clear();
			for (FacturaDetalleExt concepto : conceptos) {
				if (concepto.getIdConcepto() == null || concepto.getIdConcepto().getId() == null || concepto.getIdConcepto().getId() <= 0L) 
					continue;
				this.conceptoAnterior = 0L;
				this.pojoDetalle = new FacturaDetalleExt();
				BeanUtils.copyProperties(this.pojoDetalle, concepto);
				this.pojoDetalle.setIdFactura(this.pojoFactura);
				this.pojoDetalle.setId(null);
				this.listDetalles.add(this.pojoDetalle);
				desglozarImpuestos();
			}
			
			this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
			totalizar();
			controlLog("Proceso refacturacion terminado");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar refacturar la Factura cancelada", e);
		} 
	}
	
	public void noCancelar() {
		try {
			// Borro lo respaldo (Factura)
			this.refacturar = false;
			this.idFactura = 0L;
		} catch (Exception e) {
			control("Ocurrio un problema desconocido con la Factura seleccionada", e);
		}
	}
	
	public void noRefacturar() {
		try {
			// Borro lo respaldo (Factura)
			this.refacturar = false;
			this.idFactura = 0L;
		} catch (Exception e) {
			control("Ocurrio un problema desconocido con la Factura seleccionada", e);
		}
	}
	
	public void imprimir() {
		HashMap<String, Object> paramsReporte = null;
		HashMap<String, String> params = null;
		Respuesta respuesta = null;
		String nombreDocumento = "";
		
		try {
			control();
			controlLog("Imprimiendo reporte ... ");
			if (this.idFactura <= 0L) {
				control(-1, "Debe indicar una Factura");
				return;
			}
			
			this.pojoFactura = this.ifzFactura.findExtById(this.idFactura);
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				controlLog("Reporte abortado: El POJO Factura es nulo");
				control(-1, "No se pudo recuperar la Factura para su impresion");
				return;
			}
			
			if (! "".equals(this.pojoFactura.getFolioFactura()))
				nombreDocumento = "F-" + this.pojoFactura.getFolioFactura() + ".pdf";
			else
				nombreDocumento = "F-" + this.pojoFactura.getId() + ".pdf";

			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idFactura", this.pojoFactura.getId());
			
			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.factura"));
			params.put("nombreDocumento", nombreDocumento);
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte, null);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				controlLog("ERROR INTERNO: No se pudo procesar el reporte de Factura.\n" + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema y No se pudo procesar el reporte de Factura.\nContacte a su Administrador");
				return;
			}

			this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
			this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte")); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento); 
			controlLog("Reporte lanzado");
    	} catch (Exception e) {
    		control("Ocurrio un problema al enviar la Factura por correo electronico", e);
    	}
	}
	
	public void recuperarXML() {
		Respuesta respuesta = null;
		FacturaTimbre timbre = null;
		FacturaExt extendida = null;
		String nombreDocumento = "";
		byte[] contenidoDocumento = null;
		
		try {
			control();
			controlLog("Descargando XML ... ");
			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			extendida = this.ifzFactura.findExtById(this.idFactura);
			if (extendida == null || extendida.getId() == null || extendida.getId() <= 0L)
				extendida = this.ifzFactura.findExtById(this.idFactura);
			else if (extendida.getId().longValue() != this.idFactura)
				extendida = this.ifzFactura.findExtById(this.idFactura);
			if (extendida == null || extendida.getId() == null || extendida.getId() <= 0L) {
				control(-1, "100 - Ocurrio un problema al intentar recuperar la Factura.\nDescarga abortada");
				return;
			}
			
			timbre = extendida.getIdTimbre();
			if (timbre == null || timbre.getId() == null || timbre.getId() <= 0L || timbre.getCfdi() == null) {
				respuesta = this.ifzFactura.consultarEstatus(this.idFactura);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					control(-1, "101 - Ocurrio un problema al intentar recuperar el XML de la Factura.\nDescarga abortada");
					return;
				}

				timbre = (FacturaTimbre) respuesta.getBody().getValor("timbre");
				if (timbre == null || timbre.getId() == null || timbre.getId() <= 0L || timbre.getCfdi() == null) {
					control(-1, "101 - Ocurrio un problema al intentar recuperar el XML de la Factura.\nDescarga abortada");
					return;
				}
			}
			
			nombreDocumento = "F-" + extendida.getFolioFactura() + ".xml";
			contenidoDocumento = timbre.getCfdi();
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				control(-1, "102 - Ocurrio un problema al intentar recuperar el XML de la Factura.\nDescarga abortada");
				return;
			}

			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", "xml");
			controlLog("Descarga lanzada");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el XML de la factura", e);
		}
	}

	public void recuperarXMLPrevio() {
		FacturaExt extendida = null;
		String comprobante = null;
		// ------------------------------
		String nombreDocumento = "";
		byte[] contenidoDocumento = null;
		
		try {
			control();
			controlLog("Descargando XML ... ");
			// Recuperamos Factura
			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			extendida = this.ifzFactura.findExtById(this.idFactura);
			if (extendida == null || extendida.getId() == null || extendida.getId() <= 0L)
				extendida = this.ifzFactura.findExtById(this.idFactura);
			else if (extendida.getId().longValue() != this.idFactura)
				extendida = this.ifzFactura.findExtById(this.idFactura);
			if (extendida == null || extendida.getId() == null || extendida.getId() <= 0L) {
				control(-1, "100 - Ocurrio un problema al intentar recuperar la Factura.\nDescarga abortada");
				return;
			}

			// Recuperamos XML
			if (extendida.getIdTimbre() == null || extendida.getIdTimbre().getId() == null || extendida.getIdTimbre().getId() <= 0L) {
				comprobante = this.ifzFactura.formarXML(this.idFactura);
				if (comprobante != null && ! "".equals(comprobante.trim()))
					contenidoDocumento = comprobante.getBytes();
			} else if (extendida.getIdTimbre() != null && extendida.getXml() != null) {
				contenidoDocumento = extendida.getIdTimbre().getXml();
			} else {
				comprobante = this.ifzFactura.formarXML(this.idFactura);
				if (comprobante != null && ! "".equals(comprobante.trim()))
					contenidoDocumento = comprobante.getBytes();
			}

			nombreDocumento = "F-" + extendida.getFolioFactura() + "-prev.xml";
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				control(-1, "102 - Ocurrio un problema al intentar recuperar el XML de la factura.\nDescarga abortada");
				return;
			}

			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", "xml");
			controlLog("Descarga lanzada");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el XML de la factura", e);
		}
	}
	
	public void totalizar() {
		this.subtotal = 0;
		this.impuestos = 0;
		this.retenciones = 0;
		this.total = 0;
		
		if (this.listDetalles == null || this.listDetalles.isEmpty()) {
			controlLog("Factura sin conceptos");
			return;
		}

		if (this.isDebug) controlLog("Totalizando ... ");
		for (FacturaDetalleExt var : this.listDetalles) {
			this.subtotal 	 += Utilerias.redondear(var.getImporte().doubleValue(), 2);
			this.impuestos   += Utilerias.redondear(var.getImpuestos().doubleValue(), 2);
			this.retenciones += Utilerias.redondear(var.getRetenciones().doubleValue(), 2);
		}
		
		this.total = this.subtotal + (this.impuestos - this.retenciones);
		this.total = Utilerias.redondear(this.total, 2);
		if (this.pojoFactura != null) {
			this.pojoFactura.setSubtotal(new BigDecimal(this.subtotal));
			this.pojoFactura.setImpuestos(this.impuestos);
			this.pojoFactura.setRetenciones(this.retenciones);
			this.pojoFactura.setTotal(new BigDecimal(this.total));
		}
	}
	
	public void evaluaCancelar() {
		Respuesta respuesta = null;
		
		try {
			control();
			if (this.idFactura <= 0L) {
				control(-1, "No indico ninguna Factura");
				return;
			}
			
			controlLog("Evaluando para Cancelar ... Factura: " + this.idFactura);
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L)
				this.pojoFactura = this.ifzFactura.findExtById(this.idFactura);
			if (this.pojoFactura.getId().longValue() != this.idFactura)
				this.pojoFactura = this.ifzFactura.findExtById(this.idFactura);
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				control(-1, "Ocurrio un problema al evaluar la Factura");
				return;
			}
			
			this.refacturar = false;
			this.pagosRegistrados = 0;
			this.mensajeCancelacion = "";
			respuesta = this.ifzFactura.evaluaCancelacion(this.idFactura);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO: " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError()); 
				control(-1, "La Factura seleccionada no se puede Cancelar.\n" + respuesta.getErrores().getDescError());
				return;
			}
			
			this.pojoFactura = (FacturaExt) respuesta.getBody().getValor("extendida");
			this.pagosRegistrados = (int) respuesta.getBody().getValor("pagosRegistrados");
			this.total = this.pojoFactura.getTotal().doubleValue();
			controlLog("Evaluacion terminada!");
		} catch (Exception e) {
			control("Ocurrio un problema al evaluar la Factura para cancelacion", e);
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
				if ("03".equals(var.getClaveSat())) {
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

	public void seleccionaTipoCambio() {
		try {
			control();
			seleccionaTipoCambio(this.fechaEmision);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el Tipo de Cambio con la fecha del Pago.\nPor favor indiquelo manualmente", e);
		} 
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
			if (this.pojoFactura.getIdFormaPago().longValue() == var.getId().longValue()) {
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
	
	public void cargarCFDIUsos() {
		try {
    		control();
    		if (this.isDebug) controlLog("Recuperando TIPOS DE USO PARA CFDI ... ");
			this.listCFDIUsoItems = new ArrayList<SelectItem>();
			this.listCFDIUso = this.ifzConValores.findAll(this.grupoCFDIUsos, "valor", 0);
			if (this.listCFDIUso == null || this.listCFDIUso.isEmpty()) {
	    		controlLog("Sin TIPOS DE USO PARA CFDI");
				return;
			}
			
			for (ConValores var : this.listCFDIUso) {
				if ("P01".equals(var.getValor()))
					this.idUsoCfdiSugerido = var.getId();
				this.listCFDIUsoItems.add(new SelectItem(var.getId(), var.getValor() + " - " + var.getDescripcion()));
			}
			if (this.isDebug) controlLog(this.listCFDIUso.size() + " TIPOS DE USO PARA CFDI recuperandos");
		} catch (Exception e) {
    		control("Ocurrio un problema al consultar el catalogo de Usus de CFDI", e);
		}
	}

	public void cargarCFDITiposRelacion() {
		try {
    		control();
    		if (this.isDebug) controlLog("Recuperando TIPOS DE RELACION PARA CFDI ... ");
			this.listCFDITipoRelacionItems = new ArrayList<SelectItem>();
			this.listCFDITipoRelacion = this.ifzConValores.findAll(this.grupoCFDITipoRelacion, "valor", 0);
			if (this.listCFDITipoRelacion == null || this.listCFDITipoRelacion.isEmpty()) {
	    		controlLog("Sin TIPOS DE RELACION PARA CFDI");
				return;
			}
			
			for (ConValores var : this.listCFDITipoRelacion)
				this.listCFDITipoRelacionItems.add(new SelectItem(var.getId(), var.getValor() + " - " + var.getDescripcion()));
			if (this.isDebug) controlLog(this.listCFDITipoRelacion.size() + " TIPOS DE RELACION PARA CFDI recuperandos");
		} catch (Exception e) {
    		control("Ocurrio un problema al consultar el catalogo de Tipos de Relacion para CFDI", e);
		}
	}

	public void cargarCFDITiposComprobante() {
		try {
    		control();
    		if (this.isDebug) controlLog("Recuperando TIPOS DE COMPROBANTE PARA CFDI ... ");
			this.listCFDITipoComprobanteItems = new ArrayList<SelectItem>();
			this.listCFDITipoComprobante = this.ifzConValores.findAll(this.grupoCFDITipoComprobante, "valor", 0);
			if (this.listCFDITipoComprobante == null || this.listCFDITipoComprobante.isEmpty()) {
	    		controlLog("Sin TIPOS DE COMPROBANTE PARA CFDI");
				return;
			}
			
			// Generamos listado, restringimos a Ingreso y Egreso
			for (ConValores var : this.listCFDITipoComprobante) {
				if (! "I E".contains(var.getValor()))
					continue;
				this.listCFDITipoComprobanteItems.add(new SelectItem(var.getValor(), var.getDescripcion(), ("I".equals(var.getValor()) ? "0" : var.getValor())));
			}
			
			Collections.sort(this.listCFDITipoComprobanteItems, new Comparator<SelectItem>() {
				@Override
				public int compare(SelectItem o1, SelectItem o2) {
					return o1.getDescription().compareTo(o2.getDescription());
				}
			});
			
			if (this.isDebug) controlLog(this.listCFDITipoComprobante.size() + " TIPOS DE COMPROBANTE PARA CFDI recuperandos");
		} catch (Exception e) {
    		control("Ocurrio un problema al consultar el catalogo de Tipos de Comprobante para CFDI", e);
		}
	}
	
	public void cargarFormasPagos() {
		try {
    		control();
			this.listFormasPagoItems = new ArrayList<SelectItem>();
			this.listFormasPago = this.ifzFormasPago.findAll("claveSat");
			if (! this.listFormasPago.isEmpty()) {
				for (FormasPagos var : this.listFormasPago) {
					if ("99".equals(var.getClaveSat()))
						this.idFormaPagoSugerido = var.getId();
					this.listFormasPagoItems.add(new SelectItem(var.getId(), var.getClaveSat() + " - " + var.getFormaPago()));
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar el catalogo de Formas de Pagos", e);
		}
	}

	public void cargarMetodosPagos() {
		try {
    		control();
    		if (this.isDebug) controlLog("Recuperando METODOS DE PAGO ... ");
			this.listMetodosPagoItems = new ArrayList<SelectItem>();
			this.listMetodosPago = this.ifzConValores.findAll(this.pojoGpoMetodosPago, "valor", 0);
			if (this.listMetodosPago == null || this.listMetodosPago.isEmpty()) {
	    		controlLog("Sin METODOS DE PAGO");
				return;
			}
			
			for (ConValores var : this.listMetodosPago) {
				if ("PPD".equals(var.getValor()))
					this.idMetodoPagoSugerido = var.getId();
				this.listMetodosPagoItems.add(new SelectItem(var.getId(), var.getValor() + " - " + var.getDescripcion()));
			}
			if (this.isDebug) controlLog(this.listMetodosPago.size() + " METODOS DE PAGO recuperandos");
		} catch (Exception e) {
    		control("Ocurrio un problema al consultar el catalogo de Metodos de Pagos", e);
		}
	}

	public void cargarSucursales() {
		try {
			control();
			if (this.isDebug) controlLog("Cargando sucursales");
			this.listBusquedaSucursalItems = new ArrayList<SelectItem>();
			this.listBusquedaSucursal = this.ifzSucursales.findLikePropiedad("sucursal", ""); 
			if (this.listBusquedaSucursal == null || this.listBusquedaSucursal.isEmpty()) {
				controlLog("Ninguna sucursal encontrada");
				return;
			}

			for (Sucursal var : this.listBusquedaSucursal) {
				if ("loreto".equals(var.getSucursal().trim().toLowerCase()))
					continue;
				this.listBusquedaSucursalItems.add(new SelectItem(var.getId(), var.getSucursal()));
			}
			if (this.isDebug) controlLog(this.listBusquedaSucursal.size() + " sucursales encontradas");
		} catch (Exception e) {
    		control("Ocurrio un problema al consultar el catalogo de Sucursales", e);
		}
	}

	public void auditoria() {
		LinkedHashMap<String, String> auditoria = null;
		
		try {
			control();
			if (this.idFactura <= 0L) {
				control(-1, "No se indico Factura o la Factura no ha sido guardada");
				return;
			}
			
			this.listAuditoriaItems = new ArrayList<SelectItem>();
			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			auditoria = this.ifzFactura.auditoria(this.idFactura);
			if (auditoria == null || auditoria.isEmpty()) {
				control(-1, "No se indico Factura o la Factura no ha sido guardada");
				return;
			}
			
			for (Entry<String, String> entry : auditoria.entrySet()) 
				this.listAuditoriaItems.add(new SelectItem(entry.getValue(), entry.getKey()));
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la informacion de Auditoria de la Factura", e);
		}
	}
	
	private void cargarMonedas() {
		try {
			this.listMonedasItems = new ArrayList<SelectItem>();
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
		List<Factura> lista = null;
		SimpleDateFormat formatter = null;
		Respuesta respuesta = null;
		Sucursal pojoSucursal = null;
		String dateStr = "";
		long folio = 0;
		
		controlLog("Validando Tipo de Comprobante ...");
		if (this.tipoComprobante == null || "".equals(this.tipoComprobante.trim())) {
			controlLog("ERROR INTERNO: Indique Tipo de Comprobante");
			control(3, "Indique Tipo de Comprobante");
			return false;
		}
		
		controlLog("Validando Detalles (Conceptos) ... ");
		if (this.listDetalles.isEmpty()) {
			controlLog("ERROR 3: Factura sin detalles");
			control(3, "Factura sin detalles");
			return false;
		}

		controlLog("Validando Metodo de Pago ... ");
		if (this.idMetodoPago <= 0L) {
			controlLog("ERROR INTERNO: No se asigno ninguno Metodo de Pago a la Factura");
			control(-1, "Debe indicar una Metodo de Pago");
			return false;
		}

		controlLog("Validando Moneda ... ");
		if (this.pojoFactura.getIdObra() == null || this.pojoFactura.getIdObra().getId() == null || this.pojoFactura.getIdObra().getId() <= 0L) {
			controlLog("ERROR INTERNO: No se asigno ninguno Obra a la Factura");
			control(-1, "Debe indicar una Obra");
			return false;
		}

		controlLog("Validando Sucursal ... ");
		if (this.sucursalId <= 0L) {
			controlLog("ERROR INTERNO: No se asigno ninguno Sucursal a la Factura");
			control(-1, "Debe indicar una Sucursal");
			return false;
		} else {
			controlLog("Obteniendo Sucursal seleccionada: " + this.sucursalId);
			for (Sucursal sucursal : this.listBusquedaSucursal) {
				if (this.sucursalId != sucursal.getId())
					continue;
				pojoSucursal = sucursal;
				break;
			}
			
			if (pojoSucursal == null) {
				controlLog("ERROR INTERNO: No se asigno ninguno Sucursal a la Factura");
				control(-1, "Debe indicar una Sucursal");
				return false;
			}
		}
		
		controlLog("Comprobante TipoDeComprobante ... ");
		this.pojoFactura.setTipoComprobante(this.tipoComprobante);
		if ("E".equals(this.pojoFactura.getTipoComprobante())) {
			this.pojoFactura.setSaldo(BigDecimal.ZERO);
			this.pojoFactura.setCondicionesPago("Contado");
			this.pojoFactura.setTipo("X");
		}

		if (this.pojoFactura.getTipo() == null || "".equals(this.pojoFactura.getTipo())) {
			this.pojoFactura.setTipo("X");
			this.pojoFactura.setCondicionesPago("Contado");
		}
		
		// Asigno condiciones de pago si corresponde
		if ("C".equals(this.pojoFactura.getTipo()) && (this.pojoFactura.getCondicionesPago() == null || "".equals(this.pojoFactura.getCondicionesPago())))
			this.pojoFactura.setCondicionesPago("0");
		
		controlLog("Comprobando MetodoDePago ... ");
		if (this.pojoFactura.getIdMetodoPago() == null || this.pojoFactura.getIdMetodoPago().getId() <= 0L || this.idMetodoPago != this.pojoFactura.getIdMetodoPago().getId()) {
			controlLog("Asignando MetodoDePago: " + this.monedaBase.getNombre());
			for (ConValores metodoPago : this.listMetodosPago) {
				if (this.idMetodoPago == metodoPago.getId()) {
					this.pojoFactura.setIdMetodoPago(metodoPago);
					break;
				}
			}
		}

		controlLog("Comprobando Moneda ... ");
		if ((this.pojoFactura.getIdMoneda() == null || this.pojoFactura.getIdMoneda() <= 0L) && this.monedaBase != null) {
			controlLog("Asignando Moneda: " + this.monedaBase.getNombre());
			this.pojoFactura.setIdMoneda(this.monedaBase.getId());
			this.pojoFactura.setDescripcionMoneda(this.monedaBase.getNombre());
			this.pojoFactura.setAbreviaturaMoneda(this.monedaBase.getAbreviacion());
		} else {
			for (Moneda moneda : this.listMonedas) {
				if (this.pojoFactura.getIdMoneda().longValue() == moneda.getId()) {
					controlLog("Asignando Moneda: " + moneda.getNombre());
					this.pojoFactura.setIdMoneda(moneda.getId());
					this.pojoFactura.setDescripcionMoneda(moneda.getNombre());
					this.pojoFactura.setAbreviaturaMoneda(moneda.getAbreviacion());
					break;
				}
			}
		}

		controlLog("Comprobando Tipo de Cambio ... ");
		this.pojoFactura.setTipoCambio((this.pojoFactura.getTipoCambio() > 0) ? this.pojoFactura.getTipoCambio() : 1.0);
		if (this.pojoFactura.getIdMoneda().longValue() != this.monedaBase.getId() && this.pojoFactura.getTipoCambio() <= 1)
			seleccionaTipoCambio(this.fechaEmision);

		controlLog("Comprobando Cliente ... ");
		if (! validaDatosCliente()) {
			controlLog("Asignando Cliente: " + this.pojoFactura.getIdObra().getIdCliente().getId() + " - " + this.pojoObra.getNombreCliente());
			asignarDatosCliente();
		}

		controlLog("Comprobando Sucursal ... ");
		if (this.pojoFactura.getIdSucursal() == null || this.pojoFactura.getIdSucursal().getId() <= 0L) {
			controlLog("Asignando Sucursal: " + pojoSucursal.getId() + " - " + pojoSucursal.getSucursal());
			this.pojoFactura.setIdSucursal(pojoSucursal);
			this.pojoFactura.setNombreSucursal(pojoSucursal.getSucursal());
		}
		
		controlLog("Comprobando Serie ... ");
		if (this.pojoFactura.getSerie() == null || "".equals(this.pojoFactura.getSerie())) {
			if (pojoSucursal.getSerie() == null || "".equals(pojoSucursal.getSerie().trim())) {
				controlLog("ERROR 9: Sucursal sin serie y/o folio asignadas para facturacion");
				control(9, "Sucursal sin serie y/o folio asignadas para facturacion");
				return false;
			} 
			
			// Asigno Serie tomada desde Sucursal
			this.pojoFactura.setSerie(pojoSucursal.getSerie());
		}

		controlLog("Comprobando Folio ... ");
		if (this.pojoFactura.getFolio() == null || "".equals(this.pojoFactura.getFolio()) || "0".equals(this.pojoFactura.getFolio())) {
			// Asignamos folio obtenida de la sucursal
			try {
				controlLog("Obteniendo Serie y Folio desde Sucursal ... " + pojoSucursal.getId() + " - " + pojoSucursal.getSucursal());
				respuesta = this.ifzSucursales.folioFacturacion(pojoSucursal);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					if (respuesta.getErrores().getCodigoError() == -1) {
						controlLog("ERROR 8: Sucursal no existe");
						control(8, "Sucursal no existe");
						return false;
					}
					
					if (respuesta.getErrores().getCodigoError() == -2) {
						controlLog("ERROR 9: La Sucursal no tiene asignada Serie para facturacion");
						control(9, "La Sucursal no tiene asignada Serie para facturacion");
						return false;
					}
				}
				
				folio = (Long) respuesta.getBody().getValor("folioFacturacion");
				controlLog("Asignando Serie y Folio: " + this.pojoFactura.getSerie() + "-" + folio);
				this.pojoFactura.setFolio(formatoFolio(String.valueOf(folio)));
			} catch (Exception e) {
				control(-1, "Ocurrio un problema al intentar asignar un folio de la Sucursal(Serie) seleccionada");
				return false;
			}
		} else {
			// Comprobamos Folio
			try {
				lista = this.ifzFactura.comprobarFolioFacturacion(this.pojoFactura.getSerie(), this.pojoFactura.getFolio());
				if (lista != null && ! lista.isEmpty()) {
					if (this.pojoFactura.getId() == null || this.pojoFactura.getId().longValue() <= 0L) {
						controlLog("ERROR INTERNO: El Folio indicado ya esta asignado a otra Factura");
						control(-1, "El Folio indicado ya esta asignado a otra Factura");
						return false;
					}
					
					for (Factura fac : lista) {
						if (fac.getEstatus() != FACTURA_ESTATUS.Activa.ordinal())
							continue;
						if (this.pojoFactura.getId().longValue() == fac.getId().longValue())
							continue;
						if (fac.getSerie() != null && ! this.pojoFactura.getSerie().equals(fac.getSerie()))
							continue;
						controlLog("ERROR INTERNO: El Folio indicado ya esta asignado a otra Factura");
						control(-1, "El Folio indicado ya esta asignado a otra Factura");
						return false;
					}
				}
			} catch (Exception e) {
				log.warn("No se pudo conprobar el Folio de la Factura", e);
			}
		}
		
		// Asigno empresa si corresponde
		if (this.pojoFactura.getIdEmpresa() == null)
			this.pojoFactura.setIdEmpresa(this.loginManager.getUsuarioEmpresa());

		// Asigno porcentaje IVA
		if (this.pojoFactura.getTasaIva() <= 0)
			this.pojoFactura.setTasaIva(this.porcentajeIva);
		
		try {
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			dateStr = formatter.format(this.fechaEmision);
			formatter.applyPattern("HH:mm:ss");
			dateStr = dateStr + " " + formatter.format(Calendar.getInstance(this.timeZone).getTime());
			formatter.applyPattern("dd/MM/yyyy HH:mm:ss");
			this.fechaEmision = formatter.parse(dateStr);
			this.pojoFactura.setFechaEmision(this.fechaEmision);
			controlLog("Fecha de emision asignada: " + dateStr);
		} catch (Exception e) {
			log.warn("No se pudo añadir la hora a la fecha de emision: " + dateStr, e);
		}
		
		if (! this.isDebug || (this.isDebug && ! this.paramsRequest.containsKey("NO_FOLIO"))) {
			if ("".equals(this.pojoFactura.getSerie()) && "".equals(this.pojoFactura.getFolio())) {
				controlLog("ERROR 12: Factura sin Serie y/o Folio asignados");
				control(12, "Factura sin Serie y/o Folio asignados");
				return false;
			}
		} 
		
		return true;
	}
	
	private boolean validaDatosCliente() {
		boolean dataClientValid = true;

		controlLog("Validando datos de Cliente ... ");
		if (this.pojoFactura.getIdCliente() == null || this.pojoFactura.getIdCliente() <= 0L) {
			controlLog("Validando datos de Cliente ... INVALIDO: Cliente no asignado");
			dataClientValid = false;
		} else if (this.pojoFactura.getRfc() == null || "".equals(this.pojoFactura.getRfc())) {
			controlLog("Validando datos de Cliente ... INVALIDO: RFC Cliente vacio");
			dataClientValid = false;
		} else if (! this.pojoFactura.getRfc().equals(this.pojoFactura.getIdObra().getIdCliente().getRfc())) {
			controlLog("Validando datos de Cliente ... INVALIDO: RFC Cliente distinto");
			dataClientValid = false;
		} else if (this.pojoFactura.getCliente() == null || "".equals(this.pojoFactura.getCliente())) {
			controlLog("Validando datos de Cliente ... INVALIDO: Nombre Cliente vacio");
			dataClientValid = false;
		} else if (! this.pojoFactura.getCliente().equals(this.pojoFactura.getIdObra().getIdCliente().getNombre())) {
			controlLog("Validando datos de Cliente ... INVALIDO: Nombre Cliente distinto");
			dataClientValid = false;
		} else if (this.pojoFactura.getDomicilio() == null) {
			controlLog("Validando datos de Cliente ... INVALIDO: Nombre Cliente distinto");
			dataClientValid = false;
		} else if ("".equals(this.pojoFactura.getDomicilio().trim())) {
			controlLog("Validando datos de Cliente ... INVALIDO: Nombre Cliente distinto");
			dataClientValid = false;
		}

		controlLog("Validando datos de Cliente ... CORRECTO! ");
		return dataClientValid;
	}
	
	private void asignarDatosCliente() {
		Respuesta respDomicilio = null;
		boolean rfcExtranjero = false;
		
		if (this.pojoFactura == null)
			return;
		
		try {
			control();
			controlLog("Asignando datos de Cliente");
			rfcExtranjero = ("XEXX010101000".equals(this.pojoFactura.getIdObra().getIdCliente().getRfc()));
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
			
			controlLog("Asignando domicilio de Cliente: " + this.pojoFactura.getTipoBeneficiario());
			respDomicilio = new Respuesta();
			if ("P".equals(this.pojoFactura.getTipoBeneficiario()))
				respDomicilio = this.ifzPersonas.buscarDomicilio(this.pojoFactura.getIdCliente());
			else
				respDomicilio = this.ifzNegocios.buscarDomicilio(this.pojoFactura.getIdCliente());
			if (respDomicilio.getErrores().getCodigoError() != 0) {
				control(-1, respDomicilio.getErrores().getCodigoError() + " - " + respDomicilio.getErrores().getDescError());
				return;
			}

			this.pojoFactura.setDomicilio((String) respDomicilio.getBody().getValor("domicilio"));
			if (! rfcExtranjero) {
				this.pojoFactura.setNoExterno((String) respDomicilio.getBody().getValor("numero_exterior"));
				this.pojoFactura.setNoInterno((String) respDomicilio.getBody().getValor("numero_interior"));
				this.pojoFactura.setColonia((String) respDomicilio.getBody().getValor("colonia"));
				this.pojoFactura.setCp(Integer.parseInt((String) respDomicilio.getBody().getValor("codigo_postal")));
				this.pojoFactura.setCiudad((String) respDomicilio.getBody().getValor("ciudad"));
				this.pojoFactura.setMunicipio((String) respDomicilio.getBody().getValor("municipio"));
				this.pojoFactura.setEstado((String) respDomicilio.getBody().getValor("estado"));
				this.pojoFactura.setPais((String) respDomicilio.getBody().getValor("pais"));
			}
		} catch (Exception e) {
    		control("Ocurrio un problema al asignar datos de cliente a la Factura", e);
		}
	}

	private void comprobarEmail() {
		if (this.usuarioEmail == null || "".equals(this.usuarioEmail.trim()) || ! this.usuarioEmail.contains("grupodisesa.com.mx") || "nomail@grupodisesa.com.mx".equals(this.usuarioEmail.trim())) {
			this.usuarioEmail = this.emailBase;
			this.usuarioEmailClave = this.emailBaseClave;
		}
	}

	private void seleccionaTipoCambio(Date fecha) {
		double tipoCambio = 1;
		
		try {
			control();
			if (this.loginManager.getInfoSesion().getEmpresa().getMonedaId() == this.pojoFactura.getIdMoneda().longValue()) {
				tipoCambio = 1;
				return;
			}
			
			tipoCambio =  this.ifzMonValores.tipoCambio(fecha);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el Tipo de Cambio con la fecha del Pago.\nPor favor indiquelo manualmente", e);
			tipoCambio = 1;
		} finally {
			// Asigno el tipo de cambio
			this.pojoFactura.setTipoCambio(tipoCambio);
		}
	}
	
	private boolean validaRequest(String param) {
		return validaRequest(param, null);
	}
	
	private boolean validaRequest(String param, String refValue) {
		if (! this.isDebug && ! getPermisoAdmin())
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		if (refValue == null || "".equals(refValue.trim()))
			return true;
		
		return (refValue.trim().equals(this.paramsRequest.get(param).trim()));
	}
	
	private void validaRequest(String param, boolean value) {
		if (! this.isDebug && ! getPermisoAdmin())
			return;
		
		param = param.trim().toUpperCase();
		if (! this.paramsRequest.containsKey(param) && value) 
			this.paramsRequest.put(param, "1");
		else if (this.paramsRequest.containsKey(param) && ! value) 
			this.paramsRequest.remove(param);
	}
	
	private void evaluamosEditable() {
		// Evaluamos TIMBRADA
		if (this.isDebug) controlLog("Validando timbre ... ");
		this.facturaTimbrada = false;
		if (this.pojoFactura.getUuid() != null && ! "".equals(this.pojoFactura.getUuid())) 
			this.facturaTimbrada = true;

		// Evaluamos ESTATUS
		if (this.isDebug) controlLog("Validando ESTATUS ... ");
		this.editable = true;
		if (this.pojoFactura.getEstatus() != 1)
			this.editable = false;
		this.editable = this.facturaTimbrada ? false : this.editable;
	}
	
	private void control() {
		this.procesoIncompleto = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null, null, null);
	}

	private void control(int tipoMensaje, String mensaje, String campo, String valor) {
		control(true, tipoMensaje, mensaje, null, campo, valor);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable, "", "");
	}
	
	private void control(boolean procesoIncompleto, int tipoMensaje, String mensaje, Throwable throwable, String campo, String valor) {
		mensaje = ((mensaje == null || "".equals(mensaje.trim())) ? "Ocurrio un problema al intentar procesar la accion" : mensaje);
		campo = (campo != null && ! "".equals(campo.trim()) ? campo : "CAMPO-NA");
		valor = (valor != null && ! "".equals(valor.trim()) ? valor : "VALOR-NA");
		
		this.procesoIncompleto = procesoIncompleto;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje + "\n");
	}

	// ---------------------------------------
	// DETALLES DE FACTURA 
	// ---------------------------------------

	public void nuevoConcepto() {
		try {
			control();
			this.conceptoAnterior = 0L;
			this.indexDetalleFactura = -1;
			this.pojoDetalle = new FacturaDetalleExt();
			this.pojoDetalle.setCantidad(new BigDecimal(1));
			this.pojoDetalle.setCosto(BigDecimal.ZERO);
			this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
		} catch (Exception e) {
			control("Ocurrio un problema al instanciar un nuevo concepto", e);
		} finally {
			this.numPaginaImpuestos = 1;
		}
	}
	
	public void editarConcepto() {
		try {
			control();
			if (this.pojoDetalle == null ||this.pojoDetalle.getIdConcepto() == null || this.pojoDetalle.getIdConcepto().getId() == null || this.pojoDetalle.getIdConcepto().getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Concepto de facturacion indicado");
				return;
			}
			
			this.conceptoAnterior = 0L;
			this.indexDetalleFactura = -1;
			this.indexDetalleFactura = this.listDetalles.indexOf(this.pojoDetalle); 
			this.listDetalleImpuestos = this.pojoDetalle.getListImpuestos();
			if (this.listDetalleImpuestos == null || this.listDetalleImpuestos.isEmpty())
				desglozarImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar editar el Concepto seleccionado", e);
		} finally {
			this.numPaginaImpuestos = 1;
		}
	}
	
	public void guardarConcepto() {
		try {
			control();
			// Validaciones
			if (this.pojoDetalle.getCantidad().doubleValue() <= 0) {
				controlLog("ERROR 10: La cantidad debe ser mayor a cero");
				control(10, "La cantidad debe ser mayor a cero");
				return;
			}
			
			if (this.pojoDetalle.getCosto().doubleValue() <= 0) {
				controlLog("ERROR 11: El costo debe ser mayor a cero");
				control(11, "El costo debe ser mayor a cero");
				return;
			}
			
			// Añadimos/Actualizamos en la lista
			if (this.pojoDetalle != null && (this.pojoDetalle.getId() == null || this.pojoDetalle.getId() <= 0L)) {
				if (this.listDetalles.isEmpty() || this.indexDetalleFactura == -1)
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
			nuevoConcepto();
		} catch (Exception e) {
			control("Ocurrio un problema al guardar el Concepto", e);
		}
	}
	
	public void eliminarConcepto() {
		try {
			control();
			if (this.pojoDetalle == null ||this.pojoDetalle.getIdConcepto() == null || this.pojoDetalle.getIdConcepto().getId() == null || this.pojoDetalle.getIdConcepto().getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar quitar el Concepto de facturacion indicado");
				return;
			}
			
			// Borramos impuestos
			if (this.listDetalleImpuestosEliminados == null)
				this.listDetalleImpuestosEliminados = new ArrayList<FacturaDetalleImpuestoExt>();
			this.listDetalleImpuestosEliminados.addAll(this.pojoDetalle.getListImpuestos());
			
			// Borramos concepto
			if (this.listDetallesEliminados == null)
				this.listDetallesEliminados = new ArrayList<FacturaDetalleExt>();
			this.listDetallesEliminados.add(this.pojoDetalle);
			this.listDetalles.remove(this.pojoDetalle);

			// Recalculamos totales
			totalizar();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el concepto seleccionado", e);
		}
	}

	public void eliminarConceptoImpuesto() {
		if (this.pojoDetalleImpuesto != null) {
			this.listDetalleImpuestos.remove(this.pojoDetalleImpuesto);
			this.listDetalleImpuestosEliminados.add(this.pojoDetalleImpuesto);
			desglozarImpuestos();
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
		Double porcentaje = 0D;
		Double porImpuesto = 0D;
		Double porRetension = 0D;
		boolean prevBorrado = false;
		
		try {
			control();
			if (this.isDebug) controlLog("Preparando desgloce de impuestos ... ");
			if (this.pojoDetalle.getIdConcepto().getId() == null || this.pojoDetalle.getIdConcepto().getId() <= 0L) {
				controlLog("No selecciono ningun concepto");
				control(-1, "No selecciono ningun concepto");
				return;
			}
			
			if (this.conceptoAnterior == this.pojoDetalle.getIdConcepto().getId().longValue() && ! this.listDetalleImpuestos.isEmpty()) {
				controlLog("Impuestos desglosados previamente");
				return;
			}
			
			// Recupero impuestos del concepto, si corresponde
			if (this.isDebug) controlLog("Recuperando impuestos ... ");
			this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
			if (this.pojoDetalle.getId() != null && this.pojoDetalle.getId() > 0L) {
				listAux = this.pojoDetalle.getListImpuestos(); 
				for (FacturaDetalleImpuestoExt imp : listAux) {
					prevBorrado = false;
					if (this.listDetalleImpuestosEliminados != null && ! this.listDetalleImpuestosEliminados.isEmpty()) {
						for (FacturaDetalleImpuestoExt del : this.listDetalleImpuestosEliminados) {
							if (imp.getId().longValue() == del.getId().longValue()) {
								prevBorrado = true;
								break;
							}
						}
					}
					
					if (! prevBorrado)
						this.listDetalleImpuestos.add(imp);
				}
			}
			
			if (this.listDetalleImpuestos != null && ! this.listDetalleImpuestos.isEmpty()) {
				for (FacturaDetalleImpuestoExt var : this.listDetalleImpuestos) {
					porcentaje = Double.valueOf(var.getIdImpuesto().getAtributo1());
					impuesto = ((this.pojoDetalle.getImporte().doubleValue() * porcentaje) / 100);
					var.setBase(this.pojoDetalle.getImporte());
					var.setTipoFactor("Tasa");
					var.setTasa(porcentaje);
					var.setImporte(new BigDecimal(impuesto));

					if ("DE".equals(var.getIdImpuesto().getTipoCuenta())) {
						porImpuesto = porcentaje;
						totalImpuestos += impuesto;
					} else {
						porRetension = porcentaje;
						totalRetenciones += impuesto;
					}
				}
			} else {
				// Recuperamos Impuesto de concepto
				listImpuestos = this.ifzConFacImpuestos.findAllExt(this.pojoDetalle.getIdConcepto().getId()); 
				if (listImpuestos == null || listImpuestos.isEmpty()) {
					if (this.isDebug) controlLog("El concepto no tiene asignado ningun impuesto");
					control(-1, "El concepto no tiene asignado ningun impuesto");
					return;
				}

				// Realizando desgloce
				if (this.isDebug) controlLog("Desglozando impuestos ... ");
				
				pojoImpuesto = null;
				this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
				for (ConceptoFacturacionImpuestosExt var : listImpuestos) {
					if (var.getIdImpuesto() != null && var.getIdImpuesto().getAtributo1() != null && ! "".equals(var.getIdImpuesto().getAtributo1())) { 
						porcentaje = Double.valueOf(var.getIdImpuesto().getAtributo1());
						impuesto = ((this.pojoDetalle.getImporte().doubleValue() * porcentaje) / 100);
						var.setMonto(new BigDecimal(impuesto));
						if ("DE".equals(var.getIdImpuesto().getTipoCuenta())) {
							porImpuesto = porcentaje;
							totalImpuestos += impuesto;
						} else {
							porRetension = porcentaje;
							totalRetenciones += impuesto;
						}
						
						pojoImpuesto = new FacturaDetalleImpuestoExt();
						pojoImpuesto.setIdFacturaDetalle(this.ifzFacturaDetalle.convertir(this.pojoDetalle));
						pojoImpuesto.setIdImpuesto(var.getIdImpuesto());
						pojoImpuesto.setBase(this.pojoDetalle.getImporte());
						pojoImpuesto.setTipoFactor("Tasa");
						pojoImpuesto.setTasa(porcentaje);
						pojoImpuesto.setImporte(new BigDecimal(impuesto));
						pojoImpuesto.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						pojoImpuesto.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
						pojoImpuesto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						pojoImpuesto.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
						this.listDetalleImpuestos.add(pojoImpuesto);
					}
				}
			}

			total = totalImpuestos - totalRetenciones;
			total = this.pojoDetalle.getImporte().doubleValue() + total;
			if (total < 0)
				total = 0;
			this.pojoDetalle.setPorcentajeIva(porImpuesto);
			this.pojoDetalle.setPorcentajeRetencion(porRetension);
			this.pojoDetalle.setImpuestos(new BigDecimal(totalImpuestos));
			this.pojoDetalle.setRetenciones(new BigDecimal(totalRetenciones));
			this.pojoDetalle.setTotal(new BigDecimal(total));
			this.pojoDetalle.setListImpuestos(this.listDetalleImpuestos);
			if (this.isDebug) controlLog("Desgloce de impuestos terminado");
		} catch (Exception e) {
			control("Ocurrio un problema al calcular los impuestos del Concepto", e);
		}
	}

	public void actualizarImpuestos() {
		double total = 0D;
		Double impuesto = 0D;
		Double totalImpuestos = 0D;
		Double totalRetenciones = 0D;
		Double porcentaje = 0D;
		Double porImpuesto = 0D;
		Double porRetension = 0D;
		
		try {
			control();
			if (this.isDebug) controlLog("Preparando desgloce de impuestos ... ");
			if (this.pojoDetalle.getIdConcepto().getId() == null || this.pojoDetalle.getIdConcepto().getId() <= 0L) {
				controlLog("No selecciono ningun concepto");
				control(-1, "No selecciono ningun concepto");
				return;
			}
			

			if (this.listDetalleImpuestos == null || this.listDetalleImpuestos.isEmpty()) {
				control(-1, "Sin impuestos para actualizar");
				return;
			}
			
			for (FacturaDetalleImpuestoExt imp : this.listDetalleImpuestos) {
				porcentaje = Double.valueOf(imp.getIdImpuesto().getAtributo1());
				impuesto = imp.getImporte().doubleValue();
				if ("DE".equals(imp.getIdImpuesto().getTipoCuenta())) {
					porImpuesto = porcentaje;
					totalImpuestos += impuesto;
				} else {
					porRetension = porcentaje;
					totalRetenciones += impuesto;
				}
			}

			total = totalImpuestos - totalRetenciones;
			total = this.pojoDetalle.getImporte().doubleValue() + total;
			if (total < 0)
				total = 0;
			this.pojoDetalle.setPorcentajeIva(porImpuesto);
			this.pojoDetalle.setPorcentajeRetencion(porRetension);
			this.pojoDetalle.setImpuestos(new BigDecimal(totalImpuestos));
			this.pojoDetalle.setRetenciones(new BigDecimal(totalRetenciones));
			this.pojoDetalle.setTotal(new BigDecimal(total));
			this.pojoDetalle.setListImpuestos(this.listDetalleImpuestos);
			if (this.isDebug) controlLog("Desgloce de impuestos terminado");
		} catch (Exception e) {
			control("Ocurrio un problema al calcular los impuestos del Concepto", e);
		}
	}
	
	// --------------------------------------------------------------------------------
	// FOLIO
	// --------------------------------------------------------------------------------

	public void evaluaFacturaFolio() {
		try {
			controlLog("Evaluando factura folio.");
			control();
			this.facturaSerie = this.pojoFactura.getSerie();
			this.facturaFolio = this.pojoFactura.getFolio();
			if (this.sucursalId > 0L) {
				for (Sucursal var : this.listBusquedaSucursal) {
					if (this.sucursalId == var.getId() && (var.getSerie() != null && ! "".equals(var.getSerie().trim()))) {
						if (! this.facturaSerie.equals(var.getSerie())) {
							this.facturaFolio = "";
							break;
						}
						
						this.facturaSerie = var.getSerie();
					}
				}
			} 
		} catch (Exception e) {
			control("No se pudo cargar la Serie y Folio de la Factura indicada.", e);
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
			this.pojoFactura.setFolioFactura("");
			if (! "".equals(this.facturaSerie.trim()) && ! "0".equals(this.facturaFolio.trim())) {
				this.pojoFactura.setFolioFactura(this.facturaSerie + '-' + this.facturaFolio);
				this.pojoFactura.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoFactura.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
			} 
			
			controlLog("Serie y folio modificados correctamente: " + this.pojoFactura.getFolioFactura());
		} catch (Exception e) {
			control("No se pudo guardar la Serie y Folio ingresados", e);
		}
	}

	// --------------------------------------------------------------------------------
	// PAGOS
	// --------------------------------------------------------------------------------
	
	/*public void obtenerPagos() {
		try {
			control();
			this.paginacionPagos = 1;
			this.listFacturaPagos = this.ifzPagos.findExtByFactura(this.pojoFactura.getId());
			if (this.listFacturaPagos != null && ! this.listFacturaPagos.isEmpty()) {
				Collections.sort(this.listFacturaPagos, new Comparator<FacturaPagosExt>() {
					@Override
					public int compare(FacturaPagosExt o1, FacturaPagosExt o2) {
						return o2.getFecha().compareTo(o1.getFecha());
					}
				});
			}
			
			obtenerNotasCredito();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar los Pagos de la Factura", e);
		}
	}*/

	public void obtenerPagoPDF() {
		FacturaPagosExt pago = null;
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			if (this.idPago <= 0L) {
				controlLog("Descarga abortada");
				return;
			}
			
			pago = this.ifzPagos.findByIdExt(this.idPago);
			if (pago == null || pago.getId() == null || pago.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Pago indicado");
				return;
			}
			
			if (pago.getIdTimbre() != null && (pago.getIdTimbre().getXmlTimbrado() == null || pago.getIdTimbre().getXmlTimbrado().length == 0)) {
				control(-1, "Ocurrio un problema al recuperar el Timbre del Pago indicado");
				return;
			}
			
			nombreDocumento = "P-" + pago.getSerie() + "-" + pago.getFolio();
			nombreDocumento = nombreDocumento + ("".equals(pago.getAgrupador().trim()) ? "_" + pago.getId() : "_" + pago.getAgrupador().replace("/", "-"));
			
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idFacturaPago", pago.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.facturaPago"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				controlLog("ERROR INTERNO: No se pudo procesar el reporte de Complemento de Pago (PDF).\n" + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al procesar el PDF del Complemento de Pago.\nContacte a su Administrador");
				return;
			}
			
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				controlLog("ERROR INTERNO: No se pudo recuperar el reporte de Complemento de Pago (PDF).\n" + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al recuperar el PDF del Complemento de Pago.\nContacte a su Administrador");
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
		} catch (Exception e) {
    		control("Ocurrio un problema al procesar el Complemento de Pago", e);
		} 
	}
	
	public void obtenerPagoXML() {
		String nombreDocumento = "";
		byte[] contenidoDocumento = null;
		FacturaPagosExt fPago = null;
		
		try {
			control();
			controlLog("Descargando XML ... ");
			if (this.idPago <= 0L) {
				controlLog("Descarga abortada");
				return;
			}
			
			fPago = this.ifzPagos.findByIdExt(this.idPago);
			if (fPago == null || fPago.getId() == null || fPago.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Pago indicado");
				return;
			}
			
			if (fPago.getIdTimbre() == null || fPago.getIdTimbre().getId() == null || fPago.getIdTimbre().getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Timbre del Pago indicado");
				return;
			}

			nombreDocumento = "P-" + fPago.getSerie() + "-" + fPago.getFolio() + ".xml";
			contenidoDocumento = fPago.getIdTimbre().getXmlTimbrado();

			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", "xml");
			controlLog("Descarga lanzada");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el XML de la factura", e);
		}
	}
	
	// --------------------------------------------------------------------------------
	// NOTAS DE CREDITO
	// --------------------------------------------------------------------------------
	
	/*public void obtenerNotasCredito() {
		try {
			control();
			this.paginacionNotasCredito = 1;
			this.listNotasCredito = this.ifzFactura.findNotasCreditoByFactura(this.pojoFactura.getId());
			if (this.listNotasCredito != null && ! this.listNotasCredito.isEmpty()) {
				Collections.sort(this.listNotasCredito, new Comparator<Factura>() {
					@Override
					public int compare(Factura o1, Factura o2) {
						return o2.getFechaEmision().compareTo(o1.getFechaEmision());
					}
				});
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar los Pagos de la Factura", e);
		}
	}*/

	public void obtenerNotaCreditoPDF() {
		FacturaExt notaCredito = null;
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			if (this.idNotaCredito <= 0L) {
				controlLog("Descarga abortada");
				return;
			}
			
			notaCredito = this.ifzFactura.findExtById(this.idNotaCredito);
			if (notaCredito == null || notaCredito.getId() == null || notaCredito.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Nota de Credito indicada");
				return;
			}
			
			if (notaCredito.getIdTimbre() != null && (notaCredito.getIdTimbre().getCfdi() == null || notaCredito.getIdTimbre().getCfdi().length == 0)) {
				control(-1, "Ocurrio un problema al recuperar el Timbre de la Nota de Credito indicada");
				return;
			}
			
			nombreDocumento = "NC-" + notaCredito.getFolioFactura();
			nombreDocumento = nombreDocumento + notaCredito.getId();
			
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idFactura", notaCredito.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.factura"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				controlLog("ERROR INTERNO: No se pudo procesar el reporte de la Nota de Credito (PDF).\n" + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al procesar el PDF de la Nota de Credito.\nContacte a su Administrador");
				return;
			}
			
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				controlLog("ERROR INTERNO: No se pudo recuperar el reporte de la Nota de Credito (PDF).\n" + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al recuperar el PDF de la Nota de Credito.\nContacte a su Administrador");
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
		} catch (Exception e) {
    		control("Ocurrio un problema al procesar la Nota de Credito", e);
		} 
	}
	
	public void obtenerNotaCreditoXML() {
		FacturaExt notaCredito = null;
		String nombreDocumento = "";
		byte[] contenidoDocumento = null;
		
		try {
			control();
			controlLog("Descargando XML ... ");
			if (this.idNotaCredito <= 0L) {
				controlLog("Descarga abortada");
				return;
			}
			
			notaCredito = this.ifzFactura.findExtById(this.idNotaCredito);
			if (notaCredito == null || notaCredito.getId() == null || notaCredito.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Nota de Credito indicada");
				return;
			}
			
			if (notaCredito.getIdTimbre() != null && (notaCredito.getIdTimbre().getCfdi() == null || notaCredito.getIdTimbre().getCfdi().length == 0)) {
				control(-1, "Ocurrio un problema al recuperar el Timbre de la Nota de Credito indicada");
				return;
			}

			nombreDocumento = "NC-" + notaCredito.getFolioFactura() + ".xml";
			contenidoDocumento = notaCredito.getIdTimbre().getCfdi();
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				controlLog("ERROR INTERNO: No se pudo recuperar el reporte de la Nota de Credito (XML).");
				control(-1, "Ocurrio un problema al recuperar el XML de la Nota de Credito.\nContacte a su Administrador");
				return;
			}

			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", "xml");
			controlLog("Descarga lanzada");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el XML de la factura", e);
		}
	}
	
	// ---------------------------------------
	// TIMBRE
	// ---------------------------------------
	
	public void validarTimbrar() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String fechaEmisionFactura = "";
		double tipoCambio = 0;
		boolean guardar = false;
		
		try {
			control();
			controlLog("Validando Factura ... ");
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId().longValue() <= 0L) {
				this.pojoFactura = this.ifzFactura.findExtById(this.idFactura);
				if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
					control(-1, "Ocurrio un problema al recuperar la Factura");
					return;
				}
			}
			
			if (this.tipoComprobante == null || "".equals(this.tipoComprobante.trim())) {
				controlLog("ERROR INTERNO - Factura sin tipo de Comprobante asignado");
				control(-1, "Indique el Tipo de Comprobante");
				return;
			}
			
			if (this.pojoFactura.getSerie() == null || "".equals(this.pojoFactura.getSerie())) {
				controlLog("ERROR INTERNO - Factura sin Serie");
				control(-1, "La factura indicada no tiene Serie asignada");
				return;
			}
			
			if (this.pojoFactura.getFolio() == null || "".equals(this.pojoFactura.getFolio())) {
				controlLog("ERROR INTERNO - Factura sin Folio");
				control(-1, "La factura indicada no tiene Folio asignado");
				return;
			}
			
			this.cfdiRelacionado = false;
			this.cfdiRelacionadoUuid = "";
			this.listFacturasRelacionadas = new ArrayList<FacturasRelacionadas>();
			this.tipoComprobante = this.tipoComprobanteSugerido;
			this.idTipoRelacion = 0;
			this.idUsoCfdi = this.idUsoCfdiSugerido;
			this.cfdiVersion = this.cfdiVersionSugerida;
			this.tipoComprobante = this.pojoFactura.getTipoComprobante();
			
			this.cfdiVersion = this.pojoFactura.getVersion();
			if (this.cfdiVersion == null || "".equals(this.cfdiVersion.trim()))
				this.cfdiVersion = this.cfdiVersionSugerida;
			this.pojoFactura.setVersion(this.cfdiVersion);
			
			// Comprobamos si es de egreso y ya ha sido relacionada
			if ("E".equals(this.pojoFactura.getTipoComprobante())) {
				this.cfdiRelacionado = true;
				/*if (this.pojoFactura.getIdFacturaRelacionada() != null) {
					if (this.pojoFactura.getIdFacturaRelacionada().getUuid() == null || "".equals(this.pojoFactura.getIdFacturaRelacionada().getUuid().trim())) {
						controlLog("ERROR INTERNO - Factura Relacionada no esta timbrada");
						control(-1, "La Factura Relacionada no esta timbrada.\nDebe timbrar la Factura relacionada antes de continuar.\nFactura Relacionada: " + this.pojoFactura.getIdFacturaRelacionada().getFolioFactura());
						return;
					}
					
					this.cfdiRelacionadoUuid = this.pojoFactura.getIdFacturaRelacionada().getUuid();
					this.tipoComprobante = this.pojoFactura.getTipoComprobante();
				}*/
				
				//Facturas Relacionadas
				if(this.listFacturasRelacionadasExt != null && this.listFacturasRelacionadasExt.size() > 0){
					for (FacturasRelacionadasExt var : listFacturasRelacionadasExt) {
						if (var.getIdFacturaRelacionada().getUuid() == null || "".equals(var.getIdFacturaRelacionada().getUuid().trim())) {
							controlLog("ERROR INTERNO - Factura Relacionada no esta timbrada");
							control(-1, "La Factura Relacionada no esta timbrada.\nDebe timbrar la Factura relacionada antes de continuar.\nFactura Relacionada: " + this.pojoFactura.getIdFacturaRelacionada().getFolioFactura());
							return;
						}
					}
					this.listFacturasRelacionadas = ifzFacturasRelacionadas.findFacturasById(this.pojoFactura.getId());
					this.tipoComprobante = this.pojoFactura.getTipoComprobante();
				}
			}

			// Fecha Emision
			controlLog("Compruebo fecha Emision ... ");
			if ("00:00:00".equals(formatter.format(this.pojoFactura.getFechaEmision()))) {
				formatter.applyPattern("MM-dd-yyyy");
				fechaEmisionFactura = formatter.format(this.pojoFactura.getFechaEmision());
				formatter.applyPattern("HH:mm:ss");
				fechaEmisionFactura = fechaEmisionFactura + " " + formatter.format(Calendar.getInstance(this.timeZone).getTime());
				formatter.applyPattern("MM-dd-yyyy HH:mm:ss");
				this.pojoFactura.setFechaEmision(formatter.parse(fechaEmisionFactura));
				controlLog("Asigno fecha emision: " + fechaEmisionFactura + ". Pendiente actualizar ... ");
				guardar = true;
			}

			// Moneda
			controlLog("Compruebo Moneda ... ");
			if (this.pojoFactura.getIdMoneda() == null || this.pojoFactura.getIdMoneda() <= 0L) {
				if (this.monedaBase != null) {
					control(-1, "Ocurrio un problema al intentar asignar la Moneda");
					return;
				}
				
				this.pojoFactura.setIdMoneda(this.monedaBase.getId());
				this.pojoFactura.setDescripcionMoneda(this.monedaBase.getNombre());
				this.pojoFactura.setAbreviaturaMoneda(this.monedaBase.getAbreviacion());
				this.pojoFactura.setTipoCambio(1.0);
				controlLog("Asigno datos de Moneda. Pendiente actualizar ... ");
				guardar = true;
			} else {
				for (Moneda var : this.listMonedas) {
					if (this.pojoFactura.getIdMoneda().longValue() == var.getId()) {
						this.pojoFactura.setDescripcionMoneda(var.getNombre());
						this.pojoFactura.setAbreviaturaMoneda(var.getAbreviacion());
						if (this.pojoFactura.getTipoCambio() <= 0)
							this.pojoFactura.setTipoCambio(1.0);
						controlLog("Asigno datos de Moneda. Pendiente actualizar ... ");
						guardar = true;
						break;
					}
				}
			}

			// Tipo Cambio
			controlLog("Comprobando Tipo de Cambio ... ");
			if (this.pojoFactura.getTipoCambio() <= 1) {
				tipoCambio = 1;
				if (this.pojoFactura.getIdMoneda().longValue() != this.monedaBase.getId()) {
					tipoCambio = this.ifzMonValores.tipoCambio(this.pojoFactura.getFechaEmision());
					if (tipoCambio <= 0) {
						control(-1, "Ocurrio un problema al intentar Recuperar el Tipo de Cambio");
						return;
					}
				}

				// Asigno tipo de cambio
				this.pojoFactura.setTipoCambio(tipoCambio);
				controlLog("Asigno Tipo de Cambio: " + tipoCambio + ". Pendiente actualizar ... ");
				guardar = true;
			}

			// Cliente
			controlLog("Compruebo datos de Cliente ... ");
			if (! validaDatosCliente()) {
				asignarDatosCliente();
				controlLog("Asigno datos de Cliente. Pendiente actualizar ... ");
				guardar = true;
			}
			
			// Actualizo los datos de la Factura si corresponde
			if (guardar) {
				controlLog("Actualizando factura ... ");
				this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzFactura.update(this.pojoFactura);
				controlLog("Factura actualizada!");
			}
			
			controlLog("Validacion terminada!");
		} catch (Exception e) {
			control("Ocurrio un problema al validar Factura para timbrado", e);
		} finally {
			validaRequest("CFDI_TEST", false);
			validaRequest("NOTIMBRAR", false);
		}
	}
	
	public void timbrar() {
		Respuesta respuesta = null;
		// ----------------------------
		String usoCFDI = "";
		String tipoRelacion = "";
		// ----------------------------
		boolean cfdiTesting = false;
		boolean cfdiNoTimbrar = false;
		
		try {
			control();
			if (this.procesando)
				return;
			this.procesando = true;
			cfdiTesting = validaRequest("CFDI_TEST");
			cfdiNoTimbrar = validaRequest("NOTIMBRAR");
			
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Factura indicada.\nProceso de timbre abortado");
				return;
			}
			
			usoCFDI = "";
			if (this.idUsoCfdi > 0L) {
				for (ConValores var : this.listCFDIUso) {
					if (this.idUsoCfdi == var.getId()) {
						usoCFDI = var.getValor();
						break;
					}
				}
			}
			
			tipoRelacion = "";
			if (this.idTipoRelacion > 0L) {
				for (ConValores var : this.listCFDITipoRelacion) {
					if (this.idTipoRelacion == var.getId()) {
						tipoRelacion = var.getValor();
						break;
					}
				}
			}

			controlLog("Timbrando factura ... ");
			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			//respuesta = this.ifzFactura.timbrar(this.pojoFactura, this.cfdiVersion, usoCFDI, ((this.cfdiRelacionado) ? 1 : 0), ((this.cfdiRelacionado) ? this.cfdiRelacionadoUuid : ""), ((this.cfdiRelacionado) ? tipoRelacion : ""), this.isDebug, cfdiTesting, cfdiNoTimbrar);
			respuesta = this.ifzFactura.timbrar(this.pojoFactura, this.cfdiVersion, usoCFDI, ((this.cfdiRelacionado) ? 1 : 0), ((this.cfdiRelacionado) ? this.listFacturasRelacionadas : null), ((this.cfdiRelacionado) ? tipoRelacion : ""), this.isDebug, cfdiTesting, cfdiNoTimbrar);
			if (respuesta.getErrores().getCodigoError() != 0) {
				control(-1, "Ocurrio un problema al timbrar la Factura, proceso de timbre abortado.\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}
			
			// Aplico pagos de factura previa si aplica (Refacturacion)
			this.ifzPagos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPagos.aplicarPagos(this.pojoFactura.getId());

			// Actualizo el listado
			this.pojoFactura = (FacturaExt) respuesta.getBody().getValor("factura");
			for (Factura var : this.listFacturas) {
				if (var.getId().longValue() != this.pojoFactura.getId().longValue()) 
					continue;
				this.listFacturas.set(this.listFacturas.indexOf(var), this.ifzFactura.findById(this.pojoFactura.getId()));
				controlLog("Factura actualizada en listado");
				break;
			}
			
			control(false, 15, "Factura timbrada", null, this.pojoFactura.getFolioFactura(), this.pojoFactura.getUuid());
		} catch (Exception e) {
			control("Ocurrio un problema al intentar timbrar la factura", e);
		} finally {
			this.procesando = false;
			controlLog("Factura ... proceso timbrado terminado");
			evaluamosEditable();
		}
	}
	
	public void consultarEstatus() {
		Respuesta respuesta = null;

		try {
			control();
			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzFactura.consultarEstatus(this.pojoFactura.getId());
			if (respuesta.getErrores().getCodigoError() != 0) {
				control(-1, "Ocurrio un problema al timbrar la Factura, proceso de timbre abortado.\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar el estatus de la Factura", e);
		} 
	}
	
	// ---------------------------------------
	// BUSQUEDA OBRAS
	// ---------------------------------------
	
 	public void nuevaBusquedaObras() {
		control();
		this.campoBusquedaObras = this.opcionesBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.tipoBusquedaProyecto = 0;
		
		this.paginacionObras = 1;
		this.listaObras = new ArrayList<Obra>();
		this.pojoObra = new Obra();
	}

	public void buscarObras() {
		try {
			control();
			this.campoBusquedaObras = (this.campoBusquedaObras != null && ! "".equals(this.campoBusquedaObras.trim()) ? this.campoBusquedaObras.trim() : this.opcionesBusquedaObras.get(0).getValue().toString());
			this.valorBusquedaObras = (this.valorBusquedaObras != null && ! "".equals(this.valorBusquedaObras.trim()) ? this.valorBusquedaObras.trim() : "");
			this.tipoBusquedaProyecto = (this.buscarAdministrativo ? 4 : 0);

			this.ifzObra.setInfoSesion(this.loginManager.getInfoSesion());
			this.listaObras = this.ifzObra.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.tipoBusquedaProyecto, false, "", 0);
			if (this.listaObras == null || this.listaObras.isEmpty()) 
				control(2, "Busqueda sin resultados", this.campoBusquedaObras, this.valorBusquedaObras);
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
		} finally {
			this.numPaginaConceptos = 1;
			this.listaObras = (this.listaObras != null ? this.listaObras : new ArrayList<Obra>());
			controlLog(this.listaObras.size() + " Obras encontradas");
		}
	}
	
	public void seleccionarObra() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				controlLog("No se pudo recuperar la Obra seleccionada. POJO nulo");
				control(-1, "No se pudo recuperar la Obra seleccionada");
				return;
			}

			controlLog("Asignando Obra: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
			this.pojoFactura.setIdObra(this.ifzObra.convertir(this.pojoObra));
			if (this.pojoObra.getIdMoneda() != null && this.pojoObra.getIdMoneda() > 0L)
				this.pojoFactura.setIdMoneda(this.pojoObra.getIdMoneda());
			else
				this.pojoFactura.setIdMoneda(this.idMonedaSugerida);

			controlLog("Asignando tipo Cambio ... ");
			this.fechaEmision = Calendar.getInstance().getTime();
			seleccionaTipoCambio();

			controlLog("Asignando Cliente: " + this.pojoFactura.getIdObra().getIdCliente().getId() + " - " + this.pojoObra.getNombreCliente());
			asignarDatosCliente();

			controlLog("Asignando Sucursal: " + this.pojoObra.getIdSucursal());
			this.sucursalId = this.pojoObra.getIdSucursal();
			evaluaFacturaFolio();
			
			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		} 
	}

	// ---------------------------------------
	// BUSQUEDA CONCEPTOS DE FACTURACION
	// ---------------------------------------	
	
	public void busquedaConceptos() {
		control();
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
		this.valorBusquedaConceptos = "";
		this.conceptosDuplidados = false;
		
		this.numPaginaConceptos = 1;
		this.listConceptoFacturacion = new ArrayList<ConceptoFacturacion>();
		this.pojoConcepto = null;
	}
	
	public void buscarConceptos() {
		try {
			control();
			this.campoBusquedaConceptos = (this.campoBusquedaConceptos != null && ! "".equals(this.campoBusquedaConceptos.trim()) ? this.campoBusquedaConceptos.trim() : this.tiposBusquedaConceptos.get(0).getValue().toString());
			this.valorBusquedaConceptos = (this.valorBusquedaConceptos != null && ! "".equals(this.valorBusquedaConceptos.trim()) ? this.valorBusquedaConceptos.trim() : "");
			
			this.ifzConceptosFacturacion.setInfoSesion(this.loginManager.getInfoSesion());
			this.listConceptoFacturacion = this.ifzConceptosFacturacion.findLikeProperty(this.campoBusquedaConceptos, this.valorBusquedaConceptos, 0);
			if (this.listConceptoFacturacion == null || this.listConceptoFacturacion.isEmpty()) 
				control(2, "Busqueda sin resultados", this.campoBusquedaObras, this.valorBusquedaObras);
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Conceptos de Facturacion", e);
		} finally {
			this.numPaginaConceptos = 1;
			this.listConceptoFacturacion = (this.listConceptoFacturacion != null ? this.listConceptoFacturacion : new ArrayList<ConceptoFacturacion>());
			controlLog(this.listConceptoFacturacion.size() + " Conceptos encontrados");
		}
	}
	
	public void seleccionarConcepto() {
		ConValores pojoUnidadMedida = null;
		
		try {
			control();
			if (this.pojoConcepto == null || this.pojoConcepto.getId() == null || this.pojoConcepto.getId() <= 0L) {
				controlLog("No selecciono ningun Concepto. POJO nulo");
				control(-1, "Debe seleccionar un Concepto");
				return;
			}
			
			// Comprobamos que el concepto no halla sido seleccionado antes
			if (! this.conceptosDuplidados) {
				for (FacturaDetalleExt detalle : this.listDetalles) {
					if (this.pojoConcepto.getId().longValue() == detalle.getIdConcepto().getId().longValue()) {
						control(-1, "El Concepto seleccionado ya existe en la Factura");
						return;
					}
				}
			}
			
			// Recuperamos el pojo de Unidad de Medida
			controlLog("Obteniendo Unidad Medida ... ");
			pojoUnidadMedida = this.ifzConValores.findById(this.pojoConcepto.getIdUnidadMedida());
			
			// Añadimos el concepto y desglozamos sus impuestos, si corresponde
			this.pojoDetalle.setIdConcepto(this.pojoConcepto);
			if (pojoUnidadMedida != null) 
				this.pojoDetalle.setIdUnidadMedida(pojoUnidadMedida);
			
			// Desglozamos impuestos del Concepto si corresponde
			this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
			if (this.pojoDetalle.getCosto().doubleValue() > 0)
				desglozarImpuestos();
			busquedaConceptos();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Concepto seleccionado", e);
		}
	}

	// --------------------------------------------------------------------------------
	// BUSQUEDA CFDI
	// --------------------------------------------------------------------------------
	
	public void nuevaBusquedaCFDI() {
		control();
		this.campoBusquedaCfdi = this.tipoBusquedaCfdi.get(0).getValue().toString();
		this.valorBusquedaCfdi = "";
		
		this.numPaginaCfdi = 1;
		this.listBusquedaCfdi = new ArrayList<Factura>();
		this.pojoCfdiSeleccionado = null;
	}
	
	public void buscarCFDI() {
		try {
			control();
			this.campoBusquedaCfdi = (this.campoBusquedaCfdi != null && ! "".equals(this.campoBusquedaCfdi.trim()) ? this.campoBusquedaCfdi.trim() : this.tipoBusquedaCfdi.get(0).getValue().toString());
			this.valorBusquedaCfdi = (this.valorBusquedaCfdi != null && ! "".equals(this.valorBusquedaCfdi.trim()) ? this.valorBusquedaCfdi.trim() : "");
			
			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			this.listBusquedaCfdi = this.ifzFactura.findLikeProperty(this.campoBusquedaCfdi, this.valorBusquedaCfdi, 0L, "", 0, true, false, "extract(year from model.fechaEmision) desc, model.folioFactura desc", 0);
			if (this.listBusquedaCfdi == null || this.listBusquedaCfdi.isEmpty()) 
				control(2, "Busqueda sin resultados", this.campoBusquedaCfdi, this.valorBusquedaCfdi);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar realizar la busqueda de Facturas", e);
		} finally {
			this.numPaginaCfdi = 1;
			this.listBusquedaCfdi = (this.listBusquedaCfdi != null ? this.listBusquedaCfdi : new ArrayList<Factura>());
			controlLog(this.listBusquedaCfdi.size() + " CFDI encontrados");
		}
	}
	
	public void seleccionarCFDI() {
		try {
			control();
			controlLog("Seleccionando factura ... ");
			if (this.pojoCfdiSeleccionado == null) {
				controlLog("Ocurrio un problema al intentar recuperar la Factura seleccionada");
				control(-1, "Ocurrio un problema al intentar recuperar la Factura seleccionada");
				return;
			}
			
			this.cfdiRelacionadoUuid = this.pojoCfdiSeleccionado.getUuid();
			nuevaBusquedaCFDI();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar obtener datos del CFDI seleccionado", e);
		} 
	}

	// --------------------------------------------------------------------------------
	// BUSQUEDA FACTURAS
	// --------------------------------------------------------------------------------
	
	public void nuevaBusquedaFacturas() {
		control();
		this.campoBusquedaFacturas = this.tipoBusquedaFacturas.get(0).getValue().toString();
		this.valorBusquedaFacturas = "";
		
		this.numPaginaFacturas = 1;
		this.listBusquedaFacturas = new ArrayList<Factura>();
		this.pojoFacturaSeleccionada = null;
	}
	public void limpiarFacturasRelacionadas() {
		control();
		
		this.listFacturasRelacionadasExt = new ArrayList<FacturasRelacionadasExt>();
		this.pojoFacturaSeleccionada = null;
		this.pojoFacturasRelacionadas = null;
		this.pojoFacturasRelacionadasExt = null;
	}
	
	public void buscarFacturas() {
		try {
			control();
			this.campoBusquedaFacturas = (this.campoBusquedaFacturas != null && ! "".equals(this.campoBusquedaFacturas.trim()) ? this.campoBusquedaFacturas.trim() : this.tipoBusquedaFacturas.get(0).getValue().toString());
			this.valorBusquedaFacturas = (this.valorBusquedaFacturas != null && ! "".equals(this.valorBusquedaFacturas.trim()) ? this.valorBusquedaFacturas.trim() : "");
			
			this.ifzFactura.setInfoSesion(this.loginManager.getInfoSesion());
			this.listBusquedaFacturas = this.ifzFactura.findLikeProperty(this.campoBusquedaFacturas, this.valorBusquedaFacturas, 0L, "I", 0, false, true, "extract(year from model.fechaEmision) desc, model.folioFactura desc", 0);//findLikeProperty(this.campoBusquedaFacturas, this.valorBusquedaFacturas, "I", 0, true, 0);
			if (this.listBusquedaFacturas == null || this.listBusquedaFacturas.isEmpty()) 
				control(2, "Busqueda sin resultados", this.campoBusquedaFacturas, this.valorBusquedaFacturas);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar realizar la busqueda de Facturas", e);
		} finally {
			this.numPaginaFacturas = 1;
			this.listBusquedaFacturas = (this.listBusquedaFacturas != null ? this.listBusquedaFacturas : new ArrayList<Factura>());
			controlLog(this.listBusquedaFacturas.size() + " Facturas encontradas");
		}
	}
	
	public void seleccionarFactura() {
		try {
			control();
			controlLog("Seleccionando factura ... ");
			if (this.pojoFacturaSeleccionada == null) {
				controlLog("Ocurrio un problema al intentar recuperar la Factura seleccionada");
				control(-1, "Ocurrio un problema al intentar recuperar la Factura seleccionada");
				return;
			}
			
			// Asignamos la factura relacionada
			if(this.listFacturasRelacionadasExt == null){
				this.listFacturasRelacionadasExt = new ArrayList<FacturasRelacionadasExt>();
			}
			this.pojoFacturasRelacionadasExt = new FacturasRelacionadasExt();
			this.pojoFacturasRelacionadasExt.setIdFacturaRelacionada(this.pojoFacturaSeleccionada);
			this.pojoFacturasRelacionadasExt.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoFacturasRelacionadasExt.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
			this.listFacturasRelacionadasExt.add(this.pojoFacturasRelacionadasExt);
			
			//this.pojoFactura.setIdFacturaRelacionada(this.pojoFacturaSeleccionada);
			
			// Recuperamos la Obra de la Factura relacionada
			this.pojoObra = this.ifzObra.findById(this.pojoFacturaSeleccionada.getIdObra());
			seleccionarObra();
			
			// Asignamos la sucursal
			this.sucursalId = this.pojoFacturaSeleccionada.getIdSucursal();
			evaluaFacturaFolio();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar obtener datos de la Factura seleccionada", e);
		} finally {
			nuevaBusquedaFacturas();
		}
	}

	// ---------------------------------------
	// ENVIO DE FACTURA
	// ---------------------------------------
	
	public void nuevoEnvio() {
		try {
			control();
			this.pojoFactura = this.ifzFactura.findExtById(this.idFactura);
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Factura indicada");
				return;
			}

			this.email = "";
			// Asunto, sugerido
			this.emailAsunto = "Envio de Factura " + this.pojoFactura.getFolioFactura();
			// Email cliente, sugerido
			if (this.pojoFactura.getIdObra().getIdCliente().getCorreo() != null && ! "".equals(this.pojoFactura.getIdObra().getIdCliente().getCorreo().trim()))
				this.email = this.pojoFactura.getIdObra().getIdCliente().getCorreo();
			this.emailCuerpo = "Anexo factura en formato PDF y XML";
			this.anexarPDF = true;
			this.anexarXML = true;
		} catch (Exception e) {
			control("Ocurrio un problema al iniciar la pantalla de envios de correos electronicos", e);
		}
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
			if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar enviar la Factura indicada");
				return;
			}
			
			if (this.email == null || "".equals(this.email.trim())) {
				control(13, "Ocurrio un problema al intentar establecer el remitente para el envio de la Factura.\nConsulte a su Administrador");
				return;
			}
			
			// Parametros para la ejecucion del reporte
			controlLog("Enviar Factura (PDF y XML) por correo a " + this.email + " ... ");
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
			if (this.anexarPDF) {
				controlLog("Generando parametros para la ejecucion de Reporte ... ");
				params = new HashMap<String, String>();
				params.put("idPrograma", 	  "168");
				params.put("nombreDocumento", nombreFacturaPDF);
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());
				
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idFactura", this.pojoFactura.getId());
				
				controlLog("Ejecutando reporte ... ");
				respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte, null);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					log.error("ERROR INTERNO: No se pudo procesar el reporte de Factura.\n" + respuesta.getErrores().getDescError());
					control(-1, "Ocurrio un problema y No se pudo procesar el PDF de la Factura indicada.\nContacte a su Administrador");
					return;
				}
				
				controlLog("Recuperando contenido de reporte (Factura PDF y XML) ... ");
				contenidoFacturaPDF = (byte[]) respuesta.getBody().getValor("contenidoReporte");
				if (contenidoFacturaPDF == null || contenidoFacturaPDF.length <= 0) {
					control(-1, "Ocurrio un problema al intentar recuperar el PDF de la Factura indicada.\nContacte a su Administrador");
					return;
				}
			}
			
			if (this.anexarXML)
				contenidoFacturaXML = this.pojoFactura.getIdTimbre().getCfdi();
			
			// Parametros para envio de correo
			controlLog("Asignando parametros de envio de correo ... ");
			correo = new HashMap<>();
			correo.put("from", this.usuarioEmail); 
			correo.put("fromPass", this.usuarioEmailClave);
			correo.put("to", this.email);
			correo.put("subject", this.emailAsunto);
			correo.put("body", this.emailCuerpo);
			
			// Adjuntos
			if (this.anexarPDF || this.anexarXML) {
				controlLog("Asignando adjuntos ... ");
				adjuntos = new HashMap<String, Object>();
				if (contenidoFacturaPDF != null)
					adjuntos.put(nombreFacturaPDF, contenidoFacturaPDF);
				if (contenidoFacturaXML != null)
					adjuntos.put(nombreFacturaXML, contenidoFacturaXML);
				controlLog("Adjuntos\n" + adjuntos.toString());
			}
			
			controlLog("Enviado correo ... ");
			respuesta = this.ifzReportes.enviarCorreo(correo, adjuntos);
			if (respuesta.getErrores().getCodigoError() != 0L) {
	    		control(-1, "Ocurrio un problema al intentar enviar la Factura (XML" + (this.anexarPDF ? ",PDF" : "") + ")" + " al correo electronico indicado");
				log.error("ERROR 501 - No se pudo enviar la factura por correo electronico\n" + respuesta.getErrores().getCodigoError() + ": " + respuesta.getErrores().getDescError());
				return;
			}
			
			controlLog("Factura Enviada a " + this.email + " desde " + this.usuarioEmail + " ... Proceso terminado!");
			control(false, 7, "Factura Enviada a " + this.email, null, this.usuarioEmail, this.email);
    	} catch (Exception e) {
    		control("Ocurrio un problema al enviar la Factura por correo electronico", e);
    	}
	}

	// ---------------------------------------
	// PROVISIONES
	// ---------------------------------------
	
	public void evaluaProvision() {
		boolean guardar = false;
		
		try {
			control();
			this.montoPagado = 0;
			this.montoProvision = 0;
			this.permiteProvisionar = false;

			// Cliente
			controlLog("Compruebo datos de Cliente ... ");
			if (! validaDatosCliente()) {
				asignarDatosCliente();
				controlLog("Asigno datos de Cliente. Pendiente actualizar ... ");
				guardar = true;
			}
			
			this.montoProvision = this.ifzFactura.calcularSaldo(this.pojoFactura.getId(), null, null).doubleValue();
			this.montoFacturado = (new BigDecimal((new DecimalFormat("0.00")).format(this.pojoFactura.getTotal()))).doubleValue();
			this.montoPagado 	= (new BigDecimal((new DecimalFormat("0.00")).format(this.montoPagado))).doubleValue();
			this.montoProvision = (new BigDecimal((new DecimalFormat("0.00")).format(this.montoProvision))).doubleValue();
			
			/*// Cargamos los pagos/abonos
			controlLog("Compruebo pagos/abonos ... ");
			liquidado = this.ifzPagos.findLiquidado(this.pojoFactura.getId(), null, null);
			this.montoPagado = Double.parseDouble((new DecimalFormat("#.00")).format(liquidado.doubleValue()));

			controlLog("Calculo Provision ... ");
			this.montoFacturado = (new BigDecimal((new DecimalFormat("0.00")).format(this.pojoFactura.getTotal()))).doubleValue();
			this.montoPagado 	= (new BigDecimal((new DecimalFormat("0.00")).format(this.montoPagado))).doubleValue();
			this.montoProvision = this.montoFacturado - this.montoPagado;*/
			
			if (this.montoProvision <= 0) {
				control(-1, "No se puede provisionar.\nFactura Liquidada");
				return;
			}

			controlLog("Sugerencia de monto de Provision: " + this.montoProvision);
			if (this.pojoFactura.getProvisionada() == 1)
				this.permiteProvisionar = true;
			
			// Actualizo los datos de la Factura si corresponde
			if (guardar) {
				controlLog("Actualizando factura ... ");
				this.ifzFactura.update(this.pojoFactura);
				controlLog("Factura actualizada!");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar evaluar la factura para Provision", e);
		}
	}
	
	public void provisionar() {
		boolean guardar = false;
		
		try {
			control();
			if (this.montoProvision <= 0) {
				controlLog("Intento provisionar una factura sin monto de provision. Factura " + this.pojoFactura.getId() + " - " + this.pojoFactura.getFolioFactura());
				control(-1, "Debe indicar un monto para la Provision");
				return;
			}

			// Comprobamos la moneda
			controlLog("Compruebo Moneda ... ");
			if (this.pojoFactura.getIdMoneda() == null && this.monedaBase != null) {
				this.pojoFactura.setIdMoneda(this.monedaBase.getId());
				this.pojoFactura.setDescripcionMoneda(this.monedaBase.getNombre());
				this.pojoFactura.setAbreviaturaMoneda(this.monedaBase.getAbreviacion());
				controlLog("Asigno datos de Moneda. Pendiente actualizar ... ");
				guardar = true;
			}
			
			// Comprobamos los datos de cliente
			controlLog("Compruebo datos de Cliente ... ");
			if (! validaDatosCliente()) {
				asignarDatosCliente();
				controlLog("Asigno datos de Cliente. Pendiente actualizar ... ");
				guardar = true;
			}
			
			// Actualizamos los datos de la factura si corresponde
			if (guardar) {
				controlLog("Actualizando Factura ... ");
				this.ifzFactura.update(this.pojoFactura);
				controlLog("Factura actualizada.");
				
				// Lanzamos la transaccion en un hilo secundario
				new Thread(this).start();
			} else {
				controlLog("Provisionando Factura ... ");
				Respuesta respuesta = this.ifzFactura.provisionar(this.pojoFactura, this.montoProvision, this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				if (respuesta.getErrores().getCodigoError() != 0L) {
					log.error("\nOcurrio un problema al intentar provisionar la Factura\n", (Throwable) respuesta.getBody().getValor("exception"));
					control(-1, respuesta.getErrores().getDescError());
					return;
				}
				controlLog("Factura Provisionada");
			}
			
			this.montoProvision = 0;
			this.montoFacturado = 0;
			this.montoPagado = 0;
		} catch (Exception e) {
			log.error("ERROR INTERNO: No se pudo enviar la Factura a Provision", e);
			control("Ocurrio un problema al intentar enviar la Factura a Provision", e);
		}
	}

	@Override
	public void run() {
		Respuesta respuesta = null;

		try {
			control();
			Thread.sleep(1000);
			controlLog("Provisionando Factura ... ");
			respuesta = this.ifzFactura.provisionar(this.pojoFactura, this.montoProvision, this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("\nOcurrio un problema al intentar provisionar la Factura\n", (Throwable) respuesta.getBody().getValor("exception"));
				control(-1, respuesta.getErrores().getDescError());
				return;
			}

			controlLog("Factura Provisionada");
		} catch (Exception e) {
			log.error("ERROR INTERNO: No se pudo enviar la Factura a Provision", e);
			control("Ocurrio un problema al intentar enviar la Factura a Provision", e);
		}
	}

	// --------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------

	public String getTituloFactura() {
		if (this.pojoFactura != null && this.pojoFactura.getId() != null && this.pojoFactura.getId() > 0L) {
			if (this.pojoFactura.getFolioFactura() != null && ! "".equals(this.pojoFactura.getFolioFactura().trim()))
				return "Factura " + this.pojoFactura.getFolioFactura() + " (" + this.pojoFactura.getId() + ")";
			return "Factura ID " + this.pojoFactura.getId();
		}
		return "Nueva Factura";
	}
	
	public void setTituloFactura(String value) {}

	public String getTituloFacturaAux() {
		if (this.pojoFactura != null && this.pojoFactura.getFolioFactura() != null && ! "".equals(this.pojoFactura.getFolioFactura().trim()))
			return this.pojoFactura.getFolioFactura() + " (" + this.pojoFactura.getId() + ")";
		return "Sin Folio (" + this.pojoFactura.getId() + ")";
	}
	
	public void setTituloFacturaAux(String value) {}

	public String getTituloConcepto() {
		if (this.pojoFactura != null && this.pojoFactura.getId() != null && this.pojoFactura.getId() > 0L) {
			if (this.pojoFactura.getFolioFactura() != null && ! "".equals(this.pojoFactura.getFolioFactura().trim()))
				return "Factura " + this.pojoFactura.getFolioFactura();
			return "Factura " + this.pojoFactura.getId();
		}
		return "Nueva Concepto";
	}
	
	public void setTituloConcepto(String value) {}

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

	public long getPojoFactura() {
		return this.idFactura;
	}
	
	public void setPojoFactura(long idFactura) {
		this.idFactura = idFactura;
	}

	public FacturaExt getFactura(){
		return this.pojoFactura;
	}
	
	public void setFactura(FacturaExt factura) {
		this.pojoFactura = factura;
	}

	public long getFacturaId() {
		if (getHasId())
			return this.pojoFactura.getId();
		return 0;
	}
	
	public void setFaturaId(long idFactura) {}
	
	public boolean getXMLPrevio() {
		return (this.isDebug && getHasId());
	}
	
	public void setXMLPrevio(boolean value) {}
	
 	public boolean getPermitirCambiarObra() {
		if ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()) || this.editable || this.pojoFactura == null || (this.pojoFactura != null && (this.pojoFactura.getId() == null || this.pojoFactura.getId() <= 0L)))
			return true;
		return false;
	}
	
	public void setPermitirCambiarObra(boolean value) {}
	
	public String getFolioFacturaRelacionada() {
		/*if (this.pojoFactura != null && this.pojoFactura.getIdFacturaRelacionada() != null && this.pojoFactura.getIdFacturaRelacionada().getId() != null && this.pojoFactura.getIdFacturaRelacionada().getId() > 0L)
			return this.pojoFactura.getIdFacturaRelacionada().getFolioFactura();
			*/
		if (this.pojoFactura != null && this.listFacturasRelacionadasExt != null && this.listFacturasRelacionadasExt.size() > 0 &&
				this.listFacturasRelacionadasExt.get(0).getIdFacturaRelacionada().getId() > 0L){
			String r = "";
			for (FacturasRelacionadasExt var : listFacturasRelacionadasExt) {
				r += "[" + var.getIdFacturaRelacionada().getFolioFactura() + "] ";
			}
			return r;
		}
		return "";
	}
	
	public void setFolioFacturaRelacionada(String value) {}

	public String getIdFacturaRelacionada() {
		if (this.pojoFactura != null && this.pojoFactura.getIdFacturaRelacionada() != null && this.pojoFactura.getIdFacturaRelacionada().getId() != null && this.pojoFactura.getIdFacturaRelacionada().getId() > 0L)
			return "Factura " + this.pojoFactura.getIdFacturaRelacionada().getId();
		return "";
	}
	
	public void setIdFacturaRelacionada(String value) {}

	public String getTipoComprobante() {
		if (this.pojoFactura != null && this.pojoFactura.getIdFacturaRelacionada() != null && this.pojoFactura.getIdFacturaRelacionada().getId() != null && this.pojoFactura.getIdFacturaRelacionada().getId() > 0L)
			return this.pojoFactura.getTipoComprobante();
		return "";
	}
	
	public void setTipoComprobante(String value) {}
	
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
	
	public List<FacturaDetalleImpuestoExt> getListConceptoFacImpuestos() {
		if (this.listDetalleImpuestos == null)
			this.listDetalleImpuestos = new ArrayList<FacturaDetalleImpuestoExt>();
		return this.listDetalleImpuestos;
	}
	
	public void setListConceptoFacImpuestos(List<FacturaDetalleImpuestoExt> listConceptoFacImpuestos) {
		this.listDetalleImpuestos = listConceptoFacImpuestos;
	}

	public int getNumPaginaImpuestos() {
		return numPaginaImpuestos;
	}
	
	public void setNumPaginaImpuestos(int numPaginaImpuestos) {
		this.numPaginaImpuestos = numPaginaImpuestos;
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

	public boolean isAnexarPDF() {
		return anexarPDF;
	}

	public void setAnexarPDF(boolean anexarPDF) {
		this.anexarPDF = anexarPDF;
	}

	public boolean isAnexarXML() {
		return anexarXML;
	}

	public void setAnexarXML(boolean anexarXML) {
		this.anexarXML = anexarXML;
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
		return valorBusquedaConceptos;
	}

	public void setValBusquedaConceptos(String valBusquedaConceptos) {
		this.valorBusquedaConceptos = valBusquedaConceptos;
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

	public boolean isConceptosDuplidados() {
		return conceptosDuplidados;
	}

	public void setConceptosDuplidados(boolean conceptosDuplidados) {
		this.conceptosDuplidados = conceptosDuplidados;
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
		return ("ADMINISTRADOR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()) ? true : this.perfilPermitirAsignarFolio);
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
	
	public String getIdTipoComprobante() {
		return tipoComprobante;
	}
	
	public void setIdTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
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
		if (this.pojoFactura != null && this.listFacturasRelacionadasExt != null && this.listFacturasRelacionadasExt.size() > 0 &&
				this.listFacturasRelacionadasExt.get(0).getIdFacturaRelacionada().getId() > 0L){
			String r = "";
			for (FacturasRelacionadasExt var : listFacturasRelacionadasExt) {
				r += "[" + var.getIdFacturaRelacionada().getUuid() + "] ";
			}
			return r;
		}
		return "";
	}

	public void setCfdiRelacionadoUuid(String cfdiRelacionadoUuid) {
		this.cfdiRelacionadoUuid = cfdiRelacionadoUuid;
	}
	
	public List<Factura> getListBusquedaCfdi() {
		return listBusquedaCfdi;
	}
	
	public void setListBusquedaCfdi(List<Factura> listBusquedaCfdi) {
		this.listBusquedaCfdi = listBusquedaCfdi;
	}
	
	public Factura getPojoCfdiSeleccionado() {
		return pojoCfdiSeleccionado;
	}
	
	public void setPojoCfdiSeleccionado(Factura pojoCfdiSeleccionado) {
		this.pojoCfdiSeleccionado = pojoCfdiSeleccionado;
	}
	
	public List<SelectItem> getTipoBusquedaCfdi() {
		return tipoBusquedaCfdi;
	}
	
	public void setTipoBusquedaCfdi(List<SelectItem> tipoBusquedaCfdi) {
		this.tipoBusquedaCfdi = tipoBusquedaCfdi;
	}
	
	public String getValorBusquedaCfdi() {
		return valorBusquedaCfdi;
	}
	
	public void setValorBusquedaCfdi(String valorBusquedaCfdi) {
		this.valorBusquedaCfdi = valorBusquedaCfdi;
	}
	
	public String getCampoBusquedaCfdi() {
		return campoBusquedaCfdi;
	}
	
	public void setCampoBusquedaCfdi(String campoBusquedaCfdi) {
		this.campoBusquedaCfdi = campoBusquedaCfdi;
	}
	
	public int getNumPaginaCfdi() {
		return numPaginaCfdi;
	}
	
	public void setNumPaginaCfdi(int numPaginaCfdi) {
		this.numPaginaCfdi = numPaginaCfdi;
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

	public int getPagosRegistrados() {
		return pagosRegistrados;
	}

	public void setPagosRegistrados(int pagosRegistrados) {
		this.pagosRegistrados = pagosRegistrados;
	}

	public List<FacturaPagosExt> getListFacturaPagos() {
		return listFacturaPagos;
	}

	public void setListFacturaPagos(List<FacturaPagosExt> listFacturaPagos) {
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
		if (this.pojoFactura != null && this.pojoFactura.getIdFormaPago() != null && this.pojoFactura.getIdFormaPago() > 0L) 
			return this.pojoFactura.getIdFormaPago();
		return 0L;
	}
	
	public void setFormaPago(long idFormaPago) {
		if (idFormaPago <= 0)
			return;
		for (FormasPagos var : this.listFormasPago) {
			if (var.getId().longValue() == idFormaPago) {
				this.pojoFactura.setIdFormaPago(var.getId());
				this.pojoFactura.setFormaPago(var.getClaveSat() + " - " + var.getFormaPago());
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

	public boolean isOrdenCronologico() {
		return ordenCronologico;
	}

	public void setOrdenCronologico(boolean ordenCronologico) {
		this.ordenCronologico = ordenCronologico;
	}
	
	public List<Factura> getListBusquedaFacturas() {
		return listBusquedaFacturas;
	}
	
	public void setListBusquedaFacturas(List<Factura> listBusquedaFacturas) {
		this.listBusquedaFacturas = listBusquedaFacturas;
	}
	
	public Factura getPojoFacturaSeleccionada() {
		return pojoFacturaSeleccionada;
	}
	
	public void setPojoFacturaSeleccionada(Factura pojoFacturaSeleccionada) {
		this.pojoFacturaSeleccionada = pojoFacturaSeleccionada;
	}
	
	public List<SelectItem> getTipoBusquedaFacturas() {
		return tipoBusquedaFacturas;
	}
	
	public void setTipoBusquedaFacturas(List<SelectItem> tipoBusquedaFacturas) {
		this.tipoBusquedaFacturas = tipoBusquedaFacturas;
	}
	
	public String getValorBusquedaFacturas() {
		return valorBusquedaFacturas;
	}
	
	public void setValorBusquedaFacturas(String valorBusquedaFacturas) {
		this.valorBusquedaFacturas = valorBusquedaFacturas;
	}
	
	public String getCampoBusquedaFacturas() {
		return campoBusquedaFacturas;
	}
	
	public void setCampoBusquedaFacturas(String campoBusquedaFacturas) {
		this.campoBusquedaFacturas = campoBusquedaFacturas;
	}
	
	public int getNumPaginaFacturas() {
		return numPaginaFacturas;
	}
	
	public void setNumPaginaFacturas(int numPaginaFacturas) {
		this.numPaginaFacturas = numPaginaFacturas;
	}

	public List<Factura> getListNotasCredito() {
		return listNotasCredito;
	}

	public void setListNotasCredito(List<Factura> listNotasCredito) {
		this.listNotasCredito = listNotasCredito;
	}

	public int getPaginacionNotasCredito() {
		return paginacionNotasCredito;
	}

	public void setPaginacionNotasCredito(int paginacionNotasCredito) {
		this.paginacionNotasCredito = paginacionNotasCredito;
	}

	public long getIdNotaCredito() {
		return idNotaCredito;
	}

	public void setIdNotaCredito(long idNotaCredito) {
		this.idNotaCredito = idNotaCredito;
	}

	public List<SelectItem> getListAuditoriaItems() {
		return listAuditoriaItems;
	}

	public void setListAuditoriaItems(List<SelectItem> listAuditoriaItems) {
		this.listAuditoriaItems = listAuditoriaItems;
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------
	
	public boolean getHasId() {
		return (this.pojoFactura != null && this.pojoFactura.getId() != null && this.pojoFactura.getId() > 0L);
	}
	
	public void setHasId(boolean value) {}
	
	public int getFacturaEstatus() {
		return (this.pojoFactura != null) ? this.pojoFactura.getEstatus() : 1;
	}
	
	public boolean getEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {}
	
	public boolean isFacturaTimbrada() {
		return facturaTimbrada;
	}

	public void setFacturaTimbrada(boolean facturaTimbrada) {
		this.facturaTimbrada = facturaTimbrada;
	}

	public boolean getValidarBotonTimbrar() {
		if ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()) && getHasId())
			return true;
		return this.editable && getHasId();
	}
	
	public void setValidarBotonTimbrar(boolean value) {}

	public boolean getPermisoAdmin() {
		return ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()));
	}
	
	public void setPermisoAdmin(boolean value) {}

	public boolean getTesting() {
		return validaRequest("CFDI_TEST");
	}
	
	public void setTesting(boolean value) {
		validaRequest("CFDI_TEST", value);
	}

	public boolean getNoTimbrar() {
		return validaRequest("NOTIMBRAR");
	}

	public void setNoTimbrar(boolean value) {
		validaRequest("NOTIMBRAR", value);
	}

	public FacturasRelacionadas getPojoFacturasRelacionadas() {
		return pojoFacturasRelacionadas;
	}

	public void setPojoFacturasRelacionadas(FacturasRelacionadas pojoFacturasRelacionadas) {
		this.pojoFacturasRelacionadas = pojoFacturasRelacionadas;
	}

	public FacturasRelacionadasExt getPojoFacturasRelacionadasExt() {
		return pojoFacturasRelacionadasExt;
	}

	public void setPojoFacturasRelacionadasExt(FacturasRelacionadasExt pojoFacturasRelacionadasExt) {
		this.pojoFacturasRelacionadasExt = pojoFacturasRelacionadasExt;
	}

	public List<FacturasRelacionadasExt> getListFacturasRelacionadasExt() {
		return listFacturasRelacionadasExt;
	}

	public void setListFacturasRelacionadasExt(List<FacturasRelacionadasExt> listFacturasRelacionadasExt) {
		this.listFacturasRelacionadasExt = listFacturasRelacionadasExt;
	}

	public List<FacturasRelacionadas> getListFacturasRelacionadas() {
		return listFacturasRelacionadas;
	}

	public void setListFacturasRelacionadas(List<FacturasRelacionadas> listFacturasRelacionadas) {
		this.listFacturasRelacionadas = listFacturasRelacionadas;
	}
}

// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA    |        AUTOR       | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//    1.0  | 2016-10-18  | Javier Tirado      | Agregamos el listado de metodos de pago 
//    2.2  | 2017-05-15  | Javier Tirado      | Agilizamos la busqueda de obras (cambio de extendido a normal)
