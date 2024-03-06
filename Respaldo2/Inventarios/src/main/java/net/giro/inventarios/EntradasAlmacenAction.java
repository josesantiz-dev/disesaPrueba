package net.giro.inventarios;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenes;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalleExt;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenMovimientosExt;
import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.MovimientosDetalleExt;
import net.giro.inventarios.beans.TipoMovimientoInventario;
import net.giro.inventarios.beans.TipoMovimientoReferenciaExtendida;
import net.giro.inventarios.beans.TipoTraspaso;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicInventariosEventos;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

@ViewScoped
@ManagedBean(name="entradasAlmacenAction")
public class EntradasAlmacenAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(EntradasAlmacenAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx;
	
	private AlmacenMovimientosRem ifzMovimientos;
	private MovimientosDetalleRem ifzMovimientosDetalle;
	private AlmacenRem ifzAlmacen;
	private EmpleadoRem ifzEmpleado;
	private OrdenCompraRem ifzOrdenCompra;
	private OrdenCompraDetalleRem ifzOrdenCompraDetalle;
	private AlmacenTraspasoRem ifzTraspasos;
	private TraspasoDetalleRem ifzTraspasosDetalle;
	private ObraAlmacenesRem ifzObrasAlmacenes;
	private ReportesRem ifzReportes;
    private long usuarioId;
    private String usuario;
	private boolean band;
	private String mensaje;
	private String mensajeDetalles;
	private int tipoMensaje;
	private List<AlmacenMovimientosExt> listaMovimientosGrid;
	private AlmacenMovimientosExt pojoMovimiento; 	
	private List<Empleado> listaEmpleados;
	private List<SelectItem> listaCboEmpleadoRecibe;
	// busqueda principal
	private List<SelectItem> listaCampoBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Almacenes
	private List<Almacen> listaAlmacenesFull;
	private List<Almacen> listaAlmacenes;
	private List<SelectItem> listaCboAlmacenes;
	private boolean almacenesAdministrativos;
	// Busqueda obras
	private List<Obra> listObrasPrincipales;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorOpcionBusquedaObras;	
	private int numPaginaObras;
	//	Busqueda Orden Compra
	private List<SelectItem> tiposBusquedaOC;	
	private String campoBusquedaOC;
	private String valorBusquedaOC;
	private OrdenCompra pojoOrdenCompra;
	private List<OrdenCompra> listaOrdenesCompra;
	private int numPaginaOrdenCompra;
	private int numPaginaOrdenCompras;
	private boolean avanzadaBusquedaOC;
	//	Buscando traspasos
	private List<SelectItem> tiposBusquedaTraspasos;
	private String campoBusquedaTraspasos;
	private String valorBusquedaTraspasos;
	private int numPaginaTraspasos;
	private AlmacenTraspasoExt pojoTraspaso;
	private List<AlmacenTraspasoExt> listaTraspasos;
	private List<TraspasoDetalleExt> listaTraspasosDetalle;
	private boolean puedeBuscarTraspaso;	
	private boolean puedeEditar;
	private boolean avanzadaBusquedaTraspaso;
	// Busqueda SALIDAS
	private List<AlmacenMovimientosExt> listSalidaBusqueda;
	private AlmacenMovimientosExt pojoSalida;
	private List<SelectItem> salidaBusquedaTipos;
	private String salidaBusquedaCampo;
	private String salidaBusquedaValor;
	private int salidaBusquedaPaginas;
	private boolean avanzadaBusquedaSalida;
	private Collection<Object> selection;
    private List<MovimientosDetalleExt> selectionItems = new ArrayList<MovimientosDetalleExt>();	
	private double cantidadProductoSeleccionado;
	private int rowSeleccionado;
	private MovimientosDetalleExt movimientoSeleccionado;
	private List<MovimientosDetalleExt> listaProductosEntrada;	
	private int cantidadDetalles;
	private long almacenAnterior;	
	private boolean sePuedeGuardarMovimiento;
	private List<SelectItem> listTiposEntradas;
	private String tipoEntrada;
	// EMPLEADO-USUARIO
	private boolean usuarioValido;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalBase;
	private List<Almacen> listEmpleadoAlmacenes;
	private int nivelAlmacenes;
	private List<Long> listPuestosValidos;
	// Tipo de Entradas
	private List<SelectItem> listTargetAlmacenes;
	private int targetAlmacen;
	// PERFILES
	private boolean perfilAdministrativo;
		
	
	public EntradasAlmacenAction() {
		ValueExpression ve = null;
		FacesContext fc = null;
		Application app = null;
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

			resValue = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilAdministrativo = ((resValue != null && "S".equals(resValue)) ? true : false);
			log.info("Perfil EGRESOS_OPERACION: " + (this.perfilAdministrativo ? "SI" : "NO"));
			
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
			this.ifzMovimientos = (AlmacenMovimientosRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzMovimientosDetalle = (MovimientosDetalleRem) this.ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");
			this.ifzAlmacen = (AlmacenRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzObrasAlmacenes = (ObraAlmacenesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzOrdenCompra = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOrdenCompraDetalle = (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzTraspasos = (AlmacenTraspasoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
			this.ifzTraspasosDetalle = (TraspasoDetalleRem) this.ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");

			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMovimientosDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObrasAlmacenes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenCompra.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOrdenCompraDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspasos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspasosDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			
			// VALORES SUGERIDOS
			this.usuarioValido = false;
			if (comprobarUsuario())
				this.usuarioValido = true;

			this.listObrasPrincipales = new ArrayList<Obra>();
			this.listaMovimientosGrid = new ArrayList<AlmacenMovimientosExt>();
			this.listaAlmacenes = new ArrayList<Almacen>();
			this.listaCboAlmacenes = new ArrayList<SelectItem>();
			
			this.listTargetAlmacenes = new ArrayList<SelectItem>();
			this.listTargetAlmacenes.add(new SelectItem(1, "Almacen"));
			this.listTargetAlmacenes.add(new SelectItem(2, "Bodega"));
			this.targetAlmacen = (int) this.listTargetAlmacenes.get(0).getValue();
			
			// Tipos de Entradas
			log.info("Tipos de Entradas : " + this.nivelAlmacenes);
			this.listTiposEntradas = new ArrayList<SelectItem>();
			switch (this.nivelAlmacenes) {
				case 1: // 1:almacen
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_ALMACEN.toString(),"Orden de Compra"));
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString(),"Traspaso"));
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString(),"Devolucion"));
					break;
				case 2: // 2:bodega
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_BODEGA.toString(),"Orden de Compra"));
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString(),"Traspaso"));
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_OBRA_BODEGA.toString(),"Devolucion"));
					break;
				default: // 3:ambos
					filtrarTipoEntrada();
					/*
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_ALMACEN.toString(),"Orden de Compra a Almacen"));
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_BODEGA.toString(),"Orden de Compra a Bodega"));
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString(),"Traspaso de Almacen a Almacen"));
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString(),"Traspaso de Almacen a Bodega"));
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString(),"Devolucion de Bodega a Almacen"));
					this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_OBRA_BODEGA.toString(),"Devolucion de Obra a Bodega"));
					*/
					break;
			}
			this.tipoEntrada = this.listTiposEntradas.get(0).getValue().toString();
			
			
			// Busqueda principal
			this.listaCampoBusqueda = new ArrayList<SelectItem>();
			this.listaCampoBusqueda.add(new SelectItem("folioFactura", "Factura" ));
			this.listaCampoBusqueda.add(new SelectItem("folioOrdenCompra", "Orden de Compra" ));
			this.listaCampoBusqueda.add(new SelectItem("fecha", "Fecha" ));
			this.listaCampoBusqueda.add(new SelectItem("nombre", "Almacen" ));
			this.listaCampoBusqueda.add(new SelectItem("recibeNombre", "Recibe" ));
			this.listaCampoBusqueda.add(new SelectItem("idTraspaso", "Traspaso" ));
			this.listaCampoBusqueda.add(new SelectItem("id", "ID" ));
			this.campoBusqueda = this.listaCampoBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
			this.valorBusquedaObras = "";
			this.valorOpcionBusquedaObras = 1;
			
			// ORDENES DE COMPRA
			this.listaOrdenesCompra = new ArrayList<OrdenCompra>();
			this.tiposBusquedaOC = new ArrayList<SelectItem>();
			this.tiposBusquedaOC.add(new SelectItem("folio","Folio"));
			this.tiposBusquedaOC.add(new SelectItem("nombreObra","Obra"));
			this.tiposBusquedaOC.add(new SelectItem("nombreProveedor","Proveedor"));
			this.tiposBusquedaOC.add(new SelectItem("nombreSolicita","Solicita"));
			this.tiposBusquedaOC.add(new SelectItem("id", "ID"));
			this.campoBusquedaOC = this.tiposBusquedaOC.get(0).getValue().toString();
			this.valorBusquedaOC = "";
			this.numPaginaOrdenCompras = 1;
			
			// Busqueda Traspasos
			this.listaTraspasos = new ArrayList<AlmacenTraspasoExt>();
			this.tiposBusquedaTraspasos = new ArrayList<>();
			this.tiposBusquedaTraspasos.add(new SelectItem("*","Coincidencia"));
			this.tiposBusquedaTraspasos.add(new SelectItem("nombre","Almacen"));
			this.tiposBusquedaTraspasos.add(new SelectItem("a.nombre","Almacen Origen"));
			this.tiposBusquedaTraspasos.add(new SelectItem("b.nombre","Almacen Destino"));
			this.tiposBusquedaTraspasos.add(new SelectItem("entregaNombre","Entrega"));
			this.tiposBusquedaTraspasos.add(new SelectItem("recibeNombre","Recibe"));
			this.tiposBusquedaTraspasos.add(new SelectItem("id","ID"));
			this.campoBusquedaTraspasos = this.tiposBusquedaTraspasos.get(0).getValue().toString();
			this.valorBusquedaTraspasos = "";
			this.numPaginaTraspasos = 1;
			
			// Busqueda SALIDAS
			this.listaTraspasos = new ArrayList<AlmacenTraspasoExt>();
			this.salidaBusquedaTipos = new ArrayList<SelectItem>();
			this.salidaBusquedaTipos.add(new SelectItem("nombreObra","Obra"));
			this.salidaBusquedaTipos.add(new SelectItem("e.nombre","Entrega"));
			this.salidaBusquedaTipos.add(new SelectItem("r.nombre","Recibe"));
			this.salidaBusquedaCampo = this.salidaBusquedaTipos.get(0).getValue().toString();
			this.salidaBusquedaValor = "";
			this.salidaBusquedaPaginas = 1;
			
			cargarEmpleados();
			//cargarAlmacenes();
			
			this.puedeEditar = true;
			this.pojoMovimiento = new AlmacenMovimientosExt();
			this.listaProductosEntrada = new ArrayList<MovimientosDetalleExt>();
		} catch (Exception e) {
			log.error("Error al instanciar EntradasAlmacenAction", e);
		}
	}
	

	public void buscar() {
		try {
			control();
			this.listaMovimientosGrid = this.ifzMovimientos.findExtLikeProperty(this.campoBusqueda, this.valorBusqueda, TipoMovimientoInventario.ENTRADA.ordinal(), "", 0);
			if (this.listaMovimientosGrid == null || this.listaMovimientosGrid.isEmpty()) {
				control(2, "Busqueda de Entradas sin resultados");
				return;
			}

			Collections.sort(this.listaMovimientosGrid, new Comparator<AlmacenMovimientosExt>() {
				@Override
				public int compare(AlmacenMovimientosExt o1, AlmacenMovimientosExt o2) {
					return o2.getFecha().compareTo(o1.getFecha());
				}
			});
		} catch (Exception e) {
    		control("Ocurrio un problema al consultar las Entradas de Almacen", e);
    	} finally {
    		this.numPagina = 1;
    	}
	}

	public void nuevaEntrada() {
		control();
		this.puedeEditar = true;
		this.pojoMovimiento = new AlmacenMovimientosExt();
		this.pojoMovimiento.setTipo(TipoMovimientoInventario.ENTRADA.ordinal());
		this.pojoMovimiento.setIdTraspaso(0L);
		this.pojoMovimiento.setIdOrdenCompra(0L);
		this.tipoEntrada = this.listTiposEntradas.get(0).getValue().toString();
		this.targetAlmacen = (int) this.listTargetAlmacenes.get(0).getValue();
		
		this.pojoOrdenCompra = null;
		this.pojoTraspaso = null;
		this.pojoSalida = null;
		
		if (this.listaProductosEntrada == null)
			this.listaProductosEntrada = new ArrayList<MovimientosDetalleExt>();
		this.listaProductosEntrada.clear();
		
		//cargarAlmacenes();
		if (! this.usuarioValido) {
			control(-1, "No es un usuario autorizado para realizar Entradas a Almacen/Bodega");
			return;
		}
		
		// Asigno usuario recibe: sugerencia
		if (this.pojoEmpleadoUsuario != null)
			this.pojoMovimiento.setRecibe(this.ifzEmpleado.findById(this.pojoEmpleadoUsuario.getId()));
	}

	public void editar() {
		try {
			control();
			this.puedeEditar = false;
			this.pojoOrdenCompra = null;
			this.pojoTraspaso = null;
			this.pojoSalida = null;

			this.tipoEntrada = this.pojoMovimiento.getTipoEntrada();
			this.targetAlmacen = this.pojoMovimiento.getAlmacen().getTipo();
			if (this.targetAlmacen == 3)
				this.targetAlmacen = 1;
			if (this.targetAlmacen == 4)
				this.targetAlmacen = 2;
			
			// Obtenemos empleado que recibe
			this.pojoMovimiento.setRecibe(this.ifzEmpleado.findById(this.pojoMovimiento.getRecibe().getId()));
			
			// Obtenemos Orden de Compra si corresponde: OC
			if (TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_ALMACEN.toString().equals(this.tipoEntrada)) {
				this.pojoOrdenCompra = this.ifzOrdenCompra.findById(this.pojoMovimiento.getIdOrdenCompra());
				this.almacenesAdministrativos = (this.pojoOrdenCompra.getTipo() == 2);
			}
			
			// Obtenemos Traspaso si corresponde: TR | DE
			if (TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString().equals(this.tipoEntrada) || TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString().equals(this.tipoEntrada)) {
				this.pojoTraspaso = this.ifzTraspasos.findByIdExt(this.pojoMovimiento.getIdTraspaso());
				this.almacenesAdministrativos = (this.pojoTraspaso.getAlmacenOrigen().getTipo() == 3 || this.pojoTraspaso.getAlmacenOrigen().getTipo() == 4);
			}
			
			// Obtenemos la salida si corresponde: SO
			if (TipoMovimientoReferenciaExtendida.SALIDA_OBRA.toString().equals(this.tipoEntrada)) {
				this.pojoSalida = this.pojoMovimiento.getIdSalida();
				this.almacenesAdministrativos = (this.pojoSalida.getAlmacen().getTipo() == 3 || this.pojoSalida.getAlmacen().getTipo() == 4);
			}

			// Filtramos Almacenes y/o Bodegas
			cargarAlmacenes();
			
			// Cargamos detalles
			this.listaProductosEntrada = this.ifzMovimientosDetalle.findDetallesExtById(this.pojoMovimiento.getId());
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los Productos de la Entrada seleccionada", e);
		}
	}

	public void guardar() {
		List<MovimientosDetalle> listaDetalle = null;
		MovimientosDetalle d = null;
		long idTarget = 0L;
		long idReferencia = 0L;
		boolean autoTraspaso = false;
		
		try {
			control();
			log.info("Validando ... ");
			if (! validaGuardarMovimiento()) 
				return;
			
			this.pojoMovimiento.setTipo(TipoMovimientoInventario.ENTRADA.ordinal());//  this.TIPO_MOVIMIENTO);
			this.pojoMovimiento.setTipoEntrada(this.tipoEntrada);
			
			// Guardar entrada
			log.info("Guardando entrada ... ");
			if (this.pojoMovimiento.getId() == null || this.pojoMovimiento.getId() <= 0L) {	
				this.pojoMovimiento.setCreadoPor(this.usuarioId);
				this.pojoMovimiento.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoMovimiento.setModificadoPor(this.usuarioId);
				this.pojoMovimiento.setFechaModificacion(Calendar.getInstance().getTime());
				
				autoTraspaso = false;
				if (this.tipoEntrada.equals(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_ALMACEN.toString()) && 10000553 == this.pojoOrdenCompra.getIdProveedor().longValue())
					autoTraspaso = true;
				
				if (this.tipoEntrada.equals(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_BODEGA.toString()) || autoTraspaso) {
					if (! subProcesoTraspaso(autoTraspaso))
						return;
				}
				
				// Guardo en la BD
				this.pojoMovimiento.setId(this.ifzMovimientos.save(this.pojoMovimiento));
				
				// Añadimos a listado
				this.listaMovimientosGrid.add(this.pojoMovimiento);
				
				// Guardo detalles
				log.info("Guardando detalles ... ");
				for (MovimientosDetalleExt md : this.listaProductosEntrada) {
					md.setIdAlmacenMovimiento(this.pojoMovimiento.getId());
					md.setModificadoPor(this.usuarioId);
					md.setFechaModificacion(Calendar.getInstance().getTime());
					md.setCreadoPor(this.usuarioId);
					md.setFechaCreacion(Calendar.getInstance().getTime());
				
					//Solo si se puede actualizar/guardar esta cantidadAuxiliar del movimiento y si la cantidad no es cero
					if (md.getCantidad_auxiliar1() != 0 && md.getCantidad() > 0)
						this.ifzMovimientosDetalle.save(md);
				}
				
				// Genero los atributos para el BACKORDER
				listaDetalle = new ArrayList<MovimientosDetalle>();
				for (MovimientosDetalleExt md : this.listaProductosEntrada) {
					// SOLO SI NO HAY CANTIDAD PENDIENTE
					if (md.getCantidad_auxiliar1() != 0) {
						d = new MovimientosDetalle();
						d.setId(0);
						d.setIdAlmacenMovimiento(0);
						d.setIdProducto(md.getProducto().getId());
						d.setCantidad(md.getCantidad());
						listaDetalle.add(d);
					}
				}
				
				// BACKORDER: Parametros
				idTarget = 0L;
				if (this.pojoMovimiento.getIdOrdenCompra() > 0L) { 
					idTarget = this.pojoMovimiento.getIdOrdenCompra();
					idReferencia = this.pojoMovimiento.getId();
				} else if (this.pojoMovimiento.getIdTraspaso() > 0) { 
					idTarget = this.pojoMovimiento.getId();
					// Actualizo los datos traspaso
					actualizarDetallesTraspaso(this.pojoMovimiento.getIdTraspaso(), listaDetalle);
				} else if (this.pojoMovimiento.getIdSalida() != null && this.pojoMovimiento.getIdSalida().getId() != null && this.pojoMovimiento.getIdSalida().getId() > 0) { 
					idTarget = this.pojoMovimiento.getId();
					// Actualizo los datos traspaso
					actualizarDetallesTraspaso(this.pojoMovimiento.getIdTraspaso(), listaDetalle);
				} 
			} 
			
			nuevaEntrada();
			this.band = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar registrar la Entrada", e);
			idTarget = 0L;
		}
		
		// Enviamos a Back Office Inventario
		try {
			if (idTarget > 0L && idReferencia > 0L)
				backOrderCompras(idTarget, idReferencia, listaDetalle);
			else if (idTarget > 0L && idReferencia <= 0L)
				backOfficeInventario(idTarget, listaDetalle);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar lanzar evento BackOffice de Inventario: " + TopicInventariosEventos.BackOfficeInventario.toString(), e);
		}
	}
	
	public void eliminar() {
		
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
				control(-1, "Ocurrio un problema al intentar obtener La Entrada seleccionada");
				return;
			}
			
			log.info("Imprimiendo Entrada ... ");
			formatter = new SimpleDateFormat("yyMMddHHss");
			
			// Parametros del reporte
			log.info("Generando parametros ... ");
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idMovimiento", this.pojoMovimiento.getId());

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.entrada"));
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
			nombreDocumento = "E-AL-" + this.pojoMovimiento.getId() + "-" + formatter.format(Calendar.getInstance().getTime());
			
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
			control("Ocurrio un problema al intentar imprimir la Entrada a Almacen", e);
		} 
	}
	
	public void eliminarProductosGrid() {
		this.listaProductosEntrada.clear();
		this.pojoOrdenCompra = null;
		this.pojoTraspaso = null;
		this.almacenAnterior = 0;
	}

	public void eliminarDetallesGrid() {
		this.cantidadDetalles = 0;
		this.almacenAnterior = 0;
	}

	private boolean subProcesoTraspaso(boolean autoTraspaso) {
		Almacen almacen = null;
		Almacen bodega = null;
		Empleado almEncargado = null;
		Empleado bodEncargado = null;
		boolean no_data = false;
		//-------------------
		List<MovimientosDetalle> listaDetalle = null;
		MovimientosDetalle d = null;
		long idTarget = 0L;
		long idReferencia = 0L;
		//-----------------------
		AlmacenTraspasoExt pTraspaso = null;
		TraspasoDetalleExt trasDetalle = null;
		List<TraspasoDetalleExt> listTraspasoDetalles = null;
		
		try {
			control();
			if (autoTraspaso) {
				// Origen
				almacen = recuperaAlmacen(this.pojoOrdenCompra.getIdObra()); //this.pojoMovimiento.getAlmacen();
				almEncargado = this.pojoMovimiento.getRecibe();//this.ifzEmpleado.findById(almacen.getIdEncargado());
				
				// destino
				bodega  = recuperaBodega(this.pojoOrdenCompra.getIdObra());
				bodEncargado = this.ifzEmpleado.findById(bodega.getIdEncargado());
			} else {
				// Origen
				almacen = recuperaAlmacen(this.pojoOrdenCompra.getIdObra()); //this.pojoMovimiento.getAlmacen();
				almEncargado = this.ifzEmpleado.findById(almacen.getIdEncargado());
				
				// destino
				bodega = this.pojoMovimiento.getAlmacen();
				bodEncargado = this.pojoMovimiento.getRecibe();
			}

			// ------------------------------------------------------------------------------------------------------
			// Entrada por Orden de Compra a Almacen Principal
			// ------------------------------------------------------------------------------------------------------
			
			this.pojoMovimiento.setId(0L);
			this.pojoMovimiento.setTipoEntrada(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_ALMACEN.toString()); //this.E_ALM_OC);
			this.pojoMovimiento.setAlmacen(almacen);
			this.pojoMovimiento.setRecibe(almEncargado);
			this.pojoMovimiento.setSistema((autoTraspaso ? 0 : 1));
			this.pojoMovimiento.setCreadoPor(this.usuarioId);
			this.pojoMovimiento.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoMovimiento.setModificadoPor(this.usuarioId);
			this.pojoMovimiento.setFechaModificacion(Calendar.getInstance().getTime());

			// Guardo movimiento
			this.pojoMovimiento.setId(this.ifzMovimientos.save(this.pojoMovimiento));
			
			// Guardo detalles
			log.info("Guardando detalles ... ");
			for (MovimientosDetalleExt md : this.listaProductosEntrada) {
				md.setIdAlmacenMovimiento(this.pojoMovimiento.getId());
				md.setModificadoPor(this.usuarioId);
				md.setFechaModificacion(Calendar.getInstance().getTime());
				md.setCreadoPor(this.usuarioId);
				md.setFechaCreacion(Calendar.getInstance().getTime());
			
				//Solo si se puede actualizar/guardar esta cantidadAuxiliar del movimiento y si la cantidad no es cero
				if (md.getCantidad_auxiliar1() != 0 && md.getCantidad() > 0)
					this.ifzMovimientosDetalle.save(md);
			}
			
			// Genero los atributos para el BACKORDER
			listaDetalle = new ArrayList<MovimientosDetalle>();
			for (MovimientosDetalleExt md : this.listaProductosEntrada) {
				// SOLO SI NO HAY CANTIDAD PENDIENTE
				if (md.getCantidad_auxiliar1() != 0) {
					d = new MovimientosDetalle();
					d.setId(0);
					d.setIdAlmacenMovimiento(0);
					d.setIdProducto(md.getProducto().getId());
					d.setCantidad(md.getCantidad());
					listaDetalle.add(d);
				}
			}
			
			// BACKORDER COMPRAS: Parametros
			idTarget = this.pojoMovimiento.getIdOrdenCompra();
			idReferencia = this.pojoMovimiento.getId();
			backOrderCompras(idTarget, idReferencia, listaDetalle);
			
			// ------------------------------------------------------------------------------------------------------
			// Traspaso de Almacen Principal a Bodega
			// ------------------------------------------------------------------------------------------------------
			
			pTraspaso = new AlmacenTraspasoExt();
			pTraspaso.setTipo(TipoTraspaso.TraspasoAlmacen.ordinal());
			pTraspaso.setFecha(Calendar.getInstance().getTime());
			pTraspaso.setAlmacenOrigen(almacen);
			pTraspaso.setEntrega(almEncargado);
			pTraspaso.setAlmacenDestino(bodega);
			pTraspaso.setRecibe(bodEncargado);
			pTraspaso.setModificadoPor(this.usuarioId);
			pTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
			pTraspaso.setCreadoPor(this.usuarioId);
			pTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
			pTraspaso.setSistema(1);
			
			listTraspasoDetalles = new ArrayList<TraspasoDetalleExt>();
			for (MovimientosDetalleExt md : this.listaProductosEntrada) {
				trasDetalle = new TraspasoDetalleExt();
				trasDetalle.setIdProducto(md.getProducto());
				trasDetalle.setCantidad(md.getCantidad());
				trasDetalle.setCantidadRecibida(md.getCantidad());
				trasDetalle.setEstatus(1);	//en transito
				trasDetalle.setModificadoPor(this.usuarioId);
				trasDetalle.setFechaModificacion(Calendar.getInstance().getTime());
				trasDetalle.setCreadoPor(this.usuarioId);
				trasDetalle.setFechaCreacion(Calendar.getInstance().getTime());

				listTraspasoDetalles.add(trasDetalle);
			}

			// Guardamos el Traspaso y sus detalles
			pTraspaso.setId(this.ifzTraspasos.save(pTraspaso));
			for (TraspasoDetalleExt item : listTraspasoDetalles) {
				item.setIdAlmacenTraspaso(pTraspaso.getId());
				item.setId(this.ifzTraspasosDetalle.save(item));
			}
			
			// Generamos movimiento de salida (salida por traspaso)
			generaSalida(pTraspaso, listTraspasoDetalles);

			// ------------------------------------------------------------------------------------------------------
			// Entrada por Traspaso (Almacen Principal a Bodega)
			// ------------------------------------------------------------------------------------------------------
			
			this.pojoMovimiento.setId(0L);
			this.pojoMovimiento.setTipoEntrada(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString()); //this.E_BOD_TR);
			this.pojoMovimiento.setFolioFactura("");
			this.pojoMovimiento.setAlmacen(bodega);
			this.pojoMovimiento.setIdObra(0L);
			this.pojoMovimiento.setIdOrdenCompra(0L);
			this.pojoMovimiento.setIdTraspaso(pTraspaso.getId());
			this.pojoMovimiento.setSistema(0);
			this.pojoMovimiento.setCreadoPor(this.usuarioId);
			this.pojoMovimiento.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoMovimiento.setModificadoPor(this.usuarioId);
			this.pojoMovimiento.setFechaModificacion(Calendar.getInstance().getTime());
			
			// Guardo detalles
			log.info("Guardando detalles ... ");
			for (MovimientosDetalleExt md : this.listaProductosEntrada) {
				md.setIdAlmacenMovimiento(0L);
				md.setId(0L);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar registrar la Entrada.\n\nFallo el proceso automatizado interno para recibir los Productos" + (no_data ? e.getMessage() : ""), (! no_data ? e : null));
			return false;
		}
		
		return true;
	}

	private Almacen recuperaAlmacen(long idObra) {
		ObraAlmacenes resultado = null;
		Almacen almacen = null;
		
		try {
			resultado = this.ifzObrasAlmacenes.findAlmacenPrincipal(idObra, 0L);
			if (resultado != null && resultado.getId() != null && resultado.getId() > 0L)
				almacen = this.ifzAlmacen.findById(resultado.getIdAlmacen());
		} catch (Exception e) {
			control("Ocurrio un problema al intentar determinar el Almacen origen para el Traspaso", e);
		}
		
		return almacen;
	}
	
	private Almacen recuperaBodega(long idObra) {
		ObraAlmacenes resultado = null;
		Almacen bodega = null;
		
		try {
			resultado = this.ifzObrasAlmacenes.findBodega(idObra);
			if (resultado != null && resultado.getId() != null && resultado.getId() > 0L)
				bodega = this.ifzAlmacen.findById(resultado.getIdAlmacen());
		} catch (Exception e) {
			control("Ocurrio un problema al intentar determinar la Bodega destino para el Traspaso", e);
		}
		
		return bodega;
	}

	private boolean generaSalida(AlmacenTraspasoExt pTraspaso, List<TraspasoDetalleExt> detalles) {
		AlmacenMovimientos movimiento = null;
		List<MovimientosDetalle> movDetalles = null;
		MovimientosDetalle md = null;
		
		try {
			control();
			// Genero movimiento de SALIDA
			movimiento = new AlmacenMovimientos();
			movimiento.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
			movimiento.setTipoEntrada(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString());
			movimiento.setIdOrdenCompra(0L);
			movimiento.setIdTraspaso(pTraspaso.getId());
			movimiento.setIdAlmacen(pTraspaso.getAlmacenOrigen().getId());
			movimiento.setFecha(pTraspaso.getFecha());
			movimiento.setEntrega(pTraspaso.getEntrega().getId());
			movimiento.setRecibe(pTraspaso.getRecibe().getId());
			movimiento.setCreadoPor(pTraspaso.getCreadoPor());
			movimiento.setFechaCreacion(pTraspaso.getFechaCreacion());
			movimiento.setModificadoPor(pTraspaso.getModificadoPor());
			movimiento.setFechaModificacion(pTraspaso.getFechaModificacion());
			
			// Genero detalles de salida
			log.info("Movimiento de Salida guardado. Guardando detalle de movimiento ...");
			for (TraspasoDetalleExt td : detalles) {
				md = new MovimientosDetalle();
				md.setIdProducto(td.getIdProducto().getId());
				md.setCantidad(td.getCantidad());
				md.setCantidadSolicitada(td.getCantidad());
				md.setEstatus(0);
				md.setCreadoPor(td.getCreadoPor());
				md.setFechaCreacion(td.getFechaCreacion());
				md.setModificadoPor(td.getModificadoPor());
				md.setFechaModificacion(td.getFechaModificacion());
				
				if (movDetalles == null)
					movDetalles = new ArrayList<MovimientosDetalle>();
				movDetalles.add(md);
			}

			// Guardo entrada (movimiento) y detalles
			log.info("Guardando Movimiento y detalles ...");
			movimiento.setId(this.ifzMovimientos.save(movimiento));
			for (MovimientosDetalle item : movDetalles)
				item.setIdAlmacenMovimiento(movimiento.getId());
			movDetalles = this.ifzMovimientosDetalle.saveOrUpdateList(movDetalles);
			log.info("Detalles de Movimiento guardados. Proceso finalizado.");
		} catch (Exception e) {
			control("Ocurrio un problema al generar la SALIDA por TRASPASO", e);
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

	private void backOrderCompras(Long idOrdenCompra, Long idMovimiento, List<MovimientosDetalle> listDetalles) throws JMSException, NamingException {
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
			target = idOrdenCompra.toString();
			referencia = idMovimiento.toString();
			atributos = gson.toJson(listDetalles);
			
			// Generamos mensaje para BACKORDER	COMPRAS
			msgInventario = new MensajeTopic(TopicInventariosEventos.BackOrderCompras, target, referencia, atributos);
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
	
	private void actualizarDetallesTraspaso(long idTraspaso, List<MovimientosDetalle> listaDetalle) {
		List<TraspasoDetalleExt> lista = null;
		AlmacenTraspaso traspaso = null;
		double cantidad = 0;
		
		try {
			lista = this.ifzTraspasosDetalle.findExtDetallesByIdTraspaso(idTraspaso);
			if (lista == null || lista.isEmpty())
				return;
			
			for (TraspasoDetalleExt var : lista) {
				cantidad = getCantidadTraspaso(var.getIdProducto().getId(), listaDetalle);
				if (cantidad <= 0) 
					continue;
				
				try {
					var.setCantidadRecibida(var.getCantidadRecibida() + cantidad);
					if (var.getCantidad() == var.getCantidadRecibida())
						var.setEstatus(2); // estatus ?  1 transito : 2 completado
					this.ifzTraspasosDetalle.update(var);
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
				traspaso = this.ifzTraspasos.findById(idTraspaso);
				traspaso.setCompleto(1);
				this.ifzTraspasos.update(traspaso);
			} catch (Exception e) {
				log.error("Ocurrio un problema al intentar actualizar el estatus del Traspaso", e);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar actualizar los detalles del Traspaso", e);
		}
	}
	
	private double getCantidadTraspaso(long idProducto, List<MovimientosDetalle> listaDetalle) {
		for (MovimientosDetalle var : listaDetalle) {
			if (idProducto == var.getIdProducto())
				return var.getCantidad();
		}
		
		return 0;
	}
	
	private boolean validaGuardarMovimiento() {
		control();
		if (this.pojoMovimiento.getAlmacen() == null) {
			control(-10, "Seleccione almacén");
			return false;
		}
		
		if (this.pojoMovimiento.getRecibe() == null) {
			control(-11, "Indique el empleado que recibe");
			return false;
		}
		
		if (this.pojoMovimiento.getIdOrdenCompra() > 0) {
			if (this.pojoMovimiento.getFolioFactura() == null && "".equals(this.pojoMovimiento.getFolioFactura().trim())) {
				control(-12, "Debe ingresar el folio de la Factura");
				return false;
			}
		}

		if (this.listaProductosEntrada.isEmpty()) {
			control(-13, "Debe existir por lo menos un producto en la lista");
			return false;
		}
		
		if (! this.sePuedeGuardarMovimiento) {
			control(-13, "Esta orden de compra se encuentra completa");
			return false;
		}
		
		//Revisar si alguno se encuentra en cero o si la cantidad recibida es mayor a la solicitada
		
		//revisar que la cantidad que estan registrando, mas la cantidad que ya se entregó, no supere a la solicitada
		for (MovimientosDetalleExt md : this.listaProductosEntrada) {
			log.info("md.getCantidad(): " + md.getCantidad());
			log.info("md.getCantidadSolicitada(): " + md.getCantidadSolicitada());
			if (md.getCantidad() > md.getCantidadSolicitada()) {
				control(-1, "La cantidad recibida no puede ser mayor a la solicitada");
				return false;
			}
		}
		
		return true;
	}
	
	private boolean comprobarUsuario() {
		List<Empleado> listEmpleado = null;
		Long idEmpleado = null;
		List<Almacen> listAux = null;
		Long idPuesto = null;
		boolean puestoValido = false;
		String msgLog = "";
		String msgLogAux = "";
		
		try {
			this.idSucursalBase = 0L;
			if (this.loginManager == null)
				return false;
			
			if ("ADMINISTRADOR".equals(this.usuario)) {
				this.nivelAlmacenes = 3;
				return true;
			}
			
			// Determinamos EMPLEADO de manera directa o por correo electronico
			// ----------------------------------------------------------------------------------
			msgLog += "Comprobando Empleado por Usuario ... ";
			idEmpleado = this.loginManager.getUsuarioResponsabilidad().getUsuario().getIdEmpleado();
			if (idEmpleado == null || idEmpleado <= 1L) {
				msgLog += "FAIL\nComprobando Empleado por Email: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo() + " ... ";
				listEmpleado = this.ifzEmpleado.findByProperty("email", this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo(), 0);
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
			
			// Recuperamos SUCURSAL base del Empleado
			// ----------------------------------------------------------------------------------
			msgLog += "\nRecuperando Sucursal ... ";
			this.idSucursalBase = this.pojoEmpleadoUsuario.getSucursal().getId();
			msgLog += "OK";
			
			// Recuperamos ALMACEN/BODEGA base del Empleado
			// ----------------------------------------------------------------------------------
			msgLog += "\nRecuperando asignaciones de Almacenes/Bodegas del Empleado ... ";
			this.listEmpleadoAlmacenes = this.ifzAlmacen.findByProperty("idEncargado", this.pojoEmpleadoUsuario.getId());
			if (this.listEmpleadoAlmacenes == null|| this.listEmpleadoAlmacenes.isEmpty()) {
				// Tiene el Puesto necesario pero aun no ha sido asignado a ningun Almacen/Bodega.
				// Entonces, recupero los Almacenes/Bodegas de la Sucursal asignada al Empleado
				msgLog += "FAIL\nRecuperando Almacenes/Bodegas por Sucursal " + this.idSucursalBase + " ... ";
				this.listEmpleadoAlmacenes = this.ifzAlmacen.findByProperty("idSucursal", this.idSucursalBase);
				if (this.listEmpleadoAlmacenes != null && ! this.listEmpleadoAlmacenes.isEmpty()) {
					listAux = new ArrayList<Almacen>();
					for (Almacen var : this.listEmpleadoAlmacenes) {
						// Excluimos Administrativos si no tiene el perfil
						if (var.getTipo() > 2 && ! this.perfilAdministrativo)
							continue;
						listAux.add(var);
					}
					
					this.listEmpleadoAlmacenes.clear();
					this.listEmpleadoAlmacenes.addAll(listAux);
					listAux.clear();
				} else {
					msgLog += "FAIL\n --> Sin Almacenes/Bodegas para Empleado <--";
					this.listEmpleadoAlmacenes = new ArrayList<Almacen>();
				}
			}

			msgLog += "OK\nDeterminando nivel de Entrada para Almacenes/Bodegas para Empleado ... ";
			this.nivelAlmacenes = 0; // 1:almacen, 2:bodega, 3:ambos
			for (Almacen var : this.listEmpleadoAlmacenes) {
				// Si es Almacen: 1-normal, 3-administrativa
				if (var.getTipo() == 1 || var.getTipo() == 3) 
					this.nivelAlmacenes = (this.nivelAlmacenes == 2 || this.nivelAlmacenes == 3) ? 3 : 1;
				
				// Si es Bodega: 2-normal, 4-administrativa
				if (var.getTipo() == 2 || var.getTipo() == 4) 
					this.nivelAlmacenes = (this.nivelAlmacenes == 1 || this.nivelAlmacenes == 3) ? 3 : 2;
				msgLogAux += "\n		" + var.getId() + " - " + var.getNombre() + " Tipo " + var.getTipo() + " Nivel " + this.nivelAlmacenes;
			}
			msgLog += "OK " + msgLogAux + "\n	Nivel Obtenido : " + this.nivelAlmacenes;

			msgLog += "\nComprobando Puestos base ... ";
			if (this.listPuestosValidos != null && ! this.listPuestosValidos.isEmpty()) {
				// Comprobamos PUESTO
				// ----------------------------------------------------------------------------------
				msgLog += "OK\nRecuperando Puesto de Empleado ... ";
				if (this.pojoEmpleadoUsuario.getPuestoCategoria() != null 
						&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto() != null 
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
			} else {
				msgLog += "FAIL\n --> Sin Puestos base para comprobar <--";
			}
			
			msgLog += "\n\nEmpleado OK!";
		} catch (Exception e) {
			log.error("Ocurrio un problema en Inventarios.EntradasAlmacenAction.comprobarUsuario()", e);
			return false;
		} finally {
			log.info(" \n---------------------------------------------------------" 
					+ "\nCOMPROBACION DE USUARIO\n---------------------------------------------------------" 
					+ "\n" + msgLog 
					+ "\n---------------------------------------------------------");
		}
		
		return true;
	}
	
	public void regresarAlmacen() {
		log.info("Almacen anterior:");
	}
	
	public void validaCantidadProducto() {
		if (! this.puedeEditar) 
			return;	//si no puede editar, quiere decir que se esta observando los detalles del movimiento, por lo tanto no hay necesidad de hacer validaciones
		
		if ("SO".equals(this.tipoEntrada))
			return;
	}

	public void validaTipoEntrada() {
		if (this.pojoMovimiento == null)
			return;

		this.almacenesAdministrativos = false;
		this.pojoOrdenCompra = null;
		this.pojoTraspaso = null;
		this.pojoSalida = null;
		
		this.pojoMovimiento.setIdSalida(null);
		this.pojoMovimiento.setIdTraspaso(0L);
		this.pojoMovimiento.setIdOrdenCompra(0L);
		if (this.listaProductosEntrada == null)
			this.listaProductosEntrada = new ArrayList<MovimientosDetalleExt>();
		this.listaProductosEntrada.clear();

		// Asigno usuario recibe: sugerencia
		if (this.pojoEmpleadoUsuario != null)
			this.pojoMovimiento.setRecibe(this.ifzEmpleado.findById(this.pojoEmpleadoUsuario.getId()));
	}

	public void filtrarTipoEntrada() {
		if (this.listTiposEntradas == null)
			this.listTiposEntradas = new ArrayList<SelectItem>();
		this.listTiposEntradas.clear();
		
		switch (this.targetAlmacen) {
			case 1: // 1:almacen
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_ALMACEN.toString(),"Orden de Compra"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString(),"Traspaso"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString(),"Devolucion"));
				break;
			case 2: // 2:bodega
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_BODEGA.toString(),"Orden de Compra"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString(),"Traspaso"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_OBRA_BODEGA.toString(),"Devolucion"));
				break;
			default: // 3:ambos
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_ALMACEN.toString(),"Orden de Compra a Almacen"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.ORDEN_COMPRA_BODEGA.toString(),"Orden de Compra a Bodega"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_ALMACEN.toString(),"Traspaso de Almacen a Almacen"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.toString(),"Traspaso de Almacen a Bodega"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_BODEGA_ALMACEN.toString(),"Devolucion de Bodega a Almacen"));
				this.listTiposEntradas.add(new SelectItem(TipoMovimientoReferenciaExtendida.DEVOLUCION_OBRA_BODEGA.toString(),"Devolucion de Obra a Bodega"));
				break;
		}
		this.tipoEntrada = this.listTiposEntradas.get(0).getValue().toString();
	}
	
	private void control() {
		this.band = false;
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
	
	private void control(boolean band, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

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
		
		log.error("\n\nENTRADAS_ALMACEN :: " + this.usuario + " :: " + codigo + "\n" + mensaje + "\n", throwable);
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
		
		cargarCboEmpleadoRecibe();
	}

	public void cargarCboEmpleadoRecibe() {
		if (this.listaCboEmpleadoRecibe == null)
			this.listaCboEmpleadoRecibe = new ArrayList<SelectItem>();
		this.listaCboEmpleadoRecibe.clear();
		
		for (Empleado e : this.listaEmpleados) {
			this.listaCboEmpleadoRecibe.add(new SelectItem(e.getId(), e.getNombre()));
			
			// Asignamos sugerencia de Empleado si corresponde			
			if (this.pojoMovimiento == null || this.pojoEmpleadoUsuario == null)
				continue;
			if (e.getId().longValue() ==  this.pojoEmpleadoUsuario.getId().longValue())
				this.pojoMovimiento.setRecibe(e);
		}
	}
	
	// ------------------------------------------------------------------------------------
	// ALMACENES
	// ------------------------------------------------------------------------------------
	
	private void cargarAlmacenes() {
		if (this.listaAlmacenesFull == null)
			this.listaAlmacenesFull = new ArrayList<Almacen>();
		this.listaAlmacenesFull.clear();
		
		this.listaAlmacenesFull = this.ifzAlmacen.findAllActivos();
		if (this.listaAlmacenesFull != null && ! this.listaAlmacenesFull.isEmpty()) {
			Collections.sort(this.listaAlmacenesFull, new Comparator<Almacen>() {
				@Override
				public int compare(Almacen o1, Almacen o2) {
					return o1.getNombre().compareTo(o2.getNombre());
				}
			});
		}
		
		filtrarAlmacenes();
	}
	
	public void filtrarAlmacenes() {
		if (this.listaAlmacenes == null)
			this.listaAlmacenes = new ArrayList<Almacen>();
		this.listaAlmacenes.clear();
		
		if (this.listaCboAlmacenes == null)
			this.listaCboAlmacenes = new ArrayList<SelectItem>();
		this.listaCboAlmacenes.clear();
		
		// Filtramos los Almacenes/Bodegas de acuerdo a la opcion de entrada seleccioanda: OC (Orden de Compra) por default
		for (Almacen var : this.listaAlmacenesFull) {
			switch (this.tipoEntrada) {
				case "OC": case "TX": case "DE": // Splo Almacenes
					if (this.almacenesAdministrativos && var.getTipo() == 3)
						this.listaAlmacenes.add(var);
					else if (! this.almacenesAdministrativos && var.getTipo() == 1)
						this.listaAlmacenes.add(var);
					break;
				case "CB": case "TR": case "SO": // Solo Bodegas
					if (this.almacenesAdministrativos && var.getTipo() == 4)
						this.listaAlmacenes.add(var);
					else if (! this.almacenesAdministrativos && var.getTipo() == 2)
						this.listaAlmacenes.add(var);
					break;
				default:
					this.listaAlmacenes.add(var);
					break;
			}
		}
		
		for (Almacen a : this.listaAlmacenes) {
			this.listaCboAlmacenes.add(new SelectItem(a.getId(), a.getNombre()));

			// Asignamos sugerencia de Almacen si corresponde			
			if (this.pojoMovimiento == null || (this.listEmpleadoAlmacenes == null || this.listEmpleadoAlmacenes.isEmpty()))
				continue;
			
			switch (this.tipoEntrada) {
				case "OC": case "TX": case "DE": // ORDEN DE COMPRA A ALMACEN, TRASPASO DE ALMACEN A ALMACEN o DEVOLUCION DE BODEGA
					for (Almacen var : this.listEmpleadoAlmacenes) {
						if (this.almacenesAdministrativos && var.getTipo() == 3) {
							this.pojoMovimiento.setAlmacen(a);
							setIdEmpleadoRecibe(a.getIdEncargado());
							break;
						} else if (! this.almacenesAdministrativos && var.getTipo() == 1) {
							this.pojoMovimiento.setAlmacen(a);
							setIdEmpleadoRecibe(a.getIdEncargado());
							break;
						}
					}
					break;
				case "CB": case "TR": case "SO": // ORDEN DE COMRA A BODEGA, TRASPASO DE ALMACEN A BODEGA o DEVOLUCION DE OBRA
					for (Almacen var : this.listEmpleadoAlmacenes) {
						if (this.almacenesAdministrativos && var.getTipo() == 4) {
							this.pojoMovimiento.setAlmacen(a);
							break;
						} else if (! this.almacenesAdministrativos && var.getTipo() == 2) {
							this.pojoMovimiento.setAlmacen(a);
							break;
						}
					}
					break;
				default:
					// do nothing
					break;
			}
		}
		
		// Reiniciamos productos
		if (this.listaProductosEntrada == null)
			this.listaProductosEntrada = new ArrayList<MovimientosDetalleExt>();
		this.listaProductosEntrada.clear();
	}

	// ------------------------------------------------------------------------------------
	// BUSQUEDA OBRAS
	// ------------------------------------------------------------------------------------

	public void nuevaBusquedaObras() {
		
	}
	
	public void buscarObras() {
		
    }

	public void seleccionarObra() {
		
	}

	// ------------------------------------------------------------------------------------
	// BUSQUEDA ORDEN DE COMPRA
	// ------------------------------------------------------------------------------------
	
	public void nuevaBusquedaOrdenCompra() {
		if (this.listaOrdenesCompra == null)
			this.listaOrdenesCompra = new ArrayList<OrdenCompra>();
		this.listaOrdenesCompra.clear();

		this.campoBusquedaOC = this.tiposBusquedaOC.get(0).getValue().toString();
		this.valorBusquedaOC = "";
		this.numPaginaOrdenCompras = 1;
		this.avanzadaBusquedaOC = false;
	}

	public void buscarOrdenesCompra() {
		List<OrdenCompra> listAuxiliar = null;
		
		try {
			control();
			if (this.campoBusquedaOC == null || "".equals(this.campoBusquedaOC.trim()))
				this.campoBusquedaOC = this.tiposBusquedaOC.get(0).getValue().toString();
			
			if (this.listaOrdenesCompra != null)
				this.listaOrdenesCompra.clear();
			
			listAuxiliar = this.ifzOrdenCompra.findNoCompletas(this.campoBusquedaOC, this.valorBusquedaOC, 0);
			if (listAuxiliar == null || listAuxiliar.isEmpty()) {
				// Buscamos con el valor de parametro exacto para descartar estatus de Orden de Compra
				listAuxiliar = this.ifzOrdenCompra.findByProperty(this.campoBusquedaOC, this.valorBusquedaOC, 0);
				if (listAuxiliar != null && ! listAuxiliar.isEmpty() && listAuxiliar.size() == 1) {
					if (this.listaOrdenesCompra.get(0).getEstatus() == 2)
						control(5, "ERROR: Orden de Compra Suministrada");
					if (this.listaOrdenesCompra.get(0).getEstatus() == 1)
						control(6, "ERROR: Orden de Compra Eliminada");
					listAuxiliar.clear();
					return;
				}
			}
			
			for (OrdenCompra item : listAuxiliar) {
				if (! this.perfilAdministrativo && item.getTipo() == 2)
					continue;
				this.listaOrdenesCompra.add(item);
			}

			if (this.listaOrdenesCompra == null || this.listaOrdenesCompra.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Ordenes de Compra", e);
    	} finally {
			this.numPaginaOrdenCompras = 1;
    		if (this.listaOrdenesCompra == null || this.listaOrdenesCompra.isEmpty())
				log.info("ERROR 2 - Busqueda sin resultados | ENTRADAS - ORDENES DE COMPRA");
    	}
	}
	
	public void seleccionarOrdenCompra() {
		List<OrdenCompraDetalleExt> listaOrdenCompraDetalle;
		MovimientosDetalleExt m = null;
		
		try {
			//Se tiene asigna el id de orden de compra a cero, para que no exista confusión en futuras validaciones
			if (this.pojoOrdenCompra.getId() == null) 
				return;
			
			if (this.listaProductosEntrada == null)
				this.listaProductosEntrada = new ArrayList<MovimientosDetalleExt>();
			this.listaProductosEntrada.clear();
			
			// Filtramos Almacenes y/o Bodegas
			this.almacenesAdministrativos = (this.pojoOrdenCompra.getTipo() == 2);
			cargarAlmacenes();

			this.pojoMovimiento.setIdOrdenCompra(this.pojoOrdenCompra.getId());
			this.pojoMovimiento.setIdTraspaso(0L);
			this.pojoMovimiento.setIdSalida(null);
			this.sePuedeGuardarMovimiento = false;
			
			// Recuperamos los productos de la Orden de Compra
			listaOrdenCompraDetalle = this.ifzOrdenCompraDetalle.findExtByProperty("idOrdenCompra", this.pojoOrdenCompra.getId(), 50);
			if (listaOrdenCompraDetalle != null && ! listaOrdenCompraDetalle.isEmpty()) {
				for (OrdenCompraDetalleExt var : listaOrdenCompraDetalle) {
					m = new MovimientosDetalleExt();
					m.setProducto(var.getIdProducto());
					//cantidadAuxiliar fungirá como la cantidad que ya esta entregada y registrada
					m.setCantidad_auxiliar1(var.getCantidad() - var.getCantidadRecibida());
					log.info("Cantidad pendiente: "+ m.getCantidad_auxiliar1() + ", ID: "+var.getIdProducto());
					//asignamos al campo cantidad-solicitada el mismo valor que tiene cantidad
					m.setCantidadSolicitada(var.getCantidad());
					m.setCantidad(var.getCantidad() - var.getCantidadRecibida());
					//Si la cantidad pendiente en alguno de los movimientos es mayor a cero, significa que se puede editar, por lo que se puede guardar
					if (m.getCantidad_auxiliar1() > 0)
						this.sePuedeGuardarMovimiento = true;
					
					this.listaProductosEntrada.add(m);
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los productos de la Orden de Compra seleccionada", e);
		}
	}
	
	public void toggleAvanzadaBusquedaOC() {
		this.avanzadaBusquedaOC = ! this.avanzadaBusquedaOC;
	}

	// ------------------------------------------------------------------------------------
	// BUSQUEDA TRASPASOS
	// ------------------------------------------------------------------------------------

	public void nuevaBusquedaTraspaso() {
		control();
		if (this.listaTraspasos == null)
			this.listaTraspasos = new ArrayList<AlmacenTraspasoExt>();
		this.listaTraspasos.clear();
		
		this.campoBusquedaTraspasos = this.tiposBusquedaTraspasos.get(0).getValue().toString();
		this.valorBusquedaTraspasos = "";
		this.numPaginaTraspasos = 1;
		this.avanzadaBusquedaTraspaso = false;

		this.puedeBuscarTraspaso = true;
	}
	
	public void buscarTraspasos() {
		List<AlmacenTraspasoExt> listAuxiliar = null;
		
		try {
			control();
			if (this.listaTraspasos != null)
				this.listaTraspasos.clear();
			
			listAuxiliar = this.ifzTraspasos.findExtIncompletosLikeProperty(this.campoBusquedaTraspasos, this.valorBusquedaTraspasos, "model.fecha desc, model.id desc", 0);
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
			if (this.listaTraspasos == null)
				this.listaTraspasos = new ArrayList<AlmacenTraspasoExt>();
			this.numPaginaTraspasos = 1;
    	}
	}

	public void seleccionarTraspaso() {
		List<TraspasoDetalleExt> listaTraspasoDetalle;
		MovimientosDetalleExt m = null;

		try {
			control();
			log.info("Obteniendo traspaso a detalles");
			
			//Se tiene asigna el id de orden de compra a cero, para que no exista confusión en futuras validaciones
			if (this.pojoTraspaso.getId() == null) 
				return;
			
			if (this.listaProductosEntrada == null)
				this.listaProductosEntrada = new ArrayList<MovimientosDetalleExt>(); // La lista del traspaso que se mostrará
			this.listaProductosEntrada.clear();
			
			this.almacenesAdministrativos = (this.pojoTraspaso.getAlmacenDestino().getTipo() > 2);
			cargarAlmacenes();
	
			this.pojoMovimiento.setIdOrdenCompra(0L);
			this.pojoMovimiento.setIdTraspaso(this.pojoTraspaso.getId());
			this.pojoMovimiento.setIdSalida(null);
			this.pojoMovimiento.setAlmacen(this.pojoTraspaso.getAlmacenDestino());
			this.pojoMovimiento.setRecibe(this.pojoTraspaso.getRecibe());
			this.sePuedeGuardarMovimiento = false;
			
			listaTraspasoDetalle = this.ifzTraspasosDetalle.findExtDetallesByIdTraspaso(this.pojoTraspaso.getId());
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
					if (m.getCantidad_auxiliar1() > 0)
						this.sePuedeGuardarMovimiento = true;
					
					this.listaProductosEntrada.add(m);
				}
			}
			
			//Asignar por primera vez el almacen
			if (this.almacenAnterior == 0) 
				this.almacenAnterior = this.pojoMovimiento.getAlmacen().getId();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los productos del Traspaso seleccionado", e);
		}
	}
	
	public void toggleAvanzadaBusquedaTraspaso() {
		this.avanzadaBusquedaTraspaso = ! this.avanzadaBusquedaTraspaso;
	}
	
	// ------------------------------------------------------------------------------------
	// BUSQUEDA SALIDAS
	// ------------------------------------------------------------------------------------
	
	public void nuevaBusquedaSalida() {
		control();
		if (this.listSalidaBusqueda == null)
			this.listSalidaBusqueda = new ArrayList<AlmacenMovimientosExt>();
		this.listSalidaBusqueda.clear();
		this.pojoSalida = null;
		
		this.salidaBusquedaCampo = this.salidaBusquedaTipos.get(0).getValue().toString();
		this.salidaBusquedaValor = "";
		this.salidaBusquedaPaginas = 1;
		this.avanzadaBusquedaSalida = false;
	}
	
	public void buscarSalida() {
		List<AlmacenMovimientosExt> listAuxiliar = null;
		
		try {
			control();
			if (this.salidaBusquedaCampo == null || "".equals(this.salidaBusquedaCampo))
				this.salidaBusquedaCampo = this.salidaBusquedaTipos.get(0).getValue().toString();
			
			if (this.listSalidaBusqueda != null)
				this.listSalidaBusqueda.clear();
			this.salidaBusquedaPaginas = 1;
			
			listAuxiliar = this.ifzMovimientos.findExtLikeProperty(this.salidaBusquedaCampo, this.salidaBusquedaValor, 1, "SO", 0);
			if (listAuxiliar == null || listAuxiliar.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
			
			for (AlmacenMovimientosExt item : listAuxiliar) {
				if (! this.perfilAdministrativo && item.getAlmacen().getTipo() > 2)
					continue;
				this.listSalidaBusqueda.add(item);
			}
			
			Collections.sort(this.listSalidaBusqueda, new Comparator<AlmacenMovimientosExt>() {
				@Override
				public int compare(AlmacenMovimientosExt o1, AlmacenMovimientosExt o2) {
					return o2.getFecha().compareTo(o1.getFecha());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Salidas de la Bodega", e);
    	} finally {
			if (this.listSalidaBusqueda == null)
				this.listSalidaBusqueda = new ArrayList<AlmacenMovimientosExt>();
			this.salidaBusquedaPaginas = 1;
    	}
	}
	
	public void seleccionarSalida() {
		List<MovimientosDetalleExt> listSalidaDetalle = null;
		MovimientosDetalleExt m = null;
		
		try {
			control();
			if (this.pojoSalida == null || this.pojoSalida.getId() == null || this.pojoSalida.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la SALIDA seleccionada");
				return;
			}
			
			this.almacenesAdministrativos = (this.pojoSalida.getAlmacen().getTipo() == 4);
			cargarAlmacenes();
			
			if (this.listaProductosEntrada == null)
				this.listaProductosEntrada = new ArrayList<MovimientosDetalleExt>(); // La lista del traspaso que se mostrará
			this.listaProductosEntrada.clear();

			this.sePuedeGuardarMovimiento = false;
			this.pojoMovimiento.setIdOrdenCompra(0L);
			this.pojoMovimiento.setIdTraspaso(0L);
			this.pojoMovimiento.setIdSalida(this.pojoSalida);
			this.pojoMovimiento.setAlmacen(this.pojoSalida.getAlmacen());
			this.pojoMovimiento.setRecibe(this.pojoSalida.getEntrega());
			this.pojoMovimiento.setEntrega(this.pojoSalida.getRecibe());
			
			// Recuperamos los detalles de la SALIDA
			listSalidaDetalle = this.ifzMovimientosDetalle.findDetallesExtById(this.pojoSalida.getId());
			if (listSalidaDetalle == null || listSalidaDetalle.isEmpty()) {
				control(-1, "La SALIDA seleccionada esta vacia");
				return;
			}
			
			// Generamos los detalles para la ENTRADA
			for (MovimientosDetalleExt var : listSalidaDetalle) {
				m = new MovimientosDetalleExt();
				m.setProducto(var.getProducto());
				m.setCantidad_auxiliar1(var.getCantidad());
				m.setCantidadSolicitada(var.getCantidad());
				m.setCantidad(var.getCantidad());

				this.listaProductosEntrada.add(m);
			}
			
			this.sePuedeGuardarMovimiento = true;
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los productos de la Orden de Compra seleccionada", e);
		}
	}

	public void toggleAvanzadaBusquedaSalida() {
		this.avanzadaBusquedaSalida = ! this.avanzadaBusquedaSalida;
	}
	
	// ------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------

	public String getTitulo() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getId() != null && this.pojoMovimiento.getId() > 0L) {
			if (this.pojoMovimiento.getAlmacen().getTipo() == 1)
				return "Entrada " + this.pojoMovimiento.getId() + " a Almacen";
			if (this.pojoMovimiento.getAlmacen().getTipo() == 2)
				return "Entrada " + this.pojoMovimiento.getId() + " a Bodega";
			if (this.pojoMovimiento.getAlmacen().getTipo() == 3)
				return "Entrada " + this.pojoMovimiento.getId() + " a Almacen ADMINISTRATIVO";
			if (this.pojoMovimiento.getAlmacen().getTipo() == 4)
				return "Entrada " + this.pojoMovimiento.getId() + " a Bodega ADMINISTRATIVO";
		}
		
		return "Nueva Entrada a " + (this.nivelAlmacenes == 1 ? "Almacen" : (this.nivelAlmacenes == 2 ? "Bodega" : "Almacen/Bodega"));
	}
	
	public void setTitulo(String value) {}
	
	public boolean getDesbloqueoGenerales() {
		boolean unlock = false;
		
		switch (this.tipoEntrada) {
			case "OC": case "CB" :
				unlock = (this.pojoOrdenCompra != null && this.pojoOrdenCompra.getId() != null && this.pojoOrdenCompra.getId() > 0L);
				break;
			case "TR": case "TX": case "TZ" : case "DE" :
				unlock = (this.pojoTraspaso != null && this.pojoTraspaso.getId() != null && this.pojoTraspaso.getId() > 0L);
				break;
			case "DX" :
				unlock = (this.pojoSalida != null && this.pojoSalida.getId() != null && this.pojoSalida.getId() > 0L);
				break;
		}
		
		return unlock;
	}
	
	public void setDesbloqueoGenerales(boolean value) {}
	
	public String getOrdenCompra() {
		if (this.pojoOrdenCompra != null && this.pojoOrdenCompra.getId() != null && this.pojoOrdenCompra.getId() > 0L)
			return this.pojoOrdenCompra.getId() + " | " + this.pojoOrdenCompra.getFolio() + " | " + this.pojoOrdenCompra.getNombreObra() + " | " + (new SimpleDateFormat("dd-MM-yyyy")).format(this.pojoOrdenCompra.getFecha());
		return "";
	}
	
	public void setOrdenCompra(String value) {}

	public String getTraspaso() {
		if (this.pojoTraspaso != null && this.pojoTraspaso.getId() != null && this.pojoTraspaso.getId() > 0L)
			return this.pojoTraspaso.getId() + " | " + this.pojoTraspaso.getAlmacenOrigen().getNombre() + " | " + (new SimpleDateFormat("dd-MM-yyyy")).format(this.pojoTraspaso.getFecha());
		return "";
	}
	
	public void setTraspaso(String value) {}

	public String getSalida() {
		if (this.pojoSalida != null && this.pojoSalida.getId() != null && this.pojoSalida.getId() > 0L)
			return this.pojoSalida.getId() + " | " + this.pojoSalida.getNombreObra() + " | " + (new SimpleDateFormat("dd-MM-yyyy")).format(this.pojoSalida.getFecha());
		return "";
	}
	
	public void setSalida(String value) {}
	
	public int getNumeroItems() {
		return this.listaProductosEntrada == null ? 0 : this.listaProductosEntrada.size();
	}
	
	public void setNumeroItems(int numeroItems) {
		
	}
	
	public AlmacenMovimientosExt getPojoMovimiento() {
		return pojoMovimiento;
	}

	public void setPojoMovimiento(AlmacenMovimientosExt pojoMovimiento) {
		this.pojoMovimiento = pojoMovimiento;
	}

	public long getIdAlmacen() {
		return this.pojoMovimiento == null || this.pojoMovimiento.getAlmacen() == null ? 0 : this.pojoMovimiento.getAlmacen().getId();
	}

	public void setIdAlmacen(long idAlmacen) {
		if (idAlmacen <= 0L)
			return;
		this.pojoMovimiento.setAlmacen(this.ifzAlmacen.findById(idAlmacen));
		setIdEmpleadoRecibe(this.pojoMovimiento.getAlmacen().getIdEncargado());
	}
	
	public long getIdEmpleadoRecibe() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getRecibe() != null && this.pojoMovimiento.getRecibe().getId() != null && this.pojoMovimiento.getRecibe().getId() > 0L)
			return this.pojoMovimiento.getRecibe().getId();
		return 0L;
	}

	public void setIdEmpleadoRecibe(long idEmpleado) {
		if (idEmpleado > 0)
			this.pojoMovimiento.setRecibe(this.ifzEmpleado.findById(idEmpleado));
	}
	
	public long getIdEmpleadoEntrega() {
		if (this.pojoMovimiento != null && this.pojoMovimiento.getEntrega() != null && this.pojoMovimiento.getEntrega().getId() != null && this.pojoMovimiento.getEntrega().getId() > 0L)
			return this.pojoMovimiento.getEntrega().getId();
		return 0L;
	}

	public void setIdEmpleadoEntrega(long idEmpleado) {
		if (idEmpleado > 0)
			this.pojoMovimiento.setEntrega(this.ifzEmpleado.findById(idEmpleado));
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

	public boolean isOperacionCompleta() {
		return band;
	}

	public void setOperacionCompleta(boolean operacionCompleta) {
		this.band = operacionCompleta;
	}

	public String getResOperacion() {
		return mensaje;
	}

	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
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

	public List<AlmacenMovimientosExt> getListaMovimientosGrid() {
		return listaMovimientosGrid;
	}

	public void setListaMovimientosGrid(List<AlmacenMovimientosExt> listaMovimientosGrid) {
		this.listaMovimientosGrid = listaMovimientosGrid;
	}

	public int getNumPaginaOrdenCompra() {
		return numPaginaOrdenCompra;
	}

	public void setNumPaginaOrdenCompra(int numPaginaOrdenCompra) {
		this.numPaginaOrdenCompra = numPaginaOrdenCompra;
	}

	public List<SelectItem> getListaCboAlmacenes() {
		return listaCboAlmacenes;
	}

	public void setListaCboAlmacenes(List<SelectItem> listaCboAlmacenes) {
		this.listaCboAlmacenes = listaCboAlmacenes;
	}

	public List<Almacen> getListaAlmacenes() {
		return listaAlmacenes;
	}

	public void setListaAlmacenes(List<Almacen> listaAlmacenes) {
		this.listaAlmacenes = listaAlmacenes;
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

	public List<Obra> getListObrasPrincipales() {
		return listObrasPrincipales;
	}

	public void setListObrasPrincipales(List<Obra> listObrasPrincipales) {
		this.listObrasPrincipales = listObrasPrincipales;
	}

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}

	public Collection<Object> getSelection() {
		return selection;
	}

	public void setSelection(Collection<Object> selection) {
		this.selection = selection;
	}

	public List<MovimientosDetalleExt> getSelectionItems() {
		return selectionItems;
	}

	public void setSelectionItems(List<MovimientosDetalleExt> selectionItems) {
		this.selectionItems = selectionItems;
	}

	public double getCantidadProductoSeleccionado() {
		return cantidadProductoSeleccionado;
	}

	public void setCantidadProductoSeleccionado(double cantidadProductoSeleccionado) {
		this.cantidadProductoSeleccionado = cantidadProductoSeleccionado;
	}

	public int getRowSeleccionado() {
		return rowSeleccionado;
	}

	public void setRowSeleccionado(int rowSeleccionado) {
		this.rowSeleccionado = rowSeleccionado;
	}

	public boolean isPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public int getCantidadDetalles() {
		return cantidadDetalles;
	}

	public void setCantidadDetalles(int cantidadDetalles) {
		this.cantidadDetalles = cantidadDetalles;
	}

	public long getAlmacenAnterior() {
		return almacenAnterior;
	}

	public void setAlmacenAnterior(long almacenAnterior) {
		this.almacenAnterior = almacenAnterior;
	}

	public List<SelectItem> getListaCboEmpleadoRecibe() {
		return listaCboEmpleadoRecibe;
	}

	public void setListaCboEmpleadoRecibe(List<SelectItem> listaCboEmpleadoRecibe) {
		this.listaCboEmpleadoRecibe = listaCboEmpleadoRecibe;
	}

	public List<Empleado> getListaEmpleados() {
		return listaEmpleados;
	}

	public void setListaEmpleados(List<Empleado> listaEmpleados) {
		this.listaEmpleados = listaEmpleados;
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

	public OrdenCompra getPojoOrdenCompra() {
		return pojoOrdenCompra;
	}

	public void setPojoOrdenCompra(OrdenCompra pojoOrdenCompra) {
		this.pojoOrdenCompra = pojoOrdenCompra;
	}

	public MovimientosDetalleExt getMovimientoSeleccionado() {
		return movimientoSeleccionado;
	}

	public void setMovimientoSeleccionado(MovimientosDetalleExt movimientoSeleccionado) {
		this.movimientoSeleccionado = movimientoSeleccionado;
	}

	public List<MovimientosDetalleExt> getListaProductosEntrada() {
		return listaProductosEntrada;
	}

	public void setListaProductosEntrada(List<MovimientosDetalleExt> listaProductosEntrada) {
		this.listaProductosEntrada = listaProductosEntrada;
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

	public boolean isUsuarioValido() {
		return usuarioValido;
	}

	public void setUsuarioValido(boolean usuarioValido) {
		this.usuarioValido = usuarioValido;
	}

	public boolean isAvanzadaBusquedaOC() {
		return avanzadaBusquedaOC;
	}

	public void setAvanzadaBusquedaOC(boolean avanzadaBusquedaOC) {
		this.avanzadaBusquedaOC = avanzadaBusquedaOC;
	}

	public boolean isAvanzadaBusquedaTraspaso() {
		return avanzadaBusquedaTraspaso;
	}

	public void setAvanzadaBusquedaTraspaso(boolean avanzadaBusquedaTraspaso) {
		this.avanzadaBusquedaTraspaso = avanzadaBusquedaTraspaso;
	}

	public boolean isAvanzadaBusquedaSalida() {
		return avanzadaBusquedaSalida;
	}

	public void setAvanzadaBusquedaSalida(boolean avanzadaBusquedaSalida) {
		this.avanzadaBusquedaSalida = avanzadaBusquedaSalida;
	}

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public List<AlmacenMovimientosExt> getListSalidaBusqueda() {
		return listSalidaBusqueda;
	}

	public void setListSalidaBusqueda(List<AlmacenMovimientosExt> listSalidaBusqueda) {
		this.listSalidaBusqueda = listSalidaBusqueda;
	}

	public AlmacenMovimientosExt getPojoSalida() {
		return pojoSalida;
	}

	public void setPojoSalida(AlmacenMovimientosExt pojoSalida) {
		this.pojoSalida = pojoSalida;
	}

	public List<SelectItem> getSalidaBusquedaTipos() {
		return salidaBusquedaTipos;
	}

	public void setSalidaBusquedaTipos(List<SelectItem> salidaBusquedaTipos) {
		this.salidaBusquedaTipos = salidaBusquedaTipos;
	}

	public String getSalidaBusquedaCampo() {
		return salidaBusquedaCampo;
	}

	public void setSalidaBusquedaCampo(String salidaBusquedaCampo) {
		this.salidaBusquedaCampo = salidaBusquedaCampo;
	}

	public String getSalidaBusquedaValor() {
		return salidaBusquedaValor;
	}

	public void setSalidaBusquedaValor(String salidaBusquedaValor) {
		this.salidaBusquedaValor = salidaBusquedaValor;
	}

	public int getSalidaBusquedaPaginas() {
		return salidaBusquedaPaginas;
	}

	public void setSalidaBusquedaPaginas(int salidaBusquedaPaginas) {
		this.salidaBusquedaPaginas = salidaBusquedaPaginas;
	}

	public boolean isPerfilAdministrativo() {
		return perfilAdministrativo;
	}

	public void setPerfilAdministrativo(boolean perfilAdministrativo) {
		this.perfilAdministrativo = perfilAdministrativo;
	}

	public int getNivelAlmacenes() {
		return nivelAlmacenes;
	}

	public void setNivelAlmacenes(int nivelAlmacenes) {
		this.nivelAlmacenes = nivelAlmacenes;
	}

	public List<SelectItem> getListTargetAlmacenes() {
		return listTargetAlmacenes;
	}

	public void setListTargetAlmacenes(List<SelectItem> listTargetAlmacenes) {
		this.listTargetAlmacenes = listTargetAlmacenes;
	}

	public int getTargetAlmacen() {
		return targetAlmacen;
	}

	public void setTargetAlmacen(int targetAlmacen) {
		this.targetAlmacen = targetAlmacen;
	}
}
