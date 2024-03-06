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
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.Categoria;
import net.giro.rh.admon.beans.Puesto;
import net.giro.rh.admon.beans.PuestoCategoria;
import net.giro.rh.admon.beans.PuestoCategoriaExt;
import net.giro.rh.admon.logica.CategoriaRem;
import net.giro.rh.admon.logica.PuestoCategoriaRem;
import net.giro.rh.admon.logica.PuestoRem;

import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class PuestoCategoriaAction implements Serializable  {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PuestoCategoriaAction.class);
	
	private InitialContext ctx;
	
    private Integer usuarioId;	    
	private int numPagina;
	
	private List<PuestoCategoriaExt> listaPuestosCategoriasGrid;
	private PuestoCategoriaExt pojoPuestoCategoria;
	
	private PuestoCategoriaRem ifzPuestoCategoria;
	private CategoriaRem ifzCategoria;
	private PuestoRem ifzPuesto;
	//private EmpresasRem ifzEmpresas;
	
	private List<Puesto> listaPuestos;
	private List<Categoria> listaCategorias;
	
	private List<SelectItem> listaCboPuestos;
	private long idPuesto;
	private Puesto pojoPuesto;
	
	private List<SelectItem> listaCboCategorias;
	private long idCategoria;
	private Categoria pojoCategoria;

	private int tipoMensaje;
	private boolean band;
	private String resOperacion;

	// Para el Nuevo/Editar PuestoCategoria

	private List<SelectItem> listaCboNuevoPuesto;
	private long idNuevoPuesto;
	
	private List<SelectItem> listaCboNuevaCategoria;
	private long idNuevaCategoria;
	
	
	public PuestoCategoriaAction() throws NamingException,Exception {
		
		String ejbName;
		
		this.tipoMensaje = 0;
		this.band = false;
		this.numPagina = 1;

		this.ctx = new InitialContext();

		ejbName = "ejb:/Logica_RecHum//PuestoFac!net.giro.rh.admon.logica.PuestoRem";
		this.ifzPuesto = (PuestoRem) this.ctx.lookup(ejbName);

		ejbName = "ejb:/Logica_RecHum//CategoriaFac!net.giro.rh.admon.logica.CategoriaRem";
		this.ifzCategoria = (CategoriaRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_RecHum//PuestoCategoriaFac!net.giro.rh.admon.logica.PuestoCategoriaRem";
		this.ifzPuestoCategoria = (PuestoCategoriaRem) this.ctx.lookup(ejbName);

		//ejbName = "ejb:/Logica_Publico//EmpresasFac!net.giro.plataforma.logica.EmpresasRem";
		//this.ifzEmpresas = (EmpresasRem) ctx.lookup(ejbName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
		this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
		
		//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		//PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		
		this.pojoPuestoCategoria = new PuestoCategoriaExt();
		this.listaPuestosCategoriasGrid = new ArrayList<PuestoCategoriaExt>();

		pojoPuesto = new Puesto();
		pojoCategoria = new Categoria();
		
		listarPuestos();
		listarCategorias();
		
		cargarPuestos();
		cargarCategorias();
		
		cargarNuevosPuestos();
		cargarNuevasCategorias();
		
	}
	
	
	private void cargarPuestos(){
		this.listaCboPuestos = null;
		this.listaCboPuestos = new ArrayList<SelectItem>();
		this.listaCboPuestos.add( new SelectItem( "0", "Todos" ) );

		for(Puesto p: this.listaPuestos ){
			this.listaCboPuestos.add( new SelectItem( p.getId().toString(), p.getDescripcion() ) );
		}
	}
	
	private void cargarNuevosPuestos(){
		this.listaCboNuevoPuesto = null;
		this.listaCboNuevoPuesto = new ArrayList<SelectItem>();

		for(Puesto p: this.listaPuestos ){
			if ( p.getEstatus() == 0 )
				this.listaCboNuevoPuesto.add( new SelectItem( p.getId().toString(), p.getDescripcion() ) );
		}
	}
	
	private void cargarNuevasCategorias(){
		this.listaCboNuevaCategoria = null;
		this.listaCboNuevaCategoria = new ArrayList<SelectItem>();

		for(Categoria c: this.listaCategorias ){
			if( c.getEstatus() == 0 )
				this.listaCboNuevaCategoria.add( new SelectItem( c.getId().toString(), c.getDescripcion() ) );
		}
	}
	
	private void cargarCategorias(){
		this.listaCboCategorias = null;
		this.listaCboCategorias = new ArrayList<SelectItem>();
		this.listaCboCategorias.add( new SelectItem( "0", "Todos" ) );

		for(Categoria c: this.listaCategorias ){
			this.listaCboCategorias.add( new SelectItem( c.getId().toString(), c.getDescripcion() ) );
		}
		
	}
	
	public String buscar(){
		try{
			this.resOperacion = "";
			
			if( this.idPuesto == 0 && this.idCategoria == 0 ){
				this.listaPuestosCategoriasGrid = this.ifzPuestoCategoria.findAllExt();
			}else{
				if( this.idPuesto >0 && this.idCategoria == 0 ){
					//log.info("Buscar Todos los puestos concordantes");
					this.listaPuestosCategoriasGrid = this.ifzPuestoCategoria.findByPropertyExt("idPuesto", this.idPuesto);
				}else{
					if(this.idPuesto == 0 && this.idCategoria > 0){
						//log.info("Buscar Todos las categorias concordantes");
						this.listaPuestosCategoriasGrid = this.ifzPuestoCategoria.findByPropertyExt("idCategoria", this.idCategoria);
					}else{
						//log.info("Buscar que concuerde puesto y categoria");
						this.listaPuestosCategoriasGrid = this.ifzPuestoCategoria.findByPuestoCategoriaExt( (int) idPuesto, (int) idCategoria);
					}
				}
			}
			
			if(this.listaPuestosCategoriasGrid.isEmpty()) {
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
	
	public String nuevo(){
		try{
			band=false;
			tipoMensaje=0;
			this.resOperacion = "";
			
			this.pojoPuestoCategoria = new PuestoCategoriaExt();
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
	
	public void guardar(){
		this.resOperacion = "";
		
		try{
			if ( this.pojoPuestoCategoria.getId() == null ) {
				if( pojoPuesto.getId() != null && pojoCategoria.getId() != null) {
					//al no estar vacia, quiere decir que ya hay algun PuestoCategoria con ese puesto y esa categoría
					if (!this.ifzPuestoCategoria.findByPuestoCategoriaExt(pojoPuesto.getId().intValue(), pojoCategoria.getId().intValue()).isEmpty()){
						this.resOperacion = "Ya existe un Puesto-Categoría con estos valores";
						log.info(this.resOperacion);
						return;
					}
					
					PuestoCategoria puestoCategoria = new PuestoCategoria();
					
					puestoCategoria.setCreadoPor( this.usuarioId.longValue() );
					puestoCategoria.setFechaCreacion( Calendar.getInstance().getTime() );
					puestoCategoria.setModificadoPor( this.usuarioId.longValue() );
					puestoCategoria.setFechaModificacion( Calendar.getInstance().getTime() );
					puestoCategoria.setIdPuesto( this.pojoPuesto.getId().intValue() );
					puestoCategoria.setIdCategoria( this.pojoCategoria.getId().intValue() );
					puestoCategoria.setEstatus(0);
					puestoCategoria.setId( this.ifzPuestoCategoria.save( puestoCategoria ));
					
					// JAVAZO
					this.pojoPuestoCategoria.setCreadoPor( this.usuarioId.longValue() );
					this.pojoPuestoCategoria.setFechaCreacion( Calendar.getInstance().getTime() );
					this.pojoPuestoCategoria.setModificadoPor( this.usuarioId.longValue() );
					this.pojoPuestoCategoria.setFechaModificacion( Calendar.getInstance().getTime() );
					this.pojoPuestoCategoria.setPuesto( this.pojoPuesto);
					this.pojoPuestoCategoria.setCategoria(this.pojoCategoria);
					this.pojoPuestoCategoria.setEstatus(0);
					this.pojoPuestoCategoria.setId(puestoCategoria.getId());
					
					this.listaPuestosCategoriasGrid.add(this.pojoPuestoCategoria);
					
					this.setResOperacion("OK");
					tipoMensaje = 0;
					this.setBand(false);
				}
				
			}else{
				this.pojoPuestoCategoria.setModificadoPor(this.usuarioId.longValue());
				this.pojoPuestoCategoria.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoPuestoCategoria.setPuesto(this.pojoPuesto);
				this.pojoPuestoCategoria.setCategoria(this.pojoCategoria);
				
				this.ifzPuestoCategoria.update( this.pojoPuestoCategoria ); 
				for (PuestoCategoriaExt var : this.listaPuestosCategoriasGrid) {
					if (var.getId() == this.pojoPuestoCategoria.getId()) 
						var = this.pojoPuestoCategoria;
				}
				
				this.setResOperacion("OK");
				this.setBand(false);
				tipoMensaje = 0;
			}
		}catch(Exception e) {
			band=true;
			tipoMensaje=1;
			this.resOperacion = "ERROR";
			log.error("error al guardar", e);
			//return "ERROR";
		}

		this.resOperacion = "";
		//return "OK";
	}
	
	public String eliminar(){
		
		try {

			this.pojoPuestoCategoria.setModificadoPor( this.usuarioId.longValue() );
			this.pojoPuestoCategoria.setFechaModificacion( Calendar.getInstance().getTime() );

			this.pojoPuestoCategoria.setEstatus(1);
			
			this.ifzPuestoCategoria.update( this.pojoPuestoCategoria );
			
			
		} catch (Exception e) {
			log.error("Error en el metodo eliminar.", e);
			return "ERROR";
		}
		
		return "OK";
	}
	
	public String editar(){	//si se necesita
		this.resOperacion = "";
		return "OK";
	}
	
	public PuestoCategoriaExt getPojoPuestoCategoria() {
		return pojoPuestoCategoria;
	}

	public void setPojoPuestoCategoria(PuestoCategoriaExt pojoPuestoCategoria) {
		//Es donde se asigna el objeto para editar/eliminar
		this.pojoPuesto = pojoPuestoCategoria.getPuesto();
		this.pojoCategoria = pojoPuestoCategoria.getCategoria();
		
		this.idNuevoPuesto = pojoPuestoCategoria.getPuesto().getId();
		this.idNuevaCategoria = pojoPuestoCategoria.getCategoria().getId();
		
		this.pojoPuestoCategoria = pojoPuestoCategoria;
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

	public List<PuestoCategoriaExt> getListaPuestosCategoriasGrid() {
		return listaPuestosCategoriasGrid;
	}

	public void setListaPuestosCategoriasGrid(
			List<PuestoCategoriaExt> listaPuestosCategoriasGrid) {
		this.listaPuestosCategoriasGrid = listaPuestosCategoriasGrid;
	}
	
	//  -- ------------------------------------> El listade de puestos y el listado de categorias

	public void listarCategorias(){
		this.listaCategorias = this.ifzCategoria.findAll();
	}

	public void listarPuestos(){
		this.listaPuestos = this.ifzPuesto.findAll();
	}
	
	public List<Puesto> getListaPuestos() {
		return listaPuestos;
	}

	public void setListaPuestos(List<Puesto> listaPuestos) {
		this.listaPuestos = listaPuestos;
	}

	public List<Categoria> getListaCategorias() {
		return listaCategorias;
	}


	public void setListaCategorias(List<Categoria> listaCategorias) {
		this.listaCategorias = listaCategorias;
	}


	public List<SelectItem> getListaCboPuestos() {
		return listaCboPuestos;
	}


	public void setListaCboPuestos(List<SelectItem> listaCboPuestos) {
		this.listaCboPuestos = listaCboPuestos;
	}

	public long getIdPuesto() {
		return idPuesto;
	}


	public void setIdPuesto(long idPuesto) {
		this.idPuesto = idPuesto;
	}

	public List<SelectItem> getListaCboCategorias() {
		return listaCboCategorias;
	}

	public void setListaCboCategorias(List<SelectItem> listaCboCategorias) {
		this.listaCboCategorias = listaCboCategorias;
	}
	
	public long getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(long idCategoria) {
		this.idCategoria = idCategoria;
	}

	//Nuevo Editar Puesto
	
	public List<SelectItem> getListaCboNuevoPuesto() {
		return listaCboNuevoPuesto;
	}

	public void setListaCboNuevoPuesto(List<SelectItem> listaCboNuevoPuesto) {
		this.listaCboNuevoPuesto = listaCboNuevoPuesto;
	}

	public long getIdNuevoPuesto() {
		return idNuevoPuesto;
	}

	public void setIdNuevoPuesto(long idNuevoPuesto) {
		this.idNuevoPuesto = idNuevoPuesto;
		for(Puesto p: this.listaPuestos){
			if( p.getId() == idNuevoPuesto ){
				this.pojoPuesto = this.ifzPuesto.findById(idNuevoPuesto);
				break;
			}
		}
	}

	public List<SelectItem> getListaCboNuevaCategoria() {
		return listaCboNuevaCategoria;
	}

	public void setListaCboNuevaCategoria(List<SelectItem> listaCboNuevaCategoria) {
		this.listaCboNuevaCategoria = listaCboNuevaCategoria;
	}

	public long getIdNuevaCategoria() {
		return idNuevaCategoria;
	}

	public void setIdNuevaCategoria(long idNuevaCategoria) {
		this.idNuevaCategoria = idNuevaCategoria;
		for(Categoria c: this.listaCategorias){
			if( c.getId() == idNuevaCategoria ){
				this.pojoCategoria = this.ifzCategoria.findById(idNuevaCategoria);
				break;
			}
		}
	}
}


//HISTORIAL DE MODIFICACIONES 
//-------------------------------------------------------------------------------------------------------------------
//VERSION |    FECHA   |        AUTOR       | DESCRIPCION 
//-------------------------------------------------------------------------------------------------------------------
//  1.0   | 2016-09-20 | Javier Tirado      | Se reasigno el pojo principal de edicion al guardar un nevo PuestoCategoria en el metodo Guardar
