package net.giro.rh.admon.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.Puesto;
import net.giro.rh.admon.logica.PuestoRem;

import org.apache.log4j.Logger;


@ManagedBean
@ViewScoped
public class PuestoAction implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PuestoAction.class);
	

	private InitialContext ctx;
	
    private Integer usuarioId;	    
	private int numPagina;
	
	Puesto pojoPuesto;
	String descripcion;
	
	// ----------------------------- Variables para la búsqueda
	private String valorBusqueda;
	private int tipoMensaje;
	private boolean band;
	private String resOperacion;
	
	private PuestoRem ifzPuesto;
	
	private List<Puesto> listaPuestosGrid;
	
	public PuestoAction() throws NamingException,Exception{
		this.listaPuestosGrid = new ArrayList<Puesto>();
		String ejbName;
		
		this.tipoMensaje = 0;
		this.band = false;
		this.numPagina = 1;

		this.ctx = new InitialContext();

		ejbName = "ejb:/Logica_RecHum//PuestoFac!net.giro.rh.admon.logica.PuestoRem";
		this.ifzPuesto = (PuestoRem) this.ctx.lookup(ejbName);

		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
		this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
		
		//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		//PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		
		this.pojoPuesto = new Puesto();
	
		
	}
	
	public String nuevo(){
		try{
			band=false;
			tipoMensaje=0;
			this.resOperacion = "";
			
			this.pojoPuesto = new Puesto();
			
		} catch(Exception e) {
			band=true;
			tipoMensaje=1;
			this.resOperacion = "ERROR";
			log.error("Error en el metodo nuevo.", e);
			return "ERROR";
		}
		this.resOperacion = "OK";
		return "OK";
	}
	
	public String guardar(){
		this.resOperacion = "";
		try{
			if ("".equals(this.descripcion.trim())) {
				band=true;
				tipoMensaje=1;
				this.resOperacion = "Debe indicar el nombre del puesto";
				return "";
			}
			
			if ( this.pojoPuesto.getId() == null) {
				this.pojoPuesto.setDescripcion( this.descripcion );
				this.pojoPuesto.setCreadoPor( this.usuarioId.longValue() );
				this.pojoPuesto.setFechaCreacion( Calendar.getInstance().getTime() );
				this.pojoPuesto.setModificadoPor( this.usuarioId.longValue() );
				this.pojoPuesto.setFechaModificacion( Calendar.getInstance().getTime() );
				this.pojoPuesto.setEstatus(0);
				
				this.pojoPuesto.setId( this.ifzPuesto.save( this.pojoPuesto ));
				
				log.info("puesto guardado con id: "+this.pojoPuesto.getId());
				this.listaPuestosGrid.add(this.pojoPuesto);
				this.resOperacion = "OK";
				this.tipoMensaje = 0;
				this.band = false;
			} else {
				this.pojoPuesto.setDescripcion( this.getDescripcion() );
				
				log.info( "this.pojoPuesto.getDescripcion: " + this.pojoPuesto.getDescripcion() );
				
				this.pojoPuesto.setModificadoPor( this.usuarioId.longValue() );
				this.pojoPuesto.setFechaModificacion( Calendar.getInstance().getTime() );
				
				this.ifzPuesto.update( pojoPuesto );
				for (Puesto var : this.listaPuestosGrid) {
					if (var.getId() == this.pojoPuesto.getId())
						var = this.pojoPuesto;
				}
				
				this.resOperacion = "OK";
				this.tipoMensaje = 0;
				this.band = false;
			}
		} catch(Exception e) {
			band=true;
			tipoMensaje=1;
			this.resOperacion = "ERROR";
			log.error("error al guardar", e);
			return "ERROR";
		}

		this.resOperacion = "";
		return "OK";
	}
	
	public String editar(){	//si se necesita
		this.resOperacion = "";
		return "OK";
	}

	public Puesto getPojoPuesto() {
		return pojoPuesto;
	}

	public void setPojoPuesto(Puesto puesto) {
		log.info("setPojoPuesto: puesto.getDescripcion() --> "+puesto.getDescripcion());
		this.pojoPuesto = puesto;
		this.descripcion = puesto.getDescripcion();
		
	}
	
	public String eliminar(){
		
		try {
			
			this.pojoPuesto.setEstatus(1);
			this.pojoPuesto.setModificadoPor(this.usuarioId.longValue() );
			this.pojoPuesto.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoPuesto.setDescripcion( this.pojoPuesto.getDescripcion() );
			this.ifzPuesto.update( this.pojoPuesto );
			
		} catch (Exception e) {
			log.error("Error en el metodo eliminar.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String buscar(){
		try{
			this.resOperacion = "";
			
			if( "".equals(this.valorBusqueda) ){	//id, descripcion
				this.listaPuestosGrid = this.ifzPuesto.findAll();
			}else{
				this.listaPuestosGrid = this.ifzPuesto.findByProperty("descripcion", this.valorBusqueda);
			}
			
			if (this.listaPuestosGrid.isEmpty()) {
				log.error("La busqueda no devolvio resultados");
				this.resOperacion = "La busqueda no devolvio resultados.";
				return "ERROR";
			}
		} catch(Exception e) {
			log.error("error al buscar", e);
			this.resOperacion = "ERROR";
			return "ERROR";
		}
		
		this.resOperacion = "";
		return "OK";
		
	}
	
	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}


	public String getValorBusqueda() {
		return valorBusqueda;
	}

	public void setValorBusqueda(String valorBusqueda) {
		this.valorBusqueda = valorBusqueda;
	}

	public int getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	public boolean isBand() {
		return band;
	}

	public void setBand(boolean band) {
		this.band = band;
	}

	public String getResOperacion() {
		return resOperacion;
	}

	public void setResOperacion(String resOperacion) {
		this.resOperacion = resOperacion;
	}

	public List<Puesto> getListaPuestosGrid() {
		return listaPuestosGrid;
	}

	public void setListaPuestosGrid(List<Puesto> listaPuestosGrid) {
		this.listaPuestosGrid = listaPuestosGrid;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
