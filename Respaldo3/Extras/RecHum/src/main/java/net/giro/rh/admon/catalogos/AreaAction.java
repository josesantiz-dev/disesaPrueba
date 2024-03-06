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
import net.giro.rh.admon.beans.Area;
import net.giro.rh.admon.beans.AreaExt;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.AreaRem;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class AreaAction  implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AreaAction.class);
	
	boolean imprimirLog = true;
	
	private InitialContext ctx;
	
    private Integer usuarioId;	    
	private int numPagina;
	
	private AreaExt pojoArea;

	// ----------------------------- Variables para la búsqueda
	private String valorBusqueda;
	private int tipoMensaje;
	private boolean band;
	private String resOperacion;
	
	private AreaRem ifzArea;
	private EmpleadoRem ifzEmpleado;
	private List<AreaExt> listaAreasGrid;
	
	private String descripcion;
	
	//private long idResponsable;	//Parece que este no se ocupa
	
	private EmpleadoExt responsable;
	private SelectItem ItemChangeListener;
	
	//private SelectItem responsableSeleccionado;	//Verificar si este se utiliza
	private List<SelectItem> listResponsables;
	private List<EmpleadoExt> tmpListResponsables;
	
	public AreaAction() throws NamingException,Exception {
		
		listaAreasGrid = new ArrayList<>();

		this.tipoMensaje = 0;
		this.band = false;
		
		String ejbName;
		this.numPagina = 1;
	
		this.ctx = new InitialContext();

		ejbName = "ejb:/Logica_RecHum//AreaFac!net.giro.rh.admon.logica.AreaRem";
		this.ifzArea = (AreaRem) this.ctx.lookup(ejbName);
		
		ejbName = "ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem";
		this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup(ejbName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
		this.usuarioId = (int) lm.getUsuarioResponsabilidad().getUsuario().getId();
		
		//ValueExpression dato = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{entorno}", PropertyResourceBundle.class);
		//PropertyResourceBundle entornoProperties = (PropertyResourceBundle)dato.getValue(fc.getELContext());
		
		responsable = new EmpleadoExt();
		
		this.listResponsables = new ArrayList<SelectItem>();
		this.tmpListResponsables = new ArrayList<EmpleadoExt>();
		
		cargarResponsables();
		
	}

	public String editar(){		//Creo que es metodo nunca se ocupa. Se manda a llamar a guardar y se verifica contra el idArea
		
		this.resOperacion = "";
		return "OK";
	}
	
	public String nuevo(){
		try{
			band=false;
			tipoMensaje=0;
			this.resOperacion = "";
			this.descripcion = "";
			this.pojoArea = new AreaExt();
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
	
	public String buscar(){
		try{
			this.resOperacion = "";
			
			if( "".equals(this.valorBusqueda) ){	//id, descripcion, responsable
				this.listaAreasGrid = this.ifzArea.findAllExt(); 
			}else{
				this.listaAreasGrid = this.ifzArea.findByPropertyExt("descripcion", this.valorBusqueda);
			}
			
			if (this.listaAreasGrid.isEmpty()) {
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
	
	public String guardar(){
		this.resOperacion = "";
		try{
			if (this.pojoArea.getId() == null ){
				this.pojoArea.setDescripcion(this.descripcion);
				this.pojoArea.setCreadoPor( this.usuarioId.longValue() );
				this.pojoArea.setFechaCreacion( Calendar.getInstance().getTime() );
				this.pojoArea.setModificadoPor( this.usuarioId.longValue() );
				this.pojoArea.setFechaModificacion( Calendar.getInstance().getTime() );
				this.pojoArea.setResponsable(this.responsable);
				
				Area area = new Area();
				area.setDescripcion( this.pojoArea.getDescripcion() );
				area.setCreadoPor(this.pojoArea.getCreadoPor());
				area.setFechaCreacion(this.pojoArea.getFechaCreacion());
				area.setModificadoPor(this.pojoArea.getModificadoPor());
				area.setFechaModificacion(this.pojoArea.getFechaModificacion());
				area.setIdResponsable(0);
				
				if (this.responsable != null && this.responsable.getId() != null && this.responsable.getId() > 0L)
					area.setIdResponsable(this.responsable.getId().intValue());
				
				area.setId(this.ifzArea.save(area));
				
				this.pojoArea.setId(area.getId());
				this.listaAreasGrid.add(this.pojoArea);
				
				this.setResOperacion("OK");
				tipoMensaje = 0;
				this.setBand(false);
				
			} else {
				this.pojoArea.setDescripcion(this.descripcion);
				this.pojoArea.setModificadoPor(this.usuarioId.longValue());
				this.pojoArea.setFechaModificacion(Calendar.getInstance().getTime());
				this.pojoArea.setResponsable(this.responsable);
				
				Area area = new Area();
				area.setId(this.pojoArea.getId());
				area.setDescripcion( this.pojoArea.getDescripcion());
				area.setEstatus(this.pojoArea.getEstatus());
				area.setCreadoPor(this.pojoArea.getCreadoPor());
				area.setModificadoPor(this.pojoArea.getModificadoPor());
				area.setFechaCreacion(this.pojoArea.getFechaCreacion());
				area.setFechaModificacion(this.pojoArea.getFechaModificacion());
				area.setIdResponsable(0);
				
				if (this.responsable != null && this.responsable.getId() != null && this.responsable.getId() > 0L)
					area.setIdResponsable(this.responsable.getId().intValue());
				
				this.ifzArea.update(area);
				for (AreaExt var : this.listaAreasGrid) {
					if (var.getId() == pojoArea.getId()) {
						var = pojoArea;
						break;
					}
				}

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
	
	public String eliminar(){
		
		try {
			System.out.println("Actualizamos pojoArea");
			this.pojoArea.setEstatus(1);
			this.pojoArea.setModificadoPor(this.usuarioId.longValue() );
			this.pojoArea.setFechaModificacion(Calendar.getInstance().getTime());
			
			System.out.println("Generamos pojo area y asignamos datos");
			Area area = new Area();
			area.setId(this.pojoArea.getId());
			area.setDescripcion( this.pojoArea.getDescripcion());
			area.setEstatus(this.pojoArea.getEstatus());
			area.setCreadoPor(this.pojoArea.getCreadoPor());
			area.setModificadoPor(this.pojoArea.getModificadoPor());
			area.setFechaCreacion(this.pojoArea.getFechaCreacion());
			area.setFechaModificacion(this.pojoArea.getFechaModificacion());
			area.setIdResponsable(0);

			System.out.println("Comprobamos responsable");
			if (this.pojoArea.getResponsable() != null && this.pojoArea.getResponsable().getId() != null && this.pojoArea.getResponsable().getId() > 0L) {
				area.setIdResponsable(this.pojoArea.getResponsable().getId().intValue());
				System.out.println("Responsable asignado");
			}

			System.out.println("ActualizAmos en BD");
			this.ifzArea.update(area);

			System.out.println("Buscamos en lista");
			for(AreaExt var : this.listaAreasGrid ){	// Actualizamos en la lista
				if(var.getId().equals(area.getId())) {
					var.setEstatus(area.getEstatus());
					var.setModificadoPor(area.getModificadoPor());
					var.setFechaModificacion(area.getFechaModificacion());
					System.out.println("Actualizamos en lista");
					break;
				}
			}
			
			nuevo();
		} catch (Exception e) {
			log.error("Error en el metodo eliminar.", e);
			return "ERROR";
		}
		
		return "OK";
	}

	
	//	--------------------------------------------------------------------> Procesos para el combo de Responsables y su selección
	public List<SelectItem> getResponsables() {
		return this.listResponsables;
	}

	public void setResponsables(List<SelectItem> listResponsables) {
		this.listResponsables = listResponsables;
	}
	
	public long getIdResponsable() {
		if (this.responsable == null) this.responsable = new EmpleadoExt();
		return responsable.getId() != null ? responsable.getId() : 0L;
	}

	public void setIdResponsable(long idResponsable) {
		this.responsable = new EmpleadoExt();
		for(EmpleadoExt e : this.tmpListResponsables){
			if ( e.getId() == idResponsable ){
				EmpleadoExt empleado = this.ifzEmpleado.findByIdExt(idResponsable);
				this.responsable = empleado;
				break;
			}
		}
	}

	private void cargarResponsables() {
		if ( this.listResponsables.isEmpty()) {
			this.tmpListResponsables = this.ifzEmpleado.findAllExt();
			for(EmpleadoExt e : this.tmpListResponsables) {
				if (e.getId() == null || e.getPersona() == null) continue;
				this.listResponsables.add(new SelectItem(e.getId().toString(), e.getPersona().getNombre()));
			}
		}
	}
	
	public SelectItem itemChangeListener() {
		return ItemChangeListener;
	}

	public void setItemChangeListener(SelectItem itemChangeListener) {
		ItemChangeListener = itemChangeListener;
	}
	//	------------------------------------------------------------------------------------------------------------------------> 
	
	public AreaExt getPojoArea() {
		return pojoArea;
	}

	public void setPojoArea(AreaExt area) {
		
		this.pojoArea = area;
		descripcion = area.getDescripcion();
		this.responsable = area.getResponsable();
	}

	public List<AreaExt> getListaAreasGrid() {
		return listaAreasGrid;
	}

	public void setListaAreasGrid(List<AreaExt> listaAreasGrid) {
		this.listaAreasGrid = listaAreasGrid;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	
	@SuppressWarnings("unused")
	private void imprimirLog(String texto){
		if (this.imprimirLog)
			System.out.println(texto);
	}

}
