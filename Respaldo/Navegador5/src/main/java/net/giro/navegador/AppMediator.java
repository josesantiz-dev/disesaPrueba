/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package net.giro.navegador;

import java.io.Serializable;

import org.apache.log4j.*;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

public class AppMediator implements HttpSessionListener, ServletContextListener, Serializable {
	private static final long serialVersionUID = 1L;
	public static final String SESSION_DESTROYED = "sessionDestroyed";
    private static Logger log = Logger.getLogger(AppMediator.class);
    private Navegador navegador;
    private LoginManager loginManager;
	private boolean started = false;
	private FacesContext fc;
	private Application application;
	private ValueExpression dato;

    
    public AppMediator() {
    	//System.out.println("APP MEDIATOR");
    }

    
    /**
     * Starts  with a UserBean configured and verified by the Login
     * manager.  All other manager classes are initialized by this method.
     *
     * @return navigation command
     */
    
    public String start(ActionEvent evnt) {
    	started = true;
    	if(navegador != null && navegador.getMediator() == null){
    		loginManager.setMediator(this);
    		loginManager.inicializa();
    		navegador.setMediator(this);
    		navegador.inicializa();
    	}
    	
        return "OK";
    }

    public String shutdown() {
        if (log.isDebugEnabled()) {
            log.debug("Started Shutting down session ");
        }
        
        if (loginManager != null) {
        	//loginManager.getLoginBean().setUserName("");
        	//loginManager.getLoginBean().setUserPassword("");
            //loginManager.dispose();
            loginManager.dispose();
        }

        if (log.isDebugEnabled()) {
            log.debug("Finished Shutting down Webmail session ");
        }

        return LoginManager.LOGOUT_SUCCESS;
    }

    public void setLoginManager(LoginManager loginManager) {
		this.loginManager = loginManager;
	}

	public LoginManager getLoginManager() {
		if (!started) 
			 start(null);
		return loginManager;
	}
	
    public Navegador getNavegador() {
    	if (!started) 
			 start(null);
		return navegador;
	}

	public void setNavegador(Navegador navegador) {
		this.navegador = navegador;
	}

    /**
     * Called by the servelet container when a session is destroyed.  This
     * method will call the logOut method which does any needed clean-up.
     *
     * @param httpSessionEvent season event contains seesion id which can be used to
     *                         clean up the respective SeasonBean.
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        if (FacesContext.getCurrentInstance() != null){
        	fc = FacesContext.getCurrentInstance();
            application = FacesContext.getCurrentInstance().getApplication();
            if (log.isDebugEnabled()){
                log.debug("Session Destroyed, cleaning up season data " +
                          httpSessionEvent.getSession().getId());
            }

            /*dato = application.getExpressionFactory().createValueExpression(fc.getELContext(), "#{mediator}", AppMediator.class);
            Mediator = (AppMediator)dato.getValue(fc.getELContext());
            
            if (Mediator != null){
                Mediator.shutdown();
            }*/
            this.shutdown();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Session cound not be Destroyed, cleaning up season data failed ");
            }
        }
        
        // mark the session as being destroyed.
        httpSessionEvent.getSession().setAttribute(SESSION_DESTROYED, Boolean.TRUE);

        if (log.isDebugEnabled()){
            log.debug("sessionDestroyed id:= " + httpSessionEvent.getSession().getId());
        }
    }

    /**
     * Called by the servlet container when a session is to be created. Currently
     * doing nothing with it.
     *
     * @param httpSessionEvent session event associated with the creation of a new session.
     */
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {}

    /**
     * Notification that the servlet context is about to be shut down. As a result
     * we want to kill the static javaMailTickleTimer.
     *
     * @param contextEvent servlet context event from servlet container.
     */
    public void contextDestroyed(ServletContextEvent contextEvent) {
        if (log.isDebugEnabled()){
            log.debug("Context is about to be Destroyed");
        }

        // we need to grab the application context which will give us
        // our bens as this method is called from another thread.
        /*if (FacesContext.getCurrentInstance() != null){
        	fc = FacesContext.getCurrentInstance();
            application = FacesContext.getCurrentInstance().getApplication();

            dato = application.getExpressionFactory().createValueExpression(fc.getELContext(), "#{mediator}", AppMediator.class);
            Mediator = (AppMediator)dato.getValue(fc.getELContext());
            
            // first listern will win on destroying timer.
            if (Mediator != null){
                if (log.isDebugEnabled()){
                    log.debug("Context is about to be Destroyed, canceling application timer.");
                }
              
            }
        }else{
            if (log.isDebugEnabled()){
                log.debug("Context is about to be Destroyed, cancelling timer");
            }
            
        }*/
    }

    /**
     * Notification that the web application is ready to process requests.
     * @param contextEvent servlet context event from servlet container.
     */
    public void contextInitialized(ServletContextEvent contextEvent) {}
}
