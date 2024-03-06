package net.giro.inventarios;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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

import net.giro.navegador.LoginManager;
import net.giro.navegador.comun.Permiso;
import net.giro.plataforma.impresion.FtpRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="manAction")
public class ManualesAction implements Serializable {
	private static Logger log = Logger.getLogger(ManualesAction.class);
	private static final long serialVersionUID = 1L;
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private Permiso permisos;
	private HttpSession httpSession;
	private String usuario;
    private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private List<String> usuariosValidos;
	private List<SelectItem> listManualesItems;
	private String manualSeleccionado;
	private boolean accesoPermitido;
	private String ftpDigitalizacionHost;
	private String ftpDigitalizacionPort;
	private String ftpDigitalizacionUser;
	private String ftpDigitalizacionPass;
	private String ftpDigitalizacionRuta;
	// Interfaces
	private FtpRem ifzFtp;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	/*// PERMISOS
	private boolean permisoConsultar;
	private boolean permisoAgregar;
	private boolean permisoEditar;
	private boolean permisoBorrar;
	private boolean permisoImprimir;*/
	
	public ManualesAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		String valor = "";
		String[] splitted = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);

			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

			this.usuariosValidos = new ArrayList<String>();
			this.usuariosValidos.add("ADMINISTRADOR");
			this.usuariosValidos.add("JAVIITR");
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{manuales}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			valor = this.entornoProperties.getString("usuarios").trim();
			if (valor != null && ! "".equals(valor.trim())) {
				splitted = valor.split(",");
				for (String val : splitted)
					this.usuariosValidos.add(val.trim());
				valor = "";
			}
			
			if (this.usuariosValidos.contains(this.usuario))
				this.accesoPermitido = true;
			this.accesoPermitido = true; // Permitido a todo el usuario con acceso al modulo
			
			// Variables para la subida de archivos
			this.ftpDigitalizacionHost = this.entornoProperties.getString("ftp.digitalizacion.host");
			this.ftpDigitalizacionPort = this.entornoProperties.getString("ftp.digitalizacion.port");
			this.ftpDigitalizacionUser = this.entornoProperties.getString("ftp.digitalizacion.user");
			this.ftpDigitalizacionPass = this.entornoProperties.getString("ftp.digitalizacion.password");
			this.ftpDigitalizacionRuta = this.entornoProperties.getString("ftp.digitalizacion.ruta");

			// Permisos
			valor = (this.entornoProperties.containsKey("MANUALES") ? this.entornoProperties.getString("MANUALES") : "MANUALES");
			valor = (valor != null && ! "".equals(valor.trim())) ? valor.trim() : "0";
			this.permisos = this.loginManager.getPermisos(this.loginManager.getIdAplicacion(), Long.parseLong(valor));
			
			// Conexion con modulos
			ctx = new InitialContext();
			this.ifzFtp = (FtpRem) ctx.lookup("ejb:/Logica_Publico//FtpFac!net.giro.plataforma.impresion.FtpRem");
			
			// Filtramos reportes
			cargarManuales();
		} catch (Exception e) {
			log.error("Error en Compras.ReportesAction", e);
		}
	}
	
	private void cargarManuales() {
		String[] splitted = null;
		String[] aux = null;
		String valor = null;
		
		try {
			if (this.listManualesItems == null)
				this.listManualesItems = new ArrayList<SelectItem>();
			this.listManualesItems.clear();
			
			valor = this.entornoProperties.getString("files").trim();
			if (valor != null && ! "".equals(valor.trim())) {
				splitted = valor.split(",");
				for (String val : splitted) {
					if (val.trim().contains("|")) {
						aux = val.trim().split("\\|");
						this.listManualesItems.add(new SelectItem(aux[0].trim(), aux[1].trim()));
					} else {
						this.listManualesItems.add(new SelectItem(val.trim(), val.trim().replace("_", " ")));
					}
				}
			}
			
			cargarProcesos();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar los Manuales para el Modulo", e);
		}
	}

	private void cargarProcesos() {
		String[] splitted = null;
		String[] aux = null;
		String valor = null;
		String extraPath = "";
		
		try {
			if (this.listManualesItems == null)
				this.listManualesItems = new ArrayList<SelectItem>();

			valor = this.entornoProperties.getString("procesos_gp").trim();
			extraPath = (this.entornoProperties.getString("procesos_gp_path") != null && ! "".equals(this.entornoProperties.getString("procesos_gp_path")) ? this.entornoProperties.getString("procesos_gp_path").trim() : "");
			if (valor != null && ! "".equals(valor.trim())) {
				splitted = valor.split(",");
				Arrays.sort(splitted);
				for (String val : splitted) {
					if (val.trim().contains("|")) {
						aux = val.trim().split("\\|");
						this.listManualesItems.add(new SelectItem(aux[0].trim(), aux[1].trim(), extraPath));
					} else {
						this.listManualesItems.add(new SelectItem(val.trim(), val.trim().replace("_", " "), extraPath));
					}
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar los Manuales para el Modulo", e);
		}
	}
	
	public void verManual() {
		byte[] fileSrc = null;
		String manual = "";
		String extraPath = "";
		String filename = "";
		
		try {
			control();
			// Validamos permiso de Lectura/Consulta
			if (! this.permisos.getConsultar()) {
				control("No tiene permitido consultar informacion");
				return;
			}
			
			if (this.manualSeleccionado == null || "".equals(this.manualSeleccionado)) {
				control("No selecciono ningun Manual");
				return;
			}
			
			// Recuperado nombre de MANUAL
			log.info("Recuperado nombre de MANUAL ... ");
			manual = this.manualSeleccionado + ".pdf";
			
			// Comprobamos path extra
			for (SelectItem item : this.listManualesItems) {
				if (this.manualSeleccionado.equals(item.getValue().toString())) {
					extraPath = item.getDescription();
					break;
				}
			}
			
			if (extraPath == null || "".equals(extraPath.trim()))
				extraPath = "";
			
			// Estableciendo parametros de conexion
			log.info("Estableciendo parametros de conexion ... ");
			this.ifzFtp.setInfo(this.ftpDigitalizacionHost, this.ftpDigitalizacionUser, this.ftpDigitalizacionPass, this.ftpDigitalizacionPort);
			// Recuperando manual
			filename = this.ftpDigitalizacionRuta + extraPath + manual;
			log.info("Recuperando manual ... " + filename);
			fileSrc = this.ifzFtp.get(filename);
			if (fileSrc == null || fileSrc.length <= 0) {
				control("No se encontro el archivo en el servidor");
				return;
			}

			// Exponiendo manual
			log.info("Exponiendo manual ... ");
			this.httpSession.setAttribute("contenido", fileSrc);
			this.httpSession.setAttribute("nombreDocumento", manual);
			this.httpSession.setAttribute("formato", "pdf");
			log.info("Proceso terminado");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el archivo desde el Servidor", e);
		} finally {
			this.manualSeleccionado = "";
		}
	}

	private void control() {
		control(false, 0, "", null);
	}

	private void control(String mensaje) {
		control(true, 1, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, 1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";
		
		codigo = "Codigo: " + formatter.format(Calendar.getInstance().getTime());
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "ERROR";

		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
			log.error(this.usuario + " :: " + codigo + " :: " + mensaje, throwable);
		}
	}

	// ---------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ---------------------------------------------------------------------------------------------------------
	
	public boolean getOperacion() {
		return operacionCancelada;
	}

	public void setOperacion(boolean operacion) {
		this.operacionCancelada = operacion;
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

	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean debugging) {
		this.isDebug = debugging;
	}

	public List<SelectItem> getListManualesItems() {
		return listManualesItems;
	}
	
	public void setListManualesItems(List<SelectItem> listManualesItems) {
		this.listManualesItems = listManualesItems;
	}
	
	public String getManualSeleccionado() {
		return manualSeleccionado;
	}
	
	public void setManualSeleccionado(String manualSeleccionado) {
		this.manualSeleccionado = manualSeleccionado;
	}
	
	public boolean getAccesoPermitido() {
		return accesoPermitido;
	}
	
	public void setAccesoPermitido(boolean accesoPermitido) {
		this.accesoPermitido = accesoPermitido;
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

	public boolean isPermisoEscritura() { return this.permisos.getConsultar(); }

	public void setPermisoEscritura(boolean value) { }
}
