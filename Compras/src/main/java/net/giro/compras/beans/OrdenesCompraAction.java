package net.giro.compras.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraImpuestosRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.inventarios.comun.TipoMaestro;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.beans.SubfamiliaImpuestosExt;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
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
@ManagedBean(name="ordenAction")
public class OrdenesCompraAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OrdenesCompraAction.class);
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
	// Impuestos IVA
	private ConGrupoValoresRem ifzGpoVal;
	private ConGrupoValores pojoGpoImpuestos;
	private ConValoresRem ifzConValores;
	private List<ConValores> listImpuestos;
	private List<SelectItem> listImpuestosItems;
	private long idImpuesto;
	private double porcentajeIva;
	// Listas
	private List<OrdenCompra> listOrdenes;
	private List<OrdenCompraDetalleExt> listOCDetalles;
	private List<PreOrdenDetalle> listOrdenDetalles;
	private List<PreOrdenDetalle> listOrdenDetallesEliminados;
	private List<PreOrdenDetalle> listOrdenDetallesOriginales;
	// POJO's
	private ObraExt pojoObra;
	private OrdenCompraExt pojoOrden;
	private long ordenCompraId;
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
	private int tipoMensaje;
	private String mensaje;
	private List<String> backtrace;
    private long usuarioId;
    private String usuario;
	private int numPaginaOrdenes;
	private int numPaginaDetalles;
	private double subtotal;
	private double iva;
	private double total;
    private boolean permiteModificar;
    private boolean editarOrdenCompra;
	private boolean seleccionarTodos;
	private int estatusOrdenCompra;
	private boolean esAdministrativo;
	private TipoMaestro tipoMaestro;
	private boolean origenRequisicion;
	private List<SelectItem> listEstatusOrdenCompraItems;
	private String patternNumber;
	private String patternDecimal;
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
	private double tipoCambioOriginal;
	private boolean evaluaConversion;
	private boolean reConvierte;
	// Orden de Compra Detalle Impuestos
	private OrdenCompraImpuestosRem ifzOrdenImpuestos;
	private List<OrdenCompraImpuestosExt> listOrdenImpuestos;
	private List<OrdenCompraImpuestosExt> listOrdenImpuestosBorrados;
	private boolean hasImpuestosExtras;
	private String descImpuestos;
	private double totalImpuestos;
	private String descRetenciones;
	private double totalRetenciones;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public OrdenesCompraAction() {
		PropertyResourceBundle msgProperties = null;
		ValueExpression valExp = null;
		Moneda valMoneda = null;
		FacesContext fc = null;
		Application app = null;
		String valPerfil = "";
		long valGrupo = 0;
		
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
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey(), item.getValue());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			// Conexion con modulos
			this.ctx = new InitialContext();
			this.ifzOrdenes = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOrdenDetalles = (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzOrdenImpuestos = (OrdenCompraImpuestosRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraImpuestosFac!net.giro.compras.logica.OrdenCompraImpuestosRem");
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
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			
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
			this.ifzSubfamliaImto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			
			/* VALIDACION DE PERFILES */
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.esAdministrativo = ((valPerfil != null && "S".equals(valPerfil.trim())) ? true : false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			valMoneda = this.ifzMonedas.findByAbreviacion(valPerfil);
			if (valMoneda != null) {
				this.idMonedaSugerida = valMoneda.getId();
				this.idMonedaBase = valMoneda.getId();
				valMoneda = null;
			}
			
			// Impuestos IVA
			valPerfil = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			this.porcentajeIva = ((valPerfil != null && ! "".equals(valPerfil.trim())) ? Double.valueOf(valPerfil) : 0);
			if (this.entornoProperties.getString("SYS_IMPTOS") == null || "".equals(this.entornoProperties.getString("SYS_IMPTOS")))
				throw new Exception("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
			valGrupo = Long.valueOf(this.entornoProperties.getString("SYS_IMPTOS"));
			this.pojoGpoImpuestos = this.ifzGpoVal.findById(valGrupo);
			cargarImpuestosIva();
			asignarImpuesto(this.porcentajeIva);
			
			// Inicializaciones
			this.listObrasGrid = new ArrayList<Obra>();
			this.listOrdenes = new ArrayList<OrdenCompra>();
			this.listOrdenDetalles = new ArrayList<PreOrdenDetalle>();
			this.listObras = new ArrayList<ObraExt>();
			this.listOrdenImpuestos = new ArrayList<OrdenCompraImpuestosExt>();
			this.listOrdenImpuestosBorrados = new ArrayList<OrdenCompraImpuestosExt>();
			this.pojoOrden = new OrdenCompraExt();
			this.numPaginaOrdenes = 1;
			this.numPaginaDetalles = 1;
			this.tipoMaestro = TipoMaestro.Inventario;
			this.patternNumber = "###,###,##0";
			this.patternDecimal = "###,###,##0.000";
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("nombre", "Nombre Obra"));
			this.tiposBusqueda.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusqueda.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusqueda.add(new SelectItem("nombreResponsable", "Encargado"));
			this.tiposBusqueda.add(new SelectItem("oc.folio", "Folio Orden Compra"));
			this.tiposBusqueda.add(new SelectItem("oc.nombreProveedor", "Proveedor Orden Compra"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
			
			// Busqueda Cotizaciones
			this.tiposBusquedaCotizaciones = new ArrayList<SelectItem>();
			this.tiposBusquedaCotizaciones.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaCotizaciones.add(new SelectItem("folio", "Folio"));
			this.tiposBusquedaCotizaciones.add(new SelectItem("nombreProveedor", "Proveedor"));
			this.tiposBusquedaCotizaciones.add(new SelectItem("nombreComprador", "Comprador"));
			this.tiposBusquedaCotizaciones.add(new SelectItem("id", "ID"));
			nuevaBusquedaCotizaciones();
			
			// Busqueda requisiciones
			this.tiposBusquedaRequisiciones = new ArrayList<SelectItem>();
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
			this.tiposBusquedaEmpleados.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaEmpleados.add(new SelectItem("clave", "Clave"));
			this.tiposBusquedaEmpleados.add(new SelectItem("id", "ID"));
			nuevaBusquedaEmpleados();
			
			// Estatus Orden Compra
			this.listEstatusOrdenCompraItems = new ArrayList<SelectItem>();
			this.listEstatusOrdenCompraItems.add(new SelectItem(0, msgProperties.getString("estatus.activa")));
			this.listEstatusOrdenCompraItems.add(new SelectItem(1, msgProperties.getString("estatus.cancelada")));
			this.listEstatusOrdenCompraItems.add(new SelectItem(2, msgProperties.getString("estatus.suministrada")));
			this.estatusOrdenCompra = (int) this.listEstatusOrdenCompraItems.get(0).getValue();
			
			cargarMonedas();
			backtrace();
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		List<Object> listObject = new ArrayList<Object>();
		List<OrdenCompra> listOrdenes = null;
		String campo = "";
		String valor = "";
		String orderBy = "";
		
		try {
			control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.listObrasGrid = new ArrayList<Obra>();

			campo = this.campoBusqueda;
			valor = this.valorBusqueda.trim().replace(" ", "%");
			campo = (campo.contains("oc.") && ! "".equals(valor)) ? campo : "*";
			if (campo.contains("oc.")) {
				log.info("Buscamos obras por el folio de Orden de Compra");
				this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
				listOrdenes = this.ifzOrdenes.findLikeProperty(campo.substring(3), valor, 0L, 0, false, true, false, "", 0);
				if (listOrdenes != null && ! listOrdenes.isEmpty()) {
					for (OrdenCompra var : listOrdenes) 
						listObject.add(var.getIdObra());
					this.listObrasGrid = this.ifzObras.findInProperty("id", listObject, "CASE model.estatus WHEN 0 THEN 1 ELSE 0 END, model.nombre");
					listObject.clear();
				}
			} else {
				orderBy = "CASE model.estatus WHEN 0 THEN 1 ELSE 0 END";
				if (campo.contains("*"))
					orderBy += ", model.nombre";
				else
					orderBy += ", " + campo;
				log.info("Buscando obras. " + this.campoBusqueda + ":" + this.valorBusqueda);
				this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
				this.listObrasGrid = this.ifzObras.findLikeProperty(campo, valor, this.esAdministrativo, orderBy, 0);
			}
			
			if (this.listObrasGrid == null || this.listObrasGrid.isEmpty()) {
				log.info("Error 2 - Busqueda sin resultados");
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la Busqueda de Obras", e);
			if (this.listObrasGrid != null) 
				this.listObrasGrid.clear();
    	} finally {
    		if (this.listObrasGrid != null && ! this.listObrasGrid.isEmpty())
    			log.info(this.listObrasGrid.size() + " resultados encontrados.");
    		this.numPagina = 1;
    		this.numPaginaOrdenes = 1;
    		this.estatusOrdenCompra = 0;
    	}
	}

	public void ver() {
		try {
			control();
			backtrace("Proceso para Listar Ordenes de Compras ... ");
			log.info("Obra seleccionada: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
			this.permiteModificar = true;
			if (this.pojoObra.getEstatus().longValue() == 0) 
				this.permiteModificar = false;
			
			this.listOrdenes = new ArrayList<OrdenCompra>();
			this.tipoMaestro = (this.pojoObra.isAdministrativa() ? TipoMaestro.Administrativo : TipoMaestro.Inventario);
			
			// Recuperamos las Ordenes de Compra para esa obra
			log.info("Cargo Ordenes de Compra ... ");
			if (this.pojoObra.getId() != null && this.pojoObra.getId() > 0L) {
				log.info("Recuperando OCs por obra: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
				this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
				this.listOrdenes = this.ifzOrdenes.findByObra(this.pojoObra.getId(), this.estatusOrdenCompra, true, null, 0); 
			}
			backtrace("Proceso terminado");
    	} catch (Exception e) {
			control("Ocurrio un problema al recuperar las Ordenes de Compra de la Obra seleccionada", e);
    	} finally {
    		this.numPaginaOrdenes = 1;
    		this.numPaginaDetalles = 1;
			if (this.listOrdenes == null)
				this.listOrdenes = new ArrayList<OrdenCompra>();
			log.info(this.listOrdenes.size() + " Ordenes de Compra encontradas.");
    	}
	}

	public void nuevoSinObra() {
		this.pojoObra = null;
		this.nuevo();
	}
	
	public void nuevoCotizacion() {
		this.origenRequisicion = false;
	}
	
	public void nuevoRequisicion() {
		this.origenRequisicion = true;
	}
	
	public void nuevo() {
		try {
			control();
			backtrace("Proceso para Crear una Orden de Compra ... ");
			this.ordenCompraId = 0L;
			this.origenRequisicion = false;
			this.editarOrdenCompra = true;
			this.evaluaConversion = false;
			this.reConvierte = false;
			this.pojoCotizacion = null;
			this.pojoProveedor = null;
			this.pojoContacto = null;
			this.pojoRequisicion = null;
			this.pojoRequisicionSeleccionada = null;
			this.idMonedaActual = this.idMonedaSugerida;
			this.idMonedaOriginal = 0;
			this.listOrdenDetalles = new ArrayList<PreOrdenDetalle>();
			this.listOrdenDetallesEliminados = new ArrayList<PreOrdenDetalle>();
			this.listOrdenImpuestos = new ArrayList<OrdenCompraImpuestosExt>();
			this.listOrdenImpuestosBorrados = new ArrayList<OrdenCompraImpuestosExt>();
			
			this.pojoOrden = new OrdenCompraExt();
			if (this.pojoObra != null && this.pojoObra.getId() > 0L) {
				this.pojoOrden.setIdObra(this.pojoObra);
				this.pojoOrden.setLugarEntrega(this.pojoObra.getNombre());
			}
			
			this.seleccionarTodos = false;
			backtrace("Proceso terminado");
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar generar una nueva Orden de Compra", e);
    	} finally {
    		this.numPaginaDetalles = 1;
			totalizarDetalles();
    	}
	}

	public void editar() {
		try {
			control();
			if (this.ordenCompraId <= 0L) {
				control(-1, "Debe indicar una Orden de Compra");
				return;
			}
			
			backtrace("Proceso para Editar Orden de Compra ... ");
			this.editarOrdenCompra = true;
			this.evaluaConversion = false;
			this.reConvierte = false;
			this.pojoOrden = this.ifzOrdenes.findExtById(this.ordenCompraId);
			asignarImpuesto(this.pojoOrden.getPorcentajeIva());
			//this.origenRequisicion = (this.pojoOrden.getBase() == 1);
			if (this.pojoOrden.getEstatus() == 2)
				this.editarOrdenCompra = false;
			this.listOrdenImpuestos = new ArrayList<OrdenCompraImpuestosExt>();
			this.listOrdenImpuestosBorrados = new ArrayList<OrdenCompraImpuestosExt>();
			
			// Obtengo la moneda de la Orden de Compra
			this.idMonedaActual = this.pojoOrden.getIdMoneda();
			this.idMonedaOriginal = this.pojoOrden.getIdMoneda();
			this.tipoCambioOriginal = (this.pojoOrden.getTipoCambio().doubleValue() > 0 ? this.pojoOrden.getTipoCambio().doubleValue() : 1.0);
			
			// detalles desde la orden de compra 
			this.recuperarListadoDetalles(true);
			
			this.nuevaBusquedaCotizaciones();
			this.nuevaBusquedaObras();
			this.seleccionarTodos = false;
			backtrace("Proceso terminado");
    	} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Orden de Compra", e);
    	}
	}
	
	public void guardar() {
		OrdenCompra orden = null;
		List<OrdenCompraDetalle> listAux = null;
		OrdenCompraDetalleExt pojoDetalle = null;
		boolean isNew = false;
		
		try {
			control();
			if (! validacionOrden()) 
				return;

			backtrace("Comprobando origen por requisicion y guardando requisicion si corresponde");
			if (this.origenRequisicion && ! guardarCotizacion())
				return;
			
			//seleccionados = 0;
			this.pojoOrden.setSubtotal(this.subtotal);
			this.pojoOrden.setIva(this.iva);
			this.pojoOrden.setTotal(this.total);
			this.pojoOrden.setTipo(this.tipoMaestro.ordinal());
    		this.pojoOrden.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());

			// Guardamos/Actualizamos en la BD
			this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
			if (this.pojoOrden.getId() == null || this.pojoOrden.getId() <= 0L) {
				isNew = true;
				backtrace("Guardando OC nueva");
				this.pojoOrden.setCreadoPor(this.usuarioId);
				this.pojoOrden.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoOrden.setModificadoPor(this.usuarioId);
				this.pojoOrden.setFechaModificacion(Calendar.getInstance().getTime());

				this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoOrden.setId(this.ifzOrdenes.save(this.pojoOrden));
				this.ordenCompraId = this.pojoOrden.getId();
				backtrace("OC guardada");
			} else {
				isNew = false;
				backtrace("Actualizando OC");
				this.pojoOrden.setModificadoPor(this.usuarioId);
				this.pojoOrden.setFechaModificacion(Calendar.getInstance().getTime());

				this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzOrdenes.update(this.pojoOrden);
				backtrace("OC actualizada");
			}
			
			// Guardamos los detalles nuevos y modificados listOrdenDetalles
			this.listOCDetalles = new ArrayList<OrdenCompraDetalleExt>();
			if (this.listOrdenDetalles != null && ! this.listOrdenDetalles.isEmpty()) {
				for (PreOrdenDetalle var : this.listOrdenDetalles) {
					if (! var.isSeleccionado()) 
						continue;
					
					// Asignamos la cotizacion al detalle
					backtrace("Generando OCDetalle de producto seleccionado");
					pojoDetalle = var.getOrdenCompraDetalle();
					pojoDetalle.setIdOrdenCompra(this.pojoOrden);
					pojoDetalle.setFechaModificacion(Calendar.getInstance().getTime());
					pojoDetalle.setModificadoPor(this.usuarioId);
					
					// Guardamos el detalle
					if (pojoDetalle.getId() == null || pojoDetalle.getId() <= 0L) {
						pojoDetalle.setFechaCreacion(Calendar.getInstance().getTime());
						pojoDetalle.setCreadoPor(this.usuarioId);
						pojoDetalle.setFechaModificacion(Calendar.getInstance().getTime());
						pojoDetalle.setModificadoPor(this.usuarioId);
					} 
					
					// Añadimos a listado de detalles
					this.listOCDetalles.add(pojoDetalle);
				}

				backtrace("Guardando OCDetalles");
    			this.ifzOrdenDetalles.setInfoSesion(this.loginManager.getInfoSesion());
				this.listOCDetalles = this.ifzOrdenDetalles.saveOrUpdateListExt(this.listOCDetalles);

				this.listOrdenDetalles.clear();
				for (OrdenCompraDetalleExt var : this.listOCDetalles)
					this.listOrdenDetalles.add(new PreOrdenDetalle(var));
				backtrace("OCDetalles guardados");
			}
			
			// Guardamos impuestos si corresponde
			backtrace("Guardando impuestos de Orden de Compra ... ");
			if (this.listOrdenImpuestos != null && ! this.listOrdenImpuestos.isEmpty()) {
				for (OrdenCompraImpuestosExt imp : this.listOrdenImpuestos) {
					imp.setIdOrdenCompra(this.pojoOrden);
					if (imp.getId() == null || imp.getId() <= 0L) {
						imp.setCreadoPor(this.usuarioId);
						imp.setFechaCreacion(Calendar.getInstance().getTime());
					} 
					
					imp.setModificadoPor(this.usuarioId);
					imp.setFechaModificacion(Calendar.getInstance().getTime());
				}

    			this.ifzOrdenImpuestos.setInfoSesion(this.loginManager.getInfoSesion());
    			this.ifzOrdenImpuestos.saveOrUpdateListExt(this.listOrdenImpuestos);
			}

			// Borramos los detalles de la OC si corresponde
			if (this.listOrdenDetallesEliminados != null && ! this.listOrdenDetallesEliminados.isEmpty()) {
				backtrace("Borramos detalles previamente eliminados");
				listAux = new ArrayList<OrdenCompraDetalle>();
				for (PreOrdenDetalle var : this.listOrdenDetallesEliminados) {
					if (var.getId() != null && var.getId() > 0L) {
						listAux.add(this.ifzOrdenDetalles.findById(var.getOrdenCompraDetalle().getId()));
						this.ifzOrdenDetalles.delete(var.getId());
					}
				}
				
				// Limpiamos la lista de eliminados
				// TODO: BACK OFFICE Eliminados
				this.listOrdenDetallesEliminados.clear();
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
			
			// Actualizo los precios de producto de esta orden de compra y las cantidad en COtizacion y Requisicion (si corresponde)
			orden = this.ifzOrdenes.findById(this.ordenCompraId);
			if (isNew) {
				this.listOrdenes.add(0, orden);
				this.ifzOrdenes.backOfficeOrdenCompra(this.ordenCompraId);
			} else {
				for (OrdenCompra var : this.listOrdenes) {
					if (var.getId().longValue() == this.ordenCompraId) {
						//index = this.listOrdenes.indexOf(var);
						this.listOrdenes.set(this.listOrdenes.indexOf(var), orden);
						break;
					}
				}
			}
			
			/*// Buscamos la OC en el listado
			int index = -1;
			if (! isNew) {
				for (OrdenCompra var : this.listOrdenes) {
					if (var.getId().longValue() == this.pojoOrden.getId().longValue()) {
						//index = this.listOrdenes.indexOf(var);
						this.listOrdenes.set(this.listOrdenes.indexOf(var), this.ifzOrdenes.findById(this.ordenCompraId));
						break;
					}
				}
			}

			this.ordenCompraId = this.pojoOrden.getId();
			orden = this.ifzOrdenes.findById(this.ordenCompraId);
			if (index < 0)
				this.listOrdenes.add(0, orden);
			else
				this.listOrdenes.set(index, orden);*/
			
			// Actualizamos la obra elegida si corresponde
			isNew = false;
			if (this.pojoObra == null)
				this.pojoObra = this.pojoOrden.getIdObra().Copia();
			this.pojoOrden.setFolio(orden.getFolio());
			this.pojoOrden.setConsecutivoProveedor(orden.getConsecutivoProveedor());
			if (this.isDebug)
				backtracePrint();
			backtrace();

			/*
			 * COMENTADO POR GUARDAR_SIN_CERRAR
			// Actualizamos el listado
			this.origenRequisicion = false;
			this.pojoOrden = new OrdenCompraExt();
			this.pojoOrdenBorrar = null;
			this.pojoRequisicionSeleccionada = null;
			this.ver();
			 */
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Orden de Compra actual", e);
    	}
	}

	public void autorizar() {
		try {
			control();
			if (this.ordenCompraId <= 0L)
				return;
			/*if (this.pojoOrdenAutorizar == null || this.pojoOrdenAutorizar.getId() == null || this.pojoOrdenAutorizar.getId() <= 0L)
				return;*/
			
			// Actualizamos el elemento de la lista
			this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
			for (OrdenCompra var : this.listOrdenes) {
				if (var.getId().longValue() == this.ordenCompraId) { //this.pojoOrdenAutorizar.getId()) {
					if (this.isDebug) 
						log.info("Autorizando OC");
					var.setAutorizado(1);
					var.setFechaAutorizacion(Calendar.getInstance().getTime());
					var.setIdUsuarioAutorizo(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					
					// Actualizamos el pojo de la BD
					this.ifzOrdenes.update(var);
					log.info("OC Autorizada");
					break;
				}
			}

			//this.pojoOrdenBorrar = null;
			//this.pojoOrdenAutorizar = null;
			this.pojoOrden = new OrdenCompraExt();
			this.ver();
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar autorizar la Orden de Compra", e);
    	}
	}
	
	public void eliminar() {
		Respuesta respuesta = null;
		
		try {
			control();
			backtrace("Proceso para Cancelar Orden de Compra ... ");
			if (this.ordenCompraId <= 0L) { //this.pojoOrdenBorrar == null || this.pojoOrdenBorrar.getId() == null || this.pojoOrdenBorrar.getId() <= 0L) {
				control(-1, "Debe indicar una Orden de Compra para Cancelar");
				backtrace("Debe indicar una Orden de Compra para Cancelar");
				return;
			}
			
			// Establecemos los atributos para la cancelacion
			backtrace("Cancelando OC");
			respuesta = this.ifzOrdenes.cancelar(this.ordenCompraId, this.usuarioId);
			if (respuesta != null && respuesta.getErrores().getCodigoError() != 0L) {
				backtrace(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError());
				return;
			}
			
			backtrace("OC Cancelada (Actualizada)");
			if (this.isDebug)
				backtracePrint();

			this.pojoOrden = new OrdenCompraExt();
			backtrace("Proceso terminado");
			this.ver();
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar la Orden de Compra", e);
    	}
	}
	
	public void eliminarDetalle() {
		try {
			control();
			// Quitamos el detalle de la lista
			if (this.isDebug) 
				log.info("Borrando producto listado");
			this.listOrdenDetalles.remove(this.pojoOrdenDetalleBorrar);
			this.listOrdenDetallesEliminados.add(this.pojoOrdenDetalleBorrar);
			this.pojoOrdenDetalleBorrar = null;
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar eiminar el producto seleccionado de la Orden de Compra", e);
    	}
	}
	
	public void reporte() {
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			if (this.ordenCompraId > 0L) { //this.pojoOrdenAutorizar != null && this.pojoOrdenAutorizar.getId() != null && this.pojoOrdenAutorizar.getId() > 0L) {
				backtrace("Imprimiento reporte Orden de Compra ... ");
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idOrdenCompra", this.ordenCompraId);

				// Parametros para la ejecucion del reporte
				params = new HashMap<String, String>();
				params.put("idPrograma", 	  this.entornoProperties.getString("REPORTE_ORDEN_COMPRA"));
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);

				respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					log.error("ERROR INTERNO - " + respuesta.getErrores().getDescError());
					control(-1, "Ocurrio un problema al intentar imprimir la Orden de Compra\nError " + respuesta.getErrores().getCodigoError());
					return;
				} 
				
				contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
				formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
				nombreDocumento = "OC-" + this.ordenCompraId + "." + formatoDocumento;
				
				if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
					log.error("ERROR INTERNO - No se recupero el contenido de la Orden de Compra");
					control(-1, "Ocurrio un problema al intentar imprimir la Orden de Compra");
					return;
				}
				
				this.httpSession.setAttribute("contenido", contenidoDocumento);
				this.httpSession.setAttribute("formato", formatoDocumento); 	
				this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
				backtrace("Reporte Orden de Compra lanzado. Proceso terminado!");
			}

			if (this.isDebug)
				backtracePrint();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la Orden de Compra", e);
		} finally {
			//this.pojoOrdenBorrar = null;
			//this.pojoOrdenAutorizar = null;
			this.pojoOrden = new OrdenCompraExt();
		}
	}
	
	private boolean guardarCotizacion() {
		try {
			backtrace("Guardando cotizacion ... ");
			if (this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				backtrace("Comprobando proveedor ... ");
				if (this.pojoProveedor == null || this.pojoProveedor.getId() <= 0L) {
					control(-1, "No se detecto ningun Proveedor seleccionado");
					return false;
				}
				
				// Guardo otizacion
				backtrace("Guardando cotizacion ... ");
				this.pojoCotizacion.setSubtotal(this.subtotal);
				this.pojoCotizacion.setIva(this.iva);
				this.pojoCotizacion.setTotal(this.total);
				this.pojoCotizacion.setTiempoEntrega(this.pojoOrden.getTiempoEntrega());
				this.pojoCotizacion.setTipo(this.tipoMaestro.ordinal());
	    		this.pojoCotizacion.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
				this.pojoCotizacion.setCreadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoCotizacion.setModificadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoCotizacion.setSistema(1);
				
				// Inserto en la BD
				this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoCotizacion.setId(this.ifzCotizaciones.save(this.pojoCotizacion));
				backtrace("Cotizacion guardada");
				
				// Detalles COTIZACION
				backtrace("Guardando detalles de cotizacion ... ");
				CotizacionExt coti = this.ifzCotizaciones.convertir(this.pojoCotizacion);
				if (this.listCotDetalles != null && ! this.listCotDetalles.isEmpty()) {
					for (CotizacionDetalleExt var : this.listCotDetalles) {
						var.setIdCotizacion(coti);
						var.setCreadoPor(this.usuarioId);
						var.setFechaCreacion(Calendar.getInstance().getTime());
						var.setModificadoPor(this.usuarioId);
						var.setFechaModificacion(Calendar.getInstance().getTime());
					}

					this.listCotDetalles = this.ifzCotiDetalles.saveOrUpdateListExt(this.listCotDetalles);
				}
				backtrace("Detalles de cotizacion guardados.");
				
				backtrace("Asignando cotizacion a Orden de Compra ... ");
				this.pojoOrden.setIdCotizacion(coti);
				this.pojoOrden.setBase(1);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar la Orden de Compra desde Requisicion", e);
			return false;
		}
		
		return true;
	}
	
	private void recuperarListadoDetalles(boolean fromOrdenCompra) {
		List<OrdenCompraDetalleExt> lista = null;
		PreOrdenDetalle det = null;
		
		try {
			control();
			this.listOrdenDetalles = new ArrayList<PreOrdenDetalle>();
			this.listOrdenDetallesEliminados = new ArrayList<PreOrdenDetalle>();
			if (this.pojoOrden == null && this.pojoOrden.getIdCotizacion() == null)
				return;

			this.ifzOrdenDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			if (fromOrdenCompra) {
				// Recuperamos los detalles de la orden de compra
				log.info("Recuperamos detalles por OC: " + this.pojoOrden.getId());
				lista = this.ifzOrdenDetalles.findExtByProperty("idOrdenCompra", this.pojoOrden.getId(), 0);
				if (lista != null && ! lista.isEmpty()) {
					for (OrdenCompraDetalleExt var : lista)
						this.listOrdenDetalles.add(new PreOrdenDetalle(var));
				}
			} else if (! this.origenRequisicion) {
				// Recuperamos los detalles de la cotizacion
				log.info("Recuperamos detalles de Cotizacion: " + this.pojoOrden.getIdCotizacion().getId());
				this.seleccionarTodos = false;
				this.listCotDetalles = this.ifzCotiDetalles.findExtByProperty("idCotizacion", this.pojoOrden.getIdCotizacion().getId(), 0);
				if (this.listCotDetalles != null && ! this.listCotDetalles.isEmpty()) {
					for (CotizacionDetalleExt var : this.listCotDetalles) {
						if (var.getEstatus() == 1) 
							continue;
						this.listOrdenDetalles.add(new PreOrdenDetalle(var));
					}
				}
			} else if (this.origenRequisicion) {
				// Recuperamos los detalles de la cotizacion proveniente de una Requisicion
				log.info("Recuperamos detalles de Requisicion (Cotizacion): " + this.pojoRequisicion.getId());
				this.seleccionarTodos = true;
				if (this.listCotDetalles != null && ! this.listCotDetalles.isEmpty()) {
					for (CotizacionDetalleExt var : this.listCotDetalles) {
						if (var.getEstatus() == 1) 
							continue;
						
						det = new PreOrdenDetalle(var);
						det.setSeleccionado(true);
						this.listOrdenDetalles.add(det);
					}
				}
			}

			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			this.listOrdenDetallesOriginales = new ArrayList<PreOrdenDetalle>();
			for (PreOrdenDetalle item : this.listOrdenDetalles) {
				det = new PreOrdenDetalle();
				BeanUtils.copyProperties(det, item);
				this.listOrdenDetallesOriginales.add(det);
			}
			
			// Comprobamos impuestos de los detalles
			comprobarOrdenImpuestos(); //comprobarImpuestosProducto();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los productos para la Orden de Compra", e);
		} finally {
			this.numPaginaDetalles = 1;
			this.totalizarDetalles();
		}
	}
	
	public void totalizarDetalles() {
		double impuesto = 0;
		
		try {
			// Inicializamos
			log.info("totalizarDetalles: inicializando ...");
			this.subtotal = 0;
			this.iva = 0;
			this.total = 0;
			this.totalImpuestos = 0;
			this.totalRetenciones = 0;
			if (this.listOrdenDetalles == null)
				this.listOrdenDetalles = new ArrayList<PreOrdenDetalle>();
	
			log.info("Totalizamos detalles ... ");
			impuesto = this.porcentajeIva / 100;
			for (PreOrdenDetalle detalle : this.listOrdenDetalles) {
				if (! detalle.isSeleccionado()) 
					continue;
				this.subtotal += detalle.getImporte();
				this.iva += detalle.getImporte() * impuesto;
			}
	
			// Calculo total
			this.total = this.subtotal + (this.iva - this.totalRetenciones) - this.totalImpuestos; 
			
			// Comprueba por impuestos adicionales y recalculo total si corresponde
			if (this.subtotal > 0 && this.listOrdenImpuestos != null && ! this.listOrdenImpuestos.isEmpty()) {
				log.info("Añadimos los impuestos y retenciones de los productos ... ");
				impuesto = 0;
				for (OrdenCompraImpuestosExt imp : this.listOrdenImpuestos) {
					if ("DE".equals(imp.getIdImpuesto().getTipoCuenta())) {
						this.descImpuestos = imp.getDescripcion();
						impuesto = imp.getPorcentaje();
						if (impuesto > 0)
							impuesto = (impuesto > 1) ? impuesto / 100 : impuesto; 
						impuesto = this.subtotal * impuesto; //((this.subtotal * Double.valueOf(imp.getIdImpuesto().getAtributo1())) / 100);
						imp.setMonto(impuesto);
						imp.setAplicaEn(1);
						this.totalImpuestos += impuesto;
					} else if ("AC".equals(imp.getIdImpuesto().getTipoCuenta())) {
						this.descRetenciones = imp.getDescripcion();
						impuesto = imp.getPorcentaje();
						if (impuesto > 0)
							impuesto = (impuesto > 1) ? impuesto / 100 : impuesto; 
						imp.setMonto(impuesto);
						imp.setAplicaEn(0);
						this.totalRetenciones += impuesto;
					}

					this.total = this.subtotal + (this.iva - this.totalRetenciones) + this.totalImpuestos; 
					if (imp.getAplicaEn() == 0) 
						this.total = this.subtotal + (this.iva - this.totalRetenciones) - this.totalImpuestos; 
				} 
			}
		    
			comprobarSeleccionarTodos();
			log.info("Totalizando terminado!");
		} catch (Exception e) {
			control("Ocurrio un problema al calcular el Total de la Orden de Compra", e);
		}
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
					this.listMonedasItems.add(new SelectItem(var.getId(), var.getNombre() + " (" + var.getAbreviacion() + ")"));
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar las Monedas", e);
		}
	}
	
	private boolean validacionOrden() {
		boolean valido = false;
		
		if (this.evaluaConversion) {
			backtrace("VALIDACION: Conversion pendiente");
			control(-1, "Hay una conversion pendiente.\nPresione el boton 'Realizar Conversion' y despues presione Guardar");
			return false;
		}

		backtrace("Validando OC");//listOrdenDetalles
		if (this.listOrdenDetalles == null || this.listOrdenDetalles.isEmpty()) {
			backtrace("VALIDACION: ERROR 4 - Lista de productos vacia");
			control(4, "Lista de productos vacia");
			return false;
		}
		
		for (PreOrdenDetalle var : this.listOrdenDetalles) {
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
		if (this.listOrdenDetalles != null && ! this.listOrdenDetalles.isEmpty()) {
			this.seleccionarTodos = true;
			for (PreOrdenDetalle var : this.listOrdenDetalles) {
				if (! var.isSeleccionado()) {
					this.seleccionarTodos = false;
					break;
				}
			}
		}
	}

	private void comprobarOrdenImpuestos() {
		List<SubfamiliaImpuestosExt> listImpuestosAux = null;
		OrdenCompraImpuestosExt pojoOrdenImpuesto = null;
		String tipoPersonaProveedor = "";
		
		try {
			if (this.listOrdenDetalles == null || this.listOrdenDetalles.isEmpty())
				return;
			
			if (this.listOrdenImpuestos == null)
				this.listOrdenImpuestos = new ArrayList<OrdenCompraImpuestosExt>();
			this.listOrdenImpuestos.clear();

			tipoPersonaProveedor = this.pojoOrden.getTipoPersonaProveedor();
			if (this.origenRequisicion) {
				if (this.pojoCotizacion.getIdProveedor() == null || this.pojoCotizacion.getIdProveedor() <= 0L)
					return;
				tipoPersonaProveedor = this.pojoCotizacion.getTipoPersonaProveedor();
			}
			
			// Compruebo si hay impuestos asignados
			if (this.pojoOrden.getId() != null && this.pojoOrden.getId() > 0L)
				this.listOrdenImpuestos = this.ifzOrdenImpuestos.findExtAll(this.pojoOrden.getId());//this.ifzOrdenImpuestos.findExtByProperty("idOrdenCompra", this.pojoOrden.getId(), 0);
			
			if (this.listOrdenImpuestos == null)
				this.listOrdenImpuestos = new ArrayList<OrdenCompraImpuestosExt>();
			
			for (PreOrdenDetalle detalle : this.listOrdenDetalles) {
				backtrace("Recupero los impuestos para la familia " + detalle.getIdProducto().getFamilia().getId() + " - " + detalle.getIdProducto().getDescFamilia());
				listImpuestosAux = this.ifzSubfamliaImto.findExtByProperty("idSubfamilia", detalle.getIdProducto().getFamilia().getId());
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
					pojoOrdenImpuesto.setAplicaEn(0);
					pojoOrdenImpuesto.setCreadoPor(this.usuarioId);
					pojoOrdenImpuesto.setFechaCreacion(Calendar.getInstance().getTime());
					pojoOrdenImpuesto.setModificadoPor(this.usuarioId);
					pojoOrdenImpuesto.setFechaModificacion(Calendar.getInstance().getTime());

					this.listOrdenImpuestos.add(pojoOrdenImpuesto);
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al verificar los impuestos de los productos a traves de la Familia aplicada al(los) producto(s)", e);
		} finally {
			backtrace(this.listOrdenImpuestos.size() + " Impuestos encontrados");
			this.hasImpuestosExtras = false;
			if (this.listOrdenImpuestos.size() > 0)
				this.hasImpuestosExtras = true;
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
			log.error("No se pudo obtener el Tipo de Cambio actual para la conversion de " + monedaOrigen + " a " + monedaDestino, e);
			tipoCambio = 1.0;
		}
		
		return tipoCambio;
	}

	private void cargarImpuestosIva() {
		try {
			this.listImpuestos = new ArrayList<ConValores>();
			this.listImpuestosItems = new ArrayList<SelectItem>();
			
			// Cargamos la lista de familias
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listImpuestos = this.ifzConValores.buscaValorGrupo("descripcion", "", this.pojoGpoImpuestos);
			if (this.listImpuestos == null || this.listImpuestos.isEmpty()) 
				return;
			
			Collections.sort(this.listImpuestos, new Comparator<ConValores>() {
				@Override
				public int compare(ConValores o1, ConValores o2) {
					return o1.getValor().compareTo(o2.getValor());
				}
			});
			
			for (ConValores impuesto : this.listImpuestos) {
				if (! "002".equals(impuesto.getAtributo4()))
					continue;
				if (! "1".equals(impuesto.getAtributo2()))
					continue;
				if (this.isDebug)
					this.listImpuestosItems.add(new SelectItem(impuesto.getId(), impuesto.getDescripcion() + " (" + impuesto.getAtributo1() + " %)"));
				else
					this.listImpuestosItems.add(new SelectItem(impuesto.getId(), impuesto.getDescripcion()));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Impuestos IVA", e);
		}
	}
	
	public void asignarImpuesto(double valorImpuesto) {
		String valor = "";
		if (valorImpuesto <= 0)
			return;
		for (ConValores impuesto : this.listImpuestos) {
			valor = impuesto.getAtributo1().trim();
			if (valor == null || "".equals(valor))
				valor = "0";
			if (Double.valueOf(valor) == valorImpuesto) {
				this.idImpuesto = impuesto.getId();
				this.porcentajeIva = valorImpuesto;
				break;
			}
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
	
	private void restauraPreciosUnitariosDetalles() {
		List<PreOrdenDetalle> tmp = null;
		
		try {
			if (this.listOrdenDetallesOriginales == null || this.listOrdenDetallesOriginales.isEmpty())
				return;
			
			tmp = new ArrayList<PreOrdenDetalle>();
			tmp.addAll(this.listOrdenDetalles);
			this.listOrdenDetalles.clear();
			this.listOrdenDetalles.addAll(this.listOrdenDetallesOriginales);
			this.pojoOrden.setTipoCambio(new BigDecimal(this.tipoCambioOriginal));
			if (this.pojoOrden.getId() != null && this.pojoOrden.getId() > 0L)
				return;
			
			for (PreOrdenDetalle pojoMod : tmp) {
				for (PreOrdenDetalle pojoOri : this.listOrdenDetalles) {
					if (pojoMod.getIdProducto().getId().longValue() == pojoOri.getIdProducto().getId().longValue()) {
						pojoOri.setCantidad(pojoMod.getCantidad());
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar reestablecer los productos", e);
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

		String buffer = "";
		for (String msg : this.backtrace)
			buffer += msg + "\n";
		
		log.info("\n\nBACKTRACE OC START --------------------------------------------------");
		log.info(buffer);
		log.info("BACKTRACE OC END --------------------------------------------------\n\n");
	}
	
	// -------------------------------------------------------------------------
	// BUSQUEDA COTIZACIONES
	// -------------------------------------------------------------------------
	
	public void nuevaBusquedaCotizaciones() {
		this.campoBusquedaCotizaciones = this.tiposBusquedaCotizaciones.get(0).getValue().toString();
		this.valorBusquedaCotizaciones = "";
		this.numPaginaCotizaciones = 1;
		
		if (this.listCotizaciones == null)
			this.listCotizaciones = new ArrayList<Cotizacion>();
		this.listCotizaciones.clear();
		this.pojoCotizacion = new Cotizacion();
	}
		
	public void buscarCotizaciones() {
		String valor = "";
		
		try {
			control();
			if ("".equals(this.campoBusquedaCotizaciones))
				this.campoBusquedaCotizaciones = this.tiposBusquedaCotizaciones.get(0).getValue().toString();
			
			backtrace("Busqueda de Cotizaciones. " + this.campoBusquedaCotizaciones + ": " + this.valorBusquedaCotizaciones);
			valor = this.valorBusquedaCotizaciones.trim().replace(" ", "%");
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCotizaciones = this.ifzCotizaciones.findLikeProperty(this.campoBusquedaCotizaciones, valor, this.pojoObra.getId(), this.tipoMaestro.ordinal(), true, false, false, "CASE sistema when 0 then 0 when -1 then 1 when 1 then 2 else 3 end, CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.fecha desc, model.id desc", 0);
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
    		this.numPaginaCotizaciones = 1;
			if (this.isDebug)
				backtracePrint();
    	}
	}

	public void seleccionarCotizacion() {
		List<PersonaExt> provs = null;
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
			
			this.pojoOrden.setIdCotizacion(cotizacion);
			if (this.pojoObra == null) {
				this.pojoObra = this.pojoOrden.getIdCotizacion().getIdObra().Copia();
				this.pojoOrden.setIdObra(this.pojoObra);
			}

			this.idMonedaActual = this.pojoCotizacion.getIdMoneda();
			this.idMonedaOriginal = this.pojoCotizacion.getIdMoneda();
			this.tipoCambioOriginal = (this.pojoCotizacion.getTipoCambio() > 0 ? this.pojoCotizacion.getTipoCambio() : 1.0);
			
			this.pojoOrden.setTiempoEntrega(this.pojoCotizacion.getTiempoEntrega());
			this.pojoOrden.setLugarEntrega(this.pojoObra.getNombre());
			this.pojoOrden.setIdMoneda(this.pojoCotizacion.getIdMoneda());
			this.pojoOrden.setTipoCambio(new BigDecimal(this.tipoCambioOriginal));
			
			// Recuperamos el proveedor
			provs = this.ifzCotizaciones.findPersonaLikeProperty("id", this.pojoCotizacion.getIdProveedor(), this.pojoCotizacion.getTipoPersonaProveedor(), 0);
			if (provs != null && ! provs.isEmpty()) {
				this.valorBusquedaTipoProveedor = this.pojoCotizacion.getTipoPersonaProveedor();
				this.pojoProveedor = provs.get(0);
			}
			
			if (this.pojoOrden.getIdMoneda() <= 0L) {
				this.pojoOrden.setIdMoneda(this.loginManager.getInfoSesion().getEmpresa().getMonedaId());
				this.pojoOrden.setMoneda(this.loginManager.getInfoSesion().getEmpresa().getMoneda());
			}

			// detalles desde la cotizacion 
			recuperarListadoDetalles(false);
			nuevaBusquedaCotizaciones();
			backtrace("Proceso de recuperacion de Cotizacion terminado");
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
		this.campoBusquedaRequisiciones = this.tiposBusquedaRequisiciones.get(0).getValue().toString();
		this.valorBusquedaRequisiciones = "";
		this.numPaginaRequisiciones = 1;
		
		if (this.listRequisiciones == null)
			this.listRequisiciones = new ArrayList<Requisicion>();
		this.listRequisiciones.clear();
		this.pojoRequisicion = new Requisicion();
	}
	
	public void buscarRequisiciones() {
		String valor = "";
		
		try {
			control();
			if ("".equals(this.campoBusquedaRequisiciones))
				this.campoBusquedaRequisiciones = this.tiposBusquedaRequisiciones.get(0).getValue().toString();

			backtrace("Busqueda de Requisiciones. " + this.campoBusquedaRequisiciones + ": " + this.valorBusquedaRequisiciones);
			valor = this.valorBusquedaRequisiciones.trim().replace(" ", "%");
			this.ifzRequisiciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listRequisiciones = this.ifzRequisiciones.findLikeProperty(this.campoBusquedaRequisiciones, valor, this.pojoObra.getId(), this.tipoMaestro.ordinal(), false, false, 0L, "", 0);
			if (this.listRequisiciones == null || this.listRequisiciones.isEmpty()) {
				backtrace("ERROR 2 - La busqueda no regreso resultados");
				control(2, "La busqueda no regreso resultados");
				return;
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Requisiciones", e);
    	} finally {
    		this.numPaginaRequisiciones = 1;
			if (this.isDebug)
				backtracePrint();
    	}
	}
	
	public void seleccionarRequisicion() {
		List<RequisicionDetalleExt> listReqDetalles = new ArrayList<RequisicionDetalleExt>();
		CotizacionDetalleExt cdet = null;
		double precioUnitario = 0;
		double tipoCambio = 0;
		double monto = 0;
		
		try {
			control();
			// Comprobamos requisicion
			backtrace("Proceso de recuperacion de Requisicion seleccionada ... Comprobando");
			if (this.pojoRequisicion == null || this.pojoRequisicion.getId() == null || this.pojoRequisicion.getId() <= 0L) {
				log.error("ERROR 2 - Debe seleccionar una requisicion");
				control(2, "La busqueda no regreso resultados");
				return;
			}

			// Recuperamos el detalle de la requisicion
			backtrace("Recuperamos detalles de Requisicion");
			listReqDetalles = this.ifzReqDetalles.findExtByProperty("idRequisicion", this.pojoRequisicion.getId(), 0);
			if (listReqDetalles == null || listReqDetalles.isEmpty()) {
				backtrace("ERROR INTERNO - La Requisicion no tiene productos o ya han sido usados en cotizaciones");
				control(-1, "La Requisicion no tiene productos disponibles");
				return;
			}

			// Genero cotizacion
			backtrace("Genero cotizacion a partir de la requisicion seleccionada");
			this.pojoCotizacion = new Cotizacion();
			this.pojoCotizacion.setId(0L);
			this.pojoCotizacion.setIdRequisicion(this.pojoRequisicion.getId());
			this.pojoCotizacion.setIdObra(this.pojoObra.getId());
			this.pojoCotizacion.setNombreObra(this.pojoObra.getNombre());
			this.pojoCotizacion.setIdComprador(this.pojoRequisicion.getIdSolicita());
			this.pojoCotizacion.setNombreComprador(this.pojoRequisicion.getNombreSolicita());
			
			if (this.pojoProveedor != null && this.pojoProveedor.getId() > 0L) {
				seleccionarProveedor();
				/*this.pojoCotizacion.setIdProveedor(this.pojoProveedor.getId());
				this.pojoCotizacion.setNombreProveedor(this.pojoProveedor.getNombre());
				this.pojoCotizacion.setTipoPersonaProveedor(this.valorBusquedaTipoProveedor);*/
			}
			
			if (this.pojoContacto != null && this.pojoContacto.getId() > 0L) {
				this.pojoCotizacion.setIdContacto(this.pojoContacto.getId());
				this.pojoCotizacion.setNombreContacto(this.pojoContacto.getNombre());
			}

			this.pojoCotizacion.setIdMoneda(this.pojoRequisicion.getIdMoneda());
			this.pojoCotizacion.setTipo(this.pojoRequisicion.getTipo());
			this.pojoCotizacion.setIva(0D);
			this.pojoCotizacion.setCreadoPor(this.usuarioId);
			this.pojoCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoCotizacion.setModificadoPor(this.usuarioId);
			this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoCotizacion.setSubtotal(this.subtotal);
			this.pojoCotizacion.setIva(this.iva);
			this.pojoCotizacion.setTotal(this.total);
			
			this.pojoOrden.setIdMoneda(this.pojoRequisicion.getIdMoneda());
			this.pojoOrden.setTipo(this.pojoRequisicion.getTipo());
			this.pojoRequisicionSeleccionada = copiaRequisicion(this.pojoRequisicion);
			this.idMonedaActual = this.pojoCotizacion.getIdMoneda();
			this.idMonedaOriginal = this.pojoCotizacion.getIdMoneda();

			// Genero detalles para cotizacion
			if (this.listCotDetalles == null)
				this.listCotDetalles = new ArrayList<CotizacionDetalleExt>();
			this.listCotDetalles.clear();

			backtrace("Genero detalles de cotizacion a partir de la requisicion seleccionada");
			for (RequisicionDetalleExt var : listReqDetalles) {
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
				cdet.setCantidadInicial(var.getCantidad());
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

			backtrace(this.listCotDetalles.size() + " detalles de Cotizacion generados.");
			// detalles desde la cotizacion 
			backtrace("Genero detalles para Orden de Compra desde Cotizacion (Requisicion).");
			recuperarListadoDetalles(false);
			nuevaBusquedaRequisiciones();
			backtrace("Proceso de recuperacion de Requisicion terminado");
    	} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Requiscion seleccionada", e);
    	} finally {
			backtrace(this.listOrdenDetalles.size() + " detalles de Orden de Compra generados.");
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
	// BUSQUEDA OBRAS
	// -------------------------------------------------------------------------
	
	public void nuevaBusquedaObras() {
    	this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.valorBusquedaTipoObra = 0;
		this.numPaginaObras = 1;
		
		if (this.listObras != null)
			this.listObras.clear();
    }
	
	public void buscarObras() {
		String valor = "";
		
		try {
			control();
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();

			backtrace("Busqueda de Obras. " + this.campoBusquedaObras + ": " + this.valorBusquedaObras);
			valor = this.valorBusquedaObras.trim().replace(" ", "%");
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikePropertyExt(this.campoBusquedaObras, valor, this.valorBusquedaTipoObra, "", 0);
			if (this.listObras == null && this.listObras.isEmpty()) {
				backtrace("Busqueda de Obras. ERROR 2 - Busqueda sin resultados");
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Obras", e);
			if (this.listObras != null) 
				this.listObras.clear();
    	} finally {
    		this.numPaginaObras = 1;
			if (this.isDebug)
				backtracePrint();
    	}
	}

	public void seleccionarObra() {
		try {
			control();
			// Recuperamos los insumos de la obra seleccionada
			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		} finally {
    		if (this.isDebug)
				backtracePrint();
    	}
	}

	// -------------------------------------------------------------------------
	// BUSQUEDA PROVEEDORES
	// -------------------------------------------------------------------------
	
	public void nuevaBusquedaProveedores() {
		this.campoBusquedaProveedores = this.tiposBusquedaProveedores.get(0).getValue().toString();
		this.valorBusquedaProveedores = "";
		this.valorBusquedaTipoProveedor = "N";
		this.numPaginaProveedores = 1;
		
		if (this.listProveedores == null) 
			this.listProveedores = new ArrayList<PersonaExt>();
		this.listProveedores.clear();
		if (this.pojoProveedor == null)
			this.pojoProveedor = new PersonaExt();
    }

	public void buscarProveedores() {
		String valor = "";
		
		try {
			control();
			if ("".equals(this.valorBusquedaTipoProveedor))
				this.valorBusquedaTipoProveedor = "N";
			
			if ("".equals(this.campoBusquedaProveedores))
				this.valorBusquedaProveedores = "id";

			
			backtrace("Buscando Proveedores. " + this.campoBusquedaProveedores + ":" + this.valorBusquedaProveedores);
			valor = this.valorBusquedaProveedores.trim().replace(" ", "%");
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listProveedores = this.ifzCotizaciones.findPersonaLikeProperty(this.campoBusquedaProveedores, valor, this.valorBusquedaTipoProveedor, 0);
			if (this.listProveedores == null && this.listProveedores.isEmpty()) {
				backtrace("Buscando Proveedores.  ERROR 2 - Busqueda sin resultados");
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Proveedores", e);
			if (this.listProveedores != null) 
				this.listProveedores.clear();
    	} finally {
    		this.numPaginaProveedores = 1;
			if (this.isDebug)
				backtracePrint();
    	}
	}

	public void seleccionarProveedor() {
		try {
			control();
			if (this.pojoProveedor == null || this.pojoProveedor.getId() <= 0L) {
				backtrace("El proveedor no pudo ser asignado, pojo nulo");
				control(-1, "ERROR INTERNO - Ocurrio un problema al recuperar el Proveedor seleccionado");
				return;
			}
			
			if (this.pojoCotizacion == null)
				return;
			
			// Asigno el PROVEEDOR seleccionada y el tipo de persona de proveedor (Persona (P) o Negocio (N)) a la cotizacion
			this.pojoCotizacion.setIdProveedor(this.pojoProveedor.getId());
			this.pojoCotizacion.setNombreProveedor(this.pojoProveedor.getNombre());
			this.pojoCotizacion.setRfcProveedor(this.pojoProveedor.getRfc());
			this.pojoCotizacion.setTipoPersonaProveedor(this.valorBusquedaTipoProveedor);
			
			// Recuperamos el contacto del proveedor seleccionado
			backtrace("Recuperando contacto para proveedor: " + this.pojoProveedor.getId() + " - " + this.pojoProveedor.getNombre());
			this.pojoContacto = this.ifzCotizaciones.findContactoByProveedor(this.pojoProveedor, this.valorBusquedaTipoProveedor);
			if (this.pojoContacto != null) {
				this.pojoCotizacion.setIdContacto(this.pojoContacto.getId());
				this.pojoCotizacion.setNombreContacto(this.pojoContacto.getNombre());
			} else {
				backtrace("El Proveedor no tiene asignado ningun Contacto");
				this.pojoCotizacion.setIdContacto(0L);
				this.pojoCotizacion.setNombreContacto("");
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
		this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();
		this.valorBusquedaEmpleados = "";
		this.numPaginaEmpleados = 1;
		
		if (this.listEmpleados == null)
			this.listEmpleados = new ArrayList<Empleado>();
		this.listEmpleados.clear();
		this.pojoEmpleado = null;
	}
	
	public void buscarEmpleados() {
		String valor = "";
		
		try {
			control();
			if ("".equals(this.campoBusquedaEmpleados))
				this.campoBusquedaEmpleados = this.tiposBusquedaEmpleados.get(0).getValue().toString();

			backtrace("Busqueda de Empleados. " + this.campoBusquedaEmpleados + ": " + this.valorBusquedaEmpleados);
			valor = this.valorBusquedaEmpleados.trim().replace(" ", "%");
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.listEmpleados = this.ifzEmpleados.findLikeProperty(this.campoBusquedaEmpleados, valor, 0);
			if (this.listEmpleados.isEmpty()) {
				backtrace("Busqueda de Empleados. ERROR 2 - Busqueda sin resultados");
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Empleados", e);
			if (this.listEmpleados != null) 
				this.listEmpleados.clear();
    	} finally {
    		this.numPaginaEmpleados = 1;
			if (this.isDebug)
				backtracePrint();
    	}
	}

	public void seleccionarEmpleado() {
		try {
			control();
			if (this.pojoEmpleado != null)
				this.pojoOrden.setIdSolicita(this.ifzEmpleados.convertir(this.pojoEmpleado));
			nuevaBusquedaEmpleados();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Empleado seleccionado", e);
		} finally {
    		if (this.isDebug)
				backtracePrint();
    	}
	}

	// -------------------------------------------------------------------------
	// LOCAL STORAGE
	// -------------------------------------------------------------------------
	
	public String backupToJson() {
		HashMap<String,String> map = new HashMap<String,String>();
		Gson gson = new Gson();

		map.put("usuario", String.valueOf(this.usuarioId));
		if (this.pojoObra != null)
			map.put("pojoObra", gson.toJson(this.pojoObra));
		if (this.pojoOrden != null)
			map.put("pojoOrden", gson.toJson(this.pojoOrden));
		if (this.pojoCotizacion != null)
			map.put("pojoCotizacion", gson.toJson(this.pojoCotizacion));
		if (this.pojoRequisicion != null)
			map.put("pojoRequisicion", gson.toJson(this.pojoRequisicion));
		if (this.listOrdenDetalles != null)
			map.put("listOrdenDetalles", gson.toJson(this.listOrdenDetalles));
		if (this.listCotDetalles != null)
			map.put("listCotDetalles", gson.toJson(this.listCotDetalles));
		
		return gson.toJson(map);
	}
	
	public void restoreFromJson() {
		
	}

	// -------------------------------------------------------------------------
	// CONVERSION 
	// -------------------------------------------------------------------------
	
	public void evaluaConversion() {
		this.evaluaConversion = false;
		if (this.idMonedaActual != this.pojoOrden.getIdMoneda()) {
			if (this.pojoOrden.getTipoCambio() == null || this.pojoOrden.getTipoCambio().doubleValue() <= 0L)
				this.pojoOrden.setTipoCambio(new BigDecimal(1.0));
			if (this.pojoOrden.getIdMoneda() == this.idMonedaOriginal) 
				this.pojoOrden.setTipoCambio(new BigDecimal(this.tipoCambioOriginal));
			if (this.pojoOrden.getIdMoneda() != this.pojoOrden.getIdCotizacion().getIdMoneda()) {
				if (this.idMonedaActual != this.loginManager.getUsuarioEmpresa().getMonedaId())
					this.pojoOrden.setTipoCambio(new BigDecimal(this.loginManager.getTipoCambioActual()));
			} 
			
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
				return;
			} 
			
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

			tipoCambio = (this.pojoOrden.getTipoCambio().doubleValue() > 1) ? this.pojoOrden.getTipoCambio().doubleValue() : this.loginManager.getTipoCambioActual();
			if (tipoCambio <= 0) {
				this.pojoOrden.setIdMoneda(this.idMonedaActual);
				control(-1, "Ocurrio un problema al convertir " + monOrigen.getNombre() + " a " + monDestino.getNombre() + ".\nNo se pudo recuperar el Tipo de Cambio");
				return;
			}
			
			// Actualizo en la Orden el tipo de cambio
			this.pojoOrden.setTipoCambio(new BigDecimal(tipoCambio));
			
			backtrace("Tipo de Cambio obtenido :) ... " + tipoCambio);
			backtrace("Realizando Conversion para productos | " + monOrigen.getAbreviacion() + " a " + monDestino.getAbreviacion() + " ... ");
			for (PreOrdenDetalle var : this.listOrdenDetalles) {
				if (this.idMonedaBase == monDestino.getId())
					monto = var.getPrecioUnitario() * tipoCambio;
				else
					monto = var.getPrecioUnitario() / tipoCambio;
				var.setPrecioUnitario(monto);
				
				monto = monto * var.getCantidad();
				var.setImporte(monto);
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

	// -------------------------------------------------------------------------
    // Propiedades 
	// -------------------------------------------------------------------------

	public String getOrdenCompraTitulo() {
		if (this.pojoOrden != null && this.pojoOrden.getId() != null && this.pojoOrden.getId() > 0L)
			return (this.tipoMaestro == TipoMaestro.Administrativo ? "ADMINISTRATIVA " : "") + this.pojoOrden.getId();
		return (this.tipoMaestro == TipoMaestro.Administrativo ? "ADMINISTRATIVA" : "");
	}
	
	public void setOrdenCompraTitulo(String value) {}
	
	public boolean getValidaCotizacionEdit() {
		if (this.editarOrdenCompra) {
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
					if (this.pojoOrden.getIdCotizacion().getIdProveedor() != null && this.pojoOrden.getIdCotizacion().getIdProveedor().getId() > 0L)
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

	public long getOrdenCompraId() {
		return ordenCompraId;
	}

	public void setOrdenCompraId(long ordenCompraId) {
		this.ordenCompraId = ordenCompraId;
	}

	public boolean getSeleccionarTodos() {
		return seleccionarTodos;
	}

	public void setSeleccionarTodos(boolean seleccionarTodos) {
		if (this.seleccionarTodos == seleccionarTodos)
			return;

		this.seleccionarTodos = seleccionarTodos;
		if (this.listOrdenDetalles != null && ! this.listOrdenDetalles.isEmpty()) {
			for (PreOrdenDetalle var : this.listOrdenDetalles)
				var.setSeleccionado(seleccionarTodos);
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

	public int getEstatusOrdenCompra() {
		return estatusOrdenCompra;
	}

	public void setEstatusOrdenCompra(int estatusOrdenCompra) {
		this.estatusOrdenCompra = estatusOrdenCompra;
	}

	public boolean isEsAdministrativo() {
		return esAdministrativo;
	}

	public void setEsAdministrativo(boolean esAdministrativo) {
		this.esAdministrativo = esAdministrativo;
	}

	public boolean getOrigenRequisicion() {
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
	
	public void setPojoRequisicionSeleccionada(Requisicion pojoRequisicionSeleccionada) {
		this.pojoRequisicionSeleccionada = pojoRequisicionSeleccionada;
	}

	public List<SelectItem> getListEstatusOrdenCompraItems() {
		return listEstatusOrdenCompraItems;
	}

	public void setListEstatusOrdenCompraItems(List<SelectItem> listEstatusOrdenCompraItems) {
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

	public List<ConValores> getListImpuestos() {
		return listImpuestos;
	}

	public void setListImpuestos(List<ConValores> listImpuestos) {
		this.listImpuestos = listImpuestos;
	}

	public List<SelectItem> getListImpuestosItems() {
		return listImpuestosItems;
	}

	public void setListImpuestosItems(List<SelectItem> listImpuestosItems) {
		this.listImpuestosItems = listImpuestosItems;
	}

	public long getIdImpuesto() {
		return idImpuesto;
	}

	public void setIdImpuesto(long idImpuesto) {
		this.idImpuesto = idImpuesto;
		if (idImpuesto <= 0)
			return;
		for (ConValores impuesto : this.listImpuestos) {
			if (impuesto.getId() == idImpuesto) {
				this.porcentajeIva = Double.valueOf(("".equals(impuesto.getAtributo1().trim()) ? "0" : impuesto.getAtributo1().trim()));
				break;
			}
		}
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
