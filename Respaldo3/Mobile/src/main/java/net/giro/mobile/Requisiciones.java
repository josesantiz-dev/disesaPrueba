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

import net.giro.compras.beans.Requisicion;
import net.giro.compras.beans.RequisicionDetalle;
import net.giro.compras.logica.RequisicionDetalleRem;
import net.giro.compras.logica.RequisicionRem;

/**
 * Servlet implementation class Requisiciones
 */
@WebServlet("/Requisiciones")
public class Requisiciones extends HttpServlet {	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(Requisiciones.class);
	private InitialContext ctx;
	private RequisicionRem ifzRequisiciones;
	private RequisicionDetalleRem ifzReqDetalles;
	//--------------------------------------------- Variables de usuario
	private DatosUsuario datosUsuario;
	private long usuarioId;
	//--------------------------------------------- Fin variables de usuario
	
	
    public Requisiciones() {
        super();

        try {
			this.usuarioId = 0;
			this.datosUsuario = new DatosUsuario();
			this.datosUsuario.inicializar();
			this.usuarioId = datosUsuario.getUsuarioId();

        	this.ctx = new InitialContext();
			this.ifzRequisiciones = (RequisicionRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionFac!net.giro.compras.logica.RequisicionRem");
			this.ifzReqDetalles = (RequisicionDetalleRem) this.ctx.lookup("ejb:/Logica_Compras//RequisicionDetalleFac!net.giro.compras.logica.RequisicionDetalleRem");
			this.ifzRequisiciones.setInfoSesion(this.datosUsuario.getInfoSesion());
			this.ifzReqDetalles.setInfoSesion(this.datosUsuario.getInfoSesion());
        } catch (Exception e) {
			log.error("Error en constructor Requisiciones", e);
			this.ctx = null;
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String operacion[] = request.getParameterValues("operacion");
		String sRespuesta = "0";

		this.datosUsuario.inicializar(request, response);
		//this.usuario = datosUsuario.getUsuario();
		this.usuarioId = datosUsuario.getUsuarioId();
		
		if (operacion[0].equals("listarRequisiciones"))
			sRespuesta = new Gson().toJson(listarRequisiciones());

		if (operacion[0].equals("listarRequisicionDetalles"))
			sRespuesta = new Gson().toJson(listarRequisicionDetalles( request.getParameterValues("idRequisicion")[0]));
		
		if (operacion[0].equals("autorizarRequisicion")) {
			String idRequisicion = request.getParameterValues("idRequisicion")[0];
			sRespuesta = new Gson().toJson(autorizarRequisicion(idRequisicion));
			log.info("Autorizando requisicion: "+idRequisicion);
		}
		
		if (operacion[0].equals("salir"))
			sRespuesta = new Gson().toJson(cerrarSesion());
		
		response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(sRespuesta);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private List<Requisicion> listarRequisiciones() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		List<Requisicion> lista = new ArrayList<Requisicion>();
		
		try {
    		params.put("estatus", 0);
    		params.put("autorizado", 0);
    		lista = this.ifzRequisiciones.findByProperties(params, 0);
			//lista = this.ifzRequisiciones.findByProperty("autorizado", 0, 0);
		} catch (Exception e) {
			log.error("Error obteniendo lista de requisiciones", e);
		}
		
		return lista;
	}
	
	private String autorizarRequisicion(String sIdRequisicion) {
		try {
			if (sIdRequisicion == null || "".equals(sIdRequisicion)) return "0";
			if (! this.datosUsuario.puedeAutorizar()) return "-1";

			Requisicion requisicion = this.ifzRequisiciones.findById(Long.parseLong(sIdRequisicion));
			requisicion.setFechaAutorizacion(Calendar.getInstance().getTime());
			requisicion.setIdUsuarioAutorizo(this.usuarioId);
			requisicion.setAutorizado(1);
			
			this.ifzRequisiciones.update(requisicion);
			return "1";
		} catch (Exception e) {
			log.error("Error autorizando requisicion", e);
		}
		
		return "0";
	}
	
	private List<RequisicionDetalle> listarRequisicionDetalles(String sIdRequisicion) {
		List<RequisicionDetalle> listaDetalle = new ArrayList<>();
		
		try {
			if (sIdRequisicion != null && ! "".equals(sIdRequisicion))
				listaDetalle = this.ifzReqDetalles.findByProperty("idRequisicion", Long.parseLong(sIdRequisicion), 0);
		} catch (Exception e) {
			log.error("Error en el metodo listarRequisicionDetalles", e);
		}
		
		return listaDetalle;
	}
	
	private String cerrarSesion() {
		String respuesta = "";
		
		respuesta = datosUsuario.cerrarSession();		
		log.info(respuesta);
		
		return respuesta;
	}
}
