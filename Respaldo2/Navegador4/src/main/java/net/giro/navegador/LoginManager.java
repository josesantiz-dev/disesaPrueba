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
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import net.giro.navegador.AppMediator;
import net.giro.ne.beans.Empresa;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.seguridad.Login;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;
import net.giro.plataforma.seguridad.logica.AutentificacionRem;

import org.apache.log4j.*;

/**
 * Permite a la vista interactuar con el bean que procesa el login, obtiene los
 * resultados obtenidos en el proceso y los expone para que puedan obtenerse desde
 * la vista, ademas, recolecta los niveles de informacion por parte del usuario conforme
 * vaya accediendo ï¿½ste a la aplicacion.
 *
 */

public class LoginManager  implements Serializable{ 
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LoginManager.class);
    private AppMediator mediator;
    private AutentificacionRem autentificacion;
    
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
    @SuppressWarnings("unused")
	private String camposHabilitados;
    private boolean tieneResponsabilidades;
    private List<UsuarioResponsabilidad> usuarioResponsabilidades = null;
    //private PropertyResourceBundle prop;
	private UsuarioResponsabilidad usuarioResponsabilidad;
	private String ipAddress;
	private Long aplicacionId; 
	private InitialContext ctx;
    private InfoSesion infoSesion;
    private Login login;
	private String usuario;
	// EMPRESAS
    private List<Empresa> usuarioEmpresas = null;
	private Empresa usuarioEmpresa;
    private boolean tieneEmpresas;
    private boolean multiEmpresas;

    public static final String LOGIN_SUCCESS = "CONTINUA";
    public static final String LOGIN_FAILURE = "INCORRECTO";
	public static final String LOGOUT_SUCCESS = "LOGOUT EXITOSO";

    // login type, either anonymous or registered.
    protected String loginType;
    
    
    public LoginManager() {
        FacesContext facesContext = null;
        HttpServletRequest request = null;
        String valParameter = "";
        //Application app = null;
        //ValueExpression dato = null;
        
        try {
        	facesContext = FacesContext.getCurrentInstance();
        	request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        	//app = facesContext.getApplication();
        	//dato = app.getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{msg}", PropertyResourceBundle.class);
        	//this.prop = (PropertyResourceBundle) dato.getValue(facesContext.getELContext());
        	this.ipAddress = request.getHeader("X-FORWARDED-FOR");
        	if (this.ipAddress == null)
        	    this.ipAddress = request.getRemoteAddr();
        	
        	valParameter = facesContext.getExternalContext().getInitParameter("aplicacionId");
        	if (valParameter != null && ! "".equals(valParameter))
        		this.aplicacionId = Long.valueOf(valParameter);
        } catch(Exception e) {
    		log.error("Error en el metodo constructor LoginManager", e);
    	}
    }

    
	public void inicializa() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
		FacesContext facesContext = null;
		String validated = null;
		
		try {
    		facesContext = FacesContext.getCurrentInstance();
    		this.usuario = facesContext.getExternalContext().getRemoteUser();
            p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(p);  
            this.autentificacion = (AutentificacionRem) this.ctx.lookup("ejb:/Logica_Publico//AutentificacionFac!net.giro.plataforma.seguridad.logica.AutentificacionRem?stateful");
        	this.usuarioResponsabilidades = null;
        	this.usuarioEmpresas = null;
    	} catch(Exception e) {
    		log.error("Error en el metodo inicializa", e);
    	}
		
    	validated = verifyUserBean();
        if (! LOGIN_SUCCESS.equals(validated)) {
        	this.loginValidateOutcome = LOGIN_FAILURE;
        	return;
        }
        
    	this.mensaje = "";
    	this.loginValidateOutcome = LOGIN_SUCCESS;
    	
    	try {
    		this.usuarioResponsabilidades = this.autentificacion.getUsuarioResponsabilidades();
    		this.tieneResponsabilidades = this.usuarioResponsabilidades.size() > 0;
			setUsuarioResponsabilidad(this.usuarioResponsabilidades.get(0));
			this.autentificacion.setResponsabilidad(getUsuarioResponsabilidad());
			this.infoSesion.setResponsabilidad(getUsuarioResponsabilidad());
		} catch (Exception e) {
			this.tieneResponsabilidades = false;
			this.loginValidateOutcome = LOGIN_FAILURE;
		}
    	
    	try {
    		this.usuarioEmpresas = this.autentificacion.getUsuarioEmpresas();
    		this.tieneEmpresas = this.usuarioEmpresas.size() > 0;
    		this.multiEmpresas = this.usuarioEmpresas.size() > 1;
    		setUsuarioEmpresa(this.usuarioEmpresas.get(0));
    		this.autentificacion.setEmpresa(getUsuarioEmpresa());
    		this.infoSesion.setEmpresa(getUsuarioEmpresa());
		} catch (Exception e) {
			this.tieneEmpresas = false;
			this.multiEmpresas = false;
			this.loginValidateOutcome = LOGIN_FAILURE;
		}
	}

    protected String verifyUserBean() {
        try {
        	this.login = new Login();
        	this.login.setUsuario(this.usuario);
        	this.login.setAplicacionId(this.aplicacionId);
        	this.login.setTerminal(this.ipAddress);
        	this.login.setIpTerminal(this.ipAddress);
        	this.tipoRespuesta = this.autentificacion.conectar(this.login);
        	
        	if (this.tipoRespuesta == 0 || this.tipoRespuesta == 3) {
        		this.infoSesion = this.autentificacion.getInfoSesion();
        		return LOGIN_SUCCESS;	
        	}
        } catch (Exception e) {
            log.error("Failed execute user lookup - SQL error", e);         
        }
        
		return LOGIN_FAILURE;
    }

    public void dispose() {
        if (log.isDebugEnabled())
            log.debug(" Disposing LoginManager");
        
        this.autentificacion.desconectar(this.infoSesion);
    }
    
    /*
     * invocado cuando el usuario quiere cambiar su contraseña
     */
    public void resetContrasena(){
    	this.tituloCambioContrasena = "";
    	this.actualPassword = "";
    	this.nuevoPassword = "";
    	this.nuevoPassword2 = "";
    }
    
    /**
     * invocado en el boton al cambiar la contraseï¿½a
     */
    public void cambiarContrasena(){
    	/*try {
    		if (! loginBean.validaContrasenaActual(MD5.getHashString(actualPassword).toUpperCase())) 
    			mensaje = prop.getString("mensajes.error.contrasenaActual");
    		else if (! nuevoPassword.equals(nuevoPassword2))
    			mensaje = prop.getString("mensajes.error.contrasenaDiferente");
    		else
    			mensaje = loginBean.cambiarPassword(MD5.getHashString(nuevoPassword).toUpperCase()) ? "" : prop.getString("mensajes.error.contrasenaDiferente");
    		if ("".equals(mensaje))
    			cargaDatosInicialesLogin();
		} catch (Exception e) {
			mensaje = prop.getString("mensajes.error.inesperado");
			log.error("Error al cambiar la contraseña del usuario", e);
		}*/
    }
	
	public void seleccionaResponsabilidad() { 
		this.autentificacion.setResponsabilidad(getUsuarioResponsabilidad());
		this.infoSesion.setResponsabilidad(getUsuarioResponsabilidad());
		this.mediator.getNavegador().inicializa();
		this.mediator.getNavegador().refreshLocation();
	}
	
	public void seleccionaEmpresa() {
		this.autentificacion.setEmpresa(getUsuarioEmpresa());
		this.infoSesion.setEmpresa(getUsuarioEmpresa());
		this.mediator.getNavegador().inicializa();
		this.mediator.getNavegador().refreshLocation();
	}
	
	public void obtenerCamposHabilitados(Long pantallaId, Date fecha){
    	if (pantallaId == null)
    		this.camposHabilitados = "";
    	else
    		this.camposHabilitados = "";
    		//this.camposHabilitados = this.loginBean.getCamposHabilitados(Long.valueOf(this.paramPerfiles.get(1)), pantallaId, fecha);
    }
	
	public String getPerfil(String perfil) {
		return autentificacion.getPerfil(perfil);
	}

	// -------------------------------------------------------------------------
	// PROPIEDADES
	// -------------------------------------------------------------------------

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

	public InitialContext getCtx()  { 
		return ctx;
	}
	
	public List<UsuarioResponsabilidad> getUsuarioResponsabilidades() {
		return usuarioResponsabilidades;
	}

	public UsuarioResponsabilidad getUsuarioResponsabilidad() {
		return usuarioResponsabilidad;
	}

	public void setUsuarioResponsabilidad(UsuarioResponsabilidad usuarioResponsabilidad) {
		this.usuarioResponsabilidad = usuarioResponsabilidad;
	}

	public List<Empresa> getUsuarioEmpresas() {
		return usuarioEmpresas;
	}

	public void setUsuarioEmpresas(List<Empresa> usuarioEmpresas) {
		this.usuarioEmpresas = usuarioEmpresas;
	}

	public Empresa getUsuarioEmpresa() {
		return usuarioEmpresa;
	}

	public void setUsuarioEmpresa(Empresa usuarioEmpresa) {
		this.usuarioEmpresa = usuarioEmpresa;
	}

	public boolean isTieneEmpresas() {
		return tieneEmpresas;
	}

	public void setTieneEmpresas(boolean tieneEmpresas) {
		this.tieneEmpresas = tieneEmpresas;
	}

	public boolean isMultiEmpresas() {
		return multiEmpresas;
	}

	public void setMultiEmpresas(boolean multiEmpresas) {
		this.multiEmpresas = multiEmpresas;
	}
}
