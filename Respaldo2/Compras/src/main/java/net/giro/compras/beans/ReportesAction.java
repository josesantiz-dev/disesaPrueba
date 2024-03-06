package net.giro.compras.beans;

import java.io.DataInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.impresion.ReportesRem;
import net.giro.respuesta.Respuesta;

import net.giro.sesion.CParametro;
import net.giro.sesion.Programa;
import net.giro.sesion.SesionProc;
import net.giro.sesion.beans.EjecutaBean;

import org.ajax4jsf.component.behavior.AjaxBehavior; // <---
import org.ajax4jsf.component.behavior.MethodExpressionAjaxBehaviorListener; // <---
import org.apache.log4j.Logger;
import org.richfaces.component.UICalendar; // <---

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@ViewScoped
@ManagedBean(name="repAction")
public class ReportesAction implements Serializable {
	private static Logger log = Logger.getLogger(ReportesAction.class);
	private static final long serialVersionUID = 1L;
	private PropertyResourceBundle entornoProperties;
	private LoginManager loginManager;
	private HttpSession httpSession;
	private SesionProc sesion;
    private boolean operacionCancelada;
	private int tipoMensaje;
	private String mensaje;
	private List<Long> reportesValidos;
	private List<Programa> listReportes;
	private List<SelectItem> listReportesItems;
	private long idReporte;
	private Programa reporteSeleccionado;
	// Interfaces
	private ReportesRem	ifzReportes;
	// VALORES DINAMICOS
	private HashMap<String, Object> paramsReporte;
	private String currentParam;
	private String renders;
	private long paramGrupo;
	// BUSQUEDA DINAMICA
	private List<SelectItem> listValoresDinamicos;
	private SelectItem pojoValorDinamico;
	private String valorBusquedaDinamica;
	private int numPaginaDinamica;
	// DEBUG
	private boolean isDebug;
	private HashMap<String, String> paramsRequest;
	
	public ReportesAction() {
		InitialContext ctx = null;
		FacesContext fc = null;
		Application app = null;
		ValueExpression valExp = null;
		String valor = "";
		String[] splitted = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			this.paramsRequest = new HashMap<String, String>();
			for (Entry<String, String> item : fc.getExternalContext().getRequestParameterMap().entrySet())
				this.paramsRequest.put(item.getKey().toUpperCase(), item.getValue().toUpperCase());
			this.isDebug = (this.paramsRequest.containsKey("DEBUG")) ? true : false;
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) valExp.getValue(fc.getELContext());
			this.httpSession = (HttpSession) fc.getExternalContext().getSession(false);

			this.reportesValidos = new ArrayList<Long>();
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgAdminProc}", PropertyResourceBundle.class);
			if (valExp != null) {
				this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
				/*valor = this.entornoProperties.getString("reportes_user");
				if (valor != null && ! "".equals(valor.trim()))
					this.reportesUsuario = valor.trim();*/
				valor = this.entornoProperties.getString("reportes");
				if (valor != null && ! "".equals(valor.trim())) {
					splitted = valor.split(",");
					for (String val : splitted)
						this.reportesValidos.add(Long.valueOf(val));
					valor = "";
				}
			}
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{sesionProc}", SesionProc.class);
			this.sesion = (SesionProc) valExp.getValue(fc.getELContext());
			this.sesion.setDebugging(this.isDebug);
			
			valExp = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
			this.entornoProperties = (PropertyResourceBundle) valExp.getValue(fc.getELContext());
			
			// Conexion con modulos
			ctx = new InitialContext();
			this.ifzReportes = (ReportesRem) ctx.lookup("ejb:/Logica_Publico//ReportesFac!net.giro.plataforma.impresion.ReportesRem");
			
			// Filtramos reportes
			cargarReportes();
		} catch (Exception e) {
			log.error("Error en Compras.ReportesAction", e);
		}
	}
	
	
	public void nuevoReporte() {
		try {
			control();
			this.idReporte = 0;
			this.paramGrupo = 0;
			this.currentParam = "";
			this.renders = "";
			cargaReporte();
		} catch (Exception e) {
			control("Error en Compras.ReportesAction.nuevoReporte()", e);
		}
	}

	public void cargaReporte() {
		this.reporteSeleccionado = null;
		if (this.idReporte > 0) {
			for (Programa var : this.listReportes) {
				if (var.getcPrograma().getId() == this.idReporte) {
					this.reporteSeleccionado = var;
					break;
				}
			}
			
			if (this.reporteSeleccionado == null) {
				control("Debe seleccionar un reporte de la lista");
				return;
			}
		}
		
		generarParametros();
	}
	
	public void lanzarReporte() {
		Respuesta respuesta = null;
		HashMap<String, String> params;
		byte[] contenidoDocumento = null;
		String formatoDocumento = "";
		String nombreDocumento = "";
		
		try {
			control();
			log.info("Proceso de ejecucion de reporte iniciado. Validando parametros ... ");
			if (! validarParametros()) {
				control("Verifique los parametros del reporte seleccionado");
				return;
			}
			
			// Parametros para la ejecucion del reporte
			log.info("Generando parametro para ejecucion ... ");
			params = new HashMap<String, String>();
			params.put("idPrograma", 	  String.valueOf(this.reporteSeleccionado.getcPrograma().getId()));
			params.put("ftp_host", 		  this.entornoProperties.getString("ftp_host"));
			params.put("ftp_port", 	 	  this.entornoProperties.getString("ftp_port"));
			params.put("ftp_user", 		  this.entornoProperties.getString("ftp_user"));
			params.put("ftp_password", 	  this.entornoProperties.getString("ftp_password"));
			params.put("socket_ip", 	  this.entornoProperties.getString("socket_ip"));
			params.put("socket_puerto",   this.entornoProperties.getString("socket_puerto"));
			params.put("usuario", 		  this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario()); 

			log.info("Ejecutando reporte seleccionado ... ");
			respuesta = this.ifzReportes.ejecutaReporte(params, this.paramsReporte);
			if (respuesta.getErrores().getCodigoError() != 0L) {
				log.error("ERROR INTERNO - " + respuesta.getErrores().getDescError());
				control("No se pudo ejecutar el reporte seleccionado.\nError " + respuesta.getErrores().getCodigoError());
				return;
			} 

			log.info("Recuperando contenido de reporte ... ");
			formatoDocumento = respuesta.getBody().getValor("formatoReporte").toString();
			nombreDocumento = this.reporteSeleccionado.getcPrograma().getId() + "_" + this.reporteSeleccionado.getcPrograma().getTitulo() + "." + formatoDocumento;
			contenidoDocumento = (byte[]) respuesta.getBody().getValor("contenidoReporte");
			if (contenidoDocumento == null || contenidoDocumento.length <= 0) {
				log.error("ERROR INTERNO - No se recupero el contenido del reporte seleccionado");
				control("No se pudo ejecutar el reporte seleccionado.");
				return;
			}
			
			this.httpSession.setAttribute("nombreDocumento", nombreDocumento);
			this.httpSession.setAttribute("contenido", contenidoDocumento);
			this.httpSession.setAttribute("formato", formatoDocumento);
			log.info("Reporte lanzado. Proceso terminado!");
			
			nuevoReporte();
		} catch (Exception e) {
			control("Ocurrio un problema grave al ejecutar el reporte seleccionado", e);
		}
	}
	
	private boolean validarParametros() {
		SimpleDateFormat sdfBase = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String dt = "";

		// Aplicamos formato a parametros tipo fecha
		for (Map.Entry<String, CParametro> map : this.reporteSeleccionado.getcParametros().entrySet()) {
			CParametro parametro = (CParametro) map.getValue();
			if ("d".equals(parametro.getTipoEntrada())) {
				dt = sdfBase.format(this.paramsReporte.get(map.getKey()));
				this.paramsReporte.put(map.getKey(), dt);
			}
		}
		
		return true;
	}

	private void cargarReportes() {
		try {
			this.listReportesItems = new ArrayList<SelectItem>();
			this.listReportes = this.sesion.getReportes(this.reportesValidos);
			if (this.listReportes == null || this.listReportes.isEmpty())
				return;
			
			for (Programa var : this.listReportes) {
				if (var.getcPrograma().getPrograma().equals(var.getcPrograma().getTitulo()))
					this.listReportesItems.add(new SelectItem(var.getcPrograma().getId(), var.getcPrograma().getPrograma()));
				else
					this.listReportesItems.add(new SelectItem(var.getcPrograma().getId(), var.getcPrograma().getPrograma() + " (" + var.getcPrograma().getTitulo() + ")"));
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar los Reportes para el Modulo", e);
		}
	}

 	private void generarParametros() {
		FacesContext facesContext = null;
		Application app = null;
		UIViewRoot root = null;
		HtmlPanelGrid panel = null;
		HtmlInputText input = null;
		ValueExpression valExp = null;
		UICalendar calendar = null;
		HtmlSelectOneMenu combobox = null;
		HtmlOutputText txtValor = null;
		UISelectItem item = null;
		HtmlCommandButton btn = null;
		HtmlPanelGroup group = null;
		AjaxBehavior ajax = null;
		String etiqueta = "";
		HtmlSelectBooleanCheckbox check = null;
		List<CParametro> lista = null;
		MethodExpression metExp = null;
		
		try {
			facesContext = FacesContext.getCurrentInstance();
			app = facesContext.getApplication();
			root = facesContext.getViewRoot();
			panel = (HtmlPanelGrid) findComponent(root, "pnlDinamico");
			if (panel.getChildCount() > 0)
				panel.getChildren().clear();

			this.paramsReporte = new HashMap<String, Object>();
			if (this.reporteSeleccionado == null)
				return;
			
			lista = new ArrayList<CParametro>();
			for (Map.Entry<String, CParametro> map : this.reporteSeleccionado.getcParametros().entrySet()) {
				lista.add(map.getValue());
			}

			Collections.sort(lista, new Comparator<CParametro>() {
				public int compare(CParametro o1, CParametro o2) {
					return o1.getOrden() - o2.getOrden();
				}
			});

			for (CParametro p : lista) {
				etiqueta = construyeEtiqueta(p.getEtiqueta(), p.getParametro());
				
				txtValor = (HtmlOutputText)facesContext.getApplication().createComponent("javax.faces.HtmlOutputText");
				txtValor.setValue(etiqueta + ":");
				txtValor.setStyleClass("letra-titulo-body");
				panel.getChildren().add(txtValor);
				if ("s".equals(p.getTipoEntrada())) {
					this.paramsReporte.put(p.getParametro(), p.getValorDefault());
					valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{repAction.param['" + p.getParametro() + "']}", String.class);
					
					input = (HtmlInputText)facesContext.getApplication().createComponent("javax.faces.HtmlInputText");
					input.setValueExpression("value", valExp);
					
					panel.getChildren().add(input);
				} else if ("g".equals(p.getTipoEntrada())) {
					this.paramsReporte.put(p.getParametro(), p.getValorDefault());
					valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{repAction.param['" + p.getParametro() + "']}", String.class);
					
					input = (HtmlInputText)facesContext.getApplication().createComponent("javax.faces.HtmlInputText");
					input.setValueExpression("value", valExp);
					input.setId("txt" + etiqueta);
					input.setDisabled(true);
					
					metExp = app.getExpressionFactory().createMethodExpression(facesContext.getELContext(), "#{repAction.busqueda}", null, new Class[] { BehaviorEvent.class });
					
					ajax = (AjaxBehavior)facesContext.getApplication().createBehavior("org.ajax4jsf.behavior.Ajax");
					ajax.setOncomplete("busquedaDinamica('frmPrincipal:pnlBusquedaDinamica')");
					ajax.setRender(Arrays.asList(new String[] { "frmPrincipal:pnlBusquedaDinamica" }));
					ajax.setLimitRender(true);
					ajax.addAjaxBehaviorListener(new MethodExpressionAjaxBehaviorListener(metExp));
					
					btn = (HtmlCommandButton)facesContext.getApplication().createComponent("javax.faces.HtmlCommandButton");
					btn.setId("cmd" + etiqueta);
					btn.setImage("/resources/image/22/buscar.png");
					btn.setStyle("margin-left: 5px;");
					btn.addClientBehavior(btn.getDefaultEventName(), ajax);
					
					btn.getChildren().add(genUIParam("programParam", p.getParametro()));
					btn.getChildren().add(genUIParam("targetRender", "frmPrincipal:txt" + etiqueta));
					btn.getChildren().add(genUIParam("paramGrupo", String.valueOf(p.getGrupo())));
					
					group = (HtmlPanelGroup)facesContext.getApplication().createComponent("javax.faces.HtmlPanelGroup");
					group.getChildren().add(input);
					group.getChildren().add(btn);
					
					panel.getChildren().add(group);
				} else if ("d".equals(p.getTipoEntrada())) {
					this.paramsReporte.put(p.getParametro(), Calendar.getInstance().getTime());
					
					calendar = (UICalendar)facesContext.getApplication().createComponent(FacesContext.getCurrentInstance(), "org.richfaces.Calendar", "org.richfaces.CalendarRenderer");
					valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{repAction.param['" + p.getParametro() + "']}", Date.class);
					calendar.setValueExpression("value", valExp);
					calendar.setDisabled(false);
					calendar.setDatePattern("dd/MMM/yyyy");
					panel.getChildren().add(calendar);
				} else if ("l".equals(p.getTipoEntrada())) {
					this.paramsReporte.put(p.getParametro(), "");
					combobox = (HtmlSelectOneMenu)facesContext.getApplication().createComponent("javax.faces.HtmlSelectOneMenu");
					
					valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{repAction.param['" + p.getParametro() + "']}", String.class);
					boolean idValor = true;
					if ((p.getValores() != null) && (!"".equals(p.getValores()))) {
						for (String str : p.getValores().split("\\|")) {
							if (idValor) {
								item = new UISelectItem();
								item.setItemLabel(str);
							} else {
								item.setItemValue(str);
								combobox.getChildren().add(item);
							}
							idValor = !idValor;
						}
					}
					
					combobox.setValueExpression("value", valExp);
					panel.getChildren().add(combobox);
				} else if ("c".equals(p.getTipoEntrada())) {
					this.paramsReporte.put(p.getParametro(), Boolean.valueOf(false));
					check = (HtmlSelectBooleanCheckbox)facesContext.getApplication().createComponent("javax.faces.HtmlSelectBooleanCheckbox");
					
					valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{repAction.param['" + p.getParametro() + "']}", Boolean.class);
					check.setValueExpression("value", valExp);
					panel.getChildren().add(check);
				}
			}
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar los parametros del reporte seleccionado", e);
		}
	}

	private String construyeEtiqueta(String value, String valueAuxiliar) {
		if (value != null && ! "".equals(value)) 
			return UCFirst(value);
		
		if (valueAuxiliar != null && ! "".equals(valueAuxiliar)) 
			return UCFirst(valueAuxiliar);
		
		return "Operacion";
	}

	private String UCFirst(String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}

	private UIComponent findComponent(UIComponent base, String id) {
		UIComponent comp = null;
		UIComponent result = null;
		Iterator<UIComponent> hijos = null;

		if (id.equals(base.getId()))
			return base;
		
		hijos = base.getFacetsAndChildren();
		while ((hijos.hasNext()) && (result == null)) {
			comp = (UIComponent)hijos.next();
			if (id.equals(comp.getId())) {
				result = comp;
			} else {
				result = findComponent(comp, id);
				if (result != null) {
					break;
				}
			}
		}

		return result;
	}

	private UIParameter genUIParam(String name, String value) {
		UIParameter param = new UIParameter();
		
		param.setName(name);
		param.setValue(value);
		
		return param;
	}

	private void control() {
		this.operacionCancelada = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
	}

	private void control(String mensaje) {
		control(true, 1, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, 1, mensaje, throwable);
	}
	
	private void control(boolean incompleta, int tipoMensaje, String mensaje, Throwable throwable) {
		this.operacionCancelada = incompleta;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje != null && ! "".equals(mensaje.trim()) ? mensaje.trim().replace("\n", "<br/>") : "Ocurrio un problema no controlado al realizar la operacion");
		log.error("\n" + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + " :: " + this.tipoMensaje + " - " + mensaje + "\n", throwable);
	}

	// --------------------------------------------------------------------------------------
	// BUSQUEDA DINAMICA
	// --------------------------------------------------------------------------------------
	
	public void busqueda() {
		try {
			control();
			this.valorBusquedaDinamica = "";
			this.listValoresDinamicos = new ArrayList<SelectItem>();
			this.numPaginaDinamica = 1;
			this.pojoValorDinamico = new SelectItem();
			
			Map<String, String> btnParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			if ((btnParams != null) && (! btnParams.isEmpty())) {
				if (btnParams.containsKey("programParam"))
					this.currentParam = ((String) btnParams.get("programParam"));
	
				if (btnParams.containsKey("targetRender"))
					this.renders = ((String) btnParams.get("targetRender") + ("".equals(this.renders) ? "" : ","));
	
				if (btnParams.containsKey("paramGrupo"))
					this.paramGrupo = Long.valueOf((String) btnParams.get("paramGrupo")).longValue();
			}
		} catch (Exception e) {
			control("Ocurrio un problema al intentar iniciar la Busqueda Dinamica", e);
		}
	}
	
	public void buscar() throws Exception {
		List<SelectItem> listValoresDinamicosAux = new ArrayList<SelectItem>();
		// ---------------------------------------------------------------------
		LinkedHashMap<Object, String> results = null;
		EjecutaBean bean = null;
		String[] parts = null;
		String pEjb = "";
		String pMetodo = "";
		String pClasesParametros = "";
		String pValoresParametros = "";
		String pMetodos = "";
		char[] arrayChar = null;
		int veces = 0;
		
		try {
			control();
			this.listValoresDinamicos = new ArrayList<SelectItem>();
			if (this.valorBusquedaDinamica == null)
				this.valorBusquedaDinamica = "";
			
			// Recuperamos valores
			this.listValoresDinamicos = getValores(this.paramGrupo);
			if (this.listValoresDinamicos == null || this.listValoresDinamicos.isEmpty()) {
				control("La Busqueda no regreso resultados");
				return;
			}
			
			if (this.listValoresDinamicos.size() == 1 && "200".equals(this.listValoresDinamicos.get(0).getValue().toString())) {
				parts = this.listValoresDinamicos.get(0).getLabel().split("-");
				pEjb = parts[0];
				pMetodo = parts[1];
				
				if (parts.length > 2)
					pClasesParametros = parts[2];
				if (parts.length > 3)
					pValoresParametros = parts[3];
				if (parts.length > 4)
					pMetodos = parts[4];
				
				if (! "".equals(pClasesParametros) && ! "".equals(pValoresParametros) && pClasesParametros.trim().split(",").length == pValoresParametros.trim().split(",").length) {
					if (pValoresParametros.contains("?")) {
						arrayChar = pValoresParametros.toCharArray();
						for (int i = 0; i< arrayChar.length; i++)
							veces += (arrayChar[i] == '?') ? 1 : 0;
					}
					
					if (veces > 0) {
						parts = pValoresParametros.split(",");
						for (int i = 0; i < parts.length; i++) {
							if (parts[i].trim().contains("?")) {
								if (veces == 1) {
									pValoresParametros = pValoresParametros.replace(parts[i].trim(), this.valorBusquedaDinamica.replace(" ", "%"));
									break;
								} else {
									// do nothing
								}
							}
						}
					}
				}
				
				// Mandamos ejecutar el bean
				log.info("EJB: " + pEjb + "\nMetodo: " + pMetodo + "\nParametros: " + pClasesParametros + "\nValores: " + pValoresParametros);
				this.listValoresDinamicos.clear();
				bean = new EjecutaBean();
				bean.setInfoSesion(this.loginManager.getInfoSesion());
				bean.setCampoDescripcion(pMetodos);
				results = bean.ejecutar(pEjb, pMetodo, pClasesParametros, pValoresParametros);
				if (results != null && ! results.isEmpty()) {
					for (Entry<Object, String> item : results.entrySet())
						this.listValoresDinamicos.add(new SelectItem(item.getKey(), item.getValue()));
				}
			} else {
				// Aplicamos filtros
				if (this.valorBusquedaDinamica != null && ! "".equals(this.valorBusquedaDinamica.trim())) {
					for (SelectItem item : this.listValoresDinamicos) {
						if (item.getLabel().toLowerCase().contains(this.valorBusquedaDinamica.trim().toLowerCase()) || item.getValue().toString().contains(this.valorBusquedaDinamica.trim()))
							listValoresDinamicosAux.add(item);
					}
					
					this.listValoresDinamicos.clear();
					this.listValoresDinamicos.addAll(listValoresDinamicosAux);
					
					// Comprobamos resultados ...
					if (this.listValoresDinamicos == null || this.listValoresDinamicos.isEmpty()) {
						control("La Busqueda no regreso resultados");
						return;
					}
				}
			}
			
			// Ordenamos
			Collections.sort(this.listValoresDinamicos, new Comparator<SelectItem>() {
				@Override
				public int compare(SelectItem o1, SelectItem o2) {
					return o1.getLabel().compareTo(o2.getLabel());
				}
			});
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la Busqueda Dinamica", e);
		}
	}
	
	public void seleccionar() {
		try {
			control();
			if (this.pojoValorDinamico == null) {
				control("Ocurrio un problema al recuperar el elemento seleccionado.");
				return;
			}
			
			this.paramsReporte.put(this.currentParam, this.pojoValorDinamico.getValue().toString());
			this.pojoValorDinamico = null;
			this.currentParam = "";
			this.renders = "";
			this.paramGrupo = 0L;
		} catch (Exception e) {
			control("Ocurrio un problema al intentar recuperar el valor dinamico seleccionado", e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<SelectItem> getValores(long IdGrupoValor) throws Exception {
		HashMap<String, String> mapValores = new HashMap<String, String>();
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<SelectItem> valores = null;
		DataInputStream input = null;
		JsonObject jsonObject = null;
		JsonParser parser = null;
		String respuesta = null;
		String comando = null;
		Socket socket = null;
		Gson gson = null;
		byte[] b = null;

		try {
			control();
			log.info("Proceso dinamico. Generando comando ... ");
			gson = new Gson();
			map.put("tipo", "030100");
			map.put("parametroGrupooId", String.valueOf(IdGrupoValor));
			parametros.put(this.currentParam, "0");
			map.put("parametros", parametros);
			comando = gson.toJson(map).trim();
			
			log.info("Conectando socket y Enviando comando ... \n" + comando);
			socket = new Socket("procesos.condese.net", 2099);
			socket.getOutputStream().write(comando.getBytes());
			
			Thread.sleep(1000);

			log.info("Recupero respuesta ... ");
			b = new byte[64000]; // b = new byte[16384]; socket.getInputStream().read(b);
			input = new DataInputStream(socket.getInputStream());
			input.read(b);

			log.info("Transformando respuesta ... ");
			parser = new JsonParser();
			respuesta = new String(b).trim();
			jsonObject = parser.parse(respuesta).getAsJsonObject();
			if ((jsonObject.has("valores")) && (!"".equals(jsonObject.get("valores").getAsString().trim()))) {
				log.info("Recuperando valores ... ");
				mapValores = (HashMap) gson.fromJson(jsonObject.get("valores").getAsString(), mapValores.getClass());
				valores = new ArrayList<SelectItem>();
				for (Map.Entry<String, String> val : mapValores.entrySet()) {
					valores.add(new SelectItem(Long.valueOf(((String) val.getKey()).toString()), ((String) val.getValue()).toString()));
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			log.info("Ejecucion terminada!");
			if (socket != null)
				socket.close();
		}

		return valores;
	}
	
	// ---------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ---------------------------------------------------------------------------------------------------------
	
	public boolean getOperacion() {
		return operacionCancelada;
	}

	public void setOperacion(boolean operacion) {
		this.operacionCancelada = operacion;
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

	public boolean getDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean debugging) {
		this.isDebug = debugging;
	}

	public List<SelectItem> getListReportesItems() {
		return listReportesItems;
	}
	
	public void setListReportesitems(List<SelectItem> listReportesItems) {
		this.listReportesItems = listReportesItems;
	}

	public long getIdReporte() {
		return idReporte;
	}

	public void setIdReporte(long idReporte) {
		this.idReporte = idReporte;
	}

	public SelectItem getPojoValorDinamico() {
		return pojoValorDinamico;
	}

	public void setPojoValorDinamico(SelectItem pojoValorDinamico) {
		this.pojoValorDinamico = pojoValorDinamico;
	}

	public int getNumPaginaDinamica() {
		return numPaginaDinamica;
	}

	public void setNumPaginaDinamica(int numPaginaDinamica) {
		this.numPaginaDinamica = numPaginaDinamica;
	}

	public List<SelectItem> getListValoresDinamicos() {
		return listValoresDinamicos;
	}

	public void setListValoresDinamicos(List<SelectItem> listValoresDinamicos) {
		this.listValoresDinamicos = listValoresDinamicos;
	}

	public String getValorBusquedaDinamica() {
		return valorBusquedaDinamica;
	}

	public void setValorBusquedaDinamica(String valorBusquedaDinamica) {
		this.valorBusquedaDinamica = valorBusquedaDinamica;
	}


	public HashMap<String, Object> getParam() {
		return paramsReporte;
	}


	public void setParam(HashMap<String, Object> param) {
		this.paramsReporte = param;
	}
}
