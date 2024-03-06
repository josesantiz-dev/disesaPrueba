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
import net.giro.plataforma.ubicacion.beans.Estado;
import net.giro.ubicacion.logica.EstadosRem;

import org.apache.log4j.Logger;

public class EstadosAction implements Serializable {
	private static final long serialVersionUID = 985652410351472793L;
	Logger log = Logger.getLogger(EstadosAction.class);
	
	private EstadosRem ifzEstados;
	
	private List<Estado> listaEstados;
	private List<ConValores> listTmpZonaEconomica;
	
	private List<SelectItem> listaZonasEconomicas;
	
	private Estado pojoEstado;
	
	private int numPagina;
	
	private Long usuarioId;
	
	private Boolean puedeEditar;
	
	private String resOperacion;
	private String tipoBusqueda;
	private String valorBusqueda;
	private String problemInesp;
	
	LoginManager loginManager;
	private Context ctx;
	
	public EstadosAction() throws Exception{
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
		
		listaEstados = new ArrayList<Estado>();
		pojoEstado = new Estado();
		
		puedeEditar = true;
		
		ifzEstados = (EstadosRem)ctx.lookup("ejb:/Logica_Publico//EstadosFac!net.giro.ubicacion.logica.EstadosRem");
		cargaZonasEconomicas();
	}
	
	public void buscar(){
		try {
			this.listaEstados = this.ifzEstados.buscarEstados(this.tipoBusqueda, valorBusqueda);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public void nuevo(){
		pojoEstado = new Estado();
	}
	
	public void guardar(){
		try {
			this.pojoEstado.setModificadoPor(Long.valueOf(this.usuarioId));
			this.pojoEstado.setFechaModificacion(Calendar.getInstance().getTime());
			
			if(this.pojoEstado.getId() <= 0){
				this.pojoEstado.setCreadoPor(Long.valueOf(this.usuarioId));
				this.pojoEstado.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoEstado.setId(ifzEstados.salvar(pojoEstado));
				this.listaEstados.add(0,pojoEstado);
			}else{
				this.ifzEstados.salvar(this.pojoEstado);
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
			ifzEstados.eliminar(pojoEstado);
			this.listaEstados.remove(pojoEstado);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar", e);
		}
	}
	
	private ConValores getzonaEconomicaById(Long id, List<ConValores> lista){
		for(ConValores cv :lista){
			if(cv.getId() == id.longValue())
				return cv;
		}
		return null;
	}
	
	public void cargaZonasEconomicas(){
		if(this.listTmpZonaEconomica!=null && !this.listTmpZonaEconomica.isEmpty())
			return;
		try {
			this.listTmpZonaEconomica =  this.ifzEstados.buscarZonasEconomicas();
			
			if(this.listaZonasEconomicas==null) 
				this.listaZonasEconomicas = new ArrayList<SelectItem>();
			else
				this.listaZonasEconomicas.clear();
			for(ConValores cv : this.listTmpZonaEconomica){
				this.listaZonasEconomicas.add(new SelectItem(cv.getId(), cv.getValor()));
			}
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo cargaZonasEconomicas", e);
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

	public List<Estado> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(List<Estado> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public Estado getPojoEstado() {
		return pojoEstado;
	}

	public void setPojoEstado(Estado pojoEstado) {
		this.pojoEstado = pojoEstado;
	}

	public Boolean getPuedeEditar() {
		return puedeEditar;
	}

	public void setPuedeEditar(Boolean puedeEditar) {
		this.puedeEditar = puedeEditar;
	}

	public List<SelectItem> getListaZonasEconomicas() {
		return listaZonasEconomicas;
	}

	public void setListaZonasEconomicas(List<SelectItem> listaZonasEconomicas) {
		this.listaZonasEconomicas = listaZonasEconomicas;
	}
	
	public void setIdZonaEconomica(Long idZonaEconomica) {
		this.pojoEstado.setZonaEconomica(getzonaEconomicaById(idZonaEconomica, listTmpZonaEconomica));
	}

	public Long getIdZonaEconomica() {
		return this.pojoEstado.getZonaEconomica()!=null ? this.pojoEstado.getZonaEconomica().getId() : -1l;
	}
}
