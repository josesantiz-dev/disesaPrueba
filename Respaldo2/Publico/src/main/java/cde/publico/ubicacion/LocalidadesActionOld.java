package cde.publico.ubicacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.plataforma.beans.ConValores;
import net.giro.plataforma.ubicacion.beans.Localidad;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.ubicacion.logica.LocalidadesRem;

import org.apache.log4j.Logger;

public class LocalidadesActionOld implements Serializable {
	private static final long serialVersionUID = 985652410351472793L;
	Logger log = Logger.getLogger(LocalidadesActionOld.class);
	
	private LocalidadesRem ifzLocalidades;
	
	private List<Localidad> listLocalidades;
	private List<Municipio> listTmpMunicipios;
	private List<ConValores> listTmpZonas;
	private List<ConValores> listTmpGradoMarginalidad;
	
	private List<SelectItem> listMunicipios;
	private List<SelectItem> listZonas;
	private List<SelectItem> listGradoMarginalidad;
	
	private Localidad pojoLocalidad;
	
	private int numPagina;
	
	private Long usuarioId;
	
	private Boolean puedeEditar;
	
	private String resOperacion;
	private String tipoBusqueda;
	private String valorBusqueda;
	private String problemInesp;
	
	LoginManager loginManager;
	private Context ctx;
	
	public LocalidadesActionOld() throws Exception{
		numPagina = 1;
		
		tipoBusqueda = "";
		valorBusqueda = "";
		
		listLocalidades = new ArrayList<Localidad>();
		pojoLocalidad = new Localidad();
		
		puedeEditar = true;
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		loginManager = (LoginManager)ve.getValue(fc.getELContext());
		ctx = loginManager.getCtx();
		usuarioId = loginManager.getInfoSesion().getAcceso().getUsuario().getId();
				
		//obtengo los posibles mensajes a mostrar al usuario
		ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{msg}", PropertyResourceBundle.class);
		PropertyResourceBundle propPlataforma = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		problemInesp = propPlataforma.getString("mensajes.error.inesperado");
		
		ifzLocalidades = (LocalidadesRem)ctx.lookup("ejb:/Logica_Publico//LocalidadesFac!net.giro.ubicacion.logica.LocalidadesRem");
		
		cargaZonas();
		cargaMunicipios();
		cargaGradoMarginalidad();
	}
	
	public void buscar(){
		try {
			this.listLocalidades = this.ifzLocalidades.buscarLocalidades(this.tipoBusqueda, valorBusqueda);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public void nuevo(){
		pojoLocalidad = new Localidad();
	}
	
	public void guardar(){
		try {
			this.pojoLocalidad.setModificadoPor(Long.valueOf(this.usuarioId));
			this.pojoLocalidad.setFechaModificacion(Calendar.getInstance().getTime());
			
			if(this.pojoLocalidad.getId() <= 0){
				this.pojoLocalidad.setCreadoPor(Long.valueOf(this.usuarioId));
				this.pojoLocalidad.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoLocalidad.setId(ifzLocalidades.salvar(pojoLocalidad));
				this.listLocalidades.add(0,pojoLocalidad);
			}else{
				this.ifzLocalidades.salvar(this.pojoLocalidad);
			}
			
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo guardar", e);
		}		
	}
	
	public void editar(){
		
	}
	
	public void eliminar(){
		try {
			ifzLocalidades.eliminar(pojoLocalidad);
			this.listLocalidades.remove(pojoLocalidad);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar", e);
		}
	}
	
	private Municipio getMunicipioById(Long id, List<Municipio> lista){
		for(Municipio municipio :lista){
			if(municipio.getId() == id.longValue())
				return municipio;
		}
		return null;
	}
	
	private ConValores getValorById(Long id, List<ConValores> lista){
		for(ConValores cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}
	
	public void cargaMunicipios(){
		if(this.listTmpMunicipios!=null && !this.listTmpMunicipios.isEmpty())
			return;
		try {
			this.listTmpMunicipios =  this.ifzLocalidades.buscarMunicipios();
			
			if(this.listMunicipios==null) 
				this.listMunicipios = new ArrayList<SelectItem>();
			else
				this.listMunicipios.clear();
			for(Municipio municipio : this.listTmpMunicipios){
				this.listMunicipios.add(new SelectItem(municipio.getId(), municipio.getNombre()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaCompaniasTel", e);
		}
	}
	
	public void cargaZonas(){
		if(this.listTmpZonas!=null && !this.listTmpZonas.isEmpty())
			return;
		try {
			this.listTmpZonas =  this.ifzLocalidades.buscarZonas();
			
			if(this.listZonas==null) 
				this.listZonas = new ArrayList<SelectItem>();
			else
				this.listZonas.clear();
			for(ConValores cv : this.listTmpZonas){
				this.listZonas.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaCompaniasTel", e);
		}
	}
	
	public void cargaGradoMarginalidad(){
		if(this.listTmpGradoMarginalidad!=null && !this.listTmpGradoMarginalidad.isEmpty())
			return;
		try {
			this.listTmpGradoMarginalidad =  this.ifzLocalidades.buscarGradoMarginalidad();
			
			if(this.listGradoMarginalidad==null) 
				this.listGradoMarginalidad = new ArrayList<SelectItem>();
			else
				this.listGradoMarginalidad.clear();
			for(ConValores cv : this.listTmpGradoMarginalidad){
				this.listGradoMarginalidad.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaGradoMarginalidad", e);
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
	
	public void setIdMunicipios(Long idMunicipio) {
		this.pojoLocalidad.setMunicipio(getMunicipioById(idMunicipio, listTmpMunicipios));
	}

	public Long getIdMunicipios() {
		return this.pojoLocalidad.getMunicipio()!=null ? this.pojoLocalidad.getMunicipio().getId() : -1l;
	}

	public void setIdZona(Long idZonaEconomica) {
		this.pojoLocalidad.setZona(getValorById(idZonaEconomica, listTmpZonas));
	}

	public Long getIdZona() {
		return this.pojoLocalidad.getZona()!=null ? this.pojoLocalidad.getZona().getId() : -1l;
	}
	
	/*public void setIdGradoMarginalidad(Long idGradoMarginalidad) {
		this.pojoLocalidad.setGradoMarginalidad(getValorById(idGradoMarginalidad, listTmpGradoMarginalidad));
	}

	public Long getIdGradoMarginalidad() {
		return this.pojoLocalidad.getGradoMarginalidad() !=null ? this.pojoLocalidad.getGradoMarginalidad().getId() : -1l;
	}*/

	public List<Localidad> getListLocalidades() {
		return listLocalidades;
	}

	public void setListLocalidades(List<Localidad> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}

	public Localidad getPojoLocalidad() {
		return pojoLocalidad;
	}

	public void setPojoLocalidad(Localidad pojoLocalidad) {
		this.pojoLocalidad = pojoLocalidad;
	}

	public List<SelectItem> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<SelectItem> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public List<SelectItem> getListZonas() {
		return listZonas;
	}

	public void setListZonas(List<SelectItem> listZonas) {
		this.listZonas = listZonas;
	}

	public List<SelectItem> getListGradoMarginalidad() {
		return listGradoMarginalidad;
	}

	public void setListGradoMarginalidad(List<SelectItem> listGradoMarginalidad) {
		this.listGradoMarginalidad = listGradoMarginalidad;
	}
}
