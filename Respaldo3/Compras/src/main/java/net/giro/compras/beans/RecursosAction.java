package net.giro.compras.beans;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.giro.cargas.documentos.beans.RecursosDocumentos;
import net.giro.cargas.documentos.logica.RecursosDocumentosRem;
import net.giro.cargas.documentos.util.TipoRecursosDocumentos;
import net.giro.navegador.LoginManager;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="recursosAction")
public class RecursosAction implements Serializable {
	private static Logger log = Logger.getLogger(RecursosAction.class);
	private static final long serialVersionUID = 1L;
	private LoginManager loginManager;
	private HttpSession httpSession;
	// ----------------------------------------------------------------
	private List<String> usuariosValidos;
	private boolean accesoPermitido;
	// ----------------------------------------------------------------
	// Recursos Documentos 
	private RecursosDocumentosRem ifzRecursos;
	private List<RecursosDocumentos> listRecursos;
	private List<SelectItem> listRecursosItems;
	private RecursosDocumentos pojoRecurso;
	// ----------------------------------------------------------------
	private TipoRecursosDocumentos tipoRecurso;
	private String recursosPath;
	private long idRecurso;
	// ----------------------------------------------------------------
	// Control
    private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// ----------------------------------------------------------------
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public RecursosAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		HttpServletRequest request = null;
		String[] splitted = null;
		String valor = "";
		// ---------------------------------------------
		PropertyResourceBundle properties = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			request = (HttpServletRequest) fc.getExternalContext().getRequest();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			valor = request.getParameter("TR");
			valor = ((valor == null || "".equals(valor.trim())) ? "0" : valor);
			this.tipoRecurso = TipoRecursosDocumentos.fromOrdinal(Integer.parseInt(valor));

			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;

			// Usuarios permitidos
			this.usuariosValidos = new ArrayList<String>();
			this.usuariosValidos.add("ADMINISTRADOR");
			this.usuariosValidos.add("JAVIITR");
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			properties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			if (properties.containsKey("recursos.user")) {
				valor = properties.getString("recursos.user").trim();
				if (valor != null && ! "".equals(valor.trim())) {
					if (! valor.contains(",")) {
						this.usuariosValidos.add(valor.trim());
						valor = "";
					}
					
					if (! "".equals(valor.trim())) {
						splitted = valor.split(",");
						for (String val : splitted)
							this.usuariosValidos.add(val.trim());
						valor = "";
					}
				}
			}
			
			if (this.usuariosValidos.contains(this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()))
				this.accesoPermitido = true;
			this.accesoPermitido = true; // Permitido a todo el usuario con acceso al modulo
			
			// Parametros de RECURSO
			if (properties.containsKey("recursos.ruta"))
				this.recursosPath = properties.getString("recursos.ruta").trim();
			this.recursosPath = ((this.recursosPath == null || "".equals(this.recursosPath.trim())) ? "/var/cargas/recursos/" : this.recursosPath);
			
			// Conexion con modulos
			ctx = new InitialContext();
			this.ifzRecursos = (RecursosDocumentosRem) ctx.lookup("ejb:/Logica_Cargas_Documentos//RecursosDocumentosFac!net.giro.cargas.documentos.logica.RecursosDocumentosRem");
			
			// Filtramos reportes
			cargarRecursos();
		} catch (Exception e) {
			log.error("Error en Compras.ReportesAction", e);
		}
	}
	
	public void cargarRecursos() {
		try {
			this.listRecursosItems = new ArrayList<SelectItem>();
			this.ifzRecursos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listRecursos = this.ifzRecursos.findByTipoAplicacion(this.tipoRecurso, this.loginManager.getInfoSesion().getAcceso().getAplicacion().getId());
			if (this.listRecursos == null || this.listRecursos.isEmpty()) {
				control("No existe ningun Proceso/Manual para este modulo");
				return;
			}
			
			for (RecursosDocumentos item : this.listRecursos)
				this.listRecursosItems.add(new SelectItem(item.getId(), item.getNombre(), item.getDescripcion()));
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar los Manuales para el Modulo", e);
		}
	}
	
	public void descargar() {
		byte[] fileContenido = null;
		String fileExtension = "";
		String fileName = "";
		
		try {
			control();
			this.pojoRecurso = this.ifzRecursos.findById(this.idRecurso);
			if (this.pojoRecurso == null || this.pojoRecurso.getId() == null || this.pojoRecurso.getId() <= 0L) {
				control("Ocurrio un problema al recuperar el Recurso solicitado");
				return;
			}
			
			fileName = this.pojoRecurso.getNombre() + "." + this.pojoRecurso.getExtension();
			fileExtension = this.pojoRecurso.getExtension();
			fileContenido = this.ifzRecursos.exportar(this.pojoRecurso, this.recursosPath);
			if (fileContenido == null || fileContenido.length <= 0) {
				control("No se encontro el archivo en el servidor");
				return;
			}

			// Exponiendo manual
			log.info("Exponiendo recurso ... ");
			this.httpSession.setAttribute("contenido", fileContenido);
			this.httpSession.setAttribute("nombreDocumento", fileName);
			this.httpSession.setAttribute("formato", fileExtension);
			log.info("Proceso terminado");
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el recurso desde el Servidor", e);
		} finally {
			this.idRecurso = 0L;
		}
	}

	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(String mensaje) {
		control(true, 1, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, 1, mensaje, throwable);
	}
	
	private void control(boolean incompleta, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacionCancelada = incompleta;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "Ocurrio un problema no controlado al realizar la operacion");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	// ---------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ---------------------------------------------------------------------------------------------------------
	
	public int getTipoRecurso() {
		return (this.tipoRecurso != null ? this.tipoRecurso.ordinal() : 0);
	}
	
	public void setTipoRecurso(int tipo) {}
	
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

	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean debugging) {
		this.isDebug = debugging;
	}

	public boolean getAccesoPermitido() {
		return accesoPermitido;
	}
	
	public void setAccesoPermitido(boolean accesoPermitido) {
		this.accesoPermitido = accesoPermitido;
	}

	public List<SelectItem> getListRecursosItems() {
		return listRecursosItems;
	}

	public void setListRecursosItems(List<SelectItem> listRecursosItems) {
		this.listRecursosItems = listRecursosItems;
	}

	public long getIdRecurso() {
		return idRecurso;
	}

	public void setIdRecurso(long idRecurso) {
		this.idRecurso = idRecurso;
	}
}
