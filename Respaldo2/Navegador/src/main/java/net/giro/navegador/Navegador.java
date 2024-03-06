package net.giro.navegador;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.PropertyResourceBundle;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.giro.plataforma.Pagina;
import net.giro.plataforma.Utilerias;
import net.giro.plataforma.seguridad.beans.Menu;

import org.ajax4jsf.component.html.HtmlAjaxCommandLink;
import org.ajax4jsf.component.html.HtmlAjaxSupport;
import org.ajax4jsf.taglib.html.jsp.AjaxSupportTag;
import org.apache.log4j.*;
import org.richfaces.component.html.HtmlDropDownMenu;
import org.richfaces.component.html.HtmlMenuGroup;
import org.richfaces.component.html.HtmlMenuItem;
import org.richfaces.component.html.HtmlToolBar;
import org.richfaces.component.html.HtmlToolBarGroup;

import com.sun.faces.taglib.jsf_core.SetPropertyActionListenerImpl;

public class Navegador implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Navegador.class);
	private ExpressionFactory expressionFactory;
	private ServletContext servletCon;
	private HttpSession httpSession;
    private AppMediator mediator;
	private String mensaje;
	private String varTmp;
	private String urlHome;
	private String origenCambioPass;
	private String urlApp;
	private Pagina menuRoot;
	private Pagina miPag;
	private int paginaId;
	private int id;
	private int numPag;
	private HtmlToolBar htmlMenu;
	private HtmlAjaxSupport a4jSupportComp;
	private HashMap<Integer, String> listIcons;
	private ValueExpression target;
	private ValueExpression valor;
	private String pagPrincipal;
	private String formatoFecha;
    
	
	public Navegador() {
		FacesContext facesContext = null;
		Application app = null;
		ValueExpression dato = null;
		PropertyResourceBundle prop = null;
		
		facesContext = FacesContext.getCurrentInstance();
		this.httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
		this.servletCon = (ServletContext) facesContext.getExternalContext().getContext();
		this.urlHome = this.servletCon.getContextPath() + "/index.faces";
		this.urlApp = this.servletCon.getContextPath();
		
		app = facesContext.getApplication();
		this.expressionFactory = app.getExpressionFactory();
		
		dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
		prop = (PropertyResourceBundle) dato.getValue(facesContext.getELContext());
		
		this.listIcons = new HashMap<Integer, String>();
		this.listIcons.put(0, prop.getObject("navegacion.menu.folder22.img").toString());
		this.listIcons.put(1, prop.getObject("navegacion.menu.catalogo22.img").toString());
		this.listIcons.put(2, prop.getObject("navegacion.menu.operacion22.img").toString());
		this.listIcons.put(3, prop.getObject("navegacion.menu.reportes22.img").toString());
		
		this.formatoFecha = prop.getString("formatoFecha");
		//inicializa();
		this.origenCambioPass = "";
		this.pagPrincipal="principal.faces";
	}
    
	
    public void inicializa() {
    	numPag = 1;
    	miPag = new Pagina();
    	menuRoot = null;
    	mensaje = "Ocurrio un problema, no se pudo mostrar la pagina solicitada!";
		miPag.setUrl("/inicio.jsp");
		miPag.setPrompt("Inicio");
    }

	public void iniciaSession(){
		this.httpSession.setAttribute("Logeado", Boolean.toString(true));
		this.menuRoot = null;
    }
    
    public boolean sessionAbierta(){
        return httpSession.getAttribute("Logeado") != null && Boolean.valueOf(httpSession.getAttribute("Logeado").toString());
    }
	
    public void refreshLocation() {
    	FacesContext facesContext = null;
		ExternalContext ec = null;
		
		try {
			facesContext = FacesContext.getCurrentInstance();
			ec = facesContext.getExternalContext();
			ec.redirect("./index.faces");
		} catch (Exception re) {
			log.error("Error al refreshLocation", re);
		}
    }
	
    public String cerrarSession() { 
		FacesContext facesContext = null;
		ExternalContext ec = null;
		String res = "CerroSession";
		
		try { 
			facesContext = FacesContext.getCurrentInstance();
			ec = facesContext.getExternalContext();
			ec.redirect("/cas/logout?service=https://disesa.condese.net:9443/DISESA/index.faces");
			
	        this.httpSession = (HttpSession) ec.getSession(false);
			this.mediator.shutdown();
	        this.httpSession.invalidate();
		} catch(Exception re) {
			log.error("Error al cerrarSession", re);
			res = "ERROR";
		}
		
		return res;
	}
    
    public String cambiarModulo() { 
		FacesContext facesContext = null;
		ExternalContext ec = null;
		String res = "CerroSession";
		
		try {
			facesContext = FacesContext.getCurrentInstance();
			ec = facesContext.getExternalContext();
			ec.redirect("/DISESA/index.faces");
			
	        this.httpSession = (HttpSession) ec.getSession(false);			
	        this.mediator.shutdown();
	        this.httpSession.invalidate();
		} catch (Exception re) {
			log.error("Error al cerrarSession", re);
			res = "ERROR";
		}
		
		return res;
	}

    public void dispose() {
        if (log.isDebugEnabled()) {
            log.debug(" Disposing LoginManager");
        }
    }

	public Pagina getMenu() {
		try {
			if (this.menuRoot == null) { 
				if (this.mediator.getLoginManager().isLogeado()) {
					Menu menu = this.mediator.getLoginManager().getInfoSesion().getResponsabilidad().getResponsabilidad().getMenu();
					this.menuRoot = this.mediator.getLoginManager().getAutentificacion().getMenu(menu);
				}
			}
		} catch (Exception e) {
			log.error("Eror en metodo getMenu",e);
		}
		
    	return menuRoot;		
	}
	
	/**
	 * recorre primero el menu seleccionado para buscar en el primer nivel aquellos nodos que tienen subnodos y crear ToolBarGroups de
	 * manera horizontal y este a su vez se envia a llenar en construye arbol
	 * @return
	 */
	public HtmlToolBar getArbolMenu(){
		HtmlToolBarGroup tbGral = null;
		HtmlDropDownMenu grupo = null;
		HtmlAjaxCommandLink link = null;
		
		try {
			this.id = 0;
			this.htmlMenu = new HtmlToolBar();
			this.servletCon = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			this.expressionFactory = FacesContext.getCurrentInstance().getApplication().getExpressionFactory();
			this.target = this.expressionFactory.createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{navegador.paginaId}", Integer.class);
			 
			getMenu();
			
			for (Pagina p : this.menuRoot.getSubMenu()) {
				tbGral = new HtmlToolBarGroup();
				tbGral.setId("item" + id++);
				if (p.getSubMenu() != null) {
					grupo = new HtmlDropDownMenu();
					grupo.setId("item" + id++);
					grupo.setValue(p.getPrompt());
					grupo.getAttributes().put("style", "color:#525252; text-align:left;");
					construyeArbol(grupo, p);
					tbGral.getChildren().add(grupo);
				} else {
					link = new HtmlAjaxCommandLink();
					link.setId("item" + id++);
					link.setOncomplete("window.location='" + servletCon.getContextPath() + p.getUrl().replace(".jsp", ".faces").replace(".xhtml", ".faces") + "'");
					link.setStyleClass("TituloGral");
					link.setValue(p.getPrompt());
					tbGral.getChildren().add(link);
				}
				
				this.htmlMenu.getChildren().add(tbGral);
			}
		} catch (Exception e) {
			log.error("Error al obtener el menu", e);
		}
		
		return this.htmlMenu;
	}
	
	public void setArbolMenu(HtmlToolBar drpMen){
		this.htmlMenu = drpMen;
	}
	
	private Pagina getPaginaActual(Pagina root, int id){
		try {
			for (Pagina p:root.getSubMenu()) {
				if (p.getSubMenu() != null)
					return getPaginaActual(p, id);
				else if(p.getId() == id)
					return p;
			}
		} catch (Exception e) {
			log.error("Error en metodo getPaginaActual", e);
		}
		
		return null;
	}
	
	public String getNombreArbol(){
		try {
			return getMenu().getPrompt();
		} catch (Exception e) {
			log.error("Error al obtener el nombre del Arbol", e);
			return "";
		}
	}
	
	private void construyeArbol(UIComponent comp, Pagina root){
		HtmlMenuGroup grupo = null;
		HtmlMenuItem item = null;
		SetPropertyActionListenerImpl spa = null;
		
		if(root.getSubMenu() == null)
			return;
		
		try {
			for (Pagina p : root.getSubMenu()) {
				if (p.getSubMenu() != null) {
					grupo = new HtmlMenuGroup();
					grupo.setValue(p.getPrompt());
					grupo.getAttributes().put("style", "color:#525252");
					grupo.setId("item" + id++);
					construyeArbol(grupo, p);
					comp.getChildren().add(grupo);
				} else {
					item = new HtmlMenuItem();
					item.setId("item" + id++);
					a4jSupportComp = (HtmlAjaxSupport) FacesContext.getCurrentInstance().getApplication().createComponent(HtmlAjaxSupport.COMPONENT_TYPE);
					a4jSupportComp.setEvent("onclick");
					a4jSupportComp.setId("status" + id++);
					
					valor = expressionFactory.createValueExpression(FacesContext.getCurrentInstance().getELContext(), String.valueOf(p.getId()), Integer.class);
					spa = new SetPropertyActionListenerImpl(target, valor);
					a4jSupportComp.addActionListener(spa);
					
					item.getFacets().put(AjaxSupportTag.AJAX_SUPPORT_FACET+"complete", a4jSupportComp);
					item.getAttributes().put("style", "color:#525252");
					item.setValue(p.getPrompt());
					item.setOncomplete("window.location='" + servletCon.getContextPath() + p.getUrl().replace(".jsp", ".faces") + "'");
					item.setSubmitMode("ajax");					
					comp.getChildren().add(item);
				}
			}
		} catch (Exception e) {
			log.error("Error en metodo costruye Arbol", e);
		}
	}
	
	public boolean accesoPagina(String url){
		try{		
			return buscaPagina(getMenu(), Utilerias.getPagina(url, "faces"));
		}catch(Exception re){
			log.error("Error en el metodo accesoPagina", re);
		}
		return false;
	}
	
	public void hoy(ActionEvent event) {
		this.miPag.setUrl("hoy.jsp");
		this.miPag.setPrompt("Hoy");
	}
	
	private boolean buscaPagina(Pagina pagR, String pagina){
		boolean result = false;
		if (pagR != null)
			for (Pagina p : pagR.getSubMenu()) {
				result = (p.getSubMenu() != null && buscaPagina(p, pagina)==true) || result; 
				result = (p.getUrl()!="" && Utilerias.getPagina(p.getUrl(), "faces").equals(pagina)) || result;
			}
		return result;
	}
	
	public String getFecha() {		
		SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	public String irHome(){
		return this.urlHome;
	}
	
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setpagSeleccionada(String pagSeleccionada) {
		this.miPag.setUrl(pagSeleccionada);
	}
	
	public String getpagSeleccionada() {
		return this.miPag.getUrl()!=null && this.miPag.getUrl()!=null ? this.miPag.getUrl(): "index.jsp";
	}

	public void setPagPrincipal(String pagPrincipal) {
		this.pagPrincipal = pagPrincipal;
	}

	public String getPagPrincipal() {
		return pagPrincipal;
	}

	public void setPagTitulo(String pagTitulo) {
		this.miPag.setPrompt(pagTitulo);
	}

	public String getPagTitulo() {
		return this.miPag.getPrompt();
	}
	
	public void setMiPag(Pagina miPag) {
		this.miPag = miPag;
	}

	public void reportes(ActionEvent event) {
		hoy(event);
	}
	
	public void verreportes(ActionEvent event) {
		hoy(event);
	}
	
	public Pagina getMiPag() {
		return getPaginaActual(menuRoot, paginaId);
	}
	
	public void setVarTmp(String varTmp) {
		inicializa();
		this.varTmp = varTmp;
	}
	
	public String getVarTmp() {
		return varTmp;
	}
	
	public void setMediator(AppMediator mediator) {
		this.mediator = mediator;
	}
	
	public AppMediator getMediator() {
		return mediator;
	}
	
	public void setHtmlMenu(HtmlToolBar htmlMenu) {
		this.htmlMenu = htmlMenu;
	}
	
	public HtmlToolBar getHtmlMenu() {
		return htmlMenu;
	}

	public void setPaginaId(int paginaId) {
		this.paginaId = paginaId;
	}

	public int getPaginaId() {
		return paginaId;
	}

	public void setNumPag(int numPag) {
		this.numPag = numPag;
	}

	public int getNumPag() {
		return numPag;
	}

	public void setUrlHome(String urlHome) {
		this.urlHome = urlHome;
	}

	public String getUrlHome() {
		return urlHome;
	}

	public void setOrigenCambioPass(String origenCambioPass) {
		this.origenCambioPass = origenCambioPass;
	}

	public String getOrigenCambioPass() {
		return origenCambioPass;
	}

	public String getUrlApp() {
		return urlApp;
	}

	public void setUrlApp(String urlApp) {
		this.urlApp = urlApp;
	}
}
