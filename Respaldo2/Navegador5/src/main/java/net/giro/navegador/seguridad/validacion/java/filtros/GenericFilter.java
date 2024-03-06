package net.giro.navegador.seguridad.validacion.java.filtros;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;

public class GenericFilter implements Filter{
	private FilterConfig filterConfig;
	
	public void destroy() {
		 this.filterConfig = null;
	}

	public void doFilter(ServletRequest svlRequest, ServletResponse svlResponse,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(svlRequest,svlResponse);
	}

	public void init(FilterConfig config) throws ServletException {
		 this.filterConfig = config;
	}
	
	public FilterConfig getFilterConfig()
    {
        return filterConfig;
    }
}
