package cde.publico.validator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/*
 * Creamos la clase para corregir los cambios en scripts de firefox para las version superirores a 10, generan 
 * error al leer el arbol de objetos del componente rich:tree de richfaces: [javax.faces.FacesException: Error decode resource].
 * 
 * Adicionalmente añadimos el siguiente filtro al web.xml:
	<filter>
		<filter-name>RichFacesFirefox11Filter</filter-name>
		<filter-class>cde.publico.validator.RichFacesFirefox11Filter</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>RichFacesFirefox11Filter</filter-name>
		<url-pattern>/a4j/*</url-pattern>
	</filter-mapping>
 */

public class RichFacesFirefox11Filter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public String getRequestURI() {
                try {
                    return URLDecoder.decode(super.getRequestURI(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException("Cannot decode request URI.", e);
                }
            }
        }, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// do nothing
	}

	@Override
	public void destroy() {
		// do nothing
	}
}
