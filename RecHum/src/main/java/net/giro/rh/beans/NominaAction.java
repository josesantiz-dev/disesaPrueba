package net.giro.rh.beans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Set;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.respuesta.Respuesta;
import net.giro.rh.admon.beans.EmpleadoNomina;
import net.giro.rh.admon.beans.EmpleadoNominaEstatus;
import net.giro.rh.admon.beans.NominaQuincenalItem;
import net.giro.rh.admon.logica.EmpleadoNominaRem;

@ViewScoped
@ManagedBean(name="nominaAction")
public class NominaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(NominaAction.class);
	private HttpSession httpSession;
	private LoginManager loginManager;
	private PropertyResourceBundle entornoProperties;
	// --------------------------------------------------------------
	private EmpleadoNominaRem ifzEmpleadoNomina;
	private ReportesRem	ifzReportes;
	private Date fechaDesde;
	private Date fechaHasta;
	private boolean nominaPreliminar;
	private boolean recalcularNomina;
	private long idNominaEstatus;
	private boolean procesando;
	private double stayTime;
	private String dowDesde;
	private String dowHasta;
	// Nomina Quincenal ---------------------------------------------
	private Date fechaDesdeQuincenal;
	private Date fechaHastaQuincenal;
	private byte[] fileSrc; 
	private String fileName;
	private LinkedHashMap<String, String> layoutNominaQuincenal;
	private List<NominaQuincenalItem> listInformacionFaltante;
	private int paginacionInformacionFaltante;
	private boolean cargaRetroactiva;
	private boolean permitirSinSueldo;
	// Control ------------------------------------------------------
	private boolean operacion;
	private int tipoMensaje;
	private String mensaje;
	// DEBUG --------------------------------------------------------
	private boolean isDebug;
	private Map<String,String> paramsRequest;
	
	public NominaAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
		PropertyResourceBundle props = null;
		List<String> keys = null;
		String value = "";
	
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);
			
			this.paramsRequest = new HashMap<String, String>();
		    for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
		    	this.paramsRequest.put(item.getKey().trim().toUpperCase(), item.getValue().trim().toUpperCase());
		    // Comprobamos si requerimos levantar la variable DEBUG
		    this.isDebug = (this.paramsRequest.containsKey("DEBUG") || this.paramsRequest.containsKey("debug")) ? true : false;

			// Interfaces
			ctx = new InitialContext();
			this.ifzEmpleadoNomina = (EmpleadoNominaRem) ctx.lookup("ejb:/Logica_RecHum//EmpleadoNominaFac!net.giro.rh.admon.logica.EmpleadoNominaRem");
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			this.ifzEmpleadoNomina.setInfoSesion(this.loginManager.getInfoSesion());
			
			// Layout Nomina Quincenal
			this.layoutNominaQuincenal = new LinkedHashMap<String, String>();
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{quincenal}", PropertyResourceBundle.class);
			props = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			keys = ordenaKeySet(props.keySet());
	        for (String key : keys)
	        	this.layoutNominaQuincenal.put(key.toUpperCase(), props.getString(key));

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) ve.getValue(fc.getELContext());
			
			value = this.loginManager.getPerfil("SYS_SEMANA_LABORAL_OBRA_INICIO");
			this.dowDesde = (value != null ? value.trim() : "");

			value = this.loginManager.getPerfil("SYS_SEMANA_LABORAL_OBRA_FIN");
			this.dowHasta = (value != null ? value.trim() : "");
			
	        // Inicializaciones
			this.recalcularNomina = false;
			this.stayTime = -1;
			nuevo();
		} catch (Exception e) {
			log.error("Ocurrio un problema al inicializar " + this.getClass().getCanonicalName(), e);
		}
	}
	
	public void buscar() {
		try {
			control();
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los registros de Nominas", e);
		}
	}
	
	public void nuevo() {
		Calendar cal = Calendar.getInstance();
		
		try {
			control();
			this.fechaHasta = getPrevEndDayOfWeek(Calendar.getInstance().getTime());
			cal.setTime(this.fechaHasta);
			cal.add(Calendar.DAY_OF_YEAR, -6);
			this.fechaDesde = cal.getTime(); //getIniDayOfWeek(this.fechaHasta);
		} catch (Exception e) {
			control("Ocurrio un problema al inicializar un nuevo registro", e);
		}
	}
	
	public void editar() {
		try {
			control();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el registro para editar", e);
		}
	}
	
	public void borrar() {
		try {
			control();
		} catch (Exception e) {
			control("Ocurrio un problema al intentar borrar el registro indicado", e);
		}
	}

	public void cancelar() {
    	try {
			control();
    	} catch (Exception e) {
			control("Ocurrio un problema al intentar cancelar el registro indicado", e);
    	}
    }
	
	public void salvar() {
		Respuesta res = null;
		
		try {
			control();
			if (! validarCalculoNomina()) 
				return;
			
			// Generamos las registros de nomina
			this.idNominaEstatus = 0L;
			this.ifzEmpleadoNomina.setInfoSesion(this.loginManager.getInfoSesion());
			res = this.ifzEmpleadoNomina.nominaSemanal(this.fechaDesde, this.fechaHasta, this.recalcularNomina, this.nominaPreliminar);
			if (res.getErrores().getCodigoError() > 0L) {
				if (res.getErrores().getErrores() != null && ! res.getErrores().getErrores().isEmpty() && res.getErrores().getErrores().get(0).getCodigoError() == 5L) {
					control(5, "No se encontraron asistencias en el periodo especificado");
					return;
				} 
				
				log.info("ERROR INTERNO: " + res.getErrores().getCodigoError() + " - " + res.getErrores().getDescError());
				control(res.getErrores().getCodigoError() + " - " + res.getErrores().getDescError());
				return;
			}

			this.stayTime = 3000;
			this.recalcularNomina = false;
			this.idNominaEstatus = (long) res.getBody().getValor("idNominaEstatus");
			log.info("Proceso Nomina: " + this.idNominaEstatus);
			control(10, "Calculo de Nomina iniciado.\nEspere mientras el proceso termina.\n\nProceso " + this.idNominaEstatus);
			this.procesando = true;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar ejecutar el proceso de Nomina", e);
		}
	}

	public void confirmaRecalculo() {
		try {
			this.recalcularNomina = true;
			this.salvar();
			this.recalcularNomina = false;
		} catch (Exception e) {
			log.error("Error en RecHum.EmpleadosNominaAction.confirmaRecalculo", e);
		}
	}
	
	public void comprobarPeticion() {
		int estatusPeticion = 0;
		
    	try {
			control();
			estatusPeticion = this.ifzEmpleadoNomina.comprobarCalculoNomina(this.idNominaEstatus);
			if (estatusPeticion == 2) {
				this.procesando = false;
				reporte();
				return;
			}
			
			if (estatusPeticion == 1) {
				control(-1, "Ocurrio un problema con el Calculo de Nomina.\n\nProceso " + this.idNominaEstatus);
				this.procesando = false;
				return;
			}

			this.procesando = true;
    	} catch (Exception e) {
			control("Ocurrio un problema al comprobar la peticion de Calculo de Nomina", e);
    	}
	}

	public void autoasignaFechaHasta() {
		if (this.fechaDesde != null)
			this.fechaHasta = getEndDayOfWeek(this.fechaDesde);
	}
	
	private void reporte() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		EmpleadoNominaEstatus estatus = null;
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			if (this.idNominaEstatus <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el numero de proceso");
				return;
			}
			
			estatus = this.ifzEmpleadoNomina.findNominaEstatus(this.idNominaEstatus);
			if (estatus == null || estatus.getId() == null || estatus.getId() <= 0L) {
				control(-1, "Ocurrio un problema al recuperar el proceso: " + this.idNominaEstatus);
				return;
			}
			
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("fechaDesde", formatter.format(estatus.getFechaDesde()));
			paramsReporte.put("fechaHasta", formatter.format(estatus.getFechaHasta()));
			
			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.nominaSemanal"));
			if (estatus.preliminar())
				params.put("idPrograma",  this.entornoProperties.getString("reporte.nominaSemanalPreliminar"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());
			
			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO: " + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, "Calculo de Nomina terminado.\nProceso " + this.idNominaEstatus + "\n\nOcurrio un problema al intentar imprimir la Nomina Semanal (Lista de Raya)\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 
			
			formatter.applyPattern("yyMMdd");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "NOMINA-S-" + formatter.format(estatus.getFechaDesde()) + "-" + formatter.format(estatus.getFechaHasta()) + "." + formatoDocumento;
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido de la Nomina Semanal (Lista de Raya)");
				control(-1, "Calculo de Nomina terminado.\nProceso " + this.idNominaEstatus + "\n\nOcurrio un problema al intentar imprimir la Nomina Semanal (Lista de Raya)");
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			control(10, "Calculo de Nomina terminado.\nProceso " + this.idNominaEstatus);
		} catch (Exception e) {
			control("Calculo de Nomina terminado.\nProceso " + this.idNominaEstatus + "\n\nOcurrio un problema al intentar imprimir la Orden de Compra", e);
		} 
	}
	
	private boolean validarCalculoNomina() {
		SimpleDateFormat formatter = null;
		List<EmpleadoNomina> nomina = null;
		
		try {
			if (this.procesando) {
				log.warn("Proceso de Nomina actualmente corriendo");
				return false;
			}
			
			if (this.fechaHasta == null || this.fechaDesde == null) {
				control("Debe indicar el rango de fecha. Fecha inicio y fecha fin");
				return false;
			}

			if (! this.fechaHasta.after(this.fechaDesde)) {
				formatter = new SimpleDateFormat("MM-dd-yyyy");
				if (! formatter.format(this.fechaDesde).equals(formatter.format(this.fechaHasta))) {
					control(4, "Fecha de inicio es mayor a la fecha final");
					return false;
				}
			}
			
			if (! this.recalcularNomina && ! this.nominaPreliminar) {
				nomina = this.ifzEmpleadoNomina.findByDates(this.fechaDesde, this.fechaHasta);
				if (nomina != null && ! nomina.isEmpty()) {
					control(6, "Nomina generada. Recalcular?");
					return false;
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al validar la Nomina", e);  
			return false;
		}
		
		return true;
	}
	
	private List<String> ordenaKeySet(Set<String> values) {
		List<String> resultado = new ArrayList<String>();
		List<String> otras = new ArrayList<String>();
		
		for (String val : values) {
			if (val.trim().length() > 1)
				otras.add(val);
			else
				resultado.add(val);
		}

		Collections.sort(resultado);
		if (! otras.isEmpty())
			resultado.addAll(otras);
		return resultado;
	}

	/*private boolean validaRequest(String param) {
		return validaRequest(param, null);
	}
	
	private boolean validaRequest(String param, String refValue) {
		if (! this.isDebug)
			return false;
		
		if (! this.paramsRequest.containsKey(param))
			return false;
		
		if (refValue == null || "".equals(refValue.trim()))
			return true;
		
		return (refValue.trim().equals(this.paramsRequest.get(param).trim()));
	}

	private void validaRequest(String param, boolean value) {
		if (! this.isDebug)
			return;
		
		param = param.trim().toUpperCase();
		if (! this.paramsRequest.containsKey(param) && value) 
			this.paramsRequest.put(param, "1");
		else if (this.paramsRequest.containsKey(param) && ! value) 
			this.paramsRequest.remove(param);
	}*/
	
	private Date getPrevEndDayOfWeek(Date fechaBase) {
		Calendar resultado = Calendar.getInstance();
		int diaActual = 0;
		int diaInicial = 0;
		int diferencia = 0;

		resultado.setTime(fechaBase);
		diaActual = resultado.get(Calendar.DAY_OF_WEEK);

		switch (this.dowHasta) {
			case "Domingo": diaInicial = 1; break;
			case "Lunes": diaInicial = 2; break;
			case "Martes": diaInicial = 3; break;
			case "Miercoles": diaInicial = 4; break;
			case "Jueves": diaInicial = 5; break;
			case "Viernes": diaInicial = 6; break;
			case "Sabado": diaInicial = 7; break;
		}

		if (diaActual > diaInicial) 
			diferencia = diaActual - diaInicial;
		else if (diaActual < diaInicial)
			diferencia = (diaActual + 7) - diaInicial;
		else
			return resultado.getTime();

		diferencia *= -1;
		resultado.add(Calendar.DAY_OF_YEAR, diferencia);
		return resultado.getTime();
	}
	
	private Date getEndDayOfWeek(Date fechaBase) {
		Calendar resultado = Calendar.getInstance();
		int diaActual = 0;
		int diaFinal = 0;
		int diferencia = 0;

		resultado.setTime(fechaBase);
		diaActual = resultado.get(Calendar.DAY_OF_WEEK);

		switch (this.dowHasta) {
			case "Domingo": diaFinal = 1; break;
			case "Lunes": diaFinal = 2; break;
			case "Martes": diaFinal = 3; break;
			case "Miercoles": diaFinal = 4; break;
			case "Jueves": diaFinal = 5; break;
			case "Viernes": diaFinal = 6; break;
			case "Sabado": diaFinal = 7; break;
		}

		if (diaActual > diaFinal) 
			diferencia = (diaFinal + 7) - diaActual;
		else if (diaActual < diaFinal)
			diferencia = diaFinal - diaActual;
		else
			return resultado.getTime();

		resultado.add(Calendar.DAY_OF_YEAR, diferencia);
		return resultado.getTime();
	}
	
	private void control() { 
		this.operacion = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}
	
	private void control(String value) { 
		control(-1, value, null); 
	}

	private void control(int tipoMensaje, String mensaje) { 
		control(tipoMensaje, mensaje, null); 
	}

	private void control(String mensaje, Throwable throwable) { 
		control(-1, mensaje, throwable); 
	}
	
	private void control(int tipoMensaje, String mensaje, Throwable throwable) {
		control(true, tipoMensaje, mensaje, throwable); 
	}

	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null)
			mensaje = "500 - Ocurrio un problema no controlado";
		this.operacion = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = mensaje.replace("\n", "<br/>");
		log.error("\n\nEMPLEADOS-NOMINA :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + "\n" + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	// ----------------------------------------------------------------------------------------
	// IMPORTAR NOMINA QUINCENAL
	// ----------------------------------------------------------------------------------------
	
	public void nuevaCargaNominaQuincenal() {
		SimpleDateFormat formatter = new SimpleDateFormat();
		Calendar cal = Calendar.getInstance();
		String fecha = "";
		int diaBase = 0;

		this.fileName = "";
		this.fileSrc = null;
		this.fechaDesdeQuincenal = null;
		this.fechaHastaQuincenal = null;
		this.cargaRetroactiva = false;
		this.permitirSinSueldo = false;
		this.listInformacionFaltante = new ArrayList<NominaQuincenalItem>();
		cal.setTime(Calendar.getInstance().getTime());
		diaBase = cal.get(Calendar.DAY_OF_MONTH);
		
		try {
			formatter = new SimpleDateFormat("yyyy-MM"); 
			fecha = formatter.format(cal.getTime()) + "-15";
			if (diaBase < 15) {
				cal.add(Calendar.MONTH, -1);
				fecha = formatter.format(cal.getTime()) + "-" + cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			}

			formatter.applyPattern("yyyy-MM-dd");
			cal.setTime(formatter.parse(fecha));
		} catch (Exception e) {
			cal.setTime(Calendar.getInstance().getTime());
		} finally {
			diaBase = cal.get(Calendar.DAY_OF_MONTH);
		}
		
		try {
			formatter.applyPattern("yyyy-MM");
			fecha = formatter.format(cal.getTime()) + "-16";
			if (diaBase <= 15)
				fecha = formatter.format(cal.getTime()) + "-01";
			formatter.applyPattern("yyyy-MM-dd");
			this.fechaDesdeQuincenal = formatter.parse(fecha);
		} catch (ParseException e) {
			this.fechaDesdeQuincenal = Calendar.getInstance().getTime();
		}
		
		try {
			formatter.applyPattern("yyyy-MM");
			fecha = formatter.format(cal.getTime()) + "-" + cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			if (diaBase <= 15)
				fecha = formatter.format(cal.getTime()) + "-15";
			formatter.applyPattern("yyyy-MM-dd");
			this.fechaHastaQuincenal = formatter.parse(fecha);
		} catch (ParseException e) {
			this.fechaHastaQuincenal = Calendar.getInstance().getTime();
		}
	}
	
	public void cargarNominaQuincenal(FileUploadEvent event) {
		UploadedFile item = null;
		
		try {
			control();
			item = event.getUploadedFile();
			if (this.fileName != null && this.fileName.equals(item.getName()) && this.fileSrc != null) 
				return;
			
			this.fileName = item.getName();
			this.fileSrc = item.getData();
			log.info("\n\n------------------------------------------------------------------------"
					+ "\n File     : " + item.getName() 
					+ "\n Data     : " + item.getData() 
					+ "\n FileSize : " + item.getSize()
					+ "\n------------------------------------------------------------------------\n");
		} catch (Exception e) {
			log.error("Error en RecHum.EmpleadosNominaAction.cargarNominaQuincenal :: " + this.fileName, e);
			control("Ocurrio un problema al cargar el archivo indicado: " + this.fileName);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void importarNominaQuincenal() {
		Respuesta respuesta = null;
		List<EmpleadoNomina> listQuincenal = null;
		
		try {
			control();
			if (this.fechaDesdeQuincenal == null)
				this.fechaDesdeQuincenal = Calendar.getInstance().getTime();
			if (this.fechaHastaQuincenal == null)
				this.fechaHastaQuincenal = Calendar.getInstance().getTime();
			this.listInformacionFaltante = new ArrayList<NominaQuincenalItem>();
			if (this.fileSrc == null) {
				control("No se indico ningun archivo para la importacion");
				return;
			}
			
			// Leemos archivo
			// -----------------------------------------------------------------------------------
			log.info("Importando Nomina Quincenal --> " + this.fileName);
			this.ifzEmpleadoNomina.setInfoSesion(this.loginManager.getInfoSesion());
			respuesta = this.ifzEmpleadoNomina.importarNominaQuincenal(this.fechaDesdeQuincenal, this.fechaHastaQuincenal, this.fileSrc, this.layoutNominaQuincenal, this.cargaRetroactiva, this.permitirSinSueldo);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("Ocurrio un problema al importar Nomina Quincenal.\n" + respuesta.getErrores().getCodigoError() + " - " + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al importar Nomina Quincenal.\n" + respuesta.getErrores().getDescError());
				return;
			}

			log.info("Comprobando Nomina Quincenal ...");
			this.paginacionInformacionFaltante = 1;
			this.listInformacionFaltante = (List<NominaQuincenalItem>) respuesta.getBody().getValor("sinProcesar");
			if (this.listInformacionFaltante != null && ! this.listInformacionFaltante.isEmpty()) {
				log.error("ERROR con archivo " + this.fileName + "\nLista de Nomina vacia o nula");
				control(-2, "Ocurrio un problema al importar Nomina Quincenal.\nHay empleados con informacion faltante: x" + this.listInformacionFaltante.size());
				return;
			}

			log.info("Recuperando registros ...");
			listQuincenal = (List<EmpleadoNomina>) respuesta.getBody().getValor("nominaQuincenal");
			if (listQuincenal == null || listQuincenal.isEmpty()) {
				log.error("ERROR con archivo " + this.fileName + "\nLista de Nomina vacia o nula");
				control(-1, "Ocurrio un problema al importar Nomina Quincenal.\nNo se pudo recuperar la lista de Empleados procesada");
				return;
			}
			
			log.info("Guardando Nomina Quincenal: " + listQuincenal.size() + " items");
			this.ifzEmpleadoNomina.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleadoNomina.saveOrUpdateList(listQuincenal);
			
			log.info("Lanzamos reporte Nomina Quincenal ...");
			reporteNominaQuicenal();
		} catch (Exception e) {
			control(-1, "Ocurrio un problema al importar Nomina Quincenal", e);
		} finally {
			this.cargaRetroactiva = false;
			this.permitirSinSueldo = false;
		}
	}
	
	public void reporteNominaQuicenal() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Respuesta respuesta = null;
		HashMap<String, Object> paramsReporte;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			// Parametros del reporte
			paramsReporte = new HashMap<String, Object>();
			paramsReporte.put("fechaDesde", formatter.format(this.fechaDesdeQuincenal));
			paramsReporte.put("fechaHasta", formatter.format(this.fechaHastaQuincenal));

			// Parametros para la ejecucion del reporte
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  this.entornoProperties.getString("reporte.nominaQuincenal"));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario());

			respuesta = this.ifzReportes.ejecutaReporte(params, paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO - " + respuesta.getErrores().getDescError());
				control(-1, "Ocurrio un problema al intentar imprimir la Nomina Quincenal\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 
			
			formatter.applyPattern("yyMMdd");
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = "NOMINA-Q-" + formatter.format(this.fechaDesdeQuincenal) + "-" + formatter.format(this.fechaHastaQuincenal) + "." + formatoDocumento;
			
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido de la Nomina Quincenal");
				control(-1, "Ocurrio un problema al intentar imprimir la Nomina Quincenal");
				return;
			}
			
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento); 	
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar imprimir la Nomina Quincenal", e);
		} 
	}
	
	public void autoasignaFechaHastaQuincenal() {
		SimpleDateFormat formatter = new SimpleDateFormat();
		Calendar cal = Calendar.getInstance();
		Date fechaBase = null;
		String fecha = "";
		int diaBase = 0;

		try {
			if (this.fechaDesdeQuincenal == null)
				return;
			fechaBase = this.fechaDesdeQuincenal;
			cal.setTime(fechaBase);
			diaBase = cal.get(Calendar.DAY_OF_MONTH);
			formatter.applyPattern("yyyy-MM");
			if (diaBase <= 15)
				fecha = formatter.format(fechaBase) + "-15";
			else
				fecha = formatter.format(fechaBase) + "-" + cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			formatter.applyPattern("yyyy-MM-dd");
			this.fechaHastaQuincenal = formatter.parse(fecha);
		} catch (Exception e) {
			this.fechaHastaQuincenal = Calendar.getInstance().getTime();
		}
	}
	
	// ----------------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------------
	
	public boolean getOperacion() {
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

	public Date getFechaDesde() {
		return fechaDesde;
	}
	
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	public boolean isRecalcularNomina() {
		return recalcularNomina;
	}

	public void setRecalcularNomina(boolean recalcularNomina) {
		this.recalcularNomina = recalcularNomina;
	}

	public boolean isProcesando() {
		return procesando;
	}

	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}

	public boolean isNominaPreliminar() {
		return nominaPreliminar;
	}

	public void setNominaPreliminar(boolean nominaPreliminar) {
		this.nominaPreliminar = nominaPreliminar;
	}

	public Date getFechaDesdeQuincenal() {
		return fechaDesdeQuincenal;
	}

	public void setFechaDesdeQuincenal(Date fechaDesdeQuincenal) {
		this.fechaDesdeQuincenal = fechaDesdeQuincenal;
	}

	public Date getFechaHastaQuincenal() {
		return fechaHastaQuincenal;
	}

	public void setFechaHastaQuincenal(Date fechaHastaQuincenal) {
		this.fechaHastaQuincenal = fechaHastaQuincenal;
	}

	public List<NominaQuincenalItem> getListInformacionFaltante() {
		return listInformacionFaltante;
	}

	public void setListInformacionFaltante(List<NominaQuincenalItem> listInformacionFaltante) {
		this.listInformacionFaltante = listInformacionFaltante;
	}

	public int getPaginacionInformacionFaltante() {
		return paginacionInformacionFaltante;
	}

	public void setPaginacionInformacionFaltante(int paginacionInformacionFaltante) {
		this.paginacionInformacionFaltante = paginacionInformacionFaltante;
	}
	
	public double getStayTime() {
		return stayTime;
	}
	
	public void setStayTime(double stayTime) {
		this.stayTime = stayTime;
	}

	public boolean getDebugging() {
		return this.isDebug;
	}
	
	public void setDebugging(boolean value) {
		this.isDebug = value;
	}
	
	public boolean getActivarRetroactivo() {
		return ("ADMINISTRADOR JAVIITR".contains(this.loginManager.getInfoSesion().getAcceso().getUsuario().getUsuario()) || this.isDebug);
	}
	
	public void setActivarRetroactivo(boolean value) {}

	public boolean getRetroactivo() {
		return this.cargaRetroactiva;
	}
	
	public void setRetroactivo(boolean value) {
		this.cargaRetroactiva = value;
	}

	public boolean getPermitirSinSueldo() {
		return this.permitirSinSueldo;
	}
	
	public void setPermitirSinSueldo(boolean value) {
		this.permitirSinSueldo = value;
	}

	public String getDowDesde() {
		return dowDesde;
	}

	public void setDowDesde(String dowDesde) {
		this.dowDesde = dowDesde;
	}

	public String getDowHasta() {
		return dowHasta;
	}

	public void setDowHasta(String dowHasta) {
		this.dowHasta = dowHasta;
	}
}

//HISTORIAL DE MODIFICACIONES 
//----------------------------------------------------------------------------------------------------------------
//  VERSIÓN |   FECHA    | 		AUTOR 		| DESCRIPCIÓN 
//----------------------------------------------------------------------------------------------------------------
//	  2.2	| 11/07/2016 | Javier Tirado	| Creacion de EmpleadoNominaAction