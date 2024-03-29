package cde.publico.ubicacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.PropertyResourceBundle;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.naming.Context;

import net.giro.navegador.LoginManager;
import net.giro.ne.beans.Regiones;
import net.giro.ubicacion.logica.RegionesRem;

import org.apache.log4j.Logger;

public class RegionesAction implements Serializable {
	private static final long serialVersionUID = 985652410351472793L;
	Logger log = Logger.getLogger(RegionesAction.class);
	
	private RegionesRem ifzRegiones;
	
	private List<Regiones> listRegiones;
	
	private Regiones pojoRegion;
	
	private int numPagina;
	
	private Long usuarioId;
	
	private Boolean puedeEditar;
	
	private String resOperacion;
	private String tipoBusqueda;
	private String valorBusqueda;
	private String problemInesp;
	
	LoginManager loginManager;
	private Context ctx;
	
	public RegionesAction() throws Exception{
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
		
		listRegiones = new ArrayList<Regiones>();
		pojoRegion= new Regiones();
		
		puedeEditar = true;
		
		ifzRegiones = (RegionesRem)ctx.lookup("ejb:/Logica_Publico//RegionesFac!net.giro.ubicacion.logica.RegionesRem");
	}
	
	public void buscar(){
		try {
			this.listRegiones = this.ifzRegiones.buscarRegiones(this.tipoBusqueda, valorBusqueda);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = problemInesp;
			log.error("Error en el metodo buscar", e);
		}
	}

	public void nuevo(){
		pojoRegion = new Regiones();
	}
	
	public void guardar(){
		try {
			this.pojoRegion.setModificadoPor(Long.valueOf(this.usuarioId));
			this.pojoRegion.setFechaModificacion(Calendar.getInstance().getTime());
			
			if(this.pojoRegion.getId() <= 0){
				this.pojoRegion.setCreadoPor(Long.valueOf(this.usuarioId));
				this.pojoRegion.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoRegion.setId(ifzRegiones.salvar(pojoRegion));
				this.listRegiones.add(0,pojoRegion);
			}else{
				this.ifzRegiones.salvar(this.pojoRegion);
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
			ifzRegiones.eliminar(pojoRegion);
			this.listRegiones.remove(pojoRegion);
			this.resOperacion = "";
		} catch (Exception e) {
			this.resOperacion = this.problemInesp;
			log.error("Error en el metodo eliminar", e);
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

	public List<Regiones> getListRegiones() {
		return listRegiones;
	}

	public void setListRegiones(List<Regiones> listRegiones) {
		this.listRegiones = listRegiones;
	}

	public Regiones getPojoRegion() {
		return pojoRegion;
	}

	public void setPojoRegion(Regiones pojoRegion) {
		this.pojoRegion = pojoRegion;
	}
}
