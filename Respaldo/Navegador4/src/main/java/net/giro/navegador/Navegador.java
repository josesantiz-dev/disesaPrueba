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
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.giro.plataforma.Pagina;
import net.giro.plataforma.Utilerias;
import net.giro.plataforma.seguridad.beans.Menu;

import org.apache.log4j.*;
import org.richfaces.component.AbstractDropDownMenu;
import org.richfaces.component.Mode;
import org.richfaces.component.UICommandLink;
import org.richfaces.component.UIDropDownMenu;
import org.richfaces.component.UIMenuGroup;
import org.richfaces.component.UIMenuItem;
import org.richfaces.component.UIToolbar;
import org.richfaces.component.UIToolbarGroup;

public class Navegador implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Navegador.class);

	private AppMediator mediator;
	private ServletContext servletCon;
	private HttpSession httpSession;
	private ExpressionFactory expressionFactory;
	// HtmlAjaxSupport a4jSupportComp = null;
	/*
	 * private ValueExpression target = null; private ValueExpression valor = null;
	 */
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
	private UIToolbar htmlMenu = null;
	private HashMap<Integer, String> listIcons;
	private String pagPrincipal = "principal.faces";
	private String formatoFecha;

	public Navegador() {
		FacesContext facesContext = null;
		Application app = null;
		ValueExpression dato = null;
		PropertyResourceBundle prop = null;

		try {
			facesContext = FacesContext.getCurrentInstance();
			app = facesContext.getApplication();

			this.httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
			this.servletCon = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			this.urlHome = this.servletCon.getContextPath() + "/index.faces";
			this.urlApp = this.servletCon.getContextPath();
			this.expressionFactory = app.getExpressionFactory();

			dato = this.expressionFactory.createValueExpression(facesContext.getELContext(), "#{msg}",
					PropertyResourceBundle.class);
			prop = (PropertyResourceBundle) dato.getValue(facesContext.getELContext());
			this.listIcons = new HashMap<Integer, String>();
			this.listIcons.put(0, prop.getObject("navegacion.menu.folder22.img").toString());
			this.listIcons.put(1, prop.getObject("navegacion.menu.catalogo22.img").toString());
			this.listIcons.put(2, prop.getObject("navegacion.menu.operacion22.img").toString());
			this.listIcons.put(3, prop.getObject("navegacion.menu.reportes22.img").toString());

			this.formatoFecha = prop.getString("formatoFecha");
			this.origenCambioPass = "";
		} catch (Exception e) {
			log.error("Error en constructor Navegador()", e);
		}
	}

	public void dispose() {
		if (log.isDebugEnabled()) {
			log.debug(" Disposing LoginManager");
		}
	}

	public void inicializa() {
		numPag = 1;
		miPag = new Pagina();
		menuRoot = null;
		mensaje = "Ocurrio un problema, no se pudo mostrar la pagina solicitada!";
		// miPag.setUrl("/inicio.jsp");
		// miPag.setPrompt("Inicio");
	}

	public Pagina getMenu() {
		System.out.println("Navegador4: getMenu");
		try {
			if (this.menuRoot == null) {
				System.out.println("Navegador4: menuRoot nullo");
				System.out.println(this.mediator.getLoginManager().isLogeado());
				System.out.println(this.mediator.getLoginManager().isTieneResponsabilidades());
				System.out.println(this.mediator.getLoginManager().getUsuario());
				if (this.mediator.getLoginManager().isLogeado()) {
					System.out.println("Navegador4: isLogeado");
					Menu menu = this.mediator.getLoginManager().getInfoSesion().getResponsabilidad()
							.getResponsabilidad().getMenu();
					System.out.println(menu.getMenu());
					this.menuRoot = this.mediator.getLoginManager().getAutentificacion().getMenu(menu);
					System.out.println("Este es el menu obtenido");
					System.out.println(this.menuRoot.getUrl());
					System.out.println(this.menuRoot.getDescripcion());
					System.out.println(this.menuRoot.getId());
					System.out.println(this.menuRoot.getParametros());
					System.out.println(this.menuRoot.getSubMenu());
				}
			}
		} catch (Exception e) {
			log.error("Eror en metodo getMenu", e);
		}
		return this.menuRoot;
	}

	/**
	 * recorre primero el menu seleccionado para buscar en el primer nivel aquellos
	 * nodos que tienen subnodos y crear ToolBarGroups de manera horizontal y este a
	 * su vez se envia a llenar en construye arbol
	 * 
	 * @return
	 */
	public UIToolbar getArbolMenu() {
		UICommandLink link = null;
		FacesContext ctx = null;
		UIToolbarGroup tbGral = null;
		UIDropDownMenu grupo = null;

		// FIXME
		System.out.println("Navegador4: Debugeando");
		try {
			ctx = FacesContext.getCurrentInstance();
			this.servletCon = ((ServletContext) ctx.getExternalContext().getContext());
			this.htmlMenu = ((UIToolbar) ctx.getApplication().createComponent(ctx, "org.richfaces.Toolbar",
					"org.richfaces.ToolbarRenderer"));
			getMenu();
			// FIXME
			System.out.println("Obtubo menu");
			
			this.id = 0;
			if (this.menuRoot != null) {
				HtmlOutputText label = null;
				for (Pagina p : this.menuRoot.getSubMenu()) {
					System.out.println("Este es la descripcion de un submenu");
					System.out.println(p.getPrompt());
					System.out.println(p.getPrompt());
					System.out.println(p.getSubMenu());
					
					tbGral = new UIToolbarGroup();
					tbGral.setId("item" + this.id++);
					if (p.getSubMenu() != null) {
						grupo = (UIDropDownMenu) ctx.getApplication().createComponent(ctx, "org.richfaces.DropDownMenu",
								"org.richfaces.DropDownMenuRenderer");
						label = (HtmlOutputText) ctx.getApplication().createComponent("javax.faces.HtmlOutputText");
						label.setValue(p.getPrompt());
						grupo.getFacets().put(AbstractDropDownMenu.Facets.label.name(), label);
						grupo.setMode(Mode.ajax);
						grupo.setHideDelay(0);
						grupo.setId("item" + this.id++);
						grupo.getAttributes().put("style", "color:#525252; text-align:left;");
						construyeArbol(grupo, p);
						tbGral.getChildren().add(grupo);
					} else {
						link = new UICommandLink();
						link.setId("item" + this.id++);
						link.setOnclick("window.location='" + this.servletCon.getContextPath()
								+ p.getUrl().replace(".jsp", ".faces") + "'");
						link.setStyleClass("TituloGral");
						link.setValue(p.getPrompt());
						link.setData("IDPAG:" + p.getId());
						tbGral.getChildren().add(link);
					}
					this.htmlMenu.getChildren().add(tbGral);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR AL OBTENER EL MENU");
			log.error("Error al obtener el menu", e);
		}
		return this.htmlMenu;
	}

	public void setArbolMenu(UIToolbar drpMen) {
		this.htmlMenu = drpMen;
	}

	private Pagina getPaginaActual(Pagina root, int id) {
		try {
			for (Pagina p : root.getSubMenu()) {
				if (p.getSubMenu() != null)
					return getPaginaActual(p, id);
				else if (p.getId() == id)
					return p;
			}
		} catch (Exception e) {
			log.error("Error en metodo getPaginaActual", e);
		}

		return null;
	}

	public String getNombreArbol() {
		try {
			return getMenu().getPrompt();
		} catch (Exception e) {
			log.error("Error al obtener el nombre del Arbol", e);
			return "";
		}
	}

	private void construyeArbol(UIComponent comp, Pagina root) {
		FacesContext ctx = null;
		UIMenuGroup grupo = null;
		/*
		 * @SuppressWarnings("unused") MethodExpression me = null;
		 */
		// SetPropertyActionListenerImpl spa = null;

		if (root.getSubMenu() == null)
			return;

		try {
			ctx = FacesContext.getCurrentInstance();
			this.expressionFactory = ctx.getApplication().getExpressionFactory();

			UIMenuItem item = null;
			for (Pagina p : root.getSubMenu()) {
				if (p.getSubMenu() != null) {
					grupo = new UIMenuGroup();
					grupo.setLabel(p.getPrompt());
					grupo.setRendered(true);
					grupo.getAttributes().put("style", "color:#525252");
					grupo.setId("item" + p.getId() + id++);
					construyeArbol(grupo, p);
					comp.getChildren().add(grupo);
				} else {
					item = (UIMenuItem) ctx.getApplication().createComponent(ctx, UIMenuItem.COMPONENT_TYPE,
							"org.richfaces.MenuItemRenderer");
					item.setId("item" + p.getId() + id++);
					item.setLabel(p.getPrompt());
					item.setOnclick("window.location='" + this.servletCon.getContextPath()
							+ p.getUrl().replace(".jsp", ".faces") + "'");
					item.getAttributes().put("style", "color:#525252");

					/*
					 * a4jSupportComp =
					 * (HtmlAjaxSupport)vFacesContext.getCurrentInstance().getApplication().
					 * createComponent(HtmlAjaxSupport.COMPONENT_TYPE);
					 * a4jSupportComp.setEvent("onclick"); a4jSupportComp.setId("status" + p.getId()
					 * + id++);
					 * 
					 * valor =
					 * expressionFactory.createValueExpression(FacesContext.getCurrentInstance().
					 * getELContext(), String.valueOf(p.getId()), Integer.class); //spa = new
					 * SetPropertyActionListenerImpl(target, valor);
					 * //a4jSupportComp.addActionListener(spa);
					 * 
					 * item.getFacets().put(AjaxSupportTag.AJAX_SUPPORT_FACET+"complete",
					 * a4jSupportComp); item.getAttributes().put("style", "color:#525252");
					 * item.setValue(p.getPrompt()); item.setOncomplete("window.location='" +
					 * servletCon.getContextPath() + p.getUrl().replace(".jsp", ".faces") + "'");
					 * item.setSubmitMode("ajax");
					 */
					comp.getChildren().add(item);
				}
			}
		} catch (Exception e) {
			log.error("Error en metodo construye Arbol", e);
		}
	}

	public boolean accesoPagina(String url) {
		try {
			return buscaPagina(getMenu(), Utilerias.getPagina(url, "faces"));
		} catch (Exception re) {
			log.error("Error en el metodo accesoPagina", re);
		}

		return false;
	}

	public void hoy(ActionEvent event) {
		this.miPag.setUrl("hoy.jsp");
		this.miPag.setPrompt("Hoy");
	}

	public void reportes(ActionEvent event) {
		hoy(event);
	}

	public void verreportes(ActionEvent event) {
		hoy(event);
	}

	private boolean buscaPagina(Pagina pagR, String pagina) {
		boolean result = false;

		if (pagR != null) {
			for (Pagina p : pagR.getSubMenu()) {
				result = (p.getSubMenu() != null && buscaPagina(p, pagina) == true) || result;
				result = (p.getUrl() != "" && Utilerias.getPagina(p.getUrl(), "faces").equals(pagina)) || result;
			}
		}

		return result;
	}

	public void iniciaSession() {
		this.httpSession.setAttribute("Logeado", Boolean.toString(true));
		this.menuRoot = null;
	}

	public boolean sessionAbierta() {
		return httpSession.getAttribute("Logeado") != null
				&& Boolean.valueOf(httpSession.getAttribute("Logeado").toString());
	}

	public String cerrarSession() {
		FacesContext facesContext = null;
		ExternalContext ec = null;
		String res = "CerroSession";

		try {
			facesContext = FacesContext.getCurrentInstance();
			ec = facesContext.getExternalContext();

			this.httpSession = (HttpSession) ec.getSession(false);
			this.mediator.shutdown();
			this.httpSession.invalidate();

			ec.redirect("/cas/logout?service=https://disesa.condese.net:9443/DISESA");
		} catch (Exception re) {
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

			this.httpSession = (HttpSession) ec.getSession(false);
			this.mediator.shutdown();
			this.httpSession.invalidate();

			ec.redirect("/DISESA/index.faces");
		} catch (Exception re) {
			log.error("Error al cerrarSession", re);
			res = "ERROR";
		}
		return res;
	}

	public String getFecha() {
		SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
		return sdf.format(Calendar.getInstance().getTime());
	}

	public String irHome() {
		return this.urlHome;
	}

	// --------------------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------------------

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
		return this.miPag.getUrl() != null && this.miPag.getUrl() != null ? this.miPag.getUrl() : "index.jsp";
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

	public void setHtmlMenu(UIToolbar htmlMenu) {
		this.htmlMenu = htmlMenu;
	}

	public UIToolbar getHtmlMenu() {
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
