package net.proc.sesion;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import net.izel.framework.util.DateUtil;
import net.izel.ws.seg.datos.Core;
import net.izel.ws.seg.datos.Pantalla;
import net.izel.ws.seg.datos.Rol;

import org.apache.log4j.Logger;

@SessionScoped
@ManagedBean(name="sesionProc")
public class SesionProc implements Serializable {
	private static Logger log = Logger.getLogger(SesionProc.class);
	private static final long serialVersionUID = 1L;
	private Core coreSeleccionado;
	private Rol rolSeleccionado;
	private Pantalla pantallaSeleccionada;
	private List<Rol> lstRoles;
	private String socket_ip;
	private String socket_puerto;
	private String rolAdmin;
	@SuppressWarnings("unused")
	private String ipWS;
	@SuppressWarnings("unused")
	private String ipVista;
	@SuppressWarnings("unused")
	private String modulo;
	private Opciones opciones;
	private Programa programa;
	private Object seleccionObj;
	private Socket socket;
	private long paginaSeleccionada;
	private long id;
	private String usuario;
	private String resOperacion;
	private String claseOcultaProceso;
	
	public SesionProc() {
		this.paginaSeleccionada = Long.valueOf(0L);
		this.lstRoles = new ArrayList<Rol>();
		this.pantallaSeleccionada = new Pantalla();
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgAdminProc}", PropertyResourceBundle.class);
		PropertyResourceBundle prop = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		
		this.ipVista = prop.getString("vista_ip");
		this.modulo = prop.getString("modulo");
		this.socket_ip = prop.getString("socket_ip");
		this.socket_puerto = prop.getString("socket_puerto");
		this.rolAdmin = prop.getString("rol_adm");
		
		HttpServletRequest req = (HttpServletRequest)fc.getExternalContext().getRequest();
		this.usuario = req.getRemoteUser();
		obtenerDatosMenu();
	}
	
	public void asignarSeleccion(String titulo, String panel, Long id) {
		this.id = id.longValue();
	}
	
	public void cerrarSesion() {
		try {
			log.info("Cerrando sesion ... ");
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().invalidateSession();
			log.info("Cerrando sesion ... OK");
		} catch (Exception e) {
			log.error("Error en cerrarSesion", e);
		}
	}
	
	public void cambiarModulo() {
		try {
			log.info("Cambiando modulo ... ");
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().invalidateSession();
			log.info("Cambiando modulo ... OK");
		} catch (Exception e) {
			log.error("Error en cambiarModulo", e);
		}
	}
	
	public void obtenerDatosMenu() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		CMensajesRoles msg = null; 
		List<CRole> roles = null;
		Gson gson = new Gson();
		String respuesta = "";
		String comando = "";
		Rol r = null;
		
		try {
			this.claseOcultaProceso = "classHiden";
			this.socket = getSocket();
			
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
	
	public void seleccionarRol() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		Mensaje msg = null;
		String respuesta = "";
		String comando = "";
		
		try {
			this.socket = getSocket();
			
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
				this.opciones = msg.opciones;
			}
		} catch (Exception e) {
			log.error("Error al seleccionar Rol", e);
		}
	}

	public Socket getSocket() throws NumberFormatException, UnknownHostException, IOException {
		try {
			if ((this.socket != null) && (!this.socket.isClosed())) {
				this.socket.close();
			}
		} catch (Exception e) { 
			log.error("Error en getSocket(). No se pudo cerrar el socket", e);
		} finally {
			this.socket = null;
			this.socket = new Socket(this.socket_ip, Integer.valueOf(this.socket_puerto).intValue());
		}
		
		return this.socket;
	}
	
	public void setSocket1(Socket socket) {
		this.socket = socket;
	}
	
	public Long getId() {
		return Long.valueOf(this.id);
	}
	
	public void setId(Long id) {
		this.id = id.longValue();
	}
	
	public Object getSeleccionObj() {
		return this.seleccionObj;
	}
	
	public void setSeleccionObj(Object seleccionObj) {
		this.seleccionObj = seleccionObj;
	}
	
	public String getResOperacion() {
		return this.resOperacion;
	}
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}
	
	public List<Rol> getLstRoles() {
		return this.lstRoles;
	}
	
	public void setLstRoles(List<Rol> lstRoles) {
		this.lstRoles = lstRoles;
	}
	
	public Core getCoreSeleccionado() {
		return this.coreSeleccionado;
	}
	
	public void setCoreSeleccionado(Core coreSeleccionado) {
		this.coreSeleccionado = coreSeleccionado;
	}
	
	public Rol getRolSeleccionado() {
		return this.rolSeleccionado;
	}
	
	public void setRolSeleccionado(Rol rolSeleccionado) {
		this.rolSeleccionado = rolSeleccionado;
	}
	
	public Pantalla getPantallaSeleccionada() {
		return this.pantallaSeleccionada;
	}
	
	public void setPantallaSeleccionada(Pantalla pantallaSeleccionada) {
		this.pantallaSeleccionada = pantallaSeleccionada;
	}
	
	public String getUsuario() {
		return this.usuario;
	}
	
	public Opciones getOpciones() {
		return this.opciones;
	}
	
	public void setOpciones(Opciones opciones) {
		this.opciones = opciones;
	}
	
	public String getFechaActual() {
		return DateUtil.fechaWebCompleta(Calendar.getInstance().getTime());
	}
	
	public Programa getPrograma() {
		return this.programa;
	}
	
	public void setPrograma(Programa programa) {
		this.paginaSeleccionada = Long.valueOf(programa.getcPrograma().getId());
		this.programa = programa;
	}
	
	public long getPaginaSeleccionada() {
		return this.paginaSeleccionada;
	}
	
	public void setPaginaSeleccionada(long paginaSeleccionada) {
		this.paginaSeleccionada = paginaSeleccionada;
	}
	
	public String getClaseOcultaProceso() {
		return this.claseOcultaProceso;
	}
	
	public void setClaseOcultaProceso(String claseOcultaProceso) {
		this.claseOcultaProceso = claseOcultaProceso;
	}
}