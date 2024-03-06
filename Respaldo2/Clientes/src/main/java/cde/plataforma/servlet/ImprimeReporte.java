package cde.plataforma.servlet;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class ImprimeReporte  extends HttpServlet {
	private static final long serialVersionUID = -6453089287301662517L;
	private static Logger log = Logger.getLogger(ImprimeReporte.class);
	
	public ImprimeReporte() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		OutputStream out = response.getOutputStream();		
		HttpSession session;
		String snombreDoc ;
		
		try {
			snombreDoc= null;
			session = request.getSession(true);
			byte[] bytes = (byte[])session.getAttribute("contenido");
			snombreDoc = String.valueOf(session.getAttribute("nombreDocumento"));
			
			if(bytes == null)
				return ;
			if (snombreDoc.equals("null")) {
				snombreDoc = "doc";
			}
			
			/*
			if(!"".equals(aplicacion) && aplicacion != null)
				response.setContentType(aplicacion);
			*/
			String nombre = snombreDoc;
			if("".equals(nombre) || nombre == null)
				nombre = "archivo";
			
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition",  "inline; filename=" + nombre);
			response.setCharacterEncoding("ANSI");
		    response.setContentLength(bytes.length);
		    out.write(bytes, 0, bytes.length);
		    out.flush();
		    out.close();
		} catch (Exception e) {
		  log.error("Error en metodo doGet", e);
		}		
	}

	public void init() throws ServletException {
	}
}
