package net.giro.adp;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.ConceptoPresupuesto;
import net.giro.adp.beans.InsumosExt;
import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.ObraAvance;
import net.giro.adp.beans.ObraCobranza;
import net.giro.adp.beans.ObraContratos;
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.adp.beans.ObraExt;
import net.giro.adp.beans.ObraSatics;
import net.giro.adp.beans.Presupuesto;
import net.giro.adp.beans.PresupuestoDetalleExt;
import net.giro.adp.logica.ConceptoPresupuestoRem;
import net.giro.adp.logica.InsumosRem;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.adp.logica.ObraAvanceRem;
import net.giro.adp.logica.ObraCobranzaRem;
import net.giro.adp.logica.ObraContratosRem;
import net.giro.adp.logica.ObraEmpleadoRem;
import net.giro.adp.logica.ObraRem;
import net.giro.adp.logica.ObraSaticsRem;
import net.giro.adp.logica.PresupuestoDetalleRem;
import net.giro.adp.logica.PresupuestoRem;
import net.giro.clientes.beans.DomicilioExt;
import net.giro.clientes.beans.Persona;
import net.giro.clientes.beans.PersonaExt;
import net.giro.clientes.logica.ClientesRem;
import net.giro.comun.ExcepConstraint;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.FtpRem;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;

import com.google.common.io.Files;

@ViewScoped
@ManagedBean(name="obrasAction")
public class ObrasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ObrasAction.class);
	private PropertyResourceBundle entornoProperties;
	private PropertyResourceBundle msgProperties;
	private HttpSession httpSession;
	private LoginManager loginManager;
	private InitialContext ctx; 
	// Interfaces
	private ConGrupoValoresRem 	ifzGpoVal;
	private ConValoresRem		ifzConValores;
	private SucursalesRem 		ifzSucursales;
	private EmpleadoRem 		ifzEmpleados;
	private InsumosRem 				ifzInsumos;
	private ConceptoPresupuestoRem 	ifzConceptosPresupuesto;
	private PresupuestoRem			ifzPresupuesto;
	private PresupuestoDetalleRem	ifzPresupuestoDetalle;
	private ObraAlmacenesRem ifzObraAlmacenes;
	private AlmacenRem	ifzAlmacenes;
	private FtpRem ifzFtp;
	private FacturaRem ifzFacturas;
	private FacturaDetalleRem ifzFacConceptos;
	private ReportesRem	ifzReportes;	
	// Listas
	private List<ObraAlmacenes>		listObraAlmacenes;
	private List<ObraEmpleadoExt> 	listObraEmpleadosGrid;
	private List<ObraAvance> 		listObraAvances;
	private List<ObraContratos> 	listObraContratos;
	private List<ObraSatics> 		listObraSatics;
	private List<ConValores> 		listEstatusObras;
	private List<Sucursal> 	 		listSucursales;
	private List<Municipio>  		listMunicipios;
	private List<Localidad>  		listLocalidades;
	private List<Colonia> 	 		listColonias;
	private List<Estado> 	 		listTmpEstado;
	private List<ConValores> 		listTmpCatDomicilios1;
	private List<ConValores>		listTmpCatDomicilios2;
	private List<ConValores> listEstatusContratos;
	// Listas auxiliares
	private List<SelectItem> listObrasTiposItems;
	private List<SelectItem> listEstatusObrasItems;
	private List<SelectItem> listCatDomicilios1;
	private List<SelectItem> listCatDomicilios2;
	private List<SelectItem> listEstado;
	private List<SelectItem> listSucursalesItems;
	private List<SelectItem> listEstatusContratoItems;
	private HashMap<Long,String> mapEstatusMensajesExtra;
	// POJO's
	private ObraExt 		pojoObra;
	private ConGrupoValores pojoGpoEstatusObras;
	private ConGrupoValores pojoGpoEstatusContrato;
	private DomicilioExt	pojoDomicilioExt;
	private Estado 			pojoEstado;
	private Municipio 		pojoMunicipio;
	private Localidad 		pojoLocalidad;
	private Colonia 		pojoColonia;
	private ObraEmpleadoExt pojoObraEmpleado;
	private EmpleadoExt		pojoEmpleado;	
	// Busqueda principal
	private ObraRem ifzObras;
	private List<Obra> listObrasGrid;
	private Obra pojoObraMain;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int tipoObraBusqueda;
	private boolean busquedaAdministrativas;
	private int numPagina;
	private boolean existePresupuesto;	
	// Busqueda obras principales
	private List<Obra> listObrasPrincipales;
	private List<SelectItem> tiposBusquedaObras;
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int numPaginaObras;	
	// Busqueda clientes
	private ClientesRem ifzClientes;
	private List<Persona> listClientes;
	private Persona pojoCliente;
	private List<SelectItem> tiposBusquedaClientes;	
	private String campoBusquedaClientes;
	private String valorBusquedaClientes;
	private String valorOpcionBusquedaClientes;
	private int numPaginaClientes;	
	// Busqueda empleados
	private ObraEmpleadoRem  ifzObraEmpleados;
	private List<EmpleadoExt> listEmpleadosGrid;
	private List<SelectItem> tiposBusquedaEmpleados;	
	private String campoBusquedaEmpleados;
	private String valorBusquedaEmpleados;
	private int numPaginaEmpleados;
	private int numPaginaObraEmpleados;	
	// Obra Almacenes
	private List<SelectItem> listAlmacenesItems;
	private List<Almacen> listAlmacenes;
	private ObraAlmacenes pojoObraAlmacen;
	private ObraAlmacenes pojoObraAlmacenBorrar;
	private Almacen pojoAlmacen;
	private int numPaginaObraAlmacenes;	
	// Obra Satics
	private ConGrupoValores pojoGpoSatics;
	private ObraSaticsRem ifzObraSatics;
	private List<SelectItem> listSaticsItems;
	private List<ConValores> listSatics;
	private List<SelectItem> listObraContratosItems;
	private ObraSatics	pojoSatics;
	private ObraSatics	pojoSaticsBorrar;
	private int numPaginaObraSatics;	
	// Busqueda Cobranza
	private ObraCobranzaRem ifzCobranza;
	private List<ObraCobranza> listObraCobranza;
	private int numPaginaObraCobranza;	
	// ObraAvance
	private ObraAvanceRem ifzObraAvance;
	private List<ObraAvance> listObraAvancesEliminados;
	private ObraAvance pojoObraAvance;
	private ObraAvance pojoObraAvanceBorrar;
	private int paginaObraAvance;	
	// ObraContratos
	private ObraContratosRem ifzContratos;
	private ObraContratos pojoObraContrato;
	private ObraContratos pojoObraContratoBorrar;
	private int paginaObraContratos;
	// Subcontratantes
	private ConGrupoValores pojoGpoSubcontratantes;
	private List<ConValores> listSubcontratantes;
	private List<SelectItem> listSubcontratantesItems;
	private long subcontratanteId;
	// Busqueda responsable
	private List<Empleado> listResponsablesGrid;	
	private Empleado pojoResponsable;	
	private List<SelectItem> tiposBusquedaResponsables;	
	private String campoBusquedaResponsables;
	private String valorBusquedaResponsables;
	private int numPaginaResponsables;
	// Busqueda domicilio
	private String busquedaMunicipio;
	private String busquedaLocalidad;
	private String busquedaColonia;
	private int numPaginaMunicipio;
	private int numPaginaLocalidad; 
	private int numPaginaColonia;
	// perfiles
	private boolean puedeEditar;
	private double porcentajeIva;
	private boolean perfilEgresosOperacion;
	// Variables de operacion
    private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
    private long usuarioId;
    private String usuario;
    private Long valObraCancelada;
    private boolean confirmadoNoSatic;
	//presupuesto
	private Presupuesto pojoPresupuesto;
	private boolean cargaInsumosValidada;
	private List<InsumosExt> listaInsumos;
	private List<ConceptoPresupuesto> listaConceptosPresupuesto;
	private List<PresupuestoDetalleExt> listaPresupuestoGrid;
	private int paginaPresupuestoDetalle;	
	private long almacenId;
	private long saticsId;
	private long saticsAdendumId;
	private byte[] fileSrc; 
	private String fileName;
	private String fileExtension;
	private String ftpDigitalizacionHost;
	private String ftpDigitalizacionPort;
	private String ftpDigitalizacionUser;
	private String ftpDigitalizacionPass;
	private String ftpDigitalizacionRuta;	
	private double totalCobranza;
	private boolean permiteGuardarCobranza;
	// Moneda
	private MonedaDAO ifzMonedas;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private Moneda monedaBase;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	
    public ObrasAction() {
		FacesContext facesContext = null;
		Application app = null;
		Map<String, String> params = null;
    	ValueExpression valExp = null;
    	String valPerfil = "";
		
    	try {
			facesContext = FacesContext.getCurrentInstance();
			app = facesContext.getApplication();
			params = facesContext.getExternalContext().getRequestParameterMap();

			this.httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
			
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(facesContext.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(facesContext.getELContext());
			
			valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			this.msgProperties = (PropertyResourceBundle) valExp.getValue(facesContext.getELContext());
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			this.puedeEditar = true; 

			// PERFILES
			comprobarPerfiles();
			
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObraEmpleados = (ObraEmpleadoRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraEmpleadoFac!net.giro.adp.logica.ObraEmpleadoRem");
			this.ifzObraAvance = (ObraAvanceRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAvanceFac!net.giro.adp.logica.ObraAvanceRem");
			this.ifzContratos = (ObraContratosRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraContratosFac!net.giro.adp.logica.ObraContratosRem");
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzClientes = (ClientesRem) this.ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			this.ifzSucursales = (SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzObraAlmacenes = (ObraAlmacenesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzAlmacenes = (AlmacenRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzObraSatics = (ObraSaticsRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraSaticsFac!net.giro.adp.logica.ObraSaticsRem");
			this.ifzFtp = (FtpRem) this.ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzCobranza = (ObraCobranzaRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraCobranzaFac!net.giro.adp.logica.ObraCobranzaRem");
			this.ifzFacturas = (FacturaRem)	this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacConceptos = (FacturaDetalleRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzInsumos = (InsumosRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzConceptosPresupuesto = (ConceptoPresupuestoRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ConceptoPresupuestoFac!net.giro.adp.logica.ConceptoPresupuestoRem");
			this.ifzPresupuesto = (PresupuestoRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoFac!net.giro.adp.logica.PresupuestoRem");
			this.ifzPresupuestoDetalle = (PresupuestoDetalleRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoDetalleFac!net.giro.adp.logica.PresupuestoDetalleRem");
			this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzObraEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzObraAvance.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzContratos.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzSucursales.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzObraAlmacenes.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzAlmacenes.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzObraSatics.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzCobranza.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzFacturas.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzFacConceptos.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzInsumos.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzConceptosPresupuesto.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzPresupuesto.setInfoSesion(this.loginManager.getInfoSesion()); 
			this.ifzPresupuestoDetalle.setInfoSesion(this.loginManager.getInfoSesion()); 
			
			this.ifzObras.showSystemOuts(false);
			this.ifzObraEmpleados.showSystemOuts(false);
			this.ifzObraAlmacenes.showSystemOuts(false);

			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			Moneda valMoneda = this.ifzMonedas.findByAbreviacion(valPerfil);
			if (valMoneda != null)
				this.monedaBase = valMoneda;
			
			this.pojoObra = new ObraExt();
			this.pojoGpoEstatusObras = new ConGrupoValores();
			this.pojoObraAlmacen = new ObraAlmacenes();			
			this.listObrasGrid 			= new ArrayList<Obra>();
			this.listEstatusObras 		= new ArrayList<ConValores>();
			this.listObrasPrincipales 	= new ArrayList<Obra>();
			this.listSucursales 		= new ArrayList<Sucursal>();
			this.listEmpleadosGrid		= new ArrayList<EmpleadoExt>();
			this.listResponsablesGrid	= new ArrayList<Empleado>();
			this.listTmpCatDomicilios1 	= new ArrayList<ConValores>();
			this.listTmpCatDomicilios2 	= new ArrayList<ConValores>();			
			this.listObraAlmacenes		= new ArrayList<ObraAlmacenes>();
			this.listAlmacenes = new ArrayList<Almacen>();
			this.listEstatusObrasItems 	= new ArrayList<SelectItem>();
			this.listEstatusContratoItems = new ArrayList<SelectItem>();
			this.listCatDomicilios1 	= new ArrayList<SelectItem>();
			this.listCatDomicilios2 	= new ArrayList<SelectItem>();
			this.listEstado			 	= new ArrayList<SelectItem>();
			this.listSucursalesItems 	= new ArrayList<SelectItem>();
			this.listAlmacenesItems = new ArrayList<SelectItem>();
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusqueda.add(new SelectItem("nombreObraPrincipal", "Obra Principal"));
			this.tiposBusqueda.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusqueda.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getDescription();
			this.valorBusqueda = "";
			this.tipoObraBusqueda = 0;
			this.numPagina = 1;
			
			// Busqueda obras principales
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
			this.valorBusquedaObras = "";
			this.numPaginaObras = 1;
			
			// Busqueda clientes
			this.listClientes = new ArrayList<Persona>();
			this.pojoCliente = new Persona();
			this.tiposBusquedaClientes = new ArrayList<SelectItem>();
			this.tiposBusquedaClientes.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaClientes.add(new SelectItem("id", "ID"));
			this.campoBusquedaClientes = this.tiposBusquedaClientes.get(0).getDescription();
			this.valorBusquedaClientes = "";
			this.valorOpcionBusquedaClientes = "N";
			this.numPaginaClientes = 1;
			
			// Busquera empleados
			this.tiposBusquedaEmpleados = new ArrayList<SelectItem>();
			this.tiposBusquedaEmpleados.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaEmpleados.add(new SelectItem("id", "ID"));
			this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getDescription();
			this.valorBusquedaEmpleados = "";
			this.numPaginaObraEmpleados = 1;
			this.numPaginaEmpleados = 1;
			
			// Busquera ObraAlmacenes
			this.numPaginaObraAlmacenes = 1;

			// Busquera ObraSatics
			this.numPaginaObraSatics = 1;
			
			// Obra Avances
			this.listObraAvances = new ArrayList<ObraAvance>();
			this.listObraAvancesEliminados = new ArrayList<ObraAvance>();
			this.pojoObraAvance = new ObraAvance();
			this.pojoObraAvanceBorrar = null;
			this.paginaObraAvance = 1;
			
			// ObraContratos
			this.listObraContratos = new ArrayList<ObraContratos>();
			this.pojoObraContrato = new ObraContratos();
			this.pojoObraContratoBorrar = null;
			this.paginaObraContratos = 1;
			
			// Subcontratantes
			this.listSubcontratantes = new ArrayList<ConValores>();
			this.listSubcontratantesItems = new ArrayList<SelectItem>();
			this.subcontratanteId = 0L;
			
			// Busqueda responsable
			this.tiposBusquedaResponsables = new ArrayList<SelectItem>();
			this.tiposBusquedaResponsables.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaResponsables.add(new SelectItem("id", "ID"));
			this.campoBusquedaResponsables = this.tiposBusquedaResponsables.get(0).getDescription();
			this.valorBusquedaResponsables = "";
			this.numPaginaResponsables = 1;

			this.ftpDigitalizacionHost = entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = entornoProperties.getString("ftp.digitalizacion.ruta");
			
			// GRUPOS
			comprobarGrupos();

			// Cargo monedas
			cargarMonedas();
			
			// Cargamos datos
			cargaEstados();
			cargaSucursales();
			nuevoDomicilio();
			
			this.listaPresupuestoGrid = new ArrayList<>();
			this.numPaginaMunicipio = 1;
			this.numPaginaLocalidad = 1; 
			this.numPaginaColonia = 1;
			
			this.pojoPresupuesto = new Presupuesto();
			this.pojoSatics = new ObraSatics();
			this.totalCobranza = 0;
			this.permiteGuardarCobranza = false;
			this.numPaginaObraCobranza = 1;
			this.paginaPresupuestoDetalle = 1;
			
			this.listObrasTiposItems = new ArrayList<SelectItem>();
			this.listObrasTiposItems.add(new SelectItem(1, this.msgProperties.getString("obrasTipo.obra")));
			this.listObrasTiposItems.add(new SelectItem(2, this.msgProperties.getString("obrasTipo.proyecto")));
			this.listObrasTiposItems.add(new SelectItem(3, this.msgProperties.getString("obrasTipo.ordenTrabajo")));
			if (this.perfilEgresosOperacion)
				this.listObrasTiposItems.add(new SelectItem(4, this.msgProperties.getString("obrasTipo.administrativa")));
		} catch (Exception e) {
			log.error("Error en GestionProyectos.ObrasAction.constructor ObrasAction", e);
		}
    }
    

    public void buscar() throws Exception {
    	try {
    		control();
    		this.numPagina = 1;
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();

			this.tipoObraBusqueda = 0;
			if (this.busquedaAdministrativas)
				this.tipoObraBusqueda = 4;

    		log.info("Buscando Obra. " + this.campoBusqueda + ": " + this.valorBusqueda);
    		this.ifzObras.orderBy(this.campoBusqueda);
    		if ("nombreObraPrincipal".equals(this.campoBusqueda))
    			this.listObrasGrid = this.ifzObras.findPrincipalLike("nombre", this.valorBusqueda, this.tipoObraBusqueda, "nombre", 0);
    		else
    			this.listObrasGrid = this.ifzObras.findLikeProperty(this.campoBusqueda, this.valorBusqueda, this.tipoObraBusqueda);
			if (this.listObrasGrid == null || this.listObrasGrid.isEmpty()) {
	    		control(2, "Busqueda sin resultados");
	    		return;
			}
			
			log.info(this.listObrasGrid.size() + " Obras encontradas");
    	} catch (Exception e) {
			if (this.listObrasGrid != null)
				this.listObrasGrid.clear();
    		control(true, -1, "Ocurrio un problema al intentar consultar las Obras", e);
    	}
    }

    public void nuevo() {
    	try {
    		control();
			this.pojoObraMain = new Obra();
			this.pojoObra = new ObraExt();
			this.pojoDomicilioExt = null;
			
			nuevoDomicilio();
			this.pojoObra.setTipoObra(1);
			this.pojoObra.setTipoCambio(1);
			this.pojoObra.setIdMoneda(this.monedaBase.getId());
			this.pojoObra.setDescMoneda(this.monedaBase.getNombre());
			
			if (this.listEstatusObras.isEmpty()) 
				if (! cargaEstatusObras())
					return;
			
			if (this.listEstatusContratoItems.isEmpty()) 
				if (! cargaEstatusContrato())
					return;
			
			if (this.listSucursales.isEmpty())
				if (! cargaSucursales())
					return;
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar generar una nueva Obra", e);
    	}
    }
    
    public void editar() throws Exception {
    	try {
    		control();
			this.pojoObra = this.ifzObras.convertir(this.pojoObraMain);
			if (this.pojoObra.getIdMoneda() == null || this.pojoObra.getIdMoneda() <= 0L) {
				this.pojoObra.setIdMoneda(this.monedaBase.getId());
				this.pojoObra.setDescMoneda(this.monedaBase.getNombre());
			}

			nuevoDomicilio();
			if (this.pojoObra.getPojoDomicilio() != null) {
				this.pojoDomicilioExt = this.pojoObra.getPojoDomicilio();
				desglosaDomicilio();
			}
			
			if (this.listEstatusObras.isEmpty()) 
				if (! cargaEstatusObras())
					return;
			
			if (this.listEstatusContratoItems.isEmpty()) 
				if (! cargaEstatusContrato())
					return;
			
			if (this.listSucursales.isEmpty())
				if (! cargaSucursales())
					return;
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar recuperar la Obra seleccionada", e);
    	}
    }
    
    public void guardar() throws Exception {
		int index = -1;
		
    	try {
    		control();
			if (! validaciones()) 
				return;
			
			this.pojoObra.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			this.pojoObra.setMontoContratado(this.pojoObra.getMontoContratado().setScale(2, BigDecimal.ROUND_CEILING));
			this.pojoObra.setMontoAnticipo(this.pojoObra.getMontoAnticipo().setScale(2, BigDecimal.ROUND_CEILING));
			this.pojoObra.setMontoDeductiva(this.pojoObra.getMontoDeductiva().setScale(2, BigDecimal.ROUND_CEILING));

			if (this.pojoObra.getId() == null) {
				this.pojoObra.setPojoDomicilio(this.pojoDomicilioExt);
				this.pojoObra.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoObra.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoObra.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoObra.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD
				this.pojoObra.setId(this.ifzObras.save(this.pojoObra));
				this.pojoObraMain = this.ifzObras.convertir(this.pojoObra);
				
				// Agregamos a la lista
				this.listObrasGrid.add(this.pojoObraMain);
			} else {
				// Actualizamos el pojo de la BD
				this.pojoObra.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.pojoObra.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzObras.update(this.pojoObra);

				// Actualizamos el elemento de la lista
				for (Obra var : this.listObrasGrid) {
					if (var.getId().longValue() == this.pojoObra.getId().longValue()) {
						index = this.listObrasGrid.indexOf(var);
						break;
					}
				}
				
				// Agregamos a la lista
				if (index >= 0)
					this.listObrasGrid.set(index, this.ifzObras.convertir(this.pojoObra));
			}

    		this.confirmadoNoSatic = false;
			this.pojoObraMain = new Obra();
			this.pojoObra = new ObraExt();
			this.pojoObra.setTipoObra(1);
			this.pojoDomicilioExt = null;
			nuevoDomicilio();
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar Guardar la Obra", e);
    	}
    }

    public String eliminar() {
    	try {
    		control();			
			// Actualizamos el elemento de la lista
			for (Obra var : this.listObrasGrid) {
				if (var.getId() == this.pojoObraMain.getId()) {
					// Validamos si ya tenemos el ID del estatus CANCELADO para obra
					if (this.valObraCancelada == null) {
						// Intentamos obtener el ID
						this.valObraCancelada = this.ifzObras.findEstatusCanceladoObras(this.pojoGpoEstatusObras);
						if (this.valObraCancelada == null) {
							control(-1, "No se pudo determinar el estatus CANCELADO para obras.");
							return this.mensaje;
						}
					}
					
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					var.setEstatus(this.valObraCancelada);
					
					// Actualizamos el pojo de la BD
					this.ifzObras.update(var);
					break;
				}
			}

			nuevo();
			this.mensaje = "OK";
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar Eliminar la Obra seleccionada", e);
    	}
    	
    	return this.mensaje;
    }

    private void comprobarPerfiles() {
    	String valPerfil = "";

		// PORCENTAJE IVA
		valPerfil = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
		this.porcentajeIva = ((valPerfil != null && ! "".equals(valPerfil)) ? Double.valueOf(valPerfil) : 0);
		
		// EGRESOS OPERACION
		valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
		this.perfilEgresosOperacion = ((valPerfil != null && ! "".equals(valPerfil) && "S".equals(valPerfil)) ? true : false);
    }
    
    private void comprobarGrupos() throws Exception {
    	// GRUPO ESTATUS DE OBRA
		this.pojoGpoEstatusObras = this.ifzGpoVal.findByName("SYS_ESTATUS_OBRA");
		if (this.pojoGpoEstatusObras == null)
			throw new Exception("No se encontro encontro el grupo SYS_ESTATUS_OBRA en con_grupo_valores");
		cargaEstatusObras();
		
		// GRUPO ESTATUS DE CONTRATO
		this.pojoGpoEstatusContrato = this.ifzGpoVal.findByName("SYS_ESTATUS_CONTRATO");
		if (this.pojoGpoEstatusContrato == null)
			throw new Exception("No se encontro encontro el grupo SYS_ESTATUS_CONTRATO en con_grupo_valores");
		cargaEstatusContrato();
		
		// GRUPO OBRA SATICS
		this.pojoGpoSatics = this.ifzGpoVal.findByName("SYS_SATICS");
		if (this.pojoGpoSatics == null)
			throw new Exception("No se encontro encontro el grupo SYS_SATICS en con_grupo_valores");
		cargarSatics();
		
		// GRUPO SUBCONTRATANTE
		this.pojoGpoSubcontratantes = this.ifzGpoVal.findByName("SYS_SUBCONTRATANTE");
		if (this.pojoGpoSubcontratantes == null) 
			throw new Exception("No se encontro encontro el grupo SYS_SUBCONTRATANTE en con_grupo_valores");
		cargaSubcontratantes();
    }

    private void cargarMonedas() {
		try {
			log.info("Cargando Monedas");
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

    private boolean esEstatusCancelado(String value) {
    	if (value == null || "".equals(value))
    		value = "";
    	
    	if ("cancelado".equals(value.toLowerCase()))
    		return true;
    	if ("cancelada".equals(value.toLowerCase()))
    		return true;
    	if (value.toLowerCase().contains("cancelado"))
    		return true;
    	if (value.toLowerCase().contains("cancelada"))
    		return true;
    	
    	return false;
    }
    
    public boolean validaciones() {
    	if (this.pojoObra.getMontoContratado().doubleValue() <= 0) {
    		control(15, "El Monto Contratado debe ser mayor a cero");
			return false;
		}
		
		if (this.pojoObra.getMontoAnticipo().doubleValue() < 0) {
			// El Arq. Carolina me pidio por correo que no importa el tipo, se permite cero o mayor
			//if ((this.pojoObra.getTipoObra() != 3) || (this.pojoObra.getTipoObra() == 3 && this.pojoObra.getMontoAnticipo().doubleValue() < 0)) {
	    		control(16, "El Monto Anticipo debe ser mayor a cero");
				return false;
			//}
		}
		
		if (this.pojoObra.getMontoDeductiva().doubleValue() < 0) {
    		control(17, "El Monto Deductiva debe ser mayor a cero");
			return false;
		}
    	
    	if (this.pojoObra.getIdMoneda() == null || this.pojoObra.getIdMoneda() <= 0L) {
    		control(18, "Debe seleccionar una Moneda");
			return false;
    	}

    	if (this.pojoObra.getFechaInicio().equals(this.pojoObra.getFechaTerminacion()) || this.pojoObra.getFechaTerminacion().before(this.pojoObra.getFechaInicio())) {
    		control(19, "La Fecha de Terminacion debe ser Mayor a la de Inicio");
			return false;
    	}
		
		// Tipo de Obra
		if (this.pojoObra.getTipoObra() <= 0)
			this.pojoObra.setTipoObra(1);
		
		// Tipo de Cambio
		if (this.pojoObra.getTipoCambio() <= 0)
			this.pojoObra.setTipoCambio(1);

		// Moneda
		for (Moneda var : this.listMonedas) {
			if (this.pojoObra.getIdMoneda().longValue() == var.getId()) {
				this.pojoObra.setIdMoneda(var.getId());
				this.pojoObra.setDescMoneda(var.getNombre());
				break;
			}
		}

    	if ((this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) && ! this.confirmadoNoSatic && getConfirmarSatic()) 
    		return false;
		
    	return true;
    }
    
    public String buscarMunicipios() throws Exception {
    	try {
    		control();
			
			this.listMunicipios = ifzClientes.buscarMunicipio(this.pojoEstado, this.busquedaMunicipio);
			if (this.listMunicipios.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return this.mensaje;
			}
			
			this.mensaje = "OK";
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar consultar los Municipios", e);
    	}
    	
    	return this.mensaje;
    }
    
    public String buscarLocalidades() throws Exception {
    	try {
			control();			
			this.listLocalidades = this.ifzClientes.buscarLocalidad(this.pojoMunicipio, this.busquedaLocalidad);
			if (this.listLocalidades.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return this.mensaje;
			}
			this.mensaje = "OK";
    	} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar consultar las Localidades", e);
    	}
    	
    	return this.mensaje;
    }
    
    public String buscarColonias() throws Exception {
    	try {
			control();			
			this.listColonias = this.ifzClientes.buscarColonia(this.pojoLocalidad, this.busquedaColonia);
			if (this.listColonias.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return this.mensaje;
			}
			this.mensaje = "OK";
    	} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar consultar las Colonias", e);
    	}
    	
    	return this.mensaje;
    }
    
    public void revisado() throws Exception {  
		int index = -1;
		
    	try {
    		control();			
			if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L) {
				for (Obra var : this.listObrasGrid) {
					if (this.pojoObra.getId() == var.getId()) {
						index = this.listObrasGrid.indexOf(var);
						this.pojoObraMain = this.ifzObras.revisado(var, this.usuarioId, Calendar.getInstance().getTime());
						this.pojoObra.setRevisado(this.pojoObraMain.getRevisado());
						this.pojoObra.setRevisadoPor(this.pojoObraMain.getRevisadoPor());
						this.pojoObra.setFechaRevisado(this.pojoObraMain.getFechaRevisado());
						break;
					}
				}
			}
			
			if (index >= 0)
				this.listObrasGrid.set(index,  this.pojoObraMain);
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar marcar como revisada la Obra", e);
    	}
    }
    
    public boolean guardarDomicilio() {
		Respuesta respuesta = new Respuesta();
		
		try {
			control();
			this.pojoDomicilioExt.setPrincipal(true);
			this.pojoDomicilioExt.setEstatus(true);
			this.pojoDomicilioExt.setColonia(pojoColonia);
			this.ifzClientes.setInfoSecion(this.loginManager.getInfoSesion());
			respuesta = this.ifzClientes.salvar(this.pojoDomicilioExt);
			if (respuesta.getErrores().getCodigoError() > 0) {
				control(-1, respuesta.getErrores().getDescError());
				return false;
			}
			
			this.pojoDomicilioExt = (DomicilioExt) respuesta.getBody().getValor("domicilio");
			this.pojoObra.setPojoDomicilio(pojoDomicilioExt);
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar Guardar el Domicilio", e);
		}
		
		return ! this.operacionCancelada;
	}
    
    public boolean cargaEstatusObras() {
    	try {
    		control();			
			this.listEstatusObras = this.ifzConValores.findAll(this.pojoGpoEstatusObras);
			if (this.listEstatusObras.isEmpty()) {
	    		control(-1, "No se encontro ningun estatus para obras.");
				return false;
			}

			this.listEstatusObrasItems.clear();
			for (ConValores var : this.listEstatusObras) {
				if (esEstatusCancelado(var.getValor().toLowerCase()))
					this.valObraCancelada = var.getId();
				this.listEstatusObrasItems.add(new SelectItem(var.getId(), var.getValor()));
			}
			
			// TODO: ARREGLO MIENTRAS SE DEFINE EL ESTATUS "CANCELADO" PARA OBRAS: POR AHORA NO ESTA EN SYS_ESTATUS_OBRA
			if (this.valObraCancelada == null)
				this.valObraCancelada = 0L;
			
			this.mensaje = "OK";
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar consultar los Estatus de Obras", e);
    		return false;
    	}
    	
    	return true;
    }
    
    public boolean cargaEstatusContrato() {
    	try {
			control();
			this.listEstatusContratos = this.ifzConValores.findAll(this.pojoGpoEstatusContrato);						
			if (this.listEstatusContratos == null || this.listEstatusContratos.isEmpty()) {
	    		control(-1, "No se encontro ningun estatus para contrato.");
				return false;
			}
			if (this.mapEstatusMensajesExtra == null)
				this.mapEstatusMensajesExtra = new HashMap<Long,String>();
			this.mapEstatusMensajesExtra.clear();

			// llenamos lista
			if (this.listEstatusContratoItems == null)
				this.listEstatusContratoItems = new ArrayList<SelectItem>();
			this.listEstatusContratoItems.clear();
			for (ConValores var : this.listEstatusContratos) {
				this.listEstatusContratoItems.add(new SelectItem(var.getId(), var.getValor()));
				if ("SIN CONTRATO".equals(var.getValor().trim().toUpperCase()))
					this.mapEstatusMensajesExtra.put(var.getId(), "SIN CONTRATO");
			}
			
			this.mensaje = "OK";
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar consultar los Estatus de Contratos para Obras", e);
    		return false;
    	}
    	
    	return true;
    }
    
    public boolean cargaSucursales() {
    	try {
    		control();
			this.listSucursales = this.ifzSucursales.findLikePropiedad("sucursal", "");
						
			if (this.listSucursales.isEmpty()) {
	    		control(-1, "No se encontro ningun estatus para obras.");
				return false;
			}

			// Formas de pagos
			this.listSucursalesItems.clear();
			for (Sucursal var : this.listSucursales) {
				this.listSucursalesItems.add(new SelectItem(var.getId(), var.getSucursal()));
			}
			
			this.mensaje = "OK";
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar consultar las Sucursales", e);
			return false;
    	}
    	
		return true;
    }
    
    public boolean cargaEstados() {
    	try {
			control();
			this.listEstado = new ArrayList<SelectItem>();
			this.listTmpEstado = this.ifzClientes.buscarEstados();
			
			if (this.listTmpEstado.isEmpty()) {
				control(-1, "No se encontro ningun estado.");
				return false;
			}

			this.listEstado.clear();
			for (Estado e : listTmpEstado) {
				listEstado.add(new SelectItem(e.getId(), e.getNombre()));
			}
			
			this.mensaje = "OK";
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar consultar los Estados", e);
			return false;
		}
    	
    	return true;
    }
    
    public void calcularAnticipoPorcentaje() {
		if (this.pojoObra.getMontoAnticipo().doubleValue() > 0D) {
			// Calculamos en base al monto de anticipo especificado
			calcularAnticipoPorMonto();
    	} else if (this.pojoObra.getPorcentajeAnticipo() > 0D) {
    		// Calculamos en base al porcentaje especificado
    		calcularAnticipoPorPorcentaje();
    	} else {
    		// Inicializamos en cero
    		this.pojoObra.setMontoAnticipo(BigDecimal.ZERO);
    		this.pojoObra.setPorcentajeAnticipo(0D);
    	}
    }
        
    public void calcularAnticipoPorMonto() {
    	Double porcentaje = 0D;
    	
    	if (this.pojoObra.getMontoContratado().doubleValue() > 0D && this.pojoObra.getMontoAnticipo().doubleValue() > 0D) {
    		porcentaje = ((this.pojoObra.getMontoAnticipo().doubleValue() * 100) / this.pojoObra.getMontoContratado().doubleValue());
    		this.pojoObra.setPorcentajeAnticipo(porcentaje);
    	}
    }
        
    public void calcularAnticipoPorPorcentaje() {
    	Double monto = 0D;
    	
    	if (this.pojoObra.getMontoContratado().doubleValue() > 0D && this.pojoObra.getPorcentajeAnticipo() > 0D) {
    		monto = ((this.pojoObra.getMontoContratado().doubleValue() * this.pojoObra.getPorcentajeAnticipo()) / 100);
    		this.pojoObra.setMontoAnticipo(new BigDecimal(monto));
    	}
    }
    
    public void nuevaBusquedaEmpleados() {
    	this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getDescription();
		this.valorBusquedaEmpleados = "";
		this.numPaginaEmpleados = 1;
    	
    	if (this.listEmpleadosGrid == null)
    		this.listEmpleadosGrid = new ArrayList<EmpleadoExt>();
    	this.listEmpleadosGrid.clear();
    	this.pojoEmpleado = new EmpleadoExt();
    	this.pojoObraEmpleado = new ObraEmpleadoExt();
    }
    
    public void nuevoDomicilio() {
    	if (this.pojoDomicilioExt != null)
    		this.pojoDomicilioExt = null;
    		
		this.pojoDomicilioExt = new DomicilioExt();
		this.pojoDomicilioExt.setEstatus(true);
		this.pojoDomicilioExt.setPersona(this.pojoObra.getIdCliente());
		this.pojoDomicilioExt.setDomicilio("");
		this.pojoDomicilioExt.setCatDomicilio1(0L);
		this.pojoDomicilioExt.setCatDomicilio2(0L);
		this.pojoDomicilioExt.setCatDomicilio3(0L);
		this.pojoDomicilioExt.setCatDomicilio4(0L);
		this.pojoDomicilioExt.setCatDomicilio5(0L);
		this.pojoDomicilioExt.setDescripcionDomicilio1("");
		this.pojoDomicilioExt.setDescripcionDomicilio2("");
		this.pojoDomicilioExt.setDescripcionDomicilio3("");
		this.pojoDomicilioExt.setDescripcionDomicilio4("");
		this.pojoDomicilioExt.setDescripcionDomicilio5("");
		
		desglosaDomicilio();
	}
    
    public void desglosaDomicilio() {
    	try {
        	this.pojoColonia = null;
    		this.pojoLocalidad = null;
    		this.pojoMunicipio = null;
    		this.pojoEstado = null;
    		
    		if (this.pojoDomicilioExt != null) {
    			if (this.pojoDomicilioExt.getColonia() != null && this.pojoDomicilioExt.getColonia().getId() > 0) 
    				this.pojoColonia = this.pojoDomicilioExt.getColonia();
    			
    			if (this.pojoColonia != null)
    				this.pojoLocalidad = this.pojoColonia.getLocalidad();
    			
    			if (this.pojoLocalidad != null)
    				this.pojoMunicipio = this.pojoLocalidad.getMunicipio();
    			
    			if (this.pojoMunicipio != null)
    				this.pojoEstado = this.pojoMunicipio.getEstado();
    		}
			
			cargaTiposDomicilio();
		} catch (Exception e) {
			log.error("Error en GestionProyectos.ObrasAction.metodo desglosaDomicilio",e);
		}
    }
    
    public void evaluaSinContrato() {
    	if (this.pojoObra.getEstatusContrato() != null && this.pojoObra.getEstatusContrato() > 0L) {
    		if (! this.mapEstatusMensajesExtra.containsKey(this.pojoObra.getEstatusContrato()))
    			return;
    		if (this.pojoObra.getNombreContrato() == null || "".equals(this.pojoObra.getNombreContrato().trim()))
    			this.pojoObra.setNombreContrato(this.mapEstatusMensajesExtra.get(this.pojoObra.getEstatusContrato()));
    		if (this.pojoObra.getObjetoContrato() == null || "".equals(this.pojoObra.getObjetoContrato().trim()))
    			this.pojoObra.setObjetoContrato(this.mapEstatusMensajesExtra.get(this.pojoObra.getEstatusContrato()));
    	}
    }
    
    private void cargaTiposDomicilio() {
		HashMap<String, String> params = null;
		
		try {
    		control();
			if (listTmpCatDomicilios1 == null)
				listTmpCatDomicilios1 = new ArrayList<ConValores>();
			else
				listTmpCatDomicilios1.clear();
			
			if (listTmpCatDomicilios2 == null)
				listTmpCatDomicilios2 = new ArrayList<ConValores>();
			else
				listTmpCatDomicilios2.clear();
			
			if (listCatDomicilios1==null) 
				listCatDomicilios1 = new ArrayList<SelectItem>();
			else
				listCatDomicilios1.clear();
			
			if (listCatDomicilios2==null) 
				listCatDomicilios2 = new ArrayList<SelectItem>();
			else
				listCatDomicilios2.clear();
			
			params = new HashMap<String, String>();
			params.put("atributo2", "1");
			cargaTipoDom(params, listCatDomicilios1, listTmpCatDomicilios1);
			
			params.clear();
			params.put("atributo2", "2");
			cargaTipoDom(params, listCatDomicilios2, listTmpCatDomicilios2);
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar consultar los Tipos de Domicilio", e);
		}
	}
        	
	private void cargaTipoDom(HashMap<String, String> params, List<SelectItem> items, List<ConValores> listDom) {
		try {
    		control();
			listDom.addAll(ifzClientes.buscarDomicilios(params));

			for (ConValores cv : listDom)
				items.add(new SelectItem(cv.getId(), cv.getValor()));
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar cargar los Tipos de Domicilios", e);
		}
	}
			
	private Estado getEstadoById(Long id) {
		for (Estado e : this.listTmpEstado) {
			if (e.getId() == id.longValue()) 
				return e;
		}
		
		return null;
	}
	    	
	public void cambioEstadoDom() {
		try {
			if (this.listMunicipios != null)
				this.listMunicipios.clear();
			
			if (this.listLocalidades != null)
				this.listLocalidades.clear();
			
			if (this.listColonias != null)
				this.listColonias.clear();
			
			this.pojoMunicipio = null;
			this.pojoLocalidad = null;
			this.pojoColonia = null;
		} catch (Exception e) {
			log.error("Error en GestionProyectos.ObrasAction.metodo cambioEstadoDom", e);
		}
	}
			
	public void inicializaEstadoDom() {
		try {
			this.pojoLocalidad = null;
			this.pojoDomicilioExt.setColonia(null);
			
			if (this.listLocalidades != null)
				this.listLocalidades.clear();
			
			if (this.listColonias != null)
				this.listColonias.clear();
		} catch (Exception e) {
			log.error("Error en GestionProyectos.ObrasAction.metodo inicializaEstadoDom", e);
		}
	}
			
	public void limpiaLocalidades() {
		if (this.listLocalidades != null)
			this.listLocalidades.clear();
		else
			this.listLocalidades = new ArrayList<Localidad>();
	}
			
	public void limpiaMunicipio() {
		if (this.listMunicipios != null)
			this.listMunicipios.clear();
		else
			this.listMunicipios = new ArrayList<Municipio>();
	}
			
	public void limpiaColonias() {
		if (this.listColonias != null)
			this.listColonias.clear();
		else
			this.listColonias = new ArrayList<Colonia>();
	}

	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}
	
	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		if (this.operacionCancelada)
			log.error("\n\nOBRAS :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n", throwable);
	}

	// -------------------------------------------------------------------------------------------------------------------
	// BUSQUEDA OBRAS
	// -------------------------------------------------------------------------------------------------------------------
	
    public void nuevaBusquedaObras() {
    	this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
		this.valorBusquedaObras = "";
		this.numPaginaObras = 1;
		
		if (this.listObrasPrincipales != null)
			this.listObrasPrincipales.clear();
		
		nuevaBusquedaClientes();
		nuevoDomicilio();
    }
    
    public void buscarObras() {
    	try {
    		control();
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();

			log.info("Buscando Obras principales");
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObrasPrincipales = this.ifzObras.findObraPrincipalLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.tipoObraBusqueda, (this.pojoObra.getId() == null ? 0 : this.pojoObra.getId()));
			if (this.listObrasPrincipales.isEmpty()) {
	    		control(2, "Busqueda sin resultados");
				return;
			}

			log.info(listObrasPrincipales.size() + " obras encontradas");
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar consultar las Obras principales", e);
    	}
    }

    public void seleccionarObra() {
    	//this.pojoObra.setIdObraPrincipal(this.pojoObraPrincipal);
    	nuevaBusquedaObras();
    }
    
    public void quitarObra() {
    	try {
    		control();
    		if (this.pojoObra.getIdObraPrincipal() != null && this.pojoObra.getIdObraPrincipal().getId() != null && this.pojoObra.getIdObraPrincipal().getId() > 0L) {
    			this.pojoObra.setIdObraPrincipal(new Obra());
    		}
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al Quitar la Obra Principal", e);
    	}
    }

	// -------------------------------------------------------------------------------------------------------------------
	// BUSQUEDA CLIENTES
	// -------------------------------------------------------------------------------------------------------------------
	
    public void nuevaBusquedaClientes() {
    	this.campoBusquedaClientes = this.tiposBusquedaClientes.get(0).getDescription();
		this.valorBusquedaClientes = "";
		this.valorOpcionBusquedaClientes = "N";
		this.numPaginaClientes = 1;
		
		if (this.listClientes == null)
			this.listClientes = new ArrayList<Persona>();
		this.listClientes.clear();
		this.pojoCliente = new Persona();
    }
    
    public void buscarClientes() throws Exception {
    	try {
    		control();
			if ("".equals(this.valorOpcionBusquedaClientes))
				this.valorOpcionBusquedaClientes = "P";
			
			if ("".equals(this.campoBusquedaClientes))
				this.campoBusquedaClientes = "nombre";
			
			log.info("Buscando clientes");
			this.listClientes = this.ifzObras.findClienteLikeProperty(this.campoBusquedaClientes, this.valorBusquedaClientes, this.valorOpcionBusquedaClientes);
			if (this.listClientes == null || this.listClientes.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}

			log.info(this.listClientes.size() + " clientes (" + (("P".equals(this.valorOpcionBusquedaClientes)) ? "Personas" : "Negocios") + ") encontrados");
		} catch (Exception e) {
    		if (this.listClientes != null)
    			this.listClientes.clear();
    		control(true, -1, "Ocurrio un problema al intentar consultar los Clientes", e);
    	}
    }

    public void seleccionarCliente() {
    	PersonaExt pojoExt = null;
    	
    	try {
			log.info("Extiendo cliente seleccionado");
			pojoExt = this.ifzObras.extenderPersona(this.pojoCliente);
			if (pojoExt == null) {
				log.info("No pude extender el cliente seleccionado - ERROR INTERNO");
				control(-1, "ERROR INTERNO - No se pudo recuperar el Cliente seleccionado");
				return;
			}

			log.info("Asigno cliente a obra");
			this.pojoObra.setIdCliente(pojoExt);
	    	this.pojoObra.setTipoCliente(this.valorOpcionBusquedaClientes);
	    	nuevaBusquedaClientes();
		} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar recuperar el Cliente seleccionado", e);
		}
    }

	// -------------------------------------------------------------------------------------------------------------------
    // BUSQUEDA RESPONSABLES
    // -------------------------------------------------------------------------------------------------------------------

    public void nuevaBusquedaResponsables() {
    	this.campoBusquedaResponsables = this.tiposBusquedaResponsables.get(0).getDescription();
		this.valorBusquedaResponsables = "";
    	this.numPaginaResponsables = 1;
    	
    	if (this.listResponsablesGrid == null)
    		this.listResponsablesGrid = new ArrayList<Empleado>();
    	this.listResponsablesGrid.clear();
    	this.pojoResponsable = new Empleado();
    }

	public void buscarResponsables() {
		try {
			control();
			if ("".equals(this.campoBusquedaResponsables)) {
				this.campoBusquedaResponsables = "nombre";
			}

			log.info("Buscando responsables");
			this.listResponsablesGrid = this.ifzEmpleados.findLikeProperty(this.campoBusquedaResponsables, this.valorBusquedaResponsables, 0);
			if (this.listResponsablesGrid.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}

			log.info(this.listResponsablesGrid.size() + " responsables encontrados");
		} catch (Exception e) {
			if (this.listResponsablesGrid != null)
    			this.listResponsablesGrid.clear();
			control(true, -1, "Ocurrio un problema al intentar consultar los Empleados", e);
		}
	}

    public void seleccionarResponsable() {
    	EmpleadoExt pojoExt = null;
    	
    	try {
			log.info("Extiendo responsable (Empleado) seleccionado");
			pojoExt = this.ifzEmpleados.convertir(this.pojoResponsable);
			if (pojoExt == null) {
				log.info("No pude extender el responsable (Empleado) seleccionado - ERROR INTERNO");
				control(-1, "ERROR INTERNO - No se pudo recuperar el responsable (Empleado) seleccionado");
				return;
			}

			log.info("Asigno responsable (Empleado) a obra");
			this.pojoObra.setIdResponsable(pojoExt);
	    	nuevaBusquedaResponsables();
		} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar recuperar el Empleado seleccionado", e);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// OBRA PRESUPUESTO 
	// -------------------------------------------------------------------------------------------------------------------
	
    public void verificarCargaInsumos() {
    	//1.	El sistema valida que exista la carga de insumos para la obra, verificando que exista el registro en la clase insumos para la obra correspondiente. 
    	//		(insumos.idObra=idObra).
    	try {
    		control();
        	this.cargaInsumosValidada = false;
        	this.existePresupuesto = false;
    		//Primero, se debería revisar si ya existe un presupuesto cargado para esta obra: Se hace obvio que hay obra, puesto que solo cuando se edita es posible acceder a la forma
    		List<Presupuesto> presupuestos = this.ifzPresupuesto.findByProperty("idObra.id", this.pojoObra.getId(), 1);
    		
    		if (! presupuestos.isEmpty()) {	//debe tener uno para considerar que ya se tiene un presupuesto agregado a esta obra
    			this.existePresupuesto = true;
    			this.pojoPresupuesto = presupuestos.get(0);
        		this.listaPresupuestoGrid = this.ifzPresupuestoDetalle.findByPropertyExt("idPresupuesto", this.pojoPresupuesto.getId(), 0);
        		
        		if (this.listaPresupuestoGrid.size() == 16) {
            		montoPorcentajeCapturables();
            		
            		if (this.listaPresupuestoGrid.get(13).getMonto() == null || this.listaPresupuestoGrid.get(13).getMonto().doubleValue() <= 0) {
            			// A14 = A5 + A13
            			this.listaPresupuestoGrid.get(13).setMonto(this.listaPresupuestoGrid.get(4).getMonto().add(this.listaPresupuestoGrid.get(12).getMonto()));
            		}
            		
            		// porcentaje de IVA: Tomado por perfil
            		if (this.listaPresupuestoGrid.get(14).getMonto().doubleValue() <= 0) {
            			this.listaPresupuestoGrid.get(14).setPorcentaje(this.porcentajeIva);
            		}
            		
            		if (this.isDebug) 
            			log.info("Ya existe presupuesto para esta obra");
        			this.cargaInsumosValidada = true;
        			return;
        		}
    		}
    		
    		//Si se llegó a este punto, quiere decir que se cargará un nuevo presupuesto
    		this.pojoPresupuesto = new Presupuesto();
    		this.listaInsumos = this.ifzInsumos.findByPropertyExt("idObra", this.pojoObra.getId(), 0);
    		
    		// Comprobamos que los insumos no esten activos (con estatus = 0)
    		for (InsumosExt var : listaInsumos) {
    			if (var.getEstatus() == 0) {
    				this.cargaInsumosValidada = true;
    				cargarConceptosPresupuesto();
    				break;
    			}
    		}
    		
    		if (this.listaInsumos == null || this.listaInsumos.isEmpty()) {
    	    	this.cargaInsumosValidada = false;
    	    	control(-1, "El archivo de insumos debe cargarse previamente");
    			return;
    		}
    	} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar verificcar la Explosion de Insumos de la Obra", e);
		}
    }

    private void cargarConceptosPresupuesto() {
    	this.listaConceptosPresupuesto = this.ifzConceptosPresupuesto.findAllActivos();
    	if (this.isDebug) 
    		log.info("Carga de Conceptos: " + this.listaConceptosPresupuesto.size());
    	
    	//Recorrer la lista de Conceptos de Presupuesto e ir cargando a la lista del Presupuesto --> this.listaPresupuestoGrid
    	this.listaPresupuestoGrid = new ArrayList<PresupuestoDetalleExt>();
    	for (ConceptoPresupuesto var : this.listaConceptosPresupuesto) {
    		PresupuestoDetalleExt presupuesto = new PresupuestoDetalleExt();
    		presupuesto.setConceptoPresupuesto(var);
    		presupuesto.setMonto(null);
    		presupuesto.setFormula(var.getFormula());
    		
    		this.listaPresupuestoGrid.add(presupuesto);
    	}
    	
    	if (this.listaPresupuestoGrid.get(14).getPorcentaje() <= 0) {
			this.listaPresupuestoGrid.get(14).setPorcentaje(this.porcentajeIva);
		}
    	
    	montoPorcentajeCapturables();
    	
    	//Mostramos los primeros 4 valores, que ya sabemos como se calculan
    	int indice = 0;
    	// ------------------------------------------------------------------------ A2
		BigDecimal A2 = listaInsumos.get(indice).getMontoMateriales();
		
		//Materiales Explosión de Insumos (sin IVA) = insumosPresupuesto.totalMateriales.
    	indice = 1;
    	this.listaPresupuestoGrid.get(indice-1).setMonto(A2);
    	
    	//Costo de Material (sin IVA y sin Oculto) = insumosPresupuesto.totalMateriales.
    	indice = 2;
    	this.listaPresupuestoGrid.get(indice-1).setMonto(A2);

    	// ------------------------------------------------------------------------ A3
    	//Costo de Mano de Obra (sin IVA) = insumosPresupuesto.totalManoObra.
    	BigDecimal A3 = listaInsumos.get(0).getMontoManoDeObra();
    	indice = 3;
    	this.listaPresupuestoGrid.get(indice-1).setMonto(A3);

    	// ------------------------------------------------------------------------ A4
    	//Costo Herramienta y equipo (sin IVA) = insumosPresupuesto.totalHerramientas
    	BigDecimal A4 = listaInsumos.get(0).getMontoHerramientas();
    	indice = 4;
    	this.listaPresupuestoGrid.get(indice-1).setMonto(A4);
    	
    	// ------------------------------------------------------------------------ A5
    	//A5 --> A2 + A3 + A4
    	//montoTotalMateriales + costoManoObra + totalHerramientas;
    	indice = 5;
    	BigDecimal A5 = BigDecimal.ZERO;
    	A5 = A5.add(A2);
    	A5 = A5.add(A3);
    	A5 = A5.add(A4);
    	this.listaPresupuestoGrid.get(indice-1).setMonto(A5);
    	
    	// ------------------------------------------------------------------------ A6
    	//A6 --> A1 - A2
    	// OCULTO: Materiales Explosión de Insumos - Costo de Material --- por ahora sera 0 (cero)
    	indice = 6;
    	BigDecimal A6 = BigDecimal.ZERO;
    	/*A6 = A6.add(A2);
    	A6 = A6.add(A3);
    	A6 = A6.add(A4);*/
    	this.listaPresupuestoGrid.get(indice-1).setMonto(A6);
    	
    	calcularMontos();
    }

    public void montoPorcentajeCapturables() {
    	/* CAPTURABLES
    	 * -----------------------------------------------------------------
    	 * MONTOS 				: A14
    	 * PORCENTAJES 			: A8, A9, A10, A11, A12, A15
    	 * PORCENTAJES TOTALES  : A1, A7, A15, A16
    	 */

    	int index = -1;
    	for (PresupuestoDetalleExt presupuesto : this.listaPresupuestoGrid) {
    		index = this.listaPresupuestoGrid.indexOf(presupuesto);
    		
    		presupuesto.setMontoCapturable(false);
    		presupuesto.setPorcentajeCapturable(false);
    		presupuesto.setPorcentajeTotalesCapturable(true);
    		
    		if (index >= 0) {
            	if (index == 1 || index == 13)
            		presupuesto.setMontoCapturable(true);
            	
            	if (index == 7 || index == 8 || index == 9 || index == 10 || index == 11 || index == 14)
            		presupuesto.setPorcentajeCapturable(true);
            	
            	if (index == 0 || index == 6 || index == 14 || index == 15)
            		presupuesto.setPorcentajeTotalesCapturable(false);
    		}
    	}
    }

    private boolean validarCamposCalculo() {
    	//Verificar que se cuente con los campos requeridos: Oculto, Indirectos de Campo, Indirectos de Oficina Central, Cargos Adicionales,  Financiamiento, Utilidad
    	int indice = 0;
    	
    	control();
    	this.cargaInsumosValidada = false;	//Se ocupará esta variable para no consumir mas
		log.info("Validando campos");

    	indice = 5;
    	if (this.listaPresupuestoGrid.get(indice - 1).getMonto() == null || this.listaPresupuestoGrid.get(indice - 1).getMonto().equals(BigDecimal.ZERO) ) {
    		control(-1, "Indique cantidad para concepto: " + this.listaPresupuestoGrid.get(indice - 1).getConceptoPresupuesto().getConcepto());
    		return false;
    	}
    	
    	indice = 7;
    	if ( this.listaPresupuestoGrid.get(indice - 1).getMonto().equals(BigDecimal.ZERO) ) {
    		control(-1, "Indique cantidad para concepto: " + this.listaPresupuestoGrid.get(indice - 1).getConceptoPresupuesto().getConcepto());
    		return false;
    	}
    	
    	//8,9,10,11,12, 15
    	indice = 8;
    	if ( this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() > 0 && this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() == 0 ) {
    		control(-1, "Indique porcentaje para concepto: " + this.listaPresupuestoGrid.get(indice - 1).getConceptoPresupuesto().getConcepto());
    		return false;
    	}
    	
    	indice = 9;
    	if ( this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() > 0 && this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() == 0 ) {
    		control(-1, "Indique porcentaje para concepto: " + this.listaPresupuestoGrid.get(indice - 1).getConceptoPresupuesto().getConcepto());
    		return false;
    	}

    	indice = 10;
    	if ( this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() > 0 && this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() == 0 ) {
    		control(-1, "Indique porcentaje para concepto: " + this.listaPresupuestoGrid.get(indice - 1).getConceptoPresupuesto().getConcepto());
    		return false;
    	}

    	indice = 11;
    	if ( this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() > 0 && this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() == 0 ) {
    		control(-1, "Indique porcentaje para concepto: " + this.listaPresupuestoGrid.get(indice - 1).getConceptoPresupuesto().getConcepto());
    		return false;
    	}

    	indice = 12;
    	if ( this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() > 0 && this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() == 0 ) {
    		control(-1, "Indique porcentaje para concepto: " + this.listaPresupuestoGrid.get(indice - 1).getConceptoPresupuesto().getConcepto());
    		return false;
    	}
    	
    	indice = 14;
		if (this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() <= 0) {
    		control(-1, "Indique cantidad para concepto: " + this.listaPresupuestoGrid.get(indice - 1).getConceptoPresupuesto().getConcepto());
    		return false;
		}
    	
    	indice = 15;
    	if ( this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() > 0 && this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() == 0 ) {
    		control(-1, "Indique porcentaje para concepto: IVA");
    		return false;
    	}
    	
    	this.cargaInsumosValidada = true;	//Se ocupará esta variable para no consumir mas
    	control();
    	
    	return true;
    }
    
    public void calcularMontos() {
    	int indice = 0;
    	BigDecimal porcentaje = BigDecimal.ZERO;
    	BigDecimal A1  = BigDecimal.ZERO;
    	BigDecimal A2  = BigDecimal.ZERO;
    	BigDecimal A3  = BigDecimal.ZERO;
    	BigDecimal A4  = BigDecimal.ZERO;
    	BigDecimal A5  = BigDecimal.ZERO;
    	BigDecimal A6  = BigDecimal.ZERO;
    	BigDecimal A7  = BigDecimal.ZERO;
    	BigDecimal A8  = BigDecimal.ZERO;
    	BigDecimal A9  = BigDecimal.ZERO;
    	BigDecimal A10 = BigDecimal.ZERO;
    	BigDecimal A11 = BigDecimal.ZERO;
    	BigDecimal A12 = BigDecimal.ZERO;
    	BigDecimal A13 = BigDecimal.ZERO;
    	BigDecimal A14 = BigDecimal.ZERO;
    	BigDecimal A15 = BigDecimal.ZERO;
    	BigDecimal A16 = BigDecimal.ZERO;
    	
    	try{ 
    		if (this.isDebug) 
    			log.info("Calculando montos");

        	// ------------------------------------------------------------------------ A1
    		// MATERIALES EXPLOSION DE INSUMOS (CON OCULTO Y SIN IVA) --> insumosPresupuesto.totalMateriales
        	indice = 1;
    		A1 = this.listaPresupuestoGrid.get(indice - 1).getMonto();

        	// ------------------------------------------------------------------------ A2
    		// COSTO DE MATERIAL (SIN IVA Y SIN OCULTO) --> insumosPresupuesto.totalMateriales :: capturable
        	indice = 2;
        	A2 = this.listaPresupuestoGrid.get(indice - 1).getMonto();
        	if (this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() <= 0) {
        		A2 = listaInsumos.get(0).getMontoMateriales();
            	this.listaPresupuestoGrid.get(indice - 1).setMonto(A2);
        	}

        	// ------------------------------------------------------------------------ A3
        	// COSTO DE MANO DE OBRA (SIN IVA) --> insumosPresupuesto.totalManoObra
        	indice = 3;
        	A3 = this.listaPresupuestoGrid.get(indice - 1).getMonto();

        	// ------------------------------------------------------------------------ A4
        	// COSTO HERRAMIENTA Y EQUIPO (SIN IVA) --> insumosPresupuesto.totalHerramientas
        	indice = 4;
        	A4 = this.listaPresupuestoGrid.get(indice - 1).getMonto();

        	// ------------------------------------------------------------------------ A5
        	// SUBTOTAL COSTO DIRECTO 1 (SIN IVA Y SIN OCULTO) --> = A2 + A3 + A4
        	indice = 5;
        	A5 = this.listaPresupuestoGrid.get(indice - 1).getMonto();
        	if (this.listaPresupuestoGrid.get(indice - 1).getMonto() == null || this.listaPresupuestoGrid.get(indice - 1).getMonto().equals(BigDecimal.ZERO)) {
        		A5 = A5.add(A2);
        		A5 = A5.add(A3);
        		A5 = A5.add(A4);
        		this.listaPresupuestoGrid.get(indice - 1).setMonto(A5);
        	}
        	
        	// ------------------------------------------------------------------------ A6
        	// OCULTO --> = A1-A2
        	indice = 6;
        	A6 = this.listaPresupuestoGrid.get(indice - 1).getMonto();
        	if (this.listaPresupuestoGrid.get(indice - 1).getMonto() == null || this.listaPresupuestoGrid.get(indice - 1).getMonto().equals(BigDecimal.ZERO)) {
        		A6 = new BigDecimal(Math.abs(A1.subtract(A2).doubleValue()));
        		this.listaPresupuestoGrid.get(indice - 1).setMonto(A6);
        	}

        	// ------------------------------------------------------------------------ A7
        	// TOTAL COSTO DIRECTO 1 (SIN IVA) --> =A5+A6
        	indice = 7;
        	A7 = A7.add(A5);
        	A7 = A7.add(A6);
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A7);
        	
        	// ------------------------------------------------------------------------ A8
        	// INDIRECTOS DE CAMPO --> =A7 * %INDIRECTOS DE CAMPO
        	indice = 8;
        	if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() * 0.01);
        		A8 = A7.multiply(porcentaje);
        	}
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A8);
        	
        	// ------------------------------------------------------------------------ A9
        	// INDIRECTOS DE OFICINA CENTRAL --> =A7 * %INDIRECTOS DE OFICINA CENTRAL 
        	indice = 9;
        	if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() * 0.01);
        		A9 = A7.multiply(porcentaje);
        	}
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A9);
        	
        	//------------------------------------------------------------------------ A10
        	//CARGOS ADICIONALES --> =A5 * % CARGOS ADICIONALES
        	indice = 10;
        	if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() * 0.01);
            	A10 = A5.multiply(porcentaje);
        	}
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A10);
        	
        	//------------------------------------------------------------------------ A11
        	//A11 -->	financiamiento = (A5 + A8 + A9) * %
        	indice = 11;
        	if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() * 0.01);
            	A11 = A11.add(A5);
            	A11 = A11.add(A8);
            	A11 = A11.add(A9);
            	A11 = A11.multiply(porcentaje);
        	}
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A11);
			
			//------------------------------------------------------------------------ A14
			//A14 --> CAPTURABLE (subtotal2 = A6 + A13)
			indice = 14;
			A14 = this.listaPresupuestoGrid.get(indice - 1).getMonto();
			if (this.listaPresupuestoGrid.get(indice - 1).getMonto() == null) {
				A14 = BigDecimal.ZERO;
				this.listaPresupuestoGrid.get(indice - 1).setMonto(A14);
			}
        	
        	//------------------------------------------------------------------------ A12
        	// UTILIDAD --> =A14-(A7+A8+A9+A10+A11)
        	indice = 12;
        	if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() * 0.01);
            	A12 = A12.add( A7);
            	A12 = A12.add( A8);
            	A12 = A12.add( A9);
            	A12 = A12.add(A10);
            	A12 = A12.add(A11);
            	A12 = A14.subtract(A12);
        	}
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A12);
        	
        	//------------------------------------------------------------------------ A13
        	// MARGEN TOTAL --> =A6+A8+A9+A10+A11+A12
        	indice = 13;
        	A13 = A13.add( A6);
        	A13 = A13.add( A8);
        	A13 = A13.add( A9);
        	A13 = A13.add(A10);
        	A13 = A13.add(A11);
        	A13 = A13.add(A12);
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A13);
        	
        	//------------------------------------------------------------------------ A14 --> CAPTURABLE
        	
        	//------------------------------------------------------------------------ A15
        	//A15 --> iva 16% = A14  * %
        	indice = 15;
    		porcentaje = new BigDecimal(this.porcentajeIva * 0.01);
        	A15 = A14.multiply(porcentaje);
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A15);
        	
        	//------------------------------------------------------------------------ A16
        	//A16 --> total con iva = A14 + A15
        	indice = 16;
        	A16 = A16.add(A14);
        	A16 = A16.add(A15);
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A16);
        	
    		calcularPorcentajesRestantes(A14.doubleValue()); //(A6.doubleValue());
    	} catch(Exception e) {
    		log.error("Error obteniendo insumos", e);
    	}
    }
    
    public void recalcularMontos() {
    	if (! validarCamposCalculo()) 
    		return;
    	
    	calcularMontos();
    }
    
    public void calcularMontosOLD() {
    	if (! validarCamposCalculo()) 
    		return;
    	
    	//Una vez agregados los conceptos, actualizar montos
    	/*
    	1.	El sistema recupera los valores del monto correspondientes a los siguientes conceptos:
			a)	Materiales Explosión de Insumos (sin IVA) = insumosPresupuesto.totalMateriales.
			b)	Costo de Material (sin IVA y sin Oculto) = insumosPresupuesto.totalMateriales.
			c)	Costo de Mano de Obra (sin IVA) = insumosPresupuesto.totalManoObra.
			d)	Costo Herramienta (sin IVA) = insumosPresupuesto.totalHerramientas
    	*/
    	
    	int indice = 0;
    	List<InsumosExt> listaInsumos;
    	BigDecimal porcentaje = BigDecimal.ZERO;
    	BigDecimal A1  = BigDecimal.ZERO;
    	BigDecimal A2  = BigDecimal.ZERO;
    	BigDecimal A3  = BigDecimal.ZERO;
    	BigDecimal A4  = BigDecimal.ZERO;
    	BigDecimal A5  = BigDecimal.ZERO;
    	BigDecimal A6  = BigDecimal.ZERO;
    	BigDecimal A7  = BigDecimal.ZERO;
    	BigDecimal A8  = BigDecimal.ZERO;
    	BigDecimal A9  = BigDecimal.ZERO;
    	BigDecimal A10 = BigDecimal.ZERO;
    	BigDecimal A11 = BigDecimal.ZERO;
    	BigDecimal A12 = BigDecimal.ZERO;
    	BigDecimal A13 = BigDecimal.ZERO;
    	BigDecimal A14 = BigDecimal.ZERO;
    	BigDecimal A15 = BigDecimal.ZERO;
    	BigDecimal A16 = BigDecimal.ZERO;
    	
    	try{ // this.listaPresupuestoGrid = this.ifzPresupuestoDetalle.findByPropertyExt("idPresupuesto", this.pojoPresupuesto.getId(), 0);
    		listaInsumos = this.ifzInsumos.findExtByActivos(0);
    		if ( listaInsumos.isEmpty() ) return;
    		if (this.isDebug) 
    			log.info("Calculando insumos");

        	// ------------------------------------------------------------------------ A1
    		// MATERIALES EXPLOSION DE INSUMOS (CON OCULTO Y SIN IVA) --> insumosPresupuesto.totalMateriales
        	indice = 1;
    		A1 = listaInsumos.get(0).getMontoMateriales();
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A1);

        	// ------------------------------------------------------------------------ A2
    		// COSTO DE MATERIAL (SIN IVA Y SIN OCULTO) --> insumosPresupuesto.totalMateriales :: capturable
        	indice = 2;
        	A2 = this.listaPresupuestoGrid.get(indice - 1).getMonto();
        	if (this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() <= 0) {
        		A2 = listaInsumos.get(0).getMontoMateriales();
            	this.listaPresupuestoGrid.get(indice - 1).setMonto(A2);
        	}

        	// ------------------------------------------------------------------------ A3
        	// COSTO DE MANO DE OBRA (SIN IVA) --> insumosPresupuesto.totalManoObra
        	A3 = listaInsumos.get(0).getMontoManoDeObra();
        	indice = 3;
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A3);

        	// ------------------------------------------------------------------------ A4
        	// COSTO HERRAMIENTA Y EQUIPO (SIN IVA) --> insumosPresupuesto.totalHerramientas
        	indice = 4;
        	A4 = listaInsumos.get(0).getMontoHerramientas();
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A4);

        	// ------------------------------------------------------------------------ A5
        	// SUBTOTAL COSTO DIRECTO 1 (SIN IVA Y SIN OCULTO) --> =A2+A3+A4
        	indice = 5;
        	A5 = A5.add(A2);
        	A5 = A5.add(A3);
        	A5 = A5.add(A4);
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A5);
        	
        	// ------------------------------------------------------------------------ A6
        	// OCULTO --> =A1-A2
        	indice = 6;
        	A6 = A6.add(A1);
        	A6 = A6.subtract(A2);
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A6);

        	// ------------------------------------------------------------------------ A7
        	// TOTAL COSTO DIRECTO 1 (SIN IVA) --> =A5+A6
        	indice = 7;
        	A7 = A7.add(A5);
        	A7 = A7.add(A6);
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A7);
        	
        	// ------------------------------------------------------------------------ A8
        	// INDIRECTOS DE CAMPO --> =A7 * %INDIRECTOS DE CAMPO
        	indice = 8;
        	if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() * 0.01);
        		A8 = A7.multiply(porcentaje);
        	}
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A8);
        	
        	// ------------------------------------------------------------------------ A9
        	// INDIRECTOS DE OFICINA CENTRAL --> =A7 * %INDIRECTOS DE OFICINA CENTRAL 
        	indice = 9;
        	if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() * 0.01);
        		A9 = A7.multiply(porcentaje);
        	}
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A9);
        	
        	//------------------------------------------------------------------------ A10
        	//CARGOS ADICIONALES --> =A5 * % CARGOS ADICIONALES
        	indice = 10;
        	if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() * 0.01);
            	A10 = A5.multiply(porcentaje);
        	}
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A10);
        	
        	//------------------------------------------------------------------------ A11
        	//A11 -->	financiamiento = (A5 + A8 + A9) * %
        	indice = 11;
        	if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() * 0.01);
            	A11 = A11.add(A5);
            	A11 = A11.add(A8);
            	A11 = A11.add(A9);
            	A11 = A11.multiply(porcentaje);
        	}
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A11);
			
			//------------------------------------------------------------------------ A14
			//A14 --> CAPTURABLE (subtotal2 = A6 + A13)
			indice = 14;
			A14 = this.listaPresupuestoGrid.get(indice - 1).getMonto();
        	
        	//------------------------------------------------------------------------ A12
        	// UTILIDAD --> =A14-(A7+A8+A9+A10+A11)
        	indice = 12;
        	if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() > 0) {
        		porcentaje = new BigDecimal(this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() * 0.01);
            	A12 = A12.add( A7);
            	A12 = A12.add( A8);
            	A12 = A12.add( A9);
            	A12 = A12.add(A10);
            	A12 = A12.add(A11);
            	A12 = A14.subtract(A12);
        	}
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A12);
        	
        	//------------------------------------------------------------------------ A13
        	// MARGEN TOTAL --> =A6+A8+A9+A10+A11+A12
        	indice = 13;
        	A13 = A13.add( A6);
        	A13 = A13.add( A8);
        	A13 = A13.add( A9);
        	A13 = A13.add(A10);
        	A13 = A13.add(A11);
        	A13 = A13.add(A12);
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A13);
        	
        	//------------------------------------------------------------------------ A14 --> CAPTURABLE
        	
        	//------------------------------------------------------------------------ A15
        	//A15 --> iva 16% = A14  * %
        	indice = 15;
    		porcentaje = new BigDecimal(this.porcentajeIva * 0.01);
        	A15 = A14.multiply(porcentaje);
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A15);
        	
        	//------------------------------------------------------------------------ A16
        	//A16 --> total con iva = A14 + A15
        	indice = 16;
        	A16 = A16.add(A14);
        	A16 = A16.add(A15);
        	this.listaPresupuestoGrid.get(indice - 1).setMonto(A16);
        	
    		calcularPorcentajesRestantes(A14.doubleValue()); //(A6.doubleValue());
    	} catch(Exception e) {
    		log.error("Error obteniendo insumos", e);
    	}
    }

    private void calcularPorcentajesRestantes(Double subTotal) {
    	int indice;
    	double porcentaje;
    	double relacionPorcentual = 100;
    	
    	/*
    		Calcular todos los porcentajes que no son obligatorios
    			(Monto entre subtotal1) * 100
    	
    		Los porcentajes obligados son: 
    			8,9,10,11,12, 15
    		
    		Por tanto, los que se deben calcular son:
    			1,2,3,4,5,6,7,13 y 14
    	
    		Si es que no tienen porcentaje asignado
    	*/

    	for (int i = 1; i<= 16; i++) {
    		indice = i;
			// Porcentaje INSUMOS: A8, A9, A10, A11, A12 y A15 no se calculan
    		if (i != 8 && i != 9 && i != 10 && i != 11 && i != 12 && i != 15) {
    			if (this.listaPresupuestoGrid.get(indice - 1).getMonto().compareTo(BigDecimal.ZERO) > 0) {
    			//if (this.listaPresupuestoGrid.get(indice - 1).getPorcentaje() == 0 && this.listaPresupuestoGrid.get(indice - 1).getMonto().compareTo(BigDecimal.ZERO) > 0) {
            		porcentaje = (this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() / subTotal ) * relacionPorcentual;
            		this.listaPresupuestoGrid.get(indice - 1).setPorcentaje(porcentaje);
            	}
    		}

			// Porcentaje SUBTOTAL: A1, A7, A15 y A16 no se calculan
    		if (i != 1 && i != 7 && i != 15 && i != 16) {
    			if (this.listaPresupuestoGrid.get(indice - 1).getMonto().compareTo(BigDecimal.ZERO) > 0) {
            		porcentaje = (this.listaPresupuestoGrid.get(indice - 1).getMonto().doubleValue() / subTotal) * relacionPorcentual;
            		if (porcentaje > 100) porcentaje = 100;
            		if (porcentaje < 0) porcentaje = 0;
            		this.listaPresupuestoGrid.get(indice - 1).setPorcentajeTotales(porcentaje);
            	}
    		}
    	}
    }

    private boolean validaGuardarPresupuesto() {
    	return true;
    }

    private BigDecimal calcularMontoTotal() {
    	BigDecimal total = BigDecimal.ZERO;
    	
    	for (PresupuestoDetalleExt var : this.listaPresupuestoGrid) {
    		total = total.add(var.getMonto());
    	}
    	
    	return total;
    }
    
    public void guardarPresupuesto() {
    	control();
		if (! validaGuardarPresupuesto()) 
			return;
		
    	try {
			this.pojoPresupuesto.setMontoTotal(this.calcularMontoTotal());
    		this.pojoPresupuesto.setModificadoPor(this.usuarioId);
			this.pojoPresupuesto.setFechaModificacion(Calendar.getInstance().getTime());
			
    		if (this.pojoPresupuesto.getId() == null) {			//Guardar; se compara con cero porque es un objeto de tipo primitivo
    			this.pojoPresupuesto.setCreadoPor(this.usuarioId);
    			this.pojoPresupuesto.setFechaCreacion(Calendar.getInstance().getTime());
    			this.pojoPresupuesto.setIdObra(this.ifzObras.findById(this.pojoObra.getId()));
    			//this.pojoPresupuesto.setIdObra(this.pojoObra.getId());
    			
    			// Guardamos en la BD
    			this.pojoPresupuesto.setId( this.ifzPresupuesto.save(this.pojoPresupuesto));
    			
    			// Guardamos los detalles
    			for (PresupuestoDetalleExt var : this.listaPresupuestoGrid) {
    				var.setIdPresupuesto(this.pojoPresupuesto.getId());
        			var.setCreadoPor(this.usuarioId);
        			var.setFechaCreacion(Calendar.getInstance().getTime());
        			var.setModificadoPor(this.usuarioId);
        			var.setFechaModificacion(Calendar.getInstance().getTime());
        			var.setMonto(new BigDecimal(Math.round(var.getMonto().doubleValue() * 100) / 100)); // redondeamos a 2 digitos
        			var.setPorcentaje(Math.round(var.getPorcentaje() * 100) / 100);
        			var.setPorcentajeTotales(Math.round(var.getPorcentajeTotales() * 100) / 100);
        			
        			// Guardamos en la BD
        			this.ifzPresupuestoDetalle.save(var);
        		}
    		} else {
    			// Actualizamos en la BD
    			this.ifzPresupuesto.update(this.pojoPresupuesto);
    			
    			// Actualizamos los detalles
    			for (PresupuestoDetalleExt var : this.listaPresupuestoGrid) {
    				var.setModificadoPor(this.usuarioId);
        			var.setFechaModificacion(Calendar.getInstance().getTime());
        			var.setMonto(new BigDecimal(Math.round(var.getMonto().doubleValue() * 100) / 100)); // redondeamos a 2 digitos
        			var.setPorcentaje(Math.round(var.getPorcentaje() * 100) / 100);
        			var.setPorcentajeTotales(Math.round(var.getPorcentajeTotales() * 100) / 100);
        			
        			// Actualizamos en la BD
        			this.ifzPresupuestoDetalle.update(var);
        		}
    		}
    	} catch(Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar Guardar el PPT-01", e);
    	}
    }

	public void reporte() {
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		String nombreDocumento = "";
		
		try {
			control();
			if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L) {
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idObra", this.pojoObra.getId());

				// Parametros para la ejecucion del reporte
				params = new HashMap<String, String>();
				params.put("idPrograma", 	  this.entornoProperties.getString("REPORTE_PPT01"));
				//params.put("nombreDocumento", this.entornoProperties.getString("REPORTE_PPT01_NOMBRE"));
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);

				Respuesta respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if (respuesta.getErrores().getCodigoError() == 0L) {
					nombreDocumento = "PPT01-O-" + this.pojoObra.getId() + "." + respuesta.getBody().getValor("formatoReporte");
					this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
					this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte")); 	
					this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
				} else {
					log.info(respuesta.getErrores().getDescError());
					control(-1, respuesta.getErrores().getDescError());
				}
			}
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar Ejecutar el reporte PPT-01", e);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// OBRA EMPLEADOS
	// -------------------------------------------------------------------------------------------------------------------
    
    public void cargaObrasEmpleados() {
    	try {
    		control();
			this.listObraEmpleadosGrid = this.ifzObraEmpleados.findByPropertyExt("idObra", this.pojoObra.getId(), 80);
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar consultar los Almacenes asignados a la Obra", e);
    	}
    }

    public void buscarEmpleados() throws Exception {
    	try {
    		control();
			if ("".equals(this.campoBusquedaEmpleados))
				this.campoBusquedaEmpleados = "id";
			
			this.listEmpleadosGrid = this.ifzEmpleados.findExtLikeProperty(this.campoBusquedaEmpleados, this.valorBusquedaEmpleados, 0);
			if (this.listEmpleadosGrid == null || this.listEmpleadosGrid.isEmpty()) {
	    		control(2, "Busqueda sin resultados");
	    		return;
			}

			log.info(this.listEmpleadosGrid.size() + " empleados encontrados");
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar consultar los Empleados", e);
    	}
    }

    public void eliminarObraEmpleado() {
    	try {
    		control();
			if (this.pojoObraEmpleado != null && this.pojoObraEmpleado.getId() > 0L) {
				// Eliminamos de la BD
				this.ifzObraEmpleados.delete(this.pojoObraEmpleado.getId());
				
				// Eliminamos de la lista
				this.listObraEmpleadosGrid.remove(this.pojoObraEmpleado);
			}
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar Eliminar el Empleado de la Obra", e);
    	}
    }

    public void seleccionarEmpleado() throws ExcepConstraint {
    	boolean existe = false;
    	
    	try {
    		control();
        	if (this.listObraEmpleadosGrid == null) 
        		this.listObraEmpleadosGrid = new ArrayList<ObraEmpleadoExt>();
        	
        	for (ObraEmpleadoExt var : this.listObraEmpleadosGrid) {
        		if (var.getEmpleadoId().longValue() == this.pojoEmpleado.getId().longValue()) {
        			existe = true;
        			break;
        		}
        	}
        	
        	if (! existe) {
        		this.pojoObraEmpleado = new ObraEmpleadoExt();
        		this.pojoObraEmpleado.setCreadoPor(this.usuarioId);
        		this.pojoObraEmpleado.setFechaCreacion(Calendar.getInstance().getTime());
        		this.pojoObraEmpleado.setModificadoPor(this.usuarioId);
        		this.pojoObraEmpleado.setFechaModificacion(Calendar.getInstance().getTime());

        		this.pojoObraEmpleado.setIdEmpleado(this.pojoEmpleado);
        		this.pojoObraEmpleado.setIdObra(this.pojoObra);
        		
        		// Guardamos en la BD
        		this.pojoObraEmpleado.setId(this.ifzObraEmpleados.save(this.pojoObraEmpleado));
        		
        		// Agregamos a la lista
        		this.listObraEmpleadosGrid.add(this.pojoObraEmpleado);
        	} else {
        		control(-1, "Este empleado ya existe en " + (this.pojoObra.getTipoObra() == 1 ? "la obra" : "el proyecto"));
        		log.info(this.mensaje);
        	}
        	
        	this.pojoEmpleado = null;
        	this.pojoObraEmpleado = null;
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar recuperar el Empleado seleccionado", e);
    	}
    }

	// -------------------------------------------------------------------------------------------------------------------
	// OBRA ALMACENES 
	// -------------------------------------------------------------------------------------------------------------------

    public void cargaAlmacenesObra() {
    	try {
			control();
			this.listObraAlmacenes = this.ifzObraAlmacenes.findByProperty("idObra", this.pojoObra.getId(), 120);
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar consultar los Almacenes asignados a la Obra", e);
    	}
    }

    public void nuevoObraAlmacen() {
    	try {
    		control();
	    	this.pojoObraAlmacen = new ObraAlmacenes();
	    	this.pojoObraAlmacenBorrar = null;
	    	this.almacenId = 0;
	    	cargaAlmacenes();
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar generar un nuevo Almacen a la Obra", e);
    	}
    }

    public void editarObraAlmacen() {
    	try {
    		control();
			this.almacenId = this.pojoObraAlmacen.getIdAlmacen();
	    	cargaAlmacenes();
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar recuperar el Almacen seleccionado", e);
    	}
    }

    public void guardarObraAlmacen() throws Exception {
    	try {
    		control();
        	if (this.listObraAlmacenes == null) 
        		this.listObraAlmacenes = new ArrayList<ObraAlmacenes>();
        	
        	if (! comprobarAlmacenObra())
        		return;

        	if (this.pojoObraAlmacen == null)
        		this.pojoObraAlmacen = new ObraAlmacenes();
        	
    		this.pojoObraAlmacen.setIdObra(this.ifzObras.convertir(this.pojoObra));
    		this.pojoObraAlmacen.setNombreObra(this.pojoObra.getNombre());
    		this.pojoObraAlmacen.setIdAlmacen(this.pojoAlmacen.getId());
    		this.pojoObraAlmacen.setNombreAlmacen(this.pojoAlmacen.getNombre());
    		this.pojoObraAlmacen.setModificadoPor(this.usuarioId);
    		this.pojoObraAlmacen.setFechaModificacion(Calendar.getInstance().getTime());
    		
        	if (this.pojoObraAlmacen.getId() == null || this.pojoObraAlmacen.getId() <= 0L) {
        		// Comprobamos que el almacen asignado no exista en la Obra/Proyecto.
	        	for (ObraAlmacenes var : this.listObraAlmacenes) {
	        		if (var.getIdAlmacen().longValue() == this.pojoAlmacen.getId().longValue()) {
	        			control(10, "El almacen asignado ya existe en la Obra");
		        		return;
	        		}
	        	}
	    		
	    		// Comprobamos que la obra solo tenga un Almacen principal, si corresponde
	        	if (this.pojoObraAlmacen.getTipo() == 2) {
		    		for (ObraAlmacenes var : this.listObraAlmacenes) {
		        		if (var.getTipo() == 2) {
			    			control(12, "La obra ya tiene asignado un almacen principal");
			        		return;
		        		}
		        	}
	        	}
	        	
	        	// Guardamos 
	        	this.pojoObraAlmacen.setCreadoPor(this.usuarioId);
        		this.pojoObraAlmacen.setFechaCreacion(Calendar.getInstance().getTime());
        		this.pojoObraAlmacen.setId(this.ifzObraAlmacenes.save(this.pojoObraAlmacen));
        		this.listObraAlmacenes.add(this.pojoObraAlmacen);
        	} else {
	    		// Comprobamos que la obra solo tenga un Almacen principal, si corresponde
        		if (this.pojoObraAlmacen.getTipo() == 2) {
    	    		for (ObraAlmacenes var : this.listObraAlmacenes) {
		        		if (var.getTipo() == 2 && this.pojoObraAlmacen.getId() != var.getId()) {
			    			control(12, "La obra ya tiene asignado un almacen principal");
			        		return;
		        		}
    	        	}
        		}
	    		
        		// Guardamos
        		this.ifzObraAlmacenes.update(this.pojoObraAlmacen);
        		for (ObraAlmacenes var : this.listObraAlmacenes) {
	        		if (var.getId().longValue() == this.pojoObraAlmacen.getId().longValue()) {
	            		var.setIdAlmacen(this.pojoObraAlmacen.getIdAlmacen());
	            		var.setNombreAlmacen(this.pojoObraAlmacen.getNombreAlmacen());
		        		var.setTipo(this.pojoObraAlmacen.getTipo());
		        		var.setModificadoPor(this.usuarioId);
		        		var.setFechaModificacion(Calendar.getInstance().getTime());
	        			break;
	        		}
	        	}
        	}
        	
        	nuevoObraAlmacen();
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar Guardar el Almacen en la Obra", e);
    	}
    }
    
    public void eliminarObraAlmacen() throws Exception {
    	try {
    		control();
	    	if (this.pojoObraAlmacenBorrar != null) {
	    		// Borramos de la BD si corresponde
	    		if (this.pojoObraAlmacenBorrar.getId() != null && this.pojoObraAlmacenBorrar.getId() > 0L)
	    			this.ifzObraAlmacenes.delete(this.pojoObraAlmacenBorrar.getId());

	    		// Borramos de la lista
	    		this.listObraAlmacenes.remove(this.pojoObraAlmacenBorrar);
	    	}
	    	
	    	nuevoObraAlmacen();
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar Eliminar el Almacen de la Obra", e);
    	}
    }

    public boolean comprobarAlmacenObra() {
    	try {
    		control();
	    	if (this.pojoObraAlmacen != null && this.pojoObraAlmacen.getTipo() == 1 && this.almacenId > 0L) {
	    		for (Almacen var : this.listAlmacenes) {
	    			if (var.getId() != this.almacenId)
	    				continue;
    				if (var.getTipo() == 1) {
    					control(4, "La obra ya tiene asignado un almacen general");
	    				return false;
    				}
    				
					this.pojoAlmacen = var;
					break;
	    		}
	    	}
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar comprobar el Almacen en la Obra", e);
    		return false;
    	}
    	
    	return true;
    }
    
    public boolean cargaAlmacenes() {
    	try {
    		control();
			this.listAlmacenes = this.ifzAlmacenes.findByProperty("nombre", "");
			if (this.listAlmacenes.isEmpty()) {
				control(-1, "No se encontro ningun almacen para Obras/Proyectos.");
				log.info(this.mensaje);
				return false;
			}

			// almacenes
			this.listAlmacenesItems.clear();
			for (Almacen var : this.listAlmacenes) {
				this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getNombre()));
			}
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar consultar los Almacenes", e);
			return false;
    	}
    	
		return true;
    }

	// -------------------------------------------------------------------------------------------------------------------
	// OBRA SATICS 
	// -------------------------------------------------------------------------------------------------------------------
    
    public boolean cargaObraContratos() {
    	try {
			control();
			if (this.listObraContratosItems == null) this.listObraContratosItems = new ArrayList<SelectItem>();
			this.listObraContratosItems.clear();
			
			List<ObraContratos> lista = this.ifzContratos.findByProperty("idObra.id", this.pojoObra.getId(), 0);
			if (lista == null || lista.isEmpty()) {
				control(2, "No se encontro ningun contrato.");
				return false;
			}

			for (ObraContratos var : lista)
				this.listObraContratosItems.add(new SelectItem(var.getId(), var.getNombreSubcontratante()));
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar consultar los Contratos de la Obra", e);
    		return false;
    	}
    	
    	return true;
    }
    
	public void cargarSatics() {
		try {
    		control();
    		if (this.listSaticsItems == null) this.listSaticsItems = new ArrayList<SelectItem>();
			this.listSaticsItems.clear();
			
			this.listSatics = this.ifzConValores.findAll(this.pojoGpoSatics);
			if (this.listSatics != null && ! this.listSatics.isEmpty()) {
				for (ConValores var : this.listSatics) {
					this.listSaticsItems.add(new SelectItem(var.getId(), var.getValor()));
				}
			}
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar consultar las Satics de la Obra", e);
		}
	}
	
	public void buscarObraSatics() {
		try {
    		control();
			if (this.listObraSatics == null)
				this.listObraSatics = new ArrayList<ObraSatics>();
			this.listObraSatics.clear();
			
			// cargamos los satic
			cargarSatics();
			
			this.ifzObraSatics.orderBy("fechaCreacion DESC");
			this.listObraSatics = this.ifzObraSatics.findLikeProperty("idObra", this.pojoObra.getId(), 120);
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar consultar las Satics", e);
		}
	}
	
	public void nuevoObraSatic() {
		try {
    		control();
			this.pojoSatics = new ObraSatics();
			this.pojoSaticsBorrar = null;
			this.saticsId = 0L;
			this.saticsAdendumId = 0L;

			nuevoUploadFile();
			cargaObraContratos();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar generar una nueva Satic para la Obra", e);
		}
	}
	
	public void editarObraSatics() {
		try {
    		control();
			cargaObraContratos();
			
			if (this.pojoSatics != null) {
				this.saticsId = this.pojoSatics.getIdSatics();
				if (this.pojoSatics.getIdSaticsAdendum() != null)
					this.saticsAdendumId = this.pojoSatics.getIdSaticsAdendum();
			}

			nuevoUploadFile();
		} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar Editar la Satic seleccionada", e);
		}
	}
	
	public void borrarObraSatics() {
		try {
    		control();
			if (this.pojoSaticsBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoSaticsBorrar.getId() != null && this.pojoSaticsBorrar.getId() > 0L)
					this.ifzObraSatics.delete(this.pojoSaticsBorrar.getId());
				
				// Borramos del listado
				this.listObraSatics.remove(this.pojoSaticsBorrar);
			}
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar Eliminar la Satic de la Obra", e);
		}
	}
	
	public void salvarSatics() {
		try {
    		control();
			if (this.pojoSatics != null) {
				// Recuperamos SATIC si corresponde
				if (this.saticsId > 0L) {
					for (ConValores var : this.listSatics) {
						if (this.saticsId == var.getId()) {
							this.pojoSatics.setIdSatics(var.getId());
							this.pojoSatics.setNombreSatics(var.getValor());
							break;
						}
					}
				}

				// Recuperamos SATIC ADENDUM si corresponde
				this.pojoSatics.setIdSaticsAdendum(0L);
				this.pojoSatics.setNombreSaticsAdendum("");
				if (this.saticsAdendumId > 0L) {
					for (ConValores var : this.listSatics) {
						if (this.saticsAdendumId == var.getId()) {
							this.pojoSatics.setIdSaticsAdendum(var.getId());
							this.pojoSatics.setNombreSaticsAdendum(var.getValor());
							break;
						}
					}
				}
				
				this.pojoSatics.setIdObra(this.ifzObras.findById(this.pojoObra.getId()));
				this.pojoSatics.setNombreObra(this.pojoObra.getNombre());
				this.pojoSatics.setModificadoPor(this.usuarioId);
				this.pojoSatics.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoSatics.getId() == null || this.pojoSatics.getId() <= 0L) {
					this.pojoSatics.setCreadoPor(this.usuarioId);
					this.pojoSatics.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoSatics.setId(this.ifzObraSatics.save(this.pojoSatics));
					
					// Agregamos al listado
					this.listObraSatics.add(this.pojoSatics);
				} else {
					// Actualizamos en la BD
					this.ifzObraSatics.update(this.pojoSatics);
				}
			}
			
			nuevoObraSatic();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar Guardar la Satic en la Obra", e);
		}
	}
	
	public void uploadSatics() {}
	
	public boolean getPermitirUploadSatic() {
		if (this.pojoSatics != null && this.pojoSatics.getId() != null && this.pojoSatics.getId() > 0L)
			return true;
		return false;
	}
	
	public void setPermitirUploadSatic(boolean value) {}
	
	public boolean getObraSaticsTipo() {
		if (this.pojoSatics != null && this.pojoSatics.getTipo() > 1)
			return true;
		return false;
	}
	
	public void setObraSaticsTipo(boolean value) {
		if (this.pojoSatics != null)
			this.pojoSatics.setTipo((value) ? 2 : 1);
	}
	
	public void nuevoUploadFile() {
		this.fileSrc = null;
		this.fileName = "";
		this.fileExtension = "";
	}
	
	public void uploadListener(FileUploadEvent event) throws Exception {
		try {
			control();
			this.fileSrc = event.getUploadedFile().getData();
			this.fileName = event.getUploadedFile().getName();
			this.fileExtension = Files.getFileExtension(event.getUploadedFile().getName());
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar recuperar el Archivo cargado", e);
		}
	}
	
	public void analizarArchivo() throws Exception {
		try {
			control();
			if (this.fileSrc == null || this.fileSrc.length <= 0) {
				nuevoUploadFile();
				return;
			}
			
			if (this.fileExtension == null || "".equals(this.fileExtension))
				this.fileExtension = "pdf";
			
			// Establecemos el nombre del archivo
			this.fileName = this.pojoSatics.getId().toString() + "." + this.fileExtension;
			if (this.fileName == null || "".equals(this.fileName))
				this.fileName = this.ftpDigitalizacionRuta + this.pojoSatics.getId().toString() + "." + this.fileExtension;
			
			// Subimos el archivo al servidor
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			if (! this.ifzFtp.putArchivo(this.fileSrc, this.ftpDigitalizacionRuta + this.fileName)) {
				control(5, "No se pudo procesar el archivo cargado");
				return;
			}
			
			// Indicamos que el archivo ha sido cargado en el servidor.
			this.pojoSatics.setPdfCargado(1);
			this.ifzObraSatics.update(this.pojoSatics);
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar procesar el Archivo cargado", e);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// OBRA COBRANZA 
	// -------------------------------------------------------------------------------------------------------------------
	
	public void cargarCobranza() {
		double monto = 0;
		FacturaDetalleExt pojoDetalle;
		ObraCobranza itemCobranza;
		ObraCobranza itemAux;
		
		try {
    		control();
			if (this.listObraCobranza == null)
				this.listObraCobranza = new ArrayList<ObraCobranza>();
			this.listObraCobranza.clear();
			
			// recuperamos las facturas actuales de la obra
			List<Factura> listFacturas = this.ifzFacturas.findByProperty("idObra", this.pojoObra.getId(), 120);
			if (listFacturas == null || listFacturas.isEmpty())
				return;
			
			// Recuperamos el pojo OBRA
			Obra pojoObra = this.ifzObras.findById(this.pojoObra.getId());
			
			// Generamos el listado de cobranza
			for (Factura var : listFacturas) {
				if (var.getEstatus() == 0)
					continue;
				
				// Recuperamos los detalles de la factura
				List<FacturaDetalleExt> lista = this.ifzFacConceptos.findByPropertyPojoCompletoExt("idFactura", var.getId());
				if (lista == null || lista.isEmpty())
					continue;
				
				// Recuperamos el primer concepto de la factura
				pojoDetalle = lista.get(0);
				
				// Recuperamos la cobranza si corresponde
				itemAux = this.ifzCobranza.comprobarConcepto(this.pojoObra.getId(), var.getId(), pojoDetalle.getId());
				if (itemAux != null) {
					itemCobranza = itemAux;
				} else {
					monto = var.getSubtotal().doubleValue() + var.getImpuestos();
					
					itemCobranza = new ObraCobranza();
					itemCobranza.setIdObra(pojoObra);
					itemCobranza.setNombreObra(pojoObra.getNombre());
					itemCobranza.setIdFactura(var.getId());
					itemCobranza.setFolio(var.getFolioFactura());
					itemCobranza.setFecha(var.getFechaEmision());
					itemCobranza.setIdConcepto(pojoDetalle.getId());
					itemCobranza.setConcepto(pojoDetalle.getIdConcepto().getDescripcion());
					itemCobranza.setMonto(new BigDecimal(monto));
				}
				
				// Agregamos al listado
				this.listObraCobranza.add(itemCobranza);
			}
			
			totalizarCobranza();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar cargar la Cobranza de la Obra", e);
		}
	}
	
	public void totalizarCobranza() {
		double monto = 0;
		double porcentaje = 0;
		
		if (this.listObraCobranza == null)
			this.listObraCobranza = new ArrayList<ObraCobranza>();
		
		this.totalCobranza = 0;
		this.permiteGuardarCobranza = true;
		for (ObraCobranza var : this.listObraCobranza) {
			// Calculamos Amortizacion
			monto = var.getEstimacion().doubleValue();
			porcentaje = var.getPorcentajeAnticipo().doubleValue();
			monto = ((monto * porcentaje) / 100);
			var.setAmortizacion(new BigDecimal(monto));
			
			// Calculamos Fondo Garantia
			monto = var.getEstimacion().doubleValue();
			porcentaje = var.getPorcentajeRetencion().doubleValue();
			monto = ((monto * porcentaje) / 100);
			var.setFondoGarantia(new BigDecimal(monto));
			
			if (var.getAnticipo().doubleValue() > 0) {
				monto = var.getAnticipo().doubleValue();
			} else {
				monto = var.getEstimacion().doubleValue() - (var.getAmortizacion().doubleValue() + var.getFondoGarantia().doubleValue());
			}
			
			// Caculamos el IVA del subtotal (monto)
			porcentaje = ((monto * this.porcentajeIva) / 100);

			var.setSubtotal(new BigDecimal(monto));
			var.setIva(new BigDecimal(porcentaje));

			// Calculamos el total redondeado a 2 decimales
			monto = monto + porcentaje;
			monto = Math.round(monto * 100) / 100;
			var.setTotal(new BigDecimal(monto));
			
			this.totalCobranza += monto;
			if (var.getMonto().doubleValue() != monto) {
				this.permiteGuardarCobranza = false;
			}
		}
	}
	
	public void salvarCobranza() {
		try {
    		control();
			if (this.listObraCobranza.size() <= 0)
				return;
			
			if (this.totalCobranza <= 0)
				totalizarCobranza();
			
			if (!this.permiteGuardarCobranza) {
				control(6, "Verifique, la columna Monto debe coincidir con la columna Total");
				return;
			}
			
			for (ObraCobranza var : this.listObraCobranza) {
				var.setIdObra(this.ifzObras.findById(this.pojoObra.getId()));
				var.setNombreObra(this.pojoObra.getNombre());
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					var.setId(this.ifzCobranza.save(var));
				} else {
					// Actualizamos en la BD
					this.ifzCobranza.update(var);
				}
			}
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar Guardar la Cobranza", e);
		}
	}
	
	public double getPorcentajeAnticipo() {
		if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())
			return this.listObraCobranza.get(0).getPorcentajeAnticipo().doubleValue();
		return 0;
	}
	
	public void setPorcentajeAnticipo(double value) {
		if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())  {
			for (ObraCobranza var : this.listObraCobranza) {
				var.setPorcentajeAnticipo(new BigDecimal(value));
			}
			totalizarCobranza();
		}
	}
	
	public double getPorcentajeRetencion() {
		if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())
			return this.listObraCobranza.get(0).getPorcentajeRetencion().doubleValue();
		return 0;
	}
	
	public void setPorcentajeRetencion(double value) {
		if (this.listObraCobranza != null && ! this.listObraCobranza.isEmpty())  {
			for (ObraCobranza var : this.listObraCobranza) {
				var.setPorcentajeRetencion(new BigDecimal(value));
			}
			totalizarCobranza();
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// OBRA AVANCE 
	// -------------------------------------------------------------------------------------------------------------------
	
	public void cargarObraAvances() {
		try {
    		control();
			if (this.listObraAvances == null)
				this.listObraAvances = new ArrayList<ObraAvance>();
			this.listObraAvances.clear();
			
			this.ifzObraAvance.orderBy("fecha DESC");
			this.listObraAvances = this.ifzObraAvance.findByProperty("idObra.id", this.pojoObra.getId(), 0);
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar cargar los Avances de la Obra", e);
		}
	}
	
	public void nuevoObraAvance() {
		try {
    		control();
			this.pojoObraAvance = new ObraAvance();
			this.pojoObraAvanceBorrar = null;
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar generar un nuevo Avance para la Obra", e);
		}
	}
	
	public void agregarObraAvance() {
		try {
    		control();
			if (this.pojoObraAvance != null) {
				if (this.pojoObraAvance.getPorcentaje() <= 0 || this.pojoObraAvance.getPorcentaje() > 100) {
					control(7, "El valor del porcentaje debe ser mayor a 0 y menor a 100");
					return;
				}
				
				// Inicializamos el listado si hace falta
				if (this.listObraAvances == null)
					this.listObraAvances = new ArrayList<ObraAvance>();
				
				// Comprobamos el porcentaje total actual mas el nuevo porcentaje
				double porcentajeTotal = 0;
				for (ObraAvance var : this.listObraAvances) {
					porcentajeTotal += var.getPorcentaje();
				}
				if ((porcentajeTotal + this.pojoObraAvance.getPorcentaje()) > 100) {
					control(14, "El porcentaje total supera el 100 %.");
					return;
				}

				// Asignamos la Obra y lo agregamos a la lista
				this.pojoObraAvance.setIdObra(this.ifzObras.findById(this.pojoObra.getId()));
				if (this.listObraAvances.isEmpty())
					this.listObraAvances.add(this.pojoObraAvance);
				else
					this.listObraAvances.add(0, this.pojoObraAvance);
			}
			
			nuevoObraAvance();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar agregar un nuevo Avance de Obra", e);
		}
	}
	
	public void borrarObraAvance() {
		try {
			control();
			if (this.pojoObraAvanceBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoObraAvanceBorrar.getId() != null && this.pojoObraAvanceBorrar.getId() > 0L)
					this.ifzObraAvance.delete(this.pojoObraAvanceBorrar.getId());
				
				// Borramos de la lista
				this.listObraAvances.remove(this.pojoObraAvanceBorrar);
			}
			
			nuevoObraAvance();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar Eliminar el Avance seleccionado", e);
		}
	}
	
	public void salvarObraAvances() {
		try {
			control();
			// Borramos los avances que corrrespondan
			if (this.listObraAvancesEliminados != null && ! this.listObraAvancesEliminados.isEmpty()) {
				for (ObraAvance var : this.listObraAvancesEliminados) {
					if (var.getId() != null && var.getId() > 0L)
						this.ifzObraAvance.delete(var.getId());
				}
				
				this.listObraAvancesEliminados.clear();
			}
			
			if (this.listObraAvances != null && ! this.listObraAvances.isEmpty()) {
				for (ObraAvance var : this.listObraAvances) {
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Guardamos o actualizamos en la BD segun corresponda
					if (var.getId() == null || var.getId() <= 0L) {
						var.setCreadoPor(this.usuarioId);
						var.setFechaCreacion(Calendar.getInstance().getTime());
						
						var.setId(this.ifzObraAvance.save(var));
					} else {
						this.ifzObraAvance.update(var);
					}
				}
			}
			
			nuevoObraAvance();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar Guarda el Avance de la Obra", e);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
	// OBRA CONTRATOS 
	// -------------------------------------------------------------------------------------------------------------------
	
	public boolean cargaSubcontratantes() {
    	try {
			control();
			this.subcontratanteId = 0L;
			if (this.listSubcontratantesItems == null) this.listSubcontratantesItems = new ArrayList<SelectItem>();
			this.listSubcontratantesItems.clear();
			
			this.listSubcontratantes = this.ifzConValores.findAll(this.pojoGpoSubcontratantes);
			if (this.listSubcontratantes == null || this.listSubcontratantes.isEmpty()) {
				control(2, "No se encontro ningun Subcontratante.");
				return false;
			}
			
			for (ConValores var : this.listSubcontratantes)
				this.listSubcontratantesItems.add(new SelectItem(var.getId(), var.getValor()));
    	} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al intentar cargar los Subcontratantes", e);
    		return false;
    	}
    	
    	return true;
    }
	
	public void cargarObraContratos() {
		try {
			control();
			if (this.listObraContratos == null)
				this.listObraContratos = new ArrayList<ObraContratos>();
			this.listObraContratos.clear();
			
			this.ifzContratos.orderBy("nombreSubcontratante");
			this.listObraContratos = this.ifzContratos.findByProperty("idObra.id", this.pojoObra.getId(), 0);
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar los Contratos de la Obra", e);
		}
	}
	
	public void nuevoObraContrato() {
		try {
			control();
			this.pojoObraContrato = new ObraContratos();
			this.pojoObraContratoBorrar = null;
			
			cargaSubcontratantes();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar generar un nuevo Contrato para la Obra", e);
		}
	}
	
	public void editarObraContrato() {
		try {
			control();
			this.pojoObraContratoBorrar = null;
			cargaSubcontratantes();
			this.subcontratanteId = this.pojoObraContrato.getIdSubcontratante();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar recuperar el Contrato seleccionado", e);
		}
	}
	
	public void salvarObraContrato() {
		try {
			control();
			if (this.pojoObraContrato != null) {
				if (this.pojoObraContrato.getMonto().doubleValue() <= 0) {
					control(8, "Monto menor o igual a cero");
					return;
				}
				
				if (this.pojoObraContrato.getIva().doubleValue() <= 0) {
					control(9, "IVA menor o igual a cero");
					return;
				}

				// Empresa subcontratante
				this.pojoObraContrato.setIdSubcontratante(this.subcontratanteId);
				for (ConValores var : this.listSubcontratantes) {
					if (this.subcontratanteId == var.getId()) {
						this.pojoObraContrato.setNombreSubcontratante(var.getValor());
						break;
					}
				}
				
				this.pojoObraContrato.setIdObra(this.ifzObras.findById(this.pojoObra.getId()));
				this.pojoObraContrato.setModificadoPor(this.usuarioId);
				this.pojoObraContrato.setFechaModificacion(Calendar.getInstance().getTime());
					
				// Guardamos o actualizamos en la BD segun corresponda
				if (this.pojoObraContrato.getId() == null || this.pojoObraContrato.getId() <= 0L) {
					this.pojoObraContrato.setCreadoPor(this.usuarioId);
					this.pojoObraContrato.setFechaCreacion(Calendar.getInstance().getTime());
					
					this.pojoObraContrato.setId(this.ifzContratos.save(this.pojoObraContrato));
				} else {
					this.ifzContratos.update(this.pojoObraContrato);
				}
				
				// Recargamos el listado de contratos
				cargarObraContratos();
			}
			
			// Reiniciamos los pojos
			nuevoObraContrato();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar Guardar el Contrato en la Obra", e);
		}
	}
	
	public void borrarObraContrato() {
		try {
			control();
			if (this.pojoObraContratoBorrar != null) {
				// Borramos de la BD si corresponde
				if (this.pojoObraContratoBorrar.getId() != null && this.pojoObraContratoBorrar.getId() > 0L)
					this.ifzContratos.delete(this.pojoObraContratoBorrar.getId());
				
				// Borramos de la lista
				this.listObraContratos.remove(this.pojoObraContratoBorrar);
			}
			
			nuevoObraContrato();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar borrar el Contrato", e);
		}
	}

	// -------------------------------------------------------------------------------------------------------------------
    // Propiedades de acceso
	// -------------------------------------------------------------------------------------------------------------------

	public boolean getConfirmarSatic() {
		if (this.pojoObra != null && this.pojoObra.getSatic02() != null && "false".equals(this.pojoObra.getSatic02()))
			return true;
		return false;
	}
	
	public void setConfirmarSatic(boolean value) {}
	
	public boolean getBand() {
		return operacionCancelada;
	}
	
	public void setBand(boolean band) {
		this.operacionCancelada = band;
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

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}
	
	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}
				
	public int getNumPagina() {
		return numPagina;
	}
				
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
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
		
	public int getTipoObraBusqueda() {
		return tipoObraBusqueda;
	}
	
	public void setTipoObraBusqueda(int tipoObraBusqueda) {
		this.tipoObraBusqueda = tipoObraBusqueda;
	}
		
	public List<Obra> getListObrasGrid() {
		return listObrasGrid;
	}
		
	public void setListObrasGrid(List<Obra> listObrasGrid) {
		this.listObrasGrid = listObrasGrid;
	}
		
	public Obra getPojoObraMain() {
		return this.pojoObraMain;
	}
		
	public void setPojoObraMain(Obra pojoObra) {
		this.pojoObraMain = pojoObra;
	}
	
	public ObraExt getPojoObra() {
		if (this.pojoObra == null)
			this.pojoObra = new ObraExt();
		return this.pojoObra;
	}
		
	public void setPojoObra(ObraExt pojoObra) {
		this.pojoObra = pojoObra;
	}
	
	public List<SelectItem> getListEstatusObrasItems() {
		return listEstatusObrasItems;
	}
			
	public void setListEstatusObrasItems(List<SelectItem> listEstatusObrasItems) {
		this.listEstatusObrasItems = listEstatusObrasItems;
	}
	
	public List<ConValores> getListEstatusObras() {
		return listEstatusObras;
	}
	
	public void setListEstatusObras(List<ConValores> listEstatusObras) {
		this.listEstatusObras = listEstatusObras;
	}
	
	public int getNumPaginaObras() {
		return numPaginaObras;
	}
			
	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}
		
	public List<Obra> getListObrasPrincipales() {
		return listObrasPrincipales;
	}
			
	public void setListObrasPrincipales(List<Obra> listObrasPrincipales) {
		this.listObrasPrincipales = listObrasPrincipales;
	}
		
	public List<SelectItem> getTiposBusquedaObras() {
		return tiposBusquedaObras;
	}
		
	public void setTiposBusquedaObras(List<SelectItem> tiposBusquedaObras) {
		this.tiposBusquedaObras = tiposBusquedaObras;
	}
		
	public String getCampoBusquedaObras() {
		return campoBusquedaObras;
	}
		
	public void setCampoBusquedaObras(String campoBusquedaObras) {
		this.campoBusquedaObras = campoBusquedaObras;
	}
		
	public String getValorBusquedaObras() {
		return valorBusquedaObras;
	}
	
	public void setValorBusquedaObras(String valorBusquedaObras) {
		this.valorBusquedaObras = valorBusquedaObras;
	}
	
	public int getNumPaginaClientes() {
		return numPaginaClientes;
	}

	public void setNumPaginaClientes(int numPaginaClientes) {
		this.numPaginaClientes = numPaginaClientes;
	}
		
	public List<SelectItem> getTiposBusquedaClientes() {
		return tiposBusquedaClientes;
	}
		
	public void setTiposBusquedaClientes(List<SelectItem> tiposBusquedaClientes) {
		this.tiposBusquedaClientes = tiposBusquedaClientes;
	}
	
	public String getCampoBusquedaClientes() {
		return campoBusquedaClientes;
	}
	
	public void setCampoBusquedaClientes(String campoBusquedaClientes) {
		this.campoBusquedaClientes = campoBusquedaClientes;
	}
		
	public String getValorBusquedaClientes() {
		return valorBusquedaClientes;
	}
		
	public void setValorBusquedaClientes(String valorBusquedaClientes) {
		this.valorBusquedaClientes = valorBusquedaClientes;
	}
	
	public String getValorOpcionBusquedaClientes() {
		return valorOpcionBusquedaClientes;
	}
		
	public void setValorOpcionBusquedaClientes(String valorOpcionBusquedaClientes) {
		this.valorOpcionBusquedaClientes = valorOpcionBusquedaClientes;
	}
	
	public List<Persona> getListClientes() {
		return listClientes;
	}
		
	public void setListClientes(List<Persona> listClientes) {
		this.listClientes = listClientes;
	}
		
	public Long getValObraCancelada() {
		return valObraCancelada;
	}
		
	public void setValObraCancelada(Long valObraCancelada) {
		this.valObraCancelada = valObraCancelada;
	}
		
	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}
		
	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}
		
	public Estado getPojoEstado() {
		return pojoEstado;
	}
		
	public void setPojoEstado(Estado pojoEstado) {
		this.pojoEstado = pojoEstado;
	}
		
	public int getNumPaginaMunicipio() {
		return numPaginaMunicipio;
	}
		
	public void setNumPaginaMunicipio(int numPaginaMunicipio) {
		this.numPaginaMunicipio = numPaginaMunicipio;
	}
		
	public Municipio getPojoMunicipio() {
		return pojoMunicipio;
	}
	
	public void setPojoMunicipio(Municipio pojoMunicipio) {
		this.pojoMunicipio = pojoMunicipio;
		this.pojoLocalidad = null;
		this.pojoColonia = null;
	}
		
	public String getBusquedaMunicipio() {
		return busquedaMunicipio;
	}
		
	public void setBusquedaMunicipio(String busquedaMunicipio) {
		this.busquedaMunicipio = busquedaMunicipio;
	}
		
	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}
		
	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}
		
	public String getBusquedaLocalidad() {
		return busquedaLocalidad;
	}
		
	public void setBusquedaLocalidad(String busquedaLocalidad) {
		this.busquedaLocalidad = busquedaLocalidad;
	}
	
	public Localidad getPojoLocalidad() {
		return pojoLocalidad;
	}
	
	public void setPojoLocalidad(Localidad pojoLocalidad) {
		this.pojoLocalidad = pojoLocalidad;
		this.pojoColonia = null;
	}
	
	public int getNumPaginaLocalidad() {
		return numPaginaLocalidad;
	}
		
	public void setNumPaginaLocalidad(int numPaginaLocalidad) {
		this.numPaginaLocalidad = numPaginaLocalidad;
	}
		
	public String getBusquedaColonia() {
		return busquedaColonia;
	}
		
	public void setBusquedaColonia(String busquedaColonia) {
		this.busquedaColonia = busquedaColonia;
	}
		
	public List<Colonia> getListColonias() {
		return listColonias;
	}
		
	public void setListColonias(List<Colonia> listColonias) {
		this.listColonias = listColonias;
	}
		
	public int getNumPaginaColonia() {
		return numPaginaColonia;
	}
		
	public void setNumPaginaColonia(int numPaginaColonia) {
		this.numPaginaColonia = numPaginaColonia;
	}
		
	public Colonia getPojoColonia() {
		return pojoColonia;
	}
		
	public void setPojoColonia(Colonia pojoColonia) {
		this.pojoColonia = pojoColonia;
	}
		
	public DomicilioExt getPojoDomicilio() {
		return pojoDomicilioExt;
	}
		
	public void setPojoDomicilio(DomicilioExt pojoDomicilioExt) {
		this.pojoDomicilioExt = pojoDomicilioExt;
		desglosaDomicilio();
	}
	
	public boolean getHasDomicilio() {
		if (this.pojoDomicilioExt == null)
			return false;
		
		if (this.pojoDomicilioExt.getColonia() == null) 
			return false;
		
		return true;
	}
	
	public void setHasDomicilio(boolean value) {
		
	}
			
	public PersonaExt getPojoPersona() {
		return this.pojoObra.getIdCliente();
	}
			
	public void setPojoPersona(PersonaExt pojoPersona) {
		this.pojoObra.setIdCliente(pojoPersona);
	}
			
	public List<SelectItem> getListCatDomicilios1() {
		return listCatDomicilios1;
	}
			
	public void setListCatDomicilios1(List<SelectItem> listCatDomicilios1) {
		this.listCatDomicilios1 = listCatDomicilios1;
	}
		
	public List<SelectItem> getListCatDomicilios2() {
		return listCatDomicilios2;
	}
		
	public void setListCatDomicilios2(List<SelectItem> listCatDomicilios2) {
		this.listCatDomicilios2 = listCatDomicilios2;
	}
		
	public Long getIdEstadoDom() {
		return this.pojoEstado != null && this.pojoEstado.getId() > 0 ? this.pojoEstado.getId() : 0L;
	}
		
	public void setIdEstadoDom(Long idEstadoDom) {
		try {
			this.pojoEstado = getEstadoById(idEstadoDom);
		} catch (Exception e) {
			log.error("Error en GestionProyectos.ObrasAction.metodo setIdEstadoDom",e);
		}
	}
			
	private ConValores getValorById(Long id, List<ConValores> lista) {
		for (ConValores cv : lista) {
			if (cv.getId() == id.longValue())
				return cv;
		}
		
		return null;
	}
			
	public List<SelectItem> getListEstado() {
		return listEstado;
	}
		
	public void setListEstado(List<SelectItem> listEstado) {
		this.listEstado = listEstado;
	}
		
	public Long getIdDom1() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio1() != null ? this.pojoDomicilioExt.getCatDomicilio1() : 0L;
	}
		
	public void setIdDom1(Long idDom1) {
		this.pojoDomicilioExt.setCatDomicilio1(getValorById(idDom1, this.listTmpCatDomicilios1).getId());
	}
		
	public Long getIdDom2() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio2() != null ? this.pojoDomicilioExt.getCatDomicilio2() : 0L;
	}
		
	public void setIdDom2(Long idDom2) {
		this.pojoDomicilioExt.setCatDomicilio2(getValorById(idDom2, this.listTmpCatDomicilios1).getId());
	}
		
	public Long getIdDom3() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio3() != null ? this.pojoDomicilioExt.getCatDomicilio3() : 0L;
	}
			
	public void setIdDom3(Long idDom3) {
		this.pojoDomicilioExt.setCatDomicilio3(getValorById(idDom3, this.listTmpCatDomicilios1).getId());
	}
		
	public Long getIdDom4() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio4() != null ? this.pojoDomicilioExt.getCatDomicilio4() : 0L;
	}
		
	public void setIdDom4(Long idDom4) {
		this.pojoDomicilioExt.setCatDomicilio4(getValorById(idDom4, this.listTmpCatDomicilios2).getId());
	}
	
	public Long getIdDom5() {
		return this.pojoDomicilioExt != null && this.pojoDomicilioExt.getCatDomicilio5() != null ? this.pojoDomicilioExt.getCatDomicilio5() : 0L;
	}
	
	public void setIdDom5(Long idDom5) {
		this.pojoDomicilioExt.setCatDomicilio5(getValorById(idDom5, listTmpCatDomicilios2).getId());
	}

	public boolean getPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public List<Sucursal> getListSucursales() {
		return listSucursales;
	}

	public void setListSucursales(List<Sucursal> listSucursales) {
		this.listSucursales = listSucursales;
	}

	public List<SelectItem> getListSucursalesItems() {
		return listSucursalesItems;
	}

	public void setListSucursalesItems(List<SelectItem> listSucursalesItems) {
		this.listSucursalesItems = listSucursalesItems;
	}
	
	public Long getSucursal() {
		if (this.pojoObra.getIdSucursal() != null)
			return this.pojoObra.getIdSucursal().getId();
		
		return 0L;
	}
	
	public void setSucursal(Long idSucursal) {
		for (Sucursal var : this.listSucursales) {
			if (var.getId() == idSucursal) {
				this.pojoObra.setIdSucursal(var);
				break;
			}
		}
	}

	public List<ObraEmpleadoExt> getListObraEmpleadosGrid() {
		return listObraEmpleadosGrid;
	}

	public void setListObraEmpleadosGrid(List<ObraEmpleadoExt> listObraEmpleados) {
		this.listObraEmpleadosGrid = listObraEmpleados;
	}

	public ObraEmpleadoExt getPojoObraEmpleado() {
		return pojoObraEmpleado;
	}

	public void setPojoObraEmpleado(ObraEmpleadoExt pojoObraEmpleado) {
		this.pojoObraEmpleado = pojoObraEmpleado;
	}

	public int getNumPaginaEmpleados() {
		return numPaginaEmpleados;
	}

	public void setNumPaginaEmpleados(int numPaginaEmpleados) {
		this.numPaginaEmpleados = numPaginaEmpleados;
	}

	public List<SelectItem> getTiposBusquedaEmpleados() {
		return tiposBusquedaEmpleados;
	}

	public void setTiposBusquedaEmpleados(List<SelectItem> tiposBusquedaEmpleados) {
		this.tiposBusquedaEmpleados = tiposBusquedaEmpleados;
	}

	public String getCampoBusquedaEmpleados() {
		return campoBusquedaEmpleados;
	}

	public void setCampoBusquedaEmpleados(String campoBusquedaEmpleados) {
		this.campoBusquedaEmpleados = campoBusquedaEmpleados;
	}

	public String getValorBusquedaEmpleados() {
		return valorBusquedaEmpleados;
	}

	public void setValorBusquedaEmpleados(String valorBusquedaEmpleados) {
		this.valorBusquedaEmpleados = valorBusquedaEmpleados;
	}

	public List<EmpleadoExt> getListEmpleadosGrid() {
		return listEmpleadosGrid;
	}

	public void setListEmpleadosGrid(List<EmpleadoExt> listEmpleadosGrid) {
		this.listEmpleadosGrid = listEmpleadosGrid;
	}

	public EmpleadoExt getPojoEmpleado() {
		return pojoEmpleado;
	}

	public void setPojoEmpleado(EmpleadoExt pojoEmpleado) {
		this.pojoEmpleado = pojoEmpleado;
	}

	public int getNumPaginaObraEmpleados() {
		return numPaginaObraEmpleados;
	}

	public void setNumPaginaObraEmpleados(int numPaginaObraEmpleados) {
		this.numPaginaObraEmpleados = numPaginaObraEmpleados;
	}

	public List<Empleado> getListResponsablesGrid() {
		return listResponsablesGrid;
	}

	public void setListResponsablesGrid(List<Empleado> listaResponsablesGrid) {
		this.listResponsablesGrid = listaResponsablesGrid;
	}

	public Empleado getPojoResponsable() {
		return pojoResponsable;
	}

	public void setPojoResponsable(Empleado pojoResponsable) {
		this.pojoResponsable = pojoResponsable;
	}

	public List<SelectItem> getTiposBusquedaResponsables() {
		return tiposBusquedaResponsables;
	}

	public void setTiposBusquedaResponsables(
			List<SelectItem> tiposBusquedaResponsables) {
		this.tiposBusquedaResponsables = tiposBusquedaResponsables;
	}

	public String getCampoBusquedaResponsables() {
		return campoBusquedaResponsables;
	}

	public void setCampoBusquedaResponsables(String campoBusquedaResponsables) {
		this.campoBusquedaResponsables = campoBusquedaResponsables;
	}

	public String getValorBusquedaResponsables() {
		return valorBusquedaResponsables;
	}

	public void setValorBusquedaResponsables(String valorBusquedaResponsables) {
		this.valorBusquedaResponsables = valorBusquedaResponsables;
	}

	public int getNumPaginaResponsables() {
		return numPaginaResponsables;
	}

	public void setNumPaginaResponsables(int numPaginaResponsables) {
		this.numPaginaResponsables = numPaginaResponsables;
	}

	public boolean isCargaInsumosValidada() {
		return cargaInsumosValidada;
	}

	public void setCargaInsumosValidada(boolean cargaInsumosValidada) {
		this.cargaInsumosValidada = cargaInsumosValidada;
	}

	public List<ConceptoPresupuesto> getListaConceptosPresupuesto() {
		return listaConceptosPresupuesto;
	}

	public void setListaConceptosPresupuesto(List<ConceptoPresupuesto> listaConceptosPresupuesto) {
		this.listaConceptosPresupuesto = listaConceptosPresupuesto;
	}

	public List<PresupuestoDetalleExt> getListaPresupuestoGrid() {
		return listaPresupuestoGrid;
	}

	public void setListaPresupuestoGrid(List<PresupuestoDetalleExt> listaPresupuestoGrid) {
		this.listaPresupuestoGrid = listaPresupuestoGrid;
	}

	public Presupuesto getPojoPresupuesto() {
		return pojoPresupuesto;
	}

	public void setPojoPresupuesto(Presupuesto pojoPresupuesto) {
		this.pojoPresupuesto = pojoPresupuesto;
	}
	
	public String getTema() {
		return this.pojoPresupuesto == null ? "" : this.pojoPresupuesto.getTema() == null ? "" : this.pojoPresupuesto.getTema();
	}
	
	public void setTema(String tema) {
		this.pojoPresupuesto.setTema(tema);
	}
	
	public String getRuta() {
		return this.pojoPresupuesto == null ? "" : this.pojoPresupuesto.getRuta() == null ? "" : this.pojoPresupuesto.getRuta();
	}
	
	public void setRuta(String ruta) {
		this.pojoPresupuesto.setRuta(ruta);
	}

	public List<ObraAlmacenes> getListObraAlmacenes() {
		return listObraAlmacenes;
	}

	public void setListObraAlmacenes(List<ObraAlmacenes> listObraAlmacenes) {
		this.listObraAlmacenes = listObraAlmacenes;
	}
	
	public int getNumPaginaObraAlmacenes() {
		return numPaginaObraAlmacenes;
	}
	
	public void setNumPaginaObraAlmacenes(int numPaginaObraAlmacenes) {
		this.numPaginaObraAlmacenes = numPaginaObraAlmacenes;
	}

	public ObraAlmacenes getPojoObraAlmacen() {
		return pojoObraAlmacen;
	}

	public void setPojoObraAlmacen(ObraAlmacenes pojoObraAlmacen) {
		this.pojoObraAlmacen = pojoObraAlmacen;
	}

	public ObraAlmacenes getPojoObraAlmacenBorrar() {
		return pojoObraAlmacenBorrar;
	}

	public void setPojoObraAlmacenBorrar(ObraAlmacenes pojoObraAlmacenBorrar) {
		this.pojoObraAlmacenBorrar = pojoObraAlmacenBorrar;
	}

	public List<SelectItem> getListAlmacenesItems() {
		return listAlmacenesItems;
	}
	
	public void setListAlmacenesItems(List<SelectItem> listAlmacenesItems) {
		this.listAlmacenesItems = listAlmacenesItems;
	}

	public Almacen getPojoAlmacen() {
		return pojoAlmacen;
	}

	public void setPojoAlmacen(Almacen pojoAlmacen) {
		this.pojoAlmacen = pojoAlmacen;
	}

	public long getAlmacenId() {
		return almacenId;
	}

	public void setAlmacenId(long almacenId) {
		this.almacenId = almacenId;
		
		// Recuperamos el almacen
		for (Almacen var : this.listAlmacenes) {
			if (almacenId == var.getId()) { 
				this.pojoAlmacen = var;
				break;
			}
		}
	}
	
	public List<ObraSatics> getListObraSatics() {
		return listObraSatics;
	}
	
	public void setListObraSatics(List<ObraSatics> listObraSatics) {
		this.listObraSatics = listObraSatics;
	}

	public List<SelectItem> getListSaticsItems() {
		return listSaticsItems;
	}

	public void setListSaticsItems(List<SelectItem> listSaticsItems) {
		this.listSaticsItems = listSaticsItems;
	}

	public ObraSatics getPojoSatics() {
		return pojoSatics;
	}

	public void setPojoSatics(ObraSatics pojoSatics) {
		this.pojoSatics = pojoSatics;
	}

	public ObraSatics getPojoSaticsBorrar() {
		return pojoSaticsBorrar;
	}

	public void setPojoSaticsBorrar(ObraSatics pojoSaticsBorrar) {
		this.pojoSaticsBorrar = pojoSaticsBorrar;
	}

	public long getSaticsId() {
		return saticsId;
	}

	public void setSaticsId(long saticsId) {
		this.saticsId = saticsId;
	}

	public long getSaticsAdendumId() {
		return saticsAdendumId;
	}

	public void setSaticsAdendumId(long saticsAdendumId) {
		this.saticsAdendumId = saticsAdendumId;
	}

	public int getNumPaginaObraSatics() {
		return numPaginaObraSatics;
	}

	public void setNumPaginaObraSatics(int numPaginaObraSatics) {
		this.numPaginaObraSatics = numPaginaObraSatics;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public List<ObraCobranza> getListObraCobranza() {
		return listObraCobranza;
	}
	
	public void setListObraCobranza(List<ObraCobranza> listObraCobranza) {
		this.listObraCobranza = listObraCobranza;
	}

	public int getNumPaginaObraCobranza() {
		return numPaginaObraCobranza;
	}

	public void setNumPaginaObraCobranza(int numPaginaObraCobranza) {
		this.numPaginaObraCobranza = numPaginaObraCobranza;
	}
	
	public double getTotalCobranza() {
		return totalCobranza;
	}
	
	public void setTotalCobranza(double totalCobranza) {
		this.totalCobranza = totalCobranza;
	}

	public List<SelectItem> getListEstatusContratoItems() {
		return listEstatusContratoItems;
	}

	public void setListEstatusContratoItems(List<SelectItem> listEstatusContratoItems) {
		this.listEstatusContratoItems = listEstatusContratoItems;
	}

	public List<ObraAvance> getListObraAvances() {
		return listObraAvances;
	}

	public void setListObraAvances(List<ObraAvance> listObraAvances) {
		this.listObraAvances = listObraAvances;
	}

	public ObraAvance getPojoObraAvance() {
		return pojoObraAvance;
	}

	public void setPojoObraAvance(ObraAvance pojoObraAvance) {
		this.pojoObraAvance = pojoObraAvance;
	}

	public ObraAvance getPojoObraAvanceBorrar() {
		return pojoObraAvanceBorrar;
	}

	public void setPojoObraAvanceBorrar(ObraAvance pojoObraAvanceBorrar) {
		this.pojoObraAvanceBorrar = pojoObraAvanceBorrar;
	}

	public int getPaginaObraAvance() {
		return paginaObraAvance;
	}

	public void setPaginaObraAvance(int paginaObraAvance) {
		this.paginaObraAvance = paginaObraAvance;
	}

	public List<ObraContratos> getListObraContratos() {
		return listObraContratos;
	}

	public void setListObraContratos(List<ObraContratos> listObraContratos) {
		this.listObraContratos = listObraContratos;
	}

	public ObraContratos getPojoObraContrato() {
		return pojoObraContrato;
	}

	public void setPojoObraContrato(ObraContratos pojoObraContrato) {
		this.pojoObraContrato = pojoObraContrato;
	}

	public ObraContratos getPojoObraContratoBorrar() {
		return pojoObraContratoBorrar;
	}

	public void setPojoObraContratoBorrar(ObraContratos pojoObraContratoBorrar) {
		this.pojoObraContratoBorrar = pojoObraContratoBorrar;
	}

	public int getPaginaObraContratos() {
		return paginaObraContratos;
	}

	public void setPaginaObraContratos(int paginaObraContratos) {
		this.paginaObraContratos = paginaObraContratos;
	}

	public List<SelectItem> getListSubcontratantesItems() {
		return listSubcontratantesItems;
	}

	public void setListSubcontratantesItems(List<SelectItem> listSubcontratantesItems) {
		this.listSubcontratantesItems = listSubcontratantesItems;
	}

	public long getSubcontratanteId() {
		return subcontratanteId;
	}

	public void setSubcontratanteId(long subcontratanteId) {
		this.subcontratanteId = subcontratanteId;
	}

	public int getPaginaPresupuestoDetalle() {
		return paginaPresupuestoDetalle;
	}

	public void setPaginaPresupuestoDetalle(int paginaPresupuestoDetalle) {
		this.paginaPresupuestoDetalle = paginaPresupuestoDetalle;
	}

	public List<SelectItem> getListObraContratosItems() {
		return listObraContratosItems;
	}

	public void setListObraContratosItems(List<SelectItem> listObraContratosItems) {
		this.listObraContratosItems = listObraContratosItems;
	}
	
	public boolean isExistePresupuesto() {
		return existePresupuesto;
	}
	
	public void setExistePresupuesto(boolean existePresupuesto) {
		this.existePresupuesto = existePresupuesto;
	}
	
	public boolean getDebugging() {
		if (this.paramsRequest.containsKey("DEBUG") && "1".equals(this.paramsRequest.get("DEBUG"))) 
			return true;
		return false;
	}
	
	public void setDebugging(boolean value) { }

	public Persona getPojoCliente() {
		return pojoCliente;
	}

	public void setPojoCliente(Persona pojoCliente) {
		this.pojoCliente = pojoCliente;
	}

	public boolean isEgresosOperacion() {
		return perfilEgresosOperacion;
	}
	
	public void setEgresosOperacion(boolean egresosOperacion) {
		this.perfilEgresosOperacion = egresosOperacion;
	}

	public List<SelectItem> getListObrasTiposItems() {
		return listObrasTiposItems;
	}

	public void setListObrasTiposItems(List<SelectItem> listObrasTiposItems) {
		this.listObrasTiposItems = listObrasTiposItems;
	}

	public List<SelectItem> getListMonedasItems() {
		return listMonedasItems;
	}

	public void setListMonedasItems(List<SelectItem> listMonedasItems) {
		this.listMonedasItems = listMonedasItems;
	}

	public boolean isConfirmadoNoSatic() {
		return confirmadoNoSatic;
	}
	
	public void setConfirmadoNoSatic(boolean confirmadoNoSatic) {
		this.confirmadoNoSatic = confirmadoNoSatic;
	}

	public boolean getPerfilAdministrativo() {
		return perfilEgresosOperacion;
	}
	
	public void setPerfilAdministrativo(boolean perfilAdministrativo) {
		this.perfilEgresosOperacion = perfilAdministrativo;
	}

	public boolean getBusquedaAdministrativas() {
		return busquedaAdministrativas;
	}

	public void setBusquedaAdministrativas(boolean busquedaAdministrativas) {
		this.busquedaAdministrativas = busquedaAdministrativas;
	}
}

/* 
 * ----------------------------------------------------------------------------------------------------------------
 * HISTORIAL DE MODIFICACIONES
 * ---------------------------------------------------------------------------------------------------------------- 
 *  VER |   FECHA    |   	AUTOR 		| DESCRIPCIÓN 
 * ---------------------------------------------------------------------------------------------------------------- 
 *  2.2	| 04/05/2016 | Daniel Azamar	| Anexando métodos para presupuestos
 *  2.2	| 05/05/2016 | Daniel Azamar	| Efectuando guardado de presupuesto
 *  2.2	| 20/05/2016 | Javier Tirado	| Se agrega vista para Obras Almacenes
 *  2.2	| 23/05/2016 | Javier Tirado	| Se agrega vista para Obras Satics
 *  2.2	| 24/05/2016 | Javier Tirado	| Se agrega vista para Obras Cobranza
 *  2.2	| 15/06/2016 | Javier Tirado	| Se agrega vista para Obras Avances
 *  2.2	| 22/06/2016 | Javier Tirado	| Se modifico para que tomara el porcentaje IVA del perfil VALOR_IVA para la variable A15.
 *  1.2	| 2016-11-09 | Javier Tirado	| Cambio el metodo de busqueda de clientes para obras.
 *  2.2 | 2017-01-21 | Javier Tirado 	| Permito monto deductiva en cero para las todas las obras
 *  2.2 | 2017-01-21 | Javier Tirado 	| Permito seleccionar Obra Principal para el tipo de obra ' Orden de Trabajo'.
 *  2.2 | 2017-01-21 | Javier Tirado 	| Permito monto anticipo en cero para tipo de obra 'Orden de Trabajo'.
 */