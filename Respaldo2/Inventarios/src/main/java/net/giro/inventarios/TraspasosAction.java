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
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.beans.TipoMovimientoInventario;
import net.giro.inventarios.beans.TipoMovimientoReferencia;
import net.giro.inventarios.beans.TipoMovimientoReferenciaExtendida;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicInventariosEventos;
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
	private HttpSession httpSession;
	private InitialContext ctx;
	private AlmacenTraspasoRem ifzTraspaso;
	private TraspasoDetalleRem ifzTraspasoDetalle;
	private AlmacenRem ifzAlmacen;
	private ProductoRem ifzProductos;
	private AlmacenProductoRem ifzAlmacenProducto;
	private EmpleadoRem ifzEmpleado;
	private ObraAlmacenesRem ifzObrasAlmacenes;
	private AlmacenMovimientosRem ifzMovimientos;
	private MovimientosDetalleRem ifzMovimientosDetalle;
	private ReportesRem ifzReportes;
	private ConGrupoValoresRem ifzGpoValores;
	private ConValoresRem ifzConValores;
    private long usuarioId;	  
    private String usuario;
	private boolean operacionCompleta;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private boolean puedeEditar;
	private boolean PERFIL_ADMINISTRATIVO;
	private int traspasoEstatus;
	// Almacenes
	private List<Almacen> listAlmacenes;
	private List<SelectItem> listAlmacenesOrigen;
	private List<SelectItem> listAlmacenesDestino;
	private boolean almacenesAdministrativos;
	// Empleados
	private List<Empleado> listEmpleados;
	private List<SelectItem> listEmpleadosEntrega;
	private List<SelectItem> listEmpleadosRecibe;
	private AlmacenTraspasoExt pojoTraspaso;
	//private AlmacenMovimientos pojoMovimiento;
	private TraspasoDetalleExt pojoTraspasoDetalleEliminar;
	private List<AlmacenTraspasoExt> listaTraspasoGrid;
	private List<TraspasoDetalleExt> listaTraspasoDetalleGrid;
	private List<TraspasoDetalleExt> listaDetalleCantidades;	//Lista para respaldar las cantidades de productos
	// BUSQUEDA PRINCIPAL
	private List<SelectItem> listaCampoBusqueda;
	private int campoBusqueda;  
	private String valorBusqueda;
	private int numPagina;
	private int pagDetalles;
	//Busqueda Productos
	private List<AlmacenProductoExt> listaBusquedaProductos; //Producto
	private AlmacenProductoExt pojoProductoBusqueda; // Producto
	private List<SelectItem> tiposBusquedaProductos;	
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private int valorOpcionBusquedaProductos;
	private int numPaginaProductos;
	private boolean avanzadaBusquedaProductos;
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
	private boolean usuarioValido;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalBase;
	private List<Almacen> listEmpleadoAlmacenes;
	private int almacenBodega;
	private List<Long> listPuestosValidos;
	
	
	public TraspasosAction() throws NamingException,Exception {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String resValue = "";
		String[] splitted = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario   = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

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

			this.ctx = new InitialContext();
			this.ifzTraspaso = (AlmacenTraspasoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
			this.ifzTraspasoDetalle = (TraspasoDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			this.ifzAlmacen = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzProductos = (ProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzAlmacenProducto = (AlmacenProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzObrasAlmacenes = (ObraAlmacenesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzMovimientos = (AlmacenMovimientosRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzMovimientosDetalle = (MovimientosDetalleRem) this.ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzGpoValores = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");

			this.ifzTraspaso.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspasoDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacenProducto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
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

			this.usuarioValido = false;
			if (comprobarUsuario())
				this.usuarioValido = true;
			
			// Tipos de Traspasos
			this.listTiposTraspasos = new ArrayList<SelectItem>();
			switch (this.almacenBodega) {
				case 1:
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString(), "Traspaso de Almacen a Almacen"));
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString(), "Traspaso de Almacen a Bodega"));
					break;
				case 2:
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_BODEGA_BODEGA.toString(), "Traspaso Bodega a Bodega"));
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString(), "Devolucion a Almacen"));
					break;
				default:
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString(), "Traspaso de Almacen a Almacen"));
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString(), "Traspaso de Almacen a Bodega"));
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_BODEGA_BODEGA.toString(), "Traspaso de Bodega a Bodega"));
					this.listTiposTraspasos.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString(), "Devolucion a Almacen"));
					break;
			}
			this.tipoTraspaso = this.listTiposTraspasos.get(0).getValue().toString();
			
			// Busqueda principal
			this.listaCampoBusqueda = new ArrayList<SelectItem>();
			this.listaCampoBusqueda.add( new SelectItem("1", "Almacen Origen"));
			this.listaCampoBusqueda.add( new SelectItem("2", "Almacen Destino"));
			this.campoBusqueda = Integer.parseInt(this.listaCampoBusqueda.get(0).getValue().toString());
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda Productos
			this.tiposBusquedaProductos = new ArrayList<SelectItem>();
			this.tiposBusquedaProductos.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaProductos.add(new SelectItem("clave", "Codigo"));
			this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
			this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
			this.valorBusquedaProductos = "";
			this.numPaginaProductos = 1;
			
			this.listaTraspasoGrid = new ArrayList<AlmacenTraspasoExt>();
			this.listAlmacenesOrigen = new ArrayList<SelectItem>();
			this.listAlmacenesDestino = new ArrayList<SelectItem>();
			cargarFamilias();
			
			nuevoTraspaso();
		} catch (Exception e) {
			log.error("Ocurrio un problema al instanciar TraspasosAction", e);
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if (this.listaTraspasoGrid != null)
				this.listaTraspasoGrid.clear();

			log.info("Buscar por almacen: " + this.valorBusqueda);
			this.listaTraspasoGrid = this.ifzTraspaso.findExtByAlmacen(this.valorBusqueda);
			if (this.listaTraspasoGrid == null || this.listaTraspasoGrid.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}

			Collections.sort(this.listaTraspasoGrid, new Comparator<AlmacenTraspasoExt>() {
				@Override
				public int compare(AlmacenTraspasoExt o1, AlmacenTraspasoExt o2) {
					return o2.getFecha().compareTo(o1.getFecha());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Traspasos", e);
    	}
	}

	public void nuevoTraspaso() {
		try {
			control();
			this.puedeEditar = true;
			this.traspasoEstatus = -1;
			this.pojoTraspaso = new AlmacenTraspasoExt();
			this.listaTraspasoDetalleGrid = new ArrayList<TraspasoDetalleExt>();
			this.listaDetalleCantidades = new ArrayList<TraspasoDetalleExt>();
			this.cantidadProductoDetalles = 0;
			
			cargarAlmacenes();
			cargarEmpleados();
			if (! this.usuarioValido) {
				control(-1, "No es un usuario autorizado para realizar Traspasos a Almacenes/Bodegas");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al inicializar un nuevo Traspaso", e);
		}
	}
	
	public void editar() {
		Almacen almOrigen = null;
		Almacen almDestino = null;
		Empleado entrega = null;
		Empleado recibe = null;
		
		try {
			control();
			this.puedeEditar = false;
			// Obtengo almacenes y empleados asignados
			this.tipoTraspaso = "";
			this.traspasoEstatus = 0;
			if (this.pojoTraspaso.getTipo() == 1)
				this.tipoTraspaso = TipoMovimientoReferencia.TRASPASO.toString();
			else if (this.pojoTraspaso.getTipo() == 2)
				this.tipoTraspaso = TipoMovimientoReferencia.DEVOLUCION.toString();
				
			almOrigen = this.pojoTraspaso.getAlmacenOrigen();
			almDestino = this.pojoTraspaso.getAlmacenDestino();
			entrega = this.pojoTraspaso.getEntrega();
			recibe = this.pojoTraspaso.getRecibe();
	
			// Cargamos Almacenes y reasignamos valores
			cargarAlmacenes();
			this.pojoTraspaso.setAlmacenOrigen(almOrigen);
			filtrarAlmacenesDestino();
			this.pojoTraspaso.setAlmacenDestino(almDestino);
	
			// Cargamos Empleados y reasignamos valores
			cargarEmpleados();
			this.pojoTraspaso.setEntrega(entrega);
			cargarCboEmpleadoRecibe();
			this.pojoTraspaso.setRecibe(recibe);
	
			// Cargo los detalles del Traspaso
			this.listaTraspasoDetalleGrid = this.ifzTraspasoDetalle.findExtDetallesByIdTraspaso(this.pojoTraspaso.getId());
			if (this.listaTraspasoDetalleGrid == null || this.listaTraspasoDetalleGrid.isEmpty()) {
				
			}
			//cargarDetalleTraspaso(this.pojoTraspaso.getId());
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los detalles del Traspaso seleccionado", e);
			this.traspasoEstatus = -1;
		} finally {
			// Determinamos estatus si corresponde
			if (this.traspasoEstatus == 0) {
				int recibidos = 0;
				int transito = 0;
				for (TraspasoDetalleExt item : this.listaTraspasoDetalleGrid) {
					if (item.getEstatus() == 2) 
						recibidos += 1;
					else
						transito += 1;
				}
				
				if (recibidos > 0 && transito == 0)
					this.traspasoEstatus = 2;
				else if (recibidos > 0 && transito > 0)
					this.traspasoEstatus = 1;
				else 
					this.traspasoEstatus = 0;
			}
		}
	}
	
	public void guardar() {
		int valTipoTraspaso = 0;
		boolean autoRecibir = false;
		boolean autoRecibido = false;
		boolean movimientoGenerado = false;
		
		try {
			control();
			if (! validaGuardarTraspaso()) 
				return;
			
			valTipoTraspaso = 1;
			if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString()))
				valTipoTraspaso = 2;
			
			if (this.pojoTraspaso.getId() == null || this.pojoTraspaso.getId() <= 0L) {
				this.pojoTraspaso.setTipo(valTipoTraspaso);//this.tipoTraspaso);
				this.pojoTraspaso.setEstatus(0);
				this.pojoTraspaso.setCreadoPor(this.usuarioId);
				this.pojoTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoTraspaso.setModificadoPor(this.usuarioId);
				this.pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_BODEGA_BODEGA.toString())) {
					if (! subprocesoDevolucion())
						return;
				}
				
				// Guardamos Traspaso
				log.info("Guardando Traspaso ... ");
				this.pojoTraspaso.setId(this.ifzTraspaso.save(this.pojoTraspaso));
				
				log.info("Traspaso guardado. Guardando detalles de Traspaso ... ");
				for (TraspasoDetalleExt td : this.listaTraspasoDetalleGrid) {
					td.setIdAlmacenTraspaso(this.pojoTraspaso.getId());
					td.setEstatus(1);	//en transito
					td.setCreadoPor(this.usuarioId);
					td.setFechaCreacion(Calendar.getInstance().getTime());
					td.setModificadoPor(this.usuarioId);
					td.setFechaModificacion(Calendar.getInstance().getTime());

					// Guarda detalle
					td.setId(this.ifzTraspasoDetalle.save(td));
				}
				
				// Añadimos el elemento al inicio de la Lista
				this.listaTraspasoGrid.add(0, this.pojoTraspaso);
				this.operacionCompleta = true;
				
				// Generamos movimiento de salida
				log.info("Proceso Terminado!. Generando Movimiento ... ");
				movimientoGenerado = generaMovimientoInventario(TipoMovimientoInventario.SALIDA); //generaSalida();
				
				// Comprobamos si debemos autorecibir el traspaso generado
				autoRecibir = false;
				if (movimientoGenerado && this.pojoTraspaso.getEntrega().getId().longValue() == this.pojoTraspaso.getRecibe().getId().longValue()) {
					autoRecibir = true;
					autoRecibido = generaMovimientoInventario(TipoMovimientoInventario.ENTRADA);
				}
			} 
		} catch (Exception e) {
			control("Ocurrio un problema al guardar el traspaso", e);
		} finally {
			if (this.operacionCompleta && ! movimientoGenerado)
				control(-1, "Se genero el Traspaso correctamente.\nSin embargo, este proceso requiere generar un movimiento de salida, lo cual no se pudo completar.");
			else if (this.operacionCompleta && autoRecibir && ! autoRecibido)
				control(-1, "Se genero el Traspaso correctamente.\nSin embargo, este proceso requiere la recepcion automatica del mismo, lo cual no se pudo completar.");
			nuevoTraspaso();
			this.operacionCompleta = true;
		}
	}
	
	public void eliminar() {
		for (TraspasoDetalleExt td: this.listaTraspasoDetalleGrid) {
			if (td.getIdProducto().getClave().equals( this.pojoTraspasoDetalleEliminar.getIdProducto().getClave() )) {
				this.listaTraspasoDetalleGrid.remove( td );
				break;
			}
		}
		
		if (this.listaTraspasoDetalleGrid.size() == 0) {
			this.almacenAnterior = 0;
			this.cantidadProductoDetalles = 0;
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
			if (this.pojoTraspaso == null || this.pojoTraspaso.getId() == null || this.pojoTraspaso.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar obtener La Entrada seleccionada");
				return;
			}
			
			// Recuperamos el Movimiento
			listMovs = this.ifzMovimientos.findSalidaByTraspaso(this.pojoTraspaso.getId(), this.pojoTraspaso.getTipo(), 0);
			if (listMovs == null || listMovs.isEmpty()) {
				control(-1, "No se pudo recuperar el Movimiento para el Traspaso seleccionado");
				return;
			}
			
			log.info("Imprimiendo Entrada ... ");
			formatter = new SimpleDateFormat("yyMMddHHss");
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
			params.put("usuario", 		  this.usuario);

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
			nombreDocumento = "T-AL-" + this.pojoTraspaso.getId() + "-" + formatter.format(Calendar.getInstance().getTime());
			
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
		this.listaTraspasoDetalleGrid.clear();
		this.cantidadProductoDetalles = 0;
		this.almacenAnterior = 0;
	}
	
	public void regresarAlmacen() {
		log.info("Almacen anterior:");
	}
	
	public void validaCantidadProducto(AjaxBehaviorEvent event) {
		int currentTraspasoDetalle = -1;
		
		if (! this.puedeEditar) 
			return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
		
		if (this.listaTraspasoDetalleGrid.isEmpty()) 
			return;
		
		if (event.getComponent().getAttributes().get("currentIndex") == null)
			return;
		currentTraspasoDetalle = (int) event.getComponent().getAttributes().get("currentIndex");
		
		this.traspasoSeleccionado = this.listaTraspasoDetalleGrid.get(currentTraspasoDetalle);
		log.info("Elemento seleccionado: " + this.traspasoSeleccionado.getIdProducto().getId() + ", Producto: " + this.traspasoSeleccionado.getIdProducto().getDescripcion() + ", Cant:" + this.traspasoSeleccionado.getCantidad());
    	this.cantidadProductoSeleccionado = this.ifzAlmacenProducto.findCantidadEnAlmacen(this.pojoTraspaso.getAlmacenOrigen().getId(), this.traspasoSeleccionado.getIdProducto().getId());
    	log.info("Cantidad del producto en Almacen: " + this.cantidadProductoSeleccionado);
    	this.setCantidadExistente(this.traspasoSeleccionado.getIdProducto().getClave(), this.cantidadProductoSeleccionado);
		
		for (TraspasoDetalleExt movimiento : this.listaTraspasoDetalleGrid) {
			if (movimiento.getIdProducto().getClave().equals(this.traspasoSeleccionado.getIdProducto().getClave())) {
				log.info(" Producto Cantidad : " + this.cantidadProductoSeleccionado 
						+ ", Movimiento Cantidad : " + movimiento.getCantidad()
						+ ", Movimiento Index : " + this.listaTraspasoDetalleGrid.indexOf(movimiento));
			}
		}
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
				if (var.getTipo() == 1 || var.getTipo() == 3) // Si es Almacen (principal)
					this.almacenBodega = (this.almacenBodega == 2 || this.almacenBodega == 3) ? 3 : 1;
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
	
	private Almacen determinarAlmacenPrincipal(long idOrigen) {
		List<ObraAlmacenes> listObrasAlms = null;
		Almacen resultado = null;
		
		try {
			if (idOrigen > 0L) {
				listObrasAlms = this.ifzObrasAlmacenes.findByProperty("idAlmacen", idOrigen, 0);
				if (listObrasAlms != null && ! listObrasAlms.isEmpty()) {
					idOrigen = listObrasAlms.get(0).getIdObra().getId();
					listObrasAlms = this.ifzObrasAlmacenes.findByProperty("idObra", idOrigen, 0);
					for (ObraAlmacenes var : listObrasAlms) {
						idOrigen = 0L;
						if (var.getAlmacenPrincipal() == 1) {
							idOrigen = var.getIdAlmacen();
							resultado = this.ifzAlmacen.findById(idOrigen);
							if (resultado.getTipo() == 1)
								break;
							else
								resultado = null;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar determinar el Almacen Principal", e);
		}
		
		return resultado;
	}
	
	private boolean generaMovimientoInventario(TipoMovimientoInventario tipoMovimiento) {
		List<MovimientosDetalle> movDetalles = null;
		AlmacenMovimientos movimiento = null;
		MovimientosDetalle movDetalle = null; 
		
		try {
			// Genero Movimiento (Entrada o Salida) y detalles
			log.info("Generando Movimiento y detalles ...");
			movimiento = new AlmacenMovimientos();
			movimiento.setFecha(this.pojoTraspaso.getFecha());
			movimiento.setIdOrdenCompra(0L);
			movimiento.setIdTraspaso(this.pojoTraspaso.getId());
			movimiento.setEntrega(this.pojoTraspaso.getEntrega().getId());
			movimiento.setRecibe(this.pojoTraspaso.getRecibe().getId());
			movimiento.setCreadoPor(this.pojoTraspaso.getCreadoPor());
			movimiento.setFechaCreacion(this.pojoTraspaso.getFechaCreacion());
			movimiento.setModificadoPor(this.pojoTraspaso.getModificadoPor());
			movimiento.setFechaModificacion(this.pojoTraspaso.getFechaModificacion());
			if (TipoMovimientoInventario.ENTRADA.equals(tipoMovimiento)) {
				movimiento.setTipo(TipoMovimientoInventario.ENTRADA.ordinal());
				movimiento.setTipoEntrada(TipoMovimientoReferencia.TRASPASO.toString());
				movimiento.setIdAlmacen(this.pojoTraspaso.getAlmacenDestino().getId());
			} else if (TipoMovimientoInventario.SALIDA.equals(tipoMovimiento)) {
				movimiento.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
				if (this.pojoTraspaso.getTipo() == 1)
					movimiento.setTipoEntrada(TipoMovimientoReferencia.TRASPASO.toString());
				else if (this.pojoTraspaso.getTipo() == 2)
					movimiento.setTipoEntrada(TipoMovimientoReferencia.DEVOLUCION.toString());
				movimiento.setIdAlmacen(this.pojoTraspaso.getAlmacenOrigen().getId());
			} else {
				log.error("Ocurrio un problema al generar el Movimiento de Inventario para el Traspaso. No se pudo determinar el tipo de Movimiento");
				return false;
			}
			
			for (TraspasoDetalleExt td : this.listaTraspasoDetalleGrid) {
				movDetalle = new MovimientosDetalle();
				movDetalle.setIdProducto(td.getIdProducto().getId());
				movDetalle.setCantidad(td.getCantidad());
				movDetalle.setCantidadSolicitada(td.getCantidad());
				movDetalle.setEstatus(0);
				movDetalle.setCreadoPor(td.getCreadoPor());
				movDetalle.setFechaCreacion(td.getFechaCreacion());
				movDetalle.setModificadoPor(td.getModificadoPor());
				movDetalle.setFechaModificacion(td.getFechaModificacion());
				
				if (movDetalles == null)
					movDetalles = new ArrayList<MovimientosDetalle>();
				movDetalles.add(movDetalle);
			}

			// Guardo entrada (movimiento) y detalles
			log.info("Guardando Movimiento y detalles ...");
			movimiento.setId(this.ifzMovimientos.save(movimiento));
			for (MovimientosDetalle item : movDetalles)
				item.setIdAlmacenMovimiento(movimiento.getId());
			movDetalles = this.ifzMovimientosDetalle.saveOrUpdateList(movDetalles);
			
			// Actualizamos los estatus del traspaso si corresponde
			if (TipoMovimientoInventario.ENTRADA.equals(tipoMovimiento))
				actualizarDetallesTraspaso(this.pojoTraspaso.getId(), movDetalles);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar autorecibir el Traspaso", e);
			return false;
		} 

		try {
			// backoffice
			backOfficeInventario(movimiento.getId(), movDetalles);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar lanzar evento BackOffice de Inventario: " + TopicInventariosEventos.BackOfficeInventario.toString(), e);
		}
		
		return true;
	}
	
	private boolean subprocesoDevolucion() {
		AlmacenProducto almacenSalida = null;
		Almacen almacenDestinoOriginal = null;
		Almacen bodega = null;
		double cantidadProducto = 0;
		
		try {
			control();
			// Traspaso de Almacen a Almacen: DEVOLUCION y TRASPASO
			bodega = determinarAlmacenPrincipal(this.pojoTraspaso.getAlmacenOrigen().getId());
			if (bodega == null) {
				control(-1, "No se pudo guardar el Movimiento indicado.\nEste Traspaso requiere internamente una Devolucion la cual no pudo realizarse.");
				return false;
			}

			// Asignamos parametros para DEVOLUCION
			this.pojoTraspaso.setAlmacenDestino(bodega);
			this.pojoTraspaso.setTipo(2);
			
			// Guardamos devolucion
			this.pojoTraspaso.setId(this.ifzTraspaso.save(this.pojoTraspaso));
			
			// Guardamos detalle
			for (TraspasoDetalleExt td : this.listaTraspasoDetalleGrid) {
				td.setIdAlmacenTraspaso(this.pojoTraspaso.getId());
				td.setEstatus(2);	//en transito
				td.setCreadoPor(this.usuarioId);
				td.setFechaCreacion(Calendar.getInstance().getTime());
				td.setModificadoPor(this.usuarioId);
				td.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guarda detalle
				td.setId(this.ifzTraspasoDetalle.save(td));
				
				cantidadProducto = this.getCantidadProductoExistencias(td.getIdProducto().getClave());
				log.info("Producto: " + td.getIdProducto().getClave() + ", Cantidad: " + td.getCantidad() + ", Cant. Almacen: " + cantidadProducto);
				
				//Identificar donde restar el producto
				almacenSalida = this.ifzAlmacenProducto.findAlmacenProducto(this.pojoTraspaso.getAlmacenOrigen().getId(), td.getIdProducto().getId());
				if (almacenSalida != null) {
					almacenSalida.setExistencia(almacenSalida.getExistencia() - td.getCantidad());
					//almacenEntrada.setExistencia( almacenEntrada.getExistencia() + td.getCantidad() );	//Se elimina de aqui el proceso de entrada a almacen
					
					this.ifzAlmacenProducto.update(almacenSalida);
					//this.ifzAlmacenProducto.update(almacenEntrada);					//Se elimina de aqui el proceso de entrada a almacen
				}
			}
			
			// Añadimos el elemento al inicio de la Lista
			this.listaTraspasoGrid.add(0, this.pojoTraspaso);
			
			// Reiniciamos para generar el TRASPASO
			this.pojoTraspaso.setId(0L);
			this.pojoTraspaso.setTipo(1);
			this.pojoTraspaso.setAlmacenOrigen(bodega);
			this.pojoTraspaso.setAlmacenDestino(almacenDestinoOriginal);
			for (TraspasoDetalleExt td : this.listaTraspasoDetalleGrid)
				td.setId(0L);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar registrar el Traspaso solicitado.\nFallo el proceso automatizado interno para enviar los Productos.", e);
			return false;
		}
		
		return true;
	}
	
	private void actualizarDetallesTraspaso(long idTraspaso, List<MovimientosDetalle> listaDetalle) {
		List<TraspasoDetalleExt> lista = null;
		
		try {
			lista = this.ifzTraspasoDetalle.findExtDetallesByIdTraspaso(idTraspaso);
			if (lista == null || lista.isEmpty())
				return;
			
			for (TraspasoDetalleExt var : lista) {
				try {
					var.setCantidadRecibida(var.getCantidadRecibida() + var.getCantidad());
					if (var.getCantidad() == var.getCantidadRecibida())
						var.setEstatus(2); // estatus ?  1 transito : 2 completado
					this.ifzTraspasoDetalle.update(var);
				} catch (Exception e) {
					log.error("Ocurrio un problema al intentar actualizar la cantidad del detalle " + var.getId() + " del Traspaso", e);
				}
			}
			
			// Actualizamos estatus del traspaso
			for (TraspasoDetalleExt var : lista) {
				if (var.getCantidad() != var.getCantidadRecibida())
					return;	// con alguno que este en false, ya no se actualiza el estatus del traspaso
			}
			
			try {
				log.info("Actualizando estatus traspaso ... ");
				this.pojoTraspaso.setCompleto(1);
				this.ifzTraspaso.update(this.pojoTraspaso);
			} catch (Exception e) {
				log.error("Ocurrio un problema al intentar actualizar el estatus del Traspaso", e);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar actualizar los detalles del Traspaso", e);
		}
	}

	private void backOfficeInventario(Long idMovimiento, List<MovimientosDetalle> listDetalles) throws JMSException, NamingException {
		Object tmp = null;
		InitialContext iniCtx = null;
		QueueConnectionFactory qcf = null;
		Connection conn = null;
		Session session = null;
		TextMessage tm = null;
		MessageProducer sendtopic = null;
		// -----------------------------------------
		Topic topicInventario = null;
		MensajeTopic msgInventario = null;
		// -----------------------------------------
		Gson gson = null;
		String comando = "";
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			gson = new Gson();
			target = idMovimiento.toString();
			atributos = gson.toJson(listDetalles);
			
			// Generamos mensaje para BACKORDER	COMPRAS
			msgInventario = new MensajeTopic(TopicInventariosEventos.BackOfficeInventario, target, referencia, atributos);
			comando = gson.toJson(msgInventario);
			
			// Preparamos conector para el TOPIC
			iniCtx = new InitialContext();
			tmp = iniCtx.lookup("ConnectionFactory");
			qcf = (QueueConnectionFactory) tmp;
			conn = qcf.createQueueConnection();

			// Instanciamos conectamos con TOPIC
			topicInventario = (Topic) iniCtx.lookup("topic/INVENTARIOS");
			session = conn.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			conn.start();
			
			// Creamos Producer y enviamos mensaje
			sendtopic = session.createProducer(topicInventario);
			tm = session.createTextMessage(comando);
			sendtopic.send(tm);
			
			// Cerramos conexiones
			conn.stop();
			session.close();
			conn.close();
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar enviar mensaje al BACKORDER COMPRAS", e);
		}
	}
	
	private boolean isProductoEnLista() {
		for (TraspasoDetalleExt p : this.listaTraspasoDetalleGrid) {
			if (this.pojoProductoBusqueda.getProducto().getClave().equals(p.getIdProducto().getClave()))
				return true;
		}
		
		return false;
	}
	
	private boolean validaAgregarProducto() {
		control();
		if (isProductoEnLista()) {
			control(-11, "El producto ya se encuentra en la lista");
			return false;
		}
		
		return true;
	}
	
	private boolean validaGuardarTraspaso() {
		double cantidadProducto = 0;
		
		if (this.pojoTraspaso.getAlmacenOrigen() == null || this.pojoTraspaso.getAlmacenOrigen().getId()== null || this.pojoTraspaso.getAlmacenOrigen().getId() == 0) {
			control(-10, "Seleccione Almacen de Origen/Salida");
			return false;
		}

		if (this.pojoTraspaso.getAlmacenDestino() == null || this.pojoTraspaso.getAlmacenDestino().getId() == null || this.pojoTraspaso.getAlmacenDestino().getId() == 0) {
			control(-12, "Seleccione Almacen Destino");
			return false;
		}
		
		/* Validacion obsoleta. Los almacenes se filtrar dependiendo del tipo de traspaso y/o almacen origen
		if (this.pojoTraspaso.getAlmacenOrigen().getId().compareTo(this.pojoTraspaso.getAlmacenDestino().getId()) == 0) {
			control(-13, "El almacen de salida no puede ser el mismo de Destino");
			return false;
		}*/
		
		if (this.pojoTraspaso.getEntrega() == null || this.pojoTraspaso.getEntrega().getId() == 0) {
			control(-14, "Indique el empleado que entrega");
			return false;
		}
		
		if (this.pojoTraspaso.getRecibe() == null) {
			control(-15, "Indique el empleado que recibe");
			return false;
		}
		
		/* Validacion obsoleta, existe proceso para cuando Entrega y Recibe son el mismo
		if (this.pojoTraspaso.getRecibe().getId().compareTo(this.pojoTraspaso.getEntrega().getId()) == 0) {
			control(-16, "El empleado que entrega no puede ser el mismo que recibe");
			return false;
		}*/
		
		if (this.listaTraspasoDetalleGrid.isEmpty()) {
			control(-17, "Debe existir por lo menos un producto en la lista");
			return false;
		}
		
		// Comprobamos si algun elmento es igual o menor a cero o si excede el inventario
		for (TraspasoDetalleExt d : this.listaTraspasoDetalleGrid) {
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
		
		return true;
	}
	
	private void setCantidadExistente(String claveProducto, double cantidadExistente) {
		for (TraspasoDetalleExt td : this.listaDetalleCantidades) {
			if (td.getIdProducto().getClave().equals(claveProducto)) {
				log.info("Encontrado: " + claveProducto + ", Cant. " + cantidadExistente);
				td.setCantidad(cantidadExistente);
				break;
			}
		}
	}
	
	private double getCantidadProductoExistencias(String claveProducto) {
		for (TraspasoDetalleExt td : this.listaDetalleCantidades) {
			if (td.getIdProducto().getClave().equals(claveProducto)) {
				return td.getCantidad();
			}
		}
		
		return 0;
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
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.operacionCompleta = false;
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		if (this.operacionCancelada)
			log.error("\n\nTRASPASOS :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n", throwable);
	}

	// ------------------------------------------------------------------------------------
	// ALMACENES
	// ------------------------------------------------------------------------------------
	
	private void cargarAlmacenes() {
		List<Almacen> listaAlmacenesFull = null;
		
		if (this.listAlmacenes == null)
			this.listAlmacenes = new ArrayList<Almacen>();
		this.listAlmacenes.clear();
		
		listaAlmacenesFull = this.ifzAlmacen.findAllActivos();
		if (listaAlmacenesFull != null && ! listaAlmacenesFull.isEmpty()) {
			for (Almacen var : listaAlmacenesFull) {
				if (this.PERFIL_ADMINISTRATIVO && var.getTipo() > 2) {
					this.listAlmacenes.add(var);
				} else if (! this.almacenesAdministrativos && var.getTipo() <= 2) {
					this.listAlmacenes.add(var);
				}
			}
			
			Collections.sort(this.listAlmacenes, new Comparator<Almacen>() {
				@Override
				public int compare(Almacen o1, Almacen o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
		}
		
		filtrarAlmacenesOrigen();
	}
	
	public void filtrarAlmacenesOrigen() {
		boolean filtrarAlmacenDestino = false;
		
		if (this.listAlmacenesOrigen == null)
			this.listAlmacenesOrigen = new ArrayList<SelectItem>();
		this.listAlmacenesOrigen.clear();
		
		if (this.listAlmacenesDestino == null)
			this.listAlmacenesDestino = new ArrayList<SelectItem>();
		this.listAlmacenesDestino.clear();
		
		this.pojoTraspaso.setAlmacenOrigen(null);
		this.pojoTraspaso.setAlmacenDestino(null);
		this.pojoTraspaso.setEntrega(null);
		this.pojoTraspaso.setRecibe(null);

		for (Almacen var : this.listAlmacenes) {
			if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString())) { 
				if (var.getTipo() == 1 || var.getTipo() == 3) {
					this.listAlmacenesOrigen.add(new SelectItem(var.getId(), var.getNombre()));

					// Asignamos sugerencia de Almacen si corresponde			
					if (this.pojoTraspaso == null || (this.listEmpleadoAlmacenes == null || this.listEmpleadoAlmacenes.isEmpty()))
						continue;
					for (Almacen a : this.listEmpleadoAlmacenes) {
						if (a.getTipo() == 2 || a.getTipo() == 4)
							continue;
						if (var.getId().longValue() == a.getId().longValue()) {
							this.pojoTraspaso.setAlmacenOrigen(var);
							filtrarAlmacenDestino = true;
							break;
						}
					}
				}
			} else if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString())) {
				if (var.getTipo() == 1 || var.getTipo() == 3) {
					this.listAlmacenesOrigen.add(new SelectItem(var.getId(), var.getNombre()));

					// Asignamos sugerencia de Almacen si corresponde			
					if (this.pojoTraspaso == null || (this.listEmpleadoAlmacenes == null || this.listEmpleadoAlmacenes.isEmpty()))
						continue;
					for (Almacen a : this.listEmpleadoAlmacenes) {
						if (a.getTipo() == 2 || a.getTipo() == 4)
							continue;
						if (var.getId().longValue() == a.getId().longValue()) {
							this.pojoTraspaso.setAlmacenOrigen(var);
							filtrarAlmacenDestino = true;
							break;
						}
					}
				}
			} else if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_BODEGA_BODEGA.toString())) { 
				if (var.getTipo() == 2 || var.getTipo() == 4) {
					this.listAlmacenesOrigen.add(new SelectItem(var.getId(), var.getNombre()));

					// Asignamos sugerencia de Almacen si corresponde			
					if (this.pojoTraspaso == null || (this.listEmpleadoAlmacenes == null || this.listEmpleadoAlmacenes.isEmpty()))
						continue;
					for (Almacen a : this.listEmpleadoAlmacenes) {
						if (a.getTipo() == 1 || a.getTipo() == 3)
							continue;
						if (var.getId().longValue() == a.getId().longValue()) {
							this.pojoTraspaso.setAlmacenOrigen(var);
							filtrarAlmacenDestino = true;
							break;
						}
					}
				}
			} else if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString())) { 
				if (var.getTipo() == 2 || var.getTipo() == 4) {
					this.listAlmacenesOrigen.add(new SelectItem(var.getId(), var.getNombre()));

					// Asignamos sugerencia de Almacen si corresponde			
					if (this.pojoTraspaso == null || (this.listEmpleadoAlmacenes == null || this.listEmpleadoAlmacenes.isEmpty()))
						continue;
					for (Almacen a : this.listEmpleadoAlmacenes) {
						if (a.getTipo() == 1 || a.getTipo() == 3)
							continue;
						if (var.getId().longValue() == a.getId().longValue()) {
							this.pojoTraspaso.setAlmacenOrigen(var);
							filtrarAlmacenDestino = true;
							break;
						}
					}
				}
			} else { // Todos
				this.listAlmacenesOrigen.add(new SelectItem(var.getId(), var.getNombre()));

				// Asignamos sugerencia de Almacen si corresponde			
				if (this.pojoTraspaso == null || (this.listEmpleadoAlmacenes == null || this.listEmpleadoAlmacenes.isEmpty()))
					continue;
				for (Almacen a : this.listEmpleadoAlmacenes) {
					if (var.getId().longValue() == a.getId().longValue()) {
						this.pojoTraspaso.setAlmacenOrigen(var);
						filtrarAlmacenDestino = true;
						break;
					}
				}
			}
		}
		
		if (filtrarAlmacenDestino)
			filtrarAlmacenesDestino();
	}
	
	public void filtrarAlmacenesDestino() {
		if (this.listAlmacenesDestino == null)
			this.listAlmacenesDestino = new ArrayList<SelectItem>();
		this.listAlmacenesDestino.clear();

		for (Almacen var : this.listAlmacenes) {
			if (this.pojoTraspaso.getAlmacenOrigen() != null && this.pojoTraspaso.getAlmacenOrigen().getId() != null && this.pojoTraspaso.getAlmacenOrigen().getId() > 0L) {
				if (this.almacenBodega != 3 && this.pojoTraspaso.getAlmacenOrigen().getId().longValue() == var.getId().longValue())
					continue;
			}
			
			if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString())) { // Bodega a Almacen
				if (var.getTipo() == 1 || var.getTipo() == 3)
					this.listAlmacenesDestino.add(new SelectItem(var.getId(), var.getNombre()));
			} else if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString())) { // Devolucion
				if (var.getTipo() == 2 || var.getTipo() == 4)
					this.listAlmacenesDestino.add(new SelectItem(var.getId(), var.getNombre()));
			} else if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.TRASPASO_BODEGA_BODEGA.toString())) { // Devolucion
				if (var.getTipo() == 2 || var.getTipo() == 3)
					this.listAlmacenesDestino.add(new SelectItem(var.getId(), var.getNombre()));
			} else if (this.tipoTraspaso.equals(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString())) { // Almacen a Almacen
				if (var.getTipo() == 1 || var.getTipo() == 3)
					this.listAlmacenesDestino.add(new SelectItem(var.getId(), var.getNombre()));
			} else { // Todos
				this.listAlmacenesDestino.add(new SelectItem(var.getId(), var.getNombre()));
			}
		}
	}

	// ------------------------------------------------------------------------------------
	// EMPLEADOS
	// ------------------------------------------------------------------------------------
	
	private void cargarEmpleados() {
		if (this.listEmpleados == null)
			this.listEmpleados = new ArrayList<Empleado>();
		this.listEmpleados.clear();
		
		this.listEmpleados = this.ifzEmpleado.findAllActivos();
		if (this.listEmpleados != null && ! this.listEmpleados.isEmpty()) {
			Collections.sort(this.listEmpleados, new Comparator<Empleado>() {
				@Override
				public int compare(Empleado o1, Empleado o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
		}
		
		cargarCboEmpleadoEntrega();
		cargarCboEmpleadoRecibe();
	}

	public void cargarCboEmpleadoEntrega() {
		//boolean filtrarEmpleadoRecibe = false;
		
		if (this.listEmpleadosEntrega == null)
			this.listEmpleadosEntrega = new ArrayList<SelectItem>();
		this.listEmpleadosEntrega.clear();
		
		/*if (this.listEmpleadosRecibe == null)
			this.listEmpleadosRecibe = new ArrayList<SelectItem>();
		this.listEmpleadosRecibe.clear();
		
		this.pojoTraspaso.setEntrega(null);
		this.pojoTraspaso.setRecibe(null);*/
		
		for (Empleado var : this.listEmpleados) {
			this.listEmpleadosEntrega.add(new SelectItem(var.getId(), var.getNombre()));
			
			/*if (this.pojoEmpleadoUsuario != null && this.pojoEmpleadoUsuario.getId() != null && var.getId().longValue() == this.pojoEmpleadoUsuario.getId().longValue()) {
				this.pojoTraspaso.setEntrega(var);
				filtrarEmpleadoRecibe = true;
			}*/
		}
		
		/*if (filtrarEmpleadoRecibe)
			cargarCboEmpleadoRecibe();*/
	}
	
	public void cargarCboEmpleadoRecibe() {
		if (this.listEmpleadosRecibe == null)
			this.listEmpleadosRecibe = new ArrayList<SelectItem>();
		this.listEmpleadosRecibe.clear();
		
		for (Empleado var : this.listEmpleados) {
			/*if (this.pojoTraspaso.getEntrega() != null && this.pojoTraspaso.getEntrega().getId() != null && this.pojoTraspaso.getEntrega().getId() > 0L) {
				if (this.pojoTraspaso.getEntrega().getId().longValue() == var.getId().longValue())
					continue;
			}*/

			this.listEmpleadosRecibe.add(new SelectItem(var.getId(), var.getNombre()));
		}
	}
	
	public void seleccionaEncargado() {
		if (this.pojoTraspaso.getAlmacenDestino() == null || this.pojoTraspaso.getAlmacenDestino().getId() == null || this.pojoTraspaso.getAlmacenDestino().getId() <= 0L)
			return;
		
		for (Empleado e : this.listEmpleados) {
			if (this.pojoTraspaso.getAlmacenDestino().getIdEncargado() == null || this.pojoTraspaso.getAlmacenDestino().getIdEncargado() <= 0L)
				continue;
			
			if (e.getId().longValue() == this.pojoTraspaso.getAlmacenDestino().getIdEncargado().longValue()) {
				this.pojoTraspaso.setRecibe(e);
				break;
			}
		}
	}
	
	private void autoSeleccionaEntrega() {
		if (this.pojoTraspaso == null)
			return;
		if (this.pojoTraspaso.getAlmacenOrigen() == null || this.pojoTraspaso.getAlmacenOrigen().getId() == null || this.pojoTraspaso.getAlmacenOrigen().getId() <= 0L)
			return;
		if (this.pojoTraspaso.getAlmacenOrigen().getIdEncargado() == null || this.pojoTraspaso.getAlmacenOrigen().getIdEncargado() <= 0L)
			return;
		
		for (Empleado var : this.listEmpleados) {
			if (this.pojoTraspaso.getAlmacenOrigen().getIdEncargado().longValue() == var.getId().longValue()) {
				this.pojoTraspaso.setEntrega(var);
				break;
			}
		}
	}
	
	private void autoSeleccionaRecibe() {
		if (this.pojoTraspaso == null)
			return;
		if (this.pojoTraspaso.getAlmacenDestino() == null || this.pojoTraspaso.getAlmacenDestino().getId() == null || this.pojoTraspaso.getAlmacenDestino().getId() <= 0L)
			return;
		if (this.pojoTraspaso.getAlmacenDestino().getIdEncargado() == null || this.pojoTraspaso.getAlmacenDestino().getIdEncargado() <= 0L)
			return;
		
		for (Empleado var : this.listEmpleados) {
			if (this.pojoTraspaso.getAlmacenDestino().getIdEncargado().longValue() == var.getId().longValue()) {
				this.pojoTraspaso.setRecibe(var);
				break;
			}
		}
	}
	
	//  -----------------------------------------------------------------------------------------
	// BUSQUEDA PRODUCTOS
	//  -----------------------------------------------------------------------------------------

	public void validaCabeceraTraspaso() {
		control();
		if (this.pojoTraspaso.getAlmacenOrigen() == null || this.pojoTraspaso.getAlmacenOrigen().getId() == null || this.pojoTraspaso.getAlmacenOrigen().getId() <= 0L) {
			control(-10, "Debe seleccionar almacén de Salida");
			return;
		}

		nuevaBusquedaProductos();
	}

	private void nuevaBusquedaProductos() {
		control();
		this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
		this.valorBusquedaProductos = "";
		this.numPaginaProductos = 1;
		
		this.idFamilia = 0;
		this.avanzadaBusquedaProductos = false;
		this.admvaBusquedaProductos = false;
		
		if (this.listaBusquedaProductos == null)
			this.listaBusquedaProductos = new ArrayList<AlmacenProductoExt>(); 
		this.listaBusquedaProductos.clear();
		this.pojoProductoBusqueda = new AlmacenProductoExt();
		this.operacionCompleta = true;
	}
	
	public void buscarProductos() {
		int tipoMaestro = 0;
		
		try {
			control();
			if (this.listaBusquedaProductos == null)
				this.listaBusquedaProductos = new ArrayList<AlmacenProductoExt>(); 
			this.listaBusquedaProductos.clear();
			
			if (this.campoBusquedaProductos == null || "".equals(this.campoBusquedaProductos.trim()))
				this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
			
			tipoMaestro = 1; // MAESTRO de Insumos
			if (this.pojoTraspaso.getAlmacenOrigen().getTipo() > 2)
				tipoMaestro = 2; // MAESTRO Administrativo
			
			this.listaBusquedaProductos = this.ifzAlmacenProducto.findExtExistentes(this.pojoTraspaso.getAlmacenOrigen().getId(), this.campoBusquedaProductos, this.valorBusquedaProductos, this.idFamilia, tipoMaestro, 0, false);
			if (this.listaBusquedaProductos == null || this.listaBusquedaProductos.isEmpty()) {
				control(2, "La busqueda no devolvio resultados");
				return;
			}
			
			// Ordenamos por descripcion
			Collections.sort(this.listaBusquedaProductos, new Comparator<AlmacenProductoExt>() {
				@Override
				public int compare(AlmacenProductoExt o1, AlmacenProductoExt o2) {
					return o1.getProducto().getDescripcion().compareTo(o2.getProducto().getDescripcion());
				}
			});
			
			this.operacionCompleta = true;
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Productos del Almacen seleccioando", e);
		}
	}

	public void seleccionarProducto() {
		TraspasoDetalleExt td = null;
		ProductoExt p = null;
		
		control();
		if (this.pojoProductoBusqueda.getId() != null) {
			if (! validaAgregarProducto()) 
				return;
			
			td = new TraspasoDetalleExt();
			p = this.ifzProductos.convertir(this.pojoProductoBusqueda.getProducto()); //.findExtById(this.pojoProductoBusqueda.getId()); 
			p.setExistencia(this.pojoProductoBusqueda.getExistencia());
			td.setCantidad(1);
			td.setIdProducto(p);
			this.listaTraspasoDetalleGrid.add(td); 
				
			td = new TraspasoDetalleExt();	//Se crea otro objeto para evitar problemas de clonacion en Java
			p.setExistencia(this.pojoProductoBusqueda.getExistencia());
			td.setCantidad(1);
			td.setIdProducto(p);
			this.listaDetalleCantidades.add(td);	//la lista de respaldo. En el campo cantidad, se guardará la cantidad que existe de producto
			
			this.pojoProductoBusqueda = new AlmacenProductoExt();
			this.cantidadProductoDetalles = this.listaTraspasoDetalleGrid.size();
			if (this.almacenAnterior == 0) {	//Asignar por primera vez el almacen
				this.setAlmacenAnterior(this.pojoTraspaso.getAlmacenOrigen().getId());
				log.info("Asignar almacen anterior: " + this.almacenAnterior);
			}
			
			this.listaBusquedaProductos.remove(this.pojoProductoBusqueda);
		}
		
		this.operacionCompleta = true;
		control(false, 4, "Producto Añadido", null);
	}

	public void toggleAvanzadaBusquedaProductos() {
		this.avanzadaBusquedaProductos = ! this.avanzadaBusquedaProductos;
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
	
	//  -----------------------------------------------------------------------------------------
	// PROPIEDADES
	//  -----------------------------------------------------------------------------------------

	public long getIdEmpleadoEntrega() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getEntrega() != null && this.pojoTraspaso.getEntrega().getId() != null && this.pojoTraspaso.getEntrega().getId() > 0L) 
			return this.pojoTraspaso.getEntrega().getId();
		return 0L;
	}

	public void setIdEmpleadoEntrega(long idEmpleado) {
		if (idEmpleado <= 0) 
			return;
		
		for (Empleado e : this.listEmpleados) {
			if (e.getId().longValue() == idEmpleado) {
				this.pojoTraspaso.setEntrega(e);
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
		
		for (Empleado e : this.listEmpleados) {
			if (e.getId().longValue() == idEmpleado) {
				this.pojoTraspaso.setRecibe(e);
				break;
			}
		}
	}
	
	public long getIdAlmacenSalida() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getAlmacenOrigen() != null && this.pojoTraspaso.getAlmacenOrigen().getId() != null)
			return this.pojoTraspaso.getAlmacenOrigen().getId();
		return 0L;
	}
	
	public void setIdAlmacenSalida(long idAlmacen) {
		if (idAlmacen <= 0)
			return;
		
		for (Almacen a : this.listAlmacenes) {
			if (a.getId().longValue() == idAlmacen) {
				this.pojoTraspaso.setAlmacenOrigen(a);
				autoSeleccionaEntrega();
				break;
			}
		}
	}
	
	public long getIdAlmacenDestino() {
		if (this.pojoTraspaso != null && pojoTraspaso.getAlmacenDestino() != null && this.pojoTraspaso.getAlmacenDestino().getId() != null)
			return this.pojoTraspaso.getAlmacenDestino().getId();
		return 0L;
	}
	
	public void setIdAlmacenDestino(long idAlmacen) {
		if (idAlmacen <= 0)
			return;

		for (Almacen a : this.listAlmacenes) {
			if (a.getId() == idAlmacen) {
				this.pojoTraspaso.setAlmacenDestino(a);
				autoSeleccionaRecibe();
				break;
			}
		}
	}
	
	public Date getFecha() {
		return this.pojoTraspaso.getFecha() == null ? Calendar.getInstance().getTime() : this.pojoTraspaso.getFecha(); 
	}
	
	public void setFecha(Date fecha) {
		this.pojoTraspaso.setFecha(fecha);
	}

	public AlmacenTraspasoExt getPojoTraspaso() {
		return pojoTraspaso;
	}

	public void setPojoTraspaso(AlmacenTraspasoExt pojoTraspaso) {
		this.pojoTraspaso = pojoTraspaso;
	}
	
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

	public List<SelectItem> getListaCboAlmacenSalida() {
		return listAlmacenesOrigen;
	}

	public void setListaCboAlmacenSalida(List<SelectItem> listaCboAlmacenSalida) {
		this.listAlmacenesOrigen = listaCboAlmacenSalida;
	}

	public List<SelectItem> getListaCboAlmacenDestino() {
		return listAlmacenesDestino;
	}

	public void setListaCboAlmacenDestino(List<SelectItem> listaCboAlmacenDestino) {
		this.listAlmacenesDestino = listaCboAlmacenDestino;
	}

	public List<Empleado> getListaEmpleados() {
		return listEmpleados;
	}

	public void setListaEmpleados(List<Empleado> listaEmpleados) {
		this.listEmpleados = listaEmpleados;
	}

	public List<SelectItem> getListaCboEmpleadoEntrega() {
		return listEmpleadosEntrega;
	}

	public void setListaCboEmpleadoEntrega(List<SelectItem> listaCboEmpleadoEntrega) {
		this.listEmpleadosEntrega = listaCboEmpleadoEntrega;
	}

	public List<SelectItem> getListaCboEmpleadoRecibe() {
		return listEmpleadosRecibe;
	}

	public void setListaCboEmpleadoRecibe(List<SelectItem> listaCboEmpleadoRecibe) {
		this.listEmpleadosRecibe = listaCboEmpleadoRecibe;
	}

	/*private void cargarDetalleTraspaso(long idAlmacenMovimiento) {
		this.listaTraspasoDetalleGrid = this.ifzTraspasoDetalle.findDetallesExtById(idAlmacenMovimiento);
	}*/

	public List<AlmacenTraspasoExt> getListaTraspasoGrid() {
		return listaTraspasoGrid;
	}

	public void setListaTraspasoGrid(List<AlmacenTraspasoExt> listaTraspasoGrid) {
		this.listaTraspasoGrid = listaTraspasoGrid;
	}

	public List<TraspasoDetalleExt> getListaTraspasoDetalleGrid() {
		return listaTraspasoDetalleGrid;
	}

	public void setListaTraspasoDetalleGrid(List<TraspasoDetalleExt> listaTraspasoDetalleGrid) {
		this.listaTraspasoDetalleGrid = listaTraspasoDetalleGrid;
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
}

// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA    |        AUTOR       | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//    2.0  | 2016-10-19  | Javier Tirado      | Modifico metodo cargarCboAlmacenDestino. Filtro solo alamcenes principales
