package net.giro.rh.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.TimeZone;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;

import net.giro.adp.beans.Obra;
import net.giro.adp.beans.ObraEmpleadoExt;
import net.giro.adp.logica.ObraEmpleadoRem;
import net.giro.adp.logica.ObraRem;
import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.ChecadorSemanalItem;
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
	private ChecadorRem ifzChecador;
	private ChecadorDetalleRem ifzDetalles;
	private ObraEmpleadoRem ifzObraEmpleados;
	private EmpleadoContratoRem ifzContrato;
	private List<ChecadorDetalle> listDetalles;
	private List<ChecadorDetalle> listDetallesLog;
	private int numPaginaDetalles;
	private int numPaginaDetallesLog;
	private boolean puedeEditar;
	private boolean buscarPorFecha;
	private Date fechaValor;
	private boolean perfilEgresos;
	private boolean buscarAdministrativo;
	private TimeZone timeZone;
	private boolean seleccionarTodos;
	// Busqueda principal
	private List<Checador> listAsistencias;
	private Checador pojoChecador;
	private long idChecador;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPaginaMain;
	// Busqueda de obras
	private ObraRem ifzObras;
	private List<Obra> listObras;
	private Obra pojoObra;
	private List<SelectItem> tiposBusquedaObras;	
	private String campoBusquedaObras;
	private String valorBusquedaObras;
	private int valorBusquedaTipoObra;
	private int numPaginaObras;
	// CARGA ASISTENCIA
	private byte[] fileSrc; 
	private String fileName;
	//private String fileExtension;
	private LinkedHashMap<String, String> layoutChecador;
	private List<ChecadorSemanalItem> asistenciasInvalidas;
	private int paginacionAsistenciasInvalidas;
	// Control
    private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	// DEBUG
	private boolean isDebug;
	private Map<String,String> paramsRequest;
	
	public AsistenciasAction() {
		PropertyResourceBundle props = null;
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		String valPerfil = "";
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());

			// Obtenemmos los parametros de la URL si corresponde
		    this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
		    	this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;

			// Layout Checador (Semanales)
			this.layoutChecador = new LinkedHashMap<String, String>();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{checador}", PropertyResourceBundle.class);
			props = (PropertyResourceBundle) ve.getValue(fc.getELContext());
	        for (String key : props.keySet())
	        	this.layoutChecador.put(key.toUpperCase(), props.getString(key));
		    
			ctx = new InitialContext();
			this.ifzChecador = (ChecadorRem) ctx.lookup("ejb:/Logica_RecHum//ChecadorFac!net.giro.rh.admon.logica.ChecadorRem");
			this.ifzDetalles = (ChecadorDetalleRem) ctx.lookup("ejb:/Logica_RecHum//ChecadorDetalleFac!net.giro.rh.admon.logica.ChecadorDetalleRem");
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObraEmpleados = (ObraEmpleadoRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraEmpleadoFac!net.giro.adp.logica.ObraEmpleadoRem");
			this.ifzContrato = (EmpleadoContratoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			
			this.ifzChecador.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObraEmpleados.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzContrato.setInfoSesion(this.loginManager.getInfoSesion());
			
			// PERFILES
			valPerfil = this.loginManager.getAutentificacion().getPerfil("EGRESOS_OPERACION");
			this.perfilEgresos = ((valPerfil != null && "S".equals(valPerfil)) ? true : false);
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusqueda.add(new SelectItem("nombreObra", "Obra"));
			this.tiposBusqueda.add(new SelectItem("idObra", "ID Obra"));
			this.tiposBusqueda.add(new SelectItem("nombreEmpleado", "Empleado"));
			this.tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPaginaMain = 1;
			
			// Busqueda obras
			this.tiposBusquedaObras = new ArrayList<SelectItem>();
			this.tiposBusquedaObras.add(new SelectItem("*", "Coincidencia"));
			this.tiposBusquedaObras.add(new SelectItem("nombre", "Obra"));
			this.tiposBusquedaObras.add(new SelectItem("nombreCliente", "Cliente"));
			this.tiposBusquedaObras.add(new SelectItem("id", "ID"));
			nuevaBusquedaObras();
			
			// Inicializaciones
			this.listAsistencias = new ArrayList<Checador>();
			this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetallesLog = new ArrayList<ChecadorDetalle>();
			this.listObras = new ArrayList<Obra>();
			this.pojoChecador = new Checador();
			this.numPaginaDetalles = 1;
			this.numPaginaDetallesLog = 1;
			this.puedeEditar = false;
			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
			this.fechaValor = Calendar.getInstance(this.timeZone).getTime();
			this.paginacionAsistenciasInvalidas = 1;
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
		}
	}

	public void buscar() {
		try {
			control();
			this.campoBusqueda = (this.campoBusqueda != null && ! "".equals(this.campoBusqueda.trim()) ? this.campoBusqueda.trim() : this.tiposBusqueda.get(0).getValue().toString());
			this.valorBusqueda = (this.valorBusqueda != null && ! "".equals(this.valorBusqueda.trim()) ? this.valorBusqueda.trim() : "");
			this.fechaValor = (this.fechaValor != null ? this.fechaValor : Calendar.getInstance(this.timeZone).getTime());
			
			this.ifzChecador.setInfoSesion(this.loginManager.getInfoSesion());
			if ("fecha".equals(this.campoBusqueda)) 
				this.listAsistencias = this.ifzChecador.findByProperty(this.campoBusqueda, this.fechaValor, "fecha desc, nombreObra, id desc", 0);
			else 
				this.listAsistencias = this.ifzChecador.findAsistencias(this.campoBusqueda, this.valorBusqueda, 0L, "", 0);//.findLikeProperty(this.campoBusqueda, this.valorBusqueda, "", 0);
			if (this.listAsistencias == null || this.listAsistencias.isEmpty()) 
				control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
    		control("Ocurrio un problema al consultar las Asistencias", e);
    	} finally {
			this.numPaginaMain = 1;
			this.listAsistencias = (this.listAsistencias != null ? this.listAsistencias : new ArrayList<Checador>());
    	}
	}
	
	public void nuevo() {
		try {
			control();
			this.puedeEditar = true;
			this.pojoChecador = new Checador();
			this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetallesLog = new ArrayList<ChecadorDetalle>();
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
			
			// Inicializaciones
			nuevaCarga();
			this.puedeEditar = false; // TODO: Validacion deshabilitado por pruebas y captura
			this.listDetallesLog = new ArrayList<ChecadorDetalle>();
			//this.fileName = this.pojoChecador.getNombreArchivo();
			
			// Recuperamos los detalles de las asistencias
			controlLog("Recuperando asistencias ... ");
			this.listDetalles = this.ifzDetalles.findAll(this.pojoChecador.getId(), "nombreEmpleado"); 
		} catch (Exception e) {
			control("Ocurrio un problema al intentar consultar los registros de la Asistencia indicada", e);
		} finally {
			this.numPaginaDetalles = 1;
			this.listDetalles = (this.listDetalles != null ? this.listDetalles : new ArrayList<ChecadorDetalle>());
    	}
	}
	
	public void guardar() {
		List<ChecadorDetalle> modificados = null;
		ChecadorDetalle asistenciaExistente = null;
		// --------------------------------------------------------------------
		GregorianCalendar timeEntrada = new GregorianCalendar(this.timeZone);
		GregorianCalendar timeSalida = new GregorianCalendar(this.timeZone);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String horaFormateada = "00:00";
		int horasTrabajadas = 0;
		int horasExtras = 0;
		long minutos = 0;
		
		try {
    		control();
    		if (! validaciones())
    			return;
			
			if (this.pojoChecador.getId() == null || this.pojoChecador.getId() <= 0L) {
				this.pojoChecador.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
				this.pojoChecador.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoChecador.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
				this.pojoChecador.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoChecador.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				
				// Guardamos el registro en la BD
				this.ifzChecador.setInfoSesion(this.loginManager.getInfoSesion());
				this.pojoChecador.setId(this.ifzChecador.save(this.pojoChecador));
				this.listAsistencias.add(this.pojoChecador);
			} else {
				this.pojoChecador.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				this.pojoChecador.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				
				// Actualizamos el registro en la BD
				this.ifzChecador.setInfoSesion(this.loginManager.getInfoSesion());
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
				
				// Calculamos Horas Extras
				if (horasTrabajadas > 8) {
					horasExtras = (int) (horasTrabajadas - horasTrabajo(var.getIdEmpleado()));
					dateFormatter.applyPattern("yyyy-MM-dd");
					horaFormateada = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
					horaFormateada += ((horasExtras < 10) ? "0" + horasExtras : horasExtras).toString() + ":" + ((minutos < 10) ? "0" + minutos : minutos).toString() + ":00";
					var.setHorasExtra(formatter.parse(horaFormateada));
				}
				
				if (modificados == null)
					modificados = new ArrayList<ChecadorDetalle>();
				
				// Guardamos detalle en la BD segun corresponda
				if (var.getId() != null && var.getId() > 0L) {
					var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					var.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
					modificados.add(var);
				} else {
					// Validamos que el dato de asistensia exista.
					asistenciaExistente = this.ifzDetalles.existeAsistenciaPojo(var.getIdChecador().getId(), var.getIdEmpleado(), var.getFecha());
					if (asistenciaExistente == null) {
						var.setCreadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						var.setFechaCreacion(Calendar.getInstance(this.timeZone).getTime());
						var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						var.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
						modificados.add(var);
					} else {
						asistenciaExistente.setHoraEntradaMarcada(var.getHoraEntradaMarcada());
						asistenciaExistente.setHoraSalidaMarcada(var.getHoraSalidaMarcada());
						asistenciaExistente.setTiempoAsistido(var.getTiempoAsistido());
						asistenciaExistente.setHorasTrabajadas(var.getHorasTrabajadas());
						asistenciaExistente.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
						asistenciaExistente.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
						modificados.add(asistenciaExistente);
					}
				}
			}
			
			if (modificados != null && ! modificados.isEmpty()) {
				this.ifzDetalles.setInfoSesion(this.loginManager.getInfoSesion());
				this.ifzDetalles.saveOrUpdateList(modificados);
			}
			
			nuevo();
		} catch (Exception e) {
    		control("Ocurrio un problema al intentar guardar las Asistencias", e);
	   	}
	}

	public void borrar() {
		Checador pojoChecadorBorrar = null;
		
		try {
			control();
			pojoChecadorBorrar = this.ifzChecador.findById(this.idChecador);
			if (pojoChecadorBorrar == null || pojoChecadorBorrar.getId() == null || pojoChecadorBorrar.getId() <= 0L) {
				control(-1, "Ocurrio un problema al intentar recuperar la Asistencia indicada");
				return;
			}

			// Buscamos la lista
			for (Checador lista : this.listAsistencias) {
				if (pojoChecadorBorrar.getId().longValue() == lista.getId().longValue()) {
					// Modificamos los datos
					lista.setEstatus(1);
					lista.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
					lista.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
					
					// Actualizamos el insumo
					this.ifzChecador.update(lista);
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar Borrar la Asistencia indicada", e);
		}
	}
	
	public void calcularHorasTrabajadas() {
		controlLog("-------> calcularHorasTrabajadas <-------"); 
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
	
	public void seleccionarTodos() {
		/*for (ChecadorDetalle item : this.listDetalles) {
			
		}*/
	}

	public long horasTrabajo(long idEmpleado) {
		GregorianCalendar timeEntrada = new GregorianCalendar(this.timeZone);
		GregorianCalendar timeSalida = new GregorianCalendar(this.timeZone);
		EmpleadoContrato pojoContrato = null;
		long horas = 8;
		long minutos = 0;
		
		try {
			// Recuperando contrato...
			controlLog("---> Contrato ... ");
			pojoContrato = this.ifzContrato.findContrato(idEmpleado);
			if (pojoContrato != null) {
				controlLog("---> Contrato: " + pojoContrato.getId() + " ... OK");
				timeEntrada.setTime(pojoContrato.getHoraEntrada());
				timeSalida.setTime(pojoContrato.getHoraSalida());
				
				minutos = timeSalida.getTimeInMillis() - timeEntrada.getTimeInMillis();
				minutos = minutos / (1000 * 60);
				horas = 0;
				while (minutos >= 60) {
					horas += 1;
					minutos = minutos - 60;
					if (minutos < 0)
						minutos = 0;
				}
				
				if (pojoContrato.getHoraEntradaComplemento() != null && pojoContrato.getHoraSalidaComplemento() != null) {
					timeEntrada.setTime(pojoContrato.getHoraEntradaComplemento());
					timeSalida.setTime(pojoContrato.getHoraSalidaComplemento());
					
					minutos = timeSalida.getTimeInMillis() - timeEntrada.getTimeInMillis();
					minutos = minutos / (1000 * 60);
					
					while (minutos >= 60) {
						horas += 1;
						minutos = minutos - 60;
						if (minutos < 0)
							minutos = 0;
					}
				}
			}
			
			controlLog("---> Contrato Empleado: " + horas + " horas");
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar el Contrato del empleado indicado. Horas default: 8", e);
			horas = 8;
		}

		return horas;
	}
	
	private boolean validaciones() {
		/*if (! this.puedeEditar)
		return false;*/

		if (this.pojoChecador.getIdEmpresa() == null || this.pojoChecador.getIdEmpresa() <= 0L)
			this.pojoChecador.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
		
		if (this.pojoChecador.getIdObra() == null || this.pojoChecador.getIdObra() <= 0L) {
			control(4, "Debe seleccionar una Obra");
			return false;
		}
		
		if (this.puedeEditar && ! "DATA_FROM_OBRA".equals(this.pojoChecador.getNombreArchivo()) && (this.fileName == null || "".equals(this.fileName))) {
			control(5, "Debe indicar el archivo de Asistencia");
			return false;
		}
		
		return true;
	}
	
	private ChecadorDetalle generaAsistencia(ObraEmpleadoExt item) {
		SimpleDateFormat dateFullFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		EmpleadoContrato pojoContrato = null;
		ChecadorDetalle asistencia = null;
		String dateString = "";
		
		try {
			asistencia = new ChecadorDetalle();
			asistencia.setIdEmpleado(item.getEmpleadoId());
			asistencia.setNombreEmpleado(item.getIdEmpleado().getNombrePorApellidos());
			asistencia.setFecha(this.pojoChecador.getFecha()); 
			
			// Recuperamos el Contrato activo del empleado
			pojoContrato = this.ifzContrato.findContrato(item.getEmpleadoId());
			if (pojoContrato != null) {
				// Entrada
				dateFormatter.applyPattern("yyyy-MM-dd");
				dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
				dateFormatter.applyPattern("HH:mm:ss");
				dateString += dateFormatter.format(pojoContrato.getHoraEntrada());
				asistencia.setHoraEntrada(dateFullFormatter.parse(dateString));
				asistencia.setHoraEntradaMarcada(dateFullFormatter.parse(dateString));

				// Salida
				dateFormatter.applyPattern("yyyy-MM-dd");
				dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
				dateFormatter.applyPattern("HH:mm:ss");
				dateString += dateFormatter.format(pojoContrato.getHoraSalida());
				asistencia.setHoraSalida(dateFullFormatter.parse(dateString));
				asistencia.setHoraSalidaMarcada(dateFullFormatter.parse(dateString));
				
				// Complemento ...
				if (pojoContrato.getHoraEntradaComplemento() != null && pojoContrato.getHoraSalidaComplemento() != null) {
					// Entrada
					dateFormatter.applyPattern("yyyy-MM-dd");
					dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
					dateFormatter.applyPattern("HH:mm:ss");
					dateString += dateFormatter.format(pojoContrato.getHoraEntradaComplemento());
					asistencia.setHoraEntradaComplemento(dateFullFormatter.parse(dateString));

					// Salida
					dateFormatter.applyPattern("yyyy-MM-dd");
					dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
					dateFormatter.applyPattern("HH:mm:ss");
					dateString += dateFormatter.format(pojoContrato.getHoraSalidaComplemento());
					asistencia.setHoraSalidaComplemento(dateFullFormatter.parse(dateString));
				}
			} else {
				// Entrada
				asistencia.setContratoInvalido(true);
				dateFormatter.applyPattern("yyyy-MM-dd");
				dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 08:00:00";
				asistencia.setHoraEntrada(dateFullFormatter.parse(dateString));
				asistencia.setHoraEntradaMarcada(dateFullFormatter.parse(dateString));

				// Salida
				dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 16:00:00";
				asistencia.setHoraSalida(dateFullFormatter.parse(dateString));
				asistencia.setHoraSalidaMarcada(dateFullFormatter.parse(dateString));
			}

			dateFormatter.applyPattern("yyyy-MM-dd");
			dateString = dateFormatter.format(this.pojoChecador.getFecha()) + " 08:00:00";
			asistencia.setTiempoAsistido(dateFullFormatter.parse(dateString));
			asistencia.setHorasTrabajadas(8);
		} catch (Exception e) {
			control("Ocurrio un problema al generar la Asistencia del Empleado " + item.getEmpleadoNombre() + " (" + item.getEmpleadoId() + ")", e);
			return null;
		}
		
		return asistencia;
	}

	private void control() { 
		this.operacion = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(int tipoMensaje, String mensaje) { 
		control(true, tipoMensaje, mensaje, null); 
	}
	
	private void control(String value, Throwable throwable) { 
		control(true, -1, value, throwable); 
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "Ocurrio un problema no controlado al realizar la operacion");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	private void controlLog(String mensaje) {
		mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim() : "???");
		log.info("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + mensaje + "\n");
	}
	
	// ------------------------------------------------------------
	// CARGA ASISTENCIA
	// ------------------------------------------------------------

	public void nuevaCarga() {
		control();
		this.fileSrc = null;
		this.fileName = "";
	}
	
	public void uploadListener(FileUploadEvent event) {
		try {
			control();
			this.fileSrc = event.getUploadedFile().getData();
			this.fileName = event.getUploadedFile().getName();
		} catch (Exception e) {
			control("Ocurrio un problema al leer el archivo de Asistencia", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void analizarArchivo() {
		Respuesta respuesta = null; 
		Date fecha = null;
		Obra obra = null;
		
		try {
			control();
			if (this.fileSrc == null)
				return;

			this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.fileName = (this.fileName != null && ! "".equals(this.fileName.trim()) ? this.fileName.trim() : "archivo");
			
			this.ifzDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzDetalles.importarAsistencia(this.fileName, this.fileSrc, this.layoutChecador);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				control(-1, respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				return;
			}

			this.paginacionAsistenciasInvalidas = 1;
			this.asistenciasInvalidas = (List<ChecadorSemanalItem>) respuesta.getBody().getValor("invalidos");
			if (this.asistenciasInvalidas != null && ! this.asistenciasInvalidas.isEmpty()) {
				control(7, "Algunos registros de asistencia tienen datos faltantes.");
				return;
			}

			// Comprobamos asistencias 
			this.listDetalles = (List<ChecadorDetalle>) respuesta.getBody().getValor("detalles");
			if (this.listDetalles == null || this.listDetalles.isEmpty()) {
				control(6, "Lista de asistencias vacia");
				return;
			}

			fecha = (Date) respuesta.getBody().getValor("fecha");
			fecha = (fecha != null ? fecha : Calendar.getInstance().getTime());
			this.pojoChecador.setFecha(fecha);
			this.pojoChecador.setNombreArchivo(this.fileName);

			// Asignamos Obra si corresponde
			obra = validaNombreParaObra(this.fileName);
			if (obra != null && obra.getId() != null && obra.getId() > 0L) {
				this.pojoChecador.setIdObra(obra.getId());
				this.pojoChecador.setNombreObra(obra.getNombre());
			}
		} catch (Exception e) {
			control("Ocurrio un problema al procesar el archivo de asistencia", e);
		} finally {
			this.fileSrc = null;
			this.fileName = "";
		}
	}
	
	private Obra validaNombreParaObra(String fileName) {
		char[] charArray = null;
		String valNumber = "";
		Obra obra = null;
		
		if (fileName == null || "".equals(fileName.trim()))
			return null;
		
		charArray = fileName.toCharArray();
		for (char caracter : charArray) {
			if (! StringUtils.isNumeric(String.valueOf(caracter)))
				break;
			valNumber += String.valueOf(caracter);
		}
		
		if (StringUtils.isNumeric(valNumber.trim())) 
			obra = this.ifzObras.findById(Long.valueOf(valNumber.trim()));
		return obra;
	}
	
	// ------------------------------------------------------------
	// BUSQUEDA OBRAS
	// ------------------------------------------------------------
	
	public void nuevaBusquedaObras() {
		control();
		this.campoBusquedaObras = this.tiposBusquedaObras.get(0).getValue().toString();
		this.valorBusquedaObras = "";
		this.valorBusquedaTipoObra = 0;
		this.numPaginaObras	= 1;
		
		this.listObras = new ArrayList<Obra>();
		this.pojoObra = null;
    }

	public void buscarObras() {
		try {
			control();
			this.campoBusquedaObras = (this.campoBusquedaObras != null && ! "".equals(this.campoBusquedaObras.trim()) ? this.campoBusquedaObras.trim() : this.tiposBusquedaObras.get(0).getValue().toString());
			this.valorBusquedaObras = (this.valorBusquedaObras != null && ! "".equals(this.valorBusquedaObras.trim()) ? this.valorBusquedaObras.trim() : "");
			this.valorBusquedaTipoObra = (this.buscarAdministrativo ? 4 : 0);
			
			this.listObras = this.ifzObras.findLikeProperty(this.campoBusquedaObras, this.valorBusquedaObras, this.valorBusquedaTipoObra, false, "nombre", 0);
			if (this.listObras == null || this.listObras.isEmpty()) 
				control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
			control("Ocurrio un problema al realizar la Busqueda de Obras", e);
    	} finally {
    		this.numPaginaObras	= 1;
			this.listObras = (this.listObras != null ? this.listObras : new ArrayList<Obra>());
    	}
	}
	
	public void seleccionarObra() {
		List<ObraEmpleadoExt> oEmpleados = null;
		ChecadorDetalle aux = null;
		
		try {
			control();
			if (this.pojoObra == null || this.pojoObra.getId() == null || this.pojoObra.getId() <= 0L) {
				control("Ocurrio un problema al recuperar la Obra seleccionada", null);
				return;
			}
			
			if (this.pojoChecador != null && this.pojoChecador.getIdObra() != null && this.pojoChecador.getIdObra() > 0L && this.pojoChecador.getIdObra().longValue() != this.pojoObra.getId().longValue()) {
				this.pojoChecador.setIdObra(0L);
				this.pojoChecador.setNombreObra("");
				this.pojoChecador.setNombreArchivo("");
				this.listDetalles = null;
			}
			
			// Asignamos datos de Obra
			this.pojoChecador.setIdObra(this.pojoObra.getId());
			this.pojoChecador.setNombreObra(this.pojoObra.getNombre());
			if (this.listDetalles != null && ! this.listDetalles.isEmpty()) {
				for (ChecadorDetalle asistencia : this.listDetalles) {
					if (this.pojoChecador != null && this.pojoChecador.getId() != null && this.pojoChecador.getId() > 0L)
						asistencia.setIdChecador(this.pojoChecador);
					asistencia.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
					asistencia.setFechaCreacion(Calendar.getInstance().getTime());
					asistencia.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
					asistencia.setFechaModificacion(Calendar.getInstance().getTime());
				}
				return;
			}
			
			// Recuperamos los empleados de la obra
			controlLog("Recuperando empleados ... ");
			oEmpleados = this.ifzObraEmpleados.findExtAll(this.pojoChecador.getIdObra());
			if (oEmpleados == null || oEmpleados.isEmpty()) {
				control("La obra seleccionada no tiene asignado ningun Empleado\nObra: " + this.pojoObra.getId() + " - " + this.pojoObra.getNombre(), null);
				this.pojoObra = null;
				return;
			}

			// Indicamos que los empleados se tomaron de la asignacion en Obra
			this.pojoChecador.setNombreArchivo("DATA_FROM_OBRA");
			Collections.sort(oEmpleados, new Comparator<ObraEmpleadoExt>() {
				@Override
				public int compare(ObraEmpleadoExt o1, ObraEmpleadoExt o2) {
					return o1.getIdEmpleado().getNombrePorApellidos().compareTo(o2.getIdEmpleado().getNombrePorApellidos());
				}
			});
			
			controlLog("Generando detalles para Asistencias (empleados) ... ");
			this.listDetalles = new ArrayList<ChecadorDetalle>();
			for (ObraEmpleadoExt empleado : oEmpleados) {
				if (empleado.getIdEmpleado().getEstatus() == 0) {
					aux = generaAsistencia(empleado); 
					if (aux != null)
						this.listDetalles.add(aux);
				}
			}

			nuevaBusquedaObras();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los Empleados de la Obra seleccionada", e);
		} 
	}

	// ------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------

	public String getObraNombre() {
		if (this.pojoChecador != null && this.pojoChecador.getIdObra() != null && this.pojoChecador.getIdObra() > 0L && this.pojoChecador.getNombreObra() != null)
			return this.pojoChecador.getIdObra() + " - " + this.pojoChecador.getNombreObra();
		return "";
	}
	
	public void setObraNombre(String value) {}

	public boolean getLogDetalles() {
		return ! (this.asistenciasInvalidas == null || this.asistenciasInvalidas.isEmpty());
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

	public long getIdChecador() {
		return idChecador;
	}

	public void setIdChecador(long idChecador) {
		this.idChecador = idChecador;
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

	public boolean isSeleccionarTodos() {
		return seleccionarTodos;
	}

	public void setSeleccionarTodos(boolean seleccionarTodos) {
		this.seleccionarTodos = seleccionarTodos;
	}

	public List<ChecadorSemanalItem> getAsistenciasInvalidas() {
		return asistenciasInvalidas;
	}

	public void setAsistenciasInvalidas(List<ChecadorSemanalItem> asistenciasInvalidas) {
		this.asistenciasInvalidas = asistenciasInvalidas;
	}

	public int getPaginacionAsistenciasInvalidas() {
		return paginacionAsistenciasInvalidas;
	}

	public void setPaginacionAsistenciasInvalidas(int paginacionAsistenciasInvalidas) {
		this.paginacionAsistenciasInvalidas = paginacionAsistenciasInvalidas;
	}
}
