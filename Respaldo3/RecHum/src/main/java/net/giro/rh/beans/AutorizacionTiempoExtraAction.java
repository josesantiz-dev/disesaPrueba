package net.giro.rh.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.logica.ObraRem;
import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.Checador;
import net.giro.rh.admon.beans.ChecadorDetalle;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.logica.ChecadorDetalleRem;
import net.giro.rh.admon.logica.ChecadorRem;
import net.giro.rh.admon.logica.EmpleadoContratoRem;

@ViewScoped
@ManagedBean(name="extraAction")
public class AutorizacionTiempoExtraAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AutorizacionTiempoExtraAction.class);
	private LoginManager loginManager;
	// Interfaces
	private ObraRem ifzObras;
	private ChecadorRem ifzChecador;
	private ChecadorDetalleRem ifzDetalles;
	private EmpleadoContratoRem ifzContratos;
	// POJO's
	private Checador pojoChecador;
	private List<Checador> listAsistencias;
	private List<ChecadorDetalle> listDetalles;
	private int numPaginaMain;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private boolean buscarPorFecha;
	private Date fechaBusqueda;
	// Variables de operacion
    private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	private int numPaginaDetalles;
	private TimeZone timeZone;
	// DEBUG
	private boolean isDebug;
	private Map<String,String> paramsRequest;
	
	public AutorizacionTiempoExtraAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		
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

			ctx = new InitialContext();
			this.ifzChecador = (ChecadorRem) ctx.lookup("ejb:/Logica_RecHum//ChecadorFac!net.giro.rh.admon.logica.ChecadorRem");
			this.ifzDetalles = (ChecadorDetalleRem) ctx.lookup("ejb:/Logica_RecHum//ChecadorDetalleFac!net.giro.rh.admon.logica.ChecadorDetalleRem");
			this.ifzObras = (ObraRem) ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzContratos = (EmpleadoContratoRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			
			this.ifzChecador.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzObras.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzContratos.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.listAsistencias = new ArrayList<Checador>();
			this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.pojoChecador = new Checador();
			
			// Busqueda principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("nombreObra", "Obra"));
			this.tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
			this.tiposBusqueda.add(new SelectItem("idObra", "ID Obra"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPaginaMain = 1;
			this.numPaginaDetalles = 1;
			
			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
			this.fechaBusqueda = Calendar.getInstance(this.timeZone).getTime();
		} catch (Exception e) {
			log.error("Error en constructor AsistenciasAction", e);
		}
	}
	
	public void buscar() {
		try {
    		control();
			this.campoBusqueda = (this.campoBusqueda != null && ! "".equals(this.campoBusqueda.trim()) ? this.campoBusqueda.trim() : this.tiposBusqueda.get(0).getValue().toString());
			this.valorBusqueda = (this.valorBusqueda != null && ! "".equals(this.valorBusqueda.trim()) ? this.valorBusqueda.trim() : "");
			this.fechaBusqueda = (this.fechaBusqueda != null ? this.fechaBusqueda : Calendar.getInstance(this.timeZone).getTime());
			
			this.ifzChecador.setInfoSesion(this.loginManager.getInfoSesion());
			if ("fecha".equals(this.campoBusqueda))
				this.listAsistencias = this.ifzChecador.findByProperty(this.campoBusqueda, this.fechaBusqueda, "nombreObra, id desc", 0);
			else
				this.listAsistencias = this.ifzChecador.findLikeProperty(this.campoBusqueda, this.valorBusqueda, "", 0);
			if (this.listAsistencias == null || this.listAsistencias.isEmpty()) 
				control(2, "Busqueda sin resultados");
    	} catch (Exception e) {
    		control("Ocurrio un problema al realizar la busqueda", e);
    	} finally {
			this.numPaginaMain = 1;
			this.listAsistencias = (this.listAsistencias != null ? this.listAsistencias : new ArrayList<Checador>());
    	}
	}
	
	public void nuevo() {
		try {
    		control();
			this.pojoChecador = new Checador();
			this.listDetalles = new ArrayList<ChecadorDetalle>();
		} catch (Exception e) {
    		control("Ocurrio un problema al generar un nuevo Registro", e);
	   	}
	}
	
	public void ver() {
		GregorianCalendar timeEntrada = new GregorianCalendar(this.timeZone);
		GregorianCalendar timeSalida = new GregorianCalendar(this.timeZone);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String horaFormateada = "00:00";
		long horasContrato = 0;
		long horas = 0;
		long minutos = 0;
		
		try {
			control();
			controlLog("Recuperando Asistencias ... Inicio de proceso!");
			if (this.pojoChecador == null || this.pojoChecador.getId() == null || this.pojoChecador.getId() <= 0L) {
				control(-1, "Debe indicar una lista de Asistencia");
				return;
			}
			
			// Recuperamos los detalles de las asistencias
			controlLog("Recuperando asistencias (empleados) ... ");
			this.ifzDetalles.setInfoSesion(this.loginManager.getInfoSesion());
			this.listDetalles = this.ifzDetalles.findAll(this.pojoChecador.getId(), "nombreEmpleado");
			if (this.listDetalles == null || this.listDetalles.isEmpty()) {
				control(-1, "La Lista de Asistencia seleccionada no tiene asignado ningun Empleado.\nPor favor contacte a su Administrador");
				return;
			}

			controlLog("Calculando horas extras ... ");
			for (ChecadorDetalle var : this.listDetalles) {
				controlLog("---> Empleado " + var.getIdEmpleado() + " ... ");
				if (var.getUsuarioAutoriza() > 0) {
					controlLog("---> Empleado Autorizado");
					continue;
				}
				
				// calculamos el tiempo extra si no esta establecido
				timeEntrada.setTime(var.getHoraEntradaMarcada());
				timeSalida.setTime(var.getHoraSalidaMarcada());
				minutos = timeSalida.getTimeInMillis() - timeEntrada.getTimeInMillis();
				minutos = minutos / (1000 * 60);
				
				if (minutos <= 0) {
					dateFormatter.applyPattern("yyyy-MM-dd");
					horaFormateada = dateFormatter.format(this.pojoChecador.getFecha()) + " 00:00:00";
					var.setTiempoAsistido(formatter.parse(horaFormateada));
					var.setHorasExtra(formatter.parse(horaFormateada));
					var.setHorasExtraAutorizadas(formatter.parse(horaFormateada));
					var.setUsuarioAutoriza(0);
					controlLog("---> Empleado sin tiempo extra");
					continue;
				}
				
				horas = 0;
				while (minutos >= 60) {
					horas += 1;
					minutos = minutos - 60;
					if (minutos < 0)
						minutos = 0;
				}
				
				// Asignamos Tiempo Asistido 
				dateFormatter.applyPattern("yyyy-MM-dd");
				horaFormateada = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
				horaFormateada += ((horas < 10) ? "0" + horas : horas).toString() + ":" + ((minutos < 10) ? "0" + minutos : minutos).toString() + ":00";
				var.setTiempoAsistido(formatter.parse(horaFormateada));

				// restamos el horario (horas al dia) del empleado
				horasContrato = horasTrabajo(var.getIdEmpleado());
				controlLog("---> Calculando tiempo extra ...");
				horas = horas - horasContrato;
				if (horas < 0) {
					horas = 0;
					minutos = 0;
				}
				
				// Asignamos el tiempo extra generado
				horaFormateada = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
				horaFormateada += ((horas < 10) ? "0" + horas : horas).toString() + ":" + ((minutos < 10) ? "0" + minutos : minutos).toString() + ":00";
				var.setHorasExtra(formatter.parse(horaFormateada));
				
				// tiempo extra sin minutos para autorizacion
				horaFormateada = dateFormatter.format(this.pojoChecador.getFecha()) + " ";
				horaFormateada += ((horas < 10) ? "0" + horas : horas).toString() + ":00:00";
				var.setHorasExtraAutorizadas(formatter.parse(horaFormateada));
				var.setUsuarioAutoriza(0);
				controlLog("---> Tiempo Extra: " + horas + " horas");
			}

			controlLog("Proceso terminado.");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Asistencias de la Obra indicada", e);
		} finally {
			this.numPaginaDetalles = 1;
			this.listDetalles = (this.listDetalles != null ? this.listDetalles : new ArrayList<ChecadorDetalle>());
		}
	}
	
	public void guardar() {
		try {
    		control();
			controlLog("\n\nAUTH-EXTRA :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: Guardando Autorizacion Tiempo Extra (" + this.pojoChecador.getId() + ") ... Inicio de proceso!");

    		controlLog("Validando datos ... ");
    		if (! validaciones())
    			return;
			
			controlLog("Actualizando Checador ... ");
			this.pojoChecador.setEstatus(2); // Autorizado
			this.pojoChecador.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
			this.pojoChecador.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
			this.ifzChecador.update(this.pojoChecador);
			
			// Guardamos los detalles del checador
			controlLog("Guardando detalles de Checador ... ");
			for (ChecadorDetalle var : this.listDetalles) {
				if (var.getUsuarioAutoriza() > 0)
					continue;
				
				// Asignamos el checador
				var.setIdChecador(this.pojoChecador);
				var.setUsuarioAutoriza(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				var.setModificadoPor(this.loginManager.getUsuarioResponsabilidad().getUsuario().getId());
				var.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				
				// Guardamos detalle en la BD
				this.ifzDetalles.update(var);
			}
			controlLog("Proceso terminado.");
		} catch (Exception e) {
    		control("Ocurrio un problema al intentar guardar los registros", e);
	   	}
	}
	
	private boolean validaciones() {
		controlLog("Validando Obra ... ");
		if (this.pojoChecador == null || this.pojoChecador.getIdObra() == null || this.pojoChecador.getId() <= 0L) {
			log.warn("ERROR 4 - Sin obra seleccionada");
    		control(4, "Debe seleccionar una Obra");
			return false;
		}

		controlLog("Validando Detalles ... ");
		if (this.listDetalles == null || this.listDetalles.isEmpty()) {
			this.listDetalles = new ArrayList<ChecadorDetalle>();
			log.warn("ERROR INTERNO - Asistencia sin ningun Empleado");
			control(-1, "La Lista de Asistencia seleccionada no tiene asignado ningun Empleado.\nPor favor contacte a su Administrador");
			return false;
		}

		controlLog("Validando Empresa ... ");
		if (this.pojoChecador.getIdEmpresa() == null || this.pojoChecador.getIdEmpresa() <= 0L)
			this.pojoChecador.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
		
		return true;
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
			pojoContrato = this.ifzContratos.findContrato(idEmpleado);
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
	
	public void busquedaPorFecha() {
		this.valorBusqueda = "";
		this.fechaBusqueda = Calendar.getInstance(this.timeZone).getTime();
		this.buscarPorFecha = ! this.buscarPorFecha;
	}

	public void asignaHorasExtras(AjaxBehaviorEvent event) {
		String horasExtras = "";
		int index = -1;
		
		if (event.getComponent().getAttributes().containsKey("indexAsignaHorasExtras"))
			index = (int) event.getComponent().getAttributes().get("indexAsignaHorasExtras");

		if (event.getComponent().getAttributes().containsKey("horasExtras"))
			horasExtras = event.getComponent().getAttributes().get("horasExtras").toString();
		
		controlLog("Index : " + index + ", Value(Horas Extras) : " + horasExtras);
	}
	
	private void control() {
		this.operacion = false;
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
	// PROPIEDADES
	// ------------------------------------------------------------

	public String getTitulo() {
		if (this.pojoChecador != null && this.pojoChecador.getId() != null && this.pojoChecador.getId() > 0L)
			return " " + this.pojoChecador.getId();
		return "";
	}
	
	public void setTitulo(String value) {}
	
	public String getObraNombre() {
		if (this.pojoChecador != null && this.pojoChecador.getIdObra() != null && this.pojoChecador.getIdObra() > 0L && this.pojoChecador.getNombreObra() != null)
			return this.pojoChecador.getIdObra() + " - " + this.pojoChecador.getNombreObra();
		return "";
	}
	
	public void setObraNombre(String value) {}
	
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

	public Checador getPojoChecador() {
		return pojoChecador;
	}

	public void setPojoChecador(Checador pojoChecador) {
		this.pojoChecador = pojoChecador;
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

	public boolean isBuscarPorFecha() {
		return buscarPorFecha;
	}
	
	public void setBuscarPorFecha(boolean buscarPorFecha) {
		this.buscarPorFecha = buscarPorFecha;
	}
	
	public Date getFechaBusqueda() {
		return fechaBusqueda;
	}
	
	public void setFechaBusqueda(Date fechaBusqueda) {
		this.fechaBusqueda = fechaBusqueda;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  1.2	| 2016-09-20 | Javier Tirado 			 | Se corrigio la actualizacion de las horas extras autorizadas en el listado.
 *    3.1   | 2017-01-24 | Javier Tirado 			 | Añado confirmacion visual para la autorizacion de horas extras.
 */

