package net.giro.compras;

import java.io.Serializable;
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

import net.giro.adp.beans.Insumos;
import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.adp.beans.InsumosExt;
import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.ObraAlmacenesExt;
import net.giro.adp.beans.ObraExt;
import net.giro.adp.beans.TiposObra;
import net.giro.adp.logica.InsumosDetallesRem;
import net.giro.adp.logica.InsumosRem;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.PersonaExt;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalleExt;
import net.giro.compras.beans.CotizacionExt;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.PreCotizacionDetalle;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.beans.RequisicionDetalleExt;
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.TipoMaestro;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.dao.MonedasValoresDAO;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="cotizaAction")
public class CotizacionesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CotizacionesAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx; 
	
	// Interfaces
	private CotizacionRem ifzCotizaciones;
	private CotizacionDetalleRem ifzCotiDetalles;
	private ObraRem ifzObras;
	private InsumosRem ifzInsumos;
	private InsumosDetallesRem ifzInsumosDet;
	private EmpleadoRem ifzEmpleados;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private ReportesRem	ifzReportes;
	private ObraAlmacenesRem ifzObraAlmacen;
	private AlmacenProductoRem ifzAlmacenProducto;
	private AlmacenTraspasoRem ifzTraspaso;
	private TraspasoDetalleRem ifzTraspasoDetalle;
	private RequisicionRem ifzReq;
	private RequisicionDetalleRem ifzReqDetalle;
	private OrdenCompraRem ifzOC;
	private OrdenCompraDetalleRem ifzOCDetalles;
	private MonedasValoresDAO ifzMonValores;
	// Listas
	private List<Cotizacion> listCotizaciones;
	private List<PreCotizacionDetalle> listCotiDetalles;
	private List<PreCotizacionDetalle> listCotiDetallesFull;
	private List<PreCotizacionDetalle> listCotiDetallesEliminados;
	private List<CotizacionDetalleExt> listDetalles;
	private List<Empleado> listCompradores;
	private List<SelectItem> listCompradoresItems;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	// POJO's
	private ObraExt pojoObra;
	private Cotizacion pojoCotizacionMain;
	private CotizacionExt pojoCotizacion;
	private Cotizacion pojoCotizacionBorrar;
	private PreCotizacionDetalle pojoCotiDetalleBorrar;
	private ConGrupoValores pojoGpoFamilias;	
	// Busqueda principal
	private List<Obra> listObrasGrid;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Busqueda obras
	private List<ObraExt> listObras;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorBusquedaTipoObra;
	private int numPaginaObras;
	// Busqueda Proveedores
	private List<PersonaExt> listProveedores;
	private List<SelectItem> tiposBusquedaProveedores;	
	private String campoBusquedaProveedores;
	private String valorBusquedaProveedores;
	private String valorBusquedaTipoProveedor;
	private int numPaginaProveedores;
	// Variables de operacion
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
    private long usuarioId;
    private String usuario;
    private String usuarioEmail;
	private int numPaginaCotizaciones;
	private int numPaginaProductos;
	private double porcentajeIva;	
	private double subtotal;
	private double iva;
	private double total;	
	private long valGpoFamilias;
	private long valFamilia;
	private long valComprador;	
	private boolean editarCotizacion;
	private boolean seleccionarTodos;
	private boolean esNuevaCotizacion;
	private String cotizacionOrdenCompra;
	private TipoMaestro tipoMaestro;
	private boolean esAdministrativo;
	private List<SelectItem> listEstatusCotizacionItems;
	// Email
	private String email;
	private String emailAsunto;
	private String emailCuerpo;
	// Traspasos
	private List<AlmacenTraspasoExt> listTraspasos;
	private List<TraspasoDetalleExt> listTraspasosDetalles;	
    private boolean permiteModificar;
	private long valObraCancelada;
	// REQUISICIONES
	private Requisicion pojoRequisicion;
	private List<Requisicion> listRequisiciones;
	private List<RequisicionDetalleExt> listReqDetalles;
	private List<SelectItem> reqOpcionesBusqueda;	
	private String reqCampoBusqueda;
	private String reqValorBusqueda;
	private int reqPaginacion;
	private boolean origenFromRequisicion;
	private boolean esCopia;
	private int estatusCotizacion;
	// Monedas
	private MonedaDAO ifzMonedas;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private long idMoneda;
	private long idMonedaBase;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	

	public CotizacionesAction() {
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
			
			Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet()) {
				this.paramsRequest.put(item.getKey(), item.getValue());
			}
			
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			this.ctx = new InitialContext();			
			this.ifzCotizaciones = (CotizacionRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzCotiDetalles = (CotizacionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzInsumos = (InsumosRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzInsumosDet = (InsumosDetallesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//InsumosDetallesFac!net.giro.adp.logica.InsumosDetallesRem");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzObraAlmacen = (ObraAlmacenesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzTraspaso = (AlmacenTraspasoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
			this.ifzTraspasoDetalle = (TraspasoDetalleRem) this.ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			this.ifzAlmacenProducto = (AlmacenProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			this.ifzReq = (RequisicionRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalle = (RequisicionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzOC = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOCDetalles = (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzMonValores = (MonedasValoresDAO) this.ctx.lookup("ejb:/Model_TYG//MonedasValoresImp!net.giro.tyg.dao.MonedasValoresDAO");
			
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			this.usuarioEmail = this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo();

			/* VALIDACION DE PERFILES */
			valPerfil = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			this.porcentajeIva = ((valPerfil != null && ! "".equals(valPerfil.trim())) ? Double.valueOf(valPerfil) : 0);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.esAdministrativo = ((valPerfil != null && "S".equals(valPerfil.trim())) ? true : false);
			
			/* Grupo de valores para Familias de productos */
			if (this.entornoProperties.getString("SYS_FAMILIA_PRODUCTO") == null || "".equals(this.entornoProperties.getString("SYS_FAMILIA_PRODUCTO")))
				throw new Exception("No se encontro encontro el grupo SYS_FAMILIA_PRODUCTO en con_grupo_valores");
			this.valGpoFamilias = Long.valueOf(this.entornoProperties.getString("SYS_FAMILIA_PRODUCTO"));
			this.pojoGpoFamilias = this.ifzGpoVal.findById(valGpoFamilias);
			
			// Listas
			this.listObrasGrid = new ArrayList<Obra>();
			this.listCotizaciones = new ArrayList<Cotizacion>();
			this.listCotiDetalles = new ArrayList<PreCotizacionDetalle>();
			this.listObras = new ArrayList<ObraExt>();
			
			// POJO's
			this.pojoCotizacion = new CotizacionExt();
			this.pojoCotizacionBorrar = new Cotizacion();
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("nombre", "Nombre Obra"));
			this.tiposBusqueda.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusqueda.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusqueda.add(new SelectItem("nombreResponsable", "Encargado"));
			this.tiposBusqueda.add(new SelectItem("cot.folio", "Folio Cotizacion"));
			this.tiposBusqueda.add(new SelectItem("cot.nombreProveedor", "Proveedor Cotizacion"));
			this.tiposBusqueda.add(new SelectItem("id", "Clave"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "Clave"));
			this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
			this.valorBusquedaObras = "";
			this.valorBusquedaTipoObra = 0;
			this.numPaginaObras = 1;
			
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
			this.reqOpcionesBusqueda = new ArrayList<SelectItem>();
			this.reqOpcionesBusqueda.add(new SelectItem("nombreSolicita", "Solicita"));
			this.reqOpcionesBusqueda.add(new SelectItem("folio", "Folio"));
			this.reqOpcionesBusqueda.add(new SelectItem("id", "ID"));
			this.reqCampoBusqueda = this.reqOpcionesBusqueda.get(0).getValue().toString();
			this.reqValorBusqueda = "";
			this.reqPaginacion = 1;
			
			// Estatus Cotizaciones
			this.listEstatusCotizacionItems = new ArrayList<SelectItem>();
			this.listEstatusCotizacionItems.add(new SelectItem(0, msgProperties.getString("cotizacion.activa")));
			this.listEstatusCotizacionItems.add(new SelectItem(2, msgProperties.getString("cotizacion.ordenCompra")));
			this.listEstatusCotizacionItems.add(new SelectItem(1, msgProperties.getString("cotizacion.eliminada")));
			this.estatusCotizacion = (int) this.listEstatusCotizacionItems.get(0).getValue();

			this.numPaginaCotizaciones = 1;
			this.numPaginaProductos = 1;
			this.valObraCancelada = 0L; // 10000798L;

			this.tipoMaestro = TipoMaestro.Inventario;
		} catch (Exception e) {
			log.error("Error en Compras.CotizacionAction.constructor CotizacionesAction", e);
			this.ctx = null;
		}
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
		if (value == null || "".equals(value))
			control(true, 1, "ERROR");
		else
			control(true, -1, value); 
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "": mensaje;
	}
	
	public void buscar() throws Exception {
		try {
    		control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = "id";
			
			if (this.listObrasGrid == null) 
				this.listObrasGrid = new ArrayList<Obra>();
			this.listObrasGrid.clear();
			
			if (this.campoBusqueda.contains("cot.")) {
				log.info("Buscamos obras por el folio de Cotizacion");
				List<Cotizacion> listAux = this.ifzCotizaciones.findLikeProperty(this.campoBusqueda.substring(4), this.valorBusqueda, 0);
				if (listAux != null && ! listAux.isEmpty()) {
					List<Long> listAdds = new ArrayList<Long>();
					Obra obraAux = null;
					for (Cotizacion var : listAux) {
						if (listAdds.contains(var.getIdObra())) 
							continue;
						obraAux = this.ifzObras.findById(var.getIdObra());
						if (! this.esAdministrativo && obraAux.getTipoObra() == TiposObra.Administrativa.ordinal())
							continue;
						this.listObrasGrid.add(obraAux);
						listAdds.add(obraAux.getId());
					}
					
					listAux.clear();
					listAdds.clear();
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
			if (this.listObrasGrid != null) this.listObrasGrid.clear();
    		log.error("Error en Compras.CotizacionAction.buscar", e);
    		control(true);
    	} finally {
    		if (this.listObrasGrid != null && ! this.listObrasGrid.isEmpty())
    			log.info(this.listObrasGrid.size() + " resultados encontrados.");
    	}
	}

	public void ver() throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		try {
    		control();
			this.esCopia = false;
			
			log.info("Obra seleccionada: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
			if (this.listFamilias == null || this.listFamilias.isEmpty())
				cargarFamilias();

			if (this.listCompradores == null || this.listCompradores.isEmpty())
				cargarCompradores();

			// Comprobamos INSUMOS de la obra
			if (! comprobarInsumos()) 
	    		log.info("La obra seleccionada no tiene INSUMOS");
			
			this.numPaginaCotizaciones = 1;
			this.permiteModificar = true;
			this.valObraCancelada = this.ifzObras.findEstatusCanceladoObras();
			if(this.pojoObra.getEstatus().equals(this.valObraCancelada)) 
				this.permiteModificar = false;

			this.tipoMaestro = TipoMaestro.Inventario;
			if(this.pojoObra.getTipoObra() == TiposObra.Administrativa.ordinal()) 
				this.tipoMaestro = TipoMaestro.Administrativo;
			
			if (this.listCotizaciones == null)
				this.listCotizaciones = new ArrayList<Cotizacion>();
			this.listCotizaciones.clear();
			
			// Recuperamos las cotizaciones para esa obra
			log.info("Cargo Cotizaciones ... ");
			if (this.pojoObra.getId() != null && this.pojoObra.getId() > 0L) {
				log.info("Recuperando Cotizaciones por obra: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
				params.put("idObra", this.pojoObra.getId());
				params.put("estatus", this.estatusCotizacion);
				
				//this.ifzCotizaciones.estatus((this.permiteModificar ? this.estatusCotizacion : null));
				this.ifzCotizaciones.OrderBy("CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.id desc");
				this.listCotizaciones = this.ifzCotizaciones.findByProperties(params); //this.ifzCotizaciones.findLikeProperty("idObra", this.pojoObra.getId(), 0);
			}
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.ver", e);
    		control(true);
    	} finally {
			if (this.listCotizaciones == null)
				this.listCotizaciones = new ArrayList<Cotizacion>();
			log.info(this.listCotizaciones.size() + " Cotizaciones encontradas.");
    	}
	}

	public void nuevoSinObra() throws Exception {
		this.pojoObra = null;
		this.nuevo();
	}
	
	public void nuevoInsumos() {
		try {
    		control();
			this.origenFromRequisicion = false;
			this.pojoRequisicion = new Requisicion();
			nuevo();
		} catch (Exception e) {
    		control(true);
    		log.error("Error en Compras.CotizacionAction.nuevoInsumos", e);
		}
	}
	
	public void nuevoRequisicion() {
		try {
    		control();
			
			this.origenFromRequisicion = true;
			nuevo();
		} catch (Exception e) {
    		control(true);
    		log.error("Error en Compras.CotizacionAction.nuevoRequisicion", e);
		}
	}
	
	public void nuevo() throws Exception {
		try {
    		control();
			this.esCopia = false;
			this.esNuevaCotizacion = true;
			this.valComprador = 0L;
			this.pojoCotizacionBorrar = null;
			this.pojoCotizacion = new CotizacionExt();
			this.editarCotizacion = true;
			this.cotizacionOrdenCompra = "";
			this.idMoneda = 0;
			
			if (this.listCotiDetalles == null)
				this.listCotiDetalles = new ArrayList<PreCotizacionDetalle>();
			this.listCotiDetalles.clear();
			
			if (this.listCotiDetallesEliminados == null)
				this.listCotiDetallesEliminados = new ArrayList<PreCotizacionDetalle>();
			this.listCotiDetallesEliminados.clear();
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control("Ocurrio un problema al recuperar la Obra seleccionada o no se selecciono ninguna Obra");
				return;
			}
			
			cargarMonedas();
			
			this.pojoCotizacion.setIdObra(this.pojoObra);
			if (this.origenFromRequisicion)
				detalleFromRequisicion();
			else
				detalleFromInsumos();

			log.info("Generamos totales.");
			totalizarCotizacionesDetalles();
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.nuevo", e);
    		control(true);
    	}
	}
	
	public void editar() throws Exception {
		try {
    		control();
			this.esNuevaCotizacion = false;
			if (this.listCotiDetalles == null)
				this.listCotiDetalles = new ArrayList<PreCotizacionDetalle>();
			this.listCotiDetalles.clear();
			
			if (this.listCotiDetallesEliminados == null)
				this.listCotiDetallesEliminados = new ArrayList<PreCotizacionDetalle>();
			this.listCotiDetallesEliminados.clear();
			
			log.info("Extendiendo la Cotizacion");
			this.pojoCotizacion = this.ifzCotizaciones.convertir(this.pojoCotizacionMain);
			this.pojoCotizacionMain = null;
			
			this.editarCotizacion = true;
			this.cotizacionOrdenCompra = "";
			if(this.pojoCotizacion.getEstatus() == 2 || ! this.permiteModificar) {
				this.editarCotizacion = false;
				List<OrdenCompra> ocAux = this.ifzOC.findByProperty("idCotizacion", this.pojoCotizacion.getId(), 0);
				if (ocAux != null && ! ocAux.isEmpty())
					this.cotizacionOrdenCompra = ocAux.get(0).getFolio();
			}
			
			// Cargamos los compradores
			log.info("Cotizacion extendida... cargo compradores");
			cargarCompradores();

			// Cargamos los Monedas
			log.info("Cotizacion extendida... cargo Monedas");
			cargarMonedas();
			
			// Recuperamos quien solicita de la requisicion
			if (this.pojoCotizacion.getIdComprador() != null)
				this.valComprador = this.pojoCotizacion.getIdComprador().getId();
			
			// Recuperamos quien solicita de la requisicion
			this.idMoneda = 0L;
			if (this.pojoCotizacion.getIdMoneda() > 0L)
				this.idMoneda = this.pojoCotizacion.getIdMoneda();
			
			// Recuperamos los detalles de la cotizacion
			this.numPaginaProductos = 1;
			PreCotizacionDetalle det = null;
			List<CotizacionDetalleExt> lista = this.ifzCotiDetalles.findExtByProperty("idCotizacion", this.pojoCotizacion.getId(), 0);
			if (lista != null && ! lista.isEmpty()) {
				for (CotizacionDetalleExt var : lista) {
					det = new PreCotizacionDetalle(var);
					if (var.getEstatus() == 1) det.setOrdenCompra(this.cotizacionOrdenCompra);
					this.listCotiDetalles.add(det);
				}
			}
			
			totalizarCotizacionesDetalles();
			nuevaBusquedaObras();
			nuevaBusquedaProveedores();
			this.seleccionarTodos = false;
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.editar", e);
    		control(true);
    	}
	}
	
	public void guardar() throws Exception {
		boolean isNew = false;
		boolean primeraCotizacion = false;
		double margen = 0;
		double margenTotal = 0;
		
		try {
    		control();
			if (! validacionesCotizacion()) 
				return;

			// Asignamos la requisicion si corresponde
			if (this.origenFromRequisicion && this.pojoCotizacion.getIdRequisicion() == null) {
				this.pojoCotizacion.setIdRequisicion(this.ifzReq.findExtById(this.pojoRequisicion.getId()));
				this.pojoCotizacion.setIdMoneda(this.pojoRequisicion.getIdMoneda());
			} else {
				if (this.idMoneda <= 0L)
					this.idMoneda = getMonedaBase("MXN");
				this.pojoCotizacion.setIdMoneda(this.idMoneda);
			}
			
			this.pojoCotizacion.setSubtotal(this.subtotal);
			this.pojoCotizacion.setIva(this.iva);
			this.pojoCotizacion.setTotal(this.total);
			this.pojoCotizacion.setModificadoPor(this.usuarioId);
			this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoCotizacion.setTipo(this.tipoMaestro.ordinal());
			
			primeraCotizacion = this.comprobarPrimeraCotizacion();
			
			// Generamos el folio
			if (this.esCopia || (this.pojoCotizacion.getFolio() == null || "".equals(this.pojoCotizacion.getFolio()))) {
				if (this.isDebug) log.info("Generamos Folio para Cotizacion.");
				SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
				int consecutivo = this.ifzCotizaciones.findConsecutivoByProveedor(this.pojoCotizacion.getIdProveedor().getId());
				String idPro = String.valueOf(this.pojoCotizacion.getIdProveedor().getId());
				String annio = formateador.format(Calendar.getInstance().getTime());
				String folio = "";

				idPro = idPro.substring(idPro.length() - 4);
				annio = annio.substring(annio.length() - 2);
				folio = idPro + "-" + annio + "-" + ((consecutivo < 10) ? "0" : "") + consecutivo;
				
				this.pojoCotizacion.setFolio(folio);
				this.pojoCotizacion.setConsecutivoProveedor(consecutivo);
			}
			
			// Asignamos el comprador
			if (this.isDebug) log.info("Asignamos comprador");
			for (Empleado var : this.listCompradores) {
				if (this.valComprador == var.getId()) {
					this.pojoCotizacion.setIdComprador(this.ifzEmpleados.convertir(var));
					break;
				}
			}
			
			if (this.esCopia || (this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L)) {
				isNew = true;
				if (this.isDebug) log.info("Guardamos Cotizacion nueva");
				this.pojoCotizacion.setCreadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD
				this.pojoCotizacion.setId(this.ifzCotizaciones.save(this.pojoCotizacion));
				
				// Agregamos la cotizacion al listado
				this.listCotizaciones.add(this.ifzCotizaciones.findById(this.pojoCotizacion.getId()));
			} else {
				// Actualizamos Cotizacion
				if (this.isDebug) log.info("Actualizamos Cotizacion.");
				this.ifzCotizaciones.update(this.pojoCotizacion);
			}
			
			// Borramos los detalles de la cotizacion si corresponde
			if (this.listCotiDetallesEliminados != null && ! this.listCotiDetallesEliminados.isEmpty()) {
				if (this.isDebug) log.info("Borramos detalles de cotizacion");
				for(PreCotizacionDetalle var : this.listCotiDetallesEliminados) {
					if (var.getId() == null || var.getId() <= 0L) continue;
					this.ifzCotiDetalles.delete(var.getId());
				}
				this.listCotiDetallesEliminados.clear();
			}
			
			// Restauramos los detalles del respaldo
			this.valFamilia = 0L;
			filtrarProductos();
			if (this.listCotiDetallesFull != null)
				this.listCotiDetallesFull.clear();
			this.listCotiDetallesFull = null;
			
			// Guardamos los detalles nuevos y modificados
			CotizacionDetalleExt aux = null;
			if (this.listCotiDetalles != null && ! this.listCotiDetalles.isEmpty()) {
				if (this.isDebug) log.info("Guardamos detalles de Cotizacion.");
				for(PreCotizacionDetalle var : this.listCotiDetalles) {
					if (! var.getSeleccionar()) continue;
					
					aux = var.getCotizacion();
					// Asignamos la cotizacion al detalle
					aux.setIdCotizacion(this.pojoCotizacion);

					// Actualiza las cantidades y precios
					aux.setCantidad(var.getCantidad());
					aux.setCotizar(var.getCotizar());
					aux.setPrecioUnitario(var.getPrecioUnitario());

					// Calculamos el margen
					margen = (aux.getProductoPrecioUnitario() * aux.getCantidad()) - (aux.getPrecioUnitario() * aux.getCantidad());
					if (margen <= 0)
						margen = 0;
					margenTotal += margen;
					
					aux.setMargen(margen);
					
					// Comprobamos solicitud a bodega o generamos el traspaso del prosucto segun corresponde
					if (primeraCotizacion)
						generaTraspaso(aux);
					else
						aux = comprobarProducto(aux);
					
					if (this.operacion) 
						return;
					
					// Guardamos el detalle
					if (this.esCopia || (aux.getId() == null || aux.getId() <= 0L)) {
						aux.setFechaCreacion(Calendar.getInstance().getTime());
						aux.setCreadoPor(this.usuarioId);
						aux.setFechaModificacion(Calendar.getInstance().getTime());
						aux.setModificadoPor(this.usuarioId);
						
						// Guardamos en la BD
						aux.setId(this.ifzCotiDetalles.save(aux));
					} else {
						aux.setFechaModificacion(Calendar.getInstance().getTime());
						aux.setModificadoPor(this.usuarioId);
						
						// Actualizamos en la BD
						this.ifzCotiDetalles.update(aux);
					}
					
					if (this.listDetalles == null)
						this.listDetalles = new ArrayList<CotizacionDetalleExt>();
					this.listDetalles.add(aux);
				}
				
				this.listCotiDetalles.clear();
			}
			
			// Guardamos los traspasos si corresponde
			if (primeraCotizacion) {
				guardaTraspasos();
			}
			
			// Buscamos la cotizacion y la editamos
			for(Cotizacion var : this.listCotizaciones) {
				if (var.getId() == this.pojoCotizacion.getId()) {
					var = this.ifzCotizaciones.findById(this.pojoCotizacion.getId());
					var.setMargen(margenTotal);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.usuarioId);
					
					// Actualizamos en la BD	
					this.ifzCotizaciones.update(var);
					break;
				}
			}
			
			if (isNew) {
				// Actualizamos la obra elegida
				if(this.pojoObra == null) this.pojoObra = this.pojoCotizacion.getIdObra().Copia();
				isNew = false;
			} 
			
			// Actualizamos el estatus de la requisicion
			if (this.origenFromRequisicion && this.pojoRequisicion.getEstatus() != 2)
				actualizaEstatusRequisicion();
			
			// Actualiza precio en Ordenes de Compras si corresponde
			actualizarPreciosOrdenesCompra();

			// Actualizamos el listado
			this.pojoCotizacionBorrar = null;
			this.pojoCotizacion = new CotizacionExt();
			this.ver();
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.guardar", e);
    		control(true);
    	}
	}
	
	public void eliminar() {
		try {
    		control();
			// Actualizamos el elemento de la lista
			for (Cotizacion var : this.listCotizaciones) {
				if (var.getId() == this.pojoCotizacionBorrar.getId()) {
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					var.setEstatus(1);
					
					// Actualizamos el pojo de la BD
					this.ifzCotizaciones.update(var);
					break;
				}
			}
			
			// Liberamos requisicion si corresponde
			//liberarRequisicion();
			
			this.pojoCotizacionBorrar = null;
			this.pojoCotizacion = new CotizacionExt();
			this.ver();
    	} catch (Exception e) {
    		control(true);
    		log.error("Error en Compras.CotizacionAction.eliminar", e);
    	}
	}

	public void eliminarDetalle() {
		try {
    		control();
			// Quitamos el detalle de la lista
			this.listCotiDetalles.remove(this.pojoCotiDetalleBorrar);
			this.listCotiDetallesEliminados.add(this.pojoCotiDetalleBorrar);
			
			this.pojoCotiDetalleBorrar = null;
    	} catch (Exception e) {
    		control(true);
    		log.error("Error en Compras.CotizacionAction.eliminarDetalle", e);
    	}
	}

	public void copiar() {
		try {
			control();
			if (this.pojoCotizacionMain != null) {
				this.esCopia = true;
				log.info("Copiando cotizacion (extendiendo)");
				this.editar();
				log.info("Cotizacion copiada");

				if (this.operacion) {
					log.info("Ocurrio un problema al copiar la Cotizacion");
					return;
				}

				log.info("Reseteo ID, Proveedor, Contacto e ID de detalles");
				this.pojoCotizacion.setId(null);
				this.pojoCotizacion.setFolio("");
				this.pojoCotizacion.setTipoPersonaProveedor("");
				this.pojoCotizacion.setIdProveedor(null);
				this.pojoCotizacion.setIdContacto(null);
				
				for(PreCotizacionDetalle var : this.listCotiDetalles)
					var.setId(null);
				log.info("Listo ... Disponible para asignar proveedor");
			}
		} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.copiar", e);
			control(true);
		}
	}
	
	public void nuevoEnvio() {
		try {
			control();
			this.email = "";
			this.emailAsunto = "Cotizacion de Material";
			this.emailCuerpo = "Favor de cotizar el siguiente material.\n\n\nAnexo listado.";
			
			if (this.pojoCotizacion == null || this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
	    		log.info("Error Interno. La Cotizacion aun no ha sido guardada.");
				control(true, -1, "Debe guardar la Cotizacion antes de enviarla por correo electronico. Guardela e intente nuevamente.");
				return;
			}
			
			if (this.pojoCotizacion != null) {
				if (this.pojoCotizacion.getIdContacto() != null && this.pojoCotizacion.getIdContacto().getCorreo() != null)
					this.email = this.pojoCotizacion.getIdContacto().getCorreo();
				this.email = (this.email != null && ! "".equals(this.email)) ? this.email : "";
			}
		} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.nuevoEnvio", e);
    		control(true);
		}
	}
	
	public void reporte() {
		byte[] contenidoReporte = null;
		String nombreReporte = "";
		
		try {
    		control();
			if (this.pojoCotizacionMain != null) {
				log.info("REPORTE COTIZACION: Preparamos parametros");
				if (this.pojoCotizacionMain.getFolio() != null && ! "".equals(this.pojoCotizacionMain.getFolio()))
					nombreReporte = "." + this.pojoCotizacionMain.getFolio();
				else if (this.pojoCotizacionMain.getId() != null && this.pojoCotizacionMain.getId() > 0L)
					nombreReporte = "." + this.pojoCotizacionMain.getId();
				nombreReporte = "cotizacion" + nombreReporte + ".xls";
				
				// Parametros del reporte
				HashMap<String, Object> paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idCotizacion", this.pojoCotizacionMain.getId());

				// Parametros para la ejecucion del reporte
				HashMap<String, String>params = new HashMap<String, String>();
				params.put("idPrograma", 	  "167");
				params.put("nombreDocumento", nombreReporte);
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);

				log.info("REPORTE COTIZACION: Ejecutamos reporte");
				Respuesta respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if(respuesta.getErrores().getCodigoError() != 0L) {
					log.error("Error Interno - No se pudo ejecutar el reporte :: " + respuesta.getErrores().getDescError());
		    		control(true, -1, "Error Interno - No se pudo ejecutar el reporte");
					return;
				}
				
				// Recogemos reporte
				log.info("REPORTE COTIZACION: Recuperamos contenido de reporte");
				contenidoReporte = (byte[]) respuesta.getBody().getValor("contenidoReporte");
				if (contenidoReporte == null) {
					log.error("Error Interno - No se pudo obtener el reporte de cotizacion ID " + this.pojoCotizacion.getId());
		    		control(true, -1, "Error Interno - No se pudo recuperar el reporte de Cotizacion " + this.pojoCotizacion.getFolio());
					return;
				}

				log.info("REPORTE COTIZACION: Lo guardo en SESSION");
				this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
				this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte")); 	
				this.httpSession.setAttribute("nombreDocumento", nombreReporte);
				log.info("REPORTE COTIZACION: Terminado");
			}
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.reporte", e);
    		control(true);
    	}
	}
	
	public void enviarCotizacion() {
		byte[] contenidoReporte = null;
		String nombreReporte = "";
		
		try {
    		control();
			if (this.pojoCotizacion != null) {
				if (this.pojoCotizacion.getFolio() != null && ! "".equals(this.pojoCotizacion.getFolio()))
					nombreReporte = "." + this.pojoCotizacion.getFolio();
				else if (this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L)
					nombreReporte = "." + this.pojoCotizacion.getId();
				nombreReporte = "cotizacion" + nombreReporte + ".xls";
				
				if ("".equals(this.email)) {
		    		if (this.isDebug) log.info("Error 8 - Sin Email especificado");
		    		control(8);
					return;
				}
				
				// Parametros del reporte
				HashMap<String, Object> paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idCotizacion", this.pojoCotizacion.getId());

				// Parametros para la ejecucion del reporte
				HashMap<String, String>params = new HashMap<String, String>();
				params.put("idPrograma", 	  "167");
				params.put("nombreDocumento", nombreReporte);
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);
				
				Respuesta respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if(respuesta.getErrores().getCodigoError() != 0L) {
					log.error("Error Interno - No se pudo ejecutar el reporte :: " + respuesta.getErrores().getDescError());
		    		control(true, -1, "Error Interno - No se pudo ejecutar el reporte");
					return;
				}
				
				// Recogemos reporte
				contenidoReporte = (byte[]) respuesta.getBody().getValor("contenidoReporte");
				if (contenidoReporte == null) {
					log.error("Error Interno - No se pudo obtener el reporte de cotizacion ID " + this.pojoCotizacion.getId());
		    		control(true, -1, "Error Interno - No se pudo recuperar el reporte de Cotizacion #" + this.pojoCotizacion.getId());
					return;
				}
				
				// Parametros para envio de correo
				HashMap<String, String> correo = new HashMap<>();
				correo.put("from", this.usuarioEmail); 
				correo.put("fromPass", "disesa12");
				correo.put("to", this.email);
				correo.put("subject", this.emailAsunto);
				correo.put("body", this.emailCuerpo);
				
				// Adjuntos
				HashMap<String, Object> adjuntos = new HashMap<String, Object>();
				adjuntos.put(nombreReporte, contenidoReporte);
				
				respuesta = this.ifzReportes.enviarCorreo(correo, adjuntos);
				if(respuesta.getErrores().getCodigoError() != 0L) {
					log.error("Error Interno - No se pudo enviar el reporte por correo electronico :: " + respuesta.getErrores().getDescError());
		    		control(true, -1, "Error Interno - No se pudo enviar el reporte por correo electronico");
					return;
				}

				if (this.isDebug) log.info("Error 9 - Correo enviado desde " + this.usuarioEmail + " a " + this.email);
	    		control(false, 9, "Correo enviado desde " + this.usuarioEmail);
			}
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.enviarCotizacion", e);
    		control(true);
    	}
	}

	public void cargarFamilias() {
		try {
			if (this.listFamilias == null)
				this.listFamilias = new ArrayList<ConValores>();
			this.listFamilias.clear();
			
			if (this.listFamiliasItems == null)
				this.listFamiliasItems = new ArrayList<SelectItem>();
			this.listFamiliasItems.clear();
			
			// Cargamos la lista de familias
			if (this.isDebug) log.info("Cargamos lista de familias");
			this.listFamilias = this.ifzConValores.buscaValorGrupo("descripcion", "", pojoGpoFamilias);
			
			// Generamos la lista auxiliar de familias
			if(this.listFamilias != null && ! this.listFamilias.isEmpty()) {
				if (this.isDebug) log.info("Generamos lista de items [familias]");
				for (ConValores cv : this.listFamilias) {
					if (cv.getValor().equals(cv.getDescripcion()))
						this.listFamiliasItems.add(new SelectItem(cv.getId(), cv.getDescripcion()));
					else
						this.listFamiliasItems.add(new SelectItem(cv.getId(), cv.getDescripcion() + " (" + cv.getValor() + ")"));
				}
			}
		} catch (Exception e) {
			log.error("Error al cargar las familias", e);
		}
	}
	
	public void cargarCompradores() {
		try {
			if (this.listCompradores == null)
				this.listCompradores = new ArrayList<Empleado>();
			this.listCompradores.clear();
			
			if (this.listCompradoresItems == null)
				this.listCompradoresItems = new ArrayList<SelectItem>();
			this.listCompradoresItems.clear();
			
			// Cargamos la lista de compradores
			if (this.isDebug)
				log.info("Cargamos lista de compradores [extendido]");
			this.listCompradores = this.ifzEmpleados.findAll();
			
			// Generamos la lista auxiliar de compradores
			if(! this.listCompradores.isEmpty()) {
				if (this.isDebug)
					log.info("Generamos lista de items [compradores]");
				for (Empleado var : this.listCompradores) {
					this.listCompradoresItems.add(new SelectItem(var.getId(), var.getNombre()));
				}
			}
		} catch (Exception e) {
			log.error("Error al cargar compradores", e);
		}
	}
	
	public void cargarMonedas() {
		try {
			if (this.listMonedasItems == null)
				this.listMonedasItems = new ArrayList<SelectItem>();
			this.listMonedasItems.clear();
			
			// Cargamos la lista de monedas
			if (this.isDebug) log.info("Cargando lista de monedas");
			this.listMonedas = this.ifzMonedas.findAll();
			if(this.listMonedas != null && ! this.listMonedas.isEmpty()) {
				if (this.isDebug) log.info("Generando lista de items (monedas)");
				for (Moneda var : this.listMonedas) {
					if ("MXN".equals(var.getAbreviacion()))
						this.idMonedaBase = var.getId();
					this.listMonedasItems.add(new SelectItem(var.getId(), var.getNombre() + " (" + var.getAbreviacion() + ")"));
				}
			}
		} catch (Exception e) {
			log.error("Error al cargar las Monedas", e);
		}
	}
	
	private long getMonedaBase(String monedaAbreviatura) {
		long idMoneda = 0L;
		
		if (this.listMonedas == null || this.listMonedas.isEmpty()) 
			cargarMonedas();
		
		for (Moneda var : this.listMonedas) {
			if (!monedaAbreviatura.equals(var.getAbreviacion()))
				continue;
			idMoneda = var.getId();
			break;
		}
		
		return idMoneda;
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
	
	public void totalizarCotizacionesDetalles() {
		this.subtotal 	= 0;
		this.iva 		= 0;
		this.total 		= 0;
		
		if (this.listCotiDetalles == null)
			this.listCotiDetalles = new ArrayList<PreCotizacionDetalle>();
		
		if (this.isDebug)
			log.info("Totalizamos detalle de cotizaciones.");
		for(PreCotizacionDetalle var : this.listCotiDetalles) {
			if (! var.getSeleccionar()) continue;
			this.subtotal += var.getImporte();
			this.iva += ((var.getImporte() * this.porcentajeIva) / 100);
		}
		
		this.total = this.subtotal + this.iva;
		comprobarSeleccionarTodos();
	}
	
	public void filtrarProductos() {
		// Inicializamos la lista respaldo si corresponde
		if (this.listCotiDetallesFull == null)
			this.listCotiDetallesFull = new ArrayList<PreCotizacionDetalle>();

		// Si el respaldo no tiene datos, lo llenamos
		if (this.listCotiDetallesFull.isEmpty()) {
			for(PreCotizacionDetalle var : this.listCotiDetalles) {
				this.listCotiDetallesFull.add(var);
			}
		} else {
			// Actualizamos los datos del respaldo por si hubo algun cambio en las propiedades
			for (PreCotizacionDetalle var1 : this.listCotiDetalles) {
				for (PreCotizacionDetalle var2 : this.listCotiDetallesFull) {
					if (var1.getIdProducto().getId() == var2.getIdProducto().getId()) {
						var2.setCotizar(var1.getCotizar());
						var2.setImporte(var1.getImporte());
						var2.setSeleccionar(var1.getSeleccionar());
						break;
					}
				}
			}
		}
		
		this.listCotiDetalles.clear();
		for(PreCotizacionDetalle var : this.listCotiDetallesFull) {
			if (var.getProductoFamiliaId() == this.valFamilia || this.valFamilia == 0L)
				this.listCotiDetalles.add(var);
		}
	}
	
	public boolean validacionesCotizacion() {
		if (this.isDebug)
			log.info("Validando Cotizacion.");
		/*if (this.pojoCotizacion.getFlete() <= 0) {
    		this.operacion = true;
			this.mensaje = "ERROR 7";
			this.tipoMensaje = 7;
			return false;
		}*/
		
		if (this.pojoCotizacion.getTiempoEntrega() <= 0) {
    		this.operacion = true;
			this.mensaje = "ERROR 6";
			this.tipoMensaje = 6;
			if (this.isDebug)
				log.info("Validando Cotizacion.  ERROR 6 - El tiempo de entrega debe ser mayor a cero");
			return false;
		}
		
		if(this.listCotiDetalles == null || this.listCotiDetalles.isEmpty()) {
    		this.operacion = true;
			this.mensaje = "ERROR 4";
			this.tipoMensaje = 4;
			if (this.isDebug)
				log.info("Validando Cotizacion.  ERROR 4 - Lista de productos vacia");
			return false;
		}
		
		boolean valido = false;
		for(PreCotizacionDetalle var: this.listCotiDetalles) {
			if (var.getSeleccionar()) {
				valido = true;
				break;
			}
		}
		
		if (! valido) {
    		this.operacion = true;
			this.mensaje = "ERROR 5";
			this.tipoMensaje = 5;
			if (this.isDebug)
				log.info("Validando Cotizacion.  ERROR 5 - Ningun producto seleccionado");
			return false;
		}
		
		return true;
	}
		
	public void comprobarSeleccionarTodos() {
		for (PreCotizacionDetalle var : this.listCotiDetalles) {
			this.seleccionarTodos = true;
			if (! var.getSeleccionar()) {
				this.seleccionarTodos = false;
				break;
			}
		}
	}
	
	private boolean comprobarPrimeraCotizacion() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		try {
			if (this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L)
				return false;
			
			params.put("idObra", this.pojoObra.getId());
			params.put("estatus", 0);
			
			return this.ifzCotizaciones.findByProperties(params).isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	private CotizacionDetalleExt comprobarProducto(CotizacionDetalleExt item) {
		HashMap<String, String> params = new HashMap<String, String>();
		List<ObraAlmacenesExt> lista = null;
		ObraAlmacenesExt almPrincipal = null;
		AlmacenProducto prodExistente = null;
		
		try {
			control();
			if (this.isDebug) log.info("Comprobamos producto.");
			params.put("idObra", this.pojoObra.getId().toString());
			params.put("almacenPrincipal", "1");
			
			lista = this.ifzObraAlmacen.findExtByProperties(params, 0);
			if (lista == null || lista.isEmpty()) {
				if (this.isDebug) log.error("ERROR 10 - La obra no tiene asignado un almacen como principal");
				control(true, 10, "La obra no tiene asignado un almacen como principal");
				log.error("ERROR 10 - " + this.mensaje);
				return item;
			}
			
			almPrincipal = lista.get(0);
			prodExistente = this.ifzAlmacenProducto.findAlmacenProducto(almPrincipal.getIdAlmacen().getId(), item.getIdProducto().getId());
			if (prodExistente != null && prodExistente.getId() != null && prodExistente.getId() > 0L && prodExistente.getExistencia() > 0) {
				if (prodExistente.getExistencia() >= item.getCotizar())
					item.setCotizar(0);
				else
					item.setCotizar(item.getCotizar() - prodExistente.getExistencia());
			}
		} catch (Exception e) {
			log.error("Error en Compras.CotizacionAction.comprobarProducto", e);
			control(true);
		}
		
		return item;
	}
	
	private void generaTraspaso(CotizacionDetalleExt item) {
		HashMap<String, String> params = new HashMap<String, String>();
		List<ObraAlmacenesExt> almacenesObra = null;
		ObraAlmacenesExt almacenObra = null;
		ObraAlmacenesExt almacenObraOrigen = null;
		Empleado empRecibe = null;
		AlmacenTraspasoExt pojoTraspaso = null;
		boolean existeTraspaso = false;
		double cantidadATraspasar = 0;
		double cantidadTraspasado = 0;
		
		try {
			control();
			if (this.listTraspasos == null)
				this.listTraspasos = new ArrayList<AlmacenTraspasoExt>();
			
			if (this.listTraspasosDetalles == null)
				this.listTraspasosDetalles = new ArrayList<TraspasoDetalleExt>();
			
			if (this.isDebug)
				log.info("Generamos Traspaso.");
			
			// encuentro el almacen donde puede tener existencia, Almacenes ligados a la OBRA.
			params.put("tipo", "2");
			params.put("idObra", this.pojoObra.getId().toString());
			almacenesObra = this.ifzObraAlmacen.findExtByProperties(params, 0);
			if (almacenesObra == null || almacenesObra.isEmpty()) {
				if (this.isDebug) log.info("La obra no tiene asignados almacenes generales");
				control(true, 11, "La obra no tiene asignados almacenes generales");
				log.error("ERROR 11 - " + this.mensaje);
				return;
			}
			
			almacenObraOrigen = null;
			cantidadATraspasar = item.getCantidad();
			for (ObraAlmacenesExt var : almacenesObra) {
				AlmacenProducto prodExistente = this.ifzAlmacenProducto.findAlmacenProducto(var.getIdAlmacen().getId(), item.getIdProducto().getId());
				if (prodExistente != null && prodExistente.getId() != null && prodExistente.getId() > 0L && prodExistente.getExistencia() > 0) {
					almacenObraOrigen = var;
					
					// Comprobamos si ya generamos un TRASPASO con similitud de almacen origen
					existeTraspaso = false;
					for (AlmacenTraspasoExt t : this.listTraspasos) {
						if (t.getAlmacenOrigen().getId().equals(almacenObraOrigen.getIdAlmacen().getId())) {
							existeTraspaso = true;
							pojoTraspaso = t;
							break;
						}
					}
					
					// genera el traspaso si corresponde
					if (! existeTraspaso) {
						// Encuentro el almacen con marca de principal en la obra si corresponde
						if (almacenObra == null) {
							params.clear();
							almacenesObra.clear();
							
							params.put("idObra", this.pojoObra.getId().toString());
							params.put("almacenPrincipal", "1");
							almacenesObra = this.ifzObraAlmacen.findExtByProperties(params, 0);
							if (almacenesObra == null || almacenesObra.isEmpty()) {
								if (this.isDebug) log.info("La obra no tiene asignado un almacen principal");
								control(true, 10, "La obra no tiene asignado un almacen principal");
								log.error("ERROR 10 - " + this.mensaje);
								return;
							}
							
							// Obtenemos almacen
							almacenObra = almacenesObra.get(0);
						}
						
						// Empleado que recibe los productos
						empRecibe = this.ifzEmpleados.findById(this.pojoObra.getIdResponsable().getId());
						
						pojoTraspaso = new AlmacenTraspasoExt();
						pojoTraspaso.setId((long) (listTraspasos.size() + 1));
						pojoTraspaso.setAlmacenOrigen(almacenObraOrigen.getIdAlmacen());
						pojoTraspaso.setAlmacenDestino(almacenObra.getIdAlmacen());
						pojoTraspaso.setEntrega(null);
						pojoTraspaso.setRecibe(empRecibe);
						pojoTraspaso.setEstatus(0);
						pojoTraspaso.setTipo(3); // Solicitud a bodega --- pojoTraspaso.setTipo(1);
						pojoTraspaso.setCreadoPor(this.usuarioId);
						pojoTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
						pojoTraspaso.setModificadoPor(this.usuarioId);
						pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
						
						listTraspasos.add(pojoTraspaso);
					}

					cantidadTraspasado = cantidadATraspasar;
					if (cantidadATraspasar > prodExistente.getExistencia()) {
						cantidadATraspasar = cantidadATraspasar - prodExistente.getExistencia();
						cantidadTraspasado = prodExistente.getExistencia();
					} 
										
					// detalles de traspaso
					TraspasoDetalleExt det = new TraspasoDetalleExt();
					det.setIdAlmacenTraspaso(pojoTraspaso.getId());
					det.setProducto(item.getIdProducto());
					det.setCantidad(item.getCantidad());
					det.setCantidadRecibida(cantidadTraspasado);
					det.setEstatus(1);
					
					det.setCreadoPor(pojoTraspaso.getCreadoPor());
					det.setFechaCreacion(pojoTraspaso.getFechaCreacion());
					det.setModificadoPor(pojoTraspaso.getModificadoPor());
					det.setFechaModificacion(pojoTraspaso.getFechaModificacion());
					
					this.listTraspasosDetalles.add(det);
				}
			}
			
			if (almacenObraOrigen == null) {
				log.warn("No podemos generar un TRASPASO ya que no se encontro existensia del producto en ningun Almacen");
				return;
			}
		} catch (Exception e) {
			control(true);
    		log.error("Error en Compras.CotizacionAction.generaTraspaso", e);
		}
	}

	private void guardaTraspasos() {
		long idTemporal = 0;
		
		try {
			// Ciclo para guardar los traspasos correspondientes
			if (this.isDebug)
				log.info("Guardamos traspasos");
			for (AlmacenTraspasoExt var1 : this.listTraspasos) {
				idTemporal = var1.getId();
				
				// Guardamos traspaso
				var1.setId(this.ifzTraspaso.save(var1));

				// Guardamos los detalles de traspasos
				AlmacenProducto almacenSalida = null;
				for (TraspasoDetalleExt var2 : this.listTraspasosDetalles) {
					if (! var2.getIdAlmacenTraspaso().equals(idTemporal)) continue;
					
					// Asignamos ID de traspaso
					var2.setIdAlmacenTraspaso(var1.getId());
					
					// guardamos detalle
					var2.setId(this.ifzTraspasoDetalle.save(var2));
					
					// descontamos existensia de almacen origen
					almacenSalida = this.ifzAlmacenProducto.findAlmacenProducto(var1.getAlmacenOrigen().getId(), var2.getProducto().getId());
					if(almacenSalida != null) {
						almacenSalida.setExistencia(almacenSalida.getExistencia() - var2.getCantidad());
						this.ifzAlmacenProducto.update(almacenSalida);
					}
				}
				
				enviaCorreoEncargadoAlmacen(var1);
			}
			
			notificacionDeTraspasos();
			
			this.listTraspasos.clear();
			this.listTraspasosDetalles.clear();
		} catch (Exception e) {
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en Compras.CotizacionAction.guardaTraspasos", e);
		}
	}
	
	private void notificacionDeTraspasos() {
		HashMap<String, String> correo = new HashMap<>();
		Respuesta respuesta = new Respuesta();
		String emailEncargado = "";
		String emailAsunto = "";
		String emailCuerpo = "";
		String productos = "";

		try {
			emailAsunto = "Notificacion de Traspado de material";
			emailCuerpo = "Atencion <b>%1<b>.<br><br>Se le informa de un <i>Traspaso de Material<i/> proveniente de <b>%2<b/> por <i>%3<i/>.";
			for (AlmacenTraspasoExt var : this.listTraspasos) {
				emailEncargado = var.getRecibe().getEmail();
				emailCuerpo = String.format(emailCuerpo, var.getRecibe().getNombre(), var.getAlmacenOrigen().getNombre(), var.getEntrega().getNombre());
				
				for (TraspasoDetalleExt det : this.listTraspasosDetalles) {
					if (! var.getId().equals(det.getIdAlmacenTraspaso())) continue;
					productos += "<tr>";
					productos += "<td>" + det.getProducto().getClave() + "</td>";
					productos += "<td>" + det.getProducto().getDescripcion() + "</td>";
					productos += "<td>" + det.getProducto().getUnidadMedida().getDescripcion() + "</td>";
					productos += "<td>" + det.getCantidad() + "</td>";
					productos += "</tr>";
				}
				
				if (! "".equals(productos)) {
					emailCuerpo += "<br><br>Contenido del Traspado:<br>";
					productos = "<table><tr><th>Clave</th><th>Descripcion</th><th>U.M.</th><th>Cantidad</th></tr>" + productos;
					productos += "</table>";
					emailCuerpo += productos;
				}
				
				// Parametros para envio de correo
				if (! "".equals(emailEncargado)) {
					correo.clear();
					correo.put("from", this.usuarioEmail); 
					correo.put("fromPass", "disesa12");
					correo.put("to", emailEncargado);
					correo.put("subject", emailAsunto);
					correo.put("body", emailCuerpo);
					
					respuesta = this.ifzReportes.enviarCorreo(correo, null);
					if(respuesta.getErrores().getCodigoError() != 0L) {
						log.error("Error Interno - No se pudo enviar la Notificacion por correo electronico :: " + respuesta.getErrores().getDescError());
			    		control(true, -1, "Error Interno - No se pudo enviar la Notificacion");
					}
				}
			}
		} catch (Exception e) {
			log.error("Error en Compras.CotizacionesAction.enviaCorreoEncargadoAlmacen al notificar el traspaso", e);
		}
	}
	
	private boolean enviaCorreoEncargadoAlmacen(AlmacenTraspasoExt item) {
		// TODO Enviar notificacion al encargado de almacen (TRASPASO)
		return true;
	}
	
	public void verificaAlmacenesObra() {
		HashMap<String, String> params = new HashMap<String, String>();
		List<ObraAlmacenes> almacenesObra = null;
		
		try {
    		control();
			if(this.pojoObra.getTipoObra() != TiposObra.Administrativa.ordinal()) {
				if (this.comprobarPrimeraCotizacion()) {
					params.put("tipo", "2");
					params.put("idObra", this.pojoObra.getId().toString());
					almacenesObra = this.ifzObraAlmacen.findByProperties(params, 0);
					if (almacenesObra == null || almacenesObra.isEmpty()) {
						if (this.isDebug) log.info("La obra no tiene asignados almacenes generales");
						control(true, 11, "La obra no tiene asignados almacenes generales");
						log.error("ERROR 11 - " + this.mensaje);
					}
				}
			}
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.verificaAlmacenesObra", e);
    		control(true);
    	}
	}

	public void filtrarObrasSinInsumos() throws Exception {
		List<Obra> resultado = new ArrayList<Obra>();
		List<Insumos> listaInsumos = null;
		
		for (Obra var : this.listObrasGrid) {
			listaInsumos = this.ifzInsumos.findByProperty("idObra", var.getId(), 0);
			if (listaInsumos == null || listaInsumos.isEmpty()) continue;
			resultado.add(var);
		}
		
		this.listObrasGrid.clear();
		this.listObrasGrid.addAll(resultado);
	}
	
	private boolean comprobarInsumos() {
		boolean resultado = false;
		
		List<Insumos> listaInsumos = this.ifzInsumos.findByProperty("idObra", this.pojoObra.getId(), 0);
		if (listaInsumos != null && ! listaInsumos.isEmpty()) 
			resultado = true;
		
		return resultado;
	}

	private boolean detalleFromInsumos() throws Exception {
		try {
			control();
			if (this.listCotiDetalles == null)
				this.listCotiDetalles = new ArrayList<PreCotizacionDetalle>();
			this.listCotiDetalles.clear();
			
			// Comprobamos los insumos de la obra
			log.info("Comprobamos insumos");
			List<InsumosExt> listaInsumos = this.ifzInsumos.findByPropertyExt("idObra", this.pojoCotizacion.getIdObra().getId(), 80);
			if (listaInsumos == null || listaInsumos.isEmpty()) {
				log.info("La obra " + this.pojoCotizacion.getIdObra().getId() + " - " + this.pojoCotizacion.getIdObra().getNombre() + " no tiene Explosion de Insumos");
				control("La obra no tiene Explosion de Insumos");
				return false;
			}
			
			if (listaInsumos != null && ! listaInsumos.isEmpty() && listaInsumos.get(0).getId() != null && listaInsumos.get(0).getId() <= 0L) {
				log.info("La obra " + this.pojoCotizacion.getIdObra().getId() + " - " + this.pojoCotizacion.getIdObra().getNombre() + " no tiene Explosion de Insumos");
				control("La obra no tiene Explosion de Insumos");
				return false;
			}
			
			// Recuperamos el detalle del insumo
			log.info("Recuperamos detalles de Explosion de Insumos");
			List<InsumosDetallesExt> listaInsumosDetalles = this.ifzInsumosDet.findByPropertyExt("idInsumo", listaInsumos.get(0).getId(), 0);
			if (listaInsumosDetalles == null || listaInsumosDetalles.isEmpty()) {
				log.info("La Explosion de Insumos no tiene productos");
				control("La Explosion de Insumos no tiene productos");
				return false;
			}

			log.info("Genero listado de Detalles de Explosion de Insumos.");
			for (InsumosDetallesExt var : listaInsumosDetalles) {
				if (this.idMoneda <= 0L)
					this.idMoneda = var.getIdProducto().getIdMoneda().getId();
				this.listCotiDetalles.add(new PreCotizacionDetalle(var));
			}
			log.info("Detalles de Explosion de Insumos recuperados.");
			return true;
		} catch (Exception e) {
			log.error("Error en Compras.CotizacionAction.seleccionarObra", e);
			control(true);
			return false;
		}
	}

	private boolean detalleFromRequisicion() throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		double precioUnitario = 0;
		double tipoCambio = 0;
		
		try {
			control();
			if (this.listCotiDetalles == null)
				this.listCotiDetalles = new ArrayList<PreCotizacionDetalle>();
			this.listCotiDetalles.clear();
			
			// Comprobamos requisicion
			if (this.pojoRequisicion == null || this.pojoRequisicion.getId() == null || this.pojoRequisicion.getId() <= 0L) {
	    		log.info("ERROR INTERNO - No selecciono ninguna Requisicion");
				control("No selecciono ninguna Requisicion");
				return false;
			}
			
			// Agregamos la requisicion a la cotizacion
			this.pojoCotizacion.setIdRequisicion(this.ifzReq.findExtById(this.pojoRequisicion.getId()));
			this.pojoCotizacion.setIdMoneda(this.pojoRequisicion.getIdMoneda());
					
			// Recuperamos el detalle de la requisicion
			log.info("Recuperamos detalles de Requisicion");
			params.put("idRequisicion", this.pojoRequisicion.getId());
			//params.put("idCotizacion", 0L);
			this.listReqDetalles = this.ifzReqDetalle.findExtByProperties(params, 0);
			//this.listReqDetalles = this.ifzReqDetalle.findExtByProperty("idRequisicion", this.pojoRequisicion.getId(), 0);
			if (this.listReqDetalles == null || this.listReqDetalles.isEmpty()) {
				log.info("ERROR INTERNO - La Requisicion no tiene productos o ya han sido usados en cotizaciones");
				control("La Requisicion no tiene productos disponibles");
				return false;
			}

			log.info("Genero listado de Detalles de Requisicion.");
			this.seleccionarTodos = true;
			PreCotizacionDetalle det = null;
			for(RequisicionDetalleExt var : listReqDetalles) {
				/*if (var.getIdCotizacion() > 0) 
					continue;*/
				
				det = new PreCotizacionDetalle(var);
				det.setSeleccionar(true);

				if (this.idMonedaBase == this.pojoCotizacion.getIdMoneda() && this.idMonedaBase != var.getIdProducto().getIdMoneda().getId()) {
					precioUnitario = var.getIdProducto().getPrecioCompra();
					tipoCambio = var.getIdProducto().getTipoCambio();
					if (tipoCambio <= 1)
						tipoCambio = recuperaTipoCambioActual(this.pojoCotizacion.getIdMoneda(), var.getIdProducto().getIdMoneda().getId());
					precioUnitario = precioUnitario * tipoCambio;
					det.setPrecioUnitario(precioUnitario);
				} else if (this.idMonedaBase != this.pojoCotizacion.getIdMoneda() && this.idMonedaBase == var.getIdProducto().getIdMoneda().getId()) {
					precioUnitario = var.getIdProducto().getPrecioCompra();
					tipoCambio = var.getIdProducto().getTipoCambio();
					if (tipoCambio <= 1)
						tipoCambio = recuperaTipoCambioActual(var.getIdProducto().getIdMoneda().getId(), this.pojoCotizacion.getIdMoneda());
					precioUnitario = precioUnitario / tipoCambio;
					det.setPrecioUnitario(precioUnitario);
				} else {
					precioUnitario = var.getIdProducto().getPrecioCompra();
					tipoCambio = 1.0;
					det.setPrecioUnitario(precioUnitario);
				}
				
				// Añado el detalle a la cotizacion
				this.listCotiDetalles.add(det);
			}
			log.info("Detalles de requisicion recuperados.");
			return true;
		} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.detalleFromRequisicion", e);
			control(true);
			return false;
    	}
	}
	
	private void actualizaEstatusRequisicion() {
		try {
			this.pojoCotizacion.getIdRequisicion().setEstatus(0);
			this.ifzReq.update(this.pojoCotizacion.getIdRequisicion());

			if (this.pojoCotizacion.getIdRequisicion() != null && this.pojoCotizacion.getIdRequisicion().getId() != null && this.pojoCotizacion.getIdRequisicion().getId() > 0L) {
				List<RequisicionDetalle> listReqDetalles = this.ifzReqDetalle.findByProperty("idRequisicion", this.pojoCotizacion.getIdRequisicion().getId(), 0);
				if (listReqDetalles != null && ! listReqDetalles.isEmpty()) {
					for (CotizacionDetalleExt var : this.listDetalles) {
						for (RequisicionDetalle detReq : listReqDetalles) {
							if (var.getIdProducto().getId().equals(detReq.getIdProducto())) {
								detReq.setIdCotizacion(this.pojoCotizacion.getId());
								detReq.setCotizacionFolio(this.pojoCotizacion.getFolio());
								this.ifzReqDetalle.update(detReq);
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error en Compras.CotizacionesAction.actualizaEstatusRequisicion", e);
			control(true);
		}
	}
	
	@SuppressWarnings("unused")
	private void liberarRequisicion() {
		try {
			if (this.pojoCotizacionBorrar.getIdRequisicion() != null && this.pojoCotizacionBorrar.getIdRequisicion() > 0L) {
				Requisicion pojoReq = this.ifzReq.findById(this.pojoCotizacionBorrar.getIdRequisicion());
				if (pojoReq != null) {
					pojoReq.setEstatus(0);
					this.ifzReq.update(pojoReq);
					
					List<RequisicionDetalle> listReqDetalles = this.ifzReqDetalle.findByProperty("idRequisicion", this.pojoCotizacionBorrar.getIdRequisicion(), 0);
					if (listReqDetalles != null && ! listReqDetalles.isEmpty()) {
						for (RequisicionDetalle detReq : listReqDetalles) {
							if (detReq.getIdCotizacion() == this.pojoCotizacionBorrar.getId().longValue()) {
								detReq.setIdCotizacion(0);
								detReq.setCotizacionFolio("");
								this.ifzReqDetalle.update(detReq);
								break;
							}
						}
					}
				}
				
				
			}
		} catch (Exception e) {
			log.error("Error en Compras.CotizacionesAction.liberarRequisicion", e);
			control(true);
		}
	}
	
	private void actualizarPreciosOrdenesCompra() {
		List<OrdenCompra> listOC = null;
		List<OrdenCompraDetalle> ocDetalles = null;
		
		if ((this.pojoCotizacion == null) || (this.listDetalles == null || this.listDetalles.isEmpty()))
			return;

		try {
			listOC = this.ifzOC.findByProperty("idCotizacion", this.pojoCotizacion.getId(), 0);
			if (listOC != null && ! listOC.isEmpty()) {
				for (OrdenCompra oc : listOC) {
					ocDetalles = this.ifzOCDetalles.findByProperty("idOrdenCompra", oc.getId(), 0);
					if (ocDetalles == null || ocDetalles.isEmpty()) 
						continue;
					
					for (CotizacionDetalleExt var : this.listDetalles) {
						for(OrdenCompraDetalle det : ocDetalles) {
							if (! var.getIdProducto().getId().equals(det.getIdProducto())) continue;
							det.setPrecioUnitario(var.getPrecioUnitario());
							det.setModificadoPor(this.usuarioId);
							det.setFechaModificacion(Calendar.getInstance().getTime());
							this.ifzOCDetalles.update(det);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.actualizarPreciosOrdenesCompra: No pude actualizar los precios en ninguna Orden de Compra", e);
			control("Error Interno - No se pudo actualizar los precios en ninguna Orden de Compra");
		} finally {
			if (this.listDetalles != null && ! this.listDetalles.isEmpty())
				this.listDetalles.clear();
			if (ocDetalles != null && ! ocDetalles.isEmpty())
				ocDetalles.clear();
			if (listOC != null && ! listOC.isEmpty())
				listOC.clear();
		}
	}
	
	// BUSQUEDA OBRAS
	// --------------------------------------------------------------------------------------
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

			if (this.isDebug) log.info("Buscando obras. " + this.campoBusquedaObras + ":" + this.valorBusquedaObras);
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikePropertyExt(this.campoBusquedaObras, this.valorBusquedaObras, this.valorBusquedaTipoObra);
			if(this.listObras.isEmpty()) {
				if (this.isDebug) log.info("Buscando obras. " + this.campoBusquedaObras + ":" + this.valorBusquedaObras);
				control(2);
				return;
			}
    	} catch (Exception e) {
			control(true);
			if (this.listObras != null) this.listObras.clear();
    		log.error("Error en Compras.CotizacionAction.buscarObras", e);
    	}
	}

	public void seleccionarObra() throws Exception {
		// Recuperamos los insumos de la obra seleccionada
		detalleFromInsumos();
		nuevaBusquedaObras();
	}

	// BUSQUEDA PROVEEDORES
	// --------------------------------------------------------------------------------------	
	public void nuevaBusquedaProveedores() {
		this.campoBusquedaProveedores = this.tiposBusquedaProveedores.get(0).getDescription();
		this.valorBusquedaProveedores = "";
		this.valorBusquedaTipoProveedor = "N";
		
		if (this.listProveedores != null)
			this.listProveedores.clear();
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
			// Asigno el tipo de persona de proveedor: Persona (P) o Negocio (N)
			this.pojoCotizacion.setTipoPersonaProveedor(this.valorBusquedaTipoProveedor);
			// Recuperamos el contacto del proveedor seleccionado
			if (this.isDebug) log.info("Recuperando contacto para proveedor: " + this.pojoCotizacion.getIdProveedor().getId() + " - " + this.pojoCotizacion.getIdProveedor().getNombre());
			PersonaExt pojoContacto = this.ifzCotizaciones.findContactoByProveedor(this.pojoCotizacion.getIdProveedor(), this.valorBusquedaTipoProveedor);
			if (pojoContacto == null) log.info("El Proveedor no tiene asignado ningun Contacto");
			if (pojoContacto != null)
				this.pojoCotizacion.setIdContacto(pojoContacto);
			
			nuevaBusquedaProveedores();
    	} catch (Exception e) {
			control(true);
    		log.error("Error en Compras.CotizacionAction.seleccionarProveedor", e);
    	}
	}
		
	// REQUISICIONES
	// --------------------------------------------------------------------------------------	
	public void reqNuevaBusqueda() {
		this.reqCampoBusqueda = reqOpcionesBusqueda.get(0).getDescription();
		this.reqValorBusqueda = "";
		this.reqPaginacion = 1;
		
		if (this.listRequisiciones == null)
			this.listRequisiciones = new ArrayList<Requisicion>();
		this.listRequisiciones.clear();
		
		if (this.pojoRequisicion == null)
			this.pojoRequisicion = new Requisicion();
	}
	
	public void reqBuscar() {
		try {
			control();
			if ("".equals(this.reqCampoBusqueda))
				reqCampoBusqueda = reqOpcionesBusqueda.get(0).getDescription();
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("idObra", this.pojoObra.getId().toString());
			params.put(this.reqCampoBusqueda, this.reqValorBusqueda);
			params.put("estatus", "0");
			if (this.tipoMaestro == TipoMaestro.Administrativo)
				params.put("tipo", String.valueOf(this.tipoMaestro.ordinal()));

			this.ifzReq.OrderBy("CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.id desc");
			this.listRequisiciones = this.ifzReq.findLikeProperties(params, 0);
			if(this.listRequisiciones.isEmpty()) {
				if (this.isDebug) log.error("ERROR 2 - La busqueda no regreso resultados");
				control(2);
				return;
			}
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.reqBuscar", e);
			control(true);
    	}
	}
	
	public void reqSeleccionar() {
		try {
			control();
			// Comprobamos requisicion
			if (this.pojoRequisicion == null || this.pojoRequisicion.getId() == null || this.pojoRequisicion.getId() <= 0L) {
				control(2);
				return;
			}
			
			nuevoRequisicion();
    	} catch (Exception e) {
    		log.error("Error en Compras.CotizacionAction.reqSeleccionar", e);
			control(true);
    	}
	}
	
	// --------------------------------------------------------------------------------------
    // Propiedades
    // --------------------------------------------------------------------------------------
	
	public String getCotizacionTitulo() {
		if (this.pojoCotizacion != null && this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L)
			return (this.tipoMaestro == TipoMaestro.Administrativo ? "ADMINISTRATIVA " : "") + this.pojoCotizacion.getId();
		return (this.tipoMaestro == TipoMaestro.Administrativo ? "ADMINISTRATIVA" : "");
	}
	
	public void setCotizacionTitulo(String value) {}
	
	public String getCotizacionFolio() {
		if (this.pojoCotizacion != null) {
			if (this.pojoCotizacion.getFolio() != null && ! "".equals(this.pojoCotizacion.getFolio()))
				return this.pojoCotizacion.getFolio();
		}
		
		return "";
	}
	
	public void setCotizacionFolio(String value) {}
	
	public String getCotizacionObra() {
		if (this.pojoObra != null)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		return "";
	}
	
	public void setCotizacionObra(String value) {}

	public String getCotizacionProveedor() {
		if (this.pojoCotizacion != null) {
			if (this.pojoCotizacion.getIdProveedor() == null)
				return "Sin proveedor";
			return this.pojoCotizacion.getIdProveedor().getId() + " - " + this.pojoCotizacion.getProveedorNombre();
		}
		
		return "";
	}
	
	public void setCotizacionProveedor(String value) {}

	public String getProveedorContacto() {
		if (this.pojoCotizacion != null) {
			if (this.pojoCotizacion.getIdProveedor() != null) {
				if (this.pojoCotizacion.getIdContacto() != null)
					return this.pojoCotizacion.getContacto();
				else
					return "Sin contacto";
			}
		}
		
		return "";
	}
	
	public void setProveedorContacto(String value) {}
	
	public String getCotizacionRequisicion() {
		if (this.pojoCotizacion != null) {
			if (this.pojoCotizacion.getIdRequisicion() != null && this.pojoCotizacion.getIdRequisicion().getId() != null && this.pojoCotizacion.getIdRequisicion().getId() > 0L)
				return "REQ-" + this.pojoCotizacion.getIdRequisicion().getId().toString();
		}
		
		return "";
	}
	
	public void setCotizacionRequisicion(String value) {}
	
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

	public List<Cotizacion> getListCotizaciones() {
		return listCotizaciones;
	}

	public void setListCotizaciones(List<Cotizacion> listCotizaciones) {
		this.listCotizaciones = listCotizaciones;
	}
	
	public Cotizacion getPojoCotizacionMain() {
		return this.pojoCotizacionMain;
	}
	
	public void setPojoCotizacionMain(Cotizacion pojoCotizacion) {
		this.pojoCotizacionMain = pojoCotizacion;
	}

	public CotizacionExt getPojoCotizacion() {
		return pojoCotizacion;
	}

	public void setPojoCotizacion(CotizacionExt pojoCotizacion) {
		this.pojoCotizacion = pojoCotizacion;
	}

	public Cotizacion getPojoCotizacionBorrar() {
		return pojoCotizacionBorrar;
	}

	public void setPojoCotizacionBorrar(Cotizacion pojoCotizacionBorrar) {
		this.pojoCotizacionBorrar = pojoCotizacionBorrar;
	}

	public int getNumPaginaCotizaciones() {
		return numPaginaCotizaciones;
	}

	public void setNumPaginaCotizaciones(int numPaginaCotizaciones) {
		this.numPaginaCotizaciones = numPaginaCotizaciones;
	}

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}

	public int getNumPaginaProductos() {
		return numPaginaProductos;
	}

	public void setNumPaginaProductos(int numPaginaProductos) {
		this.numPaginaProductos = numPaginaProductos;
	}

	public List<ObraExt> getListObras() {
		return listObras;
	}

	public void setListObras(List<ObraExt> listObras) {
		this.listObras = listObras;
	}

	public List<PreCotizacionDetalle> getListCotiDetalles() {
		return listCotiDetalles;
	}

	public void setListCotiDetalles(List<PreCotizacionDetalle> listCotiDetalles) {
		this.listCotiDetalles = listCotiDetalles;
	}

	public List<PreCotizacionDetalle> getListCotiDetallesEliminados() {
		return listCotiDetallesEliminados;
	}

	public void setListCotiDetallesEliminados(List<PreCotizacionDetalle> listCotiDetallesEliminados) {
		this.listCotiDetallesEliminados = listCotiDetallesEliminados;
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
				if (this.isDebug)
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

	public int getNumPaginaProveedores() {
		return numPaginaProveedores;
	}

	public void setNumPaginaProveedores(int numPaginaProveedores) {
		this.numPaginaProveedores = numPaginaProveedores;
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

	public List<SelectItem> getListCompradoresItems() {
		return listCompradoresItems;
	}

	public void setListCompradoresItems(List<SelectItem> listCompradoresItems) {
		this.listCompradoresItems = listCompradoresItems;
	}

	public List<SelectItem> getListFamiliasItems() {
		return listFamiliasItems;
	}

	public void setListFamiliasItems(List<SelectItem> listFamiliasItems) {
		this.listFamiliasItems = listFamiliasItems;
	}
	
	public List<PersonaExt> getListProveedores() {
		return listProveedores;
	}
	
	public void setListProveedores(List<PersonaExt> listProveedores) {
		this.listProveedores = listProveedores;
	}

	public long getValFamilia() {
		return valFamilia;
	}

	public void setValFamilia(long valFamilia) {
		this.valFamilia = valFamilia;
	}

	public long getValComprador() {
		return valComprador;
	}

	public void setValComprador(long valComprador) {
		this.valComprador = valComprador;
	}
	
	public PreCotizacionDetalle getPojoCotiDetalleBorrar() {
		return pojoCotiDetalleBorrar;
	}
	
	public void setPojoCotiDetalleBorrar(PreCotizacionDetalle pojoCotiDetalleBorrar) {
		this.pojoCotiDetalleBorrar = pojoCotiDetalleBorrar;
	}

	public boolean isSeleccionarTodos() {
		return seleccionarTodos;
	}

	public void setSeleccionarTodos(boolean seleccionarTodos) {
		this.seleccionarTodos = seleccionarTodos;
		
		if (! this.seleccionarTodos) return;
		for (PreCotizacionDetalle var : this.listCotiDetalles) {
			var.setSeleccionar(seleccionarTodos);
		}
	}	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailAsunto() {
		return emailAsunto;
	}

	public void setEmailAsunto(String emailAsunto) {
		this.emailAsunto = emailAsunto;
	}

	public String getEmailCuerpo() {
		return emailCuerpo;
	}

	public void setEmailCuerpo(String emailCuerpo) {
		this.emailCuerpo = emailCuerpo;
	}

	public boolean isEsNuevaCotizacion() {
		return esNuevaCotizacion;
	}

	public void setEsNuevaCotizacion(boolean esNuevaCotizacion) {
		this.esNuevaCotizacion = esNuevaCotizacion;
	}

	public long getValObraCancelada() {
		return valObraCancelada;
	}

	public void setValObraCancelada(long valObraCancelada) {
		this.valObraCancelada = valObraCancelada;
	}

	public boolean getPermiteModificar() {
		return permiteModificar;
	}

	public void setPermiteModificar(boolean permiteModificar) {
		this.permiteModificar = permiteModificar;
	}
	
	public Requisicion getPojoRequisicion() {
		return pojoRequisicion;
	}
	
	public void setPojoRequisicion(Requisicion pojoRequisicion) {
		this.pojoRequisicion = pojoRequisicion;
	}
	
	public List<Requisicion> getListRequisiciones() {
		return listRequisiciones;
	}
	
	public void setListRequisiciones(List<Requisicion> listRequisiciones) {
		this.listRequisiciones = listRequisiciones;
	}
	
	public List<SelectItem> getReqOpcionesBusqueda() {
		return reqOpcionesBusqueda;
	}
	
	public void setReqOpcionesBusqueda(List<SelectItem> reqOpcionesBusqueda) {
		this.reqOpcionesBusqueda = reqOpcionesBusqueda;
	}
	
	public String getReqCampoBusqueda() {
		return reqCampoBusqueda;
	}
	
	public void setReqCampoBusqueda(String reqCampoBusqueda) {
		this.reqCampoBusqueda = reqCampoBusqueda;
	}
	
	public String getReqValorBusqueda() {
		return reqValorBusqueda;
	}
	
	public void setReqValorBusqueda(String reqValorBusqueda) {
		this.reqValorBusqueda = reqValorBusqueda;
	}
	
	public int getReqPaginacion() {
		return reqPaginacion;
	}
	
	public void setReqPaginacion(int reqPaginacion) {
		this.reqPaginacion = reqPaginacion;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
	
	public boolean isEditarCotizacion() {
		return editarCotizacion;
	}
	
	public void setEditarCotizacion(boolean editarCotizacion) {
		this.editarCotizacion = editarCotizacion;
	}

	public int getEstatusCotizacion() {
		return estatusCotizacion;
	}

	public void setEstatusCotizacion(int estatusCotizacion) {
		this.estatusCotizacion = estatusCotizacion;
	}

	public String getCotizacionOrdenCompra() {
		if (this.cotizacionOrdenCompra != null && ! "".equals(this.cotizacionOrdenCompra))
			return "OC-" + this.cotizacionOrdenCompra;
		return "";
	}
	
	public void setCotizacionOrdenCompra(String cotizacionOrdenCompra) {
		this.cotizacionOrdenCompra = cotizacionOrdenCompra;
	}
	
	public long getIdMoneda() {
		return idMoneda;
	}
	
	public void setIdMoneda(long idMoneda) {
		this.idMoneda = idMoneda;
	}

	public boolean isEsAdministrativo() {
		return esAdministrativo;
	}

	public void setEsAdministrativo(boolean esAdministrativo) {
		this.esAdministrativo = esAdministrativo;
	}
	
	public boolean getMaestroAdministrativo() {
		return (this.tipoMaestro == TipoMaestro.Administrativo);
	}
	
	public void setMaestroAdministrativo(boolean value) {}


	public List<SelectItem> getListEstatusCotizacionItems() {
		return listEstatusCotizacionItems;
	}


	public void setListEstatusCotizacionItems(
			List<SelectItem> listEstatusCotizacionItems) {
		this.listEstatusCotizacionItems = listEstatusCotizacionItems;
	}
}
