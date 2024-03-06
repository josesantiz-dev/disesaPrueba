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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import net.giro.navegador.AppMediator;
import net.giro.navegador.comun.Permiso;
import net.giro.ne.beans.Empresa;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.seguridad.Login;
import net.giro.plataforma.seguridad.beans.PermisosUsuario;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;
import net.giro.plataforma.seguridad.logica.AutentificacionRem;
import net.giro.plataforma.seguridad.logica.PermisosRem;

import org.apache.log4j.*;

/**
 * Permite a la vista interactuar con el bean que procesa el login, obtiene los
 * resultados obtenidos en el proceso y los expone para que puedan obtenerse desde
 * la vista, ademas, recolecta los niveles de informacion por parte del usuario conforme
 * vaya accediendo �ste a la aplicacion.
 */
public class LoginManager implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LoginManager.class);
    private AppMediator mediator;
    private AutentificacionRem autentificacion;
	private InitialContext ctx;
    private InfoSesion infoSesion;
    private Login login;
	private String usuario;
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
	private String ipAddress;
	private Long aplicacionId; 
	private boolean debugging;
    // RESPONSABILIDADES
    private boolean tieneResponsabilidades;
    private List<UsuarioResponsabilidad> usuarioResponsabilidades = null;
	private UsuarioResponsabilidad usuarioResponsabilidad;
	// EMPRESAS
    private boolean tieneEmpresas;
    private List<Empresa> usuarioEmpresas = null;
	private Empresa usuarioEmpresa;
    private boolean multiEmpresas;
    private double tipoCambioActual;
    private double tipoCambioPrevio;
    public static final String LOGIN_SUCCESS = "CONTINUA";
    public static final String LOGIN_FAILURE = "INCORRECTO";
	public static final String LOGOUT_SUCCESS = "LOGOUT EXITOSO";
    // login type, either anonymous or registered.
    protected String loginType;
    // PERMISOS
    private PermisosRem ifzPermisos;
    private HashMap<Long, HashMap<Long, Permiso>> permisos; // { idFuncion : BINARY_VALUE }
    
    public LoginManager() {
    	FacesContext facesContext = null;
    	HttpServletRequest request = null;
        String valParameter = "";

        try {
	        facesContext = FacesContext.getCurrentInstance();
			request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
	        this.ipAddress = request.getHeader( "X-FORWARDED-FOR" );
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
		Hashtable<String, Object> environment = null;
		FacesContext facesContext = null;
		String validated = null;
		Empresa empresa = null;
		
		try {
    		facesContext = FacesContext.getCurrentInstance();
    		this.usuario = facesContext.getExternalContext().getRemoteUser();
    		environment = new Hashtable<String, Object>();
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            this.ctx = new InitialContext(environment);  
            this.autentificacion = (AutentificacionRem) this.ctx.lookup("ejb:/Logica_Publico//AutentificacionFac!net.giro.plataforma.seguridad.logica.AutentificacionRem?stateful");
            this.ifzPermisos = (PermisosRem) this.ctx.lookup("ejb:/Logica_Publico//PermisosFac!net.giro.plataforma.seguridad.logica.PermisosRem");
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

    	// SELECCION DE RESPONSABILIDAD
    	// ------------------------------------------------------------------------------------
    	try {
    		this.usuarioResponsabilidades = this.autentificacion.getUsuarioResponsabilidades();
    		this.tieneResponsabilidades = this.usuarioResponsabilidades.size() > 0;
			setUsuarioResponsabilidad(this.usuarioResponsabilidades.get(0));
			this.autentificacion.setResponsabilidad(getUsuarioResponsabilidad());
			this.infoSesion.setResponsabilidad(getUsuarioResponsabilidad());
		} catch (Exception e) {
			this.tieneResponsabilidades = false;
			this.loginValidateOutcome = LOGIN_FAILURE;
        	return;
		}

    	// SELECCION DE EMPRESA
    	// ------------------------------------------------------------------------------------
    	try {
    		this.usuarioEmpresas = this.autentificacion.getUsuarioEmpresas();
    		this.tieneEmpresas = this.usuarioEmpresas.size() > 0;
    		this.multiEmpresas = this.usuarioEmpresas.size() > 1;
			empresa = this.usuarioEmpresas.get(0);
    		if (this.infoSesion.getEmpresa() == null || this.infoSesion.getEmpresa().getId() == null) 
        		this.infoSesion.setEmpresa(empresa);
			empresa = this.infoSesion.getEmpresa();
    		this.autentificacion.setEmpresa(empresa);
    		setUsuarioEmpresa(empresa);
		} catch (Exception e) {
			this.tieneEmpresas = false;
			this.multiEmpresas = false;
			this.loginValidateOutcome = LOGIN_FAILURE;
			return;
		}
    	
    	// SELECCION DE TIPOS DE CAMBIO
    	// ------------------------------------------------------------------------------------
    	consultaTipoDeCambio();
    	
    	// CONSULTA DE PERMISOS
    	// ------------------------------------------------------------------------------------
    	consultaPermisos();
    	/*try {
    		factor = 0;
    		calendar = Calendar.getInstance();
    		dia = calendar.get(Calendar.DAY_OF_WEEK);
    		if (dia == Calendar.SUNDAY) 
    			factor = 2;
    		if (dia == Calendar.SATURDAY)
    			factor = 1;
    		dia = factor * -1;
			calendar.add(Calendar.DAY_OF_YEAR, dia);
    		
    		// Tipo de Cambio del dia
			fecha = calendar.getTime();
    		this.tipoCambioActual = this.autentificacion.getTipoCambio(fecha);
    		
    		// determinamos dia habil
    		factor = 1;
    		dia = calendar.get(Calendar.DAY_OF_WEEK);
    		if (dia == Calendar.MONDAY)
    			factor = 3;
    		dia = factor * -1;
			calendar.add(Calendar.DAY_OF_YEAR, dia);
    		
    		// Tipo de Cambio del dia habil anterior
			fecha = calendar.getTime();
    		this.tipoCambioPrevio = this.autentificacion.getTipoCambio(fecha);
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Tipo de Cambio", e);
			this.tipoCambioActual = 0;
			this.tipoCambioPrevio = 0;
		} finally {
			if (this.tipoCambioActual <= 0)
				this.tipoCambioActual = 1.0;
			if (this.tipoCambioPrevio <= 0)
				this.tipoCambioPrevio = 1.0;
		}*/
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

    protected void consultaTipoDeCambio() {
		Calendar calendar = null;
		Date fecha = null;
		int factor = 0;
		int dia = 0;
		
    	try {
    		factor = 0;
    		calendar = Calendar.getInstance();
    		dia = calendar.get(Calendar.DAY_OF_WEEK);
    		if (dia == Calendar.SUNDAY) 
    			factor = 2;
    		if (dia == Calendar.SATURDAY)
    			factor = 1;
    		dia = factor * -1;
			calendar.add(Calendar.DAY_OF_YEAR, dia);
    		
    		// Tipo de Cambio del dia
			fecha = calendar.getTime();
    		this.tipoCambioActual = this.autentificacion.getTipoCambio(fecha);
    		
    		// determinamos dia habil
    		dia = calendar.get(Calendar.DAY_OF_WEEK);
    		factor = (dia == Calendar.MONDAY) ? 3 : 1;
    		dia = factor * -1;
			calendar.add(Calendar.DAY_OF_YEAR, dia);
    		
    		// Tipo de Cambio del dia habil anterior
			fecha = calendar.getTime();
    		this.tipoCambioPrevio = this.autentificacion.getTipoCambio(fecha);
		} catch (Exception e) {
			log.error("Ocurrio un problema al recuperar el Tipo de Cambio", e);
			this.tipoCambioActual = 0;
			this.tipoCambioPrevio = 0;
		} finally {
			if (this.tipoCambioActual <= 0)
				this.tipoCambioActual = 1.0;
			if (this.tipoCambioPrevio <= 0)
				this.tipoCambioPrevio = 1.0;
		}
    }
    
    protected void consultaPermisos() {
		List<PermisosUsuario> listPermisos = null;
		HashMap<Long, Permiso> itemPermiso = null;
		Permiso permiso = null;
		long idApp = 0L;
		
    	try {
    		this.permisos = new HashMap<Long, HashMap<Long, Permiso>>();
    		this.ifzPermisos.setInfoSesion(this.infoSesion);
    		listPermisos = this.ifzPermisos.findByUsuario(this.infoSesion.getAcceso().getUsuario().getId(), "");
    		listPermisos = (listPermisos != null) ? listPermisos : new ArrayList<PermisosUsuario>();
    		if (listPermisos.isEmpty()) {
    			log.warn("Usuario sin permisos asignados");
    			return;
    		}
    		
    		itemPermiso = new HashMap<Long, Permiso>();
    		for (PermisosUsuario item : listPermisos) {
    			if (idApp > 0L && idApp != item.getIdAplicacion().getId()) {
    				this.permisos.put(idApp, itemPermiso);
    	    		itemPermiso = new HashMap<Long, Permiso>();
    			}

				idApp = item.getIdAplicacion().getId();
    			permiso = new Permiso(item.getPermiso());
    			itemPermiso.put(item.getIdFuncion().getId(), permiso);
    		}

			this.permisos.put(idApp, itemPermiso);
    	} catch (Exception e) {
			log.error("Ocurrio un problema al consultar los Permisos", e);
		}
    }
    
    public void dispose() {
    	this.dispose(false);
    }

    public void dispose(boolean keepCompany) {
        if (log.isDebugEnabled())
            log.debug(" Disposing LoginManager");
        this.autentificacion.desconectar(this.infoSesion, keepCompany);
    }
    
	public void seleccionaResponsabilidad() { 
		this.autentificacion.setResponsabilidad(getUsuarioResponsabilidad());
		this.infoSesion.setResponsabilidad(getUsuarioResponsabilidad());
		this.mediator.getNavegador().inicializa();
		this.mediator.getNavegador().refreshLocation();
	}

	public void seleccionaEmpresa() {
		if (this.debugging)
			log.debug("Seleccionando Empresa ... ");
		this.infoSesion.setEmpresa(getUsuarioEmpresa());
		this.autentificacion.propagaEmpresa(this.infoSesion, getUsuarioEmpresa());
		this.mediator.getNavegador().inicializa();
		this.mediator.getNavegador().refreshLocation();
		if (this.debugging)
			log.debug("Empresa seleccionada: " + getUsuarioEmpresa().getId() + " - " + getUsuarioEmpresa().getEmpresa());
	}
	
    /*
     * invocado cuando el usuario quiere cambiar su contrase�a
     */
    public void resetContrasena(){
    	this.tituloCambioContrasena = "";
    	this.actualPassword = "";
    	this.nuevoPassword = "";
    	this.nuevoPassword2 = "";
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

	public boolean validar() {
		InfoSesion aux = null;
		
		if (this.infoSesion != null && this.infoSesion.getEmpresa() != null) {
			aux = this.autentificacion.getInfoSesion();
			if (aux.getEmpresa().getId().longValue() != this.infoSesion.getEmpresa().getId().longValue()) {
				this.usuarioEmpresa = aux.getEmpresa();
				this.infoSesion = aux;
				return true;
			}
		}
		
		return false;
	}

	public List<String> getUsuarios() {
		return (this.autentificacion.usuarios() != null) ? this.autentificacion.usuarios() : new ArrayList<String>();
	}

	public HashMap<String, String> getSessiones() {
		return (this.autentificacion.sessiones() != null) ? this.autentificacion.sessiones() : new HashMap<String, String>();
	}
	public HashMap<Long, Permiso> getPermisos(long idAplicacion) {
		return (idAplicacion > 0L && this.permisos.containsKey(idAplicacion)) ? this.permisos.get(idAplicacion) : null;
	}
	
	public Permiso getPermisos(long idAplicacion, long idFuncion) {
		return (idAplicacion > 0L && idFuncion > 0L && this.permisos.containsKey(idAplicacion) && this.permisos.get(idAplicacion).containsKey(idFuncion)) ? this.permisos.get(idAplicacion).get(idFuncion) : null;
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

	public boolean isDebugging() {
		return debugging;
	}

	public void setDebugging(boolean debugging) {
		this.debugging = debugging;
	}

	public double getTipoCambioActual() {
		return tipoCambioActual;
	}

	public void setTipoCambioActual(double tipoCambioActual) {
		this.tipoCambioActual = tipoCambioActual;
	}

	public double getTipoCambioPrevio() {
		return tipoCambioPrevio;
	}

	public void setTipoCambioPrevio(double tipoCambioPrevio) {
		this.tipoCambioPrevio = tipoCambioPrevio;
	}
	
	public long getIdAplicacion() {
		this.aplicacionId = (this.aplicacionId != null) ? this.aplicacionId : 0L;
		return this.aplicacionId;
	}
	
	public void setIdAplicacion(long value) {}
	
	/*public long getIdFuncion() {
		this.funcionId = (this.funcionId != null) ? this.funcionId : 0L;
		return this.funcionId;
	}
	
	public void setIdFuncion(long funcionId) {
		this.funcionId = funcionId;
	}*/
}
