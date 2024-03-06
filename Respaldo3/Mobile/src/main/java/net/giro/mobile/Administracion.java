package net.giro.mobile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class Administracion
 */
@WebServlet("/Administracion")
public class Administracion extends HttpServlet {	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Administracion.class);

	//--------------------------------------------- Variables de usuario
	/*DatosUsuario datosUsuario;
	String usuario;
	int usuarioId;*/
	//--------------------------------------------- Fin variables de usuario
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Administracion() {
        super();
        
		/*this.datosUsuario = new DatosUsuario();
		this.datosUsuario.inicializar();
		this.usuario = datosUsuario.getUsuario();
		this.usuarioId = datosUsuario.getUsuarioId();*/
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operacion[] = request.getParameterValues("operacion");
		//String sRespuesta = "";

		//response.setContentType("text/html;charset=UTF-8");
		if( operacion[0].equals("salir") ) {
			cerrarSesion();
			//sRespuesta = new Gson().toJson(cerrarSesion());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private String cerrarSesion() { 
		DatosUsuario datosUsuario = new DatosUsuario();
		String respuesta = "";
		
		//respuesta = sesion.cerrarSession();
		respuesta = datosUsuario.cerrarSession();
		log.info(respuesta);
		
		return respuesta;
	}
}
