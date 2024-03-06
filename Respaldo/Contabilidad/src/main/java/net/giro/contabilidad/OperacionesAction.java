package net.giro.contabilidad;

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

import org.apache.log4j.Logger;

import net.giro.contabilidad.beans.OperacionesExt;
import net.giro.contabilidad.logica.OperacionesRem;
import net.giro.navegador.LoginManager;
import net.giro.plataforma.seguridad.beans.Aplicacion;

@ViewScoped
@ManagedBean(name="operAction")
public class OperacionesAction implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(OperacionesAction.class);
	private InitialContext ctx;
	// Interfaces
	private OperacionesRem ifzOperaciones;
	// Listas
	private List<OperacionesExt> listOperaciones;
	private List<Aplicacion> listAplicaciones;
	// POJO's
	private OperacionesExt pojoOperacion;
	private OperacionesExt pojoOperacionBorrar;
	// Busqueda principal
	private List<SelectItem> tiposBusqueda;	
	private String campoBusqueda;
	private String valorBusqueda;
	private int numPagina;
	// Busqueda aplicaciones
	private List<SelectItem> tiposBusquedaApps;	
	private String campoBusquedaApps;
	private String valorBusquedaApps;
	private int numPaginaApps;
	// Variables de operacion
    private long usuarioId;
    private boolean operacion;
	private String mensaje;
	private int tipoMensaje;
	
	
	public OperacionesAction() {
		try {
			this.ctx = new InitialContext();
			this.ifzOperaciones = (OperacionesRem) this.ctx.lookup("ejb:/Logica_Contabilidad//OperacionesFac!net.giro.contabilidad.logica.OperacionesRem");
			
			FacesContext fc = FacesContext.getCurrentInstance();
			Application app = fc.getApplication();

			ValueExpression ve = app.getExpressionFactory().createValueExpression(fc.getELContext(), "#{loginManager}", LoginManager.class);
			LoginManager lm = (LoginManager) ve.getValue(fc.getELContext());
			this.usuarioId = lm.getUsuarioResponsabilidad().getUsuario().getId();

			// ----------------------------------------------------------------------------------
			// Inicializaciones
			// ----------------------------------------------------------------------------------
			
			// Listas
			this.listOperaciones = new ArrayList<OperacionesExt>();
			this.listAplicaciones= new ArrayList<Aplicacion>();
			
			// POJO's
			this.pojoOperacion = new OperacionesExt();
			this.pojoOperacionBorrar = new OperacionesExt();
			
			// Busqueda Principal
			this.tiposBusqueda = new ArrayList<SelectItem>();
			this.tiposBusqueda.add(new SelectItem("descripcion", "Descripcion"));
			this.tiposBusqueda.add(new SelectItem("aplicacionNombre", "Modulo"));
			this.tiposBusqueda.add(new SelectItem("id", "ID"));
			this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			this.valorBusqueda = "";
			this.numPagina = 1;
			
			// Busqueda aplicaciones
			this.tiposBusquedaApps = new ArrayList<SelectItem>();
			this.tiposBusquedaApps.add(new SelectItem("aplicacion", "Descripcion"));
			this.tiposBusquedaApps.add(new SelectItem("id", "ID"));
			this.campoBusquedaApps = this.tiposBusquedaApps.get(0).getValue().toString();
			this.valorBusquedaApps = "";
			this.numPaginaApps = 1;
		} catch (Exception e) {
			log.error("Error en constructor OperacionesAction", e);
			this.ctx = null;
		}
	}
	
	
	public void buscar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusqueda))
				this.campoBusqueda = this.tiposBusqueda.get(0).getValue().toString();
			
			this.listOperaciones = this.ifzOperaciones.findExtLikeProperty(this.campoBusqueda, this.valorBusqueda, 1000);
			if (this.listOperaciones.isEmpty()) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda vacia");
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesAction.buscar", e);
		}
	}
	
	public void nuevo() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			this.pojoOperacion = new OperacionesExt();
			this.pojoOperacionBorrar = null;
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesAction.buscar", e);
		}
	}
	
	public void editar() {}
	
	public void guardar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoOperacion != null) {
				this.pojoOperacion.setModificadoPor(this.usuarioId);
				this.pojoOperacion.setFechaModificacion(Calendar.getInstance().getTime());
				
				if (this.pojoOperacion.getId() == null || this.pojoOperacion.getId() <= 0L) {
					this.pojoOperacion.setCreadoPor(this.usuarioId);
					this.pojoOperacion.setFechaCreacion(Calendar.getInstance().getTime());
					
					// Guardamos en la BD
					this.pojoOperacion.setId(this.ifzOperaciones.save(this.pojoOperacion));
					// Agregamos a la lista
					this.listOperaciones.add(this.pojoOperacion);
				} else {
					// Actualizamos en la BD
					this.ifzOperaciones.update(this.pojoOperacion);
					
					// Actualizamo en la lista
					for(OperacionesExt var : this.listOperaciones) {
						if (var.getId().equals(this.pojoOperacion.getId())) {
							var.setDescripcion(this.pojoOperacion.getDescripcion());
							var.setIdModulo(this.pojoOperacion.getIdModulo());
							var.setModificadoPor(this.pojoOperacion.getModificadoPor());
							var.setFechaModificacion(this.pojoOperacion.getFechaModificacion());
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesAction.guardar", e);
		}
	}
	
	public void borrar() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			if (this.pojoOperacionBorrar != null && this.pojoOperacionBorrar.getId() > 0L) {
				// Borramos de la bd
				this.ifzOperaciones.delete(this.pojoOperacionBorrar.getId());
				
				// Borramos de la lista
				this.listOperaciones.remove(this.pojoOperacionBorrar);
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesAction.borrar", e);
		}
	}
	
	public void nuevaBusquedaAplicaciones() {
		if (this.listAplicaciones == null)
			this.listAplicaciones = new ArrayList<Aplicacion>();
		this.listAplicaciones.clear();
		
		this.campoBusquedaApps = tiposBusquedaApps.get(0).getValue().toString();
		this.valorBusquedaApps = "";
		numPaginaApps = 1;
	}
	
	public void buscarAplicaciones() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;
			
			if ("".equals(this.campoBusquedaApps))
				this.campoBusquedaApps = tiposBusquedaApps.get(0).getValue().toString();
			
			if ("".equals(this.valorBusquedaApps)) {
				this.listAplicaciones = this.ifzOperaciones.findAllAplicaciones();
			} else {
				this.listAplicaciones = this.ifzOperaciones.findAplicacionLikeProperty(this.campoBusquedaApps, this.valorBusquedaApps);
			}
			
			if (this.listAplicaciones == null || this.listAplicaciones.isEmpty()) {
				this.operacion = true;
				this.mensaje = "ERROR";
				this.tipoMensaje = 2;
				log.info("Busqueda vacia");
				return;
			}
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesAction.buscarAplicaciones", e);
		}
	}
	
	public void seleccionarAplicacion() {
		try {
			this.operacion = false;
			this.mensaje = "OK";
			this.tipoMensaje = 0;

			nuevaBusquedaAplicaciones();
		} catch (Exception e) {
			this.operacion = true;
			this.mensaje = "ERROR";
			this.tipoMensaje = 1;
			log.error("Error en OperacionesAction.seleccionarAplicacion", e);
		}
	}

	
	
	public List<OperacionesExt> getListOperaciones() {
		return listOperaciones;
	}

	public void setListOperaciones(List<OperacionesExt> listOperaciones) {
		this.listOperaciones = listOperaciones;
	}

	public List<Aplicacion> getListAplicaciones() {
		return listAplicaciones;
	}

	public void setListAplicaciones(List<Aplicacion> listAplicaciones) {
		this.listAplicaciones = listAplicaciones;
	}

	public OperacionesExt getPojoOperacion() {
		return pojoOperacion;
	}

	public void setPojoOperacion(OperacionesExt pojoOperacion) {
		this.pojoOperacion = pojoOperacion;
	}

	public OperacionesExt getPojoOperacionBorrar() {
		return pojoOperacionBorrar;
	}

	public void setPojoOperacionBorrar(OperacionesExt pojoOperacionBorrar) {
		this.pojoOperacionBorrar = pojoOperacionBorrar;
	}

	public List<SelectItem> getTiposBusqueda() {
		return tiposBusqueda;
	}

	public void setTiposBusqueda(List<SelectItem> tiposBusqueda) {
		this.tiposBusqueda = tiposBusqueda;
	}

	public String getCampoBusqueda() {
		return campoBusqueda;
	}

	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
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

	public List<SelectItem> getTiposBusquedaApps() {
		return tiposBusquedaApps;
	}

	public void setTiposBusquedaApps(List<SelectItem> tiposBusquedaApps) {
		this.tiposBusquedaApps = tiposBusquedaApps;
	}

	public String getCampoBusquedaApps() {
		return campoBusquedaApps;
	}

	public void setCampoBusquedaApps(String campoBusquedaApps) {
		this.campoBusquedaApps = campoBusquedaApps;
	}

	public String getValorBusquedaApps() {
		return valorBusquedaApps;
	}

	public void setValorBusquedaApps(String valorBusquedaApps) {
		this.valorBusquedaApps = valorBusquedaApps;
	}

	public int getNumPaginaApps() {
		return numPaginaApps;
	}

	public void setNumPaginaApps(int numPaginaApps) {
		this.numPaginaApps = numPaginaApps;
	}

	public boolean isOperacion() {
		return operacion;
	}

	public void setOperacion(boolean operacion) {
		this.operacion = operacion;
	}

	public String getMensaje() {
		return mensaje;
	}


	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}


	public int getTipoMensaje() {
		return tipoMensaje;
	}


	public void setTipoMensaje(int tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}
}
