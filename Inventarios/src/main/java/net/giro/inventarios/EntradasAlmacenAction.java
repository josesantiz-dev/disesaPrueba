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
import java.util.Map.Entry;
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
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.beans.TipoObraAutorizadas;
import net.giro.adp.beans.TipoObraJerarquia;
import net.giro.adp.beans.TipoObraRevisadas;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.adp.logica.ObraRem;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.comun.TipoMaestro;
import net.giro.inventarios.comun.TipoMovimientoInventario;
import net.giro.inventarios.comun.TipoMovimientoReferenciaExtendida;
import net.giro.inventarios.comun.TipoTraspaso;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;
import net.giro.navegador.comun.Permiso;
import net.giro.navegador.comun.PermisoExt;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="entradasAction")
public class EntradasAlmacenAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(EntradasAlmacenAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private Permiso permisosBase;
	private PermisoExt permisos;
	private HttpSession httpSession;
	private AlmacenMovimientosRem ifzMovimientos;
	private MovimientosDetalleRem ifzMovimientosDetalle;
	private ObraAlmacenesRem ifzObrasAlmacenes;
	private ReportesRem ifzReportes;
	private List<AlmacenMovimientos> listMovimientos;
	private AlmacenMovimientosExt pojoMovimiento;
	private List<MovimientosDetalleExt> listDetalles;
	private long idMovimiento;
	private OrdenCompra movimientoOrdenCompra;
	private boolean sePuedeGuardarMovimiento;
	// empleados
	private EmpleadoRem ifzEmpleado;
	private List<Empleado> listEmpleados;
	private List<SelectItem> listEmpleadosItems;
	private boolean PERFIL_ADMINISTRATIVO;
	// busqueda principal
	private List<SelectItem> listaCampoBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private Date fechaBusqueda;
	private int numPagina;
	private int numPaginaDetalles;
	// Almacenes
	private AlmacenRem ifzAlmacen;
	private List<Almacen> listAlmacenes;
	private List<SelectItem> listAlmacenesItems;
	private boolean almacenesAdministrativos;
	// Busqueda obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorOpcionBusquedaObras;
	private int numPaginaObras;
	// Busqueda Orden Compra
	private OrdenCompraRem ifzOrdenCompra;
	private OrdenCompraDetalleRem ifzOrdenCompraDetalle;
	private OrdenCompra pojoOrdenCompra;
	private List<OrdenCompra> listaOrdenesCompra;
	private List<SelectItem> tiposBusquedaOC;
	private String campoBusquedaOC;
	private String valorBusquedaOC;
	private int numPaginaOrdenCompras;
	//Busqueda Productos
	private ProductoRem ifzProductos;
	private List<Producto> listaBusquedaProductos; 
	private Producto pojoProductoBusqueda; 
	private List<SelectItem> tiposBusquedaProductos;	
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private int numPaginaProductos;
	// Familia
	private ConGrupoValoresRem ifzGpoValores;
	private ConValoresRem ifzConValores;
	private ConGrupoValores pojoGpoMaestro;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	private long idFamilia;
	// Buscando traspasos
	private AlmacenTraspasoRem ifzTraspasos;
	private TraspasoDetalleRem ifzTraspasosDetalle;
	private List<SelectItem> tiposBusquedaTraspasos;
	private String campoBusquedaTraspasos;
	private String valorBusquedaTraspasos;
	private int numPaginaTraspasos;
	private AlmacenTraspasoExt pojoTraspaso;
	private List<AlmacenTraspasoExt> listaTraspasos;
	private List<TraspasoDetalleExt> listaTraspasosDetalle;
	private boolean puedeBuscarTraspaso;	
	private boolean puedeEditar;
	// EMPLEADO-USUARIO
	private EmpleadoExt pojoEmpleadoUsuario;
	private List<Long> listPuestosValidos;
	private List<Almacen> listAlmacenesTrabajo;
	private Almacen almacenTrabajo;
	private Empleado almacenEncargado;
	// Tipo de Entradas
	private List<SelectItem> listTiposEntradas;
	private String tipoEntrada;
	// PERFILES
	private boolean perfilAdministrativo;
	private boolean limiteUbicacionGeografica;
	// Control
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
		
	public EntradasAlmacenAction() {
		InitialContext ctx = null;
		Map<String, String> params = null;
		ValueExpression ve = null;
		FacesContext fc = null;
		Application app = null;
		String resValue = "";
		String[] splitted = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			params = fc.getExternalContext().getRequestParameterMap();
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());			
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			if (! this.isDebug)
				this.paramsRequest.clear();
			
			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilAdministrativo = ((resValue != null && "S".equals(resValue)) ? true : false);
			log.info("Perfil EGRESOS_OPERACION: " + (this.perfilAdministrativo ? "SI" : "NO"));

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
			
			this.limiteUbicacionGeografica = false;
			resValue = this.entornoProperties.getString("limiteUbicacionGeografica");
			if (resValue != null && ! "".equals(resValue.trim())) 
				this.limiteUbicacionGeografica = Boolean.parseBoolean(resValue);

			ctx = new InitialContext();
			this.ifzMovimientos = (AlmacenMovimientosRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzMovimientosDetalle = (MovimientosDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");
			this.ifzAlmacen = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObrasAlmacenes = (ObraAlmacenesRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzEmpleado = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzOrdenCompra = (OrdenCompraRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOrdenCompraDetalle = (OrdenCompraDetalleRem) ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzTraspasos = (AlmacenTraspasoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
			this.ifzTraspasosDetalle = (TraspasoDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzProductos = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzGpoValores = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");

			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMovimientosDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObrasAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenCompra.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenCompraDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspasos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspasosDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProductos.setInfoSesion(this.loginManager.getInfoSesion());
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

			// Permisos
			resValue = (this.entornoProperties.containsKey("ENTRADAS") ? this.entornoProperties.getString("ENTRADAS") : "ENTRADAS");
			resValue = (resValue != null && ! "".equals(resValue.trim())) ? resValue.trim() : "0";
			this.permisosBase = this.loginManager.getPermisos(this.loginManager.getIdAplicacion(), Long.parseLong(resValue));
			comprobarUsuario();
			
			// Busqueda principal
			this.listaCampoBusqueda = new ArrayList<SelectItem>();
			this.listaCampoBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.listaCampoBusqueda.add(new SelectItem("a.folioFactura", "Factura"));
			this.listaCampoBusqueda.add(new SelectItem("a.folioOrdenCompra", "Orden de Compra"));
			this.listaCampoBusqueda.add(new SelectItem("c.clave", "Codigo"));
			this.listaCampoBusqueda.add(new SelectItem("a.fecha", "Fecha"));
			this.listaCampoBusqueda.add(new SelectItem("a.recibeNombre", "Recibe"));
			this.listaCampoBusqueda.add(new SelectItem("a.idTraspaso", "Traspaso"));
			this.listaCampoBusqueda.add(new SelectItem("a.id", "ID"));
			this.campoBusqueda = this.listaCampoBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda ORDENES DE COMPRA
			this.tiposBusquedaOC = new ArrayList<SelectItem>();
			this.tiposBusquedaOC.add(new SelectItem("*","Coincidencia"));
			this.tiposBusquedaOC.add(new SelectItem("folio","Folio"));
			this.tiposBusquedaOC.add(new SelectItem("nombreObra","Obra"));
			this.tiposBusquedaOC.add(new SelectItem("nombreProveedor","Proveedor"));
			this.tiposBusquedaOC.add(new SelectItem("nombreSolicita","Solicita"));
			this.tiposBusquedaOC.add(new SelectItem("id", "ID"));
			nuevaBusquedaOrdenCompra();
			
			// Busqueda TRASPASOS
			this.tiposBusquedaTraspasos = new ArrayList<SelectItem>();
			this.tiposBusquedaTraspasos.add(new SelectItem("*","Coincidencia"));
			this.tiposBusquedaTraspasos.add(new SelectItem("a.fecha", "Fecha"));
			this.tiposBusquedaTraspasos.add(new SelectItem("c.clave", "Codigo"));
			this.tiposBusquedaTraspasos.add(new SelectItem("a.nombreObra", "Obra"));
			this.tiposBusquedaTraspasos.add(new SelectItem("a.id","ID"));
			nuevaBusquedaTraspaso();
			
			// Busqueda OBRAS
		/*	this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();*/
			this.listObras = new ArrayList<Obra>();
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
			
			// Busqueda PRODUCTOS
			this.tiposBusquedaProductos = new ArrayList<SelectItem>();
			this.tiposBusquedaProductos.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaProductos.add(new SelectItem("clave", "Codigo"));
			this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
			nuevaBusquedaProductos();

			cargarAlmacenes();
			cargarEmpleados();
			cargarFamilias();
			this.puedeEditar = true;
			this.listMovimientos = new ArrayList<AlmacenMovimientos>();
			this.pojoMovimiento = new AlmacenMovimientosExt();
			this.listDetalles = new ArrayList<MovimientosDetalleExt>();
			filtrarTipoEntrada();
			if (! this.listAlmacenesTrabajo.isEmpty())
				setIdAlmacenTrabajo(this.listAlmacenesTrabajo.get(0).getId());
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar el Bean", e);
		}
	}
	
	public void buscar() {
		SimpleDateFormat formatter = null;
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
				control(-1, "Debe seleccionar un Almacen/Bodega previamente");
				return;
			}
			
			this.campoBusqueda = (this.campoBusqueda != null && ! "".equals(this.campoBusqueda.trim())) ? this.campoBusqueda.trim() : this.listaCampoBusqueda.get(0).getValue().toString();
			this.valorBusqueda = (this.valorBusqueda != null && ! "".equals(this.valorBusqueda.trim())) ? this.valorBusqueda.trim() : "";
			value = this.valorBusqueda;
			if ("fecha".equals(this.campoBusqueda)) {
				formatter = new SimpleDateFormat("MM/dd/yyyy");
				value = formatter.format(this.fechaBusqueda);
			}

    		this.numPagina = 1;
			this.listMovimientos = this.ifzMovimientos.findLikeProperty(this.campoBusqueda, value, this.almacenTrabajo.getId(), TipoMovimientoInventario.ENTRADA.ordinal(), "", "model.fecha desc, model.id desc", 0); //(this.campoBusqueda, this.valorBusqueda, TipoMovimientoInventario.ENTRADA.ordinal(), "", 0);
			this.listMovimientos = this.listMovimientos != null ? this.listMovimientos : new ArrayList<AlmacenMovimientos>();
			if (this.listMovimientos == null || this.listMovimientos.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
    		control("Ocurrio un problema al consultar las Entradas", e);
    	} 
	}

	public void nuevaEntrada() {
		Map<String,String> params = null;
		
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
			
			this.puedeEditar = true;
			this.pojoMovimiento = new AlmacenMovimientosExt();
			this.pojoMovimiento.setTipo(TipoMovimientoInventario.ENTRADA.ordinal());
			this.pojoMovimiento.setAlmacen(this.almacenTrabajo);
			this.pojoMovimiento.setRecibe(this.almacenEncargado);
			if (this.almacenEncargado == null && this.pojoEmpleadoUsuario != null)
				this.pojoMovimiento.setRecibe(this.ifzEmpleado.findById(this.pojoEmpleadoUsuario.getId()));
			this.pojoMovimiento.setIdOrdenCompra(0L);
			this.pojoMovimiento.setIdTraspaso(0L);
			this.pojoMovimiento.setIdSalida(null);
			this.pojoMovimiento.setIdObra(0L);
			this.pojoMovimiento.setEntrega(null);
			this.listDetalles = new ArrayList<MovimientosDetalleExt>();

			params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			if (params.containsKey("origen"))
				this.tipoEntrada = params.get("origen");
			if (this.tipoEntrada == null || "".equals(this.tipoEntrada.trim()))
				this.tipoEntrada = this.listTiposEntradas.get(0).getValue().toString();
			this.movimientoOrdenCompra = null;
			this.pojoTraspaso = null;
		} catch (Exception e) {
			control("Ocurrio un problema al preparar la nueva Entrada a Almacen/Bodega", e);
		}
	}

	public void editar() {
		try {
			control();
			if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
				control(-1, "Debe seleccionar un Almacen/Bodega previamente");
				return;
			}
			
			this.puedeEditar = false;
			this.movimientoOrdenCompra = null;
			this.pojoTraspaso = null;
			this.pojoMovimiento = this.ifzMovimientos.findByIdExt(this.idMovimiento);
			if (this.pojoMovimiento == null || this.pojoMovimiento.getId() == null || this.pojoMovimiento.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar la Entrada seleccionada");
				return;
			}
			
			// Extendemos el movimiento
			this.tipoEntrada = this.pojoMovimiento.getTipoEntrada();
			// Obtenemos Orden de Compra si corresponde: OC
			if (TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_ALMACEN.toString().equals(this.tipoEntrada)) {
				this.movimientoOrdenCompra = this.ifzOrdenCompra.findById(this.pojoMovimiento.getIdOrdenCompra());
				this.almacenesAdministrativos = (this.movimientoOrdenCompra.getTipo() == 2);
			}
			
			// Obtenemos Traspaso si corresponde: TR | TX | DE
			if (TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString().equals(this.tipoEntrada) || TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString().equals(this.tipoEntrada) || TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString().equals(this.tipoEntrada)) {
				this.pojoTraspaso = this.ifzTraspasos.findByIdExt(this.pojoMovimiento.getIdTraspaso());
				this.almacenesAdministrativos = (this.pojoTraspaso.getAlmacenOrigen().getTipo() == 3 || this.pojoTraspaso.getAlmacenOrigen().getTipo() == 4);
			}
			
			// Filtramos Almacenes y/o Bodegas
			cargarAlmacenes();
			
			// Cargamos detalles
			this.listDetalles = this.ifzMovimientosDetalle.findAllExt(this.pojoMovimiento.getId());
			this.listDetalles = (this.listDetalles != null) ? this.listDetalles : new ArrayList<MovimientosDetalleExt>();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los Productos de la Entrada seleccionada", e);
		}
	}

	public void guardar() {
		List<MovimientosDetalleExt> detalles = null;
		Long idMovimiento = 0L;
		String tipoEntrada = "";
		
		try {
			// Validamos
			// ----------------------------------------------------------------------------------------------------------------------
			control();
			log.info("Validando ... ");
			if (! validaciones()) 
				return;
			
			if (this.pojoMovimiento.getId() != null && this.pojoMovimiento.getId() > 0L) {
				this.pojoMovimiento.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoMovimiento.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzMovimientos.update(this.pojoMovimiento);
				control(-1, "Entrada actualizada");
				return;
			}

			// Validamos tipo Entrada
			tipoEntrada = this.tipoEntrada;
			if ("OC CB".contains(this.tipoEntrada))
				tipoEntrada = "OC";
			if ("TR TX".contains(this.tipoEntrada))
				tipoEntrada = "TR";
			if ("DE DX DB".contains(this.tipoEntrada))
				tipoEntrada = "DE";
			
			// Generamos Movimiento y Detalles
			// ----------------------------------------------------------------------------------------------------------------------
			log.info("Guardando Movimiento y Detalles ... ");
			this.pojoMovimiento.setTipo(TipoMovimientoInventario.ENTRADA.ordinal());
			this.pojoMovimiento.setTipoEntrada(tipoEntrada);
			this.pojoMovimiento.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoMovimiento.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoMovimiento.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoMovimiento.setFechaModificacion(Calendar.getInstance().getTime());
			
			detalles = new ArrayList<MovimientosDetalleExt>();
			for (MovimientosDetalleExt md : this.listDetalles) {
				if (md.getCantidad() <= 0)
					continue;
				detalles.add(md);
			}

			// Guardo el Movimiento y Detalles
			// ----------------------------------------------------------------------------------------------------------------------
			log.info("Guardando Movimiento y Detalles ... ");
			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			this.pojoMovimiento.setId(this.ifzMovimientos.save(this.pojoMovimiento));
			for (MovimientosDetalleExt detalle : detalles) 
				detalle.setIdAlmacenMovimiento(this.pojoMovimiento.getId());
			this.ifzMovimientosDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			detalles = this.ifzMovimientosDetalle.saveOrUpdateListExt(detalles);
			idMovimiento = this.pojoMovimiento.getId();
			
			// Añadimos a listado y Lanzamos BackOffice
			// ----------------------------------------------------------------------------------------------------------------------
			this.listMovimientos.add(0, this.ifzMovimientos.convertir(this.pojoMovimiento));
			// Lanzamos Reporte
			// ----------------------------------------------------------------------------------------------------------------------
			reporte(this.pojoMovimiento.getId());
			// Finalizamos
			// ----------------------------------------------------------------------------------------------------------------------
			nuevaEntrada();
			
			// Ahora, afectamos existencias por JMS
			// ----------------------------------------------------------------------------------------------------------------------
			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			if (! this.ifzMovimientos.backOfficeInventario(idMovimiento, tipoEntrada))
				control(-1, "Se guardo la Entrada, sin embargo, la carga de material no se pudo realizar.\nFallo proceso BackOffice");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar registrar la Entrada", e);
		} 
	}
	
	public void eliminar() {
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

			movimiento = this.ifzMovimientos.findById(this.idMovimiento);
			if (movimiento == null || movimiento.getId() == null || movimiento.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Entrada indicada");
				return;
			}
			
			// Cancelamos
			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzMovimientos.cancelar(movimiento);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al intentar Cancelar la Entrada indicada\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
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
	
	public void reporte() {
		try {
			control();
			this.pojoMovimiento = this.ifzMovimientos.findByIdExt(this.idMovimiento);
			if (this.pojoMovimiento == null || this.pojoMovimiento.getId() == null || this.pojoMovimiento.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar obtener La Entrada seleccionada");
				return;
			}
			
			reporte(this.pojoMovimiento.getId());
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la Entrada a Almacen", e);
		} 
	}
	
	public void eliminarProductosGrid() {
		this.listDetalles = new ArrayList<MovimientosDetalleExt>();
		this.movimientoOrdenCompra = null;
		this.pojoTraspaso = null;
		//this.almacenAnterior = 0;
	}

	public void eliminarDetallesGrid() {
		//this.cantidadDetalles = 0;
		//this.almacenAnterior = 0;
	}

	public void lanzarEvento() {
		try {
			control();
			this.pojoMovimiento = this.ifzMovimientos.findByIdExt(this.idMovimiento);
			if (this.pojoMovimiento == null || this.pojoMovimiento.getId() == null || this.pojoMovimiento.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar el Traspaso seleccionado");
				return;
			}

			// Afectamos existencias por JMS
			// ----------------------------------------------------------------------------------------------------------------------
			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			if (! this.ifzMovimientos.backOfficeInventario(this.pojoMovimiento.getId(), null))
				throw new Exception("La carga de material no se pudo realizar.\nFallo proceso BackOffice");
		} catch (Exception e) {
			control("Ocurrio un problema al lanzar el evento de la Entrada seleccionada", e);
		}
	}
	
	public void validaNuevaEntrada() {
		control();
		/*if (! this.usuarioValido) {
			control(-1, "No es un usuario autorizado para realizar Entradas a Almacen/Bodega");
			return;
		}*/
		
		if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
			control(-1, "Debe seleccionar un Almacen/Bodega para poder realizar Entradas");
			return;
		}
	}

	public void validaCantidadProducto(AjaxBehaviorEvent event) {
		int index = -1;
		
		if (! this.puedeEditar) 
			return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
		
		if ("SO".equals(this.tipoEntrada)) {
			if (event.getComponent().getAttributes().get("indexProducto") == null)
				return;
			index = (int) event.getComponent().getAttributes().get("indexProducto");
			if (index < 0L) 
				return;
			this.listDetalles.get(index).setCantidad_auxiliar1(this.listDetalles.get(index).getCantidad());
			this.listDetalles.get(index).setCantidadSolicitada(this.listDetalles.get(index).getCantidad());
			return;
		}
	}

	public void validaTipoEntrada() {
		if (this.pojoMovimiento == null)
			return;

		this.almacenesAdministrativos = false;
		this.movimientoOrdenCompra = null;
		this.pojoMovimiento.setIdSalida(null);
		this.pojoMovimiento.setIdTraspaso(0L);
		this.pojoMovimiento.setIdOrdenCompra(0L);
		this.listDetalles = new ArrayList<MovimientosDetalleExt>();
		this.pojoTraspaso = null;
		// Asigno usuario recibe: sugerencia
		if (this.pojoEmpleadoUsuario != null)
			this.pojoMovimiento.setRecibe(this.ifzEmpleado.findById(this.pojoEmpleadoUsuario.getId()));
	}

	public void filtrarTipoEntrada() {
		if (this.listTiposEntradas == null)
			this.listTiposEntradas = new ArrayList<SelectItem>();
		this.listTiposEntradas.clear();
		
		log.info("filtrando TiposEntrada ... ");
		switch (this.getAlmacenMovimientoTipo()) {
			case "AL": // 1:almacen
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_ALMACEN.toString(),"Orden de Compra"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString(),"Traspaso de Almacen"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString(),"Devolucion de Bodega"));
				break;
			
			case "BO": // 2:bodega
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString(),"Traspaso de Almacen/Bodega"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_OBRA_BODEGA.toString(),"Devolucion de Obra"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_BODEGA.toString(),"Orden de Compra"));
				break;
			
			default: break; 
		}
		
		if (! this.listTiposEntradas.isEmpty())
			this.tipoEntrada = this.listTiposEntradas.get(0).getValue().toString();
		nuevaEntrada();
	}

	public void regresarAlmacen() {
		log.info("Almacen anterior:");
	}
	
	private void reporte(long idMovimiento) {
		AlmacenMovimientos movimiento = null;
		SimpleDateFormat formatter = null;
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			formatter = new SimpleDateFormat("yyMMddHH");
			nombreDocumento = "E-[tipo]-[id]-[fecha].[extension]";
			log.info("Imprimiendo Reporte de Movimiento de Almacen ... " + idMovimiento);
			movimiento = this.ifzMovimientos.findById(idMovimiento);
			if (movimiento == null || movimiento.getId() == null || movimiento.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar obtener el Movimiento indicado");
				return;
			}
			
			// Parametros del reporte
			log.info("Generando parametros ... ");
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idMovimiento", idMovimiento);

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.entrada"));
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
				control(-1, "Ocurrio un problema al intentar imprimir el Movimiento de Almacen\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 

			log.info("Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = nombreDocumento.replace("[tipo]", movimiento.getTipoEntrada());
			nombreDocumento = nombreDocumento.replace("[id]", String.format("%1$05d", movimiento.getId()).substring(3));
			nombreDocumento = nombreDocumento.replace("[fecha]", formatter.format(Calendar.getInstance().getTime()));
			nombreDocumento = nombreDocumento.replace("[extension]", formatoDocumento);
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido del Movimiento de Almacen");
				control(-1, "Ocurrio un problema al intentar imprimir el Movimiento de Almacen");
				return;
			}

			log.info("Lanzando reporte ... ");
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			log.info("Reporte lanzado. Proceso finalizado");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir el Movimiento de Almacen", e);
		} 
	}

	private boolean validaciones() {
		int productos = 0;

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
		/*
		if (! this.usuarioValido) {
			control(-1, "No es un usuario autorizado para guardar Entradas a Almacen/Bodega");
			return false;
		}
		
		if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
			control(-1, "Debe seleccionar un Almacen/Bodega previamente");
			return false;
		}*/
		
		if (this.pojoMovimiento.getAlmacen() == null) {
			control(10, "Seleccione almacén");
			return false;
		}
		
		if (this.pojoMovimiento.getRecibe() == null) {
			control(11, "Indique el empleado que recibe");
			return false;
		}
		
		if (this.pojoMovimiento.getIdOrdenCompra() > 0) {
			if (this.pojoMovimiento.getFolioFactura() == null || "".equals(this.pojoMovimiento.getFolioFactura().trim())) {
				control(12, "Debe ingresar el folio de la Factura");
				return false;
			}
		}

		if (this.listDetalles.isEmpty()) {
			control(13, "Debe existir por lo menos un producto en la lista");
			return false;
		}
		
		if (! this.isDebug && ! this.sePuedeGuardarMovimiento) {
			control(13, "Esta orden de compra se encuentra completa");
			return false;
		}
		
		//revisar que la cantidad que estan registrando sea mayor a cero
		for (MovimientosDetalleExt md : this.listDetalles) {
			if (md.getCantidad() > 0)
				productos += 1;
			md.setFechaCreacion(Calendar.getInstance().getTime());
			md.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			md.setFechaModificacion(Calendar.getInstance().getTime());
			md.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
		}
		
		if (productos <= 0) {
			control(-1, "Debe indicar la cantidad recibida de cada producto");
			return false;
		}
		
		//revisar que la cantidad que estan registrando, mas la cantidad que ya se entregó, no supere a la solicitada
		for (MovimientosDetalleExt md : this.listDetalles) {
			if (md.getCantidad() > md.getCantidadSolicitada()) {
				control(-1, "La cantidad recibida no puede ser mayor a la solicitada");
				return false;
			}
		}
		
		if (validaRequest("NOSAVE")) {
			control(-1, "Listo para guardar");
			return false;
		}
		
		return true;
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
	
	private void asignaRecibe(long idRecibe) {
		if (idRecibe <= 0L) {
			this.pojoMovimiento.setRecibe(null);
			return;
		}
		
		if (this.pojoMovimiento.getRecibe() != null && validarID(this.pojoMovimiento.getRecibe().getId()) > 0L && this.pojoMovimiento.getRecibe().getId().longValue() == idRecibe)
			return;
		this.pojoMovimiento.setRecibe(this.ifzEmpleado.findById(idRecibe));
	}

	private void asignaEntrega(long idEntrega) {
		if (idEntrega <= 0L) {
			this.pojoMovimiento.setEntrega(null);
			return;
		}
		
		if (this.pojoMovimiento.getEntrega() != null && validarID(this.pojoMovimiento.getEntrega().getId()) > 0L && this.pojoMovimiento.getEntrega().getId().longValue() == idEntrega)
			return;
		this.pojoMovimiento.setEntrega(this.ifzEmpleado.findById(idEntrega));
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
	
	private Long validarID(Long value) {
		return (value != null && value > 0L) ? value : 0L;
	}
	
	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}

	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}

	private void control(boolean incompleta, int tipoMensaje, String mensaje, Throwable throwable) {
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
	
	
	private void cargarAlmacenesOLD() {
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
			this.listAlmacenes = this.ifzAlmacen.findByTipo(tipos, false, "");
			this.listAlmacenes = (this.listAlmacenes != null) ? this.listAlmacenes : new ArrayList<Almacen>();

			idAlmacenesAsignados = new ArrayList<Long>();
			if (! "ADMINISTRADOR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) {
				for (Almacen var : this.listAlmacenesTrabajo) 
					idAlmacenesAsignados.add(var.getId());
			}
			
			this.listAlmacenesItems = new ArrayList<SelectItem>();
			for (Almacen var : this.listAlmacenes) 
				this.listAlmacenesItems.add(new SelectItem(var.getId(), (idAlmacenesAsignados.contains(var.getId()) ? "* " : "") + var.getNombre() + " (" + var.getIdentificador() + ")"));
			
			/*if (! "ADMINISTRADOR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) 
				this.listaAlmacenesFull = this.ifzAlmacen.findByProperty("idEncargado", this.pojoEmpleadoUsuario.getId());
			else
				this.listaAlmacenesFull = this.ifzAlmacen.findAll();
			if (this.listaAlmacenesFull != null && ! this.listaAlmacenesFull.isEmpty()) {
				Collections.sort(this.listaAlmacenesFull, new Comparator<Almacen>() {
					@Override
					public int compare(Almacen o1, Almacen o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
				});
			}
			
			filtrarAlmacenes();*/
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Almacenes", e);
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
	
	// ------------------------------------------------------------------------------------
	// EMPLEADOS
	// ------------------------------------------------------------------------------------
	
	private void cargarEmpleados() {
		this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
		this.listEmpleados = this.ifzEmpleado.findAll();
		this.listEmpleados = this.listEmpleados != null ? this.listEmpleados : new ArrayList<Empleado>();
		
		Collections.sort(this.listEmpleados, new Comparator<Empleado>() {
			@Override
			public int compare(Empleado o1, Empleado o2) {
				return o1.getNombre().compareTo(o2.getNombre());
			}
		});
		
		cargarCboEmpleadoRecibe();
	}

	public void cargarCboEmpleadoRecibe() {
		this.listEmpleadosItems = new ArrayList<SelectItem>();
		for (Empleado e : this.listEmpleados) {
			this.listEmpleadosItems.add(new SelectItem(e.getId(), e.getNombre()));
			
			// Asignamos sugerencia de Empleado si corresponde			
			if (this.pojoMovimiento == null || this.pojoEmpleadoUsuario == null)
				continue;
			if (e.getId().longValue() ==  this.pojoEmpleadoUsuario.getId().longValue())
				this.pojoMovimiento.setRecibe(e);
		}
	}
	
	// ------------------------------------------------------------------------------------
	// BUSQUEDA ORDEN DE COMPRA
	// ------------------------------------------------------------------------------------
	
	public void nuevaBusquedaOrdenCompra() {
		control();
		this.campoBusquedaOC = this.tiposBusquedaOC.get(0).getValue().toString();
		this.valorBusquedaOC = "";
		
		this.listaOrdenesCompra = new ArrayList<OrdenCompra>();
		this.pojoOrdenCompra = null;
		this.numPaginaOrdenCompras = 1;
	}

	public void buscarOrdenesCompra() {
		List<OrdenCompra> auxiliar = null;
		int tipoMaestro = 0;
		
		try {
			control();
			this.campoBusquedaOC = (this.campoBusquedaOC != null && ! "".equals(this.campoBusquedaOC.trim())) ? this.campoBusquedaOC.trim() : this.tiposBusquedaOC.get(0).getValue().toString();
			this.valorBusquedaOC = (this.valorBusquedaOC != null && ! "".equals(this.valorBusquedaOC.trim())) ? this.valorBusquedaOC.trim() : "";
			tipoMaestro = (this.almacenTrabajo.getTipo() > 2) ? TipoMaestro.Administrativo.ordinal() : TipoMaestro.Inventario.ordinal();
			
			this.listaOrdenesCompra = this.ifzOrdenCompra.findLikeProperty(this.campoBusquedaOC, this.valorBusquedaOC, 0L, tipoMaestro, false, true, true, "", 0);
			if (this.listaOrdenesCompra == null || this.listaOrdenesCompra.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
			
			if (this.limiteUbicacionGeografica) {
				auxiliar = new ArrayList<OrdenCompra>();
				for (OrdenCompra orden : this.listaOrdenesCompra) {
					if (this.almacenTrabajo.getIdSucursal() == orden.getIdSucursal())
						auxiliar.add(orden);
				}
				this.listaOrdenesCompra.clear();
				this.listaOrdenesCompra.addAll(auxiliar);
				auxiliar.clear();
				if (this.listaOrdenesCompra == null || this.listaOrdenesCompra.isEmpty()) 
					control(2, "Busqueda sin resultados.\nLa Orden de Compra no es valida para esta Sucursal");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Ordenes de Compra", e);
    	} finally {
			this.numPaginaOrdenCompras = 1;
			this.listaOrdenesCompra = (this.listaOrdenesCompra != null ? this.listaOrdenesCompra : new ArrayList<OrdenCompra>());
    		log.info(this.listaOrdenesCompra.size() + " Ordenes encontradas");
    	}
	}
	
	public void seleccionarOrdenCompra() {
		List<OrdenCompraDetalleExt> listaOrdenCompraDetalle;
		MovimientosDetalleExt detalle = null;
		// --------------------------------------------------
		List<ObraAlmacenes> almacenes = null;
		boolean almancenesValidos = false;
		
		try {
			// Validaciones 
			// ------------------------------------------------------------------------------ 
			if (this.pojoOrdenCompra == null || this.pojoOrdenCompra.getId() == null || this.pojoOrdenCompra.getId() <= 0L)  {
				control(-1, "Ocurrio un problema al recuperar la Orden de Compra seleccionada");
				return;
			}

			this.movimientoOrdenCompra = new OrdenCompra();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(this.movimientoOrdenCompra, this.pojoOrdenCompra);
			
			almacenes = this.ifzObrasAlmacenes.findAll(this.movimientoOrdenCompra.getIdObra(), null);
			if (almacenes == null || almacenes.isEmpty()) {
				control(-1, "La Obra asignada a la Orden de Compra no tiene asignacion valida de Almacenes");
				return;
			}

			if (this.limiteUbicacionGeografica) {
				almancenesValidos = false;
				for (ObraAlmacenes almacen : almacenes) {
					if (almacen.getIdAlmacen() == this.almacenTrabajo.getId().longValue()) {
						almancenesValidos = true;
						break;
					}
				} 
				
				if (! almancenesValidos) {
					control(-1, "No se permiten Entradas a Almacenes/Bodegas que no tiene asignado.\nLa Obra asignada a la Orden de Compra no tiene asignado el Almacen/Bodega seleccionado");
					return;
				}
			}
			
			// Inicializaciones
			// ------------------------------------------------------------------------------
			log.info("Orden de Compra seleccionada: " + this.movimientoOrdenCompra.getFolio() + " (" + this.movimientoOrdenCompra.getId() + ")");
			log.info("Recuperamos los productos de la Orden de Compra ... ");
			listaOrdenCompraDetalle = this.ifzOrdenCompraDetalle.findExtAll(this.movimientoOrdenCompra.getId()); 
			if (listaOrdenCompraDetalle == null || listaOrdenCompraDetalle.isEmpty()) {
				control(-1, "La Orden de Compra seleccionada no tiene Productos asignados");
				return;
			}

			this.sePuedeGuardarMovimiento = false;
			this.pojoMovimiento.setIdOrdenCompra(this.movimientoOrdenCompra.getId());
			this.pojoMovimiento.setFolioOrdenCompra(this.movimientoOrdenCompra.getFolio());
			this.pojoMovimiento.setIdObra(this.movimientoOrdenCompra.getIdObra());
			this.pojoMovimiento.setNombreObra(this.movimientoOrdenCompra.getNombreObra());
			this.pojoMovimiento.setIdTraspaso(0L);
			this.pojoMovimiento.setIdSalida(null);
			this.listDetalles = new ArrayList<MovimientosDetalleExt>();
			this.numPaginaDetalles = 1;

			log.info("Generado detalles de movimiento ... ");
			for (OrdenCompraDetalleExt var : listaOrdenCompraDetalle) {
				detalle = new MovimientosDetalleExt();
				detalle.setProducto(var.getIdProducto());
				detalle.setCantidadSolicitada(var.getPendiente());
				detalle.setCantidad_auxiliar1(var.getPendiente());
				detalle.setCantidad(0);
				//detalle.setPrecioUnitario(var.getPrecioUnitario());
				detalle.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				detalle.setFechaCreacion(Calendar.getInstance().getTime());
				detalle.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				detalle.setFechaModificacion(Calendar.getInstance().getTime());

				if (detalle.getCantidad_auxiliar1() > 0)
					this.sePuedeGuardarMovimiento = true;
				this.listDetalles.add(detalle);
			}
			log.info("Generado detalles de movimiento ... OK");
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los productos de la Orden de Compra seleccionada", e);
		}
	}

	// ------------------------------------------------------------------------------------
	// BUSQUEDA TRASPASOS
	// ------------------------------------------------------------------------------------

	public void nuevaBusquedaTraspaso() {
		control();
		this.campoBusquedaTraspasos = this.tiposBusquedaTraspasos.get(0).getValue().toString();
		this.valorBusquedaTraspasos = "";
		this.puedeBuscarTraspaso = true;
		
		this.listaTraspasos = new ArrayList<AlmacenTraspasoExt>();
		this.numPaginaTraspasos = 1;
	}
	
	public void buscarTraspasos() {
		List<AlmacenTraspasoExt> listAuxiliar = null;
		TipoTraspaso tTraspaso = TipoTraspaso.Ninguno;
		long idAlmacenOrigen = 0L;
		long idAlmacenDestino = 0L;
		
		try {
			control();
			this.campoBusquedaTraspasos = (this.campoBusquedaTraspasos != null && ! "".equals(this.campoBusquedaTraspasos.trim())) ? this.campoBusquedaTraspasos.trim() : this.tiposBusquedaTraspasos.get(0).getValue().toString();
			this.valorBusquedaTraspasos = (this.valorBusquedaTraspasos != null && ! "".equals(this.valorBusquedaTraspasos.trim())) ? this.valorBusquedaTraspasos.trim() : "";
			
			if ("TR TX".contains(this.tipoEntrada)) 
				tTraspaso = TipoTraspaso.TRASPASO;
			else if ("DE".contains(this.tipoEntrada)) 
				tTraspaso = TipoTraspaso.DEVOLUCION;
			else if ("SB".contains(this.tipoEntrada)) 
				tTraspaso = TipoTraspaso.SOLICITUD_BODEGA;
			idAlmacenDestino = this.almacenTrabajo.getId();
			
			this.ifzTraspasos.setInfoSesion(this.loginManager.getInfoSesion());
			listAuxiliar = this.ifzTraspasos.findExtLikeProperty(this.campoBusquedaTraspasos, this.valorBusquedaTraspasos, idAlmacenOrigen, idAlmacenDestino, tTraspaso, false, false, "model.fecha desc, model.id desc", 0);
			if (listAuxiliar == null || listAuxiliar.isEmpty()) {
				control(2, "Busqueda de Traspasos sin resultados");
				return;
			}
			
			for (AlmacenTraspasoExt item : listAuxiliar) {
				if (! this.perfilAdministrativo && item.getAlmacenOrigen().getTipo() > 2)
					continue;
				this.listaTraspasos.add(item);
			}
			
			Collections.sort(this.listaTraspasos, new Comparator<AlmacenTraspasoExt>() {
				@Override
				public int compare(AlmacenTraspasoExt o1, AlmacenTraspasoExt o2) {
					return o2.getId().compareTo(o1.getId());
				}
			});
		} catch (Exception e) {
    		control("Ocurrio un problema al consultar los Traspasos", e);
    	} finally {
			this.listaTraspasos = this.listaTraspasos != null ? this.listaTraspasos : new ArrayList<AlmacenTraspasoExt>();
			this.numPaginaTraspasos = 1;
    	}
	}

	public void seleccionarTraspaso() {
		List<TraspasoDetalleExt> listaTraspasoDetalle;
		MovimientosDetalleExt m = null;

		try {
			control();
			if (this.pojoTraspaso.getId() == null) {
				control(-1, "Debe seleccionar un Traspaso");
				return;
			}
			
			//Se tiene asigna el id de orden de compra a cero, para que no exista confusión en futuras validaciones
			this.sePuedeGuardarMovimiento = true;
			this.pojoMovimiento.setIdOrdenCompra(0L);
			this.pojoMovimiento.setIdTraspaso(this.pojoTraspaso.getId());
			this.pojoMovimiento.setIdSalida(null);
			this.pojoMovimiento.setEntrega(this.pojoTraspaso.getEntrega());
			this.pojoMovimiento.setIdObra(this.pojoTraspaso.getIdObra());
			this.pojoMovimiento.setNombreObra(this.pojoTraspaso.getNombreObra());
			this.listDetalles = new ArrayList<MovimientosDetalleExt>(); 
			log.info("Obteniendo traspaso a detalles");
			listaTraspasoDetalle = this.ifzTraspasosDetalle.findExtAll(this.pojoTraspaso.getId());
			if (listaTraspasoDetalle != null && ! listaTraspasoDetalle.isEmpty()) {
				for (TraspasoDetalleExt var : listaTraspasoDetalle) {
					m = new MovimientosDetalleExt();
					m.setProducto(var.getIdProducto());
					//cantidadAuxiliar fungirá como la cantidad que ya esta entregada y registrada
					// --> Cantidad de producto PENDIENTE
					m.setCantidad_auxiliar1(var.getCantidad() - var.getCantidadRecibida());	
					log.info("Cantidad pendiente: " + m.getCantidad_auxiliar1() + ", ID: " + var.getIdProducto().getId());
					//asignamos al campo cantidad-solicitada el mismo valor que tiene cantidad
					m.setCantidadSolicitada(var.getCantidad());		
					m.setCantidad(var.getCantidad() - var.getCantidadRecibida());
					//Si la cantidad pendiente en alguno de los movimientos es mayor a cero, significa que se puede editar, por lo que se puede guardar
					/*if (m.getCantidad_auxiliar1() > 0)
						this.sePuedeGuardarMovimiento = true;*/
					this.listDetalles.add(m);
				}
			}
			this.numPaginaDetalles = 1;
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los productos del Traspaso seleccionado", e);
		}
	}

	// ------------------------------------------------------------------------------------
	// BUSQUEDA DEVOLUCIONES
	// ------------------------------------------------------------------------------------

	public void nuevaBusquedaDevoluciones() {
		nuevaBusquedaTraspaso();
	}
	
	public void buscarDevoluciones() {
		buscarTraspasos();
	}
	
	public void seleccionarDevolucion() {
		seleccionarTraspaso();
	}
	
	// --------------------------------------------------------------------
	// BUSQUEDA OBRAS 
	// --------------------------------------------------------------------

	public void nuevaBusquedaObrasOLD() {
		control();
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.valorOpcionBusquedaObras = 1;
		
		this.listObras = new ArrayList<Obra>();
		this.pojoObra = new Obra();
		this.numPaginaObras = 1;
	}
	public void nuevaBusquedaObras() {
		control();
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		
		this.numPaginaObras = 1;
		this.listObras = new ArrayList<Obra>();
	}

	public void buscarObrasOLD() {
		try {
    		control();
			this.campoBusquedaObras = (this.campoBusquedaObras != null && ! "".equals(this.campoBusquedaObras.trim())) ? this.campoBusquedaObras.trim() : this.tiposBusquedaObras.get(0).getValue().toString();
			this.valorBusquedaObras = (this.valorBusquedaObras != null && ! "".equals(this.valorBusquedaObras.trim())) ? this.valorBusquedaObras.trim() : "";
			this.valorOpcionBusquedaObras = (this.almacenTrabajo.getTipo() > 2) ? 4 : 0;
			
			if ("SO".equals(this.tipoEntrada))
				this.listObras = this.ifzObras.findLikePropertyByAlmacen(this.campoBusquedaObras, this.valorBusquedaObras, this.almacenTrabajo.getId(), 0);
			else
				this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, 0L, 0L, this.valorOpcionBusquedaObras, (this.valorOpcionBusquedaObras == 4), false, TipoObraRevisadas.Todas, TipoObraAutorizadas.Todas, TipoObraJerarquia.Todas, "nombre", 0);
			if (this.listObras == null || this.listObras.isEmpty()) 
				control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
			control("Ocurrio un problema al consultar las Obras", e);
    	} finally {
    		this.listObras = this.listObras != null ? this.listObras : new ArrayList<Obra>();
			this.numPaginaObras = 1;
    		log.info(this.listObras.size() + " Obra encontradas");
    	}
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
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Obra seleccionada");
				return;
			}
			
			this.sePuedeGuardarMovimiento = false;
			this.pojoMovimiento.setIdOrdenCompra(0L);
			this.pojoMovimiento.setIdTraspaso(0L);
			this.pojoMovimiento.setIdSalida(null);
			this.pojoMovimiento.setIdObra(this.pojoObra.getId());
			this.pojoMovimiento.setNombreObra(this.pojoObra.getNombre());
			this.listDetalles = new ArrayList<MovimientosDetalleExt>(); 
			asignaEntrega(this.pojoObra.getIdResponsable());
			this.sePuedeGuardarMovimiento = true;
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los productos de la Orden de Compra seleccionada", e);
		}
	}

	//  -----------------------------------------------------------------------------------------
	// BUSQUEDA PRODUCTOS
	//  -----------------------------------------------------------------------------------------

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
	
	public void nuevaBusquedaProductos() {
		control();
		this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
		this.valorBusquedaProductos = "";
		this.idFamilia = 0;
		
		this.listaBusquedaProductos = new ArrayList<Producto>(); 
		this.pojoProductoBusqueda = new Producto();
		this.numPaginaProductos = 1;
	}
	
	public void buscarProductos() {
		int tipoMaestro = 0;
		
		try {
			control();
			this.campoBusquedaProductos = (this.campoBusquedaProductos != null && ! "".equals(this.campoBusquedaProductos.trim())) ? this.campoBusquedaProductos.trim() : this.tiposBusquedaProductos.get(0).getValue().toString();
			this.valorBusquedaProductos = (this.valorBusquedaProductos != null && ! "".equals(this.valorBusquedaProductos.trim())) ? this.valorBusquedaProductos.trim() : "";
			tipoMaestro = (this.almacenTrabajo.getTipo() > 2) ? 2 : 1;
			
			this.listaBusquedaProductos = this.ifzProductos.findLikeProperty(this.campoBusquedaProductos, this.valorBusquedaProductos, this.idFamilia, tipoMaestro, "", 0);
			if (this.listaBusquedaProductos == null || this.listaBusquedaProductos.isEmpty()) {
				control(2, "La busqueda no devolvio resultados");
				return;
			}
			
			// Ordenamos por descripcion
			Collections.sort(this.listaBusquedaProductos, new Comparator<Producto>() {
				@Override
				public int compare(Producto o1, Producto o2) {
					return o1.getDescripcion().compareTo(o2.getDescripcion());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Productos de la Bodega seleccionada", e);
		} finally {
			this.listaBusquedaProductos = this.listaBusquedaProductos != null ? this.listaBusquedaProductos : new ArrayList<Producto>(); 
			this.numPaginaProductos = 1;
		}
	}

	public void seleccionarProducto() {
		MovimientosDetalleExt detalle = null;
		ProductoExt var = null;
		
		try {
			// Validaciones
			if (this.pojoProductoBusqueda == null || this.pojoProductoBusqueda.getId() == null || this.pojoProductoBusqueda.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Producto seleccionado");
				return;
			}
			
			for (MovimientosDetalleExt item : this.listDetalles) {
				if (this.pojoProductoBusqueda.getId().longValue() == item.getProducto().getId().longValue()) {
					control(-1, "El producto seleccionada ya ha sido agregado previamente");
					return;
				}
			}
			
			// Extendemos producto
			var = this.ifzProductos.findExtById(this.pojoProductoBusqueda.getId());
			detalle = new MovimientosDetalleExt();
			detalle.setProducto(var);
			detalle.setCantidad_auxiliar1(0);	
			detalle.setCantidadSolicitada(0);		
			detalle.setCantidad(0);
			this.listDetalles.add(0, detalle);
			this.sePuedeGuardarMovimiento = true;
			this.numPaginaDetalles = 1;
			control(-1, "Producto Añadido");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el Producto seleccionado", e);
		}
	}

	public void quitarProducto() {
		Map<String,String> params = null;
		long idProductoBorrar = 0L;
		
		try {
			control();
			params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			if (params.containsKey("idProductoBorrar"))
				idProductoBorrar = Long.valueOf(params.get("idProductoBorrar"));
			if (idProductoBorrar <= 0L) {
				control(-1, "Ocurrio un problema al intentar eliminar el Producto indicado");
				return;
			}
			
			for (MovimientosDetalleExt detalle : this.listDetalles) {
				if (idProductoBorrar == detalle.getProducto().getId().longValue()) {
					this.listDetalles.remove(detalle);
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Producto seleccionado", e);
		}
	}
	
	// ------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------

	public String getTitulo() {
		String descripcion = "Nueva Entrada";
		if (this.pojoMovimiento != null && this.pojoMovimiento.getId() != null && this.pojoMovimiento.getId() > 0L) 
			descripcion = "Entrada";
		if (this.tipoEntrada == null || "".equals(this.tipoEntrada.trim()))
			this.tipoEntrada = "";
		switch (this.tipoEntrada) {
			case "OC": descripcion += " por Orden de Compra"; break;
			case "TX": 
			case "TR": descripcion += " por Traspaso"; break;
			case "DE": 
			case "SO": descripcion += " por Devolucion"; break;
			default  : break;
		}
		descripcion += ("".equals(this.getAlmacenMovimientoTipo()) ? "" : "AL".equals(this.getAlmacenMovimientoTipo()) ? " a Almacen" : " a Bodega");
		descripcion += (this.almacenTrabajo != null && this.almacenTrabajo.getTipo() > 2 ? " (ADMINISTRATIVA)" : "");
		
		return descripcion;
	}
	
	public void setTitulo(String value) {}
	
	public String getAlmacenTrabajo() {
		if (this.almacenTrabajo != null && this.almacenTrabajo.getId() != null && this.almacenTrabajo.getId() > 0L)
			return this.almacenTrabajo.getNombre() + " (" + this.almacenTrabajo.getIdentificador() + ")";
		return "";
	}
	
	public void setAlmacenTrabajo(String value) {}
	
	public String getOrdenCompra() {
		if (this.movimientoOrdenCompra != null && this.movimientoOrdenCompra.getId() != null && this.movimientoOrdenCompra.getId() > 0L)
			return "<b>" + this.movimientoOrdenCompra.getId() + "</b> - " + this.movimientoOrdenCompra.getFolio() + " - " + this.movimientoOrdenCompra.getNombreProveedor();
		return "";
	}
	
	public void setOrdenCompra(String value) {}

	public String getTraspaso() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getId() != null && this.pojoTraspaso.getId() > 0L)
			return "<b>" + this.pojoTraspaso.getId() + "</b> - " + ((new SimpleDateFormat("dd-MMM-yyyy")).format(this.pojoTraspaso.getFecha())) + " " + this.pojoTraspaso.getAlmacenOrigen().getNombre();
		return "";
	}
	
	public void setTraspaso(String value) {}

	public String getObraOrigen() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getObraOrigen() != null)
			return "<b>" + this.pojoTraspaso.getObraOrigen().getId() + "</b> - " + this.pojoTraspaso.getObraOrigen().getNombre();
		return "Sin Especificar";
	}
	
	public void setObraOrigen(String value) {}
	
	public String getObra() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getIdObra() > 0L)
			return "<b>" + this.pojoMovimiento.getIdObra() + "</b> - " + this.pojoMovimiento.getNombreObra();
		return "";
	}
	
	public void setObra(String value) {}
	
	public long getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(long idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public String getFolioFactura() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getFolioFactura() != null && ! "".equals(this.pojoMovimiento.getFolioFactura().trim()))
			return this.pojoMovimiento.getFolioFactura();
		return "";
	}

	public void setFolioFactura(String folioFactura) {
		if (this.pojoMovimiento == null)
			this.pojoMovimiento = new AlmacenMovimientosExt();
		this.pojoMovimiento.setFolioFactura(folioFactura);
	}

	public Date getFecha() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getFecha() != null) 
			return this.pojoMovimiento.getFecha(); 
		return Calendar.getInstance().getTime();
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

	public boolean isBand() {
		return this.isOperacionCompleta();
	}

	public void setBand(boolean band) {
		this.setOperacionCompleta(band);
	}

	public String getResOperacion() {
		return this.getMensaje();
	}

	public void setResOperacion(String resOperacion) {
		this.setMensaje(resOperacion);
	}

	public boolean isOperacionCompleta() {
		return operacionCancelada;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCancelada = operacionCompleta;
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

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public List<AlmacenMovimientos> getListaMovimientosGrid() {
		return listMovimientos;
	}

	public void setListaMovimientosGrid(List<AlmacenMovimientos> listaMovimientosGrid) {
		this.listMovimientos = listaMovimientosGrid;
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

	public int getValorOpcionBusquedaObras() {
		return valorOpcionBusquedaObras;
	}

	public void setValorOpcionBusquedaObras(int valorOpcionBusquedaObras) {
		this.valorOpcionBusquedaObras = valorOpcionBusquedaObras;
	}

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}

	public List<Obra> getListObrasPrincipales() {
		return listObras;
	}

	public void setListObrasPrincipales(List<Obra> listObrasPrincipales) {
		this.listObras = listObrasPrincipales;
	}
	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public long getIdEmpleadoRecibe() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getEntrega() != null && validarID(this.pojoMovimiento.getEntrega().getId()) > 0L)
			return this.pojoMovimiento.getEntrega().getId();
		return 0;
	}

	public void setIdEmpleadoRecibe(long idEmpleadoRecibe) {
		if (idEmpleadoRecibe <= 0L) {
			this.pojoMovimiento.setEntrega(null);
			return;
		}
		
		for (Empleado empleado : this.listEmpleados) {
			if (idEmpleadoRecibe == empleado.getId().longValue()) {
				this.pojoMovimiento.setEntrega(empleado);
				break;
			}
		}
	}

	public List<SelectItem> getListEmpleadosItems() {
		return listEmpleadosItems;
	}

	public void setListEmpleadosItems(List<SelectItem> listaCboEmpleadoRecibe) {
		this.listEmpleadosItems = listaCboEmpleadoRecibe;
	}

	public List<Empleado> getListaEmpleados() {
		return listEmpleados;
	}

	public void setListaEmpleados(List<Empleado> listaEmpleados) {
		this.listEmpleados = listaEmpleados;
	}

	public String getValorBusquedaOC() {
		return valorBusquedaOC;
	}

	public void setValorBusquedaOC(String valorBusquedaOC) {
		this.valorBusquedaOC = valorBusquedaOC;
	}

	public List<OrdenCompra> getListaOrdenesCompra() {
		return listaOrdenesCompra;
	}

	public void setListaOrdenesCompra(List<OrdenCompra> listaOrdenesCompra) {
		this.listaOrdenesCompra = listaOrdenesCompra;
	}

	public OrdenCompra getPojoOrdenCompra() {
		return pojoOrdenCompra;
	}

	public void setPojoOrdenCompra(OrdenCompra pojoOrdenCompra) {
		this.pojoOrdenCompra = pojoOrdenCompra;
	}

	public int getNumPaginaOrdenCompras() {
		return numPaginaOrdenCompras;
	}

	public void setNumPaginaOrdenCompras(int numPaginaOrdenCompras) {
		this.numPaginaOrdenCompras = numPaginaOrdenCompras;
	}

	public List<SelectItem> getTiposBusquedaOC() {
		return tiposBusquedaOC;
	}

	public void setTiposBusquedaOC(List<SelectItem> tiposBusquedaOC) {
		this.tiposBusquedaOC = tiposBusquedaOC;
	}

	public String getCampoBusquedaOC() {
		return campoBusquedaOC;
	}

	public void setCampoBusquedaOC(String campoBusquedaOC) {
		this.campoBusquedaOC = campoBusquedaOC;
	}

	public List<MovimientosDetalleExt> getListaProductosEntrada() {
		return listDetalles;
	}

	public void setListaProductosEntrada(List<MovimientosDetalleExt> listaProductosEntrada) {
		this.listDetalles = listaProductosEntrada;
	}
	
	public List<SelectItem> getTiposBusquedaTraspasos() {
		return tiposBusquedaTraspasos;
	}

	public void setTiposBusquedaTraspasos(List<SelectItem> tiposBusquedaTraspasos) {
		this.tiposBusquedaTraspasos = tiposBusquedaTraspasos;
	}

	public String getCampoBusquedaTraspasos() {
		return campoBusquedaTraspasos;
	}

	public void setCampoBusquedaTraspasos(String campoBusquedaTraspasos) {
		this.campoBusquedaTraspasos = campoBusquedaTraspasos;
	}

	public String getValorBusquedaTraspasos() {
		return valorBusquedaTraspasos;
	}

	public void setValorBusquedaTraspasos(String valorBusquedaTraspasos) {
		this.valorBusquedaTraspasos = valorBusquedaTraspasos;
	}

	public AlmacenTraspasoExt getPojoTraspaso() {
		return pojoTraspaso;
	}

	public void setPojoTraspaso(AlmacenTraspasoExt pojoTraspaso) {
		this.pojoTraspaso = pojoTraspaso;
	}

	public List<AlmacenTraspasoExt> getListaTraspasos() {
		return listaTraspasos;
	}

	public void setListaTraspasos(List<AlmacenTraspasoExt> listaTraspasos) {
		this.listaTraspasos = listaTraspasos;
	}

	public List<TraspasoDetalleExt> getListaTraspasosDetalle() {
		return listaTraspasosDetalle;
	}

	public void setListaTraspasosDetalle(List<TraspasoDetalleExt> listaTraspasosDetalle) {
		this.listaTraspasosDetalle = listaTraspasosDetalle;
	}

	public boolean isPuedeBuscarTraspaso() {
		return puedeBuscarTraspaso;
	}

	public void setPuedeBuscarTraspaso(boolean puedeBuscarTraspaso) {
		this.puedeBuscarTraspaso = puedeBuscarTraspaso;
	}

	public int getNumPaginaTraspasos() {
		return numPaginaTraspasos;
	}

	public void setNumPaginaTraspasos(int numPaginaTraspasos) {
		this.numPaginaTraspasos = numPaginaTraspasos;
	}

	public List<SelectItem> getListTiposEntradas() {
		return listTiposEntradas;
	}
	
	public void setListTiposEntradas(List<SelectItem> listTiposEntradas) {
		this.listTiposEntradas = listTiposEntradas;
	}
	
	public String getTipoEntrada() {
		return tipoEntrada;
	}
	
	public void setTipoEntrada(String tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public boolean isPerfilAdministrativo() {
		return perfilAdministrativo;
	}

	public void setPerfilAdministrativo(boolean perfilAdministrativo) {
		this.perfilAdministrativo = perfilAdministrativo;
	}

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}

	public boolean isDebugging() { 
		if (this.paramsRequest.containsKey("DEBUG")) 
			return true;
		return this.isDebug;
	}
	
	public void setDebugging(boolean value) { }
	
	public int getNumeroItems() {
		if (this.listDetalles != null && ! this.listDetalles.isEmpty())
			return this.listDetalles.size();
		return 0;
	}
	
	public void setNumeroItems(int numero) {}

	public List<SelectItem> getListAlmacenesTrabajoItems() {
		return listAlmacenesItems;
	}

	public void setListAlmacenesTrabajoItems(List<SelectItem> listAlmacenesTrabajoItems) {
		this.listAlmacenesItems = listAlmacenesTrabajoItems;
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
				this.almacenEncargado = this.ifzEmpleado.findById(almacen.getIdEncargado());
				asignaRecibe(almacen.getIdEncargado());
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
			return this.almacenTrabajo.getId() + " - " + this.almacenTrabajo.getNombre();
		return "";
	}
	
	public void setAlmacenMovimiento(String value) {}
	
	public String getAlmacenMovimientoTipo() {
		if (this.almacenTrabajo != null && this.almacenTrabajo.getId() != null && this.almacenTrabajo.getId() > 0L)
			return this.almacenTrabajo.getTipo() == 1 || this.almacenTrabajo.getTipo() == 3 ? "AL" : "BO";
		return "";
	}
	
	public void setAlmacenMovimientoTipo(String value) {}

	public String getRecibe() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getRecibe() != null && validarID(this.pojoMovimiento.getRecibe().getId()) > 0L)
			return this.pojoMovimiento.getRecibe().getId() + " - " + this.pojoMovimiento.getRecibe().getNombre();
		return "";
	}

	public void setRecibe(String recibe) {}

	public String getEnvia() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getEntrega() != null && validarID(this.pojoMovimiento.getEntrega().getId()) > 0L)
			return this.pojoMovimiento.getEntrega().getId() + " - " + this.pojoMovimiento.getEntrega().getNombre();
		return "";
	}

	public void setEnvia(String recibe) {}

	public List<Producto> getListaBusquedaProductos() {
		return listaBusquedaProductos;
	}

	public void setListaBusquedaProductos(List<Producto> listaBusquedaProductos) {
		this.listaBusquedaProductos = listaBusquedaProductos;
	}

	public List<SelectItem> getTiposBusquedaProductos() {
		return tiposBusquedaProductos;
	}

	public Producto getPojoProductoBusqueda() {
		return pojoProductoBusqueda;
	}

	public void setPojoProductoBusqueda(Producto pojoProductoBusqueda) {
		this.pojoProductoBusqueda = pojoProductoBusqueda;
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

	public int getNumPaginaProductos() {
		return numPaginaProductos;
	}

	public void setNumPaginaProductos(int numPaginaProductos) {
		this.numPaginaProductos = numPaginaProductos;
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
	
	public int getNumPaginaDetalles() {
		return numPaginaDetalles;
	}
	
	public void setNumPaginaDetalles(int numPaginaDetalles) {
		this.numPaginaDetalles = numPaginaDetalles;
	}

	public boolean isAdministrativo() {
		return PERFIL_ADMINISTRATIVO;
	}

	public void setAdministrativo(boolean pAdministrativo) { }
}
