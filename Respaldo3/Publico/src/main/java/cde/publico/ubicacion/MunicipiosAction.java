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
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.plataforma.ubicacion.beans.Municipio;
import net.giro.ubicacion.logica.MunicipiosRem;

import org.apache.log4j.Logger;

public class MunicipiosAction implements Serializable {
	private static final long serialVersionUID = 985652410351472793L;
	Logger log = Logger.getLogger(MunicipiosAction.class);
	
	private MunicipiosRem ifzMunicipios;
	
	private List<Municipio> listMunicipios;
	private List<Estado> listTmpEstados;
	
	private List<SelectItem> listEstados;
	
	private Municipio pojoMunicipio;
	
	private int numPagina;
	
	private Long usuarioId;
	
	private Boolean puedeEditar;
	
	private String resOperacion;
	private String tipoBusqueda;
	private String valorBusqueda;
	private String problemInesp;
	
	LoginManager loginManager;
	private Context ctx;
	
	public MunicipiosAction() throws Exception{
		numPagina = 1;
		
		tipoBusqueda = "";
		valorBusqueda = "";
		
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
		
		listMunicipios = new ArrayList<Municipio>();
		pojoMunicipio = new Municipio();
		
		puedeEditar = true;
		
		ifzMunicipios = (MunicipiosRem)ctx.lookup("ejb:/Logica_Publico//MunicipiosFac!net.giro.ubicacion.logica.MunicipiosRem");
		cargaEstados();
	}
	
	public void buscar(){
		try {
			this.listMunicipios = this.ifzMunicipios.buscarMunicipios(this.tipoBusqueda, valorBusqueda);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public void nuevo(){
		pojoMunicipio = new Municipio();
	}
	
	public void guardar(){
		try {
			this.pojoMunicipio.setModificadoPor(Long.valueOf(this.usuarioId));
			this.pojoMunicipio.setFechaModificacion(Calendar.getInstance().getTime());
			
			if(this.pojoMunicipio.getId() <= 0){
				this.pojoMunicipio.setCreadoPor(Long.valueOf(this.usuarioId));
				this.pojoMunicipio.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoMunicipio.setId(ifzMunicipios.salvar(pojoMunicipio));
				this.listMunicipios.add(0,pojoMunicipio);
			}else{
				this.ifzMunicipios.salvar(this.pojoMunicipio);
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
			ifzMunicipios.eliminar(pojoMunicipio);
			this.listMunicipios.remove(pojoMunicipio);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar", e);
		}
	}
	
	private Estado getEstadoById(Long id, List<Estado> lista){
		for(Estado edo :lista){
			if(edo.getId() == id.longValue())
				return edo;
		}
		return null;
	}
	
	public void cargaEstados(){
		if(this.listTmpEstados!=null && !this.listTmpEstados.isEmpty())
			return;
		try {
			this.listTmpEstados =  this.ifzMunicipios.buscarEstados();
			
			if(this.listEstados==null) 
				this.listEstados = new ArrayList<SelectItem>();
			else
				this.listEstados.clear();
			for(Estado edo : this.listTmpEstados){
				this.listEstados.add(new SelectItem(edo.getId(), edo.getNombre()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaCompaniasTel", e);
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

	public void setIdEstado(Long idEstado) {
		this.pojoMunicipio.setEstado(getEstadoById(idEstado, listTmpEstados));
	}

	public Long getIdEstado() {
		return this.pojoMunicipio.getEstado()!=null ? this.pojoMunicipio.getEstado().getId() : -1l;
	}

	public List<Municipio> getListMunicipios() {
		return listMunicipios;
	}

	public void setListMunicipios(List<Municipio> listMunicipios) {
		this.listMunicipios = listMunicipios;
	}

	public Municipio getPojoMunicipio() {
		return pojoMunicipio;
	}

	public void setPojoMunicipio(Municipio pojoMunicipio) {
		this.pojoMunicipio = pojoMunicipio;
	}

	public List<SelectItem> getListEstados() {
		return listEstados;
	}

	public void setListEstados(List<SelectItem> listEstados) {
		this.listEstados = listEstados;
	}
}
