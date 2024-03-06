package net.giro.navegador.seguridad.validacion.java.filtros;

import java.io.IOException;

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

public class LoginFilter implements Filter{
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
		HttpServletRequest hrequest = ( HttpServletRequest )request;
		HttpServletResponse hresponse = ( HttpServletResponse )response;
		HttpSession session = hrequest != null ? (HttpSession)hrequest.getSession(false) : null;
		
		boolean logeado = session==null || session.getAttribute("Logeado") == null ? false : Boolean.valueOf(session.getAttribute("Logeado").toString());  
		
		if(logeado){
			hresponse.sendRedirect(servletContext.getContextPath() + "/administracion/principal.faces");
			return;
		}
		
		chain.doFilter(hrequest,hresponse);
		
	}
}
