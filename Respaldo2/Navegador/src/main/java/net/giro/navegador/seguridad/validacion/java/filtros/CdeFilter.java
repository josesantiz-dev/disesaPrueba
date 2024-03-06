package net.giro.navegador.seguridad.validacion.java.filtros;

import javax.el.ValueExpression;



import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.giro.navegador.AppMediator;
import net.giro.plataforma.Utilerias;


import java.io.IOException;
import java.util.PropertyResourceBundle;

public class CdeFilter implements Filter{
	private FilterConfig filterConfig;
	private ServletContext servletContext;
	
	public void destroy() {
		 this.filterConfig = null;
	}

	public void init(FilterConfig config) throws ServletException {
		 this.filterConfig = config;
		 this.servletContext = filterConfig.getServletContext();
	}
	
	public FilterConfig getFilterConfig(){
        return filterConfig;
    }
	
	public void doFilter(final ServletRequest request,final ServletResponse response, FilterChain chain) throws IOException, ServletException {
		PropertyResourceBundle prop = null;
		HttpServletRequest hrequest = ( HttpServletRequest )request;
		HttpServletResponse hresponse = ( HttpServletResponse )response;
		HttpSession session = (HttpSession)hrequest.getSession(false);
		
		boolean logeado = session==null || session.getAttribute("Logeado") == null ? false : Boolean.valueOf(session.getAttribute("Logeado").toString());  
		
		if(!logeado){
			hresponse.sendRedirect("/FOCIR_WEB/login.faces");
			return;
		}
		/*obtengo el FacesContext asi por que hasta este punto el filtro se ejecuta antes de que se cree el FacesContext
		 * de la aplicacion, tonces tengo que crearlo ^^
		 */
		FacesContext fc = FacesContext.getCurrentInstance();
		if(fc == null)
			fc = getNuevoContexto(request, response);
		
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{mediator}", AppMediator.class);
		AppMediator cw = (AppMediator)ve.getValue(fc.getELContext());
		
		if(cw!=null && (cw.getNavegador().accesoPagina(hrequest.getRequestURI()) || Utilerias.getPagina(hrequest.getRequestURI(), "faces").equals(cw.getNavegador().getPagPrincipal())))
				chain.doFilter(hrequest,hresponse);
		else {
			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			prop = (PropertyResourceBundle)ve.getValue(fc.getELContext());
			cw.getNavegador().setMensaje(prop.getString("mensajes.error.sinPermisoPagin"));
			hresponse.sendRedirect("/FOCIR_WEB/errorFound.faces");
			return;
		}
		
	}
	
	private FacesContext getNuevoContexto(ServletRequest request, ServletResponse response) {
		try{
			FacesContext facesContext = FacesContext.getCurrentInstance();
	        if (facesContext == null) {
	            FacesContextFactory contextFactory  = (FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
	            LifecycleFactory lifecycleFactory = (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY); 
	            Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

	            facesContext = contextFactory.getFacesContext(this.servletContext, request, response, lifecycle);

	            UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "");
	            facesContext.setViewRoot(view);                
	        }
	
			return facesContext;
		}catch(Exception re){re.printStackTrace();}
		
		return null;
	}
}
