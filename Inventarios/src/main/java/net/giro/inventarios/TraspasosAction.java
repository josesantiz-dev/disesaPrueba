package net.giro.inventarios;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.TipoObraAutorizadas;
import net.giro.adp.beans.TipoObraJerarquia;
import net.giro.adp.beans.TipoObraRevisadas;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.adp.logica.ObraRem;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.comun.TipoMovimientoReferencia;
import net.giro.inventarios.comun.TipoMovimientoReferenciaExtendida;
import net.giro.inventarios.comun.TipoTraspaso;
import net.giro.inventarios.dao.TraspasoBodegaBodegaDAO;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;
import net.giro.navegador.comun.Permiso;
import net.giro.navegador.comun.PermisoExt;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;

@ViewScoped
@ManagedBean(name="traspasosAction")
public class TraspasosAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TraspasosAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private Permiso permisosBase;
	private PermisoExt permisos;
	private HttpSession httpSession;
	private AlmacenTraspasoRem ifzTraspaso;
	private TraspasoDetalleRem ifzTraspasoDetalle;
	private TraspasoBodegaBodegaDAO ifzTraspasoBodegas;
	private AlmacenProductoRem ifzAlmacenProducto;
	private ObraAlmacenesRem ifzObrasAlmacenes;
	private AlmacenMovimientosRem ifzMovimientos;
	private MovimientosDetalleRem ifzMovimientosDetalle;
	private ReportesRem ifzReportes;
	private ConGrupoValoresRem ifzGpoValores;
	private ConValoresRem ifzConValores;
	private NQueryRem ifzQuery;
	private boolean puedeEditar;
	private boolean puedeCancelar;
	private boolean PERFIL_ADMINISTRATIVO;
	private int traspasoEstatus;
	private long idTraspaso;
	private String movimientosSalida;
	private String movimientosEntrada;
	private AlmacenTraspasoExt pojoTraspaso;
	private TraspasoDetalleExt pojoTraspasoDetalleEliminar;
	private List<AlmacenTraspaso> listTraspasos;
	private List<TraspasoDetalleExt> traspasoDetalles;
	private List<TraspasoDetalleExt> listaDetalleCantidades; // Lista para respaldar las cantidades de productos
	private int pagDetalles;
	// Almacenes
	private AlmacenRem ifzAlmacen;
	private List<Almacen> listAlmacenes;
	private List<SelectItem> listAlmacenesItems;
	// Empleados
	private EmpleadoRem ifzEmpleado;
	private List<Empleado> listEmpleados;
	private List<SelectItem> listEmpleadosItems;
	// BUSQUEDA PRINCIPAL
	private List<SelectItem> listaCampoBusqueda;
	private String campoBusqueda;  
	private String valorBusqueda;
	private Date fechaBusqueda;
	private int numPagina;
	// Busqueda obras
	private ObraRem ifzObras;
	private Obra pojoObra;
	private Obra pojoObraOrigen;
	private List<Obra> listObrasPrincipales;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorOpcionBusquedaObras;
	private int numPaginaObras;
	private boolean bodegaBloqueada;
	private boolean settingObraOrigen;
	//Busqueda Productos
	private ProductoRem ifzProductos;
	private List<AlmacenProductoExt> listaBusquedaProductos; //Producto
	private AlmacenProductoExt pojoProductoBusqueda; // Producto
	private List<SelectItem> tiposBusquedaProductos;	
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private int valorOpcionBusquedaProductos;
	private int numPaginaProductos;
	private boolean admvaBusquedaProductos;
	// Familia
	private ConGrupoValores pojoGpoMaestro;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	private long idFamilia;
	//Variables para esta el movimiento de salida
	private TraspasoDetalleExt traspasoSeleccionado;
	private int cantidadProductoDetalles;
	private double cantidadProductoSeleccionado;
	private long almacenAnterior;
	// TIPOS DE TRASPASO
	private List<SelectItem> listTiposTraspasos;
	private String tipoTraspaso;
	// EMPLEADO-USUARIO
	private EmpleadoExt pojoEmpleadoUsuario;
	private List<Almacen> listAlmacenesTrabajo;
	//private List<SelectItem> listAlmacenesTrabajoItems;
	private Almacen almacenTrabajo;
	private int almacenBodega;
	private List<Long> listPuestosValidos;
	// Control
	private boolean operacionCompleta;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// DEBUG
	private boolean isDebug;
	private Map<String, String> paramsRequest;
	
	public TraspasosAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String resValue = "";
		String[] splitted = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);

	    	this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
		    	this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
		    // Comprobamos si requerimos levantar la variable DEBUG
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;

			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);
			
			// Recuperamos puesto requerido (No obligatorio)
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			resValue = this.entornoProperties.getString("puesto");
			if (resValue != null && ! "".equals(resValue.trim())) {
				this.listPuestosValidos = new ArrayList<Long>();
				if (resValue.contains(",")) {
					splitted = resValue.split(",");
					for (String val : splitted)
						this.listPuestosValidos.add(Long.valueOf(val));
				} else {
					this.listPuestosValidos.add(Long.valueOf(resValue));
				}
			}

			ctx = new InitialContext();
			this.ifzTraspaso = (AlmacenTraspasoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
			this.ifzTraspasoDetalle = (TraspasoDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			this.ifzTraspasoBodegas = (TraspasoBodegaBodegaDAO) ctx.lookup("ejb:/Model_Inventarios//TraspasoBodegaBodegaImp!net.giro.inventarios.dao.TraspasoBodegaBodegaDAO");
			this.ifzAlmacen = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzProductos = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzAlmacenProducto = (AlmacenProductoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			this.ifzEmpleado = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObrasAlmacenes = (ObraAlmacenesRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzMovimientos = (AlmacenMovimientosRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzMovimientosDetalle = (MovimientosDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzGpoValores = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");

			this.ifzTraspaso.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspasoDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacenProducto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObrasAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMovimientosDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Grupo de valores para TIPO DE MAESTROS (Nivel Cero) de productos 
			this.pojoGpoMaestro = this.ifzGpoValores.findByName("SYS_CODE_NIVEL0");
			if (this.pojoGpoMaestro == null || this.pojoGpoMaestro.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_CODE_NIVEL0 en con_grupo_valores");
			
			// Grupo de valores para ESPECIALIDADES de productos
			this.pojoGpoEspecialidades = this.ifzGpoValores.findByName("SYS_PRODUCTO_ESPECIALIDADES");
			if (this.pojoGpoEspecialidades == null || this.pojoGpoEspecialidades.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_PRODUCTO_ESPECIALIDADES en con_grupo_valores");

			// Grupo de valores para FAMILIAS de productos 
			this.pojoGpoFamilias = this.ifzGpoValores.findByName("SYS_FAMILIA_PRODUCTO");
			if (this.pojoGpoFamilias == null || this.pojoGpoFamilias.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_FAMILIA_PRODUCTO en con_grupo_valores");
			
			// Tipos de Traspasos
			this.listTiposTraspasos = new ArrayList<SelectItem>();
			switch (this.almacenBodega) {
				case 1:
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString(), "Traspaso de Almacen a Bodega"));
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString(), "Traspaso de Almacen a Almacen"));
					break;
				case 2:
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString(), "Traspaso por Devolucion a Almacen"));
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_BODEGA_BODEGA.toString(), "Traspaso de Bodega a Bodega"));
					break;
				default:
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString(), "Traspaso de Almacen a Bodega"));
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString(), "Traspaso por Devolucion a Almacen"));
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString(), "Traspaso de Almacen a Almacen"));
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_BODEGA_BODEGA.toString(), "Traspaso de Bodega a Bodega"));
					break;
			}
			this.tipoTraspaso = this.listTiposTraspasos.get(0).getValue().toString();

			// Permisos
			resValue = (this.entornoProperties.containsKey("SALIDAS") ? this.entornoProperties.getString("SALIDAS") : "SALIDAS");
			resValue = (resValue != null && ! "".equals(resValue.trim())) ? resValue.trim() : "0";
			this.permisosBase = this.loginManager.getPermisos(this.loginManager.getIdAplicacion(), Long.parseLong(resValue));
			comprobarUsuario();
			
			// Busqueda principal
			this.listaCampoBusqueda = new ArrayList<SelectItem>();
			this.listaCampoBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.listaCampoBusqueda.add(new SelectItem("a.fecha", "Fecha"));
			this.listaCampoBusqueda.add(new SelectItem("a.nombreObra", "Obra"));
			this.listaCampoBusqueda.add(new SelectItem("a.recibeNombre", "Recibe"));
			this.listaCampoBusqueda.add(new SelectItem("a.entregaNombre", "Entrega"));
			this.listaCampoBusqueda.add(new SelectItem("c.clave", "Codigo"));
			this.listaCampoBusqueda.add(new SelectItem("a.id", "ID"));
			this.campoBusqueda = this.listaCampoBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			//this.numPagina = 1;

			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreResponsable", "Responsable"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
			
			// Busqueda Productos
			this.tiposBusquedaProductos = new ArrayList<SelectItem>();
			this.tiposBusquedaProductos.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaProductos.add(new SelectItem("clave", "Codigo"));
			this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
			nuevaBusquedaProductos();
			
			this.listTraspasos = new ArrayList<AlmacenTraspaso>();
			cargarAlmacenes();
			//cargarAlmacenesOLD();
			cargarFamilias();
			cargarEmpleados();
			nuevoTraspaso();
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar TraspasosAction", e);
		}
	}
	
	public void buscar() {
		String value = "";
		
		try {
			// Validamos permiso de Lectura/Consulta
			/*if (! this.permisos.getConsultar()) {
				control(-1, "No tiene permitido Consultar informacion");
				controlLog("301 - No tiene permitido Consultar informacion");
				return;
			}*/

			if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
				control(-1, "Debe seleccionar un Almacen/Bodega previamente");
				return;
			}
			
			this.campoBusqueda = (this.campoBusqueda != null && ! "".equals(this.campoBusqueda.trim())) ? this.campoBusqueda.trim() : this.listaCampoBusqueda.get(0).getValue().toString();
			this.valorBusqueda = (this.valorBusqueda != null && ! "".equals(this.valorBusqueda.trim())) ? this.valorBusqueda.trim() : "";
			value = "fecha".equals(this.campoBusqueda) ? (new SimpleDateFormat("MM/dd/yyyy")).format(this.fechaBusqueda) : this.valorBusqueda;
			
			System.out.println("campoBusqueda " + campoBusqueda);
    		System.out.println("valorBusqueda " + valorBusqueda);
    		
    		this.numPagina = 1;
			this.listTraspasos = this.ifzTraspaso.findLikePropertyTraspasoDevolucion(this.campoBusqueda, value, this.almacenTrabajo.getId(), this.almacenTrabajo.getId(), true, false, "date(model.fecha) desc, model.id desc", 0);
    		this.listTraspasos = (this.listTraspasos != null ? this.listTraspasos : new ArrayList<AlmacenTraspaso>());
			if (this.listTraspasos.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Traspasos", e);
		} 
	}

	public void nuevoTraspaso() {
		Map<String,String> params = null;
		ObraAlmacenes obraAlmacen = null;
		
		try {
			control();
			if (! validaciones())
				return;
			this.puedeEditar = true;
			this.traspasoEstatus = -1;
			this.idTraspaso = 0L;
			this.movimientosSalida = "";
			this.movimientosEntrada = "";
			this.pojoTraspaso = new AlmacenTraspasoExt();
			this.pojoTraspaso.setFecha(Calendar.getInstance().getTime());
			this.pojoTraspaso.setAlmacenOrigen(this.almacenTrabajo);
			this.pojoTraspaso.setEntrega(this.ifzEmpleado.findById(this.almacenTrabajo.getIdEncargado()));
			this.pojoTraspaso.setIdObra(0L);
			this.pojoTraspaso.setAlmacenDestino(null);
			this.pojoTraspaso.setRecibe(null);
			this.pojoTraspaso.setSolicitudCotizacion(0L);
			this.pojoTraspaso.setSolicitudRequisicion(0L);
			this.traspasoDetalles = new ArrayList<TraspasoDetalleExt>();
			this.listaDetalleCantidades = new ArrayList<TraspasoDetalleExt>();

			// Cargamos Almacenes
			cargarAlmacenes();
			// Cargamos Empleados
			cargarEmpleados();

			params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			this.tipoTraspaso = (params.containsKey("origen") ? params.get("origen") : this.tipoTraspaso);
			this.tipoTraspaso = (this.tipoTraspaso == null || "".equals(this.tipoTraspaso.trim()) ? this.listTiposTraspasos.get(0).getValue().toString() : this.tipoTraspaso);
			this.pojoObra = null;
			this.pojoObraOrigen = null;
			this.cantidadProductoDetalles = 0;
			//this.pagDetalles = 1;
			//filtrarAlmacenes();
			
			if ("TR TZ".contains(this.tipoTraspaso))
				this.bodegaBloqueada = true;
			else if ("DE".equals(this.tipoTraspaso)) {
				obraAlmacen = obraAlmacen(this.almacenTrabajo);
				if (obraAlmacen != null && obraAlmacen.getId() != null && obraAlmacen.getId() > 0L) {
					setIdAlmacenDestino(obraAlmacen.getIdAlmacen());
					seleccionaEncargado();
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al inicializar un nuevo Traspaso", e);
		}
	}
	
	public void editar() {
		try {
			control();
			/*
			if (! this.usuarioValido) {
				control(-1, "No es un usuario autorizado para ver o editar Entradas a Almacen/Bodega");
				return;
			}*/
			
			this.puedeEditar = false;
			// Obtengo almacenes y empleados asignados
			this.tipoTraspaso = "";
			this.traspasoEstatus = 0;
			this.pojoTraspaso = this.ifzTraspaso.findByIdExt(this.idTraspaso);
			if (this.pojoTraspaso == null || this.pojoTraspaso.getId() == null || this.pojoTraspaso.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Traspaso indicado");
				return;
			}

			if (this.pojoTraspaso.getTipo() == 1 || this.pojoTraspaso.getTipo() == 3)
				this.tipoTraspaso = TipoMovimientoReferencia.TRASPASO.toString();
			else if (this.pojoTraspaso.getTipo() == 2)
				this.tipoTraspaso = TipoMovimientoReferencia.DEVOLUCION.toString();
			this.movimientosSalida = "";
			this.movimientosEntrada = "";
			if (this.getPermisoAdmin()) {
				this.movimientosSalida = this.movimientoSalida(this.pojoTraspaso.getId());
				this.movimientosSalida = (this.movimientosSalida != null ? this.movimientosSalida.trim() : "");
				this.movimientosEntrada = this.movimientoEntrada(this.pojoTraspaso.getId());
				this.movimientosEntrada = (this.movimientosEntrada != null ? this.movimientosEntrada.trim() : "");
			}
				
			// Cargamos Almacenes
			cargarAlmacenes();
			// Cargamos Empleados
			cargarEmpleados();

			// Cargo los detalles del Traspaso
			this.traspasoDetalles = this.ifzTraspasoDetalle.findExtAll(this.pojoTraspaso.getId());
			if (this.traspasoDetalles == null || this.traspasoDetalles.isEmpty())
				control(-1, "El Traspaso indicado no tiene productos");
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los detalles del Traspaso seleccionado", e);
			this.traspasoEstatus = -1;
		} finally {
			this.pagDetalles = 1;
			// Determinamos estatus si corresponde
			this.traspasoEstatus = (this.pojoTraspaso.getRecibido() == 1 ? (this.pojoTraspaso.getCompleto() == 1 ? 2 : 1) : 0);
		}
	}
	
	public void guardar() {
		long idTraspaso = 0L;
		long idObraOrigen = 0L;
		
		try {
			System.out.println("ENTRA GUARDAR");
			control();
			if (! validaGuardarTraspaso()) 
				return;

			System.out.println("ENTRA GUARDAR 1");
			if (this.pojoTraspaso.getId() != null && this.pojoTraspaso.getId() > 0L) {
				this.pojoTraspaso.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzTraspaso.update(this.pojoTraspaso);
				control(-1, "Traspaso actualizado");
				return;
			}

			System.out.println("ENTRA GUARDAR 2");
			if (this.pojoObraOrigen != null && this.pojoObraOrigen.getId() != null && this.pojoObraOrigen.getId() > 0L)
				idObraOrigen = this.pojoObraOrigen.getId();

			System.out.println("ENTRA GUARDAR 3");
			this.pojoTraspaso.setId(null);
			this.pojoTraspaso.setTipo(TipoTraspaso.TRASPASO.ordinal());
			if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString()))
				this.pojoTraspaso.setTipo(TipoTraspaso.DEVOLUCION.ordinal());
			this.pojoTraspaso.setEstatus(0);
			this.pojoTraspaso.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoTraspaso.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());

			System.out.println("ENTRA GUARDAR 4");
			// Guardamos Traspaso
			log.info("Guardando Traspaso ... ");
			this.ifzTraspaso.setInfoSesion(this.loginManager.getInfoSesion());
			this.pojoTraspaso.setId(this.ifzTraspaso.save(this.pojoTraspaso));
			idTraspaso = this.pojoTraspaso.getId();

			System.out.println("ENTRA GUARDAR 5");
			log.info("Traspaso guardado. Guardando detalles de Traspaso ... ");
			for (TraspasoDetalleExt detalle : this.traspasoDetalles) {
				detalle.setIdAlmacenTraspaso(this.pojoTraspaso.getId());
				detalle.setEstatus(1); // en transito
				detalle.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				detalle.setFechaCreacion(Calendar.getInstance().getTime());
				detalle.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				detalle.setFechaModificacion(Calendar.getInstance().getTime());
			}

			System.out.println("ENTRA GUARDAR 6");
			// Guarda detalle
			this.ifzTraspasoDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspasoDetalle.saveOrUpdateListExt(this.traspasoDetalles);
			if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_BODEGA_BODEGA.toString())) 
				this.ifzTraspasoBodegas.save(this.pojoTraspaso.getId(), idObraOrigen, this.pojoObra.getId(), this.loginManager.getInfoSesion().getEmpresa().getCodigo());

			System.out.println("ENTRA GUARDAR 7");
			// Añadimos el elemento al inicio de la Lista
			this.operacionCompleta = true;
			this.listTraspasos = (this.listTraspasos != null ? this.listTraspasos : new ArrayList<AlmacenTraspaso>());
			this.listTraspasos.add(0, this.ifzTraspaso.convertir(this.pojoTraspaso));
			nuevoTraspaso();

			System.out.println("ENTRA GUARDAR 8");
			// Ahora, afectamos existencias por JMS
			// ----------------------------------------------------------------------------------------------------------------------
			this.ifzTraspaso.setInfoSesion(this.loginManager.getInfoSesion());
			if (! this.ifzTraspaso.postTraspaso(idTraspaso)) 
				control(false, -1, "Se genero el Traspaso correctamente.\nSin embargo, este proceso requiere generar un movimiento de salida, lo cual no se pudo completar.", null);

			System.out.println("ENTRA GUARDAR 9");
		} catch (Exception e) {
			control("Ocurrio un problema al guardar el traspaso", e);
		} 
	}
	
	public void eliminar() {
		Respuesta respuesta = null;
		AlmacenTraspaso traspaso = null;
		
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
				control(-1, "Debe seleccionar un Almacen/Bodega previamente");
				return;
			}
			
			/*if (! this.permisos.borrar(this.almacenTrabajo.getId())) {
				control(-1, "No tiene permitido Borrar/Eliminar informacion");
				controlLog("301 - No tiene permitido Borrar/Eliminar informacion");
				return;
			}*/
			
			traspaso = this.ifzTraspaso.findById(this.idTraspaso);
			if (traspaso == null || traspaso.getId() == null || traspaso.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Traspaso indicado");
				return;
			}
			
			// Cancelamos
			this.ifzTraspaso.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzTraspaso.cancelar(traspaso);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al intentar Cancelar el Traspaso indicado\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}
			
			// Actualizo el Traspaso en el listado
			traspaso = (AlmacenTraspaso) respuesta.getBody().getValor("traspaso");
			for (AlmacenTraspaso item : this.listTraspasos) {
				if (traspaso.getId().longValue() == item.getId().longValue()) {
					this.listTraspasos.set(this.listTraspasos.indexOf(item), traspaso);
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los detalles del Traspaso seleccionado", e);
		}
	}

	public void reporte() {
		List<AlmacenMovimientos> listMovs = null;
		AlmacenMovimientos pojoMovimiento = null;
		SimpleDateFormat formatter = null;
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			this.pojoTraspaso = this.ifzTraspaso.findByIdExt(this.idTraspaso);
			if (this.pojoTraspaso == null || this.pojoTraspaso.getId() == null || this.pojoTraspaso.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar obtener el Traspaso seleccionado");
				return;
			}
			
			// Recuperamos el Movimiento
			listMovs = this.ifzMovimientos.findSalidaByTraspaso(this.pojoTraspaso.getId(), this.pojoTraspaso.getTipo(), 0);
			if (listMovs == null || listMovs.isEmpty()) {
				control(-1, "No se pudo recuperar el Movimiento para el Traspaso seleccionado");
				return;
			}
			
			log.info("Imprimiendo Entrada ... ");
			formatter = new SimpleDateFormat("yyMMddHH");
			nombreDocumento = "T-[tipo]-[id]-[fecha].[extension]";
			pojoMovimiento = listMovs.get(0);
			
			// Parametros del reporte
			log.info("Generando parametros ... ");
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idMovimiento", pojoMovimiento.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.salida"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());

			log.info("Generando reporte ... ");
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO - " + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al intentar imprimir la Entrada a Almacen\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 

			log.info("Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = nombreDocumento.replace("[tipo]", (this.pojoTraspaso.getTipo() == 1 ? "TRA" : this.pojoTraspaso.getTipo() == 2 ? "DEV" : this.pojoTraspaso.getTipo() == 3 ? "SBO" : "NA"));
			nombreDocumento = nombreDocumento.replace("[id]", String.format("%1$05d", this.pojoTraspaso.getId()).substring(3));
			nombreDocumento = nombreDocumento.replace("[fecha]", formatter.format(Calendar.getInstance().getTime()));
			nombreDocumento = nombreDocumento.replace("[extension]", formatoDocumento);
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido de la Entrada a Almacen");
				control(-1, "Ocurrio un problema al intentar imprimir la Entrada a Almacen");
				return;
			}

			log.info("Lanzando reporte ... ");
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			log.info("Maestro exportado. Proceso finalizado");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir el Traspaso", e);
		} 
	}

	public void eliminarProductosGrid() {
		this.traspasoDetalles.clear();
		this.cantidadProductoDetalles = 0;
		this.almacenAnterior = 0;
	}
	
	public void regresarAlmacen() {
		log.info("Almacen anterior:");
	}
	
	public void validaCantidadProducto(AjaxBehaviorEvent event) {
		int currentTraspasoDetalle = -1;

    	try {
    		control();
			if (! this.puedeEditar) 
				return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
			
			if (this.traspasoDetalles.isEmpty()) 
				return;
			
			if (event.getComponent().getAttributes().get("currentIndex") == null)
				return;
			currentTraspasoDetalle = (int) event.getComponent().getAttributes().get("currentIndex");
			
			this.traspasoSeleccionado = this.traspasoDetalles.get(currentTraspasoDetalle);
			log.info("Elemento seleccionado: " + this.traspasoSeleccionado.getIdProducto().getId() + ", Producto: " + this.traspasoSeleccionado.getIdProducto().getDescripcion() + ", Cant:" + this.traspasoSeleccionado.getCantidad());
				this.cantidadProductoSeleccionado = this.ifzAlmacenProducto.findCantidadEnAlmacen(this.pojoTraspaso.getAlmacenOrigen().getId(), this.traspasoSeleccionado.getIdProducto().getId());
			
	    	log.info("Cantidad del producto en Almacen: " + this.cantidadProductoSeleccionado);
	    	this.setCantidadExistente(this.traspasoSeleccionado.getIdProducto().getClave(), this.cantidadProductoSeleccionado);
			
			for (TraspasoDetalleExt movimiento : this.traspasoDetalles) {
				if (movimiento.getIdProducto().getClave().equals(this.traspasoSeleccionado.getIdProducto().getClave())) {
					log.info(" Producto Cantidad : " + this.cantidadProductoSeleccionado 
							+ ", Movimiento Cantidad : " + movimiento.getCantidad()
							+ ", Movimiento Index : " + this.traspasoDetalles.indexOf(movimiento));
				}
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar la existencia del Producto", e);
		}
	}

	public boolean validaciones() {
		control();
		// Validamos permiso de Lectura/Consulta
		if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
			control(-1, "Debe seleccionar un Almacen/Bodega previamente");
			return false; 
		}
		
		/*if (! this.permisos.escribir(this.almacenTrabajo.getId())) {
			control(-1, "No tiene permitido Añadir/Editar informacion");
			controlLog("301 - No tiene permitido Añadir/Editar informacion");
			return false;
		}*/
		
		return true;
	}
	
	public void ejecutarSalida() {
		try {
			if (! this.ifzTraspaso.postTraspaso(this.pojoTraspaso.getId())) 
				throw new Exception("500 Internal Server Error");
			control(-1, "Evento de descuento lanzado");
		} catch (Exception e) {
			e.printStackTrace();
			control("Ocurrio un problema al intentar lanzar el evento de descuento del Traspaso indicado", e);
		}
	}

	private boolean comprobarUsuario() {
		List<Empleado> listEmpleado = null;
		Long idEmpleado = null;
		Long idPuesto = null;
		boolean puestoValido = false;
		String msgLog = "";
		
		try {
			if (this.loginManager == null)
				return false;

			this.permisos = new PermisoExt(this.permisosBase, 0L);
			this.listAlmacenesTrabajo = new ArrayList<Almacen>();
			if ("ADMINISTRADOR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) {
				this.listAlmacenesTrabajo = this.ifzAlmacen.findAll();
				this.permisos = new PermisoExt(this.permisosBase, -1);
				return true;
			}
			
			// Determinamos EMPLEADO de manera directa o por correo electronico
			// ----------------------------------------------------------------------------------
			msgLog += "Comprobando Empleado por Usuario ... ";
			idEmpleado = this.loginManager.getUsuarioResponsabilidad().getUsuario().getIdEmpleado();
			if (idEmpleado == null || idEmpleado <= 1L) {
				msgLog += "FAIL\nComprobando Empleado por Email: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo() + " ... ";
				listEmpleado = this.ifzEmpleado.findByProperty("email", this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo(), false, false, "", 0);
				if (listEmpleado != null && listEmpleado.size() == 1)
					idEmpleado = listEmpleado.get(0).getId();
			}
			
			if (idEmpleado == null || idEmpleado <= 1L) {
				msgLog += "FAIL\n--> Usuario sin Empleado asociado <--";
				return false;
			}
			
			// Recuperamos pojo EMPLEADO
			msgLog += "Recuperando Empleado " + idEmpleado + " ... ";
			this.pojoEmpleadoUsuario = this.ifzEmpleado.findByIdExt(idEmpleado);
			if (this.pojoEmpleadoUsuario == null || this.pojoEmpleadoUsuario.getId() == null || this.pojoEmpleadoUsuario.getId() <= 0L){
				msgLog += "FAIL\n--> No se encontro Empleado asociado al Usuario <--";
				return false;
			}
			
			// Recuperamos ALMACEN/BODEGA base del Empleado
			// ----------------------------------------------------------------------------------
			msgLog += "\nRecuperando asignaciones de Almacenes/Bodegas del Empleado ... ";
			this.listAlmacenesTrabajo = this.ifzAlmacen.findByProperty("idEncargado", this.pojoEmpleadoUsuario.getId());
			this.listAlmacenesTrabajo = (this.listAlmacenesTrabajo != null) ? this.listAlmacenesTrabajo : new ArrayList<Almacen>();
			if (! this.listAlmacenesTrabajo.isEmpty()) {
				this.almacenTrabajo = this.listAlmacenesTrabajo.get(0);
				this.permisos = new PermisoExt(this.permisosBase, this.almacenTrabajo.getId());
			}

			msgLog += "\nComprobando Puestos base ... ";
			if (this.listPuestosValidos != null && ! this.listPuestosValidos.isEmpty()) {
				// Comprobamos PUESTO
				// ----------------------------------------------------------------------------------
				msgLog += "OK\nRecuperando Puesto de Empleado ... ";
				if (this.pojoEmpleadoUsuario.getPuestoCategoria() == null) {
					msgLog += "FAIL\n --> Empleado sin PuestoCategoria asignado <--";
					return false;
				}
				
				if (this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto() != null 
						&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() != null 
						&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() <= 0L) {
					msgLog += "FAIL\n --> Empleado sin Puesto asignado <--";
					return false;
				}
				
				// Validamos PUESTO
				puestoValido = false;
				idPuesto = this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId();
				msgLog += "OK\nValidando Puesto de Empleado ... ";
				for (Long idPuestoValido : this.listPuestosValidos) {
					if (idPuesto.longValue() == idPuestoValido.longValue()) {
						puestoValido = true;
						msgLog += "OK";
						break;
					}
				}
				
				if (! puestoValido) {
					msgLog += "FAIL\n --> Empleado sin puesto valido para Entradas de Almacen <--";
					return false;
				}
			} else {
				msgLog += "FAIL\n --> Sin Puestos base para comprobar <--";
			}
			
			msgLog += "\n\nEmpleado OK!";
		} catch (Exception e) {
			log.error("Ocurrio un problema en Inventarios.EntradasAlmacenAction.comprobarUsuario()", e);
			return false;
		} finally {
			log.info(" \n---------------------------------------------------------" 
					+ "\nCOMPROBACION DE USUARIO: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "\n---------------------------------------------------------" 
					+ "\n" + msgLog 
					+ "\n---------------------------------------------------------");
		}
		
		return true;
	}

	@SuppressWarnings("unused")
	private boolean comprobarUsuarioOLD() {
		Long idEmpleado = null;
		Long idPuesto = null;
		boolean puestoValido = false;
		String msgLog = "";
		
		try {
			if (this.loginManager == null)
				return false;

			this.listAlmacenesTrabajo = new ArrayList<Almacen>();
			//this.listAlmacenesTrabajoItems = new ArrayList<SelectItem>();
			
			if ("ADMINISTRADOR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) {
				this.listAlmacenesTrabajo = this.ifzAlmacen.findAll();
				for (Almacen var : this.listAlmacenesTrabajo) 
					this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getNombre() + " (" + var.getIdentificador() + ")"));
				return true;
			}
	
			if (this.listPuestosValidos == null || this.listPuestosValidos.isEmpty()) {
				msgLog += "FAIL\n--> Sin puestos para comparar <--";
				return false;
			}
			
			// Determinamos EMPLEADO de manera directa o por correo electronico
			// ----------------------------------------------------------------------------------
			msgLog += "Comprobando Empleado por Usuario ... ";
			idEmpleado = this.loginManager.getUsuarioResponsabilidad().getUsuario().getIdEmpleado();
			if (idEmpleado == null || idEmpleado <= 1L) {
				msgLog += "FAIL\n--> Usuario sin Empleado asociado <--";
				return false;
			}
			
			// Recuperamos pojo EMPLEADO
			msgLog += "Recuperando Empleado " + idEmpleado + " ... ";
			this.pojoEmpleadoUsuario = this.ifzEmpleado.findByIdExt(idEmpleado);
			if (this.pojoEmpleadoUsuario == null || this.pojoEmpleadoUsuario.getId() == null || this.pojoEmpleadoUsuario.getId() <= 0L){
				msgLog += "FAIL\n--> No se encontro Empleado asociado al Usuario <--";
				return false;
			}
			
			// Recuperamos ALMACEN/BODEGA base del Empleado
			// ----------------------------------------------------------------------------------
			msgLog += "\nRecuperando asignaciones de Almacenes/Bodegas del Empleado ... ";
			this.listAlmacenesTrabajo = this.ifzAlmacen.findByProperty("idEncargado", this.pojoEmpleadoUsuario.getId());
			if (this.listAlmacenesTrabajo == null|| this.listAlmacenesTrabajo.isEmpty()) {
				msgLog += "FAIL\n --> Sin Almacenes/Bodegas para Empleado <--";
				this.listAlmacenesTrabajo = new ArrayList<Almacen>();
			}
			
			msgLog += "OK\nDeterminando nivel de Entrada para Almacenes/Bodegas para Empleado ... ";
			this.almacenBodega = 0;
			for (Almacen var : this.listAlmacenesTrabajo) {
				this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getNombre() + " (" + var.getIdentificador() + ")"));
				if (var.getTipo() == 1 || var.getTipo() == 3) // Si es Almacen (principal)
					this.almacenBodega = (this.almacenBodega == 2 || this.almacenBodega == 3) ? 3 : 1;
				if (var.getTipo() == 2 || var.getTipo() == 4) // Si es Bodega (obra)
					this.almacenBodega = (this.almacenBodega == 1 || this.almacenBodega == 3) ? 3 : 2;
			}
			
			this.almacenTrabajo = this.listAlmacenesTrabajo.get(0);
			
			// Comprobamos PUESTO
			// ----------------------------------------------------------------------------------
			msgLog += "\nComprobando Puestos base ... ";
			if (this.pojoEmpleadoUsuario.getPuestoCategoria() == null) {
				msgLog += "FAIL\n --> Empleado sin PuestoCategoria asignado <--";
				return false;
			}
			
			if (this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto() != null 
					&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() != null 
					&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() <= 0L) {
				msgLog += "FAIL\n --> Empleado sin puesto asignado <--";
				return false;
			}
			
			// Validamos PUESTO
			puestoValido = false;
			idPuesto = this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId();
			msgLog += "OK\nValidando Puesto de Empleado ... ";
			for (Long idPuestoValido : this.listPuestosValidos) {
				if (idPuesto.longValue() == idPuestoValido.longValue()) {
					puestoValido = true;
					msgLog += "OK";
					break;
				}
			}
			
			if (! puestoValido) {
				msgLog += "FAIL\n --> Empleado sin puesto valido para Entradas de Almacen <--";
				return false;
			}
			
			msgLog += "\n\nEmpleado OK!";
		} catch (Exception e) {
			log.error("Ocurrio un problema en Inventarios.EntradasAlmacenAction.comprobarUsuario()", e);
			return false;
		} finally {
			log.info(" \n---------------------------------------------------------" 
					+ "\nCOMPROBACION DE USUARIO: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "\n---------------------------------------------------------" 
					+ "\n" + msgLog 
					+ "\n---------------------------------------------------------");
		}
		
		return true;
	}

	private boolean isProductoEnLista() {
		for (TraspasoDetalleExt detalle : this.traspasoDetalles) {
			if (this.pojoProductoBusqueda.getProducto().getId().longValue() == detalle.getIdProducto().getId().longValue())
				return true;
		}
		
		return false;
	}
	
	private boolean validaGuardarTraspaso() {
		double cantidadProducto = 0;

		// Validamos permiso de Lectura/Consulta
		/*if (! this.permisos.getEditar()) {
			control(-1, "No tiene permitido Añadir/Editar informacion");
			controlLog("301 - No tiene permitido Añadir/Editar informacion");
			return false;
		}*/
		/*
		if (! this.usuarioValido) {
			control(-1, "No es un usuario autorizado para realizar Traspasos entre Almacenes/Bodegas");
			return false;
		}*/
		
		if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
			control(-1, "Debe indicar un Almacen/Bodega de origen para el Material");
			return false;
		}
		
		if (this.pojoTraspaso.getAlmacenOrigen() == null || this.pojoTraspaso.getAlmacenOrigen().getId() == null || this.pojoTraspaso.getAlmacenOrigen().getId() <= 0) {
			this.pojoTraspaso.setAlmacenOrigen(this.almacenTrabajo);
			this.pojoTraspaso.setEntrega(this.ifzEmpleado.findById(this.almacenTrabajo.getIdEncargado()));
		}
		
		/*if (this.pojoTraspaso.getAlmacenOrigen() == null || this.pojoTraspaso.getAlmacenOrigen().getId() == null || this.pojoTraspaso.getAlmacenOrigen().getId() == 0) {
			control(-10, "Seleccione Almacen/Bodega de origen del Material");
			return false;
		}*/

		if (this.pojoTraspaso.getEntrega() == null || this.pojoTraspaso.getEntrega().getId() == 0) {
			control(-14, "No asigno el trabajador que Entrega el Material");
			return false;
		}
		
		if (this.pojoTraspaso.getAlmacenDestino() == null || this.pojoTraspaso.getAlmacenDestino().getId() == null || this.pojoTraspaso.getAlmacenDestino().getId() == 0) {
			control(-12, "No asigno el Almacen de Destino del Material");
			return false;
		}
		
		/* Validacion obsoleta. Los almacenes se filtrar dependiendo del tipo de traspaso y/o almacen origen
		if (this.pojoTraspaso.getAlmacenOrigen().getId().compareTo(this.pojoTraspaso.getAlmacenDestino().getId()) == 0) {
			control(-13, "El almacen de salida no puede ser el mismo de Destino");
			return false;
		}*/
		
		if (this.pojoTraspaso.getRecibe() == null) {
			control(-15, "Indique el empleado que recibe");
			return false;
		}
		
		/* Validacion obsoleta, existe proceso para cuando Entrega y Recibe son el mismo
		if (this.pojoTraspaso.getRecibe().getId().compareTo(this.pojoTraspaso.getEntrega().getId()) == 0) {
			control(-16, "El empleado que entrega no puede ser el mismo que recibe");
			return false;
		}*/
		
		if (this.traspasoDetalles.isEmpty()) {
			control(-17, "Debe existir por lo menos un producto en la lista");
			return false;
		}
		
		// Comprobamos si algun elmento es igual o menor a cero o si excede el inventario
		for (TraspasoDetalleExt d : this.traspasoDetalles) {
			// comprobamos si la cantidad ingresada es igual o menor a cero
			if (d.getCantidad() == 0) {
				control(-18, "Indique cantidad de producto");
				return false;
			}

			// comprobamos si la cantidad ingresada excede el inventario
			cantidadProducto = this.getCantidadProductoExistencias(d.getIdProducto().getClave());
			if (d.getCantidad() > cantidadProducto) {
				control(-19, "Cantidad de producto Insuficiente");
				return false;
			}
		}
		
		if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_BODEGA_BODEGA.toString())) {
			if (this.pojoObraOrigen != null && this.pojoObraOrigen.getId() != null && this.pojoObraOrigen.getId() > 0L) {
				this.pojoTraspaso.setIdObra(this.pojoObraOrigen.getId());
				this.pojoTraspaso.setNombreObra(this.pojoObraOrigen.getNombre());
			}
		}
		
		return true;
	}
	
	private void setCantidadExistente(String claveProducto, double cantidadExistente) {
		for (TraspasoDetalleExt td : this.listaDetalleCantidades) {
			if (td.getIdProducto().getClave().equals(claveProducto)) {
				td.setCantidad(cantidadExistente);
				break;
			}
		}
	}
	
	private double getCantidadProductoExistencias(String claveProducto) {
		for (TraspasoDetalleExt td : this.listaDetalleCantidades) {
			if (td.getIdProducto().getClave().equals(claveProducto)) 
				return td.getCantidad();
		}
		
		return 0;
	}

	@SuppressWarnings("unchecked")
	private String movimientoSalida(Long idTraspaso) {
		List<Object> rows = null;
		String queryString = "";
		
		try {
			if (idTraspaso == null || idTraspaso <= 0L)
				return "";
			queryString = "select string_agg(cast(id as varchar), ',' order by id) as salidas from almacen_movimientos where tipo = 1 and id_traspaso = :idTraspaso group by id_traspaso";
			queryString = queryString.replace(":idTraspaso", idTraspaso.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				return rows.get(0).toString();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el(los) Movimiento(s) de SALIDA para el Traspaso: " + idTraspaso, e);
		}
		
		return "";
	}

	@SuppressWarnings("unchecked")
	private String movimientoEntrada(Long idTraspaso) {
		List<Object> rows = null;
		String queryString = "";
		
		try {
			if (idTraspaso == null || idTraspaso <= 0L)
				return "";
			queryString = "select string_agg(cast(id as varchar), ',' order by id) as entradas from almacen_movimientos where tipo = 0 and id_traspaso = :idTraspaso group by id_traspaso";
			queryString = queryString.replace(":idTraspaso", idTraspaso.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				return rows.get(0).toString();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el(los) Movimiento(s) de ENTRADA para el Traspaso: " + idTraspaso, e);
		}
		
		return "";
	}

	private Long validarID(Long value) {
		return (value != null && value > 0L) ? value : 0L;
	}
	
	private void control() {
		this.operacionCompleta = true;
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

	private void control(boolean incompleta, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacionCompleta = false;
		this.operacionCancelada = incompleta;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "Ocurrio un problema no controlado al realizar la operacion");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje + "\n");
	}
	
	// ------------------------------------------------------------------------------------
	// ALMACENES
	// ------------------------------------------------------------------------------------

	private void cargarAlmacenes() {
		List<Integer> tipos = null;
		List<Long> idAlmacenesAsignados = null;
		
		try {
			tipos = new ArrayList<Integer>(Arrays.asList(1, 2));
			if (isAdministrativo()) {
				tipos.add(3);
				tipos.add(4);
			}
			// Solo Almacenes/Bodegas ligados al Empleado
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.listAlmacenes = this.ifzAlmacen.findByTipo(tipos, false, "");
			this.listAlmacenes = (this.listAlmacenes != null) ? this.listAlmacenes : new ArrayList<Almacen>();

			idAlmacenesAsignados = new ArrayList<Long>();
			if (! "ADMINISTRADOR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) {
				for (Almacen var : this.listAlmacenesTrabajo) 
					idAlmacenesAsignados.add(var.getId());
			}
			
			this.listAlmacenesItems = new ArrayList<SelectItem>();
			//for (Almacen var : this.listAlmacenes) 
			//	this.listAlmacenesItems.add(new SelectItem(var.getId(), (idAlmacenesAsignados.contains(var.getId()) ? "* " : "") + var.getNombre() + " (" + var.getIdentificador() + ")"));
		
			for (Almacen var : listAlmacenesTrabajo) 
				this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getNombre() + " (" + var.getIdentificador() + ")"));
			

		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Almacenes", e);
		}
	}
	
	@SuppressWarnings("unused")
	private void cargarAlmacenesOLD() {
		try {
			this.listAlmacenesItems = new ArrayList<SelectItem>();
			this.listAlmacenes = this.ifzAlmacen.findAll();
			if (this.listAlmacenes == null || this.listAlmacenes.isEmpty()) 
				return;
			
			Collections.sort(this.listAlmacenes, new Comparator<Almacen>() {
				@Override
				public int compare(Almacen o1, Almacen o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
			
			if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L)
				return;
			filtrarAlmacenes();
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Almacenes", e);
		}
	}
	
	public void filtrarAlmacenes() {
		int tipoAlmacenRequeridos;
		
		this.listAlmacenesItems = new ArrayList<SelectItem>();
		this.bodegaBloqueada = false;
		
		if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString())) {
			// Traspaso de Almacen a Bodega: Se requieren bodegas
			tipoAlmacenRequeridos = (this.almacenTrabajo.getTipo() > 2) ? 4 : 2;
			for (Almacen var : this.listAlmacenes) {
				if (this.puedeEditar && this.almacenTrabajo.getId().longValue() == var.getId().longValue())
					continue;
				if (var.getTipo() == tipoAlmacenRequeridos) 
					this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getIdentificador() + " " + var.getNombre()));
			}
			
		} else if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString())) { 
			// Traspaso por Devolucion de Bodega a Almacen: Se requieren almacenes
			tipoAlmacenRequeridos = (this.almacenTrabajo.getTipo() > 2) ? 3 : 1;
			for (Almacen var : this.listAlmacenes) {
				if (this.almacenTrabajo.getId().longValue() == var.getId().longValue())
					continue;
				if (var.getTipo() == tipoAlmacenRequeridos) 
					this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getIdentificador() + " " + var.getNombre()));
			}
			
		} else if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString())) { 
			// Traspaso de Almacen a Almacen: Se requieren almacenes
			tipoAlmacenRequeridos = (this.almacenTrabajo.getTipo() > 2) ? 3 : 1;
			for (Almacen var : this.listAlmacenes) {
				if (this.almacenTrabajo.getId().longValue() == var.getId().longValue())
					continue;
				if (var.getTipo() == tipoAlmacenRequeridos) 
					this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getIdentificador() + " " + var.getNombre()));
			}
			
		} else if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_BODEGA_BODEGA.toString())) { 
			// Traspaso de Bodega a Bodega: Se requieren bodegas
			tipoAlmacenRequeridos = (this.almacenTrabajo.getTipo() > 2) ? 4 : 2;
			for (Almacen var : this.listAlmacenes) {
				if (this.almacenTrabajo.getId().longValue() == var.getId().longValue())
					continue;
				if (var.getTipo() == tipoAlmacenRequeridos) 
					this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getIdentificador() + " " + var.getNombre()));
			}
		} 
	}

	private ObraAlmacenes obraAlmacen(Almacen bodega) {
		try {
			if (bodega != null && bodega.getId() != null && bodega.getId() > 0L) {
				this.ifzObrasAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
				return this.ifzObrasAlmacenes.findAlmacenPrincipal(bodega.getId());
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Almacen asignado a la Bodega", e);
		}
		
		return null;
	}
	
	private ObraAlmacenes obraAlmacen(Obra obra) {
		try {
			if (obra != null && obra.getId() != null && obra.getId() > 0L) {
				this.ifzObrasAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
				return this.ifzObrasAlmacenes.findAlmacenPrincipal(obra.getId(), 0L);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Almacen asignado a la Bodega", e);
		}
		
		return null;
	}
	
	private ObraAlmacenes obraBodega(Obra obra) {
		try {
			if (obra != null && obra.getId() != null && obra.getId() > 0L) {
				this.ifzObrasAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
				return this.ifzObrasAlmacenes.findBodega(obra.getId());
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Bodega asignada a la Obra seleccionada", e);
		}
		
		return null;
	}

	public void actualizarResultados() {
		this.listTraspasos = new ArrayList<AlmacenTraspaso>();
		if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L)
			return;
		if (! "fecha".equals(this.campoBusqueda) && "".equals(this.valorBusqueda))
			return;
		buscar();
	}
	
	// ------------------------------------------------------------------------------------
	// EMPLEADOS
	// ------------------------------------------------------------------------------------
	
	private void cargarEmpleados() {
		this.listEmpleados = this.ifzEmpleado.findAll();
		this.listEmpleados = this.listEmpleados != null ? this.listEmpleados : new ArrayList<Empleado>();
		
		Collections.sort(this.listEmpleados, new Comparator<Empleado>() {
			@Override
			public int compare(Empleado o1, Empleado o2) {
				return o1.getNombre().compareTo(o2.getNombre());
			}
		});

		this.listEmpleadosItems = new ArrayList<SelectItem>();
		for (Empleado var : this.listEmpleados) 
			this.listEmpleadosItems.add(new SelectItem(var.getId(), var.getNombre()));
	}

	public void seleccionaEncargado() {
		this.pojoTraspaso.setRecibe(null);
		if (this.pojoTraspaso.getAlmacenDestino() == null || this.pojoTraspaso.getAlmacenDestino().getId() == null || this.pojoTraspaso.getAlmacenDestino().getId() <= 0L)
			return;
		if (this.pojoTraspaso.getAlmacenDestino().getIdEncargado() <= 0L)
			return;
		
		for (Empleado e : this.listEmpleados) {
			if (this.pojoTraspaso.getAlmacenDestino().getIdEncargado() == e.getId().longValue()) {
				this.pojoTraspaso.setRecibe(e);
				break;
			}
		}
	}
	
	// --------------------------------------------------------------------
	// BUSQUEDA OBRAS 
	// --------------------------------------------------------------------

	public void nuevaBusquedaObras() {
		Map<String,String> params = null;
		control();
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.valorOpcionBusquedaObras = 0;
		
		this.numPaginaObras = 1;
		this.listObrasPrincipales = new ArrayList<Obra>();
		
		this.settingObraOrigen = false;
		params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (params.containsKey("settingObraOrigen"))
			this.settingObraOrigen = ("1".equals(params.get("settingObraOrigen")));
	}

	public void buscarObras() {
    	try {
    		control();
    		this.campoBusquedaObras = (this.campoBusquedaObras != null && ! "".equals(this.campoBusquedaObras.trim())) ? this.campoBusquedaObras.trim() : this.tiposBusquedaObras.get(0).getValue().toString();
    		this.valorBusquedaObras = (this.valorBusquedaObras != null && ! "".equals(this.valorBusquedaObras.trim())) ? this.valorBusquedaObras.trim() : "";
    		this.valorOpcionBusquedaObras = this.almacenTrabajo.getTipo() > 2 ? 4 : 0;

			this.numPaginaObras = 1;
			if (this.settingObraOrigen)
				this.listObrasPrincipales = this.ifzObras.findLikePropertyByAlmacen(this.campoBusquedaObras, this.valorBusquedaObras, this.almacenTrabajo.getId(), 0);
			else
				this.listObrasPrincipales = this.ifzObras.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, 0L, 0L, this.valorOpcionBusquedaObras, (this.valorOpcionBusquedaObras == 4), false, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Todas, "nombre", 0);

    		this.listObrasPrincipales = this.listObrasPrincipales != null ? this.listObrasPrincipales : new ArrayList<Obra>();
    		System.out.println("campoBusquedaObras " + campoBusquedaObras);
    		System.out.println("valorBusquedaObras " + valorBusquedaObras);
    		System.out.println("valorOpcionBusquedaObras " + valorOpcionBusquedaObras);
    		System.out.println("settingObraOrigen " + settingObraOrigen);
    		if (this.listObrasPrincipales.isEmpty()) 
				control(2, "Busqueda sin resultados");
    		log.info(this.listObrasPrincipales.size() + " Obra encontradas");
    	} catch (Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
    	} 
    }

	public void seleccionarObra() {
		ObraAlmacenes item = null;
		String mensaje = "";
		long idSucursal = 0;
		
		try {
			control();
			// Compruebo seleccion de Obra
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Obra seleccionada");
				return;
			}
			
			if (this.settingObraOrigen) {
				this.pojoObraOrigen = new Obra();
				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
				BeanUtils.copyProperties(this.pojoObraOrigen, this.pojoObra);
				if (! "DE".equals(this.tipoTraspaso)) {
					this.pojoObra = null;
					return;
				}
			}
			
			this.pojoTraspaso.setAlmacenDestino(null);
			seleccionaEncargado();
			idSucursal = this.almacenTrabajo.getIdSucursal();
			if (idSucursal == 3)
				idSucursal = 1;

			if ("TR TZ".contains(this.tipoTraspaso)) {
				// Recuperamos la bodega asignada a la Obra
				item = obraBodega(this.pojoObra);
				mensaje = "La Obra seleccionada no tiene asignada ninguna Bodega.\n" + this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
				this.bodegaBloqueada = true;
				if (item == null || item.getId() == null || item.getId() <= 0L) {
					if ("".equals(mensaje))
						mensaje = "La Obra seleccionada no tiene asignado ninguna Bodega.\n" + this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
					control(-1, mensaje);
					return;
				}
			} else if ("DE".contains(this.tipoTraspaso)) {
				// Recuperamos el Almacen (principal) asignado a la Obra
				item = obraAlmacen(this.pojoObra); 
				mensaje = "La Obra seleccionada no tiene asignado ningun Almacen.\n" + this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
				this.bodegaBloqueada = false;
				if (item == null || item.getId() == null || item.getId() <= 0L) {
					if ("".equals(mensaje))
						mensaje = "La Obra seleccionada no tiene asignado ningun Almacen principal.\n" + this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
					control(-1, mensaje);
					return;
				}
			}
			
			if (this.almacenTrabajo.getId().longValue() == item.getIdAlmacen()) {
				if ("TZ".equals(this.tipoTraspaso)) {
					control(-1, "La Obra seleccionada tiene asignado la misma Bodega para el origen y el destino del material.\nNo es necesario hacer el movimiento, simplemente haga un salida a Obra");
					return;
				}
				
				control(-1, "La Obra seleccionada tiene asignado el mismo Almacen/Bodega para el origen y el destino del material");
				return;
			}
			
			// Asignamos datos de Obra seleccionada
			this.pojoTraspaso.setIdObra(this.pojoObra.getId());
			this.pojoTraspaso.setNombreObra(this.pojoObra.getNombre());
			
			// Filtramos y asignamos Almacen/Bodega
			filtrarAlmacenes();
			setIdAlmacenDestino(item.getIdAlmacen());
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Bodega asignada a la Obra seleccionada", e);
		}
	}
	
	//  -----------------------------------------------------------------------------------------
	// BUSQUEDA PRODUCTOS
	//  -----------------------------------------------------------------------------------------

	public void nuevaBusquedaProductos() {
		control();
		this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
		this.valorBusquedaProductos = "";
		this.admvaBusquedaProductos = false;
		this.idFamilia = 0;

		this.numPaginaProductos = 1;
		this.listaBusquedaProductos = new ArrayList<AlmacenProductoExt>(); 
		this.pojoProductoBusqueda = new AlmacenProductoExt();
		this.operacionCompleta = true;
		
		if (this.pojoTraspaso == null)
			return;
		if (this.pojoTraspaso.getAlmacenOrigen() == null || this.pojoTraspaso.getAlmacenOrigen().getId() == null || this.pojoTraspaso.getAlmacenOrigen().getId() <= 0L) 
			control(-10, "Debe seleccionar almacén de Salida");
	}
	
	public void buscarProductos() {
		int tipoMaestro = 0;
		
		try {
			control();
    		this.campoBusquedaProductos = (this.campoBusquedaProductos != null && ! "".equals(this.campoBusquedaProductos.trim())) ? this.campoBusquedaProductos.trim() : this.tiposBusquedaProductos.get(0).getValue().toString();
    		this.valorBusquedaProductos = (this.valorBusquedaProductos != null && ! "".equals(this.valorBusquedaProductos.trim())) ? this.valorBusquedaProductos.trim() : "";
    		tipoMaestro = this.pojoTraspaso.getAlmacenOrigen().getTipo() > 2 ? 2 : 1;

    		this.numPaginaProductos = 1;
			this.listaBusquedaProductos = this.ifzAlmacenProducto.findExtExistentes(this.pojoTraspaso.getAlmacenOrigen().getId(), this.campoBusquedaProductos, this.valorBusquedaProductos, this.idFamilia, tipoMaestro, 0, false);
			this.listaBusquedaProductos = this.listaBusquedaProductos != null ? this.listaBusquedaProductos : new ArrayList<AlmacenProductoExt>(); 

			if (this.listaBusquedaProductos.isEmpty()) 
				control(2, "La busqueda no devolvio resultados");
			// Ordenamos por descripcion
			Collections.sort(this.listaBusquedaProductos, new Comparator<AlmacenProductoExt>() {
				@Override
				public int compare(AlmacenProductoExt o1, AlmacenProductoExt o2) {
					return o1.getProducto().getDescripcion().compareTo(o2.getProducto().getDescripcion());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Productos del Almacen seleccioando", e);
		} 
	}

	public void seleccionarProducto() {
		TraspasoDetalleExt detalle = null;
		ProductoExt producto = null;

		try {
			control();
			if (this.pojoProductoBusqueda == null || this.pojoProductoBusqueda.getId() == null || this.pojoProductoBusqueda.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar el Producto seleccionado");
				return;
			}

			this.listaBusquedaProductos.remove(this.pojoProductoBusqueda);
			if (! validaAgregarProducto()) 
				return;

			producto = this.ifzProductos.convertir(this.pojoProductoBusqueda.getProducto()); //.findExtById(this.pojoProductoBusqueda.getId()); 
			producto.setExistencia(this.pojoProductoBusqueda.getExistencia());

			detalle = new TraspasoDetalleExt();
			detalle.setPrecioUnitario(this.pojoProductoBusqueda.getPrecioUnitario());
			detalle.setIdProducto(producto);
			detalle.setCantidad(1);
			this.traspasoDetalles.add(detalle); 

			//Se crea otro objeto para evitar problemas de clonacion en Java
			detalle = new TraspasoDetalleExt();	
			detalle.setPrecioUnitario(this.pojoProductoBusqueda.getPrecioUnitario());
			detalle.setIdProducto(producto);
			detalle.setCantidad(1);
			this.listaDetalleCantidades.add(detalle);	//la lista de respaldo. En el campo cantidad, se guardará la cantidad que existe de producto

			this.pojoProductoBusqueda = new AlmacenProductoExt();
			this.cantidadProductoDetalles = this.traspasoDetalles.size();
			if (this.almacenAnterior == 0) {	//Asignar por primera vez el almacen
				this.setAlmacenAnterior(this.pojoTraspaso.getAlmacenOrigen().getId());
				log.info("Asignar almacen anterior: " + this.almacenAnterior);
			}

			this.operacionCompleta = true;
			control(false, 4, "Producto Añadido", null);
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Productos del Almacen seleccioando", e);
		}
	}

	public void quitarProducto() {
		Map<String,String> params = null;
		long idProductoBorrar = 0L;
		
		try {
			control();
			params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			idProductoBorrar = (params.containsKey("idProductoBorrar") ? Long.valueOf(params.get("idProductoBorrar")) : 0L);
			if (idProductoBorrar <= 0L) {
				control(-1, "Ocurrio un problema al intentar eliminar el Producto indicado");
				return;
			}
			
			for (TraspasoDetalleExt detalle : this.traspasoDetalles) {
				if (idProductoBorrar == detalle.getIdProducto().getId().longValue()) {
					this.traspasoDetalles.remove(detalle);
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Producto seleccionado", e);
		}
	}

	private boolean validaAgregarProducto() {
		control();
		if (isProductoEnLista()) {
			control(-11, "El producto ya se encuentra en la lista");
			return false;
		}
		
		return true;
	}

	// ------------------------------------------------------------------------------------
	// FAMILIAS
	// ------------------------------------------------------------------------------------
	
	private void cargarFamilias() {
		HashMap<String, Object> params = null;
		List<ConValores> listAux1 = null;
		List<ConValores> listAux2 = null;
		long idMaestro = 0L;
		
		try {
			this.listFamilias = new ArrayList<ConValores>();
			this.listFamiliasItems = new ArrayList<SelectItem>();
			
			// Recuperamos los Maestros disponibles
			listAux1 = this.ifzConValores.findAll(this.pojoGpoMaestro);
			listAux1 = listAux1 != null ? listAux1 : new ArrayList<ConValores>();
			for (ConValores var : listAux1) {
				if (! "1".equals(var.getValor()))
					continue;
				idMaestro = var.getId();
				break;
			}
			
			if (idMaestro <= 0L)
				return;
			
			// Recuperamos las especialidades del Maestro elegido
			params = new HashMap<String, Object>();
			params.put("grupoValorId.id", String.valueOf(this.pojoGpoEspecialidades.getId()));
			params.put("atributo1", String.valueOf(idMaestro));
			listAux1 = this.ifzConValores.findByProperties(params, 0);
			listAux1 = listAux1 != null ? listAux1 : new ArrayList<ConValores>();
			if (listAux1.isEmpty()) 
				return;
			
			// Recuperamos las familias de cada especialidad
			for (ConValores especialidad : listAux1) {
				params.clear();
				params.put("grupoValorId.id", String.valueOf(this.pojoGpoFamilias.getId()));
				params.put("atributo1", String.valueOf(especialidad.getId()));
				listAux2 = this.ifzConValores.findByProperties(params, 0);
				if (listAux2 == null || listAux2.isEmpty())
					continue;
				this.listFamilias.addAll(listAux2);
			}
			
			// Ordeno las familias por descripcion (nombre)
			Collections.sort(this.listFamilias, new Comparator<ConValores>() {
			    	@Override
			        public int compare(ConValores o1, ConValores o2) {
			    		return o1.getDescripcion().compareTo(o2.getDescripcion());
			        }
			});
			
			for (ConValores var : this.listFamilias) {
				if (var.getValor().equals(var.getDescripcion()))
					this.listFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion()));
				else
					this.listFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar las Familias", e);
		}
	}
	
	//  -----------------------------------------------------------------------------------------
	// PROPIEDADES
	//  -----------------------------------------------------------------------------------------

	public String getTitulo() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getId() != null && this.pojoTraspaso.getId() > 0L) {
			if ("TX".equals(this.tipoTraspaso))
				return "Traspaso de Almacen a Almacen " + this.pojoTraspaso.getId();
			if ("TR".equals(this.tipoTraspaso))
				return "Traspaso de Almacen a Bodega " + this.pojoTraspaso.getId();
			if ("TZ".equals(this.tipoTraspaso))
				return "Traspaso de Bodega a Bodega " + this.pojoTraspaso.getId();
			if ("DE".equals(this.tipoTraspaso))
				return "Devolucion a Almacen " + this.pojoTraspaso.getId();
			return "Traspaso de Almacen/Bodega " + this.pojoTraspaso.getId();
		} 
		
		if ("TX".equals(this.tipoTraspaso))
			return "Traspaso de Almacen a Almacen";
		if ("TR".equals(this.tipoTraspaso))
			return "Traspaso de Almacen a Bodega";
		if ("TZ".equals(this.tipoTraspaso))
			return "Traspaso de Bodega a Bodega";
		if ("DE".equals(this.tipoTraspaso))
			return "Devolucion a Almacen";
		return "Traspaso de Almacen/Bodega";
	}
	
	public void setTitulo(String value) {}

	public String getObraOrigen() {
		if (this.pojoObraOrigen != null && this.pojoObraOrigen.getId() != null && this.pojoObraOrigen.getId() > 0L)
			return this.pojoObraOrigen.getId() + " - " + this.pojoObraOrigen.getNombre();
		return "";
	}
	
	public void setObraOrigen(String value) {}

	public String getObra() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getIdObra() > 0L)
			return this.pojoTraspaso.getIdObra() + " - " + this.pojoTraspaso.getNombreObra();
		return "";
	}
	
	public void setObra(String value) {}

	public String getBodegaDestino() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getAlmacenDestino() != null && this.pojoTraspaso.getAlmacenDestino().getId() != null && this.pojoTraspaso.getAlmacenDestino().getId() > 0L) 
			return this.pojoTraspaso.getAlmacenDestino().getNombre();
		return "";
	}
	
	public void setBodegaDestino(String value) {}

	public long getIdEmpleadoRecibe() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getRecibe() != null && this.pojoTraspaso.getRecibe().getId() != null && this.pojoTraspaso.getRecibe().getId() > 0L) 
			return this.pojoTraspaso.getRecibe().getId();
		return 0L;
	}

	public void setIdEmpleadoRecibe(long idEmpleado) {
		if (idEmpleado <= 0) 
			return;
		
		for (Empleado e : this.listEmpleados) {
			if (e.getId().longValue() == idEmpleado) {
				this.pojoTraspaso.setRecibe(e);
				break;
			}
		}
	}

	public String getAlmacenOrigen() {
		if (this.pojoTraspaso != null && pojoTraspaso.getAlmacenOrigen() != null && this.pojoTraspaso.getAlmacenOrigen().getId() != null && this.pojoTraspaso.getAlmacenOrigen().getId() > 0L)
			return this.pojoTraspaso.getAlmacenOrigen().getIdentificador() + " " + this.pojoTraspaso.getAlmacenOrigen().getNombre() + " (" + this.pojoTraspaso.getAlmacenOrigen().getId() + ")";
		return "NA";
	}
	
	public void setAlmacenOrigen(String value) {}

	public String getAlmacenOrigenTipo() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getAlmacenOrigen() != null && this.pojoTraspaso.getAlmacenOrigen().getId() != null && this.pojoTraspaso.getAlmacenOrigen().getId() > 0L)
			return this.pojoTraspaso.getAlmacenOrigen().getTipo() == 1 || this.pojoTraspaso.getAlmacenOrigen().getTipo() == 3 ? "AL" : "BO";
		return "";
	}
	
	public void setAlmacenOrigenTipo(String value) {}

	public long getIdAlmacenDestino() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getAlmacenDestino() != null && this.pojoTraspaso.getAlmacenDestino().getId() != null && this.pojoTraspaso.getAlmacenDestino().getId() > 0L)
			return this.pojoTraspaso.getAlmacenDestino().getId();
		return 0L;
	}
	
	public void setIdAlmacenDestino(long idAlmacen) {
		if (idAlmacen <= 0)
			return;

		for (Almacen almacen : this.listAlmacenes) {
			if (idAlmacen == almacen.getId().longValue()) {
				this.pojoTraspaso.setAlmacenDestino(almacen);
				seleccionaEncargado();
				break;
			}
		}
	}
	
	public long getPojoTraspaso() {
		return idTraspaso;
	}

	public void setPojoTraspaso(long pojoTraspaso) {
		this.idTraspaso = pojoTraspaso;
	}

	public Date getFecha() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getFecha() != null) 
			return this.pojoTraspaso.getFecha(); 
		return Calendar.getInstance().getTime();
	}
	
	public void setFecha(Date fecha) {
		this.pojoTraspaso.setFecha(fecha);
	}

	public String getMovimientosSalida() {
		return "Salida: " + this.movimientosSalida;
	}

	public void setMovimientosSalida(String idMovimientoSalida) {}

	public String getMovimientosEntrada() {
		return "Entrada: " + this.movimientosEntrada;
	}

	public void setMovimientosEntrada(String idMovimientoEntrada) {}
	
	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getNumPaginaProductos() {
		return numPaginaProductos;
	}

	public void setNumPaginaProductos(int numPaginaProductos) {
		this.numPaginaProductos = numPaginaProductos;
	}

	public boolean isBand() {
		return operacionCancelada;
	}

	public void setBand(boolean band) {
		this.operacionCancelada = band;
	}

	public boolean isOperacionCompleta() {
		return operacionCompleta;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCompleta = operacionCompleta;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public boolean isPuedeCancelar() {
		return puedeCancelar;
	}

	public void setPuedeCancelar(boolean puedeCancelar) {
		this.puedeCancelar = puedeCancelar;
	}
	
	public boolean getPuedeDescontar() {
		return "ADMINISTRADOR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());
	}
	
	public void setPuedeDescontar(boolean value) {}
	
	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public List<SelectItem> getListaCampoBusqueda() {
		return listaCampoBusqueda;
	}

	public void setListaCampoBusqueda(List<SelectItem> listaCampoBusqueda) {
		this.listaCampoBusqueda = listaCampoBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}
	
	public List<SelectItem> getListAlmacenesItems() {
		return listAlmacenesItems;
	}

	public void setListAlmacenesItems(List<SelectItem> listAlmacenesItems) {
		this.listAlmacenesItems = listAlmacenesItems;
	}

	/*public List<SelectItem> getListAlmacenesTrabajoItems() {
		return listAlmacenesItems;
	}

	public void setListAlmacenesTrabajoItems(List<SelectItem> listAlmacenesTrabajoItems) {
		this.listAlmacenesItems = listAlmacenesTrabajoItems;
	}*/
	
	public List<Empleado> getListaEmpleados() {
		return listEmpleados;
	}

	public void setListaEmpleados(List<Empleado> listaEmpleados) {
		this.listEmpleados = listaEmpleados;
	}

	public List<SelectItem> getListEmpleadosItems() {
		return listEmpleadosItems;
	}

	public void setListEmpleadosItems(List<SelectItem> listEmpleadosItems) {
		this.listEmpleadosItems = listEmpleadosItems;
	}

	public List<AlmacenTraspaso> getListaTraspasoGrid() {
		return listTraspasos;
	}

	public void setListaTraspasoGrid(List<AlmacenTraspaso> listaTraspasoGrid) {
		this.listTraspasos = listaTraspasoGrid;
	}

	public List<TraspasoDetalleExt> getListaTraspasoDetalleGrid() {
		return traspasoDetalles;
	}

	public void setListaTraspasoDetalleGrid(List<TraspasoDetalleExt> listaTraspasoDetalleGrid) {
		this.traspasoDetalles = listaTraspasoDetalleGrid;
	}

	public List<AlmacenProductoExt> getListaBusquedaProductos() {
		return listaBusquedaProductos;
	}

	public void setListaBusquedaProductos(List<AlmacenProductoExt> listaBusquedaProductos) {
		this.listaBusquedaProductos = listaBusquedaProductos;
	}

	public List<SelectItem> getTiposBusquedaProductos() {
		return tiposBusquedaProductos;
	}

	public void setTiposBusquedaProductos(List<SelectItem> tiposBusquedaProductos) {
		this.tiposBusquedaProductos = tiposBusquedaProductos;
	}

	public String getValorBusquedaProductos() {
		return valorBusquedaProductos;
	}

	public void setValorBusquedaProductos(String valorBusquedaProductos) {
		this.valorBusquedaProductos = valorBusquedaProductos;
	}

	public AlmacenProductoExt getPojoProductoBusqueda() {
		return pojoProductoBusqueda;
	}

	public void setPojoProductoBusqueda(AlmacenProductoExt pojoProductoBusqueda) {
		this.pojoProductoBusqueda = pojoProductoBusqueda;
	}

	public String getCampoBusquedaProductos() {
		return campoBusquedaProductos;
	}

	public void setCampoBusquedaProductos(String campoBusquedaProductos) {
		this.campoBusquedaProductos = campoBusquedaProductos;
	}

	public int getValorOpcionBusquedaProductos() {
		return valorOpcionBusquedaProductos;
	}

	public void setValorOpcionBusquedaProductos(int valorOpcionBusquedaProductos) {
		this.valorOpcionBusquedaProductos = valorOpcionBusquedaProductos;
	}

	public TraspasoDetalleExt getPojoTraspasoDetalleEliminar() {
		return pojoTraspasoDetalleEliminar;
	}

	public void setPojoTraspasoDetalleEliminar(TraspasoDetalleExt pojoTraspasoDetalleEliminar) {
		this.pojoTraspasoDetalleEliminar = pojoTraspasoDetalleEliminar;
	}
	
	public int getCantidadProductoDetalles() {
		return cantidadProductoDetalles;
	}

	public void setCantidadProductoDetalles(int cantidadProductoDetalles) {
		this.cantidadProductoDetalles = cantidadProductoDetalles;
	}

	public TraspasoDetalleExt getTraspasoSeleccionado() {
		return this.traspasoSeleccionado;
	}

	public void setTraspasoSeleccionado(TraspasoDetalleExt traspasoSeleccionado) {
		this.traspasoSeleccionado = traspasoSeleccionado;
	}

	public double getCantidadProductoSeleccionado() {
		return cantidadProductoSeleccionado;
	}

	public void setCantidadProductoSeleccionado(double cantidadProductoSeleccionado) {
		this.cantidadProductoSeleccionado = cantidadProductoSeleccionado;
	}

	public long getAlmacenAnterior() {
		return almacenAnterior;
	}

	public void setAlmacenAnterior(long almacenAnterior) {
		this.almacenAnterior = almacenAnterior;
	}

	public List<SelectItem> getListTiposTraspasos() {
		return listTiposTraspasos;
	}

	public void setListTiposTraspasos(List<SelectItem> listTiposTraspasos) {
		this.listTiposTraspasos = listTiposTraspasos;
	}

	public String getTipoTraspaso() {
		return tipoTraspaso;
	}

	public void setTipoTraspaso(String tipoTraspaso) {
		this.tipoTraspaso = tipoTraspaso;
	}

	public int getPagDetalles() {
		return pagDetalles;
	}

	public void setPagDetalles(int pagDetalles) {
		this.pagDetalles = pagDetalles;
	}
	
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

	public boolean isAdministrativo() {
		return PERFIL_ADMINISTRATIVO;
	}

	public void setAdministrativo(boolean pAdministrativo) { }

	public boolean isAdmvaBusquedaProductos() {
		return admvaBusquedaProductos;
	}

	public void setAdmvaBusquedaProductos(boolean admvaBusquedaProductos) {
		this.admvaBusquedaProductos = admvaBusquedaProductos;
	}

	public int getTraspasoEstatus() {
		return traspasoEstatus;
	}

	public void setTraspasoEstatus(int traspasoEstatus) {
		this.traspasoEstatus = traspasoEstatus;
	}
	
	public Obra getPojoObra() {
		return pojoObra;
	}
	
	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public Obra getPojoObraOrigen() {
		return pojoObraOrigen;
	}
	
	public void setPojoObraOrigen(Obra pojoObraOrigen) {
		this.pojoObraOrigen = pojoObraOrigen;
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

	public int getValorOpcionBusquedaObras() {
		return valorOpcionBusquedaObras;
	}

	public void setValorOpcionBusquedaObras(int valorOpcionBusquedaObras) {
		this.valorOpcionBusquedaObras = valorOpcionBusquedaObras;
	}

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}
	
	public boolean isBodegaBloqueada() {
		return bodegaBloqueada;
	}

	public void setBodegaBloqueada(boolean bodegaBloqueada) {
		this.bodegaBloqueada = bodegaBloqueada;
	}

	public long getIdAlmacenTrabajo() {
		if (this.almacenTrabajo != null && this.almacenTrabajo.getId() != null && this.almacenTrabajo.getId() > 0L)
			return this.almacenTrabajo.getId();
		return 0;
	}

	public void setIdAlmacenTrabajo(long idAlmacenTrabajo) {
		if (idAlmacenTrabajo <= 0L) {
			this.almacenTrabajo = null;
			return;
		}

		if (this.almacenTrabajo != null && validarID(this.almacenTrabajo.getId()) > 0L && this.almacenTrabajo.getId().longValue() == idAlmacenTrabajo)
			return;

		for (Almacen almacen : this.listAlmacenes) {
			if (idAlmacenTrabajo == almacen.getId().longValue()) {
				this.almacenTrabajo = almacen;
				break;
			}
		}

		// Actualizamos permisos si corresponde
		if (! "ADMINISTRADOR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) {
			this.permisos = new PermisoExt(this.permisosBase, 0L);
			for (Almacen almacen : this.listAlmacenesTrabajo) {
				if (idAlmacenTrabajo == almacen.getId().longValue()) {
					this.permisos = new PermisoExt(this.permisosBase, idAlmacenTrabajo);
					break;
				}
			}
		}
	}
	
	public String getAlmacenMovimiento() {
		if (this.almacenTrabajo != null && this.almacenTrabajo.getId() != null && this.almacenTrabajo.getId() > 0L)
			return this.almacenTrabajo.getIdentificador() + " " + this.almacenTrabajo.getNombre() + " (" +  this.almacenTrabajo.getId() + ")";
		return "";
	}
	
	public void setAlmacenMovimiento(String value) {}
	
	public String getAlmacenMovimientoTipo() {
		if (this.almacenTrabajo != null && this.almacenTrabajo.getId() != null && this.almacenTrabajo.getId() > 0L)
			return this.almacenTrabajo.getTipo() == 1 || this.almacenTrabajo.getTipo() == 3 ? "AL" : "BO";
		return "";
	}
	
	public void setAlmacenMovimientoTipo(String value) {}

	public String getEmpleadoEntrega() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getEntrega() != null && this.pojoTraspaso.getEntrega().getId() != null && this.pojoTraspaso.getEntrega().getId() > 0L) 
			return this.pojoTraspaso.getEntrega().getClave() + " - " + this.pojoTraspaso.getEntrega().getNombre() + " (" + this.pojoTraspaso.getEntrega().getId() + ")";
		if (this.pojoEmpleadoUsuario != null)
			return this.pojoEmpleadoUsuario.getClave() + " - " + this.pojoEmpleadoUsuario.getNombre() + " (" + this.pojoEmpleadoUsuario.getId() + ")";
		return "";
	}

	public void setEmpleadoEntrega(String idEmpleado) {}

	public boolean getDebugging() {
		return this.isDebug;
	}
	
	public void setDebugging(boolean value) {}
	
	public boolean getPermisoAdmin() {
		return "ADMINISTRADOR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());
	}
	
	public void setPermisoAdmin(boolean value) {}
}

// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA    |        AUTOR       | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//    2.0  | 2016-10-19  | Javier Tirado      | Modifico metodo cargarCboAlmacenDestino. Filtro solo alamcenes principales

