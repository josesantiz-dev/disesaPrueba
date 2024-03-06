/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package net.giro.navegador;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import net.giro.navegador.AppMediator;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.seguridad.Login;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;
import net.giro.plataforma.seguridad.logica.AutentificacionRem;

import org.apache.log4j.*;

/**
 * Permite a la vista interactuar con el bean que procesa el login, obtiene los
 * resultados obtenidos en el proceso y los expone para que puedan obtenerse desde
 * la vista, ademas, recolecta los niveles de informacion por parte del usuario conforme
 * vaya accediendo �ste a la aplicacion.
 *
 */

public class LoginManager  implements Serializable{
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(LoginManager.class);
    
    private AppMediator mediator;
    
    /**
     * permite a la vista conocer cual fue el token de respuesta por parte de la logica y decidir 
     * que modal o mensaje mostrar al usuario 
     */
    private int tipoRespuesta;
    
    public String loginValidateOutcome = "";
    private String mensaje;
    private String tituloCambioContrasena;
    private String nuevoPassword;
    private String nuevoPassword2;
    private String actualPassword;
    
    private boolean tieneResponsabilidades;
    List<UsuarioResponsabilidad> usuarioResponsabilidades = null;
    PropertyResourceBundle prop;
	private UsuarioResponsabilidad usuarioResponsabilidad;
	private String ipAddress;
	private Long aplicacionId; 
	private InitialContext ctx;
    private InfoSesion infoSesion;
    AutentificacionRem autentificacion;
    private Login login;
	private String usuario;

    public static final String LOGIN_SUCCESS = "CONTINUA";
    public static final String LOGIN_FAILURE = "INCORRECTO";
	public static final String LOGOUT_SUCCESS = "LOGOUT EXITOSO";

    // login type, either anonymous or registered.
    protected	String loginType;
    //private HashMap<Integer, String> paramPerfiles;

    public LoginManager() {
        FacesContext facesContext =  FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        
		ValueExpression dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
		prop = (PropertyResourceBundle)dato.getValue(facesContext.getELContext());
		
		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    	this.ipAddress = request.getHeader( "X-FORWARDED-FOR" );
    	if (ipAddress == null ) {
    	    this.ipAddress = request.getRemoteAddr();
    	}
    	
    	this.aplicacionId = Long.valueOf(facesContext.getExternalContext().getInitParameter("aplicacionId"));
    	//inicializa();
    }
    
    
	/*public void asignaNuevoValPerfil(Integer perfil, String valor){
		this.paramPerfiles.put(perfil, valor);
	}

	public void setParamPerfiles(HashMap<Integer, String> paramPerfiles) {
		this.paramPerfiles = paramPerfiles;
	}

	public HashMap<Integer, String> getParamPerfiles() {
		return paramPerfiles;
	}*/
    
    protected String verifyUserBean() {
        try {
			login = new Login();
			login.setUsuario(usuario);
			login.setAplicacionId(aplicacionId);
			login.setTerminal(ipAddress);
			login.setIpTerminal(ipAddress);
			tipoRespuesta = autentificacion.conectar(login);
			
			if (this.tipoRespuesta == 0 || this.tipoRespuesta == 3) {
				infoSesion = autentificacion.getInfoSesion();
				//sucursalesVisibles = autentificacion.getSucursalesVisibles(usuario.getUsuarioId());
				//sucursalUsuario = autentificacion.getSucursalUsuario(usuario.getUsuarioId());
				return LOGIN_SUCCESS;	
			}
        } catch (Exception e) {
            log.error("Failed execute user lookup - SQL error", e);         
        }
        
		return LOGIN_FAILURE;
    }
    
    public void dispose() {
        if (log.isDebugEnabled()) {
            log.debug(" Disposing LoginManager");
        }  
        
        autentificacion.desconectar(this.infoSesion);
    }
    
	public void inicializa() {
		FacesContext facesContext =  FacesContext.getCurrentInstance();
		this.usuario  = facesContext.getExternalContext().getRemoteUser();
		
		try {
    		Hashtable<String, Object> p = new Hashtable<String, Object>();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            
            try {
    			this.ctx = new InitialContext(p);
    			this.autentificacion = (AutentificacionRem) ctx.lookup("ejb:/Logica_Publico//AutentificacionFac!net.giro.plataforma.seguridad.logica.AutentificacionRem?stateful");
    		} catch (NamingException e1) {
    			e1.printStackTrace();
    		}
    	} catch(Exception e) {
    		log.error("Error en el metodo constructor", e);
    	}
		
    	usuarioResponsabilidades = null;
    	
    	String validated = verifyUserBean();
        if (LOGIN_SUCCESS.equals(validated)){
        	mensaje = "";
        	loginValidateOutcome = LOGIN_SUCCESS;
        	
        	try {
        		usuarioResponsabilidades = autentificacion.getUsuarioResponsabilidades();
    			tieneResponsabilidades = usuarioResponsabilidades.size() > 0;
    			setUsuarioResponsabilidad(usuarioResponsabilidades.get(0));
    			autentificacion.setResponsabilidad(getUsuarioResponsabilidad());
    			infoSesion.setResponsabilidad(getUsuarioResponsabilidad());
    			
    			/*paramPerfiles = new HashMap<Integer, String>();
    	    	paramPerfiles.put(1, null); //usuario
    	    	paramPerfiles.put(2, null); //responsabilidad
    	    	paramPerfiles.put(4, null); //puesto
    	    	paramPerfiles.put(8, null); //area
    	    	paramPerfiles.put(16, null); //menu
    	    	paramPerfiles.put(32, null); //sucursal
    	    	paramPerfiles.put(64, null); //sitio
    	    	
    	    	autentificacion.getPerfil(perfil)

    	    	asignaNuevoValPerfil(1,  String.valueOf(usuarioResponsabilidades.get(0).getUsuario().getId()));
    	    	//asignaNuevoValPerfil(2,  usuarioResponsabilidades.get(0).getResponsabilidad().getResponsabilidad());
    	    	//asignaNuevoValPerfil(16, usuarioResponsabilidades.get(0).getResponsabilidad().getMenu().getMenu()); 
    	    	asignaNuevoValPerfil(64, null); */
    		} catch(Exception e) {
    			tieneResponsabilidades = false;
    			loginValidateOutcome = LOGIN_FAILURE;
    		}
        } else {
        	loginValidateOutcome = LOGIN_FAILURE;
        }
	}
	
	public void seleccionaResponsabilidad() { 
		autentificacion.setResponsabilidad(getUsuarioResponsabilidad());
		infoSesion.setResponsabilidad(getUsuarioResponsabilidad());
		mediator.getNavegador().inicializa();
		
		//asignaNuevoValPerfil(2, String.valueOf(getUsuarioResponsabilidad().getResponsabilidad().getId()));
		//asignaNuevoValPerfil(16, getUsuarioResponsabilidad().getResponsabilidad().getMenu() != null ? String.valueOf(getUsuarioResponsabilidad().getResponsabilidad().getMenu().getId()) : null);
		//mediator.getNavegador().iniciaSession();
	}

    /*
     * invocado cuando el usuario quiere cambiar su contrase�a
     */
    public void resetContrasena(){
    	tituloCambioContrasena = "";
    	actualPassword = "";
    	nuevoPassword = "";
    	nuevoPassword2 = "";
    }
    
    /**
     * invocado en el boton al cambiar la contrase�a
     */
    public void cambiarContrasena(){
//    	try {
//    		if(!loginBean.validaContrasenaActual(MD5.getHashString(actualPassword).toUpperCase())) 
//    			mensaje = prop.getString("mensajes.error.contrasenaActual");
//    		else if(!nuevoPassword.equals(nuevoPassword2))
//    			mensaje = prop.getString("mensajes.error.contrasenaDiferente");
//    		else
//    			mensaje = loginBean.cambiarPassword(MD5.getHashString(nuevoPassword).toUpperCase()) ? "" : prop.getString("mensajes.error.contrasenaDiferente");
//    		if("".equals(mensaje))
//    			cargaDatosInicialesLogin();
//		} catch (Exception e) {
//			mensaje = prop.getString("mensajes.error.inesperado");
//			log.error("Error al cambiar la contrase�a del usuario", e);
//		}
    }

	// ------------------------------------------------------------------------------------------------------
	// PROPIEDADES
	// ------------------------------------------------------------------------------------------------------
	
    public InfoSesion getInfoSesion() {
		return infoSesion;
	}

    public String getUsuario() {
	   return usuario;
    }
    
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public boolean isLogeado() {
		return this.loginValidateOutcome.equals(LOGIN_SUCCESS);
	}
 
	public AutentificacionRem getAutentificacion() {
		return autentificacion;
	}
	
	public void setMediator(AppMediator mediator) {
		this.mediator = mediator;
	}

	public AppMediator getMediator() {
		return mediator;
	}

	public void setTieneResponsabilidades(boolean tieneResponsabilidades) {
		this.tieneResponsabilidades = tieneResponsabilidades;
	}

	public boolean isTieneResponsabilidades() {
		return tieneResponsabilidades;
	}

	public void setNuevoPassword(String nuevoPassword) {
		this.nuevoPassword = nuevoPassword;
	}

	public String getNuevoPassword() {
		return nuevoPassword;
	}

	public void setNuevoPassword2(String nuevoPassword2) {
		this.nuevoPassword2 = nuevoPassword2;
	}

	public String getNuevoPassword2() {
		return nuevoPassword2;
	}

	public void setTipoRespuesta(int tipoRespuesta) {
		this.tipoRespuesta = tipoRespuesta;
	}

	public int getTipoRespuesta() {
		return tipoRespuesta;
	}

	public void setTituloCambioContrasena(String tituloCambioContrasena) {
		this.tituloCambioContrasena = tituloCambioContrasena;
	}

	public String getTituloCambioContrasena() {
		return tituloCambioContrasena;
	}

	public void setActualPassword(String actualPassword) {
		this.actualPassword = actualPassword;
	}

	public String getActualPassword() {
		return actualPassword;
	}

	public List<UsuarioResponsabilidad> getUsuarioResponsabilidades() {
		return usuarioResponsabilidades;
	}

	public UsuarioResponsabilidad getUsuarioResponsabilidad() {
		return usuarioResponsabilidad;
	}

	public InitialContext getCtx() {
		return ctx;
	}

	public void setUsuarioResponsabilidad(UsuarioResponsabilidad usuarioResponsabilidad) {
		this.usuarioResponsabilidad = usuarioResponsabilidad;
	}
}
