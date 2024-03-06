package cde.publico.ubicacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.ubicacion.beans.Colonia;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.publico.respuesta.Respuesta;
import net.giro.ubicacion.logica.ColoniasRem;

import org.apache.log4j.Logger;

public class ColoniasAction implements Serializable {
	private static final long serialVersionUID = 985652410351472793L;
	Logger log = Logger.getLogger(ColoniasAction.class);
	
	private ColoniasRem ifzColonias;
	
	private List<Colonia> listColonias;
	private List<Localidad> listLocalidades;
	private List<Municipio> listMunicipios;
	private List<Estado> listEstados;
	
	private List<SelectItem> listTmpEstados;
	
	private Colonia pojoColonia;
	private Localidad pojoLocalidad;
	private Municipio pojoMunicipio;
	private Estado pojoEstado;
	
	private int numPagina;
	private int numPaginaLocalidades;
	private int numPaginaMunicipios;
	
	private Boolean puedeEditar;
	
	private String resOperacion;
	private String problemInesp;
	
	private String tipoBusqueda;
	private String valorBusqueda;
	
	private String valorBusquedaLocalidades;
	private String valorBusquedaMunicipios;
	
	LoginManager loginManager;
	private Context ctx;
	
	public ColoniasAction() throws Exception{
		numPagina = 1;
		
		tipoBusqueda = "";
		valorBusqueda = "";
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		ctx = loginManager.getCtx();
				
		//obtengo los posibles mensajes a mostrar al usuario
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensajes.error.inesperado");
		
		listColonias = new ArrayList<Colonia>();
		listMunicipios = new ArrayList<Municipio>();
		listLocalidades = new ArrayList<Localidad>();
		pojoColonia = new Colonia();
		pojoMunicipio = new Municipio();
		pojoLocalidad = new Localidad();
		pojoEstado = new Estado();
		
		puedeEditar = true;
		
		ifzColonias = (ColoniasRem)ctx.lookup("ejb:/Logica_Publico//ColoniasFac!net.giro.ubicacion.logica.ColoniasRem");
		ifzColonias.setInfoSesion(loginManager.getInfoSesion());
		
		cargaEstados();
	}
	
	public void buscar(){
		try {
			this.listColonias = this.ifzColonias.buscarColonias(this.tipoBusqueda, valorBusqueda);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarLocalidades(){
		try{
			Respuesta respuesta = ifzColonias.buscarLocalidades(pojoMunicipio, valorBusquedaMunicipios);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listLocalidades = (List<Localidad>) respuesta.getBody().getValor("listLocalidades");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al buscar localidades", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarMunicipios(){
		try{
			Respuesta respuesta = ifzColonias.buscarMunicipios(pojoEstado, valorBusquedaMunicipios);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listMunicipios = (List<Municipio>) respuesta.getBody().getValor("listMunicipios");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al buscar municipios", e);
		}
	}

	public void nuevo(){
		pojoColonia = new Colonia();
	}
	
	public void guardar(){
		try {
			this.resOperacion = "";
			
			long id = pojoColonia.getId();
			
			this.pojoColonia.setLocalidad(pojoLocalidad);
			
			Respuesta respuesta = ifzColonias.salvar(pojoColonia);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				pojoColonia = (Colonia) respuesta.getBody().getValor("pojoColonia");
				
				if(id == 0)
					listColonias.add(pojoColonia);
				else {
					List<Colonia> listColoniasAux = new ArrayList<Colonia>();
					
					listColoniasAux.add(pojoColonia);
					
					for(Colonia pojoColoniaAux : listColonias)
						if(pojoColoniaAux.getId() == id)
							listColoniasAux.add(pojoColoniaAux);
					
					listColonias = listColoniasAux;
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}		
	}
	
	public void editar(){
		if(pojoColonia.getLocalidad() != null){
			if(pojoColonia.getLocalidad().getId() > 0 && pojoColonia.getLocalidad().getMunicipio() != null){
				pojoLocalidad = pojoColonia.getLocalidad();
				pojoMunicipio = pojoColonia.getLocalidad().getMunicipio();
				pojoEstado = pojoMunicipio.getEstado();
			}
		}
	}
	
	public void eliminar(){
		try {
			ifzColonias.eliminar(pojoColonia);
			this.listColonias.remove(pojoColonia);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar", e);
		}
	}
	
	public void limpiaEstados(){
		pojoLocalidad = new Localidad();
		pojoMunicipio = new Municipio();
	}
	
	public void limpiaMunicipios(){
		if(listMunicipios != null)
			listMunicipios.clear();
		else
			listMunicipios = new ArrayList<Municipio>();
	}
	
	public void seleccionarMunicipio(){
		try{
			pojoLocalidad = new Localidad();
		} catch (Exception e) {
			log.error("Error al seleccionar municipio", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargaEstados(){
		try {
			this.resOperacion = "";
			
			Respuesta respuesta = ifzColonias.cargarEstados();
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listEstados = (List<Estado>) respuesta.getBody().getValor("listEstados");
				
				if(listTmpEstados != null)
					listTmpEstados.clear();
				else
					listTmpEstados = new ArrayList<SelectItem>();
				
				for(Estado pojoEstadoAux : listEstados)
					listTmpEstados.add(new SelectItem(pojoEstadoAux.getId(), pojoEstadoAux.getNombre()));
			} else
				this.resOperacion = problemInesp;
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaZonasEconomicas", e);
		}
	}
	
	public Long getIdPojoEstado(){
		return pojoEstado.getId();
	}
	
	public void setIdPojoEstado(Long id){
		for(Estado pojoEstadoAux : listEstados){
			if(pojoEstadoAux.getId() == id){
				pojoEstado = pojoEstadoAux;
			}
		}
	}
	
	public String getResOperacion() {
		return resOperacion;
	}


	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public Boolean getPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(Boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public List<Colonia> getListColonias() {
		return listColonias;
	}

	public void setListColonias(List<Colonia> listColonias) {
		this.listColonias = listColonias;
	}

	public Colonia getPojoColonia() {
		return pojoColonia;
	}

	public void setPojoColonia(Colonia pojoColonia) {
		this.pojoColonia = pojoColonia;
	}

	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}

	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public List<SelectItem> getListTmpEstados() {
		return listTmpEstados;
	}

	public void setListTmpEstados(List<SelectItem> listTmpEstados) {
		this.listTmpEstados = listTmpEstados;
	}

	public Localidad getPojoLocalidad() {
		return pojoLocalidad;
	}

	public void setPojoLocalidad(Localidad pojoLocalidad) {
		this.pojoLocalidad = pojoLocalidad;
	}

	public Municipio getPojoMunicipio() {
		return pojoMunicipio;
	}

	public void setPojoMunicipio(Municipio pojoMunicipio) {
		this.pojoMunicipio = pojoMunicipio;
	}

	public int getNumPaginaLocalidades() {
		return numPaginaLocalidades;
	}

	public void setNumPaginaLocalidades(int numPaginaLocalidades) {
		this.numPaginaLocalidades = numPaginaLocalidades;
	}

	public int getNumPaginaMunicipios() {
		return numPaginaMunicipios;
	}

	public void setNumPaginaMunicipios(int numPaginaMunicipios) {
		this.numPaginaMunicipios = numPaginaMunicipios;
	}

	public String getValorBusquedaLocalidades() {
		return valorBusquedaLocalidades;
	}

	public void setValorBusquedaLocalidades(String valorBusquedaLocalidades) {
		this.valorBusquedaLocalidades = valorBusquedaLocalidades;
	}

	public String getValorBusquedaMunicipios() {
		return valorBusquedaMunicipios;
	}

	public void setValorBusquedaMunicipios(String valorBusquedaMunicipios) {
		this.valorBusquedaMunicipios = valorBusquedaMunicipios;
	}
}
