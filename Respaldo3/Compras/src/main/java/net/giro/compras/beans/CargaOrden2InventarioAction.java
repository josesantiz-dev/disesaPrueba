package net.giro.compras.beans;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import org.richfaces.event.FileUploadEvent; // <---

import com.google.gson.Gson;

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
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.TraspasoDetalle;
import net.giro.inventarios.comun.TipoMaestro;
import net.giro.inventarios.comun.TipoMovimientoInventario;
import net.giro.inventarios.comun.TipoMovimientoReferencia;
import net.giro.inventarios.comun.TipoMovimientoReferenciaExtendida;
import net.giro.inventarios.comun.TipoTraspaso;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosInventarios;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.logica.EmpleadoRem;
import net.giro.tyg.admon.Moneda;

@ViewScoped
@ManagedBean(name="carga2Action")
public class CargaOrden2InventarioAction implements Runnable, Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CargaOrden2InventarioAction.class);
	private PropertyResourceBundle cargaCaso1;
	private PropertyResourceBundle cargaCaso2;
	private PropertyResourceBundle cargaCaso3;
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
	// Traspasos
	private AlmacenTraspasoRem ifzTraspaso;
	private TraspasoDetalleRem ifzTraspasoDetalle;	
	// Facturas
	HashMap<String, HashMap<Long, Double>> mapFacturasProductos;
	private ObraAlmacenesRem ifzObraAlmacen;
	private AlmacenMovimientosRem ifzMovimientos;
	private MovimientosDetalleRem ifzMovimientosDetalle;
	// Variables de operacion
	private String fileName;
	private byte[] fileSrc; 
	private List<String> libroLog;
	private LinkedHashMap<String, byte[]> listOC;
	private LinkedHashMap<String, String> listErrores;
	private long usuarioId = 0;
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
	// Tipos de Carga
	private List<SelectItem> tiposCarga;
	private int tipoCarga;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;

	
	public CargaOrden2InventarioAction() {
		Map<String, String> params = null;
		ValueExpression ve = null;
		FacesContext fc = null;
		Application app = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			params = fc.getExternalContext().getRequestParameterMap();
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : params.entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());			
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{carga1}", PropertyResourceBundle.class);
			this.cargaCaso1 = (PropertyResourceBundle) ve.getValue(fc.getELContext());

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{carga2}", PropertyResourceBundle.class);
			this.cargaCaso2 = (PropertyResourceBundle) ve.getValue(fc.getELContext());

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{carga}", PropertyResourceBundle.class);
			this.cargaCaso3 = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
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
			this.ifzTraspaso 		= (AlmacenTraspasoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
			this.ifzTraspasoDetalle = (TraspasoDetalleRem) this.ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			this.ifzMovimientos = (AlmacenMovimientosRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzMovimientosDetalle = (MovimientosDetalleRem) this.ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");

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
			this.ifzTraspaso.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzTraspasoDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.ifzReq.showSystemOuts(false);
			this.ifzCoti.showSystemOuts(false);
			this.ifzOC.showSystemOuts(false);
			this.ifzReqDetalles.showSystemOuts(false);
			this.ifzCotiDetalles.showSystemOuts(false);
			this.ifzOCDetalles.showSystemOuts(false);
			this.ifzObras.showSystemOuts(false);
			
			this.tiposCarga = new ArrayList<>();
			this.tiposCarga.add(new SelectItem(1, "Las Ordenes de Compra no existen"));
			this.tiposCarga.add(new SelectItem(3, "Las Ordenes de Compra no existen, Inventario al 100%"));
			this.tiposCarga.add(new SelectItem(2, "Las Ordenes de Compra existen"));
			this.tipoCarga = (int) this.tiposCarga.get(0).getValue();
			
			/*this.maxCargas = 1;
			if (this.isDebug && this.paramsRequest.containsKey("MULTI_CARGA"))*/
				this.maxCargas = 0;
		} catch (Exception e) {
			log.error("Error en constructor CargaOrdenComprasAction", e);
			this.ctx = null;
		}
	}
	
	
	public void nuevo() {
		try {
			control();
			this.fileName = "";
			this.fileSrc = null;
			this.strBitacora = null;
		} catch (Exception e) {
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.nuevo", e);
			control(true, -1, "Ocurrio un problema al preparar");
		}
	}
	
	public void uploadListener(FileUploadEvent event) {
		try {
			control();
			if (this.strBitacora != null)
				this.strBitacora.clear();
			bitacora();
			
			this.fileName = event.getUploadedFile().getName();
			this.fileSrc = event.getUploadedFile().getData();
			
			if (this.listOC == null)
				this.listOC = new LinkedHashMap<String, byte[]>();
			this.listOC.put(this.fileName, this.fileSrc);
		} catch (Exception e) {
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.uploadListener", e);
			control(true, -1, "Ocurrio un problema al cargar el Libro indicado");
		}
	}
	
	/**
	 * Lanza un hilo independiente con el procesamiento del archivo para desocupar al hilo principal
	 */
	public void procesar() {
		PropertyResourceBundle properties = null;
		
		try {
			control();
			bitacora();
			if (this.listErrores == null)
				this.listErrores = new LinkedHashMap<String, String>();
			this.listErrores.clear();
			
			if (this.listOC == null || this.listOC.isEmpty()) {
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
				
				if (this.tipoCarga == 1) {
					properties = this.cargaCaso1;
				} else if (this.tipoCarga == 2) {
					properties = this.cargaCaso2;
				} else if (this.tipoCarga == 3) {
					properties = this.cargaCaso3;
				} else {
					control(true, -1, "No se pudo cargar el Layout para procesar el Libro");
					bitacora("Preparando... ERROR", true); 
					bitacora("Carga fallida...", false);  
					return;
				}

	            Set<String> keys = properties.keySet();
	            for(String key : keys)
	            	this.cellReference.put(key.toUpperCase().trim(), properties.getString(key).toUpperCase().trim());
				bitacora("Preparando... OK", true); 
			} catch (Exception e) {
				log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.procesar :(", e);
				control(true, -1, "Ocurrio un problema con los preparativos de carga");
				bitacora(this.fileName + " Preparando... ERROR", true); 
				bitacora("Carga fallida...", false);  
				return;
			}
			
			// Lanzamos hilo con el procesamiento del archivo para desocupar el hilo principal y usarlo para reportar el avance
			this.procesando = true;
			new Thread(this).start();
			log.info("proceso iniciado");
		} catch (Exception e) {  
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.procesar", e);
			control(true, -1, "Ocurrio un problema con los preparativos para procesar el Libro indicado");
		}
	}

	@Override
	public void run() {
		LinkedHashMap<String, String> listErroresMain = new LinkedHashMap<String, String>();
		
		try {
			control();
			if (this.libroLog == null)
				this.libroLog = new ArrayList<String>();
			
			for (Entry<String, byte[]> entry : this.listOC.entrySet()) {
				this.fileName = entry.getKey();
				this.fileSrc = entry.getValue();

				bitacora(" ", false);
				bitacora("-----------------------------------------------", false);
				bitacora("Procesando libro " + this.fileName + " ...", false); 
				bitacora("-----------------------------------------------", false);
				this.doProcesar();
				bitacora("Proceso terminado... 	", false); 
				listErroresMain.putAll(this.listErrores);
				
				this.fileName = ""; //(multiCarga) ? "" : this.fileName;
				this.fileSrc = null;//(multiCarga) ? null : this.fileSrc;
				this.listErrores.clear();
			}
			
			log.info("---------------------------------------------------------");
			log.warn(listErroresMain.toString());
			log.info("---------------------------------------------------------");
			this.listOC.clear();
			listErroresMain.clear();
		} catch (Exception e) {
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.run", e);
			control(true, -1, "Ocurrio un problema al procesar el Libro indicado");
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
		HashMap<String, Respuesta> listRespuestas = null; 
		Respuesta result = null;
		boolean libroOk = true;
	
		try {
			control();
			try {
				bitacora("Validando archivo... ", false); 
				if (this.fileSrc == null || this.fileSrc.length <= 0) {
					control(true, -1, "Ocurrio un problema al procesar el archivo " + this.fileName);
					bitacora("Comprobando archivo... ERROR", true); 
					bitacora("Carga fallida...", false);  
					return;
				}
				bitacora("Validando archivo... OK", true); 
			} catch (Exception e) {
				control(true, -1, "Ocurrio un problema al procesar el archivo " + this.fileName);
				bitacora("Comprobando archivo... ERROR", true); 
				bitacora("Carga fallida...", false);  
				bitacora(this.mensaje, false);
				return;
			}
			
			// procesamos archivo
			// ----------------------------------------------------------------------------------
			bitacora("Leyendo archivo... ", false);
			result = this.ifzOC.procesarArchivo(this.fileSrc, this.cellReference);
			if (result.getErrores().getCodigoError() != 0L) {
				log.error("Error al realizar PROCESO DE LECTURA DE ARCHIVO");
				control(true, -1, this.fileName + ": " + result.getErrores().getDescError());
				bitacora("Leyendo archivo... ERROR", true); 
				bitacora("Carga fallida...", false);
				bitacora(this.mensaje, false);
				this.listErrores.put(this.fileName, this.mensaje); 
				this.fileSrc = null;
				return;
			}
			bitacora("Leyendo archivo... OK", true); 

			listRespuestas = (HashMap<String, Respuesta>) result.getBody().getValor("respuestas");
			if (listRespuestas == null || listRespuestas.isEmpty()) {
				listRespuestas = new LinkedHashMap<String, Respuesta>();
				listRespuestas.put("Hoja1", result);
			}
			
			libroOk = true;
			for (Entry<String, Respuesta> item : listRespuestas.entrySet()) {
				bitacora("-----------------------------------------------", false);
				bitacora("Procesando Hoja " + item.getKey() + " ...", false); 
				if (! procesaResultado(item.getValue(), item.getKey()))
					libroOk = false;
				libroLog2Bitacora();
			}

			if (libroOk)
				moveFile(this.fileName, "ok");
			this.fileSrc = null;
		} catch (Exception e) {
			log.error("Ocurrio un error en Compras.CargaOrdenComprasAction.doProcesar", e);
			control(true, -1, "Ocurrio un problema al intentar procesar el Libro " + this.fileName);
			bitacora("Carga fallida...", false);
			bitacora(this.mensaje, false);
			this.listErrores.put(this.fileName, e.getMessage()); 
		} 
	}
	
	@SuppressWarnings("unchecked")
	private boolean procesaResultado(Respuesta result, String sheetName) throws Exception {
		HashMap<String, HashMap<String, String>> sinProcesar = null;
		List<OrdenCompra> listAux = null;
		List<OrdenCompraDetalle> ocDetalles = null;
		HashMap<Long, Double> aux = null;
		
		// Validamos datos devueltos
		// ----------------------------------------------------------------------------------
		bitacora("Comprobando estructura...", false); 
		if (! validarRespuesta((HashMap<String, Object>) result.getBody().getValor("encabezados"))) {
			bitacora("Comprobando estructura... ERROR", true); 
			bitacora("Carga fallida...", false); 
			return false;
		}
		bitacora("Comprobando estructura... OK", true); 
		
		if ((this.tipoCarga == 1 || this.tipoCarga == 3) && this.pojoOrdenCompra != null && this.pojoOrdenCompra.getId() != null && this.pojoOrdenCompra.getId() > 0L) {
			bitacora("Comprobando estructura... OK", true); 
			bitacora("Carga previa... Orden Compra ---> " + this.pojoOrdenCompra.getId(), false); 
			bitacora("Carga abortada... OK", false); 

			aux = new HashMap<Long, Double>();
			ocDetalles = this.ifzOCDetalles.findAll(this.pojoOrdenCompra.getId());
			for (OrdenCompraDetalle var : ocDetalles)
				aux.put(var.getIdProducto(), var.getCantidad());
			
			this.mapFacturasProductos = new HashMap<String, HashMap<Long,Double>>();
			this.mapFacturasProductos.put("F-SYS-" + this.pojoOrdenCompra.getFolio(), aux);
			
			// Proceso INVENTARIO
			// ----------------------------------------------------------------------------------
			// Compruebo ENTRADA
			if (comprobarEntrada(this.pojoOrdenCompra.getId()))  {
				if (this.mapFacturasProductos != null && ! this.mapFacturasProductos.isEmpty())
					procesoInventario();
			}
			return true;
		}
		
		// Recuperamos listas y validamos
		bitacora("Comprobando Productos...", false); 
		sinProcesar = (HashMap<String, HashMap<String, String>>) result.getBody().getValor("sin_procesar");
		if (this.tipoCarga != 2 && sinProcesar != null && ! sinProcesar.isEmpty()) {
			log.error("Error al comprobar la respuesta. validando productos. Productos con informacion faltante");
			control(true, -1, "Hay Productos con informacion faltante. " + sinProcesar);
			this.libroLog.add(this.mensaje);
			this.listErrores.put(this.fileName, this.mensaje); 
			bitacora("Comprobando Productos... ERROR", true); 
			bitacora("Carga fallida...", false);
			return false;
		}

		// Generamos lista de productos
		// ----------------------------------------------------------------------------------
		if (! validarProductos((HashMap<String, String>) result.getBody().getValor("productos"))) {
			bitacora("Comprobando Productos... ERROR", true); 
			bitacora("Carga fallida...", false);
			return false;
		}
		bitacora("Comprobando Productos... OK", true); 
		
		// Validamos que no exista la OC
		listAux = this.ifzOC.findByProperty("folio", (this.fileName.replace(".xls", "").replace(".xlsx", "")), 0);
		if (listAux != null && ! listAux.isEmpty()) {
			log.warn("Esta OC ya fue cargada previamente");
		}
		
		if (this.tipoCarga != 2) {
			// Generamos REQUISICION
			// ----------------------------------------------------------------------------------
			try {
				bitacora("Generando Requisicion... ", false); 
				this.pojoRequisicion = new Requisicion();
				this.pojoRequisicion.setIdObra(this.pojoObra.getId());
				this.pojoRequisicion.setNombreObra(this.pojoObra.getNombre());
				this.pojoRequisicion.setIdSolicita(this.pojoSolicita.getId());
				this.pojoRequisicion.setNombreSolicita(this.pojoSolicita.getNombre());
				this.pojoRequisicion.setIdMoneda(this.idMoneda);
				this.pojoRequisicion.setCreadoPor(this.usuarioId);
				this.pojoRequisicion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoRequisicion.setTipo(TipoMaestro.Inventario.ordinal());
				this.pojoRequisicion.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
				this.pojoRequisicion.setSistema(1);
				
				// Detalles REQUISICION
				List<RequisicionDetalle> reqDetalles = new ArrayList<RequisicionDetalle>();
				RequisicionDetalle detR = null;
				for (Producto p : this.listProductos) {
					detR = new RequisicionDetalle();
					detR.setIdRequisicion(this.idParent);
					detR.setIdProducto(p.getId());
					detR.setCantidad(p.getExistencia());
					detR.setCreadoPor(this.usuarioId);
					detR.setFechaCreacion(Calendar.getInstance().getTime());
					detR.setModificadoPor(this.usuarioId);
					detR.setFechaModificacion(Calendar.getInstance().getTime());
					reqDetalles.add(detR);
				}
				
				// Guardamos REQUISICION
				this.ifzReq.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzReqDetalles.setInfoSesion(this.loginManager.getInfoSesion());
				this.idParent = this.ifzReq.save(this.pojoRequisicion);
				this.pojoRequisicion.setId(this.idParent);
				for (RequisicionDetalle var : reqDetalles) {
					var.setIdRequisicion(this.idParent);
					var.setId(this.ifzReqDetalles.save(var));
				}
				
				this.idParent = 0L;
				bitacora("Generando Requisicion... OK ---> " + this.pojoRequisicion.getId(), true); 
			} catch (Exception e) {
				log.error("Ocurrio un problema al intentar generar la REQUISICION", e);
				control(true, -1, "Ocurrio un problema al intentar generar la REQUISICION");
				bitacora("Generando Requisicion... ERROR", true); 
				bitacora("Carga fallida...", false);
				this.libroLog.add(this.mensaje);
				this.listErrores.put(this.fileName, e.getMessage()); 
				dropValues();
				return false;
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
				this.pojoCotizacion.setIdMoneda(this.idMoneda);
				this.pojoCotizacion.setCreadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoCotizacion.setModificadoPor(this.usuarioId);
				this.pojoCotizacion.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoCotizacion.setTipo(TipoMaestro.Inventario.ordinal());
				this.pojoCotizacion.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
				this.pojoCotizacion.setSistema(1);
								
				// Detalles COTIZACION
				List<CotizacionDetalle> cotiDetalles = new ArrayList<CotizacionDetalle>();
				CotizacionDetalle detC = null;
				for (Producto p : this.listProductos) {
					detC = new CotizacionDetalle();
					detC.setIdProducto(p.getId());
					detC.setCantidad(p.getExistencia());
					detC.setCantidadInicial(p.getExistencia());
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
				this.ifzCoti.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzCotiDetalles.setInfoSesion(this.loginManager.getInfoSesion());
				this.idParent = this.ifzCoti.save(this.pojoCotizacion);
				this.pojoCotizacion.setId(this.idParent);
				
				for (CotizacionDetalle var : cotiDetalles) {
					var.setIdCotizacion(this.idParent);
					var.setId(this.ifzCotiDetalles.save(var));
				}
				
				this.idParent = 0L;
				cotiDetalles.clear();
				bitacora("Generando Cotizacion... OK ---> " + this.pojoCotizacion.getId(), true); 
			} catch (Exception e) {
				log.error("Ocurrio un problema al intentar generar la COTIZACION", e);
				control(true, -1, "Ocurrio un problema al intentar generar la COTIZACION");
				bitacora("Generando Cotizacion... ERROR", true); 
				bitacora("Carga fallida...", false);
				this.libroLog.add(this.mensaje);
				this.listErrores.put(this.fileName, e.getMessage()); 
				dropValues();
				return false;
			} /*finally { // DESHABILITADO: Ya no se hace conteo de cotizados
				this.ifzCoti.backOfficeRequisicion(this.pojoCotizacion.getId());
			}*/
			
			// Generamos ORDEN DE COMPRA
			// ----------------------------------------------------------------------------------
			try {
				bitacora("Generando Orden de Compra... ", false); 
				this.pojoOrdenCompra = new OrdenCompra();
				this.pojoOrdenCompra.setFolio(this.folioOC);
				this.pojoOrdenCompra.setAnticipo(BigDecimal.ZERO);
				this.pojoOrdenCompra.setIdCotizacion(this.pojoCotizacion);
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
				this.pojoOrdenCompra.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
				this.pojoOrdenCompra.setSistema(1);
	
				// Detalles ORDEN DE COMPRA
				ocDetalles = new ArrayList<OrdenCompraDetalle>();
				OrdenCompraDetalle detO = null;
				for (Producto p : this.listProductos) {
					detO = new OrdenCompraDetalle();
					detO.setIdProducto(p.getId());
					detO.setCantidad(p.getExistencia());
					//detO.setCantidadRecibida(p.getExistencia());
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
				this.ifzOC.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzOCDetalles.setInfoSesion(this.loginManager.getInfoSesion());
				this.idParent = this.ifzOC.save(this.pojoOrdenCompra);
				this.pojoOrdenCompra.setId(this.idParent);
				
				for (OrdenCompraDetalle var : ocDetalles) {
					var.setIdOrdenCompra(this.idParent);
					this.ifzOCDetalles.save(var);
				}
				
				if (this.tipoCarga == 3) {
					aux = new HashMap<Long, Double>();
					for (OrdenCompraDetalle var : ocDetalles)
						aux.put(var.getIdProducto(), var.getCantidad());
					
					this.mapFacturasProductos = new HashMap<String, HashMap<Long,Double>>();
					this.mapFacturasProductos.put("F-SYS-" + this.pojoOrdenCompra.getFolio(), aux);
				}
				
				this.idParent = 0L;
				ocDetalles.clear();
				bitacora("Generando Orden de Compra... OK ---> " + this.pojoOrdenCompra.getId(), true); 
				this.listErrores.put(this.fileName, "OK"); 
			} catch (Exception e) {
				log.error("Ocurrio un problema al intentar generar la ORDEN DE COMPRA", e);
				control(true, -1, "Ocurrio un problema al intentar generar la ORDEN DE COMPRA");
				bitacora("Generando Orden de Compra... ERROR", true); 
				this.libroLog.add(this.mensaje);
				this.listErrores.put(this.fileName, e.getMessage()); 
				dropValues();
				return false;
			} finally {
				this.ifzOC.backOfficeOrdenCompra(this.pojoOrdenCompra.getId());//backOfficeCotizacion(this.pojoOrdenCompra.getId());
			}
		}
		
		// Proceso INVENTARIO
		// ----------------------------------------------------------------------------------
		if (this.mapFacturasProductos != null && ! this.mapFacturasProductos.isEmpty())
			procesoInventario();

		control(false, 0, "\nSe generó la Orden de Compra " + this.pojoOrdenCompra.getId());
		this.pojoRequisicion = null;
		this.pojoCotizacion = null;
		this.pojoOrdenCompra = null;
		return true;
	}
	
	private boolean validarRespuesta(HashMap<String, Object> values) {
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
		List<OrdenCompra> listOrdenes = null;
		List<PersonaExt> list = null;
		String idPro = "";
		String annio = "";
		String valStr = "";

		try {
			// La Orden de Compra ya existe
			if (this.tipoCarga == 2) {
				valStr = (values.containsKey("OC") ? values.get("OC").toString() : "0"); 
				if ("".equals(valStr)) {
					control(true, -1, "OC: Formato invalido");
					this.libroLog.add(this.mensaje);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				
				this.pojoOrdenCompra = this.ifzOC.findByCodigo(valStr);
				if (this.pojoOrdenCompra == null) {
					control(true, -1, "Orden de Compra no existe");
					this.libroLog.add(this.mensaje);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				
				this.folioOC = valStr;
				this.idMoneda = this.pojoOrdenCompra.getIdMoneda();
				this.tipoCambio = this.pojoOrdenCompra.getTipoCambio();
				this.pojoObra = this.ifzObras.findById(this.pojoOrdenCompra.getIdObra());
			} else {
				// Comprobamos si existe la ORDEN DE COMPRA
				valStr = (values.containsKey("OC") ? values.get("OC").toString() : "0"); 
				if (valStr != null && ! "".equals(valStr)) {
					listOrdenes = this.ifzOC.findByProperty("folio", valStr, 0);
					if (listOrdenes != null && ! listOrdenes.isEmpty()) {
						Collections.sort(listOrdenes, new Comparator<OrdenCompra>() {
							@Override
							public int compare(OrdenCompra o1, OrdenCompra o2) {
								return o2.getId().compareTo(o1.getId());
							}
						});
						
						this.pojoOrdenCompra = listOrdenes.get(0);
						this.folioOC = this.pojoOrdenCompra.getFolio();
						this.idMoneda = this.pojoOrdenCompra.getIdMoneda();
						this.tipoCambio = this.pojoOrdenCompra.getTipoCambio();
						this.pojoObra = this.ifzObras.findById(this.pojoOrdenCompra.getIdObra());
						return true;
					}
				}
				
				// OBRA
				valStr = values.containsKey("OBRA") ? values.get("OBRA").toString() : "0";
				if (! validateNumber(values.get("OBRA"))) {
					control(true, -1, "ID Obra: Formato invalido");
					this.libroLog.add(this.mensaje);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				
				this.pojoObra = this.ifzObras.findById(getValidLong(valStr));
				if (this.pojoObra == null) {
					control(true, -1, "Obra no encontrada");
					this.libroLog.add(this.mensaje);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				
				// SOLICITA
				valStr = values.containsKey("SOLICITA") ? values.get("SOLICITA").toString() : "0";
				if (! validateNumber(values.get("SOLICITA"))) {
					control(true, -1, "ID Solicita: Formato invalido");
					this.libroLog.add(this.mensaje);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				
				this.pojoSolicita = this.ifzEmpleados.findById(getValidLong(valStr));
				if (this.pojoSolicita == null) {
					control(true, -1, "Solicita no encontrado");
					this.libroLog.add(this.mensaje);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				
				// COMPRADOR
				valStr = values.containsKey("COMPRADOR") ? values.get("COMPRADOR").toString() : "0";
				if (! validateNumber(values.get("COMPRADOR"))) {
					control(true, -1, "ID COMPRADOR: Formato invalido");
					this.libroLog.add(this.mensaje);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				
				this.pojoComprador = this.ifzEmpleados.findById(getValidLong(valStr));
				if (this.pojoComprador == null) {
					control(true, -1, "Comprador no existe");
					this.libroLog.add(this.mensaje);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				
				// PROVEEDOR
				valStr = (values.containsKey("PROVEEDOR")) ? values.get("PROVEEDOR").toString().trim() : ""; 
				if (! "".equals(valStr.trim())) {
					list = new ArrayList<PersonaExt>(); 
					if (valStr.contains("-"))
						valStr = valStr.replace("-", "");
					
					if (valStr.trim().length() == 13) {
						this.tipoPersonaProveedor = "P";
					} else if (valStr.trim().length() == 12) {
						this.tipoPersonaProveedor = "N";
					} else {
						control(true, -1, "El RFC del proveedor no es valido");
						this.libroLog.add(this.mensaje + ": " + valStr);
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
								this.libroLog.add(this.mensaje + ": " + valStr);
								this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
								return false;
							}
						} else {					
							control(true, -1, "Proveedor no existe");
							this.libroLog.add(this.mensaje + ": " + valStr);
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
				valStr = values.containsKey("ANTICIPO") ? values.get("ANTICIPO").toString() : "0";
				if (! validateNumber(values.get("ANTICIPO"))) {
					control(true, -1, "El anticipo no tiene el formato correcto o no fue asignado. Tipo Númerico");
					this.libroLog.add(this.mensaje + ": " + valStr);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				this.anticipo = (new BigDecimal(getValidDouble(valStr)));
				
				this.idMoneda = 0L;
				valStr = (values.containsKey("MONEDA")) ? values.get("MONEDA").toString() : "";
				this.idMoneda = getMoneda(valStr); 
				if (this.idMoneda <= 0L) {
					control(true, -1, "La moneda asignada no es valida.");
					this.libroLog.add(this.mensaje + ": " + valStr);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				
				this.tipoCambio = BigDecimal.ZERO;
				if (this.idMoneda == 10000001)
					values.put("TIPO_CAMBIO", 0D);
				valStr = values.containsKey("TIPO_CAMBIO") ? values.get("TIPO_CAMBIO").toString() : "0";
				if (! validateNumber(values.get("TIPO_CAMBIO"))) {
					control(true, -1, "El tipo de cambio no tiene el formato correcto o no fue asignado. Tipo Númerico");
					this.libroLog.add(this.mensaje + ": " + valStr);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				this.tipoCambio = (new BigDecimal(getValidDouble(valStr)));
				
				this.flete = 0;
				valStr = values.containsKey("FLETE") ? values.get("FLETE").toString() : "0";
				if (! validateNumber(values.get("FLETE"))) {
					control(true, -1, "El flete no tiene el formato correcto o no fue asignado. Tipo Númerico");
					this.libroLog.add(this.mensaje + ": " + valStr);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				this.flete = getValidDouble(valStr);
				
				this.plazo = 0;
				valStr = values.containsKey("PLAZO") ? values.get("PLAZO").toString() : "0";
				if (! validateNumber(values.get("PLAZO"))) {
					control(true, -1, "El plazo no tiene el formato correcto o no fue asignado. Tipo Númerico");
					this.libroLog.add(this.mensaje + ": " + valStr);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				this.plazo = ((Double) getValidDouble(valStr)).intValue();
				
				this.tiempoEntrega = 0;
				valStr = values.containsKey("TIEMPO_ENTREGA") ? values.get("TIEMPO_ENTREGA").toString() : "0";
				if (! validateNumber(values.get("TIEMPO_ENTREGA"))) {
					control(true, -1, "El tiempo de entrega no tiene el formato correcto o no fue asignado. Tipo Númerico");
					this.libroLog.add(this.mensaje + ": " + valStr);
					this.listErrores.put(this.fileName, this.mensaje + ": " + valStr); 
					return false;
				}
				this.tiempoEntrega = ((Double) getValidDouble(valStr)).intValue();
				
				this.lugarEntrega = (values.containsKey("LUGAR_ENTREGA")) ? values.get("LUGAR_ENTREGA").toString() : "";
			}
		} catch (Exception e) {
			log.error("Error al comprobar la respuesta. validando encabezados", e);
			control(true, -1, "Error al comprobar la respuesta. validando encabezados");
			this.libroLog.add(this.mensaje + ": " + e.getMessage());
			this.listErrores.put(this.fileName, e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private boolean validarProductos(HashMap<String, String> values) {
		Producto p = null;
		
		try {
			control();
			if (this.listProductos == null)
				this.listProductos = new ArrayList<Producto>();
			this.listProductos.clear();
			
			if (values == null || values.isEmpty()) {
				control(true, -1, "La productos no tienen codigo");
				this.libroLog.add(this.mensaje);
				throw new Exception("La lista no contiene productos");
			}

			for (Map.Entry<String, String> entry : values.entrySet()) {
				p = this.ifzProds.findByClave(entry.getKey());
				if (p == null) {
					log.error("Error al comprobar la respuesta. validando productos. No se encontro el producto " + entry.getKey());
					control(true, -1, "No se encontro ningun producto con clave " + entry.getKey());
					this.libroLog.add(this.mensaje);
					this.listErrores.put(this.fileName, this.mensaje); 
					return false;
				}
				
				// Asigno las facturas en la que involucra al producto, si corresponde
				getFacturas(p.getId(), entry.getValue());
				p.setExistencia(getVal(entry.getValue(), 0));
				p.setPrecioUnitario(getVal(entry.getValue(), 1));
				this.listProductos.add(p);
			}
		} catch (Exception e) {
			log.error("Error al comprobar la respuesta. validando productos.", e);
			control(true, -1, "Ocurrio un problema al validar los Productos de la Orden de Compra. " + this.mensaje);
			this.libroLog.add(this.mensaje);
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
				// Busqueda por nombre
				for (Moneda var : listaMonedas) {
					if (value.toUpperCase().equals(var.getNombre().toUpperCase()))
						return var.getId();
				}
				
				// Busqueda por sinonimo
				for (Moneda var : listaMonedas) {
					if (var.getSinonimos().contains(value))
						return var.getId();
				}
			}
		} catch (Exception e) {
			log.error("Error en Compras.OrdenesComprasAction.getMoneda('" + value + "')", e);
		}
		
		return 0L;
	}

	private boolean validateNumber(Object obj) {
		NumberFormat formatter = new DecimalFormat("#");
		String value = "";
		boolean resultado = false;
		
		try {
			switch (obj.getClass().getName()) {
				case "java.lang.Long":
					value = obj.toString();
					if (value.contains("E"))
						value = formatter.format(Long.valueOf(value));
					Long.valueOf(value);
					resultado = true;
					break;
					
				case "java.lang.Double":
					value = obj.toString();
					if (value.contains("E"))
						value = formatter.format(Double.valueOf(value));
					Double.valueOf(value);
					resultado = true;
					break;
					
				case "java.math.BigDecimal":
					value = obj.toString();
					if (value.contains("E"))
						value = formatter.format(Double.valueOf(value));
					new BigDecimal(value);
					resultado = true;
					break;
				
				case "java.lang.String":
					value = obj.toString();
					if (value.contains("E"))
						value = formatter.format(Double.valueOf(value));
					Double.valueOf(value);
					resultado = true;
					break;
					
				default :
					resultado = false;
					break;
			} 
		} catch (Exception e) {
			resultado = false;
		}
		
		return resultado;
	}

	private long getValidLong(String value) {
		NumberFormat formatter = new DecimalFormat("#");
		
		try {
			if (value.contains("E")) {
				value = formatter.format(Double.valueOf(value));
				return Long.valueOf(value);
			} else {
				return Long.valueOf(value);
			}
		} catch (Exception e) {
			return 0L;
		}
	}

	private double getValidDouble(String value) {
		NumberFormat formatter = new DecimalFormat("#");
		
		try {
			if (value.contains("E")) {
				value = formatter.format(Double.valueOf(value));
				return Double.valueOf(value);
			} else {
				return Double.valueOf(value);
			}
		} catch (Exception e) {
			return 0D;
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
	
	@SuppressWarnings("unchecked")
	private void getFacturas(Long idProducto, String values) {
		HashMap<String, Double> facturas;
		HashMap<Long, Double> aux;
		String[] splitted = null;
		Gson gson = new Gson(); 
		String json = "";
		
		try {
			if (this.mapFacturasProductos == null)
				this.mapFacturasProductos = new HashMap<String, HashMap<Long, Double>>();
			
			splitted = values.trim().split("\\|");
			if (splitted.length <= 2)
				return;
			json = splitted[splitted.length - 1].trim();
			facturas = gson.fromJson(json, HashMap.class);
			
			for (Entry<String, Double> item : facturas.entrySet()) {
				aux = new HashMap<Long, Double>();
				if (this.mapFacturasProductos.containsKey(item.getKey())) {
					aux = this.mapFacturasProductos.get(item.getKey());
					if (aux.containsKey(idProducto))
						continue;
				} 
				
				aux.put(idProducto, item.getValue());
				this.mapFacturasProductos.put(item.getKey(), aux);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar recuperar las Facturas del Producto " + idProducto, e);
		}
	}

	private void totalizarCotizacionesDetalles(List<CotizacionDetalle> detalles) {
		double subtotal	= 0;
		double iva 		= 0;
		double total	= 0;
		
		for(CotizacionDetalle var : detalles) {
			var.setImporte(var.getPrecioUnitario() * var.getCantidad());
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

	private void moveFile(String fileName, String destino) {
		String path = "";
		String pathTarget = "";
		File file = null;
		
		try {
			if (this.tipoCarga == 1) 
				path = "/home/javaz/Descargas/CARGAS OC/caso1/";
			else if (this.tipoCarga == 2)
				path = "/home/javaz/Descargas/CARGAS OC/caso2/";
			else if (this.tipoCarga == 3)
				path = "/home/javaz/Descargas/CARGAS OC/caso3/";
			else 
				return;
			pathTarget = path + destino + "/";
			
			file = new File(path + this.fileName);
			file.renameTo(new File(pathTarget + this.fileName));
		} catch (Exception e) {
    		log.error("Ocurrio un problema al mover el archivo al detino: " + pathTarget, e);
    	}
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
	
	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(boolean operacion, int tipoMensaje, String mensaje) {
		this.operacionCancelada = operacion;
		this.tipoMensaje = tipoMensaje;
		
		this.mensaje = "";
		if (mensaje != null)
			this.mensaje = mensaje;
	}
	
	private void bitacora() {
		if (this.strBitacora != null)
			this.strBitacora.clear();
		this.strBitacora = new ArrayList<String>();
	}
	
	private void bitacora(String mensaje, boolean replacePrev) {
		if (mensaje == null || "".equals(mensaje))
			return;

		if (this.strBitacora == null)
			this.strBitacora = new ArrayList<String>();
		this.strBitacora.add(mensaje);
		
		if (replacePrev)
			this.strBitacora.remove(this.strBitacora.size() - 2);
		log.info("---> " + mensaje);
	}

	private void libroLog2Bitacora() {
		for (String val : this.libroLog)
			bitacora(val, false);
		this.libroLog.clear();
	}
	
	// ----------------------------------------------------------------------------------------
	// INVENTARIOS
	// ----------------------------------------------------------------------------------------
	
	private boolean comprobarEntrada(long idOrdenCompra) {
		List<AlmacenMovimientos> listMovs = null;
		
		listMovs = this.ifzMovimientos.findByProperty("idOrdenCompra", idOrdenCompra);
		if (listMovs == null || listMovs.isEmpty())
			return false;
		return true;
	}
	
	private void procesoInventario() {
		AlmacenMovimientos entrada = null;
		AlmacenMovimientos salida = null;
		List<MovimientosDetalle> detallesMovimiento = null;
		List<AlmacenMovimientos> listMovs = null;
		HashMap<Long, Double> aux = null;
		ObraAlmacenesExt obAlmacen = null;
		MovimientosDetalle entradaProducto = null;
		Almacen idAlmacen = null;
		Almacen idBodega = null;
		long idEncargadoAlmacen = 0L;
		long idEncargadoBodega  = 0L;
		double valCantidad = 0;
		// TRASPASO
		AlmacenTraspaso pojoTraspaso = null;
		TraspasoDetalle tDetalle = null;
		List<TraspasoDetalle> detallesTraspaso = null;
		// --------------------------------------------
		boolean encontrado = false;
		List<MovimientosDetalle> listAux = null;
		
		// Comprobamos movimientos
		listMovs = this.ifzMovimientos.findByProperty("idOrdenCompra", this.pojoOrdenCompra.getId());
		if (listMovs != null && ! listMovs.isEmpty()) {
			log.info("La Orden de Compra " + this.pojoOrdenCompra.getId() + " tiene movimientos: " + listMovs.size());
			for (AlmacenMovimientos item : listMovs) {
				item.setEstatus(0);
				item.setModificadoPor(1);
				item.setFechaModificacion(Calendar.getInstance().getTime());
			}
			
			try {
				this.ifzMovimientos.saveOrUpdateList(listMovs);
			} catch (Exception e) {
				log.info("Ocurrio un problema al intentar cancelar los movimientos de la Orden de Compra " + this.pojoOrdenCompra.getId());
				return;
			}
		}
		
		try {
			// Obtenemos ALMACEN
			log.info("Inventario ---> Recuperando ALMACEN ... ");
			obAlmacen = this.ifzObraAlmacen.findExtAlmacenPrincipal(this.pojoObra.getId(), this.pojoObra.getIdSucursal());
			if (obAlmacen != null) {
				idAlmacen = obAlmacen.getIdAlmacen();
				idEncargadoAlmacen = obAlmacen.getIdAlmacen().getIdEncargado();
				log.info("Inventario ---> Recuperando ALMACEN ... OK");
			}
			
			// Obtenemos BODEGA
			log.info("Inventario ---> Recuperando BODEGA ... ");
			obAlmacen = this.ifzObraAlmacen.findExtBodega(this.pojoObra.getId());
			if (obAlmacen != null) {
				idBodega = obAlmacen.getIdAlmacen();
				idEncargadoBodega = obAlmacen.getIdAlmacen().getIdEncargado();
				log.info("Inventario ---> Recuperando BODEGA ... OK");
			}
			
			if (idAlmacen == null || idAlmacen.getId() == null || idAlmacen.getId() <= 0L) 
				throw new Exception("No se pudo recuperar el Almacen Principal de la Obra " + this.pojoObra.getId());
			
			if (idBodega == null || idBodega.getId() == null || idBodega.getId() <= 0L) 
				throw new Exception("No se pudo recuperar la Bodega para la Obra " + this.pojoObra.getId());
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Almacen y/o Bodega para generar los Movimientos de Inventarios con la Orden de Compra " + this.pojoOrdenCompra.getId(), e);
			return;
		}

		try {
			// Entrada en ALMACEN
			log.info("Inventario ---> Generando ENTRADA X ORDEN DE COMPRA ... ");
			for (Entry<String, HashMap<Long, Double>> factura : this.mapFacturasProductos.entrySet()) {
				entrada = new AlmacenMovimientos();
				entrada.setFecha(Calendar.getInstance().getTime());
				entrada.setTipo(TipoMovimientoInventario.ENTRADA.ordinal());
				entrada.setTipoEntrada(TipoMovimientoReferencia.ORDEN_COMPRA.toString());
				entrada.setFolioFactura(factura.getKey());
				entrada.setIdTraspaso(0L);
				entrada.setIdOrdenCompra(this.pojoOrdenCompra.getId());
				entrada.setIdAlmacen(idAlmacen);
				entrada.setRecibe(idEncargadoAlmacen);
				entrada.setCreadoPor(this.usuarioId);
				entrada.setFechaCreacion(Calendar.getInstance().getTime());
				entrada.setModificadoPor(this.usuarioId);
				entrada.setFechaModificacion(Calendar.getInstance().getTime());
				
				aux = factura.getValue();
				if (detallesMovimiento == null)
					detallesMovimiento = new ArrayList<MovimientosDetalle>();
				for (Entry<Long, Double> item : aux.entrySet()) {
					valCantidad = item.getValue();
					entradaProducto = new MovimientosDetalle();
					entradaProducto.setIdProducto(item.getKey());
					entradaProducto.setCantidad(valCantidad);
					entradaProducto.setCreadoPor(this.usuarioId);
					entradaProducto.setFechaCreacion(Calendar.getInstance().getTime());
					entradaProducto.setModificadoPor(this.usuarioId);
					entradaProducto.setFechaModificacion(Calendar.getInstance().getTime());
					detallesMovimiento.add(entradaProducto);
				}
				log.info("Inventario ---> Generando ENTRADA X ORDEN DE COMPRA ... OK");
				
				log.info("Inventario ---> Guardando ENTRADA X ORDEN DE COMPRA ... ");
				entrada.setId(this.ifzMovimientos.save(entrada));
				for (MovimientosDetalle item : detallesMovimiento) {
					if (item.getId() == null || item.getId() <= 0L) {
						item.setIdAlmacenMovimiento(entrada.getId());
						item.setId(this.ifzMovimientosDetalle.save(item));
					}
				}
				log.info("Inventario ---> Guardando ENTRADA X ORDEN DE COMPRA ... OK");
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar la Entrada a Almacen " + idAlmacen + " de la Orden de Compra " + this.pojoOrdenCompra.getId(), e);
		} finally {
			actualizaBackOrder(TopicEventosInventarios.POST_ENTRADA, entrada.getIdOrdenCompra(), entrada.getId(), detallesMovimiento);
		}
		
		// Consolidamos Productos
		encontrado = true;
		listAux = new ArrayList<MovimientosDetalle>();
		for (MovimientosDetalle item : detallesMovimiento) {
			encontrado = false;
			for (MovimientosDetalle itemAux : listAux) {
				if (item.getIdProducto() == itemAux.getIdProducto()) {
					itemAux.setCantidad(itemAux.getCantidad() + item.getCantidad());
					encontrado = true;
					break;
				}
			}
			
			if (! encontrado)
				listAux.add(item);
		}

		detallesMovimiento.clear();
		detallesMovimiento.addAll(listAux);
		listAux.clear();
		
		// Traspaso a BODEGA
		try {
			log.info("Inventario ---> Generando TRASPASO ... ");
			pojoTraspaso = new AlmacenTraspaso();
			pojoTraspaso.setFecha(Calendar.getInstance().getTime());
			pojoTraspaso.setTipo(TipoTraspaso.TRASPASO.ordinal());
			pojoTraspaso.setIdAlmacenOrigen(idAlmacen);
			pojoTraspaso.setEntrega(idEncargadoAlmacen);
			pojoTraspaso.setIdAlmacenDestino(idBodega);
			pojoTraspaso.setRecibe(idEncargadoBodega);
			pojoTraspaso.setCreadoPor(this.usuarioId);
			pojoTraspaso.setFechaCreacion(Calendar.getInstance().getTime());
			pojoTraspaso.setModificadoPor(this.usuarioId);
			pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
			pojoTraspaso.setSistema(1);
			
			detallesTraspaso = new ArrayList<TraspasoDetalle>();
			for (MovimientosDetalle item : detallesMovimiento) {
				tDetalle = new TraspasoDetalle();
				tDetalle.setIdProducto(item.getIdProducto());
				tDetalle.setCantidad(item.getCantidad());
				tDetalle.setEstatus(1);
				tDetalle.setCreadoPor(this.usuarioId);
				tDetalle.setFechaCreacion(Calendar.getInstance().getTime());
				tDetalle.setModificadoPor(this.usuarioId);
				tDetalle.setFechaModificacion(Calendar.getInstance().getTime());
				detallesTraspaso.add(tDetalle);
			}
			log.info("Inventario ---> Generando TRASPASO ... OK");

			log.info("Inventario ---> Guardando TRASPASO ... ");
			pojoTraspaso.setId(this.ifzTraspaso.save(pojoTraspaso));
			for (TraspasoDetalle item : detallesTraspaso) {
				item.setIdAlmacenTraspaso(pojoTraspaso.getId());
				item.setId(this.ifzTraspasoDetalle.save(item));
			}
			log.info("Inventario ---> Guardando TRASPASO ... OK");
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar el Traspaso desde el Almacen " + idAlmacen + " hacia la Bodega " + idBodega, e);
		} finally {
			generaSalida(pojoTraspaso, detallesTraspaso, detallesMovimiento);
		}
		
		// Entrada en BODEGA
		try {
			log.info("Inventario ---> Generando ENTRADA X TRASPASO ... ");
			entrada.setId(0L);
			entrada.setTipo(TipoMovimientoInventario.ENTRADA.ordinal());
			entrada.setTipoEntrada(TipoMovimientoReferenciaExtendida.TRASPASO_ALMACEN_BODEGA.name());
			entrada.setFolioFactura("");
			entrada.setIdTraspaso(pojoTraspaso.getId());
			entrada.setIdOrdenCompra(0L);
			entrada.setIdAlmacen(idBodega);
			entrada.setRecibe(idEncargadoBodega);
			log.info("Inventario ---> Generando ENTRADA X TRASPASO ... OK");

			log.info("Inventario ---> Guardando ENTRADA X TRASPASO ... ");
			entrada.setId(this.ifzMovimientos.save(entrada));
			for (MovimientosDetalle item : detallesMovimiento) {
				item.setId(0L);
				item.setIdAlmacenMovimiento(entrada.getId());
				item.setId(this.ifzMovimientosDetalle.save(item));
			}
			log.info("Inventario ---> Guardando ENTRADA X TRASPASO ... OK");
		} catch (Exception e) {
			log.error("Ocurrio un problema al registrar la Entrada a Bodega " + idBodega + " del Traspaso " + pojoTraspaso.getId(), e);
		} finally {
			actualizarDetallesTraspaso(pojoTraspaso, detallesTraspaso);
			actualizaBackOrder(TopicEventosInventarios.MOVIMIENTO, pojoTraspaso.getId(), entrada.getId(), detallesMovimiento);
		}
		
		// SALIDA a Obra
		try {
			log.info("Inventario ---> Generando SALIDA A OBRA ... ");
			salida = new AlmacenMovimientos();
			salida.setFecha(Calendar.getInstance().getTime());
			salida.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
			salida.setTipoEntrada(TipoMovimientoReferencia.SALIDA_OBRA.toString());
			salida.setIdAlmacen(idBodega);
			salida.setEntrega(idEncargadoBodega);
			salida.setIdObra(this.pojoObra.getId());
			salida.setNombreObra(this.pojoObra.getNombre());
			salida.setRecibe(this.pojoObra.getIdResponsable());
			salida.setCreadoPor(this.usuarioId);
			salida.setFechaCreacion(Calendar.getInstance().getTime());
			salida.setModificadoPor(this.usuarioId);
			salida.setFechaModificacion(Calendar.getInstance().getTime());
			log.info("Inventario ---> Generando SALIDA A OBRA ... OK");

			log.info("Inventario ---> Guardando SALIDA A OBRA ... ");
			salida.setId(this.ifzMovimientos.save(salida));
			for (MovimientosDetalle item : detallesMovimiento) {
				item.setId(0L);
				item.setIdAlmacenMovimiento(salida.getId());
				item.setId(this.ifzMovimientosDetalle.save(item));
			}
			log.info("Inventario ---> Guardando SALIDA A OBRA ... OK");
		} catch (Exception e) {
			log.error("Ocurrio un problema al registrar la Salida de la Bodega " + idBodega + " hacia la Obra " + this.pojoObra.getId(), e);
		} finally {
			actualizaBackOrder(TopicEventosInventarios.MOVIMIENTO, salida.getId(), 0L, detallesMovimiento);
		}
	}
	
	private boolean generaSalida(AlmacenTraspaso pojoTraspaso, List<TraspasoDetalle> detalles, List<MovimientosDetalle> listaDetalle) {
		AlmacenMovimientos movSalida = null;
		MovimientosDetalle md = null;
		List<MovimientosDetalle> mDetalles = null;

		try {
			// Genero movimiento de SALIDA
			log.info("Inventario ---> Generando SALIDA X TRASPASO ... ");
			movSalida = new AlmacenMovimientos();
			movSalida.setTipo(1);
			if (pojoTraspaso.getTipo() == 1)
				movSalida.setTipoEntrada(TipoMovimientoReferencia.TRASPASO.toString());
			else if (pojoTraspaso.getTipo() == 2)
				movSalida.setTipoEntrada(TipoMovimientoReferencia.DEVOLUCION.toString());
			else
				movSalida.setTipoEntrada("");
			movSalida.setIdOrdenCompra(0L);
			movSalida.setIdTraspaso(pojoTraspaso.getId());
			movSalida.setIdAlmacen(pojoTraspaso.getIdAlmacenOrigen());
			movSalida.setFecha(pojoTraspaso.getFecha());
			movSalida.setEntrega(pojoTraspaso.getEntrega());
			movSalida.setRecibe(pojoTraspaso.getRecibe());
			movSalida.setCreadoPor(pojoTraspaso.getCreadoPor());
			movSalida.setFechaCreacion(pojoTraspaso.getFechaCreacion());
			movSalida.setModificadoPor(pojoTraspaso.getModificadoPor());
			movSalida.setFechaModificacion(pojoTraspaso.getFechaModificacion());

			// Genero detalles de salida
			for (TraspasoDetalle td : detalles) {
				md = new MovimientosDetalle();
				md.setIdAlmacenMovimiento(0L);
				md.setIdProducto(td.getIdProducto());
				md.setCantidad(td.getCantidad());
				md.setCantidadSolicitada(td.getCantidad());
				md.setEstatus(0);
				md.setCreadoPor(td.getCreadoPor());
				md.setFechaCreacion(td.getFechaCreacion());
				md.setModificadoPor(td.getModificadoPor());
				md.setFechaModificacion(td.getFechaModificacion());
				
				if (mDetalles == null)
					mDetalles = new ArrayList<MovimientosDetalle>();
				mDetalles.add(md);
			}
			log.info("Inventario ---> Generando SALIDA X TRASPASO ... OK");
			
			// Guardo salida x traspaso
			log.info("Inventario ---> Guardando SALIDA X TRASPASO ... ");
			movSalida.setId(this.ifzMovimientos.save(movSalida));
			for (MovimientosDetalle item : mDetalles) {
				item.setIdAlmacenMovimiento(movSalida.getId());
				item.setId(this.ifzMovimientosDetalle.save(item));
			}
			log.info("Inventario ---> Guardando SALIDA X TRASPASO ... OK");
		} catch (Exception e) {
			log.error("Ocurrio un problema al generar la SALIDA por TRASPASO " + pojoTraspaso.getId(), e);
			return false;
		} finally {
			actualizaBackOrder(TopicEventosInventarios.MOVIMIENTO, movSalida.getId(), 0L, listaDetalle);
		}
		
		return true;
	}

	private void actualizarDetallesTraspaso(AlmacenTraspaso pojoTraspaso, List<TraspasoDetalle> detalles) {
		try {
			pojoTraspaso.setCompleto(1);
			pojoTraspaso.setModificadoPor(this.usuarioId);
			pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzTraspaso.update(pojoTraspaso);
			
			for (TraspasoDetalle var : detalles) {
				var.setEstatus(2); // estatus ?  1 transito : 2 completado
				var.setCantidadRecibida(var.getCantidad());
				pojoTraspaso.setModificadoPor(this.usuarioId);
				pojoTraspaso.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzTraspasoDetalle.update(var);
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar actualizar los detalles del Traspaso", e);
		}
	}

	private void actualizaBackOrder(TopicEventosInventarios evento, Long idMovimiento, Long idReferencia, List<MovimientosDetalle> listDetalles) {
		MensajeTopic msgTopic = null;
		Gson gson = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (evento == null)
				return;
			
			gson = new Gson();
			target = idMovimiento.toString();
			referencia = idReferencia.toString();
			atributos = gson.toJson(listDetalles);
			
			// Generamos mensaje para BACKORDER	COMPRAS
			msgTopic = new MensajeTopic(evento, target, referencia, atributos, this.loginManager.getInfoSesion());
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al BACKORDER COMPRAS\n\n" + comando + "\n\n", e);
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------------
	
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
		return this.isDebug;
	}
	
	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public boolean getMultiCarga() {
		return (this.maxCargas == 0);
	}
	
	public void setMultiCarga(boolean value) {
		this.maxCargas = (value ? 0 : 1);
	}
	
	public int getMaxCargas() {
		return this.maxCargas;
	}

	public void setMaxCargas(int maxCargas) {
		this.maxCargas = maxCargas;
	}
	
	public List<SelectItem> getTiposCarga() {
		return tiposCarga;
	}

	public void setTiposCarga(List<SelectItem> tiposCarga) {
		this.tiposCarga = tiposCarga;
	}

	public int getTipoCarga() {
		return tipoCarga;
	}

	public void setTipoCarga(int tipoCarga) {
		this.tipoCarga = tipoCarga;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VER |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	1.2	| 09/01/2017 | Javier Tirado 			 | Creacion de CargaOrdenComprasAction
 *  2.2 | 29/04/2017 | Javier Tirado 			 | Añado validacion para interpretar M.N. como PESOS en cueStion de MONEDA. Corrijo el metodo getVal
 */