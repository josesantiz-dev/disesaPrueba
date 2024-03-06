package net.giro.rh.admon.catalogos;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;

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
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.logica.ChecadorDetalleRem;
import net.giro.rh.admon.logica.ChecadorRem;
import net.giro.rh.admon.logica.EmpleadoContratoRem;

@ViewScoped
@ManagedBean(name="asisAction")
public class AsistenciasAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AsistenciasAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;
	// Interfaces
	private ChecadorRem ifzChecador;
	private ChecadorDetalleRem ifzDetalles;
	private ObraRem ifzObras;
	private ObraEmpleadoRem ifzObraEmpleados;
	private EmpleadoContratoRem ifzContrato;
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
	private int numPaginaMain;
	// Busqueda de obras
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorBusquedaTipoObra;
	private boolean avanzadaBusquedaObras;
	// Variables de operacion
    private long usuarioId;
    private String usuario;
    private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
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
	private TimeZone timeZone;
	// DEBUG
	private boolean isDebug;
	private Map<String,String> paramsRequest;
	

	public AsistenciasAction() {
		Map<String,String> params = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();

			// Obtenemmos los parametros de la URL si corresponde
			params = fc.getExternalContext().getRequestParameterMap();
		    this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : params.entrySet())
		    	this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;
		    
			this.ctx = new InitialContext();
			this.ifzChecador = (ChecadorRem) this.ctx.lookup("ejb:/Logica_RecHum//ChecadorFac!net.giro.rh.admon.logica.ChecadorRem");
			this.ifzDetalles = (ChecadorDetalleRem) this.ctx.lookup("ejb:/Logica_RecHum//ChecadorDetalleFac!net.giro.rh.admon.logica.ChecadorDetalleRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObraEmpleados = (ObraEmpleadoRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraEmpleadoFac!net.giro.adp.logica.ObraEmpleadoRem");
			this.ifzContrato = (EmpleadoContratoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			
			this.ifzChecador.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObraEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzContrato.setInfoSesion(this.loginManager.getInfoSesion());
			
			// PERFILES
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
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
			this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
			this.valorBusquedaObras = "";
			this.valorBusquedaTipoObra = 0;
			this.numPaginaObras	= 1;
			// Paginadores
			this.numPaginaDetalles 	= 1;
			this.numPaginaDetallesLog= 1;
			this.puedeEditar = false;
			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
			this.fechaValor = Calendar.getInstance(this.timeZone).getTime();
		} catch (Exception e) {
			log.error("Error en RecHum.AsistenciasAction.constructor AsistenciasAction", e);
			this.ctx = null;
		}
	}


	public void buscar() throws Exception {
		try {
			control();
			if (this.fechaValor == null)
				this.fechaValor = Calendar.getInstance(this.timeZone).getTime();
			
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();

			if (this.listAsistencias == null)
				this.listAsistencias = new ArrayList<Checador>();
			this.listAsistencias.clear();
			this.numPaginaMain = 1;

			this.ifzChecador.orderBy("nombreObra, id desc");
			if ("fecha".equals(this.campoBusqueda))
				this.listAsistencias = this.ifzChecador.findByProperty(this.campoBusqueda, this.fechaValor, 0);
			else
				this.listAsistencias = this.ifzChecador.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 0);
			
			/*if (this.listAsistencias != null)
				this.listAsistencias.clear();

			this.ifzChecador.orderBy("nombreObra, id desc");
			this.listAsistencias = this.ifzChecador.findByDate(this.fechaValor, this.valorBusqueda);*/
			if (this.listAsistencias == null || this.listAsistencias.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar las Asistencias", e);
    	} finally {
			if (this.listAsistencias == null)
				this.listAsistencias = new ArrayList<Checador>();
			this.numPaginaMain = 1;
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
    		control("Ocurrio un problema al intentar generar una nueva Asistensia", e);
	   	}
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
			this.ifzDetalles.orderBy("nombreEmpleado");
			this.listDetalles = this.ifzDetalles.findByProperty("idChecador.id", this.pojoChecador.getId(), 0);
			if (this.listDetalles == null || this.listDetalles.isEmpty())
				this.listDetalles = new ArrayList<ChecadorDetalle>();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar los registros de la Asistencia indicada", e);
		}
	}
	
	public void guardar() throws Exception {
		GregorianCalendar timeEntrada = new GregorianCalendar(this.timeZone);
		GregorianCalendar timeSalida = new GregorianCalendar(this.timeZone);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String horaFormateada = "00:00";
		int horasTrabajadas = 0;
		long minutos = 0;
		
		try {
    		control();
    		if (! this.puedeEditar)
    			return;
    		
			if (this.pojoObra == null || this.pojoObra.getId() == null) {
				control(4, "Debe seleccionar una Obra");
				return;
			}
			
			if (this.puedeEditar && ! "DATA_FROM_OBRA".equals(this.fileName) && (this.fileName == null || "".equals(this.fileName))) {
				control(5, "Debe indicar el archivo de Asistencia");
				return;
			}
			
			if (this.pojoChecador.getId() == null || this.pojoChecador.getId() <= 0L) {
				this.pojoChecador.setIdObra(this.pojoObra.getId());
				this.pojoChecador.setNombreObra(this.pojoObra.getNombre());
				this.pojoChecador.setNombreArchivo(this.fileName);
				this.pojoChecador.setCreadoPor(this.usuarioId);
				this.pojoChecador.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
				this.pojoChecador.setModificadoPor(this.usuarioId);
				this.pojoChecador.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				
				// Guardamos el registro en la BD
				this.pojoChecador.setId(this.ifzChecador.save(this.pojoChecador));
				this.listAsistencias.add(this.pojoChecador);
			} else {
				this.pojoChecador.setModificadoPor(this.usuarioId);
				this.pojoChecador.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				
				// Actualizamos el registro en la BD
				this.ifzChecador.update(this.pojoChecador);
				for (Checador var : this.listAsistencias) {
					if (var.getId().equals(this.pojoChecador.getId())) {
						var.setFechaModificacion(this.pojoChecador.getFechaModificacion());
						break;
					}
				}
			}
						
			// Guardamos los detalles del checador
			for (ChecadorDetalle var : this.listDetalles) {
				if (var.getUsuarioAutoriza() > 0)
					continue;
				
				// Asignamos el checador
				var.setIdChecador(this.pojoChecador);
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());

				// Normalizamos entrada marcada
				dateFormatter.applyPattern("yyyy-MM-dd");
				horaFormateada = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
				dateFormatter.applyPattern("HH:mm");
				horaFormateada += dateFormatter.format(var.getHoraEntradaMarcada()) + ":00";
				var.setHoraEntradaMarcada(formatter.parse(horaFormateada));
				
				// Normalizamos salida marcada
				dateFormatter.applyPattern("yyyy-MM-dd");
				horaFormateada = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
				dateFormatter.applyPattern("HH:mm");
				horaFormateada += dateFormatter.format(var.getHoraSalidaMarcada()) + ":00";
				var.setHoraSalidaMarcada(formatter.parse(horaFormateada));
				
				// Calculamos Tiempo Asistido
				timeEntrada.setTime(var.getHoraEntradaMarcada());
				timeSalida.setTime(var.getHoraSalidaMarcada());
				minutos = timeSalida.getTimeInMillis() - timeEntrada.getTimeInMillis();
				minutos = minutos / (1000 * 60);
				horasTrabajadas = 0;
				while (minutos >= 60) {
					horasTrabajadas += 1;
					minutos = minutos - 60;
					if (minutos < 0)
						minutos = 0;
				}
				
				// Asignamos Tiempo Asistido y Horas Trabajadas
				dateFormatter.applyPattern("yyyy-MM-dd");
				horaFormateada = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
				horaFormateada += ((horasTrabajadas < 10) ? "0" + horasTrabajadas : horasTrabajadas).toString() + ":" + ((minutos < 10) ? "0" + minutos : minutos).toString() + ":00";
				var.setTiempoAsistido(formatter.parse(horaFormateada));
				var.setHorasTrabajadas(horasTrabajadas);
				
				// Guardamos detalle en la BD segun corresponda
				if (var.getId() != null && var.getId() > 0L) {
					this.ifzDetalles.update(var);
				} else {
					// Validamos que el dato de asistensia exista.
					ChecadorDetalle pojoExiste = this.ifzDetalles.existeAsistenciaPojo(var.getIdChecador().getId(), var.getIdEmpleado(), var.getFecha());

					// Guardamos/Actualizamos en la BD
					if (pojoExiste == null) {
						var.setCreadoPor(this.usuarioId);
						var.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
						
						var.setId(this.ifzDetalles.save(var));
					} else {
						pojoExiste.setHoraEntradaMarcada(var.getHoraEntradaMarcada());
						pojoExiste.setHoraSalidaMarcada(var.getHoraSalidaMarcada());
						pojoExiste.setTiempoAsistido(var.getTiempoAsistido());
						pojoExiste.setHorasTrabajadas(var.getHorasTrabajadas());
						pojoExiste.setModificadoPor(this.usuarioId);
						pojoExiste.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
						
						this.ifzDetalles.update(pojoExiste);
					}
				}
			}
			
			nuevo();
		} catch (Exception e) {
    		control("Ocurrio un problema al intentar guardar las Asistencias", e);
	   	}
	}

	public void borrar() {
		try {
			control();
			if (this.pojoChecadorBorrar != null && this.pojoChecadorBorrar.getId() != null && this.pojoChecadorBorrar.getId() > 0L) {
				// Buscamos el insumo en la lista
				for (Checador var : this.listAsistencias) {
					if (this.pojoChecadorBorrar.getId().equals(var.getId())) {
						// Modificamos los datos
						var.setEstatus(1);
						var.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
						var.setModificadoPor(this.usuarioId);
						
						// Actualizamos el insumo
						this.ifzChecador.update(var);
						break;
					}
				}
			}
			
			this.pojoChecadorBorrar = null;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Borrar la Asistencia indicada", e);
		}
	}
	
	public void calcularHorasTrabajadas() {
		log.info("-------> calcularHorasTrabajadas <-------"); 
	}

	public void busquedaPorFecha() {
		this.valorBusqueda = "";
		this.fechaValor = Calendar.getInstance(this.timeZone).getTime();
		this.buscarPorFecha = ! this.buscarPorFecha;
	}

	public void cambiaFecha() {
		if (this.pojoChecador == null)
			return;
		
		if (this.pojoChecador.getFecha() == null)
			this.pojoChecador.setFecha(Calendar.getInstance(this.timeZone).getTime());
		
		if (this.listDetalles == null || this.listDetalles.isEmpty())
			return;
		
		// Cambiamos fecha a los detalles
		for (ChecadorDetalle var : this.listDetalles) {
			var.setFecha(this.pojoChecador.getFecha());
		}
	}
	
	/*private boolean validaRequest(String param, String refValue) {
		if (! this.isDebug)
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		if (refValue == null || "".equals(refValue.trim()))
			return true;
		
		return (refValue.trim().equals(this.paramsRequest.get(param).trim()));
	}*/
	
	private void control() { 
		this.operacion = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}
	
	private void control(int tipoMensaje, String mensaje) { 
		control(true, tipoMensaje, mensaje, null); 
	}
	
	private void control(String value, Throwable throwable) { 
		if (value == null || "".equals(value))
			control(true, 1, "ERROR", throwable);
		else
			control(true, -1, value, throwable); 
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";

		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		codigo = "Ex" + formatter.format(Calendar.getInstance().getTime());
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		log.error("\n\n ASISTENCIAS :: " + this.usuario + " :: " + codigo + "+" + this.tipoMensaje + "\n" + mensaje + "\n" + this.mensajeDetalles, throwable);
	}

	// ------------------------------------------------------------
	// CARGA ASISTENCIA
	// ------------------------------------------------------------

	public void nuevaCarga() {
		control();
		this.fileSrc = null;
		this.fileName = "";
		this.fileExtension = "";
	}
	
	public void uploadListener(FileUploadEvent event) throws Exception {
		try {
			control();
			this.fileSrc = event.getUploadedFile().getData();
			this.fileName = event.getUploadedFile().getName();
			this.fileExtension = Files.getFileExtension(this.fileName);
		} catch (Exception e) {
			control("Ocurrio un problema al leer el archivo de Asistencia", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void analizarArchivo() throws Exception {
		Respuesta respuesta = null; 
		
		try {
			control();
			if (this.fileSrc == null)
				return;
			
			if (this.fileName == null || "".equals(this.fileName))
				this.fileName = "archivo";
			
			if (this.fileExtension == null || "".equals(this.fileExtension))
				this.fileExtension = "xml";
			
			respuesta = this.ifzDetalles.analizaDetalles(this.fileName, this.fileExtension, this.fileSrc);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error(respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, respuesta.getErrores().getDescError());
				return;
			}
			
			// Limpiamos listas
			if (this.listDetalles == null)
				this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetalles.clear();

			if (this.listDetallesLog == null)
				this.listDetallesLog = new ArrayList<ChecadorDetalle>();
			this.listDetallesLog.clear();
			
			// Comprobamos asistencias
			this.listDetalles = (List<ChecadorDetalle>) respuesta.getBody().getValor("detalles");
			if (this.listDetalles == null || this.listDetalles.isEmpty()) {
				control(6, "Lista de detalles vacia");
				return;
			}
			
			// Comprobamos asistencias con algun dato faltante
			this.listDetallesLog = (List<ChecadorDetalle>) respuesta.getBody().getValor("detallesLog");
			if (this.listDetallesLog != null && !this.listDetallesLog.isEmpty()) {
				this.listDetalles.clear();
				nuevaCarga();
				control(7, "Lista de detalles con datos faltantes.");
				return;
			}
		} catch (Exception e) {
			control("Ocurrio un problema al procesar el archivo de asistencia", e);
		}
	}
	
	// ------------------------------------------------------------
	// BUSQUEDA OBRAS
	// ------------------------------------------------------------
	
	public void nuevaBusquedaObras() {
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.valorBusquedaTipoObra = 0;
		this.numPaginaObras	= 1;
		
		if (this.listObras != null)
			this.listObras.clear();
    }

	public void buscarObras() throws Exception {
		try {
			control();
			if (this.campoBusquedaObras == null || "".equals(this.campoBusquedaObras.trim()))
				this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
			
			this.valorBusquedaTipoObra = 0;
			if (this.buscarAdministrativo)
				this.valorBusquedaTipoObra = 4;
			
			this.ifzObras.estatus(this.ifzObras.findEstatusCanceladoObras());
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.valorBusquedaTipoObra);
			if (this.listObras == null || this.listObras.isEmpty()) {
				control(2, "Busqueda sin resultados");
				return;
			}
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la Busqueda de Obras", e);
    	} finally {
    		if (this.listObras == null)
    			this.listObras = new ArrayList<Obra>();
    		this.numPaginaObras	= 1;
    	}
	}
	
	public void seleccionarObra() throws Exception {
		SimpleDateFormat dateFullFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		List<ObraEmpleadoExt> lista = null;
		List<EmpleadoContrato> listContratos = null;
		EmpleadoContrato pojoContrato = null;
		String dateString = "";
		
		try {
			control();
			// Recuperamos los empleados de la obra
			log.info("Recuperando empleados ... ");
			lista = this.ifzObraEmpleados.findByPropertyExt("idObra", this.pojoObra.getId());
			if (lista == null || lista.isEmpty()) {
				control("La obra seleccionada no tiene asignado ningun Empleado", null);
				return;
			}
			
			if (this.listDetalles == null || this.listDetalles.isEmpty()) {
				if (this.listDetalles == null)
					this.listDetalles = new ArrayList<ChecadorDetalle>();
				this.listDetalles.clear();
				this.fileName = "DATA_FROM_OBRA";

				log.info("Ordenando empleados ... ");
				Collections.sort(lista, new Comparator<ObraEmpleadoExt>() {
					@Override
					public int compare(ObraEmpleadoExt o1, ObraEmpleadoExt o2) {
						return o1.getIdEmpleado().getNombrePorApellidos().compareTo(o2.getIdEmpleado().getNombrePorApellidos());
					}
				});

				log.info("Generando detalles para Asistencias (empleados) ... ");
				ChecadorDetalle aux = null;
				for (ObraEmpleadoExt var : lista) {
					aux = new ChecadorDetalle();
					aux.setIdEmpleado(var.getEmpleadoId());
					aux.setNombreEmpleado(var.getIdEmpleado().getNombrePorApellidos());
					aux.setFecha(this.pojoChecador.getFecha()); 
					
					listContratos = this.ifzContrato.contratoValido(var.getEmpleadoId());
					if (listContratos != null && ! listContratos.isEmpty()) {
						pojoContrato = listContratos.get(0);

						dateFormatter.applyPattern("yyyy-MM-dd");
						dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
						dateFormatter.applyPattern("HH:mm:ss");
						dateString += dateFormatter.format(pojoContrato.getHoraEntrada());
						aux.setHoraEntrada(dateFullFormatter.parse(dateString));
						
						/*dateFormatter.applyPattern("yyyy-MM-dd");
						dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 00:00:00";*/
						aux.setHoraEntradaMarcada(dateFullFormatter.parse(dateString));

						dateFormatter.applyPattern("yyyy-MM-dd");
						dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
						dateFormatter.applyPattern("HH:mm:ss");
						dateString += dateFormatter.format(pojoContrato.getHoraSalida());
						aux.setHoraSalida(dateFullFormatter.parse(dateString));
						
						/*dateFormatter.applyPattern("yyyy-MM-dd");
						dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 00:00:00";*/
						aux.setHoraSalidaMarcada(dateFullFormatter.parse(dateString));
						
						if (pojoContrato.getHoraEntradaComplemento() != null && pojoContrato.getHoraSalidaComplemento() != null) {
							dateFormatter.applyPattern("yyyy-MM-dd");
							dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
							dateFormatter.applyPattern("HH:mm:ss");
							dateString += dateFormatter.format(pojoContrato.getHoraEntradaComplemento());
							aux.setHoraEntradaComplemento(dateFullFormatter.parse(dateString));

							dateFormatter.applyPattern("yyyy-MM-dd");
							dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
							dateFormatter.applyPattern("HH:mm:ss");
							dateString += dateFormatter.format(pojoContrato.getHoraSalidaComplemento());
							aux.setHoraSalidaComplemento(dateFullFormatter.parse(dateString));
						}
					} else {
						aux.setContratoInvalido(true);
						dateFormatter.applyPattern("yyyy-MM-dd");
						dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 08:00:00";
						aux.setHoraEntrada(dateFullFormatter.parse(dateString));
						//dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 00:00:00";
						aux.setHoraEntradaMarcada(dateFullFormatter.parse(dateString));
	
						dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 16:00:00";
						aux.setHoraSalida(dateFullFormatter.parse(dateString));
						//dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 00:00:00";
						aux.setHoraSalidaMarcada(dateFullFormatter.parse(dateString));
					}

					dateFormatter.applyPattern("yyyy-MM-dd");
					dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 08:00:00";
					aux.setTiempoAsistido(dateFullFormatter.parse(dateString));
					aux.setHorasTrabajadas(8);
					
					this.listDetalles.add(aux);
				}
			}
			
			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los Empleados de la Obra seleccionada", e);
		}
	}

	public void toggleAvanzadaBusquedaObras() {
		this.avanzadaBusquedaObras = ! this.avanzadaBusquedaObras;
	}
	
	// ------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------

	public boolean getScrolling() {
		if (this.listDetalles != null && ! this.listDetalles.isEmpty() && this.listDetalles.size() > 9)
			return true;
		return false;
	}
	
	public void setScrolling(boolean value) {}
	
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

	public String getAsistenciaId() {
		if (this.pojoChecador != null && this.pojoChecador.getId() != null && this.pojoChecador.getId() > 0L)
			return " [" + this.pojoChecador.getId() + "]";
		return "";
	}
	
	public void setAsistenciaId(String value) {}

	public boolean isOperacion() {
		return operacion;
	}

	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
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

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public TimeZone getTimeZone() {
		return this.timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public boolean isAvanzadaBusquedaObras() {
		return avanzadaBusquedaObras;
	}

	public void setAvanzadaBusquedaObras(boolean avanzadaBusquedaObras) {
		this.avanzadaBusquedaObras = avanzadaBusquedaObras;
	}
}
