package net.giro.compras.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.beans.RequisicionDetalleExt;
import net.giro.compras.beans.RequisicionExt;
import net.giro.compras.beans.SolicitudBodega;
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.comun.TiposObra;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.comun.TipoInsumo;
import net.giro.inventarios.comun.TipoMaestro;
import net.giro.inventarios.logica.ProductoRem;
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
import net.giro.tyg.admon.MonedasValores;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.dao.MonedasValoresDAO;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="reqAction")
public class RequisicionesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(RequisicionesAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	// Interfaces
	private RequisicionRem ifzRequisiciones;
	private RequisicionDetalleRem ifzReqDetalles;
	private ObraRem ifzObras;
	private EmpleadoRem ifzEmpleados;
	private ProductoRem ifzProductos;
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private ReportesRem	ifzReportes;
	private OrdenCompraRem ifzOrdenes;
	private OrdenCompraDetalleRem ifzOrdenDetalles;
	private CotizacionRem ifzCotizaciones;
	private CotizacionDetalleRem ifzCotiDetalles;
	// Listas
	private List<RequisicionDetalleExt> listDetalles;
	private List<RequisicionDetalleExt> listDetallesEliminados;
	private List<EmpleadoExt> listEmpleados;
	private List<SelectItem> listEmpleadosItems;
	private List<ConValores> listEspecialidades;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	// POJO's
	private long idRequisicion;
	private RequisicionExt pojoRequisicion;
	private RequisicionDetalleExt pojoReqDetalle;
	private RequisicionDetalleExt pojoReqDetalleBorrar;
	private ConGrupoValores pojoGpoMaestros;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	private ConGrupoValores pojoGpoUnidadesMedida;
	// Busqueda principal
	private List<Requisicion> listRequisiciones;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
	private int numPagina;
	// Busqueda obras
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> tiposBusquedaObras;	
	private List<SelectItem> tiposObras;
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorBusquedaTipoObra;
	private int numPaginaObras;
	private boolean avanzadaBusquedaObras;
	// Busqueda productos
	private int tipoMaestroBusqueda;
	private List<Producto> listProductos;
	private Producto pojoProductoSeleccionado;
	private List<SelectItem> tiposBusquedaProductos;	
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private long familiaBusquedaProductos;
	private int numPaginaProductos;	
	// Nuevo Producto
	private MonedasValoresDAO ifzMonValores;
	private ProductoExt pojoProducto; 
	private Producto pojoProductoNuevo;
	private List<ConValores> listUnidadesMedida;
	private List<SelectItem> listUnidadesMedidaItems;
	private boolean previaBusquedaProducto;
	private long idEspecialidadActivo;
	// Monedas
	private MonedaDAO ifzMonedas;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private long idMonedaSugerida;
	private long idMoneda;
	// Variables de operacion
	private double cantidadProducto;
	private long valComprador;
	private boolean permiteEditar;
	private TipoMaestro tipoMaestro;
	private ConGrupoValores grupoGastos;
	private List<Long> idPuestoCompradores;
	private List<Long> idEmpleadosCompradores;
	// Solicitud a Bodega
	private List<SolicitudBodega> listSolicitudes;
	private int numPaginaSolicitudBodega;
	// Control
    private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	// PERFILES
	private boolean perfilEgresos;
	private double limiteInferiorActivos;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public RequisicionesAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		Moneda valMoneda = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			valPerfil = this.entornoProperties.getString("SYS_CONFIG_COMPRADORES");
			if (valPerfil != null && ! "".equals(valPerfil.trim()))
				this.idPuestoCompradores = getPuestosCompradores(valPerfil);
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey(), item.getValue());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			ctx = new InitialContext();
			this.ifzRequisiciones = (RequisicionRem) ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalles = (RequisicionDetalleRem) ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzEmpleados = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzProductos = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzGpoVal = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzMonedas = (MonedaDAO) ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzMonValores = (MonedasValoresDAO) ctx.lookup("ejb:/Model_TYG//MonedasValoresImp!net.giro.tyg.dao.MonedasValoresDAO");
			this.ifzOrdenes = (OrdenCompraRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOrdenDetalles = (OrdenCompraDetalleRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzCotizaciones = (CotizacionRem) ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzCotiDetalles = (CotizacionDetalleRem) ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");

			this.ifzRequisiciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReqDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoVal.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCotiDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			valMoneda = this.ifzMonedas.findByAbreviacion(valPerfil);
			if (valMoneda != null)
				this.idMonedaSugerida = valMoneda.getId();

			// EVALUACION DE PERFILES
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_LIMITE_INFERIOR_GASTOS");
			this.limiteInferiorActivos = ((valPerfil != null && ! "".equals(valPerfil)) ? Double.valueOf(valPerfil.trim()) : 5000);

			/*valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_REQUISICION_SIN_LIMITE");
			if (valPerfil != null && ! "".equals(valPerfil.trim()))
				this.requisicionSinLimites = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);*/
			
			// Grupo de valores para MAESTROS de productos
			this.pojoGpoMaestros = this.ifzGpoVal.findByName("SYS_CODE_NIVEL0");
			if (this.pojoGpoMaestros == null || this.pojoGpoMaestros.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_CODE_NIVEL0 (Maestros) en con_grupo_valores");
			
			// Grupo de valores para ESPECIALIDADES de productos
			this.pojoGpoEspecialidades = this.ifzGpoVal.findByName("SYS_PRODUCTO_ESPECIALIDADES");
			if (this.pojoGpoEspecialidades == null || this.pojoGpoEspecialidades.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_PRODUCTO_ESPECIALIDADES en con_grupo_valores");
			
			// Grupo de valores para FAMILIAS de productos 
			this.pojoGpoFamilias = this.ifzGpoVal.findByName("SYS_FAMILIA_PRODUCTO");
			if (this.pojoGpoFamilias == null || this.pojoGpoFamilias.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_FAMILIA_PRODUCTO en con_grupo_valores");
			
			// Grupo de valores para UNIDAD DE MEDIDA de productos 
			this.pojoGpoUnidadesMedida = this.ifzGpoVal.findByName("SYS_UNIDAD_MEDIDA");
			if (this.pojoGpoUnidadesMedida == null || this.pojoGpoUnidadesMedida.getId() <= 0L)
				throw new Exception("No se encontro encontro el grupo SYS_UNIDAD_MEDIDA en con_grupo_valores");
			
			// Grupo de valores para SYS_MOVGTOS (Gastos)
			this.grupoGastos = this.ifzGpoVal.findByName("SYS_MOVGTOS");
			if (this.grupoGastos == null || this.grupoGastos.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");
			
			// Inicializaciones
			this.listRequisiciones = new ArrayList<Requisicion>();
			this.listDetalles = new ArrayList<RequisicionDetalleExt>();
			this.listSolicitudes = new ArrayList<SolicitudBodega>();
			this.listObras = new ArrayList<Obra>();
			this.pojoRequisicion = new RequisicionExt();
			this.tipoMaestro = TipoMaestro.Inventario;
			this.tipoMaestroBusqueda = (this.perfilEgresos ? 0 : 1);
			this.idEspecialidadActivo = 10002580;
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("nombreObra", (this.perfilEgresos ? "Obra/Egreso Administrativo" : "Obra")));
			this.tiposBusqueda.add(new SelectItem("nombreSolicita", "Comprador"));
			this.tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.tiposBusqueda.add(new SelectItem("idObra", "ID Obra"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.fechaBusqueda = Calendar.getInstance().getTime();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda obras
			this.tiposObras = new ArrayList<SelectItem>();
			this.tiposObras.add(new SelectItem(0, "Todos"));
			this.tiposObras.add(new SelectItem(1, "Obra"));
			this.tiposObras.add(new SelectItem(2, "Proyecto"));
			this.tiposObras.add(new SelectItem(3, "Orden de Trabajo"));
			
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
			
			// Busqueda productos
			this.tiposBusquedaProductos = new ArrayList<SelectItem>();
			this.tiposBusquedaProductos.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaProductos.add(new SelectItem("clave", "Clave"));
			this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
			nuevaBusquedaProductos();
			
			cargarSolicitantes();
			cargarMonedas();
		} catch (Exception e) {
			log.error("Error en Compras.RequisicionAction.constructor RequisicionesAction", e);
		}
	}
	
	public void buscar() {
		long idPropietario = 0L;
		
		try {
    		control();
    		this.campoBusqueda = (this.campoBusqueda != null && ! "".equals(this.campoBusqueda.trim()) ? this.campoBusqueda.trim() : this.tiposBusqueda.get(0).getValue().toString());
    		this.valorBusqueda = (this.valorBusqueda != null && ! "".equals(this.valorBusqueda.trim()) ? this.valorBusqueda.trim() : "");
    		this.fechaBusqueda = (this.fechaBusqueda != null ? this.fechaBusqueda : Calendar.getInstance().getTime());

			idPropietario = (this.loginManager.getInfoSesion().getAcceso().getUsuario().getIdEmpleado() != null ? this.loginManager.getInfoSesion().getAcceso().getUsuario().getIdEmpleado() : 0L);
			idPropietario = ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()) ? 0L : idPropietario);
			idPropietario = 0L; // Desactivado, Quien tenga acceso a esta pantalla, puede ver todas las requisiciones
			
			controlLog("Buscando Requisiciones --> " + this.campoBusqueda + " : " + this.valorBusqueda);
			this.ifzRequisiciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listRequisiciones = this.ifzRequisiciones.findLikeProperty(this.campoBusqueda, ("fecha".equals(this.campoBusqueda) ? this.fechaBusqueda : this.valorBusqueda), 0L, this.tipoMaestroBusqueda, true, false, idPropietario, "CASE model.sistema WHEN 0 THEN 0 ELSE 1 END, model.fecha desc, model.id desc", 0);
    		this.listRequisiciones = (this.listRequisiciones != null ? this.listRequisiciones : new ArrayList<Requisicion>());
			if (this.listRequisiciones.isEmpty()) 
	    		control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar las Requisiciones", e);
    	} finally {
			this.numPagina = 1;
			controlLog("Requisiciones encontradas: " + this.listRequisiciones.size());
    	}
	}
	
	public void nuevo() {
		try {
    		control();
    		controlLog("Nueva Requisicion");
    		this.permiteEditar = true;
			this.pojoRequisicion = new RequisicionExt();
			this.pojoRequisicion.setFecha(Calendar.getInstance().getTime());
			this.pojoRequisicion.setIdPropietario(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			this.pojoRequisicion.setNombrePropietario(this.loginManager.getInfoSesion().getAcceso().getUsuario().getNombre());
			this.pojoProducto = null;
			this.valComprador = 0;
			this.idMoneda = this.idMonedaSugerida;
			this.listDetalles = new ArrayList<RequisicionDetalleExt>();
			this.listSolicitudes = new ArrayList<SolicitudBodega>();
			this.numPaginaProductos = 1;
			this.numPaginaSolicitudBodega = 1;
			
			if (! this.perfilEgresos)
				cargarFamilias();
			
			cargarSolicitantes();
			cargarMonedas();
			nuevaBusquedaObras();
			nuevaBusquedaProductos();
    	} catch (Exception e) {
    		control("Ocurrio un problema al inicializar una nueva Requisicion", e);
    	}
	}
	
	public void nuevoMaestroInventarios() {
		try {
			controlLog("Requisicion para Maestro de Inventarios (Insumos)");
			this.tipoMaestro = TipoMaestro.Inventario;
			cargarFamilias();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar las Familias del Maestro de Inventarios (Insumos)", e);
		}
	}
	
	public void nuevoMaestroAdminitrativo() {
		try {
			controlLog("Requisicion para Maestro Administrativo (Egresos de Operacion)");
			this.tipoMaestro = TipoMaestro.Administrativo;
			cargarFamilias();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar las Familias del Maestro Administrativo", e);
		}
	}
	
	public void editar() {
		try {
    		control();
    		this.pojoRequisicion = this.ifzRequisiciones.findExtById(this.idRequisicion);
    		if (this.pojoRequisicion == null || this.pojoRequisicion.getId() == null || this.pojoRequisicion.getId() <= 0L) {
    			control(-1, "No se pudo recuperar la Requisicion seleccionada");
    			return;
    		}

			// Cargamos los compradores
			cargarSolicitantes();
			// Cargamos los Monedas
			cargarMonedas();

			if (this.pojoRequisicion.getTipo() == TipoMaestro.Administrativo.ordinal())
				nuevoMaestroAdminitrativo();
			else
				nuevoMaestroInventarios();

    		this.permiteEditar = ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()) || this.pojoRequisicion.getCreadoPor() == this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.permiteEditar = ((this.pojoRequisicion.getEstatus() == 0) ? this.permiteEditar: false);
			this.valComprador = (this.pojoRequisicion.getIdSolicita() != null ? this.pojoRequisicion.getIdSolicita().getId() : 0L);
			this.idMoneda = (this.pojoRequisicion.getIdMoneda() > 0L ? this.pojoRequisicion.getIdMoneda() : this.idMonedaSugerida);
			
			// Recuperamos los detalles de la Requisicion
			this.listDetalles = this.ifzReqDetalles.findExtAll(this.pojoRequisicion.getId());
			this.listDetalles = (this.listDetalles != null ? this.listDetalles : new ArrayList<RequisicionDetalleExt>());
			this.listDetallesEliminados = new ArrayList<RequisicionDetalleExt>();
			this.numPaginaProductos = 1;
			
			// Recuperamos Solicitud a Bodega, previamente generada si corresponde
			this.listSolicitudes = this.ifzRequisiciones.solicitudBodega(this.pojoRequisicion.getId());
			this.listSolicitudes = (this.listSolicitudes != null ? this.listSolicitudes : new ArrayList<SolicitudBodega>());
			this.numPaginaSolicitudBodega = 1;
			
			nuevaBusquedaObras();
			nuevaBusquedaProductos();
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar la Requisicion seleccionada", e);
    	} 
	}
	
	public void guardar() {
		Requisicion requisicion = null;
		
		try {
    		control();
    		if (! validaciones())
    			return;

			this.listRequisiciones = (this.listRequisiciones != null ? this.listRequisiciones : new ArrayList<Requisicion>());
			if (this.pojoRequisicion.getId() == null || this.pojoRequisicion.getId() <= 0L) {
				// Guardamos en la BD
				controlLog("Guardando Requisicion.");
				this.pojoRequisicion.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoRequisicion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoRequisicion.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoRequisicion.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.ifzRequisiciones.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoRequisicion.setId(this.ifzRequisiciones.save(this.pojoRequisicion));
				requisicion = this.ifzRequisiciones.findById(this.pojoRequisicion.getId());
				
				// Añadimos a listado
				this.listRequisiciones.add(0, requisicion);
				controlLog("Requisicion guardada");
			} else {
				// Actualizamos en la BD
				controlLog("Actualizando Requisicion.");
				this.pojoRequisicion.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoRequisicion.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.ifzRequisiciones.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzRequisiciones.update(this.pojoRequisicion);
				requisicion = this.ifzRequisiciones.findById(this.pojoRequisicion.getId());
				
				// Buscamos y Actualizamos la cotizacion en el listado
				for (Requisicion var : this.listRequisiciones) {
					if (this.pojoRequisicion.getId().longValue() == var.getId().longValue()) {
						this.listRequisiciones.set(this.listRequisiciones.indexOf(var), requisicion);
						break;
					}
				}
				controlLog("Requisicion actualizada");
			}
			
			this.idRequisicion = requisicion.getId();
			
			// Guardamos los detalles nuevos y modificados
			if (this.listDetalles != null && ! this.listDetalles.isEmpty()) {
				controlLog("Guardando detalles de requisicion");
				// Asignamos la cotizacion a los detalles
				for (RequisicionDetalleExt var : this.listDetalles) {
					var.setIdRequisicion(this.pojoRequisicion);
					var.setCantidadInicial(var.getCantidad());
					if (var.getId() == null || var.getId() <= 0L) {
						var.setFechaCreacion(Calendar.getInstance().getTime());
						var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					}
					var.setFechaModificacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				}

				// Guardamos en la BD
				this.listDetalles = this.ifzReqDetalles.saveOrUpdateListExt(this.listDetalles);
				controlLog("Detalles de requisicion guardados");
			}

			// Borramos los detalles de la requisiciones si corresponde
			this.listDetallesEliminados = (this.listDetallesEliminados != null ? this.listDetallesEliminados : new ArrayList<RequisicionDetalleExt>());
			if (this.listDetallesEliminados != null && ! this.listDetallesEliminados.isEmpty()) {
				if (this.isDebug) controlLog("Borrando detalles de Requisicion previamente eliminados.");
				this.ifzReqDetalles.deleteAllExt(this.listDetallesEliminados);
				this.listDetallesEliminados.clear();
				controlLog("Detalles de Requisicion borrados");
			}
			
			// SE EJECUTA AL CONFIRMAR CERRAR LA REQUISICION
			/*this.pojoRequisicion = new RequisicionExt();
			this.pojoRequisicionBorrar = null;
			this.listReqDetalles.clear();*/
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar Guardar la Requisicion", e);
    	}
	}
	
	public void eliminar() {
		Respuesta respuesta = null;
		Requisicion requisicion = null;
		
		try {
			control();
			requisicion = this.ifzRequisiciones.findById(this.idRequisicion);
    		if (requisicion == null || requisicion.getId() == null || requisicion.getId() <= 0L) {
    			control(-1, "No se pudo recuperar la Requisicion seleccionada para Cancelar");
    			return;
    		}
			
			// Establecemos los atributos para la cancelacion
			this.ifzRequisiciones.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzRequisiciones.cancelar(requisicion.getId(), this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			if (respuesta != null && respuesta.getErrores().getCodigoError() != 0L) {
				log.warn(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError());
				return;
			}

			requisicion = (Requisicion) respuesta.getBody().getValor("requisicion");
			for (Requisicion var : this.listRequisiciones) {
				if (var.getId() == requisicion.getId()) {
					BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
					BeanUtils.copyProperties(var, requisicion);
					break;
				}
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar Cancelar la Requisicion seleccionada", e);
    	}
	}

	public void eliminarDetalle() {
		int index = -1;
		
		try {
    		control();
			if (this.pojoReqDetalleBorrar == null) {
	    		control(-1, "No se puede eliminar este producto de la Requisicion.\nNo se pudo recuperar el Producto asignado");
	    		return;
			}
			
			// Validamos si agregamos el detalle al listado de eliminados
			controlLog("Borrando detalle de requisicion");
			index = this.listDetalles.indexOf(this.pojoReqDetalleBorrar);
			if (this.pojoReqDetalleBorrar.getId() != null && this.pojoReqDetalleBorrar.getId() > 0L) {
				if (this.pojoReqDetalleBorrar.getCantidadOrdenCompra() > 0) {
		    		control(-1, "No se puede eliminar este producto de la Requisicion.\nEste producto esta asignado en al menos una Orden de Compra");
		    		return;
				}

				if (this.pojoReqDetalleBorrar.getCantidadSuministrada() > 0) {
		    		control(-1, "No se puede eliminar este producto de la Requisicion.\nEste producto ya cuenta con Suministro");
		    		return;
				}
				
				this.pojoReqDetalleBorrar.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoReqDetalleBorrar.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.listDetallesEliminados = (this.listDetallesEliminados != null ? this.listDetallesEliminados : new ArrayList<RequisicionDetalleExt>());
				this.listDetallesEliminados.add(this.pojoReqDetalleBorrar);
			}
			
			if (index >= 0)
				this.listDetalles.remove(index);
			this.pojoReqDetalleBorrar = null;
			controlLog("--- -- ---> Detalle de requisicion borrado");
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar Eliminar el concepto seleccionado", e);
    	}
	}

	public void reporte() {
		Respuesta respuesta = null;
		Requisicion requisicion = null;
		// ------------------------------------
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			requisicion = this.ifzRequisiciones.findById(this.idRequisicion);
    		if (requisicion == null || requisicion.getId() == null || requisicion.getId() <= 0L) {
    			control(-1, "No se pudo recuperar la Requisicion seleccionada para Imprimir");
    			return;
    		}
    		
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idRequisicion", requisicion.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("REPORTE_REQUISICION"));
			params.put("nombreDocumento", this.entornoProperties.getString("REPORTE_REQUISICION_NOMBRE"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());

			controlLog("REPORTE REQUISICION: Ejecutamos reporte");
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				controlLog(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
	    		control(-1, "Ocurrio un problema al intentar exportar la Requisicion.\n501 - No se pudo ejecutar la peticion");
				return;
			}
			
			// Recogemos reporte
			controlLog("REPORTE REQUISICION: Recuperamos contenido de reporte");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "REQ-" + requisicion.getId() + "." + formatoDocumento;
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("Error Interno - No se pudo obtener el reporte de cotizacion ID " + requisicion.getId());
	    		control(-1, "Error Interno - No se pudo recuperar el reporte de Cotizacion " + requisicion.getId());
				return;
			}

			controlLog("REPORTE REQUISICION: Lo guardo en SESSION");
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			controlLog("REPORTE REQUISICION: Terminado");
		} catch (Exception e) {
    		control("Ocurrio un problema al exportar la Requisicion\n500 - Error interno en el servidor", e);
		}
	}

	private List<Long> getPuestosCompradores(String value) {
		List<Long> puestos = null;
		String[] splitted = null;
		
		if (value == null || "".equals(value.trim()))
			return new ArrayList<Long>();
		
		puestos = new ArrayList<Long>();
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
	
	private void cargarSolicitantes() {
		try {
			// Cargamos la lista de compradores
			if (this.isDebug) controlLog("Cargando lista de solicitantes (Empleados).");
			this.listEmpleadosItems = new ArrayList<SelectItem>();
			this.idEmpleadosCompradores = new ArrayList<Long>();
			
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.listEmpleados = this.ifzEmpleados.findAllPuestoExt(this.idPuestoCompradores);
			this.listEmpleados = (this.listEmpleados != null ? this.listEmpleados : new ArrayList<EmpleadoExt>());
			
			if (this.isDebug) controlLog("Generando lista de items (Solicitantes)");
			for (EmpleadoExt var : this.listEmpleados) {
				this.listEmpleadosItems.add(new SelectItem(var.getId(), var.getNombre()));
				this.idEmpleadosCompradores.add(var.getId());
			}
		} catch (Exception e) {
			log.error("Error al cargar compradores", e);
		}
	}
	
	private void cargarUnidadesMedida() {
		try {
			// Cargamos la lista de familias
			controlLog("Cargando lista de familias");
			this.listUnidadesMedidaItems = new ArrayList<SelectItem>();
			this.listUnidadesMedida = this.ifzConValores.buscaValorGrupo("descripcion", "", this.pojoGpoUnidadesMedida);
			this.listUnidadesMedida = (this.listUnidadesMedida != null ? this.listUnidadesMedida : new ArrayList<ConValores>());
			
			// Generamos la lista auxiliar de familias
			controlLog("Generando lista de items (UnidadesMedida)");
			for (ConValores var : this.listUnidadesMedida) {
				if (var.getValor().trim().equals(var.getDescripcion().trim()))
					this.listUnidadesMedidaItems.add(new SelectItem(var.getId(), var.getDescripcion()));
				else
					this.listUnidadesMedidaItems.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
			}
		} catch (Exception e) {
			log.error("Error al cargar las UnidadesMedida", e);
		} finally {
			if (this.listUnidadesMedidaItems != null && ! this.listUnidadesMedidaItems.isEmpty()) 
				controlLog(this.listUnidadesMedidaItems.size() + " items (UnidadesMedida) generados");
		}
	}
	
	private void cargarFamilias() {
		HashMap<String, Object> params = null;
		List<ConValores> listFamAux = null;
		List<ConValores> listAux = null;
		String valTipoMaestro = "";
		String idMaestro = "";
		
		try {
			this.listFamiliasItems = new ArrayList<SelectItem>();
			
			controlLog("Cargando Maestros");
			valTipoMaestro = String.valueOf(this.tipoMaestro.ordinal());
			listAux = this.ifzConValores.findAll(this.pojoGpoMaestros);
			listAux = (listAux != null ? listAux : new ArrayList<ConValores>());
			for (ConValores var : listAux) {
				if (! var.getValor().equals(valTipoMaestro)) 
					continue;
				idMaestro = String.valueOf(var.getId());
				break;
			}

			controlLog("Cargando Especialidades del Maestro " + this.tipoMaestro.toString());
			params = new HashMap<String, Object>();
			params.put("grupoValorId.id", this.pojoGpoEspecialidades.getId());
			params.put("atributo1", idMaestro);
			this.listEspecialidades = this.ifzConValores.findByProperties(params, 0);
			this.listEspecialidades = (this.listEspecialidades != null ? this.listEspecialidades : new ArrayList<ConValores>());
			
			this.listFamilias = new ArrayList<ConValores>();
			for (ConValores var : this.listEspecialidades) {
				params.put("grupoValorId.id", this.pojoGpoFamilias.getId());
				params.put("atributo1", String.valueOf(var.getId()));
				listFamAux = this.ifzConValores.findByProperties(params, 0);
				if (listFamAux == null || listFamAux.isEmpty())
					continue;
				this.listFamilias.addAll(listFamAux);
			}

			Collections.sort(this.listFamilias, new Comparator<ConValores>() {
			    	@Override
			        public int compare(ConValores o1, ConValores o2) {
			    		return o1.getDescripcion().compareTo(o2.getDescripcion());
			        }
			});
			
			// Generamos la lista auxiliar de familias
			controlLog("Generando lista de items (familias)");
			for (ConValores var : this.listFamilias) {
				if (var.getValor().trim().equals(var.getDescripcion().trim()))
					this.listFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion()));
				else
					this.listFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al cargar las Familias del Maestro indicado", e);
		} finally {
			if (this.listFamiliasItems != null && ! this.listFamiliasItems.isEmpty()) 
				controlLog(this.listFamiliasItems.size() + " items (familias) generados");
		}
	}

	private void cargarMonedas() {
		try {
			// Cargamos la lista de monedas
			if (this.isDebug) controlLog("Cargando lista de monedas");
			this.listMonedasItems = new ArrayList<SelectItem>();
			this.listMonedas = this.ifzMonedas.findAll();
			this.listMonedas = (this.listMonedas != null ? this.listMonedas : new ArrayList<Moneda>());
			if (this.isDebug) controlLog("Generando lista de items (monedas)");
			for (Moneda var : this.listMonedas) 
				this.listMonedasItems.add(new SelectItem(var.getId(), var.getNombre() + " (" + var.getAbreviacion() + ")"));
		} catch (Exception e) {
			log.error("Error al cargar las Monedas", e);
		}
	}
	
	private boolean validaciones() {
		EmpleadoExt comprador = null;
		
		try {
			if (this.valComprador <= 0L) {
	    		control(5, "Debe indicar un Comprador");
				return false;
			}
			
			for (EmpleadoExt empleado : this.listEmpleados) {
				if (this.valComprador == empleado.getId().longValue()) {
					comprador = empleado;
					break;
				}
			}
			
			if (comprador == null) {
	    		control(-1, "Ocurrio un problema al recuperar al Comprador seleccionado");
				return false;
			}

			this.listDetalles = (this.listDetalles != null ? this.listDetalles : new ArrayList<RequisicionDetalleExt>());
			if (this.listDetalles.isEmpty()) {
	    		control(-1, "Debe guardar la Requisicion con al menos un Producto");
				return false;
			}
	
			this.idMoneda = (this.idMoneda <= 0L ? this.loginManager.getInfoSesion().getEmpresa().getMonedaId() : this.idMoneda);
			this.pojoRequisicion.setTipo(this.tipoMaestro.ordinal());
			this.pojoRequisicion.setIdMoneda(this.idMoneda);
			this.pojoRequisicion.setIdSolicita(comprador);
			this.pojoRequisicion.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
		} catch (Exception e) {
    		control("Ocurrio un problema al intentar validar los datos de la Requisicion", e);
			return false;
		}
		
		return true;
	}

	public void buscarGuardar() {
		List<Requisicion> reqs = new ArrayList<Requisicion>();
		List<Cotizacion> cots = new ArrayList<Cotizacion>();
		List<OrdenCompra> ors = new ArrayList<OrdenCompra>();
		HashMap<Long, Long> convertidos = new HashMap<Long, Long>();
		Producto producto = null;
		long idProductoOld = 0;
		long idProductoNew = 0;
		
		try {
			control();
			controlLog("Proceso para copiar producto de las requisiciones de la Obra CHAMETLA a MAESTRO ADMINISTRATIVO");
			reqs = this.ifzRequisiciones.findByProperty("idObra", 10000090L, 0);
			if (reqs == null || reqs.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}

			controlLog("Se encontraron " + reqs.size() + " requisiciones");
			for (Requisicion r : reqs) {
				List<RequisicionDetalle> det = this.ifzReqDetalles.findByProperty("idRequisicion", r.getId(), 0);
				if (det == null || det.isEmpty()) {
					controlLog("Sin detalles en requisicion " + r.getId());
					continue;
				}
				
				controlLog("Se encontraron " + reqs.size() + " detalles en requisicion " + r.getId());
				for (RequisicionDetalle d : det) {
					idProductoOld = d.getIdProducto();
					if (! convertidos.containsKey(idProductoOld)) {
						producto = this.ifzProductos.findById(d.getIdProducto());
						producto.setId(0L);
						producto.setClave("");
						producto.setEspecialidad(10002580l);
						producto.setDescEspecialidad("ACTIVOS");
						producto.setFamilia(10002595l);
						producto.setDescFamilia("OBRA EN PROCESO");
						producto.setSubfamilia(10002634l);
						producto.setDescSubfamilia("CHAMETLA");
						producto.setTipo(TipoMaestro.Administrativo.ordinal());
						producto.setFechaCreacion(Calendar.getInstance().getTime());
						producto.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						producto.setFechaModificacion(Calendar.getInstance().getTime());
						producto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						producto.setId(this.ifzProductos.save(producto));
						idProductoNew = producto.getId();
						convertidos.put(idProductoOld, idProductoNew);
					} 

					d.setIdProducto(convertidos.get(idProductoOld));
					d.setFechaModificacion(Calendar.getInstance().getTime());
					d.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					this.ifzReqDetalles.update(d);
				}
			}

			for (Requisicion r : reqs) {
				cots = this.ifzCotizaciones.findByProperty("idRequisicion", r.getId(), 0);
				if (cots == null || cots.isEmpty()) {
					controlLog("Sin cotizaciones con la requisicion " + r.getId());
					continue;
				}
				
				controlLog("Se encontraron " + reqs.size() + " cotizaciones con la requisicion " + r.getId());
				for (Cotizacion c : cots) {
					List<CotizacionDetalle> det = this.ifzCotiDetalles.findByProperty("idCotizacion", c.getId(), 0);
					if (det == null || det.isEmpty()) {
						controlLog("Sin detalles en cotizacion " + c.getId());
						continue;
					}
					
					for (CotizacionDetalle d : det) {
						if (convertidos.containsKey(d.getIdProducto())) {
							d.setIdProducto(convertidos.get(d.getIdProducto()));
							d.setFechaModificacion(Calendar.getInstance().getTime());
							d.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
							this.ifzCotiDetalles.update(d);
						}
					}
				}
			}
			
			for (Cotizacion c : cots) {
				ors = this.ifzOrdenes.findByProperty("idCotizacion", c.getId(), 0);
				if (ors == null || ors.isEmpty()) {
					controlLog("Sin ordenes de compra con la cotizacion " + c.getId());
					continue;
				}
				
				controlLog("Se encontraron " + ors.size() + " ordenes de compra con la cotizacion " + c.getId());
				for (OrdenCompra o : ors) {
					List<OrdenCompraDetalle> det = this.ifzOrdenDetalles.findByProperty("idCotizacion", o.getId(), 0);
					if (det == null || det.isEmpty()) {
						controlLog("Sin detalles en orden de compra " + o.getId());
						continue;
					}
					
					for (OrdenCompraDetalle d : det) {
						if (convertidos.containsKey(d.getIdProducto())) {
							d.setIdProducto(convertidos.get(d.getIdProducto()));
							d.setFechaModificacion(Calendar.getInstance().getTime());
							d.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
							this.ifzOrdenDetalles.update(d);
						}
					}
				}
			}

			controlLog("Proceso terminado");
			//control(true);
		} catch (Exception e) {
			control("Ocurrio un problema con el Proceso BuscarGuardar :(. Convertidos: " + convertidos, e);
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

	private void control(String value, Throwable throwable) { 
		control(true, -1, value, throwable); 
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
	
    // --------------------------------------------------------------------------------------
    // Busqueda Obras
    // --------------------------------------------------------------------------------------
	
	public void nuevaBusquedaObras() {
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.numPaginaObras = 1;
		this.valorBusquedaTipoObra = (int) this.tiposObras.get(0).getValue();

		if (this.tipoMaestro == TipoMaestro.Administrativo) {
			if (this.tiposObras.size() <= 4)
				this.tiposObras.add(new SelectItem(4, "Administrativa"));
			this.valorBusquedaTipoObra = 4;
		} else {
			if (this.tiposObras.size() > 4)
				this.tiposObras.remove(4);
		}
		
		this.pojoObra = null;
		if (this.listObras != null)
			this.listObras.clear();
    }
	
	public void buscarObras() {
		String valor = "";
		
		try {
    		control();
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
			
			if (this.listObras != null) 
				this.listObras.clear();
			
			controlLog("Buscando Obras. " + this.campoBusquedaObras + " - " + this.valorBusquedaObras);
			if (this.valorBusquedaObras == null)
				this.valorBusquedaObras = "";
			valor = this.valorBusquedaObras.replace(" ", "%");
			this.numPaginaObras = 1;
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObras, valor, this.valorBusquedaTipoObra, false, "", 0);
			if (this.listObras == null || this.listObras.isEmpty()){
				if (this.isDebug) controlLog("Buscando Obras. ERROR 2: Busqueda sin resultados");
	    		control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
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
			
			this.pojoRequisicion.setIdObra(this.ifzObras.convertir(this.pojoObra));
			this.pojoRequisicion.setIdMoneda(this.pojoObra.getIdMoneda());
			this.idMoneda = this.pojoObra.getIdMoneda();
			nuevaBusquedaObras();
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar la Obra seleccionada", e);
    	}
	}

	public void toggleAvanzadaBusquedaObras() {
		this.avanzadaBusquedaObras = ! this.avanzadaBusquedaObras;
	}
	
    // --------------------------------------------------------------------------------------
	// Busqueda Solicitantes (Empleados)
    // --------------------------------------------------------------------------------------
	
	/*public void nuevaBusquedaSolicitante() {
		try {
    		control();
    		this.pojoSolicitante = null;
			this.campoBusquedaSolicitantes = this.tiposBusquedaSolicitantes.get(0).getValue().toString();
			this.valorBusquedaSolicitantes = "";
			this.numPaginaSolicitantes = 1;
			
			if (this.listSolicitantes != null)
				this.listSolicitantes.clear();
    	} catch (Exception e) {
    		control("Ocurrio un problema al inicializar la busqueda de Solicitantes", e);
    	}
	}
	
	public void buscarSolicitantes() {
		String valor = "";
		
		try {
    		control();
			if ("".equals(this.campoBusquedaSolicitantes))
				this.campoBusquedaSolicitantes = this.tiposBusquedaSolicitantes.get(0).getValue().toString();

			this.numPaginaSolicitantes = 1;
			if (this.listSolicitantes != null)
				this.listSolicitantes.clear();
			
			controlLog("Buscando Solicitantes. " + this.campoBusquedaSolicitantes + ": " + this.valorBusquedaSolicitantes);
			if (this.valorBusquedaSolicitantes == null)
				this.valorBusquedaSolicitantes = "";
			valor = this.valorBusquedaSolicitantes.replace(" ", "%");
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSolicitantes = this.ifzEmpleados.findLikeProperty(this.campoBusquedaSolicitantes, valor, 0);
			if(this.listSolicitantes == null || this.listSolicitantes.isEmpty()){
				controlLog("Buscando Solicitantes. ERROR 2: Busqueda sin resultados");
	    		control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar los Solicitantes", e);
    	}
	}
	
	public void seleccionarSolicitante() {
		try {
    		control();
    		if (this.pojoRequisicion != null) {
    			controlLog("Extendemos el solicitante (Empleado) seleccionado y lo asignamos a la Requisicion");
    			this.pojoRequisicion.setIdSolicita(this.ifzEmpleados.convertir(this.pojoSolicitante));
    		}
    		nuevaBusquedaSolicitante();
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar el Solicitante seleccionado", e);
    	}
	}*/

    // --------------------------------------------------------------------------------------
    // Busqueda Productos
    // --------------------------------------------------------------------------------------
	
	public void nuevaBusquedaProductos() {
		control();
		this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
		this.valorBusquedaProductos = "";
		this.familiaBusquedaProductos = 0;
		this.numPaginaProductos = 1;
		if (this.listProductos != null)
			this.listProductos.clear();
		this.pojoProductoSeleccionado = null;
		this.previaBusquedaProducto = false;
		this.pojoProductoNuevo = new Producto();
		this.pojoProductoNuevo.setIdMoneda(this.idMoneda);
		nuevoReqDetalle();
    }
	
	public void buscarProductos() {
		String valor = "";
		
		try {
    		control();
			if ("".equals(this.campoBusquedaProductos))
				this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();

			if (this.familiaBusquedaProductos <= 0L)
				this.familiaBusquedaProductos = 0L;

			this.previaBusquedaProducto = false;
			this.numPaginaProductos = 1;
			if (this.listProductos == null)
				this.listProductos = new ArrayList<Producto>();
			this.listProductos.clear();

			// Parametros de busqueda
			controlLog("Buscando Productos - " + this.campoBusquedaProductos + ": " + this.valorBusquedaProductos);
			if (this.valorBusquedaProductos == null)
				this.valorBusquedaProductos = "";
			valor = this.valorBusquedaProductos.replace(" ", "%");
			this.listProductos = this.ifzProductos.findLikeProperty(this.campoBusquedaProductos, valor, this.familiaBusquedaProductos, this.tipoMaestro.ordinal(), "", 0); // .findLikeProperties(params, null, 0, this.campoBusquedaProductos);
			if(this.listProductos.isEmpty()) {
				this.previaBusquedaProducto = true;
				controlLog("Buscando Productos. ERROR 2: Busqueda sin resultados");
	    		control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar los Productos", e);
    	}
	}
	
	public void seleccionarProducto() throws Exception {
		try {
    		control();
    		if (this.pojoProductoSeleccionado == null || this.pojoProductoSeleccionado.getId() == null || this.pojoProductoSeleccionado.getId() <= 0L) {
    			control(-1, "Debe seleccionar un Producto");
    			return;
    		}

    		controlLog("Extendemos producto seleccionado");
			this.pojoProducto = this.ifzProductos.convertir(this.pojoProductoSeleccionado);
			agregarReqDetalle();
			if (this.operacion)
				return;
			
			this.previaBusquedaProducto = false;
			control(6, "Producto agregado");
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar el Producto seleccionado", e);
    	}
	}

    // --------------------------------------------------------------------------------------
    // Agregar DETALLE
    // --------------------------------------------------------------------------------------
	
	public void nuevoReqDetalle() {
		this.pojoReqDetalle = null;
		this.pojoProducto = null;
		this.cantidadProducto = 1;
	}
	
	public void agregarReqDetalle() {
		boolean existe = false;
		
		try {
    		control();
			if (this.pojoProducto == null || this.pojoProducto.getId() == null || this.pojoProducto.getId() <= 0L) {
	    		control(5, "Debe seleccionar un Producto");
				return;
			}
			
			if (this.cantidadProducto <= 0) {
	    		control(4, "La Cantidad debe ser mayor a cero");
				return;
			}
			
			// Generamos el detalle de la requisicion
			controlLog("Agregando detalle a requisicion");
			this.pojoReqDetalle = new RequisicionDetalleExt();
			this.pojoReqDetalle.setIdProducto(this.pojoProducto);
			this.pojoReqDetalle.setCantidad(this.cantidadProducto);
			this.pojoReqDetalle.setCantidadInicial(this.cantidadProducto);
			
			// Agregamos el detalle al listado
			this.listDetalles = (this.listDetalles != null ? this.listDetalles : new ArrayList<RequisicionDetalleExt>());
			for (RequisicionDetalleExt var : this.listDetalles) {
				if (var.getIdProducto().getId().equals(this.pojoReqDetalle.getIdProducto().getId())) {
					var.setCantidad(var.getCantidad() + this.pojoReqDetalle.getCantidad());
					existe = true;
					break;
				}
			}
			
			if (! existe)
				this.listDetalles.add(this.pojoReqDetalle);
			controlLog("--- -- ---> Detalle agregado");
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar añadir el Producto indicado", e);
    	}
	}
	
    // --------------------------------------------------------------------------------------
    // Nuevo Producto
    // --------------------------------------------------------------------------------------
	
	public void nuevoProducto() {
		try {
			control();
			this.pojoProductoNuevo = new Producto();
			this.pojoProductoNuevo.setIdMoneda(this.idMoneda);
			
			if (this.tipoMaestro == null || this.tipoMaestro == TipoMaestro.Ninguno) {
				this.tipoMaestro = TipoMaestro.Inventario;
				if (TiposObra.Administrativa.ordinal() == this.pojoObra.getTipoObra())
					this.tipoMaestro = TipoMaestro.Administrativo;
			}
			
			if (this.tipoMaestro == TipoMaestro.Inventario)
				this.pojoProductoNuevo.setClave(generaClave());
			
			if (this.listFamiliasItems == null || this.listFamiliasItems.isEmpty())
				cargarFamilias();
			cargarUnidadesMedida();
		} catch (Exception e) {
			control("Ocurrio un problema al inicializar nuevo Producto", e);
		}
	}
	
	public void guardarProducto() {
		try {
			control();
			if (! validaGuardarProducto()) 
				return;

			// Obtenemos descripcion y abreviatura de moneda elegida
			if (this.pojoProductoNuevo.getIdMoneda() > 0L) {
				for (Moneda var : this.listMonedas) {
					if (var.getId() != this.pojoProductoNuevo.getIdMoneda())
						continue;
					this.pojoProductoNuevo.setDescMoneda(var.getNombre());
					this.pojoProductoNuevo.setDescMonedaAbreviatura(var.getAbreviacion());
					break;
				}
			}

			// Asignamos descripciones de ConValores: Unidad de Medida, Especialidad, Familia
			this.pojoProductoNuevo.setDescUnidadMedida(getDescripcionFromList(this.listUnidadesMedida, this.pojoProductoNuevo.getUnidadMedida()));
			this.pojoProductoNuevo.setDescEspecialidad(getDescripcionFromList(this.listEspecialidades, this.pojoProductoNuevo.getEspecialidad()));
			this.pojoProductoNuevo.setDescFamilia(getDescripcionFromList(this.listFamilias, this.pojoProductoNuevo.getFamilia()));
			// Asigno el tipo de maestro y tipo de insumo
			this.pojoProductoNuevo.setTipo(this.tipoMaestro.ordinal());
			this.pojoProductoNuevo.setTipoInsumo(TipoInsumo.Material.ordinal());
			// Asignamos tipo de cambio si corresponde
			if (this.pojoProductoNuevo.getIdMoneda() > 0L && this.pojoProductoNuevo.getPrecioCompraPesos() <= 0)
				asignaTipoCambio();
    		this.pojoProductoNuevo.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			// Asigno valores de auditoria
			this.pojoProductoNuevo.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoProductoNuevo.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoProductoNuevo.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoProductoNuevo.setFechaModificacion(Calendar.getInstance().getTime());
			
			// Guardo el producto en la BD
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			this.pojoProductoNuevo.setId(this.ifzProductos.save(this.pojoProductoNuevo));

			// Guardamos o actualizamos copia en Gastos si corresponde
			if ("ACTIVOS".equals(this.pojoProductoNuevo.getDescEspecialidad().trim().toUpperCase()))
				copiaActualizaEnGastos(this.pojoProductoNuevo);

			controlLog("Extendemos producto nuevo seleccionado");
			this.pojoProducto = this.ifzProductos.convertir(this.pojoProductoNuevo);
			this.pojoProductoNuevo = new Producto();
			this.previaBusquedaProducto = false;

			agregarReqDetalle();
			if (this.operacion)
				return;
			nuevaBusquedaProductos();
			control(false, 6, "Producto agregado", null);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar el nuevo Producto", e);
		}
	}
	
	private void copiaActualizaEnGastos(Producto pojoProducto) {
		List<ConValores> lista = null;
		ConValores pojoGasto = null;
	
		try {
			if (pojoProducto.getPrecioCompra() < this.limiteInferiorActivos)
				return;
			
			lista = this.ifzConValores.buscaValorGrupo("valor", String.valueOf(pojoProducto.getId()), this.grupoGastos); 
			if (lista != null && ! lista.isEmpty()) {
				pojoGasto = lista.get(0);
				
				if (this.idEspecialidadActivo == pojoProducto.getEspecialidad()) { 
					controlLog("  --- Actualizando copia en Gasto");
					pojoGasto.setDescripcion(pojoProducto.getDescripcion());
					pojoGasto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());
					// Actualizo en la BD
					this.ifzConValores.update(pojoGasto);
					controlLog("  --- Copia en Gasto actualizado");
				} else if (this.idEspecialidadActivo != pojoProducto.getEspecialidad()) {
					controlLog("  --- Eliminando copia en Gasto");
					this.ifzConValores.delete(pojoGasto);
					controlLog("  --- Copia en Gasto Eliminada");
				}
			} else {
				if (pojoProducto.getEspecialidad() != this.idEspecialidadActivo) 
					return; 
				
				controlLog("  --- Generando copia en Gasto");
				pojoGasto = new ConValores();
				pojoGasto.setValor(String.valueOf(pojoProducto.getId()));
				pojoGasto.setDescripcion(pojoProducto.getDescripcion());
				pojoGasto.setGrupoValorId(this.grupoGastos);
				pojoGasto.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				pojoGasto.setFechaCreacion(Calendar.getInstance().getTime());
				pojoGasto.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());
				// Guardo en la BD
				pojoGasto.setId(this.ifzConValores.save(pojoGasto));
				controlLog("  --- Gasto generado");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al copiar el Producto al catalogo de Gastos: Producto " + pojoProducto.getId(), e);
		}
	}
	
	private boolean validaGuardarProducto() {
		int intento = 0;
		int maxIntentosClave = 3;
		boolean claveRepetida = false;
		this.tipoMensaje = 0;

		controlLog("Validando producto");
		if (this.pojoProductoNuevo.getClave().trim().equals("")) {
			controlLog("---> Debe indicar la clave del producto");
			this.tipoMensaje = -10;
			return false;
		}
		
		if (this.pojoProductoNuevo.getDescripcion().trim().equals("")) {
			controlLog("---> Debe indicar la descripcion del producto");
			this.tipoMensaje = -11;
			return false;
		}
		
		if (this.pojoProductoNuevo.getUnidadMedida() <= 0)  {
			controlLog("---> Producto sin unidad de medida");
			this.tipoMensaje = -19;
			return false;
		}
		
		if (this.pojoProductoNuevo.getFamilia() <= 0)  {
			controlLog("---> Producto sin familia");
			this.tipoMensaje = -20;
			return false;
		}
		
		if (this.pojoProductoNuevo.getExistencia() < 0) {
			controlLog("---> La existensia debe ser mayor o igual a cero");
			this.tipoMensaje = -16;
			return false;
		}

		if (this.pojoProductoNuevo.getMinimo() < 0) {
			controlLog("---> El Minimo debe ser mayor o igual a cero");
			this.tipoMensaje = -12;
			return false;
		}

		if (this.pojoProductoNuevo.getMaximo() < 0) {
			controlLog("---> El Maximo debe ser mayor o igual a cero");
			this.tipoMensaje = -13;
			return false;
		}
		
		if (this.pojoProductoNuevo.getMinimo() > this.pojoProductoNuevo.getMaximo()) {
			controlLog("---> El Minimo debe ser menor al Maximo");
			this.tipoMensaje = -17;
			return false;
		}

		if (this.pojoProductoNuevo.getIdMoneda() <= 0) {
			controlLog("---> Debe indicar la moneda");
			this.tipoMensaje = -21;
			return false;
		}
		
		/*if (this.pojoProductoNuevo.getPrecioCompra() == 0) {
			this.tipoMensaje = -14;
			controlLog("---> Debe indicar el precio de compra");
			return false;
		}

		if (this.pojoProductoNuevo.getPrecioVenta() == 0) {
			controlLog("---> Debe indicar el precio de venta");
			this.tipoMensaje = -15;
			return false;
		}*/
		
		// Validar la clave: Generamos la clave las veces que sea necesario
		do {
			intento += 1;
			controlLog("---> Valido clave de producto : " + this.pojoProductoNuevo.getClave());
			claveRepetida = this.ifzProductos.validarClaveProducto(this.pojoProductoNuevo);
			if (claveRepetida) 
				this.pojoProductoNuevo.setClave(generaClave());
		} while(claveRepetida && intento <= maxIntentosClave);
		
		if (claveRepetida) {
			controlLog("---> Producto clave repetida");
			this.tipoMensaje = -18;
			return false;
		}

		controlLog("Producto validado");
		return true;
	}
	
	private void asignaTipoCambio() {
		Moneda monedaOrigen = null;
		Moneda monedaDestino = null;
		Double tipoCambio = 1.0;
		
		if (this.pojoProductoNuevo == null)
			return;
		
		if (this.pojoProductoNuevo.getIdMoneda() > 0L) {
			// Origen: MXN
			for (Moneda var : this.listMonedas) {
				if (! "MXN".equals(var.getAbreviacion())) continue;
				monedaOrigen = var;
				break;
			}
			
			// Destino: Elegido
			for (Moneda var : this.listMonedas) {
				if (var.getId() != this.pojoProductoNuevo.getIdMoneda()) continue;
				monedaDestino = var;
				break;
			}
			
			if (monedaOrigen.getId() == monedaDestino.getId()) {
				monedaOrigen = null;
				monedaDestino = null;
			}
			
			tipoCambio = recuperaTipoCambioActual(monedaOrigen, monedaDestino);
			if (tipoCambio == null || tipoCambio <= 0.0) {
				log.warn("No se pudo recuperar el tipo de cambio para " + monedaDestino + " a " + monedaOrigen);
				tipoCambio = 1.0;
			}
			
			this.pojoProductoNuevo.setTipoCambio(tipoCambio); 
			this.pojoProductoNuevo.setPrecioCompraPesos(this.pojoProductoNuevo.getPrecioCompra() * tipoCambio);
		}
	}
	
	private String getDescripcionFromList(List<ConValores> lista, long id) {
		String resultado = "";
		
		try {
			if (lista == null || lista.isEmpty()) {
				controlLog("La lista indicada es nula o vacia.");
				return "";
			}
			
			if (id <= 0) {
				controlLog("No indico el ID para poder obtener el valor de la lista.");
				return "";
			}
			
			for (ConValores var : lista) {
				if (var.getId() == id) {
					resultado = var.getDescripcion();
					break;
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el valor de la lista especificada", e);
			resultado = "";
		}
		
		return resultado;
	}
	
	private Double recuperaTipoCambioActual(Moneda monedaOrigen, Moneda monedaDestino) {
		MonedasValores valor = null;
		Double tipoCambio = 1.0;

		try {
			if (monedaOrigen != null && monedaDestino != null) {
				valor = this.ifzMonValores.findActual(monedaOrigen, monedaDestino);
				tipoCambio = valor.getValor().doubleValue();
			}
		} catch (Exception e) {
			log.error("No se pudo obtener el Tipo de Cambio actual para la conversion de " + monedaOrigen + " a " + monedaDestino, e);
			tipoCambio = 1.0;
		}
		
		return tipoCambio;
	}
	
	private String generaClave() {
		String resultado = "";
		int consecutivo = 0;
		
		/*if (! this.usuario.equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) {
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
		}*/
		
		resultado = "IZSYS";
		consecutivo = getConsecutivo(resultado);
		resultado = resultado + String.format("%1$05d", consecutivo);
		
		return resultado;
	}
	
	public void generaCodigoProducto() {
		String codigo = "";
		String origenCodigo = "";
		String valor = "";
		int maxDigitos = 5;
		int consecutivo = 0;
		
		try {
			control();
			codigo = "";
			origenCodigo = "";

			if (this.tipoMaestro == TipoMaestro.Inventario)
				return;

			controlLog("Obteniendo especialidad ... ");
			this.pojoProductoNuevo.setEspecialidad(getEspecialidadFromFamilia(this.pojoProductoNuevo.getFamilia()));
			this.pojoProductoNuevo.setSubfamilia(0L);
			
			controlLog("Codificando producto ... ");
			valor = getValorFromList(this.listEspecialidades, this.pojoProductoNuevo.getEspecialidad(), false);
			codigo += (valor != null && ! "".equals(valor)) ? valor : "";
			if (valor == null || "".equals(valor)) {
				controlLog("Codificando producto nuevo ... No tengo especialidad :(");
				return;
			}
			
			valor = getValorFromList(this.listFamilias, this.pojoProductoNuevo.getFamilia(), false);
			codigo += (valor != null && ! "".equals(valor)) ? valor : "";
			if (valor == null || "".equals(valor)) {
				controlLog("Codificando producto nuevo ... No tengo familia :(");
				return;
			}
			
			consecutivo = getConsecutivo(codigo);
			codigo = codigo + String.format("%1$0" + maxDigitos + "d", consecutivo);
			origenCodigo = this.pojoProductoNuevo.getEspecialidad() + "-" + this.pojoProductoNuevo.getFamilia() + "-0";
			controlLog("Codificando producto nuevo ... terminado :) " + codigo + " :: " + origenCodigo);
		} catch (Exception e) {
			log.error("Error en Invetarios.ProductoAction.generaCodigoProducto: No pude codificar, genero codigo caduco", e);
			codigo = this.pojoProducto.getClave();
			if (codigo == null || "".equals(codigo) || ! codigo.startsWith("IZ"))
				codigo = generaClave();
			origenCodigo = "";
			controlLog("Producto con codigo caduco ... terminado :( " + codigo);
		}

		// Asignamos valores
		this.pojoProductoNuevo.setClave(codigo);
		this.pojoProductoNuevo.setOrigenCodigo(origenCodigo);
	}
	
	private String getValorFromList(List<ConValores> lista, long id, boolean validaContraDescripcion) {
		String resultado = "";
		String descripcion = "";
		
		try {
			if (lista == null || lista.isEmpty()) {
				controlLog("La lista indicada es nula o vacia.");
				return "";
			}
			
			if (id <= 0){
				controlLog("No indico el ID para poder obtener el valor de la lista.");
				return "";
			}
			
			for (ConValores var : lista) {
				if (var.getId() == id) {
					resultado = var.getValor();
					descripcion = var.getDescripcion();
					if (validaContraDescripcion) {
						if (resultado.trim().equals(descripcion.trim()))
							resultado = "";
					}
					break;
				}
			}
		} catch(Exception e) {
			log.error("Ocurrio un problema al intentar recuperar el valor de la lista especificada", e);
			resultado = "";
		}
		
		return resultado;
	}

	private int getConsecutivo(String prefix) {
		BigDecimal aux = BigDecimal.ZERO;
		int maximo = 1;
		
		List<Producto> lista = this.ifzProductos.findLikeProperty("clave", prefix, 0);
		if (lista != null && ! lista.isEmpty()) {
			for (Producto var : lista) {
				aux = new BigDecimal(var.getClave().substring(prefix.length()).replaceAll("[^0-9]", ""));
				if (maximo < aux.intValue())
					maximo = aux.intValue();
			}
			
			maximo += 1;
		}
		
		return maximo;
	}
	
	private long getEspecialidadFromFamilia(long idFamilia) {
		ConValores valFamilia = null;
		long idEspecialidad = 0;
		
		try {
			valFamilia = this.ifzConValores.findById(idFamilia);
			if (valFamilia != null && valFamilia.getAtributo1() != null && ! "".equals(valFamilia.getAtributo1()))
				idEspecialidad = Long.parseLong(valFamilia.getAtributo1());
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar obtener la Especialidad a partir de la familia " + idFamilia, e);
		}
		
		return idEspecialidad;
	}

    // --------------------------------------------------------------------------------------
    // ESTATUS
    // --------------------------------------------------------------------------------------
	
	public void estatusCotizaciones() {
		
	}
	
	public void estatusOrdenes() {
		
	}
	
    // --------------------------------------------------------------------------------------
    // Propiedades
    // --------------------------------------------------------------------------------------
	
	public boolean getHasId() {
		return (this.pojoRequisicion != null && this.pojoRequisicion.getId() != null && this.pojoRequisicion.getId() > 0L);
	}
	
	public void setHasId(boolean value) {}

	public String getTitulo() {
		if (getHasId())
			return "Requisicion " + this.pojoRequisicion.getId() + (this.tipoMaestro == TipoMaestro.Administrativo ? " [ADMINISTRATIVA]" : "");
		return "Nueva Requisicion " + (this.tipoMaestro == TipoMaestro.Administrativo ? "[ADMINISTRATIVA]" : "");
	}
	
	public void setTitulo(String value) {}
	
 	public boolean getEsAdministrativo() {
		if (this.tipoMaestro != null && this.tipoMaestro == TipoMaestro.Administrativo)
			return true;
		return false;
	}
	
	public void setEsAdministrativo(boolean value) {}

	public boolean getDetalleEditable() {
		if (this.pojoRequisicion != null && this.pojoRequisicion.getId() != null && this.pojoRequisicion.getId() > 0L)
			return ("ADMINISTRADOR JAVIITR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()) || this.pojoRequisicion.getCreadoPor() == this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
		return true;
	}
	
	public void setDetalleEditable(boolean value) {}

	public String getRequisicionObra() {
		if (this.pojoRequisicion != null && this.pojoRequisicion.getIdObra() != null)
			return "<b>" + this.pojoRequisicion.getIdObra().getId() + "</b> - " + this.pojoRequisicion.getIdObra().getNombre();
		return "";
	}
	
	public void setRequisicionObra(String value) {}

	public boolean getObraEditable() {
		return (this.pojoRequisicion == null || this.pojoRequisicion.getId() == null || this.pojoRequisicion.getId() <= 0L) ? true : false; //"ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());
	}
	
	public void setObraEditable(boolean value) {}

	public String getProducto() {
		if (this.pojoProducto != null && this.pojoProducto.getClave() != null && this.pojoProducto.getDescripcion() != null)
			return this.pojoProducto.getClave() + " - " + this.pojoProducto.getDescripcion();
		return "";
	}
	
	public void setProducto(String value) {}
	
	public String getRequisicionSolicitante() {
		if (this.pojoRequisicion != null && this.pojoRequisicion.getIdSolicita() != null)
			return this.pojoRequisicion.getIdSolicita().getId() + " - " + this.pojoRequisicion.getIdSolicita().getNombre();
		return "";
	}
	
	public void setRequisicionSolicitante(String value) {}

	public long getIdRequisicion() {
		return this.idRequisicion;
	}
	
	public void setIdRequisicion(long idRequisicion) {
		this.idRequisicion = idRequisicion;
	}

	public RequisicionExt getPojoRequisicion() {
		return pojoRequisicion;
	}

	public void setPojoRequisicion(RequisicionExt pojoRequisicion) {
		this.pojoRequisicion = pojoRequisicion;
	}

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}

	public List<Requisicion> getListRequisiciones() {
		return listRequisiciones;
	}

	public void setListRequisiciones(List<Requisicion> listRequisiciones) {
		this.listRequisiciones = listRequisiciones;
	}

	public List<RequisicionDetalleExt> getListReqDetalles() {
		return listDetalles;
	}

	public void setListReqDetalles(List<RequisicionDetalleExt> listReqDetalles) {
		this.listDetalles = listReqDetalles;
	}

	public List<SelectItem> getListEmpleadosItems() {
		return listEmpleadosItems;
	}

	public void setListEmpleadosItems(List<SelectItem> listEmpleadosItems) {
		this.listEmpleadosItems = listEmpleadosItems;
	}
	
	public RequisicionDetalleExt getPojoReqDetalleBorrar() {
		return pojoReqDetalleBorrar;
	}

	public void setPojoReqDetalleBorrar(RequisicionDetalleExt pojoReqDetalleBorrar) {
		this.pojoReqDetalleBorrar = pojoReqDetalleBorrar;
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
	
	public boolean getOperacion() {
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

	public long getValComprador() {
		return valComprador;
	}

	public void setValComprador(long valComprador) {
		this.valComprador = valComprador;
	}
	
	public List<RequisicionDetalleExt> getListReqDetallesEliminados() {
		return listDetallesEliminados;
	}
	
	public void setListReqDetallesEliminados(List<RequisicionDetalleExt> listReqDetallesEliminados) {
		this.listDetallesEliminados = listReqDetallesEliminados;
	}

	public List<SelectItem> getTiposBusquedaProductos() {
		return tiposBusquedaProductos;
	}

	public void setTiposBusquedaProductos(List<SelectItem> tiposBusquedaProductos) {
		this.tiposBusquedaProductos = tiposBusquedaProductos;
	}

	public String getCampoBusquedaProductos() {
		return campoBusquedaProductos;
	}

	public void setCampoBusquedaProductos(String campoBusquedaProductos) {
		this.campoBusquedaProductos = campoBusquedaProductos;
	}

	public String getValorBusquedaProductos() {
		return valorBusquedaProductos;
	}

	public void setValorBusquedaProductos(String valorBusquedaProductos) {
		this.valorBusquedaProductos = valorBusquedaProductos;
	}

	public long getFamiliaBusquedaProductos() {
		return familiaBusquedaProductos;
	}

	public void setFamiliaBusquedaProductos(long familiaBusquedaProductos) {
		this.familiaBusquedaProductos = familiaBusquedaProductos;
	}

	public List<Producto> getListProductos() {
		return listProductos;
	}

	public void setListProductos(List<Producto> listProductos) {
		this.listProductos = listProductos;
	}

	public Producto getPojoProducto() {
		return pojoProductoSeleccionado;
	}

	public void setPojoProducto(Producto pojoProducto) {
		this.pojoProductoSeleccionado = pojoProducto;
	}
	
	public RequisicionDetalleExt getPojoReqDetalle() {
		return pojoReqDetalle;
	}

	public void setPojoReqDetalle(RequisicionDetalleExt pojoReqDetalle) {
		this.pojoReqDetalle = pojoReqDetalle;
	}

	public List<SelectItem> getListFamiliasItems() {
		return listFamiliasItems;
	}

	public void setListFamiliasItems(List<SelectItem> listFamiliasItems) {
		this.listFamiliasItems = listFamiliasItems;
	}

	public double getCantidadProducto() {
		return cantidadProducto;
	}

	public void setCantidadProducto(double cantidadProducto) {
		this.cantidadProducto = cantidadProducto;
	}
	
	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public boolean getPermiteEditar() {
		return permiteEditar;
	}

	public void setPermiteEditar(boolean permiteEditar) {
		this.permiteEditar = permiteEditar;
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
		return (this.pojoRequisicion != null ? this.pojoRequisicion.getIdMoneda() : 0L);
	}
	
	public void setMoneda(long idMoneda) {
		if (this.pojoRequisicion == null)
			return;
		if (this.listMonedas == null || this.listMonedas.isEmpty())
			return;

		this.idMoneda = idMoneda;
		this.pojoRequisicion.setIdMoneda(idMoneda);
		this.pojoRequisicion.setMoneda("");
		for (Moneda moneda : this.listMonedas) {
			if (idMoneda == moneda.getId()) {
				this.pojoRequisicion.setMoneda(moneda.getAbreviacion());
				break;
			}
		}
	}

	public boolean isPerfilEgresos() {
		return perfilEgresos;
	}

	public void setPerfilEgresos(boolean perfilEgresos) {
		this.perfilEgresos = perfilEgresos;
	}

	public Producto getPojoProductoNuevo() {
		return pojoProductoNuevo;
	}

	public void setPojoProductoNuevo(Producto pojoProductoNuevo) {
		this.pojoProductoNuevo = pojoProductoNuevo;
	}

	public List<SelectItem> getListUnidadesMedidaItems() {
		return listUnidadesMedidaItems;
	}

	public void setListUnidadesMedidaItems(List<SelectItem> listUnidadesMedidaItems) {
		this.listUnidadesMedidaItems = listUnidadesMedidaItems;
	}

	public double getPrecioVenta() {
		if (this.pojoProductoNuevo != null)
			return this.pojoProductoNuevo.getPrecioCompra() * 1.7;
		return 0.0;
	}
	
	public void setPrecioVenta(double precioVenta) {
		this.pojoProductoNuevo.setPrecioVenta(precioVenta);
	}

	public double getPrecioUnitario() {
		if (this.pojoProductoNuevo != null)
			return this.pojoProductoNuevo.getPrecioCompra() * 1.1;
		return 0.0;
	}
	
	public void setPrecioUnitario(double precioUnitario){
		this.pojoProductoNuevo.setPrecioUnitario(precioUnitario);
	}
	
	public boolean isPermiteExcedente() {
		if (this.pojoProductoNuevo != null)
			return this.pojoProductoNuevo.getPermiteExcedente() == 1 ? true : false;
		return false;
	}
	
	public void setPermiteExcedente(boolean permiteExcedente){
		this.pojoProductoNuevo.setPermiteExcedente( permiteExcedente ? 1 : 0);
	}

	public boolean isPreviaBusquedaProducto() {
		return previaBusquedaProducto;
	}

	public void setPreviaBusquedaProducto(boolean previaBusquedaProducto) {
		this.previaBusquedaProducto = previaBusquedaProducto;
	}

	public List<SelectItem> getTiposObras() {
		return tiposObras;
	}

	public void setTiposObras(List<SelectItem> tiposObras) {
		this.tiposObras = tiposObras;
	}

	public int getValorBusquedaTipoObra() {
		return valorBusquedaTipoObra;
	}

	public void setValorBusquedaTipoObra(int valorBusquedaTipoObra) {
		this.valorBusquedaTipoObra = valorBusquedaTipoObra;
	}

	public boolean isAvanzadaBusquedaObras() {
		return avanzadaBusquedaObras;
	}

	public void setAvanzadaBusquedaObras(boolean avanzadaBusquedaObras) {
		this.avanzadaBusquedaObras = avanzadaBusquedaObras;
	}

	public boolean getTieneSBO() {
		return (this.listSolicitudes != null && ! this.listSolicitudes.isEmpty());
	}
	
	public void setTieneSBO(boolean value) {}
	
	public List<SolicitudBodega> getListSolicitudes() {
		return listSolicitudes;
	}

	public void setListSolicitudes(List<SolicitudBodega> listSolicitudes) {
		this.listSolicitudes = listSolicitudes;
	}

	public int getNumPaginaSolicitudBodega() {
		return numPaginaSolicitudBodega;
	}

	public void setNumPaginaSolicitudBodega(int numPaginaSolicitudBodega) {
		this.numPaginaSolicitudBodega = numPaginaSolicitudBodega;
	}
}


//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//2.2	  | 28/06/2016 | Daniel Azamar		| Corrección al apartado de guardar. Se comentó la linea que asigna la requisición a Null. En el método nuevo() se limpio la lista de requisiciones y el comprador se seteó a cero
