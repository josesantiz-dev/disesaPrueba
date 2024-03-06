package net.giro.compras;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;
import java.util.Set;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraAlmacenesExt;
import net.giro.adp.logica.ObraAlmacenesRem;
import net.giro.adp.logica.ObraRem;
import net.giro.clientes.beans.PersonaExt;
import net.giro.compras.beans.Cotizacion;
import net.giro.compras.beans.CotizacionDetalle;
import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.beans.OrdenCompraDetalle;
import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.logica.CotizacionDetalleRem;
import net.giro.compras.logica.CotizacionRem;
import net.giro.compras.logica.OrdenCompraDetalleRem;
import net.giro.compras.logica.OrdenCompraRem;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.TipoMaestro;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.Moneda;

@ViewScoped
@ManagedBean(name="cargaOCAction")
public class CargaOrdenComprasAction implements Runnable,Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CargaOrdenComprasAction.class);
	private PropertyResourceBundle cargaProps;
	private LoginManager loginManager;	
	private InitialContext ctx;
	// Interfaces
	private RequisicionRem ifzReq;
	private CotizacionRem ifzCoti;
	private OrdenCompraRem ifzOC;
	private RequisicionDetalleRem ifzReqDetalles;
	private CotizacionDetalleRem ifzCotiDetalles;
	private OrdenCompraDetalleRem ifzOCDetalles;
	private ProductoRem ifzProds;
	private ObraRem ifzObras;
	private EmpleadoRem ifzEmpleados;
	private ObraAlmacenesRem ifzObraAlmacen;
	private AlmacenProductoRem ifzAlmacenProducto;
	private AlmacenTraspasoRem ifzTraspaso;
	private TraspasoDetalleRem ifzTraspasoDetalle;		
	// POJO's
	private Requisicion pojoRequisicion;
	private Cotizacion pojoCotizacion;
	private OrdenCompra pojoOrdenCompra;
	private Obra pojoObra;
	private Empleado pojoSolicita;
	private Empleado pojoComprador;
	private PersonaExt pojoProveedor;
	private PersonaExt pojoContacto;	
	private List<Producto> listProductos;
	private List<AlmacenTraspasoExt> listTraspasos;
	private List<TraspasoDetalleExt> listTraspasosDetalles;	
	// Variables de operacion
	private String fileName;
	private byte[] fileSrc; 
	private LinkedHashMap<String, byte[]> listOC;
	private LinkedHashMap<String, String> listErrores;
	private long usuarioId = 0;
	//private String usuario = "";
	private boolean operacionCancelada;
	private String mensaje;
	private int tipoMensaje;
	private boolean procesando;
	private List<String> strBitacora;
	private long idParent;
	private double porcentajeIva;
	private String folioOC;
	private String folioCotizacion;
	private int consecutivoProveedorOC;
	private int consecutivoProveedorCoti;
	private String tipoPersonaProveedor;
	private long idMoneda;
	private BigDecimal tipoCambio;
	private BigDecimal anticipo;
	private double flete;
	private int plazo;
	private int tiempoEntrega;
	private String lugarEntrega;
	private int maxCargas;
	private HashMap<String, String> cellReference;
	// Busqueda Ordenes de Compra
	private List<OrdenCompra> listOrdenesCompra;
	private List<SelectItem> OCBusquedaTipos;
	private String OCBusquedaCampo;
	private String OCBusquedaValor;
	private int OCBusquedaPagina;
	// Reemplazar Orden de Compra
	private boolean reemplazarOrdenCompra;
	private OrdenCompra pojoOrdenCompraReemplazar;
	private OrdenCompra pojoOrdenCompraReemplazarBK;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;

	
	public CargaOrdenComprasAction() {
		Map<String, String> params = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			params = fc.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());			
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{carga}", PropertyResourceBundle.class);
			this.cargaProps = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			//this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			valPerfil = this.loginManager.getAutentificacion().getPerfil("VALOR_IVA");
			this.porcentajeIva = (valPerfil == null || "".equals(valPerfil.trim())) ? 0 : Double.valueOf(valPerfil.trim());

			// Interfaces
			this.ctx = new InitialContext();
			this.ifzReq  			= (RequisicionRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzCoti 			= (CotizacionRem)  this.ctx.lookup("ejb:/Logica_Compras//CotizacionFac!net.giro.compras.logica.CotizacionRem");
			this.ifzOC   			= (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			this.ifzReqDetalles 	= (RequisicionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzCotiDetalles 	= (CotizacionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//CotizacionDetalleFac!net.giro.compras.logica.CotizacionDetalleRem");
			this.ifzOCDetalles 		= (OrdenCompraDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraDetalleFac!net.giro.compras.logica.OrdenCompraDetalleRem");
			this.ifzProds 			= (ProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzObras 			= (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzEmpleados 		= (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzObraAlmacen 	= (ObraAlmacenesRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraAlmacenesFac!net.giro.adp.logica.ObraAlmacenesRem");
			this.ifzAlmacenProducto = (AlmacenProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			this.ifzTraspaso 		= (AlmacenTraspasoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
			this.ifzTraspasoDetalle = (TraspasoDetalleRem) this.ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");

			this.ifzReq.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCoti.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOC.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReqDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzCotiDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzOCDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProds.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObraAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacenProducto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspaso.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspasoDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.ifzReq.showSystemOuts(false);
			this.ifzCoti.showSystemOuts(false);
			this.ifzOC.showSystemOuts(false);
			this.ifzReqDetalles.showSystemOuts(false);
			this.ifzCotiDetalles.showSystemOuts(false);
			this.ifzOCDetalles.showSystemOuts(false);
			this.ifzObras.showSystemOuts(false);

			// BUSQUEDA ORDENES DE COMPRA
			this.listOrdenesCompra = new ArrayList<OrdenCompra>();
			this.OCBusquedaTipos = new ArrayList<SelectItem>();
			this.OCBusquedaTipos.add(new SelectItem("folio","Folio"));
			this.OCBusquedaTipos.add(new SelectItem("nombreObra","Obra"));
			this.OCBusquedaTipos.add(new SelectItem("nombreProveedor","Proveedor"));
			this.OCBusquedaTipos.add(new SelectItem("nombreSolicita","Solicita"));
			this.OCBusquedaTipos.add(new SelectItem("id","ID"));
			this.OCBusquedaCampo = this.OCBusquedaTipos.get(0).getValue().toString();
			this.OCBusquedaValor = "";
			this.OCBusquedaPagina = 1;
			
			this.maxCargas = 1;
			if (this.isDebug && this.paramsRequest.containsKey("MULTI_CARGA"))
				this.maxCargas = 0; 
		} catch (Exception e) {
			log.error("Error en constructor CargaOrdenComprasAction", e);
			this.ctx = null;
		}
	}
	
	
	public void nuevo() {
		try {
			control(false, 0, null);
			
			this.fileName = "";
			this.fileSrc = null;
			this.strBitacora = null;
		} catch (Exception e) {
			control(true, 1, null);
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.nuevo", e);
		}
	}
	
	public void uploadListener(FileUploadEvent event) {
		try {
			control(false, 0, null);
			
			if (this.strBitacora != null)
				this.strBitacora.clear();
			bitacora(null, false);
			
			this.fileName = event.getUploadedFile().getName();
			this.fileSrc = event.getUploadedFile().getData();
			
			if (this.listOC == null)
				this.listOC = new LinkedHashMap<String, byte[]>();
			this.listOC.put(this.fileName, this.fileSrc);
		} catch (Exception e) {
			control(true, 1, null);
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.uploadListener", e);
		}
	}
	
	/**
	 * Lanza un hilo independiente con el procesamiento del archivo para desocupar al hilo principal
	 */
	public void procesar() {
		try {
			control(false, 0, null);
			bitacora(null, false);
			
			if (this.listErrores == null)
				this.listErrores = new LinkedHashMap<String, String>();
			this.listErrores.clear();
			
			if(this.listOC == null || this.listOC.isEmpty()) {
				control(true, -1, "Falta indicar el archivo");
				return;
			}
			
			// Comprobamos archivo
			// ----------------------------------------------------------------------------------
			try {
				bitacora("Preparando... ", false); 
				if (this.cellReference == null)
					this.cellReference = new HashMap<String, String>();
				this.cellReference.clear();
	            Set<String> keys = this.cargaProps.keySet();
	            for(String key : keys)
	            	this.cellReference.put(key, this.cargaProps.getString(key));
	            
	            if (this.cellReference.containsKey("FACTURAS"))
	            	this.cellReference.remove("FACTURAS");
			} catch (Exception e) {
				bitacora("Preparando... ERROR", true); 
				bitacora("Carga fallida...", false);  
				control(true, 1, null);
				log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.procesar", e);
				return;
			}
			
			// Lanzamos hilo con el procesamiento del archivo para desocupar el hilo principal y usarlo para reportar el avance
			this.procesando = true;
			new Thread(this).start();
			log.info("proceso iniciado");
		} catch (Exception e) {  
			control(true, 1, null);
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.procesar", e);
		}
	}

	@Override
	public void run() {
		boolean multiCarga = false;
		
		try {
			control(false, 0, null);
			multiCarga = (this.listOC.size() > 1) ? true : false;

			// Sobreescribirmos el metodo Run (Runnable) para ejecutar el metodo de procesamiento de archivo
			for (Entry<String, byte[]> entry : this.listOC.entrySet()) {
				this.fileName = entry.getKey();
				this.fileSrc = entry.getValue();
				
				log.info("---> Procesando... " + this.fileName + " <---");
				if (multiCarga) 
					bitacora("---> Procesando... " + this.fileName + " <---", false); 
				this.doProcesar();
				this.fileName = (multiCarga) ? "" : this.fileName;
				this.fileSrc = (multiCarga) ? null : this.fileSrc;
			}
			
			log.info("---------------------------------------------------------");
			log.warn(this.listErrores.toString());
			log.info("---------------------------------------------------------");
			this.listOC.clear();
			this.listErrores.clear();
		} catch (Exception e) {
			control(true, 1, null);
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.run", e);
		} finally {
			this.procesando = false;
			log.info("proceso finalizado");
		}
	}
	
	/**
	 * Genera el procesamiento de lectura de archivo
	 * @author Javier Tirado
	 */
	@SuppressWarnings("unchecked")
	public void doProcesar() {
		boolean primeraCotizacion = false;
	
		try {
			control(false, 0, null);
			bitacora("Preparando... OK", true); 

			try {
				bitacora("Comprobando archivo... ", false); 
				if(this.fileSrc == null || this.fileSrc.length <= 0) {
					bitacora("Comprobando archivo... ERROR", true); 
					bitacora("Carga fallida...", false);  
					control(true, 1, null);
					return;
				}
				bitacora("Comprobando archivo... OK", true); 
			} catch (Exception e) {
				bitacora("Comprobando archivo... ERROR", true); 
				bitacora("Carga fallida...", false);  
				control(true, 1, null);
				return;
			}
			
			// procesamos archivo
			// ----------------------------------------------------------------------------------
			bitacora("Comprobando estructura... ", false);
			Respuesta result = this.ifzOC.procesarArchivo(this.fileSrc, this.cellReference);
			if (result.getErrores().getCodigoError() != 0L) {
				log.error("Error al realizar PROCESO DE LECTURA DE ARCHIVO");
				bitacora("Comprobando estructura... ERROR", true); 
				bitacora("Carga fallida...", false);
				control(true, -1, result.getErrores().getDescError());
				this.listErrores.put(this.fileName, this.mensaje); 
				this.fileSrc = null;
				return;
			}
			
			// Validamos datos devueltos
			// ----------------------------------------------------------------------------------
			if (! validarRespuesta((HashMap<String, Object>) result.getBody().getValor("encabezados"))) {
				log.error("Error al realizar VALIDACION DE DATOS REQUERIDOS");
				bitacora("Comprobando estructura... ERROR", true); 
				bitacora("Carga fallida...", false); 
				return;
			}
			
			// Recuperamos listas y validamos		
			HashMap<String, HashMap<String, String>> sinProcesar = (HashMap<String, HashMap<String, String>>) result.getBody().getValor("sin_procesar");
			if (sinProcesar != null && ! sinProcesar.isEmpty()) {
				control(true, -1, "Error al procesar la Orden de Compra. Hay registros con informacion faltante.");
				log.error("Error al comprobar la respuesta. validando productos. Productos con informacion faltante");
				this.listErrores.put(this.fileName, this.mensaje + sinProcesar); 
				bitacora("Comprobando estructura... ERROR", true); 
				bitacora("Carga fallida...", false);
				return;
			}

			// Generamos lista de productos
			// ----------------------------------------------------------------------------------
			if (! validarProductos((HashMap<String, String>) result.getBody().getValor("productos"))) {
				log.error("Error al realizar VALIDACION DE PRODUCTOS");
				bitacora("Comprobando estructura... ERROR", true); 
				bitacora("Carga fallida...", false);
				return;
			}
			bitacora("Comprobando estructura... OK", true); 
			
			if (this.isDebug) {
				if (this.paramsRequest.containsKey("BREAK"))
					throw new Exception("Proceso interrumpido");
				
				if (this.paramsRequest.containsKey("REPLACE")) {
					this.pojoOrdenCompraReemplazar = this.ifzOC.findById(Long.valueOf(this.paramsRequest.get("REPLACE")));
					// TODO: Secuencia para reemplazar los valores de la Orden de Compra
					return;
				}
			}
			
			// Validamos que no exista la OC
			List<OrdenCompra> listAux = this.ifzOC.findByProperty("folio", (this.fileName.replace(".xls", "").replace(".xlsx", "")), 0);
			if (listAux != null && ! listAux.isEmpty()) {
				log.warn("Esta OC ya fue cargada previamente");
			}
			
			// Generamos REQUISICION
			// ----------------------------------------------------------------------------------
			try {
				bitacora("Generando Requisicion... ", false); 
				this.pojoRequisicion = new Requisicion();
				this.pojoRequisicion.setIdObra(this.pojoObra.getId());
				this.pojoRequisicion.setNombreObra(this.pojoObra.getNombre());
				this.pojoRequisicion.setIdSolicita(this.pojoSolicita.getId());
				this.pojoRequisicion.setNombreSolicita(this.pojoSolicita.getNombre());
				this.pojoRequisicion.setCreadoPor(this.usuarioId);
				this.pojoRequisicion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoRequisicion.setTipo(TipoMaestro.Inventario.ordinal());
				this.pojoRequisicion.setSistema(1);
				
				// Guardamos REQUISICION
				idParent = this.ifzReq.save(this.pojoRequisicion);
				this.pojoRequisicion.setId(idParent);
				
				// Detalles REQUISICION
				RequisicionDetalle detR = null;
				for (Producto p : this.listProductos) {
					detR = new RequisicionDetalle();
					detR.setIdRequisicion(idParent);
					detR.setIdProducto(p.getId());
					detR.setCantidad(p.getExistencia());
					detR.setCreadoPor(this.usuarioId);
					detR.setFechaCreacion(Calendar.getInstance().getTime());
					detR.setModificadoPor(this.usuarioId);
					detR.setFechaModificacion(Calendar.getInstance().getTime());
					detR.setId(ifzReqDetalles.save(detR));
				}
				
				idParent = 0L;
				bitacora("Generando Requisicion... OK ---> " + this.pojoRequisicion.getId(), true); 
			} catch (Exception e) {
				this.listErrores.put(this.fileName, e.getMessage()); 
				log.error("Error al realizar REQUISICION", e);
				bitacora("Generando Requisicion... ERROR", true); 
				bitacora("Carga fallida...", false);  
				control(true, 1, null);
				dropValues();
				return;
			}
			
			// Generamos COTIZACION
			// ----------------------------------------------------------------------------------
			try {
				bitacora("Generando Cotizacion... ", false); 
				this.pojoCotizacion = new Cotizacion();
				this.pojoCotizacion.setFolio(this.folioCotizacion);
				this.pojoCotizacion.setIdRequisicion(this.pojoRequisicion.getId());
				this.pojoCotizacion.setIdObra(this.pojoObra.getId());
				this.pojoCotizacion.setNombreObra(this.pojoObra.getNombre());
				this.pojoCotizacion.setIdComprador(this.pojoComprador.getId());
				this.pojoCotizacion.setNombreComprador(this.pojoComprador.getNombre());
				this.pojoCotizacion.setIdProveedor(this.pojoProveedor.getId());
				this.pojoCotizacion.setNombreProveedor(this.pojoProveedor.getNombre());
				this.pojoCotizacion.setTipoPersonaProveedor(this.tipoPersonaProveedor);
				this.pojoCotizacion.setConsecutivoProveedor(this.consecutivoProveedorCoti);
				this.pojoCotizacion.setIdContacto(this.pojoContacto.getId());
				this.pojoCotizacion.setNombreContacto(this.pojoContacto.getNombre());
				this.pojoCotizacion.setTiempoEntrega(this.tiempoEntrega);
				this.pojoCotizacion.setFlete(this.flete);
				this.pojoCotizacion.setIva(0D);
				this.pojoCotizacion.setCreadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoCotizacion.setModificadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoCotizacion.setTipo(TipoMaestro.Inventario.ordinal());
				this.pojoCotizacion.setSistema(1);
				
				primeraCotizacion = this.comprobarPrimeraCotizacion();
								
				// Detalles COTIZACION
				List<CotizacionDetalle> cotiDetalles = new ArrayList<CotizacionDetalle>();
				CotizacionDetalle detC = null;
				for (Producto p : this.listProductos) {
					detC = new CotizacionDetalle();
					detC.setIdProducto(p.getId());
					detC.setCantidad(p.getExistencia());
					detC.setCotizar(p.getExistencia());
					detC.setPrecioUnitario(p.getPrecioUnitario());
					detC.setMargen(0);
					detC.setCreadoPor(this.usuarioId);
					detC.setFechaCreacion(Calendar.getInstance().getTime());
					detC.setModificadoPor(this.usuarioId);
					detC.setFechaModificacion(Calendar.getInstance().getTime());
					cotiDetalles.add(detC);
				}
				
				// Sacamos totales cotizacion
				totalizarCotizacionesDetalles(cotiDetalles);

				// Guardamos COTIZACION
				idParent = this.ifzCoti.save(this.pojoCotizacion);
				this.pojoCotizacion.setId(idParent);
				
				for (CotizacionDetalle var : cotiDetalles) {
					var.setIdCotizacion(idParent);
					if (primeraCotizacion)
						generaTraspaso(var);
					var.setId(ifzCotiDetalles.save(var));
				}
				
				idParent = 0L;
				cotiDetalles.clear();
				bitacora("Generando Cotizacion... OK ---> " + this.pojoCotizacion.getId(), true); 
			} catch (Exception e) {
				this.listErrores.put(this.fileName, e.getMessage()); 
				log.error("Error al realizar COTIZACION", e);
				bitacora("Generando Cotizacion... ERROR", true); 
				bitacora("Carga fallida...", false);  
				control(true, 1, null);
				dropValues();
				return;
			}
			
			// Generamos ORDEN DE COMPRA
			// ----------------------------------------------------------------------------------
			try {
				bitacora("Generando Orden de Compra... ", false); 
				this.pojoOrdenCompra = new OrdenCompra();
				this.pojoOrdenCompra.setFolio(this.folioOC);
				this.pojoOrdenCompra.setAnticipo(BigDecimal.ZERO);
				this.pojoOrdenCompra.setIdCotizacion(this.pojoCotizacion.getId());
				this.pojoOrdenCompra.setIdObra(this.pojoObra.getId());
				this.pojoOrdenCompra.setNombreObra(this.pojoObra.getNombre());
				this.pojoOrdenCompra.setIdProveedor(this.pojoProveedor.getId());
				this.pojoOrdenCompra.setNombreProveedor(this.pojoProveedor.getNombre());
				this.pojoOrdenCompra.setTipoPersonaProveedor(this.tipoPersonaProveedor);
				this.pojoOrdenCompra.setConsecutivoProveedor(this.consecutivoProveedorOC);
				this.pojoOrdenCompra.setIdContacto(this.pojoContacto.getId());
				this.pojoOrdenCompra.setNombreContacto(this.pojoContacto.getNombre());
				this.pojoOrdenCompra.setIdSolicita(this.pojoSolicita.getId());
				this.pojoOrdenCompra.setNombreSolicita(this.pojoSolicita.getNombre());
				this.pojoOrdenCompra.setIdMoneda(this.idMoneda);
				this.pojoOrdenCompra.setTipoCambio(this.tipoCambio);
				this.pojoOrdenCompra.setPlazo(this.plazo);
				this.pojoOrdenCompra.setAnticipo(this.anticipo);
				this.pojoOrdenCompra.setTiempoEntrega(this.tiempoEntrega);
				this.pojoOrdenCompra.setLugarEntrega(this.lugarEntrega);
				this.pojoOrdenCompra.setCreadoPor(this.usuarioId);
				this.pojoOrdenCompra.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoOrdenCompra.setModificadoPor(this.usuarioId);
				this.pojoOrdenCompra.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoOrdenCompra.setTipo(TipoMaestro.Inventario.ordinal());
				this.pojoOrdenCompra.setSistema(1);

				// Detalles ORDEN DE COMPRA
				List<OrdenCompraDetalle> ocDetalles = new ArrayList<OrdenCompraDetalle>();
				OrdenCompraDetalle detO = null;
				for (Producto p : this.listProductos) {
					detO = new OrdenCompraDetalle();
					detO.setIdProducto(p.getId());
					detO.setCantidad(p.getExistencia());
					detO.setCantidadRecibida(p.getExistencia());
					detO.setPrecioUnitario(p.getPrecioUnitario());
					detO.setCreadoPor(this.usuarioId);
					detO.setFechaCreacion(Calendar.getInstance().getTime());
					detO.setModificadoPor(this.usuarioId);
					detO.setFechaModificacion(Calendar.getInstance().getTime());
					ocDetalles.add(detO);
				}
				
				// Sacamos totales cotizacion
				totalizarOrdenCompraDetalles(ocDetalles);
				
				// Guardamos ORDEN DE COMPRA
				idParent = this.ifzOC.save(this.pojoOrdenCompra);
				this.pojoOrdenCompra.setId(idParent);
				
				for (OrdenCompraDetalle var : ocDetalles) {
					var.setIdOrdenCompra(idParent);
					ifzOCDetalles.save(var);
				}
				
				idParent = 0L;
				ocDetalles.clear();
				bitacora("Generando Orden de Compra... OK ---> " + this.pojoOrdenCompra.getId(), true); 
				this.listErrores.put(this.fileName, "OK"); 
			} catch (Exception e) {
				this.listErrores.put(this.fileName, e.getMessage()); 
				log.error("Error al realizar ORDEN DE COMPRA", e);
				bitacora("Generando Orden de Compra... ERROR", true); 
				control(true, 1, null);
				dropValues();
				return;
			}
			
			// Guardamos los traspasos si corresponde
			try {
				if (primeraCotizacion) {
					bitacora("Terminando... ", false); 
					guardaTraspasos();
					bitacora("Terminando... OK", true); 
				}
			} catch (Exception e) {
				this.listErrores.put(this.fileName, "Error al realizar TRASPASOS"); 
				log.error("Error al realizar TRASPASOS", e);
				bitacora("Terminando... ERROR", true);
				bitacora("Carga fallida...", false);  
				control(true, 1, null);
				dropValues();
				return;
			}

			bitacora("Proceso terminado... ", false); 
			control(false, 0, "\nSe generó la Orden de Compra " + this.pojoOrdenCompra.getId());
			this.pojoRequisicion = null;
			this.pojoCotizacion = null;
			this.pojoOrdenCompra = null;
			this.fileSrc = null;
		} catch (Exception e) {
			this.listErrores.put(this.fileName, e.getMessage()); 
			bitacora("Carga fallida...", false);  
			control(true, 1, "ERROR");
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.doProcesar", e);
		} 
	}
	
	private boolean validarRespuesta(HashMap<String, Object> values) {
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
		String idPro = "";
		String annio = "";
		String valStr = "";
		long valId = 0L;

		try {
			// OBRA
			valId = (values.containsKey("OBRA") ? (long) ((double) values.get("OBRA")) : 0L); 
			this.pojoObra = ifzObras.findById(valId);
			if (this.pojoObra == null) {
				control(true, -1, "Obra no existe");
				this.listErrores.put(this.fileName, this.mensaje + ": " + valId); 
				return false;
			}
			
			// SOLICITA
			valId = ((values.containsKey("SOLICITA")) ? (long) ((double) values.get("SOLICITA")) : 0L); 
			this.pojoSolicita = ifzEmpleados.findById(valId);
			if (this.pojoSolicita == null) {
				control(true, -1, "Solicita no existe");
				this.listErrores.put(this.fileName, this.mensaje + ": " + valId); 
				return false;
			}
			
			// COMPRADOR
			valId = ((values.containsKey("COMPRADOR")) ? (long) ((double) values.get("COMPRADOR")) : 0L); 
			this.pojoComprador = ifzEmpleados.findById(valId);
			if (this.pojoComprador == null) {
				control(true, -1, "Comprador no existe");
				this.listErrores.put(this.fileName, this.mensaje + ": " + valId); 
				return false;
			}
			
			// PROVEEDOR
			valStr = (values.containsKey("PROVEEDOR")) ? values.get("PROVEEDOR").toString().trim() : ""; 
			if (! "".equals(valStr.trim())) {
				List<PersonaExt> list = new ArrayList<PersonaExt>(); 
				if (valStr.contains("-"))
					valStr = valStr.replace("-", "");
				
				if (valStr.trim().length() == 13) {
					this.tipoPersonaProveedor = "P";
				} else if (valStr.trim().length() == 12) {
					this.tipoPersonaProveedor = "N";
				} else {
					control(true, -1, "El RFC del proveedor no es valido");
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				
				list = this.ifzCoti.findPersonaLikeProperty("rfc", valStr.trim(), this.tipoPersonaProveedor, 0);
				if (list.isEmpty()) {
					if ("P".equals(this.tipoPersonaProveedor)) {
						this.tipoPersonaProveedor = "N";
						list = this.ifzCoti.findPersonaLikeProperty("rfc", valStr.trim(), this.tipoPersonaProveedor, 0);
						if (list.isEmpty()) {
							control(true, -1, "Proveedor no existe");
							this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
							return false;
						}
					} else {					
						control(true, -1, "Proveedor no existe");
						this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
						return false;
					}
				}
				
				this.pojoProveedor = list.get(0);
			}
			
			// CONTACTO
			this.pojoContacto = this.ifzCoti.findContactoByProveedor(this.pojoProveedor, null);
			if (this.pojoContacto == null) {
				this.pojoContacto = new PersonaExt();
				this.pojoContacto.setId(0L);
				this.pojoContacto.setNombre("");
			}
			
			// FOLIO ORDEN COMPRA
			this.folioOC = (values.containsKey("OC")) ? values.get("OC").toString() : "";
			if ("".equals(this.folioOC)) {
				idPro = String.valueOf(this.pojoProveedor.getId());
				annio = formateador.format(Calendar.getInstance().getTime());

				idPro = idPro.substring(idPro.length() - 4);
				annio = annio.substring(annio.length() - 2);
				this.consecutivoProveedorOC = this.ifzOC.findConsecutivoByProveedor(this.pojoProveedor.getId());
				this.folioOC = idPro + "-" + annio + "-" + ((this.consecutivoProveedorOC < 10) ? "0" : "") + this.consecutivoProveedorOC;
			}
			
			// FOLIO COTIZACION
			idPro = String.valueOf(this.pojoProveedor.getId());
			idPro = idPro.substring(idPro.length() - 4);
			annio = formateador.format(Calendar.getInstance().getTime());
			annio = annio.substring(annio.length() - 2);
			this.consecutivoProveedorCoti = this.ifzCoti.findConsecutivoByProveedor(this.pojoProveedor.getId());
			this.folioCotizacion = idPro + "-" + annio + "-" + ((this.consecutivoProveedorCoti < 10) ? "0" : "") + this.consecutivoProveedorCoti;

			this.anticipo = BigDecimal.ZERO;
			if (! values.containsKey("ANTICIPO") || ! Double.class.equals(values.get("ANTICIPO").getClass())) {
				control(true, -1, "El anticipo no tiene el formato correcto o no fue asignado. Tipo Númerico");
				this.listErrores.put(this.fileName, this.mensaje); 
				return false;
			}	
			this.anticipo = (new BigDecimal((double) values.get("ANTICIPO")));
			
			this.idMoneda = 0L;
			valStr = (values.containsKey("MONEDA")) ? values.get("MONEDA").toString() : "";
			this.idMoneda = getMoneda(valStr); 
			if (this.idMoneda <= 0L) {
				control(true, -1, "La moneda asignada no es valida.");
				this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
				return false;
			}
			
			this.tipoCambio = BigDecimal.ZERO;
			if (! values.containsKey("TIPO_CAMBIO") || ! Double.class.equals(values.get("TIPO_CAMBIO").getClass())) {
				control(true, -1, "El tipo de cambio no tiene el formato correcto o no fue asignado. Tipo Númerico");
				this.listErrores.put(this.fileName, this.mensaje); 
				return false;
			}	
			this.tipoCambio = (new BigDecimal((double) values.get("TIPO_CAMBIO")));
			
			this.flete = 0;
			if (! values.containsKey("FLETE") || ! Double.class.equals(values.get("FLETE").getClass())) {
				control(true, -1, "El flete no tiene el formato correcto o no fue asignado. Tipo Númerico");
				this.listErrores.put(this.fileName, this.mensaje); 
				return false;
			}
			this.flete = (double) values.get("FLETE");
			
			this.plazo = 0;
			if (! values.containsKey("PLAZO") || ! Double.class.equals(values.get("PLAZO").getClass())) {
				control(true, -1, "El plazo no tiene el formato correcto o no fue asignado. Tipo Númerico");
				this.listErrores.put(this.fileName, this.mensaje); 
				return false;
			}
			this.plazo = ((Double) values.get("PLAZO")).intValue();
			
			this.tiempoEntrega = 0;
			if (! values.containsKey("TIEMPO_ENTREGA") || ! Double.class.equals(values.get("TIEMPO_ENTREGA").getClass())) {
				control(true, -1, "El tiempo de entrega no tiene el formato correcto o no fue asignado. Tipo Númerico");
				this.listErrores.put(this.fileName, this.mensaje); 
				return false;
			}
			this.tiempoEntrega = ((Double) values.get("TIEMPO_ENTREGA")).intValue();
			
			this.lugarEntrega = (values.containsKey("LUGAR_ENTREGA")) ? values.get("LUGAR_ENTREGA").toString() : "";
		} catch (Exception e) {
			log.error("Error al comprobar la respuesta. validando encabezados", e);
			this.listErrores.put(this.fileName, e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private boolean validarProductos(HashMap<String, String> values) {
		Producto p = null;
		
		try {
			if (this.listProductos == null)
				this.listProductos = new ArrayList<Producto>();
			this.listProductos.clear();
			
			if (values == null || values.isEmpty()) {
				control(true, -1, "La productos no tienen codigo");
				throw new Exception("La lista no contiene productos");
			}

			for (Map.Entry<String, String> entry : values.entrySet()) {
				p = this.ifzProds.findByClave(entry.getKey());
				if (p == null) {
					control(true, -1, "No se encontro ningun producto con clave " + entry.getKey());
					this.listErrores.put(this.fileName, this.mensaje); 
					log.error("Error al comprobar la respuesta. validando productos. No se encontro el producto " + entry.getKey());
					return false;
				}
				
				p.setExistencia(getVal(entry.getValue(), 0));
				p.setPrecioUnitario(getVal(entry.getValue(), 1));
				this.listProductos.add(p);
			}
		} catch (Exception e) {
			log.error("Error al comprobar la respuesta. validando productos.", e);
			this.listErrores.put(this.fileName, e.getMessage()); 
			return false;
		}
		
		return true;
	}
	
	private long getMoneda(String value) throws Exception {
		List<Moneda> listaMonedas = null;
		
		if (value == null || "".equals(value))
			return 0L;
		
		if ("M.N.".equals(value))
			value = "PESOS";
		
		try {
			listaMonedas = this.ifzOC.findMonedas();
			if (listaMonedas != null && ! listaMonedas.isEmpty()) {
				for (Moneda var : listaMonedas) {
					if (value.toUpperCase().equals(var.getNombre().toUpperCase()))
						return var.getId();
				}
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.getMoneda('" + value + "')", e);
		}
		
		return 0L;
	}
	
	private void control() {
		control(false, 0, null);
	}
	
	private void control(boolean operacion, int tipoMensaje, String mensaje) {
		this.operacionCancelada = operacion;
		this.tipoMensaje = tipoMensaje;
		
		this.mensaje = "";
		if (mensaje != null)
			this.mensaje = mensaje;
	}
	
	private void bitacora(String mensaje, boolean replacePrev) {
		if (mensaje == null || "".equals(mensaje)) {
			this.strBitacora = new ArrayList<String>();
			return;
		}

		this.strBitacora.add(mensaje);
		log.info("---> " + mensaje);
		if (replacePrev)
			this.strBitacora.remove(this.strBitacora.size() - 2);
	}

	// ----------------------------------------------------------------------------------------
	// TRASPASO
	// ----------------------------------------------------------------------------------------
	
	private boolean comprobarPrimeraCotizacion() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		try {
			if (this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L)
				return false;
			
			params.put("idObra", this.pojoObra.getId());
			params.put("estatus", 0);
			
			return this.ifzCoti.findByProperties(params).isEmpty();
		} catch (Exception e) {
			return false;
		}
	}
	
	private void generaTraspaso(CotizacionDetalle item) {
		HashMap<String, String> params = new HashMap<String, String>();
		List<ObraAlmacenesExt> almacenesObra = null;
		ObraAlmacenesExt almacenObra = null;
		ObraAlmacenesExt almacenObraOrigen = null;
		Empleado empRecibe = null;
		AlmacenTraspasoExt pojoTraspaso = null;
		boolean existeTraspaso = false;
		double cantidadATraspasar = 0;
		double cantidadTraspasado = 0;
		
		try {
			control(false, 0, null);
			
			if (this.listTraspasos == null)
				this.listTraspasos = new ArrayList<AlmacenTraspasoExt>();
			
			if (this.listTraspasosDetalles == null)
				this.listTraspasosDetalles = new ArrayList<TraspasoDetalleExt>();
			
			// encuentro el almacen donde puede tener existencia, Almacenes ligados a la OBRA.
			params.put("tipo", "2");
			params.put("idObra", this.pojoObra.getId().toString());
			almacenesObra = this.ifzObraAlmacen.findExtByProperties(params, 0);
			if (almacenesObra == null || almacenesObra.isEmpty()) {
				control(true, 11, "La obra no tiene asignados almacenes generales");
				log.error(this.mensaje);
				return;
			}
			
			almacenObraOrigen = null;
			cantidadATraspasar = item.getCantidad();
			for (ObraAlmacenesExt var : almacenesObra) {
				AlmacenProducto prodExistente = this.ifzAlmacenProducto.findAlmacenProducto(var.getIdAlmacen().getId(), item.getIdProducto());
				if (prodExistente != null && prodExistente.getId() != null && prodExistente.getId() > 0L && prodExistente.getExistencia() > 0) {
					almacenObraOrigen = var;
					
					// Comprobamos si ya generamos un TRASPASO con similitud de almacen origen
					existeTraspaso = false;
					for (AlmacenTraspasoExt t : this.listTraspasos) {
						if (t.getAlmacenOrigen().getId().equals(almacenObraOrigen.getIdAlmacen().getId())) {
							existeTraspaso = true;
							pojoTraspaso = t;
							break;
						}
					}
					
					// genera el traspaso si corresponde
					if (! existeTraspaso) {
						// Encuentro el almacen con marca de principal en la obra si corresponde
						if (almacenObra == null) {
							params.clear();
							almacenesObra.clear();
							
							params.put("idObra", this.pojoObra.getId().toString());
							params.put("almacenPrincipal", "1");
							almacenesObra = this.ifzObraAlmacen.findExtByProperties(params, 0);
							if (almacenesObra == null || almacenesObra.isEmpty()) {
								control(true, 10, "La obra no tiene asignado un almacen principal");
								log.error(this.mensaje);
								return;
							}
							
							// Obtenemos almacen
							almacenObra = almacenesObra.get(0);
						}
						
						// Empleado que recibe los productos
						empRecibe = this.ifzEmpleados.findById(this.pojoObra.getIdResponsable());
						
						pojoTraspaso = new AlmacenTraspasoExt();
						pojoTraspaso.setId((long) (this.listTraspasos.size() + 1));
						pojoTraspaso.setAlmacenOrigen(almacenObraOrigen.getIdAlmacen());
						pojoTraspaso.setAlmacenDestino(almacenObra.getIdAlmacen());
						pojoTraspaso.setEntrega(null);
						pojoTraspaso.setRecibe(empRecibe);
						pojoTraspaso.setEstatus(0);
						pojoTraspaso.setTipo(3); // Solicitud a bodega --- pojoTraspaso.setTipo(1);
						pojoTraspaso.setCreadoPor(this.usuarioId);
						pojoTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
						pojoTraspaso.setModificadoPor(this.usuarioId);
						pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
						
						this.listTraspasos.add(pojoTraspaso);
					}

					cantidadTraspasado = cantidadATraspasar;
					if (cantidadATraspasar > prodExistente.getExistencia()) {
						cantidadATraspasar = cantidadATraspasar - prodExistente.getExistencia();
						cantidadTraspasado = prodExistente.getExistencia();
					} 
										
					// detalles de traspaso
					TraspasoDetalleExt det = new TraspasoDetalleExt();
					det.setIdAlmacenTraspaso(pojoTraspaso.getId());
					det.setIdProducto(this.ifzProds.findExtById(item.getIdProducto()));
					det.setCantidad(item.getCantidad());
					det.setCantidadRecibida(cantidadTraspasado);
					det.setEstatus(1);
					
					det.setCreadoPor(pojoTraspaso.getCreadoPor());
					det.setFechaCreacion(pojoTraspaso.getFechaCreacion());
					det.setModificadoPor(pojoTraspaso.getModificadoPor());
					det.setFechaModificacion(pojoTraspaso.getFechaModificacion());
					
					this.listTraspasosDetalles.add(det);
				}
			}
			
			if (almacenObraOrigen == null) {
				log.warn("No podemos generar un TRASPASO ya que no se encontro existensia del producto en ningun Almacen");
				return;
			}
		} catch (Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en Compras.CotizacionesAction.generaTraspaso", e);
		}
	}

	private void guardaTraspasos() {
		long idTemporal = 0;
		
		try {
			// Ciclo para guardar los traspasos correspondientes
			for (AlmacenTraspasoExt var1 : this.listTraspasos) {
				idTemporal = var1.getId();
				
				// Guardamos traspaso
				var1.setId(this.ifzTraspaso.save(var1));

				// Guardamos los detalles de traspasos
				AlmacenProducto almacenSalida = null;
				for (TraspasoDetalleExt var2 : this.listTraspasosDetalles) {
					if (! var2.getIdAlmacenTraspaso().equals(idTemporal)) continue;
					
					// Asignamos ID de traspaso
					var2.setIdAlmacenTraspaso(var1.getId());
					
					// guardamos detalle
					var2.setId(this.ifzTraspasoDetalle.save(var2));
					
					// descontamos existensia de almacen origen
					almacenSalida = this.ifzAlmacenProducto.findAlmacenProducto(var1.getAlmacenOrigen().getId(), var2.getIdProducto().getId());
					if(almacenSalida != null) {
						almacenSalida.setExistencia(almacenSalida.getExistencia() - var2.getCantidad());
						this.ifzAlmacenProducto.update(almacenSalida);
					}
				}
				
				enviaCorreoEncargadoAlmacen(var1);
			}
			
			this.listTraspasos.clear();
			this.listTraspasosDetalles.clear();
		} catch (Exception e) {
			control(true, 1, "ERROR");
    		log.error("Error en Compras.CotizacionesAction.guardaTraspasos", e);
		}
	}
	
	private boolean enviaCorreoEncargadoAlmacen(AlmacenTraspasoExt item) {
		// TODO Enviar notificacion al encargado de almacen (TRASPASO)
		return true;
	}
	
	private void dropValues() {
		try {
			if (this.pojoOrdenCompra != null && this.pojoOrdenCompra.getId() != null && this.pojoOrdenCompra.getId() > 0L) {
				this.ifzOC.delete(this.pojoOrdenCompra.getId());
				List<OrdenCompraDetalle> tmp = this.ifzOCDetalles.findByProperty("idOrdenCompra", this.pojoOrdenCompra.getId(), 0);
				if (tmp != null && !tmp.isEmpty()) {
					for (OrdenCompraDetalle var : tmp)
						this.ifzOCDetalles.delete(var.getId());
				}
				this.pojoOrdenCompra = null;
			}
			
			if (this.pojoCotizacion != null && this.pojoCotizacion.getId() != null && this.pojoCotizacion.getId() > 0L) {
				this.ifzCoti.delete(this.pojoCotizacion.getId());
				List<CotizacionDetalle> tmp = this.ifzCotiDetalles.findByProperty("idCotizacion", this.pojoCotizacion.getId(), 0);
				if (tmp != null && !tmp.isEmpty()) {
					for (CotizacionDetalle var : tmp)
						this.ifzCotiDetalles.delete(var.getId());
				}
				this.pojoCotizacion = null;
			}
			
			if (this.pojoRequisicion != null && this.pojoRequisicion.getId() != null && this.pojoRequisicion.getId() > 0L) {
				this.ifzReq.delete(this.pojoRequisicion.getId());
				List<RequisicionDetalle> tmp = this.ifzReqDetalles.findByProperty("idRequisicion", this.pojoRequisicion.getId(), 0);
				if (tmp != null && !tmp.isEmpty()) {
					for (RequisicionDetalle var : tmp)
						this.ifzReqDetalles.delete(var.getId());
				}
				this.pojoRequisicion = null;
			}
		} catch (Exception e) {
			log.error("Error en Compras.CargaOrdenComprasAction.dropValues()", e);
		}
	}
	
	private double getVal(String values, int part) {
		String[] splitted = null;
		double val = 0;
		
		splitted = values.trim().split("\\|");
		if (part < 0)
			part = 0;
		else if (part >= splitted.length)
			part = splitted.length - 1;
		
		val = Double.valueOf(splitted[part].trim());
		
		return val;
	}
	
	private void totalizarCotizacionesDetalles(List<CotizacionDetalle> detalles) {
		double subtotal	= 0;
		double iva 		= 0;
		double total	= 0;
		
		for(CotizacionDetalle var : detalles) {
			var.setImporte(var.getPrecioUnitario() * var.getCotizar());
			subtotal += var.getImporte();
			iva += ((var.getImporte() * this.porcentajeIva) / 100);
		}
		
		total = subtotal + iva;
		this.pojoCotizacion.setSubtotal(subtotal);
		this.pojoCotizacion.setTotal(total);
	}
	
	private void totalizarOrdenCompraDetalles(List<OrdenCompraDetalle> detalles) {
		double subtotal	= 0;
		double iva 		= 0;
		double total	= 0;
		
		for(OrdenCompraDetalle var : detalles) {
			var.setImporte(var.getPrecioUnitario() * var.getCantidad());
			subtotal += var.getImporte();
			iva += ((var.getImporte() * this.porcentajeIva) / 100);
		}
		
		total = subtotal + iva;
		this.pojoOrdenCompra.setSubtotal(subtotal);
		this.pojoOrdenCompra.setIva(iva);
		this.pojoOrdenCompra.setTotal(total);
	}

	// ----------------------------------------------------------------------------------------
	// BUSQUEDA ORDEN DE COMPRA
	// ----------------------------------------------------------------------------------------

	public void nuevaBusquedaOrdenCompra() {
		this.OCBusquedaCampo = this.OCBusquedaTipos.get(0).getValue().toString();
		this.OCBusquedaValor = "";
		this.OCBusquedaPagina = 1;
	}
	
	public void buscarOrdenesCompra() {
		try {
			control();
			if (this.OCBusquedaCampo == null || "".equals(this.OCBusquedaCampo.trim()))
				this.OCBusquedaCampo = this.OCBusquedaTipos.get(0).getValue().toString();
			
			this.listOrdenesCompra = this.ifzOC.findNoCompletas(this.OCBusquedaCampo, this.OCBusquedaValor, 0);
			if (this.listOrdenesCompra == null || this.listOrdenesCompra.isEmpty()) {
				log.info("ERROR 2 - Busqueda sin resultados");
				control(true, 2, "ERROR");
			}
		} catch (Exception e) {
    		log.error("Error en buscar Ordenes de Compra", e);
			control(true, 1, "Error al buscar");
    	} finally {
			this.OCBusquedaPagina = 1;
    	}
	}
	
	public void seleccionarOrdenCompra() {
		if (this.pojoOrdenCompraReemplazar == null || this.pojoOrdenCompraReemplazar.getId() == null || this.pojoOrdenCompraReemplazar.getId() <= 0L)
			return;
		
		if (this.pojoOrdenCompraReemplazar.getId().longValue() != this.pojoOrdenCompraReemplazarBK.getId().longValue())
			this.pojoOrdenCompraReemplazarBK = this.pojoOrdenCompraReemplazar;
	}
	
	// ----------------------------------------------------------------------------------------
	// REEMPLAZAR ORDEN DE COMPRA
	// ----------------------------------------------------------------------------------------

	public void nuevoReemplazoOrdenCompra() {
		if (! this.reemplazarOrdenCompra) {
			this.pojoOrdenCompraReemplazarBK = null;
			this.pojoOrdenCompraReemplazar = null;
			return;
		}
		
		if (this.pojoOrdenCompraReemplazar != null) {
			this.pojoOrdenCompraReemplazarBK = this.pojoOrdenCompraReemplazar;
			this.pojoOrdenCompraReemplazar = null;
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------------
	
	public String getOrdenCompraReemplazar() {
		if (this.pojoOrdenCompraReemplazar != null && this.pojoOrdenCompraReemplazar.getId() != null && this.pojoOrdenCompraReemplazar.getId() > 0L)
			return this.pojoOrdenCompraReemplazar.getId() + " - " + this.pojoOrdenCompraReemplazar.getFolio();
		return "";
	}
	
	public void setOrdenCompraReemplazar(String value) {}

	public boolean getProcesando() {
		return procesando;
	}

	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}

	public String getBitacora() {
		String val = "";
		
		if (this.strBitacora == null || this.strBitacora.isEmpty())
			return "";
		
		for (String s : this.strBitacora) {
			val += s + "\n";
		}
			
		return val;
	}
	
	public void setBitacora(String bitacora) { }

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
	
	public boolean isDebugging() { 
		if (this.paramsRequest.containsKey("DEBUG")) 
			return true;
		return false;
	}
	
	public void setDebugging(boolean isDebug) { }

	public int getMaxCargas() {
		return this.maxCargas;
	}

	public void setMaxCargas(int maxCargas) {
		this.maxCargas = maxCargas;
	}
	


	public List<OrdenCompra> getListOrdenesCompra() {
		return listOrdenesCompra;
	}
	


	public void setListOrdenesCompra(List<OrdenCompra> listOrdenesCompra) {
		this.listOrdenesCompra = listOrdenesCompra;
	}
	


	public List<SelectItem> getOCBusquedaTipos() {
		return OCBusquedaTipos;
	}
	


	public void setOCBusquedaTipos(List<SelectItem> oCBusquedaTipos) {
		OCBusquedaTipos = oCBusquedaTipos;
	}
	


	public String getOCBusquedaCampo() {
		return OCBusquedaCampo;
	}
	


	public void setOCBusquedaCampo(String oCBusquedaCampo) {
		OCBusquedaCampo = oCBusquedaCampo;
	}
	


	public String getOCBusquedaValor() {
		return OCBusquedaValor;
	}
	


	public void setOCBusquedaValor(String oCBusquedaValor) {
		OCBusquedaValor = oCBusquedaValor;
	}
	


	public int getOCBusquedaPagina() {
		return OCBusquedaPagina;
	}
	


	public void setOCBusquedaPagina(int oCBusquedaPagina) {
		OCBusquedaPagina = oCBusquedaPagina;
	}
	


	public boolean isReemplazarOrdenCompra() {
		return reemplazarOrdenCompra;
	}
	


	public void setReemplazarOrdenCompra(boolean reemplazarOrdenCompra) {
		this.reemplazarOrdenCompra = reemplazarOrdenCompra;
	}
	


	public OrdenCompra getPojoOrdenCompraReemplazar() {
		return pojoOrdenCompraReemplazar;
	}
	


	public void setPojoOrdenCompraReemplazar(OrdenCompra pojoOrdenCompraReemplazar) {
		this.pojoOrdenCompraReemplazar = pojoOrdenCompraReemplazar;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	1.2	| 09/01/2017 | Javier Tirado 			 | Creacion de CargaOrdenComprasAction
 *  2.2 | 29/04/2017 | Javier Tirado 			 | Añado validacion para interpretar M.N. como PESOS en cueStion de MONEDA. Corrijo el metodo getVal
 */