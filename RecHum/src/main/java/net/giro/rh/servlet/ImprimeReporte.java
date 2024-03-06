package net.giro.rh.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class ImprimeReporte extends HttpServlet {
	private static final long serialVersionUID = -6453089287301662517L;
	private static Logger log = Logger.getLogger(ImprimeReporte.class);
	
	public ImprimeReporte() {
		super();
	}

	public void init() throws ServletException { }

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OutputStream out = response.getOutputStream();		
		HttpSession session = null;
		byte[] bytes = null;
		String archivo = "";
		String formato = "";
		
		try {
			session = request.getSession(true);
			bytes 	= (byte[]) session.getAttribute("contenido");
			archivo = (String) session.getAttribute("nombreDocumento");
			formato = (String) session.getAttribute("formato");
			
			if (bytes == null || bytes.length <= 0)
				return;
			
			if (archivo == null || "".equals(archivo))
				archivo = "documento";
			
			if (formato == null)
				formato = "unknown";
			
			if (! archivo.contains("." + formato))
				archivo = String.format("%s.%s", archivo, formato);
			
			log.info("contentType " + formato);
			if ("doc".equals(formato) || "docx".equals(formato)) {
				response.setContentType("application/msword");
			    response.addHeader("Content-Disposition", "attachment; filename=" + archivo);
			} else if ("xls".equals(formato) || "xlsx".equals(formato)) {
				response.setContentType("application/vnd.ms-excel");
			    response.addHeader("Content-Disposition", "attachment; filename=" + archivo);
			} else if ("pdf".equals(formato)) {
				response.setContentType("application/pdf");
			    response.addHeader("Content-Disposition", "inline; filename=" + archivo);
			} else {
			    response.addHeader("Content-Disposition", "attachment; filename=" + archivo);
			}

			response.setCharacterEncoding("ANSI");
		    response.setContentLength(bytes.length);
		    out.write(bytes, 0, bytes.length);
		    out.flush();
		    out.close();
		} catch (Exception e) {
			log.error("Error en metodo doGet", e);
		}
		/*OutputStream out = response.getOutputStream();		
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
			
			/ *
			if(!"".equals(aplicacion) && aplicacion != null)
				response.setContentType(aplicacion);
			* /
			String nombre = snombreDoc;
			if("".equals(nombre) || nombre == null)
				nombre = "archivo";
			
			//response.setContentType("application/pdf");
			if(nombre.contains("pdf"))
				response.addHeader("Content-Disposition",  "inline; filename=" + nombre);
			else
				response.addHeader("Content-Disposition",  "attachment; filename=" + nombre);
			response.setCharacterEncoding("ANSI");
		    response.setContentLength(bytes.length);
		    out.write(bytes, 0, bytes.length);
		    out.flush();
		    out.close();
		} catch (Exception e) {
		  log.error("Error en metodo doGet", e);
		}	*/
	}
}
