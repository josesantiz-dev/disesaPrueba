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
import net.giro.inventarios.beans.AlmacenProducto;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.beans.DesgloceSBO;
import net.giro.inventarios.beans.Producto;
import net.giro.inventarios.beans.ProductoHistorial;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.inventarios.logica.ProductoRem;
import net.giro.navegador.LoginManager;
import net.giro.navegador.comun.Permiso;
import net.giro.plataforma.beans.ConGrupoValores;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.plataforma.logica.ConGrupoValoresRem;
import net.giro.plataforma.logica.ConValoresRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="invAction")
public class InventarioAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(InventarioAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private Permiso permisos;
	private HttpSession httpSession;
	// Interfaces
	private AlmacenProductoRem ifzAlmacenProducto;
	private AlmacenRem ifzAlmacen;
	private EmpleadoRem ifzEmpleado;
	private ConGrupoValoresRem ifzGpoValores;
	private ConValoresRem ifzConValores;
	private ReportesRem ifzReportes;
	// variables
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private List<AlmacenProductoExt> listInventario;
	private AlmacenProductoExt pojoAlmacenProducto;
	private String valorPrevio;
	// Busqueda principal
	private List<SelectItem> inventarioBusquedaTipos;
	private String inventarioBusquedaCampo;
	private String inventarioBusquedaValor;
	private int paginacionPrincipal;
	private boolean productosSinExisencia;
	// Almacen
	private List<Almacen> listAlmacenes;
	private List<SelectItem> listAlmacenesItems;
	private long idAlmacen;
	// ----------------------------------------
	private Almacen almacenTrabajo;
	// Familia
	private ConGrupoValores pojoGpoMaestro;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	private long idFamilia;
	//Busqueda Productos
	// ----------------------------------------
	private ProductoRem ifzProducto;
	private List<Producto> listBusquedaProductos; 
	private List<SelectItem> tiposBusquedaProductos;	
	private String campoBusquedaProductos;
	private String valorBusquedaProductos;
	private Producto pojoProducto;
	private int paginacionProductos;
	// Activacion Existencia
	private boolean valorCantidadInventario;
	// Historial
	private List<ProductoHistorial> historial;
	private Date fechaDesde;
	private Date fechaHasta;
	private int paginacionHistorial;
	// Desgloce de SBO
	private List<DesgloceSBO> desgloceSBO;
	private int paginacionDesgloceSBO;
	// EMPLEADO-USUARIO
	private boolean usuarioValido;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalBase;
	private long idBodegaBase;
	private long idAlmacenBase;
	/*// PERMISOS
	private boolean permisoConsultar;
	private boolean permisoAgregar;
	private boolean permisoEditar;
	private boolean permisoBorrar;
	private boolean permisoImprimir;*/
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public InventarioAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String resValue = "";
		
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
			
			// Recuperamos puesto requerido (No obligatorio)
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());

			// Permisos
			resValue = (this.entornoProperties.containsKey("INVENTARIO") ? this.entornoProperties.getString("INVENTARIO") : "INVENTARIO");
			resValue = (resValue != null && ! "".equals(resValue.trim())) ? resValue.trim() : "0";
			this.permisos = this.loginManager.getPermisos(this.loginManager.getIdAplicacion(), Long.parseLong(resValue));
			
			ctx = new InitialContext();
			this.ifzAlmacenProducto = (AlmacenProductoRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			this.ifzProducto = (ProductoRem) ctx.lookup("ejb:/Logica_Inventarios//ProductoFac!net.giro.inventarios.logica.ProductoRem");
			this.ifzAlmacen = (AlmacenRem) ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzEmpleado = (EmpleadoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzGpoValores = (ConGrupoValoresRem) ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");

			this.ifzAlmacenProducto.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzGpoValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzConValores.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			
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
			
			// Busqueda principal
			this.inventarioBusquedaTipos = new ArrayList<SelectItem>();
			this.inventarioBusquedaTipos.add(new SelectItem("*","Coincidencia"));
			this.inventarioBusquedaTipos.add(new SelectItem("p.descripcion","Descripcion"));
			this.inventarioBusquedaTipos.add(new SelectItem("p.clave","Codigo"));
			this.inventarioBusquedaTipos.add(new SelectItem("p.id","id"));
			this.inventarioBusquedaCampo = this.inventarioBusquedaTipos.get(0).getValue().toString();
			this.inventarioBusquedaValor = "";
			this.paginacionPrincipal = 1;

			// Busqueda Productos
			this.tiposBusquedaProductos = new ArrayList<SelectItem>();
			this.tiposBusquedaProductos.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaProductos.add(new SelectItem("descripcion", "Nombre"));
			this.tiposBusquedaProductos.add(new SelectItem("clave", "Clave"));
			this.tiposBusquedaProductos.add(new SelectItem("id", "ID"));
			nuevaBusquedaProductos();
			
			cargarAlmacenes();
			cargarFamilias();

			this.usuarioValido = false;
			if (comprobarUsuario())
				this.usuarioValido = true;
			this.productosSinExisencia = true;
			this.valorCantidadInventario = false;
		} catch (Exception e) {
			control("Ocurrio un problema al instanciar InventarioAction", e);
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
			
			if (this.almacenTrabajo == null || this.almacenTrabajo.getId() == null || this.almacenTrabajo.getId() <= 0L) {
				control(-1, "Debe seleccionar un Almacen/Bodega para poder consultar las Existencias");
				return;
			}

			log.info("Buscar por " + this.inventarioBusquedaCampo + ": " + this.inventarioBusquedaValor + " en " + this.almacenTrabajo.getNombre());
    		this.paginacionPrincipal = 1;
			this.listInventario = this.ifzAlmacenProducto.findLikePropertyExt(this.almacenTrabajo.getId(), this.inventarioBusquedaCampo, this.inventarioBusquedaValor, this.idFamilia, (this.almacenTrabajo.getTipo() > 2 ? 2 : 1), this.productosSinExisencia, "clave", 0);
			if (this.listInventario == null || this.listInventario.isEmpty()) 
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
    		control("Ocurrio un problema al consultar el Inventario", e);
    	} 
	}
	
	public void reestablecer() {
		if (this.listInventario != null)
			this.listInventario.clear();
		this.valorCantidadInventario = false;
	}
	
	public void ejecutarReporte() {
		SimpleDateFormat formatter = null;
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			log.info("Imprimiendo Inventario ... ");
			formatter = new SimpleDateFormat("yyMMddHH");
			nombreDocumento = "INVENTARIO-[id]-[fecha].[extension]";
			
			if (this.idAlmacen <= 0) {
				control(-1, "Debe seleccionar un Almacen de la lista");
				return;
			}
			
			// Parametros del reporte
			log.info("Inventario. Generando parametros ... ");
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idAlmacen", this.idAlmacen);
			paramsReporte.put("idFamilia", this.idFamilia);

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.inventario"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario());

			log.info("Inventario. Generando reporte ... ");
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte, 30);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO - " + respuesta.getErrores().getCodigoError() + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al intentar imprimir el Inventario");
				return;
			} 

			log.info("Inventario. Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = nombreDocumento.replace("[id]", String.format("%1$05d", this.idAlmacen).substring(3));
			nombreDocumento = nombreDocumento.replace("[fecha]", formatter.format(Calendar.getInstance().getTime()));
			nombreDocumento = nombreDocumento.replace("[extension]", formatoDocumento);
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				control(-1, "Ocurrio un problema al intentar imprimir el Inventario");
				return;
			}

			log.info("Inventario. Lanzando reporte ... ");
			this.httpSession.setAttribute("formato", formatoDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			log.info("Inventario exportado. Proceso finalizado");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir el Inventario", e);
		} 
	}

	public void guardaCantidadPrevia(AjaxBehaviorEvent event) {
		String value = "";
		int index = -1;

		try {
			control();
			value = (event.getComponent().getAttributes().get("targetIndex") != null) ? event.getComponent().getAttributes().get("targetIndex").toString() : "-1";
			index = Integer.valueOf(value);
			if (index < 0)
				return;
			this.valorPrevio = index + ":" + this.listInventario.get(index).getExistencia();
		} catch (Exception e) {
    		control("Ocurrio un problema al actualizar las cantidad en el Inventario", e);
		}
	}
	
	public void guardaCantidadProducto(AjaxBehaviorEvent event) {
		AlmacenProductoExt existencia = null;
		double cantidad = 0;
		// ----------------------------------
		String value = "";
		int index = -1;

		try {
			control();
			value = (event.getComponent().getAttributes().get("targetIndex") != null) ? event.getComponent().getAttributes().get("targetIndex").toString() : "-1";
			index = Integer.valueOf(value);
			if (index < 0)
				return;
			existencia = this.listInventario.get(index);
			if (this.valorPrevio.contains(index + ":")) {
				cantidad = Double.parseDouble(this.valorPrevio.replace(index + ":", ""));
				if (cantidad == existencia.getExistencia())
					return;
			}
			
			// Actualizo el datos en BD
			this.ifzAlmacenProducto.update(existencia);
			this.valorPrevio = "";
		} catch (Exception e) {
    		control("Ocurrio un problema al actualizar las cantidad en el Inventario", e);
		}
	}
	
	public void activacionCantidadInventario() {
		this.valorCantidadInventario = ! this.valorCantidadInventario;
	}
	
	public void historial() {
		long idProducto = 0L;
		
		try {
			control();
			this.historial = this.ifzAlmacenProducto.historial(this.almacenTrabajo.getId(), idProducto, this.fechaDesde, this.fechaHasta);
			if (this.historial == null || this.historial.isEmpty())
				control(-1, "Historial no disponible");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el Historial del Producto indicado", e);
		} finally {
			this.paginacionHistorial = 1;
		}
	}
	
	public void desgloceSBO() {
		long idProducto = 0L;
		
		try {
			control();
			this.desgloceSBO = this.ifzAlmacenProducto.desgloceSBO(this.almacenTrabajo.getId(), idProducto);
			if (this.desgloceSBO == null || this.desgloceSBO.isEmpty())
				control(-1, "Desgloce de cantidades en Solicitud a Bodega no disponible");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar generar el desgloce de cantidad en Solicitud a Bodega del Producto indicado", e);
		} finally {
			this.paginacionDesgloceSBO = 1;
		}
	}
	
	private boolean comprobarUsuario() {
		Long idEmpleado = null;
		List<Empleado> listEmpleado = null;
		List<Almacen> listAlmacenes = null;
		int cont = 0;
		
		try {
			this.idBodegaBase = 0L;
			this.idAlmacenBase = 0L;
			this.idSucursalBase = 0L;
			
			if (this.loginManager == null)
				return false;
			
			if ("ADMINISTRADOR".equals(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()))
				return true;
			
			// Determinamos EMPLEADO de manera directa o por correo electronico
			idEmpleado = this.loginManager.getUsuarioResponsabilidad().getUsuario().getIdEmpleado();
			if (idEmpleado == null || idEmpleado <= 1L) {
				listEmpleado = this.ifzEmpleado.findByProperty("email", this.loginManager.getUsuarioResponsabilidad().getUsuario().getCorreo(), false, false, "", 0);
				if (listEmpleado != null && listEmpleado.size() == 1)
					idEmpleado = listEmpleado.get(0).getId();
			}
			
			if (idEmpleado == null || idEmpleado <= 1L) {
				log.info("Usuario sin Empleado asociado");
				return false;
			}
			
			// Recuperamos empleado
			this.pojoEmpleadoUsuario = this.ifzEmpleado.findByIdExt(idEmpleado);
			if (this.pojoEmpleadoUsuario == null || this.pojoEmpleadoUsuario.getId() == null || this.pojoEmpleadoUsuario.getId() <= 0L){
				log.info("No se encontro Empleado asociado al Usuario");
				return false;
			}
			
			// Comprobamos PUESTO
			if (this.pojoEmpleadoUsuario.getPuestoCategoria() != null 
					&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto() != null 
					&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() != null 
					&& this.pojoEmpleadoUsuario.getPuestoCategoria().getIdPuesto().getId() <= 0L) {
				log.info("Empleado sin puesto asignado");
				return false;
			}
			
			// Recuperamos SUCURSAL base del Empleado
			this.idSucursalBase = this.pojoEmpleadoUsuario.getSucursal().getId();
			listAlmacenes = this.ifzAlmacen.findByProperty("idSucursal", this.idSucursalBase);
			if (listAlmacenes != null && ! listAlmacenes.isEmpty())  {
				// Determinando Bodega (Almacenes principales) en base a la Sucursal del Empleado
				cont = 0;
				for (Almacen var : listAlmacenes) {
					if (var.getTipo() != 1)
						continue;
					
					cont++;
					this.idBodegaBase = var.getId();
					if (cont > 1) {
						this.idBodegaBase = 0L;
						break;
					}
				}
				
				// Determinando Almacen (Almacenes de obra) en base a la Sucursal del Empleado
				cont = 0;
				for (Almacen var : listAlmacenes) {
					if (var.getTipo() != 2)
						continue;
					
					cont++;
					this.idAlmacenBase = var.getId();
					if (cont > 1) {
						this.idAlmacenBase = 0L;
						break;
					}
				}
				
				if (this.idBodegaBase > 0L && this.idAlmacenBase == 0L)
					this.idAlmacenBase = this.idBodegaBase;
			}
		} catch (Exception e) {
			log.error("Ocurrio un problema en Inventarios.EntradasAlmacenAction.comprobarUsuario()", e);
			return false;
		}
		
		return true;
	}
	
	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(tipoMensaje, mensaje, null);
	}

	private void control(String mensaje, Throwable throwable) {
		control(-1, mensaje, throwable);
	}
	
	private void control(int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.operacionCancelada = true;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		codigo = "Codigo: " + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = "<br><br>" + sw.toString();
		}
		
		log.error("\n\nINVENTARIOS:Inventario :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + codigo + "\n" + mensaje + "\n", throwable);
	}

	// ------------------------------------------------------------------------------------
	// ALMACENES
	// ------------------------------------------------------------------------------------
	
	private void cargarAlmacenes() {
		try {
			this.idAlmacen = 0L;
			this.listAlmacenesItems = new ArrayList<SelectItem>();
			
			this.listAlmacenes = this.ifzAlmacen.findAll();
			if (this.listAlmacenes != null && ! this.listAlmacenes.isEmpty()) {
				Collections.sort(this.listAlmacenes, new Comparator<Almacen>() {
					@Override
					public int compare(Almacen o1, Almacen o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
				});
				
				for (Almacen almacen : this.listAlmacenes) {
					this.listAlmacenesItems.add(new SelectItem(almacen.getId(), almacen.getNombre() + " (" + almacen.getIdentificador() + ") " + (this.isDebug ? almacen.getId() : "")));
					if (this.idAlmacen <= 0L && this.idBodegaBase > 0L && this.idBodegaBase == almacen.getId().longValue()) {
						this.idAlmacen = this.idBodegaBase;
						this.almacenTrabajo = almacen;
					}

					if (this.idAlmacen <= 0L && this.idAlmacenBase > 0L && this.idAlmacenBase == almacen.getId().longValue()) {
						this.idAlmacen = this.idAlmacenBase;
						this.almacenTrabajo = almacen;
					}
				}
			}
		} catch (Exception e) {
			control(-1, "Ocurrio un problema al recuperar los Almacenes", e);
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
			this.listFamilias = new ArrayList<ConValores>();
			this.listFamiliasItems = new ArrayList<SelectItem>();
			
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
			control(-1, "Ocurrio un problema al recuperar las Familias", e);
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
		this.pojoProducto = new Producto();
	}

	public void buscarProductos() {
		int tipoMaestro = 1; // MAESTRO de Insumos
		
		try {
			control();
			if (this.campoBusquedaProductos == null)
				this.campoBusquedaProductos = this.tiposBusquedaProductos.get(0).getValue().toString();
			
			tipoMaestro = 1;  // MAESTRO Operacion
			if (this.almacenTrabajo.getTipo() > 2)
				tipoMaestro = 2; // MAESTRO Administrativo
			
			// Buscamos productos en Almacen
			this.listBusquedaProductos = this.ifzProducto.findLikeProperty(this.campoBusquedaProductos, this.valorBusquedaProductos, this.idFamilia, tipoMaestro, "", 0);
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
			control("Ocurrio un problema al consultar los Productos del Almacen seleccioando", e);
		} finally {
			this.paginacionProductos = 1;
		}
	}
	
	public void seleccionarProducto() {
		AlmacenProducto existencia = null;
		
		try {
			control();
			if (this.pojoProducto == null && this.pojoProducto.getId() == null && this.pojoProducto.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar el Producto seleccionado");
				return;
			}
			
			// Comprobamos producto seleccionado
			existencia = this.ifzAlmacenProducto.findAlmacenProducto(this.almacenTrabajo.getId(), this.pojoProducto.getId());
			if (existencia != null && existencia.getId() != null && existencia.getId() > 0L) {
				control(-1, "El producto seleccionado ya existe actualmente en Inventario");
				return;
			}
			
			// Genero el nuevo inventario para el producto seleccionado
			existencia = new AlmacenProducto();
			existencia.setIdAlmacen(this.almacenTrabajo);
			existencia.setIdProducto(this.pojoProducto.getId());
			existencia.setExistencia(0);
			// Guardo inventario
			existencia.setId(this.ifzAlmacenProducto.save(existencia));
			// Agrego inventario al listado
			this.listInventario.add(0, this.ifzAlmacenProducto.findByIdExt(existencia.getId()));
		} catch (Exception e) {
			control("Ocurrio un probla al recuperar el Producto seleccionado.", e);
		}
	}

	// --------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------
	
	public String getTituloExistencia() {
		if (this.pojoAlmacenProducto != null && this.pojoAlmacenProducto.getProducto() != null)
			return this.pojoAlmacenProducto.getProducto().getClave() + " - " + this.pojoAlmacenProducto.getProducto().getDescripcion();
		return "";
	}
	
	public void setTituloExistencia(String value) {}

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

	public List<AlmacenProductoExt> getListInventario() {
		return listInventario;
	}

	public void setListInventario(List<AlmacenProductoExt> listInventario) {
		this.listInventario = listInventario;
	}

	public AlmacenProductoExt getPojoAlmacenProducto() {
		return pojoAlmacenProducto;
	}

	public void setPojoAlmacenProducto(AlmacenProductoExt pojoAlmacenProducto) {
		this.pojoAlmacenProducto = pojoAlmacenProducto;
	}

	public List<SelectItem> getInventarioBusquedaTipos() {
		return inventarioBusquedaTipos;
	}

	public void setInventarioBusquedaTipos(List<SelectItem> inventarioBusquedaTipos) {
		this.inventarioBusquedaTipos = inventarioBusquedaTipos;
	}

	public String getInventarioBusquedaCampo() {
		return inventarioBusquedaCampo;
	}

	public void setInventarioBusquedaCampo(String inventarioBusquedaCampo) {
		this.inventarioBusquedaCampo = inventarioBusquedaCampo;
	}

	public String getInventarioBusquedaValor() {
		return inventarioBusquedaValor;
	}

	public void setInventarioBusquedaValor(String inventarioBusquedaValor) {
		this.inventarioBusquedaValor = inventarioBusquedaValor;
	}

	public int getPaginacionPrincipal() {
		return paginacionPrincipal;
	}

	public void setPaginacionPrincipal(int paginacionPrincipal) {
		this.paginacionPrincipal = paginacionPrincipal;
	}

	public boolean isValorCantidadInventario() {
		return valorCantidadInventario;
	}

	public void setValorCantidadInventario(boolean valorCantidadInventario) {
		this.valorCantidadInventario = valorCantidadInventario;
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
		this.almacenTrabajo = null;
		if (idAlmacen > 0L) {
			for (Almacen almacen : this.listAlmacenes) {
				if (idAlmacen != almacen.getId().longValue()) 
					continue;
				this.almacenTrabajo = almacen;
				break;
			}
		}
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

	public boolean isUsuarioValido() {
		return usuarioValido;
	}

	public void setUsuarioValido(boolean usuarioValido) {
		this.usuarioValido = usuarioValido;
	}

	public boolean isProductosSinExisencia() {
		return productosSinExisencia;
	}

	public void setProductosSinExisencia(boolean productosSinExisencia) {
		this.productosSinExisencia = productosSinExisencia;
	}

	public boolean isDebugging() { 
		if (this.paramsRequest.containsKey("DEBUG")) 
			return true;
		return this.isDebug;
	}
	
	public void setDebugging(boolean value) { }

	public boolean getPuedeAgregarProducto() { 
		if ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) 
			return true;
		return false;
	}
	
	public void setPuedeAgregarProducto(boolean value) { }

	public boolean getInhabilitarAgregarProducto() {
		return ! (this.almacenTrabajo != null && this.almacenTrabajo.getId() != null && this.almacenTrabajo.getId() > 0L);
	}
	
	public void setInhabilitarAgregarProducto(boolean value) {}

	public boolean getPuedeActivarCantidadInventario() { 
		if ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario())) 
			return true;
		return false;
	}
	
	public void setPuedeActivarCantidadInventario(boolean value) { }
	
	public boolean getInhabilitarActivacionCantidadInventario() {
		return ! (this.almacenTrabajo != null && this.almacenTrabajo.getId() != null && this.almacenTrabajo.getId() > 0L);
	}
	
	public void setInhabilitarActivacionCantidadInventario(boolean value) {}

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

	public Producto getPojoProducto() {
		return pojoProducto;
	}

	public void setPojoProducto(Producto pojoProducto) {
		this.pojoProducto = pojoProducto;
	}

	public int getPaginacionProductos() {
		return paginacionProductos;
	}

	public void setPaginacionProductos(int paginacionProductos) {
		this.paginacionProductos = paginacionProductos;
	}

	public List<ProductoHistorial> getHistorial() {
		return historial;
	}

	public void setHistorial(List<ProductoHistorial> historial) {
		this.historial = historial;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public int getPaginacionHistorial() {
		return paginacionHistorial;
	}

	public void setPaginacionHistorial(int paginacionHistorial) {
		this.paginacionHistorial = paginacionHistorial;
	}

	public List<DesgloceSBO> getDesgloceSBO() {
		return desgloceSBO;
	}

	public void setDesgloceSBO(List<DesgloceSBO> desgloceSBO) {
		this.desgloceSBO = desgloceSBO;
	}

	public int getPaginacionDesgloceSBO() {
		return paginacionDesgloceSBO;
	}

	public void setPaginacionDesgloceSBO(int paginacionDesgloceSBO) {
		this.paginacionDesgloceSBO = paginacionDesgloceSBO;
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
