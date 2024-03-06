package net.giro.inventarios;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.beans.AlmacenProductoExt;
import net.giro.inventarios.logica.AlmacenProductoRem;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.navegador.LoginManager;
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
	private HttpSession httpSession;
	private InitialContext ctx;
	// Interfaces
	private AlmacenProductoRem ifzAlmacenProducto;
	private AlmacenRem ifzAlmacen;
	private EmpleadoRem ifzEmpleado;
	private ConGrupoValoresRem ifzGpoValores;
	private ConValoresRem ifzConValores;
	private ReportesRem ifzReportes;
	// variables
    private String usuario;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private List<AlmacenProductoExt> listInventario;
	// Busqueda principal
	private List<SelectItem> inventarioBusquedaTipos;
	private String inventarioBusquedaCampo;
	private String inventarioBusquedaValor;
	private int inventarioBusquedaPagina;
	private boolean inventarioBusquedaAvanzada;
	// Almacen
	private List<Almacen> listAlmacenes;
	private List<SelectItem> listAlmacenesItems;
	private long idAlmacen;
	// Familia
	private ConGrupoValores pojoGpoMaestro;
	private ConGrupoValores pojoGpoEspecialidades;
	private ConGrupoValores pojoGpoFamilias;
	private List<ConValores> listFamilias;
	private List<SelectItem> listFamiliasItems;
	private long idFamilia;
	// EMPLEADO-USUARIO
	private boolean usuarioValido;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalBase;
	private long idBodegaBase;
	private long idAlmacenBase;
	
	
	public InventarioAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

			// Recuperamos puesto requerido (No obligatorio)
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());

			this.ctx = new InitialContext();
			this.ifzAlmacenProducto = (AlmacenProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			this.ifzAlmacen = (AlmacenRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzGpoValores = (ConGrupoValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConGrupoValoresFac!net.giro.plataforma.logica.ConGrupoValoresRem");
			this.ifzConValores = (ConValoresRem) this.ctx.lookup("ejb:/Logica_Publico//ConValoresFac!net.giro.plataforma.logica.ConValoresRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");

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
			this.inventarioBusquedaPagina = 1;
			this.inventarioBusquedaAvanzada = false;
			
			cargarAlmacenes();
			cargarFamilias();

			this.usuarioValido = false;
			if (comprobarUsuario())
				this.usuarioValido = true;
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al instanciar InventarioAction", e);
		}
	}
	
	
	public void buscar() {
		try {
			control();
			if (this.listInventario != null)
				this.listInventario.clear();

			log.info("Buscar por " + this.inventarioBusquedaCampo + ": " + this.inventarioBusquedaValor);
			this.listInventario = this.ifzAlmacenProducto.findExtExistentes(this.idAlmacen, this.inventarioBusquedaCampo, this.inventarioBusquedaValor, this.idFamilia, 1, 1000, false);
			if (this.listInventario == null || this.listInventario.isEmpty()) {
				control(true, 2, "Busqueda sin resultados");
				return;
			}

			Collections.sort(
					this.listInventario, 
					new Comparator<AlmacenProductoExt>() {
						@Override
						public int compare(AlmacenProductoExt o1, AlmacenProductoExt o2) {
							return o1.getProducto().getClave().compareTo(o2.getProducto().getClave());
						}
					}
			);
		} catch (Exception e) {
    		control(true, -1, "Ocurrio un problema al consultar el Inventario", e);
    	}
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
			formatter = new SimpleDateFormat("yyMMddHHss");
			
			if (this.idAlmacen <= 0) {
				control(true, -1, "Debe seleccionar un Almacen de la lista");
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
			params.put("usuario", 		  this.usuario);

			log.info("Inventario. Generando reporte ... ");
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO - " + respuesta.getErrores().getCodigoError() + respuesta.getErrores().getDescError());
				control(true, -1, "Ocurrio un problema al intentar imprimir el Inventario");
				return;
			} 

			log.info("Inventario. Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "INVENTARIO-" + formatter.format(Calendar.getInstance().getTime());
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				control(true, -1, "Ocurrio un problema al intentar imprimir el Inventario");
				return;
			}

			log.info("Inventario. Lanzando reporte ... ");
			this.httpSession.setAttribute("formato", formatoDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			log.info("Inventario exportado. Proceso finalizado");
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar imprimir el Inventario", e);
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
			
			if ("ADMINISTRADOR".equals(this.usuario))
				return true;
			
			// Determinamos EMPLEADO de manera directa o por correo electronico
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
	
	public void toggleInventarioBusquedaAvanzada() {
		this.inventarioBusquedaAvanzada = ! this.inventarioBusquedaAvanzada;
	}
	
	private void control() {
		control(false, 0, "", null);
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		control(operacionCancelada, tipoMensaje, mensaje, null);
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";
		
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null || "".equals(mensaje.trim())) ? codigo : mensaje.replace("\n", "<br/>") + ("<br/>" + codigo);
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		if (this.operacionCancelada)
			log.error("\nINVENTARIO :: " + this.usuario + " :: " + codigo + "\n" + mensaje + "\n", throwable);
	}

	// ------------------------------------------------------------------------------------
	// ALMACENES
	// ------------------------------------------------------------------------------------
	
	private void cargarAlmacenes() {
		try {
			this.idAlmacen = 0L;
			if (this.listAlmacenes == null)
				this.listAlmacenes = new ArrayList<Almacen>();
			this.listAlmacenes.clear();
			
			if (this.listAlmacenesItems == null)
				this.listAlmacenesItems = new ArrayList<SelectItem>();
			this.listAlmacenesItems.clear();
			
			this.listAlmacenes = this.ifzAlmacen.findAllActivos();
			if (this.listAlmacenes != null && ! this.listAlmacenes.isEmpty()) {
				Collections.sort(this.listAlmacenes, new Comparator<Almacen>() {
					@Override
					public int compare(Almacen o1, Almacen o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
				});
				
				for (Almacen var : this.listAlmacenes) {
					this.listAlmacenesItems.add(new SelectItem(var.getId(), var.getNombre()));
					
					if (this.idAlmacen <= 0L && this.idBodegaBase > 0L && this.idBodegaBase == var.getId().longValue())
						this.idAlmacen = this.idBodegaBase;

					if (this.idAlmacen <= 0L && this.idAlmacenBase > 0L && this.idAlmacenBase == var.getId().longValue())
						this.idAlmacen = this.idAlmacenBase;
				}
			}
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al recuperar los Almacenes", e);
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
			control(true, -1, "Ocurrio un problema al recuperar las Familias", e);
		}
	}
	
	// --------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------

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

	public int getInventarioBusquedaPagina() {
		return inventarioBusquedaPagina;
	}

	public void setInventarioBusquedaPagina(int inventarioBusquedaPagina) {
		this.inventarioBusquedaPagina = inventarioBusquedaPagina;
	}

	public boolean isInventarioBusquedaAvanzada() {
		return inventarioBusquedaAvanzada;
	}

	public void setInventarioBusquedaAvanzada(boolean inventarioBusquedaAvanzada) {
		this.inventarioBusquedaAvanzada = inventarioBusquedaAvanzada;
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
}
