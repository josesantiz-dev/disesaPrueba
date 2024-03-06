package cde.publico;

import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ManualesProcesos;
import net.giro.plataforma.beans.ManualesProcesosAplicaciones;
import net.giro.plataforma.logica.ManualesProcesosRem;
import net.giro.plataforma.seguridad.beans.Aplicacion;
import net.giro.respuesta.Respuesta;

import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class ManualesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ManualesAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	// ----------------------------------------------------------------
	private ManualesProcesosRem ifzManuales;
	private List<ManualesProcesos> listManualesProcesos;
	private List<ManualesProcesosAplicaciones> listManualProcesoApps;
	private List<ManualesProcesosAplicaciones> listManualProcesoAppsEliminadas;
	private int numPaginaApps;
	private ManualesProcesos pojoManualProceso;
	private long idManualProceso;
	private String manualesRuta;
	private String manualesPrefijo;
	// Carga Manual/Proceso
	private byte[] fileSrc;
	private String fileName;
	//private String fileExtension;
	//private long fileSize;
	// Aplicaciones
	private List<Aplicacion> listAplicaciones;
	private List<SelectItem> listAplicacionesItems;
	private long idAplicacion;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Variables de operacion
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public ManualesAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		PropertyResourceBundle entornoProperties = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			this.manualesRuta = entornoProperties.getString("manuales.path");
			if (this.manualesRuta == null || "".equals(this.manualesRuta))
				this.manualesRuta = "/var/cargas/";
			
			this.manualesPrefijo = entornoProperties.getString("manuales.prefix");
			if (this.manualesPrefijo == null || "".equals(this.manualesPrefijo))
				this.manualesPrefijo = "man-";
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			// Interfaces
			this.ctx = new InitialContext();
			this.ifzManuales = (ManualesProcesosRem) this.ctx.lookup("ejb:/Logica_Publico//ManualesProcesosFac!net.giro.plataforma.logica.ManualesProcesosRem");
			this.ifzManuales.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("id", "Clave"));
			this.tiposBusqueda.add(new SelectItem("nombre", "Nombre"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
		} catch (Exception e) {
			log.error("Error en constructor ManualesAction", e);
			this.ctx = null;
		}
	}
	
	public void buscar() {
		try {
			control();
			if (this.campoBusqueda == null || "".equals(this.campoBusqueda.trim()))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.listManualesProcesos = this.ifzManuales.findLikeProperty(this.campoBusqueda, this.valorBusqueda, false, "", 0);
			if (this.listManualesProcesos == null || this.listManualesProcesos.isEmpty())
				control(2, "Busqueda sin resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la consulta de Manueales/Procesos", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.idManualProceso = 0L;
			this.pojoManualProceso = new ManualesProcesos();
			this.listManualProcesoApps = new ArrayList<ManualesProcesosAplicaciones>();
			this.listManualProcesoAppsEliminadas = new ArrayList<ManualesProcesosAplicaciones>();
			cargarAplicaciones();
		} catch (Exception e) {
			control("Ocurrio un problema con la inicializacion de registro de Manual/Proceso", e);
		}
	}
	
	public void editar() {
		try {
			control();
			this.pojoManualProceso = this.ifzManuales.findById(this.idManualProceso);
			if (this.pojoManualProceso == null || this.pojoManualProceso.getId() == null || this.pojoManualProceso.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Manual/Proceso indicado");
				return;
			}
			
			cargarAplicaciones();
			this.fileName = this.pojoManualProceso.getNombreArchivo();
			//this.fileSize = this.pojoManualProceso.getTamano();
			//this.fileExtension = this.pojoManualProceso.getExtension();
			this.listManualProcesoApps = this.ifzManuales.findByManualProceso(this.idManualProceso, false, "");
			this.numPaginaApps = 1;
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Manual/Proceso", e);
		}
	}
	
	public void guardar() {
		Respuesta respuesta = null;
		String manualFileName = "";
		boolean esNuevo = false;
		
		try {
			control();
			if (! validarManualProceso())
				return;
			
			esNuevo = (this.pojoManualProceso.getId() == null || this.pojoManualProceso.getId() <= 0L);
			manualFileName = this.manualesRuta;
			if (this.manualesPrefijo != null && ! "".equals(this.manualesPrefijo.trim()))
				manualFileName += this.manualesPrefijo + (! this.manualesPrefijo.endsWith("-") ? "-" : "");
			manualFileName += ":ID." + this.pojoManualProceso.getExtension();
			this.pojoManualProceso.setStorageFileName(manualFileName);
			this.pojoManualProceso.setFileSrc(this.fileSrc);
			
			this.ifzManuales.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzManuales.save(this.pojoManualProceso, this.listManualProcesoApps);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}
			
			// Borramos asignaciones de aplicaciones si corresponde
			if (this.listManualProcesoAppsEliminadas != null && ! this.listManualProcesoAppsEliminadas.isEmpty()) 
				this.ifzManuales.deleteAplicaciones(this.listManualProcesoAppsEliminadas);
			
			this.pojoManualProceso = (ManualesProcesos) respuesta.getBody().getValor("manualProceso");
			if (this.listManualesProcesos != null && ! this.listManualesProcesos.isEmpty()) {
				for (ManualesProcesos item : this.listManualesProcesos) {
					if (this.pojoManualProceso.getId().longValue() == item.getId().longValue()) {
						this.listManualesProcesos.set(this.listManualesProcesos.indexOf(item), this.pojoManualProceso);
						break;
					}
				}
			}
			
			if (esNuevo) 
				this.listManualesProcesos.add(this.pojoManualProceso);
			
			// Restauramos variables
			this.idManualProceso = 0L;
			this.pojoManualProceso = new ManualesProcesos();
			this.listManualProcesoApps = new ArrayList<ManualesProcesosAplicaciones>();
			this.listManualProcesoAppsEliminadas = new ArrayList<ManualesProcesosAplicaciones>();
			nuevaCarga();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar guardar el Manual/Proceso", e);
		}
	}
	
	public void eliminar() {
		Respuesta respuesta = null;
		
		try {
			control();
			this.pojoManualProceso = this.ifzManuales.findById(this.idManualProceso);
			if (this.pojoManualProceso == null || this.pojoManualProceso.getId() == null || this.pojoManualProceso.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el Manual/Proceso indicado");
				return;
			}
			
			respuesta = this.ifzManuales.cancelar(this.pojoManualProceso);
			if (respuesta.getErrores().getCodigoError() != 0) {
				control(-1, respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}
			
			this.pojoManualProceso = (ManualesProcesos) respuesta.getBody().getValor("manualProceso");
			if (this.listManualesProcesos != null && ! this.listManualesProcesos.isEmpty()) {
				for (ManualesProcesos manual : this.listManualesProcesos) {
					if (this.pojoManualProceso.getId().longValue() == manual.getId().longValue()) {
						this.listManualesProcesos.set(this.listManualesProcesos.indexOf(manual), this.pojoManualProceso);
						break;
					}
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Manual/Proceso especificado", e);
		}
	}
	
	private boolean validarManualProceso() {
		if (this.pojoManualProceso.getFormato() == null || "".equals(this.pojoManualProceso.getFormato().trim())) {
			control(-1, "Debe indicar el nombre de formato para el Manual/Proceso");
			return false;
		}

		if (this.pojoManualProceso.getDescripcion() == null || "".equals(this.pojoManualProceso.getDescripcion().trim())) {
			control(-1, "Debe indicar la descripcion del Manual/Proceso");
			return false;
		}
		
		if (this.pojoManualProceso.getFormato() == null || "".equals(this.pojoManualProceso.getFormato().trim())) {
			control(-1, "Debe indicar el nombre de formato para el Manual/Proceso");
			return false;
		}
		
		if (this.pojoManualProceso.getVersion() <= 0L)
			this.pojoManualProceso.setVersion(1L);
		
		if (this.pojoManualProceso.getIdEmpresa() <= 0L)
			this.pojoManualProceso.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
		
		if ((this.pojoManualProceso.getId() == null || this.pojoManualProceso.getId() <= 0L) && this.pojoManualProceso.getFileSrc() == null) {
			control(-1, "Debe indicar el archivo Manual/Proceso");
			return false;
		}
		
		return true;
	}

	private String getExtension(String fileName) {
		String[] splitted = null;
		if (fileName == null || "".equals(fileName.trim()))
			return "";
		if (! fileName.contains("."))
			return "";
		splitted = fileName.split("\\.");
		if (splitted.length > 0)
			return splitted[splitted.length - 1];
		return "";
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
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Ocurrio un problema al realizar la operacion";
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n\nMANUALES/PROCESOS :: " + this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario() + "\n" + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	// ----------------------------------------------------------------------------------------
	// CARGA
	// ----------------------------------------------------------------------------------------

	public void nuevaCarga() {
		this.fileName = "";
		this.fileSrc = null;
	}
	
	public void uploadListener(UploadEvent event) {
		UploadItem item = null;
		
		try {
			control();
			item = event.getUploadItem();
			if (this.fileName.equals(item.getFileName())) 
				return;
			this.fileName = item.getFileName();
			this.pojoManualProceso.setNombreArchivo(item.getFileName());
			this.pojoManualProceso.setTamano(item.getFileSize());
			this.pojoManualProceso.setExtension(getExtension(item.getFileName()));
			this.fileSrc = ((item.isTempFile()) ? Files.readAllBytes(item.getFile().toPath()) : item.getData());
			log.info("\n\n------------------------------------------------------------------------"
					+ "\nTemp     :: " + item.isTempFile() 
					+ "\nFilename :: " + item.getFileName() 
					+ "\nFilesize :: " + item.getFileSize() 
					+ "\nFile     :: " + item.getFile() 
					+ "\nData     :: " + item.getData() 
					+ "\n------------------------------------------------------------------------");
		} catch (Exception e) {
			control("Ocurrio un problema al cargar el archivo", e);
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// APLICACIONES
	// ----------------------------------------------------------------------------------------

	private void cargarAplicaciones() {
		try {
			this.listAplicacionesItems = new ArrayList<SelectItem>();
			this.listAplicaciones = this.ifzManuales.findAllAplicaciones("");
			if (this.listAplicaciones == null || this.listAplicaciones.isEmpty())
				return;
			for (Aplicacion app : this.listAplicaciones)
				this.listAplicacionesItems.add(new SelectItem(app.getId(), app.getAplicacion()));
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar las Aplicaciones disponibles", e);
		}
	}
	
	public void agregarAplicacion() {
		Aplicacion app = null;
		
		try {
			control();
			if (this.idAplicacion <= 0L) {
				control(-1, "Debe indicar una Aplicacion");
				return;
			}
			
			if (this.listManualProcesoApps == null)
				this.listManualProcesoApps = new ArrayList<ManualesProcesosAplicaciones>();
			if (! this.listManualProcesoApps.isEmpty()) {
				// Comprobamos el listado
				for (ManualesProcesosAplicaciones item : this.listManualProcesoApps) {
					if (this.idAplicacion == item.getIdAplicacion().getId())
						return; // Si ya existe en el listado, no hacemos nada
				}
			}
			
			// Recupero el objeto Aplicacion
			for (Aplicacion item : this.listAplicaciones) {
				if (this.idAplicacion == item.getId()) {
					app = item;
					break;
				}
			}
			
			if (app == null) {
				control(-1, "Ocurrio un problema al intentar recuperar la Aplicacion seleccionada");
				return;
			}
			
			// Asigno la Aplicacion al Manual/Proceso,s i corresponde
			this.listManualProcesoApps.add(new ManualesProcesosAplicaciones(this.pojoManualProceso, app));
			Collections.sort(this.listManualProcesoApps, new Comparator<ManualesProcesosAplicaciones>() {
				@Override
				public int compare(ManualesProcesosAplicaciones o1, ManualesProcesosAplicaciones o2) {
					return o1.getIdAplicacion().getAplicacion().compareTo(o2.getIdAplicacion().getAplicacion());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar las Aplicaciones disponibles", e);
		} finally {
			this.idAplicacion = 0L;
		}
	}
	
	public void quitarAplicacion() {
		try {
			control();
			if (this.idAplicacion <= 0L) {
				control(-1, "Debe indicar una Aplicacion");
				return;
			}
			
			if (this.listManualProcesoAppsEliminadas == null)
				this.listManualProcesoAppsEliminadas = new ArrayList<ManualesProcesosAplicaciones>();
			if (this.listManualProcesoApps == null || this.listManualProcesoApps.isEmpty())
				return;
			
			// Comprobamos el listado
			for (ManualesProcesosAplicaciones item : this.listManualProcesoApps) {
				if (this.idAplicacion == item.getIdAplicacion().getId()) {
					this.listManualProcesoAppsEliminadas.add(item);
					this.listManualProcesoApps.remove(item);
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar cargar las Aplicaciones disponibles", e);
		} finally {
			this.idAplicacion = 0L;
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------------

	public List<ManualesProcesos> getListManualesProcesos() {
		return listManualesProcesos;
	}

	public void setListManualesProcesos(List<ManualesProcesos> listManualesProcesos) {
		this.listManualesProcesos = listManualesProcesos;
	}

	public List<ManualesProcesosAplicaciones> getListManualProcesoApps() {
		return listManualProcesoApps;
	}

	public void setListManualProcesoApps(List<ManualesProcesosAplicaciones> listManualProcesoApps) {
		this.listManualProcesoApps = listManualProcesoApps;
	}

	public int getNumPaginaApps() {
		return numPaginaApps;
	}

	public void setNumPaginaApps(int numPaginaApps) {
		this.numPaginaApps = numPaginaApps;
	}

	public ManualesProcesos getPojoManualProceso() {
		return pojoManualProceso;
	}

	public void setPojoManualProceso(ManualesProcesos pojoManualProceso) {
		this.pojoManualProceso = pojoManualProceso;
	}

	public long getIdManualProceso() {
		return idManualProceso;
	}

	public void setIdManualProceso(long idManualProceso) {
		this.idManualProceso = idManualProceso;
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

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
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

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
}
