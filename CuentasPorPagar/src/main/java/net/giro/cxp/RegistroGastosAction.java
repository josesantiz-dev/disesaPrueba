package net.giro.cxp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.*;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.Persona; 
import net.giro.clientes.logica.PersonaRem;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConGrupoValores; // import cde.cont.admon.datos.ConGrupoValores;
import net.giro.plataforma.beans.ConValores; // import cde.cont.admon.datos.ConValores;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem; // import cde.cont.admon.logica.ConValoresFacadeRemote;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.plataforma.Utilerias; // import cde.plataforma.Utilerias;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.FacturaConcepto;
import net.giro.cxp.beans.FacturaImpuestos;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt; // import cde.tyg.admon.datos.Tygpersonas;
import net.giro.cxp.beans.TiposGastoCXP;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.CuentasBancariasRem;
import net.giro.cxp.logica.GastosImpuestoRem;
import net.giro.cxp.logica.PagosGastosDetImpuestosRem;
import net.giro.cxp.logica.PagosGastosDetRem;
import net.giro.navegador.LoginManager;

public class RegistroGastosAction implements Serializable {
	private static Logger log = Logger.getLogger(RegistroGastosAction.class);
	private static final long serialVersionUID = 1L;
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
   	private HttpSession httpSession;
	private InitialContext ctx;
	private PagosGastosRem ifzRegGastos; 
	private PagosGastosDetRem ifzRegGastosDet;
	private ConGrupoValoresRem ifzGpoVal;	
	private CuentasBancariasRem ifzCtas; 
	private GastosImpuestoRem ifzGastoImpuesto;
	private ConValoresRem ifzConValores;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	private PersonaRem ifzPersonas;
	private SucursalesRem ifzSucursal;
	private PagosGastosDetImpuestosRem ifzDesgloImpto;
	private ReportesRem ifzReporte;
	private List<Persona> listTerceros; 
	private List<Sucursal> listSucursales;	
	private List<SelectItem> listSucursalesItems;
	private List<PagosGastosDetImpuestosExt> lisImpuestosEliminados;
	private List<PagosGastosDetImpuestosExt> listDesgloseImpuestos;
	private List<PagosGastosDetImpuestosExt> listDesgloseRetenciones;
	private List<GastosImpuestoExt> listImpuestosDelGasto;
	private List<GastosImpuestoExt> listRetencionesDelGasto;
	private List<Persona> listBeneficiarios;
	private List<CuentaBancaria> listCuentas;
	private List<SelectItem> listCuentasItems;
	private List<Persona> listProveedores;
	private List<PagosGastos> listRegistroGastos;
	private List<ConValores> listRetEncontradas;
	private List<FacturaImpuestos> listFacturaConImpuestos;
	private List<FacturaImpuestos> listFacturaConImpuestosEliminadas;
	private PagosGastosExt pojoRegistroGasto;
	private long pojoRegistroGastoMain;
	private PagosGastosDetExt pojoRegistroGastoComprobado;
	private PagosGastosDetImpuestosExt pojoDesgloImpto;
	private ConGrupoValores pojoGpoValPersonas;
	private ConGrupoValores pojoGpoValGasto;
	private ConGrupoValores pojoGpoValTerceros;
	private CtasBancoExt pojoCtas;
	private ConValores pojoConceptoGtos;
	private ConValores pojoNvaRet;
	private PersonaExt pojoBeneficiarios;
	private PersonaExt pojoProveedores;
	private PersonaExt pojoTerceros;
	private FacturaImpuestos reg; 
	private Double subtotal;
	private Double totalImpuestos;
	private Double totalRetenciones;
	private Double montoTotalFactura;
	private Double totalPago;
	private Double totalFacturasReportadas;
	private String terceros;
	private String operacion;
	private String conceptoGasto;
	private String conceptoGastoPrevio;
	private String tipoPersona;
	private String nombreProveedor;	
	private String nombreBeneficiario;
	private String beneficiario;
	private String beneficiarioAsignado;
	private String cuenta;	
	private String valBusqRet;
	private String campoBusqRet;
	private String perfilRequiereObra;
	private boolean band;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private String sucursalesVisibles;
	private HashMap <Integer, String> mensajesInf;
	private String[] listBusqRet;
	private long usuarioId;
	private String usuario;
	private boolean esTransferencia;
	private boolean existeRetencion;
	private boolean esCheque;
	private boolean esSpei;
   	private int numPaginaFact;
	private int tipoReporte;
	private boolean sinFactura;
	private ConGrupoValores pojoGpoValFormasPago; 
	private List<ConValores> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	private boolean reqReferencia;
	private String referenciaOperacion;
	// Busqueda Principal
	private List<SelectItem> tiposBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
   	private int numPagina;
	// Carga XML
	// ------------------------------------------------
	private FtpRem ifzFtp;
	private byte[] fileSrc;
	private String fileName;
	private boolean fileLoaded;
	private Long facturaId;
	private HashMap<String, Object> facturaValores;
	// ------------------------------------------------
	private Date facturaFecha;
	private String facturaUuid;
	private String facturaRfc;
	private String facturaEmisor;
	private String facturaTipo;
	private String facturaMoneda;
	private Double facturaTipoCambio;
	private Double facturaDescuento;
	private Double facturaSubtotal;
	private Double facturaTotal;
	private List<FacturaConcepto> listFacturaConceptos;
	private double conceptosTotalImportes;
	private double conceptosTotalImpuestos;
	private double conceptosTotal;
	private int paginaFacturaComprobacion;
	// ------------------------------------------------
	private boolean facturaDescartada;
	private boolean facturaActualizar;
	private String prefijoFacturas;
	private String ftpDigitalizacionHost;
	private String ftpDigitalizacionPort;
	private String ftpDigitalizacionUser;
	private String ftpDigitalizacionPass;
	private String ftpDigitalizacionRuta;
	// BUSQUEDA CONCEPTOS
	// ------------------------------------------------
	private List<ConValores> listConceptoGasto;
	private ConValores pojoConcepto;
	private List<SelectItem> tiposBusquedaConceptos;
	private String campoBusquedaConceptos;
	private String valorBusquedaConceptos;
	private int pagBusquedaConceptos;
	private boolean avanzadaConceptos;
	// Busqueda Obras
	// ------------------------------------------------
	private ObraRem ifzObras;
	private Obra pojoObra;
	private List<Obra> listObras;
	private List<SelectItem> listObrasTiposItems;
	private int tipoObraBusquedaObra;
	private List<SelectItem> tipoBusquedaObra;
	private String valTipoBusquedaObra;
	private String campoBusquedaObra;
	private int tipoObra;
	private int paginacionObras;
	public boolean avanzadaObra;
	// ORDEN DE COMPRA
	// ------------------------------------------------
	private OrdenCompraRem ifzOrdenCompra;
	private List<OrdenCompra> listOrdenCompra;
	private OrdenCompra pojoOrdenCompra;
	private List<SelectItem> listTiposBusquedaOrdenCompra;
	private String campoBusquedaOrdenCompra;
	private String valorBusquedaOrdenCompra;
	public boolean busquedaAvanzadaOrdenCompra;
	private int paginacionOrdenCompra;
	// EMPLEADO-USUARIO
	// ------------------------------------------------
	private EmpleadoRem ifzEmpleado;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalSugerida;
	// PERFILES
	// ------------------------------------------------
	private boolean perfilEgresos;
	private boolean perfilPermiteEditar;
	// DEBUG
	// ------------------------------------------------
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public RegistroGastosAction() {
		PropertyResourceBundle msgProperties = null;
		ValueExpression valExp = null;
		FacesContext facesContext = null;
		Application app = null;
		String valPerfil = "";
		
		try {
			facesContext = FacesContext.getCurrentInstance();
	        app = facesContext.getApplication();

	        this.httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
	        
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(facesContext.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(facesContext.getELContext());
			
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			msgProperties = (PropertyResourceBundle) valExp.getValue(facesContext.getELContext());

			Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// EVALUACION DE PERFILES
			//valPerfil = this.loginManager.getAutentificacion().getPerfil("REQUIERE_OBRA");
			this.perfilRequiereObra = this.loginManager.getAutentificacion().getPerfil("REQUIERE_OBRA");
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("REGISTRO_GASTOS_EDITAR");
			this.perfilPermiteEditar = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
	   		this.ctx = new InitialContext();
	   		this.ifzRegGastos 		= (PagosGastosRem) 				this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
	   		this.ifzGastoImpuesto 	= (GastosImpuestoRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
	   		this.ifzDesgloImpto 	= (PagosGastosDetImpuestosRem) 	this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
	   		//this.ifzProPagDet 	= (ProgPagosDetalleRem) 		this.ctx.lookup("ejb:/Logica_CuentasPorPagar//ProgPagosDetalleFac!net.giro.cxp.logica.ProgPagosDetalleRem");
	   		this.ifzObras 			= (ObraRem) 					this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
	   		this.ifzGpoVal 			= (ConGrupoValoresRem) 			this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
	   		this.ifzConValores 		= (ConValoresRem) 				this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
	   		this.ifzSucursal 		= (SucursalesRem) 				this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
	   		this.ifzReporte 		= (ReportesRem) 				this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
	   		this.ifzCtas 			= (CuentasBancariasRem) 		this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
	   		this.ifzFtp 			= (FtpRem) 						this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzRegGastosDet 	= (PagosGastosDetRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzOrdenCompra 	= (OrdenCompraRem) 				this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzLocalidad 		= (LocalidadDAO) 				this.ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			this.ifzColonia 		= (ColoniaDAO) 					this.ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");		
			this.ifzEmpleado 		= (EmpleadoRem) 				this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzPersonas 		= (PersonaRem) 					this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
	   		
			this.ifzRegGastos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGastoImpuesto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
			//this.ifzProPagDet.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReporte.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzRegGastosDet.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenCompra.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			
			comprobarUsuario();
			getFTPData();
			this.prefijoFacturas = "CXP-RG-"; 
	   		
			this.pojoGpoValPersonas = new ConGrupoValores();
			this.pojoRegistroGasto = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoBeneficiarios= new PersonaExt();
			this.pojoConceptoGtos = new ConValores();
			this.pojoRegistroGastoComprobado = new PagosGastosDetExt();
			this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
			this.pojoConceptoGtos = new ConValores();
			this.pojoProveedores = new PersonaExt();
			this.pojoGpoValGasto = new ConGrupoValores();
			this.pojoTerceros = new PersonaExt();
			this.pojoGpoValTerceros = new ConGrupoValores();
			this.pojoNvaRet = new ConValores();	
			this.listRegistroGastos = new ArrayList<PagosGastos>();
			this.listTerceros = new ArrayList<Persona>();
			this.listSucursales = new ArrayList<Sucursal>();
			this.listCuentas = new ArrayList<CuentaBancaria>();
			this.listBeneficiarios = new ArrayList<Persona>();
			this.lisImpuestosEliminados = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>(); 
			this.listDesgloseRetenciones= new ArrayList<PagosGastosDetImpuestosExt>();
			this.listRetEncontradas = new ArrayList<ConValores>();
			this.listImpuestosDelGasto = new ArrayList<GastosImpuestoExt>();
			this.listRetencionesDelGasto = new ArrayList<GastosImpuestoExt>();	
			this.reg = new FacturaImpuestos();
			this.listSucursalesItems = new ArrayList<SelectItem>();
			this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>(); 	
			
			this.numPaginaFact = 1;
			this.tipoMensaje = 0;
			this.cuenta = "";
			this.beneficiario = "";	
			this.tipoPersona = "P";
			this.operacion = "C";
			this.mensaje = "";
			this.esTransferencia = false;
			this.esCheque = true;
			this.esSpei = false;
			this.band = false;
			this.sinFactura = false;
			this.existeRetencion = false;
			
			this.subtotal = 0D;
			this.totalImpuestos = 0D;
			this.totalRetenciones = 0D;
			this.montoTotalFactura = 0D;
			this.totalPago = 0D;
			this.totalFacturasReportadas = 0D;
			
			// Busqueda principal
			// ----------------------------------------------------------------------
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("beneficiario", "Beneficiario"));
			this.tiposBusqueda.add(new SelectItem("concepto", "Concepto"));
			this.tiposBusqueda.add(new SelectItem("folioAutorizacion", "Referencia"));
			this.tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.fechaBusqueda = Calendar.getInstance().getTime();
			this.numPagina = 1;
	   		
	   		// BUSQUEDA CONCEPTOS
	   		this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
	   		this.tiposBusquedaConceptos.add(new SelectItem("descripcion", "Descripcion"));
	   		this.tiposBusquedaConceptos.add(new SelectItem("id", "ID"));
	   		nuevaBusquedaConceptos();

			// Busqueda Obras
			this.tipoBusquedaObra = new ArrayList<SelectItem>();
			this.tipoBusquedaObra.add(new SelectItem("*", "Concidencia"));
			this.tipoBusquedaObra.add(new SelectItem("nombre", "Nombre"));
			this.tipoBusquedaObra.add(new SelectItem("nombreCliente", "Cliente"));
			this.tipoBusquedaObra.add(new SelectItem("nombreContrato", "Contrato"));
			this.tipoBusquedaObra.add(new SelectItem("id", "ID"));
			// ---------------------------------------------------
			this.listObrasTiposItems = new ArrayList<SelectItem>();
			this.listObrasTiposItems.add(new SelectItem(1, msgProperties.getString("obrasTipo.obra")));
			this.listObrasTiposItems.add(new SelectItem(2, msgProperties.getString("obrasTipo.proyecto")));
			this.listObrasTiposItems.add(new SelectItem(3, msgProperties.getString("obrasTipo.ordenTrabajo")));
			if (this.perfilEgresos)
				this.listObrasTiposItems.add(new SelectItem(4, msgProperties.getString("obrasTipo.administrativa")));
			obrasBusqueda();
			
			// Busqueda Orden de Compra
			this.listTiposBusquedaOrdenCompra = new ArrayList<SelectItem>();
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("*", "Concidencia"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("folio", "Folio"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("nombreProveedor", "Proveedor"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("nombreSolicita", "Solicita"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("nombreContacto", "Contacto"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("id", "ID"));
			ordenCompraBusqueda();
			
			this.mensajesInf = new HashMap<Integer, String>();
			this.mensajesInf.put(-1, msgProperties.getString("mensaje.error.inesperado"));
			this.mensajesInf.put( 1, msgProperties.getString("mensaje.info.impuestoDuplicado"));
			this.mensajesInf.put( 2, msgProperties.getString("mensaje.info.faltanFacturas"));
			this.mensajesInf.put( 3, msgProperties.getString("mensaje.info.noEliminar"));
			this.mensajesInf.put( 4, msgProperties.getString("mensaje.info.noEditar"));
			this.mensajesInf.put( 5, msgProperties.getString("mensaje.info.imptosCero"));
			this.mensajesInf.put( 6, msgProperties.getString("mensaje.info.montoFacturaCero"));
			this.mensajesInf.put( 7, msgProperties.getString("mensaje.info.montoFacturaMenorImptos"));
			this.mensajesInf.put(10, msgProperties.getString("mensaje.info.noAgregar"));
			this.mensajesInf.put(11, msgProperties.getString("mensaje.info.noEditarGto"));
			this.mensajesInf.put(12, msgProperties.getString("navegacion.label.montoInvalido"));
			this.mensajesInf.put(13, msgProperties.getString("mensaje.info.busquedaVacia"));
			this.mensajesInf.put(14, msgProperties.getString("mensaje.info.noCancelar"));
			this.mensajesInf.put(15, msgProperties.getString("navegacion.label.saldoInsuficiente"));
			this.mensajesInf.put(16, msgProperties.getString("navegacion.label.retencionesPend"));
			this.mensajesInf.put(17, msgProperties.getString("navegacion.label.retDuplicada"));
			this.mensajesInf.put(18, msgProperties.getString("navegacion.label.mtoRet"));
			this.mensajesInf.put(19, msgProperties.getString("navegacion.label.gtoNoPro"));
			this.mensajesInf.put(20, msgProperties.getString("navegacion.label.gtoDupli"));
			this.mensajesInf.put(21, msgProperties.getString("navegacion.label.gtoNoRev"));
			this.mensajesInf.put(22, msgProperties.getString("navegacion.label.gtoMtoInva"));
	   		
			this.listBusqRet = new String[3];
			this.listBusqRet[0] = "Clave";
			this.listBusqRet[1] = "Descripcion";
			this.listBusqRet[2] = "Cuenta Contable";
			this.tipoReporte = 1;
			this.valBusqRet = "";
			this.campoBusqRet = this.listBusqRet[0];
			
			// FORMAS DE PAGO
			this.pojoGpoValFormasPago = this.ifzGpoVal.findByName("SYS_FORMAS_PAGO");
			if (this.pojoGpoValFormasPago == null || this.pojoGpoValFormasPago.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_FORMAS_PAGO en con_grupo_valores");

			// MOVIMIENTOS GASTOS
			this.pojoGpoValGasto = this.ifzGpoVal.findByName("SYS_MOVGTOS");
			if (this.pojoGpoValGasto == null || this.pojoGpoValGasto.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");

			// RELACIONES PERSONAS
			this.pojoGpoValPersonas = this.ifzGpoVal.findById(4L); // valGpoPersonas
			if (this.pojoGpoValPersonas == null || this.pojoGpoValPersonas.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");

			this.reqReferencia = true;
			cargarSucursales();
			cargarFormasDePago();
			cargarCuentas();
			this.pojoRegistroGasto = new PagosGastosExt();
			control();
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.RegistroGastosAction", e);
			this.ctx = null;
		}
  	}
   	
   	public void buscar() {
   		String orderBy = "";
   		
   		try {
   			control();
   			if (this.campoBusqueda == null || "".equals(this.campoBusqueda.trim()))
   				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
   			
   			if ("fecha".equals(this.campoBusqueda)) {
   				this.valorBusqueda = "";
   				orderBy = "id desc";
   			} else {
   				orderBy = "fecha desc, id desc";
				this.valorBusqueda = this.valorBusqueda.trim().replace(" ", "%");
   			}
   			
			this.ifzRegGastos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listRegistroGastos = this.ifzRegGastos.findRegistroGastosLikeProperty(this.campoBusqueda, ("fecha".equals(this.campoBusqueda) ? this.fechaBusqueda : this.valorBusqueda), "", true, orderBy, 0);
			if (this.listRegistroGastos == null || this.listRegistroGastos.isEmpty())
				control(13, this.mensajesInf.get(13));
		} catch (Exception e) {
			this.listRegistroGastos = new ArrayList<PagosGastos>();
			control("Ocurrio un problema al consultar los Registros de Gastos", e);
		}
   	}
   	
   	public void nuevo() {
		try {
			this.numPaginaFact = 1;
			this.pojoRegistroGasto = new PagosGastosExt();
			this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>();
			
			this.pojoCtas = new CtasBancoExt();
			this.pojoBeneficiarios = new PersonaExt();
			this.pojoTerceros = new PersonaExt();
			this.beneficiarioAsignado = "";
			
			this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.lisImpuestosEliminados = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseRetenciones = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listImpuestosDelGasto = new ArrayList<GastosImpuestoExt>();
			this.listRetEncontradas = new ArrayList<ConValores>();
			this.montoTotalFactura = 0D;

			cargarSucursales();
			setSucursal(this.idSucursalSugerida);

			this.cuenta = "";
			this.tipoPersona = "P";
			this.beneficiario = "";
			if (this.pojoEmpleadoUsuario != null && this.pojoEmpleadoUsuario.getId() != null && this.pojoEmpleadoUsuario.getId() > 0L)
				this.beneficiario = this.pojoEmpleadoUsuario.getPersona().getId() + " - " + this.pojoEmpleadoUsuario.getNombre();
			this.operacion = ""; 
			this.esTransferencia = false;
			this.esSpei = false;
			this.esCheque = true;
			this.existeRetencion = false;
			this.totalFacturasReportadas = 0D;
			this.referenciaOperacion = "";
			
			obrasBusqueda();
			ordenCompraBusqueda();
			agregar();
		} catch (Exception e) {
			log.error("Error en el metodo nuevo.", e);
		}
	}

   	public void editar() {
   		List<PagosGastosDetImpuestosExt> listImpuestos = null;
   		List<PagosGastosDetExt> listDetalles = null; 
   		long idSucursal = 0;
   		
   		try {
   			control();
			this.numPaginaFact = 1;
			this.beneficiarioAsignado = "";
   			log.info("RegistroGasto ... Preparando para editar");
   			this.pojoRegistroGasto = this.ifzRegGastos.findExtById(this.pojoRegistroGastoMain);
   			if (this.pojoRegistroGasto == null) {
   	   			log.info("RegistroGasto ... No selecciono ningun registro");
   	   			control(-1, "No selecciono ningun registro");
   				return;
   			}
   			
   			this.operacion = this.pojoRegistroGasto.getOperacion();
			this.referenciaOperacion = "";
   			if ("C".equals(this.operacion)) 
   				this.referenciaOperacion = this.pojoRegistroGasto.getNoCheque().toString();
			else if ("T".equals(this.operacion)) 
				this.referenciaOperacion = this.pojoRegistroGasto.getFolioAutorizacion();
   			
   			// Sucursal
   			idSucursal = this.pojoRegistroGasto.getIdSucursal().getId();
			cargarSucursales();
			setSucursal(idSucursal);
			
			// Cuenta
			log.info("RegistroGasto... Recuperando Cuenta Origen");
			this.cuenta = "";
			if (this.pojoRegistroGasto != null && this.pojoRegistroGasto.getIdCuentaOrigen() != null && this.pojoRegistroGasto.getIdCuentaOrigen().getId() > 0L) {
				this.pojoCtas = this.pojoRegistroGasto.getIdCuentaOrigen();
				this.cuenta = this.pojoRegistroGasto.getIdCuentaOrigen().getId() + " - " 
						+ this.pojoRegistroGasto.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto() + " - " 
						+ this.pojoRegistroGasto.getIdCuentaOrigen().getNumeroDeCuenta();
				log.info("RegistroGasto... Cuenta Origen recuperada");
			}
			
			// Beneficiario
			log.info("RegistroGasto... Recuperando Beneficiario");
			this.beneficiario = "";
			if (this.pojoRegistroGasto != null && this.pojoRegistroGasto.getIdBeneficiario() != null && this.pojoRegistroGasto.getIdBeneficiario().getId() > 0L) {
				this.beneficiario = this.pojoRegistroGasto.getIdBeneficiario().getId() + " - " + this.pojoRegistroGasto.getBeneficiario();
				this.pojoBeneficiarios = this.pojoRegistroGasto.getIdBeneficiario();
				this.beneficiarioAsignado = this.pojoBeneficiarios.getRfc();
				log.info("RegistroGasto... Beneficiario recuperado");
			}
			
			if (this.pojoRegistroGasto.getTipoBeneficiario() != null && ! "".equals(this.pojoRegistroGasto.getTipoBeneficiario().trim()))
   				this.tipoPersona = this.pojoRegistroGasto.getTipoBeneficiario();
			
   			this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>();
   			this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
   			
   			// recuperamos detalle de Registro de Gastos
			log.info("RegistroGasto ... Recuperando detalles");
   			listDetalles = this.ifzRegGastosDet.findExtAll(this.pojoRegistroGasto.getId());
   			if (listDetalles != null && ! listDetalles.isEmpty()) {
   	   			for (PagosGastosDetExt var : listDetalles) {
   	   				this.reg = new FacturaImpuestos();
   	   				this.reg.setPojoFactura(var);

					try {
						listImpuestos = this.ifzDesgloImpto.findExtAll(var.getId());
						if (listImpuestos == null || listImpuestos.isEmpty()) {
							editarFactura();
							listImpuestos = copiaListas(this.listDesgloseImpuestos);
						}
						
						this.reg.setListImpuestos(listImpuestos); 
					} catch (Exception e) {
						log.error("Error al obtener la lista de impuestos de la factura " + var.getReferencia() + " (" + var.getId() + ")", e);
					}
					
   	   	   			this.listFacturaConImpuestos.add(this.reg);
   	   			}
   			}

			log.info("RegistroGasto ... " + this.listFacturaConImpuestos.size() + " detalles recuperados");
			this.reg = new FacturaImpuestos();
			actualizaTotalFaturas();
			log.info("RegistroGasto... Lista para editar");
   		} catch (Exception e) {
			control("Ocurrio un problema al intentar editar el Registro", e);
   		}
   	}
   	
   	public void guardar() {
		Respuesta resp = new Respuesta();
		boolean registroNuevo = false;
		boolean borrarXML = false;
		String fileName = "";
		
		try {
			control();
			if (! validaciones())
				return;
			
			if (this.pojoRegistroGasto.getId() == null)
				registroNuevo = true;
			
			/*if (this.pojoObra != null) {
				if (this.pojoRegistroGasto.getIdObra() == null) {
					this.pojoRegistroGasto.setIdObra(this.pojoObra);
				} else {
					if (this.pojoRegistroGasto.getIdObra().getId() != null && this.pojoRegistroGasto.getIdObra().getId() != this.pojoObra.getId())
						this.pojoRegistroGasto.setIdObra(this.pojoObra);
				}
			}*/

			if (this.pojoRegistroGasto.getIdObra() == null)
				this.pojoRegistroGasto.setIdObra(new Obra());
			if (this.pojoRegistroGasto.getIdOrdenCompra() <= 0L)
				this.pojoRegistroGasto.setIdOrdenCompra(0L);
			this.pojoRegistroGasto.setIdCuentaOrigen(this.pojoCtas);
			this.pojoRegistroGasto.setIdBeneficiario(this.pojoBeneficiarios);
			this.pojoRegistroGasto.setBeneficiario(this.nombreBeneficiario);
			this.pojoRegistroGasto.setTipoBeneficiario(this.tipoPersona);
			this.pojoRegistroGasto.setIdCuentaDestinoTerceros(this.pojoTerceros);
			this.pojoRegistroGasto.setMonto(this.totalFacturasReportadas);
			this.pojoRegistroGasto.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			this.pojoRegistroGasto.setOperacion(this.operacion);
			if ("C".equals(this.operacion))
				this.pojoRegistroGasto.setNoCheque(Integer.parseInt(this.referenciaOperacion));
			else if ("T".equals(this.operacion))
				this.pojoRegistroGasto.setFolioAutorizacion(this.referenciaOperacion);
			
			if (registroNuevo) {
				this.pojoRegistroGasto.setTipo("P");//gasto "Pago"
				this.pojoRegistroGasto.setEstatus("C");//comprobado
				this.pojoRegistroGasto.setCreadoPor(this.usuarioId);
				this.pojoRegistroGasto.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoRegistroGasto.setModificadoPor(this.usuarioId);
				this.pojoRegistroGasto.setFechaModificacion(Calendar.getInstance().getTime());

				this.ifzRegGastos.setInfoSesion(this.loginManager.getInfoSesion());
				resp = ifzRegGastos.salvar(this.pojoRegistroGasto);
				
				//validando folio cheque					
				if (resp.getResultado() != -1) {
					this.pojoRegistroGasto.setId(Long.valueOf(resp.getReferencia()));
				} else {
					if (! "BIEN".equals(resp.getRespuesta())) {
						band = true;// esta variable termine usandola como bandera gral
						this.mensaje = resp.getRespuesta();
						
						if ((resp.getRespuesta().indexOf("SE CANCELA EL FOLIO ") > -1) || (resp.getRespuesta().indexOf("SE CANCELAN LOS FOLIOS ") > -1))
							this.tipoMensaje = 9;
						else
							this.tipoMensaje = 8;
						return;
					}
				}
				
				this.listRegistroGastos.add(this.ifzRegGastos.convertir(this.pojoRegistroGasto));
			} else {
				this.pojoRegistroGasto.setModificadoPor(this.usuarioId);
				this.pojoRegistroGasto.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Actualiza Registro de Gastos
				this.ifzRegGastos.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzRegGastos.update(this.pojoRegistroGasto);

				// Actualizo en la LISTA
				for (PagosGastos var : this.listRegistroGastos) {
					if (var.getId().equals(this.pojoRegistroGasto.getId())) {
						var = this.ifzRegGastos.convertir(this.pojoRegistroGasto);
						break;
					}
				}
			}
				
			//grabando las facturas en caso de que hayan agregado algunas
			if (! this.listFacturaConImpuestos.isEmpty()) {
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					var.getPojoFactura().setIdPagosGastos(this.pojoRegistroGasto);
					if (var.getPojoFactura().getId() != null && var.getPojoFactura().getId() > 0L) 
						this.ifzRegGastosDet.update(var.getPojoFactura());
					else 
						var.getPojoFactura().setId(this.ifzRegGastosDet.save(var.getPojoFactura()));
					// Impuestos
					if (! var.getListImpuestos().isEmpty()) {
						for (PagosGastosDetImpuestosExt var2 : var.getListImpuestos()) {
							var2.setIdPagosGastosDet(var.getPojoFactura());
							if (var2.getId() == null || var2.getId() <= 0L)
								var2.setId(this.ifzDesgloImpto.save(var2));
							else
								this.ifzDesgloImpto.update(var2);
						}
					}
					// Retenciones
					if (! var.getListRetenciones().isEmpty()) {
						for (PagosGastosDetImpuestosExt var2 : var.getListRetenciones()) {
							var2.setIdPagosGastosDet(var.getPojoFactura());
							if (var2.getId() == null || var2.getId() <= 0L)
								var2.setId(this.ifzDesgloImpto.save(var2));
							else
								this.ifzDesgloImpto.update(var2);
						}
					}
				}
			}

			// Borramos facturas previamente eliminadas
			if (this.listFacturaConImpuestosEliminadas != null && ! this.listFacturaConImpuestosEliminadas.isEmpty()) {
				getFTPData();
				
				for (FacturaImpuestos var : this.listFacturaConImpuestosEliminadas) {
					// borramos XML (documento cargado) si corresponde
					if (var.getPojoFactura().getIdCfdi() != null && var.getPojoFactura().getIdCfdi() > 0L) {
						borrarXML = true;
						this.facturaId = var.getPojoFactura().getIdCfdi();
						for (FacturaImpuestos item : this.listFacturaConImpuestos) {
							if (item.getPojoFactura().getIdCfdi() != null && item.getPojoFactura().getIdCfdi() > 0L && item.getPojoFactura().getIdCfdi().longValue() == this.facturaId.longValue()) {
								borrarXML = false;
								break;
							}
						}
						
						if (borrarXML) {
							// Lo elimino de la BD
							log.info("Eliminando XML " + this.facturaId);
				    		this.ifzRegGastosDet.eliminarFactura(this.facturaId);
							
							// Lo elimino fisicamente del servidor
							fileName = this.prefijoFacturas + var.getPojoFactura().getIdCfdi() + ".xml";
							this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
							if (! this.ifzFtp.delArchivo(this.ftpDigitalizacionRuta + fileName))
								log.warn("ERROR FTP - No se pudo Borrar la factura del servidor, ID: " + this.facturaId);
						}
						
						// Borro detalle (factura)
						this.ifzRegGastosDet.delete(var.getPojoFactura().getId());
						this.facturaId = 0L;
					} else {
						// Borro detalle (factura)
						this.ifzRegGastosDet.delete(var.getPojoFactura().getId());
					}
				}
				
				this.listFacturaConImpuestosEliminadas.clear();
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			mensajeTransaccion();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar el Registro de Egreso", e);
		}
   	}
   	
	public void eliminar() {
		try {
			if (this.listFacturaConImpuestosEliminadas == null)
				this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.listFacturaConImpuestosEliminadas.add(this.reg);
			this.listFacturaConImpuestos.remove(this.reg);
			actualizaTotalFaturas();					
		} catch (Exception e) { 
			control("Ocurrio un problema al intentar eliminar la Factura indicada", e);
		}
	}

	public void reporte() {
   		try {
			/*listaParam = new HashMap<String, String>();
			if (this.tipoReporte == 1) {
				listaParam.put("folio", this.pojoGtosComp.getNoCheque());
				listaParam.put("banco_id", this.pojoGtosComp.getIdCuentaOrigen().getId());
				datosRep = ifzReporte.ejecutaReporte(listaParam, "cheque.jasper");
			}else{
				listaParam.put("gasto_id", this.pojoGtosComp.getPagosGastosId().intValue());
				datosRep = ifzReporte.generaReporte("registro_pago.jasper", listaParam);
			}
			
			httpSession.setAttribute("reporte", datosRep);
			httpSession.setAttribute("tipoReporte", "pdf");
			this.resOperacion = "";	*/		
		} catch (Exception e) {
			control("Ocurrio un problema al lanzar el reporte de Registro de Gasto", e);
		}
	}

   	public void cargarSucursales() {
   		try {
			this.listSucursales = new ArrayList<Sucursal>();
			this.listSucursalesItems = new ArrayList<SelectItem>();
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSucursales = this.ifzSucursal.findAll(); 
			if (this.listSucursales != null && ! this.listSucursales.isEmpty()) {
				for (Sucursal var : this.listSucursales)
					this.listSucursalesItems.add(new SelectItem(var.getId(), var.getSucursal()));
			}
		} catch (Exception e) {
			log.error("error en cargarSucursales", e);
		}
   	}
   	
   	public void cargarFormasDePago() {
		try {
			this.listFormasPago = new ArrayList<ConValores>();
			this.listFormasPagoItems = new ArrayList<SelectItem>();
			if (this.pojoGpoValFormasPago == null || this.pojoGpoValFormasPago.getId() <= 0L)
				return;

			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listFormasPago = this.ifzConValores.findAll(this.pojoGpoValFormasPago);
			if (this.listFormasPago != null && ! this.listFormasPago.isEmpty()) {
				for (ConValores fp : this.listFormasPago)
					this.listFormasPagoItems.add(new SelectItem(fp.getValor(), fp.getDescripcion()));
			}
		} catch (Exception e) {
			log.error("error en cargarFormasDePago", e);
		}
	}

   	public void cargarCuentas() {
   		try {
			this.listCuentas = new ArrayList<CuentaBancaria>();
			this.listCuentasItems = new ArrayList<SelectItem>();
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCuentas = this.ifzCtas.findLikeClaveNombreCuenta("", 0, this.sucursalesVisibles, null);
			if (this.listCuentas != null && ! this.listCuentas.isEmpty()) {
				for (CuentaBancaria var : this.listCuentas)
					this.listCuentasItems.add(new SelectItem(var.getId() + " - " + var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta(), var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta()));
			}
		} catch (Exception e) {
			log.error("error en cargarCuentas", e);
		}
   	}
   	
	public void evaluaOperacion() {
		try {
			this.reqReferencia = true;
			/*
			 * Eliminamos el siguiente codigo, siempre debe pedir referencia
			 *
			this.reqReferencia = "transferencia".equals(this.operacion.trim().toLowerCase()) ? true : false; //"T".equals(this.operacion);
			this.reqReferencia = "cheque".equals(this.operacion.trim().toLowerCase()) ? true : false; //"C".equals(this.operacion);
			this.reqReferencia = "spei".equals(this.operacion.trim().toLowerCase()) ? true : false; //"S".equals(this.operacion);
			//this.esTransferencia = "transferencia".equals(this.operacion.trim().toLowerCase()) ? true : false; //"T".equals(this.operacion);
			//this.esCheque = "cheque".equals(this.operacion.trim().toLowerCase()) ? true : false; //"C".equals(this.operacion);
			//this.esSpei = "spei".equals(this.operacion.trim().toLowerCase()) ? true : false; //"S".equals(this.operacion);
			*/
			this.mensaje = "";
			this.terceros = "";
			this.pojoRegistroGasto.setNoCheque(0);
			this.pojoRegistroGasto.setFolioAutorizacion("");
			this.beneficiario = "";
		} catch (Exception e) {
			this.mensaje = this.mensajesInf.get(-1);
			log.error("error en evaluaOperacion", e);
		}
	}

   	public void evaluaCancelar() {
		try {
	   		control();
			this.pojoRegistroGasto = this.ifzRegGastos.findExtById(this.pojoRegistroGastoMain);
			if (this.pojoRegistroGasto == null) {
	   			log.info("RegistroGasto ... No selecciono ningun registro");
	   			control(-1, "No selecciono ningun registro");
				return;
			}
			
	   		if (this.pojoRegistroGasto.getEstatus().equals("X"))
	   			control(14, mensajesInf.get(14));
		} catch (Exception e) {
			control("Ocurrio un problema al intentar lanzar el Evento para Contabilizar", e);
		}
   	}

   	public void buscarRet() {
   		try {
   			this.mensaje = "";
   			this.listRetEncontradas = this.ifzConValores.findLikeByProperty(this.campoBusqRet, this.valBusqRet, 5);
   			if (this.listRetEncontradas.isEmpty() || this.listRetEncontradas == null)
   				this.mensaje = this.mensajesInf.get(13);
   		} catch (Exception e) {
   			log.error("Error en metodo buscarRet", e);
   			this.mensaje = mensajesInf.get(-1);
   		}
   	}
   	
	public void mensajeTransaccion() {
		try {
			// Enviamos mensaje a cola de transacciones
			log.info("Enviando Transaccion ... ");
			this.ifzRegGastos.contabilizador(this.pojoRegistroGastoMain);
			log.info("Transaccion enviada");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar lanzar el Evento para Contabilizar", e);
		}
	}
	
   	public void cambioBeneficiario() {
		try {
			this.beneficiario = "";
		} catch (Exception e) {			
			log.error("error en cambioBeneficiario", e);
		}
	}
   	
   	public String validaSaldoCuentaPropios() {
		try {
			control();
			if (this.totalFacturasReportadas == 0 && this.listFacturaConImpuestos.isEmpty()) {
				control(12, this.mensajesInf.get(12));
				return "ERROR";
			}
			
			/* 
			 * VALIDACION TEMPORALMENTE DESACTIVADA: ASI LO PIDIO EL JEFE
			if (this.pojoCtas.getInstitucionBancaria().getId() != 0L) {
				if (! ifzCtas.haySaldoSuficiente(totalFacturasReportadas, this.pojoCtas.getId())) {
					control(true, 15, this.mensajesInf.get(15));
					return "ERROR";
				}
			}*/
		} catch (Exception e) {
			control("Ocurrio un problema al validar el Saldo del Banco", e);
			return "ERROR";
		}
		
		return "OK";
	}

   	public void eliminarImpuestoDesglosado() {
   		double sumImpuestos = 0;
   		double sumRetenciones = 0;
   		
		try {
			this.lisImpuestosEliminados.add(this.pojoDesgloImpto);
			this.listDesgloseImpuestos.remove(this.pojoDesgloImpto);
			this.subtotal = this.montoTotalFactura;
			this.totalImpuestos = sumImpuestos;
			this.totalRetenciones = sumRetenciones;
			this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
			
			if (! this.listDesgloseImpuestos.isEmpty()) {			
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(), 2);
					else	
						if ("1".equals(var.getIdImpuesto().getAtributo4()))
							sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(), 2);
				}

				this.subtotal = this.montoTotalFactura - (sumImpuestos - sumRetenciones);
				this.totalImpuestos = sumImpuestos;
				this.totalRetenciones = sumRetenciones;
				this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
				
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("0".equals(var.getIdImpuesto().getAtributo4()) && !"AC".equals(var.getIdImpuesto().getTipoCuenta())) {
						var.setValor(BigDecimal.valueOf(Utilerias.redondear(Utilerias.redondear(this.subtotal * ((Double.valueOf(var.getIdImpuesto().getAtributo1())/ 100)),2),2)));
					}
				}
				
				sumImpuestos = 0D;
				sumRetenciones = 0D;
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(), 2);	
					else	
						sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(), 2);	
				}
				
				this.subtotal = this.montoTotalFactura - (sumImpuestos - sumRetenciones);
				this.totalImpuestos = sumImpuestos;
				this.totalRetenciones = sumRetenciones;
				this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
			} 
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Impuesto Desglosado", e);
		} finally {
			this.subtotal= Utilerias.redondear(this.subtotal, 2);
			this.totalImpuestos = Utilerias.redondear(this.totalImpuestos, 2);
			this.totalRetenciones = Utilerias.redondear(this.totalRetenciones, 2);
			this.totalPago = Utilerias.redondear(this.totalPago, 2);
		}
	}
   	
   	public void cambioMontoImpuesto() {
   		double sumImpuestos = 0;
   		double sumRetenciones = 0;
   		
		try {
			control();
			if (this.montoTotalFactura != 0) {
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getValor() != null) {
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
							sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(),2);
						else
							sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(),2);
					}
				}

				this.subtotal = this.montoTotalFactura - (sumImpuestos - sumRetenciones);
				this.totalImpuestos = sumImpuestos;
				this.totalRetenciones = sumRetenciones;
				this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al calcular el monto del Impuesto", e);
		} finally {
			this.subtotal= Utilerias.redondear(this.subtotal, 2);
			this.totalImpuestos = Utilerias.redondear(this.totalImpuestos, 2);
			this.totalRetenciones = Utilerias.redondear(this.totalRetenciones, 2);
			this.totalPago = Utilerias.redondear(this.totalPago, 2);
		}
	}
   	
   	public String actualizaTotalFaturas() {
		BigDecimal totalAux = BigDecimal.ZERO;
   		double monto = 0;
		
		try {
			control();
			this.totalFacturasReportadas = 0D;
			this.pojoRegistroGasto.setConFactura(0);
			this.pojoRegistroGasto.setSinFactura(0);
			if (! this.listFacturaConImpuestos.isEmpty()) {				
				for (FacturaImpuestos var: listFacturaConImpuestos) {
					if (var.getPojoFactura().getSubtotal() == null)
						var.getPojoFactura().setSubtotal(0D);
					if (var.getPojoFactura().getTotalImpuestos() == null)
						var.getPojoFactura().setTotalImpuestos(0D);
					if (var.getPojoFactura().getTotalRetenciones() == null)
						var.getPojoFactura().setTotalRetenciones(0D);
					
					this.totalFacturasReportadas = this.totalFacturasReportadas.doubleValue() + ((var.getPojoFactura().getSubtotal().doubleValue() + var.getPojoFactura().getTotalImpuestos().doubleValue())- var.getPojoFactura().getTotalRetenciones().doubleValue());
					totalAux = new BigDecimal(this.totalFacturasReportadas).setScale(2, BigDecimal.ROUND_HALF_EVEN);
					this.totalFacturasReportadas = totalAux.doubleValue(); //Utilerias.redondear(totalFacturasReportadas, 2);

					if (var.getPojoFactura().getFacturado() == 0) {
						monto = this.pojoRegistroGasto.getSinFactura();
						monto += (var.getPojoFactura().getSubtotal() + (var.getPojoFactura().getTotalImpuestos() - var.getPojoFactura().getTotalRetenciones()));
						monto = Utilerias.redondear(monto, 2);
						this.pojoRegistroGasto.setSinFactura(monto);
					} else if (var.getPojoFactura().getFacturado() == 1) {
						monto = this.pojoRegistroGasto.getConFactura();
						monto += (var.getPojoFactura().getSubtotal() + (var.getPojoFactura().getTotalImpuestos() - var.getPojoFactura().getTotalRetenciones()));
						monto = Utilerias.redondear(monto, 2);
						this.pojoRegistroGasto.setConFactura(monto);
					}
				}
			}
		} catch (Exception e) {
			this.mensaje = this.mensajesInf.get(-1);
			log.error("error en actualizaTotalFaturas", e);
			throw e;
		}
		
		return "OK";
	}
   	
   	public String validaGrupoImpuestos() {
		try {
			int contador =0;
			band=false;
			mensaje="";
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					contador = 0;
					for (PagosGastosDetImpuestosExt var2 : this.listDesgloseImpuestos) {
						if (! band)
							if (! "0".equals(var2.getIdImpuesto().getAtributo2())) //porque el cero es general esos pueden existir N impuestos al mimos tiempo
								if (var1.getIdImpuesto().getAtributo2().equals(var2.getIdImpuesto().getAtributo2())) {
									contador = contador + 1;
									if (contador == 2) {
										band=true;
										this.tipoMensaje=1;
										mensaje = this.mensajesInf.get(1);
										return "ERROR";
									}
								}	
					}					
				}
			}
		} catch (Exception e) {
			this.mensaje = this.mensajesInf.get(-1);
			log.error("error en validaGrupoImpuestos", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String validaMontoImpuestos() {
   		double porcentaje = 0;
   		
		try {
			this.band=false;
			this.tipoMensaje = 0;
			this.mensaje = "";
			
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					if (var1.getValor() == null || var1.getValor() == BigDecimal.ZERO) {
						porcentaje = Double.parseDouble(var1.getIdImpuesto().getAtributo1());
						if (porcentaje > 0) {
							this.band = true;
							this.tipoMensaje=5;
							this.mensaje = this.mensajesInf.get(5);
							return "ERROR";
						}
					}
				}
			}
		} catch (Exception e) {
			this.mensaje = this.mensajesInf.get(-1);
			log.error("error en validaMontoImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
   	public String validaMontoFactura() {
		try {
			band=false;
			this.tipoMensaje = 0;
			mensaje = "";
			
			if (this.montoTotalFactura <= 0) {
				band = true;
				this.tipoMensaje=6;
				mensaje = this.mensajesInf.get(6);
				return "ERROR";
			}
		} catch (Exception e) {
			this.mensaje = this.mensajesInf.get(-1);
			log.error("error en validaMontoFactura", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
	public String validaMontoFacturaContraTotalImpuestos() {
		Double sumaImpuestos = 0D;
		
		try {
			this.band=false;
			this.tipoMensaje = 0;
			this.mensaje = this.mensajesInf.get(0);
			
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					if (! "AC".equals(var1.getIdImpuesto().getTipoCuenta()))
						sumaImpuestos = sumaImpuestos + var1.getValor().doubleValue();
				}
			}
			
			if (sumaImpuestos > this.montoTotalFactura) {
				this.band = true;
				this.tipoMensaje=7;
				this.mensaje = this.mensajesInf.get(7);
				return "ERROR";
			}
		} catch (Exception e) {
			this.mensaje = this.mensajesInf.get(-1);
			log.error("error en validaMontoFacturaContraTotalImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
	
	public String cancelarCheques() {
   		/*try {
   			Respuesta resp = new Respuesta();
   			resOperacion=""; 			
   			resp = ifzGtosComp.salvar(this.pojoGtosComp, false,null);
			
			
			if (! "BIEN".equals(resp.getRespuesta()) && resp.getResultado() == -1) {
				
				encontroMismoGrupo = true;// esta variable termine usandola como bandera gral
				this.resOperacion = resp.getRespuesta();
				this.tipoMensaje = 8;
				
				//antes le mostraba el mensaje que me regresaba la funcion CANCELA_CHEQUES pero ahora solo le dire que se guardo		
				guardar();
				
			}
   		} catch (Exception e) {
   			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en cancelarCheques", e);
			return "ERROR";
		}*/
		
		return "OK";
   	}

	public String agregaRet() {
		Double mto_ret; 
		
		try {
			this.mensaje = "";
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getIdImpuesto().getId() == this.pojoNvaRet.getId()) {
						this.mensaje = mensajesInf.get(17);
						return "OK";
					}
				}
			}

			mto_ret = Utilerias.redondear((this.montoTotalFactura - this.totalImpuestos) * (Double.valueOf(this.pojoNvaRet.getAtributo1())/ 100),2);
			this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
			this.pojoDesgloImpto.setIdImpuesto(this.pojoNvaRet);
			this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
			this.totalRetenciones = Utilerias.redondear(this.totalRetenciones + mto_ret,2);
			this.pojoDesgloImpto.setValor(BigDecimal.valueOf(mto_ret));
			this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
			this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
			this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
			this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
		} catch (Exception e) {
			log.error("Error en metodo agregaRet",e);
		}
		return "OK";
	}

	public List<Persona> autoacompletaTerceros (Object obj) {
		try {
			this.listTerceros = this.ifzPersonas.findLikePersonas(obj.toString(), 20);
			return this.listTerceros;
		} catch (Exception e) {
			log.error("error en autoacompletaTerceros", e);
			return new ArrayList<Persona>();
		}
	}
	
	public String cambioTerceros() {
		Pattern pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		Matcher match = null;
		this.mensaje = "";
		
		try {
			if ("S".equals(this.operacion)) {
				match = pat.matcher(this.beneficiario);
				if (match.find()) {		
					this.pojoTerceros = this.ifzRegGastos.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findById(Long.valueOf(match.group(1)));
					if (this.pojoTerceros != null) {
						if (this.pojoTerceros.getBanco() != null)
							if (this.pojoTerceros.getClabeInterbancaria() != null && !"".equals(this.pojoTerceros.getClabeInterbancaria()))
								//if (this.pojoTerceros.getgetCuentaBancaria() != null && !"".equals(this.pojoTerceros.getCuentaBancaria()))
									//if ("F".equals(this.pojoTerceros.getPersonalidad()))
								if (this.pojoTerceros.getNombre() != null && !"".equals(this.pojoTerceros.getNombre()) && this.pojoTerceros.getPrimerApellido() != null && !"".equals(this.pojoTerceros.getPrimerApellido()))
									return "OK";
								else
									this.mensaje = "El nombre del Beneficiario no esta completo, verifique !!";
									/*else
										if (this.pojoTerceros.getNombre() != null && !"".equals(this.pojoTerceros.getNombre()))
											return "OK";
										else
											this.resOperacion = "El nombre del Beneficiario no esta completo, verifique !!";*/
								//else
									//this.resOperacion = "El Beneficiario no tiene capturada cuenta bancaria, verifique !!";
							else
								this.mensaje = "El Beneficiario no tiene capturada cuenta Clabe, verifique !!";
						else
							this.mensaje = "El Beneficiario no tiene capturado banco, verifique !!";
					}
				}
			}
		} catch (Exception e) {			
			log.error("error en cambioTerceros", e);
			return "ERROR";
		}

		return "OK";
	}
	
	public List<Persona> autoacompletaBeneficiario(Object obj) {
		try {
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listBeneficiarios = this.ifzPersonas.findLikeClaveNombre("nombre", obj.toString(), this.pojoGpoValPersonas, this.tipoPersona, 100, false);
			return this.listBeneficiarios;
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar los Beneficiarios", e);
			return new ArrayList<Persona>();
		}
	}
	
	public List<Persona> autoacompletaProveedor (Object obj) {
		try {
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listProveedores = this.ifzPersonas.findLikeProveedor(obj.toString(), this.pojoGpoValPersonas, "PROV", this.pojoConceptoGtos.getId(), 20);
			return this.listProveedores;
		} catch (Exception e) {
			log.error("error en autoacompletaProveedor", e);
			return new ArrayList<Persona>();
		}
	}
	
	public String GeneraListaProveedores () {
		Pattern pat = null;
		Matcher match = null;
		
		try {
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			//validando gasto para poder presentar  una lista de proveedores en base al gasto seleccionado
			match = pat.matcher(this.conceptoGasto);			
			if (match.find())							
				this.pojoConceptoGtos = this.ifzConValores.findById(Long.valueOf(match.group(1)));
		} catch (Exception e) {
			this.mensaje = this.mensajesInf.get(-1);
			log.error("error en GeneraListaProveedores", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	private boolean validaciones() {
		long idAuxiliar = 0L;

		if (this.listFacturaConImpuestos == null || this.listFacturaConImpuestos.isEmpty()) {
			// CANCELADA: Por orden de ADMIINISTRACION cancelamos la validacion de cero en el acumulado de facturas, aun debe haber al menos una factura
			//if ((this.totalFacturasReportadas == null || this.totalFacturasReportadas == 0) && this.listFacturaConImpuestos.isEmpty()) {
			log.info("ERROR: 2 - Debe indicar al menos una factura");
			control(2, this.mensajesInf.get(2));
			return false;
		}
		
		// CUENTA
		// -----------------------------------------------------------------------------------------------------
		idAuxiliar = 0;
		if (! validateMatch(this.cuenta, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)")) {
			log.info("ERROR: INTERNO - No selecciono una Cuenta");
			control(-1, "No selecciona una Cuenta");
			return false;
		}
		
		idAuxiliar = matchToId(this.cuenta, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		if (idAuxiliar > 0L)
			this.pojoCtas = this.ifzRegGastos.findCuentaBancariaById(idAuxiliar);
		if (this.pojoCtas == null || this.pojoCtas.getId() <= 0L) {
			log.info("ERROR: INTERNO - No se pudo recuperar la Cuenta de Origen");
			control(-1, "Ocurrio un problema al intentar recuperar la Cuenta seleccionada");
			return false;
		}

		// BENEFICIARIO
		// -----------------------------------------------------------------------------------------------------
		idAuxiliar = 0;
		if (! validateMatch(this.beneficiario, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)")) {
			log.info("ERROR: INTERNO - Debe indicar un Beneficiario");
			control(-1, "Debe indicar un Beneficiario");
			return false;
		} 
		idAuxiliar = matchToId(this.beneficiario, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		if (idAuxiliar > 0L) 
			this.pojoBeneficiarios = this.ifzRegGastos.findPersonaById(idAuxiliar, this.tipoPersona); 
		if (this.pojoBeneficiarios == null || this.pojoBeneficiarios.getId() <= 0L) {
			log.info("ERROR: INTERNO - No se pudo recuperar la Cuenta de Origen");
			control(-1, "Ocurrio un problema al intentar recuperar el Beneficiario seleccionado");
			return false;
		}
		
		if ("N".equals(this.tipoPersona)) {
			this.nombreBeneficiario = this.pojoBeneficiarios.getNombre();
		} else {
			this.nombreBeneficiario = this.pojoBeneficiarios.getPrimerNombre() + " " 
					+ ((this.pojoBeneficiarios.getSegundoNombre()   != null && ! "".equals(this.pojoBeneficiarios.getSegundoNombre().trim()))   ? this.pojoBeneficiarios.getSegundoNombre().trim()   : "") + " " 
					+ ((this.pojoBeneficiarios.getPrimerApellido()  != null && ! "".equals(this.pojoBeneficiarios.getPrimerApellido().trim()))  ? this.pojoBeneficiarios.getPrimerApellido().trim()  : "") + " " 
					+ ((this.pojoBeneficiarios.getSegundoApellido() != null && ! "".equals(this.pojoBeneficiarios.getSegundoApellido().trim())) ? this.pojoBeneficiarios.getSegundoApellido().trim() : "");
		}

		// TERCEROS ???
		// -----------------------------------------------------------------------------------------------------
		idAuxiliar = 0;
		if ("T".equals(this.operacion) || "S".equals(this.operacion)) {
			idAuxiliar = matchToId(this.beneficiario, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			if (idAuxiliar > 0L) 
				this.pojoTerceros = this.ifzRegGastos.findPersonaById(idAuxiliar, this.tipoPersona); 
		}
		
		if ("C".equals(this.operacion) && ! NumberUtils.isNumber(this.referenciaOperacion)) {
			log.info("ERROR INTERNO: Formato incorrecto para referencia de Cheque. Solo Numeros");
			control(-1, "Referencia no valida para Cheque, debe ser de solo Numeros");
			return false;
		}
		
		if (! "OK".equals(validaSaldoCuentaPropios()))
			return false;
		
		return true;
	}
	
	private boolean validateMatch(String target, String patern) {
		Pattern pat = null;
		Matcher match = null;

		try {
			pat = Pattern.compile(patern);
			match = pat.matcher(target);
			if (match.find())
				Long.valueOf(match.group(1));
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar el patron para recuperar el ID", e);
			return false;
		}
		
		return true;
	}
	
	private long matchToId(String target, String patern) {
		Pattern pat = null;
		Matcher match = null;

		try {
			pat = Pattern.compile(patern);
			match = pat.matcher(target);
			if (match.find())
				return Long.valueOf(match.group(1));
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar el patron para recuperar el ID", e);
		}
		
		return 0;
	}
	
	private List<PagosGastosDetImpuestosExt> copiaListas(List<PagosGastosDetImpuestosExt> fuente) {
   		List<PagosGastosDetImpuestosExt> resultado = new ArrayList<PagosGastosDetImpuestosExt>();
   		for (PagosGastosDetImpuestosExt pgi : fuente) 
   			resultado.add(((PagosGastosDetImpuestosExt) pgi.clone()));
   		return resultado;
   	}
   	
	private String getGeneraNombreProveedor(PersonaExt prov) {
		if (prov == null)
			return "";
		
		return prov.getId() + " - " + prov.getNombre() + " " 
			+ (prov.getPrimerApellido() != null ? prov.getPrimerApellido() : "") + " " 
			+ (prov.getSegundoApellido() != null ? prov.getSegundoApellido() : "");
	}

	private boolean validaRequest(String param, String refValue) {
		String valParam = "";
		
		if (! this.isDebug)
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		valParam = this.paramsRequest.get(param);
		if (NumberUtils.isNumber(valParam))
			return ("1".equals(valParam));
		else
			return (valParam.equals(refValue));
	}

	private boolean comprobarUsuario() {
		List<Empleado> listEmpleado = null;
		Long idEmpleado = null;
		
		try {
			if (this.loginManager == null)
				return false;
			
			// Detarminamos EMPLEADO de manera directa o por correo electronico
			idEmpleado = this.loginManager.getUsuarioResponsabilidad().getUsuario().getIdEmpleado();
			if (idEmpleado == null || idEmpleado <= 1L) {
				listEmpleado = this.ifzEmpleado.findByProperty("email", this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo(), false, false, "", 0);
				if (listEmpleado != null && listEmpleado.size() == 1)
					idEmpleado = listEmpleado.get(0).getId();
			}
			
			if (idEmpleado == null || idEmpleado <= 1L) {
				log.info("Usuario sin Empleado asociado");
				return false;
			}
			
			// Recuperamos empleado
			this.pojoEmpleadoUsuario = this.ifzEmpleado.findByIdExt(idEmpleado);
			if (this.pojoEmpleadoUsuario == null || this.pojoEmpleadoUsuario.getId() == null || this.pojoEmpleadoUsuario.getId() <= 0L){
				log.info("No se encontro Empleado asociado al Usuario");
				return false;
			}
			
			// Recuperamos SUCURSAL base del Empleado
			this.idSucursalSugerida = this.pojoEmpleadoUsuario.getSucursal().getId();
		} catch (Exception e) {
			log.error("Ocurrio un problema en CuentasPorPagar.RegistroGastosAction.comprobarUsuario()", e);
			return false;
		}
		
		return true;
	}
	
	private void control() {
		this.band = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		this.band = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = sw.toString();
		}
		
		log.error("\n\n" + this.usuario + " :: REGISTRO DE GASTOS :: " + this.tipoMensaje + " - " + mensaje, throwable);
	}

   	// ---------------------------------------------------------------------------
	// FACTURAS COMPROBADAS
   	// ---------------------------------------------------------------------------

	public void nuevaComprobacionSinFactura() {
		agregar();
		this.sinFactura = false;
	}
	
	public void nuevaComprobacionConFactura() {
		agregar();
	}
	
	public void agregar() {
		PersonaExt proveedor = null;
		Matcher match = null;
		Pattern pat = null;
		
		try {
			control();
			if (this.pojoRegistroGasto.getId() != null && this.pojoRegistroGasto.getId() > 0L) {
				if (! this.perfilPermiteEditar && ! validaRequest("DETEDIT", "1")) {
					log.info("ERROR 10 - " + this.mensajesInf.get(10));
					this.sinFactura = true;
					control(10, this.mensajesInf.get(10));
					return;
				}
			}
			
			reiniciaFactura();
			this.sinFactura = false;
			this.reg = new FacturaImpuestos();
			this.pojoRegistroGastoComprobado = new PagosGastosDetExt();
			this.pojoRegistroGastoComprobado.setFecha(Calendar.getInstance().getTime());
			this.pojoRegistroGastoComprobado.setReferencia("");
			this.facturaEmisor = "";
			this.montoTotalFactura = 0D;
			this.conceptoGasto = "";
			this.conceptoGastoPrevio = "";
			this.nombreProveedor = "";
			this.listDesgloseImpuestos = new ArrayList<>();
			this.listDesgloseRetenciones = new ArrayList<>();
			this.listRetEncontradas = new ArrayList<>();
			this.listRetencionesDelGasto = new ArrayList<>();
			desglosaImpuestos();
			
			proveedor = new PersonaExt();
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.beneficiario);
			if (match.find()) {
				proveedor = this.ifzRegGastos.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona);
				this.pojoRegistroGastoComprobado.setIdProveedor(proveedor);
				this.pojoRegistroGastoComprobado.setTipoPersonaProveedor(this.tipoPersona);
			}

			obrasBusqueda();
			ordenCompraBusqueda();
			control();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar agregar un nuevo Registro", e);
		}
	}

	public void editarFactura() {
		String facturaFolio = "";

		try {
			// Inicializaciones
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseRetenciones = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listRetencionesDelGasto = new ArrayList<GastosImpuestoExt>();
			
			// Recogemos datos de comprobacion
			reiniciaFactura();
			this.pojoRegistroGastoComprobado = new PagosGastosDetExt();
			this.pojoRegistroGastoComprobado = this.reg.getPojoFactura();
			if (this.pojoRegistroGastoComprobado == null) {
				control(-1, "Ocurrio un problema al obtener la Factura de la Comprobacion seleccionada");
				return;
			}
			
			if (this.pojoRegistroGastoComprobado.getIdObra() == null && this.pojoRegistroGastoComprobado.getIdOrdenCompra() != null)
				this.pojoRegistroGastoComprobado.setIdObra(this.ifzObras.findById(this.pojoRegistroGastoComprobado.getIdOrdenCompra().getIdObra()));
			
			// Recuperamos datos de Factura si corresponde
			if (this.pojoRegistroGastoComprobado.getIdCfdi() == null || this.pojoRegistroGastoComprobado.getIdCfdi() <= 0L)
				this.reg.getPojoFactura().setIdCfdi(0L);
			
			this.sinFactura = (this.pojoRegistroGastoComprobado.getFacturado() == 0);
			this.facturaId = this.pojoRegistroGastoComprobado.getIdCfdi();
			facturaFolio = this.pojoRegistroGastoComprobado.getFacturaFolio();
			this.facturaRfc = this.pojoRegistroGastoComprobado.getFacturaRfc();
			this.facturaEmisor = this.pojoRegistroGastoComprobado.getNombreProveedor();
			this.facturaSubtotal = 0D;
			if (! this.sinFactura)
				this.facturaSubtotal = this.pojoRegistroGastoComprobado.getSubtotal();
			this.subtotal = this.pojoRegistroGastoComprobado.getSubtotal();
			this.totalImpuestos = this.pojoRegistroGastoComprobado.getTotalImpuestos();
			this.totalRetenciones = this.pojoRegistroGastoComprobado.getTotalRetenciones();
			this.facturaTotal = this.pojoRegistroGastoComprobado.getTotal();
			this.facturaMoneda = "MXN";
			this.facturaTipoCambio = 1.0;
			if (this.facturaId != null && this.facturaId.longValue() > 0L) {
				facturaFolio = this.ifzRegGastosDet.getFacturaProperty(this.facturaId, "factura");
				this.facturaTipo = this.ifzRegGastosDet.getFacturaProperty(this.facturaId, "tipo");
				this.facturaRfc = this.ifzRegGastosDet.getFacturaProperty(this.facturaId, "rfcEmisor");
				this.facturaEmisor = this.ifzRegGastosDet.getFacturaProperty(this.facturaId, "nombre");
				this.facturaMoneda = this.ifzRegGastosDet.getFacturaProperty(this.facturaId, "moneda");
				this.facturaTipoCambio = this.ifzRegGastosDet.getFacturaTipoCambio(this.facturaId);
				this.facturaDescuento = this.ifzRegGastosDet.getFacturaDescuento(this.facturaId);
				//this.facturaSubtotal = this.ifzRegGastosDet.getFacturaSubtotal(this.facturaId);
				this.facturaTotal = this.ifzRegGastosDet.getFacturaTotal(this.facturaId);
				this.facturaValores = this.ifzRegGastosDet.facturaValores(this.facturaId);
			}
	
			this.facturaActualizar = false;
			if ((this.facturaEmisor == null || "".equals(this.facturaEmisor.trim())) || (facturaFolio == null || "".equals(facturaFolio.trim())))
				this.facturaActualizar = true;
			
			this.conceptoGasto = "";
			if (this.pojoRegistroGastoComprobado.getIdConcepto() != null && this.pojoRegistroGastoComprobado.getIdConcepto().getId() > 0L) {
				this.pojoConceptoGtos = this.pojoRegistroGastoComprobado.getIdConcepto();
				this.conceptoGasto = this.pojoConceptoGtos.getId() + " - " + this.pojoConceptoGtos.getDescripcion();
			}
			
			this.nombreProveedor = this.pojoRegistroGastoComprobado.getIdProveedor().getId() + " - " + this.pojoRegistroGastoComprobado.getIdProveedor().getNombre(); 
			if ("".equals(this.facturaEmisor.trim())) 
				this.nombreProveedor = this.facturaEmisor;
			else
				this.nombreProveedor = getGeneraNombreProveedor(this.pojoRegistroGastoComprobado.getIdProveedor());
			if (this.reg.getListImpuestos() != null && ! this.reg.getListImpuestos().isEmpty()) {
				this.listDesgloseImpuestos = copiaListas(this.reg.getListImpuestos());
				this.totalImpuestos = this.reg.getTotalImpuestos();
			}
			
			if (this.reg.getListRetenciones() != null && ! this.reg.getListRetenciones().isEmpty()) {
				this.listDesgloseRetenciones = copiaListas(this.reg.getListRetenciones());
				this.totalRetenciones = this.reg.getTotalRetenciones();
			}

			this.montoTotalFactura = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
			this.montoTotalFactura = Utilerias.redondear(this.montoTotalFactura, 2);
			this.totalPago = this.montoTotalFactura;
			this.conceptoGastoPrevio = this.conceptoGasto;
			if (this.facturaTipo != null && "E".equals(this.facturaTipo)) {
				this.subtotal *= -1;
				this.totalImpuestos *= -1;
				this.totalRetenciones *= -1;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al desglozar la Comprobacion seleccionada", e);
		} 
	}
	
	public void guardarFactura() {
		boolean encontro = false;			
		boolean  registroNuevo = false;
		String[] splitted = null;
		String value = "";
		// -------------------------------
		List<Persona> beneficiarios = null;
		
		try {
			control();
			log.info("Validando Factura");
			if (! validarFactura())
				return;

			log.info("Guardando Factura");
			if (this.pojoRegistroGastoComprobado.getTipoPersonaProveedor() == null || "".equals(this.pojoRegistroGastoComprobado.getTipoPersonaProveedor().trim()))
				this.pojoRegistroGastoComprobado.setTipoPersonaProveedor(this.pojoRegistroGastoComprobado.getIdProveedor().getTipoPersona() == 2L ? "N" : "P");
			
			if (this.facturaActualizar) {
				this.facturaActualizar = false;
				this.ifzRegGastosDet.setFacturaProperty(this.facturaId, "nombre", this.facturaEmisor);
				
				if (this.pojoRegistroGastoComprobado.getReferencia().contains("-")) {
					splitted = this.pojoRegistroGastoComprobado.getReferencia().split("-");

					if (splitted.length > 2) {
						for (int i = 0; i <= (splitted.length - 2); i++)
							value += splitted[i] + "-";
						this.ifzRegGastosDet.setFacturaProperty(this.facturaId, "serie", value);
						value = splitted[splitted.length - 1];
						this.ifzRegGastosDet.setFacturaProperty(this.facturaId, "folio", value);
					} else {
						value = splitted[0];
						this.ifzRegGastosDet.setFacturaProperty(this.facturaId, "serie", value);
						value = splitted[1];
						this.ifzRegGastosDet.setFacturaProperty(this.facturaId, "folio", value);
					}
				} else {
					this.ifzRegGastosDet.setFacturaProperty(this.facturaId, "serie", this.pojoRegistroGastoComprobado.getReferencia());
				}
			}
			
			if (this.facturaId == null)
				analizarArchivo();
			
			control();
			this.pojoRegistroGastoComprobado.setIdConcepto(this.pojoConceptoGtos);
			if (this.pojoRegistroGastoComprobado.getIdObra() == null)
				this.pojoRegistroGastoComprobado.setIdObra(new Obra());
			if (this.pojoRegistroGastoComprobado.getIdOrdenCompra() == null)
				this.pojoRegistroGastoComprobado.setIdOrdenCompra(new OrdenCompra());
			this.pojoRegistroGastoComprobado.setIdCfdi(this.facturaId);
			this.pojoRegistroGastoComprobado.setFacturaRfc(this.facturaRfc);
			this.pojoRegistroGastoComprobado.setNombreProveedor(this.facturaEmisor);
			this.pojoRegistroGastoComprobado.setModificadoPor(this.usuarioId);
			this.pojoRegistroGastoComprobado.setFechaModificacion(Calendar.getInstance().getTime());
			if (this.pojoRegistroGastoComprobado.getId() == null || this.pojoRegistroGastoComprobado.getId() <= 0L) {
				this.pojoRegistroGastoComprobado.setCreadoPor(this.usuarioId);
				this.pojoRegistroGastoComprobado.setFechaCreacion(Calendar.getInstance().getTime());
				registroNuevo = true;
			}

			// Comprobamos si es un egreso y asignamos en negativo
			if (this.facturaTipo != null && ("E".equals(this.facturaTipo) || "EGRESO".equals(this.facturaTipo.trim().toLowerCase()))) {
				this.subtotal *= -1;
				this.totalImpuestos *= -1;
				this.totalRetenciones *= -1;
			}
			
			this.pojoRegistroGastoComprobado.setSubtotal(this.subtotal);
			this.pojoRegistroGastoComprobado.setTotalImpuestos(this.totalImpuestos);
			this.pojoRegistroGastoComprobado.setTotalRetenciones(this.totalRetenciones);

			log.info("Asignamos ID Registro Gasto");
			this.pojoRegistroGastoComprobado.setIdPagosGastos(this.pojoRegistroGasto); // asignar papa //this.pojoGtosComp.getId();
			
			if (this.pojoRegistroGasto.getId() == null || this.pojoRegistroGasto.getId() <= 0L) {
				//si el gasto ya existe guardo las facturas creadas, sino las meto a una lista
				// y las guardare hasta que creen un gasto. Ya que sí el gasto no existe no puedo grabar 
				//facturas hasta que me serciore que creen un gasto. Siempre que el gasto no tenga llave 
				//la factura tampoko tendra llave, por eso no pregunto si es nueva 	
				
				this.pojoRegistroGastoComprobado.setCreadoPor(this.usuarioId);
				this.pojoRegistroGastoComprobado.setFechaCreacion(Calendar.getInstance().getTime());				
				   	   						
				if (! this.listDesgloseImpuestos.isEmpty()) {
					for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos)
						var.setId(this.pojoRegistroGastoComprobado.getId());
				}
				
   				if (! this.listDesgloseRetenciones.isEmpty()) {
					for (PagosGastosDetImpuestosExt var : this.listDesgloseRetenciones)
						var.setId(this.pojoRegistroGastoComprobado.getId());
				} 	
			} else {
				// si el gasto existe ya puedo grabar facturas 
				if (registroNuevo) {
					this.pojoRegistroGastoComprobado.setCreadoPor(this.usuarioId);
					this.pojoRegistroGastoComprobado.setFechaCreacion(Calendar.getInstance().getTime());

					log.info("Guardo factura en BD");
					this.pojoRegistroGastoComprobado.setId(this.ifzRegGastosDet.save(this.pojoRegistroGastoComprobado));
					log.info("Factura guardada");
					
					if (! this.listDesgloseImpuestos.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
							var.setId(this.pojoRegistroGastoComprobado.getId());
							var.setId(this.ifzDesgloImpto.save(var));
						} 
					}

   					if (! this.listDesgloseRetenciones.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseRetenciones) {
							var.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
							var.setId(this.ifzDesgloImpto.save(var));
						} 
   					}
				} else {
					log.info("Actualizo factura en BD");
					this.ifzRegGastosDet.update(this.pojoRegistroGastoComprobado);
					log.info("Factura actualizada");
					   	   						
					if (! this.listDesgloseImpuestos.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
							var.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
							
							if (var.getId() == null)
								var.setId(this.ifzDesgloImpto.save(var));
							else
								this.ifzDesgloImpto.update(var);
						}
					}
					
					if (! this.listDesgloseRetenciones.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseRetenciones) {
							var.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);

							if (var.getId() == 0L)
								var.setId(this.ifzDesgloImpto.save(var));
							else
								this.ifzDesgloImpto.update(var);
						}
					}
				}
			}
			
			// eliminando los impuestos anteriormente eliminados del listado
			if (! this.lisImpuestosEliminados.isEmpty()) {
				for (PagosGastosDetImpuestosExt var : this.lisImpuestosEliminados) {
					if (var.getId().longValue() > 0L)
						this.ifzDesgloImpto.delete(var.getId());
				}
				
				// Limpiamos lista
				this.lisImpuestosEliminados.clear();
			}
			
			this.reg.setPojoFactura(this.pojoRegistroGastoComprobado);
			this.reg.setListImpuestos(copiaListas(this.listDesgloseImpuestos));
			this.reg.setListRetenciones(copiaListas(this.listDesgloseRetenciones));
			  					
			encontro = false;
			if (! this.listFacturaConImpuestos.isEmpty()) {
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					if (var.equals(this.reg)) {
						var = this.reg;
						encontro = true;
						break;
					}
				}
			}
			   					
			if (! encontro)
				this.listFacturaConImpuestos.add(this.reg);
			
			actualizaTotalFaturas();
			this.facturaDescartada = false;
			this.facturaId = null;
			control(false, -1, "Comprobacion agregada", null);
   		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Factura", e);
			return;
		}
		
		// Encontramos Beneficiario si corresponde y lo asignamos al Registro de Gasto
		if (this.beneficiarioAsignado == null || "".equals(this.beneficiarioAsignado.trim())) {
			if (this.beneficiarioAsignado != null && ! this.beneficiarioAsignado.equals(this.facturaRfc)) {
				this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
				beneficiarios = this.ifzPersonas.findLikeClaveNombre("rfc", this.facturaRfc, this.pojoGpoValPersonas, this.pojoRegistroGastoComprobado.getTipoPersonaProveedor(), 10, false);
				if (beneficiarios != null && ! beneficiarios.isEmpty()) {
					for (Persona item : beneficiarios) {
						this.beneficiario = item.getId() + " - " + item.getNombre();
						this.tipoPersona = this.pojoRegistroGastoComprobado.getTipoPersonaProveedor();
						this.beneficiarioAsignado = this.facturaRfc;
						break;
					}
				}
			}
		}
   	} 	
   	
   	public void cancelarRegistroGasto() {
   		Respuesta rep = new Respuesta();
   		
		try {
			control();
			this.ifzRegGastos.setInfoSesion(this.loginManager.getInfoSesion());
			rep = this.ifzRegGastos.cancelacion(this.pojoRegistroGasto, Calendar.getInstance().getTime());
			if (rep.getResultado() != 0) {
				control(-1, rep.getRespuesta());
				return;
			}
			
			for (PagosGastos var : this.listRegistroGastos) {
				if (var.getId().longValue() == this.pojoRegistroGasto.getId().longValue()) {
					var.setEstatus("X");
					break;
				}
			}
			/*if (rep.getResultado() == 0) {
				this.pojoRegistroGasto = (PagosGastosExt) rep.getObjeto();
				this.mensaje = "";
				
				for (PagosGastos var : this.listRegistroGastos) {
					if (var.getId().equals(this.pojoRegistroGasto.getId())) {
						var.setEstatus("X");
						break;
					}						
				}
				
				if (this.perfilRequiereObra == null && ! "S".equals(this.perfilRequiereObra)) { // TO DO: Analizar condicion
					List<PagosGastosDet> detalles = this.ifzRegGastosDet.findByProperty("pagosGastosId", this.pojoRegistroGasto);
					List<ProgPagosDetalle> lista = null;
					if (detalles != null && ! detalles.isEmpty()) {
						for (PagosGastosDet var: detalles) {
							Double montoValue = ((var.getSubtotal() + var.getTotalImpuestos())- var.getTotalRetenciones());
							Long conceptoValue = var.getIdConcepto(); //var.getConceptoId().getValorId();
							lista = this.ifzProPagDet.findByMontoConceptoEstatus(montoValue, conceptoValue, "E");
							if (lista != null) {
								lista.get(0).setEstatus("G");
								lista.get(0).setModificadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
								lista.get(0).setFechaModificacion(Calendar.getInstance().getTime());
								this.ifzProPagDet.update(lista.get(0));
							}
						}
					}
				}
			} else
				this.mensaje = rep.getRespuesta();*/
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Cancelar el Registro de Gasto.", e);
		}
	}
   	
   	public void evaluaEditar() {
   		try {
			control();
   			this.sinFactura = false;
			if (this.reg.getPojoFactura().getId() != null && this.reg.getPojoFactura().getId() > 0L) {
				if (! this.perfilPermiteEditar && ! validaRequest("DETEDIT", "1")) { 
					this.sinFactura = true;
		   			log.info("ERROR 4: No se permite editar las facturas");
		   			control(4, this.mensajesInf.get(4));
					return;
		   		}
			}
			
			editarFactura();
		} catch (Exception e) {
			control("Ocurrio un problema al preparar el registro para Editar.", e);
		}
   	}
   	
   	public void evaluaEliminar() {
   		try {
   			control();
			this.sinFactura = false;
			if (this.reg.getPojoFactura().getId() != null) {
				if (! this.perfilPermiteEditar && ! validaRequest("DETEDIT", "1")) { //! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					this.sinFactura = true;
		   			log.info("ERROR 3: No se permite eliminar las facturas");
		   			control(3, this.mensajesInf.get(3));
					return;
		   		}
			} 
		} catch (Exception e) {
			control("Ocurrio un problema al intentar preparar el Registro para Eliminar", e);
		}
   	}

   	public void recalcularImpuestos() {
   		this.facturaSubtotal = 0D;
   		desglosaImpuestos();
   	}
   	
	public void desglosaImpuestos() {
   		List<PagosGastosDetImpuestosExt> listDesgloseImpuestos_tmp = null;
   		double porcentajeImpuesto = 0;
   		double sumImpuestos = 0;
   		double sumRetenciones = 0;
   		double importeImpuesto = 0;
   		long idConcepto = 0;
   		// ----------------------------------------------------------------
   		double montoBaseImpuestos = 0;
   		boolean baseSubtotal = false;
   			
   		try {
			control();
			if (this.montoTotalFactura == null)
				this.montoTotalFactura = 0D;
			if (this.conceptoGasto == null)
				this.conceptoGasto = "";
			if (this.conceptoGastoPrevio == null)
				this.conceptoGastoPrevio = "";
			this.subtotal = 0D;
			this.totalImpuestos = sumImpuestos;
			this.totalRetenciones = sumRetenciones;
			this.totalPago = this.montoTotalFactura;
			montoBaseImpuestos = (this.facturaSubtotal > 0 ? this.facturaSubtotal : this.montoTotalFactura);
			baseSubtotal = (this.facturaSubtotal > 0);

   			if (this.sinFactura) {
				this.listDesgloseImpuestos.clear();
				this.subtotal = this.montoTotalFactura;
   			}
   			
   			if ("".equals(this.conceptoGasto.trim()) || this.montoTotalFactura <= 0D) {
   				if (! this.listDesgloseImpuestos.isEmpty())
					this.lisImpuestosEliminados.addAll(this.listDesgloseImpuestos);
				this.listDesgloseImpuestos.clear();
				this.subtotal = this.montoTotalFactura;
				this.totalPago = this.montoTotalFactura;
				return;
			}

   			// Validamos Concepto de Gastos
			if (! this.conceptoGastoPrevio.equals(this.conceptoGasto)) {
				this.conceptoGastoPrevio = this.conceptoGasto;
				if (! this.listDesgloseImpuestos.isEmpty())
					this.lisImpuestosEliminados.addAll(this.listDesgloseImpuestos);
				this.listDesgloseImpuestos.clear();
				
				idConcepto = matchToId(this.conceptoGasto, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
				if (idConcepto <= 0L) {
					this.listDesgloseImpuestos.clear();
					return;
				}

				// Recuperamos Concepto de Gastos
				this.pojoConceptoGtos = this.ifzConValores.findById(idConcepto);
				if (this.pojoConceptoGtos == null || this.pojoConceptoGtos.getId() <= 0L) {
					if (! this.listDesgloseImpuestos.isEmpty())
						this.lisImpuestosEliminados.addAll(this.listDesgloseImpuestos);
					this.listDesgloseImpuestos.clear();
					return;
				}
			}
			
			// Impuestos del Gasto (Egreso)
			this.listImpuestosDelGasto = this.ifzGastoImpuesto.findAllExt(this.pojoConceptoGtos.getId(), "");
			if (this.listImpuestosDelGasto != null && ! this.listImpuestosDelGasto.isEmpty()) {
				if (this.listDesgloseImpuestos == null || this.listDesgloseImpuestos.isEmpty()) {
					for (GastosImpuestoExt var : this.listImpuestosDelGasto) {
						this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
						this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
						this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
						this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
						this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());

						importeImpuesto = 0D;
						porcentajeImpuesto = Double.parseDouble(var.getImpuestoId().getAtributo1().trim());
						if (porcentajeImpuesto > 0) {
							porcentajeImpuesto = (porcentajeImpuesto / 100); 
							if (baseSubtotal) 
								importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto);  
							else 
								importeImpuesto = montoBaseImpuestos - (montoBaseImpuestos / (1 + porcentajeImpuesto));
							importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
	
							if ("AC".equals(var.getImpuestoId().getTipoCuenta())) {
								sumRetenciones += importeImpuesto;
								sumRetenciones = Utilerias.redondear(sumRetenciones, 2); 
							} else if ("DE".equals(var.getImpuestoId().getTipoCuenta())) {
								sumImpuestos += importeImpuesto;
								sumImpuestos = Utilerias.redondear(sumImpuestos, 2); 
							}
						}

						this.pojoDesgloImpto.setValor(new BigDecimal(importeImpuesto));	
						this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
					}

					this.facturaSubtotal = 0D;
					this.subtotal = montoBaseImpuestos - (sumImpuestos - sumRetenciones);
					if (baseSubtotal) 
						this.subtotal = montoBaseImpuestos;
					this.totalImpuestos = sumImpuestos;
					this.totalRetenciones = sumRetenciones;
					this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
					return;
				}
				
				// la lista si tenia elementos y hay que hacer el desglose imptos en base a los imptos que tiene actualmente la lista
				listDesgloseImpuestos_tmp = new ArrayList<PagosGastosDetImpuestosExt>();
				listDesgloseImpuestos_tmp.addAll(this.listDesgloseImpuestos);					
				this.listDesgloseImpuestos.clear();
				
				// Impuestos y Retenciones
				for (PagosGastosDetImpuestosExt var : listDesgloseImpuestos_tmp) {	
					importeImpuesto = 0;
					porcentajeImpuesto = Double.parseDouble(var.getIdImpuesto().getAtributo1().trim());
					if (porcentajeImpuesto > 0) {
						porcentajeImpuesto = (porcentajeImpuesto / 100); 
						if (baseSubtotal) 
							importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto);  
						else 
							importeImpuesto = montoBaseImpuestos - (montoBaseImpuestos / (1 + porcentajeImpuesto));
						importeImpuesto = Utilerias.redondear(importeImpuesto, 2);
						
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta())) {
							sumRetenciones += importeImpuesto;
							sumRetenciones = Utilerias.redondear(sumRetenciones, 2); 
						} else if ("DE".equals(var.getIdImpuesto().getTipoCuenta())) {
							sumImpuestos += importeImpuesto;
							sumImpuestos = Utilerias.redondear(sumImpuestos, 2); 
						}
					}

					var.setValor(new BigDecimal(importeImpuesto));
					this.listDesgloseImpuestos.add(var);
				}

				this.facturaSubtotal = 0D;
				this.subtotal = montoBaseImpuestos - (sumImpuestos - sumRetenciones);
				if (baseSubtotal) 
					this.subtotal = montoBaseImpuestos;
				this.totalImpuestos = sumImpuestos;
				this.totalRetenciones = sumRetenciones;
				this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
			}
			
			/*// Impuestos
			this.listImpuestosDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","DE", this.pojoConceptoGtos.getId());
			if (this.listImpuestosDelGasto != null && ! this.listImpuestosDelGasto.isEmpty()) {
				if (this.listDesgloseImpuestos.isEmpty()) {
					// Traslados
					for (GastosImpuestoExt var : this.listImpuestosDelGasto) {
						this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
						this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
						this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
						this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
						this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());

						importeImpuesto = 0D;
						porcentajeImpuesto = Double.parseDouble(var.getImpuestoId().getAtributo1().trim());
						if (porcentajeImpuesto <= 0) {
							this.pojoDesgloImpto.setValor(new BigDecimal(importeImpuesto));	
							this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
							continue;
						}
						
						porcentajeImpuesto = (porcentajeImpuesto / 100); 
						if (baseSubtotal) 
							importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto);  
						else 
							importeImpuesto = montoBaseImpuestos - (montoBaseImpuestos / (1 + porcentajeImpuesto));
						importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
						sumImpuestos += importeImpuesto; 
						sumImpuestos = Utilerias.redondear(sumImpuestos, 4);
						this.pojoDesgloImpto.setValor(new BigDecimal(importeImpuesto));	
						this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
					}
					
					// Retenciones
					this.listRetencionesDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","AC", this.pojoConceptoGtos.getId());
					if (! this.listRetencionesDelGasto.isEmpty()) {
						for (GastosImpuestoExt var : this.listRetencionesDelGasto) {
							this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
							this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
							this.pojoDesgloImpto.setValor(BigDecimal.ZERO);				
							this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
							this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
							this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());

							importeImpuesto = 0;
							porcentajeImpuesto = Double.parseDouble(var.getImpuestoId().getAtributo1().trim());
							if (porcentajeImpuesto <= 0) {
								this.pojoDesgloImpto.setValor(new BigDecimal(importeImpuesto));	
								this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
								continue;
							}

							porcentajeImpuesto = (porcentajeImpuesto / 100); 
							//importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto); 
							if (baseSubtotal) 
								importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto);  
							else 
								importeImpuesto = montoBaseImpuestos - (montoBaseImpuestos / (1 + porcentajeImpuesto));
							importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
							sumRetenciones += importeImpuesto;
							sumRetenciones = Utilerias.redondear(sumRetenciones, 4);
							this.pojoDesgloImpto.setValor(new BigDecimal(importeImpuesto));
							this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
						}
					}

					this.facturaSubtotal = 0D;
					this.subtotal = this.montoTotalFactura - (sumImpuestos - sumRetenciones);
					this.totalImpuestos = sumImpuestos;
					this.totalRetenciones = sumRetenciones;
					this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
					return;
				} 
				
				// la lista si tenia elementos y hay que hacer el desglose imptos en base a los imptos que tiene actualmente la lista
				listDesgloseImpuestos_tmp = new ArrayList<PagosGastosDetImpuestosExt>();
				listDesgloseImpuestos_tmp.addAll(this.listDesgloseImpuestos);					
				this.listDesgloseImpuestos.clear();
				
				// Impuestos y Retenciones
				for (PagosGastosDetImpuestosExt var : listDesgloseImpuestos_tmp) {	
					importeImpuesto = 0;
					porcentajeImpuesto = Double.parseDouble(var.getIdImpuesto().getAtributo1().trim());
					if (porcentajeImpuesto <= 0) {
						var.setValor(new BigDecimal(importeImpuesto));
						this.listDesgloseImpuestos.add(var);
						continue;
					}
					
					porcentajeImpuesto = (porcentajeImpuesto / 100); 
					if (baseSubtotal) 
						importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto);  
					else 
						importeImpuesto = montoBaseImpuestos - (montoBaseImpuestos / (1 + porcentajeImpuesto));
					//importeImpuesto = this.montoTotalFactura / (1 + porcentajeImpuesto);
					importeImpuesto = (importeImpuesto * porcentajeImpuesto);
					importeImpuesto = Utilerias.redondear(importeImpuesto, 2);
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta())) {
						sumRetenciones += importeImpuesto;
						sumRetenciones = Utilerias.redondear(sumRetenciones, 2); 
					} else if ("DE".equals(var.getIdImpuesto().getTipoCuenta())) {
						sumImpuestos += importeImpuesto;
						sumImpuestos = Utilerias.redondear(sumImpuestos, 2); 
					}

					var.setValor(new BigDecimal(importeImpuesto));
					this.listDesgloseImpuestos.add(var);
				}

				this.facturaSubtotal = 0D;
				this.subtotal = this.montoTotalFactura - (sumImpuestos - sumRetenciones);
				this.totalImpuestos = sumImpuestos;
				this.totalRetenciones = sumRetenciones;
				this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
			} */
		} catch (Exception e) {
			control("Ocurrio un problema al desglozar los impuestos", e);
		} finally {
   			if (! "".equals(this.conceptoGasto.trim()) && this.montoTotalFactura > 0D) {
   				if (this.subtotal <= 0 && this.montoTotalFactura > 0D)
   					this.subtotal = this.montoTotalFactura;
				this.subtotal = Utilerias.redondear(this.subtotal, 2);
				this.totalImpuestos = Utilerias.redondear(this.totalImpuestos, 2);
				this.totalRetenciones = Utilerias.redondear(this.totalRetenciones, 2);
				this.totalPago = Utilerias.redondear(this.totalPago, 2);
				
				log.info("SUBTOTAL    : " + this.subtotal);
				log.info("IMPUESTOS   : " + this.totalImpuestos);
				log.info("RETENCIONES : " + this.totalRetenciones);
				log.info("TOTAL       : " + this.totalPago);
   			}
		}
	}
   	
   	private boolean validarFactura() {
		long idAuxiliar = 0L;
		long idStored = 0L;

		this.pojoRegistroGastoComprobado.setFacturado((this.sinFactura ? 0 : 1));
		if (this.pojoRegistroGastoComprobado.getFacturado() == 1 && (this.facturaId == null || this.facturaId <= 0L))
			this.pojoRegistroGastoComprobado.setFacturado(0);

		// DESHABILITADA: Por orden de ADMINISTRACION permitimos negativos y ceros
		/*if (this.facturaTotal > 0) {
			if (! "OK".equals(validaMontoFactura()))
				return false;
			
			if (! "OK".equals(validaMontoImpuestos()))
				return false;
			
			if (! "OK".equals(validaGrupoImpuestos()))
				return false;
		
			if (! "OK".equals(validaMontoFacturaContraTotalImpuestos()))
				return false;
			
			if (this.totalRetenciones <= 0 && ! this.listRetencionesDelGasto.isEmpty()) {
				control(16, this.mensajesInf.get(16));
				return false;
			}
		}*/
		
		// GASTO (Concepto)
		// -----------------------------------------------------------------------------------------------------
		if (this.pojoRegistroGastoComprobado != null && this.pojoRegistroGastoComprobado.getIdConcepto() != null && this.pojoRegistroGastoComprobado.getIdConcepto().getId() > 0L)
			idStored = this.pojoRegistroGastoComprobado.getIdConcepto().getId();
		
		if (! validateMatch(this.conceptoGasto, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)")) {
			log.info("ERROR: INTERNO - No selecciono ningun Gasto (Concepto)");
			control(-1, "No selecciono ningun Gasto (Concepto)");
			return false;
		} 
		
		idAuxiliar = matchToId(this.conceptoGasto, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		if (idAuxiliar > 0L && idAuxiliar != idStored)
			this.pojoConceptoGtos = this.ifzConValores.findById(idAuxiliar);

		// Documento
		// -----------------------------------------------------------------------------------------------------
		if (this.pojoRegistroGastoComprobado.getFacturado() == 0) {
			if (this.pojoRegistroGastoComprobado.getReferencia() == null || "".equals(this.pojoRegistroGastoComprobado.getReferencia())) {
				control(-1, "Indique una Folio o Referencia");
				return false;
			}
			
			if (this.facturaEmisor == null || "".equals(this.facturaEmisor)) {
				control(-1, "Indique una Razon Social");
				return false;
			}
		}
		
   		return true;
   	}
   	
	// ------------------------------------------------------------------------------------------
	// CARGA DE ARCHIVO
	// ------------------------------------------------------------------------------------------
	
	public void nuevaCarga() {
		this.fileName = "";
		this.fileSrc = null;
		this.fileLoaded = false;
		if (this.isDebug) 
			log.info("\n\nCarga preparada :: " + this.fileLoaded + "\n\n");
	}
	
	public void uploadListener(UploadEvent event) throws Exception {
		UploadItem item = null;
		
		try {
			control();
			item = event.getUploadItem();
			if (this.fileName != null && this.fileName.equals(item.getFileName())) 
				return;
			this.fileName = item.getFileName();
			this.fileSrc = ((item.isTempFile()) ? Files.readAllBytes(item.getFile().toPath()) : item.getData());
			if (this.fileSrc != null)
				this.fileLoaded = true;
			if (this.isDebug) {
				log.info("\n\n------------------------------------------------------------------------"
						+ "\nArchivo cargado :: " + this.fileLoaded
						+ "\n------------------------------------------------------------------------"
						+ "\nTemp            :: " + item.isTempFile() 
						+ "\nFilename        :: " + item.getFileName() 
						+ "\nFilesize        :: " + item.getFileSize() 
						+ "\nFile            :: " + item.getFile() 
						+ "\nData            :: " + item.getData() 
						+ "\n------------------------------------------------------------------------");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al hacer la carga del XML", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void analizarArchivo() throws Exception {
		SimpleDateFormat formatter = null;
		boolean emisorDesconocido = false;
		List<Persona> listProveedores = null;
		PersonaExt pojoProveedor = null;
		String tipoPersona = "";
		Respuesta respuesta = new Respuesta();
		String facturaFolio = "";
		String facturaFecha = "";
		String fileName = "";
		double saldoFactura = 0;
		// ------------------------------------------
		String comprobados = "";
		
		try {
			control();
			reiniciaFactura();
			if (this.fileSrc == null) {
				log.warn("VALIDACION - No ha especificado ninguna factura (*.xml)");
				control(-1, "No ha especificado ninguna factura (*.xml)");
				return;
			} 
			
			this.ifzRegGastosDet.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzRegGastosDet.cargaFacturaXML(this.fileSrc, TiposGastoCXP.RegistroGasto, this.pojoRegistroGasto.getId()); //, tipoGasto).cargarFacturaXMLRegistroGasto(this.fileSrc);//, this.pojoRegistroGasto.getId());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				reiniciaFactura();
				if (respuesta.getBody().getValor("comprobados") != null)
					comprobados = desglozaComprobados(respuesta.getBody().getValor("comprobados").toString());
				log.warn("ERROR INTERNO - Ocurrio un problema al intentar guardar la factura. " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError() + "\nNo se cargo la Factura." + comprobados);
				return;
			}
			
			// recupero los datos de la factura cargada
			this.facturaValores = (HashMap<String, Object>) respuesta.getBody().getValor("valores");
			if (this.facturaValores == null)
				this.facturaValores = new HashMap<String, Object>();
			this.facturaId = (Long) respuesta.getBody().getValor("idComprobante");
			this.facturaRfc = respuesta.getBody().getValor("comprobanteRfc").toString();
			this.facturaTipo = respuesta.getBody().getValor("comprobanteTipo").toString();
			this.facturaMoneda = respuesta.getBody().getValor("comprobanteMoneda").toString();
			this.facturaDescuento = Double.parseDouble(respuesta.getBody().getValor("comprobanteDescuento").toString());
			this.facturaSubtotal = Double.parseDouble(respuesta.getBody().getValor("comprobanteSubtotal").toString());
			this.facturaTotal = Double.parseDouble(respuesta.getBody().getValor("comprobanteTotal").toString());
			this.facturaTipoCambio = 1.0;
			if (respuesta.getBody().getValor("comprobanteTipoCambio") != null && ! "".equals(respuesta.getBody().getValor("comprobanteTipoCambio").toString()))
				this.facturaTipoCambio = Double.parseDouble(respuesta.getBody().getValor("comprobanteTipoCambio").toString());
			facturaFecha = respuesta.getBody().getValor("comprobanteFecha").toString();
			saldoFactura = Double.parseDouble(respuesta.getBody().getValor("comprobanteSaldo").toString());
			
			if (this.facturaTotal.doubleValue() > 0 && saldoFactura <= 0) {
				reiniciaFactura();
				if (respuesta.getBody().getValor("comprobados") != null)
					comprobados = desglozaComprobados(respuesta.getBody().getValor("comprobados").toString());
				log.warn("ERROR INTERNO - La Factura indicada ya fue saldada. ");
				control(-1, "El Documento ya ha sido saldado." + comprobados);
				return;
			}
			
			// Comprobamos con los detalles actuales
			for (FacturaImpuestos item : this.listFacturaConImpuestos) {
				if (item.getPojoFactura().getIdCfdi().longValue() == this.facturaId.longValue())
					saldoFactura -= item.getPojoFactura().getTotal();
				
				if (this.facturaTotal.doubleValue() > 0 && saldoFactura <= 0) {
					reiniciaFactura();
					if (respuesta.getBody().getValor("comprobados") != null)
						comprobados = desglozaComprobados(respuesta.getBody().getValor("comprobados").toString());
					control(-1, "El Documento ya fue cargado previamente en este Registro de Egreso.\nEl Documento queda saldado con el monto añadido en este Registro." + comprobados);
					return;
				}
			}
			
			if (respuesta.getBody().getValor("comprobanteRazonSocial") != null && ! "".equals(respuesta.getBody().getValor("comprobanteRazonSocial").toString().trim()))
				this.facturaEmisor = respuesta.getBody().getValor("comprobanteRazonSocial").toString();
			
			if (respuesta.getBody().getValor("comprobanteFactura") != null && ! "".equals(respuesta.getBody().getValor("comprobanteFactura").toString().trim()))
				facturaFolio = respuesta.getBody().getValor("comprobanteFactura").toString();
			
			// Comprobamos Persona/Negocio
			tipoPersona = "N";
			listProveedores = this.ifzPersonas.findLikeClaveNombre("rfc", this.facturaRfc, this.pojoGpoValPersonas, tipoPersona, 0, false);
			if (listProveedores == null || listProveedores.isEmpty()) {
				tipoPersona = "P";
				listProveedores = this.ifzPersonas.findLikeClaveNombre("rfc", this.facturaRfc, this.pojoGpoValPersonas, tipoPersona, 0, false);
				if (listProveedores == null || listProveedores.isEmpty()) {
					emisorDesconocido = true;
					tipoPersona = "N";
				}
			} 
			
			if (listProveedores != null && ! listProveedores.isEmpty()) {
				pojoProveedor = extenderPersona(listProveedores.get(0));
				pojoProveedor.setTipoPersona(("N".equals(tipoPersona) ? 2L : 1L));
				this.pojoRegistroGastoComprobado.setIdProveedor(pojoProveedor);
				this.facturaEmisor = pojoProveedor.getNombre();
			}

			this.facturaActualizar = false;
			if ((this.facturaEmisor == null || "".equals(this.facturaEmisor.trim())) || (facturaFolio == null || "".equals(facturaFolio.trim())))
				this.facturaActualizar = true;
			
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.pojoRegistroGastoComprobado.setFecha(formatter.parse(facturaFecha));
			this.pojoRegistroGastoComprobado.setTipoPersonaProveedor(tipoPersona);
			this.pojoRegistroGastoComprobado.setNombreProveedor(this.facturaEmisor);
			this.pojoRegistroGastoComprobado.setFacturaRfc(this.facturaRfc);
			this.pojoRegistroGastoComprobado.setFacturaSerie(respuesta.getBody().getValor("comprobanteSerie").toString());
			this.pojoRegistroGastoComprobado.setFacturaFolio(respuesta.getBody().getValor("comprobanteFolio").toString());
			this.pojoRegistroGastoComprobado.setReferencia(facturaFolio);
			this.montoTotalFactura = Utilerias.redondear(saldoFactura, 2);
			// Aplicamos descento
			if (this.facturaDescuento > 0)
				this.facturaSubtotal -= this.facturaDescuento;
			
			// Compruebo el ID de la factura cargada
			if (this.facturaId == null || this.facturaId <= 0L) {
				reiniciaFactura();
				log.warn("ERROR INTERNO - Ocurrio un problema al intentar guardar la factura");
				control(-1, "Ocurrio un problema al intentar guardar la factura");
				return;
			}

			getFTPData();
			if (validaRequest("FTP_LOCAL", "1")) {
				this.ftpDigitalizacionUser = "guess";
				this.ftpDigitalizacionPass = "guess";
				log.info("FTP ... " + this.ftpDigitalizacionHost + ":" + this.ftpDigitalizacionPort + ":" + this.ftpDigitalizacionUser + ":" + this.ftpDigitalizacionPass);
			}

			// Subimos fisicamente el archivo
			fileName = this.prefijoFacturas + this.facturaId + ".xml";
			log.info("Credenciales FTP ... ");
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			log.info("Subimos XML ... ");/*this.ftpDigitalizacionRuta*/
			if (! this.ifzFtp.put(this.fileSrc, this.ftpDigitalizacionRuta + fileName)) {
				log.warn("ERROR FTP - Ocurrio un problema al intentar guardar la factura en el servidor");
				control(-1, "Ocurrio un problema al intentar guardar la factura en el servidor");
			}
			log.info("FTP - Carga completa");
			if (emisorDesconocido)
				control(-1, "El Emisor de la Factura no existe en Negocios/Personas. RFC " + this.facturaRfc);
			desglosaImpuestos();
			nuevaCarga();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar/procesar el XML indicado", e);
		} 
	}

	public void descargarArchivo() {
		String fileName = "";
		
		try {
			control();
			if (this.reg == null) {
				log.error("ERROR 20 - No tiene asignada ninguna factura");
				control(20, "No ha especificado ninguna factura (*.xml)");
				return;
			}

			if (this.reg.getPojoFactura().getIdCfdi() == null || this.reg.getPojoFactura().getIdCfdi() <= 0L)
				this.reg.getPojoFactura().setIdCfdi(0L);

			this.facturaId = this.reg.getPojoFactura().getIdCfdi();
			if (this.facturaId == null || this.facturaId <= 0L) {
				log.error("ERROR 20 - No tiene asignada ninguna factura");
				control(20, "No ha especificado ninguna factura (*.xml)");
				return;
			}

			this.facturaEmisor = this.ifzRegGastosDet.getFacturaProperty(this.facturaId, "nombre");
			if (this.facturaEmisor == null || "".equals(this.facturaEmisor.trim()))
				this.facturaEmisor = "Emisor desconocido";

			getFTPData();
			if (validaRequest("FTP_LOCAL", "1")) {
				this.ftpDigitalizacionUser = "guess";
				this.ftpDigitalizacionPass = "guess";
				this.ftpDigitalizacionPort = "22";
				log.info("FTP ... " + this.ftpDigitalizacionHost + ":" + this.ftpDigitalizacionPort + ":" + this.ftpDigitalizacionUser + ":" + this.ftpDigitalizacionPass);
			}
			 
			// Inicializamos variables de archivos y recuperamos el archivo
			fileName = this.prefijoFacturas + this.facturaId + ".xml";
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			this.fileSrc = this.ifzFtp.get(this.ftpDigitalizacionRuta + fileName);
			if (this.fileSrc == null || this.fileSrc.length <= 0) {
				log.error("ERROR 21 - No se encontro el archivo en el servidor");
				control(21, "No se encontro el archivo en el servidor");
				return;
			}

			// Ponemos los datos en session
			this.httpSession.setAttribute("nombreDocumento", fileName);
			this.httpSession.setAttribute("formato", "xml");
			this.httpSession.setAttribute("contenido", this.fileSrc);
		} catch (Exception e) {
    		control(true, 9, "Ocurrio un problema al intentar recuperar el XML del servidor", e);
		}
	}

	public void borrarXML() {
		try {
			control();
			if (this.facturaDescartada && this.facturaId != null && this.facturaId > 0L) {
				log.info("Eliminando XML " + this.facturaId);
	    		this.ifzRegGastosDet.eliminarFactura(this.facturaId);
			}
		} catch (Exception e) {
    		control("No pude borrar el XML " + this.facturaId + " cargado previamentes y que no se uso", e);
		}
	}
	
	private void getFTPData() {
		this.ftpDigitalizacionHost = this.entornoProperties.getString("ftp.digitalizacion.host");
		this.ftpDigitalizacionPort = this.entornoProperties.getString("ftp.digitalizacion.port");
		this.ftpDigitalizacionUser = this.entornoProperties.getString("ftp.digitalizacion.user");
		this.ftpDigitalizacionPass = this.entornoProperties.getString("ftp.digitalizacion.password");
		this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta.cxp");
	}

	private void reiniciaFactura() {
		this.facturaId = 0L;
		this.facturaFecha = Calendar.getInstance().getTime();
		this.facturaTipo = "";
		this.facturaUuid = "";
		this.facturaRfc = "";
		this.facturaEmisor = "";
		this.facturaDescuento = 0D;
		this.facturaSubtotal = 0D;
		this.facturaTotal = 0D;
		this.facturaMoneda = "";
		this.facturaTipoCambio = 0D;
		this.facturaTotal = 0D;
		this.facturaValores = new HashMap<String, Object>();
		this.listFacturaConceptos =  new ArrayList<FacturaConcepto>();
		this.facturaDescartada = true;
	}

	private String desglozaComprobados(String comprobados) {
		String resultados = "";
		String[] splitted = null;
		String[] aux = null;
		// --------------------------
		String tipoGasto = "";
		String idTarget = "";
		String monto = "";
		String tipoComprobante = "";
		
		try {
			splitted = comprobados.split(",");
			for (int index = 0; index < splitted.length; index ++) {
				aux = splitted[index].trim().split("\\|");
				tipoGasto = TiposGastoCXP.fromString(aux[0].trim()).name();
				idTarget = aux[1].trim();
				monto = (new DecimalFormat("###,###,##0.00")).format(Double.parseDouble(aux[2].trim()));
				if (aux.length > 3)
					tipoComprobante = aux[3].trim();
				
				if (! "".equals(resultados))
					resultados += "\n";
				
				// TIPO_GASTO ID por $ 0.00 (I)
				resultados += tipoGasto + " " + idTarget + " por $ " + monto;
				if (! "".equals(tipoComprobante))
					resultados += " (" + tipoComprobante + ")";
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al desglozar los registros comprobados", e);
			resultados = comprobados;
			comprobados = resultados.replace(",", "\n");
			comprobados = comprobados.replace("|", " - ");
		} finally {
			resultados = "\n\nComprobaciones previas:\n" + resultados;
		}
		
		return resultados;
		/*String resultados = "";
		String[] splitted = null;
		String[] aux = null;
		double monto = 0;
		
		try {
			splitted = comprobados.split(",");
			for (int index = 0; index < splitted.length; index ++) {
				aux = splitted[index].trim().split("\\|");
				monto = Double.parseDouble(aux[2].trim());
				if (! "".equals(resultados))
					resultados += "\n";
				resultados += TiposGastoCXP.fromString(aux[0].trim()).name() + " #" + aux[1].trim() + " por $" + (new DecimalFormat("#.00")).format(monto);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al desglozar los registros comprobados", e);
			resultados = comprobados;
			comprobados = resultados.replace(",", "\n");
			comprobados = comprobados.replace("|", " - ");
		} finally {
			resultados = "\n\nComprobaciones previas:\n" + resultados;
		}
		
		return resultados;*/
	}
	
	private PersonaExt extenderPersona(Persona persona) {
		PersonaExt personaExt =  new PersonaExt();
		//long personaAuxiliar = 0L;
		
		personaExt.setId(persona.getId());
		personaExt.setCreadoPor(persona.getCreadoPor());
		personaExt.setFechaCreacion(persona.getFechaCreacion());
		personaExt.setModificadoPor(persona.getModificadoPor());
		personaExt.setFechaModificacion(persona.getFechaModificacion());
		if(persona.getHomonimo() > 0){
			personaExt.setHomonimo(true);
		}else{
			personaExt.setHomonimo(false);
		}
		personaExt.setNombre(persona.getNombre());
		personaExt.setPrimerNombre(persona.getPrimerNombre());
		personaExt.setSegundoNombre(persona.getSegundoNombre());
		personaExt.setNombresPropios(persona.getNombresPropios());
		personaExt.setPrimerApellido(persona.getPrimerApellido());
		personaExt.setSegundoApellido(persona.getSegundoApellido());
		personaExt.setRfc(persona.getRfc());
		personaExt.setSexo(persona.getSexo());
		personaExt.setFechaNacimiento(persona.getFechaNacimiento());
		if(persona.getEstadoCivil() != null){
			personaExt.setEstadoCivil(ifzConValores.findById(persona.getEstadoCivil()));
		}
		if(persona.getLocalidad() != null){
			personaExt.setLocalidad(ifzLocalidad.findById(persona.getLocalidad()));
		}
		if(persona.getNacionalidad () != null){
			personaExt.setNacionalidad(ifzConValores.findById(persona.getNacionalidad()));
		}
		/*if(persona.getConyuge() != null) {
			if (personaAuxiliar == 0L) {
				personaAuxiliar = persona.getId();
				personaExt.setConyuge(convertPersonaToPersonaExt(persona.getConyuge()));
			} else {
				if (persona.getConyuge().getId() != personaAuxiliar)
					personaExt.setConyuge(convertPersonaToPersonaExt(persona.getConyuge()));
			}
			personaAuxiliar = 0L;
		}*/
		personaExt.setNumeroHijos(persona.getNumeroHijos());
		personaExt.setDomicilio(persona.getDomicilio());
		personaExt.setTelefono(persona.getTelefono());
		personaExt.setCorreo(persona.getCorreo());
		
		if(persona.getFinado() > 0){
			personaExt.setFinado(true);
		}else{
			personaExt.setFinado(false);
		}
		
		if(persona.getTipoSangre() != null){
			personaExt.setTipoSangre(ifzConValores.findById(persona.getTipoSangre()));
		}
		if(persona.getColonia() != null){
			personaExt.setColonia(ifzColonia.findById(persona.getColonia()));
		}
		
		personaExt.setTipoPersona(persona.getTipoPersona());
		
		String res = persona.getPrimerNombre() != null && persona.getPrimerNombre().length() > 0? persona.getPrimerNombre() : "";
		res += persona.getSegundoNombre()	!= null && persona.getSegundoNombre().length() > 0 ? " " + persona.getSegundoNombre() : "";
		res += persona.getNombresPropios()	!= null && persona.getNombresPropios().length() > 0 ? " " + persona.getNombresPropios() : "";
		res += persona.getPrimerApellido()	!= null && persona.getPrimerApellido().length() > 0 ? " " + persona.getPrimerApellido() : "";
		res += persona.getSegundoApellido()	!= null && persona.getSegundoApellido().length() > 0 ? " " + persona.getSegundoApellido() : "";
		
		personaExt.setNombreCompleto(res);
		
		personaExt.setNumeroCuenta(persona.getNumeroCuenta());
		personaExt.setClabeInterbancaria(persona.getClabeInterbancaria());
		//personaExt.setBanco(persona.getBanco());

		return personaExt;
	}

   	// ---------------------------------------------------------------------------
	// CONCEPTOS
   	// ---------------------------------------------------------------------------
	
	public void nuevaBusquedaConceptos() {
		control();
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
   		this.valorBusquedaConceptos = "";
   		this.pagBusquedaConceptos = 1;
   		this.avanzadaConceptos = false;
   		
   		this.listConceptoGasto = new ArrayList<ConValores>();
   		this.pojoConcepto = new ConValores();
	}
	
	public void buscarConceptos() {
		String value = "";
		
		try {
			control();
			if (this.campoBusquedaConceptos == null || "".equals(this.campoBusquedaConceptos))
				this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
			value = this.valorBusquedaConceptos;
			value = (value == null) ? "" : value.trim();
			if (value.contains(" "))
				value = value.replace(" ", "%");
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listConceptoGasto = this.ifzConValores.findLikeProperty(this.campoBusquedaConceptos, value, this.pojoGpoValGasto, 0);
			if (this.listConceptoGasto == null || this.listConceptoGasto.isEmpty()) {
				log.info("ERROR 13: Busqueda sin resultados");
				control(13, this.mensajesInf.get(13));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al buscar los Conceptos para Gastos", e);
		}
	}
	
	public void seleccionarConcepto() {
		try {
			control();
			if (this.pojoConcepto == null || this.pojoConcepto.getId() <= 0L) {
				log.info("ERROR INTERNO: No se pudo recuperar el Concepto seleccionado");
				control(-1, "Ocurrio un problema al intentar recuperar el Concepto seleccionado");
				return;
			}
			
			this.conceptoGasto = this.pojoConcepto.getId() + " - " + this.pojoConcepto.getDescripcion();
			desglosaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Concepto de Gasto seleccionado", e);
		} finally {
			nuevaBusquedaConceptos();
		}
	}

	public void toggleAvanzadaConceptos() {
		this.avanzadaConceptos = ! this.avanzadaConceptos;
	}

	// ------------------------------------------------------------------------------------------
	// BUSQUEDA OBRAS
	// ------------------------------------------------------------------------------------------
	
	public void obrasBusqueda() {
		control();
		this.campoBusquedaObra = this.tipoBusquedaObra.get(0).getValue().toString();
		this.valTipoBusquedaObra = "";
		this.avanzadaObra = false;
		this.paginacionObras = 1;
   		this.tipoObra = 0;
		this.tipoObraBusquedaObra = (int) this.listObrasTiposItems.get(0).getValue();
		
		this.listObras = new ArrayList<Obra>();
		this.pojoObra = null;
	}

	public void buscarObras() {
		try {
			control();
			this.paginacionObras = 1;
			if (this.listObras != null)
				this.listObras.clear();

			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valTipoBusquedaObra, this.tipoObraBusquedaObra, false, "", 0);
			if (this.listObras == null || this.listObras.isEmpty()) {
				log.info("ERROR 13 - Busqueda sin resultados");
				control(13, this.mensajesInf.get(13));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Obras", e);
		} finally {
			if (this.listObras == null)
				this.listObras = new ArrayList<Obra>();
			log.info(this.listObras.size() + " Obras encontradas");
		}
	}

	public void seleccionarObra() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Obra seleccionada");
				return;
			}
			
			// Asignamos la Obra al REGISTRO DE GASTO, si corresponde
			this.pojoRegistroGastoComprobado.setIdObra(this.pojoObra);
			obrasBusqueda();
			ordenCompraBusqueda();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		}
	}
		
	public void cambioObra() {
		try {
			this.tipoObra = 1;
		} catch (Exception e) {
			log.error("error en cambioObra", e);
		}
	}

	public void toggleAvanzadaObra() {
		this.avanzadaObra = ! this.avanzadaObra;
	}

	// ------------------------------------------------------------------------------------------
	// BUSQUEDA ORDEN DE COMPRA
	// ------------------------------------------------------------------------------------------
	
	public void ordenCompraBusqueda() {
		control();
		this.campoBusquedaOrdenCompra = this.listTiposBusquedaOrdenCompra.get(0).getValue().toString();
		this.valorBusquedaOrdenCompra = "";
		this.paginacionOrdenCompra = 1;
		this.busquedaAvanzadaOrdenCompra = false;
		
		this.listOrdenCompra = new ArrayList<OrdenCompra>();
		this.pojoOrdenCompra = null;
		if (this.pojoRegistroGastoComprobado == null || this.pojoRegistroGastoComprobado.getIdObra() == null || this.pojoRegistroGastoComprobado.getIdObra().getId() == null || this.pojoRegistroGastoComprobado.getIdObra().getId() <= 0L) 
			control(-1, "Debe seleccionar una Obra");
	}
	
	public void ordenCompraBuscar() {
		try {
			control();
			if ("".equals(this.campoBusquedaOrdenCompra))
				this.campoBusquedaOrdenCompra = this.listTiposBusquedaOrdenCompra.get(0).getValue().toString();//this.opcionesBusquedaOrdenCompra.get(0);
			
			this.ifzOrdenCompra.setInfoSesion(this.loginManager.getInfoSesion());
			this.listOrdenCompra = this.ifzOrdenCompra.findLikeProperty(this.campoBusquedaOrdenCompra, this.valorBusquedaOrdenCompra, this.pojoRegistroGastoComprobado.getIdObra().getId(), true, 0);
			if (this.listOrdenCompra == null || this.listOrdenCompra.isEmpty()) {
				log.info("ORDEN DE COMPRA - Busqueda sin resultados");
				control(13, mensajesInf.get(13));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Ordenes de Compra de la Obra seleccionada", e);
		}
	}
	
	public void ordenCompraSeleccionar() {
		try {
			control();
			if (this.pojoOrdenCompra == null || this.pojoOrdenCompra.getId() == null || this.pojoOrdenCompra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Orden de Compra seleccionada");
				return;
			}
			
			// Asignamos la Orden de Compra al REGISTRO DE GASTO, si corresponde
			this.pojoRegistroGastoComprobado.setIdOrdenCompra(this.pojoOrdenCompra);
			ordenCompraBusqueda();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Orden de Compra seleccionada", e);
		}
	}

	public void toggleBusquedaAvanzadaOrdenCompra() {
		this.busquedaAvanzadaOrdenCompra = ! this.busquedaAvanzadaOrdenCompra;
	}
	
	// ------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------------

	public FacturaImpuestos getReg() {
		return reg;
	}

	public void setReg(FacturaImpuestos reg) {
		//this.reg = reg;
		try {
			this.reg = new FacturaImpuestos();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.reg, reg);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar asignar la comprobacion seleccionada. Proceso con asignacion directa", e);
			this.reg = new FacturaImpuestos(reg.getPojoFactura(), reg.getListImpuestos(), reg.getListRetenciones());
			this.reg.setSeleccionado(reg.isSeleccionado());
		}
	}

   	public String getTituloComprobacion() {
   		if (this.pojoRegistroGasto != null && this.pojoRegistroGasto.getId() != null && this.pojoRegistroGasto.getId() > 0L)
   			return "Registro Egresos " + this.pojoRegistroGasto.getId();
   		return "Nuevo Registro Egresos";
   	}
   	
   	public void setTituloComprobacion(String value) { }

   	public String getTituloComprobacionFactura() {
   		if (this.reg != null && this.reg.getPojoFactura() != null && this.reg.getPojoFactura().getId() != null && this.reg.getPojoFactura().getId() > 0L)
   			return "Comprobacion " + this.reg.getPojoFactura().getId() + (this.reg.getPojoFactura().getFacturado() == 1 ? " [Factura]" : "[Sin Factura]");
   		return "Nueva Comprobacion";
   	}
   	
   	public void setTituloComprobacionFactura(String value) {}
   	
	public String getObraNombre() {
   		if (this.pojoRegistroGastoComprobado != null && this.pojoRegistroGastoComprobado.getIdObra() != null && this.pojoRegistroGastoComprobado.getIdObra().getNombre() != null && ! "".equals(this.pojoRegistroGastoComprobado.getIdObra().getNombre()))
   	   		return "<b>" + this.pojoRegistroGastoComprobado.getIdObra().getId() + "</b> - " + this.pojoRegistroGastoComprobado.getIdObra().getNombre();
   		return "";
	}
	
   	public void setObraNombre(String value) { }
   	
	public String getOrdenCompraDescripcion() {
		if (this.pojoRegistroGastoComprobado != null && this.pojoRegistroGastoComprobado.getIdOrdenCompra() != null && this.pojoRegistroGastoComprobado.getIdOrdenCompra().getId()!= null && this.pojoRegistroGastoComprobado.getIdOrdenCompra().getId()> 0L)
			return "<b>" + this.pojoRegistroGastoComprobado.getIdOrdenCompra().getId() + "</b> " + this.pojoRegistroGastoComprobado.getIdOrdenCompra().getFolio() + " - " + this.pojoRegistroGastoComprobado.getIdOrdenCompra().getNombreProveedor() + " - $ " + String.format("%1$,.2f", this.pojoRegistroGastoComprobado.getIdOrdenCompra().getTotal());
		return "";
	}
	
	public void setOrdenCompraDescripcion(String value) {}
	
	public ConGrupoValores getPojoGpoValTerceros() {
		return pojoGpoValTerceros;
	}

	public void setPojoGpoValTerceros(ConGrupoValores pojoGpoValTerceros) {
		this.pojoGpoValTerceros = pojoGpoValTerceros;
	}

	public PersonaExt getPojoTerceros() {
		return pojoTerceros;
	}

	public void setPojoTerceros(PersonaExt pojoTerceros) {
		this.pojoTerceros = pojoTerceros;
	}

	public ConGrupoValores getPojoGpoValGasto() {
		return pojoGpoValGasto;
	}

	public void setPojoGpoValGasto(ConGrupoValores pojoGpoValGasto) {
		this.pojoGpoValGasto = pojoGpoValGasto;
	}

	public PersonaExt getPojoProveedores() {
		return pojoProveedores;
	}

	public void setPojoProveedores(PersonaExt pojoProveedores) {
		this.pojoProveedores = pojoProveedores;
	}

	public PagosGastosDetExt getPojoComprobadoGto() {
		return pojoRegistroGastoComprobado;
	}

	public void setPojoComprobadoGto(PagosGastosDetExt pojoComprobadoGto) {
		this.pojoRegistroGastoComprobado = pojoComprobadoGto;
	}
	
	public String getReferencia() {
		if (this.pojoRegistroGastoComprobado != null && this.pojoRegistroGastoComprobado.getReferencia() != null)
			return this.pojoRegistroGastoComprobado.getReferencia();
		return "";
	}

	public void setReferencia(String referencia) {
		if (this.pojoRegistroGastoComprobado != null)
			this.pojoRegistroGastoComprobado.setReferencia(referencia);
	}

	public Date getFechaCompruebaGto() {
		if (this.pojoRegistroGastoComprobado.getFecha() != null)
			return this.pojoRegistroGastoComprobado.getFecha();
		return Calendar.getInstance().getTime();
	}

	public void setFechaCompruebaGto(Date fechaCompruebaGto) {
		this.pojoRegistroGastoComprobado.setFecha(fechaCompruebaGto);
	}
	
	public boolean getFacturaCredito() {
		return this.pojoRegistroGastoComprobado.getEsCredito();
	}

	public void setFacturaCredito(boolean facturaCredito) {
		this.pojoRegistroGastoComprobado.setEsCredito(facturaCredito);
	}

	public boolean getManoObra() {
		return this.pojoRegistroGastoComprobado.getManoObra();
	}

	public void setManoObra(boolean manoObra) {
		this.pojoRegistroGastoComprobado.setManoObra(manoObra);
	}
	
	public Long getPagosGastosDetId() {
		return this.pojoRegistroGastoComprobado.getId() != null ? this.pojoRegistroGastoComprobado.getId() : 0;
	}

	public void setPagosGastosDetId(Long pagosGastosDetId) {
		this.pojoRegistroGastoComprobado.setId(pagosGastosDetId);
	}
	
	public ConValores getPojoConceptoGtos() {
		return pojoConceptoGtos;
	}

	public void setPojoConceptoGtos(ConValores pojoConceptoGtos) {
		this.pojoConceptoGtos = pojoConceptoGtos;
	}

	public PagosGastosDetImpuestosExt getPojoDesgloImpto() {
		return pojoDesgloImpto;
	}

	public void setPojoDesgloImpto(PagosGastosDetImpuestosExt pojoDesgloImpto) {
		this.pojoDesgloImpto = pojoDesgloImpto;
	}

	public long getPojoGtosComp() {
		return pojoRegistroGastoMain;
	}

	public void setPojoGtosComp(long pojoGtosCompId) {
		this.pojoRegistroGastoMain = pojoGtosCompId;
	}
	
	public String getConcepto() {
		if (this.pojoRegistroGasto.getConcepto() != null) 
			return this.pojoRegistroGasto.getConcepto();
		return "";
	}

	public void setConcepto(String concepto) {
		this.pojoRegistroGasto.setConcepto(concepto);
	}
	
	public ConGrupoValores getPojoGpoValPersonas() {
		return pojoGpoValPersonas;
	}

	public void setPojoGpoValPersonas(ConGrupoValores pojoGpoValPersonas) {
		this.pojoGpoValPersonas = pojoGpoValPersonas;
	}

	public PersonaExt getPojoBeneficiarios() {
		return pojoBeneficiarios;
	}

	public void setPojoBeneficiarios(PersonaExt pojoBeneficiarios) {
		this.pojoBeneficiarios = pojoBeneficiarios;
	}

	public CtasBancoExt getPojoCtas() {
		return pojoCtas;
	}

	public void setPojoCtas(CtasBancoExt pojoCtas) {
		this.pojoCtas = pojoCtas;
	}
	
	public Date getFecha() {
		return this.pojoRegistroGasto.getFecha() != null ? this.pojoRegistroGasto.getFecha() : Calendar.getInstance().getTime();
	}

	public void setFecha(Date fecha) {
		this.pojoRegistroGasto.setFecha(fecha);
	}
		
	public Long getPagosGastosId() {
		return this.pojoRegistroGasto != null && this.pojoRegistroGasto.getId() != null ? this.pojoRegistroGasto.getId() : 0;
	}

	public void setPagosGastosId(Long pagosGastosId) {
		this.pojoRegistroGasto.setId(pagosGastosId);
	}

	public List<Persona> getListBeneficiarios() {
		return listBeneficiarios;
	}

	public void setListBeneficiarios(List<Persona> listBeneficiarios) {
		this.listBeneficiarios = listBeneficiarios;
	}

	public List<CuentaBancaria> getListCuentas() {
		return listCuentas;
	}

	public void setListCuentas(List<CuentaBancaria> listCuentas) {
		this.listCuentas = listCuentas;
	}
	
	public List<PagosGastosDetImpuestosExt> getListDesgloseImpuestos() {
		return listDesgloseImpuestos;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setListDesgloseImpuestos(List<PagosGastosDetImpuestosExt> listDesgloseImpuestos) {
		this.listDesgloseImpuestos = (List) ((ArrayList)listDesgloseImpuestos).clone();
	}

	public List<GastosImpuestoExt> getListImpuestosDelGasto() {
		return listImpuestosDelGasto;
	}

	public void setListImpuestosDelGasto(List<GastosImpuestoExt> listImpuestosDelGasto) {
		this.listImpuestosDelGasto = listImpuestosDelGasto;
	}

	public List<Persona> getListTerceros() {
		return listTerceros;
	}

	public void setListTerceros(List<Persona> listTerceros) {
		this.listTerceros = listTerceros;
	}

	public String getNombreBeneficiario() {
		return nombreBeneficiario;
	}

	public void setNombreBeneficiario(String nombreBeneficiario) {
		this.nombreBeneficiario = nombreBeneficiario;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public Double getTotalFacturasReportadas() {
		return totalFacturasReportadas;
	}

	public void setTotalFacturasReportadas(Double totalFacturasReportadas) {
		this.totalFacturasReportadas = totalFacturasReportadas;
	}

	public boolean isEncontroMismoGrupo() {
		return band;
	}

	public void setEncontroMismoGrupo(boolean encontroMismoGrupo) {
		this.band = encontroMismoGrupo;
	}

	public String getConceptoGasto() {
		return conceptoGasto;
	}

	public void setConceptoGasto(String conceptoGasto) {
		this.conceptoGasto = conceptoGasto;
	}
	
	public List<Persona> getListProveedores() {
		return listProveedores;
	}

	public void setListProveedores(List<Persona> listProveedores) {
		this.listProveedores = listProveedores;
	}

	public List<ConValores> getListConceptoGasto() {
		return listConceptoGasto;
	}

	public void setListConceptoGasto(List<ConValores> listConceptoGasto) {
		this.listConceptoGasto = listConceptoGasto;
	}
	
	public List<FacturaImpuestos> getListFacturaConImpuestos() {
		return listFacturaConImpuestos;
	}

	public void setListFacturaConImpuestos( List<FacturaImpuestos> listFacturaConImpuestos) {
		this.listFacturaConImpuestos = listFacturaConImpuestos;
	}
	
	public List<Sucursal> getListSucursales() {
		return listSucursales;
	}

	public void setListSucursales(List<Sucursal> listSucursales) {
		this.listSucursales = listSucursales;
	}
	
	public long getSucursal() {
		if (this.pojoRegistroGasto != null && this.pojoRegistroGasto.getIdSucursal() != null)
			return this.pojoRegistroGasto.getIdSucursal().getId();
		return 0L;
	}

	public void setSucursal(long idSucursal) {
		if (idSucursal > 0L && this.pojoRegistroGasto != null) {
			for (Sucursal var : this.listSucursales) {
				if (var.getId() != idSucursal)
					continue;
				this.pojoRegistroGasto.setIdSucursal(var);
				break;
			}
		}
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public boolean isEsTransferencia() {
		return esTransferencia;
	}

	public void setEsTransferencia(boolean esTransferencia) {
		this.esTransferencia = esTransferencia;
	}

	public boolean isEsCheque() {
		return esCheque;
	}

	public void setEsCheque(boolean esCheque) {
		this.esCheque = esCheque;
	}
	
	public boolean isEsSpei() {
		return esSpei;
	}

	public void setEsSpei(boolean esSpei) {
		this.esSpei = esSpei;
	}

	public String getTerceros() {
		return terceros;
	}

	public void setTerceros(String terceros) {
		this.terceros = terceros;
	}
		
	public String getTipoPersona() {
		return this.tipoPersona;
	}
	
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
		this.beneficiario = "";
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensje) {
		this.tipoMensaje = tipoMensje;
	}

	public boolean isFacturaConLlave() {
		return sinFactura;
	}

	public void setFacturaConLlave(boolean facturaConLlave) {
		this.sinFactura = facturaConLlave;
	}

	public boolean getFacturado() {
		return this.sinFactura;
	}
	
	public void setFacturado(boolean facturado) {
		this.sinFactura = facturado;
	}

	public int getFacturadoAux() {
		return (this.sinFactura ? 1 : 0);
	}
	
	public void setFacturadoAux(int facturado) {
		this.sinFactura = (facturado == 1);
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valorBusqueda = valTipoBusqueda;
	}

	public String getValTipoBusqueda() {
		return valorBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setListGastos(List<PagosGastos> listGastos) {
		this.listRegistroGastos = listGastos;
	}

	public List<PagosGastos> getListGastos() {
		return listRegistroGastos;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public List<GastosImpuestoExt> getListRetDelGasto() {
		return listRetencionesDelGasto;
	}

	public void setListRetDelGasto(List<GastosImpuestoExt> listRetDelGasto) {
		this.listRetencionesDelGasto = listRetDelGasto;
	}

	public boolean isExisteRetencion() {
		return existeRetencion;
	}

	public void setExisteRetencion(boolean existeRetencion) {
		this.existeRetencion = existeRetencion;
	}

	public List<PagosGastosDetImpuestosExt> getListDesgloseRet() {
		return listDesgloseRetenciones;
	}

	public void setListDesgloseRet(List<PagosGastosDetImpuestosExt> listDesgloseRet) {
		this.listDesgloseRetenciones = listDesgloseRet;
	}

	public String getValBusqRet() {
		return valBusqRet;
	}

	public void setValBusqRet(String valBusqRet) {
		this.valBusqRet = valBusqRet;
	}

	public String getCampoBusqRet() {
		return campoBusqRet;
	}

	public void setCampoBusqRet(String campoBusqRet) {
		this.campoBusqRet = campoBusqRet;
	}

	public String[] getListBusqRet() {
		return listBusqRet;
	}

	public void setListBusqRet(String[] listBusqRet) {
		this.listBusqRet = listBusqRet;
	}

	public List<ConValores> getListRetEncontradas() {
		return listRetEncontradas;
	}

	public void setListRetEncontradas(List<ConValores> listRetEncontradas) {
		this.listRetEncontradas = listRetEncontradas;
	}

	public ConValores getPojoNvaRet() {
		return pojoNvaRet;
	}

	public void setPojoNvaRet(ConValores pojoNvaRet) {
		this.pojoNvaRet = pojoNvaRet;
	}

	public void setTipoReporte(int tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public int getTipoReporte() {
		return tipoReporte;
	}

	public String generaPoliza() {
		/*try {
			this.pojoGtosComp=ifzGtosComp.findAllById(pojoGtosComp.getId());
			ifzGtosComp.mensajeSaldoCtas(pojoGtosComp, this.pojoGtosComp.getIdCuentaOrigen().getEmpresa().getId());
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return "OK";
	}

	public ObraRem getIfzObras() {
		return ifzObras;
	}

	public void setIfzObras(ObraRem ifzObras) {
		this.ifzObras = ifzObras;
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}

	public List<SelectItem> getTipoBusquedaObra() {
		return tipoBusquedaObra;
	}

	public void setTipoBusquedaObra(List<SelectItem> tipoBusquedaObra) {
		this.tipoBusquedaObra = tipoBusquedaObra;
	}

	public String getValTipoBusquedaObra() {
		return valTipoBusquedaObra;
	}

	public void setValTipoBusquedaObra(String valTipoBusquedaObra) {
		this.valTipoBusquedaObra = valTipoBusquedaObra;
	}

	public String getCampoBusquedaObra() {
		return campoBusquedaObra;
	}

	public void setCampoBusquedaObra(String campoBusquedaObra) {
		this.campoBusquedaObra = campoBusquedaObra;
	}

	public int getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(int tipoObra) {
		this.tipoObra = tipoObra;
	}
	
	public String getPerfilRequiereObra() {
		return perfilRequiereObra;
	}

	public void setPerfilRequiereObra(String perfilRequiereObra) {
		this.perfilRequiereObra = perfilRequiereObra;
	}

	public String getObservaciones() {
		if (this.pojoRegistroGastoComprobado != null && this.pojoRegistroGastoComprobado.getObservaciones() != null)
			return this.pojoRegistroGastoComprobado.getObservaciones();
		return "";
	}

	public void setObservaciones(String observaciones) {
		if (this.pojoRegistroGastoComprobado != null)
			this.pojoRegistroGastoComprobado.setObservaciones(observaciones);
	}
	
   	public String getFacturaId() {
		if (this.facturaId != null && this.facturaId > 0L)
			return this.facturaId.toString();
		return "";
	}
	
	public void setFacturaId(String value) {}
	
	public List<SelectItem> getListFormasPagoItems() {
		return listFormasPagoItems;
	}

	public void setListFormasPagoItems(List<SelectItem> listFormasPagoItems) {
		this.listFormasPagoItems = listFormasPagoItems;
	}
   	
   	public boolean isReqReferencia() {
		return reqReferencia;
	}

	public void setReqReferencia(boolean reqReferencia) {
		this.reqReferencia = reqReferencia;
	}

	public String getReferenciaOperacion() {
		return referenciaOperacion;
	}

	public void setReferenciaOperacion(String referenciaOperacion) {
		this.referenciaOperacion = referenciaOperacion;
	}

	public List<OrdenCompra> getListOrdenCompra() {
		return listOrdenCompra;
	}
	
	public void setListOrdenCompra(List<OrdenCompra> listOrdenCompra) {
		this.listOrdenCompra = listOrdenCompra;
	}

	public OrdenCompra getPojoOrdenCompra() {
		return pojoOrdenCompra;
	}

	public void setPojoOrdenCompra(OrdenCompra pojoOrdenCompra) {
		this.pojoOrdenCompra = pojoOrdenCompra;
	}
	
	public List<SelectItem> getListTiposBusquedaOrdenCompra() {
		return listTiposBusquedaOrdenCompra;
	}

	public void setListTiposBusquedaOrdenCompra(List<SelectItem> listTiposBusquedaOrdenCompra) {
		this.listTiposBusquedaOrdenCompra = listTiposBusquedaOrdenCompra;
	}

	public String getCampoBusquedaOrdenCompra() {
		return campoBusquedaOrdenCompra;
	}

	public void setCampoBusquedaOrdenCompra(String campoBusquedaOrdenCompra) {
		this.campoBusquedaOrdenCompra = campoBusquedaOrdenCompra;
	}

	public String getValorBusquedaOrdenCompra() {
		return valorBusquedaOrdenCompra;
	}

	public void setValorBusquedaOrdenCompra(String valorBusquedaOrdenCompra) {
		this.valorBusquedaOrdenCompra = valorBusquedaOrdenCompra;
	}

	public int getPaginacionOrdenCompra() {
		return paginacionOrdenCompra;
	}

	public void setPaginacionOrdenCompra(int paginacionOrdenCompra) {
		this.paginacionOrdenCompra = paginacionOrdenCompra;
	}

	public int getPaginacionObras() {
		return paginacionObras;
	}

	public void setPaginacionObras(int paginacionObras) {
		this.paginacionObras = paginacionObras;
	}

	public boolean getPerfilAdministrativo() {
		return perfilEgresos;
	}
	
	public void setPerfilAdministrativo(boolean perfilEgresos) {
		this.perfilEgresos = perfilEgresos;
	}

	public boolean getBuscarAdministrativo() {
		return tipoObraBusquedaObra == 4;
	}
	
	public void setBuscarAdministrativo(boolean tipoObraBusquedaObra) {
		this.tipoObraBusquedaObra = tipoObraBusquedaObra ? 4 : 0;
	}
	
	public List<SelectItem> getListObrasTiposItems() {
		return listObrasTiposItems;
	}
	
	public void setListObrasTiposItems(List<SelectItem> listObrasTiposItems) {
		this.listObrasTiposItems = listObrasTiposItems;
	}
	
	public int getTipoObraBusquedaObra() {
		return tipoObraBusquedaObra;
	}
	
	public void setTipoObraBusquedaObra(int tipoObraBusquedaObra) {
		this.tipoObraBusquedaObra = tipoObraBusquedaObra;
	}
	
	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public List<SelectItem> getListSucursalesItems() {
		return listSucursalesItems;
	}

	public void setListSucursalesItems(List<SelectItem> listSucursalesItems) {
		this.listSucursalesItems = listSucursalesItems;
	}

	public List<SelectItem> getListCuentasItems() {
		return listCuentasItems;
	}

	public void setListCuentasItems(List<SelectItem> listCuentasItems) {
		this.listCuentasItems = listCuentasItems;
	}

	public boolean isAvanzadaObra() {
		return avanzadaObra;
	}
	
	public void setAvanzadaObra(boolean avanzadaObra) {
		this.avanzadaObra = avanzadaObra;
	}
	
	public boolean isBusquedaAvanzadaOrdenCompra() {
		return busquedaAvanzadaOrdenCompra;
	}
	
	public void setBusquedaAvanzadaOrdenCompra(boolean busquedaAvanzadaOrdenCompra) {
		this.busquedaAvanzadaOrdenCompra = busquedaAvanzadaOrdenCompra;
	}

	public ConValores getPojoConcepto() {
		return pojoConcepto;
	}
	
	public void setPojoConcepto(ConValores pojoConcepto) {
		this.pojoConcepto = pojoConcepto;
	}
	
	public List<SelectItem> getTiposBusquedaConceptos() {
		return tiposBusquedaConceptos;
	}
	
	public void setTiposBusquedaConceptos(List<SelectItem> tiposBusquedaConceptos) {
		this.tiposBusquedaConceptos = tiposBusquedaConceptos;
	}
	
	public String getCampoBusquedaConceptos() {
		return campoBusquedaConceptos;
	}
	
	public void setCampoBusquedaConceptos(String campoBusquedaConceptos) {
		this.campoBusquedaConceptos = campoBusquedaConceptos;
	}
	
	public String getValorBusquedaConceptos() {
		return valorBusquedaConceptos;
	}
	
	public void setValorBusquedaConceptos(String valorBusquedaConceptos) {
		this.valorBusquedaConceptos = valorBusquedaConceptos;
	}
	
	public int getPagBusquedaConceptos() {
		return pagBusquedaConceptos;
	}
	
	public void setPagBusquedaConceptos(int pagBusquedaConceptos) {
		this.pagBusquedaConceptos = pagBusquedaConceptos;
	}
	
	public boolean isAvanzadaConceptos() {
		return avanzadaConceptos;
	}
	
	public void setAvanzadaConceptos(boolean avanzadaConceptos) {
		this.avanzadaConceptos = avanzadaConceptos;
	}

	public boolean isPermiteEditarRegistroGasto() {
		return perfilPermiteEditar;
	}

	public void setPermiteEditarRegistroGasto(boolean perfilEditarRegistroGasto) {
		this.perfilPermiteEditar = perfilEditarRegistroGasto;
	}

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}
	
	public int getNumPaginaFact() {
		return numPaginaFact;
	}

	public void setNumPaginaFact(int numPaginaFact) {
		this.numPaginaFact = numPaginaFact;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}

	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	// Montos comprobacion

	public double getDescuento() {
		if (this.facturaDescuento == null)
			this.facturaDescuento = 0D;
		return this.facturaDescuento;
	}

	public void setDescuento(double descuento) {
		this.facturaDescuento = descuento;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTotalImpuestos() {
		return totalImpuestos;
	}

	public void setTotalImpuestos(Double totalImpuestos) {
		this.totalImpuestos = totalImpuestos;
	}

	public Double getTotalRetenciones() {
		return totalRetenciones;
	}

	public void setTotalRetenciones(Double totalRetenciones) {
		this.totalRetenciones = totalRetenciones;
	}

	public Double getTotal() {
		return montoTotalFactura;
	}

	public void setTotal(Double total) {
		this.montoTotalFactura = total;
		this.totalPago = total;
	}
	
	// Factura Valores
	
	public String getFacturaDescripcion() {
		String descripcion = "Seleccione factura";
		if (this.facturaId != null && this.facturaId > 0L) {
			descripcion = this.facturaId.toString();
			if (this.facturaEmisor != null && ! "".equals(this.facturaEmisor.trim()) && ! "Emisor desconocido".equals(this.facturaEmisor.trim()))
				descripcion += " - " + this.facturaRfc;
			if (this.facturaMoneda != null && "USD".equals(this.facturaMoneda.trim())){
				descripcion += " - " + this.facturaMoneda;
				if (this.facturaTipoCambio != null && this.facturaTipoCambio > 0D)
					descripcion += " - T.C. " + this.facturaTipoCambio;
			}
		}
		
		return descripcion;
	}
	
	public void setFacturaDescripcion(String value) {}

	public String getFacturaEmisor() {
		return facturaEmisor;
	}

	public void setFacturaEmisor(String facturaEmisor) {
		this.facturaEmisor = facturaEmisor;
	}

	public String getFacturaUuid() {
		//return this.facturaValores.get("uuid").toString();
		return facturaUuid;
	}

	public void setFacturaUuid(String facturaUuid) {
		//this.facturaValores.put("uuid", facturaUuid);
		this.facturaUuid = facturaUuid;
	}

	public Date getFacturaFecha() {
		//return (Date) this.facturaValores.get("fecha");
		return facturaFecha;
	}

	public void setFacturaFecha(Date facturaFecha) {
		//this.facturaValores.put("fecha", facturaFecha);
		this.facturaFecha = facturaFecha;
	}

	public String getFacturaRfc() {
		//return this.facturaValores.get("rfc").toString();
		return facturaRfc;
	}

	public void setFacturaRfc(String facturaRfc) {
		//this.facturaValores.put("rfc", facturaRfc);
		this.facturaRfc = facturaRfc;
	}

	public String getFacturaTipo() {
		//return this.facturaValores.get("tipo").toString();
		return facturaTipo;
	}

	public void setFacturaTipo(String facturaTipo) {
		//return this.facturaValores.put("tipo", facturaTipo);
		this.facturaTipo = facturaTipo;
	}

	public String getFacturaMoneda() {
		//return this.facturaValores.get("moneda").toString();
		return facturaMoneda;
	}

	public void setFacturaMoneda(String facturaMoneda) {
		//this.facturaValores.put("moneda", facturaMoneda);
		this.facturaMoneda = facturaMoneda;
	}

	public Double getFacturaTipoCambio() {
		//return (Double) this.facturaValores.get("tipoCambio");
		return facturaTipoCambio;
	}

	public void setFacturaTipoCambio(Double facturaTipoCambio) {
		//this.facturaValores.put("tipoCambio", facturaTipoCambio);
		this.facturaTipoCambio = facturaTipoCambio;
	}

	public Double getFacturaTotal() {
		//return (Double) this.facturaValores.get("total");
		return facturaTotal;
	}

	public void setFacturaTotal(Double facturaTotal) {
		//this.facturaValores.put("total", facturaTotal);
		this.facturaTotal = facturaTotal;
	}

	public List<FacturaConcepto> getListFacturaConceptos() {
		return listFacturaConceptos;
	}

	public void setListFacturaConceptos(List<FacturaConcepto> listFacturaConceptos) {
		this.listFacturaConceptos = listFacturaConceptos;
	}

	public int getPaginaFacturaComprobacion() {
		return paginaFacturaComprobacion;
	}

	public void setPaginaFacturaComprobacion(int paginaFacturaComprobacion) {
		this.paginaFacturaComprobacion = paginaFacturaComprobacion;
	}

	// Montos Conceptos de Factura

	public double getConceptosTotalImportes() {
		return conceptosTotalImportes;
	}

	public void setConceptosTotalImportes(double conceptosTotalImportes) {
		this.conceptosTotalImportes = conceptosTotalImportes;
	}

	public double getConceptosTotalImpuestos() {
		return conceptosTotalImpuestos;
	}

	public void setConceptosTotalImpuestos(double conceptosTotalImpuestos) {
		this.conceptosTotalImpuestos = conceptosTotalImpuestos;
	}

	public double getConceptosTotal() {
		return conceptosTotal;
	}

	public void setConceptosTotal(double conceptosTotal) {
		this.conceptosTotal = conceptosTotal;
	}
}
