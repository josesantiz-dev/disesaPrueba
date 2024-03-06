package net.giro.tyg;


import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;


import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.FormasPagos;
import net.giro.tyg.logica.TygRem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;



@KeepAlive
public class FormasDePagoAction  {


	Logger log = Logger.getLogger(FormasDePagoAction.class);


	LoginManager loginManager;
	private TygRem	 ifzTyg;
	private List<FormasPagos> listValores;	
	private FormasPagos pojoFormasDePago;

	private String problemInesp;
	private String resOperacion;
	

	@SuppressWarnings("unused")
	private Long usuarioId;
	private int numPagina;

	private InitialContext ctx;
	private Object lookup;
	
	private boolean registroSeleccionado;

	private String valTipoBusqueda;
	private String valorBusqueda;
	private String campoBusqueda;
	
	private String ctaBancariaDep;

	private String busquedaVacia;

	public FormasDePagoAction() throws Exception {
		setCtx(new InitialContext());
		numPagina = 1;



		listValores = new ArrayList<FormasPagos>();
		pojoFormasDePago = new FormasPagos();
	
		valTipoBusqueda = "formaPago";
	
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
			resOperacion= "";
			pojoFormasDePago = new FormasPagos();		
		}catch(Exception e){
			this.resOperacion = this.problemInesp;
			log.error("error al nuevo", e);
			return "ERROR";
		}
		return "OK";
	}

	public void guardar() {
		try {
			long id = pojoFormasDePago.getId();
			this.resOperacion = "";
			Respuesta respuesta = ifzTyg.salvar(pojoFormasDePago);
			
			if(respuesta.getErrores().getCodigoError() == 0){
				pojoFormasDePago = (FormasPagos) respuesta.getBody().getValor("pojoFormasPagos");
				if(id <= 0)
					this.listValores.add(0,pojoFormasDePago);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();

		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}	
	}

	@SuppressWarnings("unchecked")
	public void buscar() {
		try {			
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.buscarFormasPagos(this.valTipoBusqueda, this.valorBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0){
				this.listValores = (List<FormasPagos>)respuesta.getBody().getValor("listFormasPago");
				if (this.listValores != null && this.listValores.isEmpty()) 
					this.resOperacion = busquedaVacia;
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public String editar(){
		this.registroSeleccionado=true;
		return "OK";
	}
	
	
	public void eliminar() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.eliminarFormasPago(this.pojoFormasDePago);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listValores.remove(this.pojoFormasDePago);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		}catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar.", e);
		}
	}
	
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getResOperacion() {
		return resOperacion;
	}



	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public TygRem getIfzTyg() {
		return ifzTyg;
	}

	public void setIfzTyg(TygRem ifzTyg) {
		this.ifzTyg = ifzTyg;
	}

	public boolean isRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(boolean registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public FormasPagos getPojoFormasDePago() {
		return pojoFormasDePago;
	}

	public void setPojoFormasDePago(FormasPagos pojoFormasDePago) {
		this.pojoFormasDePago = pojoFormasDePago;
	}

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public List<FormasPagos> getListValores() {
		return listValores;
	}

	public void setListValores(List<FormasPagos> listValores) {
		this.listValores = listValores;
	}

	public String getCtaBancariaDep() {
		return ctaBancariaDep;
	}

	public void setCtaBancariaDep(String ctaBancariaDep) {
		this.ctaBancariaDep = ctaBancariaDep;
	}

	public Object getLookup() {
		return lookup;
	}

	public void setLookup(Object lookup) {
		this.lookup = lookup;
	}

	public InitialContext getCtx() {
		return ctx;
	}

	public void setCtx(InitialContext ctx) {
		this.ctx = ctx;
	}


}
