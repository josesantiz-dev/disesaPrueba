package net.giro.cxp.gastos;

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
//import net.giro.cxp.beans.ProgPagosDetalle;
import net.giro.cxp.beans.SucursalExt;
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
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.CtasBanco;
import net.giro.tyg.logica.CtasBancoRem;

import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class CajaChicaAction {
	private static Logger log = Logger.getLogger(CajaChicaAction.class);
	private InitialContext ctx;
	private LoginManager loginManager;
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
	private SucursalExt pojoSucursal;
	private Obra pojoObra;
	// INTERFACES
	private PagosGastosRem ifzGtosComp;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private CtasBancoRem ifzCtas;
	private GastosImpuestoRem ifzGastoImpuesto;
	private PagosGastosDetImpuestosRem ifzDesgloImpto;
	private PersonaRem ifzPersonas;
	private PagosGastosDetRem ifzComprobadoGto;
	private SucursalesRem ifzSucursal;	
	private ReportesRem	ifzReportes;
	private ObraRem ifzObras;
	private FtpRem ifzFtp;
	//private ProgPagosDetalleRem ifzProPagDet;
	// LISTAS
	private List<String> tipoBusqueda;
	private List<PagosGastos> listCajaChica;
	private List<PagosGastosDetImpuestosExt> lisImpuestosEliminados;
	private List<PagosGastosDetImpuestosExt> listDesgloseImpuestos;
	private List<PagosGastosDetImpuestosExt>	listDesgloseRet;
	private List<GastosImpuestoExt>	listImpuestosDelGasto;
	private List<GastosImpuestoExt>	listRetDelGasto;
	private List<Persona> listBeneficiarios;
	private List<CtasBanco> listCuentas;
	private List<SelectItem> listCuentasItems;
	private List<Persona> listProveedores;
	private List<ConValores> listConceptoGasto;
	private List<FacturaImpuestos> listFacturaConImpuestos;
	private List<ConValores> listRetEncontradas;
	//private List<Object> listProPagDet;
	private List<PagosGastosDetExt> listFacturas;
	private List<Sucursal> listSucursales;
	private List<SelectItem> listSucursalesItems;
	private List<Obra> listObras;
	//private Long valGpoPersonas;
	private Long valGpoGastos;
	private String [] listBusqRet;
	private FacturaImpuestos reg; 
	private Double totalMenosImpuestos;
	private Double totalFacturasReportadas;
	private Double subtotalMasImpuestos;
	private Double totImpuestos;
	private Double totRetenciones;
	private Double totPago;
	private Double impto;
	private Double imptoRet;
	private Double sum_imptoRet;	
	private Double sum_impto;	
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
	private String sucursal;
	private String observaciones;
	private int tipoCajaChica;
	private boolean facturado;
	private boolean vobo;
	private boolean autorizado;
	// BUSQUEDA PRINCIPAL
	private String valTipoBusqueda;
	private String campoBusqueda;
	// BUSQUEDA DE OBRAS
	private List<SelectItem> tiposBusquedaObra;
	private List<String> tipoBusquedaObra;
	private String campoBusquedaObra;
	private String valorBusquedaObra;
	private String tipoObra;
	private int tipoObraBusquedaObra;
	private int paginacionObras;
	// BUSQUEDA PERSONA/NEGOCIO
	private PersonaNegocioVistaRem ifzPerNeg;
	private List<PersonaNegocioVista> listPersonasNegocios;
	private PersonaNegocioVista pojoPersonaNegocio;
	private List<SelectItem> tiposBusquedaPersonaNegocio;
	private String campoBusquedaPersonaNegocio;
	private String valorBusquedaPersonaNegocio;
	private int paginacionBusquedaPersonaNegocio;
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
   	private HttpSession httpSession;
	//private FacesContext facesContext;
	private boolean comprobacionGenerada;
	private boolean existeRetencion;
	private PropertyResourceBundle entornoProperties;
	private byte[] fileSrc; 
	//private String fileName;
	//private String fileExtension;
	private String ftpDigitalizacionHost;
	private String ftpDigitalizacionPort;
	private String ftpDigitalizacionUser;
	private String ftpDigitalizacionPass;
	private String ftpDigitalizacionRuta;
	private Long facturaId;
	private String prefijoFacturas;
	// PERFILES
	private String perfilRequiereObra;
	private double perfilLimiteCajaChica;
	//private double monto_perfil = 3300;
	private String perfilCajaChicaVoBo;
	private String perfilCajaChicaAutorizacion;
	private String perfilDividirCajaChica;
	private boolean perfilEgresos;
	private ConGrupoValores pojoGpoValFormasPago; 
	private List<ConValores> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	private boolean reqReferencia;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;

	
	public CajaChicaAction() throws NamingException, Exception {
		FacesContext facesContext =  FacesContext.getCurrentInstance();
		Application app = facesContext.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
		ValueExpression dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		ValueExpression msg = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle tyg = (PropertyResourceBundle) msg.getValue(facesContext.getELContext());
		Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
		
		try {
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			this.entornoProperties = (PropertyResourceBundle) dato.getValue(facesContext.getELContext());
	        this.httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
			this.loginManager = (LoginManager) ve.getValue(facesContext.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId(); 
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
	
			// Variables para la subida de archivos
			this.ftpDigitalizacionHost = entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = entornoProperties.getString("ftp.digitalizacion.ruta");
			this.prefijoFacturas = "CXC-CC-"; 
	   				
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
	   		this.pojoSucursal = new SucursalExt();
			
			this.listCajaChica = new ArrayList<PagosGastos>();
			this.listCuentas = new ArrayList<CtasBanco>();
			this.listRetEncontradas = new ArrayList<ConValores>();
			this.listRetDelGasto = new ArrayList<GastosImpuestoExt>();		
			this.listDesgloseRet= new ArrayList<PagosGastosDetImpuestosExt>();
			this.listBeneficiarios = new ArrayList<Persona>();
			this.lisImpuestosEliminados = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listImpuestosDelGasto = new ArrayList<GastosImpuestoExt>();		
			this.listFacturaConImpuestos = new ArrayList<FacturaImpuestos>();
			this.listFacturas = new ArrayList<PagosGastosDetExt>();
			this.listSucursales = new ArrayList<Sucursal>();
			
	   		tipoReporte = 1;
	   		tipoCajaChica = 0;
			
			tipoMensaje =0;
			numPagina = 1;
			numPaginaFact = 1;
			totalFacturasReportadas = 0D;
			totImpuestos=0D;
			totalMenosImpuestos =0D;
			totRetenciones=0D;
			totPago=0D;
			subtotalMasImpuestos=0D;
			tipoPersona="P";
			cuenta="";
			beneficiario="";
			mensaje = "";
			encontroMismoGrupo=false;
			facturaConLlave = false;
			this.existeRetencion=false;
			
			tipoBusqueda = new ArrayList<String>();
			tipoBusqueda.add("Fecha del Movto. a Caja Chica");
			tipoBusqueda.add("Beneficiario");
			tipoBusqueda.add("Cuenta Bancaria");
			tipoBusqueda.add("ID");
			valTipoBusqueda = tipoBusqueda.get(0);
			campoBusqueda = "";
			
			// Busqueda OBRAS
			tipoBusquedaObra = new ArrayList<String>();
			tipoBusquedaObra.add("Nombre");
			tipoBusquedaObra.add("Id de Obra");
			tipoObra = "O";
			this.tiposBusquedaObra = new ArrayList<SelectItem>();
			this.tiposBusquedaObra.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaObra.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObra.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObra.add(new SelectItem("id", "ID"));
			this.campoBusquedaObra = tiposBusquedaObra.get(0).getValue().toString();
			this.valorBusquedaObra = "";
			this.paginacionObras = 1;
			comprobacionGenerada=false;
			
			listBusqRet = new String[3];
	   		listBusqRet[0] = "Clave";
	   		listBusqRet[1] = "Descripcion";
	   		listBusqRet[2] = "Cuenta Contable";
	   		
	   		// BUSQUEDA PERSONA/NEGOCIO
	   		this.tiposBusquedaPersonaNegocio = new ArrayList<SelectItem>();
	   		this.tiposBusquedaPersonaNegocio.add(new SelectItem("nombre", "Nombre"));
	   		this.tiposBusquedaPersonaNegocio.add(new SelectItem("rfc", "RFC"));
	   		this.tiposBusquedaPersonaNegocio.add(new SelectItem("id", "ID"));
	   		this.campoBusquedaPersonaNegocio = this.tiposBusquedaPersonaNegocio.get(0).getDescription();
	   		this.valorBusquedaPersonaNegocio = "";
	   		this.paginacionBusquedaPersonaNegocio = 1;
	   		valBusqRet = "";
	   		campoBusqRet =listBusqRet[0];
	   		this.ctx = new InitialContext();
			this.ifzGpoVal 			= (ConGrupoValoresRem) 			this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzGtosComp 		= (PagosGastosRem) 				this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
			this.ifzCtas 			= (CtasBancoRem)				this.ctx.lookup("ejb:/Logica_TYG//CtasBancoFac!net.giro.tyg.logica.CtasBancoRem");
			this.ifzPersonas 		= (PersonaRem) 					this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
			this.ifzConValores 		= (ConValoresRem) 				this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGastoImpuesto 	= (GastosImpuestoRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
			this.ifzDesgloImpto 	= (PagosGastosDetImpuestosRem) 	this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
			this.ifzComprobadoGto 	= (PagosGastosDetRem) 			this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzObras 			= (ObraRem) 					this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzSucursal 		= (SucursalesRem) 				this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			//this.ifzProPagDet 	= (ProgPagosDetalleRem) 		this.ctx.lookup("ejb:/Logica_CuentasPorPagar//ProgPagosDetalleFac!net.giro.cxp.logica.ProgPagosDetalleRem");
			this.ifzReportes		= (ReportesRem)					this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzFtp 			= (FtpRem) 						this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzPerNeg 			= (PersonaNegocioVistaRem) 		this.ctx.lookup("ejb:/Logica_Clientes//PersonaNegocioVistaFac!net.giro.clientes.logica.PersonaNegocioVistaRem");
			
			this.ifzComprobadoGto.setInfoSecion(this.loginManager.getInfoSesion());
	
			// FORMAS DE PAGO
			this.pojoGpoValFormasPago = this.ifzGpoVal.findByName("SYS_FORMAS_PAGO");
			if (this.pojoGpoValFormasPago == null || this.pojoGpoValFormasPago.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_FORMAS_PAGO en con_grupo_valores");
			cargarFormasDePago();
	
			// MOVIMIENTO GASTOS
			this.pojoGpoValGasto = this.ifzGpoVal.findByName("SYS_MOVGTOS");
			if (this.pojoGpoValGasto == null || this.pojoGpoValGasto.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");
			
			deseaCancelar = tyg.getString("navegacion.label.deseaCancelar");
			
			// Recuperamos perfiles
			getPerfiles();
			
			cargarSucursales();
			cargarCuentas();
		} catch (Exception e) {
			log.error("Error en CuentasPorPagar.RegistroGastosAction", e);
			this.ctx = null;
		}
  	}
	
	
	public String buscar(){
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
			
			if (listCajaChica.isEmpty()) {
				control(12);
				log.info("ERROR 12: Busqueda sin resultados");
			}
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorPagar.CajaChicaAction.buscar", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
   	public String nuevo (){
		try {
			control();
			
			this.pojoCajaChica = new PagosGastosExt();
			this.pojoCtas = new CtasBancoExt();
			this.pojoBeneficiarios = new PersonaExt();
			this.pojoObra = new Obra();
			this.pojoSucursal = new SucursalExt();
			
			this.listFacturaConImpuestos.clear();			
			this.lisImpuestosEliminados.clear();
			this.listDesgloseImpuestos.clear();
			this.listImpuestosDelGasto.clear();
			
			this.cuenta="";
			this.tipoPersona = "P";
			this.beneficiario="";
			this.totalFacturasReportadas = 0D;
			this.varOper = "";// this.operacionValidate("C"); // "2";
			this.observaciones = "";
   			this.numPaginaFact = 1;
			
			//evaluaOperacion(); 

			this.perfilCajaChicaVoBo = "";
			this.perfilCajaChicaAutorizacion = "";
			actualizarPerfil("DIVIDIR_CAJA");
			if ("S".equals(this.perfilDividirCajaChica)) {
				actualizarPerfil("CAJA_CHICA_VOBO");
				actualizarPerfil("CAJA_CHICA_AUTORIZACION");
			}
		} catch (Exception e) {
			control(true);
			log.error("Error en el metodo nuevo.", e);
			return "ERROR";
		}
		
		return "OK";
	}

   	public void editar() {
   		try {
			log.info("CajaChica... Preparando para editar");
   			control();
   			actualizarPerfil("DIVIDIR_CAJA");
   			this.numPaginaFact = 1;
			if ("S".equals(this.perfilDividirCajaChica)) {
				actualizarPerfil("CAJA_CHICA_VOBO");
				actualizarPerfil("CAJA_CHICA_AUTORIZACION");
			}
			
			log.info("CajaChica... Extendiento pojo (PagosGastos)");
			this.pojoCajaChica = this.ifzGtosComp.convertir(this.pojoCaja);
			log.info("CajaChica... Pojo (PagosGastos) extendido");
			
			this.vobo = (this.pojoCajaChica.getVoboPor() > 0 ? true : false);
			this.autorizado = (this.pojoCajaChica.getAutorizadoPor() > 0 ? true : false);
			this.tipoCajaChica = ("Obra".equals(this.pojoCajaChica.getArea()) ? 0 : 1);
			this.varOper = this.pojoCajaChica.getOperacion(); // this.operacionValidate(this.pojoGtosComp.getOperacion());
			this.pojoObra = this.pojoCajaChica.getIdObra();
			
			// Sucursal
			log.info("CajaChica... Recuperando Sucursal");
			this.sucursal = "";
			if (this.pojoCajaChica != null && this.pojoCajaChica.getIdSucursal() != null && this.pojoCajaChica.getIdSucursal().getId() > 0L) {
				this.sucursal = this.pojoCajaChica.getIdSucursal().getId() + " - " + this.pojoCajaChica.getIdSucursal().getSucursal();
				this.pojoSucursal = this.pojoCajaChica.getIdSucursal();
				log.info("CajaChica... Sucursal recuperada");
			}
			
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
				listFacturaConImpuestos.clear();
				listFacturas = this.ifzComprobadoGto.findLikePojoCompletoExt(this.pojoCajaChica.getId(), 100);
				for (PagosGastosDetExt var : listFacturas) {
					reg = new FacturaImpuestos();
					reg.setPojoFactura(var);
					
					try {
						reg.setListImpuestos(this.ifzDesgloImpto.findLikePojoCompletoExt(var, 100));
					} catch (Exception e) {
						log.error("Error al obtener la lista de impuestos de la factura " + var.getReferencia() + " (" + var.getId() + ")", e);
					}
					
					listFacturaConImpuestos.add(reg);
				}
				
				log.info("CajaChica... " + listFacturaConImpuestos.size() + " Facturas recuperadas");
				log.info("CajaChica... Totalizando");
				actualizaTotalFaturas();
			} catch (Exception e) {
	   			control(true);
				log.error("Error en CuentasPorPagar.CajaChicaAction.editar --> Generando lista de facturas con impuestos", e);
			}
			
			log.info("CajaChica... Lista para editar");
   		} catch(Exception e) {
   			control(true);
			log.error("Error en CuentasPorPagar.CajaChicaAction.editar.", e);
		}
   	}
   	
	public String guardar() {
		Pattern pat = null;
		Matcher match = null;
		Respuesta resp = new Respuesta();
		boolean registroNuevo = false;
		
		try {
			control();
			log.info("Guardando CajaChica (PagosGastos)");
			
			if ("S".equals(this.perfilRequiereObra)  && this.tipoCajaChica == 0 && this.pojoObra == null) {
				control(19);
				log.info("ERROR 19: Debe selecccionar una Obra");
				return "OK";
			}
			
			if(this.listFacturaConImpuestos.isEmpty()) {
				control(15);
				log.info("ERROR 15: No añadio ninguna factura");
				return "OK";
			}

			// this.varOper = this.operacionValidate(this.varOper);
			if(this.pojoCajaChica.getId() == null)
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
			if(!"G".equals(this.pojoGtosComp.getOperacion())) {
				/**** valida que el concepto de caja chica este programado ****/
				/*long concepto = 283; //Id concepto CAJA CHICA
				
   				this.listProPagDet = ifzProPagDet.findByAgenteEstatusMontoConcepto(sucursalesVisibles, "G", concepto);
   				if(this.listProPagDet == null){
   					this.tipoMensaje = 16;
   					return "ERROR";
   				}* /
				
				for(Object obj: this.listProPagDet){
					ProgPagosDetalle ppd = (ProgPagosDetalle)((Object[])obj)[1];
					if(!"R".equals((String)((Object[])obj)[0]) && pojoGtosComp.getMonto().compareTo(ppd.getMonto().doubleValue()) != 0){
						ban = 3;
					}
					
					if("R".equals((String)((Object[])obj)[0]) && pojoGtosComp.getMonto().compareTo(ppd.getMonto().doubleValue()) != 0){
						ban = 2;
					}
					
					if(!"R".equals((String)((Object[])obj)[0]) && pojoGtosComp.getMonto().compareTo(ppd.getMonto().doubleValue()) == 0){
						ban = 1;
					}

					if("R".equals((String)((Object[])obj)[0]) && pojoGtosComp.getMonto().compareTo(ppd.getMonto().doubleValue()) == 0){
						ban = 0;
						break;
					}
				}
				
				if(ban == 1){
					this.tipoMensaje =17;
   					return "ERROR";
				}
				
				if(ban == 2){
					this.tipoMensaje =18;
   					return "ERROR";
				}
				
				if(ban == 3){
					this.tipoMensaje =16;
   					return "ERROR";
				}*/

			//validando Cuenta
			log.info("Validando cuenta: " + this.cuenta);
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.cuenta);			
			if(match.find()) {
				if(registroNuevo || this.pojoCajaChica.getIdCuentaOrigen() == null) {
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
				
			/*	if (! "OK".equals(validaSaldoCuentaPropios()))
					return "ERROR";
			} else {
				this.pojoGtosComp.setIdCuentaOrigen(null);				
			} */
			
			//validando sucursal
			log.info("Validando sucursal: " + this.sucursal);
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			match = pat.matcher(this.sucursal);
			if(match.find()) {
				if(registroNuevo || this.pojoCajaChica.getIdSucursal() == null) {
					log.info("Obtenemos sucursal (Sucursal): " + match.group(1));
					this.pojoSucursal = this.ifzGtosComp.findSucursalById(Long.valueOf(match.group(1))); // this.ifzSucursal.findById(Long.valueOf(match.group(1)));
				} else {
					if(this.pojoSucursal.getId() != Long.valueOf(match.group(1))) {
						log.info("Cambiamos sucursal por: " + match.group(1));
						this.pojoSucursal = this.ifzGtosComp.findSucursalById(Long.valueOf(match.group(1))); // this.ifzSucursal.findById(Long.valueOf(match.group(1)));
					} else {
						log.info("sucursal sin ambios");
					}
				}
			}
			this.pojoCajaChica.setIdSucursal(this.pojoSucursal);
			
			//validando beneficiario
			log.info("Validando beneficiario: " + this.beneficiario);
			match = pat.matcher(this.beneficiario);
			if (match.find()) {
				if(registroNuevo || this.pojoCajaChica.getIdBeneficiario() == null) {
					log.info("Obtenemos beneficiario (Persona): " + match.group(1));
					this.pojoBeneficiarios = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona); // this.ifzPersonas.findByIdPojoCompleto(Long.valueOf(match.group(1)));
				} else {
					if(this.pojoBeneficiarios.getId() != Long.valueOf(match.group(1))) {
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
				//this.pojoGtosComp.setOperacion("C");//cheque
				
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
				
				/*if (this.pojoGtosComp != null)
					return "OK";*/
				
				// Guardamos
				log.info("Guardamos CajaChica (PagosGastos)");
				resp = ifzGtosComp.salvar(this.pojoCajaChica, true, null);
				log.info("CajaChica (PagosGastos) guardada");
				
				//validando folio cheque
				if(resp.getResultado() != -1){
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
					if(! "BIEN".equals(resp.getRespuesta())) {
						this.mensaje = resp.getRespuesta();
						
						if((resp.getRespuesta().indexOf("SE CANCELA EL FOLIO ") > -1) || (resp.getRespuesta().indexOf("SE CANCELAN LOS FOLIOS ") > -1)) {
							control(true, 7, this.mensaje);
							log.info("ERROR 7: " + this.mensaje);
						} else {
							control(true, 6, this.mensaje);
							log.info("ERROR 6: " + this.mensaje);
						}
						
						// this.varOper = this.operacionValidate(this.varOper);
						return "ERROR";   							
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
			if(! this.listFacturaConImpuestos.isEmpty()) {
				log.info("Guardando facturas (PagosGastosDet)");
				for(FacturaImpuestos var: listFacturaConImpuestos ){
					if (var.getPojoFactura().getId() != null)
						continue;
					
					var.getPojoFactura().setIdPagosGastos(this.pojoCajaChica);					
					var.getPojoFactura().setId(this.ifzComprobadoGto.save(var.getPojoFactura()));
					
					if(! var.getListImpuestos().isEmpty()){
						for( PagosGastosDetImpuestosExt var2 : var.getListImpuestos()){
							var2.setIdPagosGastosDet(var.getPojoFactura());
							if(var2.getId() == null)
								var2.setId(this.ifzDesgloImpto.save(var2));
							else
								this.ifzDesgloImpto.update(var2);
						}
					}
				}
				log.info("Facturas (PagosGastosDet) guardadas");
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			this.pojoCaja = this.ifzGtosComp.convertir(this.pojoCajaChica);
			mensajeTransaccion();
		} catch(Exception e) { 
			control(true);
			log.error("Error en CuentasPorPagar.CajaChicaAction.guardar", e);
			// this.varOper = this.operacionValidate(this.varOper);
			return "ERROR";
		}
		
		return "OK";
	}

	public String cancelar() {
		//Respuesta resp = new Respuesta();
		
		try {
			control();
			
			//TODO: PROCESO TEMPORAL DE CANCELACION. LO COMENTADO ES LO CORRECTO
			log.info("Cancelando Caja Chica (PagosGastos)");
			this.msgCancelar = "";
			this.pojoCaja.setEstatus("X");
			this.pojoCaja.setModificadoPor(this.usuarioId);
			this.pojoCaja.setFechaModificacion(Calendar.getInstance().getTime());
			/*this.pojoGtosComp.setEstatus("X");
			this.pojoGtosComp.setModificadoPor(this.usuarioId);
			this.pojoGtosComp.setFechaModificacion(Calendar.getInstance().getTime());*/
			
			// Actualimos registro
			this.ifzGtosComp.update(this.pojoCaja);
			log.info("Caja Chica (PagosGastos) cancelada");
						
			/*resp = ifzGtosComp.cancelacion(this.pojoGtosComp, Calendar.getInstance().getTime(), Short.valueOf(String.valueOf(this.usuarioId)), null);
			if(resp.getResultado() == -1) {
				this.msgCancelar = resp.getRespuesta();
			} else {
				this.msgCancelar = ""; 
				for(PagosGastosExt var : this.listCajaChica) {				
					if(var.getId().equals(this.pojoGtosComp.getId())) {		
						var.setEstatus("X");					
						break;
					}
				}	
			}*/
		} catch(Exception e) {
			control(true);
			this.msgCancelar = "Ocurrio un problema inesperado";
			log.error("Error en CuentasPorPagar.CajaChica.cancelar", e);
			return "ERROR";
		}
		
		return "OK";	
	}

	public String evaluaCancelar(){
   		try {
   			control();
   			
   			this.msgCancelar = "";
   			if (this.pojoCaja != null)
   				this.msgCancelar = "X".equals(this.pojoCaja.getEstatus()) ? "" : this.deseaCancelar;
   			/*if (this.pojoGtosComp != null)
   				this.msgCancelar = "X".equals(this.pojoGtosComp.getEstatus()) ? "" : this.deseaCancelar;*/
		} catch (Exception e) {	
   			control(true);		
			log.error("Error CuentasPorPagar.CajaChicaAction.evaluaCancelar.", e);
			return "ERROR";
		}
   		
		return "OK";
   	}

	public String reporte() {
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		String nombreDocumento = "";
		
		try{
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
				if(respuesta.getErrores().getCodigoError() == 0L) {
					nombreDocumento = "ReposicionCajaChica-" + this.pojoCaja.getId() + "." + respuesta.getBody().getValor("formatoReporte");
					
					this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
					this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte")); 	
					this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
				} else {
					this.mensaje = respuesta.getErrores().getDescError();
					System.out.println( "mensaje: " + mensaje);
				}
			}
		} catch (Exception e) {
			this.mensaje = "error al ejecutar reporte";
			e.printStackTrace();
		}
		
		return "OK";
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
			
			Respuesta respuesta = this.ifzGtosComp.enviarTransaccion(this.pojoCaja, 1L);
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
	
   	// ---------------------------------------------------------------------------
	// Detalles
   	// ---------------------------------------------------------------------------
	
	public String agregar(){
		try {
			control();
			
			if(pojoCajaChica.getId() != null) {
				if (! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					control(10);
					log.info("ERROR 10: No se permite añadir facturas.");
					comprobacionGenerada = true;
					return "ERROR";
				}
			}
			
			this.conceptoGasto="";
			this.conceptoGasto2="";
			this.nombreProveedor="";
			this.descripcionFactura="";
			this.facturado = false;
			
			encontroMismoGrupo= false;
			
			totImpuestos=0D;
			this.totalMenosImpuestos=0D;
			this.subtotalMasImpuestos=0D;
			listDesgloseImpuestos.clear();
			listDesgloseRet.clear();
			listRetEncontradas.clear();
			
			this.totRetenciones=0D;
			this.totPago=0D;
			this.observaciones = "";
			
			PersonaExt proveedor = new PersonaExt();
			Pattern pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			Matcher match = pat.matcher(this.beneficiario);
			
			if(match.find()) {
				proveedor = this.ifzGtosComp.findPersonaById(Long.valueOf(match.group(1)), this.tipoPersona);
			} 
			
			this.pojoComprobadoGto = new PagosGastosDetExt();
			this.pojoComprobadoGto.setIdProveedor(proveedor);
			
			this.reg = new FacturaImpuestos();
		} catch(Exception e) {
			control(true);
			log.error("Error en CuentasPorPagar.CajaChicaAction.agregar", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String evaluaEditar() {
   		try {
   			control();
			
   			this.facturaConLlave = false;
			if (reg.getPojoFactura().getId() != null) {
				if (! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					control(11);
		   			log.info("ERROR 11: No se permite editar las facturas");
		   			this.facturaConLlave = true;
		   		}
			}
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorPagar.CajaChicaAction.evaluaEditar.", e);
			return "ERROR";
		}
   		
		return "OK";
   	}

   	public String evaluarFactura(){
   		try {
			control();
			
			this.facturaConLlave = false;
			if (this.reg.getPojoFactura().getId() != null) {
				if (! this.paramsRequest.containsKey("DETEDIT") || (this.paramsRequest.containsKey("DETEDIT") && ! "1".equals(this.paramsRequest.get("DETEDIT")))) {
					control(2);
		   			log.info("ERROR 2: No se permite eliminar facturas");
					this.facturaConLlave = true;
				} 
			}
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorPagar.CajaChicaAction.evaluarFactura.", e);
			return "ERROR";
		}
   		
		return "OK";
   	}

   	public String eliminar(){
		try {
			control();
			
			this.listFacturaConImpuestos.remove(reg);
			actualizaTotalFaturas();
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorPagar.CajaChicaAction.eliminar", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
	public String guardarFactura(){
		boolean encontro 	  = false;			
		boolean registroNuevo = false;
		Pattern pat 		  = null;
		Matcher match 		  = null;
		encontroMismoGrupo	  = false;
		tipoMensaje			  = 0;
		mensaje				  = "";
		
		try{
			if (! "OK".equals(validaMontoFactura()))
				return "ERROR";
			
			if (! "OK".equals(validaMontoImpuestos()))
				return "ERROR";
			
			if (! "OK".equals(validaGrupoImpuestos()))
				return "ERROR";
		
			if (! "OK".equals(validaMontoFacturaContraTotalImpuestos()))
				return "ERROR";
			
			if (this.facturaId == null)
				analizarArchivo();
	
			if(this.pojoComprobadoGto.getId() == null)
				registroNuevo = true;
			
			this.pojoComprobadoGto.setModificadoPor(this.usuarioId);
			this.pojoComprobadoGto.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoComprobadoGto.setFacturado((short) (this.facturado ? 1 : 0));
			this.pojoComprobadoGto.setIdXml(this.facturaId);
			   					
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			
			//validando gasto
			match = pat.matcher(this.conceptoGasto);			
			if (match.find()) {
				if(registroNuevo) {
					this.pojoConceptoGtos= this.ifzConValores.findById(Long.valueOf(match.group(1)));
				} else {
					if (this.pojoConceptoGtos.getId() != Long.valueOf(match.group(1)))
						this.pojoConceptoGtos= this.ifzConValores.findById(Long.valueOf(match.group(1)));
				}
			}

			this.pojoComprobadoGto.setIdConcepto(this.pojoConceptoGtos);
			this.pojoComprobadoGto.setTotalImpuestos(totImpuestos);  					
			this.pojoComprobadoGto.setSubtotal(totalMenosImpuestos);   	
			this.pojoComprobadoGto.setTotalRetenciones(totRetenciones);
			this.pojoComprobadoGto.setIdPagosGastos(this.pojoCajaChica);
			this.pojoComprobadoGto.setObservaciones(this.observaciones);
						
			if( this.pojoCajaChica.getId() == null){
				//si el gasto no existe guardo las facturas en una lista
				// y las guardare hasta que creen un gasto. sí el gasto no existe no puedo grabar 
				//facturas hasta que me serciore que creen un gasto. Siempre que el gasto no tenga llave 
				//la factura tampoko tendra llave, por eso no pregunto si es nueva 	
					
				this.pojoComprobadoGto.setCreadoPor(this.usuarioId);
				this.pojoComprobadoGto.setFechaCreacion(Calendar.getInstance().getTime());
				
				if (! this.listDesgloseImpuestos.isEmpty()) {
					for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
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
					
					if(! this.listDesgloseImpuestos.isEmpty()){
						for( PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
							var.setIdPagosGastosDet(this.pojoComprobadoGto);
							// GUARDAMOS
							var.setId(this.ifzDesgloImpto.save(var));
						} 
					}
				} else {
					this.ifzComprobadoGto.update(this.pojoComprobadoGto);

					if(! this.listDesgloseImpuestos.isEmpty()){
						for(PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos){
							var.setIdPagosGastosDet(this.pojoComprobadoGto);
							
							if(var.getId() == null)
								var.setId(this.ifzDesgloImpto.save(var));
							else
								this.ifzDesgloImpto.update(var);
						}
					}
				}
				
				//eliminando los pojos de impuestos de la base de datos,
				// ya que anteriormente solo los elimine de la memoria
				if(! this.lisImpuestosEliminados.isEmpty()){
					for(PagosGastosDetImpuestosExt var : lisImpuestosEliminados){
						if(var.getId() != null)
							this.ifzDesgloImpto.delete(var);
					}
					
					this.lisImpuestosEliminados.clear();
				}
			}  	
			//guardando las facturas con llave o sin llave en la lista de facturaImpuestos por si hacen clic
			// en editar de una factura poder listar los impuestos tal cual los dejaron
			reg.setPojoFactura(this.pojoComprobadoGto);
			reg.setListImpuestos(copiaListas(listDesgloseImpuestos));
			
		   encontro = false;
			if(! listFacturaConImpuestos.isEmpty()){
				for(FacturaImpuestos var: listFacturaConImpuestos){
					if(var.equals(reg)){
						var = reg;
						encontro = true;
					}
				}
			}
			   					
			if(! encontro)
				this.listFacturaConImpuestos.add(reg);

			actualizaTotalFaturas();
			this.facturaId = null;
   		} catch(Exception e) {
   			mensaje = "";
			tipoMensaje = 9;
			log.error("error en guardarFactura", e);			
			return "ERROR";
		}
   			
		return "OK";
   	}

   	public String eliminarImpuestoDesglosado(){
   		try{
   			this.tipoMensaje = 0;
			this.sum_impto = 0D;
			this.sum_imptoRet = 0D;
			this.impto = 0D;
		
			this.lisImpuestosEliminados.add(this.pojoDesgloImpto);
			this.listDesgloseImpuestos.remove(this.pojoDesgloImpto);
			
			if(! this.listDesgloseImpuestos.isEmpty()){			
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
						sum_imptoRet = Utilerias.redondear(sum_imptoRet + var.getValor().doubleValue(),2);	
					else	
						sum_impto = Utilerias.redondear(sum_impto + var.getValor().doubleValue(),2);	
				}
				
				this.totImpuestos = this.sum_impto;
				this.totRetenciones = this.sum_imptoRet;
				this.totalMenosImpuestos = Utilerias.redondear(this.subtotalMasImpuestos - sum_impto, 2);
				this.totPago = Utilerias.redondear(this.subtotalMasImpuestos - sum_imptoRet, 2);
			} else {
				//sabemos que la lista de imptos quedo vacia porque entro a esta opcion y ponemos en cero el total de impuestos
				totImpuestos=0D;	
				totRetenciones = 0D;
				this.totalMenosImpuestos = this.subtotalMasImpuestos;
				this.totPago = this.subtotalMasImpuestos;
			}
		} catch(Exception e) {
			this.tipoMensaje =5;
			log.error("error en eliminarImpuestoDesglosado", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String cambioMontoImpuesto(){
   		try{
			this.tipoMensaje = 0;
			if(this.subtotalMasImpuestos != 0){
				this.sum_impto = 0D;
				this.sum_imptoRet = 0D;
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getValor() != null) {
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
							sum_imptoRet = Utilerias.redondear(sum_imptoRet + var.getValor().doubleValue(),2);
						else
							sum_impto = Utilerias.redondear(sum_impto + var.getValor().doubleValue(),2);
					}
				}
				
				this.totImpuestos = this.sum_impto;
				this.totRetenciones = this.sum_imptoRet;
				this.totPago = Utilerias.redondear(this.subtotalMasImpuestos - this.totRetenciones, 2);	
				this.totalMenosImpuestos = Utilerias.redondear( this.subtotalMasImpuestos - this.sum_impto, 2);	
			}
		} catch (Exception e) {
			this.tipoMensaje = 9;
			log.error("error en cambioMontoImpuesto", e);
			return "ERROR";
		}
		
		return "OK";
	}
   	
   	public String actualizaTotalFaturas(){
		try{
			this.totalFacturasReportadas=0D;
			this.tipoMensaje =0;
			
			if(! listFacturaConImpuestos.isEmpty()) {
				for (FacturaImpuestos var: listFacturaConImpuestos) {
					this.totalFacturasReportadas = Utilerias.redondear(this.totalFacturasReportadas + ((var.getPojoFactura().getSubtotal() + var.getPojoFactura().getTotalImpuestos())- var.getPojoFactura().getTotalRetenciones()),2);
				}
			}
			
			// Actualizamos el monto
			this.pojoCajaChica.setMonto(this.totalFacturasReportadas);
		} catch(Exception e) {
			log.error("error en actualizaTotalFaturas", e);
			return "ERROR";
		}
		
		return "OK";
	}

   	public String validaMontoImpuestos(){
		try{
			encontroMismoGrupo=false;
			this.tipoMensaje = 0;
			
			if(! this.listDesgloseImpuestos.isEmpty()){
				for( PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos){
					if(var1.getValor() == null || var1.getValor().doubleValue() <= 0){
						encontroMismoGrupo = true;
						this.tipoMensaje=3;
						return "ERROR";
					}  
				}
			}
		} catch(Exception e) {			
			log.error("error en validaMontoImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
   	public String validaMontoFactura(){
		try{
			encontroMismoGrupo=false;
			this.tipoMensaje = 0;
			
			if(this.subtotalMasImpuestos <= 0){
				encontroMismoGrupo = true;
				this.tipoMensaje=4;
				return "ERROR";
			}	
		}catch(Exception e){			
			log.error("error en validaMontoFactura", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
	public String validaMontoFacturaContraTotalImpuestos(){
		try{
			encontroMismoGrupo=false;
			Double sumaImpuestos = 0D;
			this.tipoMensaje = 0;
			
			if(! this.listDesgloseImpuestos.isEmpty()){
				for( PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos){
					sumaImpuestos = sumaImpuestos + var1.getValor().doubleValue();
				}
			}
			
			if(sumaImpuestos > this.subtotalMasImpuestos){
				encontroMismoGrupo = true;
				this.tipoMensaje=5;
				return "ERROR";
			}	
		} catch(Exception e) {			
			log.error("error en validaMontoFacturaContraTotalImpuestos", e);
			return "ERROR";
		}
	
		return "OK";
	}
   	
   	public String desglosaImpuestos(){
   		try{
			this.tipoMensaje  = 0;
			this.sum_impto	  = 0D;
			this.impto		  = 0D;
			this.imptoRet	  = 0D;
			this.sum_imptoRet = 0D;
			Pattern pat 	  = null;
   			Matcher match 	  = null;
   			
   			List<PagosGastosDetImpuestosExt> listDesgloseImpuestos_tmp = new ArrayList<PagosGastosDetImpuestosExt>();
   			
   			if (this.facturado) {
   				totImpuestos			 = 0D;
				totRetenciones			 = 0D;
				this.totalMenosImpuestos = Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
				totPago 				 = Utilerias.redondear( this.subtotalMasImpuestos - totRetenciones,2);
   				return "OK";
   			}
				
   			//validando gasto para que el pojo de gasto tenga valor correcto ya que a veces se quedaba con el valor del ultimo gasto que escogieron en otra factura
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
								
			match = pat.matcher(this.conceptoGasto);			
			if(match.find())									
				this.pojoConceptoGtos= this.ifzConValores.findById(Long.valueOf(match.group(1)));
			
			this.listImpuestosDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","DE", this.pojoConceptoGtos.getId());
			
			if(! this.listImpuestosDelGasto.isEmpty()) {
				if (listDesgloseImpuestos.isEmpty()){//si viene vacio es posible que hayan cambiado el concepto de gasto o
					// es la primera vez que se cargaran lo imptos al comprobante
					for( GastosImpuestoExt var : this.listImpuestosDelGasto){
						this.pojoDesgloImpto = new PagosGastosDetImpuestosExt();
						this.pojoDesgloImpto.setModificadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
						this.pojoDesgloImpto.setCreadoPor(this.usuarioId);
						this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
						this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
						this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
				
						impto = Utilerias.redondear(subtotalMasImpuestos - (Utilerias.redondear(this.subtotalMasImpuestos /((Double.valueOf(var.getImpuestoId().getAtributo1())/ 100)+1),2)),2);
						this.pojoDesgloImpto.setValor(new BigDecimal(impto));		
						
						sum_impto = Utilerias.redondear(sum_impto + this.pojoDesgloImpto.getValor().doubleValue(),2);
						this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
					}
					
					totImpuestos = sum_impto;
					this.totalMenosImpuestos = Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
					
					//Verifico si existen retenciones para el gasto
					this.listRetDelGasto = this.ifzGastoImpuesto.findByPropertyPojoCompletoExt("gastoId","AC", this.pojoConceptoGtos.getId());
					
					if(! this.listRetDelGasto.isEmpty()){
						for(GastosImpuestoExt var: listRetDelGasto){
							pojoDesgloImpto = new PagosGastosDetImpuestosExt();					
							this.pojoDesgloImpto.setModificadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
							this.pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setCreadoPor(Long.valueOf(String.valueOf(this.usuarioId)));
							this.pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
							this.pojoDesgloImpto.setIdImpuesto(var.getImpuestoId());
							this.pojoDesgloImpto.setIdPagosGastosDet(this.pojoComprobadoGto);
							this.pojoDesgloImpto.setValor(BigDecimal.ZERO);
							
							impto = Utilerias.redondear(subtotalMasImpuestos - (Utilerias.redondear(this.subtotalMasImpuestos /((Double.valueOf(var.getImpuestoId().getAtributo1())/ 100)+1),2)),2);
							this.pojoDesgloImpto.setValor(new BigDecimal(impto));

							sum_imptoRet = Utilerias.redondear(sum_imptoRet + this.pojoDesgloImpto.getValor().doubleValue(),2);
							this.listDesgloseImpuestos.add(this.pojoDesgloImpto);
						}
					}
					
					this.totRetenciones = Utilerias.redondear(sum_imptoRet,2);
					this.totPago		= Utilerias.redondear(this.subtotalMasImpuestos - totRetenciones,2);
				} else { // la lista si tenia elementos y hay que hacer el desglose imptos en base a los imptos que tiene actualmente la lista
					listDesgloseImpuestos_tmp.addAll(listDesgloseImpuestos);					
					this.listDesgloseImpuestos.clear();
					
					for( PagosGastosDetImpuestosExt var : listDesgloseImpuestos_tmp){		
						if("AC".equals(var.getIdImpuesto().getTipoCuenta())){
							var.setValor(BigDecimal.ZERO);
							sum_imptoRet = Utilerias.redondear(sum_imptoRet + var.getValor().doubleValue(),2);
						} else {
							impto = Utilerias.redondear(subtotalMasImpuestos -(Utilerias.redondear(this.subtotalMasImpuestos /((Double.valueOf(var.getIdImpuesto().getAtributo1())/ 100)+1),2)),2);
							var.setValor(new BigDecimal(impto));
							sum_impto = Utilerias.redondear(sum_impto + var.getValor().doubleValue(),2);
						}
						
						this.listDesgloseImpuestos.add(var);
					}
					
					totImpuestos			 = sum_impto;
					totRetenciones 			 = sum_imptoRet;
					this.totalMenosImpuestos = Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
					totPago					 = Utilerias.redondear(subtotalMasImpuestos - totRetenciones,2);
				}
			} else {
				//porque si no tiene impuestos asociados el gasto, esta propiedad se quedaba NULL y marcaba error
				totImpuestos			 = 0D;
				totRetenciones			 = 0D;
				this.totalMenosImpuestos = Utilerias.redondear( this.subtotalMasImpuestos - sum_impto,2);
				totPago 				 = Utilerias.redondear( this.subtotalMasImpuestos - totRetenciones,2);
			}
		} catch(Exception e) {
			tipoMensaje = 9;
			log.error("Error en desglosaImpuesto", e);
			return "ERROR";
		}
		
		return "OK";
	}

	public void nuevaCarga() {
		this.fileSrc = null;
	}
	
	public void uploadListener(UploadEvent event) throws Exception {
		UploadItem item = null;
		
		try {
			this.encontroMismoGrupo = false;
			this.tipoMensaje = 0;
			this.mensaje = "";
			
			item = event.getUploadItem();
			this.fileSrc = ((item.isTempFile()) ? Files.readAllBytes(item.getFile().toPath()) : item.getData());
		} catch (Exception e) {
			this.encontroMismoGrupo = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en uploadListener", e);
			throw e;
		}
	}
	
	public void analizarArchivo() throws Exception {
		String fileName = "";
		
		try {
			this.encontroMismoGrupo = false;
			this.tipoMensaje = 0;
			this.mensaje = "";
			
			if (this.fileSrc == null) {
				this.facturaId = 0L;
				this.encontroMismoGrupo = true;
				this.tipoMensaje = 1;
				this.mensaje = "No ha especificado ninguna factura (*.xml)";
				log.warn(this.mensaje);
				return;
			} 

			this.facturaId = this.ifzComprobadoGto.analizaFactura(this.fileSrc);
			if (this.facturaId == null || this.facturaId <= 0L) {
				this.facturaId = 0L;
				this.encontroMismoGrupo = true;
				this.tipoMensaje = -1;
				this.mensaje = "Ocurrio un problema al intentar guardar la factura";
				log.warn(this.mensaje);
				return;
			}

			// Subimos fisicamente el archivo
			fileName = this.prefijoFacturas + this.facturaId + ".xml";
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			if (! this.ifzFtp.putArchivo(this.fileSrc, this.ftpDigitalizacionRuta + fileName)) {
				this.encontroMismoGrupo = true;
				this.tipoMensaje = -1;
				this.mensaje = "Ocurrio un problema al intentar guardar la factura en el servidor";
				log.warn(this.mensaje);
			}
		} catch (Exception e) {
			this.encontroMismoGrupo = true;
			this.tipoMensaje = 1;
			this.mensaje = "No ha especificado ninguna factura (*.xml)";
			log.error("Error en CuentasPorPagar.CajaChicaAction.analizarArchivo()", e);
			throw e;
		}
	}
	
	public void descargarArchivo() {
		String fileName = "";
		
		try {
			this.encontroMismoGrupo = false;
			this.tipoMensaje = 0;
			this.mensaje = "";
			
			if (this.facturaId == null || this.facturaId <= 0L) {
				this.encontroMismoGrupo = true;
				this.tipoMensaje = 20;
				this.mensaje = "No tiene asignada ninguna factura";
				return;
			}
			 
			// Inicializamos variables de archivos y recuperamos el archivo
			fileName = this.prefijoFacturas + this.facturaId + ".xml";
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			this.fileSrc = this.ifzFtp.getArchivo(this.ftpDigitalizacionRuta + fileName);
			if(this.fileSrc == null || this.fileSrc.length <= 0) {
				this.encontroMismoGrupo = true;
				this.tipoMensaje = 21;
				this.mensaje = "No se encontro el archivo en el servidor";
				return;
			}

			// Ponemos los datos en session
			this.httpSession.setAttribute("nombreDocumento", fileName);
			this.httpSession.setAttribute("formato", "xml");
			this.httpSession.setAttribute("contenido", this.fileSrc);
		} catch (Exception e) {
			this.encontroMismoGrupo = true;
			this.mensaje = "";
			this.tipoMensaje = 9;
    		log.error("Error en CuentasPorPagar.CajaChicaAction.descargarArchivo", e);
		}
	}
	
	public String buscarRet(){
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
			
			if(pojoCajaChica.getMonto() <= 0){
				tipoMensaje = 8;					
				encontroMismoGrupo = true;	
				mensaje = "";
				return "ERROR";
			}

			// do nothing
			/*if (this.pojoCtas.getId() > 0L) {
				if(!ifzCtas.haySaldoSuficiente(pojoGtosComp.getMonto(), this.pojoCtas.getCtaBancoId())){
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

	public String cancelarCheques(){
		Respuesta resp = new Respuesta();
			
   		try {			
   			resp = ifzGtosComp.salvar(this.pojoCajaChica, false, null);
			if(! "BIEN".equals(resp.getRespuesta()) && resp.getResultado() == -1) {
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
   	
   	public String validaGrupoImpuestos(){
		try {
			this.tipoMensaje = 0;
			int contador =0;
			encontroMismoGrupo=false;
			
			if(! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var1 : this.listDesgloseImpuestos) {
					contador = 0;
					for (PagosGastosDetImpuestosExt var2 : this.listDesgloseImpuestos) {
						if (encontroMismoGrupo) 
							continue;
						if(! "0".equals(var2.getIdImpuesto().getAtributo2())) { //porque el cero es general esos pueden existir N impuestos al mimos tiempo
							if (var1.getIdImpuesto().getAtributo2().equals(var2.getIdImpuesto().getAtributo2())) {
								contador = contador + 1;
								if(contador == 2) {
									encontroMismoGrupo=true;
									this.tipoMensaje=1;
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
   	
   	public String agregaRet(){
		try{
			this.tipoMensaje = 0;
			this.encontroMismoGrupo = false;
			
			if(!listDesgloseImpuestos.isEmpty()){
				for(PagosGastosDetImpuestosExt var: listDesgloseImpuestos){
					if(var.getIdImpuesto().getId() == this.pojoNvaRet.getId()){
						this.encontroMismoGrupo = true;
						this.tipoMensaje=14;
						return "OK";
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
		} catch(Exception e) {
			log.error("Error en metodo agregaRet",e);
		}
		
		return "OK";
	}
   	
	public List<Persona> autoacompletaBeneficiario(Object obj) {
		try {
			this.listBeneficiarios = this.ifzPersonas.findLikeClaveNombre("nombre",obj.toString(),this.pojoGpoValPersonas,this.tipoPersona,20,false);	
			return this.listBeneficiarios;
		} catch (Exception e) {
			log.error("error en autoacompletaBeneficiario", e);
			return new ArrayList<Persona>();
		}
	}	   	
	
	public List<Persona> autoacompletaProveedor (Object obj){
		try{
			this.listProveedores = this.ifzPersonas.findLikeProveedor(obj.toString(), this.pojoGpoValPersonas, "PROV", this.pojoConceptoGtos.getId(), 20);
			return this.listProveedores;
		}
		catch(Exception e){
			log.error("error en autoacompletaProveedor", e);
			return new ArrayList<Persona>();
		}
	}
	
	public List<ConValores> autoacompletaConceptoGasto(Object obj){
		try{
			this.listConceptoGasto=this.ifzConValores.findLikeValorIdPropiedadGrupo(obj.toString() , this.pojoGpoValGasto, 20);
			return this.listConceptoGasto;
		}
		catch(Exception e){
			return new ArrayList<ConValores>();
		}
	}
	
	public String GeneraListaProveedores (){
		Pattern pat = null;
		Matcher match = null;
		
		try{
			pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			

			//validando gasto para poder presentar  una lista de proveedores en base al gasto seleccionado
			match = pat.matcher(this.conceptoGasto);			
			if(match.find())							
				this.pojoConceptoGtos= this.ifzConValores.findById(Long.valueOf(match.group(1)));
								
		}catch(Exception e){
			log.error("error en GeneraListaProveedores", e);
			return "ERROR";
		}
		return "OK";
	}
	
	public FacturaImpuestos getReg() {
		return reg;
	}

	public void setReg(FacturaImpuestos reg) {
		this.reg = reg;
		
		if (this.reg.getPojoFactura().getIdXml() == null || this.reg.getPojoFactura().getIdXml() <= 0L)
			this.reg.getPojoFactura().setIdXml(0L);
		
		this.facturaId = this.reg.getPojoFactura().getIdXml();
		this.pojoComprobadoGto = this.reg.getPojoFactura();
		this.conceptoGasto = this.reg.getPojoFactura().getIdConcepto().getId() +" - "+ this.reg.getPojoFactura().getIdConcepto().getDescripcion();
		this.conceptoGasto2=this.conceptoGasto;
		this.nombreProveedor = this.reg.getPojoFactura().getIdProveedor().getId() +" - "+ this.reg.getPojoFactura().getIdProveedor().getNombre() +" "+ (this.reg.getPojoFactura().getIdProveedor().getPrimerApellido() != null ? this.reg.getPojoFactura().getIdProveedor().getPrimerApellido() : "") +" "+(this.reg.getPojoFactura().getIdProveedor().getSegundoApellido() != null ? this.reg.getPojoFactura().getIdProveedor().getSegundoApellido() : "");
		this.descripcionFactura= this.reg.getPojoFactura().getIdConcepto().getDescripcion() + " - "+this.nombreProveedor; // this.reg.getPojoFactura().getProveedorId().getNombre()+" " + (this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoMaterno() : "")+" " +(this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() != null ? this.reg.getPojoFactura().getProveedorId().getApellidoPaterno() :"");
		
		this.subtotalMasImpuestos=this.reg.getPojoFactura().getSubtotal() + this.reg.getPojoFactura().getTotalImpuestos();
		this.totalMenosImpuestos = this.reg.getPojoFactura().getSubtotal();
		this.totImpuestos = this.reg.getPojoFactura().getTotalImpuestos();
		this.totRetenciones = this.reg.getPojoFactura().getTotalRetenciones();
		this.totPago = Utilerias.redondear((this.subtotalMasImpuestos - this.reg.getPojoFactura().getTotalRetenciones()),2);
		this.observaciones = this.reg.getPojoFactura().getObservaciones();
		
		listDesgloseImpuestos = copiaListas(this.reg.getListImpuestos());
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
		
		if (this.listObras == null)
			this.listObras = new ArrayList<Obra>();
		this.listObras.clear();
	}
	
	public void buscarObras() {
		try {
			control();
			this.paginacionObras = 1;
			if (this.listObras != null)
				this.listObras.clear();

			log.info("Buscando obras tipo " + this.tipoObraBusquedaObra + " - " + this.campoBusquedaObra + ": " + this.valorBusquedaObra);
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valorBusquedaObra.trim().replace(" ", "%"), this.tipoObraBusquedaObra);
			if(this.listObras == null || this.listObras.isEmpty()) {
				log.info("ERROR 12 - Busqueda sin resultados");
				control(12);
				return;
			}

			if (this.listObras == null)
				this.listObras = new ArrayList<Obra>();
			log.info(this.listObras.size() + " Obras encontradas");
		} catch (Exception e) {
			log.error("Error en buscar obras", e);
			control(true);
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
			/*
			//this.pojoObra.setTipoProyecto(this.tipoObra);
			this.listObras.clear();
			valorBusquedaObra = tipoBusquedaObra.get(0);
			campoBusquedaObra = "";
			tipoObra = "O";*/
		} catch(Exception e) {
			log.error("error en cambioBeneficiario", e);
			control(true);
		}
	}
		
	public String cambioObra(){
		try{
			this.tipoObra = "";
		}catch(Exception e){
			log.error("error en cambioBeneficiario", e);
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
   	// METODOS DE CONTROL
   	// ---------------------------------------------------------------------------

	private void control() {
		control(false, 0, "");
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
		this.mensaje = val3;
	}

	private void getPerfiles() {
		String valPerfil = "";
		
		if (this.loginManager == null) 
			return;
		
		try {
			// EGRESOS_OPERACION ---> ADMINISTRATIVO
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
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
		} catch(Exception e) {
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
		} catch(Exception e) {
			log.error("Erro al recuperar el perfil [" + perfil + "] en CuentasPorPagar.CajaChicaAction.actualizarPerfil" , e);
		}
	}

	private List<PagosGastosDetImpuestosExt> copiaListas(List<PagosGastosDetImpuestosExt> fuente) {
   		List<PagosGastosDetImpuestosExt> resultado = new ArrayList<PagosGastosDetImpuestosExt>();
   		
   		for(PagosGastosDetImpuestosExt pgi : fuente){
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
					this.listSucursalesItems.add(new SelectItem(var.getId() + " - " + var.getSucursal(), var.getSucursal()));
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
		} catch(Exception e) {
			log.error("error en cargarFormasDePago", e);
		}
	}

	public void cargarCuentas() {
   		try {
			if (this.listCuentas == null)
				this.listCuentas = new ArrayList<CtasBanco>();
			this.listCuentas.clear();
			
			if (this.listCuentasItems == null)
				this.listCuentasItems = new ArrayList<SelectItem>();
			this.listCuentasItems.clear();
			
			this.listCuentas = this.ifzCtas.findLikeClaveNombreCuenta("", 0, this.sucursalesVisibles, 0);
			if (this.listCuentas != null && ! this.listCuentas.isEmpty()) {
				for (CtasBanco var : this.listCuentas)
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
   	
   	// ---------------------------------------------------------------------------
   	// PROPIEDADES
   	// ---------------------------------------------------------------------------

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
   		/*if (pojoObra == null)
   			return "";
   		
   		if (pojoObra.getNombre() == null)
   			return "";
   		
   		return pojoObra.getNombre();*/
	}
	
   	public void setObraNombre(String value) {}
	
	public String getFacturaId() {
		if (this.facturaId != null && this.facturaId > 0L)
			return this.facturaId.toString();
		return "";
	}
	
	public void setFacturaId(String value) {}

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
		return this.pojoComprobadoGto.getId() != null ? this.pojoComprobadoGto.getId() : 0;
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
		return this.pojoCajaChica.getNoCheque() != null ? this.pojoCajaChica.getNoCheque() : 0;
	}

	public void setNoCheque(Integer noCheque) {
		this.pojoCajaChica.setNoCheque(noCheque != 0 ? noCheque:null);
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

	public List<CtasBanco> getListCuentas() {
		return listCuentas;
	}

	public void setListCuentas(List<CtasBanco> listCuentas) {
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

	public String getNombreBeneficiario() {
		if (this.pojoBeneficiarios != null && this.pojoBeneficiarios.getId() > 0L)
			return this.pojoBeneficiarios.getId() + " - " + this.nombreBeneficiario;
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

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
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
		return totalMenosImpuestos;
	}

	public void setTotalMenosImpuestos(Double totalMenosImpuestos) {
		this.totalMenosImpuestos = totalMenosImpuestos;
	}

	public Double getTotalFacturasReportadas() {
		return totalFacturasReportadas;
	}

	public void setTotalFacturasReportadas(Double totalFacturasReportadas) {
		this.totalFacturasReportadas = totalFacturasReportadas;
	}

	public Double getImpto() {
		return impto;
	}

	public void setImpto(Double impto) {
		this.impto = impto;
	}

	public Double getSum_impto() {
		return sum_impto;
	}

	public void setSum_impto(Double sum_impto) {
		this.sum_impto = sum_impto;
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
		return subtotalMasImpuestos;
	}
	
	public void setSubtotalMasImpuestos(Double subtotalMasImpuestos) {
		this.subtotalMasImpuestos = subtotalMasImpuestos;
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
		return totImpuestos;
	}

	public void setTotImpuestos(Double totImpuestos) {
		this.totImpuestos = totImpuestos;
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
		return totRetenciones;
	}

	public void setTotRetenciones(Double totRetenciones) {
		this.totRetenciones = totRetenciones;
	}

	public Double getTotPago() {
		return totPago;
	}

	public void setTotPago(Double totPago) {
		this.totPago = totPago;
	}

	public Double getImptoRet() {
		return imptoRet;
	}

	public void setImptoRet(Double imptoRet) {
		this.imptoRet = imptoRet;
	}

	public Double getSum_imptoRet() {
		return sum_imptoRet;
	}

	public void setSum_imptoRet(Double sum_imptoRet) {
		this.sum_imptoRet = sum_imptoRet;
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

	public List<String> getTipoBusquedaObra() {
		return tipoBusquedaObra;
	}

	public void setTipoBusquedaObra(List<String> tipoBusquedaObra) {
		this.tipoBusquedaObra = tipoBusquedaObra;
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
		return facturado;
	}
	
	public void setFacturado(boolean facturado) {
		this.facturado = facturado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public SucursalExt getPojoSucursal() {
		return pojoSucursal;
	}

	public void setPojoSucursal(SucursalExt pojoSucursal) {
		this.pojoSucursal = pojoSucursal;
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
