package net.giro.tyg;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;

import net.giro.navegador.LoginManager;
import net.giro.respuesta.Respuesta;
import net.giro.tyg.admon.Tasas;

import net.giro.tyg.admon.ValoresTasas;
import net.giro.tyg.logica.TygRem;

import org.apache.log4j.Logger;


public class ValoresTasasAction  {


	Logger log = Logger.getLogger(ValoresTasasAction.class);
	// declaraciones ValoresTasas
	
	LoginManager loginManager;
	
	private List<ValoresTasas> listTmpValoresTasas;
	@SuppressWarnings("unused")
	private List<SelectItem> listValoresTasas;
	
	private List<Tasas> listTmpTasas;
	private List<SelectItem> listTasas;
	
	private ValoresTasas pojoValoresTasas;

	// declaraciones Tasas
	
	
	private Tasas pojoTasas;
	private Tasas pojoTasasAux;
	

	private String problemInesp;
	private String resOperacion;
	private String campoBusqueda;
	private String busquedaVacia;
	private String registroDuplicado;
	private String valTipoBusqueda;
	private Long usuarioId;
	private int numPagina;
	private Integer anioAux;
	@SuppressWarnings("unused")
	private InitialContext ctx;
	private Object lookup;
	private TygRem ifzTyg;

	private List<ValoresTasas> listValores;

	private ArrayList<String> tipoBusqueda;

	public ValoresTasasAction() throws Exception {

		ctx = new InitialContext();
		numPagina = 1;

		pojoValoresTasas = new ValoresTasas();

		listTmpValoresTasas = new ArrayList<ValoresTasas>();
		
		tipoBusqueda = new ArrayList<String>();
		
		tipoBusqueda.add("anio");
		tipoBusqueda.add("tasa");
		
		pojoTasas = new Tasas();
		pojoTasasAux = new Tasas();
		listTmpTasas = new ArrayList<Tasas>();
		listTasas = new ArrayList<SelectItem>();
		listValores= new ArrayList<ValoresTasas>();

		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		InitialContext ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
		
		dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msgTyg}", PropertyResourceBundle.class);
		propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		busquedaVacia= propPlataforma.getString("mensaje.info.busquedaVacia");
		
		ifzTyg =  (TygRem)ctx.lookup("ejb:/Logica_TYG//TygFacade!net.giro.tyg.logica.TygRem");
		ifzTyg.setInfoSesion(loginManager.getInfoSesion());

		cargarSelectTasas();
	}

	// Methods

	public void nuevo() {
		this.pojoValoresTasas = new ValoresTasas();
		this.resOperacion = "";
		cargarSelectTasas();
	}

	public void guardar() {
		try {
			this.resOperacion = "";
			long id = pojoValoresTasas.getId();
			Respuesta respuesta = this.ifzTyg.salvar(pojoValoresTasas);
			if(respuesta.getErrores().getCodigoError() == 0){
				pojoValoresTasas = (ValoresTasas)respuesta.getBody().getValor("pojoValoresTasas");
				if(id <= 0)
					this.listValores.add(0,pojoValoresTasas);
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}	
	}
	
	

	public void eliminar() {
		try {
			this.resOperacion = "";
			
			Respuesta respuesta = this.ifzTyg.eliminarValoresTasas(this.pojoValoresTasas);
			if(respuesta.getErrores().getCodigoError() == 0)
				this.listValores.remove(this.pojoValoresTasas);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo elimina valor de tasa.", e);
		}
	}

	@SuppressWarnings("unchecked")
	public void buscar() {
		try {			
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.buscarValoresTasas(this.valTipoBusqueda, this.campoBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0){
				this.listValores = (List<ValoresTasas>)respuesta.getBody().getValor("listValoresTasas");
				if(listValores == null && listValores.isEmpty())
					this.resOperacion = busquedaVacia;
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	

	public void editar() {
		this.resOperacion = "";
		cargarSelectTasas();
	}

	@SuppressWarnings("unchecked")
	public void cargarSelectTasas(){
		try{
			Respuesta respuesta = ifzTyg.autoCargarTasas();
			if(respuesta.getErrores().getCodigoError() == 0){
				listTmpTasas = (List<Tasas>)respuesta.getBody().getValor("listTasas");
				listTasas.clear();
				for (Tasas i : listTmpTasas) {
					listTasas.add(new SelectItem(i.getId(), i
							.getDescripcion()));
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error en cargarSelectTasas", e);
			this.resOperacion = problemInesp;
		}
		
	}

	public void limpiacampoBusqueda() {
		this.campoBusqueda = "";
	}

	// Getters and Setters



	public List<ValoresTasas> getListValoresTasas() {
		return listTmpValoresTasas;
	}

	public ValoresTasas getPojoValoresTasas() {
		return pojoValoresTasas;
	}

	public void setPojoValoresTasas(ValoresTasas pojoValoresTasas) {
		this.pojoValoresTasas = pojoValoresTasas;
	}


	public List<Tasas> getListTmpTasas() {
		return listTmpTasas;
	}

	public void setListTmpTasas(List<Tasas> listTmpTasas) {
		this.listTmpTasas = listTmpTasas;
	}

	public Tasas getPojoTasas() {
		return pojoTasas;
	}

	public void setPojoTasas(Tasas pojoTasas) {
		this.pojoTasas = pojoTasas;
	}

	public List<SelectItem> getListTasas() {
		return listTasas;
	}

	public void setListTasas(List<SelectItem> listTasas) {
		this.listTasas = listTasas;
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



	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}


	public String getRegistroDuplicado() {
		return registroDuplicado;
	}

	public void setRegistroDuplicado(String registroDuplicado) {
		this.registroDuplicado = registroDuplicado;
	}

	public Tasas getPojoTasasAux() {
		return pojoTasasAux;
	}

	public void setPojoTasasAux(Tasas pojoTasasAux) {
		this.pojoTasasAux = pojoTasasAux;
	}

	public Integer getAnioAux() {
		return anioAux;
	}

	public void setAnioAux(Integer anioAux) {
		this.anioAux = anioAux;
	}

	public Object getLookup() {
		return lookup;
	}

	public void setLookup(Object lookup) {
		this.lookup = lookup;
	}

	public TygRem getIfzTyg() {
		return ifzTyg;
	}

	public void setIfzTyg(TygRem ifzTyg) {
		this.ifzTyg = ifzTyg;
	}
	

	private Tasas getTasasById(Long id, List<Tasas> lista){
		for(Tasas cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}
	
	public void setSuggTasas(Long tasas) {
		this.pojoValoresTasas.setTasas(getTasasById(tasas, this.listTmpTasas));
	}

	public Long getSuggTasas() {
		return pojoValoresTasas.getTasas() !=null? pojoValoresTasas.getTasas().getId(): -1L;
	}

	public List<ValoresTasas> getListTmpValoresTasas() {
		return listTmpValoresTasas;
	}

	public void setListTmpValoresTasas(List<ValoresTasas> listTmpValoresTasas) {
		this.listTmpValoresTasas = listTmpValoresTasas;
	}

	

	public void setListValoresTasas(List<SelectItem> listValoresTasas) {
		this.listValoresTasas = listValoresTasas;
	}

	public List<ValoresTasas> getListValores() {
		return listValores;
	}

	public void setListValores(List<ValoresTasas> listValores) {
		this.listValores = listValores;
	}
	
	public Long getIdListTasas(){
		return (this.pojoValoresTasas.getTasas() == null ?  -1L : this.pojoValoresTasas.getTasas().getId());
	}
	
	public void setIdListTasas(Long id){
		this.pojoValoresTasas.setTasas(getTasasById(id, this.listTmpTasas));
	}
}
