package net.giro.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
			this.usuarioId = datosUsuario.getUsuarioId();
			
			this.ctx = new InitialContext();
			this.ifzObras = (ObraRem) this.ctx.lookup("ejb:/Logica_GestionProyectos//ObraFac!net.giro.adp.logica.ObraRem");
			this.ifzObras.setInfoSesion(this.datosUsuario.getInfoSesion());
		} catch (Exception e) {
			log.error("Imposible crear interfaz Obra", e);
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operacion[] = request.getParameterValues("operacion");
		String sRespuesta = "";

		this.datosUsuario.inicializar(request, response);
		this.usuarioId = datosUsuario.getUsuarioId();
		
		if( operacion[0].equals("listarObras")) {
			sRespuesta = new Gson().toJson(getListaObras());
		}
		
		if( operacion[0].equals("autorizarObra")) {
			sRespuesta = new Gson().toJson(autorizarObra(request.getParameterValues("idObra")[0]));
		}
		
		if( operacion[0].equals("salir") ) {
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
		HashMap<String, Object> params = new HashMap<String, Object>();
    	List<Obra> obras = new ArrayList<Obra>();
    	List<Obra> originales = new ArrayList<Obra>();
    	
    	try {
    		params.put("revisado", 1);
    		params.put("autorizado", 0);
    		originales = this.ifzObras.findByProperties(params, 0);
    		
    		for (Obra var : originales) {
    			if (var.getEstatus() <= 0) continue;
    			obras.add(var);
    		}
    		
    		//obras = this.ifzObras.findByProperty("autorizado", 0);
    	} catch (Exception e) {
    		log.error("Error obteniendo listado de obras", e);
		}
    	
    	return obras;
    }
    
	private String autorizarObra(String sIdObra) {
		long idObra = Long.parseLong(sIdObra);	//Convertimos el string a entero
		
		if (! this.datosUsuario.puedeAutorizar()) 
			return "-1";
		
		Obra obra = this.ifzObras.findById(idObra);
		obra.setFechaAutorizacion(Calendar.getInstance().getTime());
		obra.setIdUsuarioAutorizo(this.usuarioId);
		obra.setAutorizado(1);
		
		try {
			this.ifzObras.update(obra);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	private String cerrarSesion() {
		String respuesta = "";
		
		respuesta = datosUsuario.cerrarSession();		
		log.info(respuesta);
		
		return respuesta;
	}
}
