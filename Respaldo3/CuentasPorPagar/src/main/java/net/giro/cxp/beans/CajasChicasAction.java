package net.giro.cxp.beans;

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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.*;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.Utilerias;
import net.giro.plataforma.beans.ConGrupoValores; // import cde.cont.admon.datos.ConGrupoValores;
import net.giro.plataforma.beans.ConValores; // import cde.cont.admon.datos.ConValores;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem; // import cde.cont.admon.logica.ConValoresFacadeRemote;
import net.giro.plataforma.ubicacion.dao.ColoniaDAO;
import net.giro.plataforma.ubicacion.dao.LocalidadDAO;
import net.giro.cxp.logica.GastosImpuestoRem;
import net.giro.cxp.logica.PagosGastosDetImpuestosRem;
import net.giro.cxp.logica.PagosGastosRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.cxp.beans.FacturaConcepto;
import net.giro.cxp.beans.FacturaImpuestos;
import net.giro.cxp.beans.GastosImpuestoExt;
import net.giro.cxp.beans.PagosGastos;
import net.giro.cxp.beans.PagosGastosDetExt;
import net.giro.cxp.beans.PagosGastosDetImpuestosExt;
import net.giro.cxp.beans.PagosGastosExt;
import net.giro.cxp.beans.PersonaExt;
import net.giro.cxp.beans.TiposGastoCXP;
import net.giro.tyg.admon.CuentaBancaria;
import net.giro.tyg.logica.CuentasBancariasRem;
import net.giro.cxp.logica.PagosGastosDetRem;
import net.giro.navegador.LoginManager;

public class CajasChicasAction implements Serializable {
	private static Logger log = Logger.getLogger(CajasChicasAction.class);
	private static final long serialVersionUID = 1L;
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
   	private HttpSession httpSession;
	// ----------------------------------------------------------------------------
	private PagosGastosRem ifzCajaChica; 
	private PagosGastosDetRem ifzCajaChicaDet;
	private PagosGastosDetImpuestosRem ifzDesgloImpto;
	private ConGrupoValoresRem ifzGrupoValores;
	private ConValoresRem ifzConValores;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	private ReportesRem	ifzReportes;
	private GastosImpuestoRem ifzGastoImpuesto;
	// ----------------------------------------------------------------------------
	private List<PagosGastos> listCajasChicas;
	private long idCajaChica;
	private PagosGastosExt pojoCajaChica;
	private PagosGastosDetExt pojoCajaChicaDetalle;
	// ----------------------------------------------------------------------------
	private PagosGastos pojoCajaChicaCuenta;
	// ----------------------------------------------------------------------------
	private List<Persona> listBeneficiarios;
	private String beneficiario;
	private String beneficiarioRfcReferencia;
	// ----------------------------------------------------------------------------
	private String razonSocial;
	private String tipoRazonSocial;
	// ----------------------------------------------------------------------------
	private int tipoCajaChica;
	private String referenciaOperacion;
	private double totalComprobacion;
	private double totalComprobaciones;
	private boolean vobo;
	private boolean autorizado;
	// Multiconceptos -------------------------------------------------------------
	private List<PagosGastosDetExt> listComprobacionFacturaConceptos;
	private double totalFacturaConceptos;
	private int paginacionFacturaConceptos;
	private int indexFacturaConcepto;
	private boolean agruparConceptos;
	private HashMap<String, List<PagosGastosDetImpuestosExt>> mapConceptoImpuestos;
	private boolean seleccionarTodo;
	// ----------------------------------------------------------------------------
	private boolean cargaComprobaciones;
	private FacturaImpuestos pojoComprobacion;
	private List<FacturaImpuestos> listComprobaciones;
	private List<FacturaImpuestos> listComprobacionesEliminadas;
	private List<PagosGastosDetImpuestosExt> listDesgloseImpuestos;
	private List<PagosGastosDetImpuestosExt> listImpuestosEliminadas;
	private int paginacionComprobaciones;
	private boolean existeRetencion;
	private long idEgresoPrevio;
	private int indexComprobacionEliminar;
	// Carga XML ------------------------------------------------------------------
	private FtpRem ifzFtp;
	private byte[] fileSrc;
	private String fileName;
	private String ftpDigitalizacionRuta;
	private String prefijoFacturas;
	// Beneficiarios --------------------------------------------------------------
	private PersonaRem ifzPersonas;
	private ConGrupoValores grupoPersonas;
	// Sucursales -----------------------------------------------------------------
	private SucursalesRem ifzSucursal;
	private List<Sucursal> listSucursales;	
	private List<SelectItem> listSucursalesItems;
	// Formas de Pago -------------------------------------------------------------
	private ConGrupoValores grupoFormasPagos; 
	private List<ConValores> listFormasPagos;
	private List<SelectItem> listFormasPagosItems;
	private long idFormaPago;
	// Cuentas Bancarias
	// ----------------------------------------------------------------------------
	private CuentasBancariasRem ifzCuentasBancarias;
	private List<CuentaBancaria> listCuentasBancarias;
	private List<SelectItem> listCuentasBancariasItems;
	// Busqueda Principal ---------------------------------------------------------
	private List<SelectItem> tiposBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
   	private int numPagina;
	// Busqueda CONCEPTOS ---------------------------------------------------------
	private ConGrupoValores grupoEgresos;
	private List<ConValores> listConceptoGasto;
	private ConValores pojoConcepto;
	private List<SelectItem> tiposBusquedaConceptos;
	private String campoBusquedaConceptos;
	private String valorBusquedaConceptos;
	private int pagBusquedaConceptos;
	// Busqueda Obras -------------------------------------------------------------
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> tiposBusquedaObras;
	private String campoBusquedaObra;
	private String valorBusquedaObra;
	private int paginacionObras;
	public boolean obrasAdministrativas;
	// Busqueda Retenciones -------------------------------------------------------
   	private ConGrupoValores grupoImpuestos;
	private List<ConValores> listBusquedaRetenciones;
	private ConValores pojoBusquedaRetenciones;
	private List<SelectItem> tiposBusquedaRetenciones;
	private String campoBusquedaRetenciones;
	private String valorBusquedaRetenciones;
	private int pagBusquedaRetenciones;
	// CONTROL --------------------------------------------------------------------
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// EMPLEADO-USUARIO -----------------------------------------------------------
	private EmpleadoRem ifzEmpleado;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalSugerida;
	// PERFILES -------------------------------------------------------------------
	private boolean perfilEgresos;
	private boolean perfilPermiteEditar;
	private boolean perfilRequiereObra;
	private boolean PERFIL_CAJA_CHICA_ABONOS;
	private boolean PERFIL_CAJA_CHICA_DIVIDIR;
	private boolean PERFIL_CAJA_CHICA_VOBO;
	private boolean PERFIL_CAJA_CHICA_AUTORIZACION;
	private double PERFIL_CAJA_CHICA_LIMITE;
	private boolean usarAsignacionCuentaBancaria;
	// DEBUG ----------------------------------------------------------------------
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public CajasChicasAction() {
		InitialContext ctx = null;
		ValueExpression valExp = null;
		FacesContext facesContext = null;
		Application app = null;
		String valPerfil = "";
		
		try {
			facesContext = FacesContext.getCurrentInstance();
	        app = facesContext.getApplication();

			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(facesContext.getELContext());
	        this.httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
	        
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(facesContext.getELContext());
			
			this.ftpDigitalizacionRuta = "/var/cargas/cxp/";
			if (this.entornoProperties.containsKey("ftp.digitalizacion.ruta.cxp") && this.entornoProperties.getString("ftp.digitalizacion.ruta.cxp") != null)
				this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta.cxp");
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : facesContext.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// EVALUACION DE PERFILES
			valPerfil = this.loginManager.getAutentificacion().getPerfil("REQUIERE_OBRA");
			this.perfilRequiereObra = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_EDITAR");
			this.perfilPermiteEditar = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
	   		ctx = new InitialContext();
	   		this.ifzCajaChica = (PagosGastosRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
			this.ifzCajaChicaDet = (PagosGastosDetRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzDesgloImpto = (PagosGastosDetImpuestosRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
	   		this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
	   		this.ifzGrupoValores = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
	   		this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
	   		this.ifzSucursal = (SucursalesRem) ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
	   		this.ifzCuentasBancarias = (CuentasBancariasRem) ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
	   		this.ifzEmpleado = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
	   		this.ifzPersonas = (PersonaRem) ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
	   		this.ifzFtp = (FtpRem) ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzLocalidad = (LocalidadDAO) ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			this.ifzColonia = (ColoniaDAO) ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");
	   		this.ifzGastoImpuesto = (GastosImpuestoRem) ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
	   		
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCajaChicaDet.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGrupoValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCuentasBancarias.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			
			comprobarUsuario();
			
			// IMPUESTOS
			this.grupoImpuestos = this.ifzGrupoValores.findByName("SYS_IMPTOS");
			if (this.grupoImpuestos == null || this.grupoImpuestos.getId() <= 0L)
				controlLog("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
			
			// FORMAS DE PAGO
			this.grupoFormasPagos = this.ifzGrupoValores.findByName("SYS_FORMAS_PAGO");
			if (this.grupoFormasPagos == null || this.grupoFormasPagos.getId() <= 0L)
				controlLog("No se encontro encontro el grupo SYS_FORMAS_PAGO en con_grupo_valores");

			// RELACIONES PERSONAS
			this.grupoPersonas = this.ifzGrupoValores.findById(4L); // valGpoPersonas
			if (this.grupoPersonas == null || this.grupoPersonas.getId() <= 0L)
				controlLog("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");

			// MOVIMIENTOS GASTOS
			this.grupoEgresos = this.ifzGrupoValores.findByName("SYS_MOVGTOS");
			if (this.grupoEgresos == null || this.grupoEgresos.getId() <= 0L)
				controlLog("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");

			// Busqueda principal
			// ----------------------------------------------------------------------
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
	   		
	   		// BUSQUEDA CONCEPTOS
	   		this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
	   		this.tiposBusquedaConceptos.add(new SelectItem("descripcion", "Descripcion"));
	   		this.tiposBusquedaConceptos.add(new SelectItem("id", "ID"));
	   		nuevaBusquedaConceptos();

			// Busqueda Obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("*", "Concidencia"));
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
			
			// Busqueda Retenciones
			this.tiposBusquedaRetenciones = new ArrayList<SelectItem>();
			this.tiposBusquedaRetenciones.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaRetenciones.add(new SelectItem("valor", "Cuenta Contable"));
			this.tiposBusquedaRetenciones.add(new SelectItem("id", "ID"));
			nuevaBusquedaRetenciones();
			
			this.prefijoFacturas = "CXP-CC-"; 
			this.listSucursalesItems = new ArrayList<SelectItem>();
			this.listFormasPagosItems = new ArrayList<SelectItem>();
			this.listCuentasBancariasItems = new ArrayList<SelectItem>();
			getPerfiles();
			cargarSucursales();
			cargarFormasDePago();
			cargarCuentasBancarias();
			control();
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
		}
  	}
   	
   	public void buscar() {
   		long idOwner = 0L;
   		String valor = "";
   		String orderBy = "";
   		
   		try {
   			control();
   			if (this.campoBusqueda == null || "".equals(this.campoBusqueda.trim()))
   				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
   			this.valorBusqueda = (this.valorBusqueda != null ? this.valorBusqueda.trim() : "");

			orderBy = "fecha desc, consecutivo desc, id desc";
			valor = this.valorBusqueda.trim().replace(" ", "%");
   			if ("fecha".equals(this.campoBusqueda)) {
   				orderBy = "fecha desc, id desc";
   				valor = "";
   			} 
   			
   			if (! "fecha".equals(this.campoBusqueda) && (valor == null || "".equals(valor.trim()))) 
   				idOwner = (! "ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario()) ? this.loginManager.getInfoSesion().getAcceso().getUsuario().getId() : 0L);
   			
   			valor = (! "*".equals(valor) ? valor : "");
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCajasChicas = this.ifzCajaChica.findCajaChicaLikeProperty(this.campoBusqueda, ("fecha".equals(this.campoBusqueda) ? this.fechaBusqueda : valor), "", true, orderBy, idOwner, 0);
			if (this.listCajasChicas == null || this.listCajasChicas.isEmpty())
				control(13, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Registros de Gastos", e);
		}
   	}
   	
   	public void nuevo() {
		try {
   			control();
			this.beneficiario = "";
   			this.idCajaChica = 0L;
   			this.tipoCajaChica = 0;
			this.referenciaOperacion = "";
			this.beneficiarioRfcReferencia = "";
			this.cargaComprobaciones = false;
			this.paginacionComprobaciones = 1;
   			this.listComprobaciones = new ArrayList<FacturaImpuestos>();
   			this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>();
			if (this.pojoEmpleadoUsuario != null && this.pojoEmpleadoUsuario.getId() != null && this.pojoEmpleadoUsuario.getId() > 0L)
				this.beneficiario = this.pojoEmpleadoUsuario.getPersona().getId() + " - " + this.pojoEmpleadoUsuario.getNombre();
			this.beneficiario = (this.beneficiario != null ? this.beneficiario : "");
   			
			this.pojoCajaChica = new PagosGastosExt();
			this.pojoCajaChica.setTipoBeneficiario("P");
			asignarSucursal(this.idSucursalSugerida);
			setBeneficiario(this.beneficiario);
			
			this.PERFIL_CAJA_CHICA_VOBO = false;
			this.PERFIL_CAJA_CHICA_AUTORIZACION = false;
			actualizarPerfil("DIVIDIR_CAJA");
			if (this.PERFIL_CAJA_CHICA_DIVIDIR) {
				actualizarPerfil("CAJA_CHICA_VOBO");
				actualizarPerfil("CAJA_CHICA_AUTORIZACION");
			}
			this.usarAsignacionCuentaBancaria = this.PERFIL_CAJA_CHICA_ABONOS;
		} catch (Exception e) {
			control("Ocurrio un problema al inicializar una nueva Caja Chica", e);
		}
	}

   	public void editar() {
   		try {
   			control();
   			this.pojoCajaChica = this.ifzCajaChica.findExtById(this.idCajaChica);
   			if (this.pojoCajaChica == null || this.pojoCajaChica.getId() == null || this.pojoCajaChica.getId() <= 0L) {
   				control(-1, "Ocurrio un problema al intentar recuperar la Caja Chica indicada");
   				return;
   			}

			cargarSucursales();
			cargarFormasDePago();
			cargarCuentasBancarias();

   			this.tipoCajaChica = (this.pojoCajaChica.getArea() != null && "Obra".equals(this.pojoCajaChica.getArea()) ? 0 : 1); 
			this.referenciaOperacion = ""; 
   			if ("C".equals(this.pojoCajaChica.getOperacion())) 
   				this.referenciaOperacion = (this.pojoCajaChica.getNoCheque() != null ? this.pojoCajaChica.getNoCheque().toString() : ""); 
			else if ("T".equals(this.pojoCajaChica.getOperacion())) 
				this.referenciaOperacion = (this.pojoCajaChica.getFolioAutorizacion() != null ? this.pojoCajaChica.getFolioAutorizacion() : ""); 
   			
			this.beneficiario = ""; 
			this.beneficiarioRfcReferencia = ""; 
			if (this.pojoCajaChica != null && this.pojoCajaChica.getIdBeneficiario() != null && this.pojoCajaChica.getIdBeneficiario().getId() > 0L) 
				this.beneficiario = this.pojoCajaChica.getIdBeneficiario().getId() + " - " + this.pojoCajaChica.getBeneficiario(); 

			this.cargaComprobaciones = true;
			this.paginacionComprobaciones = 1; 
			this.listComprobaciones = new ArrayList<FacturaImpuestos>(); 
			this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>(); 
			this.totalComprobaciones = this.pojoCajaChica.getMonto().doubleValue();
			this.usarAsignacionCuentaBancaria = this.PERFIL_CAJA_CHICA_ABONOS;
   		} catch (Exception e) {
			control("Ocurrio un problema al intentar editar la Caja Chica indicado", e);
   		} 
   	}
   	
   	public void guardar() {
		boolean registroNuevo = false;
		Respuesta resp = null;
		PagosGastos cajaChica = null;
		// -----------------------------
		boolean borrarXML = false;
		Long facturaId = 0L;
		String fileName = "";
		
		try {
			control();
			controlLog("Guardando Caja Chica. Validaciones ... ");
			if (! validaciones()) {
				controlLog("Guardando Caja Chica. Validaciones ... FAIL");
				return;
			}

			controlLog("Guardando Caja Chica. Validaciones ... OK");
			if (this.pojoCajaChica.getId() == null || this.pojoCajaChica.getId() <= 0L)
				registroNuevo = true;

			this.pojoCajaChica.setConcepto("Reposición de Caja chica");
			this.pojoCajaChica.setMonto(this.totalComprobaciones);
			this.pojoCajaChica.setTipo("C");//comprobacion caja chica
			this.pojoCajaChica.setEstatus("G");//generado
			this.pojoCajaChica.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			if (registroNuevo) {
				this.pojoCajaChica.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoCajaChica.setFechaCreacion(Calendar.getInstance().getTime());
			}
			this.pojoCajaChica.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoCajaChica.setFechaModificacion(Calendar.getInstance().getTime());

			controlLog("Guardando Caja Chica. Guardando ... ");
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			resp = this.ifzCajaChica.salvar(this.pojoCajaChica);
			if (resp.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al guardar la Caja Chica.\n" + resp.getErrores().getDescError());
				return;
			}
			
			this.pojoCajaChica = (PagosGastosExt) resp.getObjeto();
			this.idCajaChica = this.pojoCajaChica.getId();
			if (resp.getBody().getValor("entity") != null)
				cajaChica = (PagosGastos) resp.getBody().getValor("entity");
			if (cajaChica == null || cajaChica.getId() == null || cajaChica.getId() <= 0L)
				cajaChica = this.ifzCajaChica.findById(this.idCajaChica);
			controlLog("Guardando Caja Chica. Guardando ... OK");
			
			// Guardamos las comprobacion de la caja chica si corresponde
			if (! this.cargaComprobaciones) {
				controlLog("Guardando Caja Chica. Guardando comprobaciones ... ");
				this.ifzCajaChicaDet.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
				for (FacturaImpuestos comprobacion : this.listComprobaciones) {
					// Guardo detalle
					comprobacion.getPojoFactura().setIdPagosGastos(this.pojoCajaChica);
					if (comprobacion.getPojoFactura().getId() != null && comprobacion.getPojoFactura().getId() > 0L) 
						this.ifzCajaChicaDet.update(comprobacion.getPojoFactura());
					else 
						comprobacion.getPojoFactura().setId(this.ifzCajaChicaDet.save(comprobacion.getPojoFactura()));
					
					// Impuestos
					if (! comprobacion.getListImpuestos().isEmpty()) {
						for (PagosGastosDetImpuestosExt impuesto : comprobacion.getListImpuestos()) {
							impuesto.setIdPagosGastosDet(comprobacion.getPojoFactura());
							if (impuesto.getId() == null || impuesto.getId() <= 0L)
								impuesto.setId(this.ifzDesgloImpto.save(impuesto));
							else
								this.ifzDesgloImpto.update(impuesto);
						}
					}
					
					// Retenciones
					if (! comprobacion.getListRetenciones().isEmpty()) {
						for (PagosGastosDetImpuestosExt retencion : comprobacion.getListRetenciones()) {
							retencion.setIdPagosGastosDet(comprobacion.getPojoFactura());
							if (retencion.getId() == null || retencion.getId() <= 0L)
								retencion.setId(this.ifzDesgloImpto.save(retencion));
							else
								this.ifzDesgloImpto.update(retencion);
						}
					}
				}
				controlLog("Guardando Caja Chica. Guardando comprobaciones ... OK");
			}
			
			// Añadimos a listado
			controlLog("Guardando Caja Chica. Añadimos a listado ");
			if (this.listCajasChicas == null || this.listCajasChicas.isEmpty() || registroNuevo) {
				this.listCajasChicas = (this.listCajasChicas != null ? this.listCajasChicas : new ArrayList<PagosGastos>());
				this.listCajasChicas.add(0, cajaChica);
			} else {
				for (PagosGastos var : this.listCajasChicas) {
					if (cajaChica.getId().longValue() == var.getId().longValue()) {
						BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
						BeanUtils.copyProperties(var, cajaChica);
						break;
					}
				}
			}
			
			// Borramos facturas previamente eliminadas
			if (this.listComprobacionesEliminadas != null && ! this.listComprobacionesEliminadas.isEmpty()) {
				controlLog("Guardando Caja Chica. Borramos comprobaciones previamente eliminadas ... ");
				for (FacturaImpuestos var : this.listComprobacionesEliminadas) {
					if (var.getPojoFactura() == null || var.getPojoFactura().getId() == null || var.getPojoFactura().getId() <= 0L)
						continue;
					
					if (var.getPojoFactura().getIdCfdi() == null || var.getPojoFactura().getIdCfdi() <= 0L) {
						this.ifzCajaChicaDet.delete(var.getPojoFactura().getId());
						continue;
					}
						
					// borramos XML (documento cargado) si corresponde
					borrarXML = true;
					facturaId = var.getPojoFactura().getIdCfdi();
					for (FacturaImpuestos item : this.listComprobaciones) {
						if (item.getPojoFactura().getIdCfdi() != null && item.getPojoFactura().getIdCfdi() > 0L && item.getPojoFactura().getIdCfdi().longValue() == facturaId.longValue()) {
							borrarXML = false;
							break;
						}
					}

					if (! borrarXML) {
						this.ifzCajaChicaDet.delete(var.getPojoFactura().getId());
						continue;
					}
					
					// Lo elimino de la BD
					controlLog("Eliminando XML " + facturaId);
		    		this.ifzCajaChicaDet.eliminarFactura(facturaId);
					
					// Lo elimino fisicamente del servidor
					fileName = this.prefijoFacturas + var.getPojoFactura().getIdCfdi() + ".xml";
					if (! this.ifzFtp.delArchivo(this.ftpDigitalizacionRuta + fileName))
						log.warn("ERROR FTP - No se pudo Borrar la factura del servidor, ID: " + facturaId);
					
					// Borro detalle (factura)
					this.ifzCajaChicaDet.delete(var.getPojoFactura().getId());
					facturaId = 0L;
				}
				
				this.listComprobacionesEliminadas.clear();
				controlLog("Guardando Caja Chica. Borramos comprobaciones previamente eliminadas ... OK");
			}

			// Borramos impuestos de comprobaciones previamente eliminadas
			if (this.listImpuestosEliminadas != null && ! this.listImpuestosEliminadas.isEmpty()) {
				controlLog("Guardando Caja Chica. Borramos impuestos de comprobaciones previamente eliminados ... OK");
				for (FacturaImpuestos var : this.listComprobacionesEliminadas) {
					if (var.getPojoFactura().getIdCfdi() == null || var.getPojoFactura().getIdCfdi() <= 0L) {
						this.ifzCajaChicaDet.delete(var.getPojoFactura().getId());
						continue;
					}
						
					// borramos XML (documento cargado) si corresponde
					borrarXML = true;
					facturaId = var.getPojoFactura().getIdCfdi();
					for (FacturaImpuestos item : this.listComprobaciones) {
						if (item.getPojoFactura().getIdCfdi() != null && item.getPojoFactura().getIdCfdi() > 0L && item.getPojoFactura().getIdCfdi().longValue() == facturaId.longValue()) {
							borrarXML = false;
							break;
						}
					}

					if (! borrarXML)
						continue;
					
					// Lo elimino de la BD
					controlLog("Eliminando XML " + facturaId);
		    		this.ifzCajaChicaDet.eliminarFactura(facturaId);
					
					// Lo elimino fisicamente del servidor
					fileName = this.prefijoFacturas + var.getPojoFactura().getIdCfdi() + ".xml";
					if (! this.ifzFtp.delArchivo(this.ftpDigitalizacionRuta + fileName))
						log.warn("ERROR FTP - No se pudo Borrar la factura del servidor, ID: " + facturaId);
					
					// Borro detalle (factura)
					this.ifzCajaChicaDet.delete(var.getPojoFactura().getId());
					facturaId = 0L;
				}
				
				this.listImpuestosEliminadas.clear();
				controlLog("Guardando Caja Chica. Borramos impuestos de comprobaciones previamente eliminados ... OK");
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			mensajeTransaccion();
			controlLog("Guadado Caja Chica completo");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Caja Chica", e);
		}
   	}
   	
	public void cancelar() {
   		Respuesta respuesta = new Respuesta();
   		
		try {
			control();
			this.pojoCajaChica = this.ifzCajaChica.findExtById(this.idCajaChica);
   			if (this.pojoCajaChica == null || this.pojoCajaChica.getId() == null || this.pojoCajaChica.getId() <= 0L) {
   				control(-1, "Ocurrio un problema al intentar recuperar la Caja Chica indicado");
   				return;
   			}
   			
   			controlLog("Cancelando CC" + this.idCajaChica + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzCajaChica.cancelacion(this.pojoCajaChica, Calendar.getInstance().getTime());
			if (respuesta.getResultado() != 0) {
				control(-1, respuesta.getRespuesta());
	   			controlLog("Cancelando RE" + this.idCajaChica + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getId() + " ... ERROR");
				return;
			}
			
			for (PagosGastos registroEgreso : this.listCajasChicas) {
				if (registroEgreso.getId().longValue() == this.pojoCajaChica.getId().longValue()) {
					registroEgreso.setEstatus("X");
					break;
				}
			}
   			
   			controlLog("Cancelando CC" + this.idCajaChica + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getId() + " ... OK");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Cancelar la Caja Chica indicado.", e);
		}
	}

	public void evaluaCancelar() {
   		try {
   			control();
			this.pojoCajaChica = this.ifzCajaChica.findExtById(this.idCajaChica);
   			if (this.pojoCajaChica == null || this.pojoCajaChica.getId() == null || this.pojoCajaChica.getId() <= 0L) {
   				control(-1, "Ocurrio un problema al evaluar los permisos para Cancelar Cajas Chicas. No se pudo recuperar");
   				return;
   			}
			
			if (! this.perfilPermiteEditar && ! validaRequest("DETEDIT", "1")) 
	   			control(3, "No tiene permitido Cancelar Cajas Chicas");
		} catch (Exception e) {
			control("Ocurrio un problema al evaluar los permisos para Cancelar la Caja Chica", e);
		}
   	}

	public void reporte() {
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		String nombreDocumento = "";
		String formatoDocumento = "";
		byte[] contenidoDocumento = null;
		
		try {
			control();
   			this.pojoCajaChica = this.ifzCajaChica.findExtById(this.idCajaChica);
   			if (this.pojoCajaChica == null || this.pojoCajaChica.getId() == null || this.pojoCajaChica.getId() <= 0L) {
   				control(-1, "Ocurrio un problema al intentar recuperar la Caja Chica indicada");
   				return;
   			}
   			
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("pagosGastosId", this.pojoCajaChica.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", this.entornoProperties.getString("REPORTE_CAJA_CHICA_ID"));
			params.put("ftp_host", this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto", this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());
			
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO: " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}

			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "CC-" + this.pojoCajaChica.getConsecutivo() + "_" + this.pojoCajaChica.getId() + "." + formatoDocumento;
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				control(-1, "Ocurrio un problema al intentar imprimir la Caja Chica.\nNo se puede descargar");
				return;
			}
			 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la Caja Chica", e);
		}
	}

	public void mensajeTransaccion() {
		try {
			// Enviamos mensaje a cola de transacciones
			controlLog("Enviando Transaccion ... ");
			this.ifzCajaChica.contabilizador(this.idCajaChica);
			controlLog("Transaccion enviada");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar lanzar el Evento para Contabilizar", e);
		}
	}

	public void cargarComprobantes() {
   		List<PagosGastosDetImpuestosExt> listImpuestos = null;
   		List<PagosGastosDetImpuestosExt> impuestos = null;
   		List<PagosGastosDetImpuestosExt> retenciones = null;
   		List<PagosGastosDetExt> listDetalles = null; 
		PersonaExt proveedor = null;
		
		try {
			control();
			if (! this.cargaComprobaciones)
				return;
			
			if (this.idCajaChica != this.pojoCajaChica.getId().longValue()) 
	   			this.pojoCajaChica = this.ifzCajaChica.findExtById(this.idCajaChica);
   			if (this.pojoCajaChica == null || this.pojoCajaChica.getId() == null || this.pojoCajaChica.getId() <= 0L) {
   				control(-1, "Ocurrio un problema al intentar recuperar la Caja Chica indicada");
   				return;
   			}
   			
			this.paginacionComprobaciones = 1; 
			this.listComprobaciones = new ArrayList<FacturaImpuestos>(); 
			this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>(); 
			if (this.pojoCajaChica.getIdBeneficiario() != null && this.pojoCajaChica.getIdBeneficiario().getId() > 0L) {
				proveedor = this.pojoCajaChica.getIdBeneficiario();
				if ((proveedor == null || proveedor.getId() <= 0L) && this.pojoCajaChica.getIdBeneficiario() != null && this.pojoCajaChica.getIdBeneficiario().getId() > 0L)
					proveedor = getPersonaNegocio(this.pojoCajaChica.getIdBeneficiario().getId(), this.pojoCajaChica.getTipoBeneficiario()); 
			}
			
			listDetalles = this.ifzCajaChicaDet.findExtAll(this.idCajaChica); 
			if (listDetalles != null && ! listDetalles.isEmpty()) {
	   			for (PagosGastosDetExt detalle : listDetalles) {
					if ((detalle.getIdProveedor() == null || detalle.getIdProveedor().getId() <= 0L) && (proveedor != null && proveedor.getId() > 0L)) {
						detalle.setIdProveedor(proveedor);
						detalle.setFacturaRfc(proveedor.getRfc());
						detalle.setNombreProveedor(proveedor.getNombre());
						detalle.setTipoPersonaProveedor(proveedor.getTipoPersona().longValue() == 2L ? "N" : "P"); 
					}
					
					try {
						impuestos = new ArrayList<PagosGastosDetImpuestosExt>();
						retenciones = new ArrayList<PagosGastosDetImpuestosExt>();
						listImpuestos = this.ifzDesgloImpto.findExtAll(detalle.getId());
						if (listImpuestos != null && ! listImpuestos.isEmpty()) {
							for (PagosGastosDetImpuestosExt impuesto : listImpuestos) {
								if ("AC".equals(impuesto.getIdImpuesto().getTipoCuenta())) 
									retenciones.add(impuesto);
								else if ("DE".equals(impuesto.getIdImpuesto().getTipoCuenta())) 
									impuestos.add(impuesto);
							}
						}
		
		   				this.listComprobaciones.add(new FacturaImpuestos(detalle, impuestos, retenciones));
					} catch (Exception e) {
						log.error("Error al obtener la lista de impuestos de la factura " + detalle.getReferencia() + " (" + detalle.getId() + ")", e);
					}
	   			}
			}
			
			actualizaTotalComprobado();
			indexarComprobaciones();
			this.cargaComprobaciones = false;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar lo Comprobado de la Caja Chica", e);
		}
	}
	
	private boolean validaciones() {
		PersonaExt proveedor = null;
		long idAuxiliar = 0L;
		boolean registroNuevo = false;
		
		// Inicializaciones
		if (this.pojoCajaChica.getId() == null || this.pojoCajaChica.getId() <= 0L)
			registroNuevo = true;
		if (this.pojoCajaChica.getOperacion() == null)
			this.pojoCajaChica.setOperacion("");
		this.pojoCajaChica.setNoCheque(0);
		this.pojoCajaChica.setFolioAutorizacion("");
		if (this.pojoCajaChica.getIdObra() == null)
			this.pojoCajaChica.setIdObra(new Obra());
		if (this.pojoCajaChica.getIdOrdenCompra() <= 0L)
			this.pojoCajaChica.setIdOrdenCompra(0L);

		if (this.perfilRequiereObra && this.tipoCajaChica == 0 && this.pojoCajaChica.getIdObra() == null) {
			control(19, "Debe selecccionar una Obra");
			return false;
		}
		
		if (this.pojoCajaChica.getIdSucursal() == null || this.pojoCajaChica.getIdSucursal().getId() <= 0L) {
			control(-1, "Debe seleccionar una Sucursal");
			return false;
		}
		
		if (this.PERFIL_CAJA_CHICA_ABONOS && this.usarAsignacionCuentaBancaria) {
			if (this.pojoCajaChica.getOperacion() == null || "".equals(this.pojoCajaChica.getOperacion().trim())) {
				control(-1, "Debe seleccionar una Operacion");
				return false;
			}
	
			if (this.pojoCajaChica.getIdCuentaOrigen() == null || this.pojoCajaChica.getIdCuentaOrigen().getId() <= 0L) {
				control(-1, "Debe seleccionar una Cuenta");
				return false;
			}
			
			if (this.referenciaOperacion == null || "".equals(this.referenciaOperacion.trim())) {
				control(-1, "Debe indicar una Referencia");
				return false;
			}
			
			if ("C".equals(this.pojoCajaChica.getOperacion().trim()) && ! NumberUtils.isNumber(this.referenciaOperacion)) {
				control(-1, "Referencia no valida para Cheque, debe ser de solo Numeros");
				return false;
			}

			if ("C".equals(this.pojoCajaChica.getOperacion())) 
				this.pojoCajaChica.setNoCheque(Integer.parseInt(this.referenciaOperacion.trim()));
			else  
				this.pojoCajaChica.setFolioAutorizacion(this.referenciaOperacion.trim());
		}

		// BENEFICIARIO
		// -----------------------------------------------------------------------------------------------------
		if (this.pojoCajaChica.getIdBeneficiario() == null || this.pojoCajaChica.getIdBeneficiario().getId() <= 0L) {
			idAuxiliar = validateToID(this.beneficiario, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			proveedor = this.ifzCajaChica.findPersonaById(idAuxiliar, this.pojoCajaChica.getTipoBeneficiario());
			if (proveedor == null || proveedor.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Beneficiario seleccionado");
				return false;
			}
			
			if (this.pojoCajaChica.getIdBeneficiario() == null || this.pojoCajaChica.getIdBeneficiario().getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Beneficiario seleccionado");
				return false;
			}
			
			this.pojoCajaChica.setIdBeneficiario(proveedor);
			this.pojoCajaChica.setBeneficiario(proveedor.getNombre());
		}

		this.pojoCajaChica.setArea("Oficina");
		if (this.perfilRequiereObra)
			this.pojoCajaChica.setArea((this.tipoCajaChica == 0 ? "Obra" : "Oficina"));
		
		// Comprobamos su tiene visto bueno
		if (this.PERFIL_CAJA_CHICA_VOBO && this.vobo && this.pojoCajaChica.getVoboPor() <= 0L) {
			this.pojoCajaChica.setVoboPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			this.pojoCajaChica.setFechaVobo(Calendar.getInstance().getTime());
		}
		
		// Comprobamos si esta autorizado
		if (this.PERFIL_CAJA_CHICA_AUTORIZACION && this.autorizado && this.pojoCajaChica.getAutorizadoPor() <= 0L) {
			this.pojoCajaChica.setAutorizadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			this.pojoCajaChica.setFechaAutorizado(Calendar.getInstance().getTime());
		}
		
		// Comprobamos Numero de Caja Chica
		/*if (this.pojoCajaChica.getConsecutivo() <= 0) {
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			this.pojoCajaChica.setConsecutivo(this.ifzCajaChica.generarNumCajaChicaByBeneficiario(this.pojoCajaChica.getIdBeneficiario().getId(), "G"));
			if (this.pojoCajaChica.getConsecutivo() <= 0) {
				control(-1, "No se pudo determinar el Numero de Caja Chica.\nReasigne al Beneficiario");
				return false;
			}
		}*/
		
		this.pojoCajaChica.setMontoLimite(0D);
		if (this.perfilRequiereObra)
			this.pojoCajaChica.setMontoLimite(this.PERFIL_CAJA_CHICA_LIMITE);

		if (this.listComprobaciones == null || this.listComprobaciones.isEmpty()) {
			if (registroNuevo || (! registroNuevo && ! this.cargaComprobaciones)) {
				control(2, "Debe indicar al menos una factura");
				return false;
			}
		}
		
		if (! validaSaldoCuentaPropios())
			return false;
		
		return true;
	}

	private long validateToID(String target, String pattern) {
		Pattern pat = null;
		Matcher match = null;

		try {
			pat = Pattern.compile(pattern);
			match = pat.matcher(target);
			if (match.find())
				return Long.valueOf(match.group(1));
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar el patron para recuperar el ID", e);
		}
		
		return 0;
	}

	private String validateToDesc(String target, String pattern) {
		Pattern pat = null;
		Matcher match = null;

		try {
			pat = Pattern.compile(pattern);
			match = pat.matcher(target);
			if (match.find())
				return match.group(2);
		} catch (Exception e) {
			log.error("Ocurrio un problema al validar el patron para recuperar la Descripcion", e);
		}
		
		return target;
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
			
			// Recuperamos empleado
			this.pojoEmpleadoUsuario = this.ifzEmpleado.findByIdExt(idEmpleado);
			if (this.pojoEmpleadoUsuario == null || this.pojoEmpleadoUsuario.getId() == null || this.pojoEmpleadoUsuario.getId() <= 0L){
				controlLog("Usuario sin Empleado asociado o No se encontro recuperar el Empleado asociado");
				return false;
			}
			
			// Recuperamos SUCURSAL base del Empleado
			this.idSucursalSugerida = this.pojoEmpleadoUsuario.getSucursal().getId();
		} catch (Exception e) {
			log.error("Ocurrio un problema en CuentasPorPagar.CajasChicasAction.comprobarUsuario()", e);
			return false;
		}
		
		return true;
	}

	private void indexarComprobaciones() {
		if (this.listComprobaciones == null || this.listComprobaciones.isEmpty())
			return;
		
		for (FacturaImpuestos comprobacion : this.listComprobaciones)
			comprobacion.setIndex(this.listComprobaciones.indexOf(comprobacion));
	}

	private boolean validaSaldoCuentaPropios() {
		try {
			control();
			if (this.totalComprobaciones == 0 && this.listComprobaciones.isEmpty()) {
				control(12, "Monto invalido. Saldo insuficiente");
				return false;
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
			return false;
		}
		
		return true;
	}

   	private void cargarSucursales() {
   		try {
   			control();
			this.listSucursalesItems = new ArrayList<SelectItem>();
			this.ifzSucursal.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSucursales = this.ifzSucursal.findAll(); 
			if (this.listSucursales == null || this.listSucursales.isEmpty()) {
				control(-1, "Ocurrio un problema al consultar las Sucursales");
				return;
			}
			
			for (Sucursal var : this.listSucursales)
				this.listSucursalesItems.add(new SelectItem(var.getId(), var.getSucursal()));
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Sucursales", e);
		}
   	}
   	
   	private void cargarFormasDePago() {
		try {
   			control();
			this.listFormasPagosItems = new ArrayList<SelectItem>();
			if (this.grupoFormasPagos == null || this.grupoFormasPagos.getId() <= 0L)
				return;
			
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listFormasPagos = this.ifzConValores.findAll(this.grupoFormasPagos);
			if (this.listFormasPagos == null || this.listFormasPagos.isEmpty()) {
				control(-1, "Ocurrio un problema al consultar las Formas de Pago");
				return;
			}
			
			for (ConValores formaPago : this.listFormasPagos)
				this.listFormasPagosItems.add(new SelectItem(formaPago.getValor(), formaPago.getDescripcion()));
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Formas de Pago", e);
		}
	}

   	private void cargarCuentasBancarias() {
   		try {
   			control();
			this.listCuentasBancariasItems = new ArrayList<SelectItem>();
			this.ifzCuentasBancarias.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCuentasBancarias = this.ifzCuentasBancarias.findAll();
			if (this.listCuentasBancarias == null || this.listCuentasBancarias.isEmpty()) {
				control(-1, "Ocurrio un problema al consultar las Cuentas Bancarias");
				return;
			}
			
			for (CuentaBancaria var : this.listCuentasBancarias)
				this.listCuentasBancariasItems.add(new SelectItem(var.getId(), var.getInstitucionBancaria().getNombreCorto() + " - " + var.getNumeroDeCuenta()));
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Cuentas Bancarias", e);
		}
   	}
   	
   	private void asignarSucursal(long idSucursal) {
   		if (idSucursal <= 0L)
   			return;
   		if (this.pojoCajaChica == null)
   			return;
   		
		if (this.listSucursales == null || this.listSucursales.isEmpty())
			cargarSucursales();

		for (Sucursal sucursal : this.listSucursales) {
			if (idSucursal == sucursal.getId()) {
				this.pojoCajaChica.setIdSucursal(sucursal);
				break;
			}
		}
   	}
   	
	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la accion solicitada";
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error(this.getClass().getCanonicalName() + " :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + "\n" + this.tipoMensaje + " - " + mensaje, throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info(this.getClass().getCanonicalName() + " :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + "\n" + mensaje);
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
			this.usarAsignacionCuentaBancaria = this.PERFIL_CAJA_CHICA_ABONOS;
			
			// REQUIERE OBRA
			valPerfil = this.loginManager.getAutentificacion().getPerfil("REQUIERE_OBRA").trim();
			this.perfilRequiereObra = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);//(valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			// LIMITE CAJA CHICA
			valPerfil = this.loginManager.getAutentificacion().getPerfil("LIMITE_CAJA_CHICA").trim();
			this.PERFIL_CAJA_CHICA_LIMITE = (valPerfil != null && ! "".equals(valPerfil)) ? Double.valueOf(valPerfil.trim()) : 0;
			
			// VISTO BUENO
			valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_VOBO").trim();
			this.PERFIL_CAJA_CHICA_VOBO = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);//(valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			// AUTORIZACION
			valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_AUTORIZACION").trim();
			this.PERFIL_CAJA_CHICA_AUTORIZACION = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);//(valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			// DIVICION DE CAJA: VoBo y Autorizacion
			valPerfil = this.loginManager.getAutentificacion().getPerfil("DIVIDIR_CAJA").trim();
			this.PERFIL_CAJA_CHICA_DIVIDIR = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);//(valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
			
			if (! this.PERFIL_CAJA_CHICA_DIVIDIR) {
				this.PERFIL_CAJA_CHICA_VOBO = false;
				this.PERFIL_CAJA_CHICA_AUTORIZACION = false;
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
					this.perfilRequiereObra = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);//(valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
				break;
					
				case "LIMITE_CAJA_CHICA" : 
					valPerfil = this.loginManager.getAutentificacion().getPerfil("LIMITE_CAJA_CHICA").trim();
					this.PERFIL_CAJA_CHICA_LIMITE = (valPerfil != null && ! "".equals(valPerfil)) ? Double.valueOf(valPerfil.trim()) : 0;
				break;
					
				case "CAJA_CHICA_VOBO" : 
					valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_VOBO").trim();
					this.PERFIL_CAJA_CHICA_VOBO = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);//(valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
				break;
				
				case "CAJA_CHICA_AUTORIZACION" : 
					valPerfil = this.loginManager.getAutentificacion().getPerfil("CAJA_CHICA_AUTORIZACION").trim();
					this.PERFIL_CAJA_CHICA_AUTORIZACION = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);//(valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
				break;
				
				case "DIVIDIR_CAJA" : 
					valPerfil = this.loginManager.getAutentificacion().getPerfil("DIVIDIR_CAJA").trim();
					this.PERFIL_CAJA_CHICA_DIVIDIR = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);//(valPerfil != null && ! "".equals(valPerfil)) ? valPerfil.trim() : "";
					
					if (! this.PERFIL_CAJA_CHICA_DIVIDIR) {
						this.PERFIL_CAJA_CHICA_VOBO = false;
						this.PERFIL_CAJA_CHICA_AUTORIZACION = false;
					}
				break;
			}
		} catch (Exception e) {
			log.error("Erro al recuperar el perfil [" + perfil + "] en CuentasPorPagar.CajaChicaAction.actualizarPerfil" , e);
		}
	}

	// ------------------------------------------------------------------------------------------
	// ASIGNACION CUENTA BANCARIA
	// ------------------------------------------------------------------------------------------
	
	public void nuevaAsignacionOperacion() {
   		try {
   			control();
   			if (this.pojoCajaChicaCuenta == null || this.pojoCajaChicaCuenta.getId() == null || this.pojoCajaChicaCuenta.getId() <= 0L) {
   	   			this.pojoCajaChicaCuenta = this.ifzCajaChica.findById(this.idCajaChica);
   	   			if (this.pojoCajaChicaCuenta == null || this.pojoCajaChicaCuenta.getId() == null || this.pojoCajaChicaCuenta.getId() <= 0L) {
   	   				control(-1, "Ocurrio un problema al intentar recuperar la Caja Chica indicada para la asignacion de Operacion");
   	   				return;
   	   			}
   			}

			cargarFormasDePago();
			cargarCuentasBancarias();
			
			if (this.pojoCajaChicaCuenta.getOperacion() == null || "".equals(this.pojoCajaChicaCuenta.getOperacion().trim()))
				this.pojoCajaChicaCuenta.setOperacion("C");
			this.referenciaOperacion = ""; 
			
   			if ("C".equals(this.pojoCajaChicaCuenta.getOperacion())) 
   				this.referenciaOperacion = (this.pojoCajaChicaCuenta.getNoCheque() != null ? this.pojoCajaChicaCuenta.getNoCheque().toString() : ""); 
			else if ("T".equals(this.pojoCajaChicaCuenta.getOperacion())) 
				this.referenciaOperacion = (this.pojoCajaChicaCuenta.getFolioAutorizacion() != null ? this.pojoCajaChicaCuenta.getFolioAutorizacion() : ""); 
   		} catch (Exception e) {
			control("Ocurrio un problema al intentar editar la Caja Chica indicado", e);
   		}
	}
	
	public void guardarAsignacionOperacion() {
		Respuesta resp = null;
		
		try {
			control();
			controlLog("Guardando Asignacion de Operacion a Caja Chica. Validaciones ... ");
			if (! validacionesAsignacionOperacion()) {
				controlLog("Guardando Asignacion de Operacion a Caja Chica. Validaciones ... FAIL");
				return;
			}

			this.pojoCajaChicaCuenta.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoCajaChicaCuenta.setFechaModificacion(Calendar.getInstance().getTime());
			controlLog("Guardando Asignacion de Operacion a Caja Chica. Validaciones ... OK");

			controlLog("Guardando Asignacion de Operacion a Caja Chica. Guardando ... ");
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			resp = this.ifzCajaChica.salvar(this.pojoCajaChicaCuenta);
			this.pojoCajaChicaCuenta = (PagosGastos) resp.getBody().getValor("entity");
			this.idCajaChica = this.pojoCajaChicaCuenta.getId();
			controlLog("Guardando Asignacion de Operacion a Caja Chica. Guardando ... OK");

			// Añadimos a listado
			controlLog("Guardando Asignacion de Operacion a Caja Chica. Añadimos a listado ");
			if (this.listCajasChicas == null || this.listCajasChicas.isEmpty()) {
				if (this.listCajasChicas == null)
					this.listCajasChicas = new ArrayList<PagosGastos>();
				this.listCajasChicas.add(this.pojoCajaChicaCuenta);
			} else {
				for (PagosGastos var : this.listCajasChicas) {
					if (this.idCajaChica == var.getId().longValue()) {
						var = this.pojoCajaChicaCuenta;
						break;
					}
				}
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			mensajeTransaccion();
			controlLog("Guadado Asignacion de Operacion a Caja Chica completo");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Asignacion de Operacion a Caja Chica", e);
		}
			
	}

	private boolean validacionesAsignacionOperacion() {
		if (this.pojoCajaChicaCuenta.getOperacion() == null)
			this.pojoCajaChicaCuenta.setOperacion("");
		this.pojoCajaChicaCuenta.setNoCheque(0);
		this.pojoCajaChicaCuenta.setFolioAutorizacion("");
		
		if (this.pojoCajaChicaCuenta.getOperacion() == null || "".equals(this.pojoCajaChicaCuenta.getOperacion().trim())) {
			control(-1, "Debe seleccionar una Operacion");
			return false;
		}

		if (this.pojoCajaChicaCuenta.getIdCuentaOrigen() == null || this.pojoCajaChicaCuenta.getIdCuentaOrigen() <= 0L) {
			control(-1, "Debe seleccionar una Cuenta");
			return false;
		}

		if (this.referenciaOperacion == null || "".equals(this.referenciaOperacion.trim())) {
			control(-1, "Debe indicar una Referencia");
			return false;
		}

		if ("C".equals(this.pojoCajaChicaCuenta.getOperacion().trim()) && ! NumberUtils.isNumber(this.referenciaOperacion)) {
			control(-1, "Referencia no valida para Cheque, debe ser de solo Numeros");
			return false;
		}
		
		if ("C".equals(this.pojoCajaChicaCuenta.getOperacion())) 
			this.pojoCajaChicaCuenta.setNoCheque(Integer.parseInt(this.referenciaOperacion.trim()));
		else  
			this.pojoCajaChicaCuenta.setFolioAutorizacion(this.referenciaOperacion.trim());
		
		for (CuentaBancaria cuenta : this.listCuentasBancarias) {
			if (this.pojoCajaChicaCuenta.getIdCuentaOrigen().longValue() == cuenta.getId()) {
				this.pojoCajaChicaCuenta.setNumeroCuentaOrigen(cuenta.getNumeroDeCuenta());
				break;
			}
		}
		
		return true;
	}

	// ------------------------------------------------------------------------------------------
	// BUSQUEDA OBRAS
	// ------------------------------------------------------------------------------------------

	public void nuevaBusquedaObras() {
		control();
		this.campoBusquedaObra = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObra = "";
		this.paginacionObras = 1;
		this.obrasAdministrativas = false;
		this.listObras = new ArrayList<Obra>();
		this.pojoObra = null;
	}
	
	public void buscarObras() {
		try {
			control();
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valorBusquedaObra, this.perfilEgresos, "", 0);
			if (this.listObras == null || this.listObras.isEmpty()) 
				control(13, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Obras", e);
		} finally {
			this.paginacionObras = 1;
			if (this.listObras == null)
				this.listObras = new ArrayList<Obra>();
			controlLog(this.listObras.size() + " Obras encontradas");
		}
	}

	public void seleccionarObra() {
		Obra obra = null;
		
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Obra seleccionada");
				return;
			}
			
			// Asignamos la Obra al REGISTRO DE GASTO, si corresponde
			obra = new Obra();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(obra, this.pojoObra);
			this.pojoCajaChica.setIdObra(obra);
			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		}
	}
	
	public void quitarObra() {
		boolean tieneObra = false;
				
		try {
			control();
			tieneObra = (this.pojoCajaChica != null && this.pojoCajaChica.getIdObra() != null && this.pojoCajaChica.getIdObra().getId() != null && this.pojoCajaChica.getIdObra().getId() > 0L);
			if (this.tipoCajaChica == 1 && tieneObra)
				this.pojoCajaChica.setIdObra(null);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar desasignar la Obra de la comprobacion", e);
		}
	}
	
	// ------------------------------------------------------------------------------------------
	// BENEFICIARIO
	// ------------------------------------------------------------------------------------------
	
	public void cambioBeneficiario() {
		this.beneficiario = "";
		this.beneficiarioRfcReferencia = "";
	}
	
	public List<Persona> autoacompletaBeneficiario(Object valorBusqueda) {
		List<Empleado> listEmpleados = null;
		String busqueda = "";
		
		try {
			busqueda = (valorBusqueda != null ? valorBusqueda.toString() : "");
			this.listBeneficiarios = new ArrayList<Persona>();
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			listEmpleados = this.ifzEmpleado.findLike(busqueda, null, 0, 100);
			if (listEmpleados == null || listEmpleados.isEmpty()) 
				return this.listBeneficiarios;
			
			for (Empleado var : listEmpleados)
				this.listBeneficiarios.add(this.ifzPersonas.findById(var.getIdPersona()));
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Beneficiarios (Empleados)", e);
		} finally {
			controlLog("Busqueda Beneficiario: [E] " + busqueda);
		}

		return this.listBeneficiarios;
	}
	
	private void seleccionarEmpleado() {
		PersonaExt proveedor = null;
		long idProveedor = 0L;

		if (this.beneficiario == null || "".equals(this.beneficiario.trim()))
			return;

		idProveedor = validateToID(this.beneficiario, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		proveedor = this.ifzCajaChica.findPersonaById(idProveedor, "P"); //getPersonaNegocio(idAuxiliar, this.pojoCajaChica.getTipoBeneficiario());
		if (proveedor != null && proveedor.getId() > 0L) {
			this.pojoCajaChica.setIdBeneficiario(proveedor);
			this.pojoCajaChica.setBeneficiario(proveedor.getNombre());
			this.pojoCajaChica.setTipoBeneficiario("P");
			//this.beneficiarioRfcReferencia = proveedor.getRfc();
		}
	}
	
	public PersonaExt getPersonaNegocio(long idProveedor, String tipoPersona) {
		try {
			if (idProveedor <= 0L)
				return null;
			tipoPersona = ((tipoPersona == null || "".equals(tipoPersona.trim())) ? "P" : tipoPersona);
			return this.ifzCajaChica.findPersonaById(idProveedor, tipoPersona);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la Persona/Negocio indicado: " + idProveedor + tipoPersona, e);
			return null;
		}
	}
	
	// ------------------------------------------------------------------------------------------
	// RAZON SOCIAL
	// ------------------------------------------------------------------------------------------
	
	public void cambioRazonSocial() {
		this.razonSocial = "";
		if (this.pojoCajaChicaDetalle != null) {
			this.pojoCajaChicaDetalle.setFacturaRfc("");
			this.pojoCajaChicaDetalle.setIdProveedor(null);
		}
	}
	
	public List<Persona> autoacompletaRazonSocial(Object valorBusqueda) {
		String busqueda = "";
		
		try {
			busqueda = (valorBusqueda != null ? valorBusqueda.toString() : "");
			busqueda = validateToDesc(busqueda, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			controlLog("Buscando Razon Social: [" + this.tipoRazonSocial + "] " + busqueda);
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			return this.ifzPersonas.findLikeClaveNombre("nombre", busqueda, this.grupoPersonas, this.tipoRazonSocial, 100, false);
		} catch (Exception e) {
			controlLog("Buscando Razon Social: [" + this.tipoRazonSocial + "] " + busqueda + " <<< ERROR ");
			control("Ocurrio un problema al consultas las Personas/Negocios", e);
			return new ArrayList<Persona>();
		} 
	}
	
	private void seleccionarRazonSocial() {
		PersonaExt proveedor = null;
		long idProveedor = 0L;

		try {
			if (this.razonSocial == null || "".equals(this.razonSocial.trim()))
				return;
	
			controlLog("Seleccionando Razon Social: [" + this.tipoRazonSocial + "] " + this.razonSocial);
			idProveedor = validateToID(this.razonSocial, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			proveedor = this.ifzCajaChica.findPersonaById(idProveedor, this.tipoRazonSocial); 
			if (proveedor != null && proveedor.getId() > 0L) {
				this.pojoCajaChicaDetalle.setIdProveedor(proveedor);
				this.pojoCajaChicaDetalle.setFacturaRfc(proveedor.getRfc());
				this.pojoCajaChicaDetalle.setProveedor(proveedor.getNombre());
				this.pojoCajaChicaDetalle.setTipoPersonaProveedor(this.tipoRazonSocial);
				if (this.pojoCajaChica != null && this.pojoCajaChica.getIdBeneficiario() != null && this.pojoCajaChica.getIdBeneficiario().getId() == idProveedor)
					this.pojoCajaChicaDetalle.setReferencia("N/A");
			}
		} catch (Exception e) {
			controlLog("Seleccionando Razon Social: [" + this.tipoRazonSocial + "] " + this.razonSocial + " <<< ERROR ");
			control("Ocurrio un problema al recuperar la Razon Social indicada", e);
		} 
	}
	
	// ------------------------------------------------------------------------------------------
	// COMPROBACIONES
	// ------------------------------------------------------------------------------------------
	
	private boolean nuevaComprobacion() {
		try {
			control();
			nuevaFactura();
			this.idEgresoPrevio = 0L;
			this.pojoComprobacion = new FacturaImpuestos();
			this.pojoCajaChicaDetalle = new PagosGastosDetExt();
			this.pojoCajaChicaDetalle.generarUniqueValue();
			this.pojoCajaChicaDetalle.setFecha(Calendar.getInstance().getTime());
			this.pojoCajaChicaDetalle.setRfcProveedor("");
			this.pojoCajaChicaDetalle.setReferencia("");
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.totalComprobacion = 0;
		} catch (Exception e) {
			control("Ocurrio un problema al generar una nueva Comprobacion", e);
			return false;
		} finally {
			this.indexComprobacionEliminar = -1;
			if (this.listComprobacionesEliminadas == null)
				this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>();
		}
		
		return true;
	}
	
	public void nuevaComprobacionConFactura() {
		try {
			if (! nuevaComprobacion())
				return;
			
			this.pojoCajaChicaDetalle.setFacturado(1);
			desglosaImpuestos();
			/*control();
			nuevaFactura();
			this.idEgresoPrevio = 0L;
			this.pojoComprobacion = new FacturaImpuestos();
			this.pojoCajaChicaDetalle = new PagosGastosDetExt();
			this.pojoCajaChicaDetalle.generarUniqueValue();
			this.pojoCajaChicaDetalle.setFacturado(1);
			this.pojoCajaChicaDetalle.setFecha(Calendar.getInstance().getTime());
			this.pojoCajaChicaDetalle.setRfcProveedor("");
			this.pojoCajaChicaDetalle.setReferencia("");
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.totalComprobacion = 0;
			desglosaImpuestos();*/
		} catch (Exception e) {
			control("Ocurrio un problema al generar una nueva Comprobacion con Factura", e);
		} /*finally {
			this.indexComprobacionEliminar = -1;
			if (this.listComprobacionesEliminadas == null)
				this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>();
			this.mapConceptoImpuestos = null;
		}*/
	}

	public void nuevaComprobacionSinFactura() {
		try {

			if (! nuevaComprobacion())
				return;

			this.pojoCajaChicaDetalle.setFacturado(0);
			desglosaImpuestos();
			cambioRazonSocial();
			/*control();
			nuevaFactura();
			this.idEgresoPrevio = 0L;
			this.pojoComprobacion = new FacturaImpuestos();
			this.pojoCajaChicaDetalle = new PagosGastosDetExt();
			this.pojoCajaChicaDetalle.generarUniqueValue();
			this.pojoCajaChicaDetalle.setFacturado(0);
			this.pojoCajaChicaDetalle.setFecha(Calendar.getInstance().getTime());
			this.pojoCajaChicaDetalle.setRfcProveedor("");
			this.pojoCajaChicaDetalle.setReferencia("");
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.totalComprobacion = 0;
			this.tipoRazonSocial = "N";
			desglosaImpuestos();
			cambioRazonSocial();*/
		} catch (Exception e) {
			control("Ocurrio un problema al generar una nueva Comprobacion sin Factura", e);
		} /*finally {
			this.indexComprobacionEliminar = -1;
			if (this.listComprobacionesEliminadas == null)
				this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>();
			this.mapConceptoImpuestos = null;
		}*/
	}

	public void editarComprobacion() {
		try {
			control();
			if (this.pojoComprobacion == null || this.pojoComprobacion.getPojoFactura() == null) {
				control(-1, "Ocurrio un problema al recuperar la Comprobacion indicada");
				return;
			}
			
			this.pojoCajaChicaDetalle = null;
			this.pojoComprobacion.setEditando(true);
			this.pojoCajaChicaDetalle = this.pojoComprobacion.getPojoFactura();
			this.pojoCajaChicaDetalle.generarUniqueValue();
			this.tipoRazonSocial = this.pojoCajaChicaDetalle.getTipoPersonaProveedor();
			this.totalComprobacion = this.pojoCajaChicaDetalle.getTotal();
			this.listDesgloseImpuestos = this.pojoComprobacion.getListImpuestos();
			if (this.listDesgloseImpuestos == null || this.listDesgloseImpuestos.isEmpty())
				desglosaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar editar la Comprobacion indicada", e);
		} finally {
			this.indexComprobacionEliminar = -1;
			if (this.listComprobacionesEliminadas == null)
				this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>();
			this.mapConceptoImpuestos = null;
		}
	}
	
	public void guardarComprobacion() {
		int factor = 1;
		
		try {
			control();
			if (! validarComprobacion())
				return;

			// Comprobamos si es un egreso y asignamos en negativo
			if (this.pojoCajaChicaDetalle.getFacturaTipo() != null && ("E".equals(this.pojoCajaChicaDetalle.getFacturaTipo()) || "EGRESO".equals(this.pojoCajaChicaDetalle.getFacturaTipo().trim().toUpperCase()))) 
				factor = -1;
			this.pojoCajaChicaDetalle.setSubtotal((this.pojoCajaChicaDetalle.getSubtotal() * factor));
			this.pojoCajaChicaDetalle.setTotalImpuestos((this.pojoCajaChicaDetalle.getTotalImpuestos() * factor));
			this.pojoCajaChicaDetalle.setTotalRetenciones((this.pojoCajaChicaDetalle.getTotalRetenciones() * factor));
			this.pojoCajaChicaDetalle.setTotal((this.pojoCajaChicaDetalle.getTotal() * factor));
			
			if (this.pojoCajaChicaDetalle.getId() == null || this.pojoCajaChicaDetalle.getId() <= 0L) {
				this.pojoCajaChicaDetalle.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoCajaChicaDetalle.setFechaCreacion(Calendar.getInstance().getTime());
			}
			this.pojoCajaChicaDetalle.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			this.pojoCajaChicaDetalle.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.pojoComprobacion.setPojoFactura(this.pojoCajaChicaDetalle);
			this.pojoComprobacion.setListImpuestos(copiaListas(this.listDesgloseImpuestos));
			this.pojoComprobacion.setTotalImpuestos(this.pojoCajaChicaDetalle.getTotalImpuestos());
			this.pojoComprobacion.setTotalRetenciones(this.pojoCajaChicaDetalle.getTotalRetenciones());
			
			if (this.pojoComprobacion.getIndex() >= 0) {
				this.listComprobaciones.set(this.pojoComprobacion.getIndex(), this.pojoComprobacion);
				control(false, 10, "Comprobacion actualizada", null);
				return;
			}
			
			this.pojoComprobacion.setIndex(this.listComprobaciones.size());
			this.listComprobaciones.add(this.pojoComprobacion);
			control(false, 10, "Comprobacion añadida", null);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Comprobacion indicada", e);
		} finally {
			actualizaTotalComprobado();
			this.agruparConceptos = false;
		}
	}

	public void evaluaEliminar() {
		SimpleDateFormat formatter = null;
		boolean permitido = false;
		
		try {
			control();
			if (this.pojoComprobacion == null || this.pojoComprobacion.getPojoFactura() == null) {
				control(-1, "Ocurrio un problema al recuperar la Comprobacion indicada");
				return;
			}
			
			if (this.pojoComprobacion.getPojoFactura() != null && this.pojoComprobacion.getPojoFactura().getId() != null && this.pojoComprobacion.getPojoFactura().getId() > 0L) {
				formatter = new SimpleDateFormat("dd-MM-yyyy");
				permitido = (formatter.format(Calendar.getInstance().getTime()).equals(formatter.format(this.pojoComprobacion.getPojoFactura().getFechaCreacion())));
				permitido = (permitido && this.loginManager.getInfoSesion().getAcceso().getUsuario().getId() == this.pojoComprobacion.getPojoFactura().getCreadoPor());
				if (! permitido && (! this.perfilPermiteEditar && ! validaRequest("DETEDIT", "1"))) 
		   			control(3, "No tiene permitido eliminar comprobaciones");
			} 
		} catch (Exception e) {
			control("Ocurrio un problema al validar la Comprobacion indicada", e);
		}
	}
	
	public void eliminarComprobacion() {
		try {
			control();
			if (this.pojoComprobacion == null || this.pojoComprobacion.getPojoFactura() == null) {
				control(-1, "Ocurrio un problema al recuperar la Comprobacion indicada");
				return;
			}
			
			this.listComprobacionesEliminadas.add(this.listComprobaciones.get(this.indexComprobacionEliminar));
			this.listComprobaciones.remove(this.indexComprobacionEliminar);
			actualizaTotalComprobado();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar la Comprobacion indicada", e);
		} finally {
			this.pojoComprobacion = new FacturaImpuestos();
			this.indexComprobacionEliminar = -1;
		}
	}

   	public void actualizaTotalComprobado() {
		double conFactura = 0;
		double sinFactura = 0;
   		double monto = 0;
		
		try {
			this.totalComprobaciones = 0D;
			this.pojoCajaChica.setConFactura(0);
			this.pojoCajaChica.setSinFactura(0);
			if (this.listComprobaciones == null || this.listComprobaciones.isEmpty()) 
				return;
			
			for (FacturaImpuestos comprobacion : this.listComprobaciones) {
				if (comprobacion.getPojoFactura().getSubtotal() == null)
					comprobacion.getPojoFactura().setSubtotal(0D);
				if (comprobacion.getPojoFactura().getTotalImpuestos() == null)
					comprobacion.getPojoFactura().setTotalImpuestos(0D);
				if (comprobacion.getPojoFactura().getTotalRetenciones() == null)
					comprobacion.getPojoFactura().setTotalRetenciones(0D);
				
				monto = ((comprobacion.getPojoFactura().getSubtotal().doubleValue() + comprobacion.getPojoFactura().getTotalImpuestos().doubleValue())- comprobacion.getPojoFactura().getTotalRetenciones().doubleValue());
				monto = (new BigDecimal(monto)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();  

				if (comprobacion.getPojoFactura().getFacturado() == 1)
					conFactura += monto;
				else if (comprobacion.getPojoFactura().getFacturado() == 0)
					sinFactura += monto;
			}
			
			this.totalComprobaciones = (conFactura + sinFactura);
			this.totalComprobaciones = (new BigDecimal(this.totalComprobaciones)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();  
			this.pojoCajaChica.setConFactura(conFactura);
			this.pojoCajaChica.setSinFactura(sinFactura);
			this.pojoCajaChica.setMonto(this.totalComprobaciones);
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la sumatoria de las Comprobaciones", e);
		}
	}
   	
   	public void desglosaImpuestos() {
   		List<GastosImpuestoExt> impuestosDelEgreso = null;
   		PagosGastosDetImpuestosExt pojoRegistroEgresosDetalleImpuesto = null;
   		double porcentajeImpuesto = 0;
   		double subtotal = 0;
   		double sumImpuestos = 0;
   		double sumRetenciones = 0;
   		double importeImpuesto = 0;
   		long idEgreso = 0;
   		// ----------------------------------------------------------------
   		double totalPago = 0;
   		double baseImpuestos = 0;
   		boolean baseSubtotal = false;
   			
   		try {
			control();
			if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getIdConcepto() != null && this.pojoCajaChicaDetalle.getIdConcepto().getId() > 0L)
				idEgreso = this.pojoCajaChicaDetalle.getIdConcepto().getId();

			this.totalComprobacion = Utilerias.redondear(this.totalComprobacion, 4);
			this.pojoCajaChicaDetalle.setSubtotal(subtotal);
			this.pojoCajaChicaDetalle.setTotalImpuestos(sumImpuestos);
			this.pojoCajaChicaDetalle.setTotalRetenciones(sumRetenciones);
			this.pojoCajaChicaDetalle.setTotal(this.totalComprobacion);
			baseImpuestos = this.totalComprobacion;

   			if (idEgreso <= 0L || this.totalComprobacion <= 0D) {
   				if (! this.listDesgloseImpuestos.isEmpty() && this.listImpuestosEliminadas != null)
					this.listImpuestosEliminadas.addAll(this.listDesgloseImpuestos);
				this.listDesgloseImpuestos.clear();
				return;
			}
   			
   			if (this.idEgresoPrevio > 0L && this.idEgresoPrevio == idEgreso && ! this.listDesgloseImpuestos.isEmpty()) {
   				for (PagosGastosDetImpuestosExt impuestoDelEgreso : this.listDesgloseImpuestos) {
   					importeImpuesto = 0D;
   					porcentajeImpuesto = impuestoDelEgreso.getPorcentaje();
   					if (porcentajeImpuesto > 0) {
   						if (porcentajeImpuesto > 1) 
   							porcentajeImpuesto = (porcentajeImpuesto / 100); 
   						totalPago = baseImpuestos;
   						if (! baseSubtotal) {
   							totalPago = (baseImpuestos / (1 + porcentajeImpuesto));
   							totalPago = Utilerias.redondear(totalPago, 4);
   							baseSubtotal = true;
   						}
   						
   						importeImpuesto = (totalPago * porcentajeImpuesto); 
   						importeImpuesto = Utilerias.redondear(importeImpuesto, 4); 

   						if ("AC".equals(impuestoDelEgreso.getIdImpuesto().getTipoCuenta())) {
   							sumRetenciones += importeImpuesto;
   							sumRetenciones = Utilerias.redondear(sumRetenciones, 4); 
   						} else if ("DE".equals(impuestoDelEgreso.getIdImpuesto().getTipoCuenta())) {
   							sumImpuestos += importeImpuesto;
   							sumImpuestos = Utilerias.redondear(sumImpuestos, 4); 
   						}
   					}

   					impuestoDelEgreso.setBase(new BigDecimal(baseImpuestos));
   					impuestoDelEgreso.setPorcentaje(porcentajeImpuesto);
   					impuestoDelEgreso.setValor(new BigDecimal(importeImpuesto));
   				}

   				subtotal = baseImpuestos - (sumImpuestos - sumRetenciones);
   				this.totalComprobacion = subtotal + (sumImpuestos - sumRetenciones);
   				this.existeRetencion = (sumRetenciones > 0);
   				return;
   			}

   			// Validamos Concepto de Gastos
			if (this.idEgresoPrevio > 0L) {
				if (! this.listDesgloseImpuestos.isEmpty() && this.listImpuestosEliminadas != null) 
					this.listImpuestosEliminadas.addAll(this.listDesgloseImpuestos);
			}
			
			// Impuestos del Gasto (Egreso)
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			impuestosDelEgreso = this.ifzGastoImpuesto.findAllExt(idEgreso, "");
			if (impuestosDelEgreso == null || impuestosDelEgreso.isEmpty()) {
				subtotal = this.totalComprobacion;
				return;
			}
			
			for (GastosImpuestoExt impuestoDelEgreso : impuestosDelEgreso) {
				importeImpuesto = 0D;
				porcentajeImpuesto = validateString2Double(impuestoDelEgreso.getImpuestoId().getAtributo1());
				if (porcentajeImpuesto > 0) {
					porcentajeImpuesto = (porcentajeImpuesto / 100); 
					totalPago = baseImpuestos;
					if (! baseSubtotal) {
						totalPago = (baseImpuestos / (1 + porcentajeImpuesto));
						totalPago = Utilerias.redondear(totalPago, 4);
						baseSubtotal = true;
					}
					
					importeImpuesto = (totalPago * porcentajeImpuesto); 
					importeImpuesto = Utilerias.redondear(importeImpuesto, 4); 

					if ("AC".equals(impuestoDelEgreso.getImpuestoId().getTipoCuenta())) {
						sumRetenciones += importeImpuesto;
						sumRetenciones = Utilerias.redondear(sumRetenciones, 4); 
					} else if ("DE".equals(impuestoDelEgreso.getImpuestoId().getTipoCuenta())) {
						sumImpuestos += importeImpuesto;
						sumImpuestos = Utilerias.redondear(sumImpuestos, 4); 
					}
				}

				pojoRegistroEgresosDetalleImpuesto = new PagosGastosDetImpuestosExt();
				pojoRegistroEgresosDetalleImpuesto.setIdPagosGastosDet(this.pojoCajaChicaDetalle);
				pojoRegistroEgresosDetalleImpuesto.setIdImpuesto(impuestoDelEgreso.getImpuestoId());
				pojoRegistroEgresosDetalleImpuesto.setBase(new BigDecimal(baseImpuestos));
				pojoRegistroEgresosDetalleImpuesto.setPorcentaje(porcentajeImpuesto);
				pojoRegistroEgresosDetalleImpuesto.setValor(new BigDecimal(importeImpuesto));
				pojoRegistroEgresosDetalleImpuesto.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				pojoRegistroEgresosDetalleImpuesto.setFechaCreacion(Calendar.getInstance().getTime());
				pojoRegistroEgresosDetalleImpuesto.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				pojoRegistroEgresosDetalleImpuesto.setFechaModificacion(Calendar.getInstance().getTime());
				this.listDesgloseImpuestos.add(pojoRegistroEgresosDetalleImpuesto);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al desglozar los impuestos", e);
		} finally {
			// Calculamos totales
			subtotal = baseImpuestos - (sumImpuestos - sumRetenciones);
			this.totalComprobacion = subtotal + (sumImpuestos - sumRetenciones);
			this.existeRetencion = (sumRetenciones > 0);
			// Asignamos totales
			this.pojoCajaChicaDetalle.setSubtotal(subtotal);
			this.pojoCajaChicaDetalle.setTotalImpuestos(sumImpuestos);
			this.pojoCajaChicaDetalle.setTotalRetenciones(sumRetenciones);
			this.pojoCajaChicaDetalle.setTotal(this.totalComprobacion);
			if (idEgreso > 0L && this.totalComprobacion > 0D) 
				controlLog("\nCOMPROBACION " + this.pojoCajaChicaDetalle.getUniqueValue() + "\n------------------------------\n     SUBTOTAL : " + subtotal + "\n    IMPUESTOS : " + sumImpuestos + "\n  RETENCIONES : " + sumRetenciones + "\n        TOTAL : " + this.totalComprobacion + "\n\n");
			// multiConceptos
			if (this.mapConceptoImpuestos != null && ! this.listDesgloseImpuestos.isEmpty()) 
				this.mapConceptoImpuestos.put(this.pojoCajaChicaDetalle.getUniqueValue(), this.listDesgloseImpuestos);
		}
   	}

   	public void cambioMontoImpuesto() {
   		double subtotal = 0;
   		double sumImpuestos = 0;
   		double sumRetenciones = 0;
   		
		try {
			control();
			if (this.totalComprobacion > 0) {
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getValor() != null) {
						if ("AC".equals(var.getIdImpuesto().getTipoCuenta()))
							sumRetenciones = Utilerias.redondear(sumRetenciones + var.getValor().doubleValue(),2);
						else
							sumImpuestos = Utilerias.redondear(sumImpuestos + var.getValor().doubleValue(),2);
					}
				}

				subtotal = this.totalComprobacion - (sumImpuestos - sumRetenciones);
				this.totalComprobacion = subtotal + (sumImpuestos - sumRetenciones);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al calcular el monto del Impuesto", e);
		} finally {
			this.pojoCajaChicaDetalle.setSubtotal(subtotal);
			this.pojoCajaChicaDetalle.setTotalImpuestos(sumImpuestos);
			this.pojoCajaChicaDetalle.setTotalRetenciones(sumRetenciones);
			this.pojoCajaChicaDetalle.setTotal(this.totalComprobacion);
		}
	}
   	
   	public void eliminarImpuestoDesglosado(int index) {
		try {
			if (index < 0 || index > this.listDesgloseImpuestos.size())
				return;
			
			if (this.listImpuestosEliminadas == null)
				this.listImpuestosEliminadas = new ArrayList<PagosGastosDetImpuestosExt>();
			this.listImpuestosEliminadas.add(this.listDesgloseImpuestos.get(index));
			this.listDesgloseImpuestos.remove(index);
			desglosaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Impuesto indicado", e);
		} 
	}
   	
   	private boolean validarComprobacion() {
   		try {
			this.pojoCajaChicaDetalle.setIdPagosGastos(this.pojoCajaChica); 
			this.pojoCajaChicaDetalle.generarUniqueValue();
			/*if (! this.pojoCajaChicaDetalle.validarUniqueValue())
				this.pojoCajaChicaDetalle.generarUniqueValue();*/
			if (this.pojoCajaChicaDetalle.getIdObra() == null)
				this.pojoCajaChicaDetalle.setIdObra(new Obra());
			this.pojoCajaChicaDetalle.setTotal(this.totalComprobacion);
   		} catch (Exception e) {
   			control("Ocurrio un problema al validar la Comprobacion indicada", e);
   			return false;
   		}
   		
   		return true;
   	}

	private List<PagosGastosDetImpuestosExt> copiaListas(List<PagosGastosDetImpuestosExt> fuente) {
   		List<PagosGastosDetImpuestosExt> resultado = new ArrayList<PagosGastosDetImpuestosExt>();
   		for (PagosGastosDetImpuestosExt pgi : fuente) 
   			resultado.add(((PagosGastosDetImpuestosExt) pgi.clone()));
   		return resultado;
   	}

	private double validateString2Double(String value) {
		if (value != null && ! "".equals(value.trim()))
			return Double.parseDouble(value.trim());
		return 0;
	}
	
	// ------------------------------------------------------------------------------------------
	// IMPORTACION DE XML
	// ------------------------------------------------------------------------------------------

	private void nuevaFactura() {
		resetXML();
		this.listComprobacionFacturaConceptos = new ArrayList<PagosGastosDetExt>();
	}

	public void resetXML() {
		this.fileName = "";
		this.fileSrc = null;
	}
	
	public void importarXML(UploadEvent event) {
		UploadItem item = null;
		
		try {
			control();
			item = event.getUploadItem();
			if (this.fileName != null && this.fileName.equals(item.getFileName()) && this.fileSrc != null) 
				return;
			
			this.fileName = item.getFileName();
			this.fileSrc = ((item.isTempFile()) ? Files.readAllBytes(item.getFile().toPath()) : item.getData());
			this.listComprobacionFacturaConceptos = new ArrayList<PagosGastosDetExt>();
			this.paginacionFacturaConceptos = 1;
			this.indexFacturaConcepto = -1;
			this.agruparConceptos = false;
			this.seleccionarTodo = false;
			
			if (! this.isDebug) return;
			controlLog("\n\n------------------------------------------------------------------------"
					+ "\n Archivo  : " + item.getFileName()  
					+ "\n------------------------------------------------------------------------"
					+ "\n Temp     : " + item.isTempFile() + "\n FileSize : " + item.getFileSize() 
					+ "\n File     : " + item.getFile() + "\n Data     : " + item.getData() 
					+ "\n------------------------------------------------------------------------");
		} catch (Exception e) {
			control("Ocurrio un problema al hacer la carga del XML", e);
		} 
	}
	
	@SuppressWarnings("unchecked")
	public void procesarXML() {
		Respuesta respuesta = new Respuesta();
		SimpleDateFormat formatter = null;
		// ------------------------------------------------
		String valorAux = "";
		String fileName = "";
		String comprobados = "";
		double saldoFactura = 0;
		String expresionImpresa = "";
		// ------------------------------------------------
		List<Persona> listProveedores = null;
		PersonaExt pojoProveedor = null;
		boolean nuevoProveedor = false;
		boolean completarProveedor = false;
		String tipoPersonaProveedor = "";
		String tipoPersonalidad = "";
		// ------------------------------------------------
		List<FacturaConcepto> conceptos = null;
		PagosGastosDetExt detalle = null;
		double totalFacturaConceptos = 0;
		
		try {
			control();
			if (this.fileSrc == null) {
				log.warn("VALIDACION - CFDI vacio (xml)");
				control(-1, "Ocurrio un problema al procesar el CFDI indicado");
				return;
			} 
			
			fileName = this.ftpDigitalizacionRuta + this.prefijoFacturas + "[ID].xml";
			this.ifzCajaChicaDet.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzCajaChicaDet.procesarCFDI(this.fileSrc, TiposGastoCXP.CajaChica, this.pojoCajaChica.getId(), fileName, this.beneficiarioRfcReferencia);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				if (respuesta.getBody().getValor("comprobados") != null)
					comprobados = desglozaComprobados(respuesta.getBody().getValor("comprobados").toString());
				log.warn("ERROR INTERNO - Ocurrio un problema al intentar guardar la factura. " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError() + "\nNo se cargo la Factura." + comprobados);
				return;
			}
			
			// Inicializamos si corresponde
			if (this.pojoCajaChicaDetalle == null) {
				this.pojoCajaChicaDetalle = new PagosGastosDetExt();
				this.pojoCajaChicaDetalle.setFacturado(1);
				this.pojoCajaChicaDetalle.setFecha(Calendar.getInstance().getTime());
				this.pojoCajaChicaDetalle.setRfcProveedor("");
				this.pojoCajaChicaDetalle.setReferencia("");
				this.pojoCajaChicaDetalle.generarUniqueValue();
			}
			
			valorAux = validateString(respuesta.getBody().getValor("idComprobante"));
			if (valorAux != null && ! "".equals(valorAux.trim()))
				this.pojoCajaChicaDetalle.setIdCfdi(Long.parseLong(valorAux.trim()));
			
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			valorAux = validateString(respuesta.getBody().getValor("comprobanteFecha"));
			if (valorAux != null && ! "".equals(valorAux.trim()))
				this.pojoCajaChicaDetalle.setFecha(formatter.parse(valorAux.trim()));
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteSerie"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoCajaChicaDetalle.setFacturaSerie(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteFolio"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoCajaChicaDetalle.setFacturaFolio(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteFolioFactura"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoCajaChicaDetalle.setReferencia(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteTipo"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoCajaChicaDetalle.setFacturaTipo(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteUuid"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoCajaChicaDetalle.setFacturaUuid(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("expresionImpresa"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoCajaChicaDetalle.setExpresionImpresa(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteEmisor"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoCajaChicaDetalle.setRfcProveedor(valorAux);

			valorAux = validateString(respuesta.getBody().getValor("comprobantePersonalidad"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				tipoPersonalidad = valorAux;

			valorAux = validateString(respuesta.getBody().getValor("comprobanteTipoPersona"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				tipoPersonaProveedor = valorAux;
			
			this.pojoCajaChicaDetalle.setTipoPersonaProveedor(tipoPersonaProveedor);
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteRfc"));
			if (valorAux != null && ! "".equals(valorAux.trim())) {
				this.pojoCajaChicaDetalle.setRfcProveedor(valorAux);
				listProveedores = this.ifzPersonas.findLikeClaveNombre("rfc", valorAux, this.grupoPersonas, "N", 0, false);
				if (listProveedores != null && ! listProveedores.isEmpty()) {
					pojoProveedor = extenderPersona(listProveedores.get(0));
					pojoProveedor.setTipoPersona(("M".equals(tipoPersonalidad) ? 2L : 1L));
					this.pojoCajaChicaDetalle.setIdProveedor(pojoProveedor);
					this.pojoCajaChicaDetalle.setFacturaRfc(pojoProveedor.getRfc());
					this.pojoCajaChicaDetalle.setNombreProveedor(pojoProveedor.getNombre());
					this.pojoCajaChicaDetalle.setTipoPersonaProveedor(tipoPersonaProveedor);
				} else {
					nuevoProveedor = true;
					this.pojoCajaChicaDetalle.setFacturaRfc(valorAux);
					valorAux = validateString(respuesta.getBody().getValor("comprobanteRazonSocial"));
					if (valorAux != null && ! "".equals(valorAux.trim())) 
						this.pojoCajaChicaDetalle.setNombreProveedor(valorAux);
					pojoProveedor = nuevoProveedor(this.pojoCajaChicaDetalle.getNombreProveedor(), this.pojoCajaChicaDetalle.getFacturaRfc(), tipoPersonaProveedor);
					this.pojoCajaChicaDetalle.setIdProveedor(pojoProveedor);
					if (pojoProveedor != null) {
						completarProveedor = true;
						this.pojoCajaChicaDetalle.setIdProveedor(pojoProveedor);
						this.pojoCajaChicaDetalle.setFacturaRfc(pojoProveedor.getRfc());
						this.pojoCajaChicaDetalle.setNombreProveedor(pojoProveedor.getNombre());
						this.pojoCajaChicaDetalle.setTipoPersonaProveedor(tipoPersonaProveedor);
					}
				}
			}
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteMoneda"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoCajaChicaDetalle.setFacturaMoneda(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteTipoCambio"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoCajaChicaDetalle.setFacturaTipoCambio(Double.parseDouble(valorAux));
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteSaldo"));
			if (valorAux != null && ! "".equals(valorAux.trim()))
				saldoFactura = Double.parseDouble(valorAux);
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteTotal"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoCajaChicaDetalle.setFacturaTotal(Double.parseDouble(valorAux));
			
			this.totalComprobacion = saldoFactura;
			conceptos = new ArrayList<FacturaConcepto>();
			this.listComprobacionFacturaConceptos = new ArrayList<PagosGastosDetExt>();
			this.listComprobacionFacturaConceptos.add(this.pojoCajaChicaDetalle);
			this.paginacionFacturaConceptos = 1;
			this.indexFacturaConcepto = -1;

			// Recuperamos los conceptos de la Factura
			if (respuesta.getBody().getValor("conceptos") != null)
				conceptos = (List<FacturaConcepto>) respuesta.getBody().getValor("conceptos");
			if (conceptos != null && conceptos.size() > 1) {
				totalFacturaConceptos = 0;
				this.listComprobacionFacturaConceptos.clear();
				for (FacturaConcepto concepto : conceptos) {
					totalFacturaConceptos += concepto.getTotal();
					// ---------------------------------------------------------------------
					detalle = new PagosGastosDetExt();
					BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
					BeanUtils.copyProperties(detalle, this.pojoCajaChicaDetalle);
					detalle.setFacturaConcepto(concepto.getDescripcion());
					detalle.setImporteConcepto(concepto.getImporte());
					detalle.setSubtotal(0D); 
					detalle.setTotalImpuestos(0D); 
					detalle.setTotalRetenciones(0D); 
					detalle.setTotal(concepto.getTotal()); 
					// ---------------------------------------------------------------------
					this.listComprobacionFacturaConceptos.add(detalle);
				}
				
				if (this.getMultiplesConceptos()) {
					this.mapConceptoImpuestos = new HashMap<String, List<PagosGastosDetImpuestosExt>>();
					this.pojoCajaChicaDetalle.setTotal(totalFacturaConceptos);
					this.agruparConceptos = true;
				}
			}
			
			// Comprobamos con los detalles actuales
			expresionImpresa = this.pojoCajaChicaDetalle.getExpresionImpresa();
			if (! "".equals(expresionImpresa.trim())) {
				for (FacturaImpuestos item : this.listComprobaciones) {
					if (item.getPojoFactura().compararEmpresionImpresa(expresionImpresa)) {
						nuevaFactura();
						if (respuesta.getBody().getValor("comprobados") != null)
							comprobados = desglozaComprobados(respuesta.getBody().getValor("comprobados").toString());
						control(-1, "El Documento ya fue cargado previamente en esta Caja Chica.\nEl Documento queda saldado con el monto añadido en este Registro." + comprobados);
						return;
					}
				}
			}
			
			desglosaImpuestos();
			resetXML();
			if (nuevoProveedor) {
				if (completarProveedor)
					control(false, 31, "El Emisor de la Factura no existe.\nSe guardo en Negocios, sin embargo, debe completar los datos.\nRFC: " + this.pojoCajaChicaDetalle.getFacturaRfc(), null);
				else
					control(false, 30, "El Emisor de la Factura no existe.\nOcurrio un problema y no se pudo guardar en Negocios.\nRFC: " + this.pojoCajaChicaDetalle.getFacturaRfc(), null);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar procesar el CFDI indicado", e);
		} 
	}
	
	public void descargarXML() {
		byte[] fileContenido = null;
		String fileName = "";
		Long idFactura = 0L;
		
		try {
			control();
			if (this.pojoComprobacion == null) {
				log.error("ERROR 20 - No tiene asignada ninguna factura");
				control(20, "No ha especificado ninguna factura (*.xml)");
				return;
			}

			if (this.pojoComprobacion.getPojoFactura().getIdCfdi() == null || this.pojoComprobacion.getPojoFactura().getIdCfdi() <= 0L)
				this.pojoComprobacion.getPojoFactura().setIdCfdi(0L);
			idFactura = this.pojoComprobacion.getPojoFactura().getIdCfdi();
			if (idFactura == null || idFactura <= 0L) {
				log.error("ERROR 20 - No tiene asignada ninguna factura");
				control(20, "No ha especificado ninguna factura (*.xml)");
				return;
			}

			// Inicializamos variables de archivos y recuperamos el archivo
			fileName = this.prefijoFacturas + idFactura + ".xml";
			fileContenido = this.ifzFtp.get(this.ftpDigitalizacionRuta + fileName);
			if (fileContenido == null || fileContenido.length <= 0) {
				control(21, "No se encontro el archivo en el servidor");
				return;
			}

			// Ponemos los datos en session
			this.httpSession.setAttribute("nombreDocumento", fileName);
			this.httpSession.setAttribute("formato", "xml");
			this.httpSession.setAttribute("contenido", fileContenido);
		} catch (Exception e) {
    		control(true, 9, "Ocurrio un problema al intentar recuperar el XML del servidor", e);
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

	private PersonaExt extenderPersona(Persona persona) {
		PersonaExt personaExt =  new PersonaExt();
		String res = "";
		
		personaExt.setId(persona.getId());
		personaExt.setCreadoPor(persona.getCreadoPor());
		personaExt.setFechaCreacion(persona.getFechaCreacion());
		personaExt.setModificadoPor(persona.getModificadoPor());
		personaExt.setFechaModificacion(persona.getFechaModificacion());
		if (persona.getHomonimo() > 0)
			personaExt.setHomonimo(true);
		else
			personaExt.setHomonimo(false);
		personaExt.setNombre(persona.getNombre());
		personaExt.setPrimerNombre(persona.getPrimerNombre());
		personaExt.setSegundoNombre(persona.getSegundoNombre());
		personaExt.setNombresPropios(persona.getNombresPropios());
		personaExt.setPrimerApellido(persona.getPrimerApellido());
		personaExt.setSegundoApellido(persona.getSegundoApellido());
		personaExt.setRfc(persona.getRfc());
		personaExt.setSexo(persona.getSexo());
		personaExt.setFechaNacimiento(persona.getFechaNacimiento());
		if (persona.getEstadoCivil() != null)
			personaExt.setEstadoCivil(this.ifzConValores.findById(persona.getEstadoCivil()));
		if (persona.getLocalidad() != null)
			personaExt.setLocalidad(this.ifzLocalidad.findById(persona.getLocalidad()));
		if (persona.getNacionalidad () != null)
			personaExt.setNacionalidad(this.ifzConValores.findById(persona.getNacionalidad()));
		personaExt.setNumeroHijos(persona.getNumeroHijos());
		personaExt.setDomicilio(persona.getDomicilio());
		personaExt.setTelefono(persona.getTelefono());
		personaExt.setCorreo(persona.getCorreo());
		if (persona.getFinado() > 0)
			personaExt.setFinado(true);
		else
			personaExt.setFinado(false);
		if(persona.getTipoSangre() != null)
			personaExt.setTipoSangre(this.ifzConValores.findById(persona.getTipoSangre()));
		if(persona.getColonia() != null)
			personaExt.setColonia(this.ifzColonia.findById(persona.getColonia()));
		personaExt.setTipoPersona(persona.getTipoPersona());
		res = persona.getPrimerNombre() != null && persona.getPrimerNombre().length() > 0? persona.getPrimerNombre() : "";
		res += persona.getSegundoNombre()	!= null && persona.getSegundoNombre().length() > 0 ? " " + persona.getSegundoNombre() : "";
		res += persona.getNombresPropios()	!= null && persona.getNombresPropios().length() > 0 ? " " + persona.getNombresPropios() : "";
		res += persona.getPrimerApellido()	!= null && persona.getPrimerApellido().length() > 0 ? " " + persona.getPrimerApellido() : "";
		res += persona.getSegundoApellido()	!= null && persona.getSegundoApellido().length() > 0 ? " " + persona.getSegundoApellido() : "";
		personaExt.setNombreCompleto(res);
		personaExt.setNumeroCuenta(persona.getNumeroCuenta());
		personaExt.setClabeInterbancaria(persona.getClabeInterbancaria());

		return personaExt;
	}

	private PersonaExt nuevoProveedor(String nombre, String rfc, String tipoPersona) {
		Respuesta respuesta = null;
		// ---------------------------------
		PersonaExt pojoProveedor = null;
		Persona persona = null;
		Negocio negocio = null;
		
		try {
			this.ifzCajaChica.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzCajaChica.nuevoNegocioProveedor(this.pojoCajaChicaDetalle.getUniqueValue(), nombre, rfc, tipoPersona);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("Ocurrio un problema al guardar el Negocio indicado: " + rfc);
				return null;
			}
			
			persona = (Persona) respuesta.getBody().getValor("persona");
			if (persona == null) {
				negocio = (Negocio) respuesta.getBody().getValor("pojoNegocio");
				if (negocio == null) 
					return null;
				persona = new Persona();
				persona.setId(negocio.getId());
				persona.setNombre(negocio.getNombre());
				persona.setRfc(negocio.getRfc());
				persona.setTipoPersona(2L);
			}
			
			if (persona != null && persona.getId() > 0L)
				pojoProveedor = extenderPersona(persona);
		} catch (Exception e) {
			log.error("Ocurrio un problema al guardar el Proveedor indicado: " + rfc, e);
		}
		
		return pojoProveedor;
	}
	
	private String validateString(Object value) {
		if (value != null && ! "".equals(value.toString().trim()))
			return value.toString().trim();
		return "";
	}
	
	public void foolMethodReset(String origen) {
		if (origen == null || "".equals(origen))
			origen = "NONE";
		resetXML();
		
		if (! this.isDebug)
			return;
		controlLog("\n\n------------------------------------------------------------------------"
				+ "\n RESET    : " + this.fileName + " [" + origen + "]"
				+ "\n------------------------------------------------------------------------"
				+ "\n FileSize : " + this.fileSrc.length
				+ "\n Data     : " + this.fileSrc 
				+ "\n------------------------------------------------------------------------");
	}

	public void foolMethodUpload(String origen) {
		if (origen == null || "".equals(origen))
			origen = "NONE";
		
		if (! this.isDebug)
			return;
		controlLog("\n\n------------------------------------------------------------------------"
				+ "\n UPLOAD   : " + this.fileName + " [" + origen + "]"
				+ "\n------------------------------------------------------------------------"
				+ "\n FileSize : " + this.fileSrc.length
				+ "\n Data     : " + this.fileSrc 
				+ "\n------------------------------------------------------------------------");
	}
	
	// ------------------------------------------------------------------------------------------
	// FACTURA Multiples conceptos
	// ------------------------------------------------------------------------------------------
	
	private void replicaAsignacion() {
		PagosGastosDetExt detalleOriginal = null;
		
		try {
			this.totalFacturaConceptos = 0;
			if (! this.getMultiplesConceptos())
				return;

			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			if (! this.pojoCajaChicaDetalle.isSeleccionado()) {
				this.seleccionarTodo = false;
				for (PagosGastosDetExt concepto : this.listComprobacionFacturaConceptos) {
					this.totalFacturaConceptos += concepto.getTotal();
					concepto.setSeleccionado(false);
					if (this.pojoCajaChicaDetalle.getUniqueValue() == concepto.getUniqueValue()) 
						BeanUtils.copyProperties(concepto, this.pojoCajaChicaDetalle);
				}
				return;
			}
			
			// Respaldamos valores originales
			detalleOriginal = new PagosGastosDetExt();
			BeanUtils.copyProperties(detalleOriginal, this.pojoCajaChicaDetalle);
			
			for (PagosGastosDetExt concepto : this.listComprobacionFacturaConceptos) {
				this.totalFacturaConceptos += concepto.getTotal();
				if (! concepto.isSeleccionado())
					continue;
				if (detalleOriginal.getUniqueValue() == concepto.getUniqueValue()) {
					BeanUtils.copyProperties(concepto, detalleOriginal);
					continue;
				}
				
				// Procesamos
				this.pojoCajaChicaDetalle = new PagosGastosDetExt();
				BeanUtils.copyProperties(this.pojoCajaChicaDetalle, concepto);
				// Comprobacion de Concepto
				if (validaObjetoIdToUpdate(detalleOriginal.getIdConcepto(), this.pojoCajaChicaDetalle.getIdConcepto())) {
					this.pojoCajaChicaDetalle.setIdConcepto(detalleOriginal.getIdConcepto());
					this.totalComprobacion = this.pojoCajaChicaDetalle.getTotal();
					this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
					if (this.mapConceptoImpuestos.containsKey(this.pojoCajaChicaDetalle.getUniqueValue()))
						this.listDesgloseImpuestos = this.mapConceptoImpuestos.get(this.pojoCajaChicaDetalle.getUniqueValue());
					desglosaImpuestos();
				}
				// Comprobacion de Obra
				if (validaObjetoIdToUpdate(detalleOriginal.getIdObra(), this.pojoCajaChicaDetalle.getIdObra()))
					this.pojoCajaChicaDetalle.setIdObra(detalleOriginal.getIdObra());
				// Comprobacion de Orden Compra
				if (validaObjetoIdToUpdate(detalleOriginal.getIdOrdenCompra(), this.pojoCajaChicaDetalle.getIdOrdenCompra()))
					this.pojoCajaChicaDetalle.setIdOrdenCompra(detalleOriginal.getIdOrdenCompra());
				// Actualizamos en lista
				BeanUtils.copyProperties(concepto, this.pojoCajaChicaDetalle);
			}
			
			// Restauramos original
			this.pojoCajaChicaDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoCajaChicaDetalle, detalleOriginal);
			this.totalComprobacion = this.pojoCajaChicaDetalle.getTotal();
		} catch (Exception e) {
			control("Ocurrio un problema al actualizar los datos al concepto indicado", e);
		} 
	}
	
	private boolean validaObjetoIdToUpdate(Object obj1, Object obj2) {
		Long idObj1 = 0L;
		Long idObj2 = 0L;
		
		if (obj1 == null && obj2 == null)
			return false;
		if (obj1 != null && obj2 == null)
			return true;
		if (obj1 == null && obj2 != null)
			return true;
		
		if (obj1 instanceof ConValores && obj2 instanceof ConValores) {
			idObj1 = ((ConValores) obj1).getId();
			idObj2 = ((ConValores) obj2).getId();
		} else if (obj1 instanceof Obra && obj2 instanceof Obra) {
			idObj1 = ((Obra) obj1).getId();
			idObj2 = ((Obra) obj2).getId();
		/*} else if (obj1 instanceof OrdenCompra && obj2 instanceof OrdenCompra){
			idObj1 = ((OrdenCompra) obj1).getId();
			idObj2 = ((OrdenCompra) obj2).getId();*/
		} else return false;

		idObj1 = (idObj1 == null ? 0L : idObj1);
		idObj2 = (idObj2 == null ? 0L : idObj2);
		if (idObj1 <= 0L && idObj2 <= 0L)
			return false;

		if (idObj1 > 0L && idObj2 <= 0L)
			return true;
		return ! (idObj1.compareTo(idObj2) == 0);
	}
	
	private boolean validateIndex(int index) {
		if (this.listComprobacionFacturaConceptos == null || this.listComprobacionFacturaConceptos.isEmpty() || index > this.listComprobacionFacturaConceptos.size() || index < 0) {
			control(-1, "No se pudo recuperar el Concepto indicado.\nNo puedo calcular impuestos");
			return false;
		}
		
		return true;
	}

	public void nuevaBusquedaConceptos(int index) {
		PagosGastosDetExt detalle = null;
		
		try {
			control();
			if (! validateIndex(index))
				return;
			
			if (this.indexFacturaConcepto != index && this.listDesgloseImpuestos != null && ! this.listDesgloseImpuestos.isEmpty()) {
				this.mapConceptoImpuestos.put(this.listDesgloseImpuestos.get(0).getIdPagosGastosDet().getUniqueValue(), this.listDesgloseImpuestos);
				this.listDesgloseImpuestos.clear();
			}
			
			this.indexFacturaConcepto = index;
			detalle = this.listComprobacionFacturaConceptos.get(index);
			this.pojoCajaChicaDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoCajaChicaDetalle, detalle);
			this.totalComprobacion = this.pojoCajaChicaDetalle.getTotal();
			if (this.mapConceptoImpuestos.containsKey(this.pojoCajaChicaDetalle.getUniqueValue()))
				this.listDesgloseImpuestos = this.mapConceptoImpuestos.get(this.pojoCajaChicaDetalle.getUniqueValue());
			if (this.listDesgloseImpuestos == null || this.listDesgloseImpuestos.isEmpty())
				desglosaImpuestos();
			nuevaBusquedaConceptos();
		} catch (Exception e) {
			control("Ocurrio un problema al preparar la asignacion de Egreso al Concepto indicado", e);
		}
	}

	public void nuevaBusquedaObras(int index) {
		PagosGastosDetExt detalle = null;
		
		try {
			control();
			if (! validateIndex(index))
				return;
			
			this.indexFacturaConcepto = index;
			detalle = this.listComprobacionFacturaConceptos.get(index);
			this.pojoCajaChicaDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoCajaChicaDetalle, detalle);
			this.totalComprobacion = this.pojoCajaChicaDetalle.getTotal();
			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al preparar la asignacion de Obra/Egreso Administrativo al Concepto indicado", e);
		}
	}

	public void quitarObra(int index) {
		PagosGastosDetExt detalle = null;
		
		try {
			control();
			if (! validateIndex(index))
				return;
			
			this.indexFacturaConcepto = index;
			detalle = this.listComprobacionFacturaConceptos.get(index);
			this.pojoCajaChicaDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoCajaChicaDetalle, detalle);
			this.totalComprobacion = this.pojoCajaChicaDetalle.getTotal();
			this.pojoCajaChicaDetalle.setSeleccionado(false);
			quitarObra();
			// multiConceptos
			replicaAsignacion();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar la Obra al Concepto indicado", e);
		}
	}
	
	public void quitarOrdenCompra(int index) {
		PagosGastosDetExt detalle = null;
		
		try {
			control();
			if (! validateIndex(index))
				return;
			
			this.indexFacturaConcepto = index;
			detalle = this.listComprobacionFacturaConceptos.get(index);
			this.pojoCajaChicaDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoCajaChicaDetalle, detalle);
			this.totalComprobacion = this.pojoCajaChicaDetalle.getTotal();
			this.pojoCajaChicaDetalle.setSeleccionado(false);
			// multiConceptos
			replicaAsignacion();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar la Orden de Compra al Concepto indicado", e);
		}
	}

	public void desglosaImpuestos(int index) {
		PagosGastosDetExt detalle = null;
		
		try {
			if (! validateIndex(index))
				return;
			
			if (this.indexFacturaConcepto != index && this.listDesgloseImpuestos != null && ! this.listDesgloseImpuestos.isEmpty()) 
				this.mapConceptoImpuestos.put(this.listDesgloseImpuestos.get(0).getIdPagosGastosDet().getUniqueValue(), this.listDesgloseImpuestos);
			
			this.indexFacturaConcepto = index;
			detalle = this.listComprobacionFacturaConceptos.get(index);
			this.pojoCajaChicaDetalle = new PagosGastosDetExt();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.pojoCajaChicaDetalle, detalle);
			this.totalComprobacion = this.pojoCajaChicaDetalle.getTotal();
			
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			if (this.mapConceptoImpuestos.containsKey(this.pojoCajaChicaDetalle.getUniqueValue())) {
				if (this.pojoCajaChicaDetalle.getIdConcepto() != null && this.pojoCajaChicaDetalle.getIdConcepto().getId() > 0L)
					this.idEgresoPrevio = this.pojoCajaChicaDetalle.getIdConcepto().getId();
				this.listDesgloseImpuestos = this.mapConceptoImpuestos.get(this.pojoCajaChicaDetalle.getUniqueValue());
			}
			desglosaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al calcular los impuestos del concepto indicado.\nConcepto Num. " + (index + 1), e);
		} 
	}

	public void totalizaConceptos() {
		double total = 0;
		
		try {
			if (! this.agruparConceptos)
				return;
			
			this.pojoCajaChicaDetalle = null;
			for (PagosGastosDetExt concepto : this.listComprobacionFacturaConceptos) {
				total += concepto.getTotal();
				if (this.pojoCajaChicaDetalle == null) {
					this.pojoCajaChicaDetalle = new PagosGastosDetExt();
					BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
					BeanUtils.copyProperties(this.pojoCajaChicaDetalle, concepto);
					continue;
				}
				
				// Asignacion de Concepto
				if (concepto.getIdConcepto() != null && concepto.getIdConcepto().getId() > 0L)
					continue;
				if (this.pojoCajaChicaDetalle.getIdConcepto() != null && this.pojoCajaChicaDetalle.getIdConcepto().getId() > 0L)
					continue;
				this.pojoCajaChicaDetalle.setIdConcepto(concepto.getIdConcepto());
				
				// Asignacion de Obra
				if (concepto.getIdObra() != null && concepto.getIdObra().getId() != null && concepto.getIdObra().getId() > 0L)
					continue;
				if (this.pojoCajaChicaDetalle.getIdObra() != null && this.pojoCajaChicaDetalle.getIdObra().getId() != null && this.pojoCajaChicaDetalle.getIdObra().getId() > 0L)
					continue;
				this.pojoCajaChicaDetalle.setIdObra(concepto.getIdObra());
				
				// Asignacion de Orden de Compra
				if (concepto.getIdOrdenCompra() != null && concepto.getIdOrdenCompra().getId() != null && concepto.getIdOrdenCompra().getId() > 0L)
					continue;
				if (this.pojoCajaChicaDetalle.getIdOrdenCompra() != null && this.pojoCajaChicaDetalle.getIdOrdenCompra().getId() != null && this.pojoCajaChicaDetalle.getIdOrdenCompra().getId() > 0L)
					continue;
				this.pojoCajaChicaDetalle.setIdOrdenCompra(concepto.getIdOrdenCompra());
			}

			this.pojoCajaChicaDetalle.setTotal(total);
			this.totalComprobacion = total;
			desglosaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al actualizar los datos al concepto indicado", e);
		} 
	}
	
	public void guardarComprobaciones() {
		FacturaImpuestos oriComprobacion = null;
		// -----------------------------------------------------
		HashMap<Long, List<PagosGastosDetExt>> agrupados = null;
		HashMap<Long, List<PagosGastosDetImpuestosExt>> impuestos = null;
		List<PagosGastosDetExt> consolidados = null;
		List<PagosGastosDetImpuestosExt> impuestosConsolidados = null;
		List<PagosGastosDetImpuestosExt> tmp = null;
		List<Long> idImpuestos = null;
		PagosGastosDetExt detalle = null;
		String descripcion = "";
		
		try {
			control();
			if (this.agruparConceptos) {
				guardarComprobacion();
				return;
			}
			
			oriComprobacion = new FacturaImpuestos();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(oriComprobacion, this.pojoComprobacion);
			oriComprobacion.setIndex(-1);
			
			// Agrupando conceptos
			idImpuestos = new ArrayList<Long>();
			agrupados = new HashMap<Long, List<PagosGastosDetExt>>();
			impuestos = new HashMap<Long, List<PagosGastosDetImpuestosExt>>();
			for (PagosGastosDetExt conceptoFactura : this.listComprobacionFacturaConceptos) {
				consolidados = new ArrayList<PagosGastosDetExt>();
				impuestosConsolidados = new ArrayList<PagosGastosDetImpuestosExt>();
				if (agrupados.containsKey(conceptoFactura.getIdConcepto().getId())) {
					consolidados = agrupados.get(conceptoFactura.getIdConcepto().getId());
					impuestosConsolidados = impuestos.get(conceptoFactura.getIdConcepto().getId());
					idImpuestos = new ArrayList<Long>();
					for (PagosGastosDetImpuestosExt imp : impuestosConsolidados)
						idImpuestos.add(imp.getIdImpuesto().getId());
				}
				
				consolidados.add(conceptoFactura);
				agrupados.put(conceptoFactura.getIdConcepto().getId(), consolidados);

				if (this.mapConceptoImpuestos.containsKey(conceptoFactura.getUniqueValue())) {
					tmp = this.mapConceptoImpuestos.get(conceptoFactura.getUniqueValue());
					for (PagosGastosDetImpuestosExt imp : tmp) {
						if (! idImpuestos.contains(imp.getIdImpuesto().getId())) {
							idImpuestos.add(imp.getIdImpuesto().getId());
							impuestosConsolidados.add(imp);
						}
					}
					
					impuestos.put(conceptoFactura.getIdConcepto().getId(), impuestosConsolidados);
				}
			}

			// Consolidando conceptos
			consolidados = new ArrayList<PagosGastosDetExt>();
			for (Entry<Long, List<PagosGastosDetExt>> grupo : agrupados.entrySet()) {
				for (PagosGastosDetExt conceptoFactura : grupo.getValue()) {
					if (detalle == null) {
						detalle = conceptoFactura;
						descripcion = detalle.getFacturaConcepto() + "|" + detalle.getImporteConceptoNumber();
						continue;
					}
					
					descripcion += "|" + conceptoFactura.getFacturaConcepto() + "|" + conceptoFactura.getImporteConceptoNumber();
					detalle.setFacturaConcepto(descripcion);
					detalle.setImporteConcepto(detalle.getImporteConcepto() + conceptoFactura.getImporteConcepto());
					detalle.setSubtotal(detalle.getSubtotal() + conceptoFactura.getSubtotal());
					detalle.setTotalImpuestos(detalle.getTotalImpuestos() + conceptoFactura.getTotalImpuestos());
					detalle.setTotalRetenciones(detalle.getTotalRetenciones() + conceptoFactura.getTotalRetenciones());
					detalle.setTotal(detalle.getTotal() + conceptoFactura.getTotal());
				}
				
				consolidados.add(detalle);
				detalle = null;
			}
			
			for (PagosGastosDetExt conceptoFactura : consolidados) { 
				this.pojoComprobacion = new FacturaImpuestos();
				BeanUtils.copyProperties(this.pojoComprobacion, oriComprobacion);
				
				this.pojoCajaChicaDetalle = new PagosGastosDetExt();
				BeanUtils.copyProperties(this.pojoCajaChicaDetalle, conceptoFactura);
				this.totalComprobacion = this.pojoCajaChicaDetalle.getTotal();
				this.idEgresoPrevio = this.pojoCajaChicaDetalle.getIdConcepto().getId();
				this.listDesgloseImpuestos = impuestos.get(this.pojoCajaChicaDetalle.getIdConcepto().getId());
				desglosaImpuestos();
				guardarComprobacion();
			}
			
			this.seleccionarTodo = false;
			this.listComprobacionFacturaConceptos = new ArrayList<PagosGastosDetExt>();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Comprobacion indicada", e);
		} 
	}
	
	public void seleccionarTodo() {
		if (this.listComprobacionFacturaConceptos == null || this.listComprobacionFacturaConceptos.isEmpty())
			return;
		for (PagosGastosDetExt concepto : this.listComprobacionFacturaConceptos) 
			concepto.setSeleccionado(this.seleccionarTodo);
	}
	
	public void validaSeleccionarTodo() {
		this.seleccionarTodo = false;
		if (this.listComprobacionFacturaConceptos == null || this.listComprobacionFacturaConceptos.isEmpty())
			return;
		this.seleccionarTodo = true;
		for (PagosGastosDetExt concepto : this.listComprobacionFacturaConceptos) {
			if (! concepto.isSeleccionado()) {
				this.seleccionarTodo = false;
				break;
			}
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
	   		this.pagBusquedaConceptos = 1;
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listConceptoGasto = this.ifzConValores.findLikeProperty(this.campoBusquedaConceptos, value, this.grupoEgresos, 0);
			if (this.listConceptoGasto == null || this.listConceptoGasto.isEmpty()) 
				control(13, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al buscar los Conceptos para Gastos", e);
		}
	}
	
	public void seleccionarConcepto() {
		ConValores egreso = null;
		
		try {
			control();
			if (this.pojoConcepto == null || this.pojoConcepto.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar el Concepto seleccionado");
				return;
			}
			
			this.idEgresoPrevio = 0L;
			if (this.pojoCajaChicaDetalle.getIdConcepto() != null && this.pojoCajaChicaDetalle.getIdConcepto().getId() > 0L)
				this.idEgresoPrevio = ((this.pojoConcepto.getId() != this.pojoCajaChicaDetalle.getIdConcepto().getId()) ? this.pojoConcepto.getId() : 0L);

			// Asignamos la Obra al REGISTRO DE GASTO, si corresponde
			egreso = new ConValores();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(egreso, this.pojoConcepto);
			this.pojoCajaChicaDetalle.setIdConcepto(egreso);
			desglosaImpuestos();

			// multiConceptos
			replicaAsignacion();
			nuevaBusquedaConceptos();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Concepto de Gasto seleccionado", e);
		} 
	}

   	// ---------------------------------------------------------------------------
	// RETENCIONES
   	// ---------------------------------------------------------------------------
	
	public void nuevaBusquedaRetenciones() {
		control();
		this.campoBusquedaRetenciones = this.tiposBusquedaRetenciones.get(0).getValue().toString();
		this.valorBusquedaRetenciones = "";
		this.pagBusquedaRetenciones = 1;
		
		this.listBusquedaRetenciones = new ArrayList<ConValores>();
		this.pojoBusquedaRetenciones = null;
	}
	
	public void buscarRetenciones() {
		String value = "";
		
		try {
			control();
			if (this.campoBusquedaRetenciones == null || "".equals(this.campoBusquedaRetenciones))
				this.campoBusquedaRetenciones = this.tiposBusquedaRetenciones.get(0).getValue().toString();
			value = this.valorBusquedaRetenciones;
			value = (value == null) ? "" : value.trim();
			if (value.contains(" "))
				value = value.replace(" ", "%");
	   		this.pagBusquedaRetenciones = 1;
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listBusquedaRetenciones = this.ifzConValores.findLikeProperty(this.campoBusquedaRetenciones, value, this.grupoImpuestos, "AC", 0);
			if (this.listBusquedaRetenciones == null || this.listBusquedaRetenciones.isEmpty()) 
				control(13, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Requisiciones", e);
		}
	}
	
	public void seleccionarRetencion() {
		PagosGastosDetImpuestosExt pojoDesgloImpto = null;
		double porcentajeImpuesto = 0;
		
		try {
			control();
			if (this.listDesgloseImpuestos != null && ! this.listDesgloseImpuestos.isEmpty()) {
				for (PagosGastosDetImpuestosExt var : this.listDesgloseImpuestos) {
					if (var.getIdImpuesto().getId() == this.pojoBusquedaRetenciones.getId()) {
						control(17, "Esta retencion ya ha sido agregada.");
						return;
					}
				}
			}

			if (! this.getMultiplesConceptos() || (this.getMultiplesConceptos() && this.agruparConceptos))
				this.idEgresoPrevio = this.pojoCajaChicaDetalle.getIdConcepto().getId();
			porcentajeImpuesto = validateString2Double(this.pojoBusquedaRetenciones.getAtributo1());
			if (porcentajeImpuesto > 0) 
				porcentajeImpuesto = (porcentajeImpuesto / 100); 
			
			pojoDesgloImpto = new PagosGastosDetImpuestosExt();
			pojoDesgloImpto.setIdImpuesto(this.pojoBusquedaRetenciones);
			pojoDesgloImpto.setIdPagosGastosDet(this.pojoCajaChicaDetalle);
			pojoDesgloImpto.setPorcentaje(porcentajeImpuesto);
			pojoDesgloImpto.setBase(BigDecimal.ZERO);
			pojoDesgloImpto.setValor(BigDecimal.ZERO);
			pojoDesgloImpto.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			pojoDesgloImpto.setFechaCreacion(Calendar.getInstance().getTime());
			pojoDesgloImpto.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			pojoDesgloImpto.setFechaModificacion(Calendar.getInstance().getTime());
			this.listDesgloseImpuestos.add(pojoDesgloImpto);
			this.existeRetencion = true;
			desglosaImpuestos();
			
			// multiConceptos
			this.pojoCajaChicaDetalle.setSeleccionado(false);
			replicaAsignacion();
		} catch (Exception e) {
			control("Ocurrio un problema al asignar la Retencion seleccionada", e);
		}
	}
	
	// ------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------------

	public String getTabSelected() {
		return "tab1";
	}
	
	public void setTabSelected(String value) {}
	
	public boolean getXmlCargado() {
		return (this.fileSrc != null && (this.fileName != null && ! "".equals(this.fileName.trim())));
	}
	
	public void setXmlCargado(boolean value) {}
	
	public boolean getSeleccionarTodo() {
		return this.seleccionarTodo;
	}
	
	public void setSeleccionarTodo(boolean seleccionarTodo) {
		this.seleccionarTodo = seleccionarTodo;
	}
	
   	public String getTitulo() {
   		if (this.pojoCajaChica != null && this.pojoCajaChica.getId() != null && this.pojoCajaChica.getId() > 0L)
   			return "Caja Chica " + this.pojoCajaChica.getId();
   		return "Nueva Caja Chica";
   	}
   	
   	public void setTitulo(String value) { }

   	public String getTituloComprobacion() {
   		if (this.pojoCajaChica != null && this.pojoCajaChica.getId() != null && this.pojoCajaChica.getId() > 0L)
   			return "Comprobacion " + this.pojoCajaChica.getId();
   		return "Nuevo Comprobacion";
   	}
   	
   	public void setTituloComprobacion(String value) { }

   	public String getTituloComprobacionFactura() {
   		if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getIdCfdi() != null && this.pojoCajaChicaDetalle.getIdCfdi() > 0L)
   			return "Factura " + this.pojoCajaChicaDetalle.getIdCfdi() + " - " + this.pojoCajaChicaDetalle.getFacturaUuid();
   		return "Nuevo Factura";
   	}
   	
   	public void setTituloComprobacionFactura(String value) { }

   	public String getTituloCajaChicaOperacion() {
   		if (this.pojoCajaChicaCuenta != null && this.pojoCajaChicaCuenta.getId() != null && this.pojoCajaChicaCuenta.getId() > 0L)
   			return "Caja Chica # " + this.pojoCajaChicaCuenta.getConsecutivo() + " - " + this.pojoCajaChicaCuenta.getBeneficiario();
   		return "Asignacion de Operacion a Caja Chica";
   	}
   	
   	public void setTituloCajaChicaOperacion(String value) {}

	public String getOperacionAsinacion() {
		if (this.pojoCajaChicaCuenta != null && this.pojoCajaChicaCuenta.getOperacion() != null)
			return this.pojoCajaChicaCuenta.getOperacion();
		return "";
	}

	public void setOperacionAsinacion(String operacion) {
		if (this.pojoCajaChicaCuenta != null)
			this.pojoCajaChicaCuenta.setOperacion(operacion);
	}

	public long getIdCuentaBancariaAsinacion() {
		if (this.pojoCajaChicaCuenta != null && this.pojoCajaChicaCuenta.getIdCuentaOrigen() != null && this.pojoCajaChicaCuenta.getIdCuentaOrigen() > 0L)
			return this.pojoCajaChicaCuenta.getIdCuentaOrigen();
		return 0L;
	}

	public void setIdCuentaBancariaAsinacion(long idCuentaBancaria) {
		this.pojoCajaChicaCuenta.setIdCuentaOrigen(idCuentaBancaria);
	}

	public double getMontoAsinacion() {
		if (this.pojoCajaChicaCuenta != null && this.pojoCajaChicaCuenta.getMonto() != null)
			return this.pojoCajaChicaCuenta.getMonto();
		return 0;
	}
	
	public void setMontoAsinacion(double monto) { }
	
	public List<PagosGastos> getListCajasChicas() {
		return listCajasChicas;
	}

	public void setListCajasChicas(List<PagosGastos> listCajasChicas) {
		this.listCajasChicas = listCajasChicas;
	}

	public long getIdRegistroEgresos() {
		return idCajaChica;
	}

	public void setIdRegistroEgresos(long idRegistroEgresos) {
		this.idCajaChica = idRegistroEgresos;
	}

	public long getPojoCajaChica() {
		return this.idCajaChica;
	}

	public void setPojoCajaChica(long idCajaChica) {
		this.idCajaChica = idCajaChica;
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

	public int getNumCajaChica() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getConsecutivo() > 0)
			return this.pojoCajaChica.getConsecutivo();
		return 0;
	}

	public void setNumCajaChica(int numCajaChica) {}

	public Date getFecha() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getFecha() != null)
			return this.pojoCajaChica.getFecha();
		return Calendar.getInstance().getTime();
	}

	public void setFecha(Date fecha) {
		this.pojoCajaChica.setFecha(fecha);
	}

	public long getIdSucursal() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getIdSucursal() != null)
			return this.pojoCajaChica.getIdSucursal().getId();
		return 0L;
	}

	public void setIdSucursal(long idSucursal) {
		asignarSucursal(idSucursal);
	}

	public String getOperacion() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getOperacion() != null)
			return this.pojoCajaChica.getOperacion();
		return "";
	}

	public void setOperacion(String operacion) {
		if (this.pojoCajaChica != null)
			this.pojoCajaChica.setOperacion(operacion);
	}

	public long getIdCuentaBancaria() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getIdCuentaOrigen() != null && this.pojoCajaChica.getIdCuentaOrigen().getId() > 0L)
			return this.pojoCajaChica.getIdCuentaOrigen().getId();
		return 0L;
	}

	public void setIdCuentaBancaria(long idCuentaBancaria) {
		if (this.pojoCajaChica != null) {
			if (idCuentaBancaria > 0L)
				this.pojoCajaChica.setIdCuentaOrigen(this.ifzCajaChica.findCuentaBancariaById(idCuentaBancaria));
			else if (idCuentaBancaria <= 0L)
				this.pojoCajaChica.setIdCuentaOrigen(null);
		}
	}

	public String getReferenciaOperacion() {
		return this.referenciaOperacion;
	}

	public void setReferenciaOperacion(String referenciaOperacion) {
		this.referenciaOperacion = referenciaOperacion;
	}

	public String getTipoBeneficiario() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getTipoBeneficiario() != null)
			return this.pojoCajaChica.getTipoBeneficiario();
		return "";
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		if (this.pojoCajaChica != null)
			this.pojoCajaChica.setTipoBeneficiario(tipoBeneficiario);
	}

	public String getBeneficiario() {
		if (this.pojoCajaChica != null &&  this.pojoCajaChica.getIdBeneficiario() != null &&  this.pojoCajaChica.getIdBeneficiario().getId() > 0L)
			return this.pojoCajaChica.getIdBeneficiario().getId() + " - " + this.pojoCajaChica.getBeneficiario();
		return "";
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
		seleccionarEmpleado();
	}

	public List<Persona> getListBeneficiarios() {
		return listBeneficiarios;
	}

	public void setListBeneficiarios(List<Persona> listBeneficiarios) {
		this.listBeneficiarios = listBeneficiarios;
	}

	public double getMonto() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getMonto() != null)
			return this.pojoCajaChica.getMonto();
		return 0;
	}
	
	public void setMonto(double monto) { }
	
	public String getConcepto() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getConcepto() != null)
			return this.pojoCajaChica.getConcepto();
		return "";
	}

	public void setConcepto(String concepto) {
		if (concepto != null && this.pojoCajaChica != null) 
			this.pojoCajaChica.setConcepto(concepto);
	}

	public String getComprobacionEgreso() {
		if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getIdConcepto() != null && this.pojoCajaChicaDetalle.getIdConcepto().getId() > 0L)
			return "<b>" + this.pojoCajaChicaDetalle.getIdConcepto().getId() + "</b> - " + this.pojoCajaChicaDetalle.getIdConcepto().getDescripcion();
		return "";
	}

	public void setComprobacionEgreso(String concepto) {}

	public String getCajaChicaObra() {
		if (this.pojoCajaChica != null && this.pojoCajaChica.getIdObra() != null && this.pojoCajaChica.getIdObra().getId() != null && this.pojoCajaChica.getIdObra().getId() > 0L)
			return "<b>" + this.pojoCajaChica.getIdObra().getId() + "</b> - " + this.pojoCajaChica.getIdObra().getNombre();
		return "";
	}

	public void setCajaChicaObra(String value) {}

	public long getComprobacionId() {
		if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getId() != null && this.pojoCajaChicaDetalle.getId() > 0L)
			return this.pojoCajaChicaDetalle.getId();
		return 0L;
	}

	public void setComprobacionId(long value) {}

	public Date getFechaComprobacion() {
		if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getFecha() != null)
			return this.pojoCajaChicaDetalle.getFecha();
		return Calendar.getInstance().getTime();
	}

	public void setFechaComprobacion(Date fecha) {
		if (this.pojoCajaChicaDetalle != null)
			this.pojoCajaChicaDetalle.setFecha(fecha);
	}

	public String getReferencia() {
		if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getReferencia() != null && ! "".equals(this.pojoCajaChicaDetalle.getReferencia().trim()))
			return this.pojoCajaChicaDetalle.getReferencia();
		return "";
	}

	public void setReferencia(String referencia) {
		if (this.pojoCajaChicaDetalle != null)
			this.pojoCajaChicaDetalle.setReferencia(referencia);
	}

	public String getTipoRazonSocial() {
		return tipoRazonSocial;
	}

	public void setTipoRazonSocial(String tipoRazonSocial) {
		this.tipoRazonSocial = tipoRazonSocial;
	}
	
	public String getRazonSocial() {
		if (this.pojoCajaChicaDetalle != null &&  this.pojoCajaChicaDetalle.getIdProveedor() != null &&  this.pojoCajaChicaDetalle.getIdProveedor().getId() > 0L)
			return this.pojoCajaChicaDetalle.getIdProveedor().getId() + " - " + this.pojoCajaChicaDetalle.getIdProveedor().getNombre();
		return "";
	}
	
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
		seleccionarRazonSocial();
	}
	
	public double getSubtotal() {
		if (this.pojoCajaChicaDetalle != null)
			return this.pojoCajaChicaDetalle.getSubtotal();
		return 0;
	}

	public void setSubtotal(double subtotal) {
		if (this.pojoCajaChicaDetalle != null)
			this.pojoCajaChicaDetalle.setSubtotal(subtotal);
	}

	public double getImpuestos() {
		if (this.pojoCajaChicaDetalle != null)
			return this.pojoCajaChicaDetalle.getTotalImpuestos();
		return 0;
	}

	public void setImpuestos(double impuestos) {
		if (this.pojoCajaChicaDetalle != null)
			this.pojoCajaChicaDetalle.setTotalImpuestos(impuestos);
	}

	public double getRetenciones() {
		if (this.pojoCajaChicaDetalle != null)
			return this.pojoCajaChicaDetalle.getTotalRetenciones();
		return 0;
	}

	public void setRetenciones(double retenciones) {
		if (this.pojoCajaChicaDetalle != null)
			this.pojoCajaChicaDetalle.setTotalRetenciones(retenciones);
	}

	public double getTotalComprobacion() {
		return totalComprobacion;
	}

	public void setTotalComprobacion(double totalComprobacion) {
		this.totalComprobacion = totalComprobacion;
	}

	public String getObservaciones() {
		if (this.pojoCajaChicaDetalle != null)
			return this.pojoCajaChicaDetalle.getObservaciones();
		return "";
	}

	public void setObservaciones(String observaciones) {
		if (this.pojoCajaChicaDetalle != null)
			this.pojoCajaChicaDetalle.setObservaciones(observaciones);
	}

	public List<FacturaImpuestos> getListComprobaciones() {
		return listComprobaciones;
	}

	public void setListComprobaciones(List<FacturaImpuestos> listComprobaciones) {
		this.listComprobaciones = listComprobaciones;
	}

	public List<PagosGastosDetImpuestosExt> getListDesgloseImpuestos() {
		return listDesgloseImpuestos;
	}

	public void setListDesgloseImpuestos(List<PagosGastosDetImpuestosExt> listDesgloseImpuestos) {
		this.listDesgloseImpuestos = listDesgloseImpuestos;
	}

	public boolean getVerDesgloce() {
		return (this.listDesgloseImpuestos != null && ! this.listDesgloseImpuestos.isEmpty());
	}
	
	public void setVerDesgloce(boolean value) {}
	
	public boolean getAgregarRetencion() {
		if (this.listDesgloseImpuestos != null && ! this.listDesgloseImpuestos.isEmpty())
			return ! this.existeRetencion; 
		return false;
	}

	public void setAgregarRetencion(boolean value) {}

	public int getIndexComprobacionEliminar() {
		return indexComprobacionEliminar;
	}

	public void setIndexComprobacionEliminar(int indexComprobacionEliminar) {
		this.indexComprobacionEliminar = indexComprobacionEliminar;
	}

	public FacturaImpuestos getPojoComprobacion() {
		return pojoComprobacion;
	}

	public void setPojoComprobacion(FacturaImpuestos pojoComprobacion) {
		this.pojoComprobacion = pojoComprobacion;
		/*try {
			this.pojoComprobacion = new FacturaImpuestos();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.pojoComprobacion, pojoComprobacion);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar asignar la comprobacion seleccionada. Procedo con asignacion directa", e);
			this.pojoComprobacion = new FacturaImpuestos(pojoComprobacion.getPojoFactura(), pojoComprobacion.getListImpuestos(), pojoComprobacion.getListRetenciones());
			this.pojoComprobacion.setSeleccionado(pojoComprobacion.isSeleccionado());
		}*/
	}
	
	public boolean getComprobacionConFactura() {
		return (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getFacturado() == 1);
	}
	
	public void setComprobacionConFactura(boolean value) {}

	public boolean getEgresoDetalleDescargarFactura() {
		return (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getId() != null && this.pojoCajaChicaDetalle.getId() > 0L && this.pojoCajaChicaDetalle.getIdCfdi() != null && this.pojoCajaChicaDetalle.getIdCfdi() > 0L);
	}
	
	public void setEgresoDetalleDescargarFactura(boolean value) {}
	
	public String getEgresoDetalleEmisor() {
		if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getNombreProveedor() != null && ! "".equals(this.pojoCajaChicaDetalle.getNombreProveedor()))
				return this.pojoCajaChicaDetalle.getNombreProveedor();
		return "";
	}
	
	public void setEgresoDetalleEmisor(String value) {}

	public String getEgresoDetalleEmisorRFC() {
		if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getFacturaRfc() != null && ! "".equals(this.pojoCajaChicaDetalle.getFacturaRfc()))
				return this.pojoCajaChicaDetalle.getFacturaRfc();
		return "";
	}
	
	public void setEgresoDetalleEmisorRFC(String value) {
		this.pojoCajaChicaDetalle.setFacturaRfc(value);
	}

	public String getEgresoDetalleFolio() {
		String folio = "";
		
		if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getFacturaFolio() != null && ! "".equals(this.pojoCajaChicaDetalle.getFacturaFolio()))
			folio = (this.pojoCajaChicaDetalle.getFacturaSerie() != null && ! "".equals(this.pojoCajaChicaDetalle.getFacturaSerie()) ? (this.pojoCajaChicaDetalle.getFacturaSerie() + "-") : "") + this.pojoCajaChicaDetalle.getFacturaFolio();
		return folio;
	}
	
	public void setEgresoDetalleFolio(String value) {}

	public Date getEgresoDetalleFecha() {
		if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getFecha() != null)
				return this.pojoCajaChicaDetalle.getFecha();
		return Calendar.getInstance().getTime();
	}
	
	public void setEgresoDetalleFecha(Date value) {}

	public double getEgresoDetalleFacturado() {
		if (this.pojoCajaChicaDetalle != null && this.pojoCajaChicaDetalle.getFacturaTotal() > 0)
			return this.pojoCajaChicaDetalle.getFacturaTotal();
		return 0;
	}

	public void setEgresoDetalleFacturado(double value) {}

	public String getEgresoDetalleMoneda() {
		if (this.pojoCajaChicaDetalle != null && ! "".equals(this.pojoCajaChicaDetalle.getFacturaMonedaTipoCambio().trim())) 
			return this.pojoCajaChicaDetalle.getFacturaMonedaTipoCambio();
		return "MXN";
	}

	public void setEgresoDetalleMoneda(double value) {}

	public boolean getEgresoDetalleManoObra() {
		if (this.pojoCajaChicaDetalle != null)
			return this.pojoCajaChicaDetalle.getManoObra();
		return false;
	}
	
	public void setEgresoDetalleManoObra(boolean manoObra) {
		if (this.pojoCajaChicaDetalle != null)
			this.pojoCajaChicaDetalle.setManoObra(manoObra);
	}

	public List<PagosGastosDetExt> getListComprobacionFacturaConceptos() {
		return listComprobacionFacturaConceptos;
	}

	public void setListComprobacionFacturaConceptos(List<PagosGastosDetExt> listComprobacionFacturaConceptos) {
		this.listComprobacionFacturaConceptos = listComprobacionFacturaConceptos;
	}

	public int getPaginacionFacturaConceptos() {
		return paginacionFacturaConceptos;
	} 
	
	public void setPaginacionFacturaConceptos(int paginacionFacturaConceptos) {
		this.paginacionFacturaConceptos = paginacionFacturaConceptos;
	}

	public boolean getMultiplesConceptos() {
		return (this.listComprobacionFacturaConceptos != null && this.listComprobacionFacturaConceptos.size() > 1);
	}
	
	public void setMultiplesConceptos(boolean value) {}
	
	public boolean isAgruparConceptos() {
		return agruparConceptos;
	}

	public void setAgruparConceptos(boolean agruparConceptos) {
		this.agruparConceptos = agruparConceptos;
	}

	public double getTotalFacturaConceptos() {
		return totalFacturaConceptos;
	}

	public void setTotalFacturaConceptos(double totalFacturaConceptos) {
		this.totalFacturaConceptos = totalFacturaConceptos;
	}
	
	public double getTotalComprobaciones() {
		return totalComprobaciones;
	}

	public void setTotalComprobaciones(double totalComprobaciones) {
		this.totalComprobaciones = totalComprobaciones;
	}

	public double getTotalImpuestos() {
		if (this.pojoCajaChicaDetalle != null)
			return this.pojoCajaChicaDetalle.getTotalImpuestos();
		return 0;
	}

	public void setTotalImpuestos(double totalImpuestos) {}

	public double getTotalRetenciones() {
		if (this.pojoCajaChicaDetalle != null)
			return this.pojoCajaChicaDetalle.getTotalRetenciones();
		return 0;
	}

	public void setTotalRetenciones(double totalRetenciones) {}

	public int getPaginacionComprobaciones() {
		return paginacionComprobaciones;
	}

	public void setPaginacionComprobaciones(int paginacionComprobaciones) {
		this.paginacionComprobaciones = paginacionComprobaciones;
	}

	public List<SelectItem> getListSucursalesItems() {
		return listSucursalesItems;
	}

	public void setListSucursalesItems(List<SelectItem> listSucursalesItems) {
		this.listSucursalesItems = listSucursalesItems;
	}

	public List<SelectItem> getListFormasPagosItems() {
		return listFormasPagosItems;
	}

	public void setListFormasPagosItems(List<SelectItem> listFormasPagosItems) {
		this.listFormasPagosItems = listFormasPagosItems;
	}

	public long getIdFormaPago() {
		return idFormaPago;
	}

	public void setIdFormaPago(long idFormaPago) {
		this.idFormaPago = idFormaPago;
	}

	public List<SelectItem> getListCuentasBancariasItems() {
		return listCuentasBancariasItems;
	}

	public void setListCuentasBancariasItems(List<SelectItem> listCuentasBancariasItems) {
		this.listCuentasBancariasItems = listCuentasBancariasItems;
	}

	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public List<ConValores> getListConceptoGasto() {
		return listConceptoGasto;
	}

	public void setListConceptoGasto(List<ConValores> listConceptoGasto) {
		this.listConceptoGasto = listConceptoGasto;
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

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public List<SelectItem> getTiposBusquedaObras() {
		return tiposBusquedaObras;
	}

	public void setTiposBusquedaObras(List<SelectItem> tiposBusquedaObras) {
		this.tiposBusquedaObras = tiposBusquedaObras;
	}

	public String getCampoBusquedaObra() {
		return campoBusquedaObra;
	}

	public void setCampoBusquedaObra(String campoBusquedaObra) {
		this.campoBusquedaObra = campoBusquedaObra;
	}

	public String getValorBusquedaObra() {
		return valorBusquedaObra;
	}

	public void setValorBusquedaObra(String valorBusquedaObra) {
		this.valorBusquedaObra = valorBusquedaObra;
	}

	public int getPaginacionObras() {
		return paginacionObras;
	}

	public void setPaginacionObras(int paginacionObras) {
		this.paginacionObras = paginacionObras;
	}

	public boolean isObrasAdministrativas() {
		return obrasAdministrativas;
	}

	public void setObrasAdministrativas(boolean obrasAdministrativas) {
		this.obrasAdministrativas = obrasAdministrativas;
	}

	public boolean getOperacionCancelada() {
		return operacionCancelada;
	}

	public void setOperacionCancelada(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

   	public boolean getPermiteEditar() {
   		if (this.pojoCajaChica != null && this.pojoCajaChica.getId() != null && this.pojoCajaChica.getId() > 0L)
   			return (this.perfilPermiteEditar || this.paramsRequest.containsKey("DETEDIT") && "1".equals(this.paramsRequest.get("DETEDIT")));
   		return true;
   	}
   	
   	public void setPermiteEditar(boolean value) {}
   	
	public boolean isPerfilRequiereObra() {
		return perfilRequiereObra;
	}

	public void setPerfilRequiereObra(boolean perfilRequiereObra) {
		this.perfilRequiereObra = perfilRequiereObra;
	}

	public boolean getPermiteAsignarCuentaBancaria() {
		return PERFIL_CAJA_CHICA_ABONOS;
	}

	public void setPermiteAsignarCuentaBancaria(boolean permiteAsignarCuentaBancaria) {
		this.PERFIL_CAJA_CHICA_ABONOS = permiteAsignarCuentaBancaria;
	} 

	public boolean getUsarAsignacionCuentaBancaria() {
		return usarAsignacionCuentaBancaria;
	}

	public void setUsarAsignacionCuentaBancaria(boolean usarAsignacionCuentaBancaria) {
		this.usarAsignacionCuentaBancaria = usarAsignacionCuentaBancaria;
	} 

	public boolean getPerfilDividirCajaChica() {
		return PERFIL_CAJA_CHICA_DIVIDIR;
	}

	public void setPerfilDividirCajaChica(boolean value) {
		this.PERFIL_CAJA_CHICA_DIVIDIR = value;
	}

	public boolean getPerfilCajaChicaVoBo() {
		return PERFIL_CAJA_CHICA_VOBO;
	}

	public void setPerfilCajaChicaVoBo(boolean value) {
		this.PERFIL_CAJA_CHICA_VOBO = value;
	}

	public boolean getPerfilCajaChicaAutorizacion() {
		return PERFIL_CAJA_CHICA_AUTORIZACION;
	}
	
	public void setPerfilCajaChicaAutorizacion(boolean value) {
		this.PERFIL_CAJA_CHICA_AUTORIZACION = value;
	}

	public double getLimiteCajaChica() {
		return this.PERFIL_CAJA_CHICA_LIMITE;
	}

	public void setLimiteCajaChica(double value) { }

	public boolean getPermisoAdmin() {
		return "ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());
	}
	
	public void setPermisoAdmin(boolean value) {}
	
	public boolean getPerfilAdministrativo() {
		return perfilEgresos;
	}
	
	public void setPerfilAdministrativo(boolean perfilEgresos) {
		this.perfilEgresos = perfilEgresos;
	}

	public boolean getBuscarAdministrativo() {
		return this.obrasAdministrativas;
	}
	
	public void setBuscarAdministrativo(boolean tipoObraBusquedaObra) {
		this.obrasAdministrativas = tipoObraBusquedaObra;
	}
	
	public int getTipoCajaChica() {
		return tipoCajaChica;
	}
	
	public void setTipoCajaChica(int tipoCajaChica) {
		this.tipoCajaChica = tipoCajaChica;
	}

	public List<ConValores> getListBusquedaRetenciones() {
		return listBusquedaRetenciones;
	}

	public void setListBusquedaRetenciones(List<ConValores> listBusquedaRetenciones) {
		this.listBusquedaRetenciones = listBusquedaRetenciones;
	}

	public ConValores getPojoBusquedaRetenciones() {
		return pojoBusquedaRetenciones;
	}

	public void setPojoBusquedaRetenciones(ConValores pojoBusquedaRetenciones) {
		this.pojoBusquedaRetenciones = pojoBusquedaRetenciones;
	}

	public List<SelectItem> getTiposBusquedaRetenciones() {
		return tiposBusquedaRetenciones;
	}

	public void setTiposBusquedaRetenciones(List<SelectItem> tiposBusquedaRetenciones) {
		this.tiposBusquedaRetenciones = tiposBusquedaRetenciones;
	}

	public String getCampoBusquedaRetenciones() {
		return campoBusquedaRetenciones;
	}

	public void setCampoBusquedaRetenciones(String campoBusquedaRetenciones) {
		this.campoBusquedaRetenciones = campoBusquedaRetenciones;
	}

	public String getValorBusquedaRetenciones() {
		return valorBusquedaRetenciones;
	}

	public void setValorBusquedaRetenciones(String valorBusquedaRetenciones) {
		this.valorBusquedaRetenciones = valorBusquedaRetenciones;
	}

	public int getPagBusquedaRetenciones() {
		return pagBusquedaRetenciones;
	}

	public void setPagBusquedaRetenciones(int pagBusquedaRetenciones) {
		this.pagBusquedaRetenciones = pagBusquedaRetenciones;
	}

	public PagosGastos getPojoCajaChicaCuenta() {
		return pojoCajaChicaCuenta;
	}

	public void setPojoCajaChicaCuenta(PagosGastos pojoCajaChicaCuenta) {
		this.pojoCajaChicaCuenta = pojoCajaChicaCuenta;
	}
}
