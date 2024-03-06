package net.giro.sesion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Map.Entry;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import net.izel.ws.seg.datos.Rol;
import net.giro.sesion.Grupo;
import net.giro.sesion.Subgrupo;
import net.giro.sesion.CMensajesRoles;
import net.giro.sesion.CRole;
import net.giro.sesion.CSoctetStream;
import net.giro.sesion.Mensaje;
import net.giro.sesion.Opciones;
import net.giro.sesion.Programa;
import net.giro.sesion.SesionProc;

import org.apache.log4j.Logger;

public class SesionProc implements Serializable {
	private static Logger log = Logger.getLogger(SesionProc.class);
	private static final long serialVersionUID = 1L;
	private Rol rolSeleccionado;
	private List<Rol> lstRoles;
	private String socket_ip;
	private String socket_puerto;
	private String rolAdmin;
	private Opciones opciones;
	private Programa programa;
	private Socket socket;
	private String usuario;
	private String claseOcultaProceso;
	private boolean isDebug;
	private String rol;
	
	
	public SesionProc() {
		FacesContext fc = null;
		Application app = null;
		HttpServletRequest req = null;
		ValueExpression dato = null;
		PropertyResourceBundle prop = null;
		
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();
			req = (HttpServletRequest) fc.getExternalContext().getRequest();
			this.usuario = req.getRemoteUser();
			
			dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgAdminProc}", PropertyResourceBundle.class);
			prop = (PropertyResourceBundle) dato.getValue(fc.getELContext());
			this.socket_ip = prop.getString("socket_ip");
			this.socket_puerto = prop.getString("socket_puerto");
			this.rolAdmin = prop.getString("rol_adm");
			this.lstRoles = new ArrayList<Rol>();
			
			obtenerDatosMenu();
		} catch (Exception e) {
			
		}
	}
	
	
	public List<Programa> getReportes(List<Long> listReportesValidos) {
		List<Long> programasAgregados = null;
		List<Programa> resultado = null;
		boolean filtrar = false;
		
		if (this.rol == null || ! "".equals(this.rol.trim())) {
			this.rolSeleccionado = this.lstRoles.get(0);
			this.rol = this.rolSeleccionado.getNombre();
			this.seleccionarRol();
		}
		
		if (this.opciones == null || this.opciones.getGrupos() == null || this.opciones.getGrupos().isEmpty())
			return null;
		
		if (listReportesValidos != null && ! listReportesValidos.isEmpty())
			filtrar = true;
		
		resultado = new ArrayList<Programa>();
		programasAgregados = new ArrayList<Long>();
		for (Entry<String, Grupo> itemGrupo : this.opciones.getGrupos().entrySet()) {
			for (Entry<String, Subgrupo> itemSubgrupo : itemGrupo.getValue().getSubgrupos().entrySet()) {
				for (Entry<String, Programa> itemPrograma : itemSubgrupo.getValue().getProgramas().entrySet()) {
					if (filtrar && listReportesValidos.contains(itemPrograma.getValue().getcPrograma().getId())) {
						if (! programasAgregados.contains(itemPrograma.getValue().getcPrograma().getId())) {
							resultado.add(itemPrograma.getValue());
							programasAgregados.add(itemPrograma.getValue().getcPrograma().getId());
						}
					} else if (! filtrar) {
						if (! resultado.contains(itemPrograma.getValue())){
							resultado.add(itemPrograma.getValue());
							programasAgregados.add(itemPrograma.getValue().getcPrograma().getId());
						}
					}
				}
			}
		}
		
		return resultado;
	}
	
	public void seleccionarRol() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		Mensaje msg = null;
		String respuesta = "";
		String comando = "";
		
		try {
			inicializaSocket();
			map.put("tipo", "020300");
			map.put("usuario", this.usuario);
			map.put("delUsuario", this.usuario);
			map.put("delRole", this.rolSeleccionado.getNombre());
			comando = gson.toJson(map);
			
			this.socket.getOutputStream().write(comando.getBytes());
			this.socket.getOutputStream().flush();
			this.claseOcultaProceso = "classHiden";
			if ((this.rolAdmin != null) && (this.rolAdmin.equals(this.rolSeleccionado.getNombre().toUpperCase()))) {
				this.claseOcultaProceso = "";
			}
			
			respuesta = CSoctetStream.read(this.socket.getInputStream());
			if (respuesta != null) {
				if (this.isDebug)
					log.info(respuesta);
				gson = new GsonBuilder().registerTypeAdapter(
						Mensaje.class, 
						new JsonSerializer<Mensaje>() {
							@Override
							public JsonElement serialize(Mensaje arg0, Type arg1, JsonSerializationContext arg2) {
								System.out.println(arg1.getClass().toString());
								return null;
							}
						}
				).create();
				msg = (Mensaje) gson.fromJson(respuesta, Mensaje.class);
				this.opciones = msg.getOpciones();
			}
		} catch (Exception e) {
			log.error("Error al intentar recuperar las opciones del Rol seleccionado", e);
		}
	}

	private void obtenerDatosMenu() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		CMensajesRoles msg = null; 
		List<CRole> roles = null;
		Gson gson = new Gson();
		String respuesta = "";
		String comando = "";
		Rol r = null;
		
		try {
			this.claseOcultaProceso = "classHiden";
			inicializaSocket();
			
			map.put("tipo", "020302");
			map.put("usuario", this.usuario);
			map.put("delUsuario", this.usuario);
			comando = gson.toJson(map);
			
			this.socket.getOutputStream().write(comando.getBytes());
			this.socket.getOutputStream().flush();
			respuesta = CSoctetStream.read(this.socket.getInputStream());
			if (respuesta != null) {
				msg = (CMensajesRoles) gson.fromJson(respuesta, CMensajesRoles.class);
				roles = msg.roles;
			}
			
			if (roles != null) {
				for (CRole rol : roles) {
					r = new Rol();
					r.setDescripcion("Rol de " + rol.getRole().toLowerCase());
					r.setImagen("DocumentNew.png");
					r.setNombre(rol.getRole());
					r.setVersion(Long.valueOf(1L));
					this.lstRoles.add(r);
				}
			} else {
				r = new Rol();
				r.setDescripcion("Rol de reporteador");
				r.setImagen("DocumentNew.png");
				r.setNombre("USUARIO");
				r.setVersion(Long.valueOf(1L));
				this.lstRoles.add(r);
			}
		} catch (Exception e) {
			log.error("Error al obtener los datos del menu", e);
		}
	}
	
	private void inicializaSocket() throws NumberFormatException, UnknownHostException, IOException {
		try {
			if ((this.socket != null) && (! this.socket.isClosed()))
				this.socket.close();
		} catch (Exception e) { 
			log.error("Error en getSocket(). No se pudo cerrar el socket", e);
		} finally {
			this.socket = null;
			this.socket = new Socket(this.socket_ip, Integer.valueOf(this.socket_puerto).intValue());
		}
	}

	// ---------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ---------------------------------------------------------------------------------------------------------

	public Rol getRolSeleccionado() {
		return rolSeleccionado;
	}

	public void setRolSeleccionado(Rol rolSeleccionado) {
		this.rolSeleccionado = rolSeleccionado;
	}

	public List<Rol> getLstRoles() {
		return lstRoles;
	}

	public void setLstRoles(List<Rol> lstRoles) {
		this.lstRoles = lstRoles;
	}

	public String getRolAdmin() {
		return rolAdmin;
	}

	public void setRolAdmin(String rolAdmin) {
		this.rolAdmin = rolAdmin;
	}

	public Opciones getOpciones() {
		return opciones;
	}

	public void setOpciones(Opciones opciones) {
		this.opciones = opciones;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getClaseOcultaProceso() {
		return claseOcultaProceso;
	}

	public void setClaseOcultaProceso(String claseOcultaProceso) {
		this.claseOcultaProceso = claseOcultaProceso;
	}

	public boolean isDebugging() {
		return isDebug;
	}

	public void setDebugging(boolean isDebugging) {
		this.isDebug = isDebugging;
	}
	
	public String getRol() {
		return rol;
	}
	
	public void setRol(String rol) {
		this.rol = rol;
	}
}