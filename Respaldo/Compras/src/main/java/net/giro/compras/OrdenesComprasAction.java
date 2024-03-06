package net.giro.compras;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraExt;
import net.giro.adp.beans.TiposObra;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.PersonaExt;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.CotizacionDetalleExt;
import net.giro.compras.beans.CotizacionExt;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.compras.beans.PreOrdenDetalle;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.beans.RequisicionDetalleExt;
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.TipoMaestro;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.SubfamiliaImpuestos;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.SubfamiliaImpuestosRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.dao.MonedasValoresDAO;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="ordenAction")
public class OrdenesComprasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OrdenesComprasAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx;	
	// Interfaces
	private OrdenCompraRem ifzOrdenes;
	private OrdenCompraDetalleRem ifzOrdenDetalles;
	private CotizacionDetalleRem ifzCotiDetalles;
	private RequisicionDetalleRem ifzReqDetalles;
	private ObraRem ifzObras;
	private EmpleadoRem ifzEmpleados;
	private ReportesRem	ifzReportes;
	private ProductoRem ifzProductos;
	private SubfamiliaImpuestosRem ifzSubfamliaImto;
	// Listas
	private List<OrdenCompra> listOrdenes;
	private List<OrdenCompraDetalleExt> listOCDetalles;
	private List<PreOrdenDetalle> listOrdenDetalles;
	private List<PreOrdenDetalle> listOrdenDetallesEliminados;
	// POJO's
	private ObraExt pojoObra;
	private OrdenCompraExt pojoOrden;
	private OrdenCompra pojoOrdenAutorizar;
	private OrdenCompra pojoOrdenBorrar;
	private PreOrdenDetalle pojoOrdenDetalle;
	private PreOrdenDetalle pojoOrdenDetalleBorrar;
	// Busqueda principal
	private List<Obra> listObrasGrid;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Busqueda Cotizaciones
	private CotizacionRem ifzCotizaciones;
	private List<Cotizacion> listCotizaciones;
	private Cotizacion pojoCotizacion;
	private List<SelectItem> tiposBusquedaCotizaciones;	
	private String campoBusquedaCotizaciones;
	private String valorBusquedaCotizaciones;
	private int numPaginaCotizaciones;
	// Busqueda Requisiciones
	private RequisicionRem ifzRequisiciones;
	private List<Requisicion> listRequisiciones;
	private Requisicion pojoRequisicionSeleccionada;
	private Requisicion pojoRequisicion;
	private List<SelectItem> tiposBusquedaRequisiciones;	
	private String campoBusquedaRequisiciones;
	private String valorBusquedaRequisiciones;
	private int numPaginaRequisiciones;
	private List<CotizacionDetalleExt> listCotDetalles;
	// Busqueda Proveedores
	private List<PersonaExt> listProveedores;
	private List<SelectItem> tiposBusquedaProveedores;	
	private String campoBusquedaProveedores;
	private String valorBusquedaProveedores;
	private String valorBusquedaTipoProveedor;
	private int numPaginaProveedores;
	private PersonaExt pojoProveedor;
	private PersonaExt pojoContacto;
	// Busqueda obras
	private List<ObraExt> listObras;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorBusquedaTipoObra;
	private int numPaginaObras;
	// Busqueda empleados
	private List<Empleado> listEmpleados;
	private Empleado pojoEmpleado;
	private List<SelectItem> tiposBusquedaEmpleados;	
	private String campoBusquedaEmpleados;
	private String valorBusquedaEmpleados;
	private int numPaginaEmpleados;
	// Variables de operacion
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
    private long usuarioId;
    private String usuario;
	private int numPaginaOrdenes;
	private int numPaginaDetalles;
	private double porcentajeIva;
	private double subtotal;
	private double iva;
	private double total;
    private boolean permiteModificar;
    private boolean editarOrdenCompra;
	private long valObraCancelada;
	private boolean seleccionarTodos;
	private long estatusOrdenCompra;
	private boolean esAdministrativo;
	private TipoMaestro tipoMaestro;
	private boolean origenRequisicion;
	private List<SelectItem> listEstatusOrdenCompraItems;
	private List<SubfamiliaImpuestos> listImpuestos;
	private String patternNumber;
	private String patternDecimal;
	// Monedas
	private MonedaDAO ifzMonedas;
	private MonedasValoresDAO ifzMonValores;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	//private long idMoneda;
	private long idMonedaBase;
	private long idMonedaActual;
	private long idMonedaOriginal;
	private double tipoCambioActual;
	private boolean evaluaConversion;
	private boolean reConvierte;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	

	public OrdenesComprasAction() {
		Map<String, String> params = new HashMap<String, String>();
		PropertyResourceBundle msgProperties = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			msgProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			params = fc.getExternalContext().getRequestParameterMap();
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey(), item.getValue());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			// Conexion con modulos
			this.ctx = new InitialContext();
			this.ifzOrdenes = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOrdenDetalles = (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzCotizaciones = (CotizacionRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzCotiDetalles = (CotizacionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzRequisiciones = (RequisicionRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalles = (RequisicionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzProductos = (ProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzSubfamliaImto = (SubfamiliaImpuestosRem) this.ctx.lookup("ejb:/Logica_Publico//SubfamiliaImpuestosFac!net.giro.plataforma.logica.SubfamiliaImpuestosRem");
			this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzMonValores = (MonedasValoresDAO) this.ctx.lookup("ejb:/Model_TYG//MonedasValoresImp!net.giro.tyg.dao.MonedasValoresDAO");
						
			/* VALIDACION DE PERFILES */
			valPerfil = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			this.porcentajeIva = ((valPerfil != null && ! "".equals(valPerfil.trim())) ? Double.valueOf(valPerfil) : 0);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.esAdministrativo = ((valPerfil != null && "S".equals(valPerfil.trim())) ? true : false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			Moneda valMoneda = this.ifzMonedas.findByAbreviacion(valPerfil);
			if (valMoneda != null)
				this.idMonedaBase = valMoneda.getId();
			
			// Listas
			this.listObrasGrid = new ArrayList<Obra>();
			this.listOrdenes = new ArrayList<OrdenCompra>();
			this.listOrdenDetalles = new ArrayList<PreOrdenDetalle>();
			this.listObras = new ArrayList<ObraExt>();
			this.pojoOrden = new OrdenCompraExt();
			this.pojoOrdenBorrar = new OrdenCompra();
			this.listImpuestos = new ArrayList<SubfamiliaImpuestos>();
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("nombre", "Nombre Obra"));
			this.tiposBusqueda.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusqueda.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusqueda.add(new SelectItem("nombreResponsable", "Encargado"));
			this.tiposBusqueda.add(new SelectItem("oc.folio", "Folio Orden Compra"));
			this.tiposBusqueda.add(new SelectItem("oc.nombreProveedor", "Proveedor Orden Compra"));
			this.tiposBusqueda.add(new SelectItem("id", "Clave"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			
			// Busqueda Cotizaciones
			this.tiposBusquedaCotizaciones = new ArrayList<SelectItem>();
			this.tiposBusquedaCotizaciones.add(new SelectItem("nombreProveedor", "Proveedor"));
			this.tiposBusquedaCotizaciones.add(new SelectItem("nombreComprador", "Comprador"));
			this.campoBusquedaCotizaciones = this.tiposBusquedaCotizaciones.get(0).getValue().toString();
			this.valorBusquedaCotizaciones = "";
			
			// Busqueda Proveedor
			this.tiposBusquedaProveedores = new ArrayList<SelectItem>();
			this.tiposBusquedaProveedores.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaProveedores.add(new SelectItem("rfc", "RFC"));
			this.tiposBusquedaProveedores.add(new SelectItem("id", "ID"));
			this.campoBusquedaProveedores = this.tiposBusquedaProveedores.get(0).getValue().toString();
			this.valorBusquedaProveedores = "";
			this.valorBusquedaTipoProveedor = "N";
			this.numPaginaProveedores = 1;
			
			// Busqueda requisiciones
			this.tiposBusquedaRequisiciones = new ArrayList<SelectItem>();
			this.tiposBusquedaRequisiciones.add(new SelectItem("nombreSolicita", "Solicita"));
			this.tiposBusquedaRequisiciones.add(new SelectItem("id", "ID"));
			this.campoBusquedaRequisiciones = this.tiposBusquedaRequisiciones.get(0).getValue().toString();
			this.valorBusquedaRequisiciones = "";
			this.numPaginaRequisiciones = 1;
			
			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "Clave"));
			this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
			this.valorBusquedaObras = "";
			this.valorBusquedaTipoObra = 0;
			
			// Busqueda Empleados
			this.tiposBusquedaEmpleados = new ArrayList<SelectItem>();
			this.tiposBusquedaEmpleados.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaEmpleados.add(new SelectItem("id", "Clave"));
			this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();
			this.valorBusquedaEmpleados = "";
			
			// Estatus Orden Compra
			this.listEstatusOrdenCompraItems = new ArrayList<SelectItem>();
			this.listEstatusOrdenCompraItems.add(new SelectItem(0L, msgProperties.getString("ordenCompra.autorizadas")));
			//this.listEstatusOrdenCompraItems.add(new SelectItem(2L, msgProperties.getString("ordenCompra.usadas")));
			this.listEstatusOrdenCompraItems.add(new SelectItem(3L, msgProperties.getString("ordenCompra.noAutorizadas")));
			this.listEstatusOrdenCompraItems.add(new SelectItem(1L, msgProperties.getString("ordenCompra.eliminadas")));
			this.estatusOrdenCompra = (long) this.listEstatusOrdenCompraItems.get(0).getValue();
			
			cargarMonedas();

			this.numPagina = 1;
			this.numPaginaOrdenes = 1;
			this.numPaginaObras = 1;
			this.numPaginaDetalles = 1;
			this.numPaginaCotizaciones = 1;
			this.numPaginaEmpleados = 1;
			this.valObraCancelada = 0L;

			this.tipoMaestro = TipoMaestro.Inventario;
			this.patternNumber = "###,###,##0";
			this.patternDecimal = "###,###,##0.00##";
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction", e);
			this.ctx = null;
		}
	}

	
	public void buscar() throws Exception {
		List<Object> listObject = new ArrayList<Object>();
		List<OrdenCompra> listOrdenes = null;
		
		try {
			control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = "id";
			
			if (this.listObrasGrid == null) 
				this.listObrasGrid = new ArrayList<Obra>();
			this.listObrasGrid.clear();
			
			if (this.campoBusqueda.contains("oc.")) {
				log.info("Buscamos obras por el folio de Orden de Compra");
				listOrdenes = this.ifzOrdenes.findLikeProperty(this.campoBusqueda.substring(3), this.valorBusqueda, 0);
				if (listOrdenes != null && ! listOrdenes.isEmpty()) {
					for (OrdenCompra var : listOrdenes) 
						listObject.add(var.getIdObra());
					
					this.valObraCancelada = this.ifzObras.findEstatusCanceladoObras();
					this.ifzObras.orderBy("CASE model.estatus WHEN 0 THEN 1 ELSE 0 END, model.nombre");
					this.listObrasGrid = this.ifzObras.findInProperty("id", listObject);
					listObject.clear();
				}
			} else {
				log.info("Buscando obras. " + this.campoBusqueda + ":" + this.valorBusqueda);
				this.valObraCancelada = this.ifzObras.findEstatusCanceladoObras();
				this.ifzObras.orderBy("CASE model.estatus WHEN 0 THEN 1 ELSE 0 END, model." + this.campoBusqueda);
				this.listObrasGrid = this.ifzObras.findLikeProperty(this.campoBusqueda, this.valorBusqueda, this.esAdministrativo);
			}

			if (this.listObrasGrid == null || this.listObrasGrid.isEmpty()) {
				log.info("Error 2 - Busqueda sin resultados");
				control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listObrasGrid != null) 
				this.listObrasGrid.clear();
			log.error("Error en Compras.OrdenesComprasAction.buscar", e);
			control(true);
    	} finally {
    		if (this.listObrasGrid != null && ! this.listObrasGrid.isEmpty())
    			log.info(this.listObrasGrid.size() + " resultados encontrados.");
    	}
	}

	public void nuevo() throws Exception {
		try {
			control();
			this.origenRequisicion = false;
			this.editarOrdenCompra = true;
			this.pojoOrden = new OrdenCompraExt();
			this.pojoCotizacion = null;
			this.pojoProveedor = null;
			this.pojoContacto = null;
			this.pojoRequisicion = null;
			this.pojoRequisicionSeleccionada = null;
			this.pojoOrdenBorrar = null;
			this.idMonedaActual = 0;
			this.idMonedaOriginal = 0;
			
			if (this.pojoObra != null && this.pojoObra.getId() > 0L) {
				this.pojoOrden.setIdObra(this.pojoObra);
				this.pojoOrden.setLugarEntrega(this.pojoObra.getNombre());
			}
			
			if (this.listOrdenDetalles == null)
				this.listOrdenDetalles = new ArrayList<PreOrdenDetalle>();
			this.listOrdenDetalles.clear();
			
			if (this.listOrdenDetallesEliminados == null)
				this.listOrdenDetallesEliminados = new ArrayList<PreOrdenDetalle>();
			this.listOrdenDetallesEliminados.clear();
			
			if (this.listImpuestos == null)
				this.listImpuestos = new ArrayList<SubfamiliaImpuestos>();
			this.listImpuestos.clear();
			
			this.totalizarDetalles();
			this.seleccionarTodos = false;
    	} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.nuevo", e);
			control(true);
    	}
	}

	public void nuevoSinObra() throws Exception {
		this.pojoObra = null;
		this.nuevo();
	}
	
	public void nuevoCotizacion() {
		this.origenRequisicion = false;
	}
	
	public void nuevoRequisicion() {
		this.origenRequisicion = true;
	}
	
	public void ver() throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		try {
			control();
			log.info("Obra seleccionada: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
			this.numPaginaOrdenes = 1;
			this.permiteModificar = true;
			if(this.pojoObra.getEstatus().equals(this.valObraCancelada)) 
				this.permiteModificar = false;
			
			if (this.listOrdenes == null)
				this.listOrdenes = new ArrayList<OrdenCompra>();
			this.listOrdenes.clear();

			this.tipoMaestro = TipoMaestro.Inventario;
			if(this.pojoObra.getTipoObra() == TiposObra.Administrativa.ordinal()) 
				this.tipoMaestro = TipoMaestro.Administrativo;
			
			// Recuperamos las Ordenes de Compra para esa obra
			log.info("Cargo Ordenes de Compra ... ");
			if (this.pojoObra.getId() != null && this.pojoObra.getId() > 0L) {
				log.info("Recuperando OCs por obra: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
				params.put("idObra", this.pojoObra.getId());
				//params.put("autorizado", 0); // TODO: Este siempre debe ser 1, salvo que 'estatusOrdenCompra' sea 3
				if (this.permiteModificar) 
					params.put("estatus", this.estatusOrdenCompra);
				if (this.estatusOrdenCompra == 3) {
					params.put("autorizado", 0);
					params.put("estatus", 0);
				}
				this.ifzOrdenes.OrderBy("CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.id desc");
				this.listOrdenes = this.ifzOrdenes.findByProperties(params, 0);
			}
    	} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.ver", e);
			control(true);
    	} finally {
			if (this.listOrdenes == null)
				this.listOrdenes = new ArrayList<OrdenCompra>();
			log.info(this.listOrdenes.size() + " Ordenes de Compra encontradas.");
    	}
	}

	public void editar() throws Exception {
		try {
			control();
			this.editarOrdenCompra = true;
			if (this.pojoOrden.getEstatus() == 2)
				this.editarOrdenCompra = false;
			
			// Obtengo la moneda de la Orden de Compra
			this.idMonedaActual = this.pojoOrden.getIdMoneda();
			this.idMonedaOriginal = this.pojoOrden.getIdMoneda();
			
			// detalles desde la orden de compra 
			this.recuperarListadoDetalles(true);
			
			this.nuevaBusquedaCotizaciones();
			this.nuevaBusquedaObras();
			this.seleccionarTodos = false;
    	} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.editar", e);
			control(true);
    	}
	}
	
	public void guardar() throws Exception {
		boolean isNew = false;
		
		try {
			control(true);
			if (! validacionOrden()) 
				return;
			
			if (this.origenRequisicion && ! guardarCotizacion())
				return;
			
			if (this.listOCDetalles == null)
				this.listOCDetalles = new ArrayList<OrdenCompraDetalleExt>();
			this.listOCDetalles.clear();
			
			//seleccionados = 0;
			this.pojoOrden.setSubtotal(this.subtotal);
			this.pojoOrden.setIva(this.iva);
			this.pojoOrden.setTotal(this.total);
			this.pojoOrden.setModificadoPor(this.usuarioId);
			this.pojoOrden.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoOrden.setTipo(this.tipoMaestro.ordinal());
			
			// Generamos el folio
			if (this.pojoOrden.getFolio() == null || "".equals(this.pojoOrden.getFolio())) {
				if (this.isDebug) log.info("Generamos folio para OC");
				SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
				int consecutivo = this.ifzOrdenes.findConsecutivoByProveedor(this.pojoOrden.getIdProveedor().getId());
				String idPro = String.valueOf(this.pojoOrden.getIdProveedor().getId());
				String annio = formateador.format(Calendar.getInstance().getTime());
				String folio = "";

				idPro = idPro.substring(idPro.length() - 4);
				annio = annio.substring(annio.length() - 2);
				folio = idPro + "-" + annio + "-" + ((consecutivo < 10) ? "0" : "") + consecutivo;
				
				this.pojoOrden.setFolio(folio);
				this.pojoOrden.setConsecutivoProveedor(consecutivo);
				if (this.isDebug) log.info("OC Folio: " + folio);
			}
			
			if (this.pojoOrden.getId() == null) {
				isNew = true;
				if (this.isDebug) log.info("Guardando OC nueva");
				this.pojoOrden.setCreadoPor(this.usuarioId);
				this.pojoOrden.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD
				this.pojoOrden.setId(this.ifzOrdenes.save(this.pojoOrden));
				if (this.isDebug) log.info("OC guardada");
				
				// Agregamos la cotizacion al listado
				this.listOrdenes.add(this.ifzOrdenes.findById(this.pojoOrden.getId()));
			} else {
				if (this.isDebug) log.info("Actualizando OC");
				// Buscamos la OC en el listado
				for(OrdenCompra var : this.listOrdenes) {
					if (var.getId() == this.pojoOrden.getId()) {
						var = this.ifzOrdenes.findById(this.pojoOrden.getId());
						var.setFechaModificacion(Calendar.getInstance().getTime());
						var.setModificadoPor(this.usuarioId);
						
						// Actualizamos en la BD	
						this.ifzOrdenes.update(var);
						if (this.isDebug) log.info("OC actualizada");
						break;
					}
				}
			}
			
			// Borramos los detalles de la OC si corresponde
			if (this.listOrdenDetallesEliminados != null && !this.listOrdenDetallesEliminados.isEmpty()) {
				if (this.isDebug) log.info("Borramos detalles previamente eliminados");
				for(PreOrdenDetalle var : this.listOrdenDetallesEliminados) {
					if (var.getId() != null && var.getId() > 0L) {
						// Lo Quitamos de la OC
						this.ifzOrdenDetalles.delete(var.getId());
					}
				}
				
				liberarDetalles();
				this.listOrdenDetallesEliminados.clear();
			}
			
			// Guardamos los detalles nuevos y modificados
			if (this.listOrdenDetalles != null && ! this.listOrdenDetalles.isEmpty()) {
				for(PreOrdenDetalle var : this.listOrdenDetalles) {
					if (var.isSeleccionado()) {
						// Asignamos la cotizacion al detalle
						if (this.isDebug) log.info("Generando OCDetalle de producto seleccionado");
						OrdenCompraDetalleExt pojoDetalle = var.getOrdenCompraDetalle();
						pojoDetalle.setIdOrdenCompra(this.pojoOrden);
						pojoDetalle.setFechaModificacion(Calendar.getInstance().getTime());
						pojoDetalle.setModificadoPor(this.usuarioId);
						
						// Guardamos el detalle
						if (pojoDetalle.getId() == null || pojoDetalle.getId() <= 0L) {
							if (this.isDebug) log.info("Guardando OCDetalle nuevo");
							pojoDetalle.setFechaCreacion(Calendar.getInstance().getTime());
							pojoDetalle.setCreadoPor(this.usuarioId);
							
							// Guardamos en la BD
							pojoDetalle.setId(this.ifzOrdenDetalles.save(pojoDetalle));
							if (this.isDebug) log.info("OCDetalle guardado");
						} else {
							if (this.isDebug) log.info("Actualizando OCDetalle");
							// Actualizamos en la BD
							this.ifzOrdenDetalles.update(pojoDetalle);
							if (this.isDebug) log.info("OCDetalle actualizado");
						}
						
						// Añadimos a listado de detalles
						this.listOCDetalles.add(pojoDetalle);
						
						// Comprobamos el precio unitario del producto y lo actualizamos si corresponde
						try {
							Producto p = this.ifzProductos.findById(pojoDetalle.getIdProducto().getId());
							if (p != null && p.getId() != null && p.getId() > 0L && p.getPrecioUnitario() < pojoDetalle.getPrecioUnitario()) {
								if (this.isDebug) log.info("Actualizando precio unitario del producto seleccionado");
								p.setPrecioCompra(pojoDetalle.getPrecioUnitario());
								p.setPrecioVenta(pojoDetalle.getPrecioUnitario() * 1.7); // 70%
								p.setPrecioUnitario(pojoDetalle.getPrecioUnitario() * 1.1); // 10%
								this.ifzProductos.update(p);
								if (this.isDebug) log.info("Precio unitario actualizado");
							}
						} catch (Exception e) {
							log.warn("Error en Compras.OrdenesComprasAction.guardar --> Actualizar precio unitario desde ORDEN DE COMPRA " + this.pojoOrden.getId() + " para el Producto " + pojoDetalle.getIdProducto().getId(), e);
						}
					}
				}
				
				this.listOrdenDetalles.clear();
			}
			
			// Atualizo el estatus de la COTIZACION si corresponde
			actualizaEstatusCotizacion();

			// Atualizo el estatus de la REQUISICION si corresponde
			if (this.origenRequisicion)
				actualizaEstatusRequisicion();
			
			// Actualizamos la obra elegida si corresponde
			if(isNew && this.pojoObra == null)
				this.pojoObra = this.pojoOrden.getIdObra().Copia();

			// Actualizamos el listado
			this.ver();
			
			this.origenRequisicion = false;
			this.pojoOrden = new OrdenCompraExt();
			this.pojoOrdenBorrar = null;
			this.pojoRequisicionSeleccionada = null;
    	} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.guardar", e);
			control(true);
    	}
	}
	
	private boolean guardarCotizacion() {
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
		int consecutivo = 0; 
		String idPro = ""; 
		String annio = ""; 
		String folio = "";
		
		try {
			if (this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				log.info("Generando folio para cotizacion");
				consecutivo = this.ifzCotizaciones.findConsecutivoByProveedor(this.pojoProveedor.getId());
				idPro = String.valueOf(this.pojoProveedor.getId());
				annio = formateador.format(Calendar.getInstance().getTime());
	
				idPro = idPro.substring(idPro.length() - 4);
				annio = annio.substring(annio.length() - 2);
				folio = idPro + "-" + annio + "-" + ((consecutivo < 10) ? "0" : "") + consecutivo;
				
				log.info("Folio generado: " + folio);

				// Guardo otizacion
				log.info("Guardando cotizacion");
				this.pojoCotizacion.setFolio(folio);
				this.pojoCotizacion.setSubtotal(this.subtotal);
				this.pojoCotizacion.setIva(this.iva);
				this.pojoCotizacion.setTotal(this.total);
				this.pojoCotizacion.setConsecutivoProveedor(consecutivo);
				this.pojoCotizacion.setTiempoEntrega(this.pojoOrden.getTiempoEntrega());
				this.pojoCotizacion.setTipo(this.tipoMaestro.ordinal());
				this.pojoCotizacion.setCreadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoCotizacion.setModificadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Inserto en la BD
				this.pojoCotizacion.setId(this.ifzCotizaciones.save(this.pojoCotizacion));
				log.info("Cotizacion guardada. Guardando detalles de cotizacion");
				
				// Detalles COTIZACION
				CotizacionExt coti = this.ifzCotizaciones.convertir(this.pojoCotizacion);
				if (this.listCotDetalles != null && ! this.listCotDetalles.isEmpty()) {
					for (CotizacionDetalleExt var :  this.listCotDetalles) {
						var.setIdCotizacion(coti);
						var.setCreadoPor(this.usuarioId);
						var.setFechaCreacion(Calendar.getInstance().getTime());
						var.setModificadoPor(this.usuarioId);
						var.setFechaModificacion(Calendar.getInstance().getTime());
						var.setId(this.ifzCotiDetalles.save(var));
					}
				}
				log.info("Detalles de cotizacion guardados.");
				
				log.info("Detalles de cotizacion guardados. Asigno cotizacion a Orden de Compra");
				this.pojoOrden.setIdCotizacion(coti);
			}
			
		} catch (Exception e) {
			log.error("ERROR en Compras.OrdenesComprasAction.guardarCotizacion() No se pudo guardar la cotizacion", e);
			control("ERROR INTERNO - Ocurrio un problema al intentar guardar la Orden de Compra desde Requisicion");
			return false;
		}
		
		return true;
	}
	
	public void autorizar() {
		try {
			control();
			if (this.pojoOrdenAutorizar == null || this.pojoOrdenAutorizar.getId() == null || this.pojoOrdenAutorizar.getId() <= 0L)
				return;
			
			// Actualizamos el elemento de la lista
			for (OrdenCompra var : this.listOrdenes) {
				if (var.getId() == this.pojoOrdenAutorizar.getId()) {
					if (this.isDebug) log.info("Autorizando OC");
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					var.setEstatus(2);
					
					// Actualizamos el pojo de la BD
					this.ifzOrdenes.update(var);
					if (this.isDebug) log.info("OC Autorizada");
					break;
				}
			}

			this.pojoOrdenBorrar = null;
			this.pojoOrdenAutorizar = null;
			this.pojoOrden = new OrdenCompraExt();
			this.ver();
    	} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.autorizar", e);
			control(true);
    	}
	}
	
	public void eliminar() {
		try {
			control();
			if (this.pojoOrdenBorrar == null || this.pojoOrdenBorrar.getId() == null || this.pojoOrdenBorrar.getId() <= 0L)
				return;
			
			// Actualizamos el elemento de la lista
			for (OrdenCompra var : this.listOrdenes) {
				if (var.getId() == this.pojoOrdenBorrar.getId()) {
					log.info("Borrando OC");
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					var.setEstatus(1);
					
					// Actualizamos el pojo de la BD
					this.ifzOrdenes.update(var);
					log.info("OC Borrada (Actualizada)");
					break;
				}
			}
			
			// Liberamos productos 
			log.info("Liberando cotizacion de OC");
			liberarCotizacion();
			log.info("Cotizacion liberada");

			this.pojoOrdenBorrar = null;
			this.pojoOrdenAutorizar = null;
			this.pojoOrden = new OrdenCompraExt();
			this.ver();
    	} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.eliminar", e);
			control(true);
    	}
	}

	public void eliminarDetalle() {
		try {
			control();
			// Quitamos el detalle de la lista
			if (this.isDebug) log.info("Borrando producto listado");
			this.listOrdenDetalles.remove(this.pojoOrdenDetalleBorrar);
			this.listOrdenDetallesEliminados.add(this.pojoOrdenDetalleBorrar);
			this.pojoOrdenDetalleBorrar = null;
    	} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.eliminarDetalle", e);
			control(true);
    	}
	}
	
	public void reporte() {
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		String nombreDocumento = "";
		
		try {
			if (this.pojoOrdenAutorizar != null && this.pojoOrdenAutorizar.getId() != null && this.pojoOrdenAutorizar.getId() > 0L) {
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idOrdenCompra", this.pojoOrdenAutorizar.getId());

				// Parametros para la ejecucion del reporte
				params = new HashMap<String, String>();
				params.put("idPrograma", 	  this.entornoProperties.getString("REPORTE_ORDEN_COMPRA"));
				//params.put("nombreDocumento", this.entornoProperties.getString("REPORTE_ORDEN_COMPRA_NOMBRE"));
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);

				Respuesta respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if(respuesta.getErrores().getCodigoError() == 0L){
					nombreDocumento = "OC-" + this.pojoOrdenAutorizar.getId() + "." + respuesta.getBody().getValor("formatoReporte");
					
					this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
					this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte")); 	
					this.httpSession.setAttribute("nombreDocumento", nombreDocumento); 

					/*this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
					this.httpSession.setAttribute("nombreDocumento", respuesta.getBody().getValor("nombreDocumento"));
					this.httpSession.setAttribute("formato", this.entornoProperties.getString("REPORTE_REQUISICION_FORMATO"));*/
				} else {
					this.mensaje = respuesta.getErrores().getDescError();
				}
			}
			
			this.pojoOrdenBorrar = null;
			this.pojoOrdenAutorizar = null;
			this.pojoOrden = new OrdenCompraExt();
		} catch (Exception e) {
			this.mensaje = "Error al ejecutar reporte";
			log.error("Error en Compras.OrdenesComprasAction.reporte", e);
		}
	}

	public void recuperarListadoDetalles(boolean fromOrdenCompra) throws Exception {
		if (this.listOrdenDetalles == null)
			this.listOrdenDetalles = new ArrayList<PreOrdenDetalle>();
		this.listOrdenDetalles.clear();
		
		if (this.listOrdenDetallesEliminados == null)
			this.listOrdenDetallesEliminados = new ArrayList<PreOrdenDetalle>();
		this.listOrdenDetallesEliminados.clear();
		
		if (this.pojoOrden == null)
			return;
		
		if (fromOrdenCompra) {
			// Recuperamos los detalles de la orden de compra
			log.info("Recuperamos detalles por OC: " + this.pojoOrden.getId());
			List<OrdenCompraDetalleExt> lista = this.ifzOrdenDetalles.findExtByProperty("idOrdenCompra", this.pojoOrden.getId(), 120);
			if (lista != null && ! lista.isEmpty()) {
				for (OrdenCompraDetalleExt var : lista)
					this.listOrdenDetalles.add(new PreOrdenDetalle(var));
			}
		} else {
			if (this.pojoOrden.getIdCotizacion() == null)
				return;
			
			// Recuperamos los detalles de la cotizacion
			log.info("Recuperamos detalles por Cotizacion: " + this.pojoOrden.getIdCotizacion().getId());
			List<CotizacionDetalleExt> lista = this.ifzCotiDetalles.findExtByProperty("idCotizacion", this.pojoOrden.getIdCotizacion().getId(), 0);
			if (lista != null && ! lista.isEmpty()) {
				for (CotizacionDetalleExt var : lista) {
					if (var.getEstatus() == 1) continue;
					this.listOrdenDetalles.add(new PreOrdenDetalle(var));
				}
			}
		}

		this.seleccionarTodos = false;
		this.totalizarDetalles();
	}

	public void totalizarDetalles() {
		this.subtotal = 0;
		this.iva = 0;
		this.total = 0;
		
		if (this.listOrdenDetalles == null)
			this.listOrdenDetalles = new ArrayList<PreOrdenDetalle>();

		log.info("Totalizamos detalles.");
		for(PreOrdenDetalle var : this.listOrdenDetalles) {
			if (! var.isSeleccionado()) continue;
			this.subtotal += var.getImporte();
			this.iva += ((var.getImporte() * this.porcentajeIva) / 100);
		}
		
		if (this.listImpuestos != null && ! this.listImpuestos.isEmpty()) {
			for (SubfamiliaImpuestos imp : this.listImpuestos) {
				if (imp.getAplicaEn() == 1) // Aplica en Subtotal
					imp.setValor(new BigDecimal(this.subtotal));
				else // Aplica en Impuesto IVA
					imp.setValor(new BigDecimal(this.iva));
			}
		}
		
		this.total = this.subtotal + this.iva;
		comprobarSeleccionarTodos();
	}

	public void cargarMonedas() throws Exception {
		try {
			if (this.listMonedasItems == null)
				this.listMonedasItems = new ArrayList<SelectItem>();
			this.listMonedasItems.clear();
			
			// Cargamos la lista de monedas
			if (this.isDebug) 
				log.info("Cargando lista de monedas");
			this.listMonedas = this.ifzMonedas.findAll();
			if (this.listMonedas != null && ! this.listMonedas.isEmpty()) {
				if (this.isDebug) 
					log.info("Generando lista de items (monedas)");
				for (Moneda var : this.listMonedas) {
					if (this.idMonedaBase <= 0 && "MXN".equals(var.getAbreviacion()))
						this.idMonedaBase = var.getId();
					this.listMonedasItems.add(new SelectItem(var.getId(), var.getNombre() + " (" + var.getAbreviacion() + ")"));
				}
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.cargarMonedas(). No pude cargar el catalogo de Monedas", e);
		}
	}
	
	public boolean validacionOrden() {
		boolean valido = false;

		if (this.isDebug) log.info("Validando OC");
		if(this.listOrdenDetalles == null || this.listOrdenDetalles.isEmpty()) {
			log.info("Validando OC. ERROR 4 - Lista de productos vacia");
			control(4);
			return false;
		}
		
		for(PreOrdenDetalle var : this.listOrdenDetalles) {
			if (var.isSeleccionado()) {
				valido = true;
				break;
			}
		}
		
		if (! valido) {
			log.info("Validando OC. ERROR 5 - Ningun producto seleccionado");
			control(5);
			return false;
		}
		
		if (this.pojoOrden.getAnticipo().doubleValue() < 0) {
			log.info("Validando OC. ERROR 8 - Anticipo menor o igual a cero");
			control(8);
			return false;
		}
		
		/*if (this.pojoOrden.getTipoCambio().doubleValue() <= 0) {
    		this.operacion = true;
			this.mensaje = "ERROR 9"; // tipo de cambio menor a cero
			this.tipoMensaje = 9;
			if (this.isDebug)
				log.info("Validando OC. ERROR 9 - Tipo de cambio menor a cero");
			return false;
		}*/
		
		if (this.pojoOrden.getPlazo() <= 0) {
			log.info("Validando OC. ERROR 7 - Plazo menor o igual a cero");
			control(7);
			return false;
		}
		
		if (this.pojoOrden.getTiempoEntrega() <= 0) {
			log.info("Validando OC. ERROR 6 - Tiempo de entrega menor o igual a cero");
			control(6);
			return false;
		}
		
		return true;
	}

	private int getCountDetallesFromCotizacion(long idCotizacion) {
		List<CotizacionDetalle> lista = null;
		
		try {
			lista = this.ifzCotiDetalles.findByProperty("idCotizacion", idCotizacion, 0);
			if (lista != null && ! lista.isEmpty())
				return lista.size();
		} catch (Exception e) {
			log.error("Error al recuperar el total de detalles de la cotizacion " + idCotizacion, e);
		}
		
		return 0;
	}
	
	private void actualizaEstatusCotizacion() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<CotizacionDetalle> cotiProductos = new ArrayList<CotizacionDetalle>();
		
		try {
			// Actualizamos el estatus de la Cotizacion usada para crear esta Orden de Compra
			this.pojoOrden.getIdCotizacion().setEstatus(2);
			this.ifzCotizaciones.update(this.pojoOrden.getIdCotizacion());
			
			// Actualizo el estatus de los productos de la cotizacion elegidos para la Orden de Compra
			for (OrdenCompraDetalleExt pojoDetalle : this.listOCDetalles) {
				params.put("idCotizacion", this.pojoOrden.getIdCotizacion().getId());
				params.put("idProducto", pojoDetalle.getIdProducto().getId());
				params.put("estatus", 0);
				
				cotiProductos = this.ifzCotiDetalles.findByProperties(params);
				if (cotiProductos != null && ! cotiProductos.isEmpty()) {
					for (CotizacionDetalle det : cotiProductos) {
						det.setEstatus(1); //TODO: No contempla la cantidad usada en la OC
						this.ifzCotiDetalles.update(det);
					}
				}
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.liberarCotizacion", e);
			control(true);
		}
	}
	
	private void actualizaEstatusRequisicion() {
		List<RequisicionDetalle> reqProductos = new ArrayList<RequisicionDetalle>();
		HashMap<String, Object> params = new HashMap<String, Object>();
		int totalSeleccionados = 0;
		int totalDetalles = 0;
		
		try {
			// Actualizamos el estatus de la Requisicion usada para crear esta Orden de Compra
			if (this.pojoRequisicionSeleccionada == null || this.pojoRequisicionSeleccionada.getId() == null || this.pojoRequisicionSeleccionada.getId() <= 0L)
				this.pojoRequisicionSeleccionada = this.ifzRequisiciones.findById(this.pojoCotizacion.getIdRequisicion());
			
			reqProductos = this.ifzReqDetalles.findByProperty("idRequisicion", this.pojoRequisicionSeleccionada.getId(), 0);
			if (reqProductos != null && ! reqProductos.isEmpty())
				totalDetalles = reqProductos.size();
						
			// Actualizo el estatus de los productos de la cotizacion elegidos para la Orden de Compra
			log.info("Actualizo el estatus de los detalles de la requisicion");
			for (OrdenCompraDetalleExt var : this.listOCDetalles) {
				params.put("idRequisicion", this.pojoRequisicionSeleccionada.getId());
				params.put("idProducto", var.getIdProducto().getId());
				
				reqProductos = this.ifzReqDetalles.findByProperties(params, 0);
				if (reqProductos == null || reqProductos.isEmpty())
					continue;

				for (RequisicionDetalle det : reqProductos) {
					det.setIdCotizacion(this.pojoOrden.getIdCotizacion().getId());
					det.setCotizacionFolio(this.pojoOrden.getIdCotizacion().getFolio());
					this.ifzReqDetalles.update(det);
					totalSeleccionados += 1;
				}
			}
			
			if (totalSeleccionados >= totalDetalles && this.pojoRequisicionSeleccionada.getEstatus() != 2) {
				this.pojoRequisicionSeleccionada.setEstatus(2);
				this.ifzRequisiciones.update(this.pojoRequisicionSeleccionada);
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.actualizaEstatusRequisicion", e);
			control(true);
		}
	}
	
	private void liberarCotizacion() {
		List<OrdenCompraDetalle> ocProductos = new ArrayList<OrdenCompraDetalle>();
		List<CotizacionDetalle> cotiProductos = new ArrayList<CotizacionDetalle>();
		List<Cotizacion> listCoti = new ArrayList<Cotizacion>();
		HashMap<String, Object> params = new HashMap<String, Object>();
		Cotizacion cotizacion = null;
		int totalCotiProductos = 0;

		try {
			cotizacion = this.ifzCotizaciones.findById(this.pojoOrdenBorrar.getIdCotizacion());
			cotizacion.setEstatus(0);
			this.ifzCotizaciones.update(cotizacion);
			
			log.info("Liberando productos");
			ocProductos = this.ifzOrdenDetalles.findByProperty("idOrdenCompra", this.pojoOrdenBorrar.getId(), 0);
			if (ocProductos == null || ocProductos.isEmpty()) {
				log.info("Sin productos para liberar");
				return;
			}
			
			for (OrdenCompraDetalle var : ocProductos) {
				// Actualizamos estatus del producto en la cotizacion
				params.clear();
				params.put("idCotizacion", this.pojoOrdenBorrar.getIdCotizacion());
				params.put("idProducto", var.getIdProducto());
				cotiProductos = this.ifzCotiDetalles.findByProperties(params);
				if (cotiProductos == null || cotiProductos.isEmpty()) {
					log.info("El producto " + var.getIdProducto() + " no se pudo encontro en la cotizacion");
					continue;
				}
				
				for (CotizacionDetalle aux : cotiProductos) {
					aux.setEstatus(0); // TODO: No incluye la cantidad
					this.ifzCotiDetalles.update(aux);
				}
			}
			log.info("Productos liberados");

			log.info("Liberando productos de cotizaciones similares");
			totalCotiProductos = getCountDetallesFromCotizacion(cotizacion.getId());
			
			params.clear();
			params.put("idObra", this.pojoOrdenBorrar.getIdObra());
			params.put("idRequisicion", 0L);
			if (cotizacion.getIdRequisicion() != null && cotizacion.getIdRequisicion() != null && cotizacion.getIdRequisicion() > 0L)
				params.put("idRequisicion", cotizacion.getIdRequisicion());

			// Seleccionamos las cotizacion disponibles para la obra
			listCoti = this.ifzCotizaciones.findByProperties(params);
			if (listCoti == null || listCoti.isEmpty()) {
				log.info("Sin cotizaciones para liberar");
				return;
			}
			
			for (Cotizacion cot : listCoti) {
				// Comprobamos que no es la cotizacion que genero la orden de compra
				if (cot.getId().equals(this.pojoOrdenBorrar.getIdCotizacion())) 
					continue;
				
				// Comprobamos cantidad de productos
				if (totalCotiProductos != getCountDetallesFromCotizacion(cot.getId()))
					continue; // No tiene la misma cantidad de productos
				
				// Comprobamos cantidad de productos
				if (cot.getEstatus() == 0)
					continue; // No tiene la misma cantidad de productos
				
				// Comprobamos que no este en una orden de compra
				params.clear();
				params.put("idCotizacion", cot.getId());
				params.put("estatus", 0);
				List<OrdenCompra> listOCAux = this.ifzOrdenes.findByProperties(params, 0);
				if (listOCAux != null && ! listOCAux.isEmpty()) 
					continue; // Esta en una orden de compra
				
				for (OrdenCompraDetalle var : ocProductos) {
					// Actualizamos estatus del producto en la cotizacion
					params.clear();
					params.put("idCotizacion", cot.getId());
					params.put("idProducto", var.getIdProducto());
					cotiProductos = this.ifzCotiDetalles.findByProperties(params);
					if (cotiProductos == null || cotiProductos.isEmpty()) 
						continue;
					
					for (CotizacionDetalle aux : cotiProductos) {
						aux.setEstatus(0); // TODO: No incluye la cantidad
						this.ifzCotiDetalles.update(aux);
					}
				}
			}
			log.info("Cotizaciones similares liberadas");
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.liberarCotizacion", e);
			control(true);
		}
	}
	
	private void liberarDetalles() {
		List<CotizacionDetalle> cotiProductos = new ArrayList<CotizacionDetalle>();
		HashMap<String, Object> params = new HashMap<String, Object>();
		int totalCotiProductos = 0;
		int productosLiberados = 0;
		
		try {
			params.put("idCotizacion", this.pojoOrden.getIdCotizacion().getId());
			totalCotiProductos = getCountDetallesFromCotizacion(this.pojoOrden.getIdCotizacion().getId());
			
			for (PreOrdenDetalle var : this.listOrdenDetallesEliminados) {
				if (var.getId() != null && var.getId() > 0L) {
					// Actualizamos estatus del producto en la cotizacion
					params.put("idProducto", var.getProductoId());
					cotiProductos = this.ifzCotiDetalles.findByProperties(params);
					if (cotiProductos != null && ! cotiProductos.isEmpty()) {
						for (CotizacionDetalle aux : cotiProductos) {
							aux.setEstatus(0);
							this.ifzCotiDetalles.update(aux);
							productosLiberados += 1;
						}
					} 
				}
			}
			
			if (productosLiberados > 0 && productosLiberados == totalCotiProductos) {
				// Actualizamos estatus de cotizacion
				this.pojoOrden.getIdCotizacion().setEstatus(0);
				this.ifzCotizaciones.update(this.pojoOrden.getIdCotizacion());
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.liberarDetalles", e);
			control(true);
		}
	}
		
	public void comprobarSeleccionarTodos() {
		for (PreOrdenDetalle var : this.listOrdenDetalles) {
			this.seleccionarTodos = true;
			if (! var.isSeleccionado()) {
				this.seleccionarTodos = false;
				break;
			}
		}
	}

	private void comprobarImpuestosProducto() {
		HashMap<Long, SubfamiliaImpuestos> mapImpuestos = new HashMap<Long, SubfamiliaImpuestos>();
		List<SubfamiliaImpuestos> listImpuestosAux = new ArrayList<SubfamiliaImpuestos>();
		int impuestosEncontrados = 0;
		
		try {
			if (this.listOrdenDetalles == null || this.listOrdenDetalles.isEmpty())
				return;
			
			// Recuperamos los impuestos involucrados por los productos en su subfamilia
			for (PreOrdenDetalle var : this.listOrdenDetalles) {
				if (var.getIdProducto().getSubfamilia() == null || var.getIdProducto().getSubfamilia().getId() <= 0L)
					continue;

				log.info("Recupero los impuestos para la subfamilia " + var.getIdProducto().getSubfamilia() + " - " + var.getIdProducto().getDescSubfamilia());
				listImpuestosAux = this.ifzSubfamliaImto.findByProperty("idSubfamilia", var.getIdProducto().getSubfamilia().getId());
				if (listImpuestosAux == null|| listImpuestosAux.isEmpty()) 
					continue;
				
				for (SubfamiliaImpuestos imp : listImpuestosAux) {
					if (mapImpuestos.containsKey(imp.getId()))
						continue;
					mapImpuestos.put(imp.getId(), imp);
					this.listImpuestos.add(imp);
				}
			}

			if (this.listImpuestos != null && ! this.listImpuestos.isEmpty()) {
				impuestosEncontrados = this.listImpuestos.size();
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al verificar los impuestos de los productos a traves de la Subfamilia aplicada al producto", e);
		} finally {
			log.info(impuestosEncontrados + " Impuestos encontrados");
		}
	}
	
	private Double recuperaTipoCambioActual(long idMonedaOrigen, long idMonedaDestino) {
		Moneda monedaOrigen = null;
		Moneda monedaDestino = null;
		MonedasValores valor = null;
		Double tipoCambio = 1.0;

		try {
			if (idMonedaOrigen > 0L && idMonedaDestino > 0L) {
				// Recupero moneda base
				for (Moneda var : this.listMonedas) {
					if (var.getId() != idMonedaOrigen) 
						continue;
					monedaOrigen = var;
					break;
				}
				
				// Recupero moneda destino
				for (Moneda var : this.listMonedas) {
					if (var.getId() != idMonedaDestino) 
						continue;
					monedaDestino = var;
					break;
				}
				
				// COmpruebo monedas y recupero el tipo de cambio actual si corresponde
				if (monedaOrigen != null && monedaDestino != null) {
					valor = this.ifzMonValores.findActual(monedaOrigen, monedaDestino);
					tipoCambio = valor.getValor().doubleValue();
				}
			}
		} catch (Exception e) {
			log.error("No se pudo obtener el Tipo de Cambio actual para la conversion de " + monedaOrigen + " a " + monedaDestino, e);
			tipoCambio = 1.0;
		}
		
		return tipoCambio;
	}
	
	public void evaluaConversion() {
		this.evaluaConversion = false;
		if (this.idMonedaActual != this.pojoOrden.getIdMoneda().longValue())
			this.evaluaConversion = true;
	}
	
	public void evaluaTipoCambio() {
		this.reConvierte = false;
		this.evaluaConversion = false;
		if (this.tipoCambioActual != this.pojoOrden.getTipoCambio().doubleValue()) {
			this.evaluaConversion = true;
			this.reConvierte = true;
		}
	}
	
	public void conversion() {
		Moneda monOrigen = null;
		Moneda monDestino = null;
		double tipoCambio = 0.0;
		double monto = 0;
		
		try {
			control();
			if (this.reConvierte) {
				log.info("Obteniendo Moneda Origen ... ");
				monOrigen = getMonedaById(this.idMonedaActual);
				if (monOrigen == null) {
					log.info("No pude obtener la Moneda Base para la conversion ... ID " + this.idMonedaActual);
					control("No pude obtener la Moneda Base para la conversion");
					return;
				}
				
				log.info("Obteniendo Moneda Destino ... ");
				monDestino = getMonedaById(this.idMonedaOriginal);
				if (monDestino == null) {
					log.info("No pude obtener la Moneda Destino para la conversion ... ID " + this.pojoOrden.getIdMoneda());
					control("No pude obtener la Moneda Destino para la conversion");
					return;
				}
				
				tipoCambio = this.tipoCambioActual;
			} else {
				log.info("Obteniendo Moneda Origen ... ");
				monOrigen = getMonedaById(this.idMonedaActual);
				if (monOrigen == null) {
					log.info("No pude obtener la Moneda Base para la conversion ... ID " + this.idMonedaActual);
					control("No pude obtener la Moneda Base para la conversion");
					return;
				}
				
				log.info("Obteniendo Moneda Destino ... ");
				monDestino = getMonedaById(this.pojoOrden.getIdMoneda());
				if (monDestino == null) {
					log.info("No pude obtener la Moneda Destino para la conversion ... ID " + this.pojoOrden.getIdMoneda());
					control("No pude obtener la Moneda Destino para la conversion");
					return;
				}

				log.info("Obteniendo Tipo de Cambio ... ");
				if (this.pojoOrden.getTipoCambio().doubleValue() > 1) {
					tipoCambio = this.pojoOrden.getTipoCambio().doubleValue();
				} else {
					log.info("Obteniendo Tipo de Cambio desde Tesoreria ... ");
					if (this.idMonedaBase == monDestino.getId())
						tipoCambio = getTipoCambio(monDestino, monOrigen);
					else
						tipoCambio = getTipoCambio(monOrigen, monDestino);
					if (tipoCambio <= 0) {
						this.pojoOrden.setIdMoneda(this.idMonedaActual);
						control("No puedo convertir " + monOrigen.getNombre() + " a " + monDestino.getNombre() + ".\nNo se pudo recuperar el Tipo de Cambio");
						return;
					}
					
					// Actualizo en la Orden el tipo de cambio
					this.pojoOrden.setTipoCambio(new BigDecimal(tipoCambio));
				}
			}
			
			log.info("Tipo de Cambio obtenido :) ... " + tipoCambio);
			log.info("Realizando Conversion para productos | " + monOrigen.getAbreviacion() + " < " + monDestino.getAbreviacion() + " ... ");
			for(PreOrdenDetalle var : this.listOrdenDetalles) {
				if (this.idMonedaBase == monDestino.getId())
					monto = var.getPrecioUnitario() * tipoCambio;
				else
					monto = var.getPrecioUnitario() / tipoCambio;
				var.setPrecioUnitario(monto);
				
				monto = monto * var.getCantidad();
				var.setImporte(monto);
			}
			

			if (this.reConvierte) {
				this.idMonedaActual = this.idMonedaOriginal;
				this.reConvierte = false;
				conversion();
			} else {
				// Reseteamos el Tipo de Cambio si la Moneda actual de la Orden es la misma que la Moneda base del Sistema
				if (this.idMonedaBase == this.pojoOrden.getIdMoneda())
					this.pojoOrden.setTipoCambio(new BigDecimal(1));
				
				// Respaldamos los valores actuales de la Orden y totalizamos
				this.idMonedaActual = this.pojoOrden.getIdMoneda();
				this.tipoCambioActual = this.pojoOrden.getTipoCambio().doubleValue();
				totalizarDetalles();
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.conversion(). No pude realizar la conversion :( ", e);
			control(true);
		} finally {
			this.evaluaConversion = false;
		}
	}
	
	private Moneda getMonedaById(long idMoneda) {
		Moneda resultado = null;
		
		try {
			for (Moneda var : this.listMonedas) {
				if (var.getId() != idMoneda)
					continue;
				resultado = var;
				break;
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.getMonedaById(). No se pudo obtener la moneda " + idMoneda, e);
		}
		
		return resultado;
	}
	
	private String getMonedaAbreviacion(long idMoneda) {
		String resultado = null;
		
		try {
			for (Moneda var : this.listMonedas) {
				if (var.getId() != idMoneda)
					continue;
				resultado = var.getAbreviacion();
				break;
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.getMonedaById(). No se pudo obtener la moneda " + idMoneda, e);
		}
		
		return resultado;
	}
	
	private double getTipoCambio(Moneda monedaOrigen, Moneda monedaDestino) {
		MonedasValores valor = null;
		Double tipoCambio = 0.0;
		
		try {
			if (monedaOrigen == null || monedaDestino == null) {
				log.warn("Una o ambas Monedas involucradas nulas");
				return tipoCambio;
			}
			
			valor = this.ifzMonValores.findActual(monedaOrigen, monedaDestino);
			tipoCambio = valor.getValor().doubleValue();
			if (tipoCambio == null || tipoCambio <= 0.0) {
				log.warn("No se pudo recuperar el tipo de cambio para " + monedaOrigen.getAbreviacion() + " a " + monedaDestino.getAbreviacion());
				tipoCambio = 0.0;
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.getTipoCambio(). No se pudo obtener el Tipo de Cambio actual para la conversion de " + monedaOrigen.getAbreviacion() + " a " + monedaDestino.getAbreviacion(), e);
			tipoCambio = 0.0;
		}
		
		return tipoCambio;
	}

	private void control() { 
		this.operacion = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(boolean value) {
		if (value)
			control(value, 1, "ERROR");
		else
			control(value, 0, null);
	}
	
	private void control(int value) { 
		if (value <= 1)
			control(true, 1, "ERROR");
		else
			control(true, value, "ERROR"); 
	}
	
	private void control(String value) { 
		if (value == null || "".equals(value.trim()))
			control(true, 1, "ERROR");
		else
			control(true, -1, value); 
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "": mensaje.replace("\n", "<br/>");
	}

	// BUSQUEDA COTIZACIONES
	// -------------------------------------------------------------------------
	public void nuevaBusquedaCotizaciones() {
		this.campoBusquedaCotizaciones = this.tiposBusquedaCotizaciones.get(0).getDescription();
		this.valorBusquedaCotizaciones = "";
		
		this.pojoCotizacion = new Cotizacion();
		if (this.listCotizaciones != null)
			this.listCotizaciones.clear();
	}
		
	public void buscarCotizaciones() throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		
		try {
			control();
			if ("".equals(this.campoBusquedaCotizaciones))
				this.campoBusquedaCotizaciones = this.tiposBusquedaCotizaciones.get(0).getDescription();
			
			if (this.isDebug) log.info("Busqueda de Cotizaciones. " + this.campoBusquedaCotizaciones + ": " + this.valorBusquedaCotizaciones);
			params.put("idObra", this.pojoObra.getId().toString());
			params.put(this.campoBusquedaCotizaciones, this.valorBusquedaCotizaciones);
			if (this.tipoMaestro == TipoMaestro.Administrativo)
				params.put("tipo", String.valueOf(this.tipoMaestro.ordinal()));
			params.put("estatus", "0");
			this.ifzCotizaciones.OrderBy("CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.id desc");
			this.listCotizaciones = this.ifzCotizaciones.findLikeProperties(params);
			if(this.listCotizaciones == null || this.listCotizaciones.isEmpty()) {
				log.info("Buscando Cotizaciones.  ERROR 2 - Busqueda sin resultados");
				control(2);
				return;
			}

			/*if (this.isDebug) log.info("Busqueda de Cotizaciones. " + this.campoBusquedaCotizaciones + ": " + this.valorBusquedaCotizaciones);
			this.ifzCotizaciones.estatus(0);
			this.ifzCotizaciones.OrderBy("CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.id desc");
			this.listCotizaciones = this.ifzCotizaciones.findLikePropertyWithObra(this.campoBusquedaCotizaciones, this.valorBusquedaCotizaciones, this.pojoObra.getId(), 0);
			*/
			if(this.listCotizaciones.isEmpty()) {
				if (this.isDebug) log.info("Busqueda de Cotizaciones. ERROR 2 - Busqueda sin resultados");
				control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listCotizaciones != null) this.listCotizaciones.clear();
			log.error("Error en Compras.OrdenesComprasAction.buscarCotizaciones", e);
			control(true);
    	}
	}

	public void seleccionarCotizacion() throws Exception {
		try {
			control();
			if (this.pojoCotizacion == null)
				return;
			
			this.pojoOrden.setIdCotizacion(this.ifzCotizaciones.findExtById(this.pojoCotizacion.getId()));
			if (this.pojoObra == null) {
				this.pojoObra = this.pojoOrden.getIdCotizacion().getIdObra().Copia();
				this.pojoOrden.setIdObra(this.pojoObra);
			}
			
			this.pojoOrden.setTiempoEntrega(this.pojoCotizacion.getTiempoEntrega());
			this.pojoOrden.setLugarEntrega(this.pojoObra.getNombre());
			this.pojoOrden.setIdMoneda(this.pojoCotizacion.getIdMoneda());
			this.idMonedaActual = this.pojoCotizacion.getIdMoneda();
			this.idMonedaOriginal = this.pojoCotizacion.getIdMoneda();

			// detalles desde la cotizacion 
			this.recuperarListadoDetalles(false);
			this.comprobarImpuestosProducto();
			this.nuevaBusquedaCotizaciones();
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.seleccionarCotizacion", e);
			control(true);
		}
	}

	// BUSQUEDA PROVEEDORES
	// --------------------------------------------------------------------------------------	
	public void nuevaBusquedaProveedores() {
		this.campoBusquedaProveedores = this.tiposBusquedaProveedores.get(0).getValue().toString();
		this.valorBusquedaProveedores = "";
		this.valorBusquedaTipoProveedor = "N";
		this.numPaginaProveedores = 1;
		
		if (this.listProveedores == null) this.listProveedores = new ArrayList<PersonaExt>();
		this.listProveedores.clear();
		if (this.pojoProveedor == null)
			this.pojoProveedor = new PersonaExt();
    }

	public void buscarProveedores() throws Exception {
		try {
			control();
			if ("".equals(this.valorBusquedaTipoProveedor))
				this.valorBusquedaTipoProveedor = "N";
			
			if ("".equals(this.campoBusquedaProveedores))
				this.valorBusquedaProveedores = "id";

			if (this.isDebug) log.info("Buscando obras. " + this.campoBusquedaProveedores + ":" + this.valorBusquedaProveedores);
			this.listProveedores = this.ifzCotizaciones.findPersonaLikeProperty(this.campoBusquedaProveedores, this.valorBusquedaProveedores, this.valorBusquedaTipoProveedor, 80);
			if(this.listProveedores.isEmpty()){
				control(2);
				return;
			}
    	} catch (Exception e) {
			control(true);
			if (this.listProveedores != null) this.listProveedores.clear();
    		log.error("Error en Compras.CotizacionAction.buscarProveedores", e);
    	}
	}

	public void seleccionarProveedor() throws Exception {
		try {
			control();
			if (this.pojoProveedor == null) {
				log.info("El proveedor no pudo ser asignado, pojo nulo");
				control("ERROR INTERNO - Ocurrio un problema al recuperar el Proveedor seleccionado");
				return;
			}
			
			// Asigno el PROVEEDOR seleccionada y el tipo de persona de proveedor (Persona (P) o Negocio (N)) a la cotizacion
			this.pojoCotizacion.setIdProveedor(this.pojoProveedor.getId());
			this.pojoCotizacion.setNombreProveedor(this.pojoProveedor.getNombre());
			this.pojoCotizacion.setTipoPersonaProveedor(this.valorBusquedaTipoProveedor);
			
			// Recuperamos el contacto del proveedor seleccionado
			if (this.isDebug) log.info("Recuperando contacto para proveedor: " + this.pojoProveedor.getId() + " - " + this.pojoProveedor.getNombre());
			this.pojoContacto = this.ifzCotizaciones.findContactoByProveedor(this.pojoProveedor, this.valorBusquedaTipoProveedor);
			if (this.pojoContacto != null) {
				this.pojoCotizacion.setIdContacto(this.pojoContacto.getId());
				this.pojoCotizacion.setNombreContacto(this.pojoContacto.getNombre());
			} else {
				log.info("El Proveedor no tiene asignado ningun Contacto");
			}
			
			nuevaBusquedaProveedores();
    	} catch (Exception e) {
			control(true);
    		log.error("Error en Compras.CotizacionAction.seleccionarProveedor", e);
    	}
	}
	
	// REQUISICIONES
	// --------------------------------------------------------------------------------------	
	public void nuevaBusquedaRequisiciones() {
		this.campoBusquedaRequisiciones = this.tiposBusquedaRequisiciones.get(0).getValue().toString();
		this.valorBusquedaRequisiciones = "";
		this.numPaginaRequisiciones = 1;
		
		if (this.listRequisiciones == null)
			this.listRequisiciones = new ArrayList<Requisicion>();
		this.listRequisiciones.clear();
		this.pojoRequisicion = new Requisicion();
	}
	
	public void buscarRequisiciones() {
		HashMap<String, String> params = new HashMap<String, String>();
		
		try {
			control();
			if ("".equals(this.campoBusquedaRequisiciones))
				this.campoBusquedaRequisiciones = this.tiposBusquedaRequisiciones.get(0).getValue().toString();
			
			params.put(this.campoBusquedaRequisiciones, this.valorBusquedaRequisiciones);
			params.put("idObra", this.pojoObra.getId().toString());
			if (this.tipoMaestro == TipoMaestro.Administrativo)
				params.put("tipo", String.valueOf(this.tipoMaestro.ordinal()));

			this.ifzRequisiciones.OrderBy("CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.id desc");
			this.listRequisiciones = this.ifzRequisiciones.findLikeProperties(params, 0);
			if(this.listRequisiciones.isEmpty()) {
				if (this.isDebug) log.error("ERROR 2 - La busqueda no regreso resultados");
				control(2);
				return;
			}
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.buscarRequisiciones", e);
			control(true);
    	}
	}
	
	public void seleccionarRequisicion() {
		List<RequisicionDetalleExt> listReqDetalles = new ArrayList<RequisicionDetalleExt>();
		double precioUnitario = 0;
		double tipoCambio = 0;
		double monto = 0;
		
		try {
			control();
			// Comprobamos requisicion
			log.info("Comprobamos Requisicion");
			if (this.pojoRequisicion == null || this.pojoRequisicion.getId() == null || this.pojoRequisicion.getId() <= 0L) {
				log.error("ERROR 2 - Debe seleccionar una requisicion");
				control(2);
				return;
			}
			
			// Recuperamos el detalle de la requisicion
			log.info("Recuperamos detalles de Requisicion");
			listReqDetalles = this.ifzReqDetalles.findExtByProperty("idRequisicion", this.pojoRequisicion.getId(), 0);
			if (listReqDetalles == null || listReqDetalles.isEmpty()) {
				log.info("ERROR INTERNO - La Requisicion no tiene productos o ya han sido usados en cotizaciones");
				control("La Requisicion no tiene productos disponibles");
				return;
			}
			log.info(listReqDetalles.size() + " detalles de Requisicion recuperados");
			
			// Genero cotizacion
			log.info("Genero cotizacion a partir de la requisicion seleccionada");
			if (this.pojoCotizacion == null)
				this.pojoCotizacion = new Cotizacion();
			this.pojoCotizacion.setId(0L);
			this.pojoCotizacion.setIdRequisicion(this.pojoRequisicion.getId());
			this.pojoCotizacion.setIdObra(this.pojoObra.getId());
			this.pojoCotizacion.setNombreObra(this.pojoObra.getNombre());
			this.pojoCotizacion.setIdComprador(this.pojoRequisicion.getIdSolicita());
			this.pojoCotizacion.setNombreComprador(this.pojoRequisicion.getNombreSolicita());
			
			if (this.pojoProveedor != null && this.pojoProveedor.getId() > 0L) {
				this.pojoCotizacion.setIdProveedor(this.pojoProveedor.getId());
				this.pojoCotizacion.setNombreProveedor(this.pojoProveedor.getNombre());
				this.pojoCotizacion.setTipoPersonaProveedor(this.valorBusquedaTipoProveedor);
			}
			
			if (this.pojoContacto != null && this.pojoContacto.getId() > 0L) {
				this.pojoCotizacion.setIdContacto(this.pojoContacto.getId());
				this.pojoCotizacion.setNombreContacto(this.pojoContacto.getNombre());
			}

			this.pojoCotizacion.setIdMoneda(this.pojoRequisicion.getIdMoneda());
			this.pojoCotizacion.setIva(0D);
			this.pojoCotizacion.setCreadoPor(this.usuarioId);
			this.pojoCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoCotizacion.setModificadoPor(this.usuarioId);
			this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoCotizacion.setTipo(this.pojoRequisicion.getTipo());
			this.idMonedaActual = this.pojoCotizacion.getIdMoneda();
			this.idMonedaOriginal = this.pojoCotizacion.getIdMoneda();

			// Genero detalles de cotizacion
			log.info("Genero detalles de cotizacion a partir de la requisicion seleccionada");
			if (this.listCotDetalles == null)
				this.listCotDetalles = new ArrayList<CotizacionDetalleExt>();
			this.listCotDetalles.clear();

			log.info("Genero detalles para Cotizacion.");
			CotizacionDetalleExt cdet = null;
			for(RequisicionDetalleExt var : listReqDetalles) {
				// Comprueba si la cotizacion es de una moneda diferente a PESOS
				if (this.idMonedaBase == this.pojoCotizacion.getIdMoneda() && this.idMonedaBase != var.getIdProducto().getIdMoneda().getId()) {
					precioUnitario = var.getIdProducto().getPrecioCompra();
					tipoCambio = var.getIdProducto().getTipoCambio();
					if (tipoCambio <= 1)
						tipoCambio = recuperaTipoCambioActual(this.pojoCotizacion.getIdMoneda(), var.getIdProducto().getIdMoneda().getId());
					precioUnitario = precioUnitario * tipoCambio;
					monto = var.getCantidad() * precioUnitario;
				} else if (this.idMonedaBase != this.pojoCotizacion.getIdMoneda() && this.idMonedaBase == var.getIdProducto().getIdMoneda().getId()) {
					precioUnitario = var.getIdProducto().getPrecioCompra();
					tipoCambio = var.getIdProducto().getTipoCambio();
					if (tipoCambio <= 1)
						tipoCambio = recuperaTipoCambioActual(var.getIdProducto().getIdMoneda().getId(), this.pojoCotizacion.getIdMoneda());
					precioUnitario = precioUnitario / tipoCambio;
					monto = var.getCantidad() * precioUnitario;
				} else {
					precioUnitario = var.getIdProducto().getPrecioCompra();
					monto = var.getCantidad() * precioUnitario;
				}
				
				cdet = new CotizacionDetalleExt();
				cdet.setCantidad(var.getCantidad());
				cdet.setCotizar(var.getCantidad());
				cdet.setIdProducto(var.getIdProducto());
				cdet.setPrecioUnitario(precioUnitario);
				cdet.setImporte(monto);
				cdet.setMargen(0);
				cdet.setCreadoPor(this.usuarioId);
				cdet.setFechaCreacion(Calendar.getInstance().getTime());
				cdet.setModificadoPor(this.usuarioId);
				cdet.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.listCotDetalles.add(cdet);
			}
			log.info(this.listCotDetalles.size() + " detalles de Cotizacion generados.");

			log.info("Genero detalles para Orden de Compra.");
			if (this.listOrdenDetalles == null) this.listOrdenDetalles = new ArrayList<PreOrdenDetalle>();
			this.listOrdenDetalles.clear();
			this.seleccionarTodos = true;
			PreOrdenDetalle det = null;
			for (CotizacionDetalleExt var : this.listCotDetalles) {
				if (var.getEstatus() == 1) continue;
				det = new PreOrdenDetalle(var);
				det.setSeleccionado(true);
				this.listOrdenDetalles.add(det);
			}

			comprobarImpuestosProducto();
			totalizarDetalles();

			this.pojoCotizacion.setSubtotal(this.subtotal);
			this.pojoCotizacion.setIva(this.iva);
			this.pojoCotizacion.setTotal(this.total);
			log.info(this.listOrdenDetalles.size() + " detalles de Orden de Compra generados.");
			
			this.pojoOrden.setIdMoneda(this.pojoRequisicion.getIdMoneda());
			this.pojoOrden.setTipo(this.pojoRequisicion.getTipo());
			this.pojoRequisicionSeleccionada = copiaRequisicion(this.pojoRequisicion);

			this.nuevaBusquedaRequisiciones();
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.reqSeleccionar", e);
			control(true);
    	}
	}
	
	private Requisicion copiaRequisicion(Requisicion pojoTarget) {
		Requisicion pojoResult = new Requisicion();
		
		try {
			BeanUtils.copyProperties(pojoResult, pojoTarget);
		} catch (Exception e) {
			pojoResult = new Requisicion();
			pojoResult.setId(pojoTarget.getId());
			pojoResult.setIdObra(pojoTarget.getIdObra());
			pojoResult.setNombreObra(pojoTarget.getNombreObra());
			pojoResult.setIdSolicita(pojoTarget.getIdSolicita());
			pojoResult.setNombreSolicita(pojoTarget.getNombreSolicita());
			pojoResult.setFecha(pojoTarget.getFecha());
			pojoResult.setTipo(pojoTarget.getTipo());
			pojoResult.setIdMoneda(pojoTarget.getIdMoneda());
			pojoResult.setAutorizado(pojoTarget.getAutorizado());
			pojoResult.setIdUsuarioAutorizo(pojoTarget.getIdUsuarioAutorizo());
			pojoResult.setFechaAutorizacion(pojoTarget.getFechaAutorizacion());
			pojoResult.setEstatus(pojoTarget.getEstatus());
			pojoResult.setCreadoPor(pojoTarget.getCreadoPor());
			pojoResult.setFechaCreacion(pojoTarget.getFechaCreacion());
			pojoResult.setModificadoPor(pojoTarget.getModificadoPor());
			pojoResult.setFechaModificacion(pojoTarget.getFechaModificacion());
		}
		
		return pojoResult;
	}
	
	// BUSQUEDA OBRAS
	// -------------------------------------------------------------------------
	public void nuevaBusquedaObras() {
    	this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
		this.valorBusquedaObras = "";
		this.valorBusquedaTipoObra = 0;
		
		if (this.listObras != null)
			this.listObras.clear();
    }
	
	public void buscarObras() throws Exception {
		try {
			control();
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();

			if (this.isDebug) log.info("Busqueda de Obras. " + this.campoBusquedaObras + ": " + this.valorBusquedaObras);
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikePropertyExt(this.campoBusquedaObras, this.valorBusquedaObras, this.valorBusquedaTipoObra);
			if(this.listObras.isEmpty()) {
				if (this.isDebug) log.info("Busqueda de Obras. ERROR 2 - Busqueda sin resultados");
				control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listObras != null) this.listObras.clear();
    		log.error("Error en buscarObras", e);
			control(true);
    	}
	}

	public void seleccionarObra() throws Exception {
		try {
			control();
			// Recuperamos los insumos de la obra seleccionada
			nuevaBusquedaObras();
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.seleccionarObra", e);
			control(true);
		}
	}

	// BUSQUEDA EMPLEADOS
	// -------------------------------------------------------------------------
	public void nuevaBusquedaEmpleados() {
		this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getDescription();
		this.valorBusquedaEmpleados = "";
		
		this.pojoEmpleado = null;
		if (this.listEmpleados != null)
			this.listEmpleados.clear();
	}
	
	public void buscarEmpleados() throws Exception {
		try {
			control();
			this.numPaginaEmpleados = 1;
			if ("".equals(this.campoBusquedaEmpleados))
				this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getDescription();

			log.info("Busqueda de Empleados. " + this.campoBusquedaEmpleados + ": " + this.valorBusquedaEmpleados);
			this.listEmpleados = this.ifzEmpleados.findLikeProperty(this.campoBusquedaEmpleados, this.valorBusquedaEmpleados, 0);
			if (this.listEmpleados.isEmpty()) {
				log.info("Busqueda de Empleados. ERROR 2 - Busqueda sin resultados");
				control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listEmpleados != null) this.listEmpleados.clear();
    		log.error("Error en buscarObras", e);
			control(true);
    	}
	}

	public void seleccionarEmpleado() throws Exception {
		try {
			control();
			if (this.pojoEmpleado != null)
				this.pojoOrden.setIdSolicita(this.ifzEmpleados.convertir(this.pojoEmpleado));
			nuevaBusquedaEmpleados();
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.seleccionarEmpleado", e);
			control(true);
		}
	}
	
	// --------------------------------------------------------------------------------------
    // Propiedades 
    // --------------------------------------------------------------------------------------

	public String getOrdenCompraTitulo() {
		if (this.pojoOrden != null && this.pojoOrden.getId() != null && this.pojoOrden.getId() > 0L)
			return (this.tipoMaestro == TipoMaestro.Administrativo ? "ADMINISTRATIVA " : "") + this.pojoOrden.getId();
		return (this.tipoMaestro == TipoMaestro.Administrativo ? "ADMINISTRATIVA" : "");
	}
	
	public void setOrdenCompraTitulo(String value) {}
	
	public boolean getMaestroAdministrativo() {
		return (this.esAdministrativo && this.tipoMaestro == TipoMaestro.Administrativo);
	}
	
	public void setMaestroAdministrativo(boolean value) {}
	
	public String getOrdenCompraObra() {
		if (this.pojoObra != null)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		return "";
	}
	
	public void setOrdenCompraObra(String value) {}
	
	public String getOrdenCompraCotizacion() {
		if (this.pojoOrden != null) {
			if (! this.origenRequisicion) {
				if (this.pojoOrden.getIdCotizacion() != null) {
					if (this.pojoOrden.getIdCotizacion().getId() != null && this.pojoOrden.getIdCotizacion().getId() > 0L)
						return this.pojoOrden.getIdCotizacion().getId() 
								+ " - " + this.pojoOrden.getIdCotizacion().getFolio() 
								+ " - " + (new DecimalFormat(this.patternDecimal)).format(this.pojoOrden.getIdCotizacion().getTotal()) 
								+ " " + getMonedaAbreviacion(this.pojoOrden.getIdCotizacion().getIdMoneda());
				}
			} else {
				if (this.pojoCotizacion != null && this.pojoCotizacion.getId() != null)
					return " Por guardar - " + (new DecimalFormat(this.patternDecimal)).format(this.pojoCotizacion.getTotal());
			}
		}
		
		return "";
	}
	
	public void setOrdenCompraCotizacion(String value) {}

	public String getOrdenCompraRequisicion() {
		if (this.pojoOrden != null && this.origenRequisicion && this.pojoRequisicionSeleccionada != null) {
			if (this.pojoRequisicionSeleccionada.getId() != null && this.pojoRequisicionSeleccionada.getId() > 0L)
				return this.pojoRequisicionSeleccionada.getId() + " por " + this.pojoRequisicionSeleccionada.getNombreSolicita();
		}
		
		return "";
	}
	
	public void setOrdenCompraRequisicion(String value) {}
	
	public String getCotizacionProveedor() {
		if (this.pojoOrden != null) {
			if (! this.origenRequisicion) {
				if (this.pojoOrden.getIdCotizacion() != null) {
					if (this.pojoOrden.getIdCotizacion().getIdProveedor() == null)
						return "Sin proveedor";
					return this.pojoOrden.getIdCotizacion().getIdProveedor().getId() + " - " + this.pojoOrden.getIdCotizacion().getProveedorNombre();
				}
			} else {
				if (this.pojoProveedor != null && this.pojoProveedor.getId() > 0L)
					return this.pojoProveedor.getId() + " - " + this.pojoProveedor.getNombre();
			}
		}
		
		return "";
	}
	
	public void setCotizacionProveedor(String value) {}

	public String getCotizacionContacto() {
		if (this.pojoOrden != null) {
			if (! this.origenRequisicion) {
				if (this.pojoOrden.getIdCotizacion() != null) {
					if (this.pojoOrden.getIdCotizacion().getIdContacto() == null)
						return "Sin contacto";
					return this.pojoOrden.getIdCotizacion().getIdContacto().getId() + " - " + this.pojoOrden.getIdCotizacion().getContactoNombre();
				}
			} else {
				if (this.pojoContacto != null && this.pojoContacto.getId() > 0L)
					return this.pojoContacto.getId() + " - " + this.pojoContacto.getNombre();
			}
		}
		
		return "";
	}
	
	public void setCotizacionContacto(String value) {}
	
	public String getOrdenCompraSolicita() {
		if (this.pojoOrden != null) {
			if (this.pojoOrden.getIdSolicita() != null) {
				if (this.pojoOrden.getIdSolicita().getId() != null && this.pojoOrden.getIdSolicita().getId() > 0L)
					return this.pojoOrden.getIdSolicita().getId() + " - " + this.pojoOrden.getSolicitaNombre();
			}
		}
		
		return "";
	}
	
	public void setOrdenCompraSolicita(String value) {}
	
	public Obra getObraMain() {
		try {
			if (this.pojoObra != null)
				return this.ifzObras.convertir(this.pojoObra);
		} catch (Exception e) {
			log.error("No puedo convertir el pojo extendido a Obra", e);
		}
		
		return new Obra();
	}
	
	public void setObraMain(Obra pojoObra) {
		try {
			if (pojoObra != null) {
				log.info("Extendiendo pojo Obra");
				this.pojoObra = this.ifzObras.convertir(pojoObra);
			}
		} catch (Exception e) {
			log.error("No puedo extender el pojo Obra", e);
		}
	}

	public ObraExt getPojoObra() {
		return pojoObra;
	}
	
	public void setPojoObra(ObraExt pojoObra) {
		this.pojoObra = pojoObra;
	}
	
	public List<Obra> getListObrasGrid() {
		return listObrasGrid;
	}
	
	public void setListObrasGrid(List<Obra> listObrasGrid) {
		this.listObrasGrid = listObrasGrid;
	}
	
	public List<ObraExt> getListObras() {
		return listObras;
	}
	
	public void setListObras(List<ObraExt> listObras) {
		this.listObras = listObras;
	}
	
	public List<OrdenCompra> getListOrdenes() {
		return listOrdenes;
	}
	
	public void setListOrdenes(List<OrdenCompra> listOrdenes) {
		this.listOrdenes = listOrdenes;
	}
	
	public List<PreOrdenDetalle> getListOrdenDetalles() {
		return listOrdenDetalles;
	}
	
	public void setListOrdenDetalles(List<PreOrdenDetalle> listOrdenDetalles) {
		this.listOrdenDetalles = listOrdenDetalles;
	}
	
	public List<PreOrdenDetalle> getListOrdenDetallesEliminados() {
		return listOrdenDetallesEliminados;
	}
	
	public void setListOrdenDetallesEliminados(List<PreOrdenDetalle> listOrdenDetallesEliminados) {
		this.listOrdenDetallesEliminados = listOrdenDetallesEliminados;
	}
	
	public OrdenCompraExt getPojoOrden() {
		return pojoOrden;
	}

	public OrdenCompra getPojoOrdenMain() {
		try {
			if (this.pojoOrden != null)
				return this.ifzOrdenes.findById(this.pojoOrden.getId());
		} catch (Exception e) {
			log.error("No puedo convertir el pojo extendido a OrdenCompra", e);
		}
		
		return new OrdenCompra();
	}
	
	public void setPojoOrdenMain(OrdenCompra pojoOrden) {
		try {
			if (pojoOrden != null) {
				log.info("Extendiendo pojo OrdenCompra");
				this.pojoOrden = this.ifzOrdenes.findExtById(pojoOrden.getId());
			}
		} catch (Exception e) {
			log.error("No puedo extender el pojo OrdenCompra", e);
		}
	}

	public void setPojoOrden(OrdenCompraExt pojoOrden) {
		this.pojoOrden = pojoOrden;
	}
	
	public OrdenCompra getPojoOrdenBorrar() {
		return pojoOrdenBorrar;
	}
	
	public void setPojoOrdenBorrar(OrdenCompra pojoOrdenBorrar) {
		this.pojoOrdenBorrar = pojoOrdenBorrar;
	}
	
	public PreOrdenDetalle getPojoOrdenDetalle() {
		return pojoOrdenDetalle;
	}
	
	public void setPojoOrdenDetalle(PreOrdenDetalle pojoOrdenDetalle) {
		this.pojoOrdenDetalle = pojoOrdenDetalle;
	}
	
	public PreOrdenDetalle getPojoOrdenDetalleBorrar() {
		return pojoOrdenDetalleBorrar;
	}
	
	public void setPojoOrdenDetalleBorrar(PreOrdenDetalle pojoOrdenDetalleBorrar) {
		this.pojoOrdenDetalleBorrar = pojoOrdenDetalleBorrar;
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
	
	public int getValorBusquedaTipoObra() {
		return valorBusquedaTipoObra;
	}
	
	public void setValorBusquedaTipoObra(int valorBusquedaTipoObra) {
		this.valorBusquedaTipoObra = valorBusquedaTipoObra;
	}
	
	public boolean isOperacion() {
		return operacion;
	}
	
	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public int getTipoMensaje() {
		return tipoMensaje;
	}
	
	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}
	
	public int getNumPaginaOrdenes() {
		return numPaginaOrdenes;
	}
	
	public void setNumPaginaOrdenes(int numPaginaOrdenes) {
		this.numPaginaOrdenes = numPaginaOrdenes;
	}
	
	public int getNumPaginaObras() {
		return numPaginaObras;
	}
	
	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}
	
	public int getNumPaginaDetalles() {
		return numPaginaDetalles;
	}
	
	public void setNumPaginaDetalles(int numPaginaDetalles) {
		this.numPaginaDetalles = numPaginaDetalles;
	}
	
	public double getSubtotal() {
		return subtotal;
	}
	
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	
	public double getIva() {
		return iva;
	}
	
	public void setIva(double iva) {
		this.iva = iva;
	}
	
	public double getTotal() {
		return total;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	
	public List<SelectItem> getTiposBusquedaCotizaciones() {
		return tiposBusquedaCotizaciones;
	}
	
	public void setTiposBusquedaCotizaciones(List<SelectItem> tiposBusquedaCotizaciones) {
		this.tiposBusquedaCotizaciones = tiposBusquedaCotizaciones;
	}
	
	public String getCampoBusquedaCotizaciones() {
		return campoBusquedaCotizaciones;
	}
	
	public void setCampoBusquedaCotizaciones(String campoBusquedaCotizaciones) {
		this.campoBusquedaCotizaciones = campoBusquedaCotizaciones;
	}
	
	public String getValorBusquedaCotizaciones() {
		return valorBusquedaCotizaciones;
	}
	
	public void setValorBusquedaCotizaciones(String valorBusquedaCotizaciones) {
		this.valorBusquedaCotizaciones = valorBusquedaCotizaciones;
	}

	public List<Cotizacion> getListCotizaciones() {
		return listCotizaciones;
	}

	public void setListCotizaciones(List<Cotizacion> listCotizaciones) {
		this.listCotizaciones = listCotizaciones;
	}

	public int getNumPaginaCotizaciones() {
		return numPaginaCotizaciones;
	}

	public void setNumPaginaCotizaciones(int numPaginaCotizaciones) {
		this.numPaginaCotizaciones = numPaginaCotizaciones;
	}
	
	public List<SelectItem> getListMonedasItems() {
		return listMonedasItems;
	}

	public void setListMonedasItems(List<SelectItem> listMonedasItems) {
		this.listMonedasItems = listMonedasItems;
	}

	public boolean getSeleccionarTodos() {
		return seleccionarTodos;
	}

	public void setSeleccionarTodos(boolean seleccionarTodos) {
		this.seleccionarTodos = seleccionarTodos;

		if (! this.seleccionarTodos) return;
		for (PreOrdenDetalle var : this.listOrdenDetalles) {
			var.setSeleccionado(seleccionarTodos);
		}
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
	
	public List<Empleado> getListEmpleados() {
		return listEmpleados;
	}

	public void setListEmpleados(List<Empleado> listEmpleados) {
		this.listEmpleados = listEmpleados;
	}
	
	public Empleado getPojoEmpleado() {
		return pojoEmpleado;
	}
	
	public void setPojoEmpleado(Empleado pojoEmpleado) {
		this.pojoEmpleado = pojoEmpleado;
	}

	public int getNumPaginaEmpleados() {
		return numPaginaEmpleados;
	}

	public void setNumPaginaEmpleados(int numPaginaEmpleados) {
		this.numPaginaEmpleados = numPaginaEmpleados;
	}

	public OrdenCompra getPojoOrdenAutorizar() {
		return pojoOrdenAutorizar;
	}

	public void setPojoOrdenAutorizar(OrdenCompra pojoOrdenAutorizar) {
		this.pojoOrdenAutorizar = pojoOrdenAutorizar;
	}

	public long getValObraCancelada() {
		return valObraCancelada;
	}

	public void setValObraCancelada(long valObraCancelada) {
		this.valObraCancelada = valObraCancelada;
	}

	public boolean isPermiteModificar() {
		return permiteModificar;
	}

	public void setPermiteModificar(boolean permiteModificar) {
		this.permiteModificar = permiteModificar;
	}
	
	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
	
	public Cotizacion getPojoCotizacion() {
		return pojoCotizacion;
	}
	
	public void setPojoCotizacion(Cotizacion pojoCotizacion) {
		this.pojoCotizacion = pojoCotizacion;
	}

	public boolean isEditarOrdenCompra() {
		return editarOrdenCompra;
	}

	public void setEditarOrdenCompra(boolean editarOrdenCompra) {
		this.editarOrdenCompra = editarOrdenCompra;
	}

	public long getEstatusOrdenCompra() {
		return estatusOrdenCompra;
	}

	public void setEstatusOrdenCompra(long estatusOrdenCompra) {
		this.estatusOrdenCompra = estatusOrdenCompra;
	}

	public boolean isEsAdministrativo() {
		return esAdministrativo;
	}

	public void setEsAdministrativo(boolean esAdministrativo) {
		this.esAdministrativo = esAdministrativo;
	}

	public boolean isOrigenRequisicion() {
		return origenRequisicion;
	}
	
	public void setOrigenRequisicion(boolean origenRequisicion) {
		this.origenRequisicion = origenRequisicion;
	}
	
	public List<Requisicion> getListRequisiciones() {
		return listRequisiciones;
	}
	
	public void setListRequisiciones(List<Requisicion> listRequisiciones) {
		this.listRequisiciones = listRequisiciones;
	}
	
	public Requisicion getPojoRequisicion() {
		return pojoRequisicion;
	}
	
	public void setPojoRequisicion(Requisicion pojoRequisicion) {
		this.pojoRequisicion = pojoRequisicion;
	}
	
	public List<SelectItem> getTiposBusquedaRequisiciones() {
		return tiposBusquedaRequisiciones;
	}
	
	public void setTiposBusquedaRequisiciones(
			List<SelectItem> tiposBusquedaRequisiciones) {
		this.tiposBusquedaRequisiciones = tiposBusquedaRequisiciones;
	}
	
	public String getCampoBusquedaRequisiciones() {
		return campoBusquedaRequisiciones;
	}
	
	public void setCampoBusquedaRequisiciones(String campoBusquedaRequisiciones) {
		this.campoBusquedaRequisiciones = campoBusquedaRequisiciones;
	}
	
	public String getValorBusquedaRequisiciones() {
		return valorBusquedaRequisiciones;
	}
	
	public void setValorBusquedaRequisiciones(String valorBusquedaRequisiciones) {
		this.valorBusquedaRequisiciones = valorBusquedaRequisiciones;
	}
	
	public int getNumPaginaRequisiciones() {
		return numPaginaRequisiciones;
	}
	
	public void setNumPaginaRequisiciones(int numPaginaRequisiciones) {
		this.numPaginaRequisiciones = numPaginaRequisiciones;
	}

	public List<PersonaExt> getListProveedores() {
		return listProveedores;
	}

	public void setListProveedores(List<PersonaExt> listProveedores) {
		this.listProveedores = listProveedores;
	}

	public List<SelectItem> getTiposBusquedaProveedores() {
		return tiposBusquedaProveedores;
	}

	public void setTiposBusquedaProveedores(
			List<SelectItem> tiposBusquedaProveedores) {
		this.tiposBusquedaProveedores = tiposBusquedaProveedores;
	}

	public String getCampoBusquedaProveedores() {
		return campoBusquedaProveedores;
	}

	public void setCampoBusquedaProveedores(String campoBusquedaProveedores) {
		this.campoBusquedaProveedores = campoBusquedaProveedores;
	}

	public String getValorBusquedaProveedores() {
		return valorBusquedaProveedores;
	}

	public void setValorBusquedaProveedores(String valorBusquedaProveedores) {
		this.valorBusquedaProveedores = valorBusquedaProveedores;
	}

	public String getValorBusquedaTipoProveedor() {
		return valorBusquedaTipoProveedor;
	}

	public void setValorBusquedaTipoProveedor(String valorBusquedaTipoProveedor) {
		this.valorBusquedaTipoProveedor = valorBusquedaTipoProveedor;
	}

	public int getNumPaginaProveedores() {
		return numPaginaProveedores;
	}

	public void setNumPaginaProveedores(int numPaginaProveedores) {
		this.numPaginaProveedores = numPaginaProveedores;
	}

	public PersonaExt getPojoContacto() {
		return pojoContacto;
	}

	public void setPojoContacto(PersonaExt pojoContacto) {
		this.pojoContacto = pojoContacto;
	}

	public PersonaExt getPojoProveedor() {
		return pojoProveedor;
	}

	public void setPojoProveedor(PersonaExt pojoProveedor) {
		this.pojoProveedor = pojoProveedor;
	}

	public Requisicion getPojoRequisicionSeleccionada() {
		return pojoRequisicionSeleccionada;
	}
	
	public void setPojoRequisicionSeleccionada(
			Requisicion pojoRequisicionSeleccionada) {
		this.pojoRequisicionSeleccionada = pojoRequisicionSeleccionada;
	}

	public List<SubfamiliaImpuestos> getListImpuestos() {
		return listImpuestos;
	}

	public void setListImpuestos(List<SubfamiliaImpuestos> listImpuestos) {
		this.listImpuestos = listImpuestos;
	}

	public List<SelectItem> getListEstatusOrdenCompraItems() {
		return listEstatusOrdenCompraItems;
	}

	public void setListEstatusOrdenCompraItems(
			List<SelectItem> listEstatusOrdenCompraItems) {
		this.listEstatusOrdenCompraItems = listEstatusOrdenCompraItems;
	}

	public String getPatternDecimal() {
		return patternDecimal;
	}

	public void setPatternDecimal(String patternDecimal) {
		this.patternDecimal = patternDecimal;
	}

	public String getPatternNumber() {
		return patternNumber;
	}

	public void setPatternNumber(String patternNumber) {
		this.patternNumber = patternNumber;
	}

	public boolean isEvaluaConversion() {
		return evaluaConversion;
	}

	public void setEvaluaConversion(boolean evaluaConversion) {
		this.evaluaConversion = evaluaConversion;
	}
}
