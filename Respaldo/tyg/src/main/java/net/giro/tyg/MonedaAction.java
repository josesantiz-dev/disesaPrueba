package net.giro.tyg;
 

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;


import javax.el.ValueExpression;
import javax.faces.application.Application;

import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.*;

import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Moneda;

import net.giro.tyg.logica.TygRem;

@KeepAlive
public class MonedaAction {
	private static Logger log = Logger.getLogger(MonedaAction.class);
	

	private TygRem ifzTyg;
	private Moneda pojoMoneda;
	
	private List<Moneda> listMoneda;
	private List<String> tipoBusqueda;
	LoginManager loginManager;

	private String campoBusqueda;	
	private String valTipoBusqueda;
    	
    @SuppressWarnings("unused")
	private Long usuarioId;	
   
    private boolean band;
    private int tipoMensaje;
	private int numPagina;

	private String resOperacion;
	private String busquedaVacia;
	private String problemInesp;
	
	public MonedaAction()throws NamingException{
	
		pojoMoneda = new Moneda();
		
		listMoneda = new ArrayList<Moneda>();
		
		this.numPagina=1;
		
		
        valTipoBusqueda = "nombre";
		tipoBusqueda = new ArrayList<String>();
		tipoBusqueda.add("nombre");
		tipoBusqueda.add("abreviacion");
		
		tipoMensaje = 0;
		band = false;
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		InitialContext ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		
		
		
		dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		busquedaVacia= propPlataforma.getString("mensaje.info.busquedaVacia");
		
		ifzTyg =  (TygRem)ctx.lookup("ejb:/Logica_TYG//TygFacade!net.giro.tyg.logica.TygRem");
		ifzTyg.setInfoSesion(loginManager.getInfoSesion());
	}
	
	
	public String nuevo (){		
		try{
			pojoMoneda = new Moneda();
		}catch(Exception e){
			log.error("error al nuevo", e);
			return "ERROR";
		}
		return "OK";
	}
	
	
	
	public void guardar (){
		try {
			this.resOperacion = "";
			long id = this.pojoMoneda.getId();
			Respuesta respuesta = ifzTyg.salvar(pojoMoneda);
			if(respuesta.getErrores().getCodigoError() == 0){
				this.pojoMoneda = (Moneda)respuesta.getBody().getValor("pojoMoneda");
				if(id <= 0)
					this.listMoneda.add(0,pojoMoneda);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}	
	}
	
	@SuppressWarnings("unchecked")
	public void buscar(){
		try {			
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.buscarMoneda(this.valTipoBusqueda, this.campoBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0){
				this.listMoneda = (List<Moneda>)respuesta.getBody().getValor("listMonedas"); 
				if (this.listMoneda != null && this.listMoneda.isEmpty()) 
					this.resOperacion = busquedaVacia;
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}


	public List<Moneda> getListMoneda() {
		return listMoneda;
	}


	public void setListMoneda(List<Moneda> listMoneda) {
		this.listMoneda = listMoneda;
	}


	public Moneda getPojoMoneda() {
		return pojoMoneda;
	}


	public void setPojoMoneda(Moneda pojoMoneda) {
		this.pojoMoneda = pojoMoneda;
	}


	public List<String> getTipoBusqueda() {
		return tipoBusqueda;
	}


	public void setTipoBusqueda(List<String> tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}


	public String getCampoBusqueda() {
		return campoBusqueda;
	}


	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}


	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}


	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}


	public boolean isBand() {
		return band;
	}


	public void setBand(boolean band) {
		this.band = band;
	}


	public int getTipoMensaje() {
		return tipoMensaje;
	}


	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}


	public int getNumPagina() {
		return numPagina;
	}


	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}


	public String getResOperacion() {
		return resOperacion;
	}


	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}	
}


	

