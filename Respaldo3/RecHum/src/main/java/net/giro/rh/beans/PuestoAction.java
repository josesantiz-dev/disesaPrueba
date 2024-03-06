package net.giro.rh.beans;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
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
	private LoginManager loginManager;
	private InitialContext ctx;
    private long usuarioId; 
	private int numPagina;
	private Puesto pojoPuesto;
	private String descripcion;
	private String valorBusqueda;
	private PuestoRem ifzPuesto;
	private List<Puesto> listaPuestosGrid;
	private boolean band;
	private int tipoMensaje;
	private String resOperacion;
	private String mensajeDetalles;
	
	public PuestoAction() throws NamingException,Exception {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application app = fc.getApplication();
		
		ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
		this.loginManager = (LoginManager) ve.getValue(fc.getELContext());
		this.usuarioId = this.loginManager.getUsuarioResponsabilidad().getUsuario().getId();
		
		this.ctx = new InitialContext();
		this.ifzPuesto = (PuestoRem) this.ctx.lookup("ejb:/Logica_RecHum//PuestoFac!net.giro.rh.admon.logica.PuestoRem");
		this.ifzPuesto.setInfoSesion(this.loginManager.getInfoSesion());
		
		this.pojoPuesto = new Puesto();
		this.listaPuestosGrid = new ArrayList<Puesto>();
		this.numPagina = 1;
		control();
	}

	public void buscar() {
		try {
			control();
			this.listaPuestosGrid = this.ifzPuesto.findLikeProperty("descripcion", this.valorBusqueda, false);
			if (this.listaPuestosGrid == null || this.listaPuestosGrid.isEmpty())
				control(-1, "La busqueda no devolvio resultados");
		} catch (Exception e) {
			control("Ocurrio un problema al consultar los Puestos", e);
		}
	}
	
	public void nuevo() {
		try {
			control();
			this.descripcion = "";
			this.pojoPuesto = new Puesto();
		} catch (Exception e) {
			control("Ocurrio un problema al preparar el nuevo Puesto", e);
		}
	}

	public void editar() {	//si se necesita
		control();
	}

	public void guardar() {
		try {
			control();
			if ("".equals(this.descripcion.trim())) {
				control(-1, "Debe indicar el nombre del puesto");
				return;
			}

			if (this.pojoPuesto.getIdEmpresa() <= 0L)
				this.pojoPuesto.setIdEmpresa(this.loginManager.getInfoSesion().getEmpresa().getId());
			
			if (this.pojoPuesto.getId() == null || this.pojoPuesto.getId() <= 0L) {
				this.pojoPuesto.setDescripcion(this.descripcion);
				this.pojoPuesto.setEstatus(0);
				this.pojoPuesto.setCreadoPor(this.usuarioId);
				this.pojoPuesto.setFechaCreacion(Calendar.getInstance().getTime());
				this.pojoPuesto.setModificadoPor(this.usuarioId);
				this.pojoPuesto.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.pojoPuesto.setId(this.ifzPuesto.save(this.pojoPuesto));
				this.listaPuestosGrid.add(this.pojoPuesto);
			} else {
				this.pojoPuesto.setDescripcion(this.descripcion);
				this.pojoPuesto.setModificadoPor(this.usuarioId);
				this.pojoPuesto.setFechaModificacion(Calendar.getInstance().getTime());
				
				this.ifzPuesto.update(this.pojoPuesto);
				for (Puesto var : this.listaPuestosGrid) {
					if (var.getId() == this.pojoPuesto.getId())
						var = this.pojoPuesto;
				}
			}
		} catch(Exception e) {
			control("Ocurrio un problema al intentar guardar el Puesto", e);
		}
	}
	
	public void eliminar() {
		try {
			control();
			this.pojoPuesto.setEstatus(1);
			this.pojoPuesto.setModificadoPor(this.usuarioId);
			this.pojoPuesto.setFechaModificacion(Calendar.getInstance().getTime());
			this.pojoPuesto.setDescripcion(this.pojoPuesto.getDescripcion());
			this.ifzPuesto.update(this.pojoPuesto);
		} catch (Exception e) {
			control("Ocurrio un problema al intentar eliminar el Puesto indicado.", e);
		}
	}

	private void control() {
		this.band = false;
		this.tipoMensaje = 0;
		this.resOperacion = "";
	}

	private void control(int tipoMensaje, String mensaje) {
		control(true, tipoMensaje, mensaje, null);
	}
	
	private void control(String mensaje, Throwable throwable) {
		control(true, -1, mensaje, throwable);
	}
	
	private void control(boolean operacionCancelada, int tipoMensaje, String mensaje, Throwable throwable) {
		if (mensaje == null || "".equals(mensaje.trim()))
			mensaje = "Se ha producido un error desconocido";
		
		this.band = operacionCancelada;
		this.tipoMensaje = tipoMensaje;
		this.resOperacion = mensaje.replace("\n", "<br/>");
		this.mensajeDetalles = "";
		
		if (throwable != null) {
			StringWriter sw = new StringWriter();
			throwable.printStackTrace(new PrintWriter(sw));
			this.mensajeDetalles = "<br><br>" + sw.toString();
		}
		
		log.error("\nPUESTO :: " + this.loginManager.getUsuarioResponsabilidad().getUsuario().getUsuario() + "\n" + this.resOperacion + this.mensajeDetalles, throwable);
	}
	
	// --------------------------------------------------------------------------------
	// PROPIEDADES
	// --------------------------------------------------------------------------------
	
	public Puesto getPojoPuesto() {
		return pojoPuesto;
	}

	public void setPojoPuesto(Puesto puesto) {
		this.pojoPuesto = puesto;
		this.descripcion = puesto.getDescripcion();
		
	}
	
	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
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
