package net.giro.mobile;

import java.util.Hashtable;
import java.util.List;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.FactoryFinder;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.giro.ne.beans.Empresa;
import net.giro.plataforma.InfoSesion;
import net.giro.plataforma.seguridad.Login;
import net.giro.plataforma.seguridad.beans.UsuarioResponsabilidad;
import net.giro.plataforma.seguridad.logica.AutentificacionRem;

public class DatosUsuario {	
	private static Logger log = Logger.getLogger(DatosUsuario.class);
	private InfoSesion infoSesion;
	private FacesContext facesContext;
	private HttpServletRequest request;
	private InitialContext ctx;
	private AutentificacionRem autentificacion;
	private String ipAddress;
	private Long aplicacionId; 
    private Login login;
	private int tipoRespuesta;
	private long usuarioId;
	private String usuario;
	private String perfilAutorizar;
	// Responsabilidades
	private List<UsuarioResponsabilidad> usuarioResponsabilidades = null;
    private UsuarioResponsabilidad usuarioResponsabilidad;
	// Empresas
	private List<Empresa> usuarioEmpresas = null;
    private Empresa usuarioEmpresa;
    
    public static final String LOGIN_SUCCESS = "CONTINUA";
    public static final String LOGIN_FAILURE = "INCORRECTO";
	public static final String LOGOUT_SUCCESS = "LOGOUT EXITOSO";
	
	public DatosUsuario() {
		this.usuario = "";
		this.perfilAutorizar = "";
	}
	
	public DatosUsuario(HttpServletRequest request) {
		this();
		this.request = request;
	}

	public void inicializar() {
		Hashtable<String, Object> p = new Hashtable<String, Object>();
        p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

		this.facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
			log.warn("---> No pude obtener el FacesContext");
			return;
        }
		
        this.usuario = facesContext.getExternalContext().getRemoteUser();
        this.request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    	this.ipAddress = this.request.getHeader( "X-FORWARDED-FOR" );
    	if (this.ipAddress == null) 
    		this.ipAddress = this.request.getRemoteAddr();
    	
    	//no tiene un id de aplicacion registrada --> VERIFICAR
    	this.aplicacionId = 12L;//= Long.valueOf(facesContext.getExternalContext().getInitParameter("aplicacionId"));
    	
    	try {
            this.ctx = new InitialContext(p);
            this.autentificacion = (AutentificacionRem) ctx.lookup("ejb:/Logica_Publico//AutentificacionFac!net.giro.plataforma.seguridad.logica.AutentificacionRem?stateful");
			
			String validated = verifyUserBean();
	        if (LOGIN_SUCCESS.equals(validated)) {
	        	// do nothing
	        }
	        
	        this.infoSesion = this.autentificacion.getInfoSesion();
			
			this.usuarioResponsabilidades = this.autentificacion.getUsuarioResponsabilidades();
			if (! this.usuarioResponsabilidades.isEmpty()) {
				this.usuarioResponsabilidad = this.usuarioResponsabilidades.get(0);
				this.usuarioId = this.usuarioResponsabilidad.getUsuario().getId();
				this.perfilAutorizar = this.autentificacion.getPerfil("AUTORIZAR");
			}
			
			this.usuarioEmpresas = this.autentificacion.getUsuarioEmpresas();
			if (! this.usuarioEmpresas.isEmpty()) {
				this.usuarioEmpresa = this.usuarioEmpresas.get(0);
			}

	        
	    	/*this.mensaje = "";
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
			}*/
    	} catch (Exception e) {
    		log.error("Error obteniendo datos de sesion del usuario", e);
    	}
	}
	
	public void inicializar(HttpServletRequest request, HttpServletResponse response) {
		this.facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
        	FacesContextFactory contextFactory  = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
            LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
 
            this.facesContext = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response, lifecycle);
 
            // Set using our inner class
            InnerFacesContext.setFacesContextAsCurrentInstance(this.facesContext);
 
            // set a new viewRoot, otherwise context.getViewRoot returns null
            UIViewRoot view = this.facesContext.getApplication().getViewHandler().createView(this.facesContext, "");
            this.facesContext.setViewRoot(view); 
        }
        
        inicializar();
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
        		 //infoSesion = autentificacion.getInfoSesion();
        		 //sucursalesVisibles = autentificacion.getSucursalesVisibles(usuario.getUsuarioId());
        		 //sucursalUsuario = autentificacion.getSucursalUsuario(usuario.getUsuarioId());
	             return LOGIN_SUCCESS;
        	}
        } catch (Exception e) {
            log.error("Failed execute user lookup - SQL error", e);         
        }
        
		return LOGIN_FAILURE;
    }

	public String cerrarSession() { 
		FacesContext facesContext = null;
		ExternalContext ec = null;
		String res = "CerroSession";
		
		try {
			facesContext =  FacesContext.getCurrentInstance();
			if (facesContext != null) {
				ec = facesContext.getExternalContext();
				ec.redirect("../../cas/logout");
			}
		} catch (Exception re) {
			log.error("Error al cerrarSession", re);
			res = "ERROR";
		}
		
		return res;
	}

	// ----------------------------------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------------------------------
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}
    
	public boolean puedeAutorizar(){
		return (this.perfilAutorizar != null && ! "".equals(this.perfilAutorizar) && perfilAutorizar.equals("S"));
	}
	
	public String getEmpresa() {
		if (usuarioEmpresa != null && usuarioEmpresa.getId() != null && usuarioEmpresa.getId() > 0L)
			return usuarioEmpresa.getEmpresa();
		return "";
	}
	
	public void setEmpresa(String value) {}
	
	private abstract static class InnerFacesContext extends FacesContext {
        protected static void setFacesContextAsCurrentInstance(FacesContext facesContext) {
            FacesContext.setCurrentInstance(facesContext);
        }
    }

	public InfoSesion getInfoSesion() {
		return infoSesion;
	}

	public void setInfoSesion(InfoSesion infoSesion) {
		this.infoSesion = infoSesion;
	} 
}
