package net.giro.inventarios;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenMovimientos;
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenTraspaso;
import net.giro.inventarios.beans.AlmacenTraspasoExt;
import net.giro.inventarios.beans.MovimientosDetalle;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoExt;
import net.giro.inventarios.beans.TraspasoDetalleExt;
import net.giro.inventarios.comun.TipoMovimientoInventario;
import net.giro.inventarios.comun.TipoMovimientoReferencia;
import net.giro.inventarios.comun.TipoTraspaso;
import net.giro.inventarios.logica.AlmacenMovimientosRem;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.AlmacenTraspasoRem;
import net.giro.inventarios.logica.MovimientosDetalleRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.inventarios.logica.TraspasoDetalleRem;
import net.giro.navegador.LoginManager;
import net.giro.navegador.comun.Permiso;
import net.giro.ne.logica.NQueryRem;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.topics.MensajeTopic;
import net.giro.plataforma.topics.TopicEventosInventarios;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

@ViewScoped
@ManagedBean(name="sboAction")
public class SolicitudesBodegaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SolicitudesBodegaAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private Permiso permisos;
	private HttpSession httpSession;
	// Interfaces
	// ----------------------------------------
	private AlmacenTraspasoRem ifzSolicitudes;
	private TraspasoDetalleRem ifzSolicitudDetalles;
	private AlmacenMovimientosRem ifzMovimientos;
	private MovimientosDetalleRem ifzMovimientosDetalle;
	private AlmacenProductoRem ifzAlmacenProducto;
	private AlmacenRem ifzAlmacen;
	private EmpleadoRem ifzEmpleado;
	private ReportesRem ifzReportes;
	private NQueryRem ifzQuery;
	// principal
	// ----------------------------------------
	private List<AlmacenTraspaso> listSolicitudes;
	private long idSolicitud;
	private AlmacenTraspaso pojoSolicitud;
	private List<TraspasoDetalleExt> listSolicitudProductos;
	private int paginacionSolicitudDetalles;
	// variables
	// ----------------------------------------
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private boolean esAdministrativo;
	private boolean buscarAdministrativos;
	private long idExplosionInsumos;
	private long idMovimientoSalida;
	// Editar Producto
	// ----------------------------------------
	private String valorPrevio;
	private boolean valorEditarProducto;
	private boolean afectarAlmacen;
	private TraspasoDetalleExt pojoDetalleQuitar;
	// Busqueda principal
	// ----------------------------------------
	private List<SelectItem> resultsBusquedaTipos;
	private String resultsBusquedaCampo;
	private String resultsBusquedaValor;
	private int resultsPaginas;
	//Busqueda Productos
	// ----------------------------------------
	private ProductoRem ifzProducto;
	private List<Producto> listBusquedaProductos; 
	private List<SelectItem> tiposBusquedaProductos;	
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private long idProducto;
	private int paginacionProductos;
	// Almacen TRABAJO
	// ----------------------------------------
	private List<SelectItem> listAlmacenesItems;
	private List<Almacen> listAlmacenes;
	private long idAlmacen;
	private boolean almacenAdmvo;
	private boolean almacenLock;
	// EMPLEADO-USUARIO
	// ----------------------------------------
	private boolean usuarioValido;
	private EmpleadoExt pojoEmpleadoUsuario;
	private List<Long> listPuestosValidos;
	private long idPuestoCompras;
	/*// PERMISOS
	// ----------------------------------------
	private boolean permisoConsultar;
	private boolean permisoAgregar;
	private boolean permisoEditar;
	private boolean permisoBorrar;
	private boolean permisoImprimir;*/
	// DEBUG
	// ----------------------------------------
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public SolicitudesBodegaAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String valPerfil = "";
		String[] splitted = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;

			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.esAdministrativo = ((valPerfil != null && "S".equals(valPerfil.trim())) ? true : false);
			this.buscarAdministrativos = this.esAdministrativo;
			
			// Recuperamos puesto requerido (No obligatorio)
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());

			this.listPuestosValidos = new ArrayList<Long>();
			
			// Puestos Validos para Almacenistas/Bodegueros
			valPerfil = this.entornoProperties.getString("puesto");
			if (valPerfil != null && ! "".equals(valPerfil.trim())) {
				if (valPerfil.contains(",")) {
					splitted = valPerfil.split(",");
					for (String val : splitted)
						this.listPuestosValidos.add(Long.valueOf(val));
				} else {
					this.listPuestosValidos.add(Long.valueOf(valPerfil));
				}
			}

			// Puesto de Compras
			this.idPuestoCompras = 0L;
			valPerfil = this.entornoProperties.getString("compras");
			if (valPerfil != null && ! "".equals(valPerfil.trim()))
				this.idPuestoCompras = Long.valueOf(valPerfil);

			// Permisos
			valPerfil = (this.entornoProperties.containsKey("SOLICITUDES") ? this.entornoProperties.getString("SOLICITUDES") : "");
			valPerfil = (valPerfil != null && ! "".equals(valPerfil.trim())) ? valPerfil.trim() : "0";
			this.permisos = this.loginManager.getPermisos(this.loginManager.getIdAplicacion(), Long.parseLong(valPerfil));
			
			ctx = new InitialContext();
			this.ifzSolicitudes = (AlmacenTraspasoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenTraspasoFac!net.giro.inventarios.logica.AlmacenTraspasoRem");
			this.ifzSolicitudDetalles = (TraspasoDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//TraspasoDetalleFac!net.giro.inventarios.logica.TraspasoDetalleRem");
			this.ifzMovimientos = (AlmacenMovimientosRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenMovimientosFac!net.giro.inventarios.logica.AlmacenMovimientosRem");
			this.ifzMovimientosDetalle = (MovimientosDetalleRem) ctx.lookup("ejb:/Logica_Inventarios//MovimientosDetalleFac!net.giro.inventarios.logica.MovimientosDetalleRem");
			this.ifzAlmacenProducto = (AlmacenProductoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			this.ifzAlmacen = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzProducto = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzEmpleado = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzQuery = (NQueryRem) ctx.lookup("ejb:/Logica_Publico//NQueryFac!net.giro.ne.logica.NQueryRem");

			this.ifzSolicitudes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSolicitudDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzProducto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMovimientos.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzMovimientosDetalle.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Busqueda principal
			this.resultsBusquedaTipos = new ArrayList<SelectItem>();
			this.resultsBusquedaTipos.add(new SelectItem("*","Coincidencia"));
			this.resultsBusquedaTipos.add(new SelectItem("nombre","Nombre"));
			this.resultsBusquedaTipos.add(new SelectItem("nombreCliente","Cliente"));
			this.resultsBusquedaTipos.add(new SelectItem("nombreContrato","Contrato"));
			this.resultsBusquedaTipos.add(new SelectItem("id","ID"));
			this.resultsBusquedaCampo = this.resultsBusquedaTipos.get(0).getValue().toString();
			this.resultsBusquedaValor = "";
			this.resultsPaginas = 1;

			// Busqueda Productos
			this.tiposBusquedaProductos = new ArrayList<SelectItem>();
			this.tiposBusquedaProductos.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Nombre"));
			this.tiposBusquedaProductos.add(new SelectItem("clave", "Clave"));
			this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
			nuevaBusquedaProductos();
			
			this.usuarioValido = false;
			if (comprobarUsuario())
				this.usuarioValido = true;
			this.afectarAlmacen = true;
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al instanciar InventarioAction", e);
		}
	}
	
	public void buscar() {
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			if (! this.permisos.getConsultar()) {
				control(301, "No tiene permitido consultar informacion");
				return;
			}
			
			if (this.idAlmacen <= 0L) {
				control(-1, "Debe indicar un Almacen");
				return;
			}
			
			this.listSolicitudes = this.ifzSolicitudes.findSolicitudLikeProperty(this.resultsBusquedaCampo, this.resultsBusquedaValor, this.idAlmacen, true, true, "date(model.fecha) desc, model.id desc", 0);
			if (this.listSolicitudes == null || this.listSolicitudes.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
    		control("Ocurrio un problema al consultar las Solicitudes de Bodega", e);
    	}
	}
	
	public void editar() {
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			if (! this.permisos.getEditar()) {
				control(301, "No tiene permitido Añadir/Editar informacion");
				return;
			}
			
			if (this.pojoSolicitud == null || this.pojoSolicitud.getId() == null || pojoSolicitud.getId() <= 0L) {
				control(-1, "No selecciono ninguna Solicitud de Bodega");
				return;
			}
			
			// Comprobamos base en Explosion de Insumos
			this.idExplosionInsumos = 0L;
			if (this.pojoSolicitud.getSolicitudRequisicion() <= 0L && this.pojoSolicitud.getSolicitudCotizacion() > 0L)
				this.idExplosionInsumos = this.explosionInsumos(this.pojoSolicitud.getIdObra());
			
			this.idMovimientoSalida = this.movimientoSalida(this.pojoSolicitud.getId());
			if (this.idMovimientoSalida <= 0L)
				this.idMovimientoSalida = 0L;
			
			// Comprobamos quien entrega los productos
			if (this.pojoSolicitud.getEntregaNombre() == null || "".equals(this.pojoSolicitud.getEntregaNombre().trim()))
				this.pojoSolicitud.setEntregaNombre(this.pojoSolicitud.getIdAlmacenOrigen().getNombreEncargado());

			// Comprobamos quien recibe los productos
			if (this.pojoSolicitud.getRecibeNombre() == null || "".equals(this.pojoSolicitud.getRecibeNombre().trim()))
				this.pojoSolicitud.setRecibeNombre(this.pojoSolicitud.getIdAlmacenDestino().getNombreEncargado());
			
			// Recuperamos detalle de solicitud
			this.listSolicitudProductos = this.ifzSolicitudDetalles.findExtAll(this.pojoSolicitud.getId());
			Collections.sort(this.listSolicitudProductos, new Comparator<TraspasoDetalleExt>() {
				@Override
				public int compare(TraspasoDetalleExt o1, TraspasoDetalleExt o2) {
					return o1.getIdProducto().getClave().compareTo(o2.getIdProducto().getClave());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la Solicitud de Bodega indicada", e);
		} finally {
			this.paginacionSolicitudDetalles = 1;
			this.valorEditarProducto = false;
		}
	}
	
	public void guardar() {
		AlmacenTraspaso traspaso = null;
		TraspasoDetalleExt det = null;
		List<TraspasoDetalleExt> detalles = null;
		
		try {
			control();
			if (this.pojoSolicitud == null || this.pojoSolicitud.getId() == null || pojoSolicitud.getId() <= 0L) {
				control(-1, "No selecciono ninguna Solicitud de Bodega");
				return;
			}
			
			if ((this.pojoSolicitud.getEstatus() == 2) || (this.pojoSolicitud.getIdTraspaso() != null && this.pojoSolicitud.getIdTraspaso().getId() != null && this.pojoSolicitud.getIdTraspaso().getId() > 0L)) {
				this.pojoSolicitud.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoSolicitud.setFechaModificacion(Calendar.getInstance().getTime());
				for (TraspasoDetalleExt item : this.listSolicitudProductos) {
					item.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					item.setFechaModificacion(Calendar.getInstance().getTime());
				}
				
				this.ifzSolicitudes.update(this.pojoSolicitud);
				this.ifzSolicitudDetalles.saveOrUpdateListExt(this.listSolicitudProductos);
				buscar();
				return;
			}

			// Generamos Traspaso nuevo
			// -------------------------------------------------------------------------------------------------------
			traspaso = new AlmacenTraspaso();
			BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
			BeanUtils.copyProperties(traspaso, this.pojoSolicitud);
			
			traspaso.setId(0L);
			traspaso.setTipo(TipoTraspaso.TRASPASO.ordinal());
			traspaso.setFecha(Calendar.getInstance().getTime());
			traspaso.setSolicitudCotizacion(0L);
			traspaso.setSolicitudRequisicion(0L);
			traspaso.setCompleto(0);
			traspaso.setEstatus(0);
			traspaso.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			traspaso.setFechaCreacion(Calendar.getInstance().getTime());
			traspaso.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			traspaso.setFechaModificacion(Calendar.getInstance().getTime());
			
			// Genero los detalles para el Traspaso nuevo
			detalles = new ArrayList<TraspasoDetalleExt>();
			for (TraspasoDetalleExt item : this.listSolicitudProductos) {
				if (item.getCantidad() <= 0) // excluimos productos sin cantidad
					continue;
				
				det = new TraspasoDetalleExt();
				BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
				BeanUtils.copyProperties(det, item);
				
				det.setId(0L);
				det.setIdAlmacenTraspaso(0L);
				det.setEstatus(1);
				det.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				det.setFechaCreacion(Calendar.getInstance().getTime());
				det.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				det.setFechaModificacion(Calendar.getInstance().getTime());
				detalles.add(det);
			}
			
			// Guardo el Traspaso nuevo
			traspaso.setId(this.ifzSolicitudes.save(traspaso));
			// Guardo los detalles del Traspaso nuevo
			for (TraspasoDetalleExt item : detalles)
				item.setIdAlmacenTraspaso(traspaso.getId());
			this.ifzSolicitudDetalles.saveOrUpdateListExt(detalles);

			// Actualizamos estatus de Solicitud a Bodega
			this.pojoSolicitud.setIdTraspaso(traspaso);
			this.pojoSolicitud.setCompleto(0);
			this.pojoSolicitud.setEstatus(2);
			this.pojoSolicitud.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoSolicitud.setFechaModificacion(Calendar.getInstance().getTime());
			this.ifzSolicitudes.update(this.pojoSolicitud);
			
			// Actualizamos estatus de detalles de solicitud a bodega
			for (TraspasoDetalleExt item : this.listSolicitudProductos) {
				item.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				item.setFechaModificacion(Calendar.getInstance().getTime());
				item.setEstatus(2);
			}
			this.ifzSolicitudDetalles.saveOrUpdateListExt(this.listSolicitudProductos);
			
			// Lanzamos actualizacion de cantidad en solicitud para los productos
			lanzarSolicitudBodegaCantidad(this.pojoSolicitud.getId());
			
			// Imprimimos traspaso generado
			imprimirTraspaso(traspaso.getId());
			buscar();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la Solicitud de Bodega indicada", e);
		}
	}
	
	public void cancelar() {
		Respuesta respuesta = null;
		
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			if (! this.permisos.getBorrar()) {
				control(301, "No tiene permitido Borrar/Eliminar informacion");
				return;
			}
			
			if (this.pojoSolicitud == null || this.pojoSolicitud.getId() == null || this.pojoSolicitud.getId() <= 0L) {
				if (this.idSolicitud > 0L)
					this.pojoSolicitud = this.ifzSolicitudes.findById(this.idSolicitud);
				if (this.pojoSolicitud == null || this.pojoSolicitud.getId() == null || this.pojoSolicitud.getId() <= 0L) {
					control(-1, "No selecciono ninguna Solicitud de Bodega");
					return;
				}
			}
			
			if (this.pojoSolicitud.getEstatus() == 1) {
				if (! validaRequest("CANCEL")) {
					control(-1, "La Solicitud de Bodega ya fue Cancelada previamente");
					return;
				}
			}
			
			if (this.pojoSolicitud.getEstatus() == 2) {
				if (! validaRequest("CANCEL")) {
					control(-1, "No se puede Cancelar la Solicitud de Bodega.\n Ya ha sido enviada a Bodega");
					return;
				}
			}
			
			respuesta = this.ifzSolicitudes.cancelar(this.pojoSolicitud);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Problema al Cancelar la Solicitud de Bodega\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}
			
			this.pojoSolicitud = (AlmacenTraspaso) respuesta.getBody().getValor("solicitud");
			if (this.listSolicitudes != null && ! this.listSolicitudes.isEmpty()) {
				for (AlmacenTraspaso solicitud : this.listSolicitudes) {
					if (this.pojoSolicitud.getId().longValue() != solicitud.getId().longValue()) 
						continue;
					this.listSolicitudes.set(this.listSolicitudes.indexOf(solicitud), this.pojoSolicitud);
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar la Solicitud de Bodega indicada", e);
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
			log.info("Imprimiendo Solicitud a Bodega: " + this.idSolicitud + " ... ");
			formatter = new SimpleDateFormat("yyMMddHH");
			nombreDocumento = "SBO-[id]-[fecha].[extension]";
			
			if (this.idSolicitud <= 0) {
				control(-1, "Debe indicar una Solicitud de Bodega");
				return;
			}
			
			// Parametros del reporte
			log.info("Generando parametros ... ");
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idObra", this.idSolicitud);

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.solicitudBodega"));
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
				log.error("ERROR INTERNO: " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al intentar imprimir la Solicitud a Bodega\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			} 

			log.info("Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = nombreDocumento.replace("[id]", String.format("%1$05d", this.idAlmacen).substring(3));
			nombreDocumento = nombreDocumento.replace("[fecha]", formatter.format(Calendar.getInstance().getTime()));
			nombreDocumento = nombreDocumento.replace("[extension]", formatoDocumento);
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				control(-1, "Ocurrio un problema al intentar imprimir la Solicitud a Bodega");
				return;
			}

			log.info("Lanzando reporte ... ");
			this.httpSession.setAttribute("formato", formatoDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			log.info("Solicitud a Bodega exportada. Proceso finalizado");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la Solicitud de Bodega", e);
		} finally {
			this.idSolicitud = 0L;
		}
	}
	
	public void descarga() {
		MensajeTopic msgTopic = null;
		// -----------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		String comando = "";
		
		try {
			if (this.pojoSolicitud == null || this.pojoSolicitud.getId() == null || pojoSolicitud.getId() <= 0L)
				return;
			target = this.pojoSolicitud.getId().toString();
			
			msgTopic = new MensajeTopic(TopicEventosInventarios.SOLICITUD_BODEGA_DESCARGA, target, referencia, atributos, this.loginManager.getInfoSesion());
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS:BO-SBO-DESCARGA\n\n" + comando + "\n\n", e);
		}
	}
	
	public void filtrarAlmacenes() {
		// Inicializaciones
		this.idAlmacen = 0L;
		if (this.listAlmacenesItems == null)
			this.listAlmacenesItems = new ArrayList<SelectItem>();
		this.listAlmacenesItems.clear();
		
		if (this.listAlmacenes != null && ! this.listAlmacenes.isEmpty()) {
			for (Almacen almacen : this.listAlmacenes) {
				if (! this.buscarAdministrativos && almacen.getTipo() == 3)
					continue;
				if (this.idAlmacen <= 0L)
					this.idAlmacen = almacen.getId();
				this.listAlmacenesItems.add(new SelectItem(almacen.getId(), almacen.getNombre() + " (" + almacen.getIdentificador() + ")"));
			}
		}
		
		this.almacenLock = false;
		if (! this.listAlmacenesItems.isEmpty()) {
			if (this.listAlmacenesItems.size() == 1) 
				this.almacenLock = true;
			
			if (this.listAlmacenesItems.size() > 1) {
				this.listAlmacenesItems.add(0, new SelectItem(0L, "Seleccione ..."));
				this.idAlmacen = 0L;
			}
		}
	}
	
	private void validarAlmacenAdministrativo() {
		this.almacenAdmvo = false;
		if (this.idAlmacen <= 0L)
			return;
		if (this.listAlmacenes == null || this.listAlmacenes.isEmpty())
			return;
		for (Almacen almacen : this.listAlmacenes) {
			if (this.idAlmacen == almacen.getId().longValue()) {
				this.almacenAdmvo = almacen.getTipo() >= 3;
				break;
			}
		}
	}
	
	private long generaMovimientoSalida(long idTraspaso) {
		List<MovimientosDetalle> movDetalles = null;
		AlmacenMovimientos movimiento = null;
		MovimientosDetalle movDetalle = null; 
		AlmacenTraspasoExt pojoTraspaso = null;
		
		try {
			pojoTraspaso = this.ifzSolicitudes.findByIdExt(idTraspaso);
			if (pojoTraspaso == null || pojoTraspaso.getId() == null || pojoTraspaso.getId() <= 0L)
				return 0;
			
			// Genero Movimiento (Entrada o Salida) y detalles
			log.info("Generando Movimiento y detalles ...");
			movimiento = new AlmacenMovimientos();
			movimiento.setSistema(1);
			movimiento.setFecha(pojoTraspaso.getFecha());
			movimiento.setIdOrdenCompra(0L);
			movimiento.setIdTraspaso(pojoTraspaso.getId());
			movimiento.setEntrega(pojoTraspaso.getEntrega().getId());
			movimiento.setRecibe(pojoTraspaso.getRecibe().getId());
			movimiento.setCreadoPor(pojoTraspaso.getCreadoPor());
			movimiento.setFechaCreacion(pojoTraspaso.getFechaCreacion());
			movimiento.setModificadoPor(pojoTraspaso.getModificadoPor());
			movimiento.setFechaModificacion(pojoTraspaso.getFechaModificacion());
			movimiento.setTipo(TipoMovimientoInventario.SALIDA.ordinal());
			movimiento.setTipoEntrada(TipoMovimientoReferencia.TRASPASO.toString());
			movimiento.setIdAlmacen(pojoTraspaso.getAlmacenOrigen());

			movDetalles = new ArrayList<MovimientosDetalle>();
			for (TraspasoDetalleExt td : this.listSolicitudProductos) {
				movDetalle = new MovimientosDetalle();
				movDetalle.setIdProducto(td.getIdProducto().getId());
				movDetalle.setCantidad(td.getCantidad());
				movDetalle.setCantidadSolicitada(td.getCantidad());
				movDetalle.setEstatus(0);
				movDetalle.setCreadoPor(td.getCreadoPor());
				movDetalle.setFechaCreacion(td.getFechaCreacion());
				movDetalle.setModificadoPor(td.getModificadoPor());
				movDetalle.setFechaModificacion(td.getFechaModificacion());
				// ---------------------------------------------------------
				movDetalles.add(movDetalle);
			}

			// Guardo entrada (movimiento) y detalles
			log.info("Guardando Movimiento y detalles ...");
			movimiento.setId(this.ifzMovimientos.save(movimiento));
			for (MovimientosDetalle item : movDetalles)
				item.setIdAlmacenMovimiento(movimiento.getId());
			movDetalles = this.ifzMovimientosDetalle.saveOrUpdateList(movDetalles);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar autorecibir el Traspaso", e);
			return 0;
		} 
		
		return movimiento.getId();
	}
	
	private void imprimirTraspaso(long idTraspaso) {
		List<AlmacenMovimientos> listMovs = null;
		AlmacenMovimientos pojoMovimiento = null;
		long idMovimiento = 0L;
		SimpleDateFormat formatter = null;
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		// --------------------------------------------
		AlmacenTraspasoExt pojoTraspaso = null;
		
		try {
			control();
			pojoTraspaso = this.ifzSolicitudes.findByIdExt(idTraspaso);
			if (pojoTraspaso == null || pojoTraspaso.getId() == null || pojoTraspaso.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar obtener el Traspaso seleccionado");
				return;
			}
			
			// Recuperamos el Movimiento
			listMovs = this.ifzMovimientos.findSalidaByTraspaso(pojoTraspaso.getId(), pojoTraspaso.getTipo(), 0);
			if (listMovs != null && ! listMovs.isEmpty()) {
				pojoMovimiento = listMovs.get(0);
				idMovimiento = pojoMovimiento.getId(); 
			}
			
			if (idMovimiento <= 0L) {
				idMovimiento = generaMovimientoSalida(idTraspaso);
				if (idMovimiento <= 0L) {
					control(-1, "Ocurrio un problema al intentar obtener el Traspaso seleccionado");
					return;
				}
			}
			
			// Parametros del reporte
			log.info("Generando parametros ... ");
			formatter = new SimpleDateFormat("yyMMddHHss");
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idMovimiento", idMovimiento);
			
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
				log.error("ERROR INTERNO - " + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al intentar imprimir la Entrada a Almacen\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 

			log.info("Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "T-AL-" + pojoTraspaso.getId() + "-" + formatter.format(Calendar.getInstance().getTime());
			
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
	
	private void lanzarSolicitudBodegaCantidad(Long idSolicitud) {
		MensajeTopic msgTopic = null;
		TopicEventosInventarios eventoInventario = null;
		String comando = "";
		// ----------------------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if (idSolicitud == null || idSolicitud <= 0L) 
				return;
			
			// Generamos evento para solicitud a bodega
			eventoInventario = TopicEventosInventarios.SOLICITUD_BODEGA_CANTIDAD;
			target = (idSolicitud != null && idSolicitud > 0L) ? idSolicitud.toString() : "0";
			
			// lanzamos evento
			msgTopic = new MensajeTopic(eventoInventario, target, referencia, atributos, this.loginManager.getInfoSesion());
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS\n\n" + comando + "\n\n", e);
		}
	}
	
	private void lanzarSolicitudBodegaQuitarProducto(Long idSolicitud, Long idProducto, Double cantidad, boolean afectarAlmacen) {
		TopicEventosInventarios eventoInventario = null;
		MensajeTopic msgTopic = null;
		Gson gson = null;
		String comando = "";
		HashMap<Long, Double> borrables = null;
		// ----------------------------------------------------
		String target = "0";
		String referencia = "0";
		String atributos = "";
		
		try {
			if ((idSolicitud == null || idSolicitud <= 0L) || (idProducto == null || idProducto <= 0L))
				return;
			
			gson = new Gson();
			borrables = new HashMap<Long, Double>();
			borrables.put(idProducto, cantidad);
			
			// Generamos evento para solicitud a bodega
			eventoInventario = TopicEventosInventarios.SOLICITUD_BODEGA_QUITAR;
			target = (idSolicitud != null && idSolicitud > 0L) ? idSolicitud.toString() : "0";
			referencia = (afectarAlmacen ? "1" : "0");
			atributos = gson.toJson(borrables);
			
			// lanzamos evento
			msgTopic = new MensajeTopic(eventoInventario, target, referencia, atributos, this.loginManager.getInfoSesion());
			msgTopic.enviar();
		} catch (Exception e) {
			if (msgTopic != null) {
				msgTopic.storageEvent();
				comando = msgTopic.getCommand();
			}
			log.error("Ocurrio un problema al intentar enviar mensaje al topic/INVENTARIOS\n\n" + comando + "\n\n", e);
		}
	}
	
	private boolean comprobarUsuario() {
		List<Almacen> almacenes = null;
		Long idEmpleado = null;
		
		try {
			// Inicializaciones
			this.pojoEmpleadoUsuario = null;
			this.listAlmacenes = new ArrayList<Almacen>();
			if (this.loginManager == null)
				return false;
			
			if ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) {
				almacenes = this.ifzAlmacen.findAll();
				return true;
			}
			
			// Determinamos EMPLEADO de manera directa
			idEmpleado = this.loginManager.getUsuarioResponsabilidad().getUsuario().getIdEmpleado();
			if (idEmpleado != null && idEmpleado > 1L) 
				this.pojoEmpleadoUsuario = this.ifzEmpleado.findByIdExt(idEmpleado);
			
			// Comprobamos empleado
			// --------------------------------------------------------------------------------------------------------------------------
			if (this.pojoEmpleadoUsuario == null || this.pojoEmpleadoUsuario.getId() == null || this.pojoEmpleadoUsuario.getId() <= 0L) {
				log.info("Usuario con asociacion de Empleado no valida ");
				almacenes = null;
				return false;
			}
			
			// Comprobamos PUESTO
			// --------------------------------------------------------------------------------------------------------------------------
			if (this.pojoEmpleadoUsuario.getPuestoCategoria() == null || this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto() == null)
				return false;
			if (this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() == null || this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() <= 0L)
				return false;
			if (this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId().longValue() == this.idPuestoCompras) {
				almacenes = this.ifzAlmacen.findAll();
				return false;
			}

			if (! this.listPuestosValidos.contains(this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId())) 
				return false;

			// Comprobamos ALMACENES
			// --------------------------------------------------------------------------------------------------------------------------
			almacenes = this.ifzAlmacen.findByEncargado(idEmpleado);
			if (almacenes == null || almacenes.isEmpty())
				almacenes = this.ifzAlmacen.findAll();
		} catch (Exception e) {
			log.error("Ocurrio un problema en Inventarios.EntradasAlmacenAction.comprobarUsuario()", e);
			return false;
		} finally {
			if (almacenes != null && ! almacenes.isEmpty())
				cargaAlmacenes(almacenes);
			filtrarAlmacenes();
		}
		
		return true;
	}
	
	private void cargaAlmacenes(List<Almacen> almacenes) {
		// Inicializaciones
		if (this.listAlmacenes == null)
			this.listAlmacenes = new ArrayList<Almacen>();
		this.listAlmacenes.clear();
		
		if (almacenes == null || almacenes.isEmpty())
			return;

		for (Almacen almacen : almacenes) {
			if (almacen.getTipo() == 2 || almacen.getTipo() == 4)
				continue;
			if (! this.esAdministrativo && almacen.getTipo() == 3)
				continue;
			this.listAlmacenes.add(almacen);
		}
	}

	@SuppressWarnings("unchecked")
	private long explosionInsumos(Long idObra) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long resultado = 0;
		
		try {
			if (idObra == null || idObra <= 0L)
				return 0L;
			queryString = "select id from insumos_presupuesto where id_obra = :idObra and estatus = 0 ";
			queryString = queryString.replace(":idObra", idObra.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = ((BigDecimal) rows.get(0)).longValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el idResponsable de la Obra: " + idObra, e);
			resultado = 0;
		}
		
		return resultado;
	}

	@SuppressWarnings("unchecked")
	private long movimientoSalida(Long idSolicitud) {
		List<Object> rows = null;
		String queryString = "";
		// --------------------------------------------
		long resultado = 0;
		
		try {
			if (idSolicitud == null || idSolicitud <= 0L)
				return 0L;
			queryString = "select id from almacen_movimientos where tipo = 1 and id_traspaso = :idSolicitud ";
			queryString = queryString.replace(":idSolicitud", idSolicitud.toString());
			rows = this.ifzQuery.findNativeQuery(queryString);
			if (rows != null && ! rows.isEmpty())
				resultado = ((BigDecimal) rows.get(0)).longValue();
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Movimiento de SALIDA para la Solicitud: " + idSolicitud, e);
			resultado = 0;
		}
		
		return resultado;
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
	
	@SuppressWarnings("unused")
	private void validaRequest(String param, boolean value) {
		if (! this.isDebug)
			return;
		
		param = param.trim().toUpperCase();
		if (! this.paramsRequest.containsKey(param) && value) 
			this.paramsRequest.put(param, "1");
		else if (this.paramsRequest.containsKey(param) && ! value) 
			this.paramsRequest.remove(param);
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
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Se ha producido un error desconocido";
		
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = "<br><br>" + sw.toString();
		}
		
		log.error("\nSOLICITUD A BODEGA :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + "\n" + this.mensaje + this.mensajeDetalles, throwable);
	}

	// --------------------------------------------------------------
	// Editar Producto
	// --------------------------------------------------------------

	public void guardaCantidadPrevia(AjaxBehaviorEvent event) {
		String value = "";
		int index = -1;

		try {
			control();
			value = (event.getComponent().getAttributes().get("targetIndex") != null) ? event.getComponent().getAttributes().get("targetIndex").toString() : "-1";
			index = Integer.valueOf(value);
			if (index < 0)
				return;
			
			this.valorPrevio = index + ":" + this.listSolicitudProductos.get(index).getCantidad();
		} catch (Exception e) {
    		control("Ocurrio un problema al actualizar las cantidad en el Inventario", e);
		}
	}
	
	public void guardaCantidadProducto(AjaxBehaviorEvent event) {
		AlmacenProducto existencia = null;
		TraspasoDetalleExt detalle = null;
		double cantidadOriginal = 0;
		double cantidadNueva = 0;
		double diferencia = 0;
		// ----------------------------------
		String value = "";
		int index = -1;

		try {
			control();
			value = (event.getComponent().getAttributes().get("targetIndex") != null) ? event.getComponent().getAttributes().get("targetIndex").toString() : "-1";
			index = Integer.valueOf(value);
			if (index < 0)
				return;
			
			detalle = this.listSolicitudProductos.get(index);
			cantidadNueva = detalle.getCantidad();
			if (this.valorPrevio.contains(index + ":")) {
				cantidadOriginal = Double.parseDouble(this.valorPrevio.replace(index + ":", ""));
				if (cantidadOriginal == cantidadNueva) 
					return;
			}
			
			if (cantidadNueva <= 0) {
				this.listSolicitudProductos.get(index).setCantidad(cantidadOriginal);
				control(-1, "No se permite cambiar la cantidad a cero o numeros negativos");
				return;
			}
			
			// Actualizo el datos en BD
			this.ifzSolicitudDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzSolicitudDetalles.update(detalle);
			
			// Actualizamos base de Solicitud (Requisicion o Cotizacion)
			diferencia = cantidadNueva - cantidadOriginal;
			this.ifzAlmacenProducto.setInfoSesion(this.loginManager.getInfoSesion());
			existencia = this.ifzAlmacenProducto.findAlmacenProducto(this.pojoSolicitud.getIdAlmacenOrigen().getId(), detalle.getIdProducto().getId());
			if (existencia == null || existencia.getId() == null || existencia.getId() <= 0L)
				return;
			existencia.addSolicitud(diferencia);
			this.ifzAlmacenProducto.update(existencia);
			control(false, 3, "Producto actualizado!", null);
		} catch (Exception e) {
    		control("Ocurrio un problema al actualizar las cantidad en el Inventario", e);
		} finally {
			this.valorPrevio = "";
		}
	}

	public void quitarProducto() {
		try {
			if (this.pojoSolicitud == null || this.pojoSolicitud.getId() == null || this.pojoSolicitud.getId() <= 0L) {
				control(-1, "Ocurrio un problema con la Solicitud a Bodega seleccioanda");
				return;
			}

			if (this.pojoDetalleQuitar == null || this.pojoDetalleQuitar.getId() == null || this.pojoDetalleQuitar.getId() <= 0L) {
				control(-1, "No indico ningun Producto de la Solicitud a Bodega");
				return;
			}
			
			this.listSolicitudProductos.remove(this.pojoDetalleQuitar);
			lanzarSolicitudBodegaQuitarProducto(this.pojoSolicitud.getId(), this.pojoDetalleQuitar.getIdProducto().getId(), this.pojoDetalleQuitar.getCantidad(), this.afectarAlmacen);
			this.pojoDetalleQuitar = null;
			this.afectarAlmacen = true;
			control(false, 4, "Producto eliminado!", null);
		} catch (Exception e) {
    		control("Ocurrio un problema al intentar quitar el producto indicado de la Solicitud a Bodega", e);
		}
	}

	// --------------------------------------------------------------------
	// BUSQUEDA PRODUCTOS 
	// --------------------------------------------------------------------

	public void nuevaBusquedaProductos() {
		control();
		this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
		this.valorBusquedaProductos = "";
		this.paginacionProductos = 1;
		
		this.listBusquedaProductos = new ArrayList<Producto>();
		this.idProducto = 0L;
	}

	public void buscarProductos() {
		try {
			control();
			if (this.campoBusquedaProductos == null)
				this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
			
			// Buscamos productos en Almacen
			this.paginacionProductos = 1;
			this.listBusquedaProductos = this.ifzProducto.findLikeProperty(this.campoBusquedaProductos, this.valorBusquedaProductos, 0L, (this.almacenAdmvo ? 2 : 1), "", 0);
			if (this.listBusquedaProductos == null || this.listBusquedaProductos.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
			
			// Ordenamos por descripcion
			Collections.sort(this.listBusquedaProductos, new Comparator<Producto>() {
				@Override
				public int compare(Producto o1, Producto o2) {
					return o1.getDescripcion().compareTo(o2.getDescripcion());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Productos", e);
		} 
	}
	
	public void seleccionarProducto() {
		TraspasoDetalleExt nuevo = null;
		ProductoExt producto = null;
		
		try {
			control();
			if (this.listSolicitudProductos == null)
				this.listSolicitudProductos = new ArrayList<TraspasoDetalleExt>();
			for (TraspasoDetalleExt detalle : this.listSolicitudProductos) {
				if (this.idProducto == detalle.getIdProducto().getId().longValue()) {
					control(-1, "El producto seleccionado ya existe en el listado");
					return;
				}
			}
			
			producto = this.ifzProducto.findExtById(this.idProducto);
			if (producto == null || producto.getId() == null || producto.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar el Producto seleccionado");
				return;
			}
			
			nuevo = new TraspasoDetalleExt();
			nuevo.setIdAlmacenTraspaso(this.pojoSolicitud.getId());
			nuevo.setIdProducto(producto);
			nuevo.setCantidad(1);
			nuevo.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			nuevo.setFechaCreacion(Calendar.getInstance().getTime());
			nuevo.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			nuevo.setFechaModificacion(Calendar.getInstance().getTime());
			this.listSolicitudProductos.add(nuevo);
			
			this.ifzSolicitudDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.listSolicitudProductos = this.ifzSolicitudDetalles.saveOrUpdateListExt(this.listSolicitudProductos);
		} catch (Exception e) {
			control("Ocurrio un probla al recuperar el Producto seleccionado.", e);
		}
	}

	// --------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------

	public String getIdSolictud() {
		if (this.pojoSolicitud != null && this.pojoSolicitud.getId() != null && this.pojoSolicitud.getId() > 0L)
			return this.pojoSolicitud.getId().toString();
		return "";
	}
	
	public void setIdSolictud(String value) {}
	
	public boolean getTraspasable() {
		if (this.pojoSolicitud != null && this.pojoSolicitud.getIdTraspaso() != null && this.pojoSolicitud.getIdTraspaso().getId() != null && this.pojoSolicitud.getIdTraspaso().getId() > 0L)
			return false;
		return getGenerarTraspaso();
	}
	
	public void setTraspasable(boolean value) {}

	public boolean getCancelable() {
		if (! "ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()))
			return false;
		if (this.pojoSolicitud != null)
			return this.pojoSolicitud.getEstatus() == 0 ? true : validaRequest("CANCEL");
		return false;
	}
	
	public void setCancelable(boolean value) {} 

	public boolean getDescargable() {
		if (! "ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()))
			return false;
		return this.isDebug;
	}
	
	public void setDescargable(boolean value) {} 
	
	public String getTraspaso() {
		if (this.pojoSolicitud != null && this.pojoSolicitud.getIdTraspaso() != null && this.pojoSolicitud.getIdTraspaso().getId() != null && this.pojoSolicitud.getIdTraspaso().getId() > 0L)
			return this.pojoSolicitud.getIdTraspaso().getId().toString();
		return "";
	}
	
	public void setTraspaso(String value) {}
	
	public boolean getGenerarTraspaso() {
		if (this.pojoSolicitud != null && this.pojoSolicitud.getCompleto() == 0) {
			if ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()))
				return this.pojoSolicitud.getEstatus() == 0 ? true : validaRequest("CANCEL");

			if (this.listPuestosValidos == null || this.listPuestosValidos.isEmpty())
				return false;
			if (this.pojoEmpleadoUsuario == null || this.pojoEmpleadoUsuario.getPuestoCategoria() == null || this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto() == null)
				return false;
			if (this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() == null || this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() <= 0L)
				return false;
			return (this.listPuestosValidos.contains(this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId()));
		}
		
		return false;
	}
	
	public void setGenerarTraspaso(boolean value) {}
	
	public boolean isEsAdministrativo() {
		return esAdministrativo;
	}
	
	public void setEsAdministrativo(boolean esAdministrativo) {
		this.esAdministrativo = esAdministrativo;
	}
	
	public boolean isOperacionCancelada() {
		return operacionCancelada;
	}
	
	public void setOperacionCancelada(boolean operacionCancelada) {
		this.operacionCancelada = operacionCancelada;
	}
	
	public int getTipoMensaje() {
		return tipoMensaje;
	}
	
	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getMensajeDetalles() {
		return mensajeDetalles;
	}
	
	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}
	
	public List<SelectItem> getResultsBusquedaTipos() {
		return resultsBusquedaTipos;
	}
	
	public void setResultsBusquedaTipos(List<SelectItem> resultsBusquedaTipos) {
		this.resultsBusquedaTipos = resultsBusquedaTipos;
	}
	
	public String getResultsBusquedaCampo() {
		return resultsBusquedaCampo;
	}
	
	public void setResultsBusquedaCampo(String resultsBusquedaCampo) {
		this.resultsBusquedaCampo = resultsBusquedaCampo;
	}
	
	public String getResultsBusquedaValor() {
		return resultsBusquedaValor;
	}
	
	public void setResultsBusquedaValor(String resultsBusquedaValor) {
		this.resultsBusquedaValor = resultsBusquedaValor;
	}
	
	public int getResultsPaginas() {
		return resultsPaginas;
	}
	
	public void setResultsPaginas(int resultsPaginas) {
		this.resultsPaginas = resultsPaginas;
	}
	
	public List<SelectItem> getListAlmacenesItems() {
		return listAlmacenesItems;
	}
	
	public void setListAlmacenesItems(List<SelectItem> listAlmacenesItems) {
		this.listAlmacenesItems = listAlmacenesItems;
	}
	
	public long getIdAlmacen() {
		return idAlmacen;
	}
	
	public void setIdAlmacen(long idAlmacen) {
		this.idAlmacen = idAlmacen;
		validarAlmacenAdministrativo();
	}
	
	public boolean isUsuarioValido() {
		return usuarioValido;
	}
	
	public void setUsuarioValido(boolean usuarioValido) {
		this.usuarioValido = usuarioValido;
	}
	
	public boolean isBuscarAdministrativos() {
		return buscarAdministrativos;
	}
	
	public void setBuscarAdministrativos(boolean buscarAdministrativos) {
		this.buscarAdministrativos = buscarAdministrativos;
	}
	
	public AlmacenTraspaso getPojoSolicitud() {
		return pojoSolicitud;
	}
	
	public void setPojoSolicitud(AlmacenTraspaso pojoSolicitud) {
		this.pojoSolicitud = pojoSolicitud;
	}
	
	public long getIdSolicitud() {
		return idSolicitud;
	}
	
	public void setIdSolicitud(long idSolicitud) {
		this.idSolicitud = idSolicitud;
	}
	
	public List<AlmacenTraspaso> getListSolicitudes() {
		return listSolicitudes;
	}
	
	public void setListSolicitudes(List<AlmacenTraspaso> listSolicitudes) {
		this.listSolicitudes = listSolicitudes;
	}
	
	public boolean isAlmacenLock() {
		return almacenLock;
	}
	
	public void setAlmacenLock(boolean almacenLock) {
		this.almacenLock = almacenLock;
	}
	
	public List<TraspasoDetalleExt> getListSolicitudProductos() {
		return listSolicitudProductos;
	}
	
	public void setListSolicitudProductos(List<TraspasoDetalleExt> listSolicitudProductos) {
		this.listSolicitudProductos = listSolicitudProductos;
	}
	
	public int getPaginacionSolicitudDetalles() {
		return paginacionSolicitudDetalles;
	}
	
	public void setPaginacionSolicitudDetalles(int paginacionSolicitudDetalles) {
		this.paginacionSolicitudDetalles = paginacionSolicitudDetalles;
	}

	public long getIdExplosionInsumos() {
		return idExplosionInsumos;
	}

	public void setIdExplosionInsumos(long idExplosionInsumos) {
		this.idExplosionInsumos = idExplosionInsumos;
	}

	public long getIdMovimientoSalida() {
		return idMovimientoSalida;
	}

	public void setIdMovimientoSalida(long idMovimientoSalida) {
		this.idMovimientoSalida = idMovimientoSalida;
	}
	
	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public boolean getDatosExtras() {
		return (this.pojoSolicitud != null && this.pojoSolicitud.getEstatus() == 0 && "ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()));
	}
	
	public void setDatosExtras(boolean value) {}
	
	public boolean isPuedeEditarProducto() {
		return (this.pojoSolicitud != null && this.pojoSolicitud.getEstatus() == 0 && "ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()));
	}

	public void setPuedeEditarProducto(boolean value) {}

	public boolean isPuedeEliminarProducto() {
		return (this.listSolicitudProductos != null && this.listSolicitudProductos.size() > 1);
	}

	public void setPuedeEliminarProducto(boolean value) {}

	public boolean getValorEditarProducto() {
		return valorEditarProducto;
	}

	public void setValorEditarProducto(boolean valorEditarProducto) {
		this.valorEditarProducto = valorEditarProducto;
	}

	public boolean getAfectarAlmacen() {
		return afectarAlmacen;
	}

	public void setAfectarAlmacen(boolean afectarAlmacen) {
		this.afectarAlmacen = afectarAlmacen;
	}

	public TraspasoDetalleExt getPojoDetalleQuitar() {
		return pojoDetalleQuitar;
	}

	public void setPojoDetalleQuitar(TraspasoDetalleExt pojoDetalleQuitar) {
		this.pojoDetalleQuitar = pojoDetalleQuitar;
	}

	public List<Producto> getListBusquedaProductos() {
		return listBusquedaProductos;
	}

	public void setListBusquedaProductos(List<Producto> listBusquedaProductos) {
		this.listBusquedaProductos = listBusquedaProductos;
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

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	public int getPaginacionProductos() {
		return paginacionProductos;
	}

	public void setPaginacionProductos(int paginacionProductos) {
		this.paginacionProductos = paginacionProductos;
	}

	// ----------------------------------------------------------------------
	// PERMISOS 
	// ----------------------------------------------------------------------

	public boolean isPermisoConsultar() { return this.permisos.getConsultar(); }

	public void setPermisoConsultar(boolean value) {}
    
	public boolean isPermisoAgregar() { return this.permisos.getEditar(); }

	public void setPermisoAgregar(boolean value) {}

	public boolean isPermisoEditar() { return this.permisos.getEditar(); }

	public void setPermisoEditar(boolean value) {}

	public boolean isPermisoBorrar() { return this.permisos.getBorrar(); }

	public void setPermisoBorrar(boolean value) {}

	public boolean isPermisoImprimir() { return this.permisos.getConsultar(); }

	public void setPermisoImprimir(boolean value) {}

	public boolean isPermisoEscritura() { return this.permisos.getEditar(); }

	public void setPermisoEscritura(boolean value) { }
}
