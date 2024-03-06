package net.giro.dashboard;

import java.io.Serializable;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

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
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="dashAction")
public class DashboardAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(DashboardAction.class);
	private LoginManager loginManager;
	//private HttpSession httpSession;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private SucursalesRem ifzSucursales;
	private EmpleadoRem ifzEmpleados;
	private InsumosRem ifzInsumos;
	private ConceptoPresupuestoRem ifzConceptosPresupuesto;
	private PresupuestoRem ifzPresupuesto;
	private PresupuestoDetalleRem ifzPresupuestoDetalle;
	private ObraAlmacenesRem ifzObraAlmacenes;
	private AlmacenRem ifzAlmacenes;
	private FacturaRem ifzFacturas;
	private FacturaDetalleRem ifzFacConceptos;
	private ReportesRem ifzReportes;
	private ObraRem ifzObras;
	private ObraContratosRem ifzContratos;
	private ObraEmpleadoRem ifzObraEmpleados;
	private ObraAvanceRem ifzObraAvance;
	private ObraSaticsRem ifzObraSatics;
	private ObraCobranzaRem ifzCobranza;
	/*private ClientesRem ifzClientes;
	private FtpRem ifzFtp;
	private MonedaDAO ifzMonedas;*/
	

    public DashboardAction() {
    	InitialContext ctx = null;
		FacesContext facesContext = null;
		Application app = null;
    	ValueExpression vExpression = null;
    	//String valPerfil = "";
    	//Moneda valMoneda = null;
    	// ------------------------------------------------
    	//PropertyResourceBundle entornoProperties;
    	//PropertyResourceBundle msgProperties;
		
    	try {
			facesContext = FacesContext.getCurrentInstance();
			app = facesContext.getApplication();
			
			vExpression = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) vExpression.getValue(facesContext.getELContext());
			//this.httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
			
			/*vExpression = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			entornoProperties = (PropertyResourceBundle) vExpression.getValue(facesContext.getELContext());
			
			vExpression = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
			msgProperties = (PropertyResourceBundle) vExpression.getValue(facesContext.getELContext());*/
			
			/*this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : facesContext.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			this.puedeEditar = true;*/ 

			// PERFILES
			//comprobarPerfiles();
			
			ctx = new InitialContext();
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObraEmpleados = (ObraEmpleadoRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraEmpleadoFac!net.giro.adp.logica.ObraEmpleadoRem");
			this.ifzObraAvance = (ObraAvanceRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraAvanceFac!net.giro.adp.logica.ObraAvanceRem");
			this.ifzContratos = (ObraContratosRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraContratosFac!net.giro.adp.logica.ObraContratosRem");
			this.ifzGpoVal = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			//this.ifzClientes = (ClientesRem) ctx.lookup("ejb:/Logica_Clientes//ClientesFac!net.giro.clientes.logica.ClientesRem");
			this.ifzSucursales = (SucursalesRem) ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzObraAlmacenes = (ObraAlmacenesRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzAlmacenes = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzObraSatics = (ObraSaticsRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraSaticsFac!net.giro.adp.logica.ObraSaticsRem");
			//this.ifzFtp = (FtpRem) ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			this.ifzCobranza = (ObraCobranzaRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraCobranzaFac!net.giro.adp.logica.ObraCobranzaRem");
			this.ifzFacturas = (FacturaRem)	ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacConceptos = (FacturaDetalleRem) ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzInsumos = (InsumosRem) ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzConceptosPresupuesto = (ConceptoPresupuestoRem) ctx.lookup("ejb:/Logica_GestionProyectos//ConceptoPresupuestoFac!net.giro.adp.logica.ConceptoPresupuestoRem");
			this.ifzPresupuesto = (PresupuestoRem) ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoFac!net.giro.adp.logica.PresupuestoRem");
			this.ifzPresupuestoDetalle = (PresupuestoDetalleRem) ctx.lookup("ejb:/Logica_GestionProyectos//PresupuestoDetalleFac!net.giro.adp.logica.PresupuestoDetalleRem");
			//this.ifzMonedas = (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			
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

			/*valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			valMoneda = this.ifzMonedas.findByAbreviacion(valPerfil);
			if (valMoneda != null)
				this.monedaBase = valMoneda;
			
			this.pojoObra 				= new ObraExt();
			this.pojoGpoEstatusObras 	= new ConGrupoValores();
			this.pojoObraAlmacen 		= new ObraAlmacenes();
			this.pojoDomicilioExt		= new DomicilioExt();
			this.listObrasGrid 			= new ArrayList<Obra>();
			this.listEstatusObras 		= new ArrayList<ConValores>();
			this.listObrasPrincipales 	= new ArrayList<Obra>();
			this.listSucursales 		= new ArrayList<Sucursal>();
			this.listEmpleadosGrid		= new ArrayList<EmpleadoExt>();
			this.listResponsablesGrid	= new ArrayList<Empleado>();
			this.listTmpCatDomicilios1 	= new ArrayList<ConValores>();
			this.listTmpCatDomicilios2 	= new ArrayList<ConValores>();			
			this.listObraAlmacenes		= new ArrayList<ObraAlmacenes>();
			this.listAlmacenes 			= new ArrayList<Almacen>();
			this.listEstatusObrasItems 	= new ArrayList<SelectItem>();
			this.listEstatusContratoItems = new ArrayList<SelectItem>();
			this.listCatDomicilios1 	= new ArrayList<SelectItem>();
			this.listCatDomicilios2 	= new ArrayList<SelectItem>();
			this.listEstado			 	= new ArrayList<SelectItem>();
			this.listSucursalesItems 	= new ArrayList<SelectItem>();
			this.listAlmacenesItems = new ArrayList<SelectItem>();
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusqueda.add(new SelectItem("nombreObraPrincipal", "Obra Principal"));
			this.tiposBusqueda.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.tipoObraBusqueda = 0;
			this.numPagina = 1;
			
			// Busqueda clientes
			this.listClientes = new ArrayList<Persona>();
			this.pojoCliente = new Persona();
			this.tiposBusquedaClientes = new ArrayList<SelectItem>();
			this.tiposBusquedaClientes.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaClientes.add(new SelectItem("id", "ID"));
			nuevaBusquedaClientes();

			// Busqueda obras principales
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
			
			// Busquera empleados
			this.tiposBusquedaEmpleados = new ArrayList<SelectItem>();
			this.tiposBusquedaEmpleados.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaEmpleados.add(new SelectItem("id", "ID"));
			nuevaBusquedaEmpleados();
			
			// Busqueda responsable
			this.tiposBusquedaResponsables = new ArrayList<SelectItem>();
			this.tiposBusquedaResponsables.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaResponsables.add(new SelectItem("id", "ID"));
			nuevaBusquedaResponsables();
			
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

			this.ftpDigitalizacionHost = this.entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = this.entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = this.entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = this.entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta");

			// Cargamos datos
			comprobarGrupos();
			cargarMonedas();
			cargaEstados();
			cargaSucursales();
			nuevoDomicilio();
			
			this.listaPresupuestoGrid = new ArrayList<>();
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
				this.listObrasTiposItems.add(new SelectItem(4, this.msgProperties.getString("obrasTipo.administrativa")));*/
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
		} 
    }
}
