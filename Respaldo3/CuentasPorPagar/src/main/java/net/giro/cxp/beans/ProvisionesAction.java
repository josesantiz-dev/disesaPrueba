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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.Negocio;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.logica.PersonaRem;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.logica.OrdenCompraRem;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class ProvisionesAction implements Serializable {
	private static Logger log = Logger.getLogger(ProvisionesAction.class);
	private static final long serialVersionUID = 1L;
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
   	private HttpSession httpSession;
	private InitialContext ctx;
	// --------------------------------------------------------------------------
	private PagosGastosRem ifzProvision; 
	private PagosGastosDetRem ifzProvisionDetalle;
	private PagosGastosDetImpuestosRem ifzDesgloImpto;
	private ConGrupoValoresRem ifzGrupoValores;
	private ConValoresRem ifzConValores;
	private LocalidadDAO ifzLocalidad;
	private ColoniaDAO ifzColonia;
	private GastosImpuestoRem ifzGastoImpuesto;
	// --------------------------------------------------------------------------
	private List<EjercicioHolder> listProvisionesItems;
	private List<PagosGastos> listProvisiones;
	private long idProvision;
	private PagosGastosExt pojoProvision;
	private PagosGastosDetExt pojoProvisionDetalle;
	private String referenciaOperacion;
	private double totalComprobacion;
	private double totalComprobaciones;
	// ----------------------------------------------------------------------------
	private List<Persona> listBeneficiarios;
	private String beneficiario;
	private String beneficiarioRfcReferencia;
	// ----------------------------------------------------------------------------
	private String razonSocial;
	private String tipoRazonSocial;
	// Multiconceptos -----------------------------------------------------------
	private List<PagosGastosDetExt> listComprobacionFacturaConceptos;
	private double totalFacturaConceptos;
	private int paginacionFacturaConceptos;
	private int indexFacturaConcepto;
	private boolean agruparConceptos;
	private HashMap<String, List<PagosGastosDetImpuestosExt>> mapConceptoImpuestos;
	private boolean seleccionarTodo;
	// --------------------------------------------------------------------------
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
	// Carga XML ----------------------------------------------------------------
	private FtpRem ifzFtp;
	private byte[] fileSrc;
	private String fileName;
	private String ftpDigitalizacionRuta;
	private String prefijoFacturas;
	// Beneficiarios ------------------------------------------------------------
	private PersonaRem ifzPersonas;
	private ConGrupoValores grupoPersonas;
	// Sucursales ---------------------------------------------------------------
	private SucursalesRem ifzSucursal;
	private List<Sucursal> listSucursales;	
	private List<SelectItem> listSucursalesItems;
	// Formas de Pago -----------------------------------------------------------
	private ConGrupoValores grupoFormasPagos; 
	private List<ConValores> listFormasPagos;
	private List<SelectItem> listFormasPagosItems;
	private long idFormaPago;
	// Cuentas Bancarias
	private CuentasBancariasRem ifzCuentasBancarias;
	private List<CuentaBancaria> listCuentasBancarias;
	private List<SelectItem> listCuentasBancariasItems;
	// Busqueda Principal -------------------------------------------------------
	private List<SelectItem> tiposBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
   	private int numPagina;
	// Busqueda Retenciones -----------------------------------------------------
   	private ConGrupoValores grupoImpuestos;
	private List<ConValores> listBusquedaRetenciones;
	private ConValores pojoBusquedaRetenciones;
	private List<SelectItem> tiposBusquedaRetenciones;
	private String campoBusquedaRetenciones;
	private String valorBusquedaRetenciones;
	private int pagBusquedaRetenciones;
	// Busqueda CONCEPTOS -------------------------------------------------------
	private ConGrupoValores grupoEgresos;
	private List<ConValores> listConceptoGasto;
	private ConValores pojoConcepto;
	private List<SelectItem> tiposBusquedaConceptos;
	private String campoBusquedaConceptos;
	private String valorBusquedaConceptos;
	private int pagBusquedaConceptos;
	// Busqueda Obras -----------------------------------------------------------
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> tiposBusquedaObras;
	private String campoBusquedaObra;
	private String valorBusquedaObra;
	private int paginacionObras;
	public boolean obrasAdministrativas;
	// ORDEN DE COMPRA ----------------------------------------------------------
	private OrdenCompraRem ifzOrdenCompra;
	private List<OrdenCompra> listOrdenCompra;
	private OrdenCompra pojoOrdenCompra;
	private List<SelectItem> tiposBusquedaOrdenCompra;
	private String campoBusquedaOrdenCompra;
	private String valorBusquedaOrdenCompra;
	private int paginacionOrdenCompra;
	// CONTROL ------------------------------------------------------------------
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// EMPLEADO-USUARIO ---------------------------------------------------------
	private EmpleadoRem ifzEmpleado;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalSugerida;
	// PERFILES -----------------------------------------------------------------
	private boolean perfilEgresos;
	private boolean perfilPermiteEditar;
	private boolean perfilRequiereObra;
	// DEBUG --------------------------------------------------------------------
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public ProvisionesAction() {
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
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("REGISTRO_GASTOS_EDITAR");
			this.perfilPermiteEditar = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
	   		this.ctx = new InitialContext();
	   		this.ifzProvision = (PagosGastosRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosFac!net.giro.cxp.logica.PagosGastosRem");
			this.ifzProvisionDetalle = (PagosGastosDetRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetFac!net.giro.cxp.logica.PagosGastosDetRem");
			this.ifzDesgloImpto = (PagosGastosDetImpuestosRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//PagosGastosDetImpuestosFac!net.giro.cxp.logica.PagosGastosDetImpuestosRem");
	   		this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzOrdenCompra = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
	   		this.ifzGrupoValores = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
	   		this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
	   		this.ifzSucursal = (SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
	   		this.ifzCuentasBancarias = (CuentasBancariasRem) this.ctx.lookup("ejb:/Logica_TYG//CuentasBancariasFac!net.giro.tyg.logica.CuentasBancariasRem");
	   		this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
	   		this.ifzPersonas = (PersonaRem) this.ctx.lookup("ejb:/Logica_Clientes//PersonaFac!net.giro.clientes.logica.PersonaRem");
	   		this.ifzFtp = (FtpRem) this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzLocalidad = (LocalidadDAO) this.ctx.lookup("ejb:/Model_Publico//LocalidadImp!net.giro.plataforma.ubicacion.dao.LocalidadDAO");
			this.ifzColonia = (ColoniaDAO) this.ctx.lookup("ejb:/Model_Publico//ColoniaImp!net.giro.plataforma.ubicacion.dao.ColoniaDAO");	
	   		this.ifzGastoImpuesto = (GastosImpuestoRem) this.ctx.lookup("ejb:/Logica_CuentasPorPagar//GastosImpuestoFac!net.giro.cxp.logica.GastosImpuestoRem");
	   		
			this.ifzProvision.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProvisionDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenCompra.setInfoSesion(this.loginManager.getInfoSesion());
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
				log.warn("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
			
			// FORMAS DE PAGO
			this.grupoFormasPagos = this.ifzGrupoValores.findByName("SYS_FORMAS_PAGO");
			if (this.grupoFormasPagos == null || this.grupoFormasPagos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_FORMAS_PAGO en con_grupo_valores");

			// RELACIONES PERSONAS
			this.grupoPersonas = this.ifzGrupoValores.findById(4L); // valGpoPersonas
			if (this.grupoPersonas == null || this.grupoPersonas.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_RELPER en con_grupo_valores");

			// MOVIMIENTOS GASTOS
			this.grupoEgresos = this.ifzGrupoValores.findByName("SYS_MOVGTOS");
			if (this.grupoEgresos == null || this.grupoEgresos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");

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
			
			// Busqueda Orden de Compra
			this.tiposBusquedaOrdenCompra = new ArrayList<SelectItem>();
			this.tiposBusquedaOrdenCompra.add(new SelectItem("*", "Concidencia"));
			this.tiposBusquedaOrdenCompra.add(new SelectItem("folio", "Folio"));
			this.tiposBusquedaOrdenCompra.add(new SelectItem("nombreProveedor", "Proveedor"));
			this.tiposBusquedaOrdenCompra.add(new SelectItem("nombreSolicita", "Solicita"));
			this.tiposBusquedaOrdenCompra.add(new SelectItem("nombreContacto", "Contacto"));
			this.tiposBusquedaOrdenCompra.add(new SelectItem("id", "ID"));
			nuevaBusquedaOrdenCompra();

			// Busqueda Retenciones
			this.tiposBusquedaRetenciones = new ArrayList<SelectItem>();
			this.tiposBusquedaRetenciones.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaRetenciones.add(new SelectItem("valor", "Cuenta Contable"));
			this.tiposBusquedaOrdenCompra.add(new SelectItem("id", "ID"));
			nuevaBusquedaRetenciones();
			
			this.prefijoFacturas = "CXP-RG-"; 
			this.listSucursalesItems = new ArrayList<SelectItem>();
			this.listFormasPagosItems = new ArrayList<SelectItem>();
			this.listCuentasBancariasItems = new ArrayList<SelectItem>();
			control();
		} catch (Exception e) {
			log.error("Error al inicializar " +  this.getClass().getCanonicalName(), e);
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
			this.valorBusqueda = this.valorBusqueda.trim().replace(" ", "%");
   			if ("fecha".equals(this.campoBusqueda)) {
   				this.valorBusqueda = "";
   				orderBy = "id desc";
   			} 

   			this.listProvisionesItems = new ArrayList<EjercicioHolder>();
			this.ifzProvision.setInfoSesion(this.loginManager.getInfoSesion());
			this.listProvisiones = this.ifzProvision.findProvisionLikeProperty(this.campoBusqueda, ("fecha".equals(this.campoBusqueda) ? this.fechaBusqueda : this.valorBusqueda), "", true, orderBy, 0);
			if (this.listProvisiones == null || this.listProvisiones.isEmpty()) {
				control(13, "Busqueda sin resultados");
				return;
			}

			this.listProvisionesItems = this.ifzProvision.encapsularProvisiones(this.listProvisiones);
			if (this.listProvisionesItems == null || this.listProvisionesItems.isEmpty())
				control(-1, "Ocurrio un problema al agrupar las Provisiones");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Provisiones", e);
		}
   	}
   	
   	public void nuevo() {
		try {
   			control();
			this.beneficiario = "";
   			this.idProvision = 0L;
			this.referenciaOperacion = "";
			this.beneficiarioRfcReferencia = "";
			this.cargaComprobaciones = false;
			this.paginacionComprobaciones = 1;
   			this.listComprobaciones = new ArrayList<FacturaImpuestos>();
   			this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>();
			this.pojoProvision = new PagosGastosExt();
			this.pojoProvision.setTipoBeneficiario("N");

			cargarSucursales();
			cargarFormasDePago();
			cargarCuentasBancarias();
			setIdSucursal(this.idSucursalSugerida);
		} catch (Exception e) {
			control("Ocurrio un problema al inicializar una nueva Provision", e);
		}
	}

   	public void editar() {
   		try {
   			control();
   			this.pojoProvision = this.ifzProvision.findExtById(this.idProvision);
   			if (this.pojoProvision == null || this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L) {
   				control(-1, "Ocurrio un problema al intentar recuperar la Provision indicada");
   				return;
   			}

			cargarSucursales();
			cargarFormasDePago();
			cargarCuentasBancarias();
			
			this.referenciaOperacion = "";
   			if ("C".equals(this.pojoProvision.getOperacion())) 
   				this.referenciaOperacion = (this.pojoProvision.getNoCheque() != null ? this.pojoProvision.getNoCheque().toString() : ""); 
			else if ("T".equals(this.pojoProvision.getOperacion())) 
				this.referenciaOperacion = (this.pojoProvision.getFolioAutorizacion() != null ? this.pojoProvision.getFolioAutorizacion() : ""); 

			this.beneficiario = ""; 
			this.beneficiarioRfcReferencia = ""; 
			if (this.pojoProvision != null && this.pojoProvision.getIdBeneficiario() != null && this.pojoProvision.getIdBeneficiario().getId() > 0L) 
				this.beneficiario = this.pojoProvision.getIdBeneficiario().getId() + " - " + this.pojoProvision.getBeneficiario(); 

			this.cargaComprobaciones = true;
			this.paginacionComprobaciones = 1; 
			this.listComprobaciones = new ArrayList<FacturaImpuestos>(); 
			this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>(); 
			this.totalComprobaciones = this.pojoProvision.getMonto().doubleValue();
			//cargarComprobantes();
			/*this.beneficiario = "";
			this.beneficiarioRfcReferencia = "";
			if (this.pojoProvision != null && this.pojoProvision.getIdBeneficiario() != null && this.pojoProvision.getIdBeneficiario().getId() > 0L) {
				this.beneficiario = this.pojoProvision.getIdBeneficiario().getId() + " - " + this.pojoProvision.getBeneficiario();
				proveedor = getPersonaNegocio(this.pojoProvision.getIdBeneficiario().getId(), this.pojoProvision.getTipoBeneficiario());
			}

			this.paginacionComprobaciones = 1;
   			this.listComprobaciones = new ArrayList<FacturaImpuestos>();
   			this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>();
   			listDetalles = this.ifzProvisionDetalle.findExtAll(this.idProvision); //.findByPagosGastosExt(this.pojoProvision, 0);
   			if (listDetalles != null && ! listDetalles.isEmpty()) {
   	   			for (PagosGastosDetExt detalle : listDetalles) {
   					if (proveedor != null && proveedor.getId() > 0L) {
   						if (detalle.getIdProveedor() == null)
   							detalle.setIdProveedor(proveedor);
   						if (proveedor.getId() == detalle.getIdProveedor().getId()) {
	   						detalle.setFacturaRfc(proveedor.getRfc());
	   						detalle.setNombreProveedor(proveedor.getNombre());
	   						detalle.setTipoPersonaProveedor(this.pojoProvision.getTipoBeneficiario()); 
   						}
   					}
   					
   					listImpuestos = getImpuestosByIdPagosGastosDetalle(detalle.getId(), null, null);
   					impuestos = getImpuestosByIdPagosGastosDetalle(0L, listImpuestos, "DE");
   					retenciones = getImpuestosByIdPagosGastosDetalle(0L, listImpuestos, "AC");

   					//impuestos = copiaListas(this.listDesgloseImpuestos);
   	   				this.listComprobaciones.add(new FacturaImpuestos(detalle, impuestos, retenciones));
   					
					/*try {
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
					}* /
   	   			}
   			}
   			
   			actualizaTotalComprobado();
			indexarComprobaciones();*/
   		} catch (Exception e) {
			control("Ocurrio un problema al intentar editar la Provision indicada", e);
   		}
   	}
   	
   	public void guardar() {
		boolean registroNuevo = false;
		Respuesta resp = null;
		PagosGastos pagosGastos = null;
		// -----------------------------
		boolean borrarXML = false;
		Long facturaId = 0L;
		String fileName = "";
		
		try {
			control();
			log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Validaciones ... ");
			if (! validaciones()) {
				log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Validaciones ... FAIL");
				return;
			}
			
			log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Validaciones ... OK");
			if (this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L)
				registroNuevo = true;

			this.pojoProvision.setMonto(this.totalComprobaciones);
			this.pojoProvision.setTipo("F");//provision de facturas
			this.pojoProvision.setEstatus("C");//comprobado
			this.pojoProvision.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			if (registroNuevo) {
				this.pojoProvision.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoProvision.setFechaCreacion(Calendar.getInstance().getTime());
			}
			this.pojoProvision.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoProvision.setFechaModificacion(Calendar.getInstance().getTime());
			
			log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Guardando ... ");
			this.ifzProvision.setInfoSesion(this.loginManager.getInfoSesion());
			resp = this.ifzProvision.salvar(this.pojoProvision);
			if (resp.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al guardar la Provision.\n" + resp.getErrores().getDescError());
				return;
			}
			
			this.pojoProvision = (PagosGastosExt) resp.getObjeto();
			this.idProvision = this.pojoProvision.getId();
			if (resp.getBody().getValor("entity") != null)
				pagosGastos = (PagosGastos) resp.getBody().getValor("entity");
			if (pagosGastos == null || pagosGastos.getId() == null || pagosGastos.getId() <= 0L)
				pagosGastos = this.ifzProvision.findById(this.idProvision);
			log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Guardando ... OK");
			
			// Guardamos las comprobacion del provision si corresponde
			if (! this.cargaComprobaciones) {
				log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Guardando comprobaciones ... ");
				this.ifzProvisionDetalle.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
				for (FacturaImpuestos comprobacion : this.listComprobaciones) {
					// Guardo detalle
					comprobacion.getPojoFactura().setIdPagosGastos(this.pojoProvision);
					if (comprobacion.getPojoFactura().getId() != null && comprobacion.getPojoFactura().getId() > 0L) 
						this.ifzProvisionDetalle.update(comprobacion.getPojoFactura());
					else 
						comprobacion.getPojoFactura().setId(this.ifzProvisionDetalle.save(comprobacion.getPojoFactura()));
					
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
				log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Guardando comprobaciones ... OK");
			}
			
			// Añadimos a listado
			/*log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Añadimos a listado ");
			if (this.listProvisiones == null || this.listProvisiones.isEmpty()) {
				if (this.listProvisiones == null)
					this.listProvisiones = new ArrayList<PagosGastos>();
				this.listProvisiones.add(pagosGastos);
			} else {
				for (PagosGastos var : this.listProvisiones) {
					if (pagosGastos.getId().longValue() == var.getId().longValue()) {
						BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
						BeanUtils.copyProperties(var, pagosGastos);
						break;
					}
				}
			}*/
			
			// Borramos facturas previamente eliminadas
			if (this.listComprobacionesEliminadas != null && ! this.listComprobacionesEliminadas.isEmpty()) {
				log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Borramos comprobaciones previamente eliminadas ... ");
				for (FacturaImpuestos var : this.listComprobacionesEliminadas) {
					if (var.getPojoFactura().getIdCfdi() == null || var.getPojoFactura().getIdCfdi() <= 0L) {
						this.ifzProvisionDetalle.delete(var.getPojoFactura().getId());
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
					log.info("Eliminando XML " + facturaId);
		    		this.ifzProvisionDetalle.eliminarFactura(facturaId);
					
					// Lo elimino fisicamente del servidor
					fileName = this.prefijoFacturas + var.getPojoFactura().getIdCfdi() + ".xml";
					if (! this.ifzFtp.delArchivo(this.ftpDigitalizacionRuta + fileName))
						log.warn("ERROR FTP - No se pudo Borrar la factura del servidor, ID: " + facturaId);
					
					// Borro detalle (factura)
					this.ifzProvisionDetalle.delete(var.getPojoFactura().getId());
					facturaId = 0L;
				}
				
				this.listComprobacionesEliminadas.clear();
				log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Borramos comprobaciones previamente eliminadas ... OK");
			}

			// Borramos facturas previamente eliminadas
			if (this.listImpuestosEliminadas != null && ! this.listImpuestosEliminadas.isEmpty()) {
				log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Borramos impuestos de comprobaciones previamente eliminados ... OK");
				for (FacturaImpuestos var : this.listComprobacionesEliminadas) {
					if (var.getPojoFactura().getIdCfdi() == null || var.getPojoFactura().getIdCfdi() <= 0L) {
						this.ifzProvisionDetalle.delete(var.getPojoFactura().getId());
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
					log.info("Eliminando XML " + facturaId);
		    		this.ifzProvisionDetalle.eliminarFactura(facturaId);
					
					// Lo elimino fisicamente del servidor
					fileName = this.prefijoFacturas + var.getPojoFactura().getIdCfdi() + ".xml";
					if (! this.ifzFtp.delArchivo(this.ftpDigitalizacionRuta + fileName))
						log.warn("ERROR FTP - No se pudo Borrar la factura del servidor, ID: " + facturaId);
					
					// Borro detalle (factura)
					this.ifzProvisionDetalle.delete(var.getPojoFactura().getId());
					facturaId = 0L;
				}
				
				this.listImpuestosEliminadas.clear();
				log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadando Provision. Borramos impuestos de comprobaciones previamente eliminados ... OK");
			}
			
			// ENVIAMOS MENSAJE A TRANSACCIONES
			mensajeTransaccion();
			log.info("[" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "] Guadado Provision completo");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Provision", e);
			log.info("Guardando Provision ... ERROR");
		}
   	}
   	
	public void cancelar() {
   		Respuesta respuesta = new Respuesta();
   		
		try {
			control();
			this.pojoProvision = this.ifzProvision.findExtById(this.idProvision);
   			if (this.pojoProvision == null || this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L) {
   				control(-1, "Ocurrio un problema al intentar recuperar la Provision indicada");
   				return;
   			}
   			
   			log.info("Cancelando RE" + this.idProvision + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			this.ifzProvision.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzProvision.cancelacion(this.pojoProvision, Calendar.getInstance().getTime());
			if (respuesta.getResultado() != 0) {
				control(-1, respuesta.getRespuesta());
	   			log.info("Cancelando RE" + this.idProvision + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getId() + " ... ERROR");
				return;
			}
			
			for (PagosGastos registroEgreso : this.listProvisiones) {
				if (registroEgreso.getId().longValue() == this.pojoProvision.getId().longValue()) {
					registroEgreso.setEstatus("X");
					break;
				}
			}
   			
   			log.info("Cancelando RE" + this.idProvision + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getId() + " ... OK");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Cancelar la Provision indicada.", e);
   			log.info("Cancelando RE" + this.idProvision + " :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getId() + " ... ERROR");
		}
	}
	
	public void mensajeTransaccion() {
		try {
			// Enviamos mensaje a cola de transacciones
			log.info("Enviando Transaccion ... ");
			this.ifzProvision.contabilizador(this.idProvision);
			log.info("Transaccion enviada");
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
			
			if (this.idProvision != this.pojoProvision.getId().longValue()) 
	   			this.pojoProvision = this.ifzProvision.findExtById(this.idProvision);
   			if (this.pojoProvision == null || this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L) {
   				control(-1, "Ocurrio un problema al intentar recuperar la Caja Chica indicada");
   				return;
   			}
   			
			this.paginacionComprobaciones = 1; 
			this.listComprobaciones = new ArrayList<FacturaImpuestos>(); 
			this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>(); 
			if (this.pojoProvision.getIdBeneficiario() != null && this.pojoProvision.getIdBeneficiario().getId() > 0L) {
				proveedor = this.pojoProvision.getIdBeneficiario();
				if ((proveedor == null || proveedor.getId() <= 0L) && this.pojoProvision.getIdBeneficiario() != null && this.pojoProvision.getIdBeneficiario().getId() > 0L)
					proveedor = getPersonaNegocio(this.pojoProvision.getIdBeneficiario().getId(), this.pojoProvision.getTipoBeneficiario()); 
			}
			
			listDetalles = this.ifzProvisionDetalle.findExtAll(this.idProvision); 
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
		if (this.pojoProvision.getId() == null || this.pojoProvision.getId() <= 0L)
			registroNuevo = true;
		if (this.pojoProvision.getOperacion() == null)
			this.pojoProvision.setOperacion("");
		this.pojoProvision.setNoCheque(0);
		this.pojoProvision.setFolioAutorizacion("");
		if (this.pojoProvision.getIdObra() == null)
			this.pojoProvision.setIdObra(new Obra());
		if (this.pojoProvision.getIdOrdenCompra() <= 0L)
			this.pojoProvision.setIdOrdenCompra(0L);
		
		if (this.pojoProvision.getIdSucursal() == null || this.pojoProvision.getIdSucursal().getId() <= 0L) {
			control(-1, "Debe seleccionar una Sucursal");
			return false;
		}
		
		// BENEFICIARIO
		// -----------------------------------------------------------------------------------------------------
		if (this.pojoProvision.getIdBeneficiario() == null || this.pojoProvision.getIdBeneficiario().getId() <= 0L) {
			idAuxiliar = validateToID(this.beneficiario, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			proveedor = this.ifzProvision.findPersonaById(idAuxiliar, this.pojoProvision.getTipoBeneficiario());
			if (proveedor == null || proveedor.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Beneficiario seleccionado");
				return false;
			}
			
			if (this.pojoProvision.getIdBeneficiario() == null || this.pojoProvision.getIdBeneficiario().getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Beneficiario seleccionado");
				return false;
			}
			
			this.pojoProvision.setIdBeneficiario(proveedor);
			this.pojoProvision.setBeneficiario(proveedor.getNombre());
		}
		
		if ("C".equals(this.pojoProvision.getOperacion().trim()) && ! NumberUtils.isNumber(this.referenciaOperacion)) {
			control(-1, "Referencia no valida para Cheque, debe ser de solo Numeros");
			return false;
		}

		if ("C".equals(this.pojoProvision.getOperacion())) 
			this.pojoProvision.setNoCheque(Integer.parseInt(this.referenciaOperacion.trim()));
		else  
			this.pojoProvision.setFolioAutorizacion(this.referenciaOperacion.trim());

		if (this.listComprobaciones == null || this.listComprobaciones.isEmpty()) {
			if (registroNuevo || (! registroNuevo && ! this.cargaComprobaciones)) {
				control(2, "Debe indicar al menos una factura");
				return false;
			}
		}

		if (! validaSaldoCuentaPropios()) {
			return false;
		}
		
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
				log.info("Usuario sin Empleado asociado o No se encontro recuperar el Empleado asociado");
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
			mensaje = "Ocurrio un problema al realizar la operacion";
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		
		log.error("\n\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: REGISTRO DE EGRESOS :: " + this.tipoMensaje + " - " + mensaje, throwable);
	}

	// ------------------------------------------------------------------------------------------
	// BENEFICIARIO
	// ------------------------------------------------------------------------------------------
	
	public void cambioBeneficiario() {
		this.beneficiario = "";
		this.beneficiarioRfcReferencia = "";
	}
	
	public List<Persona> autoacompletaBeneficiario(Object valorBusqueda) {
		try {
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			return this.ifzPersonas.findLikeClaveNombre("nombre", valorBusqueda.toString(), this.grupoPersonas, getTipoBeneficiario(), 100, false);
		} catch (Exception e) {
			control("Ocurrio un problema al consultas las Personas/Negocios", e);
			return new ArrayList<Persona>();
		}
	}

	private void seleccionarBeneficiario() {
		PersonaExt beneficiario = null;
		long idBeneficiario = 0L;

		if (this.beneficiario == null || "".equals(this.beneficiario.trim()))
			return;

		idBeneficiario = validateToID(this.beneficiario, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
		beneficiario = getPersonaNegocio(idBeneficiario, this.pojoProvision.getTipoBeneficiario()); //this.ifzProvision.findPersonaById(idBeneficiario, this.pojoProvision.getTipoBeneficiario()); //getPersonaNegocio(idAuxiliar, this.pojoCajaChica.getTipoBeneficiario());
		if (beneficiario != null && beneficiario.getId() > 0L) {
			this.pojoProvision.setIdBeneficiario(beneficiario);
			this.pojoProvision.setBeneficiario(beneficiario.getNombre());
			//this.pojoProvision.setTipoBeneficiario("P");
			//this.beneficiarioRfcReferencia = proveedor.getRfc();
		}
	}
	
	private PersonaExt getPersonaNegocio(long idPersonaNegocio, String tipoPersona) {
		try {
			if (idPersonaNegocio <= 0L || (tipoPersona == null || "".equals(tipoPersona.trim())))
				return null;
			tipoPersona = ((tipoPersona == null || "".equals(tipoPersona.trim())) ? "N" : tipoPersona);
			return this.ifzProvision.findPersonaById(idPersonaNegocio, tipoPersona);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la Persona/Negocio indicado: " + idPersonaNegocio + tipoPersona, e);
			return null;
		}
	}

	// ------------------------------------------------------------------------------------------
	// RAZON SOCIAL
	// ------------------------------------------------------------------------------------------
	
	public void cambioRazonSocial() {
		this.razonSocial = "";
		if (this.pojoProvisionDetalle != null) {
			this.pojoProvisionDetalle.setFacturaRfc("");
			this.pojoProvisionDetalle.setIdProveedor(null);
		}
	}
	
	public List<Persona> autoacompletaRazonSocial(Object valorBusqueda) {
		String busqueda = "";
		
		try {
			busqueda = (valorBusqueda != null ? valorBusqueda.toString() : "");
			busqueda = validateToDesc(busqueda, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			log.info("Buscando Razon Social: [" + this.tipoRazonSocial + "] " + busqueda);
			this.ifzPersonas.setInfoSesion(this.loginManager.getInfoSesion());
			return this.ifzPersonas.findLikeClaveNombre("nombre", busqueda, this.grupoPersonas, this.tipoRazonSocial, 100, false);
		} catch (Exception e) {
			log.info("Buscando Razon Social: [" + this.tipoRazonSocial + "] " + busqueda + " <<< ERROR ");
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
			
			log.info("Seleccionando Razon Social: [" + this.tipoRazonSocial + "] " + this.razonSocial);
			idProveedor = validateToID(this.razonSocial, "(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
			proveedor = this.ifzProvision.findPersonaById(idProveedor, this.tipoRazonSocial); 
			if (proveedor != null && proveedor.getId() > 0L) {
				this.pojoProvisionDetalle.setIdProveedor(proveedor);
				this.pojoProvisionDetalle.setFacturaRfc(proveedor.getRfc());
				this.pojoProvisionDetalle.setProveedor(proveedor.getNombre());
				this.pojoProvisionDetalle.setTipoPersonaProveedor(this.tipoRazonSocial);
			}
		} catch (Exception e) {
			log.info("Seleccionando Razon Social: [" + this.tipoRazonSocial + "] " + this.razonSocial + " <<< ERROR ");
			control("Ocurrio un problema al recuperar la Razon Social indicada", e);
		} 
	}
	
	// ------------------------------------------------------------------------------------------
	// COMPROBACIONES
	// ------------------------------------------------------------------------------------------
	
	public void nuevaComprobacionConFactura() {
		try {
			control();
			nuevaFactura();
			this.idEgresoPrevio = 0L;
			this.pojoComprobacion = new FacturaImpuestos();
			this.pojoProvisionDetalle = new PagosGastosDetExt();
			this.pojoProvisionDetalle.generarUniqueValue();
			this.pojoProvisionDetalle.setFacturado(1);
			this.pojoProvisionDetalle.setFecha(Calendar.getInstance().getTime());
			this.pojoProvisionDetalle.setRfcProveedor("");
			this.pojoProvisionDetalle.setReferencia("");
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.totalComprobacion = 0;
			desglosaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al validar la Comprobacion indicada", e);
		} finally {
			this.indexComprobacionEliminar = -1;
			if (this.listComprobacionesEliminadas == null)
				this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>();
			this.mapConceptoImpuestos = null;
		}
	}

	public void nuevaComprobacionSinFactura() {
		PersonaExt proveedor = null;
		
		try {
			control();
			nuevaFactura();
			this.idEgresoPrevio = 0L;
			this.pojoComprobacion = new FacturaImpuestos();
			this.pojoProvisionDetalle = new PagosGastosDetExt();
			this.pojoProvisionDetalle.generarUniqueValue();
			this.pojoProvisionDetalle.setFacturado(0);
			this.pojoProvisionDetalle.setFecha(Calendar.getInstance().getTime());
			this.pojoProvisionDetalle.setRfcProveedor("");
			this.pojoProvisionDetalle.setReferencia("");
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			this.totalComprobacion = 0;
			this.tipoRazonSocial = "N";
			desglosaImpuestos();
			cambioRazonSocial();
			
			// Autoasignamos Proveedor si corresponde
			if (this.pojoProvision.getIdBeneficiario() != null && this.pojoProvision.getIdBeneficiario().getId() > 0L) {
				proveedor = getPersonaNegocio(this.pojoProvision.getIdBeneficiario().getId(), this.pojoProvision.getTipoBeneficiario());
				if (proveedor != null && proveedor.getId() > 0L) {
					this.pojoProvisionDetalle.setIdProveedor(proveedor);
					this.pojoProvisionDetalle.setFacturaRfc(proveedor.getRfc());
					this.pojoProvisionDetalle.setNombreProveedor(proveedor.getNombre());
					this.pojoProvisionDetalle.setTipoPersonaProveedor(this.pojoProvision.getTipoBeneficiario());
					this.razonSocial = proveedor.getId() + " - " + proveedor.getNombre();
					this.tipoRazonSocial = this.pojoProvision.getTipoBeneficiario();
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al validar la Comprobacion indicada", e);
		} finally {
			this.indexComprobacionEliminar = -1;
			if (this.listComprobacionesEliminadas == null)
				this.listComprobacionesEliminadas = new ArrayList<FacturaImpuestos>();
			this.mapConceptoImpuestos = null;
		}
	}

	public void editarComprobacion() {
		try {
			control();
			if (this.pojoComprobacion == null || this.pojoComprobacion.getPojoFactura() == null) {
				control(-1, "Ocurrio un problema al recuperar la Comprobacion indicada");
				return;
			}

			this.pojoProvisionDetalle = null;
			this.pojoComprobacion.setEditando(true);
			this.pojoProvisionDetalle = this.pojoComprobacion.getPojoFactura();
			this.pojoProvisionDetalle.generarUniqueValue();
			/*if (! this.pojoProvisionDetalle.validarUniqueValue())*/
				this.pojoProvisionDetalle.generarUniqueValue();
			this.tipoRazonSocial = this.pojoProvisionDetalle.getTipoPersonaProveedor();
			this.totalComprobacion = this.pojoProvisionDetalle.getTotal();
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
	
	public void evaluaEliminar() {
		try {
			control();
			if (this.pojoComprobacion == null || this.pojoComprobacion.getPojoFactura() == null) {
				control(-1, "Ocurrio un problema al recuperar la Comprobacion indicada");
				return;
			}
			
			if (this.pojoComprobacion.getPojoFactura().getId() != null && this.pojoComprobacion.getPojoFactura().getId() > 0L) {
				if (! this.perfilPermiteEditar && ! validaRequest("DETEDIT", "1")) { 
		   			control(3, "No tiene permitido eliminar comprobaciones");
					return;
		   		}
			} 
		} catch (Exception e) {
			control("Ocurrio un problema al validar la Comprobacion indicada", e);
		}
	}
	
	public void guardarComprobacion() {
		int factor = 1;
		
		try {
			control();
			if (! validarComprobacion())
				return;

			// Comprobamos si es un egreso y asignamos en negativo
			if (this.pojoProvisionDetalle.getFacturaTipo() != null && ("E".equals(this.pojoProvisionDetalle.getFacturaTipo()) || "EGRESO".equals(this.pojoProvisionDetalle.getFacturaTipo().trim().toUpperCase()))) 
				factor = -1;
			this.pojoProvisionDetalle.setSubtotal((this.pojoProvisionDetalle.getSubtotal() * factor));
			this.pojoProvisionDetalle.setTotalImpuestos((this.pojoProvisionDetalle.getTotalImpuestos() * factor));
			this.pojoProvisionDetalle.setTotalRetenciones((this.pojoProvisionDetalle.getTotalRetenciones() * factor));
			this.pojoProvisionDetalle.setTotal((this.pojoProvisionDetalle.getTotal() * factor));
			
			if (this.pojoProvisionDetalle.getId() == null || this.pojoProvisionDetalle.getId() <= 0L) {
				this.pojoProvisionDetalle.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoProvisionDetalle.setFechaCreacion(Calendar.getInstance().getTime());
			}
			this.pojoProvisionDetalle.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			this.pojoProvisionDetalle.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.pojoComprobacion.setPojoFactura(this.pojoProvisionDetalle);
			this.pojoComprobacion.setListImpuestos(copiaListas(this.listDesgloseImpuestos));
			this.pojoComprobacion.setTotalImpuestos(this.pojoProvisionDetalle.getTotalImpuestos());
			this.pojoComprobacion.setTotalRetenciones(this.pojoProvisionDetalle.getTotalRetenciones());
			
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

	public void enviar() {
		PagosGastosExt registroEgreso = null;
		PagosGastosDetExt factura = null;
		List<FacturaImpuestos> seleccionados = null; 
		List<PagosGastosDetImpuestosExt> auxiliar = null;
		List<PagosGastosDetImpuestosExt> impuestos = null;
		List<PagosGastosDetImpuestosExt> retenciones = null;
		double montoRegEgreso = 0;
		
		try {
   			control();
			if (! validarEnvio())
				return;
			
			// Generamos Registro de Egreso
			// -------------------------------------------------------------------------------------------------------
			registroEgreso = new PagosGastosExt();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(registroEgreso, this.pojoProvision);
			registroEgreso.setId(0L);
			registroEgreso.setTipo("P");
			registroEgreso.setEstatus("C"); // comprobado
			registroEgreso.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			registroEgreso.setFechaCreacion(Calendar.getInstance().getTime());
			registroEgreso.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			registroEgreso.setFechaModificacion(Calendar.getInstance().getTime());
			// Guardamos como Registro de Egreso
			this.ifzProvision.setInfoSesion(this.loginManager.getInfoSesion());
			registroEgreso.setId(this.ifzProvision.save(registroEgreso));
			
			// Generamos detalles de Registro de Egreso
			seleccionados = new ArrayList<FacturaImpuestos>();
			this.ifzProvisionDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			for (FacturaImpuestos comprobado : this.listComprobaciones) { 
				if (! comprobado.isSeleccionado()) 
					continue; 
				
				factura = new PagosGastosDetExt(); 
				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0); 
				BeanUtils.copyProperties(factura, comprobado.getPojoFactura()); 
				auxiliar = getImpuestosByIdPagosGastosDetalle(factura.getId(), null, null);
				impuestos = getImpuestosByIdPagosGastosDetalle(0L, auxiliar, "DE");
				retenciones = getImpuestosByIdPagosGastosDetalle(0L, auxiliar, "AC");
				seleccionados.add(new FacturaImpuestos(factura, impuestos, retenciones)); 
				
				// Marcamos lo compronado de Provision como enviado a Registro de Egresos (Gastos) 
				comprobado.setSeleccionado(false); 
				comprobado.getPojoFactura().setEstatus("E"); 
				comprobado.getPojoFactura().setFechaModificacion(Calendar.getInstance().getTime()); 
				comprobado.getPojoFactura().setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId()); 
				this.ifzProvisionDetalle.update(comprobado.getPojoFactura()); 
			}
			
			if (seleccionados == null || seleccionados.isEmpty())
				return; 
			
			this.ifzProvisionDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			for (FacturaImpuestos seleccionado : seleccionados) {
				// Instanciamos el detalle para el Registro de Egreso (Gasto)
				seleccionado.getPojoFactura().setId(0L);
				seleccionado.getPojoFactura().setIdPagosGastos(registroEgreso);
				seleccionado.getPojoFactura().setEstatus("");
				seleccionado.getPojoFactura().setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				seleccionado.getPojoFactura().setFechaCreacion(Calendar.getInstance().getTime());
				seleccionado.getPojoFactura().setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				seleccionado.getPojoFactura().setFechaModificacion(Calendar.getInstance().getTime());
				// Guardamos
				seleccionado.getPojoFactura().setId(this.ifzProvisionDetalle.save(seleccionado.getPojoFactura()));
				
				if (! seleccionado.getListImpuestos().isEmpty()) {
					this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
					for (PagosGastosDetImpuestosExt impuesto : seleccionado.getListImpuestos()) {
						impuesto.setId(0L);
						impuesto.setIdPagosGastosDet(seleccionado.getPojoFactura());
						impuesto.setId(this.ifzDesgloImpto.save(impuesto));
					}
				}				
				
				if (! seleccionado.getListRetenciones().isEmpty()) {
					this.ifzDesgloImpto.setInfoSesion(this.loginManager.getInfoSesion());
					for (PagosGastosDetImpuestosExt retencion : seleccionado.getListRetenciones()) {
						retencion.setId(0L);
						retencion.setIdPagosGastosDet(seleccionado.getPojoFactura());
						retencion.setId(this.ifzDesgloImpto.save(retencion));
					}
				}
				
				montoRegEgreso += seleccionado.getPojoFactura().getTotal();
			}
			
			registroEgreso.setMonto(montoRegEgreso);
			this.ifzProvision.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProvision.update(registroEgreso);
			control(false, -1, "Provisiones Enviadas", null);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar enviar las Comprobaciones seleccionadas al Registro de Egreso", e);
		}
	}
	
   	public void actualizaTotalComprobado() {
		double conFactura = 0;
		double sinFactura = 0;
   		double monto = 0;
		
		try {
			this.totalComprobaciones = monto;
			this.pojoProvision.setConFactura(monto);
			this.pojoProvision.setSinFactura(monto);
			this.pojoProvision.setMonto(this.totalComprobaciones);
			if (this.listComprobaciones == null || this.listComprobaciones.isEmpty()) 
				return;
			
			for (FacturaImpuestos comprobacion : this.listComprobaciones) {
				if (comprobacion.getPojoFactura().getSubtotal() == null)
					comprobacion.getPojoFactura().setSubtotal(0D);
				if (comprobacion.getPojoFactura().getTotalImpuestos() == null)
					comprobacion.getPojoFactura().setTotalImpuestos(0D);
				if (comprobacion.getPojoFactura().getTotalRetenciones() == null)
					comprobacion.getPojoFactura().setTotalRetenciones(0D);
				
				monto = ((comprobacion.getPojoFactura().getSubtotal().doubleValue() + comprobacion.getPojoFactura().getTotalImpuestos().doubleValue()) - comprobacion.getPojoFactura().getTotalRetenciones().doubleValue());
				if (comprobacion.getPojoFactura().getFacturado() == 1)
					conFactura += monto;
				else if (comprobacion.getPojoFactura().getFacturado() == 0)
					sinFactura += monto;
				this.totalComprobaciones += monto;
			}  

			conFactura = (new BigDecimal(conFactura)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();  
			sinFactura = (new BigDecimal(sinFactura)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();  
			this.totalComprobaciones = (new BigDecimal(this.totalComprobaciones)).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();  
			this.pojoProvision.setConFactura(conFactura);
			this.pojoProvision.setSinFactura(sinFactura);
			this.pojoProvision.setMonto(this.totalComprobaciones);
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
			if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getIdConcepto() != null && this.pojoProvisionDetalle.getIdConcepto().getId() > 0L)
				idEgreso = this.pojoProvisionDetalle.getIdConcepto().getId();

			this.totalComprobacion = Utilerias.redondear(this.totalComprobacion, 4);
			this.pojoProvisionDetalle.setSubtotal(subtotal);
			this.pojoProvisionDetalle.setTotalImpuestos(sumImpuestos);
			this.pojoProvisionDetalle.setTotalRetenciones(sumRetenciones);
			this.pojoProvisionDetalle.setTotal(this.totalComprobacion);
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
   				subtotal = baseImpuestos - (sumImpuestos - sumRetenciones);
   				this.totalComprobacion = subtotal + (sumImpuestos - sumRetenciones);
   				this.existeRetencion = (sumRetenciones > 0);
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
				pojoRegistroEgresosDetalleImpuesto.setIdPagosGastosDet(this.pojoProvisionDetalle);
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

			subtotal = baseImpuestos - (sumImpuestos - sumRetenciones);
			this.totalComprobacion = subtotal + (sumImpuestos - sumRetenciones);
			this.existeRetencion = (sumRetenciones > 0);
		} catch (Exception e) {
			control("Ocurrio un problema al desglozar los impuestos", e);
		} finally {
			this.pojoProvisionDetalle.setSubtotal(subtotal);
			this.pojoProvisionDetalle.setTotalImpuestos(sumImpuestos);
			this.pojoProvisionDetalle.setTotalRetenciones(sumRetenciones);
			this.pojoProvisionDetalle.setTotal(this.totalComprobacion);
			if (idEgreso > 0L && this.totalComprobacion > 0D) 
				log.info("\nCOMPROBACION\n------------------------------\n     SUBTOTAL : " + subtotal + "\n    IMPUESTOS : " + sumImpuestos + "\n  RETENCIONES : " + sumRetenciones + "\n        TOTAL : " + this.totalComprobacion + "\n\n");
			// multiConceptos
			if (this.mapConceptoImpuestos != null && ! this.listDesgloseImpuestos.isEmpty()) {
				/*if (this.pojoProvisionDetalle.getUniqueValue() == 0)
					this.pojoProvisionDetalle.setUniqueValue(genUniqueValue(this.listComprobacionFacturaConceptos.size(), 0));*/
				this.mapConceptoImpuestos.put(this.pojoProvisionDetalle.getUniqueValue(), this.listDesgloseImpuestos);
			}
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
			this.pojoProvisionDetalle.setSubtotal(subtotal);
			this.pojoProvisionDetalle.setTotalImpuestos(sumImpuestos);
			this.pojoProvisionDetalle.setTotalRetenciones(sumRetenciones);
			this.pojoProvisionDetalle.setTotal(this.totalComprobacion);
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

   	private List<PagosGastosDetImpuestosExt> getImpuestosByIdPagosGastosDetalle(long idPagosGastosDetalle, List<PagosGastosDetImpuestosExt> base, String tipo) {
   		List<PagosGastosDetImpuestosExt> impuestos = new ArrayList<PagosGastosDetImpuestosExt>();
   		
   		try {
	   		if ((base == null || base.isEmpty()) && idPagosGastosDetalle > 0L)
	   			base = this.ifzDesgloImpto.findExtAll(idPagosGastosDetalle);
	   		
	   		if (base != null && ! base.isEmpty()) {
	   			if (tipo == null || "".equals(tipo.trim()))
	   				return base;
	   			
	   			for (PagosGastosDetImpuestosExt impuesto : base) {
	   				if (tipo.equals(impuesto.getIdImpuesto().getTipoCuenta())) 
	   					impuestos.add(impuesto);
	   			}
	   		}
   		} catch (Exception e) {
   			log.error("Ocurrio un problema al recuperar los impuestos del PagosGastosDetalle indicado", e);
   		}
   		
   		return impuestos;
   	}
   	
	private boolean validarEnvio() {
		boolean ningunaSeleccionada = false;
		
		for (FacturaImpuestos comprobacion : this.listComprobaciones) {
			if (comprobacion.isSeleccionado()) {
				ningunaSeleccionada = true;
				break;
			}
		}
		
		if (! ningunaSeleccionada) {
			control(-1, "Debe seleccionar al menos una Comprobacion");
			return false;
		}
		
		return true;
	}

   	private boolean validarComprobacion() {
   		try {
			if (this.pojoProvisionDetalle.getIdConcepto() == null || this.pojoProvisionDetalle.getIdConcepto().getId() <= 0L) {
				control(-1, "Debe asignar un Egreso");
				return false;
			}

			this.pojoProvisionDetalle.setIdPagosGastos(this.pojoProvision); 
			this.pojoProvisionDetalle.setTotal(this.totalComprobacion);
			if (this.pojoProvisionDetalle.getIdObra() == null)
				this.pojoProvisionDetalle.setIdObra(new Obra());
			if (this.pojoProvisionDetalle.getIdOrdenCompra() == null)
				this.pojoProvisionDetalle.setIdOrdenCompra(new OrdenCompra());
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
			
			if (! this.isDebug)
				return;
			log.info("\n\n------------------------------------------------------------------------"
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
		Long facturaId = 0L;
		String valorAux = "";
		String fileName = "";
		String comprobados = "";
		double totalFactura = 0;
		double saldoFactura = 0;
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
		//long uniqueValue = 0L;
		
		try {
			control();
			if (this.fileSrc == null) {
				log.warn("VALIDACION - CFDI vacio (xml)");
				control(-1, "Ocurrio un problema al procesar el CFDI indicado");
				return;
			} 
			
			fileName = this.ftpDigitalizacionRuta + this.prefijoFacturas + "[ID].xml";
			this.ifzProvisionDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzProvisionDetalle.procesarCFDI(this.fileSrc, TiposGastoCXP.RegistroGasto, this.pojoProvision.getId(), fileName, this.beneficiarioRfcReferencia);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				if (respuesta.getBody().getValor("comprobados") != null)
					comprobados = desglozaComprobados(respuesta.getBody().getValor("comprobados").toString());
				log.warn("ERROR INTERNO - Ocurrio un problema al intentar guardar la factura. " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError() + "\nNo se cargo la Factura." + comprobados);
				return;
			}
			
			// Inicializamos si corresponde
			if (this.pojoProvisionDetalle == null) {
				this.pojoProvisionDetalle = new PagosGastosDetExt();
				this.pojoProvisionDetalle.setFacturado(1);
				this.pojoProvisionDetalle.setFecha(Calendar.getInstance().getTime());
				this.pojoProvisionDetalle.setReferencia("");
			}
			
			valorAux = validateString(respuesta.getBody().getValor("idComprobante"));
			if (valorAux != null && ! "".equals(valorAux.trim()))
				this.pojoProvisionDetalle.setIdCfdi(Long.parseLong(valorAux.trim()));
			
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			valorAux = validateString(respuesta.getBody().getValor("comprobanteFecha"));
			if (valorAux != null && ! "".equals(valorAux.trim()))
				this.pojoProvisionDetalle.setFecha(formatter.parse(valorAux.trim()));
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteSerie"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoProvisionDetalle.setFacturaSerie(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteFolio"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoProvisionDetalle.setFacturaFolio(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteFolioFactura"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoProvisionDetalle.setReferencia(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteTipo"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoProvisionDetalle.setFacturaTipo(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteUuid"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoProvisionDetalle.setFacturaUuid(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("expresionImpresa"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoProvisionDetalle.setExpresionImpresa(valorAux.trim());

			valorAux = validateString(respuesta.getBody().getValor("comprobanteEmisor"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoProvisionDetalle.setRfcProveedor(valorAux);

			valorAux = validateString(respuesta.getBody().getValor("comprobantePersonalidad"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				tipoPersonalidad = valorAux;

			valorAux = validateString(respuesta.getBody().getValor("comprobanteTipoPersona"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				tipoPersonaProveedor = valorAux;
			
			this.pojoProvisionDetalle.setTipoPersonaProveedor(tipoPersonaProveedor);

			valorAux = validateString(respuesta.getBody().getValor("comprobanteRfc"));
			if (valorAux != null && ! "".equals(valorAux.trim())) {
				this.pojoProvisionDetalle.setRfcProveedor(valorAux);
				listProveedores = this.ifzPersonas.findLikeClaveNombre("rfc", valorAux, this.grupoPersonas, "N", 0, false);
				if (listProveedores != null && ! listProveedores.isEmpty()) {
					pojoProveedor = extenderPersona(listProveedores.get(0));
					pojoProveedor.setTipoPersona(("M".equals(tipoPersonalidad) ? 2L : 1L));
					this.pojoProvisionDetalle.setIdProveedor(pojoProveedor);
					this.pojoProvisionDetalle.setFacturaRfc(pojoProveedor.getRfc());
					this.pojoProvisionDetalle.setNombreProveedor(pojoProveedor.getNombre());
					this.pojoProvisionDetalle.setTipoPersonaProveedor(tipoPersonaProveedor);
				} else {
					nuevoProveedor = true;
					this.pojoProvisionDetalle.setFacturaRfc(valorAux);
					valorAux = validateString(respuesta.getBody().getValor("comprobanteRazonSocial"));
					if (valorAux != null && ! "".equals(valorAux.trim())) 
						this.pojoProvisionDetalle.setNombreProveedor(valorAux);
					pojoProveedor = nuevoProveedor(this.pojoProvisionDetalle.getNombreProveedor(), this.pojoProvisionDetalle.getFacturaRfc(), tipoPersonaProveedor);
					this.pojoProvisionDetalle.setIdProveedor(pojoProveedor);
					if (pojoProveedor != null) {
						completarProveedor = true;
						this.pojoProvisionDetalle.setIdProveedor(pojoProveedor);
						this.pojoProvisionDetalle.setFacturaRfc(pojoProveedor.getRfc());
						this.pojoProvisionDetalle.setNombreProveedor(pojoProveedor.getNombre());
						this.pojoProvisionDetalle.setTipoPersonaProveedor(tipoPersonaProveedor);
					}
				}
			}
			
			/*
			valorAux = validateString(respuesta.getBody().getValor("comprobanteTipoPersona"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoProvisionDetalle.setTipoPersonaProveedor(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteRfc"));
			if (valorAux != null && ! "".equals(valorAux.trim())) {
				if ("N".equals(this.pojoProvisionDetalle.getTipoPersonaProveedor()))
					listProveedores = this.ifzPersonas.findLikeClaveNombre("rfc", valorAux, this.grupoPersonas, this.pojoProvisionDetalle.getTipoPersonaProveedor(), 0, false);
				else if ("P".equals(this.pojoProvisionDetalle.getTipoPersonaProveedor()))
					listProveedores = this.ifzPersonas.findLikeClaveNombre("rfc", valorAux, this.grupoPersonas, this.pojoProvisionDetalle.getTipoPersonaProveedor(), 0, false);
				if (listProveedores != null && ! listProveedores.isEmpty()) {
					pojoProveedor = extenderPersona(listProveedores.get(0));
					pojoProveedor.setTipoPersona(("N".equals(this.pojoProvisionDetalle.getTipoPersonaProveedor()) ? 2L : 1L));
					this.pojoProvisionDetalle.setIdProveedor(pojoProveedor);
					this.pojoProvisionDetalle.setNombreProveedor(pojoProveedor.getNombre());
					this.pojoProvisionDetalle.setFacturaRfc(pojoProveedor.getRfc());
				} else {
					nuevoProveedor = true;
					this.pojoProvisionDetalle.setFacturaRfc(valorAux);
					valorAux = validateString(respuesta.getBody().getValor("comprobanteRazonSocial"));
					if (valorAux != null && ! "".equals(valorAux.trim())) 
						this.pojoProvisionDetalle.setNombreProveedor(valorAux);
					pojoProveedor = nuevoProveedor(this.pojoProvisionDetalle.getNombreProveedor(), this.pojoProvisionDetalle.getFacturaRfc(), this.pojoProvisionDetalle.getTipoPersonaProveedor());
					this.pojoProvisionDetalle.setIdProveedor(pojoProveedor);
					if (pojoProveedor != null) {
						completarProveedor = true;
						this.pojoProvisionDetalle.setIdProveedor(pojoProveedor);
						this.pojoProvisionDetalle.setNombreProveedor(pojoProveedor.getNombre());
						this.pojoProvisionDetalle.setFacturaRfc(pojoProveedor.getRfc());
					}
				}
			}*/
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteMoneda"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoProvisionDetalle.setFacturaMoneda(valorAux.trim());
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteTipoCambio"));
			if (valorAux != null && ! "".equals(valorAux.trim())) 
				this.pojoProvisionDetalle.setFacturaTipoCambio(Double.parseDouble(valorAux));
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteSaldo"));
			if (valorAux != null && ! "".equals(valorAux.trim()))
				saldoFactura = Double.parseDouble(valorAux);
			
			valorAux = validateString(respuesta.getBody().getValor("comprobanteTotal"));
			if (valorAux != null && ! "".equals(valorAux.trim())) {
				this.pojoProvisionDetalle.setFacturaTotal(Double.parseDouble(valorAux));
				totalFactura =  Double.parseDouble(valorAux);
			}
			
			this.totalComprobacion = saldoFactura;
			conceptos = new ArrayList<FacturaConcepto>();
			this.listComprobacionFacturaConceptos = new ArrayList<PagosGastosDetExt>();
			this.listComprobacionFacturaConceptos.add(this.pojoProvisionDetalle);
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
					//uniqueValue = genUniqueValue(conceptos.size(), conceptos.indexOf(concepto));
					// ---------------------------------------------------------------------
					detalle = new PagosGastosDetExt();
					BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
					BeanUtils.copyProperties(detalle, this.pojoProvisionDetalle);
					//detalle.setUniqueValue(uniqueValue);
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
					this.pojoProvisionDetalle.setTotal(totalFacturaConceptos);
					this.agruparConceptos = true;
				}
			}
			
			// Comprobamos con los detalles actuales
			for (FacturaImpuestos item : this.listComprobaciones) {
				if (item.getPojoFactura().getIdCfdi().longValue() == facturaId.longValue()) {
					saldoFactura -= item.getPojoFactura().getTotal();
					if (totalFactura > 0 && saldoFactura <= 0) {
						nuevaFactura();
						if (respuesta.getBody().getValor("comprobados") != null)
							comprobados = desglozaComprobados(respuesta.getBody().getValor("comprobados").toString());
						control(-1, "El Documento ya fue cargado previamente en este Provision.\nEl Documento queda saldado con el monto añadido en este Registro." + comprobados);
						return;
					}
				}
			}
			
			desglosaImpuestos();
			resetXML();
			if (nuevoProveedor) {
				if (completarProveedor)
					control(false, 31, "El Emisor de la Factura no existe.\nSe guardo en Negocios, sin embargo, debe completar los datos.\nRFC: " + this.pojoProvisionDetalle.getFacturaRfc(), null);
				else
					control(false, 30, "El Emisor de la Factura no existe.\nOcurrio un problema y no se pudo guardar en Negocios.\nRFC: " + this.pojoProvisionDetalle.getFacturaRfc(), null);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar procesar el CFDI indicado", e);
		} 
	}
	
	public void descargarXML() {
		String fileName = "";
		byte[] fileContenido = null;
		Long idFactura = 0L;
		
		try {
			control();
			if (this.pojoComprobacion == null) {
				log.error("ERROR 20 - No tiene asignada ninguna factura");
				control(-1, "No ha especificado ninguna factura (*.xml)");
				return;
			}

			if (this.pojoComprobacion.getPojoFactura().getIdCfdi() == null || this.pojoComprobacion.getPojoFactura().getIdCfdi() <= 0L)
				this.pojoComprobacion.getPojoFactura().setIdCfdi(0L);
			idFactura = this.pojoComprobacion.getPojoFactura().getIdCfdi();
			if (idFactura == null || idFactura <= 0L) {
				log.error("ERROR 20 - No tiene asignada ninguna factura");
				control(-1, "No ha especificado ninguna factura (*.xml)");
				return;
			}

			// Inicializamos variables de archivos y recuperamos el archivo
			fileName = this.prefijoFacturas + idFactura + ".xml";
			fileContenido = this.ifzFtp.get(this.ftpDigitalizacionRuta + fileName);
			if (fileContenido == null || fileContenido.length <= 0) {
				log.error("ERROR 21 - No tiene asignada ninguna factura");
				control(-1, "No se encontro el archivo en el servidor");
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
		Negocio negocio = null;
		Persona persona = null;
		
		try {
			this.ifzProvision.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzProvision.nuevoNegocioProveedor(this.pojoProvisionDetalle.getUniqueValue(), nombre, rfc, tipoPersona);
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
		log.info("\n\n------------------------------------------------------------------------"
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
		log.info("\n\n------------------------------------------------------------------------"
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
			if (! this.pojoProvisionDetalle.isSeleccionado()) {
				this.seleccionarTodo = false;
				for (PagosGastosDetExt concepto : this.listComprobacionFacturaConceptos) {
					this.totalFacturaConceptos += concepto.getTotal();
					concepto.setSeleccionado(false);
					if (this.pojoProvisionDetalle.getUniqueValue() == concepto.getUniqueValue()) 
						BeanUtils.copyProperties(concepto, this.pojoProvisionDetalle);
				}
				return;
			}
			
			// Respaldamos valores originales
			detalleOriginal = new PagosGastosDetExt();
			BeanUtils.copyProperties(detalleOriginal, this.pojoProvisionDetalle);
			
			for (PagosGastosDetExt concepto : this.listComprobacionFacturaConceptos) {
				this.totalFacturaConceptos += concepto.getTotal();
				if (! concepto.isSeleccionado())
					continue;
				if (detalleOriginal.getUniqueValue() == concepto.getUniqueValue()) {
					BeanUtils.copyProperties(concepto, detalleOriginal);
					continue;
				}
				
				// Procesamos
				this.pojoProvisionDetalle = new PagosGastosDetExt();
				BeanUtils.copyProperties(this.pojoProvisionDetalle, concepto);
				// Comprobacion de Concepto
				if (validaObjetoIdToUpdate(detalleOriginal.getIdConcepto(), this.pojoProvisionDetalle.getIdConcepto())) {
					this.pojoProvisionDetalle.setIdConcepto(detalleOriginal.getIdConcepto());
					this.totalComprobacion = this.pojoProvisionDetalle.getTotal();
					this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
					if (this.mapConceptoImpuestos.containsKey(this.pojoProvisionDetalle.getUniqueValue()))
						this.listDesgloseImpuestos = this.mapConceptoImpuestos.get(this.pojoProvisionDetalle.getUniqueValue());
					desglosaImpuestos();
				}
				// Comprobacion de Obra
				if (validaObjetoIdToUpdate(detalleOriginal.getIdObra(), this.pojoProvisionDetalle.getIdObra()))
					this.pojoProvisionDetalle.setIdObra(detalleOriginal.getIdObra());
				// Comprobacion de Orden Compra
				if (validaObjetoIdToUpdate(detalleOriginal.getIdOrdenCompra(), this.pojoProvisionDetalle.getIdOrdenCompra()))
					this.pojoProvisionDetalle.setIdOrdenCompra(detalleOriginal.getIdOrdenCompra());
				// Actualizamos en lista
				BeanUtils.copyProperties(concepto, this.pojoProvisionDetalle);
			}
			
			// Restauramos original
			this.pojoProvisionDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoProvisionDetalle, detalleOriginal);
			this.totalComprobacion = this.pojoProvisionDetalle.getTotal();
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
		} else if (obj1 instanceof OrdenCompra && obj2 instanceof OrdenCompra){
			idObj1 = ((OrdenCompra) obj1).getId();
			idObj2 = ((OrdenCompra) obj2).getId();
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
			this.pojoProvisionDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoProvisionDetalle, detalle);
			this.totalComprobacion = this.pojoProvisionDetalle.getTotal();
			if (this.mapConceptoImpuestos.containsKey(this.pojoProvisionDetalle.getUniqueValue()))
				this.listDesgloseImpuestos = this.mapConceptoImpuestos.get(this.pojoProvisionDetalle.getUniqueValue());
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
			this.pojoProvisionDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoProvisionDetalle, detalle);
			this.totalComprobacion = this.pojoProvisionDetalle.getTotal();
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
			this.pojoProvisionDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoProvisionDetalle, detalle);
			this.totalComprobacion = this.pojoProvisionDetalle.getTotal();
			this.pojoProvisionDetalle.setSeleccionado(false);
			quitarObra();
			// multiConceptos
			replicaAsignacion();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar la Obra al Concepto indicado", e);
		}
	}
	
	public void nuevaBusquedaOrdenCompra(int index) {
		PagosGastosDetExt detalle = null;
		
		try {
			control();
			if (! validateIndex(index))
				return;
			
			this.indexFacturaConcepto = index;
			detalle = this.listComprobacionFacturaConceptos.get(index);
			this.pojoProvisionDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoProvisionDetalle, detalle);
			this.totalComprobacion = this.pojoProvisionDetalle.getTotal();
			nuevaBusquedaOrdenCompra();
		} catch (Exception e) {
			control("Ocurrio un problema al preparar la asignacion de Orden de Compra al Concepto indicado", e);
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
			this.pojoProvisionDetalle = new PagosGastosDetExt();
			BeanUtils.copyProperties(this.pojoProvisionDetalle, detalle);
			this.totalComprobacion = this.pojoProvisionDetalle.getTotal();
			this.pojoProvisionDetalle.setSeleccionado(false);
			quitarOrdenCompra();
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
			this.pojoProvisionDetalle = new PagosGastosDetExt();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.pojoProvisionDetalle, detalle);
			this.totalComprobacion = this.pojoProvisionDetalle.getTotal();
			
			this.listDesgloseImpuestos = new ArrayList<PagosGastosDetImpuestosExt>();
			if (this.mapConceptoImpuestos.containsKey(this.pojoProvisionDetalle.getUniqueValue())) {
				if (this.pojoProvisionDetalle.getIdConcepto() != null && this.pojoProvisionDetalle.getIdConcepto().getId() > 0L)
					this.idEgresoPrevio = this.pojoProvisionDetalle.getIdConcepto().getId();
				this.listDesgloseImpuestos = this.mapConceptoImpuestos.get(this.pojoProvisionDetalle.getUniqueValue());
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
			
			this.pojoProvisionDetalle = null;
			for (PagosGastosDetExt concepto : this.listComprobacionFacturaConceptos) {
				total += concepto.getTotal();
				if (this.pojoProvisionDetalle == null) {
					this.pojoProvisionDetalle = new PagosGastosDetExt();
					BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
					BeanUtils.copyProperties(this.pojoProvisionDetalle, concepto);
					continue;
				}
				
				// Asignacion de Concepto
				if (concepto.getIdConcepto() != null && concepto.getIdConcepto().getId() > 0L)
					continue;
				if (this.pojoProvisionDetalle.getIdConcepto() != null && this.pojoProvisionDetalle.getIdConcepto().getId() > 0L)
					continue;
				this.pojoProvisionDetalle.setIdConcepto(concepto.getIdConcepto());
				
				// Asignacion de Obra
				if (concepto.getIdObra() != null && concepto.getIdObra().getId() != null && concepto.getIdObra().getId() > 0L)
					continue;
				if (this.pojoProvisionDetalle.getIdObra() != null && this.pojoProvisionDetalle.getIdObra().getId() != null && this.pojoProvisionDetalle.getIdObra().getId() > 0L)
					continue;
				this.pojoProvisionDetalle.setIdObra(concepto.getIdObra());
				
				// Asignacion de Orden de Compra
				if (concepto.getIdOrdenCompra() != null && concepto.getIdOrdenCompra().getId() != null && concepto.getIdOrdenCompra().getId() > 0L)
					continue;
				if (this.pojoProvisionDetalle.getIdOrdenCompra() != null && this.pojoProvisionDetalle.getIdOrdenCompra().getId() != null && this.pojoProvisionDetalle.getIdOrdenCompra().getId() > 0L)
					continue;
				this.pojoProvisionDetalle.setIdOrdenCompra(concepto.getIdOrdenCompra());
			}

			this.pojoProvisionDetalle.setTotal(total);
			this.totalComprobacion = total;
			desglosaImpuestos();
		} catch (Exception e) {
			control("Ocurrio un problema al actualizar los datos al concepto indicado", e);
		} 
	}
	
	public void guardarComprobaciones() {
		FacturaImpuestos oriComprobacion = null;
		// -----------------------------------------------------
		LinkedHashMap<String, String> agrupador = null;
		String[] splitted = null;
		String indices = "";
		String key = "";
		Long idConcepto = 0L;
		Long idObra = 0L;
		Long idOrdenCompra = 0L;
		int indice = 0;
		String descripcion = "";
		// -----------------------------------------------------
		PagosGastosDetExt detalle = null;
		PagosGastosDetExt conceptoFactura = null;
		List<PagosGastosDetExt> consolidados = null;
		// -----------------------------------------------------
		List<Long> idImpuestos = null;
		List<PagosGastosDetImpuestosExt> tmp = null;
		List<PagosGastosDetImpuestosExt> impuestosConsolidados = null;
		HashMap<Long, List<PagosGastosDetImpuestosExt>> impuestos = null;
		
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
			
			// Agrupador de conceptos e impuestos
			agrupador = new LinkedHashMap<String, String>();
			idImpuestos = new ArrayList<Long>();
			impuestos = new HashMap<Long, List<PagosGastosDetImpuestosExt>>();
			for (PagosGastosDetExt comprobado : this.listComprobacionFacturaConceptos) {
				// Inicializamos
				idObra = 0L;
				idOrdenCompra = 0L;
				indice = this.listComprobacionFacturaConceptos.indexOf(comprobado);
				idConcepto = comprobado.getIdConcepto().getId();
				if (comprobado.getIdObra() != null && comprobado.getIdObra().getId() != null && comprobado.getIdObra().getId() > 0L)
					idObra = comprobado.getIdObra().getId();
				if (comprobado.getIdOrdenCompra() != null && comprobado.getIdOrdenCompra().getId() != null && comprobado.getIdOrdenCompra().getId() > 0L)
					idOrdenCompra = comprobado.getIdOrdenCompra().getId();
				
				// generamos llave con concepto-obra-ordenCompra y comporamos si previamente ya la habiamos generado
				indices = "";
				key = idConcepto.toString() + "|" + idObra.toString() + "|" + idOrdenCompra.toString();
				if (agrupador.containsKey(key)) 
					indices = agrupador.get(key);
				
				// Añado el indice del concepto comprobado al agrupador
				indices += (! "".equals(indices.trim()) ? "," : "") + indice;
				agrupador.put(key, indices);
				
				if (this.mapConceptoImpuestos.containsKey(comprobado.getUniqueValue())) {
					tmp = this.mapConceptoImpuestos.get(comprobado.getUniqueValue());
					impuestosConsolidados = new ArrayList<PagosGastosDetImpuestosExt>();
					if (impuestos.containsKey(comprobado.getIdConcepto().getId()))
						impuestosConsolidados = impuestos.get(comprobado.getIdConcepto().getId());
					for (PagosGastosDetImpuestosExt imp : tmp) {
						if (! idImpuestos.contains(imp.getIdImpuesto().getId())) {
							idImpuestos.add(imp.getIdImpuesto().getId());
							impuestosConsolidados.add(imp);
						}
					}
					
					impuestos.put(comprobado.getIdConcepto().getId(), impuestosConsolidados);
				}
			}
			
			// Consolidando conceptos
			consolidados = new ArrayList<PagosGastosDetExt>();
			for (Entry<String, String> grupo : agrupador.entrySet()) {
				splitted = grupo.getValue().split(",");
				for (String indexStr : splitted) {
					if (detalle == null) {
						detalle = this.listComprobacionFacturaConceptos.get(Integer.parseInt(indexStr));;
						descripcion = detalle.getFacturaConcepto() + "|" + detalle.getImporteConceptoNumber();
						continue;
					}
					
					conceptoFactura = this.listComprobacionFacturaConceptos.get(Integer.parseInt(indexStr));
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
			
			for (PagosGastosDetExt consolidado : consolidados) { 
				this.pojoComprobacion = new FacturaImpuestos();
				BeanUtils.copyProperties(this.pojoComprobacion, oriComprobacion);
				
				this.pojoProvisionDetalle = new PagosGastosDetExt();
				BeanUtils.copyProperties(this.pojoProvisionDetalle, consolidado);
				this.totalComprobacion = this.pojoProvisionDetalle.getTotal();
				this.idEgresoPrevio = this.pojoProvisionDetalle.getIdConcepto().getId();
				this.listDesgloseImpuestos = impuestos.get(this.pojoProvisionDetalle.getIdConcepto().getId());
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
			if (this.pojoProvisionDetalle.getIdConcepto() != null && this.pojoProvisionDetalle.getIdConcepto().getId() > 0L)
				this.idEgresoPrevio = ((this.pojoConcepto.getId() != this.pojoProvisionDetalle.getIdConcepto().getId()) ? this.pojoConcepto.getId() : 0L);

			// Asignamos la Obra al REGISTRO DE GASTO, si corresponde
			egreso = new ConValores();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(egreso, this.pojoConcepto);
			this.pojoProvisionDetalle.setIdConcepto(egreso);
			desglosaImpuestos();

			// multiConceptos
			replicaAsignacion();
			nuevaBusquedaConceptos();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Concepto de Gasto seleccionado", e);
		} 
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
			log.info(this.listObras.size() + " Obras encontradas");
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
			this.pojoProvisionDetalle.setIdObra(obra);
			if (this.pojoProvisionDetalle.getIdOrdenCompra() != null && this.pojoProvisionDetalle.getIdOrdenCompra().getId() != null && this.pojoProvisionDetalle.getIdOrdenCompra().getId() > 0L) {
				if (this.pojoProvisionDetalle.getIdOrdenCompra().getIdObra().longValue() != this.pojoObra.getId().longValue())
					this.pojoProvisionDetalle.setIdOrdenCompra(null);
			}

			// multiConceptos
			replicaAsignacion();
			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		}
	}
	
	public void quitarObra() {
		try {
			control();
			if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getIdObra() != null && this.pojoProvisionDetalle.getIdObra().getId() != null && this.pojoProvisionDetalle.getIdObra().getId() > 0L) {
				this.pojoProvisionDetalle.setIdObra(null);
				quitarOrdenCompra();
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar desasignar la Obra de la comprobacion", e);
		}
	}
	
	// ------------------------------------------------------------------------------------------
	// BUSQUEDA ORDEN DE COMPRA
	// ------------------------------------------------------------------------------------------
	
	public void nuevaBusquedaOrdenCompra() {
		control();
		this.campoBusquedaOrdenCompra = this.tiposBusquedaOrdenCompra.get(0).getValue().toString();
		this.valorBusquedaOrdenCompra = "";
		this.paginacionOrdenCompra = 1;
		
		this.listOrdenCompra = new ArrayList<OrdenCompra>();
		this.pojoOrdenCompra = null;
		if (this.pojoProvisionDetalle == null || this.pojoProvisionDetalle.getIdObra() == null || this.pojoProvisionDetalle.getIdObra().getId() == null || this.pojoProvisionDetalle.getIdObra().getId() <= 0L) 
			control(-1, "Debe seleccionar una Obra");
	}
	
	public void buscarOrdenCompra() {
		try {
			control();
			if ("".equals(this.campoBusquedaOrdenCompra))
				this.campoBusquedaOrdenCompra = this.tiposBusquedaOrdenCompra.get(0).getValue().toString();

			this.paginacionOrdenCompra = 1;
			this.ifzOrdenCompra.setInfoSesion(this.loginManager.getInfoSesion());
			this.listOrdenCompra = this.ifzOrdenCompra.findLikeProperty(this.campoBusquedaOrdenCompra, this.valorBusquedaOrdenCompra, this.pojoProvisionDetalle.getIdObra().getId(), true, 0);
			if (this.listOrdenCompra == null || this.listOrdenCompra.isEmpty()) 
				control(13, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Ordenes de Compra de la Obra seleccionada", e);
		}
	}
	
	public void seleccionarOrdenCompra() {
		OrdenCompra ordenCompra = null;
		
		try {
			control();
			if (this.pojoOrdenCompra == null || this.pojoOrdenCompra.getId() == null || this.pojoOrdenCompra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Orden de Compra seleccionada");
				return;
			}

			// Asignamos la Orden de Compra al REGISTRO DE GASTO, si corresponde
			ordenCompra = new OrdenCompra();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(ordenCompra, this.pojoOrdenCompra);
			this.pojoProvisionDetalle.setIdOrdenCompra(ordenCompra);

			// multiConceptos
			replicaAsignacion();
			nuevaBusquedaOrdenCompra();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Orden de Compra seleccionada", e);
		}
	}
	
	public void quitarOrdenCompra() {
		try {
			control();
			if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getIdOrdenCompra() != null && this.pojoProvisionDetalle.getIdOrdenCompra().getId() != null && this.pojoProvisionDetalle.getIdOrdenCompra().getId() > 0L)
				this.pojoProvisionDetalle.setIdOrdenCompra(null);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar desasignar la Orden de Compra de la comprobacion", e);
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
				this.idEgresoPrevio = this.pojoProvisionDetalle.getIdConcepto().getId();
			porcentajeImpuesto = validateString2Double(this.pojoBusquedaRetenciones.getAtributo1());
			if (porcentajeImpuesto > 0) 
				porcentajeImpuesto = (porcentajeImpuesto / 100); 
			
			pojoDesgloImpto = new PagosGastosDetImpuestosExt();
			pojoDesgloImpto.setIdImpuesto(this.pojoBusquedaRetenciones);
			pojoDesgloImpto.setIdPagosGastosDet(this.pojoProvisionDetalle);
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
			this.pojoProvisionDetalle.setSeleccionado(false);
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
   		if (this.pojoProvision != null && this.pojoProvision.getId() != null && this.pojoProvision.getId() > 0L)
   			return "Provision " + this.pojoProvision.getId();
   		return "Nueva Provision";
   	}
   	
   	public void setTitulo(String value) { }

   	public String getTituloComprobacion() {
   		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getId() != null && this.pojoProvisionDetalle.getId() > 0L)
   			return "Comprobacion " + this.pojoProvisionDetalle.getId();
   		return "Nuevo Comprobacion";
   	}
   	
   	public void setTituloComprobacion(String value) { }

   	public String getTituloComprobacionFactura() {
   		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getIdCfdi() != null && this.pojoProvisionDetalle.getIdCfdi() > 0L)
   			return "Factura " + this.pojoProvisionDetalle.getIdCfdi() + " - " + this.pojoProvisionDetalle.getFacturaUuid();
   		return "Nuevo Factura";
   	}
   	
   	public void setTituloComprobacionFactura(String value) { }

	public void setListProvisionesItems(List<EjercicioHolder> listProvisionesItems) {
		this.listProvisionesItems = listProvisionesItems;
	}

	public List<EjercicioHolder> getListProvisionesItems() {
		return listProvisionesItems;
	}

	public List<PagosGastos> getListProvisiones() {
		return listProvisiones;
	}

	public void setListProvisiones(List<PagosGastos> listProvisiones) {
		this.listProvisiones = listProvisiones;
	}

	public long getIdProvision() {
		return idProvision;
	}

	public void setIdProvision(long idProvision) {
		this.idProvision = idProvision;
	}

	public PagosGastosExt getPojoRegistroEgresos() {
		return pojoProvision;
	}

	public void setPojoRegistroEgresos(PagosGastosExt pojoProvision) {
		this.pojoProvision = pojoProvision;
	}

	public Date getFecha() {
		if (this.pojoProvision != null && this.pojoProvision.getFecha() != null)
			return this.pojoProvision.getFecha();
		return Calendar.getInstance().getTime();
	}

	public void setFecha(Date fecha) {
		this.pojoProvision.setFecha(fecha);
	}

	public long getIdSucursal() {
		if (this.pojoProvision != null && this.pojoProvision.getIdSucursal() != null)
			return this.pojoProvision.getIdSucursal().getId();
		return 0L;
	}

	public void setIdSucursal(long idSucursal) {
		if (idSucursal > 0L && this.pojoProvision != null) {
			for (Sucursal sucursal : this.listSucursales) {
				if (sucursal.getId() == idSucursal) {
					this.pojoProvision.setIdSucursal(sucursal);
					break;
				}
			}
		}
	}

	public String getOperacion() {
		if (this.pojoProvision != null && this.pojoProvision.getOperacion() != null)
			return this.pojoProvision.getOperacion();
		return "";
	}

	public void setOperacion(String operacion) {
		if (this.pojoProvision != null)
			this.pojoProvision.setOperacion(operacion);
	}

	public String getReferenciaOperacion() {
		return this.referenciaOperacion;
	}

	public void setReferenciaOperacion(String referenciaOperacion) {
		this.referenciaOperacion = referenciaOperacion;
	}

	public long getIdCuentaBancaria() {
		if (this.pojoProvision != null && this.pojoProvision.getIdCuentaOrigen() != null && this.pojoProvision.getIdCuentaOrigen().getId() > 0L)
			return this.pojoProvision.getIdCuentaOrigen().getId();
		return 0L;
	}

	public void setIdCuentaBancaria(long idCuentaBancaria) {
		if (idCuentaBancaria > 0L && this.pojoProvision != null) 
			this.pojoProvision.setIdCuentaOrigen(this.ifzProvision.findCuentaBancariaById(idCuentaBancaria));
	}

	public String getTipoBeneficiario() {
		if (this.pojoProvision != null && this.pojoProvision.getTipoBeneficiario() != null)
			return this.pojoProvision.getTipoBeneficiario();
		return "";
	}

	public void setTipoBeneficiario(String tipoBeneficiario) {
		if (this.pojoProvision != null)
			this.pojoProvision.setTipoBeneficiario(tipoBeneficiario);
	}

	public String getBeneficiario() {
		if (this.pojoProvision != null &&  this.pojoProvision.getIdBeneficiario() != null &&  this.pojoProvision.getIdBeneficiario().getId() > 0L)
			return this.pojoProvision.getIdBeneficiario().getId() + " - " + this.pojoProvision.getBeneficiario();
		return "";
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
		seleccionarBeneficiario();
	}

	public List<Persona> getListBeneficiarios() {
		return listBeneficiarios;
	}

	public void setListBeneficiarios(List<Persona> listBeneficiarios) {
		this.listBeneficiarios = listBeneficiarios;
	}

	public String getConcepto() {
		if (this.pojoProvision != null && this.pojoProvision.getConcepto() != null)
			return this.pojoProvision.getConcepto();
		return "";
	}

	public void setConcepto(String concepto) {
		if (concepto != null && this.pojoProvision != null) 
			this.pojoProvision.setConcepto(concepto);
	}

	public String getComprobacionEgreso() {
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getIdConcepto() != null && this.pojoProvisionDetalle.getIdConcepto().getId() > 0L)
			return "<b>" + this.pojoProvisionDetalle.getIdConcepto().getId() + "</b> - " + this.pojoProvisionDetalle.getIdConcepto().getDescripcion();
		return "";
	}

	public void setComprobacionEgreso(String concepto) {}

	public String getComprobacionObra() {
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getIdObra() != null && this.pojoProvisionDetalle.getIdObra().getId() != null && this.pojoProvisionDetalle.getIdObra().getId() > 0L)
			return "<b>" + this.pojoProvisionDetalle.getIdObra().getId() + "</b> - " + this.pojoProvisionDetalle.getIdObra().getNombre();
		return "";
	}

	public void setComprobacionObra(String value) {}

	public String getComprobacionOrdenCompra() {
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getIdOrdenCompra() != null && this.pojoProvisionDetalle.getIdOrdenCompra().getId() != null && this.pojoProvisionDetalle.getIdOrdenCompra().getId() > 0L)
			return "<b>" + this.pojoProvisionDetalle.getIdOrdenCompra().getId() + "</b> - " + this.pojoProvisionDetalle.getIdOrdenCompra().getFolio() + " - " + this.pojoProvisionDetalle.getIdOrdenCompra().getNombreProveedor();
		return "";
	}

	public void setComprobacionOrdenCompra(String value) {}

	public long getComprobacionId() {
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getId() != null && this.pojoProvisionDetalle.getId() > 0L)
			return this.pojoProvisionDetalle.getId();
		return 0L;
	}

	public void setComprobacionId(long value) {}

	public Date getFechaComprobacion() {
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getFecha() != null)
			return this.pojoProvisionDetalle.getFecha();
		return Calendar.getInstance().getTime();
	}

	public void setFechaComprobacion(Date fecha) {
		if (this.pojoProvisionDetalle != null)
			this.pojoProvisionDetalle.setFecha(fecha);
	}

	public String getReferencia() {
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getReferencia() != null && ! "".equals(this.pojoProvisionDetalle.getReferencia().trim()))
			return this.pojoProvisionDetalle.getReferencia();
		return "";
	}

	public void setReferencia(String referencia) {
		if (this.pojoProvisionDetalle != null)
			this.pojoProvisionDetalle.setReferencia(referencia);
	}

	public String getTipoRazonSocial() {
		return tipoRazonSocial;
	}

	public void setTipoRazonSocial(String tipoRazonSocial) {
		this.tipoRazonSocial = tipoRazonSocial;
	}
	
	public String getRazonSocial() {
		if (this.pojoProvisionDetalle != null &&  this.pojoProvisionDetalle.getIdProveedor() != null &&  this.pojoProvisionDetalle.getIdProveedor().getId() > 0L)
			return this.pojoProvisionDetalle.getIdProveedor().getId() + " - " + this.pojoProvisionDetalle.getIdProveedor().getNombre();
		return "";
	}
	
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
		seleccionarRazonSocial();
	}
	
	public double getSubtotal() {
		if (this.pojoProvisionDetalle != null)
			return this.pojoProvisionDetalle.getSubtotal();
		return 0;
	}

	public void setSubtotal(double subtotal) {
		if (this.pojoProvisionDetalle != null)
			this.pojoProvisionDetalle.setSubtotal(subtotal);
	}

	public double getImpuestos() {
		if (this.pojoProvisionDetalle != null)
			return this.pojoProvisionDetalle.getTotalImpuestos();
		return 0;
	}

	public void setImpuestos(double impuestos) {
		if (this.pojoProvisionDetalle != null)
			this.pojoProvisionDetalle.setTotalImpuestos(impuestos);
	}

	public double getRetenciones() {
		if (this.pojoProvisionDetalle != null)
			return this.pojoProvisionDetalle.getTotalRetenciones();
		return 0;
	}

	public void setRetenciones(double retenciones) {
		if (this.pojoProvisionDetalle != null)
			this.pojoProvisionDetalle.setTotalRetenciones(retenciones);
	}

	public double getTotalComprobacion() {
		return totalComprobacion;
	}

	public void setTotalComprobacion(double totalComprobacion) {
		this.totalComprobacion = totalComprobacion;
	}

	public String getObservaciones() {
		if (this.pojoProvisionDetalle != null)
			return this.pojoProvisionDetalle.getObservaciones();
		return "";
	}

	public void setObservaciones(String observaciones) {
		if (this.pojoProvisionDetalle != null)
			this.pojoProvisionDetalle.setObservaciones(observaciones);
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
		return (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getFacturado() == 1);
	}
	
	public void setComprobacionConFactura(boolean value) {}

	public boolean getEgresoDetalleDescargarFactura() {
		return (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getId() != null && this.pojoProvisionDetalle.getId() > 0L && this.pojoProvisionDetalle.getIdCfdi() != null && this.pojoProvisionDetalle.getIdCfdi() > 0L);
	}
	
	public void setEgresoDetalleDescargarFactura(boolean value) {}
	
	public String getEgresoDetalleEmisor() {
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getNombreProveedor() != null && ! "".equals(this.pojoProvisionDetalle.getNombreProveedor()))
				return this.pojoProvisionDetalle.getNombreProveedor();
		return "";
	}
	
	public void setEgresoDetalleEmisor(String value) {}

	public String getEgresoDetalleEmisorRFC() {
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getFacturaRfc() != null && ! "".equals(this.pojoProvisionDetalle.getFacturaRfc()))
				return this.pojoProvisionDetalle.getFacturaRfc();
		return "";
	}
	
	public void setEgresoDetalleEmisorRFC(String value) {
		this.pojoProvisionDetalle.setFacturaRfc(value);
	}

	public String getEgresoDetalleFolio() {
		String folio = "";
		
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getFacturaFolio() != null && ! "".equals(this.pojoProvisionDetalle.getFacturaFolio()))
			folio = (this.pojoProvisionDetalle.getFacturaSerie() != null && ! "".equals(this.pojoProvisionDetalle.getFacturaSerie()) ? (this.pojoProvisionDetalle.getFacturaSerie() + "-") : "") + this.pojoProvisionDetalle.getFacturaFolio();
		return folio;
	}
	
	public void setEgresoDetalleFolio(String value) {}

	public Date getEgresoDetalleFecha() {
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getFecha() != null)
				return this.pojoProvisionDetalle.getFecha();
		return Calendar.getInstance().getTime();
	}
	
	public void setEgresoDetalleFecha(Date value) {}

	public double getEgresoDetalleFacturado() {
		if (this.pojoProvisionDetalle != null && this.pojoProvisionDetalle.getFacturaTotal() > 0)
			return this.pojoProvisionDetalle.getFacturaTotal();
		return 0;
	}

	public void setEgresoDetalleFacturado(double value) {}

	public String getEgresoDetalleMoneda() {
		if (this.pojoProvisionDetalle != null && ! "".equals(this.pojoProvisionDetalle.getFacturaMonedaTipoCambio().trim())) 
			return this.pojoProvisionDetalle.getFacturaMonedaTipoCambio();
		return "MXN";
	}

	public void setEgresoDetalleMoneda(double value) {}

	public boolean getEgresoDetalleManoObra() {
		if (this.pojoProvisionDetalle != null)
			return this.pojoProvisionDetalle.getManoObra();
		return false;
	}
	
	public void setEgresoDetalleManoObra(boolean manoObra) {
		if (this.pojoProvisionDetalle != null)
			this.pojoProvisionDetalle.setManoObra(manoObra);
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
		if (this.pojoProvisionDetalle != null)
			return this.pojoProvisionDetalle.getTotalImpuestos();
		return 0;
	}

	public void setTotalImpuestos(double totalImpuestos) {}

	public double getTotalRetenciones() {
		if (this.pojoProvisionDetalle != null)
			return this.pojoProvisionDetalle.getTotalRetenciones();
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

	public List<SelectItem> getTiposBusquedaOrdenCompra() {
		return tiposBusquedaOrdenCompra;
	}

	public void setTiposBusquedaOrdenCompra(List<SelectItem> tiposBusquedaOrdenCompra) {
		this.tiposBusquedaOrdenCompra = tiposBusquedaOrdenCompra;
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

	public boolean isOperacionCancelada() {
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

	public boolean isPerfilRequiereObra() {
		return perfilRequiereObra;
	}

	public void setPerfilRequiereObra(boolean perfilRequiereObra) {
		this.perfilRequiereObra = perfilRequiereObra;
	}
	
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
}
