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

	public void init() throws ServletException { }

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OutputStream out = null;
		HttpSession session = null;	
		byte[] contenido = null;
		String nombreDocumento = "";
		String formato = "";
		
		try {
			session = request.getSession(true);
			contenido = (byte[]) session.getAttribute("contenido");
			nombreDocumento = String.valueOf(session.getAttribute("nombreDocumento"));
			formato  = (String) session.getAttribute("formato");
			
			if (contenido == null)
				return;
			
			if (nombreDocumento == null || "".equals(nombreDocumento) || "null".equals(nombreDocumento))
				nombreDocumento = "documento";
			
			if (formato == null)
				formato = "";

			log.info("contentType " + formato);
			if ("doc".equals(formato) || "docx".equals(formato)) {
				response.setContentType("application/msword");
			    response.addHeader("Content-Disposition", "attachment; filename=" + nombreDocumento);
			} else if ("xls".equals(formato) || "xlsx".equals(formato)) {
				response.setContentType("application/vnd.ms-excel");
			    response.addHeader("Content-Disposition", "attachment; filename=" + nombreDocumento);
			} else if ("pdf".equals(formato)) {
				response.setContentType("application/pdf");
			    response.addHeader("Content-Disposition", "inline; filename=" + nombreDocumento);
			} else {
			    response.addHeader("Content-Disposition", "attachment; filename=" + nombreDocumento);
			}
			
			response.setCharacterEncoding("ANSI");
		    response.setContentLength(contenido.length);
		    out = response.getOutputStream();
		    out.write(contenido, 0, contenido.length);
		    out.flush();
		    out.close();
		} catch (Exception e) {
			log.error("Error en metodo doGet", e);
		}	
	}
}
