package cde.publico;

import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.cargas.documentos.beans.RecursosDocumentos;
import net.giro.cargas.documentos.beans.RecursosDocumentosModulos;
import net.giro.cargas.documentos.logica.RecursosDocumentosRem;
import net.giro.cargas.documentos.util.TipoRecursosDocumentos;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.logica.AdministracionRem;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.respuesta.Respuesta;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

@KeepAlive
public class RecursosDocumentosAction implements Serializable {
	private static Logger log = Logger.getLogger(RecursosDocumentosAction.class);
	private static final long serialVersionUID = 1L;
	private InitialContext ctx;
	private LoginManager loginManager;
   	private HttpSession httpSession;
	// ---------------------------------------------------------------------------------
	private RecursosDocumentosRem ifzRecursos;
	private List<RecursosDocumentos> listRecursos;
	private RecursosDocumentos pojoRecurso;
	private long idRecursoDocumento;
	private int paginacion;
	// Tipos de Recursos
	private List<SelectItem> listTiposRecursos;
	// RecursoModulos
	private List<RecursosDocumentosModulos> listRecursoModulos;
	private long idModulo;
	private int paginacionModulos;
	// modulos
	private AdministracionRem ifzAdmin;
	private List<Aplicacion> listModulos;
	private List<SelectItem> listModulosItems;
	// Carga Recursos ------------------------------------------------------------------
	private byte[] fileSource;
	private String fileName;
	private String filePath;
	// Busqueda Principal
	private List<SelectItem> tiposBusqueda;
	private String campoBusqueda;
	private String valorBusqueda;
	private boolean activarFecha;
	private Date fechaBusqueda;
	// control
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;

	public RecursosDocumentosAction() throws Exception {
		PropertyResourceBundle entornoProperties = null;;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
	        this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			if (entornoProperties.containsKey("ftp.digitalizacion.ruta.recursos") && entornoProperties.getString("ftp.digitalizacion.ruta.recursos") != null)
				this.filePath = entornoProperties.getString("ftp.digitalizacion.ruta.recursos");
			this.filePath = (this.filePath == null || "".equals(this.filePath.trim()) ? "/var/cargas/recursos/" : this.filePath);
			
			this.ctx = new InitialContext();
			this.ifzRecursos = (RecursosDocumentosRem) this.ctx.lookup("ejb:/Logica_Cargas_Documentos//RecursosDocumentosFac!net.giro.cargas.documentos.logica.RecursosDocumentosRem");
			this.ifzAdmin = (AdministracionRem) this.ctx.lookup("ejb:/Logica_Publico//AdministracionFac!net.giro.plataforma.logica.AdministracionRem");

			// Busqueda principal
			// ----------------------------------------------------------------------
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusqueda.add(new SelectItem("fechaCreacion", "Fecha"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.fechaBusqueda = Calendar.getInstance().getTime();
			this.paginacion = 1;
	   		
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
			
			this.listTiposRecursos = new ArrayList<SelectItem>();
			for (TipoRecursosDocumentos tipo : TipoRecursosDocumentos.values())
				this.listTiposRecursos.add(new SelectItem(tipo.ordinal(), tipo.name()));
		} catch (Exception e) {
			log.error("Error en Publico.TopicsAction", e);
			this.ctx = null;
		}
	}

	public void buscar() {
		try {
			control();
   			if (this.campoBusqueda == null || "".equals(this.campoBusqueda.trim()))
   				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();

			this.paginacion = 1;
			this.ifzRecursos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listRecursos = this.ifzRecursos.findLikeProperty(this.campoBusqueda, this.valorBusqueda, "fechaCreacion desc", 0);
			if (this.listRecursos == null || this.listRecursos.isEmpty())
				control(2, "La búsqueda no regresó resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Recursos", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.paginacionModulos = 1;
			this.idRecursoDocumento = 0L;
			this.pojoRecurso = new RecursosDocumentos();
			this.pojoRecurso.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			this.listRecursoModulos = new ArrayList<RecursosDocumentosModulos>();
			
			nuevaCarga();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cancelar el Evento", e);
		}
	}
	
	public void editar() {
		try {
			control();
			this.pojoRecurso = this.ifzRecursos.findById(this.idRecursoDocumento);
			if (this.pojoRecurso == null || this.pojoRecurso.getId() == null || this.pojoRecurso.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Recurso solicitado");
				return;
			}

			this.paginacionModulos = 1;
			this.listRecursoModulos = this.ifzRecursos.findAllModulos(this.idRecursoDocumento);
			cargarModulos();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Evento", e);
		}
	}
	
	public void guardar() {
		Respuesta respuesta = null;
		try {
			control();
			if (! validaciones())
				return;

			this.ifzRecursos.setInfoSesion(this.loginManager.getInfoSesion());
			if (this.pojoRecurso.getId() == null || this.pojoRecurso.getId() <= 0L) {
				respuesta = this.ifzRecursos.importar(this.pojoRecurso, this.fileSource, this.filePath);
				if (respuesta.getErrores().getCodigoError() != 0L) {
					control(-1, "Ocurrio un problema al guardar el Recurso\n" + respuesta.getErrores().getCodigoError() + " - "+ respuesta.getErrores().getDescError());
					return;
				}
				
				this.pojoRecurso = (RecursosDocumentos) respuesta.getBody().getValor("entity");
				cargarModulos();
			} else {
				this.pojoRecurso.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoRecurso.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.ifzRecursos.update(this.pojoRecurso);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al guardar el Evento", e);
		}
	}

	public void eliminar() {
		try {
			control();
			this.pojoRecurso = this.ifzRecursos.findById(this.idRecursoDocumento);
			if (this.pojoRecurso == null || this.pojoRecurso.getId() == null || this.pojoRecurso.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Recurso solicitado");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Eliminar el Recurso indicado", e);
		}
	}

	private boolean validaciones() {
		return true;
	}

	public void foolMethodReset(String origen) {
		if (origen == null || "".equals(origen))
			origen = "NONE";
		
		if (! this.isDebug)
			return;
		log.info("\n\n------------------------------------------------------------------------"
				+ "\n RESET    : " + this.fileName + " [" + origen + "]"
				+ "\n------------------------------------------------------------------------"
				+ "\n FileSize : " + this.fileSource.length
				+ "\n Data     : " + this.fileSource 
				+ "\n------------------------------------------------------------------------");
	}

	public void foolMethodUpload(String origen) {
		if (origen == null || "".equals(origen))
			origen = "NONE";
		
		if (! this.isDebug)
			return;
		log.info("\n\n------------------------------------------------------------------------"
				+ "\n UPLOAD   : " + this.fileName + " [" + origen + "]"
				+ "\n------------------------------------------------------------------------"
				+ "\n FileSize : " + this.fileSource.length
				+ "\n Data     : " + this.fileSource 
				+ "\n------------------------------------------------------------------------");
	}
	
	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		String message = "";
		
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al intentar realizar la operacion indicada";
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		
		message = this.getClass().getCanonicalName() + " :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + "\nMensaje :: " + this.tipoMensaje + " - " + mensaje;
		message = "\n" + message + "\n";
		if (! operacionCancelada) {
			log.info(message, throwable); 
			return;
		}
		
		log.error(message, throwable); 
	}

	// -------------------------------------------------------------------------------------------------------
	// CARGA/DESCARGA DE RECURSOS
	// -------------------------------------------------------------------------------------------------------
	
	public void nuevaCarga() {
		this.fileName = "";
		this.fileSource = null;
	}
	
	public void importar(UploadEvent event) {
		UploadItem item = null;
		
		try {
			control();
			item = event.getUploadItem();
			if (this.fileName != null && this.fileName.equals(item.getFileName()) && this.fileSource != null) 
				return;

			this.fileName = item.getFileName();
			this.fileSource = ((item.isTempFile()) ? Files.readAllBytes(item.getFile().toPath()) : item.getData());
			if (! this.isDebug) return;
			log.info("\n\n------------------------------------------------------------------------"
					+ "\n Archivo  : " + item.getFileName()  
					+ "\n------------------------------------------------------------------------"
					+ "\n Temp     : " + item.isTempFile() 
					+ "\n FileSize : " + item.getFileSize() 
					+ "\n File     : " + item.getFile() 
					+ "\n Data     : " + item.getData() 
					+ "\n------------------------------------------------------------------------");
		} catch (Exception e) {
			control("Ocurrio un problema al hacer la carga del Recurso", e);
		} 
	}
	
	public void asignarRecurso() {
		try {
			control();
			if (this.pojoRecurso == null) {
				control(-1, "Ocurrio un problema al intentar asignar el Documento indicado");
				return;
			}

			this.pojoRecurso.setNombre(getFileName(this.fileName));
			this.pojoRecurso.setExtension(getExtension(this.fileName));
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Evento", e);
		}
	}
	
	public void exportar() {
		byte[] fileContenido = null;
		String fileExtension = "";
		String fileName = "";
		
		try {
			control();
			this.pojoRecurso = this.ifzRecursos.findById(this.idRecursoDocumento);
			if (this.pojoRecurso == null || this.pojoRecurso.getId() == null || this.pojoRecurso.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Recurso solicitado");
				return;
			}
			
			fileName = this.pojoRecurso.getNombre() + "." + this.pojoRecurso.getExtension();
			fileExtension = this.pojoRecurso.getExtension();
			fileContenido = this.ifzRecursos.exportar(this.pojoRecurso, this.filePath);
			if (fileContenido == null || fileContenido.length <= 0) {
				control(21, "No se encontro el archivo en el servidor");
				return;
			}

			// Ponemos los datos en session
			this.httpSession.setAttribute("nombreDocumento", fileName);
			this.httpSession.setAttribute("formato", fileExtension);
			this.httpSession.setAttribute("contenido", fileContenido);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cancelar el Evento", e);
		}
	}

	private String getFileName(String value) {
		List<String> values = null;
		String fileName = "";
		
		values = getValues(value);
		values.remove(values.size() - 1);
		for (String val : values)
			fileName += (! "".equals(fileName) ? "." : "") + val;
		
		return fileName;
	}
	
	private String getExtension(String value) {
		List<String> values = null;
		String extension = "";
		
		values = getValues(value);
		extension = values.get(values.size() - 1);
		
		return extension;
	}
	
	private List<String> getValues(String value) {
		List<String> values = null;
		String[] splitted = null;
		
		values = new ArrayList<String>();
		splitted = value.trim().split("\\.");
		for (String val : splitted)
			values.add(val);
		
		return values;
	}
	
	// -------------------------------------------------------------------------------------------------------
	// MODULOS
	// -------------------------------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	private void cargarModulos() {
		Respuesta respuesta = null;
		
		try {
			control();
			this.ifzAdmin.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzAdmin.buscarAplicacion("aplicacion", "");
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "");
				return;
			}
			
			this.listModulosItems = new ArrayList<SelectItem>();
			this.listModulos = (List<Aplicacion>) respuesta.getBody().getValor("listAplicaciones");
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, "Ocurrio un problema al consultar los Modulos");
				return;
			}
			
			for (Aplicacion app : this.listModulos) {
				if (app.getEstatus() == 1)
					continue;
				if (app.getListable() == 0)
					continue;
				this.listModulosItems.add(new SelectItem(app.getId(), app.getAplicacion()));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cancelar el Evento", e);
		}
	}
	
	public void agregar() {
		RecursosDocumentosModulos pojoRecursoModulo = null;
		Aplicacion modulo = null;
		
		try {
			control();
			if (this.listRecursoModulos == null)
				this.listRecursoModulos = new ArrayList<RecursosDocumentosModulos>();
			
			// Comprobamos si el modulo ya fue agregado previamente
			for (RecursosDocumentosModulos item : this.listRecursoModulos) {
				if (this.idModulo == item.getIdModulo()) {
					control(-1, "El Modulo indicado ya existe en el listado");
					return;
				}
			}
			
			// Recuperamos modulo
			for (Aplicacion app : this.listModulos) {
				if (this.idModulo == app.getId()) {
					modulo = app;
					break;
				}
			}
			
			if (modulo == null || modulo.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar asignar el Modulo indicado");
				return;
			}
			
			pojoRecursoModulo = new RecursosDocumentosModulos();
			pojoRecursoModulo.setIdRecursoDocumento(this.idRecursoDocumento);
			pojoRecursoModulo.setIdModulo(modulo.getId());
			pojoRecursoModulo.setModuloNombre(modulo.getAplicacion());
			pojoRecursoModulo.setFechaCreacion(Calendar.getInstance().getTime());
			pojoRecursoModulo.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			pojoRecursoModulo.setFechaModificacion(Calendar.getInstance().getTime());
			pojoRecursoModulo.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
			
			// Guardamos modulos asignados
			pojoRecursoModulo = this.ifzRecursos.saveOrUpdateModulo(this.pojoRecurso.getId(), pojoRecursoModulo);
			this.listRecursoModulos.add(pojoRecursoModulo);
			this.idModulo = 0;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar agregar el Modulo indicado", e);
		}
	}
	
	public void quitar() {
		try {
			control();
			// Comprobamos si el modulo ya fue agregado previamente
			for (RecursosDocumentosModulos item : this.listRecursoModulos) {
				if (this.idModulo == item.getId()) {
					this.listRecursoModulos.remove(this.listRecursoModulos.indexOf(item));
					break;
				}
			}
			
			// Borramos modulos eliminados previamente si corresponde
			this.ifzRecursos.deleteModulo(this.idModulo);
			this.idModulo = 0L;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar quitar el Modulo indicado", e);
		}
	}
	
	// -------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------
	
	public String getTitulo() {
		if (this.pojoRecurso != null && this.pojoRecurso.getId() != null && this.pojoRecurso.getId() > 0L)
			return "Recurso " + this.pojoRecurso.getId();
		return "Nuevo Recurso";
	}
	
	public void setTitulo(String value) {}
	
	public boolean getRecursoCargado() {
		return (this.pojoRecurso != null && this.pojoRecurso.getNombre() != null && ! "".equals(this.pojoRecurso.getNombre().trim()));
	}
	
	public void setRecursoCargado(String value) {}
	
	public boolean getVerModulos() {
		return (this.pojoRecurso != null && this.pojoRecurso.getId() != null && this.pojoRecurso.getId() > 0L);
	}
	
	public void setVerModulos(boolean value) {}
	
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

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
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

	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}

	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}
	
	public boolean getActivarFecha() {
		return activarFecha;
	}
	
	public void setActivarFecha(boolean activarFecha) {
		this.activarFecha = activarFecha;
	}

	public List<RecursosDocumentos> getListRecursos() {
		return listRecursos;
	}

	public void setListRecursos(List<RecursosDocumentos> listRecursos) {
		this.listRecursos = listRecursos;
	}

	public long getIdRecursoDocumento() {
		return idRecursoDocumento;
	}

	public void setIdRecursoDocumento(long idRecursoDocumento) {
		this.idRecursoDocumento = idRecursoDocumento;
	}

	public RecursosDocumentos getPojoRecurso() {
		if (this.pojoRecurso == null)
			this.pojoRecurso = new RecursosDocumentos();
		return this.pojoRecurso;
	}

	public void setPojoRecurso(RecursosDocumentos value) {}

	public int getPaginacion() {
		return paginacion;
	}

	public void setPaginacion(int paginacion) {
		this.paginacion = paginacion;
	}

	public List<SelectItem> getListTiposRecursos() {
		return listTiposRecursos;
	}

	public void setListTiposRecursos(List<SelectItem> listTiposRecursos) {
		this.listTiposRecursos = listTiposRecursos;
	}

	public List<RecursosDocumentosModulos> getListRecursoModulos() {
		return listRecursoModulos;
	}

	public void setListRecursoModulos(List<RecursosDocumentosModulos> listRecursoModulos) {
		this.listRecursoModulos = listRecursoModulos;
	}

	public long getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(long idModulo) {
		this.idModulo = idModulo;
	}

	public int getPaginacionModulos() {
		return paginacionModulos;
	}

	public void setPaginacionModulos(int paginacionModulos) {
		this.paginacionModulos = paginacionModulos;
	}

	public List<SelectItem> getListModulosItems() {
		return listModulosItems;
	}

	public void setListModulosItems(List<SelectItem> listModulosItems) {
		this.listModulosItems = listModulosItems;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
}
