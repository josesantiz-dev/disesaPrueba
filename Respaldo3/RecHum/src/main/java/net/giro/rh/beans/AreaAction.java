package net.giro.rh.beans;

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

import net.giro.navegador.LoginManager;
import net.giro.rh.admon.beans.AreaExt;
import net.giro.rh.admon.beans.Empleado;
import net.giro.rh.admon.beans.EmpleadoExt;
import net.giro.rh.admon.logica.AreaRem;
import net.giro.rh.admon.logica.EmpleadoRem;

import org.apache.log4j.Logger;

@ViewScoped
@ManagedBean(name="areaAction")
public class AreaAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AreaAction.class);
	private LoginManager loginManager;
	private InitialContext ctx;

	private AreaRem ifzArea;
	private List<AreaExt> listaAreasGrid;
	private AreaExt pojoArea;
	private String descripcion;
    private long usuarioId;
	private boolean band;
	private int tipoMensaje;
	private String resOperacion;
	private int numPagina;
	private boolean imprimirLog = true;
	private String valorBusqueda;
	// EMPLEADOS
	private EmpleadoRem ifzEmpleado;
	private List<Empleado> tmpListResponsables;
	private List<SelectItem> listResponsables;
	private long responsable;
	
	
	public AreaAction() {
		FacesContext fc = null;
		Application app = null;
		ValueExpression ve = null;
	
		try {
			fc = FacesContext.getCurrentInstance();
			app = fc.getApplication();

			ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
			
			this.ctx = new InitialContext();
			this.ifzArea = (AreaRem) this.ctx.lookup("ejb:/Logica_RecHum//AreaFac!net.giro.rh.admon.logica.AreaRem");
			this.ifzEmpleado = (EmpleadoRem) this.ctx.lookup("ejb:/Logica_RecHum//EmpleadoFac!net.giro.rh.admon.logica.EmpleadoRem");
			
			this.ifzArea.setInfoSesion(this.loginManager.getInfoSesion());
			this.ifzEmpleado.setInfoSesion(this.loginManager.getInfoSesion());
			
			this.responsable = 0L;
			this.listaAreasGrid = new ArrayList<>();
			this.tipoMensaje = 0;
			this.band = false;
			this.numPagina = 1;
			this.listResponsables = new ArrayList<SelectItem>();
			this.tmpListResponsables = new ArrayList<Empleado>();
			
			cargarResponsables();
		} catch (Exception e) {
			log.error("Error en constructor AreaAction", e);
			this.ctx = null;
		}
	}


	public void buscar() {
		try {
			this.resOperacion = "";
			this.listaAreasGrid = this.ifzArea.findLikePropertyExt("descripcion", this.valorBusqueda);
			if (this.listaAreasGrid == null || this.listaAreasGrid.isEmpty()) {
				log.error("La busqueda no devolvio resultados");
				this.resOperacion = "La busqueda no devolvio resultados.";
			}
		} catch (Exception e) {
			log.error("error al buscar", e);
			this.resOperacion = "ERROR";
		}
		
		this.resOperacion = "";
	}

	public void nuevo() {
		try {
			band=false;
			tipoMensaje=0;
			this.resOperacion = "";
			this.descripcion = "";
			this.pojoArea = new AreaExt();
		} catch (Exception e) {
			log.error("Error en el metodo nuevo.", e);
			band=true;
			tipoMensaje=1;
			this.resOperacion = "ERROR";
		}
		
		this.resOperacion = "OK";
	}
	
	public void editar() {		//Creo que es metodo nunca se ocupa. Se manda a llamar a guardar y se verifica contra el idArea
		this.resOperacion = "";
		this.descripcion = this.pojoArea.getDescripcion();
		if (this.pojoArea.getResponsable() != null)
			this.responsable = this.pojoArea.getResponsable().getId(); // this.ifzEmpleado.convertir(this.pojoArea.getResponsable());
	}
	
	public void guardar() {
		EmpleadoExt pojoResponsable = null;
		this.resOperacion = "";
		
		try {
			if (this.responsable > 0L) {
				pojoResponsable = this.ifzEmpleado.findByIdExt(this.responsable);
				this.pojoArea.setResponsable(pojoResponsable);
			}

			this.pojoArea.setDescripcion(this.descripcion);
			if (this.pojoArea.getIdEmpresa() == null || this.pojoArea.getIdEmpresa() <= 0L)
				this.pojoArea.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			
			if (this.pojoArea.getId() == null || this.pojoArea.getId() <= 0L) {
				this.pojoArea.setCreadoPor(this.usuarioId);
				this.pojoArea.setFechaCreacion( Calendar.getInstance().getTime() );
				this.pojoArea.setModificadoPor(this.usuarioId);
				this.pojoArea.setFechaModificacion( Calendar.getInstance().getTime() );
				
				this.pojoArea.setId(this.ifzArea.save(this.pojoArea));
				this.listaAreasGrid.add(this.pojoArea);
				
				this.setResOperacion("OK");
				tipoMensaje = 0;
				this.setBand(false);
			} else {
				this.pojoArea.setModificadoPor(this.usuarioId);
				this.pojoArea.setFechaModificacion( Calendar.getInstance().getTime() );
				
				this.ifzArea.update(this.pojoArea);
				for (AreaExt var : this.listaAreasGrid) {
					if (var.getId() == pojoArea.getId()) {
						var = this.pojoArea;
						break;
					}
				}

				this.setResOperacion("OK");
				tipoMensaje = 0;
				this.setBand(false);
			}
		} catch (Exception e) {
			log.error("error al guardar", e);
			band=true;
			tipoMensaje=1;
			this.resOperacion = "ERROR";
		}

		this.resOperacion = "";
	}
	
	public void eliminar() {
		try {
			log.info("Actualizamos pojoArea");
			this.pojoArea.setEstatus(1);
			this.pojoArea.setModificadoPor(this.usuarioId);
			this.pojoArea.setFechaModificacion(Calendar.getInstance().getTime());
			
			log.info("Generamos pojo area y asignamos datos");
			/*Area area = new Area();
			area.setId(this.pojoArea.getId());
			area.setDescripcion( this.pojoArea.getDescripcion());
			area.setEstatus(this.pojoArea.getEstatus());
			area.setCreadoPor(this.pojoArea.getCreadoPor());
			area.setModificadoPor(this.pojoArea.getModificadoPor());
			area.setFechaCreacion(this.pojoArea.getFechaCreacion());
			area.setFechaModificacion(this.pojoArea.getFechaModificacion());
			area.setIdResponsable(0);

			log.info("Comprobamos responsable");
			if (this.pojoArea.getResponsable() != null && this.pojoArea.getResponsable().getId() != null && this.pojoArea.getResponsable().getId() > 0L) {
				area.setIdResponsable(this.pojoArea.getResponsable().getId().intValue());
				log.info("Responsable asignado");
			}*/

			log.info("ActualizAmos en BD");
			this.ifzArea.update(this.pojoArea);

			log.info("Buscamos en lista");
			for (AreaExt var : this.listaAreasGrid) {	// Actualizamos en la lista
				if (var.getId().equals(this.pojoArea.getId())) {
					var.setEstatus(this.pojoArea.getEstatus());
					var.setModificadoPor(this.pojoArea.getModificadoPor());
					var.setFechaModificacion(this.pojoArea.getFechaModificacion());
					log.info("Actualizamos en lista");
					break;
				}
			}
			
			nuevo();
		} catch (Exception e) {
			log.error("Error en el metodo eliminar.", e);
		}
	}

	private void cargarResponsables() {
		if (this.tmpListResponsables == null)
			this.tmpListResponsables = new ArrayList<Empleado>();
		this.tmpListResponsables.clear();
		
		this.tmpListResponsables = this.ifzEmpleado.findAll();
		if (this.tmpListResponsables != null && ! this.tmpListResponsables.isEmpty()) {
			for (Empleado e : this.tmpListResponsables)
				this.listResponsables.add(new SelectItem(e.getId(), e.getNombre()));
		}
	}
	
	@SuppressWarnings("unused")
	private void imprimirLog(String texto){
		if (this.imprimirLog)
			log.info(texto);
	}
	
	//	------------------------------------------------------------------------------------------------------------------------>
	// PROPIEDADES
	//	------------------------------------------------------------------------------------------------------------------------> 

	public long getResponsable() {
		return responsable;
	}

	public void setResponsable(long responsable) {
		this.responsable = responsable;
	}

	public long getIdResponsable() {/*
		if (this.responsable == null) 
			this.responsable = new Empleado();
		return responsable.getId() != null ? responsable.getId() : 0L;*/
		return responsable;
	}

	public void setIdResponsable(long idResponsable) {
		this.responsable = idResponsable;/*new Empleado();
		for (Empleado e : this.tmpListResponsables) {
			if (e.getId() == idResponsable) {
				Empleado empleado = this.ifzEmpleado.findById(idResponsable);
				this.responsable = empleado;
				break;
			}
		}*/
	}

	public List<SelectItem> getResponsables() {
		return this.listResponsables;
	}

	public void setResponsables(List<SelectItem> listResponsables) {
		this.listResponsables = listResponsables;
	}
	
	/*public SelectItem itemChangeListener() {
		return ItemChangeListener;
	}

	public void setItemChangeListener(SelectItem itemChangeListener) {
		ItemChangeListener = itemChangeListener;
	}*/
	
	public AreaExt getPojoArea() {
		return pojoArea;
	}

	public void setPojoArea(AreaExt area) {
		this.pojoArea = area;
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
}
