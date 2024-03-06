package net.giro.rh.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;

import com.google.common.io.Files;

import net.giro.adp.beans.ObraExt;
import net.giro.adp.logica.ObraRem;
import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.logica.ChecadorDetalleRem;
import net.giro.rh.admon.logica.ChecadorRem;

@ViewScoped
@ManagedBean(name="checadorAction")
public class ChecadorAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ChecadorAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	// Variables de operacion
	private ChecadorRem ifzChecador;
	private ChecadorDetalleRem ifzDetalles;
	private List<ChecadorDetalle> listDetalles;
	private Checador pojoChecador;
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
    private long usuarioId;
	private int numPaginaObras;
	private int numPaginaDetalles;
	private byte[] fileSrc; 
	private String fileName;
	private String fileExtension;
	// Busqueda obras
	private ObraRem ifzObras;
	private List<ObraExt> listObras;
	private ObraExt pojoObra;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorBusquedaTipoObra;
	// Seleccion
	private boolean seleccionarTodos;
	
	public ChecadorAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			this.usuarioId = 0;
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();

			this.ctx = new InitialContext();
			this.ifzChecador = (ChecadorRem) this.ctx.lookup("ejb:/Logica_RecHum//ChecadorFac!net.giro.rh.admon.logica.ChecadorRem");
			this.ifzDetalles = (ChecadorDetalleRem) this.ctx.lookup("ejb:/Logica_RecHum//ChecadorDetalleFac!net.giro.rh.admon.logica.ChecadorDetalleRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			
			this.ifzChecador.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Listas
			this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.pojoChecador = new Checador();
			this.numPaginaObras = 1;
			this.numPaginaDetalles = 1;
			
			// Busqueda obras
			this.pojoObra = null;
			this.listObras = new ArrayList<ObraExt>();
			tiposBusquedaObras = new ArrayList<SelectItem>();
			tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			tiposBusquedaObras.add(new SelectItem("id", "Clave"));
			tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			tiposBusquedaObras.add(new SelectItem("nombreEncargado", "Encargado"));
	    	this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
			this.valorBusquedaObras = "";
			this.valorBusquedaTipoObra = 1;
		} catch (Exception e) {
			log.error("Error en constructor ChecadorAction", e);
			this.ctx = null;
		}
	}
	
	
	public void nuevo() {
		try {
    		this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			nuevaBusquedaObras();
			nuevoUploadFile();
			
			this.pojoChecador = new Checador();
			this.pojoObra = null;
			this.listDetalles = new ArrayList<ChecadorDetalle>();
		} catch (Exception e) {
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en nuevo", e);
    		throw e;
	   	}
	}
	
	@SuppressWarnings("unchecked")
	public void guardar() throws Exception {
		try {
    		this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if (this.pojoObra == null || this.pojoObra.getId() == null) {
	    		this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 4;
	    		log.error("Error en guardar. No selecciono OBRA");
				return;
			}
			
			if (this.fileName == null || "".equals(this.fileName)) {
	    		this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 5;
	    		log.error("Error en guardar. No subio archivo");
				return;
			}

			this.pojoChecador.setIdObra(this.pojoObra.getId());
			this.pojoChecador.setNombreObra(this.pojoObra.getNombre());
			this.pojoChecador.setNombreArchivo(this.fileName);
			this.pojoChecador.setCreadoPor(this.usuarioId);
			this.pojoChecador.setFechaCreacion(Calendar.getInstance().getTime());
			this.pojoChecador.setModificadoPor(this.usuarioId);
			this.pojoChecador.setFechaModificacion(Calendar.getInstance().getTime());	
			
			// Analizamos el archivo anexo
			Respuesta respuesta = this.ifzDetalles.analizaDetalles(this.fileName, this.fileExtension, this.fileSrc);
			if(respuesta.getErrores().getCodigoError() != 0L) {
	    		this.operacion = true;
				this.tipoMensaje = -1;
				this.mensaje = respuesta.getErrores().getDescError();
				log.error(respuesta.getErrores().getCodigoError() + " - " + this.mensaje);
				return;
			}
			
			// Comprobamos que se genero detalles
			List<ChecadorDetalle> detalles = (List<ChecadorDetalle>) respuesta.getBody().getValor("detalles");
			if (detalles == null || detalles.isEmpty()) {
	    		this.operacion = true;
				this.tipoMensaje = 6;
				this.mensaje = "Lista de detalles vacia.";
				log.error("Error en guardar. Lista de detalles vacia");
				return;
			}
			
			// Comprobamos que no haya asistencia sin algun dato faltante
			this.listDetalles = (List<ChecadorDetalle>) respuesta.getBody().getValor("detallesLog");
			if (this.listDetalles != null && !this.listDetalles.isEmpty()) {
				nuevoUploadFile();
	    		this.operacion = true;
				this.tipoMensaje = 7;
				this.mensaje = "Lista de detalles con datos faltantes.";
				log.error("Error en guardar. Lista de detalles log no esta vacia. " + this.mensaje);
				return;
			}
			
			// Guardamos el registro en la BD
			this.pojoChecador.setId(this.ifzChecador.save(this.pojoChecador));
			//Checador pojoChecadorAux = this.ifzChecador.convertir(pojoChecador);
			
			// Guardamos los detalles del checador
			for (ChecadorDetalle var : detalles) {
				// Asignamos el checador
				var.setIdChecador(this.pojoChecador);
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Validamos que el dato de asistensia exista.
				ChecadorDetalle pojoExiste = this.ifzDetalles.existeAsistenciaPojo(var.getIdChecador().getId(), var.getIdEmpleado(), var.getFecha());

				// Guardamos en la BD
				if (pojoExiste == null) {
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
					
					var.setId(this.ifzDetalles.save(var));
				} else {
					pojoExiste.setHoraEntradaMarcada(var.getHoraEntradaMarcada());
					pojoExiste.setHoraSalidaMarcada(var.getHoraSalidaMarcada());
					
					this.ifzDetalles.update(pojoExiste);
				}
				
				/*if (var.getId() == null || var.getId() <= 0L) {
					var.setCreadoPor(this.usuarioId);
					var.setFechaCreacion(Calendar.getInstance().getTime());
					
					var.setId(this.ifzDetalles.save(var));
				} else {
					this.ifzDetalles.update(var);
				}*/
			}
			
			nuevo();
		} catch (Exception e) {
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en guardar", e);
    		throw e;
	   	}
	}
	
	public void nuevoUploadFile() {
		this.fileSrc = null;
		this.fileName = "";
		this.fileExtension = "";
	}
	
	public void uploadListener(FileUploadEvent event) throws Exception {
		try {
			this.operacion = false;
			this.tipoMensaje = 0;
			this.mensaje = "OK";
			
			this.fileSrc = event.getUploadedFile().getData();
			this.fileName = stripExtension(event.getUploadedFile().getName());
			this.fileExtension = Files.getFileExtension(event.getUploadedFile().getName());
		} catch (Exception e) {
			this.operacion = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en uploadListener", e);
			throw e;
		}
	}
	
	private String stripExtension(String str) {
		int pos = -1;
		
        if (str == null) 
        	return null;

        pos = str.lastIndexOf(".");
        if (pos == -1) 
        	return str;

        return str.substring(0, pos);
    }
	
	public void analizarArchivo() throws Exception {
		try {
			this.operacion = false;
			this.tipoMensaje = 0;
			this.mensaje = "";
			
			if(this.fileSrc == null)
				return;
			
			if(this.fileName == null || "".equals(this.fileName))
				this.fileName = "archivo";
			
			if(this.fileExtension == null || "".equals(this.fileExtension))
				this.fileExtension = "xml";
			
			Respuesta respuesta = this.ifzChecador.analizarArchivo(this.fileSrc, this.fileExtension);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				/*this.pojoInsumo = (InsumosExt) respuesta.getBody().getValor("pojoInsumo");
				this.listMateriales = (List<InsumosDetallesExt>) respuesta.getBody().getValor("materiales");
				this.listManoDeObra = (List<InsumosDetallesExt>) respuesta.getBody().getValor("manoDeObra");
				this.listHerramientas = (List<InsumosDetallesExt>) respuesta.getBody().getValor("herramientas");
				this.listOtros = (List<InsumosDetallesExt>) respuesta.getBody().getValor("otros");
				this.listProductos = (List<InsumoRow>) respuesta.getBody().getValor("productos");
				
				this.countOtros = this.listOtros.size();
				
				if (this.listProductos != null && !this.listProductos.isEmpty()) {
					this.band = true;
					this.tipoMensaje = 4;
					this.mensaje = "ERROR";
					log.error(respuesta.getErrores().getCodigoError() + " - " + this.mensaje);
				}
				
				this.pojoInsumo.setNombreArchivo(this.fileName);*/
			} else {
				this.operacion = true;
				this.tipoMensaje = -1;
				this.mensaje = respuesta.getErrores().getDescError();
				log.error(respuesta.getErrores().getCodigoError() + " - " + this.mensaje);
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en analizarArchivo", e);
			throw e;
		}
	}
	
	public String getObraNombre() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L && this.pojoObra.getNombre() != null)
			return this.pojoObra.getNombre();
		return "";
	}
	
	public void setObraNombre(String value) {}
	
	public TimeZone getTimeZone() {  
		TimeZone timeZone = TimeZone.getDefault();  
		return timeZone;
	}

	// --------------------------------------------------------------------------------------
	// Busqueda Obras
	// --------------------------------------------------------------------------------------
	
	public void nuevaBusquedaObras() {
    	this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.valorBusquedaTipoObra = 1;
		if (this.listObras != null)
			this.listObras.clear();
    }

	public void buscarObras() throws Exception {
		try {
    		this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
			
			if ("".equals(this.campoBusquedaObras))
				this.listObras = this.ifzObras.findLikePropertyExt("id", this.valorBusquedaObras, this.valorBusquedaTipoObra, "", 0);
			else 
				this.listObras = this.ifzObras.findLikePropertyExt(this.campoBusquedaObras, this.valorBusquedaObras, this.valorBusquedaTipoObra, "", 0);
			
			if (this.listObras == null || this.listObras.isEmpty()) {
	    		this.operacion = true;
				this.tipoMensaje = 2;
				this.mensaje = "ERROR";
				return;
			}
    	} catch (Exception e) {
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en buscarObras", e);
    		throw e;
    	}
	}

	public void seleccionarObra() throws Exception {
		// Recuperamos los insumos de la obra seleccionada
		nuevaBusquedaObras();
	}
	
	// ------------------------------------------------------
	// - PROPIEDADES
	// ------------------------------------------------------
	
	public long getChecadorId() {
		if (this.pojoChecador != null && this.pojoChecador.getId() != null && this.pojoChecador.getId() > 0L)
			return this.pojoChecador.getId();
		return 0L;
	}
	
	public void setChecadorId(long value) {}
	
	public List<ObraExt> getListObras() {
		return listObras;
	}

	public void setListObras(List<ObraExt> listObras) {
		this.listObras = listObras;
	}

	public Checador getPojoChecador() {
		return pojoChecador;
	}

	public void setPojoChecador(Checador pojoChecador) {
		this.pojoChecador = pojoChecador;
	}

	public ObraExt getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(ObraExt pojoObra) {
		this.pojoObra = pojoObra;
	}

	public List<SelectItem> getTiposBusquedaObras() {
		return tiposBusquedaObras;
	}

	public void setTiposBusquedaObras(List<SelectItem> tiposBusquedaObras) {
		this.tiposBusquedaObras = tiposBusquedaObras;
	}

	public String getCampoBusquedaObras() {
		return campoBusquedaObras;
	}

	public void setCampoBusquedaObras(String campoBusquedaObras) {
		this.campoBusquedaObras = campoBusquedaObras;
	}

	public String getValorBusquedaObras() {
		return valorBusquedaObras;
	}

	public void setValorBusquedaObras(String valorBusquedaObras) {
		this.valorBusquedaObras = valorBusquedaObras;
	}

	public int getValorBusquedaTipoObra() {
		return valorBusquedaTipoObra;
	}

	public void setValorBusquedaTipoObra(int valorBusquedaTipoObra) {
		this.valorBusquedaTipoObra = valorBusquedaTipoObra;
	}

	public boolean isOperacion() {
		return operacion;
	}

	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
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

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}

	public List<ChecadorDetalle> getListDetalles() {
		return listDetalles;
	}

	public void setListDetalles(List<ChecadorDetalle> listDetalles) {
		this.listDetalles = listDetalles;
	}

	public int getNumPaginaDetalles() {
		return numPaginaDetalles;
	}

	public void setNumPaginaDetalles(int numPaginaDetalles) {
		this.numPaginaDetalles = numPaginaDetalles;
	}
	
	public boolean isSeleccionarTodos() {
		return seleccionarTodos;
	}
	
	public void setSeleccionarTodos(boolean seleccionarTodos) {
		this.seleccionarTodos = seleccionarTodos;
	}
}
