package net.giro.cxp;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.ParseException;
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
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

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
import net.giro.cxp.beans.FacturaImpuestos;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.beans.PagosGastosDet;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt; // import cde.tyg.admon.datos.Tygpersonas;
import net.giro.cxp.beans.ProgPagosDetalle;
import net.giro.cxp.beans.ProgPagosDetalleExt;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.CuentasBancariasRem;
import net.giro.cxp.logica.GastosImpuestoRem;
import net.giro.cxp.logica.PagosGastosDetImpuestosRem;
import net.giro.cxp.logica.PagosGastosDetRem;
import net.giro.cxp.logica.ProgPagosDetalleRem;
import net.giro.navegador.LoginManager;

public class RegistroGastosAction {
	private static Logger log = Logger.getLogger(RegistroGastosAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
   	private HttpSession httpSession;
	private InitialContext ctx;
	
	private PagosGastosRem ifzGtosComp; 
	private ConGrupoValoresRem ifzGpoVal;	
	private CuentasBancariasRem ifzCtas; 
	private GastosImpuestoRem ifzGastoImpuesto;
	private ConValoresRem ifzConValores;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	private PersonaRem ifzPersonas;
	private PagosGastosDetRem ifzComprobadoGto;
	private SucursalesRem ifzSucursal;
	private ProgPagosDetalleRem ifzProPagDet;
	private PagosGastosDetImpuestosRem ifzDesgloImpto;
	//private NQueryRem ifzQuery;
	private ReportesRem ifzReporte;
	
	private List<Persona> listTerceros; 
	private List<Sucursal> listSucursales;	
	private List<SelectItem> listSucursalesItems;
	private List<PagosGastosDetImpuestosExt> lisImpuestosEliminados;
	private List<PagosGastosDetImpuestosExt> listDesgloseImpuestos;
	private List<PagosGastosDetImpuestosExt> listDesgloseRet;
	private List<GastosImpuestoExt> listImpuestosDelGasto;
	private List<GastosImpuestoExt> listRetDelGasto;
	private List<Persona> listBeneficiarios;
	private List<CuentaBancaria> listCuentas;
	private List<SelectItem> listCuentasItems;
	private List<Persona> listProveedores;
	private List<PagosGastosExt> listRegistroGastos;
	private List<ConValores> listRetEncontradas;
	private List<ProgPagosDetalleExt> listProPagDet;
	private List<FacturaImpuestos> listFacturaConImpuestos;
	private List<FacturaImpuestos> listFacturaConImpuestosEliminadas;
	private PagosGastosExt pojoRegistroGasto;
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
	private Double totalFacturasReportadas;
	private Double montoTotalFactura;
	private Double importeImpuesto;
	private Double importeRetencion;
	private Double sumImpuestos;
	private Double sumRetenciones;
	private Double subtotal;
	private Double totalImpuestos;
	private Double totalRetenciones;
	private Double totalPago;
	private String terceros;
	private String operacion;
	private String descripcionFactura;
	private String conceptoGasto;
	private String tipoPersona;
	private String nombreProveedor;	
	private String nombreBeneficiario;
	private String beneficiario;		
	private String cuenta;	
	private String valTipoBusqueda;
	private String valBusqRet;
	private String campoBusqRet;
	private String campoBusqueda;
	private String perfilRequiereObra;
	private String resOperacion;
	private String sucursalesVisibles;
	private HashMap <Integer, String> mensajesInf;
	private String [] tipoBusqueda;
	private String [] listBusqRet;
	private long usuarioId;
	@SuppressWarnings({ "rawtypes", "unused"}) 
	private HashMap listaParam;
	@SuppressWarnings("unused")
	private byte[] datosRep;
	private boolean encontroMismoGrupo;
	private boolean esTransferencia;
	private boolean existeRetencion;
	private boolean esCheque;
	private boolean esSpei;
   	private int 	numPagina;
	private int 	tipoMensaje;
	private int 	tipoReporte;
	private boolean sinFactura;
	private ObraRem ifzObras;
	private Obra pojoObra;
	private List<Obra> listObras;
	private List<SelectItem> tipoBusquedaObra;
	private String valTipoBusquedaObra;
	private String campoBusquedaObra;
	private int tipoObra;
	private int paginacionObras;
	private String observaciones;
	private ConGrupoValores pojoGpoValFormasPago; 
	private List<ConValores> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	private boolean reqReferencia;
	private String referenciaOperacion;
	// SEGMENTO
	//private AreaRem ifzAreas;
	//private List<Area> listAreas;
	//private boolean muestraSeg;
	//private String segmento;
	//private Long segNeg;
	// Carga XML
	private FtpRem ifzFtp;
	private byte[] fileSrc;
	private Long facturaId;
	private String facturaRfc;
	private String facturaEmisor;
	private boolean facturaDescartada;
	private boolean facturaActualizar;
	private String prefijoFacturas;
	private String ftpDigitalizacionHost;
	private String ftpDigitalizacionPort;
	private String ftpDigitalizacionUser;
	private String ftpDigitalizacionPass;
	private String ftpDigitalizacionRuta;
	public boolean avanzadaObra;
	// BUSQUEDA CONCEPTOS
	private List<ConValores> listConceptoGasto;
	private ConValores pojoConcepto;
	private List<SelectItem> tiposBusquedaConceptos;
	private String campoBusquedaConceptos;
	private String valorBusquedaConceptos;
	private int pagBusquedaConceptos;
	private boolean avanzadaConceptos;
	// ORDEN DE COMPRA
	private OrdenCompraRem ifzOrdenCompra;
	private List<OrdenCompra> listOrdenCompra;
	private OrdenCompra pojoOrdenCompra;
	private List<SelectItem> listTiposBusquedaOrdenCompra;
	private String campoBusquedaOrdenCompra;
	private String valorBusquedaOrdenCompra;
	public boolean busquedaAvanzadaOrdenCompra;
	private int paginacionOrdenCompra;
	// EMPLEADO-USUARIO
	private EmpleadoRem ifzEmpleado;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalSugerida;
	// PERFILES
	private boolean perfilEgresos;
	private List<SelectItem> listObrasTiposItems;
	private int tipoObraBusquedaObra;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	
	public RegistroGastosAction() throws NamingException, Exception {
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
			
	   		this.ctx = new InitialContext();
	   		this.ifzPersonas 		= (PersonaRem) 					this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
	   		this.ifzGtosComp 		= (PagosGastosRem) 				this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
	   		//this.ifzAreas 			= (AreaRem) 					this.ctx.lookup("ejb:/Logica_CuentasPorPagar//AreaFac!net.giro.cxp.logica.AreaRem");
	   		this.ifzGastoImpuesto 	= (GastosImpuestoRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
	   		this.ifzDesgloImpto 	= (PagosGastosDetImpuestosRem) 	this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
	   		this.ifzProPagDet 		= (ProgPagosDetalleRem) 		this.ctx.lookup("ejb:/Logica_CuentasPorPagar//ProgPagosDetalleFac!net.giro.cxp.logica.ProgPagosDetalleRem");
	   		this.ifzObras 			= (ObraRem) 					this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
	   		this.ifzGpoVal 			= (ConGrupoValoresRem) 			this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
	   		this.ifzConValores 		= (ConValoresRem) 				this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
	   		this.ifzSucursal 		= (SucursalesRem) 				this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
	   		this.ifzReporte 		= (ReportesRem) 				this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
	   		//this.ifzQuery 			= (NQueryRem) 					this.ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");
	   		this.ifzCtas 			= (CuentasBancariasRem) 		this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
	   		this.ifzFtp 			= (FtpRem) 						this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzComprobadoGto 	= (PagosGastosDetRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzOrdenCompra 	= (OrdenCompraRem) 				this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzLocalidad 		= (LocalidadDAO) 				this.ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			this.ifzColonia 		= (ColoniaDAO) 					this.ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");		
			this.ifzEmpleado 		= (EmpleadoRem) 				this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGtosComp.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGastoImpuesto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProPagDet.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReporte.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzComprobadoGto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenCompra.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.ifzOrdenCompra.showSystemOuts(false);
			
			comprobarUsuario();
			
			this.ftpDigitalizacionHost = this.entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = this.entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = this.entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = this.entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta");
			this.prefijoFacturas = "CXP-RG-"; 
	   		
			/*this.segNeg = 0L;
			this.muestraSeg = false;*/
	   		
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
			//this.pojoSucursal = new SucursalExt();
			this.pojoTerceros = new PersonaExt();
			this.pojoGpoValTerceros = new ConGrupoValores();
			this.pojoNvaRet = new ConValores();	
			this.listRegistroGastos = new ArrayList<PagosGastosExt>();
			this.listTerceros = new ArrayList<Persona>();
			this.listSucursales = new ArrayList<Sucursal>();
			this.listCuentas = new ArrayList<CuentaBancaria>();
			this.listBeneficiarios = new ArrayList<Persona>();
			this.lisImpuestosEliminados = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>(); 
			this.listDesgloseRet= new ArrayList<PagosGastosDetImpuestosExt>();
			this.listRetEncontradas = new ArrayList<ConValores>();
			this.listImpuestosDelGasto = new ArrayList<GastosImpuestoExt>();
			this.listRetDelGasto = new ArrayList<GastosImpuestoExt>();		
			this.listProPagDet = new ArrayList<ProgPagosDetalleExt>();
			this.reg = new FacturaImpuestos();
			this.listSucursalesItems = new ArrayList<SelectItem>();
			this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>(); 
			//this.pojoCatBan = new CatBancos();
			//this.pojoSpei = new SpeiOutgoing();	
			
			this.numPagina = 1;
			this.tipoMensaje = 0;
			this.cuenta = "";
			this.beneficiario = "";	
			this.tipoPersona = "P";
			this.operacion = "C";
			this.resOperacion = "";
			this.esTransferencia = false;
			this.esCheque = true;
			this.esSpei = false;
			this.encontroMismoGrupo = false;
			this.sinFactura = false;
			this.existeRetencion = false;
			this.totalFacturasReportadas = 0D;
			this.montoTotalFactura=0D;
			this.totalImpuestos=0D;
			this.totalRetenciones=0D;
			this.subtotal=0D;
			this.totalPago=0D;
			
			this.tipoBusqueda = new String[3];
			this.tipoBusqueda[0] = "Todos";
			this.tipoBusqueda[1] = "Persona"; // "Empleado";
			this.tipoBusqueda[2] = "Negocio"; // "Proveedor";
			this.valTipoBusqueda = this.tipoBusqueda[0];
			this.campoBusqueda = "";
	   		
	   		// BUSQUEDA CONCEPTOS
	   		this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
	   		this.tiposBusquedaConceptos.add(new SelectItem("descripcion", "Descripcion"));
	   		this.tiposBusquedaConceptos.add(new SelectItem("id", "ID"));
	   		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
	   		this.valorBusquedaConceptos = "";
	   		this.pagBusquedaConceptos = 1;
	   		this.avanzadaConceptos = false;

			// Busqueda Obras
			this.tipoBusquedaObra = new ArrayList<SelectItem>();
			this.tipoBusquedaObra.add(new SelectItem("nombre", "Nombre"));
			this.tipoBusquedaObra.add(new SelectItem("nombreCliente", "Cliente"));
			this.tipoBusquedaObra.add(new SelectItem("nombreContrato", "Contrato"));
			this.tipoBusquedaObra.add(new SelectItem("id", "ID"));
	   		/*this.tipoBusquedaObra = new ArrayList<String>();
	   		this.tipoBusquedaObra.add("Nombre");
	   		this.tipoBusquedaObra.add("Cliente");
	   		this.tipoBusquedaObra.add("Contrato");
	   		this.tipoBusquedaObra.add("Clave");
	   		this.campoBusquedaObra = this.tipoBusquedaObra.get(0);*/
			this.campoBusquedaObra = this.tipoBusquedaObra.get(0).getValue().toString();
	   		this.valTipoBusquedaObra = "";
	   		this.tipoObra = 0;
			this.paginacionObras = 1;
						
			this.listObrasTiposItems = new ArrayList<SelectItem>();
			this.listObrasTiposItems.add(new SelectItem(1, msgProperties.getString("obrasTipo.obra")));
			this.listObrasTiposItems.add(new SelectItem(2, msgProperties.getString("obrasTipo.proyecto")));
			this.listObrasTiposItems.add(new SelectItem(3, msgProperties.getString("obrasTipo.ordenTrabajo")));
			if (this.perfilEgresos)
				this.listObrasTiposItems.add(new SelectItem(4, msgProperties.getString("obrasTipo.administrativa")));

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

			// Busqueda Orden de Compra
			this.listTiposBusquedaOrdenCompra = new ArrayList<SelectItem>();
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("folio", "Folio"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("nombreProveedor", "Proveedor"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("nombreSolicita", "Solicita"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("nombreContacto", "Contacto"));
			this.listTiposBusquedaOrdenCompra.add(new SelectItem("id", "ID"));
			/*this.opcionesBusquedaOrdenCompra = new ArrayList<String>();
			this.opcionesBusquedaOrdenCompra.add("Proveedor");
			this.opcionesBusquedaOrdenCompra.add("Id");*/
			this.campoBusquedaOrdenCompra = this.listTiposBusquedaOrdenCompra.get(0).getValue().toString();
			//this.campoBusquedaOrdenCompra = this.opcionesBusquedaOrdenCompra.get(0);
			this.valorBusquedaOrdenCompra = "";
			this.paginacionOrdenCompra = 1;
	   		
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
			//this.pojoGpoValPersonas = this.ifzGpoVal.findByName("SYS_RELPER");
			if (this.pojoGpoValPersonas == null || this.pojoGpoValPersonas.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");

			// CUENTAS TERCEROS
			/*this.pojoGpoValTerceros = this.ifzGpoVal.findByName("SYS_CUENTASTERCEROS");
			if (this.pojoGpoValTerceros == null || this.pojoGpoValTerceros.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_CUENTASTERCEROS en con_grupo_valores");*/
			
			
			this.reqReferencia = true;
			//this.operacion = operacionValidate("");
			cargarFormasDePago();
			cargarCuentas();
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.RegistroGastosAction", e);
			this.ctx = null;
		}
  	}
   	

   	public String buscar() throws NamingException, SQLException, ParseException{
   		String tmp = null;
   		
   		try {
			if (this.valTipoBusqueda.equals(this.tipoBusqueda[0]))
				tmp = null;
			else if (this.valTipoBusqueda.equals(this.tipoBusqueda[1]))
				tmp = "P"; //"DE";
			else if (this.valTipoBusqueda.equals(this.tipoBusqueda[2]))
				tmp = "N"; // "EMP";
			else 
				tmp = null; // "PROV";
			
			this.listRegistroGastos = this.ifzGtosComp.findLikeBenefTipoPersonaExt(this.campoBusqueda, tmp, "P", "pago", 120, this.sucursalesVisibles);
			
			if (this.listRegistroGastos.isEmpty())
				this.resOperacion = mensajesInf.get(13);
			else
				this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			this.listRegistroGastos = new ArrayList<PagosGastosExt>();
			log.error("Error en el metodo buscar", e);
		}
   		
		return "OK";
   	}
   	
   	public String nuevo() {
		try {	
			this.pojoRegistroGasto = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoBeneficiarios = new PersonaExt();
			this.pojoTerceros = new PersonaExt();
			this.pojoObra = null;
			this.pojoOrdenCompra = null;
			
			cargarSucursales();
			setSucursal(this.idSucursalSugerida);

			if (this.listFacturaConImpuestosEliminadas == null)
				this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			
			this.listFacturaConImpuestosEliminadas.clear();	
			this.listFacturaConImpuestos.clear();			
			this.lisImpuestosEliminados.clear();
			this.listDesgloseImpuestos.clear(); 
			this.listImpuestosDelGasto.clear();
			
			this.listRetEncontradas.clear();
			this.listDesgloseRet.clear();
			this.totalRetenciones=0D;
			this.totalPago= 0D;
			
			this.cuenta="";
			this.tipoPersona="P";
			this.beneficiario = "";
			if (this.pojoEmpleadoUsuario != null && this.pojoEmpleadoUsuario.getId() != null && this.pojoEmpleadoUsuario.getId() > 0L)
				this.beneficiario = this.pojoEmpleadoUsuario.getPersona().getId() + " - " + this.pojoEmpleadoUsuario.getNombre();
			this.operacion = ""; 
			this.esTransferencia=false;
			this.esSpei=false;
			this.esCheque=true;
			this.existeRetencion = false;
			this.totalFacturasReportadas =0D;
			this.observaciones = "";
			this.referenciaOperacion = "";
			
			obrasBusqueda();
			ordenCompraBusqueda();
			agregar();
		} catch (Exception e) {
			log.error("Error en el metodo nuevo.", e);
			return "ERROR";
		}
		
		return "OK";
	}

   	public void editar() {
   		try {
   			control();
   			log.info("RegistroGasto ... Preparando para editar");
   			if (this.pojoRegistroGasto == null) {
   	   			log.info("RegistroGasto ... No selecciono ningun registro");
   	   			control("No selecciono ningun registro");
   				return;
   			}
   			
   			this.operacion = this.pojoRegistroGasto.getOperacion();
   			if ("C".equals(this.operacion)) {
   				this.referenciaOperacion = this.pojoRegistroGasto.getNoCheque().toString();
			} else if ("T".equals(this.operacion)) {
				this.referenciaOperacion = this.pojoRegistroGasto.getFolioAutorizacion();
			} else {
				this.referenciaOperacion = "";
			}
   			
   			//this.sucursal = this.pojoRegistroGasto.getIdSucursal().getId() + " - " + this.pojoRegistroGasto.getIdSucursal().getSucursal();
   			this.cuenta = this.pojoRegistroGasto.getIdCuentaOrigen().getId() + " - " + this.pojoRegistroGasto.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto() + " - " + this.pojoRegistroGasto.getIdCuentaOrigen().getNumeroDeCuenta();
   			this.beneficiario = this.pojoRegistroGasto.getIdBeneficiario().getId() + " - " + this.pojoRegistroGasto.getIdBeneficiario().getNombre();
   			this.tipoPersona = this.pojoRegistroGasto.getTipoBeneficiario();
			//this.segmento = "";
   			
   			if (this.listFacturaConImpuestos == null)
   				this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>();
   			this.listFacturaConImpuestos.clear();
			
			if (this.listFacturaConImpuestosEliminadas == null)
				this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.listFacturaConImpuestosEliminadas.clear();
   			
   			// recuperamos detalle de Registro de Gastos
			log.info("RegistroGasto ... Recuperando detalles");
   			List<PagosGastosDetExt> listDetalles = this.ifzComprobadoGto.findByPagosGastosExt(this.pojoRegistroGasto, 0);
   			if (listDetalles != null && ! listDetalles.isEmpty()) {
   	   			for (PagosGastosDetExt var : listDetalles) {
   	   				this.reg = new FacturaImpuestos();
   	   				this.reg.setPojoFactura(var);
   	   	   			this.listFacturaConImpuestos.add(this.reg);
   	   			}
   			}

			log.info("RegistroGasto ... " + this.listFacturaConImpuestos.size() + " detalles recuperados");
			this.reg = new FacturaImpuestos();
			actualizaTotalFaturas();
			log.info("RegistroGasto... Lista para editar");
   		} catch (Exception e) {
			//this.operacion = this.operacionValidate(this.operacion);
			log.error("Error en CuentasPorPagar.RegistroGastosAction.editar", e);
			control(true);
   		}
   	}
   	
	public String guardar() throws NamingException, Exception {
		Respuesta resp = new Respuesta();
		Pattern pat = null;
		Matcher match = null;
		boolean  registroNuevo = false;
		boolean borrarXML = false;
		String fileName = "";
		
		try {
			control();
			//this.segNeg = 0L;
			if (this.pojoRegistroGasto.getId() == null)
				registroNuevo = true;
			
			this.pojoRegistroGasto.setIdObra(new Obra());
			this.pojoRegistroGasto.setIdOrdenCompra(0L);
			this.pojoRegistroGasto.setModificadoPor(this.usuarioId);
			this.pojoRegistroGasto.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoRegistroGasto.setTipoBeneficiario(this.tipoPersona);
			
			if (this.pojoObra != null) {
				if (this.pojoRegistroGasto.getIdObra() == null) {
					this.pojoRegistroGasto.setIdObra(this.pojoObra);
				} else {
					if (this.pojoRegistroGasto.getIdObra().getId() != null && this.pojoRegistroGasto.getIdObra().getId() != this.pojoObra.getId())
						this.pojoRegistroGasto.setIdObra(this.pojoObra);
				}
			}
			
			if (this.totalFacturasReportadas == null || this.totalFacturasReportadas == 0) {
				//this.operacion = this.operacionValidate(this.operacion);
				log.info("ERROR 2 - Debe indicar al menos una factura");
				control(true, 2, this.mensajesInf.get(2));
				return "OK";
			}
			
			//validando Cuenta
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.cuenta);
			if (match.find()) {
				if (registroNuevo) {
					this.pojoCtas = this.ifzGtosComp.findCuentaBancariaById(Long.valueOf(match.group(1))); 
				} else {
					if (this.pojoCtas.getId() != Long.valueOf(match.group(1)))
						this.pojoCtas= this.ifzGtosComp.findCuentaBancariaById(Long.valueOf(match.group(1))); 
				}
			}

			// Asigno cuenta
			this.pojoRegistroGasto.setIdCuentaOrigen(this.pojoCtas);

			if (! "OK".equals(validaSaldoCuentaPropios())) {
				//this.operacion = this.operacionValidate(this.operacion);
				return "ERROR";
			}
			
			//validando segmento
			/*pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.segmento);
			if (match.find())
				this.segNeg = Long.valueOf(match.group(1));
			this.pojoRegistroGasto.setSegmento(segNeg);*/
			
			//validando operacion para mostrar catalodo de Terceros
			if ("T".equals(this.operacion) || "S".equals(this.operacion)) {
				match = pat.matcher(this.beneficiario);			
				if (match.find())
					if (registroNuevo)				
						this.pojoTerceros = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
					else
						if (this.pojoTerceros.getId() != Long.valueOf(match.group(1)))
							this.pojoTerceros = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				this.pojoRegistroGasto.setIdCuentaDestinoTerceros(this.pojoTerceros);	
				this.pojoRegistroGasto.setBeneficiario(this.pojoTerceros.getNombre()+" "+(this.pojoTerceros.getPrimerApellido() != null ? this.pojoTerceros.getPrimerApellido():"")+" "+(this.pojoTerceros.getSegundoApellido() != null ? this.pojoTerceros.getSegundoApellido():""));
			}

			//validando beneficiario
			match = pat.matcher(this.beneficiario);
			if (match.find()) {
				if (registroNuevo) {
					this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				} else {
					if (this.pojoBeneficiarios.getId() != Long.valueOf(match.group(1)))
						this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				}
			}
			
			// Asigno beneficiario
			this.pojoRegistroGasto.setIdBeneficiario(this.pojoBeneficiarios);
			if ("N".equals(this.tipoPersona)) {
				this.nombreBeneficiario = this.pojoBeneficiarios.getNombre();
			} else {
				this.nombreBeneficiario = this.pojoBeneficiarios.getPrimerNombre() + " " 
						+ ((this.pojoBeneficiarios.getSegundoNombre()   != null && ! "".equals(this.pojoBeneficiarios.getSegundoNombre().trim()))   ? this.pojoBeneficiarios.getSegundoNombre().trim()   : "") + " " 
						+ ((this.pojoBeneficiarios.getPrimerApellido()  != null && ! "".equals(this.pojoBeneficiarios.getPrimerApellido().trim()))  ? this.pojoBeneficiarios.getPrimerApellido().trim()  : "") + " " 
						+ ((this.pojoBeneficiarios.getSegundoApellido() != null && ! "".equals(this.pojoBeneficiarios.getSegundoApellido().trim())) ? this.pojoBeneficiarios.getSegundoApellido().trim() : "");
			}
			
			this.pojoRegistroGasto.setBeneficiario(this.nombreBeneficiario);
			this.pojoRegistroGasto.setOperacion(this.operacion);
			this.pojoRegistroGasto.setMonto(this.totalFacturasReportadas);
			
			// Asignacion de operacion
			if ("C".equals(this.operacion)) {
				this.pojoRegistroGasto.setNoCheque(Integer.parseInt(this.referenciaOperacion));
			} else if ("T".equals(this.operacion)) {
				this.pojoRegistroGasto.setFolioAutorizacion(this.referenciaOperacion);
			}
				
			if (registroNuevo) {
				if ("S".equals(this.operacion)) {
					/*String sql = "select spei_autorizacion(3, current_date, "+usuarioId+", 6, "+this.pojoSucursal.getId()+", null, "+this.totalFacturasReportadas+")";
					List<Object> listObj = this.ifzQuery.findNativeQuery(sql);
					String Id = String.valueOf(listObj.get(0).toString());
					if (Id.equals("")) {
						this.resOperacion = "ERROR DESCONOCIDO";
						return "OK";
					} else {*/
						/*if (Id.substring(0, 1).equals("0") || Id.substring(0, 1).equals("1")) {
							CtasBanco pojoCtaSpei = new CtasBanco();
							pojoCtaSpei = this.ifzCtas.findAllById(Short.valueOf("659"));
							
							sql = "select spei_referencia()";
							listObj = this.ifzQuery.findNativeQuery(sql);
							String ref = String.valueOf(listObj.get(0).toString());
							if (ref.equals("")) {
								this.resOperacion = "ERROR DESCONOCIDO";
								return "OK";
							}else{
								this.pojoSpei.setNombreOrdenante(pojoCtaSpei.getCatBancoId().getNombreLargo().substring(0, pojoCtaSpei.getCatBancoId().getNombreLargo().length()<40?pojoCtaSpei.getCatBancoId().getNombreLargo().length():40)); 
								this.pojoSpei.setIdTipoCuentaOrdenante("40");
								this.pojoSpei.setCuentaOrdenante(pojoCtaSpei.getClabe());
								this.pojoSpei.setRfcOrdenante(pojoCtaSpei.getEmpresa().getRfcEmp());
								this.pojoSpei.setNombreBeneficiario(this.nombreBeneficiario.substring(0, this.nombreBeneficiario.length()<40?this.nombreBeneficiario.length():40));
								this.pojoSpei.setIdTipoCuentaBeneficiario("40");
								this.pojoSpei.setCuentaBeneficiario(this.pojoTerceros.getClabe());
								this.pojoSpei.setRfcBeneficiario(this.pojoTerceros.getRfc());
								//this.pojoSpei.setIdInstitucionBen(this.pojoTerceros.getCatBancoId().getSpeiInstitucionesId().getIdInstitucion());
								this.pojoSpei.setMonto(this.totalFacturasReportadas);
								this.pojoSpei.setIva(0D);
								this.pojoSpei.setReferenciaNumerica(Integer.parseInt(ref));
								this.pojoSpei.setIdTipoPago("1");
								this.pojoSpei.setStatus("");
								this.pojoSpei.setStatusOperacion(0);
								this.pojoSpei.setClaveRastreo("0");
								this.pojoSpei.setIdDevolucion(0);
								this.pojoSpei.setFechaCaptura(Calendar.getInstance().getTime());								
								this.pojoSpei.setUsuarioId(this.usuarioId);
								this.pojoSpei.setTipoOperacion(1);
								this.pojoSpei.setApp(6);
								this.pojoSpei.setConceptoPago(this.pojoGtosComp.getConcepto().substring(0, this.pojoGtosComp.getConcepto().length()<250?this.pojoGtosComp.getConcepto().length():250)); 
								this.pojoSpei.setEnvioAutomatico(Id.substring(0, 1));
							}
						}else{
							this.resOperacion = "ERROR: " + Id;
							return "OK";
						}*/
					/*	this.resOperacion = "NOT_IMPLEMENTED";
						return "OK";
					}*/
				}
				
				this.pojoRegistroGasto.setCreadoPor(this.usuarioId);
				this.pojoRegistroGasto.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoRegistroGasto.setTipo("P");//gasto "Pago"
				this.pojoRegistroGasto.setEstatus("C");//comprobado

				resp = ifzGtosComp.salvar(this.pojoRegistroGasto, true);
				
				//validando folio cheque					
				if (resp.getResultado() != -1) {
					this.pojoRegistroGasto.setId(Long.valueOf(resp.getReferencia()));	
					
					/*if ("S".equals(this.operacion)) {
						//this.pojoSpei.setIdOperacion(this.pojoGtosComp.getId().intValue());
						//this.pojoSpei.setIdSpeiOutgoing(ifzSpei.save(this.pojoSpei));
						//this.pojoGtosComp.setIdSpeiOutgoing(this.pojoSpei);
						//this.ifzGtosComp.update(pojoGtosComp);
					}
					
					resOperacion="C";
					if ("T".equals(this.operacion) || "S".equals(this.operacion))
					{							
						// Es un SPEI, genera el recibo.
						if ("S".equals(this.operacion)) {
							this.resOperacion = "S";
						}else
							this.resOperacion = "T"; //limpio el mensaje para que no salga el reporte de cheque por ser transferencia/spei
					}
					
					/ *if (perfil == null && !"S".equals(perfil)) {
						List<ProgPagosDetalle> lista = null;
						if (! listFacturaConImpuestos.isEmpty()) {
							for (FacturaImpuestos var: listFacturaConImpuestos) {
								Double montoValue = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
								Long conceptoValue = var.getPojoFactura().getConceptoId().getId();
								lista = ifzProPagDet.findByMontoConceptoEstatus(montoValue, conceptoValue, "G");
								if (lista != null) {
									lista.get(0).setEstatus("E");
									lista.get(0).setModificadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
									lista.get(0).setFechaModificacion(Calendar.getInstance().getTime());
									ifzProPagDet.update(lista.get(0));
								}
							}
						}
					}* /
					
					if (resOperacion.equals("C")) {/ *
						//impresion del cheque
						listaParam = new HashMap<String, String>();
						listaParam.put("folio", this.pojoGtosComp.getNoCheque());						
						listaParam.put("banco_id", this.pojoGtosComp.getIdCuentaOrigen().getId());
						//datosRep = ifzReporte.generaReporte("cheque.jasper", listaParam);
						httpSession.setAttribute("reporte", datosRep);
						httpSession.setAttribute("tipoReporte", "pdf");* /
					}*/
				} else {
					if (! "BIEN".equals(resp.getRespuesta())) {
						encontroMismoGrupo = true;// esta variable termine usandola como bandera gral
						this.resOperacion = resp.getRespuesta();
						
						if ((resp.getRespuesta().indexOf("SE CANCELA EL FOLIO ") > -1) || (resp.getRespuesta().indexOf("SE CANCELAN LOS FOLIOS ") > -1))
							this.tipoMensaje = 9;
						else
							this.tipoMensaje = 8;
						
						//this.operacion = this.operacionValidate(this.operacion);
						return "ERROR";   							
					}
				}
				
				this.listRegistroGastos.add(this.pojoRegistroGasto);
			} else {
				// Actualiza Registro de Gastos
				this.ifzGtosComp.update(this.pojoRegistroGasto);

				// Actualizo en la LISTA
				for (PagosGastosExt var : this.listRegistroGastos) {
					if (var.getId().equals(this.pojoRegistroGasto.getId())) {
						var = this.pojoRegistroGasto;
						break;
					}
				}
			}
				
			//grabando las facturas en caso de que hayan agregado algunas
			if (! this.listFacturaConImpuestos.isEmpty()) {
				long idPagosGastosDet = 0L;
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					if (var.getPojoFactura().getId() == null) {
						var.getPojoFactura().setIdPagosGastos(this.pojoRegistroGasto);
						idPagosGastosDet = this.ifzComprobadoGto.save(var.getPojoFactura());
						var.getPojoFactura().setId(idPagosGastosDet);
						
						if (! var.getListImpuestos().isEmpty()) {
							for (PagosGastosDetImpuestosExt var2 : var.getListImpuestos()) {
								var2.setIdPagosGastosDet(var.getPojoFactura());

								if (var2.getId() == 0L)
									var2.setId(this.ifzDesgloImpto.save(var2));
								else
									this.ifzDesgloImpto.update(var2);
							}
						}
						
						if (! var.getListRetenciones().isEmpty()) {
							for (PagosGastosDetImpuestosExt var2 : var.getListRetenciones()) {
								var2.setIdPagosGastosDet(var.getPojoFactura());

								if (var2.getId() == 0L)
									var2.setId(this.ifzDesgloImpto.save(var2));
								else
									this.ifzDesgloImpto.update(var2);
							}
						}
					} else {
						var.getPojoFactura().setIdPagosGastos(this.pojoRegistroGasto);
						this.ifzComprobadoGto.update(var.getPojoFactura());
						
						if (! var.getListImpuestos().isEmpty()) {
							for (PagosGastosDetImpuestosExt var2 : var.getListImpuestos()) {
								var2.setIdPagosGastosDet(var.getPojoFactura());

								if (var2.getId() == 0L)
									var2.setId(this.ifzDesgloImpto.save(var2));
								else
									this.ifzDesgloImpto.update(var2);
							}
						}
						
						if (! var.getListRetenciones().isEmpty()) {
							for (PagosGastosDetImpuestosExt var2 : var.getListRetenciones()) {
								var2.setIdPagosGastosDet(var.getPojoFactura());

								if (var2.getId() == 0L)
									var2.setId(this.ifzDesgloImpto.save(var2));
								else
									this.ifzDesgloImpto.update(var2);
							}
						}
					}
				}
			}

			// Borramos facturas previamente eliminadas
			if (this.listFacturaConImpuestosEliminadas != null && ! this.listFacturaConImpuestosEliminadas.isEmpty()) {
				getFTPData();
				
				for (FacturaImpuestos var : this.listFacturaConImpuestosEliminadas) {
					// borramos XML (documento cargado) si corresponde
					if (var.getPojoFactura().getIdXml() != null && var.getPojoFactura().getIdXml() > 0L) {
						borrarXML = true;
						this.facturaId = var.getPojoFactura().getIdXml();
						for (FacturaImpuestos item : this.listFacturaConImpuestos) {
							if (item.getPojoFactura().getIdXml() != null && item.getPojoFactura().getIdXml() > 0L && item.getPojoFactura().getIdXml().longValue() == this.facturaId.longValue()) {
								borrarXML = false;
								break;
							}
						}
						
						if (borrarXML) {
							// Lo elimino de la BD
							log.info("Eliminando XML " + this.facturaId);
				    		this.ifzComprobadoGto.eliminarFactura(this.facturaId);
							
							// Lo elimino fisicamente del servidor
							fileName = this.prefijoFacturas + var.getPojoFactura().getIdXml() + ".xml";
							this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
							if (! this.ifzFtp.delArchivo(this.ftpDigitalizacionRuta + fileName))
								log.warn("ERROR FTP - No se pudo Borrar la factura del servidor, ID: " + this.facturaId);
						}
						
						// Borro detalle (factura)
						this.ifzComprobadoGto.delete(var.getPojoFactura());
						this.facturaId = 0L;
					} else {
						// Borro detalle (factura)
						this.ifzComprobadoGto.delete(var.getPojoFactura());
					}
				}
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			mensajeTransaccion();
		} catch (Exception e) {
			//this.operacion = this.operacionValidate(this.operacion);
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en CuentasPorPagar.RegistroGastosAction.guardar", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public String eliminar() {
		try {
			if (this.listFacturaConImpuestosEliminadas == null)
				this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.listFacturaConImpuestosEliminadas.add(this.reg);
			this.listFacturaConImpuestos.remove(this.reg);
			actualizaTotalFaturas();					
		} catch (Exception e) {			
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en el metodo eliminar.", e);
			return "ERROR";
		}

		return "OK";
	}

	public String reporte() {
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
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en el metodo reporte.", e);
			return "ERROR";
		}
		return "OK";
	}

   	public String checaCancelado() throws NamingException, SQLException, ParseException {
   		control();
   		if (this.pojoRegistroGasto.getEstatus().equals("X"))
   			control(mensajesInf.get(14));
   		return "OK";
   	}
   	
   	public void cargarSucursales() {
   		try {
			if (this.listSucursales == null)
				this.listSucursales = new ArrayList<Sucursal>();
			this.listSucursales.clear();
			
			if (this.listSucursalesItems == null)
				this.listSucursalesItems = new ArrayList<SelectItem>();
			this.listSucursalesItems.clear();
			
			this.listSucursales = this.ifzSucursal.buscarSucursales("sucursal", "");
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
			if (this.listFormasPago == null)
				this.listFormasPago = new ArrayList<ConValores>();
			this.listFormasPago.clear();
			
			if (this.listFormasPagoItems == null)
				this.listFormasPagoItems = new ArrayList<SelectItem>();
			this.listFormasPagoItems.clear();
			
			if (this.pojoGpoValFormasPago == null || this.pojoGpoValFormasPago.getId() <= 0L)
				return;
			
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
			if (this.listCuentas == null)
				this.listCuentas = new ArrayList<CuentaBancaria>();
			this.listCuentas.clear();
			
			if (this.listCuentasItems == null)
				this.listCuentasItems = new ArrayList<SelectItem>();
			this.listCuentasItems.clear();
			
			this.listCuentas = this.ifzCtas.findLikeClaveNombreCuenta("", 0, this.sucursalesVisibles, null);
			if (this.listCuentas != null && ! this.listCuentas.isEmpty()) {
				for (CuentaBancaria var : this.listCuentas)
					this.listCuentasItems.add(new SelectItem(var.getId() + " - " + var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta(), var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta()));
			}
		} catch (Exception e) {
			log.error("error en cargarCuentas", e);
		}
   	}
   	
	public String evaluaOperacion() {
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
			this.resOperacion = "";
			this.terceros = "";
			this.pojoRegistroGasto.setNoCheque(0);
			this.pojoRegistroGasto.setFolioAutorizacion("");
			this.beneficiario = "";
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en evaluaOperacion", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	private List<PagosGastosDetImpuestosExt> copiaListas(List<PagosGastosDetImpuestosExt> fuente) {
   		List<PagosGastosDetImpuestosExt> resultado = new ArrayList<PagosGastosDetImpuestosExt>();
   		for (PagosGastosDetImpuestosExt pgi : fuente) {
   			resultado.add( ((PagosGastosDetImpuestosExt) pgi.clone()));
   		}
   		return resultado;
   	}
   	
   	public String buscarRet() {
   		try {
   			resOperacion= "";
   			this.listRetEncontradas = ifzConValores.findLikeByProperty(this.campoBusqRet,this.valBusqRet, 5);
   			if (listRetEncontradas.isEmpty() || listRetEncontradas==null)
   				this.resOperacion = this.mensajesInf.get(13);
   		} catch (Exception e) {
   			log.error("Error en metodo buscarRet", e);
   			this.resOperacion = mensajesInf.get(-1);
   		}
   		return "OK";
   	}
   	
	public void mensajeTransaccion() {
		try {
			// Enviamos mensaje a cola de transacciones
			log.info("Enviando Transaccion ... ");
			if (this.pojoRegistroGasto == null || this.pojoRegistroGasto.getId() == null || this.pojoRegistroGasto.getId() <= 0L) {
				log.info("NO se pudo enviar la Transaccion. No selecciono registro de Registro Gasto");
				return;
			}
			
			Respuesta respuesta = this.ifzGtosComp.enviarTransaccion(this.pojoRegistroGasto);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("No se pudo enviar mensaje a la cola de transacciones." + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError(), (Throwable) respuesta.getBody().getValor("exception"));
				control(respuesta.getErrores().getDescError());
				return;
			}
			log.info("Transaccion enviada");
		} catch (Exception e) {
			log.error("error en mensajeTransaccion", e);
			control(true);
		}
	}
	
   	public String cambioBeneficiario() {
		try {
			this.beneficiario = "";
		} catch (Exception e) {			
			log.error("error en cambioBeneficiario", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String validaSaldoCuentaPropios() {
		try {
			control();
			if (totalFacturasReportadas == 0) {
				control(true, 12, this.mensajesInf.get(12));
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
			log.error("Error en el metodo validaSaldoCuentaPropios.", e);
			control(true);
			return "ERROR";
		}
		
		return "OK";
	}

	private String getGeneraNombreProveedor(PersonaExt prov) {
		if (prov == null)
			return "";
		
		return prov.getId() + " - " + prov.getNombre() + " " 
			+ (prov.getPrimerApellido() != null ? prov.getPrimerApellido() : "") + " " 
			+ (prov.getSegundoApellido() != null ? prov.getSegundoApellido() : "");
	}

	public String agregar() {
		PersonaExt proveedor = null;
		Matcher match = null;
		Pattern pat = null;
		
		try {
			control();
			if (! this.isDebug && this.pojoRegistroGasto.getId() != null) { // por si el gasto ya tiene llave no sigan agregando comprobante hasta que hagan clic en nuevo
				log.info("ERROR 10 - " + this.mensajesInf.get(10));				  						
				this.sinFactura = true;
				control(true, 10, this.mensajesInf.get(10));
				return "ERROR";
			}

			this.pojoRegistroGastoComprobado = new PagosGastosDetExt();
			this.conceptoGasto = "";
			this.nombreProveedor = "";
			this.descripcionFactura = "";
			this.observaciones = "";
			this.listDesgloseImpuestos.clear();
			this.listDesgloseRet.clear();
			this.listRetEncontradas.clear();
			this.listRetDelGasto.clear();
			this.totalImpuestos = 0D;
			this.totalRetenciones = 0D;
			this.totalPago = 0D;
			this.subtotal = 0D;
			this.montoTotalFactura = 0D;
			
			proveedor = new PersonaExt();
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.beneficiario);
			if (match.find()) {
				proveedor = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona);
				this.pojoRegistroGastoComprobado.setIdProveedor(proveedor);
				this.pojoRegistroGastoComprobado.setTipoPersonaProveedor(this.tipoPersona);
			}

			this.reg = new FacturaImpuestos();
			this.pojoObra = null;
			obrasBusqueda();

			this.campoBusquedaOrdenCompra = this.listTiposBusquedaOrdenCompra.get(0).getValue().toString();
			this.valorBusquedaOrdenCompra = "";
			this.paginacionOrdenCompra = 1;
			
			if (this.listOrdenCompra == null)
				this.listOrdenCompra = new ArrayList<OrdenCompra>();
			this.listOrdenCompra.clear();
			this.pojoOrdenCompra = new OrdenCompra();
		} catch (Exception e) {
			log.error("Error en agregar", e);
			control(true);
			return "ERROR";
		}
		
		return "OK";
	}

	public void editarFactura() {
		String facturaFolio = "";
		
		if (this.reg.getPojoFactura().getIdXml() == null || this.reg.getPojoFactura().getIdXml() <= 0L)
			this.reg.getPojoFactura().setIdXml(0L);

		this.facturaId = this.reg.getPojoFactura().getIdXml();
		if (this.facturaId.longValue() > 0L) {
			this.facturaRfc = this.ifzComprobadoGto.getFacturaProperty(this.facturaId, "rfcEmisor");
			this.facturaEmisor = this.ifzComprobadoGto.getFacturaProperty(this.facturaId, "nombre");
			facturaFolio = this.ifzComprobadoGto.getFacturaProperty(this.facturaId, "factura");
		}

		this.facturaActualizar = false;
		if ((this.facturaEmisor == null || "".equals(this.facturaEmisor.trim())) || (facturaFolio == null || "".equals(facturaFolio.trim())))
			this.facturaActualizar = true;
		
		this.pojoRegistroGastoComprobado = this.reg.getPojoFactura();
		this.conceptoGasto = this.reg.getPojoFactura().getIdConcepto().getId() +" - "+ this.reg.getPojoFactura().getIdConcepto().getDescripcion();
		//this.conceptoGasto2 = this.conceptoGasto;
		
		if ("".equals("".equals(this.facturaEmisor.trim()))) 
			this.nombreProveedor = this.facturaEmisor;
		else
			this.nombreProveedor = getGeneraNombreProveedor(this.reg.getPojoFactura().getIdProveedor());
		this.descripcionFactura = this.reg.getPojoFactura().getIdConcepto().getDescripcion() + " - " + this.nombreProveedor;
		
		this.montoTotalFactura = this.reg.getPojoFactura().getSubtotal() + this.reg.getPojoFactura().getTotalImpuestos();
		this.subtotal = this.reg.getPojoFactura().getSubtotal();
		this.totalImpuestos = this.reg.getPojoFactura().getTotalImpuestos();
		this.totalRetenciones = this.reg.getPojoFactura().getTotalRetenciones();
		this.totalPago = Utilerias.redondear((this.montoTotalFactura - this.reg.getPojoFactura().getTotalRetenciones()),2);
		this.observaciones = this.reg.getPojoFactura().getObservaciones();
		this.listDesgloseImpuestos = copiaListas(this.reg.getListImpuestos());
	}
	
	public String guardarFactura() {
		boolean encontro = false;			
		boolean  registroNuevo = false;  
		Pattern pat = null;
		Matcher match = null;
		String[] splitted = null;
		String value = "";
		int ban = 0;
		
		try {
			log.info("Guardando Factura");
			control();
			
			if (! "OK".equals(validaMontoFactura()))
				return "ERROR";
			
			if (! "OK".equals(validaMontoImpuestos()))
				return "ERROR";
			
			if (! "OK".equals(validaGrupoImpuestos()))
				return "ERROR";
		
			if (! "OK".equals(validaMontoFacturaContraTotalImpuestos()))
				return "ERROR";
			
			if (this.totalRetenciones <= 0 && ! this.listRetDelGasto.isEmpty()) {
				this.resOperacion = mensajesInf.get(16);
				return "OK";
			}
			
			this.pojoRegistroGastoComprobado.setModificadoPor(this.usuarioId);
			this.pojoRegistroGastoComprobado.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoRegistroGastoComprobado.setFacturado((short) (this.sinFactura ? 0 : 1)); //(short) (this.sinFactura ? 0 : 1)
			this.pojoRegistroGastoComprobado.setIdXml(this.facturaId);
			this.pojoRegistroGastoComprobado.setFacturaRazonSocial(this.facturaEmisor);
			this.pojoRegistroGastoComprobado.setFacturaRfc(this.facturaRfc);

			if (this.pojoRegistroGastoComprobado.getTipoPersonaProveedor() == null || "".equals(this.pojoRegistroGastoComprobado.getTipoPersonaProveedor().trim()))
				this.pojoRegistroGastoComprobado.setTipoPersonaProveedor(this.pojoRegistroGastoComprobado.getIdProveedor().getTipoPersona() == 2L ? "N" : "P");
			
			if (this.facturaActualizar) {
				this.facturaActualizar = false;
				this.ifzComprobadoGto.setFacturaProperty(this.facturaId, "nombre", this.facturaEmisor);
				
				if (this.pojoRegistroGastoComprobado.getReferencia().contains("-")) {
					splitted = this.pojoRegistroGastoComprobado.getReferencia().split("-");

					if (splitted.length > 2) {
						for (int i = 0; i <= (splitted.length - 2); i++)
							value += splitted[i] + "-";
						this.ifzComprobadoGto.setFacturaProperty(this.facturaId, "serie", value);
						
						value = splitted[splitted.length - 1];
						this.ifzComprobadoGto.setFacturaProperty(this.facturaId, "folio", value);
					} else {
						value = splitted[0];
						this.ifzComprobadoGto.setFacturaProperty(this.facturaId, "serie", value);
						
						value = splitted[1];
						this.ifzComprobadoGto.setFacturaProperty(this.facturaId, "folio", value);
					}
				} else {
					this.ifzComprobadoGto.setFacturaProperty(this.facturaId, "serie", this.pojoRegistroGastoComprobado.getReferencia());
				}
			}
			
			pat = Pattern.compile("(^[\\d_\\-\\w]+)(\\s{1}-{1}\\s{1}[\\d\\s\\w]+)");
			//validando sucursal
			/*log.info("Validando sucursal");
			match = pat.matcher(this.sucursal);
			match.find();
			Long agente = Long.valueOf(match.group(1));*/
			
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			//validando gasto
			log.info("Validando gasto");
			match = pat.matcher(this.conceptoGasto);			
			match.find();
			Long concepto = Long.valueOf(match.group(1));
			
			/*if (this.perfilRequiereObra == null && !"S".equals(this.perfilRequiereObra)) {
   				this.listProPagDet = ifzProPagDet.findByAgenteEstatusMontoConcepto(agente.toString(), "G", concepto);
   				if (this.listProPagDet == null) {
   					log.error(mensajesInf.get(19));
   					this.resOperacion = this.mensajesInf.get(19);
   					return "ERROR";
   				}
			}*/
			
			for (Object obj: this.listProPagDet) {
				ProgPagosDetalle ppd = (ProgPagosDetalle)((Object[])obj)[1];
				if (! "R".equals((String)((Object[])obj)[0]) && totalPago.compareTo(ppd.getMonto().doubleValue()) != 0) {
					ban = 3;
				}
				if ("R".equals((String)((Object[])obj)[0]) && totalPago.compareTo(ppd.getMonto().doubleValue()) != 0) {
					ban = 2;
				}
				if (! "R".equals((String)((Object[])obj)[0]) && totalPago.compareTo(ppd.getMonto().doubleValue()) == 0) {
					ban = 1;
				}   						
				if ("R".equals((String)((Object[])obj)[0]) && totalPago.compareTo(ppd.getMonto().doubleValue()) == 0) {
					ban = 0;
					break;
				}
			}
			
			if (ban == 1) {
				log.error(this.mensajesInf.get(21));
				this.resOperacion = this.mensajesInf.get(21);
				return "ERROR";
			}
			
			if (ban == 2) {
				log.error(this.mensajesInf.get(22));
				this.resOperacion = this.mensajesInf.get(22);
				return "ERROR";
			}
			
			if (ban == 3) {
				log.error(this.mensajesInf.get(19));
				this.resOperacion = this.mensajesInf.get(19);
				return "ERROR";
			}
			
			if (this.facturaId == null)
				analizarArchivo();
			control();
			if (this.pojoRegistroGastoComprobado.getId()==null)
				registroNuevo = true;
			
			this.pojoRegistroGastoComprobado.setModificadoPor(this.usuarioId);
			this.pojoRegistroGastoComprobado.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoRegistroGastoComprobado.setIdXml(this.facturaId);
			if (this.pojoRegistroGastoComprobado.getIdObra() == null)
				this.pojoRegistroGastoComprobado.setIdObra(new Obra());
			if (this.pojoRegistroGastoComprobado.getIdOrdenCompra() == null)
				this.pojoRegistroGastoComprobado.setIdOrdenCompra(new OrdenCompra());
			   					
			if (registroNuevo) {
				this.pojoConceptoGtos = this.ifzConValores.findById(concepto);
			} else {
				if (this.pojoConceptoGtos.getId() != concepto)
					this.pojoConceptoGtos = this.ifzConValores.findById(concepto);
			}
					
			this.pojoRegistroGastoComprobado.setIdConcepto(this.pojoConceptoGtos);
			
			//validando proveedor
			/*match = pat.matcher(this.nombreProveedor);			
			if (match.find())
				if (registroNuevo)				
					this.pojoProveedores = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findById(Long.valueOf(match.group(1)));
				else
					if (this.pojoProveedores.getPersonaId()!= Long.valueOf(match.group(1)))
						this.pojoProveedores= this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findById(Long.valueOf(match.group(1)));
			   					
			//this.pojoComprobadoGto.setProveedorId(this.pojoProveedores);
			 */
			  					
			this.pojoRegistroGastoComprobado.setSubtotal(subtotal);
			this.pojoRegistroGastoComprobado.setTotalImpuestos(this.totalImpuestos);
			this.pojoRegistroGastoComprobado.setTotalRetenciones(totalRetenciones);
			this.pojoRegistroGastoComprobado.setObservaciones(this.observaciones);

			log.info("Asignamos ID Registro Gasto");
			this.pojoRegistroGastoComprobado.setIdPagosGastos(this.pojoRegistroGasto); // asignar papa //this.pojoGtosComp.getId();
			
			if (this.pojoRegistroGasto.getId() == null) {
				//si el gasto ya existe guardo las facturas creadas, sino las meto a una lista
				// y las guardare hasta que creen un gasto. Ya que sí el gasto no existe no puedo grabar 
				//facturas hasta que me serciore que creen un gasto. Siempre que el gasto no tenga llave 
				//la factura tampoko tendra llave, por eso no pregunto si es nueva 	
				
				this.pojoRegistroGastoComprobado.setCreadoPor(this.usuarioId);
				this.pojoRegistroGastoComprobado.setFechaCreacion(Calendar.getInstance().getTime());				
				   	   						
				if (! this.listDesgloseImpuestos.isEmpty())
					for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
						var.setId(this.pojoRegistroGastoComprobado.getId());
					}
				
   				if (! this.listDesgloseRet.isEmpty())
					for (PagosGastosDetImpuestosExt var : this.listDesgloseRet) {
						var.setId(this.pojoRegistroGastoComprobado.getId());
					} 	
			} else {
				// si el gasto existe ya puedo grabar facturas 
				if (registroNuevo) {
					this.pojoRegistroGastoComprobado.setCreadoPor(this.usuarioId);
					this.pojoRegistroGastoComprobado.setFechaCreacion(Calendar.getInstance().getTime());

					log.info("Guardo factura en BD");
					this.pojoRegistroGastoComprobado.setId(this.ifzComprobadoGto.save(this.pojoRegistroGastoComprobado));
					log.info("Factura guardada");
					
					if (! this.listDesgloseImpuestos.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
							var.setId(this.pojoRegistroGastoComprobado.getId());
							var.setId(this.ifzDesgloImpto.save(var));
						} 
					}

   					if (! this.listDesgloseRet.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseRet) {
							var.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
							var.setId(this.ifzDesgloImpto.save(var));
						} 
   					}
				} else {
					log.info("Actualizo factura en BD");
					this.ifzComprobadoGto.update(this.pojoRegistroGastoComprobado);
					log.info("Factura actualizada");
					   	   						
					if (! this.listDesgloseImpuestos.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
							var.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
							
							if (var.getId()== null)
								var.setId(this.ifzDesgloImpto.save(var));
							else
								this.ifzDesgloImpto.update(var);
						}
					}
					
					if (! this.listDesgloseRet.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseRet) {
							var.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);

							if (var.getId() == 0L)
								var.setId(this.ifzDesgloImpto.save(var));
							else
								this.ifzDesgloImpto.update(var);
						}
					}
				}
				
				//eliminando los pojos de impuestos de la base de datos,
				// ya que anteriormente solo los elimine de la memoria   					
				if (! this.lisImpuestosEliminados.isEmpty()) {
					for (PagosGastosDetImpuestosExt var : lisImpuestosEliminados) {
						if (var.getId() != 0L)
							this.ifzDesgloImpto.delete(var);
					}
					this.lisImpuestosEliminados.clear();
				}
			}  
			
			reg.setPojoFactura(this.pojoRegistroGastoComprobado);
			reg.setListImpuestos(copiaListas(listDesgloseImpuestos));
			reg.setListRetenciones(copiaListas(listDesgloseRet));
			  					
			encontro = false;
			if (! listFacturaConImpuestos.isEmpty()) {
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					/*if (reg.getPojoFactura().getConceptoId().getId() == var.getPojoFactura().getConceptoId().getId()) {
						Double varmonto = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
						Double regmonto = ((reg.getPojoFactura().getSubtotal() + reg.getPojoFactura().getTotalImpuestos())- reg.getPojoFactura().getTotalRetenciones());
						
						if (varmonto.doubleValue() == regmonto.doubleValue()) {
							encontro = true;
						}
					}*/
					
					if (var.equals(reg)) {
						var = reg;
						encontro = true;
					}
				}
			}
			/*if (perfil == null && !"S".equals(perfil)) {
				if (! listFacturaConImpuestos.isEmpty()) {
					for (FacturaImpuestos var : this.listFacturaConImpuestos) {
						/*if (reg.getPojoFactura().getConceptoId().getValorId().compareTo(var.getPojoFactura().getConceptoId().getValorId()) == 0) {
							Double varmonto = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
							Double regmonto = ((reg.getPojoFactura().getSubtotal() + reg.getPojoFactura().getTotalImpuestos())- reg.getPojoFactura().getTotalRetenciones());
							if (varmonto.doubleValue() == regmonto.doubleValue()) {
								encontro = true;
								this.resOperacion = mensajesInf.get(20);
			   					return "ERROR";
							}
						}* /
						if (reg.getPojoFactura().getConceptoId().getId() == var.getPojoFactura().getConceptoId().getId()) {
							Double varmonto = ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
							Double regmonto = ((reg.getPojoFactura().getSubtotal() + reg.getPojoFactura().getTotalImpuestos())- reg.getPojoFactura().getTotalRetenciones());
							
							if (varmonto.doubleValue() == regmonto.doubleValue()) {
								encontro = true;
								this.resOperacion = mensajesInf.get(20);
			   					return "ERROR";
							}
						}
						
						if (var.equals(reg)) {
							var = reg;
							encontro = true;
						}
					}
				}
		   	}*/
			   					
			if (! encontro)
				this.listFacturaConImpuestos.add(reg);
			
			actualizaTotalFaturas();
			this.facturaDescartada = false;
			this.facturaId = null;
			control(false, -1, "Comprobacion agregada");
   		} catch (Exception e) {
   			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en guardarFactura", e);
			return "ERROR";
		}

		return "OK";
   	} 	
   	
   	public String cancelarRegistroGasto() {
   		Respuesta rep = new Respuesta();
   		
		try {
			rep = this.ifzGtosComp.cancelacion(pojoRegistroGasto, Calendar.getInstance().getTime());
			if (rep.getResultado() == 0) {
				this.pojoRegistroGasto = (PagosGastosExt) rep.getObjeto();
				this.resOperacion = "";
				
				for (PagosGastosExt var : this.listRegistroGastos) {
					if (var.getId().equals(this.pojoRegistroGasto.getId())) {
						var.setEstatus("X");
						break;
					}						
				}
				
				if (perfilRequiereObra == null && !"S".equals(perfilRequiereObra)) {
					List<PagosGastosDet> detalles = this.ifzComprobadoGto.findByProperty("pagosGastosId", this.pojoRegistroGasto);
					List<ProgPagosDetalle> lista = null;
					if (detalles != null && ! detalles.isEmpty()) {
						for (PagosGastosDet var: detalles) {
							Double montoValue = ((var.getSubtotal() + var.getTotalImpuestos())- var.getTotalRetenciones());
							Long conceptoValue = var.getIdConcepto(); //var.getConceptoId().getValorId();
							lista = ifzProPagDet.findByMontoConceptoEstatus(montoValue, conceptoValue, "E");
							if (lista != null) {
								lista.get(0).setEstatus("G");
								lista.get(0).setModificadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
								lista.get(0).setFechaModificacion(Calendar.getInstance().getTime());
								ifzProPagDet.update(lista.get(0));
							}
						}
					}
				}
				
			}else
				this.resOperacion = rep.getRespuesta();
		}catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en el metodo cancelarRegistroGasto.", e);
			return "ERROR";
		}

		return "OK";
	}
   	
   	public String evaluaEditar() {
   		try {
			control();
   			this.sinFactura = false;
			if (this.reg.getPojoFactura().getId() != null) {
				if (! validaRequest("DETEDIT", "1")) { //! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					this.sinFactura = true;
		   			log.info("ERROR 4: No se permite editar las facturas");
		   			control(true, 4, this.mensajesInf.get(4));
					return "ERROR";
		   		}
			}
			
			editarFactura();
			/*if (this.reg.getPojoFactura().getId() != null && ! this.isDebug) {
				this.facturaConLlave = true;
				/ *this.tipoMensaje = 4;
				this.resOperacion = this.mensajesInf.get(4);* /
				log.info("ERROR 4 - Una vez creado el gasto No es posible editar el comprobante");
				control(true, 4, this.mensajesInf.get(4));
				return "ERROR";
			} 
			
			this.facturaConLlave = false;
			this.pojoObra = this.pojoRegistroGastoComprobado.getIdObra();
			if (this.pojoRegistroGastoComprobado.getIdOrdenCompra() != null && this.pojoRegistroGastoComprobado.getIdOrdenCompra().getId() != null && this.pojoRegistroGastoComprobado.getIdOrdenCompra().getId() > 0L)
				this.pojoOrdenCompra = this.pojoRegistroGastoComprobado.getIdOrdenCompra();
			desglosaImpuestos();*/
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en el metodo evaluaEditar.", e);
			return "ERROR";
		}

		return "OK";
   	}
   	
   	public String evaluaEliminar() {
   		try {
   			
			if (reg.getPojoFactura().getId() != null) {
				sinFactura = true;
				this.tipoMensaje = 3;
				resOperacion = this.mensajesInf.get(3);
			} else {
				this.tipoMensaje = 0;
				resOperacion = "";
				sinFactura = false;
			}
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("Error en el metodo evaluaEliminar.", e);
			return "ERROR";
		}
   		
		return "OK";
   	}
   	
   	public String eliminarImpuestoDesglosado() {
		try {

			this.sumImpuestos=0D;
			this.sumRetenciones=0D;
			this.importeImpuesto=0D;
		
			this.lisImpuestosEliminados.add(this.pojoDesgloImpto);
			this.listDesgloseImpuestos.remove(this.pojoDesgloImpto);
			
			if (! this.listDesgloseImpuestos.isEmpty()) {			
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(),2);	
					else	
						if ("1".equals(var.getIdImpuesto().getAtributo4()))
							sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(),2);	
				}
				totalImpuestos=sumImpuestos;
				totalRetenciones = sumRetenciones;
				this.subtotal= Utilerias.redondear( this.montoTotalFactura - sumImpuestos,2);
				this.totalPago = Utilerias.redondear( this.montoTotalFactura - sumRetenciones,2);
				
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("0".equals(var.getIdImpuesto().getAtributo4()) && !"AC".equals(var.getIdImpuesto().getTipoCuenta())) {
						var.setValor(BigDecimal.valueOf(Utilerias.redondear(Utilerias.redondear(this.subtotal *((Double.valueOf(var.getIdImpuesto().getAtributo1())/ 100)),2),2)));
					}
				}
				sumImpuestos=0D;
				sumRetenciones=0D;
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(),2);	
					else	
						sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(),2);	
				}
				totalImpuestos=sumImpuestos;
				totalRetenciones = sumRetenciones;
				this.subtotal= Utilerias.redondear( this.montoTotalFactura - sumImpuestos,2);
				this.totalPago = Utilerias.redondear( this.montoTotalFactura - sumRetenciones,2);
			} else {
				//sabemos que la lista de imptos quedo vacia porque entro a esta opcion y ponemos en cero el total de impuestos
				totalImpuestos=0D;	
				totalRetenciones = 0D;
				this.subtotal = this.montoTotalFactura;
				this.totalPago = this.montoTotalFactura;
			}
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en eliminarImpuestoDesglosado", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String cambioMontoImpuesto() {
		try {
			this.resOperacion = "";
			if (this.montoTotalFactura!=0) {
				this.sumImpuestos=0D;
				this.sumRetenciones=0D;
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getValor() != null) {
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
							sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(),2);
						else
							sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(),2);
					}
				}
				
				totalImpuestos=sumImpuestos;
				totalRetenciones=sumRetenciones;
				totalPago=Utilerias.redondear(montoTotalFactura - totalRetenciones,2);	
				this.subtotal= Utilerias.redondear( this.montoTotalFactura - sumImpuestos,2);	
			}
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en cambioMontoImpuesto", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String actualizaTotalFaturas() {
		BigDecimal totalAux = BigDecimal.ZERO;
		
		try {
			this.totalFacturasReportadas = 0D;
			this.tipoMensaje = 0;
			resOperacion = "";
			
			if (! listFacturaConImpuestos.isEmpty()) {				
				for (FacturaImpuestos var: listFacturaConImpuestos) {
					if (var.getPojoFactura().getSubtotal() == null)
						continue;
					if (var.getPojoFactura().getTotalImpuestos() == null)
						continue;
					if (var.getPojoFactura().getTotalRetenciones() == null)
						continue;
					
					this.totalFacturasReportadas = this.totalFacturasReportadas.doubleValue() + ((var.getPojoFactura().getSubtotal().doubleValue() + var.getPojoFactura().getTotalImpuestos().doubleValue())- var.getPojoFactura().getTotalRetenciones().doubleValue());
					totalAux = new BigDecimal(this.totalFacturasReportadas).setScale(2, BigDecimal.ROUND_HALF_EVEN);
					this.totalFacturasReportadas = totalAux.doubleValue(); //Utilerias.redondear(totalFacturasReportadas, 2);
				}
			}
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en actualizaTotalFaturas", e);
			throw e;
		}
		
		return "OK";
	}
   	
   	public String validaGrupoImpuestos() {
		try {
			int contador =0;
			encontroMismoGrupo=false;
			resOperacion="";
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					contador = 0;
					for (PagosGastosDetImpuestosExt var2 : this.listDesgloseImpuestos) {
						if (! encontroMismoGrupo)
							if (! "0".equals(var2.getIdImpuesto().getAtributo2())) //porque el cero es general esos pueden existir N impuestos al mimos tiempo
								if (var1.getIdImpuesto().getAtributo2().equals(var2.getIdImpuesto().getAtributo2())) {
									contador = contador + 1;
									if (contador == 2) {
										encontroMismoGrupo=true;
										this.tipoMensaje=1;
										resOperacion = this.mensajesInf.get(1);
										return "ERROR";
									}
								}	
					}					
				}
			}
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en validaGrupoImpuestos", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String validaMontoImpuestos() {
   		double porcentaje = 0;
   		
		try {
			this.encontroMismoGrupo=false;
			this.tipoMensaje = 0;
			this.resOperacion = "";
			
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					if (var1.getValor() == null || var1.getValor() == BigDecimal.ZERO) {
						porcentaje = Double.parseDouble(var1.getIdImpuesto().getAtributo1());
						if (porcentaje > 0) {
							this.encontroMismoGrupo = true;
							this.tipoMensaje=5;
							this.resOperacion = this.mensajesInf.get(5);
							return "ERROR";
						}
					}
				}
			}
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en validaMontoImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
   	public String validaMontoFactura() {
		try {
			encontroMismoGrupo=false;
			this.tipoMensaje = 0;
			resOperacion = "";
			
			if (this.montoTotalFactura <= 0) {
				encontroMismoGrupo = true;
				this.tipoMensaje=6;
				resOperacion = this.mensajesInf.get(6);
				return "ERROR";
			}
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en validaMontoFactura", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
	public String validaMontoFacturaContraTotalImpuestos() {
		Double sumaImpuestos = 0D;
		
		try {
			this.encontroMismoGrupo=false;
			this.tipoMensaje = 0;
			this.resOperacion = this.mensajesInf.get(0);
			
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					if (! "AC".equals(var1.getIdImpuesto().getTipoCuenta()))
						sumaImpuestos = sumaImpuestos + var1.getValor().doubleValue();
				}
			}
			
			if (sumaImpuestos > this.montoTotalFactura) {
				this.encontroMismoGrupo = true;
				this.tipoMensaje=7;
				this.resOperacion = this.mensajesInf.get(7);
				return "ERROR";
			}
		} catch (Exception e) {
			this.resOperacion = this.mensajesInf.get(-1);
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

	public void desglosaImpuestos() {
   		List<PagosGastosDetImpuestosExt> listDesgloseImpuestos_tmp = null;
   		double porcentajeImpuesto = 0;
   		double importe = 0;
   		Matcher match = null;
		Pattern pat = null;
   			
   		try {
			control();
			this.importeImpuesto = 0D;
			this.importeRetencion = 0D;
			this.sumImpuestos = 0D;
			this.sumRetenciones = 0D;
			this.subtotal = this.montoTotalFactura;
			this.totalImpuestos = 0D;
			this.totalRetenciones = 0D;
			this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
			this.subtotal = Utilerias.redondear(this.subtotal, 2);
			this.totalPago = Utilerias.redondear(this.totalPago, 2);
   			
   			if (this.sinFactura) 
				this.listDesgloseImpuestos.clear();
			
   			//validando gasto para que el pojo de gasto tenga valor correcto ya que a veces se quedaba con el valor del ultimo gasto que escogieron en otra factura
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.conceptoGasto);
			if (match.find()) {
				this.pojoConceptoGtos = this.ifzConValores.findById(Long.valueOf(match.group(1)));
				this.listImpuestosDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","DE", this.pojoConceptoGtos.getId());
				if (this.listImpuestosDelGasto != null && ! this.listImpuestosDelGasto.isEmpty()) {
					if (this.listDesgloseImpuestos.isEmpty()) {//si viene vacio es posible que hayan cambiado el concepto de gasto o
						// es la primera vez que se cargaran lo imptos al comprobante
						for (GastosImpuestoExt var : this.listImpuestosDelGasto) {
							this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
							this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
							this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
							this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
							this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());

							this.importeImpuesto = 0D;
							porcentajeImpuesto = Double.parseDouble(var.getImpuestoId().getAtributo1().trim());
							if (porcentajeImpuesto > 0) {
								porcentajeImpuesto = 1 + (porcentajeImpuesto / 100);
								this.importeImpuesto = Utilerias.redondear(this.montoTotalFactura - (Utilerias.redondear(this.montoTotalFactura / porcentajeImpuesto, 2)), 2);
							}
							
							this.pojoDesgloImpto.setValor(new BigDecimal(this.importeImpuesto));	
							this.sumImpuestos = Utilerias.redondear(this.sumImpuestos + this.pojoDesgloImpto.getValor().doubleValue(),2);
							this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
						}
						
						this.totalImpuestos = this.sumImpuestos;
						this.subtotal = Utilerias.redondear( this.montoTotalFactura - this.sumImpuestos,2);
						
						// Verifico si existen retenciones para el gasto
						this.listRetDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","AC", this.pojoConceptoGtos.getId());
						if (! this.listRetDelGasto.isEmpty()) {
							for (GastosImpuestoExt var : this.listRetDelGasto) {
								this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
								this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
								this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoRegistroGastoComprobado);
								this.pojoDesgloImpto.setValor(BigDecimal.ZERO);				
								this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
								this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
								this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
								this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());

								this.importeRetencion = 0D;
								porcentajeImpuesto = Double.parseDouble(var.getImpuestoId().getAtributo1().trim());
								if (porcentajeImpuesto > 0) {
									porcentajeImpuesto = 1 + (porcentajeImpuesto / 100);
									this.importeRetencion = Utilerias.redondear(this.montoTotalFactura - (Utilerias.redondear(this.montoTotalFactura / porcentajeImpuesto, 2)), 2);
								}
								//this.impto = Utilerias.redondear(this.subtotalMasImpuestos - (Utilerias.redondear(this.subtotalMasImpuestos / ((Double.valueOf(var.getImpuestoId().getAtributo1())/ 100)+1),2)),2);
								this.pojoDesgloImpto.setValor(new BigDecimal(this.importeRetencion));
								this.sumRetenciones = Utilerias.redondear(this.sumRetenciones + this.pojoDesgloImpto.getValor().doubleValue(),2);
								this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
							}
						}
						
						this.totalRetenciones = Utilerias.redondear(this.sumRetenciones,2);
						this.totalPago		  = Utilerias.redondear(this.montoTotalFactura - totalRetenciones,2);
						return;
					} 
					
					// la lista si tenia elementos y hay que hacer el desglose imptos en base a los imptos que tiene actualmente la lista
					listDesgloseImpuestos_tmp = new ArrayList<PagosGastosDetImpuestosExt>();
					listDesgloseImpuestos_tmp.addAll(this.listDesgloseImpuestos);					
					this.listDesgloseImpuestos.clear();
					
					for (PagosGastosDetImpuestosExt var : listDesgloseImpuestos_tmp) {	
						importe = 0;
						this.importeImpuesto = 0D;
						this.importeRetencion = 0D;
						
						porcentajeImpuesto = Double.parseDouble(var.getIdImpuesto().getAtributo1().trim());
						if (porcentajeImpuesto > 0) 
							porcentajeImpuesto = 1 + (porcentajeImpuesto / 100);
						
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta())) {
							if (porcentajeImpuesto > 0)
								this.importeRetencion = Utilerias.redondear(this.montoTotalFactura - (Utilerias.redondear(this.montoTotalFactura / porcentajeImpuesto, 2)), 2);
							this.sumRetenciones = Utilerias.redondear(this.sumRetenciones + this.importeRetencion, 2);
							importe = this.importeRetencion;
						} else {
							if (porcentajeImpuesto > 0)
								this.importeImpuesto = Utilerias.redondear(this.montoTotalFactura - (Utilerias.redondear(this.montoTotalFactura / porcentajeImpuesto, 2)), 2);
							this.sumImpuestos = Utilerias.redondear(this.sumImpuestos + this.importeImpuesto, 2);
							importe = this.importeImpuesto;
						}

						var.setValor(new BigDecimal(importe));
						this.listDesgloseImpuestos.add(var);
					}

					this.subtotal = Utilerias.redondear(this.montoTotalFactura - this.sumImpuestos, 2); // SUBTOTAL
					this.totalImpuestos = this.sumImpuestos; // IMPUESTOS
					this.totalRetenciones = this.sumRetenciones; // RETENCIONES 
					this.totalPago = Utilerias.redondear(this.montoTotalFactura - this.totalRetenciones, 2); // TOTAL
				} else {
					//porque si no tiene impuestos asociados el gasto, esta propiedad se quedaba NULL y marcaba error
					this.totalImpuestos = 0D;
					this.totalRetenciones = 0D;
					this.subtotal = Utilerias.redondear( this.montoTotalFactura - this.sumImpuestos,2);
					this.totalPago = Utilerias.redondear( this.montoTotalFactura - this.totalRetenciones,2);
				}
			}
		} catch (Exception e) {
			log.error("Error en desglosaImpuesto", e);
			this.resOperacion = this.mensajesInf.get(-1);
		} finally {
			log.info("SUBTOTAL    : " + this.subtotal);
			log.info("IMPUESTOS   : " + this.totalImpuestos);
			log.info("RETENCIONES : " + this.totalRetenciones);
			log.info("TOTAL       : " + this.totalPago);
		}
	}
   	
	public String agregaRet() {
		Double mto_ret; 
		
		try {
			this.resOperacion = "";
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getIdImpuesto().getId() == this.pojoNvaRet.getId()) {
						this.resOperacion = mensajesInf.get(17);
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
		this.resOperacion = "";
		
		try {
			if ("S".equals(this.operacion)) {
				match = pat.matcher(this.beneficiario);
				if (match.find()) {		
					this.pojoTerceros = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1))); // this.ifzPersonas.findById(Long.valueOf(match.group(1)));
					if (this.pojoTerceros != null) {
						if (this.pojoTerceros.getBanco() != null)
							if (this.pojoTerceros.getClabeInterbancaria() != null && !"".equals(this.pojoTerceros.getClabeInterbancaria()))
								//if (this.pojoTerceros.getgetCuentaBancaria() != null && !"".equals(this.pojoTerceros.getCuentaBancaria()))
									//if ("F".equals(this.pojoTerceros.getPersonalidad()))
										if (this.pojoTerceros.getNombre() != null && !"".equals(this.pojoTerceros.getNombre()) && this.pojoTerceros.getPrimerApellido() != null && !"".equals(this.pojoTerceros.getPrimerApellido()))
											return "OK";
										else
											this.resOperacion = "El nombre del Beneficiario no esta completo, verifique !!";
									/*else
										if (this.pojoTerceros.getNombre() != null && !"".equals(this.pojoTerceros.getNombre()))
											return "OK";
										else
											this.resOperacion = "El nombre del Beneficiario no esta completo, verifique !!";*/
								//else
									//this.resOperacion = "El Beneficiario no tiene capturada cuenta bancaria, verifique !!";
							else
								this.resOperacion = "El Beneficiario no tiene capturada cuenta Clabe, verifique !!";
						else
							this.resOperacion = "El Beneficiario no tiene capturado banco, verifique !!";
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
			this.listBeneficiarios = this.ifzPersonas.findLikeClaveNombre("nombre",obj.toString(),this.pojoGpoValPersonas,this.tipoPersona,20,false);	
			return this.listBeneficiarios;
		}
		catch(Exception e) {
			log.error("error en autoacompletaBeneficiario", e);
			return new ArrayList<Persona>();
		}
	}
	
	public List<Persona> autoacompletaProveedor (Object obj) {
		try {
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
			this.resOperacion = this.mensajesInf.get(-1);
			log.error("error en GeneraListaProveedores", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public FacturaImpuestos getReg() {
		return reg;
	}

	public void setReg(FacturaImpuestos reg) {
		this.reg = new FacturaImpuestos();
		this.listDesgloseImpuestos.clear();
		this.listDesgloseRet.clear();
		this.listRetDelGasto.clear();

		this.reg = reg;
		this.pojoRegistroGastoComprobado = this.reg.getPojoFactura();
		this.conceptoGasto = this.reg.getPojoFactura().getIdConcepto().getId() +" - "+ this.reg.getPojoFactura().getIdConcepto().getDescripcion();
		this.nombreProveedor = this.reg.getPojoFactura().getIdProveedor().getId() +" - "+ this.reg.getPojoFactura().getIdProveedor().getNombre(); //this.reg.getPojoFactura().getProveedorId().getPersonaId() +" - "+ this.reg.getPojoFactura().getProveedorId().getNombre() +" "+ (this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() : "") +" "+(this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() : "");
		this.descripcionFactura = this.reg.getPojoFactura().getIdConcepto().getDescripcion() + " - " + this.reg.getPojoFactura().getIdPagosGastos().getBeneficiario(); // + " - "+this.reg.getPojoFactura().getProveedorId().getNombre()+" " + (this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() : "")+" " +(this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() :"");
		this.montoTotalFactura = Utilerias.redondear((this.reg.getPojoFactura().getSubtotal() + this.reg.getPojoFactura().getTotalImpuestos()), 2);
		this.totalPago = Utilerias.redondear((this.montoTotalFactura - this.reg.getPojoFactura().getTotalRetenciones()), 2);
		this.subtotal = this.reg.getPojoFactura().getSubtotal();
		this.totalImpuestos = this.reg.getPojoFactura().getTotalImpuestos();
		this.totalRetenciones = this.reg.getPojoFactura().getTotalRetenciones();
		this.observaciones = this.reg.getPojoFactura().getObservaciones();
		
		if (this.reg.getListImpuestos() != null && ! this.reg.getListImpuestos().isEmpty())
			this.listDesgloseImpuestos = copiaListas(reg.getListImpuestos());
		if (this.reg.getListRetenciones() != null && ! this.reg.getListRetenciones().isEmpty())
			this.listDesgloseRet = copiaListas(reg.getListRetenciones());
		
		if ((this.listDesgloseImpuestos == null || this.listDesgloseImpuestos.isEmpty()) && (this.listDesgloseRet == null || this.listDesgloseRet.isEmpty()))
			desglosaImpuestos();
		/*else
			this.conceptoGasto2 = this.conceptoGasto;*/
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
				listEmpleado = this.ifzEmpleado.findByProperty("email", this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo(), 0);
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
		control(false, 0 , "");
	}

	private void control(boolean operacionCancelada) {
		if (operacionCancelada)
			control(true, 1, "ERROR");
		else
			control(false, 0 , "");
	}

	private void control(String mensaje) {
		control(true, -1, mensaje);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.encontroMismoGrupo = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.resOperacion = mensaje;
	}

   	// ---------------------------------------------------------------------------
	// CONCEPTOS
   	// ---------------------------------------------------------------------------
	
	public void nuevaBusquedaConceptos() {
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
   		this.valorBusquedaConceptos = "";
   		this.pagBusquedaConceptos = 1;
   		this.avanzadaConceptos = false;
   		
   		if (this.listConceptoGasto == null)
   			this.listConceptoGasto = new ArrayList<ConValores>();
   		this.listConceptoGasto.clear();
   		this.pojoConcepto = new ConValores();
	}
	
	public void buscarConceptos() {
		try {
			control();
			if (this.campoBusquedaConceptos == null || "".equals(this.campoBusquedaConceptos))
				this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
			
			this.listConceptoGasto = this.ifzConValores.findLikeProperty(this.campoBusquedaConceptos, this.valorBusquedaConceptos, this.pojoGpoValGasto, 0);
			if (this.listConceptoGasto == null || this.listConceptoGasto.isEmpty()) {
				log.info("ERROR 13: Busqueda sin resultados");
				control(this.mensajesInf.get(13));
			}
		} catch (Exception e) {
			log.error("ERROR al ejecutar buscarConceptos", e);
			control("Ocurrio un problema al buscar los Conceptos para Gastos");
		}
	}
	
	public void seleccionarConcepto() {
		try {
			control();
			if (this.pojoConcepto == null || this.pojoConcepto.getId() <= 0L) {
				log.info("ERROR INTERNO: No se pudo recuperar el Concepto seleccionado");
				control("Ocurrio un problema al intentar recuperar el Concepto seleccionado");
				return;
			}
			
			this.conceptoGasto = this.pojoConcepto.getId() + " - " + this.pojoConcepto.getDescripcion();
			desglosaImpuestos();
		} catch (Exception e) {
			log.error("ERROR al ejecutar buscarConceptos", e);
			control("Ocurrio un problema al buscar los Conceptos para Gastos");
		} finally {
			nuevaBusquedaConceptos();
		}
	}

	public void toggleAvanzadaConceptos() {
		this.avanzadaConceptos = ! this.avanzadaConceptos;
	}
	
	// ------------------------------------------------------------------------------------------
	// CARGA DE ARCHIVO
	// ------------------------------------------------------------------------------------------
	
	public void nuevaCarga() {
		control();
		this.fileSrc = null;
	}
	
	public void uploadListener(UploadEvent event) throws Exception {
		UploadItem item = null;
		
		try {
			control();
			item = event.getUploadItem();
			this.fileSrc = ((item.isTempFile()) ? Files.readAllBytes(item.getFile().toPath()) : item.getData());
		} catch (Exception e) {
			log.error("Error en uploadListener", e);
			control(true);
		}
	}
	
	public void analizarArchivo() throws Exception {
		boolean emisorDesconocido = false;
		List<Persona> listProveedores = null;
		PersonaExt pojoProveedor = null;
		String tipoPersona = "";
		Respuesta respuesta = new Respuesta();
		String facturaFolio = "";
		String fileName = "";
		
		try {
			control();
			reiniciaFactura();
			if (this.fileSrc == null) {
				log.warn("VALIDACION - No ha especificado ninguna factura (*.xml)");
				control("No ha especificado ninguna factura (*.xml)");
				return;
			} 
			
			this.ifzComprobadoGto.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzComprobadoGto.cargaFacturaXML(this.fileSrc, this.pojoRegistroGasto.getId());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				reiniciaFactura();
				log.warn("ERROR INTERNO - Ocurrio un problema al intentar guardar la factura. " + respuesta.getErrores().getDescError());
				control("No se guardo la factura. " + respuesta.getErrores().getDescError());
				return;
			}
			
			// recupero los datos de la factura cargada
			this.facturaId = (Long) respuesta.getBody().getValor("idComprobante");
			this.facturaRfc = respuesta.getBody().getValor("comprobanteRfc").toString();
			
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

			this.pojoRegistroGastoComprobado.setTipoPersonaProveedor(tipoPersona);
			this.pojoRegistroGastoComprobado.setFacturaRazonSocial(this.facturaEmisor);
			this.pojoRegistroGastoComprobado.setFacturaRfc(this.facturaRfc);
			this.pojoRegistroGastoComprobado.setFacturaSerie(respuesta.getBody().getValor("comprobanteSerie").toString());
			this.pojoRegistroGastoComprobado.setFacturaFolio(respuesta.getBody().getValor("comprobanteFolio").toString());
			this.pojoRegistroGastoComprobado.setReferencia(facturaFolio);
			this.montoTotalFactura = Double.parseDouble(respuesta.getBody().getValor("comprobanteTotal").toString());
			
			// Compruebo el ID de la factura cargada
			if (this.facturaId == null || this.facturaId <= 0L) {
				reiniciaFactura();
				log.warn("ERROR INTERNO - Ocurrio un problema al intentar guardar la factura");
				control("Ocurrio un problema al intentar guardar la factura");
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
			if (! this.ifzFtp.putArchivo(this.fileSrc, this.ftpDigitalizacionRuta + fileName)) {
				log.warn("ERROR FTP - Ocurrio un problema al intentar guardar la factura en el servidor");
				control("Ocurrio un problema al intentar guardar la factura en el servidor");
			}
			log.info("FTP - Carga completa");
			if (emisorDesconocido)
				control(true, -1, "El Emisor de la Factura no existe en Negocios/Personas. RFC " + this.facturaRfc);
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.CajaChicaAction.analizarArchivo()", e);
			control(true, 1, "No ha especificado ninguna factura (*.xml)");
		} finally {
			desglosaImpuestos();
		}
	}

	public void descargarArchivo() {
		String fileName = "";
		
		try {
			control();
			if (this.reg == null) {
				log.error("ERROR 20 - No tiene asignada ninguna factura");
				control(true, 20, "No ha especificado ninguna factura (*.xml)");
				return;
			}

			if (this.reg.getPojoFactura().getIdXml() == null || this.reg.getPojoFactura().getIdXml() <= 0L)
				this.reg.getPojoFactura().setIdXml(0L);

			this.facturaId = this.reg.getPojoFactura().getIdXml();
			if (this.facturaId == null || this.facturaId <= 0L) {
				log.error("ERROR 20 - No tiene asignada ninguna factura");
				control(true, 20, "No ha especificado ninguna factura (*.xml)");
				return;
			}

			this.facturaEmisor = this.ifzComprobadoGto.getFacturaProperty(this.facturaId, "nombre");
			if (this.facturaEmisor == null || "".equals(this.facturaEmisor.trim()))
				this.facturaEmisor = "Emisor desconocido";

			this.ftpDigitalizacionHost = this.entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = this.entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = this.entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = this.entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta");
			
			if (validaRequest("FTP_LOCAL", "1")) {
				this.ftpDigitalizacionUser = "guess";
				this.ftpDigitalizacionPass = "guess";
				this.ftpDigitalizacionPort = "22";
				log.info("FTP ... " + this.ftpDigitalizacionHost + ":" + this.ftpDigitalizacionPort + ":" + this.ftpDigitalizacionUser + ":" + this.ftpDigitalizacionPass);
			}
			 
			// Inicializamos variables de archivos y recuperamos el archivo
			fileName = this.prefijoFacturas + this.facturaId + ".xml";
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			this.fileSrc = this.ifzFtp.getArchivo(this.ftpDigitalizacionRuta + fileName);
			if (this.fileSrc == null || this.fileSrc.length <= 0) {
				log.error("ERROR 21 - No se encontro el archivo en el servidor");
				control(true, 21, "No se encontro el archivo en el servidor");
				return;
			}

			// Ponemos los datos en session
			this.httpSession.setAttribute("nombreDocumento", fileName);
			this.httpSession.setAttribute("formato", "xml");
			this.httpSession.setAttribute("contenido", this.fileSrc);
		} catch (Exception e) {
    		log.error("Error en CuentasPorPagar.CajaChicaAction.descargarArchivo :: ERROR 9 ? :(", e);
    		control(true, 9, "");
		}
	}

	public void borrarXML() {
		try {
			control();
			if (this.facturaDescartada && this.facturaId != null && this.facturaId > 0L) {
				log.info("Eliminando XML " + this.facturaId);
	    		this.ifzComprobadoGto.eliminarFactura(this.facturaId);
			}
		} catch (Exception e) {
    		log.error("Error en CuentasPorPagar.CajaChicaAction.borrarXML :: No pude borrar el XML " + this.facturaId + " cargado previamentes y que no se uso :(", e);
    		control("No pude borrar el XML " + this.facturaId + " cargado previamentes y que no se uso");
		}
	}
	
	private void getFTPData() {
		this.ftpDigitalizacionHost = this.entornoProperties.getString("ftp.digitalizacion.host");
		this.ftpDigitalizacionPort = this.entornoProperties.getString("ftp.digitalizacion.port");
		this.ftpDigitalizacionUser = this.entornoProperties.getString("ftp.digitalizacion.user");
		this.ftpDigitalizacionPass = this.entornoProperties.getString("ftp.digitalizacion.password");
		this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta");
	}

	private void reiniciaFactura() {
		this.facturaId = 0L;
		this.facturaRfc = "";
		this.facturaEmisor = "";
		this.facturaDescartada = true;
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

	// ------------------------------------------------------------------------------------------
	// BUSQUEDA OBRAS
	// ------------------------------------------------------------------------------------------
	
	public void obrasBusqueda() {
		control();
		
		// Inicializaciones
		this.campoBusquedaObra = this.tipoBusquedaObra.get(0).getValue().toString();
		this.valTipoBusquedaObra = "";
		this.tipoObraBusquedaObra = (int) this.listObrasTiposItems.get(0).getValue();
		this.avanzadaObra = false;
		this.paginacionObras = 1;
		
		if (this.listObras == null)
			this.listObras = new ArrayList<Obra>();
		this.listObras.clear();
		
		if (this.pojoObra == null)
			this.pojoObra = new Obra();
	}

	public String buscarObras() {
		try {
			control();
			this.paginacionObras = 1;
			if (this.listObras != null)
				this.listObras.clear();

			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valTipoBusquedaObra, this.tipoObraBusquedaObra);
			if (this.listObras == null || this.listObras.isEmpty()) {
				log.info("ERROR 13 - Busqueda sin resultados");
				control(mensajesInf.get(13));
			}
		} catch (Exception e) {
			log.error("Error en buscar obras", e);
			control(true);
		} finally {
			if (this.listObras == null)
				this.listObras = new ArrayList<Obra>();
			log.info(this.listObras.size() + " Obras encontradas");
		}
		
		return "OK";
	}

	public void seleccionarObra() {
		try {
			control();
			this.pojoRegistroGasto.setIdObra(this.pojoObra);
			obrasBusqueda();
			
			this.pojoOrdenCompra = null;
			ordenCompraBusqueda();
		} catch (Exception e) {
			log.error("error en seleccionarObra", e);
			control(true);
		}
	}
		
	public String cambioObra() {
		try {
			this.tipoObra = 1;
		} catch (Exception e) {
			log.error("error en cambioObra", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public void toggleAvanzadaObra() {
		this.avanzadaObra = ! this.avanzadaObra;
	}

	// ------------------------------------------------------------------------------------------
	// BUSQUEDA ORDEN DE COMPRA
	// ------------------------------------------------------------------------------------------
	
	public void ordenCompraBusqueda() {
		control();
		// Inicializaciones
		this.campoBusquedaOrdenCompra = this.listTiposBusquedaOrdenCompra.get(0).getValue().toString();
		this.valorBusquedaOrdenCompra = "";
		this.busquedaAvanzadaOrdenCompra = false;
		this.paginacionOrdenCompra = 1;
		
		if (this.listOrdenCompra == null)
			this.listOrdenCompra = new ArrayList<OrdenCompra>();
		this.listOrdenCompra.clear();
		
		if (this.pojoOrdenCompra == null)
			this.pojoOrdenCompra = new OrdenCompra();
		
		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
			this.encontroMismoGrupo = true;
			this.tipoMensaje = 1;
			this.resOperacion = "Debe seleccionar una Obra.";
		}
	}
	
	public void ordenCompraBuscar() {
		HashMap<String, String> params = new HashMap<String, String>();
		
		try {
			control();
			if ("".equals(this.campoBusquedaOrdenCompra))
				this.campoBusquedaOrdenCompra = this.listTiposBusquedaOrdenCompra.get(0).getValue().toString();//this.opcionesBusquedaOrdenCompra.get(0);
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				log.info("ORDEN DE COMPRA - Debe seleccionar una Obra.");
				control("Debe seleccionar una Obra.");
				return;
			}	
			
			params.put("idObra", this.pojoObra.getId().toString());
			if (this.valorBusquedaOrdenCompra != null && ! "".equals(this.valorBusquedaOrdenCompra.trim()))
				params.put(this.campoBusquedaOrdenCompra, this.valorBusquedaOrdenCompra);
			
			this.listOrdenCompra = this.ifzOrdenCompra.findLikeProperties(params, 0);
			if (this.listOrdenCompra == null || this.listOrdenCompra.isEmpty()) {
				log.info("ORDEN DE COMPRA - Busqueda sin resultados");
				control(mensajesInf.get(13));
			}
		} catch (Exception e) {
			log.error("ORDEN DE COMPRA - Error al buscar", e);
			control(true);
		}
	}
	
	public void ordenCompraSeleccionar() {
		// Asignamos la orden de compra al REGISTRO DE GASTO, si corresponde
		if (this.pojoOrdenCompra != null && this.pojoOrdenCompra.getId() != null && this.pojoOrdenCompra.getId() > 0L) {
			this.pojoRegistroGastoComprobado.setIdObra(this.pojoObra);
			this.pojoRegistroGastoComprobado.setIdOrdenCompra(this.pojoOrdenCompra);
		}
	}

	public void toggleBusquedaAvanzadaOrdenCompra() {
		this.busquedaAvanzadaOrdenCompra = ! this.busquedaAvanzadaOrdenCompra;
	}
	
	// ------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------------

   	public String getTituloComprobacion() {
   		if (this.pojoRegistroGasto.getId() != null && this.pojoRegistroGasto.getId() > 0L)
   			return "Registro Gasto " + this.pojoRegistroGasto.getId();
   		return "Nuevo Registro Gasto";
   	}
   	
   	public void setTituloComprobacion(String value) { }

	public String getFacturaDescripcion() {
		if (this.facturaId != null && this.facturaId > 0L) {
			if (this.facturaEmisor != null && ! "".equals(this.facturaEmisor.trim()) && ! "Emisor desconocido".equals(this.facturaEmisor.trim()))
				return this.facturaId + " - " + this.facturaRfc;
			return this.facturaId.toString();
		}
		
		return "Seleccione factura";
	}
	
	public void setFacturaDescripcion(String value) {}

	public String getOrdenCompraDescripcion() {
		if (this.pojoOrdenCompra != null && this.pojoOrdenCompra.getId()!= null && this.pojoOrdenCompra.getId()> 0L)
			return this.pojoOrdenCompra.getFolio() + " - " + this.pojoOrdenCompra.getNombreProveedor() + " - $ " + String.format("%1$,.2f", this.pojoOrdenCompra.getTotal());
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
		return this.pojoRegistroGastoComprobado.getReferencia() != null ? this.pojoRegistroGastoComprobado.getReferencia() : "";
	}

	public void setReferencia(String referencia) {
		this.pojoRegistroGastoComprobado.setReferencia(referencia);
	}

	public double getSubtotal() {
		return this.pojoRegistroGastoComprobado.getSubtotal() != null ? this.pojoRegistroGastoComprobado.getSubtotal() : 0;
	}

	public void setSubtotal(double subtotal) {
		this.pojoRegistroGastoComprobado.setSubtotal(subtotal);
	}
	
	public Date getFechaCompruebaGto() {
		if (this.pojoRegistroGastoComprobado.getFecha()!= null)
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
	
	public Long getPagosGastosDetId() {
		return this.pojoRegistroGastoComprobado.getId() != null ? this.pojoRegistroGastoComprobado.getId() : 0;
	}

	public void setPagosGastosDetId(Long pagosGastosDetId) {
		this.pojoRegistroGastoComprobado.setId(pagosGastosDetId);
	}
	
	public Double getTotalImpuestos() {
		return this.pojoRegistroGastoComprobado.getTotalImpuestos() != null ? this.pojoRegistroGastoComprobado.getTotalImpuestos() : 0;
	}

	public void setTotalImpuestos(Double totalImpuestos) {
		this.pojoRegistroGastoComprobado.setTotalImpuestos(totalImpuestos);
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

	public PagosGastosExt getPojoGtosComp() {
		return pojoRegistroGasto;
	}

	public void setPojoGtosComp(PagosGastosExt pojoGtosComp) {
		this.pojoRegistroGasto = pojoGtosComp;
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
		return this.pojoRegistroGasto.getId() != null ? this.pojoRegistroGasto.getId() : 0;
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

	public long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
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

	public Double getTotalMenosImpuestos() {
		return subtotal;
	}

	public void setTotalMenosImpuestos(Double totalMenosImpuestos) {
		this.subtotal = totalMenosImpuestos;
	}

	public Double getTotalFacturasReportadas() {
		return totalFacturasReportadas;
	}

	public void setTotalFacturasReportadas(Double totalFacturasReportadas) {
		this.totalFacturasReportadas = totalFacturasReportadas;
	}

	public Double getImpto() {
		return importeImpuesto;
	}

	public void setImpto(Double impto) {
		this.importeImpuesto = impto;
	}

	public Double getSum_impto() {
		return sumImpuestos;
	}

	public void setSum_impto(Double sum_impto) {
		this.sumImpuestos = sum_impto;
	}

	public boolean isEncontroMismoGrupo() {
		return encontroMismoGrupo;
	}

	public void setEncontroMismoGrupo(boolean encontroMismoGrupo) {
		this.encontroMismoGrupo = encontroMismoGrupo;
	}

	public String getConceptoGasto() {
		return conceptoGasto;
	}

	public void setConceptoGasto(String conceptoGasto) {
		this.conceptoGasto = conceptoGasto;
	}
	
	public Double getSubtotalMasImpuestos() {
		return montoTotalFactura;
	}

	public void setSubtotalMasImpuestos(Double subtotalMasImpuestos) {
		this.montoTotalFactura = subtotalMasImpuestos;
		this.totalPago = subtotalMasImpuestos;
	}

	public String getDescripcionFactura() {
		return descripcionFactura;
	}

	public void setDescripcionFactura(String descripcionFactura) {
		this.descripcionFactura = descripcionFactura;
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
		this.beneficiario="";
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
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setTipoBusqueda(String [] tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String [] getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setListGastos(List<PagosGastosExt> listGastos) {
		this.listRegistroGastos = listGastos;
	}

	public List<PagosGastosExt> getListGastos() {
		return listRegistroGastos;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public Double getTotImpuestos() {
		return totalImpuestos;
	}

	public void setTotImpuestos(Double totImpuestos) {
		this.totalImpuestos = totImpuestos;
	}

	public Double getImptoRet() {
		return importeRetencion;
	}

	public void setImptoRet(Double imptoRet) {
		this.importeRetencion = imptoRet;
	}

	public Double getSum_imptoRet() {
		return sumRetenciones;
	}

	public void setSum_imptoRet(Double sum_imptoRet) {
		this.sumRetenciones = sum_imptoRet;
	}

	public List<GastosImpuestoExt> getListRetDelGasto() {
		return listRetDelGasto;
	}

	public void setListRetDelGasto(List<GastosImpuestoExt> listRetDelGasto) {
		this.listRetDelGasto = listRetDelGasto;
	}

	public boolean isExisteRetencion() {
		return existeRetencion;
	}

	public void setExisteRetencion(boolean existeRetencion) {
		this.existeRetencion = existeRetencion;
	}

	public List<PagosGastosDetImpuestosExt> getListDesgloseRet() {
		return listDesgloseRet;
	}

	public void setListDesgloseRet(List<PagosGastosDetImpuestosExt> listDesgloseRet) {
		this.listDesgloseRet = listDesgloseRet;
	}

	public Double getTotRetenciones() {
		return totalRetenciones;
	}

	public void setTotRetenciones(Double totRetenciones) {
		this.totalRetenciones = totRetenciones;
	}

	public Double getTotPago() {
		return totalPago;
	}

	public void setTotPago(Double totPago) {
		this.totalPago = totPago;
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

	/*public List<Area> getListAreas() {
		return listAreas;
	}

	public void setListAreas(List<Area> listAreas) {
		this.listAreas = listAreas;
	}

	public String getSegmento() {
		return segmento;
	}

	public void setSegmento(String segmento) {
		this.segmento = segmento;
	}

	public boolean isMuestraSeg() {
		return muestraSeg;
	}

	public void setMuestraSeg(boolean muestraSeg) {
		this.muestraSeg = muestraSeg;
	}

	public Long getSegNeg() {
		return segNeg;
	}

	public void setSegNeg(Long segNeg) {
		this.segNeg = segNeg;
	}*/

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
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public String getObraNombre() {
   		if (this.pojoObra != null && this.pojoObra.getNombre() != null && ! "".equals(this.pojoObra.getNombre()))
   	   		return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
   		return "";
	}
	
   	public void setObraNombre(String value) { }
   	
   	public int getObraNombreLength() {
   		if ("".equals(getObraNombre()))
   			return 0;
   		
		if (getObraNombre().length() <= 3)
			return 1;   		
		else if (getObraNombre().length() <= 10)
			return getObraNombre().length();
		else
			return getObraNombre().length() - 2;
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

	public String getFacturaEmisor() {
		return facturaEmisor;
	}

	public void setFacturaEmisor(String facturaEmisor) {
		this.facturaEmisor = facturaEmisor;
	}
}
