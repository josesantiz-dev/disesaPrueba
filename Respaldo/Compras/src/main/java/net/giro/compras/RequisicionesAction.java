package net.giro.compras;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.TiposObra;
import net.giro.adp.logica.ObraRem;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.beans.RequisicionDetalleExt;
import net.giro.compras.beans.RequisicionExt;
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.beans.TipoInsumo;
import net.giro.inventarios.beans.TipoMaestro;
import net.giro.inventarios.logica.ProductoRem;
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
@ManagedBean(name="reqAction")
public class RequisicionesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(RequisicionesAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx;
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
	private List<RequisicionDetalleExt> listReqDetalles;
	private List<RequisicionDetalleExt> listReqDetallesEliminados;
	private List<SelectItem> listEmpleadosItems;
	private List<ConValores> listEspecialidades;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	// POJO's
	private RequisicionExt pojoRequisicion;
	private Requisicion pojoRequisicionBorrar;
	private Requisicion pojoRequisicionReporte;
	private RequisicionDetalleExt pojoReqDetalle;
	private RequisicionDetalleExt pojoReqDetalleBorrar;
	private Producto pojoProductoSeleccionado;
	private ProductoExt pojoProducto;
	private ConGrupoValores pojoGpoMaestros;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	private ConGrupoValores pojoGpoUnidadesMedida;
	private Obra pojoObra;
	// Busqueda principal
	private List<Requisicion> listRequisiciones;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	private int tipoMaestroBusqueda;
	// Busqueda obras
	private List<Obra> listObras;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorBusquedaTipoObra;
	private int numPaginaObras;	
	// Busqueda Solicitantes
	private List<Empleado> listSolicitantes;
	private Empleado pojoSolicitante;
	private List<SelectItem> tiposBusquedaSolicitantes;
	private String campoBusquedaSolicitantes;
	private String valorBusquedaSolicitantes;
	private int numPaginaSolicitantes;
	// Busqueda productos
	private List<Producto> listProductos;
	private List<SelectItem> tiposBusquedaProductos;	
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private long familiaBusquedaProductos;
	private int numPaginaProductos;	
	// Nuevo Producto
	private MonedasValoresDAO ifzMonValores;
	private Producto pojoProductoNuevo;
	private List<ConValores> listUnidadesMedida;
	private List<SelectItem> listUnidadesMedidaItems;
	private boolean previaBusquedaProducto;
	// Monedas
	private MonedaDAO ifzMonedas;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private long idMoneda;
	// Variables de operacion
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
    private long usuarioId;
    private String usuario;	
	private double cantidadProducto;
	private long valComprador;
	private boolean permiteEditar;
	private boolean perfilEgresos;
	private TipoMaestro tipoMaestro;
	private ConGrupoValores pojoGpoGastos;
	private ConGrupoValores pojoGpoTipoEgresos;
	private ConValores pojoEgresoActivo;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	
	public RequisicionesAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey(), item.getValue());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			// EVALUACION DE PERFILES
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			this.ctx = new InitialContext();
			this.ifzRequisiciones = (RequisicionRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalles = (RequisicionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzEmpleados = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzProductos = (ProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzGpoVal = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzMonValores = (MonedasValoresDAO) this.ctx.lookup("ejb:/Model_TYG//MonedasValoresImp!net.giro.tyg.dao.MonedasValoresDAO");
			this.ifzOrdenes = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzOrdenDetalles = (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzCotizaciones = (CotizacionRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzCotiDetalles = (CotizacionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			
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
			this.pojoGpoGastos = this.ifzGpoVal.findByName("SYS_MOVGTOS");
			if (this.pojoGpoGastos == null || this.pojoGpoGastos.getId() <= 0L)
				log.error("No se encontro encontro el grupo SYS_MOVGTOS en con_grupo_valores");

			// TIPOS EGRESO 
			// ----------------------------------------------------------------------------
			this.pojoGpoTipoEgresos = this.ifzGpoVal.findByName("SYS_TIPO_EGRESO");
			if (this.pojoGpoTipoEgresos == null || this.pojoGpoTipoEgresos.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_TIPO_EGRESO en con_grupo_valores");
			recuperarEgreso();
			
			// Listas
			this.listRequisiciones = new ArrayList<Requisicion>();
			this.listReqDetalles = new ArrayList<RequisicionDetalleExt>();
			this.listObras = new ArrayList<Obra>();
			
			// POJO's
			this.pojoRequisicion = new RequisicionExt();
			this.pojoRequisicionBorrar = new Requisicion();
			this.pojoProductoNuevo = new Producto();
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			if (this.perfilEgresos)
				this.tiposBusqueda.add(new SelectItem("nombreObra", "Obra/Egreso Administrativo"));
			else
				this.tiposBusqueda.add(new SelectItem("nombreObra", "Obra"));
			this.tiposBusqueda.add(new SelectItem("nombreSolicita", "Solicita"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
			this.valorBusquedaObras = "";
			this.valorBusquedaTipoObra = 0;
			this.numPaginaObras = 1;
			
			// Busqueda Solicitantes
			this.tiposBusquedaSolicitantes = new ArrayList<SelectItem>();
			this.tiposBusquedaSolicitantes.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaSolicitantes.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaSolicitantes.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaSolicitantes.add(new SelectItem("id", "ID"));
			this.campoBusquedaSolicitantes = this.tiposBusquedaSolicitantes.get(0).getValue().toString();
			this.valorBusquedaSolicitantes = "";
			this.numPaginaSolicitantes = 1;
			
			// Busqueda productos
			this.tiposBusquedaProductos = new ArrayList<SelectItem>();
			this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusquedaProductos.add(new SelectItem("clave", "Clave"));
			this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
			this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
			this.valorBusquedaProductos = "";
			this.familiaBusquedaProductos = 0;
			this.numPaginaProductos = 1;

			this.tipoMaestro = TipoMaestro.Inventario;
			this.tipoMaestroBusqueda = (this.perfilEgresos ? 0 : 1);
			nuevaBusquedaObras();
			nuevaBusquedaProductos();
		} catch (Exception e) {
			log.error("Error en Compras.RequisicionAction.constructor RequisicionesAction", e);
			this.ctx = null;
		}
	}


	public void buscar() throws Exception {
		try {
    		control();
			
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = "id";
			
			if (this.isDebug)
				log.info("Buscando Requisiciones. " + this.campoBusqueda + ": " + this.valorBusqueda);
			this.ifzRequisiciones.OrderBy("CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.id desc");
			this.listRequisiciones = this.ifzRequisiciones.findLikeProperty(this.campoBusqueda, this.valorBusqueda, this.tipoMaestroBusqueda, 0);
			if (this.listRequisiciones.isEmpty()) {
	    		control(2);
				if (this.isDebug) log.info("Buscando Requisiciones. ERROR 2 - Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
    		control(true);
			if (this.listRequisiciones != null) this.listRequisiciones.clear();
    		log.error("Error en Compras.RequisicionAction.buscar", e);
    	}
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
			log.info("Proceso para copiar producto de las requisiciones de la Obra CHAMETLA a MAESTRO ADMINISTRATIVO");
			reqs = this.ifzRequisiciones.findByProperty("idObra", 10000090L, 0);
			if (reqs == null || reqs.isEmpty()) {
				log.info("la busqueda no regreso resultados");
				control(2);
				return;
			}

			log.info("Se encontraron " + reqs.size() + " requisiciones");
			for (Requisicion r : reqs) {
				List<RequisicionDetalle> det = this.ifzReqDetalles.findByProperty("idRequisicion", r.getId(), 0);
				if (det == null || det.isEmpty()) {
					log.info("Sin detalles en requisicion " + r.getId());
					continue;
				}
				
				log.info("Se encontraron " + reqs.size() + " detalles en requisicion " + r.getId());
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
						producto.setCreadoPor(this.usuarioId);
						producto.setFechaModificacion(Calendar.getInstance().getTime());
						producto.setModificadoPor(this.usuarioId);
						producto.setId(this.ifzProductos.save(producto));
						idProductoNew = producto.getId();
						convertidos.put(idProductoOld, idProductoNew);
					} 

					d.setIdProducto(convertidos.get(idProductoOld));
					d.setFechaModificacion(Calendar.getInstance().getTime());
					d.setModificadoPor(this.usuarioId);
					this.ifzReqDetalles.update(d);
				}
			}

			for (Requisicion r : reqs) {
				cots = this.ifzCotizaciones.findByProperty("idRequisicion", r.getId(), 0);
				if (cots == null || cots.isEmpty()) {
					log.info("Sin cotizaciones con la requisicion " + r.getId());
					continue;
				}
				
				log.info("Se encontraron " + reqs.size() + " cotizaciones con la requisicion " + r.getId());
				for (Cotizacion c : cots) {
					List<CotizacionDetalle> det = this.ifzCotiDetalles.findByProperty("idCotizacion", c.getId(), 0);
					if (det == null || det.isEmpty()) {
						log.info("Sin detalles en cotizacion " + c.getId());
						continue;
					}
					
					for (CotizacionDetalle d : det) {
						if (convertidos.containsKey(d.getIdProducto())) {
							d.setIdProducto(convertidos.get(d.getIdProducto()));
							d.setFechaModificacion(Calendar.getInstance().getTime());
							d.setModificadoPor(this.usuarioId);
							this.ifzCotiDetalles.update(d);
						}
					}
				}
			}
			
			for (Cotizacion c : cots) {
				ors = this.ifzOrdenes.findByProperty("idCotizacion", c.getId(), 0);
				if (ors == null || ors.isEmpty()) {
					log.info("Sin ordenes de compra con la cotizacion " + c.getId());
					continue;
				}
				
				log.info("Se encontraron " + ors.size() + " ordenes de compra con la cotizacion " + c.getId());
				for (OrdenCompra o : ors) {
					List<OrdenCompraDetalle> det = this.ifzOrdenDetalles.findByProperty("idCotizacion", o.getId(), 0);
					if (det == null || det.isEmpty()) {
						log.info("Sin detalles en orden de compra " + o.getId());
						continue;
					}
					
					for (OrdenCompraDetalle d : det) {
						if (convertidos.containsKey(d.getIdProducto())) {
							d.setIdProducto(convertidos.get(d.getIdProducto()));
							d.setFechaModificacion(Calendar.getInstance().getTime());
							d.setModificadoPor(this.usuarioId);
							this.ifzOrdenDetalles.update(d);
						}
					}
				}
			}

			log.info("Proceso terminado");
			control(true);
		} catch (Exception e) {
			log.error("Error en Compras.RequisicionAction.buscarGuardar(). Convertidos: " + convertidos, e);
			control(false);
		}
	}
	
	public void nuevo() throws Exception {
		try {
    		control();
    		log.info("Nueva Requisicion");
    		this.permiteEditar = true;
			this.pojoRequisicion = new RequisicionExt();
			this.pojoRequisicionBorrar = null;
			this.pojoProducto = null;
			this.listReqDetalles.clear();
			this.valComprador = 0;
			this.idMoneda = 0;
			
			if (! this.perfilEgresos)
				cargarFamilias();
			
			cargarSolicitantes();
			nuevaBusquedaObras();
			nuevaBusquedaProductos();
			cargarMonedas();
    	} catch (Exception e) {
    		log.error("Error en Compras.RequisicionAction.nuevo", e);
    		control(true);
    	}
	}
	
	public void nuevoMaestroInventarios() throws Exception {
		log.info("Requisicion para Maestro de Inventarios (Insumos)");
		this.tipoMaestro = TipoMaestro.Inventario;
		cargarFamilias();
	}
	
	public void nuevoMaestroAdminitrativo() throws Exception {
		log.info("Requisicion para Maestro Administrativo (Egresos de Operacion)");
		this.tipoMaestro = TipoMaestro.Administrativo;
		cargarFamilias();
	}
	
	public void editar() throws Exception {
		try {
    		control();
			if (this.listReqDetalles == null)
				this.listReqDetalles = new ArrayList<RequisicionDetalleExt>();
			this.listReqDetalles.clear();
			
			if (this.listReqDetallesEliminados == null)
				this.listReqDetallesEliminados = new ArrayList<RequisicionDetalleExt>();
			this.listReqDetallesEliminados.clear();

			this.permiteEditar = true;
			if (this.pojoRequisicion.getEstatus() != 0)
				this.permiteEditar = false;
			
			// Cargamos los compradores
			cargarSolicitantes();

			// Cargamos los Monedas
			cargarMonedas();

			this.tipoMaestro = TipoMaestro.Inventario;
			if (this.pojoRequisicion.getTipo() == TipoMaestro.Administrativo.ordinal())
				this.tipoMaestro = TipoMaestro.Administrativo;
			
			// Recuperamos quien solicita de la requisicion
			if (this.pojoRequisicion.getIdSolicita() != null)
				this.valComprador = this.pojoRequisicion.getIdSolicita().getId();

			// Recuperamos moneda
			this.idMoneda = 0L;
			if (this.pojoRequisicion.getIdMoneda() != null && this.pojoRequisicion.getIdMoneda() > 0L)
				this.idMoneda = this.pojoRequisicion.getIdMoneda();
			
			// Recuperamos los detalles de la cotizacion
			this.listReqDetalles = this.ifzReqDetalles.findExtByProperty("idRequisicion", this.pojoRequisicion.getId(), 120);
			
			nuevaBusquedaObras();
			nuevaBusquedaProductos();
    	} catch (Exception e) {
    		control(true);
    		log.error("Error en Compras.RequisicionAction.editar", e);
    	}
	}
	
	public void guardar() throws Exception {
		try {
    		control();
			if(this.valComprador <= 0L) {
	    		control(5);
				return;
			}
			
			this.pojoRequisicion.setIdMoneda(this.idMoneda);
			this.pojoRequisicion.setModificadoPor(this.usuarioId);
			this.pojoRequisicion.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoRequisicion.setTipo(this.tipoMaestro.ordinal());
			
			// Asignamos el comprador
			if (this.valComprador > 0L)
				this.pojoRequisicion.setIdSolicita(this.ifzEmpleados.findByIdExt(this.valComprador));
			
			if (this.pojoRequisicion.getId() == null) {
				log.info("Guardando Requisicion.");
				this.pojoRequisicion.setCreadoPor(this.usuarioId);
				this.pojoRequisicion.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos en la BD
				this.pojoRequisicion.setId(this.ifzRequisiciones.save(this.pojoRequisicion));
				this.listRequisiciones.add(this.ifzRequisiciones.findById(this.pojoRequisicion.getId()));
				log.info("Requisicion guardada");
			} else {
				// Actualizamos en la BD
				log.info("Actualizando Requisicion.");
				this.ifzRequisiciones.update(this.pojoRequisicion);
				
				// Buscamos la cotizacion en el listado
				int index = -1;
				for(Requisicion var : this.listRequisiciones) {
					if (this.pojoRequisicion.getId().equals(var.getId())) {
						index = this.listRequisiciones.indexOf(var);
						break;
					}
				}
				
				// Actualizamos los datos en el listado
				if (index > -1)
					this.listRequisiciones.set(index, this.ifzRequisiciones.findById(this.pojoRequisicion.getId()));
				log.info("Requisicion actualizada");
			}
			
			// Borramos los detalles de la requisiciones si corresponde
			if (this.listReqDetallesEliminados != null && !this.listReqDetallesEliminados.isEmpty()) {
				if (this.isDebug) log.info("Borrando detalles de Requisicion previamente eliminados.");
				for(RequisicionDetalleExt var : this.listReqDetallesEliminados) {
					if (var.getId() != null && var.getId() > 0L) {
						this.ifzReqDetalles.delete(var.getId());
					}
				}
				
				// limpiamos el listado
				this.listReqDetallesEliminados.clear();
				if (this.isDebug) log.info("Detalles de Requisicion borrados");
			}
			
			// Guardamos los detalles nuevos y modificados
			if (this.listReqDetalles != null && !this.listReqDetalles.isEmpty()) {
				log.info("Guardando detalles de requisicion");
				for(RequisicionDetalleExt var : this.listReqDetalles) {
					// Asignamos la cotizacion al detalle
					var.setIdRequisicion(this.pojoRequisicion);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.usuarioId);
					
					// Guardamos el detalle
					if (var.getId() == null || var.getId() <= 0L) {
						var.setFechaCreacion(Calendar.getInstance().getTime());
						var.setCreadoPor(this.usuarioId);
						
						// Guardamos en la BD
						var.setId(this.ifzReqDetalles.save(var));
					} else {
						// Actualizamos en la BD
						this.ifzReqDetalles.update(var);
					}
				}
				log.info("Detalles de requisicion guardados");
			}
			
			this.pojoRequisicion = new RequisicionExt();
			this.pojoRequisicionBorrar = null;
			this.listReqDetalles.clear();
    	} catch (Exception e) {
    		log.error("Error en Compras.RequisicionAction.guardar", e);
    		control(true);
    	}
	}
	
	public void eliminar() {
		try {
    		control();
			
			// Actualizamos el elemento de la lista
    		log.info("Eliminando Requisicion");
			for (Requisicion var : this.listRequisiciones) {
				if (var.getId() == this.pojoRequisicionBorrar.getId()) {
					if (this.isDebug) log.info("Borrando Requisicion.");
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					var.setEstatus(1);
					
					// Actualizamos el pojo de la BD
					this.ifzRequisiciones.update(var);
					log.info("--- -- ---> Requisicion borrada (Actualizada)");
					break;
				}
			}
			
			this.pojoRequisicionBorrar = null;
			this.pojoRequisicion = new RequisicionExt();
    	} catch (Exception e) {
    		control(true);
    		log.error("Error en Compras.RequisicionAction.eliminar", e);
    	}
	}

	public void eliminarDetalle() {
		int index = -1;
		
		try {
    		control();
			if (this.listReqDetallesEliminados == null)
				this.listReqDetallesEliminados = new ArrayList<RequisicionDetalleExt>();
			
			if (this.pojoReqDetalleBorrar == null)
				return;

			//Agregamos el detalle al listado de eliminados
			log.info("Borrando detalle de requisicion");
			index = this.listReqDetalles.indexOf(this.pojoReqDetalleBorrar);
			if (this.pojoReqDetalleBorrar.getId() != null && this.pojoReqDetalleBorrar.getId() > 0L) {
				this.pojoReqDetalleBorrar.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoReqDetalleBorrar.setModificadoPor(this.usuarioId);
				this.listReqDetallesEliminados.add(this.pojoReqDetalleBorrar);
			}
			
			if (index >= 0)
				this.listReqDetalles.remove(index);
			
			log.info("--- -- ---> Detalle de requisicion borrado");
			this.pojoReqDetalleBorrar = null;
    	} catch (Exception e) {
    		log.error("Error en Compras.RequisicionAction.eliminarDetalle", e);
    		control(true);
    	}
	}

	public void agregarReqDetalle() {
		try {
    		control();
			if (this.pojoReqDetalle == null)
				this.pojoReqDetalle = new RequisicionDetalleExt();
			
			log.info("Agregando detalle a requisicion");
			if(this.pojoProducto == null || this.pojoProducto.getId() == null || this.pojoProducto.getId() <= 0L) {
	    		log.info("--- -- ---> ERROR 5 - Producto no seleccionado");
	    		control(5);
				return;
			}
			
			if(this.cantidadProducto <= 0) {
	    		log.info("--- -- ---> ERROR 4 - Cantidad menor oo igual a cero");
	    		control(4);
				return;
			}
			
			// Generamos el detalle de la requisicion
			this.pojoReqDetalle.setIdProducto(this.pojoProducto);
			this.pojoReqDetalle.setCantidad(this.cantidadProducto);
			
			// Agregamos el detalle al listado
			if (this.listReqDetalles == null)
				this.listReqDetalles = new ArrayList<RequisicionDetalleExt>();
			
			boolean existe = false;
			for (RequisicionDetalleExt var : this.listReqDetalles) {
				if (var.getIdProducto().getId().equals(this.pojoReqDetalle.getIdProducto().getId())) {
					var.setCantidad(var.getCantidad() + this.pojoReqDetalle.getCantidad());
					existe = true;
					break;
				}
			}
			
			if (! existe)
				this.listReqDetalles.add(this.pojoReqDetalle);
			log.info("--- -- ---> Detalle agregado");
			this.pojoReqDetalle = null;
			this.pojoProducto = null;
			this.cantidadProducto = 0;
    	} catch (Exception e) {
    		control(true);
    		log.error("Error en Compras.RequisicionAction.agregarReqDetalle", e);
    	}
	}
	
	public void cargarSolicitantes() {
		List<Empleado> listEmpleados = new ArrayList<Empleado>();
		
		try {
			if (this.listEmpleadosItems == null)
				this.listEmpleadosItems = new ArrayList<SelectItem>();
			this.listEmpleadosItems.clear();

			// Cargamos la lista de compradores
			if (this.isDebug) log.info("Cargando lista de solicitantes (Empleados).");
			listEmpleados = this.ifzEmpleados.findAll();
			
			// Generamos la lista auxiliar de compradores
			if(listEmpleados != null && ! listEmpleados.isEmpty()) {
				if (this.isDebug) log.info("Generando lista de items (Solicitantes)");
				for (Empleado var : listEmpleados) {
					this.listEmpleadosItems.add(new SelectItem(var.getId(), var.getNombre()));
				}
			}
		} catch (Exception e) {
			log.error("Error al cargar compradores", e);
		}
	}
	
	public void cargarUnidadesMedida() {
		try {
			if (this.listUnidadesMedidaItems == null)
				this.listUnidadesMedidaItems = new ArrayList<SelectItem>();
			this.listUnidadesMedidaItems.clear();
			
			// Cargamos la lista de familias
			log.info("Cargando lista de familias");
			this.listUnidadesMedida = this.ifzConValores.buscaValorGrupo("descripcion", "", this.pojoGpoUnidadesMedida);
			
			// Generamos la lista auxiliar de familias
			if (this.listUnidadesMedida != null && ! this.listUnidadesMedida.isEmpty()) {
				log.info("Generando lista de items (UnidadesMedida)");
				for (ConValores var : this.listUnidadesMedida) {
					if (var.getValor().trim().equals(var.getDescripcion().trim()))
						this.listUnidadesMedidaItems.add(new SelectItem(var.getId(), var.getDescripcion()));
					else
						this.listUnidadesMedidaItems.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
				}
			}
		} catch (Exception e) {
			log.error("Error al cargar las UnidadesMedida", e);
		} finally {
			if (this.listUnidadesMedidaItems != null && ! this.listUnidadesMedidaItems.isEmpty()) 
				log.info(this.listUnidadesMedidaItems.size() + " items (UnidadesMedida) generados");
		}
	}
	
	public void cargarFamilias() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<ConValores> listFamAux = null;
		List<ConValores> listAux = null;
		String valTipoMaestro = "";
		String idMaestro = "";
		
		try {
			if (this.listEspecialidades == null)
				this.listEspecialidades = new ArrayList<ConValores>();
			this.listEspecialidades.clear();
			
			if (this.listFamilias == null)
				this.listFamilias = new ArrayList<ConValores>();
			this.listFamilias.clear();
			
			if (this.listFamiliasItems == null)
				this.listFamiliasItems = new ArrayList<SelectItem>();
			this.listFamiliasItems.clear();
			
			log.info("Cargando Maestros");
			valTipoMaestro = String.valueOf(this.tipoMaestro.ordinal());
			listAux = this.ifzConValores.findAll(this.pojoGpoMaestros);
			for (ConValores var : listAux) {
				if (! var.getValor().equals(valTipoMaestro)) continue;
				idMaestro = String.valueOf(var.getId());
				break;
			}

			log.info("Cargando Especialidades del Maestro " + this.tipoMaestro.toString());
			params.put("grupoValorId.id", this.pojoGpoEspecialidades.getId());
			params.put("atributo1", idMaestro);
			this.listEspecialidades = this.ifzConValores.findByProperties(params, 0);
			if (this.listEspecialidades != null && ! this.listEspecialidades.isEmpty()) {
				for (ConValores var : this.listEspecialidades) {
					params.put("grupoValorId.id", this.pojoGpoFamilias.getId());
					params.put("atributo1", String.valueOf(var.getId()));
					listFamAux = this.ifzConValores.findByProperties(params, 0);
					if (listFamAux == null || listFamAux.isEmpty())
						continue;
					this.listFamilias.addAll(listFamAux);
				}
			}
			
			Collections.sort(this.listFamilias, new Comparator<ConValores>() {
			    	@Override
			        public int compare(ConValores o1, ConValores o2) {
			    		return o1.getDescripcion().compareTo(o2.getDescripcion());
			        }
			});
			
			// Generamos la lista auxiliar de familias
			if (this.listFamilias != null && ! this.listFamilias.isEmpty()) {
				log.info("Generando lista de items (familias)");
				for (ConValores var : this.listFamilias) {
					if (var.getValor().trim().equals(var.getDescripcion().trim()))
						this.listFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion()));
					else
						this.listFamiliasItems.add(new SelectItem(var.getId(), var.getDescripcion() + " (" + var.getValor() + ")"));
				}
			}
		} catch (Exception e) {
			log.error("Error al cargar las familias", e);
		} finally {
			if (this.listFamiliasItems != null && ! this.listFamiliasItems.isEmpty()) 
				log.info(this.listFamiliasItems.size() + " items (familias) generados");
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
					this.listMonedasItems.add(new SelectItem(var.getId(), var.getNombre() + " (" + var.getAbreviacion() + ")"));
				}
			}
		} catch (Exception e) {
			log.error("Error al cargar las Monedas", e);
		}
	}
	
	public void reporte() {
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		String nombreDocumento = "";
		
		try{
			if (this.pojoRequisicionReporte != null && this.pojoRequisicionReporte.getId() != null && this.pojoRequisicionReporte.getId() > 0L) {
				// Parametros del reporte
				paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idRequisicion", this.pojoRequisicionReporte.getId());

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
				params.put("usuario", 		  this.usuario);

				Respuesta respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
				if(respuesta.getErrores().getCodigoError() == 0L){
					nombreDocumento = "Req-" + this.pojoRequisicionReporte.getId() + "." + respuesta.getBody().getValor("formatoReporte");
					
					this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));
					this.httpSession.setAttribute("formato", respuesta.getBody().getValor("formatoReporte"));	
					this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
					
					/*this.httpSession.setAttribute("contenido", respuesta.getBody().getValor("contenidoReporte"));	
					this.httpSession.setAttribute("nombreDocumento", respuesta.getBody().getValor("nombreDocumento"));
					this.httpSession.setAttribute("formato", this.entornoProperties.getString("REPORTE_REQUISICION_FORMATO"));*/ 
				} else {
					this.mensaje = respuesta.getErrores().getDescError();
				}
			}
			
			this.pojoRequisicionReporte = null;
			this.pojoRequisicionBorrar = null;
		} catch (Exception e) {
			this.mensaje = "error al ejecutar reporte";
			e.printStackTrace();
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
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "": mensaje;
	}
	
    // Busqueda Obras
    // --------------------------------------------------------------------------------------
	public void nuevaBusquedaObras() {
    	this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";

		this.valorBusquedaTipoObra = 0;
		if (this.tipoMaestro == TipoMaestro.Administrativo)
			this.valorBusquedaTipoObra = 4;
		
		this.pojoObra = null;
		if (this.listObras != null)
			this.listObras.clear();
    }
	
	public void buscarObras() throws Exception {
		try {
    		control();
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();

			log.info("Buscando Obras. " + this.campoBusquedaObras + " - " + this.valorBusquedaObras);
			this.numPaginaObras = 1;
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.valorBusquedaTipoObra);
			if(this.listObras.isEmpty()){
				if (this.isDebug) log.info("Buscando Obras. ERROR 2: Busqueda sin resultados");
	    		control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listObras != null) this.listObras.clear();
    		log.error("Error en Compras.RequisicionAction.buscarObras", e);
    		control(true);
    	}
	}

	public void seleccionarObra() throws Exception {
		// Recuperamos los insumos de la obra seleccionada
		if (this.pojoObra != null && this.pojoObra.getId() != null)
			this.pojoRequisicion.setIdObra(this.ifzObras.convertir(this.pojoObra));
		nuevaBusquedaObras();
	}

	// Busqueda Solicitantes (Empleados)
    // --------------------------------------------------------------------------------------
	public void nuevaBusquedaSolicitante() {
		try {
    		control();
    		this.pojoSolicitante = null;
			this.campoBusquedaSolicitantes = this.tiposBusquedaSolicitantes.get(0).getValue().toString();
			this.valorBusquedaSolicitantes = "";
			this.numPaginaSolicitantes = 1;
			
			if (this.listSolicitantes != null)
				this.listSolicitantes.clear();
    	} catch (Exception e) {
    		log.error("Error en Compras.RequisicionAction.nuevaBusquedaSolicitante", e);
    		control(true);
    	}
	}
	
	public void buscarSolicitantes() {
		try {
    		control();
			if ("".equals(this.campoBusquedaSolicitantes))
				this.campoBusquedaSolicitantes = this.tiposBusquedaSolicitantes.get(0).getDescription();

			log.info("Buscando Solicitantes. " + this.campoBusquedaSolicitantes + ": " + this.valorBusquedaSolicitantes);
			this.numPaginaSolicitantes = 1;
			this.listSolicitantes = this.ifzEmpleados.findLikeProperty(this.campoBusquedaSolicitantes, this.valorBusquedaSolicitantes, 0);
			if(this.listSolicitantes == null || this.listSolicitantes.isEmpty()){
				log.info("Buscando Solicitantes. ERROR 2: Busqueda sin resultados");
	    		control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listSolicitantes != null) this.listSolicitantes.clear();
    		log.error("Error en Compras.RequisicionAction.buscarSolicitantes", e);
    		control(true);
    	}
	}
	
	public void seleccionarSolicitante() {
		try {
    		control();
    		if (this.pojoRequisicion != null) {
    			log.info("Extendemos el solicitante (Empleado) seleccionado y lo asignamos a la Requisicion");
    			this.pojoRequisicion.setIdSolicita(this.ifzEmpleados.convertir(this.pojoSolicitante));
    		}
    		nuevaBusquedaSolicitante();
    	} catch (Exception e) {
    		log.error("Error en Compras.RequisicionAction.seleccionarSolicitante", e);
    		control(true);
    	}
	}
	
    // Busqueda Productos
    // --------------------------------------------------------------------------------------
	public void nuevaBusquedaProductos() {
		control();
		this.previaBusquedaProducto = false;
    	this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
		this.valorBusquedaProductos = "";
		this.familiaBusquedaProductos = 0;
		this.numPaginaProductos = 1;
		if (this.listProductos != null)
			this.listProductos.clear();
		
		this.pojoProductoSeleccionado = null;
    }
	
	public void buscarProductos() {
		HashMap<String, String> params = new HashMap<String, String>();
		String valorBusqueda = "";
		
		try {
    		control();
			if ("".equals(this.campoBusquedaProductos))
				this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
			
			valorBusqueda = this.valorBusquedaProductos.trim();
			if (! "".equals(valorBusqueda) && valorBusqueda.contains(" "))
				valorBusqueda = valorBusqueda.replace(" ", "%");

			if (this.familiaBusquedaProductos <= 0L)
				this.familiaBusquedaProductos = 0L;

			this.previaBusquedaProducto = false;
			this.numPaginaProductos = 1;
			if (this.listProductos == null)
				this.listProductos = new ArrayList<Producto>();
			this.listProductos.clear();

			// Parametros de busqueda
			log.info("Buscando Productos - " + this.campoBusquedaProductos + ": " + this.valorBusquedaProductos);
			params.put(this.campoBusquedaProductos, valorBusqueda);
			params.put("tipo", String.valueOf(this.tipoMaestro.ordinal()));
			params.put("estatus", "0");
			if (this.familiaBusquedaProductos > 0L) 
				params.put("familia", String.valueOf(this.familiaBusquedaProductos));
			/*if (this.idMoneda > 0L) 
			params.put("idMoneda", String.valueOf(this.idMoneda));*/
			
			this.listProductos = this.ifzProductos.findLikeProperties(params, null, 0, this.campoBusquedaProductos);
			if(this.listProductos.isEmpty()) {
				this.previaBusquedaProducto = true;
				log.info("Buscando Productos. ERROR 2: Busqueda sin resultados");
	    		control(2);
				return;
			}
    	} catch (Exception e) {
			if (this.listProductos != null) this.listProductos.clear();
    		log.error("Error en Compras.RequisicionAction.buscarProductos", e);
    		control(true);
    	}
	}
	
	public void seleccionarProducto() throws Exception {
		try {
    		control();
			if (this.pojoProductoSeleccionado != null) {
				log.info("Extendemos producto seleccionado");
				this.pojoProducto = this.ifzProductos.convertir(this.pojoProductoSeleccionado);
			}
			
			this.previaBusquedaProducto = false;
			nuevaBusquedaProductos();
    	} catch (Exception e) {
			if (this.listProductos != null) this.listProductos.clear();
    		log.error("Error en Compras.RequisicionAction.seleccionarProducto", e);
    		control(true);
    	}
	}
	
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
			log.error("ERROR en Compras.RequisicionAction.nuevoProducto. No se pudo generar el nuevo producto.");
			control(true);
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
			// Asignamos precios
			this.pojoProductoNuevo.setPrecioCompra(0.0);
			this.pojoProductoNuevo.setPrecioUnitario(0.0);
			this.pojoProductoNuevo.setPrecioVenta(0.0);
			// Asigno valores de auditoria
			this.pojoProductoNuevo.setCreadoPor(this.usuarioId);
			this.pojoProductoNuevo.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoProductoNuevo.setModificadoPor(this.usuarioId);
			this.pojoProductoNuevo.setFechaModificacion(Calendar.getInstance().getTime());
			
			// Guardo el producto en la BD
			this.pojoProductoNuevo.setId(this.ifzProductos.save(this.pojoProductoNuevo));

			// Guardamos o actualizamos copia en Gastos si corresponde
			if ("ACTIVOS".equals(this.pojoProductoNuevo.getDescEspecialidad().trim().toUpperCase()))
				copiaActualizaEnGastos(this.pojoProductoNuevo);

			log.info("Extendemos producto nuevo seleccionado");
			this.pojoProducto = this.ifzProductos.convertir(this.pojoProductoNuevo);
			this.pojoProductoNuevo = new Producto();
			this.previaBusquedaProducto = false;
			nuevaBusquedaProductos();
		} catch (Exception e) {
			log.error("ERROR en Compras.RequisicionAction.guardarProducto. No se pudo guardar el nuevo producto.", e);
			control(true);
		}
	}
	
	private void copiaActualizaEnGastos(Producto pojoProducto) {
		ConValores pojoGasto = null;
	
		try {
			log.info("  --- Buscando producto en Gasto");
			List<ConValores> lista = this.ifzConValores.buscaValorGrupo("valor", String.valueOf(pojoProducto.getId()), this.pojoGpoGastos); //.findByPropertyPojoSolito("valor", String.valueOf(pojoProducto.getId()));
			if (lista != null && ! lista.isEmpty())
				pojoGasto = lista.get(0);
			
			if (pojoGasto != null && pojoGasto.getId() > 0L) {
				log.info("  --- Actualizando copia en Gasto");
				pojoGasto.setDescripcion(pojoProducto.getDescripcion());
				pojoGasto.setModificadoPor(this.usuarioId);
				pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());

				// Actualizo en la BD
				this.ifzConValores.update(pojoGasto);
				log.info("  --- Copia en Gasto actualizado");
			} else {
				log.info("  --- Generando copia en Gasto");
				pojoGasto = new ConValores();
				pojoGasto.setValor(String.valueOf(pojoProducto.getId()));
				pojoGasto.setDescripcion(pojoProducto.getDescripcion());
				pojoGasto.setGrupoValorId(this.pojoGpoGastos);
				pojoGasto.setCreadoPor(this.usuarioId);
				pojoGasto.setFechaCreacion(Calendar.getInstance().getTime());
				pojoGasto.setModificadoPor(this.usuarioId);
				pojoGasto.setFechaModificacion(Calendar.getInstance().getTime());

				if (this.pojoEgresoActivo != null && this.pojoEgresoActivo.getId() > 0L)
					pojoGasto.setAtributo1(String.valueOf(this.pojoEgresoActivo.getId()));

				// Guardo en la BD
				pojoGasto.setId(this.ifzConValores.save(pojoGasto));
				log.info("  --- Gasto generado");
			}
		} catch (Exception e) {
			log.error("  --- Ocurrio un problema al hacer una copia del producto en Gastos: " + pojoProducto.getId(), e);
			control(true);
		}
	}
	
	private boolean validaGuardarProducto() {
		int intento = 0;
		int maxIntentosClave = 3;
		boolean claveRepetida = false;
		this.tipoMensaje = 0;

		log.info("Validando producto");
		if (this.pojoProductoNuevo.getClave().trim().equals("")) {
			log.info("---> Debe indicar la clave del producto");
			this.tipoMensaje = -10;
			return false;
		}
		
		if (this.pojoProductoNuevo.getDescripcion().trim().equals("")) {
			log.info("---> Debe indicar la descripcion del producto");
			this.tipoMensaje = -11;
			return false;
		}
		
		if (this.pojoProductoNuevo.getUnidadMedida() <= 0)  {
			log.info("---> Producto sin unidad de medida");
			this.tipoMensaje = -19;
			return false;
		}
		
		if (this.pojoProductoNuevo.getFamilia() <= 0)  {
			log.info("---> Producto sin familia");
			this.tipoMensaje = -20;
			return false;
		}
		
		if (this.pojoProductoNuevo.getExistencia() < 0) {
			log.info("---> La existensia debe ser mayor o igual a cero");
			this.tipoMensaje = -16;
			return false;
		}

		if (this.pojoProductoNuevo.getMinimo() < 0) {
			log.info("---> El Minimo debe ser mayor o igual a cero");
			this.tipoMensaje = -12;
			return false;
		}

		if (this.pojoProductoNuevo.getMaximo() < 0) {
			log.info("---> El Maximo debe ser mayor o igual a cero");
			this.tipoMensaje = -13;
			return false;
		}
		
		if (this.pojoProductoNuevo.getMinimo() > this.pojoProductoNuevo.getMaximo()) {
			log.info("---> El Minimo debe ser menor al Maximo");
			this.tipoMensaje = -17;
			return false;
		}

		if (this.pojoProductoNuevo.getIdMoneda() <= 0) {
			log.info("---> Debe indicar la moneda");
			this.tipoMensaje = -21;
			return false;
		}
		
		/*if (this.pojoProductoNuevo.getPrecioCompra() == 0) {
			this.tipoMensaje = -14;
			log.info("---> Debe indicar el precio de compra");
			return false;
		}

		if (this.pojoProductoNuevo.getPrecioVenta() == 0) {
			log.info("---> Debe indicar el precio de venta");
			this.tipoMensaje = -15;
			return false;
		}*/
		
		// Validar la clave: Generamos la clave las veces que sea necesario
		do {
			intento += 1;
			log.info("---> Valido clave de producto : " + this.pojoProductoNuevo.getClave());
			claveRepetida = this.ifzProductos.validarClaveProducto(this.pojoProductoNuevo);
			if (claveRepetida) 
				this.pojoProductoNuevo.setClave(generaClave());
		} while(claveRepetida && intento <= maxIntentosClave);
		
		if (claveRepetida) {
			log.info("---> Producto clave repetida");
			this.tipoMensaje = -18;
			return false;
		}

		log.info("Producto validado");
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
				log.info("La lista indicada es nula o vacia.");
				return "";
			}
			
			if (id <= 0) {
				log.info("No indico el ID para poder obtener el valor de la lista.");
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
		
		if (! this.usuario.equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) {
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
		}
		
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

			log.info("Obteniendo especialidad ... ");
			this.pojoProductoNuevo.setEspecialidad(getEspecialidadFromFamilia(this.pojoProductoNuevo.getFamilia()));
			this.pojoProductoNuevo.setSubfamilia(0L);
			
			log.info("Codificando producto ... ");
			valor = getValorFromList(this.listEspecialidades, this.pojoProductoNuevo.getEspecialidad(), false);
			codigo += (valor != null && ! "".equals(valor)) ? valor : "";
			if (valor == null || "".equals(valor)) {
				log.info("Codificando producto nuevo ... No tengo especialidad :(");
				return;
			}
			
			valor = getValorFromList(this.listFamilias, this.pojoProductoNuevo.getFamilia(), false);
			codigo += (valor != null && ! "".equals(valor)) ? valor : "";
			if (valor == null || "".equals(valor)) {
				log.info("Codificando producto nuevo ... No tengo familia :(");
				return;
			}
			
			consecutivo = getConsecutivo(codigo);
			codigo = codigo + String.format("%1$0" + maxDigitos + "d", consecutivo);
			origenCodigo = this.pojoProductoNuevo.getEspecialidad() + "-" + this.pojoProductoNuevo.getFamilia() + "-0";
			log.info("Codificando producto nuevo ... terminado :) " + codigo + " :: " + origenCodigo);
		} catch (Exception e) {
			log.error("Error en Invetarios.ProductoAction.generaCodigoProducto: No pude codificar, genero codigo caduco", e);
			codigo = this.pojoProducto.getClave();
			if (codigo == null || "".equals(codigo) || ! codigo.startsWith("IZ"))
				codigo = generaClave();
			origenCodigo = "";
			log.info("Producto con codigo caduco ... terminado :( " + codigo);
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
				log.info("La lista indicada es nula o vacia.");
				return "";
			}
			
			if (id <= 0){
				log.info("No indico el ID para poder obtener el valor de la lista.");
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
	
	private void recuperarEgreso() {
		List<ConValores> listEgresos = new ArrayList<ConValores>();
		
		try {
			listEgresos = this.ifzConValores.buscaValorGrupo("descripcion", "ACTIVO", this.pojoGpoTipoEgresos);
			if (listEgresos != null && ! listEgresos.isEmpty())
				this.pojoEgresoActivo = listEgresos.get(0);
		} catch (Exception e) {
			log.error("ERROR al Recuperar Egreso --> ACTIVO", e);
		}
	}
	
    // --------------------------------------------------------------------------------------
    // Propiedades
    // --------------------------------------------------------------------------------------
	
	public String getRequisicionTitulo() {
		if (this.pojoRequisicion != null && this.pojoRequisicion.getId() != null && this.pojoRequisicion.getId() > 0L)
			return (this.pojoRequisicion.getTipo() == TipoMaestro.Administrativo.ordinal() ? "ADMINISTRATIVA " : "") + this.pojoRequisicion.getId();
		return (this.getEsAdministrativo() ? "ADMINISTRATIVA" : "");
	}
	
	public void setRequisicionTitulo(String value) {}
	
 	public boolean getEsAdministrativo() {
		if (this.tipoMaestro != null && this.tipoMaestro == TipoMaestro.Administrativo)
			return true;
		return false;
	}
	
	public void setEsAdministrativo(boolean value) {}
	
	public String getRequisicionObra() {
		if (this.pojoRequisicion != null && this.pojoRequisicion.getIdObra() != null)
			return this.pojoRequisicion.getIdObra().getId() + " - " + this.pojoRequisicion.getIdObra().getNombre();
		return "";
	}
	
	public void setRequisicionObra(String value) {}

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

	public Requisicion getPojoRequisicionMain() {
		try {
			if (this.pojoRequisicion != null)
				return this.ifzRequisiciones.findById(this.pojoRequisicion.getId());
		} catch (Exception e) {
			log.error("No puedo convertir el pojo extendido a Requisicion", e);
		}
		
		return new Requisicion();
	}
	
	public void setPojoRequisicionMain(Requisicion pojoRequisicion) {
		try {
			if (pojoRequisicion != null) {
				log.info("Extendiendo pojo Requisicion");
				this.pojoRequisicion = this.ifzRequisiciones.findExtById(pojoRequisicion.getId());
			}
		} catch (Exception e) {
			log.error("No puedo extender el pojo Requisicion", e);
		}
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
		return listReqDetalles;
	}

	public void setListReqDetalles(List<RequisicionDetalleExt> listReqDetalles) {
		this.listReqDetalles = listReqDetalles;
	}

	public List<SelectItem> getListEmpleadosItems() {
		return listEmpleadosItems;
	}

	public void setListEmpleadosItems(List<SelectItem> listEmpleadosItems) {
		this.listEmpleadosItems = listEmpleadosItems;
	}
	
	public RequisicionExt getPojoRequisicion() {
		return pojoRequisicion;
	}

	public void setPojoRequisicion(RequisicionExt pojoRequisicion) {
		this.pojoRequisicion = pojoRequisicion;
	}

	public Requisicion getPojoRequisicionBorrar() {
		return pojoRequisicionBorrar;
	}

	public void setPojoRequisicionBorrar(Requisicion pojoRequisicionBorrar) {
		this.pojoRequisicionBorrar = pojoRequisicionBorrar;
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
		return listReqDetallesEliminados;
	}
	
	public void setListReqDetallesEliminados(List<RequisicionDetalleExt> listReqDetallesEliminados) {
		this.listReqDetallesEliminados = listReqDetallesEliminados;
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

	public Requisicion getPojoRequisicionReporte() {
		return pojoRequisicionReporte;
	}

	public void setPojoRequisicionReporte(Requisicion pojoRequisicionReporte) {
		this.pojoRequisicionReporte = pojoRequisicionReporte;
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public boolean isPermiteEditar() {
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

	public List<Empleado> getListSolicitantes() {
		return listSolicitantes;
	}

	public void setListSolicitantes(List<Empleado> listSolicitantes) {
		this.listSolicitantes = listSolicitantes;
	}

	public Empleado getPojoSolicitante() {
		return pojoSolicitante;
	}

	public void setPojoSolicitante(Empleado pojoSolicitante) {
		this.pojoSolicitante = pojoSolicitante;
	}

	public List<SelectItem> getTiposBusquedaSolicitantes() {
		return tiposBusquedaSolicitantes;
	}

	public void setTiposBusquedaSolicitantes(List<SelectItem> tiposBusquedaSolicitantes) {
		this.tiposBusquedaSolicitantes = tiposBusquedaSolicitantes;
	}

	public String getCampoBusquedaSolicitantes() {
		return campoBusquedaSolicitantes;
	}

	public void setCampoBusquedaSolicitantes(String campoBusquedaSolicitantes) {
		this.campoBusquedaSolicitantes = campoBusquedaSolicitantes;
	}

	public String getValorBusquedaSolicitantes() {
		return valorBusquedaSolicitantes;
	}

	public void setValorBusquedaSolicitantes(String valorBusquedaSolicitantes) {
		this.valorBusquedaSolicitantes = valorBusquedaSolicitantes;
	}

	public int getNumPaginaSolicitantes() {
		return numPaginaSolicitantes;
	}

	public void setNumPaginaSolicitantes(int numPaginaSolicitantes) {
		this.numPaginaSolicitantes = numPaginaSolicitantes;
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
}


//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//2.2	  | 28/06/2016 | Daniel Azamar		| Corrección al apartado de guardar. Se comentó la linea que asigna la requisición a Null. En el método nuevo() se limpio la lista de requisiciones y el comprador se seteó a cero
