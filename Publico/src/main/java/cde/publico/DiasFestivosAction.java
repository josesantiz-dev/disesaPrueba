package cde.publico;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.DiasFestivos;
import net.giro.plataforma.logica.DiasFestivosRem;

import org.apache.log4j.Logger;

public class DiasFestivosAction implements Serializable {
	private static Logger log = Logger.getLogger(DiasFestivosAction.class);
	private static final long serialVersionUID = 1L;
	private InitialContext ctx;
	private LoginManager loginManager;
	private DiasFestivosRem ifzFestivos;
	private List<DiasFestivos> listDiasFestivos;
	private DiasFestivos diaFestivo;
	private String usuario;
	private int numPagina;
	private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;

	public DiasFestivosAction() throws Exception{
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			//this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			this.usuario = this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario();
			
			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;

			this.ctx = new InitialContext();
			this.ifzFestivos = (DiasFestivosRem) this.ctx.lookup("ejb:/Logica_Publico//DiasFestivosFac!net.giro.plataforma.logica.DiasFestivosRem");
			
			this.numPagina = 1;
			this.listDiasFestivos = new ArrayList<DiasFestivos>();
			this.diaFestivo = new DiasFestivos();
		} catch (Exception e) {
			log.error("Error en Publico.TopicsAction", e);
			this.ctx = null;
		}
	}

	
	public void buscar(){
		try {
			control();
			this.ifzFestivos.setInfoSesion(this.loginManager.getInfoSesion());
			this.listDiasFestivos = this.ifzFestivos.findAll(0);
			if (this.listDiasFestivos == null || this.listDiasFestivos.isEmpty())
				control(2, "La búsqueda no regresó resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Dias Festivos", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.diaFestivo = new DiasFestivos();
		} catch (Exception e) {
			control("Ocurrio un problema al generar el Dia Festivo nuevo", e);
		}
	}
	
	public void editar() {
		try {
			control();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el Dia Festivo", e);
		}
	}
	
	public void guardar() {
		try {
			control();
			if (! validaciones())
				return;

			this.diaFestivo.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			this.ifzFestivos.setInfoSesion(this.loginManager.getInfoSesion());
			if (this.diaFestivo == null || this.diaFestivo.getId() == null || this.diaFestivo.getId() <= 0L) {
				this.diaFestivo.setCreadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.diaFestivo.setFechaCreacion(Calendar.getInstance().getTime());
				this.diaFestivo.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.diaFestivo.setFechaModificacion(Calendar.getInstance().getTime());
				this.diaFestivo.setId(this.ifzFestivos.save(this.diaFestivo));
				this.listDiasFestivos.add(this.diaFestivo);
			} else {
				this.diaFestivo.setModificadoPor(this.loginManager.getInfoSesion().getAcceso().getUsuario().getId());
				this.diaFestivo.setFechaModificacion(Calendar.getInstance().getTime());
				this.ifzFestivos.update(this.diaFestivo);
			}
		} catch (Exception e) {
			control("Ocurrio un problema al guardar el Dia Festivo", e);
		}
	}

	public void eliminar() {
		try {
			control();
			if (this.diaFestivo != null || this.diaFestivo.getId() != null || this.diaFestivo.getId() > 0L) {
				this.ifzFestivos.setInfoSesion(this.loginManager.getInfoSesion());
				this.diaFestivo = this.ifzFestivos.cancelar(this.diaFestivo);
				for (DiasFestivos dia : this.listDiasFestivos) {
					if (dia.getId().longValue() != this.diaFestivo.getId().longValue()) 
						continue;
					dia = this.diaFestivo;
					break;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Dia Festivo", e);
		}
	}

	private boolean validaciones() {
		return true;
	}

	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Se ha producido un error desconocido";
		
		this.operacionCancelada = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		log.error("\nTopic :: " + this.usuario + "\n" + this.mensaje + "\n" + this.mensajeDetalles, throwable);
	}

	// -------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------------------------------------
	
	public List<DiasFestivos> getListDiasFestivos() {
		return listDiasFestivos;
	}

	public void setListDiasFestivos(List<DiasFestivos> listDiasFestivos) {
		this.listDiasFestivos = listDiasFestivos;
	}

	public DiasFestivos getDiaFestivo() {
		return diaFestivo;
	}

	public void setDiaFestivo(DiasFestivos diaFestivo) {
		this.diaFestivo = diaFestivo;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public boolean isOperacion() {
		return operacionCancelada;
	}

	public void setOperacion(boolean operacionCancelada) {
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

	public String getMensajeDetalles() {
		return mensajeDetalles;
	}

	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebug) {
		this.isDebug = isDebug;
	}
}
