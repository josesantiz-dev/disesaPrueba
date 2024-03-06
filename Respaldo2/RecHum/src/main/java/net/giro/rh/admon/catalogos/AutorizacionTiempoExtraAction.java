package net.giro.rh.admon.catalogos;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
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
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import net.giro.adp.beans.ObraExt;
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
	private InitialContext ctx;
	// Interfaces
	private ChecadorRem ifzChecador;
	private ChecadorDetalleRem ifzDetalles;
	private ObraRem ifzObras;
	private EmpleadoContratoRem ifzContratos;
	// POJO's
	private Checador pojoChecador;
	private ObraExt pojoObra;
	// Busqueda principal
	private List<Checador> listAsistencias;
	private List<ChecadorDetalle> listDetalles;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private boolean buscarPorFecha;
	private Date fechaBusqueda;
	private int numPaginaMain;
	// Variables de operacion
    private long usuarioId;
    private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	private int numPaginaDetalles;
	private TimeZone timeZone;
	// DEBUG
	private boolean isDebug;
	private Map<String,String> paramsRequest;
	
	
	public AutorizacionTiempoExtraAction() {
		Map<String,String> params = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
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
			this.ifzContratos = (EmpleadoContratoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			
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
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPaginaMain = 1;
			this.numPaginaDetalles = 1;
			
			this.timeZone = TimeZone.getTimeZone("America/Mazatlan");
			this.fechaBusqueda = Calendar.getInstance(this.timeZone).getTime();
		} catch (Exception e) {
			log.error("Error en constructor AsistenciasAction", e);
			this.ctx = null;
		}
	}

	
	public void buscar() throws Exception {
		try {
    		control();
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();

			if (this.listAsistencias == null)
				this.listAsistencias = new ArrayList<Checador>();
			this.listAsistencias.clear();
			this.numPaginaMain = 1;
			
			this.ifzChecador.orderBy("nombreObra, id desc");
			if ("fecha".equals(this.campoBusqueda))
				this.listAsistencias = this.ifzChecador.findByProperty(this.campoBusqueda, this.fechaBusqueda, 0);
			else
				this.listAsistencias = this.ifzChecador.findLikeProperty(this.campoBusqueda, this.valorBusqueda, 0);
			if (this.listAsistencias == null || this.listAsistencias.isEmpty()) {
				log.info("ERROR 2 - Busqueda sin resultados");
				control(2);
				return;
			}
    	} catch (Exception e) {
    		control("Ocurrio un problema al realizar la busqueda", e);
    	}
	}
	
	public void nuevo() {
		try {
    		control();
			this.pojoChecador = new Checador();
			this.pojoObra = null;
			if (this.listDetalles == null)
				this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetalles.clear();
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
			log.info("Proceso para recuperar asistencias.");
			if (this.pojoChecador == null) {
				nuevo();
				return;
			}
			
			if (this.listDetalles == null)
				this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetalles.clear();
			this.numPaginaDetalles = 1;
			
			// Recuperamos la obra
			log.info("Recuperando Obra asignada a Asistencia seleccionada ... ");
			if (this.pojoChecador.getIdObra() != null && this.pojoChecador.getIdObra() > 0L)
				this.pojoObra = this.ifzObras.findByIdExt(this.pojoChecador.getIdObra());
			
			// Recuperamos los detalles de las asistencias
			log.info("Recuperando asistencias (empleados) ... ");
			this.ifzDetalles.orderBy("nombreEmpleado");
			this.listDetalles = this.ifzDetalles.findByProperty("idChecador.id", this.pojoChecador.getId(), 0);
			if (this.listDetalles == null || this.listDetalles.isEmpty()) {
				this.listDetalles = new ArrayList<ChecadorDetalle>();
				log.info("Checador sin detalles");
				return;
			}

			log.info("Calculando horas extras ... ");
			for (ChecadorDetalle var : this.listDetalles) {
				if (var.getUsuarioAutoriza() > 0)
					continue;
				
				//if (var.getHorasExtra() == null || (var.getHorasExtra() != null && var.getHorasExtraAutorizadas() == null)) {
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
			}

			log.info("Proceso terminado.");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar las Asistencias de la Obra indicada", e);
		}
	}
	
	public void guardar() throws Exception {
		try {
    		control();
			if (this.pojoObra == null || this.pojoObra.getId() == null) {
				log.warn("ERROR 4 - Sin obra seleccionada");
	    		control(4);
				return;
			}
			
			log.info("Actualizando Checador ... ");
			this.pojoChecador.setEstatus(2); // Autorizado
			this.pojoChecador.setModificadoPor(this.usuarioId);
			this.pojoChecador.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
			this.ifzChecador.update(this.pojoChecador);
						
			// Guardamos los detalles del checador
			log.info("Guardando detalles de Checador ... ");
			for (ChecadorDetalle var : this.listDetalles) {
				if (var.getUsuarioAutoriza() > 0)
					continue;
				
				// Asignamos el checador
				var.setIdChecador(this.pojoChecador);
				var.setUsuarioAutoriza(this.usuarioId);
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance(this.timeZone).getTime());
				
				// Guardamos detalle en la BD
				this.ifzDetalles.update(var);
			}
			log.info("Proceso terminado.");
		} catch (Exception e) {
    		control("Ocurrio un problema al intentar guardar los registros", e);
	   	}
	}
	
	public long horasTrabajo(long idEmpleado) {
		GregorianCalendar timeEntrada = new GregorianCalendar(this.timeZone);
		GregorianCalendar timeSalida = new GregorianCalendar(this.timeZone);
		List<EmpleadoContrato> lista = null;
		EmpleadoContrato pojoContrato = null;
		long horas = 0;
		long minutos = 0;
		
		try {
			lista = this.ifzContratos.findByProperty("idEmpleado", idEmpleado);
			if (lista != null && ! lista.isEmpty()) {
				pojoContrato = lista.get(0);
				
				timeEntrada.setTime(pojoContrato.getHoraEntrada());
				timeSalida.setTime(pojoContrato.getHoraSalida());
				
				minutos = timeSalida.getTimeInMillis() - timeEntrada.getTimeInMillis();
				minutos = minutos / (1000 * 60);
				
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
		} catch (Exception e) {
			log.error("Ocurrio un problema al consultar el Contrato del empleado indicado", e);
			horas = 8;
		}

		return horas;
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

	public void busquedaPorFecha() {
		this.valorBusqueda = "";
		this.fechaBusqueda = Calendar.getInstance(this.timeZone).getTime();
		this.buscarPorFecha = ! this.buscarPorFecha;
	}

	private void control() {
		control(false, 0, "", null);
	}

	private void control(int tipoMensaje) {
		if (tipoMensaje == 0) {
			control();
			return;
		}
		
		control(true, tipoMensaje, "ERROR", null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje)) {
			control();
			return;
		}
		
		control(true, 1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje;
		this.mensajeDetalles = "";
		
		if (this.mensaje != null && this.mensaje.contains("\n"))
			this.mensaje = this.mensaje.replace("\n", "<br>");
		
		if (throwable != null) {
			StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
	}

	// ------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------

	public String getObraNombre() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L && this.pojoObra.getNombre() != null)
			return this.pojoObra.getId() + " - " + this.pojoObra.getNombre();
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

	public ObraExt getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(ObraExt pojoObra) {
		this.pojoObra = pojoObra;
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

