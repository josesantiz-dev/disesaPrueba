package cde.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.logica.LocalidadesRem;
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.publico.respuesta.Respuesta;

import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;

@KeepAlive
public class LocalidadesAction {
	Logger log = Logger.getLogger(LocalidadesAction.class);
	
	private Context ctx;
	@SuppressWarnings("unused")
	private Object lookup;	
	LoginManager loginManager;
	
	private String resOperacion;
	private String problemInesp;
	
	long numPagina;
	long numPaginaMunicipios;
	
	List<Estado> listEstados;
	List<Municipio> listMunicipios;
	List<Localidad> listLocalidades;
	
	List<SelectItem> listTmpMunicipios;
	List<SelectItem> listTmpEstados;
	
	
	Estado pojoEstado;
	Municipio pojoMunicipio;
	Localidad pojoLocalidad;
	
	String tipoBusqueda;
	String valorBusqueda;
	
	String valorBusquedaMunicipios;
	
	LocalidadesRem ifzLocalidades;
	
	public LocalidadesAction() throws Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		ctx = loginManager.getCtx();
		
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensaje.error.inesperado");
		
		ifzLocalidades = (LocalidadesRem) ctx.lookup("ejb:/Logica_Publico//LocalidadesFac!net.giro.plataforma.logica.LocalidadesRem");
		ifzLocalidades.setInfoSesion(loginManager.getInfoSesion());
	
		numPagina = 1;
		numPaginaMunicipios = 1;
		
		listEstados = new ArrayList<Estado>();
		listMunicipios = new ArrayList<Municipio>();	
		listLocalidades = new ArrayList<Localidad>();
		
		listTmpEstados = new ArrayList<SelectItem>();
		listTmpMunicipios = new ArrayList<SelectItem>();
		
		pojoEstado = new Estado();
		pojoLocalidad = new Localidad();
		pojoMunicipio = new Municipio();
		
		cargarEstados();
	}
	
	@SuppressWarnings("unchecked")
	public void buscar(){
		try{
			this.resOperacion = "";
			
			Respuesta respuesta = ifzLocalidades.buscarLocalidades(tipoBusqueda, valorBusqueda);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listLocalidades = (List<Localidad>) respuesta.getBody().getValor("listLocalidades");
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al buscar", e);
			this.resOperacion = problemInesp;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void buscarMunicipios(){
		try{
			this.resOperacion = "";
			
			Respuesta respuesta = ifzLocalidades.buscarMunicipios(pojoEstado, valorBusquedaMunicipios);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listMunicipios = (List<Municipio>) respuesta.getBody().getValor("listMunicipios");
			} else 
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error al buscar municipios", e);
		}
	}
	
	public void cargarMunicipios(){
		
	}
	
	@SuppressWarnings("unchecked")
	public void cargarEstados(){
		try{
			this.resOperacion = "";
			
			Respuesta respuesta = ifzLocalidades.cargarEstados();
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				listEstados = (List<Estado>)respuesta.getBody().getValor("listEstados");
				
				if(listTmpEstados == null)
					listTmpEstados = new ArrayList<SelectItem>();
				else
					listTmpEstados.clear();
				
				for(Estado pojoEstadoAux : listEstados){
					SelectItem selectItem = new SelectItem(pojoEstadoAux.getId(), pojoEstadoAux.getNombre());
					listTmpEstados.add(selectItem);
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e) {
			log.error("Error al cargar estados", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void guardar(){
		try{
			this.resOperacion = "";
			
			pojoLocalidad.setMunicipio(pojoMunicipio);
			
			Respuesta respuesta = ifzLocalidades.guardarLocalidad(pojoLocalidad);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				long id = pojoLocalidad.getId();
				
				pojoLocalidad = (Localidad) respuesta.getBody().getValor("pojoLocalidad");
				
				if(id == 0L)
					listLocalidades.add(pojoLocalidad);
				else {
					List<Localidad> listLocalidadesAux = new ArrayList<Localidad>();
					
					listLocalidadesAux.add(pojoLocalidad);
					
					for(Localidad pojoLocalidadAux : listLocalidades){
						if(pojoLocalidadAux.getId() != id)
							listLocalidadesAux.add(pojoLocalidadAux);
					}
					
					listLocalidades = listLocalidadesAux;
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e){
			log.error("Error al guardar", e);
			this.resOperacion = problemInesp;
		}
	}
	
	public void guardarMunicipio(){
		try{
			this.resOperacion = "";
			
			Respuesta respuesta = ifzLocalidades.guardarMunicipio(pojoMunicipio);
			
			if(respuesta.getErrores().getCodigoError() == 0L){
				long id = pojoMunicipio.getId();
				
				pojoMunicipio = (Municipio) respuesta.getBody().getValor("pojoMunicipio");
				
				if(id == 0L)
					listMunicipios.add(pojoMunicipio);
				else{
					List<Municipio> listMunicipiosAux = new ArrayList<Municipio>();
					
					listMunicipiosAux.add(pojoMunicipio);
					
					for(Municipio pojoMunicipioAux : listMunicipios){
						if(pojoMunicipioAux.getId() != id)
							listMunicipiosAux.add(pojoMunicipioAux);
					}
					
					listMunicipios = listMunicipiosAux;
				}
			} else
				this.resOperacion = respuesta.getErrores().getDescError();
		} catch (Exception e){
			log.error("Error al guardarMunicipio", e);
			this.resOperacion = this.problemInesp;
		}
	}
	
	public void eliminar(){
		
	}
	
	public void editar(){
		this.resOperacion = "";
		try{
			pojoMunicipio = pojoLocalidad.getMunicipio();
		} catch (Exception e) {
			log.error("Error al editar", e);
			this.resOperacion = this.problemInesp;
		}
	}
	
	public void nuevo(){
		this.resOperacion = "";
		try{
			pojoLocalidad = new Localidad();
			pojoMunicipio = new Municipio();
		} catch (Exception e) {
			log.error("Error en nuevo" ,e);
			this.resOperacion = this.problemInesp;
		}
	}
	
	public void nuevoMunicipio() {
		this.resOperacion = "";
		try {
			pojoMunicipio = new Municipio();
		} catch (Exception e) {
			log.error("Error en nuevoMunicipio", e);
			this.resOperacion = this.problemInesp;
		}
	}
	
	public void seleccionarMunicipio() {
		this.resOperacion = "";
	}
	
	public long getPojoEstadoAux(){
		return pojoEstado != null ? pojoEstado.getId() : 0L;
	}
	
	public void setPojoEstadoAux(long idEstado){
		pojoEstado = new Estado();
		pojoEstado.setId(idEstado);
		
		for(Estado pojoEstadoAux : listEstados){
			if(pojoEstadoAux.getId() == idEstado){
				pojoEstado = pojoEstadoAux;
			}
		}
	}
	
	public long getPojoEstadoMunicipioAux(){
		return pojoMunicipio.getEstado() != null ? pojoMunicipio.getEstado().getId() : 0L;
	}
	
	public void setPojoEstadoMunicipioAux(long idEstado){
		pojoMunicipio.setEstado(new Estado());
		pojoMunicipio.getEstado().setId(idEstado);
		
		for(Estado pojoEstadoAux : listEstados){
			if(pojoEstadoAux.getId() == idEstado){
				pojoMunicipio.setEstado(pojoEstadoAux);
			}
		}
	}
	
	public String getResOperacion() {
		return resOperacion;
	}
	
	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}
	
	public long getNumPagina() {
		return numPagina;
	}
	
	public void setNumPagina(long numPagina) {
		this.numPagina = numPagina;
	}
	
	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}
	
	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}
	
	public List<SelectItem> getListTmpMunicipios() {
		return listTmpMunicipios;
	}
	
	public void setListTmpMunicipios(List<SelectItem> listTmpMunicipios) {
		this.listTmpMunicipios = listTmpMunicipios;
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

	public List<SelectItem> getListTmpEstados() {
		return listTmpEstados;
	}

	public void setListTmpEstados(List<SelectItem> listTmpEstados) {
		this.listTmpEstados = listTmpEstados;
	}

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public long getNumPaginaMunicipios() {
		return numPaginaMunicipios;
	}

	public void setNumPaginaMunicipios(long numPaginaMunicipios) {
		this.numPaginaMunicipios = numPaginaMunicipios;
	}

	public String getValorBusquedaMunicipios() {
		return valorBusquedaMunicipios;
	}

	public void setValorBusquedaMunicipios(String valorBusquedaMunicipios) {
		this.valorBusquedaMunicipios = valorBusquedaMunicipios;
	}
}
