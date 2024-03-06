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
import net.giro.rh.admon.beans.Categoria;
import net.giro.rh.admon.logica.CategoriaRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean
public class CategoriaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CategoriaAction.class);
	

	private InitialContext ctx;
	
    private Integer usuarioId;	    
	private int numPagina;
	
	Categoria categoria;
	String descripcion;
	
	// ----------------------------- Variables para la b�squeda
	private String valorBusqueda;
	private int tipoMensaje;
	private boolean band;
	private String resOperacion;
	
	private CategoriaRem ifzCategoria;
	
	private List<Categoria> listaCategoriasGrid;
	
	public CategoriaAction() throws NamingException,Exception{
		this.listaCategoriasGrid = new ArrayList<Categoria>();
		String ejbName;
		
		this.tipoMensaje = 0;
		this.band = false;
		this.numPagina = 1;

		this.ctx = new InitialContext();

		ejbName = "ejb:/Logica_RecHum//CategoriaFac!net.giro.rh.admon.logica.CategoriaRem";
		this.ifzCategoria = (CategoriaRem) this.ctx.lookup(ejbName);

		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
		this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
		
		//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		//PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		
		this.categoria = new Categoria();
	
	}
	
	public String nuevo(){
		try{
			band=false;
			tipoMensaje=0;
			this.resOperacion = "";
			this.setDescripcion("");
			this.categoria = new Categoria();
			
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
			if ( this.categoria.getId() == null ){
				/*
				Categoria categoria = new Categoria();
				//categoria.setDescripcion( this.categoria.getDescripcion() );
				categoria.setDescripcion( this.categoria.getDescripcion() );
				
				categoria.setCreadoPor( this.usuarioId.longValue() );
				
				categoria.setFechaCreacion( Calendar.getInstance().getTime() );
				
				categoria.setModificadoPor( this.usuarioId.longValue() );
				
				categoria.setFechaModificacion( Calendar.getInstance().getTime() );
				
				categoria.setEstatus(0);
				*/
				this.categoria.setDescripcion(this.getDescripcion());
				this.categoria.setCreadoPor( this.usuarioId.longValue() );
				this.categoria.setFechaCreacion( Calendar.getInstance().getTime() );
				this.categoria.setModificadoPor( this.usuarioId.longValue() );
				this.categoria.setFechaModificacion( Calendar.getInstance().getTime() );
				this.categoria.setEstatus(0);
				
				this.categoria.setId( this.ifzCategoria.save( categoria ) );
				this.listaCategoriasGrid.add(this.categoria);
				
				log.info("Categoria guardada con id: "+this.categoria.getId());
				
				this.setResOperacion("OK");
				tipoMensaje = 0;
				this.setBand(false);
				
			}else{
				
				this.categoria.setDescripcion( this.getDescripcion() );
				this.categoria.setModificadoPor( this.usuarioId.longValue() );
				this.categoria.setFechaModificacion( Calendar.getInstance().getTime() );
				
				this.ifzCategoria.update( categoria );
				for (Categoria var : this.listaCategoriasGrid) {
					if (var.getId() == categoria.getId())
						var = categoria;
				}

				//this.categoria = null;
				
				this.setResOperacion("OK");
				tipoMensaje = 0;
				this.setBand(false);
				
			}
		}catch(Exception e) {
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

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
		this.descripcion = categoria.getDescripcion();
		System.out.println( "setCategoria: this.categoria.getId() --> " + this.categoria.getId() +  " || this.categoria.getDescripcion()--> "+this.categoria.getDescripcion());
	}
	
	public String eliminar(){
		
		try {
			//log.info("this.categoria.getDescripcion()");
			
			System.out.println( "Eliminar: this.categoria.getId() --> " + this.categoria.getId() +  " || this.categoria.getDescripcion()--> "+this.categoria.getDescripcion());
			this.categoria.setEstatus(1);
			this.categoria.setModificadoPor(this.usuarioId.longValue() );
			this.categoria.setFechaModificacion(Calendar.getInstance().getTime());
			this.categoria.setDescripcion( this.categoria.getDescripcion() );
			this.ifzCategoria.update( this.categoria );
			
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
				this.listaCategoriasGrid = this.ifzCategoria.findAll();
			}else{
				this.listaCategoriasGrid = this.ifzCategoria.findByProperty("descripcion", this.valorBusqueda);
			}
			
			if(this.listaCategoriasGrid.isEmpty()) {
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

	public List<Categoria> getListaCategoriasGrid() {
		return listaCategoriasGrid;
	}

	public void setListaCategoriasGrid(List<Categoria> listaCategoriasGrid) {
		this.listaCategoriasGrid = listaCategoriasGrid;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}