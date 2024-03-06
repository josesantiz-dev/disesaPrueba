package cde.publico;

import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.OficialExt;
import net.giro.plataforma.logica.EspecialistasRem;
import net.giro.plataforma.seguridad.beans.Usuario;
import net.giro.publico.respuesta.Respuesta;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;

@KeepAlive
public class EspecialistasAction {
	Logger log = Logger.getLogger(EspecialistasAction.class);
	
	private Context ctx;
	@SuppressWarnings("unused")
	private Object lookup;	
	LoginManager loginManager;
	
	private String resOperacion;
	private String problemInesp;
	
	EspecialistasRem ifzEspecialistas;
	
	List<OficialExt> listOficiales;
	List<Usuario> listUsuarios;
	
	long numPagina;
	long numPaginaUsuarios;
	
	OficialExt pojoOficial;
	
	public EspecialistasAction() throws Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		ctx = loginManager.getCtx();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		
		ifzEspecialistas = (EspecialistasRem) ctx.lookup("ejb:/Logica_Publico//EspecialistasFac!net.giro.plataforma.logica.EspecialistasRem");
		ifzEspecialistas.setInfoSesion(loginManager.getInfoSesion());
	
		numPagina = 1;
		numPaginaUsuarios= 1;
		
		pojoOficial = new OficialExt();
	}
	
	@SuppressWarnings("unchecked")
	public void buscar(){
		try{
			 resOperacion = "";
			 
			 Respuesta respuesta = ifzEspecialistas.cargarOficiales();
			 if(respuesta.getErrores().getCodigoError() == 0L){
				 listOficiales = (List<OficialExt>) respuesta.getBody().getValor("listOficiales");
			 } else
				 this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e){
			log.error("Error al buscar especialistas", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarUsuarios(){
		try{
			resOperacion = "";
			
			Respuesta respuesta = ifzEspecialistas.cargarUsuarios();
			if(respuesta.getErrores().getCodigoError() == 0L){
				listUsuarios = (List<Usuario>) respuesta.getBody().getValor("listUsuarios");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al buscar usuarios", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void editar(){
		
	}
	
	public void guardar(){
		try{
			this.resOperacion = "";
			long id = pojoOficial.getId();
			
			Respuesta respuesta = ifzEspecialistas.guardarOficial(pojoOficial);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoOficial = (OficialExt) respuesta.getBody().getValor("pojoOficial");
				
				if(id == 0L)
					listOficiales.add(pojoOficial);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al guardar especialista", e);		
		}
	}
	
	public void nuevo(){
		try{
			this.resOperacion = "";
			
			pojoOficial = new OficialExt();
		} catch (Exception e) {
			
		}
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public List<OficialExt> getListOficiales() {
		return listOficiales;
	}

	public void setListOficiales(List<OficialExt> listOficiales) {
		this.listOficiales = listOficiales;
	}

	public long getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(long numPagina) {
		this.numPagina = numPagina;
	}

	public OficialExt getPojoOficial() {
		return pojoOficial;
	}

	public void setPojoOficial(OficialExt pojoOficial) {
		this.pojoOficial = pojoOficial;
	}

	public List<Usuario> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<Usuario> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}

	public long getNumPaginaUsuarios() {
		return numPaginaUsuarios;
	}

	public void setNumPaginaUsuarios(long numPaginaUsuarios) {
		this.numPaginaUsuarios = numPaginaUsuarios;
	}
}
