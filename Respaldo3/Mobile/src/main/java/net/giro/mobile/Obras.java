package net.giro.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import net.giro.adp.beans.Obra;
import net.giro.adp.logica.ObraRem;

/**
 * Servlet implementation class Obras
 */
@WebServlet("/Obras")
public class Obras extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Obras.class);
	private InitialContext ctx;
	private ObraRem ifzObras;
	//--------------------------------------------- Variables de usuario
	private DatosUsuario datosUsuario;
	private long usuarioId;
	private boolean usuarioInvalido;
	//--------------------------------------------- Fin variables de usuario
    public static final String LOGIN_SUCCESS = "CONTINUA";
    public static final String LOGIN_FAILURE = "INCORRECTO";
	public static final String LOGOUT_SUCCESS = "LOGOUT EXITOSO";
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Obras() {
        super();
        
		try {
			this.datosUsuario = new DatosUsuario();
			this.datosUsuario.inicializar();
			this.usuarioInvalido = this.datosUsuario.isUsuarioAutorizado();
			this.usuarioId = datosUsuario.getUsuarioId();
			
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObras.setInfoSesion(this.datosUsuario.getInfoSesion());
		} catch (Exception e) {
			log.error("Imposible crear interfaz Obra", e);
			this.ctx = null;
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operacion[] = null;
		String sRespuesta = "";

		sRespuesta = "USER_NOT_VALID";
		this.datosUsuario.inicializar(request, response);
		this.usuarioInvalido = this.datosUsuario.isUsuarioAutorizado();
		if (this.usuarioInvalido) {
			sRespuesta = "OPERATION_NOT_VALID";
			this.usuarioId = this.datosUsuario.getUsuarioId();
			operacion = request.getParameterValues("operacion");
			if (operacion[0].equals("listarObras"))
				sRespuesta = new Gson().toJson(getListaObras());
			else if( operacion[0].equals("autorizarObra")) 
				sRespuesta = new Gson().toJson(autorizarObra(request.getParameterValues("idObra")[0]));
			else if (operacion[0].equals("salir")) 
				sRespuesta = new Gson().toJson(cerrarSesion());
		} 

		response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(sRespuesta);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private List<Obra> getListaObras() {
    	List<Obra> originales = new ArrayList<Obra>();
    	List<Obra> obras = new ArrayList<Obra>();
    	
    	try {
    		this.ifzObras.setInfoSesion(this.datosUsuario.getInfoSesion());
    		originales = this.ifzObras.findAll(0L, false, 1, 0, null); 
    		if (originales != null && ! originales.isEmpty()) {
	    		for (Obra var : originales) {
	    			if (var.getEstatus() <= 0) 
	    				continue;
	    			obras.add(var);
	    		}
    		}
    	} catch (Exception e) {
    		log.error("Error obteniendo listado de obras", e);
		}
    	
    	return obras;
    }
    
	private String autorizarObra(String sIdObra) {
		Obra obra = null;
		long idObra = 0;
		
		try {
			if (! this.datosUsuario.puedeAutorizar()) 
				return "-1";
			
			idObra = Long.parseLong(sIdObra);
			if (idObra <= 0)
				return "0";
			
			obra = this.ifzObras.findById(idObra);
			if (obra == null || obra.getId() == null || obra.getId() <= 0L)
				return "0";
			
			obra.setFechaAutorizacion(Calendar.getInstance().getTime());
			obra.setIdUsuarioAutorizo(this.usuarioId);
			obra.setAutorizado(1);
			this.ifzObras.update(obra);
		} catch (Exception e) {
			log.error("Ocurrio un problema al intentar Autorizar la Obra", e);
			return "0";
		}
		
		return "1";
	}

	private String cerrarSesion() {
		String respuesta = "";
		
		respuesta = this.datosUsuario.cerrarSession();		
		log.info(respuesta);
		
		return respuesta;
	}
}
