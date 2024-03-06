package net.proc.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
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

import net.proc.beans.EjecutaBean;
import net.proc.sesion.CParametro;
import net.proc.sesion.CSoctetStream;
import net.proc.sesion.SesionProc;
import net.proc.socket.respuesta.RespuestaCrearJob;

import org.ajax4jsf.component.behavior.AjaxBehavior;
import org.ajax4jsf.component.behavior.MethodExpressionAjaxBehaviorListener;
import org.apache.log4j.Logger;
import org.richfaces.component.UICalendar;

@ViewScoped
@ManagedBean(name="programa")
public class ProgramaController implements Serializable {
	private static Logger log = Logger.getLogger(ProgramaController.class);
	private static final long serialVersionUID = 1L;
	private SesionProc sesion;
	private Date fechaEmpieza;
	private Date fechaFinaliza;
	private int numVeces;
	private int valorFinaliza;
	private int cada;
	private String repetir;
	private String finalizaTipo;
	private boolean expandido;
	private boolean correoExpandido;
	private String enviarA;
	private String asunto;
	private String cuerpo;
	private HashMap<String, Object> param;
	private boolean diaTodos;
	private boolean diaLunes;
	private boolean diaMartes;
	private boolean diaMiercoles;
	private boolean diaJueves;
	private boolean diaViernes;
	private boolean diaSabado;
	private boolean diaDomingo;
	private List<SelectItem> listValoresDinamicos;
	private SelectItem pojoValorDinamico;
	private String valorBusquedaDinamica;
	private int numPaginaDinamica;
	private String currentParam;
	private String renders;
	private long paramGrupo;
	private boolean operacionCompleta;
	private int tipoMensaje;
	private String mensaje;
	private String mensajeDetalles;
	
	
	public ProgramaController() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{sesionProc}", SesionProc.class);

		this.sesion = ((SesionProc) dato.getValue(fc.getELContext()));
		this.fechaEmpieza = Calendar.getInstance().getTime();
		this.fechaFinaliza = Calendar.getInstance().getTime();
		this.numVeces = 1;
		this.cada = 1;
		this.valorFinaliza = 1;
		this.repetir = "d";
		this.diaTodos = false;
		this.finalizaTipo = "F";
		this.expandido = false;
		this.asunto = "";
		this.correoExpandido = false;
		this.cuerpo = "";
		this.enviarA = "";
		
		busqueda();
		generarParametros();
	}
	
	
	private void datos() {
		SimpleDateFormat sdfBase = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		String dt = "";
		
		for (Map.Entry<String, CParametro> map : this.sesion.getPrograma().getcParametros().entrySet()) {
			CParametro parametro = (CParametro)map.getValue();
			if ("d".equals(parametro.getTipoEntrada())) {
				dt = sdfBase.format(this.param.get(map.getKey()));
				this.param.put(map.getKey(), dt);
			}
		}
	}
	
	public void generarParametros() {
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
		
		try {
			this.param = new HashMap<String, Object>();
			lista = new ArrayList<CParametro>();
			for (Map.Entry<String, CParametro> map : this.sesion.getPrograma().getcParametros().entrySet()) {
				lista.add(map.getValue());
			}

			Collections.sort(lista, new Comparator<CParametro>() {
				public int compare(CParametro o1, CParametro o2) {
					return o1.getOrden() - o2.getOrden();
				}
			});

			facesContext = FacesContext.getCurrentInstance();
			app = facesContext.getApplication();
			root = facesContext.getViewRoot();
			panel = (HtmlPanelGrid)findComponent(root, "pnlDinamico");

			for (CParametro p : lista) {
				etiqueta = construyeEtiqueta(p.getEtiqueta(), p.getParametro());
				
				txtValor = (HtmlOutputText)facesContext.getApplication().createComponent("javax.faces.HtmlOutputText");
				txtValor.setValue(etiqueta + ":");
				txtValor.setStyleClass("letra-titulo-body");
				panel.getChildren().add(txtValor);
				if ("s".equals(p.getTipoEntrada())) {
					this.param.put(p.getParametro(), p.getValorDefault());
					valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{programa.param['" + p.getParametro() + "']}", String.class);
					
					input = (HtmlInputText)facesContext.getApplication().createComponent("javax.faces.HtmlInputText");
					input.setValueExpression("value", valExp);
					
					panel.getChildren().add(input);
				} else if ("g".equals(p.getTipoEntrada())) {
					this.param.put(p.getParametro(), p.getValorDefault());
					valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{programa.param['" + p.getParametro() + "']}", String.class);
					
					input = (HtmlInputText)facesContext.getApplication().createComponent("javax.faces.HtmlInputText");
					input.setValueExpression("value", valExp);
					input.setId("txt" + etiqueta);
					input.setDisabled(true);
					
					MethodExpression metExp = app.getExpressionFactory().createMethodExpression(facesContext.getELContext(), "#{programa.busqueda}", null, new Class[] { BehaviorEvent.class });
					
					ajax = (AjaxBehavior)facesContext.getApplication().createBehavior("org.ajax4jsf.behavior.Ajax");
					ajax.setOncomplete("busquedaDinamica('frmOperaciones:pnlBusquedaDinamica')");
					ajax.setRender(Arrays.asList(new String[] { "frmOperaciones:pnlBusquedaDinamica" }));
					ajax.setLimitRender(true);
					ajax.addAjaxBehaviorListener(new MethodExpressionAjaxBehaviorListener(metExp));
					
					btn = (HtmlCommandButton)facesContext.getApplication().createComponent("javax.faces.HtmlCommandButton");
					btn.setId("cmd" + etiqueta);
					btn.setImage("/resources/image/16/Search.png");
					btn.setStyle("margin-left: 5px;");
					btn.addClientBehavior(btn.getDefaultEventName(), ajax);
					
					btn.getChildren().add(genUIParam("programParam", p.getParametro()));
					btn.getChildren().add(genUIParam("targetRender", "frmOperaciones:txt" + etiqueta));
					btn.getChildren().add(genUIParam("paramGrupo", String.valueOf(p.getGrupo())));
					
					group = (HtmlPanelGroup)facesContext.getApplication().createComponent("javax.faces.HtmlPanelGroup");
					group.getChildren().add(input);
					group.getChildren().add(btn);
					
					panel.getChildren().add(group);
				} else if ("d".equals(p.getTipoEntrada())) {
					this.param.put(p.getParametro(), Calendar.getInstance().getTime());
					
					calendar = (UICalendar)facesContext.getApplication().createComponent(FacesContext.getCurrentInstance(), "org.richfaces.Calendar", "org.richfaces.CalendarRenderer");
					valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{programa.param['" + p.getParametro() + "']}", Date.class);
					calendar.setValueExpression("value", valExp);
					calendar.setDisabled(false);
					calendar.setDatePattern("dd/MMM/yyyy");
					panel.getChildren().add(calendar);
				} else if ("l".equals(p.getTipoEntrada())) {
					this.param.put(p.getParametro(), "");
					combobox = (HtmlSelectOneMenu)facesContext.getApplication().createComponent("javax.faces.HtmlSelectOneMenu");
					
					valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{programa.param['" + p.getParametro() + "']}", String.class);
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
					this.param.put(p.getParametro(), Boolean.valueOf(false));
					check = (HtmlSelectBooleanCheckbox)facesContext.getApplication().createComponent("javax.faces.HtmlSelectBooleanCheckbox");
					
					valExp = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{programa.param['" + p.getParametro() + "']}", Boolean.class);
					check.setValueExpression("value", valExp);
					panel.getChildren().add(check);
				}
			}
		} catch (Exception e) {
			log.error("Error en PROCESOS.ProgramaController.generarParametros", e);
		}
	}
	
	public void procesar() {
		SimpleDateFormat sdfBase = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, String> correo = new HashMap<String, String>();
		HashMap<String, Object> programacion = new HashMap<String, Object>();
		RespuestaCrearJob resp = null;
		Gson gson = new Gson();
		Socket socket = null;
		String resultado = "";
		String comando = "";
		String dias = "";

		try {
			log.info("----------------- PROCESANDO REPORTE ... ");
			log.info("----------------- " + this.sesion.getPrograma().getcPrograma().getPrograma());
			datos();
			
			map.put("tipo", "000100");
			map.put("idAplicacion", "3");
			map.put("idPrograma", String.valueOf(this.sesion.getPrograma().getcPrograma().getId()));
			map.put("usuario", this.sesion.getUsuario());
			map.put("role", this.sesion.getRolSeleccionado().getNombre());
			map.put("parametros", this.param);
			
			if ((this.diaTodos) || (this.diaDomingo)) 
				dias = dias + ("".equals(dias) ? "" : ",") + 1;
			if ((this.diaTodos) || (this.diaLunes)) 
				dias = dias + ("".equals(dias) ? "" : ",") + 2;
			if ((this.diaTodos) || (this.diaMartes)) 
				dias = dias + ("".equals(dias) ? "" : ",") + 3;
			if ((this.diaTodos) || (this.diaMiercoles)) 
				dias = dias + ("".equals(dias) ? "" : ",") + 4;
			if ((this.diaTodos) || (this.diaJueves)) 
				dias = dias + ("".equals(dias) ? "" : ",") + 5;
			if ((this.diaTodos) || (this.diaViernes)) 
				dias = dias + ("".equals(dias) ? "" : ",") + 6;
			if ((this.diaTodos) || (this.diaSabado)) 
				dias = dias + ("".equals(dias) ? "" : ",") + 7;
			
			programacion.put("empieza", sdfBase.format(this.fechaEmpieza));
			programacion.put("repetir", this.repetir);
			programacion.put("cada", String.valueOf(this.cada));
			programacion.put("diasSemana", dias);
			programacion.put("finaliza", this.finalizaTipo);
			programacion.put("despues", "F".equals(this.finalizaTipo) ? sdfBase.format(this.fechaFinaliza) : String.valueOf(this.valorFinaliza));
			
			if (! this.expandido)
				programacion.clear();

			map.put("programacion", programacion);
			if (! this.enviarA.trim().equals("")) {
				correo.put("to", this.enviarA);
				correo.put("subject", this.asunto);
				correo.put("body", this.cuerpo);
				map.put("correo", correo);
			}

			comando = gson.toJson(map);
			log.info("----------------- COMANDO: " + comando);
			socket = this.sesion.getSocket();
			socket.getOutputStream().write(comando.getBytes());
			socket.getOutputStream().flush();
			resultado = CSoctetStream.read(socket.getInputStream());
			log.info("----------------- RESULTADO: " + resultado);
			resp = (RespuestaCrearJob) gson.fromJson(resultado, RespuestaCrearJob.class);
			this.mensaje = ("OK".equals(resp.getMensaje()) ? String.format("PROCESO EJECUTADO ID %s", new Object[] { resp.getID() }) : resp.getMensaje());
		} catch (Exception e) {
			log.error("----------------- ERROR AL PROCESAR REPORTE (PROCESOS.ProgramaController.procesar)", e);
		} finally {
			log.info("----------------- PROCESO REPORTE TERMINADO! ");
		}
	}
	
	public void seleccionarDias() {
		this.diaLunes = this.diaTodos;
		this.diaMartes = this.diaTodos;
		this.diaMiercoles = this.diaTodos;
		this.diaJueves = this.diaTodos;
		this.diaViernes = this.diaTodos;
		this.diaSabado = this.diaTodos;
		this.diaDomingo = this.diaTodos;
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
	
	private UIParameter genUIParam(String name, String value) {
		UIParameter param = new UIParameter();
		
		param.setName(name);
		param.setValue(value);
		
		return param;
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
				if (result != null)
					break;
			}
		}

		return result;
	}
	
	private void controlCompleto() {
		this.operacionCompleta = true;
		this.tipoMensaje = 0;
		this.mensaje = "OK";
		this.mensajeDetalles = "";
	}
	
	private void control() {
		this.operacionCompleta = false;
		this.tipoMensaje = 0;
		this.mensaje = "";
		this.mensajeDetalles = "";
	}
	
	private void control(String mensaje) {
		control(false, -1, mensaje, null);
	}

	private void control(String mensaje, Throwable throwable) {
		control(false, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCompleta, int tipoMensaje, String mensaje, Throwable throwable) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
		StringWriter sw = null;
		String codigo = "";
		
		codigo = "Codigo: " + formatter.format(Calendar.getInstance().getTime());
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "ERROR";

		this.operacionCompleta = operacionCompleta;
		this.tipoMensaje = tipoMensaje;
		this.mensaje = (mensaje == null) ? "" : mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = this.mensaje;
			this.mensajeDetalles += "<br><br>" + sw.toString();
		}
		
		log.error(this.sesion.getUsuario() + " :: " + codigo + " :: " + mensaje + this.mensajeDetalles, throwable);
	}

	// --------------------------------------------------------------------------------------
	// BUSQUEDA DINAMICA
	// --------------------------------------------------------------------------------------
	
	public void busqueda() {
		Map<String, String> btnParams = null;
		
		control();
		this.valorBusquedaDinamica = "";
		this.numPaginaDinamica = 1;
		this.pojoValorDinamico = new SelectItem();
		
		if (this.listValoresDinamicos == null)
			this.listValoresDinamicos = new ArrayList<SelectItem>();
		this.listValoresDinamicos.clear();
		
		btnParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (btnParams != null && ! btnParams.isEmpty()) {
			if (btnParams.containsKey("programParam"))
				this.currentParam = ((String) btnParams.get("programParam"));

			if (btnParams.containsKey("targetRender"))
				this.renders = ((String) btnParams.get("targetRender") + ("".equals(this.renders) ? "" : ","));

			if (btnParams.containsKey("paramGrupo"))
				this.paramGrupo = Long.valueOf((String) btnParams.get("paramGrupo")).longValue();
		}
		
		controlCompleto();
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
			if (this.listValoresDinamicos == null)
				this.listValoresDinamicos = new ArrayList<SelectItem>();
			this.listValoresDinamicos.clear();
			
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
					
					// Ordenamos
					Collections.sort(this.listValoresDinamicos, new Comparator<SelectItem>() {
						@Override
						public int compare(SelectItem o1, SelectItem o2) {
							return o1.getLabel().compareTo(o2.getLabel());
						}
					});
				}
			}

			controlCompleto();
		} catch (Exception e) {
			control("Ocurrio un problema al realizar la Busqueda", e);
		}
	}
	
	public void seleccionar() {
		try {
			control();
			if (this.pojoValorDinamico == null) {
				control("Ocurrio un problema al recuperar el elemento seleccionado.");
				return;
			}
			
			this.param.put(this.currentParam, this.pojoValorDinamico.getValue().toString());
			this.pojoValorDinamico = null;
			this.currentParam = "";
			this.renders = "";
			this.paramGrupo = 0L;
			controlCompleto();
		} catch (Exception e) {
			control("Ocurrio un problema al recuperar el valor seleccionado", e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<SelectItem> getValores(long IdGrupoValor) throws Exception {
		HashMap<String, String> mapValores = new HashMap<String, String>();
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<SelectItem> valores = new ArrayList<SelectItem>();
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
			if ((jsonObject.has("valores")) && (! "".equals(jsonObject.get("valores").getAsString().trim()))) {
				log.info("Recuperando valores ... ");
				mapValores = (HashMap) gson.fromJson(jsonObject.get("valores").getAsString(), mapValores.getClass());
				for (Map.Entry<String, String> val : mapValores.entrySet())
					valores.add(new SelectItem(Long.valueOf(((String) val.getKey()).toString()), ((String) val.getValue()).toString()));
			}
			controlCompleto();
		} catch (Exception e) {
			log.error("ERROR EN BUSQUEDA AL PROCESAR RESULTADOS. \n\nPETICION: " + comando + ".\n\nRESPUESTA: " + respuesta, e);
			throw e;
		} finally {
			log.info("Ejecucion terminada!");
			if (socket != null)
				socket.close();
		}

		return valores;
	}
	
	// --------------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------------

	public boolean isOperacionCompleta() {
		return this.operacionCompleta;
	}
	
	public void setOperacionCompleta(boolean operacionCompleta) {
		this.operacionCompleta = operacionCompleta;
	}

	public int getTipoMensaje() {
		return this.tipoMensaje;
	}
	
	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
	
	public String getResOperacion() {
		return this.mensaje;
	}
	
	public void setResOperacion(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getMensajeDetalles() {
		return this.mensajeDetalles;
	}
	
	public void setMensajeDetalles(String mensajeDetalles) {
		this.mensajeDetalles = mensajeDetalles;
	}
	
	public Date getFechaEmpieza() {
		return this.fechaEmpieza;
	}
	
	public void setFechaEmpieza(Date fechaEmpieza) {
		this.fechaEmpieza = fechaEmpieza;
	}
	
	public String getRepetir() {
		return this.repetir;
	}
	
	public void setRepetir(String repetir) {
		this.repetir = repetir;
	}
	
	public boolean isDiaTodos() {
		return this.diaTodos;
	}
	
	public void setDiaTodos(boolean diaTodos) {
		this.diaTodos = diaTodos;
	}
	
	public boolean isDiaLunes() {
		return this.diaLunes;
	}
	
	public void setDiaLunes(boolean diaLunes) {
		this.diaLunes = diaLunes;
	}
	
	public boolean isDiaMartes() {
		return this.diaMartes;
	}
	
	public void setDiaMartes(boolean diaMartes) {
		this.diaMartes = diaMartes;
	}
	
	public boolean isDiaMiercoles() {
		return this.diaMiercoles;
	}
	
	public void setDiaMiercoles(boolean diaMiercoles) {
		this.diaMiercoles = diaMiercoles;
	}
	
	public boolean isDiaJueves() {
		return this.diaJueves;
	}
	
	public void setDiaJueves(boolean diaJueves) {
		this.diaJueves = diaJueves;
	}
	
	public boolean isDiaViernes() {
		return this.diaViernes;
	}
	
	public void setDiaViernes(boolean diaViernes) {
		this.diaViernes = diaViernes;
	}
	
	public boolean isDiaSabado() {
		return this.diaSabado;
	}
	
	public void setDiaSabado(boolean diaSabado) {
		this.diaSabado = diaSabado;
	}
	
	public boolean isDiaDomingo() {
		return this.diaDomingo;
	}
	
	public void setDiaDomingo(boolean diaDomingo) {
		this.diaDomingo = diaDomingo;
	}
	
	public int getCada() {
		return this.cada;
	}
	
	public void setCada(int cada) {
		this.cada = cada;
	}
	
	public Date getFechaFinaliza() {
		return this.fechaFinaliza;
	}
	
	public void setFechaFinaliza(Date fechaFinaliza) {
		this.fechaFinaliza = fechaFinaliza;
	}
	
	public String getFinalizaTipo() {
		return this.finalizaTipo;
	}
	
	public void setFinalizaTipo(String finalizaTipo) {
		this.finalizaTipo = finalizaTipo;
	}
	
	public int getNumVeces() {
		return this.numVeces;
	}
	
	public void setNumVeces(int numVeces) {
		this.numVeces = numVeces;
	}
	
	public HashMap<String, Object> getParam() {
		return this.param;
	}
	
	public void setParam(HashMap<String, Object> param) {
		this.param = param;
	}
	
	public int getValorFinaliza() {
		return this.valorFinaliza;
	}
	
	public void setValorFinaliza(int valorFinaliza) {
		this.valorFinaliza = valorFinaliza;
	}
	
	public String getEnviarA() {
		return this.enviarA;
	}
	
	public void setEnviarA(String enviarA) {
		this.enviarA = enviarA;
	}
	
	public String getAsunto() {
		return this.asunto;
	}
	
	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}
	
	public String getCuerpo() {
		return this.cuerpo;
	}
	
	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
	
	public boolean isExpandido() {
		return this.expandido;
	}
	
	public void setExpandido(boolean expandido) {
		this.expandido = expandido;
		System.out.println("expandido " + this.expandido);
	}
	
	public boolean isCorreoExpandido() {
		return this.correoExpandido;
	}
	
	public void setCorreoExpandido(boolean correoExpandido) {
		this.correoExpandido = correoExpandido;
		System.out.println("correoExpandido " + this.correoExpandido);
	}
	
	public List<SelectItem> getListValoresDinamicos() {
		return this.listValoresDinamicos;
	}
	
	public void setListValoresDinamicos(List<SelectItem> listValoresDinamicos) {
		this.listValoresDinamicos = listValoresDinamicos;
	}
	
	public SelectItem getPojoValorDinamico() {
		return this.pojoValorDinamico;
	}
	
	public void setPojoValorDinamico(SelectItem pojoValorDinamico) {
		this.pojoValorDinamico = pojoValorDinamico;
	}
	
	public String getValorBusquedaDinamica() {
		return this.valorBusquedaDinamica;
	}
	
	public void setValorBusquedaDinamica(String valorBusquedaDinamica) {
		this.valorBusquedaDinamica = valorBusquedaDinamica;
	}
	
	public int getNumPaginaDinamica() {
		return this.numPaginaDinamica;
	}
	
	public void setNumPaginaDinamica(int numPaginaDinamica) {
		this.numPaginaDinamica = numPaginaDinamica;
	}
	
	public String getRenders() {
		return this.renders;
	}
	
	public void setRenders(String renders) {
		this.renders = renders;
	}
	
	public long getParamGrupo() {
		return this.paramGrupo;
	}
	
	public void setParamGrupo(long paramGrupo) {
		this.paramGrupo = paramGrupo;
	}
}
