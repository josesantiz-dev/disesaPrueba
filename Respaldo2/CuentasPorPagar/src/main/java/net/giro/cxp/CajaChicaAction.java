package net.giro.cxp;

import java.math.BigDecimal;
import java.nio.file.Files;
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

import net.giro.adp.beans.Obra;
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

public class CajaChicaAction {
	private static Logger log = Logger.getLogger(CajaChicaAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
   	private HttpSession httpSession;
	private InitialContext ctx;
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
	// INTERFACES
	private PagosGastosRem ifzGtosComp;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private CuentasBancariasRem ifzCtas;
	private GastosImpuestoRem ifzGastoImpuesto;
	private PagosGastosDetImpuestosRem ifzDesgloImpto;
	private PersonaRem ifzPersonas;
	//private ClientesRem ifzClientes;
	private PagosGastosDetRem ifzComprobadoGto;
	private SucursalesRem ifzSucursal;	
	private ReportesRem	ifzReportes;
	private ObraRem ifzObras;
	private EmpleadoRem ifzEmpleado;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	// LISTAS
	private List<String> tipoBusqueda;
	private List<PagosGastos> listCajaChica;
	private List<PagosGastosDetImpuestosExt> lisImpuestosEliminados;
	private List<PagosGastosDetImpuestosExt> listDesgloseImpuestos;
	private List<PagosGastosDetImpuestosExt>	listDesgloseRet;
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
	private String [] listBusqRet;
	private FacturaImpuestos reg; 
	private Double totalFacturasReportadas;
	private Double importeImpuesto;
	private Double importeRetencion;
	private Double sumRetenciones;	
	private Double sumImpuestos;
	private Double montoTotalFactura; // TOTAL FACTURA
	private Double subtotal; // SUBTOTAL (COMPROBACION)
	private Double totalImpuestos; // TOTAL IMPUESTOS (COMPROBACION)
	private Double totalRetenciones; // TOTAL RETENCIONES (COMPROBACION)
	private Double totalPago; // TOTAL PAGO (COMPROBACION)
	private String conceptoGasto2;
	private String descripcionFactura;
	private String conceptoGasto;	
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
	private String valTipoBusqueda;
	private String campoBusqueda;
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
   	private int 	numPagina;
   	private int 	numPaginaFact;
   	private int 	tipoMensaje;
   	private int 	tipoReporte;
	private boolean comprobacionGenerada;
	private boolean existeRetencion;
	// Carga XML
	private FtpRem ifzFtp;
	private byte[] fileSrc;
	private Long facturaId;
	private String facturaRfc;
	private String facturaEmisor;
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
	// control de FOCUS
	//private String inputNameFocused;
	// SUGERENCIAS
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalSugerida;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	
	public CajaChicaAction() throws NamingException, Exception {
		FacesContext facesContext = null;
		Application app = null;
		ValueExpression ve = null;
		PropertyResourceBundle tyg = null;
		Map<String, String> params = null;
		
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
			
			params = facesContext.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// Variables para la subida de archivos
			this.ftpDigitalizacionHost = entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = entornoProperties.getString("ftp.digitalizacion.ruta");
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
			this.listDesgloseRet= new ArrayList<PagosGastosDetImpuestosExt>();
			this.listBeneficiarios = new ArrayList<Persona>();
			this.lisImpuestosEliminados = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listImpuestosDelGasto = new ArrayList<GastosImpuestoExt>();		
			this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>();
			this.listFacturaConImpuestosEliminadas = new ArrayList<FacturaImpuestos>();
			this.listFacturas = new ArrayList<PagosGastosDetExt>();
			this.listSucursales = new ArrayList<Sucursal>();
			
			this.encontroMismoGrupo=false;
			this.tipoCajaChica = 0;
			this.tipoMensaje =0;
			this.mensaje = "";
			this.numPagina = 1;
			this.numPaginaFact = 1;
			this.tipoReporte = 1;
			this.totalFacturasReportadas = 0D;
			this.totalImpuestos=0D;
			this.subtotal =0D;
			this.totalRetenciones=0D;
			this.totalPago=0D;
			this.montoTotalFactura=0D;
			this.tipoPersona="P";
			this.cuenta="";
			this.beneficiario="";
			this.facturaConLlave = false;
			this.existeRetencion=false;
			
			this.tipoBusqueda = new ArrayList<String>();
			this.tipoBusqueda.add("Fecha del Movto. a Caja Chica");
			this.tipoBusqueda.add("Beneficiario");
			this.tipoBusqueda.add("Cuenta Bancaria");
			this.tipoBusqueda.add("ID");
			this.valTipoBusqueda = tipoBusqueda.get(0);
			this.campoBusqueda = "";
			
			// Busqueda OBRAS
			this.tipoObra = "O";
			this.tiposBusquedaObra = new ArrayList<SelectItem>();
			this.tiposBusquedaObra.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaObra.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObra.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObra.add(new SelectItem("id", "ID"));
			this.campoBusquedaObra = this.tiposBusquedaObra.get(0).getValue().toString();
			this.valorBusquedaObra = "";
			this.paginacionObras = 1;
			this.comprobacionGenerada=false;
			
			this.listBusqRet = new String[3];
			this.listBusqRet[0] = "Clave";
			this.listBusqRet[1] = "Descripcion";
			this.listBusqRet[2] = "Cuenta Contable";
	   		
	   		// BUSQUEDA PERSONA/NEGOCIO
	   		this.tiposBusquedaPersonaNegocio = new ArrayList<SelectItem>();
	   		this.tiposBusquedaPersonaNegocio.add(new SelectItem("nombre", "Nombre"));
	   		this.tiposBusquedaPersonaNegocio.add(new SelectItem("rfc", "RFC"));
	   		this.tiposBusquedaPersonaNegocio.add(new SelectItem("id", "ID"));
	   		this.campoBusquedaPersonaNegocio = this.tiposBusquedaPersonaNegocio.get(0).getDescription();
	   		this.valorBusquedaPersonaNegocio = "";
	   		this.paginacionBusquedaPersonaNegocio = 1;
	   		
	   		// BUSQUEDA CONCEPTOS
	   		this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
	   		this.tiposBusquedaConceptos.add(new SelectItem("descripcion", "Descripcion"));
	   		this.tiposBusquedaConceptos.add(new SelectItem("id", "ID"));
	   		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
	   		this.valorBusquedaConceptos = "";
	   		this.pagBusquedaConceptos = 1;
	   		
	   		this.valBusqRet = "";
	   		this.campoBusqRet = this.listBusqRet[0];
	   		
	   		this.ctx = new InitialContext();
			this.ifzGpoVal 			= (ConGrupoValoresRem) 			this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzGtosComp 		= (PagosGastosRem) 				this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
			this.ifzCtas 			= (CuentasBancariasRem)			this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
			this.ifzPersonas 		= (PersonaRem) 					this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzConValores 		= (ConValoresRem) 				this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGastoImpuesto 	= (GastosImpuestoRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
			this.ifzDesgloImpto 	= (PagosGastosDetImpuestosRem) 	this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
			this.ifzComprobadoGto 	= (PagosGastosDetRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzObras 			= (ObraRem) 					this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzSucursal 		= (SucursalesRem) 				this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzEmpleado 		= (EmpleadoRem) 				this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzReportes		= (ReportesRem)					this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzFtp 			= (FtpRem) 						this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzPerNeg 			= (PersonaNegocioVistaRem) 		this.ctx.lookup("ejb:/Logica_Clientes//PersonaNegocioVistaFac!net.giro.clientes.logica.PersonaNegocioVistaRem");
			//this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			this.ifzLocalidad = (LocalidadDAO) this.ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			this.ifzColonia = (ColoniaDAO) this.ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");		

			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGtosComp.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCtas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGastoImpuesto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzComprobadoGto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPerNeg.setInfoSesion(this.loginManager.getInfoSesion());
			//this.ifzClientes.setInfoSesion(this.loginManager.getInfoSesion());

			// VALORES SUGERIDOS por Usuario
			comprobarUsuario();
			
			// FORMAS DE PAGO
			this.pojoGpoValFormasPago = this.ifzGpoVal.findByName("SYS_FORMAS_PAGO");
			if (this.pojoGpoValFormasPago == null || this.pojoGpoValFormasPago.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_FORMAS_PAGO en con_grupo_valores");
	
			// MOVIMIENTO GASTOS
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
			//this.inputNameFocused = "txtvalor";
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.RegistroGastosAction", e);
			this.ctx = null;
		}
  	}
	
	
	public void buscar() {
		String campoBusquedaCC = "";
		
		try {
			control();
			switch (this.valTipoBusqueda) {
				case "Fecha del Movto. a Caja Chica" : campoBusquedaCC = "fecha"; break;
				case "Beneficiario" : campoBusquedaCC = "beneficiario"; break;
				case "Cuenta Bancaria" : campoBusquedaCC = "numeroCuentaOrigen"; break;
				case "ID" : campoBusquedaCC = "id"; break;
				default: campoBusquedaCC = "beneficiario"; break;
			}
			
			log.info("CajaChica... Buscando " + ("".equals(this.valTipoBusqueda) ? "sin filtro" : ("por " + campoBusquedaCC + ": " + this.campoBusqueda)));
			this.listCajaChica = this.ifzGtosComp.findLikeCajaChica(campoBusquedaCC, this.campoBusqueda, "C", "G", 150, this.sucursalesVisibles);
			if (this.listCajaChica == null || this.listCajaChica.isEmpty()) {
				log.info("ERROR 12: Busqueda sin resultados");
				control(12);
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.CajaChicaAction.buscar", e);
			control(true);
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
			log.error("Error en el metodo nuevo.", e);
			control(true);
		} finally {
			//this.inputNameFocused = "pnlTipoCajaChica";
		}
	}

   	public void editar() {
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
			this.pojoCajaChica = this.ifzGtosComp.convertir(this.pojoCaja);
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
				
				this.listFacturas = this.ifzComprobadoGto.findLikePojoCompletoExt(this.pojoCajaChica.getId(), 100);
				for (PagosGastosDetExt var : this.listFacturas) {
					this.reg = new FacturaImpuestos();
					this.reg.setPojoFactura(var);
					
					try {
						this.reg.setListImpuestos(this.ifzDesgloImpto.findLikePojoCompletoExt(var, 100));
					} catch (Exception e) {
						log.error("Error al obtener la lista de impuestos de la factura " + var.getReferencia() + " (" + var.getId() + ")", e);
					}
					
					this.listFacturaConImpuestos.add(this.reg);
				}
				
				log.info("CajaChica... " + listFacturaConImpuestos.size() + " Facturas recuperadas");
				log.info("CajaChica... Totalizando");
				actualizaTotalFaturas();
			} catch (Exception e) {
				log.error("Error en CuentasPorPagar.CajaChicaAction.editar --> Generando lista de facturas con impuestos", e);
	   			control(true);
			}
			
			log.info("CajaChica... Lista para editar");
   		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.CajaChicaAction.editar.", e);
   			control(true);
		} finally {
			//this.inputNameFocused = "pnlTipoCajaChica";
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
			if ("S".equals(this.perfilRequiereObra) && this.tipoCajaChica == 0 && this.pojoObra == null) {
				log.info("ERROR 19: Debe selecccionar una Obra");
				control(19);
				return;
			}
			
			if (this.listFacturaConImpuestos.isEmpty()) {
				log.info("ERROR 15: No añadio ninguna factura");
				control(15);
				return;
			}

			if (this.pojoCajaChica.getId() == null || this.pojoCajaChica.getId() <= 0L)
				registroNuevo = true;

			if (this.pojoObra != null) {
				if (this.pojoCajaChica.getIdObra() == null) {
					this.pojoCajaChica.setIdObra(this.pojoObra);
				} else {
					if (this.pojoCajaChica.getIdObra().getId() != null && this.pojoCajaChica.getIdObra().getId() != this.pojoObra.getId())
						this.pojoCajaChica.setIdObra(this.pojoObra);
				}
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
					this.pojoCtas = this.ifzGtosComp.findCuentaBancariaById(Long.valueOf(match.group(1))); // this.ifzCtas.findAllById(Short.valueOf(match.group(1)));
				} else {
					if (this.pojoCtas.getId() != Long.valueOf(match.group(1))) {
						log.info("Cambiamos cuenta (CtasBanco) por: " + match.group(1));
						this.pojoCtas = this.ifzGtosComp.findCuentaBancariaById(Long.valueOf(match.group(1))); 
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
					this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				} else {
					if (this.pojoBeneficiarios.getId() != Long.valueOf(match.group(1))) {
						log.info("Cambiamos beneficiario (Persona) por: " + match.group(1));
						this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
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
			this.pojoCajaChica.setModificadoPor(this.usuarioId);
			this.pojoCajaChica.setFechaModificacion(Calendar.getInstance().getTime());
						
			if (registroNuevo) {
				this.pojoCajaChica.setCreadoPor(this.usuarioId);//(Short.valueOf(String.valueOf(this.usuarioId)));
				this.pojoCajaChica.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoCajaChica.setTipo("C");//comprobacion de caja chica
				this.pojoCajaChica.setEstatus("G");//generado
				this.pojoCajaChica.setConcepto("Reposición de Caja chica"); // (this.pojoGtosComp.getOperacion() == "C" ? "Reposición de Caja chica": (this.pojoGtosComp.getOperacion()== "G" ? "Comprobación de Caja Chica": "Reposición y Comprobación de Caja Chica"));
				
				// Asignamos consecutivo
				log.info("Asignamos consecutivo");
				int consecutivo = this.ifzGtosComp.findConsecutivoByBeneficiario(this.pojoCajaChica.getIdBeneficiario().getId(), "C", "G");
				this.pojoCajaChica.setConsecutivo(consecutivo);
				
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
				resp = this.ifzGtosComp.salvar(this.pojoCajaChica, true);
				
				//validando folio cheque
				if (resp.getResultado() != -1) {
					// Asignamos el ID correspondiente
					this.pojoCajaChica.setId(Long.valueOf(resp.getReferencia()));
					this.pojoCaja = this.ifzGtosComp.convertir(this.pojoCajaChica);
					
					//impresion del cheque
					if (! "G".equals(this.pojoCajaChica.getOperacion())) {
						mensaje="BIEN";
					}
					
					// Lo agregamos a la lista
					this.listCajaChica.add(this.pojoCaja);
				} else {
					if (! "BIEN".equals(resp.getRespuesta())) {
						this.mensaje = resp.getRespuesta();
						
						if ((resp.getRespuesta().indexOf("SE CANCELA EL FOLIO ") > -1) || (resp.getRespuesta().indexOf("SE CANCELAN LOS FOLIOS ") > -1)) {
							log.info("ERROR 7: " + this.mensaje);
							control(true, 7, this.mensaje);
						} else {
							log.info("ERROR 6: " + this.mensaje);
							control(true, 6, this.mensaje);
						}
						
						return;   							
					}
				}
			} else {
				// Actualizamos
				log.info("Actualizamos CajaChica (PagosGastos)");
				this.ifzGtosComp.update(this.pojoCajaChica);
				log.info("CajaChica (PagosGastos) actualizada");
				this.pojoCaja = this.ifzGtosComp.convertir(this.pojoCajaChica);
			}
			
			//grabando las facturas en caso de que hayan agregado algunas
			if (! this.listFacturaConImpuestos.isEmpty()) {
				log.info("Guardando facturas (PagosGastosDet)");
				for (FacturaImpuestos var : this.listFacturaConImpuestos) {
					if (var.getPojoFactura().getId() != null)
						continue;
					
					var.getPojoFactura().setIdPagosGastos(this.pojoCajaChica);					
					var.getPojoFactura().setId(this.ifzComprobadoGto.save(var.getPojoFactura()));
					
					if (! var.getListImpuestos().isEmpty()) {
						for (PagosGastosDetImpuestosExt var2 : var.getListImpuestos()) {
							var2.setIdPagosGastosDet(var.getPojoFactura());
							if (var2.getId() == null)
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
			this.pojoCaja = this.ifzGtosComp.convertir(this.pojoCajaChica);
			mensajeTransaccion();
		} catch (Exception e) { 
			log.error("Error en CuentasPorPagar.CajaChicaAction.guardar", e);
			control(true);
		}
	}

	public void cancelar() {
		int index = -1;
		
		try {
			control();
			index = this.listCajaChica.indexOf(this.pojoCaja);
			this.ifzGtosComp.setInfoSesion(this.loginManager.getInfoSesion());
			this.pojoCaja = this.ifzGtosComp.cancelar(this.pojoCaja);
			
			// Actualizo el pojo en la lista si corresponde
			if (index >= 0)
				this.listCajaChica.set(index, this.pojoCaja);
			//this.pojoCaja.setEstatus("X");
			log.info("Caja Chica " + this.pojoCaja.getId() + " (PagosGastos) cancelada");
		} catch (Exception e) {
			this.msgCancelar = "Ocurrio un problema inesperado";
			log.error("Error en CuentasPorPagar.CajaChica.cancelar", e);
			control(true);
		}
	}

	public void evaluaCancelar() {
   		try {
   			control();
   			this.msgCancelar = "";
   			if (this.pojoCaja != null)
   				this.msgCancelar = "X".equals(this.pojoCaja.getEstatus()) ? "" : this.deseaCancelar;
		} catch (Exception e) {
			log.error("Error CuentasPorPagar.CajaChicaAction.evaluaCancelar.", e);
   			control(true);
		}
   	}

	public void reporte() {
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		String nombreDocumento = "";
		
		try{
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
			log.error("ERROR INTERNO al ejecutar el Reporte de Caja Chica", e);
			control("Ocurrio un problema al ejecutar el reporte");
		}
	}

	public void mensajeTransaccion() {
		try {
			control();
			// Enviamos mensaje a cola de transacciones
			log.info("Enviando Transaccion ... ");
			if (this.pojoCaja == null || this.pojoCaja.getId() == null || this.pojoCaja.getId() <= 0L) {
				log.info("NO se pudo enviar la Transaccion. No selecciono registro de Caja Chica");
				return;
			}
			
			Respuesta respuesta = this.ifzGtosComp.enviarTransaccion(this.pojoCaja);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("No se pudo enviar mensaje a la cola de transacciones." + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError(), (Throwable) respuesta.getBody().getValor("exception"));
				control(respuesta.getErrores().getDescError());
				return;
			}
			log.info("Transaccion envianda");
		} catch (Exception e) {
			log.error("Error en mensajeTransaccion", e);
			control(true);
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
		this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta");
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
			if (this.pojoCajaChica.getId() != null) {
				if (! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					this.comprobacionGenerada = true;
					log.info("ERROR 10: No se permite añadir facturas.");
					control(10);
					return;
				}
			}

			this.pojoComprobadoGto = new PagosGastosDetExt();
			this.sinFactura = false;
			this.encontroMismoGrupo = false;
			this.totalImpuestos = 0D;
			this.subtotal = 0D;
			this.montoTotalFactura = 0D;
			this.totalRetenciones = 0D;
			this.totalPago = 0D;
			this.conceptoGasto = "";
			this.conceptoGasto2 = "";
			this.nombreProveedor = "";
			this.descripcionFactura = "";
			this.observaciones = "";
			this.listDesgloseImpuestos.clear();
			this.listDesgloseRet.clear();
			this.listRetEncontradas.clear();
			reiniciaFactura();
			
			proveedor = new PersonaExt();
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.beneficiario);
			if (match.find()) {
				proveedor = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona);
				this.pojoComprobadoGto.setIdProveedor(proveedor);
				this.pojoComprobadoGto.setTipoPersonaProveedor(this.tipoPersona);
			}
			
			this.reg = new FacturaImpuestos();
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.CajaChicaAction.agregar", e);
			control(true);
		}
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
		
		this.pojoComprobadoGto = this.reg.getPojoFactura();
		this.conceptoGasto = this.reg.getPojoFactura().getIdConcepto().getId() +" - "+ this.reg.getPojoFactura().getIdConcepto().getDescripcion();
		this.conceptoGasto2 = this.conceptoGasto;
		
		if (! "".equals(this.facturaEmisor.trim())) 
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
		desglosaImpuestos();
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
			log.error("Error en CuentasPorPagar.CajaChicaAction.eliminar", e);
			control(true);
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
			if (! "OK".equals(validaMontoFactura()))
				return;
			
			if (! "OK".equals(validaMontoImpuestos()))
				return;
			
			if (! "OK".equals(validaGrupoImpuestos()))
				return;
		
			if (! "OK".equals(validaMontoFacturaContraTotalImpuestos()))
				return;
			
			if (this.facturaId == null)
				analizarArchivo();
	
			if (this.pojoComprobadoGto.getId() == null || this.pojoComprobadoGto.getId() <= 0L)
				registroNuevo = true;
			
			this.pojoComprobadoGto.setModificadoPor(this.usuarioId);
			this.pojoComprobadoGto.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoComprobadoGto.setFacturado((short) (this.sinFactura ? 0 : 1));
			this.pojoComprobadoGto.setIdXml(this.facturaId);
			this.pojoComprobadoGto.setFacturaRazonSocial(this.facturaEmisor);
			this.pojoComprobadoGto.setFacturaRfc(this.facturaRfc);
			
			if (this.pojoComprobadoGto.getTipoPersonaProveedor() == null || "".equals(this.pojoComprobadoGto.getTipoPersonaProveedor().trim()))
				this.pojoComprobadoGto.setTipoPersonaProveedor(this.pojoComprobadoGto.getIdProveedor().getTipoPersona() == 2L ? "N" : "P");
			
			if (this.facturaActualizar) {
				this.facturaActualizar = false;
				this.ifzComprobadoGto.setFacturaProperty(this.facturaId, "nombre", this.facturaEmisor);
				
				if (this.pojoComprobadoGto.getReferencia().contains("-")) {
					splitted = this.pojoComprobadoGto.getReferencia().split("-");

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
					this.ifzComprobadoGto.setFacturaProperty(this.facturaId, "serie", this.pojoComprobadoGto.getReferencia());
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
			this.pojoComprobadoGto.setTotalImpuestos(totalImpuestos);  					
			this.pojoComprobadoGto.setSubtotal(subtotal);   	
			this.pojoComprobadoGto.setTotalRetenciones(totalRetenciones);
			this.pojoComprobadoGto.setIdPagosGastos(this.pojoCajaChica);
			this.pojoComprobadoGto.setObservaciones(this.observaciones);
						
			if (this.pojoCajaChica.getId() == null) {
				//si el gasto no existe guardo las facturas en una lista
				// y las guardare hasta que creen un gasto. sí el gasto no existe no puedo grabar 
				//facturas hasta que me serciore que creen un gasto. Siempre que el gasto no tenga llave 
				//la factura tampoko tendra llave, por eso no pregunto si es nueva 	
					
				this.pojoComprobadoGto.setCreadoPor(this.usuarioId);
				this.pojoComprobadoGto.setFechaCreacion(Calendar.getInstance().getTime());
				
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
					
					// GUARDAMOS
					this.pojoComprobadoGto.setId(this.ifzComprobadoGto.save(this.pojoComprobadoGto));
					
					if (! this.listDesgloseImpuestos.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
							var.setIdPagosGastosDet(this.pojoComprobadoGto);
							// GUARDAMOS
							var.setId(this.ifzDesgloImpto.save(var));
						} 
					}
				} else {
					this.ifzComprobadoGto.update(this.pojoComprobadoGto);

					if (! this.listDesgloseImpuestos.isEmpty()) {
						for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
							var.setIdPagosGastosDet(this.pojoComprobadoGto);
							
							if (var.getId() == null)
								var.setId(this.ifzDesgloImpto.save(var));
							else
								this.ifzDesgloImpto.update(var);
						}
					}
				}
				
				//eliminando los pojos de impuestos de la base de datos,
				// ya que anteriormente solo los elimine de la memoria
				if (! this.lisImpuestosEliminados.isEmpty()) {
					for (PagosGastosDetImpuestosExt var : this.lisImpuestosEliminados) {
						if (var.getId() != null)
							this.ifzDesgloImpto.delete(var);
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
			control(false, -1, "Comprobacion agregada");
   		} catch (Exception e) {
			log.error("error en guardarFactura", e);
			control(9);
		}
   	}

	public void evaluaEditar() {
   		try {
   			control();
   			this.facturaConLlave = false;
			if (this.reg.getPojoFactura().getId() != null) {
				if (! validaRequest("DETEDIT", "1")) { //! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					this.facturaConLlave = true;
		   			log.info("ERROR 11: No se permite editar las facturas");
		   			control(11);
		   			return;
		   		}
			}
			
			editarFactura();
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.CajaChicaAction.evaluaEditar.", e);
			control(true);
		}
   	}

   	public void evaluarFactura() {
   		try {
			control();
			this.facturaConLlave = false;
			if (this.reg.getPojoFactura().getId() != null) {
				if (! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					this.facturaConLlave = true;
		   			log.info("ERROR 2: No se permite eliminar facturas");
		   			control(2);
		   			return;
				} 
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.CajaChicaAction.evaluarFactura.", e);
			control(true);
		}
   	}

   	public String eliminarImpuestoDesglosado() {
   		try {
   			control();
			this.sumImpuestos = 0D;
			this.sumRetenciones = 0D;
			this.importeImpuesto = 0D;
		
			this.lisImpuestosEliminados.add(this.pojoDesgloImpto);
			this.listDesgloseImpuestos.remove(this.pojoDesgloImpto);
			
			if (! this.listDesgloseImpuestos.isEmpty()) {			
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						this.sumRetenciones = Utilerias.redondear(this.sumRetenciones + var.getValor().doubleValue(),2);	
					else	
						this.sumImpuestos = Utilerias.redondear(this.sumImpuestos + var.getValor().doubleValue(),2);	
				}
				
				this.totalImpuestos = this.sumImpuestos;
				this.totalRetenciones = this.sumRetenciones;
				this.subtotal = Utilerias.redondear(this.montoTotalFactura - this.sumImpuestos, 2);
				this.totalPago = Utilerias.redondear(this.montoTotalFactura - this.sumRetenciones, 2);
			} else {
				//sabemos que la lista de imptos quedo vacia porque entro a esta opcion y ponemos en cero el total de impuestos
				this.totalImpuestos=0D;	
				this.totalRetenciones = 0D;
				this.subtotal = this.montoTotalFactura;
				this.totalPago = this.montoTotalFactura;
			}
		} catch (Exception e) {
			log.error("error en eliminarImpuestoDesglosado", e);
			control(5);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String cambioMontoImpuesto() {
   		try {
   			control();
			if (this.montoTotalFactura != 0) {
				this.sumImpuestos = 0D;
				this.sumRetenciones = 0D;
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getValor() != null) {
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
							this.sumRetenciones = Utilerias.redondear(this.sumRetenciones + var.getValor().doubleValue(),2);
						else
							this.sumImpuestos = Utilerias.redondear(this.sumImpuestos + var.getValor().doubleValue(),2);
					}
				}
				
				this.totalImpuestos = this.sumImpuestos;
				this.totalRetenciones = this.sumRetenciones;
				this.totalPago = Utilerias.redondear(this.montoTotalFactura - this.totalRetenciones, 2);	
				this.subtotal = Utilerias.redondear(this.montoTotalFactura - this.sumImpuestos, 2);	
			}
		} catch (Exception e) {
			log.error("ERROR 9 en cambioMontoImpuesto", e);
			control(9);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String actualizaTotalFaturas() {
		try{
			control();
			this.totalFacturasReportadas = 0D;
			if (! this.listFacturaConImpuestos.isEmpty()) {
				for (FacturaImpuestos var : listFacturaConImpuestos) {
					this.totalFacturasReportadas = Utilerias.redondear(this.totalFacturasReportadas + ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones()),2);
				}
			}
			
			// Actualizamos el monto
			this.pojoCajaChica.setMonto(this.totalFacturasReportadas);
		} catch (Exception e) {
			log.error("error en actualizaTotalFaturas", e);
			control(true);
			return "ERROR";
		}
		
		return "OK";
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
							log.info("ERROR 3 - Monto de Impuestos debe ser mayor a cero");
							control(3);
							return "ERROR";
						}
					}  
				}
			}
		} catch (Exception e) {
			log.error("error en validaMontoImpuestos", e);
			control(true);
			return "ERROR";
		}
	
		return "OK";
	}
   	
   	public String validaMontoFactura() {
		try {
			control();
			if (this.montoTotalFactura <= 0) {
				log.info("ERROR 4 - Monto de Factura debe ser mayor a cero");
				control(4);
				return "ERROR";
			}	
		} catch (Exception e) {			
			log.error("error en validaMontoFactura", e);
			control(true);
			return "ERROR";
		}
	
		return "OK";
	}
   	
	public String validaMontoFacturaContraTotalImpuestos() {
		try{
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
   		double importe = 0;
   		Matcher match = null;
		Pattern pat = null;
   			
   		try {
			control();
			this.importeImpuesto = 0D;
			this.importeRetencion = 0D;
			this.sumImpuestos = 0D;
			this.sumRetenciones = 0D;
			this.subtotal = Utilerias.redondear(this.montoTotalFactura - this.sumImpuestos, 2);
			this.totalPago = Utilerias.redondear(this.montoTotalFactura - this.totalRetenciones, 2);
   			
   			if (this.sinFactura) {
				this.listDesgloseImpuestos.clear();
   				this.totalImpuestos = 0D;
   				this.totalRetenciones = 0D;
   				this.subtotal = Utilerias.redondear(this.montoTotalFactura - this.sumImpuestos, 2);
   				this.totalPago = Utilerias.redondear(this.montoTotalFactura - this.totalRetenciones, 2);
   			}
				
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
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
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
								this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
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
						this.totalPago		= Utilerias.redondear(this.montoTotalFactura - totalRetenciones,2);
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
			control(9);
		} finally {
			log.info("SUBTOTAL    : " + this.subtotal);
			log.info("IMPUESTOS   : " + this.totalImpuestos);
			log.info("RETENCIONES : " + this.totalRetenciones);
			log.info("TOTAL       : " + this.totalPago);
		}
	}

	public String buscarRet() {
   		try{
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
		try{
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
			if (! "".equals(this.conceptoGasto2)) {
				if (! this.conceptoGasto2.equals(this.conceptoGasto)) {
					this.nombreProveedor = "";
					this.listDesgloseImpuestos.clear();
					this.listDesgloseRet.clear();
					this.listRetEncontradas.clear();	
					desglosaImpuestos();
					this.conceptoGasto2 = conceptoGasto;
				}
			} else {
				conceptoGasto2 = conceptoGasto;
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
   			resp = ifzGtosComp.salvar(this.pojoCajaChica, false);
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
									control(1);
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
		try{
			control();
			if (! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getIdImpuesto().getId() == this.pojoNvaRet.getId()) {
						log.error("ERROR 14 - ? :(");
						control(14);
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
			log.error("Error en metodo agregaRet", e);
			control(true);
		}
	}
   	
	public List<Persona> autoacompletaBeneficiario(Object obj) {
		try {
			this.listBeneficiarios = this.ifzPersonas.findLikeClaveNombre("nombre", obj.toString(), this.pojoGpoValPersonas, this.tipoPersona, 20, false);	
			return this.listBeneficiarios;
		} catch (Exception e) {
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

   	// ---------------------------------------------------------------------------
   	// OBRAS
   	// ---------------------------------------------------------------------------
	
	public void nuevaBusquedaObras() {
		control();
		this.campoBusquedaObra = tiposBusquedaObra.get(0).getValue().toString();
		this.valorBusquedaObra = "";
		this.paginacionObras = 1;
		this.tipoObraBusquedaObra = 0;
		this.avanzada = false;
		
		if (this.listObras == null)
			this.listObras = new ArrayList<Obra>();
		this.listObras.clear();
		//this.inputNameFocused = "pnlTipoCajaChica";
	}
	
	public void buscarObras() {
		try {
			control();
			this.paginacionObras = 1;
			if (this.listObras != null)
				this.listObras.clear();

			log.info("Buscando obras tipo " + this.tipoObraBusquedaObra + " - " + this.campoBusquedaObra + ": " + this.valorBusquedaObra);
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valorBusquedaObra.trim().replace(" ", "%"), this.tipoObraBusquedaObra);
			if (this.listObras == null || this.listObras.isEmpty()) {
				log.info("ERROR 12 - Busqueda sin resultados");
				control(12);
			}
		} catch (Exception e) {
			log.error("Error en buscar obras", e);
			control(true);
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
			
			nuevaBusquedaObras();
		} catch (Exception e) {
			log.error("error en seleccionarObra", e);
			control(true);
		}
	}
		
	public String cambioObra() {
		try {
			this.tipoObra = "";
		} catch (Exception e) {
			log.error("error en cambioObra", e);
			return "ERROR";
		}
		
		return "OK";
	}

   	// ---------------------------------------------------------------------------
   	// PERSONA/NEGOCIO
   	// ---------------------------------------------------------------------------
		
	public void nuevaBusquedaPersonaNegocio() {
		control();
		log.info("Busqueda Persona/Negocio. Nueva busqueda");
		this.campoBusquedaPersonaNegocio = this.tiposBusquedaPersonaNegocio.get(0).getValue().toString();
   		this.valorBusquedaPersonaNegocio = "";
   		this.paginacionBusquedaPersonaNegocio = 1;
   		
   		if (this.listPersonasNegocios == null)
   			this.listPersonasNegocios = new ArrayList<PersonaNegocioVista>();
   		this.listPersonasNegocios.clear();
   		this.pojoPersonaNegocio = new PersonaNegocioVista();
		//this.inputNameFocused = "pnlTipoCajaChica";
	}
	
	public void buscarPersonaNegocio() {
		try {
			control();
			if (this.campoBusquedaPersonaNegocio == null || "".equals(this.campoBusquedaPersonaNegocio))
				this.campoBusquedaPersonaNegocio = this.tiposBusquedaPersonaNegocio.get(0).getValue().toString();

			log.info("Busqueda Persona/Negocio. Buscar por " + this.campoBusquedaPersonaNegocio + ": " + this.valorBusquedaPersonaNegocio);
			this.listPersonasNegocios = this.ifzPerNeg.findLikeProperty(this.campoBusquedaPersonaNegocio, this.valorBusquedaPersonaNegocio, null, 0);
			if (this.listPersonasNegocios.isEmpty()) {
				control(12);
				log.info("ERROR 12: Busqueda sin resultados");
			}
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorPagar.CajaChicaAction.buscarPersonaNegocio", e);
		}
	}
	
	public void seleccionarPersonaNegocio() {
		try {
			control();
			if (this.pojoPersonaNegocio == null)
				return;
			
			log.info("Busqueda Persona/Negocio. Selecciono: " + this.pojoPersonaNegocio.getId() + " - " + this.pojoPersonaNegocio.getNombre() + " - " + this.pojoPersonaNegocio.getTipo());
			this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(this.pojoPersonaNegocio.getId(), this.pojoPersonaNegocio.getTipo());
			
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
			control(true);
			log.error("Error en CuentasPorPagar.CajaChicaAction.buscarPersonaNegocio", e);
		}
	}

   	// ---------------------------------------------------------------------------
	// CONCEPTOS
   	// ---------------------------------------------------------------------------
	
	public void nuevaBusquedaConceptos() {
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getValue().toString();
   		this.valorBusquedaConceptos = "";
   		this.pagBusquedaConceptos = 1;
   		
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
				log.info("ERROR 12: Busqueda sin resultados");
				control(12);
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
	
   	// ---------------------------------------------------------------------------
	// CARGA Y DESCARGA DE FACTURA XML
   	// ---------------------------------------------------------------------------

	private void reiniciaFactura() {
		this.facturaId = 0L;
		this.facturaRfc = "";
		this.facturaEmisor = "";
	}
	
	public void nuevaCarga() {
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
			respuesta = this.ifzComprobadoGto.cargaFacturaXML(this.fileSrc, this.pojoCajaChica.getId());
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
				this.pojoComprobadoGto.setIdProveedor(pojoProveedor);
				this.facturaEmisor = pojoProveedor.getNombre();
			}

			this.facturaActualizar = false;
			if ((this.facturaEmisor == null || "".equals(this.facturaEmisor.trim())) || (facturaFolio == null || "".equals(facturaFolio.trim())))
				this.facturaActualizar = true;

			this.pojoComprobadoGto.setTipoPersonaProveedor(tipoPersona);
			this.pojoComprobadoGto.setFacturaRfc(this.facturaRfc);
			this.pojoComprobadoGto.setFacturaRazonSocial(this.facturaEmisor);
			this.pojoComprobadoGto.setFacturaSerie(respuesta.getBody().getValor("comprobanteSerie").toString());
			this.pojoComprobadoGto.setFacturaFolio(respuesta.getBody().getValor("comprobanteFolio").toString());
			this.pojoComprobadoGto.setReferencia(facturaFolio);
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
				control(20);
				return;
			}

			if (this.reg.getPojoFactura().getIdXml() == null || this.reg.getPojoFactura().getIdXml() <= 0L)
				this.reg.getPojoFactura().setIdXml(0L);

			this.facturaId = this.reg.getPojoFactura().getIdXml();
			if (this.facturaId == null || this.facturaId <= 0L) {
				log.error("ERROR 20 - No tiene asignada ninguna factura");
				control(20);
				return;
			}

			this.facturaRfc = this.ifzComprobadoGto.getFacturaProperty(this.facturaId, "rfcEmisor");
			this.facturaEmisor = this.ifzComprobadoGto.getFacturaProperty(this.facturaId, "nombre");
			if (this.facturaEmisor == null || "".equals(this.facturaEmisor.trim()))
				this.facturaEmisor = this.facturaRfc;

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
				control(21);
				return;
			}

			// Ponemos los datos en session
			this.httpSession.setAttribute("nombreDocumento", fileName);
			this.httpSession.setAttribute("formato", "xml");
			this.httpSession.setAttribute("contenido", this.fileSrc);
		} catch (Exception e) {
    		log.error("Error en CuentasPorPagar.CajaChicaAction.descargarArchivo :: ERROR 9 ? :(", e);
    		control(9);
		}
	}
	
   	// ---------------------------------------------------------------------------
   	// METODOS DE CONTROL
   	// ---------------------------------------------------------------------------

	private void control() {
		this.encontroMismoGrupo = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(boolean value) {
		if (! value) {
			control(false, 0, "");
			return;
		} 

		control(true, 9, "ERROR");
	}
		
	private void control(int tipoMensaje) {
		if (tipoMensaje == 0) {
			control(false);
			return;
		}
		
		control(true, tipoMensaje, "ERROR");
	}
	
	private void control(String mensaje) {
		if (mensaje == null || "".equals(mensaje)) {
			control();
			return;
		}
		
		control(true, -1, mensaje);
	}
	
	private void control(boolean val1, int val2, String val3) {
		this.encontroMismoGrupo = val1;
		this.tipoMensaje = val2;
		this.mensaje = (val3.contains("\n") ? val3.replace("\n", "<br/>") : val3);
	}

	private void getPerfiles() {
		String valPerfil = "";
		
		if (this.loginManager == null) 
			return;
		
		try {
			// EGRESOS_OPERACION ---> ADMINISTRATIVO
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);

			// CAJA_CHICA_ABONOS
			valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_ABONOS");
			this.PERFIL_CAJA_CHICA_ABONOS = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			// REQUIERE OBRA
			valPerfil = this.loginManager.getAutentificacion().getPerfil("REQUIERE_OBRA").trim();
			this.perfilRequiereObra = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			// LIMITE CAJA CHICA
			valPerfil = this.loginManager.getAutentificacion().getPerfil("LIMITE_CAJA_CHICA").trim();
			this.perfilLimiteCajaChica = (valPerfil != null && ! "".equals(valPerfil)) ? Double.valueOf(valPerfil.trim()) : 0;
			
			// VISTO BUENO
			valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_VOBO").trim();
			this.perfilCajaChicaVoBo = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			// AUTORIZACION
			valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_AUTORIZACION").trim();
			this.perfilCajaChicaAutorizacion = (valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			// DIVICION DE CAJA: VoBo y Autorizacion
			valPerfil = this.loginManager.getAutentificacion().getPerfil("DIVIDIR_CAJA").trim();
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

	private List<PagosGastosDetImpuestosExt> copiaListas(List<PagosGastosDetImpuestosExt> fuente) {
   		List<PagosGastosDetImpuestosExt> resultado = new ArrayList<PagosGastosDetImpuestosExt>();
   		
   		for (PagosGastosDetImpuestosExt pgi : fuente) {
   			resultado.add(((PagosGastosDetImpuestosExt) pgi.clone()) );
   		}
   		
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
	
   	// ---------------------------------------------------------------------------
   	// PROPIEDADES
   	// ---------------------------------------------------------------------------

   	public String getTituloComprobacion() {
   		if (this.pojoComprobadoGto.getId() != null && this.pojoComprobadoGto.getId() > 0L)
   			return "Comprobacion " + this.pojoComprobadoGto.getId();
   		return "Nueva Comprobacion";
   	}
   	
   	public void setTituloComprobacion(String value) { }
   	
   	public boolean getPermiteVoBo() {
   		return ("S".equals(this.perfilDividirCajaChica) && "S".equals(this.perfilCajaChicaVoBo));
   	}
   	
   	public void setPermiteVoBo(boolean value) {}
   	
   	public boolean getPermiteAutorizar() {
   		return ("S".equals(this.perfilDividirCajaChica) && "S".equals(this.perfilCajaChicaAutorizacion));
   	}

   	public void setPermiteAutorizar(boolean value) {}
   	
   	public String getObraNombre() {
   		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
   			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
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
		if (this.facturaId != null && this.facturaId > 0L) {
			if (this.facturaEmisor != null && ! "".equals(this.facturaEmisor.trim()) && ! "Emisor desconocido".equals(this.facturaEmisor.trim()))
				return this.facturaId + " - " + this.facturaRfc;
			return this.facturaId.toString();
		}
		
		return "Seleccione factura";
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

	public double getSubtotal() {
		return this.pojoComprobadoGto.getSubtotal() != null ? this.pojoComprobadoGto.getSubtotal() : 0;
	}

	public void setSubtotal(double subtotal) {
		this.pojoComprobadoGto.setSubtotal(subtotal);
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
	
	public Long getPagosGastosDetId() {
		return this.pojoComprobadoGto.getId() != null ? this.pojoComprobadoGto.getId() : 0L;
	}

	public void setPagosGastosDetId(Long pagosGastosDetId) {
		this.pojoComprobadoGto.setId(pagosGastosDetId);
	}
	
	public Double getTotalImpuestos() {
		return this.pojoComprobadoGto.getTotalImpuestos() != null ? this.pojoComprobadoGto.getTotalImpuestos() : 0;
	}

	public void setTotalImpuestos(Double totalImpuestos) {
		this.pojoComprobadoGto.setTotalImpuestos(totalImpuestos);
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
	
	public Double getSubtotalMasImpuestos() {
		return montoTotalFactura;
	}
	
	public void setSubtotalMasImpuestos(Double subtotalMasImpuestos) {
		this.montoTotalFactura = subtotalMasImpuestos;
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
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public List<String> getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(List<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
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

	public Double getTotImpuestos() {
		return totalImpuestos;
	}

	public void setTotImpuestos(Double totImpuestos) {
		this.totalImpuestos = totImpuestos;
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
		return listDesgloseRet;
	}

	public void setListDesgloseRet(List<PagosGastosDetImpuestosExt> listDesgloseRet) {
		this.listDesgloseRet = listDesgloseRet;
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
		return tipoObraBusquedaObra == 4;
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
