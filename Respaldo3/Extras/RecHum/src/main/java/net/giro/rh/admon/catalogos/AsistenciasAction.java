package net.giro.rh.admon.catalogos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.adp.logica.ObraEmpleadoRem;
import net.giro.adp.logica.ObraRem;
import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.logica.ChecadorDetalleRem;
import net.giro.rh.admon.logica.ChecadorRem;

@ViewScoped
@ManagedBean(name="asisAction")
public class AsistenciasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AsistenciasAction.class);
	private InitialContext ctx;
	// Interfaces
	private ChecadorRem ifzChecador;
	private ChecadorDetalleRem ifzDetalles;
	private ObraRem ifzObras;
	private ObraEmpleadoRem ifzObraEmpleados;
	// Listas
	private List<ChecadorDetalle> listDetalles;
	private List<ChecadorDetalle> listDetallesLog;
	// Busqueda principal
	private List<Checador> listAsistencias;
	private Checador pojoChecador;
	private Checador pojoChecadorBorrar;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	// Busqueda de obras
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorBusquedaTipoObra;
	// Variables de operacion
    private long usuarioId;
    //private String usuario;
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	private int numPaginaMain;
	private int numPaginaDetalles;
	private int numPaginaDetallesLog;
	private int numPaginaObras;
	private byte[] fileSrc; 
	private String fileName;
	private String fileExtension;
	private boolean puedeEditar;
	private boolean buscarPorFecha;
	private Date fechaValor;
	private boolean perfilEgresos;
	private boolean buscarAdministrativo;
	

	public AsistenciasAction() {
		String valPerfil = "";
		
		try {
			//PropertyResourceBundle entornoProperties = null;
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			LoginManager loginManager = (LoginManager) ve.getValue(fc.getELContext());
			
			this.ctx = new InitialContext();
			this.ifzChecador = (ChecadorRem) this.ctx.lookup("ejb:/Logica_RecHum//ChecadorFac!net.giro.rh.admon.logica.ChecadorRem");
			this.ifzDetalles = (ChecadorDetalleRem) this.ctx.lookup("ejb:/Logica_RecHum//ChecadorDetalleFac!net.giro.rh.admon.logica.ChecadorDetalleRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObraEmpleados = (ObraEmpleadoRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraEmpleadoFac!net.giro.adp.logica.ObraEmpleadoRem");
			
			// Inicializaciones
			this.usuarioId = loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			valPerfil = loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			//this.usuario = loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();			
			//ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			//entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			this.listAsistencias = new ArrayList<Checador>();
			this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetallesLog = new ArrayList<ChecadorDetalle>();
			this.listObras = new ArrayList<Obra>();
			this.pojoChecador = new Checador();
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("nombreObra", "Obra"));
			this.tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPaginaMain = 1;
			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("nombreContrato", "Contrato"));
			this.tiposBusquedaObras.add(new SelectItem("id", "Clave"));
			this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getDescription();
			this.valorBusquedaObras = "";
			this.valorBusquedaTipoObra = 0;
			this.numPaginaObras	= 1;
			// Paginadores
			this.numPaginaDetalles 	= 1;
			this.numPaginaDetallesLog= 1;
			this.puedeEditar = false;
			this.fechaValor = Calendar.getInstance().getTime();
		} catch (Exception e) {
			log.error("Error en RecHum.AsistenciasAction.constructor AsistenciasAction", e);
			this.ctx = null;
		}
	}


	private void control() { 
		control(false, 0, "");
	}
	
	private void control(boolean value) {
		if (value)
			control(value, 1, "ERROR");
		else
			control(value, 0, null);
	}
	
	private void control(String value) { 
		if (value == null || "".equals(value))
			control(true, 1, "ERROR");
		else
			control(true, -1, value); 
	}
	
	private void control(int value) { 
		if (value <= 1)
			control(true, 1, "ERROR");
		else
			control(true, value, "ERROR"); 
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje) {
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "": mensaje;
	}
	
	public void buscar() throws Exception {
		try {
			Object busqueda = null;
			control();
			
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = tiposBusqueda.get(0).getValue().toString();
			
			if ("fecha".equals(this.campoBusqueda)) {
				busqueda = this.fechaValor;
			} else {
				busqueda = this.valorBusqueda;
			}

			if (this.listAsistencias == null)
				this.listAsistencias = new ArrayList<Checador>();
			this.listAsistencias.clear();
			
			this.listAsistencias = this.ifzChecador.findLikeProperty(this.campoBusqueda, busqueda, 120);
			if (this.listAsistencias == null || this.listAsistencias.isEmpty()) {
				log.info("ERROR 2 - BUsqueda sin resultados");
				control(2);
				return;
			}
    	} catch (Exception e) {
    		log.error("Error en RecHum.AsistenciasAction.buscar", e);
			control(true);
    	}
	}
	
	public void nuevo() {
		try {
			control();
			this.puedeEditar = true;
			this.pojoChecador = new Checador();
			this.pojoObra = null;
			if (this.listDetalles == null)
				this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetalles.clear();
			
			if (this.listDetallesLog == null)
				this.listDetallesLog = new ArrayList<ChecadorDetalle>();
			this.listDetallesLog.clear();
			
			nuevaBusquedaObras();
			nuevaCarga();
		} catch (Exception e) {
    		log.error("Error en RecHum.AsistenciasAction.nuevo", e);
			control(true);
	   	}
	}
	
	public void nuevaCarga() {
		this.fileSrc = null;
		this.fileName = "";
		this.fileExtension = "";
	}
	
	public void ver() {
		try {
			control();
			if (this.pojoChecador == null) {
				nuevo();
				return;
			}
			
			nuevaBusquedaObras();
			nuevaCarga();
			
			this.puedeEditar = true; // TODO: Validacion deshabilitado por pruebas y captura
			if (this.listDetalles == null)
				this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetalles.clear();
			
			if (this.listDetallesLog == null)
				this.listDetallesLog = new ArrayList<ChecadorDetalle>();
			this.listDetallesLog.clear();
			
			// Recuperamos la obra
			if (this.pojoChecador.getIdObra() != null && this.pojoChecador.getIdObra() > 0L)
				this.pojoObra = this.ifzObras.findById(this.pojoChecador.getIdObra());
			this.fileName = this.pojoChecador.getNombreArchivo();
			
			// Recuperamos los detalles de las asistencias
			this.numPaginaDetalles = 1;
			this.listDetalles = this.ifzDetalles.findByProperty("idChecador.id", this.pojoChecador.getId(), 0);
			if (this.listDetalles == null || this.listDetalles.isEmpty())
				this.listDetalles = new ArrayList<ChecadorDetalle>();
		} catch (Exception e) {
			log.error("Error en RecHum.AsistenciasAction.ver", e);
			control(true);
		}
	}
	
	public void borrar() {
		try {
			control();
			if(this.pojoChecadorBorrar != null && this.pojoChecadorBorrar.getId() != null && this.pojoChecadorBorrar.getId() > 0L) {
				// Buscamos el insumo en la lista
				for(Checador var : this.listAsistencias) {
					if (this.pojoChecadorBorrar.getId().equals(var.getId())) {
						// Modificamos los datos
						var.setEstatus(1);
						var.setFechaModificacion(Calendar.getInstance().getTime());
						var.setModificadoPor(this.usuarioId);
						
						// Actualizamos el insumo
						this.ifzChecador.update(var);
						break;
					}
				}
			}
			
			this.pojoChecadorBorrar = null;
		} catch (Exception e) {
			log.error("Error en RecHum.AsistenciasAction.buscar", e);
			control(true);
		}
	}
	
	public void guardar() throws Exception {
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar t1 = new GregorianCalendar();
		GregorianCalendar t2 = new GregorianCalendar();
		
		try {
    		control();
    		if (! this.puedeEditar)
    			return;
    		
			if (this.pojoObra == null || this.pojoObra.getId() == null) {
	    		log.error("Error en RecHum.AsistenciasAction.guardar. No selecciono OBRA");
				control(4);
				return;
			}
			
			if (this.puedeEditar && ! "DATA_FROM_OBRA".equals(this.fileName) && (this.fileName == null || "".equals(this.fileName))) {
	    		log.error("Error en RecHum.AsistenciasAction.guardar. No subio archivo");
				control(5);
				return;
			}
			
			this.pojoChecador.setModificadoPor(this.usuarioId);
			this.pojoChecador.setFechaModificacion(Calendar.getInstance().getTime());

			if (this.pojoChecador.getId() == null || this.pojoChecador.getId() <= 0L) {
				this.pojoChecador.setIdObra(this.pojoObra.getId());
				this.pojoChecador.setNombreObra(this.pojoObra.getNombre());
				this.pojoChecador.setNombreArchivo(this.fileName);
				this.pojoChecador.setCreadoPor(this.usuarioId);
				this.pojoChecador.setFechaCreacion(Calendar.getInstance().getTime());
				
				// Guardamos el registro en la BD
				this.pojoChecador.setId(this.ifzChecador.save(this.pojoChecador));
				this.listAsistencias.add(this.pojoChecador);
			} else {
				// Actualizamos el registro en la BD
				this.ifzChecador.update(this.pojoChecador);
				for(Checador var : this.listAsistencias) {
					if (var.getId().equals(this.pojoChecador.getId())) {
						var.setFechaModificacion(this.pojoChecador.getFechaModificacion());
						break;
					}
				}
			}
						
			// Guardamos los detalles del checador
			for (ChecadorDetalle var : this.listDetalles) {
				// Asignamos el checador
				var.setIdChecador(this.pojoChecador);
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Actualizamos Horas Trabajadas
				t1.setTime(var.getHoraSalidaMarcada());
				t2.setTime(var.getHoraEntradaMarcada());
				long diff = t1.getTimeInMillis() - t2.getTimeInMillis();
				var.setHorasTrabajadas((int) (diff / 3600000));
				
				// Guardamos detalle en la BD segun corresponda
				if (var.getId() != null && var.getId() > 0L) {
					this.ifzDetalles.update(var);
				} else {
					// Validamos que el dato de asistensia exista.
					ChecadorDetalle pojoExiste = this.ifzDetalles.existeAsistenciaPojo(var.getIdChecador().getId(), var.getIdEmpleado(), var.getFecha());

					// Guardamos/Actualizamos en la BD
					if (pojoExiste == null) {
						var.setCreadoPor(this.usuarioId);
						var.setFechaCreacion(Calendar.getInstance().getTime());
						
						var.setId(this.ifzDetalles.save(var));
					} else {
						pojoExiste.setHoraEntradaMarcada(var.getHoraEntradaMarcada());
						pojoExiste.setHoraSalidaMarcada(var.getHoraSalidaMarcada());
						pojoExiste.setHorasTrabajadas(var.getHorasTrabajadas());
						pojoExiste.setModificadoPor(this.usuarioId);
						pojoExiste.setFechaModificacion(Calendar.getInstance().getTime());
						
						this.ifzDetalles.update(pojoExiste);
					}
				}
			}
			
			nuevo();
		} catch (Exception e) {
    		log.error("Error en RecHum.AsistenciasAction.guardar", e);
			control(true);
	   	}
	}
	
	public void calcularHorasTrabajadas() {
		log.info("-------> calcularHorasTrabajadas <-------"); 
	}
	
	public void uploadListener(FileUploadEvent event) throws Exception {
		try {
			control();
			this.fileSrc = event.getUploadedFile().getData();
			this.fileName = event.getUploadedFile().getName();
			this.fileExtension = Files.getFileExtension(this.fileName);
		} catch (Exception e) {
			log.error("Error en RecHum.AsistenciasAction.uploadListener", e);
			control(true);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void analizarArchivo() throws Exception {
		try {
			control();
			if(this.fileSrc == null)
				return;
			
			if(this.fileName == null || "".equals(this.fileName))
				this.fileName = "archivo";
			
			if(this.fileExtension == null || "".equals(this.fileExtension))
				this.fileExtension = "xml";
			
			Respuesta respuesta = this.ifzDetalles.analizaDetalles(this.fileName, this.fileExtension, this.fileSrc);
			if(respuesta.getErrores().getCodigoError() != 0L) {
				this.mensaje = respuesta.getErrores().getDescError();
				log.error(respuesta.getErrores().getCodigoError() + " - " + this.mensaje);
				control(this.mensaje);
				return;
			}
			
			// Limpiamos listas
			if(this.listDetalles == null)
				this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetalles.clear();

			if(this.listDetallesLog == null)
				this.listDetallesLog = new ArrayList<ChecadorDetalle>();
			this.listDetallesLog.clear();
			
			// Comprobamos asistencias
			this.listDetalles = (List<ChecadorDetalle>) respuesta.getBody().getValor("detalles");
			if (this.listDetalles == null || this.listDetalles.isEmpty()) {
	    		log.error("Error en RecHum.AsistenciasAction.guardar. Lista de detalles vacia");
				control(6);
				return;
			}
			
			// Comprobamos asistencias con algun dato faltante
			this.listDetallesLog = (List<ChecadorDetalle>) respuesta.getBody().getValor("detallesLog");
			if (this.listDetallesLog != null && !this.listDetallesLog.isEmpty()) {
				this.listDetalles.clear();
				nuevaCarga();
				this.mensaje = "Lista de detalles con datos faltantes.";
				log.error("Error en RecHum.AsistenciasAction.guardar. Lista de detalles log no esta vacia. " + this.mensaje);
				control(7);
				return;
			}
		} catch (Exception e) {
			log.error("Error en RecHum.AsistenciasAction.analizarArchivo", e);
			control(true);
		}
	}
	
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	public String getObraNombre() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L && this.pojoObra.getNombre() != null)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
		return "";
	}
	
	public void setObraNombre(String value) {}

	public boolean getLogDetalles() {
		return !(this.listDetallesLog == null || this.listDetallesLog.isEmpty());
	}
	
	public void setLogDetalles(boolean value) {}
	
	public void busquedaPorFecha() {
		this.valorBusqueda = "";
		this.fechaValor = Calendar.getInstance().getTime();
		this.buscarPorFecha = ! this.buscarPorFecha;
	}
	
	public String getAsistenciaId() {
		if (this.pojoChecador != null && this.pojoChecador.getId() != null && this.pojoChecador.getId() > 0L)
			return " [" + this.pojoChecador.getId() + "]";
		return "";
	}
	
	public void setAsistenciaId(String value) {}
	
	// BUSQUEDA OBRAS
	// ------------------------------------------------------------
	public void nuevaBusquedaObras() {
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.valorBusquedaTipoObra = 0;
		
		if (this.listObras != null)
			this.listObras.clear();
    }

	public void buscarObras() throws Exception {
		try {
			control();
			if ("".equals(this.campoBusquedaObras))
				this.campoBusquedaObras = tiposBusquedaObras.get(0).getDescription();
			
			this.valorBusquedaTipoObra = 0;
			if (this.buscarAdministrativo)
				this.valorBusquedaTipoObra = 4;
			
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.valorBusquedaTipoObra);
			if(this.listObras.isEmpty()){
				control(2);
				return;
			}
    	} catch (Exception e) {
    		log.error("Error en RecHum.AsistenciasAction.buscarObras", e);
			control(true);
    	}
	}
	
	public void seleccionarObra() throws Exception {
		// Recuperamos los empleados de la obra
		List<ObraEmpleadoExt> lista = this.ifzObraEmpleados.findByPropertyExt("idObra", this.pojoObra.getId());
		if (lista != null && !lista.isEmpty()) {
			if(this.listDetalles == null || this.listDetalles.isEmpty()) {
				if(this.listDetalles == null)
					this.listDetalles = new ArrayList<ChecadorDetalle>();
				this.listDetalles.clear();
				this.fileName = "DATA_FROM_OBRA";
				
				SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat dateFullFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 08:00:00";
				
				for (ObraEmpleadoExt var : lista) {
					ChecadorDetalle aux = new ChecadorDetalle();
					aux.setIdEmpleado(var.getEmpleadoId());
					aux.setNombreEmpleado(var.getEmpleadoNombre());
					aux.setFecha(this.pojoChecador.getFecha()); 
					
					aux.setHoraEntrada(dateFullFormatter.parse(dateString));
					aux.setHoraSalida(dateFullFormatter.parse(dateString));
					aux.setHoraEntradaMarcada(dateFullFormatter.parse(dateString));
					aux.setHoraSalidaMarcada(dateFullFormatter.parse(dateString));
					aux.setTiempoAsistido(dateFullFormatter.parse(dateString));
					aux.setHorasTrabajadas(0);
					
					this.listDetalles.add(aux);
				}
			}
		}
		
		nuevaBusquedaObras();
	}
	
	public void cambiaFecha() {
		if (this.pojoChecador == null)
			return;
		
		if (this.pojoChecador.getFecha() == null)
			this.pojoChecador.setFecha(Calendar.getInstance().getTime());
		
		if (this.listDetalles == null || this.listDetalles.isEmpty())
			return;
		
		// Cambiamos fecha a los detalles
		for (ChecadorDetalle var : this.listDetalles) {
			var.setFecha(this.pojoChecador.getFecha());
		}
	}
	
	// ------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------
	
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

	public int getNumPaginaMain() {
		return numPaginaMain;
	}

	public void setNumPaginaMain(int numPaginaMain) {
		this.numPaginaMain = numPaginaMain;
	}

	public int getNumPaginaDetalles() {
		return numPaginaDetalles;
	}

	public void setNumPaginaDetalles(int numPaginaDetalles) {
		this.numPaginaDetalles = numPaginaDetalles;
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

	public List<Checador> getListAsistencias() {
		return listAsistencias;
	}

	public void setListAsistencias(List<Checador> listAsistencias) {
		this.listAsistencias = listAsistencias;
	}

	public List<ChecadorDetalle> getListDetalles() {
		return listDetalles;
	}

	public void setListDetalles(List<ChecadorDetalle> listDetalles) {
		this.listDetalles = listDetalles;
	}

	public List<Obra> getListObras() {
		return listObras;
	}

	public void setListObras(List<Obra> listObras) {
		this.listObras = listObras;
	}

	public Checador getPojoChecador() {
		return pojoChecador;
	}

	public void setPojoChecador(Checador pojoChecador) {
		this.pojoChecador = pojoChecador;
	}

	public Checador getPojoChecadorBorrar() {
		return pojoChecadorBorrar;
	}

	public void setPojoChecadorBorrar(Checador pojoChecadorBorrar) {
		this.pojoChecadorBorrar = pojoChecadorBorrar;
	}

	public int getNumPaginaObras() {
		return numPaginaObras;
	}

	public void setNumPaginaObras(int numPaginaObras) {
		this.numPaginaObras = numPaginaObras;
	}

	public List<ChecadorDetalle> getListDetallesLog() {
		return listDetallesLog;
	}

	public void setListDetallesLog(List<ChecadorDetalle> listDetallesLog) {
		this.listDetallesLog = listDetallesLog;
	}

	public int getNumPaginaDetallesLog() {
		return numPaginaDetallesLog;
	}

	public void setNumPaginaDetallesLog(int numPaginaDetallesLog) {
		this.numPaginaDetallesLog = numPaginaDetallesLog;
	}

	public boolean getPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public Obra getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(Obra pojoObra) {
		this.pojoObra = pojoObra;
	}

	public boolean getBuscarPorFecha() {
		return buscarPorFecha;
	}

	public void setBuscarPorFecha(boolean buscarPorFecha) {
		this.buscarPorFecha = buscarPorFecha;
	}

	public Date getFechaValor() {
		return fechaValor;
	}

	public void setFechaValor(Date fechaValor) {
		this.fechaValor = fechaValor;
	}


	
	public boolean isEsAdministrativo() {
		return perfilEgresos;
	}
	


	public void setEsAdministrativo(boolean perfilEgresos) {
		this.perfilEgresos = perfilEgresos;
	}

	public boolean isBuscarAdministrativo() {
		return buscarAdministrativo;
	}

	public void setBuscarAdministrativo(boolean buscarAdministrativo) {
		this.buscarAdministrativo = buscarAdministrativo;
	}
}
