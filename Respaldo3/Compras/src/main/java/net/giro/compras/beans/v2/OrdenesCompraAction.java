package net.giro.compras.beans.v2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import net.giro.adp.logica.InsumosRem;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.PersonaExt;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalleExt;
import net.giro.compras.beans.CotizacionExt;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.compras.beans.OrdenCompraExt;
import net.giro.compras.beans.OrdenCompraImpuestosExt;
import net.giro.compras.beans.PreOrdenDetalle;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalleExt;
import net.giro.compras.comun.EstatusCompras;
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraImpuestosRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.comun.TiposObra;
import net.giro.inventarios.comun.TipoMaestro;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.SubfamiliaImpuestosExt;
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
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

@ViewScoped
@ManagedBean(name="ordenesAction")
public class OrdenesCompraAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OrdenesCompraAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	// --------------------------------------------------------------------------
	private OrdenCompraRem ifzOrdenes;
	private OrdenCompraDetalleRem ifzOrdenDetalles;
	private CotizacionDetalleRem ifzCotiDetalles;
	private RequisicionDetalleRem ifzReqDetalles;
	private EmpleadoRem ifzEmpleados;
	private ReportesRem	ifzReportes;
	private ProductoRem ifzProductos;
	private SubfamiliaImpuestosRem ifzSubfamiliaImpuestos;
	// --------------------------------------------------------------------------
	private long idOrdenCompra;
	private List<OrdenCompra> listOrdenes;
	private List<OrdenCompraDetalleExt> listOrdenDetalles;
	private List<PreOrdenDetalle> listPreDetalles;
	private List<PreOrdenDetalle> listPreDetallesEliminados;
	private List<CotizacionDetalleExt> listCotDetalles;
	private OrdenCompraExt pojoOrden;
	private PreOrdenDetalle pojoOrdenDetalle;
	private PreOrdenDetalle pojoOrdenDetalleBorrar;
	private int paginacionDetalles;
	private double porcentajeIva;
	private double subtotal;
	private double iva;
	private double total;
    private boolean permiteModificar;
    private boolean ordenEditable;
	private boolean seleccionarTodos;
	private boolean esAdministrativo;
	private TipoMaestro tipoMaestro;
	private String patternNumber;
	private String patternDecimal;
	private boolean origenRequisicion;
	// --------------------------------------------------------------------------
	private Obra pojoObraBase;
	private Cotizacion pojoOrdenCotizacion;
	private Requisicion pojoOrdenRequisicion;
	// Busqueda Explosion de Insumos
	private InsumosRem ifzInsumos;
	private long idExplosionInsumos;
	// Orden de Compra Detalle Impuestos
	private OrdenCompraImpuestosRem ifzOrdenImpuestos;
	private List<OrdenCompraImpuestosExt> listOrdenImpuestos;
	private List<OrdenCompraImpuestosExt> listOrdenImpuestosBorrados;
	private boolean hasImpuestosExtras;
	private String descImpuestos;
	private double totalImpuestos;
	private String descRetenciones;
	private double totalRetenciones;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
	private int paginacionPrincipal;
	// Busqueda Obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> opcionesBusquedaObra;
	private String campoBusquedaObra;
	private String valorBusquedaObra;
	private int paginacionObras;
	// Busqueda Cotizaciones
	private CotizacionRem ifzCotizaciones;
	private List<Cotizacion> listCotizaciones;
	private Cotizacion pojoCotizacion;
	private List<SelectItem> tiposBusquedaCotizaciones;	
	private String campoBusquedaCotizaciones;
	private String valorBusquedaCotizaciones;
	private int paginacionCotizaciones;
	// Busqueda Requisiciones
	private RequisicionRem ifzRequisiciones;
	private List<Requisicion> listRequisiciones;
	private Requisicion pojoRequisicion;
	private List<SelectItem> tiposBusquedaRequisiciones;	
	private String campoBusquedaRequisiciones;
	private String valorBusquedaRequisiciones;
	private int paginacionRequisiciones;
	// Busqueda Proveedores
	private List<PersonaExt> listProveedores;
	private PersonaExt pojoProveedor;
	private PersonaExt pojoContacto;
	private List<SelectItem> tiposBusquedaProveedores;	
	private String campoBusquedaProveedores;
	private String valorBusquedaProveedores;
	private String valorBusquedaTipoProveedor;
	private int paginacionProveedores;
	// Busqueda Empleados
	private List<Empleado> listEmpleados;
	private Empleado pojoEmpleado;
	private List<SelectItem> tiposBusquedaEmpleados;	
	private String campoBusquedaEmpleados;
	private String valorBusquedaEmpleados;
	private int paginacionEmpleados;
	// Monedas
	private MonedaDAO ifzMonedas;
	private MonedasValoresDAO ifzMonValores;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private long idMonedaBase;
	private long idMonedaActual;
	private long idMonedaOriginal;
	private long idMonedaSugerida;
	private double tipoCambioActual;
	private boolean evaluaConversion;
	private boolean reConvierte;
	// Control
    private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	private List<String> backtrace;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public OrdenesCompraAction() {
		ValueExpression valExp = null;
		InitialContext ctx = null;
		Moneda valMoneda = null;
		FacesContext fc = null;
		Application app = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey(), item.getValue());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			// Conexion con modulos
			ctx = new InitialContext();
			this.ifzOrdenes = (OrdenCompraRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOrdenDetalles = (OrdenCompraDetalleRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzOrdenImpuestos = (OrdenCompraImpuestosRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraImpuestosFac!net.giro.compras.logica.OrdenCompraImpuestosRem");
			this.ifzCotizaciones = (CotizacionRem) ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzCotiDetalles = (CotizacionDetalleRem) ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzRequisiciones = (RequisicionRem) ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalles = (RequisicionDetalleRem) ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzInsumos = (InsumosRem) ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzProductos = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzSubfamiliaImpuestos = (SubfamiliaImpuestosRem) ctx.lookup("ejb:/Logica_Publico//SubfamiliaImpuestosFac!net.giro.plataforma.logica.SubfamiliaImpuestosRem");
			this.ifzMonedas = (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzMonValores = (MonedasValoresDAO) ctx.lookup("ejb:/Model_TYG//MonedasValoresImp!net.giro.tyg.dao.MonedasValoresDAO");
			
			this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenImpuestos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCotiDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzRequisiciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReqDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSubfamiliaImpuestos.setInfoSesion(this.loginManager.getInfoSesion());
			
			/* VALIDACION DE PERFILES */
			valPerfil = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			this.porcentajeIva = ((valPerfil != null && ! "".equals(valPerfil.trim())) ? Double.valueOf(valPerfil) : 0);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.esAdministrativo = ((valPerfil != null && "S".equals(valPerfil.trim())) ? true : false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			valMoneda = this.ifzMonedas.findByAbreviacion(valPerfil);
			if (valMoneda != null) {
				this.idMonedaSugerida = valMoneda.getId();
				this.idMonedaBase = valMoneda.getId();
				valMoneda = null;
			}
			
			// Inicializaciones
			this.listOrdenes = new ArrayList<OrdenCompra>();
			this.pojoOrden = new OrdenCompraExt();
			this.listPreDetalles = new ArrayList<PreOrdenDetalle>();
			this.listOrdenImpuestos = new ArrayList<OrdenCompraImpuestosExt>();
			this.listOrdenImpuestosBorrados = new ArrayList<OrdenCompraImpuestosExt>();
			this.paginacionDetalles = 1;
			this.tipoMaestro = TipoMaestro.Inventario;
			this.patternNumber = "###,###,##0";
			this.patternDecimal = "###,###,###,##0.0000";
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("folio", "Folio"));
			this.tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.tiposBusqueda.add(new SelectItem("nombreObra", "Obra"));
			this.tiposBusqueda.add(new SelectItem("nombreProveedor", "Proveedor"));
			this.tiposBusqueda.add(new SelectItem("idCotizacion.nombreComprador", "Comprador"));
			this.tiposBusqueda.add(new SelectItem("idCotizacion.folio", "Folio Cotizacion"));
			this.tiposBusqueda.add(new SelectItem("idCotizacion.id", "Cotizacion"));
			this.tiposBusqueda.add(new SelectItem("idCotizacion.idRequisicion", "Requisicion"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.paginacionPrincipal = 1;

			// Busqueda obras
			this.opcionesBusquedaObra = new ArrayList<SelectItem>();
			this.opcionesBusquedaObra.add(new SelectItem("*", "Coincidencia"));
			this.opcionesBusquedaObra.add(new SelectItem("nombre", (this.esAdministrativo ? "Obra/Egreso Administrativo" : "Obra")));
			this.opcionesBusquedaObra.add(new SelectItem("nombreCliente", "Cliente"));
			this.opcionesBusquedaObra.add(new SelectItem("id", "ID"));
			nuevaBusquedaObra();
			
			// Busqueda Cotizaciones
			this.tiposBusquedaCotizaciones = new ArrayList<SelectItem>();
			this.tiposBusquedaCotizaciones.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaCotizaciones.add(new SelectItem("folio", "Folio"));
			this.tiposBusquedaCotizaciones.add(new SelectItem("fecha", "Fecha"));
			this.tiposBusquedaCotizaciones.add(new SelectItem("nombreProveedor", "Proveedor"));
			this.tiposBusquedaCotizaciones.add(new SelectItem("nombreComprador", "Comprador"));
			this.tiposBusquedaCotizaciones.add(new SelectItem("id", "ID"));
			nuevaBusquedaCotizaciones();

			// Busqueda Requisiciones
			this.tiposBusquedaRequisiciones = new ArrayList<SelectItem>();
			this.tiposBusquedaRequisiciones.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaRequisiciones.add(new SelectItem("fecha", "Fecha"));
			this.tiposBusquedaRequisiciones.add(new SelectItem("nombreSolicita", "Comprador"));
			this.tiposBusquedaRequisiciones.add(new SelectItem("id", "ID"));
			nuevaBusquedaRequisiciones();
			
			// Busqueda Proveedor
			this.tiposBusquedaProveedores = new ArrayList<SelectItem>();
			this.tiposBusquedaProveedores.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaProveedores.add(new SelectItem("rfc", "RFC"));
			this.tiposBusquedaProveedores.add(new SelectItem("id", "ID"));
			nuevaBusquedaProveedores();
			
			// Busqueda Empleados
			this.tiposBusquedaEmpleados = new ArrayList<SelectItem>();
			this.tiposBusquedaEmpleados.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaEmpleados.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaEmpleados.add(new SelectItem("clave", "Clave"));
			this.tiposBusquedaEmpleados.add(new SelectItem("id", "ID"));
			nuevaBusquedaEmpleados();
			
			cargarMonedas();
			backtrace();
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
		}
	}
	
	public void buscar() {
		int tipoMaestro = 0;
		long idObra = 0L;
		
		try {
			control();
    		if (this.pojoObraBase != null && this.pojoObraBase.getId() != null && this.pojoObraBase.getId() > 0L) {
    			idObra = this.pojoObraBase.getId();
    			tipoMaestro = (this.pojoObraBase.getTipoObra() == 4 ? 2 : 1);
    		}

    		this.campoBusqueda = (this.campoBusqueda != null && ! "".equals(this.campoBusqueda.trim()) ? this.campoBusqueda.trim() : this.tiposBusqueda.get(0).getValue().toString());
    		this.valorBusqueda = (this.valorBusqueda != null && ! "".equals(this.valorBusqueda.trim()) ? this.valorBusqueda.trim() : "");
    		this.fechaBusqueda = (this.fechaBusqueda != null ? this.fechaBusqueda : Calendar.getInstance().getTime());

			this.paginacionPrincipal = 1;
			this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.listOrdenes = this.ifzOrdenes.findLikeProperty(this.campoBusqueda, ("fecha".equals(this.campoBusqueda) ? this.fechaBusqueda : this.valorBusqueda), idObra, tipoMaestro, false, true, true, "CASE model.sistema WHEN 0 THEN 1 WHEN 1 THEN 2 ELSE 3 END, model.id desc, model.folio desc", 0);
			if (this.listOrdenes == null || this.listOrdenes.isEmpty()) 
	    		control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Ordenes de Compra", e);
    	} finally {
			this.listOrdenes = (this.listOrdenes != null ? this.listOrdenes : new ArrayList<OrdenCompra>());
			controlLog(this.listOrdenes.size() + " Ordenes de Compra encontradas. [" + this.campoBusqueda + " - " + this.valorBusqueda + "]");
    	}
	}

	public void evaluaNuevo() {
		try {
    		control();
    		if (this.pojoObraBase == null || this.pojoObraBase.getId() == null || this.pojoObraBase.getId() <= 0L) {
    			control(-1, "Es necesario indicar una Obra para poder generar una Orden de Compra");
    			return;
    		}

			this.tipoMaestro = (this.pojoObraBase.isAdministrativa() ? TipoMaestro.Administrativo : TipoMaestro.Inventario);
			this.permiteModificar = (this.pojoObraBase.getEstatus().longValue() == 0L || this.pojoObraBase.getEstatus().longValue() == 10001211L) ? false : true;
    		if (! this.permiteModificar) {
    			control(-1, "No esta permitido añadir Ordenes de Compra a esta Obra.\nLa Obra esta Cancelada o Cerrada");
    			return;
    		}
    		
    		nuevo();
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar generar una nueva Orden de Compra", e);
    	}
	}
	
	public void nuevo() {
		ObraExt obra = null;
		
		try {
			control();
			obra = this.ifzObras.convertir(this.pojoObraBase);
			if (obra == null || obra.getId() == null || obra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al extender la Obra");
				return;
			}

			this.idExplosionInsumos = 0L;
			this.idExplosionInsumos = this.ifzInsumos.findIdActual(this.pojoObraBase.getId()); 
			
			backtrace("Proceso para Crear una Orden de Compra ... ");
			this.idOrdenCompra = 0L;
			this.origenRequisicion = false;
			this.ordenEditable = true;
			this.evaluaConversion = false;
			this.reConvierte = false;
			this.pojoOrdenCotizacion = null;
			this.pojoOrdenRequisicion = null;
			//this.pojoProveedor = null;
			this.pojoContacto = null;
			this.idMonedaActual = this.idMonedaSugerida;
			this.idMonedaOriginal = 0;
			this.paginacionDetalles = 1;
			this.listPreDetalles = new ArrayList<PreOrdenDetalle>();
			this.listPreDetallesEliminados = new ArrayList<PreOrdenDetalle>();
			this.listOrdenImpuestos = new ArrayList<OrdenCompraImpuestosExt>();
			this.listOrdenImpuestosBorrados = new ArrayList<OrdenCompraImpuestosExt>();
			this.seleccionarTodos = false;
			nuevaBusquedaCotizaciones();
			nuevaBusquedaRequisiciones();
			nuevaBusquedaProveedores();
			nuevaBusquedaEmpleados();
			
			this.pojoOrden = new OrdenCompraExt();
			this.pojoOrden.setTipo(this.tipoMaestro.ordinal());
			this.pojoOrden.setIdObra(obra);
			this.pojoOrden.setLugarEntrega(obra.getNombre());
    		this.pojoOrden.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			this.pojoOrden.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoOrden.setFechaCreacion(Calendar.getInstance().getTime());
			backtrace("Proceso terminado");
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar generar una nueva Orden de Compra", e);
    	} finally {
			totalizarDetalles();
    	}
	}

	public void nuevoCotizacion() {
		this.origenRequisicion = false;
	}
	
	public void nuevoRequisicion() {
		this.origenRequisicion = true;
	}
	
	public void editar() {
		int obraTipo = 0;
		long obraEstatus = 0;
		
		try {
			control();
			backtrace("Proceso para Editar Orden de Compra ... ");
			this.pojoOrden = this.ifzOrdenes.findExtById(this.idOrdenCompra);
			if (this.pojoOrden == null || this.pojoOrden.getId() == null || this.pojoOrden.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar la Orden de Compra seleccionada");
				return;
			}

			this.idExplosionInsumos = 0L;
			this.idExplosionInsumos = this.ifzInsumos.findIdActual(this.pojoOrden.getIdObra().getId()); 
			
			// Validacion de permiso de edicion
			obraTipo = (this.pojoObraBase != null ? this.pojoObraBase.getTipoObra() : this.pojoOrden.getIdObra().getTipoObra());
			obraEstatus = (this.pojoObraBase != null ? this.pojoObraBase.getEstatus() : this.pojoOrden.getIdObra().getEstatus());
			this.tipoMaestro = (obraTipo == TiposObra.Administrativa.ordinal() ? TipoMaestro.Administrativo: TipoMaestro.Inventario);
			this.permiteModificar = (obraEstatus == 0 || obraEstatus == 10001211L) ? false : true;
			
			this.reConvierte = false;
			this.evaluaConversion = false;
			this.listOrdenImpuestos = new ArrayList<OrdenCompraImpuestosExt>();
			this.listOrdenImpuestosBorrados = new ArrayList<OrdenCompraImpuestosExt>();
			// Editable: Si la Orden esta activa y la Obra no esta Cancelada o Cerrada
			this.ordenEditable = (this.pojoOrden.getEstatus() == EstatusCompras.ACTIVA.ordinal() ? this.permiteModificar : false);
			/*if (this.permiteModificar) {
				formatter = new SimpleDateFormat("dd-MM-yyyy");
				this.ordenEditable = (formatter.format(Calendar.getInstance().getTime()).equals(formatter.format(this.pojoOrden.getFechaCreacion())) && this.loginManager.getInfoSesion().getAcceso().getUsuario().getId() == this.pojoOrden.getCreadoPor());
				this.ordenEditable = ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario()) ? true : this.ordenEditable);
			}*/
			
			// Obtengo la moneda de la Orden de Compra
			this.idMonedaActual = this.pojoOrden.getIdMoneda();
			this.idMonedaOriginal = this.pojoOrden.getIdMoneda();
			
			// detalles desde la orden de compra 
			if (! recuperarListadoDetalles(true))
				return;
			
			nuevaBusquedaCotizaciones();
			nuevaBusquedaRequisiciones();
			nuevaBusquedaProveedores();
			nuevaBusquedaEmpleados();
			backtrace("Proceso terminado");
    	} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Orden de Compra", e);
    	}
	}
	
	public void guardar() {
		List<OrdenCompraDetalle> listAux = null;
		OrdenCompraDetalleExt pojoDetalle = null;
		OrdenCompra ordenCompra = null;
		
		try {
			control();
			if (! validaciones()) 
				return;

			backtrace("Comprobando origen por requisicion y guardando requisicion si corresponde");
			if (this.origenRequisicion && ! guardarCotizacion())
				return;
			
			//seleccionados = 0;
			this.pojoOrden.setTipo(this.tipoMaestro.ordinal());
    		this.pojoOrden.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			this.pojoOrden.setSubtotal(this.subtotal);
			this.pojoOrden.setIva(this.iva);
			this.pojoOrden.setTotal(this.total);
			
			// Guardamos/Actualizamos en la BD
			if (this.pojoOrden.getId() == null || this.pojoOrden.getId() <= 0L) {
				backtrace("Guardando OC nueva");
				this.pojoOrden.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoOrden.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoOrden.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoOrden.setFechaModificacion(Calendar.getInstance().getTime());

				this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoOrden.setId(this.ifzOrdenes.save(this.pojoOrden));
				ordenCompra = this.ifzOrdenes.findById(this.pojoOrden.getId());
				backtrace("OC guardada");
				
				// Agregamos la orden de Compra al listado
				this.listOrdenes.add(0, ordenCompra);
			} else {
				backtrace("Actualizando OC");
				this.pojoOrden.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoOrden.setFechaModificacion(Calendar.getInstance().getTime());

				this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzOrdenes.update(this.pojoOrden);
				ordenCompra = this.ifzOrdenes.findById(this.pojoOrden.getId());
				backtrace("OC actualizada");

				// Buscamos la OC en el listado
				for (OrdenCompra var : this.listOrdenes) {
					if (var.getId().longValue() == this.pojoOrden.getId().longValue()) {
						var = ordenCompra;
						break;
					}
				}
			}

			this.idOrdenCompra = ordenCompra.getId();
			this.pojoOrden.setFolio(ordenCompra.getFolio());
			this.pojoOrden.setConsecutivoProveedor(ordenCompra.getConsecutivoProveedor());
			
			// Guardamos los detalles nuevos y modificados listOrdenDetalles
			if (this.listPreDetalles != null && ! this.listPreDetalles.isEmpty()) {
				this.listOrdenDetalles = new ArrayList<OrdenCompraDetalleExt>();
				for (PreOrdenDetalle var : this.listPreDetalles) {
					if (! var.isSeleccionado()) 
						continue;
					
					// Asignamos la cotizacion al detalle
					backtrace("Generando OCDetalle de producto seleccionado");
					pojoDetalle = var.getOrdenCompraDetalle();
					pojoDetalle.setIdOrdenCompra(this.pojoOrden);
					if (pojoDetalle.getId() == null || pojoDetalle.getId() <= 0L) {
						pojoDetalle.setFechaCreacion(Calendar.getInstance().getTime());
						pojoDetalle.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					} 
					pojoDetalle.setFechaModificacion(Calendar.getInstance().getTime());
					pojoDetalle.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					
					// Añadimos a listado de detalles
					backtrace("Añadimos a listado de detalles");
					this.listOrdenDetalles.add(pojoDetalle);
				}

				if (this.listOrdenDetalles != null && ! this.listOrdenDetalles.isEmpty()) {
					backtrace("Guardamos listado de detalles");
	    			this.ifzOrdenDetalles.setInfoSesion(this.loginManager.getInfoSesion());
	    			this.listOrdenDetalles = this.ifzOrdenDetalles.saveOrUpdateListExt(this.listOrdenDetalles);
	    			this.listOrdenDetalles = this.listOrdenDetalles != null ? this.listOrdenDetalles : new ArrayList<OrdenCompraDetalleExt>();
	    			this.listPreDetalles = new ArrayList<PreOrdenDetalle>();
					for (OrdenCompraDetalleExt var : this.listOrdenDetalles)
						this.listPreDetalles.add(new PreOrdenDetalle(var));
				}
			}
			
			// Guardamos impuestos si corresponde
			backtrace("Guardando impuestos de Orden de Compra ... ");
			if (this.listOrdenImpuestos != null && ! this.listOrdenImpuestos.isEmpty()) {
				for (OrdenCompraImpuestosExt imp : this.listOrdenImpuestos) {
					imp.setIdOrdenCompra(this.pojoOrden);
					if (imp.getId() == null || imp.getId() <= 0L) {
						imp.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						imp.setFechaCreacion(Calendar.getInstance().getTime());
					}
					imp.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					imp.setFechaModificacion(Calendar.getInstance().getTime());
				}
				
				// Guardamos
    			this.ifzOrdenImpuestos.setInfoSesion(this.loginManager.getInfoSesion());
    			this.listOrdenImpuestos = this.ifzOrdenImpuestos.saveOrUpdateListExt(this.listOrdenImpuestos);
			}
			
			// Actualizo el estatus de la orden de compra y de sus dependientes
			this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenes.backOfficeOrdenCompra(this.pojoOrden.getId());
			
			// Borramos los detalles de la OC si corresponde
			if (this.listPreDetallesEliminados != null && ! this.listPreDetallesEliminados.isEmpty()) {
				backtrace("Borramos detalles previamente eliminados");
				listAux = new ArrayList<OrdenCompraDetalle>();
				for (PreOrdenDetalle var : this.listPreDetallesEliminados) {
					if (var.getId() != null && var.getId() > 0L) {
						listAux.add(this.ifzOrdenDetalles.findById(var.getOrdenCompraDetalle().getId()));
						this.ifzOrdenDetalles.delete(var.getId());
					}
				}
				
				// Limpiamos la lista de eliminados
				this.listPreDetallesEliminados.clear();
			}
			
			// Borramos impuestos de Orden de Compra previamente eliminados
			backtrace("Borramos impuestos de Orden de Compra previamente eliminados ... ");
			if (this.listOrdenImpuestosBorrados != null && ! this.listOrdenImpuestosBorrados.isEmpty()) {
				for (OrdenCompraImpuestosExt imp : this.listOrdenImpuestosBorrados) {
					if (imp.getId() != null || imp.getId() > 0L)
						this.ifzOrdenImpuestos.delete(imp.getId());
				}
				
				this.listOrdenImpuestosBorrados.clear();
			}
			
			if (this.isDebug)
				backtracePrint();
			backtrace();

			/* COMENTADO POR GUARDAR_SIN_CERRAR
			// Actualizamos el listado
			this.origenRequisicion = false;
			this.pojoOrden = new OrdenCompraExt();
			this.pojoOrdenBorrar = null;
			this.pojoOrdenRequisicion = null;
			this.ver(); */
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Orden de Compra actual", e);
    	}
	}

	public void autorizar() {
		Respuesta respuesta = null;
		OrdenCompra orden = null;
		
		try {
			control();
			if (this.idOrdenCompra <= 0L) {
				control(-1, "Ocurrio un problema al intentar Autorizar la Orden de Compra seleccionada");
				return;
			}

			this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzOrdenes.autorizar(this.idOrdenCompra, false, false);
			if (respuesta.getErrores().getCodigoError() != 0) {
				controlLog(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError());
				return;
			}

			// Actualizamos el elemento de la lista
			orden = (OrdenCompra) respuesta.getBody().getValor("ordenCompra");
			for (OrdenCompra var : this.listOrdenes) {
				if (this.idOrdenCompra == var.getId().longValue()) {
					var = orden;
					controlLog("OC Autorizada");
					break;
				}
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar autorizar la Orden de Compra", e);
    	}
	}
	
	public void cancelar() {
		Respuesta respuesta = null;
		OrdenCompra orden = null;
		
		try {
			control();
			backtrace("Proceso para Cancelar Orden de Compra ... ");
			if (this.idOrdenCompra <= 0L) {
				control(-1, "Debe indicar una Orden de Compra para Cancelar");
				backtrace("Debe indicar una Orden de Compra para Cancelar");
				return;
			}
			
			// Establecemos los atributos para la cancelacion
			backtrace("Cancelando OC");
			respuesta = this.ifzOrdenes.cancelar(this.idOrdenCompra, this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			if (respuesta != null && respuesta.getErrores().getCodigoError() != 0L) {
				backtrace(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError());
				return;
			}
			
			backtrace("OC Cancelada (Actualizada)");
			if (this.isDebug)
				backtracePrint();

			orden = (OrdenCompra) respuesta.getBody().getValor("ordenCompra");
			for (OrdenCompra var : this.listOrdenes) {
				if (this.idOrdenCompra == var.getId().longValue()) {
					var = orden;
					break;
				}
			}
			backtrace("Proceso terminado");
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar la Orden de Compra", e);
    	}
	}
	
	public void eliminarDetalle() {
		try {
			control();
			// Quitamos el detalle de la lista
			if (this.isDebug) 
				controlLog("Borrando producto listado");
			this.listPreDetalles.remove(this.pojoOrdenDetalleBorrar);
			this.listPreDetallesEliminados.add(this.pojoOrdenDetalleBorrar);
			this.pojoOrdenDetalleBorrar = null;
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar eiminar el producto seleccionado de la Orden de Compra", e);
    	}
	}
	
	public void reporte() {
		Respuesta respuesta = null;
		OrdenCompra orden = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			orden = this.ifzOrdenes.findById(this.idOrdenCompra);
			if (orden == null || orden.getId() == null || orden.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar la Orden de Compra seleccionada.\nOrden de Compra no encontrada");
				return;
			}
			
			backtrace("Imprimiento reporte Orden de Compra ... ");
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idOrdenCompra", orden.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	this.entornoProperties.getString("REPORTE_ORDEN_COMPRA"));
			params.put("ftp_host", 		this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto", this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				controlLog(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
	    		control(-1, "Ocurrio un problema al intentar exportar la Orden de Compra.\n501 - No se pudo ejecutar la peticion");
				return;
			} 
			
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "OC-" + orden.getFolio() + "." + formatoDocumento;
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				controlLog("ERROR INTERNO - No se recupero el contenido de la Orden de Compra");
	    		control(-1, "Ocurrio un problema al intentar exportar la Orden de Compra.\n404 - No se encontro la Orden de Compra " + orden.getFolio());
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			backtrace("Reporte Orden de Compra lanzado. Proceso terminado!");

			if (this.isDebug)
				backtracePrint();
		} catch (Exception e) {
    		control("Ocurrio un problema al exportar la Orden de Compra\n500 - Error interno en el servidor", e);
		} 
	}
	
	private boolean guardarCotizacion() {
		CotizacionExt extendida = null;
		
		try {
			backtrace("Guardando cotizacion ... ");
			if (this.pojoOrdenCotizacion.getId() == null || this.pojoOrdenCotizacion.getId() <= 0L) {
				backtrace("Comprobando proveedor ... ");
				if (this.pojoOrdenCotizacion.getIdProveedor() == null || this.pojoOrdenCotizacion.getIdProveedor() <= 0L) {
					control(-1, "No se detecto ningun Proveedor seleccionado");
					return false;
				}

				// Guardo otizacion
				backtrace("Guardando cotizacion ... ");
				this.pojoOrdenCotizacion.setSubtotal(this.subtotal);
				this.pojoOrdenCotizacion.setIva(this.iva);
				this.pojoOrdenCotizacion.setTotal(this.total);
				this.pojoOrdenCotizacion.setTiempoEntrega(this.pojoOrden.getTiempoEntrega());
				this.pojoOrdenCotizacion.setTipo(this.tipoMaestro.ordinal());
	    		this.pojoOrdenCotizacion.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
				this.pojoOrdenCotizacion.setSistema(1);
				this.pojoOrdenCotizacion.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoOrdenCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoOrdenCotizacion.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoOrdenCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Inserto en la BD
				this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoOrdenCotizacion.setId(this.ifzCotizaciones.save(this.pojoOrdenCotizacion));
				backtrace("Cotizacion guardada");
				
				// Detalles COTIZACION
				backtrace("Guardando detalles de cotizacion ... ");
				this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
				extendida = this.ifzCotizaciones.findExtById(this.pojoOrdenCotizacion.getId());
				if (this.listCotDetalles != null && ! this.listCotDetalles.isEmpty()) {
					for (CotizacionDetalleExt var :  this.listCotDetalles) {
						var.setIdCotizacion(extendida);
						var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						var.setFechaCreacion(Calendar.getInstance().getTime());
						var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						var.setFechaModificacion(Calendar.getInstance().getTime());
					}
					
					// Guardamos
					this.listCotDetalles = this.ifzCotiDetalles.saveOrUpdateListExt(this.listCotDetalles);
					backtrace("Detalles de cotizacion guardados.");
				}
				
				backtrace("Asignando cotizacion a Orden de Compra ... ");
				this.pojoOrden.setIdCotizacion(extendida);
				this.pojoOrden.setBase(1);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Orden de Compra desde Requisicion", e);
			return false;
		}
		
		return true;
	}
	
	private boolean recuperarListadoDetalles(boolean fromOrdenCompra) {
		List<OrdenCompraDetalleExt> lista = null;
		PreOrdenDetalle det = null;
		String mensaje = "";
		
		try {
			control();
			this.paginacionDetalles = 1;
			this.listPreDetalles = new ArrayList<PreOrdenDetalle>();
			this.listPreDetallesEliminados = new ArrayList<PreOrdenDetalle>();
			if (this.pojoOrden == null && this.pojoOrden.getIdCotizacion() == null)
				return false;

			this.ifzOrdenDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			if (fromOrdenCompra) {
				// Recuperamos los detalles de la orden de compra
				controlLog("Recuperamos detalles por OC: " + this.pojoOrden.getId());
				this.seleccionarTodos = true;
				lista = this.ifzOrdenDetalles.findExtAll(this.pojoOrden.getId()); 
				if (lista == null || lista.isEmpty()) {
					mensaje = "Ocurrio un problema al recuperar los productos de la Orden de Compra"; 
					return false;
				}
				
				for (OrdenCompraDetalleExt var : lista)
					this.listPreDetalles.add(new PreOrdenDetalle(var));
			} else if (! this.origenRequisicion) {
				// Recuperamos los detalles de la cotizacion
				controlLog("Recuperamos detalles de Cotizacion: " + this.pojoOrden.getIdCotizacion().getId());
				this.seleccionarTodos = false;
				this.listCotDetalles = this.ifzCotiDetalles.findExtAll(this.pojoOrden.getIdCotizacion().getId()); 
				if (this.listCotDetalles != null && ! this.listCotDetalles.isEmpty()) {
					for (CotizacionDetalleExt var : this.listCotDetalles) {
						if (var.getSuministroPendiente() <= 0) 
							continue;
						this.listPreDetalles.add(new PreOrdenDetalle(var));
					}
					
					// Comprobamos existencias en Almacen
					//comprobarAlmacen();
				}
				
				if (this.listPreDetalles == null || this.listPreDetalles.isEmpty()) {
					mensaje = "La Cotizacion seleccionada no tiene Producto disponibles"; 
					return false;
				}
			} else if (this.origenRequisicion) {
				// Recuperamos los detalles de la cotizacion proveniente de una Requisicion
				controlLog("Recuperamos detalles de Requisicion (Cotizacion): " + this.pojoRequisicion.getId());
				this.seleccionarTodos = true;
				if (this.listCotDetalles != null && ! this.listCotDetalles.isEmpty()) {
					for (CotizacionDetalleExt var : this.listCotDetalles) {
						if (var.getEstatus() == 1) 
							continue;
						
						det = new PreOrdenDetalle(var);
						det.setSeleccionado(true);
						this.listPreDetalles.add(det);
					}
				}
				
				if (this.listPreDetalles == null || this.listPreDetalles.isEmpty()) {
					mensaje = "La Requisicion seleccionada no tiene Producto disponibles";
					return false;
				}
			}
			
			// Comprobamos impuestos de los detalles
			comprobarOrdenImpuestos(); 
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los productos para la Orden de Compra", e);
			mensaje = "";
			return false;
		} finally {
			if (! "".equals(mensaje.trim()) && (this.listPreDetalles == null || this.listPreDetalles.isEmpty())) {
				control(-1, mensaje);
				return false;
			}
			
			this.totalizarDetalles();
		}
		
		return true;
	}
	
	public void totalizarDetalles() {
		double impuesto = 0;
		
		this.subtotal = 0;
		this.iva = 0;
		this.total = 0;
		this.totalImpuestos = 0;
		this.totalRetenciones = 0;
		if (this.listPreDetalles == null) {
			this.listPreDetalles = new ArrayList<PreOrdenDetalle>();
			return;
		}

		controlLog("Totalizamos detalles ... ");
		for (PreOrdenDetalle detalle : this.listPreDetalles) {
			if (! detalle.isSeleccionado()) 
				continue;
			
			this.subtotal += detalle.getImporte();
			this.iva += ((detalle.getImporte() * this.porcentajeIva) / 100);
		}

		if (this.subtotal > 0 && (this.listOrdenImpuestos != null && ! this.listOrdenImpuestos.isEmpty())) {
			controlLog("Añadimos los impuestos y retenciones de los productos ... ");
			for (OrdenCompraImpuestosExt imp : this.listOrdenImpuestos) {
				if ("DE".equals(imp.getIdImpuesto().getTipoCuenta())) {
					this.descImpuestos = imp.getDescripcion();
					impuesto = imp.getPorcentaje();
					if (impuesto > 0)
						impuesto = (impuesto > 1) ? impuesto / 100 : impuesto; 
					impuesto = this.subtotal * impuesto; 
					imp.setMonto(impuesto);
					this.totalImpuestos += impuesto;
				} else if ("AC".equals(imp.getIdImpuesto().getTipoCuenta())) {
					this.descRetenciones = imp.getDescripcion();
					impuesto = imp.getPorcentaje();
					if (impuesto > 0)
						impuesto = (impuesto > 1) ? impuesto / 100 : impuesto; 
					impuesto = this.iva * impuesto;
					imp.setMonto(impuesto);
					this.totalRetenciones += impuesto;
				}
			}
		}
		
		this.total = ((this.subtotal + this.iva) - this.totalRetenciones) - this.totalImpuestos;
		comprobarSeleccionarTodos();
		controlLog("Totalizando terminado!");
	}
	
	@SuppressWarnings("unused")
	private boolean validaCancelarOrdenCompra() {
		try {
			
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	private void cargarMonedas() {
		try {
			// Cargamos la lista de monedas
			this.listMonedasItems = new ArrayList<SelectItem>();
			this.listMonedas = this.ifzMonedas.findAll();
			if (this.listMonedas == null || this.listMonedas.isEmpty())
				return;
			
			for (Moneda var : this.listMonedas) 
				this.listMonedasItems.add(new SelectItem(var.getId(), var.getNombre() + " (" + var.getAbreviacion() + ")"));
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar las Monedas", e);
		}
	}
	
	private boolean validaciones() {
		boolean valido = false;
		
		if (this.evaluaConversion) {
			backtrace("VALIDACION: Conversion pendiente");
			control(-1, "Hay una conversion pendiente.\nPresione el boton 'Realizar Conversion' y despues presione Guardar");
			return false;
		}

		backtrace("Validando OC");//listOrdenDetalles
		if (this.listPreDetalles == null || this.listPreDetalles.isEmpty()) {
			backtrace("VALIDACION: ERROR 4 - Lista de productos vacia");
			control(4, "Lista de productos vacia");
			return false;
		}
		
		for (PreOrdenDetalle var : this.listPreDetalles) {
			if (var.isSeleccionado()) {
				valido = true;
				break;
			}
		}
		
		if (! valido) {
			backtrace("VALIDACION: ERROR 5 - Ningun producto seleccionado");
			control(5, "Ningun producto seleccionado");
			return false;
		}
		
		if (this.pojoOrden.getAnticipo().doubleValue() < 0) {
			backtrace("VALIDACION: ERROR 8 - Anticipo menor o igual a cero");
			control(8, "Anticipo menor o igual a cero");
			return false;
		}
		
		/*if (this.pojoOrden.getTipoCambio().doubleValue() <= 0) {
    		this.operacion = true;
			this.mensaje = "ERROR 9"; // tipo de cambio menor a cero
			this.tipoMensaje = 9;
			if (this.isDebug)
				backtrace("VALIDACION: ERROR 9 - Tipo de cambio menor a cero");
			return false;
		}*/
		
		if (this.pojoOrden.getPlazo() <= 0) {
			backtrace("VALIDACION: ERROR 7 - Plazo menor o igual a cero");
			control(7, "Plazo menor o igual a cero");
			return false;
		}
		
		if (this.pojoOrden.getTiempoEntrega() <= 0) {
			backtrace("VALIDACION: ERROR 6 - Tiempo de entrega menor o igual a cero");
			control(6, "Tiempo de entrega menor o igual a cero");
			return false;
		}

		if (this.isDebug)
			backtracePrint();
		
		return true;
	}
	
	public void comprobarSeleccionarTodos() {
		if (this.listPreDetalles == null || this.listPreDetalles.isEmpty())
			return;
		
		this.seleccionarTodos = true;
		for (PreOrdenDetalle var : this.listPreDetalles) {
			if (! var.isSeleccionado()) {
				this.seleccionarTodos = false;
				break;
			}
		}
	}

	private void comprobarOrdenImpuestos() {
		List<SubfamiliaImpuestosExt> listImpuestosAux = null;
		OrdenCompraImpuestosExt pojoOrdenImpuesto = null;
		String tipoPersonaProveedor = "";
		
		try {
			if (this.listPreDetalles == null || this.listPreDetalles.isEmpty())
				return;
			
			tipoPersonaProveedor = this.pojoOrden.getTipoPersonaProveedor();
			if (this.origenRequisicion) {
				if (this.pojoOrdenCotizacion.getIdProveedor() == null || this.pojoOrdenCotizacion.getIdProveedor() <= 0L)
					return;
				tipoPersonaProveedor = this.pojoOrdenCotizacion.getTipoPersonaProveedor();
			}
			
			// Compruebo si hay impuestos asignados
			this.listOrdenImpuestos = new ArrayList<OrdenCompraImpuestosExt>();
			if (this.pojoOrden.getId() != null && this.pojoOrden.getId() > 0L)
				this.listOrdenImpuestos = this.ifzOrdenImpuestos.findExtAll(this.pojoOrden.getId());
			this.listOrdenImpuestos = (this.listOrdenImpuestos != null ? this.listOrdenImpuestos : new ArrayList<OrdenCompraImpuestosExt>());
			
			for (PreOrdenDetalle detalle : this.listPreDetalles) {
				backtrace("Recupero los impuestos para la familia " + detalle.getIdProducto().getFamilia().getId() + " - " + detalle.getIdProducto().getDescFamilia());
				listImpuestosAux = this.ifzSubfamiliaImpuestos.findExtByProperty("idSubfamilia", detalle.getIdProducto().getFamilia().getId());
				if (listImpuestosAux == null || listImpuestosAux.isEmpty()) 
					continue;
				
				for (SubfamiliaImpuestosExt imp : listImpuestosAux) {
					// No se permite la RETENCION(10001035) por RENTA DE EQUIPO(10002662) para un Negocio (Persona Moral)
					if ("N".equals(tipoPersonaProveedor)) {
						if (imp.getIdSubfamilia().getId() == 10002662 && imp.getIdImpuesto().getId() == 10001035)
							continue;
					}

					// Comprobamos que el impuesto no halla sido agregado previamente
					if (existeImpuesto(imp.getIdImpuesto().getId())) 
						continue;

					pojoOrdenImpuesto = new OrdenCompraImpuestosExt();
					pojoOrdenImpuesto.setIdOrdenCompra(this.pojoOrden);
					pojoOrdenImpuesto.setIdImpuesto(imp.getIdImpuesto());
					pojoOrdenImpuesto.setPorcentaje(imp.getPorcentaje().doubleValue());
					pojoOrdenImpuesto.setMonto(0);
					pojoOrdenImpuesto.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					pojoOrdenImpuesto.setFechaCreacion(Calendar.getInstance().getTime());
					pojoOrdenImpuesto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					pojoOrdenImpuesto.setFechaModificacion(Calendar.getInstance().getTime());
					this.listOrdenImpuestos.add(pojoOrdenImpuesto);
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al verificar los impuestos de los productos a traves de la Familia aplicada al(los) producto(s)", e);
		} finally {
			this.listOrdenImpuestos = (this.listOrdenImpuestos != null ? this.listOrdenImpuestos : new ArrayList<OrdenCompraImpuestosExt>());
			this.hasImpuestosExtras = (this.listOrdenImpuestos.size() > 0);
			backtrace(this.listOrdenImpuestos.size() + " Impuestos encontrados");
			totalizarDetalles();
		}
	}
	
	private boolean existeImpuesto(long idImpuesto) {
		if (this.listOrdenImpuestos == null)
			this.listOrdenImpuestos = new ArrayList<OrdenCompraImpuestosExt>();
		
		for (OrdenCompraImpuestosExt item : this.listOrdenImpuestos) {
			if (idImpuesto == item.getIdImpuesto().getId())
				return true;
		}
		
		return false;
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
			control("No se pudo obtener el Tipo de Cambio actual para la conversion de " + monedaOrigen + " a " + monedaDestino, e);
			tipoCambio = 1.0;
		}
		
		return tipoCambio;
	}
	
	public void evaluaConversion() {
		this.evaluaConversion = false;
		if (this.idMonedaActual != this.pojoOrden.getIdMoneda()) {
			if (this.pojoOrden.getIdMoneda() != this.pojoOrden.getIdCotizacion().getIdMoneda())
				this.pojoOrden.setTipoCambio(new BigDecimal(getTipoCambio(this.idMonedaActual, this.pojoOrden.getIdMoneda())));
			else 
				this.pojoOrden.setTipoCambio(new BigDecimal(1.0));
			if (this.operacion)
				return;
			this.evaluaConversion = true;
		}
	}
	
	public void evaluaTipoCambio() {
		this.reConvierte = false;
		if (this.pojoOrden.getIdCotizacion().getIdMoneda() == this.pojoOrden.getIdMoneda())
			return;
		if (this.evaluaConversion)
			return;
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
			if (this.idMonedaOriginal == this.pojoOrden.getIdMoneda()) {
				restauraPreciosUnitariosDetalles();
			} else {
				if (this.reConvierte) {
					restauraPreciosUnitariosDetalles();
					this.idMonedaActual = this.idMonedaOriginal;
					this.reConvierte = false;
				} 
				
				backtrace("Obteniendo Moneda Origen ... ");
				monOrigen = getMonedaById(this.idMonedaActual);
				if (monOrigen == null) {
					backtrace("No pude obtener la Moneda Base para la conversion ... ID " + this.idMonedaActual);
					control(-1, "No pude obtener la Moneda Base para la conversion");
					return;
				}
				
				backtrace("Obteniendo Moneda Destino ... ");
				monDestino = getMonedaById(this.pojoOrden.getIdMoneda());
				if (monDestino == null) {
					backtrace("No pude obtener la Moneda Destino para la conversion ... ID " + this.pojoOrden.getIdMoneda());
					control(-1, "No pude obtener la Moneda Destino para la conversion");
					return;
				}

				backtrace("Obteniendo Tipo de Cambio ... ");
				if (this.pojoOrden.getTipoCambio().doubleValue() > 1) {
					tipoCambio = this.pojoOrden.getTipoCambio().doubleValue();
				} else {
					backtrace("Obteniendo Tipo de Cambio desde Tesoreria ... ");
					if (this.idMonedaBase == monDestino.getId())
						tipoCambio = getTipoCambio(monDestino, monOrigen);
					else
						tipoCambio = getTipoCambio(monOrigen, monDestino);
					if (tipoCambio <= 0) {
						this.pojoOrden.setIdMoneda(this.idMonedaActual);
						control(-1, "Ocurrio un problema al convertir " + monOrigen.getNombre() + " a " + monDestino.getNombre() + ".\nNo se pudo recuperar el Tipo de Cambio");
						return;
					}
					
					// Actualizo en la Orden el tipo de cambio
					this.pojoOrden.setTipoCambio(new BigDecimal(tipoCambio));
				}
				
				backtrace("Tipo de Cambio obtenido :) ... " + tipoCambio);
				backtrace("Realizando Conversion para productos | " + monOrigen.getAbreviacion() + " a " + monDestino.getAbreviacion() + " ... ");
				for (PreOrdenDetalle var : this.listPreDetalles) {
					if (this.idMonedaBase == monDestino.getId())
						monto = var.getPrecioUnitario() * tipoCambio;
					else
						monto = var.getPrecioUnitario() / tipoCambio;
					var.setPrecioUnitario(monto);
					
					monto = monto * var.getCantidad();
					var.setImporte(monto);
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar realizar la Conversion de Monedas", e);
		} finally {
			this.evaluaConversion = false;
			// Respaldamos los valores actuales de la Orden y totalizamos
			this.idMonedaActual = this.pojoOrden.getIdMoneda();
			this.tipoCambioActual = this.pojoOrden.getTipoCambio().doubleValue();
			totalizarDetalles();
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
			control("Error en Compras.OrdenesComprasAction.getMonedaById(). No se pudo obtener la moneda " + idMoneda, e);
		}
		
		return resultado;
	}
	
	private double getTipoCambio(long idMonedaOrigen, long idMonedaDestino) {
		Moneda monOrigen = null;
		Moneda monDestino = null;
		
		monOrigen  = getMonedaById(idMonedaOrigen);
		monDestino = getMonedaById(idMonedaDestino);
		
		controlLog("Comprobando Moneda Origen ... " + idMonedaOrigen);
		if (monOrigen == null) {
			controlLog("No pude obtener la Moneda Origen para la conversion ... ID " + idMonedaOrigen);
			control(-1, "No pude obtener la Moneda Origen para la conversion");
			return 1.0;
		}
		
		controlLog("Comprobando Moneda Destino ... " + idMonedaDestino);
		if (monDestino == null) {
			controlLog("No pude obtener la Moneda Destino para la conversion ... ID " + idMonedaDestino);
			control(-1, "No pude obtener la Moneda Destino para la conversion");
			return 1.0;
		}
		
		return getTipoCambio(monOrigen, monDestino);
	}
	
	private double getTipoCambio(Moneda monedaOrigen, Moneda monedaDestino) {
		MonedasValores valor = null;
		Double tipoCambio = 1.0;
		
		try {
			control();
			// Validamos monedas
			if (monedaOrigen == null || monedaDestino == null) {
				log.warn("Una o ambas Monedas involucradas nulas");
				control(-1, "Ocurrio un problema al intentar recuperar las Monedas involucradas en la Conversion");
				return tipoCambio;
			}
			
			// Comprobamos Tipo de Cambio registrado
			valor = this.ifzMonValores.findActual(monedaOrigen, monedaDestino);
			tipoCambio = valor.getValor().doubleValue();
			if (tipoCambio == null || tipoCambio <= 0.0) {
				log.warn("No se pudo recuperar el tipo de cambio para " + monedaOrigen.getAbreviacion() + " a " + monedaDestino.getAbreviacion());
				control(-1, "No puedo Convertir " + monedaOrigen.getAbreviacion() + " a " + monedaDestino.getAbreviacion() + "\nNo existe registro de Tipo de Cambio");
				tipoCambio = 1.0;
			}
		} catch (Exception e) {
			log.warn("Error en Compras.OrdenesComprasAction.getTipoCambio(). No se pudo obtener el Tipo de Cambio actual para la conversion de " + monedaOrigen.getAbreviacion() + " a " + monedaDestino.getAbreviacion(), e);
			control("No puedo Convertir " + monedaOrigen.getAbreviacion() + " a " + monedaDestino.getAbreviacion() + "\nNo existe registro de Tipo de Cambio", e);
			tipoCambio = 1.0;
		}
		
		return tipoCambio;
	}

	private void restauraPreciosUnitariosDetalles() {
		for (PreOrdenDetalle pojoMod : this.listPreDetalles) {
			for (CotizacionDetalleExt pojoOri : this.listCotDetalles) {
				if (pojoMod.getIdProducto().getId().longValue() == pojoOri.getIdProducto().getId().longValue()) {
					pojoMod.setPrecioUnitario(pojoOri.getPrecioUnitario());
					break;
				}
			}
		}
	}

	private void control() {
		this.operacion = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(int tipoMensaje, String mensaje) { 
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, 1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema no controlado al realizar la operacion";
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n" + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje + "\n");
	}

	private void backtrace() {
		if (this.backtrace == null)
			this.backtrace = new ArrayList<String>();
		this.backtrace.clear();
	}
	
	private void backtrace(String mensaje) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		String codigo = "";
		
		codigo = formatter.format(Calendar.getInstance().getTime());
		this.backtrace.add(codigo + " - " + mensaje);
	}
	
	private void backtracePrint() {
		if (this.backtrace == null || this.backtrace.size() <= 0)
			return;

		String buffer = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + "\n";
		for (String msg : this.backtrace)
			buffer += msg + "\n";
		
		controlLog("\n\nBACKTRACE OC START --------------------------------------------------");
		controlLog(buffer);
		controlLog("BACKTRACE OC END --------------------------------------------------\n\n");
	}

	// -------------------------------------------------------------------------
	// BUSQUEDA OBRAS
	// -------------------------------------------------------------------------
	
	public void nuevaBusquedaObra() {
		control();
		this.campoBusquedaObra = this.opcionesBusquedaObra.get(0).getValue().toString();
		this.valorBusquedaObra = "";
		
		this.paginacionObras = 1;
		this.listObras = new ArrayList<Obra>();
		this.pojoObra = null;
	}
	
	public void buscarObras() {
		boolean incluirObrasAdministrativas = false;
		
		try {
			control();
			if ("".equals(this.campoBusquedaObra))
				this.campoBusquedaObra = this.opcionesBusquedaObra.get(0).getValue().toString();
			incluirObrasAdministrativas = this.esAdministrativo; 

			controlLog("Cobranza ... buscando obras");
			this.paginacionObras = 1;
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObra, this.valorBusquedaObra, incluirObrasAdministrativas, false, "", 0);
			if (this.listObras == null || this.listObras.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch(Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
    	}
	}
	
	public void seleccionarObra() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Obra seleccionada");
				return;
			}

			this.pojoObraBase = new Obra();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.pojoObraBase, this.pojoObra);
			nuevaBusquedaObra();
			
			if (this.listOrdenes != null && ! this.listOrdenes.isEmpty())
				buscar();
		} catch(Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada.", e);
		}
	}
	
	public void quitarObra() {
		try {
			control();
			this.pojoObraBase = null;
			this.permiteModificar = false;
			this.tipoMaestro = TipoMaestro.Ninguno;
			nuevaBusquedaObra();
		} catch(Exception e) {
			control("Ocurrio un problema al intentar quitar la Obra seleccionada", e);
		}
	}
	
	// -------------------------------------------------------------------------
	// BUSQUEDA COTIZACIONES
	// -------------------------------------------------------------------------
	
	public void nuevaBusquedaCotizaciones() {
		control();
		this.campoBusquedaCotizaciones = this.tiposBusquedaCotizaciones.get(0).getValue().toString();
		this.valorBusquedaCotizaciones = "";
		
		this.paginacionCotizaciones = 1;
		this.listCotizaciones = new ArrayList<Cotizacion>();
		this.pojoCotizacion = null;
	}
		
	public void buscarCotizaciones() throws Exception {
		try {
			control();
			if ("".equals(this.campoBusquedaCotizaciones))
				this.campoBusquedaCotizaciones = this.tiposBusquedaCotizaciones.get(0).getValue().toString();
			
			backtrace("Busqueda de Cotizaciones. " + this.campoBusquedaCotizaciones + ": " + this.valorBusquedaCotizaciones);
    		this.paginacionCotizaciones = 1;
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCotizaciones = this.ifzCotizaciones.findLikeProperty(this.campoBusquedaCotizaciones, this.valorBusquedaCotizaciones, this.pojoObraBase.getId(), this.tipoMaestro.ordinal(), false, false, false, "CASE sistema when 0 then 0 when -1 then 1 when 1 then 2 else 3 end, CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.fecha desc, model.id desc", 0);
			if (this.listCotizaciones == null || this.listCotizaciones.isEmpty()) {
				backtrace("Buscando Cotizaciones. ERROR 2 - Busqueda sin resultados");
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Cotizaciones", e);
			if (this.listCotizaciones != null) 
				this.listCotizaciones.clear();
    	} finally {
			if (this.isDebug)
				backtracePrint();
    	}
	}

	public void seleccionarCotizacion() throws Exception {
		List<PersonaExt> provs = null;
		PersonaExt proveedor = null;
		CotizacionExt cotizacion = null;
		
		try {
			control();
			if (this.pojoCotizacion == null)
				return;
			
			cotizacion = this.ifzCotizaciones.findExtById(this.pojoCotizacion.getId());
			if (cotizacion == null || cotizacion.getId() == null || cotizacion.getId() <= 0L) {
				control(-1, "Ocuriro un problema al extender la Cotizacion");
				return;
			}
			
			this.pojoOrdenCotizacion = null;
			this.pojoOrden.setIdCotizacion(cotizacion);
			this.pojoOrden.setFlete(cotizacion.getFlete());
			this.pojoOrden.setTiempoEntrega(cotizacion.getTiempoEntrega());
			this.pojoOrden.setLugarEntrega(this.pojoObraBase.getNombre());
			this.pojoOrden.setIdMoneda(cotizacion.getIdMoneda());
			this.pojoOrden.setTipoCambio(new BigDecimal(1.0));
			this.idMonedaActual = cotizacion.getIdMoneda();
			this.idMonedaOriginal = cotizacion.getIdMoneda();
			
			// Recuperamos el proveedor
			provs = this.ifzCotizaciones.findPersonaLikeProperty("id", cotizacion.getIdProveedor().getId(), cotizacion.getTipoPersonaProveedor(), 0);
			if (provs != null && ! provs.isEmpty()) {
				proveedor = provs.get(0);
				this.pojoOrden.setIdProveedor(proveedor);
				this.pojoOrden.setNombreProveedor(proveedor.getNombre());
				this.pojoOrden.setTipoPersonaProveedor(cotizacion.getTipoPersonaProveedor());
				this.valorBusquedaTipoProveedor = cotizacion.getTipoPersonaProveedor();
			}
			
			if (this.pojoOrden.getIdMoneda() <= 0L) {
				this.pojoOrden.setIdMoneda(this.loginManager.getInfoSesion().getEmpresa().getMonedaId());
				this.pojoOrden.setMoneda(this.loginManager.getInfoSesion().getEmpresa().getMoneda());
			}

			// detalles desde la cotizacion 
			if (! recuperarListadoDetalles(false))
				return;
			backtrace("Proceso de recuperacion de Cotizacion terminado");
			nuevaBusquedaCotizaciones();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Cotizacion seleccionada", e);
		} finally {
    		if (this.isDebug)
				backtracePrint();
    	}
	}

	// -------------------------------------------------------------------------
	// REQUISICIONES
	// -------------------------------------------------------------------------
	
	public void nuevaBusquedaRequisiciones() {
		control();
		this.campoBusquedaRequisiciones = this.tiposBusquedaRequisiciones.get(0).getValue().toString();
		this.valorBusquedaRequisiciones = "";
		
		this.paginacionRequisiciones = 1;
		this.listRequisiciones = new ArrayList<Requisicion>();
		this.pojoRequisicion = null;
	}
	
	public void buscarRequisiciones() {
		try {
			control();
			if ("".equals(this.campoBusquedaRequisiciones))
				this.campoBusquedaRequisiciones = this.tiposBusquedaRequisiciones.get(0).getValue().toString();

    		this.paginacionRequisiciones = 1;
			this.ifzRequisiciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listRequisiciones = this.ifzRequisiciones.findLikeProperty(this.campoBusquedaRequisiciones, this.valorBusquedaRequisiciones, this.pojoObraBase.getId(), this.tipoMaestro.ordinal(), false, false, 0L, "", 0);
			if (this.listRequisiciones == null || this.listRequisiciones.isEmpty()) 
				control(2, "La busqueda no regreso resultados");
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Requisiciones", e);
    	} finally {
			if (this.isDebug)
				backtracePrint();
    	}
	}
	
	public void seleccionarRequisicion() {
		List<RequisicionDetalleExt> listReqDetalles = null;
		CotizacionDetalleExt cdet = null;
		double precioUnitario = 0;
		double tipoCambio = 0;
		double monto = 0;
		
		try {
			control();
			// Comprobamos requisicion
			backtrace("Proceso de recuperacion de Requisicion seleccionada ... Comprobando");
			if (this.pojoRequisicion == null || this.pojoRequisicion.getId() == null || this.pojoRequisicion.getId() <= 0L) {
				controlLog("ERROR 2 - Debe seleccionar una requisicion");
				control(2, "La busqueda no regreso resultados");
				return;
			}

			// Recuperamos el detalle de la requisicion
			backtrace("Recuperamos detalles de Requisicion");
			listReqDetalles = this.ifzReqDetalles.findExtAll(this.pojoRequisicion.getId()); 
			if (listReqDetalles == null || listReqDetalles.isEmpty()) {
				backtrace("ERROR INTERNO - La Requisicion no tiene productos o ya han sido usados en cotizaciones");
				control(-1, "La Requisicion no tiene productos disponibles");
				return;
			}

			// Asigno Requisicion
			backtrace("Asigno Requisicion");
			this.pojoOrdenRequisicion = copiaRequisicion(this.pojoRequisicion);
			
			// Genero cotizacion
			backtrace("Genero cotizacion a partir de la requisicion seleccionada");
			this.pojoOrdenCotizacion = new Cotizacion();
			this.pojoOrdenCotizacion.setId(0L);
			this.pojoOrdenCotizacion.setIdRequisicion(this.pojoRequisicion.getId());
			this.pojoOrdenCotizacion.setIdObra(this.pojoObraBase.getId());
			this.pojoOrdenCotizacion.setNombreObra(this.pojoObraBase.getNombre());
			this.pojoOrdenCotizacion.setFlete(0);
			this.pojoOrdenCotizacion.setTiempoEntrega(0);
			this.pojoOrdenCotizacion.setIdComprador(this.pojoRequisicion.getIdSolicita());
			this.pojoOrdenCotizacion.setNombreComprador(this.pojoRequisicion.getNombreSolicita());
			this.pojoOrdenCotizacion.setIdProveedor(0L);
			this.pojoOrdenCotizacion.setNombreProveedor("");
			this.pojoOrdenCotizacion.setRfcProveedor("");
			this.pojoOrdenCotizacion.setTipoPersonaProveedor("");
			
			// Proveedor
			if (this.pojoProveedor != null && this.pojoProveedor.getId() > 0L) {
				this.pojoOrdenCotizacion.setIdProveedor(this.pojoProveedor.getId());
				this.pojoOrdenCotizacion.setNombreProveedor(this.pojoProveedor.getNombre());
				this.pojoOrdenCotizacion.setRfcProveedor(this.pojoProveedor.getRfc());
				this.pojoOrdenCotizacion.setTipoPersonaProveedor(this.valorBusquedaTipoProveedor);
				// Contacto
				if (this.pojoContacto != null && this.pojoContacto.getId() > 0L) {
					this.pojoOrdenCotizacion.setIdContacto(this.pojoContacto.getId());
					this.pojoOrdenCotizacion.setNombreContacto(this.pojoContacto.getNombre());
				}
			}

			this.pojoOrdenCotizacion.setIdMoneda(this.pojoRequisicion.getIdMoneda());
			this.pojoOrdenCotizacion.setTipo(this.pojoRequisicion.getTipo());
			this.pojoOrdenCotizacion.setSubtotal(this.subtotal);
			this.pojoOrdenCotizacion.setIva(this.iva);
			this.pojoOrdenCotizacion.setTotal(this.total);
			this.pojoOrdenCotizacion.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoOrdenCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoOrdenCotizacion.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoOrdenCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
			
			this.idMonedaActual = this.pojoOrdenCotizacion.getIdMoneda();
			this.idMonedaOriginal = this.pojoOrdenCotizacion.getIdMoneda();
			this.pojoOrden.setIdMoneda(this.pojoRequisicion.getIdMoneda());
			this.pojoOrden.setTipo(this.pojoOrdenCotizacion.getTipo());

			// Genero detalles para cotizacion
			backtrace("Genero detalles de cotizacion a partir de la requisicion seleccionada");
			this.listCotDetalles = new ArrayList<CotizacionDetalleExt>();
			for (RequisicionDetalleExt var : listReqDetalles) {
				// Comprueba si la cotizacion es de una moneda diferente a PESOS
				if (this.idMonedaBase == this.pojoOrdenCotizacion.getIdMoneda() && this.idMonedaBase != var.getIdProducto().getIdMoneda().getId()) {
					precioUnitario = var.getIdProducto().getPrecioCompra();
					tipoCambio = var.getIdProducto().getTipoCambio();
					if (tipoCambio <= 1)
						tipoCambio = recuperaTipoCambioActual(this.pojoOrdenCotizacion.getIdMoneda(), var.getIdProducto().getIdMoneda().getId());
					precioUnitario = precioUnitario * tipoCambio;
					monto = var.getCantidad() * precioUnitario;
				} else if (this.idMonedaBase != this.pojoOrdenCotizacion.getIdMoneda() && this.idMonedaBase == var.getIdProducto().getIdMoneda().getId()) {
					precioUnitario = var.getIdProducto().getPrecioCompra();
					tipoCambio = var.getIdProducto().getTipoCambio();
					if (tipoCambio <= 1)
						tipoCambio = recuperaTipoCambioActual(var.getIdProducto().getIdMoneda().getId(), this.pojoOrdenCotizacion.getIdMoneda());
					precioUnitario = precioUnitario / tipoCambio;
					monto = var.getCantidad() * precioUnitario;
				} else {
					precioUnitario = var.getIdProducto().getPrecioCompra();
					monto = var.getCantidad() * precioUnitario;
				}
				
				cdet = new CotizacionDetalleExt();
				cdet.setCantidad(var.getCantidad());
				cdet.setCantidadInicial(var.getCantidad());
				cdet.setIdProducto(var.getIdProducto());
				cdet.setPrecioUnitario(precioUnitario);
				cdet.setImporte(monto);
				cdet.setMargen(0);
				cdet.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				cdet.setFechaCreacion(Calendar.getInstance().getTime());
				cdet.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				cdet.setFechaModificacion(Calendar.getInstance().getTime());
				this.listCotDetalles.add(cdet);
			}

			backtrace(this.listCotDetalles.size() + " detalles de Cotizacion generados.");
			// detalles desde la cotizacion 
			backtrace("Genero detalles para Orden de Compra desde Cotizacion (Requisicion).");
			if (! recuperarListadoDetalles(false))
				return;
			backtrace("Proceso de recuperacion de Requisicion terminado");
			nuevaBusquedaRequisiciones();
    	} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Requiscion seleccionada", e);
    	} finally {
			backtrace(this.listPreDetalles.size() + " detalles de Orden de Compra generados.");
    		if (this.isDebug)
				backtracePrint();
    	}
	}
	
	private Requisicion copiaRequisicion(Requisicion pojoTarget) {
		Requisicion pojoResult = new Requisicion();
		
		try {
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
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

	// -------------------------------------------------------------------------
	// BUSQUEDA PROVEEDORES
	// -------------------------------------------------------------------------
	
	public void nuevaBusquedaProveedores() {
		control();
		this.campoBusquedaProveedores = this.tiposBusquedaProveedores.get(0).getValue().toString();
		this.valorBusquedaProveedores = "";
		this.valorBusquedaTipoProveedor = "N";
		
		this.paginacionProveedores = 1;
		this.listProveedores = new ArrayList<PersonaExt>();
		this.pojoProveedor = null;
    }

	public void buscarProveedores() {
		try {
			control();
			this.valorBusquedaTipoProveedor = (! "".equals(this.valorBusquedaTipoProveedor) ? this.valorBusquedaTipoProveedor : "N");
			this.campoBusquedaProveedores = (! "".equals(this.campoBusquedaProveedores) ? this.campoBusquedaProveedores.trim() : this.tiposBusquedaProveedores.get(0).getValue().toString());
			
			backtrace("Buscando Proveedores. " + this.campoBusquedaProveedores + ":" + this.valorBusquedaProveedores);
    		this.paginacionProveedores = 1;
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listProveedores = this.ifzCotizaciones.findPersonaLikeProperty(this.campoBusquedaProveedores, this.valorBusquedaProveedores, this.valorBusquedaTipoProveedor, 0);
			if (this.listProveedores == null && this.listProveedores.isEmpty()) 
				control(2, "Busqueda de Proveedores sin resultados");
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Proveedores", e);
    	} finally {
			if (this.listProveedores == null) 
				this.listProveedores = new ArrayList<PersonaExt>();
			if (this.isDebug)
				backtracePrint();
    	}
	}

	public void seleccionarProveedor() throws Exception {
		try {
			control();
			if (this.pojoProveedor == null) {
				backtrace("El proveedor no pudo ser asignado, pojo nulo");
				control(-1, "ERROR INTERNO - Ocurrio un problema al recuperar el Proveedor seleccionado");
				return;
			}
			
			// Asigno el PROVEEDOR seleccionada y el tipo de persona de proveedor (Persona (P) o Negocio (N)) a la cotizacion
			this.pojoOrdenCotizacion.setIdProveedor(this.pojoProveedor.getId());
			this.pojoOrdenCotizacion.setNombreProveedor(this.pojoProveedor.getNombre());
			this.pojoOrdenCotizacion.setRfcProveedor(this.pojoProveedor.getRfc());
			this.pojoOrdenCotizacion.setTipoPersonaProveedor(this.valorBusquedaTipoProveedor);
			this.pojoOrdenCotizacion.setIdContacto(0L);
			this.pojoOrdenCotizacion.setNombreContacto("");
			
			// Recuperamos el contacto del proveedor seleccionado
			backtrace("Recuperando contacto para proveedor: " + this.pojoProveedor.getId() + " - " + this.pojoProveedor.getNombre());
			this.pojoContacto = this.ifzCotizaciones.findContactoByProveedor(this.pojoProveedor, this.valorBusquedaTipoProveedor);
			if (this.pojoContacto != null) {
				this.pojoOrdenCotizacion.setIdContacto(this.pojoContacto.getId());
				this.pojoOrdenCotizacion.setNombreContacto(this.pojoContacto.getNombre());
			} 
			
			comprobarOrdenImpuestos();
			nuevaBusquedaProveedores();
    	} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Proveedor seleccionado", e);
    	} finally {
    		if (this.isDebug)
				backtracePrint();
    	}
	}

	// -------------------------------------------------------------------------
	// BUSQUEDA EMPLEADOS
	// -------------------------------------------------------------------------
	
	public void nuevaBusquedaEmpleados() {
		control();
		this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();
		this.valorBusquedaEmpleados = "";
		
		this.paginacionEmpleados = 1;
		this.listEmpleados = new ArrayList<Empleado>();
		this.pojoEmpleado = null;
	}
	
	public void buscarEmpleados() {
		try {
			control();
			if (this.campoBusquedaEmpleados == null || "".equals(this.campoBusquedaEmpleados.trim()))
				this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();

			backtrace("Busqueda de Empleados. " + this.campoBusquedaEmpleados + ": " + this.valorBusquedaEmpleados);
    		this.paginacionEmpleados = 1;
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.listEmpleados = this.ifzEmpleados.findLikeProperty(this.campoBusquedaEmpleados, this.valorBusquedaEmpleados, false, false, null, 0);
			if (this.listEmpleados == null || this.listEmpleados.isEmpty()) 
				control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Empleados", e);
    	} finally {
			if (this.listEmpleados == null) 
				this.listEmpleados = new ArrayList<Empleado>();
			if (this.isDebug)
				backtracePrint();
    	}
	}

	public void seleccionarEmpleado() {
		try {
			control();
			if (this.pojoEmpleado == null || this.pojoEmpleado.getId() == null || this.pojoEmpleado.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Empleado seleccionado");
				return;
			}
				
			this.pojoOrden.setIdSolicita(this.ifzEmpleados.findByIdExt(this.pojoEmpleado.getId()));
			nuevaBusquedaEmpleados();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Empleado seleccionado.", e);
		} finally {
    		if (this.isDebug)
				backtracePrint();
    	}
	}

	// -------------------------------------------------------------------------
	// LOCAL STORAGE
	// -------------------------------------------------------------------------
	
	public String infoJson() {
		HashMap<String,String> map = new HashMap<String,String>();
		Gson gson = new Gson();

		map.put("usuario", String.valueOf(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId()));
		if (this.pojoObraBase != null)
			map.put("pojoObra", gson.toJson(this.pojoObraBase));
		if (this.pojoOrden != null)
			map.put("pojoOrden", gson.toJson(this.pojoOrden));
		if (this.pojoOrdenCotizacion != null)
			map.put("pojoCotizacion", gson.toJson(this.pojoOrdenCotizacion));
		if (this.pojoRequisicion != null)
			map.put("pojoRequisicion", gson.toJson(this.pojoRequisicion));
		if (this.listPreDetalles != null)
			map.put("listOrdenDetalles", gson.toJson(this.listPreDetalles));
		if (this.listCotDetalles != null)
			map.put("listCotDetalles", gson.toJson(this.listCotDetalles));
		
		return gson.toJson(map);
	}
	
	public void restoreJson() {
		
	}
	
	// -------------------------------------------------------------------------
    // Propiedades 
	// -------------------------------------------------------------------------

	public boolean getHasId() {
		return (this.pojoOrden != null && this.pojoOrden.getId() != null && this.pojoOrden.getId() > 0L);
	}
	
	public void setHasId(boolean value) {}

	public String getTitulo() {
		if (getHasId())
			return "Orden de Compra " + this.pojoOrden.getId() + (this.tipoMaestro == TipoMaestro.Administrativo ? " [ADMINISTRATIVA]" : "");
		return "Nueva Orden de Compra " + (this.tipoMaestro == TipoMaestro.Administrativo ? "[ADMINISTRATIVA]" : "");
	}
	
	public void setTitulo(String value) {}
	
	public String getObraBase() {
		if (this.pojoObraBase != null && this.pojoObraBase.getId() != null && this.pojoObraBase.getId() > 0L)
			return "<b>" + this.pojoObraBase.getId() + "</b> - " + this.pojoObraBase.getNombre();
		return "";
	}

	public void setObraBase(String value) {}

	public long getIdExplosionInsumos() {
		return this.idExplosionInsumos;
	}
	
	public void setIdExplosionInsumos(long value) {}
	
	public long getIdRequisicion() {
		if (this.pojoOrden != null && this.pojoOrden.getIdCotizacion() != null && this.pojoOrden.getIdCotizacion().getIdRequisicion() != null && this.pojoOrden.getIdCotizacion().getIdRequisicion().getId() != null && this.pojoOrden.getIdCotizacion().getIdRequisicion().getId() > 0L)
			return this.pojoOrden.getIdCotizacion().getIdRequisicion().getId();
		return 0;
	}
	
	public void setIdRequisicion(long value) {}

	public boolean getValidaCotizacionEdit() {
		if (this.ordenEditable) {
			if (this.pojoOrden != null && this.pojoOrden.getId() != null && this.pojoOrden.getId() > 0L)
				return false;
			return true;
		}
		
		return false;
	}
	
	public void setValidaCotizacionEdit(boolean value) {}
	
	public boolean getMaestroAdministrativo() {
		return (this.esAdministrativo && this.tipoMaestro == TipoMaestro.Administrativo);
	}
	
	public void setMaestroAdministrativo(boolean value) {}
	
	public String getOrdenCompraObra() {
		if (this.pojoOrden.getIdObra() != null && this.pojoOrden.getIdObra().getId() != null && this.pojoOrden.getIdObra().getId() > 0L)
			return this.pojoOrden.getIdObra().getId() + " - " + this.pojoOrden.getIdObra().getNombre();
		return "SIN OBRA";
	}
	
	public void setOrdenCompraObra(String value) {}

	public String getFolio() {
		if (this.pojoOrden != null && this.pojoOrden.getFolio() != null && ! "".equals(this.pojoOrden.getFolio().trim()))
			return "<b>" + this.pojoOrden.getFolio() + "</b>";
		return "<i>Se genera al guardar</i>";
	}
	
	public void setFolio(String value) {}
	
	public String getOrdenCompraCotizacion() {
		if (this.pojoOrden != null) {
			if (! this.origenRequisicion) {
				if (this.pojoOrden.getIdCotizacion() != null && this.pojoOrden.getIdCotizacion().getId() != null && this.pojoOrden.getIdCotizacion().getId() > 0L)
					return this.pojoOrden.getIdCotizacion().getId() + " - <b>" + this.pojoOrden.getIdCotizacion().getFolio() + "</b>"
							+ " - $ <i>" + (new DecimalFormat(this.patternDecimal)).format(this.pojoOrden.getIdCotizacion().getTotal()) + "</i> "
							+ this.pojoOrden.getIdCotizacion().getMoneda(); 
			} else {
				if (this.pojoOrdenCotizacion != null && this.pojoOrdenCotizacion.getId() != null)
					return " Por guardar - " + (new DecimalFormat(this.patternDecimal)).format(this.pojoOrdenCotizacion.getTotal());
			}
		}
		
		return "";
	}
	
	public void setOrdenCompraCotizacion(String value) {}

	public String getOrdenCompraRequisicion() {
		if (this.pojoOrden != null && this.origenRequisicion && this.pojoOrdenRequisicion != null) {
			if (this.pojoOrdenRequisicion.getId() != null && this.pojoOrdenRequisicion.getId() > 0L)
				return "<b>" + this.pojoOrdenRequisicion.getId() + "</b> - " + this.pojoOrdenRequisicion.getNombreSolicita();
		}
		
		return "";
	}
	
	public void setOrdenCompraRequisicion(String value) {}
	
	public String getCotizacionProveedor() {
		String cotizacionProveedor = "";
		
		if (this.pojoOrden != null) {
			if (! this.origenRequisicion) {
				if (this.pojoOrden.getIdCotizacion() != null && this.pojoOrden.getIdCotizacion().getIdProveedor() != null && this.pojoOrden.getIdCotizacion().getIdProveedor().getId() > 0L)
					cotizacionProveedor = this.pojoOrden.getIdCotizacion().getIdProveedor().getId() + " - " + this.pojoOrden.getIdCotizacion().getProveedorNombre() + " - <b>" + this.pojoOrden.getIdCotizacion().getIdProveedor().getRfc() + "</b>";
			} else {
				if (this.pojoOrdenCotizacion != null && this.pojoOrdenCotizacion.getIdProveedor() != null && this.pojoOrdenCotizacion.getIdProveedor() > 0L)
					cotizacionProveedor = this.pojoOrdenCotizacion.getIdProveedor() + " - " + this.pojoOrdenCotizacion.getNombreProveedor() + " - <b>" + this.pojoOrdenCotizacion.getRfcProveedor() + "</b>";
			}
		}
		
		return cotizacionProveedor;
	}
	
	public void setCotizacionProveedor(String value) {}

	public String getCotizacionContacto() {
		if (this.pojoOrden != null) {
			if (! this.origenRequisicion) {
				if (this.pojoOrden.getIdCotizacion() != null) {
					if (this.pojoOrden.getIdCotizacion().getIdContacto() != null && this.pojoOrden.getIdCotizacion().getIdContacto().getId() > 0L)
						return this.pojoOrden.getIdCotizacion().getIdContacto().getId() + " - " + this.pojoOrden.getIdCotizacion().getContactoNombre();
				}
			} else {
				if (this.pojoContacto != null && this.pojoContacto.getId() > 0L)
					return this.pojoContacto.getId() + " - " + this.pojoContacto.getNombre();
			}
		}
		
		return "Sin contacto";
	}
	
	public void setCotizacionContacto(String value) {}

	public String getCotizacionComprador() {
		if (this.pojoOrden != null) {
			if (! this.origenRequisicion) {
				if (this.pojoOrden.getIdCotizacion() != null) {
					if (this.pojoOrden.getIdCotizacion().getIdComprador() != null && this.pojoOrden.getIdCotizacion().getIdComprador().getId() > 0L)
						return this.pojoOrden.getIdCotizacion().getIdComprador().getId() + " - " + this.pojoOrden.getIdCotizacion().getCompradorNombre(); 
				}
			} else {
				if (this.pojoContacto != null && this.pojoContacto.getId() > 0L)
					return this.pojoContacto.getId() + " - " + this.pojoContacto.getNombre();
			}
		}
		
		return "Sin contacto";
	}
	
	public void setCotizacionComprador(String value) {}
	
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

	public long getIdOrdenCompra() {
		return idOrdenCompra;
	}

	public void setIdOrdenCompra(long idOrdenCompra) {
		this.idOrdenCompra = idOrdenCompra;
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
	
	public List<OrdenCompra> getListOrdenes() {
		return listOrdenes;
	}
	
	public void setListOrdenes(List<OrdenCompra> listOrdenes) {
		this.listOrdenes = listOrdenes;
	}
	
	public List<PreOrdenDetalle> getListOrdenDetalles() {
		return listPreDetalles;
	}
	
	public void setListOrdenDetalles(List<PreOrdenDetalle> listOrdenDetalles) {
		this.listPreDetalles = listOrdenDetalles;
	}
	
	public List<PreOrdenDetalle> getListOrdenDetallesEliminados() {
		return listPreDetallesEliminados;
	}
	
	public void setListOrdenDetallesEliminados(List<PreOrdenDetalle> listOrdenDetallesEliminados) {
		this.listPreDetallesEliminados = listOrdenDetallesEliminados;
	}
	
	public OrdenCompraExt getPojoOrden() {
		return pojoOrden;
	}

	public void setPojoOrden(OrdenCompraExt pojoOrden) {
		this.pojoOrden = pojoOrden;
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
	
	public List<SelectItem> getOpcionesBusquedaObra() {
		return opcionesBusquedaObra;
	}
	
	public void setOpcionesBusquedaObra(List<SelectItem> opcionesBusquedaObra) {
		this.opcionesBusquedaObra = opcionesBusquedaObra;
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
	
	public boolean isOperacion() {
		return operacion;
	}
	
	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
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

	public int getPaginacionPrincipal() {
		return paginacionPrincipal;
	}
	
	public void setPaginacionPrincipal(int paginacionPrincipal) {
		this.paginacionPrincipal = paginacionPrincipal;
	}
	
	public int getPaginacionDetalles() {
		return paginacionDetalles;
	}
	
	public void setPaginacionDetalles(int paginacionDetalles) {
		this.paginacionDetalles = paginacionDetalles;
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

	public int getPaginacionCotizaciones() {
		return paginacionCotizaciones;
	}

	public void setPaginacionCotizaciones(int paginacionCotizaciones) {
		this.paginacionCotizaciones = paginacionCotizaciones;
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
		if (this.seleccionarTodos == seleccionarTodos)
			return;

		this.seleccionarTodos = seleccionarTodos;
		if (this.listPreDetalles != null && ! this.listPreDetalles.isEmpty()) {
			for (PreOrdenDetalle var : this.listPreDetalles)
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

	public int getPaginacionEmpleados() {
		return paginacionEmpleados;
	}

	public void setPaginacionEmpleados(int paginacionEmpleados) {
		this.paginacionEmpleados = paginacionEmpleados;
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
		return ordenEditable;
	}

	public void setEditarOrdenCompra(boolean editarOrdenCompra) {
		this.ordenEditable = editarOrdenCompra;
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
	
	public void setTiposBusquedaRequisiciones(List<SelectItem> tiposBusquedaRequisiciones) {
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
	
	public int getPaginacionRequisiciones() {
		return paginacionRequisiciones;
	}
	
	public void setPaginacionRequisiciones(int paginacionRequisiciones) {
		this.paginacionRequisiciones = paginacionRequisiciones;
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

	public void setTiposBusquedaProveedores(List<SelectItem> tiposBusquedaProveedores) {
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

	public int getPaginacionProveedores() {
		return paginacionProveedores;
	}

	public void setPaginacionProveedores(int paginacionProveedores) {
		this.paginacionProveedores = paginacionProveedores;
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

	/*public Requisicion getPojoOrdenRequisicion() {
		return pojoOrdenRequisicion;
	}
	
	public void setPojoOrdenRequisicion(Requisicion pojoOrdenRequisicion) {
		this.pojoOrdenRequisicion = pojoOrdenRequisicion;
	}*/

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

	public List<OrdenCompraImpuestosExt> getListOrdenImpuestos() {
		return listOrdenImpuestos;
	}

	public void setListOrdenImpuestos(List<OrdenCompraImpuestosExt> listOrdenImpuestos) {
		this.listOrdenImpuestos = listOrdenImpuestos;
	}

	public double getTotalImpuestos() {
		return totalImpuestos;
	}
	
	public void setTotalImpuestos(double totalImpuestos) {
		this.totalImpuestos = totalImpuestos;
	}

	public double getTotalRetenciones() {
		return totalRetenciones;
	}
	
	public void setTotalRetenciones(double totalRetenciones) {
		this.totalRetenciones = totalRetenciones;
	}

	public boolean isHasImpuestosExtras() {
		return hasImpuestosExtras;
	}

	public void setHasImpuestosExtras(boolean hasImpuestosExtras) {
		this.hasImpuestosExtras = hasImpuestosExtras;
	}

	public boolean isHasImpuestos() {
		return (this.totalImpuestos > 0);
	}

	public void setHasRetenciones(boolean hasRetenciones) {}

	public boolean isHasRetenciones() {
		return (this.totalRetenciones > 0);
	}

	public void setHasImpuestos(boolean hasImpuestosExtras) {}
	
	public String getDescImpuestos() {
		return descImpuestos;
	}
	
	public void setDescImpuestos(String descImpuestos) {
		this.descImpuestos = descImpuestos;
	}
	
	public String getDescRetenciones() {
		return descRetenciones;
	}
	
	public void setDescRetenciones(String descRetenciones) {
		this.descRetenciones = descRetenciones;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}
	
	public long getMoneda() {
		return (this.pojoOrden != null ? this.pojoOrden.getIdMoneda() : 0L);
	}
	
	public void setMoneda(long idMoneda) {
		if (this.pojoOrden == null)
			return;
		if (this.listMonedas == null || this.listMonedas.isEmpty())
			return;
		
		this.pojoOrden.setIdMoneda(idMoneda);
		this.pojoOrden.setMoneda("");
		for (Moneda moneda : this.listMonedas) {
			if (idMoneda == moneda.getId()) {
				this.pojoOrden.setMoneda(moneda.getAbreviacion());
				break;
			}
		}
	}
}
