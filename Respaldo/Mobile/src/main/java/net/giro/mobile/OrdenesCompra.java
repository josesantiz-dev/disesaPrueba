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

import net.giro.compras.beans.OrdenCompra;
import net.giro.compras.logica.OrdenCompraRem;

/**
 * Servlet implementation class OrdenesCompra
 */
@WebServlet("/OrdenesCompra")
public class OrdenesCompra extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OrdenesCompra.class);
    
	private InitialContext ctx;
	private OrdenCompraRem ifzOrdenCompra;
	
	//--------------------------------------------- Variables de usuario
	private DatosUsuario datosUsuario;
	//private String usuario;
	private long usuarioId;
	//--------------------------------------------- Fin variables de usuario
	
	
    public OrdenesCompra() {
        super();
		
		try{
			this.ctx = new InitialContext();
			this.ifzOrdenCompra = (OrdenCompraRem) this.ctx.lookup("ejb:/Logica_Compras//OrdenCompraFac!net.giro.compras.logica.OrdenCompraRem");
			
			this.datosUsuario = new DatosUsuario();
			this.datosUsuario.inicializar();
			//this.usuario = this.datosUsuario.getUsuario();
			this.usuarioId = this.datosUsuario.getUsuarioId();
		} catch (Exception e) {
			log.error("Imposible crear interfaz OrdenCompra", e);
			this.ctx = null;
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operacion[] = request.getParameterValues("operacion");
		String sRespuesta = "";

		this.datosUsuario.inicializar(request, response);
		//this.usuario = this.datosUsuario.getUsuario();
		this.usuarioId = datosUsuario.getUsuarioId();
		
		if (operacion[0].equals("listarOrdenesCompra"))
			sRespuesta = new Gson().toJson(getListaOrdenesCompra());

		if (operacion[0].equals("getOrdenCompra"))
			sRespuesta = new Gson().toJson(getOrdenCompra(request.getParameterValues("idOrdenCompra")[0]));
		
		if (operacion[0].equals("autorizarOrdenCompra")) {
			String idOrdenCompra = request.getParameterValues("idOrdenCompra")[0];
			sRespuesta = new Gson().toJson(autorizarOrdenCompra(idOrdenCompra));
			log.info("Autorizando orden de compra: " + idOrdenCompra);
		}
		
		if (operacion[0].equals("salir"))
			sRespuesta = new Gson().toJson(cerrarSesion());

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

	private OrdenCompra getOrdenCompra(String sIdOrdenCompra){
		OrdenCompra orden = new OrdenCompra();
		
		try {
			if (sIdOrdenCompra == null || "".equals(sIdOrdenCompra)) return orden;
			orden = this.ifzOrdenCompra.findById(Long.parseLong(sIdOrdenCompra));
		} catch (Exception e) {
			log.error("Error obteniendo ordenes de compra", e);
		}
		
		return orden;
	}
	
	private List<OrdenCompra> getListaOrdenesCompra() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<OrdenCompra> listaOrdenesCompra = new ArrayList<>();
		
		try {
    		params.put("estatus", 0);
    		params.put("autorizado", 0);
    		listaOrdenesCompra = this.ifzOrdenCompra.findByProperties(params, 0);
			//listaOrdenesCompra = this.ifzOrdenCompra.findByProperty("autorizado", 0, 0);
		} catch (Exception e) {
			log.error("Error obteniendo ordenes de compra", e);
		}
		
		return listaOrdenesCompra;
	}

	private String autorizarOrdenCompra(String sIdOrdenCompra) {
		try{
			if (sIdOrdenCompra == null || "".equals(sIdOrdenCompra)) return "0";
			if (!this.datosUsuario.puedeAutorizar()) return "-1";
			
			OrdenCompra orden = this.ifzOrdenCompra.findById(Long.parseLong(sIdOrdenCompra));
			orden.setFechaAutorizacion(Calendar.getInstance().getTime());
			orden.setIdUsuarioAutorizo(this.usuarioId);
			orden.setAutorizado(1);
			
			this.ifzOrdenCompra.update(orden);
			return "1";
		} catch (Exception e) {
			log.error("Error autorizando ordenes de compra", e);
		}
		
		return "0";
	}

	private String cerrarSesion() {
		String respuesta = "";
		
		respuesta = datosUsuario.cerrarSession();		
		log.info(respuesta);
		
		return respuesta;
	}
}
