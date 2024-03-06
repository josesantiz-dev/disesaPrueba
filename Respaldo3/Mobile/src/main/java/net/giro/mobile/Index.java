package net.giro.mobile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class Obras
 */
@WebServlet("/Index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Index.class);
	//private InitialContext ctx;
	// --------------------------------------------- 
	private DatosUsuario datosUsuario;
	//private long usuarioId;
	private boolean usuarioInvalido;
	
	public Index() {
		super();
        
		try {
			this.datosUsuario = new DatosUsuario();
			this.datosUsuario.inicializar();
			this.usuarioInvalido = this.datosUsuario.isUsuarioAutorizado();
			//this.usuarioId = this.datosUsuario.getUsuarioId();
			
			/*this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObras.setInfoSesion(this.datosUsuario.getInfoSesion());*/
		} catch (Exception e) {
			log.error("Imposible crear interfaz Obra", e);
			//this.ctx = null;
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String operacion[] = null;
		//String sRespuesta = "";

		this.datosUsuario.inicializar(request, response);
		this.usuarioInvalido = this.datosUsuario.isUsuarioAutorizado();
		if (! this.usuarioInvalido)
			return;
		response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json");
        response.getWriter().write("");

		/*this.usuarioId = datosUsuario.getUsuarioId();
		operacion = request.getParameterValues("operacion");
		if (operacion[0].equals("listarObras"))
			sRespuesta = new Gson().toJson(getListaObras());
		else if( operacion[0].equals("autorizarObra")) 
			sRespuesta = new Gson().toJson(autorizarObra(request.getParameterValues("idObra")[0]));
		else if (operacion[0].equals("salir")) 
			sRespuesta = new Gson().toJson(cerrarSesion());
		else
			return;

		response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(sRespuesta);*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public boolean isUsuarioInvalido() {
		return usuarioInvalido;
	}

	public void setUsuarioInvalido(boolean usuarioInvalido) {
		this.usuarioInvalido = usuarioInvalido;
	}
}
