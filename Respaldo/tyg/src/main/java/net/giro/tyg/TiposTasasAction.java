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
import net.giro.tyg.admon.TiposTasas;
import net.giro.tyg.logica.TygRem;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;


@KeepAlive
public class TiposTasasAction  {



	Logger log = Logger.getLogger(TiposTasasAction.class);


	LoginManager loginManager;
	
	// declaraciones TiposTasas
	private List<SelectItem> listTiposTasas;
	private List<TiposTasas> listTmpTiposTasas;
	private List<SelectItem> listTasas;
	private List<Tasas> listTmpTasas;
	
	

	// declaraciones Tasas

	
	private Tasas pojoTasas;
	private TiposTasas pojoTiposTasas;
	

	private String problemInesp;
	private String resOperacion;
	private String campoBusqueda;
	private String busquedaVacia;
	private String registroExistente;
	
	private TygRem	 ifzTyg;
	
	private Long usuarioId;
	private int numPagina;


	private String valTipoBusqueda;

	private List<TiposTasas> listValores;

	public TiposTasasAction() throws Exception {

		@SuppressWarnings("unused")
		Logger log = Logger.getLogger(TiposTasasAction.class);
		
		numPagina = 1;
		
		valTipoBusqueda="clave"; 

		pojoTiposTasas = new TiposTasas();
		
		listTmpTasas=new ArrayList<Tasas>();
		listTmpTiposTasas = new ArrayList<TiposTasas>();
		
		pojoTasas = new Tasas();
		listTmpTasas = new ArrayList<Tasas>();
		listTasas = new ArrayList<SelectItem>();
		listValores=new ArrayList<TiposTasas>();
		
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
		
		cargarSelectTasas();

	}

	// Methods

	public void nuevo() {
		this.pojoTiposTasas = new TiposTasas();
		this.resOperacion = "";
		cargarSelectTasas();
	}

	public long guardar() {
			try {
				this.resOperacion = "";
				long id = pojoTiposTasas.getId();
				Respuesta respuesta = ifzTyg.salvar(pojoTiposTasas);		
				if(respuesta.getErrores().getCodigoError() == 0L){
					pojoTiposTasas = (TiposTasas)respuesta.getBody().getValor("pojoTiposTasas");
					if(id <= 0)
						this.listValores.add(0,pojoTiposTasas);
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
			Respuesta respuesta = this.ifzTyg.eliminarTipoTasas(this.pojoTiposTasas);
			if(respuesta.getErrores().getCodigoError() == 0L)
				this.listValores.remove(this.pojoTiposTasas);
			else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo elimina Tipo tasa.", e);
		}
	}

	@SuppressWarnings("unchecked")
	public void buscar() {
		
		try {			
			this.resOperacion = "";
			Respuesta respuesta = this.ifzTyg.buscarTipoTasas(this.valTipoBusqueda, this.campoBusqueda);
			if(respuesta.getErrores().getCodigoError() == 0L){
				this.listValores = (List<TiposTasas>)respuesta.getBody().getValor("listTiposTasas");
				if (this.listValores != null && this.listValores.isEmpty()) 
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

//	public void cargaTasas() {
//
//		try {
//			this.listTmpTasas = ifzTyg.autoCargarTasas();
//			for (Tasas e : this.listTmpTasas) {
//				if (this.pojoTiposTasas.getId() != null) {
//					this.pojoTasas = e;
//				}
//				this.listTasas
//						.add(new SelectItem(e.getId(), e.getDescripcion()));
//			}
//		} catch (Exception e) {
//			log.error("Error en metodo carga tasas.", e);
//		}
//
//	}
	
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



	public TiposTasas getPojoTiposTasas() {
		return pojoTiposTasas;
	}

	public void setPojoTiposTasas(TiposTasas pojoTiposTasas) {
		this.pojoTiposTasas = pojoTiposTasas;
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



	public TygRem getIfzTyg() {
		return ifzTyg;
	}

	public void setIfzTyg(TygRem ifzTyg) {
		this.ifzTyg = ifzTyg;
	}

	public String getRegistroExistente() {
		return registroExistente;
	}

	public void setRegistroExistente(String registroExistente) {
		this.registroExistente = registroExistente;
	}

	public List<TiposTasas> getListValores() {
		return listValores;
	}

	public void setListValores(List<TiposTasas> listValores) {
		this.listValores = listValores;
	}
	private Tasas getTasasById(Long id, List<Tasas> lista){
		for(Tasas cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}
	
	public void setSuggTasas(Long tasas) {
		this.pojoTiposTasas.setTasas(getTasasById(tasas, this.listTmpTasas));
	}

	public Long getSuggTasas() {
		return pojoTiposTasas.getTasas() !=null? pojoTiposTasas.getTasas().getId(): -1L;
	}

	public List<TiposTasas> getListTmpTiposTasas() {
		return listTmpTiposTasas;
	}

	public void setListTmpTiposTasas(List<TiposTasas> listTmpTiposTasas) {
		this.listTmpTiposTasas = listTmpTiposTasas;
	}

	public List<SelectItem> getListTiposTasas() {
		return listTiposTasas;
	}

	public void setListTiposTasas(List<SelectItem> listTiposTasas) {
		this.listTiposTasas = listTiposTasas;
	}

	public String getValTipoBusqueda() {
		return valTipoBusqueda;
	}

	public void setValTipoBusqueda(String valTipoBusqueda) {
		this.valTipoBusqueda = valTipoBusqueda;
	}
}
