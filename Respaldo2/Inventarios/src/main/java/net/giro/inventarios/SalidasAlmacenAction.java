package net.giro.inventarios;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.Obra;
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
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.beans.TipoMovimientoInventario;
import net.giro.inventarios.beans.TipoMovimientoReferencia;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="salidasAlmacenAction")
public class SalidasAlmacenAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SalidasAlmacenAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx;
	
	private AlmacenMovimientosRem ifzMovimientos;
	private MovimientosDetalleRem ifzMovimientosDetalle;
	private ProductoRem ifzProductos;
	private AlmacenRem ifzAlmacen;
	private ObraRem ifzObras;
	private AlmacenProductoRem ifzAlmacenProducto;
	private EmpleadoRem ifzEmpleado;
	private ReportesRem ifzReportes;
	private ConGrupoValoresRem ifzGpoValores;
	private ConValoresRem ifzConValores;
    private long usuarioId;
    private String usuario;
	private int numPagina;
	private int numPaginaObras;
	private int numPaginaOrdenCompra;
	private int numPaginaProductos;
	private boolean operacionCompleta;
	private String resOperacion;
	private boolean band;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private boolean puedeEditar;
	private String valorBusqueda;
	private List<SelectItem> listaCampoBusqueda;
	private int campoBusqueda;
	private final int TIPO_MOVIMIENTO = 1;	//1 al ser salida
	private List<AlmacenMovimientosExt> listaMovimientosGrid;
	private AlmacenMovimientosExt pojoMovimientoEditar;
	private int currentIndex;
	private long almacenAnterior;
	private List<Almacen> listaAlmacenes;
	private List<SelectItem> listaCboAlmacenes;
	private boolean almacenesAdministrativos;
	private List<Empleado> listaEmpleados;
	private List<SelectItem> listaCboEmpleadoEntrega;
	private List<SelectItem> listaCboEmpleadoRecibe;
	// Busqueda obras
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorOpcionBusquedaObras;
	private boolean avanzadaBusquedaObras;
	private List<Obra>		 		listObrasPrincipales;
	//Variables para esta el movimiento de salida
	private List<MovimientosDetalleExt> listaProductosSalida;
	private List<MovimientosDetalleExt> listaProductosSalidaEliminados;
	private int numeroItems;
	//Busqueda Productos
	private List<Producto> listaBusquedaProductos; 
	private List<SelectItem> tiposBusquedaProductos;	
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private int valorOpcionBusquedaProductos;
	private Producto pojoProductoBusqueda;
	private boolean avanzadaBusquedaProductos;
	private boolean admvaBusquedaProductos;
	// Familia
	private ConGrupoValores pojoGpoMaestro;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	private long idFamilia;
	//Variables para el movimiento
	private AlmacenMovimientosExt pojoMovimiento; 
	private Obra pojoObra;
	private MovimientosDetalleExt pojoMovimientoDetalleEliminar;
	private MovimientosDetalleExt productoSalidaSeleccionado;
	private MovimientosDetalleExt movimientoSeleccionado;
	private double cantidadProductoSeleccionado;
	private int rowSeleccionado;
    // PEFILES
	private boolean PERFIL_ADMINISTRATIVO;
	private boolean buscarAdministrativo;
	// EMPLEADO-USUARIO
	private boolean usuarioValido;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalBase;
	private List<Almacen> listEmpleadoAlmacenes;
	private int almacenBodega;
	//private long idBodegaBase;
	//private long idAlmacenBase;
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
    
	
	public SalidasAlmacenAction() throws NamingException,Exception {
		ValueExpression ve = null;
		FacesContext fc = null;
		Application app = null;
		String resValue = "";
		String[] splitted = null;

		fc = FacesContext.getCurrentInstance();
		app = fc.getApplication();

		this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);

		ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
		this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
		this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

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

		this.ctx = new InitialContext();
		this.ifzMovimientos = (AlmacenMovimientosRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
		this.ifzMovimientosDetalle = (MovimientosDetalleRem) this.ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");
		this.ifzProductos = (ProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
		this.ifzAlmacen = (AlmacenRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
		this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
		this.ifzProductos = (ProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
		this.ifzAlmacenProducto = (AlmacenProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
		this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
		this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
		this.ifzGpoValores = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
		this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
		this.ifzTraspaso = (AlmacenTraspasoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
		this.ifzTraspasoDetalle = (TraspasoDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
		
		this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzMovimientosDetalle.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
		this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
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
		
		this.numPagina = 1;
		this.productoSalidaSeleccionado = new MovimientosDetalleExt();
		this.listaCampoBusqueda = null;
		this.listaCampoBusqueda = new ArrayList<SelectItem>();
		this.listaCampoBusqueda.add( new SelectItem("1", "Almacen"));
		//this.listaCampoBusqueda.add( new SelectItem("2", "Obra"));
		
		// Busqueda obras
		this.listObrasPrincipales = new ArrayList<Obra>();
		this.tiposBusquedaObras = new ArrayList<SelectItem>();
		this.tiposBusquedaObras.add(new SelectItem("nombre", "Nombre"));
		this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
		this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
		this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.valorOpcionBusquedaObras = 0;
		this.numPaginaObras = 1;
		
		// Busqueda Productos
		this.listaBusquedaProductos = new ArrayList<Producto>();
		this.tiposBusquedaProductos = new ArrayList<SelectItem>();
		this.tiposBusquedaProductos.add(new SelectItem("*", "Coincidencia"));
		this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Nombre"));
		this.tiposBusquedaProductos.add(new SelectItem("clave", "Clave"));
		this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
		this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
		this.valorBusquedaProductos = "";
		this.numPaginaProductos = 1;

		this.usuarioValido = false;
		if (comprobarUsuario())
			this.usuarioValido = true;
		
		// TRASPASOS
		this.TRASPASO_ALMACEN = 1;
		this.TRASPASO_DEVOLUCION = 2;
		this.TRASPASO_ALMACEN_ALMACEN = 3;
		this.listTiposTraspasos = new ArrayList<SelectItem>();
		this.listTiposTraspasos.add(new SelectItem(this.TRASPASO_ALMACEN, "Traspaso de Almacen a Bodega"));
		this.listTiposTraspasos.add(new SelectItem(this.TRASPASO_ALMACEN_ALMACEN, "Traspaso de Bodega a Bodega"));
		this.listTiposTraspasos.add(new SelectItem(this.TRASPASO_DEVOLUCION, "Devolucion a Almacen"));
		this.tipoTraspaso = (int) this.listTiposTraspasos.get(0).getValue();
		
		this.listaMovimientosGrid = new ArrayList<AlmacenMovimientosExt>();
		this.listaAlmacenes = new ArrayList<Almacen>();
		
		nuevoMovimiento();
	}
	

	public void buscar() {
		try {
			control();
			if (this.listaMovimientosGrid != null)
				this.listaMovimientosGrid.clear();
			
			if (this.valorBusqueda.equals("")) {
				log.info("Buscar todos");
				this.listaMovimientosGrid = this.ifzMovimientos.findExtByEspecificField("nombreAlmacen", this.valorBusqueda, this.TIPO_MOVIMIENTO);
			} else {
				if (this.campoBusqueda==1) {//buscar por almacen
					log.info("Buscar por almacen: "+this.valorBusqueda);
					this.listaMovimientosGrid = this.ifzMovimientos.findExtByEspecificField("nombreAlmacen", this.valorBusqueda, this.TIPO_MOVIMIENTO);
				} else {//buscar por obra
					log.info("Buscar por obra: "+this.valorBusqueda);
					this.listaMovimientosGrid = this.ifzMovimientos.findExtByEspecificField("nombreObra", this.valorBusqueda, this.TIPO_MOVIMIENTO);
				}
			}
			
			if (this.listaMovimientosGrid == null || this.listaMovimientosGrid.isEmpty()) {
				control(true, 2, "Busqueda sin resultados");
				return;
			}

			Collections.sort(this.listaMovimientosGrid, new Comparator<AlmacenMovimientosExt>() {
				@Override
				public int compare(AlmacenMovimientosExt o1, AlmacenMovimientosExt o2) {
					return o2.getFecha().compareTo(o1.getFecha());
				}
			});
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al consultar las Salidas", e);
    	}
	}
	
	public void nuevoMovimiento() {
		control();
		this.puedeEditar = true;
		this.baseTraspaso = false;
		this.pojoMovimiento = new AlmacenMovimientosExt();
		this.pojoObra = new Obra();
		this.listaProductosSalida = null;
		this.pojoTraspaso = null;
		this.listTraspasoDetalleGrid = null;
		this.numeroItems = 0;
		
		if (this.listaProductosSalida == null)
			this.listaProductosSalida = new ArrayList<MovimientosDetalleExt>();
		this.listaProductosSalida.clear();

		if (this.listaProductosSalidaEliminados == null)
			this.listaProductosSalidaEliminados = new ArrayList<MovimientosDetalleExt>();
		this.listaProductosSalidaEliminados.clear();

		cargarEmpleados();
		cargarAlmacenes();
		cargarFamilias();
		if (! this.usuarioValido) {
			control(true, -1, "No es un usuario autorizado para realizar Salidas de Bodega");
			return;
		}
	}

	public void editar() {
		try {
			control();
			this.puedeEditar = false;
			this.baseTraspaso = false;
			this.listaProductosSalida = null;
			this.pojoTraspaso = null;
			this.listTraspasoDetalleGrid = null;
			
			// Recuperamos datos de quien recibe
			this.pojoObra = this.ifzObras.findById(this.pojoMovimiento.getIdObra());
			
			// Recuperamos detalles: Salida o Traspaso segun corresponda
			if (this.pojoMovimiento.getIdTraspaso() > 0L) {
				this.baseTraspaso = true;
				cargarEmpleados();
				cargarAlmacenes();
				
				this.pojoTraspaso = this.ifzTraspaso.findByIdExt(this.pojoMovimiento.getIdTraspaso());
				this.listTraspasoDetalleGrid = this.ifzTraspasoDetalle.findExtDetallesByIdTraspaso(this.pojoMovimiento.getIdTraspaso());
				if (this.listTraspasoDetalleGrid == null)
					this.listTraspasoDetalleGrid = new ArrayList<TraspasoDetalleExt>();
			} else {
				this.listaProductosSalida = this.ifzMovimientosDetalle.findDetallesExtById(this.pojoMovimiento.getId());
				if (this.listaProductosSalida == null)
					this.listaProductosSalida = new ArrayList<MovimientosDetalleExt>();
			}
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al recuperar la Salida indicada", e);
		}
	}

	public void guardar() {
		AlmacenProducto pExistencia = null;
		
		try {
			control();
			if (! validaGuardarMovimiento()) 
				return;
			
			if (this.pojoMovimiento.getId() == null || this.pojoMovimiento.getId() <= 0L) {	
				this.pojoMovimiento.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
				this.pojoMovimiento.setTipoEntrada(TipoMovimientoReferencia.SALIDA_OBRA.toString());
				this.pojoMovimiento.setIdObra(this.pojoObra.getId());
				this.pojoMovimiento.setNombreObra(this.pojoObra.getNombre());
				this.pojoMovimiento.setCreadoPor(this.usuarioId);
				this.pojoMovimiento.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoMovimiento.setModificadoPor(this.usuarioId);
				this.pojoMovimiento.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.pojoMovimiento.setId(this.ifzMovimientos.save(this.pojoMovimiento));
				
				for (MovimientosDetalleExt md : this.listaProductosSalida) {
					md.setIdAlmacenMovimiento(this.pojoMovimiento.getId());
					md.setCreadoPor(this.usuarioId);
					md.setFechaCreacion(Calendar.getInstance().getTime());
					md.setModificadoPor(this.usuarioId);
					md.setFechaModificacion(Calendar.getInstance().getTime());
					
					this.ifzMovimientosDetalle.save(md);
				}
				
				//Ahora, afectar las existencias
				for (MovimientosDetalleExt md : this.listaProductosSalida) {
					pExistencia = this.ifzAlmacenProducto.findAlmacenProducto(this.pojoMovimiento.getAlmacen().getId(), md.getProducto().getId());
					if (pExistencia != null) {
						//Actualizamos la existencia del producto
						log.info("Existencia: " + pExistencia.getExistencia() + ", Cantidad: " + md.getCantidad() + ", CantidadExistente: " + md.getCantidadSolicitada());
						pExistencia.setExistencia(pExistencia.getExistencia() - md.getCantidad());
						this.ifzAlmacenProducto.update(pExistencia);
					}	
				}
				
				this.listaMovimientosGrid.add(this.pojoMovimiento);
			}
			
			nuevoMovimiento();
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al guardar la Salida", e);
		}
	}
	
	public void eliminar() {
		boolean existe = false;
		
		if (this.pojoMovimientoDetalleEliminar == null)
			return;
		
		for (MovimientosDetalleExt md : this.listaProductosSalida) {
			if (md.getProducto().getClave().equals(this.pojoMovimientoDetalleEliminar.getProducto().getClave())) {
				this.listaProductosSalida.remove(md);
				break;
			}
		}
		
		if (this.pojoMovimientoDetalleEliminar.getId() == null || this.pojoMovimientoDetalleEliminar.getId() <= 0L)
			return;

		if (this.listaProductosSalidaEliminados == null)
			this.listaProductosSalidaEliminados = new ArrayList<MovimientosDetalleExt>();
		
		if (! this.listaProductosSalidaEliminados.isEmpty()) {
			for (MovimientosDetalleExt item : this.listaProductosSalidaEliminados) {
				if (this.pojoMovimientoDetalleEliminar.getId().longValue() == item.getId().longValue()) {
					existe = true;
					break;
				}
			}
		}
		
		if (! existe)
			this.listaProductosSalidaEliminados.add(this.pojoMovimientoDetalleEliminar);
		
		if (this.listaProductosSalida.size() == 0) {
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
			if (this.pojoMovimiento == null || this.pojoMovimiento.getId() == null || this.pojoMovimiento.getId() <= 0L) {
				control(true, -1, "Ocurrio un problema al intentar obtener La Entrada seleccionada");
				return;
			}
			
			log.info("Imprimiendo Salida ... ");
			formatter = new SimpleDateFormat("yyMMddHHss");
			
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
			params.put("usuario", 		  this.usuario);

			log.info("Generando reporte ... ");
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO - " + respuesta.getErrores().getCodigoError() + ": " + respuesta.getErrores().getDescError());
				control(true, -1, "Ocurrio un problema al intentar imprimir la SALIDA\nNo se genero el reporte");
				return;
			} 

			log.info("Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "E-AL-" + this.pojoMovimiento.getId() + "-" + formatter.format(Calendar.getInstance().getTime());
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido de la Entrada a Almacen");
				control(true, -1, "Ocurrio un problema al intentar imprimir la Entrada a Almacen");
				return;
			}

			log.info("Lanzando reporte ... ");
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			log.info("Maestro exportado. Proceso finalizado");
		} catch (Exception e) {
			log.error("Error en Inventarios.EntradasAlmacenAction.reporte()", e);
			control(true, -1, "Ocurrio un problema al intentar imprimir la Entrada a Almacen");
		} 
	}
	
	public void verificarProducto() {
		
	}

	private void cargarAlmacenes() {
		boolean asignarEntrega = false;
		
		this.listaAlmacenes = this.ifzAlmacen.findAllActivos();
		if (this.listaAlmacenes != null && ! this.listaAlmacenes.isEmpty()) {
			Collections.sort(this.listaAlmacenes, new Comparator<Almacen>() {
				@Override
				public int compare(Almacen o1, Almacen o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
		}
		
		if (this.listaAlmacenes == null)
			this.listaAlmacenes = new ArrayList<Almacen>();
		
		if (this.listaCboAlmacenes == null)
			this.listaCboAlmacenes = new ArrayList<SelectItem>();
		this.listaCboAlmacenes.clear();
		
		for (Almacen a : this.listaAlmacenes) {
			if (! this.baseTraspaso && (this.almacenesAdministrativos && a.getTipo() == 3))
				continue;
			if (! this.baseTraspaso && (! this.almacenesAdministrativos && a.getTipo() == 1))
				continue;
			this.listaCboAlmacenes.add(new SelectItem(a.getId(), a.getNombre()));

			// Asignamos sugerencia de Almacen si corresponde			
			if (this.pojoMovimiento == null || (this.listEmpleadoAlmacenes == null || this.listEmpleadoAlmacenes.isEmpty()))
				continue;
			for (Almacen var : this.listEmpleadoAlmacenes) {
				if (var.getTipo() == 1 || var.getTipo() == 3)
					continue;
				if (a.getId().longValue() == var.getId().longValue()) {
					this.pojoMovimiento.setAlmacen(a);
					asignarEntrega = true;
					break;
				}
			}
		}
		
		if (asignarEntrega)
			seleccionaEncargado();
	}

	private boolean comprobarUsuario() {
		List<Empleado> listEmpleado = null;
		Long idEmpleado = null;
		List<Almacen> listAux = null;
		Long idPuesto = null;
		boolean puestoValido = false;
		
		try {
			this.idSucursalBase = 0L;
			if (this.loginManager == null)
				return false;
			
			if ("ADMINISTRADOR".equals(this.usuario))
				return true;
	
			if (this.listPuestosValidos == null || this.listPuestosValidos.isEmpty()) {
				log.info("Sin puestos para comparar");
				return false;
			}
			
			// Determinamos EMPLEADO de manera directa o por correo electronico
			// ----------------------------------------------------------------------------------
			idEmpleado = this.loginManager.getUsuarioResponsabilidad().getUsuario().getIdEmpleado();
			if (idEmpleado == null || idEmpleado <= 1L) {
				listEmpleado = this.ifzEmpleado.findByProperty("email", this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo(), 0);
				if (listEmpleado != null && listEmpleado.size() == 1)
					idEmpleado = listEmpleado.get(0).getId();
			}
			
			if (idEmpleado == null || idEmpleado <= 1L) {
				log.info("Usuario sin Empleado asociado");
				return false;
			}
			
			// Recuperamos pojo EMPLEADO
			this.pojoEmpleadoUsuario = this.ifzEmpleado.findByIdExt(idEmpleado);
			if (this.pojoEmpleadoUsuario == null || this.pojoEmpleadoUsuario.getId() == null || this.pojoEmpleadoUsuario.getId() <= 0L){
				log.info("No se encontro Empleado asociado al Usuario");
				return false;
			}
			
			// Recuperamos SUCURSAL base del Empleado
			// ----------------------------------------------------------------------------------
			this.idSucursalBase = this.pojoEmpleadoUsuario.getSucursal().getId();
			
			// Recuperamos ALMACEN/BODEGA base del Empleado
			// ----------------------------------------------------------------------------------
			this.listEmpleadoAlmacenes = this.ifzAlmacen.findByProperty("idEncargado", this.pojoEmpleadoUsuario.getId());
			if (this.listEmpleadoAlmacenes == null|| this.listEmpleadoAlmacenes.isEmpty()) {
				// Tiene el Puesto necesario pero aun no ha sido asignado a ningun Almacen/Bodega.
				// Entonces, recupero los Almacenes/Bodegas de la Sucursal asignada al Empleado
				this.listEmpleadoAlmacenes = this.ifzAlmacen.findByProperty("idSucursal", this.idSucursalBase);
				if (this.listEmpleadoAlmacenes != null && ! this.listEmpleadoAlmacenes.isEmpty()) {
					listAux = new ArrayList<Almacen>();
					for (Almacen var : this.listEmpleadoAlmacenes) {
						// Excluimos Administrativos si no tiene el perfil
						if (var.getTipo() > 2 && ! this.PERFIL_ADMINISTRATIVO)
							continue;
						listAux.add(var);
					}
					
					this.listEmpleadoAlmacenes.clear();
					this.listEmpleadoAlmacenes.addAll(listAux);
					listAux.clear();
				} else {
					this.listEmpleadoAlmacenes = new ArrayList<Almacen>();
				}
			}
			
			this.almacenBodega = 0;
			for (Almacen var : this.listEmpleadoAlmacenes) {
				/*if (var.getTipo() == 1 || var.getTipo() == 3) // Si es Almacen (principal)
					this.almacenBodega = (this.almacenBodega == 2 || this.almacenBodega == 3) ? 3 : 1;*/
				if (var.getTipo() == 2 || var.getTipo() == 4) // Si es Bodega (obra)
					this.almacenBodega = (this.almacenBodega == 1 || this.almacenBodega == 3) ? 3 : 2;
			}
			
			// Comprobamos PUESTO
			// ----------------------------------------------------------------------------------
			if (this.pojoEmpleadoUsuario.getPuestoCategoria() != null 
					&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto() != null 
					&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() != null 
					&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() <= 0L) {
				log.info("Empleado sin puesto asignado");
				return false;
			}
			
			// Validamos PUESTO
			puestoValido = false;
			idPuesto = this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId();
			for (Long idPuestoValido : this.listPuestosValidos) {
				if (idPuesto.longValue() == idPuestoValido.longValue()) {
					puestoValido = true;
					break;
				}
			}
			
			if (! puestoValido) {
				log.info("Empleado sin puesto valido para Entradas de Almacen");
				return false;
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema en Inventarios.EntradasAlmacenAction.comprobarUsuario()", e);
			return false;
		}
		
		return true;
	}
	
	public void eliminarProductosGrid() {
		this.listaProductosSalida.clear();
		this.numeroItems = 0;
		this.almacenAnterior = 0;
	}
	
	public void regresarAlmacen() {
		log.info("Almacen anterior:"+this.getIdAlmacen());
	}
	
	private boolean validaAgregarProducto() {
		control();
		if (this.pojoProductoBusqueda == null || this.pojoProductoBusqueda.getId() == null || this.pojoProductoBusqueda.getId() <= 0L)
			return false;
		
		if (comprobarProductoEnLista()) {
			control(true, -10, "El producto ya se encuentra en la Lista");
			return false;
		}
		
		return true;
	}
	
	private boolean comprobarProductoEnLista() {
		if (this.listaProductosSalida == null)
			this.listaProductosSalida = new ArrayList<MovimientosDetalleExt>();
		
		for (MovimientosDetalleExt p : this.listaProductosSalida) {
			if (this.pojoProductoBusqueda.getClave().equals(p.getProducto().getClave()))
				return true;
		}
		
		return false;
	}
	
	public void validaCantidadProducto(AjaxBehaviorEvent event) {
		int currentTraspasoDetalle = -1;
		
		control();
		if (! this.puedeEditar) 
			return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
		
		if (this.listaProductosSalida.isEmpty()) 
			return;
		
		if (event.getComponent().getAttributes().get("currentIndex") == null)
			return;
		currentTraspasoDetalle = (int) event.getComponent().getAttributes().get("currentIndex");
		
		this.movimientoSeleccionado = this.listaProductosSalida.get(currentTraspasoDetalle);
    	log.info("Elemento seleccionado : " + this.movimientoSeleccionado.getProducto().getId() 
    			+ ", Producto : " + this.movimientoSeleccionado.getProducto().getDescripcion() 
    			+ ", Cant : " + this.movimientoSeleccionado.getCantidad());
        this.cantidadProductoSeleccionado = this.ifzAlmacenProducto.findCantidadEnAlmacen(this.pojoMovimiento.getAlmacen().getId(), this.movimientoSeleccionado.getProducto().getId());
    	log.info("Cantidad del producto en Almacen: " + this.cantidadProductoSeleccionado);
    	
    	//asignar la cantidad que existe en almacen al produdcto en el campo: cantidadSolicitada
    	this.setCantidadExistente(this.movimientoSeleccionado.getProducto().getClave(), this.cantidadProductoSeleccionado);
		
		for (MovimientosDetalleExt movimiento : this.listaProductosSalida) {
			if (movimiento.getProducto().getClave().equals(this.movimientoSeleccionado.getProducto().getClave())) {
				log.info("Revisar: Almacen: " + this.pojoMovimiento.getAlmacen().getId() 
						+ ", Producto: " + movimiento.getProducto().getId());
				this.cantidadProductoSeleccionado = this.ifzAlmacenProducto.findCantidadEnAlmacen(this.pojoMovimiento.getAlmacen().getId(), movimiento.getProducto().getId());
				log.info("Cantidad Producto : " + this.cantidadProductoSeleccionado
						+ ", Movimiento Cantidad : " + movimiento.getCantidad()
						+ ", Movimiento Index : " + this.listaProductosSalida.indexOf(movimiento));
			}
		}
	}
	
	private void setCantidadExistente(String claveProducto, double cantidadExistente) {
		for (MovimientosDetalleExt md : this.listaProductosSalida) {
			if (md.getProducto().getClave().equals( claveProducto)) {
				md.setCantidadSolicitada(cantidadExistente);
				break;
			}
		}
	}

	private boolean validaGuardarMovimiento() {
		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
			control(-12, "Seleccione Obra");
			return false;
		}
		
		if (this.pojoMovimiento.getAlmacen() == null) {
			control(-13, "Seleccione almacén");
			return false;
		}
		
		if (this.pojoMovimiento.getEntrega().getId() == null) {
			control(-14, "Indique el empleado que entrega");
			return false;
		}
		
		if (this.pojoMovimiento.getRecibe().getId() == null) {
			control(-15, "Indique el empleado que recibe");
			return false;
		}
		
		if (this.pojoMovimiento.getRecibe().getId().compareTo(this.pojoMovimiento.getEntrega().getId()) == 0) {
			control(-16, "La persona que entrega y que recibe deben ser diferentes");
			return false;
		}

		if (this.listaProductosSalida.isEmpty()) {
			control(-17, "Debe existir por lo menos un producto en la lista");
			return false;
		}
		
		//Revisar si alguno se encuentra en cero
		for (MovimientosDetalleExt md : this.listaProductosSalida) {
			if ( md.getCantidad() == 0) {
				control(-18, "La cantidad de producto no puede ser cero.");
				return false;
			}
		}
		
		//Si en alguno de los productos se encuentra que la cantidad es mayor a la que hay en almacen, salir
		for (MovimientosDetalleExt md : this.listaProductosSalida) {
			if ( md.getCantidad() > md.getCantidadSolicitada()) {
				control(-19, "No existe suficiente cantidad de producto.");
				return false;
			}
		}
		
		return true;
	}
	
	private void control() {
		this.operacionCompleta = true;
		this.band = false;
		this.mensaje = "";
		this.tipoMensaje = 0;
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(boolean band, int tipoMensaje, String mensaje) {
		control(band, tipoMensaje, mensaje, null);
	}
	
	private void control(boolean band, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.operacionCompleta = false;
		this.band = band;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		codigo = "Codigo: " + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		if (this.band)
			log.error("\n\nSALIDAS_ALMACEN :: " + this.usuario + " :: " + codigo + "\n" + mensaje + "\n", throwable);
	}
	
	// --------------------------------------------------------------------
	// BUSQUEDA OBRAS 
	// --------------------------------------------------------------------

	public void nuevaBusquedaObras() {
		control();
		if (this.listObrasPrincipales == null)
			this.listObrasPrincipales = new ArrayList<Obra>();
		this.listObrasPrincipales.clear();
		
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.valorOpcionBusquedaObras = 0;
		this.avanzadaBusquedaObras = false;
		this.numPaginaObras = 1;
	}

	public void buscarObras() {
    	try {
    		control();
			if (this.campoBusquedaObras == null || "".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
			
			this.valorOpcionBusquedaObras = 0;
			if (this.buscarAdministrativo)
				this.valorOpcionBusquedaObras = 4;

			this.listObrasPrincipales = this.ifzObras.findSubObraLike(this.campoBusquedaObras, this.valorBusquedaObras, this.valorOpcionBusquedaObras, "nombre", 0);
			if (this.listObrasPrincipales == null || this.listObrasPrincipales.isEmpty()) {
				this.listObrasPrincipales = new ArrayList<Obra>();
				control(true, 2, "Busqueda sin resultados");
	    		return;
			}

			this.mensaje = "OK";
    	} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al consultar las Obras", e);
    	} finally {
    		log.info(this.listObrasPrincipales.size() + " Obra encontradas");
			this.numPaginaObras = 1;
    	}
    }

	public void seleccionarObra() {
		control();
		
		// Compruebo seleccion de Obra
		if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L)
			return;

		this.almacenesAdministrativos = (this.pojoObra.getTipoObra() == 4);
		cargarAlmacenes();
		
		// Autoasignamos quien RECIBE el material de acuerdo al Responsable de la Obra elegida
		for (Empleado e : this.listaEmpleados) {
			if (this.pojoObra.getIdResponsable() == null || this.pojoObra.getIdResponsable() <= 0L)
				continue;
			
			if (e.getId().longValue() == this.pojoObra.getIdResponsable().longValue()) {
				this.pojoMovimiento.setRecibe(e);
				break;
			}
		}
	}

	public void toggleAvanzadaBusquedaObras() {
		this.avanzadaBusquedaObras = ! this.avanzadaBusquedaObras;
	}
	
	// --------------------------------------------------------------------
	// BUSQUEDA PRODUCTOS 
	// --------------------------------------------------------------------

	public void nuevaBusquedaProductos() {
		control();
		if (this.listaBusquedaProductos == null)
			this.listaBusquedaProductos = new ArrayList<Producto>();
		this.listaBusquedaProductos.clear();
		this.pojoProductoBusqueda = new Producto();
		
		this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
		this.valorBusquedaProductos = "";
		this.numPaginaProductos = 1;
		
		this.idFamilia = 0;
		this.avanzadaBusquedaProductos = false;
		this.admvaBusquedaProductos = false;
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L) {
			if (this.pojoObra.getTipoObra() == 4) // ADMINISTRATIVA
				this.admvaBusquedaProductos = true;
		}
	}

	public void buscarProductos() {
		List<AlmacenProductoExt> resultados = null;
		int tipoMaestro = 1; // MAESTRO de Insumos
		
		try {
			control();
			if (this.listaBusquedaProductos != null)
				this.listaBusquedaProductos.clear();
			this.numPaginaProductos = 1;
			
			if (this.campoBusquedaProductos == null)
				this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
			
			tipoMaestro = 1;
			if (this.admvaBusquedaProductos)
				tipoMaestro = 2; // MAESTRO Administrativo
			
			// Buscamos productos en Almacen
			resultados = this.ifzAlmacenProducto.findExtExistentes(this.pojoMovimiento.getAlmacen().getId(), this.campoBusquedaProductos, this.valorBusquedaProductos, this.idFamilia, tipoMaestro, 0, false);
			if (resultados == null || resultados.isEmpty()) {
				log.info("ERROR 2 : Busqueda sin resultados");
				control(true, 2, "Busqueda sin resultados");
				return;
			}
			
			// Recuperamos productos
			for (AlmacenProductoExt var : resultados) {
				if (var.getExistencia() > 0) {
					var.getProducto().setExistencia(var.getExistencia());
					this.listaBusquedaProductos.add(var.getProducto());
				}
			}
			
			// Ordenamos por descripcion
			Collections.sort(this.listaBusquedaProductos, new Comparator<Producto>() {
				@Override
				public int compare(Producto o1, Producto o2) {
					return o1.getDescripcion().compareTo(o2.getDescripcion());
				}
			});
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al consultar los Productos del Almacen seleccioando", e);
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
			this.productoSalidaSeleccionado.setCantidad(1);
			this.listaProductosSalida.add(this.productoSalidaSeleccionado);
			this.numeroItems = this.listaProductosSalida.size();
			
			// Asignamos 'almacenAnterior' si corresponde 
			if (this.almacenAnterior == 0)	
				this.almacenAnterior = this.pojoMovimiento.getAlmacen().getId();

			nuevaBusquedaProductos();
			control(false, 4, "Producto Añadido");
			this.operacionCompleta = true;
		} catch (Exception e) {
			control(true, -1, "Ocurrio un probla al recuperar el Producto seleccionado.", e);
		}
	}

	public void toggleAvanzadaBusquedaProductos() {
		this.avanzadaBusquedaProductos = ! this.avanzadaBusquedaProductos;
	}

	// ------------------------------------------------------------------------------------
	// EMPLEADOS
	// ------------------------------------------------------------------------------------

	private void cargarEmpleados() {
		if (this.listaEmpleados == null)
			this.listaEmpleados = new ArrayList<Empleado>();
		this.listaEmpleados.clear();
		
		this.listaEmpleados = this.ifzEmpleado.findAllActivos();
		if (this.listaEmpleados != null && ! this.listaEmpleados.isEmpty()) {
			Collections.sort(this.listaEmpleados, new Comparator<Empleado>() {
				@Override
				public int compare(Empleado o1, Empleado o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
		}
		
		cargarCboEmpleadoEntrega();
		cargarCboEmpleadoRecibe();
	}
	
	private void cargarCboEmpleadoEntrega() {
		if (this.listaCboEmpleadoEntrega == null)
			this.listaCboEmpleadoEntrega = new ArrayList<SelectItem>();
		this.listaCboEmpleadoEntrega.clear();
		
		if (this.listaEmpleados != null && ! this.listaEmpleados.isEmpty()) {
			for (Empleado e : this.listaEmpleados)
				this.listaCboEmpleadoEntrega.add(new SelectItem(e.getId() , e.getNombre()));
		}
	}
	
	private void cargarCboEmpleadoRecibe() {
		if (this.listaCboEmpleadoRecibe == null)
			this.listaCboEmpleadoRecibe = new ArrayList<SelectItem>();
		this.listaCboEmpleadoRecibe.clear();

		if (this.listaEmpleados != null && ! this.listaEmpleados.isEmpty()) {
			for (Empleado e : this.listaEmpleados) 
				this.listaCboEmpleadoRecibe.add(new SelectItem(e.getId(), e.getNombre()));
		}
	}

	public void seleccionaEncargado() {
		if (this.pojoMovimiento.getAlmacen() == null || this.pojoMovimiento.getAlmacen().getId() == null || this.pojoMovimiento.getAlmacen().getId() <= 0L)
			return;
		
		for (Empleado e : this.listaEmpleados) {
			if (this.pojoMovimiento.getAlmacen().getIdEncargado() == null || this.pojoMovimiento.getAlmacen().getIdEncargado() <= 0L)
				continue;
			
			if (e.getId().longValue() == this.pojoMovimiento.getAlmacen().getIdEncargado().longValue()) {
				this.pojoMovimiento.setEntrega(e);
				break;
			}
		}
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
			control(true, -1, "Ocurrio un problema al recuperar las Familias", e);
		}
	}
	
	// --------------------------------------------------------------------
	// PROPIEDADES 
	// --------------------------------------------------------------------

	public String getTitulo() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getId() != null && this.pojoMovimiento.getId() > 0L)
			return "Salida a Obra " + this.pojoMovimiento.getId();
		return "Nueva Salida a Obra";
	}
	
	public void setTitulo(String value) {}
	
	public String getTraspaso() {
		if (this.pojoMovimiento != null && this.pojoTraspaso != null && this.pojoTraspaso.getId() != null && this.pojoTraspaso.getId() > 0L) 
			return "Traspaso " + this.pojoTraspaso.getId() + " a Bodega [Salida " + this.pojoMovimiento.getId() + "]";
		return "";
	}
	
	public void setTraspaso(String value) {}
	
	public boolean getDeshabilitarProductosAdministrativos() {
		/*if ("ADMINISTRADOR".equals(this.usuario))
			return false;
		return true;*/
		return false;
	}
	
	public void setDeshabilitarProductosAdministrativos(boolean value) {}
	
	public String getObra() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		return "";
	}
	
	public void setObra(String value) {}
	
	public AlmacenMovimientosExt getPojoMovimiento() {
		return pojoMovimiento;
	}

	public void setPojoMovimiento(AlmacenMovimientosExt pojoMovimiento) {
		this.pojoMovimiento = pojoMovimiento;
	}

	public Date getFecha() {
		return this.pojoMovimiento.getFecha() == null ? Calendar.getInstance().getTime() : this.pojoMovimiento.getFecha(); 
	}
	
	public void setFecha(Date fecha) {
		this.pojoMovimiento.setFecha(fecha);
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

	public boolean isBand() {
		return band;
	}

	public void setBand(boolean band) {
		this.band = band;
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

	public int getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(int campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public List<AlmacenMovimientosExt> getListaMovimientosGrid() {
		return listaMovimientosGrid;
	}

	public void setListaMovimientosGrid(List<AlmacenMovimientosExt> listaMovimientosGrid) {
		this.listaMovimientosGrid = listaMovimientosGrid;
	}
	
	public List<Almacen> getListaAlmacenes() {
		return listaAlmacenes;
	}

	public void setListaAlmacenes(List<Almacen> listaAlmacenes) {
		this.listaAlmacenes = listaAlmacenes;
	}

	public List<SelectItem> getListaCboAlmacenes() {
		return listaCboAlmacenes;
	}

	public void setListaCboAlmacenes(List<SelectItem> listaCboAlmacenes) {
		this.listaCboAlmacenes = listaCboAlmacenes;
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
		return listObrasPrincipales;
	}

	public void setListObrasPrincipales(List<Obra> listObrasPrincipales) {
		this.listObrasPrincipales = listObrasPrincipales;
	}

	public int getNumPaginaOrdenCompra() {
		return numPaginaOrdenCompra;
	}

	public void setNumPaginaOrdenCompra(int numPaginaOrdenCompra) {
		this.numPaginaOrdenCompra = numPaginaOrdenCompra;
	}

	public List<MovimientosDetalleExt> getListaProductosSalida() {
		return listaProductosSalida;
	}

	public void setListaProductosSalida(List<MovimientosDetalleExt> listaProductosSalida) {
		this.listaProductosSalida = listaProductosSalida;
	}

	public List<Producto> getListaBusquedaProductos() {
		return listaBusquedaProductos;
	}

	public void setListaBusquedaProductos(List<Producto> listaBusquedaProductos) {
		this.listaBusquedaProductos = listaBusquedaProductos;
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
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public AlmacenMovimientosExt getPojoMovimientoEditar() {
		return pojoMovimientoEditar;
	}

	public void setPojoMovimientoEditar(AlmacenMovimientosExt pojoMovimientoEditar) {
		this.pojoMovimientoEditar = pojoMovimientoEditar;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public List<SelectItem> getListaCboEmpleadoEntrega() {
		return listaCboEmpleadoEntrega;
	}

	public void setListaCboEmpleadoEntrega(List<SelectItem> listaCboEmpleadoEntrega) {
		this.listaCboEmpleadoEntrega = listaCboEmpleadoEntrega;
	}

	public List<SelectItem> getListaCboEmpleadoRecibe() {
		return listaCboEmpleadoRecibe;
	}

	public void setListaCboEmpleadoRecibe(List<SelectItem> listaCboEmpleadoRecibe) {
		this.listaCboEmpleadoRecibe = listaCboEmpleadoRecibe;
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
	
	public boolean isBuscarAdministrativo() {
		return buscarAdministrativo;
	}
	
	public void setBuscarAdministrativo(boolean buscarAdministrativo) {
		this.buscarAdministrativo = buscarAdministrativo;
	}

	public boolean isAdmvaBusquedaProductos() {
		return admvaBusquedaProductos;
	}

	public void setAdmvaBusquedaProductos(boolean admvaBusquedaProductos) {
		this.admvaBusquedaProductos = admvaBusquedaProductos;
	}

	public boolean isUsuarioValido() {
		return usuarioValido;
	}

	public void setUsuarioValido(boolean usuarioValido) {
		this.usuarioValido = usuarioValido;
	}

	public boolean isAvanzadaBusquedaProductos() {
		return avanzadaBusquedaProductos;
	}

	public void setAvanzadaBusquedaProductos(boolean avanzadaBusquedaProductos) {
		this.avanzadaBusquedaProductos = avanzadaBusquedaProductos;
	}

	public boolean isAvanzadaBusquedaObras() {
		return avanzadaBusquedaObras;
	}

	public void setAvanzadaBusquedaObras(boolean avanzadaBusquedaObras) {
		this.avanzadaBusquedaObras = avanzadaBusquedaObras;
	}

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
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

	public boolean getBaseTraspaso() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getIdTraspaso() > 0L && this.pojoTraspaso != null && this.pojoTraspaso.getId() != null && this.pojoTraspaso.getId() > 0L)
			return true;
		return false;
	}
	
	public void setBaseTraspaso(boolean value) {}

	public long getIdAlmacen() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getAlmacen() != null && this.pojoMovimiento.getAlmacen().getId() != null && this.pojoMovimiento.getAlmacen().getId() > 0L)
			return this.pojoMovimiento.getAlmacen().getId();
		return 0L;
	}

	public void setIdAlmacen(long idAlmacen) {
		if (idAlmacen <= 0L)
			return;
		
		for (Almacen var : this.listaAlmacenes) {
			if (idAlmacen == var.getId().longValue()) {
				this.pojoMovimiento.setAlmacen(var);
				break;
			}
		}
	}

	public long getEntrega() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getEntrega() != null && this.pojoMovimiento.getEntrega().getId() != null && this.pojoMovimiento.getEntrega().getId() > 0L)
			return this.pojoMovimiento.getEntrega().getId();
		return 0L; 
	}
	
	public void setEntrega(long idEmpleado) {
		if (idEmpleado <= 0) 
			return;
		
		for (Empleado var : this.listaEmpleados) {
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
		if (idEmpleado <= 0) 
			return;
		
		for (Empleado var : this.listaEmpleados) {
			if (idEmpleado == var.getId().longValue()) {
				this.pojoMovimiento.setRecibe(var);
				break;
			}
		}
	}

	public long getIdAlmacenSalida() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getAlmacenOrigen() != null && this.pojoTraspaso.getAlmacenOrigen().getId() != null)
			return this.pojoTraspaso.getAlmacenOrigen().getId();
		return 0L;
	}

	public void setIdAlmacenSalida(long idAlmacen) {}
	
	public long getIdAlmacenDestino() {
		if (this.pojoTraspaso != null && pojoTraspaso.getAlmacenDestino() != null && this.pojoTraspaso.getAlmacenDestino().getId() != null)
			return this.pojoTraspaso.getAlmacenDestino().getId();
		return 0L;
	}
	
	public void setIdAlmacenDestino(long idAlmacen) {}

	public long getIdEmpleadoEntrega() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getEntrega() != null && this.pojoTraspaso.getEntrega().getId() != null && this.pojoTraspaso.getEntrega().getId() > 0L)
			return this.pojoTraspaso.getEntrega().getId();
		return 0L; 
	}
	
	public void setIdEmpleadoEntrega(long idEmpleado) {
		if (idEmpleado <= 0) 
			return;
		
		for (Empleado var : this.listaEmpleados) {
			if (idEmpleado == var.getId().longValue()) {
				this.pojoTraspaso.setEntrega(var);
				break;
			}
		}
	}

	public long getIdEmpleadoRecibe() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getRecibe() != null && this.pojoTraspaso.getRecibe().getId() != null && this.pojoTraspaso.getRecibe().getId() > 0L)
			return this.pojoTraspaso.getRecibe().getId();
		return 0L; 
	}

	public void setIdEmpleadoRecibe(long idEmpleado) {
		if (idEmpleado <= 0) 
			return;
		
		for (Empleado var : this.listaEmpleados) {
			if (idEmpleado == var.getId().longValue()) {
				this.pojoTraspaso.setRecibe(var);
				break;
			}
		}
	}

	public List<SelectItem> getListaCboAlmacenSalida() {
		return listaCboAlmacenes;
	}

	public void setListaCboAlmacenSalida(List<SelectItem> listaCboAlmacenSalida) {
		this.listaCboAlmacenes = listaCboAlmacenSalida;
	}

	public List<SelectItem> getListaCboAlmacenDestino() {
		return listaCboAlmacenes;
	}

	public void setListaCboAlmacenDestino(List<SelectItem> listaCboAlmacenDestino) {
		this.listaCboAlmacenes = listaCboAlmacenDestino;
	}

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
}
