package net.giro.cxp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.TipoObraAutorizadas;
import net.giro.adp.beans.TipoObraJerarquia;
import net.giro.adp.beans.TipoObraRevisadas;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaNegocioVista;
import net.giro.clientes.logica.PersonaNegocioVistaRem;
import net.giro.clientes.logica.PersonaRem;
import net.giro.cxp.beans.CtasBancoExt;
import net.giro.cxp.beans.FacturaImpuestos;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.TiposGastoCXP;
import net.giro.cxp.logica.GastosImpuestoRem;
import net.giro.cxp.logica.PagosGastosDetImpuestosRem;
import net.giro.cxp.logica.PagosGastosDetRem;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.Utilerias;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.CuentasBancariasRem;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class CajaChicaAction implements Serializable {
	private static Logger log = Logger.getLogger(CajaChicaAction.class);
	private static final long serialVersionUID = 1L;
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
   	private HttpSession httpSession;
	private InitialContext ctx;
	// INTERFACES
	private PagosGastosRem ifzCajaChica;
	private PagosGastosDetRem ifzCajaChicaDetalles;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private CuentasBancariasRem ifzCtas;
	private GastosImpuestoRem ifzGastoImpuesto;
	private PagosGastosDetImpuestosRem ifzDesgloImpto;
	private PersonaRem ifzPersonas;
	private SucursalesRem ifzSucursal;	
	private ReportesRem	ifzReportes;
	private ObraRem ifzObras;
	private EmpleadoRem ifzEmpleado;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	// POJO's
	private PagosGastos pojoCaja;
	private PagosGastosExt pojoCajaChica;
	private ConGrupoValores pojoGpoValPersonas;
	private PersonaExt pojoBeneficiarios;
	private CtasBancoExt pojoCtas;
	private PagosGastosDetExt pojoComprobadoGto;
	private ConValores pojoConceptoGtos;
	private PagosGastosDetImpuestosExt pojoDesgloImpto;
	private PersonaExt pojoProveedores;
	private ConGrupoValores pojoGpoValGasto;
	private ConValores pojoNvaRet;
	private Obra pojoObra;
	// LISTAS
	private List<PagosGastos> listCajaChica;
	private List<PagosGastosDetImpuestosExt> lisImpuestosEliminados;
	private List<PagosGastosDetImpuestosExt> listDesgloseImpuestos;
	private List<PagosGastosDetImpuestosExt> listDesgloseRetenciones;
	private List<GastosImpuestoExt>	listImpuestosDelGasto;
	private List<GastosImpuestoExt>	listRetDelGasto;
	private List<Persona> listBeneficiarios;
	private List<CuentaBancaria> listCuentas;
	private List<SelectItem> listCuentasItems;
	private List<Persona> listProveedores;
	private List<FacturaImpuestos> listFacturaConImpuestos;
	private List<FacturaImpuestos> listFacturaConImpuestosEliminadas;
	private List<ConValores> listRetEncontradas;
	private List<PagosGastosDetExt> listFacturas;
	private List<Sucursal> listSucursales;
	private List<SelectItem> listSucursalesItems;
	private List<Obra> listObras;
	private Long valGpoGastos;
	private String[] listBusqRet;
	private FacturaImpuestos reg; 
	private Double subtotal; // SUBTOTAL (COMPROBACION)
	private Double totalImpuestos; // TOTAL IMPUESTOS (COMPROBACION)
	private Double totalRetenciones; // TOTAL RETENCIONES (COMPROBACION)
	private Double montoTotalFactura; // TOTAL PAGO (COMPROBACION)
	private Double totalPago; // TOTAL PAGO (COMPROBACION)
	private Double totalFacturasReportadas;
	private String descripcionFactura;
	private String conceptoGasto;
	private String conceptoGastoPrevio;
	private String tipoPersona;
	private String nombreProveedor;	
	private String nombreBeneficiario;
	private String beneficiario;		
	private String cuenta;	
	private String mensaje;
	private String sucursalesVisibles;
	private String msgCancelar;
	private String deseaCancelar;
	private String valBusqRet;
	private String campoBusqRet;
	private String varOper;
	private String observaciones;
	private int tipoCajaChica;
	private boolean sinFactura;
	private boolean vobo;
	private boolean autorizado;
	// BUSQUEDA PRINCIPAL
	private List<SelectItem> tiposBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
	// BUSQUEDA DE OBRAS
	private List<SelectItem> tiposBusquedaObra;
	private String campoBusquedaObra;
	private String valorBusquedaObra;
	private String tipoObra;
	private int tipoObraBusquedaObra;
	private int paginacionObras;
	private boolean avanzada;
	// BUSQUEDA PERSONA/NEGOCIO
	private PersonaNegocioVistaRem ifzPerNeg;
	private List<PersonaNegocioVista> listPersonasNegocios;
	private PersonaNegocioVista pojoPersonaNegocio;
	private List<SelectItem> tiposBusquedaPersonaNegocio;
	private String campoBusquedaPersonaNegocio;
	private String valorBusquedaPersonaNegocio;
	private int paginacionBusquedaPersonaNegocio;
	// BUSQUEDA CONCEPTOS
	private List<ConValores> listConceptoGasto;
	private ConValores pojoConcepto;
	private List<SelectItem> tiposBusquedaConceptos;
	private String campoBusquedaConceptos;
	private String valorBusquedaConceptos;
	private int pagBusquedaConceptos;
	private boolean avanzadaConceptos;
	// VARIABLES DE SISTEMA
	private Long usuarioId;  
	private String usuario;  
	private Long sucUsuario;  
	private boolean cambioMonto;
	private boolean encontroMismoGrupo;
	private boolean facturaConLlave;
	private boolean muestraAgregar;
   	private int numPagina;
   	private int numPaginaFact;
   	private int tipoMensaje;
   	private int tipoReporte;
	private boolean comprobacionGenerada;
	private boolean existeRetencion;
	// Carga XML
	private FtpRem ifzFtp;
	private byte[] fileSrc;
	private String fileName;
	private boolean fileLoaded;
	private Long facturaId;
	private String facturaRfc;
	private String facturaEmisor;
	private String facturaMoneda;
	private Double facturaTipoCambio;
	private Double facturaDescuento;
	private Double facturaSubtotal;
	private Double facturaTotal;
	private boolean facturaActualizar;
	private String prefijoFacturas;
	private String ftpDigitalizacionHost;
	private String ftpDigitalizacionPort;
	private String ftpDigitalizacionUser;
	private String ftpDigitalizacionPass;
	private String ftpDigitalizacionRuta;
	// PERFILES
	private boolean PERFIL_CAJA_CHICA_ABONOS;
	private String perfilRequiereObra;
	private double perfilLimiteCajaChica;
	private String perfilCajaChicaVoBo;
	private String perfilCajaChicaAutorizacion;
	private String perfilDividirCajaChica;
	private boolean perfilEgresos;
	private ConGrupoValores pojoGpoValFormasPago; 
	private List<ConValores> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	private boolean reqReferencia;
	private boolean perfilPermiteEditar;
	// SUGERENCIAS
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalSugerida;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public CajaChicaAction() {
		FacesContext facesContext = null;
		Application app = null;
		ValueExpression ve = null;
		PropertyResourceBundle tyg = null;
		
		try {
			facesContext = FacesContext.getCurrentInstance();
			app = facesContext.getApplication();
			
	        this.httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
			
			ve = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(facesContext.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId(); 
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			ve = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(facesContext.getELContext());
			
			ve = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			tyg = (PropertyResourceBundle) ve.getValue(facesContext.getELContext());
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : facesContext.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// Variables para la subida de archivos
			this.ftpDigitalizacionHost = entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = entornoProperties.getString("ftp.digitalizacion.ruta.cxp");
			this.prefijoFacturas = "CXP-CC-"; 
	   		
	   		this.pojoGpoValPersonas = new ConGrupoValores();
	   		this.pojoCajaChica = new PagosGastosExt();
	   		this.pojoCtas = new CtasBancoExt();
	   		this.pojoBeneficiarios= new PersonaExt();
	   		this.pojoConceptoGtos = new ConValores();
	   		this.pojoComprobadoGto = new PagosGastosDetExt();
	   		this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
	   		this.pojoConceptoGtos = new ConValores();
	   		this.pojoProveedores = new PersonaExt();
	   		this.pojoGpoValGasto = new ConGrupoValores();
	   		this.pojoNvaRet = new ConValores();		
	   		this.reg = new FacturaImpuestos();
			this.listCajaChica = new ArrayList<PagosGastos>();
			this.listCuentas = new ArrayList<CuentaBancaria>();
			this.listRetEncontradas = new ArrayList<ConValores>();
			this.listRetDelGasto = new ArrayList<GastosImpuestoExt>();		
			this.listDesgloseRetenciones= new ArrayList<PagosGastosDetImpuestosExt>();
			this.listBeneficiarios = new ArrayList<Persona>();
			this.lisImpuestosEliminados = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listImpuestosDelGasto = new ArrayList<GastosImpuestoExt>();		
			this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>();
			this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.listFacturas = new ArrayList<PagosGastosDetExt>();
			this.listSucursales = new ArrayList<Sucursal>();
			this.listSucursalesItems = new ArrayList<SelectItem>();
			this.encontroMismoGrupo = false;
			this.tipoCajaChica = 0;
			this.tipoMensaje = 0;
			this.mensaje = "";
			this.numPagina = 1;
			this.numPaginaFact = 1;
			this.tipoReporte = 1;
			this.tipoPersona = "P";
			this.cuenta = "";
			this.beneficiario = "";
			this.facturaConLlave = false;
			this.existeRetencion = false;
			this.subtotal = 0D;
			this.totalImpuestos = 0D;
			this.totalRetenciones = 0D;
			this.montoTotalFactura = 0D;
			this.totalPago = 0D;
			this.totalFacturasReportadas = 0D;
			
			// Busqueda principal
			// -----------------------------------------------------------------------------
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("beneficiario", "Beneficiario"));
			this.tiposBusqueda.add(new SelectItem("numeroCuentaOrigen", "Cuenta Bancaria"));
			this.tiposBusqueda.add(new SelectItem("noCheque", "Cheque"));
			this.tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.fechaBusqueda = Calendar.getInstance().getTime();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda OBRAS
			// -----------------------------------------------------------------------------
			this.tipoObra = "O";
			this.tiposBusquedaObra = new ArrayList<SelectItem>();
			this.tiposBusquedaObra.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaObra.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaObra.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObra.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();

	   		// BUSQUEDA PERSONA/NEGOCIO
			// -----------------------------------------------------------------------------
	   		this.tiposBusquedaPersonaNegocio = new ArrayList<SelectItem>();
	   		this.tiposBusquedaPersonaNegocio.add(new SelectItem("nombre", "Nombre"));
	   		this.tiposBusquedaPersonaNegocio.add(new SelectItem("rfc", "RFC"));
	   		this.tiposBusquedaPersonaNegocio.add(new SelectItem("id", "ID"));
	   		nuevaBusquedaPersonaNegocio();
	   		
	   		// BUSQUEDA CONCEPTOS
			// -----------------------------------------------------------------------------
	   		this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
	   		this.tiposBusquedaConceptos.add(new SelectItem("descripcion", "Descripcion"));
	   		this.tiposBusquedaConceptos.add(new SelectItem("id", "ID"));
	   		nuevaBusquedaConceptos();

			this.comprobacionGenerada=false;
			this.listBusqRet = new String[3];
			this.listBusqRet[0] = "Clave";
			this.listBusqRet[1] = "Descripcion";
			this.listBusqRet[2] = "Cuenta Contable";
	   		this.valBusqRet = "";
	   		this.campoBusqRet = this.listBusqRet[0];
	   		
	   		this.ctx = new InitialContext();
			this.ifzCajaChica = (PagosGastosRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
			this.ifzCajaChicaDetalles = (PagosGastosDetRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzDesgloImpto = (PagosGastosDetImpuestosRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
			this.ifzGastoImpuesto = (GastosImpuestoRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzCtas = (CuentasBancariasRem) this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
			this.ifzPersonas = (PersonaRem) this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzSucursal = (SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzFtp = (FtpRem) this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzPerNeg = (PersonaNegocioVistaRem) this.ctx.lookup("ejb:/Logica_Clientes//PersonaNegocioVistaFac!net.giro.clientes.logica.PersonaNegocioVistaRem");
			//this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			this.ifzLocalidad = (LocalidadDAO) this.ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			this.ifzColonia = (ColoniaDAO) this.ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");		
			
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCajaChicaDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGastoImpuesto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPerNeg.setInfoSesion(this.loginManager.getInfoSesion());
			
			// VALORES SUGERIDOS por Usuario
			comprobarUsuario();
			
			// FORMAS DE PAGO
			// -----------------------------------------------------------------------------
			this.pojoGpoValFormasPago = this.ifzGpoVal.findByName("SYS_FORMAS_PAGO");
			if (this.pojoGpoValFormasPago == null || this.pojoGpoValFormasPago.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_FORMAS_PAGO en con_grupo_valores");
	
			// MOVIMIENTO GASTOS
			// -----------------------------------------------------------------------------
			this.pojoGpoValGasto = this.ifzGpoVal.findByName("SYS_MOVGTOS");
			if (this.pojoGpoValGasto == null || this.pojoGpoValGasto.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");
			
			this.deseaCancelar = tyg.getString("navegacion.label.deseaCancelar");
			this.reqReferencia = true;
			
			// Recuperamos perfiles
			getPerfiles();
			cargarFormasDePago();
			cargarSucursales();
			cargarCuentas();
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.CajaChicaAction", e);
			this.ctx = null;
		}
  	}
	
	
	public void buscar() {
		String orderBy = "";
   		
   		try {
   			control();
   			if (this.campoBusqueda == null || "".equals(this.campoBusqueda.trim()))
   				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();

			orderBy = "fecha desc, id desc";
   			if ("fecha".equals(this.campoBusqueda))
   				orderBy = "id desc";
   			
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCajaChica = this.ifzCajaChica.findCajaChicaLikeProperty(this.campoBusqueda, ("fecha".equals(this.campoBusqueda) ? this.fechaBusqueda : this.valorBusqueda), "", true, orderBy, 0L, 0);
			if (this.listCajaChica == null || this.listCajaChica.isEmpty())
				control(12, "Busqueda sin resultados");
		} catch (Exception e) {
			this.listCajaChica = new ArrayList<PagosGastos>();
			control("Ocurrio un problema al consultar las Cajas Chicas", e);
		}
	}
	
   	public void nuevo() {
		try {
			control();
			this.pojoCajaChica = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoBeneficiarios = new PersonaExt();
			this.pojoObra = new Obra();
			
			cargarSucursales();
			setSucursal(this.idSucursalSugerida);
			
			if (this.listFacturaConImpuestosEliminadas == null)
				this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			
			this.listFacturaConImpuestos.clear();
			this.listFacturaConImpuestosEliminadas.clear();			
			this.lisImpuestosEliminados.clear();
			this.listDesgloseImpuestos.clear();
			this.listImpuestosDelGasto.clear();

			this.cuenta="";
			this.tipoPersona = "P";
			this.beneficiario = "";
			if (this.pojoEmpleadoUsuario != null && this.pojoEmpleadoUsuario.getId() != null && this.pojoEmpleadoUsuario.getId() > 0L)
				this.beneficiario = this.pojoEmpleadoUsuario.getPersona().getId() + " - " + this.pojoEmpleadoUsuario.getNombre();
			this.totalFacturasReportadas = 0D;
			this.varOper = "";
			this.observaciones = "";
   			this.numPaginaFact = 1;
			this.perfilCajaChicaVoBo = "";
			this.perfilCajaChicaAutorizacion = "";
			actualizarPerfil("DIVIDIR_CAJA");
			if ("S".equals(this.perfilDividirCajaChica)) {
				actualizarPerfil("CAJA_CHICA_VOBO");
				actualizarPerfil("CAJA_CHICA_AUTORIZACION");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al generar una nueva Caja Chica", e);
		} 
	}

   	public void editar() {
   		List<PagosGastosDetImpuestosExt> listImpuestos = null;
   		
   		try {
   			control();
			log.info("CajaChica... Preparando para editar");
   			actualizarPerfil("DIVIDIR_CAJA");
   			this.numPaginaFact = 1;
			if ("S".equals(this.perfilDividirCajaChica)) {
				actualizarPerfil("CAJA_CHICA_VOBO");
				actualizarPerfil("CAJA_CHICA_AUTORIZACION");
			}
			
			cargarSucursales();
			
			log.info("CajaChica... Extendiento pojo (PagosGastos)");
			this.pojoCajaChica = this.ifzCajaChica.convertir(this.pojoCaja);
			log.info("CajaChica... Pojo (PagosGastos) extendido");
			
			this.vobo = (this.pojoCajaChica.getVoboPor() > 0 ? true : false);
			this.autorizado = (this.pojoCajaChica.getAutorizadoPor() > 0 ? true : false);
			this.tipoCajaChica = ("Obra".equals(this.pojoCajaChica.getArea()) ? 0 : 1);
			this.varOper = this.pojoCajaChica.getOperacion(); 
			this.pojoObra = this.pojoCajaChica.getIdObra();
			
			if (this.listFacturaConImpuestosEliminadas == null)
				this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.listFacturaConImpuestosEliminadas.clear();
			
			// Cuenta
			log.info("CajaChica... Recuperando Cuenta Origen");
			this.cuenta = "";
			if (this.pojoCajaChica != null && this.pojoCajaChica.getIdCuentaOrigen() != null && this.pojoCajaChica.getIdCuentaOrigen().getId() > 0L) {
				this.cuenta = this.pojoCajaChica.getIdCuentaOrigen().getId() + " - " 
						+ this.pojoCajaChica.getIdCuentaOrigen().getInstitucionBancaria().getNombreCorto() + " - " 
						+ this.pojoCajaChica.getIdCuentaOrigen().getNumeroDeCuenta();
				this.pojoCtas = this.pojoCajaChica.getIdCuentaOrigen();
				log.info("CajaChica... Cuenta Origen recuperada");
			}
			
			// Beneficiario
			log.info("CajaChica... Recuperando Beneficiario");
			this.beneficiario = "";
			if (this.pojoCajaChica != null && this.pojoCajaChica.getIdBeneficiario() != null && this.pojoCajaChica.getIdBeneficiario().getId() > 0L) {
				this.beneficiario = this.pojoCajaChica.getIdBeneficiario().getId() + " - " + this.pojoCajaChica.getBeneficiario();
				this.pojoBeneficiarios = this.pojoCajaChica.getIdBeneficiario();
				log.info("CajaChica... Beneficiario recuperado");
			}
			
			try {
				log.info("CajaChica... Recuperando Lista de Facturas");
				if (this.listFacturaConImpuestos == null)
					this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>();
				this.listFacturaConImpuestos.clear();
				
				this.listFacturas = this.ifzCajaChicaDetalles.findExtAll(this.pojoCajaChica.getId()); 
				if (this.listFacturas != null && ! this.listFacturas.isEmpty()) {
					for (PagosGastosDetExt var : this.listFacturas) {
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
				
				log.info("CajaChica... " + this.listFacturaConImpuestos.size() + " Facturas recuperadas");
				log.info("CajaChica... Totalizando");
				actualizaTotalFaturas();
			} catch (Exception e) {
				control("Ocurrio un problema al generar lista de facturas con impuestos de la Caja Chica", e);
			}
			
			log.info("CajaChica... Lista para editar");
   		} catch (Exception e) {
   			control("Ocurrio un problema al recuperar la Caja Chica", e);
		} 
   	}
   	
	public void guardar() {
		Pattern pat = null;
		Matcher match = null;
		Respuesta resp = new Respuesta();
		boolean registroNuevo = false;
		boolean borrarXML = false;
		String fileName = "";
		
		try {
			control();
			log.info("Guardando CajaChica (PagosGastos)");
			if ("S".equals(this.perfilRequiereObra) && this.tipoCajaChica == 0 && this.pojoCajaChica.getIdObra() == null) {
				log.info("ERROR 19: Debe selecccionar una Obra");
				control(19, "Debe selecccionar una Obra");
				return;
			}
			
			if (this.listFacturaConImpuestos.isEmpty()) {
				log.info("ERROR 15: No añadio ninguna factura");
				control(15, "No añadio ninguna factura");
				return;
			}

			if (this.pojoCajaChica.getId() == null || this.pojoCajaChica.getId() <= 0L)
				registroNuevo = true;

			if (this.pojoObra != null) {
				if (this.pojoCajaChica.getIdObra() == null || (this.pojoCajaChica.getIdObra().getId() != null && this.pojoCajaChica.getIdObra().getId().longValue() != this.pojoObra.getId().longValue()))
					this.pojoCajaChica.setIdObra(this.pojoObra);
			}
			
			/*
			 * ELIMINAMOS ESTE CODIGO PORQUE LA CAJA CHICA NO TIENE EL COMPORTAMIENTO DE REVISAR PROGRAMACION DE PAGOS
			 * JAVAZO - 2016/09/30
			 * 
			if (!"G".equals(this.pojoGtosComp.getOperacion())) {
				/**** valida que el concepto de caja chica este programado ****/
				/*long concepto = 283; //Id concepto CAJA CHICA
				
   				this.listProPagDet = ifzProPagDet.findByAgenteEstatusMontoConcepto(sucursalesVisibles, "G", concepto);
   				if (this.listProPagDet == null) {
   					this.tipoMensaje = 16;
   					return "ERROR";
   				}* /
				
				for (Object obj: this.listProPagDet) {
					ProgPagosDetalle ppd = (ProgPagosDetalle)((Object[])obj)[1];
					if (!"R".equals((String)((Object[])obj)[0]) && pojoGtosComp.getMonto().compareTo(ppd.getMonto().doubleValue()) != 0) {
						ban = 3;
					}
					
					if ("R".equals((String)((Object[])obj)[0]) && pojoGtosComp.getMonto().compareTo(ppd.getMonto().doubleValue()) != 0) {
						ban = 2;
					}
					
					if (!"R".equals((String)((Object[])obj)[0]) && pojoGtosComp.getMonto().compareTo(ppd.getMonto().doubleValue()) == 0) {
						ban = 1;
					}

					if ("R".equals((String)((Object[])obj)[0]) && pojoGtosComp.getMonto().compareTo(ppd.getMonto().doubleValue()) == 0) {
						ban = 0;
						break;
					}
				}
				
				if (ban == 1) {
					this.tipoMensaje =17;
   					return "ERROR";
				}
				
				if (ban == 2) {
					this.tipoMensaje =18;
   					return "ERROR";
				}
				
				if (ban == 3) {
					this.tipoMensaje =16;
   					return "ERROR";
				}*/

			//validando Cuenta
			log.info("Validando cuenta: " + this.cuenta);
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.cuenta);			
			if (match.find()) {
				if (registroNuevo || this.pojoCajaChica.getIdCuentaOrigen() == null) {
					log.info("Obtenemos cuenta (CtasBanco): " + match.group(1));
					this.pojoCtas = this.ifzCajaChica.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzCtas.findAllById(Short.valueOf(match.group(1)));
				} else {
					if (this.pojoCtas.getId() != Long.valueOf(match.group(1))) {
						log.info("Cambiamos cuenta (CtasBanco) por: " + match.group(1));
						this.pojoCtas = this.ifzCajaChica.findCuentaBancariaById(Long.valueOf(match.group(1))); 
					} else {
						log.info("cuenta (CtasBanco) sin ambios");
					}
				}
			}
			this.pojoCajaChica.setIdCuentaOrigen(this.pojoCtas);
			
			//validando beneficiario
			log.info("Validando beneficiario: " + this.beneficiario);
			match = pat.matcher(this.beneficiario);
			if (match.find()) {
				if (registroNuevo || this.pojoCajaChica.getIdBeneficiario() == null) {
					log.info("Obtenemos beneficiario (Persona): " + match.group(1));
					this.pojoBeneficiarios = this.ifzCajaChica.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				} else {
					if (this.pojoBeneficiarios.getId() != Long.valueOf(match.group(1))) {
						log.info("Cambiamos beneficiario (Persona) por: " + match.group(1));
						this.pojoBeneficiarios = this.ifzCajaChica.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
					} else {
						log.info("beneficiario (Persona) sin ambios");
					}
				}
			}			
			this.nombreBeneficiario = this.pojoBeneficiarios.getPrimerNombre() + " " 
					+ ((this.pojoBeneficiarios.getSegundoNombre()   != null && ! "".equals(this.pojoBeneficiarios.getSegundoNombre().trim()))   ? this.pojoBeneficiarios.getSegundoNombre().trim()   : "") + " " 
					+ ((this.pojoBeneficiarios.getPrimerApellido()  != null && ! "".equals(this.pojoBeneficiarios.getPrimerApellido().trim()))  ? this.pojoBeneficiarios.getPrimerApellido().trim()  : "") + " " 
					+ ((this.pojoBeneficiarios.getSegundoApellido() != null && ! "".equals(this.pojoBeneficiarios.getSegundoApellido().trim())) ? this.pojoBeneficiarios.getSegundoApellido().trim() : "");
			this.nombreBeneficiario = this.nombreBeneficiario.replace("  ", " ");
			this.pojoCajaChica.setIdBeneficiario(this.pojoBeneficiarios);
			this.pojoCajaChica.setBeneficiario(this.nombreBeneficiario);
			this.pojoCajaChica.setTipoBeneficiario(this.tipoPersona);
			
			// Comprobamos su tiene visto bueno
			if ("S".equals(this.perfilCajaChicaVoBo) && this.vobo) {
				this.pojoCajaChica.setVoboPor(this.usuarioId);
				this.pojoCajaChica.setFechaVobo(Calendar.getInstance().getTime());
			}
			
			// Comprobamos si esta autorizado
			if ("S".equals(this.perfilCajaChicaAutorizacion) && this.autorizado) {
				this.pojoCajaChica.setAutorizadoPor(this.usuarioId);
				this.pojoCajaChica.setFechaAutorizado(Calendar.getInstance().getTime());
			}
			
			this.pojoCajaChica.setOperacion(this.varOper);
			this.pojoCajaChica.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			
			if (registroNuevo) {
				this.pojoCajaChica.setTipo("C");//comprobacion de caja chica
				this.pojoCajaChica.setEstatus("G");//generado
				this.pojoCajaChica.setConcepto("Reposición de Caja chica"); // (this.pojoGtosComp.getOperacion() == "C" ? "Reposición de Caja chica": (this.pojoGtosComp.getOperacion()== "G" ? "Comprobación de Caja Chica": "Reposición y Comprobación de Caja Chica"));
				this.pojoCajaChica.setCreadoPor(this.usuarioId);//(Short.valueOf(String.valueOf(this.usuarioId)));
				this.pojoCajaChica.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoCajaChica.setModificadoPor(this.usuarioId);
				this.pojoCajaChica.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Asignamos consecutivo
				log.info("Asignamos consecutivo");
				/*int consecutivo = this.ifzCajaChica.findConsecutivoByBeneficiario(this.pojoCajaChica.getIdBeneficiario().getId(), "C", "G");
				this.pojoCajaChica.setConsecutivo(consecutivo);*/
				// Comprobamos Numero de Caja Chica
				/*if (this.pojoCajaChica.getConsecutivo() <= 0) {
					this.pojoCajaChica.setConsecutivo(this.ifzCajaChica.findConsecutivoByBeneficiario(this.pojoCajaChica.getIdBeneficiario().getId(), "C", "G"));
					if (this.pojoCajaChica.getConsecutivo() <= 0) {
						control(-1, "No se pudo determinar el Numero de Caja Chica.\nReasigne al Beneficiario");
						return;
					}
				}*/
				
				// Asignamos AREA
				log.info("Asignamos Area");
				if ("S".equals(this.perfilRequiereObra))
					this.pojoCajaChica.setArea((this.tipoCajaChica == 0 ? "Obra" : "Oficina"));
				else
					this.pojoCajaChica.setArea("Oficina");
				
				// Asignamos monto limite
				log.info("Asignamos Monto Limite");
				if ("S".equals(this.perfilRequiereObra))
					this.pojoCajaChica.setMontoLimite(this.perfilLimiteCajaChica);
				else
					this.pojoCajaChica.setMontoLimite(0D);
				
				// Guardamos
				log.info("Guardamos CajaChica (PagosGastos)");
				resp = this.ifzCajaChica.salvar(this.pojoCajaChica);
				
				// Lo agregamos a la lista
				this.listCajaChica.add(this.pojoCaja);
				
				//validando folio cheque
				if (resp.getResultado() != -1) {
					// Asignamos el ID correspondiente
					this.pojoCajaChica.setId(Long.valueOf(resp.getReferencia()));
					this.pojoCaja = this.ifzCajaChica.convertir(this.pojoCajaChica);
					
					//impresion del cheque
					if (! "G".equals(this.pojoCajaChica.getOperacion())) {
						mensaje="BIEN";
					}
				} else {
					if (! "BIEN".equals(resp.getRespuesta())) {
						if ((resp.getRespuesta().indexOf("SE CANCELA EL FOLIO ") > -1) || (resp.getRespuesta().indexOf("SE CANCELAN LOS FOLIOS ") > -1))
							control(7, resp.getRespuesta());
						else 
							control(6, resp.getRespuesta());
						return;   							
					}
				}
			} else {
				// Actualizamos
				log.info("Actualizamos CajaChica (PagosGastos)");
				this.pojoCajaChica.setModificadoPor(this.usuarioId);
				this.pojoCajaChica.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzCajaChica.update(this.pojoCajaChica);
				log.info("CajaChica (PagosGastos) actualizada");
				this.pojoCaja = this.ifzCajaChica.findById(this.pojoCajaChica.getId());
				// Actualizo en lista
				for (PagosGastos item : this.listCajaChica) {
					if (item.getId().longValue() == this.pojoCaja.getId().longValue()) {
						this.listCajaChica.set(this.listCajaChica.indexOf(item), this.pojoCaja);
						break;
					}
				}
			}
			
			//grabando las facturas en caso de que hayan agregado algunas
			if (! this.listFacturaConImpuestos.isEmpty()) {
				log.info("Guardando facturas (PagosGastosDet)");
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					var.getPojoFactura().setIdPagosGastos(this.pojoCajaChica);	
					if (var.getPojoFactura().getId() != null && var.getPojoFactura().getId() > 0L) 
						this.ifzCajaChicaDetalles.update(var.getPojoFactura());
					else 
						var.getPojoFactura().setId(this.ifzCajaChicaDetalles.save(var.getPojoFactura()));
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
					// Retencciones
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
				log.info("Facturas (PagosGastosDet) guardadas");
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
				    		this.ifzCajaChicaDetalles.eliminarFactura(this.facturaId);
							
							// Lo elimino fisicamente del servidor
							fileName = this.prefijoFacturas + var.getPojoFactura().getIdCfdi() + ".xml";
							this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
							if (! this.ifzFtp.delArchivo(this.ftpDigitalizacionRuta + fileName))
								log.warn("ERROR FTP - No se pudo Borrar la factura del servidor, ID: " + this.facturaId);
						}
						
						// Borro detalle (factura)
						this.ifzCajaChicaDetalles.delete(var.getPojoFactura().getId());
						this.facturaId = 0L;
					} else {
						// Borro detalle (factura)
						this.ifzCajaChicaDetalles.delete(var.getPojoFactura().getId());
					}
				}
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			this.pojoCaja = this.ifzCajaChica.convertir(this.pojoCajaChica);
			mensajeTransaccion();
		} catch (Exception e) { 
			control("Ocurrio un problema al guardar la Caja Chica", e);
		}
	}

	public void cancelar() {
		int index = -1;
		
		try {
			control();
			index = this.listCajaChica.indexOf(this.pojoCaja);
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			this.pojoCaja = this.ifzCajaChica.cancelar(this.pojoCaja);
			
			// Actualizo el pojo en la lista si corresponde
			if (index >= 0)
				this.listCajaChica.set(index, this.pojoCaja);
			//this.pojoCaja.setEstatus("X");
			log.info("Caja Chica " + this.pojoCaja.getId() + " (PagosGastos) cancelada");
		} catch (Exception e) {
			this.msgCancelar = "Ocurrio un problema inesperado";
			control("Ocurrio un problema al intentar Cancelar la Caja Chica seleccionada", e);
		}
	}

	public void reporte() {
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		String nombreDocumento = "";
		
		try {
			control();
			if (this.pojoCaja != null && this.pojoCaja.getId() != null && this.pojoCaja.getId() > 0L) {
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("pagosGastosId", this.pojoCaja.getId());

				// Parametros para la ejecucion del reporte
				params = new HashMap<String, String>();
				params.put("idPrograma", 	  this.entornoProperties.getString("REPORTE_CAJA_CHICA_ID"));
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);
				
				Respuesta respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					//this.mensaje = respuesta.getErrores().getDescError();
					log.error("ERROR INTERNO " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
					control(respuesta.getErrores().getDescError());
					return;
				}
				
				nombreDocumento = "ReposicionCajaChica-" + this.pojoCaja.getId() + "." + respuesta.getBody().getValor("formatoReporte");
				this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
				this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte")); 	
				this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la Caja Chica", e);
		}
	}

	public void mensajeTransaccion() {
		try {
			control();
			// Enviamos mensaje a cola de transacciones
			log.info("Enviando Transaccion ... ");
			this.ifzCajaChica.contabilizador(this.pojoCaja.getId());
			log.info("Transaccion enviada");
		} catch (Exception e) {
			control("Ocurrio un problema al ejecutar el evento de Transaccion", e);
		}
	}

	public void preRenderView() {
        /*FocusManager focusManager = ServiceTracker.getService(FocusManager.class);
        focusManager.focus(this.inputNameFocused);*/
    }
	
	public void toggleAvanzada() {
		this.avanzada = ! this.avanzada;
	}
	
	private void getFTPData() {
		this.ftpDigitalizacionHost = this.entornoProperties.getString("ftp.digitalizacion.host");
		this.ftpDigitalizacionPort = this.entornoProperties.getString("ftp.digitalizacion.port");
		this.ftpDigitalizacionUser = this.entornoProperties.getString("ftp.digitalizacion.user");
		this.ftpDigitalizacionPass = this.entornoProperties.getString("ftp.digitalizacion.password");
		this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta.cxp");
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
			log.error("Ocurrio un problema en Inventarios.EntradasAlmacenAction.comprobarUsuario()", e);
			return false;
		}
		
		return true;
	}
	
   	// ---------------------------------------------------------------------------
	// Detalles
   	// ---------------------------------------------------------------------------
	
	public void agregar() {
		PersonaExt proveedor = null;
		Matcher match = null;
		Pattern pat = null;
		
		try {
			control();
			if (this.pojoCajaChica.getId() != null && this.pojoCajaChica.getId() > 0L) {
				if (! this.perfilPermiteEditar && ! validaRequest("DETEDIT", "1")) {
				    this.comprobacionGenerada = true;
					log.info("ERROR 10: No se permite añadir facturas.");
					control(10, "No se permite añadir facturas");
					return;
				}
			}

			reiniciaFactura();
			this.pojoComprobadoGto = new PagosGastosDetExt();
			this.sinFactura = false;
			this.encontroMismoGrupo = false;
			this.montoTotalFactura = 0D;
			this.conceptoGasto = "";
			this.conceptoGastoPrevio = ""; // this.conceptoGasto2 = "";
			this.nombreProveedor = "";
			this.descripcionFactura = "";
			this.observaciones = "";
			this.listDesgloseImpuestos.clear();
			this.listDesgloseRetenciones.clear();
			this.listRetEncontradas.clear();
			desglosaImpuestos();
			
			proveedor = new PersonaExt();
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.beneficiario);
			if (match.find()) {
				proveedor = this.ifzCajaChica.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona);
				this.pojoComprobadoGto.setIdProveedor(proveedor);
				this.pojoComprobadoGto.setTipoPersonaProveedor(this.tipoPersona);
			}
			
			this.reg = new FacturaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar agregar Factura", e);
		}
	}
	
	public void editarFactura() {
		String facturaFolio = "";

		try {
			control();
			// Inicializaciones
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseRetenciones = new ArrayList<PagosGastosDetImpuestosExt>();
			
			if (this.reg.getPojoFactura().getIdCfdi() == null || this.reg.getPojoFactura().getIdCfdi() <= 0L)
				this.reg.getPojoFactura().setIdCfdi(0L);

			reiniciaFactura();
			this.pojoComprobadoGto = this.reg.getPojoFactura();
			if (this.pojoComprobadoGto == null) {
				control(-1, "Ocurrio un problema al obtener la Factura de la Comprobacion seleccionada");
				return;
			}

			this.sinFactura = (this.pojoComprobadoGto.getFacturado() == 0);
			this.facturaId = this.pojoComprobadoGto.getIdCfdi();
			facturaFolio = this.pojoComprobadoGto.getFacturaFolio();
			this.facturaRfc = this.pojoComprobadoGto.getFacturaRfc();
			this.facturaEmisor = this.pojoComprobadoGto.getNombreProveedor();
			this.subtotal = this.pojoComprobadoGto.getSubtotal();
			this.totalImpuestos = this.pojoComprobadoGto.getTotalImpuestos();
			this.totalRetenciones = this.pojoComprobadoGto.getTotalRetenciones();
			this.facturaTotal = this.pojoComprobadoGto.getTotal();
			this.facturaMoneda = "MXN";
			this.facturaTipoCambio = 1.0;
			if (this.facturaId != null && this.facturaId.longValue() > 0L) {
				facturaFolio = this.ifzCajaChicaDetalles.getFacturaProperty(this.facturaId, "factura");
				this.facturaRfc = this.ifzCajaChicaDetalles.getFacturaProperty(this.facturaId, "rfcEmisor");
				this.facturaEmisor = this.ifzCajaChicaDetalles.getFacturaProperty(this.facturaId, "nombre");
				this.facturaMoneda = this.ifzCajaChicaDetalles.getFacturaProperty(this.facturaId, "moneda");
				this.facturaTipoCambio = this.ifzCajaChicaDetalles.getFacturaTipoCambio(this.facturaId);
				this.facturaDescuento = this.ifzCajaChicaDetalles.getFacturaDescuento(this.facturaId);
				//this.facturaSubtotal = this.ifzCajaChicaDetalles.getFacturaSubtotal(this.facturaId);
				this.facturaTotal = this.ifzCajaChicaDetalles.getFacturaTotal(this.facturaId);
			}
	
			this.facturaActualizar = false;
			if ((this.facturaEmisor == null || "".equals(this.facturaEmisor.trim())) || (facturaFolio == null || "".equals(facturaFolio.trim())))
				this.facturaActualizar = true;

			this.conceptoGasto = "";
			if (this.pojoComprobadoGto.getIdConcepto() != null && this.pojoComprobadoGto.getIdConcepto().getId() > 0L) {
				this.pojoConceptoGtos = this.pojoComprobadoGto.getIdConcepto();
				this.conceptoGasto = this.pojoConceptoGtos.getId() + " - " + this.pojoConceptoGtos.getDescripcion();
			}
			
			if (this.facturaEmisor != null && ! "".equals(this.facturaEmisor.trim())) 
				this.nombreProveedor = this.facturaEmisor;
			else
				this.nombreProveedor = getGeneraNombreProveedor(this.pojoComprobadoGto.getIdProveedor());
			this.descripcionFactura = this.pojoComprobadoGto.getIdConcepto().getDescripcion() + " - " + this.nombreProveedor;

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
			this.conceptoGastoPrevio = this.conceptoGasto;
		} catch (Exception e) {
			control("Ocurrio un problema al desglozar la Comprobacion seleccionada", e);
		} 
	}
	
	public void guardarFactura() {
		boolean encontro = false;			
		boolean registroNuevo = false;
		Pattern pat = null;
		Matcher match = null;
		String[] splitted = null;
		String value = "";
		
		try {
			control();
			if (this.facturaTotal != null && this.facturaTotal > 0) {
				if (! "OK".equals(validaMontoFactura()))
					return;
				
				if (! "OK".equals(validaMontoImpuestos()))
					return;
				
				if (! "OK".equals(validaGrupoImpuestos()))
					return;
			
				if (! "OK".equals(validaMontoFacturaContraTotalImpuestos()))
					return;
			}
			
			if (this.facturaId == null)
				analizarArchivo();
	
			if (this.pojoComprobadoGto.getId() == null || this.pojoComprobadoGto.getId() <= 0L)
				registroNuevo = true;
			
			this.pojoComprobadoGto.setFacturado((this.sinFactura ? 0 : 1));
			this.pojoComprobadoGto.setIdCfdi(this.facturaId);
			this.pojoComprobadoGto.setNombreProveedor(this.facturaEmisor);
			this.pojoComprobadoGto.setFacturaRfc(this.facturaRfc);
			
			if (this.pojoComprobadoGto.getFacturado() == 1 && (this.facturaId == null || this.facturaId <= 0L))
				this.pojoComprobadoGto.setFacturado(0);
			
			if (this.pojoComprobadoGto.getTipoPersonaProveedor() == null || "".equals(this.pojoComprobadoGto.getTipoPersonaProveedor().trim()))
				this.pojoComprobadoGto.setTipoPersonaProveedor(this.pojoComprobadoGto.getIdProveedor().getTipoPersona() == 2L ? "N" : "P");
			
			if (this.facturaActualizar) {
				this.facturaActualizar = false;
				this.ifzCajaChicaDetalles.setFacturaProperty(this.facturaId, "nombre", this.facturaEmisor);
				
				if (this.pojoComprobadoGto.getReferencia().contains("-")) {
					splitted = this.pojoComprobadoGto.getReferencia().split("-");

					if (splitted.length > 2) {
						for (int i = 0; i <= (splitted.length - 2); i++)
							value += splitted[i] + "-";
						this.ifzCajaChicaDetalles.setFacturaProperty(this.facturaId, "serie", value);
						
						value = splitted[splitted.length - 1];
						this.ifzCajaChicaDetalles.setFacturaProperty(this.facturaId, "folio", value);
					} else {
						value = splitted[0];
						this.ifzCajaChicaDetalles.setFacturaProperty(this.facturaId, "serie", value);
						
						value = splitted[1];
						this.ifzCajaChicaDetalles.setFacturaProperty(this.facturaId, "folio", value);
					}
				} else {
					this.ifzCajaChicaDetalles.setFacturaProperty(this.facturaId, "serie", this.pojoComprobadoGto.getReferencia());
				}
			}
			
			//validando gasto
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.conceptoGasto);			
			if (match.find()) {
				if (registroNuevo) {
					this.pojoConceptoGtos = this.ifzConValores.findById(Long.valueOf(match.group(1)));
				} else {
					if (this.pojoConceptoGtos.getId() != Long.valueOf(match.group(1)))
						this.pojoConceptoGtos = this.ifzConValores.findById(Long.valueOf(match.group(1)));
				}
			}

			this.pojoComprobadoGto.setIdConcepto(this.pojoConceptoGtos);
			this.pojoComprobadoGto.setTotalImpuestos(this.totalImpuestos);  					
			this.pojoComprobadoGto.setSubtotal(this.subtotal);   	
			this.pojoComprobadoGto.setTotalRetenciones(this.totalRetenciones);
			this.pojoComprobadoGto.setIdPagosGastos(this.pojoCajaChica);
			this.pojoComprobadoGto.setObservaciones(this.observaciones);
						
			if (this.pojoCajaChica.getId() == null || this.pojoCajaChica.getId() <= 0L) {
				//si el gasto no existe guardo las facturas en una lista
				// y las guardare hasta que creen un gasto. sí el gasto no existe no puedo grabar 
				//facturas hasta que me serciore que creen un gasto. Siempre que el gasto no tenga llave 
				//la factura tampoko tendra llave, por eso no pregunto si es nueva 	
					
				this.pojoComprobadoGto.setCreadoPor(this.usuarioId);
				this.pojoComprobadoGto.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoComprobadoGto.setModificadoPor(this.usuarioId);
				this.pojoComprobadoGto.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (! this.listDesgloseImpuestos.isEmpty()) {
					for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
						var.setIdPagosGastosDet(this.pojoComprobadoGto);
					}
				}
			} else {
				// si el gasto existe ya puedo grabar facturas 
				if (registroNuevo) {
					this.pojoComprobadoGto.setCreadoPor(this.usuarioId);
					this.pojoComprobadoGto.setFechaCreacion(Calendar.getInstance().getTime());
					this.pojoComprobadoGto.setModificadoPor(this.usuarioId);
					this.pojoComprobadoGto.setFechaModificacion(Calendar.getInstance().getTime());
					
					// GUARDAMOS
					this.pojoComprobadoGto.setId(this.ifzCajaChicaDetalles.save(this.pojoComprobadoGto));
					
					if (! this.listDesgloseImpuestos.isEmpty()) {
						// Guardamos Impuestos
						for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos)
							var.setIdPagosGastosDet(this.pojoComprobadoGto);
						this.listDesgloseImpuestos = this.ifzDesgloImpto.saveOrUpdateListExt(this.listDesgloseImpuestos);
					}
				} else {
					this.pojoComprobadoGto.setModificadoPor(this.usuarioId);
					this.pojoComprobadoGto.setFechaModificacion(Calendar.getInstance().getTime());
					this.ifzCajaChicaDetalles.update(this.pojoComprobadoGto);

					if (! this.listDesgloseImpuestos.isEmpty()) {
						// Guardamos Impuestos
						for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos)
							var.setIdPagosGastosDet(this.pojoComprobadoGto);
						this.listDesgloseImpuestos = this.ifzDesgloImpto.saveOrUpdateListExt(this.listDesgloseImpuestos);
						/*for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
							var.setIdPagosGastosDet(this.pojoComprobadoGto);
							
							if (var.getId() == null)
								var.setId(this.ifzDesgloImpto.save(var));
							else
								this.ifzDesgloImpto.update(var);
						}*/
					}
				}
				
				//eliminando los pojos de impuestos de la base de datos,
				// ya que anteriormente solo los elimine de la memoria
				if (! this.lisImpuestosEliminados.isEmpty()) {
					for (PagosGastosDetImpuestosExt var : this.lisImpuestosEliminados) {
						if (var.getId() != null)
							this.ifzDesgloImpto.delete(var.getId());
					}
					
					this.lisImpuestosEliminados.clear();
				}
			}  	
			//guardando las facturas con llave o sin llave en la lista de facturaImpuestos por si hacen clic
			// en editar de una factura poder listar los impuestos tal cual los dejaron
			this.reg.setPojoFactura(this.pojoComprobadoGto);
			this.reg.setListImpuestos(copiaListas(this.listDesgloseImpuestos));
			
			encontro = false;
			if (! this.listFacturaConImpuestos.isEmpty()) {
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					if (var.equals(this.reg)) {
						var = this.reg;
						encontro = true;
					}
				}
			}
			   					
			if (! encontro)
				this.listFacturaConImpuestos.add(this.reg);

			actualizaTotalFaturas();
			this.facturaId = null;
			control(false, -1, "Comprobacion agregada", null);
   		} catch (Exception e) {
			control(true, 9, "Ocurrio un problema al guardar la Factura", e);
		}
   	}

   	public void eliminar() {
		try {
			control();
			if (this.listFacturaConImpuestosEliminadas == null)
				this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.listFacturaConImpuestosEliminadas.add(this.reg);
			this.listFacturaConImpuestos.remove(this.reg);
			actualizaTotalFaturas();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar elimiar la Caja Chica", e);
		}
	}
   	
	public void evaluaEditar() {
   		try {
   			control();
   			this.facturaConLlave = false;
			if (this.reg.getPojoFactura().getId() != null) {
				if (! this.perfilPermiteEditar && ! validaRequest("DETEDIT", "1")) { //! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					this.facturaConLlave = true;
		   			control(11, "No se permite editar las facturas");
		   			return;
		   		}
			}
			
			editarFactura();
		} catch (Exception e) {
			control("Ocurrio un problema al evaluar para Edicion", e);
		}
   	}

	public void evaluaCancelar() {
   		try {
   			control();
   			this.msgCancelar = "";
   			if (this.pojoCaja != null)
   				this.msgCancelar = "X".equals(this.pojoCaja.getEstatus()) ? "" : this.deseaCancelar;
		} catch (Exception e) {
			control("No se puede evaluar el estatus de la Caja Chica", e);
		}
   	}

   	public void evaluarFactura() {
   		try {
			control();
			this.facturaConLlave = false;
			if (this.reg.getPojoFactura().getId() != null) {
				if (! this.perfilPermiteEditar &&  ! validaRequest("DETEDIT", "1")) {//! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					this.facturaConLlave = true;
		   			control(2, "No se permite eliminar facturas");
		   			return;
				} 
			}
		} catch (Exception e) {
			control("Ocurrio un problema al evaluar la factura", e);
		}
   	}

   	public String eliminarImpuestoDesglosado() {
   		double sumImpuestos = 0;
   		double sumRetenciones = 0;
   		
   		try {
   			control();
			this.lisImpuestosEliminados.add(this.pojoDesgloImpto);
			this.listDesgloseImpuestos.remove(this.pojoDesgloImpto);
			this.subtotal = this.montoTotalFactura;
			this.totalImpuestos = sumImpuestos;
			this.totalRetenciones = sumRetenciones;
			this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
			
			if (! this.listDesgloseImpuestos.isEmpty()) {			
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(),2);	
					else	
						sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(),2);	
				}

				this.subtotal = this.montoTotalFactura - (sumImpuestos - sumRetenciones);
				this.totalImpuestos = sumImpuestos;
				this.totalRetenciones = sumRetenciones;
				this.totalPago = this.subtotal + (this.totalImpuestos - this.totalRetenciones);
			} 
		} catch (Exception e) {
			control(true, 5, "Ocurrio un problema al eliminar el Impuesto desglosado", e);
			return "ERROR";
		} finally {
			this.subtotal = Utilerias.redondear(this.subtotal, 2);
			this.totalImpuestos = Utilerias.redondear(this.totalImpuestos, 2);
			this.totalRetenciones = Utilerias.redondear(this.totalRetenciones, 2);
			this.totalPago = Utilerias.redondear(this.totalPago, 2);
		}
		
		return "OK";
	}
   	
   	public String cambioMontoImpuesto() {
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
			control(true, 9, "cambioMontoImpuesto", e);
			return "ERROR";
		} finally {
			this.subtotal = Utilerias.redondear(this.subtotal, 2);
			this.totalImpuestos = Utilerias.redondear(this.totalImpuestos, 2);
			this.totalRetenciones = Utilerias.redondear(this.totalRetenciones, 2);
			this.totalPago = Utilerias.redondear(this.totalPago, 2);
		}
		
		return "OK";
	}
   	
   	public void actualizaTotalFaturas() {
   		double monto = 0;
   		
		try {
			control();
			this.totalFacturasReportadas = 0D;
			this.pojoCajaChica.setConFactura(0);
			this.pojoCajaChica.setSinFactura(0);
			if (! this.listFacturaConImpuestos.isEmpty()) {
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					if (var.getPojoFactura().getSubtotal() == null)
						var.getPojoFactura().setSubtotal(0D);
					if (var.getPojoFactura().getTotalImpuestos() == null)
						var.getPojoFactura().setTotalImpuestos(0D);
					if (var.getPojoFactura().getTotalRetenciones() == null)
						var.getPojoFactura().setTotalRetenciones(0D);
					
					monto = (var.getPojoFactura().getSubtotal() + (var.getPojoFactura().getTotalImpuestos() - var.getPojoFactura().getTotalRetenciones()));
					this.totalFacturasReportadas += Utilerias.redondear(monto, 2);
					//this.totalFacturasReportadas += ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones());
					//this.totalFacturasReportadas = Utilerias.redondear(this.totalFacturasReportadas, 2);
					
					if (var.getPojoFactura().getFacturado() == 0) {
						monto = this.pojoCajaChica.getSinFactura() + monto;
						//monto += (var.getPojoFactura().getSubtotal() + (var.getPojoFactura().getTotalImpuestos() - var.getPojoFactura().getTotalRetenciones()));
						monto = Utilerias.redondear(monto, 2);
						this.pojoCajaChica.setSinFactura(monto);
					} else if (var.getPojoFactura().getFacturado() == 1) {
						monto = this.pojoCajaChica.getConFactura() + monto;
						//monto += (var.getPojoFactura().getSubtotal() + (var.getPojoFactura().getTotalImpuestos() - var.getPojoFactura().getTotalRetenciones()));
						monto = Utilerias.redondear(monto, 2);
						this.pojoCajaChica.setConFactura(monto);
					}
				}
			}
			
			// Actualizamos el monto
			this.pojoCajaChica.setMonto(this.totalFacturasReportadas);
		} catch (Exception e) {
			control("Ocurrio un problema al actualizar los totales de las Facturas", e);
		}
	}

   	public String validaMontoImpuestos() {
   		double porcentaje = 0;
   		
		try {
			this.encontroMismoGrupo = false;
			this.tipoMensaje = 0;
			
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					if (var1.getValor() == null || var1.getValor().doubleValue() <= 0) {
						porcentaje = Double.parseDouble(var1.getIdImpuesto().getAtributo1());
						if (porcentaje > 0) {
							control(3, "Monto de Impuestos debe ser mayor a cero");
							return "ERROR";
						}
					}  
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al validar los montos de los Impuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
   	public String validaMontoFactura() {
		try {
			control();
			if (this.montoTotalFactura <= 0) {
				control(4, "Monto de Factura debe ser mayor a cero");
				return "ERROR";
			}	
		} catch (Exception e) {
			control("Ocurrio un problema al validar el monto de la Factura", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
	public String validaMontoFacturaContraTotalImpuestos() {
		try {
			encontroMismoGrupo=false;
			Double sumaImpuestos = 0D;
			this.tipoMensaje = 0;
			
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					sumaImpuestos = sumaImpuestos + var1.getValor().doubleValue();
				}
			}
			
			if (sumaImpuestos > this.montoTotalFactura) {
				this.encontroMismoGrupo = true;
				this.tipoMensaje=5;
				return "ERROR";
			}	
		} catch (Exception e) {			
			log.error("error en validaMontoFacturaContraTotalImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
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
						this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
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
						this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
						this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
						this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());

						importeImpuesto = 0D;
						porcentajeImpuesto = Double.parseDouble(var.getImpuestoId().getAtributo1().trim());
						if (porcentajeImpuesto > 0) {
							porcentajeImpuesto = (porcentajeImpuesto / 100); 
							importeImpuesto = montoBaseImpuestos - (montoBaseImpuestos / (1 + porcentajeImpuesto)); //(montoBaseImpuestos * porcentajeImpuesto); // montoBaseImpuestos - (montoBaseImpuestos / porcentajeImpuesto); //this.montoTotalFactura - (this.montoTotalFactura / porcentajeImpuesto);
							importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
						}
						
						sumImpuestos += importeImpuesto; 
						sumImpuestos = Utilerias.redondear(sumImpuestos, 4); 
						this.pojoDesgloImpto.setValor(new BigDecimal(importeImpuesto));	
						this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
					}
					
					// Retenciones
					this.listRetDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","AC", this.pojoConceptoGtos.getId());
					if (! this.listRetDelGasto.isEmpty()) {
						for (GastosImpuestoExt var : this.listRetDelGasto) {
							this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
							this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
							this.pojoDesgloImpto.setValor(BigDecimal.ZERO);				
							this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
							this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
							this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());

							importeImpuesto = 0;
							porcentajeImpuesto = Double.parseDouble(var.getImpuestoId().getAtributo1().trim());
							if (porcentajeImpuesto > 0) {
								porcentajeImpuesto = (porcentajeImpuesto / 100); 
								importeImpuesto = montoBaseImpuestos - (montoBaseImpuestos / (1 + porcentajeImpuesto)); //(montoBaseImpuestos * porcentajeImpuesto); // montoBaseImpuestos - (montoBaseImpuestos / porcentajeImpuesto); //this.montoTotalFactura - (this.montoTotalFactura / porcentajeImpuesto);
								importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
							}

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
					if (porcentajeImpuesto > 0) 
						porcentajeImpuesto = (porcentajeImpuesto / 100); //1 + (porcentajeImpuesto / 100);
					
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta())) {
						if (porcentajeImpuesto > 0) {
							importeImpuesto = this.montoTotalFactura / (1 + porcentajeImpuesto);
							importeImpuesto = (importeImpuesto * porcentajeImpuesto);
							importeImpuesto = Utilerias.redondear(importeImpuesto, 2);
							//importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto); // this.montoTotalFactura - (this.montoTotalFactura / porcentajeImpuesto);
							//importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
						}

						sumRetenciones += importeImpuesto;
						sumRetenciones = Utilerias.redondear(sumRetenciones, 4); 
					} else if ("DE".equals(var.getIdImpuesto().getTipoCuenta())) {
						if (porcentajeImpuesto > 0) {
							importeImpuesto = (montoBaseImpuestos * porcentajeImpuesto); // this.montoTotalFactura - (this.montoTotalFactura / porcentajeImpuesto);
							importeImpuesto = Utilerias.redondear(importeImpuesto, 4);
						}
						
						sumImpuestos += importeImpuesto;
						sumImpuestos = Utilerias.redondear(sumImpuestos, 4); 
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
	
	public String buscarRet() {
   		try {
   			this.tipoMensaje = 0;
   			this.mensaje = "";
   			this.listRetEncontradas = ifzConValores.findLikeByProperty(this.campoBusqRet,this.valBusqRet, 5);
   			if (listRetEncontradas.isEmpty() || listRetEncontradas == null) {
   				tipoMensaje=12;
   				encontroMismoGrupo=true;
   			}
   		} catch (Exception e) {
   			this.tipoMensaje = 9;
   			log.error("Error en metodo buscarRet", e);
   		}
   		
   		return "OK";
   	}
   	
	public String validaSaldoCuentaPropios() {
		try {
			encontroMismoGrupo = false;
			tipoMensaje = 0;
			mensaje = "";
			
			if (pojoCajaChica.getMonto() <= 0) {
				tipoMensaje = 8;					
				encontroMismoGrupo = true;	
				mensaje = "";
				return "ERROR";
			}

			// do nothing
			/*if (this.pojoCtas.getId() > 0L) {
				if (!ifzCtas.haySaldoSuficiente(pojoGtosComp.getMonto(), this.pojoCtas.getCtaBancoId())) {
					tipoMensaje=13;					
					encontroMismoGrupo=true;	
					mensaje="";
					return "ERROR";
				}
			}*/
		} catch (Exception e) {
			log.error("Error en el metodo validaSaldoCuentaPropios.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
   	public String evaluaSiCambioGasto() {
		try { 
			GeneraListaProveedores();
			this.existeRetencion = true;
			if (! "".equals(this.conceptoGastoPrevio)) {
				if (! this.conceptoGastoPrevio.equals(this.conceptoGasto)) {
					this.nombreProveedor = "";
					this.listDesgloseImpuestos.clear();
					this.listDesgloseRetenciones.clear();
					this.listRetEncontradas.clear();	
					desglosaImpuestos();
					this.conceptoGastoPrevio = conceptoGasto;
				}
			} else {
				conceptoGastoPrevio = conceptoGasto;
			}
		} catch (Exception e) {
			log.error("error en evaluaSiCambioGasto", e);
			return "ERROR";
		}
		
		return "OK";
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

	public String cancelarCheques() {
		Respuesta resp = new Respuesta();
			
   		try {			
   			resp = ifzCajaChica.salvar(this.pojoCajaChica);
			if (! "BIEN".equals(resp.getRespuesta()) && resp.getResultado() == -1) {
				encontroMismoGrupo = true;// esta variable termine usandola como bandera gral
				this.mensaje = resp.getRespuesta();
				this.tipoMensaje=6;	
				
				//antes le mostraba el mensaje que me regresaba la funcion CANCELA_CHEQUES pero ahora solo le dire que se guardo		
				guardar();
				nuevo();
			}
   		} catch (Exception e) {
			log.error("error en cancelarCheques", e);
			return "ERROR";
		}
		
		return "OK";
   	}
   	
   	public String validaGrupoImpuestos() {
		int contador = 0;
		
		try {
			control();
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					contador = 0;
					for (PagosGastosDetImpuestosExt var2 : this.listDesgloseImpuestos) {
						if (this.encontroMismoGrupo) 
							continue;
						if (! "0".equals(var2.getIdImpuesto().getAtributo2())) { 
							// porque el cero es general esos pueden existir N impuestos al mimos tiempo
							if (var1.getIdImpuesto().getAtributo2().equals(var2.getIdImpuesto().getAtributo2())) {
								contador = contador + 1;
								if (contador == 2) {
									control("Ocurrio un problema desconocido. Contacte Administrador");
									return "ERROR";
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {			
			log.error("error en validaGrupoImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
   	public void agregaRet() {
		try {
			control();
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getIdImpuesto().getId() == this.pojoNvaRet.getId()) {
						control(14, "Ocurrio un problema con Retencion");
						return;
					}
				}
			}
			
			this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
			this.pojoDesgloImpto.setIdImpuesto(this.pojoNvaRet);
			this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
			this.pojoDesgloImpto.setValor(BigDecimal.ZERO);
			this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
			this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
			this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
			this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
		} catch (Exception e) {
			control("Ocurrio un problema al agregar la Retencion", e);
		}
	}
   	
	public List<Persona> autoacompletaBeneficiario(Object obj) {
		List<Empleado> listEmpleados = null;
		
		try {
			if (this.listBeneficiarios != null)
				this.listBeneficiarios.clear();
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			listEmpleados = this.ifzEmpleado.findLike(obj.toString(), null, 0, 100);
			if (listEmpleados != null && ! listEmpleados.isEmpty()) {
				for (Empleado var : listEmpleados)
					this.listBeneficiarios.add(this.ifzPersonas.findById(var.getIdPersona()));
			}

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
	
	public void GeneraListaProveedores() {
		Pattern pat = null;
		Matcher match = null;
		
		try {
			//validando gasto para poder presentar  una lista de proveedores en base al gasto seleccionado
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.conceptoGasto);			
			if (match.find())							
				this.pojoConceptoGtos = this.ifzConValores.findById(Long.valueOf(match.group(1)));
		} catch (Exception e) {
			log.error("error en GeneraListaProveedores", e);
		}
	}
	
	private String getGeneraNombreProveedor(PersonaExt prov) {
		if (prov != null)
			return prov.getId() + " - " + prov.getNombre();// + ("P".equals(tipoPersona) ? (" " + (prov.getPrimerApellido() != null ? prov.getPrimerApellido() : "") + " " + (prov.getSegundoApellido() != null ? prov.getSegundoApellido() : "")) : "");
		return "";
		
		/*if (prov == null)
			return "";
		
		return prov.getId() + " - " + prov.getNombre() + " " 
			+ (prov.getPrimerApellido() != null ? prov.getPrimerApellido() : "") + " " 
			+ (prov.getSegundoApellido() != null ? prov.getSegundoApellido() : "");*/
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
	
   	// ---------------------------------------------------------------------------
   	// OBRAS
   	// ---------------------------------------------------------------------------
	
	public void nuevaBusquedaObras() {
		control();
		this.campoBusquedaObra = this.tiposBusquedaObra.get(0).getValue().toString();
		this.valorBusquedaObra = "";
		this.paginacionObras = 1;
		this.tipoObraBusquedaObra = 0;
		this.avanzada = false;
		
		this.listObras = new ArrayList<Obra>();
		this.pojoObra = null;
	}
	
	public void buscarObras() {
		try {
			control();
			this.paginacionObras = 1;
			if (this.listObras != null)
				this.listObras.clear();
			
			log.info("Buscando obras tipo " + this.tipoObraBusquedaObra + " - " + this.campoBusquedaObra + ": " + this.valorBusquedaObra);
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valorBusquedaObra, 0L, 0L, this.tipoObraBusquedaObra, (this.tipoObraBusquedaObra == 4), false, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Todas, "", 0);//.findLikeProperty(this.campoBusquedaObra, this.valorBusquedaObra.trim().replace(" ", "%"), this.tipoObraBusquedaObra);
			if (this.listObras == null || this.listObras.isEmpty()) {
				control(12, "Busqueda sin resultados");
				return;
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
			if (this.pojoObra == null) {
				log.info("ERROR - Debe seleccionar una Obra");
				control("Debe seleccionar una obra");
				return;
			}

			// Asignamos la obra al Gasto a Comprobar
			this.pojoCajaChica.setIdObra(this.pojoObra);
			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		}
	}
		
	public void cambioObra() {
		try {
			control();
			this.tipoObra = "";
		} catch (Exception e) {
			control("error en cambioObra", e);
		}
	}
	
	public void quitarObra() {
		try {
			control();
			// Desasignamos la obra a la Caja Chica
			this.pojoCajaChica.setIdObra(null);
			this.pojoObra = null;
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		}
	}

   	// ---------------------------------------------------------------------------
   	// PERSONA/NEGOCIO
   	// ---------------------------------------------------------------------------
		
	public void nuevaBusquedaPersonaNegocio() {
		control();
		this.campoBusquedaPersonaNegocio = this.tiposBusquedaPersonaNegocio.get(0).getValue().toString();
   		this.valorBusquedaPersonaNegocio = "";
   		this.paginacionBusquedaPersonaNegocio = 1;
   		
   		this.listPersonasNegocios = new ArrayList<PersonaNegocioVista>();
   		this.pojoPersonaNegocio = new PersonaNegocioVista();
	}
	
	public void buscarPersonaNegocio() {
		String value = "";
		
		try {
			control();
			if (this.campoBusquedaPersonaNegocio == null || "".equals(this.campoBusquedaPersonaNegocio))
				this.campoBusquedaPersonaNegocio = this.tiposBusquedaPersonaNegocio.get(0).getValue().toString();
			value = this.valorBusquedaPersonaNegocio;
			value = (value == null || ! "".equals(value.trim())) ? "" : value.trim();
			if (value.contains(" "))
				value = value.replace(" ", "%");
			log.info("Busqueda Persona/Negocio. Buscar por " + this.campoBusquedaPersonaNegocio + ": " + this.valorBusquedaPersonaNegocio);
			this.ifzPerNeg.setInfoSesion(this.loginManager.getInfoSesion());
			this.listPersonasNegocios = this.ifzPerNeg.findLikeProperty(this.campoBusquedaPersonaNegocio, value, null, 0);
			if (this.listPersonasNegocios.isEmpty()) {
				control(12, "Busqueda sin resultados");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Beneficiarios", e);
		}
	}
	
	public void seleccionarPersonaNegocio() {
		try {
			control();
			if (this.pojoPersonaNegocio == null)
				return;
			
			log.info("Busqueda Persona/Negocio. Selecciono: " + this.pojoPersonaNegocio.getId() + " - " + this.pojoPersonaNegocio.getNombre() + " - " + this.pojoPersonaNegocio.getTipo());
			this.pojoBeneficiarios = this.ifzCajaChica.findPersonaById(this.pojoPersonaNegocio.getId(), this.pojoPersonaNegocio.getTipo());
			
			this.nombreBeneficiario = this.pojoBeneficiarios.getNombre();
			if ("P".equals(this.pojoPersonaNegocio.getTipo())) {
				this.nombreBeneficiario = (this.pojoBeneficiarios.getPrimerNombre() + " " 
					+ ((this.pojoBeneficiarios.getSegundoNombre()   != null && ! "".equals(this.pojoBeneficiarios.getSegundoNombre().trim()))   ? this.pojoBeneficiarios.getSegundoNombre().trim()   : "") + " " 
					+ ((this.pojoBeneficiarios.getPrimerApellido()  != null && ! "".equals(this.pojoBeneficiarios.getPrimerApellido().trim()))  ? this.pojoBeneficiarios.getPrimerApellido().trim()  : "") + " " 
					+ ((this.pojoBeneficiarios.getSegundoApellido() != null && ! "".equals(this.pojoBeneficiarios.getSegundoApellido().trim())) ? this.pojoBeneficiarios.getSegundoApellido().trim() : "")).trim();
				if (this.nombreBeneficiario == null || "".equals(this.nombreBeneficiario) || "null".equals(this.nombreBeneficiario)) {
					this.nombreBeneficiario = "SIN NOMBRE";
					if (this.pojoBeneficiarios.getNombre() != null && ! "".equals(this.pojoBeneficiarios.getNombre()))
						this.nombreBeneficiario = this.pojoBeneficiarios.getNombre();
				}
			}
			
			// Eliminamos el doble espacio
			this.nombreBeneficiario = this.nombreBeneficiario.replace("  ", " ");
			log.info("Nombre obtenido del pojo: " + this.nombreBeneficiario);
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Beneficiario seleccionado", e);
		}
	}

   	// ---------------------------------------------------------------------------
	// CONCEPTOS
   	// ---------------------------------------------------------------------------
	
	public void nuevaBusquedaConceptos() {
		control();
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
   		this.valorBusquedaConceptos = "";
   		this.pagBusquedaConceptos = 1;
   		
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
				log.info("ERROR 12: Busqueda sin resultados");
				control(12, "Busqueda sin resultados");
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
				control("Ocurrio un problema al intentar recuperar el Concepto seleccionado");
				return;
			}
			
			this.conceptoGasto = this.pojoConcepto.getId() + " - " + this.pojoConcepto.getDescripcion();
			desglosaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al buscar los Conceptos para Gastos", e);
		} finally {
			nuevaBusquedaConceptos();
		}
	}

	public void toggleAvanzadaConceptos() {
		this.avanzadaConceptos = ! this.avanzadaConceptos;
	}
	
   	// ---------------------------------------------------------------------------
	// CARGA Y DESCARGA DE FACTURA XML
   	// ---------------------------------------------------------------------------

	private void reiniciaFactura() {
		this.facturaId = 0L;
		this.facturaRfc = "";
		this.facturaEmisor = "";
		this.facturaMoneda = "";
		this.facturaTipoCambio = 0D;
		this.facturaDescuento = 0D;
		this.facturaSubtotal = 0D;
		this.facturaTotal = 0D;
	}

	public void nuevaCarga() {
		this.fileName = "";
		this.fileSrc = null;
		this.fileLoaded = false;
		if (this.isDebug) 
			log.info("\n\nCarga preparada :: " + this.fileLoaded + "\n\n");
	}
	
	public void uploadListener(UploadEvent event) {
		UploadItem item = null;
		
		try {
			control();
			item = event.getUploadItem();
			if (this.fileName.equals(item.getFileName())) 
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
			control("Ocurrio un problema al cargar el archivo", e);
		}
	}
	
	public void analizarArchivo() throws Exception {
		SimpleDateFormat formatter = null;
		boolean emisorDesconocido = false;
		List<Persona> listProveedores = null;
		PersonaExt pojoProveedor = null;
		String tipoPersona = "";
		Respuesta respuesta = new Respuesta();
		String facturaFolio = "";
		String fileName = "";
		// ------------------------------------------
		String comprobados = "";
		String facturaFecha = "";
		
		try {
			control();
			reiniciaFactura();
			if (this.fileSrc == null) {
				log.warn("VALIDACION - No ha especificado ninguna factura (*.xml)");
				control("No ha especificado ninguna factura (*.xml)");
				return;
			} 
			
			if (! this.fileLoaded) {
				log.warn("VALIDACION - No ha especificado ninguna factura (*.xml)");
				control(-1, "Ocurrio un problema al cargar el XML.\nCierre la ventana de carga e intente nuevamente");
				return;
			}
			
			this.ifzCajaChicaDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzCajaChicaDetalles.cargaFacturaXML(this.fileSrc, TiposGastoCXP.CajaChica, this.pojoCajaChica.getId());//, this.pojoCajaChica.getId());
			if (respuesta.getErrores().getCodigoError() != 0L) {
				reiniciaFactura();
				if (respuesta.getBody().getValor("comprobados") != null)
					comprobados = desglozaComprobados(respuesta.getBody().getValor("comprobados").toString());
				log.warn("ERROR INTERNO - Ocurrio un problema al intentar guardar la factura. " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError() + "\nNo se cargo la Factura." + comprobados);
				return;
			}
			
			// recupero los datos de la factura cargada
			this.facturaId = (Long) respuesta.getBody().getValor("idComprobante");
			this.facturaRfc = respuesta.getBody().getValor("comprobanteRfc").toString();
			this.facturaMoneda = respuesta.getBody().getValor("comprobanteMoneda").toString();
			this.facturaDescuento = Double.parseDouble(respuesta.getBody().getValor("comprobanteDescuento").toString());
			this.facturaSubtotal = Double.parseDouble(respuesta.getBody().getValor("comprobanteSubtotal").toString());
			this.facturaTotal = Double.parseDouble(respuesta.getBody().getValor("comprobanteTotal").toString());
			this.facturaTipoCambio = 1.0;
			if (respuesta.getBody().getValor("comprobanteTipoCambio") != null)
				this.facturaTipoCambio = Double.parseDouble(respuesta.getBody().getValor("comprobanteTipoCambio").toString());
			facturaFecha = respuesta.getBody().getValor("comprobanteFecha").toString();
			
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
				this.pojoComprobadoGto.setIdProveedor(pojoProveedor);
				this.facturaEmisor = pojoProveedor.getNombre();
			}

			this.facturaActualizar = false;
			if ((this.facturaEmisor == null || "".equals(this.facturaEmisor.trim())) || (facturaFolio == null || "".equals(facturaFolio.trim())))
				this.facturaActualizar = true;

			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.pojoComprobadoGto.setFecha(formatter.parse(facturaFecha));
			this.pojoComprobadoGto.setTipoPersonaProveedor(tipoPersona);
			this.pojoComprobadoGto.setFacturaRfc(this.facturaRfc);
			this.pojoComprobadoGto.setNombreProveedor(this.facturaEmisor);
			this.pojoComprobadoGto.setFacturaSerie(respuesta.getBody().getValor("comprobanteSerie").toString());
			this.pojoComprobadoGto.setFacturaFolio(respuesta.getBody().getValor("comprobanteFolio").toString());
			this.pojoComprobadoGto.setReferencia(facturaFolio);
			this.montoTotalFactura = Double.parseDouble(respuesta.getBody().getValor("comprobanteTotal").toString());
			// Aplicamos descento
			if (this.facturaDescuento > 0)
				this.facturaSubtotal -= this.facturaDescuento;
			
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
			if (! this.ifzFtp.put(this.fileSrc, this.ftpDigitalizacionRuta + fileName)) {
				log.warn("ERROR FTP - Ocurrio un problema al intentar guardar la factura en el servidor");
				control("Ocurrio un problema al intentar guardar la factura en el servidor");
			}
			log.info("FTP - Carga completa");
			if (emisorDesconocido)
				control("El Emisor de la Factura no existe en Negocios/Personas. RFC " + this.facturaRfc);
			desglosaImpuestos();
			nuevaCarga();
		} catch (Exception e) {
			control("Ocurrio un problema al procesar la Factura indicada.", e);
		} 
	}
	
	public void descargarArchivo() {
		String fileName = "";
		
		try {
			control();
			if (this.reg == null) {
				control(20, "No tiene asignada ninguna factura");
				return;
			}

			if (this.reg.getPojoFactura().getIdCfdi() == null || this.reg.getPojoFactura().getIdCfdi() <= 0L)
				this.reg.getPojoFactura().setIdCfdi(0L);

			this.facturaId = this.reg.getPojoFactura().getIdCfdi();
			if (this.facturaId == null || this.facturaId <= 0L) {
				control(20, "No tiene asignada ninguna factura");
				return;
			}

			this.facturaRfc = this.ifzCajaChicaDetalles.getFacturaProperty(this.facturaId, "rfcEmisor");
			this.facturaEmisor = this.ifzCajaChicaDetalles.getFacturaProperty(this.facturaId, "nombre");
			if (this.facturaEmisor == null || "".equals(this.facturaEmisor.trim()))
				this.facturaEmisor = this.facturaRfc;

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
				control(21, "No se encontro el archivo en el servidor");
				return;
			}

			// Ponemos los datos en session
			this.httpSession.setAttribute("nombreDocumento", fileName);
			this.httpSession.setAttribute("formato", "xml");
			this.httpSession.setAttribute("contenido", this.fileSrc);
		} catch (Exception e) {
    		control(true, 9, "Ocurrio un problema al descargar el XML", e);
		}
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
	}
	
   	// ---------------------------------------------------------------------------
   	// METODOS DE CONTROL
   	// ---------------------------------------------------------------------------

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

	private void control() {
		this.encontroMismoGrupo = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(String mensaje) {
		control(true, -1, mensaje, null);
	}
	
	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null)
			mensaje = "Ocurrio un problema al realizar la accion solicitada";
		this.encontroMismoGrupo = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		
		log.error("\n\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: CAJAS CHICAS :: " + this.tipoMensaje + " - " + mensaje, throwable);
	}

	private List<PagosGastosDetImpuestosExt> copiaListas(List<PagosGastosDetImpuestosExt> fuente) {
   		List<PagosGastosDetImpuestosExt> resultado = new ArrayList<PagosGastosDetImpuestosExt>();
   		
   		for (PagosGastosDetImpuestosExt pgi : fuente) 
   			resultado.add(((PagosGastosDetImpuestosExt) pgi.clone()));
   		return resultado;
   	}
	
	public void cargarSucursales() {
   		try {
			if (this.listSucursales == null)
				this.listSucursales = new ArrayList<Sucursal>();
			this.listSucursales.clear();
			
			if (this.listSucursalesItems == null)
				this.listSucursalesItems = new ArrayList<SelectItem>();
			this.listSucursalesItems.clear();
			
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSucursales = this.ifzSucursal.findAll(); // findLikePropiedad("sucursal", ""); //this.listSucursales = this.ifzSucursal.buscarSucursales("sucursal", "");
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
			if (this.listCuentas == null)
				this.listCuentas = new ArrayList<CuentaBancaria>();
			this.listCuentas.clear();
			
			if (this.listCuentasItems == null)
				this.listCuentasItems = new ArrayList<SelectItem>();
			this.listCuentasItems.clear();

			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCuentas = this.ifzCtas.findLikeClaveNombreCuenta("", 0, this.sucursalesVisibles, 1L);
			if (this.listCuentas != null && ! this.listCuentas.isEmpty()) {
				for (CuentaBancaria var : this.listCuentas)
					this.listCuentasItems.add(new SelectItem(var.getId() + " - " + var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta(), var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta()));
			}
		} catch (Exception e) {
			log.error("error en cargarCuentas", e);
		}
   	}
	
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

	// ------------------------------------------------------------------------------------------
	// PERFILES
	// ------------------------------------------------------------------------------------------

	private void getPerfiles() {
		String valPerfil = "";
		
		if (this.loginManager == null) 
			return;
		
		try {
			// EGRESOS_OPERACION ---> ADMINISTRATIVO
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_EDITAR");
			this.perfilPermiteEditar = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);

			// CAJA_CHICA_ABONOS
			valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_ABONOS");
			this.PERFIL_CAJA_CHICA_ABONOS = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			// REQUIERE OBRA
			valPerfil = this.loginManager.getAutentificacion().getPerfil("REQUIERE_OBRA");
			this.perfilRequiereObra = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			// LIMITE CAJA CHICA
			valPerfil = this.loginManager.getAutentificacion().getPerfil("LIMITE_CAJA_CHICA");
			this.perfilLimiteCajaChica = (valPerfil != null && ! "".equals(valPerfil)) ? Double.valueOf(valPerfil.trim()) : 0;
			
			// VISTO BUENO
			valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_VOBO");
			this.perfilCajaChicaVoBo = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			// AUTORIZACION
			valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_AUTORIZACION");
			this.perfilCajaChicaAutorizacion = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			// DIVICION DE CAJA: VoBo y Autorizacion
			valPerfil = this.loginManager.getAutentificacion().getPerfil("DIVIDIR_CAJA");
			this.perfilDividirCajaChica = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			if (! "S".equals(this.perfilDividirCajaChica)) {
				this.perfilCajaChicaVoBo = "";
				this.perfilCajaChicaAutorizacion = "";
			}
		} catch (Exception e) {
			log.error("Error al recuperar los perfiles en CuentasPorPagar.CajaChicaAction.getPerfiles" , e);
		}
	}
	
	private void actualizarPerfil(String perfil) {
		String valPerfil = "";
		
		if (this.loginManager == null) 
			return;
		
		try {
			switch (perfil) {
				case "REQUIERE_OBRA" : 
					valPerfil = this.loginManager.getAutentificacion().getPerfil("REQUIERE_OBRA").trim();
					this.perfilRequiereObra = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
				break;
					
				case "LIMITE_CAJA_CHICA" : 
					valPerfil = this.loginManager.getAutentificacion().getPerfil("LIMITE_CAJA_CHICA").trim();
					this.perfilLimiteCajaChica = (valPerfil != null && ! "".equals(valPerfil)) ? Double.valueOf(valPerfil.trim()) : 0;
				break;
					
				case "CAJA_CHICA_VOBO" : 
					valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_VOBO").trim();
					this.perfilCajaChicaVoBo = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
				break;
				
				case "CAJA_CHICA_AUTORIZACION" : 
					valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_AUTORIZACION").trim();
					this.perfilCajaChicaAutorizacion = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
				break;
				
				case "DIVIDIR_CAJA" : 
					valPerfil = this.loginManager.getAutentificacion().getPerfil("DIVIDIR_CAJA").trim();
					this.perfilDividirCajaChica = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
					
					if (! "S".equals(this.perfilDividirCajaChica)) {
						this.perfilCajaChicaVoBo = "";
						this.perfilCajaChicaAutorizacion = "";
					}
				break;
			}
		} catch (Exception e) {
			log.error("Erro al recuperar el perfil [" + perfil + "] en CuentasPorPagar.CajaChicaAction.actualizarPerfil" , e);
		}
	}

   	// ---------------------------------------------------------------------------
   	// PROPIEDADES
   	// ---------------------------------------------------------------------------

   	public String getTituloComprobacion() {
   		if (this.pojoComprobadoGto.getId() != null && this.pojoComprobadoGto.getId() > 0L)
   			return "Comprobacion " + this.pojoComprobadoGto.getId();
   		return "Nueva Comprobacion";
   	}
   	
   	public void setTituloComprobacion(String value) { }

   	public boolean getPermiteEditar() {
   		if (this.pojoCajaChica != null && this.pojoCajaChica.getId() != null && this.pojoCajaChica.getId() > 0L)
   			return (this.perfilPermiteEditar || this.paramsRequest.containsKey("DETEDIT") && "1".equals(this.paramsRequest.get("DETEDIT")));
   		return true;
   	}
   	
   	public void setPermiteEditar(boolean value) {}
   	
   	public boolean getPermiteVoBo() {
   		return ("S".equals(this.perfilDividirCajaChica) && "S".equals(this.perfilCajaChicaVoBo));
   	}
   	
   	public void setPermiteVoBo(boolean value) {}
   	
   	public boolean getPermiteAutorizar() {
   		return ("S".equals(this.perfilDividirCajaChica) && "S".equals(this.perfilCajaChicaAutorizacion));
   	}

   	public void setPermiteAutorizar(boolean value) {}
   	
   	public String getObraNombre() {
   		if (this.pojoCajaChica != null && this.pojoCajaChica.getIdObra() != null && this.pojoCajaChica.getIdObra().getId() != null && this.pojoCajaChica.getIdObra().getId() > 0L)
   			return this.pojoCajaChica.getIdObra().getId() + " - " + this.pojoCajaChica.getIdObra().getNombre();
   		return "";
	}
	
   	public void setObraNombre(String value) {}
	
	public String getFacturaId() {
		if (this.facturaId != null && this.facturaId > 0L)
			return this.facturaId.toString();
		return "";
	}
	
	public void setFacturaId(String value) {}

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

	public String getNombreBeneficiario() {
		if (this.pojoBeneficiarios != null && this.pojoBeneficiarios.getId() > 0L)
			return this.pojoBeneficiarios.getId() + " - " + this.nombreBeneficiario;
		return nombreBeneficiario;
	}

	public void setNombreBeneficiario(String nombreBeneficiario) {
		this.nombreBeneficiario = nombreBeneficiario;
	}

	public FacturaImpuestos getReg() {
		return reg;
	}

	public void setReg(FacturaImpuestos reg) {
		this.reg = reg;
	}

	public PagosGastos getPojoCaja() {
		return pojoCaja;
	}
	
	public void setPojoCaja(PagosGastos pojoCaja) {
		this.pojoCaja = pojoCaja;
	}
	
	public PagosGastosExt getPojoGtosComp() {
		return pojoCajaChica;
	}

	public void setPojoGtosComp(PagosGastosExt pojoGtosComp) {
		this.pojoCajaChica = pojoGtosComp;
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
		return pojoComprobadoGto;
	}

	public void setPojoComprobadoGto(PagosGastosDetExt pojoComprobadoGto) {
		this.pojoComprobadoGto = pojoComprobadoGto;
	}
	
	public String getReferencia() {
		return this.pojoComprobadoGto.getReferencia() != null ? this.pojoComprobadoGto.getReferencia() : "";
	}

	public void setReferencia(String referencia) {
		this.pojoComprobadoGto.setReferencia(referencia);
	}

	public String getFacturaEmisor() {
		return facturaEmisor;
	}

	public void setFacturaEmisor(String facturaEmisor) {
		this.facturaEmisor = facturaEmisor;
	}
	
	public String getFacturaMoneda() {
		return facturaMoneda;
	}

	public void setFacturaMoneda(String facturaMoneda) {
		this.facturaMoneda = facturaMoneda;
	}

	public Double getFacturaTipoCambio() {
		return facturaTipoCambio;
	}

	public void setFacturaTipoCambio(Double facturaTipoCambio) {
		this.facturaTipoCambio = facturaTipoCambio;
	}
	
	public Date getFechaCompruebaGto() {
		return this.pojoComprobadoGto.getFecha()!= null ? this.pojoComprobadoGto.getFecha() : Calendar.getInstance().getTime();
	}

	public void setFechaCompruebaGto(Date fechaCompruebaGto) {
		this.pojoComprobadoGto.setFecha(fechaCompruebaGto);
	}
	
	public boolean getFacturaCredito() {
		return this.pojoComprobadoGto.getEsCredito();
	}

	public void setFacturaCredito(boolean facturaCredito) {
		this.pojoComprobadoGto.setEsCredito(facturaCredito);
	}

	public boolean getManoObra() {
		return this.pojoComprobadoGto.getManoObra();
	}

	public void setManoObra(boolean manoObra) {
		this.pojoComprobadoGto.setManoObra(manoObra);
	}
	
	public Long getPagosGastosDetId() {
		return this.pojoComprobadoGto.getId() != null ? this.pojoComprobadoGto.getId() : 0L;
	}

	public void setPagosGastosDetId(Long pagosGastosDetId) {
		this.pojoComprobadoGto.setId(pagosGastosDetId);
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
	
	public Integer getNoCheque() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getNoCheque() != null)
			return this.pojoCajaChica.getNoCheque();
		return 0;
	}

	public void setNoCheque(Integer noCheque) {
		this.pojoCajaChica.setNoCheque(null);
		if (noCheque != null && noCheque > 0)
			this.pojoCajaChica.setNoCheque(noCheque);
	}
	
	public Date getFecha() {
		return this.pojoCajaChica.getFecha() != null ? this.pojoCajaChica.getFecha() : Calendar.getInstance().getTime();
	}

	public void setFecha(Date fecha) {
		this.pojoCajaChica.setFecha(fecha);
	}
	
	public double getMonto() {
		return this.pojoCajaChica.getMonto() != null ? this.pojoCajaChica.getMonto() : 0;
	}
	
	public void setMonto(double monto) { }
	
	public String getOperacion() {
		return this.pojoCajaChica.getOperacion() != null ? this.pojoCajaChica.getOperacion():"";
	}

	public void setOperacion(String operacion) {
		this.pojoCajaChica.setOperacion(operacion);
	}
	
	public Long getPagosGastosId() {
		return this.pojoCajaChica.getId() != null ? this.pojoCajaChica.getId() : 0;
	}

	public void setPagosGastosId(Long pagosGastosId) {
		this.pojoCajaChica.setId(pagosGastosId);
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

	public void setListDesgloseImpuestos(List<PagosGastosDetImpuestosExt> listDesgloseImpuestos) {
		this.listDesgloseImpuestos = listDesgloseImpuestos;
	}

	public List<GastosImpuestoExt> getListImpuestosDelGasto() {
		return listImpuestosDelGasto;
	}

	public void setListImpuestosDelGasto(List<GastosImpuestoExt> listImpuestosDelGasto) {
		this.listImpuestosDelGasto = listImpuestosDelGasto;
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

	public int getNumPaginaFact() {
		return numPaginaFact;
	}

	public void setNumPaginaFact(int numPaginaFact) {
		this.numPaginaFact = numPaginaFact;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	// Montos Factura
	
	public Double getFacturaTotal() {
		return facturaTotal;
	}

	public void setFacturaTotal(Double facturaTotal) {
		this.facturaTotal = facturaTotal;
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

	public Double getTotalFacturasReportadas() {
		return totalFacturasReportadas;
	}

	public void setTotalFacturasReportadas(Double totalFacturasReportadas) {
		this.totalFacturasReportadas = totalFacturasReportadas;
	}

	public boolean isCambioMonto() {
		return cambioMonto;
	}

	public void setCambioMonto(boolean cambioMonto) {
		this.cambioMonto = cambioMonto;
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

	public List<PagosGastos> getListCajaChica() {
		return listCajaChica;
	}

	public void setListCajaChica(List<PagosGastos> listCajaChica) {
		this.listCajaChica = listCajaChica;
	}

	public List<PagosGastosDetExt> getListFacturas() {
		return listFacturas;
	}

	public void setListFacturas(List<PagosGastosDetExt> listFacturas) {
		this.listFacturas = listFacturas;
	}

	public Long getValGpoGastos() {
		return valGpoGastos;
	}

	public void setValGpoGastos(Long valGpoGastos) {
		this.valGpoGastos = valGpoGastos;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public boolean isFacturaConLlave() {
		return facturaConLlave;
	}

	public void setFacturaConLlave(boolean facturaConLlave) {
		this.facturaConLlave = facturaConLlave;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getValTipoBusqueda() {
		return valorBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valorBusqueda = valTipoBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public boolean isComprobacionGenerada() {
		return comprobacionGenerada;
	}

	public void setComprobacionGenerada(boolean comprobacionGenerada) {
		this.comprobacionGenerada = comprobacionGenerada;
	}

	public void setMsgCancelar(String msgCancelar) {
		this.msgCancelar = msgCancelar;
	}

	public String getMsgCancelar() {
		return msgCancelar;
	}

	public ConValores getPojoNvaRet() {
		return pojoNvaRet;
	}

	public void setPojoNvaRet(ConValores pojoNvaRet) {
		this.pojoNvaRet = pojoNvaRet;
	}

	public List<PagosGastosDetImpuestosExt> getListDesgloseRet() {
		return listDesgloseRetenciones;
	}

	public void setListDesgloseRet(List<PagosGastosDetImpuestosExt> listDesgloseRet) {
		this.listDesgloseRetenciones = listDesgloseRet;
	}

	public List<GastosImpuestoExt> getListRetDelGasto() {
		return listRetDelGasto;
	}

	public void setListRetDelGasto(List<GastosImpuestoExt> listRetDelGasto) {
		this.listRetDelGasto = listRetDelGasto;
	}

	public List<ConValores> getListRetEncontradas() {
		return listRetEncontradas;
	}

	public void setListRetEncontradas(List<ConValores> listRetEncontradas) {
		this.listRetEncontradas = listRetEncontradas;
	}

	public String[] getListBusqRet() {
		return listBusqRet;
	}

	public void setListBusqRet(String[] listBusqRet) {
		this.listBusqRet = listBusqRet;
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

	public boolean isExisteRetencion() {
		return existeRetencion;
	}

	public void setExisteRetencion(boolean existeRetencion) {
		this.existeRetencion = existeRetencion;
	}

	public String getVarOper() {
		return varOper;
	}

	public void setVarOper(String varOper) {
		this.varOper = varOper;
	}

	public long getSucUsuario() {
		return sucUsuario;
	}

	public void setSucUsuario(long sucUsuario) {
		this.sucUsuario = sucUsuario;
	}

	public boolean isMuestraAgregar() {
		return muestraAgregar;
	}

	public void setMuestraAgregar(boolean muestraAgregar) {
		this.muestraAgregar = muestraAgregar;
	}

	public void setTipoReporte(int tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public int getTipoReporte() {
		return tipoReporte;
	}
	
	public String getTipoPersona() {
		return this.tipoPersona;
	}
	
	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
		this.beneficiario="";
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

	public String getValorBusquedaObra() {
		return valorBusquedaObra;
	}

	public void setValorBusquedaObra(String valorBusquedaObra) {
		this.valorBusquedaObra = valorBusquedaObra;
	}

	public String getCampoBusquedaObra() {
		return campoBusquedaObra;
	}

	public void setCampoBusquedaObra(String campoBusquedaObra) {
		this.campoBusquedaObra = campoBusquedaObra;
	}

	public String getTipoObra() {
		return tipoObra;
	}

	public void setTipoObra(String tipoObra) {
		this.tipoObra = tipoObra;
	}

	public String getPerfilRequiereObra() {
		return perfilRequiereObra;
	}

	public void setPerfilRequiereObra(String perfilRequiereObra) {
		this.perfilRequiereObra = perfilRequiereObra;
	}
	
	public double getLimiteCajaChica() {
		return this.perfilLimiteCajaChica;
	}

	public int getTipoCajaChica() {
		return tipoCajaChica;
	}

	public void setTipoCajaChica(int tipoCajaChica) {
		this.tipoCajaChica = tipoCajaChica;
	}
	
	public boolean isFacturado() {
		return sinFactura;
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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public long getSucursal() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getIdSucursal() != null)
			return this.pojoCajaChica.getIdSucursal().getId();
		return 0L;
	}

	public void setSucursal(long idSucursal) {
		if (idSucursal > 0L && this.pojoCajaChica != null) {
			for (Sucursal var : this.listSucursales) {
				if (var.getId() != idSucursal)
					continue;
				this.pojoCajaChica.setIdSucursal(var);
				break;
			}
		}
	}

	public List<Sucursal> getListSucursales() {
		return listSucursales;
	}

	public void setListSucursales(List<Sucursal> listSucursales) {
		this.listSucursales = listSucursales;
	}

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
	
	public String getPerfilCajaChicaVoBo() {
		return perfilCajaChicaVoBo;
	}
	
	public void setPerfilCajaChicaVoBo(String perfilCajaChicaVoBo) {
		this.perfilCajaChicaVoBo = perfilCajaChicaVoBo;
	}
	
	public String getPerfilCajaChicaAutorizacion() {
		return perfilCajaChicaAutorizacion;
	}
	
	public void setPerfilCajaChicaAutorizacion(String perfilCajaChicaAutorizacion) {
		this.perfilCajaChicaAutorizacion = perfilCajaChicaAutorizacion;
	}

	public String getPerfilDividirCajaChica() {
		return perfilDividirCajaChica;
	}

	public void setPerfilDividirCajaChica(String perfilDividirCajaChica) {
		this.perfilDividirCajaChica = perfilDividirCajaChica;
	}

	public boolean getVobo() {
		return vobo;
	}

	public void setVobo(boolean vobo) {
		this.vobo = vobo;
	}

	public boolean getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
	}

	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public List<PersonaNegocioVista> getListPersonasNegocios() {
		return listPersonasNegocios;
	}

	public void setListPersonasNegocios(List<PersonaNegocioVista> listPersonasNegocios) {
		this.listPersonasNegocios = listPersonasNegocios;
	}

	public PersonaNegocioVista getPojoPersonaNegocio() {
		return pojoPersonaNegocio;
	}

	public void setPojoPersonaNegocio(PersonaNegocioVista pojoPersonaNegocio) {
		this.pojoPersonaNegocio = pojoPersonaNegocio;
	}

	public List<SelectItem> getTiposBusquedaPersonaNegocio() {
		return tiposBusquedaPersonaNegocio;
	}

	public void setTiposBusquedaPersonaNegocio(List<SelectItem> tiposBusquedaPersonaNegocio) {
		this.tiposBusquedaPersonaNegocio = tiposBusquedaPersonaNegocio;
	}

	public String getCampoBusquedaPersonaNegocio() {
		return campoBusquedaPersonaNegocio;
	}

	public void setCampoBusquedaPersonaNegocio(String campoBusquedaPersonaNegocio) {
		this.campoBusquedaPersonaNegocio = campoBusquedaPersonaNegocio;
	}

	public String getValorBusquedaPersonaNegocio() {
		return valorBusquedaPersonaNegocio;
	}

	public void setValorBusquedaPersonaNegocio(String valorBusquedaPersonaNegocio) {
		this.valorBusquedaPersonaNegocio = valorBusquedaPersonaNegocio;
	}

	public int getPaginacionBusquedaPersonaNegocio() {
		return paginacionBusquedaPersonaNegocio;
	}

	public void setPaginacionBusquedaPersonaNegocio(int paginacionBusquedaPersonaNegocio) {
		this.paginacionBusquedaPersonaNegocio = paginacionBusquedaPersonaNegocio;
	}

	public boolean getEsAdministrativo() {
		return perfilEgresos;
	}
	
	public void setEsAdministrativo(boolean perfilEgresos) {
		this.perfilEgresos = perfilEgresos;
	}
	
	public boolean getBuscarAdministrativo() {
		return (this.tipoObraBusquedaObra == 4);
	}
	
	public void setBuscarAdministrativo(boolean tipoObraBusquedaObra) {
		this.tipoObraBusquedaObra = tipoObraBusquedaObra ? 4 : 0;
	}
	
	public List<SelectItem> getTiposBusquedaObra() {
		return tiposBusquedaObra;
	}
	
	public void setTiposBusquedaObra(List<SelectItem> tiposBusquedaObra) {
		this.tiposBusquedaObra = tiposBusquedaObra;
	}

	public int getPaginacionObras() {
		return paginacionObras;
	}

	public void setPaginacionObras(int paginacionObras) {
		this.paginacionObras = paginacionObras;
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

	public boolean isAvanzada() {
		return avanzada;
	}

	public void setAvanzada(boolean avanzada) {
		this.avanzada = avanzada;
	}

	public boolean isAvanzadaConceptos() {
		return avanzadaConceptos;
	}

	public void setAvanzadaConceptos(boolean avanzadaConceptos) {
		this.avanzadaConceptos = avanzadaConceptos;
	}

	public boolean getPermiteAsignarCuentaBancaria() {
		return PERFIL_CAJA_CHICA_ABONOS;
	}

	public void setPermiteAsignarCuentaBancaria(boolean pPermiteAsignarCuentaBancaria) {
		this.PERFIL_CAJA_CHICA_ABONOS = pPermiteAsignarCuentaBancaria;
	}
	
	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}
}

/* 
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ---------------------------------------------------------------------------------------------------------------- 
 *  VER |   FECHA    |   	AUTOR 		| DESCRIPCIÓN 
 * ---------------------------------------------------------------------------------------------------------------- 
 *  1.1 | 2016-09-30 | Javier Tirado 	| Se añade la carga de tipos de pago
 *  1.1 | 2016-09-30 | Javier Tirado 	| Quitamos la opcion de seleccionar un negocio como beneficiario
 *  1.1 | 2016-09-30 | Javier Tirado    | Cambiar las opciones de cheque y transferencia por un listado del catálogo de Formas de pago
 *  1.1 | 2016-10-03 | Javier Tirado    | Añadimos los perfiles para Vo.Bo. y Autorizacion.
 *  2.2 | 2017-04-21 | Javier Tirado 	| Añado opcion de actualizar. Valido sucursal, cuenta origen y beneficiario.
 *  2.2 | 2017-04-21 | Javier Tirado 	| Corrijo metodo cancelar CajaChica. Añado cuenta origen al guardado.
 */
