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
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraEmpleado;
import net.giro.adp.logica.ObraEmpleadoRem;
import net.giro.adp.logica.ObraRem;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.comun.TipoMovimientoInventario;
import net.giro.inventarios.comun.TipoMovimientoReferencia;
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

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="salidasAlmacenAction")
public class SalidasAlmacenAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SalidasAlmacenAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private Permiso permisosBase;
	private PermisoExt permisos;
	private HttpSession httpSession;
	private AlmacenMovimientosRem ifzMovimientos;
	private MovimientosDetalleRem ifzMovimientosDetalle;
	private ObraEmpleadoRem ifzObraEmpleados;
	private AlmacenProductoRem ifzAlmacenProducto;
	private ReportesRem ifzReportes;
	private ConGrupoValoresRem ifzGpoValores;
	private ConValoresRem ifzConValores;
	private int numPaginaOrdenCompra;
	private boolean puedeEditar;
	private List<AlmacenMovimientos> listMovimientos;
	private int currentIndex;
	private long almacenAnterior;
	// Almacenes
	private AlmacenRem ifzAlmacen;
	private List<Almacen> listAlmacenes;
	private List<SelectItem> listAlmacenesItems;
	private boolean almacenesAdministrativos;
	// Busqueda principal
	private List<SelectItem> listaCampoBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
	private int numPagina;
	// Busqueda obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private List<SelectItem> tiposBusquedaObras;
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int numPaginaObras;
	//Variables para esta el movimiento de salida
	private List<MovimientosDetalleExt> listDetalles;
	private List<MovimientosDetalleExt> listDetallesEliminados;
	private int numeroItems;
	//Busqueda Productos
	private ProductoRem ifzProductos;
	private List<Producto> listProductos; 
	private Producto pojoProductoBusqueda;
	private List<SelectItem> tiposBusquedaProductos;	
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private int valorOpcionBusquedaProductos;
	private int numPaginaProductos;
	// Familia
	private ConGrupoValores pojoGpoMaestro;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	private long idFamilia;
	//Variables para el movimiento
	private long idMovimiento;
	private AlmacenMovimientosExt pojoMovimiento; 
	private Obra pojoObra;
	private MovimientosDetalleExt pojoMovimientoDetalleEliminar;
	private MovimientosDetalleExt productoSalidaSeleccionado;
	private MovimientosDetalleExt movimientoSeleccionado;
	private double cantidadProductoSeleccionado;
	private int rowSeleccionado;
    // PEFILES
	private boolean PERFIL_ADMINISTRATIVO;
	// EMPLEADO-USUARIO
	private EmpleadoRem ifzEmpleado;
	private List<Empleado> listEmpleados;
	private List<SelectItem> listEmpleadosEntrega;
	private List<SelectItem> listEmpleadosRecibe;
	private EmpleadoExt pojoEmpleadoUsuario;
	private List<Almacen> listAlmacenesTrabajo;
	private Almacen almacenTrabajo;
	private List<Long> listPuestosValidos;
	// TRASPASO
	private AlmacenTraspasoRem ifzTraspaso;
	private TraspasoDetalleRem ifzTraspasoDetalle;
	private List<TraspasoDetalleExt> listTraspasoDetalleGrid;
	private AlmacenTraspasoExt pojoTraspaso;
	private int pagTraspasoDetalles;
	private int TRASPASO_ALMACEN;
	private int TRASPASO_DEVOLUCION;
	private int TRASPASO_ALMACEN_ALMACEN;
	private List<SelectItem> listTiposTraspasos;
	private int tipoTraspaso;
	private boolean baseTraspaso;
	// control
	private boolean operacionCompleta;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
    
	public SalidasAlmacenAction() {
		InitialContext ctx = null;
		ValueExpression ve = null;
		FacesContext fc = null;
		Application app = null;
		String resValue = "";
		String[] splitted = null;

		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
	
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
			
			// EVALUACION DE PERFILES
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.PERFIL_ADMINISTRATIVO = ((resValue != null && "S".equals(resValue)) ? true : false);

			ctx = new InitialContext();
			this.ifzMovimientos = (AlmacenMovimientosRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzMovimientosDetalle = (MovimientosDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");
			this.ifzProductos = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzAlmacen = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObraEmpleados = (ObraEmpleadoRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraEmpleadoFac!net.giro.adp.logica.ObraEmpleadoRem");
			this.ifzProductos = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzAlmacenProducto = (AlmacenProductoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			this.ifzEmpleado = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzGpoValores = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzTraspaso = (AlmacenTraspasoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
			this.ifzTraspasoDetalle = (TraspasoDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			
			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMovimientosDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObraEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacenProducto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspaso.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspasoDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			
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
			this.listaCampoBusqueda.add(new SelectItem("c.clave", "Codigo"));
			this.listaCampoBusqueda.add(new SelectItem("a.id", "ID"));
			this.campoBusqueda = this.listaCampoBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.fechaBusqueda = Calendar.getInstance().getTime();
			
			// Busqueda obras
			this.listObras = new ArrayList<Obra>();
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
			
			// Busqueda Productos
			this.listProductos = new ArrayList<Producto>();
			this.tiposBusquedaProductos = new ArrayList<SelectItem>();
			this.tiposBusquedaProductos.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Nombre"));
			this.tiposBusquedaProductos.add(new SelectItem("clave", "Clave"));
			this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
			nuevaBusquedaProductos();
	
			// TRASPASOS
			this.TRASPASO_ALMACEN = 1;
			this.TRASPASO_DEVOLUCION = 2;
			this.TRASPASO_ALMACEN_ALMACEN = 3;
			this.listTiposTraspasos = new ArrayList<SelectItem>();
			this.listTiposTraspasos.add(new SelectItem(this.TRASPASO_ALMACEN, "Traspaso de Almacen a Bodega"));
			this.listTiposTraspasos.add(new SelectItem(this.TRASPASO_ALMACEN_ALMACEN, "Traspaso de Bodega a Bodega"));
			this.listTiposTraspasos.add(new SelectItem(this.TRASPASO_DEVOLUCION, "Devolucion a Almacen"));
			this.tipoTraspaso = (int) this.listTiposTraspasos.get(0).getValue();

			cargarAlmacenes();
			this.numPagina = 1;
			this.listMovimientos = new ArrayList<AlmacenMovimientos>();
			this.productoSalidaSeleccionado = new MovimientosDetalleExt();
			if (! this.listAlmacenesTrabajo.isEmpty())
				setIdAlmacenTrabajo(this.listAlmacenesTrabajo.get(0).getId());
			nuevo();
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar el Bean", e);
		}
	}
	
	public void buscar() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String value = "";
		
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			/*if (! this.permisos.getConsultar()) {
				control(-1, "No tiene permitido Consultar informacion");
				controlLog("301 - No tiene permitido Consultar informacion");
				return;
			}*/

			if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
				control(-1, "Debe seleccionar un Almacen/Bodega para poder consultar los Traspasos");
				return;
			}
			
			this.campoBusqueda = (this.campoBusqueda != null && ! "".equals(this.campoBusqueda.trim())) ? this.campoBusqueda.trim() : this.listaCampoBusqueda.get(0).getValue().toString();
			this.valorBusqueda = (this.valorBusqueda != null && ! "".equals(this.valorBusqueda.trim())) ? this.valorBusqueda.trim() : "";
			value = ("fecha".equals(this.campoBusqueda)) ? formatter.format(this.fechaBusqueda) : this.valorBusqueda;

    		this.numPagina = 1;
			this.listMovimientos = this.ifzMovimientos.findLikeProperty(this.campoBusqueda, value, this.almacenTrabajo.getId(), TipoMovimientoInventario.SALIDA.ordinal(), "SO", "model.fecha desc, model.id desc", 0);
			this.listMovimientos = this.listMovimientos != null ? this.listMovimientos : new ArrayList<AlmacenMovimientos>();
			if (this.listMovimientos.isEmpty())
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Salidas", e);
    	} 
	}
	
	private void nuevo() {
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
				control(-1, "Debe seleccionar un Almacen/Bodega previamente");
				return;
			}
			
			/*if (! this.permisos.escribir(this.almacenTrabajo.getId())) {
				control(-1, "No tiene permitido Añadir/Editar informacion");
				controlLog("301 - No tiene permitido Añadir/Editar informacion");
				return;
			}*/
			
			this.pojoObra = new Obra();
			this.pojoMovimiento = new AlmacenMovimientosExt();
			this.puedeEditar = true;
			this.baseTraspaso = false;
			this.pojoMovimiento.setAlmacen(this.almacenTrabajo);
			this.pojoMovimiento.setEntrega(this.ifzEmpleado.findById(this.almacenTrabajo.getIdEncargado()));
			this.listDetalles = null;
			this.pojoTraspaso = null;
			this.listTraspasoDetalleGrid = null;
			this.numeroItems = 0;
			this.listDetalles = new ArrayList<MovimientosDetalleExt>();
			this.listDetallesEliminados = new ArrayList<MovimientosDetalleExt>();
		} catch (Exception e) {
			control("Ocurrio un problema al preparar la nueva Salida de Bodega", e);
		}
	}
	
	public void nuevoMovimiento() {
		control();
		// Validamos permiso de Lectura/Consulta
		if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
			control(-1, "Debe seleccionar un Almacen/Bodega previamente");
			return;
		}
		
		/*if (! this.permisos.escribir(this.almacenTrabajo.getId())) {
			control(-1, "No tiene permitido Añadir/Editar informacion");
			controlLog("301 - No tiene permitido Añadir/Editar informacion");
			return;
		}*/
		
		nuevo();
		cargarFamilias();
		cargarCboEmpleadoEntrega();
		//cargarAlmacenes();
		this.pojoMovimiento.setAlmacen(this.almacenTrabajo);
		setEntrega(this.almacenTrabajo.getIdEncargado());
	}

	public void editar() {
		try {
			control();
			this.puedeEditar = false;
			this.listDetalles = null;
			this.pojoTraspaso = null;
			this.listTraspasoDetalleGrid = null;
			
			this.pojoMovimiento = this.ifzMovimientos.findByIdExt(this.idMovimiento);
			if (this.pojoMovimiento == null || this.pojoMovimiento.getId() == null || this.pojoMovimiento.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Salida indicada");
				return;
			}
			
			// Recuperamos detalles: Salida o Traspaso segun corresponda
			this.baseTraspaso = false;
			if (this.pojoMovimiento.getIdTraspaso() > 0L) {
				this.baseTraspaso = true;
				this.pojoTraspaso = this.ifzTraspaso.findByIdExt(this.pojoMovimiento.getIdTraspaso());
				this.listTraspasoDetalleGrid = this.ifzTraspasoDetalle.findExtAll(this.pojoMovimiento.getIdTraspaso());
				this.listTraspasoDetalleGrid = this.listTraspasoDetalleGrid != null ? this.listTraspasoDetalleGrid : new ArrayList<TraspasoDetalleExt>();
				cargarCboEmpleadoRecibe();
			} else {
				// Recuperamos datos de quien recibe
				this.pojoObra = this.ifzObras.findById(this.pojoMovimiento.getIdObra());
				this.listDetalles = this.ifzMovimientosDetalle.findAllExt(this.pojoMovimiento.getId());
				this.listDetalles = this.listDetalles != null ? this.listDetalles : new ArrayList<MovimientosDetalleExt>();
				cargarEmpleadosObra();
			}

			cargarCboEmpleadoEntrega();
			cargarAlmacenes();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar la Salida indicada", e);
		}
	}

	public void guardar() {
		Long idMovimiento = 0L;
		Long idObra = 0L;
		
		try {
			control();
			if (! validaciones()) 
				return;
			
			if (this.pojoMovimiento.getId() == null || this.pojoMovimiento.getId() <= 0L) {	
				this.pojoMovimiento.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
				this.pojoMovimiento.setTipoEntrada(TipoMovimientoReferencia.SALIDA_OBRA.toString());
				this.pojoMovimiento.setIdObra(this.pojoObra.getId());
				this.pojoMovimiento.setNombreObra(this.pojoObra.getNombre());
				this.pojoMovimiento.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoMovimiento.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoMovimiento.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoMovimiento.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos y agregamos encabezado de movimiento (SALIDA)
				this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoMovimiento.setId(this.ifzMovimientos.save(this.pojoMovimiento));
				this.listMovimientos.add(this.ifzMovimientos.findById(this.pojoMovimiento.getId()));
			} else {
				this.pojoMovimiento.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoMovimiento.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzMovimientos.update(this.pojoMovimiento);
			}
				
			// Actualizo propietario de detalles
			for (MovimientosDetalleExt detalle : this.listDetalles)
				detalle.setIdAlmacenMovimiento(this.pojoMovimiento.getId());
			
			// Guardo detalles 
			this.ifzMovimientosDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMovimientosDetalle.saveOrUpdateListExt(this.listDetalles);
			idMovimiento = this.pojoMovimiento.getId();
			idObra = this.pojoMovimiento.getIdObra();
			nuevoMovimiento();

			// Ahora, afectamos existencias por JMS
			// ----------------------------------------------------------------------------------------------------------------------
			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			if (! this.ifzMovimientos.backOfficeInventario(idMovimiento, String.valueOf(idObra))) 
				control(-1, "Se guardo la Salida, sin embargo, la descarga de material no se pudo realizar.\nFallo proceso BackOffice");
		} catch (Exception e) {
			control("Ocurrio un problema al guardar la Salida", e);
		}
	}

	public void cancelar() {
		Respuesta respuesta = null;
		AlmacenMovimientos movimiento = null;
		
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
			
			movimiento = this.ifzMovimientos.findById(this.pojoMovimiento.getId());
			if (movimiento == null || movimiento.getId() == null || movimiento.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Salida indicada");
				return;
			}
			
			// Cancelamos
			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzMovimientos.cancelar(movimiento);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al intentar Cancelar la Salida indicada\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}
			
			// Actualizo el Traspaso en el listado
			movimiento = (AlmacenMovimientos) respuesta.getBody().getValor("movimiento");
			for (AlmacenMovimientos item : this.listMovimientos) {
				if (movimiento.getId().longValue() == item.getId().longValue()) {
					this.listMovimientos.set(this.listMovimientos.indexOf(item), movimiento);
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Cancelar la Entrada indicada", e);
		}
	}

	public void eliminar() {
		boolean existe = false;

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
		if (this.pojoMovimientoDetalleEliminar == null)
			return;
		
		for (MovimientosDetalleExt md : this.listDetalles) {
			if (md.getProducto().getClave().equals(this.pojoMovimientoDetalleEliminar.getProducto().getClave())) {
				this.listDetalles.remove(md);
				break;
			}
		}

		this.listDetallesEliminados = this.listDetallesEliminados != null ? this.listDetallesEliminados : new ArrayList<MovimientosDetalleExt>();
		if (this.pojoMovimientoDetalleEliminar.getId() != null && this.pojoMovimientoDetalleEliminar.getId() <= 0L) {
			for (MovimientosDetalleExt item : this.listDetallesEliminados) {
				if (this.pojoMovimientoDetalleEliminar.getId().longValue() == item.getId().longValue()) {
					existe = true;
					break;
				}
			}
			
			if (! existe) 
				this.listDetallesEliminados.add(this.pojoMovimientoDetalleEliminar);
			this.pojoMovimientoDetalleEliminar = null;
		}
		
		if (this.listDetalles.size() == 0) {
			this.almacenAnterior = 0;
			this.numeroItems = 0;
		}
	}

	public void reporte() {
		SimpleDateFormat formatter = null;
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			this.pojoMovimiento = this.ifzMovimientos.findByIdExt(this.idMovimiento);
			if (this.pojoMovimiento == null || this.pojoMovimiento.getId() == null || this.pojoMovimiento.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Salida indicada");
				return;
			}
			
			log.info("Imprimiendo Salida ... ");
			formatter = new SimpleDateFormat("yyMMddHH");
			nombreDocumento = "S-[tipo]-[id]-[fecha].[extension]";
			
			// Parametros del reporte
			log.info("Generando parametros ... ");
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idMovimiento", this.pojoMovimiento.getId());

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
				log.error("ERROR INTERNO - " + respuesta.getErrores().getCodigoError() + ": " + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al intentar imprimir la SALIDA\nNo se genero el reporte");
				return;
			} 

			log.info("Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = nombreDocumento.replace("[tipo]", this.pojoMovimiento.getTipoEntrada());
			nombreDocumento = nombreDocumento.replace("[id]", String.format("%1$05d", this.pojoMovimiento.getId()).substring(3));
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
			log.error("Error en Inventarios.EntradasAlmacenAction.reporte()", e);
			control(-1, "Ocurrio un problema al intentar imprimir la Entrada a Almacen");
		} 
	}
	
	public void verificarProducto() {
		
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
	
	public void eliminarProductosGrid() {
		this.listDetalles.clear();
		this.numeroItems = 0;
		this.almacenAnterior = 0;
	}
	
	public void regresarAlmacen() {
		log.info("Almacen anterior:" + this.getIdAlmacen());
	}
	
	private boolean validaAgregarProducto() {
		control();
		if (this.pojoProductoBusqueda == null || this.pojoProductoBusqueda.getId() == null || this.pojoProductoBusqueda.getId() <= 0L)
			return false;
		
		if (comprobarProductoEnLista()) {
			control(-10, "El producto ya se encuentra en la Lista");
			return false;
		}
		
		return true;
	}
	
	private boolean comprobarProductoEnLista() {
		this.listDetalles = this.listDetalles != null ? this.listDetalles : new ArrayList<MovimientosDetalleExt>();
		for (MovimientosDetalleExt p : this.listDetalles) {
			if (this.pojoProductoBusqueda.getClave().equals(p.getProducto().getClave()))
				return true;
		}
		
		return false;
	}
	
	public void validaCantidadProducto(AjaxBehaviorEvent event) {
		int currentTraspasoDetalle = -1;

        try {
			control();
			if (! this.puedeEditar) 
				return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
			
			if (this.listDetalles.isEmpty()) 
				return;
			
			if (event.getComponent().getAttributes().get("currentIndex") == null)
				return;
			currentTraspasoDetalle = (int) event.getComponent().getAttributes().get("currentIndex");
			
			this.movimientoSeleccionado = this.listDetalles.get(currentTraspasoDetalle);
	    	log.info("Elemento seleccionado : " + this.movimientoSeleccionado.getProducto().getId() 
	    			+ ", Producto : " + this.movimientoSeleccionado.getProducto().getDescripcion() 
	    			+ ", Cant : " + this.movimientoSeleccionado.getCantidad());
				this.cantidadProductoSeleccionado = this.ifzAlmacenProducto.findCantidadEnAlmacen(this.pojoMovimiento.getAlmacen().getId(), this.movimientoSeleccionado.getProducto().getId());
			
	    	log.info("Cantidad del producto en Almacen: " + this.cantidadProductoSeleccionado);
	    	
	    	//asignar la cantidad que existe en almacen al produdcto en el campo: cantidadSolicitada
	    	this.setCantidadExistente(this.movimientoSeleccionado.getProducto().getClave(), this.cantidadProductoSeleccionado);
			
			for (MovimientosDetalleExt movimiento : this.listDetalles) {
				if (movimiento.getProducto().getClave().equals(this.movimientoSeleccionado.getProducto().getClave())) {
					log.info("Revisar: Almacen: " + this.pojoMovimiento.getAlmacen().getId() 
							+ ", Producto: " + movimiento.getProducto().getId());
					this.cantidadProductoSeleccionado = this.ifzAlmacenProducto.findCantidadEnAlmacen(this.pojoMovimiento.getAlmacen().getId(), movimiento.getProducto().getId());
					log.info("Cantidad Producto : " + this.cantidadProductoSeleccionado
							+ ", Movimiento Cantidad : " + movimiento.getCantidad()
							+ ", Movimiento Index : " + this.listDetalles.indexOf(movimiento));
				}
			}
        } catch (Exception e) {
			control("Ocurrio un problema al validar la existencia del Producto", e);
		}
	}
	
	private void setCantidadExistente(String claveProducto, double cantidadExistente) {
		for (MovimientosDetalleExt md : this.listDetalles) {
			if (md.getProducto().getClave().equals(claveProducto)) {
				md.setCantidadSolicitada(cantidadExistente);
				break;
			}
		}
	}

	private boolean validaciones() {
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
		
		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
			control(-12, "Seleccione Obra");
			return false;
		}
		
		if (this.pojoMovimiento.getAlmacen() == null)
			this.pojoMovimiento.setAlmacen(this.almacenTrabajo);
		if (this.pojoMovimiento.getAlmacen() == null || this.pojoMovimiento.getAlmacen().getId() == null || this.pojoMovimiento.getAlmacen().getId() <= 0L) {
			control(-13, "Ocurrio un problema al validar la Bodega de trabajo");
			return false;
		}
		
		if (this.pojoMovimiento.getEntrega() == null || this.pojoMovimiento.getEntrega().getId() == null || this.pojoMovimiento.getEntrega().getId() <= 0L) {
			control(-14, "Debe indicar al trabajador que entregara el material");
			return false;
		}
		
		if (this.pojoMovimiento.getRecibe() == null || this.pojoMovimiento.getRecibe().getId() == null || this.pojoMovimiento.getRecibe().getId() <= 0L) {
			control(-15, "Debe indicar el trabajador que recibira el material");
			return false;
		}
		
		if (this.pojoMovimiento.getRecibe().getId().compareTo(this.pojoMovimiento.getEntrega().getId()) == 0) {
			control(-16, "El trabajador que entrega y recibe el material deben ser diferentes");
			return false;
		}

		if (this.listDetalles == null || this.listDetalles.isEmpty()) {
			control(-17, "No añadio ningun Producto/Material a la salida");
			return false;
		}
		
		//Revisar si alguno se encuentra en cero
		for (MovimientosDetalleExt md : this.listDetalles) {
			if ( md.getCantidad() == 0) {
				control(-18, "La cantidad de producto no puede ser cero.");
				return false;
			}
		}
		
		//Si en alguno de los productos se encuentra que la cantidad es mayor a la que hay en almacen, salir
		for (MovimientosDetalleExt md : this.listDetalles) {
			if ( md.getCantidad() > md.getCantidadSolicitada()) {
				control(-19, "La existencia actual del Producto/Material no es suficiente para cubrir la cantidad indicada");
				return false;
			}
		}
		
		return true;
	}

	private void control() {
		this.operacionCompleta = true;
		this.operacionCancelada = false;
		this.mensaje = "";
		this.tipoMensaje = 0;
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
	// FAMILIAS
	// ------------------------------------------------------------------------------------
	
	private void cargarFamilias() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<ConValores> listAux1 = null;
		List<ConValores> listAux2 = null;
		long idMaestro = 0L;
		
		try {
			if (this.listFamilias == null)
				this.listFamilias = new ArrayList<ConValores>();
			this.listFamilias.clear();
			
			if (this.listFamiliasItems == null)
				this.listFamiliasItems = new ArrayList<SelectItem>();
			this.listFamiliasItems.clear();
			
			// Recuperamos los Maestros disponibles
			listAux1 = this.ifzConValores.findAll(this.pojoGpoMaestro);
			if (listAux1 == null || listAux1.isEmpty())
				return;
			
			for (ConValores var : listAux1) {
				if (! "1".equals(var.getValor()))
					continue;
				idMaestro = var.getId();
				break;
			}
			
			if (idMaestro <= 0L)
				return;
			
			// Recuperamos las especialidades del Maestro elegido
			params.clear();
			params.put("grupoValorId.id", String.valueOf(this.pojoGpoEspecialidades.getId()));
			params.put("atributo1", String.valueOf(idMaestro));
			listAux1 = this.ifzConValores.findByProperties(params, 0);
			if (listAux1 == null || listAux1.isEmpty()) 
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
	
	// ------------------------------------------------------------------------------------
	// EMPLEADOS
	// ------------------------------------------------------------------------------------

	private List<Empleado> cargarEmpleados(List<Long> puestosExtras) {
		List<Empleado> resultados = null;
		List<Long> puestosValidos = null;

		try {
			if (this.listEmpleados == null)
				this.listEmpleados = new ArrayList<Empleado>();
			
			puestosValidos = new ArrayList<Long>();
			puestosValidos.addAll(this.listPuestosValidos);
			if (puestosExtras != null && ! puestosExtras.isEmpty())
				puestosValidos.addAll(puestosExtras);
			
			resultados = this.ifzEmpleado.findEmpleadosAlmacenes(puestosValidos);
			if (resultados != null && ! resultados.isEmpty()) {
				Collections.sort(resultados, new Comparator<Empleado>() {
					@Override
					public int compare(Empleado o1, Empleado o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
				});
			}
	
			if (this.listEmpleados.isEmpty()) {
				this.listEmpleados.addAll(resultados);
			} else {
				for (Empleado item : resultados) {
					if (this.listEmpleados.contains(item))
						continue;
					this.listEmpleados.add(item);
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Empleados", e);
		}
		
		return resultados;
	}
	
	private void validaEmpleadoEntrega() {
		if (this.pojoMovimiento.getEntrega() != null)
			return;
		cargarCboEmpleadoEntrega();
		for (Empleado var : this.listEmpleados) {
			if (this.pojoObra.getIdResponsable().longValue() == var.getId().longValue()) {
				this.pojoMovimiento.setEntrega(var);
				break;
			}
		}
	}
	
	private void validaEmpleadoRecibe() {
		if (this.pojoMovimiento.getRecibe() != null)
			return;
		cargarCboEmpleadoRecibe();
		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L)
			return;
		for (Empleado var : this.listEmpleados) {
			if (this.pojoObra.getIdResponsable().longValue() == var.getId().longValue()) {
				this.pojoMovimiento.setRecibe(var);
				break;
			}
		}
	}
	
	private void cargarCboEmpleadoEntrega() {
		List<Empleado> empEntrega = null;
		
		if (this.listEmpleadosEntrega == null)
			this.listEmpleadosEntrega = new ArrayList<SelectItem>();
		this.listEmpleadosEntrega.clear();
		
		empEntrega = cargarEmpleados(null);
		if (empEntrega != null && ! empEntrega.isEmpty()) {
			for (Empleado e : empEntrega) 
				this.listEmpleadosEntrega.add(new SelectItem(e.getId(), e.getNombre()));
		}
	}
	
	private void cargarCboEmpleadoRecibe() {
		List<Empleado> empRecibe = null;
		List<Long> puestosExtras = null;
		
		if (this.listEmpleadosRecibe == null)
			this.listEmpleadosRecibe = new ArrayList<SelectItem>();
		this.listEmpleadosRecibe.clear();

		puestosExtras = new ArrayList<Long>();
		puestosExtras.add(10000016L); // RESIDENTE DE OBRA
		puestosExtras.add(10000018L); // RESIDENTE A CARGO DE OBRA
		
		empRecibe = cargarEmpleados(puestosExtras);
		if (empRecibe != null && ! empRecibe.isEmpty()) {
			for (Empleado e : empRecibe) 
				this.listEmpleadosRecibe.add(new SelectItem(e.getId(), e.getNombre()));
		}
	}

	// ------------------------------------------------------------------------------------
	// ALMACENES
	// ------------------------------------------------------------------------------------

	private void cargarAlmacenes() {
		List<Integer> tipos = null;
		List<Long> idAlmacenesAsignados = null;
		
		try {
			tipos = new ArrayList<Integer>(Arrays.asList(1, 2));
			if (this.almacenesAdministrativos) {
				tipos.add(3);
				tipos.add(4);
			}
			// Solo Almacenes/Bodegas ligados al Empleado
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			boolean f = false;
			this.listAlmacenes = this.ifzAlmacen.findByTipo(tipos, f, "");
			this.listAlmacenes = (this.listAlmacenes != null) ? this.listAlmacenes : new ArrayList<Almacen>();

			idAlmacenesAsignados = new ArrayList<Long>();
			if (! "ADMINISTRADOR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) {
				for (Almacen var : this.listAlmacenesTrabajo) 
					idAlmacenesAsignados.add(var.getId());
			}
			
			this.listAlmacenesItems = new ArrayList<SelectItem>();
			for (Almacen var : this.listAlmacenes) 
				this.listAlmacenesItems.add(new SelectItem(var.getId(), (idAlmacenesAsignados.contains(var.getId()) ? "* " : "") + var.getNombre() + " (" + var.getIdentificador() + ")"));
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Almacenes", e);
		}
	}
	
	@SuppressWarnings("unused")
	private void cargarAlmacenesOLD() {
		List<Almacen> listAuxiliar = new ArrayList<Almacen>();
		String tmpAlmacenes = "";

		try {
			this.almacenesAdministrativos = this.PERFIL_ADMINISTRATIVO;
			tmpAlmacenes = (this.baseTraspaso ? this.pojoTraspaso.getAlmacenOrigen().getId().toString() + "," + this.pojoTraspaso.getAlmacenDestino().getId().toString() : "");
			if (this.listAlmacenes == null)
				this.listAlmacenes = new ArrayList<Almacen>();
			this.listAlmacenes.clear();
			
			if (this.listAlmacenesItems == null)
				this.listAlmacenesItems = new ArrayList<SelectItem>();
			this.listAlmacenesItems.clear();
			
			if (this.listAlmacenesTrabajo != null && ! this.listAlmacenesTrabajo.isEmpty())
				listAuxiliar.addAll(this.listAlmacenesTrabajo);
			
			if ((listAuxiliar == null || listAuxiliar.isEmpty()) && "ADMINISTRADOR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()))
				listAuxiliar = this.ifzAlmacen.findAll();
			
			if (listAuxiliar != null && ! listAuxiliar.isEmpty()) {
				for (Almacen var : listAuxiliar) {
					if (this.baseTraspaso && ! tmpAlmacenes.contains(var.getId().toString()))
						continue;
					
					if (var.getTipo() == 1 || var.getTipo() == 3)
						continue;
					if (var.getTipo() == 4 && ! this.almacenesAdministrativos)
						continue;
					this.listAlmacenes.add(var);
				}
			}
			
			Collections.sort(this.listAlmacenes, new Comparator<Almacen>() {
				@Override
				public int compare(Almacen o1, Almacen o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
			
			for (Almacen a : this.listAlmacenes) 
				this.listAlmacenesItems.add(new SelectItem(a.getId(), a.getNombre()));
			
			// Autoasignamos primer Almacen/Bodega disponible
			if (this.listAlmacenes != null && ! this.listAlmacenes.isEmpty())
				setIdAlmacen((long) this.listAlmacenesItems.get(0).getValue());
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar las Bodegas", e);
		}
	}

	public void actualizarResultados() {
		this.listMovimientos = new ArrayList<AlmacenMovimientos>();
		if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L)
			return;
		if (! "fecha".equals(this.campoBusqueda) && "".equals(this.valorBusqueda))
			return;
		buscar();
	}
	
	// --------------------------------------------------------------------
	// BUSQUEDA OBRAS 
	// --------------------------------------------------------------------

	public void nuevaBusquedaObras() {
		control();
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		
		this.numPaginaObras = 1;
		this.listObras = new ArrayList<Obra>();
	}

	public void buscarObras() {
    	try {
    		control();
			this.campoBusquedaObras = (this.campoBusquedaObras != null && ! "".equals(this.campoBusquedaObras.trim())) ? this.campoBusquedaObras.trim() : this.tiposBusquedaObras.get(0).getValue().toString();
			this.valorBusquedaObras = (this.valorBusquedaObras != null && ! "".equals(this.valorBusquedaObras.trim())) ? this.valorBusquedaObras.trim() : "";

			this.numPaginaObras = 1;
			this.listObras = this.ifzObras.findLikePropertyByAlmacen(this.campoBusquedaObras, this.valorBusquedaObras, this.almacenTrabajo.getId(), 0); 
			this.listObras = this.listObras != null ? this.listObras : new ArrayList<Obra>();
			if (this.listObras == null || this.listObras.isEmpty()) 
				control(2, "Busqueda sin resultados");
			this.mensaje = "OK";
    	} catch (Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
    	} 
    }

	public void seleccionarObra() {
		control();
		// Compruebo seleccion de Obra
		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
			control(-1, "Debe indicar una Obra");
			return;
		}

		// Autoasignamos quien RECIBE el material de acuerdo al Responsable de la Obra elegida
		cargarEmpleadosObra();
		setRecibe(this.pojoObra.getIdResponsable());
		validaEmpleadoRecibe();
	}

	private void cargarEmpleadosObra() {
		List<ObraEmpleado> empleadosObra = null;
		List<Long> empleadosId = null; 
		
		try {
			this.listEmpleados = new ArrayList<Empleado>();
			this.listEmpleadosRecibe = new ArrayList<SelectItem>();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L)
				return;
			
			log.info("Cargando empleados de obra: " + this.pojoObra.getId() + " ... ");
			this.ifzObraEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			empleadosObra = this.ifzObraEmpleados.findAll(this.pojoObra.getId());
			if (empleadosObra == null || empleadosObra.isEmpty())
				empleadosObra = new ArrayList<ObraEmpleado>();
			
			empleadosId = new ArrayList<Long>();
			for (ObraEmpleado empleadoObra : empleadosObra) {
				if (this.pojoObra.getIdResponsable().longValue() == empleadoObra.getIdEmpleado().longValue())
					continue;  //  excluimos el responsable de la obra para añadirlo mas adelante al inicio de la lista
				empleadosId.add(empleadoObra.getIdEmpleado());
			}

			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.listEmpleados = this.ifzEmpleado.findAll(empleadosId);
			this.listEmpleados.add(0, this.ifzEmpleado.findById(this.pojoObra.getIdResponsable()));
			for (Empleado empleado : this.listEmpleados) 
				this.listEmpleadosRecibe.add(new SelectItem(empleado.getId(), empleado.getNombre()));
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar los Empleados asignados a la Obra", e);
		}
	}
	
	// --------------------------------------------------------------------
	// BUSQUEDA PRODUCTOS 
	// --------------------------------------------------------------------

	public void nuevaBusquedaProductos() {
		control();
		this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
		this.valorBusquedaProductos = "";
		this.idFamilia = 0;

		this.numPaginaProductos = 1;
		this.listProductos = new ArrayList<Producto>();
		this.pojoProductoBusqueda = new Producto();
	}

	public void buscarProductos() {
		List<AlmacenProductoExt> resultados = null;
		int tipoMaestro = 1; // MAESTRO de Insumos
		
		try {
			control();
			this.campoBusquedaProductos = (this.campoBusquedaProductos != null && ! "".equals(this.campoBusquedaProductos.trim())) ? this.campoBusquedaProductos.trim() : this.tiposBusquedaProductos.get(0).getValue().toString();
			this.valorBusquedaProductos = (this.valorBusquedaProductos != null && ! "".equals(this.valorBusquedaProductos.trim())) ? this.valorBusquedaProductos.trim() : "";
			tipoMaestro = this.almacenTrabajo.getTipo() > 2 ? 2 : 1;
			
			// Buscamos productos en Almacen
			this.numPaginaProductos = 1;
			resultados = this.ifzAlmacenProducto.findExtExistentes(this.pojoMovimiento.getAlmacen().getId(), this.campoBusquedaProductos, this.valorBusquedaProductos, this.idFamilia, tipoMaestro, 0, false);
			if (resultados == null || resultados.isEmpty()) {
				log.info("ERROR 2 : Busqueda sin resultados");
				control(2, "Busqueda sin resultados");
				return;
			}
			
			// Recuperamos productos
			this.listProductos = new ArrayList<Producto>();
			for (AlmacenProductoExt var : resultados) {
				if (var.getExistencia() > 0) {
					var.getProducto().setExistencia(var.getExistencia());
					this.listProductos.add(var.getProducto());
				}
			}
			
			// Ordenamos por descripcion
			Collections.sort(this.listProductos, new Comparator<Producto>() {
				@Override
				public int compare(Producto o1, Producto o2) {
					return o1.getDescripcion().compareTo(o2.getDescripcion());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Productos del Almacen seleccioando", e);
		}
	}
	
	public void seleccionarProducto() {
		ProductoExt productoExtendido = null;
		
		try {
			control();
			if (! validaAgregarProducto()) 
				return;
			
			// Extiendo producto seleccionado
			log.info("Extendiendo producto ... ");
			this.cantidadProductoSeleccionado = this.pojoProductoBusqueda.getExistencia();
			productoExtendido = this.ifzProductos.convertir(this.pojoProductoBusqueda);
			
			// Genero detalle para Movimiento
			log.info("Genero movimiento detalle ... ");
			this.productoSalidaSeleccionado = new MovimientosDetalleExt();
			this.productoSalidaSeleccionado.setProducto(productoExtendido);
			this.productoSalidaSeleccionado.setCantidadSolicitada(this.cantidadProductoSeleccionado);
			this.productoSalidaSeleccionado.setCantidad(1);
			this.productoSalidaSeleccionado.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.productoSalidaSeleccionado.setFechaCreacion(Calendar.getInstance().getTime());
			this.productoSalidaSeleccionado.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.productoSalidaSeleccionado.setFechaModificacion(Calendar.getInstance().getTime());
			this.listDetalles.add(this.productoSalidaSeleccionado);
			this.numeroItems = this.listDetalles.size();
			
			// Asignamos 'almacenAnterior' si corresponde 
			if (this.almacenAnterior == 0)	
				this.almacenAnterior = this.pojoMovimiento.getAlmacen().getId();

			nuevaBusquedaProductos();
			control(false, 4, "Producto Añadido", null);
			this.operacionCompleta = true;
		} catch (Exception e) {
			control("Ocurrio un probla al recuperar el Producto seleccionado.", e);
		}
	}

	// --------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------

	public String getAlmacenMovimiento() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getAlmacen() != null && this.pojoMovimiento.getAlmacen().getId() != null && this.pojoMovimiento.getAlmacen().getId() > 0L)
			return this.pojoMovimiento.getAlmacen().getId() + " - " + this.pojoMovimiento.getAlmacen().getNombre();
		return "";
	}
	
	public void setAlmacenMovimiento(String value) {}

	public String getEmpleadoEntrega() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getEntrega() != null && this.pojoMovimiento.getEntrega().getId() != null && this.pojoMovimiento.getEntrega().getId() > 0L) 
			return this.pojoMovimiento.getEntrega().getClave() + " - " + this.pojoMovimiento.getEntrega().getNombre() + " (" + this.pojoMovimiento.getEntrega().getId() + ")";
		if (this.pojoEmpleadoUsuario != null)
			return this.pojoEmpleadoUsuario.getClave() + " - " + this.pojoEmpleadoUsuario.getNombre() + " (" + this.pojoEmpleadoUsuario.getId() + ")";
		return "";
	}

	public void setEmpleadoEntrega(String idEmpleado) {}
	
	public boolean getDeshabilitarProductosAdministrativos() {
		/*if ("ADMINISTRADOR".equals(this.usuario))
			return false;
		return true;*/
		return false;
	}
	
	public void setDeshabilitarProductosAdministrativos(boolean value) {}
	
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

	public boolean isBand() {
		return operacionCancelada;
	}

	public void setBand(boolean band) {
		this.operacionCancelada = band;
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

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public List<SelectItem> getListAlmacenesTrabajoItems() {
		return listAlmacenesItems;
	}

	public void setListAlmacenesTrabajoItems(List<SelectItem> listAlmacenesTrabajoItems) {
		this.listAlmacenesItems = listAlmacenesTrabajoItems;
	}
	
	public List<AlmacenMovimientos> getListaMovimientosGrid() {
		return listMovimientos;
	}

	public void setListaMovimientosGrid(List<AlmacenMovimientos> listaMovimientosGrid) {
		this.listMovimientos = listaMovimientosGrid;
	}

	public long getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(long idMovimiento) {
		this.idMovimiento = idMovimiento;
	}
	
	public AlmacenMovimientosExt getPojoMovimiento() {
		return pojoMovimiento;
	}

	public void setPojoMovimiento(AlmacenMovimientosExt pojoMovimiento) {
		this.pojoMovimiento = pojoMovimiento;
	}

	public List<Almacen> getListaAlmacenes() {
		return listAlmacenes;
	}

	public void setListaAlmacenes(List<Almacen> listaAlmacenes) {
		this.listAlmacenes = listaAlmacenes;
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

	public List<Obra> getListObrasPrincipales() {
		return listObras;
	}

	public void setListObrasPrincipales(List<Obra> listObrasPrincipales) {
		this.listObras = listObrasPrincipales;
	}

	public List<Producto> getListaBusquedaProductos() {
		return listProductos;
	}

	public void setListaBusquedaProductos(List<Producto> listaBusquedaProductos) {
		this.listProductos = listaBusquedaProductos;
	}

	public int getNumPaginaProductos() {
		return numPaginaProductos;
	}

	public void setNumPaginaProductos(int numPaginaProductos) {
		this.numPaginaProductos = numPaginaProductos;
	}

	public Producto getPojoProductoBusqueda() {
		return pojoProductoBusqueda;
	}

	public void setPojoProductoBusqueda(Producto pojoProductoBusqueda) {
		this.pojoProductoBusqueda = pojoProductoBusqueda;
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

	public int getValorOpcionBusquedaProductos() {
		return valorOpcionBusquedaProductos;
	}

	public void setValorOpcionBusquedaProductos(int valorOpcionBusquedaProductos) {
		this.valorOpcionBusquedaProductos = valorOpcionBusquedaProductos;
	}

	public boolean isOperacionCompleta() {
		return operacionCompleta;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCompleta = operacionCompleta;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public MovimientosDetalleExt getPojoMovimientoDetalleEliminar() {
		return pojoMovimientoDetalleEliminar;
	}

	public void setPojoMovimientoDetalleEliminar(MovimientosDetalleExt pojoMovimientoDetalleEliminar) {
		this.pojoMovimientoDetalleEliminar = pojoMovimientoDetalleEliminar;
	}

	public int getNumeroItems() {
		return numeroItems;
	}

	public void setNumeroItems(int numeroItems) {
		this.numeroItems = numeroItems;
	}

	public int getRowSeleccionado() {
		return rowSeleccionado;
	}

	public void setRowSeleccionado(int rowSeleccionado) {
		log.info("row: "+rowSeleccionado);
		this.rowSeleccionado = rowSeleccionado;
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
	
	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public boolean getEsAdministrativo() {
		return PERFIL_ADMINISTRATIVO;
	}
	
	public void setEsAdministrativo(boolean esAdministrativo) {}
	
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

	public boolean getBaseTraspaso() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getIdTraspaso() > 0L && this.pojoTraspaso != null && this.pojoTraspaso.getId() != null && this.pojoTraspaso.getId() > 0L)
			return true;
		return false;
	}
	
	public void setBaseTraspaso(boolean value) {}

	// --------------------------------------------------------------------
	// PROPIEDADES SALIDA
	// --------------------------------------------------------------------

	public String getTitulo() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getId() != null && this.pojoMovimiento.getId() > 0L)
			return "Salida a Obra " + this.pojoMovimiento.getId();
		return "Nueva Salida a Obra";
	}
	
	public void setTitulo(String value) {}

	public Date getFecha() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getFecha() != null) 
			return this.pojoMovimiento.getFecha(); 
		return Calendar.getInstance().getTime();
	}
	
	public void setFecha(Date fecha) {
		this.pojoMovimiento.setFecha(fecha);
	}

	public long getIdAlmacen() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getAlmacen() != null && this.pojoMovimiento.getAlmacen().getId() != null && this.pojoMovimiento.getAlmacen().getId() > 0L)
			return this.pojoMovimiento.getAlmacen().getId();
		return 0L;
	}

	public void setIdAlmacen(long idAlmacen) {
		this.pojoMovimiento.setAlmacen(null);
		if (idAlmacen <= 0L)
			return;
		
		for (Almacen almacen : this.listAlmacenes) {
			if (idAlmacen == almacen.getId().longValue()) {
				this.pojoMovimiento.setAlmacen(almacen);
				setEntrega(almacen.getIdEncargado());
				/*this.buscarAdministrativo = false;
				if (almacen.getTipo() > 2)
					this.buscarAdministrativo = true;*/
				break;
			}
		}
		
		validaEmpleadoEntrega();
	}

	public List<SelectItem> getListaCboAlmacenes() {
		return listAlmacenesItems;
	}

	public void setListaCboAlmacenes(List<SelectItem> listaCboAlmacenes) {
		this.listAlmacenesItems = listaCboAlmacenes;
	}

	public long getEntrega() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getEntrega() != null && this.pojoMovimiento.getEntrega().getId() != null && this.pojoMovimiento.getEntrega().getId() > 0L)
			return this.pojoMovimiento.getEntrega().getId();
		return 0L; 
	}
	
	public void setEntrega(long idEmpleado) {
		if (idEmpleado <= 0) {
			this.pojoMovimiento.setEntrega(null);
			return;
		}
		
		if (this.pojoMovimiento.getEntrega() != null && this.pojoMovimiento.getEntrega() != null && this.pojoMovimiento.getEntrega().getId().longValue() == idEmpleado)
			return;
		for (Empleado var : this.listEmpleados) {
			if (idEmpleado == var.getId().longValue()) {
				this.pojoMovimiento.setEntrega(var);
				break;
			}
		}
	}

	public long getRecibe() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getRecibe() != null && this.pojoMovimiento.getRecibe().getId() != null && this.pojoMovimiento.getRecibe().getId() > 0L)
			return this.pojoMovimiento.getRecibe().getId();
		return 0L; 
	}

	public void setRecibe(long idEmpleado) {
		if (idEmpleado <= 0) {
			this.pojoMovimiento.setRecibe(null);
			return;
		}
	
		if (this.pojoMovimiento.getRecibe() != null && this.pojoMovimiento.getRecibe() != null && this.pojoMovimiento.getRecibe().getId().longValue() == idEmpleado)
			return;
		for (Empleado var : this.listEmpleados) {
			if (idEmpleado == var.getId().longValue()) {
				this.pojoMovimiento.setRecibe(var);
				break;
			}
		}
	}

	public List<SelectItem> getListaCboEmpleadoEntrega() {
		return listEmpleadosEntrega;
	}

	public void setListaCboEmpleadoEntrega(List<SelectItem> listaCboEmpleadoEntrega) {
		this.listEmpleadosEntrega = listaCboEmpleadoEntrega;
	}

	public String getObra() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		return "";
	}
	
	public void setObra(String value) {}

	public List<MovimientosDetalleExt> getListaProductosSalida() {
		return listDetalles;
	}

	public void setListaProductosSalida(List<MovimientosDetalleExt> listaProductosSalida) {
		this.listDetalles = listaProductosSalida;
	}

	public int getNumPaginaOrdenCompra() {
		return numPaginaOrdenCompra;
	}

	public void setNumPaginaOrdenCompra(int numPaginaOrdenCompra) {
		this.numPaginaOrdenCompra = numPaginaOrdenCompra;
	}

	// --------------------------------------------------------------------
	// PROPIEDADES TRASPASO
	// --------------------------------------------------------------------

	public int getTipoTraspaso() {
		return tipoTraspaso;
	}

	public void setTipoTraspaso(int tipoTraspaso) {
		this.tipoTraspaso = tipoTraspaso;
	}

	public List<SelectItem> getListTiposTraspasos() {
		return listTiposTraspasos;
	}

	public void setListTiposTraspasos(List<SelectItem> listTiposTraspasos) {
		this.listTiposTraspasos = listTiposTraspasos;
	}

	public Date getTraspasoFecha() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getFecha() != null)
			return this.pojoMovimiento.getFecha();
		return Calendar.getInstance().getTime(); 
	}
	
	public void setTraspasoFecha(Date fecha) {
		this.pojoMovimiento.setFecha(fecha);
	}

	public long getIdAlmacenSalida() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getAlmacenOrigen() != null && this.pojoTraspaso.getAlmacenOrigen().getId() != null)
			return this.pojoTraspaso.getAlmacenOrigen().getId();
		return 0L;
	}

	public void setIdAlmacenSalida(long idAlmacen) {}

	public List<SelectItem> getListaCboAlmacenSalida() {
		return listAlmacenesItems;
	}

	public void setListaCboAlmacenSalida(List<SelectItem> listaCboAlmacenSalida) {
		this.listAlmacenesItems = listaCboAlmacenSalida;
	}

	public long getIdEmpleadoEntrega() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getEntrega() != null && this.pojoTraspaso.getEntrega().getId() != null && this.pojoTraspaso.getEntrega().getId() > 0L)
			return this.pojoTraspaso.getEntrega().getId();
		return 0L; 
	}
	
	public void setIdEmpleadoEntrega(long idEmpleado) {
		if (idEmpleado <= 0) 
			return;
		
		for (Empleado var : this.listEmpleados) {
			if (idEmpleado == var.getId().longValue()) {
				this.pojoTraspaso.setEntrega(var);
				break;
			}
		}
	}

	public List<SelectItem> getListTraspasoCboEmpleadoEntrega() {
		return listEmpleadosEntrega;
	}

	public void setListTraspasoCboEmpleadoEntrega(List<SelectItem> listaCboEmpleadoEntrega) {
		this.listEmpleadosEntrega = listaCboEmpleadoEntrega;
	}

	public long getIdAlmacenDestino() {
		if (this.pojoTraspaso != null && pojoTraspaso.getAlmacenDestino() != null && this.pojoTraspaso.getAlmacenDestino().getId() != null)
			return this.pojoTraspaso.getAlmacenDestino().getId();
		return 0L;
	}
	
	public void setIdAlmacenDestino(long idAlmacen) {}

	public List<SelectItem> getListaCboAlmacenDestino() {
		return listAlmacenesItems;
	}

	public void setListaCboAlmacenDestino(List<SelectItem> listaCboAlmacenDestino) {
		this.listAlmacenesItems = listaCboAlmacenDestino;
	}

	public long getIdEmpleadoRecibe() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getRecibe() != null && this.pojoTraspaso.getRecibe().getId() != null && this.pojoTraspaso.getRecibe().getId() > 0L)
			return this.pojoTraspaso.getRecibe().getId();
		return 0L; 
	}

	public void setIdEmpleadoRecibe(long idEmpleado) {
		if (idEmpleado <= 0) 
			return;
		
		for (Empleado var : this.listEmpleados) {
			if (idEmpleado == var.getId().longValue()) {
				this.pojoTraspaso.setRecibe(var);
				break;
			}
		}
	}

	public List<SelectItem> getListaCboEmpleadoRecibe() {
		return listEmpleadosRecibe;
	}

	public void setListaCboEmpleadoRecibe(List<SelectItem> listaCboEmpleadoRecibe) {
		this.listEmpleadosRecibe = listaCboEmpleadoRecibe;
	}

	public String getTraspaso() {
		if (this.pojoMovimiento != null && this.pojoTraspaso != null && this.pojoTraspaso.getId() != null && this.pojoTraspaso.getId() > 0L) 
			return "Traspaso " + this.pojoTraspaso.getId() + " a Bodega [Salida " + this.pojoMovimiento.getId() + "]";
		return "";
	}
	
	public void setTraspaso(String value) {}

	public List<TraspasoDetalleExt> getListaTraspasoDetalleGrid() {
		return listTraspasoDetalleGrid;
	}

	public void setListaTraspasoDetalleGrid(List<TraspasoDetalleExt> listaTraspasoDetalleGrid) {
		this.listTraspasoDetalleGrid = listaTraspasoDetalleGrid;
	}

	public int getPagDetalles() {
		return pagTraspasoDetalles;
	}

	public void setPagDetalles(int pagTraspasoDetalles) {
		this.pagTraspasoDetalles = pagTraspasoDetalles;
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

		if (this.almacenTrabajo != null && this.almacenTrabajo.getId() != null && this.almacenTrabajo.getId() > 0L && this.almacenTrabajo.getId().longValue() == idAlmacenTrabajo)
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
}
