package net.giro.compras.beans.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.giro.adp.beans.InsumosDetallesExt;
import net.giro.adp.beans.InsumosExt;
import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.ObraAlmacenesExt;
import net.giro.adp.beans.ObraExt;
import net.giro.adp.logica.InsumosDetallesRem;
import net.giro.adp.logica.InsumosRem;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.PersonaExt;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalleExt;
import net.giro.compras.beans.CotizacionExt;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.PreCotizacion;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.beans.RequisicionDetalleExt;
import net.giro.compras.beans.RequisicionExt;
import net.giro.compras.beans.SolicitudBodega;
import net.giro.compras.beans.SolicitudBodegaProducto;
import net.giro.compras.comun.EstatusCompras;
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.comun.TiposObra;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.comun.TipoInsumo;
import net.giro.inventarios.comun.TipoMaestro;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;

@ViewScoped
@ManagedBean(name="cotizacionAction")
public class CotizacionesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CotizacionesAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private CotizacionRem ifzCotizaciones;
	private CotizacionDetalleRem ifzCotiDetalles;
	private InsumosDetallesRem ifzInsumosDet;
	private EmpleadoRem ifzEmpleados;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private ReportesRem	ifzReportes;
	private RequisicionDetalleRem ifzReqDetalle;
	private OrdenCompraRem ifzOC;
	private List<Cotizacion> listCotizaciones;
	private List<CotizacionDetalleExt> listDetalles;
	private long idCotizacion;
	private long idExplosionInsumos;
	private int estatusExplosionInsumos;
	private CotizacionExt pojoCotizacion;
	private Obra pojoObraBase;
	private String origenBaseCotizacon;
	// PreCotizacion
	private List<PreCotizacion> listPreCotizacionOrigen;
	private List<PreCotizacion> listPreCotizacion;
	private List<PreCotizacion> listPreCotizacionFull;
	private List<PreCotizacion> listPreCotizacionEliminados;
	private int paginacionProductos;
	// Compradores
	private List<EmpleadoExt> listCompradores;
	private List<SelectItem> listCompradoresItems;
	private long idComprador;	
	// Familias
	private ConGrupoValores grupoFamilias;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	private long idFamilia;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
	private int numPagina;
	// Busqueda Explosion de Insumos
	private InsumosRem ifzInsumos;
	// Busqueda Obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> opcionesBusquedaObras;
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private boolean obraAdministrativa;
	private int paginacionBusquedaObras;
	// Busqueda Requisiciones
	private RequisicionRem ifzReq;
	private List<Requisicion> listRequisiciones;
	private Requisicion pojoRequisicion;
	private List<SelectItem> opcionesBusquedaRequisiciones;	
	private String campoBusquedaRequisiciones;
	private String valorBusquedaRequisiciones;
	private int paginacionBusquedaRequisiciones;
	// Busqueda Proveedores
	private List<PersonaExt> listProveedores;
	private PersonaExt pojoProveedor;
	private List<SelectItem> opcionesBusquedaProveedores;	
	private String campoBusquedaProveedores;
	private String valorBusquedaProveedores;
	private String tipoPersonaBusquedaProveedores;
	private int paginacionBusquedaProveedores;
	// Impuestos IVA
	private ConGrupoValores grupoImpuestos;
	private List<ConValores> listImpuestos;
	private List<SelectItem> listImpuestosItems;
	private long idImpuesto;
	private double porcentajeIva;
	// Almacen y Productos
	private ObraAlmacenesRem ifzObraAlmacen;
	private AlmacenProductoRem ifzInventario;
	// Variables de operacion
    private String usuarioEmail;
    private String usuarioEmailClave;
	private boolean seleccionarTodos;
	private boolean editarCotizacion;
    private boolean permiteModificar;
	private String cotizacionOrdenCompra;
	private TipoMaestro tipoMaestro;
	private boolean esAdministrativo;
	private List<SelectItem> listEstatusCotizacionItems;
	private boolean origenFromRequisicion;
	private boolean copiandoCotizacion;
	private List<Long> idPuestoCompradores;
	// Solicitud Bodega
	private boolean procesoSolicitudBodega;
	private List<SolicitudBodega> listSolicitudes;
	private int paginacionSolicitudBodega;
	private boolean solicitudBodega; 
	// Email
	private String email;
	private String emailCC;
	private String emailCCO;
	private String emailAsunto;
	private String emailCuerpo;
	// Monedas
	private MonedaDAO ifzMonedas;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private long idMoneda;
	private long idMonedaBase;
	private long idMonedaSugerida;
	private double tipoCambio;
	// Control
    private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private List<String> stackTrace;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public CotizacionesAction() {
		InitialContext ctx = null;
		PropertyResourceBundle msgProperties = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		String valPerfil = "";
		Moneda valMoneda = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			msgProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = this.paramsRequest.containsKey("DEBUG") ? true : false;
			
			ctx = new InitialContext();			
			this.ifzCotizaciones = (CotizacionRem) ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzCotiDetalles = (CotizacionDetalleRem) ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzInsumos = (InsumosRem) ctx.lookup("ejb:/Logica_GestionProyectos//InsumosFac!net.giro.adp.logica.InsumosRem");
			this.ifzInsumosDet = (InsumosDetallesRem) ctx.lookup("ejb:/Logica_GestionProyectos//InsumosDetallesFac!net.giro.adp.logica.InsumosDetallesRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzGpoVal = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzObraAlmacen = (ObraAlmacenesRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzReq = (RequisicionRem) ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalle = (RequisicionDetalleRem) ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzOC = (OrdenCompraRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzMonedas = (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzInventario = (AlmacenProductoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCotiDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzInsumos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzInsumosDet.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObraAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReq.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReqDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOC.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzInventario.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.usuarioEmail = this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo();
			this.usuarioEmailClave = this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreoClave();
			if (this.usuarioEmailClave == null || "".equals(this.usuarioEmailClave.trim()))
				this.usuarioEmailClave = "Opdisesa_01";

			/* VALIDACION DE PERFILES */
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.esAdministrativo = ((valPerfil != null && "S".equals(valPerfil.trim())) ? true : false);

			valPerfil = this.entornoProperties.getString("SYS_CONFIG_COMPRADORES");
			if (valPerfil != null && ! "".equals(valPerfil.trim()))
				this.idPuestoCompradores = getPuestosCompradores(valPerfil);
			
			/* Solicitud a Bodega */
			this.procesoSolicitudBodega = false;
			if (this.entornoProperties.getString("solicitud.bodega") != null && ! "".equals(this.entornoProperties.getString("solicitud.bodega").trim()))
				this.procesoSolicitudBodega = ("on".equals(this.entornoProperties.getString("solicitud.bodega").trim().toLowerCase()) ? true : false);

			/* Moneda */
			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			valMoneda = this.ifzMonedas.findByAbreviacion(valPerfil);
			if (valMoneda != null) {
				this.idMonedaSugerida = valMoneda.getId();
				this.idMonedaBase = valMoneda.getId();
				valMoneda = null;
			}

			/* Porcentaje IVA */
			valPerfil = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			this.porcentajeIva = ((valPerfil != null && ! "".equals(valPerfil.trim())) ? Double.valueOf(valPerfil) : 0);

			/* grupo Impuestos */
			this.grupoImpuestos = this.ifzGpoVal.findByName("SYS_IMPTOS");
			if (this.grupoImpuestos == null || this.grupoImpuestos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
			cargarImpuestosIva();
			asignarImpuesto(this.porcentajeIva);
			
			/* grupo Familias de productos */
			this.grupoFamilias = this.ifzGpoVal.findByName("SYS_FAMILIA_PRODUCTO");
			if (this.grupoFamilias == null || this.grupoFamilias.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_FAMILIA_PRODUCTO en con_grupo_valores");
			cargarFamilias();

			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("folio", "Folio"));
			this.tiposBusqueda.add(new SelectItem("nombreObra", "Obra"));
			this.tiposBusqueda.add(new SelectItem("nombreProveedor", "Proveedor"));
			this.tiposBusqueda.add(new SelectItem("nombreComprador", "Comprador"));
			this.tiposBusqueda.add(new SelectItem("idRequisicion", "Requisicion"));
			this.tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.fechaBusqueda = Calendar.getInstance().getTime();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda obras
			this.opcionesBusquedaObras = new ArrayList<SelectItem>();
			this.opcionesBusquedaObras.add(new SelectItem("*", "Coincidencia"));
			this.opcionesBusquedaObras.add(new SelectItem("nombre", (this.esAdministrativo ? "Obra/Egreso Administrativo" : "Obra")));
			this.opcionesBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.opcionesBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObra();
			
			// Busqueda requisiciones
			this.opcionesBusquedaRequisiciones = new ArrayList<SelectItem>();
			this.opcionesBusquedaRequisiciones.add(new SelectItem("*", "Coincidencia"));
			this.opcionesBusquedaRequisiciones.add(new SelectItem("nombreSolicita", "Comprador"));
			this.opcionesBusquedaRequisiciones.add(new SelectItem("nombrePropietario", "Solicita"));
			this.opcionesBusquedaRequisiciones.add(new SelectItem("id", "ID"));
			nuevaBusquedaRequisiciones();

			// Busqueda Proveedor
			this.opcionesBusquedaProveedores = new ArrayList<SelectItem>();
			this.opcionesBusquedaProveedores.add(new SelectItem("nombre", "Nombre"));
			this.opcionesBusquedaProveedores.add(new SelectItem("rfc", "RFC"));
			this.opcionesBusquedaProveedores.add(new SelectItem("id", "ID"));
			nuevaBusquedaProveedores();

			// Estatus Cotizaciones
			this.listEstatusCotizacionItems = new ArrayList<SelectItem>();
			this.listEstatusCotizacionItems.add(new SelectItem(0, msgProperties.getString("estatus.activa")));
			this.listEstatusCotizacionItems.add(new SelectItem(2, msgProperties.getString("estatus.ordenCompra")));
			this.listEstatusCotizacionItems.add(new SelectItem(3, msgProperties.getString("estatus.suministrada")));
			this.listEstatusCotizacionItems.add(new SelectItem(1, msgProperties.getString("estatus.cancelada")));

			// Inicializaciones
			this.listCotizaciones = new ArrayList<Cotizacion>();
			this.listPreCotizacion = new ArrayList<PreCotizacion>();
			this.listPreCotizacionEliminados = new ArrayList<PreCotizacion>();
			this.listSolicitudes = new ArrayList<SolicitudBodega>();
			this.pojoCotizacion = new CotizacionExt();
			this.listFamiliasItems = new ArrayList<SelectItem>();
			this.paginacionProductos = 1;
			this.paginacionSolicitudBodega = 1;
			this.tipoMaestro = TipoMaestro.Inventario;
			controlLog("Solicitud a Bodega: " + (this.procesoSolicitudBodega ? "on" : "off"));
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
		}
	}
	
	public void buscar() {
		boolean incluyeCotizacionesSistema = false;
		int tipoMaestro = 0;
		long idObra = 0L;
		
		try {
    		control();
    		if (this.pojoObraBase != null && this.pojoObraBase.getId() != null && this.pojoObraBase.getId() > 0L) {
    			idObra = this.pojoObraBase.getId();
    			tipoMaestro = (this.pojoObraBase.getTipoObra() == 4 ? 2 : 1);
    			incluyeCotizacionesSistema = (this.pojoObraBase.getTipoObra() == 4);
    		}

    		this.campoBusqueda = (this.campoBusqueda != null && ! "".equals(this.campoBusqueda.trim()) ? this.campoBusqueda.trim() : this.tiposBusqueda.get(0).getValue().toString());
    		this.valorBusqueda = (this.valorBusqueda != null && ! "".equals(this.valorBusqueda.trim()) ? this.valorBusqueda.trim() : "");
    		this.fechaBusqueda = (this.fechaBusqueda != null ? this.fechaBusqueda : Calendar.getInstance().getTime());

			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCotizaciones = this.ifzCotizaciones.findLikeProperty(this.campoBusqueda, ("fecha".equals(this.campoBusqueda) ? this.fechaBusqueda : this.valorBusqueda), idObra, tipoMaestro, true, true, incluyeCotizacionesSistema, "", 0);
			if (this.listCotizaciones == null || this.listCotizaciones.isEmpty()) 
	    		control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar consultar las Cotizaciones", e);
    	} finally {
			this.numPagina = 1;
			this.listCotizaciones = (this.listCotizaciones != null ? this.listCotizaciones : new ArrayList<Cotizacion>());
			controlLog(this.listCotizaciones.size() + " Cotizaciones encontradas. [" + this.campoBusqueda + " - " + this.valorBusqueda + "]");
    	}
	}
	
	public void evaluaNuevaCotizacion() {
		try {
    		control();
    		if (this.pojoObraBase == null || this.pojoObraBase.getId() == null || this.pojoObraBase.getId() <= 0L) {
    			control(-1, "Es necesario indicar una Obra para poder generar una Cotizacion");
    			return;
    		}

			this.tipoMaestro = (this.pojoObraBase.isAdministrativa() ? TipoMaestro.Administrativo : TipoMaestro.Inventario);
			this.permiteModificar = (this.pojoObraBase.getEstatus().longValue() == 0L || this.pojoObraBase.getEstatus().longValue() == 10001211L) ? false : true;
    		if (! this.permiteModificar) {
    			control(-1, "No esta permitido añadir nuevas Cotizaciones a esta Obra.\nLa Obra esta Cancelada o Cerrada");
    			return;
    		}
    		
        	this.origenBaseCotizacon = "?";
    		if (this.pojoObraBase.getTipoObra() == 4)
    			this.origenBaseCotizacon = "A";
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar generar una nueva Cotizacion", e);
    	}
	}
	
	public void nuevaPorExplosionInsumos() {
		try {
    		control();
			this.origenFromRequisicion = false;
			nuevo();
		} catch (Exception e) {
    		control("Ocurrio un problema al intentar generar una nueva Cotizacion", e);
		}
	}
	
	public void nuevaPorRequisicion() {
		try {
    		control();
			this.origenFromRequisicion = true;
			nuevaBusquedaRequisiciones();
		} catch (Exception e) {
    		control("Ocurrio un problema al intentar generar una nueva Cotizacion", e);
		}
	}
	
	public void nuevo() {
		ObraExt obra = null;
		
		try {
    		control();
			this.idCotizacion = 0L;
			this.copiandoCotizacion = false;
			this.editarCotizacion = true;
			this.cotizacionOrdenCompra = "";
			cargarMonedas();
			cargarCompradores();

			if (! this.origenFromRequisicion) {
				this.idComprador = 0L;
				obra = this.ifzObras.convertir(this.pojoObraBase);
				this.idMoneda = this.pojoObraBase.getIdMoneda();
				this.idMoneda = (this.idMoneda > 0L ? this.idMoneda : this.idMonedaSugerida);
				
				this.pojoCotizacion = new CotizacionExt();
				this.pojoCotizacion.setTipo(this.tipoMaestro.ordinal());
				this.pojoCotizacion.setIdObra(obra);
				this.pojoCotizacion.setIdMoneda(this.idMoneda);
			}
			
			this.pojoCotizacion.setPorcentajeIva(this.porcentajeIva);
			this.pojoCotizacion.setTipoCambio(1.0);
			if (this.pojoCotizacion.getIdMoneda() != this.loginManager.getInfoSesion().getEmpresa().getMonedaId())
				this.pojoCotizacion.setTipoCambio(this.loginManager.getTipoCambioActual());
			this.pojoCotizacion.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());

			cargarDetalles((this.origenFromRequisicion ? 2 : 1));
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar generar una nueva Cotizacion", e);
    	} 
	}

	public void editar() {
		List<OrdenCompra> ocAux = null;
		long obraEstatus = 0;
		int obraTipo = 0;
				
		try {
    		control();
			this.pojoCotizacion = this.ifzCotizaciones.findExtById(this.idCotizacion);
			if (this.pojoCotizacion == null || this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar la Cotizacion seleccionada");
				return;
			}

			obraTipo = (this.pojoObraBase != null ? this.pojoObraBase.getTipoObra() : this.pojoCotizacion.getIdObra().getTipoObra());
			obraEstatus = (this.pojoObraBase != null ? this.pojoObraBase.getEstatus() : this.pojoCotizacion.getIdObra().getEstatus());
			this.tipoMaestro = (obraTipo == TiposObra.Administrativa.ordinal() ? TipoMaestro.Administrativo: TipoMaestro.Inventario);
			this.permiteModificar = (obraEstatus == 0 || obraEstatus == 10001211L) ? false : true;
			this.editarCotizacion = this.pojoCotizacion.getEstatus() == EstatusCompras.SUMINISTRADA.ordinal() || this.pojoCotizacion.getEstatus() == EstatusCompras.CANCELADA.ordinal() ? false : this.permiteModificar;
			this.origenFromRequisicion = this.pojoCotizacion.getIdRequisicion() != null && this.pojoCotizacion.getIdRequisicion().getId() != null && this.pojoCotizacion.getIdRequisicion().getId() > 0L;
			
			// Asignamos porcentaje IVA
			stackTrace("Extendiendo la Cotizacion");
			asignarImpuesto(this.pojoCotizacion.getPorcentajeIva());
			
			// Cargamos los compradores
			stackTrace("Cotizacion extendida... cargo compradores");
			cargarCompradores();

			// Cargamos los Monedas
			stackTrace("Cotizacion extendida... cargo Monedas");
			cargarMonedas();
			
			// Recuperamos quien solicita de la requisicion 
			this.idComprador = 0L;
			if (this.pojoCotizacion.getIdComprador() != null)
				this.idComprador = this.pojoCotizacion.getIdComprador().getId();
			
			// Recuperamos quien solicita de la requisicion
			this.idMoneda = 0L;
			if (this.pojoCotizacion.getIdMoneda() > 0L)
				this.idMoneda = this.pojoCotizacion.getIdMoneda();
			this.tipoCambio = this.pojoCotizacion.getTipoCambio();
			this.tipoCambio = (this.tipoCambio > 0 ? this.tipoCambio : 1.0);
			
			// Recuperamos los detalles de la cotizacion
			cargarDetalles(0);

			// Comprobamos las Ordenes de Compra donde se encuentre la Cotizacion
			this.cotizacionOrdenCompra = "";
			this.ifzOC.setInfoSesion(this.loginManager.getInfoSesion());
			ocAux = this.ifzOC.findByCotizacion(this.idCotizacion, false, 0);
			if (ocAux != null && ! ocAux.isEmpty()) {
				for (OrdenCompra orden : ocAux)
					this.cotizacionOrdenCompra = orden.getFolio() + (! "".equals(this.cotizacionOrdenCompra.trim()) ? "," : "");
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar la Cotizacion", e);
    	} 
	}

	public void copiar() {
		try {
			control();
			this.pojoCotizacion = this.ifzCotizaciones.findExtById(this.idCotizacion);
			if (this.pojoCotizacion == null || this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar la Cotizacion seleccionada");
				return;
			}

			if (this.pojoCotizacion.getIdObra().getEstatus() == 0 || this.pojoCotizacion.getIdObra().getEstatus() == 10001211L)  {
    			control(-1, "No esta permitido añadir nuevas Cotizaciones a esta Obra.\nLa Obra esta Cancelada o Cerrada");
				return;
			}

			this.editarCotizacion = true; 
			this.cotizacionOrdenCompra = "";
			this.idComprador = 0L;
			this.idMoneda = 0L;
			this.tipoMaestro = (this.pojoCotizacion.getIdObra().isAdministrativa() ? TipoMaestro.Administrativo: TipoMaestro.Inventario);

			// Asignamos porcentaje IVA
			stackTrace("Extendiendo la Cotizacion");
			asignarImpuesto(this.pojoCotizacion.getPorcentajeIva());
			
			// Cargamos los compradores
			stackTrace("Cotizacion extendida... cargo compradores");
			cargarCompradores();

			// Cargamos los Monedas
			stackTrace("Cotizacion extendida... cargo Monedas");
			cargarMonedas();
			
			// Recuperamos quien solicita de la requisicion 
			if (this.pojoCotizacion.getIdComprador() != null)
				this.idComprador = this.pojoCotizacion.getIdComprador().getId();
			
			// Recuperamos quien solicita de la requisicion
			if (this.pojoCotizacion.getIdMoneda() > 0L)
				this.idMoneda = this.pojoCotizacion.getIdMoneda();
			this.tipoCambio = this.pojoCotizacion.getTipoCambio();
			this.tipoCambio = (this.tipoCambio > 0 ? this.tipoCambio : 1.0);
			
			// Recuperamos los detalles de la cotizacion
			this.copiandoCotizacion = true;
			cargarDetalles(0);

			// Lanzamos el metodo de edicion para recuperar todos los datos
			/*this.copiandoCotizacion = true;
			editar();
			this.copiandoCotizacion = false;
			if (this.operacionCancelada)
				return;

			// Asignamos tipo de cambio actual si corresponde
			this.tipoCambio = 1.0;
			if (this.pojoCotizacion.getIdMoneda() != this.loginManager.getInfoSesion().getEmpresa().getMonedaId())
				this.tipoCambio = this.loginManager.getTipoCambioActual();*/
			
			// Desligamos los detalles de la cotizacion original
			if (this.listPreCotizacion != null && ! this.listPreCotizacion.isEmpty()) {
				for (PreCotizacion var : this.listPreCotizacion) 
					var.setId(null);
			}
			
			stackTrace("Reseteo datos (id, folio, proveedor, contacto) ... ");
			this.idCotizacion = 0L;
			this.pojoCotizacion.setId(null);
			this.pojoCotizacion.setFolio("");
			this.pojoCotizacion.setFecha(Calendar.getInstance().getTime());
			this.pojoCotizacion.setIdProveedor(null);
			this.pojoCotizacion.setNombreProveedor("");
			this.pojoCotizacion.setTipoPersonaProveedor("");
			this.pojoCotizacion.setConsecutivoProveedor(0);
			this.pojoCotizacion.setIdContacto(null);
			this.pojoCotizacion.setNombreContacto("");
			this.pojoCotizacion.setFlete(0);
			this.pojoCotizacion.setTiempoEntrega(0);
			stackTrace("Cotizacion copiada");
		} catch (Exception e) {
    		control("Ocurrio un problema al copiar la Cotizacion indicada", e);
		} finally {
			this.copiandoCotizacion = false;
			totalizarCotizacionesDetalles();
		}
	}
	
	public void guardar() {
		boolean isNew = false;
		double margenTotal = 0;
		Cotizacion cotizacion = null;
		long idCotizacion = 0L;
		// --------------------------------------------------
		String mensaje = "";
		boolean existenciasActualizadas = false;
		
		try {
    		control();
			if (! validaciones()) 
				return;

			// Revisamos los productos para comprobar la existencia
			//comprobarExistencias();
			if (! this.operacionCancelada) 
				existenciasActualizadas = true;
			// Revisamos los productos para comprobar la existencia
			/*mensaje = comprobarCambiosExistencia();
			if (! "".equals(mensaje)) {
				control(-1, mensaje);
				return;
			}*/
			
			// Restauramos los detalles del respaldo
			this.idFamilia = 0L;
			filtrarProductos();
			this.listPreCotizacionFull = new ArrayList<PreCotizacion>();

			// Comprobamos la moneda
			stackTrace("Asignando moneda, tipo maestro, empresa, consecutivo proveedor y totales ... ");
			if (this.idMoneda <= 0L) {
				this.idMoneda = this.loginManager.getInfoSesion().getEmpresa().getMonedaId();
				this.pojoCotizacion.setIdMoneda(this.loginManager.getInfoSesion().getEmpresa().getMonedaId());
				this.pojoCotizacion.setMoneda(this.loginManager.getInfoSesion().getEmpresa().getMoneda());
			}
			if (this.tipoCambio <= 0L) 
				this.tipoCambio = 1.0;
    		
			if (this.origenFromRequisicion && this.pojoCotizacion.getIdRequisicion() == null) {
				stackTrace("Asignando requisicion ... ");
				this.pojoCotizacion.setIdRequisicion(this.ifzReq.findExtById(this.pojoRequisicion.getId()));
				this.pojoCotizacion.setIdMoneda(this.pojoRequisicion.getIdMoneda());
				this.pojoCotizacion.setTipoCambio(this.pojoRequisicion.getTipoCambio());
				this.idMoneda = this.pojoRequisicion.getIdMoneda();
				this.tipoCambio = this.pojoRequisicion.getTipoCambio();
				stackTrace("OK", true);
			} 
			
			this.pojoCotizacion.setIdMoneda(this.idMoneda);
			this.pojoCotizacion.setTipoCambio(this.tipoCambio);
			this.pojoCotizacion.setTipo(this.tipoMaestro.ordinal());
    		this.pojoCotizacion.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			stackTrace("OK", true);
			
			if (this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				// Guardamos en la BD
				isNew = true;
				stackTrace("Guardamos Cotizacion nueva ... ");
				this.pojoCotizacion.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoCotizacion.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
				
    			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoCotizacion.setId(this.ifzCotizaciones.save(this.pojoCotizacion));
				cotizacion = this.ifzCotizaciones.findById(this.pojoCotizacion.getId());
				stackTrace("OK", true);

				// Agregamos la cotizacion al listado
				this.listCotizaciones.add(0, cotizacion);
			} else {
				// Actualizamos Cotizacion
				isNew = false;
				stackTrace("Actualizamos Cotizacion ... ");
				this.pojoCotizacion.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
				
    			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzCotizaciones.update(this.pojoCotizacion);
				cotizacion = this.ifzCotizaciones.findById(this.pojoCotizacion.getId());
				stackTrace("OK", true);
				
				// Buscamos la cotizacion en listado
				for (Cotizacion var : this.listCotizaciones) {
					if (var.getId().longValue() == this.pojoCotizacion.getId()) {
						var = cotizacion;
						break;
					}
				}
			}

			// Guardamos los detalles de la cotizacion
			if (this.listDetalles != null && ! this.listDetalles.isEmpty()) {
				// Asigno la cotizacion al detalle y recupero el margen para suma global de la cotizacion
				for (CotizacionDetalleExt item : this.listDetalles) {
					item.setIdCotizacion(this.pojoCotizacion);
					margenTotal += item.getMargen();
				}
				
    			this.ifzCotiDetalles.setInfoSesion(this.loginManager.getInfoSesion());
				this.listDetalles = this.ifzCotiDetalles.saveOrUpdateListExt(this.listDetalles);
				this.listDetalles = this.listDetalles != null ? this.listDetalles : new ArrayList<CotizacionDetalleExt>();
				this.listPreCotizacion = new ArrayList<PreCotizacion>();
				for (CotizacionDetalleExt var : this.listDetalles) 
					this.listPreCotizacion.add(new PreCotizacion(var));
				stackTrace("OK", true);
			}

			// Borramos los detalles de la cotizacion si corresponde
			if (this.listPreCotizacionEliminados != null && ! this.listPreCotizacionEliminados.isEmpty()) {
				stackTrace("Borramos detalles de cotizacion ... ");
				for (PreCotizacion var : this.listPreCotizacionEliminados) {
					if (var.getId() == null || var.getId() <= 0L) 
						continue;
					this.ifzCotiDetalles.delete(var.getId());
				}
				
				// Limpiamos la lista de eliminados
				// TO DO: BACK OFFICE Eliminados
				this.listPreCotizacionEliminados.clear();
				stackTrace("OK", true);
			}

			// Actualizamos Folio y Margen en la BD
			stackTrace("Actualizamos el Margen en la BD ... ");
			idCotizacion = cotizacion.getId();
			this.idCotizacion = cotizacion.getId();
			this.pojoCotizacion.setFolio(cotizacion.getFolio());
			this.pojoCotizacion.setConsecutivoProveedor(cotizacion.getConsecutivoProveedor());
			this.pojoCotizacion.setMargen(margenTotal);
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCotizaciones.update(this.pojoCotizacion);
			stackTrace("OK", true);
			
			if (isNew) {
				mensaje = lanzarSBO();
				if (existenciasActualizadas)
					mensaje = "\nSe detecto un cambio en existencias de Almacenes.\nLa Cotizacion y/o Solicitud(es) a Bodega fueron actualizados\n\n" + mensaje;
			}

			// Actualiza precio en Ordenes de Compras
			stackTrace("Actualiza precio en Ordenes de Compras ... ");
			this.ifzCotizaciones.backOfficeCotizacionPreciosOrdenCompra(idCotizacion);
			stackTrace("OK", true);
			controlLog(printStackTrace("\n"));
			editar();
			
			mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "OK");
			control(13, mensaje);
		} catch (Exception e) {
			control("Ocurrio un problema al guardar la Cotizacion", e);
		}
	}
	
	public void cancelar() {
		Respuesta respuesta = null;
		Cotizacion cotizacion = null;
		
		try {
			control();
			if (this.idCotizacion <= 0L) {
				control(-1, "Debe indicar una Cotizacion para Cancelar");
				return;
			}
			
			// Establecemos los atributos para la cancelacion
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzCotizaciones.cancelar(this.idCotizacion, this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			if (respuesta != null && respuesta.getErrores().getCodigoError() != 0L) {
				log.warn(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError());
				return;
			}
			
			cotizacion = (Cotizacion) respuesta.getBody().getValor("cotizacion");
			for (Cotizacion var : this.listCotizaciones) {
				if (var.getId().longValue() == this.pojoCotizacion.getId()) {
					var = cotizacion;
					break;
				}
			}

			if (this.pojoCotizacion != null && this.idCotizacion == this.pojoCotizacion.getId().longValue()) {
				this.pojoCotizacion.setEstatus(cotizacion.getEstatus());
				this.pojoCotizacion.setModificadoPor(cotizacion.getModificadoPor());
				this.pojoCotizacion.setFechaModificacion(cotizacion.getFechaModificacion());
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar cancelar la Cotizacion", e);
    	}
	}

	public void reporte() {
		Respuesta respuesta = null;
		Cotizacion cotizacion = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoReporte = null;
		String formatoDocumento = "";
		String nombreReporte = "";
		
		try {
    		control();
    		cotizacion = this.ifzCotizaciones.findById(this.idCotizacion);
			if (cotizacion == null || cotizacion.getId() == null || cotizacion.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar la Cotizacion seleccionada.\nCotizacion no encontrada");
				return;
			}
			
			stackTrace("REPORTE COTIZACION: Preparamos parametros");
			if (cotizacion.getFolio() != null && ! "".equals(cotizacion.getFolio()))
				nombreReporte = "-" + cotizacion.getFolio();
			else if (cotizacion.getId() != null && cotizacion.getId() > 0L)
				nombreReporte = "-" + cotizacion.getId();
			nombreReporte = "COT" + nombreReporte;
			
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idCotizacion", cotizacion.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  "167");
			params.put("nombreDocumento", nombreReporte);
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());

			stackTrace("REPORTE COTIZACION: Ejecutamos reporte");
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				stackTrace("Error Interno - No se pudo ejecutar el reporte :: " + respuesta.getErrores().getDescError());
				controlLog(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
	    		control(-1, "Ocurrio un problema al intentar exportar la Cotizacion.\n501 - No se pudo ejecutar la peticion");
	    		return;
			}
			
			// Recogemos reporte
			stackTrace("REPORTE COTIZACION: Recuperamos contenido de reporte");
			contenidoReporte = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreReporte = nombreReporte + "." + formatoDocumento;
			if (contenidoReporte == null || contenidoReporte.length <= 0) {
	    		control(-1, "Ocurrio un problema al intentar exportar la Cotizacion.\n404 - No se encontro la Cotizacion " + cotizacion.getFolio());
				return;
			}

			stackTrace("REPORTE COTIZACION: Lo guardo en SESSION");
			this.httpSession.setAttribute("contenido", contenidoReporte);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreReporte);
			stackTrace("REPORTE COTIZACION: Terminado");
    	} catch (Exception e) {
    		control("Ocurrio un problema al exportar la Cotizacion\n500 - Error interno en el servidor", e);
    	}
	}

	public void comprobarExistencias() {
		String cambios = "";
		
		control();
		cambios = comprobarCambiosExistencia();
		if (! "".equals(cambios.trim())) 
			control(-1, cambios);
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
	
	public void totalizarCotizacionesDetalles() {
		double valorIva = 0;
		double subtotal = 0;
		double iva = 0;
		double total = 0;
		
		try {
			valorIva = (this.porcentajeIva / 100);
			this.seleccionarTodos = true;
			this.listPreCotizacion = (this.listPreCotizacion != null ? this.listPreCotizacion : new ArrayList<PreCotizacion>());
			for (PreCotizacion var : this.listPreCotizacion) {
				if (! var.isSeleccionado()) {
					this.seleccionarTodos = false;
					continue;
				}
				
				subtotal += var.getImporte();
				iva += (var.getImporte() * valorIva);
			}

			// Calculamos total de cotizacion
			total = subtotal + iva;
		} catch (Exception e) {
			control("Ocurrio un problema al calcular el Total de la Cotizacion", e);
		} finally {
			this.pojoCotizacion = (this.pojoCotizacion != null ? this.pojoCotizacion : new CotizacionExt());
			this.pojoCotizacion.setPorcentajeIva(this.porcentajeIva);
			this.pojoCotizacion.setSubtotal(subtotal);
			this.pojoCotizacion.setIva(iva);
			this.pojoCotizacion.setTotal(total);
		}
	}

	public void filtrarProductos() {
		// Inicializamos la lista respaldo si corresponde
		this.listPreCotizacionFull = (this.listPreCotizacionFull != null ? this.listPreCotizacionFull : new ArrayList<PreCotizacion>());
		if (this.listPreCotizacionFull.isEmpty()) { // Si el respaldo no tiene datos, lo llenamos
			for (PreCotizacion var : this.listPreCotizacion) 
				this.listPreCotizacionFull.add(var);
		} else {
			// Actualizamos los datos del respaldo por si hubo algun cambio en las propiedades
			for (PreCotizacion var1 : this.listPreCotizacion) {
				for (PreCotizacion var2 : this.listPreCotizacionFull) {
					if (var1.getIdProducto().getId() == var2.getIdProducto().getId()) {
						var2.setRequeridos(var1.getRequeridos());
						var2.setImporte(var1.getImporte());
						var2.setSeleccionado(var1.isSeleccionado());
						break;
					}
				}
			}
		}
		
		this.paginacionProductos = 1;
		this.listPreCotizacion = new ArrayList<PreCotizacion>();
		for (PreCotizacion var : this.listPreCotizacionFull) {
			if (var.getProductoFamiliaId() == this.idFamilia || this.idFamilia == 0L)
				this.listPreCotizacion.add(var);
		}
	}

	public void comprobarSeleccionarTodos() {
		this.seleccionarTodos = true;
		for (PreCotizacion detalle : this.listPreCotizacion) {
			if (! detalle.isSeleccionado()) {
				this.seleccionarTodos = false;
				break;
			}
		}
	}
	
	public boolean validaciones() {
		CotizacionDetalleExt cotDetalle = null;
		double margen = 0;
		//----------------------------------------
		boolean valido = false;
		
		valido = false;
		for (PreCotizacion detalle : this.listPreCotizacion) {
			if (detalle.isSeleccionado()) {
				valido = true;
				break;
			}
		}
		
		stackTrace("Validando Cotizacion ... ");
		if (this.pojoCotizacion.getFlete() < 0) {
    		control(7, "El Flete debe ser mayor a cero");
			return false;
		}
		
		if (this.pojoCotizacion.getProveedor().equals("")) {
			control(12, "Falta definir Proveedor");
			return false;
		}
		
		if (this.idComprador <= 0L) {
			control(-1, "Debe seleccionar un Comprador");
			return false;
		}
		
		if (this.pojoCotizacion.getTiempoEntrega() <= 0) {
    		control(6, "El tiempo de entrega debe ser mayor a cero");
			return false;
		}
		
		if (this.listPreCotizacion == null || this.listPreCotizacion.isEmpty()) { 
			control(4, "Lista de productos vacia");
			return false;
		}
		
		if (! valido) {
			control(5, "Ningun producto seleccionado");
			return false;
		}

		stackTrace("Asignamos comprador ... ");
		for (EmpleadoExt var : this.listCompradores) {
			if (this.idComprador == var.getId()) {
				this.pojoCotizacion.setIdComprador(var);
				stackTrace("OK", true);
				break;
			}
		}
		
		if (this.pojoCotizacion.getIdComprador() == null || this.pojoCotizacion.getIdComprador().getId() == null || this.pojoCotizacion.getIdComprador().getId() <= 0L) {
			control(-1, "Debe seleccionar un Comprador");
			return false;
		}
		
		this.listDetalles = new ArrayList<CotizacionDetalleExt>();
		for (PreCotizacion var : this.listPreCotizacion) {
			if (! var.isSeleccionado())
				continue; 
			if (var.getCantidad() <= 0) 
				continue; 
			
			// Generamos el detalle de cotizacion
			cotDetalle = var.getCotizacion();
			cotDetalle.setIdCotizacion(this.pojoCotizacion);
			// Calculamos el margen
			margen = (cotDetalle.getProductoPrecioUnitario() * cotDetalle.getCantidad()) - (cotDetalle.getPrecioUnitario() * cotDetalle.getCantidad());
			if (margen <= 0)
				margen = 0;
			cotDetalle.setMargen(margen);
			
			// Guardamos el detalle
			if (cotDetalle.getId() == null || cotDetalle.getId() <= 0L) {
				cotDetalle.setFechaCreacion(Calendar.getInstance().getTime());
				cotDetalle.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			} 

			cotDetalle.setFechaModificacion(Calendar.getInstance().getTime());
			cotDetalle.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			// Añadimos a listado
			this.listDetalles.add(cotDetalle);
		}
		stackTrace("OK", true);
		
		return true;
	}
	
	private List<Long> getPuestosCompradores(String value) {
		List<Long> puestos = new ArrayList<Long>();
		String[] splitted = null;
		
		if (value == null || "".equals(value.trim()))
			return puestos;
		
		if (! value.contains(",")) {
			puestos.add(Long.valueOf(value));
			return puestos;
		}
		
		value = value.replace(" ", "");
		splitted = value.trim().split(",");
		for (String val : splitted)
			puestos.add(Long.valueOf(val));
		return puestos;
	}

	private void filtrarFamilias() {
		if (this.listFamilias == null || this.listFamilias.isEmpty()) 
			cargarFamilias();
		
		this.listFamiliasItems = new ArrayList<SelectItem>();
		for (ConValores familia : this.listFamilias) {
			if (familia.getId() <= 0L || familia.getValor() == null || "".equals(familia.getValor().trim()))
				continue;
			if (familia.getDescripcion() == null || "".equals(familia.getDescripcion().trim()))
				familia.setDescripcion(familia.getValor());
			this.listFamiliasItems.add(new SelectItem(familia.getId(), familia.getDescripcion() + (! familia.getValor().equals(familia.getDescripcion()) ? " (" + familia.getValor() + ")" : "")));
		} 
		
		if (this.listFamiliasItems.isEmpty())
			this.listFamiliasItems.add(new SelectItem(0L, "Sin Familias disponibles"));
	}

	private void cargarFamilias() {
		try {
			controlLog("Cargamos lista de familias");
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listFamilias = this.ifzConValores.findAll(this.grupoFamilias, "descripcion", 0); 
			this.listFamilias = (this.listFamilias != null ? this.listFamilias : new ArrayList<ConValores>());
		} catch (Exception e) {
			log.error("Ocurrio un problema al cargar los Familias", e);
		} 
	}
	
	private void cargarCompradores() {
		try {
			// Cargamos la lista de compradores
			this.listCompradoresItems = new ArrayList<SelectItem>();
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCompradores = this.ifzEmpleados.findAllPuestoExt(this.idPuestoCompradores);
			if (this.listCompradores == null || this.listCompradores.isEmpty()) 
				return;
			
				// Generamos la lista auxiliar de compradores
			for (EmpleadoExt var : this.listCompradores)
				this.listCompradoresItems.add(new SelectItem(var.getId(), var.getNombre()));
		} catch (Exception e) {
			log.error("Ocurrio un problema al cargar los Compradores", e);
		} finally {
			if (this.listCompradoresItems.isEmpty())
				this.listCompradoresItems.add(new SelectItem(0L, "Sin Impuestos disponibles"));
		}
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
			log.error("Ocurrio un problema al cargar las Monedas", e);
		} finally {
			if (this.listMonedasItems.isEmpty())
				this.listMonedasItems.add(new SelectItem(0L, "Sin Monedas disponibles"));
		}
	}
	
	private void cargarImpuestosIva() {
		try {
			// Cargamos la lista de familias
			this.listImpuestosItems = new ArrayList<SelectItem>();
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listImpuestos = this.ifzConValores.buscaValorGrupo("descripcion", "", this.grupoImpuestos);
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
			log.error("Ocurrio un problema al cargar los Impuestos IVA", e);
		} finally {
			if (this.listImpuestosItems.isEmpty())
				this.listImpuestosItems.add(new SelectItem(0L, "Sin Impuestos disponibles"));
		}
	}
	
	private boolean verificaAlmacenesBodegaObra(boolean indicaError) {
		List<ObraAlmacenes> almacenesObra = null;
		boolean almacenPrincipal = false;
		int almacenes = 0;
		int bodegas = 0;
		
		try {
    		control();
			this.ifzObraAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			almacenesObra = this.ifzObraAlmacen.findAll(this.pojoCotizacion.getIdObra().getId()); 
			if (almacenesObra == null || almacenesObra.isEmpty()) 
				return false;
			
			for (ObraAlmacenes oAlmacen : almacenesObra) {
				almacenPrincipal = ((oAlmacen.getTipo() == 1 || oAlmacen.getTipo() == 3) && oAlmacen.getAlmacenPrincipal() == 1 ? true : almacenPrincipal);
				almacenes += ((oAlmacen.getTipo() == 1 || oAlmacen.getTipo() == 3) ? 1 : 0);
				bodegas += (oAlmacen.getTipo() == 2 || oAlmacen.getTipo() == 4 ? 1 : 0);
			}
			
			if (indicaError) {
				if (almacenes <= 0)
					control(10, "La Obra no tiene asignado ningun Almacen");
				else if (! almacenPrincipal) 
					control(11, "La Obra no tiene asignado ningun Almacen principal");
				else if (bodegas <= 0)
					control(101, "La Obra no tiene asignada ninguna Bodega");
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al verificar la asignacion de Almacenes/Bodega de la Obra", e);
    		return false;
    	}
		
		return (almacenes > 0 && bodegas > 0);
	}

	private boolean comprobarPrimeraCotizacion() {
		boolean generarSBO = false;
		
		try {
			if (this.pojoCotizacion.getIdObra().getId() == null || this.pojoCotizacion.getIdObra().getId() <= 0L)
				return false;
			if (this.procesoSolicitudBodega && validaRequest("SBO"))
				return true;
			
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			generarSBO = this.ifzCotizaciones.comprobarSolicitudBodega(this.pojoCotizacion.getIdObra().getId());
			generarSBO = ! generarSBO; // Invertimos el valor obtenido para indicar si lanzamos o no la SBO
			generarSBO = (validaRequest("SBO") ? true : generarSBO);
		} catch (Exception e) {
			control("Ocurrio un problema al realizar comprobaciones para el proceso de Solicitud a Bodega", e);
			generarSBO = false;
		}
		
		return generarSBO;
	}

	private boolean validaRequest(String param) {
		return validaRequest(param, null);
	}
	
	private boolean validaRequest(String param, String refValue) {
		if (! this.isDebug)
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		if (refValue == null || "".equals(refValue.trim()))
			return true;
		
		return (refValue.trim().equals(this.paramsRequest.get(param).trim()));
	}
	
	private void control() { 
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.stackTrace = new ArrayList<String>();
	}
	
	private void control(String value, Throwable throwable) { 
		control(true, -1, value, throwable); 
	}
	
	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "Ocurrio un problema no controlado al realizar la operacion");
		mensaje = (! "OK".equals(mensaje) ? mensaje : "");
		stackTrace(mensaje);
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + this.mensaje + "\n" + printStackTrace("\n") + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje);
	}
	
	private void stackTrace(String mensaje) {
		stackTrace(mensaje, false);
	}

	private void stackTrace(String mensaje, boolean append) {
		if (mensaje == null || "".equals(mensaje.trim()))
			return;
		
		this.stackTrace = (this.stackTrace != null ? this.stackTrace : new ArrayList<String>());
		if (! append || (append && this.stackTrace.isEmpty())) {
			this.stackTrace.add(mensaje);
			return;
		}
		
		mensaje = this.stackTrace.get(this.stackTrace.size() - 1) + mensaje;
		this.stackTrace.set((this.stackTrace.size() - 1), mensaje);
	}
	
	private String printStackTrace(String separador) {
		separador = (separador != null && ! "".equals(separador.trim()) ? separador.trim() : ",");
		this.stackTrace = (this.stackTrace != null ? this.stackTrace : new ArrayList<String>());
		return "\nSTACKTRACE:\n" + StringUtils.join(this.stackTrace, separador) + "\n";
	}

	// --------------------------------------------------------------------------------------	
	// Detalle de Cotizacion
	// --------------------------------------------------------------------------------------

	private void cargarDetalles(int origen) {
		List<PreCotizacion> detalles = null;
		boolean generarSBO = false;
		boolean hasId = false;
		
		try {
			this.listSolicitudes = new ArrayList<SolicitudBodega>();
			this.listPreCotizacion = new ArrayList<PreCotizacion>();
			this.listPreCotizacionEliminados = new ArrayList<PreCotizacion>();
			
			if (origen == 0) { // desde cotizacion
				detalles = this.detallesFromCotizacion();
				this.seleccionarTodos = true;
				generarSBO = false;
				if (this.copiandoCotizacion) {
					this.listSolicitudes = new ArrayList<SolicitudBodega>();
					generarSBO = true;
				}
			} else if (origen == 1) { // desde explosion de insumos
				detalles = this.detallesFromExplosion();
				this.seleccionarTodos = false;
				generarSBO = comprobarPrimeraCotizacion();
			} else if (origen == 2) { // desde requisicion
				detalles = this.detallesFromRequisicion();
				this.seleccionarTodos = true;
				generarSBO = true;
			} 

			if (detalles == null || detalles.isEmpty()) {
				this.seleccionarTodos = false;
				generarSBO = false;
				if ("".equals(this.mensaje.trim()))
					control(-1, "Ocurrio un problema al recuperar los productos para la Cotizacion");
				else
					control(-1, "No se pudo generar la Cotizacion.\n" + this.mensaje);
				return;
			}
			
			if (this.listFamilias == null || this.listFamilias.isEmpty())
				cargarFamilias();
			filtrarFamilias();
			
			Collections.sort(detalles, new Comparator<PreCotizacion>() {
				@Override
				public int compare(PreCotizacion o1, PreCotizacion o2) {
					return o1.getIdProducto().getClave().compareTo(o2.getIdProducto().getClave());
				}
			});
			this.listPreCotizacionOrigen = new ArrayList<PreCotizacion>();
			this.listPreCotizacionOrigen.addAll(detalles);
			
			hasId = (this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L);
			hasId = (hasId && this.copiandoCotizacion ? false : hasId);
			generarSBO = (this.procesoSolicitudBodega ? generarSBO : false);
			generarSBO = (validaRequest("SBO") ? true : generarSBO);
			if (generarSBO && ! hasId) {
				//generarSBO = (this.pojoCotizacion.getIdObra().getTipoObra() == 4 ? false : generarSBO); // ELIMINO Restriccion de Obras Administrativas
				generarSBO = (origen <= 0 ? false : generarSBO);
				if (generarSBO && ! verificaAlmacenesBodegaObra(false)) { //&& ! verificaAlmacenesBodegaObra(true)) {
					control(10, "No se puede generar Solicitud a Bodega.\nLa obra no tiene asignados Almacenes/Bodega");
					generarSBO = false;
				}
				
				if (generarSBO) 
					detalles = this.existenciasAlmacenes(this.pojoCotizacion.getIdObra().getId(), detalles);
			}
			
			if (! detalles.isEmpty())
				this.listPreCotizacion.addAll(detalles);
			if (this.listPreCotizacion.isEmpty() && this.solicitudBodega) {
				control(100, "Todo el material esta cubierto en Solicitud(es) a Bodega");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los productos para la Cotizacion", e);
		} finally {
			totalizarCotizacionesDetalles();
			this.paginacionProductos = 1;
			this.listPreCotizacion = (this.listPreCotizacion != null ? this.listPreCotizacion : new ArrayList<PreCotizacion>());
			this.paginacionSolicitudBodega = 1;
			this.listSolicitudes = (this.listSolicitudes != null ? this.listSolicitudes : new ArrayList<SolicitudBodega>());
			this.solicitudBodega = (this.listSolicitudes.size() > 0);
		}
	}

	private List<PreCotizacion> detallesFromCotizacion() {
		List<CotizacionDetalleExt> lista = null;
		List<PreCotizacion> detalles = null;
		List<Long> familiasId = null;
		
		try {
			// Recuperamos los productos de la cotizacion
			lista = this.ifzCotiDetalles.findExtAll(this.pojoCotizacion.getId()); 
			if (lista == null || lista.isEmpty()) {
				control(-1, "Ocurrio un problema al recuperar los detalles de la Cotizacion");
				return detalles;
			}

			Collections.sort(lista, new Comparator<CotizacionDetalleExt>() {
				@Override
				public int compare(CotizacionDetalleExt o1, CotizacionDetalleExt o2) {
					return o1.getProductoClave().compareTo(o2.getProductoClave());
				}
			});
			
			// Generamos los items para la pantalla
			detalles = new ArrayList<PreCotizacion>();
			this.listFamilias = new ArrayList<ConValores>();
			familiasId = new ArrayList<Long>();
			for (CotizacionDetalleExt var : lista) {
				detalles.add(new PreCotizacion(var));
				if (! familiasId.contains(var.getIdProducto().getFamilia().getId())) {
					familiasId.add(var.getIdProducto().getFamilia().getId());
					this.listFamilias.add(var.getIdProducto().getFamilia());
				}
			}
			
			// Recuperamos Solicitud a Bodega, previamente generada si corresponde
			this.listSolicitudes = this.ifzCotizaciones.solicitudBodega(this.pojoCotizacion.getId());
			this.listSolicitudes = (this.listSolicitudes != null ? this.listSolicitudes : new ArrayList<SolicitudBodega>());
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los productos de la Cotizacion", e);
			detalles = null;
		} 
		
		return detalles;
	}
	
	private List<PreCotizacion> detallesFromExplosion() {
		List<PreCotizacion> detalles = null;
		PreCotizacion detalle = null;
		// -----------------------------------------------
		InsumosExt explosionInsumos = null;
		List<InsumosDetallesExt> insumosDetalles = null;
		List<InsumosDetallesExt> insumosValidos = null;
		List<Long> familiasId = null;
		
		try {
			control();
			// Comprobamos los insumos de la obra
			stackTrace("Comprobamos Explosion de Insumos");
			this.ifzInsumos.setInfoSesion(this.loginManager.getInfoSesion());
			explosionInsumos = this.ifzInsumos.findByIdExt(this.idExplosionInsumos);
			if (explosionInsumos == null || explosionInsumos.getId() == null || explosionInsumos.getId() <= 0L) {
				control(-1, "La Obra no tiene Explosion de Insumos");
				return detalles;
			}
			
			// Recuperamos el detalle del insumo
			stackTrace("Recuperamos detalles de Explosion de Insumos");
			this.ifzInsumosDet.setInfoSesion(this.loginManager.getInfoSesion());
			insumosDetalles = this.ifzInsumosDet.findAllExt(explosionInsumos.getId()); 
			if (insumosDetalles == null || insumosDetalles.isEmpty()) {
				control(-1, "La Explosion de Insumos no tiene productos");
				return detalles;
			}

			Collections.sort(insumosDetalles, new Comparator<InsumosDetallesExt>() {
				@Override
				public int compare(InsumosDetallesExt o1, InsumosDetallesExt o2) {
					return o1.getProductoClave().compareTo(o2.getProductoClave());
				}
			});
			
			// Comprobamos el porcentaje de suministro de la Explosion de Insumos
			stackTrace("Comprobamos el porcentaje de suministro de la Explosion de Insumos");
			insumosValidos = new ArrayList<InsumosDetallesExt>();
			this.listFamilias = new ArrayList<ConValores>();
			familiasId = new ArrayList<Long>();
			for (InsumosDetallesExt insumo : insumosDetalles) {
				if (insumo.getTipo() > TipoInsumo.Material.ordinal()) 
					continue;
				if (this.idMoneda <= 0L)
					this.idMoneda = insumo.getIdProducto().getIdMoneda().getId();
				if (insumo.getPendiente() > 0) {
					insumosValidos.add(insumo);
					if (! familiasId.contains(insumo.getIdProducto().getFamilia().getId())) {
						familiasId.add(insumo.getIdProducto().getFamilia().getId());
						this.listFamilias.add(insumo.getIdProducto().getFamilia());
					}
				}
			}
			insumosDetalles.clear();
			insumosDetalles.addAll(insumosValidos);
			insumosValidos = null;
			if (insumosDetalles == null || insumosDetalles.isEmpty()) {
				control(-1, "La Explosion de Insumos ya ha sido completamente suministrada");
				return detalles;
			}
			
			detalles = new ArrayList<PreCotizacion>();
			for (InsumosDetallesExt insumo : insumosDetalles) {
				stackTrace("Preparo detalle ... #" + (insumosDetalles.indexOf(insumo) + 1));
				detalle = new PreCotizacion(insumo);
				detalles.add(detalle);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los detalles de la Explosion de Insumos cargados a la Obra", e);
			detalles = null;
		}
		
		return detalles;
	}
	
	private List<PreCotizacion> detallesFromRequisicion() {
		List<PreCotizacion> detalles = null;
		PreCotizacion detalle = null;
		List<RequisicionDetalleExt> requisicionDetalles = null;
		List<RequisicionDetalleExt> auxiliar = null;
		List<Long> familiasId = null;
		double precioUnitario = 0;
		double tipoCambio = 0;
		
		try {
			control();
			// Comprobamos requisicion
			if (this.pojoCotizacion == null || this.pojoCotizacion.getIdRequisicion() == null || this.pojoCotizacion.getIdRequisicion().getId() == null || this.pojoCotizacion.getIdRequisicion().getId() <= 0L) {
				control(-1, "No selecciono ninguna Requisicion");
				return null;
			}
			
			// Recuperamos el detalle de la requisicion
			stackTrace("Recuperamos detalles de Requisicion");
			this.ifzReqDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			requisicionDetalles = this.ifzReqDetalle.findExtAll(this.pojoCotizacion.getIdRequisicion().getId()); 
			if (requisicionDetalles == null || requisicionDetalles.isEmpty()) {
				control(-1, "La Requisicion no tiene productos asignados");
				return null;
			}

			Collections.sort(requisicionDetalles, new Comparator<RequisicionDetalleExt>() {
				@Override
				public int compare(RequisicionDetalleExt o1, RequisicionDetalleExt o2) {
					return o1.getProductoClave().compareTo(o2.getProductoClave());
				}
			});
			
			auxiliar = new ArrayList<RequisicionDetalleExt>();
			for (RequisicionDetalleExt item : requisicionDetalles) {
				if (item.getSuministroPendiente() <= 0)
					continue;
				auxiliar.add(item);
			}
			
			requisicionDetalles.clear();
			requisicionDetalles.addAll(auxiliar);
			if (requisicionDetalles == null || requisicionDetalles.isEmpty()) {
				control(-1, "La Requisicion no tiene productos disponibles");
				return null;
			}
			
			detalles = new ArrayList<PreCotizacion>();
			this.listFamilias = new ArrayList<ConValores>();
			familiasId = new ArrayList<Long>();
			for (RequisicionDetalleExt item : requisicionDetalles) {
				stackTrace("Preparo detalle ... #" + (requisicionDetalles.indexOf(item) + 1));
				detalle = new PreCotizacion(item);

				stackTrace("conversion de moneda");
				if (this.idMonedaBase == this.pojoCotizacion.getIdMoneda() && this.idMonedaBase != item.getIdProducto().getIdMoneda().getId()) {
					precioUnitario = item.getIdProducto().getPrecioCompra();
					tipoCambio = this.loginManager.getTipoCambioActual();
					precioUnitario = precioUnitario * tipoCambio;
					detalle.setPrecioUnitario(precioUnitario);
				} else if (this.idMonedaBase != this.pojoCotizacion.getIdMoneda() && this.idMonedaBase == item.getIdProducto().getIdMoneda().getId()) {
					precioUnitario = item.getIdProducto().getPrecioCompra();
					tipoCambio = this.loginManager.getTipoCambioActual();
					precioUnitario = precioUnitario / tipoCambio;
					detalle.setPrecioUnitario(precioUnitario);
				} else {
					precioUnitario = item.getIdProducto().getPrecioCompra();
					tipoCambio = 1.0;
					detalle.setPrecioUnitario(precioUnitario);
				}
				
				if (! familiasId.contains(item.getIdProducto().getFamilia().getId())) {
					familiasId.add(item.getIdProducto().getFamilia().getId());
					this.listFamilias.add(item.getIdProducto().getFamilia());
				}
				
				// Añado a cotizacion
				detalles.add(detalle);
			}
		} catch (Exception e) {
    		control("Ocurrio un problema al recuperar los detalles de la Requisicion seleccionada", e);
    		detalles = null;
    	}
		
		return detalles;
	}
	
	// --------------------------------------------------------------------------------------	
	// Envio de Cotizacion
	// --------------------------------------------------------------------------------------

	public void nuevoEnvio() {
		try {
			control();
			this.email = "";
			this.emailAsunto = "Cotizacion de Material";
			this.emailCuerpo = "Favor de cotizar el siguiente material.\n\n\nAnexo listado.";
			
			if (this.pojoCotizacion == null || this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				control(-1, "Debe guardar la Cotizacion antes de enviarla por correo electronico.\nGuardela e intente nuevamente.");
				return;
			}
			
			if (this.pojoCotizacion != null) {
				if (this.pojoCotizacion.getIdContacto() != null && this.pojoCotizacion.getIdContacto().getCorreo() != null)
					this.email = this.pojoCotizacion.getIdContacto().getCorreo();
				this.email = (this.email != null && ! "".equals(this.email)) ? this.email : "";
			}
		} catch (Exception e) {
    		control("Ocurrio un problema al preparar el envio de la Cotizacion", e);
		}
	}
	
	public void enviarCotizacion() {
		Respuesta respuesta = null;
		HashMap<String, String> params = null;
		HashMap<String, Object> paramsReporte = null;
		byte[] contenidoReporte = null;
		String nombreReporte = "";
		// ------------------------------------------
		String cco = "";
		HashMap<String, String> correo = null;
		HashMap<String, Object> adjuntos = null;
		
		try {
    		control();
			if (this.pojoCotizacion == null || this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				control(-1, "Debe guardar previamente la Cotizacion para poder enviarla por correo electronico");
				return;
			}
			
			if (this.pojoCotizacion.getFolio() != null && ! "".equals(this.pojoCotizacion.getFolio()))
				nombreReporte = "." + this.pojoCotizacion.getFolio();
			else if (this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L)
				nombreReporte = "." + this.pojoCotizacion.getId();
			nombreReporte = "cotizacion" + nombreReporte + ".xls";
			
			if ("".equals(this.email)) {
	    		control(8, "Debe indicar un Email");
				return;
			}
			
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idCotizacion", this.pojoCotizacion.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  "167");
			params.put("nombreDocumento", nombreReporte);
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());
			
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				stackTrace("Error Interno - No se pudo ejecutar el reporte :: " + respuesta.getErrores().getDescError());
	    		control(-1, "Ocurrio un problema al intentar enviar la Cotizacion por correo electronico.\n501 - No se pudo ejecutar la peticion");
				return;
			}
			
			// Recogemos reporte
			contenidoReporte = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			if (contenidoReporte == null) {
	    		control(-1, "Ocurrio un problema al intentar enviar la Cotizacion por correo electronico.\n404 - No se encontro la Cotizacion " + this.pojoCotizacion.getFolio());
				return;
			}
			
			cco = this.email;
			this.email = getDestinatarioTO(cco);
			cco = getDestinatarioBCC(cco);
			cco = cco.replace(this.email + ",", "");
			
			// Parametros para envio de correo
			correo = new HashMap<String, String>();
			correo.put("from", this.usuarioEmail); 
			correo.put("fromPass", this.usuarioEmailClave);
			correo.put("to", this.email);
			correo.put("cco", cco);
			correo.put("subject", this.emailAsunto);
			correo.put("body", this.emailCuerpo);
			
			// Adjuntos
			adjuntos = new HashMap<String, Object>();
			adjuntos.put(nombreReporte, contenidoReporte);
			
			respuesta = this.ifzReportes.enviarCorreo(correo, adjuntos);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				stackTrace("Error Interno - No se pudo enviar el reporte por correo electronico :: " + respuesta.getErrores().getDescError());
	    		control(-1, "Ocurrio un problema al intentar enviar la Cotizacion por correo electronico.\n503 - No se pudo procesar el envio");
				return;
			}

			stackTrace("Error 9 - Correo enviado desde " + this.usuarioEmail + " a " + this.email);
    		control(false, 9, "Correo enviado desde " + this.usuarioEmail, null);
    	} catch (Exception e) {
    		control("Ocurrio un problema al enviar la Cotizacion por correo electronico\n500 - Error interno en el servidor", e);
    	}
	}
	
	private String getDestinatarioTO(String email) {
		String[] splitted = null;
		
		if (email == null || "".equals(email))
			return "";
		if (! email.contains(","))
			return email;
		splitted = email.split(",");
		email = splitted[0];
		return email;
	}
	
	private String getDestinatarioBCC(String email) {
		List<String> emails = null;
		
		if (email == null || "".equals(email))
			return "";
		if (! email.contains(","))
			return email;
		emails = string2List(email, ",");
		email = StringUtils.join(emails, ",");
		return email;
	}

	private List<String> string2List(String valor, String separador) {
		List<String> valores = new ArrayList<String>();
		String[] splitted = null;
		
		if (valor == null || "".equals(valor.trim()))
			return valores;
		
		splitted = valor.split(separador);
		for (String val : splitted) {
			if ("".equals(val.trim()))
				continue;
			valores.add(val.trim());
		}
		
		return valores;
	}
	
	// --------------------------------------------------------------------------------------	
	// SOLICITUD A BODEGA
	// --------------------------------------------------------------------------------------

	public String lanzarSBO() {
		Long idObra = 0L;
		Long idCoticacion = 0L;
		Long idRequisicion = 0L;
		int generadas = 0;
		String mensaje = "";
		
		control();
		if (this.pojoCotizacion == null) {
			try {
				this.pojoCotizacion = this.ifzCotizaciones.findExtById(this.idCotizacion);
				if (this.pojoCotizacion == null || this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
					control(-1, "Ocurrio un problema al intentar recuperar la base para registrar la Solicitud a Bodega");
					return mensaje;
				}
			} catch (Exception e) {
				control("Ocurrio un problema al intentar recuperar la base para registrar la Solicitud a Bodega", e);
				return mensaje;
			}
		}
		
		if (this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L)
			idCoticacion = this.pojoCotizacion.getId();
		if (this.pojoCotizacion.getIdObra() != null && this.pojoCotizacion.getIdObra().getId() != null && this.pojoCotizacion.getIdObra().getId() > 0L)
			idObra = this.pojoCotizacion.getIdObra().getId();
		if (this.pojoCotizacion.getIdRequisicion() != null && this.pojoCotizacion.getIdRequisicion().getId() != null && this.pojoCotizacion.getIdRequisicion().getId() >= 0L)
			idRequisicion = this.pojoCotizacion.getIdRequisicion().getId();
		generadas = lanzarSolicitudBodega(idObra, idCoticacion, idRequisicion);
		if (generadas < 0)
			mensaje = "No se pudo registrar la Solicitud a Bodega";
		else if (generadas == 1)
			mensaje = "Se genero 1 Solicitud a Bodega";
		else if (generadas > 1)
			mensaje = "Se generaron " + generadas + " Solicitudes a Bodega";
		else
			mensaje = "No se genero ninguna Solicitud a Bodega";
		return mensaje;
	}
	
	private List<Long> encontrarAlmacenes(long idObra) {
		List<ObraAlmacenesExt> asignados = null;
		List<Long> almacenes = null;
		
		try {
			almacenes = new ArrayList<Long>();
			this.ifzObraAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			asignados = this.ifzObraAlmacen.findExtAll(idObra, null);
			if (asignados != null && ! asignados.isEmpty()) {
				for (ObraAlmacenesExt almacen : asignados) {
					if (almacen.getIdAlmacen().getPermitirSBO() == 0)
						continue; // Descartamos bodegas
					almacenes.add(almacen.getIdAlmacen().getId());
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al comprobar los Almacenes asignados a la Obra", e);
		}
		
		return almacenes;
	}
	
	private List<PreCotizacion> existenciasAlmacenes(long idObra, List<PreCotizacion> items) {
		List<PreCotizacion> itemsCotizacion = null;
		List<Long> listProductos = new ArrayList<Long>();
		HashMap<Long, Double> listProductosRestante = new HashMap<Long, Double>();
		List<Long> almacenes = null;
		HashMap<Long, Integer> productosEncontrados = new HashMap<Long, Integer>();
		List<AlmacenProducto> existencias = null;
		AlmacenProducto existencia = null;
		double cantSolicitud = 0;
		double cantidad = 0;
		// ------------------------------------------------------------------------
		SolicitudBodega solicitud = null;
		SolicitudBodegaProducto solicitudProducto = null;
		
		try {
			// Recuperamos Almacenes
			almacenes = this.encontrarAlmacenes(idObra);
			if (almacenes == null || almacenes.isEmpty())
				return items;

			// Generamos listado de productos y reinicio existencia en el insumo(virtualmente)
			for (PreCotizacion item : items) {
				listProductos.add(item.getIdProducto().getId());
				listProductosRestante.put(item.getIdProducto().getId(), item.getRequeridos());
			}
			
			// Comprobamos existencias en los Almances
			itemsCotizacion = new ArrayList<PreCotizacion>();
			itemsCotizacion.addAll(items);
			for (Long almacen : almacenes) {
				items.clear();
				items.addAll(itemsCotizacion);
				itemsCotizacion.clear();
				if (items.isEmpty())
					continue;
				
				// Encontramos existencias de productos en Almacen 
				this.ifzInventario.setInfoSesion(this.loginManager.getInfoSesion());
				existencias = this.ifzInventario.encontrarExistencia(almacen, listProductos);
				if (existencias == null || existencias.isEmpty()) {
					itemsCotizacion.clear();
					itemsCotizacion.addAll(items);
					continue;
				}
				
				// generamos mapa de producto encontrados
				productosEncontrados.clear();
				for (AlmacenProducto item : existencias) 
					productosEncontrados.put(item.getIdProducto(), existencias.indexOf(item));
				
				// Generamos encabezado de Solicitud a Bodega
				solicitud = new SolicitudBodega();
				solicitud.setIdAlmacen(existencias.get(0).getIdAlmacen().getId());
				solicitud.setAlmacen(existencias.get(0).getIdAlmacen().getNombre());
				solicitud.setIdentificador(existencias.get(0).getIdAlmacen().getIdentificador());
				
				for (PreCotizacion insumo : items) {
					// Comprobamos si existe el insumo en los productos encontrados en el Almacen
					if (! productosEncontrados.containsKey(insumo.getIdProducto().getId())) {
						itemsCotizacion.add(insumo);
						continue;
					}
					
					// Actualizo la existencia del detalle
					existencia = existencias.get(productosEncontrados.get(insumo.getIdProducto().getId()));
					cantidad = insumo.getExistencias();
					cantidad += existencia.getExistencia();
					insumo.setExistencias(cantidad);

					cantidad = listProductosRestante.get(insumo.getIdProducto().getId());
					cantSolicitud = cantidad;
					if (cantidad > existencia.getExistencia()) {
						cantSolicitud = existencia.getExistencia();
						insumo.setCantidad((cantidad - cantSolicitud));
						insumo.setSeleccionado(true);
						itemsCotizacion.add(insumo);
					} 
					cantidad = cantidad - cantSolicitud;
					listProductosRestante.put(insumo.getIdProducto().getId(), cantidad);

					solicitudProducto = new SolicitudBodegaProducto();
					solicitudProducto.setIdAlmacen(existencia.getIdAlmacen().getId());
					solicitudProducto.setPojoProducto(insumo.getIdProducto());
					solicitudProducto.setIdProducto(existencia.getIdProducto());
					solicitudProducto.setClave(insumo.getIdProducto().getClave());
					solicitudProducto.setDescripcion(insumo.getIdProducto().getDescripcion());
					if (insumo.getIdProducto().getFamilia() != null) {
						solicitudProducto.setIdFamilia(insumo.getIdProducto().getFamilia().getId());
						solicitudProducto.setFamilia(insumo.getIdProducto().getFamilia().getDescripcion());
					}
					if (insumo.getIdProducto().getUnidadMedida() != null) {
						solicitudProducto.setIdUnidadMedida(insumo.getIdProducto().getUnidadMedida().getId());
						solicitudProducto.setUnidadMedida(insumo.getIdProducto().getUnidadMedida().getDescripcion());
					}
					solicitudProducto.setDisponible(existencia.getExistencia());
					solicitudProducto.setRequeridos(insumo.getRequeridos());
					solicitudProducto.setSolicitud(cantSolicitud);
					solicitud.getListProductos().add(solicitudProducto);
				}
				
				if (! solicitud.getListProductos().isEmpty()) {
					solicitud.ordenarProductos();
					this.listSolicitudes.add(solicitud);
				}
			}
			
			// Dejamos solo los productos que se iran en la cotizacion
			items.clear();
			items.addAll(itemsCotizacion);
		} catch (Exception e) {
			control("Ocurrio un problema al comprobar la existencia de los insumos en Almacenes", e);
		} finally {
			this.paginacionSolicitudBodega = 1;
			this.listSolicitudes = (this.listSolicitudes != null ? this.listSolicitudes : new ArrayList<SolicitudBodega>());
			this.solicitudBodega = (this.listSolicitudes.size() > 0);
		}
		
		return items;
	}

	private String comprobarCambiosExistencia() {
		List<PreCotizacion> detalles = null;
		boolean generarSBO = false;
		// ----------------------------------------------
		List<PreCotizacion> base = null;
		List<PreCotizacion> cotizacionPrevia = null;
		List<SolicitudBodega> solicitudesPrevias = null;
		// ----------------------------------------------
		boolean cambioCotizacion = false;
		boolean cambioSolicitud = false;
		String mensaje = "";

		try {
			generarSBO = this.procesoSolicitudBodega;
			generarSBO = (validaRequest("SBO") ? true : generarSBO);
			// Validamos si procedemos a realizar la comprobacion de existencias en Almacenes
			if (! generarSBO)// || this.pojoCotizacion.getIdObra().getTipoObra() == 4)
				return mensaje;
			
			// Si es una edicion, no debe comprobar existencias
			// ------------------------------------------------------------------------------------------
			if (this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L)
				return mensaje;
			
			// Respaldamos detalles (Cotizacion y Solicitudes)
			// ------------------------------------------------------------------------------------------
			base = new ArrayList<PreCotizacion>();
			base.addAll(this.listPreCotizacionOrigen);
			
			cotizacionPrevia = new ArrayList<PreCotizacion>();
			cotizacionPrevia.addAll(this.listPreCotizacion);
			this.listPreCotizacion.clear();

			solicitudesPrevias = new ArrayList<SolicitudBodega>();
			solicitudesPrevias.addAll(this.listSolicitudes);
			this.listSolicitudes.clear();
			
			// Relanzamos el proceso de verificacion de existencias con los detalles iniciales
			// ------------------------------------------------------------------------------------------
			detalles = new ArrayList<PreCotizacion>();
			if (this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				//generarSBO = (this.pojoCotizacion.getIdObra().getTipoObra() == 4 ? false : generarSBO);
				if (generarSBO && ! verificaAlmacenesBodegaObra(false)) { //&& ! verificaAlmacenesBodegaObra(true)) {
					control(10, "No se puede generar Solicitud a Bodega.\nLa obra no tiene asignados Almacenes/Bodega");
					generarSBO = false;
				}
				
				if (generarSBO) 
					detalles = this.existenciasAlmacenes(this.pojoCotizacion.getIdObra().getId(), base);
			}
			
			// Comprobamos cambios en Cotizacion
			// ----------------------------[ No tenia productos y ahora si ] ------------------------ [ Tenia productos y ahora ya no ] ------------------
			cambioCotizacion = ((cotizacionPrevia.isEmpty() && ! detalles.isEmpty()) || (! cotizacionPrevia.isEmpty() && detalles.isEmpty()));
			if (! cambioSolicitud && ! cotizacionPrevia.isEmpty() && ! detalles.isEmpty()) { // Comprobamos diferencias
				for (PreCotizacion previa : cotizacionPrevia) {
					if (detalles.isEmpty())
						break;
					for (PreCotizacion detalle : detalles) {
						if (detalle.getProductoId() == previa.getProductoId()) {
							if (detalle.getCantidad() == previa.getCantidad()) {
								detalle.setSeleccionado(previa.isSeleccionado());
								continue;
							}
							
							detalle.setCantidad(previa.getCantidad());
							cambioCotizacion = true;
							break;
						}
					}
				}
			}
			
			if (! cambioCotizacion && ! this.solicitudBodega) {
				for (PreCotizacion detalle : detalles) 
					detalle.setExistencias(0);
			}
			
			// Solicitudes
			// -------------------------------[ No tenia solicitud y ahora si ] --------------------------------- [ Tenia solicitud y ahora ya no ] ------------------
			cambioSolicitud = ((solicitudesPrevias.isEmpty() && ! this.listSolicitudes.isEmpty()) || (! solicitudesPrevias.isEmpty() && this.listSolicitudes.isEmpty()));
			if (! cambioSolicitud) // Comprobamos diferencias
				cambioSolicitud = comprobarCambiosSolicitudes(solicitudesPrevias, this.listSolicitudes);

			// Alertamos si hubo cambios
			// ------------------------------------------------------------------------------------------
			if (cambioCotizacion || cambioSolicitud) 
				mensaje = "Se detecto un cambio en existencias de Almacenes.\nLas cantidades en Cotizacion y/o Solicitud a Bodega fueron actualizadas";
			
			// Actualizamos listado
			if (this.listPreCotizacion.isEmpty() && ! detalles.isEmpty()) 
				this.listPreCotizacion.addAll(detalles);
			if (this.listPreCotizacion.isEmpty() && this.solicitudBodega) {
				control(100, "Todo el material esta cubierto en Solicitud(es) a Bodega");
				return "";
			}
		} catch (Exception e) {
			control("Ocurrio un problema al comprobar la existencia de los insumos en Almacenes", e);
			mensaje = "Ocurrio un problema al comprobar la existencia de los insumos en Almacenes";
		} finally {
			this.paginacionProductos = 1;
			this.listPreCotizacion = (this.listPreCotizacion != null ? this.listPreCotizacion : new ArrayList<PreCotizacion>());
			this.paginacionSolicitudBodega = 1;
			this.listSolicitudes = (this.listSolicitudes != null ? this.listSolicitudes : new ArrayList<SolicitudBodega>());
			this.solicitudBodega = (this.listSolicitudes.size() > 0);
		}
		
		return mensaje;
	}

	private boolean comprobarCambiosSolicitudes(List<SolicitudBodega> original, List<SolicitudBodega> nueva) {
		if ((original == null || original.isEmpty()) && (nueva == null || nueva.isEmpty()))
			return false;
		return false;
	}
	
	private int lanzarSolicitudBodega(Long idObra, Long idCoticacion, Long idRequisicion) {
		Respuesta respuesta = null;
		int generadas = 0;
		boolean lanzar = false;
		
		try {
			// Validaciones
			if (! this.procesoSolicitudBodega && ! validaRequest("SBO")) 
				return 0;
			if (this.listSolicitudes == null || this.listSolicitudes.isEmpty())
				return 0;
			lanzar = (this.origenFromRequisicion) ? true : comprobarPrimeraCotizacion();
			if (! lanzar)
				return 0;
			
			// Lanzamos solicitud
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzCotizaciones.solicitudBodega(idObra, idCoticacion, idRequisicion, this.listSolicitudes);
			generadas = (int) respuesta.getBody().getValor("solicitudes");
			if (respuesta.getErrores().getCodigoError() > 0L) {
				control(-1, respuesta.getErrores().getDescError());
				return -1;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al registrar la Solicitud a Bodega", e);
			generadas = -1;
		} 
		
		return generadas;
	}

	// -------------------------------------------------------------------------------------------------------------------
	// Busqueda Obra 
	// -------------------------------------------------------------------------------------------------------------------

	public void nuevaBusquedaObra() {
		control();
		this.campoBusquedaObras = this.opcionesBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.obraAdministrativa = false;
		
		this.paginacionBusquedaObras = 1;
		this.listObras = new ArrayList<Obra>();
		this.pojoObra = null;
	}

	public void buscarObras() {
		boolean incluirObrasAdministrativas = false;
		
		try {
			control();
			this.campoBusquedaObras = (this.campoBusquedaObras != null && ! "".equals(this.campoBusquedaObras.trim()) ? this.campoBusquedaObras.trim() : this.opcionesBusquedaObras.get(0).getValue().toString());
			this.valorBusquedaObras = (this.valorBusquedaObras != null ? this.valorBusquedaObras.trim() : "");
			
			// No se permiten Cotizaciones Administrativas, solo ADMINISTRADOR 
			incluirObrasAdministrativas = this.esAdministrativo; //(this.esAdministrativo && "ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario())); 
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, incluirObrasAdministrativas, false, "", 0);
			if (this.listObras == null || this.listObras.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch(Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
    	} finally {
			this.paginacionBusquedaObras = 1;
			this.listObras = (this.listObras != null ? this.listObras : new ArrayList<Obra>());
			controlLog(this.listObras.size() + " Obras encontradas. [" + this.campoBusqueda + " - " + this.valorBusqueda + "]");
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

			this.idExplosionInsumos = 0L;
			this.idExplosionInsumos = this.ifzInsumos.findIdActual(this.pojoObra.getId()); 
			this.estatusExplosionInsumos = this.ifzInsumos.estatusExplosionInsumos(this.idExplosionInsumos); 
			
			if (this.listCotizaciones != null && ! this.listCotizaciones.isEmpty()) 
				buscar();
		} catch(Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		}
	}
	
	public void quitarObra() {
		try {
			control();
			this.pojoObraBase = null;
			this.permiteModificar = false;
		} catch(Exception e) {
			control("Ocurrio un problema al recuperar la Obra seleccionada", e);
		}
	}
	
	// --------------------------------------------------------------------------------------	
	// Busqueda Requisiciones
	// --------------------------------------------------------------------------------------	
	
	public void nuevaBusquedaRequisiciones() { 
		control();
		this.campoBusquedaRequisiciones = this.opcionesBusquedaRequisiciones.get(0).getValue().toString();
		this.valorBusquedaRequisiciones = "";

		this.paginacionBusquedaRequisiciones = 1;
		this.listRequisiciones = new ArrayList<Requisicion>();
		this.pojoRequisicion = null;
	}
	
	public void buscarRequisiciones() { 
		String orderBy = "";
		String valor = "";
		
		try {
			control();
			this.campoBusquedaRequisiciones = (this.campoBusquedaRequisiciones != null && ! "".equals(this.campoBusquedaRequisiciones.trim()) ? this.campoBusquedaRequisiciones.trim() : this.opcionesBusquedaRequisiciones.get(0).getValue().toString());
			this.valorBusquedaRequisiciones = (this.valorBusquedaRequisiciones != null ? this.valorBusquedaRequisiciones.trim() : "");
			
			valor = this.valorBusquedaRequisiciones.trim().replace(" ", "%");
			orderBy = "case model.estatus when 0 then 1 else 2 end, case model.sistema when 0 then 1 when 1 then 2 else 3 end, model.fecha desc, model.id desc";
			this.ifzReq.setInfoSesion(this.loginManager.getInfoSesion());
			this.listRequisiciones = this.ifzReq.findLikeProperty(this.campoBusquedaRequisiciones, valor, this.pojoObraBase.getId(), this.tipoMaestro.ordinal(), false, false, 0L, orderBy, 0);
			if (this.listRequisiciones == null || this.listRequisiciones.isEmpty()) 
				control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
    		control("Ocurrio un problema al realizar la busqueda de Requisiciones", e);
    	} finally {
			this.paginacionBusquedaRequisiciones = 1;
			this.listRequisiciones = (this.listRequisiciones != null ? this.listRequisiciones : new ArrayList<Requisicion>());
			controlLog(this.listRequisiciones.size() + " Requisiciones encontradas. [" + this.campoBusquedaRequisiciones + " - " + valor + "]");
    	}
	}
	
	public void seleccionarRequisicion() { 
		List<RequisicionDetalle> requisicionDetalles = null;
		List<RequisicionDetalle> auxiliar = null;
		RequisicionExt requisicion = null;
		EmpleadoExt comprador = null;
		
		try {
			control();
			// Comprobamos requisicion
			if (this.pojoRequisicion == null || this.pojoRequisicion.getId() == null || this.pojoRequisicion.getId() <= 0L) {
				control(-1, "Debe seleccionar una Requisicion");
				return;
			}

			// Comprobamos Productos/Material de la Requisicion
			requisicionDetalles = this.ifzReqDetalle.findAll(this.pojoRequisicion.getId()); 
			if (requisicionDetalles == null || requisicionDetalles.isEmpty()) {
				control(-1, "La Requisicion no tiene productos asignados");
				return;
			}

			auxiliar = new ArrayList<RequisicionDetalle>();
			for (RequisicionDetalle detalle : requisicionDetalles) {
				if (detalle.getPendiente() > 0)
					auxiliar.add(detalle);
			}
			
			if (auxiliar == null || auxiliar.isEmpty()) {
				control(-1, "La Requisicion no tiene productos disponibles");
				return;
			}

			// Añadimos la requisicion a la Cotizacion
			requisicion = this.ifzReq.findExtById(this.pojoRequisicion.getId());
			
			this.pojoCotizacion = new CotizacionExt();
			this.pojoCotizacion.setIdObra(requisicion.getIdObra());
			this.pojoCotizacion.setIdRequisicion(requisicion);
			this.pojoCotizacion.setIdMoneda(this.pojoRequisicion.getIdMoneda());
			this.idMoneda = this.pojoRequisicion.getIdMoneda();
			
			// Asigno comprador
			this.idComprador = 0L;
			comprador = this.ifzEmpleados.findByIdExt(this.pojoRequisicion.getIdSolicita());
			if (comprador != null && comprador.getId() != null && comprador.getId() > 0L) {
				this.pojoCotizacion.setIdComprador(comprador);
				this.idComprador = comprador.getId();
			}

			this.origenFromRequisicion = true;
			//nuevaBusquedaRequisiciones();
			nuevo();
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar la Requisicion seleccionada", e);
    	} 
	}
	
	// --------------------------------------------------------------------------------------	
	// Busqueda Proveedores
	// --------------------------------------------------------------------------------------	
	
	public void nuevaBusquedaProveedores() {
		control();
		this.campoBusquedaProveedores = this.opcionesBusquedaProveedores.get(0).getValue().toString();
		this.valorBusquedaProveedores = "";
		this.tipoPersonaBusquedaProveedores = "N";

		this.paginacionBusquedaProveedores = 1;
		this.listProveedores = new ArrayList<PersonaExt>();
		this.pojoProveedor = null;
    }

	public void buscarProveedores() {
		String valor = "";
		
		try {
			control();
			this.campoBusquedaProveedores = (this.campoBusquedaProveedores != null && ! "".equals(this.campoBusquedaProveedores.trim()) ? this.campoBusquedaProveedores.trim() : this.opcionesBusquedaProveedores.get(0).getValue().toString());
			this.valorBusquedaProveedores = (this.valorBusquedaProveedores != null ? this.valorBusquedaProveedores.trim() : "");
			this.tipoPersonaBusquedaProveedores = (this.tipoPersonaBusquedaProveedores != null && ! "".equals(this.tipoPersonaBusquedaProveedores.trim()) ? this.tipoPersonaBusquedaProveedores.trim() : "N");

			if ("".equals(this.valorBusquedaProveedores.trim())) {
				control(-1, "Indique un valor de busqueda");
				return;
			}
			
			stackTrace("Buscando obras. " + this.campoBusquedaProveedores + ":" + this.valorBusquedaProveedores);
			valor = this.valorBusquedaProveedores.trim().replace(" ", "%");
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listProveedores = this.ifzCotizaciones.findPersonaLikeProperty(this.campoBusquedaProveedores, valor, this.tipoPersonaBusquedaProveedores, 80);
			if (this.listProveedores == null || this.listProveedores.isEmpty())
				control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Proveedores", e);
    	} finally {
			this.paginacionBusquedaProveedores = 1;
			this.listProveedores = (this.listProveedores != null ? this.listProveedores : new ArrayList<PersonaExt>());
			controlLog(this.listProveedores.size() + " Proveedores encontrados. [" + this.campoBusquedaProveedores + " - " + this.valorBusquedaProveedores + "]");
    	}
	}

	public void seleccionarProveedor() {
		PersonaExt pojoContacto = null;
		
		try {
			control();
			if (this.pojoProveedor == null || pojoProveedor.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Proveedor seleccionado");
				return;
			}
			
			// Asigno proveedor y tipo de persona: Persona (P) o Negocio (N)
			this.pojoCotizacion.setIdProveedor(this.pojoProveedor);
			this.pojoCotizacion.setTipoPersonaProveedor(this.tipoPersonaBusquedaProveedores);
			// Recuperamos el contacto del proveedor seleccionado
			stackTrace("Recuperando contacto para proveedor: " + this.pojoCotizacion.getIdProveedor().getId() + " - " + this.pojoCotizacion.getIdProveedor().getNombre());
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			pojoContacto = this.ifzCotizaciones.findContactoByProveedor(this.pojoCotizacion.getIdProveedor(), this.tipoPersonaBusquedaProveedores);
			if (pojoContacto == null) 
				stackTrace("El Proveedor no tiene asignado ningun Contacto");
			if (pojoContacto != null)
				this.pojoCotizacion.setIdContacto(pojoContacto);
			nuevaBusquedaProveedores();
    	} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Proveedor seleccionado", e);
    	}
	}

	// --------------------------------------------------------------------------------------	
	// PROPIEDADES
	// --------------------------------------------------------------------------------------	

	public boolean getHasId() {
		return (this.pojoCotizacion != null && this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L);
	}
	
	public void setHasId(boolean value) {}
	
	public String getTitulo() {
		if (getHasId())
			return "Cotizacion " + this.pojoCotizacion.getId() + (this.tipoMaestro == TipoMaestro.Administrativo ? " [ADMINISTRATIVA]" : "");
		return "Nueva Cotizacion " + (this.tipoMaestro == TipoMaestro.Administrativo ? "[ADMINISTRATIVA]" : "");
	}
	
	public void setTitulo(String value) {}
	
	public boolean getEditable() {
		if (! getHasId())
			return true;
		if (this.editarCotizacion)
			return true;
		return getPermisoAdmin();
	}
	
	public void setEditable(boolean value) {}

	public String getObraBase() {
		if (this.pojoObraBase != null && this.pojoObraBase.getId() != null && this.pojoObraBase.getId() > 0L)
			return "<b>" + this.pojoObraBase.getId() + "</b> - " + this.pojoObraBase.getNombre();
		return "";
	}

	public void setObraBase(String value) {}

	public String getObraBaseTitle() {
		if (this.pojoObraBase != null && this.pojoObraBase.getId() != null && this.pojoObraBase.getId() > 0L)
			return this.pojoObraBase.getId() + " - " + this.pojoObraBase.getNombre();
		return "";
	}

	public void setObraBaseTitle(String value) {}

	public String getObra() {
		if (this.pojoCotizacion != null && this.pojoCotizacion.getIdObra() != null && this.pojoCotizacion.getIdObra().getId() != null && this.pojoCotizacion.getIdObra().getId() > 0L)
			return "<b>" + this.pojoCotizacion.getIdObra().getId() + "</b> - " + this.pojoCotizacion.getIdObra().getNombre();
		return "";
	}

	public void setObra(String value) {}

	public int getTipoCotizacion() {
		if (this.pojoObraBase != null && this.pojoObraBase.getId() != null && this.pojoObraBase.getId() > 0L)
			return ((this.pojoObraBase.getTipoObra() == 4) ? 2 : 1);
		return 0;
	}
	
	public void setTipoCotizacion(int value) {}
	
	public List<Cotizacion> getListCotizaciones() {
		return listCotizaciones;
	}

	public void setListCotizaciones(List<Cotizacion> listCotizaciones) {
		this.listCotizaciones = listCotizaciones;
	}

	public List<CotizacionDetalleExt> getListDetalles() {
		return listDetalles;
	}

	public void setListDetalles(List<CotizacionDetalleExt> listDetalles) {
		this.listDetalles = listDetalles;
	}

	public List<SelectItem> getListCompradoresItems() {
		return listCompradoresItems;
	}

	public void setListCompradoresItems(List<SelectItem> listCompradoresItems) {
		this.listCompradoresItems = listCompradoresItems;
	}

	public long getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public long getIdExplosionInsumos() {
		return idExplosionInsumos;
	}

	public void setIdExplosionInsumos(long idExplosionInsumos) {
		this.idExplosionInsumos = idExplosionInsumos;
	}
	
	public String getFolio() {
		if (this.pojoCotizacion != null && this.pojoCotizacion.getFolio() != null && ! "".equals(this.pojoCotizacion.getFolio().trim()))
			return "<b>" + this.pojoCotizacion.getFolio() + "</b>";
		return "<i>Se genera al guardar</i>";
	}
	
	public void setFolio(String value) {}
	
	public String getProveedor() {
		if (this.pojoCotizacion != null && this.pojoCotizacion.getIdProveedor() != null && this.pojoCotizacion.getIdProveedor().getId() > 0L)
			return this.pojoCotizacion.getIdProveedor().getId() + " - " + this.pojoCotizacion.getIdProveedor().getNombre() + " - <b>" + this.pojoCotizacion.getIdProveedor().getRfc() + "</b>";
		return "";
	}
	
	public void setProveedor(String value) {}
	
	public String getProveedorContacto() {
		if (this.pojoCotizacion != null && this.pojoCotizacion.getIdContacto() != null && this.pojoCotizacion.getIdContacto().getId() > 0L)
			return this.pojoCotizacion.getIdContacto().getId() + " - " + this.pojoCotizacion.getIdContacto().getNombre();
		return "";
	}
	
	public void setProveedorContacto(String value) {}

	public List<PreCotizacion> getListPreCotizacion() {
		return listPreCotizacion;
	}

	public void setListPreCotizacion(List<PreCotizacion> listPreCotizacion) {
		this.listPreCotizacion = listPreCotizacion;
	}

	public int getPaginacionProductos() {
		return paginacionProductos;
	}

	public void setPaginacionProductos(int paginacionProductos) {
		this.paginacionProductos = paginacionProductos;
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

	public List<SelectItem> getReqOpcionesBusqueda() {
		return opcionesBusquedaRequisiciones;
	}

	public void setReqOpcionesBusqueda(List<SelectItem> reqOpcionesBusqueda) {
		this.opcionesBusquedaRequisiciones = reqOpcionesBusqueda;
	}

	public String getReqCampoBusqueda() {
		return campoBusquedaRequisiciones;
	}

	public void setReqCampoBusqueda(String reqCampoBusqueda) {
		this.campoBusquedaRequisiciones = reqCampoBusqueda;
	}

	public String getReqValorBusqueda() {
		return valorBusquedaRequisiciones;
	}

	public void setReqValorBusqueda(String reqValorBusqueda) {
		this.valorBusquedaRequisiciones = reqValorBusqueda;
	}

	public int getReqPaginacion() {
		return paginacionBusquedaRequisiciones;
	}

	public void setReqPaginacion(int reqPaginacion) {
		this.paginacionBusquedaRequisiciones = reqPaginacion;
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

	public double getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(double porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public double getSubtotal() {
		if (this.pojoCotizacion != null)
			return this.pojoCotizacion.getSubtotal();
		return 0;
	}

	public void setSubtotal(double subtotal) {}

	public double getIva() {
		if (this.pojoCotizacion != null)
			return this.pojoCotizacion.getIva();
		return 0;
	}

	public void setIva(double iva) {}

	public double getTotal() {
		if (this.pojoCotizacion != null)
			return this.pojoCotizacion.getTotal();
		return 0;
	}

	public void setTotal(double total) {}

	public long getIdComprador() {
		return idComprador;
	}

	public void setIdComprador(long idComprador) {
		this.idComprador = idComprador;
	}

	public boolean isSeleccionarTodos() {
		return seleccionarTodos;
	}

	public void setSeleccionarTodos(boolean seleccionarTodos) {
		if (this.seleccionarTodos == seleccionarTodos)
			return;
		this.seleccionarTodos = seleccionarTodos;
		if (this.listPreCotizacion != null && ! this.listPreCotizacion.isEmpty()) {
			for (PreCotizacion var : this.listPreCotizacion)
				var.setSeleccionado(seleccionarTodos);
		}
	}

	public boolean isEditarCotizacion() {
		return editarCotizacion;
	}

	public void setEditarCotizacion(boolean editarCotizacion) {
		this.editarCotizacion = editarCotizacion;
	}

	public boolean isEsAdministrativo() {
		return esAdministrativo;
	}

	public void setEsAdministrativo(boolean esAdministrativo) {
		this.esAdministrativo = esAdministrativo;
	}

	public boolean isOrigenFromRequisicion() {
		return origenFromRequisicion;
	}

	public void setOrigenFromRequisicion(boolean origenFromRequisicion) {
		this.origenFromRequisicion = origenFromRequisicion;
	}
	
	public List<SelectItem> getListMonedasItems() {
		return listMonedasItems;
	}

	public void setListMonedasItems(List<SelectItem> listMonedasItems) {
		this.listMonedasItems = listMonedasItems;
	}

	public long getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(long idMoneda) {
		this.idMoneda = idMoneda;
	}
	
	public long getMoneda() {
		return (this.pojoCotizacion != null ? this.pojoCotizacion.getIdMoneda() : 0L);
	}
	
	public void setMoneda(long idMoneda) {
		if (this.pojoCotizacion == null)
			return;
		if (this.listMonedas == null || this.listMonedas.isEmpty())
			return;

		this.idMoneda = idMoneda;
		this.pojoCotizacion.setIdMoneda(idMoneda);
		this.pojoCotizacion.setMoneda("");
		for (Moneda moneda : this.listMonedas) {
			if (idMoneda == moneda.getId()) {
				this.pojoCotizacion.setMoneda(moneda.getAbreviacion());
				break;
			}
		}
	}

	public double getTipoCambio() {
		return tipoCambio;
	}

	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	public boolean isProcesoSolicitudBodega() {
		return procesoSolicitudBodega;
	}

	public void setProcesoSolicitudBodega(boolean procesoSolicitudBodega) {
		this.procesoSolicitudBodega = procesoSolicitudBodega;
	}

	public List<SolicitudBodega> getListSolicitudes() {
		return listSolicitudes;
	}

	public void setListSolicitudes(List<SolicitudBodega> listSolicitudes) {
		this.listSolicitudes = listSolicitudes;
	}

	public int getPaginacionSolicitudBodega() {
		return paginacionSolicitudBodega;
	}

	public void setPaginacionSolicitudBodega(int paginacionSolicitudBodega) {
		this.paginacionSolicitudBodega = paginacionSolicitudBodega;
	}

	public boolean isSolicitudBodega() {
		return solicitudBodega;
	}

	public void setSolicitudBodega(boolean solicitudBodega) {
		this.solicitudBodega = solicitudBodega;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}

	public List<SelectItem> getOpcionesBusquedaObra() {
		return opcionesBusquedaObras;
	}

	public void setOpcionesBusquedaObra(List<SelectItem> opcionesBusquedaObra) {
		this.opcionesBusquedaObras = opcionesBusquedaObra;
	}

	public String getCampoBusquedaObra() {
		return campoBusquedaObras;
	}

	public void setCampoBusquedaObra(String campoBusquedaObra) {
		this.campoBusquedaObras = campoBusquedaObra;
	}

	public String getValorBusquedaObra() {
		return valorBusquedaObras;
	}

	public void setValorBusquedaObra(String valorBusquedaObra) {
		this.valorBusquedaObras = valorBusquedaObra;
	}

	public boolean isObraAdministrativa() {
		return obraAdministrativa;
	}

	public void setObraAdministrativa(boolean obraAdministrativa) {
		this.obraAdministrativa = obraAdministrativa;
	}

	public int getPaginacionObras() {
		return paginacionBusquedaObras;
	}

	public void setPaginacionObras(int paginacionObras) {
		this.paginacionBusquedaObras = paginacionObras;
	}

	public Obra getPojoObraSeleccionada() {
		return pojoObra;
	}

	public void setPojoObraSeleccionada(Obra pojoObraSeleccionada) {
		this.pojoObra = pojoObraSeleccionada;
	}
	
	public CotizacionExt getPojoCotizacion() {
		return pojoCotizacion;
	}

	public void setPojoCotizacion(CotizacionExt pojoCotizacion) {
		this.pojoCotizacion = pojoCotizacion;
	}

	public String getCotizacionExplInsumos() {
		if (this.idExplosionInsumos > 0L && ! this.origenFromRequisicion)
			return "<b>" + this.idExplosionInsumos + "</b>";
		return "";
	}
	
	public void setCotizacionExplInsumos(String value) {}

	public String getCotizacionRequisicion() {
		if (this.pojoCotizacion != null && this.pojoCotizacion.getIdRequisicion() != null && this.pojoCotizacion.getIdRequisicion().getId() != null && this.pojoCotizacion.getIdRequisicion().getId() > 0L) 
			return "<b>" + this.pojoCotizacion.getIdRequisicion().getId() + "</b> - " + this.pojoCotizacion.getIdRequisicion().getNombreSolicita();
		return "";
	}

	public void setCotizacionRequisicion(String value) {}
	
	public String getCotizacionOrdenCompra() {
		return (this.cotizacionOrdenCompra != null && ! "".equals(this.cotizacionOrdenCompra.trim())) ? this.cotizacionOrdenCompra.trim() : "";
	}
	
	public void setCotizacionOrdenCompra(String value) {}

	public String getOrigenBaseCotizacon() {
		return origenBaseCotizacon;
	}

	public void setOrigenBaseCotizacon(String origenBaseCotizacon) {
		this.origenBaseCotizacon = origenBaseCotizacon;
	}

	public String getEstatusExplosionInsumos() {
		if (this.idExplosionInsumos > 0L)
			return ((this.estatusExplosionInsumos == 2) ? "Explosion de Insumos SUMINISTRADA" : ((this.estatusExplosionInsumos == 1) ? "Explosion de Insumos CANCELADA" : ""));
		return "Sin Explosion de Insumos";
	}

	public void setEstatusExplosionInsumos(String value) {}
	
	public boolean getDeshabilitarExplosionInsumos() {
		return ! (this.idExplosionInsumos > 0L && this.estatusExplosionInsumos == 0);
	}
	
	public void setDeshabilitarExplosionInsumos(boolean value) {}

	// Proveedores
	
	public List<PersonaExt> getListProveedores() {
		return listProveedores;
	}

	public void setListProveedores(List<PersonaExt> listProveedores) {
		this.listProveedores = listProveedores;
	}

	public PersonaExt getPojoProveedor() {
		return pojoProveedor;
	}

	public void setPojoProveedor(PersonaExt pojoProveedor) {
		this.pojoProveedor = pojoProveedor;
	}

	public List<SelectItem> getTiposBusquedaProveedores() {
		return opcionesBusquedaProveedores;
	}

	public void setTiposBusquedaProveedores(List<SelectItem> tiposBusquedaProveedores) {
		this.opcionesBusquedaProveedores = tiposBusquedaProveedores;
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
		return tipoPersonaBusquedaProveedores;
	}

	public void setValorBusquedaTipoProveedor(String valorBusquedaTipoProveedor) {
		this.tipoPersonaBusquedaProveedores = valorBusquedaTipoProveedor;
	}

	public int getNumPaginaProveedores() {
		return paginacionBusquedaProveedores;
	}

	public void setNumPaginaProveedores(int numPaginaProveedores) {
		this.paginacionBusquedaProveedores = numPaginaProveedores;
	}

	// EMAIL
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailCC() {
		return emailCC;
	}

	public void setEmailCC(String emailCC) {
		this.emailCC = emailCC;
	}

	public String getEmailCCO() {
		return emailCCO;
	}

	public void setEmailCCO(String emailCCO) {
		this.emailCCO = emailCCO;
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

	// Familias
	
	public List<SelectItem> getListFamiliasItems() {
		return listFamiliasItems;
	}

	public void setListFamiliasItems(List<SelectItem> listFamiliasItems) {
		this.listFamiliasItems = listFamiliasItems;
	}

	public long getIdFamilia() {
		return idFamilia;
	}

	public void setIdFamilia(long idFamilia) {
		this.idFamilia = idFamilia;
	}

	// ---------------------------------------------------------------------------
	// CONTROL
	// ---------------------------------------------------------------------------
	
	public boolean getOperacion() {
		return this.operacionCancelada;
	}

	public void setOperacion(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
	}

	public int getTipoMensaje() {
		return this.tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getMensaje() {
		return this.mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	// ---------------------------------------------------------------------------
	// PERMISOS
	// ---------------------------------------------------------------------------
	
	public boolean getPermisoAdmin() {
		return ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()));
	}
	
	public void setPermisoAdmin(boolean value) {}
}
