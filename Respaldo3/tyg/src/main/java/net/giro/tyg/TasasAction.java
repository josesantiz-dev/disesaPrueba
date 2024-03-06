package net.giro.tyg; 




import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;

//import cde.plataforma.LoginManager;
//import cde.plataforma.seguridad.exceptions.ExcepConstraint;
//

import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Tasas;
import net.giro.tyg.logica.TygRem;

import org.apache.log4j.Logger;


public class TasasAction  {
	Logger log = Logger.getLogger(TasasAction.class);

	private TygRem	 ifzTyg;
	
	private List<Tasas> listTasas;

	private Tasas pojoTasas;

	private String problemInesp;
	private String resOperacion;
	private String campoBusqueda;
	private String busquedaVacia;
	private String registroExistente;

	private Long usuarioId;

	private int numPagina;

	private InitialContext ctx;
	private Object lookup;
	
	LoginManager loginManager;

	private String valTipoBusqueda;

	public TasasAction() throws Exception {
		try{
			setCtx(new InitialContext());
			numPagina = 1;

			pojoTasas = new Tasas();

			listTasas = new ArrayList<Tasas>();

			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();
			
			ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
			PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
			problemInesp = propPlataforma.getString("mensaje.error.inesperado");	
			
			dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
			propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
			busquedaVacia= propPlataforma.getString("mensaje.info.busquedaVacia");
			
			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			loginManager = (LoginManager)ve.getValue(fc.getELContext());
			InitialContext ctx = loginManager.getCtx();
			usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
			
			ifzTyg =  (TygRem)ctx.lookup("ejb:/Logica_TYG//TygFacade!net.giro.tyg.logica.TygRem");
			ifzTyg.setInfoSesion(loginManager.getInfoSesion());
		} catch (Exception e){
			log.error("Error al crear al inicializar", e);
		}
	}

	// Methods

	public void nuevo() {
		this.pojoTasas = new Tasas();
		this.resOperacion = "";
	}

	public long guardar() {
		try {
			this.resOperacion = "";
			long id = pojoTasas.getId();
			Respuesta respuesta = this.ifzTyg.salvar(this.pojoTasas);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.pojoTasas = (Tasas) respuesta.getBody().getValor("pojoTasas");
				if(id <= 0)
					this.listTasas.add(0,pojoTasas);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}	
		return 1;
	}


	public void eliminar() {
		try {
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.eliminarTasas(this.pojoTasas);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listTasas.remove(this.pojoTasas);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo elimina Tasa.", e);
		}
	}

	@SuppressWarnings("unchecked")
	public void buscar() {
		try {			
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.buscarTasas(this.campoBusqueda, this.valTipoBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.listTasas = (List<Tasas>)respuesta.getBody().getValor("listTasas");
				if (this.listTasas != null && this.listTasas.isEmpty()) {
					this.resOperacion = busquedaVacia;
		
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public void editar() {
		this.resOperacion = "";
	}

	public TygRem getIfzTyg() {
		return ifzTyg;
	}

	public void setIfzTyg(TygRem ifzTyg) {
		this.ifzTyg = ifzTyg;
	}

	public List<Tasas> getListTasas() {
		return listTasas;
	}

	public void setListTasas(List<Tasas> listTasas) {
		this.listTasas = listTasas;
	}

	public Tasas getPojoTasas() {
		return pojoTasas;
	}

	public void setPojoTasas(Tasas pojoTasas) {
		this.pojoTasas = pojoTasas;
	}

	public String getProblemInesp() {
		return problemInesp;
	}

	public void setProblemInesp(String problemInesp) {
		this.problemInesp = problemInesp;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}

	public String getBusquedaVacia() {
		return busquedaVacia;
	}

	public void setBusquedaVacia(String busquedaVacia) {
		this.busquedaVacia = busquedaVacia;
	}

	public String getRegistroExistente() {
		return registroExistente;
	}

	public void setRegistroExistente(String registroExistente) {
		this.registroExistente = registroExistente;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public InitialContext getCtx() {
		return ctx;
	}

	public void setCtx(InitialContext ctx) {
		this.ctx = ctx;
	}

	public Object getLookup() {
		return lookup;
	}

	public void setLookup(Object lookup) {
		this.lookup = lookup;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}

	// Getters and Setters

	

}
