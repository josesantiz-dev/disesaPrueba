package net.giro.cxc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.cxc.beans.ConceptoFacturacion;
import net.giro.cxc.beans.ConceptoFacturacionImpuestosExt;
import net.giro.cxc.beans.Factura;
import net.giro.cxc.beans.FacturaDetalleExt;
import net.giro.cxc.beans.FacturaExt;
import net.giro.cxc.beans.FacturaPagos;
import net.giro.cxc.fe.FactElect;
import net.giro.cxc.logica.ConceptoFacturacionImpuestosRem;
import net.giro.cxc.logica.ConceptoFacturacionRem;
import net.giro.cxc.logica.FacturaDetalleRem;
import net.giro.cxc.logica.FacturaPagosRem;
import net.giro.cxc.logica.FacturaRem;
import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Sucursal;
import net.giro.ne.logica.SucursalesRem;
import net.giro.plataforma.Utilerias;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.admon.Moneda;
import net.giro.tyg.dao.MonedaDAO;
import net.giro.tyg.logica.FormasPagosRem;

import org.apache.commons.ssl.Base64;
import org.apache.log4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@ViewScoped
@ManagedBean(name="facturaAction")
public class FacturaAction implements Serializable, Runnable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(FacturaAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private InitialContext ctx; 
	
	// Interfaces 
	private ConGrupoValoresRem ifzGpoVal;
	private ConValoresRem ifzConValores;
	private FacturaRem ifzFactura;
	private FacturaDetalleRem ifzFacturaDetalle;
	private ConceptoFacturacionImpuestosRem ifzConFacImpuestos;
	private ReportesRem ifzReportes;
	private FacturaPagosRem ifzPagos;
	// POJO's
	private Factura pojoFacturaBorrar;
	private FacturaExt pojoFactura;
	private FacturaDetalleExt pojoDetalle; 
	private ConceptoFacturacionImpuestosExt pojoDetalleImpuesto;
	// Listas
	private List<Factura> listFacturas;
	private List<FacturaDetalleExt> listDetalles;
	private List<FacturaDetalleExt> listDetallesEliminados;
	private List<ConceptoFacturacionImpuestosExt> listDetalleImpuestos;
	private List<ConceptoFacturacionImpuestosExt> listDetalleImpuestosEliminados;
	private HashMap<Long, List<ConceptoFacturacionImpuestosExt>> listMapDetalleImpuestos;
	// Elementos auxiliares
	private List<SelectItem> listConceptoFacturacionItems;	
	private SelectItem conceptoSeleccionado;
	// Busqueda principal
	private String campoBusqueda;		
	private String valorBusqueda;
	private int tipoBusqueda;
	private int numPagina;
	// Busqueda proyectos/Obras
	private ObraRem ifzObra;
	private List<Obra> listaObras;
	private Obra pojoObra;
	private List<SelectItem> opcionesBusquedaObras;
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int tipoBusquedaProyecto;
	private int paginacionObras;
	// Busqueda Sucursales
	private SucursalesRem ifzSucursales;
	private List<Sucursal> listBusquedaSucursal;
	private List<SelectItem> listBusquedaSucursalItems;	
	private List<SelectItem> tipoBusquedaSucursal;	
	private String valorBusquedaSucursal;
	private String campoBusquedaSucursal;
	private int paginaSucursales;
	private long sucursalId;
	// Busqueda conceptos de facturacion
	private ConceptoFacturacionRem ifzConceptosFacturacion;
	private List<ConceptoFacturacion> listConceptoFacturacion;
	private ConceptoFacturacion pojoConcepto;
	private List<SelectItem> tiposBusquedaConceptos;	
	private String valBusquedaConceptos;
	private String campoBusquedaConceptos;
	private int numPaginaConceptos;
	// Metodos de pago
	private ConGrupoValores pojoGpoMetodosPago; 
	private List<ConValores> listMetodosPago;
	private List<SelectItem> listMetodosPagoItems;	
	// Formas de pago
	private FormasPagosRem ifzFormasPago;
	private List<FormasPagos> listFormasPago;
	private List<SelectItem> listFormasPagoItems;
	// Variables de control
	private boolean procesoIncompleto;
	private int tipoMensaje;
	private String mensaje;
    private long usuarioId;
    private String usuario;
	private int numPaginaDetalles;
	private int numPaginaImpuestos;
	private int indexDetalleFactura;
	private boolean facturaTimbrada;
	private boolean puedeTimbrar;
	private boolean perfilEgresos;
	private boolean buscarAdministrativo;
	// -- Datos de la factura
	private double subtotal;
	private double impuestos;
	private double retenciones;
	private double total;
	private double costo;
	private long conceptoAnterior;
	private int MAX_LENGTH_FOLIO_FACTURA;
	private String facturaSerie;
	private String facturaFolio;
	// Email
	private String email;
	private String emailAsunto;
	private String emailCuerpo;
	// Provisiones
	private boolean permiteProvisionar;
	private double montoFacturado;
	private double montoPagado;
	private double montoProvision;
	// Moneda
	private MonedaDAO ifzMonedas;
	private List<Moneda> listMonedas;
	private List<SelectItem> listMonedasItems;
	private Moneda monedaBase;
	// DEBUG
	private boolean isDebug;
	private Map<String,String> paramsRequest;

	
	public FacturaAction() throws NamingException,Exception {
		Map<String,String> params = null;
		ValueExpression valExp = null;
		String valPerfil = "";
		FacesContext fc = null;
		Application app = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			// Obtenemmos los parametros de la URL si corresponde
			params = fc.getExternalContext().getRequestParameterMap();
		    if (this.paramsRequest == null)
		    	this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : params.entrySet())
		    	this.paramsRequest.put(item.getKey(), item.getValue());
		    // Comprobamos si requerimos levantar la variable DEBUG
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			this.pojoFactura = new FacturaExt();
			this.pojoDetalle = new FacturaDetalleExt();
			this.listFacturas = new ArrayList<Factura>();
			this.listDetalles = new ArrayList<FacturaDetalleExt>();
			this.listaObras = new ArrayList<Obra>();
			
			this.numPagina = 1;		
			this.numPaginaDetalles = 1;
			this.numPaginaImpuestos = 1;
	
			this.ctx = new InitialContext();
			this.ifzFactura 			 = (FacturaRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaFac!net.giro.cxc.logica.FacturaRem");
			this.ifzFacturaDetalle 		 = (FacturaDetalleRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaDetalleFac!net.giro.cxc.logica.FacturaDetalleRem");
			this.ifzConceptosFacturacion = (ConceptoFacturacionRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//ConceptoFacturacionFac!net.giro.cxc.logica.ConceptoFacturacionRem");
			this.ifzConFacImpuestos 	 = (ConceptoFacturacionImpuestosRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//ConceptoFacturacionImpuestosFac!net.giro.cxc.logica.ConceptoFacturacionImpuestosRem");
			this.ifzPagos				 = (FacturaPagosRem) this.ctx.lookup("ejb:/Logica_CuentasPorCobrar//FacturaPagosFac!net.giro.cxc.logica.FacturaPagosRem");
			this.ifzObra 				 = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzGpoVal 				 = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores 			 = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzSucursales			 = (SucursalesRem) this.ctx.lookup("ejb:/Logica_Publico//SucursalesFac!net.giro.ne.logica.SucursalesRem");
			this.ifzReportes 			 = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzMonedas 			 = (MonedaDAO) this.ctx.lookup("ejb:/Model_TYG//MonedaImp!net.giro.tyg.dao.MonedaDAO");
			this.ifzFormasPago 			 = (FormasPagosRem) this.ctx.lookup("ejb:/Logica_TYG//FormasPagosFac!net.giro.tyg.logica.FormasPagosRem");
			
			this.ifzFactura.showSystemOuts(false);
			this.ifzFacturaDetalle.showSystemOuts(false);
			this.ifzConFacImpuestos.showSystemOuts(false);
	
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("SYS_MONEDA_BASE");
			this.monedaBase = this.ifzMonedas.findByAbreviacion(valPerfil);
		
			// Busqueda pricipal
			this.campoBusqueda = "";		
			this.valorBusqueda = "";
			this.tipoBusqueda = 0;
			this.numPagina = 1;
			
			// Busqueda proyectos/Obras
			this.campoBusquedaObras = "folioFactura";
			this.valorBusquedaObras = "";
			this.tipoBusquedaProyecto = 0;
			
			// Busqueda conceptos de facturacion
			this.tiposBusquedaConceptos = new ArrayList<SelectItem>();
			this.tiposBusquedaConceptos.add(new SelectItem("descripcion","Concepto"));
			this.tiposBusquedaConceptos.add(new SelectItem("cuentaContable","Cuenta Contable"));
			this.tiposBusquedaConceptos.add(new SelectItem("id","Clave"));
			this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getDescription();
			this.valBusquedaConceptos = "";
			this.numPaginaConceptos = 1;
			
			
			// COMPROBAMOS LA MAXIMA LONGITUD DE FOLIO PARA LA FACTURACION
			// ---------------------------------------------------------------------------------------------------------
			this.MAX_LENGTH_FOLIO_FACTURA = 6;
			if (this.entornoProperties.containsKey("MAX_LENGTH_FOLIO_FACTURA") && this.entornoProperties.getString("MAX_LENGTH_FOLIO_FACTURA") != null && ! "".equals(entornoProperties.getString("MAX_LENGTH_FOLIO_FACTURA")))
				this.MAX_LENGTH_FOLIO_FACTURA = valueLengthFolioFactura(this.entornoProperties.getString("MAX_LENGTH_FOLIO_FACTURA"), this.MAX_LENGTH_FOLIO_FACTURA);
			
			// CARGAMOS LOS METODOS DE PAGO PARA FACTURAS
			// ---------------------------------------------------------------------------------------------------------
			this.pojoGpoMetodosPago = this.ifzGpoVal.findByName("SYS_METODO_PAGO");
			if (this.pojoGpoMetodosPago == null || this.pojoGpoMetodosPago.getId() <= 0L)
				log.warn("No se encontro encontro el grupo SYS_METODO_PAGO en con_grupo_valores");

			// Inicializamos listas
			if (this.listConceptoFacturacion == null)
				this.listConceptoFacturacion = new ArrayList<ConceptoFacturacion>();
			this.listConceptoFacturacion.clear();
			
			if (this.listConceptoFacturacionItems == null)
				this.listConceptoFacturacionItems = new ArrayList<SelectItem>();
			this.listConceptoFacturacionItems.clear();
			
			this.pojoFactura = new FacturaExt();
			this.pojoFactura.setFechaEmision(Calendar.getInstance().getTime());
			//this.concepto = 0L;
			this.conceptoAnterior = 0L;
			this.subtotal = 0;
			this.impuestos = 0;
			this.total = 0;
			
			this.numPaginaDetalles = 1;
			this.paginacionObras = 1;
			this.numPaginaImpuestos = 1;
			
			this.indexDetalleFactura = -1;
			
			// Busqueda SUCURSALES
			this.tipoBusquedaSucursal = new ArrayList<SelectItem>();
			this.tipoBusquedaSucursal.add(new SelectItem("sucursal", "Nombre"));
			this.tipoBusquedaSucursal.add(new SelectItem("id", "Clave"));
			this.valorBusquedaSucursal = this.tipoBusquedaSucursal.get(0).getValue().toString();
			this.campoBusquedaSucursal = "";
			this.paginaSucursales = 1;
			
			// Busqueda obras
			this.opcionesBusquedaObras = new ArrayList<SelectItem>();
			this.opcionesBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.opcionesBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.opcionesBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.opcionesBusquedaObras.add(new SelectItem("id", "Clave"));
			this.campoBusquedaObras = this.opcionesBusquedaObras.get(0).getDescription();
			this.valorBusquedaObras = "";
			this.tipoBusquedaProyecto = 0;
			this.paginacionObras = 1;
			
			// Cargo Metodos de Pagos
			cargarMetodosPagos();
			
			// Cargo Formas de Pago
			cargarFormasPagos();

			// Cargo monedas
			cargarMonedas();
			
			// Caego sucursal
			cargarSucursales();
			control();
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if ("".equals(this.campoBusqueda) || "".equals(this.campoBusqueda))
				this.campoBusqueda = "id";
			
			this.ifzFactura.orderBy("estatus DESC, fechaCreacion DESC, id DESC");
			this.listFacturas = this.ifzFactura.findLikeProperty(this.campoBusqueda, this.valorBusqueda, this.tipoBusqueda, 1000);
			if (this.listFacturas.isEmpty()) {
				log.info("2 - Busqueda sin resultados con el filtro " + this.campoBusqueda + " = " + this.valorBusqueda);
				control(2);
			}
		} catch(Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.buscar", e);
			control(true);
		}
	}

	public void nuevo() {
		try {
			control();
			this.pojoFactura = new FacturaExt();
			this.pojoDetalle = new FacturaDetalleExt();

			if (this.listDetalles == null)
				this.listDetalles = new ArrayList<FacturaDetalleExt>();
			this.listDetalles.clear();
			
			if (this.listDetallesEliminados == null)
				this.listDetallesEliminados = new ArrayList<FacturaDetalleExt>();
			this.listDetallesEliminados.clear();
			
			if (this.listDetalleImpuestos == null) 
				this.listDetalleImpuestos = new ArrayList<ConceptoFacturacionImpuestosExt>();
			this.listDetalleImpuestos.clear();
			
			if (this.listDetalleImpuestosEliminados == null) 
				this.listDetalleImpuestosEliminados = new ArrayList<ConceptoFacturacionImpuestosExt>();
			this.listDetalleImpuestosEliminados.clear();
			
			if (this.listMapDetalleImpuestos == null)
				this.listMapDetalleImpuestos = new HashMap<Long, List<ConceptoFacturacionImpuestosExt>>();
			this.listMapDetalleImpuestos.clear();
			
			this.sucursalId = 0L;
			this.conceptoAnterior = 0L;
			this.subtotal = 0;
			this.impuestos = 0;
			this.retenciones = 0;
			this.total = 0;
			this.puedeTimbrar = false;
			this.facturaTimbrada = false;
		} catch (Exception e) {
			log.error("Error en el metodo nuevo.", e);
			control(true);
		}
	}

	public void editar() throws Exception {
		try {
			control();
			this.sucursalId = 0L;
			this.pojoObra = new Obra();
			
			// Inicializamos listas y variables de la factura
			if (this.listDetalles == null) 
				this.listDetalles = new ArrayList<FacturaDetalleExt>();
			this.listDetalles.clear();
			
			if (this.listDetallesEliminados == null) 
				this.listDetallesEliminados = new ArrayList<FacturaDetalleExt>();
			this.listDetallesEliminados.clear();
			
			if (this.listDetalleImpuestos == null) 
				this.listDetalleImpuestos = new ArrayList<ConceptoFacturacionImpuestosExt>();
			this.listDetalleImpuestos.clear();
			
			if (this.listDetalleImpuestosEliminados == null) 
				this.listDetalleImpuestosEliminados = new ArrayList<ConceptoFacturacionImpuestosExt>();
			this.listDetalleImpuestosEliminados.clear();
			
			if (this.listMapDetalleImpuestos == null)
				this.listMapDetalleImpuestos = new HashMap<Long, List<ConceptoFacturacionImpuestosExt>>();
			this.listMapDetalleImpuestos.clear();
			
			if (this.pojoFactura != null) {
				// Recuperamos los detalles de la factura (Conceptos)
				log.info("Obra extendida. Recuperando detalles (Conceptos) de factura ... ");
				this.listDetalles = ifzFacturaDetalle.findByPropertyPojoCompletoExt("idFactura", pojoFactura.getId().longValue());
				if (this.listDetalles == null || this.listDetalles.isEmpty()) {
					log.info("ERROR INTERNO - No se pudo recuperar los detalles (Conceptos) de la factura indicada"); 
					control("No se pudo recuperar los detalles (Conceptos) de la factura indicada");
					return;
				}
				
				// Desglozamos los impuestos para cada concepto para generar listas de control (MAPA)
				log.info(this.listDetalles.size() + " conceptos en factura. Comprobando conceptos ... ");
				this.pojoDetalle = new FacturaDetalleExt();
				for (FacturaDetalleExt var : this.listDetalles) {
					this.pojoDetalle = var;
					if (var.getIdConcepto() == null) 
						continue;
					desglozarImpuestos();
				}
				
				// Recuperamos la sucursal asignada a la factura
				log.info("Conceptos comprobados. terminando ... ");
				if (this.pojoFactura.getIdSucursal() != null && this.pojoFactura.getIdSucursal().getId() > 0L)
					this.sucursalId = this.pojoFactura.getIdSucursal().getId();
				
				this.puedeTimbrar = true;
				this.facturaTimbrada = false;
				if (this.pojoFactura.getUuid() != null && ! "".equals(this.pojoFactura.getUuid())) {
					this.facturaTimbrada = true;
					this.puedeTimbrar = false;
				}
				
				log.info("Proceso terminando");
			}

			nuevoConceptoFacturacion();
			totalizar();
		} catch (Exception e) {
			log.error("Error en editar()", e);
			control(true);
		}
	}
	
	public void guardar() {
		try {
			control();
			log.info("Guardando Factura ... ");
			if (this.listDetalles.isEmpty()) {
				log.info("ERROR 3: Factura sin detalles");
				control(3);
				return;
			}

			log.info("Comprobando Moneda ... ");
			if (this.pojoFactura.getIdMoneda() == null && this.monedaBase != null) {
				this.pojoFactura.setIdMoneda(this.monedaBase);
				this.pojoFactura.setDescMoneda(this.monedaBase.getNombre());
				this.pojoFactura.setAbreviaturaMoneda(this.monedaBase.getAbreviacion());
				log.info("Moneda asignada: " + this.monedaBase.getNombre());
			}

			log.info("Comprobando Cliente ... ");
			if (this.pojoFactura.getCliente() == null || "".equals(this.pojoFactura.getCliente())) {
				log.info("Agregando valores adicionales de Cliente ... ");
				this.pojoFactura.setIdCliente(this.pojoFactura.getIdObra().getIdCliente().getId());
				this.pojoFactura.setCliente(this.pojoFactura.getIdObra().getIdCliente().getNombre());
				this.pojoFactura.setTipoCliente(this.pojoFactura.getIdObra().getTipoCliente());
				this.pojoFactura.setRfc(this.pojoFactura.getIdObra().getIdCliente().getRfc());
				if (this.pojoFactura.getTipoCliente() == null || "".equals(this.pojoFactura.getTipoCliente())) {
					if (this.pojoFactura.getIdObra().getIdCliente().getTipoPersona() == 1L)
						this.pojoFactura.setTipoCliente("P");
					else
						this.pojoFactura.setTipoCliente("N");
				}
				log.info("Valores de Cliente agregados");
			}
			
			this.pojoFactura.setFechaVencimiento(Calendar.getInstance().getTime());
			this.pojoFactura.setSubtotal(new BigDecimal(this.subtotal));
			this.pojoFactura.setImpuestos(this.impuestos);
			this.pojoFactura.setRetenciones(this.retenciones);
			this.pojoFactura.setIdFormaPago(this.pojoFactura.getIdMetodoPago()); // ;(1L); // PAGO EN UNA SOLA EXHIBICION
			
			// Asignamos la sucursal si corresponde
			log.info("Comprobando Sucursal ... ");
			if (this.sucursalId > 0L) {
				for (Sucursal var : this.listBusquedaSucursal) {
					if (this.sucursalId != var.getId())
						continue;
					
					// Asignamos sucursal
					this.pojoFactura.setIdSucursal(var);
					this.pojoFactura.setNombreSucursal(var.getSucursal());
					log.info("Sucursal asignada");
					
					// asignamos serie y folio tomado de la sucursal seleccionada, si corresponde
					log.info("Comprobando Serie y Folio de Sucursal ... ");
					if (this.pojoFactura.getSerie() == null || "".equals(this.pojoFactura.getSerie())) 
						this.pojoFactura.setSerie(var.getSerie());
					
					if (var.getSerie() == null || "".equals(var.getSerie().trim())) {
						this.pojoFactura.setSerie("");
						this.pojoFactura.setFolio("0");
						log.info("Sucursal sin Serie y Folio");
						break;
					}
						
					if (this.pojoFactura.getFolio() == null || "".equals(this.pojoFactura.getFolio()) || "0".equals(this.pojoFactura.getFolio())) {
						// Asignamos folio obtenida de la sucursal
						long folio = 0;
						log.info("Asignando Serie y Folio desde Sucursal ... ");
						Respuesta res = this.ifzSucursales.folioFacturacion(var);
						if (res.getErrores().getCodigoError() != 0L) {
							if (res.getErrores().getCodigoError() == -1) {
								log.info("ERROR 8: Sucursal no existe");
								control(8);
							}
							
							if (res.getErrores().getCodigoError() == -2) {
								log.info("ERROR 9: Sucursal sin serie y/o folio asignadas para facturacion");
								control(9);
							}
							
							return;
						}
						
						folio = (Long) res.getBody().getValor("folioFacturacion");
						this.pojoFactura.setFolio(formatoFolio(String.valueOf(folio)));
						
						// Generamos el folioFactura con la serie y folio obtenidos anteriormente
						this.pojoFactura.setFolioFactura(this.pojoFactura.getSerie() + "-" + this.pojoFactura.getFolio());
						log.info("Serie y Folio asignados: " + this.pojoFactura.getFolioFactura());
					}
					
					// Rompemos el ciclo
					break;
				}
			}
			
			if (! this.isDebug || (this.isDebug && ! this.paramsRequest.containsKey("NO_FOLIO"))) {
				if ("".equals(this.pojoFactura.getSerie()) && "".equals(this.pojoFactura.getFolio())) {
					log.info("ERROR 12: Factura sin serie y/o folio asignadas");
					control(12);
					return;
				}
			} 
			
			if (this.pojoFactura.getId() == null) {
				this.pojoFactura.setEstatus(1);
				this.pojoFactura.setCreadoPor(this.usuarioId);
				this.pojoFactura.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoFactura.setModificadoPor(this.usuarioId);
				this.pojoFactura.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos la factura y la añadimos al listado
				this.pojoFactura.setId(this.ifzFactura.save(this.pojoFactura));
				this.listFacturas.add(this.ifzFactura.convertir(this.pojoFactura));
				log.info("Factura guardada");
			} else {
				this.pojoFactura.setModificadoPor(this.usuarioId);
				this.pojoFactura.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Actualizamos la factura en la BD
				this.ifzFactura.update(this.pojoFactura);
				
				// Actualizamos la factura en la lista
				int index = -1;
				for(Factura var : this.listFacturas) {
					if(var.getId() == this.pojoFactura.getId()) {
						index = this.listFacturas.indexOf(var);
						break;
					}
				}
				
				if (index >= 0) 
					this.listFacturas.set(index, this.ifzFactura.convertir(this.pojoFactura));
				log.info("Factura actualizada");
			}
			
			// Guardamos los detalles de la factura
			log.info("Guardando detalles (" + this.listDetalles.size() + ") ... ");
			for (FacturaDetalleExt var : this.listDetalles) {
				log.info("Guardando concepto " + var.getIdConcepto().getDescripcion() + " ... ");
				var.setIdFactura(this.pojoFactura);
				
				if (var.getId() == null || var.getId() <= 0L) {
					// Asignamos la factura a la que corresponde y su auditoria
					var.setIdFactura(this.pojoFactura);
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Guadamos en la BD y actualizamos el ID que le corresponde
					var.setId(this.ifzFacturaDetalle.save(var));
					log.info("Concepto guardado, comprobando impuestos ... ");
				} else {
					var.setModificadoPor(this.usuarioId);
					var.setFechaModificacion(Calendar.getInstance().getTime());
					
					// Actualizamos el detalle en la BD
					this.ifzFacturaDetalle.update(var);
					log.info("Concepto actualizado, comprobando impuestos ... ");
				}
				
				if (this.listMapDetalleImpuestos.containsKey(var.getIdConcepto().getId())) {
					if (this.listDetalleImpuestos == null)
						this.listDetalleImpuestos = new ArrayList<ConceptoFacturacionImpuestosExt>();
					this.listDetalleImpuestos.clear();
					
					this.listDetalleImpuestos.addAll(this.listMapDetalleImpuestos.get(var.getIdConcepto().getId()));
					if (this.listDetalleImpuestos != null && ! this.listDetalleImpuestos.isEmpty()) {
						// Guardamos los impuestos
						log.info("Guardando Impuestos de Concepto ... ");
						for (ConceptoFacturacionImpuestosExt imp : this.listDetalleImpuestos) {
							imp.setModificadoPor(this.usuarioId);
							imp.setFechaModificacion(Calendar.getInstance().getTime());
							
							if (imp.getId() == null || imp.getId() <= 0L) {
								imp.setCreadoPor(this.usuarioId);
								imp.setFechaCreacion(Calendar.getInstance().getTime());
								imp.setId(this.ifzConFacImpuestos.save(imp));
								continue;
							} 
							
							this.ifzConFacImpuestos.update(imp);
						}
						
						log.info("Impuestos de Concepto guardados.");
					}
				}
			}
			
			// Comprobamos y eliminamos los detalles previamente borrados de la factura
			if (this.listDetallesEliminados != null && !this.listDetallesEliminados.isEmpty()) {
				log.info("Eliminando detalles previamente borrados ... ");
				for (FacturaDetalleExt var: this.listDetallesEliminados) {
					if (var.getId() == null || var.getId() <= 0L)
						continue;
					
					// Borramos de la BD el detalle de factura.
					this.ifzFacturaDetalle.delete(var);
				}
				
				// Limpiamos el listado de detalles eliminados
				this.listDetallesEliminados.clear();
				log.info("Detalles eliminados.");
			}
			
			// Comprobamos y eliminamos los impuestos previamente borrados de los conceptos
			if (this.listDetalleImpuestosEliminados != null && ! this.listDetalleImpuestosEliminados.isEmpty()) {
				log.info("Eliminando impuestos de detalles previamente borrados ... ");
				for (ConceptoFacturacionImpuestosExt var : this.listDetalleImpuestosEliminados) {
					if (var.getId() == null || var.getId() <= 0L)
						continue;
					
					// Borramos de la BD el detalle de factura.
					this.ifzConFacImpuestos.delete(var.getId());
				}

				// Limpiamos el listado de impuestos eliminados
				this.listDetalleImpuestosEliminados.clear();
				log.info("Impuestos eliminados.");
			}
			
			this.puedeTimbrar = true;
			this.facturaTimbrada = false;
			if (this.pojoFactura.getUuid() != null && ! "".equals(this.pojoFactura.getUuid())) {
				this.facturaTimbrada = true;
				this.puedeTimbrar = false;
			}

			control();
		} catch(Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.guardar", e);
			control(true);
			return;
		}
	}
	
	public void guardarFacturaFolio() {
		try {
			control();
			this.pojoFactura.setSerie(this.facturaSerie);
			this.pojoFactura.setFolio(this.facturaFolio);
			this.pojoFactura.setFolioFactura(this.facturaSerie + '-' + this.facturaFolio);
			this.pojoFactura.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoFactura.setModificadoPor(this.usuarioId);
			log.info("Serie y folio modificados correctamente: " + this.pojoFactura.getFolioFactura());
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.guardarFacturaFolio(). No se pudo guardar la Serie y Folio ingresados.", e);
			control("No se pudo guardar la Serie y Folio ingresados");
		}
	}
	
	public void eliminar() {
		Respuesta respuesta = null;
		
		try {
			control();
			if (this.pojoFacturaBorrar != null && this.pojoFacturaBorrar.getId() > 0L) {
				// Actualizamos la factura
				log.info("Cancelando factura " + this.pojoFacturaBorrar.getId() + " folio " + this.pojoFactura.getFolioFactura() + " ... ");
				respuesta = this.ifzFactura.cancelarFactura(this.pojoFacturaBorrar, this.usuarioId);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					log.error("Ocurrio un problema al intentar Cancelar la factura " + this.pojoFacturaBorrar.getId());
					control("Ocurrio un problema al intentar Cancelar la Factura seleccionada.");
					return;
				}

				// Actualizamos en la lista
				log.info("Factura cancelada. Actualizando listado de facturas ... ");
				for (Factura var : this.listFacturas) {
					if (var.getId() == this.pojoFacturaBorrar.getId()) {
						var.setEstatus(this.pojoFactura.getEstatus());
						break;
					}
				}
				
				this.pojoFacturaBorrar = null;
				log.info("Proceso terminado");
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.eliminar.", e);
			control(true);
		}
	}

	public void totalizar() {
		log.info("Totalizando ... ");
		this.subtotal = 0;
		this.impuestos = 0;
		this.retenciones = 0;
		this.total = 0;

		if (this.listDetalles == null)
			this.listDetalles = new ArrayList<FacturaDetalleExt>();
		if (this.listDetalles.isEmpty()) {
			log.info("Factura sin conceptos");
			return;
		}
		
		for(FacturaDetalleExt var : this.listDetalles) {
			this.subtotal 	 += var.getImporte().doubleValue();
			this.impuestos   += var.getImpuestos().doubleValue();
			this.retenciones += var.getRetenciones().doubleValue();
		}
		
		this.total = this.subtotal + (this.impuestos - this.retenciones);
		this.total = Utilerias.redondear(this.total, 2);
		log.info("Totalizado terminado");
	}
	
	private String formatoFolio(String folio) {
		int iteracion = 0;
		
		if (folio.length() < this.MAX_LENGTH_FOLIO_FACTURA) {
			iteracion = (this.MAX_LENGTH_FOLIO_FACTURA - folio.length());
			for(int x = 0; x < iteracion; x++) {
				folio = "0" + folio;
			}
		}
		
		return folio;
	}
	
	private int valueLengthFolioFactura(String value, int defaultValue) {
		try {
			return Integer.parseInt(value.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public void evaluaFacturaFolio() {
		try {
			control();
			this.facturaSerie = this.pojoFactura.getSerie();
			this.facturaFolio = this.pojoFactura.getFolio();
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.evaluaFacturaFolio(). No se pudo cargar los valores para Serie y Folio de la factura");
			control("No se pudo cargar la Serie y Folio de la Factura indicada.");
		}
	}

	public void cargarMetodosPagos() {
		try {
    		control();
    		log.info("Recuperando METODOS DE PAGO ... ");
			if (this.listMetodosPago == null)
				this.listMetodosPago = new ArrayList<ConValores>();
			this.listMetodosPago.clear();

			if (this.listMetodosPagoItems == null)
				this.listMetodosPagoItems = new ArrayList<SelectItem>();
			this.listMetodosPagoItems.clear();
			
			this.listMetodosPago = this.ifzConValores.findAll(this.pojoGpoMetodosPago);
			if (this.listMetodosPago == null || this.listMetodosPago.isEmpty()) {
	    		log.info("Sin METODOS DE PAGO");
				return;
			}
			
			for (ConValores var : this.listMetodosPago)
				this.listMetodosPagoItems.add(new SelectItem(var.getId(), var.getDescripcion()));
    		log.info(this.listMetodosPago.size() + " METODOS DE PAGO recuperandos");
		} catch (Exception e) {
    		log.error("Error en CuentasPorCobrar.FacturaAction.cargarMetodosPagos", e);
    		control(true);
		}
	}
	
	public void cargarFormasPagos() {
		try {
    		control();
			if (this.listFormasPagoItems == null)
				this.listFormasPagoItems = new ArrayList<SelectItem>();
			this.listFormasPagoItems.clear();
			
			this.listFormasPago = this.ifzFormasPago.findLikeColumnName("formaPago", "");
			if (!this.listFormasPago.isEmpty()) {
				for (FormasPagos var : this.listFormasPago) {
					this.listFormasPagoItems.add(new SelectItem(var.getId(), var.getId() + " - " + var.getFormaPago()));
				}
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.cargarFormasPagos", e);
    		control(true);
		}
	}

	public void cargarSucursales() throws Exception {
		try {
			control();
			log.info("Cargando sucursales");
			if (this.listBusquedaSucursal == null)
				this.listBusquedaSucursal = new ArrayList<Sucursal>();
			this.listBusquedaSucursal.clear();
			
			if (this.listBusquedaSucursalItems == null)
				this.listBusquedaSucursalItems = new ArrayList<SelectItem>();
			this.listBusquedaSucursalItems.clear();

			this.listBusquedaSucursal = this.ifzSucursales.buscarSucursales("sucursal", "");
			if (this.listBusquedaSucursal == null || this.listBusquedaSucursal.isEmpty()) {
				log.info("Ninguna sucursal encontrada");
				return;
			}

			for (Sucursal var : this.listBusquedaSucursal)
				this.listBusquedaSucursalItems.add(new SelectItem(var.getId(), var.getSucursal()));
			log.info(this.listBusquedaSucursal.size() + " sucursales encontradas");
		} catch (Exception e) {
    		log.error("Error en cargarSucursales", e);
			control(true);
		}
	}

	private void cargarMonedas() {
		try {
			if (this.listMonedasItems == null)
				this.listMonedasItems = new ArrayList<SelectItem>();
			this.listMonedasItems.clear();
			
			this.listMonedas = this.ifzMonedas.findAll();
			if (this.listMonedas != null && ! this.listMonedas.isEmpty()) {
				for (Moneda mon : this.listMonedas)
					this.listMonedasItems.add(new SelectItem(mon.getId(), mon.getNombre() + " (" + mon.getAbreviacion() + ")"));
			}
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.cargarMonedas", e);
		}
	}

	private void control() {
		control(false, 0, "");
	}
	
	private void control(boolean value) {
		this.procesoIncompleto = value;
		
		if (! value) {
			control(false, 0, "");
			return;
		} 

		control(true, 1, "ERROR");
	}
		
	private void control(int tipoMensaje) {
		if (tipoMensaje == 0) {
			control(false);
			return;
		}
		
		control(true, tipoMensaje, "ERROR");
	}
	
	private void control(String mensaje) {
		if (mensaje == null || "".equals(mensaje.trim())) 
			control();
		else
			control(true, -1, mensaje);
	}
	
	private void control(boolean procesoIncompleto, int tipoMensaje, String mensaje) {
		this.procesoIncompleto = procesoIncompleto;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje;
	}

	// ---------------------------------------
	// DETALLES DE FACTURA
	// ---------------------------------------

	public void nuevoConceptoFacturacion() {
		try {
			control();
			if (this.listDetalleImpuestos == null)
				this.listDetalleImpuestos = new ArrayList<ConceptoFacturacionImpuestosExt>();
			this.listDetalleImpuestos.clear();

			this.conceptoAnterior = 0L;
			this.pojoDetalle = new FacturaDetalleExt();
			//this.pojoDetalle.setIdConcepto(new ConceptoFacturacion());
			this.pojoDetalle.setCantidad(new BigDecimal(1));
			this.pojoDetalle.setCosto(BigDecimal.ZERO);
		} catch (Exception e) {
			log.error("Error en nuevoConceptoFacturacion()", e);
			control(true);
		}
	}
	
	public void editarFacturaDetalleConcepto(){
		if (this.pojoDetalle.getIdConcepto() != null) {
			this.conceptoAnterior = 0L;
			this.indexDetalleFactura = -1;
			this.indexDetalleFactura = this.listDetalles.indexOf(this.pojoDetalle);
			desglozarImpuestos();
		}
	}
	
	public void guardarConcepto() {
		try {
			control();
			
			if (this.pojoDetalle.getCantidad().doubleValue() <= 0) {
				control(10);
				log.info("ERROR 10: La cantidad debe ser mayor a cero");
				return;
			}
			
			if (this.pojoDetalle.getCosto().doubleValue() <= 0) {
				control(11);
				log.info("ERROR 11: El costo debe ser mayor a cero");
				return;
			}
			
			if(this.pojoDetalle != null && this.pojoDetalle.getId() == null) {
				if (this.indexDetalleFactura == -1)
					this.listDetalles.add(this.pojoDetalle);
				else
					this.listDetalles.set(this.indexDetalleFactura, this.pojoDetalle);
			} else {
				for(FacturaDetalleExt var : this.listDetalles) {
					if (var.getId() == this.pojoDetalle.getId()) {
						var = this.pojoDetalle;
						break;
					}
				}
			}
			
			totalizar();
			
			this.indexDetalleFactura = -1;
			this.pojoDetalle = null;
			nuevoConceptoFacturacion();
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorCobrar.FacturaAction.guardarConcepto", e);
		}
	}
	
	public void eliminarFacturaDetalle(){
		try {
			control();
			
			if(this.listDetallesEliminados == null)
				this.listDetallesEliminados = new ArrayList<FacturaDetalleExt>();
			
			this.listDetallesEliminados.add(this.pojoDetalle);
			this.listDetalles.remove(this.pojoDetalle);

			if (this.listMapDetalleImpuestos.containsKey(this.pojoDetalle.getIdConcepto().getId())) {
				if (this.listDetalleImpuestosEliminados == null) 
					this.listDetalleImpuestosEliminados = new ArrayList<ConceptoFacturacionImpuestosExt>();
				this.listDetalleImpuestosEliminados.clear();
				this.listDetalleImpuestosEliminados.addAll(this.listMapDetalleImpuestos.get(this.pojoDetalle.getIdConcepto().getId()));
				this.listMapDetalleImpuestos.remove(this.pojoDetalle.getIdConcepto().getId());
			}
		
			totalizar();
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorCobrar.FacturaAction.eliminarFacturaDetalle", e);
		}
	}

	public void eliminarImpuesto() {
		if (this.pojoDetalleImpuesto != null) {
			log.info("Eliminando Impuesto ... ");
			this.listDetalleImpuestos.remove(this.pojoDetalleImpuesto);
			this.listDetalleImpuestosEliminados.add(this.pojoDetalleImpuesto);
			log.info("Impuesto eliminado. Desglosando impuestos ... ");
			
			desglozarImpuestos();
			log.info("Impuestos desglosandod");
		}
	}
	
	public void desglozarImpuestos() {
		Double impuesto = 0D;
		Double totalImpuestos = 0D;
		Double totalRetenciones = 0D;
		Double porImpuesto = 0D;
		Double porRetension = 0D;
		//long concepto = 0;
		
		try {
			control();
			log.info("\nPreparando desgloce de impuestos ... ");
			if (this.pojoDetalle.getIdConcepto().getId() == null || this.pojoDetalle.getIdConcepto().getId() <= 0L) {
				log.info("\nNo selecciono ningun concepto");
				control("No selecciono ningun concepto");
				return;
			}
			
			if (this.conceptoAnterior == this.pojoDetalle.getIdConcepto().getId().longValue() && ! this.listDetalleImpuestos.isEmpty()) {
				log.info("\nImpuestos desglosados previamente");
				return;
			}
			
			// Recupero impuestos del concepto, si corresponde
			log.info("\nRecuperando impuestos ... ");
			if (this.listDetalleImpuestos == null)
				this.listDetalleImpuestos = new ArrayList<ConceptoFacturacionImpuestosExt>();
			this.listDetalleImpuestos.clear();
			
			this.listDetalleImpuestos = this.ifzConFacImpuestos.findByPropertyExt("idConceptoFacturacion", this.pojoDetalle.getIdConcepto().getId());
			if (this.listDetalleImpuestos == null || this.listDetalleImpuestos.isEmpty()) {
				if (this.listMapDetalleImpuestos.containsKey(this.pojoDetalle.getIdConcepto().getId())) {
					if (this.listDetalleImpuestosEliminados == null) 
						this.listDetalleImpuestosEliminados = new ArrayList<ConceptoFacturacionImpuestosExt>();
					this.listDetalleImpuestosEliminados.addAll(this.listMapDetalleImpuestos.get(this.pojoDetalle.getIdConcepto().getId()));
					this.listMapDetalleImpuestos.remove(this.pojoDetalle.getIdConcepto().getId());
				}
				
				log.info("\nEl concepto no tiene asignado ningun impuesto");
				control("El concepto no tiene asignado ningun impuesto");
				return;
			}

			// Realizando desgloce
			log.info("\nDesglozando impuestos ... ");
			for (ConceptoFacturacionImpuestosExt var : this.listDetalleImpuestos) {
				if (var.getIdImpuesto() != null && var.getIdImpuesto().getAtributo1() != null && ! "".equals(var.getIdImpuesto().getAtributo1())) {					
					impuesto = ((this.pojoDetalle.getImporte().doubleValue() * Double.valueOf(var.getIdImpuesto().getAtributo1())) / 100);
					var.setMonto(new BigDecimal(impuesto));
					if ("DE".equals(var.getIdImpuesto().getTipoCuenta())) {
						porImpuesto = Double.valueOf(var.getIdImpuesto().getAtributo1());
						totalImpuestos += impuesto;
					} else {
						porRetension = Double.valueOf(var.getIdImpuesto().getAtributo1());
						totalRetenciones += impuesto;
					}
				}
			}
			
			List<ConceptoFacturacionImpuestosExt> listAux = new ArrayList<ConceptoFacturacionImpuestosExt>();
			listAux.addAll(this.listDetalleImpuestos);
			this.listMapDetalleImpuestos.put(this.pojoDetalle.getIdConcepto().getId(), listAux);
			this.pojoDetalle.setPorcentajeIva(porImpuesto);
			this.pojoDetalle.setPorcentajeRetencion(porRetension);
			this.pojoDetalle.setImpuestos(new BigDecimal(totalImpuestos));
			this.pojoDetalle.setRetenciones(new BigDecimal(totalRetenciones));
			log.info("\nDesgloce de impuestos terminado");
		} catch (Exception e) {
			log.error("\nError en CuentasPorCobrar.FacturaAction.desglozarImpuestos(). No se pudo desglozar impuestos\n", e);
			control("No se pudo desglozar impuestos");
		}
	}

	// ---------------------------------------
	// BUSQUEDA CONCEPTOS DE FACTURACION
	// ---------------------------------------	
	
	public void busquedaConceptos() {
		control();
		
		this.listConceptoFacturacion.clear();
		this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getDescription();
		this.valBusquedaConceptos = "";
		this.numPaginaConceptos = 1;
	}
	
	public void buscarConceptos() {
		try {
			control();
			
			if ("".equals(this.campoBusquedaConceptos))
				this.campoBusquedaConceptos = this.tiposBusquedaConceptos.get(0).getDescription();
			
			this.listConceptoFacturacion = this.ifzConceptosFacturacion.findLikeProperty(this.campoBusquedaConceptos, this.valBusquedaConceptos, 0);
			if (this.listConceptoFacturacion.isEmpty()) {
				control(2);
				log.info("No se encontro ningun concepto de facturacion con el filtro --> " + this.campoBusquedaConceptos + ":" + this.valBusquedaConceptos);
			}
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorCobrar.FacturaAction.buscarConceptos", e);
		}
	}
	
	public void seleccionarConcepto() {
		try {
			control();
			if (this.pojoConcepto == null || this.pojoConcepto.getId() == null || this.pojoConcepto.getId() <= 0L) {
				log.info("No selecciono ningun Concepto. POJO nulo");
				control("Debe seleccionar un Concepto");
				return;
			}
			
			// Añadimos el concepto y desglozamos sus impuestos, si corresponde
			this.pojoDetalle.setIdConcepto(this.pojoConcepto);
			desglozarImpuestos();
			
			busquedaConceptos();
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorCobrar.FacturaAction.seleccionarConcepto", e);
		}
	}

	// ---------------------------------------
	// OBRAS/PROYECTOS
	// ---------------------------------------
	
	public void nuevaBusquedaObras() {
		control();
		this.campoBusquedaObras = this.opcionesBusquedaObras.get(0).getDescription();
		this.valorBusquedaObras = "";
		this.tipoBusquedaProyecto = 0;
		this.paginacionObras = 1;
		
		this.pojoObra = new Obra();
		if (this.listaObras != null && !this.listaObras.isEmpty())
			this.listaObras.clear();
	}

	public void buscarObras(){
		try {
			control();
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.opcionesBusquedaObras.get(0).getValue().toString();
			
			this.tipoBusquedaProyecto = 0;
			if (this.buscarAdministrativo)
				this.tipoBusquedaProyecto = 4;

			this.ifzObra.estatus(this.ifzObra.findEstatusCanceladoObras());
			this.listaObras = this.ifzObra.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.tipoBusquedaProyecto);
			if(listaObras.isEmpty()) {
				log.info("ERROR 2: Busqueda sin resultados. " + this.campoBusquedaObras + ": " + this.valorBusquedaObras);
				control(2);
			}
		} catch(Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.buscarProyectos", e);
			control(true);
		}
	}
	
	public void seleccionarObra() {
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				log.info("No se pudo recuperar la Obra seleccionada. POJO nulo");
				control("No se pudo recuperar la Obra seleccionada");
				return;
			}

			this.pojoFactura.setIdObra(this.ifzObra.convertir(this.pojoObra));
			this.pojoFactura.setIdBeneficiario(this.pojoObra.getIdCliente());
			this.pojoFactura.setCliente(this.pojoObra.getNombreCliente());
			this.pojoFactura.setRfc(this.pojoFactura.getIdObra().getIdCliente().getRfc());
			this.pojoFactura.setBeneficiario(this.pojoObra.getNombreCliente());
			this.pojoFactura.setTipoBeneficiario(this.pojoObra.getTipoCliente());
			if (this.pojoFactura.getTipoBeneficiario() == null || "".equals(this.pojoFactura.getTipoBeneficiario())) {
				if (this.pojoFactura.getIdObra().getIdCliente().getTipoPersona() == 1L)
					this.pojoFactura.setTipoBeneficiario("P");
				else
					this.pojoFactura.setTipoBeneficiario("N");
			}
			
			nuevaBusquedaObras();
		} catch (Exception e) {
			control(true);
			log.error("Error en CuentasPorCobrar.FacturaAction.seleccionarProyecto", e);
		} 
	}

	// ---------------------------------------
	// TIMBRAR
	// ---------------------------------------
	
	public void timbrar() {
		String resTimbre = "";
		
		try {
			control();
			
			if (this.pojoFactura != null) {
				FactElect timbre = new FactElect();
				resTimbre = timbre.foliar(this.pojoFactura.getId().intValue(), (int) this.usuarioId);
				actualizaFactura(timbre.getData(), timbre.facturaActualizada());
				
				if (! "".equals(resTimbre.trim())) {
					control(5);
					this.mensaje = resTimbre.trim();
					log.info("ERROR 5: Error al timbrar --> " + resTimbre.trim());
				}
			}
		} catch(Exception e) {
			control(true);
			log.error("Error en CuentasPorCobrar.FacturaAction.timbrar.", e);
		}
	}
	
	private void actualizaFactura(HashMap<String, Object> data, boolean facturaActualizada) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			if (data.containsKey("no_externo") && data.get("no_externo") != null && ! "SN".equals(data.get("no_externo").toString().trim()) && ! "".equals(data.get("no_externo").toString().trim()))
				this.pojoFactura.setNoExterno(data.get("no_externo").toString());
			if (data.containsKey("no_interno") && data.get("no_interno") != null && ! "SN".equals(data.get("no_interno").toString().trim()) && ! "".equals(data.get("no_interno").toString().trim()))
				this.pojoFactura.setNoInterno(data.get("no_interno").toString());
			if (data.containsKey("cadena_original") && data.get("cadena_original") != null)
				this.pojoFactura.setCadenaOriginal(data.get("cadena_original").toString().getBytes());
			if (data.containsKey("cfdi") && data.get("cfdi") != null)
				this.pojoFactura.setCfdi(data.get("cfdi").toString().getBytes());
			if (data.containsKey("ciudad") && data.get("ciudad") != null)
				this.pojoFactura.setCiudad(data.get("ciudad").toString());
			if (data.containsKey("cliente") && data.get("cliente") != null)
				this.pojoFactura.setCliente(data.get("cliente").toString());
			if (data.containsKey("colonia") && data.get("colonia") != null)
				this.pojoFactura.setColonia(data.get("colonia").toString());
			if (data.containsKey("cp") && data.get("cp") != null)
				this.pojoFactura.setCp(Integer.valueOf(data.get("cp").toString()));
			if (data.containsKey("domicilio") && data.get("domicilio") != null)
				this.pojoFactura.setDomicilio(data.get("domicilio").toString());
			if (data.containsKey("estado") && data.get("estado") != null)
				this.pojoFactura.setEstado(data.get("estado").toString());
			if (data.containsKey("folio") && data.get("folio") != null)
				this.pojoFactura.setFolio(data.get("folio").toString());
			if (data.containsKey("id_folio") && data.get("id_folio") != null)
				this.pojoFactura.setIdFolio(Long.valueOf(data.get("id_folio").toString()));
			if (data.containsKey("timbre") && data.get("timbre") != null)
				this.pojoFactura.setTimbre(data.get("timbre").toString().getBytes());
			if (data.containsKey("tipo_comprobante") && data.get("tipo_comprobante") != null)
				this.pojoFactura.setTipoComprobante(data.get("tipo_comprobante").toString());
			if (data.containsKey("pais") && data.get("pais") != null)
				this.pojoFactura.setPais(data.get("pais").toString());
			if (data.containsKey("sello") && data.get("sello") != null)
				this.pojoFactura.setSello(data.get("sello").toString().getBytes());
			if (data.containsKey("serie") && data.get("serie") != null)
				this.pojoFactura.setSerie(data.get("serie").toString());
			if (data.containsKey("state") && data.get("state") != null)
				this.pojoFactura.setState(Double.valueOf(data.get("state").toString()));
			if (data.containsKey("descripcion") && data.get("descripcion") != null)
				this.pojoFactura.setDescripcion(data.get("descripcion").toString()); // .setState(Double.valueOf(data.get("state").toString()));
			if (data.containsKey("xml") && data.get("xml") != null)
				this.pojoFactura.setXml(data.get("xml").toString().getBytes());
			if (data.containsKey("nocertificado") && data.get("nocertificado") != null)
				this.pojoFactura.setNocertificado(data.get("nocertificado").toString());
			if (data.containsKey("uuid") && data.get("uuid") != null)
				this.pojoFactura.setUuid(data.get("uuid").toString());
			if (data.containsKey("fecha_timbrado") && data.get("fecha_timbrado") != null)
				this.pojoFactura.setFechaTimbrado(formatter.parse(data.get("fecha_timbrado").toString()));
			if (data.containsKey("no_certificado_sat") && data.get("no_certificado_sat") != null)
				this.pojoFactura.setNoCertificadoSat(data.get("no_certificado_sat").toString());
			if (data.containsKey("sello_sat") && data.get("sello_sat") != null)
				this.pojoFactura.setSelloSat(data.get("sello_sat").toString());
			
			if (! facturaActualizada)
				this.ifzFactura.update(pojoFactura);
			
			this.puedeTimbrar = true;
			this.facturaTimbrada = false;
			if (this.pojoFactura.getUuid() != null && ! "".equals(this.pojoFactura.getUuid())) {
				this.facturaTimbrada = true;
				this.puedeTimbrar = false;
			}
		} catch(Exception e) {
			this.procesoIncompleto = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en actualizaFactura.", e);
		}
	}

	// ---------------------------------------
	// ENVIO DE FACTURA
	// ---------------------------------------
	
	public void nuevoEnvio() {
		this.email = "";
		this.emailAsunto = "Envio de Factura";
		if (this.pojoFactura != null && this.pojoFactura.getId() > 0L)
			this.emailAsunto += " " + this.pojoFactura.getFolioFactura();
		this.emailCuerpo = "Anexo factura en formato PDF";
	}
	
	public void enviarFactura() {
		try {
			control();
			
			if (this.pojoFactura != null) {
				if ("".equals(this.email)) {
					control(13);
					log.info("ERROR 13: NO especifico correo para el envio de la factura");
					return;
				}

				// Validamos mensaje
				if ("".equals(this.emailCuerpo))
					this.emailCuerpo = "Anexo factura en formato PDF.";

				if (this.emailCuerpo.contains("[Mensaje]"))
					this.emailCuerpo = this.emailCuerpo.replace("[Mensaje]", "");

				if (this.emailCuerpo.contains("\n"))
					this.emailCuerpo = this.emailCuerpo.replace("\n", "<br/>");
				
				this.emailCuerpo = "<html><body>" + this.emailCuerpo + "</body></html>";
				
				// Parametros del reporte
				HashMap<String, Object> paramsReporte = new HashMap<String, Object>();
				paramsReporte.put("idFactura", this.pojoFactura.getId());
				
				HashMap<String, String> correo = new HashMap<>();
				correo.put("from", "ameza@condese.com"); 
				correo.put("to", this.email);
				correo.put("subject", this.emailAsunto);
				correo.put("body", this.emailCuerpo);
				
				// Ajuntamos CFDI
				JsonArray adjuntos = new JsonArray();
				JsonObject adjunto = new JsonObject();
				Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
				adjunto.addProperty("archivo", "factura-" + this.pojoFactura.getSerie() + this.pojoFactura.getFolio());
				adjunto.addProperty("contenido", new String(Base64.encodeBase64(this.pojoFactura.getXml())));
				adjuntos.add(adjunto);
				
				correo.put("adjuntos", gson.toJson(adjuntos));

				// Parametros para la ejecucion del reporte
				HashMap<String, String>params = new HashMap<String, String>();
				params.put("idPrograma", 	  "168");
				params.put("nombreDocumento", "factura.pdf");
				params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
				params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
				params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
				params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
				params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
				params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
				params.put("usuario", 		  this.usuario);

				Respuesta respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte, correo);
				if(respuesta.getErrores().getCodigoError() != 0L) {
					control(6);
					this.mensaje = respuesta.getErrores().getDescError();
					log.info("ERROR 6: Error al timbrar --> " + this.mensaje.trim());
					return;
				}

				this.procesoIncompleto = false;
				this.tipoMensaje = 7;
				this.mensaje = "";
				log.info("INFO 7: Factura Enviada a " + this.email);
			}
    	} catch (Exception e) {
    		control(true);
    		log.error("Error en CuentasPorCobrar.FacturaAction.enviarFactura", e);
    	}
	}

	// ---------------------------------------
	// PROVISIONES
	// ---------------------------------------
	
	public void evaluaProvision() {
		List<FacturaPagos> listPagos = null;
		
		try {
			control();
			this.montoPagado = 0;
			this.montoProvision = 0;
			this.permiteProvisionar = false;
			
			// Cargamos los abonos
			listPagos = this.ifzPagos.findByProperty("fac.id", this.pojoFactura.getId());
			if (listPagos != null && ! listPagos.isEmpty()) {
				for (FacturaPagos var : listPagos) 
					this.montoPagado += var.getMonto().doubleValue();
			}
			
			this.montoFacturado = (new BigDecimal((new DecimalFormat("0.0000")).format(this.pojoFactura.getTotal()))).doubleValue();
			this.montoPagado 	= (new BigDecimal((new DecimalFormat("0.0000")).format(this.montoPagado))).doubleValue();
			this.montoProvision = this.montoFacturado - this.montoPagado;
			log.info("Sugerencia de monto de Provision: " + this.montoProvision);
			
			if (this.montoProvision <= 0) {
				control("La Factura no se puede provisionar.\nYa ha sido pagada");
				return;
			}

			if (this.pojoFactura.getProvisionada() == 1)
				this.permiteProvisionar = true;
		} catch (Exception e) {
			log.error("Error en CuentasPorCobrar.FacturaAction.evaluaProvision", e);
			control(true);
		}
	}
	
	public void provisionar() {
		boolean guardar = false;
		
		try {
			control();
			if (this.montoProvision <= 0) {
				log.info("Intento provisionar una factura sin monto de provision. Factura " + this.pojoFactura.getId() + " - " + this.pojoFactura.getFolioFactura());
				control("Debe indicar un monto para la Provision");
				return;
			}

			// Comprobamos la moneda
			log.info("Compruebo Moneda ... ");
			if (this.pojoFactura.getIdMoneda() == null && this.monedaBase != null) {
				this.pojoFactura.setIdMoneda(this.monedaBase);
				this.pojoFactura.setDescMoneda(this.monedaBase.getNombre());
				this.pojoFactura.setAbreviaturaMoneda(this.monedaBase.getAbreviacion());
				log.info("Asigno datos de Moneda. Pendiente actualizar ... ");
				guardar = true;
			}
			
			// Comprobamos los datos de cliente
			log.info("Compruebo datos de Cliente ... ");
			if (this.pojoFactura.getCliente() == null || "".equals(this.pojoFactura.getCliente())) {
				this.pojoFactura.setIdBeneficiario(this.pojoFactura.getIdObra().getIdCliente().getId());
				this.pojoFactura.setCliente(this.pojoFactura.getIdObra().getIdCliente().getNombre());
				this.pojoFactura.setBeneficiario(this.pojoFactura.getIdObra().getIdCliente().getNombre());
				this.pojoFactura.setRfc(this.pojoFactura.getIdObra().getIdCliente().getRfc());
				this.pojoFactura.setTipoBeneficiario(this.pojoFactura.getIdObra().getTipoCliente());
				if (this.pojoFactura.getTipoBeneficiario() == null || "".equals(this.pojoFactura.getTipoBeneficiario())) {
					if (this.pojoFactura.getIdObra().getIdCliente().getTipoPersona() == 1L)
						this.pojoFactura.setTipoBeneficiario("P");
					else
						this.pojoFactura.setTipoBeneficiario("N");
				}
				log.info("Asigno datos de Cliente. Pendiente actualizar ... ");
				guardar = true;
			}
			
			// Actualizamos los datos de la factura si corresponde
			if (guardar) {
				log.info("Actualizando Factura ... ");
				this.ifzFactura.update(this.pojoFactura);
				log.info("Factura actualizada.");
				
				// Lanzamos la transaccion en un hilo secundario
				new Thread(this).start();
			} else {
				log.info("Provisionando Factura ... ");
				Respuesta respuesta = this.ifzFactura.provisionar(this.pojoFactura, this.montoProvision, this.usuarioId);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					log.error("\nOcurrio un problema al intentar provisionar la Factura\n", (Throwable) respuesta.getBody().getValor("exception"));
					control(respuesta.getErrores().getDescError());
					return;
				}
				log.info("Factura Provisionada");
			}
			
			this.montoProvision = 0;
			this.montoFacturado = 0;
			this.montoPagado = 0;
		} catch (Exception e) {
			log.error("ERROR INTERNO: No se pudo enviar la Factura a Provision", e);
			control("Ocurrio un problema al intentar enviar la Factura a Provision");
		}
	}

	@Override
	public void run() {
		Respuesta respuesta = null;

		try {
			control();
			Thread.sleep(1000);
			log.info("Provisionando Factura ... ");
			respuesta = this.ifzFactura.provisionar(this.pojoFactura, this.montoProvision, this.usuarioId);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("\nOcurrio un problema al intentar provisionar la Factura\n", (Throwable) respuesta.getBody().getValor("exception"));
				control(respuesta.getErrores().getDescError());
				return;
			}

			log.info("Factura Provisionada");
		} catch (Exception e) {
			log.error("ERROR INTERNO: No se pudo enviar la Factura a Provision", e);
			control("Ocurrio un problema al intentar enviar la Factura a Provision");
		}
	}
	
	// ---------------------------------------
	// PROPIEDADES
	// ---------------------------------------

	public Factura getPojoFactura() {
		try {
			if (this.pojoFactura != null)
				return this.ifzFactura.convertir(this.pojoFactura);
		} catch (Exception e) {
			log.error("No puedo convertir el pojo extendido a Factura", e);
		}
		
		return new Factura();
	}
	
	public void setPojoFactura(Factura pojoFactura) {
		try {
			if (pojoFactura != null) {
				log.info("Extendiendo pojo Factura");
				setFactura(this.ifzFactura.convertir(pojoFactura));
			}
		} catch (Exception e) {
			log.error("No puedo convertir el pojo Factura a extendido", e);
		}
	}

	public boolean getTipoFactura() {
		if (this.pojoFactura != null && this.pojoFactura.getTipo() != null)
			return "C".equals(this.pojoFactura.getTipo());
		return false;
	}
	
	public void setTipoFactura(boolean tipoFactura) {
		if (this.pojoFactura != null)
			this.pojoFactura.setTipo(tipoFactura ? "C" : "X");
	}
	
	public String getTituloFactura() {
		if (this.pojoFactura == null || this.pojoFactura.getId() == null || this.pojoFactura.getIdObra() == null) 
			return "Nueva Factura";
		
		return "Factura " + this.pojoFactura.getFolioFactura();
	}
	
	public void setTituloFactura(String value) {}

	public String getNombreObra() {
		if (this.pojoFactura != null && this.pojoFactura.getIdObra() != null && this.pojoFactura.getIdObra().getNombre() != null)
			return this.pojoFactura.getIdObra().getId() + " - " + this.pojoFactura.getIdObra().getNombre();
		return "";
	}
	
	public void setNombreObra(String value) {}
	
	public String getNombreCliente() {
		if (this.pojoFactura != null && this.pojoFactura.getIdObra() != null && this.pojoFactura.getIdObra().getNombre() != null)
			return this.pojoFactura.getIdObra().getIdCliente().getId() + " - " + this.pojoFactura.getIdObra().getClienteNombre();
		return "";
	}
	
	public void setNombreCliente(String value) {}

	public String getConceptoDescripcion() {
		if (this.pojoDetalle != null && this.pojoDetalle.getIdConcepto() != null && this.pojoDetalle.getIdConcepto().getId() != null && this.pojoDetalle.getIdConcepto().getId() >= 0L)
			return this.pojoDetalle.getIdConcepto().getId() + " - " + this.pojoDetalle.getIdConcepto().getDescripcion();
		return "";
	}
	
	public void setConceptoDescripcion(String value) { }

	public FacturaExt getFactura(){
		return this.pojoFactura;
	}
	
	public void setFactura(FacturaExt factura) {
		this.pojoFactura = factura;
	}

	public FacturaDetalleExt getFacturaDetalle() {
		return pojoDetalle;
	}
	
	public void setFacturaDetalle(FacturaDetalleExt facturaDetalle) {
		this.pojoDetalle = facturaDetalle;
	}
	
	public Obra getPojoObra() {
		return this.pojoObra;
	}
		
	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public double getTotal() {
		return this.total;
	}
		
	public void setTotal(double total) {
		this.total = total;
	}
	
	public int getTipoMensaje() {
		return tipoMensaje;
	}
	
	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
	
	public List<Obra> getListaObras() {
		return listaObras;
	}
	
	public void setListaObras(List<Obra> listaObras) {
		this.listaObras = listaObras;
	}
	
	public List<FacturaDetalleExt> getListFacturaDetalles() {
		return listDetalles;
	}
		
	public void setListFacturaDetalles(List<FacturaDetalleExt> listFacturaDetalles) {
		this.listDetalles = listFacturaDetalles;
	}
	
	public double getSubtotal() {
		return this.subtotal;
	}
		
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
		
	public double getImpuestos() {
		return this.impuestos;
	}
		
	public void setImpuestos(double impuestos) {
		this.impuestos = impuestos;
	}
		
	public String getResOperacion() {
		return mensaje;
	}
	
	public void setResOperacion(String resOperacion) {
		this.mensaje = resOperacion;
	}
	
	public String getCampoBusqueda() {
		return campoBusqueda;
	}
		
	public void setCampoBusqueda(String campoBusqueda){
		this.campoBusqueda = campoBusqueda;
	}
		
	public String getCampoBusquedaProyecto() {
		return campoBusquedaObras;
	}
		
	public void setCampoBusquedaProyecto(String campoBusquedaProyecto) {
		this.campoBusquedaObras = campoBusquedaProyecto;
	}
		
	public String getValorBusqueda() {
		return valorBusqueda;
	}
	
	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}
	
	public String getValorBusquedaProyecto() {
		return valorBusquedaObras;
	}
		
	public void setValorBusquedaProyecto(String valorBusquedaProyecto) {
		this.valorBusquedaObras = valorBusquedaProyecto;
	}
		
	public int getTipoBusqueda() {
		return tipoBusqueda;
	}
	
	public void setTipoBusqueda(int tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}
		
	public int getTipoBusquedaProyecto() {
		return tipoBusquedaProyecto;
	}
	
	public void setTipoBusquedaProyecto(int tipoBusquedaProyecto) {
		this.tipoBusquedaProyecto = tipoBusquedaProyecto;
	}
	
	public List<Factura> getListFacturaGrid() {
		return listFacturas;
	}
	
	public void setListFacturaGrid(List<Factura> listFacturaGrid) {
		this.listFacturas = listFacturaGrid;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}
	
	public double getCosto() {
		return costo;
	}
	
	public void setCosto(double costo) {
		this.costo = costo;
	}
	
	public SelectItem getConceptoSeleccionado() {
		return conceptoSeleccionado;
	}
	
	public void setConceptoSeleccionado(SelectItem conceptoSeleccionado) {
		this.conceptoSeleccionado = conceptoSeleccionado;
	}
	
	public int getNumPaginaDetalles() {
		return numPaginaDetalles;
	}
	
	public void setNumPaginaDetalles(int numPaginaDetalles) {
		this.numPaginaDetalles = numPaginaDetalles;
	}
		
	public boolean isBand() {
		return procesoIncompleto;
	}
	
	public void setBand(boolean band) {
		this.procesoIncompleto = band;
	}
	
	public List<SelectItem> getListConceptoFacturacionItems() {
		return listConceptoFacturacionItems;
	}
	
	public void setListConceptoFacturacionItems(List<SelectItem> listConceptoFacturacionItems) {
		this.listConceptoFacturacionItems = listConceptoFacturacionItems;
	}
	
	public int getNumPaginaImpuestos() {
		return numPaginaImpuestos;
	}
	
	public void setNumPaginaImpuestos(int numPaginaImpuestos) {
		this.numPaginaImpuestos = numPaginaImpuestos;
	}
	
	public List<ConceptoFacturacionImpuestosExt> getListConceptoFacImpuestos() {
		return listDetalleImpuestos;
	}
	
	public void setListConceptoFacImpuestos(List<ConceptoFacturacionImpuestosExt> listConceptoFacImpuestos) {
		this.listDetalleImpuestos = listConceptoFacImpuestos;
	}
	
	public List<ConceptoFacturacionImpuestosExt> getListConceptoFacImpuestosEliminados() {
		return listDetalleImpuestosEliminados;
	}
	
	public void setListConceptoFacImpuestosEliminados(List<ConceptoFacturacionImpuestosExt> listConceptoFacImpuestosEliminados) {
		this.listDetalleImpuestosEliminados = listConceptoFacImpuestosEliminados;
	}
	
	public int getNumPaginaProyectos() {
		return paginacionObras;
	}
	
	public void setNumPaginaProyectos(int numPaginaProyectos) {
		this.paginacionObras = numPaginaProyectos;
	}
	
	public ConceptoFacturacionImpuestosExt getPojoConceptoImpuesto() {
		return pojoDetalleImpuesto;
	}
	
	public void setPojoConceptoImpuesto(ConceptoFacturacionImpuestosExt pojoConceptoImpuesto) {
		this.pojoDetalleImpuesto = pojoConceptoImpuesto;
	}
	
	public double getRetenciones() {
		return retenciones;
	}
	
	public void setRetenciones(double retenciones) {
		this.retenciones = retenciones;
	}

	public List<Sucursal> getListBusquedaSucursal() {
		return listBusquedaSucursal;
	}

	public void setListBusquedaSucursal(List<Sucursal> listBusquedaSucursal) {
		this.listBusquedaSucursal = listBusquedaSucursal;
	}

	public List<SelectItem> getTipoBusquedaSucursal() {
		return tipoBusquedaSucursal;
	}

	public void setTipoBusquedaSucursal(List<SelectItem> tipoBusquedaSucursal) {
		this.tipoBusquedaSucursal = tipoBusquedaSucursal;
	}

	public String getValorBusquedaSucursal() {
		return valorBusquedaSucursal;
	}

	public void setValorBusquedaSucursal(String valorBusquedaSucursal) {
		this.valorBusquedaSucursal = valorBusquedaSucursal;
	}

	public String getCampoBusquedaSucursal() {
		return campoBusquedaSucursal;
	}

	public void setCampoBusquedaSucursal(String campoBusquedaSucursal) {
		this.campoBusquedaSucursal = campoBusquedaSucursal;
	}
	
	public int getPaginaSucursales() {
		return paginaSucursales;
	}
	
	public void setPaginaSucursales(int paginaSucursales) {
		this.paginaSucursales = paginaSucursales;
	}

	public List<SelectItem> getListBusquedaSucursalItems() {
		return listBusquedaSucursalItems;
	}

	public void setListBusquedaSucursalItems(List<SelectItem> listBusquedaSucursalItems) {
		this.listBusquedaSucursalItems = listBusquedaSucursalItems;
	}

	public long getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(long sucursalId) {
		this.sucursalId = sucursalId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	public boolean isPuedeTimbrar() {
		return puedeTimbrar;
	}

	public void setPuedeTimbrar(boolean puedeTimbrar) {
		this.puedeTimbrar = puedeTimbrar;
	}
	
	public boolean isFacturaTimbrada() {
		return facturaTimbrada;
	}

	public void setFacturaTimbrada(boolean facturaTimbrada) {
		this.facturaTimbrada = facturaTimbrada;
	}
	
	public List<SelectItem> getListMetodosPagoItems() {
		return listMetodosPagoItems;
	}

	public void setListMetodosPagoItems(List<SelectItem> listMetodosPagoItems) {
		this.listMetodosPagoItems = listMetodosPagoItems;
	}

	public List<SelectItem> getListFormasPagoItems() {
		return listFormasPagoItems;
	}

	public void setListFormasPagoItems(List<SelectItem> listFormasPagoItems) {
		this.listFormasPagoItems = listFormasPagoItems;
	}

	public List<SelectItem> getOpcionesBusquedaProyecto() {
		return opcionesBusquedaObras;
	}

	public void setOpcionesBusquedaProyecto(List<SelectItem> opcionesBusquedaProyecto) {
		this.opcionesBusquedaObras = opcionesBusquedaProyecto;
	}
	
	public List<ConceptoFacturacion> getListConceptoFacturacion() {
		return listConceptoFacturacion;
	}

	public void setListConceptoFacturacion(List<ConceptoFacturacion> listConceptoFacturacion) {
		this.listConceptoFacturacion = listConceptoFacturacion;
	}

	public ConceptoFacturacion getPojoConcepto() {
		return pojoConcepto;
	}

	public void setPojoConcepto(ConceptoFacturacion pojoConcepto) {
		this.pojoConcepto = pojoConcepto;
	}
	
	public List<SelectItem> getTiposBusquedaConceptos() {
		return tiposBusquedaConceptos;
	}

	public void setTiposBusquedaConceptos(List<SelectItem> tiposBusquedaConceptos) {
		this.tiposBusquedaConceptos = tiposBusquedaConceptos;
	}

	public String getValBusquedaConceptos() {
		return valBusquedaConceptos;
	}

	public void setValBusquedaConceptos(String valBusquedaConceptos) {
		this.valBusquedaConceptos = valBusquedaConceptos;
	}

	public String getCampoBusquedaConceptos() {
		return campoBusquedaConceptos;
	}

	public void setCampoBusquedaConceptos(String campoBusquedaConceptos) {
		this.campoBusquedaConceptos = campoBusquedaConceptos;
	}

	public int getNumPaginaConceptos() {
		return numPaginaConceptos;
	}

	public void setNumPaginaConceptos(int numPaginaConceptos) {
		this.numPaginaConceptos = numPaginaConceptos;
	}

	public boolean getEsAdministrativo() {
		return perfilEgresos;
	}
	
	public void setEsAdministrativo(boolean esAdministrativo) {
		this.perfilEgresos = esAdministrativo;
	}
	
	public boolean isBuscarAdministrativo() {
		return buscarAdministrativo;
	}
	
	public void setBuscarAdministrativo(boolean buscarAdministrativo) {
		this.buscarAdministrativo = buscarAdministrativo;
	}

	public double getMontoFacturado() {
		return montoFacturado;
	}

	public void setMontoFacturado(double montoFacturado) {
		this.montoFacturado = montoFacturado;
	}

	public double getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(double montoPagado) {
		this.montoPagado = montoPagado;
	}

	public double getMontoProvision() {
		return montoProvision;
	}

	public void setMontoProvision(double montoProvision) {
		this.montoProvision = montoProvision;
	}
	
	public boolean getUserAdmin() {
		return ("ADMINISTRADOR".equals(this.usuario) ? true : false);
	}
	
	public void setUserAdmin(boolean value) {}

	public String getFacturaSerie() {
		return facturaSerie;
	}

	public void setFacturaSerie(String facturaSerie) {
		this.facturaSerie = facturaSerie;
	}

	public String getFacturaFolio() {
		return facturaFolio;
	}

	public void setFacturaFolio(String facturaFolio) {
		this.facturaFolio = facturaFolio;
	}
	
	public boolean getDebugging() {
		return this.isDebug;
	}
	
	public void setDebugging(boolean value) {
		this.isDebug = value;
	}

	public boolean isPermiteProvisionar() {
		return permiteProvisionar;
	}

	public void setPermiteProvisionar(boolean permiteProvisionar) {
		this.permiteProvisionar = permiteProvisionar;
	}

	public Factura getPojoFacturaBorrar() {
		return pojoFacturaBorrar;
	}

	public void setPojoFacturaBorrar(Factura pojoFacturaBorrar) {
		this.pojoFacturaBorrar = pojoFacturaBorrar;
	}
}

// HISTORIAL DE MODIFICACIONES 
// -------------------------------------------------------------------------------------------------------------------
// VERSION |    FECHA    |        AUTOR       | DESCRIPCION 
// -------------------------------------------------------------------------------------------------------------------
//    1.0  | 2016-10-18  | Javier Tirado      | Agregamos el listado de metodos de pago 
//    2.2  | 2017-05-15  | Javier Tirado      | Agilizamos la busqueda de obras (cambio de extendido a normal)
