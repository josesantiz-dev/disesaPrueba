package net.giro.rh.admon.catalogos;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.TimeZone;

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
import net.giro.rh.admon.beans.ChecadorExt;
import net.giro.rh.admon.beans.EmpleadoContrato;
import net.giro.rh.admon.logica.ChecadorDetalleRem;
import net.giro.rh.admon.logica.ChecadorRem;
import net.giro.rh.admon.logica.EmpleadoContratoRem;

@ViewScoped
@ManagedBean(name="extraAction")
public class AutorizacionTiempoExtraAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AutorizacionTiempoExtraAction.class);
	
	private InitialContext ctx;
	
	// Interfaces
	private ChecadorRem ifzChecador;
	private ChecadorDetalleRem ifzDetalles;
	private ObraRem ifzObras;
	private EmpleadoContratoRem ifzContratos;
	
	// POJO's
	private ChecadorExt pojoChecador;
	private ObraExt pojoObra;
	
	// Busqueda principal
	private List<ChecadorExt> listAsistencias;
	private List<ChecadorDetalle> listDetalles;
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	
	// Variables de operacion
    int usuarioId;
    String usuario;
	PropertyResourceBundle entornoProperties;
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	private int numPaginaMain;
	private int numPaginaDetalles;
	private int idConsecutivo;
	
	
	public AutorizacionTiempoExtraAction() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
			ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			
			this.usuarioId = 0;
			this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = lm.getUsuarioResponsabilidad().getUsuario().getUsuario();
			this.entornoProperties = (PropertyResourceBundle) dato.getValue(fc.getELContext());

			this.ctx = new InitialContext();
			this.ifzChecador = (ChecadorRem) this.ctx.lookup("ejb:/Logica_RecHum//ChecadorFac!net.giro.rh.admon.logica.ChecadorRem");
			this.ifzDetalles = (ChecadorDetalleRem) this.ctx.lookup("ejb:/Logica_RecHum//ChecadorDetalleFac!net.giro.rh.admon.logica.ChecadorDetalleRem");
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzContratos = (EmpleadoContratoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoContratoFac!net.giro.rh.admon.logica.EmpleadoContratoRem");
			
			listAsistencias = new ArrayList<ChecadorExt>();
			listDetalles = new ArrayList<ChecadorDetalle>();
			
			pojoChecador = new ChecadorExt();
			
			// Busqueda principal
			tiposBusqueda = new ArrayList<SelectItem>();
			tiposBusqueda.add(new SelectItem("nombreObra", "Obra"));
			tiposBusqueda.add(new SelectItem("fecha", "Fecha"));
			campoBusqueda = tiposBusqueda.get(0).getValue().toString();
			valorBusqueda = "";
			numPaginaMain = 1;
			numPaginaDetalles = 1;
		} catch (Exception e) {
			log.error("Error en constructor AsistenciasAction", e);
			this.ctx = null;
		}
	}

	
	public void buscar() throws Exception {
		try {
    		this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.valorBusqueda))
				this.campoBusqueda = tiposBusqueda.get(0).getValue().toString();
			
			this.listAsistencias = this.ifzChecador.findExtLikeProperty(this.campoBusqueda, this.valorBusqueda, 120);
			if(this.listAsistencias.isEmpty()){
	    		this.operacion = true;
				this.tipoMensaje = 2;
				this.mensaje = "ERROR";
				return;
			}
    	} catch (Exception e) {
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en buscar", e);
    		e.printStackTrace();
    	}
	}
	
	public void nuevo() {
		try {
    		this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoChecador = new ChecadorExt();
			this.pojoObra = null;
			if (this.listDetalles == null)
				this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetalles.clear();
		} catch (Exception e) {
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en nuevo", e);
    		e.printStackTrace();
	   	}
	}
	
	public void ver() {
		try {
			this.operacion = false;
			this.mensaje = "";
			this.tipoMensaje = 0;
			this.idConsecutivo = 0;
			
			if (this.pojoChecador == null) {
				nuevo();
				return;
			}
			
			if (this.listDetalles == null)
				this.listDetalles = new ArrayList<ChecadorDetalle>();
			this.listDetalles.clear();
			
			// Recuperamos la obra
			if (this.pojoChecador.getIdObra() != null && this.pojoChecador.getIdObra() > 0L)
				this.pojoObra = this.ifzObras.findByIdExt(this.pojoChecador.getIdObra());
			
			// Recuperamos los detalles de las asistencias
			this.listDetalles = this.ifzDetalles.findByProperty("idChecador.id", this.pojoChecador.getId(), 0);
			if (this.listDetalles == null || this.listDetalles.isEmpty()) {
				this.listDetalles = new ArrayList<ChecadorDetalle>();
				return;
			}

			GregorianCalendar t1 = new GregorianCalendar();
			GregorianCalendar t2 = new GregorianCalendar();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			String horaFormateada = "00:00";
			
			for (ChecadorDetalle var : this.listDetalles) {
				if (var.getHorasExtra() == null || (var.getHorasExtra() != null && var.getHorasExtraAutorizadas() == null)) {
					// calculamos el tiempo extra si no esta establecido
					t1.setTime(var.getHoraSalidaMarcada());
					t2.setTime(var.getHoraEntradaMarcada());
					
					// Generamos el tiempo extra
					long horas = Math.abs(t1.getTimeInMillis() - t2.getTimeInMillis()) / 3600000;
					long mins  = Math.abs(60 - t2.get(Calendar.MINUTE) + t1.get(Calendar.MINUTE));
					if (mins == 60) mins = 0;
					if (mins > 60) mins = mins - 60;
					
					// Asignamos el tiempo asistido
					//horaFormateada = ((horas < 10) ? "0" + horas : horas).toString() + ":" + ((mins < 10) ? "0" + mins : mins).toString();
					//var.setTiempoAsistido(formatter.parse(horaFormateada));
					
					// restamos el horario del empleado
					horas = horas - horasTrabajo(var.getIdEmpleado());
					if (horas <= 0) horas = 0;
					
					// Asignamos el tiempo extra generado
					horaFormateada = ((horas < 10) ? "0" + horas : horas).toString() + ":" + ((mins < 10) ? "0" + mins : mins).toString();
					var.setHorasExtra(formatter.parse(horaFormateada));
					
					// tiempo extra sin minutos para autorizacion
					mins = 0;
					horaFormateada = ((horas < 10) ? "0" + horas : horas).toString() + ":" + ((mins < 10) ? "0" + mins : mins).toString();
					var.setHorasExtraAutorizadas(formatter.parse(horaFormateada));
					var.setUsuarioAutoriza(0);
				}
			}
		} catch (Exception e) {
			this.operacion = true;
			this.tipoMensaje = 1;
			this.mensaje = "ERROR";
			log.error("Error en ver", e);
		}
	}
	
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
			
			this.pojoChecador.setModificadoPor(this.usuarioId);
			this.pojoChecador.setFechaModificacion(Calendar.getInstance().getTime());
			Checador pojoChecadorAux = this.ifzChecador.convertChecadorExtToChecador(pojoChecador);
						
			// Guardamos los detalles del checador
			for (ChecadorDetalle var : this.listDetalles) {
				// Asignamos el checador
				var.setIdChecador(pojoChecadorAux);
				var.setUsuarioAutoriza(this.usuarioId);
				var.setModificadoPor(this.usuarioId);
				var.setFechaModificacion(Calendar.getInstance().getTime());
				
				// Guardamos detalle en la BD
				this.ifzDetalles.update(var);
			}
		} catch (Exception e) {
    		this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
    		log.error("Error en guardar", e);
    		e.printStackTrace();
	   	}
	}
	
	public TimeZone getTimeZone() {  
		TimeZone timeZone = TimeZone.getDefault();  
		return timeZone;
	}

	public String getObraNombre() {
		if (this.pojoObra != null && this.pojoObra.getId() != null && this.pojoObra.getId() > 0L && this.pojoObra.getNombre() != null)
			return this.pojoObra.getNombre();
		return "";
	}
	
	public void setObraNombre(String value) {}
	
	public int horasTrabajo(long idEmpleado) {
		List<EmpleadoContrato> lista = this.ifzContratos.findByProperty("idEmpleado", idEmpleado);
		if (lista != null && ! lista.isEmpty()) {
			long horas = 0L;
			GregorianCalendar t1 = new GregorianCalendar();
			GregorianCalendar t2 = new GregorianCalendar();
			EmpleadoContrato pojoContrato = lista.get(0);
			
			t1.setTime(pojoContrato.getHoraEntrada());
			t2.setTime(pojoContrato.getHoraSalida());
			horas = Math.abs(t2.getTimeInMillis() - t1.getTimeInMillis()) / 3600000;
			
			if (pojoContrato.getHoraEntradaComplemento() != null && pojoContrato.getHoraSalidaComplemento() != null) {
				t1.setTime(pojoContrato.getHoraEntradaComplemento());
				t2.setTime(pojoContrato.getHoraSalidaComplemento());
				
				horas += Math.abs(t2.getTimeInMillis() - t1.getTimeInMillis()) / 3600000;
			}
			
			return (int) horas;
		}

		return 8;
	}
	
	public void valueToItem() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
		String value = params.get("valueItem");
		String hora = "";
		int index = -1;
		
		if (value != null && !"".equals(value)) {
			if (value.contains("-")) {
				try {
					String[] splitted = value.split("-");
					hora = splitted[1];
					splitted = splitted[0].split(":");
					value = splitted[splitted.length - 1];
					value = value.replace("item", "");
					index = Integer.parseInt(value);
					if (index > 0) index -= 1;
					if (index < 0) return;
					
					// Actualizamos el nuevo valor en la lista donde corresponda
					this.listDetalles.get(index).setHorasExtraAutorizadas(formatter.parse(hora));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// ------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------
	
	public String getItemId() {
		this.idConsecutivo += 1;
		return "item" + String.valueOf(this.idConsecutivo);
	}
	
	public void setItemId(String value) {}
	
	public String getPrevItemId() {
		return "item" + this.idConsecutivo;
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

	public List<ChecadorExt> getListAsistencias() {
		return listAsistencias;
	}

	public void setListAsistencias(List<ChecadorExt> listAsistencias) {
		this.listAsistencias = listAsistencias;
	}

	public List<ChecadorDetalle> getListDetalles() {
		return listDetalles;
	}

	public void setListDetalles(List<ChecadorDetalle> listDetalles) {
		this.listDetalles = listDetalles;
	}

	public ChecadorExt getPojoChecador() {
		return pojoChecador;
	}

	public void setPojoChecador(ChecadorExt pojoChecador) {
		this.pojoChecador = pojoChecador;
	}

	public ObraExt getPojoObra() {
		return pojoObra;
	}

	public void setPojoObra(ObraExt pojoObra) {
		this.pojoObra = pojoObra;
	}
}

/* HISTORIAL DE MODIFICACIONES 
 * ----------------------------------------------------------------------------------------------------------------
 *  VERSIÓN |    FECHA   | 			 AUTOR 			 | DESCRIPCIÓN 
 * ----------------------------------------------------------------------------------------------------------------
 * 	  1.2	| 2016-09-20 | Javier Tirado 			 | Se corrigio la actualizacion de las horas extras autorizadas en el listado.
 *    3.1   | 2017-01-24 | Javier Tirado 			 | Añado confirmacion visual para la autorizacion de horas extras.
 */

