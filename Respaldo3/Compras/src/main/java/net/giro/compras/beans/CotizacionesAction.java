package net.giro.compras.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

import net.giro.adp.beans.Insumos;
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
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
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

import org.apache.commons.lang.StringUtils;
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
	private RequisicionRem ifzReq;
	private RequisicionDetalleRem ifzReqDetalle;
	private OrdenCompraRem ifzOC;
	// Impuestos IVA
	private ConGrupoValores pojoGpoImpuestos;
	private List<ConValores> listImpuestos;
	private List<SelectItem> listImpuestosItems;
	private long idImpuesto;
	private double porcentajeIva;
	// Almacen y Productos
	private ObraAlmacenesRem ifzObraAlmacen;
	private AlmacenProductoRem ifzInventario;
	// PreCotizacion
	private List<PreCotizacion> listPreCotizacionOrigen;
	private List<PreCotizacion> listPreCotizacion;
	private List<PreCotizacion> listPreCotizacionFull;
	private List<PreCotizacion> listPreCotizacionEliminados;
	private PreCotizacion pojoPreCotizacionBorrar;
	// Listas
	private List<Cotizacion> listCotizaciones;
	private List<CotizacionDetalleExt> listDetalles;
	private List<EmpleadoExt> listCompradores;
	private List<SelectItem> listCompradoresItems;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	// POJO's
	private ObraExt pojoObra;
	private long idCotizacion;
	private CotizacionExt pojoCotizacion;
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
    private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private List<String> stackTrace;
    private long usuarioId;
    private String usuario;
    private String usuarioEmail;
    private String usuarioEmailClave;
	private int numPaginaCotizaciones;
	private int numPaginaProductos;	
	private double subtotal;
	private double iva;
	private double total;	
	private long valGpoFamilias;
	private long valFamilia;
	private long valComprador;	
	private boolean seleccionarTodos;
	private boolean esNuevaCotizacion;
	private boolean editarCotizacion;
    private boolean permiteModificar;
	private String cotizacionOrdenCompra;
	private TipoMaestro tipoMaestro;
	private boolean esAdministrativo;
	private List<SelectItem> listEstatusCotizacionItems;
	private boolean origenFromRequisicion;
	private int estatusCotizacion;
	private boolean copiandoCotizacion;
	private List<Long> idPuestoCompradores;
	// Email
	private String email;
	private String emailCC;
	private String emailCCO;
	private String emailAsunto;
	private String emailCuerpo;
	// REQUISICIONES
	private Requisicion pojoRequisicion;
	private List<Requisicion> listRequisiciones;
	private List<SelectItem> reqOpcionesBusqueda;	
	private String reqCampoBusqueda;
	private String reqValorBusqueda;
	private int reqPaginacion;
	// Monedas
	private MonedaDAO ifzMonedas;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private long idMoneda;
	private long idMonedaBase;
	private long idMonedaSugerida;
	private double tipoCambio;
	// Solicitud Bodega
	private long idExplosionInsumos;
	private boolean procesoSolicitudBodega;
	private List<SolicitudBodega> listSolicitudes;
	private int numPaginaSolicitudBodega;
	private boolean solicitudBodega; 
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public CotizacionesAction() {
		PropertyResourceBundle msgProperties = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		String valPerfil = "";
		Moneda valMoneda = null;
		long valGrupo = 0;
		
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
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = this.paramsRequest.containsKey("DEBUG") ? true : false;
			
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
			this.ifzReq = (RequisicionRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalle = (RequisicionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzOC = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzMonedas = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzInventario = (AlmacenProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			
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
			
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			this.usuarioEmail = this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo();
			this.usuarioEmailClave = this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreoClave();
			if (this.usuarioEmailClave == null || "".equals(this.usuarioEmailClave.trim()))
				this.usuarioEmailClave = "disesa12";

			/* VALIDACION DE PERFILES */
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.esAdministrativo = ((valPerfil != null && "S".equals(valPerfil.trim())) ? true : false);

			valPerfil = this.entornoProperties.getString("SYS_CONFIG_COMPRADORES");
			if (valPerfil != null && ! "".equals(valPerfil.trim()))
				this.idPuestoCompradores = getPuestosCompradores(valPerfil);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			valMoneda = this.ifzMonedas.findByAbreviacion(valPerfil);
			if (valMoneda != null) {
				this.idMonedaSugerida = valMoneda.getId();
				this.idMonedaBase = valMoneda.getId();
				valMoneda = null;
			}

			/* impuestos */
			valPerfil = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			this.porcentajeIva = ((valPerfil != null && ! "".equals(valPerfil.trim())) ? Double.valueOf(valPerfil) : 0);
			if (this.entornoProperties.getString("SYS_IMPTOS") == null || "".equals(this.entornoProperties.getString("SYS_IMPTOS")))
				throw new Exception("No se encontro encontro el grupo SYS_IMPTOS en con_grupo_valores");
			valGrupo = Long.valueOf(this.entornoProperties.getString("SYS_IMPTOS"));
			this.pojoGpoImpuestos = this.ifzGpoVal.findById(valGrupo);
			cargarImpuestosIva();
			asignarImpuesto(this.porcentajeIva);
			
			/* Solicitud a Bodega */
			this.procesoSolicitudBodega = false;
			if (this.entornoProperties.getString("solicitud.bodega") != null && ! "".equals(this.entornoProperties.getString("solicitud.bodega").trim()))
				this.procesoSolicitudBodega = ("on".equals(this.entornoProperties.getString("solicitud.bodega").trim().toLowerCase()) ? true : false);
			
			/* Grupo de valores para Familias de productos */
			if (this.entornoProperties.getString("SYS_FAMILIA_PRODUCTO") == null || "".equals(this.entornoProperties.getString("SYS_FAMILIA_PRODUCTO")))
				throw new Exception("No se encontro encontro el grupo SYS_FAMILIA_PRODUCTO en con_grupo_valores");
			this.valGpoFamilias = Long.valueOf(this.entornoProperties.getString("SYS_FAMILIA_PRODUCTO"));
			this.pojoGpoFamilias = this.ifzGpoVal.findById(valGpoFamilias);
			
			// Inicializaciones
			this.listObrasGrid = new ArrayList<Obra>();
			this.listCotizaciones = new ArrayList<Cotizacion>();
			this.listPreCotizacion = new ArrayList<PreCotizacion>();
			this.listObras = new ArrayList<ObraExt>();
			this.pojoCotizacion = new CotizacionExt();
			this.listFamiliasItems = new ArrayList<SelectItem>();
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("nombre", "Nombre Obra"));
			this.tiposBusqueda.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusqueda.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusqueda.add(new SelectItem("nombreResponsable", "Encargado"));
			this.tiposBusqueda.add(new SelectItem("cot.folio", "Folio Cotizacion"));
			this.tiposBusqueda.add(new SelectItem("cot.nombreProveedor", "Proveedor Cotizacion"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			
			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
			
			// Busqueda Proveedor
			this.tiposBusquedaProveedores = new ArrayList<SelectItem>();
			this.tiposBusquedaProveedores.add(new SelectItem("nombre", "Nombre"));
			this.tiposBusquedaProveedores.add(new SelectItem("rfc", "RFC"));
			this.tiposBusquedaProveedores.add(new SelectItem("id", "ID"));
			nuevaBusquedaProveedores();
			
			// Busqueda requisiciones
			this.reqOpcionesBusqueda = new ArrayList<SelectItem>();
			this.reqOpcionesBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.reqOpcionesBusqueda.add(new SelectItem("nombreSolicita", "Comprador"));
			this.reqOpcionesBusqueda.add(new SelectItem("id", "ID"));
			nuevaBusquedaRequisiciones();
			
			// Estatus Cotizaciones
			this.listEstatusCotizacionItems = new ArrayList<SelectItem>();
			this.listEstatusCotizacionItems.add(new SelectItem(0, msgProperties.getString("estatus.activa")));
			this.listEstatusCotizacionItems.add(new SelectItem(1, msgProperties.getString("estatus.cancelada")));
			this.listEstatusCotizacionItems.add(new SelectItem(2, msgProperties.getString("estatus.suministrada")));
			this.estatusCotizacion = (int) this.listEstatusCotizacionItems.get(0).getValue();

			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			this.numPaginaCotizaciones = 1;
			this.numPaginaProductos = 1;
			this.tipoMaestro = TipoMaestro.Inventario;
			log.info("Solicitud a Bodega: " + (this.procesoSolicitudBodega ? "on" : "off"));
		} catch (Exception e) {
			control("Error en Compras.CotizacionAction.constructor CotizacionesAction", e);
			this.ctx = null;
		}
	}


	public void buscar() {
		List<Cotizacion> listAux = null;
		List<Long> listAdds = null;
		Obra obraAux = null;
		String orderBy = "";
		String campo = "";
		String valor = "";
		
		try {
    		control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.listObrasGrid = new ArrayList<Obra>();
			campo = this.campoBusqueda;
			campo = (campo.contains("cot.") && ! "".equals(valor)) ? campo : "*";
			valor = this.valorBusqueda.trim().replace(" ", "%");
			
			if (campo.contains("cot.")) {
				stackTrace("Buscamos obras por el folio de Cotizacion");
				listAux = this.ifzCotizaciones.findLikeProperty(campo.substring(4), valor, 0);
				if (listAux != null && ! listAux.isEmpty()) {
					listAdds = new ArrayList<Long>();
					this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
					for (Cotizacion var : listAux) {
						if (listAdds.contains(var.getIdObra())) 
							continue;
						obraAux = this.ifzObras.findById(var.getIdObra());
						if (! this.esAdministrativo && obraAux.isAdministrativa())
							continue;
						this.listObrasGrid.add(obraAux);
						listAdds.add(obraAux.getId());
					}
					
					listAux.clear();
					listAdds.clear();
				}
			} else {
				orderBy = "CASE model.estatus WHEN 0 THEN 1 ELSE 0 END";
				if (campo.contains("*"))
					orderBy += ", model.nombre";
				else
					orderBy += ", " + campo;
				stackTrace("Buscando obras. " + this.campoBusqueda + ":" + this.valorBusqueda);
				this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
				this.listObrasGrid = this.ifzObras.findLikeProperty(campo, valor, this.esAdministrativo, orderBy, 0);
			}
			
			if (this.listObrasGrid == null || this.listObrasGrid.isEmpty()) {
	    		control(2, "Busqueda sin resultados: " + this.campoBusqueda + " - " + this.valorBusqueda);
				return;
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar las Cotizaciones", e);
			if (this.listObrasGrid != null) 
				this.listObrasGrid.clear();
    	} finally {
    		if (this.listObrasGrid != null && ! this.listObrasGrid.isEmpty())
    			log.info(this.listObrasGrid.size() + " resultados encontrados.");
    	}
	}

	public void ver() {
		try {
    		control();
			stackTrace("Obra seleccionada: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
			if (this.listCompradores == null || this.listCompradores.isEmpty())
				cargarCompradores();
			
			this.numPaginaCotizaciones = 1;
			this.permiteModificar = true;
			if(this.pojoObra.getEstatus().longValue() == 0) 
				this.permiteModificar = false;
			this.tipoMaestro = (this.pojoObra.isAdministrativa() ? TipoMaestro.Administrativo : TipoMaestro.Inventario);

			// Recuperamos el ID de la Explosion de Insumos ligada si corresponde
			this.idExplosionInsumos = 0L;
			this.idExplosionInsumos = this.ifzInsumos.findIdActual(this.pojoObra.getId()); 
			this.listCotizaciones = new ArrayList<Cotizacion>();
			
			// Recuperamos las cotizaciones para esa obra
			stackTrace("Cargo Cotizaciones ... ");
			if (this.pojoObra.getId() != null && this.pojoObra.getId() > 0L) {
				stackTrace("Recuperando Cotizaciones por obra: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre());
				this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
				this.listCotizaciones = this.ifzCotizaciones.findAll(this.pojoObra.getId(), this.estatusCotizacion, "CASE model.sistema WHEN 0 THEN 1 WHEN 1 THEN 2 ELSE 3 END, CASE model.estatus WHEN 0 THEN 1 WHEN 2 THEN 2 WHEN 1 THEN 3 ELSE 4 END, model.fecha desc, model.id desc");
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar las Cotizaciones de la Obra seleccionada", e);
    	} finally {
			if (this.listCotizaciones == null)
				this.listCotizaciones = new ArrayList<Cotizacion>();
			log.info(this.listCotizaciones.size() + " Cotizaciones encontradas.");
    	}
	}

	public void evaluaNuevaCotizacion() {
		try {
			if (! verificaAlmacenesBodegaObra(false))
				return;
			if (getMaestroAdministrativo()) {
				nuevaBusquedaRequisiciones();
				this.esNuevaCotizacion = true;
				this.editarCotizacion = true;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar evaluar los parametros para la nueva Cotizacion", e);
		}
	}
	
	public void nuevoSinObra() {
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
    		control("Ocurrio un problema al intentar generar una nueva Cotizacion", e);
		}
	}
	
	public void nuevoRequisicion() {
		try {
    		control();
			this.origenFromRequisicion = true;
			nuevo();
		} catch (Exception e) {
    		control("Ocurrio un problema al intentar generar una nueva Cotizacion", e);
		}
	}
	
	public void nuevo() {
		try {
    		control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar la Obra seleccionada o no se selecciono ninguna Obra");
				return;
			}

			this.idCotizacion = 0L;
			this.copiandoCotizacion = false;
			this.esNuevaCotizacion = true;
			this.valComprador = 0L;
			this.editarCotizacion = true;
			this.cotizacionOrdenCompra = "";
			this.idMoneda = this.idMonedaSugerida;
			this.listPreCotizacion = new ArrayList<PreCotizacion>();
			this.listPreCotizacionEliminados = new ArrayList<PreCotizacion>();
			this.listSolicitudes = new ArrayList<SolicitudBodega>();
			cargarMonedas();
			
			this.pojoCotizacion = new CotizacionExt();
			this.pojoCotizacion.setIdObra(this.pojoObra);
			this.pojoCotizacion.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			cargarDetalles((this.origenFromRequisicion ? 2 : 1));
			
			if (this.pojoCotizacion.getIdMoneda() <= 0)
				this.pojoCotizacion.setIdMoneda(this.idMoneda);
			this.pojoCotizacion.setTipoCambio(1.0);
			if (this.pojoCotizacion.getIdMoneda() != this.loginManager.getInfoSesion().getEmpresa().getMonedaId())
				this.pojoCotizacion.setTipoCambio(this.loginManager.getTipoCambioActual());
			this.pojoCotizacion.setPorcentajeIva(this.porcentajeIva);
    	} catch (Exception e) {
    		control("Ocurrio un problema al intentar generar una nueva Cotizacion", e);
    	}
	}

	public void editar() {
		List<OrdenCompra> ocAux = null;
				
		try {
    		control();
			this.pojoCotizacion = this.ifzCotizaciones.findExtById(this.idCotizacion);
			if (this.pojoCotizacion == null || this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar la Cotizacion seleccionada");
				return;
			}

			this.esNuevaCotizacion = false;
			this.editarCotizacion = true;
			this.cotizacionOrdenCompra = "";
			this.listPreCotizacion = new ArrayList<PreCotizacion>();
			this.listPreCotizacionEliminados = new ArrayList<PreCotizacion>();
			this.listSolicitudes = new ArrayList<SolicitudBodega>();
			this.editarCotizacion = this.pojoCotizacion.getEstatus() == 3 ? false : this.editarCotizacion; // this.permiteModificar
			if (this.pojoCotizacion.getEstatus() == 2 || ! this.permiteModificar) {
				this.editarCotizacion = false;
				this.ifzOC.setInfoSesion(this.loginManager.getInfoSesion());
				ocAux = this.ifzOC.findByCotizacion(this.pojoCotizacion.getId(), false, 0);
				if (ocAux != null && ! ocAux.isEmpty())
					this.cotizacionOrdenCompra = ocAux.get(0).getFolio();
			}

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
				this.valComprador = this.pojoCotizacion.getIdComprador().getId();
			
			// Recuperamos quien solicita de la requisicion
			this.idMoneda = 0L;
			if (this.pojoCotizacion.getIdMoneda() > 0L)
				this.idMoneda = this.pojoCotizacion.getIdMoneda();
			this.tipoCambio = this.pojoCotizacion.getTipoCambio();
			
			// Recuperamos los detalles de la cotizacion
			cargarDetalles(0);
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar la Cotizacion", e);
    	} finally {
			nuevaBusquedaObras();
			nuevaBusquedaProveedores();
    	}
	}

	public void copiar() {
		try {
			control();
			// Lanzamos el metodo de edicion para recuperar todos los datos
			this.copiandoCotizacion = true;
			editar();
			this.copiandoCotizacion = false;
			if (this.operacionCancelada)
				return;

			// Asignamos tipo de cambio actual si corresponde
			this.tipoCambio = 1.0;
			if (this.pojoCotizacion.getIdMoneda() != this.loginManager.getInfoSesion().getEmpresa().getMonedaId())
				this.tipoCambio = this.loginManager.getTipoCambioActual();
			
			// Desligamos los detalles de la cotizacion original
			if (this.listPreCotizacion != null && ! this.listPreCotizacion.isEmpty()) {
				for (PreCotizacion var : this.listPreCotizacion) 
					var.setId(null);
			}
			
			stackTrace("Reseteo datos (id, folio, proveedor, contacto) ... ");
			this.pojoCotizacion.setId(null);
			this.pojoCotizacion.setFolio("");
			this.pojoCotizacion.setFecha(Calendar.getInstance().getTime());
			this.pojoCotizacion.setIdProveedor(null);
			this.pojoCotizacion.setTipoPersonaProveedor("");
			this.pojoCotizacion.setConsecutivoProveedor(0);
			this.pojoCotizacion.setIdContacto(null);
			this.pojoCotizacion.setFlete(0);
			this.pojoCotizacion.setTiempoEntrega(0);
			stackTrace("Cotizacion copiada");
		} catch (Exception e) {
    		control("Ocurrio un problema al copiar la Cotizacion indicada", e);
		} finally {
			this.numPaginaProductos = 1;
			this.copiandoCotizacion = false;
			this.esNuevaCotizacion = true;
			this.editarCotizacion = true;
			this.cotizacionOrdenCompra = "";
			nuevaBusquedaObras();
			nuevaBusquedaProveedores();
			totalizarCotizacionesDetalles();
		}
	}
	
	public void guardar() {
		boolean isNew = false;
		double margenTotal = 0;
		Cotizacion cotizacion = null;
		boolean existenciasActualizadas = false;
		String mensaje = "";
		
		try {
    		control();
			if (! validacionesCotizacion()) 
				return;

			// Revisamos los productos para comprobar la existencia
			comprobarExistencias();
			if (! this.operacionCancelada) 
				existenciasActualizadas = true;
			/*mensaje = comprobarCambiosExistencia();
			if (! "".equals(mensaje)) {
				control(-1, mensaje);
				return;
			}*/
			
			// Restauramos los detalles del respaldo
			this.valFamilia = 0L;
			filtrarProductos();
			this.listPreCotizacionFull = new ArrayList<PreCotizacion>();

			// Comprobamos la moneda
			stackTrace("Asignando moneda, tipo maestro, empresa, consecutivo proveedor y totales ... ");
			if (this.idMoneda <= 0L) 
				this.idMoneda = this.loginManager.getInfoSesion().getEmpresa().getMonedaId();
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
			this.pojoCotizacion.setSubtotal(this.subtotal);
			this.pojoCotizacion.setIva(this.iva);
			this.pojoCotizacion.setTotal(this.total);
			stackTrace("OK", true);
			
			if (this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L) {
				// Guardamos en la BD
				isNew = true;
				stackTrace("Guardamos Cotizacion nueva ... ");
				this.pojoCotizacion.setCreadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoCotizacion.setModificadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
				
    			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoCotizacion.setId(this.ifzCotizaciones.save(this.pojoCotizacion));
				this.idCotizacion = this.pojoCotizacion.getId();
				stackTrace("OK", true);
			} else {
				// Actualizamos Cotizacion
				isNew = false;
				stackTrace("Actualizamos Cotizacion ... ");
				this.pojoCotizacion.setModificadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
				
    			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzCotizaciones.update(this.pojoCotizacion);
				stackTrace("OK", true);
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
				// TODO: BACK OFFICE Eliminados
				this.listPreCotizacionEliminados.clear();
				stackTrace("OK", true);
			}

			// Actualizamos el Margen en la BD
			stackTrace("Actualizamos el Margen en la BD ... ");
			this.pojoCotizacion.setMargen(margenTotal);
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCotizaciones.update(this.pojoCotizacion);
			stackTrace("OK", true);
			
			// Recuperamos la Cotizacion y la añadimos a la lista si corresponde
			this.idCotizacion = this.pojoCotizacion.getId();
			cotizacion = this.ifzCotizaciones.findById(this.idCotizacion);
			if (this.pojoObra == null) 
				this.pojoObra = this.pojoCotizacion.getIdObra().Copia();
			
			if (isNew) {
				this.listCotizaciones.add(0, cotizacion);

				/* DESHABILITADO: Ya no se hace conteo de cotizados
				// Actualizamos el estatus de la requisicion si corresponde
				if (this.origenFromRequisicion && this.pojoRequisicion.getEstatus() != 2) {
					stackTrace("Actualizamos el estatus de la requisicion ... ");
					this.ifzCotizaciones.backOfficeRequisicion(this.pojoCotizacion.getId()); 
					stackTrace("OK", true);
				}*/
				
				mensaje = lanzarSBO();
				if (existenciasActualizadas)
					mensaje = "\nSe detecto un cambio en existencias de Almacenes.\nLa Cotizacion y/o Solicitud(es) a Bodega fueron actualizados\n\n" + mensaje;
			} else {
				// Buscamos la cotizacion en listado
				for (Cotizacion var : this.listCotizaciones) {
					if (var.getId().longValue() != this.idCotizacion) 
						continue;
					this.listCotizaciones.set(this.listCotizaciones.indexOf(var), cotizacion);
					break;
				}

				// Actualiza precio en Ordenes de Compras
				stackTrace("Actualiza precio en Ordenes de Compras ... ");
				this.ifzCotizaciones.backOfficeCotizacionPreciosOrdenCompra(this.pojoCotizacion.getId());
				stackTrace("OK", true);
			}

			log.info(printStackTrace("\n"));
			editar();
			control(13, mensaje);
		} catch (Exception e) {
			control("Ocurrio un problema al guardar la Cotizacion", e);
		}
	}
	
	public void cancelar() {
		Respuesta respuesta = null;
		
		try {
			control();
			if (this.idCotizacion <= 0L) {
				control(-1, "Debe indicar una Cotizacion para Cancelar");
				return;
			}
			
			// Establecemos los atributos para la cancelacion
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzCotizaciones.cancelar(this.idCotizacion, this.usuarioId);
			if (respuesta != null && respuesta.getErrores().getCodigoError() != 0L) {
				log.warn(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError());
				return;
			}

			this.pojoCotizacion = new CotizacionExt();
			this.ver();
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar cancelar la Cotizacion", e);
    	}
	}

	public void reporte() {
		Cotizacion cotizacion = null;
		byte[] contenidoReporte = null;
		String formatoDocumento = "";
		String nombreReporte = "";
		
		try {
    		control();
    		cotizacion = this.ifzCotizaciones.findById(this.idCotizacion);
			if (cotizacion == null || cotizacion.getId() == null || cotizacion.getId() <= 0L) {
				control(-1, "Debe guardar previamente la Cotizacion para poder exportar");
				return;
			}
			
			stackTrace("REPORTE COTIZACION: Preparamos parametros");
			if (cotizacion.getFolio() != null && ! "".equals(cotizacion.getFolio()))
				nombreReporte = "-" + cotizacion.getFolio();
			else if (cotizacion.getId() != null && cotizacion.getId() > 0L)
				nombreReporte = "-" + cotizacion.getId();
			nombreReporte = "COT" + nombreReporte;
			
			// Parametros del reporte
			HashMap<String, Object> paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idCotizacion", cotizacion.getId());

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

			stackTrace("REPORTE COTIZACION: Ejecutamos reporte");
			Respuesta respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				stackTrace("Error Interno - No se pudo ejecutar el reporte :: " + respuesta.getErrores().getDescError());
	    		control(-1, "Ocurrio un problema al intentar exportar la Cotizacion.\n501 - No se pudo ejecutar la peticion");
	    		return;
			}
			
			// Recogemos reporte
			stackTrace("REPORTE COTIZACION: Recuperamos contenido de reporte");
			contenidoReporte = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreReporte = nombreReporte + "." + formatoDocumento;
			if (contenidoReporte == null) {
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
		
		try {
			this.subtotal = 0;
			this.iva = 0;
			this.total = 0;
			if (this.listPreCotizacion == null || this.listPreCotizacion.isEmpty()) 
				return;
	
			valorIva = (this.porcentajeIva / 100);
			this.seleccionarTodos = true;
			for (PreCotizacion var : this.listPreCotizacion) {
				if (! var.isSeleccionado()) {
					this.seleccionarTodos = false;
					continue;
				}
				
				this.subtotal += var.getImporte();
				this.iva += (var.getImporte() * valorIva);
			}

			// Calculamos total de cotizacion
			this.total = this.subtotal + this.iva;
		} catch (Exception e) {
			control("Ocurrio un problema al calcular el Total de la Cotizacion", e);
		}
	}

	public void filtrarProductos() {
		// Inicializamos la lista respaldo si corresponde
		if (this.listPreCotizacionFull == null)
			this.listPreCotizacionFull = new ArrayList<PreCotizacion>();

		// Si el respaldo no tiene datos, lo llenamos
		if (this.listPreCotizacionFull.isEmpty()) {
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
		
		this.numPaginaProductos = 1;
		this.listPreCotizacion.clear();
		for (PreCotizacion var : this.listPreCotizacionFull) {
			if (var.getProductoFamiliaId() == this.valFamilia || this.valFamilia == 0L)
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
	
	public boolean validacionesCotizacion() {
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
		
		if (this.valComprador <= 0L) {
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
			if (this.valComprador == var.getId()) {
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
				cotDetalle.setCreadoPor(this.usuarioId);
			} 

			cotDetalle.setFechaModificacion(Calendar.getInstance().getTime());
			cotDetalle.setModificadoPor(this.usuarioId);
			// Añadimos a listado
			this.listDetalles.add(cotDetalle);
		}
		stackTrace("OK", true);
		
		return true;
	}
	
	private void cargarFamilias() {
		try {
			// Cargamos la lista de familias
			stackTrace("Cargamos lista de familias");
			this.listFamiliasItems = new ArrayList<SelectItem>();
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listFamilias = this.ifzConValores.buscaValorGrupo("descripcion", "", pojoGpoFamilias);
			if (this.listFamilias == null || this.listFamilias.isEmpty())
				return;
			
			// Generamos la lista auxiliar de familias
			stackTrace("Generamos lista de items [familias]");
			for (ConValores cv : this.listFamilias) {
				if (cv.getValor().equals(cv.getDescripcion()))
					this.listFamiliasItems.add(new SelectItem(cv.getId(), cv.getDescripcion()));
				else
					this.listFamiliasItems.add(new SelectItem(cv.getId(), cv.getDescripcion() + " (" + cv.getValor() + ")"));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al cargar las familias", e);
		}
	}
	
	private void filtrarFamilias() {
		if (this.listFamilias == null || this.listFamilias.isEmpty()) 
			return;
		
		Collections.sort(this.listFamilias, new Comparator<ConValores>() {
			@Override
			public int compare(ConValores o1, ConValores o2) {
				return o1.getDescripcion().compareTo(o2.getDescripcion());
			}
		});
		
		this.listFamiliasItems = new ArrayList<SelectItem>();
		for (ConValores cv : this.listFamilias) {
			if (cv.getValor().equals(cv.getDescripcion()))
				this.listFamiliasItems.add(new SelectItem(cv.getId(), cv.getDescripcion()));
			else
				this.listFamiliasItems.add(new SelectItem(cv.getId(), cv.getDescripcion() + " (" + cv.getValor() + ")"));
		}
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
	
	private void cargarCompradores() {
		try {
			// Cargamos la lista de compradores
			stackTrace("Cargamos lista de compradores [extendido]");
			this.listCompradoresItems = new ArrayList<SelectItem>();
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.listCompradores = this.ifzEmpleados.findAllPuestoExt(this.idPuestoCompradores);
			if (this.listCompradores == null || this.listCompradores.isEmpty()) 
				return;
			
			// Generamos la lista auxiliar de compradores
			stackTrace("Generamos lista de items [compradores]");
			for (EmpleadoExt var : this.listCompradores)
				this.listCompradoresItems.add(new SelectItem(var.getId(), var.getNombre()));
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Compradores", e);
		} finally {
			if (this.listCompradores == null)
				this.listCompradores = new ArrayList<EmpleadoExt>();
		}
	}
	
	private void cargarMonedas() {
		try {
			// Cargamos la lista de monedas
			stackTrace("Cargando lista de monedas");
			this.listMonedasItems = new ArrayList<SelectItem>();
			this.listMonedas = this.ifzMonedas.findAll();
			if (this.listMonedas != null && ! this.listMonedas.isEmpty()) {
				stackTrace("Generando lista de items (monedas)");
				for (Moneda var : this.listMonedas)
					this.listMonedasItems.add(new SelectItem(var.getId(), var.getNombre() + " (" + var.getAbreviacion() + ")"));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al cargar las Monedas", e);
		}
	}
	
	private void cargarImpuestosIva() {
		try {
			// Cargamos la lista de familias
			stackTrace("Cargamos lista de familias");
			this.listImpuestosItems = new ArrayList<SelectItem>();
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.listImpuestos = this.ifzConValores.buscaValorGrupo("descripcion", "", this.pojoGpoImpuestos);
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
				this.listImpuestosItems.add(new SelectItem(impuesto.getId(), impuesto.getDescripcion() + ((this.isDebug) ? " (" + impuesto.getAtributo1() + " %)" : "")));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al cargar los Impuestos IVA", e);
		}
	}
	
	private boolean verificaAlmacenesBodegaObra(boolean comprobarBodega) {
		List<ObraAlmacenes> almacenesObra = null;
		
		try {
    		control();
    		// ELIMINO Restriccion de Obras Administrativas 2019-09-02. Aut. Carolina Gaona
			/*if (this.pojoObra.isAdministrativa()) 
				return true;*/

			this.ifzObraAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			almacenesObra = this.ifzObraAlmacen.findAll(this.pojoObra.getId()); 
			if (almacenesObra == null || almacenesObra.isEmpty()) {
				control(11, "La obra no tiene asignados Almacenes generales");
				return false;
			}
			
			if (comprobarBodega) {
				for (ObraAlmacenes item : almacenesObra) {
					if (item.getAlmacenPrincipal() == 1)
						continue;
					if (item.getTipo() == 2)
						return true;
				}
				
				return false;
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al verificar la asignacion de Almacenes/Bodega de la Obra", e);
    		return false;
    	}
		
		return true;
	}

	private boolean comprobarPrimeraCotizacion() {
		boolean generarSBO = false;
		
		try {
			if (this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L)
				return false;
			if (this.procesoSolicitudBodega && validaRequest("SBO"))
				return true;
			
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			generarSBO = this.ifzCotizaciones.comprobarSolicitudBodega(this.pojoObra.getId());
			generarSBO = ! generarSBO; // Invertimos el valor obtenido para indicar si lanzamos o no la SBO
			generarSBO = (validaRequest("SBO") ? true : generarSBO);
		} catch (Exception e) {
			control("Ocurrio un problema al realizar comprobaciones para el proceso de Solicitud a Bodega", e);
			generarSBO = false;
		}
		
		return generarSBO;
	}

	@SuppressWarnings("unused")
	private void filtrarObrasSinInsumos() {
		List<Obra> resultado = new ArrayList<Obra>();
		List<Insumos> listaInsumos = null;

		try {
			this.ifzInsumos.setInfoSesion(this.loginManager.getInfoSesion());
			for (Obra var : this.listObrasGrid) {
				listaInsumos = this.ifzInsumos.findByProperty("idObra", var.getId(), 0);
				if (listaInsumos == null || listaInsumos.isEmpty()) continue;
				resultado.add(var);
			}
			
			this.listObrasGrid.clear();
			this.listObrasGrid.addAll(resultado);
		} catch (Exception e) {
			control("Ocurrio un problema al filtrar las Obras sin Explosion de Insumos", e);
		}
	}

	private void cargarDetalles(int origen) {
		List<PreCotizacion> detalles = null;
		boolean generarSBO = false;
		boolean hasId = false;
		
		try {
			if (origen == 0) {
				detalles = this.detallesFromCotizacion();
				this.seleccionarTodos = true;
				generarSBO = false;
				if (this.copiandoCotizacion) {
					this.listSolicitudes = new ArrayList<SolicitudBodega>();
					generarSBO = true;
				}
			} else if (origen == 1) {
				detalles = this.detallesFromExplosion();
				this.seleccionarTodos = false;
				generarSBO = comprobarPrimeraCotizacion();
			} else if (origen == 2) {
				detalles = this.detallesFromRequisicion();
				this.seleccionarTodos = true;
				generarSBO = true;
			} 

			if (detalles == null || detalles.isEmpty()) {
				this.seleccionarTodos = false;
				generarSBO = false;
				if ("".equals(this.mensaje.trim()))
					control(-1, "Ocurrio un problema al recuperar los detalles para la Cotizacion");
				else
					control(-1, "No se pudo generar la Cotizacion.\n" + this.mensaje);
				return;
			}
			
			if (this.listFamilias == null || this.listFamilias.isEmpty())
				cargarFamilias();
			else
				filtrarFamilias();
			
			Collections.sort(detalles, new Comparator<PreCotizacion>() {
				@Override
				public int compare(PreCotizacion o1, PreCotizacion o2) {
					return o1.getIdProducto().getClave().compareTo(o2.getIdProducto().getClave());
				}
			});
			this.listPreCotizacionOrigen = new ArrayList<PreCotizacion>();
			this.listPreCotizacionOrigen.addAll(detalles);
			this.solicitudBodega = false;
			
			hasId = (this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L);
			hasId = (hasId && this.copiandoCotizacion ? false : hasId);
			generarSBO = (validaRequest("SBO") ? true : generarSBO);
			if (generarSBO && ! hasId) {
				//generarSBO = (this.pojoObra.getTipoObra() == 4 ? false : generarSBO); // ELIMINO Restriccion de Obras Administrativas
				generarSBO = (origen <= 0 ? false : generarSBO);
				if (generarSBO && ! verificaAlmacenesBodegaObra(true)) {
					control(101, "No se puede generar Solicitud a Bodega.\nLa obra no tiene asignada una Bodega");
					generarSBO = false;
				}
				
				if (generarSBO) 
					detalles = this.existenciasAlmacenes(this.pojoCotizacion.getIdObra().getId(), detalles);
			}
			
			if (! detalles.isEmpty())
				this.listPreCotizacion.addAll(detalles);
			this.solicitudBodega = (this.listSolicitudes.size() > 0);
			if (this.listPreCotizacion.isEmpty() && this.solicitudBodega) {
				control(100, "El material solicitado puede ser cubierto completamente por los Almacenes en Solicitudes a Bodega. Procede con SBO?");
				return;
			}
		} catch (Exception e) {
			control(-1, "Ocurrio un problema al recuperar los detalles para la Cotizacion");
		} finally {
			this.numPaginaProductos = 1;
			totalizarCotizacionesDetalles();
		}
	}

	private List<PreCotizacion> detallesFromCotizacion() {
		List<CotizacionDetalleExt> lista = null;
		List<PreCotizacion> detalles = null;
		List<Long> familiasId = null;
		long idFamiliaProducto = 0L;
		
		try {
			// Recuperamos los productos de la cotizacion
			lista = this.ifzCotiDetalles.findExtAll(this.pojoCotizacion.getId()); 
			if (lista == null || lista.isEmpty()) {
				control(-1, "Ocurrio un problema al recuperar los detalles de la Cotizacion");
				return detalles;
			}

			// Generamos los items para la pantalla
			detalles = new ArrayList<PreCotizacion>();
			this.listFamilias = new ArrayList<ConValores>();
			familiasId = new ArrayList<Long>();
			for (CotizacionDetalleExt var : lista) {
				detalles.add(new PreCotizacion(var));
				idFamiliaProducto = (var.getIdProducto().getFamilia() != null && var.getIdProducto().getFamilia().getId() > 0L) ? var.getIdProducto().getFamilia().getId() : 0L;
				if (idFamiliaProducto > 0L && ! familiasId.contains(idFamiliaProducto)) {
					familiasId.add(idFamiliaProducto);
					this.listFamilias.add(var.getIdProducto().getFamilia());
				}
			}
			
			// Recuperamos Solicitud a Bodega, previamente generada si corresponde
			this.listSolicitudes = this.ifzCotizaciones.solicitudBodega(this.pojoCotizacion.getId());
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los detalles de la Cotizacion", e);
			detalles = null;
		} finally {
			if (this.listSolicitudes == null)
				this.listSolicitudes = new ArrayList<SolicitudBodega>();
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
		// -----------------------------------------
		long idModenaProducto = 0L;
		long idFamiliaProducto = 0L;
		
		try {
			control();
			// Comprobamos los insumos de la obra
			stackTrace("Comprobamos Explosion de Insumos");
			this.ifzInsumos.setInfoSesion(this.loginManager.getInfoSesion());
			explosionInsumos = this.ifzInsumos.findByIdExt(this.idExplosionInsumos);//findExtActual(this.pojoCotizacion.getIdObra().getId()); 
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
			
			// Comprobamos el porcentaje de suministro de la Explosion de Insumos
			stackTrace("Comprobamos el porcentaje de suministro de la Explosion de Insumos");
			insumosValidos = new ArrayList<InsumosDetallesExt>();
			this.listFamilias = new ArrayList<ConValores>();
			familiasId = new ArrayList<Long>();
			for (InsumosDetallesExt insumo : insumosDetalles) {
				if (insumo.getTipo() > TipoInsumo.Material.ordinal()) 
					continue;
				if (this.idMoneda <= 0L){
					idModenaProducto = (insumo.getIdProducto().getIdMoneda() != null && insumo.getIdProducto().getIdMoneda().getId() > 0L) ? insumo.getIdProducto().getIdMoneda().getId() : this.idMonedaBase;
					this.idMoneda = idModenaProducto;
				}
				
				if (insumo.getPendiente() > 0) {
					insumosValidos.add(insumo);
					idFamiliaProducto = (insumo.getIdProducto().getFamilia() != null && insumo.getIdProducto().getFamilia().getId() > 0L) ? insumo.getIdProducto().getFamilia().getId() : 0L;
					if (idFamiliaProducto > 0L && ! familiasId.contains(idFamiliaProducto)) {
						familiasId.add(idFamiliaProducto);
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
				detalle = new PreCotizacion(insumo);//, insumo.getIdProducto().getExistencia());
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
		// -----------------------------------------
		long idModenaProducto = 0L;
		long idFamiliaProducto = 0L;
		
		try {
			control();
			// Comprobamos requisicion
			if (this.pojoRequisicion == null || this.pojoRequisicion.getId() == null || this.pojoRequisicion.getId() <= 0L) {
				control(-1, "No selecciono ninguna Requisicion");
				return detalles;
			}
			
			// Recuperamos el detalle de la requisicion
			stackTrace("Recuperamos detalles de Requisicion");
			this.ifzReqDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			requisicionDetalles = this.ifzReqDetalle.findExtAll(this.pojoRequisicion.getId()); 
			if (requisicionDetalles == null || requisicionDetalles.isEmpty()) {
				control(-1, "La Requisicion no tiene productos asignados");
				return detalles;
			}
			
			auxiliar = new ArrayList<RequisicionDetalleExt>();
			for (RequisicionDetalleExt item : requisicionDetalles) {
				if (this.idMoneda <= 0L) {
					idModenaProducto = (item.getIdProducto().getIdMoneda() != null && item.getIdProducto().getIdMoneda().getId() > 0L) ? item.getIdProducto().getIdMoneda().getId() : this.idMonedaBase;
					this.idMoneda = idModenaProducto;
				}
				
				if (item.getPendiente() <= 0)
					continue;
				if (item.getEstatus() == 1)
					continue;
				auxiliar.add(item);
			}
			requisicionDetalles.clear();
			requisicionDetalles.addAll(auxiliar);
			if (requisicionDetalles == null || requisicionDetalles.isEmpty()) {
				control(-1, "La Requisicion no tiene productos disponibles");
				return detalles;
			}
			
			detalles = new ArrayList<PreCotizacion>();
			this.listFamilias = new ArrayList<ConValores>();
			familiasId = new ArrayList<Long>();
			for (RequisicionDetalleExt item : requisicionDetalles) {
				stackTrace("Preparo detalle ... #" + (requisicionDetalles.indexOf(item) + 1));
				detalle = new PreCotizacion(item);
				
				stackTrace("conversion de moneda");
				idModenaProducto = (item.getIdProducto().getIdMoneda() != null && item.getIdProducto().getIdMoneda().getId() > 0L) ? item.getIdProducto().getIdMoneda().getId() : this.idMonedaBase;
				if (this.idMonedaBase == this.pojoCotizacion.getIdMoneda() && this.idMonedaBase != idModenaProducto) {
					precioUnitario = item.getIdProducto().getPrecioCompra();
					tipoCambio = this.loginManager.getTipoCambioActual();
					precioUnitario = precioUnitario * tipoCambio;
					detalle.setPrecioUnitario(precioUnitario);
				} else if (this.idMonedaBase != this.pojoCotizacion.getIdMoneda() && this.idMonedaBase == idModenaProducto) {
					precioUnitario = item.getIdProducto().getPrecioCompra();
					tipoCambio = this.loginManager.getTipoCambioActual();
					precioUnitario = precioUnitario / tipoCambio;
					detalle.setPrecioUnitario(precioUnitario);
				} else {
					precioUnitario = item.getIdProducto().getPrecioCompra();
					tipoCambio = 1.0;
					detalle.setPrecioUnitario(precioUnitario);
				}
				
				idFamiliaProducto = (item.getIdProducto().getFamilia() != null && item.getIdProducto().getFamilia().getId() > 0L) ? item.getIdProducto().getFamilia().getId() : 0L;
				if (idFamiliaProducto > 0L && ! familiasId.contains(idFamiliaProducto)) {
					familiasId.add(idFamiliaProducto);
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
		if (mensaje == null || "".equals(mensaje.trim()) && tipoMensaje == 0)
			mensaje = "Ocurrio un problema no controlado al procesar la cotizacion";
		stackTrace(mensaje);
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n\nError COTIZACIONES :: " + this.usuario + "\n" + this.tipoMensaje + " - " + this.mensaje + printStackTrace("\n"), throwable);
	}
	
	private void stackTrace(String mensaje) {
		stackTrace(mensaje, false);
	}

	private void stackTrace(String mensaje, boolean append) {
		if (this.stackTrace == null)
			this.stackTrace = new ArrayList<String>();
		
		if (mensaje == null || "".equals(mensaje.trim()))
			return;
		
		if (! append || (append && this.stackTrace.isEmpty())) {
			this.stackTrace.add(mensaje);
			return;
		}
		
		mensaje = this.stackTrace.get(this.stackTrace.size() - 1) + mensaje;
		this.stackTrace.set((this.stackTrace.size() - 1), mensaje);
	}
	
	private String printStackTrace(String separador) {
		if (this.stackTrace == null)
			this.stackTrace = new ArrayList<String>();
		if (this.stackTrace.isEmpty())
			return "";
		if (separador == null || "".equals(separador.trim()))
			separador = ",";
		return "\nSTACKTRACE:\n" + StringUtils.join(this.stackTrace, separador) + "\n";
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
			params.put("usuario", 		  this.usuario);
			
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
			
			cco = this.email.replace(" ", "");
			this.email = popReceiverTO(cco);
			cco = popReceiversBCC(cco);
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
	
	private String popReceiverTO(String email) {
		String[] splitted = null;
		
		if (email == null || "".equals(email))
			return "";
		if (! email.contains(","))
			return email;
		splitted = email.split(",");
		email = splitted[0];
		return email;
	}
	
	private String popReceiversBCC(String email) {
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
		if (generadas == 1)
			mensaje = "Se genero 1 Solicitud a Bodega";
		if (generadas > 1)
			mensaje = "Se generaron " + generadas + " Solicitudes a Bodega";
		return mensaje;
	}
	
	private List<Long> encontrarAlmacenes(long idObra) {
		List<Long> almacenes = new ArrayList<Long>();
		// ------------------------------------------------
		List<ObraAlmacenesExt> asignados = null;
		
		try {
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
				item.setExistencias(0);
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
		} 
		
		return items;
	}

	public void comprobarExistencias() {
		List<PreCotizacion> detalles = null;
		boolean generarSBO = false;
		// ----------------------------------------------
		List<PreCotizacion> cotizacionPrevia = null;
		List<SolicitudBodega> solicitudesPrevias = null;
		int cotiHashCode = 0; 
		int soliHashCode = 0; 

		try {
			control();
			if (! this.procesoSolicitudBodega && ! validaRequest("SBO")) 
				return;
			
			// Si es una edicion, no debe comprobar existencias
			// ------------------------------------------------------------------------------------------
			if (this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L)
				return;
			
			// Relanzamos el proceso de verificacion de existencias con los detalles iniciales
			// ------------------------------------------------------------------------------------------
			generarSBO = this.procesoSolicitudBodega || validaRequest("SBO");
			if (generarSBO && ! verificaAlmacenesBodegaObra(true)) {
				control(-1, "No se puede generar Solicitud a Bodega.\nLa obra no tiene asignada una Bodega");
				return;
			}

			if (! generarSBO)
				return;

			// Respaldamos detalles (Cotizacion y Solicitudes)
			// ------------------------------------------------------------------------------------------
			cotizacionPrevia = new ArrayList<PreCotizacion>();
			cotizacionPrevia.addAll(this.listPreCotizacion);
			cotiHashCode = this.listPreCotizacion.hashCode();
			this.listPreCotizacion.clear();

			solicitudesPrevias = new ArrayList<SolicitudBodega>();
			solicitudesPrevias.addAll(this.listSolicitudes);
			soliHashCode = this.listSolicitudes.hashCode();
			this.listSolicitudes.clear();

			detalles = new ArrayList<PreCotizacion>();
			detalles.addAll(this.listPreCotizacionOrigen);
			this.listPreCotizacion = this.existenciasAlmacenes(this.pojoCotizacion.getIdObra().getId(), detalles);
			this.solicitudBodega = this.listSolicitudes.size() > 0;
			log.info("HashCodes COTIZACION :: " + cotiHashCode + " :: " + this.listPreCotizacion.hashCode());
			log.info("HashCodes SOLICITUD  :: " + soliHashCode + " :: " + this.listSolicitudes.hashCode());
			
			// Alertamos si hubo cambios
			// ------------------------------------------------------------------------------------------
			if ((cotiHashCode != this.listPreCotizacion.hashCode()) || (soliHashCode != this.listSolicitudes.hashCode())) {
				if (this.listPreCotizacion.isEmpty() && this.solicitudBodega) {
					control(100, "Todo el material esta cubierto en Solicitud(es) a Bodega");
					return;
				}
				
				control(-1, "Se detecto un cambio en existencias de Almacenes.\nLa Cotizacion y/o Solicitud(es) a Bodega ya fueron actualizados");
			}
		} catch (Exception e) {
			control("Ocurrio un problema al comprobar la existencia de los insumos en Almacenes", e);
		} 
	}
	
	@SuppressWarnings("unused")
	private String comprobarCambiosExistenciaOLD() {
		List<PreCotizacion> detalles = null;
		boolean generarSBO = false;
		// ----------------------------------------------
		List<PreCotizacion> cotizacionPrevia = null;
		List<SolicitudBodega> solicitudesPrevias = null;
		// ----------------------------------------------
		boolean cambioCotizacion = false;
		boolean cambioSolicitud = false;
		boolean lanzar = false;
		String mensaje = "";

		try {
			lanzar = this.procesoSolicitudBodega;
			if (validaRequest("SBO"))
				lanzar = true;
			// Validamos si procedemos a realizar la comprobacion de existencias en Almacenes
			if (! lanzar || this.pojoObra.getTipoObra() == 4)
				return mensaje;
			
			// Si es una edicion, no debe comprobar existencias
			// ------------------------------------------------------------------------------------------
			if (this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L)
				return mensaje;
			
			// Respaldamos detalles (Cotizacion y Solicitudes)
			// ------------------------------------------------------------------------------------------
			cotizacionPrevia = new ArrayList<PreCotizacion>();
			cotizacionPrevia.addAll(this.listPreCotizacion);
			this.listPreCotizacion.clear();

			solicitudesPrevias = new ArrayList<SolicitudBodega>();
			solicitudesPrevias.addAll(this.listSolicitudes);
			this.listSolicitudes.clear();
			
			// Relanzamos el proceso de verificacion de existencias con los detalles iniciales
			// ------------------------------------------------------------------------------------------
			detalles = new ArrayList<PreCotizacion>();
			generarSBO = validaRequest("SBO");
			this.solicitudBodega = false;
			generarSBO = (this.pojoObra.getTipoObra() == 4 ? false : generarSBO);
			if (generarSBO && ! verificaAlmacenesBodegaObra(true)) {
				control(-1, "No se puede generar Solicitud a Bodega.\nLa obra no tiene asignada una Bodega");
				generarSBO = false;
			}
			
			if (generarSBO) {
				detalles = this.existenciasAlmacenes(this.pojoCotizacion.getIdObra().getId(), this.listPreCotizacionOrigen);
				this.solicitudBodega = this.listSolicitudes.size() > 0;
			}

			// Comprobamos cambios en Cotizacion
			// ------------------------------------------------------------------------------------------
			cambioCotizacion = false;
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
			
			if (! cambioCotizacion && ! this.solicitudBodega) {
				detalles.addAll(cotizacionPrevia);
				for (PreCotizacion detalle : detalles) 
					detalle.setExistencias(0);
			}
			
			// Solicitudes
			// ------------------------------------------------------------------------------------------
			cambioSolicitud = false;
			if (solicitudesPrevias.isEmpty() && (this.listSolicitudes != null && ! this.listSolicitudes.isEmpty())) // No tenia solicitud, y ahora se generaron
				cambioSolicitud = true;
			else if (! solicitudesPrevias.isEmpty() && (this.listSolicitudes != null && this.listSolicitudes.isEmpty())) // Tenia solicitud, y ahora ya no
				cambioSolicitud = true;
			if (! cambioSolicitud) 
				cambioSolicitud = comprobarCambiosSolicitudes(solicitudesPrevias, this.listSolicitudes);
			if (! cambioSolicitud)
				this.listSolicitudes.addAll(solicitudesPrevias);

			// Alertamos si hubo cambios
			// ------------------------------------------------------------------------------------------
			if (cambioCotizacion || cambioSolicitud) 
				mensaje = "Se detecto un cambio en existencias de Almacenes.\nLas cantidades en Cotizacion y/o Solicitud a Bodega fueron actualizadas";
			
			// Actualizamos listado
			this.listPreCotizacion.addAll(detalles);
			if (this.listPreCotizacion.isEmpty() && this.solicitudBodega) {
				control(100, "Todo el material esta cubierto en Solicitud(es) a Bodega");
				return "";
			}
		} catch (Exception e) {
			control("Ocurrio un problema al comprobar la existencia de los insumos en Almacenes", e);
			mensaje = "Ocurrio un problema al comprobar la existencia de los insumos en Almacenes";
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
	
	// --------------------------------------------------------------------------------------	
	// BUSQUEDA OBRAS
	// --------------------------------------------------------------------------------------
	
	public void nuevaBusquedaObras() {
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.valorBusquedaTipoObra = 0;
		this.numPaginaObras = 1;
		
		if (this.listObras != null)
			this.listObras.clear();
    }
	
	public void buscarObras() {
		String valor = "";
		
		try {
			control();
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();

			stackTrace("Buscando obras. " + this.campoBusquedaObras + ":" + this.valorBusquedaObras);
			valor = this.valorBusquedaObras.trim().replace(" ", "%");
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.listObras = this.ifzObras.findLikePropertyExt(this.campoBusquedaObras, valor, this.valorBusquedaTipoObra, "", 0);
			if(this.listObras.isEmpty()) {
				stackTrace("Buscando obras. " + this.campoBusquedaObras + ":" + this.valorBusquedaObras);
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al realizar la busqueda de Obras", e);
			if (this.listObras != null) this.listObras.clear();
    	}
	}

	public void seleccionarObra() {
		// Recuperamos los insumos de la obra seleccionada
		nuevoInsumos();
		nuevaBusquedaObras();
	}

	// --------------------------------------------------------------------------------------	
	// REQUISICIONES
	// --------------------------------------------------------------------------------------	
	
	public void nuevaBusquedaRequisiciones() { 
		this.reqCampoBusqueda = this.reqOpcionesBusqueda.get(0).getValue().toString();
		this.reqValorBusqueda = "";
		this.reqPaginacion = 1;
		
		this.listRequisiciones = new ArrayList<Requisicion>();
		this.pojoRequisicion = new Requisicion();
	}
	
	public void buscarRequisiciones() { 
		String valor = "";
		
		try {
			control();
			if (this.reqCampoBusqueda == null || "".equals(this.reqCampoBusqueda.trim()))
				this.reqCampoBusqueda = this.reqOpcionesBusqueda.get(0).getValue().toString();
			
			this.reqPaginacion = 1;
			valor = this.reqValorBusqueda.trim().replace(" ", "%");
			this.ifzReq.setInfoSesion(this.loginManager.getInfoSesion());
			this.listRequisiciones = this.ifzReq.findLikeProperty(this.reqCampoBusqueda, valor, this.pojoObra.getId(), this.tipoMaestro.ordinal(), false, false, 0L, "", 0);
			if (this.listRequisiciones == null || this.listRequisiciones.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al realizar la busqueda de Requisiciones", e);
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
			
			requisicion = this.ifzReq.findExtById(this.pojoRequisicion.getId());
			requisicionDetalles = this.ifzReqDetalle.findAll(this.pojoRequisicion.getId()); 
			if (requisicionDetalles == null || requisicionDetalles.isEmpty()) {
				control(-1, "La Requisicion no tiene productos asignados");
				return;
			}

			auxiliar = new ArrayList<RequisicionDetalle>();
			for (RequisicionDetalle detalle : requisicionDetalles) {
				if (detalle.getPendiente() <= 0)
					continue;
				if (detalle.getEstatus() == 1)
					continue;
				auxiliar.add(detalle);
			}
			
			if (requisicionDetalles == null || requisicionDetalles.isEmpty()) {
				control(-1, "La Requisicion no tiene productos disponibles");
				return;
			}

			nuevoRequisicion();
			// Asigno moneda
			this.pojoCotizacion.setIdMoneda(this.pojoRequisicion.getIdMoneda());
			// Añadimos la requisicion a la Cotizacion
			this.pojoCotizacion.setIdRequisicion(requisicion);
			// Asigno comprador
			this.valComprador = 0L;
			comprador = this.ifzEmpleados.findByIdExt(this.pojoRequisicion.getIdSolicita());
			if (comprador != null && comprador.getId() != null && comprador.getId() > 0L) {
				this.pojoCotizacion.setIdComprador(comprador);
				this.valComprador = comprador.getId();
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al recuperar la Requisicion seleccionada", e);
    	}
	}
	
	// --------------------------------------------------------------------------------------	
	// BUSQUEDA PROVEEDORES
	// --------------------------------------------------------------------------------------	
	
	public void nuevaBusquedaProveedores() {
		this.campoBusquedaProveedores = this.tiposBusquedaProveedores.get(0).getValue().toString();
		this.valorBusquedaProveedores = "";
		this.valorBusquedaTipoProveedor = "N";
		this.numPaginaProveedores = 1;
		
		if (this.listProveedores != null)
			this.listProveedores.clear();
    }

	public void buscarProveedores() {
		String valor = "";
		
		try {
			control();
			if ("".equals(this.valorBusquedaTipoProveedor))
				this.valorBusquedaTipoProveedor = "N";
			
			if ("".equals(this.campoBusquedaProveedores))
				this.valorBusquedaProveedores = "id";

			stackTrace("Buscando obras. " + this.campoBusquedaProveedores + ":" + this.valorBusquedaProveedores);
			valor = this.valorBusquedaProveedores.trim().replace(" ", "%");
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			this.listProveedores = this.ifzCotizaciones.findPersonaLikeProperty(this.campoBusquedaProveedores, valor, this.valorBusquedaTipoProveedor, 80);
			if(this.listProveedores.isEmpty()){
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la busqueda de Proveedores", e);
			if (this.listProveedores != null) this.listProveedores.clear();
    	}
	}

	public void seleccionarProveedor() {
		PersonaExt pojoContacto = null;
		
		try {
			control();
			// Asigno el tipo de persona de proveedor: Persona (P) o Negocio (N)
			this.pojoCotizacion.setTipoPersonaProveedor(this.valorBusquedaTipoProveedor);
			// Recuperamos el contacto del proveedor seleccionado
			stackTrace("Recuperando contacto para proveedor: " + this.pojoCotizacion.getIdProveedor().getId() + " - " + this.pojoCotizacion.getIdProveedor().getNombre());
			this.ifzCotizaciones.setInfoSesion(this.loginManager.getInfoSesion());
			pojoContacto = this.ifzCotizaciones.findContactoByProveedor(this.pojoCotizacion.getIdProveedor(), this.valorBusquedaTipoProveedor);
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
    // ESTATUS
    // --------------------------------------------------------------------------------------
	
	public void estatusOrdenes() {
		
	}
	
	// --------------------------------------------------------------------------------------
    // PROPIEDADES
    // --------------------------------------------------------------------------------------
	
	public String getObra() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		return "";
	}
	
	public void setObra(String value) {}
	
	public boolean getNuevaCotizacion() {
		if (this.pojoCotizacion == null || this.pojoCotizacion.getId() == null || this.pojoCotizacion.getId() <= 0L)
			return true;
		return false;
	}
	
	public void setNuevaCotizacion(boolean value) {}
	
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
	
	public String getCotizacionProveedor() {
		if (this.pojoCotizacion != null) {
			if (this.pojoCotizacion.getIdProveedor() != null && this.pojoCotizacion.getIdProveedor().getId() > 0L)
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
	
	public Obra getObraMain() {
		try {
			if (this.pojoObra != null) 
				return this.ifzObras.convertir(this.pojoObra);
		} catch (Exception e) {
			control("No puedo convertir el pojo extendido a Obra", e);
		}
		
		return new Obra();
	}
	
	public void setObraMain(Obra pojoObra) {
		try {
			if (pojoObra != null) {
				stackTrace("Extendiendo pojo Obra");
				this.pojoObra = this.ifzObras.convertir(pojoObra);
			}
		} catch (Exception e) {
			control("No puedo extender el pojo Obra", e);
		}
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
		return operacionCancelada;
	}

	public void setOperacion(boolean operacion) {
		this.operacionCancelada = operacion;
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
	
	public long getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public CotizacionExt getPojoCotizacion() {
		return pojoCotizacion;
	}

	public void setPojoCotizacion(CotizacionExt pojoCotizacion) {
		this.pojoCotizacion = pojoCotizacion;
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

	public List<PreCotizacion> getListCotiDetalles() {
		return listPreCotizacion;
	}

	public void setListCotiDetalles(List<PreCotizacion> listPreCotizacion) {
		this.listPreCotizacion = listPreCotizacion;
	}

	public List<PreCotizacion> getListCotiDetallesEliminados() {
		return listPreCotizacionEliminados;
	}

	public void setListCotiDetallesEliminados(List<PreCotizacion> listPreCotizacionEliminados) {
		this.listPreCotizacionEliminados = listPreCotizacionEliminados;
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

	public List<SelectItem> getListMonedasItems() {
		return listMonedasItems;
	}

	public void setListMonedasItems(List<SelectItem> listMonedasItems) {
		this.listMonedasItems = listMonedasItems;
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
	
	public PreCotizacion getPojoCotiDetalleBorrar() {
		return pojoPreCotizacionBorrar;
	}
	
	public void setPojoCotiDetalleBorrar(PreCotizacion pojoPreCotizacionBorrar) {
		this.pojoPreCotizacionBorrar = pojoPreCotizacionBorrar;
	}

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

	public boolean isEsNuevaCotizacion() {
		return esNuevaCotizacion;
	}

	public void setEsNuevaCotizacion(boolean esNuevaCotizacion) {
		this.esNuevaCotizacion = esNuevaCotizacion;
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

	public String getCotizacionRequisicion() {
		if (this.pojoCotizacion != null && this.pojoCotizacion.getIdRequisicion() != null && this.pojoCotizacion.getIdRequisicion().getId() != null && this.pojoCotizacion.getIdRequisicion().getId() > 0L)
			return "Requisicion: " + this.pojoCotizacion.getIdRequisicion().getId().toString();
		return "";
	}

	public void setCotizacionRequisicion(String value) {}
	
	public String getCotizacionOrdenCompra() {
		if (this.cotizacionOrdenCompra != null && ! "".equals(this.cotizacionOrdenCompra))
			return "Orden de Compra: " + this.cotizacionOrdenCompra;
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

	public void setListEstatusCotizacionItems(List<SelectItem> listEstatusCotizacionItems) {
		this.listEstatusCotizacionItems = listEstatusCotizacionItems;
	}

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

	public boolean isSolicitudBodega() {
		return solicitudBodega;
	}

	public void setSolicitudBodega(boolean solicitudBodega) {
		this.solicitudBodega = solicitudBodega;
	}

	public long getIdExplosionInsumos() {
		return idExplosionInsumos;
	}

	public void setIdExplosionInsumos(long idExplosionInsumos) {
		this.idExplosionInsumos = idExplosionInsumos;
	}

	public List<ConValores> getListImpuestos() {
		return listImpuestos;
	}

	public void setListImpuestos(List<ConValores> listImpuestos) {
		this.listImpuestos = listImpuestos;
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
}
