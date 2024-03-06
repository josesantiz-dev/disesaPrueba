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

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;
import net.giro.inventarios.beans.Almacen;
import net.giro.inventarios.logica.AlmacenRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="solbodegaAction")
public class SolicitudesBodegaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SolicitudesBodegaAction.class);
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private InitialContext ctx;
	// Interfaces
	//private AlmacenProductoRem ifzAlmacenProducto;
	private AlmacenRem ifzAlmacen;
	private ObraRem ifzObras;
	private EmpleadoRem ifzEmpleado;
	private ReportesRem ifzReportes;
	// variables
    private String usuario;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private List<Obra> listResults;
	private Obra pojoObra;
	private boolean esAdministrativo;
	private boolean buscarAdministrativos;
	// Busqueda principal
	private List<SelectItem> resultsBusquedaTipos;
	private String resultsBusquedaCampo;
	private String resultsBusquedaValor;
	private int resultsPaginas;
	private boolean resultsBusquedaAvanzada;
	// Almacen
	private List<Almacen> listAlmacenes;
	private List<SelectItem> listAlmacenesItems;
	private long idAlmacen;
	// EMPLEADO-USUARIO
	private boolean usuarioValido;
	private EmpleadoExt pojoEmpleadoUsuario;
	private long idSucursalBase;
	private long idBodegaBase;
	private long idAlmacenBase;
	
	
	public SolicitudesBodegaAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.esAdministrativo = ((valPerfil != null && "S".equals(valPerfil.trim())) ? true : false);
			
			// Recuperamos puesto requerido (No obligatorio)
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());

			this.ctx = new InitialContext();
			//this.ifzAlmacenProducto = (AlmacenProductoRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenProductoFac!net.giro.inventarios.logica.AlmacenProductoRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzAlmacen = (AlmacenRem) this.ctx.lookup("ejb:/Logica_Inventarios//AlmacenFac!net.giro.inventarios.logica.AlmacenRem");
			this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			this.ifzReportes = (ReportesRem) this.ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");

			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzAlmacen.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzReportes.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Busqueda principal
			this.resultsBusquedaTipos = new ArrayList<SelectItem>();
			this.resultsBusquedaTipos.add(new SelectItem("*","Coincidencia"));
			this.resultsBusquedaTipos.add(new SelectItem("nombre","Nombre"));
			this.resultsBusquedaTipos.add(new SelectItem("nombreCliente","Cliente"));
			this.resultsBusquedaTipos.add(new SelectItem("nombreContrato","Contrato"));
			this.resultsBusquedaTipos.add(new SelectItem("id","ID"));
			this.resultsBusquedaCampo = this.resultsBusquedaTipos.get(0).getValue().toString();
			this.resultsBusquedaValor = "";
			this.resultsBusquedaAvanzada = false;
			this.resultsPaginas = 1;
			
			cargarAlmacenes();

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
			if (this.listResults != null)
				this.listResults.clear();

			log.info("Buscar por " + this.resultsBusquedaCampo + ": " + this.resultsBusquedaValor);
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			if ("*".equals(this.resultsBusquedaCampo))
				this.listResults = this.ifzObras.findLike(this.resultsBusquedaValor, this.buscarAdministrativos, "nombre", 0); 
			else
				this.listResults = this.ifzObras.findLikeProperty(this.resultsBusquedaCampo, this.resultsBusquedaValor, this.buscarAdministrativos);
			if (this.listResults == null || this.listResults.isEmpty()) {
				control(true, 2, "Busqueda sin resultados");
				return;
			}
		} catch (Exception e) {
    		control(true, 1, "Ocurrio un problema al consultar las Obras", e);
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
			log.info("Imprimiendo Solicitud a Bodega ... ");
			formatter = new SimpleDateFormat("yyMMddHHss");
			
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0) {
				control(true, -1, "No puedo determinar la Obra para la Solicitud a Bodega");
				return;
			}
			
			// Parametros del reporte
			log.info("Generando parametros ... ");
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("idObra", this.pojoObra.getId());
			//paramsReporte.put("idFamilia", 0L);

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.solicitudBodega"));
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
				log.error("ERROR INTERNO - " + respuesta.getErrores().getCodigoError() + respuesta.getErrores().getDescError());
				control(true, -1, "Ocurrio un problema al intentar imprimir la Solicitud a Bodega");
				return;
			} 

			log.info("Obteniendo reporte ... ");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "RSB-" + this.pojoObra.getId()+ "-" + formatter.format(Calendar.getInstance().getTime());
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				control(true, -1, "Ocurrio un problema al intentar imprimir la Solicitud a Bodega");
				return;
			}

			log.info("Lanzando reporte ... ");
			this.httpSession.setAttribute("formato", formatoDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			log.info("Solicitud a Bodega exportada. Proceso finalizado");
		} catch (Exception e) {
			control(true, -1, "Ocurrio un problema al intentar imprimir la Solicitud a Bodega", e);
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
	
	public void toggleResultsBusquedaAvanzada() {
		this.resultsBusquedaAvanzada = ! this.resultsBusquedaAvanzada;
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
			log.error("\nSOLICITUD A BODEGA :: " + this.usuario + " :: " + codigo + "\n" + mensaje + "\n", throwable);
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
	
	// --------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------

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

	public List<Obra> getListResults() {
		return listResults;
	}

	public void setListResults(List<Obra> listResults) {
		this.listResults = listResults;
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

	public boolean isResultsBusquedaAvanzada() {
		return resultsBusquedaAvanzada;
	}

	public void setResultsBusquedaAvanzada(boolean resultsBusquedaAvanzada) {
		this.resultsBusquedaAvanzada = resultsBusquedaAvanzada;
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
}
